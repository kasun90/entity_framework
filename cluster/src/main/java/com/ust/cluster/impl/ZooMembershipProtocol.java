package com.ust.cluster.impl;

import com.google.common.base.Preconditions;
import com.ust.cluster.ex.MembershipException;
import com.ust.cluster.core.Member;
import com.ust.cluster.core.MemberListener;
import com.ust.cluster.core.MembershipEvent;
import com.ust.cluster.core.MembershipProtocol;
import com.ust.common.GsonCodec;
import java.io.UnsupportedEncodingException;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;
import net.openhft.hashing.LongHashFunction;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.utils.ZKPaths;

import org.apache.zookeeper.CreateMode;

public class ZooMembershipProtocol implements MembershipProtocol {

    private final CuratorFramework client;
    private final GsonCodec codec;
    private final Map<String, GroupHandler> mapHnd;

    private ZooMembershipProtocol(CuratorFramework client) {
        this.client = client;
        codec = new GsonCodec();
        mapHnd = new ConcurrentHashMap<>();
    }

    @Override
    public Member addMember(String group, String memberName, Map<String, String> tags) {
        try {
            tags.put(Member.ATTR_ID, String.valueOf(LongHashFunction.xx_r39().hashChars(UUID.randomUUID().toString())));
            Member member = new Member(group, memberName, tags);
            String node = codec.encode(member);
            client.create().creatingParentContainersIfNeeded().withMode(CreateMode.EPHEMERAL)
                .forPath(ZKPaths.makePath(group, memberName), node.getBytes("UTF-8"));
            getHnd(group).addMember(member);
            return member;
        } catch (Exception ex) {
            throw new MembershipException(ex);
        }
    }

    @Override
    public void updateMember(Member member, Map<String, String> tags) {
        Preconditions.checkArgument(getHnd(member.getGroup()).isExist(member));
        try {
            member.getMeta().putAll(tags);
            String node = codec.encode(member);
            client.setData().forPath(ZKPaths.makePath(member.getGroup(), member.getName()), node.getBytes("UTF-8"));
        } catch (Exception ex) {
            throw new MembershipException(ex);
        }
    }

    @Override
    public void removeMember(Member member) {
        try {
            client.delete().forPath(ZKPaths.makePath(member.getGroup(), member.getName()));
            getHnd(member.getGroup()).removeMember(member);
        } catch (Exception ex) {
            throw new MembershipException(ex);
        }
    }

    @Override
    public Collection<Member> getMembers(String group) {
        return getHnd(group).getMembers();
    }

    private GroupHandler getHnd(String group) {
        return mapHnd.computeIfAbsent(group, gr -> createGroup(gr));
    }

    private GroupHandler createGroup(String group) {
        return new GroupHandler(client, codec, group);
    }

    @Override
    public void listen(String group, MemberListener listener) {
        getHnd(group).listen(listener);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private CuratorFramework client;

        public Builder() {
        }

        public Builder withClient(CuratorFramework client) {
            this.client = client;
            return this;
        }

        public ZooMembershipProtocol build() {
            return new ZooMembershipProtocol(client);
        }
    }

    public static final class GroupHandler {

        private final String group;
        private final PathChildrenCache childrenCache;
        private final Set<Member> members;
        private final GsonCodec codec;
        private final Set<MemberListener> listeners;

        protected GroupHandler(CuratorFramework client, GsonCodec codec, String group) {
            this.codec = codec;
            this.group = group;
            this.childrenCache = new PathChildrenCache(client, ZKPaths.makePath("/", group), true);
            members = new ConcurrentSkipListSet<>();
            listeners = new ConcurrentSkipListSet<>();
            initListener();
        }

        private Member convertToMember(ChildData child) {
            try {
                String data = new String(child.getData(), "UTF-8");
                Member member = codec.decode(Member.class, data);
                return member;
            } catch (UnsupportedEncodingException ex) {
                throw new MembershipException(ex);
            }
        }

        protected void addMember(Member member) {
            members.add(member);
        }

        protected void removeMember(Member member) {
            members.remove(member);
        }

        protected Collection<Member> getMembers() {
            return members;
        }

        protected boolean isExist(Member member) {
            return members.contains(member);
        }

        protected void listen(MemberListener listener) {
            listeners.add(listener);
        }

        private void trigger(MembershipEvent event) {
            for (MemberListener listener : listeners) {
                try {
                    listener.onEvent(event);
                    System.out.println("event "+event.toString());
                } catch (Exception ex) {

                }
            }
        }

        private void initListener() {
            childrenCache.getListenable().addListener((PathChildrenCacheListener) new PathChildrenCacheListener() {
                @Override
                public void childEvent(CuratorFramework cf, PathChildrenCacheEvent evt) throws Exception {
                    switch (evt.getType()) {
                        case INITIALIZED: {
                            List<Member> members = evt.getInitialData().stream()
                                .map(child -> convertToMember(child)).collect(Collectors.toList());
                            GroupHandler.this.members.addAll(members);
                            break;
                        }
                        case CHILD_ADDED: {
                            Member member = convertToMember(evt.getData());
                            members.add(member);
                            trigger(new MembershipEvent(MembershipEvent.EventType.ADDED, member));
                            break;
                        }
                        case CHILD_REMOVED: {
                            String node = ZKPaths.getNodeFromPath(evt.getData().getPath());
                            Member member = convertToMember(evt.getData());
                            members.remove(member);
                            trigger(new MembershipEvent(MembershipEvent.EventType.REMOVED, member));
                            break;
                        }
                        case CHILD_UPDATED: {
                            Member member = convertToMember(evt.getData());
                            members.remove(member);
                            members.add(member);
                            trigger(new MembershipEvent(MembershipEvent.EventType.UPDATED, member));
                            break;
                        }
                        default:
                            break;
                    }
                }
            });
            try {
                this.childrenCache.start(PathChildrenCache.StartMode.NORMAL);
            } catch (Exception ex) {
                throw new MembershipException(ex);
            }
        }

    }
}

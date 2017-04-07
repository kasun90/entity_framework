package com.ust.cluster.membership.zoo;

import com.ust.cluster.impl.ZooMembershipProtocol;
import com.ust.cluster.core.AttributeMap;
import com.ust.cluster.core.Member;
import com.ust.cluster.core.MembershipEvent;
import com.ust.cluster.core.MembershipProtocol;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BooleanSupplier;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.After;
import org.junit.Before;
import org.apache.curator.test.TestingServer;
import org.junit.Assert;
import org.junit.Test;

public class ZooMembershipProtocolTest {

    private TestingServer server;
    private CuratorFramework client;

    public ZooMembershipProtocolTest() {
    }

    @Before
    public void setUp() throws Exception {
        server = new TestingServer(33333, true);
        client = CuratorFrameworkFactory.builder().namespace("TestEnvironment")
            .connectString("127.0.0.1:33333").retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
        client.start();
    }

    @After
    public void tearDown() throws IOException {
        server.close();
        client.close();
    }

    @Test
    public void add_membership_test() throws InterruptedException {
        List<MembershipEvent> listenEvents = new LinkedList<>();
        MembershipProtocol membership1 = ZooMembershipProtocol
            .builder().withClient(client).build();
        membership1.listen("TestGroup", (event) -> listenEvents.add(event));
        Member member = membership1.addMember("TestGroup", "Test", AttributeMap.builder()
            .attr("ip", "127.0.0.1").attr("port", 12345).build().map());

        MembershipProtocol membership2 = ZooMembershipProtocol
            .builder().withClient(client).build();

        wait_if(() -> membership2.getMembers("TestGroup").isEmpty());

        Assert.assertEquals(member, listenEvents.iterator().next().getMember());
        Assert.assertEquals(MembershipEvent.EventType.ADDED, listenEvents.iterator().next().getEventType());
        Assert.assertTrue(membership2.getMembers("TestGroup").iterator().next().equals(member));
        Assert.assertEquals(membership2.getMembers("TestGroup").iterator().next().getId(), member.getId());
        Assert.assertEquals(membership2.getMembers("TestGroup").iterator().next().getMeta("ip"), member.getMeta("ip"));
    }

    @Test
    public void update_member_test() throws InterruptedException {
        List<MembershipEvent> listenEvents1 = new LinkedList<>();
        List<MembershipEvent> listenEvents2 = new LinkedList<>();
        MembershipProtocol membership1 = ZooMembershipProtocol
            .builder().withClient(client).build();
        MembershipProtocol membership2 = ZooMembershipProtocol
            .builder().withClient(client).build();
        membership1.listen("TestGroup", (event) -> listenEvents1.add(event));
        membership2.listen("TestGroup", (event) -> listenEvents2.add(event));
        Member member = membership1.addMember("TestGroup", "Test", AttributeMap.builder()
            .attr("ip", "127.0.0.1").attr("port", 12345).build().map());

        wait_if(() -> membership1.getMembers("TestGroup").isEmpty());

        membership1.updateMember(member, AttributeMap.builder().attr("ip", "192.168.1.1").build().map());

        wait_if(() -> membership2.getMembers("TestGroup").isEmpty());
        wait_if(()->listenEvents1.size() != 2);
        Iterator<MembershipEvent> iterator1 = listenEvents1.iterator();
        Iterator<MembershipEvent> iterator2 = listenEvents2.iterator();

        Assert.assertEquals(iterator1.next().getMember(), iterator2.next().getMember());
        Assert.assertEquals(iterator1.next().getMember(), iterator2.next().getMember());
        Assert.assertTrue(membership2.getMembers("TestGroup").iterator().next().equals(member));
        Assert.assertEquals(membership2.getMembers("TestGroup").iterator().next().getId(), member.getId());
        Assert.assertEquals("192.168.1.1", membership2.getMembers("TestGroup").iterator().next().getMeta("ip"));
    }

    @Test(expected = com.ust.cluster.ex.MembershipException.class)
    public void add_duplicate_member_creation_test() {
        MembershipProtocol membership1 = ZooMembershipProtocol
            .builder().withClient(client).build();
        membership1.addMember("TestGroup", "Test", AttributeMap.builder().attr("ip", "127.0.0.1").build().map());
        membership1.addMember("TestGroup", "Test", AttributeMap.builder().attr("ip", "127.0.0.1").build().map());
    }

    @Test
    public void remove_member_test() throws InterruptedException {
        MembershipProtocol membership1 = ZooMembershipProtocol
            .builder().withClient(client).build();

        Member member = membership1.addMember("TestGroup", "Test", AttributeMap.builder().attr("ip", "127.0.0.1").build().map());

        MembershipProtocol membership2 = ZooMembershipProtocol
            .builder().withClient(client).build();
        wait_if(() -> membership2.getMembers("TestGroup").isEmpty());
        membership1.removeMember(member);
        wait_if(() -> !membership2.getMembers("TestGroup").isEmpty());
        Assert.assertTrue(membership2.getMembers("TestGroup").isEmpty());
    }

    @Test(expected = com.ust.cluster.ex.MembershipException.class)
    public void remove_already_deleted_member_test() {
        MembershipProtocol membership1 = ZooMembershipProtocol
            .builder().withClient(client).build();

        Member member = membership1.addMember("TestGroup", "Test", AttributeMap.builder().attr("ip", "127.0.0.1").build().map());
        membership1.removeMember(member);
        membership1.removeMember(member);
    }

    @Test(expected = IllegalArgumentException.class)
    public void update_non_exist_member_test() {
        MembershipProtocol membership1 = ZooMembershipProtocol
            .builder().withClient(client).build();
        membership1.updateMember(new Member("xxxx", "xxxx",
            AttributeMap.builder().attr(Member.ATTR_ID, "xxxx").build().map()), Collections.EMPTY_MAP);

    }

    private void wait_if(BooleanSupplier supplier) throws InterruptedException {
        int count = 0;
        while (supplier.getAsBoolean()) {
            Thread.sleep(1000);
            count++;
            if (count == 10) {
                Assert.fail("too much time in a loop");
            }
        }
    }
}

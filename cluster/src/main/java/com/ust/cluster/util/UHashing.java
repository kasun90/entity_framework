/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ust.cluster.util;

import static com.google.common.base.Preconditions.checkArgument;
import com.google.common.hash.HashCode;

/**
 *
 * @author nuwan use google consistent hashing algo. but customized to remove the garbage creation
 */
public class UHashing {

    public static int consistentHash(HashCode hashCode, int buckets) {
        return consistentHash(hashCode.padToLong(), buckets);
    }
    
    public static int consistentHash(long input, int buckets) {
    checkArgument(buckets > 0, "buckets must be positive: %s", buckets);
    int candidate = 0;
    int next;
    long state = input;
    // Jump from bucket to bucket until we go out of range
    while (true) {
      state = 2862933555777941757L * state + 1;
      next = (int) ((candidate + 1) / (((double) ((int) (state >>> 33) + 1)) / (0x1.0p31)));
      if (next >= 0 && next < buckets) {
        candidate = next;
      } else {
        return candidate;
      }
    }
  }
}

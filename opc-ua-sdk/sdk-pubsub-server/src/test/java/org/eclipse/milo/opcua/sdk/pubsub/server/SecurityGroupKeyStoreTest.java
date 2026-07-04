/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.server;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.time.InstantSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Set;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfigValidationException;
import org.eclipse.milo.opcua.sdk.pubsub.config.SecurityGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.security.PubSubSecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.junit.jupiter.api.Test;

/**
 * Rotation determinism for {@link SecurityGroupKeyStore}: token id as a pure function of time,
 * window clamping, key stability inside the retention window, pruning, and the construction-time
 * guards (unusable lifetime, unsupported policy, duplicate group ids).
 */
class SecurityGroupKeyStoreTest {

  private static final Duration LIFETIME = Duration.ofMinutes(10);

  private final TestClock clock = new TestClock();
  private final CountingKeyGenerator keyGenerator = new CountingKeyGenerator();

  private SecurityGroupKeyStore newStore(SecurityGroupConfig... groups) {
    return new SecurityGroupKeyStore(List.of(groups), clock, keyGenerator);
  }

  private static SecurityGroupConfig.Builder group(String name) {
    return SecurityGroupConfig.builder(name).keyLifeTime(LIFETIME);
  }

  @Test
  void currentKeyAtBase() {
    SecurityGroupKeyStore store = newStore(group("g").build());

    SecurityGroupKeyStore.SecurityKeys served = store.getSecurityKeys("g", 0, 0).orElseThrow();

    assertEquals(1, served.firstTokenId());
    assertEquals(1, served.keys().size());
    // null securityPolicyUri defaults to PubSub-Aes256-CTR: 68-byte key data
    assertEquals(PubSubSecurityPolicy.PubSubAes256Ctr.getUri(), served.securityPolicyUri());
    assertEquals(68, served.keys().get(0).length());
    assertEquals(LIFETIME.toMillis(), served.timeToNextKeyMillis());
    assertEquals(LIFETIME.toMillis(), served.keyLifetimeMillis());
  }

  @Test
  void tokenIdIsAFunctionOfTime() {
    SecurityGroupKeyStore store = newStore(group("g").build());

    clock.advance(LIFETIME.multipliedBy(3).dividedBy(2)); // 1.5x lifetime

    SecurityGroupKeyStore.SecurityKeys served = store.getSecurityKeys("g", 0, 0).orElseThrow();

    assertEquals(2, served.firstTokenId());
    assertEquals(LIFETIME.toMillis() / 2.0, served.timeToNextKeyMillis());
  }

  @Test
  void firstTokenIdIsMonotonicAcrossRotations() {
    SecurityGroupKeyStore store = newStore(group("g").build());

    long previousTokenId = 0;
    for (int rotation = 0; rotation < 5; rotation++) {
      SecurityGroupKeyStore.SecurityKeys served = store.getSecurityKeys("g", 0, 0).orElseThrow();

      assertEquals(previousTokenId + 1, served.firstTokenId());

      // a repeated request at the same instant serves the same token id
      assertEquals(
          served.firstTokenId(), store.getSecurityKeys("g", 0, 0).orElseThrow().firstTokenId());

      previousTokenId = served.firstTokenId();
      clock.advance(LIFETIME);
    }
  }

  @Test
  void requestedKeyCountIsCappedByTheFutureWindow() {
    SecurityGroupKeyStore store =
        newStore(group("g").maxPastKeyCount(uint(2)).maxFutureKeyCount(uint(3)).build());

    clock.advance(LIFETIME.multipliedBy(9)); // currentTokenId = 10

    SecurityGroupKeyStore.SecurityKeys served = store.getSecurityKeys("g", 0, 5).orElseThrow();

    // 5 keys beyond the first requested, but the window ends at 10 + 3
    assertEquals(10, served.firstTokenId());
    assertEquals(4, served.keys().size());
  }

  @Test
  void startingTokenIdClampsIntoTheWindow() {
    SecurityGroupKeyStore store =
        newStore(group("g").maxPastKeyCount(uint(2)).maxFutureKeyCount(uint(3)).build());

    clock.advance(LIFETIME.multipliedBy(9)); // currentTokenId = 10

    // stale id clamps up to currentTokenId - maxPastKeyCount
    SecurityGroupKeyStore.SecurityKeys past = store.getSecurityKeys("g", 5, 0).orElseThrow();
    assertEquals(8, past.firstTokenId());
    assertEquals(1, past.keys().size());

    // far-future id clamps down to currentTokenId + maxFutureKeyCount
    SecurityGroupKeyStore.SecurityKeys future = store.getSecurityKeys("g", 20, 0).orElseThrow();
    assertEquals(13, future.firstTokenId());
    assertEquals(1, future.keys().size());

    // an id inside [currentTokenId - maxPast, currentTokenId + maxFuture] is served exactly
    SecurityGroupKeyStore.SecurityKeys inWindow = store.getSecurityKeys("g", 9, 0).orElseThrow();
    assertEquals(9, inWindow.firstTokenId());
  }

  @Test
  void keysAreStableInsideTheRetentionWindow() {
    SecurityGroupKeyStore store =
        newStore(group("g").maxPastKeyCount(uint(2)).maxFutureKeyCount(uint(3)).build());

    SecurityGroupKeyStore.SecurityKeys first = store.getSecurityKeys("g", 0, 3).orElseThrow();
    SecurityGroupKeyStore.SecurityKeys second = store.getSecurityKeys("g", 0, 3).orElseThrow();

    assertEquals(first.keys(), second.keys());
    // each token's key was generated exactly once and served from the cache thereafter
    for (long tokenId = 1; tokenId <= 4; tokenId++) {
      assertEquals(1, keyGenerator.generateCount("g", tokenId));
    }

    // overlapping consecutive calls agree on shared token ids (FirstTokenId dedup contract)
    clock.advance(LIFETIME); // currentTokenId = 2
    SecurityGroupKeyStore.SecurityKeys third = store.getSecurityKeys("g", 0, 3).orElseThrow();
    assertEquals(2, third.firstTokenId());
    assertEquals(first.keys().subList(1, 4), third.keys().subList(0, 3));
  }

  @Test
  void distinctGroupsGetIndependentKeys() {
    SecurityGroupKeyStore store = newStore(group("a").build(), group("b").build());

    SecurityGroupKeyStore.SecurityKeys a = store.getSecurityKeys("a", 0, 0).orElseThrow();
    SecurityGroupKeyStore.SecurityKeys b = store.getSecurityKeys("b", 0, 0).orElseThrow();

    // same token id, but the key material is generated per group
    assertEquals(a.firstTokenId(), b.firstTokenId());
    assertNotEquals(a.keys().get(0), b.keys().get(0));

    // each group's key was generated exactly once, keyed by its own group id
    assertEquals(1, keyGenerator.generateCount("a", 1));
    assertEquals(1, keyGenerator.generateCount("b", 1));
  }

  @Test
  void tokensBehindTheRetentionWindowArePruned() {
    SecurityGroupKeyStore store =
        newStore(group("g").maxPastKeyCount(uint(2)).maxFutureKeyCount(uint(0)).build());

    store.getSecurityKeys("g", 0, 0).orElseThrow(); // generates token 1
    assertTrue(store.cachedTokenIds("g").contains(1L));

    clock.advance(LIFETIME.multipliedBy(10)); // currentTokenId = 11, retention floor = 9
    store.getSecurityKeys("g", 0, 0).orElseThrow();

    NavigableSet<Long> cached = store.cachedTokenIds("g");
    assertFalse(cached.contains(1L));
    assertTrue(cached.first() >= 9L);
  }

  @Test
  void replaceGroupsRefreshesTheServingWindowOnRetainedGroups() {
    SecurityGroupKeyStore store =
        newStore(group("g").maxPastKeyCount(uint(2)).maxFutureKeyCount(uint(3)).build());

    clock.advance(LIFETIME.multipliedBy(9)); // currentTokenId = 10

    // baseline: the construction-time window governs (past clamp at 10 - 2 = 8)
    assertEquals(8, store.getSecurityKeys("g", 5, 0).orElseThrow().firstTokenId());
    ByteString currentKeyBefore = store.getSecurityKeys("g", 0, 0).orElseThrow().keys().get(0);

    // a count-only change on a RETAINED group (not in the invalidated set — count changes are
    // deliberately not an invalidation trigger): the applied window governs from now on ...
    store.replaceGroups(
        List.of(group("g").maxPastKeyCount(uint(0)).maxFutureKeyCount(uint(1)).build()), Set.of());

    SecurityGroupKeyStore.SecurityKeys past = store.getSecurityKeys("g", 5, 0).orElseThrow();
    assertEquals(10, past.firstTokenId(), "the applied MaxPastKeyCount must clamp the window");

    SecurityGroupKeyStore.SecurityKeys future = store.getSecurityKeys("g", 20, 5).orElseThrow();
    assertEquals(11, future.firstTokenId(), "the applied MaxFutureKeyCount must cap the window");
    assertEquals(1, future.keys().size());

    // ... while the keys and the rotation base are untouched: the same token id serves the same
    // byte-identical key, generated exactly once
    SecurityGroupKeyStore.SecurityKeys current = store.getSecurityKeys("g", 0, 0).orElseThrow();
    assertEquals(10, current.firstTokenId());
    assertEquals(currentKeyBefore, current.keys().get(0));
    assertEquals(1, keyGenerator.generateCount("g", 10));
  }

  @Test
  void unusableKeyLifetimeFallsBackToTheDefault() {
    SecurityGroupKeyStore store = newStore(group("g").keyLifeTime(Duration.ZERO).build());

    SecurityGroupKeyStore.SecurityKeys served = store.getSecurityKeys("g", 0, 0).orElseThrow();

    assertEquals(SecurityGroupKeyStore.DEFAULT_KEY_LIFETIME.toMillis(), served.keyLifetimeMillis());
    assertEquals(1, served.firstTokenId());
  }

  @Test
  void keyDataLengthFollowsThePolicy() {
    SecurityGroupKeyStore store =
        newStore(
            group("g").securityPolicyUri(PubSubSecurityPolicy.PubSubAes128Ctr.getUri()).build());

    SecurityGroupKeyStore.SecurityKeys served = store.getSecurityKeys("g", 0, 0).orElseThrow();

    assertEquals(PubSubSecurityPolicy.PubSubAes128Ctr.getUri(), served.securityPolicyUri());
    assertEquals(52, served.keys().get(0).length());
  }

  @Test
  void unsupportedPolicyUriFailsConstruction() {
    SecurityGroupConfig config =
        group("g").securityPolicyUri("http://opcfoundation.org/UA/SecurityPolicy#None").build();

    assertThrows(PubSubConfigValidationException.class, () -> newStore(config));
  }

  @Test
  void duplicateSecurityGroupIdFailsConstruction() {
    SecurityGroupConfig a = group("a").securityGroupId("dup").build();
    SecurityGroupConfig b = group("b").securityGroupId("dup").build();

    assertThrows(PubSubConfigValidationException.class, () -> newStore(a, b));
  }

  @Test
  void unknownGroupIsEmpty() {
    SecurityGroupKeyStore store = newStore(group("g").build());

    assertTrue(store.getSecurityKeys("unknown", 0, 0).isEmpty());
  }

  @Test
  void tokenIdClampsAtUInt32Max() {
    SecurityGroupKeyStore store = newStore(group("g").keyLifeTime(Duration.ofMillis(1)).build());

    clock.advance(Duration.ofMillis(1L << 33)); // token arithmetic exceeds 2^32 - 1

    SecurityGroupKeyStore.SecurityKeys served = store.getSecurityKeys("g", 0, 0).orElseThrow();

    assertEquals(0xFFFF_FFFFL, served.firstTokenId());
  }

  @Test
  void defaultGeneratorProducesPolicySizedKeys() {
    SecurityGroupKeyStore store =
        new SecurityGroupKeyStore(List.of(group("g").maxFutureKeyCount(uint(1)).build()));

    SecurityGroupKeyStore.SecurityKeys served = store.getSecurityKeys("g", 0, 1).orElseThrow();

    assertEquals(2, served.keys().size());
    served.keys().forEach(key -> assertEquals(68, key.length()));
    assertFalse(served.keys().get(0).equals(served.keys().get(1)));
  }

  /** A mutable, deterministic {@link InstantSource}. */
  private static final class TestClock implements InstantSource {

    private Instant now = Instant.parse("2026-01-01T00:00:00Z");

    @Override
    public Instant instant() {
      return now;
    }

    void advance(Duration duration) {
      now = now.plus(duration);
    }
  }

  /**
   * A deterministic generator that bakes the (group id, token id) pair into the bytes and counts
   * invocations: distinct pairs get distinct bytes, repeated pairs get identical bytes.
   */
  private static final class CountingKeyGenerator implements SecurityGroupKeyStore.KeyGenerator {

    private final Map<String, Integer> counts = new HashMap<>();

    @Override
    public ByteString generate(String securityGroupId, long tokenId, int length) {
      counts.merge(securityGroupId + ":" + tokenId, 1, Integer::sum);

      byte[] seed = (securityGroupId + ":" + tokenId).getBytes(StandardCharsets.UTF_8);
      byte[] bytes = new byte[length];
      for (int i = 0; i < length; i++) {
        bytes[i] = (byte) (seed[i % seed.length] + i);
      }
      return ByteString.of(bytes);
    }

    int generateCount(String securityGroupId, long tokenId) {
      return counts.getOrDefault(securityGroupId + ":" + tokenId, 0);
    }
  }
}

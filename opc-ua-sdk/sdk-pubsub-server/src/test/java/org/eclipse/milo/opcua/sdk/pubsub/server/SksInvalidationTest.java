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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.time.Instant;
import java.time.InstantSource;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import org.eclipse.milo.opcua.sdk.pubsub.ReconfigureResult;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.SecurityGroupConfig;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.junit.jupiter.api.Test;

/**
 * The R7/S15 SKS-side key invalidation (Part 14 §6.2.12.2): {@link
 * SksServerFace#invalidatedGroupIds} computes the invalidated set (SecurityPolicyUri or KeyLifetime
 * changed, group removed), {@link SecurityGroupKeyStore#replaceGroups} keeps retained groups'
 * rotation state and served keys undisturbed while invalidated groups get a fresh rotation base and
 * regenerated keys, replacement-set violations keep the old state (never throw from a hook), and
 * {@link SksServerFace#onConfigurationApplied} wires the two together.
 */
class SksInvalidationTest {

  private static final Duration LIFETIME = Duration.ofHours(1);

  private static SecurityGroupConfig group(String id, Duration lifetime) {
    return SecurityGroupConfig.builder(id).keyLifeTime(lifetime).build();
  }

  private static PubSubConfig configWith(SecurityGroupConfig... groups) {
    PubSubConfig.Builder builder = PubSubConfig.builder();
    for (SecurityGroupConfig group : groups) {
      builder.securityGroup(group);
    }
    return builder.build();
  }

  /** A generator producing unique key data per invocation, so regeneration is observable. */
  private static final class CountingGenerator implements SecurityGroupKeyStore.KeyGenerator {

    private final AtomicLong counter = new AtomicLong();

    @Override
    public ByteString generate(String securityGroupId, long tokenId, int length) {
      byte[] bytes = new byte[length];
      long value = counter.incrementAndGet();
      bytes[0] = (byte) value;
      bytes[1] = (byte) (value >> 8);
      return ByteString.of(bytes);
    }
  }

  @Test
  void invalidatedGroupIdsFollowsSection62122() {
    PubSubConfig oldConfig =
        configWith(
            group("unchanged", LIFETIME),
            group("policy-changed", LIFETIME),
            group("lifetime-changed", LIFETIME),
            group("removed", LIFETIME));

    PubSubConfig newConfig =
        configWith(
            group("unchanged", LIFETIME),
            SecurityGroupConfig.builder("policy-changed")
                .keyLifeTime(LIFETIME)
                .securityPolicyUri("http://opcfoundation.org/UA/SecurityPolicy#PubSub-Aes128-CTR")
                .build(),
            group("lifetime-changed", Duration.ofMinutes(5)),
            group("added", LIFETIME));

    Set<String> invalidated = SksServerFace.invalidatedGroupIds(oldConfig, newConfig);

    // changed policy/lifetime and removed groups are invalidated; unchanged and brand-new are
    // not (a new group has no existing keys to invalidate)
    assertEquals(Set.of("policy-changed", "lifetime-changed", "removed"), invalidated);
  }

  @Test
  void replaceGroupsKeepsRetainedStateAndRegeneratesInvalidated() {
    var clock = new AtomicLong(1_000_000L);
    InstantSource instantSource = () -> Instant.ofEpochMilli(clock.get());

    var store =
        new SecurityGroupKeyStore(
            List.of(group("retained", LIFETIME), group("invalidated", LIFETIME)),
            instantSource,
            new CountingGenerator());

    // advance two lifetimes: both groups serve token id 3
    clock.addAndGet(2 * LIFETIME.toMillis());

    SecurityGroupKeyStore.SecurityKeys retainedBefore =
        store.getSecurityKeys("retained", 0, 0).orElseThrow();
    SecurityGroupKeyStore.SecurityKeys invalidatedBefore =
        store.getSecurityKeys("invalidated", 0, 0).orElseThrow();
    assertEquals(3, retainedBefore.firstTokenId());
    assertEquals(3, invalidatedBefore.firstTokenId());

    store.replaceGroups(
        List.of(
            group("retained", LIFETIME),
            group("invalidated", Duration.ofMinutes(30)),
            group("added", LIFETIME)),
        Set.of("invalidated"));

    // the retained group's rotation base and cached key bytes are undisturbed
    SecurityGroupKeyStore.SecurityKeys retainedAfter =
        store.getSecurityKeys("retained", 0, 0).orElseThrow();
    assertEquals(retainedBefore.firstTokenId(), retainedAfter.firstTokenId());
    assertEquals(retainedBefore.keys(), retainedAfter.keys());

    // the invalidated group restarts its rotation (fresh base recorded NOW: token id 1) and
    // serves regenerated key data
    SecurityGroupKeyStore.SecurityKeys invalidatedAfter =
        store.getSecurityKeys("invalidated", 0, 0).orElseThrow();
    assertEquals(1, invalidatedAfter.firstTokenId());
    assertNotEquals(invalidatedBefore.keys(), invalidatedAfter.keys());

    // adds and removes reflect in the served set
    assertEquals(Set.of("retained", "invalidated", "added"), store.securityGroupIds());
    store.replaceGroups(List.of(group("retained", LIFETIME)), Set.of());
    assertEquals(Set.of("retained"), store.securityGroupIds());
  }

  @Test
  void replacementViolationsKeepTheOldStateAndNeverThrow() {
    var clock = new AtomicLong(1_000_000L);
    InstantSource instantSource = () -> Instant.ofEpochMilli(clock.get());

    var store =
        new SecurityGroupKeyStore(
            List.of(group("g1", LIFETIME)), instantSource, new CountingGenerator());

    ByteString before = store.getSecurityKeys("g1", 0, 0).orElseThrow().keys().get(0);

    // an unsupported policy URI in the replacement set: ERROR logged, old state kept
    store.replaceGroups(
        List.of(
            SecurityGroupConfig.builder("g1")
                .keyLifeTime(LIFETIME)
                .securityPolicyUri("http://example.com/not-a-policy")
                .build()),
        Set.of("g1"));

    assertEquals(Set.of("g1"), store.securityGroupIds());
    assertEquals(before, store.getSecurityKeys("g1", 0, 0).orElseThrow().keys().get(0));

    // duplicate ids in the replacement set: the first wins, the second is dropped with ERROR
    store.replaceGroups(List.of(group("g1", LIFETIME), group("g1", LIFETIME)), Set.of());
    assertEquals(Set.of("g1"), store.securityGroupIds());
  }

  @Test
  void onConfigurationAppliedRefreshesTheFaceKeyStore() {
    PubSubConfig initial = configWith(group("stable", LIFETIME), group("edited", LIFETIME));

    // the face is never started: onConfigurationApplied touches only the key store
    var face = new SksServerFace(TestPubSubServerHolder.server().getServer(), initial, allowAll());

    ByteString stableBefore =
        face.keyStore().getSecurityKeys("stable", 0, 0).orElseThrow().keys().get(0);
    ByteString editedBefore =
        face.keyStore().getSecurityKeys("edited", 0, 0).orElseThrow().keys().get(0);

    PubSubConfig applied =
        configWith(
            group("stable", LIFETIME),
            group("edited", Duration.ofMinutes(10)),
            group("new-group", LIFETIME));

    face.onConfigurationApplied(applied, new ReconfigureResult(List.of()));

    assertEquals(Set.of("stable", "edited", "new-group"), face.keyStore().securityGroupIds());
    // untouched groups keep serving byte-identical keys; the lifetime-edited group regenerated
    assertEquals(
        stableBefore, face.keyStore().getSecurityKeys("stable", 0, 0).orElseThrow().keys().get(0));
    assertNotEquals(
        editedBefore, face.keyStore().getSecurityKeys("edited", 0, 0).orElseThrow().keys().get(0));

    // a second apply relative to the retained last-seen config: removing the edited group
    face.onConfigurationApplied(
        configWith(group("stable", LIFETIME)), new ReconfigureResult(List.of()));
    assertEquals(Set.of("stable"), face.keyStore().securityGroupIds());
    assertTrue(face.keyStore().getSecurityKeys("edited", 0, 0).isEmpty());
  }

  private static PubSubMethodAuthorizer allowAll() {
    return new PubSubMethodAuthorizer() {
      @Override
      public org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode checkConfigure(
          org.eclipse.milo.opcua.sdk.server.Session session) {
        return org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode.GOOD;
      }

      @Override
      public org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode checkSksAdmin(
          org.eclipse.milo.opcua.sdk.server.Session session) {
        return org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode.GOOD;
      }

      @Override
      public org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode checkKeyAccess(
          org.eclipse.milo.opcua.sdk.server.Session session, String securityGroupId) {
        return org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode.GOOD;
      }
    };
  }

  /** One shared never-started server for the face-construction row. */
  private static final class TestPubSubServerHolder {

    private static final TestPubSubServer SERVER = TestPubSubServer.create();

    private static TestPubSubServer server() {
      return SERVER;
    }
  }
}

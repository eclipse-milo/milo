/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.config;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.junit.jupiter.api.Test;

/**
 * The K9 effective-SKS inheritance chain pinned on {@link EffectiveMessageSecurity}: {@code mode}
 * and {@code securityGroup} come from the reader override when one is present, else from the group;
 * {@code keyServices} is the first non-empty of override → group → {@link
 * PubSubConfig#defaultSecurityKeyServices()}.
 *
 * <p>As-built note (pinned deliberately): ANY non-null reader override is active — including one
 * whose mode is {@code Invalid} or {@code None} — whereas the WP-T4 brief phrased activation as
 * "active iff reader SecurityMode != Invalid". The all-default {@code Invalid} sentinel never
 * reaches this resolver from the Part 14 mapping direction ({@code PubSubConfigMapper} normalizes
 * it to an absent override), so the object-presence semantics pinned here are the authoritative
 * config-model behavior.
 */
class EffectiveMessageSecurityTest {

  private static final SecurityGroupRef GROUP_REF = new SecurityGroupRef("SG-group");
  private static final SecurityGroupRef OVERRIDE_REF = new SecurityGroupRef("SG-override");

  private static final EndpointDescription GROUP_SKS = endpoint("opc.tcp://group-sks:4840");
  private static final EndpointDescription OVERRIDE_SKS = endpoint("opc.tcp://override-sks:4840");
  private static final EndpointDescription DEFAULT_SKS = endpoint("opc.tcp://default-sks:4840");

  // region mode + securityGroup resolution

  @Test
  void noGroupAndNoOverrideResolvesToNone() {
    EffectiveMessageSecurity effective =
        EffectiveMessageSecurity.forGroup(PubSubConfig.builder().build(), null);

    assertEquals(MessageSecurityMode.None, effective.mode());
    assertNull(effective.securityGroup());
    assertTrue(effective.keyServices().isEmpty());
  }

  @Test
  void groupValuesApplyWhenNoOverrideIsPresent() {
    MessageSecurityConfig group =
        MessageSecurityConfig.builder()
            .mode(MessageSecurityMode.Sign)
            .securityGroup(GROUP_REF)
            .keyServices(List.of(GROUP_SKS))
            .build();

    EffectiveMessageSecurity effective =
        EffectiveMessageSecurity.forGroup(PubSubConfig.builder().build(), group);

    assertEquals(MessageSecurityMode.Sign, effective.mode());
    assertEquals(GROUP_REF, effective.securityGroup());
    assertEquals(List.of(GROUP_SKS), effective.keyServices());
  }

  @Test
  void readerOverrideWinsOverGroupForModeAndSecurityGroup() {
    MessageSecurityConfig group =
        MessageSecurityConfig.builder()
            .mode(MessageSecurityMode.Sign)
            .securityGroup(GROUP_REF)
            .keyServices(List.of(GROUP_SKS))
            .build();

    MessageSecurityConfig override =
        MessageSecurityConfig.builder()
            .mode(MessageSecurityMode.SignAndEncrypt)
            .securityGroup(OVERRIDE_REF)
            .keyServices(List.of(OVERRIDE_SKS))
            .build();

    EffectiveMessageSecurity effective =
        EffectiveMessageSecurity.forReader(PubSubConfig.builder().build(), group, override);

    assertEquals(MessageSecurityMode.SignAndEncrypt, effective.mode());
    assertEquals(OVERRIDE_REF, effective.securityGroup());
    assertEquals(List.of(OVERRIDE_SKS), effective.keyServices());
  }

  @Test
  void nullOverrideInheritsTheGroup() {
    MessageSecurityConfig group =
        MessageSecurityConfig.builder()
            .mode(MessageSecurityMode.SignAndEncrypt)
            .securityGroup(GROUP_REF)
            .build();

    EffectiveMessageSecurity effective =
        EffectiveMessageSecurity.forReader(PubSubConfig.builder().build(), group, null);

    assertEquals(MessageSecurityMode.SignAndEncrypt, effective.mode());
    assertEquals(GROUP_REF, effective.securityGroup());
  }

  /**
   * The as-built activation pin: ANY non-null override is active, even mode {@code None} with no
   * SecurityGroup — it replaces the group's mode and ref rather than inheriting them.
   */
  @Test
  void anyNonNullOverrideIsActiveEvenModeNone() {
    MessageSecurityConfig group =
        MessageSecurityConfig.builder()
            .mode(MessageSecurityMode.Sign)
            .securityGroup(GROUP_REF)
            .keyServices(List.of(GROUP_SKS))
            .build();

    MessageSecurityConfig noneOverride =
        MessageSecurityConfig.builder()
            .mode(MessageSecurityMode.None)
            .keyServices(List.of(OVERRIDE_SKS))
            .build();

    EffectiveMessageSecurity effective =
        EffectiveMessageSecurity.forReader(PubSubConfig.builder().build(), group, noneOverride);

    assertEquals(MessageSecurityMode.None, effective.mode());
    assertNull(effective.securityGroup());
    assertEquals(List.of(OVERRIDE_SKS), effective.keyServices());
  }

  // endregion

  // region keyServices first-non-empty chain

  @Test
  void emptyOverrideKeyServicesFallToTheGroup() {
    MessageSecurityConfig group =
        MessageSecurityConfig.builder()
            .mode(MessageSecurityMode.Sign)
            .securityGroup(GROUP_REF)
            .keyServices(List.of(GROUP_SKS))
            .build();

    MessageSecurityConfig override =
        MessageSecurityConfig.builder()
            .mode(MessageSecurityMode.SignAndEncrypt)
            .securityGroup(OVERRIDE_REF)
            .build();

    EffectiveMessageSecurity effective =
        EffectiveMessageSecurity.forReader(PubSubConfig.builder().build(), group, override);

    // mode/ref from the override, key services from the group (first non-empty)
    assertEquals(MessageSecurityMode.SignAndEncrypt, effective.mode());
    assertEquals(OVERRIDE_REF, effective.securityGroup());
    assertEquals(List.of(GROUP_SKS), effective.keyServices());
  }

  @Test
  void emptyGroupKeyServicesFallToTheConfigDefaults() {
    PubSubConfig config = PubSubConfig.builder().defaultSecurityKeyService(DEFAULT_SKS).build();

    MessageSecurityConfig group =
        MessageSecurityConfig.builder()
            .mode(MessageSecurityMode.Sign)
            .securityGroup(GROUP_REF)
            .build();

    assertEquals(
        List.of(DEFAULT_SKS), EffectiveMessageSecurity.forGroup(config, group).keyServices());

    // the root defaults also apply with no group config at all
    assertEquals(
        List.of(DEFAULT_SKS), EffectiveMessageSecurity.forGroup(config, null).keyServices());
  }

  @Test
  void groupKeyServicesShadowTheConfigDefaults() {
    PubSubConfig config = PubSubConfig.builder().defaultSecurityKeyService(DEFAULT_SKS).build();

    MessageSecurityConfig group =
        MessageSecurityConfig.builder()
            .mode(MessageSecurityMode.Sign)
            .securityGroup(GROUP_REF)
            .keyServices(List.of(GROUP_SKS))
            .build();

    assertEquals(
        List.of(GROUP_SKS), EffectiveMessageSecurity.forGroup(config, group).keyServices());
  }

  // endregion

  // region immutability

  @Test
  void keyServicesListIsDefensivelyCopiedAndImmutable() {
    var source = new ArrayList<EndpointDescription>();
    source.add(GROUP_SKS);

    var effective = new EffectiveMessageSecurity(MessageSecurityMode.Sign, GROUP_REF, source);

    // defensive copy: mutating the source list does not change the record
    source.add(OVERRIDE_SKS);
    assertEquals(List.of(GROUP_SKS), effective.keyServices());

    // List.copyOf: the exposed list is unmodifiable
    assertThrows(
        UnsupportedOperationException.class, () -> effective.keyServices().add(OVERRIDE_SKS));
  }

  // endregion

  private static EndpointDescription endpoint(String url) {
    var server =
        new ApplicationDescription(
            "urn:milo:test:sks",
            "urn:milo:test:sks",
            LocalizedText.NULL_VALUE,
            ApplicationType.Server,
            null,
            null,
            new String[] {url});

    return new EndpointDescription(
        url,
        server,
        ByteString.NULL_VALUE,
        MessageSecurityMode.SignAndEncrypt,
        null,
        null,
        null,
        ubyte(0));
  }
}

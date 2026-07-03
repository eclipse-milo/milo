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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;

/**
 * {@link ServerPubSubOptions} builder/toBuilder/equals/hashCode round-trips, guarding the Security
 * Key Service fields ({@code securityKeyServerEnabled}, {@code methodAuthorizer}) against
 * toBuilder/equals/hashCode drift: option round-trips must not silently lose them.
 */
class ServerPubSubOptionsTest {

  @Test
  void securityKeyServerFieldsDefaultToDisabled() {
    ServerPubSubOptions options = ServerPubSubOptions.builder().build();

    assertFalse(options.isSecurityKeyServerEnabled());
    assertNull(options.getMethodAuthorizer());
  }

  @Test
  void builderSetsTheSecurityKeyServerFields() {
    PubSubMethodAuthorizer authorizer = mock(PubSubMethodAuthorizer.class);

    ServerPubSubOptions options =
        ServerPubSubOptions.builder()
            .securityKeyServerEnabled(true)
            .methodAuthorizer(authorizer)
            .build();

    assertTrue(options.isSecurityKeyServerEnabled());
    assertSame(authorizer, options.getMethodAuthorizer());
  }

  @Test
  void toBuilderRoundTripPreservesAllFields() {
    PubSubMethodAuthorizer authorizer = mock(PubSubMethodAuthorizer.class);
    PubSubConfigurationStore store = mock(PubSubConfigurationStore.class);

    ServerPubSubOptions options =
        ServerPubSubOptions.builder()
            .exposeInformationModel(true)
            .allowRemoteConfiguration(true)
            .configurationStore(store)
            .diagnosticsEnabled(true)
            .securityKeyServerEnabled(true)
            .methodAuthorizer(authorizer)
            .build();

    ServerPubSubOptions copy = options.toBuilder().build();

    assertEquals(options, copy);
    assertEquals(options.hashCode(), copy.hashCode());

    assertTrue(copy.isExposeInformationModel());
    assertTrue(copy.isAllowRemoteConfiguration());
    assertSame(store, copy.getConfigurationStore());
    assertTrue(copy.isDiagnosticsEnabled());
    assertTrue(copy.isSecurityKeyServerEnabled());
    assertSame(authorizer, copy.getMethodAuthorizer());
  }

  @Test
  void equalsDistinguishesTheSecurityKeyServerFields() {
    ServerPubSubOptions base = ServerPubSubOptions.builder().build();

    assertEquals(base, base.toBuilder().build());
    assertNotEquals(base, base.toBuilder().securityKeyServerEnabled(true).build());
    assertNotEquals(
        base, base.toBuilder().methodAuthorizer(mock(PubSubMethodAuthorizer.class)).build());
  }
}

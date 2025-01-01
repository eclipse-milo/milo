/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

import org.eclipse.milo.opcua.sdk.client.identity.AnonymousProvider;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.testng.annotations.Test;

public class OpcUaClientConfigTest {

  private final EndpointDescription endpoint =
      new EndpointDescription(
          "opc.tcp://localhost:62541",
          null,
          null,
          null,
          null,
          new UserTokenPolicy[] {
            new UserTokenPolicy("anonymous", UserTokenType.Anonymous, null, null, null)
          },
          null,
          null);

  @Test
  public void testCopy() {
    OpcUaClientConfig original =
        OpcUaClientConfig.builder()
            .setEndpoint(endpoint)
            .setSessionName(() -> "testSessionName")
            .setSessionTimeout(uint(60000 * 60))
            .setMaxResponseMessageSize(UInteger.MAX)
            .setMaxPendingPublishRequests(uint(2))
            .setIdentityProvider(new AnonymousProvider())
            .setSessionLocaleIds(new String[] {"en", "es"})
            .build();

    OpcUaClientConfig copy = OpcUaClientConfig.copy(original).build();

    assertEquals(copy.getSessionName(), original.getSessionName());
    assertEquals(copy.getSessionTimeout(), original.getSessionTimeout());
    assertEquals(copy.getMaxResponseMessageSize(), original.getMaxResponseMessageSize());
    assertEquals(copy.getMaxPendingPublishRequests(), original.getMaxPendingPublishRequests());
    assertEquals(copy.getIdentityProvider(), original.getIdentityProvider());
    assertEquals(copy.getKeepAliveFailuresAllowed(), original.getKeepAliveFailuresAllowed());
    assertEquals(copy.getKeepAliveInterval(), original.getKeepAliveInterval());
    assertEquals(copy.getKeepAliveTimeout(), original.getKeepAliveTimeout());
    assertEquals(copy.getSessionLocaleIds(), original.getSessionLocaleIds());
  }

  @Test
  public void testCopyAndModify() {
    OpcUaClientConfig original =
        OpcUaClientConfig.builder()
            .setEndpoint(endpoint)
            .setSessionName(() -> "testSessionName")
            .setSessionTimeout(uint(60000 * 60))
            .setMaxResponseMessageSize(UInteger.MAX)
            .setMaxPendingPublishRequests(uint(2))
            .setIdentityProvider(new AnonymousProvider())
            .build();

    OpcUaClientConfig copy =
        OpcUaClientConfig.copy(
            original,
            builder ->
                builder
                    .setSessionName(() -> "foo")
                    .setSessionTimeout(uint(0))
                    .setMaxResponseMessageSize(uint(0))
                    .setMaxPendingPublishRequests(uint(0))
                    .setIdentityProvider(new AnonymousProvider())
                    .setKeepAliveFailuresAllowed(uint(2))
                    .setKeepAliveInterval(uint(10000))
                    .setKeepAliveTimeout(uint(15000))
                    .setSessionLocaleIds(new String[] {"en", "es"}));

    assertNotEquals(copy.getSessionName(), original.getSessionName());
    assertNotEquals(copy.getIdentityProvider(), original.getIdentityProvider());
    assertNotEquals(copy.getSessionLocaleIds(), original.getSessionLocaleIds());

    assertEquals(copy.getSessionTimeout(), uint(0));
    assertEquals(copy.getMaxResponseMessageSize(), uint(0));
    assertEquals(copy.getMaxPendingPublishRequests(), uint(0));
    assertEquals(copy.getKeepAliveFailuresAllowed(), uint(2));
    assertEquals(copy.getKeepAliveInterval(), uint(10000));
    assertEquals(copy.getKeepAliveTimeout(), uint(15000));
    assertEquals(copy.getSessionLocaleIds(), new String[] {"en", "es"});
  }
}

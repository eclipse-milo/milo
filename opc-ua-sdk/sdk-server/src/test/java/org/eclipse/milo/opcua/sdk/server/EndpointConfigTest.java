/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.transport.TransportProfile;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.junit.jupiter.api.Test;

public class EndpointConfigTest {

  @Test
  public void securityMismatchThrows() {
    assertThrows(
        IllegalArgumentException.class,
        () ->
            // mismatch between securityPolicy and securityMode
            EndpointConfig.newBuilder()
                .setSecurityPolicy(SecurityPolicy.Basic128Rsa15)
                .setSecurityMode(MessageSecurityMode.None)
                .build());

    assertThrows(
        IllegalArgumentException.class,
        () ->
            // mismatch between securityPolicy and securityMode
            EndpointConfig.newBuilder()
                .setSecurityPolicy(SecurityPolicy.None)
                .setSecurityMode(MessageSecurityMode.SignAndEncrypt)
                .build());
  }

  @Test
  public void missingCertificateThrows() {
    assertThrows(
        IllegalStateException.class,
        () ->
            // missing certificate
            EndpointConfig.newBuilder()
                .setSecurityPolicy(SecurityPolicy.Basic128Rsa15)
                .setSecurityMode(MessageSecurityMode.SignAndEncrypt)
                .build());
  }

  @Test
  public void unsupportedTransportThrows() {
    assertThrows(
        IllegalArgumentException.class,
        () ->
            EndpointConfig.newBuilder().setTransportProfile(TransportProfile.HTTPS_UAXML).build());

    assertThrows(
        IllegalArgumentException.class,
        () ->
            EndpointConfig.newBuilder().setTransportProfile(TransportProfile.HTTPS_UAJSON).build());

    assertThrows(
        IllegalArgumentException.class,
        () -> EndpointConfig.newBuilder().setTransportProfile(TransportProfile.WSS_UAJSON).build());

    assertThrows(
        IllegalArgumentException.class,
        () ->
            EndpointConfig.newBuilder()
                .setTransportProfile(TransportProfile.WSS_UASC_UABINARY)
                .build());
  }

  @Test
  public void missingTokenPolicyDefaultsToAnonymous() {
    EndpointConfig endpointConfig = EndpointConfig.newBuilder().build();

    List<UserTokenPolicy> tokenPolicies = endpointConfig.getTokenPolicies();
    assertEquals(1, tokenPolicies.size());
    assertEquals(EndpointConfig.Builder.USER_TOKEN_POLICY_ANONYMOUS, tokenPolicies.get(0));
  }
}

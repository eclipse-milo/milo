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
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import org.eclipse.milo.opcua.sdk.server.identity.IdentityValidator;
import org.eclipse.milo.opcua.stack.core.channel.EncodingLimits;
import org.eclipse.milo.opcua.stack.core.channel.SecurityKeysListener;
import org.eclipse.milo.opcua.stack.core.security.CertificateManager;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.structured.BuildInfo;
import org.junit.jupiter.api.Test;

public class OpcUaServerConfigTest {

  @Test
  public void testCopy() {
    var endpoints = Set.of(mock(EndpointConfig.class));
    var applicationName = LocalizedText.english("application-name");
    var buildInfo = new BuildInfo("a", "b", "c", "d", "e", DateTime.MIN_VALUE);
    IdentityValidator identityValidator = mock(IdentityValidator.class);
    var encodingLimits = new EncodingLimits(8196, 2, 16392, 4);
    OpcUaServerConfigLimits limits = new OpcUaServerConfigLimits() {};
    CertificateManager certificateManager = mock(CertificateManager.class);
    RoleMapper roleMapper = identity -> List.of();
    SecurityKeysListener securityKeysListener = keyset -> {};
    ExecutorService executor = mock(ExecutorService.class);
    ScheduledExecutorService scheduledExecutor = mock(ScheduledExecutorService.class);

    OpcUaServerConfig original =
        OpcUaServerConfig.builder()
            .setEndpoints(endpoints)
            .setApplicationName(applicationName)
            .setApplicationUri("urn:application")
            .setProductUri("urn:product")
            .setBuildInfo(buildInfo)
            .setIdentityValidator(identityValidator)
            .setEncodingLimits(encodingLimits)
            .setLimits(limits)
            .setCertificateManager(certificateManager)
            .setRoleMapper(roleMapper)
            .setSecurityKeysListener(securityKeysListener)
            .setExecutor(executor)
            .setScheduledExecutor(scheduledExecutor)
            .build();

    OpcUaServerConfig copy = OpcUaServerConfig.copy(original).build();

    assertSame(original.getEndpoints(), copy.getEndpoints());
    assertSame(original.getApplicationName(), copy.getApplicationName());
    assertEquals(original.getApplicationUri(), copy.getApplicationUri());
    assertEquals(original.getProductUri(), copy.getProductUri());
    assertSame(original.getBuildInfo(), copy.getBuildInfo());
    assertSame(original.getIdentityValidator(), copy.getIdentityValidator());
    assertSame(original.getEncodingLimits(), copy.getEncodingLimits());
    assertSame(original.getLimits(), copy.getLimits());
    assertSame(original.getCertificateManager(), copy.getCertificateManager());
    assertSame(original.getRoleMapper().orElseThrow(), copy.getRoleMapper().orElseThrow());
    assertSame(
        original.getSecurityKeysListener().orElseThrow(),
        copy.getSecurityKeysListener().orElseThrow());
    assertSame(original.getExecutor(), copy.getExecutor());
    assertSame(original.getScheduledExecutorService(), copy.getScheduledExecutorService());
  }
}

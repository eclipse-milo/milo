/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.diagnostics.variables;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.eclipse.milo.opcua.sdk.core.AccessLevel;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfig;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.diagnostics.SessionSecurityDiagnosticsAccessMode;
import org.eclipse.milo.opcua.sdk.server.model.objects.ServerDiagnosticsTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.SessionSecurityDiagnosticsArrayTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.security.DefaultCertificateManager;
import org.eclipse.milo.opcua.stack.core.security.MemoryCertificateQuarantine;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.junit.jupiter.api.Test;

class SessionSecurityDiagnosticsAccessModeTest {

  @Test
  void restrictedModeIsDefaultAndRestrictsAnonymousAccess() throws Exception {
    OpcUaServerConfig config = newConfig();

    assertEquals(
        SessionSecurityDiagnosticsAccessMode.RESTRICTED,
        config.getSessionSecurityDiagnosticsAccessMode());

    OpcUaServer server = new OpcUaServer(config, transportProfile -> null);
    Session session = mock(Session.class);
    when(session.getRoleIds()).thenReturn(Optional.empty());

    ServerDiagnosticsTypeNode diagnosticsNode = getDiagnosticsNode(server);
    SessionSecurityDiagnosticsArrayTypeNode securityArray =
        diagnosticsNode
            .getSessionsDiagnosticsSummaryNode()
            .getSessionSecurityDiagnosticsArrayNode();

    assertEquals(
        AccessLevel.toValue(AccessLevel.NONE), readUserAccessLevel(session, securityArray));
    assertEquals(
        AccessLevel.toValue(AccessLevel.READ_ONLY),
        readUserAccessLevel(session, diagnosticsNode.getEnabledFlagNode()));
    assertTrue(securityArray.getAccessRestrictions().getSigningRequired());
    assertTrue(securityArray.getAccessRestrictions().getEncryptionRequired());
  }

  @Test
  void legacyModeRestoresPreviousAccessWithoutRoleMapping() throws Exception {
    OpcUaServerConfig config = newConfig(SessionSecurityDiagnosticsAccessMode.LEGACY);

    OpcUaServer server = new OpcUaServer(config, transportProfile -> null);
    Session session = mock(Session.class);
    ServerDiagnosticsTypeNode diagnosticsNode = getDiagnosticsNode(server);
    SessionSecurityDiagnosticsArrayTypeNode securityArray =
        diagnosticsNode
            .getSessionsDiagnosticsSummaryNode()
            .getSessionSecurityDiagnosticsArrayNode();

    assertEquals(
        AccessLevel.toValue(AccessLevel.READ_ONLY), readUserAccessLevel(session, securityArray));
    assertEquals(
        AccessLevel.toValue(AccessLevel.READ_WRITE),
        readUserAccessLevel(session, diagnosticsNode.getEnabledFlagNode()));
    assertTrue(securityArray.getAccessRestrictions().getSigningRequired());
    assertTrue(securityArray.getAccessRestrictions().getEncryptionRequired());
  }

  @Test
  void copyPreservesConfiguredAccessMode() {
    OpcUaServerConfig config = newConfig(SessionSecurityDiagnosticsAccessMode.LEGACY);

    OpcUaServerConfig copiedConfig = OpcUaServerConfig.copy(config).build();

    assertEquals(
        SessionSecurityDiagnosticsAccessMode.LEGACY,
        copiedConfig.getSessionSecurityDiagnosticsAccessMode());
  }

  private static OpcUaServerConfig newConfig(SessionSecurityDiagnosticsAccessMode accessMode) {
    return OpcUaServerConfig.builder()
        .setCertificateManager(new DefaultCertificateManager(new MemoryCertificateQuarantine()))
        .setSessionSecurityDiagnosticsAccessMode(accessMode)
        .build();
  }

  private static OpcUaServerConfig newConfig() {
    return OpcUaServerConfig.builder()
        .setCertificateManager(new DefaultCertificateManager(new MemoryCertificateQuarantine()))
        .build();
  }

  private static ServerDiagnosticsTypeNode getDiagnosticsNode(OpcUaServer server) {
    return (ServerDiagnosticsTypeNode)
        server
            .getAddressSpaceManager()
            .getManagedNode(NodeIds.Server_ServerDiagnostics)
            .orElseThrow();
  }

  private static UByte readUserAccessLevel(Session session, UaVariableNode node) throws Exception {
    return (UByte) node.getFilterChain().readAttribute(session, node, AttributeId.UserAccessLevel);
  }
}

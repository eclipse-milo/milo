/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.diagnostics.objects;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import org.eclipse.milo.opcua.sdk.server.model.objects.SessionsDiagnosticsSummaryTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.SessionDiagnosticsVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.SessionSecurityDiagnosticsArrayTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.SessionSecurityDiagnosticsTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.sdk.server.nodes.instantiation.InstanceDeclaration;
import org.eclipse.milo.opcua.sdk.server.nodes.instantiation.InstantiationRequest;
import org.eclipse.milo.opcua.sdk.server.nodes.instantiation.StagedGraph;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.PermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.junit.jupiter.api.Test;

class SessionsDiagnosticsSummaryObjectTest {

  @Test
  void sessionNameBrowseNamePreservesValidName() {
    String sessionName = "session";

    assertSame(sessionName, SessionsDiagnosticsSummaryObject.sessionNameBrowseName(sessionName));
  }

  @Test
  void sessionNameBrowseNameTruncatesOverlongName() {
    String sessionName = "a".repeat(SessionsDiagnosticsSummaryObject.MAX_BROWSE_NAME_LENGTH + 1);

    String browseName = SessionsDiagnosticsSummaryObject.sessionNameBrowseName(sessionName);

    assertEquals(SessionsDiagnosticsSummaryObject.MAX_BROWSE_NAME_LENGTH, browseName.length());
    assertDoesNotThrow(() -> new QualifiedName(1, browseName));
  }

  /**
   * The staged-graph hook must converge with the legacy post-hoc patch: security diagnostics
   * Variables and their fields carry the standard security array's RolePermissions,
   * UserRolePermissions, and AccessRestrictions, while ordinary SessionDiagnostics Variables retain
   * their separate standard permissions.
   */
  @Test
  void securityDiagnosticsAccessControlProtectsOnlySecurityDiagnosticsSubtree() {
    var summaryNode = mock(SessionsDiagnosticsSummaryTypeNode.class);
    var securityArray = mock(SessionSecurityDiagnosticsArrayTypeNode.class);
    var securityDiagnostics = mock(SessionSecurityDiagnosticsTypeNode.class);
    var securityField = mock(UaVariableNode.class);
    var ordinaryDiagnostics = mock(SessionDiagnosticsVariableTypeNode.class);
    var ordinaryField = mock(UaVariableNode.class);
    var graph = mock(StagedGraph.class);
    var rolePermissions =
        new RolePermissionType[] {
          new RolePermissionType(
              NodeIds.WellKnownRole_SecurityAdmin,
              PermissionType.of(PermissionType.Field.Browse, PermissionType.Field.Read))
        };
    var accessRestrictions =
        AccessRestrictionType.of(
            AccessRestrictionType.Field.SigningRequired,
            AccessRestrictionType.Field.EncryptionRequired);

    when(summaryNode.getSessionSecurityDiagnosticsArrayNode()).thenReturn(securityArray);
    when(securityArray.getRolePermissions()).thenReturn(rolePermissions);
    when(securityArray.getAccessRestrictions()).thenReturn(accessRestrictions);

    InstantiationRequest.OnNode hook =
        SessionsDiagnosticsSummaryObject.securityDiagnosticsAccessControl(summaryNode);

    hook.accept(mock(InstanceDeclaration.class), securityDiagnostics, null, graph);
    hook.accept(mock(InstanceDeclaration.class), securityField, securityDiagnostics, graph);
    hook.accept(mock(InstanceDeclaration.class), ordinaryDiagnostics, null, graph);
    hook.accept(mock(InstanceDeclaration.class), ordinaryField, ordinaryDiagnostics, graph);

    verify(securityDiagnostics).setRolePermissions(rolePermissions);
    verify(securityDiagnostics).setUserRolePermissions(null);
    verify(securityDiagnostics).setAccessRestrictions(accessRestrictions);
    verify(securityField).setRolePermissions(rolePermissions);
    verify(securityField).setUserRolePermissions(null);
    verify(securityField).setAccessRestrictions(accessRestrictions);
    verifyNoInteractions(ordinaryDiagnostics, ordinaryField);
  }
}

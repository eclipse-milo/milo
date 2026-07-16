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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.eclipse.milo.opcua.sdk.server.model.variables.SessionSecurityDiagnosticsArrayTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.sdk.server.nodes.factories.NodeFactory;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.PermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.junit.jupiter.api.Test;

class SessionSecurityDiagnosticsVariableArrayTest {

  @Test
  void securityAccessControlAppliesArrayMetadataToElementAndFieldNodes() {
    var arrayNode = mock(SessionSecurityDiagnosticsArrayTypeNode.class);
    var elementNode = mock(UaVariableNode.class);
    var fieldNode = mock(UaVariableNode.class);
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

    when(arrayNode.getRolePermissions()).thenReturn(rolePermissions);
    when(arrayNode.getAccessRestrictions()).thenReturn(accessRestrictions);

    NodeFactory.InstantiationCallback callback =
        SessionSecurityDiagnosticsVariableArray.securityAccessControl(arrayNode);

    callback.onVariableAdded(null, elementNode, NodeIds.SessionSecurityDiagnosticsType);
    callback.onVariableAdded(elementNode, fieldNode, NodeIds.BaseDataVariableType);

    verify(elementNode).setRolePermissions(rolePermissions);
    verify(elementNode).setUserRolePermissions(null);
    verify(elementNode).setAccessRestrictions(accessRestrictions);
    verify(fieldNode).setRolePermissions(rolePermissions);
    verify(fieldNode).setUserRolePermissions(null);
    verify(fieldNode).setAccessRestrictions(accessRestrictions);
  }
}

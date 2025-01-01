/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.model.objects;

import java.util.Optional;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.SessionDiagnosticsArrayTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.SessionSecurityDiagnosticsArrayTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.SessionDiagnosticsDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.SessionSecurityDiagnosticsDataType;

public class SessionsDiagnosticsSummaryTypeNode extends BaseObjectTypeNode
    implements SessionsDiagnosticsSummaryType {
  public SessionsDiagnosticsSummaryTypeNode(
      UaNodeContext context,
      NodeId nodeId,
      QualifiedName browseName,
      LocalizedText displayName,
      LocalizedText description,
      UInteger writeMask,
      UInteger userWriteMask,
      RolePermissionType[] rolePermissions,
      RolePermissionType[] userRolePermissions,
      AccessRestrictionType accessRestrictions,
      UByte eventNotifier) {
    super(
        context,
        nodeId,
        browseName,
        displayName,
        description,
        writeMask,
        userWriteMask,
        rolePermissions,
        userRolePermissions,
        accessRestrictions,
        eventNotifier);
  }

  public SessionsDiagnosticsSummaryTypeNode(
      UaNodeContext context,
      NodeId nodeId,
      QualifiedName browseName,
      LocalizedText displayName,
      LocalizedText description,
      UInteger writeMask,
      UInteger userWriteMask,
      RolePermissionType[] rolePermissions,
      RolePermissionType[] userRolePermissions,
      AccessRestrictionType accessRestrictions) {
    super(
        context,
        nodeId,
        browseName,
        displayName,
        description,
        writeMask,
        userWriteMask,
        rolePermissions,
        userRolePermissions,
        accessRestrictions);
  }

  @Override
  public SessionDiagnosticsArrayTypeNode getSessionDiagnosticsArrayNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "SessionDiagnosticsArray");
    return (SessionDiagnosticsArrayTypeNode) component.orElse(null);
  }

  @Override
  public SessionDiagnosticsDataType[] getSessionDiagnosticsArray() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "SessionDiagnosticsArray");
    return component
        .map(node -> (SessionDiagnosticsDataType[]) node.getValue().getValue().getValue())
        .orElse(null);
  }

  @Override
  public void setSessionDiagnosticsArray(SessionDiagnosticsDataType[] value) {
    getVariableComponent("http://opcfoundation.org/UA/", "SessionDiagnosticsArray")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public SessionSecurityDiagnosticsArrayTypeNode getSessionSecurityDiagnosticsArrayNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "SessionSecurityDiagnosticsArray");
    return (SessionSecurityDiagnosticsArrayTypeNode) component.orElse(null);
  }

  @Override
  public SessionSecurityDiagnosticsDataType[] getSessionSecurityDiagnosticsArray() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "SessionSecurityDiagnosticsArray");
    return component
        .map(node -> (SessionSecurityDiagnosticsDataType[]) node.getValue().getValue().getValue())
        .orElse(null);
  }

  @Override
  public void setSessionSecurityDiagnosticsArray(SessionSecurityDiagnosticsDataType[] value) {
    getVariableComponent("http://opcfoundation.org/UA/", "SessionSecurityDiagnosticsArray")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }
}

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
import org.eclipse.milo.opcua.sdk.server.model.variables.SessionDiagnosticsVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.SessionSecurityDiagnosticsTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.SubscriptionDiagnosticsArrayTypeNode;
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
import org.eclipse.milo.opcua.stack.core.types.structured.SubscriptionDiagnosticsDataType;

public class SessionDiagnosticsObjectTypeNode extends BaseObjectTypeNode
    implements SessionDiagnosticsObjectType {
  public SessionDiagnosticsObjectTypeNode(
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

  public SessionDiagnosticsObjectTypeNode(
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
  public SessionDiagnosticsVariableTypeNode getSessionDiagnosticsNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "SessionDiagnostics");
    return (SessionDiagnosticsVariableTypeNode) component.orElse(null);
  }

  @Override
  public SessionDiagnosticsDataType getSessionDiagnostics() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "SessionDiagnostics");
    return component
        .map(node -> (SessionDiagnosticsDataType) node.getValue().getValue().getValue())
        .orElse(null);
  }

  @Override
  public void setSessionDiagnostics(SessionDiagnosticsDataType value) {
    getVariableComponent("http://opcfoundation.org/UA/", "SessionDiagnostics")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public SessionSecurityDiagnosticsTypeNode getSessionSecurityDiagnosticsNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "SessionSecurityDiagnostics");
    return (SessionSecurityDiagnosticsTypeNode) component.orElse(null);
  }

  @Override
  public SessionSecurityDiagnosticsDataType getSessionSecurityDiagnostics() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "SessionSecurityDiagnostics");
    return component
        .map(node -> (SessionSecurityDiagnosticsDataType) node.getValue().getValue().getValue())
        .orElse(null);
  }

  @Override
  public void setSessionSecurityDiagnostics(SessionSecurityDiagnosticsDataType value) {
    getVariableComponent("http://opcfoundation.org/UA/", "SessionSecurityDiagnostics")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public SubscriptionDiagnosticsArrayTypeNode getSubscriptionDiagnosticsArrayNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "SubscriptionDiagnosticsArray");
    return (SubscriptionDiagnosticsArrayTypeNode) component.orElse(null);
  }

  @Override
  public SubscriptionDiagnosticsDataType[] getSubscriptionDiagnosticsArray() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "SubscriptionDiagnosticsArray");
    return component
        .map(node -> (SubscriptionDiagnosticsDataType[]) node.getValue().getValue().getValue())
        .orElse(null);
  }

  @Override
  public void setSubscriptionDiagnosticsArray(SubscriptionDiagnosticsDataType[] value) {
    getVariableComponent("http://opcfoundation.org/UA/", "SubscriptionDiagnosticsArray")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }
}

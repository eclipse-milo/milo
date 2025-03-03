/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.model.objects;

import java.util.Optional;
import org.eclipse.milo.opcua.sdk.core.nodes.ObjectNode;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.SamplingIntervalDiagnosticsArrayTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.ServerDiagnosticsSummaryTypeNode;
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
import org.eclipse.milo.opcua.stack.core.types.structured.SamplingIntervalDiagnosticsDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.ServerDiagnosticsSummaryDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.SubscriptionDiagnosticsDataType;

public class ServerDiagnosticsTypeNode extends BaseObjectTypeNode implements ServerDiagnosticsType {
  public ServerDiagnosticsTypeNode(
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

  public ServerDiagnosticsTypeNode(
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
  public PropertyTypeNode getEnabledFlagNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(ServerDiagnosticsType.ENABLED_FLAG);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Boolean getEnabledFlag() {
    return getProperty(ServerDiagnosticsType.ENABLED_FLAG).orElse(null);
  }

  @Override
  public void setEnabledFlag(Boolean value) {
    setProperty(ServerDiagnosticsType.ENABLED_FLAG, value);
  }

  @Override
  public ServerDiagnosticsSummaryTypeNode getServerDiagnosticsSummaryNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "ServerDiagnosticsSummary");
    return (ServerDiagnosticsSummaryTypeNode) component.orElse(null);
  }

  @Override
  public ServerDiagnosticsSummaryDataType getServerDiagnosticsSummary() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "ServerDiagnosticsSummary");
    return component
        .map(node -> (ServerDiagnosticsSummaryDataType) node.getValue().getValue().getValue())
        .orElse(null);
  }

  @Override
  public void setServerDiagnosticsSummary(ServerDiagnosticsSummaryDataType value) {
    getVariableComponent("http://opcfoundation.org/UA/", "ServerDiagnosticsSummary")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public SamplingIntervalDiagnosticsArrayTypeNode getSamplingIntervalDiagnosticsArrayNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "SamplingIntervalDiagnosticsArray");
    return (SamplingIntervalDiagnosticsArrayTypeNode) component.orElse(null);
  }

  @Override
  public SamplingIntervalDiagnosticsDataType[] getSamplingIntervalDiagnosticsArray() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "SamplingIntervalDiagnosticsArray");
    return component
        .map(node -> (SamplingIntervalDiagnosticsDataType[]) node.getValue().getValue().getValue())
        .orElse(null);
  }

  @Override
  public void setSamplingIntervalDiagnosticsArray(SamplingIntervalDiagnosticsDataType[] value) {
    getVariableComponent("http://opcfoundation.org/UA/", "SamplingIntervalDiagnosticsArray")
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

  @Override
  public SessionsDiagnosticsSummaryTypeNode getSessionsDiagnosticsSummaryNode() {
    Optional<ObjectNode> component =
        getObjectComponent("http://opcfoundation.org/UA/", "SessionsDiagnosticsSummary");
    return (SessionsDiagnosticsSummaryTypeNode) component.orElse(null);
  }
}

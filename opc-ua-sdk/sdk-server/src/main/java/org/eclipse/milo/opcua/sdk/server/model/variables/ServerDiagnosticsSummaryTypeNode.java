/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.model.variables;

import java.util.Optional;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessLevelExType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class ServerDiagnosticsSummaryTypeNode extends BaseDataVariableTypeNode
    implements ServerDiagnosticsSummaryType {
  public ServerDiagnosticsSummaryTypeNode(
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
      DataValue value,
      NodeId dataType,
      Integer valueRank,
      UInteger[] arrayDimensions,
      UByte accessLevel,
      UByte userAccessLevel,
      Double minimumSamplingInterval,
      boolean historizing,
      AccessLevelExType accessLevelEx) {
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
        value,
        dataType,
        valueRank,
        arrayDimensions,
        accessLevel,
        userAccessLevel,
        minimumSamplingInterval,
        historizing,
        accessLevelEx);
  }

  public ServerDiagnosticsSummaryTypeNode(
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
      DataValue value,
      NodeId dataType,
      Integer valueRank,
      UInteger[] arrayDimensions) {
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
        value,
        dataType,
        valueRank,
        arrayDimensions);
  }

  @Override
  public BaseDataVariableTypeNode getServerViewCountNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "ServerViewCount");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public UInteger getServerViewCount() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "ServerViewCount");
    return component.map(node -> (UInteger) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setServerViewCount(UInteger value) {
    getVariableComponent("http://opcfoundation.org/UA/", "ServerViewCount")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getCurrentSessionCountNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "CurrentSessionCount");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public UInteger getCurrentSessionCount() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "CurrentSessionCount");
    return component.map(node -> (UInteger) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setCurrentSessionCount(UInteger value) {
    getVariableComponent("http://opcfoundation.org/UA/", "CurrentSessionCount")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getCumulatedSessionCountNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "CumulatedSessionCount");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public UInteger getCumulatedSessionCount() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "CumulatedSessionCount");
    return component.map(node -> (UInteger) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setCumulatedSessionCount(UInteger value) {
    getVariableComponent("http://opcfoundation.org/UA/", "CumulatedSessionCount")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getSecurityRejectedSessionCountNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "SecurityRejectedSessionCount");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public UInteger getSecurityRejectedSessionCount() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "SecurityRejectedSessionCount");
    return component.map(node -> (UInteger) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setSecurityRejectedSessionCount(UInteger value) {
    getVariableComponent("http://opcfoundation.org/UA/", "SecurityRejectedSessionCount")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getRejectedSessionCountNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "RejectedSessionCount");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public UInteger getRejectedSessionCount() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "RejectedSessionCount");
    return component.map(node -> (UInteger) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setRejectedSessionCount(UInteger value) {
    getVariableComponent("http://opcfoundation.org/UA/", "RejectedSessionCount")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getSessionTimeoutCountNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "SessionTimeoutCount");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public UInteger getSessionTimeoutCount() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "SessionTimeoutCount");
    return component.map(node -> (UInteger) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setSessionTimeoutCount(UInteger value) {
    getVariableComponent("http://opcfoundation.org/UA/", "SessionTimeoutCount")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getSessionAbortCountNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "SessionAbortCount");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public UInteger getSessionAbortCount() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "SessionAbortCount");
    return component.map(node -> (UInteger) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setSessionAbortCount(UInteger value) {
    getVariableComponent("http://opcfoundation.org/UA/", "SessionAbortCount")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getPublishingIntervalCountNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "PublishingIntervalCount");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public UInteger getPublishingIntervalCount() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "PublishingIntervalCount");
    return component.map(node -> (UInteger) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setPublishingIntervalCount(UInteger value) {
    getVariableComponent("http://opcfoundation.org/UA/", "PublishingIntervalCount")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getCurrentSubscriptionCountNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "CurrentSubscriptionCount");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public UInteger getCurrentSubscriptionCount() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "CurrentSubscriptionCount");
    return component.map(node -> (UInteger) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setCurrentSubscriptionCount(UInteger value) {
    getVariableComponent("http://opcfoundation.org/UA/", "CurrentSubscriptionCount")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getCumulatedSubscriptionCountNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "CumulatedSubscriptionCount");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public UInteger getCumulatedSubscriptionCount() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "CumulatedSubscriptionCount");
    return component.map(node -> (UInteger) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setCumulatedSubscriptionCount(UInteger value) {
    getVariableComponent("http://opcfoundation.org/UA/", "CumulatedSubscriptionCount")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getSecurityRejectedRequestsCountNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "SecurityRejectedRequestsCount");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public UInteger getSecurityRejectedRequestsCount() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "SecurityRejectedRequestsCount");
    return component.map(node -> (UInteger) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setSecurityRejectedRequestsCount(UInteger value) {
    getVariableComponent("http://opcfoundation.org/UA/", "SecurityRejectedRequestsCount")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getRejectedRequestsCountNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "RejectedRequestsCount");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public UInteger getRejectedRequestsCount() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "RejectedRequestsCount");
    return component.map(node -> (UInteger) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setRejectedRequestsCount(UInteger value) {
    getVariableComponent("http://opcfoundation.org/UA/", "RejectedRequestsCount")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }
}

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
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessLevelExType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class ProgramDiagnostic2TypeNode extends BaseDataVariableTypeNode
    implements ProgramDiagnostic2Type {
  public ProgramDiagnostic2TypeNode(
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

  public ProgramDiagnostic2TypeNode(
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
  public PropertyTypeNode getLastTransitionTimeNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(ProgramDiagnostic2Type.LAST_TRANSITION_TIME);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public DateTime getLastTransitionTime() {
    return getProperty(ProgramDiagnostic2Type.LAST_TRANSITION_TIME).orElse(null);
  }

  @Override
  public void setLastTransitionTime(DateTime value) {
    setProperty(ProgramDiagnostic2Type.LAST_TRANSITION_TIME, value);
  }

  @Override
  public BaseDataVariableTypeNode getCreateSessionIdNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "CreateSessionId");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public NodeId getCreateSessionId() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "CreateSessionId");
    return component.map(node -> (NodeId) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setCreateSessionId(NodeId value) {
    getVariableComponent("http://opcfoundation.org/UA/", "CreateSessionId")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getCreateClientNameNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "CreateClientName");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public String getCreateClientName() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "CreateClientName");
    return component.map(node -> (String) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setCreateClientName(String value) {
    getVariableComponent("http://opcfoundation.org/UA/", "CreateClientName")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getInvocationCreationTimeNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "InvocationCreationTime");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public DateTime getInvocationCreationTime() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "InvocationCreationTime");
    return component.map(node -> (DateTime) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setInvocationCreationTime(DateTime value) {
    getVariableComponent("http://opcfoundation.org/UA/", "InvocationCreationTime")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getLastMethodCallNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "LastMethodCall");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public String getLastMethodCall() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "LastMethodCall");
    return component.map(node -> (String) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setLastMethodCall(String value) {
    getVariableComponent("http://opcfoundation.org/UA/", "LastMethodCall")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getLastMethodSessionIdNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "LastMethodSessionId");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public NodeId getLastMethodSessionId() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "LastMethodSessionId");
    return component.map(node -> (NodeId) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setLastMethodSessionId(NodeId value) {
    getVariableComponent("http://opcfoundation.org/UA/", "LastMethodSessionId")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getLastMethodInputArgumentsNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "LastMethodInputArguments");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public Argument[] getLastMethodInputArguments() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "LastMethodInputArguments");
    return component.map(node -> (Argument[]) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setLastMethodInputArguments(Argument[] value) {
    getVariableComponent("http://opcfoundation.org/UA/", "LastMethodInputArguments")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getLastMethodOutputArgumentsNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "LastMethodOutputArguments");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public Argument[] getLastMethodOutputArguments() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "LastMethodOutputArguments");
    return component.map(node -> (Argument[]) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setLastMethodOutputArguments(Argument[] value) {
    getVariableComponent("http://opcfoundation.org/UA/", "LastMethodOutputArguments")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getLastMethodInputValuesNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "LastMethodInputValues");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public Object[] getLastMethodInputValues() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "LastMethodInputValues");
    return component.map(node -> (Object[]) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setLastMethodInputValues(Object[] value) {
    getVariableComponent("http://opcfoundation.org/UA/", "LastMethodInputValues")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getLastMethodOutputValuesNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "LastMethodOutputValues");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public Object[] getLastMethodOutputValues() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "LastMethodOutputValues");
    return component.map(node -> (Object[]) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setLastMethodOutputValues(Object[] value) {
    getVariableComponent("http://opcfoundation.org/UA/", "LastMethodOutputValues")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getLastMethodCallTimeNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "LastMethodCallTime");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public DateTime getLastMethodCallTime() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "LastMethodCallTime");
    return component.map(node -> (DateTime) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setLastMethodCallTime(DateTime value) {
    getVariableComponent("http://opcfoundation.org/UA/", "LastMethodCallTime")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }

  @Override
  public BaseDataVariableTypeNode getLastMethodReturnStatusNode() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "LastMethodReturnStatus");
    return (BaseDataVariableTypeNode) component.orElse(null);
  }

  @Override
  public StatusCode getLastMethodReturnStatus() {
    Optional<VariableNode> component =
        getVariableComponent("http://opcfoundation.org/UA/", "LastMethodReturnStatus");
    return component.map(node -> (StatusCode) node.getValue().getValue().getValue()).orElse(null);
  }

  @Override
  public void setLastMethodReturnStatus(StatusCode value) {
    getVariableComponent("http://opcfoundation.org/UA/", "LastMethodReturnStatus")
        .ifPresent(n -> n.setValue(new DataValue(new Variant(value))));
  }
}

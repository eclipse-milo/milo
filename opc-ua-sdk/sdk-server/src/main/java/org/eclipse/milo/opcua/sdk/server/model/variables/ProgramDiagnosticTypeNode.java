/*
 * Copyright (c) 2024 the Eclipse Milo Authors
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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessLevelExType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.StatusResult;

public class ProgramDiagnosticTypeNode extends BaseDataVariableTypeNode
    implements ProgramDiagnosticType {
  public ProgramDiagnosticTypeNode(
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

  public ProgramDiagnosticTypeNode(
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
  public PropertyTypeNode getCreateSessionIdNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(ProgramDiagnosticType.CREATE_SESSION_ID);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public NodeId getCreateSessionId() {
    return getProperty(ProgramDiagnosticType.CREATE_SESSION_ID).orElse(null);
  }

  @Override
  public void setCreateSessionId(NodeId value) {
    setProperty(ProgramDiagnosticType.CREATE_SESSION_ID, value);
  }

  @Override
  public PropertyTypeNode getCreateClientNameNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(ProgramDiagnosticType.CREATE_CLIENT_NAME);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public String getCreateClientName() {
    return getProperty(ProgramDiagnosticType.CREATE_CLIENT_NAME).orElse(null);
  }

  @Override
  public void setCreateClientName(String value) {
    setProperty(ProgramDiagnosticType.CREATE_CLIENT_NAME, value);
  }

  @Override
  public PropertyTypeNode getInvocationCreationTimeNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(ProgramDiagnosticType.INVOCATION_CREATION_TIME);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public DateTime getInvocationCreationTime() {
    return getProperty(ProgramDiagnosticType.INVOCATION_CREATION_TIME).orElse(null);
  }

  @Override
  public void setInvocationCreationTime(DateTime value) {
    setProperty(ProgramDiagnosticType.INVOCATION_CREATION_TIME, value);
  }

  @Override
  public PropertyTypeNode getLastTransitionTimeNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(ProgramDiagnosticType.LAST_TRANSITION_TIME);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public DateTime getLastTransitionTime() {
    return getProperty(ProgramDiagnosticType.LAST_TRANSITION_TIME).orElse(null);
  }

  @Override
  public void setLastTransitionTime(DateTime value) {
    setProperty(ProgramDiagnosticType.LAST_TRANSITION_TIME, value);
  }

  @Override
  public PropertyTypeNode getLastMethodCallNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(ProgramDiagnosticType.LAST_METHOD_CALL);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public String getLastMethodCall() {
    return getProperty(ProgramDiagnosticType.LAST_METHOD_CALL).orElse(null);
  }

  @Override
  public void setLastMethodCall(String value) {
    setProperty(ProgramDiagnosticType.LAST_METHOD_CALL, value);
  }

  @Override
  public PropertyTypeNode getLastMethodSessionIdNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(ProgramDiagnosticType.LAST_METHOD_SESSION_ID);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public NodeId getLastMethodSessionId() {
    return getProperty(ProgramDiagnosticType.LAST_METHOD_SESSION_ID).orElse(null);
  }

  @Override
  public void setLastMethodSessionId(NodeId value) {
    setProperty(ProgramDiagnosticType.LAST_METHOD_SESSION_ID, value);
  }

  @Override
  public PropertyTypeNode getLastMethodInputArgumentsNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(ProgramDiagnosticType.LAST_METHOD_INPUT_ARGUMENTS);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Object[] getLastMethodInputArguments() {
    return getProperty(ProgramDiagnosticType.LAST_METHOD_INPUT_ARGUMENTS).orElse(null);
  }

  @Override
  public void setLastMethodInputArguments(Object[] value) {
    setProperty(ProgramDiagnosticType.LAST_METHOD_INPUT_ARGUMENTS, value);
  }

  @Override
  public PropertyTypeNode getLastMethodOutputArgumentsNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(ProgramDiagnosticType.LAST_METHOD_OUTPUT_ARGUMENTS);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Object[] getLastMethodOutputArguments() {
    return getProperty(ProgramDiagnosticType.LAST_METHOD_OUTPUT_ARGUMENTS).orElse(null);
  }

  @Override
  public void setLastMethodOutputArguments(Object[] value) {
    setProperty(ProgramDiagnosticType.LAST_METHOD_OUTPUT_ARGUMENTS, value);
  }

  @Override
  public PropertyTypeNode getLastMethodCallTimeNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(ProgramDiagnosticType.LAST_METHOD_CALL_TIME);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public DateTime getLastMethodCallTime() {
    return getProperty(ProgramDiagnosticType.LAST_METHOD_CALL_TIME).orElse(null);
  }

  @Override
  public void setLastMethodCallTime(DateTime value) {
    setProperty(ProgramDiagnosticType.LAST_METHOD_CALL_TIME, value);
  }

  @Override
  public PropertyTypeNode getLastMethodReturnStatusNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(ProgramDiagnosticType.LAST_METHOD_RETURN_STATUS);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public StatusResult getLastMethodReturnStatus() {
    return getProperty(ProgramDiagnosticType.LAST_METHOD_RETURN_STATUS).orElse(null);
  }

  @Override
  public void setLastMethodReturnStatus(StatusResult value) {
    setProperty(ProgramDiagnosticType.LAST_METHOD_RETURN_STATUS, value);
  }
}

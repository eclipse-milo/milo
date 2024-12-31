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
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.TransactionErrorType;

public class TransactionDiagnosticsTypeNode extends BaseObjectTypeNode
    implements TransactionDiagnosticsType {
  public TransactionDiagnosticsTypeNode(
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

  public TransactionDiagnosticsTypeNode(
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
  public PropertyTypeNode getStartTimeNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(TransactionDiagnosticsType.START_TIME);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public DateTime getStartTime() {
    return getProperty(TransactionDiagnosticsType.START_TIME).orElse(null);
  }

  @Override
  public void setStartTime(DateTime value) {
    setProperty(TransactionDiagnosticsType.START_TIME, value);
  }

  @Override
  public PropertyTypeNode getEndTimeNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(TransactionDiagnosticsType.END_TIME);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public DateTime getEndTime() {
    return getProperty(TransactionDiagnosticsType.END_TIME).orElse(null);
  }

  @Override
  public void setEndTime(DateTime value) {
    setProperty(TransactionDiagnosticsType.END_TIME, value);
  }

  @Override
  public PropertyTypeNode getResultNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(TransactionDiagnosticsType.RESULT);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public StatusCode getResult() {
    return getProperty(TransactionDiagnosticsType.RESULT).orElse(null);
  }

  @Override
  public void setResult(StatusCode value) {
    setProperty(TransactionDiagnosticsType.RESULT, value);
  }

  @Override
  public PropertyTypeNode getAffectedTrustListsNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(TransactionDiagnosticsType.AFFECTED_TRUST_LISTS);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public NodeId[] getAffectedTrustLists() {
    return getProperty(TransactionDiagnosticsType.AFFECTED_TRUST_LISTS).orElse(null);
  }

  @Override
  public void setAffectedTrustLists(NodeId[] value) {
    setProperty(TransactionDiagnosticsType.AFFECTED_TRUST_LISTS, value);
  }

  @Override
  public PropertyTypeNode getAffectedCertificateGroupsNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(TransactionDiagnosticsType.AFFECTED_CERTIFICATE_GROUPS);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public NodeId[] getAffectedCertificateGroups() {
    return getProperty(TransactionDiagnosticsType.AFFECTED_CERTIFICATE_GROUPS).orElse(null);
  }

  @Override
  public void setAffectedCertificateGroups(NodeId[] value) {
    setProperty(TransactionDiagnosticsType.AFFECTED_CERTIFICATE_GROUPS, value);
  }

  @Override
  public PropertyTypeNode getErrorsNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(TransactionDiagnosticsType.ERRORS);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public TransactionErrorType[] getErrors() {
    return getProperty(TransactionDiagnosticsType.ERRORS).orElse(null);
  }

  @Override
  public void setErrors(TransactionErrorType[] value) {
    setProperty(TransactionDiagnosticsType.ERRORS, value);
  }
}

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
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PerformUpdateType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.Annotation;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class AuditHistoryAnnotationUpdateEventTypeNode extends AuditHistoryUpdateEventTypeNode
    implements AuditHistoryAnnotationUpdateEventType {
  public AuditHistoryAnnotationUpdateEventTypeNode(
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

  public AuditHistoryAnnotationUpdateEventTypeNode(
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
  public PropertyTypeNode getPerformInsertReplaceNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(AuditHistoryAnnotationUpdateEventType.PERFORM_INSERT_REPLACE);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public PerformUpdateType getPerformInsertReplace() {
    return getProperty(AuditHistoryAnnotationUpdateEventType.PERFORM_INSERT_REPLACE).orElse(null);
  }

  @Override
  public void setPerformInsertReplace(PerformUpdateType value) {
    setProperty(AuditHistoryAnnotationUpdateEventType.PERFORM_INSERT_REPLACE, value);
  }

  @Override
  public PropertyTypeNode getNewValuesNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(AuditHistoryAnnotationUpdateEventType.NEW_VALUES);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Annotation[] getNewValues() {
    return getProperty(AuditHistoryAnnotationUpdateEventType.NEW_VALUES).orElse(null);
  }

  @Override
  public void setNewValues(Annotation[] value) {
    setProperty(AuditHistoryAnnotationUpdateEventType.NEW_VALUES, value);
  }

  @Override
  public PropertyTypeNode getOldValuesNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(AuditHistoryAnnotationUpdateEventType.OLD_VALUES);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Annotation[] getOldValues() {
    return getProperty(AuditHistoryAnnotationUpdateEventType.OLD_VALUES).orElse(null);
  }

  @Override
  public void setOldValues(Annotation[] value) {
    setProperty(AuditHistoryAnnotationUpdateEventType.OLD_VALUES, value);
  }
}

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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class TrustListOutOfDateAlarmTypeNode extends SystemOffNormalAlarmTypeNode
    implements TrustListOutOfDateAlarmType {
  public TrustListOutOfDateAlarmTypeNode(
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

  public TrustListOutOfDateAlarmTypeNode(
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
  public PropertyTypeNode getTrustListIdNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(TrustListOutOfDateAlarmType.TRUST_LIST_ID);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public NodeId getTrustListId() {
    return getProperty(TrustListOutOfDateAlarmType.TRUST_LIST_ID).orElse(null);
  }

  @Override
  public void setTrustListId(NodeId value) {
    setProperty(TrustListOutOfDateAlarmType.TRUST_LIST_ID, value);
  }

  @Override
  public PropertyTypeNode getLastUpdateTimeNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(TrustListOutOfDateAlarmType.LAST_UPDATE_TIME);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public DateTime getLastUpdateTime() {
    return getProperty(TrustListOutOfDateAlarmType.LAST_UPDATE_TIME).orElse(null);
  }

  @Override
  public void setLastUpdateTime(DateTime value) {
    setProperty(TrustListOutOfDateAlarmType.LAST_UPDATE_TIME, value);
  }

  @Override
  public PropertyTypeNode getUpdateFrequencyNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(TrustListOutOfDateAlarmType.UPDATE_FREQUENCY);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Double getUpdateFrequency() {
    return getProperty(TrustListOutOfDateAlarmType.UPDATE_FREQUENCY).orElse(null);
  }

  @Override
  public void setUpdateFrequency(Double value) {
    setProperty(TrustListOutOfDateAlarmType.UPDATE_FREQUENCY, value);
  }
}

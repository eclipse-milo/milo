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
import org.eclipse.milo.opcua.stack.core.types.enumerated.DataSetOrderingType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpNetworkMessageContentMask;

public class UadpWriterGroupMessageTypeNode extends WriterGroupMessageTypeNode
    implements UadpWriterGroupMessageType {
  public UadpWriterGroupMessageTypeNode(
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

  public UadpWriterGroupMessageTypeNode(
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
  public PropertyTypeNode getGroupVersionNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(UadpWriterGroupMessageType.GROUP_VERSION);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UInteger getGroupVersion() {
    return getProperty(UadpWriterGroupMessageType.GROUP_VERSION).orElse(null);
  }

  @Override
  public void setGroupVersion(UInteger value) {
    setProperty(UadpWriterGroupMessageType.GROUP_VERSION, value);
  }

  @Override
  public PropertyTypeNode getDataSetOrderingNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(UadpWriterGroupMessageType.DATA_SET_ORDERING);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public DataSetOrderingType getDataSetOrdering() {
    return getProperty(UadpWriterGroupMessageType.DATA_SET_ORDERING).orElse(null);
  }

  @Override
  public void setDataSetOrdering(DataSetOrderingType value) {
    setProperty(UadpWriterGroupMessageType.DATA_SET_ORDERING, value);
  }

  @Override
  public PropertyTypeNode getNetworkMessageContentMaskNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(UadpWriterGroupMessageType.NETWORK_MESSAGE_CONTENT_MASK);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UadpNetworkMessageContentMask getNetworkMessageContentMask() {
    return getProperty(UadpWriterGroupMessageType.NETWORK_MESSAGE_CONTENT_MASK).orElse(null);
  }

  @Override
  public void setNetworkMessageContentMask(UadpNetworkMessageContentMask value) {
    setProperty(UadpWriterGroupMessageType.NETWORK_MESSAGE_CONTENT_MASK, value);
  }

  @Override
  public PropertyTypeNode getSamplingOffsetNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(UadpWriterGroupMessageType.SAMPLING_OFFSET);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Double getSamplingOffset() {
    return getProperty(UadpWriterGroupMessageType.SAMPLING_OFFSET).orElse(null);
  }

  @Override
  public void setSamplingOffset(Double value) {
    setProperty(UadpWriterGroupMessageType.SAMPLING_OFFSET, value);
  }

  @Override
  public PropertyTypeNode getPublishingOffsetNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(UadpWriterGroupMessageType.PUBLISHING_OFFSET);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Double[] getPublishingOffset() {
    return getProperty(UadpWriterGroupMessageType.PUBLISHING_OFFSET).orElse(null);
  }

  @Override
  public void setPublishingOffset(Double[] value) {
    setProperty(UadpWriterGroupMessageType.PUBLISHING_OFFSET, value);
  }
}

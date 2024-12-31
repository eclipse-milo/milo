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
import java.util.UUID;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpDataSetMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpNetworkMessageContentMask;

public class UadpDataSetReaderMessageTypeNode extends DataSetReaderMessageTypeNode
    implements UadpDataSetReaderMessageType {
  public UadpDataSetReaderMessageTypeNode(
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

  public UadpDataSetReaderMessageTypeNode(
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
    Optional<VariableNode> propertyNode =
        getPropertyNode(UadpDataSetReaderMessageType.GROUP_VERSION);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UInteger getGroupVersion() {
    return getProperty(UadpDataSetReaderMessageType.GROUP_VERSION).orElse(null);
  }

  @Override
  public void setGroupVersion(UInteger value) {
    setProperty(UadpDataSetReaderMessageType.GROUP_VERSION, value);
  }

  @Override
  public PropertyTypeNode getNetworkMessageNumberNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(UadpDataSetReaderMessageType.NETWORK_MESSAGE_NUMBER);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UShort getNetworkMessageNumber() {
    return getProperty(UadpDataSetReaderMessageType.NETWORK_MESSAGE_NUMBER).orElse(null);
  }

  @Override
  public void setNetworkMessageNumber(UShort value) {
    setProperty(UadpDataSetReaderMessageType.NETWORK_MESSAGE_NUMBER, value);
  }

  @Override
  public PropertyTypeNode getDataSetOffsetNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(UadpDataSetReaderMessageType.DATA_SET_OFFSET);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UShort getDataSetOffset() {
    return getProperty(UadpDataSetReaderMessageType.DATA_SET_OFFSET).orElse(null);
  }

  @Override
  public void setDataSetOffset(UShort value) {
    setProperty(UadpDataSetReaderMessageType.DATA_SET_OFFSET, value);
  }

  @Override
  public PropertyTypeNode getDataSetClassIdNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(UadpDataSetReaderMessageType.DATA_SET_CLASS_ID);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UUID getDataSetClassId() {
    return getProperty(UadpDataSetReaderMessageType.DATA_SET_CLASS_ID).orElse(null);
  }

  @Override
  public void setDataSetClassId(UUID value) {
    setProperty(UadpDataSetReaderMessageType.DATA_SET_CLASS_ID, value);
  }

  @Override
  public PropertyTypeNode getNetworkMessageContentMaskNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(UadpDataSetReaderMessageType.NETWORK_MESSAGE_CONTENT_MASK);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UadpNetworkMessageContentMask getNetworkMessageContentMask() {
    return getProperty(UadpDataSetReaderMessageType.NETWORK_MESSAGE_CONTENT_MASK).orElse(null);
  }

  @Override
  public void setNetworkMessageContentMask(UadpNetworkMessageContentMask value) {
    setProperty(UadpDataSetReaderMessageType.NETWORK_MESSAGE_CONTENT_MASK, value);
  }

  @Override
  public PropertyTypeNode getDataSetMessageContentMaskNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(UadpDataSetReaderMessageType.DATA_SET_MESSAGE_CONTENT_MASK);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UadpDataSetMessageContentMask getDataSetMessageContentMask() {
    return getProperty(UadpDataSetReaderMessageType.DATA_SET_MESSAGE_CONTENT_MASK).orElse(null);
  }

  @Override
  public void setDataSetMessageContentMask(UadpDataSetMessageContentMask value) {
    setProperty(UadpDataSetReaderMessageType.DATA_SET_MESSAGE_CONTENT_MASK, value);
  }

  @Override
  public PropertyTypeNode getPublishingIntervalNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(UadpDataSetReaderMessageType.PUBLISHING_INTERVAL);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Double getPublishingInterval() {
    return getProperty(UadpDataSetReaderMessageType.PUBLISHING_INTERVAL).orElse(null);
  }

  @Override
  public void setPublishingInterval(Double value) {
    setProperty(UadpDataSetReaderMessageType.PUBLISHING_INTERVAL, value);
  }

  @Override
  public PropertyTypeNode getProcessingOffsetNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(UadpDataSetReaderMessageType.PROCESSING_OFFSET);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Double getProcessingOffset() {
    return getProperty(UadpDataSetReaderMessageType.PROCESSING_OFFSET).orElse(null);
  }

  @Override
  public void setProcessingOffset(Double value) {
    setProperty(UadpDataSetReaderMessageType.PROCESSING_OFFSET, value);
  }

  @Override
  public PropertyTypeNode getReceiveOffsetNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(UadpDataSetReaderMessageType.RECEIVE_OFFSET);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Double getReceiveOffset() {
    return getProperty(UadpDataSetReaderMessageType.RECEIVE_OFFSET).orElse(null);
  }

  @Override
  public void setReceiveOffset(Double value) {
    setProperty(UadpDataSetReaderMessageType.RECEIVE_OFFSET, value);
  }
}

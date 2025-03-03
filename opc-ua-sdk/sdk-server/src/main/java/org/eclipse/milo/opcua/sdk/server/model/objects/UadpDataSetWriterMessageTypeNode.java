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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpDataSetMessageContentMask;

public class UadpDataSetWriterMessageTypeNode extends DataSetWriterMessageTypeNode
    implements UadpDataSetWriterMessageType {
  public UadpDataSetWriterMessageTypeNode(
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

  public UadpDataSetWriterMessageTypeNode(
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
  public PropertyTypeNode getDataSetMessageContentMaskNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(UadpDataSetWriterMessageType.DATA_SET_MESSAGE_CONTENT_MASK);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UadpDataSetMessageContentMask getDataSetMessageContentMask() {
    return getProperty(UadpDataSetWriterMessageType.DATA_SET_MESSAGE_CONTENT_MASK).orElse(null);
  }

  @Override
  public void setDataSetMessageContentMask(UadpDataSetMessageContentMask value) {
    setProperty(UadpDataSetWriterMessageType.DATA_SET_MESSAGE_CONTENT_MASK, value);
  }

  @Override
  public PropertyTypeNode getConfiguredSizeNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(UadpDataSetWriterMessageType.CONFIGURED_SIZE);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UShort getConfiguredSize() {
    return getProperty(UadpDataSetWriterMessageType.CONFIGURED_SIZE).orElse(null);
  }

  @Override
  public void setConfiguredSize(UShort value) {
    setProperty(UadpDataSetWriterMessageType.CONFIGURED_SIZE, value);
  }

  @Override
  public PropertyTypeNode getNetworkMessageNumberNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(UadpDataSetWriterMessageType.NETWORK_MESSAGE_NUMBER);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UShort getNetworkMessageNumber() {
    return getProperty(UadpDataSetWriterMessageType.NETWORK_MESSAGE_NUMBER).orElse(null);
  }

  @Override
  public void setNetworkMessageNumber(UShort value) {
    setProperty(UadpDataSetWriterMessageType.NETWORK_MESSAGE_NUMBER, value);
  }

  @Override
  public PropertyTypeNode getDataSetOffsetNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(UadpDataSetWriterMessageType.DATA_SET_OFFSET);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UShort getDataSetOffset() {
    return getProperty(UadpDataSetWriterMessageType.DATA_SET_OFFSET).orElse(null);
  }

  @Override
  public void setDataSetOffset(UShort value) {
    setProperty(UadpDataSetWriterMessageType.DATA_SET_OFFSET, value);
  }
}

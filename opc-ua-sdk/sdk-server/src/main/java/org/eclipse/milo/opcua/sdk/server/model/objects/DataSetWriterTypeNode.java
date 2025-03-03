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
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetFieldContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.KeyValuePair;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class DataSetWriterTypeNode extends BaseObjectTypeNode implements DataSetWriterType {
  public DataSetWriterTypeNode(
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

  public DataSetWriterTypeNode(
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
  public PropertyTypeNode getDataSetWriterIdNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(DataSetWriterType.DATA_SET_WRITER_ID);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UShort getDataSetWriterId() {
    return getProperty(DataSetWriterType.DATA_SET_WRITER_ID).orElse(null);
  }

  @Override
  public void setDataSetWriterId(UShort value) {
    setProperty(DataSetWriterType.DATA_SET_WRITER_ID, value);
  }

  @Override
  public PropertyTypeNode getDataSetFieldContentMaskNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(DataSetWriterType.DATA_SET_FIELD_CONTENT_MASK);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public DataSetFieldContentMask getDataSetFieldContentMask() {
    return getProperty(DataSetWriterType.DATA_SET_FIELD_CONTENT_MASK).orElse(null);
  }

  @Override
  public void setDataSetFieldContentMask(DataSetFieldContentMask value) {
    setProperty(DataSetWriterType.DATA_SET_FIELD_CONTENT_MASK, value);
  }

  @Override
  public PropertyTypeNode getKeyFrameCountNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(DataSetWriterType.KEY_FRAME_COUNT);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UInteger getKeyFrameCount() {
    return getProperty(DataSetWriterType.KEY_FRAME_COUNT).orElse(null);
  }

  @Override
  public void setKeyFrameCount(UInteger value) {
    setProperty(DataSetWriterType.KEY_FRAME_COUNT, value);
  }

  @Override
  public PropertyTypeNode getDataSetWriterPropertiesNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(DataSetWriterType.DATA_SET_WRITER_PROPERTIES);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public KeyValuePair[] getDataSetWriterProperties() {
    return getProperty(DataSetWriterType.DATA_SET_WRITER_PROPERTIES).orElse(null);
  }

  @Override
  public void setDataSetWriterProperties(KeyValuePair[] value) {
    setProperty(DataSetWriterType.DATA_SET_WRITER_PROPERTIES, value);
  }

  @Override
  public DataSetWriterTransportTypeNode getTransportSettingsNode() {
    Optional<ObjectNode> component =
        getObjectComponent("http://opcfoundation.org/UA/", "TransportSettings");
    return (DataSetWriterTransportTypeNode) component.orElse(null);
  }

  @Override
  public DataSetWriterMessageTypeNode getMessageSettingsNode() {
    Optional<ObjectNode> component =
        getObjectComponent("http://opcfoundation.org/UA/", "MessageSettings");
    return (DataSetWriterMessageTypeNode) component.orElse(null);
  }

  @Override
  public PubSubStatusTypeNode getStatusNode() {
    Optional<ObjectNode> component = getObjectComponent("http://opcfoundation.org/UA/", "Status");
    return (PubSubStatusTypeNode) component.orElse(null);
  }

  @Override
  public PubSubDiagnosticsDataSetWriterTypeNode getDiagnosticsNode() {
    Optional<ObjectNode> component =
        getObjectComponent("http://opcfoundation.org/UA/", "Diagnostics");
    return (PubSubDiagnosticsDataSetWriterTypeNode) component.orElse(null);
  }
}

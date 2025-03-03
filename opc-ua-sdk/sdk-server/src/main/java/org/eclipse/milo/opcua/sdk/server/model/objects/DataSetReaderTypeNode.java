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
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.core.nodes.ObjectNode;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetFieldContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.KeyValuePair;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class DataSetReaderTypeNode extends BaseObjectTypeNode implements DataSetReaderType {
  public DataSetReaderTypeNode(
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

  public DataSetReaderTypeNode(
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
  public PropertyTypeNode getPublisherIdNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(DataSetReaderType.PUBLISHER_ID);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Object getPublisherId() {
    return getProperty(DataSetReaderType.PUBLISHER_ID).orElse(null);
  }

  @Override
  public void setPublisherId(Object value) {
    setProperty(DataSetReaderType.PUBLISHER_ID, value);
  }

  @Override
  public PropertyTypeNode getWriterGroupIdNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(DataSetReaderType.WRITER_GROUP_ID);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UShort getWriterGroupId() {
    return getProperty(DataSetReaderType.WRITER_GROUP_ID).orElse(null);
  }

  @Override
  public void setWriterGroupId(UShort value) {
    setProperty(DataSetReaderType.WRITER_GROUP_ID, value);
  }

  @Override
  public PropertyTypeNode getDataSetWriterIdNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(DataSetReaderType.DATA_SET_WRITER_ID);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UShort getDataSetWriterId() {
    return getProperty(DataSetReaderType.DATA_SET_WRITER_ID).orElse(null);
  }

  @Override
  public void setDataSetWriterId(UShort value) {
    setProperty(DataSetReaderType.DATA_SET_WRITER_ID, value);
  }

  @Override
  public PropertyTypeNode getDataSetMetaDataNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(DataSetReaderType.DATA_SET_META_DATA);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public DataSetMetaDataType getDataSetMetaData() {
    return getProperty(DataSetReaderType.DATA_SET_META_DATA).orElse(null);
  }

  @Override
  public void setDataSetMetaData(DataSetMetaDataType value) {
    setProperty(DataSetReaderType.DATA_SET_META_DATA, value);
  }

  @Override
  public PropertyTypeNode getDataSetFieldContentMaskNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(DataSetReaderType.DATA_SET_FIELD_CONTENT_MASK);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public DataSetFieldContentMask getDataSetFieldContentMask() {
    return getProperty(DataSetReaderType.DATA_SET_FIELD_CONTENT_MASK).orElse(null);
  }

  @Override
  public void setDataSetFieldContentMask(DataSetFieldContentMask value) {
    setProperty(DataSetReaderType.DATA_SET_FIELD_CONTENT_MASK, value);
  }

  @Override
  public PropertyTypeNode getMessageReceiveTimeoutNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(DataSetReaderType.MESSAGE_RECEIVE_TIMEOUT);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public Double getMessageReceiveTimeout() {
    return getProperty(DataSetReaderType.MESSAGE_RECEIVE_TIMEOUT).orElse(null);
  }

  @Override
  public void setMessageReceiveTimeout(Double value) {
    setProperty(DataSetReaderType.MESSAGE_RECEIVE_TIMEOUT, value);
  }

  @Override
  public PropertyTypeNode getKeyFrameCountNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(DataSetReaderType.KEY_FRAME_COUNT);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public UInteger getKeyFrameCount() {
    return getProperty(DataSetReaderType.KEY_FRAME_COUNT).orElse(null);
  }

  @Override
  public void setKeyFrameCount(UInteger value) {
    setProperty(DataSetReaderType.KEY_FRAME_COUNT, value);
  }

  @Override
  public PropertyTypeNode getHeaderLayoutUriNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(DataSetReaderType.HEADER_LAYOUT_URI);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public String getHeaderLayoutUri() {
    return getProperty(DataSetReaderType.HEADER_LAYOUT_URI).orElse(null);
  }

  @Override
  public void setHeaderLayoutUri(String value) {
    setProperty(DataSetReaderType.HEADER_LAYOUT_URI, value);
  }

  @Override
  public PropertyTypeNode getSecurityModeNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(DataSetReaderType.SECURITY_MODE);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public MessageSecurityMode getSecurityMode() {
    return getProperty(DataSetReaderType.SECURITY_MODE).orElse(null);
  }

  @Override
  public void setSecurityMode(MessageSecurityMode value) {
    setProperty(DataSetReaderType.SECURITY_MODE, value);
  }

  @Override
  public PropertyTypeNode getSecurityGroupIdNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(DataSetReaderType.SECURITY_GROUP_ID);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public String getSecurityGroupId() {
    return getProperty(DataSetReaderType.SECURITY_GROUP_ID).orElse(null);
  }

  @Override
  public void setSecurityGroupId(String value) {
    setProperty(DataSetReaderType.SECURITY_GROUP_ID, value);
  }

  @Override
  public PropertyTypeNode getSecurityKeyServicesNode() {
    Optional<VariableNode> propertyNode = getPropertyNode(DataSetReaderType.SECURITY_KEY_SERVICES);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public EndpointDescription[] getSecurityKeyServices() {
    return getProperty(DataSetReaderType.SECURITY_KEY_SERVICES).orElse(null);
  }

  @Override
  public void setSecurityKeyServices(EndpointDescription[] value) {
    setProperty(DataSetReaderType.SECURITY_KEY_SERVICES, value);
  }

  @Override
  public PropertyTypeNode getDataSetReaderPropertiesNode() {
    Optional<VariableNode> propertyNode =
        getPropertyNode(DataSetReaderType.DATA_SET_READER_PROPERTIES);
    return (PropertyTypeNode) propertyNode.orElse(null);
  }

  @Override
  public KeyValuePair[] getDataSetReaderProperties() {
    return getProperty(DataSetReaderType.DATA_SET_READER_PROPERTIES).orElse(null);
  }

  @Override
  public void setDataSetReaderProperties(KeyValuePair[] value) {
    setProperty(DataSetReaderType.DATA_SET_READER_PROPERTIES, value);
  }

  @Override
  public DataSetReaderTransportTypeNode getTransportSettingsNode() {
    Optional<ObjectNode> component =
        getObjectComponent("http://opcfoundation.org/UA/", "TransportSettings");
    return (DataSetReaderTransportTypeNode) component.orElse(null);
  }

  @Override
  public DataSetReaderMessageTypeNode getMessageSettingsNode() {
    Optional<ObjectNode> component =
        getObjectComponent("http://opcfoundation.org/UA/", "MessageSettings");
    return (DataSetReaderMessageTypeNode) component.orElse(null);
  }

  @Override
  public PubSubStatusTypeNode getStatusNode() {
    Optional<ObjectNode> component = getObjectComponent("http://opcfoundation.org/UA/", "Status");
    return (PubSubStatusTypeNode) component.orElse(null);
  }

  @Override
  public PubSubDiagnosticsDataSetReaderTypeNode getDiagnosticsNode() {
    Optional<ObjectNode> component =
        getObjectComponent("http://opcfoundation.org/UA/", "Diagnostics");
    return (PubSubDiagnosticsDataSetReaderTypeNode) component.orElse(null);
  }

  @Override
  public SubscribedDataSetTypeNode getSubscribedDataSetNode() {
    Optional<ObjectNode> component =
        getObjectComponent("http://opcfoundation.org/UA/", "SubscribedDataSet");
    return (SubscribedDataSetTypeNode) component.orElse(null);
  }

  @Override
  public UaMethodNode getCreateTargetVariablesMethodNode() {
    Optional<UaNode> methodNode =
        findNode(
            "http://opcfoundation.org/UA/",
            "CreateTargetVariables",
            node -> node instanceof UaMethodNode,
            Reference.HAS_COMPONENT_PREDICATE);
    return (UaMethodNode) methodNode.orElse(null);
  }

  @Override
  public UaMethodNode getCreateDataSetMirrorMethodNode() {
    Optional<UaNode> methodNode =
        findNode(
            "http://opcfoundation.org/UA/",
            "CreateDataSetMirror",
            node -> node instanceof UaMethodNode,
            Reference.HAS_COMPONENT_PREDICATE);
    return (UaMethodNode) methodNode.orElse(null);
  }
}

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

import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.sdk.core.nodes.MethodNode;
import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.methods.Out;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetFieldContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.FieldTargetDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.KeyValuePair;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Lazy;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.8/#9.1.8.2">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.8/#9.1.8.2</a>
 */
public interface DataSetReaderType extends BaseObjectType {
  QualifiedProperty<Object> PUBLISHER_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "PublisherId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=24"),
          -1,
          Object.class);

  QualifiedProperty<UShort> WRITER_GROUP_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "WriterGroupId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=5"),
          -1,
          UShort.class);

  QualifiedProperty<UShort> DATA_SET_WRITER_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DataSetWriterId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=5"),
          -1,
          UShort.class);

  QualifiedProperty<DataSetMetaDataType> DATA_SET_META_DATA =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DataSetMetaData",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=14523"),
          -1,
          DataSetMetaDataType.class);

  QualifiedProperty<DataSetFieldContentMask> DATA_SET_FIELD_CONTENT_MASK =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DataSetFieldContentMask",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=15583"),
          -1,
          DataSetFieldContentMask.class);

  QualifiedProperty<Double> MESSAGE_RECEIVE_TIMEOUT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MessageReceiveTimeout",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  QualifiedProperty<UInteger> KEY_FRAME_COUNT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "KeyFrameCount",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<String> HEADER_LAYOUT_URI =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "HeaderLayoutUri",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<MessageSecurityMode> SECURITY_MODE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SecurityMode",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=302"),
          -1,
          MessageSecurityMode.class);

  QualifiedProperty<String> SECURITY_GROUP_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SecurityGroupId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<EndpointDescription[]> SECURITY_KEY_SERVICES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SecurityKeyServices",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=312"),
          1,
          EndpointDescription[].class);

  QualifiedProperty<KeyValuePair[]> DATA_SET_READER_PROPERTIES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DataSetReaderProperties",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=14533"),
          1,
          KeyValuePair[].class);

  Object getPublisherId();

  void setPublisherId(Object value);

  PropertyType getPublisherIdNode();

  UShort getWriterGroupId();

  void setWriterGroupId(UShort value);

  PropertyType getWriterGroupIdNode();

  UShort getDataSetWriterId();

  void setDataSetWriterId(UShort value);

  PropertyType getDataSetWriterIdNode();

  DataSetMetaDataType getDataSetMetaData();

  void setDataSetMetaData(DataSetMetaDataType value);

  PropertyType getDataSetMetaDataNode();

  DataSetFieldContentMask getDataSetFieldContentMask();

  void setDataSetFieldContentMask(DataSetFieldContentMask value);

  PropertyType getDataSetFieldContentMaskNode();

  Double getMessageReceiveTimeout();

  void setMessageReceiveTimeout(Double value);

  PropertyType getMessageReceiveTimeoutNode();

  UInteger getKeyFrameCount();

  void setKeyFrameCount(UInteger value);

  PropertyType getKeyFrameCountNode();

  String getHeaderLayoutUri();

  void setHeaderLayoutUri(String value);

  PropertyType getHeaderLayoutUriNode();

  MessageSecurityMode getSecurityMode();

  void setSecurityMode(MessageSecurityMode value);

  PropertyType getSecurityModeNode();

  String getSecurityGroupId();

  void setSecurityGroupId(String value);

  PropertyType getSecurityGroupIdNode();

  EndpointDescription[] getSecurityKeyServices();

  void setSecurityKeyServices(EndpointDescription[] value);

  PropertyType getSecurityKeyServicesNode();

  KeyValuePair[] getDataSetReaderProperties();

  void setDataSetReaderProperties(KeyValuePair[] value);

  PropertyType getDataSetReaderPropertiesNode();

  DataSetReaderTransportType getTransportSettingsNode();

  DataSetReaderMessageType getMessageSettingsNode();

  PubSubStatusType getStatusNode();

  PubSubDiagnosticsDataSetReaderType getDiagnosticsNode();

  SubscribedDataSetType getSubscribedDataSetNode();

  MethodNode getCreateTargetVariablesMethodNode();

  MethodNode getCreateDataSetMirrorMethodNode();

  abstract class CreateTargetVariablesMethod extends AbstractMethodInvocationHandler {
    private final Lazy<Argument[]> inputArguments = new Lazy<>();

    private final Lazy<Argument[]> outputArguments = new Lazy<>();

    public CreateTargetVariablesMethod(UaMethodNode node) {
      super(node);
    }

    @Override
    public Argument[] getInputArguments() {
      return inputArguments.get(
          () -> {
            NamespaceTable namespaceTable = getNode().getNodeContext().getNamespaceTable();

            return new Argument[] {
              new Argument(
                  "ConfigurationVersion",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=14593")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "")),
              new Argument(
                  "TargetVariablesToAdd",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=14744")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  1,
                  new UInteger[] {UInteger.valueOf(0)},
                  new LocalizedText("", ""))
            };
          });
    }

    @Override
    public Argument[] getOutputArguments() {
      return outputArguments.get(
          () -> {
            NamespaceTable namespaceTable = getNode().getNodeContext().getNamespaceTable();

            return new Argument[] {
              new Argument(
                  "AddResults",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=19")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  1,
                  new UInteger[] {UInteger.valueOf(0)},
                  new LocalizedText("", ""))
            };
          });
    }

    @Override
    protected Variant[] invoke(
        AbstractMethodInvocationHandler.InvocationContext context, Variant[] inputValues)
        throws UaException {
      ConfigurationVersionDataType configurationVersion =
          (ConfigurationVersionDataType) inputValues[0].getValue();
      FieldTargetDataType[] targetVariablesToAdd =
          (FieldTargetDataType[]) inputValues[1].getValue();
      Out<StatusCode[]> addResults = new Out<>();
      invoke(context, configurationVersion, targetVariablesToAdd, addResults);
      return new Variant[] {new Variant(addResults.get())};
    }

    protected abstract void invoke(
        AbstractMethodInvocationHandler.InvocationContext context,
        ConfigurationVersionDataType configurationVersion,
        FieldTargetDataType[] targetVariablesToAdd,
        Out<StatusCode[]> addResults)
        throws UaException;
  }

  abstract class CreateDataSetMirrorMethod extends AbstractMethodInvocationHandler {
    private final Lazy<Argument[]> inputArguments = new Lazy<>();

    private final Lazy<Argument[]> outputArguments = new Lazy<>();

    public CreateDataSetMirrorMethod(UaMethodNode node) {
      super(node);
    }

    @Override
    public Argument[] getInputArguments() {
      return inputArguments.get(
          () -> {
            NamespaceTable namespaceTable = getNode().getNodeContext().getNamespaceTable();

            return new Argument[] {
              new Argument(
                  "ParentNodeName",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "")),
              new Argument(
                  "RolePermissions",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=96")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  1,
                  new UInteger[] {UInteger.valueOf(0)},
                  new LocalizedText("", ""))
            };
          });
    }

    @Override
    public Argument[] getOutputArguments() {
      return outputArguments.get(
          () -> {
            NamespaceTable namespaceTable = getNode().getNodeContext().getNamespaceTable();

            return new Argument[] {
              new Argument(
                  "ParentNodeId",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", ""))
            };
          });
    }

    @Override
    protected Variant[] invoke(
        AbstractMethodInvocationHandler.InvocationContext context, Variant[] inputValues)
        throws UaException {
      String parentNodeName = (String) inputValues[0].getValue();
      RolePermissionType[] rolePermissions = (RolePermissionType[]) inputValues[1].getValue();
      Out<NodeId> parentNodeId = new Out<>();
      invoke(context, parentNodeName, rolePermissions, parentNodeId);
      return new Variant[] {new Variant(parentNodeId.get())};
    }

    protected abstract void invoke(
        AbstractMethodInvocationHandler.InvocationContext context,
        String parentNodeName,
        RolePermissionType[] rolePermissions,
        Out<NodeId> parentNodeId)
        throws UaException;
  }
}

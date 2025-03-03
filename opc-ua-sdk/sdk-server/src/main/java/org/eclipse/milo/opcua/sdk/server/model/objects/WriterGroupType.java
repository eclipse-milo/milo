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
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetWriterDataType;
import org.eclipse.milo.opcua.stack.core.util.Lazy;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.3">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.3</a>
 */
public interface WriterGroupType extends PubSubGroupType {
  QualifiedProperty<UShort> WRITER_GROUP_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "WriterGroupId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=5"),
          -1,
          UShort.class);

  QualifiedProperty<Double> PUBLISHING_INTERVAL =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "PublishingInterval",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  QualifiedProperty<Double> KEEP_ALIVE_TIME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "KeepAliveTime",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  QualifiedProperty<UByte> PRIORITY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Priority",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=3"),
          -1,
          UByte.class);

  QualifiedProperty<String[]> LOCALE_IDS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "LocaleIds",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=295"),
          1,
          String[].class);

  QualifiedProperty<String> HEADER_LAYOUT_URI =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "HeaderLayoutUri",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  UShort getWriterGroupId();

  void setWriterGroupId(UShort value);

  PropertyType getWriterGroupIdNode();

  Double getPublishingInterval();

  void setPublishingInterval(Double value);

  PropertyType getPublishingIntervalNode();

  Double getKeepAliveTime();

  void setKeepAliveTime(Double value);

  PropertyType getKeepAliveTimeNode();

  UByte getPriority();

  void setPriority(UByte value);

  PropertyType getPriorityNode();

  String[] getLocaleIds();

  void setLocaleIds(String[] value);

  PropertyType getLocaleIdsNode();

  String getHeaderLayoutUri();

  void setHeaderLayoutUri(String value);

  PropertyType getHeaderLayoutUriNode();

  WriterGroupTransportType getTransportSettingsNode();

  WriterGroupMessageType getMessageSettingsNode();

  PubSubDiagnosticsWriterGroupType getDiagnosticsNode();

  MethodNode getAddDataSetWriterMethodNode();

  MethodNode getRemoveDataSetWriterMethodNode();

  abstract class AddDataSetWriterMethod extends AbstractMethodInvocationHandler {
    private final Lazy<Argument[]> inputArguments = new Lazy<>();

    private final Lazy<Argument[]> outputArguments = new Lazy<>();

    public AddDataSetWriterMethod(UaMethodNode node) {
      super(node);
    }

    @Override
    public Argument[] getInputArguments() {
      return inputArguments.get(
          () -> {
            NamespaceTable namespaceTable = getNode().getNodeContext().getNamespaceTable();

            return new Argument[] {
              new Argument(
                  "Configuration",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=15597")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
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
                  "DataSetWriterNodeId",
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
      DataSetWriterDataType configuration = (DataSetWriterDataType) inputValues[0].getValue();
      Out<NodeId> dataSetWriterNodeId = new Out<>();
      invoke(context, configuration, dataSetWriterNodeId);
      return new Variant[] {new Variant(dataSetWriterNodeId.get())};
    }

    protected abstract void invoke(
        AbstractMethodInvocationHandler.InvocationContext context,
        DataSetWriterDataType configuration,
        Out<NodeId> dataSetWriterNodeId)
        throws UaException;
  }

  abstract class RemoveDataSetWriterMethod extends AbstractMethodInvocationHandler {
    private final Lazy<Argument[]> inputArguments = new Lazy<>();

    public RemoveDataSetWriterMethod(UaMethodNode node) {
      super(node);
    }

    @Override
    public Argument[] getInputArguments() {
      return inputArguments.get(
          () -> {
            NamespaceTable namespaceTable = getNode().getNodeContext().getNamespaceTable();

            return new Argument[] {
              new Argument(
                  "DataSetWriterNodeId",
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
    public Argument[] getOutputArguments() {
      return new Argument[] {};
    }

    @Override
    protected Variant[] invoke(
        AbstractMethodInvocationHandler.InvocationContext context, Variant[] inputValues)
        throws UaException {
      NodeId dataSetWriterNodeId = (NodeId) inputValues[0].getValue();
      invoke(context, dataSetWriterNodeId);
      return new Variant[] {};
    }

    protected abstract void invoke(
        AbstractMethodInvocationHandler.InvocationContext context, NodeId dataSetWriterNodeId)
        throws UaException;
  }
}

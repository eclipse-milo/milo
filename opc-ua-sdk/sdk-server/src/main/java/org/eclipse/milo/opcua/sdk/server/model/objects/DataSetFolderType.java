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

import org.eclipse.milo.opcua.sdk.core.nodes.MethodNode;
import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.methods.Out;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetFieldFlags;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishedVariableDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.SimpleAttributeOperand;
import org.eclipse.milo.opcua.stack.core.util.Lazy;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.1">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.1</a>
 */
public interface DataSetFolderType extends FolderType {
  MethodNode getAddPublishedDataItemsMethodNode();

  MethodNode getAddPublishedEventsMethodNode();

  MethodNode getAddPublishedDataItemsTemplateMethodNode();

  MethodNode getAddPublishedEventsTemplateMethodNode();

  MethodNode getRemovePublishedDataSetMethodNode();

  MethodNode getAddDataSetFolderMethodNode();

  MethodNode getRemoveDataSetFolderMethodNode();

  abstract class AddPublishedDataItemsMethod extends AbstractMethodInvocationHandler {
    private final Lazy<Argument[]> inputArguments = new Lazy<>();

    private final Lazy<Argument[]> outputArguments = new Lazy<>();

    public AddPublishedDataItemsMethod(UaMethodNode node) {
      super(node);
    }

    @Override
    public Argument[] getInputArguments() {
      return inputArguments.get(
          () -> {
            NamespaceTable namespaceTable = getNode().getNodeContext().getNamespaceTable();

            return new Argument[] {
              new Argument(
                  "Name",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "")),
              new Argument(
                  "FieldNameAliases",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  1,
                  new UInteger[] {UInteger.valueOf(0)},
                  new LocalizedText("", "")),
              new Argument(
                  "FieldFlags",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=15904")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  1,
                  new UInteger[] {UInteger.valueOf(0)},
                  new LocalizedText("", "")),
              new Argument(
                  "VariablesToAdd",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=14273")
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
                  "DataSetNodeId",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "")),
              new Argument(
                  "ConfigurationVersion",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=14593")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "")),
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
      String name = (String) inputValues[0].getValue();
      String[] fieldNameAliases = (String[]) inputValues[1].getValue();
      DataSetFieldFlags[] fieldFlags = (DataSetFieldFlags[]) inputValues[2].getValue();
      PublishedVariableDataType[] variablesToAdd =
          (PublishedVariableDataType[]) inputValues[3].getValue();
      Out<NodeId> dataSetNodeId = new Out<>();
      Out<ConfigurationVersionDataType> configurationVersion = new Out<>();
      Out<StatusCode[]> addResults = new Out<>();
      invoke(
          context,
          name,
          fieldNameAliases,
          fieldFlags,
          variablesToAdd,
          dataSetNodeId,
          configurationVersion,
          addResults);
      return new Variant[] {
        new Variant(dataSetNodeId.get()),
        new Variant(configurationVersion.get()),
        new Variant(addResults.get())
      };
    }

    protected abstract void invoke(
        AbstractMethodInvocationHandler.InvocationContext context,
        String name,
        String[] fieldNameAliases,
        DataSetFieldFlags[] fieldFlags,
        PublishedVariableDataType[] variablesToAdd,
        Out<NodeId> dataSetNodeId,
        Out<ConfigurationVersionDataType> configurationVersion,
        Out<StatusCode[]> addResults)
        throws UaException;
  }

  abstract class AddPublishedEventsMethod extends AbstractMethodInvocationHandler {
    private final Lazy<Argument[]> inputArguments = new Lazy<>();

    private final Lazy<Argument[]> outputArguments = new Lazy<>();

    public AddPublishedEventsMethod(UaMethodNode node) {
      super(node);
    }

    @Override
    public Argument[] getInputArguments() {
      return inputArguments.get(
          () -> {
            NamespaceTable namespaceTable = getNode().getNodeContext().getNamespaceTable();

            return new Argument[] {
              new Argument(
                  "Name",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "")),
              new Argument(
                  "EventNotifier",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "")),
              new Argument(
                  "FieldNameAliases",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  1,
                  new UInteger[] {UInteger.valueOf(0)},
                  new LocalizedText("", "")),
              new Argument(
                  "FieldFlags",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=15904")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  1,
                  new UInteger[] {UInteger.valueOf(0)},
                  new LocalizedText("", "")),
              new Argument(
                  "SelectedFields",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=601")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  1,
                  new UInteger[] {UInteger.valueOf(0)},
                  new LocalizedText("", "")),
              new Argument(
                  "Filter",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=586")
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
                  "ConfigurationVersion",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=14593")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "")),
              new Argument(
                  "DataSetNodeId",
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
      String name = (String) inputValues[0].getValue();
      NodeId eventNotifier = (NodeId) inputValues[1].getValue();
      String[] fieldNameAliases = (String[]) inputValues[2].getValue();
      DataSetFieldFlags[] fieldFlags = (DataSetFieldFlags[]) inputValues[3].getValue();
      SimpleAttributeOperand[] selectedFields =
          (SimpleAttributeOperand[]) inputValues[4].getValue();
      ContentFilter filter = (ContentFilter) inputValues[5].getValue();
      Out<ConfigurationVersionDataType> configurationVersion = new Out<>();
      Out<NodeId> dataSetNodeId = new Out<>();
      invoke(
          context,
          name,
          eventNotifier,
          fieldNameAliases,
          fieldFlags,
          selectedFields,
          filter,
          configurationVersion,
          dataSetNodeId);
      return new Variant[] {
        new Variant(configurationVersion.get()), new Variant(dataSetNodeId.get())
      };
    }

    protected abstract void invoke(
        AbstractMethodInvocationHandler.InvocationContext context,
        String name,
        NodeId eventNotifier,
        String[] fieldNameAliases,
        DataSetFieldFlags[] fieldFlags,
        SimpleAttributeOperand[] selectedFields,
        ContentFilter filter,
        Out<ConfigurationVersionDataType> configurationVersion,
        Out<NodeId> dataSetNodeId)
        throws UaException;
  }

  abstract class AddPublishedDataItemsTemplateMethod extends AbstractMethodInvocationHandler {
    private final Lazy<Argument[]> inputArguments = new Lazy<>();

    private final Lazy<Argument[]> outputArguments = new Lazy<>();

    public AddPublishedDataItemsTemplateMethod(UaMethodNode node) {
      super(node);
    }

    @Override
    public Argument[] getInputArguments() {
      return inputArguments.get(
          () -> {
            NamespaceTable namespaceTable = getNode().getNodeContext().getNamespaceTable();

            return new Argument[] {
              new Argument(
                  "Name",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "")),
              new Argument(
                  "DataSetMetaData",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=14523")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "")),
              new Argument(
                  "VariablesToAdd",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=14273")
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
                  "DataSetNodeId",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "")),
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
      String name = (String) inputValues[0].getValue();
      DataSetMetaDataType dataSetMetaData = (DataSetMetaDataType) inputValues[1].getValue();
      PublishedVariableDataType[] variablesToAdd =
          (PublishedVariableDataType[]) inputValues[2].getValue();
      Out<NodeId> dataSetNodeId = new Out<>();
      Out<StatusCode[]> addResults = new Out<>();
      invoke(context, name, dataSetMetaData, variablesToAdd, dataSetNodeId, addResults);
      return new Variant[] {new Variant(dataSetNodeId.get()), new Variant(addResults.get())};
    }

    protected abstract void invoke(
        AbstractMethodInvocationHandler.InvocationContext context,
        String name,
        DataSetMetaDataType dataSetMetaData,
        PublishedVariableDataType[] variablesToAdd,
        Out<NodeId> dataSetNodeId,
        Out<StatusCode[]> addResults)
        throws UaException;
  }

  abstract class AddPublishedEventsTemplateMethod extends AbstractMethodInvocationHandler {
    private final Lazy<Argument[]> inputArguments = new Lazy<>();

    private final Lazy<Argument[]> outputArguments = new Lazy<>();

    public AddPublishedEventsTemplateMethod(UaMethodNode node) {
      super(node);
    }

    @Override
    public Argument[] getInputArguments() {
      return inputArguments.get(
          () -> {
            NamespaceTable namespaceTable = getNode().getNodeContext().getNamespaceTable();

            return new Argument[] {
              new Argument(
                  "Name",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "")),
              new Argument(
                  "DataSetMetaData",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=14523")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "")),
              new Argument(
                  "EventNotifier",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "")),
              new Argument(
                  "SelectedFields",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=601")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  1,
                  new UInteger[] {UInteger.valueOf(0)},
                  new LocalizedText("", "")),
              new Argument(
                  "Filter",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=586")
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
                  "DataSetNodeId",
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
      String name = (String) inputValues[0].getValue();
      DataSetMetaDataType dataSetMetaData = (DataSetMetaDataType) inputValues[1].getValue();
      NodeId eventNotifier = (NodeId) inputValues[2].getValue();
      SimpleAttributeOperand[] selectedFields =
          (SimpleAttributeOperand[]) inputValues[3].getValue();
      ContentFilter filter = (ContentFilter) inputValues[4].getValue();
      Out<NodeId> dataSetNodeId = new Out<>();
      invoke(context, name, dataSetMetaData, eventNotifier, selectedFields, filter, dataSetNodeId);
      return new Variant[] {new Variant(dataSetNodeId.get())};
    }

    protected abstract void invoke(
        AbstractMethodInvocationHandler.InvocationContext context,
        String name,
        DataSetMetaDataType dataSetMetaData,
        NodeId eventNotifier,
        SimpleAttributeOperand[] selectedFields,
        ContentFilter filter,
        Out<NodeId> dataSetNodeId)
        throws UaException;
  }

  abstract class RemovePublishedDataSetMethod extends AbstractMethodInvocationHandler {
    private final Lazy<Argument[]> inputArguments = new Lazy<>();

    public RemovePublishedDataSetMethod(UaMethodNode node) {
      super(node);
    }

    @Override
    public Argument[] getInputArguments() {
      return inputArguments.get(
          () -> {
            NamespaceTable namespaceTable = getNode().getNodeContext().getNamespaceTable();

            return new Argument[] {
              new Argument(
                  "DataSetNodeId",
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
      NodeId dataSetNodeId = (NodeId) inputValues[0].getValue();
      invoke(context, dataSetNodeId);
      return new Variant[] {};
    }

    protected abstract void invoke(
        AbstractMethodInvocationHandler.InvocationContext context, NodeId dataSetNodeId)
        throws UaException;
  }

  abstract class AddDataSetFolderMethod extends AbstractMethodInvocationHandler {
    private final Lazy<Argument[]> inputArguments = new Lazy<>();

    private final Lazy<Argument[]> outputArguments = new Lazy<>();

    public AddDataSetFolderMethod(UaMethodNode node) {
      super(node);
    }

    @Override
    public Argument[] getInputArguments() {
      return inputArguments.get(
          () -> {
            NamespaceTable namespaceTable = getNode().getNodeContext().getNamespaceTable();

            return new Argument[] {
              new Argument(
                  "Name",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12")
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
                  "DataSetFolderNodeId",
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
      String name = (String) inputValues[0].getValue();
      Out<NodeId> dataSetFolderNodeId = new Out<>();
      invoke(context, name, dataSetFolderNodeId);
      return new Variant[] {new Variant(dataSetFolderNodeId.get())};
    }

    protected abstract void invoke(
        AbstractMethodInvocationHandler.InvocationContext context,
        String name,
        Out<NodeId> dataSetFolderNodeId)
        throws UaException;
  }

  abstract class RemoveDataSetFolderMethod extends AbstractMethodInvocationHandler {
    private final Lazy<Argument[]> inputArguments = new Lazy<>();

    public RemoveDataSetFolderMethod(UaMethodNode node) {
      super(node);
    }

    @Override
    public Argument[] getInputArguments() {
      return inputArguments.get(
          () -> {
            NamespaceTable namespaceTable = getNode().getNodeContext().getNamespaceTable();

            return new Argument[] {
              new Argument(
                  "DataSetFolderNodeId",
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
      NodeId dataSetFolderNodeId = (NodeId) inputValues[0].getValue();
      invoke(context, dataSetFolderNodeId);
      return new Variant[] {};
    }

    protected abstract void invoke(
        AbstractMethodInvocationHandler.InvocationContext context, NodeId dataSetFolderNodeId)
        throws UaException;
  }
}

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

import org.eclipse.milo.opcua.sdk.core.nodes.MethodNode;
import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.methods.Out;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.StandaloneSubscribedDataSetDataType;
import org.eclipse.milo.opcua.stack.core.util.Lazy;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.4.1">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.4.1</a>
 */
public interface SubscribedDataSetFolderType extends FolderType {
  MethodNode getAddSubscribedDataSetMethodNode();

  MethodNode getRemoveSubscribedDataSetMethodNode();

  MethodNode getAddDataSetFolderMethodNode();

  MethodNode getRemoveDataSetFolderMethodNode();

  abstract class AddSubscribedDataSetMethod extends AbstractMethodInvocationHandler {
    private final Lazy<Argument[]> inputArguments = new Lazy<>();

    private final Lazy<Argument[]> outputArguments = new Lazy<>();

    public AddSubscribedDataSetMethod(UaMethodNode node) {
      super(node);
    }

    @Override
    public Argument[] getInputArguments() {
      return inputArguments.get(
          () -> {
            NamespaceTable namespaceTable = getNode().getNodeContext().getNamespaceTable();

            return new Argument[] {
              new Argument(
                  "SubscribedDataSet",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=23600")
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
                  "SubscribedDataSetNodeId",
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
      StandaloneSubscribedDataSetDataType subscribedDataSet =
          (StandaloneSubscribedDataSetDataType) inputValues[0].getValue();
      Out<NodeId> subscribedDataSetNodeId = new Out<>();
      invoke(context, subscribedDataSet, subscribedDataSetNodeId);
      return new Variant[] {new Variant(subscribedDataSetNodeId.get())};
    }

    protected abstract void invoke(
        AbstractMethodInvocationHandler.InvocationContext context,
        StandaloneSubscribedDataSetDataType subscribedDataSet,
        Out<NodeId> subscribedDataSetNodeId)
        throws UaException;
  }

  abstract class RemoveSubscribedDataSetMethod extends AbstractMethodInvocationHandler {
    private final Lazy<Argument[]> inputArguments = new Lazy<>();

    public RemoveSubscribedDataSetMethod(UaMethodNode node) {
      super(node);
    }

    @Override
    public Argument[] getInputArguments() {
      return inputArguments.get(
          () -> {
            NamespaceTable namespaceTable = getNode().getNodeContext().getNamespaceTable();

            return new Argument[] {
              new Argument(
                  "SubscribedDataSetNodeId",
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
      NodeId subscribedDataSetNodeId = (NodeId) inputValues[0].getValue();
      invoke(context, subscribedDataSetNodeId);
      return new Variant[] {};
    }

    protected abstract void invoke(
        AbstractMethodInvocationHandler.InvocationContext context, NodeId subscribedDataSetNodeId)
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

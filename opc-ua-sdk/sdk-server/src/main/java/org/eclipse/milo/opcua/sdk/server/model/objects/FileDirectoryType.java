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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.util.Lazy;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part20/4.3.1">https://reference.opcfoundation.org/v105/Core/docs/Part20/4.3.1</a>
 */
public interface FileDirectoryType extends FolderType {
  MethodNode getCreateDirectoryMethodNode();

  MethodNode getCreateFileMethodNode();

  MethodNode getDeleteFileSystemObjectMethodNode();

  MethodNode getMoveOrCopyMethodNode();

  abstract class CreateDirectoryMethod extends AbstractMethodInvocationHandler {
    private final Lazy<Argument[]> inputArguments = new Lazy<>();

    private final Lazy<Argument[]> outputArguments = new Lazy<>();

    public CreateDirectoryMethod(UaMethodNode node) {
      super(node);
    }

    @Override
    public Argument[] getInputArguments() {
      return inputArguments.get(
          () -> {
            NamespaceTable namespaceTable = getNode().getNodeContext().getNamespaceTable();

            return new Argument[] {
              new Argument(
                  "DirectoryName",
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
                  "DirectoryNodeId",
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
      String directoryName = (String) inputValues[0].getValue();
      Out<NodeId> directoryNodeId = new Out<>();
      invoke(context, directoryName, directoryNodeId);
      return new Variant[] {new Variant(directoryNodeId.get())};
    }

    protected abstract void invoke(
        AbstractMethodInvocationHandler.InvocationContext context,
        String directoryName,
        Out<NodeId> directoryNodeId)
        throws UaException;
  }

  abstract class CreateFileMethod extends AbstractMethodInvocationHandler {
    private final Lazy<Argument[]> inputArguments = new Lazy<>();

    private final Lazy<Argument[]> outputArguments = new Lazy<>();

    public CreateFileMethod(UaMethodNode node) {
      super(node);
    }

    @Override
    public Argument[] getInputArguments() {
      return inputArguments.get(
          () -> {
            NamespaceTable namespaceTable = getNode().getNodeContext().getNamespaceTable();

            return new Argument[] {
              new Argument(
                  "FileName",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "")),
              new Argument(
                  "RequestFileOpen",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1")
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
                  "FileNodeId",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "")),
              new Argument(
                  "FileHandle",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7")
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
      String fileName = (String) inputValues[0].getValue();
      Boolean requestFileOpen = (Boolean) inputValues[1].getValue();
      Out<NodeId> fileNodeId = new Out<>();
      Out<UInteger> fileHandle = new Out<>();
      invoke(context, fileName, requestFileOpen, fileNodeId, fileHandle);
      return new Variant[] {new Variant(fileNodeId.get()), new Variant(fileHandle.get())};
    }

    protected abstract void invoke(
        AbstractMethodInvocationHandler.InvocationContext context,
        String fileName,
        Boolean requestFileOpen,
        Out<NodeId> fileNodeId,
        Out<UInteger> fileHandle)
        throws UaException;
  }

  abstract class DeleteFileSystemObjectMethod extends AbstractMethodInvocationHandler {
    private final Lazy<Argument[]> inputArguments = new Lazy<>();

    public DeleteFileSystemObjectMethod(UaMethodNode node) {
      super(node);
    }

    @Override
    public Argument[] getInputArguments() {
      return inputArguments.get(
          () -> {
            NamespaceTable namespaceTable = getNode().getNodeContext().getNamespaceTable();

            return new Argument[] {
              new Argument(
                  "ObjectToDelete",
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
      NodeId objectToDelete = (NodeId) inputValues[0].getValue();
      invoke(context, objectToDelete);
      return new Variant[] {};
    }

    protected abstract void invoke(
        AbstractMethodInvocationHandler.InvocationContext context, NodeId objectToDelete)
        throws UaException;
  }

  abstract class MoveOrCopyMethod extends AbstractMethodInvocationHandler {
    private final Lazy<Argument[]> inputArguments = new Lazy<>();

    private final Lazy<Argument[]> outputArguments = new Lazy<>();

    public MoveOrCopyMethod(UaMethodNode node) {
      super(node);
    }

    @Override
    public Argument[] getInputArguments() {
      return inputArguments.get(
          () -> {
            NamespaceTable namespaceTable = getNode().getNodeContext().getNamespaceTable();

            return new Argument[] {
              new Argument(
                  "ObjectToMoveOrCopy",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "")),
              new Argument(
                  "TargetDirectory",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "")),
              new Argument(
                  "CreateCopy",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "")),
              new Argument(
                  "NewName",
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
                  "NewNodeId",
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
      NodeId objectToMoveOrCopy = (NodeId) inputValues[0].getValue();
      NodeId targetDirectory = (NodeId) inputValues[1].getValue();
      Boolean createCopy = (Boolean) inputValues[2].getValue();
      String newName = (String) inputValues[3].getValue();
      Out<NodeId> newNodeId = new Out<>();
      invoke(context, objectToMoveOrCopy, targetDirectory, createCopy, newName, newNodeId);
      return new Variant[] {new Variant(newNodeId.get())};
    }

    protected abstract void invoke(
        AbstractMethodInvocationHandler.InvocationContext context,
        NodeId objectToMoveOrCopy,
        NodeId targetDirectory,
        Boolean createCopy,
        String newName,
        Out<NodeId> newNodeId)
        throws UaException;
  }
}

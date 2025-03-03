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
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.util.Lazy;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.1">https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.1</a>
 */
public interface FileType extends BaseObjectType {
  QualifiedProperty<ULong> SIZE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Size",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=9"),
          -1,
          ULong.class);

  QualifiedProperty<Boolean> WRITABLE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Writable",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> USER_WRITABLE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "UserWritable",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<UShort> OPEN_COUNT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "OpenCount",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=5"),
          -1,
          UShort.class);

  QualifiedProperty<String> MIME_TYPE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MimeType",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<UInteger> MAX_BYTE_STRING_LENGTH =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxByteStringLength",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<DateTime> LAST_MODIFIED_TIME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "LastModifiedTime",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=13"),
          -1,
          DateTime.class);

  ULong getSize();

  void setSize(ULong value);

  PropertyType getSizeNode();

  Boolean getWritable();

  void setWritable(Boolean value);

  PropertyType getWritableNode();

  Boolean getUserWritable();

  void setUserWritable(Boolean value);

  PropertyType getUserWritableNode();

  UShort getOpenCount();

  void setOpenCount(UShort value);

  PropertyType getOpenCountNode();

  String getMimeType();

  void setMimeType(String value);

  PropertyType getMimeTypeNode();

  UInteger getMaxByteStringLength();

  void setMaxByteStringLength(UInteger value);

  PropertyType getMaxByteStringLengthNode();

  DateTime getLastModifiedTime();

  void setLastModifiedTime(DateTime value);

  PropertyType getLastModifiedTimeNode();

  MethodNode getOpenMethodNode();

  MethodNode getCloseMethodNode();

  MethodNode getReadMethodNode();

  MethodNode getWriteMethodNode();

  MethodNode getGetPositionMethodNode();

  MethodNode getSetPositionMethodNode();

  abstract class OpenMethod extends AbstractMethodInvocationHandler {
    private final Lazy<Argument[]> inputArguments = new Lazy<>();

    private final Lazy<Argument[]> outputArguments = new Lazy<>();

    public OpenMethod(UaMethodNode node) {
      super(node);
    }

    @Override
    public Argument[] getInputArguments() {
      return inputArguments.get(
          () -> {
            NamespaceTable namespaceTable = getNode().getNodeContext().getNamespaceTable();

            return new Argument[] {
              new Argument(
                  "Mode",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=3")
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
      UByte mode = (UByte) inputValues[0].getValue();
      Out<UInteger> fileHandle = new Out<>();
      invoke(context, mode, fileHandle);
      return new Variant[] {new Variant(fileHandle.get())};
    }

    protected abstract void invoke(
        AbstractMethodInvocationHandler.InvocationContext context,
        UByte mode,
        Out<UInteger> fileHandle)
        throws UaException;
  }

  abstract class CloseMethod extends AbstractMethodInvocationHandler {
    private final Lazy<Argument[]> inputArguments = new Lazy<>();

    public CloseMethod(UaMethodNode node) {
      super(node);
    }

    @Override
    public Argument[] getInputArguments() {
      return inputArguments.get(
          () -> {
            NamespaceTable namespaceTable = getNode().getNodeContext().getNamespaceTable();

            return new Argument[] {
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
    public Argument[] getOutputArguments() {
      return new Argument[] {};
    }

    @Override
    protected Variant[] invoke(
        AbstractMethodInvocationHandler.InvocationContext context, Variant[] inputValues)
        throws UaException {
      UInteger fileHandle = (UInteger) inputValues[0].getValue();
      invoke(context, fileHandle);
      return new Variant[] {};
    }

    protected abstract void invoke(
        AbstractMethodInvocationHandler.InvocationContext context, UInteger fileHandle)
        throws UaException;
  }

  abstract class ReadMethod extends AbstractMethodInvocationHandler {
    private final Lazy<Argument[]> inputArguments = new Lazy<>();

    private final Lazy<Argument[]> outputArguments = new Lazy<>();

    public ReadMethod(UaMethodNode node) {
      super(node);
    }

    @Override
    public Argument[] getInputArguments() {
      return inputArguments.get(
          () -> {
            NamespaceTable namespaceTable = getNode().getNodeContext().getNamespaceTable();

            return new Argument[] {
              new Argument(
                  "FileHandle",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "")),
              new Argument(
                  "Length",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=6")
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
                  "Data",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=15")
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
      UInteger fileHandle = (UInteger) inputValues[0].getValue();
      Integer length = (Integer) inputValues[1].getValue();
      Out<ByteString> data = new Out<>();
      invoke(context, fileHandle, length, data);
      return new Variant[] {new Variant(data.get())};
    }

    protected abstract void invoke(
        AbstractMethodInvocationHandler.InvocationContext context,
        UInteger fileHandle,
        Integer length,
        Out<ByteString> data)
        throws UaException;
  }

  abstract class WriteMethod extends AbstractMethodInvocationHandler {
    private final Lazy<Argument[]> inputArguments = new Lazy<>();

    public WriteMethod(UaMethodNode node) {
      super(node);
    }

    @Override
    public Argument[] getInputArguments() {
      return inputArguments.get(
          () -> {
            NamespaceTable namespaceTable = getNode().getNodeContext().getNamespaceTable();

            return new Argument[] {
              new Argument(
                  "FileHandle",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "")),
              new Argument(
                  "Data",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=15")
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
      UInteger fileHandle = (UInteger) inputValues[0].getValue();
      ByteString data = (ByteString) inputValues[1].getValue();
      invoke(context, fileHandle, data);
      return new Variant[] {};
    }

    protected abstract void invoke(
        AbstractMethodInvocationHandler.InvocationContext context,
        UInteger fileHandle,
        ByteString data)
        throws UaException;
  }

  abstract class GetPositionMethod extends AbstractMethodInvocationHandler {
    private final Lazy<Argument[]> inputArguments = new Lazy<>();

    private final Lazy<Argument[]> outputArguments = new Lazy<>();

    public GetPositionMethod(UaMethodNode node) {
      super(node);
    }

    @Override
    public Argument[] getInputArguments() {
      return inputArguments.get(
          () -> {
            NamespaceTable namespaceTable = getNode().getNodeContext().getNamespaceTable();

            return new Argument[] {
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
    public Argument[] getOutputArguments() {
      return outputArguments.get(
          () -> {
            NamespaceTable namespaceTable = getNode().getNodeContext().getNamespaceTable();

            return new Argument[] {
              new Argument(
                  "Position",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=9")
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
      UInteger fileHandle = (UInteger) inputValues[0].getValue();
      Out<ULong> position = new Out<>();
      invoke(context, fileHandle, position);
      return new Variant[] {new Variant(position.get())};
    }

    protected abstract void invoke(
        AbstractMethodInvocationHandler.InvocationContext context,
        UInteger fileHandle,
        Out<ULong> position)
        throws UaException;
  }

  abstract class SetPositionMethod extends AbstractMethodInvocationHandler {
    private final Lazy<Argument[]> inputArguments = new Lazy<>();

    public SetPositionMethod(UaMethodNode node) {
      super(node);
    }

    @Override
    public Argument[] getInputArguments() {
      return inputArguments.get(
          () -> {
            NamespaceTable namespaceTable = getNode().getNodeContext().getNamespaceTable();

            return new Argument[] {
              new Argument(
                  "FileHandle",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7")
                      .toNodeId(namespaceTable)
                      .orElseThrow(),
                  -1,
                  null,
                  new LocalizedText("", "")),
              new Argument(
                  "Position",
                  ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=9")
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
      UInteger fileHandle = (UInteger) inputValues[0].getValue();
      ULong position = (ULong) inputValues[1].getValue();
      invoke(context, fileHandle, position);
      return new Variant[] {};
    }

    protected abstract void invoke(
        AbstractMethodInvocationHandler.InvocationContext context,
        UInteger fileHandle,
        ULong position)
        throws UaException;
  }
}

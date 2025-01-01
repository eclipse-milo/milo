/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaNode;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class FileTypeNode extends BaseObjectTypeNode implements FileType {
  public FileTypeNode(
      OpcUaClient client,
      NodeId nodeId,
      NodeClass nodeClass,
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
        client,
        nodeId,
        nodeClass,
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

  @Override
  public ULong getSize() throws UaException {
    PropertyTypeNode node = getSizeNode();
    return (ULong) node.getValue().getValue().getValue();
  }

  @Override
  public void setSize(ULong value) throws UaException {
    PropertyTypeNode node = getSizeNode();
    node.setValue(new Variant(value));
  }

  @Override
  public ULong readSize() throws UaException {
    try {
      return readSizeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeSize(ULong value) throws UaException {
    try {
      writeSizeAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends ULong> readSizeAsync() {
    return getSizeNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (ULong) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeSizeAsync(ULong size) {
    DataValue value = DataValue.valueOnly(new Variant(size));
    return getSizeNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getSizeNode() throws UaException {
    try {
      return getSizeNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getSizeNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "Size", ExpandedNodeId.parse("ns=0;i=46"), false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Boolean getWritable() throws UaException {
    PropertyTypeNode node = getWritableNode();
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setWritable(Boolean value) throws UaException {
    PropertyTypeNode node = getWritableNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Boolean readWritable() throws UaException {
    try {
      return readWritableAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeWritable(Boolean value) throws UaException {
    try {
      writeWritableAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Boolean> readWritableAsync() {
    return getWritableNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Boolean) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeWritableAsync(Boolean writable) {
    DataValue value = DataValue.valueOnly(new Variant(writable));
    return getWritableNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getWritableNode() throws UaException {
    try {
      return getWritableNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getWritableNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "Writable", ExpandedNodeId.parse("ns=0;i=46"), false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Boolean getUserWritable() throws UaException {
    PropertyTypeNode node = getUserWritableNode();
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setUserWritable(Boolean value) throws UaException {
    PropertyTypeNode node = getUserWritableNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Boolean readUserWritable() throws UaException {
    try {
      return readUserWritableAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeUserWritable(Boolean value) throws UaException {
    try {
      writeUserWritableAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Boolean> readUserWritableAsync() {
    return getUserWritableNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Boolean) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeUserWritableAsync(Boolean userWritable) {
    DataValue value = DataValue.valueOnly(new Variant(userWritable));
    return getUserWritableNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getUserWritableNode() throws UaException {
    try {
      return getUserWritableNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getUserWritableNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "UserWritable",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public UShort getOpenCount() throws UaException {
    PropertyTypeNode node = getOpenCountNode();
    return (UShort) node.getValue().getValue().getValue();
  }

  @Override
  public void setOpenCount(UShort value) throws UaException {
    PropertyTypeNode node = getOpenCountNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UShort readOpenCount() throws UaException {
    try {
      return readOpenCountAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeOpenCount(UShort value) throws UaException {
    try {
      writeOpenCountAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UShort> readOpenCountAsync() {
    return getOpenCountNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UShort) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeOpenCountAsync(UShort openCount) {
    DataValue value = DataValue.valueOnly(new Variant(openCount));
    return getOpenCountNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getOpenCountNode() throws UaException {
    try {
      return getOpenCountNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getOpenCountNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "OpenCount", ExpandedNodeId.parse("ns=0;i=46"), false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public String getMimeType() throws UaException {
    PropertyTypeNode node = getMimeTypeNode();
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setMimeType(String value) throws UaException {
    PropertyTypeNode node = getMimeTypeNode();
    node.setValue(new Variant(value));
  }

  @Override
  public String readMimeType() throws UaException {
    try {
      return readMimeTypeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeMimeType(String value) throws UaException {
    try {
      writeMimeTypeAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends String> readMimeTypeAsync() {
    return getMimeTypeNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (String) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeMimeTypeAsync(String mimeType) {
    DataValue value = DataValue.valueOnly(new Variant(mimeType));
    return getMimeTypeNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getMimeTypeNode() throws UaException {
    try {
      return getMimeTypeNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMimeTypeNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "MimeType", ExpandedNodeId.parse("ns=0;i=46"), false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public UInteger getMaxByteStringLength() throws UaException {
    PropertyTypeNode node = getMaxByteStringLengthNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxByteStringLength(UInteger value) throws UaException {
    PropertyTypeNode node = getMaxByteStringLengthNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readMaxByteStringLength() throws UaException {
    try {
      return readMaxByteStringLengthAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeMaxByteStringLength(UInteger value) throws UaException {
    try {
      writeMaxByteStringLengthAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readMaxByteStringLengthAsync() {
    return getMaxByteStringLengthNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxByteStringLengthAsync(UInteger maxByteStringLength) {
    DataValue value = DataValue.valueOnly(new Variant(maxByteStringLength));
    return getMaxByteStringLengthNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getMaxByteStringLengthNode() throws UaException {
    try {
      return getMaxByteStringLengthNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMaxByteStringLengthNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "MaxByteStringLength",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public DateTime getLastModifiedTime() throws UaException {
    PropertyTypeNode node = getLastModifiedTimeNode();
    return (DateTime) node.getValue().getValue().getValue();
  }

  @Override
  public void setLastModifiedTime(DateTime value) throws UaException {
    PropertyTypeNode node = getLastModifiedTimeNode();
    node.setValue(new Variant(value));
  }

  @Override
  public DateTime readLastModifiedTime() throws UaException {
    try {
      return readLastModifiedTimeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeLastModifiedTime(DateTime value) throws UaException {
    try {
      writeLastModifiedTimeAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends DateTime> readLastModifiedTimeAsync() {
    return getLastModifiedTimeNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (DateTime) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeLastModifiedTimeAsync(DateTime lastModifiedTime) {
    DataValue value = DataValue.valueOnly(new Variant(lastModifiedTime));
    return getLastModifiedTimeNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getLastModifiedTimeNode() throws UaException {
    try {
      return getLastModifiedTimeNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getLastModifiedTimeNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "LastModifiedTime",
            ExpandedNodeId.parse("ns=0;i=46"),
            false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }
}

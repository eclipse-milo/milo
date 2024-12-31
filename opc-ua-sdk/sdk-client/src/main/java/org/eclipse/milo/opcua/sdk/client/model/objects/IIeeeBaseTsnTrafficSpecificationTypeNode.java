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
import org.eclipse.milo.opcua.sdk.client.model.variables.BaseDataVariableTypeNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaNode;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.UnsignedRationalNumber;

public class IIeeeBaseTsnTrafficSpecificationTypeNode extends BaseInterfaceTypeNode
    implements IIeeeBaseTsnTrafficSpecificationType {
  public IIeeeBaseTsnTrafficSpecificationTypeNode(
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
  public UShort getMaxIntervalFrames() throws UaException {
    BaseDataVariableTypeNode node = getMaxIntervalFramesNode();
    return (UShort) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxIntervalFrames(UShort value) throws UaException {
    BaseDataVariableTypeNode node = getMaxIntervalFramesNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UShort readMaxIntervalFrames() throws UaException {
    try {
      return readMaxIntervalFramesAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeMaxIntervalFrames(UShort value) throws UaException {
    try {
      writeMaxIntervalFramesAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UShort> readMaxIntervalFramesAsync() {
    return getMaxIntervalFramesNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UShort) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxIntervalFramesAsync(UShort maxIntervalFrames) {
    DataValue value = DataValue.valueOnly(new Variant(maxIntervalFrames));
    return getMaxIntervalFramesNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public BaseDataVariableTypeNode getMaxIntervalFramesNode() throws UaException {
    try {
      return getMaxIntervalFramesNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getMaxIntervalFramesNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "MaxIntervalFrames",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (BaseDataVariableTypeNode) node);
  }

  @Override
  public UInteger getMaxFrameSize() throws UaException {
    BaseDataVariableTypeNode node = getMaxFrameSizeNode();
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxFrameSize(UInteger value) throws UaException {
    BaseDataVariableTypeNode node = getMaxFrameSizeNode();
    node.setValue(new Variant(value));
  }

  @Override
  public UInteger readMaxFrameSize() throws UaException {
    try {
      return readMaxFrameSizeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeMaxFrameSize(UInteger value) throws UaException {
    try {
      writeMaxFrameSizeAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UInteger> readMaxFrameSizeAsync() {
    return getMaxFrameSizeNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (UInteger) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxFrameSizeAsync(UInteger maxFrameSize) {
    DataValue value = DataValue.valueOnly(new Variant(maxFrameSize));
    return getMaxFrameSizeNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public BaseDataVariableTypeNode getMaxFrameSizeNode() throws UaException {
    try {
      return getMaxFrameSizeNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getMaxFrameSizeNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "MaxFrameSize",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (BaseDataVariableTypeNode) node);
  }

  @Override
  public UnsignedRationalNumber getInterval() throws UaException {
    BaseDataVariableTypeNode node = getIntervalNode();
    return cast(node.getValue().getValue().getValue(), UnsignedRationalNumber.class);
  }

  @Override
  public void setInterval(UnsignedRationalNumber value) throws UaException {
    BaseDataVariableTypeNode node = getIntervalNode();
    ExtensionObject encoded = ExtensionObject.encode(client.getStaticEncodingContext(), value);
    node.setValue(new Variant(encoded));
  }

  @Override
  public UnsignedRationalNumber readInterval() throws UaException {
    try {
      return readIntervalAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeInterval(UnsignedRationalNumber value) throws UaException {
    try {
      writeIntervalAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends UnsignedRationalNumber> readIntervalAsync() {
    return getIntervalNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> cast(v.getValue().getValue(), UnsignedRationalNumber.class));
  }

  @Override
  public CompletableFuture<StatusCode> writeIntervalAsync(UnsignedRationalNumber interval) {
    ExtensionObject encoded = ExtensionObject.encode(client.getStaticEncodingContext(), interval);
    DataValue value = DataValue.valueOnly(new Variant(encoded));
    return getIntervalNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public BaseDataVariableTypeNode getIntervalNode() throws UaException {
    try {
      return getIntervalNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getIntervalNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "Interval", ExpandedNodeId.parse("ns=0;i=47"), false);
    return future.thenApply(node -> (BaseDataVariableTypeNode) node);
  }
}

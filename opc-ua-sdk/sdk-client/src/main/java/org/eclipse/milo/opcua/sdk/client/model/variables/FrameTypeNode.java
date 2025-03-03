/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.model.variables;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
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
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessLevelExType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.CartesianCoordinates;
import org.eclipse.milo.opcua.stack.core.types.structured.Orientation;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class FrameTypeNode extends BaseDataVariableTypeNode implements FrameType {
  public FrameTypeNode(
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
      DataValue value,
      NodeId dataType,
      Integer valueRank,
      UInteger[] arrayDimensions,
      UByte accessLevel,
      UByte userAccessLevel,
      Double minimumSamplingInterval,
      Boolean historizing,
      AccessLevelExType accessLevelEx) {
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
        value,
        dataType,
        valueRank,
        arrayDimensions,
        accessLevel,
        userAccessLevel,
        minimumSamplingInterval,
        historizing,
        accessLevelEx);
  }

  @Override
  public Boolean getConstant() throws UaException {
    PropertyTypeNode node = getConstantNode();
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setConstant(Boolean value) throws UaException {
    PropertyTypeNode node = getConstantNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Boolean readConstant() throws UaException {
    try {
      return readConstantAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeConstant(Boolean value) throws UaException {
    try {
      writeConstantAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Boolean> readConstantAsync() {
    return getConstantNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Boolean) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeConstantAsync(Boolean constant) {
    DataValue value = DataValue.valueOnly(new Variant(constant));
    return getConstantNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getConstantNode() throws UaException {
    try {
      return getConstantNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getConstantNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "Constant", ExpandedNodeId.parse("ns=0;i=46"), false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public Boolean getFixedBase() throws UaException {
    PropertyTypeNode node = getFixedBaseNode();
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setFixedBase(Boolean value) throws UaException {
    PropertyTypeNode node = getFixedBaseNode();
    node.setValue(new Variant(value));
  }

  @Override
  public Boolean readFixedBase() throws UaException {
    try {
      return readFixedBaseAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeFixedBase(Boolean value) throws UaException {
    try {
      writeFixedBaseAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Boolean> readFixedBaseAsync() {
    return getFixedBaseNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (Boolean) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeFixedBaseAsync(Boolean fixedBase) {
    DataValue value = DataValue.valueOnly(new Variant(fixedBase));
    return getFixedBaseNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public PropertyTypeNode getFixedBaseNode() throws UaException {
    try {
      return getFixedBaseNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getFixedBaseNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "FixedBase", ExpandedNodeId.parse("ns=0;i=46"), false);
    return future.thenApply(node -> (PropertyTypeNode) node);
  }

  @Override
  public CartesianCoordinates getCartesianCoordinates() throws UaException {
    CartesianCoordinatesTypeNode node = getCartesianCoordinatesNode();
    return cast(node.getValue().getValue().getValue(), CartesianCoordinates.class);
  }

  @Override
  public void setCartesianCoordinates(CartesianCoordinates value) throws UaException {
    CartesianCoordinatesTypeNode node = getCartesianCoordinatesNode();
    ExtensionObject encoded = ExtensionObject.encode(client.getStaticEncodingContext(), value);
    node.setValue(new Variant(encoded));
  }

  @Override
  public CartesianCoordinates readCartesianCoordinates() throws UaException {
    try {
      return readCartesianCoordinatesAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeCartesianCoordinates(CartesianCoordinates value) throws UaException {
    try {
      writeCartesianCoordinatesAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends CartesianCoordinates> readCartesianCoordinatesAsync() {
    return getCartesianCoordinatesNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> cast(v.getValue().getValue(), CartesianCoordinates.class));
  }

  @Override
  public CompletableFuture<StatusCode> writeCartesianCoordinatesAsync(
      CartesianCoordinates cartesianCoordinates) {
    ExtensionObject encoded =
        ExtensionObject.encode(client.getStaticEncodingContext(), cartesianCoordinates);
    DataValue value = DataValue.valueOnly(new Variant(encoded));
    return getCartesianCoordinatesNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public CartesianCoordinatesTypeNode getCartesianCoordinatesNode() throws UaException {
    try {
      return getCartesianCoordinatesNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends CartesianCoordinatesTypeNode>
      getCartesianCoordinatesNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "CartesianCoordinates",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (CartesianCoordinatesTypeNode) node);
  }

  @Override
  public Orientation getOrientation() throws UaException {
    OrientationTypeNode node = getOrientationNode();
    return cast(node.getValue().getValue().getValue(), Orientation.class);
  }

  @Override
  public void setOrientation(Orientation value) throws UaException {
    OrientationTypeNode node = getOrientationNode();
    ExtensionObject encoded = ExtensionObject.encode(client.getStaticEncodingContext(), value);
    node.setValue(new Variant(encoded));
  }

  @Override
  public Orientation readOrientation() throws UaException {
    try {
      return readOrientationAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeOrientation(Orientation value) throws UaException {
    try {
      writeOrientationAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends Orientation> readOrientationAsync() {
    return getOrientationNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> cast(v.getValue().getValue(), Orientation.class));
  }

  @Override
  public CompletableFuture<StatusCode> writeOrientationAsync(Orientation orientation) {
    ExtensionObject encoded =
        ExtensionObject.encode(client.getStaticEncodingContext(), orientation);
    DataValue value = DataValue.valueOnly(new Variant(encoded));
    return getOrientationNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public OrientationTypeNode getOrientationNode() throws UaException {
    try {
      return getOrientationNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends OrientationTypeNode> getOrientationNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "Orientation",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (OrientationTypeNode) node);
  }

  @Override
  public NodeId getBaseFrame() throws UaException {
    BaseDataVariableTypeNode node = getBaseFrameNode();
    return (NodeId) node.getValue().getValue().getValue();
  }

  @Override
  public void setBaseFrame(NodeId value) throws UaException {
    BaseDataVariableTypeNode node = getBaseFrameNode();
    node.setValue(new Variant(value));
  }

  @Override
  public NodeId readBaseFrame() throws UaException {
    try {
      return readBaseFrameAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeBaseFrame(NodeId value) throws UaException {
    try {
      writeBaseFrameAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends NodeId> readBaseFrameAsync() {
    return getBaseFrameNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> (NodeId) v.getValue().getValue());
  }

  @Override
  public CompletableFuture<StatusCode> writeBaseFrameAsync(NodeId baseFrame) {
    DataValue value = DataValue.valueOnly(new Variant(baseFrame));
    return getBaseFrameNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public BaseDataVariableTypeNode getBaseFrameNode() throws UaException {
    try {
      return getBaseFrameNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getBaseFrameNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/", "BaseFrame", ExpandedNodeId.parse("ns=0;i=47"), false);
    return future.thenApply(node -> (BaseDataVariableTypeNode) node);
  }
}

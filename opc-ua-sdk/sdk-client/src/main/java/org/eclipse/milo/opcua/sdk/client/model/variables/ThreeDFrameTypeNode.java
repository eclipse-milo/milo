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
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.ThreeDCartesianCoordinates;
import org.eclipse.milo.opcua.stack.core.types.structured.ThreeDOrientation;

public class ThreeDFrameTypeNode extends FrameTypeNode implements ThreeDFrameType {
  public ThreeDFrameTypeNode(
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
  public ThreeDCartesianCoordinates getCartesianCoordinates() throws UaException {
    ThreeDCartesianCoordinatesTypeNode node = getCartesianCoordinatesNode();
    return cast(node.getValue().getValue().getValue(), ThreeDCartesianCoordinates.class);
  }

  @Override
  public void setCartesianCoordinates(ThreeDCartesianCoordinates value) throws UaException {
    ThreeDCartesianCoordinatesTypeNode node = getCartesianCoordinatesNode();
    ExtensionObject encoded = ExtensionObject.encode(client.getStaticEncodingContext(), value);
    node.setValue(new Variant(encoded));
  }

  @Override
  public ThreeDCartesianCoordinates readCartesianCoordinates() throws UaException {
    try {
      return readCartesianCoordinatesAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeCartesianCoordinates(ThreeDCartesianCoordinates value) throws UaException {
    try {
      writeCartesianCoordinatesAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends ThreeDCartesianCoordinates> readCartesianCoordinatesAsync() {
    return getCartesianCoordinatesNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> cast(v.getValue().getValue(), ThreeDCartesianCoordinates.class));
  }

  @Override
  public CompletableFuture<StatusCode> writeCartesianCoordinatesAsync(
      ThreeDCartesianCoordinates cartesianCoordinates) {
    ExtensionObject encoded =
        ExtensionObject.encode(client.getStaticEncodingContext(), cartesianCoordinates);
    DataValue value = DataValue.valueOnly(new Variant(encoded));
    return getCartesianCoordinatesNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public ThreeDCartesianCoordinatesTypeNode getCartesianCoordinatesNode() throws UaException {
    try {
      return getCartesianCoordinatesNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends ThreeDCartesianCoordinatesTypeNode>
      getCartesianCoordinatesNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "CartesianCoordinates",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (ThreeDCartesianCoordinatesTypeNode) node);
  }

  @Override
  public ThreeDOrientation getOrientation() throws UaException {
    ThreeDOrientationTypeNode node = getOrientationNode();
    return cast(node.getValue().getValue().getValue(), ThreeDOrientation.class);
  }

  @Override
  public void setOrientation(ThreeDOrientation value) throws UaException {
    ThreeDOrientationTypeNode node = getOrientationNode();
    ExtensionObject encoded = ExtensionObject.encode(client.getStaticEncodingContext(), value);
    node.setValue(new Variant(encoded));
  }

  @Override
  public ThreeDOrientation readOrientation() throws UaException {
    try {
      return readOrientationAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public void writeOrientation(ThreeDOrientation value) throws UaException {
    try {
      writeOrientationAsync(value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  @Override
  public CompletableFuture<? extends ThreeDOrientation> readOrientationAsync() {
    return getOrientationNodeAsync()
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(v -> cast(v.getValue().getValue(), ThreeDOrientation.class));
  }

  @Override
  public CompletableFuture<StatusCode> writeOrientationAsync(ThreeDOrientation orientation) {
    ExtensionObject encoded =
        ExtensionObject.encode(client.getStaticEncodingContext(), orientation);
    DataValue value = DataValue.valueOnly(new Variant(encoded));
    return getOrientationNodeAsync()
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  @Override
  public ThreeDOrientationTypeNode getOrientationNode() throws UaException {
    try {
      return getOrientationNodeAsync().get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError));
    }
  }

  @Override
  public CompletableFuture<? extends ThreeDOrientationTypeNode> getOrientationNodeAsync() {
    CompletableFuture<UaNode> future =
        getMemberNodeAsync(
            "http://opcfoundation.org/UA/",
            "Orientation",
            ExpandedNodeId.parse("ns=0;i=47"),
            false);
    return future.thenApply(node -> (ThreeDOrientationTypeNode) node);
  }
}

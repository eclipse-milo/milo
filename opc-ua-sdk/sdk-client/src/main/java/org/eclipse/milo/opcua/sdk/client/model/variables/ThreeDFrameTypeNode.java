/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.model.variables;

import com.digitalpetri.opcua.uanodeset.runtime.client.ClientMembers;
import com.digitalpetri.opcua.uanodeset.runtime.members.MemberDeclaration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.core.ValueRanks;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
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
import org.jspecify.annotations.Nullable;

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
  public @Nullable ThreeDCartesianCoordinates getCartesianCoordinates() throws UaException {
    ThreeDCartesianCoordinatesTypeNode node = getCartesianCoordinatesNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:CartesianCoordinates (declaration i=18796, owner i=18791)"
              + " on "
              + getNodeId());
    }
    return (ThreeDCartesianCoordinates)
        decodeValue(
            node.getValue().getValue().getValue(),
            ThreeDCartesianCoordinates.class,
            ValueRanks.Scalar);
  }

  @Override
  public void setCartesianCoordinates(@Nullable ThreeDCartesianCoordinates value)
      throws UaException {
    ThreeDCartesianCoordinatesTypeNode node = getCartesianCoordinatesNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:CartesianCoordinates (declaration i=18796, owner i=18791)"
              + " on "
              + getNodeId());
    }
    node.setValue(
        new Variant(encodeValue(value, ThreeDCartesianCoordinates.class, ValueRanks.Scalar)));
  }

  @Override
  public @Nullable ThreeDCartesianCoordinates readCartesianCoordinates() throws UaException {
    return ClientMembers.await(readCartesianCoordinatesAsync(), false);
  }

  @Override
  public void writeCartesianCoordinates(@Nullable ThreeDCartesianCoordinates value)
      throws UaException {
    try {
      StatusCode statusCode = writeCartesianCoordinatesAsync(value).get();
      if (statusCode != null && !statusCode.isGood()) {
        throw new UaException(statusCode);
      }
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends @Nullable ThreeDCartesianCoordinates>
      readCartesianCoordinatesAsync() {
    return getCartesianCoordinatesNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:CartesianCoordinates (declaration i=18796,"
                            + " owner i=18791) on "
                            + getNodeId()));
              }
              return node.readAttributeAsync(AttributeId.Value);
            })
        .thenApply(
            v -> {
              if (!v.getStatusCode().isGood()) {
                throw new CompletionException(new UaException(v.getStatusCode()));
              }
              try {
                return (ThreeDCartesianCoordinates)
                    decodeValue(
                        v.getValue().getValue(),
                        ThreeDCartesianCoordinates.class,
                        ValueRanks.Scalar);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeCartesianCoordinatesAsync(
      @Nullable ThreeDCartesianCoordinates cartesianCoordinates) {
    return getCartesianCoordinatesNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:CartesianCoordinates (declaration i=18796,"
                            + " owner i=18791) on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(
                            encodeValue(
                                cartesianCoordinates,
                                ThreeDCartesianCoordinates.class,
                                ValueRanks.Scalar)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public ThreeDCartesianCoordinatesTypeNode getCartesianCoordinatesNode() throws UaException {
    return ClientMembers.await(getCartesianCoordinatesNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends ThreeDCartesianCoordinatesTypeNode>
      getCartesianCoordinatesNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        ThreeDCartesianCoordinatesTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "CartesianCoordinates",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:CartesianCoordinates (declaration i=18796, owner"
                + " i=18791)"));
  }

  @Override
  public @Nullable ThreeDOrientation getOrientation() throws UaException {
    ThreeDOrientationTypeNode node = getOrientationNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:Orientation (declaration i=18792, owner i=18791)"
              + " on "
              + getNodeId());
    }
    return (ThreeDOrientation)
        decodeValue(
            node.getValue().getValue().getValue(), ThreeDOrientation.class, ValueRanks.Scalar);
  }

  @Override
  public void setOrientation(@Nullable ThreeDOrientation value) throws UaException {
    ThreeDOrientationTypeNode node = getOrientationNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:Orientation (declaration i=18792, owner i=18791)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(encodeValue(value, ThreeDOrientation.class, ValueRanks.Scalar)));
  }

  @Override
  public @Nullable ThreeDOrientation readOrientation() throws UaException {
    return ClientMembers.await(readOrientationAsync(), false);
  }

  @Override
  public void writeOrientation(@Nullable ThreeDOrientation value) throws UaException {
    try {
      StatusCode statusCode = writeOrientationAsync(value).get();
      if (statusCode != null && !statusCode.isGood()) {
        throw new UaException(statusCode);
      }
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends @Nullable ThreeDOrientation> readOrientationAsync() {
    return getOrientationNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:Orientation (declaration i=18792, owner"
                            + " i=18791) on "
                            + getNodeId()));
              }
              return node.readAttributeAsync(AttributeId.Value);
            })
        .thenApply(
            v -> {
              if (!v.getStatusCode().isGood()) {
                throw new CompletionException(new UaException(v.getStatusCode()));
              }
              try {
                return (ThreeDOrientation)
                    decodeValue(
                        v.getValue().getValue(), ThreeDOrientation.class, ValueRanks.Scalar);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeOrientationAsync(
      @Nullable ThreeDOrientation orientation) {
    return getOrientationNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:Orientation (declaration i=18792, owner"
                            + " i=18791) on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(
                            encodeValue(orientation, ThreeDOrientation.class, ValueRanks.Scalar)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public ThreeDOrientationTypeNode getOrientationNode() throws UaException {
    return ClientMembers.await(getOrientationNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends ThreeDOrientationTypeNode> getOrientationNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        ThreeDOrientationTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "Orientation",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:Orientation (declaration i=18792, owner i=18791)"));
  }
}

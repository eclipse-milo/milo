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
import org.eclipse.milo.opcua.stack.core.types.structured.CartesianCoordinates;
import org.eclipse.milo.opcua.stack.core.types.structured.Orientation;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.jspecify.annotations.Nullable;

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
  public @Nullable Boolean getConstant() throws UaException {
    PropertyTypeNode node = getConstantNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:Constant (declaration i=18788, owner i=18786)"
              + " on "
              + getNodeId());
    }
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setConstant(@Nullable Boolean value) throws UaException {
    PropertyTypeNode node = getConstantNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:Constant (declaration i=18788, owner i=18786)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable Boolean readConstant() throws UaException {
    return ClientMembers.await(readConstantAsync(), false);
  }

  @Override
  public void writeConstant(@Nullable Boolean value) throws UaException {
    try {
      StatusCode statusCode = writeConstantAsync(value).get();
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
  public CompletableFuture<? extends @Nullable Boolean> readConstantAsync() {
    return getConstantNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:Constant (declaration i=18788, owner i=18786)"
                            + " on "
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
                return (Boolean) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeConstantAsync(@Nullable Boolean constant) {
    return getConstantNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:Constant (declaration i=18788, owner i=18786)"
                            + " on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(constant));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getConstantNode() throws UaException {
    return ClientMembers.await(getConstantNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getConstantNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "Constant",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:Constant (declaration i=18788, owner i=18786)"));
  }

  @Override
  public @Nullable Boolean getFixedBase() throws UaException {
    PropertyTypeNode node = getFixedBaseNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:FixedBase (declaration i=18790, owner i=18786)"
              + " on "
              + getNodeId());
    }
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setFixedBase(@Nullable Boolean value) throws UaException {
    PropertyTypeNode node = getFixedBaseNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:FixedBase (declaration i=18790, owner i=18786)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable Boolean readFixedBase() throws UaException {
    return ClientMembers.await(readFixedBaseAsync(), false);
  }

  @Override
  public void writeFixedBase(@Nullable Boolean value) throws UaException {
    try {
      StatusCode statusCode = writeFixedBaseAsync(value).get();
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
  public CompletableFuture<? extends @Nullable Boolean> readFixedBaseAsync() {
    return getFixedBaseNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:FixedBase (declaration i=18790, owner"
                            + " i=18786) on "
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
                return (Boolean) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeFixedBaseAsync(@Nullable Boolean fixedBase) {
    return getFixedBaseNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:FixedBase (declaration i=18790, owner"
                            + " i=18786) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(fixedBase));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getFixedBaseNode() throws UaException {
    return ClientMembers.await(getFixedBaseNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getFixedBaseNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "FixedBase",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:FixedBase (declaration i=18790, owner i=18786)"));
  }

  @Override
  public @Nullable CartesianCoordinates getCartesianCoordinates() throws UaException {
    CartesianCoordinatesTypeNode node = getCartesianCoordinatesNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:CartesianCoordinates (declaration i=18801, owner i=18786)"
              + " on "
              + getNodeId());
    }
    return (CartesianCoordinates)
        decodeValue(
            node.getValue().getValue().getValue(), CartesianCoordinates.class, ValueRanks.Scalar);
  }

  @Override
  public void setCartesianCoordinates(@Nullable CartesianCoordinates value) throws UaException {
    CartesianCoordinatesTypeNode node = getCartesianCoordinatesNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:CartesianCoordinates (declaration i=18801, owner i=18786)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(encodeValue(value, CartesianCoordinates.class, ValueRanks.Scalar)));
  }

  @Override
  public @Nullable CartesianCoordinates readCartesianCoordinates() throws UaException {
    return ClientMembers.await(readCartesianCoordinatesAsync(), false);
  }

  @Override
  public void writeCartesianCoordinates(@Nullable CartesianCoordinates value) throws UaException {
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
  public CompletableFuture<? extends @Nullable CartesianCoordinates>
      readCartesianCoordinatesAsync() {
    return getCartesianCoordinatesNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:CartesianCoordinates (declaration i=18801,"
                            + " owner i=18786) on "
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
                return (CartesianCoordinates)
                    decodeValue(
                        v.getValue().getValue(), CartesianCoordinates.class, ValueRanks.Scalar);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeCartesianCoordinatesAsync(
      @Nullable CartesianCoordinates cartesianCoordinates) {
    return getCartesianCoordinatesNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:CartesianCoordinates (declaration i=18801,"
                            + " owner i=18786) on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(
                            encodeValue(
                                cartesianCoordinates,
                                CartesianCoordinates.class,
                                ValueRanks.Scalar)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public CartesianCoordinatesTypeNode getCartesianCoordinatesNode() throws UaException {
    return ClientMembers.await(getCartesianCoordinatesNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends CartesianCoordinatesTypeNode>
      getCartesianCoordinatesNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        CartesianCoordinatesTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "CartesianCoordinates",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:CartesianCoordinates (declaration i=18801, owner"
                + " i=18786)"));
  }

  @Override
  public @Nullable Orientation getOrientation() throws UaException {
    OrientationTypeNode node = getOrientationNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:Orientation (declaration i=18787, owner i=18786)"
              + " on "
              + getNodeId());
    }
    return (Orientation)
        decodeValue(node.getValue().getValue().getValue(), Orientation.class, ValueRanks.Scalar);
  }

  @Override
  public void setOrientation(@Nullable Orientation value) throws UaException {
    OrientationTypeNode node = getOrientationNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:Orientation (declaration i=18787, owner i=18786)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(encodeValue(value, Orientation.class, ValueRanks.Scalar)));
  }

  @Override
  public @Nullable Orientation readOrientation() throws UaException {
    return ClientMembers.await(readOrientationAsync(), false);
  }

  @Override
  public void writeOrientation(@Nullable Orientation value) throws UaException {
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
  public CompletableFuture<? extends @Nullable Orientation> readOrientationAsync() {
    return getOrientationNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:Orientation (declaration i=18787, owner"
                            + " i=18786) on "
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
                return (Orientation)
                    decodeValue(v.getValue().getValue(), Orientation.class, ValueRanks.Scalar);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeOrientationAsync(@Nullable Orientation orientation) {
    return getOrientationNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:Orientation (declaration i=18787, owner"
                            + " i=18786) on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(
                            encodeValue(orientation, Orientation.class, ValueRanks.Scalar)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public OrientationTypeNode getOrientationNode() throws UaException {
    return ClientMembers.await(getOrientationNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends OrientationTypeNode> getOrientationNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        OrientationTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "Orientation",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:Orientation (declaration i=18787, owner i=18786)"));
  }

  @Override
  public @Nullable NodeId getBaseFrame() throws UaException {
    BaseDataVariableTypeNode node = getBaseFrameNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:BaseFrame (declaration i=18789, owner i=18786)"
              + " on "
              + getNodeId());
    }
    return (NodeId) node.getValue().getValue().getValue();
  }

  @Override
  public void setBaseFrame(@Nullable NodeId value) throws UaException {
    BaseDataVariableTypeNode node = getBaseFrameNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:BaseFrame (declaration i=18789, owner i=18786)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable NodeId readBaseFrame() throws UaException {
    return ClientMembers.await(readBaseFrameAsync(), false);
  }

  @Override
  public void writeBaseFrame(@Nullable NodeId value) throws UaException {
    try {
      StatusCode statusCode = writeBaseFrameAsync(value).get();
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
  public CompletableFuture<? extends @Nullable NodeId> readBaseFrameAsync() {
    return getBaseFrameNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:BaseFrame (declaration i=18789, owner"
                            + " i=18786) on "
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
                return (NodeId) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeBaseFrameAsync(@Nullable NodeId baseFrame) {
    return getBaseFrameNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:BaseFrame (declaration i=18789, owner"
                            + " i=18786) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(baseFrame));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable BaseDataVariableTypeNode getBaseFrameNode() throws UaException {
    return ClientMembers.await(getBaseFrameNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable BaseDataVariableTypeNode> getBaseFrameNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "BaseFrame",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:BaseFrame (declaration i=18789, owner i=18786)"));
  }
}

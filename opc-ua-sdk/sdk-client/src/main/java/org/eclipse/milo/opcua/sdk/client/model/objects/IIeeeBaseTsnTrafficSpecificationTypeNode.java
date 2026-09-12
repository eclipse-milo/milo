/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.model.objects;

import com.digitalpetri.opcua.uanodeset.runtime.client.ClientMembers;
import com.digitalpetri.opcua.uanodeset.runtime.client.ClientViews;
import com.digitalpetri.opcua.uanodeset.runtime.members.MemberDeclaration;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.model.variables.BaseDataVariableTypeNode;
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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.UnsignedRationalNumber;
import org.jspecify.annotations.Nullable;

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

  /**
   * Creates an independently owned view context retaining this exact existing node and its client.
   * Cached attributes remain shared even after SDK cache eviction. Close the returned context when
   * its views are no longer needed.
   */
  public static ClientViews createViews(IIeeeBaseTsnTrafficSpecificationTypeNode node) {
    Objects.requireNonNull(node, "node");
    return ClientViews.forNode(node.client, node);
  }

  @Override
  public @Nullable UShort getMaxIntervalFrames() throws UaException {
    BaseDataVariableTypeNode node = getMaxIntervalFramesNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxIntervalFrames (declaration i=24180, owner i=24179)"
              + " on "
              + getNodeId());
    }
    return (UShort) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxIntervalFrames(@Nullable UShort value) throws UaException {
    BaseDataVariableTypeNode node = getMaxIntervalFramesNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxIntervalFrames (declaration i=24180, owner i=24179)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UShort readMaxIntervalFrames() throws UaException {
    return ClientMembers.await(readMaxIntervalFramesAsync(), false);
  }

  @Override
  public void writeMaxIntervalFrames(@Nullable UShort value) throws UaException {
    try {
      StatusCode statusCode = writeMaxIntervalFramesAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UShort> readMaxIntervalFramesAsync() {
    return getMaxIntervalFramesNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxIntervalFrames (declaration i=24180, owner"
                            + " i=24179) on "
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
                return (UShort) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxIntervalFramesAsync(
      @Nullable UShort maxIntervalFrames) {
    return getMaxIntervalFramesNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxIntervalFrames (declaration i=24180, owner"
                            + " i=24179) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(maxIntervalFrames));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public BaseDataVariableTypeNode getMaxIntervalFramesNode() throws UaException {
    return ClientMembers.await(getMaxIntervalFramesNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getMaxIntervalFramesNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxIntervalFrames",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:MaxIntervalFrames (declaration i=24180, owner i=24179)"));
  }

  @Override
  public @Nullable UInteger getMaxFrameSize() throws UaException {
    BaseDataVariableTypeNode node = getMaxFrameSizeNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxFrameSize (declaration i=24181, owner i=24179)"
              + " on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxFrameSize(@Nullable UInteger value) throws UaException {
    BaseDataVariableTypeNode node = getMaxFrameSizeNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxFrameSize (declaration i=24181, owner i=24179)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UInteger readMaxFrameSize() throws UaException {
    return ClientMembers.await(readMaxFrameSizeAsync(), false);
  }

  @Override
  public void writeMaxFrameSize(@Nullable UInteger value) throws UaException {
    try {
      StatusCode statusCode = writeMaxFrameSizeAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UInteger> readMaxFrameSizeAsync() {
    return getMaxFrameSizeNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxFrameSize (declaration i=24181, owner"
                            + " i=24179) on "
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
                return (UInteger) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxFrameSizeAsync(@Nullable UInteger maxFrameSize) {
    return getMaxFrameSizeNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxFrameSize (declaration i=24181, owner"
                            + " i=24179) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(maxFrameSize));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public BaseDataVariableTypeNode getMaxFrameSizeNode() throws UaException {
    return ClientMembers.await(getMaxFrameSizeNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getMaxFrameSizeNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxFrameSize",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:MaxFrameSize (declaration i=24181, owner i=24179)"));
  }

  @Override
  public @Nullable UnsignedRationalNumber getInterval() throws UaException {
    BaseDataVariableTypeNode node = getIntervalNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:Interval (declaration i=24182, owner i=24179)"
              + " on "
              + getNodeId());
    }
    return (UnsignedRationalNumber)
        decodeValue(
            node.getValue().getValue().getValue(), UnsignedRationalNumber.class, ValueRanks.Scalar);
  }

  @Override
  public void setInterval(@Nullable UnsignedRationalNumber value) throws UaException {
    BaseDataVariableTypeNode node = getIntervalNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:Interval (declaration i=24182, owner i=24179)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(encodeValue(value, UnsignedRationalNumber.class, ValueRanks.Scalar)));
  }

  @Override
  public @Nullable UnsignedRationalNumber readInterval() throws UaException {
    return ClientMembers.await(readIntervalAsync(), false);
  }

  @Override
  public void writeInterval(@Nullable UnsignedRationalNumber value) throws UaException {
    try {
      StatusCode statusCode = writeIntervalAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UnsignedRationalNumber> readIntervalAsync() {
    return getIntervalNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:Interval (declaration i=24182, owner i=24179)"
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
                return (UnsignedRationalNumber)
                    decodeValue(
                        v.getValue().getValue(), UnsignedRationalNumber.class, ValueRanks.Scalar);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeIntervalAsync(
      @Nullable UnsignedRationalNumber interval) {
    return getIntervalNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:Interval (declaration i=24182, owner i=24179)"
                            + " on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(
                            encodeValue(
                                interval, UnsignedRationalNumber.class, ValueRanks.Scalar)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public BaseDataVariableTypeNode getIntervalNode() throws UaException {
    return ClientMembers.await(getIntervalNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getIntervalNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "Interval",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:Interval (declaration i=24182, owner i=24179)"));
  }
}

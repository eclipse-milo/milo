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
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.core.ValueRanks;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.enumerated.RedundancySupport;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RedundantServerDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.ArrayUtil;
import org.jspecify.annotations.Nullable;

public class ServerRedundancyTypeNode extends BaseObjectTypeNode implements ServerRedundancyType {
  public ServerRedundancyTypeNode(
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
  public static ClientViews createViews(ServerRedundancyTypeNode node) {
    Objects.requireNonNull(node, "node");
    return ClientViews.forNode(node.client, node);
  }

  @Override
  public @Nullable RedundancySupport getRedundancySupport() throws UaException {
    PropertyTypeNode node = getRedundancySupportNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:RedundancySupport (declaration i=2035, owner i=2034)"
              + " on "
              + getNodeId());
    }
    Object value = node.getValue().getValue().getValue();
    Object convertedValue;
    {
      if (value == null || value instanceof Matrix && ((Matrix) value).isNull()) {
        convertedValue = null;
      } else {
        Object elements = value instanceof Matrix ? ((Matrix) value).getElements() : value;
        int rank =
            value instanceof Matrix
                ? ((Matrix) value).getValueRank()
                : ArrayUtil.getValueRank(value);
        boolean permitted = rank == -1;
        if (!permitted) {
          throw new UaException(
              StatusCodes.Bad_TypeMismatch,
              "RedundancySupport: ValueRank=-1 does not permit rank " + rank);
        }
        if (value != null && !((Object) value instanceof RedundancySupport)) {
          if (!(value instanceof Integer)) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "RedundancySupport: expected"
                    + " org.eclipse.milo.opcua.stack.core.types.enumerated.RedundancySupport or"
                    + " Int32, got "
                    + value);
          }
          if (RedundancySupport.from((Integer) value) == null) {
            throw new UaException(
                StatusCodes.Bad_OutOfRange,
                "RedundancySupport: unknown"
                    + " org.eclipse.milo.opcua.stack.core.types.enumerated.RedundancySupport value "
                    + value);
          }
        }
        convertedValue =
            value == null || value instanceof RedundancySupport
                ? (RedundancySupport) value
                : RedundancySupport.from((Integer) value);
      }
    }
    return (RedundancySupport) convertedValue;
  }

  @Override
  public void setRedundancySupport(@Nullable RedundancySupport value) throws UaException {
    PropertyTypeNode node = getRedundancySupportNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:RedundancySupport (declaration i=2035, owner i=2034)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable RedundancySupport readRedundancySupport() throws UaException {
    return ClientMembers.await(readRedundancySupportAsync(), false);
  }

  @Override
  public void writeRedundancySupport(@Nullable RedundancySupport value) throws UaException {
    try {
      StatusCode statusCode = writeRedundancySupportAsync(value).get();
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
  public CompletableFuture<? extends @Nullable RedundancySupport> readRedundancySupportAsync() {
    return getRedundancySupportNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:RedundancySupport (declaration i=2035, owner"
                            + " i=2034) on "
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
                Object value = v.getValue().getValue();
                Object convertedValue;
                {
                  if (value == null || value instanceof Matrix && ((Matrix) value).isNull()) {
                    convertedValue = null;
                  } else {
                    Object elements =
                        value instanceof Matrix ? ((Matrix) value).getElements() : value;
                    int rank =
                        value instanceof Matrix
                            ? ((Matrix) value).getValueRank()
                            : ArrayUtil.getValueRank(value);
                    boolean permitted = rank == -1;
                    if (!permitted) {
                      throw new UaRuntimeException(
                          StatusCodes.Bad_TypeMismatch,
                          "RedundancySupport: ValueRank=-1 does not permit rank " + rank);
                    }
                    if (value != null && !((Object) value instanceof RedundancySupport)) {
                      if (!(value instanceof Integer)) {
                        throw new UaRuntimeException(
                            StatusCodes.Bad_TypeMismatch,
                            "RedundancySupport: expected"
                                + " org.eclipse.milo.opcua.stack.core.types.enumerated.RedundancySupport"
                                + " or Int32, got "
                                + value);
                      }
                      if (RedundancySupport.from((Integer) value) == null) {
                        throw new UaRuntimeException(
                            StatusCodes.Bad_OutOfRange,
                            "RedundancySupport: unknown"
                                + " org.eclipse.milo.opcua.stack.core.types.enumerated.RedundancySupport"
                                + " value "
                                + value);
                      }
                    }
                    convertedValue =
                        value == null || value instanceof RedundancySupport
                            ? (RedundancySupport) value
                            : RedundancySupport.from((Integer) value);
                  }
                }
                return (RedundancySupport) convertedValue;
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeRedundancySupportAsync(
      @Nullable RedundancySupport redundancySupport) {
    return getRedundancySupportNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:RedundancySupport (declaration i=2035, owner"
                            + " i=2034) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(redundancySupport));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getRedundancySupportNode() throws UaException {
    return ClientMembers.await(getRedundancySupportNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getRedundancySupportNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "RedundancySupport",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:RedundancySupport (declaration i=2035, owner i=2034)"));
  }

  @Override
  public @Nullable RedundantServerDataType @Nullable [] getRedundantServerArray()
      throws UaException {
    PropertyTypeNode node = getRedundantServerArrayNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:RedundantServerArray (declaration i=32410, owner i=2034)"
              + " on "
              + getNodeId());
    }
    return (RedundantServerDataType[])
        decodeValue(
            node.getValue().getValue().getValue(),
            RedundantServerDataType.class,
            ValueRanks.OneDimension);
  }

  @Override
  public void setRedundantServerArray(@Nullable RedundantServerDataType @Nullable [] value)
      throws UaException {
    PropertyTypeNode node = getRedundantServerArrayNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:RedundantServerArray (declaration i=32410, owner i=2034)"
              + " on "
              + getNodeId());
    }
    node.setValue(
        new Variant(encodeValue(value, RedundantServerDataType.class, ValueRanks.OneDimension)));
  }

  @Override
  public @Nullable RedundantServerDataType @Nullable [] readRedundantServerArray()
      throws UaException {
    return ClientMembers.await(readRedundantServerArrayAsync(), false);
  }

  @Override
  public void writeRedundantServerArray(@Nullable RedundantServerDataType @Nullable [] value)
      throws UaException {
    try {
      StatusCode statusCode = writeRedundantServerArrayAsync(value).get();
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
  public CompletableFuture<? extends @Nullable RedundantServerDataType @Nullable []>
      readRedundantServerArrayAsync() {
    return getRedundantServerArrayNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:RedundantServerArray (declaration i=32410,"
                            + " owner i=2034) on "
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
                return (RedundantServerDataType[])
                    decodeValue(
                        v.getValue().getValue(),
                        RedundantServerDataType.class,
                        ValueRanks.OneDimension);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeRedundantServerArrayAsync(
      @Nullable RedundantServerDataType @Nullable [] redundantServerArray) {
    return getRedundantServerArrayNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:RedundantServerArray (declaration i=32410,"
                            + " owner i=2034) on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(
                            encodeValue(
                                redundantServerArray,
                                RedundantServerDataType.class,
                                ValueRanks.OneDimension)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getRedundantServerArrayNode() throws UaException {
    return ClientMembers.await(getRedundantServerArrayNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getRedundantServerArrayNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "RedundantServerArray",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:RedundantServerArray (declaration i=32410, owner"
                + " i=2034)"));
  }
}

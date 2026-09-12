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
import org.eclipse.milo.opcua.stack.core.types.enumerated.DataSetOrderingType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpNetworkMessageContentMask;
import org.eclipse.milo.opcua.stack.core.util.ArrayUtil;
import org.jspecify.annotations.Nullable;

public class UadpWriterGroupMessageTypeNode extends WriterGroupMessageTypeNode
    implements UadpWriterGroupMessageType {
  public UadpWriterGroupMessageTypeNode(
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
  public static ClientViews createViews(UadpWriterGroupMessageTypeNode node) {
    Objects.requireNonNull(node, "node");
    return ClientViews.forNode(node.client, node);
  }

  @Override
  public @Nullable UInteger getGroupVersion() throws UaException {
    PropertyTypeNode node = getGroupVersionNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:GroupVersion (declaration i=21106, owner i=21105)"
              + " on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setGroupVersion(@Nullable UInteger value) throws UaException {
    PropertyTypeNode node = getGroupVersionNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:GroupVersion (declaration i=21106, owner i=21105)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UInteger readGroupVersion() throws UaException {
    return ClientMembers.await(readGroupVersionAsync(), false);
  }

  @Override
  public void writeGroupVersion(@Nullable UInteger value) throws UaException {
    try {
      StatusCode statusCode = writeGroupVersionAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UInteger> readGroupVersionAsync() {
    return getGroupVersionNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:GroupVersion (declaration i=21106, owner"
                            + " i=21105) on "
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
  public CompletableFuture<StatusCode> writeGroupVersionAsync(@Nullable UInteger groupVersion) {
    return getGroupVersionNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:GroupVersion (declaration i=21106, owner"
                            + " i=21105) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(groupVersion));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getGroupVersionNode() throws UaException {
    return ClientMembers.await(getGroupVersionNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getGroupVersionNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "GroupVersion",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:GroupVersion (declaration i=21106, owner i=21105)"));
  }

  @Override
  public @Nullable DataSetOrderingType getDataSetOrdering() throws UaException {
    PropertyTypeNode node = getDataSetOrderingNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DataSetOrdering (declaration i=21107, owner i=21105)"
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
              "DataSetOrdering: ValueRank=-1 does not permit rank " + rank);
        }
        if (value != null && !((Object) value instanceof DataSetOrderingType)) {
          if (!(value instanceof Integer)) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "DataSetOrdering: expected"
                    + " org.eclipse.milo.opcua.stack.core.types.enumerated.DataSetOrderingType or"
                    + " Int32, got "
                    + value);
          }
          if (DataSetOrderingType.from((Integer) value) == null) {
            throw new UaException(
                StatusCodes.Bad_OutOfRange,
                "DataSetOrdering: unknown"
                    + " org.eclipse.milo.opcua.stack.core.types.enumerated.DataSetOrderingType"
                    + " value "
                    + value);
          }
        }
        convertedValue =
            value == null || value instanceof DataSetOrderingType
                ? (DataSetOrderingType) value
                : DataSetOrderingType.from((Integer) value);
      }
    }
    return (DataSetOrderingType) convertedValue;
  }

  @Override
  public void setDataSetOrdering(@Nullable DataSetOrderingType value) throws UaException {
    PropertyTypeNode node = getDataSetOrderingNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DataSetOrdering (declaration i=21107, owner i=21105)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable DataSetOrderingType readDataSetOrdering() throws UaException {
    return ClientMembers.await(readDataSetOrderingAsync(), false);
  }

  @Override
  public void writeDataSetOrdering(@Nullable DataSetOrderingType value) throws UaException {
    try {
      StatusCode statusCode = writeDataSetOrderingAsync(value).get();
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
  public CompletableFuture<? extends @Nullable DataSetOrderingType> readDataSetOrderingAsync() {
    return getDataSetOrderingNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:DataSetOrdering (declaration i=21107, owner"
                            + " i=21105) on "
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
                          "DataSetOrdering: ValueRank=-1 does not permit rank " + rank);
                    }
                    if (value != null && !((Object) value instanceof DataSetOrderingType)) {
                      if (!(value instanceof Integer)) {
                        throw new UaRuntimeException(
                            StatusCodes.Bad_TypeMismatch,
                            "DataSetOrdering: expected"
                                + " org.eclipse.milo.opcua.stack.core.types.enumerated.DataSetOrderingType"
                                + " or Int32, got "
                                + value);
                      }
                      if (DataSetOrderingType.from((Integer) value) == null) {
                        throw new UaRuntimeException(
                            StatusCodes.Bad_OutOfRange,
                            "DataSetOrdering: unknown"
                                + " org.eclipse.milo.opcua.stack.core.types.enumerated.DataSetOrderingType"
                                + " value "
                                + value);
                      }
                    }
                    convertedValue =
                        value == null || value instanceof DataSetOrderingType
                            ? (DataSetOrderingType) value
                            : DataSetOrderingType.from((Integer) value);
                  }
                }
                return (DataSetOrderingType) convertedValue;
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeDataSetOrderingAsync(
      @Nullable DataSetOrderingType dataSetOrdering) {
    return getDataSetOrderingNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:DataSetOrdering (declaration i=21107, owner"
                            + " i=21105) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(dataSetOrdering));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getDataSetOrderingNode() throws UaException {
    return ClientMembers.await(getDataSetOrderingNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getDataSetOrderingNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "DataSetOrdering",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:DataSetOrdering (declaration i=21107, owner i=21105)"));
  }

  @Override
  public @Nullable UadpNetworkMessageContentMask getNetworkMessageContentMask() throws UaException {
    PropertyTypeNode node = getNetworkMessageContentMaskNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:NetworkMessageContentMask (declaration i=21108, owner"
              + " i=21105) on "
              + getNodeId());
    }
    return (UadpNetworkMessageContentMask) node.getValue().getValue().getValue();
  }

  @Override
  public void setNetworkMessageContentMask(@Nullable UadpNetworkMessageContentMask value)
      throws UaException {
    PropertyTypeNode node = getNetworkMessageContentMaskNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:NetworkMessageContentMask (declaration i=21108, owner"
              + " i=21105) on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UadpNetworkMessageContentMask readNetworkMessageContentMask()
      throws UaException {
    return ClientMembers.await(readNetworkMessageContentMaskAsync(), false);
  }

  @Override
  public void writeNetworkMessageContentMask(@Nullable UadpNetworkMessageContentMask value)
      throws UaException {
    try {
      StatusCode statusCode = writeNetworkMessageContentMaskAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UadpNetworkMessageContentMask>
      readNetworkMessageContentMaskAsync() {
    return getNetworkMessageContentMaskNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:NetworkMessageContentMask (declaration"
                            + " i=21108, owner i=21105) on "
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
                return (UadpNetworkMessageContentMask) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeNetworkMessageContentMaskAsync(
      @Nullable UadpNetworkMessageContentMask networkMessageContentMask) {
    return getNetworkMessageContentMaskNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:NetworkMessageContentMask (declaration"
                            + " i=21108, owner i=21105) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(networkMessageContentMask));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getNetworkMessageContentMaskNode() throws UaException {
    return ClientMembers.await(getNetworkMessageContentMaskNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getNetworkMessageContentMaskNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "NetworkMessageContentMask",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:NetworkMessageContentMask (declaration i=21108, owner"
                + " i=21105)"));
  }

  @Override
  public @Nullable Double getSamplingOffset() throws UaException {
    PropertyTypeNode node = getSamplingOffsetNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SamplingOffset (declaration i=21109, owner i=21105)"
              + " on "
              + getNodeId());
    }
    return (Double) node.getValue().getValue().getValue();
  }

  @Override
  public void setSamplingOffset(@Nullable Double value) throws UaException {
    PropertyTypeNode node = getSamplingOffsetNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SamplingOffset (declaration i=21109, owner i=21105)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable Double readSamplingOffset() throws UaException {
    return ClientMembers.await(readSamplingOffsetAsync(), false);
  }

  @Override
  public void writeSamplingOffset(@Nullable Double value) throws UaException {
    try {
      StatusCode statusCode = writeSamplingOffsetAsync(value).get();
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
  public CompletableFuture<? extends @Nullable Double> readSamplingOffsetAsync() {
    return getSamplingOffsetNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:SamplingOffset (declaration i=21109, owner"
                            + " i=21105) on "
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
                return (Double) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeSamplingOffsetAsync(@Nullable Double samplingOffset) {
    return getSamplingOffsetNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:SamplingOffset (declaration i=21109, owner"
                            + " i=21105) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(samplingOffset));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getSamplingOffsetNode() throws UaException {
    return ClientMembers.await(getSamplingOffsetNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getSamplingOffsetNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "SamplingOffset",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:SamplingOffset (declaration i=21109, owner i=21105)"));
  }

  @Override
  public @Nullable Double @Nullable [] getPublishingOffset() throws UaException {
    PropertyTypeNode node = getPublishingOffsetNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:PublishingOffset (declaration i=21110, owner i=21105)"
              + " on "
              + getNodeId());
    }
    return (Double[]) node.getValue().getValue().getValue();
  }

  @Override
  public void setPublishingOffset(@Nullable Double @Nullable [] value) throws UaException {
    PropertyTypeNode node = getPublishingOffsetNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:PublishingOffset (declaration i=21110, owner i=21105)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable Double @Nullable [] readPublishingOffset() throws UaException {
    return ClientMembers.await(readPublishingOffsetAsync(), false);
  }

  @Override
  public void writePublishingOffset(@Nullable Double @Nullable [] value) throws UaException {
    try {
      StatusCode statusCode = writePublishingOffsetAsync(value).get();
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
  public CompletableFuture<? extends @Nullable Double @Nullable []> readPublishingOffsetAsync() {
    return getPublishingOffsetNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:PublishingOffset (declaration i=21110, owner"
                            + " i=21105) on "
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
                return (Double[]) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writePublishingOffsetAsync(
      @Nullable Double @Nullable [] publishingOffset) {
    return getPublishingOffsetNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:PublishingOffset (declaration i=21110, owner"
                            + " i=21105) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(publishingOffset));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getPublishingOffsetNode() throws UaException {
    return ClientMembers.await(getPublishingOffsetNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getPublishingOffsetNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "PublishingOffset",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:PublishingOffset (declaration i=21110, owner i=21105)"));
  }
}

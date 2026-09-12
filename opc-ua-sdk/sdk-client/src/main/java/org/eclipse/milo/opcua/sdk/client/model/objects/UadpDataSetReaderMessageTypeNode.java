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
import java.util.UUID;
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
import org.eclipse.milo.opcua.stack.core.types.structured.UadpDataSetMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpNetworkMessageContentMask;
import org.jspecify.annotations.Nullable;

public class UadpDataSetReaderMessageTypeNode extends DataSetReaderMessageTypeNode
    implements UadpDataSetReaderMessageType {
  public UadpDataSetReaderMessageTypeNode(
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
  public static ClientViews createViews(UadpDataSetReaderMessageTypeNode node) {
    Objects.requireNonNull(node, "node");
    return ClientViews.forNode(node.client, node);
  }

  @Override
  public @Nullable UInteger getGroupVersion() throws UaException {
    PropertyTypeNode node = getGroupVersionNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:GroupVersion (declaration i=21117, owner i=21116)"
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
          "http://opcfoundation.org/UA/:GroupVersion (declaration i=21117, owner i=21116)"
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
                        "http://opcfoundation.org/UA/:GroupVersion (declaration i=21117, owner"
                            + " i=21116) on "
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
                        "http://opcfoundation.org/UA/:GroupVersion (declaration i=21117, owner"
                            + " i=21116) on "
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
            "http://opcfoundation.org/UA/:GroupVersion (declaration i=21117, owner i=21116)"));
  }

  @Override
  public @Nullable UShort getNetworkMessageNumber() throws UaException {
    PropertyTypeNode node = getNetworkMessageNumberNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:NetworkMessageNumber (declaration i=21119, owner i=21116)"
              + " on "
              + getNodeId());
    }
    return (UShort) node.getValue().getValue().getValue();
  }

  @Override
  public void setNetworkMessageNumber(@Nullable UShort value) throws UaException {
    PropertyTypeNode node = getNetworkMessageNumberNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:NetworkMessageNumber (declaration i=21119, owner i=21116)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UShort readNetworkMessageNumber() throws UaException {
    return ClientMembers.await(readNetworkMessageNumberAsync(), false);
  }

  @Override
  public void writeNetworkMessageNumber(@Nullable UShort value) throws UaException {
    try {
      StatusCode statusCode = writeNetworkMessageNumberAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UShort> readNetworkMessageNumberAsync() {
    return getNetworkMessageNumberNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:NetworkMessageNumber (declaration i=21119,"
                            + " owner i=21116) on "
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
  public CompletableFuture<StatusCode> writeNetworkMessageNumberAsync(
      @Nullable UShort networkMessageNumber) {
    return getNetworkMessageNumberNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:NetworkMessageNumber (declaration i=21119,"
                            + " owner i=21116) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(networkMessageNumber));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getNetworkMessageNumberNode() throws UaException {
    return ClientMembers.await(getNetworkMessageNumberNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getNetworkMessageNumberNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "NetworkMessageNumber",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:NetworkMessageNumber (declaration i=21119, owner"
                + " i=21116)"));
  }

  @Override
  public @Nullable UShort getDataSetOffset() throws UaException {
    PropertyTypeNode node = getDataSetOffsetNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DataSetOffset (declaration i=17477, owner i=21116)"
              + " on "
              + getNodeId());
    }
    return (UShort) node.getValue().getValue().getValue();
  }

  @Override
  public void setDataSetOffset(@Nullable UShort value) throws UaException {
    PropertyTypeNode node = getDataSetOffsetNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DataSetOffset (declaration i=17477, owner i=21116)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UShort readDataSetOffset() throws UaException {
    return ClientMembers.await(readDataSetOffsetAsync(), false);
  }

  @Override
  public void writeDataSetOffset(@Nullable UShort value) throws UaException {
    try {
      StatusCode statusCode = writeDataSetOffsetAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UShort> readDataSetOffsetAsync() {
    return getDataSetOffsetNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:DataSetOffset (declaration i=17477, owner"
                            + " i=21116) on "
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
  public CompletableFuture<StatusCode> writeDataSetOffsetAsync(@Nullable UShort dataSetOffset) {
    return getDataSetOffsetNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:DataSetOffset (declaration i=17477, owner"
                            + " i=21116) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(dataSetOffset));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getDataSetOffsetNode() throws UaException {
    return ClientMembers.await(getDataSetOffsetNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getDataSetOffsetNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "DataSetOffset",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:DataSetOffset (declaration i=17477, owner i=21116)"));
  }

  @Override
  public @Nullable UUID getDataSetClassId() throws UaException {
    PropertyTypeNode node = getDataSetClassIdNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DataSetClassId (declaration i=21120, owner i=21116)"
              + " on "
              + getNodeId());
    }
    return (UUID) node.getValue().getValue().getValue();
  }

  @Override
  public void setDataSetClassId(@Nullable UUID value) throws UaException {
    PropertyTypeNode node = getDataSetClassIdNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DataSetClassId (declaration i=21120, owner i=21116)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UUID readDataSetClassId() throws UaException {
    return ClientMembers.await(readDataSetClassIdAsync(), false);
  }

  @Override
  public void writeDataSetClassId(@Nullable UUID value) throws UaException {
    try {
      StatusCode statusCode = writeDataSetClassIdAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UUID> readDataSetClassIdAsync() {
    return getDataSetClassIdNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:DataSetClassId (declaration i=21120, owner"
                            + " i=21116) on "
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
                return (UUID) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeDataSetClassIdAsync(@Nullable UUID dataSetClassId) {
    return getDataSetClassIdNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:DataSetClassId (declaration i=21120, owner"
                            + " i=21116) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(dataSetClassId));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getDataSetClassIdNode() throws UaException {
    return ClientMembers.await(getDataSetClassIdNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getDataSetClassIdNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "DataSetClassId",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:DataSetClassId (declaration i=21120, owner i=21116)"));
  }

  @Override
  public @Nullable UadpNetworkMessageContentMask getNetworkMessageContentMask() throws UaException {
    PropertyTypeNode node = getNetworkMessageContentMaskNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:NetworkMessageContentMask (declaration i=21121, owner"
              + " i=21116) on "
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
          "http://opcfoundation.org/UA/:NetworkMessageContentMask (declaration i=21121, owner"
              + " i=21116) on "
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
                            + " i=21121, owner i=21116) on "
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
                            + " i=21121, owner i=21116) on "
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
            "http://opcfoundation.org/UA/:NetworkMessageContentMask (declaration i=21121, owner"
                + " i=21116)"));
  }

  @Override
  public @Nullable UadpDataSetMessageContentMask getDataSetMessageContentMask() throws UaException {
    PropertyTypeNode node = getDataSetMessageContentMaskNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DataSetMessageContentMask (declaration i=21122, owner"
              + " i=21116) on "
              + getNodeId());
    }
    return (UadpDataSetMessageContentMask) node.getValue().getValue().getValue();
  }

  @Override
  public void setDataSetMessageContentMask(@Nullable UadpDataSetMessageContentMask value)
      throws UaException {
    PropertyTypeNode node = getDataSetMessageContentMaskNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DataSetMessageContentMask (declaration i=21122, owner"
              + " i=21116) on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UadpDataSetMessageContentMask readDataSetMessageContentMask()
      throws UaException {
    return ClientMembers.await(readDataSetMessageContentMaskAsync(), false);
  }

  @Override
  public void writeDataSetMessageContentMask(@Nullable UadpDataSetMessageContentMask value)
      throws UaException {
    try {
      StatusCode statusCode = writeDataSetMessageContentMaskAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UadpDataSetMessageContentMask>
      readDataSetMessageContentMaskAsync() {
    return getDataSetMessageContentMaskNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:DataSetMessageContentMask (declaration"
                            + " i=21122, owner i=21116) on "
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
                return (UadpDataSetMessageContentMask) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeDataSetMessageContentMaskAsync(
      @Nullable UadpDataSetMessageContentMask dataSetMessageContentMask) {
    return getDataSetMessageContentMaskNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:DataSetMessageContentMask (declaration"
                            + " i=21122, owner i=21116) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(dataSetMessageContentMask));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getDataSetMessageContentMaskNode() throws UaException {
    return ClientMembers.await(getDataSetMessageContentMaskNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getDataSetMessageContentMaskNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "DataSetMessageContentMask",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:DataSetMessageContentMask (declaration i=21122, owner"
                + " i=21116)"));
  }

  @Override
  public @Nullable Double getPublishingInterval() throws UaException {
    PropertyTypeNode node = getPublishingIntervalNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:PublishingInterval (declaration i=21123, owner i=21116)"
              + " on "
              + getNodeId());
    }
    return (Double) node.getValue().getValue().getValue();
  }

  @Override
  public void setPublishingInterval(@Nullable Double value) throws UaException {
    PropertyTypeNode node = getPublishingIntervalNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:PublishingInterval (declaration i=21123, owner i=21116)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable Double readPublishingInterval() throws UaException {
    return ClientMembers.await(readPublishingIntervalAsync(), false);
  }

  @Override
  public void writePublishingInterval(@Nullable Double value) throws UaException {
    try {
      StatusCode statusCode = writePublishingIntervalAsync(value).get();
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
  public CompletableFuture<? extends @Nullable Double> readPublishingIntervalAsync() {
    return getPublishingIntervalNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:PublishingInterval (declaration i=21123,"
                            + " owner i=21116) on "
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
  public CompletableFuture<StatusCode> writePublishingIntervalAsync(
      @Nullable Double publishingInterval) {
    return getPublishingIntervalNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:PublishingInterval (declaration i=21123,"
                            + " owner i=21116) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(publishingInterval));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getPublishingIntervalNode() throws UaException {
    return ClientMembers.await(getPublishingIntervalNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getPublishingIntervalNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "PublishingInterval",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:PublishingInterval (declaration i=21123, owner"
                + " i=21116)"));
  }

  @Override
  public @Nullable Double getProcessingOffset() throws UaException {
    PropertyTypeNode node = getProcessingOffsetNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ProcessingOffset (declaration i=21124, owner i=21116)"
              + " on "
              + getNodeId());
    }
    return (Double) node.getValue().getValue().getValue();
  }

  @Override
  public void setProcessingOffset(@Nullable Double value) throws UaException {
    PropertyTypeNode node = getProcessingOffsetNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ProcessingOffset (declaration i=21124, owner i=21116)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable Double readProcessingOffset() throws UaException {
    return ClientMembers.await(readProcessingOffsetAsync(), false);
  }

  @Override
  public void writeProcessingOffset(@Nullable Double value) throws UaException {
    try {
      StatusCode statusCode = writeProcessingOffsetAsync(value).get();
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
  public CompletableFuture<? extends @Nullable Double> readProcessingOffsetAsync() {
    return getProcessingOffsetNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ProcessingOffset (declaration i=21124, owner"
                            + " i=21116) on "
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
  public CompletableFuture<StatusCode> writeProcessingOffsetAsync(
      @Nullable Double processingOffset) {
    return getProcessingOffsetNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ProcessingOffset (declaration i=21124, owner"
                            + " i=21116) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(processingOffset));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getProcessingOffsetNode() throws UaException {
    return ClientMembers.await(getProcessingOffsetNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getProcessingOffsetNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ProcessingOffset",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:ProcessingOffset (declaration i=21124, owner i=21116)"));
  }

  @Override
  public @Nullable Double getReceiveOffset() throws UaException {
    PropertyTypeNode node = getReceiveOffsetNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ReceiveOffset (declaration i=21125, owner i=21116)"
              + " on "
              + getNodeId());
    }
    return (Double) node.getValue().getValue().getValue();
  }

  @Override
  public void setReceiveOffset(@Nullable Double value) throws UaException {
    PropertyTypeNode node = getReceiveOffsetNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ReceiveOffset (declaration i=21125, owner i=21116)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable Double readReceiveOffset() throws UaException {
    return ClientMembers.await(readReceiveOffsetAsync(), false);
  }

  @Override
  public void writeReceiveOffset(@Nullable Double value) throws UaException {
    try {
      StatusCode statusCode = writeReceiveOffsetAsync(value).get();
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
  public CompletableFuture<? extends @Nullable Double> readReceiveOffsetAsync() {
    return getReceiveOffsetNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ReceiveOffset (declaration i=21125, owner"
                            + " i=21116) on "
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
  public CompletableFuture<StatusCode> writeReceiveOffsetAsync(@Nullable Double receiveOffset) {
    return getReceiveOffsetNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ReceiveOffset (declaration i=21125, owner"
                            + " i=21116) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(receiveOffset));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getReceiveOffsetNode() throws UaException {
    return ClientMembers.await(getReceiveOffsetNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getReceiveOffsetNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ReceiveOffset",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:ReceiveOffset (declaration i=21125, owner i=21116)"));
  }
}

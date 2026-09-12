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
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.jspecify.annotations.Nullable;

public class OperationLimitsTypeNode extends FolderTypeNode implements OperationLimitsType {
  public OperationLimitsTypeNode(
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
  public static ClientViews createViews(OperationLimitsTypeNode node) {
    Objects.requireNonNull(node, "node");
    return ClientViews.forNode(node.client, node);
  }

  @Override
  public @Nullable UInteger getMaxNodesPerRead() throws UaException {
    PropertyTypeNode node = getMaxNodesPerReadNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxNodesPerRead (declaration i=11565, owner i=11564)"
              + " on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxNodesPerRead(@Nullable UInteger value) throws UaException {
    PropertyTypeNode node = getMaxNodesPerReadNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxNodesPerRead (declaration i=11565, owner i=11564)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UInteger readMaxNodesPerRead() throws UaException {
    return ClientMembers.await(readMaxNodesPerReadAsync(), false);
  }

  @Override
  public void writeMaxNodesPerRead(@Nullable UInteger value) throws UaException {
    try {
      StatusCode statusCode = writeMaxNodesPerReadAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UInteger> readMaxNodesPerReadAsync() {
    return getMaxNodesPerReadNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxNodesPerRead (declaration i=11565, owner"
                            + " i=11564) on "
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
  public CompletableFuture<StatusCode> writeMaxNodesPerReadAsync(
      @Nullable UInteger maxNodesPerRead) {
    return getMaxNodesPerReadNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxNodesPerRead (declaration i=11565, owner"
                            + " i=11564) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(maxNodesPerRead));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getMaxNodesPerReadNode() throws UaException {
    return ClientMembers.await(getMaxNodesPerReadNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getMaxNodesPerReadNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxNodesPerRead",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:MaxNodesPerRead (declaration i=11565, owner i=11564)"));
  }

  @Override
  public @Nullable UInteger getMaxNodesPerHistoryReadData() throws UaException {
    PropertyTypeNode node = getMaxNodesPerHistoryReadDataNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxNodesPerHistoryReadData (declaration i=12161, owner"
              + " i=11564) on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxNodesPerHistoryReadData(@Nullable UInteger value) throws UaException {
    PropertyTypeNode node = getMaxNodesPerHistoryReadDataNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxNodesPerHistoryReadData (declaration i=12161, owner"
              + " i=11564) on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UInteger readMaxNodesPerHistoryReadData() throws UaException {
    return ClientMembers.await(readMaxNodesPerHistoryReadDataAsync(), false);
  }

  @Override
  public void writeMaxNodesPerHistoryReadData(@Nullable UInteger value) throws UaException {
    try {
      StatusCode statusCode = writeMaxNodesPerHistoryReadDataAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UInteger> readMaxNodesPerHistoryReadDataAsync() {
    return getMaxNodesPerHistoryReadDataNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxNodesPerHistoryReadData (declaration"
                            + " i=12161, owner i=11564) on "
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
  public CompletableFuture<StatusCode> writeMaxNodesPerHistoryReadDataAsync(
      @Nullable UInteger maxNodesPerHistoryReadData) {
    return getMaxNodesPerHistoryReadDataNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxNodesPerHistoryReadData (declaration"
                            + " i=12161, owner i=11564) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(maxNodesPerHistoryReadData));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getMaxNodesPerHistoryReadDataNode() throws UaException {
    return ClientMembers.await(getMaxNodesPerHistoryReadDataNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getMaxNodesPerHistoryReadDataNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxNodesPerHistoryReadData",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:MaxNodesPerHistoryReadData (declaration i=12161, owner"
                + " i=11564)"));
  }

  @Override
  public @Nullable UInteger getMaxNodesPerHistoryReadEvents() throws UaException {
    PropertyTypeNode node = getMaxNodesPerHistoryReadEventsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxNodesPerHistoryReadEvents (declaration i=12162, owner"
              + " i=11564) on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxNodesPerHistoryReadEvents(@Nullable UInteger value) throws UaException {
    PropertyTypeNode node = getMaxNodesPerHistoryReadEventsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxNodesPerHistoryReadEvents (declaration i=12162, owner"
              + " i=11564) on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UInteger readMaxNodesPerHistoryReadEvents() throws UaException {
    return ClientMembers.await(readMaxNodesPerHistoryReadEventsAsync(), false);
  }

  @Override
  public void writeMaxNodesPerHistoryReadEvents(@Nullable UInteger value) throws UaException {
    try {
      StatusCode statusCode = writeMaxNodesPerHistoryReadEventsAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UInteger> readMaxNodesPerHistoryReadEventsAsync() {
    return getMaxNodesPerHistoryReadEventsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxNodesPerHistoryReadEvents (declaration"
                            + " i=12162, owner i=11564) on "
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
  public CompletableFuture<StatusCode> writeMaxNodesPerHistoryReadEventsAsync(
      @Nullable UInteger maxNodesPerHistoryReadEvents) {
    return getMaxNodesPerHistoryReadEventsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxNodesPerHistoryReadEvents (declaration"
                            + " i=12162, owner i=11564) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(maxNodesPerHistoryReadEvents));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getMaxNodesPerHistoryReadEventsNode() throws UaException {
    return ClientMembers.await(getMaxNodesPerHistoryReadEventsNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getMaxNodesPerHistoryReadEventsNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxNodesPerHistoryReadEvents",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:MaxNodesPerHistoryReadEvents (declaration i=12162, owner"
                + " i=11564)"));
  }

  @Override
  public @Nullable UInteger getMaxNodesPerWrite() throws UaException {
    PropertyTypeNode node = getMaxNodesPerWriteNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxNodesPerWrite (declaration i=11567, owner i=11564)"
              + " on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxNodesPerWrite(@Nullable UInteger value) throws UaException {
    PropertyTypeNode node = getMaxNodesPerWriteNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxNodesPerWrite (declaration i=11567, owner i=11564)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UInteger readMaxNodesPerWrite() throws UaException {
    return ClientMembers.await(readMaxNodesPerWriteAsync(), false);
  }

  @Override
  public void writeMaxNodesPerWrite(@Nullable UInteger value) throws UaException {
    try {
      StatusCode statusCode = writeMaxNodesPerWriteAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UInteger> readMaxNodesPerWriteAsync() {
    return getMaxNodesPerWriteNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxNodesPerWrite (declaration i=11567, owner"
                            + " i=11564) on "
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
  public CompletableFuture<StatusCode> writeMaxNodesPerWriteAsync(
      @Nullable UInteger maxNodesPerWrite) {
    return getMaxNodesPerWriteNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxNodesPerWrite (declaration i=11567, owner"
                            + " i=11564) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(maxNodesPerWrite));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getMaxNodesPerWriteNode() throws UaException {
    return ClientMembers.await(getMaxNodesPerWriteNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getMaxNodesPerWriteNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxNodesPerWrite",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:MaxNodesPerWrite (declaration i=11567, owner i=11564)"));
  }

  @Override
  public @Nullable UInteger getMaxNodesPerHistoryUpdateData() throws UaException {
    PropertyTypeNode node = getMaxNodesPerHistoryUpdateDataNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxNodesPerHistoryUpdateData (declaration i=12163, owner"
              + " i=11564) on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxNodesPerHistoryUpdateData(@Nullable UInteger value) throws UaException {
    PropertyTypeNode node = getMaxNodesPerHistoryUpdateDataNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxNodesPerHistoryUpdateData (declaration i=12163, owner"
              + " i=11564) on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UInteger readMaxNodesPerHistoryUpdateData() throws UaException {
    return ClientMembers.await(readMaxNodesPerHistoryUpdateDataAsync(), false);
  }

  @Override
  public void writeMaxNodesPerHistoryUpdateData(@Nullable UInteger value) throws UaException {
    try {
      StatusCode statusCode = writeMaxNodesPerHistoryUpdateDataAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UInteger> readMaxNodesPerHistoryUpdateDataAsync() {
    return getMaxNodesPerHistoryUpdateDataNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxNodesPerHistoryUpdateData (declaration"
                            + " i=12163, owner i=11564) on "
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
  public CompletableFuture<StatusCode> writeMaxNodesPerHistoryUpdateDataAsync(
      @Nullable UInteger maxNodesPerHistoryUpdateData) {
    return getMaxNodesPerHistoryUpdateDataNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxNodesPerHistoryUpdateData (declaration"
                            + " i=12163, owner i=11564) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(maxNodesPerHistoryUpdateData));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getMaxNodesPerHistoryUpdateDataNode() throws UaException {
    return ClientMembers.await(getMaxNodesPerHistoryUpdateDataNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getMaxNodesPerHistoryUpdateDataNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxNodesPerHistoryUpdateData",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:MaxNodesPerHistoryUpdateData (declaration i=12163, owner"
                + " i=11564)"));
  }

  @Override
  public @Nullable UInteger getMaxNodesPerHistoryUpdateEvents() throws UaException {
    PropertyTypeNode node = getMaxNodesPerHistoryUpdateEventsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxNodesPerHistoryUpdateEvents (declaration i=12164, owner"
              + " i=11564) on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxNodesPerHistoryUpdateEvents(@Nullable UInteger value) throws UaException {
    PropertyTypeNode node = getMaxNodesPerHistoryUpdateEventsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxNodesPerHistoryUpdateEvents (declaration i=12164, owner"
              + " i=11564) on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UInteger readMaxNodesPerHistoryUpdateEvents() throws UaException {
    return ClientMembers.await(readMaxNodesPerHistoryUpdateEventsAsync(), false);
  }

  @Override
  public void writeMaxNodesPerHistoryUpdateEvents(@Nullable UInteger value) throws UaException {
    try {
      StatusCode statusCode = writeMaxNodesPerHistoryUpdateEventsAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UInteger> readMaxNodesPerHistoryUpdateEventsAsync() {
    return getMaxNodesPerHistoryUpdateEventsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxNodesPerHistoryUpdateEvents (declaration"
                            + " i=12164, owner i=11564) on "
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
  public CompletableFuture<StatusCode> writeMaxNodesPerHistoryUpdateEventsAsync(
      @Nullable UInteger maxNodesPerHistoryUpdateEvents) {
    return getMaxNodesPerHistoryUpdateEventsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxNodesPerHistoryUpdateEvents (declaration"
                            + " i=12164, owner i=11564) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(maxNodesPerHistoryUpdateEvents));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getMaxNodesPerHistoryUpdateEventsNode() throws UaException {
    return ClientMembers.await(getMaxNodesPerHistoryUpdateEventsNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getMaxNodesPerHistoryUpdateEventsNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxNodesPerHistoryUpdateEvents",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:MaxNodesPerHistoryUpdateEvents (declaration i=12164,"
                + " owner i=11564)"));
  }

  @Override
  public @Nullable UInteger getMaxNodesPerMethodCall() throws UaException {
    PropertyTypeNode node = getMaxNodesPerMethodCallNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxNodesPerMethodCall (declaration i=11569, owner i=11564)"
              + " on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxNodesPerMethodCall(@Nullable UInteger value) throws UaException {
    PropertyTypeNode node = getMaxNodesPerMethodCallNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxNodesPerMethodCall (declaration i=11569, owner i=11564)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UInteger readMaxNodesPerMethodCall() throws UaException {
    return ClientMembers.await(readMaxNodesPerMethodCallAsync(), false);
  }

  @Override
  public void writeMaxNodesPerMethodCall(@Nullable UInteger value) throws UaException {
    try {
      StatusCode statusCode = writeMaxNodesPerMethodCallAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UInteger> readMaxNodesPerMethodCallAsync() {
    return getMaxNodesPerMethodCallNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxNodesPerMethodCall (declaration i=11569,"
                            + " owner i=11564) on "
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
  public CompletableFuture<StatusCode> writeMaxNodesPerMethodCallAsync(
      @Nullable UInteger maxNodesPerMethodCall) {
    return getMaxNodesPerMethodCallNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxNodesPerMethodCall (declaration i=11569,"
                            + " owner i=11564) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(maxNodesPerMethodCall));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getMaxNodesPerMethodCallNode() throws UaException {
    return ClientMembers.await(getMaxNodesPerMethodCallNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getMaxNodesPerMethodCallNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxNodesPerMethodCall",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:MaxNodesPerMethodCall (declaration i=11569, owner"
                + " i=11564)"));
  }

  @Override
  public @Nullable UInteger getMaxNodesPerBrowse() throws UaException {
    PropertyTypeNode node = getMaxNodesPerBrowseNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxNodesPerBrowse (declaration i=11570, owner i=11564)"
              + " on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxNodesPerBrowse(@Nullable UInteger value) throws UaException {
    PropertyTypeNode node = getMaxNodesPerBrowseNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxNodesPerBrowse (declaration i=11570, owner i=11564)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UInteger readMaxNodesPerBrowse() throws UaException {
    return ClientMembers.await(readMaxNodesPerBrowseAsync(), false);
  }

  @Override
  public void writeMaxNodesPerBrowse(@Nullable UInteger value) throws UaException {
    try {
      StatusCode statusCode = writeMaxNodesPerBrowseAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UInteger> readMaxNodesPerBrowseAsync() {
    return getMaxNodesPerBrowseNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxNodesPerBrowse (declaration i=11570, owner"
                            + " i=11564) on "
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
  public CompletableFuture<StatusCode> writeMaxNodesPerBrowseAsync(
      @Nullable UInteger maxNodesPerBrowse) {
    return getMaxNodesPerBrowseNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxNodesPerBrowse (declaration i=11570, owner"
                            + " i=11564) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(maxNodesPerBrowse));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getMaxNodesPerBrowseNode() throws UaException {
    return ClientMembers.await(getMaxNodesPerBrowseNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getMaxNodesPerBrowseNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxNodesPerBrowse",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:MaxNodesPerBrowse (declaration i=11570, owner i=11564)"));
  }

  @Override
  public @Nullable UInteger getMaxNodesPerRegisterNodes() throws UaException {
    PropertyTypeNode node = getMaxNodesPerRegisterNodesNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxNodesPerRegisterNodes (declaration i=11571, owner"
              + " i=11564) on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxNodesPerRegisterNodes(@Nullable UInteger value) throws UaException {
    PropertyTypeNode node = getMaxNodesPerRegisterNodesNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxNodesPerRegisterNodes (declaration i=11571, owner"
              + " i=11564) on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UInteger readMaxNodesPerRegisterNodes() throws UaException {
    return ClientMembers.await(readMaxNodesPerRegisterNodesAsync(), false);
  }

  @Override
  public void writeMaxNodesPerRegisterNodes(@Nullable UInteger value) throws UaException {
    try {
      StatusCode statusCode = writeMaxNodesPerRegisterNodesAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UInteger> readMaxNodesPerRegisterNodesAsync() {
    return getMaxNodesPerRegisterNodesNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxNodesPerRegisterNodes (declaration"
                            + " i=11571, owner i=11564) on "
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
  public CompletableFuture<StatusCode> writeMaxNodesPerRegisterNodesAsync(
      @Nullable UInteger maxNodesPerRegisterNodes) {
    return getMaxNodesPerRegisterNodesNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxNodesPerRegisterNodes (declaration"
                            + " i=11571, owner i=11564) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(maxNodesPerRegisterNodes));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getMaxNodesPerRegisterNodesNode() throws UaException {
    return ClientMembers.await(getMaxNodesPerRegisterNodesNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getMaxNodesPerRegisterNodesNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxNodesPerRegisterNodes",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:MaxNodesPerRegisterNodes (declaration i=11571, owner"
                + " i=11564)"));
  }

  @Override
  public @Nullable UInteger getMaxNodesPerTranslateBrowsePathsToNodeIds() throws UaException {
    PropertyTypeNode node = getMaxNodesPerTranslateBrowsePathsToNodeIdsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxNodesPerTranslateBrowsePathsToNodeIds (declaration"
              + " i=11572, owner i=11564) on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxNodesPerTranslateBrowsePathsToNodeIds(@Nullable UInteger value)
      throws UaException {
    PropertyTypeNode node = getMaxNodesPerTranslateBrowsePathsToNodeIdsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxNodesPerTranslateBrowsePathsToNodeIds (declaration"
              + " i=11572, owner i=11564) on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UInteger readMaxNodesPerTranslateBrowsePathsToNodeIds() throws UaException {
    return ClientMembers.await(readMaxNodesPerTranslateBrowsePathsToNodeIdsAsync(), false);
  }

  @Override
  public void writeMaxNodesPerTranslateBrowsePathsToNodeIds(@Nullable UInteger value)
      throws UaException {
    try {
      StatusCode statusCode = writeMaxNodesPerTranslateBrowsePathsToNodeIdsAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UInteger>
      readMaxNodesPerTranslateBrowsePathsToNodeIdsAsync() {
    return getMaxNodesPerTranslateBrowsePathsToNodeIdsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxNodesPerTranslateBrowsePathsToNodeIds"
                            + " (declaration i=11572, owner i=11564) on "
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
  public CompletableFuture<StatusCode> writeMaxNodesPerTranslateBrowsePathsToNodeIdsAsync(
      @Nullable UInteger maxNodesPerTranslateBrowsePathsToNodeIds) {
    return getMaxNodesPerTranslateBrowsePathsToNodeIdsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxNodesPerTranslateBrowsePathsToNodeIds"
                            + " (declaration i=11572, owner i=11564) on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(new Variant(maxNodesPerTranslateBrowsePathsToNodeIds));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getMaxNodesPerTranslateBrowsePathsToNodeIdsNode()
      throws UaException {
    return ClientMembers.await(getMaxNodesPerTranslateBrowsePathsToNodeIdsNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getMaxNodesPerTranslateBrowsePathsToNodeIdsNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxNodesPerTranslateBrowsePathsToNodeIds",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:MaxNodesPerTranslateBrowsePathsToNodeIds (declaration"
                + " i=11572, owner i=11564)"));
  }

  @Override
  public @Nullable UInteger getMaxNodesPerNodeManagement() throws UaException {
    PropertyTypeNode node = getMaxNodesPerNodeManagementNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxNodesPerNodeManagement (declaration i=11573, owner"
              + " i=11564) on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxNodesPerNodeManagement(@Nullable UInteger value) throws UaException {
    PropertyTypeNode node = getMaxNodesPerNodeManagementNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxNodesPerNodeManagement (declaration i=11573, owner"
              + " i=11564) on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UInteger readMaxNodesPerNodeManagement() throws UaException {
    return ClientMembers.await(readMaxNodesPerNodeManagementAsync(), false);
  }

  @Override
  public void writeMaxNodesPerNodeManagement(@Nullable UInteger value) throws UaException {
    try {
      StatusCode statusCode = writeMaxNodesPerNodeManagementAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UInteger> readMaxNodesPerNodeManagementAsync() {
    return getMaxNodesPerNodeManagementNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxNodesPerNodeManagement (declaration"
                            + " i=11573, owner i=11564) on "
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
  public CompletableFuture<StatusCode> writeMaxNodesPerNodeManagementAsync(
      @Nullable UInteger maxNodesPerNodeManagement) {
    return getMaxNodesPerNodeManagementNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxNodesPerNodeManagement (declaration"
                            + " i=11573, owner i=11564) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(maxNodesPerNodeManagement));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getMaxNodesPerNodeManagementNode() throws UaException {
    return ClientMembers.await(getMaxNodesPerNodeManagementNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getMaxNodesPerNodeManagementNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxNodesPerNodeManagement",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:MaxNodesPerNodeManagement (declaration i=11573, owner"
                + " i=11564)"));
  }

  @Override
  public @Nullable UInteger getMaxMonitoredItemsPerCall() throws UaException {
    PropertyTypeNode node = getMaxMonitoredItemsPerCallNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxMonitoredItemsPerCall (declaration i=11574, owner"
              + " i=11564) on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxMonitoredItemsPerCall(@Nullable UInteger value) throws UaException {
    PropertyTypeNode node = getMaxMonitoredItemsPerCallNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxMonitoredItemsPerCall (declaration i=11574, owner"
              + " i=11564) on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UInteger readMaxMonitoredItemsPerCall() throws UaException {
    return ClientMembers.await(readMaxMonitoredItemsPerCallAsync(), false);
  }

  @Override
  public void writeMaxMonitoredItemsPerCall(@Nullable UInteger value) throws UaException {
    try {
      StatusCode statusCode = writeMaxMonitoredItemsPerCallAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UInteger> readMaxMonitoredItemsPerCallAsync() {
    return getMaxMonitoredItemsPerCallNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxMonitoredItemsPerCall (declaration"
                            + " i=11574, owner i=11564) on "
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
  public CompletableFuture<StatusCode> writeMaxMonitoredItemsPerCallAsync(
      @Nullable UInteger maxMonitoredItemsPerCall) {
    return getMaxMonitoredItemsPerCallNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxMonitoredItemsPerCall (declaration"
                            + " i=11574, owner i=11564) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(maxMonitoredItemsPerCall));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getMaxMonitoredItemsPerCallNode() throws UaException {
    return ClientMembers.await(getMaxMonitoredItemsPerCallNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getMaxMonitoredItemsPerCallNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxMonitoredItemsPerCall",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:MaxMonitoredItemsPerCall (declaration i=11574, owner"
                + " i=11564)"));
  }
}

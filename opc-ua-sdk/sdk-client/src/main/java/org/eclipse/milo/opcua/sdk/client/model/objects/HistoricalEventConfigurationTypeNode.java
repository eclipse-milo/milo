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
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
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
import org.eclipse.milo.opcua.stack.core.types.structured.SimpleAttributeOperand;
import org.jspecify.annotations.Nullable;

public class HistoricalEventConfigurationTypeNode extends BaseObjectTypeNode
    implements HistoricalEventConfigurationType {
  public HistoricalEventConfigurationTypeNode(
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
  public static ClientViews createViews(HistoricalEventConfigurationTypeNode node) {
    Objects.requireNonNull(node, "node");
    return ClientViews.forNode(node.client, node);
  }

  @Override
  public @Nullable DateTime getStartOfArchive() throws UaException {
    PropertyTypeNode node = getStartOfArchiveNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:StartOfArchive (declaration i=32623, owner i=32621)"
              + " on "
              + getNodeId());
    }
    return (DateTime) node.getValue().getValue().getValue();
  }

  @Override
  public void setStartOfArchive(@Nullable DateTime value) throws UaException {
    PropertyTypeNode node = getStartOfArchiveNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:StartOfArchive (declaration i=32623, owner i=32621)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable DateTime readStartOfArchive() throws UaException {
    return ClientMembers.await(readStartOfArchiveAsync(), false);
  }

  @Override
  public void writeStartOfArchive(@Nullable DateTime value) throws UaException {
    try {
      StatusCode statusCode = writeStartOfArchiveAsync(value).get();
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
  public CompletableFuture<? extends @Nullable DateTime> readStartOfArchiveAsync() {
    return getStartOfArchiveNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:StartOfArchive (declaration i=32623, owner"
                            + " i=32621) on "
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
                return (DateTime) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeStartOfArchiveAsync(@Nullable DateTime startOfArchive) {
    return getStartOfArchiveNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:StartOfArchive (declaration i=32623, owner"
                            + " i=32621) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(startOfArchive));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getStartOfArchiveNode() throws UaException {
    return ClientMembers.await(getStartOfArchiveNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getStartOfArchiveNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "StartOfArchive",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:StartOfArchive (declaration i=32623, owner i=32621)"));
  }

  @Override
  public @Nullable DateTime getStartOfOnlineArchive() throws UaException {
    PropertyTypeNode node = getStartOfOnlineArchiveNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:StartOfOnlineArchive (declaration i=32624, owner i=32621)"
              + " on "
              + getNodeId());
    }
    return (DateTime) node.getValue().getValue().getValue();
  }

  @Override
  public void setStartOfOnlineArchive(@Nullable DateTime value) throws UaException {
    PropertyTypeNode node = getStartOfOnlineArchiveNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:StartOfOnlineArchive (declaration i=32624, owner i=32621)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable DateTime readStartOfOnlineArchive() throws UaException {
    return ClientMembers.await(readStartOfOnlineArchiveAsync(), false);
  }

  @Override
  public void writeStartOfOnlineArchive(@Nullable DateTime value) throws UaException {
    try {
      StatusCode statusCode = writeStartOfOnlineArchiveAsync(value).get();
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
  public CompletableFuture<? extends @Nullable DateTime> readStartOfOnlineArchiveAsync() {
    return getStartOfOnlineArchiveNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:StartOfOnlineArchive (declaration i=32624,"
                            + " owner i=32621) on "
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
                return (DateTime) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeStartOfOnlineArchiveAsync(
      @Nullable DateTime startOfOnlineArchive) {
    return getStartOfOnlineArchiveNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:StartOfOnlineArchive (declaration i=32624,"
                            + " owner i=32621) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(startOfOnlineArchive));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getStartOfOnlineArchiveNode() throws UaException {
    return ClientMembers.await(getStartOfOnlineArchiveNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getStartOfOnlineArchiveNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "StartOfOnlineArchive",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:StartOfOnlineArchive (declaration i=32624, owner"
                + " i=32621)"));
  }

  @Override
  public @Nullable SimpleAttributeOperand @Nullable [] getSortByEventFields() throws UaException {
    PropertyTypeNode node = getSortByEventFieldsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SortByEventFields (declaration i=18644, owner i=32621)"
              + " on "
              + getNodeId());
    }
    return (SimpleAttributeOperand[])
        decodeValue(
            node.getValue().getValue().getValue(),
            SimpleAttributeOperand.class,
            ValueRanks.OneDimension);
  }

  @Override
  public void setSortByEventFields(@Nullable SimpleAttributeOperand @Nullable [] value)
      throws UaException {
    PropertyTypeNode node = getSortByEventFieldsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SortByEventFields (declaration i=18644, owner i=32621)"
              + " on "
              + getNodeId());
    }
    node.setValue(
        new Variant(encodeValue(value, SimpleAttributeOperand.class, ValueRanks.OneDimension)));
  }

  @Override
  public @Nullable SimpleAttributeOperand @Nullable [] readSortByEventFields() throws UaException {
    return ClientMembers.await(readSortByEventFieldsAsync(), false);
  }

  @Override
  public void writeSortByEventFields(@Nullable SimpleAttributeOperand @Nullable [] value)
      throws UaException {
    try {
      StatusCode statusCode = writeSortByEventFieldsAsync(value).get();
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
  public CompletableFuture<? extends @Nullable SimpleAttributeOperand @Nullable []>
      readSortByEventFieldsAsync() {
    return getSortByEventFieldsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:SortByEventFields (declaration i=18644, owner"
                            + " i=32621) on "
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
                return (SimpleAttributeOperand[])
                    decodeValue(
                        v.getValue().getValue(),
                        SimpleAttributeOperand.class,
                        ValueRanks.OneDimension);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeSortByEventFieldsAsync(
      @Nullable SimpleAttributeOperand @Nullable [] sortByEventFields) {
    return getSortByEventFieldsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:SortByEventFields (declaration i=18644, owner"
                            + " i=32621) on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(
                            encodeValue(
                                sortByEventFields,
                                SimpleAttributeOperand.class,
                                ValueRanks.OneDimension)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getSortByEventFieldsNode() throws UaException {
    return ClientMembers.await(getSortByEventFieldsNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getSortByEventFieldsNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "SortByEventFields",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:SortByEventFields (declaration i=18644, owner i=32621)"));
  }

  @Override
  public FolderTypeNode getEventTypesNode() throws UaException {
    return ClientMembers.await(getEventTypesNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends FolderTypeNode> getEventTypesNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        FolderTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "EventTypes",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            false,
            "http://opcfoundation.org/UA/:EventTypes (declaration i=32622, owner i=32621)"));
  }
}

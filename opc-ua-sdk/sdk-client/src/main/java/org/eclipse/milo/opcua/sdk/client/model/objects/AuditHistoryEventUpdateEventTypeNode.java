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
import org.eclipse.milo.opcua.stack.core.types.enumerated.PerformUpdateType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.EventFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryEventFieldList;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.ArrayUtil;
import org.jspecify.annotations.Nullable;

public class AuditHistoryEventUpdateEventTypeNode extends AuditHistoryUpdateEventTypeNode
    implements AuditHistoryEventUpdateEventType {
  public AuditHistoryEventUpdateEventTypeNode(
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
  public static ClientViews createViews(AuditHistoryEventUpdateEventTypeNode node) {
    Objects.requireNonNull(node, "node");
    return ClientViews.forNode(node.client, node);
  }

  @Override
  public @Nullable NodeId getUpdatedNode() throws UaException {
    PropertyTypeNode node = getUpdatedNodeNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:UpdatedNode (declaration i=3025, owner i=2999)"
              + " on "
              + getNodeId());
    }
    return (NodeId) node.getValue().getValue().getValue();
  }

  @Override
  public void setUpdatedNode(@Nullable NodeId value) throws UaException {
    PropertyTypeNode node = getUpdatedNodeNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:UpdatedNode (declaration i=3025, owner i=2999)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable NodeId readUpdatedNode() throws UaException {
    return ClientMembers.await(readUpdatedNodeAsync(), false);
  }

  @Override
  public void writeUpdatedNode(@Nullable NodeId value) throws UaException {
    try {
      StatusCode statusCode = writeUpdatedNodeAsync(value).get();
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
  public CompletableFuture<? extends @Nullable NodeId> readUpdatedNodeAsync() {
    return getUpdatedNodeNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:UpdatedNode (declaration i=3025, owner"
                            + " i=2999) on "
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
  public CompletableFuture<StatusCode> writeUpdatedNodeAsync(@Nullable NodeId updatedNode) {
    return getUpdatedNodeNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:UpdatedNode (declaration i=3025, owner"
                            + " i=2999) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(updatedNode));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getUpdatedNodeNode() throws UaException {
    return ClientMembers.await(getUpdatedNodeNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getUpdatedNodeNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "UpdatedNode",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:UpdatedNode (declaration i=3025, owner i=2999)"));
  }

  @Override
  public @Nullable PerformUpdateType getPerformInsertReplace() throws UaException {
    PropertyTypeNode node = getPerformInsertReplaceNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:PerformInsertReplace (declaration i=3028, owner i=2999)"
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
              "PerformInsertReplace: ValueRank=-1 does not permit rank " + rank);
        }
        if (value != null && !((Object) value instanceof PerformUpdateType)) {
          if (!(value instanceof Integer)) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "PerformInsertReplace: expected"
                    + " org.eclipse.milo.opcua.stack.core.types.enumerated.PerformUpdateType or"
                    + " Int32, got "
                    + value);
          }
          if (PerformUpdateType.from((Integer) value) == null) {
            throw new UaException(
                StatusCodes.Bad_OutOfRange,
                "PerformInsertReplace: unknown"
                    + " org.eclipse.milo.opcua.stack.core.types.enumerated.PerformUpdateType value "
                    + value);
          }
        }
        convertedValue =
            value == null || value instanceof PerformUpdateType
                ? (PerformUpdateType) value
                : PerformUpdateType.from((Integer) value);
      }
    }
    return (PerformUpdateType) convertedValue;
  }

  @Override
  public void setPerformInsertReplace(@Nullable PerformUpdateType value) throws UaException {
    PropertyTypeNode node = getPerformInsertReplaceNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:PerformInsertReplace (declaration i=3028, owner i=2999)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable PerformUpdateType readPerformInsertReplace() throws UaException {
    return ClientMembers.await(readPerformInsertReplaceAsync(), false);
  }

  @Override
  public void writePerformInsertReplace(@Nullable PerformUpdateType value) throws UaException {
    try {
      StatusCode statusCode = writePerformInsertReplaceAsync(value).get();
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
  public CompletableFuture<? extends @Nullable PerformUpdateType> readPerformInsertReplaceAsync() {
    return getPerformInsertReplaceNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:PerformInsertReplace (declaration i=3028,"
                            + " owner i=2999) on "
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
                          "PerformInsertReplace: ValueRank=-1 does not permit rank " + rank);
                    }
                    if (value != null && !((Object) value instanceof PerformUpdateType)) {
                      if (!(value instanceof Integer)) {
                        throw new UaRuntimeException(
                            StatusCodes.Bad_TypeMismatch,
                            "PerformInsertReplace: expected"
                                + " org.eclipse.milo.opcua.stack.core.types.enumerated.PerformUpdateType"
                                + " or Int32, got "
                                + value);
                      }
                      if (PerformUpdateType.from((Integer) value) == null) {
                        throw new UaRuntimeException(
                            StatusCodes.Bad_OutOfRange,
                            "PerformInsertReplace: unknown"
                                + " org.eclipse.milo.opcua.stack.core.types.enumerated.PerformUpdateType"
                                + " value "
                                + value);
                      }
                    }
                    convertedValue =
                        value == null || value instanceof PerformUpdateType
                            ? (PerformUpdateType) value
                            : PerformUpdateType.from((Integer) value);
                  }
                }
                return (PerformUpdateType) convertedValue;
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writePerformInsertReplaceAsync(
      @Nullable PerformUpdateType performInsertReplace) {
    return getPerformInsertReplaceNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:PerformInsertReplace (declaration i=3028,"
                            + " owner i=2999) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(performInsertReplace));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getPerformInsertReplaceNode() throws UaException {
    return ClientMembers.await(getPerformInsertReplaceNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getPerformInsertReplaceNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "PerformInsertReplace",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:PerformInsertReplace (declaration i=3028, owner"
                + " i=2999)"));
  }

  @Override
  public @Nullable EventFilter getFilter() throws UaException {
    PropertyTypeNode node = getFilterNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:Filter (declaration i=3003, owner i=2999)"
              + " on "
              + getNodeId());
    }
    return (EventFilter)
        decodeValue(node.getValue().getValue().getValue(), EventFilter.class, ValueRanks.Scalar);
  }

  @Override
  public void setFilter(@Nullable EventFilter value) throws UaException {
    PropertyTypeNode node = getFilterNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:Filter (declaration i=3003, owner i=2999)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(encodeValue(value, EventFilter.class, ValueRanks.Scalar)));
  }

  @Override
  public @Nullable EventFilter readFilter() throws UaException {
    return ClientMembers.await(readFilterAsync(), false);
  }

  @Override
  public void writeFilter(@Nullable EventFilter value) throws UaException {
    try {
      StatusCode statusCode = writeFilterAsync(value).get();
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
  public CompletableFuture<? extends @Nullable EventFilter> readFilterAsync() {
    return getFilterNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:Filter (declaration i=3003, owner i=2999)"
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
                return (EventFilter)
                    decodeValue(v.getValue().getValue(), EventFilter.class, ValueRanks.Scalar);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeFilterAsync(@Nullable EventFilter filter) {
    return getFilterNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:Filter (declaration i=3003, owner i=2999)"
                            + " on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(encodeValue(filter, EventFilter.class, ValueRanks.Scalar)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getFilterNode() throws UaException {
    return ClientMembers.await(getFilterNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getFilterNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "Filter",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:Filter (declaration i=3003, owner i=2999)"));
  }

  @Override
  public @Nullable HistoryEventFieldList @Nullable [] getNewValues() throws UaException {
    PropertyTypeNode node = getNewValuesNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:NewValues (declaration i=3029, owner i=2999)"
              + " on "
              + getNodeId());
    }
    return (HistoryEventFieldList[])
        decodeValue(
            node.getValue().getValue().getValue(),
            HistoryEventFieldList.class,
            ValueRanks.OneDimension);
  }

  @Override
  public void setNewValues(@Nullable HistoryEventFieldList @Nullable [] value) throws UaException {
    PropertyTypeNode node = getNewValuesNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:NewValues (declaration i=3029, owner i=2999)"
              + " on "
              + getNodeId());
    }
    node.setValue(
        new Variant(encodeValue(value, HistoryEventFieldList.class, ValueRanks.OneDimension)));
  }

  @Override
  public @Nullable HistoryEventFieldList @Nullable [] readNewValues() throws UaException {
    return ClientMembers.await(readNewValuesAsync(), false);
  }

  @Override
  public void writeNewValues(@Nullable HistoryEventFieldList @Nullable [] value)
      throws UaException {
    try {
      StatusCode statusCode = writeNewValuesAsync(value).get();
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
  public CompletableFuture<? extends @Nullable HistoryEventFieldList @Nullable []>
      readNewValuesAsync() {
    return getNewValuesNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:NewValues (declaration i=3029, owner i=2999)"
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
                return (HistoryEventFieldList[])
                    decodeValue(
                        v.getValue().getValue(),
                        HistoryEventFieldList.class,
                        ValueRanks.OneDimension);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeNewValuesAsync(
      @Nullable HistoryEventFieldList @Nullable [] newValues) {
    return getNewValuesNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:NewValues (declaration i=3029, owner i=2999)"
                            + " on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(
                            encodeValue(
                                newValues, HistoryEventFieldList.class, ValueRanks.OneDimension)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getNewValuesNode() throws UaException {
    return ClientMembers.await(getNewValuesNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getNewValuesNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "NewValues",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:NewValues (declaration i=3029, owner i=2999)"));
  }

  @Override
  public @Nullable HistoryEventFieldList @Nullable [] getOldValues() throws UaException {
    PropertyTypeNode node = getOldValuesNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:OldValues (declaration i=3030, owner i=2999)"
              + " on "
              + getNodeId());
    }
    return (HistoryEventFieldList[])
        decodeValue(
            node.getValue().getValue().getValue(),
            HistoryEventFieldList.class,
            ValueRanks.OneDimension);
  }

  @Override
  public void setOldValues(@Nullable HistoryEventFieldList @Nullable [] value) throws UaException {
    PropertyTypeNode node = getOldValuesNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:OldValues (declaration i=3030, owner i=2999)"
              + " on "
              + getNodeId());
    }
    node.setValue(
        new Variant(encodeValue(value, HistoryEventFieldList.class, ValueRanks.OneDimension)));
  }

  @Override
  public @Nullable HistoryEventFieldList @Nullable [] readOldValues() throws UaException {
    return ClientMembers.await(readOldValuesAsync(), false);
  }

  @Override
  public void writeOldValues(@Nullable HistoryEventFieldList @Nullable [] value)
      throws UaException {
    try {
      StatusCode statusCode = writeOldValuesAsync(value).get();
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
  public CompletableFuture<? extends @Nullable HistoryEventFieldList @Nullable []>
      readOldValuesAsync() {
    return getOldValuesNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:OldValues (declaration i=3030, owner i=2999)"
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
                return (HistoryEventFieldList[])
                    decodeValue(
                        v.getValue().getValue(),
                        HistoryEventFieldList.class,
                        ValueRanks.OneDimension);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeOldValuesAsync(
      @Nullable HistoryEventFieldList @Nullable [] oldValues) {
    return getOldValuesNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:OldValues (declaration i=3030, owner i=2999)"
                            + " on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(
                            encodeValue(
                                oldValues, HistoryEventFieldList.class, ValueRanks.OneDimension)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getOldValuesNode() throws UaException {
    return ClientMembers.await(getOldValuesNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getOldValuesNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "OldValues",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:OldValues (declaration i=3030, owner i=2999)"));
  }
}

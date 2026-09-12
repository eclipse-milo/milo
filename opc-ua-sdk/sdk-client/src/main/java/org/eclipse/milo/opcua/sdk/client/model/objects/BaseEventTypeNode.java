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
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.TimeZoneDataType;
import org.jspecify.annotations.Nullable;

public class BaseEventTypeNode extends BaseObjectTypeNode implements BaseEventType {
  public BaseEventTypeNode(
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
  public static ClientViews createViews(BaseEventTypeNode node) {
    Objects.requireNonNull(node, "node");
    return ClientViews.forNode(node.client, node);
  }

  @Override
  public @Nullable ByteString getEventId() throws UaException {
    PropertyTypeNode node = getEventIdNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:EventId (declaration i=2042, owner i=2041)"
              + " on "
              + getNodeId());
    }
    return (ByteString) node.getValue().getValue().getValue();
  }

  @Override
  public void setEventId(@Nullable ByteString value) throws UaException {
    PropertyTypeNode node = getEventIdNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:EventId (declaration i=2042, owner i=2041)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable ByteString readEventId() throws UaException {
    return ClientMembers.await(readEventIdAsync(), false);
  }

  @Override
  public void writeEventId(@Nullable ByteString value) throws UaException {
    try {
      StatusCode statusCode = writeEventIdAsync(value).get();
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
  public CompletableFuture<? extends @Nullable ByteString> readEventIdAsync() {
    return getEventIdNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:EventId (declaration i=2042, owner i=2041)"
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
                return (ByteString) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeEventIdAsync(@Nullable ByteString eventId) {
    return getEventIdNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:EventId (declaration i=2042, owner i=2041)"
                            + " on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(eventId));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getEventIdNode() throws UaException {
    return ClientMembers.await(getEventIdNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getEventIdNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "EventId",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:EventId (declaration i=2042, owner i=2041)"));
  }

  @Override
  public @Nullable NodeId getEventType() throws UaException {
    PropertyTypeNode node = getEventTypeNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:EventType (declaration i=2043, owner i=2041)"
              + " on "
              + getNodeId());
    }
    return (NodeId) node.getValue().getValue().getValue();
  }

  @Override
  public void setEventType(@Nullable NodeId value) throws UaException {
    PropertyTypeNode node = getEventTypeNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:EventType (declaration i=2043, owner i=2041)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable NodeId readEventType() throws UaException {
    return ClientMembers.await(readEventTypeAsync(), false);
  }

  @Override
  public void writeEventType(@Nullable NodeId value) throws UaException {
    try {
      StatusCode statusCode = writeEventTypeAsync(value).get();
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
  public CompletableFuture<? extends @Nullable NodeId> readEventTypeAsync() {
    return getEventTypeNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:EventType (declaration i=2043, owner i=2041)"
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
                return (NodeId) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeEventTypeAsync(@Nullable NodeId eventType) {
    return getEventTypeNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:EventType (declaration i=2043, owner i=2041)"
                            + " on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(eventType));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getEventTypeNode() throws UaException {
    return ClientMembers.await(getEventTypeNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getEventTypeNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "EventType",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:EventType (declaration i=2043, owner i=2041)"));
  }

  @Override
  public @Nullable NodeId getSourceNode() throws UaException {
    PropertyTypeNode node = getSourceNodeNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SourceNode (declaration i=2044, owner i=2041)"
              + " on "
              + getNodeId());
    }
    return (NodeId) node.getValue().getValue().getValue();
  }

  @Override
  public void setSourceNode(@Nullable NodeId value) throws UaException {
    PropertyTypeNode node = getSourceNodeNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SourceNode (declaration i=2044, owner i=2041)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable NodeId readSourceNode() throws UaException {
    return ClientMembers.await(readSourceNodeAsync(), false);
  }

  @Override
  public void writeSourceNode(@Nullable NodeId value) throws UaException {
    try {
      StatusCode statusCode = writeSourceNodeAsync(value).get();
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
  public CompletableFuture<? extends @Nullable NodeId> readSourceNodeAsync() {
    return getSourceNodeNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:SourceNode (declaration i=2044, owner i=2041)"
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
                return (NodeId) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeSourceNodeAsync(@Nullable NodeId sourceNode) {
    return getSourceNodeNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:SourceNode (declaration i=2044, owner i=2041)"
                            + " on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(sourceNode));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getSourceNodeNode() throws UaException {
    return ClientMembers.await(getSourceNodeNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getSourceNodeNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "SourceNode",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:SourceNode (declaration i=2044, owner i=2041)"));
  }

  @Override
  public @Nullable String getSourceName() throws UaException {
    PropertyTypeNode node = getSourceNameNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SourceName (declaration i=2045, owner i=2041)"
              + " on "
              + getNodeId());
    }
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setSourceName(@Nullable String value) throws UaException {
    PropertyTypeNode node = getSourceNameNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SourceName (declaration i=2045, owner i=2041)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable String readSourceName() throws UaException {
    return ClientMembers.await(readSourceNameAsync(), false);
  }

  @Override
  public void writeSourceName(@Nullable String value) throws UaException {
    try {
      StatusCode statusCode = writeSourceNameAsync(value).get();
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
  public CompletableFuture<? extends @Nullable String> readSourceNameAsync() {
    return getSourceNameNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:SourceName (declaration i=2045, owner i=2041)"
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
                return (String) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeSourceNameAsync(@Nullable String sourceName) {
    return getSourceNameNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:SourceName (declaration i=2045, owner i=2041)"
                            + " on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(sourceName));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getSourceNameNode() throws UaException {
    return ClientMembers.await(getSourceNameNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getSourceNameNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "SourceName",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:SourceName (declaration i=2045, owner i=2041)"));
  }

  @Override
  public @Nullable DateTime getTime() throws UaException {
    PropertyTypeNode node = getTimeNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:Time (declaration i=2046, owner i=2041)"
              + " on "
              + getNodeId());
    }
    return (DateTime) node.getValue().getValue().getValue();
  }

  @Override
  public void setTime(@Nullable DateTime value) throws UaException {
    PropertyTypeNode node = getTimeNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:Time (declaration i=2046, owner i=2041)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable DateTime readTime() throws UaException {
    return ClientMembers.await(readTimeAsync(), false);
  }

  @Override
  public void writeTime(@Nullable DateTime value) throws UaException {
    try {
      StatusCode statusCode = writeTimeAsync(value).get();
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
  public CompletableFuture<? extends @Nullable DateTime> readTimeAsync() {
    return getTimeNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:Time (declaration i=2046, owner i=2041)"
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
                return (DateTime) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeTimeAsync(@Nullable DateTime time) {
    return getTimeNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:Time (declaration i=2046, owner i=2041)"
                            + " on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(time));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getTimeNode() throws UaException {
    return ClientMembers.await(getTimeNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getTimeNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "Time",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:Time (declaration i=2046, owner i=2041)"));
  }

  @Override
  public @Nullable DateTime getReceiveTime() throws UaException {
    PropertyTypeNode node = getReceiveTimeNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ReceiveTime (declaration i=2047, owner i=2041)"
              + " on "
              + getNodeId());
    }
    return (DateTime) node.getValue().getValue().getValue();
  }

  @Override
  public void setReceiveTime(@Nullable DateTime value) throws UaException {
    PropertyTypeNode node = getReceiveTimeNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ReceiveTime (declaration i=2047, owner i=2041)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable DateTime readReceiveTime() throws UaException {
    return ClientMembers.await(readReceiveTimeAsync(), false);
  }

  @Override
  public void writeReceiveTime(@Nullable DateTime value) throws UaException {
    try {
      StatusCode statusCode = writeReceiveTimeAsync(value).get();
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
  public CompletableFuture<? extends @Nullable DateTime> readReceiveTimeAsync() {
    return getReceiveTimeNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ReceiveTime (declaration i=2047, owner"
                            + " i=2041) on "
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
  public CompletableFuture<StatusCode> writeReceiveTimeAsync(@Nullable DateTime receiveTime) {
    return getReceiveTimeNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ReceiveTime (declaration i=2047, owner"
                            + " i=2041) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(receiveTime));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getReceiveTimeNode() throws UaException {
    return ClientMembers.await(getReceiveTimeNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getReceiveTimeNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ReceiveTime",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:ReceiveTime (declaration i=2047, owner i=2041)"));
  }

  @Override
  public @Nullable TimeZoneDataType getLocalTime() throws UaException {
    PropertyTypeNode node = getLocalTimeNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:LocalTime (declaration i=3190, owner i=2041)"
              + " on "
              + getNodeId());
    }
    return (TimeZoneDataType)
        decodeValue(
            node.getValue().getValue().getValue(), TimeZoneDataType.class, ValueRanks.Scalar);
  }

  @Override
  public void setLocalTime(@Nullable TimeZoneDataType value) throws UaException {
    PropertyTypeNode node = getLocalTimeNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:LocalTime (declaration i=3190, owner i=2041)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(encodeValue(value, TimeZoneDataType.class, ValueRanks.Scalar)));
  }

  @Override
  public @Nullable TimeZoneDataType readLocalTime() throws UaException {
    return ClientMembers.await(readLocalTimeAsync(), false);
  }

  @Override
  public void writeLocalTime(@Nullable TimeZoneDataType value) throws UaException {
    try {
      StatusCode statusCode = writeLocalTimeAsync(value).get();
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
  public CompletableFuture<? extends @Nullable TimeZoneDataType> readLocalTimeAsync() {
    return getLocalTimeNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:LocalTime (declaration i=3190, owner i=2041)"
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
                return (TimeZoneDataType)
                    decodeValue(v.getValue().getValue(), TimeZoneDataType.class, ValueRanks.Scalar);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeLocalTimeAsync(@Nullable TimeZoneDataType localTime) {
    return getLocalTimeNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:LocalTime (declaration i=3190, owner i=2041)"
                            + " on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(
                            encodeValue(localTime, TimeZoneDataType.class, ValueRanks.Scalar)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getLocalTimeNode() throws UaException {
    return ClientMembers.await(getLocalTimeNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getLocalTimeNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "LocalTime",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:LocalTime (declaration i=3190, owner i=2041)"));
  }

  @Override
  public @Nullable LocalizedText getMessage() throws UaException {
    PropertyTypeNode node = getMessageNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:Message (declaration i=2050, owner i=2041)"
              + " on "
              + getNodeId());
    }
    return (LocalizedText) node.getValue().getValue().getValue();
  }

  @Override
  public void setMessage(@Nullable LocalizedText value) throws UaException {
    PropertyTypeNode node = getMessageNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:Message (declaration i=2050, owner i=2041)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable LocalizedText readMessage() throws UaException {
    return ClientMembers.await(readMessageAsync(), false);
  }

  @Override
  public void writeMessage(@Nullable LocalizedText value) throws UaException {
    try {
      StatusCode statusCode = writeMessageAsync(value).get();
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
  public CompletableFuture<? extends @Nullable LocalizedText> readMessageAsync() {
    return getMessageNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:Message (declaration i=2050, owner i=2041)"
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
                return (LocalizedText) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeMessageAsync(@Nullable LocalizedText message) {
    return getMessageNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:Message (declaration i=2050, owner i=2041)"
                            + " on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(message));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getMessageNode() throws UaException {
    return ClientMembers.await(getMessageNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMessageNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "Message",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:Message (declaration i=2050, owner i=2041)"));
  }

  @Override
  public @Nullable UShort getSeverity() throws UaException {
    PropertyTypeNode node = getSeverityNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:Severity (declaration i=2051, owner i=2041)"
              + " on "
              + getNodeId());
    }
    return (UShort) node.getValue().getValue().getValue();
  }

  @Override
  public void setSeverity(@Nullable UShort value) throws UaException {
    PropertyTypeNode node = getSeverityNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:Severity (declaration i=2051, owner i=2041)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UShort readSeverity() throws UaException {
    return ClientMembers.await(readSeverityAsync(), false);
  }

  @Override
  public void writeSeverity(@Nullable UShort value) throws UaException {
    try {
      StatusCode statusCode = writeSeverityAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UShort> readSeverityAsync() {
    return getSeverityNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:Severity (declaration i=2051, owner i=2041)"
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
                return (UShort) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeSeverityAsync(@Nullable UShort severity) {
    return getSeverityNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:Severity (declaration i=2051, owner i=2041)"
                            + " on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(severity));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getSeverityNode() throws UaException {
    return ClientMembers.await(getSeverityNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getSeverityNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "Severity",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:Severity (declaration i=2051, owner i=2041)"));
  }

  @Override
  public @Nullable NodeId getConditionClassId() throws UaException {
    PropertyTypeNode node = getConditionClassIdNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ConditionClassId (declaration i=31771, owner i=2041)"
              + " on "
              + getNodeId());
    }
    return (NodeId) node.getValue().getValue().getValue();
  }

  @Override
  public void setConditionClassId(@Nullable NodeId value) throws UaException {
    PropertyTypeNode node = getConditionClassIdNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ConditionClassId (declaration i=31771, owner i=2041)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable NodeId readConditionClassId() throws UaException {
    return ClientMembers.await(readConditionClassIdAsync(), false);
  }

  @Override
  public void writeConditionClassId(@Nullable NodeId value) throws UaException {
    try {
      StatusCode statusCode = writeConditionClassIdAsync(value).get();
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
  public CompletableFuture<? extends @Nullable NodeId> readConditionClassIdAsync() {
    return getConditionClassIdNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ConditionClassId (declaration i=31771, owner"
                            + " i=2041) on "
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
  public CompletableFuture<StatusCode> writeConditionClassIdAsync(
      @Nullable NodeId conditionClassId) {
    return getConditionClassIdNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ConditionClassId (declaration i=31771, owner"
                            + " i=2041) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(conditionClassId));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getConditionClassIdNode() throws UaException {
    return ClientMembers.await(getConditionClassIdNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getConditionClassIdNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ConditionClassId",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:ConditionClassId (declaration i=31771, owner i=2041)"));
  }

  @Override
  public @Nullable LocalizedText getConditionClassName() throws UaException {
    PropertyTypeNode node = getConditionClassNameNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ConditionClassName (declaration i=31772, owner i=2041)"
              + " on "
              + getNodeId());
    }
    return (LocalizedText) node.getValue().getValue().getValue();
  }

  @Override
  public void setConditionClassName(@Nullable LocalizedText value) throws UaException {
    PropertyTypeNode node = getConditionClassNameNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ConditionClassName (declaration i=31772, owner i=2041)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable LocalizedText readConditionClassName() throws UaException {
    return ClientMembers.await(readConditionClassNameAsync(), false);
  }

  @Override
  public void writeConditionClassName(@Nullable LocalizedText value) throws UaException {
    try {
      StatusCode statusCode = writeConditionClassNameAsync(value).get();
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
  public CompletableFuture<? extends @Nullable LocalizedText> readConditionClassNameAsync() {
    return getConditionClassNameNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ConditionClassName (declaration i=31772,"
                            + " owner i=2041) on "
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
                return (LocalizedText) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeConditionClassNameAsync(
      @Nullable LocalizedText conditionClassName) {
    return getConditionClassNameNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ConditionClassName (declaration i=31772,"
                            + " owner i=2041) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(conditionClassName));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getConditionClassNameNode() throws UaException {
    return ClientMembers.await(getConditionClassNameNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getConditionClassNameNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ConditionClassName",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:ConditionClassName (declaration i=31772, owner i=2041)"));
  }

  @Override
  public @Nullable NodeId @Nullable [] getConditionSubClassId() throws UaException {
    PropertyTypeNode node = getConditionSubClassIdNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ConditionSubClassId (declaration i=31773, owner i=2041)"
              + " on "
              + getNodeId());
    }
    return (NodeId[]) node.getValue().getValue().getValue();
  }

  @Override
  public void setConditionSubClassId(@Nullable NodeId @Nullable [] value) throws UaException {
    PropertyTypeNode node = getConditionSubClassIdNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ConditionSubClassId (declaration i=31773, owner i=2041)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable NodeId @Nullable [] readConditionSubClassId() throws UaException {
    return ClientMembers.await(readConditionSubClassIdAsync(), false);
  }

  @Override
  public void writeConditionSubClassId(@Nullable NodeId @Nullable [] value) throws UaException {
    try {
      StatusCode statusCode = writeConditionSubClassIdAsync(value).get();
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
  public CompletableFuture<? extends @Nullable NodeId @Nullable []> readConditionSubClassIdAsync() {
    return getConditionSubClassIdNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ConditionSubClassId (declaration i=31773,"
                            + " owner i=2041) on "
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
                return (NodeId[]) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeConditionSubClassIdAsync(
      @Nullable NodeId @Nullable [] conditionSubClassId) {
    return getConditionSubClassIdNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ConditionSubClassId (declaration i=31773,"
                            + " owner i=2041) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(conditionSubClassId));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getConditionSubClassIdNode() throws UaException {
    return ClientMembers.await(getConditionSubClassIdNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getConditionSubClassIdNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ConditionSubClassId",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:ConditionSubClassId (declaration i=31773, owner"
                + " i=2041)"));
  }

  @Override
  public @Nullable LocalizedText @Nullable [] getConditionSubClassName() throws UaException {
    PropertyTypeNode node = getConditionSubClassNameNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ConditionSubClassName (declaration i=31774, owner i=2041)"
              + " on "
              + getNodeId());
    }
    return (LocalizedText[]) node.getValue().getValue().getValue();
  }

  @Override
  public void setConditionSubClassName(@Nullable LocalizedText @Nullable [] value)
      throws UaException {
    PropertyTypeNode node = getConditionSubClassNameNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ConditionSubClassName (declaration i=31774, owner i=2041)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable LocalizedText @Nullable [] readConditionSubClassName() throws UaException {
    return ClientMembers.await(readConditionSubClassNameAsync(), false);
  }

  @Override
  public void writeConditionSubClassName(@Nullable LocalizedText @Nullable [] value)
      throws UaException {
    try {
      StatusCode statusCode = writeConditionSubClassNameAsync(value).get();
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
  public CompletableFuture<? extends @Nullable LocalizedText @Nullable []>
      readConditionSubClassNameAsync() {
    return getConditionSubClassNameNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ConditionSubClassName (declaration i=31774,"
                            + " owner i=2041) on "
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
                return (LocalizedText[]) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeConditionSubClassNameAsync(
      @Nullable LocalizedText @Nullable [] conditionSubClassName) {
    return getConditionSubClassNameNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ConditionSubClassName (declaration i=31774,"
                            + " owner i=2041) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(conditionSubClassName));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getConditionSubClassNameNode() throws UaException {
    return ClientMembers.await(getConditionSubClassNameNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getConditionSubClassNameNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ConditionSubClassName",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:ConditionSubClassName (declaration i=31774, owner"
                + " i=2041)"));
  }
}

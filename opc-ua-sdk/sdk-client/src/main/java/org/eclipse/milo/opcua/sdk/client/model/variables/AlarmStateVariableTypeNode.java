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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessLevelExType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.jspecify.annotations.Nullable;

public class AlarmStateVariableTypeNode extends BaseDataVariableTypeNode
    implements AlarmStateVariableType {
  public AlarmStateVariableTypeNode(
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
  public @Nullable UShort getHighestActiveSeverity() throws UaException {
    PropertyTypeNode node = getHighestActiveSeverityNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:HighestActiveSeverity (declaration i=32245, owner i=32244)"
              + " on "
              + getNodeId());
    }
    return (UShort) node.getValue().getValue().getValue();
  }

  @Override
  public void setHighestActiveSeverity(@Nullable UShort value) throws UaException {
    PropertyTypeNode node = getHighestActiveSeverityNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:HighestActiveSeverity (declaration i=32245, owner i=32244)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UShort readHighestActiveSeverity() throws UaException {
    return ClientMembers.await(readHighestActiveSeverityAsync(), false);
  }

  @Override
  public void writeHighestActiveSeverity(@Nullable UShort value) throws UaException {
    try {
      StatusCode statusCode = writeHighestActiveSeverityAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UShort> readHighestActiveSeverityAsync() {
    return getHighestActiveSeverityNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:HighestActiveSeverity (declaration i=32245,"
                            + " owner i=32244) on "
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
  public CompletableFuture<StatusCode> writeHighestActiveSeverityAsync(
      @Nullable UShort highestActiveSeverity) {
    return getHighestActiveSeverityNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:HighestActiveSeverity (declaration i=32245,"
                            + " owner i=32244) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(highestActiveSeverity));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getHighestActiveSeverityNode() throws UaException {
    return ClientMembers.await(getHighestActiveSeverityNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getHighestActiveSeverityNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "HighestActiveSeverity",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:HighestActiveSeverity (declaration i=32245, owner"
                + " i=32244)"));
  }

  @Override
  public @Nullable UShort getHighestUnackSeverity() throws UaException {
    PropertyTypeNode node = getHighestUnackSeverityNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:HighestUnackSeverity (declaration i=32246, owner i=32244)"
              + " on "
              + getNodeId());
    }
    return (UShort) node.getValue().getValue().getValue();
  }

  @Override
  public void setHighestUnackSeverity(@Nullable UShort value) throws UaException {
    PropertyTypeNode node = getHighestUnackSeverityNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:HighestUnackSeverity (declaration i=32246, owner i=32244)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UShort readHighestUnackSeverity() throws UaException {
    return ClientMembers.await(readHighestUnackSeverityAsync(), false);
  }

  @Override
  public void writeHighestUnackSeverity(@Nullable UShort value) throws UaException {
    try {
      StatusCode statusCode = writeHighestUnackSeverityAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UShort> readHighestUnackSeverityAsync() {
    return getHighestUnackSeverityNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:HighestUnackSeverity (declaration i=32246,"
                            + " owner i=32244) on "
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
  public CompletableFuture<StatusCode> writeHighestUnackSeverityAsync(
      @Nullable UShort highestUnackSeverity) {
    return getHighestUnackSeverityNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:HighestUnackSeverity (declaration i=32246,"
                            + " owner i=32244) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(highestUnackSeverity));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getHighestUnackSeverityNode() throws UaException {
    return ClientMembers.await(getHighestUnackSeverityNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getHighestUnackSeverityNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "HighestUnackSeverity",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:HighestUnackSeverity (declaration i=32246, owner"
                + " i=32244)"));
  }

  @Override
  public @Nullable UInteger getActiveCount() throws UaException {
    PropertyTypeNode node = getActiveCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ActiveCount (declaration i=32247, owner i=32244)"
              + " on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setActiveCount(@Nullable UInteger value) throws UaException {
    PropertyTypeNode node = getActiveCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ActiveCount (declaration i=32247, owner i=32244)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UInteger readActiveCount() throws UaException {
    return ClientMembers.await(readActiveCountAsync(), false);
  }

  @Override
  public void writeActiveCount(@Nullable UInteger value) throws UaException {
    try {
      StatusCode statusCode = writeActiveCountAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UInteger> readActiveCountAsync() {
    return getActiveCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ActiveCount (declaration i=32247, owner"
                            + " i=32244) on "
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
  public CompletableFuture<StatusCode> writeActiveCountAsync(@Nullable UInteger activeCount) {
    return getActiveCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ActiveCount (declaration i=32247, owner"
                            + " i=32244) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(activeCount));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getActiveCountNode() throws UaException {
    return ClientMembers.await(getActiveCountNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getActiveCountNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ActiveCount",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:ActiveCount (declaration i=32247, owner i=32244)"));
  }

  @Override
  public @Nullable UInteger getUnacknowledgedCount() throws UaException {
    PropertyTypeNode node = getUnacknowledgedCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:UnacknowledgedCount (declaration i=32248, owner i=32244)"
              + " on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setUnacknowledgedCount(@Nullable UInteger value) throws UaException {
    PropertyTypeNode node = getUnacknowledgedCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:UnacknowledgedCount (declaration i=32248, owner i=32244)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UInteger readUnacknowledgedCount() throws UaException {
    return ClientMembers.await(readUnacknowledgedCountAsync(), false);
  }

  @Override
  public void writeUnacknowledgedCount(@Nullable UInteger value) throws UaException {
    try {
      StatusCode statusCode = writeUnacknowledgedCountAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UInteger> readUnacknowledgedCountAsync() {
    return getUnacknowledgedCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:UnacknowledgedCount (declaration i=32248,"
                            + " owner i=32244) on "
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
  public CompletableFuture<StatusCode> writeUnacknowledgedCountAsync(
      @Nullable UInteger unacknowledgedCount) {
    return getUnacknowledgedCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:UnacknowledgedCount (declaration i=32248,"
                            + " owner i=32244) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(unacknowledgedCount));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getUnacknowledgedCountNode() throws UaException {
    return ClientMembers.await(getUnacknowledgedCountNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getUnacknowledgedCountNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "UnacknowledgedCount",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:UnacknowledgedCount (declaration i=32248, owner"
                + " i=32244)"));
  }

  @Override
  public @Nullable UInteger getUnconfirmedCount() throws UaException {
    PropertyTypeNode node = getUnconfirmedCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:UnconfirmedCount (declaration i=32249, owner i=32244)"
              + " on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setUnconfirmedCount(@Nullable UInteger value) throws UaException {
    PropertyTypeNode node = getUnconfirmedCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:UnconfirmedCount (declaration i=32249, owner i=32244)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UInteger readUnconfirmedCount() throws UaException {
    return ClientMembers.await(readUnconfirmedCountAsync(), false);
  }

  @Override
  public void writeUnconfirmedCount(@Nullable UInteger value) throws UaException {
    try {
      StatusCode statusCode = writeUnconfirmedCountAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UInteger> readUnconfirmedCountAsync() {
    return getUnconfirmedCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:UnconfirmedCount (declaration i=32249, owner"
                            + " i=32244) on "
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
  public CompletableFuture<StatusCode> writeUnconfirmedCountAsync(
      @Nullable UInteger unconfirmedCount) {
    return getUnconfirmedCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:UnconfirmedCount (declaration i=32249, owner"
                            + " i=32244) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(unconfirmedCount));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getUnconfirmedCountNode() throws UaException {
    return ClientMembers.await(getUnconfirmedCountNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getUnconfirmedCountNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "UnconfirmedCount",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:UnconfirmedCount (declaration i=32249, owner i=32244)"));
  }

  @Override
  public @Nullable ContentFilter getFilter() throws UaException {
    PropertyTypeNode node = getFilterNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:Filter (declaration i=32250, owner i=32244)"
              + " on "
              + getNodeId());
    }
    return (ContentFilter)
        decodeValue(node.getValue().getValue().getValue(), ContentFilter.class, ValueRanks.Scalar);
  }

  @Override
  public void setFilter(@Nullable ContentFilter value) throws UaException {
    PropertyTypeNode node = getFilterNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:Filter (declaration i=32250, owner i=32244)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(encodeValue(value, ContentFilter.class, ValueRanks.Scalar)));
  }

  @Override
  public @Nullable ContentFilter readFilter() throws UaException {
    return ClientMembers.await(readFilterAsync(), false);
  }

  @Override
  public void writeFilter(@Nullable ContentFilter value) throws UaException {
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
  public CompletableFuture<? extends @Nullable ContentFilter> readFilterAsync() {
    return getFilterNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:Filter (declaration i=32250, owner i=32244)"
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
                return (ContentFilter)
                    decodeValue(v.getValue().getValue(), ContentFilter.class, ValueRanks.Scalar);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeFilterAsync(@Nullable ContentFilter filter) {
    return getFilterNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:Filter (declaration i=32250, owner i=32244)"
                            + " on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(encodeValue(filter, ContentFilter.class, ValueRanks.Scalar)));
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
            "http://opcfoundation.org/UA/:Filter (declaration i=32250, owner i=32244)"));
  }
}

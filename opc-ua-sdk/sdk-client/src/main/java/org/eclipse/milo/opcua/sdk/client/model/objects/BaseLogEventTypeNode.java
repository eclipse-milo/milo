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
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.TraceContextDataType;
import org.jspecify.annotations.Nullable;

public class BaseLogEventTypeNode extends BaseEventTypeNode implements BaseLogEventType {
  public BaseLogEventTypeNode(
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
  public static ClientViews createViews(BaseLogEventTypeNode node) {
    Objects.requireNonNull(node, "node");
    return ClientViews.forNode(node.client, node);
  }

  @Override
  public @Nullable NodeId getConditionClassId() throws UaException {
    PropertyTypeNode node = getConditionClassIdNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ConditionClassId (declaration i=19363, owner i=19362)"
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
          "http://opcfoundation.org/UA/:ConditionClassId (declaration i=19363, owner i=19362)"
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
                        "http://opcfoundation.org/UA/:ConditionClassId (declaration i=19363, owner"
                            + " i=19362) on "
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
                        "http://opcfoundation.org/UA/:ConditionClassId (declaration i=19363, owner"
                            + " i=19362) on "
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
  public PropertyTypeNode getConditionClassIdNode() throws UaException {
    return ClientMembers.await(getConditionClassIdNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getConditionClassIdNodeAsync() {
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
            false,
            "http://opcfoundation.org/UA/:ConditionClassId (declaration i=19363, owner i=19362)"));
  }

  @Override
  public @Nullable LocalizedText getConditionClassName() throws UaException {
    PropertyTypeNode node = getConditionClassNameNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ConditionClassName (declaration i=19364, owner i=19362)"
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
          "http://opcfoundation.org/UA/:ConditionClassName (declaration i=19364, owner i=19362)"
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
                        "http://opcfoundation.org/UA/:ConditionClassName (declaration i=19364,"
                            + " owner i=19362) on "
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
                        "http://opcfoundation.org/UA/:ConditionClassName (declaration i=19364,"
                            + " owner i=19362) on "
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
  public PropertyTypeNode getConditionClassNameNode() throws UaException {
    return ClientMembers.await(getConditionClassNameNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getConditionClassNameNodeAsync() {
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
            false,
            "http://opcfoundation.org/UA/:ConditionClassName (declaration i=19364, owner"
                + " i=19362)"));
  }

  @Override
  public @Nullable StatusCode getErrorCode() throws UaException {
    PropertyTypeNode node = getErrorCodePropertyNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ErrorCode (declaration i=19365, owner i=19362)"
              + " on "
              + getNodeId());
    }
    return (StatusCode) node.getValue().getValue().getValue();
  }

  @Override
  public void setErrorCode(@Nullable StatusCode value) throws UaException {
    PropertyTypeNode node = getErrorCodePropertyNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ErrorCode (declaration i=19365, owner i=19362)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable StatusCode readErrorCode() throws UaException {
    return ClientMembers.await(readErrorCodeAsync(), false);
  }

  @Override
  public void writeErrorCode(@Nullable StatusCode value) throws UaException {
    try {
      StatusCode statusCode = writeErrorCodeAsync(value).get();
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
  public CompletableFuture<? extends @Nullable StatusCode> readErrorCodeAsync() {
    return getErrorCodePropertyNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ErrorCode (declaration i=19365, owner"
                            + " i=19362) on "
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
                return (StatusCode) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeErrorCodeAsync(@Nullable StatusCode errorCode) {
    return getErrorCodePropertyNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ErrorCode (declaration i=19365, owner"
                            + " i=19362) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(errorCode));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getErrorCodePropertyNode() throws UaException {
    return ClientMembers.await(getErrorCodePropertyNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getErrorCodePropertyNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ErrorCode",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:ErrorCode (declaration i=19365, owner i=19362)"));
  }

  @Override
  public @Nullable NodeId getErrorCodeNode() throws UaException {
    PropertyTypeNode node = getErrorCodeNodeNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ErrorCodeNode (declaration i=19366, owner i=19362)"
              + " on "
              + getNodeId());
    }
    return (NodeId) node.getValue().getValue().getValue();
  }

  @Override
  public void setErrorCodeNode(@Nullable NodeId value) throws UaException {
    PropertyTypeNode node = getErrorCodeNodeNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ErrorCodeNode (declaration i=19366, owner i=19362)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable NodeId readErrorCodeNode() throws UaException {
    return ClientMembers.await(readErrorCodeNodeAsync(), false);
  }

  @Override
  public void writeErrorCodeNode(@Nullable NodeId value) throws UaException {
    try {
      StatusCode statusCode = writeErrorCodeNodeAsync(value).get();
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
  public CompletableFuture<? extends @Nullable NodeId> readErrorCodeNodeAsync() {
    return getErrorCodeNodeNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ErrorCodeNode (declaration i=19366, owner"
                            + " i=19362) on "
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
  public CompletableFuture<StatusCode> writeErrorCodeNodeAsync(@Nullable NodeId errorCodeNode) {
    return getErrorCodeNodeNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ErrorCodeNode (declaration i=19366, owner"
                            + " i=19362) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(errorCodeNode));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getErrorCodeNodeNode() throws UaException {
    return ClientMembers.await(getErrorCodeNodeNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getErrorCodeNodeNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ErrorCodeNode",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:ErrorCodeNode (declaration i=19366, owner i=19362)"));
  }

  @Override
  public @Nullable TraceContextDataType getTraceContext() throws UaException {
    PropertyTypeNode node = getTraceContextNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:TraceContext (declaration i=24376, owner i=19362)"
              + " on "
              + getNodeId());
    }
    return (TraceContextDataType)
        decodeValue(
            node.getValue().getValue().getValue(), TraceContextDataType.class, ValueRanks.Scalar);
  }

  @Override
  public void setTraceContext(@Nullable TraceContextDataType value) throws UaException {
    PropertyTypeNode node = getTraceContextNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:TraceContext (declaration i=24376, owner i=19362)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(encodeValue(value, TraceContextDataType.class, ValueRanks.Scalar)));
  }

  @Override
  public @Nullable TraceContextDataType readTraceContext() throws UaException {
    return ClientMembers.await(readTraceContextAsync(), false);
  }

  @Override
  public void writeTraceContext(@Nullable TraceContextDataType value) throws UaException {
    try {
      StatusCode statusCode = writeTraceContextAsync(value).get();
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
  public CompletableFuture<? extends @Nullable TraceContextDataType> readTraceContextAsync() {
    return getTraceContextNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:TraceContext (declaration i=24376, owner"
                            + " i=19362) on "
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
                return (TraceContextDataType)
                    decodeValue(
                        v.getValue().getValue(), TraceContextDataType.class, ValueRanks.Scalar);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeTraceContextAsync(
      @Nullable TraceContextDataType traceContext) {
    return getTraceContextNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:TraceContext (declaration i=24376, owner"
                            + " i=19362) on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(
                            encodeValue(
                                traceContext, TraceContextDataType.class, ValueRanks.Scalar)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getTraceContextNode() throws UaException {
    return ClientMembers.await(getTraceContextNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getTraceContextNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "TraceContext",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:TraceContext (declaration i=24376, owner i=19362)"));
  }
}

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

public class AuditClientUpdateMethodResultEventTypeNode extends AuditClientEventTypeNode
    implements AuditClientUpdateMethodResultEventType {
  public AuditClientUpdateMethodResultEventTypeNode(
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
  public static ClientViews createViews(AuditClientUpdateMethodResultEventTypeNode node) {
    Objects.requireNonNull(node, "node");
    return ClientViews.forNode(node.client, node);
  }

  @Override
  public @Nullable ExpandedNodeId getObjectId() throws UaException {
    PropertyTypeNode node = getObjectIdNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ObjectId (declaration i=23994, owner i=23926)"
              + " on "
              + getNodeId());
    }
    return (ExpandedNodeId) node.getValue().getValue().getValue();
  }

  @Override
  public void setObjectId(@Nullable ExpandedNodeId value) throws UaException {
    PropertyTypeNode node = getObjectIdNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ObjectId (declaration i=23994, owner i=23926)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable ExpandedNodeId readObjectId() throws UaException {
    return ClientMembers.await(readObjectIdAsync(), false);
  }

  @Override
  public void writeObjectId(@Nullable ExpandedNodeId value) throws UaException {
    try {
      StatusCode statusCode = writeObjectIdAsync(value).get();
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
  public CompletableFuture<? extends @Nullable ExpandedNodeId> readObjectIdAsync() {
    return getObjectIdNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ObjectId (declaration i=23994, owner i=23926)"
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
                return (ExpandedNodeId) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeObjectIdAsync(@Nullable ExpandedNodeId objectId) {
    return getObjectIdNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ObjectId (declaration i=23994, owner i=23926)"
                            + " on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(objectId));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getObjectIdNode() throws UaException {
    return ClientMembers.await(getObjectIdNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getObjectIdNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ObjectId",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:ObjectId (declaration i=23994, owner i=23926)"));
  }

  @Override
  public @Nullable ExpandedNodeId getMethodId() throws UaException {
    PropertyTypeNode node = getMethodIdNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MethodId (declaration i=23995, owner i=23926)"
              + " on "
              + getNodeId());
    }
    return (ExpandedNodeId) node.getValue().getValue().getValue();
  }

  @Override
  public void setMethodId(@Nullable ExpandedNodeId value) throws UaException {
    PropertyTypeNode node = getMethodIdNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MethodId (declaration i=23995, owner i=23926)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable ExpandedNodeId readMethodId() throws UaException {
    return ClientMembers.await(readMethodIdAsync(), false);
  }

  @Override
  public void writeMethodId(@Nullable ExpandedNodeId value) throws UaException {
    try {
      StatusCode statusCode = writeMethodIdAsync(value).get();
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
  public CompletableFuture<? extends @Nullable ExpandedNodeId> readMethodIdAsync() {
    return getMethodIdNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MethodId (declaration i=23995, owner i=23926)"
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
                return (ExpandedNodeId) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeMethodIdAsync(@Nullable ExpandedNodeId methodId) {
    return getMethodIdNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MethodId (declaration i=23995, owner i=23926)"
                            + " on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(methodId));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getMethodIdNode() throws UaException {
    return ClientMembers.await(getMethodIdNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMethodIdNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MethodId",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:MethodId (declaration i=23995, owner i=23926)"));
  }

  @Override
  public @Nullable StatusCode getStatusCodeId() throws UaException {
    PropertyTypeNode node = getStatusCodeIdNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:StatusCodeId (declaration i=23998, owner i=23926)"
              + " on "
              + getNodeId());
    }
    return (StatusCode) node.getValue().getValue().getValue();
  }

  @Override
  public void setStatusCodeId(@Nullable StatusCode value) throws UaException {
    PropertyTypeNode node = getStatusCodeIdNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:StatusCodeId (declaration i=23998, owner i=23926)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable StatusCode readStatusCodeId() throws UaException {
    return ClientMembers.await(readStatusCodeIdAsync(), false);
  }

  @Override
  public void writeStatusCodeId(@Nullable StatusCode value) throws UaException {
    try {
      StatusCode statusCode = writeStatusCodeIdAsync(value).get();
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
  public CompletableFuture<? extends @Nullable StatusCode> readStatusCodeIdAsync() {
    return getStatusCodeIdNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:StatusCodeId (declaration i=23998, owner"
                            + " i=23926) on "
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
  public CompletableFuture<StatusCode> writeStatusCodeIdAsync(@Nullable StatusCode statusCodeId) {
    return getStatusCodeIdNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:StatusCodeId (declaration i=23998, owner"
                            + " i=23926) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(statusCodeId));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getStatusCodeIdNode() throws UaException {
    return ClientMembers.await(getStatusCodeIdNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getStatusCodeIdNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "StatusCodeId",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:StatusCodeId (declaration i=23998, owner i=23926)"));
  }

  @Override
  public @Nullable Object @Nullable [] getInputArguments() throws UaException {
    PropertyTypeNode node = getInputArgumentsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:InputArguments (declaration i=23999, owner i=23926)"
              + " on "
              + getNodeId());
    }
    return (Object[]) node.getValue().getValue().getValue();
  }

  @Override
  public void setInputArguments(@Nullable Object @Nullable [] value) throws UaException {
    PropertyTypeNode node = getInputArgumentsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:InputArguments (declaration i=23999, owner i=23926)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable Object @Nullable [] readInputArguments() throws UaException {
    return ClientMembers.await(readInputArgumentsAsync(), false);
  }

  @Override
  public void writeInputArguments(@Nullable Object @Nullable [] value) throws UaException {
    try {
      StatusCode statusCode = writeInputArgumentsAsync(value).get();
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
  public CompletableFuture<? extends @Nullable Object @Nullable []> readInputArgumentsAsync() {
    return getInputArgumentsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:InputArguments (declaration i=23999, owner"
                            + " i=23926) on "
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
                return (Object[]) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeInputArgumentsAsync(
      @Nullable Object @Nullable [] inputArguments) {
    return getInputArgumentsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:InputArguments (declaration i=23999, owner"
                            + " i=23926) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(inputArguments));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getInputArgumentsNode() throws UaException {
    return ClientMembers.await(getInputArgumentsNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getInputArgumentsNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "InputArguments",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:InputArguments (declaration i=23999, owner i=23926)"));
  }

  @Override
  public @Nullable Object @Nullable [] getOutputArguments() throws UaException {
    PropertyTypeNode node = getOutputArgumentsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:OutputArguments (declaration i=25684, owner i=23926)"
              + " on "
              + getNodeId());
    }
    return (Object[]) node.getValue().getValue().getValue();
  }

  @Override
  public void setOutputArguments(@Nullable Object @Nullable [] value) throws UaException {
    PropertyTypeNode node = getOutputArgumentsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:OutputArguments (declaration i=25684, owner i=23926)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable Object @Nullable [] readOutputArguments() throws UaException {
    return ClientMembers.await(readOutputArgumentsAsync(), false);
  }

  @Override
  public void writeOutputArguments(@Nullable Object @Nullable [] value) throws UaException {
    try {
      StatusCode statusCode = writeOutputArgumentsAsync(value).get();
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
  public CompletableFuture<? extends @Nullable Object @Nullable []> readOutputArgumentsAsync() {
    return getOutputArgumentsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:OutputArguments (declaration i=25684, owner"
                            + " i=23926) on "
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
                return (Object[]) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeOutputArgumentsAsync(
      @Nullable Object @Nullable [] outputArguments) {
    return getOutputArgumentsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:OutputArguments (declaration i=25684, owner"
                            + " i=23926) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(outputArguments));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getOutputArgumentsNode() throws UaException {
    return ClientMembers.await(getOutputArgumentsNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getOutputArgumentsNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "OutputArguments",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:OutputArguments (declaration i=25684, owner i=23926)"));
  }
}

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
import org.eclipse.milo.opcua.sdk.client.model.variables.FiniteStateVariableTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.FiniteTransitionVariableTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.ProgramDiagnostic2TypeNode;
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
import org.eclipse.milo.opcua.stack.core.types.structured.ProgramDiagnostic2DataType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.jspecify.annotations.Nullable;

public class ProgramStateMachineTypeNode extends FiniteStateMachineTypeNode
    implements ProgramStateMachineType {
  public ProgramStateMachineTypeNode(
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
  public static ClientViews createViews(ProgramStateMachineTypeNode node) {
    Objects.requireNonNull(node, "node");
    return ClientViews.forNode(node.client, node);
  }

  @Override
  public @Nullable Boolean getCreatable() throws UaException {
    PropertyTypeNode node = getCreatableNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:Creatable (declaration i=2392, owner i=2391)"
              + " on "
              + getNodeId());
    }
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setCreatable(@Nullable Boolean value) throws UaException {
    PropertyTypeNode node = getCreatableNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:Creatable (declaration i=2392, owner i=2391)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable Boolean readCreatable() throws UaException {
    return ClientMembers.await(readCreatableAsync(), false);
  }

  @Override
  public void writeCreatable(@Nullable Boolean value) throws UaException {
    try {
      StatusCode statusCode = writeCreatableAsync(value).get();
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
  public CompletableFuture<? extends @Nullable Boolean> readCreatableAsync() {
    return getCreatableNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:Creatable (declaration i=2392, owner i=2391)"
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
                return (Boolean) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeCreatableAsync(@Nullable Boolean creatable) {
    return getCreatableNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:Creatable (declaration i=2392, owner i=2391)"
                            + " on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(creatable));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getCreatableNode() throws UaException {
    return ClientMembers.await(getCreatableNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getCreatableNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "Creatable",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:Creatable (declaration i=2392, owner i=2391)"));
  }

  @Override
  public @Nullable Boolean getDeletable() throws UaException {
    PropertyTypeNode node = getDeletableNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:Deletable (declaration i=2393, owner i=2391)"
              + " on "
              + getNodeId());
    }
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setDeletable(@Nullable Boolean value) throws UaException {
    PropertyTypeNode node = getDeletableNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:Deletable (declaration i=2393, owner i=2391)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable Boolean readDeletable() throws UaException {
    return ClientMembers.await(readDeletableAsync(), false);
  }

  @Override
  public void writeDeletable(@Nullable Boolean value) throws UaException {
    try {
      StatusCode statusCode = writeDeletableAsync(value).get();
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
  public CompletableFuture<? extends @Nullable Boolean> readDeletableAsync() {
    return getDeletableNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:Deletable (declaration i=2393, owner i=2391)"
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
                return (Boolean) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeDeletableAsync(@Nullable Boolean deletable) {
    return getDeletableNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:Deletable (declaration i=2393, owner i=2391)"
                            + " on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(deletable));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getDeletableNode() throws UaException {
    return ClientMembers.await(getDeletableNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getDeletableNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "Deletable",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:Deletable (declaration i=2393, owner i=2391)"));
  }

  @Override
  public @Nullable Boolean getAutoDelete() throws UaException {
    PropertyTypeNode node = getAutoDeleteNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:AutoDelete (declaration i=2394, owner i=2391)"
              + " on "
              + getNodeId());
    }
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setAutoDelete(@Nullable Boolean value) throws UaException {
    PropertyTypeNode node = getAutoDeleteNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:AutoDelete (declaration i=2394, owner i=2391)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable Boolean readAutoDelete() throws UaException {
    return ClientMembers.await(readAutoDeleteAsync(), false);
  }

  @Override
  public void writeAutoDelete(@Nullable Boolean value) throws UaException {
    try {
      StatusCode statusCode = writeAutoDeleteAsync(value).get();
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
  public CompletableFuture<? extends @Nullable Boolean> readAutoDeleteAsync() {
    return getAutoDeleteNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:AutoDelete (declaration i=2394, owner i=2391)"
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
                return (Boolean) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeAutoDeleteAsync(@Nullable Boolean autoDelete) {
    return getAutoDeleteNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:AutoDelete (declaration i=2394, owner i=2391)"
                            + " on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(autoDelete));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getAutoDeleteNode() throws UaException {
    return ClientMembers.await(getAutoDeleteNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getAutoDeleteNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "AutoDelete",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:AutoDelete (declaration i=2394, owner i=2391)"));
  }

  @Override
  public @Nullable Integer getRecycleCount() throws UaException {
    PropertyTypeNode node = getRecycleCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:RecycleCount (declaration i=2395, owner i=2391)"
              + " on "
              + getNodeId());
    }
    return (Integer) node.getValue().getValue().getValue();
  }

  @Override
  public void setRecycleCount(@Nullable Integer value) throws UaException {
    PropertyTypeNode node = getRecycleCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:RecycleCount (declaration i=2395, owner i=2391)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable Integer readRecycleCount() throws UaException {
    return ClientMembers.await(readRecycleCountAsync(), false);
  }

  @Override
  public void writeRecycleCount(@Nullable Integer value) throws UaException {
    try {
      StatusCode statusCode = writeRecycleCountAsync(value).get();
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
  public CompletableFuture<? extends @Nullable Integer> readRecycleCountAsync() {
    return getRecycleCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:RecycleCount (declaration i=2395, owner"
                            + " i=2391) on "
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
                return (Integer) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeRecycleCountAsync(@Nullable Integer recycleCount) {
    return getRecycleCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:RecycleCount (declaration i=2395, owner"
                            + " i=2391) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(recycleCount));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getRecycleCountNode() throws UaException {
    return ClientMembers.await(getRecycleCountNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getRecycleCountNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "RecycleCount",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:RecycleCount (declaration i=2395, owner i=2391)"));
  }

  @Override
  public @Nullable UInteger getInstanceCount() throws UaException {
    PropertyTypeNode node = getInstanceCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:InstanceCount (declaration i=2396, owner i=2391)"
              + " on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setInstanceCount(@Nullable UInteger value) throws UaException {
    PropertyTypeNode node = getInstanceCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:InstanceCount (declaration i=2396, owner i=2391)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UInteger readInstanceCount() throws UaException {
    return ClientMembers.await(readInstanceCountAsync(), false);
  }

  @Override
  public void writeInstanceCount(@Nullable UInteger value) throws UaException {
    try {
      StatusCode statusCode = writeInstanceCountAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UInteger> readInstanceCountAsync() {
    return getInstanceCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:InstanceCount (declaration i=2396, owner"
                            + " i=2391) on "
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
  public CompletableFuture<StatusCode> writeInstanceCountAsync(@Nullable UInteger instanceCount) {
    return getInstanceCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:InstanceCount (declaration i=2396, owner"
                            + " i=2391) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(instanceCount));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getInstanceCountNode() throws UaException {
    return ClientMembers.await(getInstanceCountNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getInstanceCountNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "InstanceCount",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:InstanceCount (declaration i=2396, owner i=2391)"));
  }

  @Override
  public @Nullable UInteger getMaxInstanceCount() throws UaException {
    PropertyTypeNode node = getMaxInstanceCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxInstanceCount (declaration i=2397, owner i=2391)"
              + " on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxInstanceCount(@Nullable UInteger value) throws UaException {
    PropertyTypeNode node = getMaxInstanceCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxInstanceCount (declaration i=2397, owner i=2391)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UInteger readMaxInstanceCount() throws UaException {
    return ClientMembers.await(readMaxInstanceCountAsync(), false);
  }

  @Override
  public void writeMaxInstanceCount(@Nullable UInteger value) throws UaException {
    try {
      StatusCode statusCode = writeMaxInstanceCountAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UInteger> readMaxInstanceCountAsync() {
    return getMaxInstanceCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxInstanceCount (declaration i=2397, owner"
                            + " i=2391) on "
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
  public CompletableFuture<StatusCode> writeMaxInstanceCountAsync(
      @Nullable UInteger maxInstanceCount) {
    return getMaxInstanceCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxInstanceCount (declaration i=2397, owner"
                            + " i=2391) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(maxInstanceCount));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getMaxInstanceCountNode() throws UaException {
    return ClientMembers.await(getMaxInstanceCountNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMaxInstanceCountNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxInstanceCount",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:MaxInstanceCount (declaration i=2397, owner i=2391)"));
  }

  @Override
  public @Nullable UInteger getMaxRecycleCount() throws UaException {
    PropertyTypeNode node = getMaxRecycleCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxRecycleCount (declaration i=2398, owner i=2391)"
              + " on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxRecycleCount(@Nullable UInteger value) throws UaException {
    PropertyTypeNode node = getMaxRecycleCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxRecycleCount (declaration i=2398, owner i=2391)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UInteger readMaxRecycleCount() throws UaException {
    return ClientMembers.await(readMaxRecycleCountAsync(), false);
  }

  @Override
  public void writeMaxRecycleCount(@Nullable UInteger value) throws UaException {
    try {
      StatusCode statusCode = writeMaxRecycleCountAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UInteger> readMaxRecycleCountAsync() {
    return getMaxRecycleCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxRecycleCount (declaration i=2398, owner"
                            + " i=2391) on "
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
  public CompletableFuture<StatusCode> writeMaxRecycleCountAsync(
      @Nullable UInteger maxRecycleCount) {
    return getMaxRecycleCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxRecycleCount (declaration i=2398, owner"
                            + " i=2391) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(maxRecycleCount));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getMaxRecycleCountNode() throws UaException {
    return ClientMembers.await(getMaxRecycleCountNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMaxRecycleCountNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxRecycleCount",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:MaxRecycleCount (declaration i=2398, owner i=2391)"));
  }

  @Override
  public @Nullable LocalizedText getCurrentState() throws UaException {
    FiniteStateVariableTypeNode node = getCurrentStateNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:CurrentState (declaration i=3830, owner i=2391)"
              + " on "
              + getNodeId());
    }
    return (LocalizedText) node.getValue().getValue().getValue();
  }

  @Override
  public void setCurrentState(@Nullable LocalizedText value) throws UaException {
    FiniteStateVariableTypeNode node = getCurrentStateNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:CurrentState (declaration i=3830, owner i=2391)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable LocalizedText readCurrentState() throws UaException {
    return ClientMembers.await(readCurrentStateAsync(), false);
  }

  @Override
  public void writeCurrentState(@Nullable LocalizedText value) throws UaException {
    try {
      StatusCode statusCode = writeCurrentStateAsync(value).get();
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
  public CompletableFuture<? extends @Nullable LocalizedText> readCurrentStateAsync() {
    return getCurrentStateNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:CurrentState (declaration i=3830, owner"
                            + " i=2391) on "
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
  public CompletableFuture<StatusCode> writeCurrentStateAsync(
      @Nullable LocalizedText currentState) {
    return getCurrentStateNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:CurrentState (declaration i=3830, owner"
                            + " i=2391) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(currentState));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public FiniteStateVariableTypeNode getCurrentStateNode() throws UaException {
    return ClientMembers.await(getCurrentStateNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends FiniteStateVariableTypeNode> getCurrentStateNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        FiniteStateVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "CurrentState",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:CurrentState (declaration i=3830, owner i=2391)"));
  }

  @Override
  public @Nullable LocalizedText getLastTransition() throws UaException {
    FiniteTransitionVariableTypeNode node = getLastTransitionNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:LastTransition (declaration i=3835, owner i=2391)"
              + " on "
              + getNodeId());
    }
    return (LocalizedText) node.getValue().getValue().getValue();
  }

  @Override
  public void setLastTransition(@Nullable LocalizedText value) throws UaException {
    FiniteTransitionVariableTypeNode node = getLastTransitionNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:LastTransition (declaration i=3835, owner i=2391)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable LocalizedText readLastTransition() throws UaException {
    return ClientMembers.await(readLastTransitionAsync(), false);
  }

  @Override
  public void writeLastTransition(@Nullable LocalizedText value) throws UaException {
    try {
      StatusCode statusCode = writeLastTransitionAsync(value).get();
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
  public CompletableFuture<? extends @Nullable LocalizedText> readLastTransitionAsync() {
    return getLastTransitionNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:LastTransition (declaration i=3835, owner"
                            + " i=2391) on "
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
  public CompletableFuture<StatusCode> writeLastTransitionAsync(
      @Nullable LocalizedText lastTransition) {
    return getLastTransitionNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:LastTransition (declaration i=3835, owner"
                            + " i=2391) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(lastTransition));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public FiniteTransitionVariableTypeNode getLastTransitionNode() throws UaException {
    return ClientMembers.await(getLastTransitionNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends FiniteTransitionVariableTypeNode>
      getLastTransitionNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        FiniteTransitionVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "LastTransition",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:LastTransition (declaration i=3835, owner i=2391)"));
  }

  @Override
  public @Nullable ProgramDiagnostic2DataType getProgramDiagnostic() throws UaException {
    ProgramDiagnostic2TypeNode node = getProgramDiagnosticNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ProgramDiagnostic (declaration i=2399, owner i=2391)"
              + " on "
              + getNodeId());
    }
    return (ProgramDiagnostic2DataType)
        decodeValue(
            node.getValue().getValue().getValue(),
            ProgramDiagnostic2DataType.class,
            ValueRanks.Scalar);
  }

  @Override
  public void setProgramDiagnostic(@Nullable ProgramDiagnostic2DataType value) throws UaException {
    ProgramDiagnostic2TypeNode node = getProgramDiagnosticNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ProgramDiagnostic (declaration i=2399, owner i=2391)"
              + " on "
              + getNodeId());
    }
    node.setValue(
        new Variant(encodeValue(value, ProgramDiagnostic2DataType.class, ValueRanks.Scalar)));
  }

  @Override
  public @Nullable ProgramDiagnostic2DataType readProgramDiagnostic() throws UaException {
    return ClientMembers.await(readProgramDiagnosticAsync(), false);
  }

  @Override
  public void writeProgramDiagnostic(@Nullable ProgramDiagnostic2DataType value)
      throws UaException {
    try {
      StatusCode statusCode = writeProgramDiagnosticAsync(value).get();
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
  public CompletableFuture<? extends @Nullable ProgramDiagnostic2DataType>
      readProgramDiagnosticAsync() {
    return getProgramDiagnosticNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ProgramDiagnostic (declaration i=2399, owner"
                            + " i=2391) on "
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
                return (ProgramDiagnostic2DataType)
                    decodeValue(
                        v.getValue().getValue(),
                        ProgramDiagnostic2DataType.class,
                        ValueRanks.Scalar);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeProgramDiagnosticAsync(
      @Nullable ProgramDiagnostic2DataType programDiagnostic) {
    return getProgramDiagnosticNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ProgramDiagnostic (declaration i=2399, owner"
                            + " i=2391) on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(
                            encodeValue(
                                programDiagnostic,
                                ProgramDiagnostic2DataType.class,
                                ValueRanks.Scalar)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable ProgramDiagnostic2TypeNode getProgramDiagnosticNode() throws UaException {
    return ClientMembers.await(getProgramDiagnosticNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable ProgramDiagnostic2TypeNode>
      getProgramDiagnosticNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        ProgramDiagnostic2TypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ProgramDiagnostic",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:ProgramDiagnostic (declaration i=2399, owner i=2391)"));
  }

  @Override
  public @Nullable BaseObjectTypeNode getFinalResultDataNode() throws UaException {
    return ClientMembers.await(getFinalResultDataNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable BaseObjectTypeNode> getFinalResultDataNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        BaseObjectTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "FinalResultData",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            true,
            "http://opcfoundation.org/UA/:FinalResultData (declaration i=3850, owner i=2391)"));
  }

  @Override
  public StateTypeNode getHaltedNode() throws UaException {
    return ClientMembers.await(getHaltedNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends StateTypeNode> getHaltedNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        StateTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "Halted",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            false,
            "http://opcfoundation.org/UA/:Halted (declaration i=2406, owner i=2391)"));
  }

  @Override
  public StateTypeNode getReadyNode() throws UaException {
    return ClientMembers.await(getReadyNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends StateTypeNode> getReadyNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        StateTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "Ready",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            false,
            "http://opcfoundation.org/UA/:Ready (declaration i=2400, owner i=2391)"));
  }

  @Override
  public StateTypeNode getRunningNode() throws UaException {
    return ClientMembers.await(getRunningNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends StateTypeNode> getRunningNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        StateTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "Running",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            false,
            "http://opcfoundation.org/UA/:Running (declaration i=2402, owner i=2391)"));
  }

  @Override
  public StateTypeNode getSuspendedNode() throws UaException {
    return ClientMembers.await(getSuspendedNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends StateTypeNode> getSuspendedNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        StateTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "Suspended",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            false,
            "http://opcfoundation.org/UA/:Suspended (declaration i=2404, owner i=2391)"));
  }

  @Override
  public TransitionTypeNode getHaltedToReadyNode() throws UaException {
    return ClientMembers.await(getHaltedToReadyNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends TransitionTypeNode> getHaltedToReadyNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        TransitionTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "HaltedToReady",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            false,
            "http://opcfoundation.org/UA/:HaltedToReady (declaration i=2408, owner i=2391)"));
  }

  @Override
  public TransitionTypeNode getReadyToRunningNode() throws UaException {
    return ClientMembers.await(getReadyToRunningNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends TransitionTypeNode> getReadyToRunningNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        TransitionTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ReadyToRunning",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            false,
            "http://opcfoundation.org/UA/:ReadyToRunning (declaration i=2410, owner i=2391)"));
  }

  @Override
  public TransitionTypeNode getRunningToHaltedNode() throws UaException {
    return ClientMembers.await(getRunningToHaltedNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends TransitionTypeNode> getRunningToHaltedNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        TransitionTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "RunningToHalted",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            false,
            "http://opcfoundation.org/UA/:RunningToHalted (declaration i=2412, owner i=2391)"));
  }

  @Override
  public TransitionTypeNode getRunningToReadyNode() throws UaException {
    return ClientMembers.await(getRunningToReadyNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends TransitionTypeNode> getRunningToReadyNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        TransitionTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "RunningToReady",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            false,
            "http://opcfoundation.org/UA/:RunningToReady (declaration i=2414, owner i=2391)"));
  }

  @Override
  public TransitionTypeNode getRunningToSuspendedNode() throws UaException {
    return ClientMembers.await(getRunningToSuspendedNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends TransitionTypeNode> getRunningToSuspendedNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        TransitionTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "RunningToSuspended",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            false,
            "http://opcfoundation.org/UA/:RunningToSuspended (declaration i=2416, owner i=2391)"));
  }

  @Override
  public TransitionTypeNode getSuspendedToRunningNode() throws UaException {
    return ClientMembers.await(getSuspendedToRunningNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends TransitionTypeNode> getSuspendedToRunningNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        TransitionTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "SuspendedToRunning",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            false,
            "http://opcfoundation.org/UA/:SuspendedToRunning (declaration i=2418, owner i=2391)"));
  }

  @Override
  public TransitionTypeNode getSuspendedToHaltedNode() throws UaException {
    return ClientMembers.await(getSuspendedToHaltedNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends TransitionTypeNode> getSuspendedToHaltedNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        TransitionTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "SuspendedToHalted",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            false,
            "http://opcfoundation.org/UA/:SuspendedToHalted (declaration i=2420, owner i=2391)"));
  }

  @Override
  public TransitionTypeNode getSuspendedToReadyNode() throws UaException {
    return ClientMembers.await(getSuspendedToReadyNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends TransitionTypeNode> getSuspendedToReadyNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        TransitionTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "SuspendedToReady",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            false,
            "http://opcfoundation.org/UA/:SuspendedToReady (declaration i=2422, owner i=2391)"));
  }

  @Override
  public TransitionTypeNode getReadyToHaltedNode() throws UaException {
    return ClientMembers.await(getReadyToHaltedNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends TransitionTypeNode> getReadyToHaltedNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        TransitionTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ReadyToHalted",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            false,
            "http://opcfoundation.org/UA/:ReadyToHalted (declaration i=2424, owner i=2391)"));
  }
}

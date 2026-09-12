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
import org.eclipse.milo.opcua.sdk.client.model.variables.BaseDataVariableTypeNode;
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

public class LldpRemoteStatisticsTypeNode extends BaseObjectTypeNode
    implements LldpRemoteStatisticsType {
  public LldpRemoteStatisticsTypeNode(
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
  public static ClientViews createViews(LldpRemoteStatisticsTypeNode node) {
    Objects.requireNonNull(node, "node");
    return ClientViews.forNode(node.client, node);
  }

  @Override
  public @Nullable UInteger getLastChangeTime() throws UaException {
    BaseDataVariableTypeNode node = getLastChangeTimeNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:LastChangeTime (declaration i=18997, owner i=18996)"
              + " on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setLastChangeTime(@Nullable UInteger value) throws UaException {
    BaseDataVariableTypeNode node = getLastChangeTimeNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:LastChangeTime (declaration i=18997, owner i=18996)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UInteger readLastChangeTime() throws UaException {
    return ClientMembers.await(readLastChangeTimeAsync(), false);
  }

  @Override
  public void writeLastChangeTime(@Nullable UInteger value) throws UaException {
    try {
      StatusCode statusCode = writeLastChangeTimeAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UInteger> readLastChangeTimeAsync() {
    return getLastChangeTimeNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:LastChangeTime (declaration i=18997, owner"
                            + " i=18996) on "
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
  public CompletableFuture<StatusCode> writeLastChangeTimeAsync(@Nullable UInteger lastChangeTime) {
    return getLastChangeTimeNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:LastChangeTime (declaration i=18997, owner"
                            + " i=18996) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(lastChangeTime));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public BaseDataVariableTypeNode getLastChangeTimeNode() throws UaException {
    return ClientMembers.await(getLastChangeTimeNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getLastChangeTimeNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "LastChangeTime",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:LastChangeTime (declaration i=18997, owner i=18996)"));
  }

  @Override
  public @Nullable UInteger getRemoteInserts() throws UaException {
    BaseDataVariableTypeNode node = getRemoteInsertsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:RemoteInserts (declaration i=18998, owner i=18996)"
              + " on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setRemoteInserts(@Nullable UInteger value) throws UaException {
    BaseDataVariableTypeNode node = getRemoteInsertsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:RemoteInserts (declaration i=18998, owner i=18996)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UInteger readRemoteInserts() throws UaException {
    return ClientMembers.await(readRemoteInsertsAsync(), false);
  }

  @Override
  public void writeRemoteInserts(@Nullable UInteger value) throws UaException {
    try {
      StatusCode statusCode = writeRemoteInsertsAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UInteger> readRemoteInsertsAsync() {
    return getRemoteInsertsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:RemoteInserts (declaration i=18998, owner"
                            + " i=18996) on "
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
  public CompletableFuture<StatusCode> writeRemoteInsertsAsync(@Nullable UInteger remoteInserts) {
    return getRemoteInsertsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:RemoteInserts (declaration i=18998, owner"
                            + " i=18996) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(remoteInserts));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public BaseDataVariableTypeNode getRemoteInsertsNode() throws UaException {
    return ClientMembers.await(getRemoteInsertsNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getRemoteInsertsNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "RemoteInserts",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:RemoteInserts (declaration i=18998, owner i=18996)"));
  }

  @Override
  public @Nullable UInteger getRemoteDeletes() throws UaException {
    BaseDataVariableTypeNode node = getRemoteDeletesNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:RemoteDeletes (declaration i=18999, owner i=18996)"
              + " on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setRemoteDeletes(@Nullable UInteger value) throws UaException {
    BaseDataVariableTypeNode node = getRemoteDeletesNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:RemoteDeletes (declaration i=18999, owner i=18996)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UInteger readRemoteDeletes() throws UaException {
    return ClientMembers.await(readRemoteDeletesAsync(), false);
  }

  @Override
  public void writeRemoteDeletes(@Nullable UInteger value) throws UaException {
    try {
      StatusCode statusCode = writeRemoteDeletesAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UInteger> readRemoteDeletesAsync() {
    return getRemoteDeletesNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:RemoteDeletes (declaration i=18999, owner"
                            + " i=18996) on "
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
  public CompletableFuture<StatusCode> writeRemoteDeletesAsync(@Nullable UInteger remoteDeletes) {
    return getRemoteDeletesNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:RemoteDeletes (declaration i=18999, owner"
                            + " i=18996) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(remoteDeletes));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public BaseDataVariableTypeNode getRemoteDeletesNode() throws UaException {
    return ClientMembers.await(getRemoteDeletesNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getRemoteDeletesNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "RemoteDeletes",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:RemoteDeletes (declaration i=18999, owner i=18996)"));
  }

  @Override
  public @Nullable UInteger getRemoteDrops() throws UaException {
    BaseDataVariableTypeNode node = getRemoteDropsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:RemoteDrops (declaration i=19000, owner i=18996)"
              + " on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setRemoteDrops(@Nullable UInteger value) throws UaException {
    BaseDataVariableTypeNode node = getRemoteDropsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:RemoteDrops (declaration i=19000, owner i=18996)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UInteger readRemoteDrops() throws UaException {
    return ClientMembers.await(readRemoteDropsAsync(), false);
  }

  @Override
  public void writeRemoteDrops(@Nullable UInteger value) throws UaException {
    try {
      StatusCode statusCode = writeRemoteDropsAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UInteger> readRemoteDropsAsync() {
    return getRemoteDropsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:RemoteDrops (declaration i=19000, owner"
                            + " i=18996) on "
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
  public CompletableFuture<StatusCode> writeRemoteDropsAsync(@Nullable UInteger remoteDrops) {
    return getRemoteDropsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:RemoteDrops (declaration i=19000, owner"
                            + " i=18996) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(remoteDrops));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public BaseDataVariableTypeNode getRemoteDropsNode() throws UaException {
    return ClientMembers.await(getRemoteDropsNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getRemoteDropsNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "RemoteDrops",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:RemoteDrops (declaration i=19000, owner i=18996)"));
  }

  @Override
  public @Nullable UInteger getRemoteAgeouts() throws UaException {
    BaseDataVariableTypeNode node = getRemoteAgeoutsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:RemoteAgeouts (declaration i=19001, owner i=18996)"
              + " on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setRemoteAgeouts(@Nullable UInteger value) throws UaException {
    BaseDataVariableTypeNode node = getRemoteAgeoutsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:RemoteAgeouts (declaration i=19001, owner i=18996)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UInteger readRemoteAgeouts() throws UaException {
    return ClientMembers.await(readRemoteAgeoutsAsync(), false);
  }

  @Override
  public void writeRemoteAgeouts(@Nullable UInteger value) throws UaException {
    try {
      StatusCode statusCode = writeRemoteAgeoutsAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UInteger> readRemoteAgeoutsAsync() {
    return getRemoteAgeoutsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:RemoteAgeouts (declaration i=19001, owner"
                            + " i=18996) on "
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
  public CompletableFuture<StatusCode> writeRemoteAgeoutsAsync(@Nullable UInteger remoteAgeouts) {
    return getRemoteAgeoutsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:RemoteAgeouts (declaration i=19001, owner"
                            + " i=18996) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(remoteAgeouts));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public BaseDataVariableTypeNode getRemoteAgeoutsNode() throws UaException {
    return ClientMembers.await(getRemoteAgeoutsNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getRemoteAgeoutsNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "RemoteAgeouts",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:RemoteAgeouts (declaration i=19001, owner i=18996)"));
  }
}

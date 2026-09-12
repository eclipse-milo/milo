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

public class PubSubCapabilitiesTypeNode extends BaseObjectTypeNode
    implements PubSubCapabilitiesType {
  public PubSubCapabilitiesTypeNode(
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
  public static ClientViews createViews(PubSubCapabilitiesTypeNode node) {
    Objects.requireNonNull(node, "node");
    return ClientViews.forNode(node.client, node);
  }

  @Override
  public @Nullable UInteger getMaxPubSubConnections() throws UaException {
    PropertyTypeNode node = getMaxPubSubConnectionsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxPubSubConnections (declaration i=23833, owner i=23832)"
              + " on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxPubSubConnections(@Nullable UInteger value) throws UaException {
    PropertyTypeNode node = getMaxPubSubConnectionsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxPubSubConnections (declaration i=23833, owner i=23832)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UInteger readMaxPubSubConnections() throws UaException {
    return ClientMembers.await(readMaxPubSubConnectionsAsync(), false);
  }

  @Override
  public void writeMaxPubSubConnections(@Nullable UInteger value) throws UaException {
    try {
      StatusCode statusCode = writeMaxPubSubConnectionsAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UInteger> readMaxPubSubConnectionsAsync() {
    return getMaxPubSubConnectionsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxPubSubConnections (declaration i=23833,"
                            + " owner i=23832) on "
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
  public CompletableFuture<StatusCode> writeMaxPubSubConnectionsAsync(
      @Nullable UInteger maxPubSubConnections) {
    return getMaxPubSubConnectionsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxPubSubConnections (declaration i=23833,"
                            + " owner i=23832) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(maxPubSubConnections));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getMaxPubSubConnectionsNode() throws UaException {
    return ClientMembers.await(getMaxPubSubConnectionsNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMaxPubSubConnectionsNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxPubSubConnections",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:MaxPubSubConnections (declaration i=23833, owner"
                + " i=23832)"));
  }

  @Override
  public @Nullable UInteger getMaxWriterGroups() throws UaException {
    PropertyTypeNode node = getMaxWriterGroupsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxWriterGroups (declaration i=23834, owner i=23832)"
              + " on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxWriterGroups(@Nullable UInteger value) throws UaException {
    PropertyTypeNode node = getMaxWriterGroupsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxWriterGroups (declaration i=23834, owner i=23832)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UInteger readMaxWriterGroups() throws UaException {
    return ClientMembers.await(readMaxWriterGroupsAsync(), false);
  }

  @Override
  public void writeMaxWriterGroups(@Nullable UInteger value) throws UaException {
    try {
      StatusCode statusCode = writeMaxWriterGroupsAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UInteger> readMaxWriterGroupsAsync() {
    return getMaxWriterGroupsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxWriterGroups (declaration i=23834, owner"
                            + " i=23832) on "
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
  public CompletableFuture<StatusCode> writeMaxWriterGroupsAsync(
      @Nullable UInteger maxWriterGroups) {
    return getMaxWriterGroupsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxWriterGroups (declaration i=23834, owner"
                            + " i=23832) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(maxWriterGroups));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getMaxWriterGroupsNode() throws UaException {
    return ClientMembers.await(getMaxWriterGroupsNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMaxWriterGroupsNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxWriterGroups",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:MaxWriterGroups (declaration i=23834, owner i=23832)"));
  }

  @Override
  public @Nullable UInteger getMaxReaderGroups() throws UaException {
    PropertyTypeNode node = getMaxReaderGroupsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxReaderGroups (declaration i=23835, owner i=23832)"
              + " on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxReaderGroups(@Nullable UInteger value) throws UaException {
    PropertyTypeNode node = getMaxReaderGroupsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxReaderGroups (declaration i=23835, owner i=23832)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UInteger readMaxReaderGroups() throws UaException {
    return ClientMembers.await(readMaxReaderGroupsAsync(), false);
  }

  @Override
  public void writeMaxReaderGroups(@Nullable UInteger value) throws UaException {
    try {
      StatusCode statusCode = writeMaxReaderGroupsAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UInteger> readMaxReaderGroupsAsync() {
    return getMaxReaderGroupsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxReaderGroups (declaration i=23835, owner"
                            + " i=23832) on "
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
  public CompletableFuture<StatusCode> writeMaxReaderGroupsAsync(
      @Nullable UInteger maxReaderGroups) {
    return getMaxReaderGroupsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxReaderGroups (declaration i=23835, owner"
                            + " i=23832) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(maxReaderGroups));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getMaxReaderGroupsNode() throws UaException {
    return ClientMembers.await(getMaxReaderGroupsNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMaxReaderGroupsNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxReaderGroups",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:MaxReaderGroups (declaration i=23835, owner i=23832)"));
  }

  @Override
  public @Nullable UInteger getMaxDataSetWriters() throws UaException {
    PropertyTypeNode node = getMaxDataSetWritersNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxDataSetWriters (declaration i=23836, owner i=23832)"
              + " on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxDataSetWriters(@Nullable UInteger value) throws UaException {
    PropertyTypeNode node = getMaxDataSetWritersNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxDataSetWriters (declaration i=23836, owner i=23832)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UInteger readMaxDataSetWriters() throws UaException {
    return ClientMembers.await(readMaxDataSetWritersAsync(), false);
  }

  @Override
  public void writeMaxDataSetWriters(@Nullable UInteger value) throws UaException {
    try {
      StatusCode statusCode = writeMaxDataSetWritersAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UInteger> readMaxDataSetWritersAsync() {
    return getMaxDataSetWritersNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxDataSetWriters (declaration i=23836, owner"
                            + " i=23832) on "
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
  public CompletableFuture<StatusCode> writeMaxDataSetWritersAsync(
      @Nullable UInteger maxDataSetWriters) {
    return getMaxDataSetWritersNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxDataSetWriters (declaration i=23836, owner"
                            + " i=23832) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(maxDataSetWriters));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getMaxDataSetWritersNode() throws UaException {
    return ClientMembers.await(getMaxDataSetWritersNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMaxDataSetWritersNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxDataSetWriters",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:MaxDataSetWriters (declaration i=23836, owner i=23832)"));
  }

  @Override
  public @Nullable UInteger getMaxDataSetReaders() throws UaException {
    PropertyTypeNode node = getMaxDataSetReadersNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxDataSetReaders (declaration i=23837, owner i=23832)"
              + " on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxDataSetReaders(@Nullable UInteger value) throws UaException {
    PropertyTypeNode node = getMaxDataSetReadersNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxDataSetReaders (declaration i=23837, owner i=23832)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UInteger readMaxDataSetReaders() throws UaException {
    return ClientMembers.await(readMaxDataSetReadersAsync(), false);
  }

  @Override
  public void writeMaxDataSetReaders(@Nullable UInteger value) throws UaException {
    try {
      StatusCode statusCode = writeMaxDataSetReadersAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UInteger> readMaxDataSetReadersAsync() {
    return getMaxDataSetReadersNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxDataSetReaders (declaration i=23837, owner"
                            + " i=23832) on "
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
  public CompletableFuture<StatusCode> writeMaxDataSetReadersAsync(
      @Nullable UInteger maxDataSetReaders) {
    return getMaxDataSetReadersNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxDataSetReaders (declaration i=23837, owner"
                            + " i=23832) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(maxDataSetReaders));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getMaxDataSetReadersNode() throws UaException {
    return ClientMembers.await(getMaxDataSetReadersNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMaxDataSetReadersNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxDataSetReaders",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:MaxDataSetReaders (declaration i=23837, owner i=23832)"));
  }

  @Override
  public @Nullable UInteger getMaxFieldsPerDataSet() throws UaException {
    PropertyTypeNode node = getMaxFieldsPerDataSetNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxFieldsPerDataSet (declaration i=23838, owner i=23832)"
              + " on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxFieldsPerDataSet(@Nullable UInteger value) throws UaException {
    PropertyTypeNode node = getMaxFieldsPerDataSetNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxFieldsPerDataSet (declaration i=23838, owner i=23832)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UInteger readMaxFieldsPerDataSet() throws UaException {
    return ClientMembers.await(readMaxFieldsPerDataSetAsync(), false);
  }

  @Override
  public void writeMaxFieldsPerDataSet(@Nullable UInteger value) throws UaException {
    try {
      StatusCode statusCode = writeMaxFieldsPerDataSetAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UInteger> readMaxFieldsPerDataSetAsync() {
    return getMaxFieldsPerDataSetNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxFieldsPerDataSet (declaration i=23838,"
                            + " owner i=23832) on "
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
  public CompletableFuture<StatusCode> writeMaxFieldsPerDataSetAsync(
      @Nullable UInteger maxFieldsPerDataSet) {
    return getMaxFieldsPerDataSetNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxFieldsPerDataSet (declaration i=23838,"
                            + " owner i=23832) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(maxFieldsPerDataSet));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getMaxFieldsPerDataSetNode() throws UaException {
    return ClientMembers.await(getMaxFieldsPerDataSetNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMaxFieldsPerDataSetNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxFieldsPerDataSet",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:MaxFieldsPerDataSet (declaration i=23838, owner"
                + " i=23832)"));
  }

  @Override
  public @Nullable UInteger getMaxDataSetWritersPerGroup() throws UaException {
    PropertyTypeNode node = getMaxDataSetWritersPerGroupNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxDataSetWritersPerGroup (declaration i=32651, owner"
              + " i=23832) on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxDataSetWritersPerGroup(@Nullable UInteger value) throws UaException {
    PropertyTypeNode node = getMaxDataSetWritersPerGroupNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxDataSetWritersPerGroup (declaration i=32651, owner"
              + " i=23832) on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UInteger readMaxDataSetWritersPerGroup() throws UaException {
    return ClientMembers.await(readMaxDataSetWritersPerGroupAsync(), false);
  }

  @Override
  public void writeMaxDataSetWritersPerGroup(@Nullable UInteger value) throws UaException {
    try {
      StatusCode statusCode = writeMaxDataSetWritersPerGroupAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UInteger> readMaxDataSetWritersPerGroupAsync() {
    return getMaxDataSetWritersPerGroupNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxDataSetWritersPerGroup (declaration"
                            + " i=32651, owner i=23832) on "
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
  public CompletableFuture<StatusCode> writeMaxDataSetWritersPerGroupAsync(
      @Nullable UInteger maxDataSetWritersPerGroup) {
    return getMaxDataSetWritersPerGroupNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxDataSetWritersPerGroup (declaration"
                            + " i=32651, owner i=23832) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(maxDataSetWritersPerGroup));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getMaxDataSetWritersPerGroupNode() throws UaException {
    return ClientMembers.await(getMaxDataSetWritersPerGroupNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getMaxDataSetWritersPerGroupNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxDataSetWritersPerGroup",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:MaxDataSetWritersPerGroup (declaration i=32651, owner"
                + " i=23832)"));
  }

  @Override
  public @Nullable UInteger getMaxSecurityGroups() throws UaException {
    PropertyTypeNode node = getMaxSecurityGroupsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxSecurityGroups (declaration i=32844, owner i=23832)"
              + " on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxSecurityGroups(@Nullable UInteger value) throws UaException {
    PropertyTypeNode node = getMaxSecurityGroupsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxSecurityGroups (declaration i=32844, owner i=23832)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UInteger readMaxSecurityGroups() throws UaException {
    return ClientMembers.await(readMaxSecurityGroupsAsync(), false);
  }

  @Override
  public void writeMaxSecurityGroups(@Nullable UInteger value) throws UaException {
    try {
      StatusCode statusCode = writeMaxSecurityGroupsAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UInteger> readMaxSecurityGroupsAsync() {
    return getMaxSecurityGroupsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxSecurityGroups (declaration i=32844, owner"
                            + " i=23832) on "
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
  public CompletableFuture<StatusCode> writeMaxSecurityGroupsAsync(
      @Nullable UInteger maxSecurityGroups) {
    return getMaxSecurityGroupsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxSecurityGroups (declaration i=32844, owner"
                            + " i=23832) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(maxSecurityGroups));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getMaxSecurityGroupsNode() throws UaException {
    return ClientMembers.await(getMaxSecurityGroupsNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getMaxSecurityGroupsNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxSecurityGroups",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:MaxSecurityGroups (declaration i=32844, owner i=23832)"));
  }

  @Override
  public @Nullable UInteger getMaxPushTargets() throws UaException {
    PropertyTypeNode node = getMaxPushTargetsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxPushTargets (declaration i=32845, owner i=23832)"
              + " on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxPushTargets(@Nullable UInteger value) throws UaException {
    PropertyTypeNode node = getMaxPushTargetsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxPushTargets (declaration i=32845, owner i=23832)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UInteger readMaxPushTargets() throws UaException {
    return ClientMembers.await(readMaxPushTargetsAsync(), false);
  }

  @Override
  public void writeMaxPushTargets(@Nullable UInteger value) throws UaException {
    try {
      StatusCode statusCode = writeMaxPushTargetsAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UInteger> readMaxPushTargetsAsync() {
    return getMaxPushTargetsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxPushTargets (declaration i=32845, owner"
                            + " i=23832) on "
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
  public CompletableFuture<StatusCode> writeMaxPushTargetsAsync(@Nullable UInteger maxPushTargets) {
    return getMaxPushTargetsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxPushTargets (declaration i=32845, owner"
                            + " i=23832) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(maxPushTargets));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getMaxPushTargetsNode() throws UaException {
    return ClientMembers.await(getMaxPushTargetsNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getMaxPushTargetsNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxPushTargets",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:MaxPushTargets (declaration i=32845, owner i=23832)"));
  }

  @Override
  public @Nullable UInteger getMaxPublishedDataSets() throws UaException {
    PropertyTypeNode node = getMaxPublishedDataSetsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxPublishedDataSets (declaration i=32846, owner i=23832)"
              + " on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxPublishedDataSets(@Nullable UInteger value) throws UaException {
    PropertyTypeNode node = getMaxPublishedDataSetsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxPublishedDataSets (declaration i=32846, owner i=23832)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UInteger readMaxPublishedDataSets() throws UaException {
    return ClientMembers.await(readMaxPublishedDataSetsAsync(), false);
  }

  @Override
  public void writeMaxPublishedDataSets(@Nullable UInteger value) throws UaException {
    try {
      StatusCode statusCode = writeMaxPublishedDataSetsAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UInteger> readMaxPublishedDataSetsAsync() {
    return getMaxPublishedDataSetsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxPublishedDataSets (declaration i=32846,"
                            + " owner i=23832) on "
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
  public CompletableFuture<StatusCode> writeMaxPublishedDataSetsAsync(
      @Nullable UInteger maxPublishedDataSets) {
    return getMaxPublishedDataSetsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxPublishedDataSets (declaration i=32846,"
                            + " owner i=23832) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(maxPublishedDataSets));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getMaxPublishedDataSetsNode() throws UaException {
    return ClientMembers.await(getMaxPublishedDataSetsNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getMaxPublishedDataSetsNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxPublishedDataSets",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:MaxPublishedDataSets (declaration i=32846, owner"
                + " i=23832)"));
  }

  @Override
  public @Nullable UInteger getMaxStandaloneSubscribedDataSets() throws UaException {
    PropertyTypeNode node = getMaxStandaloneSubscribedDataSetsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxStandaloneSubscribedDataSets (declaration i=32847, owner"
              + " i=23832) on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxStandaloneSubscribedDataSets(@Nullable UInteger value) throws UaException {
    PropertyTypeNode node = getMaxStandaloneSubscribedDataSetsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxStandaloneSubscribedDataSets (declaration i=32847, owner"
              + " i=23832) on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UInteger readMaxStandaloneSubscribedDataSets() throws UaException {
    return ClientMembers.await(readMaxStandaloneSubscribedDataSetsAsync(), false);
  }

  @Override
  public void writeMaxStandaloneSubscribedDataSets(@Nullable UInteger value) throws UaException {
    try {
      StatusCode statusCode = writeMaxStandaloneSubscribedDataSetsAsync(value).get();
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
      readMaxStandaloneSubscribedDataSetsAsync() {
    return getMaxStandaloneSubscribedDataSetsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxStandaloneSubscribedDataSets (declaration"
                            + " i=32847, owner i=23832) on "
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
  public CompletableFuture<StatusCode> writeMaxStandaloneSubscribedDataSetsAsync(
      @Nullable UInteger maxStandaloneSubscribedDataSets) {
    return getMaxStandaloneSubscribedDataSetsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxStandaloneSubscribedDataSets (declaration"
                            + " i=32847, owner i=23832) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(maxStandaloneSubscribedDataSets));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getMaxStandaloneSubscribedDataSetsNode() throws UaException {
    return ClientMembers.await(getMaxStandaloneSubscribedDataSetsNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getMaxStandaloneSubscribedDataSetsNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxStandaloneSubscribedDataSets",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:MaxStandaloneSubscribedDataSets (declaration i=32847,"
                + " owner i=23832)"));
  }

  @Override
  public @Nullable UInteger getMaxNetworkMessageSizeDatagram() throws UaException {
    PropertyTypeNode node = getMaxNetworkMessageSizeDatagramNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxNetworkMessageSizeDatagram (declaration i=32652, owner"
              + " i=23832) on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxNetworkMessageSizeDatagram(@Nullable UInteger value) throws UaException {
    PropertyTypeNode node = getMaxNetworkMessageSizeDatagramNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxNetworkMessageSizeDatagram (declaration i=32652, owner"
              + " i=23832) on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UInteger readMaxNetworkMessageSizeDatagram() throws UaException {
    return ClientMembers.await(readMaxNetworkMessageSizeDatagramAsync(), false);
  }

  @Override
  public void writeMaxNetworkMessageSizeDatagram(@Nullable UInteger value) throws UaException {
    try {
      StatusCode statusCode = writeMaxNetworkMessageSizeDatagramAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UInteger> readMaxNetworkMessageSizeDatagramAsync() {
    return getMaxNetworkMessageSizeDatagramNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxNetworkMessageSizeDatagram (declaration"
                            + " i=32652, owner i=23832) on "
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
  public CompletableFuture<StatusCode> writeMaxNetworkMessageSizeDatagramAsync(
      @Nullable UInteger maxNetworkMessageSizeDatagram) {
    return getMaxNetworkMessageSizeDatagramNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxNetworkMessageSizeDatagram (declaration"
                            + " i=32652, owner i=23832) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(maxNetworkMessageSizeDatagram));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getMaxNetworkMessageSizeDatagramNode() throws UaException {
    return ClientMembers.await(getMaxNetworkMessageSizeDatagramNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getMaxNetworkMessageSizeDatagramNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxNetworkMessageSizeDatagram",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:MaxNetworkMessageSizeDatagram (declaration i=32652, owner"
                + " i=23832)"));
  }

  @Override
  public @Nullable UInteger getMaxNetworkMessageSizeBroker() throws UaException {
    PropertyTypeNode node = getMaxNetworkMessageSizeBrokerNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxNetworkMessageSizeBroker (declaration i=32653, owner"
              + " i=23832) on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxNetworkMessageSizeBroker(@Nullable UInteger value) throws UaException {
    PropertyTypeNode node = getMaxNetworkMessageSizeBrokerNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxNetworkMessageSizeBroker (declaration i=32653, owner"
              + " i=23832) on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UInteger readMaxNetworkMessageSizeBroker() throws UaException {
    return ClientMembers.await(readMaxNetworkMessageSizeBrokerAsync(), false);
  }

  @Override
  public void writeMaxNetworkMessageSizeBroker(@Nullable UInteger value) throws UaException {
    try {
      StatusCode statusCode = writeMaxNetworkMessageSizeBrokerAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UInteger> readMaxNetworkMessageSizeBrokerAsync() {
    return getMaxNetworkMessageSizeBrokerNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxNetworkMessageSizeBroker (declaration"
                            + " i=32653, owner i=23832) on "
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
  public CompletableFuture<StatusCode> writeMaxNetworkMessageSizeBrokerAsync(
      @Nullable UInteger maxNetworkMessageSizeBroker) {
    return getMaxNetworkMessageSizeBrokerNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxNetworkMessageSizeBroker (declaration"
                            + " i=32653, owner i=23832) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(maxNetworkMessageSizeBroker));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getMaxNetworkMessageSizeBrokerNode() throws UaException {
    return ClientMembers.await(getMaxNetworkMessageSizeBrokerNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getMaxNetworkMessageSizeBrokerNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxNetworkMessageSizeBroker",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:MaxNetworkMessageSizeBroker (declaration i=32653, owner"
                + " i=23832)"));
  }

  @Override
  public @Nullable Boolean getSupportSecurityKeyPull() throws UaException {
    PropertyTypeNode node = getSupportSecurityKeyPullNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SupportSecurityKeyPull (declaration i=32654, owner i=23832)"
              + " on "
              + getNodeId());
    }
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setSupportSecurityKeyPull(@Nullable Boolean value) throws UaException {
    PropertyTypeNode node = getSupportSecurityKeyPullNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SupportSecurityKeyPull (declaration i=32654, owner i=23832)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable Boolean readSupportSecurityKeyPull() throws UaException {
    return ClientMembers.await(readSupportSecurityKeyPullAsync(), false);
  }

  @Override
  public void writeSupportSecurityKeyPull(@Nullable Boolean value) throws UaException {
    try {
      StatusCode statusCode = writeSupportSecurityKeyPullAsync(value).get();
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
  public CompletableFuture<? extends @Nullable Boolean> readSupportSecurityKeyPullAsync() {
    return getSupportSecurityKeyPullNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:SupportSecurityKeyPull (declaration i=32654,"
                            + " owner i=23832) on "
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
  public CompletableFuture<StatusCode> writeSupportSecurityKeyPullAsync(
      @Nullable Boolean supportSecurityKeyPull) {
    return getSupportSecurityKeyPullNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:SupportSecurityKeyPull (declaration i=32654,"
                            + " owner i=23832) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(supportSecurityKeyPull));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getSupportSecurityKeyPullNode() throws UaException {
    return ClientMembers.await(getSupportSecurityKeyPullNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getSupportSecurityKeyPullNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "SupportSecurityKeyPull",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:SupportSecurityKeyPull (declaration i=32654, owner"
                + " i=23832)"));
  }

  @Override
  public @Nullable Boolean getSupportSecurityKeyPush() throws UaException {
    PropertyTypeNode node = getSupportSecurityKeyPushNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SupportSecurityKeyPush (declaration i=32655, owner i=23832)"
              + " on "
              + getNodeId());
    }
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setSupportSecurityKeyPush(@Nullable Boolean value) throws UaException {
    PropertyTypeNode node = getSupportSecurityKeyPushNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SupportSecurityKeyPush (declaration i=32655, owner i=23832)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable Boolean readSupportSecurityKeyPush() throws UaException {
    return ClientMembers.await(readSupportSecurityKeyPushAsync(), false);
  }

  @Override
  public void writeSupportSecurityKeyPush(@Nullable Boolean value) throws UaException {
    try {
      StatusCode statusCode = writeSupportSecurityKeyPushAsync(value).get();
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
  public CompletableFuture<? extends @Nullable Boolean> readSupportSecurityKeyPushAsync() {
    return getSupportSecurityKeyPushNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:SupportSecurityKeyPush (declaration i=32655,"
                            + " owner i=23832) on "
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
  public CompletableFuture<StatusCode> writeSupportSecurityKeyPushAsync(
      @Nullable Boolean supportSecurityKeyPush) {
    return getSupportSecurityKeyPushNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:SupportSecurityKeyPush (declaration i=32655,"
                            + " owner i=23832) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(supportSecurityKeyPush));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getSupportSecurityKeyPushNode() throws UaException {
    return ClientMembers.await(getSupportSecurityKeyPushNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getSupportSecurityKeyPushNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "SupportSecurityKeyPush",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:SupportSecurityKeyPush (declaration i=32655, owner"
                + " i=23832)"));
  }

  @Override
  public @Nullable Boolean getSupportSecurityKeyServer() throws UaException {
    PropertyTypeNode node = getSupportSecurityKeyServerNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SupportSecurityKeyServer (declaration i=32848, owner"
              + " i=23832) on "
              + getNodeId());
    }
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setSupportSecurityKeyServer(@Nullable Boolean value) throws UaException {
    PropertyTypeNode node = getSupportSecurityKeyServerNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SupportSecurityKeyServer (declaration i=32848, owner"
              + " i=23832) on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable Boolean readSupportSecurityKeyServer() throws UaException {
    return ClientMembers.await(readSupportSecurityKeyServerAsync(), false);
  }

  @Override
  public void writeSupportSecurityKeyServer(@Nullable Boolean value) throws UaException {
    try {
      StatusCode statusCode = writeSupportSecurityKeyServerAsync(value).get();
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
  public CompletableFuture<? extends @Nullable Boolean> readSupportSecurityKeyServerAsync() {
    return getSupportSecurityKeyServerNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:SupportSecurityKeyServer (declaration"
                            + " i=32848, owner i=23832) on "
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
  public CompletableFuture<StatusCode> writeSupportSecurityKeyServerAsync(
      @Nullable Boolean supportSecurityKeyServer) {
    return getSupportSecurityKeyServerNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:SupportSecurityKeyServer (declaration"
                            + " i=32848, owner i=23832) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(supportSecurityKeyServer));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getSupportSecurityKeyServerNode() throws UaException {
    return ClientMembers.await(getSupportSecurityKeyServerNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getSupportSecurityKeyServerNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "SupportSecurityKeyServer",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:SupportSecurityKeyServer (declaration i=32848, owner"
                + " i=23832)"));
  }
}

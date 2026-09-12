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

public class HistoryServerCapabilitiesTypeNode extends BaseObjectTypeNode
    implements HistoryServerCapabilitiesType {
  public HistoryServerCapabilitiesTypeNode(
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
  public static ClientViews createViews(HistoryServerCapabilitiesTypeNode node) {
    Objects.requireNonNull(node, "node");
    return ClientViews.forNode(node.client, node);
  }

  @Override
  public @Nullable Boolean getAccessHistoryDataCapability() throws UaException {
    PropertyTypeNode node = getAccessHistoryDataCapabilityNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:AccessHistoryDataCapability (declaration i=2331, owner"
              + " i=2330) on "
              + getNodeId());
    }
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setAccessHistoryDataCapability(@Nullable Boolean value) throws UaException {
    PropertyTypeNode node = getAccessHistoryDataCapabilityNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:AccessHistoryDataCapability (declaration i=2331, owner"
              + " i=2330) on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable Boolean readAccessHistoryDataCapability() throws UaException {
    return ClientMembers.await(readAccessHistoryDataCapabilityAsync(), false);
  }

  @Override
  public void writeAccessHistoryDataCapability(@Nullable Boolean value) throws UaException {
    try {
      StatusCode statusCode = writeAccessHistoryDataCapabilityAsync(value).get();
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
  public CompletableFuture<? extends @Nullable Boolean> readAccessHistoryDataCapabilityAsync() {
    return getAccessHistoryDataCapabilityNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:AccessHistoryDataCapability (declaration"
                            + " i=2331, owner i=2330) on "
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
  public CompletableFuture<StatusCode> writeAccessHistoryDataCapabilityAsync(
      @Nullable Boolean accessHistoryDataCapability) {
    return getAccessHistoryDataCapabilityNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:AccessHistoryDataCapability (declaration"
                            + " i=2331, owner i=2330) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(accessHistoryDataCapability));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getAccessHistoryDataCapabilityNode() throws UaException {
    return ClientMembers.await(getAccessHistoryDataCapabilityNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getAccessHistoryDataCapabilityNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "AccessHistoryDataCapability",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:AccessHistoryDataCapability (declaration i=2331, owner"
                + " i=2330)"));
  }

  @Override
  public @Nullable Boolean getAccessHistoryEventsCapability() throws UaException {
    PropertyTypeNode node = getAccessHistoryEventsCapabilityNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:AccessHistoryEventsCapability (declaration i=2332, owner"
              + " i=2330) on "
              + getNodeId());
    }
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setAccessHistoryEventsCapability(@Nullable Boolean value) throws UaException {
    PropertyTypeNode node = getAccessHistoryEventsCapabilityNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:AccessHistoryEventsCapability (declaration i=2332, owner"
              + " i=2330) on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable Boolean readAccessHistoryEventsCapability() throws UaException {
    return ClientMembers.await(readAccessHistoryEventsCapabilityAsync(), false);
  }

  @Override
  public void writeAccessHistoryEventsCapability(@Nullable Boolean value) throws UaException {
    try {
      StatusCode statusCode = writeAccessHistoryEventsCapabilityAsync(value).get();
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
  public CompletableFuture<? extends @Nullable Boolean> readAccessHistoryEventsCapabilityAsync() {
    return getAccessHistoryEventsCapabilityNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:AccessHistoryEventsCapability (declaration"
                            + " i=2332, owner i=2330) on "
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
  public CompletableFuture<StatusCode> writeAccessHistoryEventsCapabilityAsync(
      @Nullable Boolean accessHistoryEventsCapability) {
    return getAccessHistoryEventsCapabilityNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:AccessHistoryEventsCapability (declaration"
                            + " i=2332, owner i=2330) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(accessHistoryEventsCapability));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getAccessHistoryEventsCapabilityNode() throws UaException {
    return ClientMembers.await(getAccessHistoryEventsCapabilityNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getAccessHistoryEventsCapabilityNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "AccessHistoryEventsCapability",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:AccessHistoryEventsCapability (declaration i=2332, owner"
                + " i=2330)"));
  }

  @Override
  public @Nullable UInteger getMaxReturnDataValues() throws UaException {
    PropertyTypeNode node = getMaxReturnDataValuesNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxReturnDataValues (declaration i=11268, owner i=2330)"
              + " on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxReturnDataValues(@Nullable UInteger value) throws UaException {
    PropertyTypeNode node = getMaxReturnDataValuesNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxReturnDataValues (declaration i=11268, owner i=2330)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UInteger readMaxReturnDataValues() throws UaException {
    return ClientMembers.await(readMaxReturnDataValuesAsync(), false);
  }

  @Override
  public void writeMaxReturnDataValues(@Nullable UInteger value) throws UaException {
    try {
      StatusCode statusCode = writeMaxReturnDataValuesAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UInteger> readMaxReturnDataValuesAsync() {
    return getMaxReturnDataValuesNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxReturnDataValues (declaration i=11268,"
                            + " owner i=2330) on "
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
  public CompletableFuture<StatusCode> writeMaxReturnDataValuesAsync(
      @Nullable UInteger maxReturnDataValues) {
    return getMaxReturnDataValuesNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxReturnDataValues (declaration i=11268,"
                            + " owner i=2330) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(maxReturnDataValues));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getMaxReturnDataValuesNode() throws UaException {
    return ClientMembers.await(getMaxReturnDataValuesNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMaxReturnDataValuesNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxReturnDataValues",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:MaxReturnDataValues (declaration i=11268, owner"
                + " i=2330)"));
  }

  @Override
  public @Nullable UInteger getMaxReturnEventValues() throws UaException {
    PropertyTypeNode node = getMaxReturnEventValuesNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxReturnEventValues (declaration i=11269, owner i=2330)"
              + " on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxReturnEventValues(@Nullable UInteger value) throws UaException {
    PropertyTypeNode node = getMaxReturnEventValuesNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxReturnEventValues (declaration i=11269, owner i=2330)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UInteger readMaxReturnEventValues() throws UaException {
    return ClientMembers.await(readMaxReturnEventValuesAsync(), false);
  }

  @Override
  public void writeMaxReturnEventValues(@Nullable UInteger value) throws UaException {
    try {
      StatusCode statusCode = writeMaxReturnEventValuesAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UInteger> readMaxReturnEventValuesAsync() {
    return getMaxReturnEventValuesNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxReturnEventValues (declaration i=11269,"
                            + " owner i=2330) on "
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
  public CompletableFuture<StatusCode> writeMaxReturnEventValuesAsync(
      @Nullable UInteger maxReturnEventValues) {
    return getMaxReturnEventValuesNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxReturnEventValues (declaration i=11269,"
                            + " owner i=2330) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(maxReturnEventValues));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getMaxReturnEventValuesNode() throws UaException {
    return ClientMembers.await(getMaxReturnEventValuesNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMaxReturnEventValuesNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxReturnEventValues",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:MaxReturnEventValues (declaration i=11269, owner"
                + " i=2330)"));
  }

  @Override
  public @Nullable Boolean getInsertDataCapability() throws UaException {
    PropertyTypeNode node = getInsertDataCapabilityNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:InsertDataCapability (declaration i=2334, owner i=2330)"
              + " on "
              + getNodeId());
    }
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setInsertDataCapability(@Nullable Boolean value) throws UaException {
    PropertyTypeNode node = getInsertDataCapabilityNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:InsertDataCapability (declaration i=2334, owner i=2330)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable Boolean readInsertDataCapability() throws UaException {
    return ClientMembers.await(readInsertDataCapabilityAsync(), false);
  }

  @Override
  public void writeInsertDataCapability(@Nullable Boolean value) throws UaException {
    try {
      StatusCode statusCode = writeInsertDataCapabilityAsync(value).get();
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
  public CompletableFuture<? extends @Nullable Boolean> readInsertDataCapabilityAsync() {
    return getInsertDataCapabilityNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:InsertDataCapability (declaration i=2334,"
                            + " owner i=2330) on "
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
  public CompletableFuture<StatusCode> writeInsertDataCapabilityAsync(
      @Nullable Boolean insertDataCapability) {
    return getInsertDataCapabilityNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:InsertDataCapability (declaration i=2334,"
                            + " owner i=2330) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(insertDataCapability));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getInsertDataCapabilityNode() throws UaException {
    return ClientMembers.await(getInsertDataCapabilityNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getInsertDataCapabilityNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "InsertDataCapability",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:InsertDataCapability (declaration i=2334, owner"
                + " i=2330)"));
  }

  @Override
  public @Nullable Boolean getReplaceDataCapability() throws UaException {
    PropertyTypeNode node = getReplaceDataCapabilityNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ReplaceDataCapability (declaration i=2335, owner i=2330)"
              + " on "
              + getNodeId());
    }
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setReplaceDataCapability(@Nullable Boolean value) throws UaException {
    PropertyTypeNode node = getReplaceDataCapabilityNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ReplaceDataCapability (declaration i=2335, owner i=2330)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable Boolean readReplaceDataCapability() throws UaException {
    return ClientMembers.await(readReplaceDataCapabilityAsync(), false);
  }

  @Override
  public void writeReplaceDataCapability(@Nullable Boolean value) throws UaException {
    try {
      StatusCode statusCode = writeReplaceDataCapabilityAsync(value).get();
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
  public CompletableFuture<? extends @Nullable Boolean> readReplaceDataCapabilityAsync() {
    return getReplaceDataCapabilityNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ReplaceDataCapability (declaration i=2335,"
                            + " owner i=2330) on "
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
  public CompletableFuture<StatusCode> writeReplaceDataCapabilityAsync(
      @Nullable Boolean replaceDataCapability) {
    return getReplaceDataCapabilityNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ReplaceDataCapability (declaration i=2335,"
                            + " owner i=2330) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(replaceDataCapability));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getReplaceDataCapabilityNode() throws UaException {
    return ClientMembers.await(getReplaceDataCapabilityNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getReplaceDataCapabilityNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ReplaceDataCapability",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:ReplaceDataCapability (declaration i=2335, owner"
                + " i=2330)"));
  }

  @Override
  public @Nullable Boolean getUpdateDataCapability() throws UaException {
    PropertyTypeNode node = getUpdateDataCapabilityNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:UpdateDataCapability (declaration i=2336, owner i=2330)"
              + " on "
              + getNodeId());
    }
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setUpdateDataCapability(@Nullable Boolean value) throws UaException {
    PropertyTypeNode node = getUpdateDataCapabilityNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:UpdateDataCapability (declaration i=2336, owner i=2330)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable Boolean readUpdateDataCapability() throws UaException {
    return ClientMembers.await(readUpdateDataCapabilityAsync(), false);
  }

  @Override
  public void writeUpdateDataCapability(@Nullable Boolean value) throws UaException {
    try {
      StatusCode statusCode = writeUpdateDataCapabilityAsync(value).get();
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
  public CompletableFuture<? extends @Nullable Boolean> readUpdateDataCapabilityAsync() {
    return getUpdateDataCapabilityNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:UpdateDataCapability (declaration i=2336,"
                            + " owner i=2330) on "
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
  public CompletableFuture<StatusCode> writeUpdateDataCapabilityAsync(
      @Nullable Boolean updateDataCapability) {
    return getUpdateDataCapabilityNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:UpdateDataCapability (declaration i=2336,"
                            + " owner i=2330) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(updateDataCapability));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getUpdateDataCapabilityNode() throws UaException {
    return ClientMembers.await(getUpdateDataCapabilityNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getUpdateDataCapabilityNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "UpdateDataCapability",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:UpdateDataCapability (declaration i=2336, owner"
                + " i=2330)"));
  }

  @Override
  public @Nullable Boolean getDeleteRawCapability() throws UaException {
    PropertyTypeNode node = getDeleteRawCapabilityNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DeleteRawCapability (declaration i=2337, owner i=2330)"
              + " on "
              + getNodeId());
    }
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setDeleteRawCapability(@Nullable Boolean value) throws UaException {
    PropertyTypeNode node = getDeleteRawCapabilityNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DeleteRawCapability (declaration i=2337, owner i=2330)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable Boolean readDeleteRawCapability() throws UaException {
    return ClientMembers.await(readDeleteRawCapabilityAsync(), false);
  }

  @Override
  public void writeDeleteRawCapability(@Nullable Boolean value) throws UaException {
    try {
      StatusCode statusCode = writeDeleteRawCapabilityAsync(value).get();
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
  public CompletableFuture<? extends @Nullable Boolean> readDeleteRawCapabilityAsync() {
    return getDeleteRawCapabilityNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:DeleteRawCapability (declaration i=2337,"
                            + " owner i=2330) on "
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
  public CompletableFuture<StatusCode> writeDeleteRawCapabilityAsync(
      @Nullable Boolean deleteRawCapability) {
    return getDeleteRawCapabilityNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:DeleteRawCapability (declaration i=2337,"
                            + " owner i=2330) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(deleteRawCapability));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getDeleteRawCapabilityNode() throws UaException {
    return ClientMembers.await(getDeleteRawCapabilityNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getDeleteRawCapabilityNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "DeleteRawCapability",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:DeleteRawCapability (declaration i=2337, owner i=2330)"));
  }

  @Override
  public @Nullable Boolean getDeleteAtTimeCapability() throws UaException {
    PropertyTypeNode node = getDeleteAtTimeCapabilityNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DeleteAtTimeCapability (declaration i=2338, owner i=2330)"
              + " on "
              + getNodeId());
    }
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setDeleteAtTimeCapability(@Nullable Boolean value) throws UaException {
    PropertyTypeNode node = getDeleteAtTimeCapabilityNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DeleteAtTimeCapability (declaration i=2338, owner i=2330)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable Boolean readDeleteAtTimeCapability() throws UaException {
    return ClientMembers.await(readDeleteAtTimeCapabilityAsync(), false);
  }

  @Override
  public void writeDeleteAtTimeCapability(@Nullable Boolean value) throws UaException {
    try {
      StatusCode statusCode = writeDeleteAtTimeCapabilityAsync(value).get();
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
  public CompletableFuture<? extends @Nullable Boolean> readDeleteAtTimeCapabilityAsync() {
    return getDeleteAtTimeCapabilityNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:DeleteAtTimeCapability (declaration i=2338,"
                            + " owner i=2330) on "
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
  public CompletableFuture<StatusCode> writeDeleteAtTimeCapabilityAsync(
      @Nullable Boolean deleteAtTimeCapability) {
    return getDeleteAtTimeCapabilityNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:DeleteAtTimeCapability (declaration i=2338,"
                            + " owner i=2330) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(deleteAtTimeCapability));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getDeleteAtTimeCapabilityNode() throws UaException {
    return ClientMembers.await(getDeleteAtTimeCapabilityNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getDeleteAtTimeCapabilityNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "DeleteAtTimeCapability",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:DeleteAtTimeCapability (declaration i=2338, owner"
                + " i=2330)"));
  }

  @Override
  public @Nullable Boolean getInsertEventCapability() throws UaException {
    PropertyTypeNode node = getInsertEventCapabilityNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:InsertEventCapability (declaration i=11278, owner i=2330)"
              + " on "
              + getNodeId());
    }
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setInsertEventCapability(@Nullable Boolean value) throws UaException {
    PropertyTypeNode node = getInsertEventCapabilityNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:InsertEventCapability (declaration i=11278, owner i=2330)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable Boolean readInsertEventCapability() throws UaException {
    return ClientMembers.await(readInsertEventCapabilityAsync(), false);
  }

  @Override
  public void writeInsertEventCapability(@Nullable Boolean value) throws UaException {
    try {
      StatusCode statusCode = writeInsertEventCapabilityAsync(value).get();
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
  public CompletableFuture<? extends @Nullable Boolean> readInsertEventCapabilityAsync() {
    return getInsertEventCapabilityNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:InsertEventCapability (declaration i=11278,"
                            + " owner i=2330) on "
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
  public CompletableFuture<StatusCode> writeInsertEventCapabilityAsync(
      @Nullable Boolean insertEventCapability) {
    return getInsertEventCapabilityNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:InsertEventCapability (declaration i=11278,"
                            + " owner i=2330) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(insertEventCapability));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getInsertEventCapabilityNode() throws UaException {
    return ClientMembers.await(getInsertEventCapabilityNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getInsertEventCapabilityNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "InsertEventCapability",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:InsertEventCapability (declaration i=11278, owner"
                + " i=2330)"));
  }

  @Override
  public @Nullable Boolean getReplaceEventCapability() throws UaException {
    PropertyTypeNode node = getReplaceEventCapabilityNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ReplaceEventCapability (declaration i=11279, owner i=2330)"
              + " on "
              + getNodeId());
    }
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setReplaceEventCapability(@Nullable Boolean value) throws UaException {
    PropertyTypeNode node = getReplaceEventCapabilityNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ReplaceEventCapability (declaration i=11279, owner i=2330)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable Boolean readReplaceEventCapability() throws UaException {
    return ClientMembers.await(readReplaceEventCapabilityAsync(), false);
  }

  @Override
  public void writeReplaceEventCapability(@Nullable Boolean value) throws UaException {
    try {
      StatusCode statusCode = writeReplaceEventCapabilityAsync(value).get();
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
  public CompletableFuture<? extends @Nullable Boolean> readReplaceEventCapabilityAsync() {
    return getReplaceEventCapabilityNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ReplaceEventCapability (declaration i=11279,"
                            + " owner i=2330) on "
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
  public CompletableFuture<StatusCode> writeReplaceEventCapabilityAsync(
      @Nullable Boolean replaceEventCapability) {
    return getReplaceEventCapabilityNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ReplaceEventCapability (declaration i=11279,"
                            + " owner i=2330) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(replaceEventCapability));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getReplaceEventCapabilityNode() throws UaException {
    return ClientMembers.await(getReplaceEventCapabilityNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getReplaceEventCapabilityNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ReplaceEventCapability",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:ReplaceEventCapability (declaration i=11279, owner"
                + " i=2330)"));
  }

  @Override
  public @Nullable Boolean getUpdateEventCapability() throws UaException {
    PropertyTypeNode node = getUpdateEventCapabilityNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:UpdateEventCapability (declaration i=11280, owner i=2330)"
              + " on "
              + getNodeId());
    }
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setUpdateEventCapability(@Nullable Boolean value) throws UaException {
    PropertyTypeNode node = getUpdateEventCapabilityNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:UpdateEventCapability (declaration i=11280, owner i=2330)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable Boolean readUpdateEventCapability() throws UaException {
    return ClientMembers.await(readUpdateEventCapabilityAsync(), false);
  }

  @Override
  public void writeUpdateEventCapability(@Nullable Boolean value) throws UaException {
    try {
      StatusCode statusCode = writeUpdateEventCapabilityAsync(value).get();
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
  public CompletableFuture<? extends @Nullable Boolean> readUpdateEventCapabilityAsync() {
    return getUpdateEventCapabilityNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:UpdateEventCapability (declaration i=11280,"
                            + " owner i=2330) on "
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
  public CompletableFuture<StatusCode> writeUpdateEventCapabilityAsync(
      @Nullable Boolean updateEventCapability) {
    return getUpdateEventCapabilityNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:UpdateEventCapability (declaration i=11280,"
                            + " owner i=2330) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(updateEventCapability));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getUpdateEventCapabilityNode() throws UaException {
    return ClientMembers.await(getUpdateEventCapabilityNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getUpdateEventCapabilityNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "UpdateEventCapability",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:UpdateEventCapability (declaration i=11280, owner"
                + " i=2330)"));
  }

  @Override
  public @Nullable Boolean getDeleteEventCapability() throws UaException {
    PropertyTypeNode node = getDeleteEventCapabilityNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DeleteEventCapability (declaration i=11501, owner i=2330)"
              + " on "
              + getNodeId());
    }
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setDeleteEventCapability(@Nullable Boolean value) throws UaException {
    PropertyTypeNode node = getDeleteEventCapabilityNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DeleteEventCapability (declaration i=11501, owner i=2330)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable Boolean readDeleteEventCapability() throws UaException {
    return ClientMembers.await(readDeleteEventCapabilityAsync(), false);
  }

  @Override
  public void writeDeleteEventCapability(@Nullable Boolean value) throws UaException {
    try {
      StatusCode statusCode = writeDeleteEventCapabilityAsync(value).get();
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
  public CompletableFuture<? extends @Nullable Boolean> readDeleteEventCapabilityAsync() {
    return getDeleteEventCapabilityNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:DeleteEventCapability (declaration i=11501,"
                            + " owner i=2330) on "
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
  public CompletableFuture<StatusCode> writeDeleteEventCapabilityAsync(
      @Nullable Boolean deleteEventCapability) {
    return getDeleteEventCapabilityNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:DeleteEventCapability (declaration i=11501,"
                            + " owner i=2330) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(deleteEventCapability));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getDeleteEventCapabilityNode() throws UaException {
    return ClientMembers.await(getDeleteEventCapabilityNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getDeleteEventCapabilityNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "DeleteEventCapability",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:DeleteEventCapability (declaration i=11501, owner"
                + " i=2330)"));
  }

  @Override
  public @Nullable Boolean getInsertAnnotationCapability() throws UaException {
    PropertyTypeNode node = getInsertAnnotationCapabilityNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:InsertAnnotationCapability (declaration i=11270, owner"
              + " i=2330) on "
              + getNodeId());
    }
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setInsertAnnotationCapability(@Nullable Boolean value) throws UaException {
    PropertyTypeNode node = getInsertAnnotationCapabilityNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:InsertAnnotationCapability (declaration i=11270, owner"
              + " i=2330) on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable Boolean readInsertAnnotationCapability() throws UaException {
    return ClientMembers.await(readInsertAnnotationCapabilityAsync(), false);
  }

  @Override
  public void writeInsertAnnotationCapability(@Nullable Boolean value) throws UaException {
    try {
      StatusCode statusCode = writeInsertAnnotationCapabilityAsync(value).get();
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
  public CompletableFuture<? extends @Nullable Boolean> readInsertAnnotationCapabilityAsync() {
    return getInsertAnnotationCapabilityNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:InsertAnnotationCapability (declaration"
                            + " i=11270, owner i=2330) on "
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
  public CompletableFuture<StatusCode> writeInsertAnnotationCapabilityAsync(
      @Nullable Boolean insertAnnotationCapability) {
    return getInsertAnnotationCapabilityNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:InsertAnnotationCapability (declaration"
                            + " i=11270, owner i=2330) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(insertAnnotationCapability));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getInsertAnnotationCapabilityNode() throws UaException {
    return ClientMembers.await(getInsertAnnotationCapabilityNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getInsertAnnotationCapabilityNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "InsertAnnotationCapability",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:InsertAnnotationCapability (declaration i=11270, owner"
                + " i=2330)"));
  }

  @Override
  public @Nullable Boolean getServerTimestampSupported() throws UaException {
    PropertyTypeNode node = getServerTimestampSupportedNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ServerTimestampSupported (declaration i=19094, owner"
              + " i=2330) on "
              + getNodeId());
    }
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setServerTimestampSupported(@Nullable Boolean value) throws UaException {
    PropertyTypeNode node = getServerTimestampSupportedNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ServerTimestampSupported (declaration i=19094, owner"
              + " i=2330) on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable Boolean readServerTimestampSupported() throws UaException {
    return ClientMembers.await(readServerTimestampSupportedAsync(), false);
  }

  @Override
  public void writeServerTimestampSupported(@Nullable Boolean value) throws UaException {
    try {
      StatusCode statusCode = writeServerTimestampSupportedAsync(value).get();
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
  public CompletableFuture<? extends @Nullable Boolean> readServerTimestampSupportedAsync() {
    return getServerTimestampSupportedNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ServerTimestampSupported (declaration"
                            + " i=19094, owner i=2330) on "
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
  public CompletableFuture<StatusCode> writeServerTimestampSupportedAsync(
      @Nullable Boolean serverTimestampSupported) {
    return getServerTimestampSupportedNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ServerTimestampSupported (declaration"
                            + " i=19094, owner i=2330) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(serverTimestampSupported));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getServerTimestampSupportedNode() throws UaException {
    return ClientMembers.await(getServerTimestampSupportedNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getServerTimestampSupportedNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ServerTimestampSupported",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:ServerTimestampSupported (declaration i=19094, owner"
                + " i=2330)"));
  }

  @Override
  public FolderTypeNode getAggregateFunctionsNode() throws UaException {
    return ClientMembers.await(getAggregateFunctionsNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends FolderTypeNode> getAggregateFunctionsNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        FolderTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "AggregateFunctions",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            false,
            "http://opcfoundation.org/UA/:AggregateFunctions (declaration i=11172, owner i=2330)"));
  }
}

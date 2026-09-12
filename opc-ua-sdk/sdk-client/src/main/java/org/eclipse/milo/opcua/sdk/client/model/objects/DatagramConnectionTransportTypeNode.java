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
import org.eclipse.milo.opcua.stack.core.types.structured.QosDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.jspecify.annotations.Nullable;

public class DatagramConnectionTransportTypeNode extends ConnectionTransportTypeNode
    implements DatagramConnectionTransportType {
  public DatagramConnectionTransportTypeNode(
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
  public static ClientViews createViews(DatagramConnectionTransportTypeNode node) {
    Objects.requireNonNull(node, "node");
    return ClientViews.forNode(node.client, node);
  }

  @Override
  public @Nullable UInteger getDiscoveryAnnounceRate() throws UaException {
    PropertyTypeNode node = getDiscoveryAnnounceRateNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DiscoveryAnnounceRate (declaration i=23839, owner i=15064)"
              + " on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setDiscoveryAnnounceRate(@Nullable UInteger value) throws UaException {
    PropertyTypeNode node = getDiscoveryAnnounceRateNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DiscoveryAnnounceRate (declaration i=23839, owner i=15064)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UInteger readDiscoveryAnnounceRate() throws UaException {
    return ClientMembers.await(readDiscoveryAnnounceRateAsync(), false);
  }

  @Override
  public void writeDiscoveryAnnounceRate(@Nullable UInteger value) throws UaException {
    try {
      StatusCode statusCode = writeDiscoveryAnnounceRateAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UInteger> readDiscoveryAnnounceRateAsync() {
    return getDiscoveryAnnounceRateNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:DiscoveryAnnounceRate (declaration i=23839,"
                            + " owner i=15064) on "
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
  public CompletableFuture<StatusCode> writeDiscoveryAnnounceRateAsync(
      @Nullable UInteger discoveryAnnounceRate) {
    return getDiscoveryAnnounceRateNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:DiscoveryAnnounceRate (declaration i=23839,"
                            + " owner i=15064) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(discoveryAnnounceRate));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getDiscoveryAnnounceRateNode() throws UaException {
    return ClientMembers.await(getDiscoveryAnnounceRateNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getDiscoveryAnnounceRateNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "DiscoveryAnnounceRate",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:DiscoveryAnnounceRate (declaration i=23839, owner"
                + " i=15064)"));
  }

  @Override
  public @Nullable UInteger getDiscoveryMaxMessageSize() throws UaException {
    PropertyTypeNode node = getDiscoveryMaxMessageSizeNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DiscoveryMaxMessageSize (declaration i=23840, owner"
              + " i=15064) on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setDiscoveryMaxMessageSize(@Nullable UInteger value) throws UaException {
    PropertyTypeNode node = getDiscoveryMaxMessageSizeNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DiscoveryMaxMessageSize (declaration i=23840, owner"
              + " i=15064) on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UInteger readDiscoveryMaxMessageSize() throws UaException {
    return ClientMembers.await(readDiscoveryMaxMessageSizeAsync(), false);
  }

  @Override
  public void writeDiscoveryMaxMessageSize(@Nullable UInteger value) throws UaException {
    try {
      StatusCode statusCode = writeDiscoveryMaxMessageSizeAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UInteger> readDiscoveryMaxMessageSizeAsync() {
    return getDiscoveryMaxMessageSizeNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:DiscoveryMaxMessageSize (declaration i=23840,"
                            + " owner i=15064) on "
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
  public CompletableFuture<StatusCode> writeDiscoveryMaxMessageSizeAsync(
      @Nullable UInteger discoveryMaxMessageSize) {
    return getDiscoveryMaxMessageSizeNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:DiscoveryMaxMessageSize (declaration i=23840,"
                            + " owner i=15064) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(discoveryMaxMessageSize));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getDiscoveryMaxMessageSizeNode() throws UaException {
    return ClientMembers.await(getDiscoveryMaxMessageSizeNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getDiscoveryMaxMessageSizeNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "DiscoveryMaxMessageSize",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:DiscoveryMaxMessageSize (declaration i=23840, owner"
                + " i=15064)"));
  }

  @Override
  public @Nullable String getQosCategory() throws UaException {
    PropertyTypeNode node = getQosCategoryNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:QosCategory (declaration i=25525, owner i=15064)"
              + " on "
              + getNodeId());
    }
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setQosCategory(@Nullable String value) throws UaException {
    PropertyTypeNode node = getQosCategoryNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:QosCategory (declaration i=25525, owner i=15064)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable String readQosCategory() throws UaException {
    return ClientMembers.await(readQosCategoryAsync(), false);
  }

  @Override
  public void writeQosCategory(@Nullable String value) throws UaException {
    try {
      StatusCode statusCode = writeQosCategoryAsync(value).get();
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
  public CompletableFuture<? extends @Nullable String> readQosCategoryAsync() {
    return getQosCategoryNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:QosCategory (declaration i=25525, owner"
                            + " i=15064) on "
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
  public CompletableFuture<StatusCode> writeQosCategoryAsync(@Nullable String qosCategory) {
    return getQosCategoryNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:QosCategory (declaration i=25525, owner"
                            + " i=15064) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(qosCategory));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getQosCategoryNode() throws UaException {
    return ClientMembers.await(getQosCategoryNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getQosCategoryNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "QosCategory",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:QosCategory (declaration i=25525, owner i=15064)"));
  }

  @Override
  public @Nullable QosDataType @Nullable [] getDatagramQos() throws UaException {
    PropertyTypeNode node = getDatagramQosNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DatagramQos (declaration i=25526, owner i=15064)"
              + " on "
              + getNodeId());
    }
    return (QosDataType[])
        decodeValue(
            node.getValue().getValue().getValue(), QosDataType.class, ValueRanks.OneDimension);
  }

  @Override
  public void setDatagramQos(@Nullable QosDataType @Nullable [] value) throws UaException {
    PropertyTypeNode node = getDatagramQosNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DatagramQos (declaration i=25526, owner i=15064)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(encodeValue(value, QosDataType.class, ValueRanks.OneDimension)));
  }

  @Override
  public @Nullable QosDataType @Nullable [] readDatagramQos() throws UaException {
    return ClientMembers.await(readDatagramQosAsync(), false);
  }

  @Override
  public void writeDatagramQos(@Nullable QosDataType @Nullable [] value) throws UaException {
    try {
      StatusCode statusCode = writeDatagramQosAsync(value).get();
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
  public CompletableFuture<? extends @Nullable QosDataType @Nullable []> readDatagramQosAsync() {
    return getDatagramQosNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:DatagramQos (declaration i=25526, owner"
                            + " i=15064) on "
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
                return (QosDataType[])
                    decodeValue(
                        v.getValue().getValue(), QosDataType.class, ValueRanks.OneDimension);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeDatagramQosAsync(
      @Nullable QosDataType @Nullable [] datagramQos) {
    return getDatagramQosNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:DatagramQos (declaration i=25526, owner"
                            + " i=15064) on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(
                            encodeValue(datagramQos, QosDataType.class, ValueRanks.OneDimension)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getDatagramQosNode() throws UaException {
    return ClientMembers.await(getDatagramQosNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getDatagramQosNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "DatagramQos",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:DatagramQos (declaration i=25526, owner i=15064)"));
  }

  @Override
  public NetworkAddressTypeNode getDiscoveryAddressNode() throws UaException {
    return ClientMembers.await(getDiscoveryAddressNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends NetworkAddressTypeNode> getDiscoveryAddressNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        NetworkAddressTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "DiscoveryAddress",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            false,
            "http://opcfoundation.org/UA/:DiscoveryAddress (declaration i=15072, owner i=15064)"));
  }
}

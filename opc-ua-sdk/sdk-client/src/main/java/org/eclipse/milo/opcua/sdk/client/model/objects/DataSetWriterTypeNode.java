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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetFieldContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.KeyValuePair;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.jspecify.annotations.Nullable;

public class DataSetWriterTypeNode extends BaseObjectTypeNode implements DataSetWriterType {
  public DataSetWriterTypeNode(
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
  public static ClientViews createViews(DataSetWriterTypeNode node) {
    Objects.requireNonNull(node, "node");
    return ClientViews.forNode(node.client, node);
  }

  @Override
  public @Nullable UShort getDataSetWriterId() throws UaException {
    PropertyTypeNode node = getDataSetWriterIdNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DataSetWriterId (declaration i=21092, owner i=15298)"
              + " on "
              + getNodeId());
    }
    return (UShort) node.getValue().getValue().getValue();
  }

  @Override
  public void setDataSetWriterId(@Nullable UShort value) throws UaException {
    PropertyTypeNode node = getDataSetWriterIdNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DataSetWriterId (declaration i=21092, owner i=15298)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UShort readDataSetWriterId() throws UaException {
    return ClientMembers.await(readDataSetWriterIdAsync(), false);
  }

  @Override
  public void writeDataSetWriterId(@Nullable UShort value) throws UaException {
    try {
      StatusCode statusCode = writeDataSetWriterIdAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UShort> readDataSetWriterIdAsync() {
    return getDataSetWriterIdNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:DataSetWriterId (declaration i=21092, owner"
                            + " i=15298) on "
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
  public CompletableFuture<StatusCode> writeDataSetWriterIdAsync(@Nullable UShort dataSetWriterId) {
    return getDataSetWriterIdNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:DataSetWriterId (declaration i=21092, owner"
                            + " i=15298) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(dataSetWriterId));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getDataSetWriterIdNode() throws UaException {
    return ClientMembers.await(getDataSetWriterIdNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getDataSetWriterIdNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "DataSetWriterId",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:DataSetWriterId (declaration i=21092, owner i=15298)"));
  }

  @Override
  public @Nullable DataSetFieldContentMask getDataSetFieldContentMask() throws UaException {
    PropertyTypeNode node = getDataSetFieldContentMaskNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DataSetFieldContentMask (declaration i=21093, owner"
              + " i=15298) on "
              + getNodeId());
    }
    return (DataSetFieldContentMask) node.getValue().getValue().getValue();
  }

  @Override
  public void setDataSetFieldContentMask(@Nullable DataSetFieldContentMask value)
      throws UaException {
    PropertyTypeNode node = getDataSetFieldContentMaskNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DataSetFieldContentMask (declaration i=21093, owner"
              + " i=15298) on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable DataSetFieldContentMask readDataSetFieldContentMask() throws UaException {
    return ClientMembers.await(readDataSetFieldContentMaskAsync(), false);
  }

  @Override
  public void writeDataSetFieldContentMask(@Nullable DataSetFieldContentMask value)
      throws UaException {
    try {
      StatusCode statusCode = writeDataSetFieldContentMaskAsync(value).get();
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
  public CompletableFuture<? extends @Nullable DataSetFieldContentMask>
      readDataSetFieldContentMaskAsync() {
    return getDataSetFieldContentMaskNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:DataSetFieldContentMask (declaration i=21093,"
                            + " owner i=15298) on "
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
                return (DataSetFieldContentMask) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeDataSetFieldContentMaskAsync(
      @Nullable DataSetFieldContentMask dataSetFieldContentMask) {
    return getDataSetFieldContentMaskNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:DataSetFieldContentMask (declaration i=21093,"
                            + " owner i=15298) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(dataSetFieldContentMask));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getDataSetFieldContentMaskNode() throws UaException {
    return ClientMembers.await(getDataSetFieldContentMaskNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getDataSetFieldContentMaskNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "DataSetFieldContentMask",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:DataSetFieldContentMask (declaration i=21093, owner"
                + " i=15298)"));
  }

  @Override
  public @Nullable UInteger getKeyFrameCount() throws UaException {
    PropertyTypeNode node = getKeyFrameCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:KeyFrameCount (declaration i=21094, owner i=15298)"
              + " on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setKeyFrameCount(@Nullable UInteger value) throws UaException {
    PropertyTypeNode node = getKeyFrameCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:KeyFrameCount (declaration i=21094, owner i=15298)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UInteger readKeyFrameCount() throws UaException {
    return ClientMembers.await(readKeyFrameCountAsync(), false);
  }

  @Override
  public void writeKeyFrameCount(@Nullable UInteger value) throws UaException {
    try {
      StatusCode statusCode = writeKeyFrameCountAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UInteger> readKeyFrameCountAsync() {
    return getKeyFrameCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:KeyFrameCount (declaration i=21094, owner"
                            + " i=15298) on "
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
  public CompletableFuture<StatusCode> writeKeyFrameCountAsync(@Nullable UInteger keyFrameCount) {
    return getKeyFrameCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:KeyFrameCount (declaration i=21094, owner"
                            + " i=15298) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(keyFrameCount));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getKeyFrameCountNode() throws UaException {
    return ClientMembers.await(getKeyFrameCountNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getKeyFrameCountNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "KeyFrameCount",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:KeyFrameCount (declaration i=21094, owner i=15298)"));
  }

  @Override
  public @Nullable KeyValuePair @Nullable [] getDataSetWriterProperties() throws UaException {
    PropertyTypeNode node = getDataSetWriterPropertiesNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DataSetWriterProperties (declaration i=17493, owner"
              + " i=15298) on "
              + getNodeId());
    }
    return (KeyValuePair[])
        decodeValue(
            node.getValue().getValue().getValue(), KeyValuePair.class, ValueRanks.OneDimension);
  }

  @Override
  public void setDataSetWriterProperties(@Nullable KeyValuePair @Nullable [] value)
      throws UaException {
    PropertyTypeNode node = getDataSetWriterPropertiesNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DataSetWriterProperties (declaration i=17493, owner"
              + " i=15298) on "
              + getNodeId());
    }
    node.setValue(new Variant(encodeValue(value, KeyValuePair.class, ValueRanks.OneDimension)));
  }

  @Override
  public @Nullable KeyValuePair @Nullable [] readDataSetWriterProperties() throws UaException {
    return ClientMembers.await(readDataSetWriterPropertiesAsync(), false);
  }

  @Override
  public void writeDataSetWriterProperties(@Nullable KeyValuePair @Nullable [] value)
      throws UaException {
    try {
      StatusCode statusCode = writeDataSetWriterPropertiesAsync(value).get();
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
  public CompletableFuture<? extends @Nullable KeyValuePair @Nullable []>
      readDataSetWriterPropertiesAsync() {
    return getDataSetWriterPropertiesNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:DataSetWriterProperties (declaration i=17493,"
                            + " owner i=15298) on "
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
                return (KeyValuePair[])
                    decodeValue(
                        v.getValue().getValue(), KeyValuePair.class, ValueRanks.OneDimension);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeDataSetWriterPropertiesAsync(
      @Nullable KeyValuePair @Nullable [] dataSetWriterProperties) {
    return getDataSetWriterPropertiesNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:DataSetWriterProperties (declaration i=17493,"
                            + " owner i=15298) on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(
                            encodeValue(
                                dataSetWriterProperties,
                                KeyValuePair.class,
                                ValueRanks.OneDimension)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getDataSetWriterPropertiesNode() throws UaException {
    return ClientMembers.await(getDataSetWriterPropertiesNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getDataSetWriterPropertiesNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "DataSetWriterProperties",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:DataSetWriterProperties (declaration i=17493, owner"
                + " i=15298)"));
  }

  @Override
  public @Nullable DataSetWriterTransportTypeNode getTransportSettingsNode() throws UaException {
    return ClientMembers.await(getTransportSettingsNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable DataSetWriterTransportTypeNode>
      getTransportSettingsNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        DataSetWriterTransportTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "TransportSettings",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            true,
            "http://opcfoundation.org/UA/:TransportSettings (declaration i=15303, owner i=15298)"));
  }

  @Override
  public @Nullable DataSetWriterMessageTypeNode getMessageSettingsNode() throws UaException {
    return ClientMembers.await(getMessageSettingsNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable DataSetWriterMessageTypeNode>
      getMessageSettingsNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        DataSetWriterMessageTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MessageSettings",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            true,
            "http://opcfoundation.org/UA/:MessageSettings (declaration i=21095, owner i=15298)"));
  }

  @Override
  public PubSubStatusTypeNode getStatusNode() throws UaException {
    return ClientMembers.await(getStatusNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PubSubStatusTypeNode> getStatusNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PubSubStatusTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "Status",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            false,
            "http://opcfoundation.org/UA/:Status (declaration i=15299, owner i=15298)"));
  }

  @Override
  public @Nullable PubSubDiagnosticsDataSetWriterTypeNode getDiagnosticsNode() throws UaException {
    return ClientMembers.await(getDiagnosticsNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PubSubDiagnosticsDataSetWriterTypeNode>
      getDiagnosticsNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PubSubDiagnosticsDataSetWriterTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "Diagnostics",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            true,
            "http://opcfoundation.org/UA/:Diagnostics (declaration i=19550, owner i=15298)"));
  }
}

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
import java.util.UUID;
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
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.jspecify.annotations.Nullable;

public class PublishedDataSetTypeNode extends BaseObjectTypeNode implements PublishedDataSetType {
  public PublishedDataSetTypeNode(
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
  public static ClientViews createViews(PublishedDataSetTypeNode node) {
    Objects.requireNonNull(node, "node");
    return ClientViews.forNode(node.client, node);
  }

  @Override
  public @Nullable ConfigurationVersionDataType getConfigurationVersion() throws UaException {
    PropertyTypeNode node = getConfigurationVersionNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ConfigurationVersion (declaration i=14519, owner i=14509)"
              + " on "
              + getNodeId());
    }
    return (ConfigurationVersionDataType)
        decodeValue(
            node.getValue().getValue().getValue(),
            ConfigurationVersionDataType.class,
            ValueRanks.Scalar);
  }

  @Override
  public void setConfigurationVersion(@Nullable ConfigurationVersionDataType value)
      throws UaException {
    PropertyTypeNode node = getConfigurationVersionNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ConfigurationVersion (declaration i=14519, owner i=14509)"
              + " on "
              + getNodeId());
    }
    node.setValue(
        new Variant(encodeValue(value, ConfigurationVersionDataType.class, ValueRanks.Scalar)));
  }

  @Override
  public @Nullable ConfigurationVersionDataType readConfigurationVersion() throws UaException {
    return ClientMembers.await(readConfigurationVersionAsync(), false);
  }

  @Override
  public void writeConfigurationVersion(@Nullable ConfigurationVersionDataType value)
      throws UaException {
    try {
      StatusCode statusCode = writeConfigurationVersionAsync(value).get();
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
  public CompletableFuture<? extends @Nullable ConfigurationVersionDataType>
      readConfigurationVersionAsync() {
    return getConfigurationVersionNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ConfigurationVersion (declaration i=14519,"
                            + " owner i=14509) on "
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
                return (ConfigurationVersionDataType)
                    decodeValue(
                        v.getValue().getValue(),
                        ConfigurationVersionDataType.class,
                        ValueRanks.Scalar);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeConfigurationVersionAsync(
      @Nullable ConfigurationVersionDataType configurationVersion) {
    return getConfigurationVersionNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ConfigurationVersion (declaration i=14519,"
                            + " owner i=14509) on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(
                            encodeValue(
                                configurationVersion,
                                ConfigurationVersionDataType.class,
                                ValueRanks.Scalar)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getConfigurationVersionNode() throws UaException {
    return ClientMembers.await(getConfigurationVersionNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getConfigurationVersionNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ConfigurationVersion",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:ConfigurationVersion (declaration i=14519, owner"
                + " i=14509)"));
  }

  @Override
  public @Nullable DataSetMetaDataType getDataSetMetaData() throws UaException {
    PropertyTypeNode node = getDataSetMetaDataNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DataSetMetaData (declaration i=15229, owner i=14509)"
              + " on "
              + getNodeId());
    }
    return (DataSetMetaDataType)
        decodeValue(
            node.getValue().getValue().getValue(), DataSetMetaDataType.class, ValueRanks.Scalar);
  }

  @Override
  public void setDataSetMetaData(@Nullable DataSetMetaDataType value) throws UaException {
    PropertyTypeNode node = getDataSetMetaDataNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DataSetMetaData (declaration i=15229, owner i=14509)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(encodeValue(value, DataSetMetaDataType.class, ValueRanks.Scalar)));
  }

  @Override
  public @Nullable DataSetMetaDataType readDataSetMetaData() throws UaException {
    return ClientMembers.await(readDataSetMetaDataAsync(), false);
  }

  @Override
  public void writeDataSetMetaData(@Nullable DataSetMetaDataType value) throws UaException {
    try {
      StatusCode statusCode = writeDataSetMetaDataAsync(value).get();
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
  public CompletableFuture<? extends @Nullable DataSetMetaDataType> readDataSetMetaDataAsync() {
    return getDataSetMetaDataNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:DataSetMetaData (declaration i=15229, owner"
                            + " i=14509) on "
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
                return (DataSetMetaDataType)
                    decodeValue(
                        v.getValue().getValue(), DataSetMetaDataType.class, ValueRanks.Scalar);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeDataSetMetaDataAsync(
      @Nullable DataSetMetaDataType dataSetMetaData) {
    return getDataSetMetaDataNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:DataSetMetaData (declaration i=15229, owner"
                            + " i=14509) on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(
                            encodeValue(
                                dataSetMetaData, DataSetMetaDataType.class, ValueRanks.Scalar)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getDataSetMetaDataNode() throws UaException {
    return ClientMembers.await(getDataSetMetaDataNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getDataSetMetaDataNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "DataSetMetaData",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:DataSetMetaData (declaration i=15229, owner i=14509)"));
  }

  @Override
  public @Nullable UUID getDataSetClassId() throws UaException {
    PropertyTypeNode node = getDataSetClassIdNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DataSetClassId (declaration i=16759, owner i=14509)"
              + " on "
              + getNodeId());
    }
    return (UUID) node.getValue().getValue().getValue();
  }

  @Override
  public void setDataSetClassId(@Nullable UUID value) throws UaException {
    PropertyTypeNode node = getDataSetClassIdNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DataSetClassId (declaration i=16759, owner i=14509)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UUID readDataSetClassId() throws UaException {
    return ClientMembers.await(readDataSetClassIdAsync(), false);
  }

  @Override
  public void writeDataSetClassId(@Nullable UUID value) throws UaException {
    try {
      StatusCode statusCode = writeDataSetClassIdAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UUID> readDataSetClassIdAsync() {
    return getDataSetClassIdNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:DataSetClassId (declaration i=16759, owner"
                            + " i=14509) on "
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
                return (UUID) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeDataSetClassIdAsync(@Nullable UUID dataSetClassId) {
    return getDataSetClassIdNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:DataSetClassId (declaration i=16759, owner"
                            + " i=14509) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(dataSetClassId));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getDataSetClassIdNode() throws UaException {
    return ClientMembers.await(getDataSetClassIdNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getDataSetClassIdNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "DataSetClassId",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:DataSetClassId (declaration i=16759, owner i=14509)"));
  }

  @Override
  public @Nullable Boolean getCyclicDataSet() throws UaException {
    PropertyTypeNode node = getCyclicDataSetNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:CyclicDataSet (declaration i=25521, owner i=14509)"
              + " on "
              + getNodeId());
    }
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setCyclicDataSet(@Nullable Boolean value) throws UaException {
    PropertyTypeNode node = getCyclicDataSetNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:CyclicDataSet (declaration i=25521, owner i=14509)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable Boolean readCyclicDataSet() throws UaException {
    return ClientMembers.await(readCyclicDataSetAsync(), false);
  }

  @Override
  public void writeCyclicDataSet(@Nullable Boolean value) throws UaException {
    try {
      StatusCode statusCode = writeCyclicDataSetAsync(value).get();
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
  public CompletableFuture<? extends @Nullable Boolean> readCyclicDataSetAsync() {
    return getCyclicDataSetNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:CyclicDataSet (declaration i=25521, owner"
                            + " i=14509) on "
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
  public CompletableFuture<StatusCode> writeCyclicDataSetAsync(@Nullable Boolean cyclicDataSet) {
    return getCyclicDataSetNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:CyclicDataSet (declaration i=25521, owner"
                            + " i=14509) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(cyclicDataSet));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getCyclicDataSetNode() throws UaException {
    return ClientMembers.await(getCyclicDataSetNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getCyclicDataSetNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "CyclicDataSet",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:CyclicDataSet (declaration i=25521, owner i=14509)"));
  }

  @Override
  public @Nullable ExtensionFieldsTypeNode getExtensionFieldsNode() throws UaException {
    return ClientMembers.await(getExtensionFieldsNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable ExtensionFieldsTypeNode>
      getExtensionFieldsNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        ExtensionFieldsTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ExtensionFields",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            true,
            "http://opcfoundation.org/UA/:ExtensionFields (declaration i=15481, owner i=14509)"));
  }
}

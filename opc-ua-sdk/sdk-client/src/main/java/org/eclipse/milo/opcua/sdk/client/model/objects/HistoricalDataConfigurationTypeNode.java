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
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ExceptionDeviationFormat;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.ArrayUtil;
import org.jspecify.annotations.Nullable;

public class HistoricalDataConfigurationTypeNode extends BaseObjectTypeNode
    implements HistoricalDataConfigurationType {
  public HistoricalDataConfigurationTypeNode(
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
  public static ClientViews createViews(HistoricalDataConfigurationTypeNode node) {
    Objects.requireNonNull(node, "node");
    return ClientViews.forNode(node.client, node);
  }

  @Override
  public @Nullable Boolean getStepped() throws UaException {
    PropertyTypeNode node = getSteppedNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:Stepped (declaration i=2323, owner i=2318)"
              + " on "
              + getNodeId());
    }
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setStepped(@Nullable Boolean value) throws UaException {
    PropertyTypeNode node = getSteppedNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:Stepped (declaration i=2323, owner i=2318)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable Boolean readStepped() throws UaException {
    return ClientMembers.await(readSteppedAsync(), false);
  }

  @Override
  public void writeStepped(@Nullable Boolean value) throws UaException {
    try {
      StatusCode statusCode = writeSteppedAsync(value).get();
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
  public CompletableFuture<? extends @Nullable Boolean> readSteppedAsync() {
    return getSteppedNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:Stepped (declaration i=2323, owner i=2318)"
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
  public CompletableFuture<StatusCode> writeSteppedAsync(@Nullable Boolean stepped) {
    return getSteppedNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:Stepped (declaration i=2323, owner i=2318)"
                            + " on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(stepped));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getSteppedNode() throws UaException {
    return ClientMembers.await(getSteppedNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getSteppedNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "Stepped",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:Stepped (declaration i=2323, owner i=2318)"));
  }

  @Override
  public @Nullable String getDefinition() throws UaException {
    PropertyTypeNode node = getDefinitionNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:Definition (declaration i=2324, owner i=2318)"
              + " on "
              + getNodeId());
    }
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setDefinition(@Nullable String value) throws UaException {
    PropertyTypeNode node = getDefinitionNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:Definition (declaration i=2324, owner i=2318)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable String readDefinition() throws UaException {
    return ClientMembers.await(readDefinitionAsync(), false);
  }

  @Override
  public void writeDefinition(@Nullable String value) throws UaException {
    try {
      StatusCode statusCode = writeDefinitionAsync(value).get();
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
  public CompletableFuture<? extends @Nullable String> readDefinitionAsync() {
    return getDefinitionNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:Definition (declaration i=2324, owner i=2318)"
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
                return (String) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeDefinitionAsync(@Nullable String definition) {
    return getDefinitionNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:Definition (declaration i=2324, owner i=2318)"
                            + " on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(definition));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getDefinitionNode() throws UaException {
    return ClientMembers.await(getDefinitionNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getDefinitionNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "Definition",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:Definition (declaration i=2324, owner i=2318)"));
  }

  @Override
  public @Nullable Double getMaxTimeInterval() throws UaException {
    PropertyTypeNode node = getMaxTimeIntervalNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxTimeInterval (declaration i=2325, owner i=2318)"
              + " on "
              + getNodeId());
    }
    return (Double) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxTimeInterval(@Nullable Double value) throws UaException {
    PropertyTypeNode node = getMaxTimeIntervalNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxTimeInterval (declaration i=2325, owner i=2318)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable Double readMaxTimeInterval() throws UaException {
    return ClientMembers.await(readMaxTimeIntervalAsync(), false);
  }

  @Override
  public void writeMaxTimeInterval(@Nullable Double value) throws UaException {
    try {
      StatusCode statusCode = writeMaxTimeIntervalAsync(value).get();
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
  public CompletableFuture<? extends @Nullable Double> readMaxTimeIntervalAsync() {
    return getMaxTimeIntervalNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxTimeInterval (declaration i=2325, owner"
                            + " i=2318) on "
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
                return (Double) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxTimeIntervalAsync(@Nullable Double maxTimeInterval) {
    return getMaxTimeIntervalNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxTimeInterval (declaration i=2325, owner"
                            + " i=2318) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(maxTimeInterval));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getMaxTimeIntervalNode() throws UaException {
    return ClientMembers.await(getMaxTimeIntervalNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getMaxTimeIntervalNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxTimeInterval",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:MaxTimeInterval (declaration i=2325, owner i=2318)"));
  }

  @Override
  public @Nullable Double getMinTimeInterval() throws UaException {
    PropertyTypeNode node = getMinTimeIntervalNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MinTimeInterval (declaration i=2326, owner i=2318)"
              + " on "
              + getNodeId());
    }
    return (Double) node.getValue().getValue().getValue();
  }

  @Override
  public void setMinTimeInterval(@Nullable Double value) throws UaException {
    PropertyTypeNode node = getMinTimeIntervalNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MinTimeInterval (declaration i=2326, owner i=2318)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable Double readMinTimeInterval() throws UaException {
    return ClientMembers.await(readMinTimeIntervalAsync(), false);
  }

  @Override
  public void writeMinTimeInterval(@Nullable Double value) throws UaException {
    try {
      StatusCode statusCode = writeMinTimeIntervalAsync(value).get();
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
  public CompletableFuture<? extends @Nullable Double> readMinTimeIntervalAsync() {
    return getMinTimeIntervalNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MinTimeInterval (declaration i=2326, owner"
                            + " i=2318) on "
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
                return (Double) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeMinTimeIntervalAsync(@Nullable Double minTimeInterval) {
    return getMinTimeIntervalNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MinTimeInterval (declaration i=2326, owner"
                            + " i=2318) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(minTimeInterval));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getMinTimeIntervalNode() throws UaException {
    return ClientMembers.await(getMinTimeIntervalNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getMinTimeIntervalNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MinTimeInterval",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:MinTimeInterval (declaration i=2326, owner i=2318)"));
  }

  @Override
  public @Nullable Double getExceptionDeviation() throws UaException {
    PropertyTypeNode node = getExceptionDeviationNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ExceptionDeviation (declaration i=2327, owner i=2318)"
              + " on "
              + getNodeId());
    }
    return (Double) node.getValue().getValue().getValue();
  }

  @Override
  public void setExceptionDeviation(@Nullable Double value) throws UaException {
    PropertyTypeNode node = getExceptionDeviationNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ExceptionDeviation (declaration i=2327, owner i=2318)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable Double readExceptionDeviation() throws UaException {
    return ClientMembers.await(readExceptionDeviationAsync(), false);
  }

  @Override
  public void writeExceptionDeviation(@Nullable Double value) throws UaException {
    try {
      StatusCode statusCode = writeExceptionDeviationAsync(value).get();
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
  public CompletableFuture<? extends @Nullable Double> readExceptionDeviationAsync() {
    return getExceptionDeviationNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ExceptionDeviation (declaration i=2327, owner"
                            + " i=2318) on "
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
                return (Double) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeExceptionDeviationAsync(
      @Nullable Double exceptionDeviation) {
    return getExceptionDeviationNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ExceptionDeviation (declaration i=2327, owner"
                            + " i=2318) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(exceptionDeviation));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getExceptionDeviationNode() throws UaException {
    return ClientMembers.await(getExceptionDeviationNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getExceptionDeviationNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ExceptionDeviation",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:ExceptionDeviation (declaration i=2327, owner i=2318)"));
  }

  @Override
  public @Nullable ExceptionDeviationFormat getExceptionDeviationFormat() throws UaException {
    PropertyTypeNode node = getExceptionDeviationFormatNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ExceptionDeviationFormat (declaration i=2328, owner i=2318)"
              + " on "
              + getNodeId());
    }
    Object value = node.getValue().getValue().getValue();
    Object convertedValue;
    {
      if (value == null || value instanceof Matrix && ((Matrix) value).isNull()) {
        convertedValue = null;
      } else {
        Object elements = value instanceof Matrix ? ((Matrix) value).getElements() : value;
        int rank =
            value instanceof Matrix
                ? ((Matrix) value).getValueRank()
                : ArrayUtil.getValueRank(value);
        boolean permitted = rank == -1;
        if (!permitted) {
          throw new UaException(
              StatusCodes.Bad_TypeMismatch,
              "ExceptionDeviationFormat: ValueRank=-1 does not permit rank " + rank);
        }
        if (value != null && !((Object) value instanceof ExceptionDeviationFormat)) {
          if (!(value instanceof Integer)) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "ExceptionDeviationFormat: expected"
                    + " org.eclipse.milo.opcua.stack.core.types.enumerated.ExceptionDeviationFormat"
                    + " or Int32, got "
                    + value);
          }
          if (ExceptionDeviationFormat.from((Integer) value) == null) {
            throw new UaException(
                StatusCodes.Bad_OutOfRange,
                "ExceptionDeviationFormat: unknown"
                    + " org.eclipse.milo.opcua.stack.core.types.enumerated.ExceptionDeviationFormat"
                    + " value "
                    + value);
          }
        }
        convertedValue =
            value == null || value instanceof ExceptionDeviationFormat
                ? (ExceptionDeviationFormat) value
                : ExceptionDeviationFormat.from((Integer) value);
      }
    }
    return (ExceptionDeviationFormat) convertedValue;
  }

  @Override
  public void setExceptionDeviationFormat(@Nullable ExceptionDeviationFormat value)
      throws UaException {
    PropertyTypeNode node = getExceptionDeviationFormatNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ExceptionDeviationFormat (declaration i=2328, owner i=2318)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable ExceptionDeviationFormat readExceptionDeviationFormat() throws UaException {
    return ClientMembers.await(readExceptionDeviationFormatAsync(), false);
  }

  @Override
  public void writeExceptionDeviationFormat(@Nullable ExceptionDeviationFormat value)
      throws UaException {
    try {
      StatusCode statusCode = writeExceptionDeviationFormatAsync(value).get();
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
  public CompletableFuture<? extends @Nullable ExceptionDeviationFormat>
      readExceptionDeviationFormatAsync() {
    return getExceptionDeviationFormatNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ExceptionDeviationFormat (declaration i=2328,"
                            + " owner i=2318) on "
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
                Object value = v.getValue().getValue();
                Object convertedValue;
                {
                  if (value == null || value instanceof Matrix && ((Matrix) value).isNull()) {
                    convertedValue = null;
                  } else {
                    Object elements =
                        value instanceof Matrix ? ((Matrix) value).getElements() : value;
                    int rank =
                        value instanceof Matrix
                            ? ((Matrix) value).getValueRank()
                            : ArrayUtil.getValueRank(value);
                    boolean permitted = rank == -1;
                    if (!permitted) {
                      throw new UaRuntimeException(
                          StatusCodes.Bad_TypeMismatch,
                          "ExceptionDeviationFormat: ValueRank=-1 does not permit rank " + rank);
                    }
                    if (value != null && !((Object) value instanceof ExceptionDeviationFormat)) {
                      if (!(value instanceof Integer)) {
                        throw new UaRuntimeException(
                            StatusCodes.Bad_TypeMismatch,
                            "ExceptionDeviationFormat: expected"
                                + " org.eclipse.milo.opcua.stack.core.types.enumerated.ExceptionDeviationFormat"
                                + " or Int32, got "
                                + value);
                      }
                      if (ExceptionDeviationFormat.from((Integer) value) == null) {
                        throw new UaRuntimeException(
                            StatusCodes.Bad_OutOfRange,
                            "ExceptionDeviationFormat: unknown"
                                + " org.eclipse.milo.opcua.stack.core.types.enumerated.ExceptionDeviationFormat"
                                + " value "
                                + value);
                      }
                    }
                    convertedValue =
                        value == null || value instanceof ExceptionDeviationFormat
                            ? (ExceptionDeviationFormat) value
                            : ExceptionDeviationFormat.from((Integer) value);
                  }
                }
                return (ExceptionDeviationFormat) convertedValue;
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeExceptionDeviationFormatAsync(
      @Nullable ExceptionDeviationFormat exceptionDeviationFormat) {
    return getExceptionDeviationFormatNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ExceptionDeviationFormat (declaration i=2328,"
                            + " owner i=2318) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(exceptionDeviationFormat));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getExceptionDeviationFormatNode() throws UaException {
    return ClientMembers.await(getExceptionDeviationFormatNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getExceptionDeviationFormatNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ExceptionDeviationFormat",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:ExceptionDeviationFormat (declaration i=2328, owner"
                + " i=2318)"));
  }

  @Override
  public @Nullable DateTime getStartOfArchive() throws UaException {
    PropertyTypeNode node = getStartOfArchiveNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:StartOfArchive (declaration i=11499, owner i=2318)"
              + " on "
              + getNodeId());
    }
    return (DateTime) node.getValue().getValue().getValue();
  }

  @Override
  public void setStartOfArchive(@Nullable DateTime value) throws UaException {
    PropertyTypeNode node = getStartOfArchiveNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:StartOfArchive (declaration i=11499, owner i=2318)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable DateTime readStartOfArchive() throws UaException {
    return ClientMembers.await(readStartOfArchiveAsync(), false);
  }

  @Override
  public void writeStartOfArchive(@Nullable DateTime value) throws UaException {
    try {
      StatusCode statusCode = writeStartOfArchiveAsync(value).get();
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
  public CompletableFuture<? extends @Nullable DateTime> readStartOfArchiveAsync() {
    return getStartOfArchiveNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:StartOfArchive (declaration i=11499, owner"
                            + " i=2318) on "
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
                return (DateTime) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeStartOfArchiveAsync(@Nullable DateTime startOfArchive) {
    return getStartOfArchiveNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:StartOfArchive (declaration i=11499, owner"
                            + " i=2318) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(startOfArchive));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getStartOfArchiveNode() throws UaException {
    return ClientMembers.await(getStartOfArchiveNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getStartOfArchiveNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "StartOfArchive",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:StartOfArchive (declaration i=11499, owner i=2318)"));
  }

  @Override
  public @Nullable DateTime getStartOfOnlineArchive() throws UaException {
    PropertyTypeNode node = getStartOfOnlineArchiveNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:StartOfOnlineArchive (declaration i=11500, owner i=2318)"
              + " on "
              + getNodeId());
    }
    return (DateTime) node.getValue().getValue().getValue();
  }

  @Override
  public void setStartOfOnlineArchive(@Nullable DateTime value) throws UaException {
    PropertyTypeNode node = getStartOfOnlineArchiveNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:StartOfOnlineArchive (declaration i=11500, owner i=2318)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable DateTime readStartOfOnlineArchive() throws UaException {
    return ClientMembers.await(readStartOfOnlineArchiveAsync(), false);
  }

  @Override
  public void writeStartOfOnlineArchive(@Nullable DateTime value) throws UaException {
    try {
      StatusCode statusCode = writeStartOfOnlineArchiveAsync(value).get();
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
  public CompletableFuture<? extends @Nullable DateTime> readStartOfOnlineArchiveAsync() {
    return getStartOfOnlineArchiveNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:StartOfOnlineArchive (declaration i=11500,"
                            + " owner i=2318) on "
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
                return (DateTime) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeStartOfOnlineArchiveAsync(
      @Nullable DateTime startOfOnlineArchive) {
    return getStartOfOnlineArchiveNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:StartOfOnlineArchive (declaration i=11500,"
                            + " owner i=2318) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(startOfOnlineArchive));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getStartOfOnlineArchiveNode() throws UaException {
    return ClientMembers.await(getStartOfOnlineArchiveNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getStartOfOnlineArchiveNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "StartOfOnlineArchive",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:StartOfOnlineArchive (declaration i=11500, owner"
                + " i=2318)"));
  }

  @Override
  public @Nullable Boolean getServerTimestampSupported() throws UaException {
    PropertyTypeNode node = getServerTimestampSupportedNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ServerTimestampSupported (declaration i=19092, owner"
              + " i=2318) on "
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
          "http://opcfoundation.org/UA/:ServerTimestampSupported (declaration i=19092, owner"
              + " i=2318) on "
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
                            + " i=19092, owner i=2318) on "
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
                            + " i=19092, owner i=2318) on "
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
            "http://opcfoundation.org/UA/:ServerTimestampSupported (declaration i=19092, owner"
                + " i=2318)"));
  }

  @Override
  public @Nullable Double getMaxTimeStoredValues() throws UaException {
    PropertyTypeNode node = getMaxTimeStoredValuesNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxTimeStoredValues (declaration i=32619, owner i=2318)"
              + " on "
              + getNodeId());
    }
    return (Double) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxTimeStoredValues(@Nullable Double value) throws UaException {
    PropertyTypeNode node = getMaxTimeStoredValuesNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxTimeStoredValues (declaration i=32619, owner i=2318)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable Double readMaxTimeStoredValues() throws UaException {
    return ClientMembers.await(readMaxTimeStoredValuesAsync(), false);
  }

  @Override
  public void writeMaxTimeStoredValues(@Nullable Double value) throws UaException {
    try {
      StatusCode statusCode = writeMaxTimeStoredValuesAsync(value).get();
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
  public CompletableFuture<? extends @Nullable Double> readMaxTimeStoredValuesAsync() {
    return getMaxTimeStoredValuesNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxTimeStoredValues (declaration i=32619,"
                            + " owner i=2318) on "
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
                return (Double) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxTimeStoredValuesAsync(
      @Nullable Double maxTimeStoredValues) {
    return getMaxTimeStoredValuesNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxTimeStoredValues (declaration i=32619,"
                            + " owner i=2318) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(maxTimeStoredValues));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getMaxTimeStoredValuesNode() throws UaException {
    return ClientMembers.await(getMaxTimeStoredValuesNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getMaxTimeStoredValuesNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxTimeStoredValues",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:MaxTimeStoredValues (declaration i=32619, owner"
                + " i=2318)"));
  }

  @Override
  public @Nullable UInteger getMaxCountStoredValues() throws UaException {
    PropertyTypeNode node = getMaxCountStoredValuesNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxCountStoredValues (declaration i=32620, owner i=2318)"
              + " on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxCountStoredValues(@Nullable UInteger value) throws UaException {
    PropertyTypeNode node = getMaxCountStoredValuesNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxCountStoredValues (declaration i=32620, owner i=2318)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UInteger readMaxCountStoredValues() throws UaException {
    return ClientMembers.await(readMaxCountStoredValuesAsync(), false);
  }

  @Override
  public void writeMaxCountStoredValues(@Nullable UInteger value) throws UaException {
    try {
      StatusCode statusCode = writeMaxCountStoredValuesAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UInteger> readMaxCountStoredValuesAsync() {
    return getMaxCountStoredValuesNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxCountStoredValues (declaration i=32620,"
                            + " owner i=2318) on "
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
  public CompletableFuture<StatusCode> writeMaxCountStoredValuesAsync(
      @Nullable UInteger maxCountStoredValues) {
    return getMaxCountStoredValuesNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxCountStoredValues (declaration i=32620,"
                            + " owner i=2318) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(maxCountStoredValues));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getMaxCountStoredValuesNode() throws UaException {
    return ClientMembers.await(getMaxCountStoredValuesNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getMaxCountStoredValuesNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxCountStoredValues",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:MaxCountStoredValues (declaration i=32620, owner"
                + " i=2318)"));
  }

  @Override
  public AggregateConfigurationTypeNode getAggregateConfigurationNode() throws UaException {
    return ClientMembers.await(getAggregateConfigurationNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends AggregateConfigurationTypeNode>
      getAggregateConfigurationNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        AggregateConfigurationTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "AggregateConfiguration",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            false,
            "http://opcfoundation.org/UA/:AggregateConfiguration (declaration i=3059, owner"
                + " i=2318)"));
  }

  @Override
  public @Nullable FolderTypeNode getAggregateFunctionsNode() throws UaException {
    return ClientMembers.await(getAggregateFunctionsNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable FolderTypeNode> getAggregateFunctionsNodeAsync() {
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
            true,
            "http://opcfoundation.org/UA/:AggregateFunctions (declaration i=11876, owner i=2318)"));
  }
}

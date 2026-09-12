/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.model.variables;

import com.digitalpetri.opcua.uanodeset.runtime.client.ClientMembers;
import com.digitalpetri.opcua.uanodeset.runtime.members.MemberDeclaration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
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
import org.eclipse.milo.opcua.stack.core.types.structured.AccessLevelExType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.jspecify.annotations.Nullable;

public class SamplingIntervalDiagnosticsTypeNode extends BaseDataVariableTypeNode
    implements SamplingIntervalDiagnosticsType {
  public SamplingIntervalDiagnosticsTypeNode(
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
      DataValue value,
      NodeId dataType,
      Integer valueRank,
      UInteger[] arrayDimensions,
      UByte accessLevel,
      UByte userAccessLevel,
      Double minimumSamplingInterval,
      Boolean historizing,
      AccessLevelExType accessLevelEx) {
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
        value,
        dataType,
        valueRank,
        arrayDimensions,
        accessLevel,
        userAccessLevel,
        minimumSamplingInterval,
        historizing,
        accessLevelEx);
  }

  @Override
  public @Nullable Double getSamplingInterval() throws UaException {
    BaseDataVariableTypeNode node = getSamplingIntervalNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SamplingInterval (declaration i=2166, owner i=2165)"
              + " on "
              + getNodeId());
    }
    return (Double) node.getValue().getValue().getValue();
  }

  @Override
  public void setSamplingInterval(@Nullable Double value) throws UaException {
    BaseDataVariableTypeNode node = getSamplingIntervalNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SamplingInterval (declaration i=2166, owner i=2165)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable Double readSamplingInterval() throws UaException {
    return ClientMembers.await(readSamplingIntervalAsync(), false);
  }

  @Override
  public void writeSamplingInterval(@Nullable Double value) throws UaException {
    try {
      StatusCode statusCode = writeSamplingIntervalAsync(value).get();
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
  public CompletableFuture<? extends @Nullable Double> readSamplingIntervalAsync() {
    return getSamplingIntervalNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:SamplingInterval (declaration i=2166, owner"
                            + " i=2165) on "
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
  public CompletableFuture<StatusCode> writeSamplingIntervalAsync(
      @Nullable Double samplingInterval) {
    return getSamplingIntervalNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:SamplingInterval (declaration i=2166, owner"
                            + " i=2165) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(samplingInterval));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public BaseDataVariableTypeNode getSamplingIntervalNode() throws UaException {
    return ClientMembers.await(getSamplingIntervalNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getSamplingIntervalNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "SamplingInterval",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:SamplingInterval (declaration i=2166, owner i=2165)"));
  }

  @Override
  public @Nullable UInteger getSampledMonitoredItemsCount() throws UaException {
    BaseDataVariableTypeNode node = getSampledMonitoredItemsCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SampledMonitoredItemsCount (declaration i=11697, owner"
              + " i=2165) on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setSampledMonitoredItemsCount(@Nullable UInteger value) throws UaException {
    BaseDataVariableTypeNode node = getSampledMonitoredItemsCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SampledMonitoredItemsCount (declaration i=11697, owner"
              + " i=2165) on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UInteger readSampledMonitoredItemsCount() throws UaException {
    return ClientMembers.await(readSampledMonitoredItemsCountAsync(), false);
  }

  @Override
  public void writeSampledMonitoredItemsCount(@Nullable UInteger value) throws UaException {
    try {
      StatusCode statusCode = writeSampledMonitoredItemsCountAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UInteger> readSampledMonitoredItemsCountAsync() {
    return getSampledMonitoredItemsCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:SampledMonitoredItemsCount (declaration"
                            + " i=11697, owner i=2165) on "
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
  public CompletableFuture<StatusCode> writeSampledMonitoredItemsCountAsync(
      @Nullable UInteger sampledMonitoredItemsCount) {
    return getSampledMonitoredItemsCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:SampledMonitoredItemsCount (declaration"
                            + " i=11697, owner i=2165) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(sampledMonitoredItemsCount));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public BaseDataVariableTypeNode getSampledMonitoredItemsCountNode() throws UaException {
    return ClientMembers.await(getSampledMonitoredItemsCountNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode>
      getSampledMonitoredItemsCountNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "SampledMonitoredItemsCount",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:SampledMonitoredItemsCount (declaration i=11697, owner"
                + " i=2165)"));
  }

  @Override
  public @Nullable UInteger getMaxSampledMonitoredItemsCount() throws UaException {
    BaseDataVariableTypeNode node = getMaxSampledMonitoredItemsCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxSampledMonitoredItemsCount (declaration i=11698, owner"
              + " i=2165) on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxSampledMonitoredItemsCount(@Nullable UInteger value) throws UaException {
    BaseDataVariableTypeNode node = getMaxSampledMonitoredItemsCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxSampledMonitoredItemsCount (declaration i=11698, owner"
              + " i=2165) on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UInteger readMaxSampledMonitoredItemsCount() throws UaException {
    return ClientMembers.await(readMaxSampledMonitoredItemsCountAsync(), false);
  }

  @Override
  public void writeMaxSampledMonitoredItemsCount(@Nullable UInteger value) throws UaException {
    try {
      StatusCode statusCode = writeMaxSampledMonitoredItemsCountAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UInteger> readMaxSampledMonitoredItemsCountAsync() {
    return getMaxSampledMonitoredItemsCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxSampledMonitoredItemsCount (declaration"
                            + " i=11698, owner i=2165) on "
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
  public CompletableFuture<StatusCode> writeMaxSampledMonitoredItemsCountAsync(
      @Nullable UInteger maxSampledMonitoredItemsCount) {
    return getMaxSampledMonitoredItemsCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxSampledMonitoredItemsCount (declaration"
                            + " i=11698, owner i=2165) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(maxSampledMonitoredItemsCount));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public BaseDataVariableTypeNode getMaxSampledMonitoredItemsCountNode() throws UaException {
    return ClientMembers.await(getMaxSampledMonitoredItemsCountNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode>
      getMaxSampledMonitoredItemsCountNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxSampledMonitoredItemsCount",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:MaxSampledMonitoredItemsCount (declaration i=11698, owner"
                + " i=2165)"));
  }

  @Override
  public @Nullable UInteger getDisabledMonitoredItemsSamplingCount() throws UaException {
    BaseDataVariableTypeNode node = getDisabledMonitoredItemsSamplingCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DisabledMonitoredItemsSamplingCount (declaration i=11699,"
              + " owner i=2165) on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setDisabledMonitoredItemsSamplingCount(@Nullable UInteger value) throws UaException {
    BaseDataVariableTypeNode node = getDisabledMonitoredItemsSamplingCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DisabledMonitoredItemsSamplingCount (declaration i=11699,"
              + " owner i=2165) on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UInteger readDisabledMonitoredItemsSamplingCount() throws UaException {
    return ClientMembers.await(readDisabledMonitoredItemsSamplingCountAsync(), false);
  }

  @Override
  public void writeDisabledMonitoredItemsSamplingCount(@Nullable UInteger value)
      throws UaException {
    try {
      StatusCode statusCode = writeDisabledMonitoredItemsSamplingCountAsync(value).get();
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
      readDisabledMonitoredItemsSamplingCountAsync() {
    return getDisabledMonitoredItemsSamplingCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:DisabledMonitoredItemsSamplingCount"
                            + " (declaration i=11699, owner i=2165) on "
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
  public CompletableFuture<StatusCode> writeDisabledMonitoredItemsSamplingCountAsync(
      @Nullable UInteger disabledMonitoredItemsSamplingCount) {
    return getDisabledMonitoredItemsSamplingCountNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:DisabledMonitoredItemsSamplingCount"
                            + " (declaration i=11699, owner i=2165) on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(new Variant(disabledMonitoredItemsSamplingCount));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public BaseDataVariableTypeNode getDisabledMonitoredItemsSamplingCountNode() throws UaException {
    return ClientMembers.await(getDisabledMonitoredItemsSamplingCountNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode>
      getDisabledMonitoredItemsSamplingCountNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "DisabledMonitoredItemsSamplingCount",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:DisabledMonitoredItemsSamplingCount (declaration i=11699,"
                + " owner i=2165)"));
  }
}

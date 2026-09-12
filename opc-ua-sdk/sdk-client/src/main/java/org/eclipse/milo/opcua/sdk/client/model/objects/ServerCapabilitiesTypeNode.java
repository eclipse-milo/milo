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
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.SignedSoftwareCertificate;
import org.jspecify.annotations.Nullable;

public class ServerCapabilitiesTypeNode extends BaseObjectTypeNode
    implements ServerCapabilitiesType {
  public ServerCapabilitiesTypeNode(
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
  public static ClientViews createViews(ServerCapabilitiesTypeNode node) {
    Objects.requireNonNull(node, "node");
    return ClientViews.forNode(node.client, node);
  }

  @Override
  public @Nullable String @Nullable [] getServerProfileArray() throws UaException {
    PropertyTypeNode node = getServerProfileArrayNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ServerProfileArray (declaration i=2014, owner i=2013)"
              + " on "
              + getNodeId());
    }
    return (String[]) node.getValue().getValue().getValue();
  }

  @Override
  public void setServerProfileArray(@Nullable String @Nullable [] value) throws UaException {
    PropertyTypeNode node = getServerProfileArrayNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ServerProfileArray (declaration i=2014, owner i=2013)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable String @Nullable [] readServerProfileArray() throws UaException {
    return ClientMembers.await(readServerProfileArrayAsync(), false);
  }

  @Override
  public void writeServerProfileArray(@Nullable String @Nullable [] value) throws UaException {
    try {
      StatusCode statusCode = writeServerProfileArrayAsync(value).get();
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
  public CompletableFuture<? extends @Nullable String @Nullable []> readServerProfileArrayAsync() {
    return getServerProfileArrayNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ServerProfileArray (declaration i=2014, owner"
                            + " i=2013) on "
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
                return (String[]) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeServerProfileArrayAsync(
      @Nullable String @Nullable [] serverProfileArray) {
    return getServerProfileArrayNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ServerProfileArray (declaration i=2014, owner"
                            + " i=2013) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(serverProfileArray));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getServerProfileArrayNode() throws UaException {
    return ClientMembers.await(getServerProfileArrayNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getServerProfileArrayNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ServerProfileArray",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:ServerProfileArray (declaration i=2014, owner i=2013)"));
  }

  @Override
  public @Nullable String @Nullable [] getLocaleIdArray() throws UaException {
    PropertyTypeNode node = getLocaleIdArrayNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:LocaleIdArray (declaration i=2016, owner i=2013)"
              + " on "
              + getNodeId());
    }
    return (String[]) node.getValue().getValue().getValue();
  }

  @Override
  public void setLocaleIdArray(@Nullable String @Nullable [] value) throws UaException {
    PropertyTypeNode node = getLocaleIdArrayNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:LocaleIdArray (declaration i=2016, owner i=2013)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable String @Nullable [] readLocaleIdArray() throws UaException {
    return ClientMembers.await(readLocaleIdArrayAsync(), false);
  }

  @Override
  public void writeLocaleIdArray(@Nullable String @Nullable [] value) throws UaException {
    try {
      StatusCode statusCode = writeLocaleIdArrayAsync(value).get();
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
  public CompletableFuture<? extends @Nullable String @Nullable []> readLocaleIdArrayAsync() {
    return getLocaleIdArrayNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:LocaleIdArray (declaration i=2016, owner"
                            + " i=2013) on "
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
                return (String[]) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeLocaleIdArrayAsync(
      @Nullable String @Nullable [] localeIdArray) {
    return getLocaleIdArrayNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:LocaleIdArray (declaration i=2016, owner"
                            + " i=2013) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(localeIdArray));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getLocaleIdArrayNode() throws UaException {
    return ClientMembers.await(getLocaleIdArrayNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getLocaleIdArrayNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "LocaleIdArray",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:LocaleIdArray (declaration i=2016, owner i=2013)"));
  }

  @Override
  public @Nullable Double getMinSupportedSampleRate() throws UaException {
    PropertyTypeNode node = getMinSupportedSampleRateNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MinSupportedSampleRate (declaration i=2017, owner i=2013)"
              + " on "
              + getNodeId());
    }
    return (Double) node.getValue().getValue().getValue();
  }

  @Override
  public void setMinSupportedSampleRate(@Nullable Double value) throws UaException {
    PropertyTypeNode node = getMinSupportedSampleRateNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MinSupportedSampleRate (declaration i=2017, owner i=2013)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable Double readMinSupportedSampleRate() throws UaException {
    return ClientMembers.await(readMinSupportedSampleRateAsync(), false);
  }

  @Override
  public void writeMinSupportedSampleRate(@Nullable Double value) throws UaException {
    try {
      StatusCode statusCode = writeMinSupportedSampleRateAsync(value).get();
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
  public CompletableFuture<? extends @Nullable Double> readMinSupportedSampleRateAsync() {
    return getMinSupportedSampleRateNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MinSupportedSampleRate (declaration i=2017,"
                            + " owner i=2013) on "
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
  public CompletableFuture<StatusCode> writeMinSupportedSampleRateAsync(
      @Nullable Double minSupportedSampleRate) {
    return getMinSupportedSampleRateNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MinSupportedSampleRate (declaration i=2017,"
                            + " owner i=2013) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(minSupportedSampleRate));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getMinSupportedSampleRateNode() throws UaException {
    return ClientMembers.await(getMinSupportedSampleRateNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMinSupportedSampleRateNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MinSupportedSampleRate",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:MinSupportedSampleRate (declaration i=2017, owner"
                + " i=2013)"));
  }

  @Override
  public @Nullable UShort getMaxBrowseContinuationPoints() throws UaException {
    PropertyTypeNode node = getMaxBrowseContinuationPointsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxBrowseContinuationPoints (declaration i=2732, owner"
              + " i=2013) on "
              + getNodeId());
    }
    return (UShort) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxBrowseContinuationPoints(@Nullable UShort value) throws UaException {
    PropertyTypeNode node = getMaxBrowseContinuationPointsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxBrowseContinuationPoints (declaration i=2732, owner"
              + " i=2013) on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UShort readMaxBrowseContinuationPoints() throws UaException {
    return ClientMembers.await(readMaxBrowseContinuationPointsAsync(), false);
  }

  @Override
  public void writeMaxBrowseContinuationPoints(@Nullable UShort value) throws UaException {
    try {
      StatusCode statusCode = writeMaxBrowseContinuationPointsAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UShort> readMaxBrowseContinuationPointsAsync() {
    return getMaxBrowseContinuationPointsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxBrowseContinuationPoints (declaration"
                            + " i=2732, owner i=2013) on "
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
  public CompletableFuture<StatusCode> writeMaxBrowseContinuationPointsAsync(
      @Nullable UShort maxBrowseContinuationPoints) {
    return getMaxBrowseContinuationPointsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxBrowseContinuationPoints (declaration"
                            + " i=2732, owner i=2013) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(maxBrowseContinuationPoints));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getMaxBrowseContinuationPointsNode() throws UaException {
    return ClientMembers.await(getMaxBrowseContinuationPointsNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMaxBrowseContinuationPointsNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxBrowseContinuationPoints",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:MaxBrowseContinuationPoints (declaration i=2732, owner"
                + " i=2013)"));
  }

  @Override
  public @Nullable UShort getMaxQueryContinuationPoints() throws UaException {
    PropertyTypeNode node = getMaxQueryContinuationPointsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxQueryContinuationPoints (declaration i=2733, owner"
              + " i=2013) on "
              + getNodeId());
    }
    return (UShort) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxQueryContinuationPoints(@Nullable UShort value) throws UaException {
    PropertyTypeNode node = getMaxQueryContinuationPointsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxQueryContinuationPoints (declaration i=2733, owner"
              + " i=2013) on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UShort readMaxQueryContinuationPoints() throws UaException {
    return ClientMembers.await(readMaxQueryContinuationPointsAsync(), false);
  }

  @Override
  public void writeMaxQueryContinuationPoints(@Nullable UShort value) throws UaException {
    try {
      StatusCode statusCode = writeMaxQueryContinuationPointsAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UShort> readMaxQueryContinuationPointsAsync() {
    return getMaxQueryContinuationPointsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxQueryContinuationPoints (declaration"
                            + " i=2733, owner i=2013) on "
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
  public CompletableFuture<StatusCode> writeMaxQueryContinuationPointsAsync(
      @Nullable UShort maxQueryContinuationPoints) {
    return getMaxQueryContinuationPointsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxQueryContinuationPoints (declaration"
                            + " i=2733, owner i=2013) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(maxQueryContinuationPoints));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getMaxQueryContinuationPointsNode() throws UaException {
    return ClientMembers.await(getMaxQueryContinuationPointsNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMaxQueryContinuationPointsNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxQueryContinuationPoints",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:MaxQueryContinuationPoints (declaration i=2733, owner"
                + " i=2013)"));
  }

  @Override
  public @Nullable UShort getMaxHistoryContinuationPoints() throws UaException {
    PropertyTypeNode node = getMaxHistoryContinuationPointsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxHistoryContinuationPoints (declaration i=2734, owner"
              + " i=2013) on "
              + getNodeId());
    }
    return (UShort) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxHistoryContinuationPoints(@Nullable UShort value) throws UaException {
    PropertyTypeNode node = getMaxHistoryContinuationPointsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxHistoryContinuationPoints (declaration i=2734, owner"
              + " i=2013) on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UShort readMaxHistoryContinuationPoints() throws UaException {
    return ClientMembers.await(readMaxHistoryContinuationPointsAsync(), false);
  }

  @Override
  public void writeMaxHistoryContinuationPoints(@Nullable UShort value) throws UaException {
    try {
      StatusCode statusCode = writeMaxHistoryContinuationPointsAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UShort> readMaxHistoryContinuationPointsAsync() {
    return getMaxHistoryContinuationPointsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxHistoryContinuationPoints (declaration"
                            + " i=2734, owner i=2013) on "
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
  public CompletableFuture<StatusCode> writeMaxHistoryContinuationPointsAsync(
      @Nullable UShort maxHistoryContinuationPoints) {
    return getMaxHistoryContinuationPointsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxHistoryContinuationPoints (declaration"
                            + " i=2734, owner i=2013) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(maxHistoryContinuationPoints));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getMaxHistoryContinuationPointsNode() throws UaException {
    return ClientMembers.await(getMaxHistoryContinuationPointsNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMaxHistoryContinuationPointsNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxHistoryContinuationPoints",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:MaxHistoryContinuationPoints (declaration i=2734, owner"
                + " i=2013)"));
  }

  @Override
  public @Nullable UShort getMaxLogObjectContinuationPoints() throws UaException {
    PropertyTypeNode node = getMaxLogObjectContinuationPointsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxLogObjectContinuationPoints (declaration i=19809, owner"
              + " i=2013) on "
              + getNodeId());
    }
    return (UShort) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxLogObjectContinuationPoints(@Nullable UShort value) throws UaException {
    PropertyTypeNode node = getMaxLogObjectContinuationPointsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxLogObjectContinuationPoints (declaration i=19809, owner"
              + " i=2013) on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UShort readMaxLogObjectContinuationPoints() throws UaException {
    return ClientMembers.await(readMaxLogObjectContinuationPointsAsync(), false);
  }

  @Override
  public void writeMaxLogObjectContinuationPoints(@Nullable UShort value) throws UaException {
    try {
      StatusCode statusCode = writeMaxLogObjectContinuationPointsAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UShort> readMaxLogObjectContinuationPointsAsync() {
    return getMaxLogObjectContinuationPointsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxLogObjectContinuationPoints (declaration"
                            + " i=19809, owner i=2013) on "
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
  public CompletableFuture<StatusCode> writeMaxLogObjectContinuationPointsAsync(
      @Nullable UShort maxLogObjectContinuationPoints) {
    return getMaxLogObjectContinuationPointsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxLogObjectContinuationPoints (declaration"
                            + " i=19809, owner i=2013) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(maxLogObjectContinuationPoints));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getMaxLogObjectContinuationPointsNode() throws UaException {
    return ClientMembers.await(getMaxLogObjectContinuationPointsNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getMaxLogObjectContinuationPointsNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxLogObjectContinuationPoints",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:MaxLogObjectContinuationPoints (declaration i=19809,"
                + " owner i=2013)"));
  }

  @Override
  public @Nullable SignedSoftwareCertificate @Nullable [] getSoftwareCertificates()
      throws UaException {
    PropertyTypeNode node = getSoftwareCertificatesNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SoftwareCertificates (declaration i=3049, owner i=2013)"
              + " on "
              + getNodeId());
    }
    return (SignedSoftwareCertificate[])
        decodeValue(
            node.getValue().getValue().getValue(),
            SignedSoftwareCertificate.class,
            ValueRanks.OneDimension);
  }

  @Override
  public void setSoftwareCertificates(@Nullable SignedSoftwareCertificate @Nullable [] value)
      throws UaException {
    PropertyTypeNode node = getSoftwareCertificatesNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SoftwareCertificates (declaration i=3049, owner i=2013)"
              + " on "
              + getNodeId());
    }
    node.setValue(
        new Variant(encodeValue(value, SignedSoftwareCertificate.class, ValueRanks.OneDimension)));
  }

  @Override
  public @Nullable SignedSoftwareCertificate @Nullable [] readSoftwareCertificates()
      throws UaException {
    return ClientMembers.await(readSoftwareCertificatesAsync(), false);
  }

  @Override
  public void writeSoftwareCertificates(@Nullable SignedSoftwareCertificate @Nullable [] value)
      throws UaException {
    try {
      StatusCode statusCode = writeSoftwareCertificatesAsync(value).get();
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
  public CompletableFuture<? extends @Nullable SignedSoftwareCertificate @Nullable []>
      readSoftwareCertificatesAsync() {
    return getSoftwareCertificatesNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:SoftwareCertificates (declaration i=3049,"
                            + " owner i=2013) on "
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
                return (SignedSoftwareCertificate[])
                    decodeValue(
                        v.getValue().getValue(),
                        SignedSoftwareCertificate.class,
                        ValueRanks.OneDimension);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeSoftwareCertificatesAsync(
      @Nullable SignedSoftwareCertificate @Nullable [] softwareCertificates) {
    return getSoftwareCertificatesNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:SoftwareCertificates (declaration i=3049,"
                            + " owner i=2013) on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(
                            encodeValue(
                                softwareCertificates,
                                SignedSoftwareCertificate.class,
                                ValueRanks.OneDimension)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getSoftwareCertificatesNode() throws UaException {
    return ClientMembers.await(getSoftwareCertificatesNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getSoftwareCertificatesNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "SoftwareCertificates",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:SoftwareCertificates (declaration i=3049, owner"
                + " i=2013)"));
  }

  @Override
  public @Nullable UInteger getMaxArrayLength() throws UaException {
    PropertyTypeNode node = getMaxArrayLengthNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxArrayLength (declaration i=11549, owner i=2013)"
              + " on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxArrayLength(@Nullable UInteger value) throws UaException {
    PropertyTypeNode node = getMaxArrayLengthNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxArrayLength (declaration i=11549, owner i=2013)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UInteger readMaxArrayLength() throws UaException {
    return ClientMembers.await(readMaxArrayLengthAsync(), false);
  }

  @Override
  public void writeMaxArrayLength(@Nullable UInteger value) throws UaException {
    try {
      StatusCode statusCode = writeMaxArrayLengthAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UInteger> readMaxArrayLengthAsync() {
    return getMaxArrayLengthNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxArrayLength (declaration i=11549, owner"
                            + " i=2013) on "
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
  public CompletableFuture<StatusCode> writeMaxArrayLengthAsync(@Nullable UInteger maxArrayLength) {
    return getMaxArrayLengthNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxArrayLength (declaration i=11549, owner"
                            + " i=2013) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(maxArrayLength));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getMaxArrayLengthNode() throws UaException {
    return ClientMembers.await(getMaxArrayLengthNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getMaxArrayLengthNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxArrayLength",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:MaxArrayLength (declaration i=11549, owner i=2013)"));
  }

  @Override
  public @Nullable UInteger getMaxStringLength() throws UaException {
    PropertyTypeNode node = getMaxStringLengthNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxStringLength (declaration i=11550, owner i=2013)"
              + " on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxStringLength(@Nullable UInteger value) throws UaException {
    PropertyTypeNode node = getMaxStringLengthNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxStringLength (declaration i=11550, owner i=2013)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UInteger readMaxStringLength() throws UaException {
    return ClientMembers.await(readMaxStringLengthAsync(), false);
  }

  @Override
  public void writeMaxStringLength(@Nullable UInteger value) throws UaException {
    try {
      StatusCode statusCode = writeMaxStringLengthAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UInteger> readMaxStringLengthAsync() {
    return getMaxStringLengthNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxStringLength (declaration i=11550, owner"
                            + " i=2013) on "
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
  public CompletableFuture<StatusCode> writeMaxStringLengthAsync(
      @Nullable UInteger maxStringLength) {
    return getMaxStringLengthNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxStringLength (declaration i=11550, owner"
                            + " i=2013) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(maxStringLength));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getMaxStringLengthNode() throws UaException {
    return ClientMembers.await(getMaxStringLengthNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getMaxStringLengthNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxStringLength",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:MaxStringLength (declaration i=11550, owner i=2013)"));
  }

  @Override
  public @Nullable UInteger getMaxByteStringLength() throws UaException {
    PropertyTypeNode node = getMaxByteStringLengthNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxByteStringLength (declaration i=12910, owner i=2013)"
              + " on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxByteStringLength(@Nullable UInteger value) throws UaException {
    PropertyTypeNode node = getMaxByteStringLengthNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxByteStringLength (declaration i=12910, owner i=2013)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UInteger readMaxByteStringLength() throws UaException {
    return ClientMembers.await(readMaxByteStringLengthAsync(), false);
  }

  @Override
  public void writeMaxByteStringLength(@Nullable UInteger value) throws UaException {
    try {
      StatusCode statusCode = writeMaxByteStringLengthAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UInteger> readMaxByteStringLengthAsync() {
    return getMaxByteStringLengthNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxByteStringLength (declaration i=12910,"
                            + " owner i=2013) on "
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
  public CompletableFuture<StatusCode> writeMaxByteStringLengthAsync(
      @Nullable UInteger maxByteStringLength) {
    return getMaxByteStringLengthNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxByteStringLength (declaration i=12910,"
                            + " owner i=2013) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(maxByteStringLength));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getMaxByteStringLengthNode() throws UaException {
    return ClientMembers.await(getMaxByteStringLengthNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getMaxByteStringLengthNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxByteStringLength",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:MaxByteStringLength (declaration i=12910, owner"
                + " i=2013)"));
  }

  @Override
  public @Nullable UInteger getMaxSessions() throws UaException {
    PropertyTypeNode node = getMaxSessionsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxSessions (declaration i=24088, owner i=2013)"
              + " on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxSessions(@Nullable UInteger value) throws UaException {
    PropertyTypeNode node = getMaxSessionsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxSessions (declaration i=24088, owner i=2013)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UInteger readMaxSessions() throws UaException {
    return ClientMembers.await(readMaxSessionsAsync(), false);
  }

  @Override
  public void writeMaxSessions(@Nullable UInteger value) throws UaException {
    try {
      StatusCode statusCode = writeMaxSessionsAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UInteger> readMaxSessionsAsync() {
    return getMaxSessionsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxSessions (declaration i=24088, owner"
                            + " i=2013) on "
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
  public CompletableFuture<StatusCode> writeMaxSessionsAsync(@Nullable UInteger maxSessions) {
    return getMaxSessionsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxSessions (declaration i=24088, owner"
                            + " i=2013) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(maxSessions));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getMaxSessionsNode() throws UaException {
    return ClientMembers.await(getMaxSessionsNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getMaxSessionsNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxSessions",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:MaxSessions (declaration i=24088, owner i=2013)"));
  }

  @Override
  public @Nullable UInteger getMaxSubscriptions() throws UaException {
    PropertyTypeNode node = getMaxSubscriptionsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxSubscriptions (declaration i=24089, owner i=2013)"
              + " on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxSubscriptions(@Nullable UInteger value) throws UaException {
    PropertyTypeNode node = getMaxSubscriptionsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxSubscriptions (declaration i=24089, owner i=2013)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UInteger readMaxSubscriptions() throws UaException {
    return ClientMembers.await(readMaxSubscriptionsAsync(), false);
  }

  @Override
  public void writeMaxSubscriptions(@Nullable UInteger value) throws UaException {
    try {
      StatusCode statusCode = writeMaxSubscriptionsAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UInteger> readMaxSubscriptionsAsync() {
    return getMaxSubscriptionsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxSubscriptions (declaration i=24089, owner"
                            + " i=2013) on "
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
  public CompletableFuture<StatusCode> writeMaxSubscriptionsAsync(
      @Nullable UInteger maxSubscriptions) {
    return getMaxSubscriptionsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxSubscriptions (declaration i=24089, owner"
                            + " i=2013) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(maxSubscriptions));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getMaxSubscriptionsNode() throws UaException {
    return ClientMembers.await(getMaxSubscriptionsNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getMaxSubscriptionsNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxSubscriptions",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:MaxSubscriptions (declaration i=24089, owner i=2013)"));
  }

  @Override
  public @Nullable UInteger getMaxMonitoredItems() throws UaException {
    PropertyTypeNode node = getMaxMonitoredItemsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxMonitoredItems (declaration i=24090, owner i=2013)"
              + " on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxMonitoredItems(@Nullable UInteger value) throws UaException {
    PropertyTypeNode node = getMaxMonitoredItemsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxMonitoredItems (declaration i=24090, owner i=2013)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UInteger readMaxMonitoredItems() throws UaException {
    return ClientMembers.await(readMaxMonitoredItemsAsync(), false);
  }

  @Override
  public void writeMaxMonitoredItems(@Nullable UInteger value) throws UaException {
    try {
      StatusCode statusCode = writeMaxMonitoredItemsAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UInteger> readMaxMonitoredItemsAsync() {
    return getMaxMonitoredItemsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxMonitoredItems (declaration i=24090, owner"
                            + " i=2013) on "
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
  public CompletableFuture<StatusCode> writeMaxMonitoredItemsAsync(
      @Nullable UInteger maxMonitoredItems) {
    return getMaxMonitoredItemsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxMonitoredItems (declaration i=24090, owner"
                            + " i=2013) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(maxMonitoredItems));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getMaxMonitoredItemsNode() throws UaException {
    return ClientMembers.await(getMaxMonitoredItemsNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getMaxMonitoredItemsNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxMonitoredItems",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:MaxMonitoredItems (declaration i=24090, owner i=2013)"));
  }

  @Override
  public @Nullable UInteger getMaxSubscriptionsPerSession() throws UaException {
    PropertyTypeNode node = getMaxSubscriptionsPerSessionNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxSubscriptionsPerSession (declaration i=24091, owner"
              + " i=2013) on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxSubscriptionsPerSession(@Nullable UInteger value) throws UaException {
    PropertyTypeNode node = getMaxSubscriptionsPerSessionNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxSubscriptionsPerSession (declaration i=24091, owner"
              + " i=2013) on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UInteger readMaxSubscriptionsPerSession() throws UaException {
    return ClientMembers.await(readMaxSubscriptionsPerSessionAsync(), false);
  }

  @Override
  public void writeMaxSubscriptionsPerSession(@Nullable UInteger value) throws UaException {
    try {
      StatusCode statusCode = writeMaxSubscriptionsPerSessionAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UInteger> readMaxSubscriptionsPerSessionAsync() {
    return getMaxSubscriptionsPerSessionNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxSubscriptionsPerSession (declaration"
                            + " i=24091, owner i=2013) on "
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
  public CompletableFuture<StatusCode> writeMaxSubscriptionsPerSessionAsync(
      @Nullable UInteger maxSubscriptionsPerSession) {
    return getMaxSubscriptionsPerSessionNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxSubscriptionsPerSession (declaration"
                            + " i=24091, owner i=2013) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(maxSubscriptionsPerSession));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getMaxSubscriptionsPerSessionNode() throws UaException {
    return ClientMembers.await(getMaxSubscriptionsPerSessionNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getMaxSubscriptionsPerSessionNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxSubscriptionsPerSession",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:MaxSubscriptionsPerSession (declaration i=24091, owner"
                + " i=2013)"));
  }

  @Override
  public @Nullable UInteger getMaxMonitoredItemsPerSubscription() throws UaException {
    PropertyTypeNode node = getMaxMonitoredItemsPerSubscriptionNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxMonitoredItemsPerSubscription (declaration i=24103,"
              + " owner i=2013) on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxMonitoredItemsPerSubscription(@Nullable UInteger value) throws UaException {
    PropertyTypeNode node = getMaxMonitoredItemsPerSubscriptionNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxMonitoredItemsPerSubscription (declaration i=24103,"
              + " owner i=2013) on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UInteger readMaxMonitoredItemsPerSubscription() throws UaException {
    return ClientMembers.await(readMaxMonitoredItemsPerSubscriptionAsync(), false);
  }

  @Override
  public void writeMaxMonitoredItemsPerSubscription(@Nullable UInteger value) throws UaException {
    try {
      StatusCode statusCode = writeMaxMonitoredItemsPerSubscriptionAsync(value).get();
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
      readMaxMonitoredItemsPerSubscriptionAsync() {
    return getMaxMonitoredItemsPerSubscriptionNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxMonitoredItemsPerSubscription (declaration"
                            + " i=24103, owner i=2013) on "
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
  public CompletableFuture<StatusCode> writeMaxMonitoredItemsPerSubscriptionAsync(
      @Nullable UInteger maxMonitoredItemsPerSubscription) {
    return getMaxMonitoredItemsPerSubscriptionNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxMonitoredItemsPerSubscription (declaration"
                            + " i=24103, owner i=2013) on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(new Variant(maxMonitoredItemsPerSubscription));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getMaxMonitoredItemsPerSubscriptionNode() throws UaException {
    return ClientMembers.await(getMaxMonitoredItemsPerSubscriptionNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getMaxMonitoredItemsPerSubscriptionNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxMonitoredItemsPerSubscription",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:MaxMonitoredItemsPerSubscription (declaration i=24103,"
                + " owner i=2013)"));
  }

  @Override
  public @Nullable UInteger getMaxSelectClauseParameters() throws UaException {
    PropertyTypeNode node = getMaxSelectClauseParametersNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxSelectClauseParameters (declaration i=24092, owner"
              + " i=2013) on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxSelectClauseParameters(@Nullable UInteger value) throws UaException {
    PropertyTypeNode node = getMaxSelectClauseParametersNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxSelectClauseParameters (declaration i=24092, owner"
              + " i=2013) on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UInteger readMaxSelectClauseParameters() throws UaException {
    return ClientMembers.await(readMaxSelectClauseParametersAsync(), false);
  }

  @Override
  public void writeMaxSelectClauseParameters(@Nullable UInteger value) throws UaException {
    try {
      StatusCode statusCode = writeMaxSelectClauseParametersAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UInteger> readMaxSelectClauseParametersAsync() {
    return getMaxSelectClauseParametersNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxSelectClauseParameters (declaration"
                            + " i=24092, owner i=2013) on "
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
  public CompletableFuture<StatusCode> writeMaxSelectClauseParametersAsync(
      @Nullable UInteger maxSelectClauseParameters) {
    return getMaxSelectClauseParametersNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxSelectClauseParameters (declaration"
                            + " i=24092, owner i=2013) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(maxSelectClauseParameters));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getMaxSelectClauseParametersNode() throws UaException {
    return ClientMembers.await(getMaxSelectClauseParametersNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getMaxSelectClauseParametersNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxSelectClauseParameters",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:MaxSelectClauseParameters (declaration i=24092, owner"
                + " i=2013)"));
  }

  @Override
  public @Nullable UInteger getMaxWhereClauseParameters() throws UaException {
    PropertyTypeNode node = getMaxWhereClauseParametersNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxWhereClauseParameters (declaration i=24093, owner"
              + " i=2013) on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxWhereClauseParameters(@Nullable UInteger value) throws UaException {
    PropertyTypeNode node = getMaxWhereClauseParametersNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxWhereClauseParameters (declaration i=24093, owner"
              + " i=2013) on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UInteger readMaxWhereClauseParameters() throws UaException {
    return ClientMembers.await(readMaxWhereClauseParametersAsync(), false);
  }

  @Override
  public void writeMaxWhereClauseParameters(@Nullable UInteger value) throws UaException {
    try {
      StatusCode statusCode = writeMaxWhereClauseParametersAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UInteger> readMaxWhereClauseParametersAsync() {
    return getMaxWhereClauseParametersNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxWhereClauseParameters (declaration"
                            + " i=24093, owner i=2013) on "
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
  public CompletableFuture<StatusCode> writeMaxWhereClauseParametersAsync(
      @Nullable UInteger maxWhereClauseParameters) {
    return getMaxWhereClauseParametersNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxWhereClauseParameters (declaration"
                            + " i=24093, owner i=2013) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(maxWhereClauseParameters));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getMaxWhereClauseParametersNode() throws UaException {
    return ClientMembers.await(getMaxWhereClauseParametersNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getMaxWhereClauseParametersNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxWhereClauseParameters",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:MaxWhereClauseParameters (declaration i=24093, owner"
                + " i=2013)"));
  }

  @Override
  public @Nullable UInteger getMaxMonitoredItemsQueueSize() throws UaException {
    PropertyTypeNode node = getMaxMonitoredItemsQueueSizeNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxMonitoredItemsQueueSize (declaration i=31770, owner"
              + " i=2013) on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxMonitoredItemsQueueSize(@Nullable UInteger value) throws UaException {
    PropertyTypeNode node = getMaxMonitoredItemsQueueSizeNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxMonitoredItemsQueueSize (declaration i=31770, owner"
              + " i=2013) on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UInteger readMaxMonitoredItemsQueueSize() throws UaException {
    return ClientMembers.await(readMaxMonitoredItemsQueueSizeAsync(), false);
  }

  @Override
  public void writeMaxMonitoredItemsQueueSize(@Nullable UInteger value) throws UaException {
    try {
      StatusCode statusCode = writeMaxMonitoredItemsQueueSizeAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UInteger> readMaxMonitoredItemsQueueSizeAsync() {
    return getMaxMonitoredItemsQueueSizeNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxMonitoredItemsQueueSize (declaration"
                            + " i=31770, owner i=2013) on "
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
  public CompletableFuture<StatusCode> writeMaxMonitoredItemsQueueSizeAsync(
      @Nullable UInteger maxMonitoredItemsQueueSize) {
    return getMaxMonitoredItemsQueueSizeNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MaxMonitoredItemsQueueSize (declaration"
                            + " i=31770, owner i=2013) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(maxMonitoredItemsQueueSize));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getMaxMonitoredItemsQueueSizeNode() throws UaException {
    return ClientMembers.await(getMaxMonitoredItemsQueueSizeNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getMaxMonitoredItemsQueueSizeNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxMonitoredItemsQueueSize",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:MaxMonitoredItemsQueueSize (declaration i=31770, owner"
                + " i=2013)"));
  }

  @Override
  public @Nullable QualifiedName @Nullable [] getConformanceUnits() throws UaException {
    PropertyTypeNode node = getConformanceUnitsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ConformanceUnits (declaration i=24094, owner i=2013)"
              + " on "
              + getNodeId());
    }
    return (QualifiedName[]) node.getValue().getValue().getValue();
  }

  @Override
  public void setConformanceUnits(@Nullable QualifiedName @Nullable [] value) throws UaException {
    PropertyTypeNode node = getConformanceUnitsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ConformanceUnits (declaration i=24094, owner i=2013)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable QualifiedName @Nullable [] readConformanceUnits() throws UaException {
    return ClientMembers.await(readConformanceUnitsAsync(), false);
  }

  @Override
  public void writeConformanceUnits(@Nullable QualifiedName @Nullable [] value) throws UaException {
    try {
      StatusCode statusCode = writeConformanceUnitsAsync(value).get();
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
  public CompletableFuture<? extends @Nullable QualifiedName @Nullable []>
      readConformanceUnitsAsync() {
    return getConformanceUnitsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ConformanceUnits (declaration i=24094, owner"
                            + " i=2013) on "
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
                return (QualifiedName[]) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeConformanceUnitsAsync(
      @Nullable QualifiedName @Nullable [] conformanceUnits) {
    return getConformanceUnitsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ConformanceUnits (declaration i=24094, owner"
                            + " i=2013) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(conformanceUnits));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getConformanceUnitsNode() throws UaException {
    return ClientMembers.await(getConformanceUnitsNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getConformanceUnitsNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ConformanceUnits",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:ConformanceUnits (declaration i=24094, owner i=2013)"));
  }

  @Override
  public @Nullable OperationLimitsTypeNode getOperationLimitsNode() throws UaException {
    return ClientMembers.await(getOperationLimitsNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable OperationLimitsTypeNode>
      getOperationLimitsNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        OperationLimitsTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "OperationLimits",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            true,
            "http://opcfoundation.org/UA/:OperationLimits (declaration i=11551, owner i=2013)"));
  }

  @Override
  public FolderTypeNode getModellingRulesNode() throws UaException {
    return ClientMembers.await(getModellingRulesNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends FolderTypeNode> getModellingRulesNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        FolderTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ModellingRules",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            false,
            "http://opcfoundation.org/UA/:ModellingRules (declaration i=2019, owner i=2013)"));
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
            "http://opcfoundation.org/UA/:AggregateFunctions (declaration i=2754, owner i=2013)"));
  }

  @Override
  public @Nullable RoleSetTypeNode getRoleSetNode() throws UaException {
    return ClientMembers.await(getRoleSetNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable RoleSetTypeNode> getRoleSetNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        RoleSetTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "RoleSet",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            true,
            "http://opcfoundation.org/UA/:RoleSet (declaration i=16295, owner i=2013)"));
  }
}

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

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.core.ValueRanks;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.AxisScaleEnumeration;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessLevelExType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowsePath;
import org.eclipse.milo.opcua.stack.core.types.structured.EUInformation;
import org.eclipse.milo.opcua.stack.core.types.structured.Range;
import org.eclipse.milo.opcua.stack.core.types.structured.RelativePath;
import org.eclipse.milo.opcua.stack.core.types.structured.RelativePathElement;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.ArrayUtil;
import org.jspecify.annotations.Nullable;

public class ArrayItemTypeNode extends DataItemTypeNode implements ArrayItemType {
  public ArrayItemTypeNode(
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
  public @Nullable Range getInstrumentRange() throws UaException {
    PropertyTypeNode node = getInstrumentRangeNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:InstrumentRange (declaration i=12024, owner i=12021)"
              + " on "
              + getNodeId());
    }
    return (Range)
        decodeValue(node.getValue().getValue().getValue(), Range.class, ValueRanks.Scalar);
  }

  @Override
  public void setInstrumentRange(@Nullable Range value) throws UaException {
    PropertyTypeNode node = getInstrumentRangeNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:InstrumentRange (declaration i=12024, owner i=12021)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(encodeValue(value, Range.class, ValueRanks.Scalar)));
  }

  @Override
  public @Nullable Range readInstrumentRange() throws UaException {
    try {
      return readInstrumentRangeAsync().get();
    } catch (ExecutionException e) {
      Throwable cause = e.getCause();
      while (cause instanceof CompletionException || cause instanceof ExecutionException) {
        cause = cause.getCause();
      }
      if (cause instanceof UaException failure) {
        throw failure;
      }
      throw new UaException(cause);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public void writeInstrumentRange(@Nullable Range value) throws UaException {
    try {
      StatusCode statusCode = writeInstrumentRangeAsync(value).get();
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
  public CompletableFuture<? extends @Nullable Range> readInstrumentRangeAsync() {
    return getInstrumentRangeNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:InstrumentRange (declaration i=12024, owner"
                            + " i=12021) on "
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
                return (Range) decodeValue(v.getValue().getValue(), Range.class, ValueRanks.Scalar);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeInstrumentRangeAsync(@Nullable Range instrumentRange) {
    return getInstrumentRangeNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:InstrumentRange (declaration i=12024, owner"
                            + " i=12021) on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(encodeValue(instrumentRange, Range.class, ValueRanks.Scalar)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getInstrumentRangeNode() throws UaException {
    try {
      return getInstrumentRangeNodeAsync().get();
    } catch (ExecutionException e) {
      Throwable cause = e.getCause();
      while (cause instanceof CompletionException || cause instanceof ExecutionException) {
        cause = cause.getCause();
      }
      if (cause instanceof UaException failure) {
        throw failure;
      }
      throw new UaException(cause);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getInstrumentRangeNodeAsync() {
    CompletableFuture<PropertyTypeNode> result = new CompletableFuture<>();
    try {
      CompletableFuture<NodeId> lookup = CompletableFuture.completedFuture(getNodeId());
      CompletableFuture<UaNode> hop0 =
          lookup.thenCompose(
              parent -> {
                NodeId parentId = parent;
                if (result.isCancelled()) {
                  return CompletableFuture.failedFuture(new CancellationException());
                }
                if (parentId == null) {
                  return CompletableFuture.completedFuture(null);
                }
                CompletableFuture<Void> namespaceReady;
                if (client.getNamespaceTable().getIndex("http://opcfoundation.org/UA/") == null
                    || client.getNamespaceTable().getIndex("http://opcfoundation.org/UA/")
                        == null) {
                  namespaceReady = client.readNamespaceTableAsync().thenApply(ignored -> null);
                } else {
                  namespaceReady = CompletableFuture.completedFuture(null);
                }
                return namespaceReady.thenCompose(
                    ignored -> {
                      if (result.isCancelled()) {
                        return CompletableFuture.failedFuture(new CancellationException());
                      }
                      var namespaceIndex =
                          client.getNamespaceTable().getIndex("http://opcfoundation.org/UA/");
                      var referenceId =
                          ExpandedNodeId.parse("i=46").toNodeId(client.getNamespaceTable());
                      if (namespaceIndex == null || referenceId.isEmpty()) {
                        return CompletableFuture.failedFuture(
                            new UaException(
                                StatusCodes.Bad_NodeIdInvalid,
                                "http://opcfoundation.org/UA/:InstrumentRange (declaration i=12024,"
                                    + " owner i=12021) on "
                                    + getNodeId()));
                      }
                      var browsePath =
                          new BrowsePath(
                              parentId,
                              new RelativePath(
                                  new RelativePathElement[] {
                                    new RelativePathElement(
                                        referenceId.orElseThrow(),
                                        false,
                                        true,
                                        new QualifiedName(namespaceIndex, "InstrumentRange"))
                                  }));
                      return client
                          .translateBrowsePathsAsync(List.of(browsePath))
                          .thenCompose(
                              response -> {
                                if (result.isCancelled()) {
                                  return CompletableFuture.failedFuture(
                                      new CancellationException());
                                }
                                var results = response == null ? null : response.getResults();
                                if (results == null
                                    || results.length != 1
                                    || results[0] == null
                                    || results[0].getStatusCode() == null) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_UnexpectedError,
                                          "http://opcfoundation.org/UA/:InstrumentRange"
                                              + " (declaration i=12024, owner i=12021) on "
                                              + getNodeId()));
                                }
                                var operation = results[0];
                                if (operation.getStatusCode().getValue()
                                    == StatusCodes.Bad_NoMatch) {
                                  return CompletableFuture.completedFuture(null);
                                }
                                if (!operation.getStatusCode().isGood()) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          operation.getStatusCode(),
                                          "http://opcfoundation.org/UA/:InstrumentRange"
                                              + " (declaration i=12024, owner i=12021)"));
                                }
                                var targets = operation.getTargets();
                                if (targets == null || targets.length == 0) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_UnexpectedError,
                                          "http://opcfoundation.org/UA/:InstrumentRange"
                                              + " (declaration i=12024, owner i=12021) on "
                                              + getNodeId()));
                                }
                                var identities = new ArrayList<CompletableFuture<NodeId>>();
                                for (var target : targets) {
                                  if (target == null
                                      || target.getTargetId() == null
                                      || target.getRemainingPathIndex() == null
                                      || target.getRemainingPathIndex().longValue()
                                          != 0xffffffffL) {
                                    return CompletableFuture.failedFuture(
                                        new UaException(
                                            StatusCodes.Bad_UnexpectedError,
                                            "http://opcfoundation.org/UA/:InstrumentRange"
                                                + " (declaration i=12024, owner i=12021) on "
                                                + getNodeId()));
                                  }
                                  if (!target.getTargetId().isLocal()) {
                                    return CompletableFuture.failedFuture(
                                        new UaException(
                                            StatusCodes.Bad_NotSupported,
                                            "http://opcfoundation.org/UA/:InstrumentRange"
                                                + " (declaration i=12024, owner i=12021) on "
                                                + getNodeId()));
                                  }
                                  if (result.isCancelled()) {
                                    return CompletableFuture.failedFuture(
                                        new CancellationException());
                                  }
                                  var localTarget =
                                      target.getTargetId().toNodeId(client.getNamespaceTable());
                                  if (localTarget.isPresent()) {
                                    identities.add(
                                        CompletableFuture.completedFuture(
                                            localTarget.orElseThrow()));
                                  } else {
                                    identities.add(
                                        client
                                            .readNamespaceTableAsync()
                                            .thenCompose(
                                                namespaceTable -> {
                                                  var resolvedTarget =
                                                      target.getTargetId().toNodeId(namespaceTable);
                                                  if (resolvedTarget.isEmpty()) {
                                                    return CompletableFuture.failedFuture(
                                                        new UaException(
                                                            StatusCodes.Bad_NodeIdInvalid,
                                                            "http://opcfoundation.org/UA/:InstrumentRange"
                                                                + " (declaration i=12024, owner"
                                                                + " i=12021) on "
                                                                + getNodeId()));
                                                  }
                                                  return CompletableFuture.completedFuture(
                                                      resolvedTarget.orElseThrow());
                                                }));
                                  }
                                }
                                return CompletableFuture.allOf(
                                        identities.toArray(CompletableFuture[]::new))
                                    .thenCompose(
                                        ready -> {
                                          if (result.isCancelled()) {
                                            return CompletableFuture.failedFuture(
                                                new CancellationException());
                                          }
                                          var unique = new LinkedHashSet<NodeId>();
                                          identities.forEach(
                                              identity -> unique.add(identity.join()));
                                          if (unique.stream().anyMatch(NodeId::isNull)) {
                                            return CompletableFuture.failedFuture(
                                                new UaException(
                                                    StatusCodes.Bad_NodeIdInvalid,
                                                    "http://opcfoundation.org/UA/:InstrumentRange"
                                                        + " (declaration i=12024, owner i=12021) on"
                                                        + " "
                                                        + getNodeId()));
                                          }
                                          if (unique.size() != 1) {
                                            return CompletableFuture.failedFuture(
                                                new UaException(
                                                    StatusCodes.Bad_TooManyMatches,
                                                    "http://opcfoundation.org/UA/:InstrumentRange"
                                                        + " (declaration i=12024, owner i=12021) on"
                                                        + " "
                                                        + getNodeId()));
                                          }
                                          return client
                                              .getAddressSpace()
                                              .getNodeAsync(unique.iterator().next())
                                              .thenCompose(
                                                  node -> {
                                                    if (node == null
                                                        || node.getNodeClass()
                                                            != NodeClass.Variable) {
                                                      return CompletableFuture.failedFuture(
                                                          new UaException(
                                                              StatusCodes.Bad_NodeClassInvalid,
                                                              "http://opcfoundation.org/UA/:InstrumentRange"
                                                                  + " (declaration i=12024, owner"
                                                                  + " i=12021) on "
                                                                  + getNodeId()));
                                                    }
                                                    return CompletableFuture.completedFuture(node);
                                                  });
                                        });
                              });
                    });
              });
      hop0.whenComplete(
          (node, failure) -> {
            if (failure != null) {
              result.completeExceptionally(failure);
            } else if (node != null && !(node instanceof PropertyTypeNode)) {
              result.completeExceptionally(
                  new UaException(
                      StatusCodes.Bad_TypeMismatch,
                      "http://opcfoundation.org/UA/:InstrumentRange (declaration i=12024, owner"
                          + " i=12021)"));
            } else {
              result.complete((PropertyTypeNode) node);
            }
          });
    } catch (Exception e) {
      result.completeExceptionally(e);
    }
    return result;
  }

  @Override
  public @Nullable Range getEuRange() throws UaException {
    PropertyTypeNode node = getEuRangeNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:EURange (declaration i=12025, owner i=12021)"
              + " on "
              + getNodeId());
    }
    return (Range)
        decodeValue(node.getValue().getValue().getValue(), Range.class, ValueRanks.Scalar);
  }

  @Override
  public void setEuRange(@Nullable Range value) throws UaException {
    PropertyTypeNode node = getEuRangeNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:EURange (declaration i=12025, owner i=12021)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(encodeValue(value, Range.class, ValueRanks.Scalar)));
  }

  @Override
  public @Nullable Range readEuRange() throws UaException {
    try {
      return readEuRangeAsync().get();
    } catch (ExecutionException e) {
      Throwable cause = e.getCause();
      while (cause instanceof CompletionException || cause instanceof ExecutionException) {
        cause = cause.getCause();
      }
      if (cause instanceof UaException failure) {
        throw failure;
      }
      throw new UaException(cause);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public void writeEuRange(@Nullable Range value) throws UaException {
    try {
      StatusCode statusCode = writeEuRangeAsync(value).get();
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
  public CompletableFuture<? extends @Nullable Range> readEuRangeAsync() {
    return getEuRangeNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:EURange (declaration i=12025, owner i=12021)"
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
                return (Range) decodeValue(v.getValue().getValue(), Range.class, ValueRanks.Scalar);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeEuRangeAsync(@Nullable Range euRange) {
    return getEuRangeNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:EURange (declaration i=12025, owner i=12021)"
                            + " on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(encodeValue(euRange, Range.class, ValueRanks.Scalar)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getEuRangeNode() throws UaException {
    try {
      return getEuRangeNodeAsync().get();
    } catch (ExecutionException e) {
      Throwable cause = e.getCause();
      while (cause instanceof CompletionException || cause instanceof ExecutionException) {
        cause = cause.getCause();
      }
      if (cause instanceof UaException failure) {
        throw failure;
      }
      throw new UaException(cause);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getEuRangeNodeAsync() {
    CompletableFuture<PropertyTypeNode> result = new CompletableFuture<>();
    try {
      CompletableFuture<NodeId> lookup = CompletableFuture.completedFuture(getNodeId());
      CompletableFuture<UaNode> hop0 =
          lookup.thenCompose(
              parent -> {
                NodeId parentId = parent;
                if (result.isCancelled()) {
                  return CompletableFuture.failedFuture(new CancellationException());
                }
                if (parentId == null) {
                  return CompletableFuture.completedFuture(null);
                }
                CompletableFuture<Void> namespaceReady;
                if (client.getNamespaceTable().getIndex("http://opcfoundation.org/UA/") == null
                    || client.getNamespaceTable().getIndex("http://opcfoundation.org/UA/")
                        == null) {
                  namespaceReady = client.readNamespaceTableAsync().thenApply(ignored -> null);
                } else {
                  namespaceReady = CompletableFuture.completedFuture(null);
                }
                return namespaceReady.thenCompose(
                    ignored -> {
                      if (result.isCancelled()) {
                        return CompletableFuture.failedFuture(new CancellationException());
                      }
                      var namespaceIndex =
                          client.getNamespaceTable().getIndex("http://opcfoundation.org/UA/");
                      var referenceId =
                          ExpandedNodeId.parse("i=46").toNodeId(client.getNamespaceTable());
                      if (namespaceIndex == null || referenceId.isEmpty()) {
                        return CompletableFuture.failedFuture(
                            new UaException(
                                StatusCodes.Bad_NodeIdInvalid,
                                "http://opcfoundation.org/UA/:EURange (declaration i=12025, owner"
                                    + " i=12021) on "
                                    + getNodeId()));
                      }
                      var browsePath =
                          new BrowsePath(
                              parentId,
                              new RelativePath(
                                  new RelativePathElement[] {
                                    new RelativePathElement(
                                        referenceId.orElseThrow(),
                                        false,
                                        true,
                                        new QualifiedName(namespaceIndex, "EURange"))
                                  }));
                      return client
                          .translateBrowsePathsAsync(List.of(browsePath))
                          .thenCompose(
                              response -> {
                                if (result.isCancelled()) {
                                  return CompletableFuture.failedFuture(
                                      new CancellationException());
                                }
                                var results = response == null ? null : response.getResults();
                                if (results == null
                                    || results.length != 1
                                    || results[0] == null
                                    || results[0].getStatusCode() == null) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_UnexpectedError,
                                          "http://opcfoundation.org/UA/:EURange (declaration"
                                              + " i=12025, owner i=12021) on "
                                              + getNodeId()));
                                }
                                var operation = results[0];
                                if (operation.getStatusCode().getValue()
                                    == StatusCodes.Bad_NoMatch) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_NotFound,
                                          "http://opcfoundation.org/UA/:EURange (declaration"
                                              + " i=12025, owner i=12021) on "
                                              + getNodeId()));
                                }
                                if (!operation.getStatusCode().isGood()) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          operation.getStatusCode(),
                                          "http://opcfoundation.org/UA/:EURange (declaration"
                                              + " i=12025, owner i=12021)"));
                                }
                                var targets = operation.getTargets();
                                if (targets == null || targets.length == 0) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_UnexpectedError,
                                          "http://opcfoundation.org/UA/:EURange (declaration"
                                              + " i=12025, owner i=12021) on "
                                              + getNodeId()));
                                }
                                var identities = new ArrayList<CompletableFuture<NodeId>>();
                                for (var target : targets) {
                                  if (target == null
                                      || target.getTargetId() == null
                                      || target.getRemainingPathIndex() == null
                                      || target.getRemainingPathIndex().longValue()
                                          != 0xffffffffL) {
                                    return CompletableFuture.failedFuture(
                                        new UaException(
                                            StatusCodes.Bad_UnexpectedError,
                                            "http://opcfoundation.org/UA/:EURange (declaration"
                                                + " i=12025, owner i=12021) on "
                                                + getNodeId()));
                                  }
                                  if (!target.getTargetId().isLocal()) {
                                    return CompletableFuture.failedFuture(
                                        new UaException(
                                            StatusCodes.Bad_NotSupported,
                                            "http://opcfoundation.org/UA/:EURange (declaration"
                                                + " i=12025, owner i=12021) on "
                                                + getNodeId()));
                                  }
                                  if (result.isCancelled()) {
                                    return CompletableFuture.failedFuture(
                                        new CancellationException());
                                  }
                                  var localTarget =
                                      target.getTargetId().toNodeId(client.getNamespaceTable());
                                  if (localTarget.isPresent()) {
                                    identities.add(
                                        CompletableFuture.completedFuture(
                                            localTarget.orElseThrow()));
                                  } else {
                                    identities.add(
                                        client
                                            .readNamespaceTableAsync()
                                            .thenCompose(
                                                namespaceTable -> {
                                                  var resolvedTarget =
                                                      target.getTargetId().toNodeId(namespaceTable);
                                                  if (resolvedTarget.isEmpty()) {
                                                    return CompletableFuture.failedFuture(
                                                        new UaException(
                                                            StatusCodes.Bad_NodeIdInvalid,
                                                            "http://opcfoundation.org/UA/:EURange"
                                                                + " (declaration i=12025, owner"
                                                                + " i=12021) on "
                                                                + getNodeId()));
                                                  }
                                                  return CompletableFuture.completedFuture(
                                                      resolvedTarget.orElseThrow());
                                                }));
                                  }
                                }
                                return CompletableFuture.allOf(
                                        identities.toArray(CompletableFuture[]::new))
                                    .thenCompose(
                                        ready -> {
                                          if (result.isCancelled()) {
                                            return CompletableFuture.failedFuture(
                                                new CancellationException());
                                          }
                                          var unique = new LinkedHashSet<NodeId>();
                                          identities.forEach(
                                              identity -> unique.add(identity.join()));
                                          if (unique.stream().anyMatch(NodeId::isNull)) {
                                            return CompletableFuture.failedFuture(
                                                new UaException(
                                                    StatusCodes.Bad_NodeIdInvalid,
                                                    "http://opcfoundation.org/UA/:EURange"
                                                        + " (declaration i=12025, owner i=12021) on"
                                                        + " "
                                                        + getNodeId()));
                                          }
                                          if (unique.size() != 1) {
                                            return CompletableFuture.failedFuture(
                                                new UaException(
                                                    StatusCodes.Bad_TooManyMatches,
                                                    "http://opcfoundation.org/UA/:EURange"
                                                        + " (declaration i=12025, owner i=12021) on"
                                                        + " "
                                                        + getNodeId()));
                                          }
                                          return client
                                              .getAddressSpace()
                                              .getNodeAsync(unique.iterator().next())
                                              .thenCompose(
                                                  node -> {
                                                    if (node == null
                                                        || node.getNodeClass()
                                                            != NodeClass.Variable) {
                                                      return CompletableFuture.failedFuture(
                                                          new UaException(
                                                              StatusCodes.Bad_NodeClassInvalid,
                                                              "http://opcfoundation.org/UA/:EURange"
                                                                  + " (declaration i=12025, owner"
                                                                  + " i=12021) on "
                                                                  + getNodeId()));
                                                    }
                                                    return CompletableFuture.completedFuture(node);
                                                  });
                                        });
                              });
                    });
              });
      hop0.whenComplete(
          (node, failure) -> {
            if (failure != null) {
              result.completeExceptionally(failure);
            } else if (node != null && !(node instanceof PropertyTypeNode)) {
              result.completeExceptionally(
                  new UaException(
                      StatusCodes.Bad_TypeMismatch,
                      "http://opcfoundation.org/UA/:EURange (declaration i=12025, owner i=12021)"));
            } else {
              result.complete((PropertyTypeNode) node);
            }
          });
    } catch (Exception e) {
      result.completeExceptionally(e);
    }
    return result;
  }

  @Override
  public @Nullable EUInformation getEngineeringUnitsProperty() throws UaException {
    PropertyTypeNode node = getEngineeringUnitsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:EngineeringUnits (declaration i=12026, owner i=12021)"
              + " on "
              + getNodeId());
    }
    return (EUInformation)
        decodeValue(node.getValue().getValue().getValue(), EUInformation.class, ValueRanks.Scalar);
  }

  @Override
  public void setEngineeringUnitsProperty(@Nullable EUInformation value) throws UaException {
    PropertyTypeNode node = getEngineeringUnitsNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:EngineeringUnits (declaration i=12026, owner i=12021)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(encodeValue(value, EUInformation.class, ValueRanks.Scalar)));
  }

  @Override
  public @Nullable EUInformation readEngineeringUnitsProperty() throws UaException {
    try {
      return readEngineeringUnitsPropertyAsync().get();
    } catch (ExecutionException e) {
      Throwable cause = e.getCause();
      while (cause instanceof CompletionException || cause instanceof ExecutionException) {
        cause = cause.getCause();
      }
      if (cause instanceof UaException failure) {
        throw failure;
      }
      throw new UaException(cause);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public void writeEngineeringUnitsProperty(@Nullable EUInformation value) throws UaException {
    try {
      StatusCode statusCode = writeEngineeringUnitsPropertyAsync(value).get();
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
  public CompletableFuture<? extends @Nullable EUInformation> readEngineeringUnitsPropertyAsync() {
    return getEngineeringUnitsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:EngineeringUnits (declaration i=12026, owner"
                            + " i=12021) on "
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
                return (EUInformation)
                    decodeValue(v.getValue().getValue(), EUInformation.class, ValueRanks.Scalar);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeEngineeringUnitsPropertyAsync(
      @Nullable EUInformation engineeringUnits) {
    return getEngineeringUnitsNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:EngineeringUnits (declaration i=12026, owner"
                            + " i=12021) on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(
                            encodeValue(engineeringUnits, EUInformation.class, ValueRanks.Scalar)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getEngineeringUnitsNode() throws UaException {
    try {
      return getEngineeringUnitsNodeAsync().get();
    } catch (ExecutionException e) {
      Throwable cause = e.getCause();
      while (cause instanceof CompletionException || cause instanceof ExecutionException) {
        cause = cause.getCause();
      }
      if (cause instanceof UaException failure) {
        throw failure;
      }
      throw new UaException(cause);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getEngineeringUnitsNodeAsync() {
    CompletableFuture<PropertyTypeNode> result = new CompletableFuture<>();
    try {
      CompletableFuture<NodeId> lookup = CompletableFuture.completedFuture(getNodeId());
      CompletableFuture<UaNode> hop0 =
          lookup.thenCompose(
              parent -> {
                NodeId parentId = parent;
                if (result.isCancelled()) {
                  return CompletableFuture.failedFuture(new CancellationException());
                }
                if (parentId == null) {
                  return CompletableFuture.completedFuture(null);
                }
                CompletableFuture<Void> namespaceReady;
                if (client.getNamespaceTable().getIndex("http://opcfoundation.org/UA/") == null
                    || client.getNamespaceTable().getIndex("http://opcfoundation.org/UA/")
                        == null) {
                  namespaceReady = client.readNamespaceTableAsync().thenApply(ignored -> null);
                } else {
                  namespaceReady = CompletableFuture.completedFuture(null);
                }
                return namespaceReady.thenCompose(
                    ignored -> {
                      if (result.isCancelled()) {
                        return CompletableFuture.failedFuture(new CancellationException());
                      }
                      var namespaceIndex =
                          client.getNamespaceTable().getIndex("http://opcfoundation.org/UA/");
                      var referenceId =
                          ExpandedNodeId.parse("i=46").toNodeId(client.getNamespaceTable());
                      if (namespaceIndex == null || referenceId.isEmpty()) {
                        return CompletableFuture.failedFuture(
                            new UaException(
                                StatusCodes.Bad_NodeIdInvalid,
                                "http://opcfoundation.org/UA/:EngineeringUnits (declaration"
                                    + " i=12026, owner i=12021) on "
                                    + getNodeId()));
                      }
                      var browsePath =
                          new BrowsePath(
                              parentId,
                              new RelativePath(
                                  new RelativePathElement[] {
                                    new RelativePathElement(
                                        referenceId.orElseThrow(),
                                        false,
                                        true,
                                        new QualifiedName(namespaceIndex, "EngineeringUnits"))
                                  }));
                      return client
                          .translateBrowsePathsAsync(List.of(browsePath))
                          .thenCompose(
                              response -> {
                                if (result.isCancelled()) {
                                  return CompletableFuture.failedFuture(
                                      new CancellationException());
                                }
                                var results = response == null ? null : response.getResults();
                                if (results == null
                                    || results.length != 1
                                    || results[0] == null
                                    || results[0].getStatusCode() == null) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_UnexpectedError,
                                          "http://opcfoundation.org/UA/:EngineeringUnits"
                                              + " (declaration i=12026, owner i=12021) on "
                                              + getNodeId()));
                                }
                                var operation = results[0];
                                if (operation.getStatusCode().getValue()
                                    == StatusCodes.Bad_NoMatch) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_NotFound,
                                          "http://opcfoundation.org/UA/:EngineeringUnits"
                                              + " (declaration i=12026, owner i=12021) on "
                                              + getNodeId()));
                                }
                                if (!operation.getStatusCode().isGood()) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          operation.getStatusCode(),
                                          "http://opcfoundation.org/UA/:EngineeringUnits"
                                              + " (declaration i=12026, owner i=12021)"));
                                }
                                var targets = operation.getTargets();
                                if (targets == null || targets.length == 0) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_UnexpectedError,
                                          "http://opcfoundation.org/UA/:EngineeringUnits"
                                              + " (declaration i=12026, owner i=12021) on "
                                              + getNodeId()));
                                }
                                var identities = new ArrayList<CompletableFuture<NodeId>>();
                                for (var target : targets) {
                                  if (target == null
                                      || target.getTargetId() == null
                                      || target.getRemainingPathIndex() == null
                                      || target.getRemainingPathIndex().longValue()
                                          != 0xffffffffL) {
                                    return CompletableFuture.failedFuture(
                                        new UaException(
                                            StatusCodes.Bad_UnexpectedError,
                                            "http://opcfoundation.org/UA/:EngineeringUnits"
                                                + " (declaration i=12026, owner i=12021) on "
                                                + getNodeId()));
                                  }
                                  if (!target.getTargetId().isLocal()) {
                                    return CompletableFuture.failedFuture(
                                        new UaException(
                                            StatusCodes.Bad_NotSupported,
                                            "http://opcfoundation.org/UA/:EngineeringUnits"
                                                + " (declaration i=12026, owner i=12021) on "
                                                + getNodeId()));
                                  }
                                  if (result.isCancelled()) {
                                    return CompletableFuture.failedFuture(
                                        new CancellationException());
                                  }
                                  var localTarget =
                                      target.getTargetId().toNodeId(client.getNamespaceTable());
                                  if (localTarget.isPresent()) {
                                    identities.add(
                                        CompletableFuture.completedFuture(
                                            localTarget.orElseThrow()));
                                  } else {
                                    identities.add(
                                        client
                                            .readNamespaceTableAsync()
                                            .thenCompose(
                                                namespaceTable -> {
                                                  var resolvedTarget =
                                                      target.getTargetId().toNodeId(namespaceTable);
                                                  if (resolvedTarget.isEmpty()) {
                                                    return CompletableFuture.failedFuture(
                                                        new UaException(
                                                            StatusCodes.Bad_NodeIdInvalid,
                                                            "http://opcfoundation.org/UA/:EngineeringUnits"
                                                                + " (declaration i=12026, owner"
                                                                + " i=12021) on "
                                                                + getNodeId()));
                                                  }
                                                  return CompletableFuture.completedFuture(
                                                      resolvedTarget.orElseThrow());
                                                }));
                                  }
                                }
                                return CompletableFuture.allOf(
                                        identities.toArray(CompletableFuture[]::new))
                                    .thenCompose(
                                        ready -> {
                                          if (result.isCancelled()) {
                                            return CompletableFuture.failedFuture(
                                                new CancellationException());
                                          }
                                          var unique = new LinkedHashSet<NodeId>();
                                          identities.forEach(
                                              identity -> unique.add(identity.join()));
                                          if (unique.stream().anyMatch(NodeId::isNull)) {
                                            return CompletableFuture.failedFuture(
                                                new UaException(
                                                    StatusCodes.Bad_NodeIdInvalid,
                                                    "http://opcfoundation.org/UA/:EngineeringUnits"
                                                        + " (declaration i=12026, owner i=12021) on"
                                                        + " "
                                                        + getNodeId()));
                                          }
                                          if (unique.size() != 1) {
                                            return CompletableFuture.failedFuture(
                                                new UaException(
                                                    StatusCodes.Bad_TooManyMatches,
                                                    "http://opcfoundation.org/UA/:EngineeringUnits"
                                                        + " (declaration i=12026, owner i=12021) on"
                                                        + " "
                                                        + getNodeId()));
                                          }
                                          return client
                                              .getAddressSpace()
                                              .getNodeAsync(unique.iterator().next())
                                              .thenCompose(
                                                  node -> {
                                                    if (node == null
                                                        || node.getNodeClass()
                                                            != NodeClass.Variable) {
                                                      return CompletableFuture.failedFuture(
                                                          new UaException(
                                                              StatusCodes.Bad_NodeClassInvalid,
                                                              "http://opcfoundation.org/UA/:EngineeringUnits"
                                                                  + " (declaration i=12026, owner"
                                                                  + " i=12021) on "
                                                                  + getNodeId()));
                                                    }
                                                    return CompletableFuture.completedFuture(node);
                                                  });
                                        });
                              });
                    });
              });
      hop0.whenComplete(
          (node, failure) -> {
            if (failure != null) {
              result.completeExceptionally(failure);
            } else if (node != null && !(node instanceof PropertyTypeNode)) {
              result.completeExceptionally(
                  new UaException(
                      StatusCodes.Bad_TypeMismatch,
                      "http://opcfoundation.org/UA/:EngineeringUnits (declaration i=12026, owner"
                          + " i=12021)"));
            } else {
              result.complete((PropertyTypeNode) node);
            }
          });
    } catch (Exception e) {
      result.completeExceptionally(e);
    }
    return result;
  }

  @Override
  public @Nullable LocalizedText getTitle() throws UaException {
    PropertyTypeNode node = getTitleNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:Title (declaration i=12027, owner i=12021)"
              + " on "
              + getNodeId());
    }
    return (LocalizedText) node.getValue().getValue().getValue();
  }

  @Override
  public void setTitle(@Nullable LocalizedText value) throws UaException {
    PropertyTypeNode node = getTitleNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:Title (declaration i=12027, owner i=12021)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable LocalizedText readTitle() throws UaException {
    try {
      return readTitleAsync().get();
    } catch (ExecutionException e) {
      Throwable cause = e.getCause();
      while (cause instanceof CompletionException || cause instanceof ExecutionException) {
        cause = cause.getCause();
      }
      if (cause instanceof UaException failure) {
        throw failure;
      }
      throw new UaException(cause);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public void writeTitle(@Nullable LocalizedText value) throws UaException {
    try {
      StatusCode statusCode = writeTitleAsync(value).get();
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
  public CompletableFuture<? extends @Nullable LocalizedText> readTitleAsync() {
    return getTitleNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:Title (declaration i=12027, owner i=12021)"
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
                return (LocalizedText) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeTitleAsync(@Nullable LocalizedText title) {
    return getTitleNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:Title (declaration i=12027, owner i=12021)"
                            + " on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(title));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getTitleNode() throws UaException {
    try {
      return getTitleNodeAsync().get();
    } catch (ExecutionException e) {
      Throwable cause = e.getCause();
      while (cause instanceof CompletionException || cause instanceof ExecutionException) {
        cause = cause.getCause();
      }
      if (cause instanceof UaException failure) {
        throw failure;
      }
      throw new UaException(cause);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getTitleNodeAsync() {
    CompletableFuture<PropertyTypeNode> result = new CompletableFuture<>();
    try {
      CompletableFuture<NodeId> lookup = CompletableFuture.completedFuture(getNodeId());
      CompletableFuture<UaNode> hop0 =
          lookup.thenCompose(
              parent -> {
                NodeId parentId = parent;
                if (result.isCancelled()) {
                  return CompletableFuture.failedFuture(new CancellationException());
                }
                if (parentId == null) {
                  return CompletableFuture.completedFuture(null);
                }
                CompletableFuture<Void> namespaceReady;
                if (client.getNamespaceTable().getIndex("http://opcfoundation.org/UA/") == null
                    || client.getNamespaceTable().getIndex("http://opcfoundation.org/UA/")
                        == null) {
                  namespaceReady = client.readNamespaceTableAsync().thenApply(ignored -> null);
                } else {
                  namespaceReady = CompletableFuture.completedFuture(null);
                }
                return namespaceReady.thenCompose(
                    ignored -> {
                      if (result.isCancelled()) {
                        return CompletableFuture.failedFuture(new CancellationException());
                      }
                      var namespaceIndex =
                          client.getNamespaceTable().getIndex("http://opcfoundation.org/UA/");
                      var referenceId =
                          ExpandedNodeId.parse("i=46").toNodeId(client.getNamespaceTable());
                      if (namespaceIndex == null || referenceId.isEmpty()) {
                        return CompletableFuture.failedFuture(
                            new UaException(
                                StatusCodes.Bad_NodeIdInvalid,
                                "http://opcfoundation.org/UA/:Title (declaration i=12027, owner"
                                    + " i=12021) on "
                                    + getNodeId()));
                      }
                      var browsePath =
                          new BrowsePath(
                              parentId,
                              new RelativePath(
                                  new RelativePathElement[] {
                                    new RelativePathElement(
                                        referenceId.orElseThrow(),
                                        false,
                                        true,
                                        new QualifiedName(namespaceIndex, "Title"))
                                  }));
                      return client
                          .translateBrowsePathsAsync(List.of(browsePath))
                          .thenCompose(
                              response -> {
                                if (result.isCancelled()) {
                                  return CompletableFuture.failedFuture(
                                      new CancellationException());
                                }
                                var results = response == null ? null : response.getResults();
                                if (results == null
                                    || results.length != 1
                                    || results[0] == null
                                    || results[0].getStatusCode() == null) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_UnexpectedError,
                                          "http://opcfoundation.org/UA/:Title (declaration i=12027,"
                                              + " owner i=12021) on "
                                              + getNodeId()));
                                }
                                var operation = results[0];
                                if (operation.getStatusCode().getValue()
                                    == StatusCodes.Bad_NoMatch) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_NotFound,
                                          "http://opcfoundation.org/UA/:Title (declaration i=12027,"
                                              + " owner i=12021) on "
                                              + getNodeId()));
                                }
                                if (!operation.getStatusCode().isGood()) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          operation.getStatusCode(),
                                          "http://opcfoundation.org/UA/:Title (declaration i=12027,"
                                              + " owner i=12021)"));
                                }
                                var targets = operation.getTargets();
                                if (targets == null || targets.length == 0) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_UnexpectedError,
                                          "http://opcfoundation.org/UA/:Title (declaration i=12027,"
                                              + " owner i=12021) on "
                                              + getNodeId()));
                                }
                                var identities = new ArrayList<CompletableFuture<NodeId>>();
                                for (var target : targets) {
                                  if (target == null
                                      || target.getTargetId() == null
                                      || target.getRemainingPathIndex() == null
                                      || target.getRemainingPathIndex().longValue()
                                          != 0xffffffffL) {
                                    return CompletableFuture.failedFuture(
                                        new UaException(
                                            StatusCodes.Bad_UnexpectedError,
                                            "http://opcfoundation.org/UA/:Title (declaration"
                                                + " i=12027, owner i=12021) on "
                                                + getNodeId()));
                                  }
                                  if (!target.getTargetId().isLocal()) {
                                    return CompletableFuture.failedFuture(
                                        new UaException(
                                            StatusCodes.Bad_NotSupported,
                                            "http://opcfoundation.org/UA/:Title (declaration"
                                                + " i=12027, owner i=12021) on "
                                                + getNodeId()));
                                  }
                                  if (result.isCancelled()) {
                                    return CompletableFuture.failedFuture(
                                        new CancellationException());
                                  }
                                  var localTarget =
                                      target.getTargetId().toNodeId(client.getNamespaceTable());
                                  if (localTarget.isPresent()) {
                                    identities.add(
                                        CompletableFuture.completedFuture(
                                            localTarget.orElseThrow()));
                                  } else {
                                    identities.add(
                                        client
                                            .readNamespaceTableAsync()
                                            .thenCompose(
                                                namespaceTable -> {
                                                  var resolvedTarget =
                                                      target.getTargetId().toNodeId(namespaceTable);
                                                  if (resolvedTarget.isEmpty()) {
                                                    return CompletableFuture.failedFuture(
                                                        new UaException(
                                                            StatusCodes.Bad_NodeIdInvalid,
                                                            "http://opcfoundation.org/UA/:Title"
                                                                + " (declaration i=12027, owner"
                                                                + " i=12021) on "
                                                                + getNodeId()));
                                                  }
                                                  return CompletableFuture.completedFuture(
                                                      resolvedTarget.orElseThrow());
                                                }));
                                  }
                                }
                                return CompletableFuture.allOf(
                                        identities.toArray(CompletableFuture[]::new))
                                    .thenCompose(
                                        ready -> {
                                          if (result.isCancelled()) {
                                            return CompletableFuture.failedFuture(
                                                new CancellationException());
                                          }
                                          var unique = new LinkedHashSet<NodeId>();
                                          identities.forEach(
                                              identity -> unique.add(identity.join()));
                                          if (unique.stream().anyMatch(NodeId::isNull)) {
                                            return CompletableFuture.failedFuture(
                                                new UaException(
                                                    StatusCodes.Bad_NodeIdInvalid,
                                                    "http://opcfoundation.org/UA/:Title"
                                                        + " (declaration i=12027, owner i=12021) on"
                                                        + " "
                                                        + getNodeId()));
                                          }
                                          if (unique.size() != 1) {
                                            return CompletableFuture.failedFuture(
                                                new UaException(
                                                    StatusCodes.Bad_TooManyMatches,
                                                    "http://opcfoundation.org/UA/:Title"
                                                        + " (declaration i=12027, owner i=12021) on"
                                                        + " "
                                                        + getNodeId()));
                                          }
                                          return client
                                              .getAddressSpace()
                                              .getNodeAsync(unique.iterator().next())
                                              .thenCompose(
                                                  node -> {
                                                    if (node == null
                                                        || node.getNodeClass()
                                                            != NodeClass.Variable) {
                                                      return CompletableFuture.failedFuture(
                                                          new UaException(
                                                              StatusCodes.Bad_NodeClassInvalid,
                                                              "http://opcfoundation.org/UA/:Title"
                                                                  + " (declaration i=12027, owner"
                                                                  + " i=12021) on "
                                                                  + getNodeId()));
                                                    }
                                                    return CompletableFuture.completedFuture(node);
                                                  });
                                        });
                              });
                    });
              });
      hop0.whenComplete(
          (node, failure) -> {
            if (failure != null) {
              result.completeExceptionally(failure);
            } else if (node != null && !(node instanceof PropertyTypeNode)) {
              result.completeExceptionally(
                  new UaException(
                      StatusCodes.Bad_TypeMismatch,
                      "http://opcfoundation.org/UA/:Title (declaration i=12027, owner i=12021)"));
            } else {
              result.complete((PropertyTypeNode) node);
            }
          });
    } catch (Exception e) {
      result.completeExceptionally(e);
    }
    return result;
  }

  @Override
  public @Nullable AxisScaleEnumeration getAxisScaleType() throws UaException {
    PropertyTypeNode node = getAxisScaleTypeNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:AxisScaleType (declaration i=12028, owner i=12021)"
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
              "AxisScaleType: ValueRank=-1 does not permit rank " + rank);
        }
        if (value != null && !((Object) value instanceof AxisScaleEnumeration)) {
          if (!(value instanceof Integer)) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "AxisScaleType: expected"
                    + " org.eclipse.milo.opcua.stack.core.types.enumerated.AxisScaleEnumeration or"
                    + " Int32, got "
                    + value);
          }
          if (AxisScaleEnumeration.from((Integer) value) == null) {
            throw new UaException(
                StatusCodes.Bad_OutOfRange,
                "AxisScaleType: unknown"
                    + " org.eclipse.milo.opcua.stack.core.types.enumerated.AxisScaleEnumeration"
                    + " value "
                    + value);
          }
        }
        convertedValue =
            value == null || value instanceof AxisScaleEnumeration
                ? (AxisScaleEnumeration) value
                : AxisScaleEnumeration.from((Integer) value);
      }
    }
    return (AxisScaleEnumeration) convertedValue;
  }

  @Override
  public void setAxisScaleType(@Nullable AxisScaleEnumeration value) throws UaException {
    PropertyTypeNode node = getAxisScaleTypeNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:AxisScaleType (declaration i=12028, owner i=12021)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable AxisScaleEnumeration readAxisScaleType() throws UaException {
    try {
      return readAxisScaleTypeAsync().get();
    } catch (ExecutionException e) {
      Throwable cause = e.getCause();
      while (cause instanceof CompletionException || cause instanceof ExecutionException) {
        cause = cause.getCause();
      }
      if (cause instanceof UaException failure) {
        throw failure;
      }
      throw new UaException(cause);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public void writeAxisScaleType(@Nullable AxisScaleEnumeration value) throws UaException {
    try {
      StatusCode statusCode = writeAxisScaleTypeAsync(value).get();
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
  public CompletableFuture<? extends @Nullable AxisScaleEnumeration> readAxisScaleTypeAsync() {
    return getAxisScaleTypeNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:AxisScaleType (declaration i=12028, owner"
                            + " i=12021) on "
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
                          "AxisScaleType: ValueRank=-1 does not permit rank " + rank);
                    }
                    if (value != null && !((Object) value instanceof AxisScaleEnumeration)) {
                      if (!(value instanceof Integer)) {
                        throw new UaRuntimeException(
                            StatusCodes.Bad_TypeMismatch,
                            "AxisScaleType: expected"
                                + " org.eclipse.milo.opcua.stack.core.types.enumerated.AxisScaleEnumeration"
                                + " or Int32, got "
                                + value);
                      }
                      if (AxisScaleEnumeration.from((Integer) value) == null) {
                        throw new UaRuntimeException(
                            StatusCodes.Bad_OutOfRange,
                            "AxisScaleType: unknown"
                                + " org.eclipse.milo.opcua.stack.core.types.enumerated.AxisScaleEnumeration"
                                + " value "
                                + value);
                      }
                    }
                    convertedValue =
                        value == null || value instanceof AxisScaleEnumeration
                            ? (AxisScaleEnumeration) value
                            : AxisScaleEnumeration.from((Integer) value);
                  }
                }
                return (AxisScaleEnumeration) convertedValue;
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeAxisScaleTypeAsync(
      @Nullable AxisScaleEnumeration axisScaleType) {
    return getAxisScaleTypeNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:AxisScaleType (declaration i=12028, owner"
                            + " i=12021) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(axisScaleType));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getAxisScaleTypeNode() throws UaException {
    try {
      return getAxisScaleTypeNodeAsync().get();
    } catch (ExecutionException e) {
      Throwable cause = e.getCause();
      while (cause instanceof CompletionException || cause instanceof ExecutionException) {
        cause = cause.getCause();
      }
      if (cause instanceof UaException failure) {
        throw failure;
      }
      throw new UaException(cause);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getAxisScaleTypeNodeAsync() {
    CompletableFuture<PropertyTypeNode> result = new CompletableFuture<>();
    try {
      CompletableFuture<NodeId> lookup = CompletableFuture.completedFuture(getNodeId());
      CompletableFuture<UaNode> hop0 =
          lookup.thenCompose(
              parent -> {
                NodeId parentId = parent;
                if (result.isCancelled()) {
                  return CompletableFuture.failedFuture(new CancellationException());
                }
                if (parentId == null) {
                  return CompletableFuture.completedFuture(null);
                }
                CompletableFuture<Void> namespaceReady;
                if (client.getNamespaceTable().getIndex("http://opcfoundation.org/UA/") == null
                    || client.getNamespaceTable().getIndex("http://opcfoundation.org/UA/")
                        == null) {
                  namespaceReady = client.readNamespaceTableAsync().thenApply(ignored -> null);
                } else {
                  namespaceReady = CompletableFuture.completedFuture(null);
                }
                return namespaceReady.thenCompose(
                    ignored -> {
                      if (result.isCancelled()) {
                        return CompletableFuture.failedFuture(new CancellationException());
                      }
                      var namespaceIndex =
                          client.getNamespaceTable().getIndex("http://opcfoundation.org/UA/");
                      var referenceId =
                          ExpandedNodeId.parse("i=46").toNodeId(client.getNamespaceTable());
                      if (namespaceIndex == null || referenceId.isEmpty()) {
                        return CompletableFuture.failedFuture(
                            new UaException(
                                StatusCodes.Bad_NodeIdInvalid,
                                "http://opcfoundation.org/UA/:AxisScaleType (declaration i=12028,"
                                    + " owner i=12021) on "
                                    + getNodeId()));
                      }
                      var browsePath =
                          new BrowsePath(
                              parentId,
                              new RelativePath(
                                  new RelativePathElement[] {
                                    new RelativePathElement(
                                        referenceId.orElseThrow(),
                                        false,
                                        true,
                                        new QualifiedName(namespaceIndex, "AxisScaleType"))
                                  }));
                      return client
                          .translateBrowsePathsAsync(List.of(browsePath))
                          .thenCompose(
                              response -> {
                                if (result.isCancelled()) {
                                  return CompletableFuture.failedFuture(
                                      new CancellationException());
                                }
                                var results = response == null ? null : response.getResults();
                                if (results == null
                                    || results.length != 1
                                    || results[0] == null
                                    || results[0].getStatusCode() == null) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_UnexpectedError,
                                          "http://opcfoundation.org/UA/:AxisScaleType (declaration"
                                              + " i=12028, owner i=12021) on "
                                              + getNodeId()));
                                }
                                var operation = results[0];
                                if (operation.getStatusCode().getValue()
                                    == StatusCodes.Bad_NoMatch) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_NotFound,
                                          "http://opcfoundation.org/UA/:AxisScaleType (declaration"
                                              + " i=12028, owner i=12021) on "
                                              + getNodeId()));
                                }
                                if (!operation.getStatusCode().isGood()) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          operation.getStatusCode(),
                                          "http://opcfoundation.org/UA/:AxisScaleType (declaration"
                                              + " i=12028, owner i=12021)"));
                                }
                                var targets = operation.getTargets();
                                if (targets == null || targets.length == 0) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_UnexpectedError,
                                          "http://opcfoundation.org/UA/:AxisScaleType (declaration"
                                              + " i=12028, owner i=12021) on "
                                              + getNodeId()));
                                }
                                var identities = new ArrayList<CompletableFuture<NodeId>>();
                                for (var target : targets) {
                                  if (target == null
                                      || target.getTargetId() == null
                                      || target.getRemainingPathIndex() == null
                                      || target.getRemainingPathIndex().longValue()
                                          != 0xffffffffL) {
                                    return CompletableFuture.failedFuture(
                                        new UaException(
                                            StatusCodes.Bad_UnexpectedError,
                                            "http://opcfoundation.org/UA/:AxisScaleType"
                                                + " (declaration i=12028, owner i=12021) on "
                                                + getNodeId()));
                                  }
                                  if (!target.getTargetId().isLocal()) {
                                    return CompletableFuture.failedFuture(
                                        new UaException(
                                            StatusCodes.Bad_NotSupported,
                                            "http://opcfoundation.org/UA/:AxisScaleType"
                                                + " (declaration i=12028, owner i=12021) on "
                                                + getNodeId()));
                                  }
                                  if (result.isCancelled()) {
                                    return CompletableFuture.failedFuture(
                                        new CancellationException());
                                  }
                                  var localTarget =
                                      target.getTargetId().toNodeId(client.getNamespaceTable());
                                  if (localTarget.isPresent()) {
                                    identities.add(
                                        CompletableFuture.completedFuture(
                                            localTarget.orElseThrow()));
                                  } else {
                                    identities.add(
                                        client
                                            .readNamespaceTableAsync()
                                            .thenCompose(
                                                namespaceTable -> {
                                                  var resolvedTarget =
                                                      target.getTargetId().toNodeId(namespaceTable);
                                                  if (resolvedTarget.isEmpty()) {
                                                    return CompletableFuture.failedFuture(
                                                        new UaException(
                                                            StatusCodes.Bad_NodeIdInvalid,
                                                            "http://opcfoundation.org/UA/:AxisScaleType"
                                                                + " (declaration i=12028, owner"
                                                                + " i=12021) on "
                                                                + getNodeId()));
                                                  }
                                                  return CompletableFuture.completedFuture(
                                                      resolvedTarget.orElseThrow());
                                                }));
                                  }
                                }
                                return CompletableFuture.allOf(
                                        identities.toArray(CompletableFuture[]::new))
                                    .thenCompose(
                                        ready -> {
                                          if (result.isCancelled()) {
                                            return CompletableFuture.failedFuture(
                                                new CancellationException());
                                          }
                                          var unique = new LinkedHashSet<NodeId>();
                                          identities.forEach(
                                              identity -> unique.add(identity.join()));
                                          if (unique.stream().anyMatch(NodeId::isNull)) {
                                            return CompletableFuture.failedFuture(
                                                new UaException(
                                                    StatusCodes.Bad_NodeIdInvalid,
                                                    "http://opcfoundation.org/UA/:AxisScaleType"
                                                        + " (declaration i=12028, owner i=12021) on"
                                                        + " "
                                                        + getNodeId()));
                                          }
                                          if (unique.size() != 1) {
                                            return CompletableFuture.failedFuture(
                                                new UaException(
                                                    StatusCodes.Bad_TooManyMatches,
                                                    "http://opcfoundation.org/UA/:AxisScaleType"
                                                        + " (declaration i=12028, owner i=12021) on"
                                                        + " "
                                                        + getNodeId()));
                                          }
                                          return client
                                              .getAddressSpace()
                                              .getNodeAsync(unique.iterator().next())
                                              .thenCompose(
                                                  node -> {
                                                    if (node == null
                                                        || node.getNodeClass()
                                                            != NodeClass.Variable) {
                                                      return CompletableFuture.failedFuture(
                                                          new UaException(
                                                              StatusCodes.Bad_NodeClassInvalid,
                                                              "http://opcfoundation.org/UA/:AxisScaleType"
                                                                  + " (declaration i=12028, owner"
                                                                  + " i=12021) on "
                                                                  + getNodeId()));
                                                    }
                                                    return CompletableFuture.completedFuture(node);
                                                  });
                                        });
                              });
                    });
              });
      hop0.whenComplete(
          (node, failure) -> {
            if (failure != null) {
              result.completeExceptionally(failure);
            } else if (node != null && !(node instanceof PropertyTypeNode)) {
              result.completeExceptionally(
                  new UaException(
                      StatusCodes.Bad_TypeMismatch,
                      "http://opcfoundation.org/UA/:AxisScaleType (declaration i=12028, owner"
                          + " i=12021)"));
            } else {
              result.complete((PropertyTypeNode) node);
            }
          });
    } catch (Exception e) {
      result.completeExceptionally(e);
    }
    return result;
  }
}

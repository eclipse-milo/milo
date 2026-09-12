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

import com.digitalpetri.opcua.uanodeset.runtime.client.ClientViews;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.SamplingIntervalDiagnosticsArrayTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.ServerDiagnosticsSummaryTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.SubscriptionDiagnosticsArrayTypeNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaNode;
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
import org.eclipse.milo.opcua.stack.core.types.structured.BrowsePath;
import org.eclipse.milo.opcua.stack.core.types.structured.RelativePath;
import org.eclipse.milo.opcua.stack.core.types.structured.RelativePathElement;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.SamplingIntervalDiagnosticsDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.ServerDiagnosticsSummaryDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.SubscriptionDiagnosticsDataType;
import org.jspecify.annotations.Nullable;

public class ServerDiagnosticsTypeNode extends BaseObjectTypeNode implements ServerDiagnosticsType {
  public ServerDiagnosticsTypeNode(
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
  public static ClientViews createViews(ServerDiagnosticsTypeNode node) {
    Objects.requireNonNull(node, "node");
    return ClientViews.forNode(node.client, node);
  }

  @Override
  public @Nullable Boolean getEnabledFlag() throws UaException {
    PropertyTypeNode node = getEnabledFlagNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:EnabledFlag (declaration i=2025, owner i=2020)"
              + " on "
              + getNodeId());
    }
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setEnabledFlag(@Nullable Boolean value) throws UaException {
    PropertyTypeNode node = getEnabledFlagNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:EnabledFlag (declaration i=2025, owner i=2020)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable Boolean readEnabledFlag() throws UaException {
    try {
      return readEnabledFlagAsync().get();
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
  public void writeEnabledFlag(@Nullable Boolean value) throws UaException {
    try {
      StatusCode statusCode = writeEnabledFlagAsync(value).get();
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
  public CompletableFuture<? extends @Nullable Boolean> readEnabledFlagAsync() {
    return getEnabledFlagNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:EnabledFlag (declaration i=2025, owner"
                            + " i=2020) on "
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
  public CompletableFuture<StatusCode> writeEnabledFlagAsync(@Nullable Boolean enabledFlag) {
    return getEnabledFlagNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:EnabledFlag (declaration i=2025, owner"
                            + " i=2020) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(enabledFlag));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getEnabledFlagNode() throws UaException {
    try {
      return getEnabledFlagNodeAsync().get();
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
  public CompletableFuture<? extends PropertyTypeNode> getEnabledFlagNodeAsync() {
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
                                "http://opcfoundation.org/UA/:EnabledFlag (declaration i=2025,"
                                    + " owner i=2020) on "
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
                                        new QualifiedName(namespaceIndex, "EnabledFlag"))
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
                                          "http://opcfoundation.org/UA/:EnabledFlag (declaration"
                                              + " i=2025, owner i=2020) on "
                                              + getNodeId()));
                                }
                                var operation = results[0];
                                if (operation.getStatusCode().getValue()
                                    == StatusCodes.Bad_NoMatch) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_NotFound,
                                          "http://opcfoundation.org/UA/:EnabledFlag (declaration"
                                              + " i=2025, owner i=2020) on "
                                              + getNodeId()));
                                }
                                if (!operation.getStatusCode().isGood()) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          operation.getStatusCode(),
                                          "http://opcfoundation.org/UA/:EnabledFlag (declaration"
                                              + " i=2025, owner i=2020)"));
                                }
                                var targets = operation.getTargets();
                                if (targets == null || targets.length == 0) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_UnexpectedError,
                                          "http://opcfoundation.org/UA/:EnabledFlag (declaration"
                                              + " i=2025, owner i=2020) on "
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
                                            "http://opcfoundation.org/UA/:EnabledFlag (declaration"
                                                + " i=2025, owner i=2020) on "
                                                + getNodeId()));
                                  }
                                  if (!target.getTargetId().isLocal()) {
                                    return CompletableFuture.failedFuture(
                                        new UaException(
                                            StatusCodes.Bad_NotSupported,
                                            "http://opcfoundation.org/UA/:EnabledFlag (declaration"
                                                + " i=2025, owner i=2020) on "
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
                                                            "http://opcfoundation.org/UA/:EnabledFlag"
                                                                + " (declaration i=2025, owner"
                                                                + " i=2020) on "
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
                                                    "http://opcfoundation.org/UA/:EnabledFlag"
                                                        + " (declaration i=2025, owner i=2020) on "
                                                        + getNodeId()));
                                          }
                                          if (unique.size() != 1) {
                                            return CompletableFuture.failedFuture(
                                                new UaException(
                                                    StatusCodes.Bad_TooManyMatches,
                                                    "http://opcfoundation.org/UA/:EnabledFlag"
                                                        + " (declaration i=2025, owner i=2020) on "
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
                                                              "http://opcfoundation.org/UA/:EnabledFlag"
                                                                  + " (declaration i=2025, owner"
                                                                  + " i=2020) on "
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
                      "http://opcfoundation.org/UA/:EnabledFlag (declaration i=2025, owner"
                          + " i=2020)"));
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
  public @Nullable ServerDiagnosticsSummaryDataType getServerDiagnosticsSummary()
      throws UaException {
    ServerDiagnosticsSummaryTypeNode node = getServerDiagnosticsSummaryNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ServerDiagnosticsSummary (declaration i=2021, owner i=2020)"
              + " on "
              + getNodeId());
    }
    return (ServerDiagnosticsSummaryDataType)
        decodeValue(
            node.getValue().getValue().getValue(),
            ServerDiagnosticsSummaryDataType.class,
            ValueRanks.Scalar);
  }

  @Override
  public void setServerDiagnosticsSummary(@Nullable ServerDiagnosticsSummaryDataType value)
      throws UaException {
    ServerDiagnosticsSummaryTypeNode node = getServerDiagnosticsSummaryNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ServerDiagnosticsSummary (declaration i=2021, owner i=2020)"
              + " on "
              + getNodeId());
    }
    node.setValue(
        new Variant(encodeValue(value, ServerDiagnosticsSummaryDataType.class, ValueRanks.Scalar)));
  }

  @Override
  public @Nullable ServerDiagnosticsSummaryDataType readServerDiagnosticsSummary()
      throws UaException {
    try {
      return readServerDiagnosticsSummaryAsync().get();
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
  public void writeServerDiagnosticsSummary(@Nullable ServerDiagnosticsSummaryDataType value)
      throws UaException {
    try {
      StatusCode statusCode = writeServerDiagnosticsSummaryAsync(value).get();
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
  public CompletableFuture<? extends @Nullable ServerDiagnosticsSummaryDataType>
      readServerDiagnosticsSummaryAsync() {
    return getServerDiagnosticsSummaryNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ServerDiagnosticsSummary (declaration i=2021,"
                            + " owner i=2020) on "
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
                return (ServerDiagnosticsSummaryDataType)
                    decodeValue(
                        v.getValue().getValue(),
                        ServerDiagnosticsSummaryDataType.class,
                        ValueRanks.Scalar);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeServerDiagnosticsSummaryAsync(
      @Nullable ServerDiagnosticsSummaryDataType serverDiagnosticsSummary) {
    return getServerDiagnosticsSummaryNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ServerDiagnosticsSummary (declaration i=2021,"
                            + " owner i=2020) on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(
                            encodeValue(
                                serverDiagnosticsSummary,
                                ServerDiagnosticsSummaryDataType.class,
                                ValueRanks.Scalar)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public ServerDiagnosticsSummaryTypeNode getServerDiagnosticsSummaryNode() throws UaException {
    try {
      return getServerDiagnosticsSummaryNodeAsync().get();
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
  public CompletableFuture<? extends ServerDiagnosticsSummaryTypeNode>
      getServerDiagnosticsSummaryNodeAsync() {
    CompletableFuture<ServerDiagnosticsSummaryTypeNode> result = new CompletableFuture<>();
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
                          ExpandedNodeId.parse("i=47").toNodeId(client.getNamespaceTable());
                      if (namespaceIndex == null || referenceId.isEmpty()) {
                        return CompletableFuture.failedFuture(
                            new UaException(
                                StatusCodes.Bad_NodeIdInvalid,
                                "http://opcfoundation.org/UA/:ServerDiagnosticsSummary (declaration"
                                    + " i=2021, owner i=2020) on "
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
                                        new QualifiedName(
                                            namespaceIndex, "ServerDiagnosticsSummary"))
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
                                          "http://opcfoundation.org/UA/:ServerDiagnosticsSummary"
                                              + " (declaration i=2021, owner i=2020) on "
                                              + getNodeId()));
                                }
                                var operation = results[0];
                                if (operation.getStatusCode().getValue()
                                    == StatusCodes.Bad_NoMatch) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_NotFound,
                                          "http://opcfoundation.org/UA/:ServerDiagnosticsSummary"
                                              + " (declaration i=2021, owner i=2020) on "
                                              + getNodeId()));
                                }
                                if (!operation.getStatusCode().isGood()) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          operation.getStatusCode(),
                                          "http://opcfoundation.org/UA/:ServerDiagnosticsSummary"
                                              + " (declaration i=2021, owner i=2020)"));
                                }
                                var targets = operation.getTargets();
                                if (targets == null || targets.length == 0) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_UnexpectedError,
                                          "http://opcfoundation.org/UA/:ServerDiagnosticsSummary"
                                              + " (declaration i=2021, owner i=2020) on "
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
                                            "http://opcfoundation.org/UA/:ServerDiagnosticsSummary"
                                                + " (declaration i=2021, owner i=2020) on "
                                                + getNodeId()));
                                  }
                                  if (!target.getTargetId().isLocal()) {
                                    return CompletableFuture.failedFuture(
                                        new UaException(
                                            StatusCodes.Bad_NotSupported,
                                            "http://opcfoundation.org/UA/:ServerDiagnosticsSummary"
                                                + " (declaration i=2021, owner i=2020) on "
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
                                                            "http://opcfoundation.org/UA/:ServerDiagnosticsSummary"
                                                                + " (declaration i=2021, owner"
                                                                + " i=2020) on "
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
                                                    "http://opcfoundation.org/UA/:ServerDiagnosticsSummary"
                                                        + " (declaration i=2021, owner i=2020) on "
                                                        + getNodeId()));
                                          }
                                          if (unique.size() != 1) {
                                            return CompletableFuture.failedFuture(
                                                new UaException(
                                                    StatusCodes.Bad_TooManyMatches,
                                                    "http://opcfoundation.org/UA/:ServerDiagnosticsSummary"
                                                        + " (declaration i=2021, owner i=2020) on "
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
                                                              "http://opcfoundation.org/UA/:ServerDiagnosticsSummary"
                                                                  + " (declaration i=2021, owner"
                                                                  + " i=2020) on "
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
            } else if (node != null && !(node instanceof ServerDiagnosticsSummaryTypeNode)) {
              result.completeExceptionally(
                  new UaException(
                      StatusCodes.Bad_TypeMismatch,
                      "http://opcfoundation.org/UA/:ServerDiagnosticsSummary (declaration i=2021,"
                          + " owner i=2020)"));
            } else {
              result.complete((ServerDiagnosticsSummaryTypeNode) node);
            }
          });
    } catch (Exception e) {
      result.completeExceptionally(e);
    }
    return result;
  }

  @Override
  public @Nullable SamplingIntervalDiagnosticsDataType @Nullable []
      getSamplingIntervalDiagnosticsArray() throws UaException {
    SamplingIntervalDiagnosticsArrayTypeNode node = getSamplingIntervalDiagnosticsArrayNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SamplingIntervalDiagnosticsArray (declaration i=2022, owner"
              + " i=2020) on "
              + getNodeId());
    }
    return (SamplingIntervalDiagnosticsDataType[])
        decodeValue(
            node.getValue().getValue().getValue(),
            SamplingIntervalDiagnosticsDataType.class,
            ValueRanks.OneDimension);
  }

  @Override
  public void setSamplingIntervalDiagnosticsArray(
      @Nullable SamplingIntervalDiagnosticsDataType @Nullable [] value) throws UaException {
    SamplingIntervalDiagnosticsArrayTypeNode node = getSamplingIntervalDiagnosticsArrayNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SamplingIntervalDiagnosticsArray (declaration i=2022, owner"
              + " i=2020) on "
              + getNodeId());
    }
    node.setValue(
        new Variant(
            encodeValue(
                value, SamplingIntervalDiagnosticsDataType.class, ValueRanks.OneDimension)));
  }

  @Override
  public @Nullable SamplingIntervalDiagnosticsDataType @Nullable []
      readSamplingIntervalDiagnosticsArray() throws UaException {
    try {
      return readSamplingIntervalDiagnosticsArrayAsync().get();
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
  public void writeSamplingIntervalDiagnosticsArray(
      @Nullable SamplingIntervalDiagnosticsDataType @Nullable [] value) throws UaException {
    try {
      StatusCode statusCode = writeSamplingIntervalDiagnosticsArrayAsync(value).get();
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
  public CompletableFuture<? extends @Nullable SamplingIntervalDiagnosticsDataType @Nullable []>
      readSamplingIntervalDiagnosticsArrayAsync() {
    return getSamplingIntervalDiagnosticsArrayNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:SamplingIntervalDiagnosticsArray (declaration"
                            + " i=2022, owner i=2020) on "
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
                return (SamplingIntervalDiagnosticsDataType[])
                    decodeValue(
                        v.getValue().getValue(),
                        SamplingIntervalDiagnosticsDataType.class,
                        ValueRanks.OneDimension);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeSamplingIntervalDiagnosticsArrayAsync(
      @Nullable SamplingIntervalDiagnosticsDataType @Nullable [] samplingIntervalDiagnosticsArray) {
    return getSamplingIntervalDiagnosticsArrayNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:SamplingIntervalDiagnosticsArray (declaration"
                            + " i=2022, owner i=2020) on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(
                            encodeValue(
                                samplingIntervalDiagnosticsArray,
                                SamplingIntervalDiagnosticsDataType.class,
                                ValueRanks.OneDimension)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable SamplingIntervalDiagnosticsArrayTypeNode
      getSamplingIntervalDiagnosticsArrayNode() throws UaException {
    try {
      return getSamplingIntervalDiagnosticsArrayNodeAsync().get();
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
  public CompletableFuture<? extends @Nullable SamplingIntervalDiagnosticsArrayTypeNode>
      getSamplingIntervalDiagnosticsArrayNodeAsync() {
    CompletableFuture<SamplingIntervalDiagnosticsArrayTypeNode> result = new CompletableFuture<>();
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
                          ExpandedNodeId.parse("i=47").toNodeId(client.getNamespaceTable());
                      if (namespaceIndex == null || referenceId.isEmpty()) {
                        return CompletableFuture.failedFuture(
                            new UaException(
                                StatusCodes.Bad_NodeIdInvalid,
                                "http://opcfoundation.org/UA/:SamplingIntervalDiagnosticsArray"
                                    + " (declaration i=2022, owner i=2020) on "
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
                                        new QualifiedName(
                                            namespaceIndex, "SamplingIntervalDiagnosticsArray"))
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
                                          "http://opcfoundation.org/UA/:SamplingIntervalDiagnosticsArray"
                                              + " (declaration i=2022, owner i=2020) on "
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
                                          "http://opcfoundation.org/UA/:SamplingIntervalDiagnosticsArray"
                                              + " (declaration i=2022, owner i=2020)"));
                                }
                                var targets = operation.getTargets();
                                if (targets == null || targets.length == 0) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_UnexpectedError,
                                          "http://opcfoundation.org/UA/:SamplingIntervalDiagnosticsArray"
                                              + " (declaration i=2022, owner i=2020) on "
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
                                            "http://opcfoundation.org/UA/:SamplingIntervalDiagnosticsArray"
                                                + " (declaration i=2022, owner i=2020) on "
                                                + getNodeId()));
                                  }
                                  if (!target.getTargetId().isLocal()) {
                                    return CompletableFuture.failedFuture(
                                        new UaException(
                                            StatusCodes.Bad_NotSupported,
                                            "http://opcfoundation.org/UA/:SamplingIntervalDiagnosticsArray"
                                                + " (declaration i=2022, owner i=2020) on "
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
                                                            "http://opcfoundation.org/UA/:SamplingIntervalDiagnosticsArray"
                                                                + " (declaration i=2022, owner"
                                                                + " i=2020) on "
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
                                                    "http://opcfoundation.org/UA/:SamplingIntervalDiagnosticsArray"
                                                        + " (declaration i=2022, owner i=2020) on "
                                                        + getNodeId()));
                                          }
                                          if (unique.size() != 1) {
                                            return CompletableFuture.failedFuture(
                                                new UaException(
                                                    StatusCodes.Bad_TooManyMatches,
                                                    "http://opcfoundation.org/UA/:SamplingIntervalDiagnosticsArray"
                                                        + " (declaration i=2022, owner i=2020) on "
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
                                                              "http://opcfoundation.org/UA/:SamplingIntervalDiagnosticsArray"
                                                                  + " (declaration i=2022, owner"
                                                                  + " i=2020) on "
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
            } else if (node != null
                && !(node instanceof SamplingIntervalDiagnosticsArrayTypeNode)) {
              result.completeExceptionally(
                  new UaException(
                      StatusCodes.Bad_TypeMismatch,
                      "http://opcfoundation.org/UA/:SamplingIntervalDiagnosticsArray (declaration"
                          + " i=2022, owner i=2020)"));
            } else {
              result.complete((SamplingIntervalDiagnosticsArrayTypeNode) node);
            }
          });
    } catch (Exception e) {
      result.completeExceptionally(e);
    }
    return result;
  }

  @Override
  public @Nullable SubscriptionDiagnosticsDataType @Nullable [] getSubscriptionDiagnosticsArray()
      throws UaException {
    SubscriptionDiagnosticsArrayTypeNode node = getSubscriptionDiagnosticsArrayNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SubscriptionDiagnosticsArray (declaration i=2023, owner"
              + " i=2020) on "
              + getNodeId());
    }
    return (SubscriptionDiagnosticsDataType[])
        decodeValue(
            node.getValue().getValue().getValue(),
            SubscriptionDiagnosticsDataType.class,
            ValueRanks.OneDimension);
  }

  @Override
  public void setSubscriptionDiagnosticsArray(
      @Nullable SubscriptionDiagnosticsDataType @Nullable [] value) throws UaException {
    SubscriptionDiagnosticsArrayTypeNode node = getSubscriptionDiagnosticsArrayNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SubscriptionDiagnosticsArray (declaration i=2023, owner"
              + " i=2020) on "
              + getNodeId());
    }
    node.setValue(
        new Variant(
            encodeValue(value, SubscriptionDiagnosticsDataType.class, ValueRanks.OneDimension)));
  }

  @Override
  public @Nullable SubscriptionDiagnosticsDataType @Nullable [] readSubscriptionDiagnosticsArray()
      throws UaException {
    try {
      return readSubscriptionDiagnosticsArrayAsync().get();
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
  public void writeSubscriptionDiagnosticsArray(
      @Nullable SubscriptionDiagnosticsDataType @Nullable [] value) throws UaException {
    try {
      StatusCode statusCode = writeSubscriptionDiagnosticsArrayAsync(value).get();
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
  public CompletableFuture<? extends @Nullable SubscriptionDiagnosticsDataType @Nullable []>
      readSubscriptionDiagnosticsArrayAsync() {
    return getSubscriptionDiagnosticsArrayNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:SubscriptionDiagnosticsArray (declaration"
                            + " i=2023, owner i=2020) on "
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
                return (SubscriptionDiagnosticsDataType[])
                    decodeValue(
                        v.getValue().getValue(),
                        SubscriptionDiagnosticsDataType.class,
                        ValueRanks.OneDimension);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeSubscriptionDiagnosticsArrayAsync(
      @Nullable SubscriptionDiagnosticsDataType @Nullable [] subscriptionDiagnosticsArray) {
    return getSubscriptionDiagnosticsArrayNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:SubscriptionDiagnosticsArray (declaration"
                            + " i=2023, owner i=2020) on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(
                            encodeValue(
                                subscriptionDiagnosticsArray,
                                SubscriptionDiagnosticsDataType.class,
                                ValueRanks.OneDimension)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public SubscriptionDiagnosticsArrayTypeNode getSubscriptionDiagnosticsArrayNode()
      throws UaException {
    try {
      return getSubscriptionDiagnosticsArrayNodeAsync().get();
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
  public CompletableFuture<? extends SubscriptionDiagnosticsArrayTypeNode>
      getSubscriptionDiagnosticsArrayNodeAsync() {
    CompletableFuture<SubscriptionDiagnosticsArrayTypeNode> result = new CompletableFuture<>();
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
                          ExpandedNodeId.parse("i=47").toNodeId(client.getNamespaceTable());
                      if (namespaceIndex == null || referenceId.isEmpty()) {
                        return CompletableFuture.failedFuture(
                            new UaException(
                                StatusCodes.Bad_NodeIdInvalid,
                                "http://opcfoundation.org/UA/:SubscriptionDiagnosticsArray"
                                    + " (declaration i=2023, owner i=2020) on "
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
                                        new QualifiedName(
                                            namespaceIndex, "SubscriptionDiagnosticsArray"))
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
                                          "http://opcfoundation.org/UA/:SubscriptionDiagnosticsArray"
                                              + " (declaration i=2023, owner i=2020) on "
                                              + getNodeId()));
                                }
                                var operation = results[0];
                                if (operation.getStatusCode().getValue()
                                    == StatusCodes.Bad_NoMatch) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_NotFound,
                                          "http://opcfoundation.org/UA/:SubscriptionDiagnosticsArray"
                                              + " (declaration i=2023, owner i=2020) on "
                                              + getNodeId()));
                                }
                                if (!operation.getStatusCode().isGood()) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          operation.getStatusCode(),
                                          "http://opcfoundation.org/UA/:SubscriptionDiagnosticsArray"
                                              + " (declaration i=2023, owner i=2020)"));
                                }
                                var targets = operation.getTargets();
                                if (targets == null || targets.length == 0) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_UnexpectedError,
                                          "http://opcfoundation.org/UA/:SubscriptionDiagnosticsArray"
                                              + " (declaration i=2023, owner i=2020) on "
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
                                            "http://opcfoundation.org/UA/:SubscriptionDiagnosticsArray"
                                                + " (declaration i=2023, owner i=2020) on "
                                                + getNodeId()));
                                  }
                                  if (!target.getTargetId().isLocal()) {
                                    return CompletableFuture.failedFuture(
                                        new UaException(
                                            StatusCodes.Bad_NotSupported,
                                            "http://opcfoundation.org/UA/:SubscriptionDiagnosticsArray"
                                                + " (declaration i=2023, owner i=2020) on "
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
                                                            "http://opcfoundation.org/UA/:SubscriptionDiagnosticsArray"
                                                                + " (declaration i=2023, owner"
                                                                + " i=2020) on "
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
                                                    "http://opcfoundation.org/UA/:SubscriptionDiagnosticsArray"
                                                        + " (declaration i=2023, owner i=2020) on "
                                                        + getNodeId()));
                                          }
                                          if (unique.size() != 1) {
                                            return CompletableFuture.failedFuture(
                                                new UaException(
                                                    StatusCodes.Bad_TooManyMatches,
                                                    "http://opcfoundation.org/UA/:SubscriptionDiagnosticsArray"
                                                        + " (declaration i=2023, owner i=2020) on "
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
                                                              "http://opcfoundation.org/UA/:SubscriptionDiagnosticsArray"
                                                                  + " (declaration i=2023, owner"
                                                                  + " i=2020) on "
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
            } else if (node != null && !(node instanceof SubscriptionDiagnosticsArrayTypeNode)) {
              result.completeExceptionally(
                  new UaException(
                      StatusCodes.Bad_TypeMismatch,
                      "http://opcfoundation.org/UA/:SubscriptionDiagnosticsArray (declaration"
                          + " i=2023, owner i=2020)"));
            } else {
              result.complete((SubscriptionDiagnosticsArrayTypeNode) node);
            }
          });
    } catch (Exception e) {
      result.completeExceptionally(e);
    }
    return result;
  }

  @Override
  public SessionsDiagnosticsSummaryTypeNode getSessionsDiagnosticsSummaryNode() throws UaException {
    try {
      return getSessionsDiagnosticsSummaryNodeAsync().get();
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
  public CompletableFuture<? extends SessionsDiagnosticsSummaryTypeNode>
      getSessionsDiagnosticsSummaryNodeAsync() {
    CompletableFuture<SessionsDiagnosticsSummaryTypeNode> result = new CompletableFuture<>();
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
                          ExpandedNodeId.parse("i=47").toNodeId(client.getNamespaceTable());
                      if (namespaceIndex == null || referenceId.isEmpty()) {
                        return CompletableFuture.failedFuture(
                            new UaException(
                                StatusCodes.Bad_NodeIdInvalid,
                                "http://opcfoundation.org/UA/:SessionsDiagnosticsSummary"
                                    + " (declaration i=2744, owner i=2020) on "
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
                                        new QualifiedName(
                                            namespaceIndex, "SessionsDiagnosticsSummary"))
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
                                          "http://opcfoundation.org/UA/:SessionsDiagnosticsSummary"
                                              + " (declaration i=2744, owner i=2020) on "
                                              + getNodeId()));
                                }
                                var operation = results[0];
                                if (operation.getStatusCode().getValue()
                                    == StatusCodes.Bad_NoMatch) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_NotFound,
                                          "http://opcfoundation.org/UA/:SessionsDiagnosticsSummary"
                                              + " (declaration i=2744, owner i=2020) on "
                                              + getNodeId()));
                                }
                                if (!operation.getStatusCode().isGood()) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          operation.getStatusCode(),
                                          "http://opcfoundation.org/UA/:SessionsDiagnosticsSummary"
                                              + " (declaration i=2744, owner i=2020)"));
                                }
                                var targets = operation.getTargets();
                                if (targets == null || targets.length == 0) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_UnexpectedError,
                                          "http://opcfoundation.org/UA/:SessionsDiagnosticsSummary"
                                              + " (declaration i=2744, owner i=2020) on "
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
                                            "http://opcfoundation.org/UA/:SessionsDiagnosticsSummary"
                                                + " (declaration i=2744, owner i=2020) on "
                                                + getNodeId()));
                                  }
                                  if (!target.getTargetId().isLocal()) {
                                    return CompletableFuture.failedFuture(
                                        new UaException(
                                            StatusCodes.Bad_NotSupported,
                                            "http://opcfoundation.org/UA/:SessionsDiagnosticsSummary"
                                                + " (declaration i=2744, owner i=2020) on "
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
                                                            "http://opcfoundation.org/UA/:SessionsDiagnosticsSummary"
                                                                + " (declaration i=2744, owner"
                                                                + " i=2020) on "
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
                                                    "http://opcfoundation.org/UA/:SessionsDiagnosticsSummary"
                                                        + " (declaration i=2744, owner i=2020) on "
                                                        + getNodeId()));
                                          }
                                          if (unique.size() != 1) {
                                            return CompletableFuture.failedFuture(
                                                new UaException(
                                                    StatusCodes.Bad_TooManyMatches,
                                                    "http://opcfoundation.org/UA/:SessionsDiagnosticsSummary"
                                                        + " (declaration i=2744, owner i=2020) on "
                                                        + getNodeId()));
                                          }
                                          return client
                                              .getAddressSpace()
                                              .getNodeAsync(unique.iterator().next())
                                              .thenCompose(
                                                  node -> {
                                                    if (node == null
                                                        || node.getNodeClass()
                                                            != NodeClass.Object) {
                                                      return CompletableFuture.failedFuture(
                                                          new UaException(
                                                              StatusCodes.Bad_NodeClassInvalid,
                                                              "http://opcfoundation.org/UA/:SessionsDiagnosticsSummary"
                                                                  + " (declaration i=2744, owner"
                                                                  + " i=2020) on "
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
            } else if (node != null && !(node instanceof SessionsDiagnosticsSummaryTypeNode)) {
              result.completeExceptionally(
                  new UaException(
                      StatusCodes.Bad_TypeMismatch,
                      "http://opcfoundation.org/UA/:SessionsDiagnosticsSummary (declaration i=2744,"
                          + " owner i=2020)"));
            } else {
              result.complete((SessionsDiagnosticsSummaryTypeNode) node);
            }
          });
    } catch (Exception e) {
      result.completeExceptionally(e);
    }
    return result;
  }
}

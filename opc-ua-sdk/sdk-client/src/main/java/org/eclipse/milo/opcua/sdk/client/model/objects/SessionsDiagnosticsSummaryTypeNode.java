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
import org.eclipse.milo.opcua.sdk.client.model.variables.SessionDiagnosticsArrayTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.SessionSecurityDiagnosticsArrayTypeNode;
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
import org.eclipse.milo.opcua.stack.core.types.structured.SessionDiagnosticsDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.SessionSecurityDiagnosticsDataType;
import org.jspecify.annotations.Nullable;

public class SessionsDiagnosticsSummaryTypeNode extends BaseObjectTypeNode
    implements SessionsDiagnosticsSummaryType {
  public SessionsDiagnosticsSummaryTypeNode(
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
  public static ClientViews createViews(SessionsDiagnosticsSummaryTypeNode node) {
    Objects.requireNonNull(node, "node");
    return ClientViews.forNode(node.client, node);
  }

  @Override
  public @Nullable SessionDiagnosticsDataType @Nullable [] getSessionDiagnosticsArray()
      throws UaException {
    SessionDiagnosticsArrayTypeNode node = getSessionDiagnosticsArrayNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SessionDiagnosticsArray (declaration i=2027, owner i=2026)"
              + " on "
              + getNodeId());
    }
    return (SessionDiagnosticsDataType[])
        decodeValue(
            node.getValue().getValue().getValue(),
            SessionDiagnosticsDataType.class,
            ValueRanks.OneDimension);
  }

  @Override
  public void setSessionDiagnosticsArray(@Nullable SessionDiagnosticsDataType @Nullable [] value)
      throws UaException {
    SessionDiagnosticsArrayTypeNode node = getSessionDiagnosticsArrayNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SessionDiagnosticsArray (declaration i=2027, owner i=2026)"
              + " on "
              + getNodeId());
    }
    node.setValue(
        new Variant(encodeValue(value, SessionDiagnosticsDataType.class, ValueRanks.OneDimension)));
  }

  @Override
  public @Nullable SessionDiagnosticsDataType @Nullable [] readSessionDiagnosticsArray()
      throws UaException {
    try {
      return readSessionDiagnosticsArrayAsync().get();
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
  public void writeSessionDiagnosticsArray(@Nullable SessionDiagnosticsDataType @Nullable [] value)
      throws UaException {
    try {
      StatusCode statusCode = writeSessionDiagnosticsArrayAsync(value).get();
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
  public CompletableFuture<? extends @Nullable SessionDiagnosticsDataType @Nullable []>
      readSessionDiagnosticsArrayAsync() {
    return getSessionDiagnosticsArrayNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:SessionDiagnosticsArray (declaration i=2027,"
                            + " owner i=2026) on "
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
                return (SessionDiagnosticsDataType[])
                    decodeValue(
                        v.getValue().getValue(),
                        SessionDiagnosticsDataType.class,
                        ValueRanks.OneDimension);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeSessionDiagnosticsArrayAsync(
      @Nullable SessionDiagnosticsDataType @Nullable [] sessionDiagnosticsArray) {
    return getSessionDiagnosticsArrayNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:SessionDiagnosticsArray (declaration i=2027,"
                            + " owner i=2026) on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(
                            encodeValue(
                                sessionDiagnosticsArray,
                                SessionDiagnosticsDataType.class,
                                ValueRanks.OneDimension)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public SessionDiagnosticsArrayTypeNode getSessionDiagnosticsArrayNode() throws UaException {
    try {
      return getSessionDiagnosticsArrayNodeAsync().get();
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
  public CompletableFuture<? extends SessionDiagnosticsArrayTypeNode>
      getSessionDiagnosticsArrayNodeAsync() {
    CompletableFuture<SessionDiagnosticsArrayTypeNode> result = new CompletableFuture<>();
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
                                "http://opcfoundation.org/UA/:SessionDiagnosticsArray (declaration"
                                    + " i=2027, owner i=2026) on "
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
                                            namespaceIndex, "SessionDiagnosticsArray"))
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
                                          "http://opcfoundation.org/UA/:SessionDiagnosticsArray"
                                              + " (declaration i=2027, owner i=2026) on "
                                              + getNodeId()));
                                }
                                var operation = results[0];
                                if (operation.getStatusCode().getValue()
                                    == StatusCodes.Bad_NoMatch) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_NotFound,
                                          "http://opcfoundation.org/UA/:SessionDiagnosticsArray"
                                              + " (declaration i=2027, owner i=2026) on "
                                              + getNodeId()));
                                }
                                if (!operation.getStatusCode().isGood()) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          operation.getStatusCode(),
                                          "http://opcfoundation.org/UA/:SessionDiagnosticsArray"
                                              + " (declaration i=2027, owner i=2026)"));
                                }
                                var targets = operation.getTargets();
                                if (targets == null || targets.length == 0) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_UnexpectedError,
                                          "http://opcfoundation.org/UA/:SessionDiagnosticsArray"
                                              + " (declaration i=2027, owner i=2026) on "
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
                                            "http://opcfoundation.org/UA/:SessionDiagnosticsArray"
                                                + " (declaration i=2027, owner i=2026) on "
                                                + getNodeId()));
                                  }
                                  if (!target.getTargetId().isLocal()) {
                                    return CompletableFuture.failedFuture(
                                        new UaException(
                                            StatusCodes.Bad_NotSupported,
                                            "http://opcfoundation.org/UA/:SessionDiagnosticsArray"
                                                + " (declaration i=2027, owner i=2026) on "
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
                                                            "http://opcfoundation.org/UA/:SessionDiagnosticsArray"
                                                                + " (declaration i=2027, owner"
                                                                + " i=2026) on "
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
                                                    "http://opcfoundation.org/UA/:SessionDiagnosticsArray"
                                                        + " (declaration i=2027, owner i=2026) on "
                                                        + getNodeId()));
                                          }
                                          if (unique.size() != 1) {
                                            return CompletableFuture.failedFuture(
                                                new UaException(
                                                    StatusCodes.Bad_TooManyMatches,
                                                    "http://opcfoundation.org/UA/:SessionDiagnosticsArray"
                                                        + " (declaration i=2027, owner i=2026) on "
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
                                                              "http://opcfoundation.org/UA/:SessionDiagnosticsArray"
                                                                  + " (declaration i=2027, owner"
                                                                  + " i=2026) on "
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
            } else if (node != null && !(node instanceof SessionDiagnosticsArrayTypeNode)) {
              result.completeExceptionally(
                  new UaException(
                      StatusCodes.Bad_TypeMismatch,
                      "http://opcfoundation.org/UA/:SessionDiagnosticsArray (declaration i=2027,"
                          + " owner i=2026)"));
            } else {
              result.complete((SessionDiagnosticsArrayTypeNode) node);
            }
          });
    } catch (Exception e) {
      result.completeExceptionally(e);
    }
    return result;
  }

  @Override
  public @Nullable SessionSecurityDiagnosticsDataType @Nullable []
      getSessionSecurityDiagnosticsArray() throws UaException {
    SessionSecurityDiagnosticsArrayTypeNode node = getSessionSecurityDiagnosticsArrayNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SessionSecurityDiagnosticsArray (declaration i=2028, owner"
              + " i=2026) on "
              + getNodeId());
    }
    return (SessionSecurityDiagnosticsDataType[])
        decodeValue(
            node.getValue().getValue().getValue(),
            SessionSecurityDiagnosticsDataType.class,
            ValueRanks.OneDimension);
  }

  @Override
  public void setSessionSecurityDiagnosticsArray(
      @Nullable SessionSecurityDiagnosticsDataType @Nullable [] value) throws UaException {
    SessionSecurityDiagnosticsArrayTypeNode node = getSessionSecurityDiagnosticsArrayNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SessionSecurityDiagnosticsArray (declaration i=2028, owner"
              + " i=2026) on "
              + getNodeId());
    }
    node.setValue(
        new Variant(
            encodeValue(value, SessionSecurityDiagnosticsDataType.class, ValueRanks.OneDimension)));
  }

  @Override
  public @Nullable SessionSecurityDiagnosticsDataType @Nullable []
      readSessionSecurityDiagnosticsArray() throws UaException {
    try {
      return readSessionSecurityDiagnosticsArrayAsync().get();
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
  public void writeSessionSecurityDiagnosticsArray(
      @Nullable SessionSecurityDiagnosticsDataType @Nullable [] value) throws UaException {
    try {
      StatusCode statusCode = writeSessionSecurityDiagnosticsArrayAsync(value).get();
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
  public CompletableFuture<? extends @Nullable SessionSecurityDiagnosticsDataType @Nullable []>
      readSessionSecurityDiagnosticsArrayAsync() {
    return getSessionSecurityDiagnosticsArrayNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:SessionSecurityDiagnosticsArray (declaration"
                            + " i=2028, owner i=2026) on "
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
                return (SessionSecurityDiagnosticsDataType[])
                    decodeValue(
                        v.getValue().getValue(),
                        SessionSecurityDiagnosticsDataType.class,
                        ValueRanks.OneDimension);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeSessionSecurityDiagnosticsArrayAsync(
      @Nullable SessionSecurityDiagnosticsDataType @Nullable [] sessionSecurityDiagnosticsArray) {
    return getSessionSecurityDiagnosticsArrayNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:SessionSecurityDiagnosticsArray (declaration"
                            + " i=2028, owner i=2026) on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(
                            encodeValue(
                                sessionSecurityDiagnosticsArray,
                                SessionSecurityDiagnosticsDataType.class,
                                ValueRanks.OneDimension)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public SessionSecurityDiagnosticsArrayTypeNode getSessionSecurityDiagnosticsArrayNode()
      throws UaException {
    try {
      return getSessionSecurityDiagnosticsArrayNodeAsync().get();
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
  public CompletableFuture<? extends SessionSecurityDiagnosticsArrayTypeNode>
      getSessionSecurityDiagnosticsArrayNodeAsync() {
    CompletableFuture<SessionSecurityDiagnosticsArrayTypeNode> result = new CompletableFuture<>();
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
                                "http://opcfoundation.org/UA/:SessionSecurityDiagnosticsArray"
                                    + " (declaration i=2028, owner i=2026) on "
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
                                            namespaceIndex, "SessionSecurityDiagnosticsArray"))
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
                                          "http://opcfoundation.org/UA/:SessionSecurityDiagnosticsArray"
                                              + " (declaration i=2028, owner i=2026) on "
                                              + getNodeId()));
                                }
                                var operation = results[0];
                                if (operation.getStatusCode().getValue()
                                    == StatusCodes.Bad_NoMatch) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_NotFound,
                                          "http://opcfoundation.org/UA/:SessionSecurityDiagnosticsArray"
                                              + " (declaration i=2028, owner i=2026) on "
                                              + getNodeId()));
                                }
                                if (!operation.getStatusCode().isGood()) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          operation.getStatusCode(),
                                          "http://opcfoundation.org/UA/:SessionSecurityDiagnosticsArray"
                                              + " (declaration i=2028, owner i=2026)"));
                                }
                                var targets = operation.getTargets();
                                if (targets == null || targets.length == 0) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_UnexpectedError,
                                          "http://opcfoundation.org/UA/:SessionSecurityDiagnosticsArray"
                                              + " (declaration i=2028, owner i=2026) on "
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
                                            "http://opcfoundation.org/UA/:SessionSecurityDiagnosticsArray"
                                                + " (declaration i=2028, owner i=2026) on "
                                                + getNodeId()));
                                  }
                                  if (!target.getTargetId().isLocal()) {
                                    return CompletableFuture.failedFuture(
                                        new UaException(
                                            StatusCodes.Bad_NotSupported,
                                            "http://opcfoundation.org/UA/:SessionSecurityDiagnosticsArray"
                                                + " (declaration i=2028, owner i=2026) on "
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
                                                            "http://opcfoundation.org/UA/:SessionSecurityDiagnosticsArray"
                                                                + " (declaration i=2028, owner"
                                                                + " i=2026) on "
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
                                                    "http://opcfoundation.org/UA/:SessionSecurityDiagnosticsArray"
                                                        + " (declaration i=2028, owner i=2026) on "
                                                        + getNodeId()));
                                          }
                                          if (unique.size() != 1) {
                                            return CompletableFuture.failedFuture(
                                                new UaException(
                                                    StatusCodes.Bad_TooManyMatches,
                                                    "http://opcfoundation.org/UA/:SessionSecurityDiagnosticsArray"
                                                        + " (declaration i=2028, owner i=2026) on "
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
                                                              "http://opcfoundation.org/UA/:SessionSecurityDiagnosticsArray"
                                                                  + " (declaration i=2028, owner"
                                                                  + " i=2026) on "
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
            } else if (node != null && !(node instanceof SessionSecurityDiagnosticsArrayTypeNode)) {
              result.completeExceptionally(
                  new UaException(
                      StatusCodes.Bad_TypeMismatch,
                      "http://opcfoundation.org/UA/:SessionSecurityDiagnosticsArray (declaration"
                          + " i=2028, owner i=2026)"));
            } else {
              result.complete((SessionSecurityDiagnosticsArrayTypeNode) node);
            }
          });
    } catch (Exception e) {
      result.completeExceptionally(e);
    }
    return result;
  }
}

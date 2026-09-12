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

import com.digitalpetri.opcua.uanodeset.runtime.client.ClientDataTypes;
import com.digitalpetri.opcua.uanodeset.runtime.client.ClientViews;
import com.digitalpetri.opcua.uanodeset.runtime.methods.MethodCallOptions;
import com.digitalpetri.opcua.uanodeset.runtime.methods.MethodCallResult;
import java.lang.reflect.Array;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.ServerStatusTypeNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.core.ValueRanks;
import org.eclipse.milo.opcua.sdk.core.model.methods.ServerTypeGetMonitoredItemsOutputs;
import org.eclipse.milo.opcua.sdk.core.typetree.DataTypeTree;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.types.UaEnumeratedType;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.OptionSetUInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UNumber;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ServerState;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowsePath;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.RelativePath;
import org.eclipse.milo.opcua.stack.core.types.structured.RelativePathElement;
import org.eclipse.milo.opcua.stack.core.types.structured.RequestHeader;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.ServerStatusDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.TimeZoneDataType;
import org.eclipse.milo.opcua.stack.core.util.ArrayUtil;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

public class ServerTypeNode extends BaseObjectTypeNode implements ServerType {
  public ServerTypeNode(
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
  public static ClientViews createViews(ServerTypeNode node) {
    Objects.requireNonNull(node, "node");
    return ClientViews.forNode(node.client, node);
  }

  @Override
  public @Nullable String @Nullable [] getServerArray() throws UaException {
    PropertyTypeNode node = getServerArrayNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ServerArray (declaration i=2005, owner i=2004)"
              + " on "
              + getNodeId());
    }
    return (String[]) node.getValue().getValue().getValue();
  }

  @Override
  public void setServerArray(@Nullable String @Nullable [] value) throws UaException {
    PropertyTypeNode node = getServerArrayNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ServerArray (declaration i=2005, owner i=2004)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable String @Nullable [] readServerArray() throws UaException {
    try {
      return readServerArrayAsync().get();
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
  public void writeServerArray(@Nullable String @Nullable [] value) throws UaException {
    try {
      StatusCode statusCode = writeServerArrayAsync(value).get();
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
  public CompletableFuture<? extends @Nullable String @Nullable []> readServerArrayAsync() {
    return getServerArrayNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ServerArray (declaration i=2005, owner"
                            + " i=2004) on "
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
  public CompletableFuture<StatusCode> writeServerArrayAsync(
      @Nullable String @Nullable [] serverArray) {
    return getServerArrayNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ServerArray (declaration i=2005, owner"
                            + " i=2004) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(serverArray));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getServerArrayNode() throws UaException {
    try {
      return getServerArrayNodeAsync().get();
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
  public CompletableFuture<? extends PropertyTypeNode> getServerArrayNodeAsync() {
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
                                "http://opcfoundation.org/UA/:ServerArray (declaration i=2005,"
                                    + " owner i=2004) on "
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
                                        new QualifiedName(namespaceIndex, "ServerArray"))
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
                                          "http://opcfoundation.org/UA/:ServerArray (declaration"
                                              + " i=2005, owner i=2004) on "
                                              + getNodeId()));
                                }
                                var operation = results[0];
                                if (operation.getStatusCode().getValue()
                                    == StatusCodes.Bad_NoMatch) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_NotFound,
                                          "http://opcfoundation.org/UA/:ServerArray (declaration"
                                              + " i=2005, owner i=2004) on "
                                              + getNodeId()));
                                }
                                if (!operation.getStatusCode().isGood()) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          operation.getStatusCode(),
                                          "http://opcfoundation.org/UA/:ServerArray (declaration"
                                              + " i=2005, owner i=2004)"));
                                }
                                var targets = operation.getTargets();
                                if (targets == null || targets.length == 0) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_UnexpectedError,
                                          "http://opcfoundation.org/UA/:ServerArray (declaration"
                                              + " i=2005, owner i=2004) on "
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
                                            "http://opcfoundation.org/UA/:ServerArray (declaration"
                                                + " i=2005, owner i=2004) on "
                                                + getNodeId()));
                                  }
                                  if (!target.getTargetId().isLocal()) {
                                    return CompletableFuture.failedFuture(
                                        new UaException(
                                            StatusCodes.Bad_NotSupported,
                                            "http://opcfoundation.org/UA/:ServerArray (declaration"
                                                + " i=2005, owner i=2004) on "
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
                                                            "http://opcfoundation.org/UA/:ServerArray"
                                                                + " (declaration i=2005, owner"
                                                                + " i=2004) on "
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
                                                    "http://opcfoundation.org/UA/:ServerArray"
                                                        + " (declaration i=2005, owner i=2004) on "
                                                        + getNodeId()));
                                          }
                                          if (unique.size() != 1) {
                                            return CompletableFuture.failedFuture(
                                                new UaException(
                                                    StatusCodes.Bad_TooManyMatches,
                                                    "http://opcfoundation.org/UA/:ServerArray"
                                                        + " (declaration i=2005, owner i=2004) on "
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
                                                              "http://opcfoundation.org/UA/:ServerArray"
                                                                  + " (declaration i=2005, owner"
                                                                  + " i=2004) on "
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
                      "http://opcfoundation.org/UA/:ServerArray (declaration i=2005, owner"
                          + " i=2004)"));
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
  public @Nullable String @Nullable [] getNamespaceArray() throws UaException {
    PropertyTypeNode node = getNamespaceArrayNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:NamespaceArray (declaration i=2006, owner i=2004)"
              + " on "
              + getNodeId());
    }
    return (String[]) node.getValue().getValue().getValue();
  }

  @Override
  public void setNamespaceArray(@Nullable String @Nullable [] value) throws UaException {
    PropertyTypeNode node = getNamespaceArrayNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:NamespaceArray (declaration i=2006, owner i=2004)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable String @Nullable [] readNamespaceArray() throws UaException {
    try {
      return readNamespaceArrayAsync().get();
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
  public void writeNamespaceArray(@Nullable String @Nullable [] value) throws UaException {
    try {
      StatusCode statusCode = writeNamespaceArrayAsync(value).get();
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
  public CompletableFuture<? extends @Nullable String @Nullable []> readNamespaceArrayAsync() {
    return getNamespaceArrayNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:NamespaceArray (declaration i=2006, owner"
                            + " i=2004) on "
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
  public CompletableFuture<StatusCode> writeNamespaceArrayAsync(
      @Nullable String @Nullable [] namespaceArray) {
    return getNamespaceArrayNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:NamespaceArray (declaration i=2006, owner"
                            + " i=2004) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(namespaceArray));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getNamespaceArrayNode() throws UaException {
    try {
      return getNamespaceArrayNodeAsync().get();
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
  public CompletableFuture<? extends PropertyTypeNode> getNamespaceArrayNodeAsync() {
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
                                "http://opcfoundation.org/UA/:NamespaceArray (declaration i=2006,"
                                    + " owner i=2004) on "
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
                                        new QualifiedName(namespaceIndex, "NamespaceArray"))
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
                                          "http://opcfoundation.org/UA/:NamespaceArray (declaration"
                                              + " i=2006, owner i=2004) on "
                                              + getNodeId()));
                                }
                                var operation = results[0];
                                if (operation.getStatusCode().getValue()
                                    == StatusCodes.Bad_NoMatch) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_NotFound,
                                          "http://opcfoundation.org/UA/:NamespaceArray (declaration"
                                              + " i=2006, owner i=2004) on "
                                              + getNodeId()));
                                }
                                if (!operation.getStatusCode().isGood()) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          operation.getStatusCode(),
                                          "http://opcfoundation.org/UA/:NamespaceArray (declaration"
                                              + " i=2006, owner i=2004)"));
                                }
                                var targets = operation.getTargets();
                                if (targets == null || targets.length == 0) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_UnexpectedError,
                                          "http://opcfoundation.org/UA/:NamespaceArray (declaration"
                                              + " i=2006, owner i=2004) on "
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
                                            "http://opcfoundation.org/UA/:NamespaceArray"
                                                + " (declaration i=2006, owner i=2004) on "
                                                + getNodeId()));
                                  }
                                  if (!target.getTargetId().isLocal()) {
                                    return CompletableFuture.failedFuture(
                                        new UaException(
                                            StatusCodes.Bad_NotSupported,
                                            "http://opcfoundation.org/UA/:NamespaceArray"
                                                + " (declaration i=2006, owner i=2004) on "
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
                                                            "http://opcfoundation.org/UA/:NamespaceArray"
                                                                + " (declaration i=2006, owner"
                                                                + " i=2004) on "
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
                                                    "http://opcfoundation.org/UA/:NamespaceArray"
                                                        + " (declaration i=2006, owner i=2004) on "
                                                        + getNodeId()));
                                          }
                                          if (unique.size() != 1) {
                                            return CompletableFuture.failedFuture(
                                                new UaException(
                                                    StatusCodes.Bad_TooManyMatches,
                                                    "http://opcfoundation.org/UA/:NamespaceArray"
                                                        + " (declaration i=2006, owner i=2004) on "
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
                                                              "http://opcfoundation.org/UA/:NamespaceArray"
                                                                  + " (declaration i=2006, owner"
                                                                  + " i=2004) on "
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
                      "http://opcfoundation.org/UA/:NamespaceArray (declaration i=2006, owner"
                          + " i=2004)"));
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
  public @Nullable UInteger getUrisVersion() throws UaException {
    PropertyTypeNode node = getUrisVersionNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:UrisVersion (declaration i=15003, owner i=2004)"
              + " on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setUrisVersion(@Nullable UInteger value) throws UaException {
    PropertyTypeNode node = getUrisVersionNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:UrisVersion (declaration i=15003, owner i=2004)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UInteger readUrisVersion() throws UaException {
    try {
      return readUrisVersionAsync().get();
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
  public void writeUrisVersion(@Nullable UInteger value) throws UaException {
    try {
      StatusCode statusCode = writeUrisVersionAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UInteger> readUrisVersionAsync() {
    return getUrisVersionNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:UrisVersion (declaration i=15003, owner"
                            + " i=2004) on "
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
  public CompletableFuture<StatusCode> writeUrisVersionAsync(@Nullable UInteger urisVersion) {
    return getUrisVersionNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:UrisVersion (declaration i=15003, owner"
                            + " i=2004) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(urisVersion));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getUrisVersionNode() throws UaException {
    try {
      return getUrisVersionNodeAsync().get();
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
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getUrisVersionNodeAsync() {
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
                                "http://opcfoundation.org/UA/:UrisVersion (declaration i=15003,"
                                    + " owner i=2004) on "
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
                                        new QualifiedName(namespaceIndex, "UrisVersion"))
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
                                          "http://opcfoundation.org/UA/:UrisVersion (declaration"
                                              + " i=15003, owner i=2004) on "
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
                                          "http://opcfoundation.org/UA/:UrisVersion (declaration"
                                              + " i=15003, owner i=2004)"));
                                }
                                var targets = operation.getTargets();
                                if (targets == null || targets.length == 0) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_UnexpectedError,
                                          "http://opcfoundation.org/UA/:UrisVersion (declaration"
                                              + " i=15003, owner i=2004) on "
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
                                            "http://opcfoundation.org/UA/:UrisVersion (declaration"
                                                + " i=15003, owner i=2004) on "
                                                + getNodeId()));
                                  }
                                  if (!target.getTargetId().isLocal()) {
                                    return CompletableFuture.failedFuture(
                                        new UaException(
                                            StatusCodes.Bad_NotSupported,
                                            "http://opcfoundation.org/UA/:UrisVersion (declaration"
                                                + " i=15003, owner i=2004) on "
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
                                                            "http://opcfoundation.org/UA/:UrisVersion"
                                                                + " (declaration i=15003, owner"
                                                                + " i=2004) on "
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
                                                    "http://opcfoundation.org/UA/:UrisVersion"
                                                        + " (declaration i=15003, owner i=2004) on "
                                                        + getNodeId()));
                                          }
                                          if (unique.size() != 1) {
                                            return CompletableFuture.failedFuture(
                                                new UaException(
                                                    StatusCodes.Bad_TooManyMatches,
                                                    "http://opcfoundation.org/UA/:UrisVersion"
                                                        + " (declaration i=15003, owner i=2004) on "
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
                                                              "http://opcfoundation.org/UA/:UrisVersion"
                                                                  + " (declaration i=15003, owner"
                                                                  + " i=2004) on "
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
                      "http://opcfoundation.org/UA/:UrisVersion (declaration i=15003, owner"
                          + " i=2004)"));
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
  public @Nullable UByte getServiceLevel() throws UaException {
    PropertyTypeNode node = getServiceLevelNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ServiceLevel (declaration i=2008, owner i=2004)"
              + " on "
              + getNodeId());
    }
    return (UByte) node.getValue().getValue().getValue();
  }

  @Override
  public void setServiceLevel(@Nullable UByte value) throws UaException {
    PropertyTypeNode node = getServiceLevelNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ServiceLevel (declaration i=2008, owner i=2004)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UByte readServiceLevel() throws UaException {
    try {
      return readServiceLevelAsync().get();
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
  public void writeServiceLevel(@Nullable UByte value) throws UaException {
    try {
      StatusCode statusCode = writeServiceLevelAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UByte> readServiceLevelAsync() {
    return getServiceLevelNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ServiceLevel (declaration i=2008, owner"
                            + " i=2004) on "
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
                return (UByte) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeServiceLevelAsync(@Nullable UByte serviceLevel) {
    return getServiceLevelNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ServiceLevel (declaration i=2008, owner"
                            + " i=2004) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(serviceLevel));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getServiceLevelNode() throws UaException {
    try {
      return getServiceLevelNodeAsync().get();
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
  public CompletableFuture<? extends PropertyTypeNode> getServiceLevelNodeAsync() {
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
                                "http://opcfoundation.org/UA/:ServiceLevel (declaration i=2008,"
                                    + " owner i=2004) on "
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
                                        new QualifiedName(namespaceIndex, "ServiceLevel"))
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
                                          "http://opcfoundation.org/UA/:ServiceLevel (declaration"
                                              + " i=2008, owner i=2004) on "
                                              + getNodeId()));
                                }
                                var operation = results[0];
                                if (operation.getStatusCode().getValue()
                                    == StatusCodes.Bad_NoMatch) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_NotFound,
                                          "http://opcfoundation.org/UA/:ServiceLevel (declaration"
                                              + " i=2008, owner i=2004) on "
                                              + getNodeId()));
                                }
                                if (!operation.getStatusCode().isGood()) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          operation.getStatusCode(),
                                          "http://opcfoundation.org/UA/:ServiceLevel (declaration"
                                              + " i=2008, owner i=2004)"));
                                }
                                var targets = operation.getTargets();
                                if (targets == null || targets.length == 0) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_UnexpectedError,
                                          "http://opcfoundation.org/UA/:ServiceLevel (declaration"
                                              + " i=2008, owner i=2004) on "
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
                                            "http://opcfoundation.org/UA/:ServiceLevel (declaration"
                                                + " i=2008, owner i=2004) on "
                                                + getNodeId()));
                                  }
                                  if (!target.getTargetId().isLocal()) {
                                    return CompletableFuture.failedFuture(
                                        new UaException(
                                            StatusCodes.Bad_NotSupported,
                                            "http://opcfoundation.org/UA/:ServiceLevel (declaration"
                                                + " i=2008, owner i=2004) on "
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
                                                            "http://opcfoundation.org/UA/:ServiceLevel"
                                                                + " (declaration i=2008, owner"
                                                                + " i=2004) on "
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
                                                    "http://opcfoundation.org/UA/:ServiceLevel"
                                                        + " (declaration i=2008, owner i=2004) on "
                                                        + getNodeId()));
                                          }
                                          if (unique.size() != 1) {
                                            return CompletableFuture.failedFuture(
                                                new UaException(
                                                    StatusCodes.Bad_TooManyMatches,
                                                    "http://opcfoundation.org/UA/:ServiceLevel"
                                                        + " (declaration i=2008, owner i=2004) on "
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
                                                              "http://opcfoundation.org/UA/:ServiceLevel"
                                                                  + " (declaration i=2008, owner"
                                                                  + " i=2004) on "
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
                      "http://opcfoundation.org/UA/:ServiceLevel (declaration i=2008, owner"
                          + " i=2004)"));
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
  public @Nullable Boolean getAuditing() throws UaException {
    PropertyTypeNode node = getAuditingNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:Auditing (declaration i=2742, owner i=2004)"
              + " on "
              + getNodeId());
    }
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setAuditing(@Nullable Boolean value) throws UaException {
    PropertyTypeNode node = getAuditingNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:Auditing (declaration i=2742, owner i=2004)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable Boolean readAuditing() throws UaException {
    try {
      return readAuditingAsync().get();
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
  public void writeAuditing(@Nullable Boolean value) throws UaException {
    try {
      StatusCode statusCode = writeAuditingAsync(value).get();
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
  public CompletableFuture<? extends @Nullable Boolean> readAuditingAsync() {
    return getAuditingNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:Auditing (declaration i=2742, owner i=2004)"
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
  public CompletableFuture<StatusCode> writeAuditingAsync(@Nullable Boolean auditing) {
    return getAuditingNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:Auditing (declaration i=2742, owner i=2004)"
                            + " on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(auditing));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getAuditingNode() throws UaException {
    try {
      return getAuditingNodeAsync().get();
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
  public CompletableFuture<? extends PropertyTypeNode> getAuditingNodeAsync() {
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
                                "http://opcfoundation.org/UA/:Auditing (declaration i=2742, owner"
                                    + " i=2004) on "
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
                                        new QualifiedName(namespaceIndex, "Auditing"))
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
                                          "http://opcfoundation.org/UA/:Auditing (declaration"
                                              + " i=2742, owner i=2004) on "
                                              + getNodeId()));
                                }
                                var operation = results[0];
                                if (operation.getStatusCode().getValue()
                                    == StatusCodes.Bad_NoMatch) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_NotFound,
                                          "http://opcfoundation.org/UA/:Auditing (declaration"
                                              + " i=2742, owner i=2004) on "
                                              + getNodeId()));
                                }
                                if (!operation.getStatusCode().isGood()) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          operation.getStatusCode(),
                                          "http://opcfoundation.org/UA/:Auditing (declaration"
                                              + " i=2742, owner i=2004)"));
                                }
                                var targets = operation.getTargets();
                                if (targets == null || targets.length == 0) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_UnexpectedError,
                                          "http://opcfoundation.org/UA/:Auditing (declaration"
                                              + " i=2742, owner i=2004) on "
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
                                            "http://opcfoundation.org/UA/:Auditing (declaration"
                                                + " i=2742, owner i=2004) on "
                                                + getNodeId()));
                                  }
                                  if (!target.getTargetId().isLocal()) {
                                    return CompletableFuture.failedFuture(
                                        new UaException(
                                            StatusCodes.Bad_NotSupported,
                                            "http://opcfoundation.org/UA/:Auditing (declaration"
                                                + " i=2742, owner i=2004) on "
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
                                                            "http://opcfoundation.org/UA/:Auditing"
                                                                + " (declaration i=2742, owner"
                                                                + " i=2004) on "
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
                                                    "http://opcfoundation.org/UA/:Auditing"
                                                        + " (declaration i=2742, owner i=2004) on "
                                                        + getNodeId()));
                                          }
                                          if (unique.size() != 1) {
                                            return CompletableFuture.failedFuture(
                                                new UaException(
                                                    StatusCodes.Bad_TooManyMatches,
                                                    "http://opcfoundation.org/UA/:Auditing"
                                                        + " (declaration i=2742, owner i=2004) on "
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
                                                              "http://opcfoundation.org/UA/:Auditing"
                                                                  + " (declaration i=2742, owner"
                                                                  + " i=2004) on "
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
                      "http://opcfoundation.org/UA/:Auditing (declaration i=2742, owner i=2004)"));
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
  public @Nullable DateTime getEstimatedReturnTime() throws UaException {
    PropertyTypeNode node = getEstimatedReturnTimeNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:EstimatedReturnTime (declaration i=12882, owner i=2004)"
              + " on "
              + getNodeId());
    }
    return (DateTime) node.getValue().getValue().getValue();
  }

  @Override
  public void setEstimatedReturnTime(@Nullable DateTime value) throws UaException {
    PropertyTypeNode node = getEstimatedReturnTimeNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:EstimatedReturnTime (declaration i=12882, owner i=2004)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable DateTime readEstimatedReturnTime() throws UaException {
    try {
      return readEstimatedReturnTimeAsync().get();
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
  public void writeEstimatedReturnTime(@Nullable DateTime value) throws UaException {
    try {
      StatusCode statusCode = writeEstimatedReturnTimeAsync(value).get();
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
  public CompletableFuture<? extends @Nullable DateTime> readEstimatedReturnTimeAsync() {
    return getEstimatedReturnTimeNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:EstimatedReturnTime (declaration i=12882,"
                            + " owner i=2004) on "
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
  public CompletableFuture<StatusCode> writeEstimatedReturnTimeAsync(
      @Nullable DateTime estimatedReturnTime) {
    return getEstimatedReturnTimeNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:EstimatedReturnTime (declaration i=12882,"
                            + " owner i=2004) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(estimatedReturnTime));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getEstimatedReturnTimeNode() throws UaException {
    try {
      return getEstimatedReturnTimeNodeAsync().get();
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
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getEstimatedReturnTimeNodeAsync() {
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
                                "http://opcfoundation.org/UA/:EstimatedReturnTime (declaration"
                                    + " i=12882, owner i=2004) on "
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
                                        new QualifiedName(namespaceIndex, "EstimatedReturnTime"))
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
                                          "http://opcfoundation.org/UA/:EstimatedReturnTime"
                                              + " (declaration i=12882, owner i=2004) on "
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
                                          "http://opcfoundation.org/UA/:EstimatedReturnTime"
                                              + " (declaration i=12882, owner i=2004)"));
                                }
                                var targets = operation.getTargets();
                                if (targets == null || targets.length == 0) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_UnexpectedError,
                                          "http://opcfoundation.org/UA/:EstimatedReturnTime"
                                              + " (declaration i=12882, owner i=2004) on "
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
                                            "http://opcfoundation.org/UA/:EstimatedReturnTime"
                                                + " (declaration i=12882, owner i=2004) on "
                                                + getNodeId()));
                                  }
                                  if (!target.getTargetId().isLocal()) {
                                    return CompletableFuture.failedFuture(
                                        new UaException(
                                            StatusCodes.Bad_NotSupported,
                                            "http://opcfoundation.org/UA/:EstimatedReturnTime"
                                                + " (declaration i=12882, owner i=2004) on "
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
                                                            "http://opcfoundation.org/UA/:EstimatedReturnTime"
                                                                + " (declaration i=12882, owner"
                                                                + " i=2004) on "
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
                                                    "http://opcfoundation.org/UA/:EstimatedReturnTime"
                                                        + " (declaration i=12882, owner i=2004) on "
                                                        + getNodeId()));
                                          }
                                          if (unique.size() != 1) {
                                            return CompletableFuture.failedFuture(
                                                new UaException(
                                                    StatusCodes.Bad_TooManyMatches,
                                                    "http://opcfoundation.org/UA/:EstimatedReturnTime"
                                                        + " (declaration i=12882, owner i=2004) on "
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
                                                              "http://opcfoundation.org/UA/:EstimatedReturnTime"
                                                                  + " (declaration i=12882, owner"
                                                                  + " i=2004) on "
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
                      "http://opcfoundation.org/UA/:EstimatedReturnTime (declaration i=12882, owner"
                          + " i=2004)"));
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
  public @Nullable TimeZoneDataType getLocalTime() throws UaException {
    PropertyTypeNode node = getLocalTimeNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:LocalTime (declaration i=17612, owner i=2004)"
              + " on "
              + getNodeId());
    }
    return (TimeZoneDataType)
        decodeValue(
            node.getValue().getValue().getValue(), TimeZoneDataType.class, ValueRanks.Scalar);
  }

  @Override
  public void setLocalTime(@Nullable TimeZoneDataType value) throws UaException {
    PropertyTypeNode node = getLocalTimeNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:LocalTime (declaration i=17612, owner i=2004)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(encodeValue(value, TimeZoneDataType.class, ValueRanks.Scalar)));
  }

  @Override
  public @Nullable TimeZoneDataType readLocalTime() throws UaException {
    try {
      return readLocalTimeAsync().get();
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
  public void writeLocalTime(@Nullable TimeZoneDataType value) throws UaException {
    try {
      StatusCode statusCode = writeLocalTimeAsync(value).get();
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
  public CompletableFuture<? extends @Nullable TimeZoneDataType> readLocalTimeAsync() {
    return getLocalTimeNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:LocalTime (declaration i=17612, owner i=2004)"
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
                return (TimeZoneDataType)
                    decodeValue(v.getValue().getValue(), TimeZoneDataType.class, ValueRanks.Scalar);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeLocalTimeAsync(@Nullable TimeZoneDataType localTime) {
    return getLocalTimeNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:LocalTime (declaration i=17612, owner i=2004)"
                            + " on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(
                            encodeValue(localTime, TimeZoneDataType.class, ValueRanks.Scalar)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getLocalTimeNode() throws UaException {
    try {
      return getLocalTimeNodeAsync().get();
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
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getLocalTimeNodeAsync() {
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
                                "http://opcfoundation.org/UA/:LocalTime (declaration i=17612, owner"
                                    + " i=2004) on "
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
                                        new QualifiedName(namespaceIndex, "LocalTime"))
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
                                          "http://opcfoundation.org/UA/:LocalTime (declaration"
                                              + " i=17612, owner i=2004) on "
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
                                          "http://opcfoundation.org/UA/:LocalTime (declaration"
                                              + " i=17612, owner i=2004)"));
                                }
                                var targets = operation.getTargets();
                                if (targets == null || targets.length == 0) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_UnexpectedError,
                                          "http://opcfoundation.org/UA/:LocalTime (declaration"
                                              + " i=17612, owner i=2004) on "
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
                                            "http://opcfoundation.org/UA/:LocalTime (declaration"
                                                + " i=17612, owner i=2004) on "
                                                + getNodeId()));
                                  }
                                  if (!target.getTargetId().isLocal()) {
                                    return CompletableFuture.failedFuture(
                                        new UaException(
                                            StatusCodes.Bad_NotSupported,
                                            "http://opcfoundation.org/UA/:LocalTime (declaration"
                                                + " i=17612, owner i=2004) on "
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
                                                            "http://opcfoundation.org/UA/:LocalTime"
                                                                + " (declaration i=17612, owner"
                                                                + " i=2004) on "
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
                                                    "http://opcfoundation.org/UA/:LocalTime"
                                                        + " (declaration i=17612, owner i=2004) on "
                                                        + getNodeId()));
                                          }
                                          if (unique.size() != 1) {
                                            return CompletableFuture.failedFuture(
                                                new UaException(
                                                    StatusCodes.Bad_TooManyMatches,
                                                    "http://opcfoundation.org/UA/:LocalTime"
                                                        + " (declaration i=17612, owner i=2004) on "
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
                                                              "http://opcfoundation.org/UA/:LocalTime"
                                                                  + " (declaration i=17612, owner"
                                                                  + " i=2004) on "
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
                      "http://opcfoundation.org/UA/:LocalTime (declaration i=17612, owner"
                          + " i=2004)"));
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
  public @Nullable ServerStatusDataType getServerStatus() throws UaException {
    ServerStatusTypeNode node = getServerStatusNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ServerStatus (declaration i=2007, owner i=2004)"
              + " on "
              + getNodeId());
    }
    return (ServerStatusDataType)
        decodeValue(
            node.getValue().getValue().getValue(), ServerStatusDataType.class, ValueRanks.Scalar);
  }

  @Override
  public void setServerStatus(@Nullable ServerStatusDataType value) throws UaException {
    ServerStatusTypeNode node = getServerStatusNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ServerStatus (declaration i=2007, owner i=2004)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(encodeValue(value, ServerStatusDataType.class, ValueRanks.Scalar)));
  }

  @Override
  public @Nullable ServerStatusDataType readServerStatus() throws UaException {
    try {
      return readServerStatusAsync().get();
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
  public void writeServerStatus(@Nullable ServerStatusDataType value) throws UaException {
    try {
      StatusCode statusCode = writeServerStatusAsync(value).get();
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
  public CompletableFuture<? extends @Nullable ServerStatusDataType> readServerStatusAsync() {
    return getServerStatusNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ServerStatus (declaration i=2007, owner"
                            + " i=2004) on "
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
                return (ServerStatusDataType)
                    decodeValue(
                        v.getValue().getValue(), ServerStatusDataType.class, ValueRanks.Scalar);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeServerStatusAsync(
      @Nullable ServerStatusDataType serverStatus) {
    return getServerStatusNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ServerStatus (declaration i=2007, owner"
                            + " i=2004) on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(
                            encodeValue(
                                serverStatus, ServerStatusDataType.class, ValueRanks.Scalar)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public ServerStatusTypeNode getServerStatusNode() throws UaException {
    try {
      return getServerStatusNodeAsync().get();
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
  public CompletableFuture<? extends ServerStatusTypeNode> getServerStatusNodeAsync() {
    CompletableFuture<ServerStatusTypeNode> result = new CompletableFuture<>();
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
                                "http://opcfoundation.org/UA/:ServerStatus (declaration i=2007,"
                                    + " owner i=2004) on "
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
                                        new QualifiedName(namespaceIndex, "ServerStatus"))
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
                                          "http://opcfoundation.org/UA/:ServerStatus (declaration"
                                              + " i=2007, owner i=2004) on "
                                              + getNodeId()));
                                }
                                var operation = results[0];
                                if (operation.getStatusCode().getValue()
                                    == StatusCodes.Bad_NoMatch) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_NotFound,
                                          "http://opcfoundation.org/UA/:ServerStatus (declaration"
                                              + " i=2007, owner i=2004) on "
                                              + getNodeId()));
                                }
                                if (!operation.getStatusCode().isGood()) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          operation.getStatusCode(),
                                          "http://opcfoundation.org/UA/:ServerStatus (declaration"
                                              + " i=2007, owner i=2004)"));
                                }
                                var targets = operation.getTargets();
                                if (targets == null || targets.length == 0) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_UnexpectedError,
                                          "http://opcfoundation.org/UA/:ServerStatus (declaration"
                                              + " i=2007, owner i=2004) on "
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
                                            "http://opcfoundation.org/UA/:ServerStatus (declaration"
                                                + " i=2007, owner i=2004) on "
                                                + getNodeId()));
                                  }
                                  if (!target.getTargetId().isLocal()) {
                                    return CompletableFuture.failedFuture(
                                        new UaException(
                                            StatusCodes.Bad_NotSupported,
                                            "http://opcfoundation.org/UA/:ServerStatus (declaration"
                                                + " i=2007, owner i=2004) on "
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
                                                            "http://opcfoundation.org/UA/:ServerStatus"
                                                                + " (declaration i=2007, owner"
                                                                + " i=2004) on "
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
                                                    "http://opcfoundation.org/UA/:ServerStatus"
                                                        + " (declaration i=2007, owner i=2004) on "
                                                        + getNodeId()));
                                          }
                                          if (unique.size() != 1) {
                                            return CompletableFuture.failedFuture(
                                                new UaException(
                                                    StatusCodes.Bad_TooManyMatches,
                                                    "http://opcfoundation.org/UA/:ServerStatus"
                                                        + " (declaration i=2007, owner i=2004) on "
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
                                                              "http://opcfoundation.org/UA/:ServerStatus"
                                                                  + " (declaration i=2007, owner"
                                                                  + " i=2004) on "
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
            } else if (node != null && !(node instanceof ServerStatusTypeNode)) {
              result.completeExceptionally(
                  new UaException(
                      StatusCodes.Bad_TypeMismatch,
                      "http://opcfoundation.org/UA/:ServerStatus (declaration i=2007, owner"
                          + " i=2004)"));
            } else {
              result.complete((ServerStatusTypeNode) node);
            }
          });
    } catch (Exception e) {
      result.completeExceptionally(e);
    }
    return result;
  }

  @Override
  public ServerCapabilitiesTypeNode getServerCapabilitiesNode() throws UaException {
    try {
      return getServerCapabilitiesNodeAsync().get();
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
  public CompletableFuture<? extends ServerCapabilitiesTypeNode> getServerCapabilitiesNodeAsync() {
    CompletableFuture<ServerCapabilitiesTypeNode> result = new CompletableFuture<>();
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
                                "http://opcfoundation.org/UA/:ServerCapabilities (declaration"
                                    + " i=2009, owner i=2004) on "
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
                                        new QualifiedName(namespaceIndex, "ServerCapabilities"))
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
                                          "http://opcfoundation.org/UA/:ServerCapabilities"
                                              + " (declaration i=2009, owner i=2004) on "
                                              + getNodeId()));
                                }
                                var operation = results[0];
                                if (operation.getStatusCode().getValue()
                                    == StatusCodes.Bad_NoMatch) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_NotFound,
                                          "http://opcfoundation.org/UA/:ServerCapabilities"
                                              + " (declaration i=2009, owner i=2004) on "
                                              + getNodeId()));
                                }
                                if (!operation.getStatusCode().isGood()) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          operation.getStatusCode(),
                                          "http://opcfoundation.org/UA/:ServerCapabilities"
                                              + " (declaration i=2009, owner i=2004)"));
                                }
                                var targets = operation.getTargets();
                                if (targets == null || targets.length == 0) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_UnexpectedError,
                                          "http://opcfoundation.org/UA/:ServerCapabilities"
                                              + " (declaration i=2009, owner i=2004) on "
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
                                            "http://opcfoundation.org/UA/:ServerCapabilities"
                                                + " (declaration i=2009, owner i=2004) on "
                                                + getNodeId()));
                                  }
                                  if (!target.getTargetId().isLocal()) {
                                    return CompletableFuture.failedFuture(
                                        new UaException(
                                            StatusCodes.Bad_NotSupported,
                                            "http://opcfoundation.org/UA/:ServerCapabilities"
                                                + " (declaration i=2009, owner i=2004) on "
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
                                                            "http://opcfoundation.org/UA/:ServerCapabilities"
                                                                + " (declaration i=2009, owner"
                                                                + " i=2004) on "
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
                                                    "http://opcfoundation.org/UA/:ServerCapabilities"
                                                        + " (declaration i=2009, owner i=2004) on "
                                                        + getNodeId()));
                                          }
                                          if (unique.size() != 1) {
                                            return CompletableFuture.failedFuture(
                                                new UaException(
                                                    StatusCodes.Bad_TooManyMatches,
                                                    "http://opcfoundation.org/UA/:ServerCapabilities"
                                                        + " (declaration i=2009, owner i=2004) on "
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
                                                              "http://opcfoundation.org/UA/:ServerCapabilities"
                                                                  + " (declaration i=2009, owner"
                                                                  + " i=2004) on "
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
            } else if (node != null && !(node instanceof ServerCapabilitiesTypeNode)) {
              result.completeExceptionally(
                  new UaException(
                      StatusCodes.Bad_TypeMismatch,
                      "http://opcfoundation.org/UA/:ServerCapabilities (declaration i=2009, owner"
                          + " i=2004)"));
            } else {
              result.complete((ServerCapabilitiesTypeNode) node);
            }
          });
    } catch (Exception e) {
      result.completeExceptionally(e);
    }
    return result;
  }

  @Override
  public ServerDiagnosticsTypeNode getServerDiagnosticsNode() throws UaException {
    try {
      return getServerDiagnosticsNodeAsync().get();
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
  public CompletableFuture<? extends ServerDiagnosticsTypeNode> getServerDiagnosticsNodeAsync() {
    CompletableFuture<ServerDiagnosticsTypeNode> result = new CompletableFuture<>();
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
                                "http://opcfoundation.org/UA/:ServerDiagnostics (declaration"
                                    + " i=2010, owner i=2004) on "
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
                                        new QualifiedName(namespaceIndex, "ServerDiagnostics"))
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
                                          "http://opcfoundation.org/UA/:ServerDiagnostics"
                                              + " (declaration i=2010, owner i=2004) on "
                                              + getNodeId()));
                                }
                                var operation = results[0];
                                if (operation.getStatusCode().getValue()
                                    == StatusCodes.Bad_NoMatch) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_NotFound,
                                          "http://opcfoundation.org/UA/:ServerDiagnostics"
                                              + " (declaration i=2010, owner i=2004) on "
                                              + getNodeId()));
                                }
                                if (!operation.getStatusCode().isGood()) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          operation.getStatusCode(),
                                          "http://opcfoundation.org/UA/:ServerDiagnostics"
                                              + " (declaration i=2010, owner i=2004)"));
                                }
                                var targets = operation.getTargets();
                                if (targets == null || targets.length == 0) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_UnexpectedError,
                                          "http://opcfoundation.org/UA/:ServerDiagnostics"
                                              + " (declaration i=2010, owner i=2004) on "
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
                                            "http://opcfoundation.org/UA/:ServerDiagnostics"
                                                + " (declaration i=2010, owner i=2004) on "
                                                + getNodeId()));
                                  }
                                  if (!target.getTargetId().isLocal()) {
                                    return CompletableFuture.failedFuture(
                                        new UaException(
                                            StatusCodes.Bad_NotSupported,
                                            "http://opcfoundation.org/UA/:ServerDiagnostics"
                                                + " (declaration i=2010, owner i=2004) on "
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
                                                            "http://opcfoundation.org/UA/:ServerDiagnostics"
                                                                + " (declaration i=2010, owner"
                                                                + " i=2004) on "
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
                                                    "http://opcfoundation.org/UA/:ServerDiagnostics"
                                                        + " (declaration i=2010, owner i=2004) on "
                                                        + getNodeId()));
                                          }
                                          if (unique.size() != 1) {
                                            return CompletableFuture.failedFuture(
                                                new UaException(
                                                    StatusCodes.Bad_TooManyMatches,
                                                    "http://opcfoundation.org/UA/:ServerDiagnostics"
                                                        + " (declaration i=2010, owner i=2004) on "
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
                                                              "http://opcfoundation.org/UA/:ServerDiagnostics"
                                                                  + " (declaration i=2010, owner"
                                                                  + " i=2004) on "
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
            } else if (node != null && !(node instanceof ServerDiagnosticsTypeNode)) {
              result.completeExceptionally(
                  new UaException(
                      StatusCodes.Bad_TypeMismatch,
                      "http://opcfoundation.org/UA/:ServerDiagnostics (declaration i=2010, owner"
                          + " i=2004)"));
            } else {
              result.complete((ServerDiagnosticsTypeNode) node);
            }
          });
    } catch (Exception e) {
      result.completeExceptionally(e);
    }
    return result;
  }

  @Override
  public VendorServerInfoTypeNode getVendorServerInfoNode() throws UaException {
    try {
      return getVendorServerInfoNodeAsync().get();
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
  public CompletableFuture<? extends VendorServerInfoTypeNode> getVendorServerInfoNodeAsync() {
    CompletableFuture<VendorServerInfoTypeNode> result = new CompletableFuture<>();
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
                                "http://opcfoundation.org/UA/:VendorServerInfo (declaration i=2011,"
                                    + " owner i=2004) on "
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
                                        new QualifiedName(namespaceIndex, "VendorServerInfo"))
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
                                          "http://opcfoundation.org/UA/:VendorServerInfo"
                                              + " (declaration i=2011, owner i=2004) on "
                                              + getNodeId()));
                                }
                                var operation = results[0];
                                if (operation.getStatusCode().getValue()
                                    == StatusCodes.Bad_NoMatch) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_NotFound,
                                          "http://opcfoundation.org/UA/:VendorServerInfo"
                                              + " (declaration i=2011, owner i=2004) on "
                                              + getNodeId()));
                                }
                                if (!operation.getStatusCode().isGood()) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          operation.getStatusCode(),
                                          "http://opcfoundation.org/UA/:VendorServerInfo"
                                              + " (declaration i=2011, owner i=2004)"));
                                }
                                var targets = operation.getTargets();
                                if (targets == null || targets.length == 0) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_UnexpectedError,
                                          "http://opcfoundation.org/UA/:VendorServerInfo"
                                              + " (declaration i=2011, owner i=2004) on "
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
                                            "http://opcfoundation.org/UA/:VendorServerInfo"
                                                + " (declaration i=2011, owner i=2004) on "
                                                + getNodeId()));
                                  }
                                  if (!target.getTargetId().isLocal()) {
                                    return CompletableFuture.failedFuture(
                                        new UaException(
                                            StatusCodes.Bad_NotSupported,
                                            "http://opcfoundation.org/UA/:VendorServerInfo"
                                                + " (declaration i=2011, owner i=2004) on "
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
                                                            "http://opcfoundation.org/UA/:VendorServerInfo"
                                                                + " (declaration i=2011, owner"
                                                                + " i=2004) on "
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
                                                    "http://opcfoundation.org/UA/:VendorServerInfo"
                                                        + " (declaration i=2011, owner i=2004) on "
                                                        + getNodeId()));
                                          }
                                          if (unique.size() != 1) {
                                            return CompletableFuture.failedFuture(
                                                new UaException(
                                                    StatusCodes.Bad_TooManyMatches,
                                                    "http://opcfoundation.org/UA/:VendorServerInfo"
                                                        + " (declaration i=2011, owner i=2004) on "
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
                                                              "http://opcfoundation.org/UA/:VendorServerInfo"
                                                                  + " (declaration i=2011, owner"
                                                                  + " i=2004) on "
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
            } else if (node != null && !(node instanceof VendorServerInfoTypeNode)) {
              result.completeExceptionally(
                  new UaException(
                      StatusCodes.Bad_TypeMismatch,
                      "http://opcfoundation.org/UA/:VendorServerInfo (declaration i=2011, owner"
                          + " i=2004)"));
            } else {
              result.complete((VendorServerInfoTypeNode) node);
            }
          });
    } catch (Exception e) {
      result.completeExceptionally(e);
    }
    return result;
  }

  @Override
  public ServerRedundancyTypeNode getServerRedundancyNode() throws UaException {
    try {
      return getServerRedundancyNodeAsync().get();
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
  public CompletableFuture<? extends ServerRedundancyTypeNode> getServerRedundancyNodeAsync() {
    CompletableFuture<ServerRedundancyTypeNode> result = new CompletableFuture<>();
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
                                "http://opcfoundation.org/UA/:ServerRedundancy (declaration i=2012,"
                                    + " owner i=2004) on "
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
                                        new QualifiedName(namespaceIndex, "ServerRedundancy"))
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
                                          "http://opcfoundation.org/UA/:ServerRedundancy"
                                              + " (declaration i=2012, owner i=2004) on "
                                              + getNodeId()));
                                }
                                var operation = results[0];
                                if (operation.getStatusCode().getValue()
                                    == StatusCodes.Bad_NoMatch) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_NotFound,
                                          "http://opcfoundation.org/UA/:ServerRedundancy"
                                              + " (declaration i=2012, owner i=2004) on "
                                              + getNodeId()));
                                }
                                if (!operation.getStatusCode().isGood()) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          operation.getStatusCode(),
                                          "http://opcfoundation.org/UA/:ServerRedundancy"
                                              + " (declaration i=2012, owner i=2004)"));
                                }
                                var targets = operation.getTargets();
                                if (targets == null || targets.length == 0) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_UnexpectedError,
                                          "http://opcfoundation.org/UA/:ServerRedundancy"
                                              + " (declaration i=2012, owner i=2004) on "
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
                                            "http://opcfoundation.org/UA/:ServerRedundancy"
                                                + " (declaration i=2012, owner i=2004) on "
                                                + getNodeId()));
                                  }
                                  if (!target.getTargetId().isLocal()) {
                                    return CompletableFuture.failedFuture(
                                        new UaException(
                                            StatusCodes.Bad_NotSupported,
                                            "http://opcfoundation.org/UA/:ServerRedundancy"
                                                + " (declaration i=2012, owner i=2004) on "
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
                                                            "http://opcfoundation.org/UA/:ServerRedundancy"
                                                                + " (declaration i=2012, owner"
                                                                + " i=2004) on "
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
                                                    "http://opcfoundation.org/UA/:ServerRedundancy"
                                                        + " (declaration i=2012, owner i=2004) on "
                                                        + getNodeId()));
                                          }
                                          if (unique.size() != 1) {
                                            return CompletableFuture.failedFuture(
                                                new UaException(
                                                    StatusCodes.Bad_TooManyMatches,
                                                    "http://opcfoundation.org/UA/:ServerRedundancy"
                                                        + " (declaration i=2012, owner i=2004) on "
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
                                                              "http://opcfoundation.org/UA/:ServerRedundancy"
                                                                  + " (declaration i=2012, owner"
                                                                  + " i=2004) on "
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
            } else if (node != null && !(node instanceof ServerRedundancyTypeNode)) {
              result.completeExceptionally(
                  new UaException(
                      StatusCodes.Bad_TypeMismatch,
                      "http://opcfoundation.org/UA/:ServerRedundancy (declaration i=2012, owner"
                          + " i=2004)"));
            } else {
              result.complete((ServerRedundancyTypeNode) node);
            }
          });
    } catch (Exception e) {
      result.completeExceptionally(e);
    }
    return result;
  }

  @Override
  public @Nullable NamespacesTypeNode getNamespacesNode() throws UaException {
    try {
      return getNamespacesNodeAsync().get();
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
  public CompletableFuture<? extends @Nullable NamespacesTypeNode> getNamespacesNodeAsync() {
    CompletableFuture<NamespacesTypeNode> result = new CompletableFuture<>();
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
                                "http://opcfoundation.org/UA/:Namespaces (declaration i=11527,"
                                    + " owner i=2004) on "
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
                                        new QualifiedName(namespaceIndex, "Namespaces"))
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
                                          "http://opcfoundation.org/UA/:Namespaces (declaration"
                                              + " i=11527, owner i=2004) on "
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
                                          "http://opcfoundation.org/UA/:Namespaces (declaration"
                                              + " i=11527, owner i=2004)"));
                                }
                                var targets = operation.getTargets();
                                if (targets == null || targets.length == 0) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_UnexpectedError,
                                          "http://opcfoundation.org/UA/:Namespaces (declaration"
                                              + " i=11527, owner i=2004) on "
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
                                            "http://opcfoundation.org/UA/:Namespaces (declaration"
                                                + " i=11527, owner i=2004) on "
                                                + getNodeId()));
                                  }
                                  if (!target.getTargetId().isLocal()) {
                                    return CompletableFuture.failedFuture(
                                        new UaException(
                                            StatusCodes.Bad_NotSupported,
                                            "http://opcfoundation.org/UA/:Namespaces (declaration"
                                                + " i=11527, owner i=2004) on "
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
                                                            "http://opcfoundation.org/UA/:Namespaces"
                                                                + " (declaration i=11527, owner"
                                                                + " i=2004) on "
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
                                                    "http://opcfoundation.org/UA/:Namespaces"
                                                        + " (declaration i=11527, owner i=2004) on "
                                                        + getNodeId()));
                                          }
                                          if (unique.size() != 1) {
                                            return CompletableFuture.failedFuture(
                                                new UaException(
                                                    StatusCodes.Bad_TooManyMatches,
                                                    "http://opcfoundation.org/UA/:Namespaces"
                                                        + " (declaration i=11527, owner i=2004) on "
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
                                                              "http://opcfoundation.org/UA/:Namespaces"
                                                                  + " (declaration i=11527, owner"
                                                                  + " i=2004) on "
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
            } else if (node != null && !(node instanceof NamespacesTypeNode)) {
              result.completeExceptionally(
                  new UaException(
                      StatusCodes.Bad_TypeMismatch,
                      "http://opcfoundation.org/UA/:Namespaces (declaration i=11527, owner"
                          + " i=2004)"));
            } else {
              result.complete((NamespacesTypeNode) node);
            }
          });
    } catch (Exception e) {
      result.completeExceptionally(e);
    }
    return result;
  }

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part5/9.1
   *
   * <p>Resolves the optional member by its namespace-qualified path. Returns null only for
   * confirmed absence. Resolution does not create a UA node. It can perform service I/O and
   * construct or reuse a Java wrapper in Milo's address space cache. A reference can change after
   * lookup.
   *
   * @return the existing member, or null for confirmed absence
   * @throws org.eclipse.milo.opcua.stack.core.UaException if a required node is absent, resolution
   *     fails, or a checked conversion fails
   */
  @NullMarked
  @Override
  public @Nullable UaMethodNode getGetMonitoredItemsMethodNode() throws UaException {
    try {
      return getGetMonitoredItemsMethodNodeAsync().get();
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

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part5/9.1
   *
   * <p>Resolves the optional member by its namespace-qualified path. Returns null only for
   * confirmed absence. Resolution does not create a UA node. It can perform service I/O and
   * construct or reuse a Java wrapper in Milo's address space cache. A reference can change after
   * lookup.
   *
   * <p>Lookup, conversion and service failures complete the future exceptionally. UaException
   * causes preserve OPC UA status. Incompatible plain payload casts can complete exceptionally with
   * ClassCastException. Cancellation does not promise transport cancellation or rollback.
   *
   * @return a nonnull future completing with the existing member, or null for confirmed absence
   */
  @NullMarked
  @Override
  public CompletableFuture<? extends @Nullable UaMethodNode> getGetMonitoredItemsMethodNodeAsync() {
    CompletableFuture<UaMethodNode> result = new CompletableFuture<>();
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
                                "http://opcfoundation.org/UA/:GetMonitoredItems (declaration"
                                    + " i=11489, owner i=2004) on "
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
                                        new QualifiedName(namespaceIndex, "GetMonitoredItems"))
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
                                          "http://opcfoundation.org/UA/:GetMonitoredItems"
                                              + " (declaration i=11489, owner i=2004) on "
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
                                          "http://opcfoundation.org/UA/:GetMonitoredItems"
                                              + " (declaration i=11489, owner i=2004)"));
                                }
                                var targets = operation.getTargets();
                                if (targets == null || targets.length == 0) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_UnexpectedError,
                                          "http://opcfoundation.org/UA/:GetMonitoredItems"
                                              + " (declaration i=11489, owner i=2004) on "
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
                                            "http://opcfoundation.org/UA/:GetMonitoredItems"
                                                + " (declaration i=11489, owner i=2004) on "
                                                + getNodeId()));
                                  }
                                  if (!target.getTargetId().isLocal()) {
                                    return CompletableFuture.failedFuture(
                                        new UaException(
                                            StatusCodes.Bad_NotSupported,
                                            "http://opcfoundation.org/UA/:GetMonitoredItems"
                                                + " (declaration i=11489, owner i=2004) on "
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
                                                            "http://opcfoundation.org/UA/:GetMonitoredItems"
                                                                + " (declaration i=11489, owner"
                                                                + " i=2004) on "
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
                                                    "http://opcfoundation.org/UA/:GetMonitoredItems"
                                                        + " (declaration i=11489, owner i=2004) on "
                                                        + getNodeId()));
                                          }
                                          if (unique.size() != 1) {
                                            return CompletableFuture.failedFuture(
                                                new UaException(
                                                    StatusCodes.Bad_TooManyMatches,
                                                    "http://opcfoundation.org/UA/:GetMonitoredItems"
                                                        + " (declaration i=11489, owner i=2004) on "
                                                        + getNodeId()));
                                          }
                                          return client
                                              .getAddressSpace()
                                              .getNodeAsync(unique.iterator().next())
                                              .thenCompose(
                                                  node -> {
                                                    if (node == null
                                                        || node.getNodeClass()
                                                            != NodeClass.Method) {
                                                      return CompletableFuture.failedFuture(
                                                          new UaException(
                                                              StatusCodes.Bad_NodeClassInvalid,
                                                              "http://opcfoundation.org/UA/:GetMonitoredItems"
                                                                  + " (declaration i=11489, owner"
                                                                  + " i=2004) on "
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
            } else if (node != null && !(node instanceof UaMethodNode)) {
              result.completeExceptionally(
                  new UaException(
                      StatusCodes.Bad_TypeMismatch,
                      "http://opcfoundation.org/UA/:GetMonitoredItems (declaration i=11489, owner"
                          + " i=2004)"));
            } else {
              result.complete((UaMethodNode) node);
            }
          });
    } catch (Exception e) {
      result.completeExceptionally(e);
    }
    return result;
  }

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part5/9.1
   *
   * <p>Invokes <code>GetMonitoredItems</code> on this node's ObjectId using the effective Method
   * contract.
   *
   * <p>The Method node is required. Confirmed absence fails with Bad_NotFound; lookup and service
   * failures are preserved. The call does not create a node or retry an invocation.
   *
   * <p>Requires Good operation status, including Good subcodes. Uncertain and Bad statuses fail
   * with UaException before any output conversion failure is reported. Application status outputs
   * remain separate.
   *
   * <p>Interrupted waiting cancels owned observation and pending discovery, restores the interrupt
   * flag and fails with Bad_UnexpectedError. It does not cancel server execution.
   *
   * @param subscriptionId ; the supplied payload may be null.
   * @return the output value or object, or its future; a single value may be null.
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  @Override
  public ServerTypeGetMonitoredItemsOutputs callGetMonitoredItems(@Nullable UInteger subscriptionId)
      throws UaException {
    return callGetMonitoredItemsDetailed(subscriptionId).requireGood();
  }

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part5/9.1
   *
   * <p>Invokes <code>GetMonitoredItems</code> on this node's ObjectId using the effective Method
   * contract.
   *
   * <p>The Method node is required. Confirmed absence fails with Bad_NotFound; lookup and service
   * failures are preserved. The call does not create a node or retry an invocation.
   *
   * <p>Requires Good operation status, including Good subcodes. Uncertain and Bad statuses fail
   * with UaException before any output conversion failure is reported. Application status outputs
   * remain separate.
   *
   * <p>Returns a non-null future. Lookup, input metadata, transport and response envelope failures
   * complete it exceptionally. Cancellation stops observation and dependent work that has not
   * started; it does not cancel server execution.
   *
   * @param subscriptionId ; the supplied payload may be null.
   * @return the output value or object, or its future; a single value may be null.
   */
  @NullMarked
  @Override
  public CompletableFuture<? extends ServerTypeGetMonitoredItemsOutputs> callGetMonitoredItemsAsync(
      @Nullable UInteger subscriptionId) {
    CompletableFuture<ServerTypeGetMonitoredItemsOutputs> result = new CompletableFuture<>();
    var call = callGetMonitoredItemsDetailedAsync(subscriptionId);
    result.whenComplete(
        (value, failure) -> {
          if (result.isCancelled()) {
            call.cancel(false);
          }
        });
    call.whenComplete(
        (value, failure) -> {
          if (failure != null) {
            result.completeExceptionally(failure);
          } else {
            try {
              result.complete(value.requireGood());
            } catch (UaException statusFailure) {
              result.completeExceptionally(statusFailure);
            }
          }
        });
    return result;
  }

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part5/9.1
   *
   * <p>Invokes <code>GetMonitoredItems</code> on this node's ObjectId using the effective Method
   * contract.
   *
   * <p>The Method node is required. Confirmed absence fails with Bad_NotFound; lookup and service
   * failures are preserved. The call does not create a node or retry an invocation.
   *
   * <p>Retains operation status, request and response diagnostics, StringTable and raw outputs.
   * Good and Uncertain outputs are decoded; output metadata and conversion failures remain
   * available separately. Bad results preserve received wire outputs.
   *
   * <p>Interrupted waiting cancels owned observation and pending discovery, restores the interrupt
   * flag and fails with Bad_UnexpectedError. It does not cancel server execution.
   *
   * @param subscriptionId ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  @Override
  public MethodCallResult<? extends ServerTypeGetMonitoredItemsOutputs>
      callGetMonitoredItemsDetailed(@Nullable UInteger subscriptionId) throws UaException {
    return callGetMonitoredItemsDetailed(MethodCallOptions.NONE, subscriptionId);
  }

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part5/9.1
   *
   * <p>Invokes <code>GetMonitoredItems</code> on this node's ObjectId using the effective Method
   * contract.
   *
   * <p>The Method node is required. Confirmed absence fails with Bad_NotFound; lookup and service
   * failures are preserved. The call does not create a node or retry an invocation.
   *
   * <p>Retains operation status, request and response diagnostics, StringTable and raw outputs.
   * Good and Uncertain outputs are decoded; output metadata and conversion failures remain
   * available separately. Bad results preserve received wire outputs.
   *
   * <p>Interrupted waiting cancels owned observation and pending discovery, restores the interrupt
   * flag and fails with Bad_UnexpectedError. It does not cancel server execution.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @param subscriptionId ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  @Override
  public MethodCallResult<? extends ServerTypeGetMonitoredItemsOutputs>
      callGetMonitoredItemsDetailed(MethodCallOptions options, @Nullable UInteger subscriptionId)
          throws UaException {
    Objects.requireNonNull(options, "options");
    var awaitedMethod = callGetMonitoredItemsDetailedAsync(options, subscriptionId);
    try {
      return awaitedMethod.get();
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
      awaitedMethod.cancel(false);
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part5/9.1
   *
   * <p>Invokes <code>GetMonitoredItems</code> on this node's ObjectId using the effective Method
   * contract.
   *
   * <p>The Method node is required. Confirmed absence fails with Bad_NotFound; lookup and service
   * failures are preserved. The call does not create a node or retry an invocation.
   *
   * <p>Retains operation status, request and response diagnostics, StringTable and raw outputs.
   * Good and Uncertain outputs are decoded; output metadata and conversion failures remain
   * available separately. Bad results preserve received wire outputs.
   *
   * <p>Returns a non-null future. Lookup, input metadata, transport and response envelope failures
   * complete it exceptionally. Cancellation stops observation and dependent work that has not
   * started; it does not cancel server execution.
   *
   * @param subscriptionId ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  @Override
  public CompletableFuture<? extends MethodCallResult<? extends ServerTypeGetMonitoredItemsOutputs>>
      callGetMonitoredItemsDetailedAsync(@Nullable UInteger subscriptionId) {
    return callGetMonitoredItemsDetailedAsync(MethodCallOptions.NONE, subscriptionId);
  }

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part5/9.1
   *
   * <p>Invokes <code>GetMonitoredItems</code> on this node's ObjectId using the effective Method
   * contract.
   *
   * <p>The Method node is required. Confirmed absence fails with Bad_NotFound; lookup and service
   * failures are preserved. The call does not create a node or retry an invocation.
   *
   * <p>Retains operation status, request and response diagnostics, StringTable and raw outputs.
   * Good and Uncertain outputs are decoded; output metadata and conversion failures remain
   * available separately. Bad results preserve received wire outputs.
   *
   * <p>Returns a non-null future. Lookup, input metadata, transport and response envelope failures
   * complete it exceptionally. Cancellation stops observation and dependent work that has not
   * started; it does not cancel server execution.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @param subscriptionId ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  @Override
  public CompletableFuture<? extends MethodCallResult<? extends ServerTypeGetMonitoredItemsOutputs>>
      callGetMonitoredItemsDetailedAsync(
          MethodCallOptions options, @Nullable UInteger subscriptionId) {
    CompletableFuture<MethodCallResult<ServerTypeGetMonitoredItemsOutputs>> result =
        new CompletableFuture<>();
    try {
      Objects.requireNonNull(options, "options");
      List<@Nullable Object> rawInputs = new ArrayList<>();
      rawInputs.add(subscriptionId);
      var lookup = getGetMonitoredItemsMethodNodeAsync();
      result.whenComplete(
          (value, failure) -> {
            if (result.isCancelled()) {
              lookup.cancel(false);
            }
          });
      CompletableFuture<MethodCallResult<ServerTypeGetMonitoredItemsOutputs>> pipeline =
          lookup.thenCompose(
              methodNode -> {
                if (result.isCancelled()) {
                  return CompletableFuture.failedFuture(new CancellationException());
                }
                if (methodNode == null) {
                  return CompletableFuture.failedFuture(
                      new UaException(
                          StatusCodes.Bad_NotFound,
                          "Method node is required for invocation: GetMonitoredItems"));
                }
                var inputMetadata =
                    ClientDataTypes.read(
                        this.client,
                        List.<ExpandedNodeId>of(ExpandedNodeId.parse("i=7"))
                            .subList(0, rawInputs.size()),
                        rawInputs.toArray());
                result.whenComplete(
                    (cancelledValue, cancelledFailure) -> {
                      if (result.isCancelled()) {
                        inputMetadata.cancel(false);
                      }
                    });
                return inputMetadata
                    .handle(
                        (inputDataTypeTree, inputMetadataFailure) -> {
                          if (inputMetadataFailure != null) {
                            var checkedMetadataFailureinputMetadataFailure =
                                UaException.extract(inputMetadataFailure);
                            if (checkedMetadataFailureinputMetadataFailure.isPresent()) {
                              throw new CompletionException(
                                  checkedMetadataFailureinputMetadataFailure.orElseThrow());
                            }
                            Throwable metadataCauseinputMetadataFailure = inputMetadataFailure;
                            while (metadataCauseinputMetadataFailure != null) {
                              if (metadataCauseinputMetadataFailure
                                  instanceof
                                  UaSerializationException metadataCodecinputMetadataFailure) {
                                throw new CompletionException(
                                    new UaException(
                                        metadataCodecinputMetadataFailure.getStatusCode().getValue()
                                                == StatusCodes.Bad_OutOfRange
                                            ? StatusCodes.Bad_OutOfRange
                                            : StatusCodes.Bad_TypeMismatch,
                                        metadataCodecinputMetadataFailure));
                              }
                              metadataCauseinputMetadataFailure =
                                  metadataCauseinputMetadataFailure.getCause();
                            }
                            throw new CompletionException(
                                UaException.extract(inputMetadataFailure)
                                    .orElseGet(() -> new UaException(inputMetadataFailure)));
                          }
                          return inputDataTypeTree;
                        })
                    .thenCompose(
                        dataTypeTree -> {
                          if (result.isCancelled()) {
                            return CompletableFuture.failedFuture(new CancellationException());
                          }
                          try {
                            List<Variant> inputArguments = new ArrayList<>();
                            if (rawInputs.size() > 0) {
                              Variant encoded0;
                              {
                                @Nullable UInteger convertedValue;
                                {
                                  Object methodValue = rawInputs.get(0);
                                  try {
                                    if (methodValue instanceof Matrix
                                        && ((Matrix) methodValue).isNull()) {
                                      methodValue = null;
                                    }
                                    NamespaceTable namespaceTable = this.client.getNamespaceTable();
                                    DataTypeTree dataTypeTree_ = dataTypeTree;
                                    NodeId argumentDataTypeId =
                                        ExpandedNodeId.parse("i=7")
                                            .toNodeId(namespaceTable)
                                            .orElseThrow(
                                                () ->
                                                    new UaException(
                                                        StatusCodes.Bad_NodeIdInvalid,
                                                        "Method argument DataType namespace is"
                                                            + " unavailable"));
                                    if (!OpcUaDataType.isBuiltin(argumentDataTypeId)
                                        && dataTypeTree_.getDataType(argumentDataTypeId) == null) {
                                      throw new UaException(
                                          StatusCodes.Bad_TypeMismatch,
                                          "Method argument SubscriptionId (effective property"
                                              + " i=11490, DataType i=7) is unavailable in the"
                                              + " effective type tree; resolved DataType: "
                                              + argumentDataTypeId);
                                    }
                                    Object numericElements =
                                        methodValue instanceof Matrix
                                            ? ((Matrix) methodValue).getElements()
                                            : methodValue;
                                    if (numericElements != null
                                        && numericElements.getClass().isArray()
                                        && (numericElements.getClass().getComponentType()
                                                == Number.class
                                            || numericElements.getClass().getComponentType()
                                                == UNumber.class)
                                        && (argumentDataTypeId.equals(NodeIds.Number)
                                            || dataTypeTree_.isSubtypeOf(
                                                argumentDataTypeId, NodeIds.Number))) {
                                      Class<?> numericElementType = null;
                                      for (int numericIndex = 0;
                                          numericIndex < Array.getLength(numericElements);
                                          numericIndex++) {
                                        Object numericElement =
                                            Array.get(numericElements, numericIndex);
                                        if (numericElement != null) {
                                          if (numericElementType != null
                                              && numericElementType != numericElement.getClass()) {
                                            throw new UaException(
                                                StatusCodes.Bad_TypeMismatch,
                                                "An abstract numeric array requires one homogeneous"
                                                    + " wire element type");
                                          }
                                          numericElementType = numericElement.getClass();
                                        }
                                      }
                                      if (numericElementType == null) {
                                        numericElementType =
                                            dataTypeTree_.getBackingClass(argumentDataTypeId);
                                      }
                                      if (numericElementType == Number.class
                                          || numericElementType == UNumber.class) {
                                        throw new UaException(
                                            StatusCodes.Bad_TypeMismatch,
                                            "An empty or all-null abstract numeric array requires a"
                                                + " concretely typed array");
                                      }
                                      Object numericArray =
                                          Array.newInstance(
                                              numericElementType, Array.getLength(numericElements));
                                      for (int numericIndex = 0;
                                          numericIndex < Array.getLength(numericElements);
                                          numericIndex++) {
                                        Array.set(
                                            numericArray,
                                            numericIndex,
                                            Array.get(numericElements, numericIndex));
                                      }
                                      if (methodValue instanceof Matrix) {
                                        methodValue =
                                            new Matrix(
                                                numericArray,
                                                ((Matrix) methodValue).getDimensions().clone(),
                                                ((Matrix) methodValue)
                                                    .getDataType()
                                                    .orElseThrow(
                                                        () ->
                                                            new UaException(
                                                                StatusCodes.Bad_TypeMismatch,
                                                                "A numeric Matrix requires an"
                                                                    + " explicit wire DataType")),
                                                ((Matrix) methodValue)
                                                    .getDataTypeId()
                                                    .orElse(null));
                                      } else {
                                        methodValue = numericArray;
                                      }
                                    }
                                    if (methodValue != null) {
                                      Object shapeElements =
                                          methodValue instanceof Matrix
                                              ? ((Matrix) methodValue).getElements()
                                              : methodValue;
                                      int valueRank =
                                          methodValue instanceof Matrix
                                              ? ((Matrix) methodValue).getValueRank()
                                              : ArrayUtil.getValueRank(methodValue);
                                      boolean emptyArray =
                                          methodValue.getClass().isArray()
                                              && ArrayUtil.getValueRank(methodValue) == 1
                                              && Array.getLength(methodValue) == 0;
                                      if (!(valueRank == -1)) {
                                        throw new UaException(
                                            StatusCodes.Bad_TypeMismatch,
                                            "Method argument ValueRank mismatch");
                                      }
                                      if (methodValue instanceof Matrix) {
                                        int[] dimensions = ((Matrix) methodValue).getDimensions();
                                        if (dimensions.length < 2
                                            || !shapeElements.getClass().isArray()
                                            || ArrayUtil.getValueRank(shapeElements) != 1) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "Malformed Method Matrix representation");
                                        }
                                        long elementCount = 1;
                                        for (int dimension : dimensions) {
                                          if (dimension < 0 || elementCount > Integer.MAX_VALUE) {
                                            throw new UaException(
                                                StatusCodes.Bad_TypeMismatch,
                                                "Malformed Method Matrix dimensions");
                                          }
                                          elementCount *= dimension;
                                        }
                                        if (elementCount != Array.getLength(shapeElements)) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "Method Matrix dimensions do not match its elements");
                                        }
                                        if (!(((Matrix) methodValue)
                                            .getDataType()
                                            .equals(Variant.of(shapeElements).getDataType()))) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "Method Matrix DataType does not match its elements");
                                        }
                                      }
                                      Variant.of(shapeElements);
                                    }
                                    if (methodValue != null) {
                                      Object typedElements =
                                          methodValue instanceof Matrix
                                              ? ((Matrix) methodValue).getElements()
                                              : methodValue;
                                      if (NodeIds.Structure.equals(argumentDataTypeId)
                                          || dataTypeTree_.isStructType(argumentDataTypeId)) {
                                        var declaredType =
                                            dataTypeTree_.getType(argumentDataTypeId);
                                        if (typedElements.getClass().isArray()) {
                                          var structureCodec =
                                              this.client
                                                  .getStaticEncodingContext()
                                                  .getDataTypeManager()
                                                  .getCodec(argumentDataTypeId);
                                          Class<?> structureClass =
                                              structureCodec == null
                                                  ? UaStructuredType.class
                                                  : structureCodec.getType();
                                          Object decodedStructures =
                                              Array.newInstance(
                                                  structureClass, Array.getLength(typedElements));
                                          for (int structureIndex = 0;
                                              structureIndex < Array.getLength(typedElements);
                                              structureIndex++) {
                                            Object structure =
                                                Array.get(typedElements, structureIndex);
                                            if (structure instanceof ExtensionObject) {
                                              structure =
                                                  ((ExtensionObject) structure).isNull()
                                                      ? null
                                                      : ((ExtensionObject) structure)
                                                          .decode(
                                                              this.client
                                                                  .getStaticEncodingContext());
                                            }
                                            if (structure != null) {
                                              if (!(structure instanceof UaStructuredType)) {
                                                throw new UaException(
                                                    StatusCodes.Bad_TypeMismatch,
                                                    "Method argument requires a Structure value");
                                              }
                                              if (NodeIds.Structure.equals(argumentDataTypeId)
                                                  || declaredType != null
                                                      && declaredType.isAbstract()) {
                                                if (!dataTypeTree_.isSubtypeOf(
                                                    ((UaStructuredType) structure)
                                                        .getTypeId()
                                                        .toNodeId(namespaceTable)
                                                        .orElse(NodeId.NULL_VALUE),
                                                    argumentDataTypeId)) {
                                                  throw new UaException(
                                                      StatusCodes.Bad_TypeMismatch,
                                                      "Method Structure is not a subtype of the"
                                                          + " effective DataType");
                                                }
                                              } else {
                                                if (!argumentDataTypeId.equals(
                                                    ((UaStructuredType) structure)
                                                        .getTypeId()
                                                        .toNodeId(namespaceTable)
                                                        .orElse(NodeId.NULL_VALUE))) {
                                                  throw new UaException(
                                                      StatusCodes.Bad_TypeMismatch,
                                                      "Method Structure does not match the"
                                                          + " effective DataType");
                                                }
                                              }
                                            }
                                            Array.set(decodedStructures, structureIndex, structure);
                                          }
                                          methodValue =
                                              methodValue instanceof Matrix
                                                  ? new Matrix(
                                                      decodedStructures,
                                                      ((Matrix) methodValue)
                                                          .getDimensions()
                                                          .clone())
                                                  : decodedStructures;
                                        } else {
                                          if (typedElements instanceof ExtensionObject) {
                                            typedElements =
                                                ((ExtensionObject) typedElements).isNull()
                                                    ? null
                                                    : ((ExtensionObject) typedElements)
                                                        .decode(
                                                            this.client.getStaticEncodingContext());
                                          }
                                          if (typedElements != null) {
                                            if (!(typedElements instanceof UaStructuredType)) {
                                              throw new UaException(
                                                  StatusCodes.Bad_TypeMismatch,
                                                  "Method argument requires a Structure value");
                                            }
                                            if (NodeIds.Structure.equals(argumentDataTypeId)
                                                || declaredType != null
                                                    && declaredType.isAbstract()) {
                                              if (!dataTypeTree_.isSubtypeOf(
                                                  ((UaStructuredType) typedElements)
                                                      .getTypeId()
                                                      .toNodeId(namespaceTable)
                                                      .orElse(NodeId.NULL_VALUE),
                                                  argumentDataTypeId)) {
                                                throw new UaException(
                                                    StatusCodes.Bad_TypeMismatch,
                                                    "Method Structure is not a subtype of the"
                                                        + " effective DataType");
                                              }
                                            } else {
                                              if (!argumentDataTypeId.equals(
                                                  ((UaStructuredType) typedElements)
                                                      .getTypeId()
                                                      .toNodeId(namespaceTable)
                                                      .orElse(NodeId.NULL_VALUE))) {
                                                throw new UaException(
                                                    StatusCodes.Bad_TypeMismatch,
                                                    "Method Structure does not match the effective"
                                                        + " DataType");
                                              }
                                            }
                                          }
                                          methodValue = typedElements;
                                        }
                                      } else {
                                        Variant.of(typedElements);
                                        NodeId assignableDataTypeId =
                                            dataTypeTree_.getBackingClass(argumentDataTypeId)
                                                        == Number.class
                                                    && dataTypeTree_.isSubtypeOf(
                                                        argumentDataTypeId, NodeIds.Integer)
                                                ? NodeIds.Integer
                                                : argumentDataTypeId;
                                        if (dataTypeTree_.getBackingClass(argumentDataTypeId)
                                                != Variant.class
                                            && !dataTypeTree_.isAssignable(
                                                assignableDataTypeId,
                                                ArrayUtil.getBoxedType(typedElements))) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "Method argument DataType mismatch");
                                        }
                                      }
                                    }
                                    convertedValue = (UInteger) methodValue;
                                  } catch (UaSerializationException conversionFailure) {
                                    throw new UaException(
                                        conversionFailure.getStatusCode().getValue()
                                                == StatusCodes.Bad_OutOfRange
                                            ? StatusCodes.Bad_OutOfRange
                                            : StatusCodes.Bad_TypeMismatch,
                                        conversionFailure);
                                  } catch (ClassCastException
                                      | IllegalArgumentException conversionFailure) {
                                    throw new UaException(
                                        StatusCodes.Bad_TypeMismatch, conversionFailure);
                                  }
                                }
                                try {
                                  Object wireValue = convertedValue;
                                  Object wireElements =
                                      wireValue instanceof Matrix
                                          ? ((Matrix) wireValue).getElements()
                                          : wireValue;
                                  var numericWireValues = new ArrayDeque<Object[]>();
                                  var numericWirePath =
                                      Collections.newSetFromMap(
                                          new IdentityHashMap<Object, Boolean>());
                                  if (wireValue != null) {
                                    numericWireValues.push(new Object[] {wireValue, false});
                                  }
                                  while (!numericWireValues.isEmpty()) {
                                    Object[] numericWireFrame = numericWireValues.pop();
                                    Object numericWireValue = numericWireFrame[0];
                                    if ((Boolean) numericWireFrame[1]) {
                                      numericWirePath.remove(numericWireValue);
                                      continue;
                                    }
                                    while (numericWireValue instanceof Variant
                                        || numericWireValue instanceof DataValue) {
                                      if (numericWireValue instanceof DataValue) {
                                        if (((DataValue) numericWireValue).getValue() == null) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "A DataValue requires a value wrapper; use"
                                                  + " Variant.NULL_VALUE for null");
                                        }
                                        if (((DataValue) numericWireValue).getStatusCode()
                                            == null) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "A DataValue requires a StatusCode; use"
                                                  + " StatusCode.GOOD for Good");
                                        }
                                        numericWireValue =
                                            ((DataValue) numericWireValue).getValue();
                                      } else {
                                        numericWireValue = ((Variant) numericWireValue).getValue();
                                      }
                                    }
                                    if (numericWireValue instanceof Matrix) {
                                      numericWireValue = ((Matrix) numericWireValue).getElements();
                                    }
                                    if (numericWireValue != null
                                        && numericWireValue.getClass().isArray()) {
                                      if (!numericWirePath.add(numericWireValue)) {
                                        throw new UaException(
                                            StatusCodes.Bad_TypeMismatch,
                                            "Cyclic Variant arrays cannot be encoded");
                                      }
                                      numericWireValues.push(new Object[] {numericWireValue, true});
                                      for (int numericWireIndex = 0;
                                          numericWireIndex < Array.getLength(numericWireValue);
                                          numericWireIndex++) {
                                        Object numericWireElement =
                                            Array.get(numericWireValue, numericWireIndex);
                                        if (numericWireElement == null
                                            && ArrayUtil.getBoxedType(numericWireValue)
                                                == Variant.class) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "A Variant wire array requires a wrapper for every"
                                                  + " element; use Variant.NULL_VALUE for null");
                                        }
                                        if (numericWireElement == null
                                            && (UaEnumeratedType.class.isAssignableFrom(
                                                    ArrayUtil.getBoxedType(numericWireValue))
                                                || OptionSetUInteger.class.isAssignableFrom(
                                                    ArrayUtil.getBoxedType(numericWireValue)))) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "An enum or OptionSet wire array cannot encode a null"
                                                  + " element");
                                        }
                                        if (numericWireElement == null
                                            && ArrayUtil.getBoxedType(numericWireValue)
                                                == Boolean.class) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "A Boolean wire array cannot retain a null element;"
                                                  + " Milo encodes it as false");
                                        }
                                        if (numericWireElement == null
                                            && ArrayUtil.getBoxedType(numericWireValue)
                                                == StatusCode.class) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "A StatusCode wire array cannot retain a null"
                                                  + " element; Milo encodes it as Good");
                                        }
                                        if (numericWireElement == null
                                            && Number.class.isAssignableFrom(
                                                ArrayUtil.getBoxedType(numericWireValue))) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "A numeric wire array cannot retain a null element;"
                                                  + " Milo encodes it as zero");
                                        }
                                        if (numericWireElement instanceof Variant
                                            || numericWireElement instanceof DataValue) {
                                          numericWireValues.push(
                                              new Object[] {numericWireElement, false});
                                        }
                                      }
                                    }
                                  }
                                  wireValue =
                                      ExtensionObject.encodeValue(
                                          this.client.getStaticEncodingContext(), wireValue);
                                  encoded0 = Variant.of(wireValue);
                                } catch (UaSerializationException encodingFailure) {
                                  throw new UaException(
                                      encodingFailure.getStatusCode().getValue()
                                              == StatusCodes.Bad_OutOfRange
                                          ? StatusCodes.Bad_OutOfRange
                                          : StatusCodes.Bad_TypeMismatch,
                                      encodingFailure);
                                } catch (ClassCastException
                                    | IllegalArgumentException encodingFailure) {
                                  throw new UaException(
                                      StatusCodes.Bad_TypeMismatch, encodingFailure);
                                }
                              }
                              inputArguments.add(encoded0);
                            }
                            CallMethodRequest request =
                                new CallMethodRequest(
                                    getNodeId(),
                                    methodNode.getNodeId(),
                                    inputArguments.toArray(new Variant[0]));
                            return this.client
                                .getSessionAsync()
                                .thenCompose(
                                    session -> {
                                      if (result.isCancelled()) {
                                        return CompletableFuture.failedFuture(
                                            new CancellationException());
                                      }
                                      RequestHeader originalHeader =
                                          this.client.newRequestHeader(
                                              session.getAuthenticationToken());
                                      RequestHeader requestHeader =
                                          new RequestHeader(
                                              originalHeader.getAuthenticationToken(),
                                              originalHeader.getTimestamp(),
                                              originalHeader.getRequestHandle(),
                                              options.returnDiagnostics(),
                                              originalHeader.getAuditEntryId(),
                                              originalHeader.getTimeoutHint(),
                                              originalHeader.getAdditionalHeader());
                                      var call =
                                          this.client.sendRequestAsync(
                                              new CallRequest(
                                                  requestHeader,
                                                  new CallMethodRequest[] {request}));
                                      result.whenComplete(
                                          (value, failure) -> {
                                            if (result.isCancelled()) {
                                              call.cancel(false);
                                            }
                                          });
                                      return call.thenCompose(
                                          message -> {
                                            if (result.isCancelled()) {
                                              throw new CancellationException();
                                            }
                                            try {
                                              if (!(message instanceof CallResponse response)
                                                  || response.getResponseHeader() == null
                                                  || response.getResponseHeader().getServiceResult()
                                                      == null) {
                                                throw new UaException(
                                                    StatusCodes.Bad_UnexpectedError,
                                                    "Malformed Call response header");
                                              }
                                              if (!response
                                                  .getResponseHeader()
                                                  .getServiceResult()
                                                  .isGood()) {
                                                throw new UaException(
                                                    response
                                                        .getResponseHeader()
                                                        .getServiceResult());
                                              }
                                              if (response.getResults() == null
                                                  || response.getResults().length != 1
                                                  || response.getResults()[0] == null
                                                  || response.getResults()[0].getStatusCode()
                                                      == null) {
                                                throw new UaException(
                                                    StatusCodes.Bad_UnexpectedError,
                                                    "Expected exactly one Call operation result");
                                              }
                                              CompletableFuture<DataTypeTree> outputMetadata =
                                                  response.getResults()[0].getStatusCode().isBad()
                                                      ? CompletableFuture.completedFuture(
                                                          dataTypeTree)
                                                      : ClientDataTypes.read(
                                                          this.client,
                                                          List.<ExpandedNodeId>of(
                                                              ExpandedNodeId.parse("i=7"),
                                                              ExpandedNodeId.parse("i=7")),
                                                          (Object)
                                                              response.getResults()[0]
                                                                  .getOutputArguments());
                                              result.whenComplete(
                                                  (cancelledValue, cancelledFailure) -> {
                                                    if (result.isCancelled()) {
                                                      outputMetadata.cancel(false);
                                                    }
                                                  });
                                              return outputMetadata.handle(
                                                  (outputDataTypeTree, metadataFailure) -> {
                                                    if (result.isCancelled()) {
                                                      throw new CancellationException();
                                                    }
                                                    try {
                                                      return MethodCallResult.decode(
                                                          request,
                                                          requestHeader,
                                                          response,
                                                          0,
                                                          outputArguments -> {
                                                            if (metadataFailure != null) {
                                                              var
                                                                  checkedMetadataFailuremetadataFailure =
                                                                      UaException.extract(
                                                                          metadataFailure);
                                                              if (checkedMetadataFailuremetadataFailure
                                                                  .isPresent()) {
                                                                throw checkedMetadataFailuremetadataFailure
                                                                    .orElseThrow();
                                                              }
                                                              Throwable
                                                                  metadataCausemetadataFailure =
                                                                      metadataFailure;
                                                              while (metadataCausemetadataFailure
                                                                  != null) {
                                                                if (metadataCausemetadataFailure
                                                                    instanceof
                                                                    UaSerializationException
                                                                        metadataCodecmetadataFailure) {
                                                                  throw new UaException(
                                                                      metadataCodecmetadataFailure
                                                                                  .getStatusCode()
                                                                                  .getValue()
                                                                              == StatusCodes
                                                                                  .Bad_OutOfRange
                                                                          ? StatusCodes
                                                                              .Bad_OutOfRange
                                                                          : StatusCodes
                                                                              .Bad_TypeMismatch,
                                                                      metadataCodecmetadataFailure);
                                                                }
                                                                metadataCausemetadataFailure =
                                                                    metadataCausemetadataFailure
                                                                        .getCause();
                                                              }
                                                              throw UaException.extract(
                                                                      metadataFailure)
                                                                  .orElseGet(
                                                                      () ->
                                                                          new UaException(
                                                                              metadataFailure));
                                                            }
                                                            if ((outputArguments == null
                                                                    ? 0
                                                                    : outputArguments.length)
                                                                != 2) {
                                                              throw new UaException(
                                                                  StatusCodes.Bad_TypeMismatch,
                                                                  "Unexpected Method output count");
                                                            }
                                                            if (outputArguments[0] == null) {
                                                              throw new UaException(
                                                                  StatusCodes.Bad_TypeMismatch,
                                                                  "Null Method output Variant");
                                                            }
                                                            @Nullable UInteger @Nullable []
                                                                decoded0;
                                                            {
                                                              Object methodValue =
                                                                  outputArguments[0].getValue();
                                                              try {
                                                                if (methodValue instanceof Matrix
                                                                    && ((Matrix) methodValue)
                                                                        .isNull()) {
                                                                  methodValue = null;
                                                                }
                                                                NamespaceTable namespaceTable =
                                                                    this.client.getNamespaceTable();
                                                                DataTypeTree dataTypeTree_ =
                                                                    outputDataTypeTree;
                                                                NodeId argumentDataTypeId =
                                                                    ExpandedNodeId.parse("i=7")
                                                                        .toNodeId(namespaceTable)
                                                                        .orElseThrow(
                                                                            () ->
                                                                                new UaException(
                                                                                    StatusCodes
                                                                                        .Bad_NodeIdInvalid,
                                                                                    "Method"
                                                                                        + " argument"
                                                                                        + " DataType"
                                                                                        + " namespace"
                                                                                        + " is unavailable"));
                                                                if (!OpcUaDataType.isBuiltin(
                                                                        argumentDataTypeId)
                                                                    && dataTypeTree_.getDataType(
                                                                            argumentDataTypeId)
                                                                        == null) {
                                                                  throw new UaException(
                                                                      StatusCodes.Bad_TypeMismatch,
                                                                      "Method argument"
                                                                          + " ServerHandles"
                                                                          + " (effective property"
                                                                          + " i=11491, DataType"
                                                                          + " i=7) is unavailable"
                                                                          + " in the effective type"
                                                                          + " tree; resolved"
                                                                          + " DataType: "
                                                                          + argumentDataTypeId);
                                                                }
                                                                Object numericElements =
                                                                    methodValue instanceof Matrix
                                                                        ? ((Matrix) methodValue)
                                                                            .getElements()
                                                                        : methodValue;
                                                                if (numericElements != null
                                                                    && numericElements
                                                                        .getClass()
                                                                        .isArray()
                                                                    && (numericElements
                                                                                .getClass()
                                                                                .getComponentType()
                                                                            == Number.class
                                                                        || numericElements
                                                                                .getClass()
                                                                                .getComponentType()
                                                                            == UNumber.class)
                                                                    && (argumentDataTypeId.equals(
                                                                            NodeIds.Number)
                                                                        || dataTypeTree_
                                                                            .isSubtypeOf(
                                                                                argumentDataTypeId,
                                                                                NodeIds.Number))) {
                                                                  Class<?> numericElementType =
                                                                      null;
                                                                  for (int numericIndex = 0;
                                                                      numericIndex
                                                                          < Array.getLength(
                                                                              numericElements);
                                                                      numericIndex++) {
                                                                    Object numericElement =
                                                                        Array.get(
                                                                            numericElements,
                                                                            numericIndex);
                                                                    if (numericElement != null) {
                                                                      if (numericElementType != null
                                                                          && numericElementType
                                                                              != numericElement
                                                                                  .getClass()) {
                                                                        throw new UaException(
                                                                            StatusCodes
                                                                                .Bad_TypeMismatch,
                                                                            "An abstract numeric"
                                                                                + " array requires"
                                                                                + " one homogeneous"
                                                                                + " wire element"
                                                                                + " type");
                                                                      }
                                                                      numericElementType =
                                                                          numericElement.getClass();
                                                                    }
                                                                  }
                                                                  if (numericElementType == null) {
                                                                    numericElementType =
                                                                        dataTypeTree_
                                                                            .getBackingClass(
                                                                                argumentDataTypeId);
                                                                  }
                                                                  if (numericElementType
                                                                          == Number.class
                                                                      || numericElementType
                                                                          == UNumber.class) {
                                                                    throw new UaException(
                                                                        StatusCodes
                                                                            .Bad_TypeMismatch,
                                                                        "An empty or all-null"
                                                                            + " abstract numeric"
                                                                            + " array requires a"
                                                                            + " concretely typed"
                                                                            + " array");
                                                                  }
                                                                  Object numericArray =
                                                                      Array.newInstance(
                                                                          numericElementType,
                                                                          Array.getLength(
                                                                              numericElements));
                                                                  for (int numericIndex = 0;
                                                                      numericIndex
                                                                          < Array.getLength(
                                                                              numericElements);
                                                                      numericIndex++) {
                                                                    Array.set(
                                                                        numericArray,
                                                                        numericIndex,
                                                                        Array.get(
                                                                            numericElements,
                                                                            numericIndex));
                                                                  }
                                                                  if (methodValue
                                                                      instanceof Matrix) {
                                                                    methodValue =
                                                                        new Matrix(
                                                                            numericArray,
                                                                            ((Matrix) methodValue)
                                                                                .getDimensions()
                                                                                .clone(),
                                                                            ((Matrix) methodValue)
                                                                                .getDataType()
                                                                                .orElseThrow(
                                                                                    () ->
                                                                                        new UaException(
                                                                                            StatusCodes
                                                                                                .Bad_TypeMismatch,
                                                                                            "A numeric"
                                                                                                + " Matrix"
                                                                                                + " requires"
                                                                                                + " an explicit"
                                                                                                + " wire"
                                                                                                + " DataType")),
                                                                            ((Matrix) methodValue)
                                                                                .getDataTypeId()
                                                                                .orElse(null));
                                                                  } else {
                                                                    methodValue = numericArray;
                                                                  }
                                                                }
                                                                if (methodValue != null) {
                                                                  Object shapeElements =
                                                                      methodValue instanceof Matrix
                                                                          ? ((Matrix) methodValue)
                                                                              .getElements()
                                                                          : methodValue;
                                                                  int valueRank =
                                                                      methodValue instanceof Matrix
                                                                          ? ((Matrix) methodValue)
                                                                              .getValueRank()
                                                                          : ArrayUtil.getValueRank(
                                                                              methodValue);
                                                                  boolean emptyArray =
                                                                      methodValue
                                                                              .getClass()
                                                                              .isArray()
                                                                          && ArrayUtil.getValueRank(
                                                                                  methodValue)
                                                                              == 1
                                                                          && Array.getLength(
                                                                                  methodValue)
                                                                              == 0;
                                                                  if (!(valueRank == 1
                                                                      || emptyArray)) {
                                                                    throw new UaException(
                                                                        StatusCodes
                                                                            .Bad_TypeMismatch,
                                                                        "Method argument ValueRank"
                                                                            + " mismatch");
                                                                  }
                                                                  if (methodValue
                                                                      instanceof Matrix) {
                                                                    int[] dimensions =
                                                                        ((Matrix) methodValue)
                                                                            .getDimensions();
                                                                    if (dimensions.length < 2
                                                                        || !shapeElements
                                                                            .getClass()
                                                                            .isArray()
                                                                        || ArrayUtil.getValueRank(
                                                                                shapeElements)
                                                                            != 1) {
                                                                      throw new UaException(
                                                                          StatusCodes
                                                                              .Bad_TypeMismatch,
                                                                          "Malformed Method Matrix"
                                                                              + " representation");
                                                                    }
                                                                    long elementCount = 1;
                                                                    for (int dimension :
                                                                        dimensions) {
                                                                      if (dimension < 0
                                                                          || elementCount
                                                                              > Integer.MAX_VALUE) {
                                                                        throw new UaException(
                                                                            StatusCodes
                                                                                .Bad_TypeMismatch,
                                                                            "Malformed Method"
                                                                                + " Matrix"
                                                                                + " dimensions");
                                                                      }
                                                                      elementCount *= dimension;
                                                                    }
                                                                    if (elementCount
                                                                        != Array.getLength(
                                                                            shapeElements)) {
                                                                      throw new UaException(
                                                                          StatusCodes
                                                                              .Bad_TypeMismatch,
                                                                          "Method Matrix dimensions"
                                                                              + " do not match its"
                                                                              + " elements");
                                                                    }
                                                                    if (!(((Matrix) methodValue)
                                                                        .getDataType()
                                                                        .equals(
                                                                            Variant.of(
                                                                                    shapeElements)
                                                                                .getDataType()))) {
                                                                      throw new UaException(
                                                                          StatusCodes
                                                                              .Bad_TypeMismatch,
                                                                          "Method Matrix DataType"
                                                                              + " does not match"
                                                                              + " its elements");
                                                                    }
                                                                  }
                                                                  Variant.of(shapeElements);
                                                                  if (!emptyArray) {
                                                                    int[] dimensions =
                                                                        methodValue
                                                                                instanceof Matrix
                                                                            ? ((Matrix) methodValue)
                                                                                .getDimensions()
                                                                            : ArrayUtil
                                                                                .getDimensions(
                                                                                    methodValue);
                                                                    long[] maximumDimensions =
                                                                        new long[] {0L};
                                                                    if (dimensions.length
                                                                        != maximumDimensions
                                                                            .length) {
                                                                      throw new UaException(
                                                                          StatusCodes
                                                                              .Bad_TypeMismatch,
                                                                          "Method argument"
                                                                              + " ArrayDimensions"
                                                                              + " mismatch");
                                                                    }
                                                                    for (int dimensionIndex = 0;
                                                                        dimensionIndex
                                                                            < dimensions.length;
                                                                        dimensionIndex++) {
                                                                      if (maximumDimensions[
                                                                                  dimensionIndex]
                                                                              != 0
                                                                          && dimensions[
                                                                                  dimensionIndex]
                                                                              > maximumDimensions[
                                                                                  dimensionIndex]) {
                                                                        throw new UaException(
                                                                            StatusCodes
                                                                                .Bad_TypeMismatch,
                                                                            "Method argument"
                                                                                + " exceeds"
                                                                                + " ArrayDimensions"
                                                                                + " maximum");
                                                                      }
                                                                    }
                                                                  }
                                                                }
                                                                if (methodValue != null) {
                                                                  Object typedElements =
                                                                      methodValue instanceof Matrix
                                                                          ? ((Matrix) methodValue)
                                                                              .getElements()
                                                                          : methodValue;
                                                                  if (NodeIds.Structure.equals(
                                                                          argumentDataTypeId)
                                                                      || dataTypeTree_.isStructType(
                                                                          argumentDataTypeId)) {
                                                                    var declaredType =
                                                                        dataTypeTree_.getType(
                                                                            argumentDataTypeId);
                                                                    if (typedElements
                                                                        .getClass()
                                                                        .isArray()) {
                                                                      var structureCodec =
                                                                          this.client
                                                                              .getStaticEncodingContext()
                                                                              .getDataTypeManager()
                                                                              .getCodec(
                                                                                  argumentDataTypeId);
                                                                      Class<?> structureClass =
                                                                          structureCodec == null
                                                                              ? UaStructuredType
                                                                                  .class
                                                                              : structureCodec
                                                                                  .getType();
                                                                      Object decodedStructures =
                                                                          Array.newInstance(
                                                                              structureClass,
                                                                              Array.getLength(
                                                                                  typedElements));
                                                                      for (int structureIndex = 0;
                                                                          structureIndex
                                                                              < Array.getLength(
                                                                                  typedElements);
                                                                          structureIndex++) {
                                                                        Object structure =
                                                                            Array.get(
                                                                                typedElements,
                                                                                structureIndex);
                                                                        if (structure
                                                                            instanceof
                                                                            ExtensionObject) {
                                                                          structure =
                                                                              ((ExtensionObject)
                                                                                          structure)
                                                                                      .isNull()
                                                                                  ? null
                                                                                  : ((ExtensionObject)
                                                                                          structure)
                                                                                      .decode(
                                                                                          this
                                                                                              .client
                                                                                              .getStaticEncodingContext());
                                                                        }
                                                                        if (structure != null) {
                                                                          if (!(structure
                                                                              instanceof
                                                                              UaStructuredType)) {
                                                                            throw new UaException(
                                                                                StatusCodes
                                                                                    .Bad_TypeMismatch,
                                                                                "Method argument"
                                                                                    + " requires a"
                                                                                    + " Structure"
                                                                                    + " value");
                                                                          }
                                                                          if (NodeIds.Structure
                                                                                  .equals(
                                                                                      argumentDataTypeId)
                                                                              || declaredType
                                                                                      != null
                                                                                  && declaredType
                                                                                      .isAbstract()) {
                                                                            if (!dataTypeTree_
                                                                                .isSubtypeOf(
                                                                                    ((UaStructuredType)
                                                                                            structure)
                                                                                        .getTypeId()
                                                                                        .toNodeId(
                                                                                            namespaceTable)
                                                                                        .orElse(
                                                                                            NodeId
                                                                                                .NULL_VALUE),
                                                                                    argumentDataTypeId)) {
                                                                              throw new UaException(
                                                                                  StatusCodes
                                                                                      .Bad_TypeMismatch,
                                                                                  "Method Structure"
                                                                                      + " is not a"
                                                                                      + " subtype"
                                                                                      + " of the"
                                                                                      + " effective"
                                                                                      + " DataType");
                                                                            }
                                                                          } else {
                                                                            if (!argumentDataTypeId
                                                                                .equals(
                                                                                    ((UaStructuredType)
                                                                                            structure)
                                                                                        .getTypeId()
                                                                                        .toNodeId(
                                                                                            namespaceTable)
                                                                                        .orElse(
                                                                                            NodeId
                                                                                                .NULL_VALUE))) {
                                                                              throw new UaException(
                                                                                  StatusCodes
                                                                                      .Bad_TypeMismatch,
                                                                                  "Method Structure"
                                                                                      + " does not"
                                                                                      + " match the"
                                                                                      + " effective"
                                                                                      + " DataType");
                                                                            }
                                                                          }
                                                                        }
                                                                        Array.set(
                                                                            decodedStructures,
                                                                            structureIndex,
                                                                            structure);
                                                                      }
                                                                      methodValue =
                                                                          methodValue
                                                                                  instanceof Matrix
                                                                              ? new Matrix(
                                                                                  decodedStructures,
                                                                                  ((Matrix)
                                                                                          methodValue)
                                                                                      .getDimensions()
                                                                                      .clone())
                                                                              : decodedStructures;
                                                                    } else {
                                                                      if (typedElements
                                                                          instanceof
                                                                          ExtensionObject) {
                                                                        typedElements =
                                                                            ((ExtensionObject)
                                                                                        typedElements)
                                                                                    .isNull()
                                                                                ? null
                                                                                : ((ExtensionObject)
                                                                                        typedElements)
                                                                                    .decode(
                                                                                        this.client
                                                                                            .getStaticEncodingContext());
                                                                      }
                                                                      if (typedElements != null) {
                                                                        if (!(typedElements
                                                                            instanceof
                                                                            UaStructuredType)) {
                                                                          throw new UaException(
                                                                              StatusCodes
                                                                                  .Bad_TypeMismatch,
                                                                              "Method argument"
                                                                                  + " requires a"
                                                                                  + " Structure"
                                                                                  + " value");
                                                                        }
                                                                        if (NodeIds.Structure
                                                                                .equals(
                                                                                    argumentDataTypeId)
                                                                            || declaredType != null
                                                                                && declaredType
                                                                                    .isAbstract()) {
                                                                          if (!dataTypeTree_
                                                                              .isSubtypeOf(
                                                                                  ((UaStructuredType)
                                                                                          typedElements)
                                                                                      .getTypeId()
                                                                                      .toNodeId(
                                                                                          namespaceTable)
                                                                                      .orElse(
                                                                                          NodeId
                                                                                              .NULL_VALUE),
                                                                                  argumentDataTypeId)) {
                                                                            throw new UaException(
                                                                                StatusCodes
                                                                                    .Bad_TypeMismatch,
                                                                                "Method Structure"
                                                                                    + " is not a"
                                                                                    + " subtype of"
                                                                                    + " the effective"
                                                                                    + " DataType");
                                                                          }
                                                                        } else {
                                                                          if (!argumentDataTypeId
                                                                              .equals(
                                                                                  ((UaStructuredType)
                                                                                          typedElements)
                                                                                      .getTypeId()
                                                                                      .toNodeId(
                                                                                          namespaceTable)
                                                                                      .orElse(
                                                                                          NodeId
                                                                                              .NULL_VALUE))) {
                                                                            throw new UaException(
                                                                                StatusCodes
                                                                                    .Bad_TypeMismatch,
                                                                                "Method Structure"
                                                                                    + " does not"
                                                                                    + " match the"
                                                                                    + " effective"
                                                                                    + " DataType");
                                                                          }
                                                                        }
                                                                      }
                                                                      methodValue = typedElements;
                                                                    }
                                                                  } else {
                                                                    Variant.of(typedElements);
                                                                    NodeId assignableDataTypeId =
                                                                        dataTypeTree_
                                                                                        .getBackingClass(
                                                                                            argumentDataTypeId)
                                                                                    == Number.class
                                                                                && dataTypeTree_
                                                                                    .isSubtypeOf(
                                                                                        argumentDataTypeId,
                                                                                        NodeIds
                                                                                            .Integer)
                                                                            ? NodeIds.Integer
                                                                            : argumentDataTypeId;
                                                                    if (dataTypeTree_
                                                                                .getBackingClass(
                                                                                    argumentDataTypeId)
                                                                            != Variant.class
                                                                        && !dataTypeTree_
                                                                            .isAssignable(
                                                                                assignableDataTypeId,
                                                                                ArrayUtil
                                                                                    .getBoxedType(
                                                                                        typedElements))) {
                                                                      throw new UaException(
                                                                          StatusCodes
                                                                              .Bad_TypeMismatch,
                                                                          "Method argument DataType"
                                                                              + " mismatch");
                                                                    }
                                                                  }
                                                                }
                                                                if (methodValue == null) {
                                                                  decoded0 = null;
                                                                } else {
                                                                  decoded0 =
                                                                      new UInteger
                                                                          [Array.getLength(
                                                                              methodValue)];
                                                                  for (int valueIndex = 0;
                                                                      valueIndex < decoded0.length;
                                                                      valueIndex++) {
                                                                    Object valueElement =
                                                                        Array.get(
                                                                            methodValue,
                                                                            valueIndex);
                                                                    decoded0[valueIndex] =
                                                                        (UInteger) valueElement;
                                                                  }
                                                                }
                                                              } catch (
                                                                  UaSerializationException
                                                                      conversionFailure) {
                                                                throw new UaException(
                                                                    conversionFailure
                                                                                .getStatusCode()
                                                                                .getValue()
                                                                            == StatusCodes
                                                                                .Bad_OutOfRange
                                                                        ? StatusCodes.Bad_OutOfRange
                                                                        : StatusCodes
                                                                            .Bad_TypeMismatch,
                                                                    conversionFailure);
                                                              } catch (ClassCastException
                                                                  | IllegalArgumentException
                                                                      conversionFailure) {
                                                                throw new UaException(
                                                                    StatusCodes.Bad_TypeMismatch,
                                                                    conversionFailure);
                                                              }
                                                            }
                                                            if (outputArguments[1] == null) {
                                                              throw new UaException(
                                                                  StatusCodes.Bad_TypeMismatch,
                                                                  "Null Method output Variant");
                                                            }
                                                            @Nullable UInteger @Nullable []
                                                                decoded1;
                                                            {
                                                              Object methodValue =
                                                                  outputArguments[1].getValue();
                                                              try {
                                                                if (methodValue instanceof Matrix
                                                                    && ((Matrix) methodValue)
                                                                        .isNull()) {
                                                                  methodValue = null;
                                                                }
                                                                NamespaceTable namespaceTable =
                                                                    this.client.getNamespaceTable();
                                                                DataTypeTree dataTypeTree_ =
                                                                    outputDataTypeTree;
                                                                NodeId argumentDataTypeId =
                                                                    ExpandedNodeId.parse("i=7")
                                                                        .toNodeId(namespaceTable)
                                                                        .orElseThrow(
                                                                            () ->
                                                                                new UaException(
                                                                                    StatusCodes
                                                                                        .Bad_NodeIdInvalid,
                                                                                    "Method"
                                                                                        + " argument"
                                                                                        + " DataType"
                                                                                        + " namespace"
                                                                                        + " is unavailable"));
                                                                if (!OpcUaDataType.isBuiltin(
                                                                        argumentDataTypeId)
                                                                    && dataTypeTree_.getDataType(
                                                                            argumentDataTypeId)
                                                                        == null) {
                                                                  throw new UaException(
                                                                      StatusCodes.Bad_TypeMismatch,
                                                                      "Method argument"
                                                                          + " ClientHandles"
                                                                          + " (effective property"
                                                                          + " i=11491, DataType"
                                                                          + " i=7) is unavailable"
                                                                          + " in the effective type"
                                                                          + " tree; resolved"
                                                                          + " DataType: "
                                                                          + argumentDataTypeId);
                                                                }
                                                                Object numericElements =
                                                                    methodValue instanceof Matrix
                                                                        ? ((Matrix) methodValue)
                                                                            .getElements()
                                                                        : methodValue;
                                                                if (numericElements != null
                                                                    && numericElements
                                                                        .getClass()
                                                                        .isArray()
                                                                    && (numericElements
                                                                                .getClass()
                                                                                .getComponentType()
                                                                            == Number.class
                                                                        || numericElements
                                                                                .getClass()
                                                                                .getComponentType()
                                                                            == UNumber.class)
                                                                    && (argumentDataTypeId.equals(
                                                                            NodeIds.Number)
                                                                        || dataTypeTree_
                                                                            .isSubtypeOf(
                                                                                argumentDataTypeId,
                                                                                NodeIds.Number))) {
                                                                  Class<?> numericElementType =
                                                                      null;
                                                                  for (int numericIndex = 0;
                                                                      numericIndex
                                                                          < Array.getLength(
                                                                              numericElements);
                                                                      numericIndex++) {
                                                                    Object numericElement =
                                                                        Array.get(
                                                                            numericElements,
                                                                            numericIndex);
                                                                    if (numericElement != null) {
                                                                      if (numericElementType != null
                                                                          && numericElementType
                                                                              != numericElement
                                                                                  .getClass()) {
                                                                        throw new UaException(
                                                                            StatusCodes
                                                                                .Bad_TypeMismatch,
                                                                            "An abstract numeric"
                                                                                + " array requires"
                                                                                + " one homogeneous"
                                                                                + " wire element"
                                                                                + " type");
                                                                      }
                                                                      numericElementType =
                                                                          numericElement.getClass();
                                                                    }
                                                                  }
                                                                  if (numericElementType == null) {
                                                                    numericElementType =
                                                                        dataTypeTree_
                                                                            .getBackingClass(
                                                                                argumentDataTypeId);
                                                                  }
                                                                  if (numericElementType
                                                                          == Number.class
                                                                      || numericElementType
                                                                          == UNumber.class) {
                                                                    throw new UaException(
                                                                        StatusCodes
                                                                            .Bad_TypeMismatch,
                                                                        "An empty or all-null"
                                                                            + " abstract numeric"
                                                                            + " array requires a"
                                                                            + " concretely typed"
                                                                            + " array");
                                                                  }
                                                                  Object numericArray =
                                                                      Array.newInstance(
                                                                          numericElementType,
                                                                          Array.getLength(
                                                                              numericElements));
                                                                  for (int numericIndex = 0;
                                                                      numericIndex
                                                                          < Array.getLength(
                                                                              numericElements);
                                                                      numericIndex++) {
                                                                    Array.set(
                                                                        numericArray,
                                                                        numericIndex,
                                                                        Array.get(
                                                                            numericElements,
                                                                            numericIndex));
                                                                  }
                                                                  if (methodValue
                                                                      instanceof Matrix) {
                                                                    methodValue =
                                                                        new Matrix(
                                                                            numericArray,
                                                                            ((Matrix) methodValue)
                                                                                .getDimensions()
                                                                                .clone(),
                                                                            ((Matrix) methodValue)
                                                                                .getDataType()
                                                                                .orElseThrow(
                                                                                    () ->
                                                                                        new UaException(
                                                                                            StatusCodes
                                                                                                .Bad_TypeMismatch,
                                                                                            "A numeric"
                                                                                                + " Matrix"
                                                                                                + " requires"
                                                                                                + " an explicit"
                                                                                                + " wire"
                                                                                                + " DataType")),
                                                                            ((Matrix) methodValue)
                                                                                .getDataTypeId()
                                                                                .orElse(null));
                                                                  } else {
                                                                    methodValue = numericArray;
                                                                  }
                                                                }
                                                                if (methodValue != null) {
                                                                  Object shapeElements =
                                                                      methodValue instanceof Matrix
                                                                          ? ((Matrix) methodValue)
                                                                              .getElements()
                                                                          : methodValue;
                                                                  int valueRank =
                                                                      methodValue instanceof Matrix
                                                                          ? ((Matrix) methodValue)
                                                                              .getValueRank()
                                                                          : ArrayUtil.getValueRank(
                                                                              methodValue);
                                                                  boolean emptyArray =
                                                                      methodValue
                                                                              .getClass()
                                                                              .isArray()
                                                                          && ArrayUtil.getValueRank(
                                                                                  methodValue)
                                                                              == 1
                                                                          && Array.getLength(
                                                                                  methodValue)
                                                                              == 0;
                                                                  if (!(valueRank == 1
                                                                      || emptyArray)) {
                                                                    throw new UaException(
                                                                        StatusCodes
                                                                            .Bad_TypeMismatch,
                                                                        "Method argument ValueRank"
                                                                            + " mismatch");
                                                                  }
                                                                  if (methodValue
                                                                      instanceof Matrix) {
                                                                    int[] dimensions =
                                                                        ((Matrix) methodValue)
                                                                            .getDimensions();
                                                                    if (dimensions.length < 2
                                                                        || !shapeElements
                                                                            .getClass()
                                                                            .isArray()
                                                                        || ArrayUtil.getValueRank(
                                                                                shapeElements)
                                                                            != 1) {
                                                                      throw new UaException(
                                                                          StatusCodes
                                                                              .Bad_TypeMismatch,
                                                                          "Malformed Method Matrix"
                                                                              + " representation");
                                                                    }
                                                                    long elementCount = 1;
                                                                    for (int dimension :
                                                                        dimensions) {
                                                                      if (dimension < 0
                                                                          || elementCount
                                                                              > Integer.MAX_VALUE) {
                                                                        throw new UaException(
                                                                            StatusCodes
                                                                                .Bad_TypeMismatch,
                                                                            "Malformed Method"
                                                                                + " Matrix"
                                                                                + " dimensions");
                                                                      }
                                                                      elementCount *= dimension;
                                                                    }
                                                                    if (elementCount
                                                                        != Array.getLength(
                                                                            shapeElements)) {
                                                                      throw new UaException(
                                                                          StatusCodes
                                                                              .Bad_TypeMismatch,
                                                                          "Method Matrix dimensions"
                                                                              + " do not match its"
                                                                              + " elements");
                                                                    }
                                                                    if (!(((Matrix) methodValue)
                                                                        .getDataType()
                                                                        .equals(
                                                                            Variant.of(
                                                                                    shapeElements)
                                                                                .getDataType()))) {
                                                                      throw new UaException(
                                                                          StatusCodes
                                                                              .Bad_TypeMismatch,
                                                                          "Method Matrix DataType"
                                                                              + " does not match"
                                                                              + " its elements");
                                                                    }
                                                                  }
                                                                  Variant.of(shapeElements);
                                                                  if (!emptyArray) {
                                                                    int[] dimensions =
                                                                        methodValue
                                                                                instanceof Matrix
                                                                            ? ((Matrix) methodValue)
                                                                                .getDimensions()
                                                                            : ArrayUtil
                                                                                .getDimensions(
                                                                                    methodValue);
                                                                    long[] maximumDimensions =
                                                                        new long[] {0L};
                                                                    if (dimensions.length
                                                                        != maximumDimensions
                                                                            .length) {
                                                                      throw new UaException(
                                                                          StatusCodes
                                                                              .Bad_TypeMismatch,
                                                                          "Method argument"
                                                                              + " ArrayDimensions"
                                                                              + " mismatch");
                                                                    }
                                                                    for (int dimensionIndex = 0;
                                                                        dimensionIndex
                                                                            < dimensions.length;
                                                                        dimensionIndex++) {
                                                                      if (maximumDimensions[
                                                                                  dimensionIndex]
                                                                              != 0
                                                                          && dimensions[
                                                                                  dimensionIndex]
                                                                              > maximumDimensions[
                                                                                  dimensionIndex]) {
                                                                        throw new UaException(
                                                                            StatusCodes
                                                                                .Bad_TypeMismatch,
                                                                            "Method argument"
                                                                                + " exceeds"
                                                                                + " ArrayDimensions"
                                                                                + " maximum");
                                                                      }
                                                                    }
                                                                  }
                                                                }
                                                                if (methodValue != null) {
                                                                  Object typedElements =
                                                                      methodValue instanceof Matrix
                                                                          ? ((Matrix) methodValue)
                                                                              .getElements()
                                                                          : methodValue;
                                                                  if (NodeIds.Structure.equals(
                                                                          argumentDataTypeId)
                                                                      || dataTypeTree_.isStructType(
                                                                          argumentDataTypeId)) {
                                                                    var declaredType =
                                                                        dataTypeTree_.getType(
                                                                            argumentDataTypeId);
                                                                    if (typedElements
                                                                        .getClass()
                                                                        .isArray()) {
                                                                      var structureCodec =
                                                                          this.client
                                                                              .getStaticEncodingContext()
                                                                              .getDataTypeManager()
                                                                              .getCodec(
                                                                                  argumentDataTypeId);
                                                                      Class<?> structureClass =
                                                                          structureCodec == null
                                                                              ? UaStructuredType
                                                                                  .class
                                                                              : structureCodec
                                                                                  .getType();
                                                                      Object decodedStructures =
                                                                          Array.newInstance(
                                                                              structureClass,
                                                                              Array.getLength(
                                                                                  typedElements));
                                                                      for (int structureIndex = 0;
                                                                          structureIndex
                                                                              < Array.getLength(
                                                                                  typedElements);
                                                                          structureIndex++) {
                                                                        Object structure =
                                                                            Array.get(
                                                                                typedElements,
                                                                                structureIndex);
                                                                        if (structure
                                                                            instanceof
                                                                            ExtensionObject) {
                                                                          structure =
                                                                              ((ExtensionObject)
                                                                                          structure)
                                                                                      .isNull()
                                                                                  ? null
                                                                                  : ((ExtensionObject)
                                                                                          structure)
                                                                                      .decode(
                                                                                          this
                                                                                              .client
                                                                                              .getStaticEncodingContext());
                                                                        }
                                                                        if (structure != null) {
                                                                          if (!(structure
                                                                              instanceof
                                                                              UaStructuredType)) {
                                                                            throw new UaException(
                                                                                StatusCodes
                                                                                    .Bad_TypeMismatch,
                                                                                "Method argument"
                                                                                    + " requires a"
                                                                                    + " Structure"
                                                                                    + " value");
                                                                          }
                                                                          if (NodeIds.Structure
                                                                                  .equals(
                                                                                      argumentDataTypeId)
                                                                              || declaredType
                                                                                      != null
                                                                                  && declaredType
                                                                                      .isAbstract()) {
                                                                            if (!dataTypeTree_
                                                                                .isSubtypeOf(
                                                                                    ((UaStructuredType)
                                                                                            structure)
                                                                                        .getTypeId()
                                                                                        .toNodeId(
                                                                                            namespaceTable)
                                                                                        .orElse(
                                                                                            NodeId
                                                                                                .NULL_VALUE),
                                                                                    argumentDataTypeId)) {
                                                                              throw new UaException(
                                                                                  StatusCodes
                                                                                      .Bad_TypeMismatch,
                                                                                  "Method Structure"
                                                                                      + " is not a"
                                                                                      + " subtype"
                                                                                      + " of the"
                                                                                      + " effective"
                                                                                      + " DataType");
                                                                            }
                                                                          } else {
                                                                            if (!argumentDataTypeId
                                                                                .equals(
                                                                                    ((UaStructuredType)
                                                                                            structure)
                                                                                        .getTypeId()
                                                                                        .toNodeId(
                                                                                            namespaceTable)
                                                                                        .orElse(
                                                                                            NodeId
                                                                                                .NULL_VALUE))) {
                                                                              throw new UaException(
                                                                                  StatusCodes
                                                                                      .Bad_TypeMismatch,
                                                                                  "Method Structure"
                                                                                      + " does not"
                                                                                      + " match the"
                                                                                      + " effective"
                                                                                      + " DataType");
                                                                            }
                                                                          }
                                                                        }
                                                                        Array.set(
                                                                            decodedStructures,
                                                                            structureIndex,
                                                                            structure);
                                                                      }
                                                                      methodValue =
                                                                          methodValue
                                                                                  instanceof Matrix
                                                                              ? new Matrix(
                                                                                  decodedStructures,
                                                                                  ((Matrix)
                                                                                          methodValue)
                                                                                      .getDimensions()
                                                                                      .clone())
                                                                              : decodedStructures;
                                                                    } else {
                                                                      if (typedElements
                                                                          instanceof
                                                                          ExtensionObject) {
                                                                        typedElements =
                                                                            ((ExtensionObject)
                                                                                        typedElements)
                                                                                    .isNull()
                                                                                ? null
                                                                                : ((ExtensionObject)
                                                                                        typedElements)
                                                                                    .decode(
                                                                                        this.client
                                                                                            .getStaticEncodingContext());
                                                                      }
                                                                      if (typedElements != null) {
                                                                        if (!(typedElements
                                                                            instanceof
                                                                            UaStructuredType)) {
                                                                          throw new UaException(
                                                                              StatusCodes
                                                                                  .Bad_TypeMismatch,
                                                                              "Method argument"
                                                                                  + " requires a"
                                                                                  + " Structure"
                                                                                  + " value");
                                                                        }
                                                                        if (NodeIds.Structure
                                                                                .equals(
                                                                                    argumentDataTypeId)
                                                                            || declaredType != null
                                                                                && declaredType
                                                                                    .isAbstract()) {
                                                                          if (!dataTypeTree_
                                                                              .isSubtypeOf(
                                                                                  ((UaStructuredType)
                                                                                          typedElements)
                                                                                      .getTypeId()
                                                                                      .toNodeId(
                                                                                          namespaceTable)
                                                                                      .orElse(
                                                                                          NodeId
                                                                                              .NULL_VALUE),
                                                                                  argumentDataTypeId)) {
                                                                            throw new UaException(
                                                                                StatusCodes
                                                                                    .Bad_TypeMismatch,
                                                                                "Method Structure"
                                                                                    + " is not a"
                                                                                    + " subtype of"
                                                                                    + " the effective"
                                                                                    + " DataType");
                                                                          }
                                                                        } else {
                                                                          if (!argumentDataTypeId
                                                                              .equals(
                                                                                  ((UaStructuredType)
                                                                                          typedElements)
                                                                                      .getTypeId()
                                                                                      .toNodeId(
                                                                                          namespaceTable)
                                                                                      .orElse(
                                                                                          NodeId
                                                                                              .NULL_VALUE))) {
                                                                            throw new UaException(
                                                                                StatusCodes
                                                                                    .Bad_TypeMismatch,
                                                                                "Method Structure"
                                                                                    + " does not"
                                                                                    + " match the"
                                                                                    + " effective"
                                                                                    + " DataType");
                                                                          }
                                                                        }
                                                                      }
                                                                      methodValue = typedElements;
                                                                    }
                                                                  } else {
                                                                    Variant.of(typedElements);
                                                                    NodeId assignableDataTypeId =
                                                                        dataTypeTree_
                                                                                        .getBackingClass(
                                                                                            argumentDataTypeId)
                                                                                    == Number.class
                                                                                && dataTypeTree_
                                                                                    .isSubtypeOf(
                                                                                        argumentDataTypeId,
                                                                                        NodeIds
                                                                                            .Integer)
                                                                            ? NodeIds.Integer
                                                                            : argumentDataTypeId;
                                                                    if (dataTypeTree_
                                                                                .getBackingClass(
                                                                                    argumentDataTypeId)
                                                                            != Variant.class
                                                                        && !dataTypeTree_
                                                                            .isAssignable(
                                                                                assignableDataTypeId,
                                                                                ArrayUtil
                                                                                    .getBoxedType(
                                                                                        typedElements))) {
                                                                      throw new UaException(
                                                                          StatusCodes
                                                                              .Bad_TypeMismatch,
                                                                          "Method argument DataType"
                                                                              + " mismatch");
                                                                    }
                                                                  }
                                                                }
                                                                if (methodValue == null) {
                                                                  decoded1 = null;
                                                                } else {
                                                                  decoded1 =
                                                                      new UInteger
                                                                          [Array.getLength(
                                                                              methodValue)];
                                                                  for (int valueIndex = 0;
                                                                      valueIndex < decoded1.length;
                                                                      valueIndex++) {
                                                                    Object valueElement =
                                                                        Array.get(
                                                                            methodValue,
                                                                            valueIndex);
                                                                    decoded1[valueIndex] =
                                                                        (UInteger) valueElement;
                                                                  }
                                                                }
                                                              } catch (
                                                                  UaSerializationException
                                                                      conversionFailure) {
                                                                throw new UaException(
                                                                    conversionFailure
                                                                                .getStatusCode()
                                                                                .getValue()
                                                                            == StatusCodes
                                                                                .Bad_OutOfRange
                                                                        ? StatusCodes.Bad_OutOfRange
                                                                        : StatusCodes
                                                                            .Bad_TypeMismatch,
                                                                    conversionFailure);
                                                              } catch (ClassCastException
                                                                  | IllegalArgumentException
                                                                      conversionFailure) {
                                                                throw new UaException(
                                                                    StatusCodes.Bad_TypeMismatch,
                                                                    conversionFailure);
                                                              }
                                                            }
                                                            return ServerTypeGetMonitoredItemsOutputs
                                                                .of(decoded0, decoded1);
                                                          });
                                                    } catch (UaException failure) {
                                                      throw new CompletionException(failure);
                                                    }
                                                  });
                                            } catch (Exception failure) {
                                              return CompletableFuture.failedFuture(failure);
                                            }
                                          });
                                    });
                          } catch (Exception failure) {
                            return CompletableFuture.failedFuture(failure);
                          }
                        });
              });
      pipeline.whenComplete(
          (value, failure) -> {
            if (failure == null) {
              result.complete(value);
            } else {
              result.completeExceptionally(failure);
            }
          });
    } catch (RuntimeException failure) {
      result.completeExceptionally(failure);
    }
    return result;
  }

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part5/9.2
   *
   * <p>Resolves the optional member by its namespace-qualified path. Returns null only for
   * confirmed absence. Resolution does not create a UA node. It can perform service I/O and
   * construct or reuse a Java wrapper in Milo's address space cache. A reference can change after
   * lookup.
   *
   * @return the existing member, or null for confirmed absence
   * @throws org.eclipse.milo.opcua.stack.core.UaException if a required node is absent, resolution
   *     fails, or a checked conversion fails
   */
  @NullMarked
  @Override
  public @Nullable UaMethodNode getResendDataMethodNode() throws UaException {
    try {
      return getResendDataMethodNodeAsync().get();
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

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part5/9.2
   *
   * <p>Resolves the optional member by its namespace-qualified path. Returns null only for
   * confirmed absence. Resolution does not create a UA node. It can perform service I/O and
   * construct or reuse a Java wrapper in Milo's address space cache. A reference can change after
   * lookup.
   *
   * <p>Lookup, conversion and service failures complete the future exceptionally. UaException
   * causes preserve OPC UA status. Incompatible plain payload casts can complete exceptionally with
   * ClassCastException. Cancellation does not promise transport cancellation or rollback.
   *
   * @return a nonnull future completing with the existing member, or null for confirmed absence
   */
  @NullMarked
  @Override
  public CompletableFuture<? extends @Nullable UaMethodNode> getResendDataMethodNodeAsync() {
    CompletableFuture<UaMethodNode> result = new CompletableFuture<>();
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
                                "http://opcfoundation.org/UA/:ResendData (declaration i=12871,"
                                    + " owner i=2004) on "
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
                                        new QualifiedName(namespaceIndex, "ResendData"))
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
                                          "http://opcfoundation.org/UA/:ResendData (declaration"
                                              + " i=12871, owner i=2004) on "
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
                                          "http://opcfoundation.org/UA/:ResendData (declaration"
                                              + " i=12871, owner i=2004)"));
                                }
                                var targets = operation.getTargets();
                                if (targets == null || targets.length == 0) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_UnexpectedError,
                                          "http://opcfoundation.org/UA/:ResendData (declaration"
                                              + " i=12871, owner i=2004) on "
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
                                            "http://opcfoundation.org/UA/:ResendData (declaration"
                                                + " i=12871, owner i=2004) on "
                                                + getNodeId()));
                                  }
                                  if (!target.getTargetId().isLocal()) {
                                    return CompletableFuture.failedFuture(
                                        new UaException(
                                            StatusCodes.Bad_NotSupported,
                                            "http://opcfoundation.org/UA/:ResendData (declaration"
                                                + " i=12871, owner i=2004) on "
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
                                                            "http://opcfoundation.org/UA/:ResendData"
                                                                + " (declaration i=12871, owner"
                                                                + " i=2004) on "
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
                                                    "http://opcfoundation.org/UA/:ResendData"
                                                        + " (declaration i=12871, owner i=2004) on "
                                                        + getNodeId()));
                                          }
                                          if (unique.size() != 1) {
                                            return CompletableFuture.failedFuture(
                                                new UaException(
                                                    StatusCodes.Bad_TooManyMatches,
                                                    "http://opcfoundation.org/UA/:ResendData"
                                                        + " (declaration i=12871, owner i=2004) on "
                                                        + getNodeId()));
                                          }
                                          return client
                                              .getAddressSpace()
                                              .getNodeAsync(unique.iterator().next())
                                              .thenCompose(
                                                  node -> {
                                                    if (node == null
                                                        || node.getNodeClass()
                                                            != NodeClass.Method) {
                                                      return CompletableFuture.failedFuture(
                                                          new UaException(
                                                              StatusCodes.Bad_NodeClassInvalid,
                                                              "http://opcfoundation.org/UA/:ResendData"
                                                                  + " (declaration i=12871, owner"
                                                                  + " i=2004) on "
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
            } else if (node != null && !(node instanceof UaMethodNode)) {
              result.completeExceptionally(
                  new UaException(
                      StatusCodes.Bad_TypeMismatch,
                      "http://opcfoundation.org/UA/:ResendData (declaration i=12871, owner"
                          + " i=2004)"));
            } else {
              result.complete((UaMethodNode) node);
            }
          });
    } catch (Exception e) {
      result.completeExceptionally(e);
    }
    return result;
  }

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part5/9.2
   *
   * <p>Invokes <code>ResendData</code> on this node's ObjectId using the effective Method contract.
   *
   * <p>The Method node is required. Confirmed absence fails with Bad_NotFound; lookup and service
   * failures are preserved. The call does not create a node or retry an invocation.
   *
   * <p>Requires Good operation status, including Good subcodes. Uncertain and Bad statuses fail
   * with UaException before any output conversion failure is reported. Application status outputs
   * remain separate.
   *
   * <p>Interrupted waiting cancels owned observation and pending discovery, restores the interrupt
   * flag and fails with Bad_UnexpectedError. It does not cancel server execution.
   *
   * @param subscriptionId ; the supplied payload may be null.
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  @Override
  public void callResendData(@Nullable UInteger subscriptionId) throws UaException {
    callResendDataDetailed(subscriptionId).requireGood();
  }

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part5/9.2
   *
   * <p>Invokes <code>ResendData</code> on this node's ObjectId using the effective Method contract.
   *
   * <p>The Method node is required. Confirmed absence fails with Bad_NotFound; lookup and service
   * failures are preserved. The call does not create a node or retry an invocation.
   *
   * <p>Requires Good operation status, including Good subcodes. Uncertain and Bad statuses fail
   * with UaException before any output conversion failure is reported. Application status outputs
   * remain separate.
   *
   * <p>Returns a non-null future. Lookup, input metadata, transport and response envelope failures
   * complete it exceptionally. Cancellation stops observation and dependent work that has not
   * started; it does not cancel server execution.
   *
   * @param subscriptionId ; the supplied payload may be null.
   * @return a future whose successful payload is null.
   */
  @NullMarked
  @Override
  public CompletableFuture<? extends @Nullable Void> callResendDataAsync(
      @Nullable UInteger subscriptionId) {
    CompletableFuture<@Nullable Void> result = new CompletableFuture<>();
    var call = callResendDataDetailedAsync(subscriptionId);
    result.whenComplete(
        (value, failure) -> {
          if (result.isCancelled()) {
            call.cancel(false);
          }
        });
    call.whenComplete(
        (value, failure) -> {
          if (failure != null) {
            result.completeExceptionally(failure);
          } else {
            try {
              result.complete(value.requireGood());
            } catch (UaException statusFailure) {
              result.completeExceptionally(statusFailure);
            }
          }
        });
    return result;
  }

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part5/9.2
   *
   * <p>Invokes <code>ResendData</code> on this node's ObjectId using the effective Method contract.
   *
   * <p>The Method node is required. Confirmed absence fails with Bad_NotFound; lookup and service
   * failures are preserved. The call does not create a node or retry an invocation.
   *
   * <p>Retains operation status, request and response diagnostics, StringTable and raw outputs.
   * Good and Uncertain outputs are decoded; output metadata and conversion failures remain
   * available separately. Bad results preserve received wire outputs.
   *
   * <p>Interrupted waiting cancels owned observation and pending discovery, restores the interrupt
   * flag and fails with Bad_UnexpectedError. It does not cancel server execution.
   *
   * @param subscriptionId ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  @Override
  public MethodCallResult<? extends @Nullable Void> callResendDataDetailed(
      @Nullable UInteger subscriptionId) throws UaException {
    return callResendDataDetailed(MethodCallOptions.NONE, subscriptionId);
  }

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part5/9.2
   *
   * <p>Invokes <code>ResendData</code> on this node's ObjectId using the effective Method contract.
   *
   * <p>The Method node is required. Confirmed absence fails with Bad_NotFound; lookup and service
   * failures are preserved. The call does not create a node or retry an invocation.
   *
   * <p>Retains operation status, request and response diagnostics, StringTable and raw outputs.
   * Good and Uncertain outputs are decoded; output metadata and conversion failures remain
   * available separately. Bad results preserve received wire outputs.
   *
   * <p>Interrupted waiting cancels owned observation and pending discovery, restores the interrupt
   * flag and fails with Bad_UnexpectedError. It does not cancel server execution.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @param subscriptionId ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  @Override
  public MethodCallResult<? extends @Nullable Void> callResendDataDetailed(
      MethodCallOptions options, @Nullable UInteger subscriptionId) throws UaException {
    Objects.requireNonNull(options, "options");
    var awaitedMethod = callResendDataDetailedAsync(options, subscriptionId);
    try {
      return awaitedMethod.get();
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
      awaitedMethod.cancel(false);
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part5/9.2
   *
   * <p>Invokes <code>ResendData</code> on this node's ObjectId using the effective Method contract.
   *
   * <p>The Method node is required. Confirmed absence fails with Bad_NotFound; lookup and service
   * failures are preserved. The call does not create a node or retry an invocation.
   *
   * <p>Retains operation status, request and response diagnostics, StringTable and raw outputs.
   * Good and Uncertain outputs are decoded; output metadata and conversion failures remain
   * available separately. Bad results preserve received wire outputs.
   *
   * <p>Returns a non-null future. Lookup, input metadata, transport and response envelope failures
   * complete it exceptionally. Cancellation stops observation and dependent work that has not
   * started; it does not cancel server execution.
   *
   * @param subscriptionId ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  @Override
  public CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callResendDataDetailedAsync(@Nullable UInteger subscriptionId) {
    return callResendDataDetailedAsync(MethodCallOptions.NONE, subscriptionId);
  }

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part5/9.2
   *
   * <p>Invokes <code>ResendData</code> on this node's ObjectId using the effective Method contract.
   *
   * <p>The Method node is required. Confirmed absence fails with Bad_NotFound; lookup and service
   * failures are preserved. The call does not create a node or retry an invocation.
   *
   * <p>Retains operation status, request and response diagnostics, StringTable and raw outputs.
   * Good and Uncertain outputs are decoded; output metadata and conversion failures remain
   * available separately. Bad results preserve received wire outputs.
   *
   * <p>Returns a non-null future. Lookup, input metadata, transport and response envelope failures
   * complete it exceptionally. Cancellation stops observation and dependent work that has not
   * started; it does not cancel server execution.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @param subscriptionId ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  @Override
  public CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callResendDataDetailedAsync(MethodCallOptions options, @Nullable UInteger subscriptionId) {
    CompletableFuture<MethodCallResult<@Nullable Void>> result = new CompletableFuture<>();
    try {
      Objects.requireNonNull(options, "options");
      List<@Nullable Object> rawInputs = new ArrayList<>();
      rawInputs.add(subscriptionId);
      var lookup = getResendDataMethodNodeAsync();
      result.whenComplete(
          (value, failure) -> {
            if (result.isCancelled()) {
              lookup.cancel(false);
            }
          });
      CompletableFuture<MethodCallResult<@Nullable Void>> pipeline =
          lookup.thenCompose(
              methodNode -> {
                if (result.isCancelled()) {
                  return CompletableFuture.failedFuture(new CancellationException());
                }
                if (methodNode == null) {
                  return CompletableFuture.failedFuture(
                      new UaException(
                          StatusCodes.Bad_NotFound,
                          "Method node is required for invocation: ResendData"));
                }
                var inputMetadata =
                    ClientDataTypes.read(
                        this.client,
                        List.<ExpandedNodeId>of(ExpandedNodeId.parse("i=7"))
                            .subList(0, rawInputs.size()),
                        rawInputs.toArray());
                result.whenComplete(
                    (cancelledValue, cancelledFailure) -> {
                      if (result.isCancelled()) {
                        inputMetadata.cancel(false);
                      }
                    });
                return inputMetadata
                    .handle(
                        (inputDataTypeTree, inputMetadataFailure) -> {
                          if (inputMetadataFailure != null) {
                            var checkedMetadataFailureinputMetadataFailure =
                                UaException.extract(inputMetadataFailure);
                            if (checkedMetadataFailureinputMetadataFailure.isPresent()) {
                              throw new CompletionException(
                                  checkedMetadataFailureinputMetadataFailure.orElseThrow());
                            }
                            Throwable metadataCauseinputMetadataFailure = inputMetadataFailure;
                            while (metadataCauseinputMetadataFailure != null) {
                              if (metadataCauseinputMetadataFailure
                                  instanceof
                                  UaSerializationException metadataCodecinputMetadataFailure) {
                                throw new CompletionException(
                                    new UaException(
                                        metadataCodecinputMetadataFailure.getStatusCode().getValue()
                                                == StatusCodes.Bad_OutOfRange
                                            ? StatusCodes.Bad_OutOfRange
                                            : StatusCodes.Bad_TypeMismatch,
                                        metadataCodecinputMetadataFailure));
                              }
                              metadataCauseinputMetadataFailure =
                                  metadataCauseinputMetadataFailure.getCause();
                            }
                            throw new CompletionException(
                                UaException.extract(inputMetadataFailure)
                                    .orElseGet(() -> new UaException(inputMetadataFailure)));
                          }
                          return inputDataTypeTree;
                        })
                    .thenCompose(
                        dataTypeTree -> {
                          if (result.isCancelled()) {
                            return CompletableFuture.failedFuture(new CancellationException());
                          }
                          try {
                            List<Variant> inputArguments = new ArrayList<>();
                            if (rawInputs.size() > 0) {
                              Variant encoded0;
                              {
                                @Nullable UInteger convertedValue;
                                {
                                  Object methodValue = rawInputs.get(0);
                                  try {
                                    if (methodValue instanceof Matrix
                                        && ((Matrix) methodValue).isNull()) {
                                      methodValue = null;
                                    }
                                    NamespaceTable namespaceTable = this.client.getNamespaceTable();
                                    DataTypeTree dataTypeTree_ = dataTypeTree;
                                    NodeId argumentDataTypeId =
                                        ExpandedNodeId.parse("i=7")
                                            .toNodeId(namespaceTable)
                                            .orElseThrow(
                                                () ->
                                                    new UaException(
                                                        StatusCodes.Bad_NodeIdInvalid,
                                                        "Method argument DataType namespace is"
                                                            + " unavailable"));
                                    if (!OpcUaDataType.isBuiltin(argumentDataTypeId)
                                        && dataTypeTree_.getDataType(argumentDataTypeId) == null) {
                                      throw new UaException(
                                          StatusCodes.Bad_TypeMismatch,
                                          "Method argument SubscriptionId (effective property"
                                              + " i=12872, DataType i=7) is unavailable in the"
                                              + " effective type tree; resolved DataType: "
                                              + argumentDataTypeId);
                                    }
                                    Object numericElements =
                                        methodValue instanceof Matrix
                                            ? ((Matrix) methodValue).getElements()
                                            : methodValue;
                                    if (numericElements != null
                                        && numericElements.getClass().isArray()
                                        && (numericElements.getClass().getComponentType()
                                                == Number.class
                                            || numericElements.getClass().getComponentType()
                                                == UNumber.class)
                                        && (argumentDataTypeId.equals(NodeIds.Number)
                                            || dataTypeTree_.isSubtypeOf(
                                                argumentDataTypeId, NodeIds.Number))) {
                                      Class<?> numericElementType = null;
                                      for (int numericIndex = 0;
                                          numericIndex < Array.getLength(numericElements);
                                          numericIndex++) {
                                        Object numericElement =
                                            Array.get(numericElements, numericIndex);
                                        if (numericElement != null) {
                                          if (numericElementType != null
                                              && numericElementType != numericElement.getClass()) {
                                            throw new UaException(
                                                StatusCodes.Bad_TypeMismatch,
                                                "An abstract numeric array requires one homogeneous"
                                                    + " wire element type");
                                          }
                                          numericElementType = numericElement.getClass();
                                        }
                                      }
                                      if (numericElementType == null) {
                                        numericElementType =
                                            dataTypeTree_.getBackingClass(argumentDataTypeId);
                                      }
                                      if (numericElementType == Number.class
                                          || numericElementType == UNumber.class) {
                                        throw new UaException(
                                            StatusCodes.Bad_TypeMismatch,
                                            "An empty or all-null abstract numeric array requires a"
                                                + " concretely typed array");
                                      }
                                      Object numericArray =
                                          Array.newInstance(
                                              numericElementType, Array.getLength(numericElements));
                                      for (int numericIndex = 0;
                                          numericIndex < Array.getLength(numericElements);
                                          numericIndex++) {
                                        Array.set(
                                            numericArray,
                                            numericIndex,
                                            Array.get(numericElements, numericIndex));
                                      }
                                      if (methodValue instanceof Matrix) {
                                        methodValue =
                                            new Matrix(
                                                numericArray,
                                                ((Matrix) methodValue).getDimensions().clone(),
                                                ((Matrix) methodValue)
                                                    .getDataType()
                                                    .orElseThrow(
                                                        () ->
                                                            new UaException(
                                                                StatusCodes.Bad_TypeMismatch,
                                                                "A numeric Matrix requires an"
                                                                    + " explicit wire DataType")),
                                                ((Matrix) methodValue)
                                                    .getDataTypeId()
                                                    .orElse(null));
                                      } else {
                                        methodValue = numericArray;
                                      }
                                    }
                                    if (methodValue != null) {
                                      Object shapeElements =
                                          methodValue instanceof Matrix
                                              ? ((Matrix) methodValue).getElements()
                                              : methodValue;
                                      int valueRank =
                                          methodValue instanceof Matrix
                                              ? ((Matrix) methodValue).getValueRank()
                                              : ArrayUtil.getValueRank(methodValue);
                                      boolean emptyArray =
                                          methodValue.getClass().isArray()
                                              && ArrayUtil.getValueRank(methodValue) == 1
                                              && Array.getLength(methodValue) == 0;
                                      if (!(valueRank == -1)) {
                                        throw new UaException(
                                            StatusCodes.Bad_TypeMismatch,
                                            "Method argument ValueRank mismatch");
                                      }
                                      if (methodValue instanceof Matrix) {
                                        int[] dimensions = ((Matrix) methodValue).getDimensions();
                                        if (dimensions.length < 2
                                            || !shapeElements.getClass().isArray()
                                            || ArrayUtil.getValueRank(shapeElements) != 1) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "Malformed Method Matrix representation");
                                        }
                                        long elementCount = 1;
                                        for (int dimension : dimensions) {
                                          if (dimension < 0 || elementCount > Integer.MAX_VALUE) {
                                            throw new UaException(
                                                StatusCodes.Bad_TypeMismatch,
                                                "Malformed Method Matrix dimensions");
                                          }
                                          elementCount *= dimension;
                                        }
                                        if (elementCount != Array.getLength(shapeElements)) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "Method Matrix dimensions do not match its elements");
                                        }
                                        if (!(((Matrix) methodValue)
                                            .getDataType()
                                            .equals(Variant.of(shapeElements).getDataType()))) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "Method Matrix DataType does not match its elements");
                                        }
                                      }
                                      Variant.of(shapeElements);
                                    }
                                    if (methodValue != null) {
                                      Object typedElements =
                                          methodValue instanceof Matrix
                                              ? ((Matrix) methodValue).getElements()
                                              : methodValue;
                                      if (NodeIds.Structure.equals(argumentDataTypeId)
                                          || dataTypeTree_.isStructType(argumentDataTypeId)) {
                                        var declaredType =
                                            dataTypeTree_.getType(argumentDataTypeId);
                                        if (typedElements.getClass().isArray()) {
                                          var structureCodec =
                                              this.client
                                                  .getStaticEncodingContext()
                                                  .getDataTypeManager()
                                                  .getCodec(argumentDataTypeId);
                                          Class<?> structureClass =
                                              structureCodec == null
                                                  ? UaStructuredType.class
                                                  : structureCodec.getType();
                                          Object decodedStructures =
                                              Array.newInstance(
                                                  structureClass, Array.getLength(typedElements));
                                          for (int structureIndex = 0;
                                              structureIndex < Array.getLength(typedElements);
                                              structureIndex++) {
                                            Object structure =
                                                Array.get(typedElements, structureIndex);
                                            if (structure instanceof ExtensionObject) {
                                              structure =
                                                  ((ExtensionObject) structure).isNull()
                                                      ? null
                                                      : ((ExtensionObject) structure)
                                                          .decode(
                                                              this.client
                                                                  .getStaticEncodingContext());
                                            }
                                            if (structure != null) {
                                              if (!(structure instanceof UaStructuredType)) {
                                                throw new UaException(
                                                    StatusCodes.Bad_TypeMismatch,
                                                    "Method argument requires a Structure value");
                                              }
                                              if (NodeIds.Structure.equals(argumentDataTypeId)
                                                  || declaredType != null
                                                      && declaredType.isAbstract()) {
                                                if (!dataTypeTree_.isSubtypeOf(
                                                    ((UaStructuredType) structure)
                                                        .getTypeId()
                                                        .toNodeId(namespaceTable)
                                                        .orElse(NodeId.NULL_VALUE),
                                                    argumentDataTypeId)) {
                                                  throw new UaException(
                                                      StatusCodes.Bad_TypeMismatch,
                                                      "Method Structure is not a subtype of the"
                                                          + " effective DataType");
                                                }
                                              } else {
                                                if (!argumentDataTypeId.equals(
                                                    ((UaStructuredType) structure)
                                                        .getTypeId()
                                                        .toNodeId(namespaceTable)
                                                        .orElse(NodeId.NULL_VALUE))) {
                                                  throw new UaException(
                                                      StatusCodes.Bad_TypeMismatch,
                                                      "Method Structure does not match the"
                                                          + " effective DataType");
                                                }
                                              }
                                            }
                                            Array.set(decodedStructures, structureIndex, structure);
                                          }
                                          methodValue =
                                              methodValue instanceof Matrix
                                                  ? new Matrix(
                                                      decodedStructures,
                                                      ((Matrix) methodValue)
                                                          .getDimensions()
                                                          .clone())
                                                  : decodedStructures;
                                        } else {
                                          if (typedElements instanceof ExtensionObject) {
                                            typedElements =
                                                ((ExtensionObject) typedElements).isNull()
                                                    ? null
                                                    : ((ExtensionObject) typedElements)
                                                        .decode(
                                                            this.client.getStaticEncodingContext());
                                          }
                                          if (typedElements != null) {
                                            if (!(typedElements instanceof UaStructuredType)) {
                                              throw new UaException(
                                                  StatusCodes.Bad_TypeMismatch,
                                                  "Method argument requires a Structure value");
                                            }
                                            if (NodeIds.Structure.equals(argumentDataTypeId)
                                                || declaredType != null
                                                    && declaredType.isAbstract()) {
                                              if (!dataTypeTree_.isSubtypeOf(
                                                  ((UaStructuredType) typedElements)
                                                      .getTypeId()
                                                      .toNodeId(namespaceTable)
                                                      .orElse(NodeId.NULL_VALUE),
                                                  argumentDataTypeId)) {
                                                throw new UaException(
                                                    StatusCodes.Bad_TypeMismatch,
                                                    "Method Structure is not a subtype of the"
                                                        + " effective DataType");
                                              }
                                            } else {
                                              if (!argumentDataTypeId.equals(
                                                  ((UaStructuredType) typedElements)
                                                      .getTypeId()
                                                      .toNodeId(namespaceTable)
                                                      .orElse(NodeId.NULL_VALUE))) {
                                                throw new UaException(
                                                    StatusCodes.Bad_TypeMismatch,
                                                    "Method Structure does not match the effective"
                                                        + " DataType");
                                              }
                                            }
                                          }
                                          methodValue = typedElements;
                                        }
                                      } else {
                                        Variant.of(typedElements);
                                        NodeId assignableDataTypeId =
                                            dataTypeTree_.getBackingClass(argumentDataTypeId)
                                                        == Number.class
                                                    && dataTypeTree_.isSubtypeOf(
                                                        argumentDataTypeId, NodeIds.Integer)
                                                ? NodeIds.Integer
                                                : argumentDataTypeId;
                                        if (dataTypeTree_.getBackingClass(argumentDataTypeId)
                                                != Variant.class
                                            && !dataTypeTree_.isAssignable(
                                                assignableDataTypeId,
                                                ArrayUtil.getBoxedType(typedElements))) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "Method argument DataType mismatch");
                                        }
                                      }
                                    }
                                    convertedValue = (UInteger) methodValue;
                                  } catch (UaSerializationException conversionFailure) {
                                    throw new UaException(
                                        conversionFailure.getStatusCode().getValue()
                                                == StatusCodes.Bad_OutOfRange
                                            ? StatusCodes.Bad_OutOfRange
                                            : StatusCodes.Bad_TypeMismatch,
                                        conversionFailure);
                                  } catch (ClassCastException
                                      | IllegalArgumentException conversionFailure) {
                                    throw new UaException(
                                        StatusCodes.Bad_TypeMismatch, conversionFailure);
                                  }
                                }
                                try {
                                  Object wireValue = convertedValue;
                                  Object wireElements =
                                      wireValue instanceof Matrix
                                          ? ((Matrix) wireValue).getElements()
                                          : wireValue;
                                  var numericWireValues = new ArrayDeque<Object[]>();
                                  var numericWirePath =
                                      Collections.newSetFromMap(
                                          new IdentityHashMap<Object, Boolean>());
                                  if (wireValue != null) {
                                    numericWireValues.push(new Object[] {wireValue, false});
                                  }
                                  while (!numericWireValues.isEmpty()) {
                                    Object[] numericWireFrame = numericWireValues.pop();
                                    Object numericWireValue = numericWireFrame[0];
                                    if ((Boolean) numericWireFrame[1]) {
                                      numericWirePath.remove(numericWireValue);
                                      continue;
                                    }
                                    while (numericWireValue instanceof Variant
                                        || numericWireValue instanceof DataValue) {
                                      if (numericWireValue instanceof DataValue) {
                                        if (((DataValue) numericWireValue).getValue() == null) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "A DataValue requires a value wrapper; use"
                                                  + " Variant.NULL_VALUE for null");
                                        }
                                        if (((DataValue) numericWireValue).getStatusCode()
                                            == null) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "A DataValue requires a StatusCode; use"
                                                  + " StatusCode.GOOD for Good");
                                        }
                                        numericWireValue =
                                            ((DataValue) numericWireValue).getValue();
                                      } else {
                                        numericWireValue = ((Variant) numericWireValue).getValue();
                                      }
                                    }
                                    if (numericWireValue instanceof Matrix) {
                                      numericWireValue = ((Matrix) numericWireValue).getElements();
                                    }
                                    if (numericWireValue != null
                                        && numericWireValue.getClass().isArray()) {
                                      if (!numericWirePath.add(numericWireValue)) {
                                        throw new UaException(
                                            StatusCodes.Bad_TypeMismatch,
                                            "Cyclic Variant arrays cannot be encoded");
                                      }
                                      numericWireValues.push(new Object[] {numericWireValue, true});
                                      for (int numericWireIndex = 0;
                                          numericWireIndex < Array.getLength(numericWireValue);
                                          numericWireIndex++) {
                                        Object numericWireElement =
                                            Array.get(numericWireValue, numericWireIndex);
                                        if (numericWireElement == null
                                            && ArrayUtil.getBoxedType(numericWireValue)
                                                == Variant.class) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "A Variant wire array requires a wrapper for every"
                                                  + " element; use Variant.NULL_VALUE for null");
                                        }
                                        if (numericWireElement == null
                                            && (UaEnumeratedType.class.isAssignableFrom(
                                                    ArrayUtil.getBoxedType(numericWireValue))
                                                || OptionSetUInteger.class.isAssignableFrom(
                                                    ArrayUtil.getBoxedType(numericWireValue)))) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "An enum or OptionSet wire array cannot encode a null"
                                                  + " element");
                                        }
                                        if (numericWireElement == null
                                            && ArrayUtil.getBoxedType(numericWireValue)
                                                == Boolean.class) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "A Boolean wire array cannot retain a null element;"
                                                  + " Milo encodes it as false");
                                        }
                                        if (numericWireElement == null
                                            && ArrayUtil.getBoxedType(numericWireValue)
                                                == StatusCode.class) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "A StatusCode wire array cannot retain a null"
                                                  + " element; Milo encodes it as Good");
                                        }
                                        if (numericWireElement == null
                                            && Number.class.isAssignableFrom(
                                                ArrayUtil.getBoxedType(numericWireValue))) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "A numeric wire array cannot retain a null element;"
                                                  + " Milo encodes it as zero");
                                        }
                                        if (numericWireElement instanceof Variant
                                            || numericWireElement instanceof DataValue) {
                                          numericWireValues.push(
                                              new Object[] {numericWireElement, false});
                                        }
                                      }
                                    }
                                  }
                                  wireValue =
                                      ExtensionObject.encodeValue(
                                          this.client.getStaticEncodingContext(), wireValue);
                                  encoded0 = Variant.of(wireValue);
                                } catch (UaSerializationException encodingFailure) {
                                  throw new UaException(
                                      encodingFailure.getStatusCode().getValue()
                                              == StatusCodes.Bad_OutOfRange
                                          ? StatusCodes.Bad_OutOfRange
                                          : StatusCodes.Bad_TypeMismatch,
                                      encodingFailure);
                                } catch (ClassCastException
                                    | IllegalArgumentException encodingFailure) {
                                  throw new UaException(
                                      StatusCodes.Bad_TypeMismatch, encodingFailure);
                                }
                              }
                              inputArguments.add(encoded0);
                            }
                            CallMethodRequest request =
                                new CallMethodRequest(
                                    getNodeId(),
                                    methodNode.getNodeId(),
                                    inputArguments.toArray(new Variant[0]));
                            return this.client
                                .getSessionAsync()
                                .thenCompose(
                                    session -> {
                                      if (result.isCancelled()) {
                                        return CompletableFuture.failedFuture(
                                            new CancellationException());
                                      }
                                      RequestHeader originalHeader =
                                          this.client.newRequestHeader(
                                              session.getAuthenticationToken());
                                      RequestHeader requestHeader =
                                          new RequestHeader(
                                              originalHeader.getAuthenticationToken(),
                                              originalHeader.getTimestamp(),
                                              originalHeader.getRequestHandle(),
                                              options.returnDiagnostics(),
                                              originalHeader.getAuditEntryId(),
                                              originalHeader.getTimeoutHint(),
                                              originalHeader.getAdditionalHeader());
                                      var call =
                                          this.client.sendRequestAsync(
                                              new CallRequest(
                                                  requestHeader,
                                                  new CallMethodRequest[] {request}));
                                      result.whenComplete(
                                          (value, failure) -> {
                                            if (result.isCancelled()) {
                                              call.cancel(false);
                                            }
                                          });
                                      return call.thenCompose(
                                          message -> {
                                            if (result.isCancelled()) {
                                              throw new CancellationException();
                                            }
                                            try {
                                              if (!(message instanceof CallResponse response)
                                                  || response.getResponseHeader() == null
                                                  || response.getResponseHeader().getServiceResult()
                                                      == null) {
                                                throw new UaException(
                                                    StatusCodes.Bad_UnexpectedError,
                                                    "Malformed Call response header");
                                              }
                                              if (!response
                                                  .getResponseHeader()
                                                  .getServiceResult()
                                                  .isGood()) {
                                                throw new UaException(
                                                    response
                                                        .getResponseHeader()
                                                        .getServiceResult());
                                              }
                                              if (response.getResults() == null
                                                  || response.getResults().length != 1
                                                  || response.getResults()[0] == null
                                                  || response.getResults()[0].getStatusCode()
                                                      == null) {
                                                throw new UaException(
                                                    StatusCodes.Bad_UnexpectedError,
                                                    "Expected exactly one Call operation result");
                                              }
                                              CompletableFuture<DataTypeTree> outputMetadata =
                                                  response.getResults()[0].getStatusCode().isBad()
                                                      ? CompletableFuture.completedFuture(
                                                          dataTypeTree)
                                                      : ClientDataTypes.read(
                                                          this.client,
                                                          List.<ExpandedNodeId>of(),
                                                          (Object)
                                                              response.getResults()[0]
                                                                  .getOutputArguments());
                                              result.whenComplete(
                                                  (cancelledValue, cancelledFailure) -> {
                                                    if (result.isCancelled()) {
                                                      outputMetadata.cancel(false);
                                                    }
                                                  });
                                              return outputMetadata.handle(
                                                  (outputDataTypeTree, metadataFailure) -> {
                                                    if (result.isCancelled()) {
                                                      throw new CancellationException();
                                                    }
                                                    try {
                                                      return MethodCallResult.decode(
                                                          request,
                                                          requestHeader,
                                                          response,
                                                          0,
                                                          outputArguments -> {
                                                            if (metadataFailure != null) {
                                                              var
                                                                  checkedMetadataFailuremetadataFailure =
                                                                      UaException.extract(
                                                                          metadataFailure);
                                                              if (checkedMetadataFailuremetadataFailure
                                                                  .isPresent()) {
                                                                throw checkedMetadataFailuremetadataFailure
                                                                    .orElseThrow();
                                                              }
                                                              Throwable
                                                                  metadataCausemetadataFailure =
                                                                      metadataFailure;
                                                              while (metadataCausemetadataFailure
                                                                  != null) {
                                                                if (metadataCausemetadataFailure
                                                                    instanceof
                                                                    UaSerializationException
                                                                        metadataCodecmetadataFailure) {
                                                                  throw new UaException(
                                                                      metadataCodecmetadataFailure
                                                                                  .getStatusCode()
                                                                                  .getValue()
                                                                              == StatusCodes
                                                                                  .Bad_OutOfRange
                                                                          ? StatusCodes
                                                                              .Bad_OutOfRange
                                                                          : StatusCodes
                                                                              .Bad_TypeMismatch,
                                                                      metadataCodecmetadataFailure);
                                                                }
                                                                metadataCausemetadataFailure =
                                                                    metadataCausemetadataFailure
                                                                        .getCause();
                                                              }
                                                              throw UaException.extract(
                                                                      metadataFailure)
                                                                  .orElseGet(
                                                                      () ->
                                                                          new UaException(
                                                                              metadataFailure));
                                                            }
                                                            if ((outputArguments == null
                                                                    ? 0
                                                                    : outputArguments.length)
                                                                != 0) {
                                                              throw new UaException(
                                                                  StatusCodes.Bad_TypeMismatch,
                                                                  "Unexpected Method output count");
                                                            }
                                                            return null;
                                                          });
                                                    } catch (UaException failure) {
                                                      throw new CompletionException(failure);
                                                    }
                                                  });
                                            } catch (Exception failure) {
                                              return CompletableFuture.failedFuture(failure);
                                            }
                                          });
                                    });
                          } catch (Exception failure) {
                            return CompletableFuture.failedFuture(failure);
                          }
                        });
              });
      pipeline.whenComplete(
          (value, failure) -> {
            if (failure == null) {
              result.complete(value);
            } else {
              result.completeExceptionally(failure);
            }
          });
    } catch (RuntimeException failure) {
      result.completeExceptionally(failure);
    }
    return result;
  }

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part5/9.3
   *
   * <p>Resolves the optional member by its namespace-qualified path. Returns null only for
   * confirmed absence. Resolution does not create a UA node. It can perform service I/O and
   * construct or reuse a Java wrapper in Milo's address space cache. A reference can change after
   * lookup.
   *
   * @return the existing member, or null for confirmed absence
   * @throws org.eclipse.milo.opcua.stack.core.UaException if a required node is absent, resolution
   *     fails, or a checked conversion fails
   */
  @NullMarked
  @Override
  public @Nullable UaMethodNode getSetSubscriptionDurableMethodNode() throws UaException {
    try {
      return getSetSubscriptionDurableMethodNodeAsync().get();
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

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part5/9.3
   *
   * <p>Resolves the optional member by its namespace-qualified path. Returns null only for
   * confirmed absence. Resolution does not create a UA node. It can perform service I/O and
   * construct or reuse a Java wrapper in Milo's address space cache. A reference can change after
   * lookup.
   *
   * <p>Lookup, conversion and service failures complete the future exceptionally. UaException
   * causes preserve OPC UA status. Incompatible plain payload casts can complete exceptionally with
   * ClassCastException. Cancellation does not promise transport cancellation or rollback.
   *
   * @return a nonnull future completing with the existing member, or null for confirmed absence
   */
  @NullMarked
  @Override
  public CompletableFuture<? extends @Nullable UaMethodNode>
      getSetSubscriptionDurableMethodNodeAsync() {
    CompletableFuture<UaMethodNode> result = new CompletableFuture<>();
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
                                "http://opcfoundation.org/UA/:SetSubscriptionDurable (declaration"
                                    + " i=12746, owner i=2004) on "
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
                                        new QualifiedName(namespaceIndex, "SetSubscriptionDurable"))
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
                                          "http://opcfoundation.org/UA/:SetSubscriptionDurable"
                                              + " (declaration i=12746, owner i=2004) on "
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
                                          "http://opcfoundation.org/UA/:SetSubscriptionDurable"
                                              + " (declaration i=12746, owner i=2004)"));
                                }
                                var targets = operation.getTargets();
                                if (targets == null || targets.length == 0) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_UnexpectedError,
                                          "http://opcfoundation.org/UA/:SetSubscriptionDurable"
                                              + " (declaration i=12746, owner i=2004) on "
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
                                            "http://opcfoundation.org/UA/:SetSubscriptionDurable"
                                                + " (declaration i=12746, owner i=2004) on "
                                                + getNodeId()));
                                  }
                                  if (!target.getTargetId().isLocal()) {
                                    return CompletableFuture.failedFuture(
                                        new UaException(
                                            StatusCodes.Bad_NotSupported,
                                            "http://opcfoundation.org/UA/:SetSubscriptionDurable"
                                                + " (declaration i=12746, owner i=2004) on "
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
                                                            "http://opcfoundation.org/UA/:SetSubscriptionDurable"
                                                                + " (declaration i=12746, owner"
                                                                + " i=2004) on "
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
                                                    "http://opcfoundation.org/UA/:SetSubscriptionDurable"
                                                        + " (declaration i=12746, owner i=2004) on "
                                                        + getNodeId()));
                                          }
                                          if (unique.size() != 1) {
                                            return CompletableFuture.failedFuture(
                                                new UaException(
                                                    StatusCodes.Bad_TooManyMatches,
                                                    "http://opcfoundation.org/UA/:SetSubscriptionDurable"
                                                        + " (declaration i=12746, owner i=2004) on "
                                                        + getNodeId()));
                                          }
                                          return client
                                              .getAddressSpace()
                                              .getNodeAsync(unique.iterator().next())
                                              .thenCompose(
                                                  node -> {
                                                    if (node == null
                                                        || node.getNodeClass()
                                                            != NodeClass.Method) {
                                                      return CompletableFuture.failedFuture(
                                                          new UaException(
                                                              StatusCodes.Bad_NodeClassInvalid,
                                                              "http://opcfoundation.org/UA/:SetSubscriptionDurable"
                                                                  + " (declaration i=12746, owner"
                                                                  + " i=2004) on "
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
            } else if (node != null && !(node instanceof UaMethodNode)) {
              result.completeExceptionally(
                  new UaException(
                      StatusCodes.Bad_TypeMismatch,
                      "http://opcfoundation.org/UA/:SetSubscriptionDurable (declaration i=12746,"
                          + " owner i=2004)"));
            } else {
              result.complete((UaMethodNode) node);
            }
          });
    } catch (Exception e) {
      result.completeExceptionally(e);
    }
    return result;
  }

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part5/9.3
   *
   * <p>Invokes <code>SetSubscriptionDurable</code> on this node's ObjectId using the effective
   * Method contract.
   *
   * <p>The Method node is required. Confirmed absence fails with Bad_NotFound; lookup and service
   * failures are preserved. The call does not create a node or retry an invocation.
   *
   * <p>Requires Good operation status, including Good subcodes. Uncertain and Bad statuses fail
   * with UaException before any output conversion failure is reported. Application status outputs
   * remain separate.
   *
   * <p>Interrupted waiting cancels owned observation and pending discovery, restores the interrupt
   * flag and fails with Bad_UnexpectedError. It does not cancel server execution.
   *
   * @param subscriptionId ; the supplied payload may be null.
   * @param lifetimeInHours ; the supplied payload may be null.
   * @return the output value or object, or its future; a single value may be null.
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  @Override
  public @Nullable UInteger callSetSubscriptionDurable(
      @Nullable UInteger subscriptionId, @Nullable UInteger lifetimeInHours) throws UaException {
    return callSetSubscriptionDurableDetailed(subscriptionId, lifetimeInHours).requireGood();
  }

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part5/9.3
   *
   * <p>Invokes <code>SetSubscriptionDurable</code> on this node's ObjectId using the effective
   * Method contract.
   *
   * <p>The Method node is required. Confirmed absence fails with Bad_NotFound; lookup and service
   * failures are preserved. The call does not create a node or retry an invocation.
   *
   * <p>Requires Good operation status, including Good subcodes. Uncertain and Bad statuses fail
   * with UaException before any output conversion failure is reported. Application status outputs
   * remain separate.
   *
   * <p>Returns a non-null future. Lookup, input metadata, transport and response envelope failures
   * complete it exceptionally. Cancellation stops observation and dependent work that has not
   * started; it does not cancel server execution.
   *
   * @param subscriptionId ; the supplied payload may be null.
   * @param lifetimeInHours ; the supplied payload may be null.
   * @return the output value or object, or its future; a single value may be null.
   */
  @NullMarked
  @Override
  public CompletableFuture<? extends @Nullable UInteger> callSetSubscriptionDurableAsync(
      @Nullable UInteger subscriptionId, @Nullable UInteger lifetimeInHours) {
    CompletableFuture<@Nullable UInteger> result = new CompletableFuture<>();
    var call = callSetSubscriptionDurableDetailedAsync(subscriptionId, lifetimeInHours);
    result.whenComplete(
        (value, failure) -> {
          if (result.isCancelled()) {
            call.cancel(false);
          }
        });
    call.whenComplete(
        (value, failure) -> {
          if (failure != null) {
            result.completeExceptionally(failure);
          } else {
            try {
              result.complete(value.requireGood());
            } catch (UaException statusFailure) {
              result.completeExceptionally(statusFailure);
            }
          }
        });
    return result;
  }

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part5/9.3
   *
   * <p>Invokes <code>SetSubscriptionDurable</code> on this node's ObjectId using the effective
   * Method contract.
   *
   * <p>The Method node is required. Confirmed absence fails with Bad_NotFound; lookup and service
   * failures are preserved. The call does not create a node or retry an invocation.
   *
   * <p>Retains operation status, request and response diagnostics, StringTable and raw outputs.
   * Good and Uncertain outputs are decoded; output metadata and conversion failures remain
   * available separately. Bad results preserve received wire outputs.
   *
   * <p>Interrupted waiting cancels owned observation and pending discovery, restores the interrupt
   * flag and fails with Bad_UnexpectedError. It does not cancel server execution.
   *
   * @param subscriptionId ; the supplied payload may be null.
   * @param lifetimeInHours ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  @Override
  public MethodCallResult<? extends @Nullable UInteger> callSetSubscriptionDurableDetailed(
      @Nullable UInteger subscriptionId, @Nullable UInteger lifetimeInHours) throws UaException {
    return callSetSubscriptionDurableDetailed(
        MethodCallOptions.NONE, subscriptionId, lifetimeInHours);
  }

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part5/9.3
   *
   * <p>Invokes <code>SetSubscriptionDurable</code> on this node's ObjectId using the effective
   * Method contract.
   *
   * <p>The Method node is required. Confirmed absence fails with Bad_NotFound; lookup and service
   * failures are preserved. The call does not create a node or retry an invocation.
   *
   * <p>Retains operation status, request and response diagnostics, StringTable and raw outputs.
   * Good and Uncertain outputs are decoded; output metadata and conversion failures remain
   * available separately. Bad results preserve received wire outputs.
   *
   * <p>Interrupted waiting cancels owned observation and pending discovery, restores the interrupt
   * flag and fails with Bad_UnexpectedError. It does not cancel server execution.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @param subscriptionId ; the supplied payload may be null.
   * @param lifetimeInHours ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  @Override
  public MethodCallResult<? extends @Nullable UInteger> callSetSubscriptionDurableDetailed(
      MethodCallOptions options,
      @Nullable UInteger subscriptionId,
      @Nullable UInteger lifetimeInHours)
      throws UaException {
    Objects.requireNonNull(options, "options");
    var awaitedMethod =
        callSetSubscriptionDurableDetailedAsync(options, subscriptionId, lifetimeInHours);
    try {
      return awaitedMethod.get();
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
      awaitedMethod.cancel(false);
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part5/9.3
   *
   * <p>Invokes <code>SetSubscriptionDurable</code> on this node's ObjectId using the effective
   * Method contract.
   *
   * <p>The Method node is required. Confirmed absence fails with Bad_NotFound; lookup and service
   * failures are preserved. The call does not create a node or retry an invocation.
   *
   * <p>Retains operation status, request and response diagnostics, StringTable and raw outputs.
   * Good and Uncertain outputs are decoded; output metadata and conversion failures remain
   * available separately. Bad results preserve received wire outputs.
   *
   * <p>Returns a non-null future. Lookup, input metadata, transport and response envelope failures
   * complete it exceptionally. Cancellation stops observation and dependent work that has not
   * started; it does not cancel server execution.
   *
   * @param subscriptionId ; the supplied payload may be null.
   * @param lifetimeInHours ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  @Override
  public CompletableFuture<? extends MethodCallResult<? extends @Nullable UInteger>>
      callSetSubscriptionDurableDetailedAsync(
          @Nullable UInteger subscriptionId, @Nullable UInteger lifetimeInHours) {
    return callSetSubscriptionDurableDetailedAsync(
        MethodCallOptions.NONE, subscriptionId, lifetimeInHours);
  }

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part5/9.3
   *
   * <p>Invokes <code>SetSubscriptionDurable</code> on this node's ObjectId using the effective
   * Method contract.
   *
   * <p>The Method node is required. Confirmed absence fails with Bad_NotFound; lookup and service
   * failures are preserved. The call does not create a node or retry an invocation.
   *
   * <p>Retains operation status, request and response diagnostics, StringTable and raw outputs.
   * Good and Uncertain outputs are decoded; output metadata and conversion failures remain
   * available separately. Bad results preserve received wire outputs.
   *
   * <p>Returns a non-null future. Lookup, input metadata, transport and response envelope failures
   * complete it exceptionally. Cancellation stops observation and dependent work that has not
   * started; it does not cancel server execution.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @param subscriptionId ; the supplied payload may be null.
   * @param lifetimeInHours ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  @Override
  public CompletableFuture<? extends MethodCallResult<? extends @Nullable UInteger>>
      callSetSubscriptionDurableDetailedAsync(
          MethodCallOptions options,
          @Nullable UInteger subscriptionId,
          @Nullable UInteger lifetimeInHours) {
    CompletableFuture<MethodCallResult<@Nullable UInteger>> result = new CompletableFuture<>();
    try {
      Objects.requireNonNull(options, "options");
      List<@Nullable Object> rawInputs = new ArrayList<>();
      rawInputs.add(subscriptionId);
      rawInputs.add(lifetimeInHours);
      var lookup = getSetSubscriptionDurableMethodNodeAsync();
      result.whenComplete(
          (value, failure) -> {
            if (result.isCancelled()) {
              lookup.cancel(false);
            }
          });
      CompletableFuture<MethodCallResult<@Nullable UInteger>> pipeline =
          lookup.thenCompose(
              methodNode -> {
                if (result.isCancelled()) {
                  return CompletableFuture.failedFuture(new CancellationException());
                }
                if (methodNode == null) {
                  return CompletableFuture.failedFuture(
                      new UaException(
                          StatusCodes.Bad_NotFound,
                          "Method node is required for invocation: SetSubscriptionDurable"));
                }
                var inputMetadata =
                    ClientDataTypes.read(
                        this.client,
                        List.<ExpandedNodeId>of(
                                ExpandedNodeId.parse("i=7"), ExpandedNodeId.parse("i=7"))
                            .subList(0, rawInputs.size()),
                        rawInputs.toArray());
                result.whenComplete(
                    (cancelledValue, cancelledFailure) -> {
                      if (result.isCancelled()) {
                        inputMetadata.cancel(false);
                      }
                    });
                return inputMetadata
                    .handle(
                        (inputDataTypeTree, inputMetadataFailure) -> {
                          if (inputMetadataFailure != null) {
                            var checkedMetadataFailureinputMetadataFailure =
                                UaException.extract(inputMetadataFailure);
                            if (checkedMetadataFailureinputMetadataFailure.isPresent()) {
                              throw new CompletionException(
                                  checkedMetadataFailureinputMetadataFailure.orElseThrow());
                            }
                            Throwable metadataCauseinputMetadataFailure = inputMetadataFailure;
                            while (metadataCauseinputMetadataFailure != null) {
                              if (metadataCauseinputMetadataFailure
                                  instanceof
                                  UaSerializationException metadataCodecinputMetadataFailure) {
                                throw new CompletionException(
                                    new UaException(
                                        metadataCodecinputMetadataFailure.getStatusCode().getValue()
                                                == StatusCodes.Bad_OutOfRange
                                            ? StatusCodes.Bad_OutOfRange
                                            : StatusCodes.Bad_TypeMismatch,
                                        metadataCodecinputMetadataFailure));
                              }
                              metadataCauseinputMetadataFailure =
                                  metadataCauseinputMetadataFailure.getCause();
                            }
                            throw new CompletionException(
                                UaException.extract(inputMetadataFailure)
                                    .orElseGet(() -> new UaException(inputMetadataFailure)));
                          }
                          return inputDataTypeTree;
                        })
                    .thenCompose(
                        dataTypeTree -> {
                          if (result.isCancelled()) {
                            return CompletableFuture.failedFuture(new CancellationException());
                          }
                          try {
                            List<Variant> inputArguments = new ArrayList<>();
                            if (rawInputs.size() > 0) {
                              Variant encoded0;
                              {
                                @Nullable UInteger convertedValue;
                                {
                                  Object methodValue = rawInputs.get(0);
                                  try {
                                    if (methodValue instanceof Matrix
                                        && ((Matrix) methodValue).isNull()) {
                                      methodValue = null;
                                    }
                                    NamespaceTable namespaceTable = this.client.getNamespaceTable();
                                    DataTypeTree dataTypeTree_ = dataTypeTree;
                                    NodeId argumentDataTypeId =
                                        ExpandedNodeId.parse("i=7")
                                            .toNodeId(namespaceTable)
                                            .orElseThrow(
                                                () ->
                                                    new UaException(
                                                        StatusCodes.Bad_NodeIdInvalid,
                                                        "Method argument DataType namespace is"
                                                            + " unavailable"));
                                    if (!OpcUaDataType.isBuiltin(argumentDataTypeId)
                                        && dataTypeTree_.getDataType(argumentDataTypeId) == null) {
                                      throw new UaException(
                                          StatusCodes.Bad_TypeMismatch,
                                          "Method argument SubscriptionId (effective property"
                                              + " i=12747, DataType i=7) is unavailable in the"
                                              + " effective type tree; resolved DataType: "
                                              + argumentDataTypeId);
                                    }
                                    Object numericElements =
                                        methodValue instanceof Matrix
                                            ? ((Matrix) methodValue).getElements()
                                            : methodValue;
                                    if (numericElements != null
                                        && numericElements.getClass().isArray()
                                        && (numericElements.getClass().getComponentType()
                                                == Number.class
                                            || numericElements.getClass().getComponentType()
                                                == UNumber.class)
                                        && (argumentDataTypeId.equals(NodeIds.Number)
                                            || dataTypeTree_.isSubtypeOf(
                                                argumentDataTypeId, NodeIds.Number))) {
                                      Class<?> numericElementType = null;
                                      for (int numericIndex = 0;
                                          numericIndex < Array.getLength(numericElements);
                                          numericIndex++) {
                                        Object numericElement =
                                            Array.get(numericElements, numericIndex);
                                        if (numericElement != null) {
                                          if (numericElementType != null
                                              && numericElementType != numericElement.getClass()) {
                                            throw new UaException(
                                                StatusCodes.Bad_TypeMismatch,
                                                "An abstract numeric array requires one homogeneous"
                                                    + " wire element type");
                                          }
                                          numericElementType = numericElement.getClass();
                                        }
                                      }
                                      if (numericElementType == null) {
                                        numericElementType =
                                            dataTypeTree_.getBackingClass(argumentDataTypeId);
                                      }
                                      if (numericElementType == Number.class
                                          || numericElementType == UNumber.class) {
                                        throw new UaException(
                                            StatusCodes.Bad_TypeMismatch,
                                            "An empty or all-null abstract numeric array requires a"
                                                + " concretely typed array");
                                      }
                                      Object numericArray =
                                          Array.newInstance(
                                              numericElementType, Array.getLength(numericElements));
                                      for (int numericIndex = 0;
                                          numericIndex < Array.getLength(numericElements);
                                          numericIndex++) {
                                        Array.set(
                                            numericArray,
                                            numericIndex,
                                            Array.get(numericElements, numericIndex));
                                      }
                                      if (methodValue instanceof Matrix) {
                                        methodValue =
                                            new Matrix(
                                                numericArray,
                                                ((Matrix) methodValue).getDimensions().clone(),
                                                ((Matrix) methodValue)
                                                    .getDataType()
                                                    .orElseThrow(
                                                        () ->
                                                            new UaException(
                                                                StatusCodes.Bad_TypeMismatch,
                                                                "A numeric Matrix requires an"
                                                                    + " explicit wire DataType")),
                                                ((Matrix) methodValue)
                                                    .getDataTypeId()
                                                    .orElse(null));
                                      } else {
                                        methodValue = numericArray;
                                      }
                                    }
                                    if (methodValue != null) {
                                      Object shapeElements =
                                          methodValue instanceof Matrix
                                              ? ((Matrix) methodValue).getElements()
                                              : methodValue;
                                      int valueRank =
                                          methodValue instanceof Matrix
                                              ? ((Matrix) methodValue).getValueRank()
                                              : ArrayUtil.getValueRank(methodValue);
                                      boolean emptyArray =
                                          methodValue.getClass().isArray()
                                              && ArrayUtil.getValueRank(methodValue) == 1
                                              && Array.getLength(methodValue) == 0;
                                      if (!(valueRank == -1)) {
                                        throw new UaException(
                                            StatusCodes.Bad_TypeMismatch,
                                            "Method argument ValueRank mismatch");
                                      }
                                      if (methodValue instanceof Matrix) {
                                        int[] dimensions = ((Matrix) methodValue).getDimensions();
                                        if (dimensions.length < 2
                                            || !shapeElements.getClass().isArray()
                                            || ArrayUtil.getValueRank(shapeElements) != 1) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "Malformed Method Matrix representation");
                                        }
                                        long elementCount = 1;
                                        for (int dimension : dimensions) {
                                          if (dimension < 0 || elementCount > Integer.MAX_VALUE) {
                                            throw new UaException(
                                                StatusCodes.Bad_TypeMismatch,
                                                "Malformed Method Matrix dimensions");
                                          }
                                          elementCount *= dimension;
                                        }
                                        if (elementCount != Array.getLength(shapeElements)) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "Method Matrix dimensions do not match its elements");
                                        }
                                        if (!(((Matrix) methodValue)
                                            .getDataType()
                                            .equals(Variant.of(shapeElements).getDataType()))) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "Method Matrix DataType does not match its elements");
                                        }
                                      }
                                      Variant.of(shapeElements);
                                    }
                                    if (methodValue != null) {
                                      Object typedElements =
                                          methodValue instanceof Matrix
                                              ? ((Matrix) methodValue).getElements()
                                              : methodValue;
                                      if (NodeIds.Structure.equals(argumentDataTypeId)
                                          || dataTypeTree_.isStructType(argumentDataTypeId)) {
                                        var declaredType =
                                            dataTypeTree_.getType(argumentDataTypeId);
                                        if (typedElements.getClass().isArray()) {
                                          var structureCodec =
                                              this.client
                                                  .getStaticEncodingContext()
                                                  .getDataTypeManager()
                                                  .getCodec(argumentDataTypeId);
                                          Class<?> structureClass =
                                              structureCodec == null
                                                  ? UaStructuredType.class
                                                  : structureCodec.getType();
                                          Object decodedStructures =
                                              Array.newInstance(
                                                  structureClass, Array.getLength(typedElements));
                                          for (int structureIndex = 0;
                                              structureIndex < Array.getLength(typedElements);
                                              structureIndex++) {
                                            Object structure =
                                                Array.get(typedElements, structureIndex);
                                            if (structure instanceof ExtensionObject) {
                                              structure =
                                                  ((ExtensionObject) structure).isNull()
                                                      ? null
                                                      : ((ExtensionObject) structure)
                                                          .decode(
                                                              this.client
                                                                  .getStaticEncodingContext());
                                            }
                                            if (structure != null) {
                                              if (!(structure instanceof UaStructuredType)) {
                                                throw new UaException(
                                                    StatusCodes.Bad_TypeMismatch,
                                                    "Method argument requires a Structure value");
                                              }
                                              if (NodeIds.Structure.equals(argumentDataTypeId)
                                                  || declaredType != null
                                                      && declaredType.isAbstract()) {
                                                if (!dataTypeTree_.isSubtypeOf(
                                                    ((UaStructuredType) structure)
                                                        .getTypeId()
                                                        .toNodeId(namespaceTable)
                                                        .orElse(NodeId.NULL_VALUE),
                                                    argumentDataTypeId)) {
                                                  throw new UaException(
                                                      StatusCodes.Bad_TypeMismatch,
                                                      "Method Structure is not a subtype of the"
                                                          + " effective DataType");
                                                }
                                              } else {
                                                if (!argumentDataTypeId.equals(
                                                    ((UaStructuredType) structure)
                                                        .getTypeId()
                                                        .toNodeId(namespaceTable)
                                                        .orElse(NodeId.NULL_VALUE))) {
                                                  throw new UaException(
                                                      StatusCodes.Bad_TypeMismatch,
                                                      "Method Structure does not match the"
                                                          + " effective DataType");
                                                }
                                              }
                                            }
                                            Array.set(decodedStructures, structureIndex, structure);
                                          }
                                          methodValue =
                                              methodValue instanceof Matrix
                                                  ? new Matrix(
                                                      decodedStructures,
                                                      ((Matrix) methodValue)
                                                          .getDimensions()
                                                          .clone())
                                                  : decodedStructures;
                                        } else {
                                          if (typedElements instanceof ExtensionObject) {
                                            typedElements =
                                                ((ExtensionObject) typedElements).isNull()
                                                    ? null
                                                    : ((ExtensionObject) typedElements)
                                                        .decode(
                                                            this.client.getStaticEncodingContext());
                                          }
                                          if (typedElements != null) {
                                            if (!(typedElements instanceof UaStructuredType)) {
                                              throw new UaException(
                                                  StatusCodes.Bad_TypeMismatch,
                                                  "Method argument requires a Structure value");
                                            }
                                            if (NodeIds.Structure.equals(argumentDataTypeId)
                                                || declaredType != null
                                                    && declaredType.isAbstract()) {
                                              if (!dataTypeTree_.isSubtypeOf(
                                                  ((UaStructuredType) typedElements)
                                                      .getTypeId()
                                                      .toNodeId(namespaceTable)
                                                      .orElse(NodeId.NULL_VALUE),
                                                  argumentDataTypeId)) {
                                                throw new UaException(
                                                    StatusCodes.Bad_TypeMismatch,
                                                    "Method Structure is not a subtype of the"
                                                        + " effective DataType");
                                              }
                                            } else {
                                              if (!argumentDataTypeId.equals(
                                                  ((UaStructuredType) typedElements)
                                                      .getTypeId()
                                                      .toNodeId(namespaceTable)
                                                      .orElse(NodeId.NULL_VALUE))) {
                                                throw new UaException(
                                                    StatusCodes.Bad_TypeMismatch,
                                                    "Method Structure does not match the effective"
                                                        + " DataType");
                                              }
                                            }
                                          }
                                          methodValue = typedElements;
                                        }
                                      } else {
                                        Variant.of(typedElements);
                                        NodeId assignableDataTypeId =
                                            dataTypeTree_.getBackingClass(argumentDataTypeId)
                                                        == Number.class
                                                    && dataTypeTree_.isSubtypeOf(
                                                        argumentDataTypeId, NodeIds.Integer)
                                                ? NodeIds.Integer
                                                : argumentDataTypeId;
                                        if (dataTypeTree_.getBackingClass(argumentDataTypeId)
                                                != Variant.class
                                            && !dataTypeTree_.isAssignable(
                                                assignableDataTypeId,
                                                ArrayUtil.getBoxedType(typedElements))) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "Method argument DataType mismatch");
                                        }
                                      }
                                    }
                                    convertedValue = (UInteger) methodValue;
                                  } catch (UaSerializationException conversionFailure) {
                                    throw new UaException(
                                        conversionFailure.getStatusCode().getValue()
                                                == StatusCodes.Bad_OutOfRange
                                            ? StatusCodes.Bad_OutOfRange
                                            : StatusCodes.Bad_TypeMismatch,
                                        conversionFailure);
                                  } catch (ClassCastException
                                      | IllegalArgumentException conversionFailure) {
                                    throw new UaException(
                                        StatusCodes.Bad_TypeMismatch, conversionFailure);
                                  }
                                }
                                try {
                                  Object wireValue = convertedValue;
                                  Object wireElements =
                                      wireValue instanceof Matrix
                                          ? ((Matrix) wireValue).getElements()
                                          : wireValue;
                                  var numericWireValues = new ArrayDeque<Object[]>();
                                  var numericWirePath =
                                      Collections.newSetFromMap(
                                          new IdentityHashMap<Object, Boolean>());
                                  if (wireValue != null) {
                                    numericWireValues.push(new Object[] {wireValue, false});
                                  }
                                  while (!numericWireValues.isEmpty()) {
                                    Object[] numericWireFrame = numericWireValues.pop();
                                    Object numericWireValue = numericWireFrame[0];
                                    if ((Boolean) numericWireFrame[1]) {
                                      numericWirePath.remove(numericWireValue);
                                      continue;
                                    }
                                    while (numericWireValue instanceof Variant
                                        || numericWireValue instanceof DataValue) {
                                      if (numericWireValue instanceof DataValue) {
                                        if (((DataValue) numericWireValue).getValue() == null) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "A DataValue requires a value wrapper; use"
                                                  + " Variant.NULL_VALUE for null");
                                        }
                                        if (((DataValue) numericWireValue).getStatusCode()
                                            == null) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "A DataValue requires a StatusCode; use"
                                                  + " StatusCode.GOOD for Good");
                                        }
                                        numericWireValue =
                                            ((DataValue) numericWireValue).getValue();
                                      } else {
                                        numericWireValue = ((Variant) numericWireValue).getValue();
                                      }
                                    }
                                    if (numericWireValue instanceof Matrix) {
                                      numericWireValue = ((Matrix) numericWireValue).getElements();
                                    }
                                    if (numericWireValue != null
                                        && numericWireValue.getClass().isArray()) {
                                      if (!numericWirePath.add(numericWireValue)) {
                                        throw new UaException(
                                            StatusCodes.Bad_TypeMismatch,
                                            "Cyclic Variant arrays cannot be encoded");
                                      }
                                      numericWireValues.push(new Object[] {numericWireValue, true});
                                      for (int numericWireIndex = 0;
                                          numericWireIndex < Array.getLength(numericWireValue);
                                          numericWireIndex++) {
                                        Object numericWireElement =
                                            Array.get(numericWireValue, numericWireIndex);
                                        if (numericWireElement == null
                                            && ArrayUtil.getBoxedType(numericWireValue)
                                                == Variant.class) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "A Variant wire array requires a wrapper for every"
                                                  + " element; use Variant.NULL_VALUE for null");
                                        }
                                        if (numericWireElement == null
                                            && (UaEnumeratedType.class.isAssignableFrom(
                                                    ArrayUtil.getBoxedType(numericWireValue))
                                                || OptionSetUInteger.class.isAssignableFrom(
                                                    ArrayUtil.getBoxedType(numericWireValue)))) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "An enum or OptionSet wire array cannot encode a null"
                                                  + " element");
                                        }
                                        if (numericWireElement == null
                                            && ArrayUtil.getBoxedType(numericWireValue)
                                                == Boolean.class) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "A Boolean wire array cannot retain a null element;"
                                                  + " Milo encodes it as false");
                                        }
                                        if (numericWireElement == null
                                            && ArrayUtil.getBoxedType(numericWireValue)
                                                == StatusCode.class) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "A StatusCode wire array cannot retain a null"
                                                  + " element; Milo encodes it as Good");
                                        }
                                        if (numericWireElement == null
                                            && Number.class.isAssignableFrom(
                                                ArrayUtil.getBoxedType(numericWireValue))) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "A numeric wire array cannot retain a null element;"
                                                  + " Milo encodes it as zero");
                                        }
                                        if (numericWireElement instanceof Variant
                                            || numericWireElement instanceof DataValue) {
                                          numericWireValues.push(
                                              new Object[] {numericWireElement, false});
                                        }
                                      }
                                    }
                                  }
                                  wireValue =
                                      ExtensionObject.encodeValue(
                                          this.client.getStaticEncodingContext(), wireValue);
                                  encoded0 = Variant.of(wireValue);
                                } catch (UaSerializationException encodingFailure) {
                                  throw new UaException(
                                      encodingFailure.getStatusCode().getValue()
                                              == StatusCodes.Bad_OutOfRange
                                          ? StatusCodes.Bad_OutOfRange
                                          : StatusCodes.Bad_TypeMismatch,
                                      encodingFailure);
                                } catch (ClassCastException
                                    | IllegalArgumentException encodingFailure) {
                                  throw new UaException(
                                      StatusCodes.Bad_TypeMismatch, encodingFailure);
                                }
                              }
                              inputArguments.add(encoded0);
                            }
                            if (rawInputs.size() > 1) {
                              Variant encoded1;
                              {
                                @Nullable UInteger convertedValue;
                                {
                                  Object methodValue = rawInputs.get(1);
                                  try {
                                    if (methodValue instanceof Matrix
                                        && ((Matrix) methodValue).isNull()) {
                                      methodValue = null;
                                    }
                                    NamespaceTable namespaceTable = this.client.getNamespaceTable();
                                    DataTypeTree dataTypeTree_ = dataTypeTree;
                                    NodeId argumentDataTypeId =
                                        ExpandedNodeId.parse("i=7")
                                            .toNodeId(namespaceTable)
                                            .orElseThrow(
                                                () ->
                                                    new UaException(
                                                        StatusCodes.Bad_NodeIdInvalid,
                                                        "Method argument DataType namespace is"
                                                            + " unavailable"));
                                    if (!OpcUaDataType.isBuiltin(argumentDataTypeId)
                                        && dataTypeTree_.getDataType(argumentDataTypeId) == null) {
                                      throw new UaException(
                                          StatusCodes.Bad_TypeMismatch,
                                          "Method argument LifetimeInHours (effective property"
                                              + " i=12747, DataType i=7) is unavailable in the"
                                              + " effective type tree; resolved DataType: "
                                              + argumentDataTypeId);
                                    }
                                    Object numericElements =
                                        methodValue instanceof Matrix
                                            ? ((Matrix) methodValue).getElements()
                                            : methodValue;
                                    if (numericElements != null
                                        && numericElements.getClass().isArray()
                                        && (numericElements.getClass().getComponentType()
                                                == Number.class
                                            || numericElements.getClass().getComponentType()
                                                == UNumber.class)
                                        && (argumentDataTypeId.equals(NodeIds.Number)
                                            || dataTypeTree_.isSubtypeOf(
                                                argumentDataTypeId, NodeIds.Number))) {
                                      Class<?> numericElementType = null;
                                      for (int numericIndex = 0;
                                          numericIndex < Array.getLength(numericElements);
                                          numericIndex++) {
                                        Object numericElement =
                                            Array.get(numericElements, numericIndex);
                                        if (numericElement != null) {
                                          if (numericElementType != null
                                              && numericElementType != numericElement.getClass()) {
                                            throw new UaException(
                                                StatusCodes.Bad_TypeMismatch,
                                                "An abstract numeric array requires one homogeneous"
                                                    + " wire element type");
                                          }
                                          numericElementType = numericElement.getClass();
                                        }
                                      }
                                      if (numericElementType == null) {
                                        numericElementType =
                                            dataTypeTree_.getBackingClass(argumentDataTypeId);
                                      }
                                      if (numericElementType == Number.class
                                          || numericElementType == UNumber.class) {
                                        throw new UaException(
                                            StatusCodes.Bad_TypeMismatch,
                                            "An empty or all-null abstract numeric array requires a"
                                                + " concretely typed array");
                                      }
                                      Object numericArray =
                                          Array.newInstance(
                                              numericElementType, Array.getLength(numericElements));
                                      for (int numericIndex = 0;
                                          numericIndex < Array.getLength(numericElements);
                                          numericIndex++) {
                                        Array.set(
                                            numericArray,
                                            numericIndex,
                                            Array.get(numericElements, numericIndex));
                                      }
                                      if (methodValue instanceof Matrix) {
                                        methodValue =
                                            new Matrix(
                                                numericArray,
                                                ((Matrix) methodValue).getDimensions().clone(),
                                                ((Matrix) methodValue)
                                                    .getDataType()
                                                    .orElseThrow(
                                                        () ->
                                                            new UaException(
                                                                StatusCodes.Bad_TypeMismatch,
                                                                "A numeric Matrix requires an"
                                                                    + " explicit wire DataType")),
                                                ((Matrix) methodValue)
                                                    .getDataTypeId()
                                                    .orElse(null));
                                      } else {
                                        methodValue = numericArray;
                                      }
                                    }
                                    if (methodValue != null) {
                                      Object shapeElements =
                                          methodValue instanceof Matrix
                                              ? ((Matrix) methodValue).getElements()
                                              : methodValue;
                                      int valueRank =
                                          methodValue instanceof Matrix
                                              ? ((Matrix) methodValue).getValueRank()
                                              : ArrayUtil.getValueRank(methodValue);
                                      boolean emptyArray =
                                          methodValue.getClass().isArray()
                                              && ArrayUtil.getValueRank(methodValue) == 1
                                              && Array.getLength(methodValue) == 0;
                                      if (!(valueRank == -1)) {
                                        throw new UaException(
                                            StatusCodes.Bad_TypeMismatch,
                                            "Method argument ValueRank mismatch");
                                      }
                                      if (methodValue instanceof Matrix) {
                                        int[] dimensions = ((Matrix) methodValue).getDimensions();
                                        if (dimensions.length < 2
                                            || !shapeElements.getClass().isArray()
                                            || ArrayUtil.getValueRank(shapeElements) != 1) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "Malformed Method Matrix representation");
                                        }
                                        long elementCount = 1;
                                        for (int dimension : dimensions) {
                                          if (dimension < 0 || elementCount > Integer.MAX_VALUE) {
                                            throw new UaException(
                                                StatusCodes.Bad_TypeMismatch,
                                                "Malformed Method Matrix dimensions");
                                          }
                                          elementCount *= dimension;
                                        }
                                        if (elementCount != Array.getLength(shapeElements)) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "Method Matrix dimensions do not match its elements");
                                        }
                                        if (!(((Matrix) methodValue)
                                            .getDataType()
                                            .equals(Variant.of(shapeElements).getDataType()))) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "Method Matrix DataType does not match its elements");
                                        }
                                      }
                                      Variant.of(shapeElements);
                                    }
                                    if (methodValue != null) {
                                      Object typedElements =
                                          methodValue instanceof Matrix
                                              ? ((Matrix) methodValue).getElements()
                                              : methodValue;
                                      if (NodeIds.Structure.equals(argumentDataTypeId)
                                          || dataTypeTree_.isStructType(argumentDataTypeId)) {
                                        var declaredType =
                                            dataTypeTree_.getType(argumentDataTypeId);
                                        if (typedElements.getClass().isArray()) {
                                          var structureCodec =
                                              this.client
                                                  .getStaticEncodingContext()
                                                  .getDataTypeManager()
                                                  .getCodec(argumentDataTypeId);
                                          Class<?> structureClass =
                                              structureCodec == null
                                                  ? UaStructuredType.class
                                                  : structureCodec.getType();
                                          Object decodedStructures =
                                              Array.newInstance(
                                                  structureClass, Array.getLength(typedElements));
                                          for (int structureIndex = 0;
                                              structureIndex < Array.getLength(typedElements);
                                              structureIndex++) {
                                            Object structure =
                                                Array.get(typedElements, structureIndex);
                                            if (structure instanceof ExtensionObject) {
                                              structure =
                                                  ((ExtensionObject) structure).isNull()
                                                      ? null
                                                      : ((ExtensionObject) structure)
                                                          .decode(
                                                              this.client
                                                                  .getStaticEncodingContext());
                                            }
                                            if (structure != null) {
                                              if (!(structure instanceof UaStructuredType)) {
                                                throw new UaException(
                                                    StatusCodes.Bad_TypeMismatch,
                                                    "Method argument requires a Structure value");
                                              }
                                              if (NodeIds.Structure.equals(argumentDataTypeId)
                                                  || declaredType != null
                                                      && declaredType.isAbstract()) {
                                                if (!dataTypeTree_.isSubtypeOf(
                                                    ((UaStructuredType) structure)
                                                        .getTypeId()
                                                        .toNodeId(namespaceTable)
                                                        .orElse(NodeId.NULL_VALUE),
                                                    argumentDataTypeId)) {
                                                  throw new UaException(
                                                      StatusCodes.Bad_TypeMismatch,
                                                      "Method Structure is not a subtype of the"
                                                          + " effective DataType");
                                                }
                                              } else {
                                                if (!argumentDataTypeId.equals(
                                                    ((UaStructuredType) structure)
                                                        .getTypeId()
                                                        .toNodeId(namespaceTable)
                                                        .orElse(NodeId.NULL_VALUE))) {
                                                  throw new UaException(
                                                      StatusCodes.Bad_TypeMismatch,
                                                      "Method Structure does not match the"
                                                          + " effective DataType");
                                                }
                                              }
                                            }
                                            Array.set(decodedStructures, structureIndex, structure);
                                          }
                                          methodValue =
                                              methodValue instanceof Matrix
                                                  ? new Matrix(
                                                      decodedStructures,
                                                      ((Matrix) methodValue)
                                                          .getDimensions()
                                                          .clone())
                                                  : decodedStructures;
                                        } else {
                                          if (typedElements instanceof ExtensionObject) {
                                            typedElements =
                                                ((ExtensionObject) typedElements).isNull()
                                                    ? null
                                                    : ((ExtensionObject) typedElements)
                                                        .decode(
                                                            this.client.getStaticEncodingContext());
                                          }
                                          if (typedElements != null) {
                                            if (!(typedElements instanceof UaStructuredType)) {
                                              throw new UaException(
                                                  StatusCodes.Bad_TypeMismatch,
                                                  "Method argument requires a Structure value");
                                            }
                                            if (NodeIds.Structure.equals(argumentDataTypeId)
                                                || declaredType != null
                                                    && declaredType.isAbstract()) {
                                              if (!dataTypeTree_.isSubtypeOf(
                                                  ((UaStructuredType) typedElements)
                                                      .getTypeId()
                                                      .toNodeId(namespaceTable)
                                                      .orElse(NodeId.NULL_VALUE),
                                                  argumentDataTypeId)) {
                                                throw new UaException(
                                                    StatusCodes.Bad_TypeMismatch,
                                                    "Method Structure is not a subtype of the"
                                                        + " effective DataType");
                                              }
                                            } else {
                                              if (!argumentDataTypeId.equals(
                                                  ((UaStructuredType) typedElements)
                                                      .getTypeId()
                                                      .toNodeId(namespaceTable)
                                                      .orElse(NodeId.NULL_VALUE))) {
                                                throw new UaException(
                                                    StatusCodes.Bad_TypeMismatch,
                                                    "Method Structure does not match the effective"
                                                        + " DataType");
                                              }
                                            }
                                          }
                                          methodValue = typedElements;
                                        }
                                      } else {
                                        Variant.of(typedElements);
                                        NodeId assignableDataTypeId =
                                            dataTypeTree_.getBackingClass(argumentDataTypeId)
                                                        == Number.class
                                                    && dataTypeTree_.isSubtypeOf(
                                                        argumentDataTypeId, NodeIds.Integer)
                                                ? NodeIds.Integer
                                                : argumentDataTypeId;
                                        if (dataTypeTree_.getBackingClass(argumentDataTypeId)
                                                != Variant.class
                                            && !dataTypeTree_.isAssignable(
                                                assignableDataTypeId,
                                                ArrayUtil.getBoxedType(typedElements))) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "Method argument DataType mismatch");
                                        }
                                      }
                                    }
                                    convertedValue = (UInteger) methodValue;
                                  } catch (UaSerializationException conversionFailure) {
                                    throw new UaException(
                                        conversionFailure.getStatusCode().getValue()
                                                == StatusCodes.Bad_OutOfRange
                                            ? StatusCodes.Bad_OutOfRange
                                            : StatusCodes.Bad_TypeMismatch,
                                        conversionFailure);
                                  } catch (ClassCastException
                                      | IllegalArgumentException conversionFailure) {
                                    throw new UaException(
                                        StatusCodes.Bad_TypeMismatch, conversionFailure);
                                  }
                                }
                                try {
                                  Object wireValue = convertedValue;
                                  Object wireElements =
                                      wireValue instanceof Matrix
                                          ? ((Matrix) wireValue).getElements()
                                          : wireValue;
                                  var numericWireValues = new ArrayDeque<Object[]>();
                                  var numericWirePath =
                                      Collections.newSetFromMap(
                                          new IdentityHashMap<Object, Boolean>());
                                  if (wireValue != null) {
                                    numericWireValues.push(new Object[] {wireValue, false});
                                  }
                                  while (!numericWireValues.isEmpty()) {
                                    Object[] numericWireFrame = numericWireValues.pop();
                                    Object numericWireValue = numericWireFrame[0];
                                    if ((Boolean) numericWireFrame[1]) {
                                      numericWirePath.remove(numericWireValue);
                                      continue;
                                    }
                                    while (numericWireValue instanceof Variant
                                        || numericWireValue instanceof DataValue) {
                                      if (numericWireValue instanceof DataValue) {
                                        if (((DataValue) numericWireValue).getValue() == null) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "A DataValue requires a value wrapper; use"
                                                  + " Variant.NULL_VALUE for null");
                                        }
                                        if (((DataValue) numericWireValue).getStatusCode()
                                            == null) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "A DataValue requires a StatusCode; use"
                                                  + " StatusCode.GOOD for Good");
                                        }
                                        numericWireValue =
                                            ((DataValue) numericWireValue).getValue();
                                      } else {
                                        numericWireValue = ((Variant) numericWireValue).getValue();
                                      }
                                    }
                                    if (numericWireValue instanceof Matrix) {
                                      numericWireValue = ((Matrix) numericWireValue).getElements();
                                    }
                                    if (numericWireValue != null
                                        && numericWireValue.getClass().isArray()) {
                                      if (!numericWirePath.add(numericWireValue)) {
                                        throw new UaException(
                                            StatusCodes.Bad_TypeMismatch,
                                            "Cyclic Variant arrays cannot be encoded");
                                      }
                                      numericWireValues.push(new Object[] {numericWireValue, true});
                                      for (int numericWireIndex = 0;
                                          numericWireIndex < Array.getLength(numericWireValue);
                                          numericWireIndex++) {
                                        Object numericWireElement =
                                            Array.get(numericWireValue, numericWireIndex);
                                        if (numericWireElement == null
                                            && ArrayUtil.getBoxedType(numericWireValue)
                                                == Variant.class) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "A Variant wire array requires a wrapper for every"
                                                  + " element; use Variant.NULL_VALUE for null");
                                        }
                                        if (numericWireElement == null
                                            && (UaEnumeratedType.class.isAssignableFrom(
                                                    ArrayUtil.getBoxedType(numericWireValue))
                                                || OptionSetUInteger.class.isAssignableFrom(
                                                    ArrayUtil.getBoxedType(numericWireValue)))) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "An enum or OptionSet wire array cannot encode a null"
                                                  + " element");
                                        }
                                        if (numericWireElement == null
                                            && ArrayUtil.getBoxedType(numericWireValue)
                                                == Boolean.class) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "A Boolean wire array cannot retain a null element;"
                                                  + " Milo encodes it as false");
                                        }
                                        if (numericWireElement == null
                                            && ArrayUtil.getBoxedType(numericWireValue)
                                                == StatusCode.class) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "A StatusCode wire array cannot retain a null"
                                                  + " element; Milo encodes it as Good");
                                        }
                                        if (numericWireElement == null
                                            && Number.class.isAssignableFrom(
                                                ArrayUtil.getBoxedType(numericWireValue))) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "A numeric wire array cannot retain a null element;"
                                                  + " Milo encodes it as zero");
                                        }
                                        if (numericWireElement instanceof Variant
                                            || numericWireElement instanceof DataValue) {
                                          numericWireValues.push(
                                              new Object[] {numericWireElement, false});
                                        }
                                      }
                                    }
                                  }
                                  wireValue =
                                      ExtensionObject.encodeValue(
                                          this.client.getStaticEncodingContext(), wireValue);
                                  encoded1 = Variant.of(wireValue);
                                } catch (UaSerializationException encodingFailure) {
                                  throw new UaException(
                                      encodingFailure.getStatusCode().getValue()
                                              == StatusCodes.Bad_OutOfRange
                                          ? StatusCodes.Bad_OutOfRange
                                          : StatusCodes.Bad_TypeMismatch,
                                      encodingFailure);
                                } catch (ClassCastException
                                    | IllegalArgumentException encodingFailure) {
                                  throw new UaException(
                                      StatusCodes.Bad_TypeMismatch, encodingFailure);
                                }
                              }
                              inputArguments.add(encoded1);
                            }
                            CallMethodRequest request =
                                new CallMethodRequest(
                                    getNodeId(),
                                    methodNode.getNodeId(),
                                    inputArguments.toArray(new Variant[0]));
                            return this.client
                                .getSessionAsync()
                                .thenCompose(
                                    session -> {
                                      if (result.isCancelled()) {
                                        return CompletableFuture.failedFuture(
                                            new CancellationException());
                                      }
                                      RequestHeader originalHeader =
                                          this.client.newRequestHeader(
                                              session.getAuthenticationToken());
                                      RequestHeader requestHeader =
                                          new RequestHeader(
                                              originalHeader.getAuthenticationToken(),
                                              originalHeader.getTimestamp(),
                                              originalHeader.getRequestHandle(),
                                              options.returnDiagnostics(),
                                              originalHeader.getAuditEntryId(),
                                              originalHeader.getTimeoutHint(),
                                              originalHeader.getAdditionalHeader());
                                      var call =
                                          this.client.sendRequestAsync(
                                              new CallRequest(
                                                  requestHeader,
                                                  new CallMethodRequest[] {request}));
                                      result.whenComplete(
                                          (value, failure) -> {
                                            if (result.isCancelled()) {
                                              call.cancel(false);
                                            }
                                          });
                                      return call.thenCompose(
                                          message -> {
                                            if (result.isCancelled()) {
                                              throw new CancellationException();
                                            }
                                            try {
                                              if (!(message instanceof CallResponse response)
                                                  || response.getResponseHeader() == null
                                                  || response.getResponseHeader().getServiceResult()
                                                      == null) {
                                                throw new UaException(
                                                    StatusCodes.Bad_UnexpectedError,
                                                    "Malformed Call response header");
                                              }
                                              if (!response
                                                  .getResponseHeader()
                                                  .getServiceResult()
                                                  .isGood()) {
                                                throw new UaException(
                                                    response
                                                        .getResponseHeader()
                                                        .getServiceResult());
                                              }
                                              if (response.getResults() == null
                                                  || response.getResults().length != 1
                                                  || response.getResults()[0] == null
                                                  || response.getResults()[0].getStatusCode()
                                                      == null) {
                                                throw new UaException(
                                                    StatusCodes.Bad_UnexpectedError,
                                                    "Expected exactly one Call operation result");
                                              }
                                              CompletableFuture<DataTypeTree> outputMetadata =
                                                  response.getResults()[0].getStatusCode().isBad()
                                                      ? CompletableFuture.completedFuture(
                                                          dataTypeTree)
                                                      : ClientDataTypes.read(
                                                          this.client,
                                                          List.<ExpandedNodeId>of(
                                                              ExpandedNodeId.parse("i=7")),
                                                          (Object)
                                                              response.getResults()[0]
                                                                  .getOutputArguments());
                                              result.whenComplete(
                                                  (cancelledValue, cancelledFailure) -> {
                                                    if (result.isCancelled()) {
                                                      outputMetadata.cancel(false);
                                                    }
                                                  });
                                              return outputMetadata.handle(
                                                  (outputDataTypeTree, metadataFailure) -> {
                                                    if (result.isCancelled()) {
                                                      throw new CancellationException();
                                                    }
                                                    try {
                                                      return MethodCallResult.decode(
                                                          request,
                                                          requestHeader,
                                                          response,
                                                          0,
                                                          outputArguments -> {
                                                            if (metadataFailure != null) {
                                                              var
                                                                  checkedMetadataFailuremetadataFailure =
                                                                      UaException.extract(
                                                                          metadataFailure);
                                                              if (checkedMetadataFailuremetadataFailure
                                                                  .isPresent()) {
                                                                throw checkedMetadataFailuremetadataFailure
                                                                    .orElseThrow();
                                                              }
                                                              Throwable
                                                                  metadataCausemetadataFailure =
                                                                      metadataFailure;
                                                              while (metadataCausemetadataFailure
                                                                  != null) {
                                                                if (metadataCausemetadataFailure
                                                                    instanceof
                                                                    UaSerializationException
                                                                        metadataCodecmetadataFailure) {
                                                                  throw new UaException(
                                                                      metadataCodecmetadataFailure
                                                                                  .getStatusCode()
                                                                                  .getValue()
                                                                              == StatusCodes
                                                                                  .Bad_OutOfRange
                                                                          ? StatusCodes
                                                                              .Bad_OutOfRange
                                                                          : StatusCodes
                                                                              .Bad_TypeMismatch,
                                                                      metadataCodecmetadataFailure);
                                                                }
                                                                metadataCausemetadataFailure =
                                                                    metadataCausemetadataFailure
                                                                        .getCause();
                                                              }
                                                              throw UaException.extract(
                                                                      metadataFailure)
                                                                  .orElseGet(
                                                                      () ->
                                                                          new UaException(
                                                                              metadataFailure));
                                                            }
                                                            if ((outputArguments == null
                                                                    ? 0
                                                                    : outputArguments.length)
                                                                != 1) {
                                                              throw new UaException(
                                                                  StatusCodes.Bad_TypeMismatch,
                                                                  "Unexpected Method output count");
                                                            }
                                                            if (outputArguments[0] == null) {
                                                              throw new UaException(
                                                                  StatusCodes.Bad_TypeMismatch,
                                                                  "Null Method output Variant");
                                                            }
                                                            @Nullable UInteger decoded0;
                                                            {
                                                              Object methodValue =
                                                                  outputArguments[0].getValue();
                                                              try {
                                                                if (methodValue instanceof Matrix
                                                                    && ((Matrix) methodValue)
                                                                        .isNull()) {
                                                                  methodValue = null;
                                                                }
                                                                NamespaceTable namespaceTable =
                                                                    this.client.getNamespaceTable();
                                                                DataTypeTree dataTypeTree_ =
                                                                    outputDataTypeTree;
                                                                NodeId argumentDataTypeId =
                                                                    ExpandedNodeId.parse("i=7")
                                                                        .toNodeId(namespaceTable)
                                                                        .orElseThrow(
                                                                            () ->
                                                                                new UaException(
                                                                                    StatusCodes
                                                                                        .Bad_NodeIdInvalid,
                                                                                    "Method"
                                                                                        + " argument"
                                                                                        + " DataType"
                                                                                        + " namespace"
                                                                                        + " is unavailable"));
                                                                if (!OpcUaDataType.isBuiltin(
                                                                        argumentDataTypeId)
                                                                    && dataTypeTree_.getDataType(
                                                                            argumentDataTypeId)
                                                                        == null) {
                                                                  throw new UaException(
                                                                      StatusCodes.Bad_TypeMismatch,
                                                                      "Method argument"
                                                                          + " RevisedLifetimeInHours"
                                                                          + " (effective property"
                                                                          + " i=12748, DataType"
                                                                          + " i=7) is unavailable"
                                                                          + " in the effective type"
                                                                          + " tree; resolved"
                                                                          + " DataType: "
                                                                          + argumentDataTypeId);
                                                                }
                                                                Object numericElements =
                                                                    methodValue instanceof Matrix
                                                                        ? ((Matrix) methodValue)
                                                                            .getElements()
                                                                        : methodValue;
                                                                if (numericElements != null
                                                                    && numericElements
                                                                        .getClass()
                                                                        .isArray()
                                                                    && (numericElements
                                                                                .getClass()
                                                                                .getComponentType()
                                                                            == Number.class
                                                                        || numericElements
                                                                                .getClass()
                                                                                .getComponentType()
                                                                            == UNumber.class)
                                                                    && (argumentDataTypeId.equals(
                                                                            NodeIds.Number)
                                                                        || dataTypeTree_
                                                                            .isSubtypeOf(
                                                                                argumentDataTypeId,
                                                                                NodeIds.Number))) {
                                                                  Class<?> numericElementType =
                                                                      null;
                                                                  for (int numericIndex = 0;
                                                                      numericIndex
                                                                          < Array.getLength(
                                                                              numericElements);
                                                                      numericIndex++) {
                                                                    Object numericElement =
                                                                        Array.get(
                                                                            numericElements,
                                                                            numericIndex);
                                                                    if (numericElement != null) {
                                                                      if (numericElementType != null
                                                                          && numericElementType
                                                                              != numericElement
                                                                                  .getClass()) {
                                                                        throw new UaException(
                                                                            StatusCodes
                                                                                .Bad_TypeMismatch,
                                                                            "An abstract numeric"
                                                                                + " array requires"
                                                                                + " one homogeneous"
                                                                                + " wire element"
                                                                                + " type");
                                                                      }
                                                                      numericElementType =
                                                                          numericElement.getClass();
                                                                    }
                                                                  }
                                                                  if (numericElementType == null) {
                                                                    numericElementType =
                                                                        dataTypeTree_
                                                                            .getBackingClass(
                                                                                argumentDataTypeId);
                                                                  }
                                                                  if (numericElementType
                                                                          == Number.class
                                                                      || numericElementType
                                                                          == UNumber.class) {
                                                                    throw new UaException(
                                                                        StatusCodes
                                                                            .Bad_TypeMismatch,
                                                                        "An empty or all-null"
                                                                            + " abstract numeric"
                                                                            + " array requires a"
                                                                            + " concretely typed"
                                                                            + " array");
                                                                  }
                                                                  Object numericArray =
                                                                      Array.newInstance(
                                                                          numericElementType,
                                                                          Array.getLength(
                                                                              numericElements));
                                                                  for (int numericIndex = 0;
                                                                      numericIndex
                                                                          < Array.getLength(
                                                                              numericElements);
                                                                      numericIndex++) {
                                                                    Array.set(
                                                                        numericArray,
                                                                        numericIndex,
                                                                        Array.get(
                                                                            numericElements,
                                                                            numericIndex));
                                                                  }
                                                                  if (methodValue
                                                                      instanceof Matrix) {
                                                                    methodValue =
                                                                        new Matrix(
                                                                            numericArray,
                                                                            ((Matrix) methodValue)
                                                                                .getDimensions()
                                                                                .clone(),
                                                                            ((Matrix) methodValue)
                                                                                .getDataType()
                                                                                .orElseThrow(
                                                                                    () ->
                                                                                        new UaException(
                                                                                            StatusCodes
                                                                                                .Bad_TypeMismatch,
                                                                                            "A numeric"
                                                                                                + " Matrix"
                                                                                                + " requires"
                                                                                                + " an explicit"
                                                                                                + " wire"
                                                                                                + " DataType")),
                                                                            ((Matrix) methodValue)
                                                                                .getDataTypeId()
                                                                                .orElse(null));
                                                                  } else {
                                                                    methodValue = numericArray;
                                                                  }
                                                                }
                                                                if (methodValue != null) {
                                                                  Object shapeElements =
                                                                      methodValue instanceof Matrix
                                                                          ? ((Matrix) methodValue)
                                                                              .getElements()
                                                                          : methodValue;
                                                                  int valueRank =
                                                                      methodValue instanceof Matrix
                                                                          ? ((Matrix) methodValue)
                                                                              .getValueRank()
                                                                          : ArrayUtil.getValueRank(
                                                                              methodValue);
                                                                  boolean emptyArray =
                                                                      methodValue
                                                                              .getClass()
                                                                              .isArray()
                                                                          && ArrayUtil.getValueRank(
                                                                                  methodValue)
                                                                              == 1
                                                                          && Array.getLength(
                                                                                  methodValue)
                                                                              == 0;
                                                                  if (!(valueRank == -1)) {
                                                                    throw new UaException(
                                                                        StatusCodes
                                                                            .Bad_TypeMismatch,
                                                                        "Method argument ValueRank"
                                                                            + " mismatch");
                                                                  }
                                                                  if (methodValue
                                                                      instanceof Matrix) {
                                                                    int[] dimensions =
                                                                        ((Matrix) methodValue)
                                                                            .getDimensions();
                                                                    if (dimensions.length < 2
                                                                        || !shapeElements
                                                                            .getClass()
                                                                            .isArray()
                                                                        || ArrayUtil.getValueRank(
                                                                                shapeElements)
                                                                            != 1) {
                                                                      throw new UaException(
                                                                          StatusCodes
                                                                              .Bad_TypeMismatch,
                                                                          "Malformed Method Matrix"
                                                                              + " representation");
                                                                    }
                                                                    long elementCount = 1;
                                                                    for (int dimension :
                                                                        dimensions) {
                                                                      if (dimension < 0
                                                                          || elementCount
                                                                              > Integer.MAX_VALUE) {
                                                                        throw new UaException(
                                                                            StatusCodes
                                                                                .Bad_TypeMismatch,
                                                                            "Malformed Method"
                                                                                + " Matrix"
                                                                                + " dimensions");
                                                                      }
                                                                      elementCount *= dimension;
                                                                    }
                                                                    if (elementCount
                                                                        != Array.getLength(
                                                                            shapeElements)) {
                                                                      throw new UaException(
                                                                          StatusCodes
                                                                              .Bad_TypeMismatch,
                                                                          "Method Matrix dimensions"
                                                                              + " do not match its"
                                                                              + " elements");
                                                                    }
                                                                    if (!(((Matrix) methodValue)
                                                                        .getDataType()
                                                                        .equals(
                                                                            Variant.of(
                                                                                    shapeElements)
                                                                                .getDataType()))) {
                                                                      throw new UaException(
                                                                          StatusCodes
                                                                              .Bad_TypeMismatch,
                                                                          "Method Matrix DataType"
                                                                              + " does not match"
                                                                              + " its elements");
                                                                    }
                                                                  }
                                                                  Variant.of(shapeElements);
                                                                }
                                                                if (methodValue != null) {
                                                                  Object typedElements =
                                                                      methodValue instanceof Matrix
                                                                          ? ((Matrix) methodValue)
                                                                              .getElements()
                                                                          : methodValue;
                                                                  if (NodeIds.Structure.equals(
                                                                          argumentDataTypeId)
                                                                      || dataTypeTree_.isStructType(
                                                                          argumentDataTypeId)) {
                                                                    var declaredType =
                                                                        dataTypeTree_.getType(
                                                                            argumentDataTypeId);
                                                                    if (typedElements
                                                                        .getClass()
                                                                        .isArray()) {
                                                                      var structureCodec =
                                                                          this.client
                                                                              .getStaticEncodingContext()
                                                                              .getDataTypeManager()
                                                                              .getCodec(
                                                                                  argumentDataTypeId);
                                                                      Class<?> structureClass =
                                                                          structureCodec == null
                                                                              ? UaStructuredType
                                                                                  .class
                                                                              : structureCodec
                                                                                  .getType();
                                                                      Object decodedStructures =
                                                                          Array.newInstance(
                                                                              structureClass,
                                                                              Array.getLength(
                                                                                  typedElements));
                                                                      for (int structureIndex = 0;
                                                                          structureIndex
                                                                              < Array.getLength(
                                                                                  typedElements);
                                                                          structureIndex++) {
                                                                        Object structure =
                                                                            Array.get(
                                                                                typedElements,
                                                                                structureIndex);
                                                                        if (structure
                                                                            instanceof
                                                                            ExtensionObject) {
                                                                          structure =
                                                                              ((ExtensionObject)
                                                                                          structure)
                                                                                      .isNull()
                                                                                  ? null
                                                                                  : ((ExtensionObject)
                                                                                          structure)
                                                                                      .decode(
                                                                                          this
                                                                                              .client
                                                                                              .getStaticEncodingContext());
                                                                        }
                                                                        if (structure != null) {
                                                                          if (!(structure
                                                                              instanceof
                                                                              UaStructuredType)) {
                                                                            throw new UaException(
                                                                                StatusCodes
                                                                                    .Bad_TypeMismatch,
                                                                                "Method argument"
                                                                                    + " requires a"
                                                                                    + " Structure"
                                                                                    + " value");
                                                                          }
                                                                          if (NodeIds.Structure
                                                                                  .equals(
                                                                                      argumentDataTypeId)
                                                                              || declaredType
                                                                                      != null
                                                                                  && declaredType
                                                                                      .isAbstract()) {
                                                                            if (!dataTypeTree_
                                                                                .isSubtypeOf(
                                                                                    ((UaStructuredType)
                                                                                            structure)
                                                                                        .getTypeId()
                                                                                        .toNodeId(
                                                                                            namespaceTable)
                                                                                        .orElse(
                                                                                            NodeId
                                                                                                .NULL_VALUE),
                                                                                    argumentDataTypeId)) {
                                                                              throw new UaException(
                                                                                  StatusCodes
                                                                                      .Bad_TypeMismatch,
                                                                                  "Method Structure"
                                                                                      + " is not a"
                                                                                      + " subtype"
                                                                                      + " of the"
                                                                                      + " effective"
                                                                                      + " DataType");
                                                                            }
                                                                          } else {
                                                                            if (!argumentDataTypeId
                                                                                .equals(
                                                                                    ((UaStructuredType)
                                                                                            structure)
                                                                                        .getTypeId()
                                                                                        .toNodeId(
                                                                                            namespaceTable)
                                                                                        .orElse(
                                                                                            NodeId
                                                                                                .NULL_VALUE))) {
                                                                              throw new UaException(
                                                                                  StatusCodes
                                                                                      .Bad_TypeMismatch,
                                                                                  "Method Structure"
                                                                                      + " does not"
                                                                                      + " match the"
                                                                                      + " effective"
                                                                                      + " DataType");
                                                                            }
                                                                          }
                                                                        }
                                                                        Array.set(
                                                                            decodedStructures,
                                                                            structureIndex,
                                                                            structure);
                                                                      }
                                                                      methodValue =
                                                                          methodValue
                                                                                  instanceof Matrix
                                                                              ? new Matrix(
                                                                                  decodedStructures,
                                                                                  ((Matrix)
                                                                                          methodValue)
                                                                                      .getDimensions()
                                                                                      .clone())
                                                                              : decodedStructures;
                                                                    } else {
                                                                      if (typedElements
                                                                          instanceof
                                                                          ExtensionObject) {
                                                                        typedElements =
                                                                            ((ExtensionObject)
                                                                                        typedElements)
                                                                                    .isNull()
                                                                                ? null
                                                                                : ((ExtensionObject)
                                                                                        typedElements)
                                                                                    .decode(
                                                                                        this.client
                                                                                            .getStaticEncodingContext());
                                                                      }
                                                                      if (typedElements != null) {
                                                                        if (!(typedElements
                                                                            instanceof
                                                                            UaStructuredType)) {
                                                                          throw new UaException(
                                                                              StatusCodes
                                                                                  .Bad_TypeMismatch,
                                                                              "Method argument"
                                                                                  + " requires a"
                                                                                  + " Structure"
                                                                                  + " value");
                                                                        }
                                                                        if (NodeIds.Structure
                                                                                .equals(
                                                                                    argumentDataTypeId)
                                                                            || declaredType != null
                                                                                && declaredType
                                                                                    .isAbstract()) {
                                                                          if (!dataTypeTree_
                                                                              .isSubtypeOf(
                                                                                  ((UaStructuredType)
                                                                                          typedElements)
                                                                                      .getTypeId()
                                                                                      .toNodeId(
                                                                                          namespaceTable)
                                                                                      .orElse(
                                                                                          NodeId
                                                                                              .NULL_VALUE),
                                                                                  argumentDataTypeId)) {
                                                                            throw new UaException(
                                                                                StatusCodes
                                                                                    .Bad_TypeMismatch,
                                                                                "Method Structure"
                                                                                    + " is not a"
                                                                                    + " subtype of"
                                                                                    + " the effective"
                                                                                    + " DataType");
                                                                          }
                                                                        } else {
                                                                          if (!argumentDataTypeId
                                                                              .equals(
                                                                                  ((UaStructuredType)
                                                                                          typedElements)
                                                                                      .getTypeId()
                                                                                      .toNodeId(
                                                                                          namespaceTable)
                                                                                      .orElse(
                                                                                          NodeId
                                                                                              .NULL_VALUE))) {
                                                                            throw new UaException(
                                                                                StatusCodes
                                                                                    .Bad_TypeMismatch,
                                                                                "Method Structure"
                                                                                    + " does not"
                                                                                    + " match the"
                                                                                    + " effective"
                                                                                    + " DataType");
                                                                          }
                                                                        }
                                                                      }
                                                                      methodValue = typedElements;
                                                                    }
                                                                  } else {
                                                                    Variant.of(typedElements);
                                                                    NodeId assignableDataTypeId =
                                                                        dataTypeTree_
                                                                                        .getBackingClass(
                                                                                            argumentDataTypeId)
                                                                                    == Number.class
                                                                                && dataTypeTree_
                                                                                    .isSubtypeOf(
                                                                                        argumentDataTypeId,
                                                                                        NodeIds
                                                                                            .Integer)
                                                                            ? NodeIds.Integer
                                                                            : argumentDataTypeId;
                                                                    if (dataTypeTree_
                                                                                .getBackingClass(
                                                                                    argumentDataTypeId)
                                                                            != Variant.class
                                                                        && !dataTypeTree_
                                                                            .isAssignable(
                                                                                assignableDataTypeId,
                                                                                ArrayUtil
                                                                                    .getBoxedType(
                                                                                        typedElements))) {
                                                                      throw new UaException(
                                                                          StatusCodes
                                                                              .Bad_TypeMismatch,
                                                                          "Method argument DataType"
                                                                              + " mismatch");
                                                                    }
                                                                  }
                                                                }
                                                                decoded0 = (UInteger) methodValue;
                                                              } catch (
                                                                  UaSerializationException
                                                                      conversionFailure) {
                                                                throw new UaException(
                                                                    conversionFailure
                                                                                .getStatusCode()
                                                                                .getValue()
                                                                            == StatusCodes
                                                                                .Bad_OutOfRange
                                                                        ? StatusCodes.Bad_OutOfRange
                                                                        : StatusCodes
                                                                            .Bad_TypeMismatch,
                                                                    conversionFailure);
                                                              } catch (ClassCastException
                                                                  | IllegalArgumentException
                                                                      conversionFailure) {
                                                                throw new UaException(
                                                                    StatusCodes.Bad_TypeMismatch,
                                                                    conversionFailure);
                                                              }
                                                            }
                                                            return decoded0;
                                                          });
                                                    } catch (UaException failure) {
                                                      throw new CompletionException(failure);
                                                    }
                                                  });
                                            } catch (Exception failure) {
                                              return CompletableFuture.failedFuture(failure);
                                            }
                                          });
                                    });
                          } catch (Exception failure) {
                            return CompletableFuture.failedFuture(failure);
                          }
                        });
              });
      pipeline.whenComplete(
          (value, failure) -> {
            if (failure == null) {
              result.complete(value);
            } else {
              result.completeExceptionally(failure);
            }
          });
    } catch (RuntimeException failure) {
      result.completeExceptionally(failure);
    }
    return result;
  }

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part5/9.4
   *
   * <p>Resolves the optional member by its namespace-qualified path. Returns null only for
   * confirmed absence. Resolution does not create a UA node. It can perform service I/O and
   * construct or reuse a Java wrapper in Milo's address space cache. A reference can change after
   * lookup.
   *
   * @return the existing member, or null for confirmed absence
   * @throws org.eclipse.milo.opcua.stack.core.UaException if a required node is absent, resolution
   *     fails, or a checked conversion fails
   */
  @NullMarked
  @Override
  public @Nullable UaMethodNode getRequestServerStateChangeMethodNode() throws UaException {
    try {
      return getRequestServerStateChangeMethodNodeAsync().get();
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

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part5/9.4
   *
   * <p>Resolves the optional member by its namespace-qualified path. Returns null only for
   * confirmed absence. Resolution does not create a UA node. It can perform service I/O and
   * construct or reuse a Java wrapper in Milo's address space cache. A reference can change after
   * lookup.
   *
   * <p>Lookup, conversion and service failures complete the future exceptionally. UaException
   * causes preserve OPC UA status. Incompatible plain payload casts can complete exceptionally with
   * ClassCastException. Cancellation does not promise transport cancellation or rollback.
   *
   * @return a nonnull future completing with the existing member, or null for confirmed absence
   */
  @NullMarked
  @Override
  public CompletableFuture<? extends @Nullable UaMethodNode>
      getRequestServerStateChangeMethodNodeAsync() {
    CompletableFuture<UaMethodNode> result = new CompletableFuture<>();
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
                                "http://opcfoundation.org/UA/:RequestServerStateChange (declaration"
                                    + " i=12883, owner i=2004) on "
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
                                            namespaceIndex, "RequestServerStateChange"))
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
                                          "http://opcfoundation.org/UA/:RequestServerStateChange"
                                              + " (declaration i=12883, owner i=2004) on "
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
                                          "http://opcfoundation.org/UA/:RequestServerStateChange"
                                              + " (declaration i=12883, owner i=2004)"));
                                }
                                var targets = operation.getTargets();
                                if (targets == null || targets.length == 0) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_UnexpectedError,
                                          "http://opcfoundation.org/UA/:RequestServerStateChange"
                                              + " (declaration i=12883, owner i=2004) on "
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
                                            "http://opcfoundation.org/UA/:RequestServerStateChange"
                                                + " (declaration i=12883, owner i=2004) on "
                                                + getNodeId()));
                                  }
                                  if (!target.getTargetId().isLocal()) {
                                    return CompletableFuture.failedFuture(
                                        new UaException(
                                            StatusCodes.Bad_NotSupported,
                                            "http://opcfoundation.org/UA/:RequestServerStateChange"
                                                + " (declaration i=12883, owner i=2004) on "
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
                                                            "http://opcfoundation.org/UA/:RequestServerStateChange"
                                                                + " (declaration i=12883, owner"
                                                                + " i=2004) on "
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
                                                    "http://opcfoundation.org/UA/:RequestServerStateChange"
                                                        + " (declaration i=12883, owner i=2004) on "
                                                        + getNodeId()));
                                          }
                                          if (unique.size() != 1) {
                                            return CompletableFuture.failedFuture(
                                                new UaException(
                                                    StatusCodes.Bad_TooManyMatches,
                                                    "http://opcfoundation.org/UA/:RequestServerStateChange"
                                                        + " (declaration i=12883, owner i=2004) on "
                                                        + getNodeId()));
                                          }
                                          return client
                                              .getAddressSpace()
                                              .getNodeAsync(unique.iterator().next())
                                              .thenCompose(
                                                  node -> {
                                                    if (node == null
                                                        || node.getNodeClass()
                                                            != NodeClass.Method) {
                                                      return CompletableFuture.failedFuture(
                                                          new UaException(
                                                              StatusCodes.Bad_NodeClassInvalid,
                                                              "http://opcfoundation.org/UA/:RequestServerStateChange"
                                                                  + " (declaration i=12883, owner"
                                                                  + " i=2004) on "
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
            } else if (node != null && !(node instanceof UaMethodNode)) {
              result.completeExceptionally(
                  new UaException(
                      StatusCodes.Bad_TypeMismatch,
                      "http://opcfoundation.org/UA/:RequestServerStateChange (declaration i=12883,"
                          + " owner i=2004)"));
            } else {
              result.complete((UaMethodNode) node);
            }
          });
    } catch (Exception e) {
      result.completeExceptionally(e);
    }
    return result;
  }

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part5/9.4
   *
   * <p>Invokes <code>RequestServerStateChange</code> on this node's ObjectId using the effective
   * Method contract.
   *
   * <p>The Method node is required. Confirmed absence fails with Bad_NotFound; lookup and service
   * failures are preserved. The call does not create a node or retry an invocation.
   *
   * <p>Requires Good operation status, including Good subcodes. Uncertain and Bad statuses fail
   * with UaException before any output conversion failure is reported. Application status outputs
   * remain separate.
   *
   * <p>Interrupted waiting cancels owned observation and pending discovery, restores the interrupt
   * flag and fails with Bad_UnexpectedError. It does not cancel server execution.
   *
   * @param state ; the supplied payload may be null.
   * @param estimatedReturnTime ; the supplied payload may be null.
   * @param secondsTillShutdown ; the supplied payload may be null.
   * @param reason ; the supplied payload may be null.
   * @param restart ; the supplied payload may be null.
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  @Override
  public void callRequestServerStateChange(
      @Nullable ServerState state,
      @Nullable DateTime estimatedReturnTime,
      @Nullable UInteger secondsTillShutdown,
      @Nullable LocalizedText reason,
      @Nullable Boolean restart)
      throws UaException {
    callRequestServerStateChangeDetailed(
            state, estimatedReturnTime, secondsTillShutdown, reason, restart)
        .requireGood();
  }

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part5/9.4
   *
   * <p>Invokes <code>RequestServerStateChange</code> on this node's ObjectId using the effective
   * Method contract.
   *
   * <p>The Method node is required. Confirmed absence fails with Bad_NotFound; lookup and service
   * failures are preserved. The call does not create a node or retry an invocation.
   *
   * <p>Requires Good operation status, including Good subcodes. Uncertain and Bad statuses fail
   * with UaException before any output conversion failure is reported. Application status outputs
   * remain separate.
   *
   * <p>Returns a non-null future. Lookup, input metadata, transport and response envelope failures
   * complete it exceptionally. Cancellation stops observation and dependent work that has not
   * started; it does not cancel server execution.
   *
   * @param state ; the supplied payload may be null.
   * @param estimatedReturnTime ; the supplied payload may be null.
   * @param secondsTillShutdown ; the supplied payload may be null.
   * @param reason ; the supplied payload may be null.
   * @param restart ; the supplied payload may be null.
   * @return a future whose successful payload is null.
   */
  @NullMarked
  @Override
  public CompletableFuture<? extends @Nullable Void> callRequestServerStateChangeAsync(
      @Nullable ServerState state,
      @Nullable DateTime estimatedReturnTime,
      @Nullable UInteger secondsTillShutdown,
      @Nullable LocalizedText reason,
      @Nullable Boolean restart) {
    CompletableFuture<@Nullable Void> result = new CompletableFuture<>();
    var call =
        callRequestServerStateChangeDetailedAsync(
            state, estimatedReturnTime, secondsTillShutdown, reason, restart);
    result.whenComplete(
        (value, failure) -> {
          if (result.isCancelled()) {
            call.cancel(false);
          }
        });
    call.whenComplete(
        (value, failure) -> {
          if (failure != null) {
            result.completeExceptionally(failure);
          } else {
            try {
              result.complete(value.requireGood());
            } catch (UaException statusFailure) {
              result.completeExceptionally(statusFailure);
            }
          }
        });
    return result;
  }

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part5/9.4
   *
   * <p>Invokes <code>RequestServerStateChange</code> on this node's ObjectId using the effective
   * Method contract.
   *
   * <p>The Method node is required. Confirmed absence fails with Bad_NotFound; lookup and service
   * failures are preserved. The call does not create a node or retry an invocation.
   *
   * <p>Retains operation status, request and response diagnostics, StringTable and raw outputs.
   * Good and Uncertain outputs are decoded; output metadata and conversion failures remain
   * available separately. Bad results preserve received wire outputs.
   *
   * <p>Interrupted waiting cancels owned observation and pending discovery, restores the interrupt
   * flag and fails with Bad_UnexpectedError. It does not cancel server execution.
   *
   * @param state ; the supplied payload may be null.
   * @param estimatedReturnTime ; the supplied payload may be null.
   * @param secondsTillShutdown ; the supplied payload may be null.
   * @param reason ; the supplied payload may be null.
   * @param restart ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  @Override
  public MethodCallResult<? extends @Nullable Void> callRequestServerStateChangeDetailed(
      @Nullable ServerState state,
      @Nullable DateTime estimatedReturnTime,
      @Nullable UInteger secondsTillShutdown,
      @Nullable LocalizedText reason,
      @Nullable Boolean restart)
      throws UaException {
    return callRequestServerStateChangeDetailed(
        MethodCallOptions.NONE, state, estimatedReturnTime, secondsTillShutdown, reason, restart);
  }

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part5/9.4
   *
   * <p>Invokes <code>RequestServerStateChange</code> on this node's ObjectId using the effective
   * Method contract.
   *
   * <p>The Method node is required. Confirmed absence fails with Bad_NotFound; lookup and service
   * failures are preserved. The call does not create a node or retry an invocation.
   *
   * <p>Retains operation status, request and response diagnostics, StringTable and raw outputs.
   * Good and Uncertain outputs are decoded; output metadata and conversion failures remain
   * available separately. Bad results preserve received wire outputs.
   *
   * <p>Interrupted waiting cancels owned observation and pending discovery, restores the interrupt
   * flag and fails with Bad_UnexpectedError. It does not cancel server execution.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @param state ; the supplied payload may be null.
   * @param estimatedReturnTime ; the supplied payload may be null.
   * @param secondsTillShutdown ; the supplied payload may be null.
   * @param reason ; the supplied payload may be null.
   * @param restart ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  @Override
  public MethodCallResult<? extends @Nullable Void> callRequestServerStateChangeDetailed(
      MethodCallOptions options,
      @Nullable ServerState state,
      @Nullable DateTime estimatedReturnTime,
      @Nullable UInteger secondsTillShutdown,
      @Nullable LocalizedText reason,
      @Nullable Boolean restart)
      throws UaException {
    Objects.requireNonNull(options, "options");
    var awaitedMethod =
        callRequestServerStateChangeDetailedAsync(
            options, state, estimatedReturnTime, secondsTillShutdown, reason, restart);
    try {
      return awaitedMethod.get();
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
      awaitedMethod.cancel(false);
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part5/9.4
   *
   * <p>Invokes <code>RequestServerStateChange</code> on this node's ObjectId using the effective
   * Method contract.
   *
   * <p>The Method node is required. Confirmed absence fails with Bad_NotFound; lookup and service
   * failures are preserved. The call does not create a node or retry an invocation.
   *
   * <p>Retains operation status, request and response diagnostics, StringTable and raw outputs.
   * Good and Uncertain outputs are decoded; output metadata and conversion failures remain
   * available separately. Bad results preserve received wire outputs.
   *
   * <p>Returns a non-null future. Lookup, input metadata, transport and response envelope failures
   * complete it exceptionally. Cancellation stops observation and dependent work that has not
   * started; it does not cancel server execution.
   *
   * @param state ; the supplied payload may be null.
   * @param estimatedReturnTime ; the supplied payload may be null.
   * @param secondsTillShutdown ; the supplied payload may be null.
   * @param reason ; the supplied payload may be null.
   * @param restart ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  @Override
  public CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callRequestServerStateChangeDetailedAsync(
          @Nullable ServerState state,
          @Nullable DateTime estimatedReturnTime,
          @Nullable UInteger secondsTillShutdown,
          @Nullable LocalizedText reason,
          @Nullable Boolean restart) {
    return callRequestServerStateChangeDetailedAsync(
        MethodCallOptions.NONE, state, estimatedReturnTime, secondsTillShutdown, reason, restart);
  }

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part5/9.4
   *
   * <p>Invokes <code>RequestServerStateChange</code> on this node's ObjectId using the effective
   * Method contract.
   *
   * <p>The Method node is required. Confirmed absence fails with Bad_NotFound; lookup and service
   * failures are preserved. The call does not create a node or retry an invocation.
   *
   * <p>Retains operation status, request and response diagnostics, StringTable and raw outputs.
   * Good and Uncertain outputs are decoded; output metadata and conversion failures remain
   * available separately. Bad results preserve received wire outputs.
   *
   * <p>Returns a non-null future. Lookup, input metadata, transport and response envelope failures
   * complete it exceptionally. Cancellation stops observation and dependent work that has not
   * started; it does not cancel server execution.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @param state ; the supplied payload may be null.
   * @param estimatedReturnTime ; the supplied payload may be null.
   * @param secondsTillShutdown ; the supplied payload may be null.
   * @param reason ; the supplied payload may be null.
   * @param restart ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  @Override
  public CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callRequestServerStateChangeDetailedAsync(
          MethodCallOptions options,
          @Nullable ServerState state,
          @Nullable DateTime estimatedReturnTime,
          @Nullable UInteger secondsTillShutdown,
          @Nullable LocalizedText reason,
          @Nullable Boolean restart) {
    CompletableFuture<MethodCallResult<@Nullable Void>> result = new CompletableFuture<>();
    try {
      Objects.requireNonNull(options, "options");
      List<@Nullable Object> rawInputs = new ArrayList<>();
      rawInputs.add(state);
      rawInputs.add(estimatedReturnTime);
      rawInputs.add(secondsTillShutdown);
      rawInputs.add(reason);
      rawInputs.add(restart);
      var lookup = getRequestServerStateChangeMethodNodeAsync();
      result.whenComplete(
          (value, failure) -> {
            if (result.isCancelled()) {
              lookup.cancel(false);
            }
          });
      CompletableFuture<MethodCallResult<@Nullable Void>> pipeline =
          lookup.thenCompose(
              methodNode -> {
                if (result.isCancelled()) {
                  return CompletableFuture.failedFuture(new CancellationException());
                }
                if (methodNode == null) {
                  return CompletableFuture.failedFuture(
                      new UaException(
                          StatusCodes.Bad_NotFound,
                          "Method node is required for invocation: RequestServerStateChange"));
                }
                var inputMetadata =
                    ClientDataTypes.read(
                        this.client,
                        List.<ExpandedNodeId>of(
                                ExpandedNodeId.parse("i=852"),
                                ExpandedNodeId.parse("i=13"),
                                ExpandedNodeId.parse("i=7"),
                                ExpandedNodeId.parse("i=21"),
                                ExpandedNodeId.parse("i=1"))
                            .subList(0, rawInputs.size()),
                        rawInputs.toArray());
                result.whenComplete(
                    (cancelledValue, cancelledFailure) -> {
                      if (result.isCancelled()) {
                        inputMetadata.cancel(false);
                      }
                    });
                return inputMetadata
                    .handle(
                        (inputDataTypeTree, inputMetadataFailure) -> {
                          if (inputMetadataFailure != null) {
                            var checkedMetadataFailureinputMetadataFailure =
                                UaException.extract(inputMetadataFailure);
                            if (checkedMetadataFailureinputMetadataFailure.isPresent()) {
                              throw new CompletionException(
                                  checkedMetadataFailureinputMetadataFailure.orElseThrow());
                            }
                            Throwable metadataCauseinputMetadataFailure = inputMetadataFailure;
                            while (metadataCauseinputMetadataFailure != null) {
                              if (metadataCauseinputMetadataFailure
                                  instanceof
                                  UaSerializationException metadataCodecinputMetadataFailure) {
                                throw new CompletionException(
                                    new UaException(
                                        metadataCodecinputMetadataFailure.getStatusCode().getValue()
                                                == StatusCodes.Bad_OutOfRange
                                            ? StatusCodes.Bad_OutOfRange
                                            : StatusCodes.Bad_TypeMismatch,
                                        metadataCodecinputMetadataFailure));
                              }
                              metadataCauseinputMetadataFailure =
                                  metadataCauseinputMetadataFailure.getCause();
                            }
                            throw new CompletionException(
                                UaException.extract(inputMetadataFailure)
                                    .orElseGet(() -> new UaException(inputMetadataFailure)));
                          }
                          return inputDataTypeTree;
                        })
                    .thenCompose(
                        dataTypeTree -> {
                          if (result.isCancelled()) {
                            return CompletableFuture.failedFuture(new CancellationException());
                          }
                          try {
                            List<Variant> inputArguments = new ArrayList<>();
                            if (rawInputs.size() > 0) {
                              Variant encoded0;
                              {
                                @Nullable ServerState convertedValue;
                                {
                                  Object methodValue = rawInputs.get(0);
                                  try {
                                    if (methodValue instanceof Matrix
                                        && ((Matrix) methodValue).isNull()) {
                                      methodValue = null;
                                    }
                                    NamespaceTable namespaceTable = this.client.getNamespaceTable();
                                    DataTypeTree dataTypeTree_ = dataTypeTree;
                                    NodeId argumentDataTypeId =
                                        ExpandedNodeId.parse("i=852")
                                            .toNodeId(namespaceTable)
                                            .orElseThrow(
                                                () ->
                                                    new UaException(
                                                        StatusCodes.Bad_NodeIdInvalid,
                                                        "Method argument DataType namespace is"
                                                            + " unavailable"));
                                    if (!OpcUaDataType.isBuiltin(argumentDataTypeId)
                                        && dataTypeTree_.getDataType(argumentDataTypeId) == null) {
                                      throw new UaException(
                                          StatusCodes.Bad_TypeMismatch,
                                          "Method argument State (effective property i=12884,"
                                              + " DataType i=852) is unavailable in the effective"
                                              + " type tree; resolved DataType: "
                                              + argumentDataTypeId);
                                    }
                                    Object numericElements =
                                        methodValue instanceof Matrix
                                            ? ((Matrix) methodValue).getElements()
                                            : methodValue;
                                    if (numericElements != null
                                        && numericElements.getClass().isArray()
                                        && (numericElements.getClass().getComponentType()
                                                == Number.class
                                            || numericElements.getClass().getComponentType()
                                                == UNumber.class)
                                        && (argumentDataTypeId.equals(NodeIds.Number)
                                            || dataTypeTree_.isSubtypeOf(
                                                argumentDataTypeId, NodeIds.Number))) {
                                      Class<?> numericElementType = null;
                                      for (int numericIndex = 0;
                                          numericIndex < Array.getLength(numericElements);
                                          numericIndex++) {
                                        Object numericElement =
                                            Array.get(numericElements, numericIndex);
                                        if (numericElement != null) {
                                          if (numericElementType != null
                                              && numericElementType != numericElement.getClass()) {
                                            throw new UaException(
                                                StatusCodes.Bad_TypeMismatch,
                                                "An abstract numeric array requires one homogeneous"
                                                    + " wire element type");
                                          }
                                          numericElementType = numericElement.getClass();
                                        }
                                      }
                                      if (numericElementType == null) {
                                        numericElementType =
                                            dataTypeTree_.getBackingClass(argumentDataTypeId);
                                      }
                                      if (numericElementType == Number.class
                                          || numericElementType == UNumber.class) {
                                        throw new UaException(
                                            StatusCodes.Bad_TypeMismatch,
                                            "An empty or all-null abstract numeric array requires a"
                                                + " concretely typed array");
                                      }
                                      Object numericArray =
                                          Array.newInstance(
                                              numericElementType, Array.getLength(numericElements));
                                      for (int numericIndex = 0;
                                          numericIndex < Array.getLength(numericElements);
                                          numericIndex++) {
                                        Array.set(
                                            numericArray,
                                            numericIndex,
                                            Array.get(numericElements, numericIndex));
                                      }
                                      if (methodValue instanceof Matrix) {
                                        methodValue =
                                            new Matrix(
                                                numericArray,
                                                ((Matrix) methodValue).getDimensions().clone(),
                                                ((Matrix) methodValue)
                                                    .getDataType()
                                                    .orElseThrow(
                                                        () ->
                                                            new UaException(
                                                                StatusCodes.Bad_TypeMismatch,
                                                                "A numeric Matrix requires an"
                                                                    + " explicit wire DataType")),
                                                ((Matrix) methodValue)
                                                    .getDataTypeId()
                                                    .orElse(null));
                                      } else {
                                        methodValue = numericArray;
                                      }
                                    }
                                    if (methodValue != null) {
                                      Object shapeElements =
                                          methodValue instanceof Matrix
                                              ? ((Matrix) methodValue).getElements()
                                              : methodValue;
                                      int valueRank =
                                          methodValue instanceof Matrix
                                              ? ((Matrix) methodValue).getValueRank()
                                              : ArrayUtil.getValueRank(methodValue);
                                      boolean emptyArray =
                                          methodValue.getClass().isArray()
                                              && ArrayUtil.getValueRank(methodValue) == 1
                                              && Array.getLength(methodValue) == 0;
                                      if (!(valueRank == -1)) {
                                        throw new UaException(
                                            StatusCodes.Bad_TypeMismatch,
                                            "Method argument ValueRank mismatch");
                                      }
                                      if (methodValue instanceof Matrix) {
                                        int[] dimensions = ((Matrix) methodValue).getDimensions();
                                        if (dimensions.length < 2
                                            || !shapeElements.getClass().isArray()
                                            || ArrayUtil.getValueRank(shapeElements) != 1) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "Malformed Method Matrix representation");
                                        }
                                        long elementCount = 1;
                                        for (int dimension : dimensions) {
                                          if (dimension < 0 || elementCount > Integer.MAX_VALUE) {
                                            throw new UaException(
                                                StatusCodes.Bad_TypeMismatch,
                                                "Malformed Method Matrix dimensions");
                                          }
                                          elementCount *= dimension;
                                        }
                                        if (elementCount != Array.getLength(shapeElements)) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "Method Matrix dimensions do not match its elements");
                                        }
                                        if (!(((Matrix) methodValue)
                                            .getDataType()
                                            .equals(Variant.of(shapeElements).getDataType()))) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "Method Matrix DataType does not match its elements");
                                        }
                                      }
                                      Variant.of(shapeElements);
                                    }
                                    {
                                      if (methodValue == null
                                          || methodValue instanceof Matrix
                                              && ((Matrix) methodValue).isNull()) {
                                        convertedValue = null;
                                      } else {
                                        Object elements =
                                            methodValue instanceof Matrix
                                                ? ((Matrix) methodValue).getElements()
                                                : methodValue;
                                        int rank =
                                            methodValue instanceof Matrix
                                                ? ((Matrix) methodValue).getValueRank()
                                                : ArrayUtil.getValueRank(methodValue);
                                        boolean permitted = rank == -1;
                                        if (!permitted) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "Method argument State: ValueRank=-1 does not permit"
                                                  + " rank "
                                                  + rank);
                                        }
                                        if (methodValue != null
                                            && !((Object) methodValue instanceof ServerState)) {
                                          if (!(methodValue instanceof Integer)) {
                                            throw new UaException(
                                                StatusCodes.Bad_TypeMismatch,
                                                "Method argument State: expected"
                                                    + " org.eclipse.milo.opcua.stack.core.types.enumerated.ServerState"
                                                    + " or Int32, got "
                                                    + methodValue);
                                          }
                                          if (ServerState.from((Integer) methodValue) == null) {
                                            throw new UaException(
                                                StatusCodes.Bad_OutOfRange,
                                                "Method argument State: unknown"
                                                    + " org.eclipse.milo.opcua.stack.core.types.enumerated.ServerState"
                                                    + " value "
                                                    + methodValue);
                                          }
                                        }
                                        convertedValue =
                                            methodValue == null
                                                    || methodValue instanceof ServerState
                                                ? (ServerState) methodValue
                                                : ServerState.from((Integer) methodValue);
                                      }
                                    }
                                  } catch (UaSerializationException conversionFailure) {
                                    throw new UaException(
                                        conversionFailure.getStatusCode().getValue()
                                                == StatusCodes.Bad_OutOfRange
                                            ? StatusCodes.Bad_OutOfRange
                                            : StatusCodes.Bad_TypeMismatch,
                                        conversionFailure);
                                  } catch (ClassCastException
                                      | IllegalArgumentException conversionFailure) {
                                    throw new UaException(
                                        StatusCodes.Bad_TypeMismatch, conversionFailure);
                                  }
                                }
                                try {
                                  Object wireValue = convertedValue;
                                  Object wireElements =
                                      wireValue instanceof Matrix
                                          ? ((Matrix) wireValue).getElements()
                                          : wireValue;
                                  if (wireElements != null && wireElements.getClass().isArray()) {
                                    for (int elementIndex = 0;
                                        elementIndex < Array.getLength(wireElements);
                                        elementIndex++) {
                                      if (Array.get(wireElements, elementIndex) == null) {
                                        throw new UaException(
                                            StatusCodes.Bad_TypeMismatch,
                                            "A Method enum or OptionSet array cannot encode a null"
                                                + " element");
                                      }
                                    }
                                  }
                                  var numericWireValues = new ArrayDeque<Object[]>();
                                  var numericWirePath =
                                      Collections.newSetFromMap(
                                          new IdentityHashMap<Object, Boolean>());
                                  if (wireValue != null) {
                                    numericWireValues.push(new Object[] {wireValue, false});
                                  }
                                  while (!numericWireValues.isEmpty()) {
                                    Object[] numericWireFrame = numericWireValues.pop();
                                    Object numericWireValue = numericWireFrame[0];
                                    if ((Boolean) numericWireFrame[1]) {
                                      numericWirePath.remove(numericWireValue);
                                      continue;
                                    }
                                    while (numericWireValue instanceof Variant
                                        || numericWireValue instanceof DataValue) {
                                      if (numericWireValue instanceof DataValue) {
                                        if (((DataValue) numericWireValue).getValue() == null) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "A DataValue requires a value wrapper; use"
                                                  + " Variant.NULL_VALUE for null");
                                        }
                                        if (((DataValue) numericWireValue).getStatusCode()
                                            == null) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "A DataValue requires a StatusCode; use"
                                                  + " StatusCode.GOOD for Good");
                                        }
                                        numericWireValue =
                                            ((DataValue) numericWireValue).getValue();
                                      } else {
                                        numericWireValue = ((Variant) numericWireValue).getValue();
                                      }
                                    }
                                    if (numericWireValue instanceof Matrix) {
                                      numericWireValue = ((Matrix) numericWireValue).getElements();
                                    }
                                    if (numericWireValue != null
                                        && numericWireValue.getClass().isArray()) {
                                      if (!numericWirePath.add(numericWireValue)) {
                                        throw new UaException(
                                            StatusCodes.Bad_TypeMismatch,
                                            "Cyclic Variant arrays cannot be encoded");
                                      }
                                      numericWireValues.push(new Object[] {numericWireValue, true});
                                      for (int numericWireIndex = 0;
                                          numericWireIndex < Array.getLength(numericWireValue);
                                          numericWireIndex++) {
                                        Object numericWireElement =
                                            Array.get(numericWireValue, numericWireIndex);
                                        if (numericWireElement == null
                                            && ArrayUtil.getBoxedType(numericWireValue)
                                                == Variant.class) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "A Variant wire array requires a wrapper for every"
                                                  + " element; use Variant.NULL_VALUE for null");
                                        }
                                        if (numericWireElement == null
                                            && (UaEnumeratedType.class.isAssignableFrom(
                                                    ArrayUtil.getBoxedType(numericWireValue))
                                                || OptionSetUInteger.class.isAssignableFrom(
                                                    ArrayUtil.getBoxedType(numericWireValue)))) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "An enum or OptionSet wire array cannot encode a null"
                                                  + " element");
                                        }
                                        if (numericWireElement == null
                                            && ArrayUtil.getBoxedType(numericWireValue)
                                                == Boolean.class) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "A Boolean wire array cannot retain a null element;"
                                                  + " Milo encodes it as false");
                                        }
                                        if (numericWireElement == null
                                            && ArrayUtil.getBoxedType(numericWireValue)
                                                == StatusCode.class) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "A StatusCode wire array cannot retain a null"
                                                  + " element; Milo encodes it as Good");
                                        }
                                        if (numericWireElement == null
                                            && Number.class.isAssignableFrom(
                                                ArrayUtil.getBoxedType(numericWireValue))) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "A numeric wire array cannot retain a null element;"
                                                  + " Milo encodes it as zero");
                                        }
                                        if (numericWireElement instanceof Variant
                                            || numericWireElement instanceof DataValue) {
                                          numericWireValues.push(
                                              new Object[] {numericWireElement, false});
                                        }
                                      }
                                    }
                                  }
                                  wireValue =
                                      ExtensionObject.encodeValue(
                                          this.client.getStaticEncodingContext(), wireValue);
                                  encoded0 = Variant.of(wireValue);
                                } catch (UaSerializationException encodingFailure) {
                                  throw new UaException(
                                      encodingFailure.getStatusCode().getValue()
                                              == StatusCodes.Bad_OutOfRange
                                          ? StatusCodes.Bad_OutOfRange
                                          : StatusCodes.Bad_TypeMismatch,
                                      encodingFailure);
                                } catch (ClassCastException
                                    | IllegalArgumentException encodingFailure) {
                                  throw new UaException(
                                      StatusCodes.Bad_TypeMismatch, encodingFailure);
                                }
                              }
                              inputArguments.add(encoded0);
                            }
                            if (rawInputs.size() > 1) {
                              Variant encoded1;
                              {
                                @Nullable DateTime convertedValue;
                                {
                                  Object methodValue = rawInputs.get(1);
                                  try {
                                    if (methodValue instanceof Matrix
                                        && ((Matrix) methodValue).isNull()) {
                                      methodValue = null;
                                    }
                                    NamespaceTable namespaceTable = this.client.getNamespaceTable();
                                    DataTypeTree dataTypeTree_ = dataTypeTree;
                                    NodeId argumentDataTypeId =
                                        ExpandedNodeId.parse("i=13")
                                            .toNodeId(namespaceTable)
                                            .orElseThrow(
                                                () ->
                                                    new UaException(
                                                        StatusCodes.Bad_NodeIdInvalid,
                                                        "Method argument DataType namespace is"
                                                            + " unavailable"));
                                    if (!OpcUaDataType.isBuiltin(argumentDataTypeId)
                                        && dataTypeTree_.getDataType(argumentDataTypeId) == null) {
                                      throw new UaException(
                                          StatusCodes.Bad_TypeMismatch,
                                          "Method argument EstimatedReturnTime (effective property"
                                              + " i=12884, DataType i=13) is unavailable in the"
                                              + " effective type tree; resolved DataType: "
                                              + argumentDataTypeId);
                                    }
                                    Object numericElements =
                                        methodValue instanceof Matrix
                                            ? ((Matrix) methodValue).getElements()
                                            : methodValue;
                                    if (numericElements != null
                                        && numericElements.getClass().isArray()
                                        && (numericElements.getClass().getComponentType()
                                                == Number.class
                                            || numericElements.getClass().getComponentType()
                                                == UNumber.class)
                                        && (argumentDataTypeId.equals(NodeIds.Number)
                                            || dataTypeTree_.isSubtypeOf(
                                                argumentDataTypeId, NodeIds.Number))) {
                                      Class<?> numericElementType = null;
                                      for (int numericIndex = 0;
                                          numericIndex < Array.getLength(numericElements);
                                          numericIndex++) {
                                        Object numericElement =
                                            Array.get(numericElements, numericIndex);
                                        if (numericElement != null) {
                                          if (numericElementType != null
                                              && numericElementType != numericElement.getClass()) {
                                            throw new UaException(
                                                StatusCodes.Bad_TypeMismatch,
                                                "An abstract numeric array requires one homogeneous"
                                                    + " wire element type");
                                          }
                                          numericElementType = numericElement.getClass();
                                        }
                                      }
                                      if (numericElementType == null) {
                                        numericElementType =
                                            dataTypeTree_.getBackingClass(argumentDataTypeId);
                                      }
                                      if (numericElementType == Number.class
                                          || numericElementType == UNumber.class) {
                                        throw new UaException(
                                            StatusCodes.Bad_TypeMismatch,
                                            "An empty or all-null abstract numeric array requires a"
                                                + " concretely typed array");
                                      }
                                      Object numericArray =
                                          Array.newInstance(
                                              numericElementType, Array.getLength(numericElements));
                                      for (int numericIndex = 0;
                                          numericIndex < Array.getLength(numericElements);
                                          numericIndex++) {
                                        Array.set(
                                            numericArray,
                                            numericIndex,
                                            Array.get(numericElements, numericIndex));
                                      }
                                      if (methodValue instanceof Matrix) {
                                        methodValue =
                                            new Matrix(
                                                numericArray,
                                                ((Matrix) methodValue).getDimensions().clone(),
                                                ((Matrix) methodValue)
                                                    .getDataType()
                                                    .orElseThrow(
                                                        () ->
                                                            new UaException(
                                                                StatusCodes.Bad_TypeMismatch,
                                                                "A numeric Matrix requires an"
                                                                    + " explicit wire DataType")),
                                                ((Matrix) methodValue)
                                                    .getDataTypeId()
                                                    .orElse(null));
                                      } else {
                                        methodValue = numericArray;
                                      }
                                    }
                                    if (methodValue != null) {
                                      Object shapeElements =
                                          methodValue instanceof Matrix
                                              ? ((Matrix) methodValue).getElements()
                                              : methodValue;
                                      int valueRank =
                                          methodValue instanceof Matrix
                                              ? ((Matrix) methodValue).getValueRank()
                                              : ArrayUtil.getValueRank(methodValue);
                                      boolean emptyArray =
                                          methodValue.getClass().isArray()
                                              && ArrayUtil.getValueRank(methodValue) == 1
                                              && Array.getLength(methodValue) == 0;
                                      if (!(valueRank == -1)) {
                                        throw new UaException(
                                            StatusCodes.Bad_TypeMismatch,
                                            "Method argument ValueRank mismatch");
                                      }
                                      if (methodValue instanceof Matrix) {
                                        int[] dimensions = ((Matrix) methodValue).getDimensions();
                                        if (dimensions.length < 2
                                            || !shapeElements.getClass().isArray()
                                            || ArrayUtil.getValueRank(shapeElements) != 1) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "Malformed Method Matrix representation");
                                        }
                                        long elementCount = 1;
                                        for (int dimension : dimensions) {
                                          if (dimension < 0 || elementCount > Integer.MAX_VALUE) {
                                            throw new UaException(
                                                StatusCodes.Bad_TypeMismatch,
                                                "Malformed Method Matrix dimensions");
                                          }
                                          elementCount *= dimension;
                                        }
                                        if (elementCount != Array.getLength(shapeElements)) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "Method Matrix dimensions do not match its elements");
                                        }
                                        if (!(((Matrix) methodValue)
                                            .getDataType()
                                            .equals(Variant.of(shapeElements).getDataType()))) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "Method Matrix DataType does not match its elements");
                                        }
                                      }
                                      Variant.of(shapeElements);
                                    }
                                    if (methodValue != null) {
                                      Object typedElements =
                                          methodValue instanceof Matrix
                                              ? ((Matrix) methodValue).getElements()
                                              : methodValue;
                                      if (NodeIds.Structure.equals(argumentDataTypeId)
                                          || dataTypeTree_.isStructType(argumentDataTypeId)) {
                                        var declaredType =
                                            dataTypeTree_.getType(argumentDataTypeId);
                                        if (typedElements.getClass().isArray()) {
                                          var structureCodec =
                                              this.client
                                                  .getStaticEncodingContext()
                                                  .getDataTypeManager()
                                                  .getCodec(argumentDataTypeId);
                                          Class<?> structureClass =
                                              structureCodec == null
                                                  ? UaStructuredType.class
                                                  : structureCodec.getType();
                                          Object decodedStructures =
                                              Array.newInstance(
                                                  structureClass, Array.getLength(typedElements));
                                          for (int structureIndex = 0;
                                              structureIndex < Array.getLength(typedElements);
                                              structureIndex++) {
                                            Object structure =
                                                Array.get(typedElements, structureIndex);
                                            if (structure instanceof ExtensionObject) {
                                              structure =
                                                  ((ExtensionObject) structure).isNull()
                                                      ? null
                                                      : ((ExtensionObject) structure)
                                                          .decode(
                                                              this.client
                                                                  .getStaticEncodingContext());
                                            }
                                            if (structure != null) {
                                              if (!(structure instanceof UaStructuredType)) {
                                                throw new UaException(
                                                    StatusCodes.Bad_TypeMismatch,
                                                    "Method argument requires a Structure value");
                                              }
                                              if (NodeIds.Structure.equals(argumentDataTypeId)
                                                  || declaredType != null
                                                      && declaredType.isAbstract()) {
                                                if (!dataTypeTree_.isSubtypeOf(
                                                    ((UaStructuredType) structure)
                                                        .getTypeId()
                                                        .toNodeId(namespaceTable)
                                                        .orElse(NodeId.NULL_VALUE),
                                                    argumentDataTypeId)) {
                                                  throw new UaException(
                                                      StatusCodes.Bad_TypeMismatch,
                                                      "Method Structure is not a subtype of the"
                                                          + " effective DataType");
                                                }
                                              } else {
                                                if (!argumentDataTypeId.equals(
                                                    ((UaStructuredType) structure)
                                                        .getTypeId()
                                                        .toNodeId(namespaceTable)
                                                        .orElse(NodeId.NULL_VALUE))) {
                                                  throw new UaException(
                                                      StatusCodes.Bad_TypeMismatch,
                                                      "Method Structure does not match the"
                                                          + " effective DataType");
                                                }
                                              }
                                            }
                                            Array.set(decodedStructures, structureIndex, structure);
                                          }
                                          methodValue =
                                              methodValue instanceof Matrix
                                                  ? new Matrix(
                                                      decodedStructures,
                                                      ((Matrix) methodValue)
                                                          .getDimensions()
                                                          .clone())
                                                  : decodedStructures;
                                        } else {
                                          if (typedElements instanceof ExtensionObject) {
                                            typedElements =
                                                ((ExtensionObject) typedElements).isNull()
                                                    ? null
                                                    : ((ExtensionObject) typedElements)
                                                        .decode(
                                                            this.client.getStaticEncodingContext());
                                          }
                                          if (typedElements != null) {
                                            if (!(typedElements instanceof UaStructuredType)) {
                                              throw new UaException(
                                                  StatusCodes.Bad_TypeMismatch,
                                                  "Method argument requires a Structure value");
                                            }
                                            if (NodeIds.Structure.equals(argumentDataTypeId)
                                                || declaredType != null
                                                    && declaredType.isAbstract()) {
                                              if (!dataTypeTree_.isSubtypeOf(
                                                  ((UaStructuredType) typedElements)
                                                      .getTypeId()
                                                      .toNodeId(namespaceTable)
                                                      .orElse(NodeId.NULL_VALUE),
                                                  argumentDataTypeId)) {
                                                throw new UaException(
                                                    StatusCodes.Bad_TypeMismatch,
                                                    "Method Structure is not a subtype of the"
                                                        + " effective DataType");
                                              }
                                            } else {
                                              if (!argumentDataTypeId.equals(
                                                  ((UaStructuredType) typedElements)
                                                      .getTypeId()
                                                      .toNodeId(namespaceTable)
                                                      .orElse(NodeId.NULL_VALUE))) {
                                                throw new UaException(
                                                    StatusCodes.Bad_TypeMismatch,
                                                    "Method Structure does not match the effective"
                                                        + " DataType");
                                              }
                                            }
                                          }
                                          methodValue = typedElements;
                                        }
                                      } else {
                                        Variant.of(typedElements);
                                        NodeId assignableDataTypeId =
                                            dataTypeTree_.getBackingClass(argumentDataTypeId)
                                                        == Number.class
                                                    && dataTypeTree_.isSubtypeOf(
                                                        argumentDataTypeId, NodeIds.Integer)
                                                ? NodeIds.Integer
                                                : argumentDataTypeId;
                                        if (dataTypeTree_.getBackingClass(argumentDataTypeId)
                                                != Variant.class
                                            && !dataTypeTree_.isAssignable(
                                                assignableDataTypeId,
                                                ArrayUtil.getBoxedType(typedElements))) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "Method argument DataType mismatch");
                                        }
                                      }
                                    }
                                    convertedValue = (DateTime) methodValue;
                                  } catch (UaSerializationException conversionFailure) {
                                    throw new UaException(
                                        conversionFailure.getStatusCode().getValue()
                                                == StatusCodes.Bad_OutOfRange
                                            ? StatusCodes.Bad_OutOfRange
                                            : StatusCodes.Bad_TypeMismatch,
                                        conversionFailure);
                                  } catch (ClassCastException
                                      | IllegalArgumentException conversionFailure) {
                                    throw new UaException(
                                        StatusCodes.Bad_TypeMismatch, conversionFailure);
                                  }
                                }
                                try {
                                  Object wireValue = convertedValue;
                                  Object wireElements =
                                      wireValue instanceof Matrix
                                          ? ((Matrix) wireValue).getElements()
                                          : wireValue;
                                  var numericWireValues = new ArrayDeque<Object[]>();
                                  var numericWirePath =
                                      Collections.newSetFromMap(
                                          new IdentityHashMap<Object, Boolean>());
                                  if (wireValue != null) {
                                    numericWireValues.push(new Object[] {wireValue, false});
                                  }
                                  while (!numericWireValues.isEmpty()) {
                                    Object[] numericWireFrame = numericWireValues.pop();
                                    Object numericWireValue = numericWireFrame[0];
                                    if ((Boolean) numericWireFrame[1]) {
                                      numericWirePath.remove(numericWireValue);
                                      continue;
                                    }
                                    while (numericWireValue instanceof Variant
                                        || numericWireValue instanceof DataValue) {
                                      if (numericWireValue instanceof DataValue) {
                                        if (((DataValue) numericWireValue).getValue() == null) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "A DataValue requires a value wrapper; use"
                                                  + " Variant.NULL_VALUE for null");
                                        }
                                        if (((DataValue) numericWireValue).getStatusCode()
                                            == null) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "A DataValue requires a StatusCode; use"
                                                  + " StatusCode.GOOD for Good");
                                        }
                                        numericWireValue =
                                            ((DataValue) numericWireValue).getValue();
                                      } else {
                                        numericWireValue = ((Variant) numericWireValue).getValue();
                                      }
                                    }
                                    if (numericWireValue instanceof Matrix) {
                                      numericWireValue = ((Matrix) numericWireValue).getElements();
                                    }
                                    if (numericWireValue != null
                                        && numericWireValue.getClass().isArray()) {
                                      if (!numericWirePath.add(numericWireValue)) {
                                        throw new UaException(
                                            StatusCodes.Bad_TypeMismatch,
                                            "Cyclic Variant arrays cannot be encoded");
                                      }
                                      numericWireValues.push(new Object[] {numericWireValue, true});
                                      for (int numericWireIndex = 0;
                                          numericWireIndex < Array.getLength(numericWireValue);
                                          numericWireIndex++) {
                                        Object numericWireElement =
                                            Array.get(numericWireValue, numericWireIndex);
                                        if (numericWireElement == null
                                            && ArrayUtil.getBoxedType(numericWireValue)
                                                == Variant.class) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "A Variant wire array requires a wrapper for every"
                                                  + " element; use Variant.NULL_VALUE for null");
                                        }
                                        if (numericWireElement == null
                                            && (UaEnumeratedType.class.isAssignableFrom(
                                                    ArrayUtil.getBoxedType(numericWireValue))
                                                || OptionSetUInteger.class.isAssignableFrom(
                                                    ArrayUtil.getBoxedType(numericWireValue)))) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "An enum or OptionSet wire array cannot encode a null"
                                                  + " element");
                                        }
                                        if (numericWireElement == null
                                            && ArrayUtil.getBoxedType(numericWireValue)
                                                == Boolean.class) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "A Boolean wire array cannot retain a null element;"
                                                  + " Milo encodes it as false");
                                        }
                                        if (numericWireElement == null
                                            && ArrayUtil.getBoxedType(numericWireValue)
                                                == StatusCode.class) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "A StatusCode wire array cannot retain a null"
                                                  + " element; Milo encodes it as Good");
                                        }
                                        if (numericWireElement == null
                                            && Number.class.isAssignableFrom(
                                                ArrayUtil.getBoxedType(numericWireValue))) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "A numeric wire array cannot retain a null element;"
                                                  + " Milo encodes it as zero");
                                        }
                                        if (numericWireElement instanceof Variant
                                            || numericWireElement instanceof DataValue) {
                                          numericWireValues.push(
                                              new Object[] {numericWireElement, false});
                                        }
                                      }
                                    }
                                  }
                                  wireValue =
                                      ExtensionObject.encodeValue(
                                          this.client.getStaticEncodingContext(), wireValue);
                                  encoded1 = Variant.of(wireValue);
                                } catch (UaSerializationException encodingFailure) {
                                  throw new UaException(
                                      encodingFailure.getStatusCode().getValue()
                                              == StatusCodes.Bad_OutOfRange
                                          ? StatusCodes.Bad_OutOfRange
                                          : StatusCodes.Bad_TypeMismatch,
                                      encodingFailure);
                                } catch (ClassCastException
                                    | IllegalArgumentException encodingFailure) {
                                  throw new UaException(
                                      StatusCodes.Bad_TypeMismatch, encodingFailure);
                                }
                              }
                              inputArguments.add(encoded1);
                            }
                            if (rawInputs.size() > 2) {
                              Variant encoded2;
                              {
                                @Nullable UInteger convertedValue;
                                {
                                  Object methodValue = rawInputs.get(2);
                                  try {
                                    if (methodValue instanceof Matrix
                                        && ((Matrix) methodValue).isNull()) {
                                      methodValue = null;
                                    }
                                    NamespaceTable namespaceTable = this.client.getNamespaceTable();
                                    DataTypeTree dataTypeTree_ = dataTypeTree;
                                    NodeId argumentDataTypeId =
                                        ExpandedNodeId.parse("i=7")
                                            .toNodeId(namespaceTable)
                                            .orElseThrow(
                                                () ->
                                                    new UaException(
                                                        StatusCodes.Bad_NodeIdInvalid,
                                                        "Method argument DataType namespace is"
                                                            + " unavailable"));
                                    if (!OpcUaDataType.isBuiltin(argumentDataTypeId)
                                        && dataTypeTree_.getDataType(argumentDataTypeId) == null) {
                                      throw new UaException(
                                          StatusCodes.Bad_TypeMismatch,
                                          "Method argument SecondsTillShutdown (effective property"
                                              + " i=12884, DataType i=7) is unavailable in the"
                                              + " effective type tree; resolved DataType: "
                                              + argumentDataTypeId);
                                    }
                                    Object numericElements =
                                        methodValue instanceof Matrix
                                            ? ((Matrix) methodValue).getElements()
                                            : methodValue;
                                    if (numericElements != null
                                        && numericElements.getClass().isArray()
                                        && (numericElements.getClass().getComponentType()
                                                == Number.class
                                            || numericElements.getClass().getComponentType()
                                                == UNumber.class)
                                        && (argumentDataTypeId.equals(NodeIds.Number)
                                            || dataTypeTree_.isSubtypeOf(
                                                argumentDataTypeId, NodeIds.Number))) {
                                      Class<?> numericElementType = null;
                                      for (int numericIndex = 0;
                                          numericIndex < Array.getLength(numericElements);
                                          numericIndex++) {
                                        Object numericElement =
                                            Array.get(numericElements, numericIndex);
                                        if (numericElement != null) {
                                          if (numericElementType != null
                                              && numericElementType != numericElement.getClass()) {
                                            throw new UaException(
                                                StatusCodes.Bad_TypeMismatch,
                                                "An abstract numeric array requires one homogeneous"
                                                    + " wire element type");
                                          }
                                          numericElementType = numericElement.getClass();
                                        }
                                      }
                                      if (numericElementType == null) {
                                        numericElementType =
                                            dataTypeTree_.getBackingClass(argumentDataTypeId);
                                      }
                                      if (numericElementType == Number.class
                                          || numericElementType == UNumber.class) {
                                        throw new UaException(
                                            StatusCodes.Bad_TypeMismatch,
                                            "An empty or all-null abstract numeric array requires a"
                                                + " concretely typed array");
                                      }
                                      Object numericArray =
                                          Array.newInstance(
                                              numericElementType, Array.getLength(numericElements));
                                      for (int numericIndex = 0;
                                          numericIndex < Array.getLength(numericElements);
                                          numericIndex++) {
                                        Array.set(
                                            numericArray,
                                            numericIndex,
                                            Array.get(numericElements, numericIndex));
                                      }
                                      if (methodValue instanceof Matrix) {
                                        methodValue =
                                            new Matrix(
                                                numericArray,
                                                ((Matrix) methodValue).getDimensions().clone(),
                                                ((Matrix) methodValue)
                                                    .getDataType()
                                                    .orElseThrow(
                                                        () ->
                                                            new UaException(
                                                                StatusCodes.Bad_TypeMismatch,
                                                                "A numeric Matrix requires an"
                                                                    + " explicit wire DataType")),
                                                ((Matrix) methodValue)
                                                    .getDataTypeId()
                                                    .orElse(null));
                                      } else {
                                        methodValue = numericArray;
                                      }
                                    }
                                    if (methodValue != null) {
                                      Object shapeElements =
                                          methodValue instanceof Matrix
                                              ? ((Matrix) methodValue).getElements()
                                              : methodValue;
                                      int valueRank =
                                          methodValue instanceof Matrix
                                              ? ((Matrix) methodValue).getValueRank()
                                              : ArrayUtil.getValueRank(methodValue);
                                      boolean emptyArray =
                                          methodValue.getClass().isArray()
                                              && ArrayUtil.getValueRank(methodValue) == 1
                                              && Array.getLength(methodValue) == 0;
                                      if (!(valueRank == -1)) {
                                        throw new UaException(
                                            StatusCodes.Bad_TypeMismatch,
                                            "Method argument ValueRank mismatch");
                                      }
                                      if (methodValue instanceof Matrix) {
                                        int[] dimensions = ((Matrix) methodValue).getDimensions();
                                        if (dimensions.length < 2
                                            || !shapeElements.getClass().isArray()
                                            || ArrayUtil.getValueRank(shapeElements) != 1) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "Malformed Method Matrix representation");
                                        }
                                        long elementCount = 1;
                                        for (int dimension : dimensions) {
                                          if (dimension < 0 || elementCount > Integer.MAX_VALUE) {
                                            throw new UaException(
                                                StatusCodes.Bad_TypeMismatch,
                                                "Malformed Method Matrix dimensions");
                                          }
                                          elementCount *= dimension;
                                        }
                                        if (elementCount != Array.getLength(shapeElements)) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "Method Matrix dimensions do not match its elements");
                                        }
                                        if (!(((Matrix) methodValue)
                                            .getDataType()
                                            .equals(Variant.of(shapeElements).getDataType()))) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "Method Matrix DataType does not match its elements");
                                        }
                                      }
                                      Variant.of(shapeElements);
                                    }
                                    if (methodValue != null) {
                                      Object typedElements =
                                          methodValue instanceof Matrix
                                              ? ((Matrix) methodValue).getElements()
                                              : methodValue;
                                      if (NodeIds.Structure.equals(argumentDataTypeId)
                                          || dataTypeTree_.isStructType(argumentDataTypeId)) {
                                        var declaredType =
                                            dataTypeTree_.getType(argumentDataTypeId);
                                        if (typedElements.getClass().isArray()) {
                                          var structureCodec =
                                              this.client
                                                  .getStaticEncodingContext()
                                                  .getDataTypeManager()
                                                  .getCodec(argumentDataTypeId);
                                          Class<?> structureClass =
                                              structureCodec == null
                                                  ? UaStructuredType.class
                                                  : structureCodec.getType();
                                          Object decodedStructures =
                                              Array.newInstance(
                                                  structureClass, Array.getLength(typedElements));
                                          for (int structureIndex = 0;
                                              structureIndex < Array.getLength(typedElements);
                                              structureIndex++) {
                                            Object structure =
                                                Array.get(typedElements, structureIndex);
                                            if (structure instanceof ExtensionObject) {
                                              structure =
                                                  ((ExtensionObject) structure).isNull()
                                                      ? null
                                                      : ((ExtensionObject) structure)
                                                          .decode(
                                                              this.client
                                                                  .getStaticEncodingContext());
                                            }
                                            if (structure != null) {
                                              if (!(structure instanceof UaStructuredType)) {
                                                throw new UaException(
                                                    StatusCodes.Bad_TypeMismatch,
                                                    "Method argument requires a Structure value");
                                              }
                                              if (NodeIds.Structure.equals(argumentDataTypeId)
                                                  || declaredType != null
                                                      && declaredType.isAbstract()) {
                                                if (!dataTypeTree_.isSubtypeOf(
                                                    ((UaStructuredType) structure)
                                                        .getTypeId()
                                                        .toNodeId(namespaceTable)
                                                        .orElse(NodeId.NULL_VALUE),
                                                    argumentDataTypeId)) {
                                                  throw new UaException(
                                                      StatusCodes.Bad_TypeMismatch,
                                                      "Method Structure is not a subtype of the"
                                                          + " effective DataType");
                                                }
                                              } else {
                                                if (!argumentDataTypeId.equals(
                                                    ((UaStructuredType) structure)
                                                        .getTypeId()
                                                        .toNodeId(namespaceTable)
                                                        .orElse(NodeId.NULL_VALUE))) {
                                                  throw new UaException(
                                                      StatusCodes.Bad_TypeMismatch,
                                                      "Method Structure does not match the"
                                                          + " effective DataType");
                                                }
                                              }
                                            }
                                            Array.set(decodedStructures, structureIndex, structure);
                                          }
                                          methodValue =
                                              methodValue instanceof Matrix
                                                  ? new Matrix(
                                                      decodedStructures,
                                                      ((Matrix) methodValue)
                                                          .getDimensions()
                                                          .clone())
                                                  : decodedStructures;
                                        } else {
                                          if (typedElements instanceof ExtensionObject) {
                                            typedElements =
                                                ((ExtensionObject) typedElements).isNull()
                                                    ? null
                                                    : ((ExtensionObject) typedElements)
                                                        .decode(
                                                            this.client.getStaticEncodingContext());
                                          }
                                          if (typedElements != null) {
                                            if (!(typedElements instanceof UaStructuredType)) {
                                              throw new UaException(
                                                  StatusCodes.Bad_TypeMismatch,
                                                  "Method argument requires a Structure value");
                                            }
                                            if (NodeIds.Structure.equals(argumentDataTypeId)
                                                || declaredType != null
                                                    && declaredType.isAbstract()) {
                                              if (!dataTypeTree_.isSubtypeOf(
                                                  ((UaStructuredType) typedElements)
                                                      .getTypeId()
                                                      .toNodeId(namespaceTable)
                                                      .orElse(NodeId.NULL_VALUE),
                                                  argumentDataTypeId)) {
                                                throw new UaException(
                                                    StatusCodes.Bad_TypeMismatch,
                                                    "Method Structure is not a subtype of the"
                                                        + " effective DataType");
                                              }
                                            } else {
                                              if (!argumentDataTypeId.equals(
                                                  ((UaStructuredType) typedElements)
                                                      .getTypeId()
                                                      .toNodeId(namespaceTable)
                                                      .orElse(NodeId.NULL_VALUE))) {
                                                throw new UaException(
                                                    StatusCodes.Bad_TypeMismatch,
                                                    "Method Structure does not match the effective"
                                                        + " DataType");
                                              }
                                            }
                                          }
                                          methodValue = typedElements;
                                        }
                                      } else {
                                        Variant.of(typedElements);
                                        NodeId assignableDataTypeId =
                                            dataTypeTree_.getBackingClass(argumentDataTypeId)
                                                        == Number.class
                                                    && dataTypeTree_.isSubtypeOf(
                                                        argumentDataTypeId, NodeIds.Integer)
                                                ? NodeIds.Integer
                                                : argumentDataTypeId;
                                        if (dataTypeTree_.getBackingClass(argumentDataTypeId)
                                                != Variant.class
                                            && !dataTypeTree_.isAssignable(
                                                assignableDataTypeId,
                                                ArrayUtil.getBoxedType(typedElements))) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "Method argument DataType mismatch");
                                        }
                                      }
                                    }
                                    convertedValue = (UInteger) methodValue;
                                  } catch (UaSerializationException conversionFailure) {
                                    throw new UaException(
                                        conversionFailure.getStatusCode().getValue()
                                                == StatusCodes.Bad_OutOfRange
                                            ? StatusCodes.Bad_OutOfRange
                                            : StatusCodes.Bad_TypeMismatch,
                                        conversionFailure);
                                  } catch (ClassCastException
                                      | IllegalArgumentException conversionFailure) {
                                    throw new UaException(
                                        StatusCodes.Bad_TypeMismatch, conversionFailure);
                                  }
                                }
                                try {
                                  Object wireValue = convertedValue;
                                  Object wireElements =
                                      wireValue instanceof Matrix
                                          ? ((Matrix) wireValue).getElements()
                                          : wireValue;
                                  var numericWireValues = new ArrayDeque<Object[]>();
                                  var numericWirePath =
                                      Collections.newSetFromMap(
                                          new IdentityHashMap<Object, Boolean>());
                                  if (wireValue != null) {
                                    numericWireValues.push(new Object[] {wireValue, false});
                                  }
                                  while (!numericWireValues.isEmpty()) {
                                    Object[] numericWireFrame = numericWireValues.pop();
                                    Object numericWireValue = numericWireFrame[0];
                                    if ((Boolean) numericWireFrame[1]) {
                                      numericWirePath.remove(numericWireValue);
                                      continue;
                                    }
                                    while (numericWireValue instanceof Variant
                                        || numericWireValue instanceof DataValue) {
                                      if (numericWireValue instanceof DataValue) {
                                        if (((DataValue) numericWireValue).getValue() == null) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "A DataValue requires a value wrapper; use"
                                                  + " Variant.NULL_VALUE for null");
                                        }
                                        if (((DataValue) numericWireValue).getStatusCode()
                                            == null) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "A DataValue requires a StatusCode; use"
                                                  + " StatusCode.GOOD for Good");
                                        }
                                        numericWireValue =
                                            ((DataValue) numericWireValue).getValue();
                                      } else {
                                        numericWireValue = ((Variant) numericWireValue).getValue();
                                      }
                                    }
                                    if (numericWireValue instanceof Matrix) {
                                      numericWireValue = ((Matrix) numericWireValue).getElements();
                                    }
                                    if (numericWireValue != null
                                        && numericWireValue.getClass().isArray()) {
                                      if (!numericWirePath.add(numericWireValue)) {
                                        throw new UaException(
                                            StatusCodes.Bad_TypeMismatch,
                                            "Cyclic Variant arrays cannot be encoded");
                                      }
                                      numericWireValues.push(new Object[] {numericWireValue, true});
                                      for (int numericWireIndex = 0;
                                          numericWireIndex < Array.getLength(numericWireValue);
                                          numericWireIndex++) {
                                        Object numericWireElement =
                                            Array.get(numericWireValue, numericWireIndex);
                                        if (numericWireElement == null
                                            && ArrayUtil.getBoxedType(numericWireValue)
                                                == Variant.class) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "A Variant wire array requires a wrapper for every"
                                                  + " element; use Variant.NULL_VALUE for null");
                                        }
                                        if (numericWireElement == null
                                            && (UaEnumeratedType.class.isAssignableFrom(
                                                    ArrayUtil.getBoxedType(numericWireValue))
                                                || OptionSetUInteger.class.isAssignableFrom(
                                                    ArrayUtil.getBoxedType(numericWireValue)))) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "An enum or OptionSet wire array cannot encode a null"
                                                  + " element");
                                        }
                                        if (numericWireElement == null
                                            && ArrayUtil.getBoxedType(numericWireValue)
                                                == Boolean.class) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "A Boolean wire array cannot retain a null element;"
                                                  + " Milo encodes it as false");
                                        }
                                        if (numericWireElement == null
                                            && ArrayUtil.getBoxedType(numericWireValue)
                                                == StatusCode.class) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "A StatusCode wire array cannot retain a null"
                                                  + " element; Milo encodes it as Good");
                                        }
                                        if (numericWireElement == null
                                            && Number.class.isAssignableFrom(
                                                ArrayUtil.getBoxedType(numericWireValue))) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "A numeric wire array cannot retain a null element;"
                                                  + " Milo encodes it as zero");
                                        }
                                        if (numericWireElement instanceof Variant
                                            || numericWireElement instanceof DataValue) {
                                          numericWireValues.push(
                                              new Object[] {numericWireElement, false});
                                        }
                                      }
                                    }
                                  }
                                  wireValue =
                                      ExtensionObject.encodeValue(
                                          this.client.getStaticEncodingContext(), wireValue);
                                  encoded2 = Variant.of(wireValue);
                                } catch (UaSerializationException encodingFailure) {
                                  throw new UaException(
                                      encodingFailure.getStatusCode().getValue()
                                              == StatusCodes.Bad_OutOfRange
                                          ? StatusCodes.Bad_OutOfRange
                                          : StatusCodes.Bad_TypeMismatch,
                                      encodingFailure);
                                } catch (ClassCastException
                                    | IllegalArgumentException encodingFailure) {
                                  throw new UaException(
                                      StatusCodes.Bad_TypeMismatch, encodingFailure);
                                }
                              }
                              inputArguments.add(encoded2);
                            }
                            if (rawInputs.size() > 3) {
                              Variant encoded3;
                              {
                                @Nullable LocalizedText convertedValue;
                                {
                                  Object methodValue = rawInputs.get(3);
                                  try {
                                    if (methodValue instanceof Matrix
                                        && ((Matrix) methodValue).isNull()) {
                                      methodValue = null;
                                    }
                                    NamespaceTable namespaceTable = this.client.getNamespaceTable();
                                    DataTypeTree dataTypeTree_ = dataTypeTree;
                                    NodeId argumentDataTypeId =
                                        ExpandedNodeId.parse("i=21")
                                            .toNodeId(namespaceTable)
                                            .orElseThrow(
                                                () ->
                                                    new UaException(
                                                        StatusCodes.Bad_NodeIdInvalid,
                                                        "Method argument DataType namespace is"
                                                            + " unavailable"));
                                    if (!OpcUaDataType.isBuiltin(argumentDataTypeId)
                                        && dataTypeTree_.getDataType(argumentDataTypeId) == null) {
                                      throw new UaException(
                                          StatusCodes.Bad_TypeMismatch,
                                          "Method argument Reason (effective property i=12884,"
                                              + " DataType i=21) is unavailable in the effective"
                                              + " type tree; resolved DataType: "
                                              + argumentDataTypeId);
                                    }
                                    Object numericElements =
                                        methodValue instanceof Matrix
                                            ? ((Matrix) methodValue).getElements()
                                            : methodValue;
                                    if (numericElements != null
                                        && numericElements.getClass().isArray()
                                        && (numericElements.getClass().getComponentType()
                                                == Number.class
                                            || numericElements.getClass().getComponentType()
                                                == UNumber.class)
                                        && (argumentDataTypeId.equals(NodeIds.Number)
                                            || dataTypeTree_.isSubtypeOf(
                                                argumentDataTypeId, NodeIds.Number))) {
                                      Class<?> numericElementType = null;
                                      for (int numericIndex = 0;
                                          numericIndex < Array.getLength(numericElements);
                                          numericIndex++) {
                                        Object numericElement =
                                            Array.get(numericElements, numericIndex);
                                        if (numericElement != null) {
                                          if (numericElementType != null
                                              && numericElementType != numericElement.getClass()) {
                                            throw new UaException(
                                                StatusCodes.Bad_TypeMismatch,
                                                "An abstract numeric array requires one homogeneous"
                                                    + " wire element type");
                                          }
                                          numericElementType = numericElement.getClass();
                                        }
                                      }
                                      if (numericElementType == null) {
                                        numericElementType =
                                            dataTypeTree_.getBackingClass(argumentDataTypeId);
                                      }
                                      if (numericElementType == Number.class
                                          || numericElementType == UNumber.class) {
                                        throw new UaException(
                                            StatusCodes.Bad_TypeMismatch,
                                            "An empty or all-null abstract numeric array requires a"
                                                + " concretely typed array");
                                      }
                                      Object numericArray =
                                          Array.newInstance(
                                              numericElementType, Array.getLength(numericElements));
                                      for (int numericIndex = 0;
                                          numericIndex < Array.getLength(numericElements);
                                          numericIndex++) {
                                        Array.set(
                                            numericArray,
                                            numericIndex,
                                            Array.get(numericElements, numericIndex));
                                      }
                                      if (methodValue instanceof Matrix) {
                                        methodValue =
                                            new Matrix(
                                                numericArray,
                                                ((Matrix) methodValue).getDimensions().clone(),
                                                ((Matrix) methodValue)
                                                    .getDataType()
                                                    .orElseThrow(
                                                        () ->
                                                            new UaException(
                                                                StatusCodes.Bad_TypeMismatch,
                                                                "A numeric Matrix requires an"
                                                                    + " explicit wire DataType")),
                                                ((Matrix) methodValue)
                                                    .getDataTypeId()
                                                    .orElse(null));
                                      } else {
                                        methodValue = numericArray;
                                      }
                                    }
                                    if (methodValue != null) {
                                      Object shapeElements =
                                          methodValue instanceof Matrix
                                              ? ((Matrix) methodValue).getElements()
                                              : methodValue;
                                      int valueRank =
                                          methodValue instanceof Matrix
                                              ? ((Matrix) methodValue).getValueRank()
                                              : ArrayUtil.getValueRank(methodValue);
                                      boolean emptyArray =
                                          methodValue.getClass().isArray()
                                              && ArrayUtil.getValueRank(methodValue) == 1
                                              && Array.getLength(methodValue) == 0;
                                      if (!(valueRank == -1)) {
                                        throw new UaException(
                                            StatusCodes.Bad_TypeMismatch,
                                            "Method argument ValueRank mismatch");
                                      }
                                      if (methodValue instanceof Matrix) {
                                        int[] dimensions = ((Matrix) methodValue).getDimensions();
                                        if (dimensions.length < 2
                                            || !shapeElements.getClass().isArray()
                                            || ArrayUtil.getValueRank(shapeElements) != 1) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "Malformed Method Matrix representation");
                                        }
                                        long elementCount = 1;
                                        for (int dimension : dimensions) {
                                          if (dimension < 0 || elementCount > Integer.MAX_VALUE) {
                                            throw new UaException(
                                                StatusCodes.Bad_TypeMismatch,
                                                "Malformed Method Matrix dimensions");
                                          }
                                          elementCount *= dimension;
                                        }
                                        if (elementCount != Array.getLength(shapeElements)) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "Method Matrix dimensions do not match its elements");
                                        }
                                        if (!(((Matrix) methodValue)
                                            .getDataType()
                                            .equals(Variant.of(shapeElements).getDataType()))) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "Method Matrix DataType does not match its elements");
                                        }
                                      }
                                      Variant.of(shapeElements);
                                    }
                                    if (methodValue != null) {
                                      Object typedElements =
                                          methodValue instanceof Matrix
                                              ? ((Matrix) methodValue).getElements()
                                              : methodValue;
                                      if (NodeIds.Structure.equals(argumentDataTypeId)
                                          || dataTypeTree_.isStructType(argumentDataTypeId)) {
                                        var declaredType =
                                            dataTypeTree_.getType(argumentDataTypeId);
                                        if (typedElements.getClass().isArray()) {
                                          var structureCodec =
                                              this.client
                                                  .getStaticEncodingContext()
                                                  .getDataTypeManager()
                                                  .getCodec(argumentDataTypeId);
                                          Class<?> structureClass =
                                              structureCodec == null
                                                  ? UaStructuredType.class
                                                  : structureCodec.getType();
                                          Object decodedStructures =
                                              Array.newInstance(
                                                  structureClass, Array.getLength(typedElements));
                                          for (int structureIndex = 0;
                                              structureIndex < Array.getLength(typedElements);
                                              structureIndex++) {
                                            Object structure =
                                                Array.get(typedElements, structureIndex);
                                            if (structure instanceof ExtensionObject) {
                                              structure =
                                                  ((ExtensionObject) structure).isNull()
                                                      ? null
                                                      : ((ExtensionObject) structure)
                                                          .decode(
                                                              this.client
                                                                  .getStaticEncodingContext());
                                            }
                                            if (structure != null) {
                                              if (!(structure instanceof UaStructuredType)) {
                                                throw new UaException(
                                                    StatusCodes.Bad_TypeMismatch,
                                                    "Method argument requires a Structure value");
                                              }
                                              if (NodeIds.Structure.equals(argumentDataTypeId)
                                                  || declaredType != null
                                                      && declaredType.isAbstract()) {
                                                if (!dataTypeTree_.isSubtypeOf(
                                                    ((UaStructuredType) structure)
                                                        .getTypeId()
                                                        .toNodeId(namespaceTable)
                                                        .orElse(NodeId.NULL_VALUE),
                                                    argumentDataTypeId)) {
                                                  throw new UaException(
                                                      StatusCodes.Bad_TypeMismatch,
                                                      "Method Structure is not a subtype of the"
                                                          + " effective DataType");
                                                }
                                              } else {
                                                if (!argumentDataTypeId.equals(
                                                    ((UaStructuredType) structure)
                                                        .getTypeId()
                                                        .toNodeId(namespaceTable)
                                                        .orElse(NodeId.NULL_VALUE))) {
                                                  throw new UaException(
                                                      StatusCodes.Bad_TypeMismatch,
                                                      "Method Structure does not match the"
                                                          + " effective DataType");
                                                }
                                              }
                                            }
                                            Array.set(decodedStructures, structureIndex, structure);
                                          }
                                          methodValue =
                                              methodValue instanceof Matrix
                                                  ? new Matrix(
                                                      decodedStructures,
                                                      ((Matrix) methodValue)
                                                          .getDimensions()
                                                          .clone())
                                                  : decodedStructures;
                                        } else {
                                          if (typedElements instanceof ExtensionObject) {
                                            typedElements =
                                                ((ExtensionObject) typedElements).isNull()
                                                    ? null
                                                    : ((ExtensionObject) typedElements)
                                                        .decode(
                                                            this.client.getStaticEncodingContext());
                                          }
                                          if (typedElements != null) {
                                            if (!(typedElements instanceof UaStructuredType)) {
                                              throw new UaException(
                                                  StatusCodes.Bad_TypeMismatch,
                                                  "Method argument requires a Structure value");
                                            }
                                            if (NodeIds.Structure.equals(argumentDataTypeId)
                                                || declaredType != null
                                                    && declaredType.isAbstract()) {
                                              if (!dataTypeTree_.isSubtypeOf(
                                                  ((UaStructuredType) typedElements)
                                                      .getTypeId()
                                                      .toNodeId(namespaceTable)
                                                      .orElse(NodeId.NULL_VALUE),
                                                  argumentDataTypeId)) {
                                                throw new UaException(
                                                    StatusCodes.Bad_TypeMismatch,
                                                    "Method Structure is not a subtype of the"
                                                        + " effective DataType");
                                              }
                                            } else {
                                              if (!argumentDataTypeId.equals(
                                                  ((UaStructuredType) typedElements)
                                                      .getTypeId()
                                                      .toNodeId(namespaceTable)
                                                      .orElse(NodeId.NULL_VALUE))) {
                                                throw new UaException(
                                                    StatusCodes.Bad_TypeMismatch,
                                                    "Method Structure does not match the effective"
                                                        + " DataType");
                                              }
                                            }
                                          }
                                          methodValue = typedElements;
                                        }
                                      } else {
                                        Variant.of(typedElements);
                                        NodeId assignableDataTypeId =
                                            dataTypeTree_.getBackingClass(argumentDataTypeId)
                                                        == Number.class
                                                    && dataTypeTree_.isSubtypeOf(
                                                        argumentDataTypeId, NodeIds.Integer)
                                                ? NodeIds.Integer
                                                : argumentDataTypeId;
                                        if (dataTypeTree_.getBackingClass(argumentDataTypeId)
                                                != Variant.class
                                            && !dataTypeTree_.isAssignable(
                                                assignableDataTypeId,
                                                ArrayUtil.getBoxedType(typedElements))) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "Method argument DataType mismatch");
                                        }
                                      }
                                    }
                                    convertedValue = (LocalizedText) methodValue;
                                  } catch (UaSerializationException conversionFailure) {
                                    throw new UaException(
                                        conversionFailure.getStatusCode().getValue()
                                                == StatusCodes.Bad_OutOfRange
                                            ? StatusCodes.Bad_OutOfRange
                                            : StatusCodes.Bad_TypeMismatch,
                                        conversionFailure);
                                  } catch (ClassCastException
                                      | IllegalArgumentException conversionFailure) {
                                    throw new UaException(
                                        StatusCodes.Bad_TypeMismatch, conversionFailure);
                                  }
                                }
                                try {
                                  Object wireValue = convertedValue;
                                  Object wireElements =
                                      wireValue instanceof Matrix
                                          ? ((Matrix) wireValue).getElements()
                                          : wireValue;
                                  var numericWireValues = new ArrayDeque<Object[]>();
                                  var numericWirePath =
                                      Collections.newSetFromMap(
                                          new IdentityHashMap<Object, Boolean>());
                                  if (wireValue != null) {
                                    numericWireValues.push(new Object[] {wireValue, false});
                                  }
                                  while (!numericWireValues.isEmpty()) {
                                    Object[] numericWireFrame = numericWireValues.pop();
                                    Object numericWireValue = numericWireFrame[0];
                                    if ((Boolean) numericWireFrame[1]) {
                                      numericWirePath.remove(numericWireValue);
                                      continue;
                                    }
                                    while (numericWireValue instanceof Variant
                                        || numericWireValue instanceof DataValue) {
                                      if (numericWireValue instanceof DataValue) {
                                        if (((DataValue) numericWireValue).getValue() == null) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "A DataValue requires a value wrapper; use"
                                                  + " Variant.NULL_VALUE for null");
                                        }
                                        if (((DataValue) numericWireValue).getStatusCode()
                                            == null) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "A DataValue requires a StatusCode; use"
                                                  + " StatusCode.GOOD for Good");
                                        }
                                        numericWireValue =
                                            ((DataValue) numericWireValue).getValue();
                                      } else {
                                        numericWireValue = ((Variant) numericWireValue).getValue();
                                      }
                                    }
                                    if (numericWireValue instanceof Matrix) {
                                      numericWireValue = ((Matrix) numericWireValue).getElements();
                                    }
                                    if (numericWireValue != null
                                        && numericWireValue.getClass().isArray()) {
                                      if (!numericWirePath.add(numericWireValue)) {
                                        throw new UaException(
                                            StatusCodes.Bad_TypeMismatch,
                                            "Cyclic Variant arrays cannot be encoded");
                                      }
                                      numericWireValues.push(new Object[] {numericWireValue, true});
                                      for (int numericWireIndex = 0;
                                          numericWireIndex < Array.getLength(numericWireValue);
                                          numericWireIndex++) {
                                        Object numericWireElement =
                                            Array.get(numericWireValue, numericWireIndex);
                                        if (numericWireElement == null
                                            && ArrayUtil.getBoxedType(numericWireValue)
                                                == Variant.class) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "A Variant wire array requires a wrapper for every"
                                                  + " element; use Variant.NULL_VALUE for null");
                                        }
                                        if (numericWireElement == null
                                            && (UaEnumeratedType.class.isAssignableFrom(
                                                    ArrayUtil.getBoxedType(numericWireValue))
                                                || OptionSetUInteger.class.isAssignableFrom(
                                                    ArrayUtil.getBoxedType(numericWireValue)))) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "An enum or OptionSet wire array cannot encode a null"
                                                  + " element");
                                        }
                                        if (numericWireElement == null
                                            && ArrayUtil.getBoxedType(numericWireValue)
                                                == Boolean.class) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "A Boolean wire array cannot retain a null element;"
                                                  + " Milo encodes it as false");
                                        }
                                        if (numericWireElement == null
                                            && ArrayUtil.getBoxedType(numericWireValue)
                                                == StatusCode.class) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "A StatusCode wire array cannot retain a null"
                                                  + " element; Milo encodes it as Good");
                                        }
                                        if (numericWireElement == null
                                            && Number.class.isAssignableFrom(
                                                ArrayUtil.getBoxedType(numericWireValue))) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "A numeric wire array cannot retain a null element;"
                                                  + " Milo encodes it as zero");
                                        }
                                        if (numericWireElement instanceof Variant
                                            || numericWireElement instanceof DataValue) {
                                          numericWireValues.push(
                                              new Object[] {numericWireElement, false});
                                        }
                                      }
                                    }
                                  }
                                  wireValue =
                                      ExtensionObject.encodeValue(
                                          this.client.getStaticEncodingContext(), wireValue);
                                  encoded3 = Variant.of(wireValue);
                                } catch (UaSerializationException encodingFailure) {
                                  throw new UaException(
                                      encodingFailure.getStatusCode().getValue()
                                              == StatusCodes.Bad_OutOfRange
                                          ? StatusCodes.Bad_OutOfRange
                                          : StatusCodes.Bad_TypeMismatch,
                                      encodingFailure);
                                } catch (ClassCastException
                                    | IllegalArgumentException encodingFailure) {
                                  throw new UaException(
                                      StatusCodes.Bad_TypeMismatch, encodingFailure);
                                }
                              }
                              inputArguments.add(encoded3);
                            }
                            if (rawInputs.size() > 4) {
                              Variant encoded4;
                              {
                                @Nullable Boolean convertedValue;
                                {
                                  Object methodValue = rawInputs.get(4);
                                  try {
                                    if (methodValue instanceof Matrix
                                        && ((Matrix) methodValue).isNull()) {
                                      methodValue = null;
                                    }
                                    NamespaceTable namespaceTable = this.client.getNamespaceTable();
                                    DataTypeTree dataTypeTree_ = dataTypeTree;
                                    NodeId argumentDataTypeId =
                                        ExpandedNodeId.parse("i=1")
                                            .toNodeId(namespaceTable)
                                            .orElseThrow(
                                                () ->
                                                    new UaException(
                                                        StatusCodes.Bad_NodeIdInvalid,
                                                        "Method argument DataType namespace is"
                                                            + " unavailable"));
                                    if (!OpcUaDataType.isBuiltin(argumentDataTypeId)
                                        && dataTypeTree_.getDataType(argumentDataTypeId) == null) {
                                      throw new UaException(
                                          StatusCodes.Bad_TypeMismatch,
                                          "Method argument Restart (effective property i=12884,"
                                              + " DataType i=1) is unavailable in the effective"
                                              + " type tree; resolved DataType: "
                                              + argumentDataTypeId);
                                    }
                                    Object numericElements =
                                        methodValue instanceof Matrix
                                            ? ((Matrix) methodValue).getElements()
                                            : methodValue;
                                    if (numericElements != null
                                        && numericElements.getClass().isArray()
                                        && (numericElements.getClass().getComponentType()
                                                == Number.class
                                            || numericElements.getClass().getComponentType()
                                                == UNumber.class)
                                        && (argumentDataTypeId.equals(NodeIds.Number)
                                            || dataTypeTree_.isSubtypeOf(
                                                argumentDataTypeId, NodeIds.Number))) {
                                      Class<?> numericElementType = null;
                                      for (int numericIndex = 0;
                                          numericIndex < Array.getLength(numericElements);
                                          numericIndex++) {
                                        Object numericElement =
                                            Array.get(numericElements, numericIndex);
                                        if (numericElement != null) {
                                          if (numericElementType != null
                                              && numericElementType != numericElement.getClass()) {
                                            throw new UaException(
                                                StatusCodes.Bad_TypeMismatch,
                                                "An abstract numeric array requires one homogeneous"
                                                    + " wire element type");
                                          }
                                          numericElementType = numericElement.getClass();
                                        }
                                      }
                                      if (numericElementType == null) {
                                        numericElementType =
                                            dataTypeTree_.getBackingClass(argumentDataTypeId);
                                      }
                                      if (numericElementType == Number.class
                                          || numericElementType == UNumber.class) {
                                        throw new UaException(
                                            StatusCodes.Bad_TypeMismatch,
                                            "An empty or all-null abstract numeric array requires a"
                                                + " concretely typed array");
                                      }
                                      Object numericArray =
                                          Array.newInstance(
                                              numericElementType, Array.getLength(numericElements));
                                      for (int numericIndex = 0;
                                          numericIndex < Array.getLength(numericElements);
                                          numericIndex++) {
                                        Array.set(
                                            numericArray,
                                            numericIndex,
                                            Array.get(numericElements, numericIndex));
                                      }
                                      if (methodValue instanceof Matrix) {
                                        methodValue =
                                            new Matrix(
                                                numericArray,
                                                ((Matrix) methodValue).getDimensions().clone(),
                                                ((Matrix) methodValue)
                                                    .getDataType()
                                                    .orElseThrow(
                                                        () ->
                                                            new UaException(
                                                                StatusCodes.Bad_TypeMismatch,
                                                                "A numeric Matrix requires an"
                                                                    + " explicit wire DataType")),
                                                ((Matrix) methodValue)
                                                    .getDataTypeId()
                                                    .orElse(null));
                                      } else {
                                        methodValue = numericArray;
                                      }
                                    }
                                    if (methodValue != null) {
                                      Object shapeElements =
                                          methodValue instanceof Matrix
                                              ? ((Matrix) methodValue).getElements()
                                              : methodValue;
                                      int valueRank =
                                          methodValue instanceof Matrix
                                              ? ((Matrix) methodValue).getValueRank()
                                              : ArrayUtil.getValueRank(methodValue);
                                      boolean emptyArray =
                                          methodValue.getClass().isArray()
                                              && ArrayUtil.getValueRank(methodValue) == 1
                                              && Array.getLength(methodValue) == 0;
                                      if (!(valueRank == -1)) {
                                        throw new UaException(
                                            StatusCodes.Bad_TypeMismatch,
                                            "Method argument ValueRank mismatch");
                                      }
                                      if (methodValue instanceof Matrix) {
                                        int[] dimensions = ((Matrix) methodValue).getDimensions();
                                        if (dimensions.length < 2
                                            || !shapeElements.getClass().isArray()
                                            || ArrayUtil.getValueRank(shapeElements) != 1) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "Malformed Method Matrix representation");
                                        }
                                        long elementCount = 1;
                                        for (int dimension : dimensions) {
                                          if (dimension < 0 || elementCount > Integer.MAX_VALUE) {
                                            throw new UaException(
                                                StatusCodes.Bad_TypeMismatch,
                                                "Malformed Method Matrix dimensions");
                                          }
                                          elementCount *= dimension;
                                        }
                                        if (elementCount != Array.getLength(shapeElements)) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "Method Matrix dimensions do not match its elements");
                                        }
                                        if (!(((Matrix) methodValue)
                                            .getDataType()
                                            .equals(Variant.of(shapeElements).getDataType()))) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "Method Matrix DataType does not match its elements");
                                        }
                                      }
                                      Variant.of(shapeElements);
                                    }
                                    if (methodValue != null) {
                                      Object typedElements =
                                          methodValue instanceof Matrix
                                              ? ((Matrix) methodValue).getElements()
                                              : methodValue;
                                      if (NodeIds.Structure.equals(argumentDataTypeId)
                                          || dataTypeTree_.isStructType(argumentDataTypeId)) {
                                        var declaredType =
                                            dataTypeTree_.getType(argumentDataTypeId);
                                        if (typedElements.getClass().isArray()) {
                                          var structureCodec =
                                              this.client
                                                  .getStaticEncodingContext()
                                                  .getDataTypeManager()
                                                  .getCodec(argumentDataTypeId);
                                          Class<?> structureClass =
                                              structureCodec == null
                                                  ? UaStructuredType.class
                                                  : structureCodec.getType();
                                          Object decodedStructures =
                                              Array.newInstance(
                                                  structureClass, Array.getLength(typedElements));
                                          for (int structureIndex = 0;
                                              structureIndex < Array.getLength(typedElements);
                                              structureIndex++) {
                                            Object structure =
                                                Array.get(typedElements, structureIndex);
                                            if (structure instanceof ExtensionObject) {
                                              structure =
                                                  ((ExtensionObject) structure).isNull()
                                                      ? null
                                                      : ((ExtensionObject) structure)
                                                          .decode(
                                                              this.client
                                                                  .getStaticEncodingContext());
                                            }
                                            if (structure != null) {
                                              if (!(structure instanceof UaStructuredType)) {
                                                throw new UaException(
                                                    StatusCodes.Bad_TypeMismatch,
                                                    "Method argument requires a Structure value");
                                              }
                                              if (NodeIds.Structure.equals(argumentDataTypeId)
                                                  || declaredType != null
                                                      && declaredType.isAbstract()) {
                                                if (!dataTypeTree_.isSubtypeOf(
                                                    ((UaStructuredType) structure)
                                                        .getTypeId()
                                                        .toNodeId(namespaceTable)
                                                        .orElse(NodeId.NULL_VALUE),
                                                    argumentDataTypeId)) {
                                                  throw new UaException(
                                                      StatusCodes.Bad_TypeMismatch,
                                                      "Method Structure is not a subtype of the"
                                                          + " effective DataType");
                                                }
                                              } else {
                                                if (!argumentDataTypeId.equals(
                                                    ((UaStructuredType) structure)
                                                        .getTypeId()
                                                        .toNodeId(namespaceTable)
                                                        .orElse(NodeId.NULL_VALUE))) {
                                                  throw new UaException(
                                                      StatusCodes.Bad_TypeMismatch,
                                                      "Method Structure does not match the"
                                                          + " effective DataType");
                                                }
                                              }
                                            }
                                            Array.set(decodedStructures, structureIndex, structure);
                                          }
                                          methodValue =
                                              methodValue instanceof Matrix
                                                  ? new Matrix(
                                                      decodedStructures,
                                                      ((Matrix) methodValue)
                                                          .getDimensions()
                                                          .clone())
                                                  : decodedStructures;
                                        } else {
                                          if (typedElements instanceof ExtensionObject) {
                                            typedElements =
                                                ((ExtensionObject) typedElements).isNull()
                                                    ? null
                                                    : ((ExtensionObject) typedElements)
                                                        .decode(
                                                            this.client.getStaticEncodingContext());
                                          }
                                          if (typedElements != null) {
                                            if (!(typedElements instanceof UaStructuredType)) {
                                              throw new UaException(
                                                  StatusCodes.Bad_TypeMismatch,
                                                  "Method argument requires a Structure value");
                                            }
                                            if (NodeIds.Structure.equals(argumentDataTypeId)
                                                || declaredType != null
                                                    && declaredType.isAbstract()) {
                                              if (!dataTypeTree_.isSubtypeOf(
                                                  ((UaStructuredType) typedElements)
                                                      .getTypeId()
                                                      .toNodeId(namespaceTable)
                                                      .orElse(NodeId.NULL_VALUE),
                                                  argumentDataTypeId)) {
                                                throw new UaException(
                                                    StatusCodes.Bad_TypeMismatch,
                                                    "Method Structure is not a subtype of the"
                                                        + " effective DataType");
                                              }
                                            } else {
                                              if (!argumentDataTypeId.equals(
                                                  ((UaStructuredType) typedElements)
                                                      .getTypeId()
                                                      .toNodeId(namespaceTable)
                                                      .orElse(NodeId.NULL_VALUE))) {
                                                throw new UaException(
                                                    StatusCodes.Bad_TypeMismatch,
                                                    "Method Structure does not match the effective"
                                                        + " DataType");
                                              }
                                            }
                                          }
                                          methodValue = typedElements;
                                        }
                                      } else {
                                        Variant.of(typedElements);
                                        NodeId assignableDataTypeId =
                                            dataTypeTree_.getBackingClass(argumentDataTypeId)
                                                        == Number.class
                                                    && dataTypeTree_.isSubtypeOf(
                                                        argumentDataTypeId, NodeIds.Integer)
                                                ? NodeIds.Integer
                                                : argumentDataTypeId;
                                        if (dataTypeTree_.getBackingClass(argumentDataTypeId)
                                                != Variant.class
                                            && !dataTypeTree_.isAssignable(
                                                assignableDataTypeId,
                                                ArrayUtil.getBoxedType(typedElements))) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "Method argument DataType mismatch");
                                        }
                                      }
                                    }
                                    convertedValue = (Boolean) methodValue;
                                  } catch (UaSerializationException conversionFailure) {
                                    throw new UaException(
                                        conversionFailure.getStatusCode().getValue()
                                                == StatusCodes.Bad_OutOfRange
                                            ? StatusCodes.Bad_OutOfRange
                                            : StatusCodes.Bad_TypeMismatch,
                                        conversionFailure);
                                  } catch (ClassCastException
                                      | IllegalArgumentException conversionFailure) {
                                    throw new UaException(
                                        StatusCodes.Bad_TypeMismatch, conversionFailure);
                                  }
                                }
                                try {
                                  Object wireValue = convertedValue;
                                  Object wireElements =
                                      wireValue instanceof Matrix
                                          ? ((Matrix) wireValue).getElements()
                                          : wireValue;
                                  var numericWireValues = new ArrayDeque<Object[]>();
                                  var numericWirePath =
                                      Collections.newSetFromMap(
                                          new IdentityHashMap<Object, Boolean>());
                                  if (wireValue != null) {
                                    numericWireValues.push(new Object[] {wireValue, false});
                                  }
                                  while (!numericWireValues.isEmpty()) {
                                    Object[] numericWireFrame = numericWireValues.pop();
                                    Object numericWireValue = numericWireFrame[0];
                                    if ((Boolean) numericWireFrame[1]) {
                                      numericWirePath.remove(numericWireValue);
                                      continue;
                                    }
                                    while (numericWireValue instanceof Variant
                                        || numericWireValue instanceof DataValue) {
                                      if (numericWireValue instanceof DataValue) {
                                        if (((DataValue) numericWireValue).getValue() == null) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "A DataValue requires a value wrapper; use"
                                                  + " Variant.NULL_VALUE for null");
                                        }
                                        if (((DataValue) numericWireValue).getStatusCode()
                                            == null) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "A DataValue requires a StatusCode; use"
                                                  + " StatusCode.GOOD for Good");
                                        }
                                        numericWireValue =
                                            ((DataValue) numericWireValue).getValue();
                                      } else {
                                        numericWireValue = ((Variant) numericWireValue).getValue();
                                      }
                                    }
                                    if (numericWireValue instanceof Matrix) {
                                      numericWireValue = ((Matrix) numericWireValue).getElements();
                                    }
                                    if (numericWireValue != null
                                        && numericWireValue.getClass().isArray()) {
                                      if (!numericWirePath.add(numericWireValue)) {
                                        throw new UaException(
                                            StatusCodes.Bad_TypeMismatch,
                                            "Cyclic Variant arrays cannot be encoded");
                                      }
                                      numericWireValues.push(new Object[] {numericWireValue, true});
                                      for (int numericWireIndex = 0;
                                          numericWireIndex < Array.getLength(numericWireValue);
                                          numericWireIndex++) {
                                        Object numericWireElement =
                                            Array.get(numericWireValue, numericWireIndex);
                                        if (numericWireElement == null
                                            && ArrayUtil.getBoxedType(numericWireValue)
                                                == Variant.class) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "A Variant wire array requires a wrapper for every"
                                                  + " element; use Variant.NULL_VALUE for null");
                                        }
                                        if (numericWireElement == null
                                            && (UaEnumeratedType.class.isAssignableFrom(
                                                    ArrayUtil.getBoxedType(numericWireValue))
                                                || OptionSetUInteger.class.isAssignableFrom(
                                                    ArrayUtil.getBoxedType(numericWireValue)))) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "An enum or OptionSet wire array cannot encode a null"
                                                  + " element");
                                        }
                                        if (numericWireElement == null
                                            && ArrayUtil.getBoxedType(numericWireValue)
                                                == Boolean.class) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "A Boolean wire array cannot retain a null element;"
                                                  + " Milo encodes it as false");
                                        }
                                        if (numericWireElement == null
                                            && ArrayUtil.getBoxedType(numericWireValue)
                                                == StatusCode.class) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "A StatusCode wire array cannot retain a null"
                                                  + " element; Milo encodes it as Good");
                                        }
                                        if (numericWireElement == null
                                            && Number.class.isAssignableFrom(
                                                ArrayUtil.getBoxedType(numericWireValue))) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "A numeric wire array cannot retain a null element;"
                                                  + " Milo encodes it as zero");
                                        }
                                        if (numericWireElement instanceof Variant
                                            || numericWireElement instanceof DataValue) {
                                          numericWireValues.push(
                                              new Object[] {numericWireElement, false});
                                        }
                                      }
                                    }
                                  }
                                  wireValue =
                                      ExtensionObject.encodeValue(
                                          this.client.getStaticEncodingContext(), wireValue);
                                  encoded4 = Variant.of(wireValue);
                                } catch (UaSerializationException encodingFailure) {
                                  throw new UaException(
                                      encodingFailure.getStatusCode().getValue()
                                              == StatusCodes.Bad_OutOfRange
                                          ? StatusCodes.Bad_OutOfRange
                                          : StatusCodes.Bad_TypeMismatch,
                                      encodingFailure);
                                } catch (ClassCastException
                                    | IllegalArgumentException encodingFailure) {
                                  throw new UaException(
                                      StatusCodes.Bad_TypeMismatch, encodingFailure);
                                }
                              }
                              inputArguments.add(encoded4);
                            }
                            CallMethodRequest request =
                                new CallMethodRequest(
                                    getNodeId(),
                                    methodNode.getNodeId(),
                                    inputArguments.toArray(new Variant[0]));
                            return this.client
                                .getSessionAsync()
                                .thenCompose(
                                    session -> {
                                      if (result.isCancelled()) {
                                        return CompletableFuture.failedFuture(
                                            new CancellationException());
                                      }
                                      RequestHeader originalHeader =
                                          this.client.newRequestHeader(
                                              session.getAuthenticationToken());
                                      RequestHeader requestHeader =
                                          new RequestHeader(
                                              originalHeader.getAuthenticationToken(),
                                              originalHeader.getTimestamp(),
                                              originalHeader.getRequestHandle(),
                                              options.returnDiagnostics(),
                                              originalHeader.getAuditEntryId(),
                                              originalHeader.getTimeoutHint(),
                                              originalHeader.getAdditionalHeader());
                                      var call =
                                          this.client.sendRequestAsync(
                                              new CallRequest(
                                                  requestHeader,
                                                  new CallMethodRequest[] {request}));
                                      result.whenComplete(
                                          (value, failure) -> {
                                            if (result.isCancelled()) {
                                              call.cancel(false);
                                            }
                                          });
                                      return call.thenCompose(
                                          message -> {
                                            if (result.isCancelled()) {
                                              throw new CancellationException();
                                            }
                                            try {
                                              if (!(message instanceof CallResponse response)
                                                  || response.getResponseHeader() == null
                                                  || response.getResponseHeader().getServiceResult()
                                                      == null) {
                                                throw new UaException(
                                                    StatusCodes.Bad_UnexpectedError,
                                                    "Malformed Call response header");
                                              }
                                              if (!response
                                                  .getResponseHeader()
                                                  .getServiceResult()
                                                  .isGood()) {
                                                throw new UaException(
                                                    response
                                                        .getResponseHeader()
                                                        .getServiceResult());
                                              }
                                              if (response.getResults() == null
                                                  || response.getResults().length != 1
                                                  || response.getResults()[0] == null
                                                  || response.getResults()[0].getStatusCode()
                                                      == null) {
                                                throw new UaException(
                                                    StatusCodes.Bad_UnexpectedError,
                                                    "Expected exactly one Call operation result");
                                              }
                                              CompletableFuture<DataTypeTree> outputMetadata =
                                                  response.getResults()[0].getStatusCode().isBad()
                                                      ? CompletableFuture.completedFuture(
                                                          dataTypeTree)
                                                      : ClientDataTypes.read(
                                                          this.client,
                                                          List.<ExpandedNodeId>of(),
                                                          (Object)
                                                              response.getResults()[0]
                                                                  .getOutputArguments());
                                              result.whenComplete(
                                                  (cancelledValue, cancelledFailure) -> {
                                                    if (result.isCancelled()) {
                                                      outputMetadata.cancel(false);
                                                    }
                                                  });
                                              return outputMetadata.handle(
                                                  (outputDataTypeTree, metadataFailure) -> {
                                                    if (result.isCancelled()) {
                                                      throw new CancellationException();
                                                    }
                                                    try {
                                                      return MethodCallResult.decode(
                                                          request,
                                                          requestHeader,
                                                          response,
                                                          0,
                                                          outputArguments -> {
                                                            if (metadataFailure != null) {
                                                              var
                                                                  checkedMetadataFailuremetadataFailure =
                                                                      UaException.extract(
                                                                          metadataFailure);
                                                              if (checkedMetadataFailuremetadataFailure
                                                                  .isPresent()) {
                                                                throw checkedMetadataFailuremetadataFailure
                                                                    .orElseThrow();
                                                              }
                                                              Throwable
                                                                  metadataCausemetadataFailure =
                                                                      metadataFailure;
                                                              while (metadataCausemetadataFailure
                                                                  != null) {
                                                                if (metadataCausemetadataFailure
                                                                    instanceof
                                                                    UaSerializationException
                                                                        metadataCodecmetadataFailure) {
                                                                  throw new UaException(
                                                                      metadataCodecmetadataFailure
                                                                                  .getStatusCode()
                                                                                  .getValue()
                                                                              == StatusCodes
                                                                                  .Bad_OutOfRange
                                                                          ? StatusCodes
                                                                              .Bad_OutOfRange
                                                                          : StatusCodes
                                                                              .Bad_TypeMismatch,
                                                                      metadataCodecmetadataFailure);
                                                                }
                                                                metadataCausemetadataFailure =
                                                                    metadataCausemetadataFailure
                                                                        .getCause();
                                                              }
                                                              throw UaException.extract(
                                                                      metadataFailure)
                                                                  .orElseGet(
                                                                      () ->
                                                                          new UaException(
                                                                              metadataFailure));
                                                            }
                                                            if ((outputArguments == null
                                                                    ? 0
                                                                    : outputArguments.length)
                                                                != 0) {
                                                              throw new UaException(
                                                                  StatusCodes.Bad_TypeMismatch,
                                                                  "Unexpected Method output count");
                                                            }
                                                            return null;
                                                          });
                                                    } catch (UaException failure) {
                                                      throw new CompletionException(failure);
                                                    }
                                                  });
                                            } catch (Exception failure) {
                                              return CompletableFuture.failedFuture(failure);
                                            }
                                          });
                                    });
                          } catch (Exception failure) {
                            return CompletableFuture.failedFuture(failure);
                          }
                        });
              });
      pipeline.whenComplete(
          (value, failure) -> {
            if (failure == null) {
              result.complete(value);
            } else {
              result.completeExceptionally(failure);
            }
          });
    } catch (RuntimeException failure) {
      result.completeExceptionally(failure);
    }
    return result;
  }
}

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
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.core.ValueRanks;
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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowsePath;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetFieldContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.FieldTargetDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.KeyValuePair;
import org.eclipse.milo.opcua.stack.core.types.structured.RelativePath;
import org.eclipse.milo.opcua.stack.core.types.structured.RelativePathElement;
import org.eclipse.milo.opcua.stack.core.types.structured.RequestHeader;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.ArrayUtil;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

public class DataSetReaderTypeNode extends BaseObjectTypeNode implements DataSetReaderType {
  public DataSetReaderTypeNode(
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
  public static ClientViews createViews(DataSetReaderTypeNode node) {
    Objects.requireNonNull(node, "node");
    return ClientViews.forNode(node.client, node);
  }

  @Override
  public @Nullable Object getPublisherId() throws UaException {
    PropertyTypeNode node = getPublisherIdNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:PublisherId (declaration i=21097, owner i=15306)"
              + " on "
              + getNodeId());
    }
    return (Object) node.getValue().getValue().getValue();
  }

  @Override
  public void setPublisherId(@Nullable Object value) throws UaException {
    PropertyTypeNode node = getPublisherIdNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:PublisherId (declaration i=21097, owner i=15306)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable Object readPublisherId() throws UaException {
    try {
      return readPublisherIdAsync().get();
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
  public void writePublisherId(@Nullable Object value) throws UaException {
    try {
      StatusCode statusCode = writePublisherIdAsync(value).get();
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
  public CompletableFuture<? extends @Nullable Object> readPublisherIdAsync() {
    return getPublisherIdNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:PublisherId (declaration i=21097, owner"
                            + " i=15306) on "
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
                return (Object) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writePublisherIdAsync(@Nullable Object publisherId) {
    return getPublisherIdNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:PublisherId (declaration i=21097, owner"
                            + " i=15306) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(publisherId));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getPublisherIdNode() throws UaException {
    try {
      return getPublisherIdNodeAsync().get();
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
  public CompletableFuture<? extends PropertyTypeNode> getPublisherIdNodeAsync() {
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
                                "http://opcfoundation.org/UA/:PublisherId (declaration i=21097,"
                                    + " owner i=15306) on "
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
                                        new QualifiedName(namespaceIndex, "PublisherId"))
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
                                          "http://opcfoundation.org/UA/:PublisherId (declaration"
                                              + " i=21097, owner i=15306) on "
                                              + getNodeId()));
                                }
                                var operation = results[0];
                                if (operation.getStatusCode().getValue()
                                    == StatusCodes.Bad_NoMatch) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_NotFound,
                                          "http://opcfoundation.org/UA/:PublisherId (declaration"
                                              + " i=21097, owner i=15306) on "
                                              + getNodeId()));
                                }
                                if (!operation.getStatusCode().isGood()) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          operation.getStatusCode(),
                                          "http://opcfoundation.org/UA/:PublisherId (declaration"
                                              + " i=21097, owner i=15306)"));
                                }
                                var targets = operation.getTargets();
                                if (targets == null || targets.length == 0) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_UnexpectedError,
                                          "http://opcfoundation.org/UA/:PublisherId (declaration"
                                              + " i=21097, owner i=15306) on "
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
                                            "http://opcfoundation.org/UA/:PublisherId (declaration"
                                                + " i=21097, owner i=15306) on "
                                                + getNodeId()));
                                  }
                                  if (!target.getTargetId().isLocal()) {
                                    return CompletableFuture.failedFuture(
                                        new UaException(
                                            StatusCodes.Bad_NotSupported,
                                            "http://opcfoundation.org/UA/:PublisherId (declaration"
                                                + " i=21097, owner i=15306) on "
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
                                                            "http://opcfoundation.org/UA/:PublisherId"
                                                                + " (declaration i=21097, owner"
                                                                + " i=15306) on "
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
                                                    "http://opcfoundation.org/UA/:PublisherId"
                                                        + " (declaration i=21097, owner i=15306) on"
                                                        + " "
                                                        + getNodeId()));
                                          }
                                          if (unique.size() != 1) {
                                            return CompletableFuture.failedFuture(
                                                new UaException(
                                                    StatusCodes.Bad_TooManyMatches,
                                                    "http://opcfoundation.org/UA/:PublisherId"
                                                        + " (declaration i=21097, owner i=15306) on"
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
                                                              "http://opcfoundation.org/UA/:PublisherId"
                                                                  + " (declaration i=21097, owner"
                                                                  + " i=15306) on "
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
                      "http://opcfoundation.org/UA/:PublisherId (declaration i=21097, owner"
                          + " i=15306)"));
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
  public @Nullable UShort getWriterGroupId() throws UaException {
    PropertyTypeNode node = getWriterGroupIdNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:WriterGroupId (declaration i=21098, owner i=15306)"
              + " on "
              + getNodeId());
    }
    return (UShort) node.getValue().getValue().getValue();
  }

  @Override
  public void setWriterGroupId(@Nullable UShort value) throws UaException {
    PropertyTypeNode node = getWriterGroupIdNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:WriterGroupId (declaration i=21098, owner i=15306)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UShort readWriterGroupId() throws UaException {
    try {
      return readWriterGroupIdAsync().get();
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
  public void writeWriterGroupId(@Nullable UShort value) throws UaException {
    try {
      StatusCode statusCode = writeWriterGroupIdAsync(value).get();
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
  public CompletableFuture<? extends @Nullable UShort> readWriterGroupIdAsync() {
    return getWriterGroupIdNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:WriterGroupId (declaration i=21098, owner"
                            + " i=15306) on "
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
  public CompletableFuture<StatusCode> writeWriterGroupIdAsync(@Nullable UShort writerGroupId) {
    return getWriterGroupIdNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:WriterGroupId (declaration i=21098, owner"
                            + " i=15306) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(writerGroupId));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getWriterGroupIdNode() throws UaException {
    try {
      return getWriterGroupIdNodeAsync().get();
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
  public CompletableFuture<? extends PropertyTypeNode> getWriterGroupIdNodeAsync() {
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
                                "http://opcfoundation.org/UA/:WriterGroupId (declaration i=21098,"
                                    + " owner i=15306) on "
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
                                        new QualifiedName(namespaceIndex, "WriterGroupId"))
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
                                          "http://opcfoundation.org/UA/:WriterGroupId (declaration"
                                              + " i=21098, owner i=15306) on "
                                              + getNodeId()));
                                }
                                var operation = results[0];
                                if (operation.getStatusCode().getValue()
                                    == StatusCodes.Bad_NoMatch) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_NotFound,
                                          "http://opcfoundation.org/UA/:WriterGroupId (declaration"
                                              + " i=21098, owner i=15306) on "
                                              + getNodeId()));
                                }
                                if (!operation.getStatusCode().isGood()) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          operation.getStatusCode(),
                                          "http://opcfoundation.org/UA/:WriterGroupId (declaration"
                                              + " i=21098, owner i=15306)"));
                                }
                                var targets = operation.getTargets();
                                if (targets == null || targets.length == 0) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_UnexpectedError,
                                          "http://opcfoundation.org/UA/:WriterGroupId (declaration"
                                              + " i=21098, owner i=15306) on "
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
                                            "http://opcfoundation.org/UA/:WriterGroupId"
                                                + " (declaration i=21098, owner i=15306) on "
                                                + getNodeId()));
                                  }
                                  if (!target.getTargetId().isLocal()) {
                                    return CompletableFuture.failedFuture(
                                        new UaException(
                                            StatusCodes.Bad_NotSupported,
                                            "http://opcfoundation.org/UA/:WriterGroupId"
                                                + " (declaration i=21098, owner i=15306) on "
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
                                                            "http://opcfoundation.org/UA/:WriterGroupId"
                                                                + " (declaration i=21098, owner"
                                                                + " i=15306) on "
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
                                                    "http://opcfoundation.org/UA/:WriterGroupId"
                                                        + " (declaration i=21098, owner i=15306) on"
                                                        + " "
                                                        + getNodeId()));
                                          }
                                          if (unique.size() != 1) {
                                            return CompletableFuture.failedFuture(
                                                new UaException(
                                                    StatusCodes.Bad_TooManyMatches,
                                                    "http://opcfoundation.org/UA/:WriterGroupId"
                                                        + " (declaration i=21098, owner i=15306) on"
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
                                                              "http://opcfoundation.org/UA/:WriterGroupId"
                                                                  + " (declaration i=21098, owner"
                                                                  + " i=15306) on "
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
                      "http://opcfoundation.org/UA/:WriterGroupId (declaration i=21098, owner"
                          + " i=15306)"));
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
  public @Nullable UShort getDataSetWriterId() throws UaException {
    PropertyTypeNode node = getDataSetWriterIdNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DataSetWriterId (declaration i=21099, owner i=15306)"
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
          "http://opcfoundation.org/UA/:DataSetWriterId (declaration i=21099, owner i=15306)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UShort readDataSetWriterId() throws UaException {
    try {
      return readDataSetWriterIdAsync().get();
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
                        "http://opcfoundation.org/UA/:DataSetWriterId (declaration i=21099, owner"
                            + " i=15306) on "
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
                        "http://opcfoundation.org/UA/:DataSetWriterId (declaration i=21099, owner"
                            + " i=15306) on "
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
    try {
      return getDataSetWriterIdNodeAsync().get();
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
  public CompletableFuture<? extends PropertyTypeNode> getDataSetWriterIdNodeAsync() {
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
                                "http://opcfoundation.org/UA/:DataSetWriterId (declaration i=21099,"
                                    + " owner i=15306) on "
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
                                        new QualifiedName(namespaceIndex, "DataSetWriterId"))
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
                                          "http://opcfoundation.org/UA/:DataSetWriterId"
                                              + " (declaration i=21099, owner i=15306) on "
                                              + getNodeId()));
                                }
                                var operation = results[0];
                                if (operation.getStatusCode().getValue()
                                    == StatusCodes.Bad_NoMatch) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_NotFound,
                                          "http://opcfoundation.org/UA/:DataSetWriterId"
                                              + " (declaration i=21099, owner i=15306) on "
                                              + getNodeId()));
                                }
                                if (!operation.getStatusCode().isGood()) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          operation.getStatusCode(),
                                          "http://opcfoundation.org/UA/:DataSetWriterId"
                                              + " (declaration i=21099, owner i=15306)"));
                                }
                                var targets = operation.getTargets();
                                if (targets == null || targets.length == 0) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_UnexpectedError,
                                          "http://opcfoundation.org/UA/:DataSetWriterId"
                                              + " (declaration i=21099, owner i=15306) on "
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
                                            "http://opcfoundation.org/UA/:DataSetWriterId"
                                                + " (declaration i=21099, owner i=15306) on "
                                                + getNodeId()));
                                  }
                                  if (!target.getTargetId().isLocal()) {
                                    return CompletableFuture.failedFuture(
                                        new UaException(
                                            StatusCodes.Bad_NotSupported,
                                            "http://opcfoundation.org/UA/:DataSetWriterId"
                                                + " (declaration i=21099, owner i=15306) on "
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
                                                            "http://opcfoundation.org/UA/:DataSetWriterId"
                                                                + " (declaration i=21099, owner"
                                                                + " i=15306) on "
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
                                                    "http://opcfoundation.org/UA/:DataSetWriterId"
                                                        + " (declaration i=21099, owner i=15306) on"
                                                        + " "
                                                        + getNodeId()));
                                          }
                                          if (unique.size() != 1) {
                                            return CompletableFuture.failedFuture(
                                                new UaException(
                                                    StatusCodes.Bad_TooManyMatches,
                                                    "http://opcfoundation.org/UA/:DataSetWriterId"
                                                        + " (declaration i=21099, owner i=15306) on"
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
                                                              "http://opcfoundation.org/UA/:DataSetWriterId"
                                                                  + " (declaration i=21099, owner"
                                                                  + " i=15306) on "
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
                      "http://opcfoundation.org/UA/:DataSetWriterId (declaration i=21099, owner"
                          + " i=15306)"));
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
  public @Nullable DataSetMetaDataType getDataSetMetaData() throws UaException {
    PropertyTypeNode node = getDataSetMetaDataNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DataSetMetaData (declaration i=21100, owner i=15306)"
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
          "http://opcfoundation.org/UA/:DataSetMetaData (declaration i=21100, owner i=15306)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(encodeValue(value, DataSetMetaDataType.class, ValueRanks.Scalar)));
  }

  @Override
  public @Nullable DataSetMetaDataType readDataSetMetaData() throws UaException {
    try {
      return readDataSetMetaDataAsync().get();
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
                        "http://opcfoundation.org/UA/:DataSetMetaData (declaration i=21100, owner"
                            + " i=15306) on "
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
                        "http://opcfoundation.org/UA/:DataSetMetaData (declaration i=21100, owner"
                            + " i=15306) on "
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
    try {
      return getDataSetMetaDataNodeAsync().get();
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
  public CompletableFuture<? extends PropertyTypeNode> getDataSetMetaDataNodeAsync() {
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
                                "http://opcfoundation.org/UA/:DataSetMetaData (declaration i=21100,"
                                    + " owner i=15306) on "
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
                                        new QualifiedName(namespaceIndex, "DataSetMetaData"))
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
                                          "http://opcfoundation.org/UA/:DataSetMetaData"
                                              + " (declaration i=21100, owner i=15306) on "
                                              + getNodeId()));
                                }
                                var operation = results[0];
                                if (operation.getStatusCode().getValue()
                                    == StatusCodes.Bad_NoMatch) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_NotFound,
                                          "http://opcfoundation.org/UA/:DataSetMetaData"
                                              + " (declaration i=21100, owner i=15306) on "
                                              + getNodeId()));
                                }
                                if (!operation.getStatusCode().isGood()) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          operation.getStatusCode(),
                                          "http://opcfoundation.org/UA/:DataSetMetaData"
                                              + " (declaration i=21100, owner i=15306)"));
                                }
                                var targets = operation.getTargets();
                                if (targets == null || targets.length == 0) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_UnexpectedError,
                                          "http://opcfoundation.org/UA/:DataSetMetaData"
                                              + " (declaration i=21100, owner i=15306) on "
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
                                            "http://opcfoundation.org/UA/:DataSetMetaData"
                                                + " (declaration i=21100, owner i=15306) on "
                                                + getNodeId()));
                                  }
                                  if (!target.getTargetId().isLocal()) {
                                    return CompletableFuture.failedFuture(
                                        new UaException(
                                            StatusCodes.Bad_NotSupported,
                                            "http://opcfoundation.org/UA/:DataSetMetaData"
                                                + " (declaration i=21100, owner i=15306) on "
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
                                                            "http://opcfoundation.org/UA/:DataSetMetaData"
                                                                + " (declaration i=21100, owner"
                                                                + " i=15306) on "
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
                                                    "http://opcfoundation.org/UA/:DataSetMetaData"
                                                        + " (declaration i=21100, owner i=15306) on"
                                                        + " "
                                                        + getNodeId()));
                                          }
                                          if (unique.size() != 1) {
                                            return CompletableFuture.failedFuture(
                                                new UaException(
                                                    StatusCodes.Bad_TooManyMatches,
                                                    "http://opcfoundation.org/UA/:DataSetMetaData"
                                                        + " (declaration i=21100, owner i=15306) on"
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
                                                              "http://opcfoundation.org/UA/:DataSetMetaData"
                                                                  + " (declaration i=21100, owner"
                                                                  + " i=15306) on "
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
                      "http://opcfoundation.org/UA/:DataSetMetaData (declaration i=21100, owner"
                          + " i=15306)"));
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
  public @Nullable DataSetFieldContentMask getDataSetFieldContentMask() throws UaException {
    PropertyTypeNode node = getDataSetFieldContentMaskNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DataSetFieldContentMask (declaration i=21101, owner"
              + " i=15306) on "
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
          "http://opcfoundation.org/UA/:DataSetFieldContentMask (declaration i=21101, owner"
              + " i=15306) on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable DataSetFieldContentMask readDataSetFieldContentMask() throws UaException {
    try {
      return readDataSetFieldContentMaskAsync().get();
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
                        "http://opcfoundation.org/UA/:DataSetFieldContentMask (declaration i=21101,"
                            + " owner i=15306) on "
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
                        "http://opcfoundation.org/UA/:DataSetFieldContentMask (declaration i=21101,"
                            + " owner i=15306) on "
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
    try {
      return getDataSetFieldContentMaskNodeAsync().get();
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
  public CompletableFuture<? extends PropertyTypeNode> getDataSetFieldContentMaskNodeAsync() {
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
                                "http://opcfoundation.org/UA/:DataSetFieldContentMask (declaration"
                                    + " i=21101, owner i=15306) on "
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
                                            namespaceIndex, "DataSetFieldContentMask"))
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
                                          "http://opcfoundation.org/UA/:DataSetFieldContentMask"
                                              + " (declaration i=21101, owner i=15306) on "
                                              + getNodeId()));
                                }
                                var operation = results[0];
                                if (operation.getStatusCode().getValue()
                                    == StatusCodes.Bad_NoMatch) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_NotFound,
                                          "http://opcfoundation.org/UA/:DataSetFieldContentMask"
                                              + " (declaration i=21101, owner i=15306) on "
                                              + getNodeId()));
                                }
                                if (!operation.getStatusCode().isGood()) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          operation.getStatusCode(),
                                          "http://opcfoundation.org/UA/:DataSetFieldContentMask"
                                              + " (declaration i=21101, owner i=15306)"));
                                }
                                var targets = operation.getTargets();
                                if (targets == null || targets.length == 0) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_UnexpectedError,
                                          "http://opcfoundation.org/UA/:DataSetFieldContentMask"
                                              + " (declaration i=21101, owner i=15306) on "
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
                                            "http://opcfoundation.org/UA/:DataSetFieldContentMask"
                                                + " (declaration i=21101, owner i=15306) on "
                                                + getNodeId()));
                                  }
                                  if (!target.getTargetId().isLocal()) {
                                    return CompletableFuture.failedFuture(
                                        new UaException(
                                            StatusCodes.Bad_NotSupported,
                                            "http://opcfoundation.org/UA/:DataSetFieldContentMask"
                                                + " (declaration i=21101, owner i=15306) on "
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
                                                            "http://opcfoundation.org/UA/:DataSetFieldContentMask"
                                                                + " (declaration i=21101, owner"
                                                                + " i=15306) on "
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
                                                    "http://opcfoundation.org/UA/:DataSetFieldContentMask"
                                                        + " (declaration i=21101, owner i=15306) on"
                                                        + " "
                                                        + getNodeId()));
                                          }
                                          if (unique.size() != 1) {
                                            return CompletableFuture.failedFuture(
                                                new UaException(
                                                    StatusCodes.Bad_TooManyMatches,
                                                    "http://opcfoundation.org/UA/:DataSetFieldContentMask"
                                                        + " (declaration i=21101, owner i=15306) on"
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
                                                              "http://opcfoundation.org/UA/:DataSetFieldContentMask"
                                                                  + " (declaration i=21101, owner"
                                                                  + " i=15306) on "
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
                      "http://opcfoundation.org/UA/:DataSetFieldContentMask (declaration i=21101,"
                          + " owner i=15306)"));
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
  public @Nullable Double getMessageReceiveTimeout() throws UaException {
    PropertyTypeNode node = getMessageReceiveTimeoutNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MessageReceiveTimeout (declaration i=21102, owner i=15306)"
              + " on "
              + getNodeId());
    }
    return (Double) node.getValue().getValue().getValue();
  }

  @Override
  public void setMessageReceiveTimeout(@Nullable Double value) throws UaException {
    PropertyTypeNode node = getMessageReceiveTimeoutNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MessageReceiveTimeout (declaration i=21102, owner i=15306)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable Double readMessageReceiveTimeout() throws UaException {
    try {
      return readMessageReceiveTimeoutAsync().get();
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
  public void writeMessageReceiveTimeout(@Nullable Double value) throws UaException {
    try {
      StatusCode statusCode = writeMessageReceiveTimeoutAsync(value).get();
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
  public CompletableFuture<? extends @Nullable Double> readMessageReceiveTimeoutAsync() {
    return getMessageReceiveTimeoutNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MessageReceiveTimeout (declaration i=21102,"
                            + " owner i=15306) on "
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
  public CompletableFuture<StatusCode> writeMessageReceiveTimeoutAsync(
      @Nullable Double messageReceiveTimeout) {
    return getMessageReceiveTimeoutNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MessageReceiveTimeout (declaration i=21102,"
                            + " owner i=15306) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(messageReceiveTimeout));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getMessageReceiveTimeoutNode() throws UaException {
    try {
      return getMessageReceiveTimeoutNodeAsync().get();
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
  public CompletableFuture<? extends PropertyTypeNode> getMessageReceiveTimeoutNodeAsync() {
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
                                "http://opcfoundation.org/UA/:MessageReceiveTimeout (declaration"
                                    + " i=21102, owner i=15306) on "
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
                                        new QualifiedName(namespaceIndex, "MessageReceiveTimeout"))
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
                                          "http://opcfoundation.org/UA/:MessageReceiveTimeout"
                                              + " (declaration i=21102, owner i=15306) on "
                                              + getNodeId()));
                                }
                                var operation = results[0];
                                if (operation.getStatusCode().getValue()
                                    == StatusCodes.Bad_NoMatch) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_NotFound,
                                          "http://opcfoundation.org/UA/:MessageReceiveTimeout"
                                              + " (declaration i=21102, owner i=15306) on "
                                              + getNodeId()));
                                }
                                if (!operation.getStatusCode().isGood()) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          operation.getStatusCode(),
                                          "http://opcfoundation.org/UA/:MessageReceiveTimeout"
                                              + " (declaration i=21102, owner i=15306)"));
                                }
                                var targets = operation.getTargets();
                                if (targets == null || targets.length == 0) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_UnexpectedError,
                                          "http://opcfoundation.org/UA/:MessageReceiveTimeout"
                                              + " (declaration i=21102, owner i=15306) on "
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
                                            "http://opcfoundation.org/UA/:MessageReceiveTimeout"
                                                + " (declaration i=21102, owner i=15306) on "
                                                + getNodeId()));
                                  }
                                  if (!target.getTargetId().isLocal()) {
                                    return CompletableFuture.failedFuture(
                                        new UaException(
                                            StatusCodes.Bad_NotSupported,
                                            "http://opcfoundation.org/UA/:MessageReceiveTimeout"
                                                + " (declaration i=21102, owner i=15306) on "
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
                                                            "http://opcfoundation.org/UA/:MessageReceiveTimeout"
                                                                + " (declaration i=21102, owner"
                                                                + " i=15306) on "
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
                                                    "http://opcfoundation.org/UA/:MessageReceiveTimeout"
                                                        + " (declaration i=21102, owner i=15306) on"
                                                        + " "
                                                        + getNodeId()));
                                          }
                                          if (unique.size() != 1) {
                                            return CompletableFuture.failedFuture(
                                                new UaException(
                                                    StatusCodes.Bad_TooManyMatches,
                                                    "http://opcfoundation.org/UA/:MessageReceiveTimeout"
                                                        + " (declaration i=21102, owner i=15306) on"
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
                                                              "http://opcfoundation.org/UA/:MessageReceiveTimeout"
                                                                  + " (declaration i=21102, owner"
                                                                  + " i=15306) on "
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
                      "http://opcfoundation.org/UA/:MessageReceiveTimeout (declaration i=21102,"
                          + " owner i=15306)"));
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
  public @Nullable UInteger getKeyFrameCount() throws UaException {
    PropertyTypeNode node = getKeyFrameCountNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:KeyFrameCount (declaration i=17563, owner i=15306)"
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
          "http://opcfoundation.org/UA/:KeyFrameCount (declaration i=17563, owner i=15306)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UInteger readKeyFrameCount() throws UaException {
    try {
      return readKeyFrameCountAsync().get();
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
                        "http://opcfoundation.org/UA/:KeyFrameCount (declaration i=17563, owner"
                            + " i=15306) on "
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
                        "http://opcfoundation.org/UA/:KeyFrameCount (declaration i=17563, owner"
                            + " i=15306) on "
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
  public PropertyTypeNode getKeyFrameCountNode() throws UaException {
    try {
      return getKeyFrameCountNodeAsync().get();
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
  public CompletableFuture<? extends PropertyTypeNode> getKeyFrameCountNodeAsync() {
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
                                "http://opcfoundation.org/UA/:KeyFrameCount (declaration i=17563,"
                                    + " owner i=15306) on "
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
                                        new QualifiedName(namespaceIndex, "KeyFrameCount"))
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
                                          "http://opcfoundation.org/UA/:KeyFrameCount (declaration"
                                              + " i=17563, owner i=15306) on "
                                              + getNodeId()));
                                }
                                var operation = results[0];
                                if (operation.getStatusCode().getValue()
                                    == StatusCodes.Bad_NoMatch) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_NotFound,
                                          "http://opcfoundation.org/UA/:KeyFrameCount (declaration"
                                              + " i=17563, owner i=15306) on "
                                              + getNodeId()));
                                }
                                if (!operation.getStatusCode().isGood()) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          operation.getStatusCode(),
                                          "http://opcfoundation.org/UA/:KeyFrameCount (declaration"
                                              + " i=17563, owner i=15306)"));
                                }
                                var targets = operation.getTargets();
                                if (targets == null || targets.length == 0) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_UnexpectedError,
                                          "http://opcfoundation.org/UA/:KeyFrameCount (declaration"
                                              + " i=17563, owner i=15306) on "
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
                                            "http://opcfoundation.org/UA/:KeyFrameCount"
                                                + " (declaration i=17563, owner i=15306) on "
                                                + getNodeId()));
                                  }
                                  if (!target.getTargetId().isLocal()) {
                                    return CompletableFuture.failedFuture(
                                        new UaException(
                                            StatusCodes.Bad_NotSupported,
                                            "http://opcfoundation.org/UA/:KeyFrameCount"
                                                + " (declaration i=17563, owner i=15306) on "
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
                                                            "http://opcfoundation.org/UA/:KeyFrameCount"
                                                                + " (declaration i=17563, owner"
                                                                + " i=15306) on "
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
                                                    "http://opcfoundation.org/UA/:KeyFrameCount"
                                                        + " (declaration i=17563, owner i=15306) on"
                                                        + " "
                                                        + getNodeId()));
                                          }
                                          if (unique.size() != 1) {
                                            return CompletableFuture.failedFuture(
                                                new UaException(
                                                    StatusCodes.Bad_TooManyMatches,
                                                    "http://opcfoundation.org/UA/:KeyFrameCount"
                                                        + " (declaration i=17563, owner i=15306) on"
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
                                                              "http://opcfoundation.org/UA/:KeyFrameCount"
                                                                  + " (declaration i=17563, owner"
                                                                  + " i=15306) on "
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
                      "http://opcfoundation.org/UA/:KeyFrameCount (declaration i=17563, owner"
                          + " i=15306)"));
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
  public @Nullable String getHeaderLayoutUri() throws UaException {
    PropertyTypeNode node = getHeaderLayoutUriNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:HeaderLayoutUri (declaration i=17564, owner i=15306)"
              + " on "
              + getNodeId());
    }
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setHeaderLayoutUri(@Nullable String value) throws UaException {
    PropertyTypeNode node = getHeaderLayoutUriNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:HeaderLayoutUri (declaration i=17564, owner i=15306)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable String readHeaderLayoutUri() throws UaException {
    try {
      return readHeaderLayoutUriAsync().get();
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
  public void writeHeaderLayoutUri(@Nullable String value) throws UaException {
    try {
      StatusCode statusCode = writeHeaderLayoutUriAsync(value).get();
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
  public CompletableFuture<? extends @Nullable String> readHeaderLayoutUriAsync() {
    return getHeaderLayoutUriNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:HeaderLayoutUri (declaration i=17564, owner"
                            + " i=15306) on "
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
  public CompletableFuture<StatusCode> writeHeaderLayoutUriAsync(@Nullable String headerLayoutUri) {
    return getHeaderLayoutUriNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:HeaderLayoutUri (declaration i=17564, owner"
                            + " i=15306) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(headerLayoutUri));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getHeaderLayoutUriNode() throws UaException {
    try {
      return getHeaderLayoutUriNodeAsync().get();
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
  public CompletableFuture<? extends PropertyTypeNode> getHeaderLayoutUriNodeAsync() {
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
                                "http://opcfoundation.org/UA/:HeaderLayoutUri (declaration i=17564,"
                                    + " owner i=15306) on "
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
                                        new QualifiedName(namespaceIndex, "HeaderLayoutUri"))
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
                                          "http://opcfoundation.org/UA/:HeaderLayoutUri"
                                              + " (declaration i=17564, owner i=15306) on "
                                              + getNodeId()));
                                }
                                var operation = results[0];
                                if (operation.getStatusCode().getValue()
                                    == StatusCodes.Bad_NoMatch) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_NotFound,
                                          "http://opcfoundation.org/UA/:HeaderLayoutUri"
                                              + " (declaration i=17564, owner i=15306) on "
                                              + getNodeId()));
                                }
                                if (!operation.getStatusCode().isGood()) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          operation.getStatusCode(),
                                          "http://opcfoundation.org/UA/:HeaderLayoutUri"
                                              + " (declaration i=17564, owner i=15306)"));
                                }
                                var targets = operation.getTargets();
                                if (targets == null || targets.length == 0) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_UnexpectedError,
                                          "http://opcfoundation.org/UA/:HeaderLayoutUri"
                                              + " (declaration i=17564, owner i=15306) on "
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
                                            "http://opcfoundation.org/UA/:HeaderLayoutUri"
                                                + " (declaration i=17564, owner i=15306) on "
                                                + getNodeId()));
                                  }
                                  if (!target.getTargetId().isLocal()) {
                                    return CompletableFuture.failedFuture(
                                        new UaException(
                                            StatusCodes.Bad_NotSupported,
                                            "http://opcfoundation.org/UA/:HeaderLayoutUri"
                                                + " (declaration i=17564, owner i=15306) on "
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
                                                            "http://opcfoundation.org/UA/:HeaderLayoutUri"
                                                                + " (declaration i=17564, owner"
                                                                + " i=15306) on "
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
                                                    "http://opcfoundation.org/UA/:HeaderLayoutUri"
                                                        + " (declaration i=17564, owner i=15306) on"
                                                        + " "
                                                        + getNodeId()));
                                          }
                                          if (unique.size() != 1) {
                                            return CompletableFuture.failedFuture(
                                                new UaException(
                                                    StatusCodes.Bad_TooManyMatches,
                                                    "http://opcfoundation.org/UA/:HeaderLayoutUri"
                                                        + " (declaration i=17564, owner i=15306) on"
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
                                                              "http://opcfoundation.org/UA/:HeaderLayoutUri"
                                                                  + " (declaration i=17564, owner"
                                                                  + " i=15306) on "
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
                      "http://opcfoundation.org/UA/:HeaderLayoutUri (declaration i=17564, owner"
                          + " i=15306)"));
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
  public @Nullable MessageSecurityMode getSecurityMode() throws UaException {
    PropertyTypeNode node = getSecurityModeNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SecurityMode (declaration i=15932, owner i=15306)"
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
              "SecurityMode: ValueRank=-1 does not permit rank " + rank);
        }
        if (value != null && !((Object) value instanceof MessageSecurityMode)) {
          if (!(value instanceof Integer)) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "SecurityMode: expected"
                    + " org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode or"
                    + " Int32, got "
                    + value);
          }
          if (MessageSecurityMode.from((Integer) value) == null) {
            throw new UaException(
                StatusCodes.Bad_OutOfRange,
                "SecurityMode: unknown"
                    + " org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode"
                    + " value "
                    + value);
          }
        }
        convertedValue =
            value == null || value instanceof MessageSecurityMode
                ? (MessageSecurityMode) value
                : MessageSecurityMode.from((Integer) value);
      }
    }
    return (MessageSecurityMode) convertedValue;
  }

  @Override
  public void setSecurityMode(@Nullable MessageSecurityMode value) throws UaException {
    PropertyTypeNode node = getSecurityModeNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SecurityMode (declaration i=15932, owner i=15306)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable MessageSecurityMode readSecurityMode() throws UaException {
    try {
      return readSecurityModeAsync().get();
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
  public void writeSecurityMode(@Nullable MessageSecurityMode value) throws UaException {
    try {
      StatusCode statusCode = writeSecurityModeAsync(value).get();
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
  public CompletableFuture<? extends @Nullable MessageSecurityMode> readSecurityModeAsync() {
    return getSecurityModeNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:SecurityMode (declaration i=15932, owner"
                            + " i=15306) on "
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
                          "SecurityMode: ValueRank=-1 does not permit rank " + rank);
                    }
                    if (value != null && !((Object) value instanceof MessageSecurityMode)) {
                      if (!(value instanceof Integer)) {
                        throw new UaRuntimeException(
                            StatusCodes.Bad_TypeMismatch,
                            "SecurityMode: expected"
                                + " org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode"
                                + " or Int32, got "
                                + value);
                      }
                      if (MessageSecurityMode.from((Integer) value) == null) {
                        throw new UaRuntimeException(
                            StatusCodes.Bad_OutOfRange,
                            "SecurityMode: unknown"
                                + " org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode"
                                + " value "
                                + value);
                      }
                    }
                    convertedValue =
                        value == null || value instanceof MessageSecurityMode
                            ? (MessageSecurityMode) value
                            : MessageSecurityMode.from((Integer) value);
                  }
                }
                return (MessageSecurityMode) convertedValue;
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeSecurityModeAsync(
      @Nullable MessageSecurityMode securityMode) {
    return getSecurityModeNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:SecurityMode (declaration i=15932, owner"
                            + " i=15306) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(securityMode));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getSecurityModeNode() throws UaException {
    try {
      return getSecurityModeNodeAsync().get();
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
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getSecurityModeNodeAsync() {
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
                                "http://opcfoundation.org/UA/:SecurityMode (declaration i=15932,"
                                    + " owner i=15306) on "
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
                                        new QualifiedName(namespaceIndex, "SecurityMode"))
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
                                          "http://opcfoundation.org/UA/:SecurityMode (declaration"
                                              + " i=15932, owner i=15306) on "
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
                                          "http://opcfoundation.org/UA/:SecurityMode (declaration"
                                              + " i=15932, owner i=15306)"));
                                }
                                var targets = operation.getTargets();
                                if (targets == null || targets.length == 0) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_UnexpectedError,
                                          "http://opcfoundation.org/UA/:SecurityMode (declaration"
                                              + " i=15932, owner i=15306) on "
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
                                            "http://opcfoundation.org/UA/:SecurityMode (declaration"
                                                + " i=15932, owner i=15306) on "
                                                + getNodeId()));
                                  }
                                  if (!target.getTargetId().isLocal()) {
                                    return CompletableFuture.failedFuture(
                                        new UaException(
                                            StatusCodes.Bad_NotSupported,
                                            "http://opcfoundation.org/UA/:SecurityMode (declaration"
                                                + " i=15932, owner i=15306) on "
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
                                                            "http://opcfoundation.org/UA/:SecurityMode"
                                                                + " (declaration i=15932, owner"
                                                                + " i=15306) on "
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
                                                    "http://opcfoundation.org/UA/:SecurityMode"
                                                        + " (declaration i=15932, owner i=15306) on"
                                                        + " "
                                                        + getNodeId()));
                                          }
                                          if (unique.size() != 1) {
                                            return CompletableFuture.failedFuture(
                                                new UaException(
                                                    StatusCodes.Bad_TooManyMatches,
                                                    "http://opcfoundation.org/UA/:SecurityMode"
                                                        + " (declaration i=15932, owner i=15306) on"
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
                                                              "http://opcfoundation.org/UA/:SecurityMode"
                                                                  + " (declaration i=15932, owner"
                                                                  + " i=15306) on "
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
                      "http://opcfoundation.org/UA/:SecurityMode (declaration i=15932, owner"
                          + " i=15306)"));
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
  public @Nullable String getSecurityGroupId() throws UaException {
    PropertyTypeNode node = getSecurityGroupIdNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SecurityGroupId (declaration i=15933, owner i=15306)"
              + " on "
              + getNodeId());
    }
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setSecurityGroupId(@Nullable String value) throws UaException {
    PropertyTypeNode node = getSecurityGroupIdNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SecurityGroupId (declaration i=15933, owner i=15306)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable String readSecurityGroupId() throws UaException {
    try {
      return readSecurityGroupIdAsync().get();
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
  public void writeSecurityGroupId(@Nullable String value) throws UaException {
    try {
      StatusCode statusCode = writeSecurityGroupIdAsync(value).get();
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
  public CompletableFuture<? extends @Nullable String> readSecurityGroupIdAsync() {
    return getSecurityGroupIdNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:SecurityGroupId (declaration i=15933, owner"
                            + " i=15306) on "
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
  public CompletableFuture<StatusCode> writeSecurityGroupIdAsync(@Nullable String securityGroupId) {
    return getSecurityGroupIdNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:SecurityGroupId (declaration i=15933, owner"
                            + " i=15306) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(securityGroupId));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getSecurityGroupIdNode() throws UaException {
    try {
      return getSecurityGroupIdNodeAsync().get();
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
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getSecurityGroupIdNodeAsync() {
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
                                "http://opcfoundation.org/UA/:SecurityGroupId (declaration i=15933,"
                                    + " owner i=15306) on "
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
                                        new QualifiedName(namespaceIndex, "SecurityGroupId"))
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
                                          "http://opcfoundation.org/UA/:SecurityGroupId"
                                              + " (declaration i=15933, owner i=15306) on "
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
                                          "http://opcfoundation.org/UA/:SecurityGroupId"
                                              + " (declaration i=15933, owner i=15306)"));
                                }
                                var targets = operation.getTargets();
                                if (targets == null || targets.length == 0) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_UnexpectedError,
                                          "http://opcfoundation.org/UA/:SecurityGroupId"
                                              + " (declaration i=15933, owner i=15306) on "
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
                                            "http://opcfoundation.org/UA/:SecurityGroupId"
                                                + " (declaration i=15933, owner i=15306) on "
                                                + getNodeId()));
                                  }
                                  if (!target.getTargetId().isLocal()) {
                                    return CompletableFuture.failedFuture(
                                        new UaException(
                                            StatusCodes.Bad_NotSupported,
                                            "http://opcfoundation.org/UA/:SecurityGroupId"
                                                + " (declaration i=15933, owner i=15306) on "
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
                                                            "http://opcfoundation.org/UA/:SecurityGroupId"
                                                                + " (declaration i=15933, owner"
                                                                + " i=15306) on "
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
                                                    "http://opcfoundation.org/UA/:SecurityGroupId"
                                                        + " (declaration i=15933, owner i=15306) on"
                                                        + " "
                                                        + getNodeId()));
                                          }
                                          if (unique.size() != 1) {
                                            return CompletableFuture.failedFuture(
                                                new UaException(
                                                    StatusCodes.Bad_TooManyMatches,
                                                    "http://opcfoundation.org/UA/:SecurityGroupId"
                                                        + " (declaration i=15933, owner i=15306) on"
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
                                                              "http://opcfoundation.org/UA/:SecurityGroupId"
                                                                  + " (declaration i=15933, owner"
                                                                  + " i=15306) on "
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
                      "http://opcfoundation.org/UA/:SecurityGroupId (declaration i=15933, owner"
                          + " i=15306)"));
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
  public @Nullable EndpointDescription @Nullable [] getSecurityKeyServices() throws UaException {
    PropertyTypeNode node = getSecurityKeyServicesNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SecurityKeyServices (declaration i=15934, owner i=15306)"
              + " on "
              + getNodeId());
    }
    return (EndpointDescription[])
        decodeValue(
            node.getValue().getValue().getValue(),
            EndpointDescription.class,
            ValueRanks.OneDimension);
  }

  @Override
  public void setSecurityKeyServices(@Nullable EndpointDescription @Nullable [] value)
      throws UaException {
    PropertyTypeNode node = getSecurityKeyServicesNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SecurityKeyServices (declaration i=15934, owner i=15306)"
              + " on "
              + getNodeId());
    }
    node.setValue(
        new Variant(encodeValue(value, EndpointDescription.class, ValueRanks.OneDimension)));
  }

  @Override
  public @Nullable EndpointDescription @Nullable [] readSecurityKeyServices() throws UaException {
    try {
      return readSecurityKeyServicesAsync().get();
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
  public void writeSecurityKeyServices(@Nullable EndpointDescription @Nullable [] value)
      throws UaException {
    try {
      StatusCode statusCode = writeSecurityKeyServicesAsync(value).get();
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
  public CompletableFuture<? extends @Nullable EndpointDescription @Nullable []>
      readSecurityKeyServicesAsync() {
    return getSecurityKeyServicesNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:SecurityKeyServices (declaration i=15934,"
                            + " owner i=15306) on "
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
                return (EndpointDescription[])
                    decodeValue(
                        v.getValue().getValue(),
                        EndpointDescription.class,
                        ValueRanks.OneDimension);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeSecurityKeyServicesAsync(
      @Nullable EndpointDescription @Nullable [] securityKeyServices) {
    return getSecurityKeyServicesNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:SecurityKeyServices (declaration i=15934,"
                            + " owner i=15306) on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(
                            encodeValue(
                                securityKeyServices,
                                EndpointDescription.class,
                                ValueRanks.OneDimension)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getSecurityKeyServicesNode() throws UaException {
    try {
      return getSecurityKeyServicesNodeAsync().get();
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
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getSecurityKeyServicesNodeAsync() {
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
                                "http://opcfoundation.org/UA/:SecurityKeyServices (declaration"
                                    + " i=15934, owner i=15306) on "
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
                                        new QualifiedName(namespaceIndex, "SecurityKeyServices"))
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
                                          "http://opcfoundation.org/UA/:SecurityKeyServices"
                                              + " (declaration i=15934, owner i=15306) on "
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
                                          "http://opcfoundation.org/UA/:SecurityKeyServices"
                                              + " (declaration i=15934, owner i=15306)"));
                                }
                                var targets = operation.getTargets();
                                if (targets == null || targets.length == 0) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_UnexpectedError,
                                          "http://opcfoundation.org/UA/:SecurityKeyServices"
                                              + " (declaration i=15934, owner i=15306) on "
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
                                            "http://opcfoundation.org/UA/:SecurityKeyServices"
                                                + " (declaration i=15934, owner i=15306) on "
                                                + getNodeId()));
                                  }
                                  if (!target.getTargetId().isLocal()) {
                                    return CompletableFuture.failedFuture(
                                        new UaException(
                                            StatusCodes.Bad_NotSupported,
                                            "http://opcfoundation.org/UA/:SecurityKeyServices"
                                                + " (declaration i=15934, owner i=15306) on "
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
                                                            "http://opcfoundation.org/UA/:SecurityKeyServices"
                                                                + " (declaration i=15934, owner"
                                                                + " i=15306) on "
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
                                                    "http://opcfoundation.org/UA/:SecurityKeyServices"
                                                        + " (declaration i=15934, owner i=15306) on"
                                                        + " "
                                                        + getNodeId()));
                                          }
                                          if (unique.size() != 1) {
                                            return CompletableFuture.failedFuture(
                                                new UaException(
                                                    StatusCodes.Bad_TooManyMatches,
                                                    "http://opcfoundation.org/UA/:SecurityKeyServices"
                                                        + " (declaration i=15934, owner i=15306) on"
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
                                                              "http://opcfoundation.org/UA/:SecurityKeyServices"
                                                                  + " (declaration i=15934, owner"
                                                                  + " i=15306) on "
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
                      "http://opcfoundation.org/UA/:SecurityKeyServices (declaration i=15934, owner"
                          + " i=15306)"));
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
  public @Nullable KeyValuePair @Nullable [] getDataSetReaderProperties() throws UaException {
    PropertyTypeNode node = getDataSetReaderPropertiesNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DataSetReaderProperties (declaration i=17494, owner"
              + " i=15306) on "
              + getNodeId());
    }
    return (KeyValuePair[])
        decodeValue(
            node.getValue().getValue().getValue(), KeyValuePair.class, ValueRanks.OneDimension);
  }

  @Override
  public void setDataSetReaderProperties(@Nullable KeyValuePair @Nullable [] value)
      throws UaException {
    PropertyTypeNode node = getDataSetReaderPropertiesNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DataSetReaderProperties (declaration i=17494, owner"
              + " i=15306) on "
              + getNodeId());
    }
    node.setValue(new Variant(encodeValue(value, KeyValuePair.class, ValueRanks.OneDimension)));
  }

  @Override
  public @Nullable KeyValuePair @Nullable [] readDataSetReaderProperties() throws UaException {
    try {
      return readDataSetReaderPropertiesAsync().get();
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
  public void writeDataSetReaderProperties(@Nullable KeyValuePair @Nullable [] value)
      throws UaException {
    try {
      StatusCode statusCode = writeDataSetReaderPropertiesAsync(value).get();
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
      readDataSetReaderPropertiesAsync() {
    return getDataSetReaderPropertiesNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:DataSetReaderProperties (declaration i=17494,"
                            + " owner i=15306) on "
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
  public CompletableFuture<StatusCode> writeDataSetReaderPropertiesAsync(
      @Nullable KeyValuePair @Nullable [] dataSetReaderProperties) {
    return getDataSetReaderPropertiesNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:DataSetReaderProperties (declaration i=17494,"
                            + " owner i=15306) on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(
                            encodeValue(
                                dataSetReaderProperties,
                                KeyValuePair.class,
                                ValueRanks.OneDimension)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getDataSetReaderPropertiesNode() throws UaException {
    try {
      return getDataSetReaderPropertiesNodeAsync().get();
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
  public CompletableFuture<? extends PropertyTypeNode> getDataSetReaderPropertiesNodeAsync() {
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
                                "http://opcfoundation.org/UA/:DataSetReaderProperties (declaration"
                                    + " i=17494, owner i=15306) on "
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
                                            namespaceIndex, "DataSetReaderProperties"))
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
                                          "http://opcfoundation.org/UA/:DataSetReaderProperties"
                                              + " (declaration i=17494, owner i=15306) on "
                                              + getNodeId()));
                                }
                                var operation = results[0];
                                if (operation.getStatusCode().getValue()
                                    == StatusCodes.Bad_NoMatch) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_NotFound,
                                          "http://opcfoundation.org/UA/:DataSetReaderProperties"
                                              + " (declaration i=17494, owner i=15306) on "
                                              + getNodeId()));
                                }
                                if (!operation.getStatusCode().isGood()) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          operation.getStatusCode(),
                                          "http://opcfoundation.org/UA/:DataSetReaderProperties"
                                              + " (declaration i=17494, owner i=15306)"));
                                }
                                var targets = operation.getTargets();
                                if (targets == null || targets.length == 0) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_UnexpectedError,
                                          "http://opcfoundation.org/UA/:DataSetReaderProperties"
                                              + " (declaration i=17494, owner i=15306) on "
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
                                            "http://opcfoundation.org/UA/:DataSetReaderProperties"
                                                + " (declaration i=17494, owner i=15306) on "
                                                + getNodeId()));
                                  }
                                  if (!target.getTargetId().isLocal()) {
                                    return CompletableFuture.failedFuture(
                                        new UaException(
                                            StatusCodes.Bad_NotSupported,
                                            "http://opcfoundation.org/UA/:DataSetReaderProperties"
                                                + " (declaration i=17494, owner i=15306) on "
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
                                                            "http://opcfoundation.org/UA/:DataSetReaderProperties"
                                                                + " (declaration i=17494, owner"
                                                                + " i=15306) on "
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
                                                    "http://opcfoundation.org/UA/:DataSetReaderProperties"
                                                        + " (declaration i=17494, owner i=15306) on"
                                                        + " "
                                                        + getNodeId()));
                                          }
                                          if (unique.size() != 1) {
                                            return CompletableFuture.failedFuture(
                                                new UaException(
                                                    StatusCodes.Bad_TooManyMatches,
                                                    "http://opcfoundation.org/UA/:DataSetReaderProperties"
                                                        + " (declaration i=17494, owner i=15306) on"
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
                                                              "http://opcfoundation.org/UA/:DataSetReaderProperties"
                                                                  + " (declaration i=17494, owner"
                                                                  + " i=15306) on "
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
                      "http://opcfoundation.org/UA/:DataSetReaderProperties (declaration i=17494,"
                          + " owner i=15306)"));
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
  public @Nullable DataSetReaderTransportTypeNode getTransportSettingsNode() throws UaException {
    try {
      return getTransportSettingsNodeAsync().get();
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
  public CompletableFuture<? extends @Nullable DataSetReaderTransportTypeNode>
      getTransportSettingsNodeAsync() {
    CompletableFuture<DataSetReaderTransportTypeNode> result = new CompletableFuture<>();
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
                                "http://opcfoundation.org/UA/:TransportSettings (declaration"
                                    + " i=15311, owner i=15306) on "
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
                                        new QualifiedName(namespaceIndex, "TransportSettings"))
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
                                          "http://opcfoundation.org/UA/:TransportSettings"
                                              + " (declaration i=15311, owner i=15306) on "
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
                                          "http://opcfoundation.org/UA/:TransportSettings"
                                              + " (declaration i=15311, owner i=15306)"));
                                }
                                var targets = operation.getTargets();
                                if (targets == null || targets.length == 0) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_UnexpectedError,
                                          "http://opcfoundation.org/UA/:TransportSettings"
                                              + " (declaration i=15311, owner i=15306) on "
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
                                            "http://opcfoundation.org/UA/:TransportSettings"
                                                + " (declaration i=15311, owner i=15306) on "
                                                + getNodeId()));
                                  }
                                  if (!target.getTargetId().isLocal()) {
                                    return CompletableFuture.failedFuture(
                                        new UaException(
                                            StatusCodes.Bad_NotSupported,
                                            "http://opcfoundation.org/UA/:TransportSettings"
                                                + " (declaration i=15311, owner i=15306) on "
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
                                                            "http://opcfoundation.org/UA/:TransportSettings"
                                                                + " (declaration i=15311, owner"
                                                                + " i=15306) on "
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
                                                    "http://opcfoundation.org/UA/:TransportSettings"
                                                        + " (declaration i=15311, owner i=15306) on"
                                                        + " "
                                                        + getNodeId()));
                                          }
                                          if (unique.size() != 1) {
                                            return CompletableFuture.failedFuture(
                                                new UaException(
                                                    StatusCodes.Bad_TooManyMatches,
                                                    "http://opcfoundation.org/UA/:TransportSettings"
                                                        + " (declaration i=15311, owner i=15306) on"
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
                                                            != NodeClass.Object) {
                                                      return CompletableFuture.failedFuture(
                                                          new UaException(
                                                              StatusCodes.Bad_NodeClassInvalid,
                                                              "http://opcfoundation.org/UA/:TransportSettings"
                                                                  + " (declaration i=15311, owner"
                                                                  + " i=15306) on "
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
            } else if (node != null && !(node instanceof DataSetReaderTransportTypeNode)) {
              result.completeExceptionally(
                  new UaException(
                      StatusCodes.Bad_TypeMismatch,
                      "http://opcfoundation.org/UA/:TransportSettings (declaration i=15311, owner"
                          + " i=15306)"));
            } else {
              result.complete((DataSetReaderTransportTypeNode) node);
            }
          });
    } catch (Exception e) {
      result.completeExceptionally(e);
    }
    return result;
  }

  @Override
  public @Nullable DataSetReaderMessageTypeNode getMessageSettingsNode() throws UaException {
    try {
      return getMessageSettingsNodeAsync().get();
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
  public CompletableFuture<? extends @Nullable DataSetReaderMessageTypeNode>
      getMessageSettingsNodeAsync() {
    CompletableFuture<DataSetReaderMessageTypeNode> result = new CompletableFuture<>();
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
                                "http://opcfoundation.org/UA/:MessageSettings (declaration i=21103,"
                                    + " owner i=15306) on "
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
                                        new QualifiedName(namespaceIndex, "MessageSettings"))
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
                                          "http://opcfoundation.org/UA/:MessageSettings"
                                              + " (declaration i=21103, owner i=15306) on "
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
                                          "http://opcfoundation.org/UA/:MessageSettings"
                                              + " (declaration i=21103, owner i=15306)"));
                                }
                                var targets = operation.getTargets();
                                if (targets == null || targets.length == 0) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_UnexpectedError,
                                          "http://opcfoundation.org/UA/:MessageSettings"
                                              + " (declaration i=21103, owner i=15306) on "
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
                                            "http://opcfoundation.org/UA/:MessageSettings"
                                                + " (declaration i=21103, owner i=15306) on "
                                                + getNodeId()));
                                  }
                                  if (!target.getTargetId().isLocal()) {
                                    return CompletableFuture.failedFuture(
                                        new UaException(
                                            StatusCodes.Bad_NotSupported,
                                            "http://opcfoundation.org/UA/:MessageSettings"
                                                + " (declaration i=21103, owner i=15306) on "
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
                                                            "http://opcfoundation.org/UA/:MessageSettings"
                                                                + " (declaration i=21103, owner"
                                                                + " i=15306) on "
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
                                                    "http://opcfoundation.org/UA/:MessageSettings"
                                                        + " (declaration i=21103, owner i=15306) on"
                                                        + " "
                                                        + getNodeId()));
                                          }
                                          if (unique.size() != 1) {
                                            return CompletableFuture.failedFuture(
                                                new UaException(
                                                    StatusCodes.Bad_TooManyMatches,
                                                    "http://opcfoundation.org/UA/:MessageSettings"
                                                        + " (declaration i=21103, owner i=15306) on"
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
                                                            != NodeClass.Object) {
                                                      return CompletableFuture.failedFuture(
                                                          new UaException(
                                                              StatusCodes.Bad_NodeClassInvalid,
                                                              "http://opcfoundation.org/UA/:MessageSettings"
                                                                  + " (declaration i=21103, owner"
                                                                  + " i=15306) on "
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
            } else if (node != null && !(node instanceof DataSetReaderMessageTypeNode)) {
              result.completeExceptionally(
                  new UaException(
                      StatusCodes.Bad_TypeMismatch,
                      "http://opcfoundation.org/UA/:MessageSettings (declaration i=21103, owner"
                          + " i=15306)"));
            } else {
              result.complete((DataSetReaderMessageTypeNode) node);
            }
          });
    } catch (Exception e) {
      result.completeExceptionally(e);
    }
    return result;
  }

  @Override
  public PubSubStatusTypeNode getStatusNode() throws UaException {
    try {
      return getStatusNodeAsync().get();
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
  public CompletableFuture<? extends PubSubStatusTypeNode> getStatusNodeAsync() {
    CompletableFuture<PubSubStatusTypeNode> result = new CompletableFuture<>();
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
                                "http://opcfoundation.org/UA/:Status (declaration i=15307, owner"
                                    + " i=15306) on "
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
                                        new QualifiedName(namespaceIndex, "Status"))
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
                                          "http://opcfoundation.org/UA/:Status (declaration"
                                              + " i=15307, owner i=15306) on "
                                              + getNodeId()));
                                }
                                var operation = results[0];
                                if (operation.getStatusCode().getValue()
                                    == StatusCodes.Bad_NoMatch) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_NotFound,
                                          "http://opcfoundation.org/UA/:Status (declaration"
                                              + " i=15307, owner i=15306) on "
                                              + getNodeId()));
                                }
                                if (!operation.getStatusCode().isGood()) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          operation.getStatusCode(),
                                          "http://opcfoundation.org/UA/:Status (declaration"
                                              + " i=15307, owner i=15306)"));
                                }
                                var targets = operation.getTargets();
                                if (targets == null || targets.length == 0) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_UnexpectedError,
                                          "http://opcfoundation.org/UA/:Status (declaration"
                                              + " i=15307, owner i=15306) on "
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
                                            "http://opcfoundation.org/UA/:Status (declaration"
                                                + " i=15307, owner i=15306) on "
                                                + getNodeId()));
                                  }
                                  if (!target.getTargetId().isLocal()) {
                                    return CompletableFuture.failedFuture(
                                        new UaException(
                                            StatusCodes.Bad_NotSupported,
                                            "http://opcfoundation.org/UA/:Status (declaration"
                                                + " i=15307, owner i=15306) on "
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
                                                            "http://opcfoundation.org/UA/:Status"
                                                                + " (declaration i=15307, owner"
                                                                + " i=15306) on "
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
                                                    "http://opcfoundation.org/UA/:Status"
                                                        + " (declaration i=15307, owner i=15306) on"
                                                        + " "
                                                        + getNodeId()));
                                          }
                                          if (unique.size() != 1) {
                                            return CompletableFuture.failedFuture(
                                                new UaException(
                                                    StatusCodes.Bad_TooManyMatches,
                                                    "http://opcfoundation.org/UA/:Status"
                                                        + " (declaration i=15307, owner i=15306) on"
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
                                                            != NodeClass.Object) {
                                                      return CompletableFuture.failedFuture(
                                                          new UaException(
                                                              StatusCodes.Bad_NodeClassInvalid,
                                                              "http://opcfoundation.org/UA/:Status"
                                                                  + " (declaration i=15307, owner"
                                                                  + " i=15306) on "
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
            } else if (node != null && !(node instanceof PubSubStatusTypeNode)) {
              result.completeExceptionally(
                  new UaException(
                      StatusCodes.Bad_TypeMismatch,
                      "http://opcfoundation.org/UA/:Status (declaration i=15307, owner i=15306)"));
            } else {
              result.complete((PubSubStatusTypeNode) node);
            }
          });
    } catch (Exception e) {
      result.completeExceptionally(e);
    }
    return result;
  }

  @Override
  public @Nullable PubSubDiagnosticsDataSetReaderTypeNode getDiagnosticsNode() throws UaException {
    try {
      return getDiagnosticsNodeAsync().get();
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
  public CompletableFuture<? extends @Nullable PubSubDiagnosticsDataSetReaderTypeNode>
      getDiagnosticsNodeAsync() {
    CompletableFuture<PubSubDiagnosticsDataSetReaderTypeNode> result = new CompletableFuture<>();
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
                                "http://opcfoundation.org/UA/:Diagnostics (declaration i=19609,"
                                    + " owner i=15306) on "
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
                                        new QualifiedName(namespaceIndex, "Diagnostics"))
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
                                          "http://opcfoundation.org/UA/:Diagnostics (declaration"
                                              + " i=19609, owner i=15306) on "
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
                                          "http://opcfoundation.org/UA/:Diagnostics (declaration"
                                              + " i=19609, owner i=15306)"));
                                }
                                var targets = operation.getTargets();
                                if (targets == null || targets.length == 0) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_UnexpectedError,
                                          "http://opcfoundation.org/UA/:Diagnostics (declaration"
                                              + " i=19609, owner i=15306) on "
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
                                            "http://opcfoundation.org/UA/:Diagnostics (declaration"
                                                + " i=19609, owner i=15306) on "
                                                + getNodeId()));
                                  }
                                  if (!target.getTargetId().isLocal()) {
                                    return CompletableFuture.failedFuture(
                                        new UaException(
                                            StatusCodes.Bad_NotSupported,
                                            "http://opcfoundation.org/UA/:Diagnostics (declaration"
                                                + " i=19609, owner i=15306) on "
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
                                                            "http://opcfoundation.org/UA/:Diagnostics"
                                                                + " (declaration i=19609, owner"
                                                                + " i=15306) on "
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
                                                    "http://opcfoundation.org/UA/:Diagnostics"
                                                        + " (declaration i=19609, owner i=15306) on"
                                                        + " "
                                                        + getNodeId()));
                                          }
                                          if (unique.size() != 1) {
                                            return CompletableFuture.failedFuture(
                                                new UaException(
                                                    StatusCodes.Bad_TooManyMatches,
                                                    "http://opcfoundation.org/UA/:Diagnostics"
                                                        + " (declaration i=19609, owner i=15306) on"
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
                                                            != NodeClass.Object) {
                                                      return CompletableFuture.failedFuture(
                                                          new UaException(
                                                              StatusCodes.Bad_NodeClassInvalid,
                                                              "http://opcfoundation.org/UA/:Diagnostics"
                                                                  + " (declaration i=19609, owner"
                                                                  + " i=15306) on "
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
            } else if (node != null && !(node instanceof PubSubDiagnosticsDataSetReaderTypeNode)) {
              result.completeExceptionally(
                  new UaException(
                      StatusCodes.Bad_TypeMismatch,
                      "http://opcfoundation.org/UA/:Diagnostics (declaration i=19609, owner"
                          + " i=15306)"));
            } else {
              result.complete((PubSubDiagnosticsDataSetReaderTypeNode) node);
            }
          });
    } catch (Exception e) {
      result.completeExceptionally(e);
    }
    return result;
  }

  @Override
  public SubscribedDataSetTypeNode getSubscribedDataSetNode() throws UaException {
    try {
      return getSubscribedDataSetNodeAsync().get();
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
  public CompletableFuture<? extends SubscribedDataSetTypeNode> getSubscribedDataSetNodeAsync() {
    CompletableFuture<SubscribedDataSetTypeNode> result = new CompletableFuture<>();
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
                                "http://opcfoundation.org/UA/:SubscribedDataSet (declaration"
                                    + " i=15316, owner i=15306) on "
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
                                        new QualifiedName(namespaceIndex, "SubscribedDataSet"))
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
                                          "http://opcfoundation.org/UA/:SubscribedDataSet"
                                              + " (declaration i=15316, owner i=15306) on "
                                              + getNodeId()));
                                }
                                var operation = results[0];
                                if (operation.getStatusCode().getValue()
                                    == StatusCodes.Bad_NoMatch) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_NotFound,
                                          "http://opcfoundation.org/UA/:SubscribedDataSet"
                                              + " (declaration i=15316, owner i=15306) on "
                                              + getNodeId()));
                                }
                                if (!operation.getStatusCode().isGood()) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          operation.getStatusCode(),
                                          "http://opcfoundation.org/UA/:SubscribedDataSet"
                                              + " (declaration i=15316, owner i=15306)"));
                                }
                                var targets = operation.getTargets();
                                if (targets == null || targets.length == 0) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_UnexpectedError,
                                          "http://opcfoundation.org/UA/:SubscribedDataSet"
                                              + " (declaration i=15316, owner i=15306) on "
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
                                            "http://opcfoundation.org/UA/:SubscribedDataSet"
                                                + " (declaration i=15316, owner i=15306) on "
                                                + getNodeId()));
                                  }
                                  if (!target.getTargetId().isLocal()) {
                                    return CompletableFuture.failedFuture(
                                        new UaException(
                                            StatusCodes.Bad_NotSupported,
                                            "http://opcfoundation.org/UA/:SubscribedDataSet"
                                                + " (declaration i=15316, owner i=15306) on "
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
                                                            "http://opcfoundation.org/UA/:SubscribedDataSet"
                                                                + " (declaration i=15316, owner"
                                                                + " i=15306) on "
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
                                                    "http://opcfoundation.org/UA/:SubscribedDataSet"
                                                        + " (declaration i=15316, owner i=15306) on"
                                                        + " "
                                                        + getNodeId()));
                                          }
                                          if (unique.size() != 1) {
                                            return CompletableFuture.failedFuture(
                                                new UaException(
                                                    StatusCodes.Bad_TooManyMatches,
                                                    "http://opcfoundation.org/UA/:SubscribedDataSet"
                                                        + " (declaration i=15316, owner i=15306) on"
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
                                                            != NodeClass.Object) {
                                                      return CompletableFuture.failedFuture(
                                                          new UaException(
                                                              StatusCodes.Bad_NodeClassInvalid,
                                                              "http://opcfoundation.org/UA/:SubscribedDataSet"
                                                                  + " (declaration i=15316, owner"
                                                                  + " i=15306) on "
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
            } else if (node != null && !(node instanceof SubscribedDataSetTypeNode)) {
              result.completeExceptionally(
                  new UaException(
                      StatusCodes.Bad_TypeMismatch,
                      "http://opcfoundation.org/UA/:SubscribedDataSet (declaration i=15316, owner"
                          + " i=15306)"));
            } else {
              result.complete((SubscribedDataSetTypeNode) node);
            }
          });
    } catch (Exception e) {
      result.completeExceptionally(e);
    }
    return result;
  }

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.8/#9.1.8.5
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
  public @Nullable UaMethodNode getCreateTargetVariablesMethodNode() throws UaException {
    try {
      return getCreateTargetVariablesMethodNodeAsync().get();
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
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.8/#9.1.8.5
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
      getCreateTargetVariablesMethodNodeAsync() {
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
                                "http://opcfoundation.org/UA/:CreateTargetVariables (declaration"
                                    + " i=17386, owner i=15306) on "
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
                                        new QualifiedName(namespaceIndex, "CreateTargetVariables"))
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
                                          "http://opcfoundation.org/UA/:CreateTargetVariables"
                                              + " (declaration i=17386, owner i=15306) on "
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
                                          "http://opcfoundation.org/UA/:CreateTargetVariables"
                                              + " (declaration i=17386, owner i=15306)"));
                                }
                                var targets = operation.getTargets();
                                if (targets == null || targets.length == 0) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_UnexpectedError,
                                          "http://opcfoundation.org/UA/:CreateTargetVariables"
                                              + " (declaration i=17386, owner i=15306) on "
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
                                            "http://opcfoundation.org/UA/:CreateTargetVariables"
                                                + " (declaration i=17386, owner i=15306) on "
                                                + getNodeId()));
                                  }
                                  if (!target.getTargetId().isLocal()) {
                                    return CompletableFuture.failedFuture(
                                        new UaException(
                                            StatusCodes.Bad_NotSupported,
                                            "http://opcfoundation.org/UA/:CreateTargetVariables"
                                                + " (declaration i=17386, owner i=15306) on "
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
                                                            "http://opcfoundation.org/UA/:CreateTargetVariables"
                                                                + " (declaration i=17386, owner"
                                                                + " i=15306) on "
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
                                                    "http://opcfoundation.org/UA/:CreateTargetVariables"
                                                        + " (declaration i=17386, owner i=15306) on"
                                                        + " "
                                                        + getNodeId()));
                                          }
                                          if (unique.size() != 1) {
                                            return CompletableFuture.failedFuture(
                                                new UaException(
                                                    StatusCodes.Bad_TooManyMatches,
                                                    "http://opcfoundation.org/UA/:CreateTargetVariables"
                                                        + " (declaration i=17386, owner i=15306) on"
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
                                                            != NodeClass.Method) {
                                                      return CompletableFuture.failedFuture(
                                                          new UaException(
                                                              StatusCodes.Bad_NodeClassInvalid,
                                                              "http://opcfoundation.org/UA/:CreateTargetVariables"
                                                                  + " (declaration i=17386, owner"
                                                                  + " i=15306) on "
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
                      "http://opcfoundation.org/UA/:CreateTargetVariables (declaration i=17386,"
                          + " owner i=15306)"));
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
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.8/#9.1.8.5
   *
   * <p>Invokes <code>CreateTargetVariables</code> on this node's ObjectId using the effective
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
   * @param configurationVersion ; the supplied payload may be null.
   * @param targetVariablesToAdd ; the supplied payload may be null.
   * @return the output value or object, or its future; a single value may be null.
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  @Override
  public @Nullable StatusCode @Nullable [] callCreateTargetVariables(
      @Nullable ConfigurationVersionDataType configurationVersion,
      @Nullable FieldTargetDataType @Nullable [] targetVariablesToAdd)
      throws UaException {
    return callCreateTargetVariablesDetailed(configurationVersion, targetVariablesToAdd)
        .requireGood();
  }

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.8/#9.1.8.5
   *
   * <p>Invokes <code>CreateTargetVariables</code> on this node's ObjectId using the effective
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
   * @param configurationVersion ; the supplied payload may be null.
   * @param targetVariablesToAdd ; the supplied payload may be null.
   * @return the output value or object, or its future; a single value may be null.
   */
  @NullMarked
  @Override
  public CompletableFuture<? extends @Nullable StatusCode @Nullable []>
      callCreateTargetVariablesAsync(
          @Nullable ConfigurationVersionDataType configurationVersion,
          @Nullable FieldTargetDataType @Nullable [] targetVariablesToAdd) {
    CompletableFuture<@Nullable StatusCode @Nullable []> result = new CompletableFuture<>();
    var call = callCreateTargetVariablesDetailedAsync(configurationVersion, targetVariablesToAdd);
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
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.8/#9.1.8.5
   *
   * <p>Invokes <code>CreateTargetVariables</code> on this node's ObjectId using the effective
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
   * @param configurationVersion ; the supplied payload may be null.
   * @param targetVariablesToAdd ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  @Override
  public MethodCallResult<? extends @Nullable StatusCode @Nullable []>
      callCreateTargetVariablesDetailed(
          @Nullable ConfigurationVersionDataType configurationVersion,
          @Nullable FieldTargetDataType @Nullable [] targetVariablesToAdd)
          throws UaException {
    return callCreateTargetVariablesDetailed(
        MethodCallOptions.NONE, configurationVersion, targetVariablesToAdd);
  }

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.8/#9.1.8.5
   *
   * <p>Invokes <code>CreateTargetVariables</code> on this node's ObjectId using the effective
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
   * @param configurationVersion ; the supplied payload may be null.
   * @param targetVariablesToAdd ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  @Override
  public MethodCallResult<? extends @Nullable StatusCode @Nullable []>
      callCreateTargetVariablesDetailed(
          MethodCallOptions options,
          @Nullable ConfigurationVersionDataType configurationVersion,
          @Nullable FieldTargetDataType @Nullable [] targetVariablesToAdd)
          throws UaException {
    Objects.requireNonNull(options, "options");
    var awaitedMethod =
        callCreateTargetVariablesDetailedAsync(options, configurationVersion, targetVariablesToAdd);
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
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.8/#9.1.8.5
   *
   * <p>Invokes <code>CreateTargetVariables</code> on this node's ObjectId using the effective
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
   * @param configurationVersion ; the supplied payload may be null.
   * @param targetVariablesToAdd ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  @Override
  public CompletableFuture<? extends MethodCallResult<? extends @Nullable StatusCode @Nullable []>>
      callCreateTargetVariablesDetailedAsync(
          @Nullable ConfigurationVersionDataType configurationVersion,
          @Nullable FieldTargetDataType @Nullable [] targetVariablesToAdd) {
    return callCreateTargetVariablesDetailedAsync(
        MethodCallOptions.NONE, configurationVersion, targetVariablesToAdd);
  }

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.8/#9.1.8.5
   *
   * <p>Invokes <code>CreateTargetVariables</code> on this node's ObjectId using the effective
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
   * @param configurationVersion ; the supplied payload may be null.
   * @param targetVariablesToAdd ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  @Override
  public CompletableFuture<? extends MethodCallResult<? extends @Nullable StatusCode @Nullable []>>
      callCreateTargetVariablesDetailedAsync(
          MethodCallOptions options,
          @Nullable ConfigurationVersionDataType configurationVersion,
          @Nullable FieldTargetDataType @Nullable [] targetVariablesToAdd) {
    CompletableFuture<MethodCallResult<@Nullable StatusCode @Nullable []>> result =
        new CompletableFuture<>();
    try {
      Objects.requireNonNull(options, "options");
      List<@Nullable Object> rawInputs = new ArrayList<>();
      rawInputs.add(configurationVersion);
      rawInputs.add(targetVariablesToAdd);
      var lookup = getCreateTargetVariablesMethodNodeAsync();
      result.whenComplete(
          (value, failure) -> {
            if (result.isCancelled()) {
              lookup.cancel(false);
            }
          });
      CompletableFuture<MethodCallResult<@Nullable StatusCode @Nullable []>> pipeline =
          lookup.thenCompose(
              methodNode -> {
                if (result.isCancelled()) {
                  return CompletableFuture.failedFuture(new CancellationException());
                }
                if (methodNode == null) {
                  return CompletableFuture.failedFuture(
                      new UaException(
                          StatusCodes.Bad_NotFound,
                          "Method node is required for invocation: CreateTargetVariables"));
                }
                var inputMetadata =
                    ClientDataTypes.read(
                        this.client,
                        List.<ExpandedNodeId>of(
                                ExpandedNodeId.parse("i=14593"), ExpandedNodeId.parse("i=14744"))
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
                                @Nullable ConfigurationVersionDataType convertedValue;
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
                                        ExpandedNodeId.parse("i=14593")
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
                                          "Method argument ConfigurationVersion (effective property"
                                              + " i=17387, DataType i=14593) is unavailable in the"
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
                                    convertedValue = (ConfigurationVersionDataType) methodValue;
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
                                @Nullable FieldTargetDataType @Nullable [] convertedValue;
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
                                        ExpandedNodeId.parse("i=14744")
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
                                          "Method argument TargetVariablesToAdd (effective property"
                                              + " i=17387, DataType i=14744) is unavailable in the"
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
                                      if (!(valueRank == 1 || emptyArray)) {
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
                                      if (!emptyArray) {
                                        int[] dimensions =
                                            methodValue instanceof Matrix
                                                ? ((Matrix) methodValue).getDimensions()
                                                : ArrayUtil.getDimensions(methodValue);
                                        long[] maximumDimensions = new long[] {0L};
                                        if (dimensions.length != maximumDimensions.length) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "Method argument ArrayDimensions mismatch");
                                        }
                                        for (int dimensionIndex = 0;
                                            dimensionIndex < dimensions.length;
                                            dimensionIndex++) {
                                          if (maximumDimensions[dimensionIndex] != 0
                                              && dimensions[dimensionIndex]
                                                  > maximumDimensions[dimensionIndex]) {
                                            throw new UaException(
                                                StatusCodes.Bad_TypeMismatch,
                                                "Method argument exceeds ArrayDimensions maximum");
                                          }
                                        }
                                      }
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
                                    if (methodValue == null) {
                                      convertedValue = null;
                                    } else {
                                      convertedValue =
                                          new FieldTargetDataType[Array.getLength(methodValue)];
                                      for (int valueIndex = 0;
                                          valueIndex < convertedValue.length;
                                          valueIndex++) {
                                        Object valueElement = Array.get(methodValue, valueIndex);
                                        convertedValue[valueIndex] =
                                            (FieldTargetDataType) valueElement;
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
                                                              ExpandedNodeId.parse("i=19")),
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
                                                            @Nullable StatusCode @Nullable []
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
                                                                    ExpandedNodeId.parse("i=19")
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
                                                                      "Method argument AddResults"
                                                                          + " (effective property"
                                                                          + " i=17388, DataType"
                                                                          + " i=19) is unavailable"
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
                                                                      new StatusCode
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
                                                                        (StatusCode) valueElement;
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
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.8/#9.1.8.6
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
  public @Nullable UaMethodNode getCreateDataSetMirrorMethodNode() throws UaException {
    try {
      return getCreateDataSetMirrorMethodNodeAsync().get();
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
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.8/#9.1.8.6
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
      getCreateDataSetMirrorMethodNodeAsync() {
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
                                "http://opcfoundation.org/UA/:CreateDataSetMirror (declaration"
                                    + " i=17389, owner i=15306) on "
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
                                        new QualifiedName(namespaceIndex, "CreateDataSetMirror"))
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
                                          "http://opcfoundation.org/UA/:CreateDataSetMirror"
                                              + " (declaration i=17389, owner i=15306) on "
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
                                          "http://opcfoundation.org/UA/:CreateDataSetMirror"
                                              + " (declaration i=17389, owner i=15306)"));
                                }
                                var targets = operation.getTargets();
                                if (targets == null || targets.length == 0) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_UnexpectedError,
                                          "http://opcfoundation.org/UA/:CreateDataSetMirror"
                                              + " (declaration i=17389, owner i=15306) on "
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
                                            "http://opcfoundation.org/UA/:CreateDataSetMirror"
                                                + " (declaration i=17389, owner i=15306) on "
                                                + getNodeId()));
                                  }
                                  if (!target.getTargetId().isLocal()) {
                                    return CompletableFuture.failedFuture(
                                        new UaException(
                                            StatusCodes.Bad_NotSupported,
                                            "http://opcfoundation.org/UA/:CreateDataSetMirror"
                                                + " (declaration i=17389, owner i=15306) on "
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
                                                            "http://opcfoundation.org/UA/:CreateDataSetMirror"
                                                                + " (declaration i=17389, owner"
                                                                + " i=15306) on "
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
                                                    "http://opcfoundation.org/UA/:CreateDataSetMirror"
                                                        + " (declaration i=17389, owner i=15306) on"
                                                        + " "
                                                        + getNodeId()));
                                          }
                                          if (unique.size() != 1) {
                                            return CompletableFuture.failedFuture(
                                                new UaException(
                                                    StatusCodes.Bad_TooManyMatches,
                                                    "http://opcfoundation.org/UA/:CreateDataSetMirror"
                                                        + " (declaration i=17389, owner i=15306) on"
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
                                                            != NodeClass.Method) {
                                                      return CompletableFuture.failedFuture(
                                                          new UaException(
                                                              StatusCodes.Bad_NodeClassInvalid,
                                                              "http://opcfoundation.org/UA/:CreateDataSetMirror"
                                                                  + " (declaration i=17389, owner"
                                                                  + " i=15306) on "
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
                      "http://opcfoundation.org/UA/:CreateDataSetMirror (declaration i=17389, owner"
                          + " i=15306)"));
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
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.8/#9.1.8.6
   *
   * <p>Invokes <code>CreateDataSetMirror</code> on this node's ObjectId using the effective Method
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
   * @param parentNodeName ; the supplied payload may be null.
   * @param rolePermissions ; the supplied payload may be null.
   * @return the output value or object, or its future; a single value may be null.
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  @Override
  public @Nullable NodeId callCreateDataSetMirror(
      @Nullable String parentNodeName, @Nullable RolePermissionType @Nullable [] rolePermissions)
      throws UaException {
    return callCreateDataSetMirrorDetailed(parentNodeName, rolePermissions).requireGood();
  }

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.8/#9.1.8.6
   *
   * <p>Invokes <code>CreateDataSetMirror</code> on this node's ObjectId using the effective Method
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
   * @param parentNodeName ; the supplied payload may be null.
   * @param rolePermissions ; the supplied payload may be null.
   * @return the output value or object, or its future; a single value may be null.
   */
  @NullMarked
  @Override
  public CompletableFuture<? extends @Nullable NodeId> callCreateDataSetMirrorAsync(
      @Nullable String parentNodeName, @Nullable RolePermissionType @Nullable [] rolePermissions) {
    CompletableFuture<@Nullable NodeId> result = new CompletableFuture<>();
    var call = callCreateDataSetMirrorDetailedAsync(parentNodeName, rolePermissions);
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
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.8/#9.1.8.6
   *
   * <p>Invokes <code>CreateDataSetMirror</code> on this node's ObjectId using the effective Method
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
   * @param parentNodeName ; the supplied payload may be null.
   * @param rolePermissions ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  @Override
  public MethodCallResult<? extends @Nullable NodeId> callCreateDataSetMirrorDetailed(
      @Nullable String parentNodeName, @Nullable RolePermissionType @Nullable [] rolePermissions)
      throws UaException {
    return callCreateDataSetMirrorDetailed(MethodCallOptions.NONE, parentNodeName, rolePermissions);
  }

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.8/#9.1.8.6
   *
   * <p>Invokes <code>CreateDataSetMirror</code> on this node's ObjectId using the effective Method
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
   * @param parentNodeName ; the supplied payload may be null.
   * @param rolePermissions ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  @Override
  public MethodCallResult<? extends @Nullable NodeId> callCreateDataSetMirrorDetailed(
      MethodCallOptions options,
      @Nullable String parentNodeName,
      @Nullable RolePermissionType @Nullable [] rolePermissions)
      throws UaException {
    Objects.requireNonNull(options, "options");
    var awaitedMethod =
        callCreateDataSetMirrorDetailedAsync(options, parentNodeName, rolePermissions);
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
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.8/#9.1.8.6
   *
   * <p>Invokes <code>CreateDataSetMirror</code> on this node's ObjectId using the effective Method
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
   * @param parentNodeName ; the supplied payload may be null.
   * @param rolePermissions ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  @Override
  public CompletableFuture<? extends MethodCallResult<? extends @Nullable NodeId>>
      callCreateDataSetMirrorDetailedAsync(
          @Nullable String parentNodeName,
          @Nullable RolePermissionType @Nullable [] rolePermissions) {
    return callCreateDataSetMirrorDetailedAsync(
        MethodCallOptions.NONE, parentNodeName, rolePermissions);
  }

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.8/#9.1.8.6
   *
   * <p>Invokes <code>CreateDataSetMirror</code> on this node's ObjectId using the effective Method
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
   * @param parentNodeName ; the supplied payload may be null.
   * @param rolePermissions ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  @Override
  public CompletableFuture<? extends MethodCallResult<? extends @Nullable NodeId>>
      callCreateDataSetMirrorDetailedAsync(
          MethodCallOptions options,
          @Nullable String parentNodeName,
          @Nullable RolePermissionType @Nullable [] rolePermissions) {
    CompletableFuture<MethodCallResult<@Nullable NodeId>> result = new CompletableFuture<>();
    try {
      Objects.requireNonNull(options, "options");
      List<@Nullable Object> rawInputs = new ArrayList<>();
      rawInputs.add(parentNodeName);
      rawInputs.add(rolePermissions);
      var lookup = getCreateDataSetMirrorMethodNodeAsync();
      result.whenComplete(
          (value, failure) -> {
            if (result.isCancelled()) {
              lookup.cancel(false);
            }
          });
      CompletableFuture<MethodCallResult<@Nullable NodeId>> pipeline =
          lookup.thenCompose(
              methodNode -> {
                if (result.isCancelled()) {
                  return CompletableFuture.failedFuture(new CancellationException());
                }
                if (methodNode == null) {
                  return CompletableFuture.failedFuture(
                      new UaException(
                          StatusCodes.Bad_NotFound,
                          "Method node is required for invocation: CreateDataSetMirror"));
                }
                var inputMetadata =
                    ClientDataTypes.read(
                        this.client,
                        List.<ExpandedNodeId>of(
                                ExpandedNodeId.parse("i=12"), ExpandedNodeId.parse("i=96"))
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
                                @Nullable String convertedValue;
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
                                        ExpandedNodeId.parse("i=12")
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
                                          "Method argument ParentNodeName (effective property"
                                              + " i=17390, DataType i=12) is unavailable in the"
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
                                    convertedValue = (String) methodValue;
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
                                @Nullable RolePermissionType @Nullable [] convertedValue;
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
                                        ExpandedNodeId.parse("i=96")
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
                                          "Method argument RolePermissions (effective property"
                                              + " i=17390, DataType i=96) is unavailable in the"
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
                                      if (!(valueRank == 1 || emptyArray)) {
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
                                      if (!emptyArray) {
                                        int[] dimensions =
                                            methodValue instanceof Matrix
                                                ? ((Matrix) methodValue).getDimensions()
                                                : ArrayUtil.getDimensions(methodValue);
                                        long[] maximumDimensions = new long[] {0L};
                                        if (dimensions.length != maximumDimensions.length) {
                                          throw new UaException(
                                              StatusCodes.Bad_TypeMismatch,
                                              "Method argument ArrayDimensions mismatch");
                                        }
                                        for (int dimensionIndex = 0;
                                            dimensionIndex < dimensions.length;
                                            dimensionIndex++) {
                                          if (maximumDimensions[dimensionIndex] != 0
                                              && dimensions[dimensionIndex]
                                                  > maximumDimensions[dimensionIndex]) {
                                            throw new UaException(
                                                StatusCodes.Bad_TypeMismatch,
                                                "Method argument exceeds ArrayDimensions maximum");
                                          }
                                        }
                                      }
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
                                    if (methodValue == null) {
                                      convertedValue = null;
                                    } else {
                                      convertedValue =
                                          new RolePermissionType[Array.getLength(methodValue)];
                                      for (int valueIndex = 0;
                                          valueIndex < convertedValue.length;
                                          valueIndex++) {
                                        Object valueElement = Array.get(methodValue, valueIndex);
                                        convertedValue[valueIndex] =
                                            (RolePermissionType) valueElement;
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
                                                              ExpandedNodeId.parse("i=17")),
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
                                                            @Nullable NodeId decoded0;
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
                                                                    ExpandedNodeId.parse("i=17")
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
                                                                      "Method argument ParentNodeId"
                                                                          + " (effective property"
                                                                          + " i=17391, DataType"
                                                                          + " i=17) is unavailable"
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
                                                                decoded0 = (NodeId) methodValue;
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
}

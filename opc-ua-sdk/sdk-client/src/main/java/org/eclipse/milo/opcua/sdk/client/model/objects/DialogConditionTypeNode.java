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
import org.eclipse.milo.opcua.sdk.client.model.variables.TwoStateVariableTypeNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaNode;
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
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowsePath;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.RelativePath;
import org.eclipse.milo.opcua.stack.core.types.structured.RelativePathElement;
import org.eclipse.milo.opcua.stack.core.types.structured.RequestHeader;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.ArrayUtil;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

public class DialogConditionTypeNode extends ConditionTypeNode implements DialogConditionType {
  public DialogConditionTypeNode(
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
  public static ClientViews createViews(DialogConditionTypeNode node) {
    Objects.requireNonNull(node, "node");
    return ClientViews.forNode(node.client, node);
  }

  @Override
  public @Nullable LocalizedText getPrompt() throws UaException {
    PropertyTypeNode node = getPromptNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:Prompt (declaration i=2831, owner i=2830)"
              + " on "
              + getNodeId());
    }
    return (LocalizedText) node.getValue().getValue().getValue();
  }

  @Override
  public void setPrompt(@Nullable LocalizedText value) throws UaException {
    PropertyTypeNode node = getPromptNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:Prompt (declaration i=2831, owner i=2830)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable LocalizedText readPrompt() throws UaException {
    try {
      return readPromptAsync().get();
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
  public void writePrompt(@Nullable LocalizedText value) throws UaException {
    try {
      StatusCode statusCode = writePromptAsync(value).get();
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
  public CompletableFuture<? extends @Nullable LocalizedText> readPromptAsync() {
    return getPromptNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:Prompt (declaration i=2831, owner i=2830)"
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
  public CompletableFuture<StatusCode> writePromptAsync(@Nullable LocalizedText prompt) {
    return getPromptNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:Prompt (declaration i=2831, owner i=2830)"
                            + " on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(prompt));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getPromptNode() throws UaException {
    try {
      return getPromptNodeAsync().get();
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
  public CompletableFuture<? extends PropertyTypeNode> getPromptNodeAsync() {
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
                                "http://opcfoundation.org/UA/:Prompt (declaration i=2831, owner"
                                    + " i=2830) on "
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
                                        new QualifiedName(namespaceIndex, "Prompt"))
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
                                          "http://opcfoundation.org/UA/:Prompt (declaration i=2831,"
                                              + " owner i=2830) on "
                                              + getNodeId()));
                                }
                                var operation = results[0];
                                if (operation.getStatusCode().getValue()
                                    == StatusCodes.Bad_NoMatch) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_NotFound,
                                          "http://opcfoundation.org/UA/:Prompt (declaration i=2831,"
                                              + " owner i=2830) on "
                                              + getNodeId()));
                                }
                                if (!operation.getStatusCode().isGood()) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          operation.getStatusCode(),
                                          "http://opcfoundation.org/UA/:Prompt (declaration i=2831,"
                                              + " owner i=2830)"));
                                }
                                var targets = operation.getTargets();
                                if (targets == null || targets.length == 0) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_UnexpectedError,
                                          "http://opcfoundation.org/UA/:Prompt (declaration i=2831,"
                                              + " owner i=2830) on "
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
                                            "http://opcfoundation.org/UA/:Prompt (declaration"
                                                + " i=2831, owner i=2830) on "
                                                + getNodeId()));
                                  }
                                  if (!target.getTargetId().isLocal()) {
                                    return CompletableFuture.failedFuture(
                                        new UaException(
                                            StatusCodes.Bad_NotSupported,
                                            "http://opcfoundation.org/UA/:Prompt (declaration"
                                                + " i=2831, owner i=2830) on "
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
                                                            "http://opcfoundation.org/UA/:Prompt"
                                                                + " (declaration i=2831, owner"
                                                                + " i=2830) on "
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
                                                    "http://opcfoundation.org/UA/:Prompt"
                                                        + " (declaration i=2831, owner i=2830) on "
                                                        + getNodeId()));
                                          }
                                          if (unique.size() != 1) {
                                            return CompletableFuture.failedFuture(
                                                new UaException(
                                                    StatusCodes.Bad_TooManyMatches,
                                                    "http://opcfoundation.org/UA/:Prompt"
                                                        + " (declaration i=2831, owner i=2830) on "
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
                                                              "http://opcfoundation.org/UA/:Prompt"
                                                                  + " (declaration i=2831, owner"
                                                                  + " i=2830) on "
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
                      "http://opcfoundation.org/UA/:Prompt (declaration i=2831, owner i=2830)"));
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
  public @Nullable LocalizedText @Nullable [] getResponseOptionSet() throws UaException {
    PropertyTypeNode node = getResponseOptionSetNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ResponseOptionSet (declaration i=9064, owner i=2830)"
              + " on "
              + getNodeId());
    }
    return (LocalizedText[]) node.getValue().getValue().getValue();
  }

  @Override
  public void setResponseOptionSet(@Nullable LocalizedText @Nullable [] value) throws UaException {
    PropertyTypeNode node = getResponseOptionSetNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ResponseOptionSet (declaration i=9064, owner i=2830)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable LocalizedText @Nullable [] readResponseOptionSet() throws UaException {
    try {
      return readResponseOptionSetAsync().get();
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
  public void writeResponseOptionSet(@Nullable LocalizedText @Nullable [] value)
      throws UaException {
    try {
      StatusCode statusCode = writeResponseOptionSetAsync(value).get();
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
  public CompletableFuture<? extends @Nullable LocalizedText @Nullable []>
      readResponseOptionSetAsync() {
    return getResponseOptionSetNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ResponseOptionSet (declaration i=9064, owner"
                            + " i=2830) on "
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
                return (LocalizedText[]) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeResponseOptionSetAsync(
      @Nullable LocalizedText @Nullable [] responseOptionSet) {
    return getResponseOptionSetNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ResponseOptionSet (declaration i=9064, owner"
                            + " i=2830) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(responseOptionSet));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getResponseOptionSetNode() throws UaException {
    try {
      return getResponseOptionSetNodeAsync().get();
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
  public CompletableFuture<? extends PropertyTypeNode> getResponseOptionSetNodeAsync() {
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
                                "http://opcfoundation.org/UA/:ResponseOptionSet (declaration"
                                    + " i=9064, owner i=2830) on "
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
                                        new QualifiedName(namespaceIndex, "ResponseOptionSet"))
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
                                          "http://opcfoundation.org/UA/:ResponseOptionSet"
                                              + " (declaration i=9064, owner i=2830) on "
                                              + getNodeId()));
                                }
                                var operation = results[0];
                                if (operation.getStatusCode().getValue()
                                    == StatusCodes.Bad_NoMatch) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_NotFound,
                                          "http://opcfoundation.org/UA/:ResponseOptionSet"
                                              + " (declaration i=9064, owner i=2830) on "
                                              + getNodeId()));
                                }
                                if (!operation.getStatusCode().isGood()) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          operation.getStatusCode(),
                                          "http://opcfoundation.org/UA/:ResponseOptionSet"
                                              + " (declaration i=9064, owner i=2830)"));
                                }
                                var targets = operation.getTargets();
                                if (targets == null || targets.length == 0) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_UnexpectedError,
                                          "http://opcfoundation.org/UA/:ResponseOptionSet"
                                              + " (declaration i=9064, owner i=2830) on "
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
                                            "http://opcfoundation.org/UA/:ResponseOptionSet"
                                                + " (declaration i=9064, owner i=2830) on "
                                                + getNodeId()));
                                  }
                                  if (!target.getTargetId().isLocal()) {
                                    return CompletableFuture.failedFuture(
                                        new UaException(
                                            StatusCodes.Bad_NotSupported,
                                            "http://opcfoundation.org/UA/:ResponseOptionSet"
                                                + " (declaration i=9064, owner i=2830) on "
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
                                                            "http://opcfoundation.org/UA/:ResponseOptionSet"
                                                                + " (declaration i=9064, owner"
                                                                + " i=2830) on "
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
                                                    "http://opcfoundation.org/UA/:ResponseOptionSet"
                                                        + " (declaration i=9064, owner i=2830) on "
                                                        + getNodeId()));
                                          }
                                          if (unique.size() != 1) {
                                            return CompletableFuture.failedFuture(
                                                new UaException(
                                                    StatusCodes.Bad_TooManyMatches,
                                                    "http://opcfoundation.org/UA/:ResponseOptionSet"
                                                        + " (declaration i=9064, owner i=2830) on "
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
                                                              "http://opcfoundation.org/UA/:ResponseOptionSet"
                                                                  + " (declaration i=9064, owner"
                                                                  + " i=2830) on "
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
                      "http://opcfoundation.org/UA/:ResponseOptionSet (declaration i=9064, owner"
                          + " i=2830)"));
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
  public @Nullable Integer getDefaultResponse() throws UaException {
    PropertyTypeNode node = getDefaultResponseNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DefaultResponse (declaration i=9065, owner i=2830)"
              + " on "
              + getNodeId());
    }
    return (Integer) node.getValue().getValue().getValue();
  }

  @Override
  public void setDefaultResponse(@Nullable Integer value) throws UaException {
    PropertyTypeNode node = getDefaultResponseNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DefaultResponse (declaration i=9065, owner i=2830)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable Integer readDefaultResponse() throws UaException {
    try {
      return readDefaultResponseAsync().get();
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
  public void writeDefaultResponse(@Nullable Integer value) throws UaException {
    try {
      StatusCode statusCode = writeDefaultResponseAsync(value).get();
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
  public CompletableFuture<? extends @Nullable Integer> readDefaultResponseAsync() {
    return getDefaultResponseNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:DefaultResponse (declaration i=9065, owner"
                            + " i=2830) on "
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
                return (Integer) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeDefaultResponseAsync(
      @Nullable Integer defaultResponse) {
    return getDefaultResponseNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:DefaultResponse (declaration i=9065, owner"
                            + " i=2830) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(defaultResponse));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getDefaultResponseNode() throws UaException {
    try {
      return getDefaultResponseNodeAsync().get();
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
  public CompletableFuture<? extends PropertyTypeNode> getDefaultResponseNodeAsync() {
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
                                "http://opcfoundation.org/UA/:DefaultResponse (declaration i=9065,"
                                    + " owner i=2830) on "
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
                                        new QualifiedName(namespaceIndex, "DefaultResponse"))
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
                                          "http://opcfoundation.org/UA/:DefaultResponse"
                                              + " (declaration i=9065, owner i=2830) on "
                                              + getNodeId()));
                                }
                                var operation = results[0];
                                if (operation.getStatusCode().getValue()
                                    == StatusCodes.Bad_NoMatch) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_NotFound,
                                          "http://opcfoundation.org/UA/:DefaultResponse"
                                              + " (declaration i=9065, owner i=2830) on "
                                              + getNodeId()));
                                }
                                if (!operation.getStatusCode().isGood()) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          operation.getStatusCode(),
                                          "http://opcfoundation.org/UA/:DefaultResponse"
                                              + " (declaration i=9065, owner i=2830)"));
                                }
                                var targets = operation.getTargets();
                                if (targets == null || targets.length == 0) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_UnexpectedError,
                                          "http://opcfoundation.org/UA/:DefaultResponse"
                                              + " (declaration i=9065, owner i=2830) on "
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
                                            "http://opcfoundation.org/UA/:DefaultResponse"
                                                + " (declaration i=9065, owner i=2830) on "
                                                + getNodeId()));
                                  }
                                  if (!target.getTargetId().isLocal()) {
                                    return CompletableFuture.failedFuture(
                                        new UaException(
                                            StatusCodes.Bad_NotSupported,
                                            "http://opcfoundation.org/UA/:DefaultResponse"
                                                + " (declaration i=9065, owner i=2830) on "
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
                                                            "http://opcfoundation.org/UA/:DefaultResponse"
                                                                + " (declaration i=9065, owner"
                                                                + " i=2830) on "
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
                                                    "http://opcfoundation.org/UA/:DefaultResponse"
                                                        + " (declaration i=9065, owner i=2830) on "
                                                        + getNodeId()));
                                          }
                                          if (unique.size() != 1) {
                                            return CompletableFuture.failedFuture(
                                                new UaException(
                                                    StatusCodes.Bad_TooManyMatches,
                                                    "http://opcfoundation.org/UA/:DefaultResponse"
                                                        + " (declaration i=9065, owner i=2830) on "
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
                                                              "http://opcfoundation.org/UA/:DefaultResponse"
                                                                  + " (declaration i=9065, owner"
                                                                  + " i=2830) on "
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
                      "http://opcfoundation.org/UA/:DefaultResponse (declaration i=9065, owner"
                          + " i=2830)"));
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
  public @Nullable Integer getOkResponse() throws UaException {
    PropertyTypeNode node = getOkResponseNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:OkResponse (declaration i=9066, owner i=2830)"
              + " on "
              + getNodeId());
    }
    return (Integer) node.getValue().getValue().getValue();
  }

  @Override
  public void setOkResponse(@Nullable Integer value) throws UaException {
    PropertyTypeNode node = getOkResponseNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:OkResponse (declaration i=9066, owner i=2830)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable Integer readOkResponse() throws UaException {
    try {
      return readOkResponseAsync().get();
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
  public void writeOkResponse(@Nullable Integer value) throws UaException {
    try {
      StatusCode statusCode = writeOkResponseAsync(value).get();
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
  public CompletableFuture<? extends @Nullable Integer> readOkResponseAsync() {
    return getOkResponseNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:OkResponse (declaration i=9066, owner i=2830)"
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
                return (Integer) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeOkResponseAsync(@Nullable Integer okResponse) {
    return getOkResponseNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:OkResponse (declaration i=9066, owner i=2830)"
                            + " on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(okResponse));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getOkResponseNode() throws UaException {
    try {
      return getOkResponseNodeAsync().get();
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
  public CompletableFuture<? extends PropertyTypeNode> getOkResponseNodeAsync() {
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
                                "http://opcfoundation.org/UA/:OkResponse (declaration i=9066, owner"
                                    + " i=2830) on "
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
                                        new QualifiedName(namespaceIndex, "OkResponse"))
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
                                          "http://opcfoundation.org/UA/:OkResponse (declaration"
                                              + " i=9066, owner i=2830) on "
                                              + getNodeId()));
                                }
                                var operation = results[0];
                                if (operation.getStatusCode().getValue()
                                    == StatusCodes.Bad_NoMatch) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_NotFound,
                                          "http://opcfoundation.org/UA/:OkResponse (declaration"
                                              + " i=9066, owner i=2830) on "
                                              + getNodeId()));
                                }
                                if (!operation.getStatusCode().isGood()) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          operation.getStatusCode(),
                                          "http://opcfoundation.org/UA/:OkResponse (declaration"
                                              + " i=9066, owner i=2830)"));
                                }
                                var targets = operation.getTargets();
                                if (targets == null || targets.length == 0) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_UnexpectedError,
                                          "http://opcfoundation.org/UA/:OkResponse (declaration"
                                              + " i=9066, owner i=2830) on "
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
                                            "http://opcfoundation.org/UA/:OkResponse (declaration"
                                                + " i=9066, owner i=2830) on "
                                                + getNodeId()));
                                  }
                                  if (!target.getTargetId().isLocal()) {
                                    return CompletableFuture.failedFuture(
                                        new UaException(
                                            StatusCodes.Bad_NotSupported,
                                            "http://opcfoundation.org/UA/:OkResponse (declaration"
                                                + " i=9066, owner i=2830) on "
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
                                                            "http://opcfoundation.org/UA/:OkResponse"
                                                                + " (declaration i=9066, owner"
                                                                + " i=2830) on "
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
                                                    "http://opcfoundation.org/UA/:OkResponse"
                                                        + " (declaration i=9066, owner i=2830) on "
                                                        + getNodeId()));
                                          }
                                          if (unique.size() != 1) {
                                            return CompletableFuture.failedFuture(
                                                new UaException(
                                                    StatusCodes.Bad_TooManyMatches,
                                                    "http://opcfoundation.org/UA/:OkResponse"
                                                        + " (declaration i=9066, owner i=2830) on "
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
                                                              "http://opcfoundation.org/UA/:OkResponse"
                                                                  + " (declaration i=9066, owner"
                                                                  + " i=2830) on "
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
                      "http://opcfoundation.org/UA/:OkResponse (declaration i=9066, owner"
                          + " i=2830)"));
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
  public @Nullable Integer getCancelResponse() throws UaException {
    PropertyTypeNode node = getCancelResponseNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:CancelResponse (declaration i=9067, owner i=2830)"
              + " on "
              + getNodeId());
    }
    return (Integer) node.getValue().getValue().getValue();
  }

  @Override
  public void setCancelResponse(@Nullable Integer value) throws UaException {
    PropertyTypeNode node = getCancelResponseNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:CancelResponse (declaration i=9067, owner i=2830)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable Integer readCancelResponse() throws UaException {
    try {
      return readCancelResponseAsync().get();
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
  public void writeCancelResponse(@Nullable Integer value) throws UaException {
    try {
      StatusCode statusCode = writeCancelResponseAsync(value).get();
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
  public CompletableFuture<? extends @Nullable Integer> readCancelResponseAsync() {
    return getCancelResponseNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:CancelResponse (declaration i=9067, owner"
                            + " i=2830) on "
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
                return (Integer) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeCancelResponseAsync(@Nullable Integer cancelResponse) {
    return getCancelResponseNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:CancelResponse (declaration i=9067, owner"
                            + " i=2830) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(cancelResponse));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getCancelResponseNode() throws UaException {
    try {
      return getCancelResponseNodeAsync().get();
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
  public CompletableFuture<? extends PropertyTypeNode> getCancelResponseNodeAsync() {
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
                                "http://opcfoundation.org/UA/:CancelResponse (declaration i=9067,"
                                    + " owner i=2830) on "
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
                                        new QualifiedName(namespaceIndex, "CancelResponse"))
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
                                          "http://opcfoundation.org/UA/:CancelResponse (declaration"
                                              + " i=9067, owner i=2830) on "
                                              + getNodeId()));
                                }
                                var operation = results[0];
                                if (operation.getStatusCode().getValue()
                                    == StatusCodes.Bad_NoMatch) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_NotFound,
                                          "http://opcfoundation.org/UA/:CancelResponse (declaration"
                                              + " i=9067, owner i=2830) on "
                                              + getNodeId()));
                                }
                                if (!operation.getStatusCode().isGood()) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          operation.getStatusCode(),
                                          "http://opcfoundation.org/UA/:CancelResponse (declaration"
                                              + " i=9067, owner i=2830)"));
                                }
                                var targets = operation.getTargets();
                                if (targets == null || targets.length == 0) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_UnexpectedError,
                                          "http://opcfoundation.org/UA/:CancelResponse (declaration"
                                              + " i=9067, owner i=2830) on "
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
                                            "http://opcfoundation.org/UA/:CancelResponse"
                                                + " (declaration i=9067, owner i=2830) on "
                                                + getNodeId()));
                                  }
                                  if (!target.getTargetId().isLocal()) {
                                    return CompletableFuture.failedFuture(
                                        new UaException(
                                            StatusCodes.Bad_NotSupported,
                                            "http://opcfoundation.org/UA/:CancelResponse"
                                                + " (declaration i=9067, owner i=2830) on "
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
                                                            "http://opcfoundation.org/UA/:CancelResponse"
                                                                + " (declaration i=9067, owner"
                                                                + " i=2830) on "
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
                                                    "http://opcfoundation.org/UA/:CancelResponse"
                                                        + " (declaration i=9067, owner i=2830) on "
                                                        + getNodeId()));
                                          }
                                          if (unique.size() != 1) {
                                            return CompletableFuture.failedFuture(
                                                new UaException(
                                                    StatusCodes.Bad_TooManyMatches,
                                                    "http://opcfoundation.org/UA/:CancelResponse"
                                                        + " (declaration i=9067, owner i=2830) on "
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
                                                              "http://opcfoundation.org/UA/:CancelResponse"
                                                                  + " (declaration i=9067, owner"
                                                                  + " i=2830) on "
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
                      "http://opcfoundation.org/UA/:CancelResponse (declaration i=9067, owner"
                          + " i=2830)"));
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
  public @Nullable Integer getLastResponse() throws UaException {
    PropertyTypeNode node = getLastResponseNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:LastResponse (declaration i=9068, owner i=2830)"
              + " on "
              + getNodeId());
    }
    return (Integer) node.getValue().getValue().getValue();
  }

  @Override
  public void setLastResponse(@Nullable Integer value) throws UaException {
    PropertyTypeNode node = getLastResponseNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:LastResponse (declaration i=9068, owner i=2830)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable Integer readLastResponse() throws UaException {
    try {
      return readLastResponseAsync().get();
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
  public void writeLastResponse(@Nullable Integer value) throws UaException {
    try {
      StatusCode statusCode = writeLastResponseAsync(value).get();
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
  public CompletableFuture<? extends @Nullable Integer> readLastResponseAsync() {
    return getLastResponseNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:LastResponse (declaration i=9068, owner"
                            + " i=2830) on "
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
                return (Integer) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeLastResponseAsync(@Nullable Integer lastResponse) {
    return getLastResponseNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:LastResponse (declaration i=9068, owner"
                            + " i=2830) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(lastResponse));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getLastResponseNode() throws UaException {
    try {
      return getLastResponseNodeAsync().get();
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
  public CompletableFuture<? extends PropertyTypeNode> getLastResponseNodeAsync() {
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
                                "http://opcfoundation.org/UA/:LastResponse (declaration i=9068,"
                                    + " owner i=2830) on "
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
                                        new QualifiedName(namespaceIndex, "LastResponse"))
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
                                          "http://opcfoundation.org/UA/:LastResponse (declaration"
                                              + " i=9068, owner i=2830) on "
                                              + getNodeId()));
                                }
                                var operation = results[0];
                                if (operation.getStatusCode().getValue()
                                    == StatusCodes.Bad_NoMatch) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_NotFound,
                                          "http://opcfoundation.org/UA/:LastResponse (declaration"
                                              + " i=9068, owner i=2830) on "
                                              + getNodeId()));
                                }
                                if (!operation.getStatusCode().isGood()) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          operation.getStatusCode(),
                                          "http://opcfoundation.org/UA/:LastResponse (declaration"
                                              + " i=9068, owner i=2830)"));
                                }
                                var targets = operation.getTargets();
                                if (targets == null || targets.length == 0) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_UnexpectedError,
                                          "http://opcfoundation.org/UA/:LastResponse (declaration"
                                              + " i=9068, owner i=2830) on "
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
                                            "http://opcfoundation.org/UA/:LastResponse (declaration"
                                                + " i=9068, owner i=2830) on "
                                                + getNodeId()));
                                  }
                                  if (!target.getTargetId().isLocal()) {
                                    return CompletableFuture.failedFuture(
                                        new UaException(
                                            StatusCodes.Bad_NotSupported,
                                            "http://opcfoundation.org/UA/:LastResponse (declaration"
                                                + " i=9068, owner i=2830) on "
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
                                                            "http://opcfoundation.org/UA/:LastResponse"
                                                                + " (declaration i=9068, owner"
                                                                + " i=2830) on "
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
                                                    "http://opcfoundation.org/UA/:LastResponse"
                                                        + " (declaration i=9068, owner i=2830) on "
                                                        + getNodeId()));
                                          }
                                          if (unique.size() != 1) {
                                            return CompletableFuture.failedFuture(
                                                new UaException(
                                                    StatusCodes.Bad_TooManyMatches,
                                                    "http://opcfoundation.org/UA/:LastResponse"
                                                        + " (declaration i=9068, owner i=2830) on "
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
                                                              "http://opcfoundation.org/UA/:LastResponse"
                                                                  + " (declaration i=9068, owner"
                                                                  + " i=2830) on "
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
                      "http://opcfoundation.org/UA/:LastResponse (declaration i=9068, owner"
                          + " i=2830)"));
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
  public @Nullable LocalizedText getEnabledState() throws UaException {
    TwoStateVariableTypeNode node = getEnabledStateNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:EnabledState (declaration i=9035, owner i=2830)"
              + " on "
              + getNodeId());
    }
    return (LocalizedText) node.getValue().getValue().getValue();
  }

  @Override
  public void setEnabledState(@Nullable LocalizedText value) throws UaException {
    TwoStateVariableTypeNode node = getEnabledStateNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:EnabledState (declaration i=9035, owner i=2830)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable LocalizedText readEnabledState() throws UaException {
    try {
      return readEnabledStateAsync().get();
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
  public void writeEnabledState(@Nullable LocalizedText value) throws UaException {
    try {
      StatusCode statusCode = writeEnabledStateAsync(value).get();
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
  public CompletableFuture<? extends @Nullable LocalizedText> readEnabledStateAsync() {
    return getEnabledStateNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:EnabledState (declaration i=9035, owner"
                            + " i=2830) on "
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
  public CompletableFuture<StatusCode> writeEnabledStateAsync(
      @Nullable LocalizedText enabledState) {
    return getEnabledStateNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:EnabledState (declaration i=9035, owner"
                            + " i=2830) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(enabledState));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public TwoStateVariableTypeNode getEnabledStateNode() throws UaException {
    try {
      return getEnabledStateNodeAsync().get();
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
  public CompletableFuture<? extends TwoStateVariableTypeNode> getEnabledStateNodeAsync() {
    CompletableFuture<TwoStateVariableTypeNode> result = new CompletableFuture<>();
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
                                "http://opcfoundation.org/UA/:EnabledState (declaration i=9035,"
                                    + " owner i=2830) on "
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
                                        new QualifiedName(namespaceIndex, "EnabledState"))
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
                                          "http://opcfoundation.org/UA/:EnabledState (declaration"
                                              + " i=9035, owner i=2830) on "
                                              + getNodeId()));
                                }
                                var operation = results[0];
                                if (operation.getStatusCode().getValue()
                                    == StatusCodes.Bad_NoMatch) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_NotFound,
                                          "http://opcfoundation.org/UA/:EnabledState (declaration"
                                              + " i=9035, owner i=2830) on "
                                              + getNodeId()));
                                }
                                if (!operation.getStatusCode().isGood()) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          operation.getStatusCode(),
                                          "http://opcfoundation.org/UA/:EnabledState (declaration"
                                              + " i=9035, owner i=2830)"));
                                }
                                var targets = operation.getTargets();
                                if (targets == null || targets.length == 0) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_UnexpectedError,
                                          "http://opcfoundation.org/UA/:EnabledState (declaration"
                                              + " i=9035, owner i=2830) on "
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
                                            "http://opcfoundation.org/UA/:EnabledState (declaration"
                                                + " i=9035, owner i=2830) on "
                                                + getNodeId()));
                                  }
                                  if (!target.getTargetId().isLocal()) {
                                    return CompletableFuture.failedFuture(
                                        new UaException(
                                            StatusCodes.Bad_NotSupported,
                                            "http://opcfoundation.org/UA/:EnabledState (declaration"
                                                + " i=9035, owner i=2830) on "
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
                                                            "http://opcfoundation.org/UA/:EnabledState"
                                                                + " (declaration i=9035, owner"
                                                                + " i=2830) on "
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
                                                    "http://opcfoundation.org/UA/:EnabledState"
                                                        + " (declaration i=9035, owner i=2830) on "
                                                        + getNodeId()));
                                          }
                                          if (unique.size() != 1) {
                                            return CompletableFuture.failedFuture(
                                                new UaException(
                                                    StatusCodes.Bad_TooManyMatches,
                                                    "http://opcfoundation.org/UA/:EnabledState"
                                                        + " (declaration i=9035, owner i=2830) on "
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
                                                              "http://opcfoundation.org/UA/:EnabledState"
                                                                  + " (declaration i=9035, owner"
                                                                  + " i=2830) on "
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
            } else if (node != null && !(node instanceof TwoStateVariableTypeNode)) {
              result.completeExceptionally(
                  new UaException(
                      StatusCodes.Bad_TypeMismatch,
                      "http://opcfoundation.org/UA/:EnabledState (declaration i=9035, owner"
                          + " i=2830)"));
            } else {
              result.complete((TwoStateVariableTypeNode) node);
            }
          });
    } catch (Exception e) {
      result.completeExceptionally(e);
    }
    return result;
  }

  @Override
  public @Nullable LocalizedText getDialogState() throws UaException {
    TwoStateVariableTypeNode node = getDialogStateNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DialogState (declaration i=9055, owner i=2830)"
              + " on "
              + getNodeId());
    }
    return (LocalizedText) node.getValue().getValue().getValue();
  }

  @Override
  public void setDialogState(@Nullable LocalizedText value) throws UaException {
    TwoStateVariableTypeNode node = getDialogStateNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DialogState (declaration i=9055, owner i=2830)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable LocalizedText readDialogState() throws UaException {
    try {
      return readDialogStateAsync().get();
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
  public void writeDialogState(@Nullable LocalizedText value) throws UaException {
    try {
      StatusCode statusCode = writeDialogStateAsync(value).get();
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
  public CompletableFuture<? extends @Nullable LocalizedText> readDialogStateAsync() {
    return getDialogStateNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:DialogState (declaration i=9055, owner"
                            + " i=2830) on "
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
  public CompletableFuture<StatusCode> writeDialogStateAsync(@Nullable LocalizedText dialogState) {
    return getDialogStateNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:DialogState (declaration i=9055, owner"
                            + " i=2830) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(dialogState));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public TwoStateVariableTypeNode getDialogStateNode() throws UaException {
    try {
      return getDialogStateNodeAsync().get();
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
  public CompletableFuture<? extends TwoStateVariableTypeNode> getDialogStateNodeAsync() {
    CompletableFuture<TwoStateVariableTypeNode> result = new CompletableFuture<>();
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
                                "http://opcfoundation.org/UA/:DialogState (declaration i=9055,"
                                    + " owner i=2830) on "
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
                                        new QualifiedName(namespaceIndex, "DialogState"))
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
                                          "http://opcfoundation.org/UA/:DialogState (declaration"
                                              + " i=9055, owner i=2830) on "
                                              + getNodeId()));
                                }
                                var operation = results[0];
                                if (operation.getStatusCode().getValue()
                                    == StatusCodes.Bad_NoMatch) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_NotFound,
                                          "http://opcfoundation.org/UA/:DialogState (declaration"
                                              + " i=9055, owner i=2830) on "
                                              + getNodeId()));
                                }
                                if (!operation.getStatusCode().isGood()) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          operation.getStatusCode(),
                                          "http://opcfoundation.org/UA/:DialogState (declaration"
                                              + " i=9055, owner i=2830)"));
                                }
                                var targets = operation.getTargets();
                                if (targets == null || targets.length == 0) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_UnexpectedError,
                                          "http://opcfoundation.org/UA/:DialogState (declaration"
                                              + " i=9055, owner i=2830) on "
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
                                            "http://opcfoundation.org/UA/:DialogState (declaration"
                                                + " i=9055, owner i=2830) on "
                                                + getNodeId()));
                                  }
                                  if (!target.getTargetId().isLocal()) {
                                    return CompletableFuture.failedFuture(
                                        new UaException(
                                            StatusCodes.Bad_NotSupported,
                                            "http://opcfoundation.org/UA/:DialogState (declaration"
                                                + " i=9055, owner i=2830) on "
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
                                                            "http://opcfoundation.org/UA/:DialogState"
                                                                + " (declaration i=9055, owner"
                                                                + " i=2830) on "
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
                                                    "http://opcfoundation.org/UA/:DialogState"
                                                        + " (declaration i=9055, owner i=2830) on "
                                                        + getNodeId()));
                                          }
                                          if (unique.size() != 1) {
                                            return CompletableFuture.failedFuture(
                                                new UaException(
                                                    StatusCodes.Bad_TooManyMatches,
                                                    "http://opcfoundation.org/UA/:DialogState"
                                                        + " (declaration i=9055, owner i=2830) on "
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
                                                              "http://opcfoundation.org/UA/:DialogState"
                                                                  + " (declaration i=9055, owner"
                                                                  + " i=2830) on "
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
            } else if (node != null && !(node instanceof TwoStateVariableTypeNode)) {
              result.completeExceptionally(
                  new UaException(
                      StatusCodes.Bad_TypeMismatch,
                      "http://opcfoundation.org/UA/:DialogState (declaration i=9055, owner"
                          + " i=2830)"));
            } else {
              result.complete((TwoStateVariableTypeNode) node);
            }
          });
    } catch (Exception e) {
      result.completeExceptionally(e);
    }
    return result;
  }

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.6.3
   *
   * <p>Resolves the required member by its namespace-qualified path. A missing member fails with
   * Bad_NotFound. Resolution does not create a UA node. It can perform service I/O and construct or
   * reuse a Java wrapper in Milo's address space cache. A reference can change after lookup.
   *
   * @return the existing member
   * @throws org.eclipse.milo.opcua.stack.core.UaException if a required node is absent, resolution
   *     fails, or a checked conversion fails
   */
  @NullMarked
  @Override
  public UaMethodNode getRespondMethodNode() throws UaException {
    try {
      return getRespondMethodNodeAsync().get();
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
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.6.3
   *
   * <p>Resolves the required member by its namespace-qualified path. A missing member fails with
   * Bad_NotFound. Resolution does not create a UA node. It can perform service I/O and construct or
   * reuse a Java wrapper in Milo's address space cache. A reference can change after lookup.
   *
   * <p>Lookup, conversion and service failures complete the future exceptionally. UaException
   * causes preserve OPC UA status. Incompatible plain payload casts can complete exceptionally with
   * ClassCastException. Cancellation does not promise transport cancellation or rollback.
   *
   * @return a nonnull future completing with the existing member
   */
  @NullMarked
  @Override
  public CompletableFuture<? extends UaMethodNode> getRespondMethodNodeAsync() {
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
                                "http://opcfoundation.org/UA/:Respond (declaration i=9069, owner"
                                    + " i=2830) on "
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
                                        new QualifiedName(namespaceIndex, "Respond"))
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
                                          "http://opcfoundation.org/UA/:Respond (declaration"
                                              + " i=9069, owner i=2830) on "
                                              + getNodeId()));
                                }
                                var operation = results[0];
                                if (operation.getStatusCode().getValue()
                                    == StatusCodes.Bad_NoMatch) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_NotFound,
                                          "http://opcfoundation.org/UA/:Respond (declaration"
                                              + " i=9069, owner i=2830) on "
                                              + getNodeId()));
                                }
                                if (!operation.getStatusCode().isGood()) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          operation.getStatusCode(),
                                          "http://opcfoundation.org/UA/:Respond (declaration"
                                              + " i=9069, owner i=2830)"));
                                }
                                var targets = operation.getTargets();
                                if (targets == null || targets.length == 0) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_UnexpectedError,
                                          "http://opcfoundation.org/UA/:Respond (declaration"
                                              + " i=9069, owner i=2830) on "
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
                                            "http://opcfoundation.org/UA/:Respond (declaration"
                                                + " i=9069, owner i=2830) on "
                                                + getNodeId()));
                                  }
                                  if (!target.getTargetId().isLocal()) {
                                    return CompletableFuture.failedFuture(
                                        new UaException(
                                            StatusCodes.Bad_NotSupported,
                                            "http://opcfoundation.org/UA/:Respond (declaration"
                                                + " i=9069, owner i=2830) on "
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
                                                            "http://opcfoundation.org/UA/:Respond"
                                                                + " (declaration i=9069, owner"
                                                                + " i=2830) on "
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
                                                    "http://opcfoundation.org/UA/:Respond"
                                                        + " (declaration i=9069, owner i=2830) on "
                                                        + getNodeId()));
                                          }
                                          if (unique.size() != 1) {
                                            return CompletableFuture.failedFuture(
                                                new UaException(
                                                    StatusCodes.Bad_TooManyMatches,
                                                    "http://opcfoundation.org/UA/:Respond"
                                                        + " (declaration i=9069, owner i=2830) on "
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
                                                              "http://opcfoundation.org/UA/:Respond"
                                                                  + " (declaration i=9069, owner"
                                                                  + " i=2830) on "
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
                      "http://opcfoundation.org/UA/:Respond (declaration i=9069, owner i=2830)"));
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
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.6.3
   *
   * <p>Invokes <code>Respond</code> on this node's ObjectId using the effective Method contract.
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
   * @param selectedResponse ; the supplied payload may be null.
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  @Override
  public void callRespond(@Nullable Integer selectedResponse) throws UaException {
    callRespondDetailed(selectedResponse).requireGood();
  }

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.6.3
   *
   * <p>Invokes <code>Respond</code> on this node's ObjectId using the effective Method contract.
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
   * @param selectedResponse ; the supplied payload may be null.
   * @return a future whose successful payload is null.
   */
  @NullMarked
  @Override
  public CompletableFuture<? extends @Nullable Void> callRespondAsync(
      @Nullable Integer selectedResponse) {
    CompletableFuture<@Nullable Void> result = new CompletableFuture<>();
    var call = callRespondDetailedAsync(selectedResponse);
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
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.6.3
   *
   * <p>Invokes <code>Respond</code> on this node's ObjectId using the effective Method contract.
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
   * @param selectedResponse ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  @Override
  public MethodCallResult<? extends @Nullable Void> callRespondDetailed(
      @Nullable Integer selectedResponse) throws UaException {
    return callRespondDetailed(MethodCallOptions.NONE, selectedResponse);
  }

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.6.3
   *
   * <p>Invokes <code>Respond</code> on this node's ObjectId using the effective Method contract.
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
   * @param selectedResponse ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  @Override
  public MethodCallResult<? extends @Nullable Void> callRespondDetailed(
      MethodCallOptions options, @Nullable Integer selectedResponse) throws UaException {
    Objects.requireNonNull(options, "options");
    var awaitedMethod = callRespondDetailedAsync(options, selectedResponse);
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
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.6.3
   *
   * <p>Invokes <code>Respond</code> on this node's ObjectId using the effective Method contract.
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
   * @param selectedResponse ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  @Override
  public CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callRespondDetailedAsync(@Nullable Integer selectedResponse) {
    return callRespondDetailedAsync(MethodCallOptions.NONE, selectedResponse);
  }

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.6.3
   *
   * <p>Invokes <code>Respond</code> on this node's ObjectId using the effective Method contract.
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
   * @param selectedResponse ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  @Override
  public CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callRespondDetailedAsync(MethodCallOptions options, @Nullable Integer selectedResponse) {
    CompletableFuture<MethodCallResult<@Nullable Void>> result = new CompletableFuture<>();
    try {
      Objects.requireNonNull(options, "options");
      List<@Nullable Object> rawInputs = new ArrayList<>();
      rawInputs.add(selectedResponse);
      var lookup = getRespondMethodNodeAsync();
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
                          "Method node is required for invocation: Respond"));
                }
                var inputMetadata =
                    ClientDataTypes.read(
                        this.client,
                        List.<ExpandedNodeId>of(ExpandedNodeId.parse("i=6"))
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
                                @Nullable Integer convertedValue;
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
                                        ExpandedNodeId.parse("i=6")
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
                                          "Method argument SelectedResponse (effective property"
                                              + " i=9070, DataType i=6) is unavailable in the"
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
                                    convertedValue = (Integer) methodValue;
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
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.6.4
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
  public @Nullable UaMethodNode getRespond2MethodNode() throws UaException {
    try {
      return getRespond2MethodNodeAsync().get();
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
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.6.4
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
  public CompletableFuture<? extends @Nullable UaMethodNode> getRespond2MethodNodeAsync() {
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
                                "http://opcfoundation.org/UA/:Respond2 (declaration i=24312, owner"
                                    + " i=2830) on "
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
                                        new QualifiedName(namespaceIndex, "Respond2"))
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
                                          "http://opcfoundation.org/UA/:Respond2 (declaration"
                                              + " i=24312, owner i=2830) on "
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
                                          "http://opcfoundation.org/UA/:Respond2 (declaration"
                                              + " i=24312, owner i=2830)"));
                                }
                                var targets = operation.getTargets();
                                if (targets == null || targets.length == 0) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_UnexpectedError,
                                          "http://opcfoundation.org/UA/:Respond2 (declaration"
                                              + " i=24312, owner i=2830) on "
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
                                            "http://opcfoundation.org/UA/:Respond2 (declaration"
                                                + " i=24312, owner i=2830) on "
                                                + getNodeId()));
                                  }
                                  if (!target.getTargetId().isLocal()) {
                                    return CompletableFuture.failedFuture(
                                        new UaException(
                                            StatusCodes.Bad_NotSupported,
                                            "http://opcfoundation.org/UA/:Respond2 (declaration"
                                                + " i=24312, owner i=2830) on "
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
                                                            "http://opcfoundation.org/UA/:Respond2"
                                                                + " (declaration i=24312, owner"
                                                                + " i=2830) on "
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
                                                    "http://opcfoundation.org/UA/:Respond2"
                                                        + " (declaration i=24312, owner i=2830) on "
                                                        + getNodeId()));
                                          }
                                          if (unique.size() != 1) {
                                            return CompletableFuture.failedFuture(
                                                new UaException(
                                                    StatusCodes.Bad_TooManyMatches,
                                                    "http://opcfoundation.org/UA/:Respond2"
                                                        + " (declaration i=24312, owner i=2830) on "
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
                                                              "http://opcfoundation.org/UA/:Respond2"
                                                                  + " (declaration i=24312, owner"
                                                                  + " i=2830) on "
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
                      "http://opcfoundation.org/UA/:Respond2 (declaration i=24312, owner i=2830)"));
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
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.6.4
   *
   * <p>Invokes <code>Respond2</code> on this node's ObjectId using the effective Method contract.
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
   * @param selectedResponse ; the supplied payload may be null.
   * @param comment ; the supplied payload may be null.
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  @Override
  public void callRespond2(@Nullable Integer selectedResponse, @Nullable LocalizedText comment)
      throws UaException {
    callRespond2Detailed(selectedResponse, comment).requireGood();
  }

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.6.4
   *
   * <p>Invokes <code>Respond2</code> on this node's ObjectId using the effective Method contract.
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
   * @param selectedResponse ; the supplied payload may be null.
   * @param comment ; the supplied payload may be null.
   * @return a future whose successful payload is null.
   */
  @NullMarked
  @Override
  public CompletableFuture<? extends @Nullable Void> callRespond2Async(
      @Nullable Integer selectedResponse, @Nullable LocalizedText comment) {
    CompletableFuture<@Nullable Void> result = new CompletableFuture<>();
    var call = callRespond2DetailedAsync(selectedResponse, comment);
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
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.6.4
   *
   * <p>Invokes <code>Respond2</code> on this node's ObjectId using the effective Method contract.
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
   * @param selectedResponse ; the supplied payload may be null.
   * @param comment ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  @Override
  public MethodCallResult<? extends @Nullable Void> callRespond2Detailed(
      @Nullable Integer selectedResponse, @Nullable LocalizedText comment) throws UaException {
    return callRespond2Detailed(MethodCallOptions.NONE, selectedResponse, comment);
  }

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.6.4
   *
   * <p>Invokes <code>Respond2</code> on this node's ObjectId using the effective Method contract.
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
   * @param selectedResponse ; the supplied payload may be null.
   * @param comment ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  @Override
  public MethodCallResult<? extends @Nullable Void> callRespond2Detailed(
      MethodCallOptions options,
      @Nullable Integer selectedResponse,
      @Nullable LocalizedText comment)
      throws UaException {
    Objects.requireNonNull(options, "options");
    var awaitedMethod = callRespond2DetailedAsync(options, selectedResponse, comment);
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
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.6.4
   *
   * <p>Invokes <code>Respond2</code> on this node's ObjectId using the effective Method contract.
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
   * @param selectedResponse ; the supplied payload may be null.
   * @param comment ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  @Override
  public CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callRespond2DetailedAsync(
          @Nullable Integer selectedResponse, @Nullable LocalizedText comment) {
    return callRespond2DetailedAsync(MethodCallOptions.NONE, selectedResponse, comment);
  }

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.6.4
   *
   * <p>Invokes <code>Respond2</code> on this node's ObjectId using the effective Method contract.
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
   * @param selectedResponse ; the supplied payload may be null.
   * @param comment ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  @Override
  public CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callRespond2DetailedAsync(
          MethodCallOptions options,
          @Nullable Integer selectedResponse,
          @Nullable LocalizedText comment) {
    CompletableFuture<MethodCallResult<@Nullable Void>> result = new CompletableFuture<>();
    try {
      Objects.requireNonNull(options, "options");
      List<@Nullable Object> rawInputs = new ArrayList<>();
      rawInputs.add(selectedResponse);
      rawInputs.add(comment);
      var lookup = getRespond2MethodNodeAsync();
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
                          "Method node is required for invocation: Respond2"));
                }
                var inputMetadata =
                    ClientDataTypes.read(
                        this.client,
                        List.<ExpandedNodeId>of(
                                ExpandedNodeId.parse("i=6"), ExpandedNodeId.parse("i=21"))
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
                                @Nullable Integer convertedValue;
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
                                        ExpandedNodeId.parse("i=6")
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
                                          "Method argument SelectedResponse (effective property"
                                              + " i=24313, DataType i=6) is unavailable in the"
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
                                    convertedValue = (Integer) methodValue;
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
                                @Nullable LocalizedText convertedValue;
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
                                          "Method argument Comment (effective property i=24313,"
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

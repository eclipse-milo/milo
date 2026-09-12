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
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowsePath;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.PriorityMappingEntryType;
import org.eclipse.milo.opcua.stack.core.types.structured.RelativePath;
import org.eclipse.milo.opcua.stack.core.types.structured.RelativePathElement;
import org.eclipse.milo.opcua.stack.core.types.structured.RequestHeader;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.ArrayUtil;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

public class PriorityMappingTableTypeNode extends BaseObjectTypeNode
    implements PriorityMappingTableType {
  public PriorityMappingTableTypeNode(
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
  public static ClientViews createViews(PriorityMappingTableTypeNode node) {
    Objects.requireNonNull(node, "node");
    return ClientViews.forNode(node.client, node);
  }

  @Override
  public @Nullable PriorityMappingEntryType @Nullable [] getPriorityMapppingEntries()
      throws UaException {
    PropertyTypeNode node = getPriorityMapppingEntriesNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:PriorityMapppingEntries (declaration i=25228, owner"
              + " i=25227) on "
              + getNodeId());
    }
    return (PriorityMappingEntryType[])
        decodeValue(
            node.getValue().getValue().getValue(),
            PriorityMappingEntryType.class,
            ValueRanks.OneDimension);
  }

  @Override
  public void setPriorityMapppingEntries(@Nullable PriorityMappingEntryType @Nullable [] value)
      throws UaException {
    PropertyTypeNode node = getPriorityMapppingEntriesNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:PriorityMapppingEntries (declaration i=25228, owner"
              + " i=25227) on "
              + getNodeId());
    }
    node.setValue(
        new Variant(encodeValue(value, PriorityMappingEntryType.class, ValueRanks.OneDimension)));
  }

  @Override
  public @Nullable PriorityMappingEntryType @Nullable [] readPriorityMapppingEntries()
      throws UaException {
    try {
      return readPriorityMapppingEntriesAsync().get();
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
  public void writePriorityMapppingEntries(@Nullable PriorityMappingEntryType @Nullable [] value)
      throws UaException {
    try {
      StatusCode statusCode = writePriorityMapppingEntriesAsync(value).get();
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
  public CompletableFuture<? extends @Nullable PriorityMappingEntryType @Nullable []>
      readPriorityMapppingEntriesAsync() {
    return getPriorityMapppingEntriesNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:PriorityMapppingEntries (declaration i=25228,"
                            + " owner i=25227) on "
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
                return (PriorityMappingEntryType[])
                    decodeValue(
                        v.getValue().getValue(),
                        PriorityMappingEntryType.class,
                        ValueRanks.OneDimension);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writePriorityMapppingEntriesAsync(
      @Nullable PriorityMappingEntryType @Nullable [] priorityMapppingEntries) {
    return getPriorityMapppingEntriesNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:PriorityMapppingEntries (declaration i=25228,"
                            + " owner i=25227) on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(
                            encodeValue(
                                priorityMapppingEntries,
                                PriorityMappingEntryType.class,
                                ValueRanks.OneDimension)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getPriorityMapppingEntriesNode() throws UaException {
    try {
      return getPriorityMapppingEntriesNodeAsync().get();
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
  public CompletableFuture<? extends PropertyTypeNode> getPriorityMapppingEntriesNodeAsync() {
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
                                "http://opcfoundation.org/UA/:PriorityMapppingEntries (declaration"
                                    + " i=25228, owner i=25227) on "
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
                                            namespaceIndex, "PriorityMapppingEntries"))
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
                                          "http://opcfoundation.org/UA/:PriorityMapppingEntries"
                                              + " (declaration i=25228, owner i=25227) on "
                                              + getNodeId()));
                                }
                                var operation = results[0];
                                if (operation.getStatusCode().getValue()
                                    == StatusCodes.Bad_NoMatch) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_NotFound,
                                          "http://opcfoundation.org/UA/:PriorityMapppingEntries"
                                              + " (declaration i=25228, owner i=25227) on "
                                              + getNodeId()));
                                }
                                if (!operation.getStatusCode().isGood()) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          operation.getStatusCode(),
                                          "http://opcfoundation.org/UA/:PriorityMapppingEntries"
                                              + " (declaration i=25228, owner i=25227)"));
                                }
                                var targets = operation.getTargets();
                                if (targets == null || targets.length == 0) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_UnexpectedError,
                                          "http://opcfoundation.org/UA/:PriorityMapppingEntries"
                                              + " (declaration i=25228, owner i=25227) on "
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
                                            "http://opcfoundation.org/UA/:PriorityMapppingEntries"
                                                + " (declaration i=25228, owner i=25227) on "
                                                + getNodeId()));
                                  }
                                  if (!target.getTargetId().isLocal()) {
                                    return CompletableFuture.failedFuture(
                                        new UaException(
                                            StatusCodes.Bad_NotSupported,
                                            "http://opcfoundation.org/UA/:PriorityMapppingEntries"
                                                + " (declaration i=25228, owner i=25227) on "
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
                                                            "http://opcfoundation.org/UA/:PriorityMapppingEntries"
                                                                + " (declaration i=25228, owner"
                                                                + " i=25227) on "
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
                                                    "http://opcfoundation.org/UA/:PriorityMapppingEntries"
                                                        + " (declaration i=25228, owner i=25227) on"
                                                        + " "
                                                        + getNodeId()));
                                          }
                                          if (unique.size() != 1) {
                                            return CompletableFuture.failedFuture(
                                                new UaException(
                                                    StatusCodes.Bad_TooManyMatches,
                                                    "http://opcfoundation.org/UA/:PriorityMapppingEntries"
                                                        + " (declaration i=25228, owner i=25227) on"
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
                                                              "http://opcfoundation.org/UA/:PriorityMapppingEntries"
                                                                  + " (declaration i=25228, owner"
                                                                  + " i=25227) on "
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
                      "http://opcfoundation.org/UA/:PriorityMapppingEntries (declaration i=25228,"
                          + " owner i=25227)"));
            } else {
              result.complete((PropertyTypeNode) node);
            }
          });
    } catch (Exception e) {
      result.completeExceptionally(e);
    }
    return result;
  }

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.2/#5.5.2.3
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
  public @Nullable UaMethodNode getAddPriorityMappingEntryMethodNode() throws UaException {
    try {
      return getAddPriorityMappingEntryMethodNodeAsync().get();
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
   * https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.2/#5.5.2.3
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
      getAddPriorityMappingEntryMethodNodeAsync() {
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
                                "http://opcfoundation.org/UA/:AddPriorityMappingEntry (declaration"
                                    + " i=25229, owner i=25227) on "
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
                                            namespaceIndex, "AddPriorityMappingEntry"))
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
                                          "http://opcfoundation.org/UA/:AddPriorityMappingEntry"
                                              + " (declaration i=25229, owner i=25227) on "
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
                                          "http://opcfoundation.org/UA/:AddPriorityMappingEntry"
                                              + " (declaration i=25229, owner i=25227)"));
                                }
                                var targets = operation.getTargets();
                                if (targets == null || targets.length == 0) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_UnexpectedError,
                                          "http://opcfoundation.org/UA/:AddPriorityMappingEntry"
                                              + " (declaration i=25229, owner i=25227) on "
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
                                            "http://opcfoundation.org/UA/:AddPriorityMappingEntry"
                                                + " (declaration i=25229, owner i=25227) on "
                                                + getNodeId()));
                                  }
                                  if (!target.getTargetId().isLocal()) {
                                    return CompletableFuture.failedFuture(
                                        new UaException(
                                            StatusCodes.Bad_NotSupported,
                                            "http://opcfoundation.org/UA/:AddPriorityMappingEntry"
                                                + " (declaration i=25229, owner i=25227) on "
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
                                                            "http://opcfoundation.org/UA/:AddPriorityMappingEntry"
                                                                + " (declaration i=25229, owner"
                                                                + " i=25227) on "
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
                                                    "http://opcfoundation.org/UA/:AddPriorityMappingEntry"
                                                        + " (declaration i=25229, owner i=25227) on"
                                                        + " "
                                                        + getNodeId()));
                                          }
                                          if (unique.size() != 1) {
                                            return CompletableFuture.failedFuture(
                                                new UaException(
                                                    StatusCodes.Bad_TooManyMatches,
                                                    "http://opcfoundation.org/UA/:AddPriorityMappingEntry"
                                                        + " (declaration i=25229, owner i=25227) on"
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
                                                              "http://opcfoundation.org/UA/:AddPriorityMappingEntry"
                                                                  + " (declaration i=25229, owner"
                                                                  + " i=25227) on "
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
                      "http://opcfoundation.org/UA/:AddPriorityMappingEntry (declaration i=25229,"
                          + " owner i=25227)"));
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
   * https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.2/#5.5.2.3
   *
   * <p>Invokes <code>AddPriorityMappingEntry</code> on this node's ObjectId using the effective
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
   * @param mappingUri ; the supplied payload may be null.
   * @param priorityLabel ; the supplied payload may be null.
   * @param priorityValuePcp ; the supplied payload may be null.
   * @param priorityValueDscp ; the supplied payload may be null.
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  @Override
  public void callAddPriorityMappingEntry(
      @Nullable String mappingUri,
      @Nullable String priorityLabel,
      @Nullable UByte priorityValuePcp,
      @Nullable UInteger priorityValueDscp)
      throws UaException {
    callAddPriorityMappingEntryDetailed(
            mappingUri, priorityLabel, priorityValuePcp, priorityValueDscp)
        .requireGood();
  }

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.2/#5.5.2.3
   *
   * <p>Invokes <code>AddPriorityMappingEntry</code> on this node's ObjectId using the effective
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
   * @param mappingUri ; the supplied payload may be null.
   * @param priorityLabel ; the supplied payload may be null.
   * @param priorityValuePcp ; the supplied payload may be null.
   * @param priorityValueDscp ; the supplied payload may be null.
   * @return a future whose successful payload is null.
   */
  @NullMarked
  @Override
  public CompletableFuture<? extends @Nullable Void> callAddPriorityMappingEntryAsync(
      @Nullable String mappingUri,
      @Nullable String priorityLabel,
      @Nullable UByte priorityValuePcp,
      @Nullable UInteger priorityValueDscp) {
    CompletableFuture<@Nullable Void> result = new CompletableFuture<>();
    var call =
        callAddPriorityMappingEntryDetailedAsync(
            mappingUri, priorityLabel, priorityValuePcp, priorityValueDscp);
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
   * https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.2/#5.5.2.3
   *
   * <p>Invokes <code>AddPriorityMappingEntry</code> on this node's ObjectId using the effective
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
   * @param mappingUri ; the supplied payload may be null.
   * @param priorityLabel ; the supplied payload may be null.
   * @param priorityValuePcp ; the supplied payload may be null.
   * @param priorityValueDscp ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  @Override
  public MethodCallResult<? extends @Nullable Void> callAddPriorityMappingEntryDetailed(
      @Nullable String mappingUri,
      @Nullable String priorityLabel,
      @Nullable UByte priorityValuePcp,
      @Nullable UInteger priorityValueDscp)
      throws UaException {
    return callAddPriorityMappingEntryDetailed(
        MethodCallOptions.NONE, mappingUri, priorityLabel, priorityValuePcp, priorityValueDscp);
  }

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.2/#5.5.2.3
   *
   * <p>Invokes <code>AddPriorityMappingEntry</code> on this node's ObjectId using the effective
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
   * @param mappingUri ; the supplied payload may be null.
   * @param priorityLabel ; the supplied payload may be null.
   * @param priorityValuePcp ; the supplied payload may be null.
   * @param priorityValueDscp ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  @Override
  public MethodCallResult<? extends @Nullable Void> callAddPriorityMappingEntryDetailed(
      MethodCallOptions options,
      @Nullable String mappingUri,
      @Nullable String priorityLabel,
      @Nullable UByte priorityValuePcp,
      @Nullable UInteger priorityValueDscp)
      throws UaException {
    Objects.requireNonNull(options, "options");
    var awaitedMethod =
        callAddPriorityMappingEntryDetailedAsync(
            options, mappingUri, priorityLabel, priorityValuePcp, priorityValueDscp);
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
   * https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.2/#5.5.2.3
   *
   * <p>Invokes <code>AddPriorityMappingEntry</code> on this node's ObjectId using the effective
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
   * @param mappingUri ; the supplied payload may be null.
   * @param priorityLabel ; the supplied payload may be null.
   * @param priorityValuePcp ; the supplied payload may be null.
   * @param priorityValueDscp ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  @Override
  public CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callAddPriorityMappingEntryDetailedAsync(
          @Nullable String mappingUri,
          @Nullable String priorityLabel,
          @Nullable UByte priorityValuePcp,
          @Nullable UInteger priorityValueDscp) {
    return callAddPriorityMappingEntryDetailedAsync(
        MethodCallOptions.NONE, mappingUri, priorityLabel, priorityValuePcp, priorityValueDscp);
  }

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.2/#5.5.2.3
   *
   * <p>Invokes <code>AddPriorityMappingEntry</code> on this node's ObjectId using the effective
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
   * @param mappingUri ; the supplied payload may be null.
   * @param priorityLabel ; the supplied payload may be null.
   * @param priorityValuePcp ; the supplied payload may be null.
   * @param priorityValueDscp ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  @Override
  public CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callAddPriorityMappingEntryDetailedAsync(
          MethodCallOptions options,
          @Nullable String mappingUri,
          @Nullable String priorityLabel,
          @Nullable UByte priorityValuePcp,
          @Nullable UInteger priorityValueDscp) {
    CompletableFuture<MethodCallResult<@Nullable Void>> result = new CompletableFuture<>();
    try {
      Objects.requireNonNull(options, "options");
      List<@Nullable Object> rawInputs = new ArrayList<>();
      rawInputs.add(mappingUri);
      rawInputs.add(priorityLabel);
      rawInputs.add(priorityValuePcp);
      rawInputs.add(priorityValueDscp);
      var lookup = getAddPriorityMappingEntryMethodNodeAsync();
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
                          "Method node is required for invocation: AddPriorityMappingEntry"));
                }
                var inputMetadata =
                    ClientDataTypes.read(
                        this.client,
                        List.<ExpandedNodeId>of(
                                ExpandedNodeId.parse("i=12"),
                                ExpandedNodeId.parse("i=12"),
                                ExpandedNodeId.parse("i=3"),
                                ExpandedNodeId.parse("i=7"))
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
                                          "Method argument MappingUri (effective property i=25230,"
                                              + " DataType i=12) is unavailable in the effective"
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
                                @Nullable String convertedValue;
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
                                          "Method argument PriorityLabel (effective property"
                                              + " i=25230, DataType i=12) is unavailable in the"
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
                                @Nullable UByte convertedValue;
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
                                        ExpandedNodeId.parse("i=3")
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
                                          "Method argument PriorityValue_PCP (effective property"
                                              + " i=25230, DataType i=3) is unavailable in the"
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
                                    convertedValue = (UByte) methodValue;
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
                                @Nullable UInteger convertedValue;
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
                                          "Method argument PriorityValue_DSCP (effective property"
                                              + " i=25230, DataType i=7) is unavailable in the"
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
   * https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.2/#5.5.2.4
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
  public @Nullable UaMethodNode getDeletePriorityMappingEntryMethodNode() throws UaException {
    try {
      return getDeletePriorityMappingEntryMethodNodeAsync().get();
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
   * https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.2/#5.5.2.4
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
      getDeletePriorityMappingEntryMethodNodeAsync() {
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
                                "http://opcfoundation.org/UA/:DeletePriorityMappingEntry"
                                    + " (declaration i=25231, owner i=25227) on "
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
                                            namespaceIndex, "DeletePriorityMappingEntry"))
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
                                          "http://opcfoundation.org/UA/:DeletePriorityMappingEntry"
                                              + " (declaration i=25231, owner i=25227) on "
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
                                          "http://opcfoundation.org/UA/:DeletePriorityMappingEntry"
                                              + " (declaration i=25231, owner i=25227)"));
                                }
                                var targets = operation.getTargets();
                                if (targets == null || targets.length == 0) {
                                  return CompletableFuture.failedFuture(
                                      new UaException(
                                          StatusCodes.Bad_UnexpectedError,
                                          "http://opcfoundation.org/UA/:DeletePriorityMappingEntry"
                                              + " (declaration i=25231, owner i=25227) on "
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
                                            "http://opcfoundation.org/UA/:DeletePriorityMappingEntry"
                                                + " (declaration i=25231, owner i=25227) on "
                                                + getNodeId()));
                                  }
                                  if (!target.getTargetId().isLocal()) {
                                    return CompletableFuture.failedFuture(
                                        new UaException(
                                            StatusCodes.Bad_NotSupported,
                                            "http://opcfoundation.org/UA/:DeletePriorityMappingEntry"
                                                + " (declaration i=25231, owner i=25227) on "
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
                                                            "http://opcfoundation.org/UA/:DeletePriorityMappingEntry"
                                                                + " (declaration i=25231, owner"
                                                                + " i=25227) on "
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
                                                    "http://opcfoundation.org/UA/:DeletePriorityMappingEntry"
                                                        + " (declaration i=25231, owner i=25227) on"
                                                        + " "
                                                        + getNodeId()));
                                          }
                                          if (unique.size() != 1) {
                                            return CompletableFuture.failedFuture(
                                                new UaException(
                                                    StatusCodes.Bad_TooManyMatches,
                                                    "http://opcfoundation.org/UA/:DeletePriorityMappingEntry"
                                                        + " (declaration i=25231, owner i=25227) on"
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
                                                              "http://opcfoundation.org/UA/:DeletePriorityMappingEntry"
                                                                  + " (declaration i=25231, owner"
                                                                  + " i=25227) on "
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
                      "http://opcfoundation.org/UA/:DeletePriorityMappingEntry (declaration"
                          + " i=25231, owner i=25227)"));
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
   * https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.2/#5.5.2.4
   *
   * <p>Invokes <code>DeletePriorityMappingEntry</code> on this node's ObjectId using the effective
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
   * @param mappingUri ; the supplied payload may be null.
   * @param priorityLabel ; the supplied payload may be null.
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  @Override
  public void callDeletePriorityMappingEntry(
      @Nullable String mappingUri, @Nullable String priorityLabel) throws UaException {
    callDeletePriorityMappingEntryDetailed(mappingUri, priorityLabel).requireGood();
  }

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.2/#5.5.2.4
   *
   * <p>Invokes <code>DeletePriorityMappingEntry</code> on this node's ObjectId using the effective
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
   * @param mappingUri ; the supplied payload may be null.
   * @param priorityLabel ; the supplied payload may be null.
   * @return a future whose successful payload is null.
   */
  @NullMarked
  @Override
  public CompletableFuture<? extends @Nullable Void> callDeletePriorityMappingEntryAsync(
      @Nullable String mappingUri, @Nullable String priorityLabel) {
    CompletableFuture<@Nullable Void> result = new CompletableFuture<>();
    var call = callDeletePriorityMappingEntryDetailedAsync(mappingUri, priorityLabel);
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
   * https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.2/#5.5.2.4
   *
   * <p>Invokes <code>DeletePriorityMappingEntry</code> on this node's ObjectId using the effective
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
   * @param mappingUri ; the supplied payload may be null.
   * @param priorityLabel ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  @Override
  public MethodCallResult<? extends @Nullable Void> callDeletePriorityMappingEntryDetailed(
      @Nullable String mappingUri, @Nullable String priorityLabel) throws UaException {
    return callDeletePriorityMappingEntryDetailed(
        MethodCallOptions.NONE, mappingUri, priorityLabel);
  }

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.2/#5.5.2.4
   *
   * <p>Invokes <code>DeletePriorityMappingEntry</code> on this node's ObjectId using the effective
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
   * @param mappingUri ; the supplied payload may be null.
   * @param priorityLabel ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  @Override
  public MethodCallResult<? extends @Nullable Void> callDeletePriorityMappingEntryDetailed(
      MethodCallOptions options, @Nullable String mappingUri, @Nullable String priorityLabel)
      throws UaException {
    Objects.requireNonNull(options, "options");
    var awaitedMethod =
        callDeletePriorityMappingEntryDetailedAsync(options, mappingUri, priorityLabel);
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
   * https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.2/#5.5.2.4
   *
   * <p>Invokes <code>DeletePriorityMappingEntry</code> on this node's ObjectId using the effective
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
   * @param mappingUri ; the supplied payload may be null.
   * @param priorityLabel ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  @Override
  public CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callDeletePriorityMappingEntryDetailedAsync(
          @Nullable String mappingUri, @Nullable String priorityLabel) {
    return callDeletePriorityMappingEntryDetailedAsync(
        MethodCallOptions.NONE, mappingUri, priorityLabel);
  }

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.2/#5.5.2.4
   *
   * <p>Invokes <code>DeletePriorityMappingEntry</code> on this node's ObjectId using the effective
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
   * @param mappingUri ; the supplied payload may be null.
   * @param priorityLabel ; the supplied payload may be null.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  @Override
  public CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callDeletePriorityMappingEntryDetailedAsync(
          MethodCallOptions options, @Nullable String mappingUri, @Nullable String priorityLabel) {
    CompletableFuture<MethodCallResult<@Nullable Void>> result = new CompletableFuture<>();
    try {
      Objects.requireNonNull(options, "options");
      List<@Nullable Object> rawInputs = new ArrayList<>();
      rawInputs.add(mappingUri);
      rawInputs.add(priorityLabel);
      var lookup = getDeletePriorityMappingEntryMethodNodeAsync();
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
                          "Method node is required for invocation: DeletePriorityMappingEntry"));
                }
                var inputMetadata =
                    ClientDataTypes.read(
                        this.client,
                        List.<ExpandedNodeId>of(
                                ExpandedNodeId.parse("i=12"), ExpandedNodeId.parse("i=12"))
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
                                          "Method argument MappingUri (effective property i=25232,"
                                              + " DataType i=12) is unavailable in the effective"
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
                                @Nullable String convertedValue;
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
                                          "Method argument PriorityLabel (effective property"
                                              + " i=25232, DataType i=12) is unavailable in the"
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

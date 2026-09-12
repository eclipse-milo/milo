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

import com.digitalpetri.opcua.uanodeset.runtime.client.ClientObjectView;
import com.digitalpetri.opcua.uanodeset.runtime.client.ClientViewType;
import com.digitalpetri.opcua.uanodeset.runtime.client.ClientViews;
import com.digitalpetri.opcua.uanodeset.runtime.views.ViewFutures;
import java.lang.reflect.Array;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.function.Supplier;
import org.eclipse.milo.opcua.sdk.client.model.variables.BaseDataVariableType;
import org.eclipse.milo.opcua.sdk.client.model.variables.BaseDataVariableTypeView;
import org.eclipse.milo.opcua.sdk.client.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaObjectNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.UaEnumeratedType;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.OptionSetUInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UNumber;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowsePath;
import org.eclipse.milo.opcua.stack.core.types.structured.RelativePath;
import org.eclipse.milo.opcua.stack.core.types.structured.RelativePathElement;
import org.eclipse.milo.opcua.stack.core.util.ArrayUtil;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.11
 *
 * <p>Selected <code>IIeeeTsnInterfaceConfigurationTalkerType</code> contract over one retained
 * backing node. Obtain this view from a ClientViews context using {@link #TYPE}; the token itself
 * does not prove membership. Access after context close fails with IllegalStateException.
 */
@NullMarked
public final class IIeeeTsnInterfaceConfigurationTalkerTypeView extends ClientObjectView
    implements IIeeeTsnInterfaceConfigurationTalkerType {
  /** Selected UA/Java contract and context-owned factory. */
  public static final ClientViewType<IIeeeTsnInterfaceConfigurationTalkerType> TYPE =
      ClientViewType.of(
          ExpandedNodeId.parse("i=24191"),
          IIeeeTsnInterfaceConfigurationTalkerType.class,
          IIeeeTsnInterfaceConfigurationTalkerTypeView::new);

  private static final Map<ExpandedNodeId, Set<Integer>> KNOWN_ENUMS =
      Map.ofEntries(
          Map.entry(ExpandedNodeId.parse("i=120"), Set.of(1, 2, 3)),
          Map.entry(ExpandedNodeId.parse("i=32417"), Set.of(0, 1, 2, 3)),
          Map.entry(ExpandedNodeId.parse("i=11939"), Set.of(1, 2, 4, 8)),
          Map.entry(ExpandedNodeId.parse("i=15632"), Set.of(1, 2, 3, 4, 5, 6, 7, 8, 9)),
          Map.entry(ExpandedNodeId.parse("i=32436"), Set.of(0, 1, 2)),
          Map.entry(ExpandedNodeId.parse("i=12552"), Set.of(0, 1, 2, 4, 8, 15)),
          Map.entry(ExpandedNodeId.parse("i=15539"), Set.of(1, 2, 3, 4)),
          Map.entry(ExpandedNodeId.parse("i=14647"), Set.of(0, 1, 2, 3, 4)),
          Map.entry(ExpandedNodeId.parse("i=18595"), Set.of(0, 1, 2)),
          Map.entry(ExpandedNodeId.parse("i=15874"), Set.of(0, 1, 2)),
          Map.entry(ExpandedNodeId.parse("i=20408"), Set.of(0, 1, 2)),
          Map.entry(ExpandedNodeId.parse("i=15008"), Set.of(0, 1, 2, 3, 4)),
          Map.entry(ExpandedNodeId.parse("i=19723"), Set.of(0, 1, 2, 3, 4)),
          Map.entry(ExpandedNodeId.parse("i=19730"), Set.of(0, 1)),
          Map.entry(ExpandedNodeId.parse("i=24210"), Set.of(0, 1, 2)),
          Map.entry(ExpandedNodeId.parse("i=24212"), Set.of(0, 1, 2)),
          Map.entry(ExpandedNodeId.parse("i=24214"), Set.of(0, 1, 2, 3, 4, 5, 6)),
          Map.entry(ExpandedNodeId.parse("i=24216"), Set.of(0, 1, 2, 3, 4)),
          Map.entry(
              ExpandedNodeId.parse("i=24218"),
              Set.of(
                  0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22,
                  23, 24, 25)),
          Map.entry(ExpandedNodeId.parse("i=24220"), Set.of(0, 1, 2, 3, 4)),
          Map.entry(ExpandedNodeId.parse("i=24222"), Set.of(0, 1, 2)),
          Map.entry(ExpandedNodeId.parse("i=24224"), Set.of(0, 1, 2, 3)),
          Map.entry(ExpandedNodeId.parse("i=18947"), Set.of(1, 2, 3, 4, 5, 6, 7)),
          Map.entry(ExpandedNodeId.parse("i=18949"), Set.of(1, 2, 3, 4, 5, 6, 7)),
          Map.entry(ExpandedNodeId.parse("i=18951"), Set.of(0, 1, 2, 3)),
          Map.entry(ExpandedNodeId.parse("i=256"), Set.of(0, 1, 2, 3)),
          Map.entry(ExpandedNodeId.parse("i=257"), Set.of(0, 1, 2, 4, 8, 16, 32, 64, 128)),
          Map.entry(ExpandedNodeId.parse("i=98"), Set.of(0, 1, 2, 3, 4)),
          Map.entry(ExpandedNodeId.parse("i=307"), Set.of(0, 1, 2, 3)),
          Map.entry(ExpandedNodeId.parse("i=302"), Set.of(0, 1, 2, 3)),
          Map.entry(ExpandedNodeId.parse("i=303"), Set.of(0, 1, 2, 3)),
          Map.entry(ExpandedNodeId.parse("i=315"), Set.of(0, 1)),
          Map.entry(
              ExpandedNodeId.parse("i=348"),
              Set.of(
                  0, 1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, 16384, 32768,
                  65536, 131072, 262144, 524288, 1048576, 2097152, 4194304, 8388608, 16777216,
                  26501220, 26501348, 26501356, 26503268, 26537060, 26571383, 26632548, 28600438,
                  33554431)),
          Map.entry(
              ExpandedNodeId.parse("i=576"),
              Set.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17)),
          Map.entry(ExpandedNodeId.parse("i=11234"), Set.of(1, 2, 3, 4)),
          Map.entry(ExpandedNodeId.parse("i=11293"), Set.of(1, 2, 3, 4)),
          Map.entry(ExpandedNodeId.parse("i=851"), Set.of(0, 1, 2, 3, 4, 5)),
          Map.entry(ExpandedNodeId.parse("i=852"), Set.of(0, 1, 2, 3, 4, 5, 6, 7)),
          Map.entry(ExpandedNodeId.parse("i=12077"), Set.of(0, 1, 2)),
          Map.entry(ExpandedNodeId.parse("i=890"), Set.of(0, 1, 2, 3, 4)));

  private IIeeeTsnInterfaceConfigurationTalkerTypeView(ClientViews views, UaObjectNode node) {
    super(views, node);
  }

  private CompletableFuture<UaVariableNode> viewMember0Async() {
    try {
      views.checkOpen();
      var memberRequest =
          ((Supplier<CompletableFuture<UaVariableNode>>)
                  () -> {
                    CompletableFuture<UaVariableNode> result = new CompletableFuture<>();
                    try {
                      CompletableFuture<NodeId> lookup =
                          CompletableFuture.completedFuture(getNodeId());
                      CompletableFuture<UaNode> hop0 =
                          lookup.thenCompose(
                              parent -> {
                                NodeId parentId = parent;
                                if (result.isCancelled()) {
                                  return CompletableFuture.failedFuture(
                                      new CancellationException());
                                }
                                if (parentId == null) {
                                  return CompletableFuture.completedFuture(null);
                                }
                                CompletableFuture<Void> namespaceReady;
                                if (client
                                            .getNamespaceTable()
                                            .getIndex("http://opcfoundation.org/UA/")
                                        == null
                                    || client
                                            .getNamespaceTable()
                                            .getIndex("http://opcfoundation.org/UA/")
                                        == null) {
                                  namespaceReady =
                                      client.readNamespaceTableAsync().thenApply(ignored -> null);
                                } else {
                                  namespaceReady = CompletableFuture.completedFuture(null);
                                }
                                return namespaceReady.thenCompose(
                                    ignored -> {
                                      if (result.isCancelled()) {
                                        return CompletableFuture.failedFuture(
                                            new CancellationException());
                                      }
                                      var namespaceIndex =
                                          client
                                              .getNamespaceTable()
                                              .getIndex("http://opcfoundation.org/UA/");
                                      var referenceId =
                                          ExpandedNodeId.parse("i=47")
                                              .toNodeId(client.getNamespaceTable());
                                      if (namespaceIndex == null || referenceId.isEmpty()) {
                                        return CompletableFuture.failedFuture(
                                            new UaException(
                                                StatusCodes.Bad_NodeIdInvalid,
                                                "http://opcfoundation.org/UA/:MacAddress"
                                                    + " (declaration i=24189, owner i=24188) on "
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
                                                            namespaceIndex, "MacAddress"))
                                                  }));
                                      return client
                                          .translateBrowsePathsAsync(List.of(browsePath))
                                          .thenCompose(
                                              response -> {
                                                if (result.isCancelled()) {
                                                  return CompletableFuture.failedFuture(
                                                      new CancellationException());
                                                }
                                                var results =
                                                    response == null ? null : response.getResults();
                                                if (results == null
                                                    || results.length != 1
                                                    || results[0] == null
                                                    || results[0].getStatusCode() == null) {
                                                  return CompletableFuture.failedFuture(
                                                      new UaException(
                                                          StatusCodes.Bad_UnexpectedError,
                                                          "http://opcfoundation.org/UA/:MacAddress"
                                                              + " (declaration i=24189, owner"
                                                              + " i=24188) on "
                                                              + getNodeId()));
                                                }
                                                var operation = results[0];
                                                if (operation.getStatusCode().getValue()
                                                    == StatusCodes.Bad_NoMatch) {
                                                  return CompletableFuture.failedFuture(
                                                      new UaException(
                                                          StatusCodes.Bad_NotFound,
                                                          "http://opcfoundation.org/UA/:MacAddress"
                                                              + " (declaration i=24189, owner"
                                                              + " i=24188) on "
                                                              + getNodeId()));
                                                }
                                                if (!operation.getStatusCode().isGood()) {
                                                  return CompletableFuture.failedFuture(
                                                      new UaException(
                                                          operation.getStatusCode(),
                                                          "http://opcfoundation.org/UA/:MacAddress"
                                                              + " (declaration i=24189, owner"
                                                              + " i=24188)"));
                                                }
                                                var targets = operation.getTargets();
                                                if (targets == null || targets.length == 0) {
                                                  return CompletableFuture.failedFuture(
                                                      new UaException(
                                                          StatusCodes.Bad_UnexpectedError,
                                                          "http://opcfoundation.org/UA/:MacAddress"
                                                              + " (declaration i=24189, owner"
                                                              + " i=24188) on "
                                                              + getNodeId()));
                                                }
                                                var identities =
                                                    new ArrayList<CompletableFuture<NodeId>>();
                                                for (var target : targets) {
                                                  if (target == null
                                                      || target.getTargetId() == null
                                                      || target.getRemainingPathIndex() == null
                                                      || target.getRemainingPathIndex().longValue()
                                                          != 0xffffffffL) {
                                                    return CompletableFuture.failedFuture(
                                                        new UaException(
                                                            StatusCodes.Bad_UnexpectedError,
                                                            "http://opcfoundation.org/UA/:MacAddress"
                                                                + " (declaration i=24189, owner"
                                                                + " i=24188) on "
                                                                + getNodeId()));
                                                  }
                                                  if (!target.getTargetId().isLocal()) {
                                                    return CompletableFuture.failedFuture(
                                                        new UaException(
                                                            StatusCodes.Bad_NotSupported,
                                                            "http://opcfoundation.org/UA/:MacAddress"
                                                                + " (declaration i=24189, owner"
                                                                + " i=24188) on "
                                                                + getNodeId()));
                                                  }
                                                  if (result.isCancelled()) {
                                                    return CompletableFuture.failedFuture(
                                                        new CancellationException());
                                                  }
                                                  var localTarget =
                                                      target
                                                          .getTargetId()
                                                          .toNodeId(client.getNamespaceTable());
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
                                                                      target
                                                                          .getTargetId()
                                                                          .toNodeId(namespaceTable);
                                                                  if (resolvedTarget.isEmpty()) {
                                                                    return CompletableFuture
                                                                        .failedFuture(
                                                                            new UaException(
                                                                                StatusCodes
                                                                                    .Bad_NodeIdInvalid,
                                                                                "http://opcfoundation.org/UA/:MacAddress"
                                                                                    + " (declaration"
                                                                                    + " i=24189,"
                                                                                    + " owner"
                                                                                    + " i=24188) on"
                                                                                    + " "
                                                                                    + getNodeId()));
                                                                  }
                                                                  return CompletableFuture
                                                                      .completedFuture(
                                                                          resolvedTarget
                                                                              .orElseThrow());
                                                                }));
                                                  }
                                                }
                                                return CompletableFuture.allOf(
                                                        identities.toArray(
                                                            CompletableFuture[]::new))
                                                    .thenCompose(
                                                        ready -> {
                                                          if (result.isCancelled()) {
                                                            return CompletableFuture.failedFuture(
                                                                new CancellationException());
                                                          }
                                                          var unique = new LinkedHashSet<NodeId>();
                                                          identities.forEach(
                                                              identity ->
                                                                  unique.add(identity.join()));
                                                          if (unique.stream()
                                                              .anyMatch(NodeId::isNull)) {
                                                            return CompletableFuture.failedFuture(
                                                                new UaException(
                                                                    StatusCodes.Bad_NodeIdInvalid,
                                                                    "http://opcfoundation.org/UA/:MacAddress"
                                                                        + " (declaration i=24189,"
                                                                        + " owner i=24188) on "
                                                                        + getNodeId()));
                                                          }
                                                          if (unique.size() != 1) {
                                                            return CompletableFuture.failedFuture(
                                                                new UaException(
                                                                    StatusCodes.Bad_TooManyMatches,
                                                                    "http://opcfoundation.org/UA/:MacAddress"
                                                                        + " (declaration i=24189,"
                                                                        + " owner i=24188) on "
                                                                        + getNodeId()));
                                                          }
                                                          return client
                                                              .getAddressSpace()
                                                              .getNodeAsync(
                                                                  unique.iterator().next())
                                                              .thenCompose(
                                                                  node -> {
                                                                    if (node == null
                                                                        || node.getNodeClass()
                                                                            != NodeClass.Variable) {
                                                                      return CompletableFuture
                                                                          .failedFuture(
                                                                              new UaException(
                                                                                  StatusCodes
                                                                                      .Bad_NodeClassInvalid,
                                                                                  "http://opcfoundation.org/UA/:MacAddress"
                                                                                      + " (declaration"
                                                                                      + " i=24189,"
                                                                                      + " owner"
                                                                                      + " i=24188)"
                                                                                      + " on "
                                                                                      + getNodeId()));
                                                                    }
                                                                    return CompletableFuture
                                                                        .completedFuture(node);
                                                                  });
                                                        });
                                              });
                                    });
                              });
                      hop0.whenComplete(
                          (node, failure) -> {
                            if (failure != null) {
                              result.completeExceptionally(failure);
                            } else if (node != null && !(node instanceof UaVariableNode)) {
                              result.completeExceptionally(
                                  new UaException(
                                      StatusCodes.Bad_TypeMismatch,
                                      "http://opcfoundation.org/UA/:MacAddress (declaration"
                                          + " i=24189, owner i=24188)"));
                            } else {
                              result.complete((UaVariableNode) node);
                            }
                          });
                    } catch (Exception e) {
                      result.completeExceptionally(e);
                    }
                    return result;
                  })
              .get();
      return ViewFutures.compose(
          memberRequest,
          found ->
              found == null
                  ? CompletableFuture.completedFuture(null)
                  : views.variableNodeAsync(found.getNodeId()));
    } catch (RuntimeException failure) {
      return CompletableFuture.failedFuture(failure);
    }
  }

  private UaVariableNode viewMember0() throws UaException {
    return ViewFutures.await(viewMember0Async());
  }

  private CompletableFuture<@Nullable UaVariableNode> viewMember1Async() {
    try {
      views.checkOpen();
      var memberRequest =
          ((Supplier<CompletableFuture<@Nullable UaVariableNode>>)
                  () -> {
                    CompletableFuture<UaVariableNode> result = new CompletableFuture<>();
                    try {
                      CompletableFuture<NodeId> lookup =
                          CompletableFuture.completedFuture(getNodeId());
                      CompletableFuture<UaNode> hop0 =
                          lookup.thenCompose(
                              parent -> {
                                NodeId parentId = parent;
                                if (result.isCancelled()) {
                                  return CompletableFuture.failedFuture(
                                      new CancellationException());
                                }
                                if (parentId == null) {
                                  return CompletableFuture.completedFuture(null);
                                }
                                CompletableFuture<Void> namespaceReady;
                                if (client
                                            .getNamespaceTable()
                                            .getIndex("http://opcfoundation.org/UA/")
                                        == null
                                    || client
                                            .getNamespaceTable()
                                            .getIndex("http://opcfoundation.org/UA/")
                                        == null) {
                                  namespaceReady =
                                      client.readNamespaceTableAsync().thenApply(ignored -> null);
                                } else {
                                  namespaceReady = CompletableFuture.completedFuture(null);
                                }
                                return namespaceReady.thenCompose(
                                    ignored -> {
                                      if (result.isCancelled()) {
                                        return CompletableFuture.failedFuture(
                                            new CancellationException());
                                      }
                                      var namespaceIndex =
                                          client
                                              .getNamespaceTable()
                                              .getIndex("http://opcfoundation.org/UA/");
                                      var referenceId =
                                          ExpandedNodeId.parse("i=47")
                                              .toNodeId(client.getNamespaceTable());
                                      if (namespaceIndex == null || referenceId.isEmpty()) {
                                        return CompletableFuture.failedFuture(
                                            new UaException(
                                                StatusCodes.Bad_NodeIdInvalid,
                                                "http://opcfoundation.org/UA/:InterfaceName"
                                                    + " (declaration i=24190, owner i=24188) on "
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
                                                            namespaceIndex, "InterfaceName"))
                                                  }));
                                      return client
                                          .translateBrowsePathsAsync(List.of(browsePath))
                                          .thenCompose(
                                              response -> {
                                                if (result.isCancelled()) {
                                                  return CompletableFuture.failedFuture(
                                                      new CancellationException());
                                                }
                                                var results =
                                                    response == null ? null : response.getResults();
                                                if (results == null
                                                    || results.length != 1
                                                    || results[0] == null
                                                    || results[0].getStatusCode() == null) {
                                                  return CompletableFuture.failedFuture(
                                                      new UaException(
                                                          StatusCodes.Bad_UnexpectedError,
                                                          "http://opcfoundation.org/UA/:InterfaceName"
                                                              + " (declaration i=24190, owner"
                                                              + " i=24188) on "
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
                                                          "http://opcfoundation.org/UA/:InterfaceName"
                                                              + " (declaration i=24190, owner"
                                                              + " i=24188)"));
                                                }
                                                var targets = operation.getTargets();
                                                if (targets == null || targets.length == 0) {
                                                  return CompletableFuture.failedFuture(
                                                      new UaException(
                                                          StatusCodes.Bad_UnexpectedError,
                                                          "http://opcfoundation.org/UA/:InterfaceName"
                                                              + " (declaration i=24190, owner"
                                                              + " i=24188) on "
                                                              + getNodeId()));
                                                }
                                                var identities =
                                                    new ArrayList<CompletableFuture<NodeId>>();
                                                for (var target : targets) {
                                                  if (target == null
                                                      || target.getTargetId() == null
                                                      || target.getRemainingPathIndex() == null
                                                      || target.getRemainingPathIndex().longValue()
                                                          != 0xffffffffL) {
                                                    return CompletableFuture.failedFuture(
                                                        new UaException(
                                                            StatusCodes.Bad_UnexpectedError,
                                                            "http://opcfoundation.org/UA/:InterfaceName"
                                                                + " (declaration i=24190, owner"
                                                                + " i=24188) on "
                                                                + getNodeId()));
                                                  }
                                                  if (!target.getTargetId().isLocal()) {
                                                    return CompletableFuture.failedFuture(
                                                        new UaException(
                                                            StatusCodes.Bad_NotSupported,
                                                            "http://opcfoundation.org/UA/:InterfaceName"
                                                                + " (declaration i=24190, owner"
                                                                + " i=24188) on "
                                                                + getNodeId()));
                                                  }
                                                  if (result.isCancelled()) {
                                                    return CompletableFuture.failedFuture(
                                                        new CancellationException());
                                                  }
                                                  var localTarget =
                                                      target
                                                          .getTargetId()
                                                          .toNodeId(client.getNamespaceTable());
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
                                                                      target
                                                                          .getTargetId()
                                                                          .toNodeId(namespaceTable);
                                                                  if (resolvedTarget.isEmpty()) {
                                                                    return CompletableFuture
                                                                        .failedFuture(
                                                                            new UaException(
                                                                                StatusCodes
                                                                                    .Bad_NodeIdInvalid,
                                                                                "http://opcfoundation.org/UA/:InterfaceName"
                                                                                    + " (declaration"
                                                                                    + " i=24190,"
                                                                                    + " owner"
                                                                                    + " i=24188) on"
                                                                                    + " "
                                                                                    + getNodeId()));
                                                                  }
                                                                  return CompletableFuture
                                                                      .completedFuture(
                                                                          resolvedTarget
                                                                              .orElseThrow());
                                                                }));
                                                  }
                                                }
                                                return CompletableFuture.allOf(
                                                        identities.toArray(
                                                            CompletableFuture[]::new))
                                                    .thenCompose(
                                                        ready -> {
                                                          if (result.isCancelled()) {
                                                            return CompletableFuture.failedFuture(
                                                                new CancellationException());
                                                          }
                                                          var unique = new LinkedHashSet<NodeId>();
                                                          identities.forEach(
                                                              identity ->
                                                                  unique.add(identity.join()));
                                                          if (unique.stream()
                                                              .anyMatch(NodeId::isNull)) {
                                                            return CompletableFuture.failedFuture(
                                                                new UaException(
                                                                    StatusCodes.Bad_NodeIdInvalid,
                                                                    "http://opcfoundation.org/UA/:InterfaceName"
                                                                        + " (declaration i=24190,"
                                                                        + " owner i=24188) on "
                                                                        + getNodeId()));
                                                          }
                                                          if (unique.size() != 1) {
                                                            return CompletableFuture.failedFuture(
                                                                new UaException(
                                                                    StatusCodes.Bad_TooManyMatches,
                                                                    "http://opcfoundation.org/UA/:InterfaceName"
                                                                        + " (declaration i=24190,"
                                                                        + " owner i=24188) on "
                                                                        + getNodeId()));
                                                          }
                                                          return client
                                                              .getAddressSpace()
                                                              .getNodeAsync(
                                                                  unique.iterator().next())
                                                              .thenCompose(
                                                                  node -> {
                                                                    if (node == null
                                                                        || node.getNodeClass()
                                                                            != NodeClass.Variable) {
                                                                      return CompletableFuture
                                                                          .failedFuture(
                                                                              new UaException(
                                                                                  StatusCodes
                                                                                      .Bad_NodeClassInvalid,
                                                                                  "http://opcfoundation.org/UA/:InterfaceName"
                                                                                      + " (declaration"
                                                                                      + " i=24190,"
                                                                                      + " owner"
                                                                                      + " i=24188)"
                                                                                      + " on "
                                                                                      + getNodeId()));
                                                                    }
                                                                    return CompletableFuture
                                                                        .completedFuture(node);
                                                                  });
                                                        });
                                              });
                                    });
                              });
                      hop0.whenComplete(
                          (node, failure) -> {
                            if (failure != null) {
                              result.completeExceptionally(failure);
                            } else if (node != null && !(node instanceof UaVariableNode)) {
                              result.completeExceptionally(
                                  new UaException(
                                      StatusCodes.Bad_TypeMismatch,
                                      "http://opcfoundation.org/UA/:InterfaceName (declaration"
                                          + " i=24190, owner i=24188)"));
                            } else {
                              result.complete((UaVariableNode) node);
                            }
                          });
                    } catch (Exception e) {
                      result.completeExceptionally(e);
                    }
                    return result;
                  })
              .get();
      return ViewFutures.compose(
          memberRequest,
          found ->
              found == null
                  ? CompletableFuture.completedFuture(null)
                  : views.variableNodeAsync(found.getNodeId()));
    } catch (RuntimeException failure) {
      return CompletableFuture.failedFuture(failure);
    }
  }

  private @Nullable UaVariableNode viewMember1() throws UaException {
    return ViewFutures.await(viewMember1Async());
  }

  private CompletableFuture<@Nullable UaVariableNode> viewMember2Async() {
    try {
      views.checkOpen();
      var memberRequest =
          ((Supplier<CompletableFuture<@Nullable UaVariableNode>>)
                  () -> {
                    CompletableFuture<UaVariableNode> result = new CompletableFuture<>();
                    try {
                      CompletableFuture<NodeId> lookup =
                          CompletableFuture.completedFuture(getNodeId());
                      CompletableFuture<UaNode> hop0 =
                          lookup.thenCompose(
                              parent -> {
                                NodeId parentId = parent;
                                if (result.isCancelled()) {
                                  return CompletableFuture.failedFuture(
                                      new CancellationException());
                                }
                                if (parentId == null) {
                                  return CompletableFuture.completedFuture(null);
                                }
                                CompletableFuture<Void> namespaceReady;
                                if (client
                                            .getNamespaceTable()
                                            .getIndex("http://opcfoundation.org/UA/")
                                        == null
                                    || client
                                            .getNamespaceTable()
                                            .getIndex("http://opcfoundation.org/UA/")
                                        == null) {
                                  namespaceReady =
                                      client.readNamespaceTableAsync().thenApply(ignored -> null);
                                } else {
                                  namespaceReady = CompletableFuture.completedFuture(null);
                                }
                                return namespaceReady.thenCompose(
                                    ignored -> {
                                      if (result.isCancelled()) {
                                        return CompletableFuture.failedFuture(
                                            new CancellationException());
                                      }
                                      var namespaceIndex =
                                          client
                                              .getNamespaceTable()
                                              .getIndex("http://opcfoundation.org/UA/");
                                      var referenceId =
                                          ExpandedNodeId.parse("i=47")
                                              .toNodeId(client.getNamespaceTable());
                                      if (namespaceIndex == null || referenceId.isEmpty()) {
                                        return CompletableFuture.failedFuture(
                                            new UaException(
                                                StatusCodes.Bad_NodeIdInvalid,
                                                "http://opcfoundation.org/UA/:TimeAwareOffset"
                                                    + " (declaration i=24194, owner i=24191) on "
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
                                                            namespaceIndex, "TimeAwareOffset"))
                                                  }));
                                      return client
                                          .translateBrowsePathsAsync(List.of(browsePath))
                                          .thenCompose(
                                              response -> {
                                                if (result.isCancelled()) {
                                                  return CompletableFuture.failedFuture(
                                                      new CancellationException());
                                                }
                                                var results =
                                                    response == null ? null : response.getResults();
                                                if (results == null
                                                    || results.length != 1
                                                    || results[0] == null
                                                    || results[0].getStatusCode() == null) {
                                                  return CompletableFuture.failedFuture(
                                                      new UaException(
                                                          StatusCodes.Bad_UnexpectedError,
                                                          "http://opcfoundation.org/UA/:TimeAwareOffset"
                                                              + " (declaration i=24194, owner"
                                                              + " i=24191) on "
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
                                                          "http://opcfoundation.org/UA/:TimeAwareOffset"
                                                              + " (declaration i=24194, owner"
                                                              + " i=24191)"));
                                                }
                                                var targets = operation.getTargets();
                                                if (targets == null || targets.length == 0) {
                                                  return CompletableFuture.failedFuture(
                                                      new UaException(
                                                          StatusCodes.Bad_UnexpectedError,
                                                          "http://opcfoundation.org/UA/:TimeAwareOffset"
                                                              + " (declaration i=24194, owner"
                                                              + " i=24191) on "
                                                              + getNodeId()));
                                                }
                                                var identities =
                                                    new ArrayList<CompletableFuture<NodeId>>();
                                                for (var target : targets) {
                                                  if (target == null
                                                      || target.getTargetId() == null
                                                      || target.getRemainingPathIndex() == null
                                                      || target.getRemainingPathIndex().longValue()
                                                          != 0xffffffffL) {
                                                    return CompletableFuture.failedFuture(
                                                        new UaException(
                                                            StatusCodes.Bad_UnexpectedError,
                                                            "http://opcfoundation.org/UA/:TimeAwareOffset"
                                                                + " (declaration i=24194, owner"
                                                                + " i=24191) on "
                                                                + getNodeId()));
                                                  }
                                                  if (!target.getTargetId().isLocal()) {
                                                    return CompletableFuture.failedFuture(
                                                        new UaException(
                                                            StatusCodes.Bad_NotSupported,
                                                            "http://opcfoundation.org/UA/:TimeAwareOffset"
                                                                + " (declaration i=24194, owner"
                                                                + " i=24191) on "
                                                                + getNodeId()));
                                                  }
                                                  if (result.isCancelled()) {
                                                    return CompletableFuture.failedFuture(
                                                        new CancellationException());
                                                  }
                                                  var localTarget =
                                                      target
                                                          .getTargetId()
                                                          .toNodeId(client.getNamespaceTable());
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
                                                                      target
                                                                          .getTargetId()
                                                                          .toNodeId(namespaceTable);
                                                                  if (resolvedTarget.isEmpty()) {
                                                                    return CompletableFuture
                                                                        .failedFuture(
                                                                            new UaException(
                                                                                StatusCodes
                                                                                    .Bad_NodeIdInvalid,
                                                                                "http://opcfoundation.org/UA/:TimeAwareOffset"
                                                                                    + " (declaration"
                                                                                    + " i=24194,"
                                                                                    + " owner"
                                                                                    + " i=24191) on"
                                                                                    + " "
                                                                                    + getNodeId()));
                                                                  }
                                                                  return CompletableFuture
                                                                      .completedFuture(
                                                                          resolvedTarget
                                                                              .orElseThrow());
                                                                }));
                                                  }
                                                }
                                                return CompletableFuture.allOf(
                                                        identities.toArray(
                                                            CompletableFuture[]::new))
                                                    .thenCompose(
                                                        ready -> {
                                                          if (result.isCancelled()) {
                                                            return CompletableFuture.failedFuture(
                                                                new CancellationException());
                                                          }
                                                          var unique = new LinkedHashSet<NodeId>();
                                                          identities.forEach(
                                                              identity ->
                                                                  unique.add(identity.join()));
                                                          if (unique.stream()
                                                              .anyMatch(NodeId::isNull)) {
                                                            return CompletableFuture.failedFuture(
                                                                new UaException(
                                                                    StatusCodes.Bad_NodeIdInvalid,
                                                                    "http://opcfoundation.org/UA/:TimeAwareOffset"
                                                                        + " (declaration i=24194,"
                                                                        + " owner i=24191) on "
                                                                        + getNodeId()));
                                                          }
                                                          if (unique.size() != 1) {
                                                            return CompletableFuture.failedFuture(
                                                                new UaException(
                                                                    StatusCodes.Bad_TooManyMatches,
                                                                    "http://opcfoundation.org/UA/:TimeAwareOffset"
                                                                        + " (declaration i=24194,"
                                                                        + " owner i=24191) on "
                                                                        + getNodeId()));
                                                          }
                                                          return client
                                                              .getAddressSpace()
                                                              .getNodeAsync(
                                                                  unique.iterator().next())
                                                              .thenCompose(
                                                                  node -> {
                                                                    if (node == null
                                                                        || node.getNodeClass()
                                                                            != NodeClass.Variable) {
                                                                      return CompletableFuture
                                                                          .failedFuture(
                                                                              new UaException(
                                                                                  StatusCodes
                                                                                      .Bad_NodeClassInvalid,
                                                                                  "http://opcfoundation.org/UA/:TimeAwareOffset"
                                                                                      + " (declaration"
                                                                                      + " i=24194,"
                                                                                      + " owner"
                                                                                      + " i=24191)"
                                                                                      + " on "
                                                                                      + getNodeId()));
                                                                    }
                                                                    return CompletableFuture
                                                                        .completedFuture(node);
                                                                  });
                                                        });
                                              });
                                    });
                              });
                      hop0.whenComplete(
                          (node, failure) -> {
                            if (failure != null) {
                              result.completeExceptionally(failure);
                            } else if (node != null && !(node instanceof UaVariableNode)) {
                              result.completeExceptionally(
                                  new UaException(
                                      StatusCodes.Bad_TypeMismatch,
                                      "http://opcfoundation.org/UA/:TimeAwareOffset (declaration"
                                          + " i=24194, owner i=24191)"));
                            } else {
                              result.complete((UaVariableNode) node);
                            }
                          });
                    } catch (Exception e) {
                      result.completeExceptionally(e);
                    }
                    return result;
                  })
              .get();
      return ViewFutures.compose(
          memberRequest,
          found ->
              found == null
                  ? CompletableFuture.completedFuture(null)
                  : views.variableNodeAsync(found.getNodeId()));
    } catch (RuntimeException failure) {
      return CompletableFuture.failedFuture(failure);
    }
  }

  private @Nullable UaVariableNode viewMember2() throws UaException {
    return ViewFutures.await(viewMember2Async());
  }

  /**
   * Resolves the required member by its namespace-qualified path. A missing member fails with
   * Bad_NotFound. Resolution does not create a UA node. It can perform service I/O and construct or
   * reuse a Java wrapper in Milo's address space cache. A reference can change after lookup.
   *
   * <p>The returned object exposes the selected child contract and shares the retained raw node's
   * state through this view context.
   *
   * @return the existing member
   * @throws org.eclipse.milo.opcua.stack.core.UaException if a required node is absent, resolution
   *     fails, or a checked conversion fails
   * @throws IllegalStateException if the view context is closed
   */
  @Override
  public BaseDataVariableType getMacAddressNode() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    return ViewFutures.await(getMacAddressNodeAsync());
  }

  private CompletableFuture<? extends BaseDataVariableType> getMacAddressNodeAsyncImplementation() {
    return ViewFutures.compose(
        viewMember0Async(),
        child ->
            child == null
                ? CompletableFuture.completedFuture(null)
                : views.wrapVariableAsync(child.getNodeId(), BaseDataVariableTypeView.TYPE));
  }

  /**
   * Resolves the required member by its namespace-qualified path. A missing member fails with
   * Bad_NotFound. Resolution does not create a UA node. It can perform service I/O and construct or
   * reuse a Java wrapper in Milo's address space cache. A reference can change after lookup.
   *
   * <p>Lookup, conversion and service failures complete the future exceptionally. UaException
   * causes preserve OPC UA status. Incompatible plain payload casts can complete exceptionally with
   * ClassCastException. Cancellation does not promise transport cancellation or rollback.
   *
   * <p>The returned object exposes the selected child contract and shares the retained raw node's
   * state through this view context.
   *
   * <p>A closed view context completes the future exceptionally with IllegalStateException.
   *
   * @return a nonnull future completing with the existing member
   */
  @Override
  public CompletableFuture<? extends BaseDataVariableType> getMacAddressNodeAsync() {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.getMacAddressNodeAsyncImplementation());
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }

  /**
   * Gets the existing member's local value without checking its quality. A null value is valid; an
   * absent node fails with Bad_NotFound. Lookup may perform service I/O; the value is not read
   * remotely. Use the node's raw DataValue to inspect quality and timestamps.
   *
   * <p>Concrete enum conversions reject unknown numbers with Bad_OutOfRange. Structured decoding
   * and existing rank/type checks retain their failures.
   *
   * @return the value, which may be null on a present member
   * @throws org.eclipse.milo.opcua.stack.core.UaException if a required node is absent, resolution
   *     fails, or a checked conversion fails
   * @throws ClassCastException if a plain payload cast encounters an incompatible Java
   *     representation
   * @throws IllegalStateException if the view context is closed
   */
  @Override
  public @Nullable String getMacAddress() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    var child = viewMember0();
    if (child == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MacAddress (declaration i=24189, owner i=24188)"
              + " on "
              + getNodeId());
    }
    @Nullable String converted;
    {
      Object rawValue = child.getValue().getValue().getValue();
      if (rawValue instanceof Matrix matrix && matrix.isNull()) {
        rawValue = null;
      }
      if (rawValue != null) {
        int actualRank =
            rawValue instanceof Matrix matrix
                ? matrix.getValueRank()
                : ArrayUtil.getValueRank(rawValue);
        Object rankElements = rawValue instanceof Matrix matrix ? matrix.getElements() : rawValue;
        if (!(actualRank == -1)) {
          throw new UaException(
              StatusCodes.Bad_TypeMismatch,
              "http://opcfoundation.org/UA/:MacAddress (declaration i=24189, owner i=24188)");
        }
        if (rawValue instanceof Matrix matrix) {
          if (actualRank < 2
              || rankElements == null
              || !rankElements.getClass().isArray()
              || ArrayUtil.getValueRank(rankElements) != 1) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "http://opcfoundation.org/UA/:MacAddress (declaration i=24189, owner i=24188)");
          }
          long elementCount = 1;
          for (int dimension : matrix.getDimensions()) {
            if (dimension < 0 || elementCount > Integer.MAX_VALUE) {
              throw new UaException(
                  StatusCodes.Bad_TypeMismatch,
                  "http://opcfoundation.org/UA/:MacAddress (declaration i=24189, owner i=24188)");
            }
            elementCount *= dimension;
          }
          if (elementCount != Array.getLength(rankElements)) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "http://opcfoundation.org/UA/:MacAddress (declaration i=24189, owner i=24188)");
          }
        } else if (actualRank > 1) {
          throw new UaException(
              StatusCodes.Bad_TypeMismatch,
              "http://opcfoundation.org/UA/:MacAddress (declaration i=24189, owner i=24188): use"
                  + " Matrix for multiple dimensions");
        }
      }
      Object element = rawValue;
      if (element != null && !(element instanceof String)) {
        throw new UaException(
            StatusCodes.Bad_TypeMismatch,
            "http://opcfoundation.org/UA/:MacAddress (declaration i=24189, owner i=24188)");
      }
      converted = (String) element;
    }
    return converted;
  }

  /**
   * Sets the existing member's local value. A null value is valid. An absent node fails with
   * Bad_NotFound before conversion or mutation. This does not create nodes or silently skip writes.
   * This does not send a Write service request.
   *
   * <p>Concrete enum conversions reject unknown numbers with Bad_OutOfRange. Structured decoding
   * and existing rank/type checks retain their failures.
   *
   * <p>The actual member DataType, rank and maximum dimensions constrain this selected view write.
   * Incompatible values fail with Bad_TypeMismatch; values outside the actual finite enumeration
   * fail with Bad_OutOfRange. Validation finishes before mutation or Write. Type and enumeration
   * discovery can perform service I/O, including for local setters.
   *
   * @param value the value to store; null is permitted
   * @throws org.eclipse.milo.opcua.stack.core.UaException if a required node is absent, resolution
   *     fails, or a checked conversion fails
   * @throws IllegalStateException if the view context is closed
   */
  @Override
  public void setMacAddress(@Nullable String value) throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    var child = viewMember0();
    if (child == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MacAddress (declaration i=24189, owner i=24188)"
              + " on "
              + getNodeId());
    }
    var writeContext = views.localWriteContext(child.getNodeId(), value, KNOWN_ENUMS);
    Variant encoded;
    {
      var checkedWrite_Context = writeContext;
      Object checkedWrite_Value = value;
      var checkedWrite_Selected =
          ExpandedNodeId.parse("i=12")
              .toNodeId(checkedWrite_Context.namespaceTable())
              .orElseThrow(
                  () ->
                      new org.eclipse.milo.opcua.stack.core.UaException(
                          org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_NodeIdInvalid,
                          "Unknown selected DataType namespace:"
                              + " ExpandedNodeId[server=ServerIndex[serverIndex=0],"
                              + " namespace=NamespaceUri[namespaceUri=http://opcfoundation.org/UA/],"
                              + " identifier=12]"));
      int checkedWrite_SelectedRank = -1;
      long[] checkedWrite_SelectedDimensions = new long[] {};
      Set<Integer> checkedWrite_SelectedEnums = null;
      boolean checkedWrite_Wire = false;
      var checkedWrite_Types = checkedWrite_Context.dataTypes();
      var checkedWrite_Actual = checkedWrite_Context.dataType();
      int checkedWrite_Rank = checkedWrite_Context.valueRank();
      var checkedWrite_Bounds = checkedWrite_Context.arrayDimensions();
      if (!checkedWrite_Types.containsType(checkedWrite_Selected)
          || !checkedWrite_Types.containsType(checkedWrite_Actual)
          || !(checkedWrite_Actual.equals(checkedWrite_Selected)
              || checkedWrite_Types.isSubtypeOf(checkedWrite_Actual, checkedWrite_Selected))) {
        throw new org.eclipse.milo.opcua.stack.core.UaException(
            org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
            "Effective DataType "
                + checkedWrite_Actual
                + " does not specialize selected "
                + checkedWrite_Selected);
      }
      boolean checkedWrite_SpecializedRank =
          checkedWrite_SelectedRank == -2
              || checkedWrite_Rank == checkedWrite_SelectedRank
              || checkedWrite_SelectedRank == -3
                  && (checkedWrite_Rank == -1 || checkedWrite_Rank == 1)
              || checkedWrite_SelectedRank == 0 && checkedWrite_Rank > 0;
      if (!checkedWrite_SpecializedRank
          || checkedWrite_Rank < -3
          || checkedWrite_Bounds != null
              && checkedWrite_Bounds.length != 0
              && (checkedWrite_Rank <= 0 || checkedWrite_Bounds.length != checkedWrite_Rank)) {
        throw new org.eclipse.milo.opcua.stack.core.UaException(
            org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
            "Effective ValueRank or ArrayDimensions conflict with the selected contract");
      }
      if (checkedWrite_SelectedDimensions.length != 0) {
        if (checkedWrite_SelectedRank <= 0
            || checkedWrite_SelectedDimensions.length != checkedWrite_SelectedRank) {
          throw new org.eclipse.milo.opcua.stack.core.UaException(
              org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
              "Invalid selected ArrayDimensions");
        }
        for (int checkedWrite_Index = 0;
            checkedWrite_Index < checkedWrite_SelectedDimensions.length;
            checkedWrite_Index++) {
          long checkedWrite_Maximum = checkedWrite_SelectedDimensions[checkedWrite_Index];
          if (checkedWrite_Maximum != 0
              && (checkedWrite_Bounds == null
                  || checkedWrite_Bounds.length != checkedWrite_SelectedDimensions.length
                  || checkedWrite_Bounds[checkedWrite_Index].longValue() == 0
                  || checkedWrite_Bounds[checkedWrite_Index].longValue() > checkedWrite_Maximum)) {
            throw new org.eclipse.milo.opcua.stack.core.UaException(
                org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                "Effective ArrayDimensions broaden the selected maximum");
          }
        }
      }
      if (checkedWrite_SelectedEnums != null
          && (checkedWrite_Context.enumValues() == null
              || !checkedWrite_SelectedEnums.containsAll(checkedWrite_Context.enumValues()))) {
        throw new org.eclipse.milo.opcua.stack.core.UaException(
            org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
            "Effective Enumeration domain broadens the selected contract");
      }
      try {
        if (checkedWrite_Value
                instanceof
                org.eclipse.milo.opcua.stack.core.types.builtin.Matrix checkedWrite_Matrix
            && checkedWrite_Matrix.isNull()) {
          checkedWrite_Value = null;
        }
        if (checkedWrite_Value != null
            && checkedWrite_SelectedRank == 1
            && checkedWrite_Types.getBackingClass(checkedWrite_Selected)
                == org.eclipse.milo.opcua.stack.core.types.builtin.Variant.class) {
          if (!(checkedWrite_Value instanceof java.lang.Object[])
              || org.eclipse.milo.opcua.stack.core.util.ArrayUtil.getValueRank(checkedWrite_Value)
                  != 1)
            throw new org.eclipse.milo.opcua.stack.core.UaException(
                org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                "Selected BaseDataType array requires Java payload values");
          for (java.lang.Object checkedWrite_Payload : (java.lang.Object[]) checkedWrite_Value)
            org.eclipse.milo.opcua.stack.core.types.builtin.Variant.of(checkedWrite_Payload);
          java.lang.Class<?> checkedWrite_EffectiveBacking =
              checkedWrite_Types.getBackingClass(checkedWrite_Actual);
          boolean checkedWrite_SpecializedPayloads =
              checkedWrite_EffectiveBacking
                      != org.eclipse.milo.opcua.stack.core.types.builtin.Variant.class
                  && !checkedWrite_Actual.equals(
                      org.eclipse.milo.opcua.stack.core.NodeIds.Structure)
                  && !checkedWrite_Types.isStructType(checkedWrite_Actual)
                  && !checkedWrite_Actual.equals(
                      org.eclipse.milo.opcua.stack.core.NodeIds.Enumeration)
                  && !checkedWrite_Types.isSubtypeOf(
                      checkedWrite_Actual, org.eclipse.milo.opcua.stack.core.NodeIds.Enumeration);
          if (checkedWrite_SpecializedPayloads
              && checkedWrite_Value.getClass().getComponentType() == java.lang.Object.class) {
            java.lang.Object[] checkedWrite_Payloads = (java.lang.Object[]) checkedWrite_Value;
            java.lang.Object checkedWrite_Projected =
                java.lang.reflect.Array.newInstance(
                    checkedWrite_EffectiveBacking, checkedWrite_Payloads.length);
            for (int checkedWrite_Index = 0;
                checkedWrite_Index < checkedWrite_Payloads.length;
                checkedWrite_Index++) {
              java.lang.Object checkedWrite_Payload = checkedWrite_Payloads[checkedWrite_Index];
              if (checkedWrite_Payload
                  instanceof
                  org.eclipse.milo.opcua.stack.core.types.builtin.OptionSetUInteger<?>
                      checkedWrite_Option) checkedWrite_Payload = checkedWrite_Option.getValue();
              java.lang.reflect.Array.set(
                  checkedWrite_Projected, checkedWrite_Index, checkedWrite_Payload);
            }
            checkedWrite_Value = checkedWrite_Projected;
          }
        }
        Object numericElements =
            checkedWrite_Value instanceof Matrix
                ? ((Matrix) checkedWrite_Value).getElements()
                : checkedWrite_Value;
        if (numericElements != null
            && numericElements.getClass().isArray()
            && (numericElements.getClass().getComponentType() == Number.class
                || numericElements.getClass().getComponentType() == UNumber.class)
            && (checkedWrite_Actual.equals(NodeIds.Number)
                || checkedWrite_Types.isSubtypeOf(checkedWrite_Actual, NodeIds.Number))) {
          Class<?> numericElementType = null;
          for (int numericIndex = 0;
              numericIndex < Array.getLength(numericElements);
              numericIndex++) {
            Object numericElement = Array.get(numericElements, numericIndex);
            if (numericElement != null) {
              if (numericElementType != null && numericElementType != numericElement.getClass()) {
                throw new UaException(
                    StatusCodes.Bad_TypeMismatch,
                    "An abstract numeric array requires one homogeneous wire element type");
              }
              numericElementType = numericElement.getClass();
            }
          }
          if (numericElementType == null) {
            numericElementType = checkedWrite_Types.getBackingClass(checkedWrite_Actual);
          }
          if (numericElementType == Number.class || numericElementType == UNumber.class) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "An empty or all-null abstract numeric array requires a concretely typed array");
          }
          Object numericArray =
              Array.newInstance(numericElementType, Array.getLength(numericElements));
          for (int numericIndex = 0;
              numericIndex < Array.getLength(numericElements);
              numericIndex++) {
            Array.set(numericArray, numericIndex, Array.get(numericElements, numericIndex));
          }
          if (checkedWrite_Value instanceof Matrix) {
            checkedWrite_Value =
                new Matrix(
                    numericArray,
                    ((Matrix) checkedWrite_Value).getDimensions().clone(),
                    ((Matrix) checkedWrite_Value)
                        .getDataType()
                        .orElseThrow(
                            () ->
                                new UaException(
                                    StatusCodes.Bad_TypeMismatch,
                                    "A numeric Matrix requires an explicit wire DataType")),
                    ((Matrix) checkedWrite_Value).getDataTypeId().orElse(null));
          } else {
            checkedWrite_Value = numericArray;
          }
        }

        if (checkedWrite_Value != null) {
          java.lang.Object checkedWrite_Elements =
              checkedWrite_Value
                      instanceof
                      org.eclipse.milo.opcua.stack.core.types.builtin.Matrix checkedWrite_Matrix
                  ? checkedWrite_Matrix.getElements()
                  : checkedWrite_Value;
          int checkedWrite_ValueRank =
              checkedWrite_Value
                      instanceof
                      org.eclipse.milo.opcua.stack.core.types.builtin.Matrix checkedWrite_Matrix
                  ? checkedWrite_Matrix.getValueRank()
                  : org.eclipse.milo.opcua.stack.core.util.ArrayUtil.getValueRank(
                      checkedWrite_Value);
          boolean checkedWrite_Empty =
              checkedWrite_Value.getClass().isArray()
                  && org.eclipse.milo.opcua.stack.core.util.ArrayUtil.getValueRank(
                          checkedWrite_Value)
                      == 1
                  && java.lang.reflect.Array.getLength(checkedWrite_Value) == 0;
          boolean checkedWrite_Shape =
              checkedWrite_Rank == -2
                  || checkedWrite_Rank == -3
                      && (checkedWrite_ValueRank == -1 || checkedWrite_ValueRank == 1)
                  || checkedWrite_Rank == -1 && checkedWrite_ValueRank == -1
                  || checkedWrite_Rank == 0 && checkedWrite_ValueRank >= 1
                  || checkedWrite_Rank > 0
                      && (checkedWrite_ValueRank == checkedWrite_Rank || checkedWrite_Empty);
          if (!checkedWrite_Shape)
            throw new org.eclipse.milo.opcua.stack.core.UaException(
                org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                "ValueRank mismatch");
          if (checkedWrite_Value
              instanceof
              org.eclipse.milo.opcua.stack.core.types.builtin.Matrix checkedWrite_Matrix) {
            int[] checkedWrite_Dimensions = checkedWrite_Matrix.getDimensions();
            if (checkedWrite_Dimensions.length < 2
                || checkedWrite_Elements == null
                || !checkedWrite_Elements.getClass().isArray()
                || org.eclipse.milo.opcua.stack.core.util.ArrayUtil.getValueRank(
                        checkedWrite_Elements)
                    != 1) {
              throw new org.eclipse.milo.opcua.stack.core.UaException(
                  org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                  "Malformed Matrix representation");
            }
            long checkedWrite_Count = 1;
            for (int checkedWrite_Dimension : checkedWrite_Dimensions) {
              if (checkedWrite_Dimension < 0 || checkedWrite_Count > java.lang.Integer.MAX_VALUE)
                throw new org.eclipse.milo.opcua.stack.core.UaException(
                    org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                    "Malformed Matrix dimensions");
              checkedWrite_Count *= checkedWrite_Dimension;
            }
            if (checkedWrite_Count != java.lang.reflect.Array.getLength(checkedWrite_Elements)
                || !checkedWrite_Matrix
                    .getDataType()
                    .equals(
                        org.eclipse.milo.opcua.stack.core.types.builtin.Variant.of(
                                checkedWrite_Elements)
                            .getDataType())) {
              throw new org.eclipse.milo.opcua.stack.core.UaException(
                  org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                  "Matrix dimensions or DataType do not match elements");
            }
          }
          if (!checkedWrite_Empty
              && checkedWrite_Bounds != null
              && checkedWrite_Bounds.length != 0) {
            int[] checkedWrite_Dimensions =
                checkedWrite_Value
                        instanceof
                        org.eclipse.milo.opcua.stack.core.types.builtin.Matrix checkedWrite_Matrix
                    ? checkedWrite_Matrix.getDimensions()
                    : org.eclipse.milo.opcua.stack.core.util.ArrayUtil.getDimensions(
                        checkedWrite_Value);
            if (checkedWrite_Dimensions.length != checkedWrite_Bounds.length)
              throw new org.eclipse.milo.opcua.stack.core.UaException(
                  org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                  "ArrayDimensions mismatch");
            for (int checkedWrite_Index = 0;
                checkedWrite_Index < checkedWrite_Dimensions.length;
                checkedWrite_Index++) {
              if (checkedWrite_Bounds[checkedWrite_Index].longValue() != 0
                  && checkedWrite_Dimensions[checkedWrite_Index]
                      > checkedWrite_Bounds[checkedWrite_Index].longValue())
                throw new org.eclipse.milo.opcua.stack.core.UaException(
                    org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                    "Value exceeds ArrayDimensions maximum");
            }
          }
          boolean checkedWrite_Array = checkedWrite_Elements.getClass().isArray();
          int checkedWrite_Length =
              checkedWrite_Array ? java.lang.reflect.Array.getLength(checkedWrite_Elements) : 1;
          boolean checkedWrite_Structure =
              checkedWrite_Actual.equals(org.eclipse.milo.opcua.stack.core.NodeIds.Structure)
                  || checkedWrite_Types.isStructType(checkedWrite_Actual);
          boolean checkedWrite_Enumeration =
              checkedWrite_Actual.equals(org.eclipse.milo.opcua.stack.core.NodeIds.Enumeration)
                  || checkedWrite_Types.isSubtypeOf(
                      checkedWrite_Actual, org.eclipse.milo.opcua.stack.core.NodeIds.Enumeration);
          boolean checkedWrite_Payloads =
              checkedWrite_Types.getBackingClass(checkedWrite_Actual)
                  == org.eclipse.milo.opcua.stack.core.types.builtin.Variant.class;
          boolean checkedWrite_PayloadArray =
              checkedWrite_Payloads
                  && checkedWrite_SelectedRank == 1
                  && checkedWrite_Types.getBackingClass(checkedWrite_Selected)
                      == org.eclipse.milo.opcua.stack.core.types.builtin.Variant.class;
          java.lang.Object checkedWrite_Converted = checkedWrite_Elements;
          if (checkedWrite_Structure) {
            var checkedWrite_Codec =
                checkedWrite_Context
                    .encodingContext()
                    .getDataTypeManager()
                    .getCodec(checkedWrite_Actual);
            java.lang.Class<?> checkedWrite_Class =
                checkedWrite_Codec == null
                    ? org.eclipse.milo.opcua.stack.core.types.UaStructuredType.class
                    : checkedWrite_Codec.getType();
            if (checkedWrite_Array)
              checkedWrite_Converted =
                  java.lang.reflect.Array.newInstance(checkedWrite_Class, checkedWrite_Length);
            for (int checkedWrite_Index = 0;
                checkedWrite_Index < checkedWrite_Length;
                checkedWrite_Index++) {
              java.lang.Object checkedWrite_Element =
                  checkedWrite_Array
                      ? java.lang.reflect.Array.get(checkedWrite_Elements, checkedWrite_Index)
                      : checkedWrite_Elements;
              if (checkedWrite_Element
                  instanceof
                  org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject
                      checkedWrite_Object) {
                checkedWrite_Element =
                    checkedWrite_Object.isNull()
                        ? null
                        : checkedWrite_Object.decode(checkedWrite_Context.encodingContext());
              }
              if (checkedWrite_Element != null) {
                if (!(checkedWrite_Element
                    instanceof org.eclipse.milo.opcua.stack.core.types.UaStructuredType))
                  throw new org.eclipse.milo.opcua.stack.core.UaException(
                      org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                      "Structure value required");
                var checkedWrite_TypeId =
                    ((org.eclipse.milo.opcua.stack.core.types.UaStructuredType)
                            checkedWrite_Element)
                        .getTypeId()
                        .toNodeId(checkedWrite_Context.namespaceTable())
                        .orElse(org.eclipse.milo.opcua.stack.core.types.builtin.NodeId.NULL_VALUE);
                boolean checkedWrite_Abstract =
                    checkedWrite_Actual.equals(org.eclipse.milo.opcua.stack.core.NodeIds.Structure)
                        || java.lang.Boolean.TRUE.equals(
                            checkedWrite_Types.getType(checkedWrite_Actual).isAbstract());
                if (!(checkedWrite_Abstract
                    ? checkedWrite_Types.isSubtypeOf(checkedWrite_TypeId, checkedWrite_Actual)
                    : checkedWrite_Actual.equals(checkedWrite_TypeId)))
                  throw new org.eclipse.milo.opcua.stack.core.UaException(
                      org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                      "Structure identity does not match the effective DataType");
              }
              if (checkedWrite_Array)
                java.lang.reflect.Array.set(
                    checkedWrite_Converted, checkedWrite_Index, checkedWrite_Element);
              else checkedWrite_Converted = checkedWrite_Element;
            }
          } else if (checkedWrite_Enumeration) {
            if (checkedWrite_Array)
              checkedWrite_Converted = new java.lang.Integer[checkedWrite_Length];
            for (int checkedWrite_Index = 0;
                checkedWrite_Index < checkedWrite_Length;
                checkedWrite_Index++) {
              java.lang.Object checkedWrite_Element =
                  checkedWrite_Array
                      ? java.lang.reflect.Array.get(checkedWrite_Elements, checkedWrite_Index)
                      : checkedWrite_Elements;
              if (checkedWrite_Element
                  instanceof
                  org.eclipse.milo.opcua.stack.core.types.UaEnumeratedType checkedWrite_Enum)
                checkedWrite_Element = checkedWrite_Enum.getValue();
              if (checkedWrite_Element != null
                  && !(checkedWrite_Element instanceof java.lang.Integer))
                throw new org.eclipse.milo.opcua.stack.core.UaException(
                    org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                    "Enumeration requires an Int32 value");
              if (checkedWrite_Element != null
                  && (checkedWrite_Context.enumValues() != null
                          && !checkedWrite_Context.enumValues().contains(checkedWrite_Element)
                      || checkedWrite_SelectedEnums != null
                          && !checkedWrite_SelectedEnums.contains(checkedWrite_Element)))
                throw new org.eclipse.milo.opcua.stack.core.UaException(
                    org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_OutOfRange,
                    "Unknown Enumeration value: " + checkedWrite_Element);
              if (checkedWrite_Wire && checkedWrite_Array && checkedWrite_Element == null)
                throw new org.eclipse.milo.opcua.stack.core.UaException(
                    org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                    "Enumeration wire arrays cannot contain null elements");
              if (checkedWrite_Array)
                java.lang.reflect.Array.set(
                    checkedWrite_Converted, checkedWrite_Index, checkedWrite_Element);
              else checkedWrite_Converted = checkedWrite_Element;
            }
          } else if (checkedWrite_PayloadArray) {
            if (!checkedWrite_Array
                || org.eclipse.milo.opcua.stack.core.util.ArrayUtil.getValueRank(
                        checkedWrite_Elements)
                    != 1)
              throw new org.eclipse.milo.opcua.stack.core.UaException(
                  org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                  "BaseDataType array requires Java payload values");
            checkedWrite_Converted =
                new org.eclipse.milo.opcua.stack.core.types.builtin.Variant[checkedWrite_Length];
            for (int checkedWrite_Index = 0;
                checkedWrite_Index < checkedWrite_Length;
                checkedWrite_Index++) {
              java.lang.Object checkedWrite_Element =
                  java.lang.reflect.Array.get(checkedWrite_Elements, checkedWrite_Index);
              if (checkedWrite_Wire)
                checkedWrite_Element =
                    org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject.encodeValue(
                        checkedWrite_Context.encodingContext(), checkedWrite_Element);
              ((org.eclipse.milo.opcua.stack.core.types.builtin.Variant[]) checkedWrite_Converted)
                      [checkedWrite_Index] =
                  org.eclipse.milo.opcua.stack.core.types.builtin.Variant.of(checkedWrite_Element);
            }
          } else {
            java.lang.Object checkedWrite_Check = checkedWrite_Elements;
            java.lang.Class<?> checkedWrite_ElementsClass =
                org.eclipse.milo.opcua.stack.core.util.ArrayUtil.getBoxedType(
                    checkedWrite_Elements);
            boolean checkedWrite_Options =
                org.eclipse.milo.opcua.stack.core.types.builtin.OptionSetUInteger.class
                    .isAssignableFrom(checkedWrite_ElementsClass);
            if (checkedWrite_Options) {
              java.lang.Class<?> checkedWrite_Backing =
                  org.eclipse.milo.opcua.stack.core.types.builtin.Variant.of(checkedWrite_Elements)
                      .getDataType()
                      .orElseThrow()
                      .getBackingClass();
              if (checkedWrite_Array)
                checkedWrite_Converted =
                    java.lang.reflect.Array.newInstance(checkedWrite_Backing, checkedWrite_Length);
              for (int checkedWrite_Index = 0;
                  checkedWrite_Index < checkedWrite_Length;
                  checkedWrite_Index++) {
                java.lang.Object checkedWrite_Element =
                    checkedWrite_Array
                        ? java.lang.reflect.Array.get(checkedWrite_Elements, checkedWrite_Index)
                        : checkedWrite_Elements;
                if (checkedWrite_Element != null)
                  checkedWrite_Element =
                      ((org.eclipse.milo.opcua.stack.core.types.builtin.OptionSetUInteger<?>)
                              checkedWrite_Element)
                          .getValue();
                if (checkedWrite_Wire && checkedWrite_Array && checkedWrite_Element == null)
                  throw new org.eclipse.milo.opcua.stack.core.UaException(
                      org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                      "OptionSet wire arrays cannot contain null elements");
                if (checkedWrite_Array)
                  java.lang.reflect.Array.set(
                      checkedWrite_Converted, checkedWrite_Index, checkedWrite_Element);
                else checkedWrite_Converted = checkedWrite_Element;
              }
              checkedWrite_Check = checkedWrite_Converted;
            }
            org.eclipse.milo.opcua.stack.core.types.builtin.Variant.of(checkedWrite_Check);
            var checkedWrite_Assignable =
                checkedWrite_Types.getBackingClass(checkedWrite_Actual) == java.lang.Number.class
                        && checkedWrite_Types.isSubtypeOf(
                            checkedWrite_Actual, org.eclipse.milo.opcua.stack.core.NodeIds.Integer)
                    ? org.eclipse.milo.opcua.stack.core.NodeIds.Integer
                    : checkedWrite_Actual;
            if (!checkedWrite_Payloads
                && checkedWrite_Check != null
                && !checkedWrite_Types.isAssignable(
                    checkedWrite_Assignable,
                    org.eclipse.milo.opcua.stack.core.util.ArrayUtil.getBoxedType(
                        checkedWrite_Check)))
              throw new org.eclipse.milo.opcua.stack.core.UaException(
                  org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                  "Value does not match effective DataType");
          }
          checkedWrite_Value =
              checkedWrite_Value
                      instanceof
                      org.eclipse.milo.opcua.stack.core.types.builtin.Matrix checkedWrite_Matrix
                  ? new org.eclipse.milo.opcua.stack.core.types.builtin.Matrix(
                      checkedWrite_Converted,
                      checkedWrite_Matrix.getDimensions().clone(),
                      checkedWrite_Matrix.getDataType().orElseThrow(),
                      checkedWrite_Matrix.getDataTypeId().orElse(null))
                  : checkedWrite_Converted;
          if (checkedWrite_Empty && checkedWrite_Rank > 1)
            checkedWrite_Value =
                new org.eclipse.milo.opcua.stack.core.types.builtin.Matrix(
                    checkedWrite_Converted, new int[checkedWrite_Rank]);
          if (checkedWrite_Wire) {
            var numericWireValues = new ArrayDeque<Object[]>();
            var numericWirePath = Collections.newSetFromMap(new IdentityHashMap<Object, Boolean>());
            if (checkedWrite_Value != null) {
              numericWireValues.push(new Object[] {checkedWrite_Value, false});
            }
            while (!numericWireValues.isEmpty()) {
              Object[] numericWireFrame = numericWireValues.pop();
              Object numericWireValue = numericWireFrame[0];
              if ((Boolean) numericWireFrame[1]) {
                numericWirePath.remove(numericWireValue);
                continue;
              }
              while (numericWireValue instanceof Variant || numericWireValue instanceof DataValue) {
                if (numericWireValue instanceof DataValue) {
                  if (((DataValue) numericWireValue).getValue() == null) {
                    throw new UaException(
                        StatusCodes.Bad_TypeMismatch,
                        "A DataValue requires a value wrapper; use Variant.NULL_VALUE for null");
                  }
                  if (((DataValue) numericWireValue).getStatusCode() == null) {
                    throw new UaException(
                        StatusCodes.Bad_TypeMismatch,
                        "A DataValue requires a StatusCode; use StatusCode.GOOD for Good");
                  }
                  numericWireValue = ((DataValue) numericWireValue).getValue();
                } else {
                  numericWireValue = ((Variant) numericWireValue).getValue();
                }
              }
              if (numericWireValue instanceof Matrix) {
                numericWireValue = ((Matrix) numericWireValue).getElements();
              }
              if (numericWireValue != null && numericWireValue.getClass().isArray()) {
                if (!numericWirePath.add(numericWireValue)) {
                  throw new UaException(
                      StatusCodes.Bad_TypeMismatch, "Cyclic Variant arrays cannot be encoded");
                }
                numericWireValues.push(new Object[] {numericWireValue, true});
                for (int numericWireIndex = 0;
                    numericWireIndex < Array.getLength(numericWireValue);
                    numericWireIndex++) {
                  Object numericWireElement = Array.get(numericWireValue, numericWireIndex);
                  if (numericWireElement == null
                      && ArrayUtil.getBoxedType(numericWireValue) == Variant.class) {
                    throw new UaException(
                        StatusCodes.Bad_TypeMismatch,
                        "A Variant wire array requires a wrapper for every element; use"
                            + " Variant.NULL_VALUE for null");
                  }
                  if (numericWireElement == null
                      && (UaEnumeratedType.class.isAssignableFrom(
                              ArrayUtil.getBoxedType(numericWireValue))
                          || OptionSetUInteger.class.isAssignableFrom(
                              ArrayUtil.getBoxedType(numericWireValue)))) {
                    throw new UaException(
                        StatusCodes.Bad_TypeMismatch,
                        "An enum or OptionSet wire array cannot encode a null element");
                  }
                  if (numericWireElement == null
                      && ArrayUtil.getBoxedType(numericWireValue) == Boolean.class) {
                    throw new UaException(
                        StatusCodes.Bad_TypeMismatch,
                        "A Boolean wire array cannot retain a null element; Milo encodes it as"
                            + " false");
                  }
                  if (numericWireElement == null
                      && ArrayUtil.getBoxedType(numericWireValue) == StatusCode.class) {
                    throw new UaException(
                        StatusCodes.Bad_TypeMismatch,
                        "A StatusCode wire array cannot retain a null element; Milo encodes it as"
                            + " Good");
                  }
                  if (numericWireElement == null
                      && Number.class.isAssignableFrom(ArrayUtil.getBoxedType(numericWireValue))) {
                    throw new UaException(
                        StatusCodes.Bad_TypeMismatch,
                        "A numeric wire array cannot retain a null element; Milo encodes it as"
                            + " zero");
                  }
                  if (numericWireElement instanceof Variant
                      || numericWireElement instanceof DataValue) {
                    numericWireValues.push(new Object[] {numericWireElement, false});
                  }
                }
              }
            }

            checkedWrite_Value =
                org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject.encodeValue(
                    checkedWrite_Context.encodingContext(), checkedWrite_Value);
          }
        }
        encoded = org.eclipse.milo.opcua.stack.core.types.builtin.Variant.of(checkedWrite_Value);
      } catch (org.eclipse.milo.opcua.stack.core.UaSerializationException checkedWrite_Failure) {
        long checkedWrite_Status =
            checkedWrite_Failure.getStatusCode().getValue()
                    == org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_OutOfRange
                ? org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_OutOfRange
                : org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch;
        throw new org.eclipse.milo.opcua.stack.core.UaException(
            checkedWrite_Status, checkedWrite_Failure);
      } catch (java.lang.IllegalArgumentException
          | java.lang.ClassCastException checkedWrite_Failure) {
        throw new org.eclipse.milo.opcua.stack.core.UaException(
            org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch, checkedWrite_Failure);
      }
    }
    views.checkOpen();
    child.setValue(encoded);
  }

  /**
   * Reads the existing member's value remotely. Only Good status is accepted, including Good
   * subcodes; Uncertain and Bad statuses fail before conversion. A Good null value is valid. An
   * absent node fails with Bad_NotFound. This does not update the wrapper's local value. Use {@code
   * views.variableNode(memberId).readValue()} on the owning ClientViews context to retain quality,
   * timestamps and unconverted values.
   *
   * <p>Concrete enum conversions reject unknown numbers with Bad_OutOfRange. Structured decoding
   * and existing rank/type checks retain their failures.
   *
   * @return the value, which may be null on a present member
   * @throws org.eclipse.milo.opcua.stack.core.UaException if a required node is absent, resolution
   *     fails, or a checked conversion fails; non-Good operation status also fails
   * @throws ClassCastException if a plain payload cast encounters an incompatible Java
   *     representation
   * @throws IllegalStateException if the view context is closed
   */
  @Override
  public @Nullable String readMacAddress() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    return ViewFutures.await(readMacAddressAsync());
  }

  private CompletableFuture<? extends @Nullable String> readMacAddressAsyncImplementation() {
    return ViewFutures.map(
        ViewFutures.compose(
            viewMember0Async(),
            child -> {
              if (child == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MacAddress (declaration i=24189, owner"
                            + " i=24188) on "
                            + getNodeId()));
              }
              views.checkOpen();
              return child.readAttributeAsync(AttributeId.Value).thenApply(response -> response);
            }),
        value -> {
          views.checkOpen();
          if (value == null || value.getStatusCode() == null) {
            throw new CompletionException(new UaException(StatusCodes.Bad_UnexpectedError));
          }
          if (!value.getStatusCode().isGood()) {
            throw new CompletionException(new UaException(value.getStatusCode()));
          }
          try {
            @Nullable String converted;
            {
              Object rawValue = value.getValue().getValue();
              if (rawValue instanceof Matrix matrix && matrix.isNull()) {
                rawValue = null;
              }
              if (rawValue != null) {
                int actualRank =
                    rawValue instanceof Matrix matrix
                        ? matrix.getValueRank()
                        : ArrayUtil.getValueRank(rawValue);
                Object rankElements =
                    rawValue instanceof Matrix matrix ? matrix.getElements() : rawValue;
                if (!(actualRank == -1)) {
                  throw new UaException(
                      StatusCodes.Bad_TypeMismatch,
                      "http://opcfoundation.org/UA/:MacAddress (declaration i=24189, owner"
                          + " i=24188)");
                }
                if (rawValue instanceof Matrix matrix) {
                  if (actualRank < 2
                      || rankElements == null
                      || !rankElements.getClass().isArray()
                      || ArrayUtil.getValueRank(rankElements) != 1) {
                    throw new UaException(
                        StatusCodes.Bad_TypeMismatch,
                        "http://opcfoundation.org/UA/:MacAddress (declaration i=24189, owner"
                            + " i=24188)");
                  }
                  long elementCount = 1;
                  for (int dimension : matrix.getDimensions()) {
                    if (dimension < 0 || elementCount > Integer.MAX_VALUE) {
                      throw new UaException(
                          StatusCodes.Bad_TypeMismatch,
                          "http://opcfoundation.org/UA/:MacAddress (declaration i=24189, owner"
                              + " i=24188)");
                    }
                    elementCount *= dimension;
                  }
                  if (elementCount != Array.getLength(rankElements)) {
                    throw new UaException(
                        StatusCodes.Bad_TypeMismatch,
                        "http://opcfoundation.org/UA/:MacAddress (declaration i=24189, owner"
                            + " i=24188)");
                  }
                } else if (actualRank > 1) {
                  throw new UaException(
                      StatusCodes.Bad_TypeMismatch,
                      "http://opcfoundation.org/UA/:MacAddress (declaration i=24189, owner"
                          + " i=24188): use Matrix for multiple dimensions");
                }
              }
              Object element = rawValue;
              if (element != null && !(element instanceof String)) {
                throw new UaException(
                    StatusCodes.Bad_TypeMismatch,
                    "http://opcfoundation.org/UA/:MacAddress (declaration i=24189, owner i=24188)");
              }
              converted = (String) element;
            }
            return converted;
          } catch (UaException failure) {
            throw new CompletionException(failure);
          }
        });
  }

  /**
   * Reads the existing member's value remotely. Only Good status is accepted, including Good
   * subcodes; Uncertain and Bad statuses fail before conversion. A Good null value is valid. An
   * absent node fails with Bad_NotFound. This does not update the wrapper's local value. Use {@code
   * views.variableNode(memberId).readValue()} on the owning ClientViews context to retain quality,
   * timestamps and unconverted values.
   *
   * <p>Concrete enum conversions reject unknown numbers with Bad_OutOfRange. Structured decoding
   * and existing rank/type checks retain their failures.
   *
   * <p>Lookup, conversion and service failures complete the future exceptionally. UaException
   * causes preserve OPC UA status. Incompatible plain payload casts can complete exceptionally with
   * ClassCastException. Cancellation does not promise transport cancellation or rollback.
   *
   * <p>A closed view context completes the future exceptionally with IllegalStateException.
   *
   * @return a nonnull future completing with the value, which may be null on a present member
   */
  @Override
  public CompletableFuture<? extends @Nullable String> readMacAddressAsync() {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.readMacAddressAsyncImplementation());
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }

  /**
   * Writes the existing member's value remotely. A null value is valid. An absent node fails with
   * Bad_NotFound before conversion or Write. This does not create nodes or update the wrapper's
   * local value.
   *
   * <p>Concrete enum conversions reject unknown numbers with Bad_OutOfRange. Structured decoding
   * and existing rank/type checks retain their failures.
   *
   * <p>The actual member DataType, rank and maximum dimensions constrain this selected view write.
   * Incompatible values fail with Bad_TypeMismatch; values outside the actual finite enumeration
   * fail with Bad_OutOfRange. Validation finishes before mutation or Write. Type and enumeration
   * discovery can perform service I/O, including for local setters.
   *
   * @param value the value to store; null is permitted
   * @throws org.eclipse.milo.opcua.stack.core.UaException if a required node is absent, resolution
   *     fails, or a checked conversion fails; non-Good operation status also fails
   * @throws IllegalStateException if the view context is closed
   */
  @Override
  public void writeMacAddress(@Nullable String value) throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    views.checkOpen();
    StatusCode status = ViewFutures.await(writeMacAddressAsync(value));
    if (status == null) {
      throw new UaException(StatusCodes.Bad_UnexpectedError);
    }
    if (!status.isGood()) {
      throw new UaException(status);
    }
  }

  private CompletableFuture<StatusCode> writeMacAddressAsyncImplementation(@Nullable String value) {
    return ViewFutures.compose(
        viewMember0Async(),
        child -> {
          if (child == null) {
            throw new CompletionException(
                new UaException(
                    StatusCodes.Bad_NotFound,
                    "http://opcfoundation.org/UA/:MacAddress (declaration i=24189, owner i=24188)"
                        + " on "
                        + getNodeId()));
          }
          return ViewFutures.compose(
              views.readWriteContextAsync(child.getNodeId(), value, KNOWN_ENUMS),
              writeContext -> {
                try {
                  Variant encoded;
                  {
                    var checkedWrite_Context = writeContext;
                    Object checkedWrite_Value = value;
                    var checkedWrite_Selected =
                        ExpandedNodeId.parse("i=12")
                            .toNodeId(checkedWrite_Context.namespaceTable())
                            .orElseThrow(
                                () ->
                                    new org.eclipse.milo.opcua.stack.core.UaException(
                                        org.eclipse.milo.opcua.stack.core.StatusCodes
                                            .Bad_NodeIdInvalid,
                                        "Unknown selected DataType namespace:"
                                            + " ExpandedNodeId[server=ServerIndex[serverIndex=0],"
                                            + " namespace=NamespaceUri[namespaceUri=http://opcfoundation.org/UA/],"
                                            + " identifier=12]"));
                    int checkedWrite_SelectedRank = -1;
                    long[] checkedWrite_SelectedDimensions = new long[] {};
                    Set<Integer> checkedWrite_SelectedEnums = null;
                    boolean checkedWrite_Wire = true;
                    var checkedWrite_Types = checkedWrite_Context.dataTypes();
                    var checkedWrite_Actual = checkedWrite_Context.dataType();
                    int checkedWrite_Rank = checkedWrite_Context.valueRank();
                    var checkedWrite_Bounds = checkedWrite_Context.arrayDimensions();
                    if (!checkedWrite_Types.containsType(checkedWrite_Selected)
                        || !checkedWrite_Types.containsType(checkedWrite_Actual)
                        || !(checkedWrite_Actual.equals(checkedWrite_Selected)
                            || checkedWrite_Types.isSubtypeOf(
                                checkedWrite_Actual, checkedWrite_Selected))) {
                      throw new org.eclipse.milo.opcua.stack.core.UaException(
                          org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                          "Effective DataType "
                              + checkedWrite_Actual
                              + " does not specialize selected "
                              + checkedWrite_Selected);
                    }
                    boolean checkedWrite_SpecializedRank =
                        checkedWrite_SelectedRank == -2
                            || checkedWrite_Rank == checkedWrite_SelectedRank
                            || checkedWrite_SelectedRank == -3
                                && (checkedWrite_Rank == -1 || checkedWrite_Rank == 1)
                            || checkedWrite_SelectedRank == 0 && checkedWrite_Rank > 0;
                    if (!checkedWrite_SpecializedRank
                        || checkedWrite_Rank < -3
                        || checkedWrite_Bounds != null
                            && checkedWrite_Bounds.length != 0
                            && (checkedWrite_Rank <= 0
                                || checkedWrite_Bounds.length != checkedWrite_Rank)) {
                      throw new org.eclipse.milo.opcua.stack.core.UaException(
                          org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                          "Effective ValueRank or ArrayDimensions conflict with the selected"
                              + " contract");
                    }
                    if (checkedWrite_SelectedDimensions.length != 0) {
                      if (checkedWrite_SelectedRank <= 0
                          || checkedWrite_SelectedDimensions.length != checkedWrite_SelectedRank) {
                        throw new org.eclipse.milo.opcua.stack.core.UaException(
                            org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                            "Invalid selected ArrayDimensions");
                      }
                      for (int checkedWrite_Index = 0;
                          checkedWrite_Index < checkedWrite_SelectedDimensions.length;
                          checkedWrite_Index++) {
                        long checkedWrite_Maximum =
                            checkedWrite_SelectedDimensions[checkedWrite_Index];
                        if (checkedWrite_Maximum != 0
                            && (checkedWrite_Bounds == null
                                || checkedWrite_Bounds.length
                                    != checkedWrite_SelectedDimensions.length
                                || checkedWrite_Bounds[checkedWrite_Index].longValue() == 0
                                || checkedWrite_Bounds[checkedWrite_Index].longValue()
                                    > checkedWrite_Maximum)) {
                          throw new org.eclipse.milo.opcua.stack.core.UaException(
                              org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                              "Effective ArrayDimensions broaden the selected maximum");
                        }
                      }
                    }
                    if (checkedWrite_SelectedEnums != null
                        && (checkedWrite_Context.enumValues() == null
                            || !checkedWrite_SelectedEnums.containsAll(
                                checkedWrite_Context.enumValues()))) {
                      throw new org.eclipse.milo.opcua.stack.core.UaException(
                          org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                          "Effective Enumeration domain broadens the selected contract");
                    }
                    try {
                      if (checkedWrite_Value
                              instanceof
                              org.eclipse.milo.opcua.stack.core.types.builtin.Matrix
                                  checkedWrite_Matrix
                          && checkedWrite_Matrix.isNull()) {
                        checkedWrite_Value = null;
                      }
                      if (checkedWrite_Value != null
                          && checkedWrite_SelectedRank == 1
                          && checkedWrite_Types.getBackingClass(checkedWrite_Selected)
                              == org.eclipse.milo.opcua.stack.core.types.builtin.Variant.class) {
                        if (!(checkedWrite_Value instanceof java.lang.Object[])
                            || org.eclipse.milo.opcua.stack.core.util.ArrayUtil.getValueRank(
                                    checkedWrite_Value)
                                != 1)
                          throw new org.eclipse.milo.opcua.stack.core.UaException(
                              org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                              "Selected BaseDataType array requires Java payload values");
                        for (java.lang.Object checkedWrite_Payload :
                            (java.lang.Object[]) checkedWrite_Value)
                          org.eclipse.milo.opcua.stack.core.types.builtin.Variant.of(
                              checkedWrite_Payload);
                        java.lang.Class<?> checkedWrite_EffectiveBacking =
                            checkedWrite_Types.getBackingClass(checkedWrite_Actual);
                        boolean checkedWrite_SpecializedPayloads =
                            checkedWrite_EffectiveBacking
                                    != org.eclipse.milo.opcua.stack.core.types.builtin.Variant.class
                                && !checkedWrite_Actual.equals(
                                    org.eclipse.milo.opcua.stack.core.NodeIds.Structure)
                                && !checkedWrite_Types.isStructType(checkedWrite_Actual)
                                && !checkedWrite_Actual.equals(
                                    org.eclipse.milo.opcua.stack.core.NodeIds.Enumeration)
                                && !checkedWrite_Types.isSubtypeOf(
                                    checkedWrite_Actual,
                                    org.eclipse.milo.opcua.stack.core.NodeIds.Enumeration);
                        if (checkedWrite_SpecializedPayloads
                            && checkedWrite_Value.getClass().getComponentType()
                                == java.lang.Object.class) {
                          java.lang.Object[] checkedWrite_Payloads =
                              (java.lang.Object[]) checkedWrite_Value;
                          java.lang.Object checkedWrite_Projected =
                              java.lang.reflect.Array.newInstance(
                                  checkedWrite_EffectiveBacking, checkedWrite_Payloads.length);
                          for (int checkedWrite_Index = 0;
                              checkedWrite_Index < checkedWrite_Payloads.length;
                              checkedWrite_Index++) {
                            java.lang.Object checkedWrite_Payload =
                                checkedWrite_Payloads[checkedWrite_Index];
                            if (checkedWrite_Payload
                                instanceof
                                org.eclipse.milo.opcua.stack.core.types.builtin.OptionSetUInteger<?>
                                    checkedWrite_Option)
                              checkedWrite_Payload = checkedWrite_Option.getValue();
                            java.lang.reflect.Array.set(
                                checkedWrite_Projected, checkedWrite_Index, checkedWrite_Payload);
                          }
                          checkedWrite_Value = checkedWrite_Projected;
                        }
                      }
                      Object numericElements =
                          checkedWrite_Value instanceof Matrix
                              ? ((Matrix) checkedWrite_Value).getElements()
                              : checkedWrite_Value;
                      if (numericElements != null
                          && numericElements.getClass().isArray()
                          && (numericElements.getClass().getComponentType() == Number.class
                              || numericElements.getClass().getComponentType() == UNumber.class)
                          && (checkedWrite_Actual.equals(NodeIds.Number)
                              || checkedWrite_Types.isSubtypeOf(
                                  checkedWrite_Actual, NodeIds.Number))) {
                        Class<?> numericElementType = null;
                        for (int numericIndex = 0;
                            numericIndex < Array.getLength(numericElements);
                            numericIndex++) {
                          Object numericElement = Array.get(numericElements, numericIndex);
                          if (numericElement != null) {
                            if (numericElementType != null
                                && numericElementType != numericElement.getClass()) {
                              throw new UaException(
                                  StatusCodes.Bad_TypeMismatch,
                                  "An abstract numeric array requires one homogeneous wire element"
                                      + " type");
                            }
                            numericElementType = numericElement.getClass();
                          }
                        }
                        if (numericElementType == null) {
                          numericElementType =
                              checkedWrite_Types.getBackingClass(checkedWrite_Actual);
                        }
                        if (numericElementType == Number.class
                            || numericElementType == UNumber.class) {
                          throw new UaException(
                              StatusCodes.Bad_TypeMismatch,
                              "An empty or all-null abstract numeric array requires a concretely"
                                  + " typed array");
                        }
                        Object numericArray =
                            Array.newInstance(numericElementType, Array.getLength(numericElements));
                        for (int numericIndex = 0;
                            numericIndex < Array.getLength(numericElements);
                            numericIndex++) {
                          Array.set(
                              numericArray, numericIndex, Array.get(numericElements, numericIndex));
                        }
                        if (checkedWrite_Value instanceof Matrix) {
                          checkedWrite_Value =
                              new Matrix(
                                  numericArray,
                                  ((Matrix) checkedWrite_Value).getDimensions().clone(),
                                  ((Matrix) checkedWrite_Value)
                                      .getDataType()
                                      .orElseThrow(
                                          () ->
                                              new UaException(
                                                  StatusCodes.Bad_TypeMismatch,
                                                  "A numeric Matrix requires an explicit wire"
                                                      + " DataType")),
                                  ((Matrix) checkedWrite_Value).getDataTypeId().orElse(null));
                        } else {
                          checkedWrite_Value = numericArray;
                        }
                      }

                      if (checkedWrite_Value != null) {
                        java.lang.Object checkedWrite_Elements =
                            checkedWrite_Value
                                    instanceof
                                    org.eclipse.milo.opcua.stack.core.types.builtin.Matrix
                                        checkedWrite_Matrix
                                ? checkedWrite_Matrix.getElements()
                                : checkedWrite_Value;
                        int checkedWrite_ValueRank =
                            checkedWrite_Value
                                    instanceof
                                    org.eclipse.milo.opcua.stack.core.types.builtin.Matrix
                                        checkedWrite_Matrix
                                ? checkedWrite_Matrix.getValueRank()
                                : org.eclipse.milo.opcua.stack.core.util.ArrayUtil.getValueRank(
                                    checkedWrite_Value);
                        boolean checkedWrite_Empty =
                            checkedWrite_Value.getClass().isArray()
                                && org.eclipse.milo.opcua.stack.core.util.ArrayUtil.getValueRank(
                                        checkedWrite_Value)
                                    == 1
                                && java.lang.reflect.Array.getLength(checkedWrite_Value) == 0;
                        boolean checkedWrite_Shape =
                            checkedWrite_Rank == -2
                                || checkedWrite_Rank == -3
                                    && (checkedWrite_ValueRank == -1 || checkedWrite_ValueRank == 1)
                                || checkedWrite_Rank == -1 && checkedWrite_ValueRank == -1
                                || checkedWrite_Rank == 0 && checkedWrite_ValueRank >= 1
                                || checkedWrite_Rank > 0
                                    && (checkedWrite_ValueRank == checkedWrite_Rank
                                        || checkedWrite_Empty);
                        if (!checkedWrite_Shape)
                          throw new org.eclipse.milo.opcua.stack.core.UaException(
                              org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                              "ValueRank mismatch");
                        if (checkedWrite_Value
                            instanceof
                            org.eclipse.milo.opcua.stack.core.types.builtin.Matrix
                                checkedWrite_Matrix) {
                          int[] checkedWrite_Dimensions = checkedWrite_Matrix.getDimensions();
                          if (checkedWrite_Dimensions.length < 2
                              || checkedWrite_Elements == null
                              || !checkedWrite_Elements.getClass().isArray()
                              || org.eclipse.milo.opcua.stack.core.util.ArrayUtil.getValueRank(
                                      checkedWrite_Elements)
                                  != 1) {
                            throw new org.eclipse.milo.opcua.stack.core.UaException(
                                org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                                "Malformed Matrix representation");
                          }
                          long checkedWrite_Count = 1;
                          for (int checkedWrite_Dimension : checkedWrite_Dimensions) {
                            if (checkedWrite_Dimension < 0
                                || checkedWrite_Count > java.lang.Integer.MAX_VALUE)
                              throw new org.eclipse.milo.opcua.stack.core.UaException(
                                  org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                                  "Malformed Matrix dimensions");
                            checkedWrite_Count *= checkedWrite_Dimension;
                          }
                          if (checkedWrite_Count
                                  != java.lang.reflect.Array.getLength(checkedWrite_Elements)
                              || !checkedWrite_Matrix
                                  .getDataType()
                                  .equals(
                                      org.eclipse.milo.opcua.stack.core.types.builtin.Variant.of(
                                              checkedWrite_Elements)
                                          .getDataType())) {
                            throw new org.eclipse.milo.opcua.stack.core.UaException(
                                org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                                "Matrix dimensions or DataType do not match elements");
                          }
                        }
                        if (!checkedWrite_Empty
                            && checkedWrite_Bounds != null
                            && checkedWrite_Bounds.length != 0) {
                          int[] checkedWrite_Dimensions =
                              checkedWrite_Value
                                      instanceof
                                      org.eclipse.milo.opcua.stack.core.types.builtin.Matrix
                                          checkedWrite_Matrix
                                  ? checkedWrite_Matrix.getDimensions()
                                  : org.eclipse.milo.opcua.stack.core.util.ArrayUtil.getDimensions(
                                      checkedWrite_Value);
                          if (checkedWrite_Dimensions.length != checkedWrite_Bounds.length)
                            throw new org.eclipse.milo.opcua.stack.core.UaException(
                                org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                                "ArrayDimensions mismatch");
                          for (int checkedWrite_Index = 0;
                              checkedWrite_Index < checkedWrite_Dimensions.length;
                              checkedWrite_Index++) {
                            if (checkedWrite_Bounds[checkedWrite_Index].longValue() != 0
                                && checkedWrite_Dimensions[checkedWrite_Index]
                                    > checkedWrite_Bounds[checkedWrite_Index].longValue())
                              throw new org.eclipse.milo.opcua.stack.core.UaException(
                                  org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                                  "Value exceeds ArrayDimensions maximum");
                          }
                        }
                        boolean checkedWrite_Array = checkedWrite_Elements.getClass().isArray();
                        int checkedWrite_Length =
                            checkedWrite_Array
                                ? java.lang.reflect.Array.getLength(checkedWrite_Elements)
                                : 1;
                        boolean checkedWrite_Structure =
                            checkedWrite_Actual.equals(
                                    org.eclipse.milo.opcua.stack.core.NodeIds.Structure)
                                || checkedWrite_Types.isStructType(checkedWrite_Actual);
                        boolean checkedWrite_Enumeration =
                            checkedWrite_Actual.equals(
                                    org.eclipse.milo.opcua.stack.core.NodeIds.Enumeration)
                                || checkedWrite_Types.isSubtypeOf(
                                    checkedWrite_Actual,
                                    org.eclipse.milo.opcua.stack.core.NodeIds.Enumeration);
                        boolean checkedWrite_Payloads =
                            checkedWrite_Types.getBackingClass(checkedWrite_Actual)
                                == org.eclipse.milo.opcua.stack.core.types.builtin.Variant.class;
                        boolean checkedWrite_PayloadArray =
                            checkedWrite_Payloads
                                && checkedWrite_SelectedRank == 1
                                && checkedWrite_Types.getBackingClass(checkedWrite_Selected)
                                    == org.eclipse.milo.opcua.stack.core.types.builtin.Variant
                                        .class;
                        java.lang.Object checkedWrite_Converted = checkedWrite_Elements;
                        if (checkedWrite_Structure) {
                          var checkedWrite_Codec =
                              checkedWrite_Context
                                  .encodingContext()
                                  .getDataTypeManager()
                                  .getCodec(checkedWrite_Actual);
                          java.lang.Class<?> checkedWrite_Class =
                              checkedWrite_Codec == null
                                  ? org.eclipse.milo.opcua.stack.core.types.UaStructuredType.class
                                  : checkedWrite_Codec.getType();
                          if (checkedWrite_Array)
                            checkedWrite_Converted =
                                java.lang.reflect.Array.newInstance(
                                    checkedWrite_Class, checkedWrite_Length);
                          for (int checkedWrite_Index = 0;
                              checkedWrite_Index < checkedWrite_Length;
                              checkedWrite_Index++) {
                            java.lang.Object checkedWrite_Element =
                                checkedWrite_Array
                                    ? java.lang.reflect.Array.get(
                                        checkedWrite_Elements, checkedWrite_Index)
                                    : checkedWrite_Elements;
                            if (checkedWrite_Element
                                instanceof
                                org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject
                                    checkedWrite_Object) {
                              checkedWrite_Element =
                                  checkedWrite_Object.isNull()
                                      ? null
                                      : checkedWrite_Object.decode(
                                          checkedWrite_Context.encodingContext());
                            }
                            if (checkedWrite_Element != null) {
                              if (!(checkedWrite_Element
                                  instanceof
                                  org.eclipse.milo.opcua.stack.core.types.UaStructuredType))
                                throw new org.eclipse.milo.opcua.stack.core.UaException(
                                    org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                                    "Structure value required");
                              var checkedWrite_TypeId =
                                  ((org.eclipse.milo.opcua.stack.core.types.UaStructuredType)
                                          checkedWrite_Element)
                                      .getTypeId()
                                      .toNodeId(checkedWrite_Context.namespaceTable())
                                      .orElse(
                                          org.eclipse.milo.opcua.stack.core.types.builtin.NodeId
                                              .NULL_VALUE);
                              boolean checkedWrite_Abstract =
                                  checkedWrite_Actual.equals(
                                          org.eclipse.milo.opcua.stack.core.NodeIds.Structure)
                                      || java.lang.Boolean.TRUE.equals(
                                          checkedWrite_Types
                                              .getType(checkedWrite_Actual)
                                              .isAbstract());
                              if (!(checkedWrite_Abstract
                                  ? checkedWrite_Types.isSubtypeOf(
                                      checkedWrite_TypeId, checkedWrite_Actual)
                                  : checkedWrite_Actual.equals(checkedWrite_TypeId)))
                                throw new org.eclipse.milo.opcua.stack.core.UaException(
                                    org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                                    "Structure identity does not match the effective DataType");
                            }
                            if (checkedWrite_Array)
                              java.lang.reflect.Array.set(
                                  checkedWrite_Converted, checkedWrite_Index, checkedWrite_Element);
                            else checkedWrite_Converted = checkedWrite_Element;
                          }
                        } else if (checkedWrite_Enumeration) {
                          if (checkedWrite_Array)
                            checkedWrite_Converted = new java.lang.Integer[checkedWrite_Length];
                          for (int checkedWrite_Index = 0;
                              checkedWrite_Index < checkedWrite_Length;
                              checkedWrite_Index++) {
                            java.lang.Object checkedWrite_Element =
                                checkedWrite_Array
                                    ? java.lang.reflect.Array.get(
                                        checkedWrite_Elements, checkedWrite_Index)
                                    : checkedWrite_Elements;
                            if (checkedWrite_Element
                                instanceof
                                org.eclipse.milo.opcua.stack.core.types.UaEnumeratedType
                                    checkedWrite_Enum)
                              checkedWrite_Element = checkedWrite_Enum.getValue();
                            if (checkedWrite_Element != null
                                && !(checkedWrite_Element instanceof java.lang.Integer))
                              throw new org.eclipse.milo.opcua.stack.core.UaException(
                                  org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                                  "Enumeration requires an Int32 value");
                            if (checkedWrite_Element != null
                                && (checkedWrite_Context.enumValues() != null
                                        && !checkedWrite_Context
                                            .enumValues()
                                            .contains(checkedWrite_Element)
                                    || checkedWrite_SelectedEnums != null
                                        && !checkedWrite_SelectedEnums.contains(
                                            checkedWrite_Element)))
                              throw new org.eclipse.milo.opcua.stack.core.UaException(
                                  org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_OutOfRange,
                                  "Unknown Enumeration value: " + checkedWrite_Element);
                            if (checkedWrite_Wire
                                && checkedWrite_Array
                                && checkedWrite_Element == null)
                              throw new org.eclipse.milo.opcua.stack.core.UaException(
                                  org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                                  "Enumeration wire arrays cannot contain null elements");
                            if (checkedWrite_Array)
                              java.lang.reflect.Array.set(
                                  checkedWrite_Converted, checkedWrite_Index, checkedWrite_Element);
                            else checkedWrite_Converted = checkedWrite_Element;
                          }
                        } else if (checkedWrite_PayloadArray) {
                          if (!checkedWrite_Array
                              || org.eclipse.milo.opcua.stack.core.util.ArrayUtil.getValueRank(
                                      checkedWrite_Elements)
                                  != 1)
                            throw new org.eclipse.milo.opcua.stack.core.UaException(
                                org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                                "BaseDataType array requires Java payload values");
                          checkedWrite_Converted =
                              new org.eclipse.milo.opcua.stack.core.types.builtin.Variant
                                  [checkedWrite_Length];
                          for (int checkedWrite_Index = 0;
                              checkedWrite_Index < checkedWrite_Length;
                              checkedWrite_Index++) {
                            java.lang.Object checkedWrite_Element =
                                java.lang.reflect.Array.get(
                                    checkedWrite_Elements, checkedWrite_Index);
                            if (checkedWrite_Wire)
                              checkedWrite_Element =
                                  org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject
                                      .encodeValue(
                                          checkedWrite_Context.encodingContext(),
                                          checkedWrite_Element);
                            ((org.eclipse.milo.opcua.stack.core.types.builtin.Variant[])
                                        checkedWrite_Converted)
                                    [checkedWrite_Index] =
                                org.eclipse.milo.opcua.stack.core.types.builtin.Variant.of(
                                    checkedWrite_Element);
                          }
                        } else {
                          java.lang.Object checkedWrite_Check = checkedWrite_Elements;
                          java.lang.Class<?> checkedWrite_ElementsClass =
                              org.eclipse.milo.opcua.stack.core.util.ArrayUtil.getBoxedType(
                                  checkedWrite_Elements);
                          boolean checkedWrite_Options =
                              org.eclipse.milo.opcua.stack.core.types.builtin.OptionSetUInteger
                                  .class
                                  .isAssignableFrom(checkedWrite_ElementsClass);
                          if (checkedWrite_Options) {
                            java.lang.Class<?> checkedWrite_Backing =
                                org.eclipse.milo.opcua.stack.core.types.builtin.Variant.of(
                                        checkedWrite_Elements)
                                    .getDataType()
                                    .orElseThrow()
                                    .getBackingClass();
                            if (checkedWrite_Array)
                              checkedWrite_Converted =
                                  java.lang.reflect.Array.newInstance(
                                      checkedWrite_Backing, checkedWrite_Length);
                            for (int checkedWrite_Index = 0;
                                checkedWrite_Index < checkedWrite_Length;
                                checkedWrite_Index++) {
                              java.lang.Object checkedWrite_Element =
                                  checkedWrite_Array
                                      ? java.lang.reflect.Array.get(
                                          checkedWrite_Elements, checkedWrite_Index)
                                      : checkedWrite_Elements;
                              if (checkedWrite_Element != null)
                                checkedWrite_Element =
                                    ((org.eclipse.milo.opcua.stack.core.types.builtin
                                                    .OptionSetUInteger<
                                                ?>)
                                            checkedWrite_Element)
                                        .getValue();
                              if (checkedWrite_Wire
                                  && checkedWrite_Array
                                  && checkedWrite_Element == null)
                                throw new org.eclipse.milo.opcua.stack.core.UaException(
                                    org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                                    "OptionSet wire arrays cannot contain null elements");
                              if (checkedWrite_Array)
                                java.lang.reflect.Array.set(
                                    checkedWrite_Converted,
                                    checkedWrite_Index,
                                    checkedWrite_Element);
                              else checkedWrite_Converted = checkedWrite_Element;
                            }
                            checkedWrite_Check = checkedWrite_Converted;
                          }
                          org.eclipse.milo.opcua.stack.core.types.builtin.Variant.of(
                              checkedWrite_Check);
                          var checkedWrite_Assignable =
                              checkedWrite_Types.getBackingClass(checkedWrite_Actual)
                                          == java.lang.Number.class
                                      && checkedWrite_Types.isSubtypeOf(
                                          checkedWrite_Actual,
                                          org.eclipse.milo.opcua.stack.core.NodeIds.Integer)
                                  ? org.eclipse.milo.opcua.stack.core.NodeIds.Integer
                                  : checkedWrite_Actual;
                          if (!checkedWrite_Payloads
                              && checkedWrite_Check != null
                              && !checkedWrite_Types.isAssignable(
                                  checkedWrite_Assignable,
                                  org.eclipse.milo.opcua.stack.core.util.ArrayUtil.getBoxedType(
                                      checkedWrite_Check)))
                            throw new org.eclipse.milo.opcua.stack.core.UaException(
                                org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                                "Value does not match effective DataType");
                        }
                        checkedWrite_Value =
                            checkedWrite_Value
                                    instanceof
                                    org.eclipse.milo.opcua.stack.core.types.builtin.Matrix
                                        checkedWrite_Matrix
                                ? new org.eclipse.milo.opcua.stack.core.types.builtin.Matrix(
                                    checkedWrite_Converted,
                                    checkedWrite_Matrix.getDimensions().clone(),
                                    checkedWrite_Matrix.getDataType().orElseThrow(),
                                    checkedWrite_Matrix.getDataTypeId().orElse(null))
                                : checkedWrite_Converted;
                        if (checkedWrite_Empty && checkedWrite_Rank > 1)
                          checkedWrite_Value =
                              new org.eclipse.milo.opcua.stack.core.types.builtin.Matrix(
                                  checkedWrite_Converted, new int[checkedWrite_Rank]);
                        if (checkedWrite_Wire) {
                          var numericWireValues = new ArrayDeque<Object[]>();
                          var numericWirePath =
                              Collections.newSetFromMap(new IdentityHashMap<Object, Boolean>());
                          if (checkedWrite_Value != null) {
                            numericWireValues.push(new Object[] {checkedWrite_Value, false});
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
                                      "A DataValue requires a value wrapper; use Variant.NULL_VALUE"
                                          + " for null");
                                }
                                if (((DataValue) numericWireValue).getStatusCode() == null) {
                                  throw new UaException(
                                      StatusCodes.Bad_TypeMismatch,
                                      "A DataValue requires a StatusCode; use StatusCode.GOOD for"
                                          + " Good");
                                }
                                numericWireValue = ((DataValue) numericWireValue).getValue();
                              } else {
                                numericWireValue = ((Variant) numericWireValue).getValue();
                              }
                            }
                            if (numericWireValue instanceof Matrix) {
                              numericWireValue = ((Matrix) numericWireValue).getElements();
                            }
                            if (numericWireValue != null && numericWireValue.getClass().isArray()) {
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
                                    && ArrayUtil.getBoxedType(numericWireValue) == Variant.class) {
                                  throw new UaException(
                                      StatusCodes.Bad_TypeMismatch,
                                      "A Variant wire array requires a wrapper for every element;"
                                          + " use Variant.NULL_VALUE for null");
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
                                    && ArrayUtil.getBoxedType(numericWireValue) == Boolean.class) {
                                  throw new UaException(
                                      StatusCodes.Bad_TypeMismatch,
                                      "A Boolean wire array cannot retain a null element; Milo"
                                          + " encodes it as false");
                                }
                                if (numericWireElement == null
                                    && ArrayUtil.getBoxedType(numericWireValue)
                                        == StatusCode.class) {
                                  throw new UaException(
                                      StatusCodes.Bad_TypeMismatch,
                                      "A StatusCode wire array cannot retain a null element; Milo"
                                          + " encodes it as Good");
                                }
                                if (numericWireElement == null
                                    && Number.class.isAssignableFrom(
                                        ArrayUtil.getBoxedType(numericWireValue))) {
                                  throw new UaException(
                                      StatusCodes.Bad_TypeMismatch,
                                      "A numeric wire array cannot retain a null element; Milo"
                                          + " encodes it as zero");
                                }
                                if (numericWireElement instanceof Variant
                                    || numericWireElement instanceof DataValue) {
                                  numericWireValues.push(new Object[] {numericWireElement, false});
                                }
                              }
                            }
                          }

                          checkedWrite_Value =
                              org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject
                                  .encodeValue(
                                      checkedWrite_Context.encodingContext(), checkedWrite_Value);
                        }
                      }
                      encoded =
                          org.eclipse.milo.opcua.stack.core.types.builtin.Variant.of(
                              checkedWrite_Value);
                    } catch (
                        org.eclipse.milo.opcua.stack.core.UaSerializationException
                            checkedWrite_Failure) {
                      long checkedWrite_Status =
                          checkedWrite_Failure.getStatusCode().getValue()
                                  == org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_OutOfRange
                              ? org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_OutOfRange
                              : org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch;
                      throw new org.eclipse.milo.opcua.stack.core.UaException(
                          checkedWrite_Status, checkedWrite_Failure);
                    } catch (java.lang.IllegalArgumentException
                        | java.lang.ClassCastException checkedWrite_Failure) {
                      throw new org.eclipse.milo.opcua.stack.core.UaException(
                          org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                          checkedWrite_Failure);
                    }
                  }
                  views.checkOpen();
                  return child
                      .writeAttributeAsync(AttributeId.Value, DataValue.valueOnly(encoded))
                      .thenApply(response -> response);
                } catch (Exception failure) {
                  return CompletableFuture.failedFuture(failure);
                }
              });
        });
  }

  /**
   * Writes the existing member's value remotely. A null value is valid. An absent node fails with
   * Bad_NotFound before conversion or Write. This does not create nodes or update the wrapper's
   * local value.
   *
   * <p>Concrete enum conversions reject unknown numbers with Bad_OutOfRange. Structured decoding
   * and existing rank/type checks retain their failures.
   *
   * <p>Lookup, conversion and service failures complete the future exceptionally. UaException
   * causes preserve OPC UA status. Incompatible plain payload casts can complete exceptionally with
   * ClassCastException. Cancellation does not promise transport cancellation or rollback.
   *
   * <p>The actual member DataType, rank and maximum dimensions constrain this selected view write.
   * Incompatible values fail with Bad_TypeMismatch; values outside the actual finite enumeration
   * fail with Bad_OutOfRange. Validation finishes before mutation or Write. Type and enumeration
   * discovery can perform service I/O, including for local setters.
   *
   * <p>A closed view context completes the future exceptionally with IllegalStateException.
   *
   * @param value the value to store; null is permitted
   * @return a nonnull future completing with the Write operation status, including non-Good
   *     statuses
   */
  @Override
  public CompletableFuture<StatusCode> writeMacAddressAsync(@Nullable String value) {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.writeMacAddressAsyncImplementation(value));
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }

  /**
   * Resolves the optional member by its namespace-qualified path. Returns null only for confirmed
   * absence. Resolution does not create a UA node. It can perform service I/O and construct or
   * reuse a Java wrapper in Milo's address space cache. A reference can change after lookup.
   *
   * <p>The returned object exposes the selected child contract and shares the retained raw node's
   * state through this view context.
   *
   * @return the existing member, or null for confirmed absence
   * @throws org.eclipse.milo.opcua.stack.core.UaException if a required node is absent, resolution
   *     fails, or a checked conversion fails
   * @throws IllegalStateException if the view context is closed
   */
  @Override
  public @Nullable BaseDataVariableType getInterfaceNameNode() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    return ViewFutures.await(getInterfaceNameNodeAsync());
  }

  private CompletableFuture<? extends @Nullable BaseDataVariableType>
      getInterfaceNameNodeAsyncImplementation() {
    return ViewFutures.compose(
        viewMember1Async(),
        child ->
            child == null
                ? CompletableFuture.completedFuture(null)
                : views.wrapVariableAsync(child.getNodeId(), BaseDataVariableTypeView.TYPE));
  }

  /**
   * Resolves the optional member by its namespace-qualified path. Returns null only for confirmed
   * absence. Resolution does not create a UA node. It can perform service I/O and construct or
   * reuse a Java wrapper in Milo's address space cache. A reference can change after lookup.
   *
   * <p>Lookup, conversion and service failures complete the future exceptionally. UaException
   * causes preserve OPC UA status. Incompatible plain payload casts can complete exceptionally with
   * ClassCastException. Cancellation does not promise transport cancellation or rollback.
   *
   * <p>The returned object exposes the selected child contract and shares the retained raw node's
   * state through this view context.
   *
   * <p>A closed view context completes the future exceptionally with IllegalStateException.
   *
   * @return a nonnull future completing with the existing member, or null for confirmed absence
   */
  @Override
  public CompletableFuture<? extends @Nullable BaseDataVariableType> getInterfaceNameNodeAsync() {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.getInterfaceNameNodeAsyncImplementation());
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }

  /**
   * Gets the existing member's local value without checking its quality. A null value is valid; an
   * absent node fails with Bad_NotFound. Lookup may perform service I/O; the value is not read
   * remotely. Use the node's raw DataValue to inspect quality and timestamps.
   *
   * <p>Concrete enum conversions reject unknown numbers with Bad_OutOfRange. Structured decoding
   * and existing rank/type checks retain their failures.
   *
   * @return the value, which may be null on a present member
   * @throws org.eclipse.milo.opcua.stack.core.UaException if a required node is absent, resolution
   *     fails, or a checked conversion fails
   * @throws ClassCastException if a plain payload cast encounters an incompatible Java
   *     representation
   * @throws IllegalStateException if the view context is closed
   */
  @Override
  public @Nullable String getInterfaceName() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    var child = viewMember1();
    if (child == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:InterfaceName (declaration i=24190, owner i=24188)"
              + " on "
              + getNodeId());
    }
    @Nullable String converted;
    {
      Object rawValue = child.getValue().getValue().getValue();
      if (rawValue instanceof Matrix matrix && matrix.isNull()) {
        rawValue = null;
      }
      if (rawValue != null) {
        int actualRank =
            rawValue instanceof Matrix matrix
                ? matrix.getValueRank()
                : ArrayUtil.getValueRank(rawValue);
        Object rankElements = rawValue instanceof Matrix matrix ? matrix.getElements() : rawValue;
        if (!(actualRank == -1)) {
          throw new UaException(
              StatusCodes.Bad_TypeMismatch,
              "http://opcfoundation.org/UA/:InterfaceName (declaration i=24190, owner i=24188)");
        }
        if (rawValue instanceof Matrix matrix) {
          if (actualRank < 2
              || rankElements == null
              || !rankElements.getClass().isArray()
              || ArrayUtil.getValueRank(rankElements) != 1) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "http://opcfoundation.org/UA/:InterfaceName (declaration i=24190, owner i=24188)");
          }
          long elementCount = 1;
          for (int dimension : matrix.getDimensions()) {
            if (dimension < 0 || elementCount > Integer.MAX_VALUE) {
              throw new UaException(
                  StatusCodes.Bad_TypeMismatch,
                  "http://opcfoundation.org/UA/:InterfaceName (declaration i=24190, owner"
                      + " i=24188)");
            }
            elementCount *= dimension;
          }
          if (elementCount != Array.getLength(rankElements)) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "http://opcfoundation.org/UA/:InterfaceName (declaration i=24190, owner i=24188)");
          }
        } else if (actualRank > 1) {
          throw new UaException(
              StatusCodes.Bad_TypeMismatch,
              "http://opcfoundation.org/UA/:InterfaceName (declaration i=24190, owner i=24188): use"
                  + " Matrix for multiple dimensions");
        }
      }
      Object element = rawValue;
      if (element != null && !(element instanceof String)) {
        throw new UaException(
            StatusCodes.Bad_TypeMismatch,
            "http://opcfoundation.org/UA/:InterfaceName (declaration i=24190, owner i=24188)");
      }
      converted = (String) element;
    }
    return converted;
  }

  /**
   * Sets the existing member's local value. A null value is valid. An absent node fails with
   * Bad_NotFound before conversion or mutation. This does not create nodes or silently skip writes.
   * This does not send a Write service request.
   *
   * <p>Concrete enum conversions reject unknown numbers with Bad_OutOfRange. Structured decoding
   * and existing rank/type checks retain their failures.
   *
   * <p>The actual member DataType, rank and maximum dimensions constrain this selected view write.
   * Incompatible values fail with Bad_TypeMismatch; values outside the actual finite enumeration
   * fail with Bad_OutOfRange. Validation finishes before mutation or Write. Type and enumeration
   * discovery can perform service I/O, including for local setters.
   *
   * @param value the value to store; null is permitted
   * @throws org.eclipse.milo.opcua.stack.core.UaException if a required node is absent, resolution
   *     fails, or a checked conversion fails
   * @throws IllegalStateException if the view context is closed
   */
  @Override
  public void setInterfaceName(@Nullable String value) throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    var child = viewMember1();
    if (child == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:InterfaceName (declaration i=24190, owner i=24188)"
              + " on "
              + getNodeId());
    }
    var writeContext = views.localWriteContext(child.getNodeId(), value, KNOWN_ENUMS);
    Variant encoded;
    {
      var checkedWrite_Context = writeContext;
      Object checkedWrite_Value = value;
      var checkedWrite_Selected =
          ExpandedNodeId.parse("i=12")
              .toNodeId(checkedWrite_Context.namespaceTable())
              .orElseThrow(
                  () ->
                      new org.eclipse.milo.opcua.stack.core.UaException(
                          org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_NodeIdInvalid,
                          "Unknown selected DataType namespace:"
                              + " ExpandedNodeId[server=ServerIndex[serverIndex=0],"
                              + " namespace=NamespaceUri[namespaceUri=http://opcfoundation.org/UA/],"
                              + " identifier=12]"));
      int checkedWrite_SelectedRank = -1;
      long[] checkedWrite_SelectedDimensions = new long[] {};
      Set<Integer> checkedWrite_SelectedEnums = null;
      boolean checkedWrite_Wire = false;
      var checkedWrite_Types = checkedWrite_Context.dataTypes();
      var checkedWrite_Actual = checkedWrite_Context.dataType();
      int checkedWrite_Rank = checkedWrite_Context.valueRank();
      var checkedWrite_Bounds = checkedWrite_Context.arrayDimensions();
      if (!checkedWrite_Types.containsType(checkedWrite_Selected)
          || !checkedWrite_Types.containsType(checkedWrite_Actual)
          || !(checkedWrite_Actual.equals(checkedWrite_Selected)
              || checkedWrite_Types.isSubtypeOf(checkedWrite_Actual, checkedWrite_Selected))) {
        throw new org.eclipse.milo.opcua.stack.core.UaException(
            org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
            "Effective DataType "
                + checkedWrite_Actual
                + " does not specialize selected "
                + checkedWrite_Selected);
      }
      boolean checkedWrite_SpecializedRank =
          checkedWrite_SelectedRank == -2
              || checkedWrite_Rank == checkedWrite_SelectedRank
              || checkedWrite_SelectedRank == -3
                  && (checkedWrite_Rank == -1 || checkedWrite_Rank == 1)
              || checkedWrite_SelectedRank == 0 && checkedWrite_Rank > 0;
      if (!checkedWrite_SpecializedRank
          || checkedWrite_Rank < -3
          || checkedWrite_Bounds != null
              && checkedWrite_Bounds.length != 0
              && (checkedWrite_Rank <= 0 || checkedWrite_Bounds.length != checkedWrite_Rank)) {
        throw new org.eclipse.milo.opcua.stack.core.UaException(
            org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
            "Effective ValueRank or ArrayDimensions conflict with the selected contract");
      }
      if (checkedWrite_SelectedDimensions.length != 0) {
        if (checkedWrite_SelectedRank <= 0
            || checkedWrite_SelectedDimensions.length != checkedWrite_SelectedRank) {
          throw new org.eclipse.milo.opcua.stack.core.UaException(
              org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
              "Invalid selected ArrayDimensions");
        }
        for (int checkedWrite_Index = 0;
            checkedWrite_Index < checkedWrite_SelectedDimensions.length;
            checkedWrite_Index++) {
          long checkedWrite_Maximum = checkedWrite_SelectedDimensions[checkedWrite_Index];
          if (checkedWrite_Maximum != 0
              && (checkedWrite_Bounds == null
                  || checkedWrite_Bounds.length != checkedWrite_SelectedDimensions.length
                  || checkedWrite_Bounds[checkedWrite_Index].longValue() == 0
                  || checkedWrite_Bounds[checkedWrite_Index].longValue() > checkedWrite_Maximum)) {
            throw new org.eclipse.milo.opcua.stack.core.UaException(
                org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                "Effective ArrayDimensions broaden the selected maximum");
          }
        }
      }
      if (checkedWrite_SelectedEnums != null
          && (checkedWrite_Context.enumValues() == null
              || !checkedWrite_SelectedEnums.containsAll(checkedWrite_Context.enumValues()))) {
        throw new org.eclipse.milo.opcua.stack.core.UaException(
            org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
            "Effective Enumeration domain broadens the selected contract");
      }
      try {
        if (checkedWrite_Value
                instanceof
                org.eclipse.milo.opcua.stack.core.types.builtin.Matrix checkedWrite_Matrix
            && checkedWrite_Matrix.isNull()) {
          checkedWrite_Value = null;
        }
        if (checkedWrite_Value != null
            && checkedWrite_SelectedRank == 1
            && checkedWrite_Types.getBackingClass(checkedWrite_Selected)
                == org.eclipse.milo.opcua.stack.core.types.builtin.Variant.class) {
          if (!(checkedWrite_Value instanceof java.lang.Object[])
              || org.eclipse.milo.opcua.stack.core.util.ArrayUtil.getValueRank(checkedWrite_Value)
                  != 1)
            throw new org.eclipse.milo.opcua.stack.core.UaException(
                org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                "Selected BaseDataType array requires Java payload values");
          for (java.lang.Object checkedWrite_Payload : (java.lang.Object[]) checkedWrite_Value)
            org.eclipse.milo.opcua.stack.core.types.builtin.Variant.of(checkedWrite_Payload);
          java.lang.Class<?> checkedWrite_EffectiveBacking =
              checkedWrite_Types.getBackingClass(checkedWrite_Actual);
          boolean checkedWrite_SpecializedPayloads =
              checkedWrite_EffectiveBacking
                      != org.eclipse.milo.opcua.stack.core.types.builtin.Variant.class
                  && !checkedWrite_Actual.equals(
                      org.eclipse.milo.opcua.stack.core.NodeIds.Structure)
                  && !checkedWrite_Types.isStructType(checkedWrite_Actual)
                  && !checkedWrite_Actual.equals(
                      org.eclipse.milo.opcua.stack.core.NodeIds.Enumeration)
                  && !checkedWrite_Types.isSubtypeOf(
                      checkedWrite_Actual, org.eclipse.milo.opcua.stack.core.NodeIds.Enumeration);
          if (checkedWrite_SpecializedPayloads
              && checkedWrite_Value.getClass().getComponentType() == java.lang.Object.class) {
            java.lang.Object[] checkedWrite_Payloads = (java.lang.Object[]) checkedWrite_Value;
            java.lang.Object checkedWrite_Projected =
                java.lang.reflect.Array.newInstance(
                    checkedWrite_EffectiveBacking, checkedWrite_Payloads.length);
            for (int checkedWrite_Index = 0;
                checkedWrite_Index < checkedWrite_Payloads.length;
                checkedWrite_Index++) {
              java.lang.Object checkedWrite_Payload = checkedWrite_Payloads[checkedWrite_Index];
              if (checkedWrite_Payload
                  instanceof
                  org.eclipse.milo.opcua.stack.core.types.builtin.OptionSetUInteger<?>
                      checkedWrite_Option) checkedWrite_Payload = checkedWrite_Option.getValue();
              java.lang.reflect.Array.set(
                  checkedWrite_Projected, checkedWrite_Index, checkedWrite_Payload);
            }
            checkedWrite_Value = checkedWrite_Projected;
          }
        }
        Object numericElements =
            checkedWrite_Value instanceof Matrix
                ? ((Matrix) checkedWrite_Value).getElements()
                : checkedWrite_Value;
        if (numericElements != null
            && numericElements.getClass().isArray()
            && (numericElements.getClass().getComponentType() == Number.class
                || numericElements.getClass().getComponentType() == UNumber.class)
            && (checkedWrite_Actual.equals(NodeIds.Number)
                || checkedWrite_Types.isSubtypeOf(checkedWrite_Actual, NodeIds.Number))) {
          Class<?> numericElementType = null;
          for (int numericIndex = 0;
              numericIndex < Array.getLength(numericElements);
              numericIndex++) {
            Object numericElement = Array.get(numericElements, numericIndex);
            if (numericElement != null) {
              if (numericElementType != null && numericElementType != numericElement.getClass()) {
                throw new UaException(
                    StatusCodes.Bad_TypeMismatch,
                    "An abstract numeric array requires one homogeneous wire element type");
              }
              numericElementType = numericElement.getClass();
            }
          }
          if (numericElementType == null) {
            numericElementType = checkedWrite_Types.getBackingClass(checkedWrite_Actual);
          }
          if (numericElementType == Number.class || numericElementType == UNumber.class) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "An empty or all-null abstract numeric array requires a concretely typed array");
          }
          Object numericArray =
              Array.newInstance(numericElementType, Array.getLength(numericElements));
          for (int numericIndex = 0;
              numericIndex < Array.getLength(numericElements);
              numericIndex++) {
            Array.set(numericArray, numericIndex, Array.get(numericElements, numericIndex));
          }
          if (checkedWrite_Value instanceof Matrix) {
            checkedWrite_Value =
                new Matrix(
                    numericArray,
                    ((Matrix) checkedWrite_Value).getDimensions().clone(),
                    ((Matrix) checkedWrite_Value)
                        .getDataType()
                        .orElseThrow(
                            () ->
                                new UaException(
                                    StatusCodes.Bad_TypeMismatch,
                                    "A numeric Matrix requires an explicit wire DataType")),
                    ((Matrix) checkedWrite_Value).getDataTypeId().orElse(null));
          } else {
            checkedWrite_Value = numericArray;
          }
        }

        if (checkedWrite_Value != null) {
          java.lang.Object checkedWrite_Elements =
              checkedWrite_Value
                      instanceof
                      org.eclipse.milo.opcua.stack.core.types.builtin.Matrix checkedWrite_Matrix
                  ? checkedWrite_Matrix.getElements()
                  : checkedWrite_Value;
          int checkedWrite_ValueRank =
              checkedWrite_Value
                      instanceof
                      org.eclipse.milo.opcua.stack.core.types.builtin.Matrix checkedWrite_Matrix
                  ? checkedWrite_Matrix.getValueRank()
                  : org.eclipse.milo.opcua.stack.core.util.ArrayUtil.getValueRank(
                      checkedWrite_Value);
          boolean checkedWrite_Empty =
              checkedWrite_Value.getClass().isArray()
                  && org.eclipse.milo.opcua.stack.core.util.ArrayUtil.getValueRank(
                          checkedWrite_Value)
                      == 1
                  && java.lang.reflect.Array.getLength(checkedWrite_Value) == 0;
          boolean checkedWrite_Shape =
              checkedWrite_Rank == -2
                  || checkedWrite_Rank == -3
                      && (checkedWrite_ValueRank == -1 || checkedWrite_ValueRank == 1)
                  || checkedWrite_Rank == -1 && checkedWrite_ValueRank == -1
                  || checkedWrite_Rank == 0 && checkedWrite_ValueRank >= 1
                  || checkedWrite_Rank > 0
                      && (checkedWrite_ValueRank == checkedWrite_Rank || checkedWrite_Empty);
          if (!checkedWrite_Shape)
            throw new org.eclipse.milo.opcua.stack.core.UaException(
                org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                "ValueRank mismatch");
          if (checkedWrite_Value
              instanceof
              org.eclipse.milo.opcua.stack.core.types.builtin.Matrix checkedWrite_Matrix) {
            int[] checkedWrite_Dimensions = checkedWrite_Matrix.getDimensions();
            if (checkedWrite_Dimensions.length < 2
                || checkedWrite_Elements == null
                || !checkedWrite_Elements.getClass().isArray()
                || org.eclipse.milo.opcua.stack.core.util.ArrayUtil.getValueRank(
                        checkedWrite_Elements)
                    != 1) {
              throw new org.eclipse.milo.opcua.stack.core.UaException(
                  org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                  "Malformed Matrix representation");
            }
            long checkedWrite_Count = 1;
            for (int checkedWrite_Dimension : checkedWrite_Dimensions) {
              if (checkedWrite_Dimension < 0 || checkedWrite_Count > java.lang.Integer.MAX_VALUE)
                throw new org.eclipse.milo.opcua.stack.core.UaException(
                    org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                    "Malformed Matrix dimensions");
              checkedWrite_Count *= checkedWrite_Dimension;
            }
            if (checkedWrite_Count != java.lang.reflect.Array.getLength(checkedWrite_Elements)
                || !checkedWrite_Matrix
                    .getDataType()
                    .equals(
                        org.eclipse.milo.opcua.stack.core.types.builtin.Variant.of(
                                checkedWrite_Elements)
                            .getDataType())) {
              throw new org.eclipse.milo.opcua.stack.core.UaException(
                  org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                  "Matrix dimensions or DataType do not match elements");
            }
          }
          if (!checkedWrite_Empty
              && checkedWrite_Bounds != null
              && checkedWrite_Bounds.length != 0) {
            int[] checkedWrite_Dimensions =
                checkedWrite_Value
                        instanceof
                        org.eclipse.milo.opcua.stack.core.types.builtin.Matrix checkedWrite_Matrix
                    ? checkedWrite_Matrix.getDimensions()
                    : org.eclipse.milo.opcua.stack.core.util.ArrayUtil.getDimensions(
                        checkedWrite_Value);
            if (checkedWrite_Dimensions.length != checkedWrite_Bounds.length)
              throw new org.eclipse.milo.opcua.stack.core.UaException(
                  org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                  "ArrayDimensions mismatch");
            for (int checkedWrite_Index = 0;
                checkedWrite_Index < checkedWrite_Dimensions.length;
                checkedWrite_Index++) {
              if (checkedWrite_Bounds[checkedWrite_Index].longValue() != 0
                  && checkedWrite_Dimensions[checkedWrite_Index]
                      > checkedWrite_Bounds[checkedWrite_Index].longValue())
                throw new org.eclipse.milo.opcua.stack.core.UaException(
                    org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                    "Value exceeds ArrayDimensions maximum");
            }
          }
          boolean checkedWrite_Array = checkedWrite_Elements.getClass().isArray();
          int checkedWrite_Length =
              checkedWrite_Array ? java.lang.reflect.Array.getLength(checkedWrite_Elements) : 1;
          boolean checkedWrite_Structure =
              checkedWrite_Actual.equals(org.eclipse.milo.opcua.stack.core.NodeIds.Structure)
                  || checkedWrite_Types.isStructType(checkedWrite_Actual);
          boolean checkedWrite_Enumeration =
              checkedWrite_Actual.equals(org.eclipse.milo.opcua.stack.core.NodeIds.Enumeration)
                  || checkedWrite_Types.isSubtypeOf(
                      checkedWrite_Actual, org.eclipse.milo.opcua.stack.core.NodeIds.Enumeration);
          boolean checkedWrite_Payloads =
              checkedWrite_Types.getBackingClass(checkedWrite_Actual)
                  == org.eclipse.milo.opcua.stack.core.types.builtin.Variant.class;
          boolean checkedWrite_PayloadArray =
              checkedWrite_Payloads
                  && checkedWrite_SelectedRank == 1
                  && checkedWrite_Types.getBackingClass(checkedWrite_Selected)
                      == org.eclipse.milo.opcua.stack.core.types.builtin.Variant.class;
          java.lang.Object checkedWrite_Converted = checkedWrite_Elements;
          if (checkedWrite_Structure) {
            var checkedWrite_Codec =
                checkedWrite_Context
                    .encodingContext()
                    .getDataTypeManager()
                    .getCodec(checkedWrite_Actual);
            java.lang.Class<?> checkedWrite_Class =
                checkedWrite_Codec == null
                    ? org.eclipse.milo.opcua.stack.core.types.UaStructuredType.class
                    : checkedWrite_Codec.getType();
            if (checkedWrite_Array)
              checkedWrite_Converted =
                  java.lang.reflect.Array.newInstance(checkedWrite_Class, checkedWrite_Length);
            for (int checkedWrite_Index = 0;
                checkedWrite_Index < checkedWrite_Length;
                checkedWrite_Index++) {
              java.lang.Object checkedWrite_Element =
                  checkedWrite_Array
                      ? java.lang.reflect.Array.get(checkedWrite_Elements, checkedWrite_Index)
                      : checkedWrite_Elements;
              if (checkedWrite_Element
                  instanceof
                  org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject
                      checkedWrite_Object) {
                checkedWrite_Element =
                    checkedWrite_Object.isNull()
                        ? null
                        : checkedWrite_Object.decode(checkedWrite_Context.encodingContext());
              }
              if (checkedWrite_Element != null) {
                if (!(checkedWrite_Element
                    instanceof org.eclipse.milo.opcua.stack.core.types.UaStructuredType))
                  throw new org.eclipse.milo.opcua.stack.core.UaException(
                      org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                      "Structure value required");
                var checkedWrite_TypeId =
                    ((org.eclipse.milo.opcua.stack.core.types.UaStructuredType)
                            checkedWrite_Element)
                        .getTypeId()
                        .toNodeId(checkedWrite_Context.namespaceTable())
                        .orElse(org.eclipse.milo.opcua.stack.core.types.builtin.NodeId.NULL_VALUE);
                boolean checkedWrite_Abstract =
                    checkedWrite_Actual.equals(org.eclipse.milo.opcua.stack.core.NodeIds.Structure)
                        || java.lang.Boolean.TRUE.equals(
                            checkedWrite_Types.getType(checkedWrite_Actual).isAbstract());
                if (!(checkedWrite_Abstract
                    ? checkedWrite_Types.isSubtypeOf(checkedWrite_TypeId, checkedWrite_Actual)
                    : checkedWrite_Actual.equals(checkedWrite_TypeId)))
                  throw new org.eclipse.milo.opcua.stack.core.UaException(
                      org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                      "Structure identity does not match the effective DataType");
              }
              if (checkedWrite_Array)
                java.lang.reflect.Array.set(
                    checkedWrite_Converted, checkedWrite_Index, checkedWrite_Element);
              else checkedWrite_Converted = checkedWrite_Element;
            }
          } else if (checkedWrite_Enumeration) {
            if (checkedWrite_Array)
              checkedWrite_Converted = new java.lang.Integer[checkedWrite_Length];
            for (int checkedWrite_Index = 0;
                checkedWrite_Index < checkedWrite_Length;
                checkedWrite_Index++) {
              java.lang.Object checkedWrite_Element =
                  checkedWrite_Array
                      ? java.lang.reflect.Array.get(checkedWrite_Elements, checkedWrite_Index)
                      : checkedWrite_Elements;
              if (checkedWrite_Element
                  instanceof
                  org.eclipse.milo.opcua.stack.core.types.UaEnumeratedType checkedWrite_Enum)
                checkedWrite_Element = checkedWrite_Enum.getValue();
              if (checkedWrite_Element != null
                  && !(checkedWrite_Element instanceof java.lang.Integer))
                throw new org.eclipse.milo.opcua.stack.core.UaException(
                    org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                    "Enumeration requires an Int32 value");
              if (checkedWrite_Element != null
                  && (checkedWrite_Context.enumValues() != null
                          && !checkedWrite_Context.enumValues().contains(checkedWrite_Element)
                      || checkedWrite_SelectedEnums != null
                          && !checkedWrite_SelectedEnums.contains(checkedWrite_Element)))
                throw new org.eclipse.milo.opcua.stack.core.UaException(
                    org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_OutOfRange,
                    "Unknown Enumeration value: " + checkedWrite_Element);
              if (checkedWrite_Wire && checkedWrite_Array && checkedWrite_Element == null)
                throw new org.eclipse.milo.opcua.stack.core.UaException(
                    org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                    "Enumeration wire arrays cannot contain null elements");
              if (checkedWrite_Array)
                java.lang.reflect.Array.set(
                    checkedWrite_Converted, checkedWrite_Index, checkedWrite_Element);
              else checkedWrite_Converted = checkedWrite_Element;
            }
          } else if (checkedWrite_PayloadArray) {
            if (!checkedWrite_Array
                || org.eclipse.milo.opcua.stack.core.util.ArrayUtil.getValueRank(
                        checkedWrite_Elements)
                    != 1)
              throw new org.eclipse.milo.opcua.stack.core.UaException(
                  org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                  "BaseDataType array requires Java payload values");
            checkedWrite_Converted =
                new org.eclipse.milo.opcua.stack.core.types.builtin.Variant[checkedWrite_Length];
            for (int checkedWrite_Index = 0;
                checkedWrite_Index < checkedWrite_Length;
                checkedWrite_Index++) {
              java.lang.Object checkedWrite_Element =
                  java.lang.reflect.Array.get(checkedWrite_Elements, checkedWrite_Index);
              if (checkedWrite_Wire)
                checkedWrite_Element =
                    org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject.encodeValue(
                        checkedWrite_Context.encodingContext(), checkedWrite_Element);
              ((org.eclipse.milo.opcua.stack.core.types.builtin.Variant[]) checkedWrite_Converted)
                      [checkedWrite_Index] =
                  org.eclipse.milo.opcua.stack.core.types.builtin.Variant.of(checkedWrite_Element);
            }
          } else {
            java.lang.Object checkedWrite_Check = checkedWrite_Elements;
            java.lang.Class<?> checkedWrite_ElementsClass =
                org.eclipse.milo.opcua.stack.core.util.ArrayUtil.getBoxedType(
                    checkedWrite_Elements);
            boolean checkedWrite_Options =
                org.eclipse.milo.opcua.stack.core.types.builtin.OptionSetUInteger.class
                    .isAssignableFrom(checkedWrite_ElementsClass);
            if (checkedWrite_Options) {
              java.lang.Class<?> checkedWrite_Backing =
                  org.eclipse.milo.opcua.stack.core.types.builtin.Variant.of(checkedWrite_Elements)
                      .getDataType()
                      .orElseThrow()
                      .getBackingClass();
              if (checkedWrite_Array)
                checkedWrite_Converted =
                    java.lang.reflect.Array.newInstance(checkedWrite_Backing, checkedWrite_Length);
              for (int checkedWrite_Index = 0;
                  checkedWrite_Index < checkedWrite_Length;
                  checkedWrite_Index++) {
                java.lang.Object checkedWrite_Element =
                    checkedWrite_Array
                        ? java.lang.reflect.Array.get(checkedWrite_Elements, checkedWrite_Index)
                        : checkedWrite_Elements;
                if (checkedWrite_Element != null)
                  checkedWrite_Element =
                      ((org.eclipse.milo.opcua.stack.core.types.builtin.OptionSetUInteger<?>)
                              checkedWrite_Element)
                          .getValue();
                if (checkedWrite_Wire && checkedWrite_Array && checkedWrite_Element == null)
                  throw new org.eclipse.milo.opcua.stack.core.UaException(
                      org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                      "OptionSet wire arrays cannot contain null elements");
                if (checkedWrite_Array)
                  java.lang.reflect.Array.set(
                      checkedWrite_Converted, checkedWrite_Index, checkedWrite_Element);
                else checkedWrite_Converted = checkedWrite_Element;
              }
              checkedWrite_Check = checkedWrite_Converted;
            }
            org.eclipse.milo.opcua.stack.core.types.builtin.Variant.of(checkedWrite_Check);
            var checkedWrite_Assignable =
                checkedWrite_Types.getBackingClass(checkedWrite_Actual) == java.lang.Number.class
                        && checkedWrite_Types.isSubtypeOf(
                            checkedWrite_Actual, org.eclipse.milo.opcua.stack.core.NodeIds.Integer)
                    ? org.eclipse.milo.opcua.stack.core.NodeIds.Integer
                    : checkedWrite_Actual;
            if (!checkedWrite_Payloads
                && checkedWrite_Check != null
                && !checkedWrite_Types.isAssignable(
                    checkedWrite_Assignable,
                    org.eclipse.milo.opcua.stack.core.util.ArrayUtil.getBoxedType(
                        checkedWrite_Check)))
              throw new org.eclipse.milo.opcua.stack.core.UaException(
                  org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                  "Value does not match effective DataType");
          }
          checkedWrite_Value =
              checkedWrite_Value
                      instanceof
                      org.eclipse.milo.opcua.stack.core.types.builtin.Matrix checkedWrite_Matrix
                  ? new org.eclipse.milo.opcua.stack.core.types.builtin.Matrix(
                      checkedWrite_Converted,
                      checkedWrite_Matrix.getDimensions().clone(),
                      checkedWrite_Matrix.getDataType().orElseThrow(),
                      checkedWrite_Matrix.getDataTypeId().orElse(null))
                  : checkedWrite_Converted;
          if (checkedWrite_Empty && checkedWrite_Rank > 1)
            checkedWrite_Value =
                new org.eclipse.milo.opcua.stack.core.types.builtin.Matrix(
                    checkedWrite_Converted, new int[checkedWrite_Rank]);
          if (checkedWrite_Wire) {
            var numericWireValues = new ArrayDeque<Object[]>();
            var numericWirePath = Collections.newSetFromMap(new IdentityHashMap<Object, Boolean>());
            if (checkedWrite_Value != null) {
              numericWireValues.push(new Object[] {checkedWrite_Value, false});
            }
            while (!numericWireValues.isEmpty()) {
              Object[] numericWireFrame = numericWireValues.pop();
              Object numericWireValue = numericWireFrame[0];
              if ((Boolean) numericWireFrame[1]) {
                numericWirePath.remove(numericWireValue);
                continue;
              }
              while (numericWireValue instanceof Variant || numericWireValue instanceof DataValue) {
                if (numericWireValue instanceof DataValue) {
                  if (((DataValue) numericWireValue).getValue() == null) {
                    throw new UaException(
                        StatusCodes.Bad_TypeMismatch,
                        "A DataValue requires a value wrapper; use Variant.NULL_VALUE for null");
                  }
                  if (((DataValue) numericWireValue).getStatusCode() == null) {
                    throw new UaException(
                        StatusCodes.Bad_TypeMismatch,
                        "A DataValue requires a StatusCode; use StatusCode.GOOD for Good");
                  }
                  numericWireValue = ((DataValue) numericWireValue).getValue();
                } else {
                  numericWireValue = ((Variant) numericWireValue).getValue();
                }
              }
              if (numericWireValue instanceof Matrix) {
                numericWireValue = ((Matrix) numericWireValue).getElements();
              }
              if (numericWireValue != null && numericWireValue.getClass().isArray()) {
                if (!numericWirePath.add(numericWireValue)) {
                  throw new UaException(
                      StatusCodes.Bad_TypeMismatch, "Cyclic Variant arrays cannot be encoded");
                }
                numericWireValues.push(new Object[] {numericWireValue, true});
                for (int numericWireIndex = 0;
                    numericWireIndex < Array.getLength(numericWireValue);
                    numericWireIndex++) {
                  Object numericWireElement = Array.get(numericWireValue, numericWireIndex);
                  if (numericWireElement == null
                      && ArrayUtil.getBoxedType(numericWireValue) == Variant.class) {
                    throw new UaException(
                        StatusCodes.Bad_TypeMismatch,
                        "A Variant wire array requires a wrapper for every element; use"
                            + " Variant.NULL_VALUE for null");
                  }
                  if (numericWireElement == null
                      && (UaEnumeratedType.class.isAssignableFrom(
                              ArrayUtil.getBoxedType(numericWireValue))
                          || OptionSetUInteger.class.isAssignableFrom(
                              ArrayUtil.getBoxedType(numericWireValue)))) {
                    throw new UaException(
                        StatusCodes.Bad_TypeMismatch,
                        "An enum or OptionSet wire array cannot encode a null element");
                  }
                  if (numericWireElement == null
                      && ArrayUtil.getBoxedType(numericWireValue) == Boolean.class) {
                    throw new UaException(
                        StatusCodes.Bad_TypeMismatch,
                        "A Boolean wire array cannot retain a null element; Milo encodes it as"
                            + " false");
                  }
                  if (numericWireElement == null
                      && ArrayUtil.getBoxedType(numericWireValue) == StatusCode.class) {
                    throw new UaException(
                        StatusCodes.Bad_TypeMismatch,
                        "A StatusCode wire array cannot retain a null element; Milo encodes it as"
                            + " Good");
                  }
                  if (numericWireElement == null
                      && Number.class.isAssignableFrom(ArrayUtil.getBoxedType(numericWireValue))) {
                    throw new UaException(
                        StatusCodes.Bad_TypeMismatch,
                        "A numeric wire array cannot retain a null element; Milo encodes it as"
                            + " zero");
                  }
                  if (numericWireElement instanceof Variant
                      || numericWireElement instanceof DataValue) {
                    numericWireValues.push(new Object[] {numericWireElement, false});
                  }
                }
              }
            }

            checkedWrite_Value =
                org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject.encodeValue(
                    checkedWrite_Context.encodingContext(), checkedWrite_Value);
          }
        }
        encoded = org.eclipse.milo.opcua.stack.core.types.builtin.Variant.of(checkedWrite_Value);
      } catch (org.eclipse.milo.opcua.stack.core.UaSerializationException checkedWrite_Failure) {
        long checkedWrite_Status =
            checkedWrite_Failure.getStatusCode().getValue()
                    == org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_OutOfRange
                ? org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_OutOfRange
                : org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch;
        throw new org.eclipse.milo.opcua.stack.core.UaException(
            checkedWrite_Status, checkedWrite_Failure);
      } catch (java.lang.IllegalArgumentException
          | java.lang.ClassCastException checkedWrite_Failure) {
        throw new org.eclipse.milo.opcua.stack.core.UaException(
            org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch, checkedWrite_Failure);
      }
    }
    views.checkOpen();
    child.setValue(encoded);
  }

  /**
   * Reads the existing member's value remotely. Only Good status is accepted, including Good
   * subcodes; Uncertain and Bad statuses fail before conversion. A Good null value is valid. An
   * absent node fails with Bad_NotFound. This does not update the wrapper's local value. Use {@code
   * views.variableNode(memberId).readValue()} on the owning ClientViews context to retain quality,
   * timestamps and unconverted values.
   *
   * <p>Concrete enum conversions reject unknown numbers with Bad_OutOfRange. Structured decoding
   * and existing rank/type checks retain their failures.
   *
   * @return the value, which may be null on a present member
   * @throws org.eclipse.milo.opcua.stack.core.UaException if a required node is absent, resolution
   *     fails, or a checked conversion fails; non-Good operation status also fails
   * @throws ClassCastException if a plain payload cast encounters an incompatible Java
   *     representation
   * @throws IllegalStateException if the view context is closed
   */
  @Override
  public @Nullable String readInterfaceName() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    return ViewFutures.await(readInterfaceNameAsync());
  }

  private CompletableFuture<? extends @Nullable String> readInterfaceNameAsyncImplementation() {
    return ViewFutures.map(
        ViewFutures.compose(
            viewMember1Async(),
            child -> {
              if (child == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:InterfaceName (declaration i=24190, owner"
                            + " i=24188) on "
                            + getNodeId()));
              }
              views.checkOpen();
              return child.readAttributeAsync(AttributeId.Value).thenApply(response -> response);
            }),
        value -> {
          views.checkOpen();
          if (value == null || value.getStatusCode() == null) {
            throw new CompletionException(new UaException(StatusCodes.Bad_UnexpectedError));
          }
          if (!value.getStatusCode().isGood()) {
            throw new CompletionException(new UaException(value.getStatusCode()));
          }
          try {
            @Nullable String converted;
            {
              Object rawValue = value.getValue().getValue();
              if (rawValue instanceof Matrix matrix && matrix.isNull()) {
                rawValue = null;
              }
              if (rawValue != null) {
                int actualRank =
                    rawValue instanceof Matrix matrix
                        ? matrix.getValueRank()
                        : ArrayUtil.getValueRank(rawValue);
                Object rankElements =
                    rawValue instanceof Matrix matrix ? matrix.getElements() : rawValue;
                if (!(actualRank == -1)) {
                  throw new UaException(
                      StatusCodes.Bad_TypeMismatch,
                      "http://opcfoundation.org/UA/:InterfaceName (declaration i=24190, owner"
                          + " i=24188)");
                }
                if (rawValue instanceof Matrix matrix) {
                  if (actualRank < 2
                      || rankElements == null
                      || !rankElements.getClass().isArray()
                      || ArrayUtil.getValueRank(rankElements) != 1) {
                    throw new UaException(
                        StatusCodes.Bad_TypeMismatch,
                        "http://opcfoundation.org/UA/:InterfaceName (declaration i=24190, owner"
                            + " i=24188)");
                  }
                  long elementCount = 1;
                  for (int dimension : matrix.getDimensions()) {
                    if (dimension < 0 || elementCount > Integer.MAX_VALUE) {
                      throw new UaException(
                          StatusCodes.Bad_TypeMismatch,
                          "http://opcfoundation.org/UA/:InterfaceName (declaration i=24190, owner"
                              + " i=24188)");
                    }
                    elementCount *= dimension;
                  }
                  if (elementCount != Array.getLength(rankElements)) {
                    throw new UaException(
                        StatusCodes.Bad_TypeMismatch,
                        "http://opcfoundation.org/UA/:InterfaceName (declaration i=24190, owner"
                            + " i=24188)");
                  }
                } else if (actualRank > 1) {
                  throw new UaException(
                      StatusCodes.Bad_TypeMismatch,
                      "http://opcfoundation.org/UA/:InterfaceName (declaration i=24190, owner"
                          + " i=24188): use Matrix for multiple dimensions");
                }
              }
              Object element = rawValue;
              if (element != null && !(element instanceof String)) {
                throw new UaException(
                    StatusCodes.Bad_TypeMismatch,
                    "http://opcfoundation.org/UA/:InterfaceName (declaration i=24190, owner"
                        + " i=24188)");
              }
              converted = (String) element;
            }
            return converted;
          } catch (UaException failure) {
            throw new CompletionException(failure);
          }
        });
  }

  /**
   * Reads the existing member's value remotely. Only Good status is accepted, including Good
   * subcodes; Uncertain and Bad statuses fail before conversion. A Good null value is valid. An
   * absent node fails with Bad_NotFound. This does not update the wrapper's local value. Use {@code
   * views.variableNode(memberId).readValue()} on the owning ClientViews context to retain quality,
   * timestamps and unconverted values.
   *
   * <p>Concrete enum conversions reject unknown numbers with Bad_OutOfRange. Structured decoding
   * and existing rank/type checks retain their failures.
   *
   * <p>Lookup, conversion and service failures complete the future exceptionally. UaException
   * causes preserve OPC UA status. Incompatible plain payload casts can complete exceptionally with
   * ClassCastException. Cancellation does not promise transport cancellation or rollback.
   *
   * <p>A closed view context completes the future exceptionally with IllegalStateException.
   *
   * @return a nonnull future completing with the value, which may be null on a present member
   */
  @Override
  public CompletableFuture<? extends @Nullable String> readInterfaceNameAsync() {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.readInterfaceNameAsyncImplementation());
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }

  /**
   * Writes the existing member's value remotely. A null value is valid. An absent node fails with
   * Bad_NotFound before conversion or Write. This does not create nodes or update the wrapper's
   * local value.
   *
   * <p>Concrete enum conversions reject unknown numbers with Bad_OutOfRange. Structured decoding
   * and existing rank/type checks retain their failures.
   *
   * <p>The actual member DataType, rank and maximum dimensions constrain this selected view write.
   * Incompatible values fail with Bad_TypeMismatch; values outside the actual finite enumeration
   * fail with Bad_OutOfRange. Validation finishes before mutation or Write. Type and enumeration
   * discovery can perform service I/O, including for local setters.
   *
   * @param value the value to store; null is permitted
   * @throws org.eclipse.milo.opcua.stack.core.UaException if a required node is absent, resolution
   *     fails, or a checked conversion fails; non-Good operation status also fails
   * @throws IllegalStateException if the view context is closed
   */
  @Override
  public void writeInterfaceName(@Nullable String value) throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    views.checkOpen();
    StatusCode status = ViewFutures.await(writeInterfaceNameAsync(value));
    if (status == null) {
      throw new UaException(StatusCodes.Bad_UnexpectedError);
    }
    if (!status.isGood()) {
      throw new UaException(status);
    }
  }

  private CompletableFuture<StatusCode> writeInterfaceNameAsyncImplementation(
      @Nullable String value) {
    return ViewFutures.compose(
        viewMember1Async(),
        child -> {
          if (child == null) {
            throw new CompletionException(
                new UaException(
                    StatusCodes.Bad_NotFound,
                    "http://opcfoundation.org/UA/:InterfaceName (declaration i=24190, owner"
                        + " i=24188) on "
                        + getNodeId()));
          }
          return ViewFutures.compose(
              views.readWriteContextAsync(child.getNodeId(), value, KNOWN_ENUMS),
              writeContext -> {
                try {
                  Variant encoded;
                  {
                    var checkedWrite_Context = writeContext;
                    Object checkedWrite_Value = value;
                    var checkedWrite_Selected =
                        ExpandedNodeId.parse("i=12")
                            .toNodeId(checkedWrite_Context.namespaceTable())
                            .orElseThrow(
                                () ->
                                    new org.eclipse.milo.opcua.stack.core.UaException(
                                        org.eclipse.milo.opcua.stack.core.StatusCodes
                                            .Bad_NodeIdInvalid,
                                        "Unknown selected DataType namespace:"
                                            + " ExpandedNodeId[server=ServerIndex[serverIndex=0],"
                                            + " namespace=NamespaceUri[namespaceUri=http://opcfoundation.org/UA/],"
                                            + " identifier=12]"));
                    int checkedWrite_SelectedRank = -1;
                    long[] checkedWrite_SelectedDimensions = new long[] {};
                    Set<Integer> checkedWrite_SelectedEnums = null;
                    boolean checkedWrite_Wire = true;
                    var checkedWrite_Types = checkedWrite_Context.dataTypes();
                    var checkedWrite_Actual = checkedWrite_Context.dataType();
                    int checkedWrite_Rank = checkedWrite_Context.valueRank();
                    var checkedWrite_Bounds = checkedWrite_Context.arrayDimensions();
                    if (!checkedWrite_Types.containsType(checkedWrite_Selected)
                        || !checkedWrite_Types.containsType(checkedWrite_Actual)
                        || !(checkedWrite_Actual.equals(checkedWrite_Selected)
                            || checkedWrite_Types.isSubtypeOf(
                                checkedWrite_Actual, checkedWrite_Selected))) {
                      throw new org.eclipse.milo.opcua.stack.core.UaException(
                          org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                          "Effective DataType "
                              + checkedWrite_Actual
                              + " does not specialize selected "
                              + checkedWrite_Selected);
                    }
                    boolean checkedWrite_SpecializedRank =
                        checkedWrite_SelectedRank == -2
                            || checkedWrite_Rank == checkedWrite_SelectedRank
                            || checkedWrite_SelectedRank == -3
                                && (checkedWrite_Rank == -1 || checkedWrite_Rank == 1)
                            || checkedWrite_SelectedRank == 0 && checkedWrite_Rank > 0;
                    if (!checkedWrite_SpecializedRank
                        || checkedWrite_Rank < -3
                        || checkedWrite_Bounds != null
                            && checkedWrite_Bounds.length != 0
                            && (checkedWrite_Rank <= 0
                                || checkedWrite_Bounds.length != checkedWrite_Rank)) {
                      throw new org.eclipse.milo.opcua.stack.core.UaException(
                          org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                          "Effective ValueRank or ArrayDimensions conflict with the selected"
                              + " contract");
                    }
                    if (checkedWrite_SelectedDimensions.length != 0) {
                      if (checkedWrite_SelectedRank <= 0
                          || checkedWrite_SelectedDimensions.length != checkedWrite_SelectedRank) {
                        throw new org.eclipse.milo.opcua.stack.core.UaException(
                            org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                            "Invalid selected ArrayDimensions");
                      }
                      for (int checkedWrite_Index = 0;
                          checkedWrite_Index < checkedWrite_SelectedDimensions.length;
                          checkedWrite_Index++) {
                        long checkedWrite_Maximum =
                            checkedWrite_SelectedDimensions[checkedWrite_Index];
                        if (checkedWrite_Maximum != 0
                            && (checkedWrite_Bounds == null
                                || checkedWrite_Bounds.length
                                    != checkedWrite_SelectedDimensions.length
                                || checkedWrite_Bounds[checkedWrite_Index].longValue() == 0
                                || checkedWrite_Bounds[checkedWrite_Index].longValue()
                                    > checkedWrite_Maximum)) {
                          throw new org.eclipse.milo.opcua.stack.core.UaException(
                              org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                              "Effective ArrayDimensions broaden the selected maximum");
                        }
                      }
                    }
                    if (checkedWrite_SelectedEnums != null
                        && (checkedWrite_Context.enumValues() == null
                            || !checkedWrite_SelectedEnums.containsAll(
                                checkedWrite_Context.enumValues()))) {
                      throw new org.eclipse.milo.opcua.stack.core.UaException(
                          org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                          "Effective Enumeration domain broadens the selected contract");
                    }
                    try {
                      if (checkedWrite_Value
                              instanceof
                              org.eclipse.milo.opcua.stack.core.types.builtin.Matrix
                                  checkedWrite_Matrix
                          && checkedWrite_Matrix.isNull()) {
                        checkedWrite_Value = null;
                      }
                      if (checkedWrite_Value != null
                          && checkedWrite_SelectedRank == 1
                          && checkedWrite_Types.getBackingClass(checkedWrite_Selected)
                              == org.eclipse.milo.opcua.stack.core.types.builtin.Variant.class) {
                        if (!(checkedWrite_Value instanceof java.lang.Object[])
                            || org.eclipse.milo.opcua.stack.core.util.ArrayUtil.getValueRank(
                                    checkedWrite_Value)
                                != 1)
                          throw new org.eclipse.milo.opcua.stack.core.UaException(
                              org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                              "Selected BaseDataType array requires Java payload values");
                        for (java.lang.Object checkedWrite_Payload :
                            (java.lang.Object[]) checkedWrite_Value)
                          org.eclipse.milo.opcua.stack.core.types.builtin.Variant.of(
                              checkedWrite_Payload);
                        java.lang.Class<?> checkedWrite_EffectiveBacking =
                            checkedWrite_Types.getBackingClass(checkedWrite_Actual);
                        boolean checkedWrite_SpecializedPayloads =
                            checkedWrite_EffectiveBacking
                                    != org.eclipse.milo.opcua.stack.core.types.builtin.Variant.class
                                && !checkedWrite_Actual.equals(
                                    org.eclipse.milo.opcua.stack.core.NodeIds.Structure)
                                && !checkedWrite_Types.isStructType(checkedWrite_Actual)
                                && !checkedWrite_Actual.equals(
                                    org.eclipse.milo.opcua.stack.core.NodeIds.Enumeration)
                                && !checkedWrite_Types.isSubtypeOf(
                                    checkedWrite_Actual,
                                    org.eclipse.milo.opcua.stack.core.NodeIds.Enumeration);
                        if (checkedWrite_SpecializedPayloads
                            && checkedWrite_Value.getClass().getComponentType()
                                == java.lang.Object.class) {
                          java.lang.Object[] checkedWrite_Payloads =
                              (java.lang.Object[]) checkedWrite_Value;
                          java.lang.Object checkedWrite_Projected =
                              java.lang.reflect.Array.newInstance(
                                  checkedWrite_EffectiveBacking, checkedWrite_Payloads.length);
                          for (int checkedWrite_Index = 0;
                              checkedWrite_Index < checkedWrite_Payloads.length;
                              checkedWrite_Index++) {
                            java.lang.Object checkedWrite_Payload =
                                checkedWrite_Payloads[checkedWrite_Index];
                            if (checkedWrite_Payload
                                instanceof
                                org.eclipse.milo.opcua.stack.core.types.builtin.OptionSetUInteger<?>
                                    checkedWrite_Option)
                              checkedWrite_Payload = checkedWrite_Option.getValue();
                            java.lang.reflect.Array.set(
                                checkedWrite_Projected, checkedWrite_Index, checkedWrite_Payload);
                          }
                          checkedWrite_Value = checkedWrite_Projected;
                        }
                      }
                      Object numericElements =
                          checkedWrite_Value instanceof Matrix
                              ? ((Matrix) checkedWrite_Value).getElements()
                              : checkedWrite_Value;
                      if (numericElements != null
                          && numericElements.getClass().isArray()
                          && (numericElements.getClass().getComponentType() == Number.class
                              || numericElements.getClass().getComponentType() == UNumber.class)
                          && (checkedWrite_Actual.equals(NodeIds.Number)
                              || checkedWrite_Types.isSubtypeOf(
                                  checkedWrite_Actual, NodeIds.Number))) {
                        Class<?> numericElementType = null;
                        for (int numericIndex = 0;
                            numericIndex < Array.getLength(numericElements);
                            numericIndex++) {
                          Object numericElement = Array.get(numericElements, numericIndex);
                          if (numericElement != null) {
                            if (numericElementType != null
                                && numericElementType != numericElement.getClass()) {
                              throw new UaException(
                                  StatusCodes.Bad_TypeMismatch,
                                  "An abstract numeric array requires one homogeneous wire element"
                                      + " type");
                            }
                            numericElementType = numericElement.getClass();
                          }
                        }
                        if (numericElementType == null) {
                          numericElementType =
                              checkedWrite_Types.getBackingClass(checkedWrite_Actual);
                        }
                        if (numericElementType == Number.class
                            || numericElementType == UNumber.class) {
                          throw new UaException(
                              StatusCodes.Bad_TypeMismatch,
                              "An empty or all-null abstract numeric array requires a concretely"
                                  + " typed array");
                        }
                        Object numericArray =
                            Array.newInstance(numericElementType, Array.getLength(numericElements));
                        for (int numericIndex = 0;
                            numericIndex < Array.getLength(numericElements);
                            numericIndex++) {
                          Array.set(
                              numericArray, numericIndex, Array.get(numericElements, numericIndex));
                        }
                        if (checkedWrite_Value instanceof Matrix) {
                          checkedWrite_Value =
                              new Matrix(
                                  numericArray,
                                  ((Matrix) checkedWrite_Value).getDimensions().clone(),
                                  ((Matrix) checkedWrite_Value)
                                      .getDataType()
                                      .orElseThrow(
                                          () ->
                                              new UaException(
                                                  StatusCodes.Bad_TypeMismatch,
                                                  "A numeric Matrix requires an explicit wire"
                                                      + " DataType")),
                                  ((Matrix) checkedWrite_Value).getDataTypeId().orElse(null));
                        } else {
                          checkedWrite_Value = numericArray;
                        }
                      }

                      if (checkedWrite_Value != null) {
                        java.lang.Object checkedWrite_Elements =
                            checkedWrite_Value
                                    instanceof
                                    org.eclipse.milo.opcua.stack.core.types.builtin.Matrix
                                        checkedWrite_Matrix
                                ? checkedWrite_Matrix.getElements()
                                : checkedWrite_Value;
                        int checkedWrite_ValueRank =
                            checkedWrite_Value
                                    instanceof
                                    org.eclipse.milo.opcua.stack.core.types.builtin.Matrix
                                        checkedWrite_Matrix
                                ? checkedWrite_Matrix.getValueRank()
                                : org.eclipse.milo.opcua.stack.core.util.ArrayUtil.getValueRank(
                                    checkedWrite_Value);
                        boolean checkedWrite_Empty =
                            checkedWrite_Value.getClass().isArray()
                                && org.eclipse.milo.opcua.stack.core.util.ArrayUtil.getValueRank(
                                        checkedWrite_Value)
                                    == 1
                                && java.lang.reflect.Array.getLength(checkedWrite_Value) == 0;
                        boolean checkedWrite_Shape =
                            checkedWrite_Rank == -2
                                || checkedWrite_Rank == -3
                                    && (checkedWrite_ValueRank == -1 || checkedWrite_ValueRank == 1)
                                || checkedWrite_Rank == -1 && checkedWrite_ValueRank == -1
                                || checkedWrite_Rank == 0 && checkedWrite_ValueRank >= 1
                                || checkedWrite_Rank > 0
                                    && (checkedWrite_ValueRank == checkedWrite_Rank
                                        || checkedWrite_Empty);
                        if (!checkedWrite_Shape)
                          throw new org.eclipse.milo.opcua.stack.core.UaException(
                              org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                              "ValueRank mismatch");
                        if (checkedWrite_Value
                            instanceof
                            org.eclipse.milo.opcua.stack.core.types.builtin.Matrix
                                checkedWrite_Matrix) {
                          int[] checkedWrite_Dimensions = checkedWrite_Matrix.getDimensions();
                          if (checkedWrite_Dimensions.length < 2
                              || checkedWrite_Elements == null
                              || !checkedWrite_Elements.getClass().isArray()
                              || org.eclipse.milo.opcua.stack.core.util.ArrayUtil.getValueRank(
                                      checkedWrite_Elements)
                                  != 1) {
                            throw new org.eclipse.milo.opcua.stack.core.UaException(
                                org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                                "Malformed Matrix representation");
                          }
                          long checkedWrite_Count = 1;
                          for (int checkedWrite_Dimension : checkedWrite_Dimensions) {
                            if (checkedWrite_Dimension < 0
                                || checkedWrite_Count > java.lang.Integer.MAX_VALUE)
                              throw new org.eclipse.milo.opcua.stack.core.UaException(
                                  org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                                  "Malformed Matrix dimensions");
                            checkedWrite_Count *= checkedWrite_Dimension;
                          }
                          if (checkedWrite_Count
                                  != java.lang.reflect.Array.getLength(checkedWrite_Elements)
                              || !checkedWrite_Matrix
                                  .getDataType()
                                  .equals(
                                      org.eclipse.milo.opcua.stack.core.types.builtin.Variant.of(
                                              checkedWrite_Elements)
                                          .getDataType())) {
                            throw new org.eclipse.milo.opcua.stack.core.UaException(
                                org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                                "Matrix dimensions or DataType do not match elements");
                          }
                        }
                        if (!checkedWrite_Empty
                            && checkedWrite_Bounds != null
                            && checkedWrite_Bounds.length != 0) {
                          int[] checkedWrite_Dimensions =
                              checkedWrite_Value
                                      instanceof
                                      org.eclipse.milo.opcua.stack.core.types.builtin.Matrix
                                          checkedWrite_Matrix
                                  ? checkedWrite_Matrix.getDimensions()
                                  : org.eclipse.milo.opcua.stack.core.util.ArrayUtil.getDimensions(
                                      checkedWrite_Value);
                          if (checkedWrite_Dimensions.length != checkedWrite_Bounds.length)
                            throw new org.eclipse.milo.opcua.stack.core.UaException(
                                org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                                "ArrayDimensions mismatch");
                          for (int checkedWrite_Index = 0;
                              checkedWrite_Index < checkedWrite_Dimensions.length;
                              checkedWrite_Index++) {
                            if (checkedWrite_Bounds[checkedWrite_Index].longValue() != 0
                                && checkedWrite_Dimensions[checkedWrite_Index]
                                    > checkedWrite_Bounds[checkedWrite_Index].longValue())
                              throw new org.eclipse.milo.opcua.stack.core.UaException(
                                  org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                                  "Value exceeds ArrayDimensions maximum");
                          }
                        }
                        boolean checkedWrite_Array = checkedWrite_Elements.getClass().isArray();
                        int checkedWrite_Length =
                            checkedWrite_Array
                                ? java.lang.reflect.Array.getLength(checkedWrite_Elements)
                                : 1;
                        boolean checkedWrite_Structure =
                            checkedWrite_Actual.equals(
                                    org.eclipse.milo.opcua.stack.core.NodeIds.Structure)
                                || checkedWrite_Types.isStructType(checkedWrite_Actual);
                        boolean checkedWrite_Enumeration =
                            checkedWrite_Actual.equals(
                                    org.eclipse.milo.opcua.stack.core.NodeIds.Enumeration)
                                || checkedWrite_Types.isSubtypeOf(
                                    checkedWrite_Actual,
                                    org.eclipse.milo.opcua.stack.core.NodeIds.Enumeration);
                        boolean checkedWrite_Payloads =
                            checkedWrite_Types.getBackingClass(checkedWrite_Actual)
                                == org.eclipse.milo.opcua.stack.core.types.builtin.Variant.class;
                        boolean checkedWrite_PayloadArray =
                            checkedWrite_Payloads
                                && checkedWrite_SelectedRank == 1
                                && checkedWrite_Types.getBackingClass(checkedWrite_Selected)
                                    == org.eclipse.milo.opcua.stack.core.types.builtin.Variant
                                        .class;
                        java.lang.Object checkedWrite_Converted = checkedWrite_Elements;
                        if (checkedWrite_Structure) {
                          var checkedWrite_Codec =
                              checkedWrite_Context
                                  .encodingContext()
                                  .getDataTypeManager()
                                  .getCodec(checkedWrite_Actual);
                          java.lang.Class<?> checkedWrite_Class =
                              checkedWrite_Codec == null
                                  ? org.eclipse.milo.opcua.stack.core.types.UaStructuredType.class
                                  : checkedWrite_Codec.getType();
                          if (checkedWrite_Array)
                            checkedWrite_Converted =
                                java.lang.reflect.Array.newInstance(
                                    checkedWrite_Class, checkedWrite_Length);
                          for (int checkedWrite_Index = 0;
                              checkedWrite_Index < checkedWrite_Length;
                              checkedWrite_Index++) {
                            java.lang.Object checkedWrite_Element =
                                checkedWrite_Array
                                    ? java.lang.reflect.Array.get(
                                        checkedWrite_Elements, checkedWrite_Index)
                                    : checkedWrite_Elements;
                            if (checkedWrite_Element
                                instanceof
                                org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject
                                    checkedWrite_Object) {
                              checkedWrite_Element =
                                  checkedWrite_Object.isNull()
                                      ? null
                                      : checkedWrite_Object.decode(
                                          checkedWrite_Context.encodingContext());
                            }
                            if (checkedWrite_Element != null) {
                              if (!(checkedWrite_Element
                                  instanceof
                                  org.eclipse.milo.opcua.stack.core.types.UaStructuredType))
                                throw new org.eclipse.milo.opcua.stack.core.UaException(
                                    org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                                    "Structure value required");
                              var checkedWrite_TypeId =
                                  ((org.eclipse.milo.opcua.stack.core.types.UaStructuredType)
                                          checkedWrite_Element)
                                      .getTypeId()
                                      .toNodeId(checkedWrite_Context.namespaceTable())
                                      .orElse(
                                          org.eclipse.milo.opcua.stack.core.types.builtin.NodeId
                                              .NULL_VALUE);
                              boolean checkedWrite_Abstract =
                                  checkedWrite_Actual.equals(
                                          org.eclipse.milo.opcua.stack.core.NodeIds.Structure)
                                      || java.lang.Boolean.TRUE.equals(
                                          checkedWrite_Types
                                              .getType(checkedWrite_Actual)
                                              .isAbstract());
                              if (!(checkedWrite_Abstract
                                  ? checkedWrite_Types.isSubtypeOf(
                                      checkedWrite_TypeId, checkedWrite_Actual)
                                  : checkedWrite_Actual.equals(checkedWrite_TypeId)))
                                throw new org.eclipse.milo.opcua.stack.core.UaException(
                                    org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                                    "Structure identity does not match the effective DataType");
                            }
                            if (checkedWrite_Array)
                              java.lang.reflect.Array.set(
                                  checkedWrite_Converted, checkedWrite_Index, checkedWrite_Element);
                            else checkedWrite_Converted = checkedWrite_Element;
                          }
                        } else if (checkedWrite_Enumeration) {
                          if (checkedWrite_Array)
                            checkedWrite_Converted = new java.lang.Integer[checkedWrite_Length];
                          for (int checkedWrite_Index = 0;
                              checkedWrite_Index < checkedWrite_Length;
                              checkedWrite_Index++) {
                            java.lang.Object checkedWrite_Element =
                                checkedWrite_Array
                                    ? java.lang.reflect.Array.get(
                                        checkedWrite_Elements, checkedWrite_Index)
                                    : checkedWrite_Elements;
                            if (checkedWrite_Element
                                instanceof
                                org.eclipse.milo.opcua.stack.core.types.UaEnumeratedType
                                    checkedWrite_Enum)
                              checkedWrite_Element = checkedWrite_Enum.getValue();
                            if (checkedWrite_Element != null
                                && !(checkedWrite_Element instanceof java.lang.Integer))
                              throw new org.eclipse.milo.opcua.stack.core.UaException(
                                  org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                                  "Enumeration requires an Int32 value");
                            if (checkedWrite_Element != null
                                && (checkedWrite_Context.enumValues() != null
                                        && !checkedWrite_Context
                                            .enumValues()
                                            .contains(checkedWrite_Element)
                                    || checkedWrite_SelectedEnums != null
                                        && !checkedWrite_SelectedEnums.contains(
                                            checkedWrite_Element)))
                              throw new org.eclipse.milo.opcua.stack.core.UaException(
                                  org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_OutOfRange,
                                  "Unknown Enumeration value: " + checkedWrite_Element);
                            if (checkedWrite_Wire
                                && checkedWrite_Array
                                && checkedWrite_Element == null)
                              throw new org.eclipse.milo.opcua.stack.core.UaException(
                                  org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                                  "Enumeration wire arrays cannot contain null elements");
                            if (checkedWrite_Array)
                              java.lang.reflect.Array.set(
                                  checkedWrite_Converted, checkedWrite_Index, checkedWrite_Element);
                            else checkedWrite_Converted = checkedWrite_Element;
                          }
                        } else if (checkedWrite_PayloadArray) {
                          if (!checkedWrite_Array
                              || org.eclipse.milo.opcua.stack.core.util.ArrayUtil.getValueRank(
                                      checkedWrite_Elements)
                                  != 1)
                            throw new org.eclipse.milo.opcua.stack.core.UaException(
                                org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                                "BaseDataType array requires Java payload values");
                          checkedWrite_Converted =
                              new org.eclipse.milo.opcua.stack.core.types.builtin.Variant
                                  [checkedWrite_Length];
                          for (int checkedWrite_Index = 0;
                              checkedWrite_Index < checkedWrite_Length;
                              checkedWrite_Index++) {
                            java.lang.Object checkedWrite_Element =
                                java.lang.reflect.Array.get(
                                    checkedWrite_Elements, checkedWrite_Index);
                            if (checkedWrite_Wire)
                              checkedWrite_Element =
                                  org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject
                                      .encodeValue(
                                          checkedWrite_Context.encodingContext(),
                                          checkedWrite_Element);
                            ((org.eclipse.milo.opcua.stack.core.types.builtin.Variant[])
                                        checkedWrite_Converted)
                                    [checkedWrite_Index] =
                                org.eclipse.milo.opcua.stack.core.types.builtin.Variant.of(
                                    checkedWrite_Element);
                          }
                        } else {
                          java.lang.Object checkedWrite_Check = checkedWrite_Elements;
                          java.lang.Class<?> checkedWrite_ElementsClass =
                              org.eclipse.milo.opcua.stack.core.util.ArrayUtil.getBoxedType(
                                  checkedWrite_Elements);
                          boolean checkedWrite_Options =
                              org.eclipse.milo.opcua.stack.core.types.builtin.OptionSetUInteger
                                  .class
                                  .isAssignableFrom(checkedWrite_ElementsClass);
                          if (checkedWrite_Options) {
                            java.lang.Class<?> checkedWrite_Backing =
                                org.eclipse.milo.opcua.stack.core.types.builtin.Variant.of(
                                        checkedWrite_Elements)
                                    .getDataType()
                                    .orElseThrow()
                                    .getBackingClass();
                            if (checkedWrite_Array)
                              checkedWrite_Converted =
                                  java.lang.reflect.Array.newInstance(
                                      checkedWrite_Backing, checkedWrite_Length);
                            for (int checkedWrite_Index = 0;
                                checkedWrite_Index < checkedWrite_Length;
                                checkedWrite_Index++) {
                              java.lang.Object checkedWrite_Element =
                                  checkedWrite_Array
                                      ? java.lang.reflect.Array.get(
                                          checkedWrite_Elements, checkedWrite_Index)
                                      : checkedWrite_Elements;
                              if (checkedWrite_Element != null)
                                checkedWrite_Element =
                                    ((org.eclipse.milo.opcua.stack.core.types.builtin
                                                    .OptionSetUInteger<
                                                ?>)
                                            checkedWrite_Element)
                                        .getValue();
                              if (checkedWrite_Wire
                                  && checkedWrite_Array
                                  && checkedWrite_Element == null)
                                throw new org.eclipse.milo.opcua.stack.core.UaException(
                                    org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                                    "OptionSet wire arrays cannot contain null elements");
                              if (checkedWrite_Array)
                                java.lang.reflect.Array.set(
                                    checkedWrite_Converted,
                                    checkedWrite_Index,
                                    checkedWrite_Element);
                              else checkedWrite_Converted = checkedWrite_Element;
                            }
                            checkedWrite_Check = checkedWrite_Converted;
                          }
                          org.eclipse.milo.opcua.stack.core.types.builtin.Variant.of(
                              checkedWrite_Check);
                          var checkedWrite_Assignable =
                              checkedWrite_Types.getBackingClass(checkedWrite_Actual)
                                          == java.lang.Number.class
                                      && checkedWrite_Types.isSubtypeOf(
                                          checkedWrite_Actual,
                                          org.eclipse.milo.opcua.stack.core.NodeIds.Integer)
                                  ? org.eclipse.milo.opcua.stack.core.NodeIds.Integer
                                  : checkedWrite_Actual;
                          if (!checkedWrite_Payloads
                              && checkedWrite_Check != null
                              && !checkedWrite_Types.isAssignable(
                                  checkedWrite_Assignable,
                                  org.eclipse.milo.opcua.stack.core.util.ArrayUtil.getBoxedType(
                                      checkedWrite_Check)))
                            throw new org.eclipse.milo.opcua.stack.core.UaException(
                                org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                                "Value does not match effective DataType");
                        }
                        checkedWrite_Value =
                            checkedWrite_Value
                                    instanceof
                                    org.eclipse.milo.opcua.stack.core.types.builtin.Matrix
                                        checkedWrite_Matrix
                                ? new org.eclipse.milo.opcua.stack.core.types.builtin.Matrix(
                                    checkedWrite_Converted,
                                    checkedWrite_Matrix.getDimensions().clone(),
                                    checkedWrite_Matrix.getDataType().orElseThrow(),
                                    checkedWrite_Matrix.getDataTypeId().orElse(null))
                                : checkedWrite_Converted;
                        if (checkedWrite_Empty && checkedWrite_Rank > 1)
                          checkedWrite_Value =
                              new org.eclipse.milo.opcua.stack.core.types.builtin.Matrix(
                                  checkedWrite_Converted, new int[checkedWrite_Rank]);
                        if (checkedWrite_Wire) {
                          var numericWireValues = new ArrayDeque<Object[]>();
                          var numericWirePath =
                              Collections.newSetFromMap(new IdentityHashMap<Object, Boolean>());
                          if (checkedWrite_Value != null) {
                            numericWireValues.push(new Object[] {checkedWrite_Value, false});
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
                                      "A DataValue requires a value wrapper; use Variant.NULL_VALUE"
                                          + " for null");
                                }
                                if (((DataValue) numericWireValue).getStatusCode() == null) {
                                  throw new UaException(
                                      StatusCodes.Bad_TypeMismatch,
                                      "A DataValue requires a StatusCode; use StatusCode.GOOD for"
                                          + " Good");
                                }
                                numericWireValue = ((DataValue) numericWireValue).getValue();
                              } else {
                                numericWireValue = ((Variant) numericWireValue).getValue();
                              }
                            }
                            if (numericWireValue instanceof Matrix) {
                              numericWireValue = ((Matrix) numericWireValue).getElements();
                            }
                            if (numericWireValue != null && numericWireValue.getClass().isArray()) {
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
                                    && ArrayUtil.getBoxedType(numericWireValue) == Variant.class) {
                                  throw new UaException(
                                      StatusCodes.Bad_TypeMismatch,
                                      "A Variant wire array requires a wrapper for every element;"
                                          + " use Variant.NULL_VALUE for null");
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
                                    && ArrayUtil.getBoxedType(numericWireValue) == Boolean.class) {
                                  throw new UaException(
                                      StatusCodes.Bad_TypeMismatch,
                                      "A Boolean wire array cannot retain a null element; Milo"
                                          + " encodes it as false");
                                }
                                if (numericWireElement == null
                                    && ArrayUtil.getBoxedType(numericWireValue)
                                        == StatusCode.class) {
                                  throw new UaException(
                                      StatusCodes.Bad_TypeMismatch,
                                      "A StatusCode wire array cannot retain a null element; Milo"
                                          + " encodes it as Good");
                                }
                                if (numericWireElement == null
                                    && Number.class.isAssignableFrom(
                                        ArrayUtil.getBoxedType(numericWireValue))) {
                                  throw new UaException(
                                      StatusCodes.Bad_TypeMismatch,
                                      "A numeric wire array cannot retain a null element; Milo"
                                          + " encodes it as zero");
                                }
                                if (numericWireElement instanceof Variant
                                    || numericWireElement instanceof DataValue) {
                                  numericWireValues.push(new Object[] {numericWireElement, false});
                                }
                              }
                            }
                          }

                          checkedWrite_Value =
                              org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject
                                  .encodeValue(
                                      checkedWrite_Context.encodingContext(), checkedWrite_Value);
                        }
                      }
                      encoded =
                          org.eclipse.milo.opcua.stack.core.types.builtin.Variant.of(
                              checkedWrite_Value);
                    } catch (
                        org.eclipse.milo.opcua.stack.core.UaSerializationException
                            checkedWrite_Failure) {
                      long checkedWrite_Status =
                          checkedWrite_Failure.getStatusCode().getValue()
                                  == org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_OutOfRange
                              ? org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_OutOfRange
                              : org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch;
                      throw new org.eclipse.milo.opcua.stack.core.UaException(
                          checkedWrite_Status, checkedWrite_Failure);
                    } catch (java.lang.IllegalArgumentException
                        | java.lang.ClassCastException checkedWrite_Failure) {
                      throw new org.eclipse.milo.opcua.stack.core.UaException(
                          org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                          checkedWrite_Failure);
                    }
                  }
                  views.checkOpen();
                  return child
                      .writeAttributeAsync(AttributeId.Value, DataValue.valueOnly(encoded))
                      .thenApply(response -> response);
                } catch (Exception failure) {
                  return CompletableFuture.failedFuture(failure);
                }
              });
        });
  }

  /**
   * Writes the existing member's value remotely. A null value is valid. An absent node fails with
   * Bad_NotFound before conversion or Write. This does not create nodes or update the wrapper's
   * local value.
   *
   * <p>Concrete enum conversions reject unknown numbers with Bad_OutOfRange. Structured decoding
   * and existing rank/type checks retain their failures.
   *
   * <p>Lookup, conversion and service failures complete the future exceptionally. UaException
   * causes preserve OPC UA status. Incompatible plain payload casts can complete exceptionally with
   * ClassCastException. Cancellation does not promise transport cancellation or rollback.
   *
   * <p>The actual member DataType, rank and maximum dimensions constrain this selected view write.
   * Incompatible values fail with Bad_TypeMismatch; values outside the actual finite enumeration
   * fail with Bad_OutOfRange. Validation finishes before mutation or Write. Type and enumeration
   * discovery can perform service I/O, including for local setters.
   *
   * <p>A closed view context completes the future exceptionally with IllegalStateException.
   *
   * @param value the value to store; null is permitted
   * @return a nonnull future completing with the Write operation status, including non-Good
   *     statuses
   */
  @Override
  public CompletableFuture<StatusCode> writeInterfaceNameAsync(@Nullable String value) {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.writeInterfaceNameAsyncImplementation(value));
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }

  /**
   * Resolves the optional member by its namespace-qualified path. Returns null only for confirmed
   * absence. Resolution does not create a UA node. It can perform service I/O and construct or
   * reuse a Java wrapper in Milo's address space cache. A reference can change after lookup.
   *
   * <p>The returned object exposes the selected child contract and shares the retained raw node's
   * state through this view context.
   *
   * @return the existing member, or null for confirmed absence
   * @throws org.eclipse.milo.opcua.stack.core.UaException if a required node is absent, resolution
   *     fails, or a checked conversion fails
   * @throws IllegalStateException if the view context is closed
   */
  @Override
  public @Nullable BaseDataVariableType getTimeAwareOffsetNode() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    return ViewFutures.await(getTimeAwareOffsetNodeAsync());
  }

  private CompletableFuture<? extends @Nullable BaseDataVariableType>
      getTimeAwareOffsetNodeAsyncImplementation() {
    return ViewFutures.compose(
        viewMember2Async(),
        child ->
            child == null
                ? CompletableFuture.completedFuture(null)
                : views.wrapVariableAsync(child.getNodeId(), BaseDataVariableTypeView.TYPE));
  }

  /**
   * Resolves the optional member by its namespace-qualified path. Returns null only for confirmed
   * absence. Resolution does not create a UA node. It can perform service I/O and construct or
   * reuse a Java wrapper in Milo's address space cache. A reference can change after lookup.
   *
   * <p>Lookup, conversion and service failures complete the future exceptionally. UaException
   * causes preserve OPC UA status. Incompatible plain payload casts can complete exceptionally with
   * ClassCastException. Cancellation does not promise transport cancellation or rollback.
   *
   * <p>The returned object exposes the selected child contract and shares the retained raw node's
   * state through this view context.
   *
   * <p>A closed view context completes the future exceptionally with IllegalStateException.
   *
   * @return a nonnull future completing with the existing member, or null for confirmed absence
   */
  @Override
  public CompletableFuture<? extends @Nullable BaseDataVariableType> getTimeAwareOffsetNodeAsync() {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.getTimeAwareOffsetNodeAsyncImplementation());
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }

  /**
   * Gets the existing member's local value without checking its quality. A null value is valid; an
   * absent node fails with Bad_NotFound. Lookup may perform service I/O; the value is not read
   * remotely. Use the node's raw DataValue to inspect quality and timestamps.
   *
   * <p>Concrete enum conversions reject unknown numbers with Bad_OutOfRange. Structured decoding
   * and existing rank/type checks retain their failures.
   *
   * @return the value, which may be null on a present member
   * @throws org.eclipse.milo.opcua.stack.core.UaException if a required node is absent, resolution
   *     fails, or a checked conversion fails
   * @throws ClassCastException if a plain payload cast encounters an incompatible Java
   *     representation
   * @throws IllegalStateException if the view context is closed
   */
  @Override
  public @Nullable UInteger getTimeAwareOffset() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    var child = viewMember2();
    if (child == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:TimeAwareOffset (declaration i=24194, owner i=24191)"
              + " on "
              + getNodeId());
    }
    @Nullable UInteger converted;
    {
      Object rawValue = child.getValue().getValue().getValue();
      if (rawValue instanceof Matrix matrix && matrix.isNull()) {
        rawValue = null;
      }
      if (rawValue != null) {
        int actualRank =
            rawValue instanceof Matrix matrix
                ? matrix.getValueRank()
                : ArrayUtil.getValueRank(rawValue);
        Object rankElements = rawValue instanceof Matrix matrix ? matrix.getElements() : rawValue;
        if (!(actualRank == -1)) {
          throw new UaException(
              StatusCodes.Bad_TypeMismatch,
              "http://opcfoundation.org/UA/:TimeAwareOffset (declaration i=24194, owner i=24191)");
        }
        if (rawValue instanceof Matrix matrix) {
          if (actualRank < 2
              || rankElements == null
              || !rankElements.getClass().isArray()
              || ArrayUtil.getValueRank(rankElements) != 1) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "http://opcfoundation.org/UA/:TimeAwareOffset (declaration i=24194, owner"
                    + " i=24191)");
          }
          long elementCount = 1;
          for (int dimension : matrix.getDimensions()) {
            if (dimension < 0 || elementCount > Integer.MAX_VALUE) {
              throw new UaException(
                  StatusCodes.Bad_TypeMismatch,
                  "http://opcfoundation.org/UA/:TimeAwareOffset (declaration i=24194, owner"
                      + " i=24191)");
            }
            elementCount *= dimension;
          }
          if (elementCount != Array.getLength(rankElements)) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "http://opcfoundation.org/UA/:TimeAwareOffset (declaration i=24194, owner"
                    + " i=24191)");
          }
        } else if (actualRank > 1) {
          throw new UaException(
              StatusCodes.Bad_TypeMismatch,
              "http://opcfoundation.org/UA/:TimeAwareOffset (declaration i=24194, owner i=24191):"
                  + " use Matrix for multiple dimensions");
        }
      }
      Object element = rawValue;
      if (element != null && !(element instanceof UInteger)) {
        throw new UaException(
            StatusCodes.Bad_TypeMismatch,
            "http://opcfoundation.org/UA/:TimeAwareOffset (declaration i=24194, owner i=24191)");
      }
      converted = (UInteger) element;
    }
    return converted;
  }

  /**
   * Sets the existing member's local value. A null value is valid. An absent node fails with
   * Bad_NotFound before conversion or mutation. This does not create nodes or silently skip writes.
   * This does not send a Write service request.
   *
   * <p>Concrete enum conversions reject unknown numbers with Bad_OutOfRange. Structured decoding
   * and existing rank/type checks retain their failures.
   *
   * <p>The actual member DataType, rank and maximum dimensions constrain this selected view write.
   * Incompatible values fail with Bad_TypeMismatch; values outside the actual finite enumeration
   * fail with Bad_OutOfRange. Validation finishes before mutation or Write. Type and enumeration
   * discovery can perform service I/O, including for local setters.
   *
   * @param value the value to store; null is permitted
   * @throws org.eclipse.milo.opcua.stack.core.UaException if a required node is absent, resolution
   *     fails, or a checked conversion fails
   * @throws IllegalStateException if the view context is closed
   */
  @Override
  public void setTimeAwareOffset(@Nullable UInteger value) throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    var child = viewMember2();
    if (child == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:TimeAwareOffset (declaration i=24194, owner i=24191)"
              + " on "
              + getNodeId());
    }
    var writeContext = views.localWriteContext(child.getNodeId(), value, KNOWN_ENUMS);
    Variant encoded;
    {
      var checkedWrite_Context = writeContext;
      Object checkedWrite_Value = value;
      var checkedWrite_Selected =
          ExpandedNodeId.parse("i=7")
              .toNodeId(checkedWrite_Context.namespaceTable())
              .orElseThrow(
                  () ->
                      new org.eclipse.milo.opcua.stack.core.UaException(
                          org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_NodeIdInvalid,
                          "Unknown selected DataType namespace:"
                              + " ExpandedNodeId[server=ServerIndex[serverIndex=0],"
                              + " namespace=NamespaceUri[namespaceUri=http://opcfoundation.org/UA/],"
                              + " identifier=7]"));
      int checkedWrite_SelectedRank = -1;
      long[] checkedWrite_SelectedDimensions = new long[] {};
      Set<Integer> checkedWrite_SelectedEnums = null;
      boolean checkedWrite_Wire = false;
      var checkedWrite_Types = checkedWrite_Context.dataTypes();
      var checkedWrite_Actual = checkedWrite_Context.dataType();
      int checkedWrite_Rank = checkedWrite_Context.valueRank();
      var checkedWrite_Bounds = checkedWrite_Context.arrayDimensions();
      if (!checkedWrite_Types.containsType(checkedWrite_Selected)
          || !checkedWrite_Types.containsType(checkedWrite_Actual)
          || !(checkedWrite_Actual.equals(checkedWrite_Selected)
              || checkedWrite_Types.isSubtypeOf(checkedWrite_Actual, checkedWrite_Selected))) {
        throw new org.eclipse.milo.opcua.stack.core.UaException(
            org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
            "Effective DataType "
                + checkedWrite_Actual
                + " does not specialize selected "
                + checkedWrite_Selected);
      }
      boolean checkedWrite_SpecializedRank =
          checkedWrite_SelectedRank == -2
              || checkedWrite_Rank == checkedWrite_SelectedRank
              || checkedWrite_SelectedRank == -3
                  && (checkedWrite_Rank == -1 || checkedWrite_Rank == 1)
              || checkedWrite_SelectedRank == 0 && checkedWrite_Rank > 0;
      if (!checkedWrite_SpecializedRank
          || checkedWrite_Rank < -3
          || checkedWrite_Bounds != null
              && checkedWrite_Bounds.length != 0
              && (checkedWrite_Rank <= 0 || checkedWrite_Bounds.length != checkedWrite_Rank)) {
        throw new org.eclipse.milo.opcua.stack.core.UaException(
            org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
            "Effective ValueRank or ArrayDimensions conflict with the selected contract");
      }
      if (checkedWrite_SelectedDimensions.length != 0) {
        if (checkedWrite_SelectedRank <= 0
            || checkedWrite_SelectedDimensions.length != checkedWrite_SelectedRank) {
          throw new org.eclipse.milo.opcua.stack.core.UaException(
              org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
              "Invalid selected ArrayDimensions");
        }
        for (int checkedWrite_Index = 0;
            checkedWrite_Index < checkedWrite_SelectedDimensions.length;
            checkedWrite_Index++) {
          long checkedWrite_Maximum = checkedWrite_SelectedDimensions[checkedWrite_Index];
          if (checkedWrite_Maximum != 0
              && (checkedWrite_Bounds == null
                  || checkedWrite_Bounds.length != checkedWrite_SelectedDimensions.length
                  || checkedWrite_Bounds[checkedWrite_Index].longValue() == 0
                  || checkedWrite_Bounds[checkedWrite_Index].longValue() > checkedWrite_Maximum)) {
            throw new org.eclipse.milo.opcua.stack.core.UaException(
                org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                "Effective ArrayDimensions broaden the selected maximum");
          }
        }
      }
      if (checkedWrite_SelectedEnums != null
          && (checkedWrite_Context.enumValues() == null
              || !checkedWrite_SelectedEnums.containsAll(checkedWrite_Context.enumValues()))) {
        throw new org.eclipse.milo.opcua.stack.core.UaException(
            org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
            "Effective Enumeration domain broadens the selected contract");
      }
      try {
        if (checkedWrite_Value
                instanceof
                org.eclipse.milo.opcua.stack.core.types.builtin.Matrix checkedWrite_Matrix
            && checkedWrite_Matrix.isNull()) {
          checkedWrite_Value = null;
        }
        if (checkedWrite_Value != null
            && checkedWrite_SelectedRank == 1
            && checkedWrite_Types.getBackingClass(checkedWrite_Selected)
                == org.eclipse.milo.opcua.stack.core.types.builtin.Variant.class) {
          if (!(checkedWrite_Value instanceof java.lang.Object[])
              || org.eclipse.milo.opcua.stack.core.util.ArrayUtil.getValueRank(checkedWrite_Value)
                  != 1)
            throw new org.eclipse.milo.opcua.stack.core.UaException(
                org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                "Selected BaseDataType array requires Java payload values");
          for (java.lang.Object checkedWrite_Payload : (java.lang.Object[]) checkedWrite_Value)
            org.eclipse.milo.opcua.stack.core.types.builtin.Variant.of(checkedWrite_Payload);
          java.lang.Class<?> checkedWrite_EffectiveBacking =
              checkedWrite_Types.getBackingClass(checkedWrite_Actual);
          boolean checkedWrite_SpecializedPayloads =
              checkedWrite_EffectiveBacking
                      != org.eclipse.milo.opcua.stack.core.types.builtin.Variant.class
                  && !checkedWrite_Actual.equals(
                      org.eclipse.milo.opcua.stack.core.NodeIds.Structure)
                  && !checkedWrite_Types.isStructType(checkedWrite_Actual)
                  && !checkedWrite_Actual.equals(
                      org.eclipse.milo.opcua.stack.core.NodeIds.Enumeration)
                  && !checkedWrite_Types.isSubtypeOf(
                      checkedWrite_Actual, org.eclipse.milo.opcua.stack.core.NodeIds.Enumeration);
          if (checkedWrite_SpecializedPayloads
              && checkedWrite_Value.getClass().getComponentType() == java.lang.Object.class) {
            java.lang.Object[] checkedWrite_Payloads = (java.lang.Object[]) checkedWrite_Value;
            java.lang.Object checkedWrite_Projected =
                java.lang.reflect.Array.newInstance(
                    checkedWrite_EffectiveBacking, checkedWrite_Payloads.length);
            for (int checkedWrite_Index = 0;
                checkedWrite_Index < checkedWrite_Payloads.length;
                checkedWrite_Index++) {
              java.lang.Object checkedWrite_Payload = checkedWrite_Payloads[checkedWrite_Index];
              if (checkedWrite_Payload
                  instanceof
                  org.eclipse.milo.opcua.stack.core.types.builtin.OptionSetUInteger<?>
                      checkedWrite_Option) checkedWrite_Payload = checkedWrite_Option.getValue();
              java.lang.reflect.Array.set(
                  checkedWrite_Projected, checkedWrite_Index, checkedWrite_Payload);
            }
            checkedWrite_Value = checkedWrite_Projected;
          }
        }
        Object numericElements =
            checkedWrite_Value instanceof Matrix
                ? ((Matrix) checkedWrite_Value).getElements()
                : checkedWrite_Value;
        if (numericElements != null
            && numericElements.getClass().isArray()
            && (numericElements.getClass().getComponentType() == Number.class
                || numericElements.getClass().getComponentType() == UNumber.class)
            && (checkedWrite_Actual.equals(NodeIds.Number)
                || checkedWrite_Types.isSubtypeOf(checkedWrite_Actual, NodeIds.Number))) {
          Class<?> numericElementType = null;
          for (int numericIndex = 0;
              numericIndex < Array.getLength(numericElements);
              numericIndex++) {
            Object numericElement = Array.get(numericElements, numericIndex);
            if (numericElement != null) {
              if (numericElementType != null && numericElementType != numericElement.getClass()) {
                throw new UaException(
                    StatusCodes.Bad_TypeMismatch,
                    "An abstract numeric array requires one homogeneous wire element type");
              }
              numericElementType = numericElement.getClass();
            }
          }
          if (numericElementType == null) {
            numericElementType = checkedWrite_Types.getBackingClass(checkedWrite_Actual);
          }
          if (numericElementType == Number.class || numericElementType == UNumber.class) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "An empty or all-null abstract numeric array requires a concretely typed array");
          }
          Object numericArray =
              Array.newInstance(numericElementType, Array.getLength(numericElements));
          for (int numericIndex = 0;
              numericIndex < Array.getLength(numericElements);
              numericIndex++) {
            Array.set(numericArray, numericIndex, Array.get(numericElements, numericIndex));
          }
          if (checkedWrite_Value instanceof Matrix) {
            checkedWrite_Value =
                new Matrix(
                    numericArray,
                    ((Matrix) checkedWrite_Value).getDimensions().clone(),
                    ((Matrix) checkedWrite_Value)
                        .getDataType()
                        .orElseThrow(
                            () ->
                                new UaException(
                                    StatusCodes.Bad_TypeMismatch,
                                    "A numeric Matrix requires an explicit wire DataType")),
                    ((Matrix) checkedWrite_Value).getDataTypeId().orElse(null));
          } else {
            checkedWrite_Value = numericArray;
          }
        }

        if (checkedWrite_Value != null) {
          java.lang.Object checkedWrite_Elements =
              checkedWrite_Value
                      instanceof
                      org.eclipse.milo.opcua.stack.core.types.builtin.Matrix checkedWrite_Matrix
                  ? checkedWrite_Matrix.getElements()
                  : checkedWrite_Value;
          int checkedWrite_ValueRank =
              checkedWrite_Value
                      instanceof
                      org.eclipse.milo.opcua.stack.core.types.builtin.Matrix checkedWrite_Matrix
                  ? checkedWrite_Matrix.getValueRank()
                  : org.eclipse.milo.opcua.stack.core.util.ArrayUtil.getValueRank(
                      checkedWrite_Value);
          boolean checkedWrite_Empty =
              checkedWrite_Value.getClass().isArray()
                  && org.eclipse.milo.opcua.stack.core.util.ArrayUtil.getValueRank(
                          checkedWrite_Value)
                      == 1
                  && java.lang.reflect.Array.getLength(checkedWrite_Value) == 0;
          boolean checkedWrite_Shape =
              checkedWrite_Rank == -2
                  || checkedWrite_Rank == -3
                      && (checkedWrite_ValueRank == -1 || checkedWrite_ValueRank == 1)
                  || checkedWrite_Rank == -1 && checkedWrite_ValueRank == -1
                  || checkedWrite_Rank == 0 && checkedWrite_ValueRank >= 1
                  || checkedWrite_Rank > 0
                      && (checkedWrite_ValueRank == checkedWrite_Rank || checkedWrite_Empty);
          if (!checkedWrite_Shape)
            throw new org.eclipse.milo.opcua.stack.core.UaException(
                org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                "ValueRank mismatch");
          if (checkedWrite_Value
              instanceof
              org.eclipse.milo.opcua.stack.core.types.builtin.Matrix checkedWrite_Matrix) {
            int[] checkedWrite_Dimensions = checkedWrite_Matrix.getDimensions();
            if (checkedWrite_Dimensions.length < 2
                || checkedWrite_Elements == null
                || !checkedWrite_Elements.getClass().isArray()
                || org.eclipse.milo.opcua.stack.core.util.ArrayUtil.getValueRank(
                        checkedWrite_Elements)
                    != 1) {
              throw new org.eclipse.milo.opcua.stack.core.UaException(
                  org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                  "Malformed Matrix representation");
            }
            long checkedWrite_Count = 1;
            for (int checkedWrite_Dimension : checkedWrite_Dimensions) {
              if (checkedWrite_Dimension < 0 || checkedWrite_Count > java.lang.Integer.MAX_VALUE)
                throw new org.eclipse.milo.opcua.stack.core.UaException(
                    org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                    "Malformed Matrix dimensions");
              checkedWrite_Count *= checkedWrite_Dimension;
            }
            if (checkedWrite_Count != java.lang.reflect.Array.getLength(checkedWrite_Elements)
                || !checkedWrite_Matrix
                    .getDataType()
                    .equals(
                        org.eclipse.milo.opcua.stack.core.types.builtin.Variant.of(
                                checkedWrite_Elements)
                            .getDataType())) {
              throw new org.eclipse.milo.opcua.stack.core.UaException(
                  org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                  "Matrix dimensions or DataType do not match elements");
            }
          }
          if (!checkedWrite_Empty
              && checkedWrite_Bounds != null
              && checkedWrite_Bounds.length != 0) {
            int[] checkedWrite_Dimensions =
                checkedWrite_Value
                        instanceof
                        org.eclipse.milo.opcua.stack.core.types.builtin.Matrix checkedWrite_Matrix
                    ? checkedWrite_Matrix.getDimensions()
                    : org.eclipse.milo.opcua.stack.core.util.ArrayUtil.getDimensions(
                        checkedWrite_Value);
            if (checkedWrite_Dimensions.length != checkedWrite_Bounds.length)
              throw new org.eclipse.milo.opcua.stack.core.UaException(
                  org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                  "ArrayDimensions mismatch");
            for (int checkedWrite_Index = 0;
                checkedWrite_Index < checkedWrite_Dimensions.length;
                checkedWrite_Index++) {
              if (checkedWrite_Bounds[checkedWrite_Index].longValue() != 0
                  && checkedWrite_Dimensions[checkedWrite_Index]
                      > checkedWrite_Bounds[checkedWrite_Index].longValue())
                throw new org.eclipse.milo.opcua.stack.core.UaException(
                    org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                    "Value exceeds ArrayDimensions maximum");
            }
          }
          boolean checkedWrite_Array = checkedWrite_Elements.getClass().isArray();
          int checkedWrite_Length =
              checkedWrite_Array ? java.lang.reflect.Array.getLength(checkedWrite_Elements) : 1;
          boolean checkedWrite_Structure =
              checkedWrite_Actual.equals(org.eclipse.milo.opcua.stack.core.NodeIds.Structure)
                  || checkedWrite_Types.isStructType(checkedWrite_Actual);
          boolean checkedWrite_Enumeration =
              checkedWrite_Actual.equals(org.eclipse.milo.opcua.stack.core.NodeIds.Enumeration)
                  || checkedWrite_Types.isSubtypeOf(
                      checkedWrite_Actual, org.eclipse.milo.opcua.stack.core.NodeIds.Enumeration);
          boolean checkedWrite_Payloads =
              checkedWrite_Types.getBackingClass(checkedWrite_Actual)
                  == org.eclipse.milo.opcua.stack.core.types.builtin.Variant.class;
          boolean checkedWrite_PayloadArray =
              checkedWrite_Payloads
                  && checkedWrite_SelectedRank == 1
                  && checkedWrite_Types.getBackingClass(checkedWrite_Selected)
                      == org.eclipse.milo.opcua.stack.core.types.builtin.Variant.class;
          java.lang.Object checkedWrite_Converted = checkedWrite_Elements;
          if (checkedWrite_Structure) {
            var checkedWrite_Codec =
                checkedWrite_Context
                    .encodingContext()
                    .getDataTypeManager()
                    .getCodec(checkedWrite_Actual);
            java.lang.Class<?> checkedWrite_Class =
                checkedWrite_Codec == null
                    ? org.eclipse.milo.opcua.stack.core.types.UaStructuredType.class
                    : checkedWrite_Codec.getType();
            if (checkedWrite_Array)
              checkedWrite_Converted =
                  java.lang.reflect.Array.newInstance(checkedWrite_Class, checkedWrite_Length);
            for (int checkedWrite_Index = 0;
                checkedWrite_Index < checkedWrite_Length;
                checkedWrite_Index++) {
              java.lang.Object checkedWrite_Element =
                  checkedWrite_Array
                      ? java.lang.reflect.Array.get(checkedWrite_Elements, checkedWrite_Index)
                      : checkedWrite_Elements;
              if (checkedWrite_Element
                  instanceof
                  org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject
                      checkedWrite_Object) {
                checkedWrite_Element =
                    checkedWrite_Object.isNull()
                        ? null
                        : checkedWrite_Object.decode(checkedWrite_Context.encodingContext());
              }
              if (checkedWrite_Element != null) {
                if (!(checkedWrite_Element
                    instanceof org.eclipse.milo.opcua.stack.core.types.UaStructuredType))
                  throw new org.eclipse.milo.opcua.stack.core.UaException(
                      org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                      "Structure value required");
                var checkedWrite_TypeId =
                    ((org.eclipse.milo.opcua.stack.core.types.UaStructuredType)
                            checkedWrite_Element)
                        .getTypeId()
                        .toNodeId(checkedWrite_Context.namespaceTable())
                        .orElse(org.eclipse.milo.opcua.stack.core.types.builtin.NodeId.NULL_VALUE);
                boolean checkedWrite_Abstract =
                    checkedWrite_Actual.equals(org.eclipse.milo.opcua.stack.core.NodeIds.Structure)
                        || java.lang.Boolean.TRUE.equals(
                            checkedWrite_Types.getType(checkedWrite_Actual).isAbstract());
                if (!(checkedWrite_Abstract
                    ? checkedWrite_Types.isSubtypeOf(checkedWrite_TypeId, checkedWrite_Actual)
                    : checkedWrite_Actual.equals(checkedWrite_TypeId)))
                  throw new org.eclipse.milo.opcua.stack.core.UaException(
                      org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                      "Structure identity does not match the effective DataType");
              }
              if (checkedWrite_Array)
                java.lang.reflect.Array.set(
                    checkedWrite_Converted, checkedWrite_Index, checkedWrite_Element);
              else checkedWrite_Converted = checkedWrite_Element;
            }
          } else if (checkedWrite_Enumeration) {
            if (checkedWrite_Array)
              checkedWrite_Converted = new java.lang.Integer[checkedWrite_Length];
            for (int checkedWrite_Index = 0;
                checkedWrite_Index < checkedWrite_Length;
                checkedWrite_Index++) {
              java.lang.Object checkedWrite_Element =
                  checkedWrite_Array
                      ? java.lang.reflect.Array.get(checkedWrite_Elements, checkedWrite_Index)
                      : checkedWrite_Elements;
              if (checkedWrite_Element
                  instanceof
                  org.eclipse.milo.opcua.stack.core.types.UaEnumeratedType checkedWrite_Enum)
                checkedWrite_Element = checkedWrite_Enum.getValue();
              if (checkedWrite_Element != null
                  && !(checkedWrite_Element instanceof java.lang.Integer))
                throw new org.eclipse.milo.opcua.stack.core.UaException(
                    org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                    "Enumeration requires an Int32 value");
              if (checkedWrite_Element != null
                  && (checkedWrite_Context.enumValues() != null
                          && !checkedWrite_Context.enumValues().contains(checkedWrite_Element)
                      || checkedWrite_SelectedEnums != null
                          && !checkedWrite_SelectedEnums.contains(checkedWrite_Element)))
                throw new org.eclipse.milo.opcua.stack.core.UaException(
                    org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_OutOfRange,
                    "Unknown Enumeration value: " + checkedWrite_Element);
              if (checkedWrite_Wire && checkedWrite_Array && checkedWrite_Element == null)
                throw new org.eclipse.milo.opcua.stack.core.UaException(
                    org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                    "Enumeration wire arrays cannot contain null elements");
              if (checkedWrite_Array)
                java.lang.reflect.Array.set(
                    checkedWrite_Converted, checkedWrite_Index, checkedWrite_Element);
              else checkedWrite_Converted = checkedWrite_Element;
            }
          } else if (checkedWrite_PayloadArray) {
            if (!checkedWrite_Array
                || org.eclipse.milo.opcua.stack.core.util.ArrayUtil.getValueRank(
                        checkedWrite_Elements)
                    != 1)
              throw new org.eclipse.milo.opcua.stack.core.UaException(
                  org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                  "BaseDataType array requires Java payload values");
            checkedWrite_Converted =
                new org.eclipse.milo.opcua.stack.core.types.builtin.Variant[checkedWrite_Length];
            for (int checkedWrite_Index = 0;
                checkedWrite_Index < checkedWrite_Length;
                checkedWrite_Index++) {
              java.lang.Object checkedWrite_Element =
                  java.lang.reflect.Array.get(checkedWrite_Elements, checkedWrite_Index);
              if (checkedWrite_Wire)
                checkedWrite_Element =
                    org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject.encodeValue(
                        checkedWrite_Context.encodingContext(), checkedWrite_Element);
              ((org.eclipse.milo.opcua.stack.core.types.builtin.Variant[]) checkedWrite_Converted)
                      [checkedWrite_Index] =
                  org.eclipse.milo.opcua.stack.core.types.builtin.Variant.of(checkedWrite_Element);
            }
          } else {
            java.lang.Object checkedWrite_Check = checkedWrite_Elements;
            java.lang.Class<?> checkedWrite_ElementsClass =
                org.eclipse.milo.opcua.stack.core.util.ArrayUtil.getBoxedType(
                    checkedWrite_Elements);
            boolean checkedWrite_Options =
                org.eclipse.milo.opcua.stack.core.types.builtin.OptionSetUInteger.class
                    .isAssignableFrom(checkedWrite_ElementsClass);
            if (checkedWrite_Options) {
              java.lang.Class<?> checkedWrite_Backing =
                  org.eclipse.milo.opcua.stack.core.types.builtin.Variant.of(checkedWrite_Elements)
                      .getDataType()
                      .orElseThrow()
                      .getBackingClass();
              if (checkedWrite_Array)
                checkedWrite_Converted =
                    java.lang.reflect.Array.newInstance(checkedWrite_Backing, checkedWrite_Length);
              for (int checkedWrite_Index = 0;
                  checkedWrite_Index < checkedWrite_Length;
                  checkedWrite_Index++) {
                java.lang.Object checkedWrite_Element =
                    checkedWrite_Array
                        ? java.lang.reflect.Array.get(checkedWrite_Elements, checkedWrite_Index)
                        : checkedWrite_Elements;
                if (checkedWrite_Element != null)
                  checkedWrite_Element =
                      ((org.eclipse.milo.opcua.stack.core.types.builtin.OptionSetUInteger<?>)
                              checkedWrite_Element)
                          .getValue();
                if (checkedWrite_Wire && checkedWrite_Array && checkedWrite_Element == null)
                  throw new org.eclipse.milo.opcua.stack.core.UaException(
                      org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                      "OptionSet wire arrays cannot contain null elements");
                if (checkedWrite_Array)
                  java.lang.reflect.Array.set(
                      checkedWrite_Converted, checkedWrite_Index, checkedWrite_Element);
                else checkedWrite_Converted = checkedWrite_Element;
              }
              checkedWrite_Check = checkedWrite_Converted;
            }
            org.eclipse.milo.opcua.stack.core.types.builtin.Variant.of(checkedWrite_Check);
            var checkedWrite_Assignable =
                checkedWrite_Types.getBackingClass(checkedWrite_Actual) == java.lang.Number.class
                        && checkedWrite_Types.isSubtypeOf(
                            checkedWrite_Actual, org.eclipse.milo.opcua.stack.core.NodeIds.Integer)
                    ? org.eclipse.milo.opcua.stack.core.NodeIds.Integer
                    : checkedWrite_Actual;
            if (!checkedWrite_Payloads
                && checkedWrite_Check != null
                && !checkedWrite_Types.isAssignable(
                    checkedWrite_Assignable,
                    org.eclipse.milo.opcua.stack.core.util.ArrayUtil.getBoxedType(
                        checkedWrite_Check)))
              throw new org.eclipse.milo.opcua.stack.core.UaException(
                  org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                  "Value does not match effective DataType");
          }
          checkedWrite_Value =
              checkedWrite_Value
                      instanceof
                      org.eclipse.milo.opcua.stack.core.types.builtin.Matrix checkedWrite_Matrix
                  ? new org.eclipse.milo.opcua.stack.core.types.builtin.Matrix(
                      checkedWrite_Converted,
                      checkedWrite_Matrix.getDimensions().clone(),
                      checkedWrite_Matrix.getDataType().orElseThrow(),
                      checkedWrite_Matrix.getDataTypeId().orElse(null))
                  : checkedWrite_Converted;
          if (checkedWrite_Empty && checkedWrite_Rank > 1)
            checkedWrite_Value =
                new org.eclipse.milo.opcua.stack.core.types.builtin.Matrix(
                    checkedWrite_Converted, new int[checkedWrite_Rank]);
          if (checkedWrite_Wire) {
            var numericWireValues = new ArrayDeque<Object[]>();
            var numericWirePath = Collections.newSetFromMap(new IdentityHashMap<Object, Boolean>());
            if (checkedWrite_Value != null) {
              numericWireValues.push(new Object[] {checkedWrite_Value, false});
            }
            while (!numericWireValues.isEmpty()) {
              Object[] numericWireFrame = numericWireValues.pop();
              Object numericWireValue = numericWireFrame[0];
              if ((Boolean) numericWireFrame[1]) {
                numericWirePath.remove(numericWireValue);
                continue;
              }
              while (numericWireValue instanceof Variant || numericWireValue instanceof DataValue) {
                if (numericWireValue instanceof DataValue) {
                  if (((DataValue) numericWireValue).getValue() == null) {
                    throw new UaException(
                        StatusCodes.Bad_TypeMismatch,
                        "A DataValue requires a value wrapper; use Variant.NULL_VALUE for null");
                  }
                  if (((DataValue) numericWireValue).getStatusCode() == null) {
                    throw new UaException(
                        StatusCodes.Bad_TypeMismatch,
                        "A DataValue requires a StatusCode; use StatusCode.GOOD for Good");
                  }
                  numericWireValue = ((DataValue) numericWireValue).getValue();
                } else {
                  numericWireValue = ((Variant) numericWireValue).getValue();
                }
              }
              if (numericWireValue instanceof Matrix) {
                numericWireValue = ((Matrix) numericWireValue).getElements();
              }
              if (numericWireValue != null && numericWireValue.getClass().isArray()) {
                if (!numericWirePath.add(numericWireValue)) {
                  throw new UaException(
                      StatusCodes.Bad_TypeMismatch, "Cyclic Variant arrays cannot be encoded");
                }
                numericWireValues.push(new Object[] {numericWireValue, true});
                for (int numericWireIndex = 0;
                    numericWireIndex < Array.getLength(numericWireValue);
                    numericWireIndex++) {
                  Object numericWireElement = Array.get(numericWireValue, numericWireIndex);
                  if (numericWireElement == null
                      && ArrayUtil.getBoxedType(numericWireValue) == Variant.class) {
                    throw new UaException(
                        StatusCodes.Bad_TypeMismatch,
                        "A Variant wire array requires a wrapper for every element; use"
                            + " Variant.NULL_VALUE for null");
                  }
                  if (numericWireElement == null
                      && (UaEnumeratedType.class.isAssignableFrom(
                              ArrayUtil.getBoxedType(numericWireValue))
                          || OptionSetUInteger.class.isAssignableFrom(
                              ArrayUtil.getBoxedType(numericWireValue)))) {
                    throw new UaException(
                        StatusCodes.Bad_TypeMismatch,
                        "An enum or OptionSet wire array cannot encode a null element");
                  }
                  if (numericWireElement == null
                      && ArrayUtil.getBoxedType(numericWireValue) == Boolean.class) {
                    throw new UaException(
                        StatusCodes.Bad_TypeMismatch,
                        "A Boolean wire array cannot retain a null element; Milo encodes it as"
                            + " false");
                  }
                  if (numericWireElement == null
                      && ArrayUtil.getBoxedType(numericWireValue) == StatusCode.class) {
                    throw new UaException(
                        StatusCodes.Bad_TypeMismatch,
                        "A StatusCode wire array cannot retain a null element; Milo encodes it as"
                            + " Good");
                  }
                  if (numericWireElement == null
                      && Number.class.isAssignableFrom(ArrayUtil.getBoxedType(numericWireValue))) {
                    throw new UaException(
                        StatusCodes.Bad_TypeMismatch,
                        "A numeric wire array cannot retain a null element; Milo encodes it as"
                            + " zero");
                  }
                  if (numericWireElement instanceof Variant
                      || numericWireElement instanceof DataValue) {
                    numericWireValues.push(new Object[] {numericWireElement, false});
                  }
                }
              }
            }

            checkedWrite_Value =
                org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject.encodeValue(
                    checkedWrite_Context.encodingContext(), checkedWrite_Value);
          }
        }
        encoded = org.eclipse.milo.opcua.stack.core.types.builtin.Variant.of(checkedWrite_Value);
      } catch (org.eclipse.milo.opcua.stack.core.UaSerializationException checkedWrite_Failure) {
        long checkedWrite_Status =
            checkedWrite_Failure.getStatusCode().getValue()
                    == org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_OutOfRange
                ? org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_OutOfRange
                : org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch;
        throw new org.eclipse.milo.opcua.stack.core.UaException(
            checkedWrite_Status, checkedWrite_Failure);
      } catch (java.lang.IllegalArgumentException
          | java.lang.ClassCastException checkedWrite_Failure) {
        throw new org.eclipse.milo.opcua.stack.core.UaException(
            org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch, checkedWrite_Failure);
      }
    }
    views.checkOpen();
    child.setValue(encoded);
  }

  /**
   * Reads the existing member's value remotely. Only Good status is accepted, including Good
   * subcodes; Uncertain and Bad statuses fail before conversion. A Good null value is valid. An
   * absent node fails with Bad_NotFound. This does not update the wrapper's local value. Use {@code
   * views.variableNode(memberId).readValue()} on the owning ClientViews context to retain quality,
   * timestamps and unconverted values.
   *
   * <p>Concrete enum conversions reject unknown numbers with Bad_OutOfRange. Structured decoding
   * and existing rank/type checks retain their failures.
   *
   * @return the value, which may be null on a present member
   * @throws org.eclipse.milo.opcua.stack.core.UaException if a required node is absent, resolution
   *     fails, or a checked conversion fails; non-Good operation status also fails
   * @throws ClassCastException if a plain payload cast encounters an incompatible Java
   *     representation
   * @throws IllegalStateException if the view context is closed
   */
  @Override
  public @Nullable UInteger readTimeAwareOffset() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    return ViewFutures.await(readTimeAwareOffsetAsync());
  }

  private CompletableFuture<? extends @Nullable UInteger> readTimeAwareOffsetAsyncImplementation() {
    return ViewFutures.map(
        ViewFutures.compose(
            viewMember2Async(),
            child -> {
              if (child == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:TimeAwareOffset (declaration i=24194, owner"
                            + " i=24191) on "
                            + getNodeId()));
              }
              views.checkOpen();
              return child.readAttributeAsync(AttributeId.Value).thenApply(response -> response);
            }),
        value -> {
          views.checkOpen();
          if (value == null || value.getStatusCode() == null) {
            throw new CompletionException(new UaException(StatusCodes.Bad_UnexpectedError));
          }
          if (!value.getStatusCode().isGood()) {
            throw new CompletionException(new UaException(value.getStatusCode()));
          }
          try {
            @Nullable UInteger converted;
            {
              Object rawValue = value.getValue().getValue();
              if (rawValue instanceof Matrix matrix && matrix.isNull()) {
                rawValue = null;
              }
              if (rawValue != null) {
                int actualRank =
                    rawValue instanceof Matrix matrix
                        ? matrix.getValueRank()
                        : ArrayUtil.getValueRank(rawValue);
                Object rankElements =
                    rawValue instanceof Matrix matrix ? matrix.getElements() : rawValue;
                if (!(actualRank == -1)) {
                  throw new UaException(
                      StatusCodes.Bad_TypeMismatch,
                      "http://opcfoundation.org/UA/:TimeAwareOffset (declaration i=24194, owner"
                          + " i=24191)");
                }
                if (rawValue instanceof Matrix matrix) {
                  if (actualRank < 2
                      || rankElements == null
                      || !rankElements.getClass().isArray()
                      || ArrayUtil.getValueRank(rankElements) != 1) {
                    throw new UaException(
                        StatusCodes.Bad_TypeMismatch,
                        "http://opcfoundation.org/UA/:TimeAwareOffset (declaration i=24194, owner"
                            + " i=24191)");
                  }
                  long elementCount = 1;
                  for (int dimension : matrix.getDimensions()) {
                    if (dimension < 0 || elementCount > Integer.MAX_VALUE) {
                      throw new UaException(
                          StatusCodes.Bad_TypeMismatch,
                          "http://opcfoundation.org/UA/:TimeAwareOffset (declaration i=24194, owner"
                              + " i=24191)");
                    }
                    elementCount *= dimension;
                  }
                  if (elementCount != Array.getLength(rankElements)) {
                    throw new UaException(
                        StatusCodes.Bad_TypeMismatch,
                        "http://opcfoundation.org/UA/:TimeAwareOffset (declaration i=24194, owner"
                            + " i=24191)");
                  }
                } else if (actualRank > 1) {
                  throw new UaException(
                      StatusCodes.Bad_TypeMismatch,
                      "http://opcfoundation.org/UA/:TimeAwareOffset (declaration i=24194, owner"
                          + " i=24191): use Matrix for multiple dimensions");
                }
              }
              Object element = rawValue;
              if (element != null && !(element instanceof UInteger)) {
                throw new UaException(
                    StatusCodes.Bad_TypeMismatch,
                    "http://opcfoundation.org/UA/:TimeAwareOffset (declaration i=24194, owner"
                        + " i=24191)");
              }
              converted = (UInteger) element;
            }
            return converted;
          } catch (UaException failure) {
            throw new CompletionException(failure);
          }
        });
  }

  /**
   * Reads the existing member's value remotely. Only Good status is accepted, including Good
   * subcodes; Uncertain and Bad statuses fail before conversion. A Good null value is valid. An
   * absent node fails with Bad_NotFound. This does not update the wrapper's local value. Use {@code
   * views.variableNode(memberId).readValue()} on the owning ClientViews context to retain quality,
   * timestamps and unconverted values.
   *
   * <p>Concrete enum conversions reject unknown numbers with Bad_OutOfRange. Structured decoding
   * and existing rank/type checks retain their failures.
   *
   * <p>Lookup, conversion and service failures complete the future exceptionally. UaException
   * causes preserve OPC UA status. Incompatible plain payload casts can complete exceptionally with
   * ClassCastException. Cancellation does not promise transport cancellation or rollback.
   *
   * <p>A closed view context completes the future exceptionally with IllegalStateException.
   *
   * @return a nonnull future completing with the value, which may be null on a present member
   */
  @Override
  public CompletableFuture<? extends @Nullable UInteger> readTimeAwareOffsetAsync() {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.readTimeAwareOffsetAsyncImplementation());
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }

  /**
   * Writes the existing member's value remotely. A null value is valid. An absent node fails with
   * Bad_NotFound before conversion or Write. This does not create nodes or update the wrapper's
   * local value.
   *
   * <p>Concrete enum conversions reject unknown numbers with Bad_OutOfRange. Structured decoding
   * and existing rank/type checks retain their failures.
   *
   * <p>The actual member DataType, rank and maximum dimensions constrain this selected view write.
   * Incompatible values fail with Bad_TypeMismatch; values outside the actual finite enumeration
   * fail with Bad_OutOfRange. Validation finishes before mutation or Write. Type and enumeration
   * discovery can perform service I/O, including for local setters.
   *
   * @param value the value to store; null is permitted
   * @throws org.eclipse.milo.opcua.stack.core.UaException if a required node is absent, resolution
   *     fails, or a checked conversion fails; non-Good operation status also fails
   * @throws IllegalStateException if the view context is closed
   */
  @Override
  public void writeTimeAwareOffset(@Nullable UInteger value) throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    views.checkOpen();
    StatusCode status = ViewFutures.await(writeTimeAwareOffsetAsync(value));
    if (status == null) {
      throw new UaException(StatusCodes.Bad_UnexpectedError);
    }
    if (!status.isGood()) {
      throw new UaException(status);
    }
  }

  private CompletableFuture<StatusCode> writeTimeAwareOffsetAsyncImplementation(
      @Nullable UInteger value) {
    return ViewFutures.compose(
        viewMember2Async(),
        child -> {
          if (child == null) {
            throw new CompletionException(
                new UaException(
                    StatusCodes.Bad_NotFound,
                    "http://opcfoundation.org/UA/:TimeAwareOffset (declaration i=24194, owner"
                        + " i=24191) on "
                        + getNodeId()));
          }
          return ViewFutures.compose(
              views.readWriteContextAsync(child.getNodeId(), value, KNOWN_ENUMS),
              writeContext -> {
                try {
                  Variant encoded;
                  {
                    var checkedWrite_Context = writeContext;
                    Object checkedWrite_Value = value;
                    var checkedWrite_Selected =
                        ExpandedNodeId.parse("i=7")
                            .toNodeId(checkedWrite_Context.namespaceTable())
                            .orElseThrow(
                                () ->
                                    new org.eclipse.milo.opcua.stack.core.UaException(
                                        org.eclipse.milo.opcua.stack.core.StatusCodes
                                            .Bad_NodeIdInvalid,
                                        "Unknown selected DataType namespace:"
                                            + " ExpandedNodeId[server=ServerIndex[serverIndex=0],"
                                            + " namespace=NamespaceUri[namespaceUri=http://opcfoundation.org/UA/],"
                                            + " identifier=7]"));
                    int checkedWrite_SelectedRank = -1;
                    long[] checkedWrite_SelectedDimensions = new long[] {};
                    Set<Integer> checkedWrite_SelectedEnums = null;
                    boolean checkedWrite_Wire = true;
                    var checkedWrite_Types = checkedWrite_Context.dataTypes();
                    var checkedWrite_Actual = checkedWrite_Context.dataType();
                    int checkedWrite_Rank = checkedWrite_Context.valueRank();
                    var checkedWrite_Bounds = checkedWrite_Context.arrayDimensions();
                    if (!checkedWrite_Types.containsType(checkedWrite_Selected)
                        || !checkedWrite_Types.containsType(checkedWrite_Actual)
                        || !(checkedWrite_Actual.equals(checkedWrite_Selected)
                            || checkedWrite_Types.isSubtypeOf(
                                checkedWrite_Actual, checkedWrite_Selected))) {
                      throw new org.eclipse.milo.opcua.stack.core.UaException(
                          org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                          "Effective DataType "
                              + checkedWrite_Actual
                              + " does not specialize selected "
                              + checkedWrite_Selected);
                    }
                    boolean checkedWrite_SpecializedRank =
                        checkedWrite_SelectedRank == -2
                            || checkedWrite_Rank == checkedWrite_SelectedRank
                            || checkedWrite_SelectedRank == -3
                                && (checkedWrite_Rank == -1 || checkedWrite_Rank == 1)
                            || checkedWrite_SelectedRank == 0 && checkedWrite_Rank > 0;
                    if (!checkedWrite_SpecializedRank
                        || checkedWrite_Rank < -3
                        || checkedWrite_Bounds != null
                            && checkedWrite_Bounds.length != 0
                            && (checkedWrite_Rank <= 0
                                || checkedWrite_Bounds.length != checkedWrite_Rank)) {
                      throw new org.eclipse.milo.opcua.stack.core.UaException(
                          org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                          "Effective ValueRank or ArrayDimensions conflict with the selected"
                              + " contract");
                    }
                    if (checkedWrite_SelectedDimensions.length != 0) {
                      if (checkedWrite_SelectedRank <= 0
                          || checkedWrite_SelectedDimensions.length != checkedWrite_SelectedRank) {
                        throw new org.eclipse.milo.opcua.stack.core.UaException(
                            org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                            "Invalid selected ArrayDimensions");
                      }
                      for (int checkedWrite_Index = 0;
                          checkedWrite_Index < checkedWrite_SelectedDimensions.length;
                          checkedWrite_Index++) {
                        long checkedWrite_Maximum =
                            checkedWrite_SelectedDimensions[checkedWrite_Index];
                        if (checkedWrite_Maximum != 0
                            && (checkedWrite_Bounds == null
                                || checkedWrite_Bounds.length
                                    != checkedWrite_SelectedDimensions.length
                                || checkedWrite_Bounds[checkedWrite_Index].longValue() == 0
                                || checkedWrite_Bounds[checkedWrite_Index].longValue()
                                    > checkedWrite_Maximum)) {
                          throw new org.eclipse.milo.opcua.stack.core.UaException(
                              org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                              "Effective ArrayDimensions broaden the selected maximum");
                        }
                      }
                    }
                    if (checkedWrite_SelectedEnums != null
                        && (checkedWrite_Context.enumValues() == null
                            || !checkedWrite_SelectedEnums.containsAll(
                                checkedWrite_Context.enumValues()))) {
                      throw new org.eclipse.milo.opcua.stack.core.UaException(
                          org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                          "Effective Enumeration domain broadens the selected contract");
                    }
                    try {
                      if (checkedWrite_Value
                              instanceof
                              org.eclipse.milo.opcua.stack.core.types.builtin.Matrix
                                  checkedWrite_Matrix
                          && checkedWrite_Matrix.isNull()) {
                        checkedWrite_Value = null;
                      }
                      if (checkedWrite_Value != null
                          && checkedWrite_SelectedRank == 1
                          && checkedWrite_Types.getBackingClass(checkedWrite_Selected)
                              == org.eclipse.milo.opcua.stack.core.types.builtin.Variant.class) {
                        if (!(checkedWrite_Value instanceof java.lang.Object[])
                            || org.eclipse.milo.opcua.stack.core.util.ArrayUtil.getValueRank(
                                    checkedWrite_Value)
                                != 1)
                          throw new org.eclipse.milo.opcua.stack.core.UaException(
                              org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                              "Selected BaseDataType array requires Java payload values");
                        for (java.lang.Object checkedWrite_Payload :
                            (java.lang.Object[]) checkedWrite_Value)
                          org.eclipse.milo.opcua.stack.core.types.builtin.Variant.of(
                              checkedWrite_Payload);
                        java.lang.Class<?> checkedWrite_EffectiveBacking =
                            checkedWrite_Types.getBackingClass(checkedWrite_Actual);
                        boolean checkedWrite_SpecializedPayloads =
                            checkedWrite_EffectiveBacking
                                    != org.eclipse.milo.opcua.stack.core.types.builtin.Variant.class
                                && !checkedWrite_Actual.equals(
                                    org.eclipse.milo.opcua.stack.core.NodeIds.Structure)
                                && !checkedWrite_Types.isStructType(checkedWrite_Actual)
                                && !checkedWrite_Actual.equals(
                                    org.eclipse.milo.opcua.stack.core.NodeIds.Enumeration)
                                && !checkedWrite_Types.isSubtypeOf(
                                    checkedWrite_Actual,
                                    org.eclipse.milo.opcua.stack.core.NodeIds.Enumeration);
                        if (checkedWrite_SpecializedPayloads
                            && checkedWrite_Value.getClass().getComponentType()
                                == java.lang.Object.class) {
                          java.lang.Object[] checkedWrite_Payloads =
                              (java.lang.Object[]) checkedWrite_Value;
                          java.lang.Object checkedWrite_Projected =
                              java.lang.reflect.Array.newInstance(
                                  checkedWrite_EffectiveBacking, checkedWrite_Payloads.length);
                          for (int checkedWrite_Index = 0;
                              checkedWrite_Index < checkedWrite_Payloads.length;
                              checkedWrite_Index++) {
                            java.lang.Object checkedWrite_Payload =
                                checkedWrite_Payloads[checkedWrite_Index];
                            if (checkedWrite_Payload
                                instanceof
                                org.eclipse.milo.opcua.stack.core.types.builtin.OptionSetUInteger<?>
                                    checkedWrite_Option)
                              checkedWrite_Payload = checkedWrite_Option.getValue();
                            java.lang.reflect.Array.set(
                                checkedWrite_Projected, checkedWrite_Index, checkedWrite_Payload);
                          }
                          checkedWrite_Value = checkedWrite_Projected;
                        }
                      }
                      Object numericElements =
                          checkedWrite_Value instanceof Matrix
                              ? ((Matrix) checkedWrite_Value).getElements()
                              : checkedWrite_Value;
                      if (numericElements != null
                          && numericElements.getClass().isArray()
                          && (numericElements.getClass().getComponentType() == Number.class
                              || numericElements.getClass().getComponentType() == UNumber.class)
                          && (checkedWrite_Actual.equals(NodeIds.Number)
                              || checkedWrite_Types.isSubtypeOf(
                                  checkedWrite_Actual, NodeIds.Number))) {
                        Class<?> numericElementType = null;
                        for (int numericIndex = 0;
                            numericIndex < Array.getLength(numericElements);
                            numericIndex++) {
                          Object numericElement = Array.get(numericElements, numericIndex);
                          if (numericElement != null) {
                            if (numericElementType != null
                                && numericElementType != numericElement.getClass()) {
                              throw new UaException(
                                  StatusCodes.Bad_TypeMismatch,
                                  "An abstract numeric array requires one homogeneous wire element"
                                      + " type");
                            }
                            numericElementType = numericElement.getClass();
                          }
                        }
                        if (numericElementType == null) {
                          numericElementType =
                              checkedWrite_Types.getBackingClass(checkedWrite_Actual);
                        }
                        if (numericElementType == Number.class
                            || numericElementType == UNumber.class) {
                          throw new UaException(
                              StatusCodes.Bad_TypeMismatch,
                              "An empty or all-null abstract numeric array requires a concretely"
                                  + " typed array");
                        }
                        Object numericArray =
                            Array.newInstance(numericElementType, Array.getLength(numericElements));
                        for (int numericIndex = 0;
                            numericIndex < Array.getLength(numericElements);
                            numericIndex++) {
                          Array.set(
                              numericArray, numericIndex, Array.get(numericElements, numericIndex));
                        }
                        if (checkedWrite_Value instanceof Matrix) {
                          checkedWrite_Value =
                              new Matrix(
                                  numericArray,
                                  ((Matrix) checkedWrite_Value).getDimensions().clone(),
                                  ((Matrix) checkedWrite_Value)
                                      .getDataType()
                                      .orElseThrow(
                                          () ->
                                              new UaException(
                                                  StatusCodes.Bad_TypeMismatch,
                                                  "A numeric Matrix requires an explicit wire"
                                                      + " DataType")),
                                  ((Matrix) checkedWrite_Value).getDataTypeId().orElse(null));
                        } else {
                          checkedWrite_Value = numericArray;
                        }
                      }

                      if (checkedWrite_Value != null) {
                        java.lang.Object checkedWrite_Elements =
                            checkedWrite_Value
                                    instanceof
                                    org.eclipse.milo.opcua.stack.core.types.builtin.Matrix
                                        checkedWrite_Matrix
                                ? checkedWrite_Matrix.getElements()
                                : checkedWrite_Value;
                        int checkedWrite_ValueRank =
                            checkedWrite_Value
                                    instanceof
                                    org.eclipse.milo.opcua.stack.core.types.builtin.Matrix
                                        checkedWrite_Matrix
                                ? checkedWrite_Matrix.getValueRank()
                                : org.eclipse.milo.opcua.stack.core.util.ArrayUtil.getValueRank(
                                    checkedWrite_Value);
                        boolean checkedWrite_Empty =
                            checkedWrite_Value.getClass().isArray()
                                && org.eclipse.milo.opcua.stack.core.util.ArrayUtil.getValueRank(
                                        checkedWrite_Value)
                                    == 1
                                && java.lang.reflect.Array.getLength(checkedWrite_Value) == 0;
                        boolean checkedWrite_Shape =
                            checkedWrite_Rank == -2
                                || checkedWrite_Rank == -3
                                    && (checkedWrite_ValueRank == -1 || checkedWrite_ValueRank == 1)
                                || checkedWrite_Rank == -1 && checkedWrite_ValueRank == -1
                                || checkedWrite_Rank == 0 && checkedWrite_ValueRank >= 1
                                || checkedWrite_Rank > 0
                                    && (checkedWrite_ValueRank == checkedWrite_Rank
                                        || checkedWrite_Empty);
                        if (!checkedWrite_Shape)
                          throw new org.eclipse.milo.opcua.stack.core.UaException(
                              org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                              "ValueRank mismatch");
                        if (checkedWrite_Value
                            instanceof
                            org.eclipse.milo.opcua.stack.core.types.builtin.Matrix
                                checkedWrite_Matrix) {
                          int[] checkedWrite_Dimensions = checkedWrite_Matrix.getDimensions();
                          if (checkedWrite_Dimensions.length < 2
                              || checkedWrite_Elements == null
                              || !checkedWrite_Elements.getClass().isArray()
                              || org.eclipse.milo.opcua.stack.core.util.ArrayUtil.getValueRank(
                                      checkedWrite_Elements)
                                  != 1) {
                            throw new org.eclipse.milo.opcua.stack.core.UaException(
                                org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                                "Malformed Matrix representation");
                          }
                          long checkedWrite_Count = 1;
                          for (int checkedWrite_Dimension : checkedWrite_Dimensions) {
                            if (checkedWrite_Dimension < 0
                                || checkedWrite_Count > java.lang.Integer.MAX_VALUE)
                              throw new org.eclipse.milo.opcua.stack.core.UaException(
                                  org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                                  "Malformed Matrix dimensions");
                            checkedWrite_Count *= checkedWrite_Dimension;
                          }
                          if (checkedWrite_Count
                                  != java.lang.reflect.Array.getLength(checkedWrite_Elements)
                              || !checkedWrite_Matrix
                                  .getDataType()
                                  .equals(
                                      org.eclipse.milo.opcua.stack.core.types.builtin.Variant.of(
                                              checkedWrite_Elements)
                                          .getDataType())) {
                            throw new org.eclipse.milo.opcua.stack.core.UaException(
                                org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                                "Matrix dimensions or DataType do not match elements");
                          }
                        }
                        if (!checkedWrite_Empty
                            && checkedWrite_Bounds != null
                            && checkedWrite_Bounds.length != 0) {
                          int[] checkedWrite_Dimensions =
                              checkedWrite_Value
                                      instanceof
                                      org.eclipse.milo.opcua.stack.core.types.builtin.Matrix
                                          checkedWrite_Matrix
                                  ? checkedWrite_Matrix.getDimensions()
                                  : org.eclipse.milo.opcua.stack.core.util.ArrayUtil.getDimensions(
                                      checkedWrite_Value);
                          if (checkedWrite_Dimensions.length != checkedWrite_Bounds.length)
                            throw new org.eclipse.milo.opcua.stack.core.UaException(
                                org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                                "ArrayDimensions mismatch");
                          for (int checkedWrite_Index = 0;
                              checkedWrite_Index < checkedWrite_Dimensions.length;
                              checkedWrite_Index++) {
                            if (checkedWrite_Bounds[checkedWrite_Index].longValue() != 0
                                && checkedWrite_Dimensions[checkedWrite_Index]
                                    > checkedWrite_Bounds[checkedWrite_Index].longValue())
                              throw new org.eclipse.milo.opcua.stack.core.UaException(
                                  org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                                  "Value exceeds ArrayDimensions maximum");
                          }
                        }
                        boolean checkedWrite_Array = checkedWrite_Elements.getClass().isArray();
                        int checkedWrite_Length =
                            checkedWrite_Array
                                ? java.lang.reflect.Array.getLength(checkedWrite_Elements)
                                : 1;
                        boolean checkedWrite_Structure =
                            checkedWrite_Actual.equals(
                                    org.eclipse.milo.opcua.stack.core.NodeIds.Structure)
                                || checkedWrite_Types.isStructType(checkedWrite_Actual);
                        boolean checkedWrite_Enumeration =
                            checkedWrite_Actual.equals(
                                    org.eclipse.milo.opcua.stack.core.NodeIds.Enumeration)
                                || checkedWrite_Types.isSubtypeOf(
                                    checkedWrite_Actual,
                                    org.eclipse.milo.opcua.stack.core.NodeIds.Enumeration);
                        boolean checkedWrite_Payloads =
                            checkedWrite_Types.getBackingClass(checkedWrite_Actual)
                                == org.eclipse.milo.opcua.stack.core.types.builtin.Variant.class;
                        boolean checkedWrite_PayloadArray =
                            checkedWrite_Payloads
                                && checkedWrite_SelectedRank == 1
                                && checkedWrite_Types.getBackingClass(checkedWrite_Selected)
                                    == org.eclipse.milo.opcua.stack.core.types.builtin.Variant
                                        .class;
                        java.lang.Object checkedWrite_Converted = checkedWrite_Elements;
                        if (checkedWrite_Structure) {
                          var checkedWrite_Codec =
                              checkedWrite_Context
                                  .encodingContext()
                                  .getDataTypeManager()
                                  .getCodec(checkedWrite_Actual);
                          java.lang.Class<?> checkedWrite_Class =
                              checkedWrite_Codec == null
                                  ? org.eclipse.milo.opcua.stack.core.types.UaStructuredType.class
                                  : checkedWrite_Codec.getType();
                          if (checkedWrite_Array)
                            checkedWrite_Converted =
                                java.lang.reflect.Array.newInstance(
                                    checkedWrite_Class, checkedWrite_Length);
                          for (int checkedWrite_Index = 0;
                              checkedWrite_Index < checkedWrite_Length;
                              checkedWrite_Index++) {
                            java.lang.Object checkedWrite_Element =
                                checkedWrite_Array
                                    ? java.lang.reflect.Array.get(
                                        checkedWrite_Elements, checkedWrite_Index)
                                    : checkedWrite_Elements;
                            if (checkedWrite_Element
                                instanceof
                                org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject
                                    checkedWrite_Object) {
                              checkedWrite_Element =
                                  checkedWrite_Object.isNull()
                                      ? null
                                      : checkedWrite_Object.decode(
                                          checkedWrite_Context.encodingContext());
                            }
                            if (checkedWrite_Element != null) {
                              if (!(checkedWrite_Element
                                  instanceof
                                  org.eclipse.milo.opcua.stack.core.types.UaStructuredType))
                                throw new org.eclipse.milo.opcua.stack.core.UaException(
                                    org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                                    "Structure value required");
                              var checkedWrite_TypeId =
                                  ((org.eclipse.milo.opcua.stack.core.types.UaStructuredType)
                                          checkedWrite_Element)
                                      .getTypeId()
                                      .toNodeId(checkedWrite_Context.namespaceTable())
                                      .orElse(
                                          org.eclipse.milo.opcua.stack.core.types.builtin.NodeId
                                              .NULL_VALUE);
                              boolean checkedWrite_Abstract =
                                  checkedWrite_Actual.equals(
                                          org.eclipse.milo.opcua.stack.core.NodeIds.Structure)
                                      || java.lang.Boolean.TRUE.equals(
                                          checkedWrite_Types
                                              .getType(checkedWrite_Actual)
                                              .isAbstract());
                              if (!(checkedWrite_Abstract
                                  ? checkedWrite_Types.isSubtypeOf(
                                      checkedWrite_TypeId, checkedWrite_Actual)
                                  : checkedWrite_Actual.equals(checkedWrite_TypeId)))
                                throw new org.eclipse.milo.opcua.stack.core.UaException(
                                    org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                                    "Structure identity does not match the effective DataType");
                            }
                            if (checkedWrite_Array)
                              java.lang.reflect.Array.set(
                                  checkedWrite_Converted, checkedWrite_Index, checkedWrite_Element);
                            else checkedWrite_Converted = checkedWrite_Element;
                          }
                        } else if (checkedWrite_Enumeration) {
                          if (checkedWrite_Array)
                            checkedWrite_Converted = new java.lang.Integer[checkedWrite_Length];
                          for (int checkedWrite_Index = 0;
                              checkedWrite_Index < checkedWrite_Length;
                              checkedWrite_Index++) {
                            java.lang.Object checkedWrite_Element =
                                checkedWrite_Array
                                    ? java.lang.reflect.Array.get(
                                        checkedWrite_Elements, checkedWrite_Index)
                                    : checkedWrite_Elements;
                            if (checkedWrite_Element
                                instanceof
                                org.eclipse.milo.opcua.stack.core.types.UaEnumeratedType
                                    checkedWrite_Enum)
                              checkedWrite_Element = checkedWrite_Enum.getValue();
                            if (checkedWrite_Element != null
                                && !(checkedWrite_Element instanceof java.lang.Integer))
                              throw new org.eclipse.milo.opcua.stack.core.UaException(
                                  org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                                  "Enumeration requires an Int32 value");
                            if (checkedWrite_Element != null
                                && (checkedWrite_Context.enumValues() != null
                                        && !checkedWrite_Context
                                            .enumValues()
                                            .contains(checkedWrite_Element)
                                    || checkedWrite_SelectedEnums != null
                                        && !checkedWrite_SelectedEnums.contains(
                                            checkedWrite_Element)))
                              throw new org.eclipse.milo.opcua.stack.core.UaException(
                                  org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_OutOfRange,
                                  "Unknown Enumeration value: " + checkedWrite_Element);
                            if (checkedWrite_Wire
                                && checkedWrite_Array
                                && checkedWrite_Element == null)
                              throw new org.eclipse.milo.opcua.stack.core.UaException(
                                  org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                                  "Enumeration wire arrays cannot contain null elements");
                            if (checkedWrite_Array)
                              java.lang.reflect.Array.set(
                                  checkedWrite_Converted, checkedWrite_Index, checkedWrite_Element);
                            else checkedWrite_Converted = checkedWrite_Element;
                          }
                        } else if (checkedWrite_PayloadArray) {
                          if (!checkedWrite_Array
                              || org.eclipse.milo.opcua.stack.core.util.ArrayUtil.getValueRank(
                                      checkedWrite_Elements)
                                  != 1)
                            throw new org.eclipse.milo.opcua.stack.core.UaException(
                                org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                                "BaseDataType array requires Java payload values");
                          checkedWrite_Converted =
                              new org.eclipse.milo.opcua.stack.core.types.builtin.Variant
                                  [checkedWrite_Length];
                          for (int checkedWrite_Index = 0;
                              checkedWrite_Index < checkedWrite_Length;
                              checkedWrite_Index++) {
                            java.lang.Object checkedWrite_Element =
                                java.lang.reflect.Array.get(
                                    checkedWrite_Elements, checkedWrite_Index);
                            if (checkedWrite_Wire)
                              checkedWrite_Element =
                                  org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject
                                      .encodeValue(
                                          checkedWrite_Context.encodingContext(),
                                          checkedWrite_Element);
                            ((org.eclipse.milo.opcua.stack.core.types.builtin.Variant[])
                                        checkedWrite_Converted)
                                    [checkedWrite_Index] =
                                org.eclipse.milo.opcua.stack.core.types.builtin.Variant.of(
                                    checkedWrite_Element);
                          }
                        } else {
                          java.lang.Object checkedWrite_Check = checkedWrite_Elements;
                          java.lang.Class<?> checkedWrite_ElementsClass =
                              org.eclipse.milo.opcua.stack.core.util.ArrayUtil.getBoxedType(
                                  checkedWrite_Elements);
                          boolean checkedWrite_Options =
                              org.eclipse.milo.opcua.stack.core.types.builtin.OptionSetUInteger
                                  .class
                                  .isAssignableFrom(checkedWrite_ElementsClass);
                          if (checkedWrite_Options) {
                            java.lang.Class<?> checkedWrite_Backing =
                                org.eclipse.milo.opcua.stack.core.types.builtin.Variant.of(
                                        checkedWrite_Elements)
                                    .getDataType()
                                    .orElseThrow()
                                    .getBackingClass();
                            if (checkedWrite_Array)
                              checkedWrite_Converted =
                                  java.lang.reflect.Array.newInstance(
                                      checkedWrite_Backing, checkedWrite_Length);
                            for (int checkedWrite_Index = 0;
                                checkedWrite_Index < checkedWrite_Length;
                                checkedWrite_Index++) {
                              java.lang.Object checkedWrite_Element =
                                  checkedWrite_Array
                                      ? java.lang.reflect.Array.get(
                                          checkedWrite_Elements, checkedWrite_Index)
                                      : checkedWrite_Elements;
                              if (checkedWrite_Element != null)
                                checkedWrite_Element =
                                    ((org.eclipse.milo.opcua.stack.core.types.builtin
                                                    .OptionSetUInteger<
                                                ?>)
                                            checkedWrite_Element)
                                        .getValue();
                              if (checkedWrite_Wire
                                  && checkedWrite_Array
                                  && checkedWrite_Element == null)
                                throw new org.eclipse.milo.opcua.stack.core.UaException(
                                    org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                                    "OptionSet wire arrays cannot contain null elements");
                              if (checkedWrite_Array)
                                java.lang.reflect.Array.set(
                                    checkedWrite_Converted,
                                    checkedWrite_Index,
                                    checkedWrite_Element);
                              else checkedWrite_Converted = checkedWrite_Element;
                            }
                            checkedWrite_Check = checkedWrite_Converted;
                          }
                          org.eclipse.milo.opcua.stack.core.types.builtin.Variant.of(
                              checkedWrite_Check);
                          var checkedWrite_Assignable =
                              checkedWrite_Types.getBackingClass(checkedWrite_Actual)
                                          == java.lang.Number.class
                                      && checkedWrite_Types.isSubtypeOf(
                                          checkedWrite_Actual,
                                          org.eclipse.milo.opcua.stack.core.NodeIds.Integer)
                                  ? org.eclipse.milo.opcua.stack.core.NodeIds.Integer
                                  : checkedWrite_Actual;
                          if (!checkedWrite_Payloads
                              && checkedWrite_Check != null
                              && !checkedWrite_Types.isAssignable(
                                  checkedWrite_Assignable,
                                  org.eclipse.milo.opcua.stack.core.util.ArrayUtil.getBoxedType(
                                      checkedWrite_Check)))
                            throw new org.eclipse.milo.opcua.stack.core.UaException(
                                org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                                "Value does not match effective DataType");
                        }
                        checkedWrite_Value =
                            checkedWrite_Value
                                    instanceof
                                    org.eclipse.milo.opcua.stack.core.types.builtin.Matrix
                                        checkedWrite_Matrix
                                ? new org.eclipse.milo.opcua.stack.core.types.builtin.Matrix(
                                    checkedWrite_Converted,
                                    checkedWrite_Matrix.getDimensions().clone(),
                                    checkedWrite_Matrix.getDataType().orElseThrow(),
                                    checkedWrite_Matrix.getDataTypeId().orElse(null))
                                : checkedWrite_Converted;
                        if (checkedWrite_Empty && checkedWrite_Rank > 1)
                          checkedWrite_Value =
                              new org.eclipse.milo.opcua.stack.core.types.builtin.Matrix(
                                  checkedWrite_Converted, new int[checkedWrite_Rank]);
                        if (checkedWrite_Wire) {
                          var numericWireValues = new ArrayDeque<Object[]>();
                          var numericWirePath =
                              Collections.newSetFromMap(new IdentityHashMap<Object, Boolean>());
                          if (checkedWrite_Value != null) {
                            numericWireValues.push(new Object[] {checkedWrite_Value, false});
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
                                      "A DataValue requires a value wrapper; use Variant.NULL_VALUE"
                                          + " for null");
                                }
                                if (((DataValue) numericWireValue).getStatusCode() == null) {
                                  throw new UaException(
                                      StatusCodes.Bad_TypeMismatch,
                                      "A DataValue requires a StatusCode; use StatusCode.GOOD for"
                                          + " Good");
                                }
                                numericWireValue = ((DataValue) numericWireValue).getValue();
                              } else {
                                numericWireValue = ((Variant) numericWireValue).getValue();
                              }
                            }
                            if (numericWireValue instanceof Matrix) {
                              numericWireValue = ((Matrix) numericWireValue).getElements();
                            }
                            if (numericWireValue != null && numericWireValue.getClass().isArray()) {
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
                                    && ArrayUtil.getBoxedType(numericWireValue) == Variant.class) {
                                  throw new UaException(
                                      StatusCodes.Bad_TypeMismatch,
                                      "A Variant wire array requires a wrapper for every element;"
                                          + " use Variant.NULL_VALUE for null");
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
                                    && ArrayUtil.getBoxedType(numericWireValue) == Boolean.class) {
                                  throw new UaException(
                                      StatusCodes.Bad_TypeMismatch,
                                      "A Boolean wire array cannot retain a null element; Milo"
                                          + " encodes it as false");
                                }
                                if (numericWireElement == null
                                    && ArrayUtil.getBoxedType(numericWireValue)
                                        == StatusCode.class) {
                                  throw new UaException(
                                      StatusCodes.Bad_TypeMismatch,
                                      "A StatusCode wire array cannot retain a null element; Milo"
                                          + " encodes it as Good");
                                }
                                if (numericWireElement == null
                                    && Number.class.isAssignableFrom(
                                        ArrayUtil.getBoxedType(numericWireValue))) {
                                  throw new UaException(
                                      StatusCodes.Bad_TypeMismatch,
                                      "A numeric wire array cannot retain a null element; Milo"
                                          + " encodes it as zero");
                                }
                                if (numericWireElement instanceof Variant
                                    || numericWireElement instanceof DataValue) {
                                  numericWireValues.push(new Object[] {numericWireElement, false});
                                }
                              }
                            }
                          }

                          checkedWrite_Value =
                              org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject
                                  .encodeValue(
                                      checkedWrite_Context.encodingContext(), checkedWrite_Value);
                        }
                      }
                      encoded =
                          org.eclipse.milo.opcua.stack.core.types.builtin.Variant.of(
                              checkedWrite_Value);
                    } catch (
                        org.eclipse.milo.opcua.stack.core.UaSerializationException
                            checkedWrite_Failure) {
                      long checkedWrite_Status =
                          checkedWrite_Failure.getStatusCode().getValue()
                                  == org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_OutOfRange
                              ? org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_OutOfRange
                              : org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch;
                      throw new org.eclipse.milo.opcua.stack.core.UaException(
                          checkedWrite_Status, checkedWrite_Failure);
                    } catch (java.lang.IllegalArgumentException
                        | java.lang.ClassCastException checkedWrite_Failure) {
                      throw new org.eclipse.milo.opcua.stack.core.UaException(
                          org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_TypeMismatch,
                          checkedWrite_Failure);
                    }
                  }
                  views.checkOpen();
                  return child
                      .writeAttributeAsync(AttributeId.Value, DataValue.valueOnly(encoded))
                      .thenApply(response -> response);
                } catch (Exception failure) {
                  return CompletableFuture.failedFuture(failure);
                }
              });
        });
  }

  /**
   * Writes the existing member's value remotely. A null value is valid. An absent node fails with
   * Bad_NotFound before conversion or Write. This does not create nodes or update the wrapper's
   * local value.
   *
   * <p>Concrete enum conversions reject unknown numbers with Bad_OutOfRange. Structured decoding
   * and existing rank/type checks retain their failures.
   *
   * <p>Lookup, conversion and service failures complete the future exceptionally. UaException
   * causes preserve OPC UA status. Incompatible plain payload casts can complete exceptionally with
   * ClassCastException. Cancellation does not promise transport cancellation or rollback.
   *
   * <p>The actual member DataType, rank and maximum dimensions constrain this selected view write.
   * Incompatible values fail with Bad_TypeMismatch; values outside the actual finite enumeration
   * fail with Bad_OutOfRange. Validation finishes before mutation or Write. Type and enumeration
   * discovery can perform service I/O, including for local setters.
   *
   * <p>A closed view context completes the future exceptionally with IllegalStateException.
   *
   * @param value the value to store; null is permitted
   * @return a nonnull future completing with the Write operation status, including non-Good
   *     statuses
   */
  @Override
  public CompletableFuture<StatusCode> writeTimeAwareOffsetAsync(@Nullable UInteger value) {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.writeTimeAwareOffsetAsyncImplementation(value));
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }
}

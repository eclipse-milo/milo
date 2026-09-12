/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.model.objects;

import com.digitalpetri.opcua.uanodeset.runtime.server.ServerObjectView;
import com.digitalpetri.opcua.uanodeset.runtime.server.ServerReferenceTypes;
import com.digitalpetri.opcua.uanodeset.runtime.server.ServerViewType;
import com.digitalpetri.opcua.uanodeset.runtime.server.ServerViews;
import java.lang.reflect.Array;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import org.eclipse.milo.opcua.sdk.server.model.variables.BaseDataVariableType;
import org.eclipse.milo.opcua.sdk.server.model.variables.BaseDataVariableTypeView;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.UaEnumeratedType;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
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
import org.eclipse.milo.opcua.stack.core.util.ArrayUtil;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.15
 *
 * <p>Selected <code>IPriorityMappingEntryType</code> contract over one retained backing node.
 * Obtain this view from a ServerViews context using {@link #TYPE}; the token itself does not prove
 * membership. Access after context close fails with IllegalStateException.
 */
@NullMarked
public final class IPriorityMappingEntryTypeView extends ServerObjectView
    implements IPriorityMappingEntryType {
  /** Selected UA/Java contract and context-owned factory. */
  public static final ServerViewType<IPriorityMappingEntryType> TYPE =
      ServerViewType.of(
          ExpandedNodeId.parse("i=24205"),
          IPriorityMappingEntryType.class,
          IPriorityMappingEntryTypeView::new);

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

  private IPriorityMappingEntryTypeView(ServerViews views, UaObjectNode node) {
    super(views, node);
  }

  private UaVariableNode viewMember0() {
    views.checkOpen();
    var resolvedMember =
        ((Supplier<UaVariableNode>)
                () -> {
                  UaNode parent = this.node;
                  {
                    var namespaceTable = parent.getNodeContext().getNamespaceTable();
                    var namespaceIndex = namespaceTable.getIndex("http://opcfoundation.org/UA/");
                    var referenceId = ExpandedNodeId.parse("i=47").toNodeId(namespaceTable);
                    if (namespaceIndex == null || referenceId.isEmpty()) {
                      throw new UaRuntimeException(
                          StatusCodes.Bad_NodeIdInvalid,
                          "http://opcfoundation.org/UA/:MappingUri (declaration i=24206, owner"
                              + " i=24205) on "
                              + getNodeId());
                    }
                    var browseName = new QualifiedName(namespaceIndex, "MappingUri");
                    var matches = new LinkedHashMap<NodeId, UaNode>();
                    for (var reference : parent.getReferences()) {
                      if (!reference.isForward()) {
                        continue;
                      }
                      if (!reference.getReferenceTypeId().equals(referenceId.orElseThrow())) {
                        if (!(ServerReferenceTypes.isSubtypeOf(
                            parent.getNodeContext(),
                            reference.getReferenceTypeId(),
                            referenceId.orElseThrow()))) {
                          continue;
                        }
                      }
                      if (!reference.getTargetNodeId().isLocal()) {
                        throw new UaRuntimeException(
                            StatusCodes.Bad_NotSupported,
                            "http://opcfoundation.org/UA/:MappingUri (declaration i=24206, owner"
                                + " i=24205) on "
                                + getNodeId());
                      }
                      var targetId = reference.getTargetNodeId().toNodeId(namespaceTable);
                      if (targetId.isEmpty()) {
                        throw new UaRuntimeException(
                            StatusCodes.Bad_NodeIdInvalid,
                            "http://opcfoundation.org/UA/:MappingUri (declaration i=24206, owner"
                                + " i=24205) on "
                                + getNodeId());
                      }
                      var local = parent.getNodeManager().getNode(targetId.orElseThrow());
                      var target =
                          local.isPresent()
                              ? local.orElseThrow()
                              : parent
                                  .getNodeContext()
                                  .getServer()
                                  .getAddressSpaceManager()
                                  .getManagedNode(targetId.orElseThrow())
                                  .orElse(null);
                      if (target == null) {
                        throw new UaRuntimeException(
                            StatusCodes.Bad_NodeIdUnknown,
                            "http://opcfoundation.org/UA/:MappingUri (declaration i=24206, owner"
                                + " i=24205) on "
                                + getNodeId());
                      }
                      if (browseName.equals(target.getBrowseName())) {
                        matches.put(target.getNodeId(), target);
                      }
                    }
                    if (matches.isEmpty()) {
                      throw new UaRuntimeException(
                          StatusCodes.Bad_NotFound,
                          "http://opcfoundation.org/UA/:MappingUri (declaration i=24206, owner"
                              + " i=24205) on "
                              + getNodeId());
                    }
                    if (matches.size() > 1) {
                      throw new UaRuntimeException(
                          StatusCodes.Bad_TooManyMatches,
                          "http://opcfoundation.org/UA/:MappingUri (declaration i=24206, owner"
                              + " i=24205) on "
                              + getNodeId());
                    }
                    parent = matches.values().iterator().next();
                    if (parent.getNodeClass() != NodeClass.Variable) {
                      throw new UaRuntimeException(
                          StatusCodes.Bad_NodeClassInvalid,
                          "http://opcfoundation.org/UA/:MappingUri (declaration i=24206, owner"
                              + " i=24205) on "
                              + getNodeId());
                    }
                  }
                  if (!(parent instanceof UaVariableNode)) {
                    throw new UaRuntimeException(
                        StatusCodes.Bad_TypeMismatch,
                        "http://opcfoundation.org/UA/:MappingUri (declaration i=24206, owner"
                            + " i=24205) on "
                            + getNodeId());
                  }
                  return (UaVariableNode) parent;
                })
            .get();
    return resolvedMember == null ? null : this.views.retainNode(resolvedMember);
  }

  private UaVariableNode viewMember1() {
    views.checkOpen();
    var resolvedMember =
        ((Supplier<UaVariableNode>)
                () -> {
                  UaNode parent = this.node;
                  {
                    var namespaceTable = parent.getNodeContext().getNamespaceTable();
                    var namespaceIndex = namespaceTable.getIndex("http://opcfoundation.org/UA/");
                    var referenceId = ExpandedNodeId.parse("i=47").toNodeId(namespaceTable);
                    if (namespaceIndex == null || referenceId.isEmpty()) {
                      throw new UaRuntimeException(
                          StatusCodes.Bad_NodeIdInvalid,
                          "http://opcfoundation.org/UA/:PriorityLabel (declaration i=24207, owner"
                              + " i=24205) on "
                              + getNodeId());
                    }
                    var browseName = new QualifiedName(namespaceIndex, "PriorityLabel");
                    var matches = new LinkedHashMap<NodeId, UaNode>();
                    for (var reference : parent.getReferences()) {
                      if (!reference.isForward()) {
                        continue;
                      }
                      if (!reference.getReferenceTypeId().equals(referenceId.orElseThrow())) {
                        if (!(ServerReferenceTypes.isSubtypeOf(
                            parent.getNodeContext(),
                            reference.getReferenceTypeId(),
                            referenceId.orElseThrow()))) {
                          continue;
                        }
                      }
                      if (!reference.getTargetNodeId().isLocal()) {
                        throw new UaRuntimeException(
                            StatusCodes.Bad_NotSupported,
                            "http://opcfoundation.org/UA/:PriorityLabel (declaration i=24207, owner"
                                + " i=24205) on "
                                + getNodeId());
                      }
                      var targetId = reference.getTargetNodeId().toNodeId(namespaceTable);
                      if (targetId.isEmpty()) {
                        throw new UaRuntimeException(
                            StatusCodes.Bad_NodeIdInvalid,
                            "http://opcfoundation.org/UA/:PriorityLabel (declaration i=24207, owner"
                                + " i=24205) on "
                                + getNodeId());
                      }
                      var local = parent.getNodeManager().getNode(targetId.orElseThrow());
                      var target =
                          local.isPresent()
                              ? local.orElseThrow()
                              : parent
                                  .getNodeContext()
                                  .getServer()
                                  .getAddressSpaceManager()
                                  .getManagedNode(targetId.orElseThrow())
                                  .orElse(null);
                      if (target == null) {
                        throw new UaRuntimeException(
                            StatusCodes.Bad_NodeIdUnknown,
                            "http://opcfoundation.org/UA/:PriorityLabel (declaration i=24207, owner"
                                + " i=24205) on "
                                + getNodeId());
                      }
                      if (browseName.equals(target.getBrowseName())) {
                        matches.put(target.getNodeId(), target);
                      }
                    }
                    if (matches.isEmpty()) {
                      throw new UaRuntimeException(
                          StatusCodes.Bad_NotFound,
                          "http://opcfoundation.org/UA/:PriorityLabel (declaration i=24207, owner"
                              + " i=24205) on "
                              + getNodeId());
                    }
                    if (matches.size() > 1) {
                      throw new UaRuntimeException(
                          StatusCodes.Bad_TooManyMatches,
                          "http://opcfoundation.org/UA/:PriorityLabel (declaration i=24207, owner"
                              + " i=24205) on "
                              + getNodeId());
                    }
                    parent = matches.values().iterator().next();
                    if (parent.getNodeClass() != NodeClass.Variable) {
                      throw new UaRuntimeException(
                          StatusCodes.Bad_NodeClassInvalid,
                          "http://opcfoundation.org/UA/:PriorityLabel (declaration i=24207, owner"
                              + " i=24205) on "
                              + getNodeId());
                    }
                  }
                  if (!(parent instanceof UaVariableNode)) {
                    throw new UaRuntimeException(
                        StatusCodes.Bad_TypeMismatch,
                        "http://opcfoundation.org/UA/:PriorityLabel (declaration i=24207, owner"
                            + " i=24205) on "
                            + getNodeId());
                  }
                  return (UaVariableNode) parent;
                })
            .get();
    return resolvedMember == null ? null : this.views.retainNode(resolvedMember);
  }

  private @Nullable UaVariableNode viewMember2() {
    views.checkOpen();
    var resolvedMember =
        ((Supplier<@Nullable UaVariableNode>)
                () -> {
                  UaNode parent = this.node;
                  {
                    var namespaceTable = parent.getNodeContext().getNamespaceTable();
                    var namespaceIndex = namespaceTable.getIndex("http://opcfoundation.org/UA/");
                    var referenceId = ExpandedNodeId.parse("i=47").toNodeId(namespaceTable);
                    if (namespaceIndex == null || referenceId.isEmpty()) {
                      throw new UaRuntimeException(
                          StatusCodes.Bad_NodeIdInvalid,
                          "http://opcfoundation.org/UA/:PriorityValue_PCP (declaration i=24208,"
                              + " owner i=24205) on "
                              + getNodeId());
                    }
                    var browseName = new QualifiedName(namespaceIndex, "PriorityValue_PCP");
                    var matches = new LinkedHashMap<NodeId, UaNode>();
                    for (var reference : parent.getReferences()) {
                      if (!reference.isForward()) {
                        continue;
                      }
                      if (!reference.getReferenceTypeId().equals(referenceId.orElseThrow())) {
                        if (!(ServerReferenceTypes.isSubtypeOf(
                            parent.getNodeContext(),
                            reference.getReferenceTypeId(),
                            referenceId.orElseThrow()))) {
                          continue;
                        }
                      }
                      if (!reference.getTargetNodeId().isLocal()) {
                        throw new UaRuntimeException(
                            StatusCodes.Bad_NotSupported,
                            "http://opcfoundation.org/UA/:PriorityValue_PCP (declaration i=24208,"
                                + " owner i=24205) on "
                                + getNodeId());
                      }
                      var targetId = reference.getTargetNodeId().toNodeId(namespaceTable);
                      if (targetId.isEmpty()) {
                        throw new UaRuntimeException(
                            StatusCodes.Bad_NodeIdInvalid,
                            "http://opcfoundation.org/UA/:PriorityValue_PCP (declaration i=24208,"
                                + " owner i=24205) on "
                                + getNodeId());
                      }
                      var local = parent.getNodeManager().getNode(targetId.orElseThrow());
                      var target =
                          local.isPresent()
                              ? local.orElseThrow()
                              : parent
                                  .getNodeContext()
                                  .getServer()
                                  .getAddressSpaceManager()
                                  .getManagedNode(targetId.orElseThrow())
                                  .orElse(null);
                      if (target == null) {
                        throw new UaRuntimeException(
                            StatusCodes.Bad_NodeIdUnknown,
                            "http://opcfoundation.org/UA/:PriorityValue_PCP (declaration i=24208,"
                                + " owner i=24205) on "
                                + getNodeId());
                      }
                      if (browseName.equals(target.getBrowseName())) {
                        matches.put(target.getNodeId(), target);
                      }
                    }
                    if (matches.isEmpty()) {
                      return null;
                    }
                    if (matches.size() > 1) {
                      throw new UaRuntimeException(
                          StatusCodes.Bad_TooManyMatches,
                          "http://opcfoundation.org/UA/:PriorityValue_PCP (declaration i=24208,"
                              + " owner i=24205) on "
                              + getNodeId());
                    }
                    parent = matches.values().iterator().next();
                    if (parent.getNodeClass() != NodeClass.Variable) {
                      throw new UaRuntimeException(
                          StatusCodes.Bad_NodeClassInvalid,
                          "http://opcfoundation.org/UA/:PriorityValue_PCP (declaration i=24208,"
                              + " owner i=24205) on "
                              + getNodeId());
                    }
                  }
                  if (!(parent instanceof UaVariableNode)) {
                    throw new UaRuntimeException(
                        StatusCodes.Bad_TypeMismatch,
                        "http://opcfoundation.org/UA/:PriorityValue_PCP (declaration i=24208, owner"
                            + " i=24205) on "
                            + getNodeId());
                  }
                  return (UaVariableNode) parent;
                })
            .get();
    return resolvedMember == null ? null : this.views.retainNode(resolvedMember);
  }

  private @Nullable UaVariableNode viewMember3() {
    views.checkOpen();
    var resolvedMember =
        ((Supplier<@Nullable UaVariableNode>)
                () -> {
                  UaNode parent = this.node;
                  {
                    var namespaceTable = parent.getNodeContext().getNamespaceTable();
                    var namespaceIndex = namespaceTable.getIndex("http://opcfoundation.org/UA/");
                    var referenceId = ExpandedNodeId.parse("i=47").toNodeId(namespaceTable);
                    if (namespaceIndex == null || referenceId.isEmpty()) {
                      throw new UaRuntimeException(
                          StatusCodes.Bad_NodeIdInvalid,
                          "http://opcfoundation.org/UA/:PriorityValue_DSCP (declaration i=24209,"
                              + " owner i=24205) on "
                              + getNodeId());
                    }
                    var browseName = new QualifiedName(namespaceIndex, "PriorityValue_DSCP");
                    var matches = new LinkedHashMap<NodeId, UaNode>();
                    for (var reference : parent.getReferences()) {
                      if (!reference.isForward()) {
                        continue;
                      }
                      if (!reference.getReferenceTypeId().equals(referenceId.orElseThrow())) {
                        if (!(ServerReferenceTypes.isSubtypeOf(
                            parent.getNodeContext(),
                            reference.getReferenceTypeId(),
                            referenceId.orElseThrow()))) {
                          continue;
                        }
                      }
                      if (!reference.getTargetNodeId().isLocal()) {
                        throw new UaRuntimeException(
                            StatusCodes.Bad_NotSupported,
                            "http://opcfoundation.org/UA/:PriorityValue_DSCP (declaration i=24209,"
                                + " owner i=24205) on "
                                + getNodeId());
                      }
                      var targetId = reference.getTargetNodeId().toNodeId(namespaceTable);
                      if (targetId.isEmpty()) {
                        throw new UaRuntimeException(
                            StatusCodes.Bad_NodeIdInvalid,
                            "http://opcfoundation.org/UA/:PriorityValue_DSCP (declaration i=24209,"
                                + " owner i=24205) on "
                                + getNodeId());
                      }
                      var local = parent.getNodeManager().getNode(targetId.orElseThrow());
                      var target =
                          local.isPresent()
                              ? local.orElseThrow()
                              : parent
                                  .getNodeContext()
                                  .getServer()
                                  .getAddressSpaceManager()
                                  .getManagedNode(targetId.orElseThrow())
                                  .orElse(null);
                      if (target == null) {
                        throw new UaRuntimeException(
                            StatusCodes.Bad_NodeIdUnknown,
                            "http://opcfoundation.org/UA/:PriorityValue_DSCP (declaration i=24209,"
                                + " owner i=24205) on "
                                + getNodeId());
                      }
                      if (browseName.equals(target.getBrowseName())) {
                        matches.put(target.getNodeId(), target);
                      }
                    }
                    if (matches.isEmpty()) {
                      return null;
                    }
                    if (matches.size() > 1) {
                      throw new UaRuntimeException(
                          StatusCodes.Bad_TooManyMatches,
                          "http://opcfoundation.org/UA/:PriorityValue_DSCP (declaration i=24209,"
                              + " owner i=24205) on "
                              + getNodeId());
                    }
                    parent = matches.values().iterator().next();
                    if (parent.getNodeClass() != NodeClass.Variable) {
                      throw new UaRuntimeException(
                          StatusCodes.Bad_NodeClassInvalid,
                          "http://opcfoundation.org/UA/:PriorityValue_DSCP (declaration i=24209,"
                              + " owner i=24205) on "
                              + getNodeId());
                    }
                  }
                  if (!(parent instanceof UaVariableNode)) {
                    throw new UaRuntimeException(
                        StatusCodes.Bad_TypeMismatch,
                        "http://opcfoundation.org/UA/:PriorityValue_DSCP (declaration i=24209,"
                            + " owner i=24205) on "
                            + getNodeId());
                  }
                  return (UaVariableNode) parent;
                })
            .get();
    return resolvedMember == null ? null : this.views.retainNode(resolvedMember);
  }

  /**
   * Resolves the required member by its namespace-qualified path. A missing member fails with
   * Bad_NotFound. Resolution does not create a UA node. A reference can change after lookup.
   *
   * <p>The returned object exposes the selected child contract and shares the retained raw node's
   * state through this view context.
   *
   * @return the existing member
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException if a required node is absent,
   *     resolution fails, or a checked conversion fails
   * @throws IllegalStateException if the view context is closed
   */
  @Override
  public BaseDataVariableType getMappingUriNode() {
    this.views.checkOpen();
    views.checkOpen();
    var child = viewMember0();
    return child == null
        ? null
        : views.wrapVariable(child.getNodeId(), BaseDataVariableTypeView.TYPE);
  }

  /**
   * Gets the existing member's local value without checking its quality. A null value is valid; an
   * absent node fails with Bad_NotFound. Use the node's raw DataValue to inspect quality and
   * timestamps.
   *
   * <p>Concrete enum conversions reject unknown numbers with Bad_OutOfRange. Structured decoding
   * and existing rank/type checks retain their failures.
   *
   * @return the value, which may be null on a present member
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException if a required node is absent,
   *     resolution fails, or a checked conversion fails
   * @throws ClassCastException if a plain payload cast encounters an incompatible Java
   *     representation
   * @throws IllegalStateException if the view context is closed
   */
  @Override
  public @Nullable String getMappingUri() {
    this.views.checkOpen();
    views.checkOpen();
    var child = viewMember0();
    if (child == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MappingUri (declaration i=24206, owner i=24205)"
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
          throw new UaRuntimeException(
              StatusCodes.Bad_TypeMismatch,
              "http://opcfoundation.org/UA/:MappingUri (declaration i=24206, owner i=24205)");
        }
        if (rawValue instanceof Matrix matrix) {
          if (actualRank < 2
              || rankElements == null
              || !rankElements.getClass().isArray()
              || ArrayUtil.getValueRank(rankElements) != 1) {
            throw new UaRuntimeException(
                StatusCodes.Bad_TypeMismatch,
                "http://opcfoundation.org/UA/:MappingUri (declaration i=24206, owner i=24205)");
          }
          long elementCount = 1;
          for (int dimension : matrix.getDimensions()) {
            if (dimension < 0 || elementCount > Integer.MAX_VALUE) {
              throw new UaRuntimeException(
                  StatusCodes.Bad_TypeMismatch,
                  "http://opcfoundation.org/UA/:MappingUri (declaration i=24206, owner i=24205)");
            }
            elementCount *= dimension;
          }
          if (elementCount != Array.getLength(rankElements)) {
            throw new UaRuntimeException(
                StatusCodes.Bad_TypeMismatch,
                "http://opcfoundation.org/UA/:MappingUri (declaration i=24206, owner i=24205)");
          }
        } else if (actualRank > 1) {
          throw new UaRuntimeException(
              StatusCodes.Bad_TypeMismatch,
              "http://opcfoundation.org/UA/:MappingUri (declaration i=24206, owner i=24205): use"
                  + " Matrix for multiple dimensions");
        }
      }
      Object element = rawValue;
      if (element != null && !(element instanceof String)) {
        throw new UaRuntimeException(
            StatusCodes.Bad_TypeMismatch,
            "http://opcfoundation.org/UA/:MappingUri (declaration i=24206, owner i=24205)");
      }
      converted = (String) element;
    }
    return converted;
  }

  /**
   * Sets the existing member's local value. A null value is valid. An absent node fails with
   * Bad_NotFound before conversion or mutation. This does not create nodes or silently skip writes.
   *
   * <p>Concrete enum conversions reject unknown numbers with Bad_OutOfRange. Structured decoding
   * and existing rank/type checks retain their failures.
   *
   * <p>The actual member DataType, rank and maximum dimensions constrain this selected view write.
   * Incompatible values fail with Bad_TypeMismatch; values outside the actual finite enumeration
   * fail with Bad_OutOfRange. Validation finishes before mutation or Write.
   *
   * @param value the value to store; null is permitted
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException if a required node is absent,
   *     resolution fails, or a checked conversion fails
   * @throws IllegalStateException if the view context is closed
   */
  @Override
  public void setMappingUri(@Nullable String value) {
    this.views.checkOpen();
    views.checkOpen();
    var child = viewMember0();
    if (child == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MappingUri (declaration i=24206, owner i=24205)"
              + " on "
              + getNodeId());
    }
    try {
      var writeContext = views.writeContext(child.getNodeId(), value, KNOWN_ENUMS);
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
                          .orElse(
                              org.eclipse.milo.opcua.stack.core.types.builtin.NodeId.NULL_VALUE);
                  boolean checkedWrite_Abstract =
                      checkedWrite_Actual.equals(
                              org.eclipse.milo.opcua.stack.core.NodeIds.Structure)
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
                    org.eclipse.milo.opcua.stack.core.types.builtin.Variant.of(
                        checkedWrite_Element);
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
                        && Number.class.isAssignableFrom(
                            ArrayUtil.getBoxedType(numericWireValue))) {
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
      child.setValue(DataValue.valueOnly(encoded));
    } catch (UaException failure) {
      throw new UaRuntimeException(failure.getStatusCode().getValue(), failure);
    }
  }

  /**
   * Resolves the required member by its namespace-qualified path. A missing member fails with
   * Bad_NotFound. Resolution does not create a UA node. A reference can change after lookup.
   *
   * <p>The returned object exposes the selected child contract and shares the retained raw node's
   * state through this view context.
   *
   * @return the existing member
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException if a required node is absent,
   *     resolution fails, or a checked conversion fails
   * @throws IllegalStateException if the view context is closed
   */
  @Override
  public BaseDataVariableType getPriorityLabelNode() {
    this.views.checkOpen();
    views.checkOpen();
    var child = viewMember1();
    return child == null
        ? null
        : views.wrapVariable(child.getNodeId(), BaseDataVariableTypeView.TYPE);
  }

  /**
   * Gets the existing member's local value without checking its quality. A null value is valid; an
   * absent node fails with Bad_NotFound. Use the node's raw DataValue to inspect quality and
   * timestamps.
   *
   * <p>Concrete enum conversions reject unknown numbers with Bad_OutOfRange. Structured decoding
   * and existing rank/type checks retain their failures.
   *
   * @return the value, which may be null on a present member
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException if a required node is absent,
   *     resolution fails, or a checked conversion fails
   * @throws ClassCastException if a plain payload cast encounters an incompatible Java
   *     representation
   * @throws IllegalStateException if the view context is closed
   */
  @Override
  public @Nullable String getPriorityLabel() {
    this.views.checkOpen();
    views.checkOpen();
    var child = viewMember1();
    if (child == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:PriorityLabel (declaration i=24207, owner i=24205)"
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
          throw new UaRuntimeException(
              StatusCodes.Bad_TypeMismatch,
              "http://opcfoundation.org/UA/:PriorityLabel (declaration i=24207, owner i=24205)");
        }
        if (rawValue instanceof Matrix matrix) {
          if (actualRank < 2
              || rankElements == null
              || !rankElements.getClass().isArray()
              || ArrayUtil.getValueRank(rankElements) != 1) {
            throw new UaRuntimeException(
                StatusCodes.Bad_TypeMismatch,
                "http://opcfoundation.org/UA/:PriorityLabel (declaration i=24207, owner i=24205)");
          }
          long elementCount = 1;
          for (int dimension : matrix.getDimensions()) {
            if (dimension < 0 || elementCount > Integer.MAX_VALUE) {
              throw new UaRuntimeException(
                  StatusCodes.Bad_TypeMismatch,
                  "http://opcfoundation.org/UA/:PriorityLabel (declaration i=24207, owner"
                      + " i=24205)");
            }
            elementCount *= dimension;
          }
          if (elementCount != Array.getLength(rankElements)) {
            throw new UaRuntimeException(
                StatusCodes.Bad_TypeMismatch,
                "http://opcfoundation.org/UA/:PriorityLabel (declaration i=24207, owner i=24205)");
          }
        } else if (actualRank > 1) {
          throw new UaRuntimeException(
              StatusCodes.Bad_TypeMismatch,
              "http://opcfoundation.org/UA/:PriorityLabel (declaration i=24207, owner i=24205): use"
                  + " Matrix for multiple dimensions");
        }
      }
      Object element = rawValue;
      if (element != null && !(element instanceof String)) {
        throw new UaRuntimeException(
            StatusCodes.Bad_TypeMismatch,
            "http://opcfoundation.org/UA/:PriorityLabel (declaration i=24207, owner i=24205)");
      }
      converted = (String) element;
    }
    return converted;
  }

  /**
   * Sets the existing member's local value. A null value is valid. An absent node fails with
   * Bad_NotFound before conversion or mutation. This does not create nodes or silently skip writes.
   *
   * <p>Concrete enum conversions reject unknown numbers with Bad_OutOfRange. Structured decoding
   * and existing rank/type checks retain their failures.
   *
   * <p>The actual member DataType, rank and maximum dimensions constrain this selected view write.
   * Incompatible values fail with Bad_TypeMismatch; values outside the actual finite enumeration
   * fail with Bad_OutOfRange. Validation finishes before mutation or Write.
   *
   * @param value the value to store; null is permitted
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException if a required node is absent,
   *     resolution fails, or a checked conversion fails
   * @throws IllegalStateException if the view context is closed
   */
  @Override
  public void setPriorityLabel(@Nullable String value) {
    this.views.checkOpen();
    views.checkOpen();
    var child = viewMember1();
    if (child == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:PriorityLabel (declaration i=24207, owner i=24205)"
              + " on "
              + getNodeId());
    }
    try {
      var writeContext = views.writeContext(child.getNodeId(), value, KNOWN_ENUMS);
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
                          .orElse(
                              org.eclipse.milo.opcua.stack.core.types.builtin.NodeId.NULL_VALUE);
                  boolean checkedWrite_Abstract =
                      checkedWrite_Actual.equals(
                              org.eclipse.milo.opcua.stack.core.NodeIds.Structure)
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
                    org.eclipse.milo.opcua.stack.core.types.builtin.Variant.of(
                        checkedWrite_Element);
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
                        && Number.class.isAssignableFrom(
                            ArrayUtil.getBoxedType(numericWireValue))) {
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
      child.setValue(DataValue.valueOnly(encoded));
    } catch (UaException failure) {
      throw new UaRuntimeException(failure.getStatusCode().getValue(), failure);
    }
  }

  /**
   * Resolves the optional member by its namespace-qualified path. Returns null only for confirmed
   * absence. Resolution does not create a UA node. A reference can change after lookup.
   *
   * <p>The returned object exposes the selected child contract and shares the retained raw node's
   * state through this view context.
   *
   * @return the existing member, or null for confirmed absence
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException if a required node is absent,
   *     resolution fails, or a checked conversion fails
   * @throws IllegalStateException if the view context is closed
   */
  @Override
  public @Nullable BaseDataVariableType getPriorityValuePcpNode() {
    this.views.checkOpen();
    views.checkOpen();
    var child = viewMember2();
    return child == null
        ? null
        : views.wrapVariable(child.getNodeId(), BaseDataVariableTypeView.TYPE);
  }

  /**
   * Gets the existing member's local value without checking its quality. A null value is valid; an
   * absent node fails with Bad_NotFound. Use the node's raw DataValue to inspect quality and
   * timestamps.
   *
   * <p>Concrete enum conversions reject unknown numbers with Bad_OutOfRange. Structured decoding
   * and existing rank/type checks retain their failures.
   *
   * @return the value, which may be null on a present member
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException if a required node is absent,
   *     resolution fails, or a checked conversion fails
   * @throws ClassCastException if a plain payload cast encounters an incompatible Java
   *     representation
   * @throws IllegalStateException if the view context is closed
   */
  @Override
  public @Nullable UByte getPriorityValuePcp() {
    this.views.checkOpen();
    views.checkOpen();
    var child = viewMember2();
    if (child == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:PriorityValue_PCP (declaration i=24208, owner i=24205)"
              + " on "
              + getNodeId());
    }
    @Nullable UByte converted;
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
          throw new UaRuntimeException(
              StatusCodes.Bad_TypeMismatch,
              "http://opcfoundation.org/UA/:PriorityValue_PCP (declaration i=24208, owner"
                  + " i=24205)");
        }
        if (rawValue instanceof Matrix matrix) {
          if (actualRank < 2
              || rankElements == null
              || !rankElements.getClass().isArray()
              || ArrayUtil.getValueRank(rankElements) != 1) {
            throw new UaRuntimeException(
                StatusCodes.Bad_TypeMismatch,
                "http://opcfoundation.org/UA/:PriorityValue_PCP (declaration i=24208, owner"
                    + " i=24205)");
          }
          long elementCount = 1;
          for (int dimension : matrix.getDimensions()) {
            if (dimension < 0 || elementCount > Integer.MAX_VALUE) {
              throw new UaRuntimeException(
                  StatusCodes.Bad_TypeMismatch,
                  "http://opcfoundation.org/UA/:PriorityValue_PCP (declaration i=24208, owner"
                      + " i=24205)");
            }
            elementCount *= dimension;
          }
          if (elementCount != Array.getLength(rankElements)) {
            throw new UaRuntimeException(
                StatusCodes.Bad_TypeMismatch,
                "http://opcfoundation.org/UA/:PriorityValue_PCP (declaration i=24208, owner"
                    + " i=24205)");
          }
        } else if (actualRank > 1) {
          throw new UaRuntimeException(
              StatusCodes.Bad_TypeMismatch,
              "http://opcfoundation.org/UA/:PriorityValue_PCP (declaration i=24208, owner i=24205):"
                  + " use Matrix for multiple dimensions");
        }
      }
      Object element = rawValue;
      if (element != null && !(element instanceof UByte)) {
        throw new UaRuntimeException(
            StatusCodes.Bad_TypeMismatch,
            "http://opcfoundation.org/UA/:PriorityValue_PCP (declaration i=24208, owner i=24205)");
      }
      converted = (UByte) element;
    }
    return converted;
  }

  /**
   * Sets the existing member's local value. A null value is valid. An absent node fails with
   * Bad_NotFound before conversion or mutation. This does not create nodes or silently skip writes.
   *
   * <p>Concrete enum conversions reject unknown numbers with Bad_OutOfRange. Structured decoding
   * and existing rank/type checks retain their failures.
   *
   * <p>The actual member DataType, rank and maximum dimensions constrain this selected view write.
   * Incompatible values fail with Bad_TypeMismatch; values outside the actual finite enumeration
   * fail with Bad_OutOfRange. Validation finishes before mutation or Write.
   *
   * @param value the value to store; null is permitted
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException if a required node is absent,
   *     resolution fails, or a checked conversion fails
   * @throws IllegalStateException if the view context is closed
   */
  @Override
  public void setPriorityValuePcp(@Nullable UByte value) {
    this.views.checkOpen();
    views.checkOpen();
    var child = viewMember2();
    if (child == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:PriorityValue_PCP (declaration i=24208, owner i=24205)"
              + " on "
              + getNodeId());
    }
    try {
      var writeContext = views.writeContext(child.getNodeId(), value, KNOWN_ENUMS);
      Variant encoded;
      {
        var checkedWrite_Context = writeContext;
        Object checkedWrite_Value = value;
        var checkedWrite_Selected =
            ExpandedNodeId.parse("i=3")
                .toNodeId(checkedWrite_Context.namespaceTable())
                .orElseThrow(
                    () ->
                        new org.eclipse.milo.opcua.stack.core.UaException(
                            org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_NodeIdInvalid,
                            "Unknown selected DataType namespace:"
                                + " ExpandedNodeId[server=ServerIndex[serverIndex=0],"
                                + " namespace=NamespaceUri[namespaceUri=http://opcfoundation.org/UA/],"
                                + " identifier=3]"));
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
                          .orElse(
                              org.eclipse.milo.opcua.stack.core.types.builtin.NodeId.NULL_VALUE);
                  boolean checkedWrite_Abstract =
                      checkedWrite_Actual.equals(
                              org.eclipse.milo.opcua.stack.core.NodeIds.Structure)
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
                    org.eclipse.milo.opcua.stack.core.types.builtin.Variant.of(
                        checkedWrite_Element);
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
                        && Number.class.isAssignableFrom(
                            ArrayUtil.getBoxedType(numericWireValue))) {
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
      child.setValue(DataValue.valueOnly(encoded));
    } catch (UaException failure) {
      throw new UaRuntimeException(failure.getStatusCode().getValue(), failure);
    }
  }

  /**
   * Resolves the optional member by its namespace-qualified path. Returns null only for confirmed
   * absence. Resolution does not create a UA node. A reference can change after lookup.
   *
   * <p>The returned object exposes the selected child contract and shares the retained raw node's
   * state through this view context.
   *
   * @return the existing member, or null for confirmed absence
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException if a required node is absent,
   *     resolution fails, or a checked conversion fails
   * @throws IllegalStateException if the view context is closed
   */
  @Override
  public @Nullable BaseDataVariableType getPriorityValueDscpNode() {
    this.views.checkOpen();
    views.checkOpen();
    var child = viewMember3();
    return child == null
        ? null
        : views.wrapVariable(child.getNodeId(), BaseDataVariableTypeView.TYPE);
  }

  /**
   * Gets the existing member's local value without checking its quality. A null value is valid; an
   * absent node fails with Bad_NotFound. Use the node's raw DataValue to inspect quality and
   * timestamps.
   *
   * <p>Concrete enum conversions reject unknown numbers with Bad_OutOfRange. Structured decoding
   * and existing rank/type checks retain their failures.
   *
   * @return the value, which may be null on a present member
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException if a required node is absent,
   *     resolution fails, or a checked conversion fails
   * @throws ClassCastException if a plain payload cast encounters an incompatible Java
   *     representation
   * @throws IllegalStateException if the view context is closed
   */
  @Override
  public @Nullable UInteger getPriorityValueDscp() {
    this.views.checkOpen();
    views.checkOpen();
    var child = viewMember3();
    if (child == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:PriorityValue_DSCP (declaration i=24209, owner i=24205)"
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
          throw new UaRuntimeException(
              StatusCodes.Bad_TypeMismatch,
              "http://opcfoundation.org/UA/:PriorityValue_DSCP (declaration i=24209, owner"
                  + " i=24205)");
        }
        if (rawValue instanceof Matrix matrix) {
          if (actualRank < 2
              || rankElements == null
              || !rankElements.getClass().isArray()
              || ArrayUtil.getValueRank(rankElements) != 1) {
            throw new UaRuntimeException(
                StatusCodes.Bad_TypeMismatch,
                "http://opcfoundation.org/UA/:PriorityValue_DSCP (declaration i=24209, owner"
                    + " i=24205)");
          }
          long elementCount = 1;
          for (int dimension : matrix.getDimensions()) {
            if (dimension < 0 || elementCount > Integer.MAX_VALUE) {
              throw new UaRuntimeException(
                  StatusCodes.Bad_TypeMismatch,
                  "http://opcfoundation.org/UA/:PriorityValue_DSCP (declaration i=24209, owner"
                      + " i=24205)");
            }
            elementCount *= dimension;
          }
          if (elementCount != Array.getLength(rankElements)) {
            throw new UaRuntimeException(
                StatusCodes.Bad_TypeMismatch,
                "http://opcfoundation.org/UA/:PriorityValue_DSCP (declaration i=24209, owner"
                    + " i=24205)");
          }
        } else if (actualRank > 1) {
          throw new UaRuntimeException(
              StatusCodes.Bad_TypeMismatch,
              "http://opcfoundation.org/UA/:PriorityValue_DSCP (declaration i=24209, owner"
                  + " i=24205): use Matrix for multiple dimensions");
        }
      }
      Object element = rawValue;
      if (element != null && !(element instanceof UInteger)) {
        throw new UaRuntimeException(
            StatusCodes.Bad_TypeMismatch,
            "http://opcfoundation.org/UA/:PriorityValue_DSCP (declaration i=24209, owner i=24205)");
      }
      converted = (UInteger) element;
    }
    return converted;
  }

  /**
   * Sets the existing member's local value. A null value is valid. An absent node fails with
   * Bad_NotFound before conversion or mutation. This does not create nodes or silently skip writes.
   *
   * <p>Concrete enum conversions reject unknown numbers with Bad_OutOfRange. Structured decoding
   * and existing rank/type checks retain their failures.
   *
   * <p>The actual member DataType, rank and maximum dimensions constrain this selected view write.
   * Incompatible values fail with Bad_TypeMismatch; values outside the actual finite enumeration
   * fail with Bad_OutOfRange. Validation finishes before mutation or Write.
   *
   * @param value the value to store; null is permitted
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException if a required node is absent,
   *     resolution fails, or a checked conversion fails
   * @throws IllegalStateException if the view context is closed
   */
  @Override
  public void setPriorityValueDscp(@Nullable UInteger value) {
    this.views.checkOpen();
    views.checkOpen();
    var child = viewMember3();
    if (child == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:PriorityValue_DSCP (declaration i=24209, owner i=24205)"
              + " on "
              + getNodeId());
    }
    try {
      var writeContext = views.writeContext(child.getNodeId(), value, KNOWN_ENUMS);
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
                          .orElse(
                              org.eclipse.milo.opcua.stack.core.types.builtin.NodeId.NULL_VALUE);
                  boolean checkedWrite_Abstract =
                      checkedWrite_Actual.equals(
                              org.eclipse.milo.opcua.stack.core.NodeIds.Structure)
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
                    org.eclipse.milo.opcua.stack.core.types.builtin.Variant.of(
                        checkedWrite_Element);
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
                        && Number.class.isAssignableFrom(
                            ArrayUtil.getBoxedType(numericWireValue))) {
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
      child.setValue(DataValue.valueOnly(encoded));
    } catch (UaException failure) {
      throw new UaRuntimeException(failure.getStatusCode().getValue(), failure);
    }
  }
}

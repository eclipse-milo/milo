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
import com.digitalpetri.opcua.uanodeset.runtime.values.ValueChecks;
import java.lang.reflect.Array;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import org.eclipse.milo.opcua.sdk.server.model.variables.BaseDataVariableType;
import org.eclipse.milo.opcua.sdk.server.model.variables.BaseDataVariableTypeView;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.util.ArrayUtil;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.14
 *
 * <p>Selected <code>IIeeeTsnVlanTagType</code> contract over one retained backing node. Obtain this
 * view from a ServerViews context using {@link #TYPE}; the token itself does not prove membership.
 * Access after context close fails with IllegalStateException.
 */
@NullMarked
public final class IIeeeTsnVlanTagTypeView extends ServerObjectView implements IIeeeTsnVlanTagType {
  /** Selected UA/Java contract and context-owned factory. */
  public static final ServerViewType<IIeeeTsnVlanTagType> TYPE =
      ServerViewType.of(
          ExpandedNodeId.parse("i=24202"), IIeeeTsnVlanTagType.class, IIeeeTsnVlanTagTypeView::new);

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

  private IIeeeTsnVlanTagTypeView(ServerViews views, UaObjectNode node) {
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
                          "http://opcfoundation.org/UA/:VlanId (declaration i=24203, owner i=24202)"
                              + " on "
                              + getNodeId());
                    }
                    var browseName = new QualifiedName(namespaceIndex, "VlanId");
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
                            "http://opcfoundation.org/UA/:VlanId (declaration i=24203, owner"
                                + " i=24202) on "
                                + getNodeId());
                      }
                      var targetId = reference.getTargetNodeId().toNodeId(namespaceTable);
                      if (targetId.isEmpty()) {
                        throw new UaRuntimeException(
                            StatusCodes.Bad_NodeIdInvalid,
                            "http://opcfoundation.org/UA/:VlanId (declaration i=24203, owner"
                                + " i=24202) on "
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
                            "http://opcfoundation.org/UA/:VlanId (declaration i=24203, owner"
                                + " i=24202) on "
                                + getNodeId());
                      }
                      if (browseName.equals(target.getBrowseName())) {
                        matches.put(target.getNodeId(), target);
                      }
                    }
                    if (matches.isEmpty()) {
                      throw new UaRuntimeException(
                          StatusCodes.Bad_NotFound,
                          "http://opcfoundation.org/UA/:VlanId (declaration i=24203, owner i=24202)"
                              + " on "
                              + getNodeId());
                    }
                    if (matches.size() > 1) {
                      throw new UaRuntimeException(
                          StatusCodes.Bad_TooManyMatches,
                          "http://opcfoundation.org/UA/:VlanId (declaration i=24203, owner i=24202)"
                              + " on "
                              + getNodeId());
                    }
                    parent = matches.values().iterator().next();
                    if (parent.getNodeClass() != NodeClass.Variable) {
                      throw new UaRuntimeException(
                          StatusCodes.Bad_NodeClassInvalid,
                          "http://opcfoundation.org/UA/:VlanId (declaration i=24203, owner i=24202)"
                              + " on "
                              + getNodeId());
                    }
                  }
                  if (!(parent instanceof UaVariableNode)) {
                    throw new UaRuntimeException(
                        StatusCodes.Bad_TypeMismatch,
                        "http://opcfoundation.org/UA/:VlanId (declaration i=24203, owner i=24202)"
                            + " on "
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
                          "http://opcfoundation.org/UA/:PriorityCodePoint (declaration i=24204,"
                              + " owner i=24202) on "
                              + getNodeId());
                    }
                    var browseName = new QualifiedName(namespaceIndex, "PriorityCodePoint");
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
                            "http://opcfoundation.org/UA/:PriorityCodePoint (declaration i=24204,"
                                + " owner i=24202) on "
                                + getNodeId());
                      }
                      var targetId = reference.getTargetNodeId().toNodeId(namespaceTable);
                      if (targetId.isEmpty()) {
                        throw new UaRuntimeException(
                            StatusCodes.Bad_NodeIdInvalid,
                            "http://opcfoundation.org/UA/:PriorityCodePoint (declaration i=24204,"
                                + " owner i=24202) on "
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
                            "http://opcfoundation.org/UA/:PriorityCodePoint (declaration i=24204,"
                                + " owner i=24202) on "
                                + getNodeId());
                      }
                      if (browseName.equals(target.getBrowseName())) {
                        matches.put(target.getNodeId(), target);
                      }
                    }
                    if (matches.isEmpty()) {
                      throw new UaRuntimeException(
                          StatusCodes.Bad_NotFound,
                          "http://opcfoundation.org/UA/:PriorityCodePoint (declaration i=24204,"
                              + " owner i=24202) on "
                              + getNodeId());
                    }
                    if (matches.size() > 1) {
                      throw new UaRuntimeException(
                          StatusCodes.Bad_TooManyMatches,
                          "http://opcfoundation.org/UA/:PriorityCodePoint (declaration i=24204,"
                              + " owner i=24202) on "
                              + getNodeId());
                    }
                    parent = matches.values().iterator().next();
                    if (parent.getNodeClass() != NodeClass.Variable) {
                      throw new UaRuntimeException(
                          StatusCodes.Bad_NodeClassInvalid,
                          "http://opcfoundation.org/UA/:PriorityCodePoint (declaration i=24204,"
                              + " owner i=24202) on "
                              + getNodeId());
                    }
                  }
                  if (!(parent instanceof UaVariableNode)) {
                    throw new UaRuntimeException(
                        StatusCodes.Bad_TypeMismatch,
                        "http://opcfoundation.org/UA/:PriorityCodePoint (declaration i=24204, owner"
                            + " i=24202) on "
                            + getNodeId());
                  }
                  return (UaVariableNode) parent;
                })
            .get();
    return resolvedMember == null ? null : this.views.retainNode(resolvedMember);
  }

  @Override
  public BaseDataVariableType getVlanIdNode() {
    this.views.checkOpen();
    views.checkOpen();
    var child = viewMember0();
    return child == null
        ? null
        : views.wrapVariable(child.getNodeId(), BaseDataVariableTypeView.TYPE);
  }

  @Override
  public @Nullable UShort getVlanId() {
    this.views.checkOpen();
    views.checkOpen();
    var child = viewMember0();
    if (child == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:VlanId (declaration i=24203, owner i=24202)"
              + " on "
              + getNodeId());
    }
    @Nullable UShort converted;
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
              "http://opcfoundation.org/UA/:VlanId (declaration i=24203, owner i=24202)");
        }
        if (rawValue instanceof Matrix matrix) {
          if (actualRank < 2
              || rankElements == null
              || !rankElements.getClass().isArray()
              || ArrayUtil.getValueRank(rankElements) != 1) {
            throw new UaRuntimeException(
                StatusCodes.Bad_TypeMismatch,
                "http://opcfoundation.org/UA/:VlanId (declaration i=24203, owner i=24202)");
          }
          long elementCount = 1;
          for (int dimension : matrix.getDimensions()) {
            if (dimension < 0 || elementCount > Integer.MAX_VALUE) {
              throw new UaRuntimeException(
                  StatusCodes.Bad_TypeMismatch,
                  "http://opcfoundation.org/UA/:VlanId (declaration i=24203, owner i=24202)");
            }
            elementCount *= dimension;
          }
          if (elementCount != Array.getLength(rankElements)) {
            throw new UaRuntimeException(
                StatusCodes.Bad_TypeMismatch,
                "http://opcfoundation.org/UA/:VlanId (declaration i=24203, owner i=24202)");
          }
        } else if (actualRank > 1) {
          throw new UaRuntimeException(
              StatusCodes.Bad_TypeMismatch,
              "http://opcfoundation.org/UA/:VlanId (declaration i=24203, owner i=24202): use Matrix"
                  + " for multiple dimensions");
        }
      }
      Object element = rawValue;
      if (element != null && !(element instanceof UShort)) {
        throw new UaRuntimeException(
            StatusCodes.Bad_TypeMismatch,
            "http://opcfoundation.org/UA/:VlanId (declaration i=24203, owner i=24202)");
      }
      converted = (UShort) element;
    }
    return converted;
  }

  @Override
  public void setVlanId(@Nullable UShort value) {
    this.views.checkOpen();
    views.checkOpen();
    var child = viewMember0();
    if (child == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:VlanId (declaration i=24203, owner i=24202)"
              + " on "
              + getNodeId());
    }
    try {
      var writeContext = views.writeContext(child.getNodeId(), value, KNOWN_ENUMS);
      Variant encoded =
          ValueChecks.validate(
              writeContext, value, ExpandedNodeId.parse("i=5"), -1, new long[] {}, null, false);
      views.checkOpen();
      child.setValue(DataValue.valueOnly(encoded));
    } catch (UaException failure) {
      throw new UaRuntimeException(failure.getStatusCode().getValue(), failure);
    }
  }

  @Override
  public BaseDataVariableType getPriorityCodePointNode() {
    this.views.checkOpen();
    views.checkOpen();
    var child = viewMember1();
    return child == null
        ? null
        : views.wrapVariable(child.getNodeId(), BaseDataVariableTypeView.TYPE);
  }

  @Override
  public @Nullable UByte getPriorityCodePoint() {
    this.views.checkOpen();
    views.checkOpen();
    var child = viewMember1();
    if (child == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:PriorityCodePoint (declaration i=24204, owner i=24202)"
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
              "http://opcfoundation.org/UA/:PriorityCodePoint (declaration i=24204, owner"
                  + " i=24202)");
        }
        if (rawValue instanceof Matrix matrix) {
          if (actualRank < 2
              || rankElements == null
              || !rankElements.getClass().isArray()
              || ArrayUtil.getValueRank(rankElements) != 1) {
            throw new UaRuntimeException(
                StatusCodes.Bad_TypeMismatch,
                "http://opcfoundation.org/UA/:PriorityCodePoint (declaration i=24204, owner"
                    + " i=24202)");
          }
          long elementCount = 1;
          for (int dimension : matrix.getDimensions()) {
            if (dimension < 0 || elementCount > Integer.MAX_VALUE) {
              throw new UaRuntimeException(
                  StatusCodes.Bad_TypeMismatch,
                  "http://opcfoundation.org/UA/:PriorityCodePoint (declaration i=24204, owner"
                      + " i=24202)");
            }
            elementCount *= dimension;
          }
          if (elementCount != Array.getLength(rankElements)) {
            throw new UaRuntimeException(
                StatusCodes.Bad_TypeMismatch,
                "http://opcfoundation.org/UA/:PriorityCodePoint (declaration i=24204, owner"
                    + " i=24202)");
          }
        } else if (actualRank > 1) {
          throw new UaRuntimeException(
              StatusCodes.Bad_TypeMismatch,
              "http://opcfoundation.org/UA/:PriorityCodePoint (declaration i=24204, owner i=24202):"
                  + " use Matrix for multiple dimensions");
        }
      }
      Object element = rawValue;
      if (element != null && !(element instanceof UByte)) {
        throw new UaRuntimeException(
            StatusCodes.Bad_TypeMismatch,
            "http://opcfoundation.org/UA/:PriorityCodePoint (declaration i=24204, owner i=24202)");
      }
      converted = (UByte) element;
    }
    return converted;
  }

  @Override
  public void setPriorityCodePoint(@Nullable UByte value) {
    this.views.checkOpen();
    views.checkOpen();
    var child = viewMember1();
    if (child == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:PriorityCodePoint (declaration i=24204, owner i=24202)"
              + " on "
              + getNodeId());
    }
    try {
      var writeContext = views.writeContext(child.getNodeId(), value, KNOWN_ENUMS);
      Variant encoded =
          ValueChecks.validate(
              writeContext, value, ExpandedNodeId.parse("i=3"), -1, new long[] {}, null, false);
      views.checkOpen();
      child.setValue(DataValue.valueOnly(encoded));
    } catch (UaException failure) {
      throw new UaRuntimeException(failure.getStatusCode().getValue(), failure);
    }
  }
}

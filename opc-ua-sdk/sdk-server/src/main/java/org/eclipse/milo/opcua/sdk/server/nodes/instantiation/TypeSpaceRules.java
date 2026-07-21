/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.nodes.instantiation;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.core.ValueRanks;
import org.eclipse.milo.opcua.sdk.core.typetree.ReferenceTypeTree;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.jspecify.annotations.Nullable;

/**
 * The type-space rules shared by compile-time validation ({@link TypeModelCompiler}) and plan-time
 * validation ({@link NodeInstantiator}): supertype-chain walks over live address-space reads and
 * the Part 3 §6.2.8 Variable narrowing predicates. One definition keeps the two phases from
 * disagreeing about what is a valid type or restriction.
 *
 * <p>The supertype walks read references through a caller-supplied function so the compiler can
 * route reads through its dependency-tracking, memoizing context while the instantiator reads the
 * live {@link org.eclipse.milo.opcua.sdk.server.AddressSpaceManager} directly.
 */
final class TypeSpaceRules {

  /** Deterministic {@link NodeId} ordering used for sorts and the multi-supertype tiebreak. */
  static final Comparator<NodeId> NODE_ID_ORDER = Comparator.comparing(NodeId::toParseableString);

  private TypeSpaceRules() {}

  /** Whether {@code referenceTypeId} is {@code supertypeId} or one of its subtypes. */
  static boolean isReferenceSubtypeOfOrEqual(
      ReferenceTypeTree referenceTypeTree, NodeId referenceTypeId, NodeId supertypeId) {
    return referenceTypeId.equals(supertypeId)
        || referenceTypeTree.isSubtypeOf(referenceTypeId, supertypeId);
  }

  /**
   * Walk the inverse HasSubtype chain to decide whether {@code typeId} is {@code supertypeId} or
   * one of its subtypes. Used for TypeDefinition and DataType compatibility, which the
   * ReferenceType tree does not cover.
   */
  static boolean isTypeSubtypeOfOrEqual(
      NodeId typeId,
      NodeId supertypeId,
      Function<NodeId, List<Reference>> references,
      NamespaceTable namespaceTable) {

    if (typeId.equals(supertypeId)) {
      return true;
    }
    Set<NodeId> visited = new HashSet<>();
    NodeId current = typeId;
    while (current != null && visited.add(current)) {
      NodeId parent = immediateSupertype(current, references, namespaceTable);
      if (supertypeId.equals(parent)) {
        return true;
      }
      current = parent;
    }
    return false;
  }

  /**
   * @return the immediate supertype of {@code typeId} — the lowest-ordered target of an inverse
   *     {@code HasSubtype} reference — or {@code null} if the type has none.
   */
  static @Nullable NodeId immediateSupertype(
      NodeId typeId, Function<NodeId, List<Reference>> references, NamespaceTable namespaceTable) {

    return references.apply(typeId).stream()
        .filter(Reference.SUBTYPE_OF)
        .map(r -> r.getTargetNodeId().toNodeId(namespaceTable).orElse(null))
        .filter(Objects::nonNull)
        .min(NODE_ID_ORDER)
        .orElse(null);
  }

  /**
   * Whether {@code restricted} is a valid ValueRank restriction of {@code base} (Part 3 §6.2.8).
   */
  static boolean isValueRankRestriction(int base, int restricted) {
    if (base == restricted || base == ValueRanks.Any) {
      return true;
    }
    if (base == ValueRanks.ScalarOrOneDimension) {
      return restricted == ValueRanks.Scalar || restricted == 1;
    }
    if (base == ValueRanks.OneOrMoreDimensions) {
      return restricted >= 1;
    }
    return false;
  }

  /**
   * Whether {@code restricted} is a valid ArrayDimensions restriction of {@code base} (Part 3
   * §6.2.8): same length, and every fixed (non-zero) base dimension preserved.
   */
  static boolean isArrayDimensionsRestriction(UInteger[] base, UInteger[] restricted) {
    if (restricted.length != base.length) {
      return false;
    }
    for (int i = 0; i < base.length; i++) {
      if (base[i].longValue() != 0 && !base[i].equals(restricted[i])) {
        return false;
      }
    }
    return true;
  }
}

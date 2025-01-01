/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.core;

import com.google.common.base.MoreObjects;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;

public class Reference {

  public enum Direction {
    FORWARD,
    INVERSE
  }

  private final NodeId sourceNodeId;
  private final NodeId referenceTypeId;
  private final ExpandedNodeId targetNodeId;
  private final Direction direction;

  public Reference(
      NodeId sourceNodeId, NodeId referenceTypeId, ExpandedNodeId targetNodeId, boolean forward) {

    this(
        sourceNodeId,
        referenceTypeId,
        targetNodeId,
        forward ? Direction.FORWARD : Direction.INVERSE);
  }

  public Reference(
      NodeId sourceNodeId,
      NodeId referenceTypeId,
      ExpandedNodeId targetNodeId,
      Direction direction) {

    this.sourceNodeId = sourceNodeId;
    this.referenceTypeId = referenceTypeId;
    this.targetNodeId = targetNodeId;
    this.direction = direction;
  }

  public NodeId getSourceNodeId() {
    return sourceNodeId;
  }

  public NodeId getReferenceTypeId() {
    return referenceTypeId;
  }

  public ExpandedNodeId getTargetNodeId() {
    return targetNodeId;
  }

  public Direction getDirection() {
    return direction;
  }

  public boolean isForward() {
    return direction == Direction.FORWARD;
  }

  public boolean isInverse() {
    return direction == Direction.INVERSE;
  }

  /**
   * Return an inverted instance of this Reference so long as the target NodeId resides within this
   * server.
   *
   * @return an inverted instance of this Reference so long as the target NodeId resides within this
   *     server.
   */
  public Optional<Reference> invert(NamespaceTable namespaceTable) {
    return getTargetNodeId()
        .toNodeId(namespaceTable)
        .map(
            sourceNodeId ->
                new Reference(
                    sourceNodeId,
                    getReferenceTypeId(),
                    getSourceNodeId().expanded(),
                    !isForward()));
  }

  /**
   * Re-index the source, target, and reference type {@link NodeId}s in this {@link Reference} from
   * their current namespace index to the index for {@code namespaceUri}.
   *
   * <p>If the target namespace URI is not present in the namespace table this {@link Reference} is
   * returned.
   *
   * @param namespaceTable the {@link NamespaceTable}.
   * @param sourceNamespaceUri the target namespace URI for the source NodeId.
   * @param referenceNamespaceUri the target namespace URI for the reference type NodeId.
   * @param targetNamespaceUri the target namespace URI for the target NodeId.
   * @return a new {@link NodeId} in the namespace index indicated by {@code namespaceUri}.
   */
  public Reference reindex(
      NamespaceTable namespaceTable,
      String sourceNamespaceUri,
      String referenceNamespaceUri,
      String targetNamespaceUri) {

    NodeId newSourceNodeId = sourceNodeId.reindex(namespaceTable, sourceNamespaceUri);

    NodeId newReferenceTypeId = referenceTypeId.reindex(namespaceTable, referenceNamespaceUri);

    // re-index targetNodeId only if it's local, otherwise leave it alone.
    ExpandedNodeId newTargetNodeId =
        targetNodeId
            .toNodeId(namespaceTable)
            .map(id -> id.reindex(namespaceTable, targetNamespaceUri).expanded())
            .orElse(targetNodeId);

    return new Reference(newSourceNodeId, newReferenceTypeId, newTargetNodeId, direction);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Reference reference = (Reference) o;
    return Objects.equals(sourceNodeId, reference.sourceNodeId)
        && Objects.equals(referenceTypeId, reference.referenceTypeId)
        && Objects.equals(targetNodeId, reference.targetNodeId)
        && direction == reference.direction;
  }

  @Override
  public int hashCode() {
    return Objects.hash(sourceNodeId, referenceTypeId, targetNodeId, direction);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("sourceNodeId", sourceNodeId)
        .add("referenceTypeId", referenceTypeId)
        .add("targetNodeId", targetNodeId)
        .add("direction", direction)
        .toString();
  }

  public static final Predicate<Reference> HAS_COMPONENT_PREDICATE =
      (reference) ->
          reference.isForward() && NodeIds.HasComponent.equals(reference.getReferenceTypeId());

  public static final Predicate<Reference> HAS_ORDERED_COMPONENT_PREDICATE =
      (reference) ->
          reference.isForward()
              && NodeIds.HasOrderedComponent.equals(reference.getReferenceTypeId());

  public static final Predicate<Reference> COMPONENT_OF_PREDICATE =
      (reference) ->
          reference.isInverse() && NodeIds.HasComponent.equals(reference.getReferenceTypeId());

  public static final Predicate<Reference> ORDERED_COMPONENT_OF_PREDICATE =
      (reference) ->
          reference.isInverse()
              && NodeIds.HasOrderedComponent.equals(reference.getReferenceTypeId());

  public static final Predicate<Reference> HAS_PROPERTY_PREDICATE =
      (reference) ->
          reference.isForward() && NodeIds.HasProperty.equals(reference.getReferenceTypeId());

  public static final Predicate<Reference> HAS_TYPE_DEFINITION_PREDICATE =
      (reference) ->
          reference.isForward() && NodeIds.HasTypeDefinition.equals(reference.getReferenceTypeId());

  public static final Predicate<Reference> HAS_EVENT_SOURCE_PREDICATE =
      (reference) ->
          reference.isForward() && NodeIds.HasEventSource.equals(reference.getReferenceTypeId());

  public static final Predicate<Reference> HAS_NOTIFIER_PREDICATE =
      (reference) ->
          reference.isForward() && NodeIds.HasNotifier.equals(reference.getReferenceTypeId());

  public static final Predicate<Reference> ORGANIZES_PREDICATE =
      (reference) ->
          reference.isForward() && NodeIds.Organizes.equals(reference.getReferenceTypeId());

  public static final Predicate<Reference> HAS_ENCODING_PREDICATE =
      (reference) ->
          reference.isForward() && NodeIds.HasEncoding.equals(reference.getReferenceTypeId());

  public static final Predicate<Reference> HAS_DESCRIPTION_PREDICATE =
      (reference) ->
          reference.isForward() && NodeIds.HasDescription.equals(reference.getReferenceTypeId());

  public static final Predicate<Reference> HAS_MODELLING_RULE_PREDICATE =
      (reference) ->
          reference.isForward() && NodeIds.HasModellingRule.equals(reference.getReferenceTypeId());

  public static final Predicate<Reference> ALWAYS_GENERATES_EVENT_PREDICATE =
      (reference) ->
          reference.isForward()
              && NodeIds.AlwaysGeneratesEvent.equals(reference.getReferenceTypeId());

  public static final Predicate<Reference> HAS_SUBTYPE =
      (reference) ->
          reference.isForward() && NodeIds.HasSubtype.equals(reference.getReferenceTypeId());

  public static final Predicate<Reference> SUBTYPE_OF =
      (reference) ->
          reference.isInverse() && NodeIds.HasSubtype.equals(reference.getReferenceTypeId());
}

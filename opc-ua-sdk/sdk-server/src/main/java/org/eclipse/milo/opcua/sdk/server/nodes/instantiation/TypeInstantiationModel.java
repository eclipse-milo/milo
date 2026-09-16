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

import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;

/**
 * The immutable, validated instantiation model of one TypeDefinition: its fully-inherited
 * InstanceDeclarationHierarchy per OPC UA Part 3 §6.3.3.2, computed at every nesting depth, with
 * every ModellingRule classified and every exclusion traceable through {@link #diagnostics()}.
 *
 * <p>Models are structural only — they contain no request state (no callbacks, root ids,
 * selections, or destinations) — and are safe to share and cache. {@link #modelRevision()} is a
 * content fingerprint: an unchanged type recompiles to an equal revision, and any change to a node
 * in {@link #dependencies()} produces a different one, which is what apply-time revalidation and
 * cache invalidation consume.
 */
public interface TypeInstantiationModel {

  /**
   * @return the {@link NodeId} of the TypeDefinition this model describes.
   */
  NodeId typeDefinitionId();

  /**
   * @return the content fingerprint of this model, for revalidation and cache invalidation.
   */
  long modelRevision();

  /**
   * @return the {@link NodeId} of every node this model was built from (the type, its supertype
   *     chain, declarations, member types, and interface types).
   */
  Set<NodeId> dependencies();

  /**
   * @return every declaration occurrence, browse-path ordered; deterministic across compilations.
   */
  List<InstanceDeclaration> declarations();

  /**
   * @param path the occurrence path to look up.
   * @return the declaration at {@code path}, if present.
   */
  Optional<InstanceDeclaration> get(BrowsePath path);

  /**
   * @return the placeholder extension points (OptionalPlaceholder / MandatoryPlaceholder
   *     declarations), browse-path ordered.
   */
  List<InstanceDeclaration> placeholders();

  /**
   * @return the hierarchical edges connecting declarations (and the type root), as a canonical
   *     logical set.
   */
  List<DeclarationEdge> hierarchy();

  /**
   * @return the non-hierarchy reference rows recorded from declarations, relative where both ends
   *     are in the model and absolute otherwise.
   */
  List<ReferenceRow> references();

  /**
   * @return the compile-time findings for this model; never contains {@link
   *     ModelDiagnostic.Severity#ERROR} entries (errors reject compilation instead).
   */
  List<ModelDiagnostic> diagnostics();

  /**
   * @return the attribute snapshot of the TypeDefinition node itself, the source of a new root
   *     instance's initial attributes per Part 3 §6.4.2.
   */
  AttributeSnapshot rootAttributes();

  /**
   * @return the type's {@code DefaultInstanceBrowseName} property value, from this type or the
   *     nearest supertype providing one; participates in root BrowseName precedence (request &gt;
   *     DefaultInstanceBrowseName &gt; type BrowseName).
   */
  Optional<QualifiedName> defaultInstanceBrowseName();
}

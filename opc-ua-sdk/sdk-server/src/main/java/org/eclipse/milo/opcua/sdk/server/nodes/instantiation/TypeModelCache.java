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

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.util.concurrent.UncheckedExecutionException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;

/**
 * A per-server cache of compiled {@link TypeInstantiationModel}s with dependency-aware transitive
 * invalidation.
 *
 * <p>Entries are keyed by TypeDefinition NodeId <em>within one server</em>: each server owns its
 * own instance (see {@link OpcUaServer#getTypeModelCache()}), so two servers in one JVM whose
 * address spaces use identical NodeIds for different types never share models. Each type's model is
 * cached individually — a supertype's model keeps its own entry rather than living only inside its
 * subtypes' entries.
 *
 * <p>Invalidation is driven by each cached model's {@link TypeInstantiationModel#dependencies()},
 * which already contains every node the model was compiled from at every depth (the type, its
 * supertype chain, declarations, member types and their closures). {@link #invalidate(NodeId)}
 * therefore drops every dependent model transitively: editing a supertype invalidates all cached
 * subtype models, and editing a member type invalidates every model whose dependency closure
 * contains it.
 *
 * <p>The invalidation contract: explicit {@link #invalidate(NodeId)} is how type changes reach this
 * cache. Server-internal mutation paths clear conservatively (registering or unregistering a {@code
 * NodeManager} clears the whole cache); address spaces that mutate type definitions out-of-band
 * must call {@link #invalidate(NodeId)} with the changed node's id themselves.
 *
 * <p>The cache is an optimization with correctness obligations, not a correctness mechanism:
 * entries may be dropped at any time (the next {@link #getOrCompile(NodeId)} simply recompiles),
 * and a plan raced by a concurrent type mutation is caught by apply-time {@link
 * TypeInstantiationModel#modelRevision()} revalidation, not by this cache.
 */
public class TypeModelCache {

  private final Cache<NodeId, TypeInstantiationModel> models = CacheBuilder.newBuilder().build();

  private final Supplier<TypeModelCompiler> compilerFactory;

  /**
   * Create a cache compiling from {@code server}'s address space.
   *
   * @param server the server whose types this cache holds models of.
   */
  public TypeModelCache(OpcUaServer server) {
    // A compiler snapshots the server's ReferenceTypeTree, which is lazily built from the address
    // space, so one is created per compilation rather than at cache construction.
    this(() -> new TypeModelCompiler(server));
  }

  /**
   * Create a cache obtaining a compiler from {@code compilerFactory}.
   *
   * @param compilerFactory supplies the compiler used when a requested model is absent; invoked
   *     once per compilation, so it may return a fresh compiler reflecting current server state.
   */
  TypeModelCache(Supplier<TypeModelCompiler> compilerFactory) {
    this.compilerFactory = compilerFactory;
  }

  /**
   * Get the cached model of the type identified by {@code typeDefinitionId}, compiling and caching
   * it if absent.
   *
   * <p>Concurrent calls for the same type share a single compilation and observe the same model
   * instance. Failed compilations are not cached; a subsequent call compiles again.
   *
   * @param typeDefinitionId the NodeId of an ObjectType or VariableType node.
   * @return the cached or freshly compiled model.
   * @throws ModelCompilationException if the type graph admits no correct instantiation (see {@link
   *     TypeModelCompiler#compile(NodeId)}).
   */
  public TypeInstantiationModel getOrCompile(NodeId typeDefinitionId)
      throws ModelCompilationException {

    try {
      return models.get(typeDefinitionId, () -> compilerFactory.get().compile(typeDefinitionId));
    } catch (ExecutionException e) {
      // compile(...)'s only checked exception; Guava wraps it here.
      throw (ModelCompilationException) e.getCause();
    } catch (UncheckedExecutionException e) {
      // An unchecked failure during compilation; unwrap to surface the original exception.
      throw (RuntimeException) e.getCause();
    }
  }

  /**
   * Get the cached model of the type identified by {@code typeDefinitionId}, if one is currently
   * cached; never compiles.
   *
   * @param typeDefinitionId the NodeId of an ObjectType or VariableType node.
   * @return the cached model, or empty if the type is absent or was invalidated.
   */
  public Optional<TypeInstantiationModel> getIfPresent(NodeId typeDefinitionId) {
    return Optional.ofNullable(models.getIfPresent(typeDefinitionId));
  }

  /**
   * Invalidate every cached model built from the node identified by {@code nodeId}, transitively:
   * any model whose {@link TypeInstantiationModel#dependencies()} contain the node is dropped, in
   * addition to the model of the type itself.
   *
   * <p>Call this after mutating a type definition node or any node reachable from one (supertypes,
   * instance declarations, member types) through a path this cache cannot observe.
   *
   * @param nodeId the {@link NodeId} of the changed node.
   */
  public void invalidate(NodeId nodeId) {
    models
        .asMap()
        .entrySet()
        .removeIf(e -> e.getKey().equals(nodeId) || e.getValue().dependencies().contains(nodeId));
  }

  /** Invalidate every cached model. */
  public void invalidateAll() {
    models.invalidateAll();
  }

  /**
   * @return the number of models currently cached.
   */
  public int size() {
    return (int) models.size();
  }
}

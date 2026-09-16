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

import static org.eclipse.milo.opcua.sdk.server.nodes.instantiation.TypeFixtures.path;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectTypeNode;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link TypeModelCache}: per-server isolation, dependency-aware transitive invalidation,
 * fingerprint behavior across invalidation, and the recompile-on-miss path that makes eviction
 * harmless.
 */
public class TypeModelCacheTest {

  private TypeFixtures fx;
  private TypeModelCache cache;

  @BeforeEach
  void setUp() {
    fx = TypeFixtures.create();
    cache = new TypeModelCache(fx::compiler);
  }

  /** A cache that counts how many times it asks its factory for a compiler. */
  private TypeModelCache newCountingCache(AtomicInteger compilerRequests) {
    return new TypeModelCache(
        () -> {
          compilerRequests.incrementAndGet();
          return fx.compiler();
        });
  }

  /** Model caching is per server; identical NodeIds in different servers never share models. */
  @Nested
  class Isolation {

    /**
     * The legacy {@code NodeFactory} cached InstanceDeclarationHierarchies in a static JVM-global
     * map, so two servers whose address spaces assign the same NodeId to different types served
     * each other's hierarchies. Per-server caches must not cross-contaminate.
     */
    @Test
    void cachesOverDifferentAddressSpacesDoNotShareModelsForTheSameNodeId() throws Exception {
      TypeFixtures fxB = TypeFixtures.create();
      TypeModelCache cacheB = new TypeModelCache(fxB::compiler);

      UaObjectTypeNode typeA = fx.addObjectType("CollidingType", NodeIds.BaseObjectType);
      fx.addVariableDeclaration(
          typeA, "OnlyInA", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);

      UaObjectTypeNode typeB = fxB.addObjectType("CollidingType", NodeIds.BaseObjectType);
      fxB.addVariableDeclaration(
          typeB, "OnlyInB", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);

      assertEquals(
          typeA.getNodeId(), typeB.getNodeId(), "precondition: the two types collide on NodeId");

      TypeInstantiationModel modelA = cache.getOrCompile(typeA.getNodeId());
      TypeInstantiationModel modelB = cacheB.getOrCompile(typeB.getNodeId());

      assertTrue(modelA.get(path("OnlyInA")).isPresent());
      assertTrue(
          modelA.get(path("OnlyInB")).isEmpty(),
          "server A's model must not contain server B's declaration");
      assertTrue(
          modelB.get(path("OnlyInB")).isPresent(),
          "server B compiled after A must get its own model, not A's cached one");
      assertTrue(
          modelB.get(path("OnlyInA")).isEmpty(),
          "server B's model must not contain server A's declaration");
    }
  }

  /**
   * {@link TypeModelCache#invalidate(NodeId)} drops every model whose dependency closure contains
   * the changed node — transitively, at every depth.
   */
  @Nested
  class Invalidation {

    /**
     * A model is compiled from its whole supertype chain, so editing a supertype must invalidate
     * every cached subtype model, including subtypes-of-subtypes.
     */
    @Test
    void supertypeEditInvalidatesAllDependentSubtypeModelsTransitively() throws Exception {
      UaObjectTypeNode base = fx.addObjectType("Base", NodeIds.BaseObjectType);
      UaObjectTypeNode derived = fx.addObjectType("Derived", base.getNodeId());
      UaObjectTypeNode grandchild = fx.addObjectType("Grandchild", derived.getNodeId());
      UaObjectTypeNode unrelated = fx.addObjectType("Unrelated", NodeIds.BaseObjectType);

      cache.getOrCompile(derived.getNodeId());
      cache.getOrCompile(grandchild.getNodeId());
      cache.getOrCompile(unrelated.getNodeId());

      fx.addVariableDeclaration(
          base, "NewMember", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);
      cache.invalidate(base.getNodeId());

      assertTrue(cache.getIfPresent(derived.getNodeId()).isEmpty());
      assertTrue(
          cache.getIfPresent(grandchild.getNodeId()).isEmpty(),
          "transitive: two subtype levels below the edited supertype");
      assertTrue(
          cache.getIfPresent(unrelated.getNodeId()).isPresent(),
          "control: a model not built from the edited node stays cached");

      assertTrue(
          cache.getOrCompile(grandchild.getNodeId()).get(path("NewMember")).isPresent(),
          "recompiling after invalidation must observe the supertype edit");
    }

    /**
     * Member types are compiled into the containing model at every nesting depth, so editing one
     * must invalidate every cached model whose dependency closure contains it.
     */
    @Test
    void memberTypeEditInvalidatesEveryModelWhoseClosureContainsIt() throws Exception {
      UaObjectTypeNode memberType = fx.addObjectType("MemberType", NodeIds.BaseObjectType);
      UaObjectTypeNode container = fx.addObjectType("Container", NodeIds.BaseObjectType);
      fx.addObjectDeclaration(
          container, "M", memberType.getNodeId(), NodeIds.ModellingRule_Mandatory);
      UaObjectTypeNode outer = fx.addObjectType("Outer", NodeIds.BaseObjectType);
      fx.addObjectDeclaration(outer, "C", container.getNodeId(), NodeIds.ModellingRule_Mandatory);
      UaObjectTypeNode unrelated = fx.addObjectType("Unrelated", NodeIds.BaseObjectType);

      cache.getOrCompile(container.getNodeId());
      cache.getOrCompile(outer.getNodeId());
      cache.getOrCompile(unrelated.getNodeId());

      fx.addVariableDeclaration(
          memberType, "NewMember", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);
      cache.invalidate(memberType.getNodeId());

      assertTrue(cache.getIfPresent(container.getNodeId()).isEmpty());
      assertTrue(
          cache.getIfPresent(outer.getNodeId()).isEmpty(),
          "transitive: the member type is two nesting levels below Outer");
      assertTrue(
          cache.getIfPresent(unrelated.getNodeId()).isPresent(),
          "control: a model not built from the edited member type stays cached");

      assertTrue(
          cache.getOrCompile(outer.getNodeId()).get(path("C", "M", "NewMember")).isPresent(),
          "recompiling after invalidation must observe the member-type edit");
    }

    /**
     * Invalidation keys on the full dependency set, not just type nodes: a change to any node a
     * model was built from — here an instance declaration — must invalidate it.
     */
    @Test
    void editToAnyNodeAModelWasBuiltFromInvalidatesIt() throws Exception {
      UaObjectTypeNode typeNode = fx.addObjectType("SomeType", NodeIds.BaseObjectType);
      NodeId declarationId =
          fx.addVariableDeclaration(
                  typeNode, "Member", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory)
              .getNodeId();

      cache.getOrCompile(typeNode.getNodeId());

      cache.invalidate(declarationId);

      assertTrue(
          cache.getIfPresent(typeNode.getNodeId()).isEmpty(),
          "a declaration node is part of the model's dependency closure");
    }
  }

  /**
   * {@link TypeInstantiationModel#modelRevision()} behavior through the cache: apply-time
   * revalidation depends on edits changing the fingerprint and no-op recompiles preserving it.
   */
  @Nested
  class Fingerprints {

    @Test
    void invalidateAndRecompileAfterAnEditYieldsANewFingerprint() throws Exception {
      UaObjectTypeNode typeNode = fx.addObjectType("EditedType", NodeIds.BaseObjectType);
      fx.addVariableDeclaration(
          typeNode, "Member", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);

      long before = cache.getOrCompile(typeNode.getNodeId()).modelRevision();

      fx.addVariableDeclaration(
          typeNode, "Extra", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);
      cache.invalidate(typeNode.getNodeId());

      long after = cache.getOrCompile(typeNode.getNodeId()).modelRevision();

      assertNotEquals(before, after, "an edited type must recompile to a new fingerprint");
    }

    @Test
    void unchangedTypeRecompilesToAnEqualFingerprint() throws Exception {
      UaObjectTypeNode typeNode = fx.addObjectType("StableType", NodeIds.BaseObjectType);
      fx.addVariableDeclaration(
          typeNode, "Member", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);

      long before = cache.getOrCompile(typeNode.getNodeId()).modelRevision();

      cache.invalidateAll();

      long after = cache.getOrCompile(typeNode.getNodeId()).modelRevision();

      assertEquals(
          before, after, "recompiling an unchanged type must not spuriously fail revalidation");
    }
  }

  /**
   * The cache is an optimization with correctness obligations, not a correctness mechanism: entries
   * may be dropped at any time and the recompile path restores an equivalent model.
   */
  @Nested
  class Eviction {

    @Test
    void evictionAtAnyPointIsHarmlessRecompileYieldsAnEquivalentModel() throws Exception {
      UaObjectTypeNode typeNode = fx.addObjectType("EvictedType", NodeIds.BaseObjectType);
      fx.addVariableDeclaration(
          typeNode, "Member", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);

      TypeInstantiationModel first = cache.getOrCompile(typeNode.getNodeId());

      cache.invalidateAll();
      assertEquals(0, cache.size(), "precondition: every entry evicted");

      TypeInstantiationModel second = cache.getOrCompile(typeNode.getNodeId());

      assertNotSame(first, second, "the entry was dropped, so this is a fresh compilation");
      assertEquals(first.modelRevision(), second.modelRevision());
      assertEquals(first.declarations(), second.declarations());
      assertEquals(first.hierarchy(), second.hierarchy());
      assertEquals(first.references(), second.references());
    }
  }

  /** Cache-hit and single-compilation semantics of {@link TypeModelCache#getOrCompile(NodeId)}. */
  @Nested
  class Compilation {

    @Test
    void repeatedGetsReturnTheCachedInstanceWithoutRecompiling() throws Exception {
      AtomicInteger compilerRequests = new AtomicInteger(0);
      TypeModelCache countingCache = newCountingCache(compilerRequests);

      UaObjectTypeNode typeNode = fx.addObjectType("CachedType", NodeIds.BaseObjectType);

      TypeInstantiationModel first = countingCache.getOrCompile(typeNode.getNodeId());
      TypeInstantiationModel second = countingCache.getOrCompile(typeNode.getNodeId());

      assertSame(first, second);
      assertEquals(1, compilerRequests.get(), "a cache hit must not compile again");
    }

    /**
     * Concurrent describes of one type must not compile two observably different models; callers
     * racing on the same key share a single compilation and observe one model instance.
     */
    @Test
    void concurrentGetsForTheSameTypeShareOneCompilation() throws Exception {
      AtomicInteger compilerRequests = new AtomicInteger(0);
      TypeModelCache countingCache = newCountingCache(compilerRequests);

      UaObjectTypeNode typeNode = fx.addObjectType("ContendedType", NodeIds.BaseObjectType);
      NodeId typeId = typeNode.getNodeId();

      int threads = 4;
      ExecutorService executor = Executors.newFixedThreadPool(threads);
      try {
        List<Callable<TypeInstantiationModel>> calls =
            Collections.nCopies(threads, () -> countingCache.getOrCompile(typeId));

        List<Future<TypeInstantiationModel>> results =
            executor.invokeAll(calls, 30, TimeUnit.SECONDS);

        TypeInstantiationModel first = results.get(0).get();
        for (Future<TypeInstantiationModel> result : results) {
          assertSame(first, result.get(), "every racing caller must observe the same model");
        }
        assertEquals(1, compilerRequests.get(), "racing callers must share one compilation");
      } finally {
        executor.shutdownNow();
      }
    }

    /**
     * Failed compilations propagate as {@link ModelCompilationException} and are not cached: once
     * the type exists, the same lookup succeeds.
     */
    @Test
    void failedCompilationIsNotCachedAndSucceedsOnceTheTypeExists() throws Exception {
      NodeId typeId = fx.newNodeId("NotYetAdded");

      assertThrows(ModelCompilationException.class, () -> cache.getOrCompile(typeId));

      UaObjectTypeNode typeNode = fx.addObjectType("NotYetAdded", NodeIds.BaseObjectType);
      assertEquals(typeId, typeNode.getNodeId(), "precondition: same NodeId as the failed lookup");

      assertEquals(typeId, cache.getOrCompile(typeId).typeDefinitionId());
    }
  }
}

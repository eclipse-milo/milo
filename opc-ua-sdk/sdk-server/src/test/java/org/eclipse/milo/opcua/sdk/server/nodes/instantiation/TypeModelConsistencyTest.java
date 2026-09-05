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
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectNode;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class TypeModelConsistencyTest {

  // An invalidation must fence loaders that already read the old declaration graph, including
  // loaders of dependent types whose dependencies are not known until compilation returns.
  @ParameterizedTest
  @ValueSource(booleans = {false, true})
  void invalidationDuringCompilationCannotPublishOrReturnTheOldModel(boolean invalidateAll)
      throws Exception {
    var fx = TypeFixtures.create();
    var base = fx.addObjectType("Base", NodeIds.BaseObjectType);
    var type = fx.addObjectType("Derived", base.getNodeId());
    var compiled = new CountDownLatch(1);
    var release = new CountDownLatch(1);
    var pauseFirst = new AtomicBoolean(true);
    fx.rebuildReferenceTypeTree();
    var cache =
        new TypeModelCache(
            () ->
                new TypeModelCompiler(fx.server()) {
                  @Override
                  public TypeInstantiationModel compile(NodeId typeId)
                      throws ModelCompilationException {
                    TypeInstantiationModel model = super.compile(typeId);
                    if (pauseFirst.compareAndSet(true, false)) {
                      compiled.countDown();
                      try {
                        if (!release.await(10, TimeUnit.SECONDS)) {
                          throw new AssertionError("test did not release compilation");
                        }
                      } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        throw new AssertionError(e);
                      }
                    }
                    return model;
                  }
                });
    when(fx.server().getTypeModelCache()).thenReturn(cache);
    var executor = Executors.newSingleThreadExecutor();
    try {
      var pending = executor.submit(() -> cache.getOrCompile(type.getNodeId()));
      assertTrue(compiled.await(10, TimeUnit.SECONDS));
      fx.addVariableDeclaration(
          base, "Added", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);
      if (invalidateAll) {
        cache.invalidateAll();
      } else {
        cache.invalidate(base.getNodeId());
      }
      release.countDown();
      assertTrue(pending.get(10, TimeUnit.SECONDS).get(path("Added")).isPresent());
      assertTrue(cache.getOrCompile(type.getNodeId()).get(path("Added")).isPresent());
      var result =
          fx.instantiator()
              .instantiate(
                  InstantiationRequest.of(UaObjectNode.class, type.getNodeId())
                      .nodeId(fx.newNodeId("Instance"))
                      .target(fx.newTargetManager())
                      .build());
      assertTrue(result.node(path("Added")).isPresent());
    } finally {
      release.countDown();
      executor.shutdownNow();
      assertTrue(executor.awaitTermination(10, TimeUnit.SECONDS));
    }
  }

  // String rendering collapses {"a, b"} and {"a", "b"}; apply must still reject the stale plan.
  @Test
  void arrayElementBoundariesChangeTheRevisionAndRejectAStalePlan() throws Exception {
    var fx = TypeFixtures.create();
    var type = fx.addObjectType("Strings", NodeIds.BaseObjectType);
    var declaration =
        fx.addVariableDeclaration(
            type, "V", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);
    declaration.setDataType(NodeIds.String);
    declaration.setValueRank(1);
    declaration.setValue(
        new DataValue(new Variant(new String[] {"a, b"}), StatusCode.GOOD, DateTime.MIN_VALUE));
    var target = fx.newTargetManager();
    var instantiator = fx.instantiator();
    var plan =
        instantiator.plan(
            InstantiationRequest.of(UaObjectNode.class, type.getNodeId())
                .nodeId(fx.newNodeId("Instance"))
                .target(target)
                .build());
    long original = fx.typeModelCache().getOrCompile(type.getNodeId()).modelRevision();
    declaration.setValue(
        new DataValue(new Variant(new String[] {"a", "b"}), StatusCode.GOOD, DateTime.MIN_VALUE));
    fx.typeModelCache().invalidate(declaration.getNodeId());
    assertNotEquals(original, fx.typeModelCache().getOrCompile(type.getNodeId()).modelRevision());
    var error = assertThrows(InstantiationException.class, () -> instantiator.apply(plan));
    assertTrue(
        error.getDiagnostics().stream()
            .anyMatch(d -> d.code() == InstantiationDiagnostic.Code.MODEL_CHANGED));
    assertTrue(target.getNodes().isEmpty());
  }
}

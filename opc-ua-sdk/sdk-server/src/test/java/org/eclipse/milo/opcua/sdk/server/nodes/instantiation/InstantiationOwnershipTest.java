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
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectNode;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.junit.jupiter.api.Test;

class InstantiationOwnershipTest {

  // A result retains historical provenance after ordinary node deletion. Reusing the same IDs
  // must not transfer ownership of the replacement instance or its references to the old result.
  @Test
  void deletingAnOldResultPreservesReplacementNodesAndReferences() throws Exception {
    var fx = TypeFixtures.create();
    var type = fx.addObjectType("Type", NodeIds.BaseObjectType);
    fx.addObjectDeclaration(type, "Child", NodeIds.BaseObjectType, NodeIds.ModellingRule_Mandatory);
    var target = fx.newTargetManager();
    fx.registerWithAddressSpace(target);
    var instantiator = fx.instantiator();
    var request =
        InstantiationRequest.of(UaObjectNode.class, type.getNodeId())
            .nodeId(fx.newNodeId("Instance"))
            .target(target)
            .build();
    var original = instantiator.instantiate(request);
    original.root().delete();
    assertTrue(target.getNodes().isEmpty(), "ordinary deletion removes the old graph");
    var replacement = instantiator.instantiate(request);
    List<Reference> replacementReferences =
        replacement.materializedNodes().stream()
            .flatMap(node -> target.getReferences(node.nodeId()).stream())
            .toList();

    original.deleteCreated();

    for (MaterializedNode node : replacement.materializedNodes()) {
      assertSame(node.node(), target.getNode(node.nodeId()).orElseThrow());
    }
    assertEquals(
        replacementReferences,
        replacement.materializedNodes().stream()
            .flatMap(node -> target.getReferences(node.nodeId()).stream())
            .toList());
    replacement.deleteCreated();
    assertTrue(target.getNodes().isEmpty(), "the replacement result still owns its graph");
  }

  // Reference equality describes an edge, not ownership of a later remove/re-add occurrence.
  @Test
  void deletingAnOldResultPreservesAnEqualReplacementReference() throws Exception {
    var fx = TypeFixtures.create();
    var type = fx.addObjectType("Type", NodeIds.BaseObjectType);
    var target = fx.newTargetManager();
    var result =
        fx.instantiator()
            .instantiate(
                InstantiationRequest.of(UaObjectNode.class, type.getNodeId())
                    .nodeId(fx.newNodeId("Instance"))
                    .target(target)
                    .build());
    Reference original =
        result.references().stream()
            .filter(MaterializedReference::added)
            .map(MaterializedReference::reference)
            .findFirst()
            .orElseThrow();
    target.removeReference(original);
    Reference replacement =
        new Reference(
            original.getSourceNodeId(),
            original.getReferenceTypeId(),
            original.getTargetNodeId(),
            original.getDirection());
    target.addReference(replacement);
    result.deleteCreated();
    assertEquals(List.of(replacement), target.getReferences(replacement.getSourceNodeId()));
  }

  // Binders run after commit and can invoke ordinary node APIs. A failing binder must not roll
  // back a replacement that another owner installed under one of the committed identifiers.
  @Test
  void rollbackPreservesANodeReplacedByAFailingBinder() throws Exception {
    var fx = TypeFixtures.create();
    var type = fx.addObjectType("Type", NodeIds.BaseObjectType);
    fx.addMethodDeclaration(type, "M", NodeIds.ModellingRule_Mandatory);
    var target = fx.newTargetManager();
    var replacement =
        new UaObjectNode(
            fx.context(),
            fx.newNodeId("Instance"),
            TypeFixtures.qn("Replacement"),
            LocalizedText.english("Replacement"),
            LocalizedText.NULL_VALUE,
            UInteger.MIN,
            UInteger.MIN);
    assertThrows(
        InstantiationException.class,
        () ->
            fx.instantiator()
                .instantiate(
                    InstantiationRequest.of(UaObjectNode.class, type.getNodeId())
                        .nodeId(replacement.getNodeId())
                        .target(target)
                        .bindMethod(
                            path("M"),
                            method -> {
                              target.addNode(replacement);
                              throw new IllegalStateException("binder failed after replacement");
                            })
                        .build()));
    assertSame(replacement, target.getNode(replacement.getNodeId()).orElseThrow());
  }
}

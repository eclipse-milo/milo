/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.aliases;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.UaNodeManager;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectNode;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class AliasLifecycleConsistencyTest extends AbstractClientServerTest {

  private final AtomicInteger sequence = new AtomicInteger();
  private final List<Reference> externalReferences = new ArrayList<>();
  private UaNodeManager nodeManager;
  private Set<NodeId> originalNodes;
  private TestVersionStore store;
  private AliasManager manager;
  private String prefix;

  @BeforeEach
  void prepareFixture() {
    testNamespace.configure((context, nodes) -> nodeManager = nodes);
    originalNodes = Set.copyOf(nodeManager.getNodeIds());
    prefix = "AliasConsistency" + sequence.incrementAndGet();
    store = new TestVersionStore();
    manager =
        new AliasManager(
            server,
            AliasManagerConfig.builder()
                .nodeNamespaceIndex(testNamespace.getNamespaceIndex())
                .versionStore(store)
                .configurationEnabled(true)
                .authorizationPolicy(
                    new AliasAuthorizationPolicy() {
                      @Override
                      public boolean checkFind(@Nullable Session session, NodeId categoryId) {
                        return true;
                      }

                      @Override
                      public boolean checkMutate(@Nullable Session session, NodeId categoryId) {
                        return true;
                      }
                    })
                .build());
  }

  @AfterEach
  void cleanFixture() {
    manager.shutdown();
    externalReferences.forEach(
        reference -> nodeManager.removeReferences(reference, server.getNamespaceTable()));
    externalReferences.clear();
    nodeManager.getNodes().stream()
        .filter(node -> !originalNodes.contains(node.getNodeId()))
        .forEach(UaNode::delete);
  }

  // AliasFor may be stored by either endpoint's registered manager. Successful public/wire
  // deletion must remove both directions there, or the next lookup still returns the target.
  @ParameterizedTest
  @ValueSource(booleans = {false, true})
  void deletingTargetRemovesReferencesOwnedByAnotherManager(boolean wire) throws Exception {
    manager.startup();
    NodeId first = newNodeId("TestInt32");
    NodeId second = newNodeId("TestAnalogValue");
    NodeId aliasId = manager.addAlias(NodeIds.Aliases, prefix, List.of(target(first)));
    Reference external =
        new Reference(second, NodeIds.AliasFor, aliasId.expanded(), Reference.Direction.INVERSE);
    managed(second).addReference(external);
    externalReferences.add(external);
    manager.touch(NodeIds.Aliases);
    assertEquals(Set.of(first, second), targets(NodeIds.Aliases, prefix));

    if (wire) {
      assertArrayEquals(
          new StatusCode[] {StatusCode.GOOD}, deleteOverWire(NodeIds.Aliases, prefix, second));
    } else {
      manager.deleteAlias(NodeIds.Aliases, prefix, List.of(target(second)));
    }

    assertEquals(Set.of(first), targets(NodeIds.Aliases, prefix));
    assertFalse(
        nodeManager.getReferences(second).contains(external),
        "inverse reference must also be removed");
  }

  // Whole-alias deletion must remove cross-manager references before the deterministic alias
  // NodeId is reused, or an old target silently becomes part of the replacement alias.
  @ParameterizedTest
  @ValueSource(booleans = {false, true})
  void deletingWholeAliasDoesNotResurrectTargetsFromAnotherManager(boolean wire) throws Exception {
    manager.startup();
    NodeId first = newNodeId("TestInt32");
    NodeId second = newNodeId("TestAnalogValue");
    NodeId aliasId = manager.addAlias(NodeIds.Aliases, prefix, List.of(target(first)));
    Reference external =
        new Reference(second, NodeIds.AliasFor, aliasId.expanded(), Reference.Direction.INVERSE);
    managed(second).addReference(external);
    externalReferences.add(external);
    manager.touch(NodeIds.Aliases);
    assertEquals(Set.of(first, second), targets(NodeIds.Aliases, prefix));

    if (wire) {
      assertArrayEquals(
          new StatusCode[] {StatusCode.GOOD}, deleteOverWire(NodeIds.Aliases, prefix, null));
    } else {
      manager.deleteAlias(NodeIds.Aliases, prefix, null);
    }
    assertTrue(server.getAddressSpaceManager().getManagedNode(aliasId).isEmpty());
    NodeId replacement = manager.addAlias(NodeIds.Aliases, prefix, List.of(target(first)));
    assertEquals(aliasId, replacement);
    assertEquals(
        Set.of(first), targets(NodeIds.Aliases, prefix), "deleted targets must not reappear");
    assertTrue(nodeManager.getReferences(aliasId).isEmpty());
    assertFalse(nodeManager.getReferences(second).contains(external));
  }

  // Removing the last target deletes the alias from every category, including Organizes links
  // stored outside the alias's manager. Reuse must not reconnect the deleted membership.
  @Test
  void deletingTargetlessAliasRemovesExternalOrganizingReferences() throws Exception {
    manager.startup();
    NodeId first = newNodeId("TestInt32");
    NodeId aliasId = manager.addAlias(NodeIds.Aliases, prefix, List.of(target(first)));
    AliasCategoryConfig other = category("Other", NodeIds.Aliases);
    manager.addCategory(other);
    Reference external =
        new Reference(
            other.categoryNodeId(),
            NodeIds.Organizes,
            aliasId.expanded(),
            Reference.Direction.FORWARD);
    managed(other.categoryNodeId()).addReference(external);
    externalReferences.add(external);
    manager.touch(other.categoryNodeId());
    assertEquals(Set.of(first), targets(other.categoryNodeId(), prefix));

    manager.deleteAlias(NodeIds.Aliases, prefix, List.of(target(first)));
    assertTrue(server.getAddressSpaceManager().getManagedNode(aliasId).isEmpty());
    assertEquals(aliasId, manager.addAlias(NodeIds.Aliases, prefix, List.of(target(first))));
    assertTrue(
        targets(other.categoryNodeId(), prefix).isEmpty(), "deleted membership must not reappear");
    assertFalse(nodeManager.getReferences(other.categoryNodeId()).contains(external));
    assertTrue(nodeManager.getReferences(aliasId).isEmpty());
  }

  // UaNode.delete traverses owned HasChild links. Global reference cleanup must preserve that
  // traversal so deleting an alias cannot strand a component in its own manager.
  @Test
  void deletingWholeAliasStillDeletesItsOwnedChildren() throws Exception {
    manager.startup();
    NodeId aliasId =
        manager.addAlias(NodeIds.Aliases, prefix, List.of(target(newNodeId("TestInt32"))));
    UaNode alias = managed(aliasId);
    NodeId childId = newNodeId(prefix + "Component");
    UaObjectNode child =
        new UaObjectNode.UaObjectNodeBuilder(alias.getNodeContext())
            .setNodeId(childId)
            .setBrowseName(newQualifiedName(prefix + "Component"))
            .setDisplayName(LocalizedText.english("component"))
            .build();
    alias.getNodeManager().addNode(child);
    alias.addReference(
        new Reference(
            aliasId, NodeIds.HasComponent, childId.expanded(), Reference.Direction.FORWARD));
    assertTrue(server.getAddressSpaceManager().getManagedNode(childId).isPresent());

    manager.deleteAlias(NodeIds.Aliases, prefix, null);

    assertTrue(server.getAddressSpaceManager().getManagedNode(aliasId).isEmpty());
    assertTrue(
        server.getAddressSpaceManager().getManagedNode(childId).isEmpty(),
        "owned child deletion must still follow HasComponent");
  }

  // A child's parent reference was created after its parent journal. Parent removal must detach
  // the surviving child so reuse of the parent's NodeId cannot silently reconnect its contents.
  @Test
  void removingParentDetachesSurvivingChildBeforeParentNodeIdReuse() throws Exception {
    manager.startup();
    AliasCategoryConfig parent = category("Parent", NodeIds.Aliases);
    AliasCategoryConfig child = category("Child", parent.categoryNodeId());
    manager.addCategory(parent);
    manager.addCategory(child);
    NodeId aliasId =
        manager.addAlias(child.categoryNodeId(), prefix, List.of(target(newNodeId("TestInt32"))));
    manager.removeCategory(parent.categoryNodeId());

    assertTrue(server.getAddressSpaceManager().getManagedNode(child.categoryNodeId()).isPresent());
    assertTrue(server.getAddressSpaceManager().getManagedNode(aliasId).isPresent());
    assertFalse(
        server
            .getAddressSpaceManager()
            .getManagedReferences(child.categoryNodeId(), Reference.ORGANIZED_BY_PREDICATE)
            .stream()
            .anyMatch(
                reference ->
                    reference.getTargetNodeId().equals(parent.categoryNodeId().expanded())));
    manager.addCategory(parent);
    assertTrue(manager.findAlias(parent.categoryNodeId(), prefix, null).isEmpty());
    assertEquals(Set.of(newNodeId("TestInt32")), targets(child.categoryNodeId(), prefix));
  }

  private AliasCategoryConfig category(String suffix, NodeId parent) {
    return new AliasCategoryConfig(
        newNodeId(prefix + suffix),
        parent,
        newQualifiedName(prefix + suffix),
        nodeManager,
        name -> newNodeId(prefix + suffix + "/" + name),
        true,
        false,
        true);
  }

  private AliasTarget target(NodeId id) {
    return new AliasTarget(id.expanded(), null, NodeIds.AliasFor);
  }

  private UaNode managed(NodeId id) {
    return server.getAddressSpaceManager().getManagedNode(id).orElseThrow();
  }

  private Set<NodeId> targets(NodeId category, String name) throws UaException {
    return manager.findAlias(category, name, null).stream()
        .flatMap(alias -> Arrays.stream(alias.getReferencedNodes()))
        .map(id -> id.toNodeId(server.getNamespaceTable()).orElseThrow())
        .collect(Collectors.toSet());
  }

  private StatusCode[] deleteOverWire(NodeId category, String name, @Nullable NodeId target)
      throws UaException {
    NodeId method =
        server
            .getAddressSpaceManager()
            .getManagedReferences(category, Reference.HAS_COMPONENT_PREDICATE)
            .stream()
            .map(
                reference ->
                    reference.getTargetNodeId().toNodeId(server.getNamespaceTable()).orElseThrow())
            .filter(id -> "DeleteAliasesFromCategory".equals(managed(id).getBrowseName().name()))
            .findFirst()
            .orElseThrow();
    CallMethodResult result =
        client.call(
                List.of(
                    new CallMethodRequest(
                        category,
                        method,
                        new Variant[] {
                          new Variant(new String[] {name}),
                          new Variant(
                              new ExpandedNodeId[] {target == null ? null : target.expanded()})
                        })))
            .getResults()[0];
    assertEquals(StatusCode.GOOD, result.getStatusCode());
    return (StatusCode[]) result.getOutputArguments()[0].value();
  }

  private static final class TestVersionStore implements AliasVersionStore {
    private final Map<ExpandedNodeId, UInteger> entries = new ConcurrentHashMap<>();

    @Override
    public Map<ExpandedNodeId, UInteger> load() {
      return Map.copyOf(entries);
    }

    @Override
    public void save(ExpandedNodeId categoryId, UInteger value) throws UaException {
      entries.put(categoryId, value);
    }
  }
}

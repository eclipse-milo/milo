/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.test.aliases;

import static org.eclipse.milo.opcua.sdk.test.aliases.AliasTestSupport.assertStrictlyGreater;
import static org.eclipse.milo.opcua.sdk.test.aliases.AliasTestSupport.readAttribute;
import static org.eclipse.milo.opcua.sdk.test.aliases.AliasTestSupport.readLastChange;
import static org.eclipse.milo.opcua.sdk.test.aliases.AliasTestSupport.requireLastChange;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.server.UaNodeManager;
import org.eclipse.milo.opcua.sdk.server.aliases.AliasCategory;
import org.eclipse.milo.opcua.sdk.server.aliases.AliasCategoryConfig;
import org.eclipse.milo.opcua.sdk.server.aliases.AliasManager;
import org.eclipse.milo.opcua.sdk.server.aliases.AliasManagerConfig;
import org.eclipse.milo.opcua.sdk.server.aliases.AliasTarget;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseDirection;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseResultMask;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.AliasNameDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseResult;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.ReferenceDescription;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Integration tests for {@link AliasManager}'s programmatic mutation API ({@code addAlias}, {@code
 * deleteAlias}, {@code addCategory}, {@code removeCategory}, {@code touch}) and the {@code
 * LastChange} version invariant, exercised against a running client/server pair.
 *
 * <p>The manager is started once for the class, after the server, and shut down before it; tests
 * use per-test alias and category names, and assert version changes relative to values captured
 * within the same test, so they are order-independent.
 */
// Fields below are assigned in @BeforeAll, which the nullability inspection does not model.
@SuppressWarnings("NotNullFieldNotInitialized")
class AliasMutationTest extends AbstractClientServerTest {

  private RecordingVersionStore versionStore;
  private AliasManager aliasManager;
  private UaNodeManager testNodeManager;

  @BeforeAll
  void startAliasManager() {
    testNamespace.configure((context, nodeManager) -> testNodeManager = nodeManager);

    versionStore = new RecordingVersionStore(server);

    AliasManagerConfig config =
        AliasManagerConfig.builder()
            .versionStore(versionStore)
            .nodeNamespaceIndex(testNamespace.getNamespaceIndex())
            .build();

    aliasManager = new AliasManager(server, config);
    aliasManager.startup();
  }

  @AfterAll
  void shutdownAliasManager() {
    aliasManager.shutdown();
  }

  @Nested
  class AddAlias {

    // WHY: pins the AddressSpace-fragment hosting rule: alias Nodes created in a standard (ns=0)
    // category live in the manager's own fragment, and Read and Browse service calls must still
    // route to them (Part 17 §6.3.1 alias model; fragment registration design invariant).
    @Test
    void addAliasToStandardCategoryCreatesClientBrowseableAliasNode() throws Exception {
      NodeId target = newNodeId("TestInt32");

      NodeId aliasNodeId =
          aliasManager.addAlias(NodeIds.Aliases, "BrowseableAlias", List.of(aliasFor(target)));

      // The client can Read the alias Node's BrowseName attribute.
      ReadResponse response =
          client.read(
              0.0,
              TimestampsToReturn.Neither,
              List.of(
                  new ReadValueId(
                      aliasNodeId, AttributeId.BrowseName.uid(), null, QualifiedName.NULL_VALUE)));

      DataValue[] results = response.getResults();
      assertNotNull(results);
      DataValue browseNameValue = results[0];
      assertTrue(
          browseNameValue.statusCode().isGood(),
          () -> "BrowseName read failed: " + browseNameValue.statusCode());
      assertEquals(
          new QualifiedName(aliasNodeId.getNamespaceIndex(), "BrowseableAlias"),
          browseNameValue.value().value());

      // The client can Browse the alias Node's forward AliasFor Reference to the target.
      BrowseResult aliasBrowse =
          client.browse(
              new BrowseDescription(
                  aliasNodeId,
                  BrowseDirection.Forward,
                  NodeIds.AliasFor,
                  true,
                  uint(0),
                  uint(BrowseResultMask.All.getValue())));

      assertTrue(
          aliasBrowse.getStatusCode().isGood(),
          () -> "Browse failed: " + aliasBrowse.getStatusCode());
      ReferenceDescription[] aliasReferences = aliasBrowse.getReferences();
      assertNotNull(aliasReferences);
      assertEquals(1, aliasReferences.length);
      assertEquals(
          target,
          aliasReferences[0].getNodeId().toNodeId(server.getNamespaceTable()).orElseThrow());

      // Browsing the standard category shows the alias, even though the category Node and the
      // alias Node live in different NodeManagers (composite Reference gathering).
      BrowseResult categoryBrowse =
          client.browse(
              new BrowseDescription(
                  NodeIds.Aliases,
                  BrowseDirection.Forward,
                  NodeIds.Organizes,
                  true,
                  uint(0),
                  uint(BrowseResultMask.All.getValue())));

      assertTrue(
          categoryBrowse.getStatusCode().isGood(),
          () -> "Browse failed: " + categoryBrowse.getStatusCode());
      ReferenceDescription[] categoryReferences = categoryBrowse.getReferences();
      assertNotNull(categoryReferences);
      boolean organized =
          Stream.of(categoryReferences)
              .anyMatch(
                  reference ->
                      aliasNodeId.equals(
                          reference.getNodeId().toNodeId(server.getNamespaceTable()).orElse(null)));
      assertTrue(organized, "Aliases category does not organize the created alias Node");
    }

    // WHY: Part 17 §6.2 — "The string part of the BrowseName shall be the DisplayName with an
    // empty locale id and no other locale shall be provided." A locale like "en" violates the
    // spec and breaks Clients that compare the DisplayName as a whole LocalizedText.
    @Test
    void aliasDisplayNameIsBrowseNameTextWithEmptyLocale() throws Exception {
      NodeId aliasNodeId =
          aliasManager.addAlias(
              NodeIds.Aliases, "EmptyLocaleAlias", List.of(aliasFor(newNodeId("TestInt32"))));

      DataValue value = readAttribute(client, aliasNodeId, AttributeId.DisplayName);
      Object rawDisplayName = value.value().value();
      assertNotNull(rawDisplayName);
      LocalizedText displayName = (LocalizedText) rawDisplayName;

      assertEquals("EmptyLocaleAlias", displayName.text());
      assertNull(displayName.locale(), "Part 17 §6.2 requires an empty locale id");
    }

    // WHY: Part 17 §6.3.4 — adding an alias that already exists with the same target is not a
    // change; the same Node must be reused and LastChange must not advance (no store write, no
    // Property change).
    @Test
    void identicalReAddReturnsSameNodeIdWithoutBumpingLastChange() throws Exception {
      NodeId target = newNodeId("TestInt32");

      NodeId first =
          aliasManager.addAlias(NodeIds.Aliases, "IdempotentAlias", List.of(aliasFor(target)));

      UInteger before = requireLastChange(server, NodeIds.Aliases);
      int savesBefore = versionStore.saves().size();

      NodeId second =
          aliasManager.addAlias(NodeIds.Aliases, "IdempotentAlias", List.of(aliasFor(target)));

      assertEquals(first, second);
      assertEquals(before, readLastChange(server, NodeIds.Aliases));
      assertEquals(savesBefore, versionStore.saves().size());
    }

    // WHY: Part 17 §6.3.4 — adding an existing alias name with a new target extends the alias
    // with another AliasFor Reference, which is an AddressSpace change and must bump LastChange
    // (§6.3.1: LastChange covers changes to alias References).
    @Test
    void addingNewTargetToExistingAliasAddsReferenceAndBumpsLastChange() throws Exception {
      NodeId firstTarget = newNodeId("TestInt32");
      NodeId secondTarget = newNodeId("TestAnalogValue");

      NodeId aliasNodeId =
          aliasManager.addAlias(NodeIds.Aliases, "ExtendedAlias", List.of(aliasFor(firstTarget)));

      UInteger before = requireLastChange(server, NodeIds.Aliases);

      NodeId again =
          aliasManager.addAlias(NodeIds.Aliases, "ExtendedAlias", List.of(aliasFor(secondTarget)));

      assertEquals(aliasNodeId, again);
      assertEquals(2, aliasForReferences(aliasNodeId).size());
      assertStrictlyGreater(before, readLastChange(server, NodeIds.Aliases));
    }
  }

  @Nested
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  class AddAliasValidation {

    // WHY: Part 17 §6.3.4 defines the per-alias failure statuses for invalid add requests, and
    // Part 17 §9.3 restricts TagVariables to aliases whose AliasFor References point to
    // Variables; the programmatic path must reject each case with the corresponding status.
    @ParameterizedTest(name = "{0}")
    @MethodSource("invalidTargets")
    void addAliasRejectsInvalidTarget(
        String description,
        NodeId categoryId,
        String aliasName,
        AliasTarget target,
        long expectedStatus) {

      UaException e =
          assertThrows(
              UaException.class,
              () -> aliasManager.addAlias(categoryId, aliasName, List.of(target)));

      assertEquals(expectedStatus, e.getStatusCode().getValue(), description);
    }

    Stream<Arguments> invalidTargets() {
      return Stream.of(
          Arguments.of(
              "non-AliasFor ReferenceType is rejected",
              NodeIds.Aliases,
              "InvalidRefTypeAlias",
              new AliasTarget(newNodeId("TestInt32").expanded(), null, NodeIds.Organizes),
              StatusCodes.Bad_InvalidArgument),
          Arguments.of(
              "unknown local target is rejected",
              NodeIds.Aliases,
              "UnknownTargetAlias",
              aliasFor(newNodeId("NoSuchTargetNode")),
              StatusCodes.Bad_NodeIdUnknown),
          Arguments.of(
              "remote target is rejected",
              NodeIds.Aliases,
              "RemoteTargetAlias",
              new AliasTarget(
                  newNodeId("TestInt32").expanded(), "urn:remote:server", NodeIds.AliasFor),
              StatusCodes.Bad_NotSupported),
          Arguments.of(
              "Method target under TagVariables violates the NodeClass constraint",
              NodeIds.TagVariables,
              "MethodTagAlias",
              aliasFor(newNodeId("sqrt(x)")),
              StatusCodes.Bad_InvalidArgument));
    }

    // WHY: Part 17 §9.3 — TagVariables restricts alias targets to Variables; a Variable target
    // is the allowed case and must succeed, proving the constraint rejects NodeClass, not the
    // category itself.
    @Test
    void variableTargetUnderTagVariablesIsAccepted() throws Exception {
      NodeId aliasNodeId =
          aliasManager.addAlias(
              NodeIds.TagVariables, "VariableTagAlias", List.of(aliasFor(newNodeId("TestInt32"))));

      assertTrue(server.getAddressSpaceManager().getManagedNode(aliasNodeId).isPresent());
    }
  }

  @Nested
  class DeleteAlias {

    // WHY: Part 17 §6.3.5 delete semantics — removing one explicit target only removes that
    // AliasFor Reference; the alias Object survives while other targets remain, and the change
    // bumps the category's LastChange (§6.3.1).
    @Test
    void removingOneExplicitTargetKeepsAliasNodeWhileAnotherTargetRemains() throws Exception {
      NodeId keptTarget = newNodeId("TestInt32");
      NodeId removedTarget = newNodeId("TestAnalogValue");

      NodeId aliasNodeId =
          aliasManager.addAlias(
              NodeIds.Aliases,
              "PartialDeleteAlias",
              List.of(aliasFor(keptTarget), aliasFor(removedTarget)));

      UInteger before = requireLastChange(server, NodeIds.Aliases);

      aliasManager.deleteAlias(
          NodeIds.Aliases, "PartialDeleteAlias", List.of(aliasFor(removedTarget)));

      assertTrue(server.getAddressSpaceManager().getManagedNode(aliasNodeId).isPresent());

      List<Reference> remaining = aliasForReferences(aliasNodeId);
      assertEquals(1, remaining.size());
      assertEquals(keptTarget.expanded(), remaining.get(0).getTargetNodeId());

      assertStrictlyGreater(before, readLastChange(server, NodeIds.Aliases));
    }

    // WHY: Part 17 §6.3.1 — an alias without at least one AliasFor Reference violates the model,
    // so removing the last target deletes the alias Object from EVERY organizing category, and
    // every one of those categories (plus their ancestors) gets a LastChange bump.
    @Test
    void removingLastTargetDeletesAliasFromEveryOrganizingCategoryAndBumpsEach() throws Exception {
      AliasCategory catA = aliasManager.addCategory(categoryConfig("DeleteCatA", NodeIds.Aliases));
      AliasCategory catB = aliasManager.addCategory(categoryConfig("DeleteCatB", NodeIds.Aliases));

      NodeId target = newNodeId("TestInt32");
      NodeId aliasNodeId =
          aliasManager.addAlias(catA.nodeId(), "SharedAlias", List.of(aliasFor(target)));

      // Organize the alias by a second category, the multi-parent arrangement §6.3.1 allows.
      UaNode aliasNode = server.getAddressSpaceManager().getManagedNode(aliasNodeId).orElseThrow();
      aliasNode.addReference(
          new Reference(
              aliasNodeId,
              NodeIds.Organizes,
              catB.nodeId().expanded(),
              Reference.Direction.INVERSE));

      UInteger rootBefore = requireLastChange(server, NodeIds.Aliases);
      UInteger catABefore = requireLastChange(server, catA.nodeId());
      UInteger catBBefore = requireLastChange(server, catB.nodeId());

      aliasManager.deleteAlias(catA.nodeId(), "SharedAlias", List.of(aliasFor(target)));

      assertTrue(server.getAddressSpaceManager().getManagedNode(aliasNodeId).isEmpty());
      assertTrue(aliasManager.findAlias(catB.nodeId(), "SharedAlias", null).isEmpty());

      assertStrictlyGreater(catABefore, readLastChange(server, catA.nodeId()));
      assertStrictlyGreater(catBBefore, readLastChange(server, catB.nodeId()));
      assertStrictlyGreater(rootBefore, readLastChange(server, NodeIds.Aliases));
    }

    // WHY: only alias linkage (AliasFor or a subtype) may be removed through deleteAlias; a
    // caller-supplied structural ReferenceType like HasTypeDefinition must be rejected with
    // Bad_InvalidArgument BEFORE anything is removed, or the alias Node would be corrupted and
    // become invisible to lookup (Part 17 §6.3.1 model integrity).
    @Test
    void nonAliasForReferenceTypeIsRejectedAndAliasNodeIsLeftUndamaged() throws Exception {
      NodeId target = newNodeId("TestInt32");

      NodeId aliasNodeId =
          aliasManager.addAlias(NodeIds.Aliases, "GuardedAlias", List.of(aliasFor(target)));

      UInteger before = requireLastChange(server, NodeIds.Aliases);

      // The malicious shape: the alias's own HasTypeDefinition Reference expressed as a target.
      UaException e =
          assertThrows(
              UaException.class,
              () ->
                  aliasManager.deleteAlias(
                      NodeIds.Aliases,
                      "GuardedAlias",
                      List.of(
                          new AliasTarget(
                              NodeIds.AliasNameType.expanded(), null, NodeIds.HasTypeDefinition))));

      assertEquals(StatusCodes.Bad_InvalidArgument, e.getStatusCode().getValue());

      // The HasTypeDefinition Reference is intact.
      List<Reference> typeDefinitions =
          server
              .getAddressSpaceManager()
              .getManagedReferences(aliasNodeId, Reference.HAS_TYPE_DEFINITION_PREDICATE);
      assertEquals(1, typeDefinitions.size());
      assertEquals(NodeIds.AliasNameType.expanded(), typeDefinitions.get(0).getTargetNodeId());

      // The alias is still found by lookup (which depends on the type definition)...
      List<AliasNameDataType> found = aliasManager.findAlias(NodeIds.Aliases, "GuardedAlias", null);
      assertEquals(1, found.size());
      assertEquals("GuardedAlias", found.get(0).getAliasName().name());

      // ...and by the idempotent add path, which returns the same undamaged Node.
      NodeId reFound =
          aliasManager.addAlias(NodeIds.Aliases, "GuardedAlias", List.of(aliasFor(target)));
      assertEquals(aliasNodeId, reFound);

      // Nothing changed, so nothing bumped.
      assertEquals(before, readLastChange(server, NodeIds.Aliases));
    }
  }

  @Nested
  class AddCategory {

    // WHY: AliasCategoryConfig's aliasNodeIdFactory is reserved for alias Nodes — the category
    // Node's NodeId comes from categoryNodeId — so category creation must not consume a factory
    // invocation. A stateful (e.g. sequence-allocating) factory stays aligned with the aliases
    // actually created, and an alias may share its name with its category without colliding on
    // the category's NodeId.
    @Test
    void addCategoryDoesNotInvokeAliasNodeIdFactoryAndAliasMayShareCategoryName() throws Exception {
      var factoryInvocations = new AtomicInteger();
      NodeId categoryId = newNodeId("FactoryReservedCat");

      AliasCategory category =
          aliasManager.addCategory(
              new AliasCategoryConfig(
                  categoryId,
                  NodeIds.Aliases,
                  newQualifiedName("FactoryReservedCat"),
                  testNodeManager,
                  aliasName -> {
                    factoryInvocations.incrementAndGet();
                    return newNodeId("FactoryReservedCat/alias/" + aliasName);
                  },
                  false,
                  false,
                  false));
      try {
        assertEquals(categoryId, category.nodeId());
        assertEquals(
            0,
            factoryInvocations.get(),
            "addCategory must not invoke the aliasNodeIdFactory for the category Node");

        // An alias whose name is identical to the category's name text.
        NodeId aliasNodeId =
            aliasManager.addAlias(
                categoryId, "FactoryReservedCat", List.of(aliasFor(newNodeId("TestInt32"))));

        assertEquals(1, factoryInvocations.get());
        assertNotEquals(categoryId, aliasNodeId);
        assertTrue(server.getAddressSpaceManager().getManagedNode(aliasNodeId).isPresent());
      } finally {
        server
            .getAddressSpaceManager()
            .getManagedNode(newNodeId("FactoryReservedCat/alias/FactoryReservedCat"))
            .ifPresent(UaNode::delete);
        aliasManager.removeCategory(categoryId);
      }
    }
  }

  @Nested
  class RemoveCategory {

    // WHY: removeCategory's contract keeps after-added alias Nodes alive while deleting the
    // created category Node; an Organizes linkage left behind would be a Reference targeting a
    // deleted NodeId — dangling for Browse, and silently reattaching the alias if a category
    // with the same NodeId were created later — so the manager must detach every directly
    // organized alias from both sides before deleting the category.
    @Test
    void removeCategoryDetachesSurvivingAliasesFromTheDeletedCategory() throws Exception {
      AliasCategory category =
          aliasManager.addCategory(categoryConfig("DetachCat", NodeIds.Aliases));
      NodeId categoryId = category.nodeId();

      NodeId aliasNodeId =
          aliasManager.addAlias(
              categoryId, "DetachedAlias", List.of(aliasFor(newNodeId("TestInt32"))));
      try {
        aliasManager.removeCategory(categoryId);

        // The category Node is gone, but the alias Node survives, per the contract.
        assertTrue(server.getAddressSpaceManager().getManagedNode(categoryId).isEmpty());
        assertTrue(server.getAddressSpaceManager().getManagedNode(aliasNodeId).isPresent());

        // No Reference on the alias Node targets the deleted category NodeId anymore.
        List<Reference> danglingReferences =
            server
                .getAddressSpaceManager()
                .getManagedReferences(
                    aliasNodeId,
                    reference ->
                        categoryId.equals(
                            reference
                                .getTargetNodeId()
                                .toNodeId(server.getNamespaceTable())
                                .orElse(null)));
        assertEquals(List.of(), danglingReferences);
      } finally {
        server.getAddressSpaceManager().getManagedNode(aliasNodeId).ifPresent(UaNode::delete);
        if (server.getAddressSpaceManager().getManagedNode(categoryId).isPresent()) {
          aliasManager.removeCategory(categoryId);
        }
      }
    }
  }

  @Nested
  class LastChange {

    // WHY: Part 17 §9.2 — the LastChange Property is mandatory on the root Aliases instance; the
    // manager must initialize (or restore) it at startup and seed the persistent store.
    @Test
    void rootAliasesLastChangeIsNonNullAfterStartup() {
      assertNotNull(readLastChange(server, NodeIds.Aliases));
      assertTrue(
          versionStore.load().containsKey(NodeIds.Aliases.expanded(server.getNamespaceTable())));
    }

    // WHY: Part 4 §7.43 — VersionTime values must let a Client detect every change, so each
    // mutation must produce a strictly greater value even within the same wall-clock second.
    @Test
    void everyMutationBumpsRootLastChangeStrictlyMonotonically() throws Exception {
      NodeId firstTarget = newNodeId("TestInt32");
      NodeId secondTarget = newNodeId("TestAnalogValue");

      var observed = new ArrayList<UInteger>();
      observed.add(requireLastChange(server, NodeIds.Aliases));

      aliasManager.addAlias(NodeIds.Aliases, "MonotonicAlias", List.of(aliasFor(firstTarget)));
      observed.add(requireLastChange(server, NodeIds.Aliases));

      aliasManager.addAlias(NodeIds.Aliases, "MonotonicAlias", List.of(aliasFor(secondTarget)));
      observed.add(requireLastChange(server, NodeIds.Aliases));

      aliasManager.deleteAlias(NodeIds.Aliases, "MonotonicAlias", List.of(aliasFor(secondTarget)));
      observed.add(requireLastChange(server, NodeIds.Aliases));

      aliasManager.deleteAlias(NodeIds.Aliases, "MonotonicAlias", null);
      observed.add(requireLastChange(server, NodeIds.Aliases));

      for (int i = 0; i < observed.size() - 1; i++) {
        UInteger earlier = observed.get(i);
        UInteger later = observed.get(i + 1);
        assertTrue(
            later.longValue() > earlier.longValue(),
            () -> "expected strictly increasing LastChange, got " + observed);
      }
    }

    // WHY: Part 17 §6.3.1 — LastChange reflects changes to the category "or any AliasNames below
    // it", so a mutation in a nested category must bump every category on the Organizes chain up
    // to the root Aliases Object.
    @Test
    void addAliasInNestedCategoryBumpsParentChainUpToRoot() throws Exception {
      AliasCategory child = aliasManager.addCategory(categoryConfig("PropChild", NodeIds.Aliases));
      AliasCategory grandchild =
          aliasManager.addCategory(categoryConfig("PropGrandchild", child.nodeId()));

      UInteger rootBefore = requireLastChange(server, NodeIds.Aliases);
      UInteger childBefore = requireLastChange(server, child.nodeId());
      UInteger grandchildBefore = requireLastChange(server, grandchild.nodeId());

      aliasManager.addAlias(
          grandchild.nodeId(), "PropagationAlias", List.of(aliasFor(newNodeId("TestInt32"))));

      assertStrictlyGreater(grandchildBefore, readLastChange(server, grandchild.nodeId()));
      assertStrictlyGreater(childBefore, readLastChange(server, child.nodeId()));
      assertStrictlyGreater(rootBefore, readLastChange(server, NodeIds.Aliases));
    }

    // WHY: Part 17 §9.2 requires the root LastChange value to survive restarts; persisting AFTER
    // publishing would open a window where a Client caches a version that is lost by a crash and
    // silently regresses. The store must therefore see each value before the Property does.
    @Test
    void lastChangeIsPersistedToStoreBeforeThePropertyPublishesIt() throws Exception {
      int savesBefore = versionStore.saves().size();

      aliasManager.touch(NodeIds.Aliases);

      List<RecordingVersionStore.SaveRecord> newSaves =
          versionStore.saves().subList(savesBefore, versionStore.saves().size()).stream()
              .filter(record -> NodeIds.Aliases.equals(record.categoryId()))
              .toList();

      assertEquals(1, newSaves.size());
      RecordingVersionStore.SaveRecord record = newSaves.get(0);

      // At save time the Property still showed the previous value...
      assertNotEquals(record.value(), record.lastChangeAtSave());
      // ...and afterwards the Property shows exactly the persisted value.
      assertEquals(record.value(), readLastChange(server, NodeIds.Aliases));
      assertEquals(record.value(), versionStore.load().get(record.storeKey()));

      // The store key is namespace-URI-qualified, so the persisted entry stays valid across
      // restarts even when the namespace table assigns the namespace a different index.
      assertTrue(
          record.storeKey().isAbsolute(),
          () -> "expected a URI-qualified store key, got " + record.storeKey().toParseableString());
    }

    // WHY: Part 17 §6.3.1 — removing a category is a change below its ancestors, so the
    // surviving ancestor chain must be bumped even though the removed category itself is gone.
    @Test
    void removeCategoryBumpsSurvivingAncestorCategories() throws Exception {
      AliasCategory category =
          aliasManager.addCategory(categoryConfig("RemovableCategory", NodeIds.Aliases));

      UInteger rootBefore = requireLastChange(server, NodeIds.Aliases);

      aliasManager.removeCategory(category.nodeId());

      assertTrue(server.getAddressSpaceManager().getManagedNode(category.nodeId()).isEmpty());
      assertStrictlyGreater(rootBefore, readLastChange(server, NodeIds.Aliases));

      // Removal also deletes the category's persisted entry, so durable stores do not
      // accumulate entries for categories that no longer exist.
      assertFalse(
          versionStore.load().containsKey(category.nodeId().expanded(server.getNamespaceTable())));
    }

    // WHY: a target change is observable from EVERY category that organizes the alias, not just
    // the one the mutation was addressed to (design ambiguity-resolution principle; Part 17
    // §6.3.1's LastChange contract covers changes to the aliases below a category), so adding or
    // removing a target through one organizing category must bump the other organizing
    // category's LastChange too.
    @Test
    void targetChangeThroughOneCategoryBumpsLastChangeOfEveryOrganizingCategory() throws Exception {
      AliasCategory catA =
          aliasManager.addCategory(categoryConfig("SharedTargetCatA", NodeIds.Aliases));
      AliasCategory catB =
          aliasManager.addCategory(categoryConfig("SharedTargetCatB", NodeIds.Aliases));

      NodeId keptTarget = newNodeId("TestInt32");
      NodeId extraTarget = newNodeId("TestAnalogValue");

      NodeId aliasNodeId =
          aliasManager.addAlias(catA.nodeId(), "SharedTargetAlias", List.of(aliasFor(keptTarget)));
      try {
        // Organize the alias by a second category, the multi-parent arrangement §6.3.1 allows.
        UaNode aliasNode =
            server.getAddressSpaceManager().getManagedNode(aliasNodeId).orElseThrow();
        aliasNode.addReference(
            new Reference(
                aliasNodeId,
                NodeIds.Organizes,
                catB.nodeId().expanded(),
                Reference.Direction.INVERSE));

        UInteger catBBeforeAdd = requireLastChange(server, catB.nodeId());

        aliasManager.addAlias(catA.nodeId(), "SharedTargetAlias", List.of(aliasFor(extraTarget)));

        assertStrictlyGreater(catBBeforeAdd, readLastChange(server, catB.nodeId()));

        UInteger catBBeforeDelete = requireLastChange(server, catB.nodeId());

        aliasManager.deleteAlias(
            catA.nodeId(), "SharedTargetAlias", List.of(aliasFor(extraTarget)));

        // The alias survives with its remaining target — this was a target change, not a
        // deletion — and the un-addressed organizing category is still bumped.
        assertTrue(server.getAddressSpaceManager().getManagedNode(aliasNodeId).isPresent());
        assertEquals(1, aliasForReferences(aliasNodeId).size());
        assertStrictlyGreater(catBBeforeDelete, readLastChange(server, catB.nodeId()));
      } finally {
        server.getAddressSpaceManager().getManagedNode(aliasNodeId).ifPresent(UaNode::delete);
        aliasManager.removeCategory(catA.nodeId());
        aliasManager.removeCategory(catB.nodeId());
      }
    }

    // WHY: touch is the documented escape hatch for out-of-band AddressSpace edits (Part 17
    // §6.3.1 requires LastChange to cover them); it must bump even though the manager itself
    // observed no mutation.
    @Test
    void touchBumpsCategoryLastChange() throws Exception {
      UInteger before = requireLastChange(server, NodeIds.Aliases);

      aliasManager.touch(NodeIds.Aliases);

      assertStrictlyGreater(before, readLastChange(server, NodeIds.Aliases));
    }
  }

  private AliasTarget aliasFor(NodeId targetNodeId) {
    return new AliasTarget(targetNodeId.expanded(), null, NodeIds.AliasFor);
  }

  private AliasCategoryConfig categoryConfig(String name, NodeId parentCategoryId) {
    return new AliasCategoryConfig(
        newNodeId(name),
        parentCategoryId,
        newQualifiedName(name),
        testNodeManager,
        aliasName -> newNodeId(name + "/" + aliasName),
        true,
        false,
        false);
  }

  /** The forward {@code AliasFor} References of the alias Node, across all NodeManagers. */
  private List<Reference> aliasForReferences(NodeId aliasNodeId) {
    return server
        .getAddressSpaceManager()
        .getManagedReferences(
            aliasNodeId,
            reference ->
                reference.isForward() && NodeIds.AliasFor.equals(reference.getReferenceTypeId()));
  }
}

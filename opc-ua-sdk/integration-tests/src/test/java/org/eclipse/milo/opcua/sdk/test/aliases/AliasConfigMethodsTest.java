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

import static java.util.Objects.requireNonNull;
import static org.eclipse.milo.opcua.sdk.test.aliases.AliasTestSupport.assertStrictlyGreater;
import static org.eclipse.milo.opcua.sdk.test.aliases.AliasTestSupport.readAttribute;
import static org.eclipse.milo.opcua.sdk.test.aliases.AliasTestSupport.readLastChange;
import static org.eclipse.milo.opcua.sdk.test.aliases.AliasTestSupport.requireLastChange;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.server.AddressSpaceComposite;
import org.eclipse.milo.opcua.sdk.server.AddressSpaceFilter;
import org.eclipse.milo.opcua.sdk.server.ManagedAddressSpaceFragmentWithLifecycle;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.SimpleAddressSpaceFilter;
import org.eclipse.milo.opcua.sdk.server.UaNodeManager;
import org.eclipse.milo.opcua.sdk.server.aliases.AliasAuthorizationPolicy;
import org.eclipse.milo.opcua.sdk.server.aliases.AliasCategory;
import org.eclipse.milo.opcua.sdk.server.aliases.AliasCategoryConfig;
import org.eclipse.milo.opcua.sdk.server.aliases.AliasLimits;
import org.eclipse.milo.opcua.sdk.server.aliases.AliasManager;
import org.eclipse.milo.opcua.sdk.server.aliases.AliasManagerConfig;
import org.eclipse.milo.opcua.sdk.server.aliases.AliasTarget;
import org.eclipse.milo.opcua.sdk.server.aliases.AliasVersionStore;
import org.eclipse.milo.opcua.sdk.server.items.DataItem;
import org.eclipse.milo.opcua.sdk.server.items.MonitoredItem;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.AliasNameDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.eclipse.milo.opcua.stack.core.types.structured.CallResponse;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

/**
 * Integration tests for {@link AliasManager}'s network mutation path: the {@code
 * AddAliasesToCategory} and {@code DeleteAliasesFromCategory} Methods (Part 17 §6.3.4/§6.3.5)
 * exercised over the wire with real client Calls — the two independent deny layers (Method
 * materialization and authorization policy), the per-entry StatusCode contract, call-level
 * validation, and the per-category configuration flag.
 *
 * <p>An {@link AliasManager} is one-shot and only one at a time may manage the standard alias
 * Objects, so each test group starts its own manager (per test, or per nested class through its own
 * lifecycle methods) and shuts it down when done. Alias Nodes the wire path creates in standard
 * categories are hosted in the manager's AddressSpace fragment and leave the AddressSpace with it,
 * so groups do not observe each other's aliases.
 */
// Fields here and in the nested classes are assigned in @BeforeAll/@BeforeEach, which the
// nullability inspection does not model.
@SuppressWarnings("NotNullFieldNotInitialized")
class AliasConfigMethodsTest extends AbstractClientServerTest {

  /**
   * The operations-per-call limit configured by {@link #grantedConfig}; small so oversized-array
   * behavior is testable with a handful of entries.
   */
  private static final int MAX_OPERATIONS_PER_CALL = 4;

  /** Grants find and mutate to every session; the opposite of the deny-by-default default. */
  private static final AliasAuthorizationPolicy ALLOW_FIND_AND_MUTATE =
      new AliasAuthorizationPolicy() {
        @Override
        public boolean checkFind(@Nullable Session session, NodeId categoryId) {
          return true;
        }

        @Override
        public boolean checkMutate(@Nullable Session session, NodeId categoryId) {
          return true;
        }
      };

  private UaNodeManager testNodeManager;

  @BeforeAll
  void captureTestNodeManager() {
    testNamespace.configure((context, nodeManager) -> testNodeManager = nodeManager);
  }

  @Nested
  class ConfigurationDisabled {

    // WHY: the first deny layer of the two-layer mutation posture — Part 17 §6.3.1 marks the
    // mutation Methods Optional, and the manager only materializes them when configuration is
    // enabled. With the default (disabled) config the Nodes must be entirely absent: a Read fails
    // with Bad_NodeIdUnknown and a Call fails Method resolution with Bad_MethodInvalid instead of
    // reaching any handler.
    @Test
    void mutationMethodNodesAreAbsentWithDefaultConfig() throws UaException {
      AliasManager manager =
          new AliasManager(
              server,
              AliasManagerConfig.builder()
                  .nodeNamespaceIndex(testNamespace.getNamespaceIndex())
                  .build());
      manager.startup();
      try {
        for (NodeId methodNodeId : List.of(aliasesAddMethodId(), aliasesDeleteMethodId())) {
          DataValue value = readAttribute(client, methodNodeId, AttributeId.BrowseName);
          assertEquals(StatusCode.of(StatusCodes.Bad_NodeIdUnknown), value.getStatusCode());
        }

        CallMethodResult result =
            callAddAliases(
                NodeIds.Aliases,
                aliasesAddMethodId(),
                new String[] {"AbsentMethodAlias"},
                new ExpandedNodeId[] {newNodeId("TestInt32").expanded()},
                new String[0],
                NodeId.NULL_VALUE);

        assertEquals(StatusCode.of(StatusCodes.Bad_MethodInvalid), result.getStatusCode());
      } finally {
        manager.shutdown();
      }
    }
  }

  @Nested
  class MutationDeniedByDefaultPolicy {

    // WHY: the second deny layer is independent of materialization — with the Methods
    // materialized (configurationEnabled true) but the DEFAULT authorization policy in place,
    // every session is denied mutation: the Call fails with Bad_UserAccessDenied (the code Part
    // 17 §6.3.4/§6.3.5 name for a caller without rights) and the category is unchanged.
    @Test
    void addAndDeleteCallsAreDeniedWithTheDefaultPolicy() throws UaException {
      AliasManager manager =
          new AliasManager(
              server,
              AliasManagerConfig.builder()
                  .configurationEnabled(true)
                  .nodeNamespaceIndex(testNamespace.getNamespaceIndex())
                  .build());
      manager.startup();
      try {
        CallMethodResult addResult =
            callAddAliases(
                NodeIds.Aliases,
                aliasesAddMethodId(),
                new String[] {"DeniedWireAlias"},
                new ExpandedNodeId[] {newNodeId("TestInt32").expanded()},
                new String[0],
                NodeId.NULL_VALUE);

        assertEquals(StatusCode.of(StatusCodes.Bad_UserAccessDenied), addResult.getStatusCode());
        assertTrue(manager.findAlias(NodeIds.Aliases, "DeniedWireAlias", null).isEmpty());

        CallMethodResult deleteResult =
            callDeleteAliases(
                NodeIds.Aliases, aliasesDeleteMethodId(), new String[] {"DeniedWireAlias"}, null);

        assertEquals(StatusCode.of(StatusCodes.Bad_UserAccessDenied), deleteResult.getStatusCode());
      } finally {
        manager.shutdown();
      }
    }
  }

  @Nested
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  class VersionPersistenceFailure {

    /**
     * An in-memory store whose saves can be switched to fail — all of them, or all after the next N
     * successes — simulating persistence that breaks after a successful startup or partway through
     * a multi-category operation.
     */
    final class TogglableStore implements AliasVersionStore {

      private final Map<ExpandedNodeId, UInteger> entries = new HashMap<>();

      volatile boolean failSaves = false;

      /** When non-negative: this many further saves succeed, then every save fails. */
      volatile int failAfterSaves = -1;

      @Override
      public Map<ExpandedNodeId, UInteger> load() {
        return Map.copyOf(entries);
      }

      @Override
      public void save(ExpandedNodeId categoryId, UInteger value) throws UaException {
        if (failSaves || failAfterSaves == 0) {
          throw new UaException(StatusCodes.Bad_ResourceUnavailable, "save failed");
        }
        if (failAfterSaves > 0) {
          failAfterSaves--;
        }
        entries.put(categoryId, value);
      }
    }

    private TogglableStore store;
    private AliasManager aliasManager;

    @BeforeAll
    void startManager() {
      store = new TogglableStore();
      aliasManager = new AliasManager(server, grantedConfig(store));
      aliasManager.startup();
    }

    @AfterAll
    void shutdownManager() {
      aliasManager.shutdown();
    }

    // WHY: save-before-mutate — §6.3.1's persisted-LastChange contract only holds if no
    // observable version was ever unpersisted, so an entry whose LastChange save fails must fail
    // with Bad_InternalError BEFORE creating anything: were the alias created anyway, a restart
    // could re-produce an already-observed LastChange value for different content and Client
    // caches would go undetectably stale.
    @Test
    void wireEntryWhoseVersionSaveFailsIsRejectedWithoutCreatingTheAlias() throws UaException {
      UInteger lastChangeBefore = requireLastChange(server, NodeIds.Aliases);

      store.failSaves = true;
      try {
        CallMethodResult result =
            callAddAliases(
                NodeIds.Aliases,
                aliasesAddMethodId(),
                new String[] {"UnpersistableWireAlias"},
                new ExpandedNodeId[] {newNodeId("TestInt32").expanded()},
                new String[0],
                NodeId.NULL_VALUE);

        assertEquals(StatusCode.GOOD, result.getStatusCode());
        assertArrayEquals(
            new StatusCode[] {StatusCode.of(StatusCodes.Bad_InternalError)}, errorCodes(result));
      } finally {
        store.failSaves = false;
      }

      assertTrue(aliasManager.findAlias(NodeIds.Aliases, "UnpersistableWireAlias", null).isEmpty());
      assertEquals(lastChangeBefore, requireLastChange(server, NodeIds.Aliases));
    }

    // WHY: the same guarantee on the programmatic path — addAlias must throw Bad_InternalError
    // with nothing applied when the LastChange save fails, and the identical call must succeed
    // once persistence recovers, proving the failed attempt left no residue behind.
    @Test
    void programmaticAddAliasFailsCleanlyWhenVersionSaveFailsAndRecovers() throws UaException {
      UInteger lastChangeBefore = requireLastChange(server, NodeIds.Aliases);
      var target = new AliasTarget(newNodeId("TestInt32").expanded(), null, NodeIds.AliasFor);

      store.failSaves = true;
      try {
        UaException e =
            assertThrows(
                UaException.class,
                () ->
                    aliasManager.addAlias(NodeIds.Aliases, "UnpersistableAlias", List.of(target)));

        assertEquals(StatusCode.of(StatusCodes.Bad_InternalError), e.getStatusCode());
      } finally {
        store.failSaves = false;
      }

      assertTrue(aliasManager.findAlias(NodeIds.Aliases, "UnpersistableAlias", null).isEmpty());
      assertEquals(lastChangeBefore, requireLastChange(server, NodeIds.Aliases));

      aliasManager.addAlias(NodeIds.Aliases, "UnpersistableAlias", List.of(target));
      assertEquals(1, aliasManager.findAlias(NodeIds.Aliases, "UnpersistableAlias", null).size());
    }

    // WHY: the prepare/publish pairing invariant — a removeCategory whose ancestor-version save
    // fails partway must abort with nothing removed, still publish the values it persisted
    // before the failure (a bump without a content change is the safe direction), and leave
    // nothing stranded in the pending set: the next mutation must compute a fresh version, not
    // republish a stale prepared one.
    @Test
    void removeCategoryAncestorSaveFailurePartwayAbortsAndDrainsPending() throws UaException {
      AliasCategory parent =
          aliasManager.addCategory(categoryConfig("PendingDrainParent", NodeIds.Aliases));
      AliasCategory child =
          aliasManager.addCategory(categoryConfig("PendingDrainChild", parent.nodeId()));

      UInteger parentBefore = requireLastChange(server, parent.nodeId());

      // Removing the child prepares its ancestors in discovery order [parent, root Aliases]:
      // let the parent's save through and fail the root's.
      store.failAfterSaves = 1;
      UaException e;
      try {
        e = assertThrows(UaException.class, () -> aliasManager.removeCategory(child.nodeId()));
      } finally {
        store.failAfterSaves = -1;
      }
      assertEquals(StatusCode.of(StatusCodes.Bad_InternalError), e.getStatusCode());

      // Nothing was removed: the category Node still exists and is still managed (the retry
      // during cleanup below succeeds).
      assertTrue(server.getAddressSpaceManager().getManagedNode(child.nodeId()).isPresent());

      // The parent's persisted-before-the-failure value was still published...
      UInteger parentAfterFailure = requireLastChange(server, parent.nodeId());
      assertStrictlyGreater(parentBefore, parentAfterFailure);

      // ...and drained from the pending set: the next mutation computes a fresh version.
      aliasManager.addAlias(
          parent.nodeId(),
          "PendingDrainAlias",
          List.of(new AliasTarget(newNodeId("TestInt32").expanded(), null, NodeIds.AliasFor)));
      assertStrictlyGreater(parentAfterFailure, readLastChange(server, parent.nodeId()));

      aliasManager.deleteAlias(parent.nodeId(), "PendingDrainAlias", null);
      aliasManager.removeCategory(child.nodeId());
      aliasManager.removeCategory(parent.nodeId());
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
  }

  @Nested
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  class AddAliasesToCategoryCalls {

    private RecordingVersionStore versionStore;
    private AliasManager aliasManager;

    @BeforeAll
    void startManager() {
      versionStore = new RecordingVersionStore(server);
      aliasManager = new AliasManager(server, grantedConfig(versionStore));
      aliasManager.startup();
    }

    @AfterAll
    void shutdownManager() {
      aliasManager.shutdown();
    }

    // WHY: Part 17 §6.3.4 — AddAliasesToCategory reports one StatusCode per entry, parallel to
    // the inputs, and the created aliases must be visible to FindAlias on the same category.
    // §6.3.1 requires the change to surface through LastChange; the manager's contract is a
    // single version bump per call — persisted exactly once — no matter how many entries changed
    // the category.
    @Test
    void addCallCreatesAliasesAndBumpsLastChangeOncePerCall() throws UaException {
      UInteger lastChangeBefore = requireLastChange(server, NodeIds.Aliases);
      long savesBefore = versionStore.savesFor(NodeIds.Aliases);

      CallMethodResult result =
          callAddAliases(
              NodeIds.Aliases,
              aliasesAddMethodId(),
              new String[] {"WireAddedA", "WireAddedB"},
              new ExpandedNodeId[] {
                newNodeId("TestInt32").expanded(), newNodeId("TestAnalogValue").expanded()
              },
              new String[0],
              NodeId.NULL_VALUE);

      assertEquals(StatusCode.GOOD, result.getStatusCode());
      assertArrayEquals(new StatusCode[] {StatusCode.GOOD, StatusCode.GOOD}, errorCodes(result));

      List<AliasNameDataType> foundA = findRootAliases("WireAddedA");
      assertEquals(1, foundA.size());
      assertArrayEquals(
          new ExpandedNodeId[] {newNodeId("TestInt32").expanded()},
          foundA.get(0).getReferencedNodes());

      List<AliasNameDataType> foundB = findRootAliases("WireAddedB");
      assertEquals(1, foundB.size());
      assertArrayEquals(
          new ExpandedNodeId[] {newNodeId("TestAnalogValue").expanded()},
          foundB.get(0).getReferencedNodes());

      assertStrictlyGreater(lastChangeBefore, readLastChange(server, NodeIds.Aliases));
      assertEquals(savesBefore + 1, versionStore.savesFor(NodeIds.Aliases));
    }

    // WHY: Part 17 §6.3.4 — results are per entry and a failed entry must not affect any other
    // entry: an unknown local TargetNode fails its own entry with Bad_NodeIdUnknown while the
    // sibling entry in the SAME call still creates its alias.
    @Test
    void failedEntryDoesNotAffectSiblingEntryInSameCall() throws UaException {
      CallMethodResult result =
          callAddAliases(
              NodeIds.Aliases,
              aliasesAddMethodId(),
              new String[] {"MissingTargetAlias", "IsolatedGoodAlias"},
              new ExpandedNodeId[] {
                newNodeId("NoSuchTargetNode").expanded(), newNodeId("TestInt32").expanded()
              },
              new String[0],
              NodeId.NULL_VALUE);

      assertEquals(StatusCode.GOOD, result.getStatusCode());
      assertArrayEquals(
          new StatusCode[] {StatusCode.of(StatusCodes.Bad_NodeIdUnknown), StatusCode.GOOD},
          errorCodes(result));

      assertTrue(aliasManager.findAlias(NodeIds.Aliases, "MissingTargetAlias", null).isEmpty());
      assertEquals(1, aliasManager.findAlias(NodeIds.Aliases, "IsolatedGoodAlias", null).size());
    }

    // WHY: Part 17 §6.3.4 — an entry duplicating an existing alias/target association "shall be
    // ignored and no error shall be generated": the repeat call reports Good, adds no Reference,
    // and neither bumps nor persists LastChange because nothing changed.
    @Test
    void duplicateOfExistingAssociationIsIgnoredWithGood() throws UaException {
      addRootAliasOverWire("DupWireAlias", newNodeId("TestInt32"));

      UInteger lastChangeBefore = requireLastChange(server, NodeIds.Aliases);
      long savesBefore = versionStore.savesFor(NodeIds.Aliases);

      CallMethodResult result =
          callAddAliases(
              NodeIds.Aliases,
              aliasesAddMethodId(),
              new String[] {"DupWireAlias"},
              new ExpandedNodeId[] {newNodeId("TestInt32").expanded()},
              new String[0],
              NodeId.NULL_VALUE);

      assertEquals(StatusCode.GOOD, result.getStatusCode());
      assertArrayEquals(new StatusCode[] {StatusCode.GOOD}, errorCodes(result));

      List<AliasNameDataType> found = findRootAliases("DupWireAlias");
      assertEquals(1, found.size());
      assertArrayEquals(
          new ExpandedNodeId[] {newNodeId("TestInt32").expanded()},
          found.get(0).getReferencedNodes());

      assertEquals(lastChangeBefore, readLastChange(server, NodeIds.Aliases));
      assertEquals(savesBefore, versionStore.savesFor(NodeIds.Aliases));
    }

    // WHY: Part 17 §6.3.4 — idempotency extends to duplicates within one request: an entry that
    // repeats an earlier entry of the same call must be ignored with Good, leaving exactly one
    // AliasFor association behind.
    @Test
    void repeatedEntryWithinOneCallIsIgnoredWithGood() throws UaException {
      CallMethodResult result =
          callAddAliases(
              NodeIds.Aliases,
              aliasesAddMethodId(),
              new String[] {"IntraDupAlias", "IntraDupAlias"},
              new ExpandedNodeId[] {
                newNodeId("TestInt32").expanded(), newNodeId("TestInt32").expanded()
              },
              new String[0],
              NodeId.NULL_VALUE);

      assertEquals(StatusCode.GOOD, result.getStatusCode());
      assertArrayEquals(new StatusCode[] {StatusCode.GOOD, StatusCode.GOOD}, errorCodes(result));

      List<AliasNameDataType> found = findRootAliases("IntraDupAlias");
      assertEquals(1, found.size());
      assertArrayEquals(
          new ExpandedNodeId[] {newNodeId("TestInt32").expanded()},
          found.get(0).getReferencedNodes());
    }

    // WHY: Part 17 §6.3.4 defines Bad_NotSupported for a server that does not support aliases
    // with targets on remote Servers, which this manager does not; a non-empty TargetServers
    // entry marks its target remote, so the entry fails and nothing is created.
    // (Uncertain_ReferenceOutOfServer is reserved for servers that ACCEPT a remote target they
    // cannot verify.)
    @Test
    void remoteTargetEntryIsRejectedWithBadNotSupported() throws UaException {
      CallMethodResult result =
          callAddAliases(
              NodeIds.Aliases,
              aliasesAddMethodId(),
              new String[] {"RemoteTargetWireAlias"},
              new ExpandedNodeId[] {newNodeId("TestInt32").expanded()},
              new String[] {"urn:test:remote-server"},
              NodeId.NULL_VALUE);

      assertEquals(StatusCode.GOOD, result.getStatusCode());
      assertArrayEquals(
          new StatusCode[] {StatusCode.of(StatusCodes.Bad_NotSupported)}, errorCodes(result));

      assertTrue(aliasManager.findAlias(NodeIds.Aliases, "RemoteTargetWireAlias", null).isEmpty());
    }

    // WHY: Part 17 §6.3.4 — "The ServerIndex in the ExpandedNodeId shall be ignored and the
    // TargetServers Uri shall be used": with a null/empty TargetServers entry the target is
    // local, so a wire ExpandedNodeId carrying serverIndex=1 must still resolve to the local
    // Node instead of the ServerIndex being treated as a remote-target signal.
    @Test
    void serverIndexInTargetNodeIsIgnoredWhenTargetServersEntryIsEmpty() throws UaException {
      NodeId localTarget = newNodeId("TestInt32");
      var targetWithServerIndex =
          new ExpandedNodeId(
              ExpandedNodeId.ServerReference.of(1),
              ExpandedNodeId.NamespaceReference.of(localTarget.getNamespaceIndex()),
              localTarget.getIdentifier());

      CallMethodResult result =
          callAddAliases(
              NodeIds.Aliases,
              aliasesAddMethodId(),
              new String[] {"ServerIndexIgnoredAlias"},
              new ExpandedNodeId[] {targetWithServerIndex},
              new String[0],
              NodeId.NULL_VALUE);

      assertEquals(StatusCode.GOOD, result.getStatusCode());
      assertArrayEquals(new StatusCode[] {StatusCode.GOOD}, errorCodes(result));

      List<AliasNameDataType> found = findRootAliases("ServerIndexIgnoredAlias");
      assertEquals(1, found.size());
      assertArrayEquals(
          new ExpandedNodeId[] {localTarget.expanded()}, found.get(0).getReferencedNodes());
    }

    // WHY: Part 17 §9.3 — aliases organized under TagVariables must target Variables; a Method
    // Node target violates the NodeClass constraint, and the violation is an entry-level failure
    // (Bad_InvalidArgument), not a call-level one.
    @Test
    void tagVariablesEntryTargetingNonVariableFailsWithBadInvalidArgument() throws UaException {
      CallMethodResult result =
          callAddAliases(
              NodeIds.TagVariables,
              tagVariablesAddMethodId(),
              new String[] {"MethodTagWireAlias"},
              new ExpandedNodeId[] {newNodeId("sqrt(x)").expanded()},
              new String[0],
              NodeId.NULL_VALUE);

      assertEquals(StatusCode.GOOD, result.getStatusCode());
      assertArrayEquals(
          new StatusCode[] {StatusCode.of(StatusCodes.Bad_InvalidArgument)}, errorCodes(result));

      assertTrue(
          aliasManager.findAlias(NodeIds.TagVariables, "MethodTagWireAlias", null).isEmpty());
    }
  }

  @Nested
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  class AddCallLevelValidation {

    private AliasManager aliasManager;

    @BeforeAll
    void startManager() {
      aliasManager = new AliasManager(server, grantedConfig(new RecordingVersionStore(server)));
      aliasManager.startup();
    }

    @AfterAll
    void shutdownManager() {
      aliasManager.shutdown();
    }

    // WHY: Part 17 §6.3.4 requires AliasNames and TargetNodes to be parallel arrays; a length
    // mismatch invalidates the whole call with Bad_InvalidArgument, and no entry may be applied.
    @Test
    void aliasNamesAndTargetNodesOfDifferentLengthFailTheCall() throws UaException {
      CallMethodResult result =
          callAddAliases(
              NodeIds.Aliases,
              aliasesAddMethodId(),
              new String[] {"MismatchAliasA", "MismatchAliasB"},
              new ExpandedNodeId[] {newNodeId("TestInt32").expanded()},
              new String[0],
              NodeId.NULL_VALUE);

      assertEquals(StatusCode.of(StatusCodes.Bad_InvalidArgument), result.getStatusCode());
      assertTrue(aliasManager.findAlias(NodeIds.Aliases, "MismatchAliasA", null).isEmpty());
    }

    // WHY: Part 17 §6.3.4 Table 11 defines Bad_InvalidArgument for a call where "the size of
    // the arrays for all arguments except TargetServers is not the same or if all arrays are
    // empty" — a zero-entry Add call is a call-level failure, not a vacuous success.
    @Test
    void addWithAllArraysEmptyFailsTheCallWithBadInvalidArgument() throws UaException {
      CallMethodResult result =
          callAddAliases(
              NodeIds.Aliases,
              aliasesAddMethodId(),
              new String[0],
              new ExpandedNodeId[0],
              new String[0],
              NodeId.NULL_VALUE);

      assertEquals(StatusCode.of(StatusCodes.Bad_InvalidArgument), result.getStatusCode());
    }

    // WHY: Part 17 §6.3.4 — a null or empty TargetServers array means all targets are local, but
    // a NON-empty one must be parallel to the other arrays; any other length invalidates the
    // whole call with Bad_InvalidArgument.
    @Test
    void nonEmptyTargetServersOfDifferentLengthFailTheCall() throws UaException {
      CallMethodResult result =
          callAddAliases(
              NodeIds.Aliases,
              aliasesAddMethodId(),
              new String[] {"ServerMismatchAliasA", "ServerMismatchAliasB"},
              new ExpandedNodeId[] {
                newNodeId("TestInt32").expanded(), newNodeId("TestAnalogValue").expanded()
              },
              new String[] {"urn:test:remote-server"},
              NodeId.NULL_VALUE);

      assertEquals(StatusCode.of(StatusCodes.Bad_InvalidArgument), result.getStatusCode());
      assertTrue(aliasManager.findAlias(NodeIds.Aliases, "ServerMismatchAliasA", null).isEmpty());
    }

    // WHY: Part 17 §6.3.4 defaults a null TargetReferenceType to AliasFor, and §6.3.1 requires
    // every alias to reference its targets with AliasFor or a subtype; the parameter applies to
    // the whole call, so a ReferenceType outside the hierarchy (Organizes) fails the call with
    // Bad_InvalidArgument rather than any individual entry.
    @Test
    void targetReferenceTypeOutsideAliasForHierarchyFailsTheCall() throws UaException {
      CallMethodResult result =
          callAddAliases(
              NodeIds.Aliases,
              aliasesAddMethodId(),
              new String[] {"BadRefTypeAlias"},
              new ExpandedNodeId[] {newNodeId("TestInt32").expanded()},
              new String[0],
              NodeIds.Organizes);

      assertEquals(StatusCode.of(StatusCodes.Bad_InvalidArgument), result.getStatusCode());
      assertTrue(aliasManager.findAlias(NodeIds.Aliases, "BadRefTypeAlias", null).isEmpty());
    }

    // WHY: Part 17 §6.3.4 names no code for oversized arrays, but Part 4 §7.38.2 defines
    // Bad_TooManyOperations as "the request specified too many operations" — exactly the
    // condition when the entry count exceeds the configured maxOperationsPerCall — so the whole
    // call fails with it before any entry is processed.
    @Test
    void addWithMoreEntriesThanMaxOperationsPerCallFailsTheCall() throws UaException {
      int entryCount = MAX_OPERATIONS_PER_CALL + 1;
      var aliasNames = new String[entryCount];
      var targetNodes = new ExpandedNodeId[entryCount];
      for (int i = 0; i < entryCount; i++) {
        aliasNames[i] = "OversizedAddAlias" + i;
        targetNodes[i] = newNodeId("TestInt32").expanded();
      }

      CallMethodResult result =
          callAddAliases(
              NodeIds.Aliases,
              aliasesAddMethodId(),
              aliasNames,
              targetNodes,
              new String[0],
              NodeId.NULL_VALUE);

      assertEquals(StatusCode.of(StatusCodes.Bad_TooManyOperations), result.getStatusCode());
      assertTrue(aliasManager.findAlias(NodeIds.Aliases, "OversizedAddAlias0", null).isEmpty());
    }

    // WHY: the same Part 4 §7.38.2 operations limit applies to DeleteAliasesFromCategory; an
    // oversized AliasNames array fails the whole call with Bad_TooManyOperations before any
    // entry is evaluated.
    @Test
    void deleteWithMoreEntriesThanMaxOperationsPerCallFailsTheCall() throws UaException {
      int entryCount = MAX_OPERATIONS_PER_CALL + 1;
      var aliasNames = new String[entryCount];
      for (int i = 0; i < entryCount; i++) {
        aliasNames[i] = "OversizedDeleteAlias" + i;
      }

      CallMethodResult result =
          callDeleteAliases(NodeIds.Aliases, aliasesDeleteMethodId(), aliasNames, null);

      assertEquals(StatusCode.of(StatusCodes.Bad_TooManyOperations), result.getStatusCode());
    }
  }

  @Nested
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  class DeleteAliasesFromCategoryCalls {

    private RecordingVersionStore versionStore;
    private AliasManager aliasManager;

    @BeforeAll
    void startManager() {
      versionStore = new RecordingVersionStore(server);
      aliasManager = new AliasManager(server, grantedConfig(versionStore));
      aliasManager.startup();
    }

    @AfterAll
    void shutdownManager() {
      aliasManager.shutdown();
    }

    // WHY: Part 17 §6.3.5 — TargetNodes entries "further restrict what is deleted": deleting one
    // explicit target removes only that association, and the alias survives with its remaining
    // target still visible to FindAlias.
    @Test
    void explicitTargetEntryRemovesOnlyThatAssociation() throws UaException {
      addRootAliasOverWire(
          "PartialWireAlias", newNodeId("TestInt32"), newNodeId("TestAnalogValue"));

      CallMethodResult result =
          callDeleteAliases(
              NodeIds.Aliases,
              aliasesDeleteMethodId(),
              new String[] {"PartialWireAlias"},
              new ExpandedNodeId[] {newNodeId("TestAnalogValue").expanded()});

      assertEquals(StatusCode.GOOD, result.getStatusCode());
      assertArrayEquals(new StatusCode[] {StatusCode.GOOD}, errorCodes(result));

      List<AliasNameDataType> found = findRootAliases("PartialWireAlias");
      assertEquals(1, found.size());
      assertArrayEquals(
          new ExpandedNodeId[] {newNodeId("TestInt32").expanded()},
          found.get(0).getReferencedNodes());
    }

    // WHY: Part 17 §6.3.1 — every alias shall have at least one AliasFor Reference, so removing
    // the last target over the wire must delete the alias Object from EVERY organizing category,
    // not just the one the Method was called on, and §6.3.1's LastChange contract requires every
    // one of those categories to be bumped.
    @Test
    void removingLastTargetDeletesAliasFromEveryOrganizingCategory() throws UaException {
      addRootAliasOverWire("SharedWireAlias", newNodeId("TestInt32"));

      AliasCategory catB =
          aliasManager.addCategory(
              new AliasCategoryConfig(
                  newNodeId("DeleteWireCatB"),
                  NodeIds.Aliases,
                  newQualifiedName("DeleteWireCatB"),
                  testNodeManager,
                  name -> newNodeId("DeleteWireCatB/" + name),
                  true,
                  false,
                  false));
      try {
        // Organize the alias by a second category, the multi-parent arrangement §6.3.1 allows.
        NodeId aliasNodeId =
            requireNonNull(
                findOrganizedAliasNodeId(NodeIds.Aliases, "SharedWireAlias"),
                "alias Node not found under Aliases");
        UaNode aliasNode =
            server.getAddressSpaceManager().getManagedNode(aliasNodeId).orElseThrow();
        aliasNode.addReference(
            new Reference(
                aliasNodeId,
                NodeIds.Organizes,
                catB.nodeId().expanded(),
                Reference.Direction.INVERSE));

        UInteger rootBefore = requireLastChange(server, NodeIds.Aliases);
        UInteger catBBefore = requireLastChange(server, catB.nodeId());

        CallMethodResult result =
            callDeleteAliases(
                NodeIds.Aliases,
                aliasesDeleteMethodId(),
                new String[] {"SharedWireAlias"},
                new ExpandedNodeId[] {newNodeId("TestInt32").expanded()});

        assertEquals(StatusCode.GOOD, result.getStatusCode());
        assertArrayEquals(new StatusCode[] {StatusCode.GOOD}, errorCodes(result));

        assertTrue(server.getAddressSpaceManager().getManagedNode(aliasNodeId).isEmpty());
        assertTrue(aliasManager.findAlias(catB.nodeId(), "SharedWireAlias", null).isEmpty());

        assertStrictlyGreater(catBBefore, readLastChange(server, catB.nodeId()));
        assertStrictlyGreater(rootBefore, readLastChange(server, NodeIds.Aliases));
      } finally {
        aliasManager.removeCategory(catB.nodeId());
      }
    }

    // WHY: Part 17 §6.3.5 defines Bad_NotFound as "The AliasName was not located": a name the
    // category does not contain fails its own entry, and since nothing changed, LastChange is
    // neither bumped nor persisted.
    @Test
    void unknownAliasNameEntryFailsWithBadNotFound() throws UaException {
      long savesBefore = versionStore.savesFor(NodeIds.Aliases);

      CallMethodResult result =
          callDeleteAliases(
              NodeIds.Aliases, aliasesDeleteMethodId(), new String[] {"NoSuchWireAlias"}, null);

      assertEquals(StatusCode.GOOD, result.getStatusCode());
      assertArrayEquals(
          new StatusCode[] {StatusCode.of(StatusCodes.Bad_NotFound)}, errorCodes(result));
      assertEquals(savesBefore, versionStore.savesFor(NodeIds.Aliases));
    }

    // WHY: Part 17 §6.3.5 — each entry succeeds or fails on its own, so a Bad_NotFound entry
    // must not prevent the sibling entry in the SAME call from deleting its alias. A null
    // TargetNodes ARRAY is accepted as "no restriction for any entry" — §6.3.5 only defines the
    // meaning of null entries, and the reading mirrors §6.3.4's null-or-empty TargetServers.
    @Test
    void failedEntryDoesNotAffectSiblingEntryInSameDeleteCall() throws UaException {
      addRootAliasOverWire("DeleteSurvivorAlias", newNodeId("TestInt32"));

      CallMethodResult result =
          callDeleteAliases(
              NodeIds.Aliases,
              aliasesDeleteMethodId(),
              new String[] {"NoSuchWireAlias", "DeleteSurvivorAlias"},
              null);

      assertEquals(StatusCode.GOOD, result.getStatusCode());
      assertArrayEquals(
          new StatusCode[] {StatusCode.of(StatusCodes.Bad_NotFound), StatusCode.GOOD},
          errorCodes(result));

      assertTrue(aliasManager.findAlias(NodeIds.Aliases, "DeleteSurvivorAlias", null).isEmpty());
    }
  }

  @Nested
  @TestInstance(TestInstance.Lifecycle.PER_CLASS)
  class ApplicationCategories {

    private AliasManager aliasManager;

    @BeforeAll
    void startManager() {
      aliasManager = new AliasManager(server, grantedConfig(new RecordingVersionStore(server)));
      aliasManager.startup();
    }

    @AfterAll
    void shutdownManager() {
      aliasManager.shutdown();
    }

    // WHY: AliasCategoryConfig.configurationEnabled materializes and binds the category's own
    // AddAliasesToCategory/DeleteAliasesFromCategory instances (Optional members per Part 17
    // §6.3.1), so a client granted mutation can manage the category's aliases entirely over the
    // wire, with the §6.3.4/§6.3.5 per-entry contract.
    @Test
    void categoryWithConfigurationEnabledGetsCallableMutationMethods() throws UaException {
      AliasCategory category =
          aliasManager.addCategory(
              new AliasCategoryConfig(
                  newNodeId("ConfiguredWireCategory"),
                  NodeIds.Aliases,
                  newQualifiedName("ConfiguredWireCategory"),
                  testNodeManager,
                  name -> newNodeId("ConfiguredWireCategory/" + name),
                  false,
                  false,
                  true));
      try {
        assertTrue(category.configurationEnabled());

        NodeId addMethodId =
            requireNonNull(
                findComponentMethodId(category.nodeId(), "AddAliasesToCategory"),
                "AddAliasesToCategory Method not found");
        NodeId deleteMethodId =
            requireNonNull(
                findComponentMethodId(category.nodeId(), "DeleteAliasesFromCategory"),
                "DeleteAliasesFromCategory Method not found");

        CallMethodResult addResult =
            callAddAliases(
                category.nodeId(),
                addMethodId,
                new String[] {"CategoryWireAlias"},
                new ExpandedNodeId[] {newNodeId("TestInt32").expanded()},
                new String[0],
                NodeId.NULL_VALUE);

        assertEquals(StatusCode.GOOD, addResult.getStatusCode());
        assertArrayEquals(new StatusCode[] {StatusCode.GOOD}, errorCodes(addResult));
        assertEquals(
            1, aliasManager.findAlias(category.nodeId(), "CategoryWireAlias", null).size());

        CallMethodResult deleteResult =
            callDeleteAliases(
                category.nodeId(), deleteMethodId, new String[] {"CategoryWireAlias"}, null);

        assertEquals(StatusCode.GOOD, deleteResult.getStatusCode());
        assertArrayEquals(new StatusCode[] {StatusCode.GOOD}, errorCodes(deleteResult));
        assertTrue(aliasManager.findAlias(category.nodeId(), "CategoryWireAlias", null).isEmpty());
      } finally {
        aliasManager.removeCategory(category.nodeId());
      }
    }

    // WHY: entries execute application-supplied code (the category's aliasNodeIdFactory and its
    // NodeManager), which can throw unchecked; Part 17 §6.3.4 requires per-entry independence
    // and §6.3.1 requires LastChange to reflect what changed, so a RuntimeException from one
    // entry must map to that entry's Bad_InternalError while the sibling entry still creates
    // its alias and the category's LastChange bumps for the applied entry.
    @Test
    void runtimeExceptionFromAliasNodeIdFactoryFailsOnlyItsEntry() throws UaException {
      AliasCategory category =
          aliasManager.addCategory(
              new AliasCategoryConfig(
                  newNodeId("ThrowingFactoryCategory"),
                  NodeIds.Aliases,
                  newQualifiedName("ThrowingFactoryCategory"),
                  testNodeManager,
                  name -> {
                    if ("ThrowingFactoryAlias".equals(name)) {
                      throw new IllegalStateException("factory failure for " + name);
                    }
                    return newNodeId("ThrowingFactoryCategory/" + name);
                  },
                  true,
                  false,
                  true));
      try {
        NodeId addMethodId =
            requireNonNull(
                findComponentMethodId(category.nodeId(), "AddAliasesToCategory"),
                "AddAliasesToCategory Method not found");

        UInteger lastChangeBefore = requireLastChange(server, category.nodeId());

        CallMethodResult result =
            callAddAliases(
                category.nodeId(),
                addMethodId,
                new String[] {"FactorySurvivorAlias", "ThrowingFactoryAlias"},
                new ExpandedNodeId[] {
                  newNodeId("TestInt32").expanded(), newNodeId("TestAnalogValue").expanded()
                },
                new String[0],
                NodeId.NULL_VALUE);

        assertEquals(StatusCode.GOOD, result.getStatusCode());
        assertArrayEquals(
            new StatusCode[] {StatusCode.GOOD, StatusCode.of(StatusCodes.Bad_InternalError)},
            errorCodes(result));

        assertEquals(
            1, aliasManager.findAlias(category.nodeId(), "FactorySurvivorAlias", null).size());
        assertTrue(
            aliasManager.findAlias(category.nodeId(), "ThrowingFactoryAlias", null).isEmpty());

        assertStrictlyGreater(lastChangeBefore, readLastChange(server, category.nodeId()));
      } finally {
        aliasManager.removeCategory(category.nodeId());
      }
    }

    // WHY: a failure after the alias Node was added to the NodeManager but before it was fully
    // wired must not leave the Node behind — untyped, unlinked, or targetless it violates the
    // Part 17 §6.3.1 alias model and would be visible to searches forever. The entry reports
    // Bad_InternalError (§6.3.4 per-entry contract for unchecked application failures) and the
    // best-effort cleanup deletes the Node again.
    @Test
    void nodeManagerFailureAfterNodeAddFailsEntryAndRemovesPartiallyCreatedAlias()
        throws UaException {
      // The category's Nodes must live in a routable AddressSpace for the wire Call to reach
      // them, so the throwing NodeManager is hosted by a registered fragment.
      var fragment = new AliasForRejectingFragment(server);
      fragment.startup();
      try {
        AliasCategory category =
            aliasManager.addCategory(
                new AliasCategoryConfig(
                    newNodeId("RejectingCategory"),
                    NodeIds.Aliases,
                    newQualifiedName("RejectingCategory"),
                    fragment.getNodeManager(),
                    name -> newNodeId("RejectingCategory/" + name),
                    false,
                    false,
                    true));
        try {
          NodeId addMethodId =
              requireNonNull(
                  findComponentMethodId(category.nodeId(), "AddAliasesToCategory"),
                  "AddAliasesToCategory Method not found");

          CallMethodResult result =
              callAddAliases(
                  category.nodeId(),
                  addMethodId,
                  new String[] {"HalfWiredAlias"},
                  new ExpandedNodeId[] {newNodeId("TestInt32").expanded()},
                  new String[0],
                  NodeId.NULL_VALUE);

          assertEquals(StatusCode.GOOD, result.getStatusCode());
          assertArrayEquals(
              new StatusCode[] {StatusCode.of(StatusCodes.Bad_InternalError)}, errorCodes(result));

          // Cleanup deleted the partially created Node: it is absent from the AddressSpace and
          // the category organizes nothing by that name.
          NodeId aliasNodeId = newNodeId("RejectingCategory/HalfWiredAlias");
          assertTrue(server.getAddressSpaceManager().getManagedNode(aliasNodeId).isEmpty());
          assertNull(findOrganizedAliasNodeId(category.nodeId(), "HalfWiredAlias"));
        } finally {
          aliasManager.removeCategory(category.nodeId());
        }
      } finally {
        fragment.shutdown();
      }
    }

    // WHY: the mutation surface is per category — with configurationEnabled false the category
    // gets its mandatory FindAlias but no AddAliasesToCategory or DeleteAliasesFromCategory
    // instance at all (Optional per Part 17 §6.3.1): no Method Node, no callable surface.
    @Test
    void categoryWithConfigurationDisabledHasNoMutationMethods() throws UaException {
      AliasCategory category =
          aliasManager.addCategory(
              new AliasCategoryConfig(
                  newNodeId("UnconfiguredWireCategory"),
                  NodeIds.Aliases,
                  newQualifiedName("UnconfiguredWireCategory"),
                  testNodeManager,
                  name -> newNodeId("UnconfiguredWireCategory/" + name),
                  false,
                  false,
                  false));
      try {
        assertFalse(category.configurationEnabled());

        assertNotNull(findComponentMethodId(category.nodeId(), "FindAlias"));
        assertNull(findComponentMethodId(category.nodeId(), "AddAliasesToCategory"));
        assertNull(findComponentMethodId(category.nodeId(), "DeleteAliasesFromCategory"));
      } finally {
        aliasManager.removeCategory(category.nodeId());
      }
    }
  }

  /**
   * A manager config with the mutation Methods materialized on the standard categories, a policy
   * granting mutation to every session, and a small operations-per-call limit so oversized-array
   * behavior is testable.
   */
  private AliasManagerConfig grantedConfig(AliasVersionStore versionStore) {
    return AliasManagerConfig.builder()
        .configurationEnabled(true)
        .authorizationPolicy(ALLOW_FIND_AND_MUTATE)
        .versionStore(versionStore)
        .limits(new AliasLimits(1000, 512, MAX_OPERATIONS_PER_CALL))
        .nodeNamespaceIndex(testNamespace.getNamespaceIndex())
        .build();
  }

  /** The NodeId the manager allocates for the {@code AddAliasesToCategory} Node on Aliases. */
  private NodeId aliasesAddMethodId() {
    return newNodeId("Aliases/AddAliasesToCategory");
  }

  /** The NodeId the manager allocates for the {@code DeleteAliasesFromCategory} Node on Aliases. */
  private NodeId aliasesDeleteMethodId() {
    return newNodeId("Aliases/DeleteAliasesFromCategory");
  }

  /** The NodeId the manager allocates for the {@code AddAliasesToCategory} Node on TagVariables. */
  private NodeId tagVariablesAddMethodId() {
    return newNodeId("TagVariables/AddAliasesToCategory");
  }

  /**
   * Add {@code aliasName} to the root {@code Aliases} category over the wire, one Add entry per
   * target, asserting every entry reports {@code Good}.
   */
  private void addRootAliasOverWire(String aliasName, NodeId... targets) throws UaException {
    var aliasNames = new String[targets.length];
    var targetNodes = new ExpandedNodeId[targets.length];
    for (int i = 0; i < targets.length; i++) {
      aliasNames[i] = aliasName;
      targetNodes[i] = targets[i].expanded();
    }

    CallMethodResult result =
        callAddAliases(
            NodeIds.Aliases,
            aliasesAddMethodId(),
            aliasNames,
            targetNodes,
            new String[0],
            NodeId.NULL_VALUE);

    assertEquals(StatusCode.GOOD, result.getStatusCode());
    for (StatusCode statusCode : errorCodes(result)) {
      assertEquals(StatusCode.GOOD, statusCode);
    }
  }

  /** Call {@code AddAliasesToCategory} through the client. */
  private CallMethodResult callAddAliases(
      NodeId objectId,
      NodeId methodId,
      String[] aliasNames,
      ExpandedNodeId[] targetNodes,
      String[] targetServers,
      NodeId targetReferenceType)
      throws UaException {

    return callMethod(
        objectId,
        methodId,
        new Variant[] {
          new Variant(aliasNames),
          new Variant(targetNodes),
          new Variant(targetServers),
          new Variant(targetReferenceType)
        });
  }

  /** Call {@code DeleteAliasesFromCategory} through the client. */
  private CallMethodResult callDeleteAliases(
      NodeId objectId,
      NodeId methodId,
      String[] aliasNames,
      ExpandedNodeId @Nullable [] targetNodes)
      throws UaException {

    return callMethod(
        objectId, methodId, new Variant[] {new Variant(aliasNames), new Variant(targetNodes)});
  }

  private CallMethodResult callMethod(NodeId objectId, NodeId methodId, Variant[] inputs)
      throws UaException {

    CallResponse response = client.call(List.of(new CallMethodRequest(objectId, methodId, inputs)));

    return requireNonNull(response.getResults())[0];
  }

  /** The per-entry {@code ErrorCodes} output of a mutation call that reached its handler. */
  private StatusCode[] errorCodes(CallMethodResult result) {
    Variant[] outputs = requireNonNull(result.getOutputArguments());

    return (StatusCode[]) requireNonNull(outputs[0].value());
  }

  /**
   * Call {@code FindAlias} on the root {@code Aliases} Object through the client and decode the
   * result entries.
   */
  private List<AliasNameDataType> findRootAliases(String pattern) throws UaException {
    CallMethodResult result =
        callMethod(
            NodeIds.Aliases,
            NodeIds.Aliases_FindAlias,
            new Variant[] {new Variant(pattern), new Variant(NodeId.NULL_VALUE)});

    assertEquals(StatusCode.GOOD, result.getStatusCode());

    Variant[] outputs = requireNonNull(result.getOutputArguments());
    ExtensionObject[] xos = (ExtensionObject[]) requireNonNull(outputs[0].value());

    var entries = new ArrayList<AliasNameDataType>();
    for (ExtensionObject xo : xos) {
      entries.add((AliasNameDataType) xo.decode(client.getStaticEncodingContext()));
    }
    return entries;
  }

  /**
   * The NodeId of the Method component of {@code objectId} with ns=0 BrowseName {@code methodName},
   * or null if the Object has no such component.
   */
  private @Nullable NodeId findComponentMethodId(NodeId objectId, String methodName) {
    List<Reference> references =
        server
            .getAddressSpaceManager()
            .getManagedReferences(objectId, Reference.HAS_COMPONENT_PREDICATE);

    for (Reference reference : references) {
      UaNode node =
          reference
              .getTargetNodeId()
              .toNodeId(server.getNamespaceTable())
              .flatMap(id -> server.getAddressSpaceManager().getManagedNode(id))
              .orElse(null);

      if (node instanceof UaMethodNode
          && new QualifiedName(0, methodName).equals(node.getBrowseName())) {
        return node.getNodeId();
      }
    }
    return null;
  }

  /**
   * The NodeId of the alias Object named {@code aliasName} directly organized by {@code categoryId}
   * (BrowseName text match, namespace ignored), or null if there is none.
   */
  private @Nullable NodeId findOrganizedAliasNodeId(NodeId categoryId, String aliasName) {
    List<Reference> references =
        server
            .getAddressSpaceManager()
            .getManagedReferences(categoryId, Reference.ORGANIZES_PREDICATE);

    for (Reference reference : references) {
      UaNode node =
          reference
              .getTargetNodeId()
              .toNodeId(server.getNamespaceTable())
              .flatMap(id -> server.getAddressSpaceManager().getManagedNode(id))
              .orElse(null);

      if (node != null && aliasName.equals(node.getBrowseName().name())) {
        return node.getNodeId();
      }
    }
    return null;
  }

  /**
   * A registered AddressSpace fragment whose {@link UaNodeManager} rejects {@code AliasFor}
   * Reference adds with a RuntimeException — the application-NodeManager failure mode that can
   * strike after an alias Node was added but before its target Reference was. Node and Reference
   * removal stay permitted so the manager's best-effort cleanup can run.
   *
   * <p>Registers first in the composite (mirroring the manager's own fragment) so Call requests for
   * the Nodes it hosts route here instead of to the TestNamespace, whose filter matches the whole
   * test namespace index but whose NodeManager does not contain them.
   */
  private static final class AliasForRejectingFragment
      extends ManagedAddressSpaceFragmentWithLifecycle {

    private final AddressSpaceFilter filter =
        SimpleAddressSpaceFilter.create(getNodeManager()::containsNode);

    AliasForRejectingFragment(OpcUaServer server) {
      super(
          server,
          new UaNodeManager() {
            @Override
            public synchronized void addReferences(
                Reference reference, NamespaceTable namespaceTable) {
              if (NodeIds.AliasFor.equals(reference.getReferenceTypeId())) {
                throw new IllegalStateException("simulated NodeManager failure: " + reference);
              }
              super.addReferences(reference, namespaceTable);
            }
          });
    }

    @Override
    public AddressSpaceFilter getFilter() {
      return filter;
    }

    @Override
    protected void registerWithComposite(AddressSpaceComposite composite) {
      composite.registerFirst(this);
    }

    @Override
    public void onDataItemsCreated(List<DataItem> dataItems) {}

    @Override
    public void onDataItemsModified(List<DataItem> dataItems) {}

    @Override
    public void onDataItemsDeleted(List<DataItem> dataItems) {}

    @Override
    public void onMonitoringModeChanged(List<MonitoredItem> monitoredItems) {}
  }
}

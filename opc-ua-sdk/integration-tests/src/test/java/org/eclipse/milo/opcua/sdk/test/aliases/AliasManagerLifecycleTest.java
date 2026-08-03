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
import static org.eclipse.milo.opcua.sdk.test.aliases.AliasTestSupport.readAttribute;
import static org.eclipse.milo.opcua.sdk.test.aliases.AliasTestSupport.requireLastChange;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.sdk.server.UaNodeManager;
import org.eclipse.milo.opcua.sdk.server.aliases.AliasCategoryConfig;
import org.eclipse.milo.opcua.sdk.server.aliases.AliasManager;
import org.eclipse.milo.opcua.sdk.server.aliases.AliasManagerConfig;
import org.eclipse.milo.opcua.sdk.server.aliases.AliasTarget;
import org.eclipse.milo.opcua.sdk.server.aliases.AliasVersionStore;
import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.methods.MethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.stack.core.AttributeId;
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
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.eclipse.milo.opcua.stack.core.types.structured.CallResponse;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Lifecycle integration tests for {@link AliasManager}: the no-manager baseline the standard
 * namespace establishes at server startup, the state a manager startup applies, the state its
 * shutdown restores, and the no-trace guarantee of a failed startup.
 *
 * <p>An {@link AliasManager} is one-shot, so every test constructs a fresh instance and returns the
 * shared server to the no-manager baseline before finishing.
 */
public class AliasManagerLifecycleTest extends AbstractClientServerTest {

  /** The standard {@code FindAlias} Method instances the manager binds at startup. */
  private static final List<NodeId> STANDARD_FIND_ALIAS_NODE_IDS =
      List.of(NodeIds.Aliases_FindAlias, NodeIds.TagVariables_FindAlias, NodeIds.Topics_FindAlias);

  /**
   * The NodeId the manager allocates for the {@code FindAliasVerbose} Method it materializes on the
   * standard {@code Aliases} Object, derived from the default Node namespace index (1).
   */
  private static final NodeId ALIASES_FIND_ALIAS_VERBOSE_NODE_ID =
      new NodeId(1, "Aliases/FindAliasVerbose");

  static Stream<NodeId> standardFindAliasNodeIds() {
    return STANDARD_FIND_ALIAS_NODE_IDS.stream();
  }

  @Nested
  class WithoutManager {

    // WHY: the standard NodeSet loads the FindAlias instances executable, but a server without an
    // AliasManager has no behavior behind them; OpcUaNamespace must surface alias support as an
    // absent feature by clearing both executable flags instead of leaving a callable Method that
    // always fails (interop honesty for the Part 17 FindAlias contract).
    @ParameterizedTest
    @MethodSource(
        "org.eclipse.milo.opcua.sdk.test.aliases.AliasManagerLifecycleTest#standardFindAliasNodeIds")
    void standardFindAliasNodeIsNotExecutableWithoutManager(NodeId findAliasNodeId) {
      UaMethodNode methodNode = serverMethodNode(findAliasNodeId);

      assertFalse(methodNode.isExecutable());
      assertFalse(methodNode.isUserExecutable());
      assertInstanceOf(
          MethodInvocationHandler.NotImplementedHandler.class, methodNode.getInvocationHandler());
    }

    // WHY: DefaultAccessController enforces UserExecutable on Call, so the no-manager baseline
    // must reject a client Call with Bad_UserAccessDenied at the access-control gate rather than
    // reaching the NotImplemented handler and answering Bad_NotImplemented.
    @Test
    void callingFindAliasWithoutManagerIsDeniedUserAccess() throws UaException {
      CallMethodResult result = callFindAlias(NodeIds.Aliases, NodeIds.Aliases_FindAlias);

      assertEquals(StatusCode.of(StatusCodes.Bad_UserAccessDenied), result.getStatusCode());
    }
  }

  @Nested
  class AfterStartup {

    // WHY: startup must restore both executable flags on every standard FindAlias Node when it
    // binds its handlers — UserExecutable is the flag access control enforces on Call, and
    // Executable is what clients browse to discover the feature is present.
    @Test
    void startupRestoresExecutableFlagsOnStandardFindAliasNodes() {
      AliasManager manager = new AliasManager(server, AliasManagerConfig.builder().build());
      manager.startup();
      try {
        for (NodeId findAliasNodeId : STANDARD_FIND_ALIAS_NODE_IDS) {
          UaMethodNode methodNode = serverMethodNode(findAliasNodeId);

          assertTrue(methodNode.isExecutable());
          assertTrue(methodNode.isUserExecutable());
        }
      } finally {
        manager.shutdown();
      }
    }

    // WHY: Part 17 §6.3.2 — FindAlias returns the array of matching aliases; a pattern that
    // matches nothing yields an empty array in the output argument, not a null value or a Bad
    // result.
    @Test
    void findAliasCallSucceedsWithEmptyResultArrayWhenNothingMatches() throws UaException {
      AliasManager manager = new AliasManager(server, AliasManagerConfig.builder().build());
      manager.startup();
      try {
        CallMethodResult result = callFindAlias(NodeIds.Aliases, NodeIds.Aliases_FindAlias);

        assertEquals(StatusCode.GOOD, result.getStatusCode());

        Variant[] outputs = requireNonNull(result.getOutputArguments());
        assertEquals(1, outputs.length);

        ExtensionObject[] aliasNodeList =
            assertInstanceOf(ExtensionObject[].class, outputs[0].value());
        assertEquals(0, aliasNodeList.length);
      } finally {
        manager.shutdown();
      }
    }

    // WHY: the materialized FindAliasVerbose Node lives in the manager's own AddressSpace
    // fragment, not in any application namespace; a client Read of its attributes and argument
    // Properties pins that the fragment registration routes services to fragment-hosted Nodes,
    // and a client Call pins that Call resolution finds the Method on the ns=0 Aliases Object.
    @Test
    void materializedFindAliasVerboseIsReadableAndCallableByClient() throws UaException {
      AliasManager manager =
          new AliasManager(
              server, AliasManagerConfig.builder().findAliasVerboseEnabled(true).build());
      manager.startup();
      try {
        DataValue browseName =
            readAttribute(client, ALIASES_FIND_ALIAS_VERBOSE_NODE_ID, AttributeId.BrowseName);
        assertEquals(StatusCode.GOOD, browseName.getStatusCode());
        assertEquals(new QualifiedName(0, "FindAliasVerbose"), browseName.value().value());

        // The argument Properties are created alongside the Method Node, with NodeIds derived
        // from it, and must be readable through the fragment as well.
        DataValue inputArguments =
            readAttribute(
                client,
                new NodeId(1, "Aliases/FindAliasVerbose.InputArguments"),
                AttributeId.Value);
        assertEquals(StatusCode.GOOD, inputArguments.getStatusCode());
        ExtensionObject[] inputArgumentsValue =
            assertInstanceOf(ExtensionObject[].class, inputArguments.value().value());
        assertEquals(2, inputArgumentsValue.length);

        DataValue outputArguments =
            readAttribute(
                client,
                new NodeId(1, "Aliases/FindAliasVerbose.OutputArguments"),
                AttributeId.Value);
        assertEquals(StatusCode.GOOD, outputArguments.getStatusCode());
        ExtensionObject[] outputArgumentsValue =
            assertInstanceOf(ExtensionObject[].class, outputArguments.value().value());
        assertEquals(1, outputArgumentsValue.length);

        CallMethodResult result =
            callFindAlias(NodeIds.Aliases, ALIASES_FIND_ALIAS_VERBOSE_NODE_ID);
        assertEquals(StatusCode.GOOD, result.getStatusCode());

        Variant[] outputs = requireNonNull(result.getOutputArguments());
        ExtensionObject[] aliasNodeList =
            assertInstanceOf(ExtensionObject[].class, outputs[0].value());
        assertEquals(0, aliasNodeList.length);
      } finally {
        manager.shutdown();
      }
    }
  }

  @Nested
  class AfterShutdown {

    // WHY: shutdown must return the standard FindAlias Nodes to the no-manager baseline —
    // handlers unbound and flags cleared — so a client Call is again denied at the
    // access-control gate exactly as before the manager existed.
    @Test
    void shutdownResetsHandlersAndFlagsSoCallIsDeniedAgain() throws UaException {
      AliasManager manager = new AliasManager(server, AliasManagerConfig.builder().build());
      manager.startup();
      try {
        CallMethodResult before = callFindAlias(NodeIds.Aliases, NodeIds.Aliases_FindAlias);
        assertEquals(StatusCode.GOOD, before.getStatusCode());
      } finally {
        manager.shutdown();
      }

      assertNoManagerBaseline();

      CallMethodResult after = callFindAlias(NodeIds.Aliases, NodeIds.Aliases_FindAlias);
      assertEquals(StatusCode.of(StatusCodes.Bad_UserAccessDenied), after.getStatusCode());
    }

    // WHY: Node hosting rule — Nodes the manager creates in its own fragment leave the
    // AddressSpace with the manager, so after shutdown a client Read of the materialized
    // FindAliasVerbose Node must fail with Bad_NodeIdUnknown instead of exposing a Method with
    // no behavior behind it.
    @Test
    void shutdownRemovesMaterializedFindAliasVerboseNodes() throws UaException {
      AliasManager manager =
          new AliasManager(
              server, AliasManagerConfig.builder().findAliasVerboseEnabled(true).build());
      manager.startup();
      try {
        DataValue whileRunning =
            readAttribute(client, ALIASES_FIND_ALIAS_VERBOSE_NODE_ID, AttributeId.BrowseName);
        assertEquals(StatusCode.GOOD, whileRunning.getStatusCode());
      } finally {
        manager.shutdown();
      }

      DataValue afterShutdown =
          readAttribute(client, ALIASES_FIND_ALIAS_VERBOSE_NODE_ID, AttributeId.BrowseName);
      assertEquals(StatusCode.of(StatusCodes.Bad_NodeIdUnknown), afterShutdown.getStatusCode());
    }

    // WHY: Node hosting rule — category and alias Nodes created through addCategory live in the
    // application-supplied NodeManager, so they must remain in the AddressSpace after the manager
    // shuts down; only the manager's behavior (handlers, flags, fragment Nodes) is removed.
    @Test
    void categoryAndAliasNodesCreatedInApplicationNamespaceSurviveShutdown() throws UaException {
      var nodeManagerRef = new AtomicReference<@Nullable UaNodeManager>();
      testNamespace.configure((context, nodeManager) -> nodeManagerRef.set(nodeManager));
      UaNodeManager nodeManager = requireNonNull(nodeManagerRef.get());

      AliasManager manager = new AliasManager(server, AliasManagerConfig.builder().build());
      manager.startup();

      NodeId categoryId = newNodeId("SurvivingCategory");
      NodeId aliasNodeId = newNodeId("AliasCat/SurvivingAlias");
      try {
        try {
          var categoryConfig =
              new AliasCategoryConfig(
                  newNodeId("SurvivingCategory"),
                  NodeIds.Aliases,
                  newQualifiedName("SurvivingCategory"),
                  nodeManager,
                  name -> newNodeId("AliasCat/" + name),
                  false,
                  false,
                  false);

          assertEquals(categoryId, manager.addCategory(categoryConfig).nodeId());

          NodeId createdAliasNodeId =
              manager.addAlias(
                  categoryId,
                  "SurvivingAlias",
                  List.of(
                      new AliasTarget(newNodeId("TestInt32").expanded(), null, NodeIds.AliasFor)));
          assertEquals(aliasNodeId, createdAliasNodeId);
        } finally {
          manager.shutdown();
        }

        assertTrue(server.getAddressSpaceManager().getManagedNode(categoryId).isPresent());
        assertTrue(server.getAddressSpaceManager().getManagedNode(aliasNodeId).isPresent());

        DataValue categoryBrowseName = readAttribute(client, categoryId, AttributeId.BrowseName);
        assertEquals(StatusCode.GOOD, categoryBrowseName.getStatusCode());
        assertEquals(newQualifiedName("SurvivingCategory"), categoryBrowseName.value().value());

        DataValue aliasBrowseName = readAttribute(client, aliasNodeId, AttributeId.BrowseName);
        assertEquals(StatusCode.GOOD, aliasBrowseName.getStatusCode());
      } finally {
        // Delete the surviving Nodes so other tests observe an alias-free hierarchy; the alias
        // first, so its category linkage is gone before the category (and its children) go.
        server.getAddressSpaceManager().getManagedNode(aliasNodeId).ifPresent(UaNode::delete);
        server.getAddressSpaceManager().getManagedNode(categoryId).ifPresent(UaNode::delete);
      }
    }
  }

  @Nested
  class StartupFailure {

    // WHY: startup validates every standard FindAlias Node for an application-bound handler
    // before mutating anything, so a conflict must fail startup with IllegalStateException and
    // leave no trace — flags unchanged, no other Node's handler touched, and the conflicting
    // handler still in place.
    @Test
    void handlerConflictFailsStartupAndLeavesNoStateBehind() {
      UaMethodNode conflictedNode = serverMethodNode(NodeIds.TagVariables_FindAlias);

      var conflictingHandler =
          new AbstractMethodInvocationHandler(conflictedNode) {
            @Override
            public Argument[] getInputArguments() {
              return new Argument[0];
            }

            @Override
            public Argument[] getOutputArguments() {
              return new Argument[0];
            }

            @Override
            protected Variant[] invoke(InvocationContext invocationContext, Variant[] inputValues) {
              return new Variant[0];
            }
          };

      conflictedNode.setInvocationHandler(conflictingHandler);
      try {
        AliasManager manager = new AliasManager(server, AliasManagerConfig.builder().build());

        assertThrows(IllegalStateException.class, manager::startup);

        // The conflicting handler is untouched and the other standard Nodes are still in the
        // unbound baseline state.
        assertSame(conflictingHandler, conflictedNode.getInvocationHandler());
        assertFalse(conflictedNode.isExecutable());
        assertFalse(conflictedNode.isUserExecutable());

        for (NodeId nodeId : List.of(NodeIds.Aliases_FindAlias, NodeIds.Topics_FindAlias)) {
          UaMethodNode methodNode = serverMethodNode(nodeId);

          assertInstanceOf(
              MethodInvocationHandler.NotImplementedHandler.class,
              methodNode.getInvocationHandler());
          assertFalse(methodNode.isExecutable());
          assertFalse(methodNode.isUserExecutable());
        }
      } finally {
        conflictedNode.setInvocationHandler(MethodInvocationHandler.NOT_IMPLEMENTED);
      }

      // A failed startup left nothing behind, so a fresh manager starts up cleanly afterwards.
      AliasManager freshManager = new AliasManager(server, AliasManagerConfig.builder().build());
      freshManager.startup();
      try {
        assertTrue(serverMethodNode(NodeIds.TagVariables_FindAlias).isUserExecutable());
      } finally {
        freshManager.shutdown();
      }
    }

    // WHY: Part 17 §6.3.1 requires the root LastChange version to persist across restarts;
    // starting with silently reset versions would leave client caches undetectably stale, so an
    // unreadable version store must fail startup — and, failing in the mutation phase, must roll
    // back to the no-manager baseline.
    @Test
    void versionStoreLoadFailureFailsStartupAndLeavesNoStateBehind() {
      AliasManager manager =
          new AliasManager(
              server, AliasManagerConfig.builder().versionStore(failingVersionStore()).build());

      IllegalStateException e = assertThrows(IllegalStateException.class, manager::startup);
      assertInstanceOf(UaException.class, e.getCause());

      assertNoManagerBaseline();
    }

    // WHY: a manager whose startup failed must be safely disposable — the lifecycle lands in
    // the stopped state, so isNotRunning() reports the failure and a subsequent shutdown() is a
    // silent no-op. Were shutdown() to run onShutdown against never-applied state, it would
    // throw (the manager's AddressSpace fragment was never started here) and turn routine
    // disposal into a second failure.
    @Test
    void failedStartupLeavesManagerStoppedAndSubsequentShutdownIsANoOp() {
      AliasManager manager =
          new AliasManager(
              server, AliasManagerConfig.builder().versionStore(failingVersionStore()).build());

      assertThrows(IllegalStateException.class, manager::startup);

      assertTrue(manager.isNotRunning());

      // The failed startup already rolled back, so there is nothing to undo: this must return
      // silently without touching the AddressSpace.
      manager.shutdown();

      assertTrue(manager.isNotRunning());
      assertNoManagerBaseline();
    }

    // WHY: the no-trace guarantee covers loaded versions too — loadPersisted must not publish to
    // the LastChange Properties before every fallible startup step has succeeded, or a failed
    // startup would leave persisted values published into the AddressSpace.
    @Test
    void failedStartupDoesNotPublishLoadedVersionsToLastChangeProperties() throws UaException {
      var nodeManagerRef = new AtomicReference<@Nullable UaNodeManager>();
      testNamespace.configure((context, nodeManager) -> nodeManagerRef.set(nodeManager));
      UaNodeManager nodeManager = requireNonNull(nodeManagerRef.get());

      NodeId categoryId = newNodeId("DeferralCategory");

      // Setup: a first manager creates a category whose LastChange Property gets a published
      // value, then shuts down; the category survives in the application namespace.
      AliasManager setupManager = new AliasManager(server, AliasManagerConfig.builder().build());
      setupManager.startup();
      try {
        setupManager.addCategory(
            new AliasCategoryConfig(
                categoryId,
                NodeIds.Aliases,
                newQualifiedName("DeferralCategory"),
                nodeManager,
                name -> newNodeId("DeferralCat/" + name),
                true,
                false,
                false));
      } finally {
        setupManager.shutdown();
      }

      try {
        UInteger publishedBefore = requireLastChange(server, categoryId);
        UInteger farFuture = uint(4_000_000_000L);

        // A store that claims a far-future persisted version for the category, has no entry for
        // the root, and cannot save: startup loads the value and then fails at the root
        // LastChange initialization — i.e. AFTER the load.
        var store =
            new AliasVersionStore() {
              @Override
              public Map<ExpandedNodeId, UInteger> load() {
                return Map.of(categoryId.expanded(server.getNamespaceTable()), farFuture);
              }

              @Override
              public void save(ExpandedNodeId id, UInteger value) throws UaException {
                throw new UaException(StatusCodes.Bad_ResourceUnavailable, "save failed");
              }
            };

        AliasManager manager =
            new AliasManager(server, AliasManagerConfig.builder().versionStore(store).build());

        assertThrows(IllegalStateException.class, manager::startup);

        // The loaded far-future value was never published.
        assertEquals(publishedBefore, requireLastChange(server, categoryId));
        assertNoManagerBaseline();
      } finally {
        server.getAddressSpaceManager().getManagedNode(categoryId).ifPresent(UaNode::delete);
      }
    }

    /** An {@link AliasVersionStore} whose load fails, simulating unreadable persisted state. */
    private AliasVersionStore failingVersionStore() {
      return new AliasVersionStore() {
        @Override
        public Map<ExpandedNodeId, UInteger> load() throws UaException {
          throw new UaException(StatusCodes.Bad_InternalError, "persisted state unreadable");
        }

        @Override
        public void save(ExpandedNodeId categoryId, UInteger value) {}
      };
    }
  }

  /** Resolve a standard Method Node from the running server, failing the test if it is absent. */
  private UaMethodNode serverMethodNode(NodeId nodeId) {
    UaNode node = server.getAddressSpaceManager().getManagedNode(nodeId).orElseThrow();

    return assertInstanceOf(UaMethodNode.class, node);
  }

  /** Assert every standard {@code FindAlias} Node is in the unbound, non-executable baseline. */
  private void assertNoManagerBaseline() {
    for (NodeId nodeId : STANDARD_FIND_ALIAS_NODE_IDS) {
      UaMethodNode methodNode = serverMethodNode(nodeId);

      assertInstanceOf(
          MethodInvocationHandler.NotImplementedHandler.class, methodNode.getInvocationHandler());
      assertFalse(methodNode.isExecutable());
      assertFalse(methodNode.isUserExecutable());
    }
  }

  /**
   * Call {@code FindAlias}/{@code FindAliasVerbose} on {@code objectId} through the client with
   * pattern {@code "%"} and no ReferenceType filter.
   */
  private CallMethodResult callFindAlias(NodeId objectId, NodeId methodId) throws UaException {
    CallResponse response =
        client.call(
            List.of(
                new CallMethodRequest(
                    objectId,
                    methodId,
                    new Variant[] {new Variant("%"), new Variant(NodeId.NULL_VALUE)})));

    return requireNonNull(response.getResults())[0];
  }
}

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
import static org.eclipse.milo.opcua.sdk.test.aliases.AliasTestSupport.readLastChange;
import static org.eclipse.milo.opcua.sdk.test.aliases.AliasTestSupport.requireLastChange;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.server.UaNodeManager;
import org.eclipse.milo.opcua.sdk.server.aliases.AliasCategory;
import org.eclipse.milo.opcua.sdk.server.aliases.AliasManager;
import org.eclipse.milo.opcua.sdk.server.aliases.AliasManagerConfig;
import org.eclipse.milo.opcua.sdk.server.aliases.AliasTarget;
import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.methods.MethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.eclipse.milo.opcua.stack.core.types.structured.CallResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Integration tests for {@link AliasManager#adoptCategory}: managing an {@code
 * AliasNameCategoryType} instance the application (e.g. a NodeSet loader) created, rather than one
 * the manager instantiated itself.
 *
 * <p>Adoptable categories are built by hand in the test namespace — an Object with a {@code
 * HasTypeDefinition} Reference to {@code AliasNameCategoryType} and component Method Nodes carrying
 * the default {@code NotImplementedHandler} — mirroring what a NodeSet loader produces. An {@link
 * AliasManager} is one-shot, so every test constructs a fresh instance against the shared server
 * and shuts it down before returning; the hand-built category Nodes stay behind, with per-test
 * names so tests remain order-independent.
 */
// Fields below are assigned in @BeforeAll, which the nullability inspection does not model.
@SuppressWarnings("NotNullFieldNotInitialized")
public class AliasAdoptCategoryTest extends AbstractClientServerTest {

  private UaNodeContext testNodeContext;
  private UaNodeManager testNodeManager;

  @BeforeAll
  void captureTestNamespaceInternals() {
    testNamespace.configure(
        (context, nodeManager) -> {
          testNodeContext = context;
          testNodeManager = nodeManager;
        });
  }

  // WHY: the core adoption contract — a NodeSet-style category becomes searchable over the wire,
  // and the aliases added to it live in the manager's own AddressSpace fragment (leaving with the
  // manager at shutdown), because the manager must not write into Nodes it does not own beyond
  // the adopted Method bindings.
  @Test
  void adoptionBindsFindAliasAndAliasesLiveInTheManagersFragment() throws UaException {
    NodeId categoryId = buildCategory("AdoptedRoot", true, false, false, false);
    NodeId findAliasNodeId = newNodeId("AdoptedRoot/FindAlias");
    NodeId aliasNodeId;

    AliasManager manager = newStartedManager();
    try {
      AliasCategory category = manager.adoptCategory(categoryId);

      assertEquals(categoryId, category.nodeId());
      assertFalse(category.lastChangeEnabled());
      assertFalse(category.findAliasVerboseEnabled());
      assertFalse(category.configurationEnabled());

      // The default alias NodeId factory allocates "<category NodeId>/Alias/<name>" in the
      // configured Node namespace.
      aliasNodeId =
          manager.addAlias(categoryId, "AdoptedAlias", List.of(aliasFor(newNodeId("TestInt32"))));
      assertEquals(
          new NodeId(
              testNamespace.getNamespaceIndex(),
              categoryId.toParseableString() + "/Alias/AdoptedAlias"),
          aliasNodeId);

      // Hosted in the manager's fragment, not the application NodeManager the category lives in.
      assertTrue(server.getAddressSpaceManager().getManagedNode(aliasNodeId).isPresent());
      assertFalse(testNodeManager.containsNode(aliasNodeId));

      // The adopted FindAlias is callable over the wire and finds the alias.
      CallMethodResult result = callFind(categoryId, findAliasNodeId);
      assertEquals(StatusCode.GOOD, result.getStatusCode());
      assertEquals(1, manager.findAlias(categoryId, "AdoptedAlias", null).size());
    } finally {
      manager.shutdown();
    }

    // Shutdown removed the fragment-hosted alias but left the application's category Nodes,
    // returning the adopted FindAlias to its unbound, non-executable state.
    assertTrue(server.getAddressSpaceManager().getManagedNode(aliasNodeId).isEmpty());
    assertTrue(server.getAddressSpaceManager().getManagedNode(categoryId).isPresent());

    UaMethodNode findAliasNode = serverMethodNode(findAliasNodeId);
    assertInstanceOf(
        MethodInvocationHandler.NotImplementedHandler.class, findAliasNode.getInvocationHandler());
    assertFalse(findAliasNode.isExecutable());
    assertFalse(findAliasNode.isUserExecutable());
  }

  // WHY: a pre-existing FindAliasVerbose instance is the category definition's decision to offer
  // verbose lookup; adoption must give it behavior like the other optional Methods instead of
  // leaving a permanently non-executable Method, and the returned handle must report it.
  @Test
  void adoptionBindsPresentUnboundFindAliasVerbose() throws UaException {
    NodeId categoryId = buildCategory("AdoptedVerbose", true, true, false, false);
    NodeId verboseNodeId = newNodeId("AdoptedVerbose/FindAliasVerbose");

    AliasManager manager = newStartedManager();
    try {
      AliasCategory category = manager.adoptCategory(categoryId);
      assertTrue(category.findAliasVerboseEnabled());

      manager.addAlias(
          categoryId, "AdoptedVerboseAlias", List.of(aliasFor(newNodeId("TestInt32"))));

      CallMethodResult result = callFind(categoryId, verboseNodeId);
      assertEquals(StatusCode.GOOD, result.getStatusCode());
    } finally {
      manager.shutdown();
    }

    UaMethodNode verboseNode = serverMethodNode(verboseNodeId);
    assertInstanceOf(
        MethodInvocationHandler.NotImplementedHandler.class, verboseNode.getInvocationHandler());
    assertFalse(verboseNode.isExecutable());
  }

  // WHY: pre-existing mutation Method Nodes are bound opportunistically and reported through
  // configurationEnabled; the two-layer deny posture still applies (the default policy denies
  // every session), so binding alone must not open network mutation.
  @Test
  void adoptionBindsPresentUnboundMutationMethodsAndReportsConfigurationEnabled()
      throws UaException {
    NodeId categoryId = buildCategory("AdoptedMutable", true, false, true, false);
    NodeId addMethodNodeId = newNodeId("AdoptedMutable/AddAliasesToCategory");

    AliasManager manager = newStartedManager();
    try {
      AliasCategory category = manager.adoptCategory(categoryId);

      assertTrue(category.configurationEnabled());

      UaMethodNode addMethodNode = serverMethodNode(addMethodNodeId);
      assertFalse(
          addMethodNode.getInvocationHandler()
              instanceof MethodInvocationHandler.NotImplementedHandler);
      assertTrue(addMethodNode.isExecutable());
    } finally {
      manager.shutdown();
    }
  }

  // WHY: "bound only if still unbound" — an optional Method another component already gave
  // behavior must be left untouched by adoption AND by the manager's shutdown, and the returned
  // handle must not claim it.
  @Test
  void adoptionLeavesAlreadyBoundFindAliasVerboseUntouched() throws UaException {
    NodeId categoryId = buildCategory("AdoptedForeignVerbose", true, true, false, false);
    UaMethodNode verboseNode =
        serverMethodNode(newNodeId("AdoptedForeignVerbose/FindAliasVerbose"));

    MethodInvocationHandler foreignHandler = dummyHandler(verboseNode);
    verboseNode.setInvocationHandler(foreignHandler);
    try {
      AliasManager manager = newStartedManager();
      try {
        AliasCategory category = manager.adoptCategory(categoryId);

        assertFalse(category.findAliasVerboseEnabled());
        assertSame(foreignHandler, verboseNode.getInvocationHandler());
      } finally {
        manager.shutdown();
      }

      // Shutdown unbinds only what adoption bound; the foreign handler survives.
      assertSame(foreignHandler, verboseNode.getInvocationHandler());
    } finally {
      verboseNode.setInvocationHandler(MethodInvocationHandler.NOT_IMPLEMENTED);
    }
  }

  // WHY: an adopted category with a LastChange Property participates in §6.3.1 version
  // maintenance: the handle reports it and mutations through the manager publish a value.
  @Test
  void adoptedCategoryWithLastChangePropertyGetsVersionMaintenance() throws UaException {
    NodeId categoryId = buildCategory("AdoptedVersioned", true, false, false, true);

    AliasManager manager = newStartedManager();
    try {
      AliasCategory category = manager.adoptCategory(categoryId);
      assertTrue(category.lastChangeEnabled());

      assertNull(readLastChange(server, categoryId));

      manager.addAlias(
          categoryId, "AdoptedVersionedAlias", List.of(aliasFor(newNodeId("TestInt32"))));

      requireLastChange(server, categoryId);
    } finally {
      manager.shutdown();
    }
  }

  // WHY: the five documented adoption failure codes, each of which must reject the call without
  // managing the category (adoptCategory validates before binding anything).

  @Test
  void adoptingAnAlreadyManagedCategoryFailsWithBadNodeIdExists() throws UaException {
    NodeId categoryId = buildCategory("AdoptedTwice", true, false, false, false);

    AliasManager manager = newStartedManager();
    try {
      manager.adoptCategory(categoryId);

      UaException e = assertThrows(UaException.class, () -> manager.adoptCategory(categoryId));
      assertEquals(StatusCode.of(StatusCodes.Bad_NodeIdExists), e.getStatusCode());
    } finally {
      manager.shutdown();
    }
  }

  @Test
  void adoptingAStandardCategoryFailsWithBadInvalidArgument() {
    AliasManager manager = newStartedManager();
    try {
      UaException e =
          assertThrows(UaException.class, () -> manager.adoptCategory(NodeIds.TagVariables));
      assertEquals(StatusCode.of(StatusCodes.Bad_InvalidArgument), e.getStatusCode());
    } finally {
      manager.shutdown();
    }
  }

  @Test
  void adoptingANonexistentNodeFailsWithBadNodeIdUnknown() {
    AliasManager manager = newStartedManager();
    try {
      UaException e =
          assertThrows(UaException.class, () -> manager.adoptCategory(newNodeId("NoSuchCategory")));
      assertEquals(StatusCode.of(StatusCodes.Bad_NodeIdUnknown), e.getStatusCode());
    } finally {
      manager.shutdown();
    }
  }

  @Test
  void adoptingANonCategoryNodeFailsWithBadNodeIdUnknown() {
    AliasManager manager = newStartedManager();
    try {
      UaException e =
          assertThrows(UaException.class, () -> manager.adoptCategory(newNodeId("TestInt32")));
      assertEquals(StatusCode.of(StatusCodes.Bad_NodeIdUnknown), e.getStatusCode());
    } finally {
      manager.shutdown();
    }
  }

  @Test
  void adoptingACategoryWithoutFindAliasFailsWithBadNotFound() throws UaException {
    NodeId categoryId = buildCategory("AdoptedNoFindAlias", false, false, false, false);

    AliasManager manager = newStartedManager();
    try {
      UaException e = assertThrows(UaException.class, () -> manager.adoptCategory(categoryId));
      assertEquals(StatusCode.of(StatusCodes.Bad_NotFound), e.getStatusCode());
    } finally {
      manager.shutdown();
    }
  }

  @Test
  void adoptingACategoryWhoseFindAliasIsAlreadyBoundFailsWithBadInvalidState() throws UaException {
    NodeId categoryId = buildCategory("AdoptedConflicted", true, false, false, false);
    UaMethodNode findAliasNode = serverMethodNode(newNodeId("AdoptedConflicted/FindAlias"));

    findAliasNode.setInvocationHandler(dummyHandler(findAliasNode));
    try {
      AliasManager manager = newStartedManager();
      try {
        UaException e = assertThrows(UaException.class, () -> manager.adoptCategory(categoryId));
        assertEquals(StatusCode.of(StatusCodes.Bad_InvalidState), e.getStatusCode());
      } finally {
        manager.shutdown();
      }
    } finally {
      findAliasNode.setInvocationHandler(MethodInvocationHandler.NOT_IMPLEMENTED);
    }
  }

  /**
   * Build a NodeSet-style adoptable category in the test namespace: an Object typed {@code
   * AliasNameCategoryType}, organized under the standard root {@code Aliases} Object, with the
   * requested component Method Nodes (all carrying the default {@code NotImplementedHandler}) and
   * optionally a {@code LastChange} Property.
   */
  private NodeId buildCategory(
      String name,
      boolean withFindAlias,
      boolean withFindAliasVerbose,
      boolean withAddMethod,
      boolean withLastChange) {

    NodeId categoryId = newNodeId(name);

    var categoryNode =
        new UaObjectNode(
            testNodeContext,
            categoryId,
            newQualifiedName(name),
            LocalizedText.english(name),
            LocalizedText.NULL_VALUE,
            uint(0),
            uint(0));
    testNodeManager.addNode(categoryNode);

    categoryNode.addReference(
        new Reference(
            categoryId,
            NodeIds.HasTypeDefinition,
            NodeIds.AliasNameCategoryType.expanded(),
            Reference.Direction.FORWARD));

    categoryNode.addReference(
        new Reference(
            categoryId,
            NodeIds.Organizes,
            NodeIds.Aliases.expanded(),
            Reference.Direction.INVERSE));

    if (withFindAlias) {
      buildComponentMethod(categoryId, name, "FindAlias");
    }
    if (withFindAliasVerbose) {
      buildComponentMethod(categoryId, name, "FindAliasVerbose");
    }
    if (withAddMethod) {
      buildComponentMethod(categoryId, name, "AddAliasesToCategory");
    }

    if (withLastChange) {
      var propertyNode =
          new UaVariableNode.UaVariableNodeBuilder(testNodeContext)
              .setNodeId(newNodeId(name + "/LastChange"))
              .setBrowseName(new QualifiedName(0, "LastChange"))
              .setDisplayName(LocalizedText.english("LastChange"))
              .setDataType(NodeIds.VersionTime)
              .setTypeDefinition(NodeIds.PropertyType)
              .build();
      testNodeManager.addNode(propertyNode);

      propertyNode.addReference(
          new Reference(
              propertyNode.getNodeId(),
              NodeIds.HasProperty,
              categoryId.expanded(),
              Reference.Direction.INVERSE));
    }

    return categoryId;
  }

  /** Build an unbound Method Node named {@code methodName} as a component of the category. */
  private void buildComponentMethod(NodeId categoryId, String categoryName, String methodName) {
    UaMethodNode.build(
        testNodeContext,
        b -> {
          b.setNodeId(newNodeId(categoryName + "/" + methodName));
          b.setBrowseName(new QualifiedName(0, methodName));
          b.setDisplayName(LocalizedText.english(methodName));

          b.addReference(
              new Reference(
                  b.getNodeId(),
                  NodeIds.HasComponent,
                  categoryId.expanded(),
                  Reference.Direction.INVERSE));

          return b.buildAndAdd();
        });
  }

  private AliasManager newStartedManager() {
    AliasManager manager =
        new AliasManager(
            server,
            AliasManagerConfig.builder()
                .nodeNamespaceIndex(testNamespace.getNamespaceIndex())
                .build());
    manager.startup();
    return manager;
  }

  private AliasTarget aliasFor(NodeId targetNodeId) {
    return new AliasTarget(targetNodeId.expanded(), null, NodeIds.AliasFor);
  }

  /** A handler standing in for "some other component already bound this Method". */
  private static AbstractMethodInvocationHandler dummyHandler(UaMethodNode methodNode) {
    return new AbstractMethodInvocationHandler(methodNode) {
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
  }

  private UaMethodNode serverMethodNode(NodeId nodeId) {
    UaNode node = server.getAddressSpaceManager().getManagedNode(nodeId).orElseThrow();
    return assertInstanceOf(UaMethodNode.class, node);
  }

  /**
   * Call {@code FindAlias}/{@code FindAliasVerbose} on {@code objectId} through the client with
   * pattern {@code "%"} and no ReferenceType filter.
   */
  private CallMethodResult callFind(NodeId objectId, NodeId methodId) throws UaException {
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

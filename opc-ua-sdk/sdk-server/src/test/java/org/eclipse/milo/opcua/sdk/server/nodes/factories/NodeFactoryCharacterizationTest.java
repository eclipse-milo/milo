/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.nodes.factories;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import org.eclipse.milo.opcua.sdk.core.AccessLevel;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.core.typetree.ReferenceTypeTree;
import org.eclipse.milo.opcua.sdk.server.AddressSpaceManager;
import org.eclipse.milo.opcua.sdk.server.NodeManager;
import org.eclipse.milo.opcua.sdk.server.ObjectTypeManager;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.UaNodeManager;
import org.eclipse.milo.opcua.sdk.server.VariableTypeManager;
import org.eclipse.milo.opcua.sdk.server.model.ObjectTypeInitializer;
import org.eclipse.milo.opcua.sdk.server.model.VariableTypeInitializer;
import org.eclipse.milo.opcua.sdk.server.namespaces.loader.NodeLoader;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.typetree.ReferenceTypeTreeBuilder;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.OpcUaEncodingManager;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.PermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

/**
 * Characterization tests pinning the observable behavior of {@link NodeFactory} and {@link
 * InstanceDeclarationHierarchy} exactly as shipped, before the replacement node-instantiation
 * engine lands.
 *
 * <p>These tests are the fixture required by the replacement design's G7 ("legacy untouched"): they
 * assert what the legacy code <em>actually does today</em>, including behavior the design catalogs
 * as defective (D1, D2, D5, D6, silent collision replacement, the child NodeId formula's
 * ambiguities). A test failure here means legacy behavior drifted. Any deliberate, reviewed legacy
 * fix must update this fixture in the same commit — that is the audit trail G7 requires. Do not
 * "fix" a failing assertion by changing it to the spec-correct expectation.
 *
 * <p><b>Nondeterminism (design risk R3):</b> node publication order — the order nodes are added to
 * the {@link NodeManager} — follows {@code HashMap} iteration order in {@code
 * NodeFactory.createNodeTree} and is not deterministic. Callback iteration follows the browse-path
 * tree. No test here asserts publication or callback <em>ordering</em>; assertions are on set
 * membership and per-node state only. G7 verification is therefore narrowed to this deterministic
 * subset, with ordering documented as unspecified.
 *
 * <p><b>Isolation note:</b> {@code NodeFactory} keeps a static JVM-global IDH cache keyed by type
 * NodeId alone (the D8 hazard). Every synthetic type in these tests gets a NodeId unique across the
 * whole test run ({@link #testPrefix}) so a cached hierarchy from one test can never be observed by
 * another.
 */
public class NodeFactoryCharacterizationTest {

  private static final AtomicInteger TEST_COUNTER = new AtomicInteger();

  private static final String TEST_NAMESPACE_URI = "urn:eclipse:milo:test:characterization";

  /** Unique per test method; prefixes every synthetic NodeId to defeat the static IDH cache. */
  private String testPrefix;

  private OpcUaServer server;
  private NamespaceTable namespaceTable;
  private UaNodeManager nodeManager;
  private UaNodeContext context;
  private NodeFactory nodeFactory;

  @BeforeEach
  public void setup() throws Exception {
    testPrefix = "NodeFactoryCharacterization" + TEST_COUNTER.incrementAndGet();

    server = Mockito.mock(OpcUaServer.class);

    namespaceTable = new NamespaceTable();
    namespaceTable.add(TEST_NAMESPACE_URI);
    Mockito.when(server.getNamespaceTable()).thenReturn(namespaceTable);

    nodeManager = new UaNodeManager();

    AddressSpaceManager addressSpaceManager = Mockito.mock(AddressSpaceManager.class);

    Mockito.when(addressSpaceManager.getManagedNode(Mockito.any(NodeId.class)))
        .then(
            (Answer<Optional<UaNode>>)
                invocation -> nodeManager.getNode(invocation.getArgument(0)));

    Mockito.when(addressSpaceManager.getManagedNode(Mockito.any(ExpandedNodeId.class)))
        .then(
            (Answer<Optional<UaNode>>)
                invocation -> nodeManager.getNode(invocation.getArgument(0), namespaceTable));

    Mockito.when(addressSpaceManager.getManagedReferences(Mockito.any(NodeId.class)))
        .then(
            (Answer<List<Reference>>)
                invocation -> nodeManager.getReferences(invocation.getArgument(0)));

    Mockito.when(addressSpaceManager.getManagedReferences(Mockito.any(NodeId.class), Mockito.any()))
        .then(
            (Answer<List<Reference>>)
                invocation ->
                    nodeManager.getReferences(
                        invocation.getArgument(0), invocation.getArgument(1)));

    Mockito.when(server.getAddressSpaceManager()).thenReturn(addressSpaceManager);
    Mockito.when(server.getStaticEncodingContext()).thenReturn(DefaultEncodingContext.INSTANCE);
    Mockito.when(server.getEncodingManager()).thenReturn(OpcUaEncodingManager.getInstance());

    context =
        new UaNodeContext() {
          @Override
          public OpcUaServer getServer() {
            return server;
          }

          @Override
          public NodeManager<UaNode> getNodeManager() {
            return nodeManager;
          }
        };

    new NodeLoader(context, nodeManager).loadNodes();

    // InstanceDeclarationHierarchy consults the ReferenceTypeTree to classify non-hierarchical
    // references; the tree must be built after ns0 is loaded, and outside the when() call
    // because building it interacts with the same mock.
    ReferenceTypeTree referenceTypeTree = ReferenceTypeTreeBuilder.build(server);
    Mockito.when(server.getReferenceTypeTree()).thenReturn(referenceTypeTree);

    ObjectTypeManager objectTypeManager = new ObjectTypeManager();
    ObjectTypeInitializer.initialize(namespaceTable, objectTypeManager);

    VariableTypeManager variableTypeManager = new VariableTypeManager();
    VariableTypeInitializer.initialize(namespaceTable, variableTypeManager);

    nodeFactory = new NodeFactory(context, objectTypeManager, variableTypeManager);
  }

  // region Child NodeId formula

  /**
   * Child NodeIds are the root identifier string-concatenated with the joined browse path
   * (separator "/", namespace-index-prefixed names), always a String identifier in the root's
   * namespace, at every depth.
   */
  @Test
  public void childNodeIdFormula() throws Exception {
    UaObjectTypeNode typeNode = addObjectType("FormulaType", NodeIds.BaseObjectType);
    UaObjectNode child =
        addObjectDeclaration(
            typeNode, "Child", NodeIds.BaseObjectType, NodeIds.ModellingRule_Mandatory);
    addVariableDeclaration(
        child, "Leaf", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);

    NodeId rootNodeId = new NodeId(1, testPrefix + ":TestRoot");
    UaNode root = nodeFactory.createNode(rootNodeId, typeNode.getNodeId());

    assertEquals(rootNodeId, root.getNodeId());

    NodeId childId = new NodeId(1, testPrefix + ":TestRoot/1:Child");
    NodeId leafId = new NodeId(1, testPrefix + ":TestRoot/1:Child/1:Leaf");

    assertTrue(nodeManager.containsNode(childId), "child at rootIdentifier + /1:Child");
    assertTrue(nodeManager.containsNode(leafId), "grandchild at rootIdentifier + /1:Child/1:Leaf");

    // The instance hierarchy is wired: root -> child -> leaf via forward HasComponent.
    assertTrue(
        nodeManager.getReferences(rootNodeId).stream()
            .anyMatch(
                r ->
                    r.isForward()
                        && NodeIds.HasComponent.equals(r.getReferenceTypeId())
                        && r.getTargetNodeId().equals(childId.expanded())));
    assertTrue(
        nodeManager.getReferences(childId).stream()
            .anyMatch(
                r ->
                    r.isForward()
                        && NodeIds.HasComponent.equals(r.getReferenceTypeId())
                        && r.getTargetNodeId().equals(leafId.expanded())));
  }

  /**
   * The formula is unescaped {@code String.format("%s%s", rootIdentifier, joinedPath)}: a numeric
   * root identifier {@code 7} and a String root identifier {@code "7"} derive <em>identical</em>
   * child NodeIds, and the second instantiation silently replaces the first's children.
   */
  @Test
  public void childNodeIdFormulaNumericVsStringRootAmbiguity() throws Exception {
    UaObjectTypeNode typeNode = addObjectType("AmbiguousRootType", NodeIds.BaseObjectType);
    addVariableDeclaration(
        typeNode, "Foo", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);

    NodeId derivedChildId = new NodeId(1, "7/1:Foo");

    nodeFactory.createNode(new NodeId(1, uint(7)), typeNode.getNodeId());
    UaNode childOfNumericRoot = nodeManager.getNode(derivedChildId).orElse(null);
    assertNotNull(childOfNumericRoot, "numeric root 7 derives String child id \"7/1:Foo\"");

    nodeFactory.createNode(new NodeId(1, "7"), typeNode.getNodeId());
    UaNode childOfStringRoot = nodeManager.getNode(derivedChildId).orElse(null);
    assertNotNull(childOfStringRoot, "string root \"7\" derives the same child id \"7/1:Foo\"");

    assertNotSame(
        childOfNumericRoot,
        childOfStringRoot,
        "second instantiation replaced the first root's child at the shared NodeId");
  }

  /**
   * The formula does not escape "/" or namespace-prefix-like text in BrowseNames: a single member
   * named {@code "A/1:B"} derives the same NodeId as a nested member {@code A} containing {@code
   * B}, and instantiating one over the other silently replaces the colliding node.
   */
  @Test
  public void childNodeIdFormulaSlashInBrowseNameAmbiguity() throws Exception {
    UaObjectTypeNode nestedType = addObjectType("NestedPathType", NodeIds.BaseObjectType);
    UaObjectNode a =
        addObjectDeclaration(
            nestedType, "A", NodeIds.BaseObjectType, NodeIds.ModellingRule_Mandatory);
    addVariableDeclaration(a, "B", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);

    UaObjectTypeNode slashType = addObjectType("SlashNameType", NodeIds.BaseObjectType);
    addVariableDeclaration(
        slashType, "A/1:B", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);

    NodeId rootNodeId = new NodeId(1, testPrefix + ":AmbigRoot");
    NodeId collidingId = new NodeId(1, testPrefix + ":AmbigRoot/1:A/1:B");

    nodeFactory.createNode(rootNodeId, nestedType.getNodeId());
    UaNode nested = nodeManager.getNode(collidingId).orElse(null);
    assertNotNull(nested, "nested member A/B lands at .../1:A/1:B");
    assertEquals(new QualifiedName(1, "B"), nested.getBrowseName());

    nodeFactory.createNode(rootNodeId, slashType.getNodeId());
    UaNode slash = nodeManager.getNode(collidingId).orElse(null);
    assertNotNull(slash, "member named \"A/1:B\" derives the identical NodeId");
    assertEquals(new QualifiedName(1, "A/1:B"), slash.getBrowseName());
    assertNotSame(nested, slash, "distinct logical members collide on one NodeId");
  }

  // endregion

  // region Callback timing and contract

  /**
   * Callbacks fire after <em>every</em> node of the tree has been stored in the NodeManager; the
   * root fires with {@code parent == null}; {@code includeOptionalNode} receives the
   * <em>member's</em> type definition id (not, as its Javadoc claims, the type being instantiated).
   * Callback ordering is browse-path-tree traversal over HashMap-grouped children and is not
   * asserted (see class Javadoc).
   */
  @Test
  public void callbackTimingAndContract() throws Exception {
    UaObjectTypeNode typeNode = addObjectType("CallbackType", NodeIds.BaseObjectType);
    addVariableDeclaration(
        typeNode, "Var", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);
    addObjectDeclaration(typeNode, "Obj", NodeIds.BaseObjectType, NodeIds.ModellingRule_Mandatory);
    addMethodDeclaration(typeNode, "Method", NodeIds.ModellingRule_Mandatory);
    addVariableDeclaration(
        typeNode, "OptVar", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Optional);

    NodeId rootNodeId = new NodeId(1, testPrefix + ":CallbackRoot");

    List<NodeId> expectedStoredIds =
        List.of(
            rootNodeId,
            new NodeId(1, testPrefix + ":CallbackRoot/1:Var"),
            new NodeId(1, testPrefix + ":CallbackRoot/1:Obj"),
            new NodeId(1, testPrefix + ":CallbackRoot/1:Method"),
            new NodeId(1, testPrefix + ":CallbackRoot/1:OptVar"));

    var recorder =
        new RecordingCallback(true) {
          @Override
          void onAnyAdded(@Nullable UaNode parent, UaNode instance) {
            // Timing pin: at callback time the entire tree is already stored.
            for (NodeId id : expectedStoredIds) {
              assertTrue(
                  nodeManager.containsNode(id),
                  "all nodes stored before any callback fires (missing " + id + ")");
            }
            super.onAnyAdded(parent, instance);
          }
        };

    UaNode root = nodeFactory.createNode(rootNodeId, typeNode.getNodeId(), recorder);

    // Root fires with parent == null.
    assertTrue(
        recorder.objectEvents.stream().anyMatch(e -> e.parent() == null && e.instance() == root),
        "root object callback fired with parent == null");

    // Members fire with the root as parent; assert set membership, not order.
    assertTrue(
        recorder.variableEvents.stream()
            .anyMatch(e -> e.parent() == root && nameOf(e.instance()).equals("Var")));
    assertTrue(
        recorder.objectEvents.stream()
            .anyMatch(e -> e.parent() == root && nameOf(e.instance()).equals("Obj")));
    assertTrue(
        recorder.methodEvents.stream()
            .anyMatch(e -> e.parent() == root && nameOf(e.instance()).equals("Method")));
    assertTrue(
        recorder.variableEvents.stream()
            .anyMatch(e -> e.parent() == root && nameOf(e.instance()).equals("OptVar")));

    // Contract pin (the D7 mismatch, as shipped): includeOptionalNode received the *member's*
    // type definition id, not the id of the type being instantiated.
    assertEquals(1, recorder.includeCalls.size(), "one optional member consulted once");
    RecordingCallback.IncludeCall call = recorder.includeCalls.get(0);
    assertEquals(new QualifiedName(1, "OptVar"), call.browseName());
    assertEquals(NodeIds.BaseDataVariableType, call.typeDefinitionId());
  }

  // endregion

  // region D1: excluded optional orphans its mandatory descendants

  /**
   * D1, pinned as shipped: excluding an Optional member prevents only that node's creation. Its
   * Mandatory descendants are still created and stored — unreachable, with no hierarchical
   * reference connecting them to anything — and their callbacks fire with {@code parent == null},
   * indistinguishable from the root signal.
   */
  @Test
  public void d1ExcludedOptionalStillCreatesMandatoryDescendants() throws Exception {
    UaObjectTypeNode typeNode = addObjectType("D1Type", NodeIds.BaseObjectType);
    UaObjectNode optObj =
        addObjectDeclaration(
            typeNode, "OptObj", NodeIds.BaseObjectType, NodeIds.ModellingRule_Optional);
    addVariableDeclaration(
        optObj, "M", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);

    var recorder = new RecordingCallback(false); // exclude all optionals

    NodeId rootNodeId = new NodeId(1, testPrefix + ":D1Root");
    UaNode root = nodeFactory.createNode(rootNodeId, typeNode.getNodeId(), recorder);

    NodeId optObjId = new NodeId(1, testPrefix + ":D1Root/1:OptObj");
    NodeId orphanId = new NodeId(1, testPrefix + ":D1Root/1:OptObj/1:M");

    assertFalse(nodeManager.containsNode(optObjId), "excluded optional itself is not created");
    assertTrue(
        nodeManager.containsNode(orphanId),
        "the excluded optional's mandatory descendant IS created and stored (D1)");

    // The orphan has no hierarchical reference linking it to a parent: only its
    // HasTypeDefinition reference was wired (HasModellingRule is skipped, and the
    // HasComponent row's source — the excluded optional — was never created).
    List<Reference> orphanReferences = nodeManager.getReferences(orphanId);
    assertTrue(
        orphanReferences.stream()
            .anyMatch(
                r -> r.isForward() && NodeIds.HasTypeDefinition.equals(r.getReferenceTypeId())));
    assertTrue(
        orphanReferences.stream()
            .noneMatch(r -> NodeIds.HasComponent.equals(r.getReferenceTypeId())),
        "orphan is unreachable: no hierarchical reference at all");

    // Its callback fired with parent == null — the documented "this is the root" signal.
    assertTrue(
        recorder.variableEvents.stream()
            .anyMatch(e -> e.parent() == null && e.instance().getNodeId().equals(orphanId)),
        "orphan callback fired with parent == null");

    // And the actual root also fired with parent == null; the two are indistinguishable.
    assertTrue(
        recorder.objectEvents.stream().anyMatch(e -> e.parent() == null && e.instance() == root));
  }

  // endregion

  // region D2: nested member types are only shallowly expanded (A1 evidence)

  /**
   * D2, pinned as shipped (A1 runtime evidence): a member whose type definition is a
   * <em>subtype</em> is expanded without the subtype's supertype-declared members — the nested
   * type-definition recursion does not build the member type's fully-inherited hierarchy. A member
   * typed directly as the declaring supertype gets the member (control case).
   */
  @Test
  public void d2NestedMemberTypeMissingSupertypeMembers() throws Exception {
    // BaseSMType declares a Mandatory CurrentState; DerivedSMType subtypes it, adding nothing.
    UaObjectTypeNode baseSmType = addObjectType("BaseSMType", NodeIds.BaseObjectType);
    addVariableDeclaration(
        baseSmType, "CurrentState", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);
    UaObjectTypeNode derivedSmType = addObjectType("DerivedSMType", baseSmType.getNodeId());

    UaObjectTypeNode typeNode = addObjectType("D2Type", NodeIds.BaseObjectType);
    addObjectDeclaration(
        typeNode, "SM", derivedSmType.getNodeId(), NodeIds.ModellingRule_Mandatory);
    addObjectDeclaration(typeNode, "SM2", baseSmType.getNodeId(), NodeIds.ModellingRule_Mandatory);

    NodeId rootNodeId = new NodeId(1, testPrefix + ":D2Root");
    nodeFactory.createNode(rootNodeId, typeNode.getNodeId());

    assertTrue(nodeManager.containsNode(new NodeId(1, testPrefix + ":D2Root/1:SM")));
    assertFalse(
        nodeManager.containsNode(new NodeId(1, testPrefix + ":D2Root/1:SM/1:CurrentState")),
        "member typed as a subtype is MISSING its supertype-declared mandatory member (D2)");

    assertTrue(
        nodeManager.containsNode(new NodeId(1, testPrefix + ":D2Root/1:SM2/1:CurrentState")),
        "control: member typed directly as the declaring type gets the member");
  }

  // endregion

  // region D5: type-declared entry wins over on-declaration override (A1 evidence)

  /**
   * D5, pinned as shipped (A1 runtime evidence): for a member with both an on-declaration child and
   * a same-named child declared by the member's type definition, the <em>type's</em> entry
   * overwrites the on-declaration override (backwards relative to Part 3 §6.3.3.3), and the
   * duplicate hierarchical rows produced by the two walks are both wired onto the instance.
   */
  @Test
  public void d5TypeDeclaredChildShadowsOnDeclarationOverride() throws Exception {
    UaObjectTypeNode memberType = addObjectType("D5MemberType", NodeIds.BaseObjectType);
    UaVariableNode typeChild =
        addVariableDeclaration(
            memberType, "Child", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);
    typeChild.setDataType(NodeIds.String);
    typeChild.setValue(new DataValue(new Variant("fromType")));

    UaObjectTypeNode typeNode = addObjectType("D5Type", NodeIds.BaseObjectType);
    UaObjectNode m =
        addObjectDeclaration(
            typeNode, "M", memberType.getNodeId(), NodeIds.ModellingRule_Mandatory);
    UaVariableNode declChild =
        addVariableDeclaration(
            m, "Child", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);
    declChild.setDataType(NodeIds.Int32);
    declChild.setValue(new DataValue(new Variant(42)));

    NodeId rootNodeId = new NodeId(1, testPrefix + ":D5Root");
    nodeFactory.createNode(rootNodeId, typeNode.getNodeId());

    NodeId childInstanceId = new NodeId(1, testPrefix + ":D5Root/1:M/1:Child");
    UaVariableNode childInstance =
        (UaVariableNode) nodeManager.getNode(childInstanceId).orElseThrow();

    assertEquals(
        NodeIds.String,
        childInstance.getDataType(),
        "the type-declared child won; the on-declaration override was silently discarded (D5)");
    assertEquals(new Variant("fromType"), childInstance.getValue().getValue());

    // Both walks appended a hierarchical row for the same parent/child pair; the factory wires
    // both, so the member instance carries the duplicate forward HasComponent reference.
    NodeId memberInstanceId = new NodeId(1, testPrefix + ":D5Root/1:M");
    long duplicateRows =
        nodeManager.getReferences(memberInstanceId).stream()
            .filter(
                r ->
                    r.isForward()
                        && NodeIds.HasComponent.equals(r.getReferenceTypeId())
                        && r.getTargetNodeId().equals(childInstanceId.expanded()))
            .count();
    assertEquals(2, duplicateRows, "duplicate hierarchical rows are wired verbatim (D5)");
  }

  // endregion

  // region D6: Optional Methods are created unconditionally

  /**
   * D6, pinned as shipped: the optional-exclusion gate exists only for Object and Variable members.
   * An Optional Method member is created unconditionally and {@code includeOptionalNode} is never
   * consulted for it; an Optional Variable member alongside it is consulted and excluded. Method
   * instances copy the declaration's attributes.
   */
  @Test
  public void d6OptionalMethodsCreatedUnconditionally() throws Exception {
    UaObjectTypeNode typeNode = addObjectType("D6Type", NodeIds.BaseObjectType);
    UaMethodNode methodDecl =
        addMethodDeclaration(typeNode, "OptMethod", NodeIds.ModellingRule_Optional);
    addVariableDeclaration(
        typeNode, "OptVar", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Optional);

    var recorder = new RecordingCallback(false); // exclude all optionals

    NodeId rootNodeId = new NodeId(1, testPrefix + ":D6Root");
    nodeFactory.createNode(rootNodeId, typeNode.getNodeId(), recorder);

    NodeId methodInstanceId = new NodeId(1, testPrefix + ":D6Root/1:OptMethod");
    assertTrue(
        nodeManager.containsNode(methodInstanceId),
        "Optional Method created despite the exclude-all callback (D6)");
    assertFalse(
        nodeManager.containsNode(new NodeId(1, testPrefix + ":D6Root/1:OptVar")),
        "Optional Variable was excluded as requested");

    assertTrue(
        recorder.includeCalls.stream()
            .noneMatch(c -> c.browseName().equals(new QualifiedName(1, "OptMethod"))),
        "includeOptionalNode never consulted for the Optional Method (D6)");
    assertTrue(
        recorder.includeCalls.stream()
            .anyMatch(c -> c.browseName().equals(new QualifiedName(1, "OptVar"))),
        "includeOptionalNode consulted for the Optional Variable");

    UaMethodNode methodInstance =
        (UaMethodNode) nodeManager.getNode(methodInstanceId).orElseThrow();
    assertEquals(methodDecl.getBrowseName(), methodInstance.getBrowseName());
    assertEquals(methodDecl.isExecutable(), methodInstance.isExecutable());
    assertEquals(methodDecl.isUserExecutable(), methodInstance.isUserExecutable());
  }

  // endregion

  // region Silent collision replacement

  /**
   * Pinned as shipped: instantiating with a root NodeId that already exists silently replaces the
   * existing node in the NodeManager's node map, and the previous node's reference-map state is
   * <em>not</em> cleared — stale references remain recorded under the replaced NodeId.
   *
   * <p>The pre-existing node here deliberately carries no {@code HasTypeDefinition} reference; if
   * it did, the stale row would derail the notification phase (see the companion NPE test below).
   */
  @Test
  public void silentCollisionReplacement() throws Exception {
    UaObjectTypeNode typeNode = addObjectType("CollisionType", NodeIds.BaseObjectType);
    addVariableDeclaration(
        typeNode, "Foo", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);

    NodeId collidingId = new NodeId(1, testPrefix + ":CollisionRoot");

    UaVariableNode existing =
        new UaVariableNode(
            context,
            collidingId,
            new QualifiedName(1, "Existing"),
            LocalizedText.english("Existing"),
            LocalizedText.NULL_VALUE,
            UInteger.MIN,
            UInteger.MIN);
    nodeManager.addNode(existing);

    Reference staleReference =
        new Reference(collidingId, NodeIds.Organizes, NodeIds.ObjectsFolder.expanded(), false);
    existing.addReference(staleReference);

    UaNode replacement = nodeFactory.createNode(collidingId, typeNode.getNodeId());

    assertSame(
        replacement,
        nodeManager.getNode(collidingId).orElseThrow(),
        "existing node silently replaced");
    assertNotSame(existing, nodeManager.getNode(collidingId).orElseThrow());

    assertTrue(
        nodeManager.getReferences(collidingId).contains(staleReference),
        "previous node's reference-map state is not cleared");
  }

  /**
   * The D13 latent NPE, pinned as shipped: because reference-map state under a replaced NodeId is
   * not cleared, a pre-existing node's stale forward {@code HasTypeDefinition} reference (here to a
   * VariableType) is found first by {@code getTypeDefinitionNode()} during the notification phase,
   * which then throws {@link NullPointerException} — after every node of the tree has already been
   * created and stored, with no cleanup.
   */
  @Test
  public void collisionWithStaleTypeDefinitionThrowsNpeDuringNotification() {
    UaObjectTypeNode typeNode = addObjectType("NpeCollisionType", NodeIds.BaseObjectType);
    addVariableDeclaration(
        typeNode, "Foo", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);

    NodeId collidingId = new NodeId(1, testPrefix + ":NpeCollisionRoot");

    // The builder adds a default HasTypeDefinition -> BaseDataVariableType reference, which
    // becomes the stale row that getTypeDefinitionNode() resolves first.
    UaVariableNode existing =
        UaVariableNode.build(
            context,
            b ->
                b.setNodeId(collidingId)
                    .setBrowseName(new QualifiedName(1, "Existing"))
                    .setDisplayName(LocalizedText.english("Existing"))
                    .build());
    nodeManager.addNode(existing);

    assertThrows(
        NullPointerException.class,
        () -> nodeFactory.createNode(collidingId, typeNode.getNodeId()));

    // The replacement and its child were already stored when the notification phase blew up.
    assertNotSame(existing, nodeManager.getNode(collidingId).orElseThrow());
    assertTrue(
        nodeManager.containsNode(new NodeId(1, testPrefix + ":NpeCollisionRoot/1:Foo")),
        "nodes created before the notification-phase NPE are left behind");
  }

  // endregion

  // region Attribute propagation

  /**
   * The exact Variable attribute propagation set, as shipped: instances copy
   * Value/DataType/ValueRank/ArrayDimensions/AccessLevel/UserAccessLevel from the declaration;
   * MinimumSamplingInterval, Historizing, and AccessLevelEx are never copied (defaults apply);
   * RolePermissions/UserRolePermissions/AccessRestrictions come from the member's <em>type
   * node</em>, never from the declaration; and the instance stores the declaration's {@link
   * DataValue} verbatim — same Variant and source timestamp, no fresh source timestamp (the value
   * getter re-stamps serverTime on a per-read copy).
   */
  @Test
  public void attributePropagationVariables() throws Exception {
    NodeId customVarTypeId = newNodeId("AttrVarType");
    UaVariableTypeNode customVarType =
        new UaVariableTypeNode(
            context,
            customVarTypeId,
            new QualifiedName(1, "AttrVarType"),
            LocalizedText.english("AttrVarType"),
            LocalizedText.NULL_VALUE,
            UInteger.MIN,
            UInteger.MIN,
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            new DataValue(new Variant("typeValue")),
            NodeIds.String,
            -1,
            null,
            false);
    customVarType.addReference(
        new Reference(
            customVarTypeId, NodeIds.HasSubtype, NodeIds.BaseDataVariableType.expanded(), false));
    nodeManager.addNode(customVarType);

    UaObjectTypeNode typeNode = addObjectType("AttrType", NodeIds.BaseObjectType);

    DataValue declarationValue = new DataValue(new Variant(42));
    RolePermissionType[] declarationRolePermissions = {
      new RolePermissionType(NodeIds.WellKnownRole_Anonymous, new PermissionType(uint(1)))
    };

    NodeId declId = newNodeId("AttrType.V");
    UaVariableNode declaration =
        new UaVariableNode(
            context,
            declId,
            new QualifiedName(1, "V"),
            LocalizedText.english("V"),
            LocalizedText.NULL_VALUE,
            UInteger.MIN,
            UInteger.MIN,
            declarationRolePermissions,
            declarationRolePermissions,
            new AccessRestrictionType(UShort.valueOf(2)),
            declarationValue,
            NodeIds.Int32,
            -1,
            null,
            AccessLevel.toValue(AccessLevel.READ_WRITE),
            AccessLevel.toValue(AccessLevel.READ_WRITE),
            100.0,
            true,
            null);
    declaration.addReference(
        new Reference(declId, NodeIds.HasTypeDefinition, customVarTypeId.expanded(), true));
    declaration.addReference(
        new Reference(
            declId, NodeIds.HasModellingRule, NodeIds.ModellingRule_Mandatory.expanded(), true));
    nodeManager.addNode(declaration);
    typeNode.addReference(
        new Reference(typeNode.getNodeId(), NodeIds.HasComponent, declId.expanded(), true));

    NodeId rootNodeId = new NodeId(1, testPrefix + ":AttrRoot");
    nodeFactory.createNode(rootNodeId, typeNode.getNodeId());

    UaVariableNode instance =
        (UaVariableNode)
            nodeManager.getNode(new NodeId(1, testPrefix + ":AttrRoot/1:V")).orElseThrow();

    // Copied from the declaration. The stored DataValue is the declaration's object verbatim —
    // same Variant, StatusCode, and source timestamp objects, no fresh source timestamp — but
    // UaVariableNode.getAttribute(Value) returns a copy with serverTime re-stamped on every
    // read, so verbatim sharing is observed on the copy's constituents rather than assertSame.
    DataValue instanceValue = instance.getValue();
    assertSame(declarationValue.getValue(), instanceValue.getValue());
    assertSame(declarationValue.getStatusCode(), instanceValue.getStatusCode());
    assertSame(
        declarationValue.getSourceTime(),
        instanceValue.getSourceTime(),
        "instance shares the declaration's DataValue verbatim (no fresh source timestamp)");
    assertEquals(NodeIds.Int32, instance.getDataType());
    assertEquals(-1, (int) instance.getValueRank());
    assertNull(instance.getArrayDimensions());
    assertEquals(AccessLevel.toValue(AccessLevel.READ_WRITE), instance.getAccessLevel());
    assertEquals(AccessLevel.toValue(AccessLevel.READ_WRITE), instance.getUserAccessLevel());
    assertEquals(declaration.getBrowseName(), instance.getBrowseName());
    assertEquals(declaration.getDisplayName(), instance.getDisplayName());

    // Never copied from the declaration (defaults apply):
    assertEquals(
        -1.0, (double) instance.getMinimumSamplingInterval(), "MinimumSamplingInterval not copied");
    assertFalse(instance.getHistorizing(), "Historizing not copied");
    assertNull(instance.getAccessLevelEx(), "AccessLevelEx not copied");

    // Security attributes come from the member's TYPE node, not the declaration:
    assertEquals(
        new AccessRestrictionType(UShort.valueOf(1)),
        instance.getAccessRestrictions(),
        "AccessRestrictions taken from the type node, not the declaration");
    assertNull(instance.getRolePermissions(), "RolePermissions taken from the (null) type node");
    assertNull(instance.getUserRolePermissions());
  }

  /**
   * Object instances copy EventNotifier from the declaration; a root instance keeps the
   * <em>type's</em> BrowseName and DisplayName (the universal post-hoc rename burden, pinned
   * as-is); a Variable root takes the type node's Value verbatim (same {@link DataValue} object).
   */
  @Test
  public void attributePropagationObjectsAndRoot() throws Exception {
    UaObjectTypeNode typeNode = addObjectType("RootAttrType", NodeIds.BaseObjectType);
    UaObjectNode objDecl =
        addObjectDeclaration(
            typeNode, "Obj", NodeIds.BaseObjectType, NodeIds.ModellingRule_Mandatory);
    objDecl.setEventNotifier(ubyte(1));

    NodeId rootNodeId = new NodeId(1, testPrefix + ":RootAttrRoot");
    UaNode root = nodeFactory.createNode(rootNodeId, typeNode.getNodeId());

    // Root keeps the type's names.
    assertEquals(typeNode.getBrowseName(), root.getBrowseName());
    assertEquals(typeNode.getDisplayName(), root.getDisplayName());

    // Object child copies EventNotifier from the declaration.
    UaObjectNode objInstance =
        (UaObjectNode)
            nodeManager.getNode(new NodeId(1, testPrefix + ":RootAttrRoot/1:Obj")).orElseThrow();
    assertEquals(ubyte(1), objInstance.getEventNotifier());

    // A Variable root takes the type's Value verbatim.
    DataValue typeValue = new DataValue(new Variant("rootTypeValue"));
    NodeId varTypeId = newNodeId("RootVarType");
    UaVariableTypeNode varType =
        new UaVariableTypeNode(
            context,
            varTypeId,
            new QualifiedName(1, "RootVarType"),
            LocalizedText.english("RootVarType"),
            LocalizedText.NULL_VALUE,
            UInteger.MIN,
            UInteger.MIN,
            typeValue,
            NodeIds.String,
            -1,
            null,
            false);
    varType.addReference(
        new Reference(
            varTypeId, NodeIds.HasSubtype, NodeIds.BaseDataVariableType.expanded(), false));
    nodeManager.addNode(varType);

    UaVariableNode varRoot =
        (UaVariableNode) nodeFactory.createNode(new NodeId(1, testPrefix + ":VarRoot"), varTypeId);

    // Stored verbatim; getValue() returns a copy with serverTime re-stamped on each read.
    DataValue varRootValue = varRoot.getValue();
    assertSame(typeValue.getValue(), varRootValue.getValue());
    assertSame(
        typeValue.getSourceTime(),
        varRootValue.getSourceTime(),
        "variable root takes the type's Value verbatim");
    assertEquals(varType.getBrowseName(), varRoot.getBrowseName());
  }

  // endregion

  // region Reference wiring

  /**
   * Pinned as shipped: {@code HasModellingRule} references are excluded from instances; every other
   * declaration reference row is wired verbatim — absolute targets as-is, so non-hierarchical
   * references between declarations point at the <em>type's</em> declaration nodes (the D4
   * behavior), and inverse rows are re-emitted as forward references (direction is not preserved).
   */
  @Test
  public void modellingRuleExcludedOtherReferencesWiredVerbatim() throws Exception {
    UaObjectTypeNode typeNode = addObjectType("RefType", NodeIds.BaseObjectType);
    UaObjectNode fooDecl =
        addObjectDeclaration(
            typeNode, "Foo", NodeIds.BaseObjectType, NodeIds.ModellingRule_Mandatory);
    UaVariableNode barDecl =
        addVariableDeclaration(
            typeNode, "Bar", NodeIds.BaseDataVariableType, NodeIds.ModellingRule_Mandatory);

    // A non-hierarchical reference between the two declarations. addReference also records the
    // auto-added inverse on Bar.
    fooDecl.addReference(
        new Reference(fooDecl.getNodeId(), NodeIds.HasCause, barDecl.getNodeId().expanded(), true));

    NodeId rootNodeId = new NodeId(1, testPrefix + ":RefRoot");
    nodeFactory.createNode(rootNodeId, typeNode.getNodeId());

    NodeId fooInstanceId = new NodeId(1, testPrefix + ":RefRoot/1:Foo");
    NodeId barInstanceId = new NodeId(1, testPrefix + ":RefRoot/1:Bar");

    // No instance carries a HasModellingRule reference.
    for (NodeId id : List.of(rootNodeId, fooInstanceId, barInstanceId)) {
      assertTrue(
          nodeManager.getReferences(id).stream()
              .noneMatch(r -> NodeIds.HasModellingRule.equals(r.getReferenceTypeId())),
          "HasModellingRule excluded from instance " + id);
    }

    // Foo instance points at the *declaration* Bar (absolute target verbatim), not at the
    // sibling instance.
    List<Reference> fooReferences = nodeManager.getReferences(fooInstanceId);
    assertTrue(
        fooReferences.stream()
            .anyMatch(
                r ->
                    r.isForward()
                        && NodeIds.HasCause.equals(r.getReferenceTypeId())
                        && r.getTargetNodeId().equals(barDecl.getNodeId().expanded())),
        "non-hierarchical reference wired verbatim to the type's declaration node (D4)");
    assertTrue(
        fooReferences.stream()
            .noneMatch(
                r ->
                    NodeIds.HasCause.equals(r.getReferenceTypeId())
                        && r.getTargetNodeId().equals(barInstanceId.expanded())),
        "it is NOT re-mapped onto the sibling instance");

    // The auto-added inverse row on the Bar declaration is re-emitted as a *forward* reference
    // on the Bar instance: direction is not preserved.
    assertTrue(
        nodeManager.getReferences(barInstanceId).stream()
            .anyMatch(
                r ->
                    r.isForward()
                        && NodeIds.HasCause.equals(r.getReferenceTypeId())
                        && r.getTargetNodeId().equals(fooDecl.getNodeId().expanded())),
        "inverse declaration row forced forward on the instance");
  }

  // endregion

  // region Fixture helpers

  private NodeId newNodeId(String id) {
    return new NodeId(1, testPrefix + ":" + id);
  }

  private static String nameOf(UaNode node) {
    return node.getBrowseName().name();
  }

  /**
   * Creates an ObjectType node subtyping {@code supertypeId} and adds it to the NodeManager.
   * References are wired single-direction (the inverse is auto-added) to avoid the duplicate-row
   * noise of {@code addComponent}-style helpers, keeping each pin focused.
   */
  private UaObjectTypeNode addObjectType(String name, NodeId supertypeId) {
    UaObjectTypeNode typeNode =
        new UaObjectTypeNode(
            context,
            newNodeId(name),
            new QualifiedName(1, name),
            LocalizedText.english(name),
            LocalizedText.NULL_VALUE,
            UInteger.MIN,
            UInteger.MIN,
            false);

    typeNode.addReference(
        new Reference(typeNode.getNodeId(), NodeIds.HasSubtype, supertypeId.expanded(), false));

    nodeManager.addNode(typeNode);

    return typeNode;
  }

  private UaObjectNode addObjectDeclaration(
      UaNode parent, String name, NodeId typeDefinitionId, NodeId modellingRuleId) {

    NodeId nodeId = newNodeId(parent.getBrowseName().name() + "." + name);

    UaObjectNode declaration =
        new UaObjectNode(
            context,
            nodeId,
            new QualifiedName(1, name),
            LocalizedText.english(name),
            LocalizedText.NULL_VALUE,
            UInteger.MIN,
            UInteger.MIN,
            ubyte(0));

    declaration.addReference(
        new Reference(nodeId, NodeIds.HasTypeDefinition, typeDefinitionId.expanded(), true));
    declaration.addReference(
        new Reference(nodeId, NodeIds.HasModellingRule, modellingRuleId.expanded(), true));

    nodeManager.addNode(declaration);

    parent.addReference(
        new Reference(parent.getNodeId(), NodeIds.HasComponent, nodeId.expanded(), true));

    return declaration;
  }

  private UaVariableNode addVariableDeclaration(
      UaNode parent, String name, NodeId typeDefinitionId, NodeId modellingRuleId) {

    NodeId nodeId = newNodeId(parent.getBrowseName().name() + "." + name);

    UaVariableNode declaration =
        new UaVariableNode(
            context,
            nodeId,
            new QualifiedName(1, name),
            LocalizedText.english(name),
            LocalizedText.NULL_VALUE,
            UInteger.MIN,
            UInteger.MIN);

    declaration.addReference(
        new Reference(nodeId, NodeIds.HasTypeDefinition, typeDefinitionId.expanded(), true));
    declaration.addReference(
        new Reference(nodeId, NodeIds.HasModellingRule, modellingRuleId.expanded(), true));

    nodeManager.addNode(declaration);

    parent.addReference(
        new Reference(parent.getNodeId(), NodeIds.HasComponent, nodeId.expanded(), true));

    return declaration;
  }

  private UaMethodNode addMethodDeclaration(UaNode parent, String name, NodeId modellingRuleId) {

    NodeId nodeId = newNodeId(parent.getBrowseName().name() + "." + name);

    UaMethodNode declaration =
        new UaMethodNode(
            context,
            nodeId,
            new QualifiedName(1, name),
            LocalizedText.english(name),
            LocalizedText.NULL_VALUE,
            UInteger.MIN,
            UInteger.MIN,
            true,
            true);

    declaration.addReference(
        new Reference(nodeId, NodeIds.HasModellingRule, modellingRuleId.expanded(), true));

    nodeManager.addNode(declaration);

    parent.addReference(
        new Reference(parent.getNodeId(), NodeIds.HasComponent, nodeId.expanded(), true));

    return declaration;
  }

  /** Records every callback invocation; makes no ordering assumptions. */
  private static class RecordingCallback implements NodeFactory.InstantiationCallback {

    record IncludeCall(NodeId typeDefinitionId, QualifiedName browseName) {}

    record NodeEvent(@Nullable UaNode parent, UaNode instance) {}

    final List<IncludeCall> includeCalls = new ArrayList<>();
    final List<NodeEvent> methodEvents = new ArrayList<>();
    final List<NodeEvent> objectEvents = new ArrayList<>();
    final List<NodeEvent> variableEvents = new ArrayList<>();

    private final boolean includeOptionals;

    RecordingCallback(boolean includeOptionals) {
      this.includeOptionals = includeOptionals;
    }

    void onAnyAdded(@Nullable UaNode parent, UaNode instance) {}

    @Override
    public boolean includeOptionalNode(NodeId typeDefinitionId, QualifiedName browseName) {
      includeCalls.add(new IncludeCall(typeDefinitionId, browseName));
      return includeOptionals;
    }

    @Override
    public void onMethodAdded(@Nullable UaObjectNode parent, UaMethodNode instance) {
      onAnyAdded(parent, instance);
      methodEvents.add(new NodeEvent(parent, instance));
    }

    @Override
    public void onObjectAdded(
        @Nullable UaNode parent, UaObjectNode instance, NodeId typeDefinitionId) {
      onAnyAdded(parent, instance);
      objectEvents.add(new NodeEvent(parent, instance));
    }

    @Override
    public void onVariableAdded(
        @Nullable UaNode parent, UaVariableNode instance, NodeId typeDefinitionId) {
      onAnyAdded(parent, instance);
      variableEvents.add(new NodeEvent(parent, instance));
    }
  }

  // endregion
}

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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.core.typetree.ReferenceTypeTree;
import org.eclipse.milo.opcua.sdk.server.AddressSpaceManager;
import org.eclipse.milo.opcua.sdk.server.NodeManager;
import org.eclipse.milo.opcua.sdk.server.ObjectTypeManager;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.UaNodeManager;
import org.eclipse.milo.opcua.sdk.server.VariableTypeManager;
import org.eclipse.milo.opcua.sdk.server.namespaces.loader.NodeLoader;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaReferenceTypeNode;
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
import org.jspecify.annotations.Nullable;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

/**
 * Shared synthetic type-graph builder for the node-instantiation test suite. The compiler tests
 * build on it; later suites in this package extend it.
 *
 * <p>Bootstraps the same mocked environment as the legacy {@code NodeFactoryCharacterizationTest}
 * fixture: a mocked {@link OpcUaServer} over a real {@link UaNodeManager} with namespace zero
 * loaded, an {@link AddressSpaceManager} stub delegating to the node manager, and a {@link
 * ReferenceTypeTree} built <em>after</em> ns0 is loaded and outside any {@code when(...)} stubbing
 * call (building it interacts with the same mock). Custom ReferenceTypes added by a test are picked
 * up because {@link #compiler()} rebuilds the tree on every call.
 *
 * <p>Synthetic nodes live in namespace index 1 ({@value #TEST_NAMESPACE_URI}). Declaration helpers
 * wire references single-direction (the inverse is auto-added by {@link
 * UaNode#addReference(Reference)}), mirroring the characterization test helpers.
 */
public class TypeFixtures {

  public static final String TEST_NAMESPACE_URI = "urn:eclipse:milo:test:instantiation";

  public static final int TEST_NAMESPACE_INDEX = 1;

  private final OpcUaServer server;
  private final NamespaceTable namespaceTable;
  private final UaNodeManager nodeManager;
  private final List<UaNodeManager> registeredManagers = new CopyOnWriteArrayList<>();
  private final UaNodeContext context;
  private final ObjectTypeManager objectTypeManager;
  private final VariableTypeManager variableTypeManager;
  private final TypeModelCache typeModelCache;

  private TypeFixtures() throws Exception {
    server = Mockito.mock(OpcUaServer.class);

    namespaceTable = new NamespaceTable();
    namespaceTable.add(TEST_NAMESPACE_URI);
    Mockito.when(server.getNamespaceTable()).thenReturn(namespaceTable);

    nodeManager = new UaNodeManager();
    registeredManagers.add(nodeManager);

    AddressSpaceManager addressSpaceManager = Mockito.mock(AddressSpaceManager.class);

    Mockito.when(addressSpaceManager.getManagedNode(Mockito.any(NodeId.class)))
        .then(
            (Answer<Optional<UaNode>>)
                invocation ->
                    registeredManagers.stream()
                        .flatMap(m -> m.getNode(invocation.<NodeId>getArgument(0)).stream())
                        .findFirst());

    Mockito.when(addressSpaceManager.getManagedNode(Mockito.any(ExpandedNodeId.class)))
        .then(
            (Answer<Optional<UaNode>>)
                invocation ->
                    registeredManagers.stream()
                        .flatMap(
                            m ->
                                m
                                    .getNode(
                                        invocation.<ExpandedNodeId>getArgument(0), namespaceTable)
                                    .stream())
                        .findFirst());

    Mockito.when(addressSpaceManager.getManagedReferences(Mockito.any(NodeId.class)))
        .then(
            (Answer<List<Reference>>)
                invocation ->
                    registeredManagers.stream()
                        .flatMap(m -> m.getReferences(invocation.<NodeId>getArgument(0)).stream())
                        .toList());

    Mockito.when(addressSpaceManager.getManagedReferences(Mockito.any(NodeId.class), Mockito.any()))
        .then(
            (Answer<List<Reference>>)
                invocation ->
                    registeredManagers.stream()
                        .flatMap(
                            m ->
                                m
                                    .getReferences(
                                        invocation.<NodeId>getArgument(0),
                                        invocation.getArgument(1))
                                    .stream())
                        .toList());

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

    objectTypeManager = new ObjectTypeManager();
    Mockito.when(server.getObjectTypeManager()).thenReturn(objectTypeManager);

    variableTypeManager = new VariableTypeManager();
    Mockito.when(server.getVariableTypeManager()).thenReturn(variableTypeManager);

    // The cache's supplier goes through compiler() so each compilation sees a ReferenceType tree
    // rebuilt from current state (custom ReferenceTypes added by tests included).
    typeModelCache = new TypeModelCache(this::compiler);
    Mockito.when(server.getTypeModelCache()).thenReturn(typeModelCache);

    new NodeLoader(context, nodeManager).loadNodes();

    rebuildReferenceTypeTree();
  }

  /**
   * @return a new fixture over a fresh node manager with namespace zero loaded.
   */
  public static TypeFixtures create() {
    try {
      return new TypeFixtures();
    } catch (Exception e) {
      throw new RuntimeException("fixture bootstrap failed", e);
    }
  }

  /**
   * @return the mocked server backing this fixture.
   */
  public OpcUaServer server() {
    return server;
  }

  /**
   * @return the node manager holding ns0 and all synthetic nodes.
   */
  public UaNodeManager nodeManager() {
    return nodeManager;
  }

  /**
   * @return the fixture's namespace table (ns0 + the test namespace at index 1).
   */
  public NamespaceTable namespaceTable() {
    return namespaceTable;
  }

  /**
   * @return the node context synthetic nodes are constructed with.
   */
  public UaNodeContext context() {
    return context;
  }

  /**
   * @return the real {@link ObjectTypeManager} wired into the mocked server.
   */
  public ObjectTypeManager objectTypeManager() {
    return objectTypeManager;
  }

  /**
   * @return the real {@link VariableTypeManager} wired into the mocked server.
   */
  public VariableTypeManager variableTypeManager() {
    return variableTypeManager;
  }

  /**
   * @return the real {@link TypeModelCache} wired into the mocked server.
   */
  public TypeModelCache typeModelCache() {
    return typeModelCache;
  }

  /**
   * @return a {@link NodeInstantiator} over this fixture's mocked server. The ReferenceType tree is
   *     rebuilt first so types added by the test are classified correctly.
   */
  public NodeInstantiator instantiator() {
    rebuildReferenceTypeTree();
    return new NodeInstantiator(server);
  }

  /**
   * @return a fresh, empty destination {@link UaNodeManager}, distinct from the source manager
   *     holding ns0 and the synthetic types.
   */
  public UaNodeManager newTargetManager() {
    return new UaNodeManager();
  }

  /**
   * Make {@code manager} visible through the mocked {@link AddressSpaceManager}, mirroring a real
   * server registering a destination manager: node-level navigation (typed getters, {@code
   * getPropertyNode}, {@code getReferences()}) resolves through the address space, so committed
   * instances are only navigable once their manager is registered.
   *
   * @param manager the manager to register.
   */
  public void registerWithAddressSpace(UaNodeManager manager) {
    registeredManagers.add(manager);
  }

  /**
   * @return a fresh compiler over this fixture's address space. The ReferenceType tree is rebuilt
   *     first so custom ReferenceTypes added by the test are classified correctly.
   */
  public TypeModelCompiler compiler() {
    rebuildReferenceTypeTree();
    return new TypeModelCompiler(server);
  }

  /** Rebuild the ReferenceType tree from current state and restub the server mock with it. */
  public void rebuildReferenceTypeTree() {
    // Built outside the when() call: building interacts with the same mock.
    ReferenceTypeTree referenceTypeTree = ReferenceTypeTreeBuilder.build(server);
    Mockito.when(server.getReferenceTypeTree()).thenReturn(referenceTypeTree);
  }

  /**
   * @param name the identifier text.
   * @return a String NodeId in the test namespace.
   */
  public NodeId newNodeId(String name) {
    return new NodeId(TEST_NAMESPACE_INDEX, name);
  }

  /**
   * @param name the browse name text.
   * @return a {@link QualifiedName} in the test namespace.
   */
  public static QualifiedName qn(String name) {
    return new QualifiedName(TEST_NAMESPACE_INDEX, name);
  }

  /**
   * @param names browse name texts, outermost first, all in the test namespace.
   * @return the {@link BrowsePath} of those names.
   */
  public static BrowsePath path(String... names) {
    return BrowsePath.of(Arrays.stream(names).map(TypeFixtures::qn).toList());
  }

  /**
   * Create an ObjectType node subtyping {@code supertypeId} and add it to the node manager. The
   * supertype id need not exist yet (cycle fixtures rely on that).
   */
  public UaObjectTypeNode addObjectType(String name, NodeId supertypeId) {
    return addObjectType(name, supertypeId, false);
  }

  /** Create an ObjectType node, optionally abstract. */
  public UaObjectTypeNode addObjectType(String name, NodeId supertypeId, boolean isAbstract) {
    UaObjectTypeNode typeNode =
        new UaObjectTypeNode(
            context,
            newNodeId(name),
            qn(name),
            LocalizedText.english(name),
            LocalizedText.NULL_VALUE,
            UInteger.MIN,
            UInteger.MIN,
            isAbstract);

    typeNode.addReference(
        new Reference(typeNode.getNodeId(), NodeIds.HasSubtype, supertypeId.expanded(), false));

    nodeManager.addNode(typeNode);

    return typeNode;
  }

  /** Create a VariableType node subtyping {@code supertypeId} and add it to the node manager. */
  public UaVariableTypeNode addVariableType(
      String name, NodeId supertypeId, @Nullable DataValue value, NodeId dataType, int valueRank) {

    UaVariableTypeNode typeNode =
        new UaVariableTypeNode(
            context,
            newNodeId(name),
            qn(name),
            LocalizedText.english(name),
            LocalizedText.NULL_VALUE,
            UInteger.MIN,
            UInteger.MIN,
            value,
            dataType,
            valueRank,
            null,
            false);

    typeNode.addReference(
        new Reference(typeNode.getNodeId(), NodeIds.HasSubtype, supertypeId.expanded(), false));

    nodeManager.addNode(typeNode);

    return typeNode;
  }

  /** Create an interface type (an ObjectType subtyping BaseInterfaceType). */
  public UaObjectTypeNode addInterfaceType(String name) {
    return addObjectType(name, NodeIds.BaseInterfaceType, true);
  }

  /** Create a concrete ReferenceType subtyping {@code supertypeId}. */
  public UaReferenceTypeNode addReferenceType(String name, NodeId supertypeId) {
    UaReferenceTypeNode referenceTypeNode =
        new UaReferenceTypeNode(
            context,
            newNodeId(name),
            qn(name),
            LocalizedText.english(name),
            LocalizedText.NULL_VALUE,
            UInteger.MIN,
            UInteger.MIN,
            false,
            false,
            LocalizedText.english("InverseOf" + name));

    referenceTypeNode.addReference(
        new Reference(
            referenceTypeNode.getNodeId(), NodeIds.HasSubtype, supertypeId.expanded(), false));

    nodeManager.addNode(referenceTypeNode);

    return referenceTypeNode;
  }

  /**
   * Give {@code typeNode} a rule-less {@code DefaultInstanceBrowseName} property (ns0 name, as the
   * spec defines it) carrying {@code defaultName}.
   */
  public UaVariableNode setDefaultInstanceBrowseName(UaNode typeNode, QualifiedName defaultName) {
    NodeId nodeId = newNodeId(typeNode.getBrowseName().name() + ".DefaultInstanceBrowseName");

    UaVariableNode property =
        new UaVariableNode(
            context,
            nodeId,
            new QualifiedName(0, "DefaultInstanceBrowseName"),
            LocalizedText.english("DefaultInstanceBrowseName"),
            LocalizedText.NULL_VALUE,
            UInteger.MIN,
            UInteger.MIN);
    property.setValue(new DataValue(new Variant(defaultName)));
    property.setDataType(NodeIds.QualifiedName);
    property.addReference(
        new Reference(nodeId, NodeIds.HasTypeDefinition, NodeIds.PropertyType.expanded(), true));

    nodeManager.addNode(property);

    typeNode.addReference(
        new Reference(typeNode.getNodeId(), NodeIds.HasProperty, nodeId.expanded(), true));

    return property;
  }

  /** Add an Object declaration under {@code parent} via forward HasComponent. */
  public UaObjectNode addObjectDeclaration(
      UaNode parent, String name, NodeId typeDefinitionId, NodeId modellingRuleId) {
    return addObjectDeclaration(
        parent, name, typeDefinitionId, modellingRuleId, NodeIds.HasComponent);
  }

  /** Add an Object declaration under {@code parent} via the given hierarchical ReferenceType. */
  public UaObjectNode addObjectDeclaration(
      UaNode parent,
      String name,
      NodeId typeDefinitionId,
      NodeId modellingRuleId,
      NodeId referenceTypeId) {

    NodeId nodeId = newNodeId(parent.getBrowseName().name() + "." + name);

    UaObjectNode declaration =
        new UaObjectNode(
            context,
            nodeId,
            qn(name),
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
        new Reference(parent.getNodeId(), referenceTypeId, nodeId.expanded(), true));

    return declaration;
  }

  /** Add a Variable declaration under {@code parent} via forward HasComponent. */
  public UaVariableNode addVariableDeclaration(
      UaNode parent, String name, NodeId typeDefinitionId, NodeId modellingRuleId) {
    return addVariableDeclaration(
        parent, name, typeDefinitionId, modellingRuleId, NodeIds.HasComponent);
  }

  /** Add a Variable declaration under {@code parent} via the given hierarchical ReferenceType. */
  public UaVariableNode addVariableDeclaration(
      UaNode parent,
      String name,
      NodeId typeDefinitionId,
      NodeId modellingRuleId,
      NodeId referenceTypeId) {

    NodeId nodeId = newNodeId(parent.getBrowseName().name() + "." + name);

    UaVariableNode declaration =
        new UaVariableNode(
            context,
            nodeId,
            qn(name),
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
        new Reference(parent.getNodeId(), referenceTypeId, nodeId.expanded(), true));

    return declaration;
  }

  /**
   * Add a Variable under {@code parent} with a TypeDefinition but <em>no</em> ModellingRule — not
   * an InstanceDeclaration per Part 3 §6.2.2; it belongs to the type node only.
   */
  public UaVariableNode addRuleLessVariable(UaNode parent, String name, NodeId typeDefinitionId) {
    NodeId nodeId = newNodeId(parent.getBrowseName().name() + "." + name);

    UaVariableNode node =
        new UaVariableNode(
            context,
            nodeId,
            qn(name),
            LocalizedText.english(name),
            LocalizedText.NULL_VALUE,
            UInteger.MIN,
            UInteger.MIN);

    node.addReference(
        new Reference(nodeId, NodeIds.HasTypeDefinition, typeDefinitionId.expanded(), true));

    nodeManager.addNode(node);

    parent.addReference(
        new Reference(parent.getNodeId(), NodeIds.HasComponent, nodeId.expanded(), true));

    return node;
  }

  /** Add a Method declaration under {@code parent} via forward HasComponent. */
  public UaMethodNode addMethodDeclaration(UaNode parent, String name, NodeId modellingRuleId) {
    NodeId nodeId = newNodeId(parent.getBrowseName().name() + "." + name);

    UaMethodNode declaration =
        new UaMethodNode(
            context,
            nodeId,
            qn(name),
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

  /**
   * Add an argument property ({@code InputArguments} / {@code OutputArguments}, ns0 names) to a
   * Method declaration, its Value holding the raw {@code Argument[]}.
   */
  public UaVariableNode addArgumentsProperty(
      UaMethodNode method, String propertyName, Object argumentArray) {

    // Derive from the method's NodeId, not its BrowseName: two methods overriding one another
    // share a BrowseName and their properties must not collide on one NodeId.
    NodeId nodeId = newNodeId(method.getNodeId().getIdentifier() + "." + propertyName);

    UaVariableNode property =
        new UaVariableNode(
            context,
            nodeId,
            new QualifiedName(0, propertyName),
            LocalizedText.english(propertyName),
            LocalizedText.NULL_VALUE,
            UInteger.MIN,
            UInteger.MIN);
    property.setValue(new DataValue(new Variant(argumentArray)));
    property.setDataType(NodeIds.Argument);
    property.addReference(
        new Reference(nodeId, NodeIds.HasTypeDefinition, NodeIds.PropertyType.expanded(), true));
    property.addReference(
        new Reference(
            nodeId, NodeIds.HasModellingRule, NodeIds.ModellingRule_Mandatory.expanded(), true));

    nodeManager.addNode(property);

    method.addReference(
        new Reference(method.getNodeId(), NodeIds.HasProperty, nodeId.expanded(), true));

    return property;
  }
}

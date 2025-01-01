/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.nodes;

import static org.eclipse.milo.opcua.sdk.core.Reference.HAS_COMPONENT_PREDICATE;
import static org.eclipse.milo.opcua.sdk.core.Reference.HAS_EVENT_SOURCE_PREDICATE;
import static org.eclipse.milo.opcua.sdk.core.Reference.HAS_NOTIFIER_PREDICATE;
import static org.eclipse.milo.opcua.sdk.core.Reference.HAS_ORDERED_COMPONENT_PREDICATE;
import static org.eclipse.milo.opcua.sdk.core.Reference.HAS_PROPERTY_PREDICATE;
import static org.eclipse.milo.opcua.sdk.core.Reference.HAS_TYPE_DEFINITION_PREDICATE;
import static org.eclipse.milo.opcua.sdk.core.Reference.ORGANIZES_PREDICATE;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.core.nodes.Node;
import org.eclipse.milo.opcua.sdk.core.nodes.ObjectNode;
import org.eclipse.milo.opcua.sdk.core.nodes.ObjectNodeProperties;
import org.eclipse.milo.opcua.sdk.core.nodes.ObjectTypeNode;
import org.eclipse.milo.opcua.sdk.server.AddressSpaceManager;
import org.eclipse.milo.opcua.sdk.server.NodeManager;
import org.eclipse.milo.opcua.sdk.server.nodes.filters.AttributeFilter;
import org.eclipse.milo.opcua.sdk.server.nodes.filters.AttributeFilterChain;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NamingRuleType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.jspecify.annotations.Nullable;

public class UaObjectNode extends UaNode implements ObjectNode {

  private UByte eventNotifier = ubyte(0);

  /** Construct a {@link UaObjectNode} using only attributes defined prior to OPC UA 1.04. */
  public UaObjectNode(
      UaNodeContext context,
      NodeId nodeId,
      QualifiedName browseName,
      LocalizedText displayName,
      LocalizedText description,
      UInteger writeMask,
      UInteger userWriteMask,
      UByte eventNotifier) {

    super(
        context,
        nodeId,
        NodeClass.Object,
        browseName,
        displayName,
        description,
        writeMask,
        userWriteMask);

    this.eventNotifier = eventNotifier;
  }

  /**
   * Construct a {@link UaObjectNode} using all attributes, including those defined by OPC UA 1.04.
   */
  public UaObjectNode(
      UaNodeContext context,
      NodeId nodeId,
      QualifiedName browseName,
      LocalizedText displayName,
      LocalizedText description,
      UInteger writeMask,
      UInteger userWriteMask,
      RolePermissionType[] rolePermissions,
      RolePermissionType[] userRolePermissions,
      AccessRestrictionType accessRestrictions,
      UByte eventNotifier) {

    super(
        context,
        nodeId,
        NodeClass.Object,
        browseName,
        displayName,
        description,
        writeMask,
        userWriteMask,
        rolePermissions,
        userRolePermissions,
        accessRestrictions);

    this.eventNotifier = eventNotifier;
  }

  /**
   * Construct a {@link UaObjectNode} using all attributes in the intersection of attributes defined
   * by Objects and ObjectTypes, making it suitable for use when instantiating an Object instance
   * from an ObjectType.
   *
   * <p>This constructor requires only attributes defined prior to OPC UA 1.04.
   */
  public UaObjectNode(
      UaNodeContext context,
      NodeId nodeId,
      QualifiedName browseName,
      LocalizedText displayName,
      LocalizedText description,
      UInteger writeMask,
      UInteger userWriteMask) {

    super(
        context,
        nodeId,
        NodeClass.Object,
        browseName,
        displayName,
        description,
        writeMask,
        userWriteMask);
  }

  /**
   * Construct a {@link UaObjectNode} using all attributes in the intersection of attributes defined
   * by Objects and ObjectTypes, making it suitable for use when instantiating an Object instance
   * from an ObjectType.
   *
   * <p>This constructor requires attributes defined by OPC UA 1.04.
   */
  public UaObjectNode(
      UaNodeContext context,
      NodeId nodeId,
      QualifiedName browseName,
      LocalizedText displayName,
      LocalizedText description,
      UInteger writeMask,
      UInteger userWriteMask,
      RolePermissionType[] rolePermissions,
      RolePermissionType[] userRolePermissions,
      AccessRestrictionType accessRestrictions) {

    super(
        context,
        nodeId,
        NodeClass.Object,
        browseName,
        displayName,
        description,
        writeMask,
        userWriteMask,
        rolePermissions,
        userRolePermissions,
        accessRestrictions);
  }

  @Override
  public UByte getEventNotifier() {
    return (UByte) filterChain.getAttribute(this, AttributeId.EventNotifier);
  }

  @Override
  public void setEventNotifier(UByte eventNotifier) {
    filterChain.setAttribute(this, AttributeId.EventNotifier, eventNotifier);
  }

  @Override
  public synchronized Object getAttribute(AttributeId attributeId) {
    if (attributeId == AttributeId.EventNotifier) {
      return eventNotifier;
    } else {
      return super.getAttribute(attributeId);
    }
  }

  @Override
  public synchronized void setAttribute(AttributeId attributeId, Object value) {
    if (attributeId == AttributeId.EventNotifier) {
      eventNotifier = (UByte) value;
      fireAttributeChanged(attributeId, value);
    } else {
      super.setAttribute(attributeId, value);
    }
  }

  @Nullable
  public UaMethodNode findMethodNode(NodeId methodId) {
    List<UaMethodNode> methodNodes = getMethodNodes();

    for (UaMethodNode methodNode : methodNodes) {
      if (methodId.equals(methodNode.getNodeId())) {
        return methodNode;
      }

      NodeId typeDefinitionId = getTypeDefinitionNode().getNodeId();

      NodeId methodDeclarationId =
          findMethodDeclarationId(typeDefinitionId, methodNode.getBrowseName());

      if (methodId.equals(methodDeclarationId)) {
        return methodNode;
      }
    }

    return null;
  }

  /**
   * Find the NodeId of the MethodNode on the type definition with the same name as {@code
   * methodName}.
   *
   * @param methodName the name of the MethodNode to search for.
   * @return the NodeId of the MethodNode on the type definition with the same name as {@code
   *     methodName}. Will be {@link NodeId#NULL_VALUE} if not found.
   */
  private NodeId findMethodDeclarationId(NodeId typeDefinitionId, QualifiedName methodName) {
    AddressSpaceManager asm = getNodeContext().getServer().getAddressSpaceManager();

    NamespaceTable namespaceTable = getNodeContext().getServer().getNamespaceTable();

    NodeId nodeId =
        asm.getManagedReferences(typeDefinitionId).stream()
            .filter(HAS_COMPONENT_PREDICATE.or(HAS_ORDERED_COMPONENT_PREDICATE))
            .flatMap(r -> getManagedNode(r.getTargetNodeId()).stream())
            .filter(
                n -> (n instanceof UaMethodNode) && Objects.equals(n.getBrowseName(), methodName))
            .findFirst()
            .map(Node::getNodeId)
            .orElse(NodeId.NULL_VALUE);

    if (nodeId.isNull()) {
      NodeId parentTypeId =
          asm.getManagedReferences(typeDefinitionId).stream()
              .filter(Reference.SUBTYPE_OF)
              .flatMap(r -> r.getTargetNodeId().toNodeId(namespaceTable).stream())
              .findFirst()
              .orElse(null);

      if (parentTypeId != null) {
        return findMethodDeclarationId(parentTypeId, methodName);
      } else {
        return nodeId;
      }
    } else {
      return nodeId;
    }
  }

  public List<UaNode> getComponentNodes() {
    return getReferences().stream()
        .filter(HAS_COMPONENT_PREDICATE)
        .flatMap(r -> getManagedNode(r.getTargetNodeId()).stream())
        .collect(Collectors.toList());
  }

  public List<UaNode> getPropertyNodes() {
    return getReferences().stream()
        .filter(HAS_PROPERTY_PREDICATE)
        .flatMap(r -> getManagedNode(r.getTargetNodeId()).stream())
        .collect(Collectors.toList());
  }

  public List<UaMethodNode> getMethodNodes() {
    return getReferences().stream()
        .filter(HAS_COMPONENT_PREDICATE.or(HAS_ORDERED_COMPONENT_PREDICATE))
        .flatMap(r -> getManagedNode(r.getTargetNodeId()).stream())
        .filter(n -> (n instanceof UaMethodNode))
        .map(UaMethodNode.class::cast)
        .collect(Collectors.toList());
  }

  public ObjectTypeNode getTypeDefinitionNode() {
    Node node =
        getReferences().stream()
            .filter(HAS_TYPE_DEFINITION_PREDICATE)
            .findFirst()
            .flatMap(r -> getManagedNode(r.getTargetNodeId()))
            .orElse(null);

    return (node instanceof ObjectTypeNode) ? (ObjectTypeNode) node : null;
  }

  public List<Node> getEventSourceNodes() {
    return getReferences().stream()
        .filter(HAS_EVENT_SOURCE_PREDICATE)
        .flatMap(r -> getManagedNode(r.getTargetNodeId()).stream())
        .collect(Collectors.toList());
  }

  public List<Node> getNotifierNodes() {
    return getReferences().stream()
        .filter(HAS_NOTIFIER_PREDICATE)
        .flatMap(r -> getManagedNode(r.getTargetNodeId()).stream())
        .collect(Collectors.toList());
  }

  public List<Node> getOrganizesNodes() {
    return getReferences().stream()
        .filter(ORGANIZES_PREDICATE)
        .flatMap(r -> getManagedNode(r.getTargetNodeId()).stream())
        .collect(Collectors.toList());
  }

  /**
   * Add a 'HasComponent' reference from this Object to {@code node} and an inverse 'ComponentOf'
   * reference from {@code node} back to this Object.
   *
   * @param node the node to add as a component of this Object.
   */
  public void addComponent(UaNode node) {
    addReference(
        new Reference(getNodeId(), NodeIds.HasComponent, node.getNodeId().expanded(), true));
  }

  /**
   * Remove the 'HasComponent' reference from this Object to {@code node} and the inverse
   * 'ComponentOf' reference from {@code node} back to this Object.
   *
   * @param node the node to remove as a component of this Object.
   */
  public void removeComponent(UaNode node) {
    removeReference(
        new Reference(getNodeId(), NodeIds.HasComponent, node.getNodeId().expanded(), true));
  }

  /**
   * Get the value of the NodeVersion Property, if it exists.
   *
   * @return the value of the NodeVersion Property, if it exists.
   * @see ObjectNodeProperties#NodeVersion
   */
  @Nullable
  public String getNodeVersion() {
    return getProperty(ObjectNodeProperties.NodeVersion).orElse(null);
  }

  /**
   * Get the value of the Icon Property, if it exists.
   *
   * @return the value of the Icon Property, if it exists.
   * @see ObjectNodeProperties#Icon
   */
  @Nullable
  public ByteString getIcon() {
    return getProperty(ObjectNodeProperties.Icon).orElse(null);
  }

  /**
   * Get the value of the NamingRule Property, if it exists.
   *
   * @return the value of the NamingRule Property, if it exists.
   * @see ObjectNodeProperties#NamingRule
   */
  @Nullable
  public NamingRuleType getNamingRule() {
    return getProperty(ObjectNodeProperties.NamingRule).orElse(null);
  }

  /**
   * Set the value of the NodeVersion Property.
   *
   * <p>A PropertyNode will be created if it does not already exist.
   *
   * @param nodeVersion the value to set.
   * @see ObjectNodeProperties#NodeVersion
   */
  public void setNodeVersion(String nodeVersion) {
    setProperty(ObjectNodeProperties.NodeVersion, nodeVersion);
  }

  /**
   * Set the value of the Icon Property.
   *
   * <p>A PropertyNode will be created if it does not already exist.
   *
   * @param icon the value to set.
   * @see ObjectNodeProperties#Icon
   */
  public void setIcon(ByteString icon) {
    setProperty(ObjectNodeProperties.Icon, icon);
  }

  /**
   * Set the value of the NamingRule Property.
   *
   * <p>A PropertyNode will be created if it does not already exist.
   *
   * @param namingRule the value to set.
   * @see ObjectNodeProperties#NamingRule
   */
  public void setNamingRule(NamingRuleType namingRule) {
    setProperty(ObjectNodeProperties.NamingRule, namingRule);
  }

  /**
   * @deprecated use {@link UaObjectNodeBuilder#UaObjectNodeBuilder(UaNodeContext)} or {@link
   *     #build(UaNodeContext, Function)}.
   */
  @Deprecated
  public static UaObjectNodeBuilder builder(UaNodeContext context) {
    return new UaObjectNodeBuilder(context);
  }

  /**
   * Build a {@link UaObjectNode} using the {@link UaObjectNodeBuilder} supplied to the {@code
   * build} function.
   *
   * @param context a {@link UaNodeContext}.
   * @param build a function that accepts a {@link UaObjectNodeBuilder} and uses it to build and
   *     return a {@link UaObjectNode}.
   * @return a {@link UaObjectNode} built using the supplied {@link UaObjectNodeBuilder}.
   */
  public static UaObjectNode build(
      UaNodeContext context, Function<UaObjectNodeBuilder, UaObjectNode> build) {

    UaObjectNodeBuilder builder = new UaObjectNodeBuilder(context);

    return build.apply(builder);
  }

  public static class UaObjectNodeBuilder implements Supplier<UaObjectNode> {

    private final List<AttributeFilter> attributeFilters = new ArrayList<>();

    private final List<Reference> references = new ArrayList<>();

    private NodeId nodeId;
    private QualifiedName browseName;
    private LocalizedText displayName;
    private LocalizedText description = LocalizedText.NULL_VALUE;
    private UInteger writeMask = UInteger.MIN;
    private UInteger userWriteMask = UInteger.MIN;
    private UByte eventNotifier = ubyte(0);

    private final UaNodeContext context;

    public UaObjectNodeBuilder(UaNodeContext context) {
      this.context = context;
    }

    /**
     * @see #build()
     */
    @Override
    public UaObjectNode get() {
      return build();
    }

    /**
     * Build and return the {@link UaObjectNode}.
     *
     * <p>The following fields are required: NodeId, BrowseName, DisplayName.
     *
     * @return a {@link UaObjectNode}.
     * @throws NullPointerException if any of the required fields are null.
     * @throws IllegalStateException if exactly one HasTypeDefinition reference is not present.
     */
    public UaObjectNode build() {
      Preconditions.checkNotNull(nodeId, "NodeId cannot be null");
      Preconditions.checkNotNull(browseName, "BrowseName cannot be null");
      Preconditions.checkNotNull(displayName, "DisplayName cannot be null");

      long hasTypeDefinitionCount =
          references.stream()
              .filter(r -> NodeIds.HasTypeDefinition.equals(r.getReferenceTypeId()))
              .count();

      if (hasTypeDefinitionCount == 0) {
        setTypeDefinition(NodeIds.BaseObjectType);
      } else {
        Preconditions.checkState(
            hasTypeDefinitionCount == 1,
            "Object Node must have exactly one HasTypeDefinition reference.");
      }

      UaObjectNode node =
          new UaObjectNode(
              context,
              nodeId,
              browseName,
              displayName,
              description,
              writeMask,
              userWriteMask,
              eventNotifier);

      references.forEach(node::addReference);

      node.getFilterChain().addLast(attributeFilters);

      return node;
    }

    /**
     * Build the {@link UaObjectNode} using the configured values and add it to the {@link
     * NodeManager} from the {@link UaNodeContext}.
     *
     * @return a {@link UaObjectNode} built from the configured values.
     * @see #build()
     */
    public UaObjectNode buildAndAdd() {
      UaObjectNode node = build();
      context.getNodeManager().addNode(node);
      return node;
    }

    public UaObjectNodeBuilder setNodeId(NodeId nodeId) {
      this.nodeId = nodeId;
      return this;
    }

    public UaObjectNodeBuilder setBrowseName(QualifiedName browseName) {
      this.browseName = browseName;
      return this;
    }

    public UaObjectNodeBuilder setDisplayName(LocalizedText displayName) {
      this.displayName = displayName;
      return this;
    }

    public UaObjectNodeBuilder setDescription(LocalizedText description) {
      this.description = description;
      return this;
    }

    public UaObjectNodeBuilder setWriteMask(UInteger writeMask) {
      this.writeMask = writeMask;
      return this;
    }

    public UaObjectNodeBuilder setUserWriteMask(UInteger userWriteMask) {
      this.userWriteMask = userWriteMask;
      return this;
    }

    public UaObjectNodeBuilder setEventNotifier(UByte eventNotifier) {
      this.eventNotifier = eventNotifier;
      return this;
    }

    public NodeId getNodeId() {
      return nodeId;
    }

    public QualifiedName getBrowseName() {
      return browseName;
    }

    public LocalizedText getDisplayName() {
      return displayName;
    }

    public LocalizedText getDescription() {
      return description;
    }

    public UInteger getWriteMask() {
      return writeMask;
    }

    public UInteger getUserWriteMask() {
      return userWriteMask;
    }

    public UByte getEventNotifier() {
      return eventNotifier;
    }

    /**
     * Add an {@link AttributeFilter} that will be added to the node's {@link AttributeFilterChain}
     * when it's built.
     *
     * <p>The order filters are added in this builder is maintained.
     *
     * @param attributeFilter the {@link AttributeFilter} to add.
     * @return this {@link UaObjectNodeBuilder}.
     */
    public UaObjectNodeBuilder addAttributeFilter(AttributeFilter attributeFilter) {
      attributeFilters.add(attributeFilter);
      return this;
    }

    /**
     * Add a {@link Reference} to the node when it's built.
     *
     * @param reference the {@link Reference} to add.
     * @return this {@link UaObjectNodeBuilder}.
     */
    public UaObjectNodeBuilder addReference(Reference reference) {
      references.add(reference);
      return this;
    }

    /**
     * Convenience method for adding the required HasTypeDefinition reference.
     *
     * <p>{@link #setNodeId(NodeId)} must have already been called before invoking this method.
     *
     * @param typeDefinition The {@link NodeId} of the TypeDefinition.
     * @return this {@link UaObjectNodeBuilder}.
     */
    public UaObjectNodeBuilder setTypeDefinition(NodeId typeDefinition) {
      Objects.requireNonNull(nodeId, "NodeId cannot be null");

      references.add(
          new Reference(nodeId, NodeIds.HasTypeDefinition, typeDefinition.expanded(), true));

      return this;
    }
  }
}

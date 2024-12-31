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
import static org.eclipse.milo.opcua.sdk.core.Reference.HAS_ORDERED_COMPONENT_PREDICATE;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.core.nodes.ObjectTypeNode;
import org.eclipse.milo.opcua.sdk.core.nodes.ObjectTypeNodeProperties;
import org.eclipse.milo.opcua.sdk.server.NodeManager;
import org.eclipse.milo.opcua.sdk.server.nodes.filters.AttributeFilter;
import org.eclipse.milo.opcua.sdk.server.nodes.filters.AttributeFilterChain;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.jspecify.annotations.Nullable;

public class UaObjectTypeNode extends UaNode implements ObjectTypeNode {

  private Boolean isAbstract;

  /** Construct a {@link UaObjectTypeNode} using only attributes defined prior to OPC UA 1.04. */
  public UaObjectTypeNode(
      UaNodeContext context,
      NodeId nodeId,
      QualifiedName browseName,
      LocalizedText displayName,
      LocalizedText description,
      UInteger writeMask,
      UInteger userWriteMask,
      Boolean isAbstract) {

    super(
        context,
        nodeId,
        NodeClass.ObjectType,
        browseName,
        displayName,
        description,
        writeMask,
        userWriteMask);

    this.isAbstract = isAbstract;
  }

  /**
   * Construct a {@link UaObjectTypeNode} using all attributes, including those defined by OPC UA
   * 1.04.
   */
  public UaObjectTypeNode(
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
      Boolean isAbstract) {

    super(
        context,
        nodeId,
        NodeClass.ObjectType,
        browseName,
        displayName,
        description,
        writeMask,
        userWriteMask,
        rolePermissions,
        userRolePermissions,
        accessRestrictions);

    this.isAbstract = isAbstract;
  }

  @Override
  public Boolean getIsAbstract() {
    return (Boolean) filterChain.getAttribute(this, AttributeId.IsAbstract);
  }

  @Override
  public void setIsAbstract(Boolean isAbstract) {
    filterChain.setAttribute(this, AttributeId.IsAbstract, isAbstract);
  }

  @Override
  public synchronized Object getAttribute(AttributeId attributeId) {
    if (attributeId == AttributeId.IsAbstract) {
      return isAbstract;
    } else {
      return super.getAttribute(attributeId);
    }
  }

  @Override
  public synchronized void setAttribute(AttributeId attributeId, Object value) {
    if (attributeId == AttributeId.IsAbstract) {
      isAbstract = (Boolean) value;
      fireAttributeChanged(attributeId, value);
    } else {
      super.setAttribute(attributeId, value);
    }
  }

  @Nullable
  public UaMethodNode findMethodNode(NodeId methodId) {
    return getReferences().stream()
        .filter(HAS_COMPONENT_PREDICATE.or(HAS_ORDERED_COMPONENT_PREDICATE))
        .flatMap(r -> getManagedNode(r.getTargetNodeId()).stream())
        .filter(n -> (n instanceof UaMethodNode) && Objects.equals(n.getNodeId(), methodId))
        .map(UaMethodNode.class::cast)
        .findFirst()
        .orElse(null);
  }

  public List<UaMethodNode> getMethodNodes() {
    return getReferences().stream()
        .filter(HAS_COMPONENT_PREDICATE.or(HAS_ORDERED_COMPONENT_PREDICATE))
        .flatMap(r -> getManagedNode(r.getTargetNodeId()).stream())
        .filter(n -> (n instanceof UaMethodNode))
        .map(UaMethodNode.class::cast)
        .collect(Collectors.toList());
  }

  /**
   * Add a 'HasComponent' reference from this node to {@code node} and an inverse 'ComponentOf'
   * reference from {@code node} back to this node.
   *
   * @param node the node to add as a component of this node.
   */
  public void addComponent(UaNode node) {
    addReference(
        new Reference(getNodeId(), NodeIds.HasComponent, node.getNodeId().expanded(), true));

    node.addReference(
        new Reference(node.getNodeId(), NodeIds.HasComponent, getNodeId().expanded(), false));
  }

  /**
   * Add a 'HasSubtype' reference from this node to {@code node} and an inverse 'SubtypeOf'
   * reference from {@code node} back to this node.
   *
   * @param node the node to add as a subtype of this node.
   */
  public void addSubtype(UaObjectTypeNode node) {
    addReference(new Reference(getNodeId(), NodeIds.HasSubtype, node.getNodeId().expanded(), true));

    node.addReference(
        new Reference(node.getNodeId(), NodeIds.HasSubtype, getNodeId().expanded(), false));
  }

  /**
   * Get the value of the NodeVersion Property, if it exists.
   *
   * @return the value of the NodeVersion Property, if it exists.
   * @see ObjectTypeNodeProperties#NodeVersion
   */
  @Nullable
  public String getNodeVersion() {
    return getProperty(ObjectTypeNodeProperties.NodeVersion).orElse(null);
  }

  /**
   * Get the value of the Icon Property, if it exists.
   *
   * @return the value of the Icon Property, if it exists.
   * @see ObjectTypeNodeProperties#Icon
   */
  @Nullable
  public ByteString getIcon() {
    return getProperty(ObjectTypeNodeProperties.Icon).orElse(null);
  }

  /**
   * Set the value of the NodeVersion Property.
   *
   * <p>A PropertyNode will be created if it does not already exist.
   *
   * @param nodeVersion the value to set.
   * @see ObjectTypeNodeProperties#NodeVersion
   */
  public void setNodeVersion(String nodeVersion) {
    setProperty(ObjectTypeNodeProperties.NodeVersion, nodeVersion);
  }

  /**
   * Set the value of the Icon Property.
   *
   * <p>A PropertyNode will be created if it does not already exist.
   *
   * @param icon the value to set.
   * @see ObjectTypeNodeProperties#NodeVersion
   */
  public void setIcon(ByteString icon) {
    setProperty(ObjectTypeNodeProperties.Icon, icon);
  }

  public static UaObjectTypeNodeBuilder builder(UaNodeContext context) {
    return new UaObjectTypeNodeBuilder(context);
  }

  /**
   * Build a {@link UaObjectTypeNode} using the {@link UaObjectTypeNodeBuilder} supplied to the
   * {@code build} function.
   *
   * @param context a {@link UaNodeContext}.
   * @param build a function that accepts a {@link UaObjectTypeNodeBuilder} and uses it to build and
   *     return a {@link UaObjectTypeNode}.
   * @return a {@link UaObjectTypeNode} built using the supplied {@link UaObjectTypeNodeBuilder}.
   */
  public static UaObjectTypeNode build(
      UaNodeContext context, Function<UaObjectTypeNodeBuilder, UaObjectTypeNode> build) {

    UaObjectTypeNodeBuilder builder = new UaObjectTypeNodeBuilder(context);

    return build.apply(builder);
  }

  public static class UaObjectTypeNodeBuilder implements Supplier<UaObjectTypeNode> {

    private final List<AttributeFilter> attributeFilters = new ArrayList<>();

    private final List<Reference> references = new ArrayList<>();

    private NodeId nodeId;
    private QualifiedName browseName;
    private LocalizedText displayName;
    private LocalizedText description = LocalizedText.NULL_VALUE;
    private UInteger writeMask = UInteger.MIN;
    private UInteger userWriteMask = UInteger.MIN;
    private boolean isAbstract = false;

    private final UaNodeContext context;

    public UaObjectTypeNodeBuilder(UaNodeContext context) {
      this.context = context;
    }

    /**
     * @see #build()
     */
    @Override
    public UaObjectTypeNode get() {
      return build();
    }

    /**
     * Build and return the {@link UaObjectTypeNode}.
     *
     * <p>The following fields are required: NodeId, BrowseName, DisplayName.
     *
     * @return a {@link UaObjectTypeNode} built from the configuration of this builder.
     */
    public UaObjectTypeNode build() {
      Preconditions.checkNotNull(nodeId, "NodeId cannot be null");
      Preconditions.checkNotNull(browseName, "BrowseName cannot be null");
      Preconditions.checkNotNull(displayName, "DisplayName cannot be null");

      UaObjectTypeNode node =
          new UaObjectTypeNode(
              context,
              nodeId,
              browseName,
              displayName,
              description,
              writeMask,
              userWriteMask,
              isAbstract);

      references.forEach(node::addReference);

      node.getFilterChain().addLast(attributeFilters);

      return node;
    }

    /**
     * Build the {@link UaObjectTypeNode} using the configured values and add it to the {@link
     * NodeManager} from the {@link UaNodeContext}.
     *
     * @return a {@link UaObjectTypeNode} built from the configured values.
     * @see #build()
     */
    public UaObjectTypeNode buildAndAdd() {
      UaObjectTypeNode node = build();
      context.getNodeManager().addNode(node);
      return node;
    }

    public UaObjectTypeNodeBuilder setNodeId(NodeId nodeId) {
      this.nodeId = nodeId;
      return this;
    }

    public UaObjectTypeNodeBuilder setBrowseName(QualifiedName browseName) {
      this.browseName = browseName;
      return this;
    }

    public UaObjectTypeNodeBuilder setDisplayName(LocalizedText displayName) {
      this.displayName = displayName;
      return this;
    }

    public UaObjectTypeNodeBuilder setDescription(LocalizedText description) {
      this.description = description;
      return this;
    }

    public UaObjectTypeNodeBuilder setWriteMask(UInteger writeMask) {
      this.writeMask = writeMask;
      return this;
    }

    public UaObjectTypeNodeBuilder setUserWriteMask(UInteger userWriteMask) {
      this.userWriteMask = userWriteMask;
      return this;
    }

    public UaObjectTypeNodeBuilder setIsAbstract(boolean isAbstract) {
      this.isAbstract = isAbstract;
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

    public boolean isAbstract() {
      return isAbstract;
    }

    /**
     * Add an {@link AttributeFilter} that will be added to the node's {@link AttributeFilterChain}
     * when it's built.
     *
     * <p>The order filters are added in this builder is maintained.
     *
     * @param attributeFilter the {@link AttributeFilter} to add.
     * @return this {@link UaObjectTypeNodeBuilder}.
     */
    public UaObjectTypeNodeBuilder addAttributeFilter(AttributeFilter attributeFilter) {
      attributeFilters.add(attributeFilter);
      return this;
    }

    /**
     * Add a {@link Reference} to the node when it's built.
     *
     * @param reference the {@link Reference} to add.
     * @return this {@link UaObjectTypeNodeBuilder}.
     */
    public UaObjectTypeNodeBuilder addReference(Reference reference) {
      references.add(reference);
      return this;
    }
  }
}

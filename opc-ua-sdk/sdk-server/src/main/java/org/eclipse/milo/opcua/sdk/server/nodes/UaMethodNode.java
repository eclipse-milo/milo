/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.nodes;

import static org.eclipse.milo.opcua.sdk.core.Reference.ALWAYS_GENERATES_EVENT_PREDICATE;
import static org.eclipse.milo.opcua.sdk.core.Reference.HAS_MODELLING_RULE_PREDICATE;
import static org.eclipse.milo.opcua.sdk.core.Reference.HAS_PROPERTY_PREDICATE;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.core.nodes.MethodNode;
import org.eclipse.milo.opcua.sdk.core.nodes.MethodNodeProperties;
import org.eclipse.milo.opcua.sdk.core.nodes.Node;
import org.eclipse.milo.opcua.sdk.core.nodes.ObjectNode;
import org.eclipse.milo.opcua.sdk.server.NodeManager;
import org.eclipse.milo.opcua.sdk.server.methods.MethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.nodes.filters.AttributeFilter;
import org.eclipse.milo.opcua.sdk.server.nodes.filters.AttributeFilterChain;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.jspecify.annotations.Nullable;

public class UaMethodNode extends UaNode implements MethodNode {

  private volatile MethodInvocationHandler handler = MethodInvocationHandler.NOT_IMPLEMENTED;

  private Boolean executable;
  private Boolean userExecutable;

  /** Construct a {@link UaMethodNode} using only attributes defined prior to OPC UA 1.04. */
  public UaMethodNode(
      UaNodeContext context,
      NodeId nodeId,
      QualifiedName browseName,
      LocalizedText displayName,
      LocalizedText description,
      UInteger writeMask,
      UInteger userWriteMask,
      Boolean executable,
      Boolean userExecutable) {

    super(
        context,
        nodeId,
        NodeClass.Method,
        browseName,
        displayName,
        description,
        writeMask,
        userWriteMask);

    this.executable = executable;
    this.userExecutable = userExecutable;
  }

  /**
   * Construct a {@link UaMethodNode} using all attributes, including those defined by OPC UA 1.04.
   */
  public UaMethodNode(
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
      Boolean executable,
      Boolean userExecutable) {

    super(
        context,
        nodeId,
        NodeClass.Method,
        browseName,
        displayName,
        description,
        writeMask,
        userWriteMask,
        rolePermissions,
        userRolePermissions,
        accessRestrictions);

    this.executable = executable;
    this.userExecutable = userExecutable;
  }

  @Override
  public Boolean isExecutable() {
    return (Boolean) filterChain.getAttribute(this, AttributeId.Executable);
  }

  @Override
  public Boolean isUserExecutable() {
    return (Boolean) filterChain.getAttribute(this, AttributeId.UserExecutable);
  }

  @Override
  public void setExecutable(Boolean executable) {
    filterChain.setAttribute(this, AttributeId.Executable, executable);
  }

  @Override
  public void setUserExecutable(Boolean userExecutable) {
    filterChain.setAttribute(this, AttributeId.UserExecutable, userExecutable);
  }

  @Override
  public synchronized Object getAttribute(AttributeId attributeId) {
    return switch (attributeId) {
      case Executable -> executable;
      case UserExecutable -> userExecutable;
      default -> super.getAttribute(attributeId);
    };
  }

  @Override
  public synchronized void setAttribute(AttributeId attributeId, Object value) {
    switch (attributeId) {
      case Executable:
        executable = (Boolean) value;
        break;

      case UserExecutable:
        userExecutable = (Boolean) value;
        break;

      default:
        super.setAttribute(attributeId, value);
        return; // prevent firing an attribute change
    }

    fireAttributeChanged(attributeId, value);
  }

  public List<Node> getPropertyNodes() {
    return getReferences().stream()
        .filter(HAS_PROPERTY_PREDICATE)
        .flatMap(r -> getManagedNode(r.getTargetNodeId()).stream())
        .collect(Collectors.toList());
  }

  public Optional<ObjectNode> getModellingRuleNode() {
    Node node =
        getReferences().stream()
            .filter(HAS_MODELLING_RULE_PREDICATE)
            .findFirst()
            .flatMap(r -> getManagedNode(r.getTargetNodeId()))
            .orElse(null);

    ObjectNode objectNode = (node instanceof ObjectNode) ? (ObjectNode) node : null;

    return Optional.ofNullable(objectNode);
  }

  public List<Node> getAlwaysGeneratesEventNodes() {
    return getReferences().stream()
        .filter(ALWAYS_GENERATES_EVENT_PREDICATE)
        .flatMap(r -> getManagedNode(r.getTargetNodeId()).stream())
        .collect(Collectors.toList());
  }

  public MethodInvocationHandler getInvocationHandler() {
    return handler;
  }

  public void setInvocationHandler(MethodInvocationHandler handler) {
    this.handler = handler;
  }

  /**
   * Get the value of the NodeVersion Property, if it exists.
   *
   * @return the value of the NodeVersion Property, if it exists.
   * @see MethodNodeProperties#NodeVersion
   */
  @Nullable
  public String getNodeVersion() {
    return getProperty(MethodNodeProperties.NodeVersion).orElse(null);
  }

  /**
   * Get the value of the InputArguments Property, if it exists.
   *
   * @return the value of the InputArguments Property, if it exists.
   * @see MethodNodeProperties#InputArguments
   */
  @Nullable
  public Argument[] getInputArguments() {
    return getProperty(MethodNodeProperties.InputArguments).orElse(null);
  }

  /**
   * Get the value of the OutputArguments Property, if it exists.
   *
   * @return the value of the OutputArguments Property, if it exists.
   * @see MethodNodeProperties#OutputArguments
   */
  @Nullable
  public Argument[] getOutputArguments() {
    return getProperty(MethodNodeProperties.OutputArguments).orElse(null);
  }

  /**
   * Set the value of the NodeVersion Property.
   *
   * <p>A PropertyNode will be created if it does not already exist.
   *
   * @param nodeVersion the value to set.
   * @see MethodNodeProperties#NodeVersion
   */
  public void setNodeVersion(String nodeVersion) {
    setProperty(MethodNodeProperties.NodeVersion, nodeVersion);
  }

  /**
   * Set the value of the InputArguments Property.
   *
   * <p>A PropertyNode will be created if it does not already exist.
   *
   * @param inputArguments the value to set.
   * @see MethodNodeProperties#InputArguments
   */
  public void setInputArguments(Argument[] inputArguments) {
    setProperty(MethodNodeProperties.InputArguments, inputArguments);
  }

  /**
   * Set the value of the OutputArguments Property.
   *
   * <p>A PropertyNode will be created if it does not already exist.
   *
   * @param outputArguments the value to set.
   * @see MethodNodeProperties#OutputArguments
   */
  public void setOutputArguments(Argument[] outputArguments) {
    setProperty(MethodNodeProperties.OutputArguments, outputArguments);
  }

  /**
   * @return a new {@link UaMethodNodeBuilder}.
   */
  public static UaMethodNodeBuilder builder(UaNodeContext context) {
    return new UaMethodNodeBuilder(context);
  }

  /**
   * Build a {@link UaMethodNode} using the {@link UaMethodNodeBuilder} supplied to the {@code
   * build} function.
   *
   * @param context a {@link UaNodeContext}.
   * @param build a function that accepts a {@link UaMethodNodeBuilder} and uses it to build and
   *     return a {@link UaMethodNode}.
   * @return a {@link UaMethodNode} built using the supplied {@link UaMethodNodeBuilder}.
   */
  public static UaMethodNode build(
      UaNodeContext context, Function<UaMethodNodeBuilder, UaMethodNode> build) {

    UaMethodNodeBuilder builder = new UaMethodNodeBuilder(context);

    return build.apply(builder);
  }

  public static class UaMethodNodeBuilder implements Supplier<UaMethodNode> {

    private final List<AttributeFilter> attributeFilters = new ArrayList<>();

    private final List<Reference> references = new ArrayList<>();

    private NodeId nodeId;
    private QualifiedName browseName;
    private LocalizedText displayName;
    private LocalizedText description = LocalizedText.NULL_VALUE;
    private UInteger writeMask = UInteger.MIN;
    private UInteger userWriteMask = UInteger.MIN;

    private boolean executable = true;
    private boolean userExecutable = true;

    private final UaNodeContext context;

    public UaMethodNodeBuilder(UaNodeContext context) {
      this.context = context;
    }

    /**
     * @see #build()
     */
    @Override
    public UaMethodNode get() {
      return build();
    }

    /**
     * Build and return the {@link UaMethodNode}.
     *
     * <p>The following fields are required: NodeId, BrowseName, DisplayName.
     *
     * @return a {@link UaMethodNode} built from the configuration of this builder.
     */
    public UaMethodNode build() {
      Preconditions.checkNotNull(nodeId, "NodeId cannot be null");
      Preconditions.checkNotNull(browseName, "BrowseName cannot be null");
      Preconditions.checkNotNull(displayName, "DisplayName cannot be null");

      UaMethodNode node =
          new UaMethodNode(
              context,
              nodeId,
              browseName,
              displayName,
              description,
              writeMask,
              userWriteMask,
              executable,
              userExecutable);

      references.forEach(node::addReference);

      node.getFilterChain().addLast(attributeFilters);

      return node;
    }

    /**
     * Build the {@link UaMethodNode} using the configured values and add it to the {@link
     * NodeManager} from the {@link UaNodeContext}.
     *
     * @return a {@link UaMethodNode} built from the configured values.
     * @see #build()
     */
    public UaMethodNode buildAndAdd() {
      UaMethodNode node = build();
      context.getNodeManager().addNode(node);
      return node;
    }

    public UaMethodNodeBuilder setNodeId(NodeId nodeId) {
      this.nodeId = nodeId;
      return this;
    }

    public UaMethodNodeBuilder setBrowseName(QualifiedName browseName) {
      this.browseName = browseName;
      return this;
    }

    public UaMethodNodeBuilder setDisplayName(LocalizedText displayName) {
      this.displayName = displayName;
      return this;
    }

    public UaMethodNodeBuilder setDescription(LocalizedText description) {
      this.description = description;
      return this;
    }

    public UaMethodNodeBuilder setWriteMask(UInteger writeMask) {
      this.writeMask = writeMask;
      return this;
    }

    public UaMethodNodeBuilder setUserWriteMask(UInteger userWriteMask) {
      this.userWriteMask = userWriteMask;
      return this;
    }

    public UaMethodNodeBuilder setExecutable(boolean executable) {
      this.executable = executable;
      return this;
    }

    public UaMethodNodeBuilder setUserExecutable(boolean userExecutable) {
      this.userExecutable = userExecutable;
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

    public boolean isExecutable() {
      return executable;
    }

    public boolean isUserExecutable() {
      return userExecutable;
    }

    /**
     * Add an {@link AttributeFilter} that will be added to the node's {@link AttributeFilterChain}
     * when it's built.
     *
     * <p>The order filters are added in this builder is maintained.
     *
     * @param attributeFilter the {@link AttributeFilter} to add.
     * @return this {@link UaMethodNodeBuilder}.
     */
    public UaMethodNodeBuilder addAttributeFilter(AttributeFilter attributeFilter) {
      attributeFilters.add(attributeFilter);
      return this;
    }

    /**
     * Add a {@link Reference} to the node when it's built.
     *
     * @param reference the {@link Reference} to add.
     * @return this {@link UaMethodNodeBuilder}.
     */
    public UaMethodNodeBuilder addReference(Reference reference) {
      references.add(reference);
      return this;
    }
  }
}

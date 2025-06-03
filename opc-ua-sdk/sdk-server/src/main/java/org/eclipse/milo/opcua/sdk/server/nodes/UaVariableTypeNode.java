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

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNodeProperties;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableTypeNode;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableTypeNodeProperties;
import org.eclipse.milo.opcua.sdk.server.NodeManager;
import org.eclipse.milo.opcua.sdk.server.nodes.filters.AttributeFilter;
import org.eclipse.milo.opcua.sdk.server.nodes.filters.AttributeFilterChain;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.jspecify.annotations.Nullable;

public class UaVariableTypeNode extends UaNode implements VariableTypeNode {

  private DataValue value;
  private NodeId dataType;
  private Integer valueRank;
  private UInteger[] arrayDimensions;
  private Boolean isAbstract;

  /** Construct a {@link UaVariableTypeNode} using only attributes defined prior to OPC UA 1.04. */
  public UaVariableTypeNode(
      UaNodeContext context,
      NodeId nodeId,
      QualifiedName browseName,
      LocalizedText displayName,
      LocalizedText description,
      UInteger writeMask,
      UInteger userWriteMask,
      DataValue value,
      NodeId dataType,
      Integer valueRank,
      UInteger[] arrayDimensions,
      Boolean isAbstract) {

    super(
        context,
        nodeId,
        NodeClass.VariableType,
        browseName,
        displayName,
        description,
        writeMask,
        userWriteMask);

    this.value = value;
    this.dataType = dataType;
    this.valueRank = valueRank;
    this.arrayDimensions = arrayDimensions;
    this.isAbstract = isAbstract;
  }

  /** Construct a {@link UaVariableTypeNode} using all available attributes. */
  public UaVariableTypeNode(
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
      DataValue value,
      NodeId dataType,
      Integer valueRank,
      UInteger[] arrayDimensions,
      Boolean isAbstract) {

    super(
        context,
        nodeId,
        NodeClass.VariableType,
        browseName,
        displayName,
        description,
        writeMask,
        userWriteMask,
        rolePermissions,
        userRolePermissions,
        accessRestrictions);

    this.value = value;
    this.dataType = dataType;
    this.valueRank = valueRank;
    this.arrayDimensions = arrayDimensions;
    this.isAbstract = isAbstract;
  }

  @Override
  public DataValue getValue() {
    return (DataValue) filterChain.getAttribute(this, AttributeId.Value);
  }

  @Override
  public NodeId getDataType() {
    return (NodeId) filterChain.getAttribute(this, AttributeId.DataType);
  }

  @Override
  public Integer getValueRank() {
    return (Integer) filterChain.getAttribute(this, AttributeId.ValueRank);
  }

  @Override
  public UInteger[] getArrayDimensions() {
    return (UInteger[]) filterChain.getAttribute(this, AttributeId.ArrayDimensions);
  }

  @Override
  public Boolean getIsAbstract() {
    return (Boolean) filterChain.getAttribute(this, AttributeId.IsAbstract);
  }

  @Override
  public void setValue(DataValue value) {
    filterChain.setAttribute(this, AttributeId.Value, value);
  }

  @Override
  public void setDataType(NodeId dataType) {
    filterChain.setAttribute(this, AttributeId.DataType, dataType);
  }

  @Override
  public void setValueRank(Integer valueRank) {
    filterChain.setAttribute(this, AttributeId.ValueRank, valueRank);
  }

  @Override
  public void setArrayDimensions(UInteger[] arrayDimensions) {
    filterChain.setAttribute(this, AttributeId.ArrayDimensions, arrayDimensions);
  }

  @Override
  public void setIsAbstract(Boolean isAbstract) {
    filterChain.setAttribute(this, AttributeId.IsAbstract, isAbstract);
  }

  @Override
  public synchronized Object getAttribute(AttributeId attributeId) {
    return switch (attributeId) {
      case Value -> value;
      case DataType -> dataType;
      case ValueRank -> valueRank;
      case ArrayDimensions -> arrayDimensions;
      case IsAbstract -> isAbstract;
      default -> super.getAttribute(attributeId);
    };
  }

  @Override
  public synchronized void setAttribute(AttributeId attributeId, Object value) {
    switch (attributeId) {
      case Value:
        this.value = (DataValue) value;
        break;

      case DataType:
        dataType = (NodeId) value;
        break;

      case ValueRank:
        valueRank = (Integer) value;
        break;

      case ArrayDimensions:
        arrayDimensions = (UInteger[]) value;
        break;

      case IsAbstract:
        isAbstract = (Boolean) value;
        break;

      default:
        super.setAttribute(attributeId, value);
        return; // prevent firing an attribute change
    }

    fireAttributeChanged(attributeId, value);
  }

  /**
   * Get the value of the NodeVersion Property, if it exists.
   *
   * @return the value of the NodeVersion Property, if it exists.
   * @see VariableTypeNodeProperties#NodeVersion
   */
  @Nullable
  public String getNodeVersion() {
    return getProperty(VariableTypeNodeProperties.NodeVersion).orElse(null);
  }

  /**
   * Set the value of the NodeVersion Property.
   *
   * <p>A PropertyNode will be created if it does not already exist.
   *
   * @param nodeVersion the value to set.
   * @see VariableNodeProperties#NodeVersion
   */
  public void setNodeVersion(String nodeVersion) {
    setProperty(VariableTypeNodeProperties.NodeVersion, nodeVersion);
  }

  /**
   * @return a new {@link UaVariableTypeNodeBuilder}.
   */
  public static UaVariableTypeNodeBuilder builder(UaNodeContext context) {
    return new UaVariableTypeNodeBuilder(context);
  }

  /**
   * Build a {@link UaVariableTypeNode} using the {@link UaVariableTypeNodeBuilder} supplied to the
   * {@code build} function.
   *
   * @param context the {@link UaNodeContext} to use.
   * @param build a function that accepts a {@link UaVariableTypeNodeBuilder} and uses it to build
   *     and return a {@link UaVariableTypeNode}.
   * @return a {@link UaVariableTypeNode} built using the supplied {@link
   *     UaVariableTypeNodeBuilder}.
   */
  public static UaVariableTypeNode build(
      UaNodeContext context, Function<UaVariableTypeNodeBuilder, UaVariableTypeNode> build) {

    UaVariableTypeNodeBuilder builder = new UaVariableTypeNodeBuilder(context);

    return build.apply(builder);
  }

  public static class UaVariableTypeNodeBuilder implements Supplier<UaVariableTypeNode> {

    private final List<AttributeFilter> attributeFilters = new ArrayList<>();

    private final List<Reference> references = new ArrayList<>();

    private NodeId nodeId;
    private QualifiedName browseName;
    private LocalizedText displayName;
    private LocalizedText description = LocalizedText.NULL_VALUE;
    private UInteger writeMask = UInteger.MIN;
    private UInteger userWriteMask = UInteger.MIN;
    private RolePermissionType[] rolePermissions;
    private RolePermissionType[] userRolePermissions;
    private AccessRestrictionType accessRestrictions;

    private DataValue value;
    private NodeId dataType;
    private Integer valueRank;
    private UInteger[] arrayDimensions;
    private Boolean isAbstract;

    private final UaNodeContext context;

    public UaVariableTypeNodeBuilder(UaNodeContext context) {
      this.context = context;
    }

    /**
     * @see #build()
     */
    @Override
    public UaVariableTypeNode get() {
      return build();
    }

    /**
     * Build and return the {@link UaVariableTypeNode}.
     *
     * <p>The following fields are required: NodeId, BrowseName, DisplayName.
     *
     * @return a {@link UaVariableTypeNode} built from the configuration of this builder.
     */
    public UaVariableTypeNode build() {
      Preconditions.checkNotNull(nodeId, "NodeId cannot be null");
      Preconditions.checkNotNull(browseName, "BrowseName cannot be null");
      Preconditions.checkNotNull(displayName, "DisplayName cannot be null");

      long hasTypeDefinitionCount =
          references.stream()
              .filter(r -> NodeIds.HasTypeDefinition.equals(r.getReferenceTypeId()))
              .count();

      if (hasTypeDefinitionCount == 0) {
        setTypeDefinition(NodeIds.BaseVariableType);
      } else {
        Preconditions.checkState(
            hasTypeDefinitionCount == 1,
            "VariableType Node must have exactly one HasTypeDefinition reference.");
      }

      UaVariableTypeNode node =
          new UaVariableTypeNode(
              context,
              nodeId,
              browseName,
              displayName,
              description,
              writeMask,
              userWriteMask,
              rolePermissions,
              userRolePermissions,
              accessRestrictions,
              value,
              dataType,
              valueRank,
              arrayDimensions,
              isAbstract);

      references.forEach(node::addReference);

      node.getFilterChain().addLast(attributeFilters);

      return node;
    }

    /**
     * Build the {@link UaVariableTypeNode} using the configured values and add it to the {@link
     * NodeManager} from the {@link UaNodeContext}.
     *
     * @return a {@link UaVariableTypeNode} built from the configured values.
     * @see #build()
     */
    public UaVariableTypeNode buildAndAdd() {
      UaVariableTypeNode node = build();
      context.getNodeManager().addNode(node);
      return node;
    }

    public UaVariableTypeNodeBuilder setNodeId(NodeId nodeId) {
      this.nodeId = nodeId;
      return this;
    }

    public UaVariableTypeNodeBuilder setBrowseName(QualifiedName browseName) {
      this.browseName = browseName;
      return this;
    }

    public UaVariableTypeNodeBuilder setDisplayName(LocalizedText displayName) {
      this.displayName = displayName;
      return this;
    }

    public UaVariableTypeNodeBuilder setDescription(LocalizedText description) {
      this.description = description;
      return this;
    }

    public UaVariableTypeNodeBuilder setWriteMask(UInteger writeMask) {
      this.writeMask = writeMask;
      return this;
    }

    public UaVariableTypeNodeBuilder setUserWriteMask(UInteger userWriteMask) {
      this.userWriteMask = userWriteMask;
      return this;
    }

    public UaVariableTypeNodeBuilder setRolePermissions(RolePermissionType[] rolePermissions) {
      this.rolePermissions = rolePermissions;
      return this;
    }

    public UaVariableTypeNodeBuilder setUserRolePermissions(
        RolePermissionType[] userRolePermissions) {
      this.userRolePermissions = userRolePermissions;
      return this;
    }

    public UaVariableTypeNodeBuilder setAccessRestrictions(
        AccessRestrictionType accessRestrictions) {
      this.accessRestrictions = accessRestrictions;
      return this;
    }

    public UaVariableTypeNodeBuilder setValue(DataValue value) {
      this.value = value;
      return this;
    }

    public UaVariableTypeNodeBuilder setDataType(NodeId dataType) {
      this.dataType = dataType;
      return this;
    }

    public UaVariableTypeNodeBuilder setValueRank(Integer valueRank) {
      this.valueRank = valueRank;
      return this;
    }

    public UaVariableTypeNodeBuilder setArrayDimensions(UInteger[] arrayDimensions) {
      this.arrayDimensions = arrayDimensions;
      return this;
    }

    public UaVariableTypeNodeBuilder setIsAbstract(Boolean isAbstract) {
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

    public RolePermissionType[] getRolePermissions() {
      return rolePermissions;
    }

    public RolePermissionType[] getUserRolePermissions() {
      return userRolePermissions;
    }

    public AccessRestrictionType getAccessRestrictions() {
      return accessRestrictions;
    }

    public DataValue getValue() {
      return value;
    }

    public NodeId getDataType() {
      return dataType;
    }

    public Integer getValueRank() {
      return valueRank;
    }

    public UInteger[] getArrayDimensions() {
      return arrayDimensions;
    }

    public Boolean getIsAbstract() {
      return isAbstract;
    }

    /**
     * Add an {@link AttributeFilter} that will be added to the node's {@link AttributeFilterChain}
     * when it's built.
     *
     * <p>The order filters are added in this builder are maintained.
     *
     * @param attributeFilter the {@link AttributeFilter} to add.
     * @return this {@link UaVariableTypeNodeBuilder}.
     */
    public UaVariableTypeNodeBuilder addAttributeFilter(AttributeFilter attributeFilter) {
      attributeFilters.add(attributeFilter);
      return this;
    }

    /**
     * Add a {@link Reference} to the node when it's built.
     *
     * @param reference the {@link Reference} to add.
     * @return this {@link UaVariableTypeNodeBuilder}.
     */
    public UaVariableTypeNodeBuilder addReference(Reference reference) {
      references.add(reference);
      return this;
    }

    /**
     * Convenience method for adding the required HasTypeDefinition reference.
     *
     * <p>{@link #setNodeId(NodeId)} must have already been called before invoking this method.
     *
     * @param typeDefinition The {@link NodeId} of the TypeDefinition.
     * @return this {@link UaVariableTypeNodeBuilder}.
     */
    public UaVariableTypeNodeBuilder setTypeDefinition(NodeId typeDefinition) {
      Objects.requireNonNull(nodeId, "NodeId cannot be null");

      // Remove any existing HasTypeDefinition references; only one is allowed.
      references.removeIf(ref -> ref.getReferenceTypeId().equals(NodeIds.HasTypeDefinition));

      references.add(
          new Reference(nodeId, NodeIds.HasTypeDefinition, typeDefinition.expanded(), true));

      return this;
    }
  }
}

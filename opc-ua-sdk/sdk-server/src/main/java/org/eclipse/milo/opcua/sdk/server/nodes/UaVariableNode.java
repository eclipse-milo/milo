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
import static org.eclipse.milo.opcua.sdk.core.Reference.HAS_MODELLING_RULE_PREDICATE;
import static org.eclipse.milo.opcua.sdk.core.Reference.HAS_PROPERTY_PREDICATE;
import static org.eclipse.milo.opcua.sdk.core.Reference.HAS_TYPE_DEFINITION_PREDICATE;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.eclipse.milo.opcua.sdk.core.AccessLevel;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.core.ValueRanks;
import org.eclipse.milo.opcua.sdk.core.nodes.Node;
import org.eclipse.milo.opcua.sdk.core.nodes.ObjectNode;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNodeProperties;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.NodeManager;
import org.eclipse.milo.opcua.sdk.server.nodes.filters.AttributeFilter;
import org.eclipse.milo.opcua.sdk.server.nodes.filters.AttributeFilterChain;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessLevelExType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.EUInformation;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.TimeZoneDataType;
import org.jspecify.annotations.Nullable;

public class UaVariableNode extends UaNode implements VariableNode {

  static final DataValue INITIAL_VALUE =
      new DataValue(new StatusCode(StatusCodes.Uncertain_InitialValue));

  private DataValue value = INITIAL_VALUE;
  private NodeId dataType = NodeIds.BaseDataType;
  private Integer valueRank = ValueRanks.Scalar;
  private UInteger[] arrayDimensions = null;
  private UByte accessLevel = AccessLevel.toValue(AccessLevel.CurrentRead);
  private UByte userAccessLevel = AccessLevel.toValue(AccessLevel.CurrentRead);
  private Double minimumSamplingInterval = -1.0;
  private Boolean historizing = false;
  private AccessLevelExType accessLevelEx;

  // OPC UA 1.03, only base attributes
  // TODO remove after nodes are regenerated?
  public UaVariableNode(
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
        NodeClass.Variable,
        browseName,
        displayName,
        description,
        writeMask,
        userWriteMask);
  }

  /** Construct a {@link UaVariableNode} using only attributes defined prior to OPC UA 1.04. */
  public UaVariableNode(
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
      UByte accessLevel,
      UByte userAccessLevel,
      Double minimumSamplingInterval,
      boolean historizing) {

    super(
        context,
        nodeId,
        NodeClass.Variable,
        browseName,
        displayName,
        description,
        writeMask,
        userWriteMask);

    this.value = value;
    this.dataType = dataType;
    this.valueRank = valueRank;
    this.arrayDimensions = arrayDimensions;
    this.accessLevel = accessLevel;
    this.userAccessLevel = userAccessLevel;
    this.minimumSamplingInterval = minimumSamplingInterval;
    this.historizing = historizing;
  }

  /**
   * Construct a {@link UaVariableNode} using all attributes, including those defined by OPC UA
   * 1.04.
   */
  public UaVariableNode(
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
      UByte accessLevel,
      UByte userAccessLevel,
      Double minimumSamplingInterval,
      boolean historizing,
      AccessLevelExType accessLevelEx) {

    super(
        context,
        nodeId,
        NodeClass.Variable,
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
    this.accessLevel = accessLevel;
    this.userAccessLevel = userAccessLevel;
    this.minimumSamplingInterval = minimumSamplingInterval;
    this.historizing = historizing;
    this.accessLevelEx = accessLevelEx;
  }

  /**
   * Construct a {@link UaVariableNode} using all attributes in the intersection of attributes
   * defined by Variables and VariableTypes, making it suitable for use when instantiating a
   * Variable instance from an VariableType.
   *
   * <p>This constructor requires attributes defined by OPC 1.04.
   */
  public UaVariableNode(
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
      UInteger[] arrayDimensions) {

    super(
        context,
        nodeId,
        NodeClass.Variable,
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
  public UByte getAccessLevel() {
    return (UByte) filterChain.getAttribute(this, AttributeId.AccessLevel);
  }

  @Override
  public UByte getUserAccessLevel() {
    return (UByte) filterChain.getAttribute(this, AttributeId.UserAccessLevel);
  }

  @Override
  public Double getMinimumSamplingInterval() {
    return (Double) filterChain.getAttribute(this, AttributeId.MinimumSamplingInterval);
  }

  @Override
  public Boolean getHistorizing() {
    return (Boolean) filterChain.getAttribute(this, AttributeId.Historizing);
  }

  @Override
  public AccessLevelExType getAccessLevelEx() {
    return (AccessLevelExType) filterChain.getAttribute(this, AttributeId.AccessLevelEx);
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
  public void setAccessLevel(UByte accessLevel) {
    filterChain.setAttribute(this, AttributeId.AccessLevel, accessLevel);
  }

  @Override
  public void setUserAccessLevel(UByte userAccessLevel) {
    filterChain.setAttribute(this, AttributeId.UserAccessLevel, userAccessLevel);
  }

  @Override
  public void setMinimumSamplingInterval(Double minimumSamplingInterval) {
    filterChain.setAttribute(this, AttributeId.MinimumSamplingInterval, minimumSamplingInterval);
  }

  @Override
  public void setHistorizing(Boolean historizing) {
    filterChain.setAttribute(this, AttributeId.Historizing, historizing);
  }

  @Override
  public void setAccessLevelEx(AccessLevelExType accessLevelEx) {
    filterChain.setAttribute(this, AttributeId.AccessLevelEx, accessLevelEx);
  }

  @Override
  public synchronized Object getAttribute(AttributeId attributeId) {
    switch (attributeId) {
      case Value:
        // The value is being directly from the field/memory.
        // We "know the value to be accurate" at this point, so apply
        // a new timestamp to the value.
        // This ensures that when static values are read they have a
        // current value for serverTimestamp.
        return value.copy(b -> b.setServerTime(DateTime.now()));

      case DataType:
        return dataType;

      case ValueRank:
        return valueRank;

      case ArrayDimensions:
        return arrayDimensions;

      case AccessLevel:
        return accessLevel;

      case UserAccessLevel:
        return userAccessLevel;

      case MinimumSamplingInterval:
        return minimumSamplingInterval;

      case Historizing:
        return historizing;

      case AccessLevelEx:
        return accessLevelEx;

      default:
        return super.getAttribute(attributeId);
    }
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

      case AccessLevel:
        accessLevel = (UByte) value;
        break;

      case UserAccessLevel:
        userAccessLevel = (UByte) value;
        break;

      case MinimumSamplingInterval:
        minimumSamplingInterval = (Double) value;
        break;

      case Historizing:
        historizing = (Boolean) value;
        break;

      case AccessLevelEx:
        accessLevelEx = (AccessLevelExType) value;
        break;

      default:
        super.setAttribute(attributeId, value);
        return; // prevent firing an attribute change
    }

    fireAttributeChanged(attributeId, value);
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

  public List<Node> getPropertyNodes() {
    return getReferences().stream()
        .filter(HAS_PROPERTY_PREDICATE)
        .flatMap(r -> getManagedNode(r.getTargetNodeId()).stream())
        .collect(Collectors.toList());
  }

  public List<Node> getComponentNodes() {
    return getReferences().stream()
        .filter(HAS_COMPONENT_PREDICATE)
        .flatMap(r -> getManagedNode(r.getTargetNodeId()).stream())
        .collect(Collectors.toList());
  }

  public VariableTypeNode getTypeDefinitionNode() {
    Node node =
        getReferences().stream()
            .filter(HAS_TYPE_DEFINITION_PREDICATE)
            .findFirst()
            .flatMap(r -> getManagedNode(r.getTargetNodeId()))
            .orElse(null);

    return (node instanceof VariableTypeNode) ? (VariableTypeNode) node : null;
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
   * @see VariableNodeProperties#NodeVersion
   */
  @Nullable
  public String getNodeVersion() {
    return getProperty(VariableNodeProperties.NodeVersion).orElse(null);
  }

  /**
   * Get the value of the LocalTime Property, if it exists.
   *
   * @return the value of the LocalTime Property, if it exists.
   * @see VariableNodeProperties#LocalTime
   */
  @Nullable
  public TimeZoneDataType getLocalTime() {
    return getProperty(VariableNodeProperties.LocalTime).orElse(null);
  }

  /**
   * Get the value of the DataTypeVersion Property, if it exists.
   *
   * @return the value of the DataTypeVersion Property, if it exists.
   * @see VariableNodeProperties#DataTypeVersion
   */
  @Nullable
  public String getDataTypeVersion() {
    return getProperty(VariableNodeProperties.DataTypeVersion).orElse(null);
  }

  /**
   * Get the value of the DictionaryFragment Property, if it exists.
   *
   * @return the value of the DictionaryFragment Property, if it exists.
   * @see VariableNodeProperties#DictionaryFragment
   */
  @Nullable
  public ByteString getDictionaryFragment() {
    return getProperty(VariableNodeProperties.DictionaryFragment).orElse(null);
  }

  /**
   * Get the value of the AllowNulls Property, if it exists.
   *
   * @return the value of the AllowNulls Property, if it exists.
   * @see VariableNodeProperties#AllowNulls
   */
  @Nullable
  public Boolean getAllowNulls() {
    return getProperty(VariableNodeProperties.AllowNulls).orElse(null);
  }

  /**
   * Get the value of the MaxStringLength Property, if it exists.
   *
   * @return the value of the MaxStringLength Property, if it exists.
   * @see VariableNodeProperties#MaxStringLength
   */
  @Nullable
  public UInteger getMaxStringLength() {
    return getProperty(VariableNodeProperties.MaxStringLength).orElse(null);
  }

  /**
   * Get the value of the MaxArrayLength Property, if it exists.
   *
   * @return the value of the MaxArrayLength Property, if it exists.
   * @see VariableNodeProperties#MaxArrayLength
   */
  @Nullable
  public UInteger getMaxArrayLength() {
    return getProperty(VariableNodeProperties.MaxArrayLength).orElse(null);
  }

  /**
   * Get the value of the EngineeringUnits Property, if it exists.
   *
   * @return the value of the EngineeringUnits Property, if it exists.
   * @see VariableNodeProperties#EngineeringUnits
   */
  @Nullable
  public EUInformation getEngineeringUnits() {
    return getProperty(VariableNodeProperties.EngineeringUnits).orElse(null);
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
    setProperty(VariableNodeProperties.NodeVersion, nodeVersion);
  }

  /**
   * Set the value of the LocalTime Property.
   *
   * <p>A PropertyNode will be created if it does not already exist.
   *
   * @param localTime the value to set.
   * @see VariableNodeProperties#LocalTime
   */
  public void setLocalTime(TimeZoneDataType localTime) {
    setProperty(VariableNodeProperties.LocalTime, localTime);
  }

  /**
   * Set the value of the DataTypeVersion Property.
   *
   * <p>A PropertyNode will be created if it does not already exist.
   *
   * @param dataTypeVersion the value to set.
   * @see VariableNodeProperties#DataTypeVersion
   */
  public void setDataTypeVersion(String dataTypeVersion) {
    setProperty(VariableNodeProperties.DataTypeVersion, dataTypeVersion);
  }

  /**
   * Set the value of the DictionaryFragment Property.
   *
   * <p>A PropertyNode will be created if it does not already exist.
   *
   * @param dictionaryFragment the value to set.
   * @see VariableNodeProperties#DictionaryFragment
   */
  public void setDictionaryFragment(ByteString dictionaryFragment) {
    setProperty(VariableNodeProperties.DictionaryFragment, dictionaryFragment);
  }

  /**
   * Set the value of the AllowNulls Property.
   *
   * <p>A PropertyNode will be created if it does not already exist.
   *
   * @param allowNulls the value to set.
   * @see VariableNodeProperties#AllowNulls
   */
  public void setAllowNulls(Boolean allowNulls) {
    setProperty(VariableNodeProperties.AllowNulls, allowNulls);
  }

  /**
   * Set the value of the MaxStringLength Property.
   *
   * <p>A PropertyNode will be created if it does not already exist.
   *
   * @param maxStringLength the value to set.
   * @see VariableNodeProperties#MaxStringLength
   */
  public void setMaxStringLength(UInteger maxStringLength) {
    setProperty(VariableNodeProperties.MaxStringLength, maxStringLength);
  }

  /**
   * Set the value of the MaxArrayLength Property.
   *
   * <p>A PropertyNode will be created if it does not already exist.
   *
   * @param maxArrayLength the value to set.
   * @see VariableNodeProperties#MaxArrayLength
   */
  public void setMaxArrayLength(UInteger maxArrayLength) {
    setProperty(VariableNodeProperties.MaxArrayLength, maxArrayLength);
  }

  /**
   * Set the value of the EngineeringUnits Property.
   *
   * <p>A PropertyNode will be created if it does not already exist.
   *
   * @param engineeringUnits the value to set.
   * @see VariableNodeProperties#EngineeringUnits
   */
  public void setEngineeringUnits(EUInformation engineeringUnits) {
    setProperty(VariableNodeProperties.EngineeringUnits, engineeringUnits);
  }

  /**
   * @deprecated use {@link UaVariableNodeBuilder#UaVariableNodeBuilder(UaNodeContext)} or {@link
   *     #build(UaNodeContext, Function)}.
   */
  @Deprecated
  public static UaVariableNodeBuilder builder(UaNodeContext context) {
    return new UaVariableNodeBuilder(context);
  }

  /**
   * Build a {@link UaVariableNode} using the {@link UaVariableNodeBuilder} supplied to the {@code
   * build} function.
   *
   * @param context a {@link UaNodeContext}.
   * @param build a function that accepts a {@link UaVariableNodeBuilder} and uses it to build and
   *     return a {@link UaVariableNode}.
   * @return a {@link UaVariableNode} built using the supplied {@link UaVariableNodeBuilder}.
   */
  public static UaVariableNode build(
      UaNodeContext context, Function<UaVariableNodeBuilder, UaVariableNode> build) {

    UaVariableNodeBuilder builder = new UaVariableNodeBuilder(context);

    return build.apply(builder);
  }

  public static class UaVariableNodeBuilder implements Supplier<UaVariableNode> {

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

    private DataValue value =
        new DataValue(
            Variant.NULL_VALUE,
            new StatusCode(StatusCodes.Uncertain_InitialValue),
            DateTime.NULL_VALUE,
            DateTime.now());

    private NodeId dataType = NodeIds.BaseDataType;
    private int valueRank = ValueRanks.Scalar;
    private UInteger[] arrayDimensions = null;
    private UByte accessLevel = AccessLevel.toValue(AccessLevel.CurrentRead);
    private UByte userAccessLevel = AccessLevel.toValue(AccessLevel.CurrentRead);
    private Double minimumSamplingInterval = -1.0;
    private boolean historizing = false;
    private AccessLevelExType accessLevelEx = null;

    private final UaNodeContext context;

    public UaVariableNodeBuilder(UaNodeContext context) {
      this.context = context;
    }

    /**
     * @see #build()
     */
    @Override
    public UaVariableNode get() {
      return build();
    }

    /**
     * Build and return the {@link UaVariableNode}.
     *
     * <p>The following fields are required: NodeId, BrowseName, DisplayName.
     *
     * @return a {@link UaVariableNode} built from the configuration of this builder.
     */
    public UaVariableNode build() {
      Preconditions.checkNotNull(nodeId, "NodeId cannot be null");
      Preconditions.checkNotNull(browseName, "BrowseName cannot be null");
      Preconditions.checkNotNull(displayName, "DisplayName cannot be null");

      long hasTypeDefinitionCount =
          references.stream()
              .filter(r -> NodeIds.HasTypeDefinition.equals(r.getReferenceTypeId()))
              .count();

      if (hasTypeDefinitionCount == 0) {
        setTypeDefinition(NodeIds.BaseDataVariableType);
      } else {
        Preconditions.checkState(
            hasTypeDefinitionCount == 1,
            "Variable Node must have exactly one HasTypeDefinition reference.");
      }

      UaVariableNode node =
          new UaVariableNode(
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
              accessLevel,
              userAccessLevel,
              minimumSamplingInterval,
              historizing,
              accessLevelEx);

      references.forEach(node::addReference);

      node.getFilterChain().addLast(attributeFilters);

      return node;
    }

    /**
     * Build the {@link UaVariableNode} using the configured values and add it to the {@link
     * NodeManager} from the {@link UaNodeContext}.
     *
     * @return a {@link UaVariableNode} built from the configured values.
     * @see #build()
     */
    public UaVariableNode buildAndAdd() {
      UaVariableNode node = build();
      context.getNodeManager().addNode(node);
      return node;
    }

    public UaVariableNodeBuilder setNodeId(NodeId nodeId) {
      this.nodeId = nodeId;
      return this;
    }

    public UaVariableNodeBuilder setBrowseName(QualifiedName browseName) {
      this.browseName = browseName;
      return this;
    }

    public UaVariableNodeBuilder setDisplayName(LocalizedText displayName) {
      this.displayName = displayName;
      return this;
    }

    public UaVariableNodeBuilder setDescription(LocalizedText description) {
      this.description = description;
      return this;
    }

    public UaVariableNodeBuilder setWriteMask(UInteger writeMask) {
      this.writeMask = writeMask;
      return this;
    }

    public UaVariableNodeBuilder setUserWriteMask(UInteger userWriteMask) {
      this.userWriteMask = userWriteMask;
      return this;
    }

    public UaVariableNodeBuilder setRolePermissions(RolePermissionType[] rolePermissions) {
      this.rolePermissions = rolePermissions;
      return this;
    }

    public UaVariableNodeBuilder setUserRolePermissions(RolePermissionType[] userRolePermissions) {
      this.userRolePermissions = userRolePermissions;
      return this;
    }

    public UaVariableNodeBuilder setAccessRestrictions(AccessRestrictionType accessRestrictions) {
      this.accessRestrictions = accessRestrictions;
      return this;
    }

    public UaVariableNodeBuilder setValue(DataValue value) {
      this.value = value;
      return this;
    }

    public UaVariableNodeBuilder setDataType(NodeId dataType) {
      this.dataType = dataType;
      return this;
    }

    public UaVariableNodeBuilder setValueRank(int valueRank) {
      this.valueRank = valueRank;
      return this;
    }

    public UaVariableNodeBuilder setArrayDimensions(UInteger[] arrayDimensions) {
      this.arrayDimensions = arrayDimensions;
      return this;
    }

    public UaVariableNodeBuilder setAccessLevel(UByte accessLevel) {
      this.accessLevel = accessLevel;
      return this;
    }

    public UaVariableNodeBuilder setAccessLevel(AccessLevel... accessLevel) {
      setAccessLevel(AccessLevel.toValue(accessLevel));
      return this;
    }

    public UaVariableNodeBuilder setAccessLevel(Set<AccessLevel> accessLevel) {
      setAccessLevel(AccessLevel.toValue(accessLevel));
      return this;
    }

    public UaVariableNodeBuilder setUserAccessLevel(UByte userAccessLevel) {
      this.userAccessLevel = userAccessLevel;
      return this;
    }

    public UaVariableNodeBuilder setUserAccessLevel(AccessLevel... accessLevel) {
      setUserAccessLevel(AccessLevel.toValue(accessLevel));
      return this;
    }

    public UaVariableNodeBuilder setUserAccessLevel(Set<AccessLevel> accessLevel) {
      setUserAccessLevel(AccessLevel.toValue(accessLevel));
      return this;
    }

    public UaVariableNodeBuilder setMinimumSamplingInterval(Double minimumSamplingInterval) {
      this.minimumSamplingInterval = minimumSamplingInterval;
      return this;
    }

    public UaVariableNodeBuilder setHistorizing(boolean historizing) {
      this.historizing = historizing;
      return this;
    }

    public UaVariableNodeBuilder setAccessLevelEx(AccessLevelExType accessLevelEx) {
      this.accessLevelEx = accessLevelEx;
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

    public int getValueRank() {
      return valueRank;
    }

    public UInteger[] getArrayDimensions() {
      return arrayDimensions;
    }

    public UByte getAccessLevel() {
      return accessLevel;
    }

    public UByte getUserAccessLevel() {
      return userAccessLevel;
    }

    public Double getMinimumSamplingInterval() {
      return minimumSamplingInterval;
    }

    public boolean getHistorizing() {
      return historizing;
    }

    public AccessLevelExType getAccessLevelEx() {
      return accessLevelEx;
    }

    /**
     * Add an {@link AttributeFilter} that will be added to the node's {@link AttributeFilterChain}
     * when it's built.
     *
     * <p>The order filters are added in this builder is maintained.
     *
     * @param attributeFilter the {@link AttributeFilter} to add.
     * @return this {@link UaVariableNodeBuilder}.
     */
    public UaVariableNodeBuilder addAttributeFilter(AttributeFilter attributeFilter) {
      attributeFilters.add(attributeFilter);
      return this;
    }

    /**
     * Add a {@link Reference} to the node when it's built.
     *
     * @param reference the {@link Reference} to add.
     * @return this {@link UaVariableNodeBuilder}.
     */
    public UaVariableNodeBuilder addReference(Reference reference) {
      references.add(reference);
      return this;
    }

    /**
     * Convenience method for adding the required HasTypeDefinition reference.
     *
     * <p>{@link #setNodeId(NodeId)} must have already been called before invoking this method.
     *
     * @param typeDefinition The {@link NodeId} of the TypeDefinition.
     * @return this {@link UaVariableNodeBuilder}.
     */
    public UaVariableNodeBuilder setTypeDefinition(NodeId typeDefinition) {
      Objects.requireNonNull(nodeId, "NodeId cannot be null");

      references.add(
          new Reference(nodeId, NodeIds.HasTypeDefinition, typeDefinition.expanded(), true));

      return this;
    }
  }
}

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

import org.eclipse.milo.opcua.sdk.core.nodes.VariableNodeProperties;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableTypeNode;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableTypeNodeProperties;
import org.eclipse.milo.opcua.stack.core.AttributeId;
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
    switch (attributeId) {
      case Value:
        return value;

      case DataType:
        return dataType;

      case ValueRank:
        return valueRank;

      case ArrayDimensions:
        return arrayDimensions;

      case IsAbstract:
        return isAbstract;

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
}

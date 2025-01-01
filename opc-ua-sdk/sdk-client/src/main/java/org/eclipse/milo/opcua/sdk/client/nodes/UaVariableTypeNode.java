/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.nodes;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableTypeNode;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableTypeNodeProperties;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
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
      OpcUaClient client,
      NodeId nodeId,
      NodeClass nodeClass,
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
        client, nodeId, nodeClass, browseName, displayName, description, writeMask, userWriteMask);

    this.value = value;
    this.dataType = dataType;
    this.valueRank = valueRank;
    this.arrayDimensions = arrayDimensions;
    this.isAbstract = isAbstract;
  }

  /**
   * Construct a {@link UaVariableTypeNode} using all attributes, including those defined by OPC UA
   * 1.04.
   */
  public UaVariableTypeNode(
      OpcUaClient client,
      NodeId nodeId,
      NodeClass nodeClass,
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
        client,
        nodeId,
        nodeClass,
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

  /**
   * {@inheritDoc}
   *
   * <p>The returned attribute is the most recently seen value; it is not read live from the server.
   *
   * @see #readValue()
   */
  @Override
  public synchronized DataValue getValue() {
    return value;
  }

  /**
   * {@inheritDoc}
   *
   * <p>The returned attribute is the most recently seen value; it is not read live from the server.
   *
   * @see #readDataType()
   */
  @Override
  public synchronized NodeId getDataType() {
    return dataType;
  }

  /**
   * {@inheritDoc}
   *
   * <p>The returned attribute is the most recently seen value; it is not read live from the server.
   *
   * @see #readValueRank()
   */
  @Override
  public synchronized Integer getValueRank() {
    return valueRank;
  }

  /**
   * {@inheritDoc}
   *
   * <p>The returned attribute is the most recently seen value; it is not read live from the server.
   *
   * @see #readArrayDimensions()
   */
  @Override
  public synchronized UInteger[] getArrayDimensions() {
    return arrayDimensions;
  }

  /**
   * {@inheritDoc}
   *
   * <p>The returned attribute is the most recently seen value; it is not read live from the server.
   *
   * @see #readIsAbstract()
   */
  @Override
  public synchronized Boolean getIsAbstract() {
    return this.isAbstract;
  }

  /**
   * {@inheritDoc}
   *
   * <p>The attribute is only updated locally; it is not written to the server.
   *
   * @see #writeValue(DataValue)
   */
  @Override
  public synchronized void setValue(DataValue value) {
    this.value = value;
  }

  /**
   * Set the local Value attribute to a {@link DataValue} containing {@code variant} with no
   * StatusCode or timestamp values.
   *
   * @param variant the {@link Variant} to set.
   * @see #setValue(DataValue)
   */
  public synchronized void setValue(Variant variant) {
    setValue(DataValue.valueOnly(variant));
  }

  /**
   * {@inheritDoc}
   *
   * <p>The attribute is only updated locally; it is not written to the server.
   *
   * @see #writeDataType(NodeId)
   */
  @Override
  public synchronized void setDataType(NodeId dataType) {
    this.dataType = dataType;
  }

  /**
   * {@inheritDoc}
   *
   * <p>The attribute is only updated locally; it is not written to the server.
   *
   * @see #writeValueRank(Integer)
   */
  @Override
  public synchronized void setValueRank(Integer valueRank) {
    this.valueRank = valueRank;
  }

  /**
   * {@inheritDoc}
   *
   * <p>The attribute is only updated locally; it is not written to the server.
   *
   * @see #writeArrayDimensions(UInteger[])
   */
  @Override
  public synchronized void setArrayDimensions(UInteger[] arrayDimensions) {
    this.arrayDimensions = arrayDimensions;
  }

  /**
   * {@inheritDoc}
   *
   * <p>The attribute is only updated locally; it is not written to the server.
   *
   * @see #writeIsAbstract(Boolean)
   */
  @Override
  public synchronized void setIsAbstract(Boolean isAbstract) {
    this.isAbstract = isAbstract;
  }

  /**
   * Read the Value attribute for this Node from the server and update the local attribute.
   *
   * <p>Unlike reads for other attributes this call does not fail if the resulting {@link DataValue}
   * is not good quality and the local attribute value is updated without regard for status.
   *
   * @return the {@link DataValue} read from the server.
   * @throws UaException if a service-level error occurs.
   */
  public DataValue readValue() throws UaException {
    DataValue value = readAttribute(AttributeId.Value);
    setValue(value);
    return value;
  }

  /**
   * Read the DataType attribute for this Node from the server and update the local attribute if the
   * operation succeeds.
   *
   * @return the {@link NodeId} read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  public NodeId readDataType() throws UaException {
    DataValue value = readAttribute(AttributeId.DataType);

    StatusCode statusCode = value.getStatusCode();

    if (statusCode != null && statusCode.isBad()) {
      throw new UaException(statusCode, "read DataType failed");
    } else {
      NodeId dataType = (NodeId) value.getValue().getValue();
      setDataType(dataType);
      return dataType;
    }
  }

  /**
   * Read the ValueRank attribute for this Node from the server and update the local attribute if
   * the operation succeeds.
   *
   * @return the {@link Integer} read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  public Integer readValueRank() throws UaException {
    DataValue value = readAttribute(AttributeId.ValueRank);

    StatusCode statusCode = value.getStatusCode();

    if (statusCode != null && statusCode.isBad()) {
      throw new UaException(statusCode, "read ValueRank failed");
    } else {
      Integer valueRank = (Integer) value.getValue().getValue();
      setValueRank(valueRank);
      return valueRank;
    }
  }

  /**
   * Read the ArrayDimensions attribute for this Node from the server and update the local attribute
   * if the operation succeeds.
   *
   * <p>ArrayDimensions is an optional attribute. If the operation fails with {@link
   * StatusCodes#Bad_AttributeIdInvalid} a {@code null} value is returned instead.
   *
   * <p>Note that {@code null} is an expected value for the ArrayDimensions attribute in some cases
   * and this call does not distinguish between the attribute existing with a null value and the
   * attribute not existing.
   *
   * @return the {@link UInteger} array read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  @Nullable
  public UInteger[] readArrayDimensions() throws UaException {
    DataValue value = readAttribute(AttributeId.ArrayDimensions);
    StatusCode statusCode = value.getStatusCode();

    if (statusCode != null
        && statusCode.isBad()
        && statusCode.getValue() != StatusCodes.Bad_AttributeIdInvalid) {

      throw new UaException(statusCode, "read ArrayDimensions failed");
    } else {
      UInteger[] arrayDimensions = (UInteger[]) value.getValue().getValue();
      setArrayDimensions(arrayDimensions);
      return arrayDimensions;
    }
  }

  /**
   * Read the IsAbstract attribute for this Node from the server and update the local attribute if
   * the operation succeeds.
   *
   * @return the {@link Boolean} read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  public Boolean readIsAbstract() throws UaException {
    DataValue value = readAttribute(AttributeId.IsAbstract);

    StatusCode statusCode = value.getStatusCode();

    if (statusCode != null && statusCode.isBad()) {
      throw new UaException(statusCode, "read IsAbstract failed");
    } else {
      Boolean isAbstract = (Boolean) value.getValue().getValue();
      setIsAbstract(isAbstract);
      return isAbstract;
    }
  }

  /**
   * Write a new Value attribute for this Node to the server and update the local attribute if the
   * operation succeeds.
   *
   * @param value the {@link DataValue} to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  public void writeValue(DataValue value) throws UaException {
    StatusCode statusCode = writeAttribute(AttributeId.Value, value);

    if (statusCode != null && statusCode.isBad()) {
      throw new UaException(statusCode, "write Value failed");
    } else {
      setValue(value);
    }
  }

  /**
   * Write a new Value attribute for this Node to the server and update the local attribute if the
   * operation succeeds.
   *
   * <p>This overload creates a {@link DataValue} with no StatusCode or timestamp values.
   *
   * @param variant the {@link Variant} to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  public void writeValue(Variant variant) throws UaException {
    writeValue(DataValue.valueOnly(variant));
  }

  /**
   * Write a new DataType attribute for this Node to the server and update the local attribute if
   * the operation succeeds.
   *
   * @param dataType the {@link NodeId} to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  public void writeDataType(NodeId dataType) throws UaException {
    DataValue value = DataValue.valueOnly(new Variant(dataType));
    StatusCode statusCode = writeAttribute(AttributeId.DataType, value);

    if (statusCode != null && statusCode.isBad()) {
      throw new UaException(statusCode, "write DataType failed");
    } else {
      setDataType(dataType);
    }
  }

  /**
   * Write a new ValueRank attribute for this Node to the server and update the local attribute if
   * the operation succeeds.
   *
   * @param valueRank the {@link Integer} to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  public void writeValueRank(Integer valueRank) throws UaException {
    DataValue value = DataValue.valueOnly(new Variant(valueRank));
    StatusCode statusCode = writeAttribute(AttributeId.ValueRank, value);

    if (statusCode != null && statusCode.isBad()) {
      throw new UaException(statusCode, "write ValueRank failed");
    } else {
      setValueRank(valueRank);
    }
  }

  /**
   * Write a new ArrayDimensions attribute for this Node to the server and update the local
   * attribute if the operation succeeds.
   *
   * @param arrayDimensions the {@link UInteger} array to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  public void writeArrayDimensions(UInteger[] arrayDimensions) throws UaException {
    DataValue value = DataValue.valueOnly(new Variant(arrayDimensions));
    StatusCode statusCode = writeAttribute(AttributeId.ArrayDimensions, value);

    if (statusCode != null && statusCode.isBad()) {
      throw new UaException(statusCode, "write ArrayDimensions failed");
    } else {
      setArrayDimensions(arrayDimensions);
    }
  }

  /**
   * Write a new IsAbstract attribute for this Node to the server and update the local attribute if
   * the operation succeeds.
   *
   * @param isAbstract the {@link Boolean} to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  public void writeIsAbstract(Boolean isAbstract) throws UaException {
    DataValue value = DataValue.valueOnly(new Variant(isAbstract));
    StatusCode statusCode = writeAttribute(AttributeId.IsAbstract, value);

    if (statusCode != null && statusCode.isBad()) {
      throw new UaException(statusCode, "write IsAbstract failed");
    } else {
      setIsAbstract(isAbstract);
    }
  }

  /**
   * Get the value of the {@link VariableTypeNodeProperties#NodeVersion} Property, if it exists.
   *
   * @return the value of the NodeVersion Property, if it exists.
   * @see VariableTypeNodeProperties
   */
  public CompletableFuture<? extends String> readNodeVersionAsync() {
    return getProperty(VariableTypeNodeProperties.NodeVersion);
  }

  /**
   * Set the value of the {@link VariableTypeNodeProperties#NodeVersion} Property, if it exists.
   *
   * @param nodeVersion the value to set.
   * @return a {@link CompletableFuture} that completes with the {@link StatusCode} of the write
   *     operation.
   * @see VariableTypeNodeProperties
   */
  public CompletableFuture<StatusCode> writeNodeVersionAsync(String nodeVersion) {
    return setProperty(VariableTypeNodeProperties.NodeVersion, nodeVersion);
  }

  @Override
  protected DataValue getAttributeValue(AttributeId attributeId) {
    switch (attributeId) {
      case Value:
        return DataValue.valueOnly(new Variant(getValue().getValue().getValue()));
      case DataType:
        return DataValue.valueOnly(new Variant(getDataType()));
      case ValueRank:
        return DataValue.valueOnly(new Variant(getValueRank()));
      case ArrayDimensions:
        return DataValue.valueOnly(new Variant(getArrayDimensions()));
      case IsAbstract:
        return DataValue.valueOnly(new Variant(getIsAbstract()));
      default:
        return super.getAttributeValue(attributeId);
    }
  }

  @Override
  protected void setAttributeValue(AttributeId attributeId, DataValue value) {
    switch (attributeId) {
      case Value:
        {
          setValue(value);
          break;
        }
      case DataType:
        {
          setDataType((NodeId) value.getValue().getValue());
          break;
        }
      case ValueRank:
        {
          setValueRank((Integer) value.getValue().getValue());
          break;
        }
      case ArrayDimensions:
        {
          setArrayDimensions((UInteger[]) value.getValue().getValue());
          break;
        }
      case IsAbstract:
        {
          setIsAbstract((Boolean) value.getValue().getValue());
          break;
        }
      default:
        {
          super.setAttributeValue(attributeId, value);
        }
    }
  }
}

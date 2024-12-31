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
import org.eclipse.milo.opcua.sdk.core.nodes.ObjectTypeNode;
import org.eclipse.milo.opcua.sdk.core.nodes.ObjectTypeNodeProperties;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
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

public class UaObjectTypeNode extends UaNode implements ObjectTypeNode {

  private Boolean isAbstract;

  /** Construct a {@link UaObjectTypeNode} using only attributes defined prior to OPC UA 1.04. */
  public UaObjectTypeNode(
      OpcUaClient client,
      NodeId nodeId,
      NodeClass nodeClass,
      QualifiedName browseName,
      LocalizedText displayName,
      LocalizedText description,
      UInteger writeMask,
      UInteger userWriteMask,
      Boolean isAbstract) {

    super(
        client, nodeId, nodeClass, browseName, displayName, description, writeMask, userWriteMask);

    this.isAbstract = isAbstract;
  }

  /**
   * Construct a {@link UaObjectTypeNode} using all attributes, including those defined by OPC UA
   * 1.04.
   */
  public UaObjectTypeNode(
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

    this.isAbstract = isAbstract;
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
    return isAbstract;
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
   * Read the IsAbstract attribute for this Node from the server and update the local attribute if
   * the operation succeeds.
   *
   * @return the {@link Boolean} read from the server.
   * @throws UaException if a service- or operation-level error occurs.s
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
   * Get the value of the {@link ObjectTypeNodeProperties#NodeVersion} Property, if it exists.
   *
   * @return the value of the NodeVersion Property, if it exists.
   * @see ObjectTypeNodeProperties
   */
  public CompletableFuture<? extends String> readNodeVersionAsync() {
    return getProperty(ObjectTypeNodeProperties.NodeVersion);
  }

  /**
   * Get the value of the {@link ObjectTypeNodeProperties#Icon} Property, if it exists.
   *
   * @return the value of the Icon Property, if it exists.
   * @see ObjectTypeNodeProperties
   */
  public CompletableFuture<? extends ByteString> readIconAsync() {
    return getProperty(ObjectTypeNodeProperties.Icon);
  }

  /**
   * Set the value of the {@link ObjectTypeNodeProperties#NodeVersion} Property, if it exists.
   *
   * @param nodeVersion the value to set.
   * @return a {@link CompletableFuture} that completes with the {@link StatusCode} of the write
   *     operation.
   * @see ObjectTypeNodeProperties
   */
  public CompletableFuture<StatusCode> writeNodeVersionAsync(String nodeVersion) {
    return setProperty(ObjectTypeNodeProperties.NodeVersion, nodeVersion);
  }

  /**
   * Set the value of the {@link ObjectTypeNodeProperties#Icon} Property, if it exists.
   *
   * @param icon the value to set.
   * @return a {@link CompletableFuture} that completes with the {@link StatusCode} of the write
   *     operation.
   * @see ObjectTypeNodeProperties
   */
  public CompletableFuture<StatusCode> writeIconAsync(ByteString icon) {
    return setProperty(ObjectTypeNodeProperties.Icon, icon);
  }

  @Override
  protected DataValue getAttributeValue(AttributeId attributeId) {
    switch (attributeId) {
      case IsAbstract:
        return DataValue.valueOnly(new Variant(getIsAbstract()));
      default:
        return super.getAttributeValue(attributeId);
    }
  }

  @Override
  protected void setAttributeValue(AttributeId attributeId, DataValue value) {
    switch (attributeId) {
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

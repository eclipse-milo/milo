/*
 * Copyright (c) 2025 the Eclipse Milo Authors
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
import org.eclipse.milo.opcua.sdk.core.nodes.ReferenceTypeNode;
import org.eclipse.milo.opcua.sdk.core.nodes.ReferenceTypeNodeProperties;
import org.eclipse.milo.opcua.stack.core.AttributeId;
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

public class UaReferenceTypeNode extends UaNode implements ReferenceTypeNode {

  private Boolean isAbstract;
  private Boolean symmetric;
  private LocalizedText inverseName;

  /** Construct a {@link UaReferenceTypeNode} using only attributes defined prior to OPC UA 1.04. */
  public UaReferenceTypeNode(
      OpcUaClient client,
      NodeId nodeId,
      NodeClass nodeClass,
      QualifiedName browseName,
      LocalizedText displayName,
      LocalizedText description,
      UInteger writeMask,
      UInteger userWriteMask,
      Boolean isAbstract,
      Boolean symmetric,
      LocalizedText inverseName) {

    super(
        client, nodeId, nodeClass, browseName, displayName, description, writeMask, userWriteMask);

    this.isAbstract = isAbstract;
    this.symmetric = symmetric;
    this.inverseName = inverseName;
  }

  /**
   * Construct a {@link UaReferenceTypeNode} using all attributes, including those defined by OPC UA
   * 1.04.
   */
  public UaReferenceTypeNode(
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
      Boolean isAbstract,
      Boolean symmetric,
      LocalizedText inverseName) {

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
    this.symmetric = symmetric;
    this.inverseName = inverseName;
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
   * <p>The returned attribute is the most recently seen value; it is not read live from the server.
   *
   * @see #readSymmetric()
   */
  @Override
  public synchronized Boolean getSymmetric() {
    return symmetric;
  }

  /**
   * {@inheritDoc}
   *
   * <p>The returned attribute is the most recently seen value; it is not read live from the server.
   *
   * @see #readInverseName()
   */
  @Override
  public synchronized LocalizedText getInverseName() {
    return inverseName;
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
   * {@inheritDoc}
   *
   * <p>The attribute is only updated locally; it is not written to the server.
   *
   * @see #writeSymmetric(Boolean)
   */
  @Override
  public synchronized void setSymmetric(Boolean symmetric) {
    this.symmetric = symmetric;
  }

  /**
   * {@inheritDoc}
   *
   * <p>The attribute is only updated locally; it is not written to the server.
   *
   * @see #writeInverseName(LocalizedText)
   */
  @Override
  public synchronized void setInverseName(LocalizedText inverseName) {
    this.inverseName = inverseName;
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

    StatusCode statusCode = value.statusCode();

    if (statusCode != null && statusCode.isBad()) {
      throw new UaException(statusCode, "read IsAbstract failed");
    } else {
      Boolean isAbstract = (Boolean) value.value().value();
      setIsAbstract(isAbstract);
      return isAbstract;
    }
  }

  /**
   * Read the Symmetric attribute for this Node from the server and update the local attribute if
   * the operation succeeds.
   *
   * @return the {@link Boolean} read from the server.
   * @throws UaException if a service- or operation-level error occurs.s
   */
  public Boolean readSymmetric() throws UaException {
    DataValue value = readAttribute(AttributeId.Symmetric);

    StatusCode statusCode = value.statusCode();

    if (statusCode != null && statusCode.isBad()) {
      throw new UaException(statusCode, "read Symmetric failed");
    } else {
      Boolean symmetric = (Boolean) value.value().value();
      setSymmetric(symmetric);
      return symmetric;
    }
  }

  /**
   * Read the InverseName attribute for this Node from the server and update the local attribute if
   * the operation succeeds.
   *
   * @return the {@link LocalizedText} read from the server.
   * @throws UaException if a service- or operation-level error occurs.s
   */
  public LocalizedText readInverseName() throws UaException {
    DataValue value = readAttribute(AttributeId.InverseName);

    StatusCode statusCode = value.statusCode();

    if (statusCode != null && statusCode.isBad()) {
      throw new UaException(statusCode, "read InverseName failed");
    } else {
      LocalizedText inverseName = (LocalizedText) value.value().value();
      setInverseName(inverseName);
      return inverseName;
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
   * Write a new Symmetric attribute for this Node to the server and update the local attribute if
   * the operation succeeds.
   *
   * @param symmetric the {@link Boolean} to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  public void writeSymmetric(Boolean symmetric) throws UaException {
    DataValue value = DataValue.valueOnly(new Variant(symmetric));
    StatusCode statusCode = writeAttribute(AttributeId.Symmetric, value);

    if (statusCode != null && statusCode.isBad()) {
      throw new UaException(statusCode, "write Symmetric failed");
    } else {
      setSymmetric(symmetric);
    }
  }

  /**
   * Write a new InverseName attribute for this Node to the server and update the local attribute if
   * the operation succeeds.
   *
   * @param inverseName the {@link LocalizedText} to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  public void writeInverseName(LocalizedText inverseName) throws UaException {
    DataValue value = DataValue.valueOnly(new Variant(inverseName));
    StatusCode statusCode = writeAttribute(AttributeId.InverseName, value);

    if (statusCode != null && statusCode.isBad()) {
      throw new UaException(statusCode, "write InverseName failed");
    } else {
      setInverseName(inverseName);
    }
  }

  /**
   * Get the value of the {@link ReferenceTypeNodeProperties#NodeVersion} Property, if it exists.
   *
   * @return the value of the NodeVersion Property, if it exists.
   * @see ReferenceTypeNodeProperties
   */
  public CompletableFuture<? extends String> readNodeVersionAsync() {
    return getProperty(ReferenceTypeNodeProperties.NodeVersion);
  }

  /**
   * Set the value of the {@link ReferenceTypeNodeProperties#NodeVersion} Property, if it exists.
   *
   * @param nodeVersion the value to set.
   * @return a {@link CompletableFuture} that completes with the {@link StatusCode} of the write
   *     operation.
   * @see ReferenceTypeNodeProperties
   */
  public CompletableFuture<StatusCode> writeNodeVersionAsync(String nodeVersion) {
    return setProperty(ReferenceTypeNodeProperties.NodeVersion, nodeVersion);
  }

  @Override
  protected DataValue getAttributeValue(AttributeId attributeId) {
    switch (attributeId) {
      case IsAbstract:
        return DataValue.valueOnly(new Variant(getIsAbstract()));
      case Symmetric:
        return DataValue.valueOnly(new Variant(getSymmetric()));
      case InverseName:
        return DataValue.valueOnly(new Variant(getInverseName()));
      default:
        return super.getAttributeValue(attributeId);
    }
  }

  @Override
  protected void setAttributeValue(AttributeId attributeId, DataValue value) {
    switch (attributeId) {
      case IsAbstract:
        {
          setIsAbstract((Boolean) value.value().value());
          break;
        }
      case Symmetric:
        {
          setSymmetric((Boolean) value.value().value());
          break;
        }
      case InverseName:
        {
          setInverseName((LocalizedText) value.value().value());
          break;
        }
      default:
        {
          super.setAttributeValue(attributeId, value);
        }
    }
  }
}

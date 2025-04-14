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
import org.eclipse.milo.opcua.sdk.core.nodes.ViewNode;
import org.eclipse.milo.opcua.sdk.core.nodes.ViewNodeProperties;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class UaViewNode extends UaNode implements ViewNode {

  private Boolean containsNoLoops;
  private UByte eventNotifier;

  /** Construct a {@link UaViewNode} using only attributes defined prior to OPC UA 1.04. */
  public UaViewNode(
      OpcUaClient client,
      NodeId nodeId,
      NodeClass nodeClass,
      QualifiedName browseName,
      LocalizedText displayName,
      LocalizedText description,
      UInteger writeMask,
      UInteger userWriteMask,
      Boolean containsNoLoops,
      UByte eventNotifier) {

    super(
        client, nodeId, nodeClass, browseName, displayName, description, writeMask, userWriteMask);

    this.containsNoLoops = containsNoLoops;
    this.eventNotifier = eventNotifier;
  }

  /**
   * Construct a {@link UaViewNode} using all attributes, including those defined by OPC UA 1.04.
   */
  public UaViewNode(
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
      Boolean containsNoLoops,
      UByte eventNotifier) {

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

    this.containsNoLoops = containsNoLoops;
    this.eventNotifier = eventNotifier;
  }

  /**
   * {@inheritDoc}
   *
   * <p>The returned attribute is the most recently seen value; it is not read live from the server.
   *
   * @see #readContainsNoLoops()
   */
  @Override
  public synchronized Boolean getContainsNoLoops() {
    return containsNoLoops;
  }

  /**
   * {@inheritDoc}
   *
   * <p>The returned attribute is the most recently seen value; it is not read live from the server.
   *
   * @see #readEventNotifier()
   */
  @Override
  public synchronized UByte getEventNotifier() {
    return eventNotifier;
  }

  /**
   * {@inheritDoc}
   *
   * <p>The attribute is only updated locally; it is not written to the server.
   *
   * @see #writeContainsNoLoops(Boolean)
   */
  @Override
  public synchronized void setContainsNoLoops(Boolean containsNoLoops) {
    this.containsNoLoops = containsNoLoops;
  }

  /**
   * {@inheritDoc}
   *
   * <p>The attribute is only updated locally; it is not written to the server.
   *
   * @see #writeEventNotifier(UByte)
   */
  @Override
  public synchronized void setEventNotifier(UByte eventNotifier) {
    this.eventNotifier = eventNotifier;
  }

  /**
   * Read the ContainsNoLoops attribute for this Node from the server and update the local attribute
   * if the operation succeeds.
   *
   * @return the {@link Boolean} read from the server.
   * @throws UaException if a service- or operation-level error occurs.s
   */
  public Boolean readContainsNoLoops() throws UaException {
    DataValue value = readAttribute(AttributeId.ContainsNoLoops);

    StatusCode statusCode = value.statusCode();

    if (statusCode.isBad()) {
      throw new UaException(statusCode, "read ContainsNoLoops failed");
    } else {
      Boolean containsNoLoops = (Boolean) value.value().value();
      setContainsNoLoops(containsNoLoops);
      return containsNoLoops;
    }
  }

  /**
   * Read the EventNotifier attribute for this Node from the server and update the local attribute
   * if the operation succeeds.
   *
   * @return the {@link UByte} read from the server.
   * @throws UaException if a service- or operation-level error occurs.s
   */
  public UByte readEventNotifier() throws UaException {
    DataValue value = readAttribute(AttributeId.EventNotifier);

    StatusCode statusCode = value.statusCode();

    if (statusCode.isBad()) {
      throw new UaException(statusCode, "read EventNotifier failed");
    } else {
      UByte eventNotifier = (UByte) value.value().value();
      setEventNotifier(eventNotifier);
      return eventNotifier;
    }
  }

  /**
   * Write a new ContainsNoLoops attribute for this Node to the server and update the local
   * attribute if the operation succeeds.
   *
   * @param containsNoLoops the {@link Boolean} to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  public void writeContainsNoLoops(Boolean containsNoLoops) throws UaException {
    DataValue value = DataValue.valueOnly(new Variant(containsNoLoops));
    StatusCode statusCode = writeAttribute(AttributeId.ContainsNoLoops, value);

    if (statusCode != null && statusCode.isBad()) {
      throw new UaException(statusCode, "write ContainsNoLoops failed");
    } else {
      setContainsNoLoops(containsNoLoops);
    }
  }

  /**
   * Write a new EventNotifier attribute for this Node to the server and update the local attribute
   * if the operation succeeds.
   *
   * @param eventNotifier the {@link UByte} to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  public void writeEventNotifier(UByte eventNotifier) throws UaException {
    DataValue value = DataValue.valueOnly(new Variant(eventNotifier));
    StatusCode statusCode = writeAttribute(AttributeId.EventNotifier, value);

    if (statusCode != null && statusCode.isBad()) {
      throw new UaException(statusCode, "write EventNotifier failed");
    } else {
      setEventNotifier(eventNotifier);
    }
  }

  /**
   * Get the value of the {@link ViewNodeProperties#NodeVersion} Property, if it exists.
   *
   * @return the value of the NodeVersion Property, if it exists.
   * @see ViewNodeProperties
   */
  public CompletableFuture<? extends String> readNodeVersionAsync() {
    return getProperty(ViewNodeProperties.NodeVersion);
  }

  /**
   * Get the value of the {@link ViewNodeProperties#ViewVersion} Property, if it exists.
   *
   * @return the value of the ViewVersion Property, if it exists.
   * @see ViewNodeProperties
   */
  public CompletableFuture<? extends UInteger> readViewVersionAsync() {
    return getProperty(ViewNodeProperties.ViewVersion);
  }

  /**
   * Set the value of the {@link ViewNodeProperties#NodeVersion} Property, if it exists.
   *
   * @param nodeVersion the value to set.
   * @return a {@link CompletableFuture} that completes with the {@link StatusCode} of the write
   *     operation.
   * @see ViewNodeProperties
   */
  public CompletableFuture<StatusCode> writeNodeVersionAsync(String nodeVersion) {
    return setProperty(ViewNodeProperties.NodeVersion, nodeVersion);
  }

  /**
   * Set the value of the {@link ViewNodeProperties#ViewVersion} Property, if it exists.
   *
   * @param viewVersion the value to set.
   * @return a {@link CompletableFuture} that completes with the {@link StatusCode} of the write
   *     operation.
   * @see ViewNodeProperties
   */
  public CompletableFuture<StatusCode> writeViewVersionAsync(UInteger viewVersion) {
    return setProperty(ViewNodeProperties.ViewVersion, viewVersion);
  }

  @Override
  protected DataValue getAttributeValue(AttributeId attributeId) {
    return switch (attributeId) {
      case ContainsNoLoops -> DataValue.valueOnly(new Variant(getContainsNoLoops()));
      case EventNotifier -> DataValue.valueOnly(new Variant(getEventNotifier()));
      default -> super.getAttributeValue(attributeId);
    };
  }

  @Override
  protected void setAttributeValue(AttributeId attributeId, DataValue value) {
    switch (attributeId) {
      case ContainsNoLoops:
        {
          setContainsNoLoops((Boolean) value.value().value());
          break;
        }
      case EventNotifier:
        {
          setEventNotifier((UByte) value.value().value());
          break;
        }
      default:
        {
          super.setAttributeValue(attributeId, value);
        }
    }
  }
}

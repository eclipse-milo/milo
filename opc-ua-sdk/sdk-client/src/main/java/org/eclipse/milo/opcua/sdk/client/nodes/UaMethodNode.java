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
import org.eclipse.milo.opcua.sdk.core.nodes.MethodNode;
import org.eclipse.milo.opcua.sdk.core.nodes.MethodNodeProperties;
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
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

public class UaMethodNode extends UaNode implements MethodNode {

  private Boolean executable;
  private Boolean userExecutable;

  /** Construct a {@link UaMethodNode} using only attributes defined prior to OPC UA 1.04. */
  public UaMethodNode(
      OpcUaClient client,
      NodeId nodeId,
      NodeClass nodeClass,
      QualifiedName browseName,
      LocalizedText displayName,
      LocalizedText description,
      UInteger writeMask,
      UInteger userWriteMask,
      Boolean executable,
      Boolean userExecutable) {

    super(
        client, nodeId, nodeClass, browseName, displayName, description, writeMask, userWriteMask);

    this.executable = executable;
    this.userExecutable = userExecutable;
  }

  /**
   * Construct a {@link UaMethodNode} using all attributes, including those defined by OPC UA 1.04.
   */
  public UaMethodNode(
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
      Boolean executable,
      Boolean userExecutable) {

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

    this.executable = executable;
    this.userExecutable = userExecutable;
  }

  /**
   * {@inheritDoc}
   *
   * <p>The returned attribute is the most recently seen value; it is not read live from the server.
   *
   * @see #readExecutable()
   */
  @Override
  public synchronized Boolean isExecutable() {
    return executable;
  }

  /**
   * {@inheritDoc}
   *
   * <p>The returned attribute is the most recently seen value; it is not read live from the server.
   *
   * @see #readUserExecutable()
   */
  @Override
  public synchronized Boolean isUserExecutable() {
    return userExecutable;
  }

  /**
   * {@inheritDoc}
   *
   * <p>The attribute is only updated locally; it is not written to the server.
   *
   * @see #writeExecutable(Boolean)
   */
  @Override
  public synchronized void setExecutable(Boolean executable) {
    this.executable = executable;
  }

  /**
   * {@inheritDoc}
   *
   * <p>The attribute is only updated locally; it is not written to the server.
   *
   * @see #writeUserExecutable(Boolean)
   */
  @Override
  public synchronized void setUserExecutable(Boolean userExecutable) {
    this.userExecutable = userExecutable;
  }

  /**
   * Read the Executable attribute for this Node from the server and update the local attribute if
   * the operation succeeds.
   *
   * @return the {@link Boolean} read from the server.
   * @throws UaException if a service- or operation-level error occurs.s
   */
  public Boolean readExecutable() throws UaException {
    DataValue value = readAttribute(AttributeId.Executable);

    StatusCode statusCode = value.statusCode();

    if (statusCode.isBad()) {
      throw new UaException(statusCode, "read Executable failed");
    } else {
      Boolean executable = (Boolean) value.value().value();
      setExecutable(executable);
      return executable;
    }
  }

  /**
   * Read the UserExecutable attribute for this Node from the server and update the local attribute
   * if the operation succeeds.
   *
   * @return the {@link Boolean} read from the server.
   * @throws UaException if a service- or operation-level error occurs.s
   */
  public Boolean readUserExecutable() throws UaException {
    DataValue value = readAttribute(AttributeId.UserExecutable);

    StatusCode statusCode = value.statusCode();

    if (statusCode.isBad()) {
      throw new UaException(statusCode, "read UserExecutable failed");
    } else {
      Boolean userExecutable = (Boolean) value.value().value();
      setUserExecutable(userExecutable);
      return userExecutable;
    }
  }

  /**
   * Write a new Executable attribute for this Node to the server and update the local attribute if
   * the operation succeeds.
   *
   * @param executable the {@link Boolean} to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  public void writeExecutable(Boolean executable) throws UaException {
    DataValue value = DataValue.valueOnly(new Variant(executable));
    StatusCode statusCode = writeAttribute(AttributeId.Executable, value);

    if (statusCode != null && statusCode.isBad()) {
      throw new UaException(statusCode, "write Executable failed");
    } else {
      setExecutable(executable);
    }
  }

  /**
   * Write a new UserExecutable attribute for this Node to the server and update the local attribute
   * if the operation succeeds.
   *
   * @param userExecutable the {@link Boolean} to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  public void writeUserExecutable(Boolean userExecutable) throws UaException {
    DataValue value = DataValue.valueOnly(new Variant(userExecutable));
    StatusCode statusCode = writeAttribute(AttributeId.UserExecutable, value);

    if (statusCode != null && statusCode.isBad()) {
      throw new UaException(statusCode, "write UserExecutable failed");
    } else {
      setUserExecutable(userExecutable);
    }
  }

  /**
   * Get the value of the {@link MethodNodeProperties#NodeVersion} Property, if it exists.
   *
   * <p>The NodeVersion Property is used to indicate the version of a Node.
   *
   * <p>The NodeVersion Property is updated each time a Reference is added or deleted to the Node
   * the Property belongs to. Attribute value changes do not cause the NodeVersion to change.
   * Clients may read the NodeVersion Property or subscribe to it to determine when the structure of
   * a Node has changed.
   *
   * <p>This Property is optional. If not present, the future will be completed exceptionally with a
   * {@link UaException} indicating {@link StatusCodes#Bad_NotFound}.
   *
   * @return the value of the NodeVersion Property, if it exists.
   * @see MethodNodeProperties
   */
  public CompletableFuture<? extends String> readNodeVersionAsync() {
    return getProperty(MethodNodeProperties.NodeVersion);
  }

  /**
   * Get the value of the {@link MethodNodeProperties#InputArguments} Property, if it exists.
   *
   * <p>The InputArguments Property is used to specify the arguments that shall be used by a client
   * when calling the Method.
   *
   * <p>This Property is optional. If not present, the future will be completed exceptionally with a
   * {@link UaException} indicating {@link StatusCodes#Bad_NotFound}.
   *
   * @return the value of the InputArguments Property, if it exists.
   * @see MethodNodeProperties
   */
  public CompletableFuture<Argument[]> readInputArgumentsAsync() {
    return getProperty(MethodNodeProperties.InputArguments);
  }

  /**
   * Get the value of the {@link MethodNodeProperties#OutputArguments} Property, if it exists.
   *
   * <p>The OutputArguments Property specifies the result returned from the Method call.
   *
   * <p>This Property is optional. If not present, the future will be completed exceptionally with a
   * {@link UaException} indicating {@link StatusCodes#Bad_NotFound}.
   *
   * @return the value of the OutputArguments Property, if it exists.
   * @see MethodNodeProperties
   */
  public CompletableFuture<Argument[]> readOutputArgumentsAsync() {
    return getProperty(MethodNodeProperties.OutputArguments);
  }

  /**
   * Set the value of the {@link MethodNodeProperties#NodeVersion} Property, if it exists.
   *
   * @param nodeVersion the value to set.
   * @return a {@link CompletableFuture} that completes with the {@link StatusCode} of the write
   *     operation.
   * @see MethodNodeProperties
   */
  public CompletableFuture<StatusCode> writeNodeVersionAsync(String nodeVersion) {
    return setProperty(MethodNodeProperties.NodeVersion, nodeVersion);
  }

  /**
   * Set the value of the {@link MethodNodeProperties#InputArguments} Property, if it exists.
   *
   * @param inputArguments the value to set.
   * @return a {@link CompletableFuture} that completes with the {@link StatusCode} of the write
   *     operation.
   * @see MethodNodeProperties
   */
  public CompletableFuture<StatusCode> writeInputArgumentsAsync(Argument[] inputArguments) {
    return setProperty(MethodNodeProperties.InputArguments, inputArguments);
  }

  /**
   * Set the value of the {@link MethodNodeProperties#OutputArguments} Property, if it exists.
   *
   * @param outputArguments the value to set.
   * @return a {@link CompletableFuture} that completes with the {@link StatusCode} of the write
   *     operation.
   * @see MethodNodeProperties
   */
  public CompletableFuture<StatusCode> writeOutputArgumentsAsync(Argument[] outputArguments) {
    return setProperty(MethodNodeProperties.OutputArguments, outputArguments);
  }

  @Override
  protected DataValue getAttributeValue(AttributeId attributeId) {
    return switch (attributeId) {
      case Executable -> DataValue.valueOnly(new Variant(isExecutable()));
      case UserExecutable -> DataValue.valueOnly(new Variant(isUserExecutable()));
      default -> super.getAttributeValue(attributeId);
    };
  }

  @Override
  protected void setAttributeValue(AttributeId attributeId, DataValue value) {
    switch (attributeId) {
      case Executable:
        {
          setExecutable((Boolean) value.value().value());
          break;
        }
      case UserExecutable:
        {
          setUserExecutable((Boolean) value.value().value());
          break;
        }
      default:
        {
          super.setAttributeValue(attributeId, value);
        }
    }
  }
}

/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.27">https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.27</a>
 */
public interface AuditUpdateMethodEventType extends AuditEventType {
  QualifiedProperty<NodeId> METHOD_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MethodId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17"),
          -1,
          NodeId.class);

  QualifiedProperty<Object[]> INPUT_ARGUMENTS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "InputArguments",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=24"),
          1,
          Object[].class);

  /**
   * Get the local value of the MethodId Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the MethodId Node.
   * @throws UaException if an error occurs creating or getting the MethodId Node.
   */
  NodeId getMethodId() throws UaException;

  /**
   * Set the local value of the MethodId Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the MethodId Node.
   * @throws UaException if an error occurs creating or getting the MethodId Node.
   */
  void setMethodId(NodeId value) throws UaException;

  /**
   * Read the value of the MethodId Node from the server and update the local value if the operation
   * succeeds.
   *
   * @return the {@link NodeId} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  NodeId readMethodId() throws UaException;

  /**
   * Write a new value for the MethodId Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link NodeId} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeMethodId(NodeId value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readMethodId}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends NodeId> readMethodIdAsync();

  /**
   * An asynchronous implementation of {@link #writeMethodId}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeMethodIdAsync(NodeId value);

  /**
   * Get the MethodId {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the MethodId {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getMethodIdNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getMethodIdNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getMethodIdNodeAsync();

  /**
   * Get the local value of the InputArguments Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the InputArguments Node.
   * @throws UaException if an error occurs creating or getting the InputArguments Node.
   */
  Object[] getInputArguments() throws UaException;

  /**
   * Set the local value of the InputArguments Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the InputArguments Node.
   * @throws UaException if an error occurs creating or getting the InputArguments Node.
   */
  void setInputArguments(Object[] value) throws UaException;

  /**
   * Read the value of the InputArguments Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link Object[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Object[] readInputArguments() throws UaException;

  /**
   * Write a new value for the InputArguments Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link Object[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeInputArguments(Object[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readInputArguments}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Object[]> readInputArgumentsAsync();

  /**
   * An asynchronous implementation of {@link #writeInputArguments}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeInputArgumentsAsync(Object[] value);

  /**
   * Get the InputArguments {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the InputArguments {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getInputArgumentsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getInputArgumentsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getInputArgumentsNodeAsync();
}

/*
 * Copyright (c) 2025 the Eclipse Milo Authors
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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.26">https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.26</a>
 */
public interface AuditHistoryUpdateEventType extends AuditUpdateEventType {
  QualifiedProperty<NodeId> PARAMETER_DATA_TYPE_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ParameterDataTypeId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17"),
          -1,
          NodeId.class);

  /**
   * Get the local value of the ParameterDataTypeId Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ParameterDataTypeId Node.
   * @throws UaException if an error occurs creating or getting the ParameterDataTypeId Node.
   */
  NodeId getParameterDataTypeId() throws UaException;

  /**
   * Set the local value of the ParameterDataTypeId Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ParameterDataTypeId Node.
   * @throws UaException if an error occurs creating or getting the ParameterDataTypeId Node.
   */
  void setParameterDataTypeId(NodeId value) throws UaException;

  /**
   * Read the value of the ParameterDataTypeId Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link NodeId} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  NodeId readParameterDataTypeId() throws UaException;

  /**
   * Write a new value for the ParameterDataTypeId Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link NodeId} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeParameterDataTypeId(NodeId value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readParameterDataTypeId}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends NodeId> readParameterDataTypeIdAsync();

  /**
   * An asynchronous implementation of {@link #writeParameterDataTypeId}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeParameterDataTypeIdAsync(NodeId value);

  /**
   * Get the ParameterDataTypeId {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ParameterDataTypeId {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getParameterDataTypeIdNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getParameterDataTypeIdNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getParameterDataTypeIdNodeAsync();
}

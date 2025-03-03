/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.model.variables;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.AxisInformation;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part8/5.3.4/#5.3.4.3">https://reference.opcfoundation.org/v105/Core/docs/Part8/5.3.4/#5.3.4.3</a>
 */
public interface XYArrayItemType extends ArrayItemType {
  QualifiedProperty<AxisInformation> X_AXIS_DEFINITION =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "XAxisDefinition",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12079"),
          -1,
          AxisInformation.class);

  /**
   * Get the local value of the XAxisDefinition Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the XAxisDefinition Node.
   * @throws UaException if an error occurs creating or getting the XAxisDefinition Node.
   */
  AxisInformation getXAxisDefinition() throws UaException;

  /**
   * Set the local value of the XAxisDefinition Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the XAxisDefinition Node.
   * @throws UaException if an error occurs creating or getting the XAxisDefinition Node.
   */
  void setXAxisDefinition(AxisInformation value) throws UaException;

  /**
   * Read the value of the XAxisDefinition Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link AxisInformation} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  AxisInformation readXAxisDefinition() throws UaException;

  /**
   * Write a new value for the XAxisDefinition Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link AxisInformation} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeXAxisDefinition(AxisInformation value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readXAxisDefinition}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends AxisInformation> readXAxisDefinitionAsync();

  /**
   * An asynchronous implementation of {@link #writeXAxisDefinition}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeXAxisDefinitionAsync(AxisInformation value);

  /**
   * Get the XAxisDefinition {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the XAxisDefinition {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getXAxisDefinitionNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getXAxisDefinitionNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getXAxisDefinitionNodeAsync();
}

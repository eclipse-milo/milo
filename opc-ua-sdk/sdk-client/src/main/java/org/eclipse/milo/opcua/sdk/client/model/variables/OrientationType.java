/*
 * Copyright (c) 2024 the Eclipse Milo Authors
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
import org.eclipse.milo.opcua.stack.core.types.structured.EUInformation;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.25">https://reference.opcfoundation.org/v105/Core/docs/Part5/7.25</a>
 */
public interface OrientationType extends BaseDataVariableType {
  QualifiedProperty<EUInformation> ANGLE_UNIT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "AngleUnit",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=887"),
          -1,
          EUInformation.class);

  /**
   * Get the local value of the AngleUnit Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the AngleUnit Node.
   * @throws UaException if an error occurs creating or getting the AngleUnit Node.
   */
  EUInformation getAngleUnit() throws UaException;

  /**
   * Set the local value of the AngleUnit Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the AngleUnit Node.
   * @throws UaException if an error occurs creating or getting the AngleUnit Node.
   */
  void setAngleUnit(EUInformation value) throws UaException;

  /**
   * Read the value of the AngleUnit Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link EUInformation} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  EUInformation readAngleUnit() throws UaException;

  /**
   * Write a new value for the AngleUnit Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link EUInformation} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeAngleUnit(EUInformation value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readAngleUnit}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends EUInformation> readAngleUnitAsync();

  /**
   * An asynchronous implementation of {@link #writeAngleUnit}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeAngleUnitAsync(EUInformation value);

  /**
   * Get the AngleUnit {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the AngleUnit {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getAngleUnitNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getAngleUnitNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getAngleUnitNodeAsync();
}

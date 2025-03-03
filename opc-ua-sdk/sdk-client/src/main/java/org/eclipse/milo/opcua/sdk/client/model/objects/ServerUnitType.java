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
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ConversionLimitEnum;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part8/6.4.2/#6.4.2.3">https://reference.opcfoundation.org/v105/Core/docs/Part8/6.4.2/#6.4.2.3</a>
 */
public interface ServerUnitType extends UnitType {
  QualifiedProperty<ConversionLimitEnum> CONVERSION_LIMIT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ConversionLimit",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=32436"),
          -1,
          ConversionLimitEnum.class);

  /**
   * Get the local value of the ConversionLimit Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ConversionLimit Node.
   * @throws UaException if an error occurs creating or getting the ConversionLimit Node.
   */
  ConversionLimitEnum getConversionLimit() throws UaException;

  /**
   * Set the local value of the ConversionLimit Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ConversionLimit Node.
   * @throws UaException if an error occurs creating or getting the ConversionLimit Node.
   */
  void setConversionLimit(ConversionLimitEnum value) throws UaException;

  /**
   * Read the value of the ConversionLimit Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link ConversionLimitEnum} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  ConversionLimitEnum readConversionLimit() throws UaException;

  /**
   * Write a new value for the ConversionLimit Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link ConversionLimitEnum} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeConversionLimit(ConversionLimitEnum value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readConversionLimit}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends ConversionLimitEnum> readConversionLimitAsync();

  /**
   * An asynchronous implementation of {@link #writeConversionLimit}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeConversionLimitAsync(ConversionLimitEnum value);

  /**
   * Get the ConversionLimit {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ConversionLimit {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getConversionLimitNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getConversionLimitNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getConversionLimitNodeAsync();

  /**
   * Get the AlternativeUnits {@link BaseObjectType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the AlternativeUnits {@link BaseObjectType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseObjectType getAlternativeUnitsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getAlternativeUnitsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseObjectType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseObjectType> getAlternativeUnitsNodeAsync();

  /**
   * Get the CoherentUnit {@link UnitType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the CoherentUnit {@link UnitType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  UnitType getCoherentUnitNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getCoherentUnitNode()}.
   *
   * @return a CompletableFuture that completes successfully with the UnitType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends UnitType> getCoherentUnitNodeAsync();
}

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
import org.eclipse.milo.opcua.sdk.client.model.variables.BaseDataVariableType;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.15">https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.15</a>
 */
public interface IPriorityMappingEntryType extends BaseInterfaceType {
  /**
   * Get the local value of the MappingUri Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the MappingUri Node.
   * @throws UaException if an error occurs creating or getting the MappingUri Node.
   */
  String getMappingUri() throws UaException;

  /**
   * Set the local value of the MappingUri Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the MappingUri Node.
   * @throws UaException if an error occurs creating or getting the MappingUri Node.
   */
  void setMappingUri(String value) throws UaException;

  /**
   * Read the value of the MappingUri Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link String} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String readMappingUri() throws UaException;

  /**
   * Write a new value for the MappingUri Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link String} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeMappingUri(String value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readMappingUri}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String> readMappingUriAsync();

  /**
   * An asynchronous implementation of {@link #writeMappingUri}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeMappingUriAsync(String value);

  /**
   * Get the MappingUri {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the MappingUri {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getMappingUriNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getMappingUriNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getMappingUriNodeAsync();

  /**
   * Get the local value of the PriorityLabel Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the PriorityLabel Node.
   * @throws UaException if an error occurs creating or getting the PriorityLabel Node.
   */
  String getPriorityLabel() throws UaException;

  /**
   * Set the local value of the PriorityLabel Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the PriorityLabel Node.
   * @throws UaException if an error occurs creating or getting the PriorityLabel Node.
   */
  void setPriorityLabel(String value) throws UaException;

  /**
   * Read the value of the PriorityLabel Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link String} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String readPriorityLabel() throws UaException;

  /**
   * Write a new value for the PriorityLabel Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link String} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writePriorityLabel(String value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readPriorityLabel}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String> readPriorityLabelAsync();

  /**
   * An asynchronous implementation of {@link #writePriorityLabel}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writePriorityLabelAsync(String value);

  /**
   * Get the PriorityLabel {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the PriorityLabel {@link BaseDataVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getPriorityLabelNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getPriorityLabelNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getPriorityLabelNodeAsync();

  /**
   * Get the local value of the PriorityValue_PCP Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the PriorityValue_PCP Node.
   * @throws UaException if an error occurs creating or getting the PriorityValue_PCP Node.
   */
  UByte getPriorityValuePcp() throws UaException;

  /**
   * Set the local value of the PriorityValue_PCP Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the PriorityValue_PCP Node.
   * @throws UaException if an error occurs creating or getting the PriorityValue_PCP Node.
   */
  void setPriorityValuePcp(UByte value) throws UaException;

  /**
   * Read the value of the PriorityValue_PCP Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link UByte} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UByte readPriorityValuePcp() throws UaException;

  /**
   * Write a new value for the PriorityValue_PCP Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link UByte} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writePriorityValuePcp(UByte value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readPriorityValuePcp}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UByte> readPriorityValuePcpAsync();

  /**
   * An asynchronous implementation of {@link #writePriorityValuePcp}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writePriorityValuePcpAsync(UByte value);

  /**
   * Get the PriorityValue_PCP {@link BaseDataVariableType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the PriorityValue_PCP {@link BaseDataVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getPriorityValuePcpNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getPriorityValuePcpNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getPriorityValuePcpNodeAsync();

  /**
   * Get the local value of the PriorityValue_DSCP Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the PriorityValue_DSCP Node.
   * @throws UaException if an error occurs creating or getting the PriorityValue_DSCP Node.
   */
  UInteger getPriorityValueDscp() throws UaException;

  /**
   * Set the local value of the PriorityValue_DSCP Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the PriorityValue_DSCP Node.
   * @throws UaException if an error occurs creating or getting the PriorityValue_DSCP Node.
   */
  void setPriorityValueDscp(UInteger value) throws UaException;

  /**
   * Read the value of the PriorityValue_DSCP Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readPriorityValueDscp() throws UaException;

  /**
   * Write a new value for the PriorityValue_DSCP Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writePriorityValueDscp(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readPriorityValueDscp}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readPriorityValueDscpAsync();

  /**
   * An asynchronous implementation of {@link #writePriorityValueDscp}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writePriorityValueDscpAsync(UInteger value);

  /**
   * Get the PriorityValue_DSCP {@link BaseDataVariableType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the PriorityValue_DSCP {@link BaseDataVariableType} Node, or {@code null} if it does
   *     not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getPriorityValueDscpNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getPriorityValueDscpNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getPriorityValueDscpNodeAsync();
}

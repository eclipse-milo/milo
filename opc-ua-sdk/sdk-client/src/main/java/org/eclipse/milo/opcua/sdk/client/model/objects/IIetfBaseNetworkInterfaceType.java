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
import org.eclipse.milo.opcua.sdk.client.model.variables.AnalogUnitType;
import org.eclipse.milo.opcua.sdk.client.model.variables.BaseDataVariableType;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.enumerated.InterfaceAdminStatus;
import org.eclipse.milo.opcua.stack.core.types.enumerated.InterfaceOperStatus;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.1">https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.1</a>
 */
public interface IIetfBaseNetworkInterfaceType extends BaseInterfaceType {
  /**
   * Get the local value of the AdminStatus Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the AdminStatus Node.
   * @throws UaException if an error occurs creating or getting the AdminStatus Node.
   */
  InterfaceAdminStatus getAdminStatus() throws UaException;

  /**
   * Set the local value of the AdminStatus Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the AdminStatus Node.
   * @throws UaException if an error occurs creating or getting the AdminStatus Node.
   */
  void setAdminStatus(InterfaceAdminStatus value) throws UaException;

  /**
   * Read the value of the AdminStatus Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link InterfaceAdminStatus} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  InterfaceAdminStatus readAdminStatus() throws UaException;

  /**
   * Write a new value for the AdminStatus Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link InterfaceAdminStatus} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeAdminStatus(InterfaceAdminStatus value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readAdminStatus}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends InterfaceAdminStatus> readAdminStatusAsync();

  /**
   * An asynchronous implementation of {@link #writeAdminStatus}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeAdminStatusAsync(InterfaceAdminStatus value);

  /**
   * Get the AdminStatus {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the AdminStatus {@link BaseDataVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getAdminStatusNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getAdminStatusNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getAdminStatusNodeAsync();

  /**
   * Get the local value of the OperStatus Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the OperStatus Node.
   * @throws UaException if an error occurs creating or getting the OperStatus Node.
   */
  InterfaceOperStatus getOperStatus() throws UaException;

  /**
   * Set the local value of the OperStatus Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the OperStatus Node.
   * @throws UaException if an error occurs creating or getting the OperStatus Node.
   */
  void setOperStatus(InterfaceOperStatus value) throws UaException;

  /**
   * Read the value of the OperStatus Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link InterfaceOperStatus} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  InterfaceOperStatus readOperStatus() throws UaException;

  /**
   * Write a new value for the OperStatus Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link InterfaceOperStatus} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeOperStatus(InterfaceOperStatus value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readOperStatus}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends InterfaceOperStatus> readOperStatusAsync();

  /**
   * An asynchronous implementation of {@link #writeOperStatus}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeOperStatusAsync(InterfaceOperStatus value);

  /**
   * Get the OperStatus {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the OperStatus {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getOperStatusNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getOperStatusNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getOperStatusNodeAsync();

  /**
   * Get the local value of the PhysAddress Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the PhysAddress Node.
   * @throws UaException if an error occurs creating or getting the PhysAddress Node.
   */
  String getPhysAddress() throws UaException;

  /**
   * Set the local value of the PhysAddress Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the PhysAddress Node.
   * @throws UaException if an error occurs creating or getting the PhysAddress Node.
   */
  void setPhysAddress(String value) throws UaException;

  /**
   * Read the value of the PhysAddress Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link String} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String readPhysAddress() throws UaException;

  /**
   * Write a new value for the PhysAddress Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link String} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writePhysAddress(String value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readPhysAddress}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String> readPhysAddressAsync();

  /**
   * An asynchronous implementation of {@link #writePhysAddress}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writePhysAddressAsync(String value);

  /**
   * Get the PhysAddress {@link BaseDataVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the PhysAddress {@link BaseDataVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getPhysAddressNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getPhysAddressNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getPhysAddressNodeAsync();

  /**
   * Get the local value of the Speed Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the Speed Node.
   * @throws UaException if an error occurs creating or getting the Speed Node.
   */
  ULong getSpeed() throws UaException;

  /**
   * Set the local value of the Speed Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the Speed Node.
   * @throws UaException if an error occurs creating or getting the Speed Node.
   */
  void setSpeed(ULong value) throws UaException;

  /**
   * Read the value of the Speed Node from the server and update the local value if the operation
   * succeeds.
   *
   * @return the {@link ULong} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  ULong readSpeed() throws UaException;

  /**
   * Write a new value for the Speed Node to the server and update the local value if the operation
   * succeeds.
   *
   * @param value the {@link ULong} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeSpeed(ULong value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readSpeed}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends ULong> readSpeedAsync();

  /**
   * An asynchronous implementation of {@link #writeSpeed}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeSpeedAsync(ULong value);

  /**
   * Get the Speed {@link AnalogUnitType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the Speed {@link AnalogUnitType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  AnalogUnitType getSpeedNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSpeedNode()}.
   *
   * @return a CompletableFuture that completes successfully with the AnalogUnitType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends AnalogUnitType> getSpeedNodeAsync();
}

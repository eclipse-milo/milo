/*
 * Copyright (c) 2026 the Eclipse Milo Authors
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
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.1">https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.1</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface IIetfBaseNetworkInterfaceType extends BaseInterfaceType {
  /** Gets the existing node's local value. */
  @Nullable InterfaceAdminStatus getAdminStatus() throws UaException;

  /** Sets the existing node's local value. */
  void setAdminStatus(@Nullable InterfaceAdminStatus value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable InterfaceAdminStatus readAdminStatus() throws UaException;

  /** Writes the value remotely. */
  void writeAdminStatus(@Nullable InterfaceAdminStatus value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable InterfaceAdminStatus> readAdminStatusAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeAdminStatusAsync(@Nullable InterfaceAdminStatus value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getAdminStatusNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getAdminStatusNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable InterfaceOperStatus getOperStatus() throws UaException;

  /** Sets the existing node's local value. */
  void setOperStatus(@Nullable InterfaceOperStatus value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable InterfaceOperStatus readOperStatus() throws UaException;

  /** Writes the value remotely. */
  void writeOperStatus(@Nullable InterfaceOperStatus value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable InterfaceOperStatus> readOperStatusAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeOperStatusAsync(@Nullable InterfaceOperStatus value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getOperStatusNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getOperStatusNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String getPhysAddress() throws UaException;

  /** Sets the existing node's local value. */
  void setPhysAddress(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readPhysAddress() throws UaException;

  /** Writes the value remotely. */
  void writePhysAddress(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readPhysAddressAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writePhysAddressAsync(@Nullable String value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable BaseDataVariableType getPhysAddressNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable BaseDataVariableType> getPhysAddressNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable ULong getSpeed() throws UaException;

  /** Sets the existing node's local value. */
  void setSpeed(@Nullable ULong value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable ULong readSpeed() throws UaException;

  /** Writes the value remotely. */
  void writeSpeed(@Nullable ULong value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable ULong> readSpeedAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSpeedAsync(@Nullable ULong value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  AnalogUnitType getSpeedNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends AnalogUnitType> getSpeedNodeAsync();
}

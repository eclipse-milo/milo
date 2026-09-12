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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.Duplex;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.2">https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.2</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface IIeeeBaseEthernetPortType extends BaseInterfaceType {
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

  /** Gets the existing node's local value. */
  @Nullable Duplex getDuplex() throws UaException;

  /** Sets the existing node's local value. */
  void setDuplex(@Nullable Duplex value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Duplex readDuplex() throws UaException;

  /** Writes the value remotely. */
  void writeDuplex(@Nullable Duplex value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Duplex> readDuplexAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeDuplexAsync(@Nullable Duplex value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getDuplexNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getDuplexNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UShort getMaxFrameLength() throws UaException;

  /** Sets the existing node's local value. */
  void setMaxFrameLength(@Nullable UShort value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UShort readMaxFrameLength() throws UaException;

  /** Writes the value remotely. */
  void writeMaxFrameLength(@Nullable UShort value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UShort> readMaxFrameLengthAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMaxFrameLengthAsync(@Nullable UShort value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getMaxFrameLengthNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getMaxFrameLengthNodeAsync();
}

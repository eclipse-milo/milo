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
import org.eclipse.milo.opcua.sdk.client.model.variables.BaseDataVariableType;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TsnStreamState;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.7">https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.7</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface IIeeeBaseTsnStreamType extends BaseInterfaceType {
  /** Gets the existing node's local value. */
  @Nullable UByte @Nullable [] getStreamId() throws UaException;

  /** Sets the existing node's local value. */
  void setStreamId(@Nullable UByte @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UByte @Nullable [] readStreamId() throws UaException;

  /** Writes the value remotely. */
  void writeStreamId(@Nullable UByte @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UByte @Nullable []> readStreamIdAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeStreamIdAsync(@Nullable UByte @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getStreamIdNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getStreamIdNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String getStreamName() throws UaException;

  /** Sets the existing node's local value. */
  void setStreamName(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readStreamName() throws UaException;

  /** Writes the value remotely. */
  void writeStreamName(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readStreamNameAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeStreamNameAsync(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getStreamNameNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getStreamNameNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable TsnStreamState getState() throws UaException;

  /** Sets the existing node's local value. */
  void setState(@Nullable TsnStreamState value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable TsnStreamState readState() throws UaException;

  /** Writes the value remotely. */
  void writeState(@Nullable TsnStreamState value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable TsnStreamState> readStateAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeStateAsync(@Nullable TsnStreamState value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getStateNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getStateNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getAccumulatedLatency() throws UaException;

  /** Sets the existing node's local value. */
  void setAccumulatedLatency(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readAccumulatedLatency() throws UaException;

  /** Writes the value remotely. */
  void writeAccumulatedLatency(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readAccumulatedLatencyAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeAccumulatedLatencyAsync(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable BaseDataVariableType getAccumulatedLatencyNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable BaseDataVariableType> getAccumulatedLatencyNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UByte getSrClassId() throws UaException;

  /** Sets the existing node's local value. */
  void setSrClassId(@Nullable UByte value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UByte readSrClassId() throws UaException;

  /** Writes the value remotely. */
  void writeSrClassId(@Nullable UByte value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UByte> readSrClassIdAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSrClassIdAsync(@Nullable UByte value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable BaseDataVariableType getSrClassIdNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable BaseDataVariableType> getSrClassIdNodeAsync();
}

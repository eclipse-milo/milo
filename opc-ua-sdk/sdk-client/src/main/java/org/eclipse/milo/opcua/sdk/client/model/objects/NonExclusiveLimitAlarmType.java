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
import org.eclipse.milo.opcua.sdk.client.model.variables.TwoStateVariableType;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.20">https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.20</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface NonExclusiveLimitAlarmType extends LimitAlarmType {
  /** Gets the existing node's local value. */
  @Nullable LocalizedText getActiveState() throws UaException;

  /** Sets the existing node's local value. */
  void setActiveState(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable LocalizedText readActiveState() throws UaException;

  /** Writes the value remotely. */
  void writeActiveState(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable LocalizedText> readActiveStateAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeActiveStateAsync(@Nullable LocalizedText value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  TwoStateVariableType getActiveStateNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends TwoStateVariableType> getActiveStateNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable LocalizedText getHighHighState() throws UaException;

  /** Sets the existing node's local value. */
  void setHighHighState(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable LocalizedText readHighHighState() throws UaException;

  /** Writes the value remotely. */
  void writeHighHighState(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable LocalizedText> readHighHighStateAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeHighHighStateAsync(@Nullable LocalizedText value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable TwoStateVariableType getHighHighStateNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable TwoStateVariableType> getHighHighStateNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable LocalizedText getHighState() throws UaException;

  /** Sets the existing node's local value. */
  void setHighState(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable LocalizedText readHighState() throws UaException;

  /** Writes the value remotely. */
  void writeHighState(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable LocalizedText> readHighStateAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeHighStateAsync(@Nullable LocalizedText value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable TwoStateVariableType getHighStateNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable TwoStateVariableType> getHighStateNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable LocalizedText getLowState() throws UaException;

  /** Sets the existing node's local value. */
  void setLowState(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable LocalizedText readLowState() throws UaException;

  /** Writes the value remotely. */
  void writeLowState(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable LocalizedText> readLowStateAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeLowStateAsync(@Nullable LocalizedText value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable TwoStateVariableType getLowStateNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable TwoStateVariableType> getLowStateNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable LocalizedText getLowLowState() throws UaException;

  /** Sets the existing node's local value. */
  void setLowLowState(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable LocalizedText readLowLowState() throws UaException;

  /** Writes the value remotely. */
  void writeLowLowState(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable LocalizedText> readLowLowStateAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeLowLowStateAsync(@Nullable LocalizedText value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable TwoStateVariableType getLowLowStateNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable TwoStateVariableType> getLowLowStateNodeAsync();
}

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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.19/#5.8.19.3">https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.19/#5.8.19.3</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface ExclusiveLimitAlarmType extends LimitAlarmType {
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

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  ExclusiveLimitStateMachineType getLimitStateNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends ExclusiveLimitStateMachineType> getLimitStateNodeAsync();
}

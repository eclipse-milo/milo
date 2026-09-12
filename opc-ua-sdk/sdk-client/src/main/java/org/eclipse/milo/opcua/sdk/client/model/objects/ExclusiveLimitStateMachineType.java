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
import org.eclipse.milo.opcua.stack.core.UaException;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.19/#5.8.19.2">https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.19/#5.8.19.2</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface ExclusiveLimitStateMachineType extends FiniteStateMachineType {
  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  StateType getHighHighNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends StateType> getHighHighNodeAsync();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  StateType getHighNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends StateType> getHighNodeAsync();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  StateType getLowNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends StateType> getLowNodeAsync();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  StateType getLowLowNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends StateType> getLowLowNodeAsync();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  TransitionType getLowLowToLowNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends TransitionType> getLowLowToLowNodeAsync();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  TransitionType getLowToLowLowNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends TransitionType> getLowToLowLowNodeAsync();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  TransitionType getHighHighToHighNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends TransitionType> getHighHighToHighNodeAsync();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  TransitionType getHighToHighHighNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends TransitionType> getHighToHighHighNodeAsync();
}

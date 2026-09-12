/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.BaseDataVariableType;
import org.eclipse.milo.opcua.sdk.server.model.variables.FiniteStateVariableType;
import org.eclipse.milo.opcua.sdk.server.model.variables.FiniteTransitionVariableType;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part16/4.4.5">https://reference.opcfoundation.org/v105/Core/docs/Part16/4.4.5</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface FiniteStateMachineType extends StateMachineType {
  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  FiniteStateVariableType getCurrentStateNode();

  /** Gets the existing node's local value. */
  @Nullable LocalizedText getCurrentState();

  /** Sets the existing node's local value. */
  void setCurrentState(@Nullable LocalizedText value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable FiniteTransitionVariableType getLastTransitionNode();

  /** Gets the existing node's local value. */
  @Nullable LocalizedText getLastTransition();

  /** Sets the existing node's local value. */
  void setLastTransition(@Nullable LocalizedText value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable BaseDataVariableType getAvailableStatesNode();

  /** Gets the existing node's local value. */
  @Nullable NodeId @Nullable [] getAvailableStates();

  /** Sets the existing node's local value. */
  void setAvailableStates(@Nullable NodeId @Nullable [] value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable BaseDataVariableType getAvailableTransitionsNode();

  /** Gets the existing node's local value. */
  @Nullable NodeId @Nullable [] getAvailableTransitions();

  /** Sets the existing node's local value. */
  void setAvailableTransitions(@Nullable NodeId @Nullable [] value);
}

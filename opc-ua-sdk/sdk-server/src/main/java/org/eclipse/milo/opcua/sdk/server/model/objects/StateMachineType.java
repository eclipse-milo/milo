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

import org.eclipse.milo.opcua.sdk.server.model.variables.StateVariableType;
import org.eclipse.milo.opcua.sdk.server.model.variables.TransitionVariableType;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part16/4.4.2">https://reference.opcfoundation.org/v105/Core/docs/Part16/4.4.2</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface StateMachineType extends BaseObjectType {
  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  StateVariableType getCurrentStateNode();

  /** Gets the existing node's local value. */
  @Nullable LocalizedText getCurrentState();

  /** Sets the existing node's local value. */
  void setCurrentState(@Nullable LocalizedText value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable TransitionVariableType getLastTransitionNode();

  /** Gets the existing node's local value. */
  @Nullable LocalizedText getLastTransition();

  /** Sets the existing node's local value. */
  void setLastTransition(@Nullable LocalizedText value);
}

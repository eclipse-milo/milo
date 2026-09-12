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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part16/4.4.16">https://reference.opcfoundation.org/v105/Core/docs/Part16/4.4.16</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface TransitionEventType extends BaseEventType {
  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  TransitionVariableType getTransitionNode();

  /** Gets the existing node's local value. */
  @Nullable LocalizedText getTransition();

  /** Sets the existing node's local value. */
  void setTransition(@Nullable LocalizedText value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  StateVariableType getFromStateNode();

  /** Gets the existing node's local value. */
  @Nullable LocalizedText getFromState();

  /** Sets the existing node's local value. */
  void setFromState(@Nullable LocalizedText value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  StateVariableType getToStateNode();

  /** Gets the existing node's local value. */
  @Nullable LocalizedText getToState();

  /** Sets the existing node's local value. */
  void setToState(@Nullable LocalizedText value);
}

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

import org.eclipse.milo.opcua.sdk.server.model.variables.TwoStateVariableType;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.20">https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.20</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface NonExclusiveLimitAlarmType extends LimitAlarmType {
  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  TwoStateVariableType getActiveStateNode();

  /** Gets the existing node's local value. */
  @Nullable LocalizedText getActiveState();

  /** Sets the existing node's local value. */
  void setActiveState(@Nullable LocalizedText value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable TwoStateVariableType getHighHighStateNode();

  /** Gets the existing node's local value. */
  @Nullable LocalizedText getHighHighState();

  /** Sets the existing node's local value. */
  void setHighHighState(@Nullable LocalizedText value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable TwoStateVariableType getHighStateNode();

  /** Gets the existing node's local value. */
  @Nullable LocalizedText getHighState();

  /** Sets the existing node's local value. */
  void setHighState(@Nullable LocalizedText value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable TwoStateVariableType getLowStateNode();

  /** Gets the existing node's local value. */
  @Nullable LocalizedText getLowState();

  /** Sets the existing node's local value. */
  void setLowState(@Nullable LocalizedText value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable TwoStateVariableType getLowLowStateNode();

  /** Gets the existing node's local value. */
  @Nullable LocalizedText getLowLowState();

  /** Sets the existing node's local value. */
  void setLowLowState(@Nullable LocalizedText value);
}

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

import com.digitalpetri.opcua.uanodeset.runtime.methods.MethodHandlerResult;
import org.eclipse.milo.opcua.sdk.core.nodes.MethodNode;
import org.eclipse.milo.opcua.sdk.server.methods.MethodBinding;
import org.eclipse.milo.opcua.sdk.server.methods.MethodBindings;
import org.eclipse.milo.opcua.sdk.server.model.variables.AlarmRateVariableType;
import org.eclipse.milo.opcua.sdk.server.model.variables.BaseDataVariableType;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/9.2">https://reference.opcfoundation.org/v105/Core/docs/Part9/9.2</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface AlarmMetricsType extends BaseObjectType {
  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getAlarmCountNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getAlarmCount();

  /** Sets the existing node's local value. */
  void setAlarmCount(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getStartTimeNode();

  /** Gets the existing node's local value. */
  @Nullable DateTime getStartTime();

  /** Sets the existing node's local value. */
  void setStartTime(@Nullable DateTime value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getMaximumActiveStateNode();

  /** Gets the existing node's local value. */
  @Nullable Double getMaximumActiveState();

  /** Sets the existing node's local value. */
  void setMaximumActiveState(@Nullable Double value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getMaximumUnAckNode();

  /** Gets the existing node's local value. */
  @Nullable Double getMaximumUnAck();

  /** Sets the existing node's local value. */
  void setMaximumUnAck(@Nullable Double value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  AlarmRateVariableType getCurrentAlarmRateNode();

  /** Gets the existing node's local value. */
  @Nullable Double getCurrentAlarmRate();

  /** Sets the existing node's local value. */
  void setCurrentAlarmRate(@Nullable Double value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  AlarmRateVariableType getMaximumAlarmRateNode();

  /** Gets the existing node's local value. */
  @Nullable Double getMaximumAlarmRate();

  /** Sets the existing node's local value. */
  void setMaximumAlarmRate(@Nullable Double value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getMaximumReAlarmCountNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaximumReAlarmCount();

  /** Sets the existing node's local value. */
  void setMaximumReAlarmCount(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  AlarmRateVariableType getAverageAlarmRateNode();

  /** Gets the existing node's local value. */
  @Nullable Double getAverageAlarmRate();

  /** Sets the existing node's local value. */
  void setAverageAlarmRate(@Nullable Double value);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/9.4
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  MethodNode getResetMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/9.4 Binds a synchronous callback for
   * this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindReset(MethodBindings bindings, ResetHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/9.4 Binds a synchronous callback for
   * this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindResetDetailed(MethodBindings bindings, ResetDetailedHandler handler)
      throws UaException;

  /** https://reference.opcfoundation.org/v105/Core/docs/Part9/9.4 */
  @FunctionalInterface
  interface ResetHandler {
    /**
     * @throws UaException for an operation failure
     */
    void invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part9/9.4 */
  @FunctionalInterface
  interface ResetDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable Void> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context)
        throws UaException;
  }
}

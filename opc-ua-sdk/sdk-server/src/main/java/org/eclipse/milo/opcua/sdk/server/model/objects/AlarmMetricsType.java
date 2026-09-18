package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.model.variables.AlarmRateVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.BaseDataVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the AlarmMetricsType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/9.2">Model
 *     documentation</a>
 */
public interface AlarmMetricsType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 17279L);

  /**
   * Returns the mandatory AlarmCount child, a BaseDataVariableType with DataType UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getAlarmCountNode();

  /**
   * Returns the Value of the AlarmCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getAlarmCount();

  /**
   * Sets the Value of the AlarmCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setAlarmCount(@Nullable UInteger value);

  /**
   * Returns the mandatory AverageAlarmRate child, a AlarmRateVariableType with DataType Double.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/9.3">AlarmRateVariableType
   *     documentation</a>
   */
  AlarmRateVariableTypeNode getAverageAlarmRateNode();

  /**
   * Returns the Value of the AverageAlarmRate child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Double getAverageAlarmRate();

  /**
   * Sets the Value of the AverageAlarmRate child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setAverageAlarmRate(@Nullable Double value);

  /**
   * Returns the mandatory CurrentAlarmRate child, a AlarmRateVariableType with DataType Double.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/9.3">AlarmRateVariableType
   *     documentation</a>
   */
  AlarmRateVariableTypeNode getCurrentAlarmRateNode();

  /**
   * Returns the Value of the CurrentAlarmRate child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Double getCurrentAlarmRate();

  /**
   * Sets the Value of the CurrentAlarmRate child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setCurrentAlarmRate(@Nullable Double value);

  /**
   * Returns the mandatory MaximumActiveState child, a BaseDataVariableType with DataType Duration.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getMaximumActiveStateNode();

  /**
   * Returns the Value of the MaximumActiveState child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Double getMaximumActiveState();

  /**
   * Sets the Value of the MaximumActiveState child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMaximumActiveState(@Nullable Double value);

  /**
   * Returns the mandatory MaximumAlarmRate child, a AlarmRateVariableType with DataType Double.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/9.3">AlarmRateVariableType
   *     documentation</a>
   */
  AlarmRateVariableTypeNode getMaximumAlarmRateNode();

  /**
   * Returns the Value of the MaximumAlarmRate child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Double getMaximumAlarmRate();

  /**
   * Sets the Value of the MaximumAlarmRate child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMaximumAlarmRate(@Nullable Double value);

  /**
   * Returns the mandatory MaximumReAlarmCount child, a BaseDataVariableType with DataType UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getMaximumReAlarmCountNode();

  /**
   * Returns the Value of the MaximumReAlarmCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getMaximumReAlarmCount();

  /**
   * Sets the Value of the MaximumReAlarmCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMaximumReAlarmCount(@Nullable UInteger value);

  /**
   * Returns the mandatory MaximumUnAck child, a BaseDataVariableType with DataType Duration.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getMaximumUnAckNode();

  /**
   * Returns the Value of the MaximumUnAck child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Double getMaximumUnAck();

  /**
   * Sets the Value of the MaximumUnAck child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMaximumUnAck(@Nullable Double value);

  /**
   * Returns the mandatory StartTime child, a BaseDataVariableType with DataType UtcTime.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getStartTimeNode();

  /**
   * Returns the Value of the StartTime child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable DateTime getStartTime();

  /**
   * Sets the Value of the StartTime child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setStartTime(@Nullable DateTime value);

  /**
   * Returns the mandatory Reset Method node.
   *
   * @throws UaRuntimeException if the Method is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/9.4">Model
   *     documentation</a>
   */
  UaMethodNode getResetMethodNode();

  /**
   * Sets this instance's Reset handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setResetHandler(@Nullable ResetHandler handler);

  /**
   * Sets this instance's Method handlers, including inherited handlers, from one implementation;
   * null clears them and restores Method-node fallback. Absent optional Methods are skipped.
   * Changes are applied in order; a failure does not roll back earlier changes.
   *
   * @throws UaRuntimeException if a mandatory Method is absent, or a Method is ambiguous or
   *     incompatible.
   */
  void setMethods(@Nullable Methods methods);

  /**
   * Handles calls to the Reset Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/9.4">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface ResetHandler {
    /**
     * Handles a call to the Reset Method.
     *
     * @throws UaException if the call fails.
     */
    void reset(AbstractMethodInvocationHandler.InvocationContext context) throws UaException;
  }

  /** Implements this type's Methods. Unimplemented Methods report Bad_NotImplemented. */
  interface Methods {
    /** Handles a call to the Reset Method; see {@link ResetHandler#reset}. */
    default void reset(AbstractMethodInvocationHandler.InvocationContext context)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }
  }
}

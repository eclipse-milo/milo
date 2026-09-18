package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.TwoStateVariableTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the NonExclusiveLimitAlarmType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.20">Model
 *     documentation</a>
 */
public interface NonExclusiveLimitAlarmType extends LimitAlarmType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 9906L);

  /**
   * Returns the optional HighHighState child, a TwoStateVariableType with DataType LocalizedText.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.2">TwoStateVariableType
   *     documentation</a>
   */
  @Nullable TwoStateVariableTypeNode getHighHighStateNode();

  /**
   * Returns the Value of the HighHighState child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable LocalizedText getHighHighState();

  /**
   * Sets the Value of the HighHighState child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setHighHighState(@Nullable LocalizedText value);

  /**
   * Returns the optional HighState child, a TwoStateVariableType with DataType LocalizedText.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.2">TwoStateVariableType
   *     documentation</a>
   */
  @Nullable TwoStateVariableTypeNode getHighStateNode();

  /**
   * Returns the Value of the HighState child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable LocalizedText getHighState();

  /**
   * Sets the Value of the HighState child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setHighState(@Nullable LocalizedText value);

  /**
   * Returns the optional LowLowState child, a TwoStateVariableType with DataType LocalizedText.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.2">TwoStateVariableType
   *     documentation</a>
   */
  @Nullable TwoStateVariableTypeNode getLowLowStateNode();

  /**
   * Returns the Value of the LowLowState child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable LocalizedText getLowLowState();

  /**
   * Sets the Value of the LowLowState child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setLowLowState(@Nullable LocalizedText value);

  /**
   * Returns the optional LowState child, a TwoStateVariableType with DataType LocalizedText.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.2">TwoStateVariableType
   *     documentation</a>
   */
  @Nullable TwoStateVariableTypeNode getLowStateNode();

  /**
   * Returns the Value of the LowState child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable LocalizedText getLowState();

  /**
   * Sets the Value of the LowState child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setLowState(@Nullable LocalizedText value);

  /**
   * Sets this instance's Method handlers, including inherited handlers, from one implementation;
   * null clears them and restores Method-node fallback. Absent optional Methods are skipped.
   * Changes are applied in order; a failure does not roll back earlier changes.
   *
   * @throws UaRuntimeException if a mandatory Method is absent, or a Method is ambiguous or
   *     incompatible.
   */
  void setMethods(@Nullable Methods methods);

  /** Implements this type's Methods. Unimplemented Methods report Bad_NotImplemented. */
  interface Methods extends LimitAlarmType.Methods {}
}

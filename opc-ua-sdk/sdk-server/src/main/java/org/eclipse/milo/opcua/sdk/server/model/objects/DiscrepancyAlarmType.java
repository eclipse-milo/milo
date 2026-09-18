package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the DiscrepancyAlarmType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.25">Model
 *     documentation</a>
 */
public interface DiscrepancyAlarmType extends AlarmConditionType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 17080L);

  /**
   * Returns the mandatory ExpectedTime child, a PropertyType with DataType Duration.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getExpectedTimeNode();

  /**
   * Returns the Value of the ExpectedTime child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Double getExpectedTime();

  /**
   * Sets the Value of the ExpectedTime child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setExpectedTime(@Nullable Double value);

  /**
   * Returns the mandatory TargetValueNode child, a PropertyType with DataType NodeId.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getTargetValueNodeNode();

  /**
   * Returns the Value of the TargetValueNode child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable NodeId getTargetValueNode();

  /**
   * Sets the Value of the TargetValueNode child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setTargetValueNode(@Nullable NodeId value);

  /**
   * Returns the optional Tolerance child, a PropertyType with DataType Double.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getToleranceNode();

  /**
   * Returns the Value of the Tolerance child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Double getTolerance();

  /**
   * Sets the Value of the Tolerance child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setTolerance(@Nullable Double value);

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
  interface Methods extends AlarmConditionType.Methods {}
}

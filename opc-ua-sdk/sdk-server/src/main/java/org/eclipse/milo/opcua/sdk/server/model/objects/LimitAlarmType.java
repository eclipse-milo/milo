package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the LimitAlarmType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.18">Model
 *     documentation</a>
 */
public interface LimitAlarmType extends AlarmConditionType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2955L);

  /**
   * Returns the optional BaseHighHighLimit child, a PropertyType with DataType Double.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getBaseHighHighLimitNode();

  /**
   * Returns the Value of the BaseHighHighLimit child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Double getBaseHighHighLimit();

  /**
   * Sets the Value of the BaseHighHighLimit child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setBaseHighHighLimit(@Nullable Double value);

  /**
   * Returns the optional BaseHighLimit child, a PropertyType with DataType Double.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getBaseHighLimitNode();

  /**
   * Returns the Value of the BaseHighLimit child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Double getBaseHighLimit();

  /**
   * Sets the Value of the BaseHighLimit child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setBaseHighLimit(@Nullable Double value);

  /**
   * Returns the optional BaseLowLimit child, a PropertyType with DataType Double.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getBaseLowLimitNode();

  /**
   * Returns the Value of the BaseLowLimit child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Double getBaseLowLimit();

  /**
   * Sets the Value of the BaseLowLimit child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setBaseLowLimit(@Nullable Double value);

  /**
   * Returns the optional BaseLowLowLimit child, a PropertyType with DataType Double.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getBaseLowLowLimitNode();

  /**
   * Returns the Value of the BaseLowLowLimit child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Double getBaseLowLowLimit();

  /**
   * Sets the Value of the BaseLowLowLimit child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setBaseLowLowLimit(@Nullable Double value);

  /**
   * Returns the optional HighDeadband child, a PropertyType with DataType Double.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getHighDeadbandNode();

  /**
   * Returns the Value of the HighDeadband child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Double getHighDeadband();

  /**
   * Sets the Value of the HighDeadband child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setHighDeadband(@Nullable Double value);

  /**
   * Returns the optional HighHighDeadband child, a PropertyType with DataType Double.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getHighHighDeadbandNode();

  /**
   * Returns the Value of the HighHighDeadband child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Double getHighHighDeadband();

  /**
   * Sets the Value of the HighHighDeadband child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setHighHighDeadband(@Nullable Double value);

  /**
   * Returns the optional HighHighLimit child, a PropertyType with DataType Double.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getHighHighLimitNode();

  /**
   * Returns the Value of the HighHighLimit child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Double getHighHighLimit();

  /**
   * Sets the Value of the HighHighLimit child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setHighHighLimit(@Nullable Double value);

  /**
   * Returns the optional HighLimit child, a PropertyType with DataType Double.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getHighLimitNode();

  /**
   * Returns the Value of the HighLimit child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Double getHighLimit();

  /**
   * Sets the Value of the HighLimit child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setHighLimit(@Nullable Double value);

  /**
   * Returns the optional LowDeadband child, a PropertyType with DataType Double.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getLowDeadbandNode();

  /**
   * Returns the Value of the LowDeadband child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Double getLowDeadband();

  /**
   * Sets the Value of the LowDeadband child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setLowDeadband(@Nullable Double value);

  /**
   * Returns the optional LowLimit child, a PropertyType with DataType Double.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getLowLimitNode();

  /**
   * Returns the Value of the LowLimit child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Double getLowLimit();

  /**
   * Sets the Value of the LowLimit child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setLowLimit(@Nullable Double value);

  /**
   * Returns the optional LowLowDeadband child, a PropertyType with DataType Double.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getLowLowDeadbandNode();

  /**
   * Returns the Value of the LowLowDeadband child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Double getLowLowDeadband();

  /**
   * Sets the Value of the LowLowDeadband child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setLowLowDeadband(@Nullable Double value);

  /**
   * Returns the optional LowLowLimit child, a PropertyType with DataType Double.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getLowLowLimitNode();

  /**
   * Returns the Value of the LowLowLimit child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Double getLowLowLimit();

  /**
   * Sets the Value of the LowLowLimit child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setLowLowLimit(@Nullable Double value);

  /**
   * Returns the optional SeverityHigh child, a PropertyType with DataType UInt16.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getSeverityHighNode();

  /**
   * Returns the Value of the SeverityHigh child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UShort getSeverityHigh();

  /**
   * Sets the Value of the SeverityHigh child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSeverityHigh(@Nullable UShort value);

  /**
   * Returns the optional SeverityHighHigh child, a PropertyType with DataType UInt16.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getSeverityHighHighNode();

  /**
   * Returns the Value of the SeverityHighHigh child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UShort getSeverityHighHigh();

  /**
   * Sets the Value of the SeverityHighHigh child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSeverityHighHigh(@Nullable UShort value);

  /**
   * Returns the optional SeverityLow child, a PropertyType with DataType UInt16.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getSeverityLowNode();

  /**
   * Returns the Value of the SeverityLow child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UShort getSeverityLow();

  /**
   * Sets the Value of the SeverityLow child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSeverityLow(@Nullable UShort value);

  /**
   * Returns the optional SeverityLowLow child, a PropertyType with DataType UInt16.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getSeverityLowLowNode();

  /**
   * Returns the Value of the SeverityLowLow child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UShort getSeverityLowLow();

  /**
   * Sets the Value of the SeverityLowLow child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSeverityLowLow(@Nullable UShort value);

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

package org.eclipse.milo.opcua.sdk.server.model.variables;

import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.EUInformation;
import org.eclipse.milo.opcua.stack.core.types.structured.NumberRange;
import org.eclipse.milo.opcua.stack.core.types.structured.Range;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the BaseAnalogType VariableType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part8/5.3.2/#5.3.2.2">Model
 *     documentation</a>
 */
public interface BaseAnalogType extends DataItemType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 15318L);

  /**
   * Returns the optional EUNumberRange child, a PropertyType with DataType NumberRange.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getEUNumberRangeNode();

  /**
   * Returns the Value of the EUNumberRange child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable NumberRange getEUNumberRange();

  /**
   * Sets the Value of the EUNumberRange child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setEUNumberRange(@Nullable NumberRange value);

  /**
   * Returns the optional EURange child, a PropertyType with DataType Range.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getEURangeNode();

  /**
   * Returns the Value of the EURange child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Range getEURange();

  /**
   * Sets the Value of the EURange child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setEURange(@Nullable Range value);

  /**
   * Returns the optional EngineeringUnits child, a PropertyType with DataType EUInformation.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getEngineeringUnits_Node();

  /**
   * Returns the Value of the EngineeringUnits child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable EUInformation getEngineeringUnits_();

  /**
   * Sets the Value of the EngineeringUnits child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setEngineeringUnits_(@Nullable EUInformation value);

  /**
   * Returns the optional InstrumentNumberRange child, a PropertyType with DataType NumberRange.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getInstrumentNumberRangeNode();

  /**
   * Returns the Value of the InstrumentNumberRange child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable NumberRange getInstrumentNumberRange();

  /**
   * Sets the Value of the InstrumentNumberRange child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setInstrumentNumberRange(@Nullable NumberRange value);

  /**
   * Returns the optional InstrumentRange child, a PropertyType with DataType Range.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getInstrumentRangeNode();

  /**
   * Returns the Value of the InstrumentRange child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Range getInstrumentRange();

  /**
   * Sets the Value of the InstrumentRange child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setInstrumentRange(@Nullable Range value);

  /**
   * Returns this node's Value.
   *
   * @throws UaRuntimeException if the Value does not convert.
   */
  @Nullable Variant getTypedValue();

  /**
   * Sets this node's Value.
   *
   * @throws UaRuntimeException if the value does not convert.
   */
  void setTypedValue(@Nullable Variant value);
}

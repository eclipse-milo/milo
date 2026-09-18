package org.eclipse.milo.opcua.sdk.server.model.variables;

import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.AxisScaleEnumeration;
import org.eclipse.milo.opcua.stack.core.types.structured.EUInformation;
import org.eclipse.milo.opcua.stack.core.types.structured.Range;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the ArrayItemType VariableType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part8/5.3.4/#5.3.4.1">Model
 *     documentation</a>
 */
public interface ArrayItemType extends DataItemType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 12021L);

  /**
   * Returns the mandatory AxisScaleType child, a PropertyType with DataType AxisScaleEnumeration.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getAxisScaleTypeNode();

  /**
   * Returns the Value of the AxisScaleType child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable AxisScaleEnumeration getAxisScaleType();

  /**
   * Sets the Value of the AxisScaleType child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setAxisScaleType(@Nullable AxisScaleEnumeration value);

  /**
   * Returns the mandatory EURange child, a PropertyType with DataType Range.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getEURangeNode();

  /**
   * Returns the Value of the EURange child.
   *
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
   * Returns the mandatory EngineeringUnits child, a PropertyType with DataType EUInformation.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getEngineeringUnits_Node();

  /**
   * Returns the Value of the EngineeringUnits child.
   *
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
   * Returns the mandatory Title child, a PropertyType with DataType LocalizedText.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getTitleNode();

  /**
   * Returns the Value of the Title child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable LocalizedText getTitle();

  /**
   * Sets the Value of the Title child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setTitle(@Nullable LocalizedText value);

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

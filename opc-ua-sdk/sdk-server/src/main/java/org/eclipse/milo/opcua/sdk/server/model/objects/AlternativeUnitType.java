package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.LinearConversionDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the AlternativeUnitType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part8/6.4.2/#6.4.2.4">Model
 *     documentation</a>
 */
public interface AlternativeUnitType extends UnitType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 32467L);

  /**
   * Returns the optional LinearConversion child, a PropertyType with DataType
   * LinearConversionDataType.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getLinearConversionNode();

  /**
   * Returns the Value of the LinearConversion child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable LinearConversionDataType getLinearConversion();

  /**
   * Sets the Value of the LinearConversion child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setLinearConversion(@Nullable LinearConversionDataType value);

  /**
   * Returns the optional MathMLConversion child, a PropertyType with DataType String.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getMathMLConversionNode();

  /**
   * Returns the Value of the MathMLConversion child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getMathMLConversion();

  /**
   * Sets the Value of the MathMLConversion child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMathMLConversion(@Nullable String value);

  /**
   * Returns the optional MathMLInverseConversion child, a PropertyType with DataType String.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getMathMLInverseConversionNode();

  /**
   * Returns the Value of the MathMLInverseConversion child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getMathMLInverseConversion();

  /**
   * Sets the Value of the MathMLInverseConversion child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMathMLInverseConversion(@Nullable String value);
}

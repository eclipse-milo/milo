package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ConversionLimitEnum;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the ServerUnitType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part8/6.4.2/#6.4.2.3">Model
 *     documentation</a>
 */
public interface ServerUnitType extends UnitType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 32447L);

  /**
   * Returns the optional AlternativeUnits child, a BaseObjectType.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.2">BaseObjectType
   *     documentation</a>
   */
  @Nullable BaseObjectTypeNode getAlternativeUnitsNode();

  /**
   * Returns the optional CoherentUnit child, a UnitType.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part8/6.4.2/#6.4.2.2">UnitType
   *     documentation</a>
   */
  @Nullable UnitTypeNode getCoherentUnitNode();

  /**
   * Returns the mandatory ConversionLimit child, a PropertyType with DataType ConversionLimitEnum.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getConversionLimitNode();

  /**
   * Returns the Value of the ConversionLimit child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable ConversionLimitEnum getConversionLimit();

  /**
   * Sets the Value of the ConversionLimit child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setConversionLimit(@Nullable ConversionLimitEnum value);
}

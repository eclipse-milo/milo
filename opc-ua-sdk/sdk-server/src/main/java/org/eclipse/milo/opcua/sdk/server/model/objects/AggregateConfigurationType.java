package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the AggregateConfigurationType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part13/4.2.1/#4.2.1.2">Model
 *     documentation</a>
 */
public interface AggregateConfigurationType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 11187L);

  /**
   * Returns the mandatory PercentDataBad child, a PropertyType with DataType Byte.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getPercentDataBadNode();

  /**
   * Returns the Value of the PercentDataBad child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UByte getPercentDataBad();

  /**
   * Sets the Value of the PercentDataBad child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setPercentDataBad(@Nullable UByte value);

  /**
   * Returns the mandatory PercentDataGood child, a PropertyType with DataType Byte.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getPercentDataGoodNode();

  /**
   * Returns the Value of the PercentDataGood child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UByte getPercentDataGood();

  /**
   * Sets the Value of the PercentDataGood child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setPercentDataGood(@Nullable UByte value);

  /**
   * Returns the mandatory TreatUncertainAsBad child, a PropertyType with DataType Boolean.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getTreatUncertainAsBadNode();

  /**
   * Returns the Value of the TreatUncertainAsBad child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Boolean getTreatUncertainAsBad();

  /**
   * Sets the Value of the TreatUncertainAsBad child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setTreatUncertainAsBad(@Nullable Boolean value);

  /**
   * Returns the mandatory UseSlopedExtrapolation child, a PropertyType with DataType Boolean.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getUseSlopedExtrapolationNode();

  /**
   * Returns the Value of the UseSlopedExtrapolation child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Boolean getUseSlopedExtrapolation();

  /**
   * Sets the Value of the UseSlopedExtrapolation child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setUseSlopedExtrapolation(@Nullable Boolean value);
}

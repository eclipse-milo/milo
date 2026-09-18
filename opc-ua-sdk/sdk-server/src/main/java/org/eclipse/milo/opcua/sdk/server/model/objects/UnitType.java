package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the UnitType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part8/6.4.2/#6.4.2.2">Model
 *     documentation</a>
 */
public interface UnitType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 32442L);

  /**
   * Returns the optional Discipline child, a PropertyType with DataType String.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getDisciplineNode();

  /**
   * Returns the Value of the Discipline child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getDiscipline();

  /**
   * Sets the Value of the Discipline child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setDiscipline(@Nullable String value);

  /**
   * Returns the mandatory Symbol child, a PropertyType with DataType LocalizedText.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getSymbolNode();

  /**
   * Returns the Value of the Symbol child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable LocalizedText getSymbol();

  /**
   * Sets the Value of the Symbol child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSymbol(@Nullable LocalizedText value);

  /**
   * Returns the mandatory UnitSystem child, a PropertyType with DataType String.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getUnitSystemNode();

  /**
   * Returns the Value of the UnitSystem child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getUnitSystem();

  /**
   * Sets the Value of the UnitSystem child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setUnitSystem(@Nullable String value);
}

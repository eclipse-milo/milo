package org.eclipse.milo.opcua.sdk.server.model.variables;

import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the MultiStateDiscreteType VariableType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part8/5.3.3/#5.3.3.3">Model
 *     documentation</a>
 */
public interface MultiStateDiscreteType extends DiscreteItemType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2376L);

  /**
   * Returns the mandatory EnumStrings child, a PropertyType with DataType LocalizedText.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getEnumStringsNode();

  /**
   * Returns the Value of the EnumStrings child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  LocalizedText @Nullable [] getEnumStrings();

  /**
   * Sets the Value of the EnumStrings child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setEnumStrings(LocalizedText @Nullable [] value);

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

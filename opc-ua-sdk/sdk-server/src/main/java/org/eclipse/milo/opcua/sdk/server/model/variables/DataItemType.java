package org.eclipse.milo.opcua.sdk.server.model.variables;

import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the DataItemType VariableType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part8/5.3.1">Model
 *     documentation</a>
 */
public interface DataItemType extends BaseDataVariableType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2365L);

  /**
   * Returns the optional Definition child, a PropertyType with DataType String.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getDefinitionNode();

  /**
   * Returns the Value of the Definition child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getDefinition();

  /**
   * Sets the Value of the Definition child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setDefinition(@Nullable String value);

  /**
   * Returns the optional ValuePrecision child, a PropertyType with DataType Double.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getValuePrecisionNode();

  /**
   * Returns the Value of the ValuePrecision child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Double getValuePrecision();

  /**
   * Sets the Value of the ValuePrecision child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setValuePrecision(@Nullable Double value);

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

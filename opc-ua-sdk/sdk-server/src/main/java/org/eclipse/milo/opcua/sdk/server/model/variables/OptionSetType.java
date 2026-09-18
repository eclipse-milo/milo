package org.eclipse.milo.opcua.sdk.server.model.variables;

import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the OptionSetType VariableType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.17">Model
 *     documentation</a>
 */
public interface OptionSetType extends BaseDataVariableType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 11487L);

  /**
   * Returns the optional BitMask child, a PropertyType with DataType Boolean.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getBitMaskNode();

  /**
   * Returns the Value of the BitMask child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  Boolean @Nullable [] getBitMask();

  /**
   * Sets the Value of the BitMask child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setBitMask(Boolean @Nullable [] value);

  /**
   * Returns the mandatory OptionSetValues child, a PropertyType with DataType LocalizedText.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getOptionSetValuesNode();

  /**
   * Returns the Value of the OptionSetValues child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  LocalizedText @Nullable [] getOptionSetValues();

  /**
   * Sets the Value of the OptionSetValues child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setOptionSetValues(LocalizedText @Nullable [] value);

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

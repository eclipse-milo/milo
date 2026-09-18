package org.eclipse.milo.opcua.sdk.server.model.variables;

import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the SelectionListType VariableType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.18">Model
 *     documentation</a>
 */
public interface SelectionListType extends BaseDataVariableType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 16309L);

  /**
   * Returns the optional RestrictToList child, a PropertyType with DataType Boolean.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getRestrictToListNode();

  /**
   * Returns the Value of the RestrictToList child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Boolean getRestrictToList();

  /**
   * Sets the Value of the RestrictToList child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setRestrictToList(@Nullable Boolean value);

  /**
   * Returns the optional SelectionDescriptions child, a PropertyType with DataType LocalizedText.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getSelectionDescriptionsNode();

  /**
   * Returns the Value of the SelectionDescriptions child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  LocalizedText @Nullable [] getSelectionDescriptions();

  /**
   * Sets the Value of the SelectionDescriptions child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSelectionDescriptions(LocalizedText @Nullable [] value);

  /**
   * Returns the mandatory Selections child, a PropertyType with DataType BaseDataType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getSelectionsNode();

  /**
   * Returns the Value of the Selections child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Variant @Nullable [] getSelections();

  /**
   * Sets the Value of the Selections child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSelections(@Nullable Variant @Nullable [] value);

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

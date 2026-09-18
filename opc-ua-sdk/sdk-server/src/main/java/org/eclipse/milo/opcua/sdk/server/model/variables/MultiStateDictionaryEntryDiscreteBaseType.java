package org.eclipse.milo.opcua.sdk.server.model.variables;

import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the MultiStateDictionaryEntryDiscreteBaseType VariableType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part19/7.1">Model
 *     documentation</a>
 */
public interface MultiStateDictionaryEntryDiscreteBaseType extends MultiStateValueDiscreteType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 19077L);

  /**
   * Returns the mandatory EnumDictionaryEntries child, a PropertyType with DataType NodeId.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getEnumDictionaryEntriesNode();

  /**
   * Returns the Value of the EnumDictionaryEntries child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Variant getEnumDictionaryEntries();

  /**
   * Sets the Value of the EnumDictionaryEntries child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setEnumDictionaryEntries(@Nullable Variant value);

  /**
   * Returns the optional ValueAsDictionaryEntries child, a PropertyType with DataType NodeId.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getValueAsDictionaryEntriesNode();

  /**
   * Returns the Value of the ValueAsDictionaryEntries child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  NodeId @Nullable [] getValueAsDictionaryEntries();

  /**
   * Sets the Value of the ValueAsDictionaryEntries child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setValueAsDictionaryEntries(NodeId @Nullable [] value);

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

package org.eclipse.milo.opcua.sdk.server.model.variables;

import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/** Server API for the DataTypeDictionaryType VariableType. */
public interface DataTypeDictionaryType extends BaseDataVariableType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 72L);

  /**
   * Returns the optional DataTypeVersion child, a PropertyType with DataType String.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getDataTypeVersion_Node();

  /**
   * Returns the Value of the DataTypeVersion child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getDataTypeVersion_();

  /**
   * Sets the Value of the DataTypeVersion child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setDataTypeVersion_(@Nullable String value);

  /**
   * Returns the optional Deprecated child, a PropertyType with DataType Boolean.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getDeprecatedNode();

  /**
   * Returns the Value of the Deprecated child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Boolean getDeprecated();

  /**
   * Sets the Value of the Deprecated child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setDeprecated(@Nullable Boolean value);

  /**
   * Returns the optional NamespaceUri child, a PropertyType with DataType String.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getNamespaceUriNode();

  /**
   * Returns the Value of the NamespaceUri child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getNamespaceUri();

  /**
   * Sets the Value of the NamespaceUri child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setNamespaceUri(@Nullable String value);

  /**
   * Returns this node's Value.
   *
   * @throws UaRuntimeException if the Value does not convert.
   */
  @Nullable ByteString getTypedValue();

  /**
   * Sets this node's Value.
   *
   * @throws UaRuntimeException if the value does not convert.
   */
  void setTypedValue(@Nullable ByteString value);
}

package org.eclipse.milo.opcua.sdk.server.model.variables;

import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the StateVariableType VariableType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part16/4.4.3">Model
 *     documentation</a>
 */
public interface StateVariableType extends BaseDataVariableType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2755L);

  /**
   * Returns the optional EffectiveDisplayName child, a PropertyType with DataType LocalizedText.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getEffectiveDisplayNameNode();

  /**
   * Returns the Value of the EffectiveDisplayName child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable LocalizedText getEffectiveDisplayName();

  /**
   * Sets the Value of the EffectiveDisplayName child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setEffectiveDisplayName(@Nullable LocalizedText value);

  /**
   * Returns the mandatory Id child, a PropertyType with DataType BaseDataType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getIdNode();

  /**
   * Returns the Value of the Id child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Variant getId();

  /**
   * Sets the Value of the Id child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setId(@Nullable Variant value);

  /**
   * Returns the optional Name child, a PropertyType with DataType QualifiedName.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getNameNode();

  /**
   * Returns the Value of the Name child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable QualifiedName getName();

  /**
   * Sets the Value of the Name child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setName(@Nullable QualifiedName value);

  /**
   * Returns the optional Number child, a PropertyType with DataType UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getNumberNode();

  /**
   * Returns the Value of the Number child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getNumber();

  /**
   * Sets the Value of the Number child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setNumber(@Nullable UInteger value);

  /**
   * Returns this node's Value.
   *
   * @throws UaRuntimeException if the Value does not convert.
   */
  @Nullable LocalizedText getTypedValue();

  /**
   * Sets this node's Value.
   *
   * @throws UaRuntimeException if the value does not convert.
   */
  void setTypedValue(@Nullable LocalizedText value);
}

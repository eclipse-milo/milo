package org.eclipse.milo.opcua.sdk.server.model.variables;

import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the TransitionVariableType VariableType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part16/4.4.4">Model
 *     documentation</a>
 */
public interface TransitionVariableType extends BaseDataVariableType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2762L);

  /**
   * Returns the optional EffectiveTransitionTime child, a PropertyType with DataType UtcTime.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getEffectiveTransitionTimeNode();

  /**
   * Returns the Value of the EffectiveTransitionTime child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable DateTime getEffectiveTransitionTime();

  /**
   * Sets the Value of the EffectiveTransitionTime child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setEffectiveTransitionTime(@Nullable DateTime value);

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
   * Returns the optional TransitionTime child, a PropertyType with DataType UtcTime.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getTransitionTimeNode();

  /**
   * Returns the Value of the TransitionTime child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable DateTime getTransitionTime();

  /**
   * Sets the Value of the TransitionTime child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setTransitionTime(@Nullable DateTime value);

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

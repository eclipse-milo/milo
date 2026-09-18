package org.eclipse.milo.opcua.sdk.server.model.variables;

import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the TwoStateVariableType VariableType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.2">Model
 *     documentation</a>
 */
public interface TwoStateVariableType extends StateVariableType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 8995L);

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
   * Returns the optional FalseState child, a PropertyType with DataType LocalizedText.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getFalseStateNode();

  /**
   * Returns the Value of the FalseState child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable LocalizedText getFalseState();

  /**
   * Sets the Value of the FalseState child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setFalseState(@Nullable LocalizedText value);

  /**
   * Returns the mandatory Id child, a PropertyType with DataType Boolean.
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
  @Nullable Boolean getTwoStateVariableTypeId();

  /**
   * Sets the Value of the Id child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setTwoStateVariableTypeId(@Nullable Boolean value);

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
   * Returns the optional TrueState child, a PropertyType with DataType LocalizedText.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getTrueStateNode();

  /**
   * Returns the Value of the TrueState child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable LocalizedText getTrueState();

  /**
   * Sets the Value of the TrueState child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setTrueState(@Nullable LocalizedText value);
}

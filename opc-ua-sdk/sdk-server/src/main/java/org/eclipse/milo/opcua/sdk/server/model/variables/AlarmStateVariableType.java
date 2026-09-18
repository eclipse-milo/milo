package org.eclipse.milo.opcua.sdk.server.model.variables;

import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.AlarmMask;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilter;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the AlarmStateVariableType VariableType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/8.2">Model
 *     documentation</a>
 */
public interface AlarmStateVariableType extends BaseDataVariableType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 32244L);

  /**
   * Returns the mandatory ActiveCount child, a PropertyType with DataType UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getActiveCountNode();

  /**
   * Returns the Value of the ActiveCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getActiveCount();

  /**
   * Sets the Value of the ActiveCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setActiveCount(@Nullable UInteger value);

  /**
   * Returns the mandatory Filter child, a PropertyType with DataType ContentFilter.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getFilterNode();

  /**
   * Returns the Value of the Filter child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable ContentFilter getFilter();

  /**
   * Sets the Value of the Filter child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setFilter(@Nullable ContentFilter value);

  /**
   * Returns the mandatory HighestActiveSeverity child, a PropertyType with DataType UInt16.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getHighestActiveSeverityNode();

  /**
   * Returns the Value of the HighestActiveSeverity child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UShort getHighestActiveSeverity();

  /**
   * Sets the Value of the HighestActiveSeverity child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setHighestActiveSeverity(@Nullable UShort value);

  /**
   * Returns the mandatory HighestUnackSeverity child, a PropertyType with DataType UInt16.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getHighestUnackSeverityNode();

  /**
   * Returns the Value of the HighestUnackSeverity child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UShort getHighestUnackSeverity();

  /**
   * Sets the Value of the HighestUnackSeverity child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setHighestUnackSeverity(@Nullable UShort value);

  /**
   * Returns the mandatory UnacknowledgedCount child, a PropertyType with DataType UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getUnacknowledgedCountNode();

  /**
   * Returns the Value of the UnacknowledgedCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getUnacknowledgedCount();

  /**
   * Sets the Value of the UnacknowledgedCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setUnacknowledgedCount(@Nullable UInteger value);

  /**
   * Returns the mandatory UnconfirmedCount child, a PropertyType with DataType UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getUnconfirmedCountNode();

  /**
   * Returns the Value of the UnconfirmedCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getUnconfirmedCount();

  /**
   * Sets the Value of the UnconfirmedCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setUnconfirmedCount(@Nullable UInteger value);

  /**
   * Returns this node's Value.
   *
   * @throws UaRuntimeException if the Value does not convert.
   */
  @Nullable AlarmMask getTypedValue();

  /**
   * Sets this node's Value.
   *
   * @throws UaRuntimeException if the value does not convert.
   */
  void setTypedValue(@Nullable AlarmMask value);
}

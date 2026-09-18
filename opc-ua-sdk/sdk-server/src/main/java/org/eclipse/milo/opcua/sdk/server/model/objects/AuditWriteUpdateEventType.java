package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the AuditWriteUpdateEventType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.25">Model
 *     documentation</a>
 */
public interface AuditWriteUpdateEventType extends AuditUpdateEventType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2100L);

  /**
   * Returns the mandatory AttributeId child, a PropertyType with DataType UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getAttributeIdNode();

  /**
   * Returns the Value of the AttributeId child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getAttributeId();

  /**
   * Sets the Value of the AttributeId child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setAttributeId(@Nullable UInteger value);

  /**
   * Returns the mandatory IndexRange child, a PropertyType with DataType NumericRange.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getIndexRangeNode();

  /**
   * Returns the Value of the IndexRange child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getIndexRange();

  /**
   * Sets the Value of the IndexRange child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setIndexRange(@Nullable String value);

  /**
   * Returns the mandatory NewValue child, a PropertyType with DataType BaseDataType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getNewValueNode();

  /**
   * Returns the Value of the NewValue child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Variant getNewValue();

  /**
   * Sets the Value of the NewValue child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setNewValue(@Nullable Variant value);

  /**
   * Returns the mandatory OldValue child, a PropertyType with DataType BaseDataType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getOldValueNode();

  /**
   * Returns the Value of the OldValue child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Variant getOldValue();

  /**
   * Sets the Value of the OldValue child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setOldValue(@Nullable Variant value);
}

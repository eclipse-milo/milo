package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the ProgressEventType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.35">Model
 *     documentation</a>
 */
public interface ProgressEventType extends BaseEventType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 11436L);

  /**
   * Returns the mandatory Context child, a PropertyType with DataType BaseDataType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getContextNode();

  /**
   * Returns the Value of the Context child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Variant getContext();

  /**
   * Sets the Value of the Context child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setContext(@Nullable Variant value);

  /**
   * Returns the mandatory Progress child, a PropertyType with DataType UInt16.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getProgressNode();

  /**
   * Returns the Value of the Progress child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UShort getProgress();

  /**
   * Sets the Value of the Progress child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setProgress(@Nullable UShort value);
}

package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the AuditUpdateStateEventType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part16/4.4.17">Model
 *     documentation</a>
 */
public interface AuditUpdateStateEventType extends AuditUpdateMethodEventType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2315L);

  /**
   * Returns the mandatory NewStateId child, a PropertyType with DataType BaseDataType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getNewStateIdNode();

  /**
   * Returns the Value of the NewStateId child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Variant getNewStateId();

  /**
   * Sets the Value of the NewStateId child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setNewStateId(@Nullable Variant value);

  /**
   * Returns the mandatory OldStateId child, a PropertyType with DataType BaseDataType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getOldStateIdNode();

  /**
   * Returns the Value of the OldStateId child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Variant getOldStateId();

  /**
   * Sets the Value of the OldStateId child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setOldStateId(@Nullable Variant value);
}

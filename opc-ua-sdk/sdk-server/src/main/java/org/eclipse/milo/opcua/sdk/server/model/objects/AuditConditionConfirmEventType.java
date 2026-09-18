package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the AuditConditionConfirmEventType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.10.7">Model
 *     documentation</a>
 */
public interface AuditConditionConfirmEventType extends AuditConditionEventType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 8961L);

  /**
   * Returns the mandatory Comment child, a PropertyType with DataType LocalizedText.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getCommentNode();

  /**
   * Returns the Value of the Comment child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable LocalizedText getComment();

  /**
   * Sets the Value of the Comment child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setComment(@Nullable LocalizedText value);

  /**
   * Returns the mandatory ConditionEventId child, a PropertyType with DataType ByteString.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getConditionEventIdNode();

  /**
   * Returns the Value of the ConditionEventId child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable ByteString getConditionEventId();

  /**
   * Sets the Value of the ConditionEventId child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setConditionEventId(@Nullable ByteString value);
}

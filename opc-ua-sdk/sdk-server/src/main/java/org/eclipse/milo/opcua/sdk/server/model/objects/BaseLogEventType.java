package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.TraceContextDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the BaseLogEventType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part26/6.3">Model
 *     documentation</a>
 */
public interface BaseLogEventType extends BaseEventType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 19362L);

  /**
   * Returns the mandatory ConditionClassId child, a PropertyType with DataType NodeId.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getConditionClassIdNode();

  /**
   * Returns the mandatory ConditionClassName child, a PropertyType with DataType LocalizedText.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getConditionClassNameNode();

  /**
   * Returns the optional ErrorCode child, a PropertyType with DataType StatusCode.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getErrorCodeNode();

  /**
   * Returns the Value of the ErrorCode child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable StatusCode getErrorCode();

  /**
   * Sets the Value of the ErrorCode child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setErrorCode(@Nullable StatusCode value);

  /**
   * Returns the optional ErrorCodeNode child, a PropertyType with DataType NodeId.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getErrorCodeNode_Node();

  /**
   * Returns the Value of the ErrorCodeNode child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable NodeId getErrorCodeNode_();

  /**
   * Sets the Value of the ErrorCodeNode child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setErrorCodeNode_(@Nullable NodeId value);

  /**
   * Returns the optional TraceContext child, a PropertyType with DataType TraceContextDataType.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getTraceContextNode();

  /**
   * Returns the Value of the TraceContext child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable TraceContextDataType getTraceContext();

  /**
   * Sets the Value of the TraceContext child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setTraceContext(@Nullable TraceContextDataType value);
}

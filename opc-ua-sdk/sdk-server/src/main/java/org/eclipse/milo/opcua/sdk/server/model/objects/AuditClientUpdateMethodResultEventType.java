package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the AuditClientUpdateMethodResultEventType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.37">Model
 *     documentation</a>
 */
public interface AuditClientUpdateMethodResultEventType extends AuditClientEventType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 23926L);

  /**
   * Returns the mandatory InputArguments child, a PropertyType with DataType BaseDataType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getInputArgumentsNode();

  /**
   * Returns the Value of the InputArguments child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Variant @Nullable [] getInputArguments();

  /**
   * Sets the Value of the InputArguments child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setInputArguments(@Nullable Variant @Nullable [] value);

  /**
   * Returns the mandatory MethodId child, a PropertyType with DataType ExpandedNodeId.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getMethodIdNode();

  /**
   * Returns the Value of the MethodId child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable ExpandedNodeId getMethodId();

  /**
   * Sets the Value of the MethodId child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMethodId(@Nullable ExpandedNodeId value);

  /**
   * Returns the mandatory ObjectId child, a PropertyType with DataType ExpandedNodeId.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getObjectIdNode();

  /**
   * Returns the Value of the ObjectId child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable ExpandedNodeId getObjectId();

  /**
   * Sets the Value of the ObjectId child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setObjectId(@Nullable ExpandedNodeId value);

  /**
   * Returns the mandatory OutputArguments child, a PropertyType with DataType BaseDataType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getOutputArgumentsNode();

  /**
   * Returns the Value of the OutputArguments child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Variant @Nullable [] getOutputArguments();

  /**
   * Sets the Value of the OutputArguments child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setOutputArguments(@Nullable Variant @Nullable [] value);

  /**
   * Returns the mandatory StatusCodeId child, a PropertyType with DataType StatusCode.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getStatusCodeIdNode();

  /**
   * Returns the Value of the StatusCodeId child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable StatusCode getStatusCodeId();

  /**
   * Sets the Value of the StatusCodeId child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setStatusCodeId(@Nullable StatusCode value);
}

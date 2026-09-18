package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the AuditUpdateMethodEventType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.27">Model
 *     documentation</a>
 */
public interface AuditUpdateMethodEventType extends AuditEventType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2127L);

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
   * Returns the mandatory MethodId child, a PropertyType with DataType NodeId.
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
  @Nullable NodeId getMethodId();

  /**
   * Sets the Value of the MethodId child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMethodId(@Nullable NodeId value);

  /**
   * Returns the optional OutputArguments child, a PropertyType with DataType BaseDataType.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getOutputArgumentsNode();

  /**
   * Returns the Value of the OutputArguments child.
   *
   * @return the value, or null if the child is absent or the Value is null.
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
   * Returns the optional StatusCodeId child, a PropertyType with DataType StatusCode.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getStatusCodeIdNode();

  /**
   * Returns the Value of the StatusCodeId child.
   *
   * @return the value, or null if the child is absent or the Value is null.
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

package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.TransactionErrorType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the TransactionDiagnosticsType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.17">Model
 *     documentation</a>
 */
public interface TransactionDiagnosticsType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 32286L);

  /**
   * Returns the mandatory AffectedCertificateGroups child, a PropertyType with DataType NodeId.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getAffectedCertificateGroupsNode();

  /**
   * Returns the Value of the AffectedCertificateGroups child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  NodeId @Nullable [] getAffectedCertificateGroups();

  /**
   * Sets the Value of the AffectedCertificateGroups child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setAffectedCertificateGroups(NodeId @Nullable [] value);

  /**
   * Returns the mandatory AffectedTrustLists child, a PropertyType with DataType NodeId.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getAffectedTrustListsNode();

  /**
   * Returns the Value of the AffectedTrustLists child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  NodeId @Nullable [] getAffectedTrustLists();

  /**
   * Sets the Value of the AffectedTrustLists child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setAffectedTrustLists(NodeId @Nullable [] value);

  /**
   * Returns the mandatory EndTime child, a PropertyType with DataType UtcTime.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getEndTimeNode();

  /**
   * Returns the Value of the EndTime child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable DateTime getEndTime();

  /**
   * Sets the Value of the EndTime child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setEndTime(@Nullable DateTime value);

  /**
   * Returns the mandatory Errors child, a PropertyType with DataType TransactionErrorType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getErrorsNode();

  /**
   * Returns the Value of the Errors child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable TransactionErrorType @Nullable [] getErrors();

  /**
   * Sets the Value of the Errors child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setErrors(@Nullable TransactionErrorType @Nullable [] value);

  /**
   * Returns the mandatory Result child, a PropertyType with DataType StatusCode.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getResultNode();

  /**
   * Returns the Value of the Result child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable StatusCode getResult();

  /**
   * Sets the Value of the Result child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setResult(@Nullable StatusCode value);

  /**
   * Returns the mandatory StartTime child, a PropertyType with DataType UtcTime.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getStartTimeNode();

  /**
   * Returns the Value of the StartTime child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable DateTime getStartTime();

  /**
   * Sets the Value of the StartTime child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setStartTime(@Nullable DateTime value);
}

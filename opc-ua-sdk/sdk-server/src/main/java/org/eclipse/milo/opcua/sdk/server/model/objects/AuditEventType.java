package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the AuditEventType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.3">Model
 *     documentation</a>
 */
public interface AuditEventType extends BaseEventType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2052L);

  /**
   * Returns the mandatory ActionTimeStamp child, a PropertyType with DataType UtcTime.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getActionTimeStampNode();

  /**
   * Returns the Value of the ActionTimeStamp child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable DateTime getActionTimeStamp();

  /**
   * Sets the Value of the ActionTimeStamp child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setActionTimeStamp(@Nullable DateTime value);

  /**
   * Returns the optional ClientApplicationUri child, a PropertyType with DataType String.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getClientApplicationUriNode();

  /**
   * Returns the Value of the ClientApplicationUri child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getClientApplicationUri();

  /**
   * Sets the Value of the ClientApplicationUri child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setClientApplicationUri(@Nullable String value);

  /**
   * Returns the mandatory ClientAuditEntryId child, a PropertyType with DataType String.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getClientAuditEntryIdNode();

  /**
   * Returns the Value of the ClientAuditEntryId child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getClientAuditEntryId();

  /**
   * Sets the Value of the ClientAuditEntryId child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setClientAuditEntryId(@Nullable String value);

  /**
   * Returns the mandatory ClientUserId child, a PropertyType with DataType String.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getClientUserIdNode();

  /**
   * Returns the Value of the ClientUserId child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getClientUserId();

  /**
   * Sets the Value of the ClientUserId child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setClientUserId(@Nullable String value);

  /**
   * Returns the mandatory ServerId child, a PropertyType with DataType String.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getServerIdNode();

  /**
   * Returns the Value of the ServerId child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getServerId();

  /**
   * Sets the Value of the ServerId child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setServerId(@Nullable String value);

  /**
   * Returns the mandatory Status child, a PropertyType with DataType Boolean.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getStatusNode();

  /**
   * Returns the Value of the Status child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Boolean getStatus();

  /**
   * Sets the Value of the Status child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setStatus(@Nullable Boolean value);
}

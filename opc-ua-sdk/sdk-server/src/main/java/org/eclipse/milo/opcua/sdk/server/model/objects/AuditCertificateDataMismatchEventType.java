package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the AuditCertificateDataMismatchEventType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.13">Model
 *     documentation</a>
 */
public interface AuditCertificateDataMismatchEventType extends AuditCertificateEventType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2082L);

  /**
   * Returns the mandatory InvalidHostname child, a PropertyType with DataType String.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getInvalidHostnameNode();

  /**
   * Returns the Value of the InvalidHostname child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getInvalidHostname();

  /**
   * Sets the Value of the InvalidHostname child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setInvalidHostname(@Nullable String value);

  /**
   * Returns the mandatory InvalidUri child, a PropertyType with DataType String.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getInvalidUriNode();

  /**
   * Returns the Value of the InvalidUri child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getInvalidUri();

  /**
   * Sets the Value of the InvalidUri child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setInvalidUri(@Nullable String value);
}

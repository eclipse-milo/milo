package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the AuditCreateSessionEventType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.8">Model
 *     documentation</a>
 */
public interface AuditCreateSessionEventType extends AuditSessionEventType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2071L);

  /**
   * Returns the mandatory ClientCertificate child, a PropertyType with DataType ByteString.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getClientCertificateNode();

  /**
   * Returns the Value of the ClientCertificate child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable ByteString getClientCertificate();

  /**
   * Sets the Value of the ClientCertificate child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setClientCertificate(@Nullable ByteString value);

  /**
   * Returns the mandatory ClientCertificateThumbprint child, a PropertyType with DataType String.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getClientCertificateThumbprintNode();

  /**
   * Returns the Value of the ClientCertificateThumbprint child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getClientCertificateThumbprint();

  /**
   * Sets the Value of the ClientCertificateThumbprint child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setClientCertificateThumbprint(@Nullable String value);

  /**
   * Returns the mandatory RevisedSessionTimeout child, a PropertyType with DataType Duration.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getRevisedSessionTimeoutNode();

  /**
   * Returns the Value of the RevisedSessionTimeout child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Double getRevisedSessionTimeout();

  /**
   * Sets the Value of the RevisedSessionTimeout child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setRevisedSessionTimeout(@Nullable Double value);

  /**
   * Returns the mandatory SecureChannelId child, a PropertyType with DataType String.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getSecureChannelIdNode();

  /**
   * Returns the Value of the SecureChannelId child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getSecureChannelId();

  /**
   * Sets the Value of the SecureChannelId child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSecureChannelId(@Nullable String value);
}

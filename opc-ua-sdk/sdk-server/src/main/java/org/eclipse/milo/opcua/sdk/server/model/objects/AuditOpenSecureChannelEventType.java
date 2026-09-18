package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.SecurityTokenRequestType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the AuditOpenSecureChannelEventType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.6">Model
 *     documentation</a>
 */
public interface AuditOpenSecureChannelEventType extends AuditChannelEventType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2060L);

  /**
   * Returns the optional CertificateErrorEventId child, a PropertyType with DataType ByteString.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getCertificateErrorEventIdNode();

  /**
   * Returns the Value of the CertificateErrorEventId child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable ByteString getCertificateErrorEventId();

  /**
   * Sets the Value of the CertificateErrorEventId child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setCertificateErrorEventId(@Nullable ByteString value);

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
   * Returns the mandatory RequestType child, a PropertyType with DataType SecurityTokenRequestType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getRequestTypeNode();

  /**
   * Returns the Value of the RequestType child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable SecurityTokenRequestType getRequestType();

  /**
   * Sets the Value of the RequestType child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setRequestType(@Nullable SecurityTokenRequestType value);

  /**
   * Returns the mandatory RequestedLifetime child, a PropertyType with DataType Duration.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getRequestedLifetimeNode();

  /**
   * Returns the Value of the RequestedLifetime child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Double getRequestedLifetime();

  /**
   * Sets the Value of the RequestedLifetime child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setRequestedLifetime(@Nullable Double value);

  /**
   * Returns the mandatory SecurityMode child, a PropertyType with DataType MessageSecurityMode.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getSecurityModeNode();

  /**
   * Returns the Value of the SecurityMode child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable MessageSecurityMode getSecurityMode();

  /**
   * Sets the Value of the SecurityMode child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSecurityMode(@Nullable MessageSecurityMode value);

  /**
   * Returns the mandatory SecurityPolicyUri child, a PropertyType with DataType String.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getSecurityPolicyUriNode();

  /**
   * Returns the Value of the SecurityPolicyUri child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getSecurityPolicyUri();

  /**
   * Sets the Value of the SecurityPolicyUri child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSecurityPolicyUri(@Nullable String value);
}

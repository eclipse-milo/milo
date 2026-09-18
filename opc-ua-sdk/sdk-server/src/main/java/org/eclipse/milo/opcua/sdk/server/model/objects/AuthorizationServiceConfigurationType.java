package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the AuthorizationServiceConfigurationType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/9.7.4">Model
 *     documentation</a>
 */
public interface AuthorizationServiceConfigurationType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 17852L);

  /**
   * Returns the mandatory IssuerEndpointUrl child, a PropertyType with DataType String.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getIssuerEndpointUrlNode();

  /**
   * Returns the Value of the IssuerEndpointUrl child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getIssuerEndpointUrl();

  /**
   * Sets the Value of the IssuerEndpointUrl child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setIssuerEndpointUrl(@Nullable String value);

  /**
   * Returns the mandatory ServiceCertificate child, a PropertyType with DataType ByteString.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getServiceCertificateNode();

  /**
   * Returns the Value of the ServiceCertificate child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable ByteString getServiceCertificate();

  /**
   * Sets the Value of the ServiceCertificate child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setServiceCertificate(@Nullable ByteString value);

  /**
   * Returns the mandatory ServiceUri child, a PropertyType with DataType String.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getServiceUriNode();

  /**
   * Returns the Value of the ServiceUri child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getServiceUri();

  /**
   * Sets the Value of the ServiceUri child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setServiceUri(@Nullable String value);
}

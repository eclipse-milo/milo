package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the AuthorizationServiceConfigurationType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/9.7.4">Model
 *     documentation</a>
 */
public interface AuthorizationServiceConfigurationType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 17852L);

  QualifiedProperty<String> ServiceUri_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ServiceUri",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
          -1,
          String.class);

  QualifiedProperty<String> IssuerEndpointUrl_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "IssuerEndpointUrl",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
          -1,
          String.class);

  QualifiedProperty<ByteString> ServiceCertificate_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ServiceCertificate",
          ExpandedNodeId.of(Namespaces.OPC_UA, 15L),
          -1,
          ByteString.class);

  /**
   * Resolves the mandatory ServiceUri child, a PropertyType with DataType String.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getServiceUriNode() throws UaException;

  /** Asynchronous form of {@link #getServiceUriNode()}. */
  CompletableFuture<? extends PropertyType> getServiceUriNodeAsync();

  /**
   * Reads the Value of the ServiceUri child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readServiceUri() throws UaException;

  /**
   * Writes the Value of the ServiceUri child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeServiceUri(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readServiceUri()}. */
  CompletableFuture<? extends @Nullable String> readServiceUriAsync();

  /** Asynchronous form of {@link #writeServiceUri}; completes with the operation status. */
  CompletableFuture<StatusCode> writeServiceUriAsync(@Nullable String value);

  /**
   * Resolves the mandatory IssuerEndpointUrl child, a PropertyType with DataType String.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getIssuerEndpointUrlNode() throws UaException;

  /** Asynchronous form of {@link #getIssuerEndpointUrlNode()}. */
  CompletableFuture<? extends PropertyType> getIssuerEndpointUrlNodeAsync();

  /**
   * Reads the Value of the IssuerEndpointUrl child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readIssuerEndpointUrl() throws UaException;

  /**
   * Writes the Value of the IssuerEndpointUrl child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeIssuerEndpointUrl(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readIssuerEndpointUrl()}. */
  CompletableFuture<? extends @Nullable String> readIssuerEndpointUrlAsync();

  /** Asynchronous form of {@link #writeIssuerEndpointUrl}; completes with the operation status. */
  CompletableFuture<StatusCode> writeIssuerEndpointUrlAsync(@Nullable String value);

  /**
   * Resolves the mandatory ServiceCertificate child, a PropertyType with DataType ByteString.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getServiceCertificateNode() throws UaException;

  /** Asynchronous form of {@link #getServiceCertificateNode()}. */
  CompletableFuture<? extends PropertyType> getServiceCertificateNodeAsync();

  /**
   * Reads the Value of the ServiceCertificate child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable ByteString readServiceCertificate() throws UaException;

  /**
   * Writes the Value of the ServiceCertificate child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeServiceCertificate(@Nullable ByteString value) throws UaException;

  /** Asynchronous form of {@link #readServiceCertificate()}. */
  CompletableFuture<? extends @Nullable ByteString> readServiceCertificateAsync();

  /** Asynchronous form of {@link #writeServiceCertificate}; completes with the operation status. */
  CompletableFuture<StatusCode> writeServiceCertificateAsync(@Nullable ByteString value);
}

package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.SecurityTokenRequestType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the AuditOpenSecureChannelEventType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.6">Model
 *     documentation</a>
 */
public interface AuditOpenSecureChannelEventType extends AuditChannelEventType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2060L);

  QualifiedProperty<SecurityTokenRequestType> RequestType_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "RequestType",
          ExpandedNodeId.of(Namespaces.OPC_UA, 315L),
          -1,
          SecurityTokenRequestType.class);

  QualifiedProperty<MessageSecurityMode> SecurityMode_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "SecurityMode",
          ExpandedNodeId.of(Namespaces.OPC_UA, 302L),
          -1,
          MessageSecurityMode.class);

  QualifiedProperty<ByteString> ClientCertificate_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ClientCertificate",
          ExpandedNodeId.of(Namespaces.OPC_UA, 15L),
          -1,
          ByteString.class);

  QualifiedProperty<Double> RequestedLifetime_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "RequestedLifetime",
          ExpandedNodeId.of(Namespaces.OPC_UA, 290L),
          -1,
          Double.class);

  QualifiedProperty<String> SecurityPolicyUri_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "SecurityPolicyUri",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
          -1,
          String.class);

  QualifiedProperty<ByteString> CertificateErrorEventId_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "CertificateErrorEventId",
          ExpandedNodeId.of(Namespaces.OPC_UA, 15L),
          -1,
          ByteString.class);

  QualifiedProperty<String> ClientCertificateThumbprint_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ClientCertificateThumbprint",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
          -1,
          String.class);

  /**
   * Resolves the mandatory RequestType child, a PropertyType with DataType
   * SecurityTokenRequestType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getRequestTypeNode() throws UaException;

  /** Asynchronous form of {@link #getRequestTypeNode()}. */
  CompletableFuture<? extends PropertyType> getRequestTypeNodeAsync();

  /**
   * Reads the Value of the RequestType child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable SecurityTokenRequestType readRequestType() throws UaException;

  /**
   * Writes the Value of the RequestType child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeRequestType(@Nullable SecurityTokenRequestType value) throws UaException;

  /** Asynchronous form of {@link #readRequestType()}. */
  CompletableFuture<? extends @Nullable SecurityTokenRequestType> readRequestTypeAsync();

  /** Asynchronous form of {@link #writeRequestType}; completes with the operation status. */
  CompletableFuture<StatusCode> writeRequestTypeAsync(@Nullable SecurityTokenRequestType value);

  /**
   * Resolves the mandatory SecurityMode child, a PropertyType with DataType MessageSecurityMode.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getSecurityModeNode() throws UaException;

  /** Asynchronous form of {@link #getSecurityModeNode()}. */
  CompletableFuture<? extends PropertyType> getSecurityModeNodeAsync();

  /**
   * Reads the Value of the SecurityMode child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable MessageSecurityMode readSecurityMode() throws UaException;

  /**
   * Writes the Value of the SecurityMode child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSecurityMode(@Nullable MessageSecurityMode value) throws UaException;

  /** Asynchronous form of {@link #readSecurityMode()}. */
  CompletableFuture<? extends @Nullable MessageSecurityMode> readSecurityModeAsync();

  /** Asynchronous form of {@link #writeSecurityMode}; completes with the operation status. */
  CompletableFuture<StatusCode> writeSecurityModeAsync(@Nullable MessageSecurityMode value);

  /**
   * Resolves the mandatory ClientCertificate child, a PropertyType with DataType ByteString.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getClientCertificateNode() throws UaException;

  /** Asynchronous form of {@link #getClientCertificateNode()}. */
  CompletableFuture<? extends PropertyType> getClientCertificateNodeAsync();

  /**
   * Reads the Value of the ClientCertificate child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable ByteString readClientCertificate() throws UaException;

  /**
   * Writes the Value of the ClientCertificate child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeClientCertificate(@Nullable ByteString value) throws UaException;

  /** Asynchronous form of {@link #readClientCertificate()}. */
  CompletableFuture<? extends @Nullable ByteString> readClientCertificateAsync();

  /** Asynchronous form of {@link #writeClientCertificate}; completes with the operation status. */
  CompletableFuture<StatusCode> writeClientCertificateAsync(@Nullable ByteString value);

  /**
   * Resolves the mandatory RequestedLifetime child, a PropertyType with DataType Duration.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getRequestedLifetimeNode() throws UaException;

  /** Asynchronous form of {@link #getRequestedLifetimeNode()}. */
  CompletableFuture<? extends PropertyType> getRequestedLifetimeNodeAsync();

  /**
   * Reads the Value of the RequestedLifetime child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Double readRequestedLifetime() throws UaException;

  /**
   * Writes the Value of the RequestedLifetime child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeRequestedLifetime(@Nullable Double value) throws UaException;

  /** Asynchronous form of {@link #readRequestedLifetime()}. */
  CompletableFuture<? extends @Nullable Double> readRequestedLifetimeAsync();

  /** Asynchronous form of {@link #writeRequestedLifetime}; completes with the operation status. */
  CompletableFuture<StatusCode> writeRequestedLifetimeAsync(@Nullable Double value);

  /**
   * Resolves the mandatory SecurityPolicyUri child, a PropertyType with DataType String.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getSecurityPolicyUriNode() throws UaException;

  /** Asynchronous form of {@link #getSecurityPolicyUriNode()}. */
  CompletableFuture<? extends PropertyType> getSecurityPolicyUriNodeAsync();

  /**
   * Reads the Value of the SecurityPolicyUri child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readSecurityPolicyUri() throws UaException;

  /**
   * Writes the Value of the SecurityPolicyUri child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSecurityPolicyUri(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readSecurityPolicyUri()}. */
  CompletableFuture<? extends @Nullable String> readSecurityPolicyUriAsync();

  /** Asynchronous form of {@link #writeSecurityPolicyUri}; completes with the operation status. */
  CompletableFuture<StatusCode> writeSecurityPolicyUriAsync(@Nullable String value);

  /**
   * Resolves the optional CertificateErrorEventId child, a PropertyType with DataType ByteString.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getCertificateErrorEventIdNode() throws UaException;

  /** Asynchronous form of {@link #getCertificateErrorEventIdNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getCertificateErrorEventIdNodeAsync();

  /**
   * Reads the Value of the CertificateErrorEventId child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable ByteString readCertificateErrorEventId() throws UaException;

  /**
   * Writes the Value of the CertificateErrorEventId child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeCertificateErrorEventId(@Nullable ByteString value) throws UaException;

  /** Asynchronous form of {@link #readCertificateErrorEventId()}. */
  CompletableFuture<? extends @Nullable ByteString> readCertificateErrorEventIdAsync();

  /**
   * Asynchronous form of {@link #writeCertificateErrorEventId}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeCertificateErrorEventIdAsync(@Nullable ByteString value);

  /**
   * Resolves the mandatory ClientCertificateThumbprint child, a PropertyType with DataType String.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getClientCertificateThumbprintNode() throws UaException;

  /** Asynchronous form of {@link #getClientCertificateThumbprintNode()}. */
  CompletableFuture<? extends PropertyType> getClientCertificateThumbprintNodeAsync();

  /**
   * Reads the Value of the ClientCertificateThumbprint child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readClientCertificateThumbprint() throws UaException;

  /**
   * Writes the Value of the ClientCertificateThumbprint child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeClientCertificateThumbprint(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readClientCertificateThumbprint()}. */
  CompletableFuture<? extends @Nullable String> readClientCertificateThumbprintAsync();

  /**
   * Asynchronous form of {@link #writeClientCertificateThumbprint}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeClientCertificateThumbprintAsync(@Nullable String value);
}

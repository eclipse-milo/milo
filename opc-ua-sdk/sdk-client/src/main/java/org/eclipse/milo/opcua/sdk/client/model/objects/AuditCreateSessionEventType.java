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
 * Client API for the AuditCreateSessionEventType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.8">Model
 *     documentation</a>
 */
public interface AuditCreateSessionEventType extends AuditSessionEventType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2071L);

  QualifiedProperty<String> SecureChannelId_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "SecureChannelId",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
          -1,
          String.class);

  QualifiedProperty<ByteString> ClientCertificate_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ClientCertificate",
          ExpandedNodeId.of(Namespaces.OPC_UA, 15L),
          -1,
          ByteString.class);

  QualifiedProperty<Double> RevisedSessionTimeout_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "RevisedSessionTimeout",
          ExpandedNodeId.of(Namespaces.OPC_UA, 290L),
          -1,
          Double.class);

  QualifiedProperty<String> ClientCertificateThumbprint_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ClientCertificateThumbprint",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
          -1,
          String.class);

  /**
   * Resolves the mandatory SecureChannelId child, a PropertyType with DataType String.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getSecureChannelIdNode() throws UaException;

  /** Asynchronous form of {@link #getSecureChannelIdNode()}. */
  CompletableFuture<? extends PropertyType> getSecureChannelIdNodeAsync();

  /**
   * Reads the Value of the SecureChannelId child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readSecureChannelId() throws UaException;

  /**
   * Writes the Value of the SecureChannelId child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSecureChannelId(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readSecureChannelId()}. */
  CompletableFuture<? extends @Nullable String> readSecureChannelIdAsync();

  /** Asynchronous form of {@link #writeSecureChannelId}; completes with the operation status. */
  CompletableFuture<StatusCode> writeSecureChannelIdAsync(@Nullable String value);

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
   * Resolves the mandatory RevisedSessionTimeout child, a PropertyType with DataType Duration.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getRevisedSessionTimeoutNode() throws UaException;

  /** Asynchronous form of {@link #getRevisedSessionTimeoutNode()}. */
  CompletableFuture<? extends PropertyType> getRevisedSessionTimeoutNodeAsync();

  /**
   * Reads the Value of the RevisedSessionTimeout child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Double readRevisedSessionTimeout() throws UaException;

  /**
   * Writes the Value of the RevisedSessionTimeout child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeRevisedSessionTimeout(@Nullable Double value) throws UaException;

  /** Asynchronous form of {@link #readRevisedSessionTimeout()}. */
  CompletableFuture<? extends @Nullable Double> readRevisedSessionTimeoutAsync();

  /**
   * Asynchronous form of {@link #writeRevisedSessionTimeout}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeRevisedSessionTimeoutAsync(@Nullable Double value);

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

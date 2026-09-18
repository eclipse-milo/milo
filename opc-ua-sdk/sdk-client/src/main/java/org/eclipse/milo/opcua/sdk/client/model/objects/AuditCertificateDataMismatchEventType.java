package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the AuditCertificateDataMismatchEventType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.13">Model
 *     documentation</a>
 */
public interface AuditCertificateDataMismatchEventType extends AuditCertificateEventType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2082L);

  QualifiedProperty<String> InvalidUri_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "InvalidUri",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
          -1,
          String.class);

  QualifiedProperty<String> InvalidHostname_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "InvalidHostname",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
          -1,
          String.class);

  /**
   * Resolves the mandatory InvalidUri child, a PropertyType with DataType String.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getInvalidUriNode() throws UaException;

  /** Asynchronous form of {@link #getInvalidUriNode()}. */
  CompletableFuture<? extends PropertyType> getInvalidUriNodeAsync();

  /**
   * Reads the Value of the InvalidUri child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readInvalidUri() throws UaException;

  /**
   * Writes the Value of the InvalidUri child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeInvalidUri(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readInvalidUri()}. */
  CompletableFuture<? extends @Nullable String> readInvalidUriAsync();

  /** Asynchronous form of {@link #writeInvalidUri}; completes with the operation status. */
  CompletableFuture<StatusCode> writeInvalidUriAsync(@Nullable String value);

  /**
   * Resolves the mandatory InvalidHostname child, a PropertyType with DataType String.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getInvalidHostnameNode() throws UaException;

  /** Asynchronous form of {@link #getInvalidHostnameNode()}. */
  CompletableFuture<? extends PropertyType> getInvalidHostnameNodeAsync();

  /**
   * Reads the Value of the InvalidHostname child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readInvalidHostname() throws UaException;

  /**
   * Writes the Value of the InvalidHostname child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeInvalidHostname(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readInvalidHostname()}. */
  CompletableFuture<? extends @Nullable String> readInvalidHostnameAsync();

  /** Asynchronous form of {@link #writeInvalidHostname}; completes with the operation status. */
  CompletableFuture<StatusCode> writeInvalidHostnameAsync(@Nullable String value);
}

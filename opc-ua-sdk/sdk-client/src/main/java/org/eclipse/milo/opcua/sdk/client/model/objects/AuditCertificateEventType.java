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
 * Client API for the AuditCertificateEventType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.12">Model
 *     documentation</a>
 */
public interface AuditCertificateEventType extends AuditSecurityEventType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2080L);

  QualifiedProperty<ByteString> Certificate_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "Certificate",
          ExpandedNodeId.of(Namespaces.OPC_UA, 15L),
          -1,
          ByteString.class);

  /**
   * Resolves the mandatory Certificate child, a PropertyType with DataType ByteString.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getCertificateNode() throws UaException;

  /** Asynchronous form of {@link #getCertificateNode()}. */
  CompletableFuture<? extends PropertyType> getCertificateNodeAsync();

  /**
   * Reads the Value of the Certificate child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable ByteString readCertificate() throws UaException;

  /**
   * Writes the Value of the Certificate child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeCertificate(@Nullable ByteString value) throws UaException;

  /** Asynchronous form of {@link #readCertificate()}. */
  CompletableFuture<? extends @Nullable ByteString> readCertificateAsync();

  /** Asynchronous form of {@link #writeCertificate}; completes with the operation status. */
  CompletableFuture<StatusCode> writeCertificateAsync(@Nullable ByteString value);
}

package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the CertificateExpirationAlarmType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.24/#5.8.24.7">Model
 *     documentation</a>
 */
public interface CertificateExpirationAlarmType extends SystemOffNormalAlarmType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 13225L);

  QualifiedProperty<ByteString> Certificate_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "Certificate",
          ExpandedNodeId.of(Namespaces.OPC_UA, 15L),
          -1,
          ByteString.class);

  QualifiedProperty<DateTime> ExpirationDate_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ExpirationDate",
          ExpandedNodeId.of(Namespaces.OPC_UA, 13L),
          -1,
          DateTime.class);

  QualifiedProperty<NodeId> CertificateType_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "CertificateType",
          ExpandedNodeId.of(Namespaces.OPC_UA, 17L),
          -1,
          NodeId.class);

  QualifiedProperty<Double> ExpirationLimit_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ExpirationLimit",
          ExpandedNodeId.of(Namespaces.OPC_UA, 290L),
          -1,
          Double.class);

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

  /**
   * Resolves the mandatory ExpirationDate child, a PropertyType with DataType DateTime.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getExpirationDateNode() throws UaException;

  /** Asynchronous form of {@link #getExpirationDateNode()}. */
  CompletableFuture<? extends PropertyType> getExpirationDateNodeAsync();

  /**
   * Reads the Value of the ExpirationDate child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable DateTime readExpirationDate() throws UaException;

  /**
   * Writes the Value of the ExpirationDate child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeExpirationDate(@Nullable DateTime value) throws UaException;

  /** Asynchronous form of {@link #readExpirationDate()}. */
  CompletableFuture<? extends @Nullable DateTime> readExpirationDateAsync();

  /** Asynchronous form of {@link #writeExpirationDate}; completes with the operation status. */
  CompletableFuture<StatusCode> writeExpirationDateAsync(@Nullable DateTime value);

  /**
   * Resolves the mandatory CertificateType child, a PropertyType with DataType NodeId.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getCertificateTypeNode() throws UaException;

  /** Asynchronous form of {@link #getCertificateTypeNode()}. */
  CompletableFuture<? extends PropertyType> getCertificateTypeNodeAsync();

  /**
   * Reads the Value of the CertificateType child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable NodeId readCertificateType() throws UaException;

  /**
   * Writes the Value of the CertificateType child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeCertificateType(@Nullable NodeId value) throws UaException;

  /** Asynchronous form of {@link #readCertificateType()}. */
  CompletableFuture<? extends @Nullable NodeId> readCertificateTypeAsync();

  /** Asynchronous form of {@link #writeCertificateType}; completes with the operation status. */
  CompletableFuture<StatusCode> writeCertificateTypeAsync(@Nullable NodeId value);

  /**
   * Resolves the optional ExpirationLimit child, a PropertyType with DataType Duration.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getExpirationLimitNode() throws UaException;

  /** Asynchronous form of {@link #getExpirationLimitNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getExpirationLimitNodeAsync();

  /**
   * Reads the Value of the ExpirationLimit child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Double readExpirationLimit() throws UaException;

  /**
   * Writes the Value of the ExpirationLimit child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeExpirationLimit(@Nullable Double value) throws UaException;

  /** Asynchronous form of {@link #readExpirationLimit()}. */
  CompletableFuture<? extends @Nullable Double> readExpirationLimitAsync();

  /** Asynchronous form of {@link #writeExpirationLimit}; completes with the operation status. */
  CompletableFuture<StatusCode> writeExpirationLimitAsync(@Nullable Double value);
}

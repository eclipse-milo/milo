package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the CertificateUpdatedAuditEventType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.27">Model
 *     documentation</a>
 */
public interface CertificateUpdatedAuditEventType extends AuditUpdateMethodEventType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 12620L);

  QualifiedProperty<NodeId> CertificateType_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "CertificateType",
          ExpandedNodeId.of(Namespaces.OPC_UA, 17L),
          -1,
          NodeId.class);

  QualifiedProperty<NodeId> CertificateGroup_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "CertificateGroup",
          ExpandedNodeId.of(Namespaces.OPC_UA, 17L),
          -1,
          NodeId.class);

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
   * Resolves the mandatory CertificateGroup child, a PropertyType with DataType NodeId.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getCertificateGroupNode() throws UaException;

  /** Asynchronous form of {@link #getCertificateGroupNode()}. */
  CompletableFuture<? extends PropertyType> getCertificateGroupNodeAsync();

  /**
   * Reads the Value of the CertificateGroup child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable NodeId readCertificateGroup() throws UaException;

  /**
   * Writes the Value of the CertificateGroup child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeCertificateGroup(@Nullable NodeId value) throws UaException;

  /** Asynchronous form of {@link #readCertificateGroup()}. */
  CompletableFuture<? extends @Nullable NodeId> readCertificateGroupAsync();

  /** Asynchronous form of {@link #writeCertificateGroup}; completes with the operation status. */
  CompletableFuture<StatusCode> writeCertificateGroupAsync(@Nullable NodeId value);
}

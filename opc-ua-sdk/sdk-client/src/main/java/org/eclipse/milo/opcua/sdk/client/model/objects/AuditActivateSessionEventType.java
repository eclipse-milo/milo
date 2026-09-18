package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.SignedSoftwareCertificate;
import org.eclipse.milo.opcua.stack.core.types.structured.UserIdentityToken;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the AuditActivateSessionEventType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.10">Model
 *     documentation</a>
 */
public interface AuditActivateSessionEventType extends AuditSessionEventType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2075L);

  QualifiedProperty<NodeId[]> CurrentRoleIds_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "CurrentRoleIds",
          ExpandedNodeId.of(Namespaces.OPC_UA, 17L),
          1,
          NodeId[].class);

  QualifiedProperty<String> SecureChannelId_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "SecureChannelId",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
          -1,
          String.class);

  QualifiedProperty<UserIdentityToken> UserIdentityToken_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "UserIdentityToken",
          ExpandedNodeId.of(Namespaces.OPC_UA, 316L),
          -1,
          UserIdentityToken.class);

  QualifiedProperty<SignedSoftwareCertificate[]> ClientSoftwareCertificates_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ClientSoftwareCertificates",
          ExpandedNodeId.of(Namespaces.OPC_UA, 344L),
          1,
          SignedSoftwareCertificate[].class);

  /**
   * Resolves the optional CurrentRoleIds child, a PropertyType with DataType NodeId.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getCurrentRoleIdsNode() throws UaException;

  /** Asynchronous form of {@link #getCurrentRoleIdsNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getCurrentRoleIdsNodeAsync();

  /**
   * Reads the Value of the CurrentRoleIds child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  NodeId @Nullable [] readCurrentRoleIds() throws UaException;

  /**
   * Writes the Value of the CurrentRoleIds child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeCurrentRoleIds(NodeId @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readCurrentRoleIds()}. */
  CompletableFuture<? extends NodeId @Nullable []> readCurrentRoleIdsAsync();

  /** Asynchronous form of {@link #writeCurrentRoleIds}; completes with the operation status. */
  CompletableFuture<StatusCode> writeCurrentRoleIdsAsync(NodeId @Nullable [] value);

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
   * Resolves the mandatory UserIdentityToken child, a PropertyType with DataType UserIdentityToken.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getUserIdentityTokenNode() throws UaException;

  /** Asynchronous form of {@link #getUserIdentityTokenNode()}. */
  CompletableFuture<? extends PropertyType> getUserIdentityTokenNodeAsync();

  /**
   * Reads the Value of the UserIdentityToken child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UserIdentityToken readUserIdentityToken() throws UaException;

  /**
   * Writes the Value of the UserIdentityToken child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeUserIdentityToken(@Nullable UserIdentityToken value) throws UaException;

  /** Asynchronous form of {@link #readUserIdentityToken()}. */
  CompletableFuture<? extends @Nullable UserIdentityToken> readUserIdentityTokenAsync();

  /** Asynchronous form of {@link #writeUserIdentityToken}; completes with the operation status. */
  CompletableFuture<StatusCode> writeUserIdentityTokenAsync(@Nullable UserIdentityToken value);

  /**
   * Resolves the mandatory ClientSoftwareCertificates child, a PropertyType with DataType
   * SignedSoftwareCertificate.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getClientSoftwareCertificatesNode() throws UaException;

  /** Asynchronous form of {@link #getClientSoftwareCertificatesNode()}. */
  CompletableFuture<? extends PropertyType> getClientSoftwareCertificatesNodeAsync();

  /**
   * Reads the Value of the ClientSoftwareCertificates child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable SignedSoftwareCertificate @Nullable [] readClientSoftwareCertificates()
      throws UaException;

  /**
   * Writes the Value of the ClientSoftwareCertificates child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeClientSoftwareCertificates(@Nullable SignedSoftwareCertificate @Nullable [] value)
      throws UaException;

  /** Asynchronous form of {@link #readClientSoftwareCertificates()}. */
  CompletableFuture<? extends @Nullable SignedSoftwareCertificate @Nullable []>
      readClientSoftwareCertificatesAsync();

  /**
   * Asynchronous form of {@link #writeClientSoftwareCertificates}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeClientSoftwareCertificatesAsync(
      @Nullable SignedSoftwareCertificate @Nullable [] value);
}

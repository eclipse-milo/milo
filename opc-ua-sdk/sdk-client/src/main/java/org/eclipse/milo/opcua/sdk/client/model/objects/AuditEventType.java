package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the AuditEventType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.3">Model
 *     documentation</a>
 */
public interface AuditEventType extends BaseEventType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2052L);

  QualifiedProperty<String> ClientUserId_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ClientUserId",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
          -1,
          String.class);

  QualifiedProperty<DateTime> ActionTimeStamp_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ActionTimeStamp",
          ExpandedNodeId.of(Namespaces.OPC_UA, 294L),
          -1,
          DateTime.class);

  QualifiedProperty<String> ClientAuditEntryId_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ClientAuditEntryId",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
          -1,
          String.class);

  QualifiedProperty<String> ClientApplicationUri_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ClientApplicationUri",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
          -1,
          String.class);

  QualifiedProperty<Boolean> Status_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA, "Status", ExpandedNodeId.of(Namespaces.OPC_UA, 1L), -1, Boolean.class);

  QualifiedProperty<String> ServerId_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ServerId",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
          -1,
          String.class);

  /**
   * Resolves the mandatory ClientUserId child, a PropertyType with DataType String.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getClientUserIdNode() throws UaException;

  /** Asynchronous form of {@link #getClientUserIdNode()}. */
  CompletableFuture<? extends PropertyType> getClientUserIdNodeAsync();

  /**
   * Reads the Value of the ClientUserId child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readClientUserId() throws UaException;

  /**
   * Writes the Value of the ClientUserId child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeClientUserId(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readClientUserId()}. */
  CompletableFuture<? extends @Nullable String> readClientUserIdAsync();

  /** Asynchronous form of {@link #writeClientUserId}; completes with the operation status. */
  CompletableFuture<StatusCode> writeClientUserIdAsync(@Nullable String value);

  /**
   * Resolves the mandatory ActionTimeStamp child, a PropertyType with DataType UtcTime.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getActionTimeStampNode() throws UaException;

  /** Asynchronous form of {@link #getActionTimeStampNode()}. */
  CompletableFuture<? extends PropertyType> getActionTimeStampNodeAsync();

  /**
   * Reads the Value of the ActionTimeStamp child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable DateTime readActionTimeStamp() throws UaException;

  /**
   * Writes the Value of the ActionTimeStamp child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeActionTimeStamp(@Nullable DateTime value) throws UaException;

  /** Asynchronous form of {@link #readActionTimeStamp()}. */
  CompletableFuture<? extends @Nullable DateTime> readActionTimeStampAsync();

  /** Asynchronous form of {@link #writeActionTimeStamp}; completes with the operation status. */
  CompletableFuture<StatusCode> writeActionTimeStampAsync(@Nullable DateTime value);

  /**
   * Resolves the mandatory ClientAuditEntryId child, a PropertyType with DataType String.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getClientAuditEntryIdNode() throws UaException;

  /** Asynchronous form of {@link #getClientAuditEntryIdNode()}. */
  CompletableFuture<? extends PropertyType> getClientAuditEntryIdNodeAsync();

  /**
   * Reads the Value of the ClientAuditEntryId child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readClientAuditEntryId() throws UaException;

  /**
   * Writes the Value of the ClientAuditEntryId child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeClientAuditEntryId(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readClientAuditEntryId()}. */
  CompletableFuture<? extends @Nullable String> readClientAuditEntryIdAsync();

  /** Asynchronous form of {@link #writeClientAuditEntryId}; completes with the operation status. */
  CompletableFuture<StatusCode> writeClientAuditEntryIdAsync(@Nullable String value);

  /**
   * Resolves the optional ClientApplicationUri child, a PropertyType with DataType String.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getClientApplicationUriNode() throws UaException;

  /** Asynchronous form of {@link #getClientApplicationUriNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getClientApplicationUriNodeAsync();

  /**
   * Reads the Value of the ClientApplicationUri child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readClientApplicationUri() throws UaException;

  /**
   * Writes the Value of the ClientApplicationUri child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeClientApplicationUri(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readClientApplicationUri()}. */
  CompletableFuture<? extends @Nullable String> readClientApplicationUriAsync();

  /**
   * Asynchronous form of {@link #writeClientApplicationUri}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeClientApplicationUriAsync(@Nullable String value);

  /**
   * Resolves the mandatory Status child, a PropertyType with DataType Boolean.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getStatusNode() throws UaException;

  /** Asynchronous form of {@link #getStatusNode()}. */
  CompletableFuture<? extends PropertyType> getStatusNodeAsync();

  /**
   * Reads the Value of the Status child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Boolean readStatus() throws UaException;

  /**
   * Writes the Value of the Status child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeStatus(@Nullable Boolean value) throws UaException;

  /** Asynchronous form of {@link #readStatus()}. */
  CompletableFuture<? extends @Nullable Boolean> readStatusAsync();

  /** Asynchronous form of {@link #writeStatus}; completes with the operation status. */
  CompletableFuture<StatusCode> writeStatusAsync(@Nullable Boolean value);

  /**
   * Resolves the mandatory ServerId child, a PropertyType with DataType String.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getServerIdNode() throws UaException;

  /** Asynchronous form of {@link #getServerIdNode()}. */
  CompletableFuture<? extends PropertyType> getServerIdNodeAsync();

  /**
   * Reads the Value of the ServerId child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readServerId() throws UaException;

  /**
   * Writes the Value of the ServerId child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeServerId(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readServerId()}. */
  CompletableFuture<? extends @Nullable String> readServerIdAsync();

  /** Asynchronous form of {@link #writeServerId}; completes with the operation status. */
  CompletableFuture<StatusCode> writeServerIdAsync(@Nullable String value);
}

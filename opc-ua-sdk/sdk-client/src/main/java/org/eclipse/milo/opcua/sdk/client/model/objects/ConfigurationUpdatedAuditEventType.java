package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the ConfigurationUpdatedAuditEventType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.5/#7.8.5.8">Model
 *     documentation</a>
 */
public interface ConfigurationUpdatedAuditEventType extends AuditEventType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 15541L);

  QualifiedProperty<UInteger> NewVersion_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "NewVersion",
          ExpandedNodeId.of(Namespaces.OPC_UA, 20998L),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> OldVersion_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "OldVersion",
          ExpandedNodeId.of(Namespaces.OPC_UA, 20998L),
          -1,
          UInteger.class);

  /**
   * Resolves the mandatory NewVersion child, a PropertyType with DataType VersionTime.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getNewVersionNode() throws UaException;

  /** Asynchronous form of {@link #getNewVersionNode()}. */
  CompletableFuture<? extends PropertyType> getNewVersionNodeAsync();

  /**
   * Reads the Value of the NewVersion child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readNewVersion() throws UaException;

  /**
   * Writes the Value of the NewVersion child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeNewVersion(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readNewVersion()}. */
  CompletableFuture<? extends @Nullable UInteger> readNewVersionAsync();

  /** Asynchronous form of {@link #writeNewVersion}; completes with the operation status. */
  CompletableFuture<StatusCode> writeNewVersionAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory OldVersion child, a PropertyType with DataType VersionTime.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getOldVersionNode() throws UaException;

  /** Asynchronous form of {@link #getOldVersionNode()}. */
  CompletableFuture<? extends PropertyType> getOldVersionNodeAsync();

  /**
   * Reads the Value of the OldVersion child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readOldVersion() throws UaException;

  /**
   * Writes the Value of the OldVersion child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeOldVersion(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readOldVersion()}. */
  CompletableFuture<? extends @Nullable UInteger> readOldVersionAsync();

  /** Asynchronous form of {@link #writeOldVersion}; completes with the operation status. */
  CompletableFuture<StatusCode> writeOldVersionAsync(@Nullable UInteger value);
}

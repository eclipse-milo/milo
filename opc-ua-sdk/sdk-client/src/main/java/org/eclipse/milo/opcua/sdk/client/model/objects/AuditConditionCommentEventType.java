package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the AuditConditionCommentEventType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.10.4">Model
 *     documentation</a>
 */
public interface AuditConditionCommentEventType extends AuditConditionEventType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2829L);

  QualifiedProperty<ByteString> ConditionEventId_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ConditionEventId",
          ExpandedNodeId.of(Namespaces.OPC_UA, 15L),
          -1,
          ByteString.class);

  QualifiedProperty<LocalizedText> Comment_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "Comment",
          ExpandedNodeId.of(Namespaces.OPC_UA, 21L),
          -1,
          LocalizedText.class);

  /**
   * Resolves the mandatory ConditionEventId child, a PropertyType with DataType ByteString.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getConditionEventIdNode() throws UaException;

  /** Asynchronous form of {@link #getConditionEventIdNode()}. */
  CompletableFuture<? extends PropertyType> getConditionEventIdNodeAsync();

  /**
   * Reads the Value of the ConditionEventId child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable ByteString readConditionEventId() throws UaException;

  /**
   * Writes the Value of the ConditionEventId child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeConditionEventId(@Nullable ByteString value) throws UaException;

  /** Asynchronous form of {@link #readConditionEventId()}. */
  CompletableFuture<? extends @Nullable ByteString> readConditionEventIdAsync();

  /** Asynchronous form of {@link #writeConditionEventId}; completes with the operation status. */
  CompletableFuture<StatusCode> writeConditionEventIdAsync(@Nullable ByteString value);

  /**
   * Resolves the mandatory Comment child, a PropertyType with DataType LocalizedText.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getCommentNode() throws UaException;

  /** Asynchronous form of {@link #getCommentNode()}. */
  CompletableFuture<? extends PropertyType> getCommentNodeAsync();

  /**
   * Reads the Value of the Comment child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable LocalizedText readComment() throws UaException;

  /**
   * Writes the Value of the Comment child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeComment(@Nullable LocalizedText value) throws UaException;

  /** Asynchronous form of {@link #readComment()}. */
  CompletableFuture<? extends @Nullable LocalizedText> readCommentAsync();

  /** Asynchronous form of {@link #writeComment}; completes with the operation status. */
  CompletableFuture<StatusCode> writeCommentAsync(@Nullable LocalizedText value);
}

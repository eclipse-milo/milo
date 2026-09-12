/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.3">https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.3</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface AuditEventType extends BaseEventType {
  QualifiedProperty<DateTime> ACTION_TIME_STAMP =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ActionTimeStamp",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=294"),
          -1,
          DateTime.class);

  QualifiedProperty<Boolean> STATUS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Status",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<String> SERVER_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ServerId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<String> CLIENT_AUDIT_ENTRY_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ClientAuditEntryId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<String> CLIENT_USER_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ClientUserId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<String> CLIENT_APPLICATION_URI =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ClientApplicationUri",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  /** Gets the existing node's local value. */
  @Nullable DateTime getActionTimeStamp() throws UaException;

  /** Sets the existing node's local value. */
  void setActionTimeStamp(@Nullable DateTime value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable DateTime readActionTimeStamp() throws UaException;

  /** Writes the value remotely. */
  void writeActionTimeStamp(@Nullable DateTime value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable DateTime> readActionTimeStampAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeActionTimeStampAsync(@Nullable DateTime value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getActionTimeStampNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getActionTimeStampNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Boolean getStatus() throws UaException;

  /** Sets the existing node's local value. */
  void setStatus(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Boolean readStatus() throws UaException;

  /** Writes the value remotely. */
  void writeStatus(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Boolean> readStatusAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeStatusAsync(@Nullable Boolean value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getStatusNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getStatusNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String getServerId() throws UaException;

  /** Sets the existing node's local value. */
  void setServerId(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readServerId() throws UaException;

  /** Writes the value remotely. */
  void writeServerId(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readServerIdAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeServerIdAsync(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getServerIdNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getServerIdNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String getClientAuditEntryId() throws UaException;

  /** Sets the existing node's local value. */
  void setClientAuditEntryId(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readClientAuditEntryId() throws UaException;

  /** Writes the value remotely. */
  void writeClientAuditEntryId(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readClientAuditEntryIdAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeClientAuditEntryIdAsync(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getClientAuditEntryIdNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getClientAuditEntryIdNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String getClientUserId() throws UaException;

  /** Sets the existing node's local value. */
  void setClientUserId(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readClientUserId() throws UaException;

  /** Writes the value remotely. */
  void writeClientUserId(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readClientUserIdAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeClientUserIdAsync(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getClientUserIdNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getClientUserIdNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String getClientApplicationUri() throws UaException;

  /** Sets the existing node's local value. */
  void setClientApplicationUri(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readClientApplicationUri() throws UaException;

  /** Writes the value remotely. */
  void writeClientApplicationUri(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readClientApplicationUriAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeClientApplicationUriAsync(@Nullable String value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getClientApplicationUriNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getClientApplicationUriNodeAsync();
}

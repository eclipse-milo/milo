/*
 * Copyright (c) 2025 the Eclipse Milo Authors
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

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.3">https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.3</a>
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

  /**
   * Get the local value of the ActionTimeStamp Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ActionTimeStamp Node.
   * @throws UaException if an error occurs creating or getting the ActionTimeStamp Node.
   */
  DateTime getActionTimeStamp() throws UaException;

  /**
   * Set the local value of the ActionTimeStamp Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ActionTimeStamp Node.
   * @throws UaException if an error occurs creating or getting the ActionTimeStamp Node.
   */
  void setActionTimeStamp(DateTime value) throws UaException;

  /**
   * Read the value of the ActionTimeStamp Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link DateTime} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  DateTime readActionTimeStamp() throws UaException;

  /**
   * Write a new value for the ActionTimeStamp Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link DateTime} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeActionTimeStamp(DateTime value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readActionTimeStamp}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends DateTime> readActionTimeStampAsync();

  /**
   * An asynchronous implementation of {@link #writeActionTimeStamp}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeActionTimeStampAsync(DateTime value);

  /**
   * Get the ActionTimeStamp {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ActionTimeStamp {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getActionTimeStampNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getActionTimeStampNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getActionTimeStampNodeAsync();

  /**
   * Get the local value of the Status Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the Status Node.
   * @throws UaException if an error occurs creating or getting the Status Node.
   */
  Boolean getStatus() throws UaException;

  /**
   * Set the local value of the Status Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the Status Node.
   * @throws UaException if an error occurs creating or getting the Status Node.
   */
  void setStatus(Boolean value) throws UaException;

  /**
   * Read the value of the Status Node from the server and update the local value if the operation
   * succeeds.
   *
   * @return the {@link Boolean} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Boolean readStatus() throws UaException;

  /**
   * Write a new value for the Status Node to the server and update the local value if the operation
   * succeeds.
   *
   * @param value the {@link Boolean} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeStatus(Boolean value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readStatus}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Boolean> readStatusAsync();

  /**
   * An asynchronous implementation of {@link #writeStatus}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeStatusAsync(Boolean value);

  /**
   * Get the Status {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the Status {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getStatusNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getStatusNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getStatusNodeAsync();

  /**
   * Get the local value of the ServerId Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ServerId Node.
   * @throws UaException if an error occurs creating or getting the ServerId Node.
   */
  String getServerId() throws UaException;

  /**
   * Set the local value of the ServerId Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ServerId Node.
   * @throws UaException if an error occurs creating or getting the ServerId Node.
   */
  void setServerId(String value) throws UaException;

  /**
   * Read the value of the ServerId Node from the server and update the local value if the operation
   * succeeds.
   *
   * @return the {@link String} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String readServerId() throws UaException;

  /**
   * Write a new value for the ServerId Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link String} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeServerId(String value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readServerId}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String> readServerIdAsync();

  /**
   * An asynchronous implementation of {@link #writeServerId}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeServerIdAsync(String value);

  /**
   * Get the ServerId {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ServerId {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getServerIdNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getServerIdNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getServerIdNodeAsync();

  /**
   * Get the local value of the ClientAuditEntryId Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ClientAuditEntryId Node.
   * @throws UaException if an error occurs creating or getting the ClientAuditEntryId Node.
   */
  String getClientAuditEntryId() throws UaException;

  /**
   * Set the local value of the ClientAuditEntryId Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ClientAuditEntryId Node.
   * @throws UaException if an error occurs creating or getting the ClientAuditEntryId Node.
   */
  void setClientAuditEntryId(String value) throws UaException;

  /**
   * Read the value of the ClientAuditEntryId Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link String} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String readClientAuditEntryId() throws UaException;

  /**
   * Write a new value for the ClientAuditEntryId Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link String} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeClientAuditEntryId(String value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readClientAuditEntryId}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String> readClientAuditEntryIdAsync();

  /**
   * An asynchronous implementation of {@link #writeClientAuditEntryId}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeClientAuditEntryIdAsync(String value);

  /**
   * Get the ClientAuditEntryId {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ClientAuditEntryId {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getClientAuditEntryIdNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getClientAuditEntryIdNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getClientAuditEntryIdNodeAsync();

  /**
   * Get the local value of the ClientUserId Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ClientUserId Node.
   * @throws UaException if an error occurs creating or getting the ClientUserId Node.
   */
  String getClientUserId() throws UaException;

  /**
   * Set the local value of the ClientUserId Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ClientUserId Node.
   * @throws UaException if an error occurs creating or getting the ClientUserId Node.
   */
  void setClientUserId(String value) throws UaException;

  /**
   * Read the value of the ClientUserId Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link String} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String readClientUserId() throws UaException;

  /**
   * Write a new value for the ClientUserId Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link String} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeClientUserId(String value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readClientUserId}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String> readClientUserIdAsync();

  /**
   * An asynchronous implementation of {@link #writeClientUserId}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeClientUserIdAsync(String value);

  /**
   * Get the ClientUserId {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ClientUserId {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getClientUserIdNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getClientUserIdNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getClientUserIdNodeAsync();
}

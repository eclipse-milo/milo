/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.model.variables;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.19">https://reference.opcfoundation.org/v105/Core/docs/Part5/7.19</a>
 */
public interface AudioVariableType extends BaseDataVariableType {
  QualifiedProperty<String> LIST_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ListId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<String> AGENCY_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "AgencyId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<String> VERSION_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "VersionId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  /**
   * Get the local value of the ListId Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ListId Node.
   * @throws UaException if an error occurs creating or getting the ListId Node.
   */
  String getListId() throws UaException;

  /**
   * Set the local value of the ListId Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ListId Node.
   * @throws UaException if an error occurs creating or getting the ListId Node.
   */
  void setListId(String value) throws UaException;

  /**
   * Read the value of the ListId Node from the server and update the local value if the operation
   * succeeds.
   *
   * @return the {@link String} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String readListId() throws UaException;

  /**
   * Write a new value for the ListId Node to the server and update the local value if the operation
   * succeeds.
   *
   * @param value the {@link String} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeListId(String value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readListId}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String> readListIdAsync();

  /**
   * An asynchronous implementation of {@link #writeListId}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeListIdAsync(String value);

  /**
   * Get the ListId {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ListId {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getListIdNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getListIdNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getListIdNodeAsync();

  /**
   * Get the local value of the AgencyId Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the AgencyId Node.
   * @throws UaException if an error occurs creating or getting the AgencyId Node.
   */
  String getAgencyId() throws UaException;

  /**
   * Set the local value of the AgencyId Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the AgencyId Node.
   * @throws UaException if an error occurs creating or getting the AgencyId Node.
   */
  void setAgencyId(String value) throws UaException;

  /**
   * Read the value of the AgencyId Node from the server and update the local value if the operation
   * succeeds.
   *
   * @return the {@link String} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String readAgencyId() throws UaException;

  /**
   * Write a new value for the AgencyId Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link String} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeAgencyId(String value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readAgencyId}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String> readAgencyIdAsync();

  /**
   * An asynchronous implementation of {@link #writeAgencyId}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeAgencyIdAsync(String value);

  /**
   * Get the AgencyId {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the AgencyId {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getAgencyIdNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getAgencyIdNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getAgencyIdNodeAsync();

  /**
   * Get the local value of the VersionId Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the VersionId Node.
   * @throws UaException if an error occurs creating or getting the VersionId Node.
   */
  String getVersionId() throws UaException;

  /**
   * Set the local value of the VersionId Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the VersionId Node.
   * @throws UaException if an error occurs creating or getting the VersionId Node.
   */
  void setVersionId(String value) throws UaException;

  /**
   * Read the value of the VersionId Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link String} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String readVersionId() throws UaException;

  /**
   * Write a new value for the VersionId Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link String} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeVersionId(String value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readVersionId}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String> readVersionIdAsync();

  /**
   * An asynchronous implementation of {@link #writeVersionId}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeVersionIdAsync(String value);

  /**
   * Get the VersionId {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the VersionId {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getVersionIdNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getVersionIdNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getVersionIdNodeAsync();
}

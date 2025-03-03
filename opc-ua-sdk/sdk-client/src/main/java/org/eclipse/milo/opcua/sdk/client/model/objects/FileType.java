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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.1">https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.1</a>
 */
public interface FileType extends BaseObjectType {
  QualifiedProperty<ULong> SIZE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Size",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=9"),
          -1,
          ULong.class);

  QualifiedProperty<Boolean> WRITABLE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Writable",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> USER_WRITABLE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "UserWritable",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<UShort> OPEN_COUNT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "OpenCount",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=5"),
          -1,
          UShort.class);

  QualifiedProperty<String> MIME_TYPE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MimeType",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<UInteger> MAX_BYTE_STRING_LENGTH =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxByteStringLength",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<DateTime> LAST_MODIFIED_TIME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "LastModifiedTime",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=13"),
          -1,
          DateTime.class);

  /**
   * Get the local value of the Size Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the Size Node.
   * @throws UaException if an error occurs creating or getting the Size Node.
   */
  ULong getSize() throws UaException;

  /**
   * Set the local value of the Size Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the Size Node.
   * @throws UaException if an error occurs creating or getting the Size Node.
   */
  void setSize(ULong value) throws UaException;

  /**
   * Read the value of the Size Node from the server and update the local value if the operation
   * succeeds.
   *
   * @return the {@link ULong} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  ULong readSize() throws UaException;

  /**
   * Write a new value for the Size Node to the server and update the local value if the operation
   * succeeds.
   *
   * @param value the {@link ULong} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeSize(ULong value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readSize}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends ULong> readSizeAsync();

  /**
   * An asynchronous implementation of {@link #writeSize}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeSizeAsync(ULong value);

  /**
   * Get the Size {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the Size {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getSizeNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSizeNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getSizeNodeAsync();

  /**
   * Get the local value of the Writable Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the Writable Node.
   * @throws UaException if an error occurs creating or getting the Writable Node.
   */
  Boolean getWritable() throws UaException;

  /**
   * Set the local value of the Writable Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the Writable Node.
   * @throws UaException if an error occurs creating or getting the Writable Node.
   */
  void setWritable(Boolean value) throws UaException;

  /**
   * Read the value of the Writable Node from the server and update the local value if the operation
   * succeeds.
   *
   * @return the {@link Boolean} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Boolean readWritable() throws UaException;

  /**
   * Write a new value for the Writable Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link Boolean} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeWritable(Boolean value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readWritable}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Boolean> readWritableAsync();

  /**
   * An asynchronous implementation of {@link #writeWritable}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeWritableAsync(Boolean value);

  /**
   * Get the Writable {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the Writable {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getWritableNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getWritableNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getWritableNodeAsync();

  /**
   * Get the local value of the UserWritable Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the UserWritable Node.
   * @throws UaException if an error occurs creating or getting the UserWritable Node.
   */
  Boolean getUserWritable() throws UaException;

  /**
   * Set the local value of the UserWritable Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the UserWritable Node.
   * @throws UaException if an error occurs creating or getting the UserWritable Node.
   */
  void setUserWritable(Boolean value) throws UaException;

  /**
   * Read the value of the UserWritable Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link Boolean} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Boolean readUserWritable() throws UaException;

  /**
   * Write a new value for the UserWritable Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link Boolean} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeUserWritable(Boolean value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readUserWritable}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Boolean> readUserWritableAsync();

  /**
   * An asynchronous implementation of {@link #writeUserWritable}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeUserWritableAsync(Boolean value);

  /**
   * Get the UserWritable {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the UserWritable {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getUserWritableNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getUserWritableNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getUserWritableNodeAsync();

  /**
   * Get the local value of the OpenCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the OpenCount Node.
   * @throws UaException if an error occurs creating or getting the OpenCount Node.
   */
  UShort getOpenCount() throws UaException;

  /**
   * Set the local value of the OpenCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the OpenCount Node.
   * @throws UaException if an error occurs creating or getting the OpenCount Node.
   */
  void setOpenCount(UShort value) throws UaException;

  /**
   * Read the value of the OpenCount Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link UShort} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UShort readOpenCount() throws UaException;

  /**
   * Write a new value for the OpenCount Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link UShort} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeOpenCount(UShort value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readOpenCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UShort> readOpenCountAsync();

  /**
   * An asynchronous implementation of {@link #writeOpenCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeOpenCountAsync(UShort value);

  /**
   * Get the OpenCount {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the OpenCount {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getOpenCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getOpenCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getOpenCountNodeAsync();

  /**
   * Get the local value of the MimeType Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the MimeType Node.
   * @throws UaException if an error occurs creating or getting the MimeType Node.
   */
  String getMimeType() throws UaException;

  /**
   * Set the local value of the MimeType Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the MimeType Node.
   * @throws UaException if an error occurs creating or getting the MimeType Node.
   */
  void setMimeType(String value) throws UaException;

  /**
   * Read the value of the MimeType Node from the server and update the local value if the operation
   * succeeds.
   *
   * @return the {@link String} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String readMimeType() throws UaException;

  /**
   * Write a new value for the MimeType Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link String} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeMimeType(String value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readMimeType}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String> readMimeTypeAsync();

  /**
   * An asynchronous implementation of {@link #writeMimeType}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeMimeTypeAsync(String value);

  /**
   * Get the MimeType {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the MimeType {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getMimeTypeNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getMimeTypeNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getMimeTypeNodeAsync();

  /**
   * Get the local value of the MaxByteStringLength Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the MaxByteStringLength Node.
   * @throws UaException if an error occurs creating or getting the MaxByteStringLength Node.
   */
  UInteger getMaxByteStringLength() throws UaException;

  /**
   * Set the local value of the MaxByteStringLength Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the MaxByteStringLength Node.
   * @throws UaException if an error occurs creating or getting the MaxByteStringLength Node.
   */
  void setMaxByteStringLength(UInteger value) throws UaException;

  /**
   * Read the value of the MaxByteStringLength Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readMaxByteStringLength() throws UaException;

  /**
   * Write a new value for the MaxByteStringLength Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeMaxByteStringLength(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readMaxByteStringLength}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readMaxByteStringLengthAsync();

  /**
   * An asynchronous implementation of {@link #writeMaxByteStringLength}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeMaxByteStringLengthAsync(UInteger value);

  /**
   * Get the MaxByteStringLength {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the MaxByteStringLength {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getMaxByteStringLengthNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getMaxByteStringLengthNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getMaxByteStringLengthNodeAsync();

  /**
   * Get the local value of the LastModifiedTime Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the LastModifiedTime Node.
   * @throws UaException if an error occurs creating or getting the LastModifiedTime Node.
   */
  DateTime getLastModifiedTime() throws UaException;

  /**
   * Set the local value of the LastModifiedTime Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the LastModifiedTime Node.
   * @throws UaException if an error occurs creating or getting the LastModifiedTime Node.
   */
  void setLastModifiedTime(DateTime value) throws UaException;

  /**
   * Read the value of the LastModifiedTime Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link DateTime} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  DateTime readLastModifiedTime() throws UaException;

  /**
   * Write a new value for the LastModifiedTime Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link DateTime} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeLastModifiedTime(DateTime value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readLastModifiedTime}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends DateTime> readLastModifiedTimeAsync();

  /**
   * An asynchronous implementation of {@link #writeLastModifiedTime}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeLastModifiedTimeAsync(DateTime value);

  /**
   * Get the LastModifiedTime {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the LastModifiedTime {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getLastModifiedTimeNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getLastModifiedTimeNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getLastModifiedTimeNodeAsync();
}

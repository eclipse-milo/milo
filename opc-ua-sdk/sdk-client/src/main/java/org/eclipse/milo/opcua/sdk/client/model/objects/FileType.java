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

import com.digitalpetri.opcua.uanodeset.runtime.methods.MethodCallOptions;
import com.digitalpetri.opcua.uanodeset.runtime.methods.MethodCallResult;
import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.1">https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.1</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
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

  /** Gets the existing node's local value. */
  @Nullable ULong getSize() throws UaException;

  /** Sets the existing node's local value. */
  void setSize(@Nullable ULong value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable ULong readSize() throws UaException;

  /** Writes the value remotely. */
  void writeSize(@Nullable ULong value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable ULong> readSizeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSizeAsync(@Nullable ULong value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getSizeNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getSizeNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Boolean getWritable() throws UaException;

  /** Sets the existing node's local value. */
  void setWritable(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Boolean readWritable() throws UaException;

  /** Writes the value remotely. */
  void writeWritable(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Boolean> readWritableAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeWritableAsync(@Nullable Boolean value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getWritableNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getWritableNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Boolean getUserWritable() throws UaException;

  /** Sets the existing node's local value. */
  void setUserWritable(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Boolean readUserWritable() throws UaException;

  /** Writes the value remotely. */
  void writeUserWritable(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Boolean> readUserWritableAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeUserWritableAsync(@Nullable Boolean value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getUserWritableNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getUserWritableNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UShort getOpenCount() throws UaException;

  /** Sets the existing node's local value. */
  void setOpenCount(@Nullable UShort value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UShort readOpenCount() throws UaException;

  /** Writes the value remotely. */
  void writeOpenCount(@Nullable UShort value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UShort> readOpenCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeOpenCountAsync(@Nullable UShort value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getOpenCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getOpenCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String getMimeType() throws UaException;

  /** Sets the existing node's local value. */
  void setMimeType(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readMimeType() throws UaException;

  /** Writes the value remotely. */
  void writeMimeType(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readMimeTypeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMimeTypeAsync(@Nullable String value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMimeTypeNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getMimeTypeNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxByteStringLength() throws UaException;

  /** Sets the existing node's local value. */
  void setMaxByteStringLength(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readMaxByteStringLength() throws UaException;

  /** Writes the value remotely. */
  void writeMaxByteStringLength(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readMaxByteStringLengthAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMaxByteStringLengthAsync(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMaxByteStringLengthNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getMaxByteStringLengthNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable DateTime getLastModifiedTime() throws UaException;

  /** Sets the existing node's local value. */
  void setLastModifiedTime(@Nullable DateTime value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable DateTime readLastModifiedTime() throws UaException;

  /** Writes the value remotely. */
  void writeLastModifiedTime(@Nullable DateTime value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable DateTime> readLastModifiedTimeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeLastModifiedTimeAsync(@Nullable DateTime value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getLastModifiedTimeNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getLastModifiedTimeNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.2
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  @NullMarked
  UaMethodNode getOpenMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.2
   *
   * <p>Returns the required node.
   *
   * @return a future completing with the required node.
   */
  @NullMarked
  CompletableFuture<? extends UaMethodNode> getOpenMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.2
   *
   * <p>Invokes <code>Open</code> on this node's ObjectId using the effective Method contract.
   * Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  @Nullable UInteger callOpen(@Nullable UByte mode) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.2
   *
   * <p>Invokes <code>Open</code> on this node's ObjectId using the effective Method contract.
   * Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UInteger> callOpenAsync(@Nullable UByte mode);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.2
   *
   * <p>Invokes <code>Open</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable UInteger> callOpenDetailed(@Nullable UByte mode)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.2
   *
   * <p>Invokes <code>Open</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable UInteger> callOpenDetailed(
      MethodCallOptions options, @Nullable UByte mode) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.2
   *
   * <p>Invokes <code>Open</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable UInteger>> callOpenDetailedAsync(
      @Nullable UByte mode);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.2
   *
   * <p>Invokes <code>Open</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable UInteger>> callOpenDetailedAsync(
      MethodCallOptions options, @Nullable UByte mode);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.3
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  @NullMarked
  UaMethodNode getCloseMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.3
   *
   * <p>Returns the required node.
   *
   * @return a future completing with the required node.
   */
  @NullMarked
  CompletableFuture<? extends UaMethodNode> getCloseMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.3
   *
   * <p>Invokes <code>Close</code> on this node's ObjectId using the effective Method contract.
   * Requires Good operation status.
   *
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  void callClose(@Nullable UInteger fileHandle) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.3
   *
   * <p>Invokes <code>Close</code> on this node's ObjectId using the effective Method contract.
   * Requires Good operation status.
   *
   * @return a future whose successful payload is null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Void> callCloseAsync(@Nullable UInteger fileHandle);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.3
   *
   * <p>Invokes <code>Close</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callCloseDetailed(@Nullable UInteger fileHandle)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.3
   *
   * <p>Invokes <code>Close</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callCloseDetailed(
      MethodCallOptions options, @Nullable UInteger fileHandle) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.3
   *
   * <p>Invokes <code>Close</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>> callCloseDetailedAsync(
      @Nullable UInteger fileHandle);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.3
   *
   * <p>Invokes <code>Close</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>> callCloseDetailedAsync(
      MethodCallOptions options, @Nullable UInteger fileHandle);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.4
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  @NullMarked
  UaMethodNode getReadMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.4
   *
   * <p>Returns the required node.
   *
   * @return a future completing with the required node.
   */
  @NullMarked
  CompletableFuture<? extends UaMethodNode> getReadMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.4
   *
   * <p>Invokes <code>Read</code> on this node's ObjectId using the effective Method contract.
   * Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  @Nullable ByteString callRead(@Nullable UInteger fileHandle, @Nullable Integer length)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.4
   *
   * <p>Invokes <code>Read</code> on this node's ObjectId using the effective Method contract.
   * Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable ByteString> callReadAsync(
      @Nullable UInteger fileHandle, @Nullable Integer length);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.4
   *
   * <p>Invokes <code>Read</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable ByteString> callReadDetailed(
      @Nullable UInteger fileHandle, @Nullable Integer length) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.4
   *
   * <p>Invokes <code>Read</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable ByteString> callReadDetailed(
      MethodCallOptions options, @Nullable UInteger fileHandle, @Nullable Integer length)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.4
   *
   * <p>Invokes <code>Read</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable ByteString>>
      callReadDetailedAsync(@Nullable UInteger fileHandle, @Nullable Integer length);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.4
   *
   * <p>Invokes <code>Read</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable ByteString>>
      callReadDetailedAsync(
          MethodCallOptions options, @Nullable UInteger fileHandle, @Nullable Integer length);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.5
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  @NullMarked
  UaMethodNode getWriteMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.5
   *
   * <p>Returns the required node.
   *
   * @return a future completing with the required node.
   */
  @NullMarked
  CompletableFuture<? extends UaMethodNode> getWriteMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.5
   *
   * <p>Invokes <code>Write</code> on this node's ObjectId using the effective Method contract.
   * Requires Good operation status.
   *
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  void callWrite(@Nullable UInteger fileHandle, @Nullable ByteString data) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.5
   *
   * <p>Invokes <code>Write</code> on this node's ObjectId using the effective Method contract.
   * Requires Good operation status.
   *
   * @return a future whose successful payload is null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Void> callWriteAsync(
      @Nullable UInteger fileHandle, @Nullable ByteString data);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.5
   *
   * <p>Invokes <code>Write</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callWriteDetailed(
      @Nullable UInteger fileHandle, @Nullable ByteString data) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.5
   *
   * <p>Invokes <code>Write</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callWriteDetailed(
      MethodCallOptions options, @Nullable UInteger fileHandle, @Nullable ByteString data)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.5
   *
   * <p>Invokes <code>Write</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>> callWriteDetailedAsync(
      @Nullable UInteger fileHandle, @Nullable ByteString data);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.5
   *
   * <p>Invokes <code>Write</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>> callWriteDetailedAsync(
      MethodCallOptions options, @Nullable UInteger fileHandle, @Nullable ByteString data);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.6
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  @NullMarked
  UaMethodNode getGetPositionMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.6
   *
   * <p>Returns the required node.
   *
   * @return a future completing with the required node.
   */
  @NullMarked
  CompletableFuture<? extends UaMethodNode> getGetPositionMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.6
   *
   * <p>Invokes <code>GetPosition</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  @Nullable ULong callGetPosition(@Nullable UInteger fileHandle) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.6
   *
   * <p>Invokes <code>GetPosition</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable ULong> callGetPositionAsync(@Nullable UInteger fileHandle);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.6
   *
   * <p>Invokes <code>GetPosition</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable ULong> callGetPositionDetailed(@Nullable UInteger fileHandle)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.6
   *
   * <p>Invokes <code>GetPosition</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable ULong> callGetPositionDetailed(
      MethodCallOptions options, @Nullable UInteger fileHandle) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.6
   *
   * <p>Invokes <code>GetPosition</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable ULong>>
      callGetPositionDetailedAsync(@Nullable UInteger fileHandle);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.6
   *
   * <p>Invokes <code>GetPosition</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable ULong>>
      callGetPositionDetailedAsync(MethodCallOptions options, @Nullable UInteger fileHandle);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.7
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  @NullMarked
  UaMethodNode getSetPositionMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.7
   *
   * <p>Returns the required node.
   *
   * @return a future completing with the required node.
   */
  @NullMarked
  CompletableFuture<? extends UaMethodNode> getSetPositionMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.7
   *
   * <p>Invokes <code>SetPosition</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  void callSetPosition(@Nullable UInteger fileHandle, @Nullable ULong position) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.7
   *
   * <p>Invokes <code>SetPosition</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return a future whose successful payload is null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Void> callSetPositionAsync(
      @Nullable UInteger fileHandle, @Nullable ULong position);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.7
   *
   * <p>Invokes <code>SetPosition</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callSetPositionDetailed(
      @Nullable UInteger fileHandle, @Nullable ULong position) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.7
   *
   * <p>Invokes <code>SetPosition</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callSetPositionDetailed(
      MethodCallOptions options, @Nullable UInteger fileHandle, @Nullable ULong position)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.7
   *
   * <p>Invokes <code>SetPosition</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callSetPositionDetailedAsync(@Nullable UInteger fileHandle, @Nullable ULong position);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.7
   *
   * <p>Invokes <code>SetPosition</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callSetPositionDetailedAsync(
          MethodCallOptions options, @Nullable UInteger fileHandle, @Nullable ULong position);
}

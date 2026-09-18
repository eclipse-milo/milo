package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
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
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the FileType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.1">Model
 *     documentation</a>
 */
public interface FileType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 11575L);

  QualifiedProperty<Boolean> UserWritable_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "UserWritable",
          ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
          -1,
          Boolean.class);

  QualifiedProperty<DateTime> LastModifiedTime_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "LastModifiedTime",
          ExpandedNodeId.of(Namespaces.OPC_UA, 13L),
          -1,
          DateTime.class);

  QualifiedProperty<UInteger> MaxByteStringLength_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "MaxByteStringLength",
          ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
          -1,
          UInteger.class);

  QualifiedProperty<ULong> Size_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA, "Size", ExpandedNodeId.of(Namespaces.OPC_UA, 9L), -1, ULong.class);

  QualifiedProperty<String> MimeType_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "MimeType",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
          -1,
          String.class);

  QualifiedProperty<Boolean> Writable_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "Writable",
          ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
          -1,
          Boolean.class);

  QualifiedProperty<UShort> OpenCount_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "OpenCount",
          ExpandedNodeId.of(Namespaces.OPC_UA, 5L),
          -1,
          UShort.class);

  /**
   * Resolves the mandatory UserWritable child, a PropertyType with DataType Boolean.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getUserWritableNode() throws UaException;

  /** Asynchronous form of {@link #getUserWritableNode()}. */
  CompletableFuture<? extends PropertyType> getUserWritableNodeAsync();

  /**
   * Reads the Value of the UserWritable child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Boolean readUserWritable() throws UaException;

  /**
   * Writes the Value of the UserWritable child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeUserWritable(@Nullable Boolean value) throws UaException;

  /** Asynchronous form of {@link #readUserWritable()}. */
  CompletableFuture<? extends @Nullable Boolean> readUserWritableAsync();

  /** Asynchronous form of {@link #writeUserWritable}; completes with the operation status. */
  CompletableFuture<StatusCode> writeUserWritableAsync(@Nullable Boolean value);

  /**
   * Resolves the optional LastModifiedTime child, a PropertyType with DataType DateTime.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getLastModifiedTimeNode() throws UaException;

  /** Asynchronous form of {@link #getLastModifiedTimeNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getLastModifiedTimeNodeAsync();

  /**
   * Reads the Value of the LastModifiedTime child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable DateTime readLastModifiedTime() throws UaException;

  /**
   * Writes the Value of the LastModifiedTime child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeLastModifiedTime(@Nullable DateTime value) throws UaException;

  /** Asynchronous form of {@link #readLastModifiedTime()}. */
  CompletableFuture<? extends @Nullable DateTime> readLastModifiedTimeAsync();

  /** Asynchronous form of {@link #writeLastModifiedTime}; completes with the operation status. */
  CompletableFuture<StatusCode> writeLastModifiedTimeAsync(@Nullable DateTime value);

  /**
   * Resolves the optional MaxByteStringLength child, a PropertyType with DataType UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getMaxByteStringLengthNode() throws UaException;

  /** Asynchronous form of {@link #getMaxByteStringLengthNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getMaxByteStringLengthNodeAsync();

  /**
   * Reads the Value of the MaxByteStringLength child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readMaxByteStringLength() throws UaException;

  /**
   * Writes the Value of the MaxByteStringLength child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMaxByteStringLength(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readMaxByteStringLength()}. */
  CompletableFuture<? extends @Nullable UInteger> readMaxByteStringLengthAsync();

  /**
   * Asynchronous form of {@link #writeMaxByteStringLength}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeMaxByteStringLengthAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory Size child, a PropertyType with DataType UInt64.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getSizeNode() throws UaException;

  /** Asynchronous form of {@link #getSizeNode()}. */
  CompletableFuture<? extends PropertyType> getSizeNodeAsync();

  /**
   * Reads the Value of the Size child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable ULong readSize() throws UaException;

  /**
   * Writes the Value of the Size child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSize(@Nullable ULong value) throws UaException;

  /** Asynchronous form of {@link #readSize()}. */
  CompletableFuture<? extends @Nullable ULong> readSizeAsync();

  /** Asynchronous form of {@link #writeSize}; completes with the operation status. */
  CompletableFuture<StatusCode> writeSizeAsync(@Nullable ULong value);

  /**
   * Resolves the optional MimeType child, a PropertyType with DataType String.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getMimeTypeNode() throws UaException;

  /** Asynchronous form of {@link #getMimeTypeNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getMimeTypeNodeAsync();

  /**
   * Reads the Value of the MimeType child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readMimeType() throws UaException;

  /**
   * Writes the Value of the MimeType child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMimeType(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readMimeType()}. */
  CompletableFuture<? extends @Nullable String> readMimeTypeAsync();

  /** Asynchronous form of {@link #writeMimeType}; completes with the operation status. */
  CompletableFuture<StatusCode> writeMimeTypeAsync(@Nullable String value);

  /**
   * Resolves the mandatory Writable child, a PropertyType with DataType Boolean.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getWritableNode() throws UaException;

  /** Asynchronous form of {@link #getWritableNode()}. */
  CompletableFuture<? extends PropertyType> getWritableNodeAsync();

  /**
   * Reads the Value of the Writable child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Boolean readWritable() throws UaException;

  /**
   * Writes the Value of the Writable child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeWritable(@Nullable Boolean value) throws UaException;

  /** Asynchronous form of {@link #readWritable()}. */
  CompletableFuture<? extends @Nullable Boolean> readWritableAsync();

  /** Asynchronous form of {@link #writeWritable}; completes with the operation status. */
  CompletableFuture<StatusCode> writeWritableAsync(@Nullable Boolean value);

  /**
   * Resolves the mandatory OpenCount child, a PropertyType with DataType UInt16.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getOpenCountNode() throws UaException;

  /** Asynchronous form of {@link #getOpenCountNode()}. */
  CompletableFuture<? extends PropertyType> getOpenCountNodeAsync();

  /**
   * Reads the Value of the OpenCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UShort readOpenCount() throws UaException;

  /**
   * Writes the Value of the OpenCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeOpenCount(@Nullable UShort value) throws UaException;

  /** Asynchronous form of {@link #readOpenCount()}. */
  CompletableFuture<? extends @Nullable UShort> readOpenCountAsync();

  /** Asynchronous form of {@link #writeOpenCount}; completes with the operation status. */
  CompletableFuture<StatusCode> writeOpenCountAsync(@Nullable UShort value);

  /**
   * Resolves the mandatory Close Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.3">Model
   *     documentation</a>
   */
  UaMethodNode getCloseMethodNode() throws UaException;

  /** Asynchronous form of {@link #getCloseMethodNode()}. */
  CompletableFuture<UaMethodNode> getCloseMethodNodeAsync();

  /**
   * Calls the Close Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.3">Model
   *     documentation</a>
   */
  void close(@Nullable UInteger fileHandle) throws UaException;

  /**
   * Calls the Close Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callClose(@Nullable UInteger fileHandle) throws UaException;

  /**
   * Calls the Close Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callCloseWith(MethodCallOptions options, @Nullable UInteger fileHandle)
      throws UaException;

  /** Asynchronous form of {@link #close}. */
  CompletableFuture<Void> closeAsync(@Nullable UInteger fileHandle);

  /** Asynchronous form of {@link #callClose}. */
  CompletableFuture<MethodCallResult<Void>> callCloseAsync(@Nullable UInteger fileHandle);

  /** Asynchronous form of {@link #callCloseWith}. */
  CompletableFuture<MethodCallResult<Void>> callCloseWithAsync(
      MethodCallOptions options, @Nullable UInteger fileHandle);

  /**
   * Resolves the mandatory GetPosition Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.6">Model
   *     documentation</a>
   */
  UaMethodNode getGetPositionMethodNode() throws UaException;

  /** Asynchronous form of {@link #getGetPositionMethodNode()}. */
  CompletableFuture<UaMethodNode> getGetPositionMethodNodeAsync();

  /**
   * Calls the GetPosition Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.6">Model
   *     documentation</a>
   */
  @Nullable ULong getPosition(@Nullable UInteger fileHandle) throws UaException;

  /**
   * Calls the GetPosition Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<@Nullable ULong> callGetPosition(@Nullable UInteger fileHandle)
      throws UaException;

  /**
   * Calls the GetPosition Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<@Nullable ULong> callGetPositionWith(
      MethodCallOptions options, @Nullable UInteger fileHandle) throws UaException;

  /** Asynchronous form of {@link #getPosition}. */
  CompletableFuture<@Nullable ULong> getPositionAsync(@Nullable UInteger fileHandle);

  /** Asynchronous form of {@link #callGetPosition}. */
  CompletableFuture<MethodCallResult<@Nullable ULong>> callGetPositionAsync(
      @Nullable UInteger fileHandle);

  /** Asynchronous form of {@link #callGetPositionWith}. */
  CompletableFuture<MethodCallResult<@Nullable ULong>> callGetPositionWithAsync(
      MethodCallOptions options, @Nullable UInteger fileHandle);

  /**
   * Resolves the mandatory Open Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.2">Model
   *     documentation</a>
   */
  UaMethodNode getOpenMethodNode() throws UaException;

  /** Asynchronous form of {@link #getOpenMethodNode()}. */
  CompletableFuture<UaMethodNode> getOpenMethodNodeAsync();

  /**
   * Calls the Open Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.2">Model
   *     documentation</a>
   */
  @Nullable UInteger open(@Nullable UByte mode) throws UaException;

  /**
   * Calls the Open Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<@Nullable UInteger> callOpen(@Nullable UByte mode) throws UaException;

  /**
   * Calls the Open Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<@Nullable UInteger> callOpenWith(MethodCallOptions options, @Nullable UByte mode)
      throws UaException;

  /** Asynchronous form of {@link #open}. */
  CompletableFuture<@Nullable UInteger> openAsync(@Nullable UByte mode);

  /** Asynchronous form of {@link #callOpen}. */
  CompletableFuture<MethodCallResult<@Nullable UInteger>> callOpenAsync(@Nullable UByte mode);

  /** Asynchronous form of {@link #callOpenWith}. */
  CompletableFuture<MethodCallResult<@Nullable UInteger>> callOpenWithAsync(
      MethodCallOptions options, @Nullable UByte mode);

  /**
   * Resolves the mandatory Read Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.4">Model
   *     documentation</a>
   */
  UaMethodNode getReadMethodNode() throws UaException;

  /** Asynchronous form of {@link #getReadMethodNode()}. */
  CompletableFuture<UaMethodNode> getReadMethodNodeAsync();

  /**
   * Calls the Read Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.4">Model
   *     documentation</a>
   */
  @Nullable ByteString read(@Nullable UInteger fileHandle, @Nullable Integer length)
      throws UaException;

  /**
   * Calls the Read Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<@Nullable ByteString> callRead(
      @Nullable UInteger fileHandle, @Nullable Integer length) throws UaException;

  /**
   * Calls the Read Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<@Nullable ByteString> callReadWith(
      MethodCallOptions options, @Nullable UInteger fileHandle, @Nullable Integer length)
      throws UaException;

  /** Asynchronous form of {@link #read}. */
  CompletableFuture<@Nullable ByteString> readAsync(
      @Nullable UInteger fileHandle, @Nullable Integer length);

  /** Asynchronous form of {@link #callRead}. */
  CompletableFuture<MethodCallResult<@Nullable ByteString>> callReadAsync(
      @Nullable UInteger fileHandle, @Nullable Integer length);

  /** Asynchronous form of {@link #callReadWith}. */
  CompletableFuture<MethodCallResult<@Nullable ByteString>> callReadWithAsync(
      MethodCallOptions options, @Nullable UInteger fileHandle, @Nullable Integer length);

  /**
   * Resolves the mandatory SetPosition Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.7">Model
   *     documentation</a>
   */
  UaMethodNode getSetPositionMethodNode() throws UaException;

  /** Asynchronous form of {@link #getSetPositionMethodNode()}. */
  CompletableFuture<UaMethodNode> getSetPositionMethodNodeAsync();

  /**
   * Calls the SetPosition Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.7">Model
   *     documentation</a>
   */
  void setPosition(@Nullable UInteger fileHandle, @Nullable ULong position) throws UaException;

  /**
   * Calls the SetPosition Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callSetPosition(@Nullable UInteger fileHandle, @Nullable ULong position)
      throws UaException;

  /**
   * Calls the SetPosition Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callSetPositionWith(
      MethodCallOptions options, @Nullable UInteger fileHandle, @Nullable ULong position)
      throws UaException;

  /** Asynchronous form of {@link #setPosition}. */
  CompletableFuture<Void> setPositionAsync(@Nullable UInteger fileHandle, @Nullable ULong position);

  /** Asynchronous form of {@link #callSetPosition}. */
  CompletableFuture<MethodCallResult<Void>> callSetPositionAsync(
      @Nullable UInteger fileHandle, @Nullable ULong position);

  /** Asynchronous form of {@link #callSetPositionWith}. */
  CompletableFuture<MethodCallResult<Void>> callSetPositionWithAsync(
      MethodCallOptions options, @Nullable UInteger fileHandle, @Nullable ULong position);

  /**
   * Resolves the mandatory Write Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.5">Model
   *     documentation</a>
   */
  UaMethodNode getWriteMethodNode() throws UaException;

  /** Asynchronous form of {@link #getWriteMethodNode()}. */
  CompletableFuture<UaMethodNode> getWriteMethodNodeAsync();

  /**
   * Calls the Write Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.5">Model
   *     documentation</a>
   */
  void write(@Nullable UInteger fileHandle, @Nullable ByteString data) throws UaException;

  /**
   * Calls the Write Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callWrite(@Nullable UInteger fileHandle, @Nullable ByteString data)
      throws UaException;

  /**
   * Calls the Write Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callWriteWith(
      MethodCallOptions options, @Nullable UInteger fileHandle, @Nullable ByteString data)
      throws UaException;

  /** Asynchronous form of {@link #write}. */
  CompletableFuture<Void> writeAsync(@Nullable UInteger fileHandle, @Nullable ByteString data);

  /** Asynchronous form of {@link #callWrite}. */
  CompletableFuture<MethodCallResult<Void>> callWriteAsync(
      @Nullable UInteger fileHandle, @Nullable ByteString data);

  /** Asynchronous form of {@link #callWriteWith}. */
  CompletableFuture<MethodCallResult<Void>> callWriteWithAsync(
      MethodCallOptions options, @Nullable UInteger fileHandle, @Nullable ByteString data);
}

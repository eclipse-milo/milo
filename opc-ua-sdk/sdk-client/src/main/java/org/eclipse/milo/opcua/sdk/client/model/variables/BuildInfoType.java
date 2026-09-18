package org.eclipse.milo.opcua.sdk.client.model.variables;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.BuildInfo;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the BuildInfoType VariableType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.7">Model
 *     documentation</a>
 */
public interface BuildInfoType extends BaseDataVariableType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 3051L);

  /**
   * Resolves the mandatory ProductUri child, a BaseDataVariableType with DataType String.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getProductUriNode() throws UaException;

  /** Asynchronous form of {@link #getProductUriNode()}. */
  CompletableFuture<? extends VariableNode> getProductUriNodeAsync();

  /**
   * Reads the Value of the ProductUri child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readProductUri() throws UaException;

  /**
   * Writes the Value of the ProductUri child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeProductUri(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readProductUri()}. */
  CompletableFuture<? extends @Nullable String> readProductUriAsync();

  /** Asynchronous form of {@link #writeProductUri}; completes with the operation status. */
  CompletableFuture<StatusCode> writeProductUriAsync(@Nullable String value);

  /**
   * Resolves the mandatory BuildNumber child, a BaseDataVariableType with DataType String.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getBuildNumberNode() throws UaException;

  /** Asynchronous form of {@link #getBuildNumberNode()}. */
  CompletableFuture<? extends VariableNode> getBuildNumberNodeAsync();

  /**
   * Reads the Value of the BuildNumber child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readBuildNumber() throws UaException;

  /**
   * Writes the Value of the BuildNumber child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeBuildNumber(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readBuildNumber()}. */
  CompletableFuture<? extends @Nullable String> readBuildNumberAsync();

  /** Asynchronous form of {@link #writeBuildNumber}; completes with the operation status. */
  CompletableFuture<StatusCode> writeBuildNumberAsync(@Nullable String value);

  /**
   * Resolves the mandatory ProductName child, a BaseDataVariableType with DataType String.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getProductNameNode() throws UaException;

  /** Asynchronous form of {@link #getProductNameNode()}. */
  CompletableFuture<? extends VariableNode> getProductNameNodeAsync();

  /**
   * Reads the Value of the ProductName child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readProductName() throws UaException;

  /**
   * Writes the Value of the ProductName child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeProductName(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readProductName()}. */
  CompletableFuture<? extends @Nullable String> readProductNameAsync();

  /** Asynchronous form of {@link #writeProductName}; completes with the operation status. */
  CompletableFuture<StatusCode> writeProductNameAsync(@Nullable String value);

  /**
   * Resolves the mandatory SoftwareVersion child, a BaseDataVariableType with DataType String.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getSoftwareVersionNode() throws UaException;

  /** Asynchronous form of {@link #getSoftwareVersionNode()}. */
  CompletableFuture<? extends VariableNode> getSoftwareVersionNodeAsync();

  /**
   * Reads the Value of the SoftwareVersion child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readSoftwareVersion() throws UaException;

  /**
   * Writes the Value of the SoftwareVersion child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSoftwareVersion(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readSoftwareVersion()}. */
  CompletableFuture<? extends @Nullable String> readSoftwareVersionAsync();

  /** Asynchronous form of {@link #writeSoftwareVersion}; completes with the operation status. */
  CompletableFuture<StatusCode> writeSoftwareVersionAsync(@Nullable String value);

  /**
   * Resolves the mandatory ManufacturerName child, a BaseDataVariableType with DataType String.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getManufacturerNameNode() throws UaException;

  /** Asynchronous form of {@link #getManufacturerNameNode()}. */
  CompletableFuture<? extends VariableNode> getManufacturerNameNodeAsync();

  /**
   * Reads the Value of the ManufacturerName child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readManufacturerName() throws UaException;

  /**
   * Writes the Value of the ManufacturerName child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeManufacturerName(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readManufacturerName()}. */
  CompletableFuture<? extends @Nullable String> readManufacturerNameAsync();

  /** Asynchronous form of {@link #writeManufacturerName}; completes with the operation status. */
  CompletableFuture<StatusCode> writeManufacturerNameAsync(@Nullable String value);

  /**
   * Resolves the mandatory BuildDate child, a BaseDataVariableType with DataType UtcTime.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  VariableNode getBuildDateNode() throws UaException;

  /** Asynchronous form of {@link #getBuildDateNode()}. */
  CompletableFuture<? extends VariableNode> getBuildDateNodeAsync();

  /**
   * Reads the Value of the BuildDate child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable DateTime readBuildDate() throws UaException;

  /**
   * Writes the Value of the BuildDate child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeBuildDate(@Nullable DateTime value) throws UaException;

  /** Asynchronous form of {@link #readBuildDate()}. */
  CompletableFuture<? extends @Nullable DateTime> readBuildDateAsync();

  /** Asynchronous form of {@link #writeBuildDate}; completes with the operation status. */
  CompletableFuture<StatusCode> writeBuildDateAsync(@Nullable DateTime value);

  /**
   * Reads the Value of this node from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable BuildInfo readTypedValue() throws UaException;

  /**
   * Writes the Value of this node to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeTypedValue(@Nullable BuildInfo value) throws UaException;

  /** Asynchronous form of {@link #readTypedValue()}. */
  CompletableFuture<? extends @Nullable BuildInfo> readTypedValueAsync();

  /** Asynchronous form of {@link #writeTypedValue}; completes with the operation status. */
  CompletableFuture<StatusCode> writeTypedValueAsync(@Nullable BuildInfo value);
}

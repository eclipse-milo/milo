package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the AddressSpaceFileType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.12">Model
 *     documentation</a>
 */
public interface AddressSpaceFileType extends FileType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 11595L);

  /**
   * Resolves the optional ExportNamespace Method node.
   *
   * @throws UaException if lookup or validation fails.
   */
  @Nullable UaMethodNode getExportNamespaceMethodNode() throws UaException;

  /** Asynchronous form of {@link #getExportNamespaceMethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getExportNamespaceMethodNodeAsync();

  /**
   * Calls the ExportNamespace Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   */
  void exportNamespace() throws UaException;

  /**
   * Calls the ExportNamespace Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callExportNamespace() throws UaException;

  /**
   * Calls the ExportNamespace Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callExportNamespaceWith(MethodCallOptions options) throws UaException;

  /** Asynchronous form of {@link #exportNamespace}. */
  CompletableFuture<Void> exportNamespaceAsync();

  /** Asynchronous form of {@link #callExportNamespace}. */
  CompletableFuture<MethodCallResult<Void>> callExportNamespaceAsync();

  /** Asynchronous form of {@link #callExportNamespaceWith}. */
  CompletableFuture<MethodCallResult<Void>> callExportNamespaceWithAsync(MethodCallOptions options);
}

package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the KeyCredentialConfigurationFolderType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.2">Model
 *     documentation</a>
 */
public interface KeyCredentialConfigurationFolderType extends FolderType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 17496L);

  /**
   * Resolves the optional CreateCredential Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.3">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getCreateCredentialMethodNode() throws UaException;

  /** Asynchronous form of {@link #getCreateCredentialMethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getCreateCredentialMethodNodeAsync();

  /**
   * Calls the CreateCredential Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.3">Model
   *     documentation</a>
   */
  @Nullable NodeId createCredential(
      @Nullable String name,
      @Nullable String resourceUri,
      @Nullable String profileUri,
      @Nullable String @Nullable [] endpointUrls)
      throws UaException;

  /**
   * Calls the CreateCredential Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<@Nullable NodeId> callCreateCredential(
      @Nullable String name,
      @Nullable String resourceUri,
      @Nullable String profileUri,
      @Nullable String @Nullable [] endpointUrls)
      throws UaException;

  /**
   * Calls the CreateCredential Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<@Nullable NodeId> callCreateCredentialWith(
      MethodCallOptions options,
      @Nullable String name,
      @Nullable String resourceUri,
      @Nullable String profileUri,
      @Nullable String @Nullable [] endpointUrls)
      throws UaException;

  /** Asynchronous form of {@link #createCredential}. */
  CompletableFuture<@Nullable NodeId> createCredentialAsync(
      @Nullable String name,
      @Nullable String resourceUri,
      @Nullable String profileUri,
      @Nullable String @Nullable [] endpointUrls);

  /** Asynchronous form of {@link #callCreateCredential}. */
  CompletableFuture<MethodCallResult<@Nullable NodeId>> callCreateCredentialAsync(
      @Nullable String name,
      @Nullable String resourceUri,
      @Nullable String profileUri,
      @Nullable String @Nullable [] endpointUrls);

  /** Asynchronous form of {@link #callCreateCredentialWith}. */
  CompletableFuture<MethodCallResult<@Nullable NodeId>> callCreateCredentialWithAsync(
      MethodCallOptions options,
      @Nullable String name,
      @Nullable String resourceUri,
      @Nullable String profileUri,
      @Nullable String @Nullable [] endpointUrls);
}

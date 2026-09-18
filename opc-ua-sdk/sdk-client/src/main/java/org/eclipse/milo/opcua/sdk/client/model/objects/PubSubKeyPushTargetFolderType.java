package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the PubSubKeyPushTargetFolderType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.1">Model
 *     documentation</a>
 */
public interface PubSubKeyPushTargetFolderType extends FolderType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 25346L);

  /**
   * Resolves the mandatory AddPushTarget Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.2">Model
   *     documentation</a>
   */
  UaMethodNode getAddPushTargetMethodNode() throws UaException;

  /** Asynchronous form of {@link #getAddPushTargetMethodNode()}. */
  CompletableFuture<UaMethodNode> getAddPushTargetMethodNodeAsync();

  /**
   * Calls the AddPushTarget Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.2">Model
   *     documentation</a>
   */
  @Nullable NodeId addPushTarget(
      @Nullable String applicationUri,
      @Nullable String endpointUrl,
      @Nullable String securityPolicyUri,
      @Nullable UserTokenPolicy userTokenType,
      @Nullable UShort requestedKeyCount,
      @Nullable Double retryInterval)
      throws UaException;

  /**
   * Calls the AddPushTarget Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<@Nullable NodeId> callAddPushTarget(
      @Nullable String applicationUri,
      @Nullable String endpointUrl,
      @Nullable String securityPolicyUri,
      @Nullable UserTokenPolicy userTokenType,
      @Nullable UShort requestedKeyCount,
      @Nullable Double retryInterval)
      throws UaException;

  /**
   * Calls the AddPushTarget Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<@Nullable NodeId> callAddPushTargetWith(
      MethodCallOptions options,
      @Nullable String applicationUri,
      @Nullable String endpointUrl,
      @Nullable String securityPolicyUri,
      @Nullable UserTokenPolicy userTokenType,
      @Nullable UShort requestedKeyCount,
      @Nullable Double retryInterval)
      throws UaException;

  /** Asynchronous form of {@link #addPushTarget}. */
  CompletableFuture<@Nullable NodeId> addPushTargetAsync(
      @Nullable String applicationUri,
      @Nullable String endpointUrl,
      @Nullable String securityPolicyUri,
      @Nullable UserTokenPolicy userTokenType,
      @Nullable UShort requestedKeyCount,
      @Nullable Double retryInterval);

  /** Asynchronous form of {@link #callAddPushTarget}. */
  CompletableFuture<MethodCallResult<@Nullable NodeId>> callAddPushTargetAsync(
      @Nullable String applicationUri,
      @Nullable String endpointUrl,
      @Nullable String securityPolicyUri,
      @Nullable UserTokenPolicy userTokenType,
      @Nullable UShort requestedKeyCount,
      @Nullable Double retryInterval);

  /** Asynchronous form of {@link #callAddPushTargetWith}. */
  CompletableFuture<MethodCallResult<@Nullable NodeId>> callAddPushTargetWithAsync(
      MethodCallOptions options,
      @Nullable String applicationUri,
      @Nullable String endpointUrl,
      @Nullable String securityPolicyUri,
      @Nullable UserTokenPolicy userTokenType,
      @Nullable UShort requestedKeyCount,
      @Nullable Double retryInterval);

  /**
   * Resolves the optional AddPushTargetFolder Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.4">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getAddPushTargetFolderMethodNode() throws UaException;

  /** Asynchronous form of {@link #getAddPushTargetFolderMethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getAddPushTargetFolderMethodNodeAsync();

  /**
   * Calls the AddPushTargetFolder Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.4">Model
   *     documentation</a>
   */
  @Nullable NodeId addPushTargetFolder(@Nullable String name) throws UaException;

  /**
   * Calls the AddPushTargetFolder Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<@Nullable NodeId> callAddPushTargetFolder(@Nullable String name)
      throws UaException;

  /**
   * Calls the AddPushTargetFolder Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<@Nullable NodeId> callAddPushTargetFolderWith(
      MethodCallOptions options, @Nullable String name) throws UaException;

  /** Asynchronous form of {@link #addPushTargetFolder}. */
  CompletableFuture<@Nullable NodeId> addPushTargetFolderAsync(@Nullable String name);

  /** Asynchronous form of {@link #callAddPushTargetFolder}. */
  CompletableFuture<MethodCallResult<@Nullable NodeId>> callAddPushTargetFolderAsync(
      @Nullable String name);

  /** Asynchronous form of {@link #callAddPushTargetFolderWith}. */
  CompletableFuture<MethodCallResult<@Nullable NodeId>> callAddPushTargetFolderWithAsync(
      MethodCallOptions options, @Nullable String name);

  /**
   * Resolves the mandatory RemovePushTarget Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.3">Model
   *     documentation</a>
   */
  UaMethodNode getRemovePushTargetMethodNode() throws UaException;

  /** Asynchronous form of {@link #getRemovePushTargetMethodNode()}. */
  CompletableFuture<UaMethodNode> getRemovePushTargetMethodNodeAsync();

  /**
   * Calls the RemovePushTarget Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.3">Model
   *     documentation</a>
   */
  void removePushTarget(@Nullable NodeId pushTargetId) throws UaException;

  /**
   * Calls the RemovePushTarget Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callRemovePushTarget(@Nullable NodeId pushTargetId) throws UaException;

  /**
   * Calls the RemovePushTarget Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callRemovePushTargetWith(
      MethodCallOptions options, @Nullable NodeId pushTargetId) throws UaException;

  /** Asynchronous form of {@link #removePushTarget}. */
  CompletableFuture<Void> removePushTargetAsync(@Nullable NodeId pushTargetId);

  /** Asynchronous form of {@link #callRemovePushTarget}. */
  CompletableFuture<MethodCallResult<Void>> callRemovePushTargetAsync(
      @Nullable NodeId pushTargetId);

  /** Asynchronous form of {@link #callRemovePushTargetWith}. */
  CompletableFuture<MethodCallResult<Void>> callRemovePushTargetWithAsync(
      MethodCallOptions options, @Nullable NodeId pushTargetId);

  /**
   * Resolves the optional RemovePushTargetFolder Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.5">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getRemovePushTargetFolderMethodNode() throws UaException;

  /** Asynchronous form of {@link #getRemovePushTargetFolderMethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getRemovePushTargetFolderMethodNodeAsync();

  /**
   * Calls the RemovePushTargetFolder Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.5">Model
   *     documentation</a>
   */
  void removePushTargetFolder(@Nullable NodeId pushTargetFolderNodeId) throws UaException;

  /**
   * Calls the RemovePushTargetFolder Method and returns the complete result, including a Bad
   * status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callRemovePushTargetFolder(@Nullable NodeId pushTargetFolderNodeId)
      throws UaException;

  /**
   * Calls the RemovePushTargetFolder Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callRemovePushTargetFolderWith(
      MethodCallOptions options, @Nullable NodeId pushTargetFolderNodeId) throws UaException;

  /** Asynchronous form of {@link #removePushTargetFolder}. */
  CompletableFuture<Void> removePushTargetFolderAsync(@Nullable NodeId pushTargetFolderNodeId);

  /** Asynchronous form of {@link #callRemovePushTargetFolder}. */
  CompletableFuture<MethodCallResult<Void>> callRemovePushTargetFolderAsync(
      @Nullable NodeId pushTargetFolderNodeId);

  /** Asynchronous form of {@link #callRemovePushTargetFolderWith}. */
  CompletableFuture<MethodCallResult<Void>> callRemovePushTargetFolderWithAsync(
      MethodCallOptions options, @Nullable NodeId pushTargetFolderNodeId);
}

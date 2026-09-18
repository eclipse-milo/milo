package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.model.methods.PubSubKeyServiceTypeGetSecurityKeys;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the PubSubKeyServiceType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.3.1">Model
 *     documentation</a>
 */
public interface PubSubKeyServiceType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 15906L);

  /**
   * Resolves the optional KeyPushTargets child, a PubSubKeyPushTargetFolderType.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.1">PubSubKeyPushTargetFolderType
   *     documentation</a>
   */
  @Nullable PubSubKeyPushTargetFolderType getKeyPushTargetsNode() throws UaException;

  /** Asynchronous form of {@link #getKeyPushTargetsNode()}. */
  CompletableFuture<? extends @Nullable PubSubKeyPushTargetFolderType> getKeyPushTargetsNodeAsync();

  /**
   * Resolves the optional SecurityGroups child, a SecurityGroupFolderType.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.1">SecurityGroupFolderType
   *     documentation</a>
   */
  @Nullable SecurityGroupFolderType getSecurityGroupsNode() throws UaException;

  /** Asynchronous form of {@link #getSecurityGroupsNode()}. */
  CompletableFuture<? extends @Nullable SecurityGroupFolderType> getSecurityGroupsNodeAsync();

  /**
   * Resolves the optional GetSecurityGroup Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.3.3">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getGetSecurityGroupMethodNode() throws UaException;

  /** Asynchronous form of {@link #getGetSecurityGroupMethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getGetSecurityGroupMethodNodeAsync();

  /**
   * Calls the GetSecurityGroup Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.3.3">Model
   *     documentation</a>
   */
  @Nullable NodeId getSecurityGroup(@Nullable String securityGroupId) throws UaException;

  /**
   * Calls the GetSecurityGroup Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<@Nullable NodeId> callGetSecurityGroup(@Nullable String securityGroupId)
      throws UaException;

  /**
   * Calls the GetSecurityGroup Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<@Nullable NodeId> callGetSecurityGroupWith(
      MethodCallOptions options, @Nullable String securityGroupId) throws UaException;

  /** Asynchronous form of {@link #getSecurityGroup}. */
  CompletableFuture<@Nullable NodeId> getSecurityGroupAsync(@Nullable String securityGroupId);

  /** Asynchronous form of {@link #callGetSecurityGroup}. */
  CompletableFuture<MethodCallResult<@Nullable NodeId>> callGetSecurityGroupAsync(
      @Nullable String securityGroupId);

  /** Asynchronous form of {@link #callGetSecurityGroupWith}. */
  CompletableFuture<MethodCallResult<@Nullable NodeId>> callGetSecurityGroupWithAsync(
      MethodCallOptions options, @Nullable String securityGroupId);

  /**
   * Resolves the optional GetSecurityKeys Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.3.2">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getGetSecurityKeysMethodNode() throws UaException;

  /** Asynchronous form of {@link #getGetSecurityKeysMethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getGetSecurityKeysMethodNodeAsync();

  /**
   * Calls the GetSecurityKeys Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.3.2">Model
   *     documentation</a>
   */
  PubSubKeyServiceTypeGetSecurityKeys.Outputs getSecurityKeys(
      @Nullable String securityGroupId,
      @Nullable UInteger startingTokenId,
      @Nullable UInteger requestedKeyCount)
      throws UaException;

  /**
   * Calls the GetSecurityKeys Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<PubSubKeyServiceTypeGetSecurityKeys.Outputs> callGetSecurityKeys(
      @Nullable String securityGroupId,
      @Nullable UInteger startingTokenId,
      @Nullable UInteger requestedKeyCount)
      throws UaException;

  /**
   * Calls the GetSecurityKeys Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<PubSubKeyServiceTypeGetSecurityKeys.Outputs> callGetSecurityKeysWith(
      MethodCallOptions options,
      @Nullable String securityGroupId,
      @Nullable UInteger startingTokenId,
      @Nullable UInteger requestedKeyCount)
      throws UaException;

  /** Asynchronous form of {@link #getSecurityKeys}. */
  CompletableFuture<PubSubKeyServiceTypeGetSecurityKeys.Outputs> getSecurityKeysAsync(
      @Nullable String securityGroupId,
      @Nullable UInteger startingTokenId,
      @Nullable UInteger requestedKeyCount);

  /** Asynchronous form of {@link #callGetSecurityKeys}. */
  CompletableFuture<MethodCallResult<PubSubKeyServiceTypeGetSecurityKeys.Outputs>>
      callGetSecurityKeysAsync(
          @Nullable String securityGroupId,
          @Nullable UInteger startingTokenId,
          @Nullable UInteger requestedKeyCount);

  /** Asynchronous form of {@link #callGetSecurityKeysWith}. */
  CompletableFuture<MethodCallResult<PubSubKeyServiceTypeGetSecurityKeys.Outputs>>
      callGetSecurityKeysWithAsync(
          MethodCallOptions options,
          @Nullable String securityGroupId,
          @Nullable UInteger startingTokenId,
          @Nullable UInteger requestedKeyCount);
}

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
 * Client API for the RoleSetType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part18/4.2.1">Model
 *     documentation</a>
 */
public interface RoleSetType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 15607L);

  /**
   * Resolves the mandatory AddRole Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part18/4.2.2">Model
   *     documentation</a>
   */
  UaMethodNode getAddRoleMethodNode() throws UaException;

  /** Asynchronous form of {@link #getAddRoleMethodNode()}. */
  CompletableFuture<UaMethodNode> getAddRoleMethodNodeAsync();

  /**
   * Calls the AddRole Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part18/4.2.2">Model
   *     documentation</a>
   */
  @Nullable NodeId addRole(@Nullable String roleName, @Nullable String namespaceUri)
      throws UaException;

  /**
   * Calls the AddRole Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<@Nullable NodeId> callAddRole(
      @Nullable String roleName, @Nullable String namespaceUri) throws UaException;

  /**
   * Calls the AddRole Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<@Nullable NodeId> callAddRoleWith(
      MethodCallOptions options, @Nullable String roleName, @Nullable String namespaceUri)
      throws UaException;

  /** Asynchronous form of {@link #addRole}. */
  CompletableFuture<@Nullable NodeId> addRoleAsync(
      @Nullable String roleName, @Nullable String namespaceUri);

  /** Asynchronous form of {@link #callAddRole}. */
  CompletableFuture<MethodCallResult<@Nullable NodeId>> callAddRoleAsync(
      @Nullable String roleName, @Nullable String namespaceUri);

  /** Asynchronous form of {@link #callAddRoleWith}. */
  CompletableFuture<MethodCallResult<@Nullable NodeId>> callAddRoleWithAsync(
      MethodCallOptions options, @Nullable String roleName, @Nullable String namespaceUri);

  /**
   * Resolves the mandatory RemoveRole Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part18/4.2.3">Model
   *     documentation</a>
   */
  UaMethodNode getRemoveRoleMethodNode() throws UaException;

  /** Asynchronous form of {@link #getRemoveRoleMethodNode()}. */
  CompletableFuture<UaMethodNode> getRemoveRoleMethodNodeAsync();

  /**
   * Calls the RemoveRole Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part18/4.2.3">Model
   *     documentation</a>
   */
  void removeRole(@Nullable NodeId roleNodeId) throws UaException;

  /**
   * Calls the RemoveRole Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callRemoveRole(@Nullable NodeId roleNodeId) throws UaException;

  /**
   * Calls the RemoveRole Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callRemoveRoleWith(MethodCallOptions options, @Nullable NodeId roleNodeId)
      throws UaException;

  /** Asynchronous form of {@link #removeRole}. */
  CompletableFuture<Void> removeRoleAsync(@Nullable NodeId roleNodeId);

  /** Asynchronous form of {@link #callRemoveRole}. */
  CompletableFuture<MethodCallResult<Void>> callRemoveRoleAsync(@Nullable NodeId roleNodeId);

  /** Asynchronous form of {@link #callRemoveRoleWith}. */
  CompletableFuture<MethodCallResult<Void>> callRemoveRoleWithAsync(
      MethodCallOptions options, @Nullable NodeId roleNodeId);
}

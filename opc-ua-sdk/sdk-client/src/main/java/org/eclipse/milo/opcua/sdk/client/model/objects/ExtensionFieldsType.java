package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the ExtensionFieldsType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.2.2">Model
 *     documentation</a>
 */
public interface ExtensionFieldsType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 15489L);

  /**
   * Resolves the mandatory AddExtensionField Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.2.3">Model
   *     documentation</a>
   */
  UaMethodNode getAddExtensionFieldMethodNode() throws UaException;

  /** Asynchronous form of {@link #getAddExtensionFieldMethodNode()}. */
  CompletableFuture<UaMethodNode> getAddExtensionFieldMethodNodeAsync();

  /**
   * Calls the AddExtensionField Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.2.3">Model
   *     documentation</a>
   */
  @Nullable NodeId addExtensionField(
      @Nullable QualifiedName fieldName, @Nullable Variant fieldValue) throws UaException;

  /**
   * Calls the AddExtensionField Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<@Nullable NodeId> callAddExtensionField(
      @Nullable QualifiedName fieldName, @Nullable Variant fieldValue) throws UaException;

  /**
   * Calls the AddExtensionField Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<@Nullable NodeId> callAddExtensionFieldWith(
      MethodCallOptions options, @Nullable QualifiedName fieldName, @Nullable Variant fieldValue)
      throws UaException;

  /** Asynchronous form of {@link #addExtensionField}. */
  CompletableFuture<@Nullable NodeId> addExtensionFieldAsync(
      @Nullable QualifiedName fieldName, @Nullable Variant fieldValue);

  /** Asynchronous form of {@link #callAddExtensionField}. */
  CompletableFuture<MethodCallResult<@Nullable NodeId>> callAddExtensionFieldAsync(
      @Nullable QualifiedName fieldName, @Nullable Variant fieldValue);

  /** Asynchronous form of {@link #callAddExtensionFieldWith}. */
  CompletableFuture<MethodCallResult<@Nullable NodeId>> callAddExtensionFieldWithAsync(
      MethodCallOptions options, @Nullable QualifiedName fieldName, @Nullable Variant fieldValue);

  /**
   * Resolves the mandatory RemoveExtensionField Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.2.4">Model
   *     documentation</a>
   */
  UaMethodNode getRemoveExtensionFieldMethodNode() throws UaException;

  /** Asynchronous form of {@link #getRemoveExtensionFieldMethodNode()}. */
  CompletableFuture<UaMethodNode> getRemoveExtensionFieldMethodNodeAsync();

  /**
   * Calls the RemoveExtensionField Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.2.4">Model
   *     documentation</a>
   */
  void removeExtensionField(@Nullable NodeId fieldId) throws UaException;

  /**
   * Calls the RemoveExtensionField Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callRemoveExtensionField(@Nullable NodeId fieldId) throws UaException;

  /**
   * Calls the RemoveExtensionField Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callRemoveExtensionFieldWith(
      MethodCallOptions options, @Nullable NodeId fieldId) throws UaException;

  /** Asynchronous form of {@link #removeExtensionField}. */
  CompletableFuture<Void> removeExtensionFieldAsync(@Nullable NodeId fieldId);

  /** Asynchronous form of {@link #callRemoveExtensionField}. */
  CompletableFuture<MethodCallResult<Void>> callRemoveExtensionFieldAsync(@Nullable NodeId fieldId);

  /** Asynchronous form of {@link #callRemoveExtensionFieldWith}. */
  CompletableFuture<MethodCallResult<Void>> callRemoveExtensionFieldWithAsync(
      MethodCallOptions options, @Nullable NodeId fieldId);
}

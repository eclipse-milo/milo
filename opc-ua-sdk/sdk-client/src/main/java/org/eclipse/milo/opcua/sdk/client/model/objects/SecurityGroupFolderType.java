package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.sdk.core.model.methods.SecurityGroupFolderTypeAddSecurityGroup;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the SecurityGroupFolderType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.1">Model
 *     documentation</a>
 */
public interface SecurityGroupFolderType extends FolderType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 15452L);

  QualifiedProperty<String[]> SupportedSecurityPolicyUris_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "SupportedSecurityPolicyUris",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
          1,
          String[].class);

  /**
   * Resolves the optional SupportedSecurityPolicyUris child, a PropertyType with DataType String.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getSupportedSecurityPolicyUrisNode() throws UaException;

  /** Asynchronous form of {@link #getSupportedSecurityPolicyUrisNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getSupportedSecurityPolicyUrisNodeAsync();

  /**
   * Reads the Value of the SupportedSecurityPolicyUris child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String @Nullable [] readSupportedSecurityPolicyUris() throws UaException;

  /**
   * Writes the Value of the SupportedSecurityPolicyUris child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSupportedSecurityPolicyUris(@Nullable String @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readSupportedSecurityPolicyUris()}. */
  CompletableFuture<? extends @Nullable String @Nullable []> readSupportedSecurityPolicyUrisAsync();

  /**
   * Asynchronous form of {@link #writeSupportedSecurityPolicyUris}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeSupportedSecurityPolicyUrisAsync(
      @Nullable String @Nullable [] value);

  /**
   * Resolves the mandatory AddSecurityGroup Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.2">Model
   *     documentation</a>
   */
  UaMethodNode getAddSecurityGroupMethodNode() throws UaException;

  /** Asynchronous form of {@link #getAddSecurityGroupMethodNode()}. */
  CompletableFuture<UaMethodNode> getAddSecurityGroupMethodNodeAsync();

  /**
   * Calls the AddSecurityGroup Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.2">Model
   *     documentation</a>
   */
  SecurityGroupFolderTypeAddSecurityGroup.Outputs addSecurityGroup(
      @Nullable String securityGroupName,
      @Nullable Double keyLifetime,
      @Nullable String securityPolicyUri,
      @Nullable UInteger maxFutureKeyCount,
      @Nullable UInteger maxPastKeyCount)
      throws UaException;

  /**
   * Calls the AddSecurityGroup Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<SecurityGroupFolderTypeAddSecurityGroup.Outputs> callAddSecurityGroup(
      @Nullable String securityGroupName,
      @Nullable Double keyLifetime,
      @Nullable String securityPolicyUri,
      @Nullable UInteger maxFutureKeyCount,
      @Nullable UInteger maxPastKeyCount)
      throws UaException;

  /**
   * Calls the AddSecurityGroup Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<SecurityGroupFolderTypeAddSecurityGroup.Outputs> callAddSecurityGroupWith(
      MethodCallOptions options,
      @Nullable String securityGroupName,
      @Nullable Double keyLifetime,
      @Nullable String securityPolicyUri,
      @Nullable UInteger maxFutureKeyCount,
      @Nullable UInteger maxPastKeyCount)
      throws UaException;

  /** Asynchronous form of {@link #addSecurityGroup}. */
  CompletableFuture<SecurityGroupFolderTypeAddSecurityGroup.Outputs> addSecurityGroupAsync(
      @Nullable String securityGroupName,
      @Nullable Double keyLifetime,
      @Nullable String securityPolicyUri,
      @Nullable UInteger maxFutureKeyCount,
      @Nullable UInteger maxPastKeyCount);

  /** Asynchronous form of {@link #callAddSecurityGroup}. */
  CompletableFuture<MethodCallResult<SecurityGroupFolderTypeAddSecurityGroup.Outputs>>
      callAddSecurityGroupAsync(
          @Nullable String securityGroupName,
          @Nullable Double keyLifetime,
          @Nullable String securityPolicyUri,
          @Nullable UInteger maxFutureKeyCount,
          @Nullable UInteger maxPastKeyCount);

  /** Asynchronous form of {@link #callAddSecurityGroupWith}. */
  CompletableFuture<MethodCallResult<SecurityGroupFolderTypeAddSecurityGroup.Outputs>>
      callAddSecurityGroupWithAsync(
          MethodCallOptions options,
          @Nullable String securityGroupName,
          @Nullable Double keyLifetime,
          @Nullable String securityPolicyUri,
          @Nullable UInteger maxFutureKeyCount,
          @Nullable UInteger maxPastKeyCount);

  /**
   * Resolves the optional AddSecurityGroupFolder Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.4">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getAddSecurityGroupFolderMethodNode() throws UaException;

  /** Asynchronous form of {@link #getAddSecurityGroupFolderMethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getAddSecurityGroupFolderMethodNodeAsync();

  /**
   * Calls the AddSecurityGroupFolder Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.4">Model
   *     documentation</a>
   */
  @Nullable NodeId addSecurityGroupFolder(@Nullable String name) throws UaException;

  /**
   * Calls the AddSecurityGroupFolder Method and returns the complete result, including a Bad
   * status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<@Nullable NodeId> callAddSecurityGroupFolder(@Nullable String name)
      throws UaException;

  /**
   * Calls the AddSecurityGroupFolder Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<@Nullable NodeId> callAddSecurityGroupFolderWith(
      MethodCallOptions options, @Nullable String name) throws UaException;

  /** Asynchronous form of {@link #addSecurityGroupFolder}. */
  CompletableFuture<@Nullable NodeId> addSecurityGroupFolderAsync(@Nullable String name);

  /** Asynchronous form of {@link #callAddSecurityGroupFolder}. */
  CompletableFuture<MethodCallResult<@Nullable NodeId>> callAddSecurityGroupFolderAsync(
      @Nullable String name);

  /** Asynchronous form of {@link #callAddSecurityGroupFolderWith}. */
  CompletableFuture<MethodCallResult<@Nullable NodeId>> callAddSecurityGroupFolderWithAsync(
      MethodCallOptions options, @Nullable String name);

  /**
   * Resolves the mandatory RemoveSecurityGroup Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.3">Model
   *     documentation</a>
   */
  UaMethodNode getRemoveSecurityGroupMethodNode() throws UaException;

  /** Asynchronous form of {@link #getRemoveSecurityGroupMethodNode()}. */
  CompletableFuture<UaMethodNode> getRemoveSecurityGroupMethodNodeAsync();

  /**
   * Calls the RemoveSecurityGroup Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.3">Model
   *     documentation</a>
   */
  void removeSecurityGroup(@Nullable NodeId securityGroupNodeId) throws UaException;

  /**
   * Calls the RemoveSecurityGroup Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callRemoveSecurityGroup(@Nullable NodeId securityGroupNodeId)
      throws UaException;

  /**
   * Calls the RemoveSecurityGroup Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callRemoveSecurityGroupWith(
      MethodCallOptions options, @Nullable NodeId securityGroupNodeId) throws UaException;

  /** Asynchronous form of {@link #removeSecurityGroup}. */
  CompletableFuture<Void> removeSecurityGroupAsync(@Nullable NodeId securityGroupNodeId);

  /** Asynchronous form of {@link #callRemoveSecurityGroup}. */
  CompletableFuture<MethodCallResult<Void>> callRemoveSecurityGroupAsync(
      @Nullable NodeId securityGroupNodeId);

  /** Asynchronous form of {@link #callRemoveSecurityGroupWith}. */
  CompletableFuture<MethodCallResult<Void>> callRemoveSecurityGroupWithAsync(
      MethodCallOptions options, @Nullable NodeId securityGroupNodeId);

  /**
   * Resolves the optional RemoveSecurityGroupFolder Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.5">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getRemoveSecurityGroupFolderMethodNode() throws UaException;

  /** Asynchronous form of {@link #getRemoveSecurityGroupFolderMethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getRemoveSecurityGroupFolderMethodNodeAsync();

  /**
   * Calls the RemoveSecurityGroupFolder Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.5">Model
   *     documentation</a>
   */
  void removeSecurityGroupFolder(@Nullable NodeId securityGroupFolderNodeId) throws UaException;

  /**
   * Calls the RemoveSecurityGroupFolder Method and returns the complete result, including a Bad
   * status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callRemoveSecurityGroupFolder(@Nullable NodeId securityGroupFolderNodeId)
      throws UaException;

  /**
   * Calls the RemoveSecurityGroupFolder Method with explicit options and returns the complete
   * result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callRemoveSecurityGroupFolderWith(
      MethodCallOptions options, @Nullable NodeId securityGroupFolderNodeId) throws UaException;

  /** Asynchronous form of {@link #removeSecurityGroupFolder}. */
  CompletableFuture<Void> removeSecurityGroupFolderAsync(
      @Nullable NodeId securityGroupFolderNodeId);

  /** Asynchronous form of {@link #callRemoveSecurityGroupFolder}. */
  CompletableFuture<MethodCallResult<Void>> callRemoveSecurityGroupFolderAsync(
      @Nullable NodeId securityGroupFolderNodeId);

  /** Asynchronous form of {@link #callRemoveSecurityGroupFolderWith}. */
  CompletableFuture<MethodCallResult<Void>> callRemoveSecurityGroupFolderWithAsync(
      MethodCallOptions options, @Nullable NodeId securityGroupFolderNodeId);
}

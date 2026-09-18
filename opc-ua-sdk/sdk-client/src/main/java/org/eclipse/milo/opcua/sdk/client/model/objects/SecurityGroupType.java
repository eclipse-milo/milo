package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the SecurityGroupType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.4.1">Model
 *     documentation</a>
 */
public interface SecurityGroupType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 15471L);

  QualifiedProperty<Double> KeyLifetime_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "KeyLifetime",
          ExpandedNodeId.of(Namespaces.OPC_UA, 290L),
          -1,
          Double.class);

  QualifiedProperty<UInteger> MaxPastKeyCount_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "MaxPastKeyCount",
          ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
          -1,
          UInteger.class);

  QualifiedProperty<String> SecurityGroupId_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "SecurityGroupId",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
          -1,
          String.class);

  QualifiedProperty<UInteger> MaxFutureKeyCount_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "MaxFutureKeyCount",
          ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
          -1,
          UInteger.class);

  QualifiedProperty<String> SecurityPolicyUri_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "SecurityPolicyUri",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
          -1,
          String.class);

  /**
   * Resolves the mandatory KeyLifetime child, a PropertyType with DataType Duration.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getKeyLifetimeNode() throws UaException;

  /** Asynchronous form of {@link #getKeyLifetimeNode()}. */
  CompletableFuture<? extends PropertyType> getKeyLifetimeNodeAsync();

  /**
   * Reads the Value of the KeyLifetime child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Double readKeyLifetime() throws UaException;

  /**
   * Writes the Value of the KeyLifetime child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeKeyLifetime(@Nullable Double value) throws UaException;

  /** Asynchronous form of {@link #readKeyLifetime()}. */
  CompletableFuture<? extends @Nullable Double> readKeyLifetimeAsync();

  /** Asynchronous form of {@link #writeKeyLifetime}; completes with the operation status. */
  CompletableFuture<StatusCode> writeKeyLifetimeAsync(@Nullable Double value);

  /**
   * Resolves the mandatory MaxPastKeyCount child, a PropertyType with DataType UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getMaxPastKeyCountNode() throws UaException;

  /** Asynchronous form of {@link #getMaxPastKeyCountNode()}. */
  CompletableFuture<? extends PropertyType> getMaxPastKeyCountNodeAsync();

  /**
   * Reads the Value of the MaxPastKeyCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readMaxPastKeyCount() throws UaException;

  /**
   * Writes the Value of the MaxPastKeyCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMaxPastKeyCount(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readMaxPastKeyCount()}. */
  CompletableFuture<? extends @Nullable UInteger> readMaxPastKeyCountAsync();

  /** Asynchronous form of {@link #writeMaxPastKeyCount}; completes with the operation status. */
  CompletableFuture<StatusCode> writeMaxPastKeyCountAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory SecurityGroupId child, a PropertyType with DataType String.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getSecurityGroupIdNode() throws UaException;

  /** Asynchronous form of {@link #getSecurityGroupIdNode()}. */
  CompletableFuture<? extends PropertyType> getSecurityGroupIdNodeAsync();

  /**
   * Reads the Value of the SecurityGroupId child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readSecurityGroupId() throws UaException;

  /**
   * Writes the Value of the SecurityGroupId child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSecurityGroupId(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readSecurityGroupId()}. */
  CompletableFuture<? extends @Nullable String> readSecurityGroupIdAsync();

  /** Asynchronous form of {@link #writeSecurityGroupId}; completes with the operation status. */
  CompletableFuture<StatusCode> writeSecurityGroupIdAsync(@Nullable String value);

  /**
   * Resolves the mandatory MaxFutureKeyCount child, a PropertyType with DataType UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getMaxFutureKeyCountNode() throws UaException;

  /** Asynchronous form of {@link #getMaxFutureKeyCountNode()}. */
  CompletableFuture<? extends PropertyType> getMaxFutureKeyCountNodeAsync();

  /**
   * Reads the Value of the MaxFutureKeyCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readMaxFutureKeyCount() throws UaException;

  /**
   * Writes the Value of the MaxFutureKeyCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMaxFutureKeyCount(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readMaxFutureKeyCount()}. */
  CompletableFuture<? extends @Nullable UInteger> readMaxFutureKeyCountAsync();

  /** Asynchronous form of {@link #writeMaxFutureKeyCount}; completes with the operation status. */
  CompletableFuture<StatusCode> writeMaxFutureKeyCountAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory SecurityPolicyUri child, a PropertyType with DataType String.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getSecurityPolicyUriNode() throws UaException;

  /** Asynchronous form of {@link #getSecurityPolicyUriNode()}. */
  CompletableFuture<? extends PropertyType> getSecurityPolicyUriNodeAsync();

  /**
   * Reads the Value of the SecurityPolicyUri child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readSecurityPolicyUri() throws UaException;

  /**
   * Writes the Value of the SecurityPolicyUri child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSecurityPolicyUri(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readSecurityPolicyUri()}. */
  CompletableFuture<? extends @Nullable String> readSecurityPolicyUriAsync();

  /** Asynchronous form of {@link #writeSecurityPolicyUri}; completes with the operation status. */
  CompletableFuture<StatusCode> writeSecurityPolicyUriAsync(@Nullable String value);

  /**
   * Resolves the optional ForceKeyRotation Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.4.3">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getForceKeyRotationMethodNode() throws UaException;

  /** Asynchronous form of {@link #getForceKeyRotationMethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getForceKeyRotationMethodNodeAsync();

  /**
   * Calls the ForceKeyRotation Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.4.3">Model
   *     documentation</a>
   */
  void forceKeyRotation() throws UaException;

  /**
   * Calls the ForceKeyRotation Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callForceKeyRotation() throws UaException;

  /**
   * Calls the ForceKeyRotation Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callForceKeyRotationWith(MethodCallOptions options) throws UaException;

  /** Asynchronous form of {@link #forceKeyRotation}. */
  CompletableFuture<Void> forceKeyRotationAsync();

  /** Asynchronous form of {@link #callForceKeyRotation}. */
  CompletableFuture<MethodCallResult<Void>> callForceKeyRotationAsync();

  /** Asynchronous form of {@link #callForceKeyRotationWith}. */
  CompletableFuture<MethodCallResult<Void>> callForceKeyRotationWithAsync(
      MethodCallOptions options);

  /**
   * Resolves the optional InvalidateKeys Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.4.2">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getInvalidateKeysMethodNode() throws UaException;

  /** Asynchronous form of {@link #getInvalidateKeysMethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getInvalidateKeysMethodNodeAsync();

  /**
   * Calls the InvalidateKeys Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.4.2">Model
   *     documentation</a>
   */
  void invalidateKeys() throws UaException;

  /**
   * Calls the InvalidateKeys Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callInvalidateKeys() throws UaException;

  /**
   * Calls the InvalidateKeys Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callInvalidateKeysWith(MethodCallOptions options) throws UaException;

  /** Asynchronous form of {@link #invalidateKeys}. */
  CompletableFuture<Void> invalidateKeysAsync();

  /** Asynchronous form of {@link #callInvalidateKeys}. */
  CompletableFuture<MethodCallResult<Void>> callInvalidateKeysAsync();

  /** Asynchronous form of {@link #callInvalidateKeysWith}. */
  CompletableFuture<MethodCallResult<Void>> callInvalidateKeysWithAsync(MethodCallOptions options);
}

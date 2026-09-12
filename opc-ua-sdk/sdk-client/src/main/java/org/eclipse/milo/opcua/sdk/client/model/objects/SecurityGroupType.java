/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.model.objects;

import com.digitalpetri.opcua.uanodeset.runtime.methods.MethodCallOptions;
import com.digitalpetri.opcua.uanodeset.runtime.methods.MethodCallResult;
import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.4.1">https://reference.opcfoundation.org/v105/Core/docs/Part14/8.4.1</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface SecurityGroupType extends BaseObjectType {
  QualifiedProperty<String> SECURITY_GROUP_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SecurityGroupId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<Double> KEY_LIFETIME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "KeyLifetime",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  QualifiedProperty<String> SECURITY_POLICY_URI =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SecurityPolicyUri",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<UInteger> MAX_FUTURE_KEY_COUNT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxFutureKeyCount",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MAX_PAST_KEY_COUNT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxPastKeyCount",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  /** Gets the existing node's local value. */
  @Nullable String getSecurityGroupId() throws UaException;

  /** Sets the existing node's local value. */
  void setSecurityGroupId(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readSecurityGroupId() throws UaException;

  /** Writes the value remotely. */
  void writeSecurityGroupId(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readSecurityGroupIdAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSecurityGroupIdAsync(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getSecurityGroupIdNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getSecurityGroupIdNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Double getKeyLifetime() throws UaException;

  /** Sets the existing node's local value. */
  void setKeyLifetime(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Double readKeyLifetime() throws UaException;

  /** Writes the value remotely. */
  void writeKeyLifetime(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Double> readKeyLifetimeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeKeyLifetimeAsync(@Nullable Double value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getKeyLifetimeNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getKeyLifetimeNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String getSecurityPolicyUri() throws UaException;

  /** Sets the existing node's local value. */
  void setSecurityPolicyUri(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readSecurityPolicyUri() throws UaException;

  /** Writes the value remotely. */
  void writeSecurityPolicyUri(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readSecurityPolicyUriAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSecurityPolicyUriAsync(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getSecurityPolicyUriNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getSecurityPolicyUriNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxFutureKeyCount() throws UaException;

  /** Sets the existing node's local value. */
  void setMaxFutureKeyCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readMaxFutureKeyCount() throws UaException;

  /** Writes the value remotely. */
  void writeMaxFutureKeyCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readMaxFutureKeyCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMaxFutureKeyCountAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getMaxFutureKeyCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getMaxFutureKeyCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxPastKeyCount() throws UaException;

  /** Sets the existing node's local value. */
  void setMaxPastKeyCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readMaxPastKeyCount() throws UaException;

  /** Writes the value remotely. */
  void writeMaxPastKeyCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readMaxPastKeyCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMaxPastKeyCountAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getMaxPastKeyCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getMaxPastKeyCountNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.4.2
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @NullMarked
  @Nullable UaMethodNode getInvalidateKeysMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.4.2
   *
   * <p>Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode> getInvalidateKeysMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.4.2
   *
   * <p>Invokes <code>InvalidateKeys</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  void callInvalidateKeys() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.4.2
   *
   * <p>Invokes <code>InvalidateKeys</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return a future whose successful payload is null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Void> callInvalidateKeysAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.4.2
   *
   * <p>Invokes <code>InvalidateKeys</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callInvalidateKeysDetailed() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.4.2
   *
   * <p>Invokes <code>InvalidateKeys</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callInvalidateKeysDetailed(MethodCallOptions options)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.4.2
   *
   * <p>Invokes <code>InvalidateKeys</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callInvalidateKeysDetailedAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.4.2
   *
   * <p>Invokes <code>InvalidateKeys</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callInvalidateKeysDetailedAsync(MethodCallOptions options);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.4.3
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @NullMarked
  @Nullable UaMethodNode getForceKeyRotationMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.4.3
   *
   * <p>Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode> getForceKeyRotationMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.4.3
   *
   * <p>Invokes <code>ForceKeyRotation</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  void callForceKeyRotation() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.4.3
   *
   * <p>Invokes <code>ForceKeyRotation</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return a future whose successful payload is null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Void> callForceKeyRotationAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.4.3
   *
   * <p>Invokes <code>ForceKeyRotation</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callForceKeyRotationDetailed() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.4.3
   *
   * <p>Invokes <code>ForceKeyRotation</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callForceKeyRotationDetailed(MethodCallOptions options)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.4.3
   *
   * <p>Invokes <code>ForceKeyRotation</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callForceKeyRotationDetailedAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.4.3
   *
   * <p>Invokes <code>ForceKeyRotation</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callForceKeyRotationDetailedAsync(MethodCallOptions options);
}

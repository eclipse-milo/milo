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
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.6.1">https://reference.opcfoundation.org/v105/Core/docs/Part14/8.6.1</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface PubSubKeyPushTargetType extends BaseObjectType {
  QualifiedProperty<String> APPLICATION_URI =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ApplicationUri",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<String> ENDPOINT_URL =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "EndpointUrl",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<String> SECURITY_POLICY_URI =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SecurityPolicyUri",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<UserTokenPolicy> USER_TOKEN_TYPE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "UserTokenType",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=304"),
          -1,
          UserTokenPolicy.class);

  QualifiedProperty<UShort> REQUESTED_KEY_COUNT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "RequestedKeyCount",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=5"),
          -1,
          UShort.class);

  QualifiedProperty<Double> RETRY_INTERVAL =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "RetryInterval",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  QualifiedProperty<DateTime> LAST_PUSH_EXECUTION_TIME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "LastPushExecutionTime",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=13"),
          -1,
          DateTime.class);

  QualifiedProperty<DateTime> LAST_PUSH_ERROR_TIME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "LastPushErrorTime",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=13"),
          -1,
          DateTime.class);

  /** Gets the existing node's local value. */
  @Nullable String getApplicationUri() throws UaException;

  /** Sets the existing node's local value. */
  void setApplicationUri(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readApplicationUri() throws UaException;

  /** Writes the value remotely. */
  void writeApplicationUri(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readApplicationUriAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeApplicationUriAsync(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getApplicationUriNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getApplicationUriNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String getEndpointUrl() throws UaException;

  /** Sets the existing node's local value. */
  void setEndpointUrl(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readEndpointUrl() throws UaException;

  /** Writes the value remotely. */
  void writeEndpointUrl(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readEndpointUrlAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeEndpointUrlAsync(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getEndpointUrlNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getEndpointUrlNodeAsync();

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
  @Nullable UserTokenPolicy getUserTokenType() throws UaException;

  /** Sets the existing node's local value. */
  void setUserTokenType(@Nullable UserTokenPolicy value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UserTokenPolicy readUserTokenType() throws UaException;

  /** Writes the value remotely. */
  void writeUserTokenType(@Nullable UserTokenPolicy value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UserTokenPolicy> readUserTokenTypeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeUserTokenTypeAsync(@Nullable UserTokenPolicy value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getUserTokenTypeNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getUserTokenTypeNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UShort getRequestedKeyCount() throws UaException;

  /** Sets the existing node's local value. */
  void setRequestedKeyCount(@Nullable UShort value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UShort readRequestedKeyCount() throws UaException;

  /** Writes the value remotely. */
  void writeRequestedKeyCount(@Nullable UShort value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UShort> readRequestedKeyCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeRequestedKeyCountAsync(@Nullable UShort value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getRequestedKeyCountNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getRequestedKeyCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Double getRetryInterval() throws UaException;

  /** Sets the existing node's local value. */
  void setRetryInterval(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Double readRetryInterval() throws UaException;

  /** Writes the value remotely. */
  void writeRetryInterval(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Double> readRetryIntervalAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeRetryIntervalAsync(@Nullable Double value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getRetryIntervalNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getRetryIntervalNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable DateTime getLastPushExecutionTime() throws UaException;

  /** Sets the existing node's local value. */
  void setLastPushExecutionTime(@Nullable DateTime value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable DateTime readLastPushExecutionTime() throws UaException;

  /** Writes the value remotely. */
  void writeLastPushExecutionTime(@Nullable DateTime value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable DateTime> readLastPushExecutionTimeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeLastPushExecutionTimeAsync(@Nullable DateTime value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getLastPushExecutionTimeNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getLastPushExecutionTimeNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable DateTime getLastPushErrorTime() throws UaException;

  /** Sets the existing node's local value. */
  void setLastPushErrorTime(@Nullable DateTime value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable DateTime readLastPushErrorTime() throws UaException;

  /** Writes the value remotely. */
  void writeLastPushErrorTime(@Nullable DateTime value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable DateTime> readLastPushErrorTimeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeLastPushErrorTimeAsync(@Nullable DateTime value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getLastPushErrorTimeNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getLastPushErrorTimeNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.6.3
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  @NullMarked
  UaMethodNode getConnectSecurityGroupsMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.6.3
   *
   * <p>Returns the required node.
   *
   * @return a future completing with the required node.
   */
  @NullMarked
  CompletableFuture<? extends UaMethodNode> getConnectSecurityGroupsMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.6.3
   *
   * <p>Invokes <code>ConnectSecurityGroups</code> on this node's ObjectId using the effective
   * Method contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  @Nullable StatusCode @Nullable [] callConnectSecurityGroups(
      @Nullable NodeId @Nullable [] securityGroupIds) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.6.3
   *
   * <p>Invokes <code>ConnectSecurityGroups</code> on this node's ObjectId using the effective
   * Method contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable StatusCode @Nullable []> callConnectSecurityGroupsAsync(
      @Nullable NodeId @Nullable [] securityGroupIds);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.6.3
   *
   * <p>Invokes <code>ConnectSecurityGroups</code> on this node's ObjectId using the effective
   * Method contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable StatusCode @Nullable []> callConnectSecurityGroupsDetailed(
      @Nullable NodeId @Nullable [] securityGroupIds) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.6.3
   *
   * <p>Invokes <code>ConnectSecurityGroups</code> on this node's ObjectId using the effective
   * Method contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable StatusCode @Nullable []> callConnectSecurityGroupsDetailed(
      MethodCallOptions options, @Nullable NodeId @Nullable [] securityGroupIds) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.6.3
   *
   * <p>Invokes <code>ConnectSecurityGroups</code> on this node's ObjectId using the effective
   * Method contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable StatusCode @Nullable []>>
      callConnectSecurityGroupsDetailedAsync(@Nullable NodeId @Nullable [] securityGroupIds);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.6.3
   *
   * <p>Invokes <code>ConnectSecurityGroups</code> on this node's ObjectId using the effective
   * Method contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable StatusCode @Nullable []>>
      callConnectSecurityGroupsDetailedAsync(
          MethodCallOptions options, @Nullable NodeId @Nullable [] securityGroupIds);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.6.4
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  @NullMarked
  UaMethodNode getDisconnectSecurityGroupsMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.6.4
   *
   * <p>Returns the required node.
   *
   * @return a future completing with the required node.
   */
  @NullMarked
  CompletableFuture<? extends UaMethodNode> getDisconnectSecurityGroupsMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.6.4
   *
   * <p>Invokes <code>DisconnectSecurityGroups</code> on this node's ObjectId using the effective
   * Method contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  @Nullable StatusCode @Nullable [] callDisconnectSecurityGroups(
      @Nullable NodeId @Nullable [] securityGroupIds) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.6.4
   *
   * <p>Invokes <code>DisconnectSecurityGroups</code> on this node's ObjectId using the effective
   * Method contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable StatusCode @Nullable []> callDisconnectSecurityGroupsAsync(
      @Nullable NodeId @Nullable [] securityGroupIds);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.6.4
   *
   * <p>Invokes <code>DisconnectSecurityGroups</code> on this node's ObjectId using the effective
   * Method contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable StatusCode @Nullable []>
      callDisconnectSecurityGroupsDetailed(@Nullable NodeId @Nullable [] securityGroupIds)
          throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.6.4
   *
   * <p>Invokes <code>DisconnectSecurityGroups</code> on this node's ObjectId using the effective
   * Method contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable StatusCode @Nullable []>
      callDisconnectSecurityGroupsDetailed(
          MethodCallOptions options, @Nullable NodeId @Nullable [] securityGroupIds)
          throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.6.4
   *
   * <p>Invokes <code>DisconnectSecurityGroups</code> on this node's ObjectId using the effective
   * Method contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable StatusCode @Nullable []>>
      callDisconnectSecurityGroupsDetailedAsync(@Nullable NodeId @Nullable [] securityGroupIds);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.6.4
   *
   * <p>Invokes <code>DisconnectSecurityGroups</code> on this node's ObjectId using the effective
   * Method contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable StatusCode @Nullable []>>
      callDisconnectSecurityGroupsDetailedAsync(
          MethodCallOptions options, @Nullable NodeId @Nullable [] securityGroupIds);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.6.6
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  @NullMarked
  UaMethodNode getTriggerKeyUpdateMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.6.6
   *
   * <p>Returns the required node.
   *
   * @return a future completing with the required node.
   */
  @NullMarked
  CompletableFuture<? extends UaMethodNode> getTriggerKeyUpdateMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.6.6
   *
   * <p>Invokes <code>TriggerKeyUpdate</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  void callTriggerKeyUpdate() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.6.6
   *
   * <p>Invokes <code>TriggerKeyUpdate</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return a future whose successful payload is null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Void> callTriggerKeyUpdateAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.6.6
   *
   * <p>Invokes <code>TriggerKeyUpdate</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callTriggerKeyUpdateDetailed() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.6.6
   *
   * <p>Invokes <code>TriggerKeyUpdate</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callTriggerKeyUpdateDetailed(MethodCallOptions options)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.6.6
   *
   * <p>Invokes <code>TriggerKeyUpdate</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callTriggerKeyUpdateDetailedAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.6.6
   *
   * <p>Invokes <code>TriggerKeyUpdate</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callTriggerKeyUpdateDetailedAsync(MethodCallOptions options);
}

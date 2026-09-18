package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
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
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the PubSubKeyPushTargetType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.6.1">Model
 *     documentation</a>
 */
public interface PubSubKeyPushTargetType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 25337L);

  QualifiedProperty<String> EndpointUrl_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "EndpointUrl",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
          -1,
          String.class);

  QualifiedProperty<Double> RetryInterval_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "RetryInterval",
          ExpandedNodeId.of(Namespaces.OPC_UA, 290L),
          -1,
          Double.class);

  QualifiedProperty<UserTokenPolicy> UserTokenType_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "UserTokenType",
          ExpandedNodeId.of(Namespaces.OPC_UA, 304L),
          -1,
          UserTokenPolicy.class);

  QualifiedProperty<String> ApplicationUri_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ApplicationUri",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
          -1,
          String.class);

  QualifiedProperty<DateTime> LastPushErrorTime_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "LastPushErrorTime",
          ExpandedNodeId.of(Namespaces.OPC_UA, 13L),
          -1,
          DateTime.class);

  QualifiedProperty<UShort> RequestedKeyCount_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "RequestedKeyCount",
          ExpandedNodeId.of(Namespaces.OPC_UA, 5L),
          -1,
          UShort.class);

  QualifiedProperty<String> SecurityPolicyUri_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "SecurityPolicyUri",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
          -1,
          String.class);

  QualifiedProperty<DateTime> LastPushExecutionTime_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "LastPushExecutionTime",
          ExpandedNodeId.of(Namespaces.OPC_UA, 13L),
          -1,
          DateTime.class);

  /**
   * Resolves the mandatory EndpointUrl child, a PropertyType with DataType String.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getEndpointUrlNode() throws UaException;

  /** Asynchronous form of {@link #getEndpointUrlNode()}. */
  CompletableFuture<? extends PropertyType> getEndpointUrlNodeAsync();

  /**
   * Reads the Value of the EndpointUrl child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readEndpointUrl() throws UaException;

  /**
   * Writes the Value of the EndpointUrl child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeEndpointUrl(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readEndpointUrl()}. */
  CompletableFuture<? extends @Nullable String> readEndpointUrlAsync();

  /** Asynchronous form of {@link #writeEndpointUrl}; completes with the operation status. */
  CompletableFuture<StatusCode> writeEndpointUrlAsync(@Nullable String value);

  /**
   * Resolves the mandatory RetryInterval child, a PropertyType with DataType Duration.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getRetryIntervalNode() throws UaException;

  /** Asynchronous form of {@link #getRetryIntervalNode()}. */
  CompletableFuture<? extends PropertyType> getRetryIntervalNodeAsync();

  /**
   * Reads the Value of the RetryInterval child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Double readRetryInterval() throws UaException;

  /**
   * Writes the Value of the RetryInterval child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeRetryInterval(@Nullable Double value) throws UaException;

  /** Asynchronous form of {@link #readRetryInterval()}. */
  CompletableFuture<? extends @Nullable Double> readRetryIntervalAsync();

  /** Asynchronous form of {@link #writeRetryInterval}; completes with the operation status. */
  CompletableFuture<StatusCode> writeRetryIntervalAsync(@Nullable Double value);

  /**
   * Resolves the mandatory UserTokenType child, a PropertyType with DataType UserTokenPolicy.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getUserTokenTypeNode() throws UaException;

  /** Asynchronous form of {@link #getUserTokenTypeNode()}. */
  CompletableFuture<? extends PropertyType> getUserTokenTypeNodeAsync();

  /**
   * Reads the Value of the UserTokenType child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UserTokenPolicy readUserTokenType() throws UaException;

  /**
   * Writes the Value of the UserTokenType child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeUserTokenType(@Nullable UserTokenPolicy value) throws UaException;

  /** Asynchronous form of {@link #readUserTokenType()}. */
  CompletableFuture<? extends @Nullable UserTokenPolicy> readUserTokenTypeAsync();

  /** Asynchronous form of {@link #writeUserTokenType}; completes with the operation status. */
  CompletableFuture<StatusCode> writeUserTokenTypeAsync(@Nullable UserTokenPolicy value);

  /**
   * Resolves the mandatory ApplicationUri child, a PropertyType with DataType String.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getApplicationUriNode() throws UaException;

  /** Asynchronous form of {@link #getApplicationUriNode()}. */
  CompletableFuture<? extends PropertyType> getApplicationUriNodeAsync();

  /**
   * Reads the Value of the ApplicationUri child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readApplicationUri() throws UaException;

  /**
   * Writes the Value of the ApplicationUri child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeApplicationUri(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readApplicationUri()}. */
  CompletableFuture<? extends @Nullable String> readApplicationUriAsync();

  /** Asynchronous form of {@link #writeApplicationUri}; completes with the operation status. */
  CompletableFuture<StatusCode> writeApplicationUriAsync(@Nullable String value);

  /**
   * Resolves the mandatory LastPushErrorTime child, a PropertyType with DataType DateTime.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getLastPushErrorTimeNode() throws UaException;

  /** Asynchronous form of {@link #getLastPushErrorTimeNode()}. */
  CompletableFuture<? extends PropertyType> getLastPushErrorTimeNodeAsync();

  /**
   * Reads the Value of the LastPushErrorTime child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable DateTime readLastPushErrorTime() throws UaException;

  /**
   * Writes the Value of the LastPushErrorTime child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeLastPushErrorTime(@Nullable DateTime value) throws UaException;

  /** Asynchronous form of {@link #readLastPushErrorTime()}. */
  CompletableFuture<? extends @Nullable DateTime> readLastPushErrorTimeAsync();

  /** Asynchronous form of {@link #writeLastPushErrorTime}; completes with the operation status. */
  CompletableFuture<StatusCode> writeLastPushErrorTimeAsync(@Nullable DateTime value);

  /**
   * Resolves the mandatory RequestedKeyCount child, a PropertyType with DataType UInt16.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getRequestedKeyCountNode() throws UaException;

  /** Asynchronous form of {@link #getRequestedKeyCountNode()}. */
  CompletableFuture<? extends PropertyType> getRequestedKeyCountNodeAsync();

  /**
   * Reads the Value of the RequestedKeyCount child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UShort readRequestedKeyCount() throws UaException;

  /**
   * Writes the Value of the RequestedKeyCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeRequestedKeyCount(@Nullable UShort value) throws UaException;

  /** Asynchronous form of {@link #readRequestedKeyCount()}. */
  CompletableFuture<? extends @Nullable UShort> readRequestedKeyCountAsync();

  /** Asynchronous form of {@link #writeRequestedKeyCount}; completes with the operation status. */
  CompletableFuture<StatusCode> writeRequestedKeyCountAsync(@Nullable UShort value);

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
   * Resolves the mandatory LastPushExecutionTime child, a PropertyType with DataType DateTime.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getLastPushExecutionTimeNode() throws UaException;

  /** Asynchronous form of {@link #getLastPushExecutionTimeNode()}. */
  CompletableFuture<? extends PropertyType> getLastPushExecutionTimeNodeAsync();

  /**
   * Reads the Value of the LastPushExecutionTime child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable DateTime readLastPushExecutionTime() throws UaException;

  /**
   * Writes the Value of the LastPushExecutionTime child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeLastPushExecutionTime(@Nullable DateTime value) throws UaException;

  /** Asynchronous form of {@link #readLastPushExecutionTime()}. */
  CompletableFuture<? extends @Nullable DateTime> readLastPushExecutionTimeAsync();

  /**
   * Asynchronous form of {@link #writeLastPushExecutionTime}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeLastPushExecutionTimeAsync(@Nullable DateTime value);

  /**
   * Resolves the mandatory ConnectSecurityGroups Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.6.3">Model
   *     documentation</a>
   */
  UaMethodNode getConnectSecurityGroupsMethodNode() throws UaException;

  /** Asynchronous form of {@link #getConnectSecurityGroupsMethodNode()}. */
  CompletableFuture<UaMethodNode> getConnectSecurityGroupsMethodNodeAsync();

  /**
   * Calls the ConnectSecurityGroups Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.6.3">Model
   *     documentation</a>
   */
  StatusCode @Nullable [] connectSecurityGroups(NodeId @Nullable [] securityGroupIds)
      throws UaException;

  /**
   * Calls the ConnectSecurityGroups Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<StatusCode @Nullable []> callConnectSecurityGroups(
      NodeId @Nullable [] securityGroupIds) throws UaException;

  /**
   * Calls the ConnectSecurityGroups Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<StatusCode @Nullable []> callConnectSecurityGroupsWith(
      MethodCallOptions options, NodeId @Nullable [] securityGroupIds) throws UaException;

  /** Asynchronous form of {@link #connectSecurityGroups}. */
  CompletableFuture<StatusCode @Nullable []> connectSecurityGroupsAsync(
      NodeId @Nullable [] securityGroupIds);

  /** Asynchronous form of {@link #callConnectSecurityGroups}. */
  CompletableFuture<MethodCallResult<StatusCode @Nullable []>> callConnectSecurityGroupsAsync(
      NodeId @Nullable [] securityGroupIds);

  /** Asynchronous form of {@link #callConnectSecurityGroupsWith}. */
  CompletableFuture<MethodCallResult<StatusCode @Nullable []>> callConnectSecurityGroupsWithAsync(
      MethodCallOptions options, NodeId @Nullable [] securityGroupIds);

  /**
   * Resolves the mandatory DisconnectSecurityGroups Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.6.4">Model
   *     documentation</a>
   */
  UaMethodNode getDisconnectSecurityGroupsMethodNode() throws UaException;

  /** Asynchronous form of {@link #getDisconnectSecurityGroupsMethodNode()}. */
  CompletableFuture<UaMethodNode> getDisconnectSecurityGroupsMethodNodeAsync();

  /**
   * Calls the DisconnectSecurityGroups Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.6.4">Model
   *     documentation</a>
   */
  StatusCode @Nullable [] disconnectSecurityGroups(NodeId @Nullable [] securityGroupIds)
      throws UaException;

  /**
   * Calls the DisconnectSecurityGroups Method and returns the complete result, including a Bad
   * status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<StatusCode @Nullable []> callDisconnectSecurityGroups(
      NodeId @Nullable [] securityGroupIds) throws UaException;

  /**
   * Calls the DisconnectSecurityGroups Method with explicit options and returns the complete
   * result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<StatusCode @Nullable []> callDisconnectSecurityGroupsWith(
      MethodCallOptions options, NodeId @Nullable [] securityGroupIds) throws UaException;

  /** Asynchronous form of {@link #disconnectSecurityGroups}. */
  CompletableFuture<StatusCode @Nullable []> disconnectSecurityGroupsAsync(
      NodeId @Nullable [] securityGroupIds);

  /** Asynchronous form of {@link #callDisconnectSecurityGroups}. */
  CompletableFuture<MethodCallResult<StatusCode @Nullable []>> callDisconnectSecurityGroupsAsync(
      NodeId @Nullable [] securityGroupIds);

  /** Asynchronous form of {@link #callDisconnectSecurityGroupsWith}. */
  CompletableFuture<MethodCallResult<StatusCode @Nullable []>>
      callDisconnectSecurityGroupsWithAsync(
          MethodCallOptions options, NodeId @Nullable [] securityGroupIds);

  /**
   * Resolves the mandatory TriggerKeyUpdate Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.6.6">Model
   *     documentation</a>
   */
  UaMethodNode getTriggerKeyUpdateMethodNode() throws UaException;

  /** Asynchronous form of {@link #getTriggerKeyUpdateMethodNode()}. */
  CompletableFuture<UaMethodNode> getTriggerKeyUpdateMethodNodeAsync();

  /**
   * Calls the TriggerKeyUpdate Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.6.6">Model
   *     documentation</a>
   */
  void triggerKeyUpdate() throws UaException;

  /**
   * Calls the TriggerKeyUpdate Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callTriggerKeyUpdate() throws UaException;

  /**
   * Calls the TriggerKeyUpdate Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callTriggerKeyUpdateWith(MethodCallOptions options) throws UaException;

  /** Asynchronous form of {@link #triggerKeyUpdate}. */
  CompletableFuture<Void> triggerKeyUpdateAsync();

  /** Asynchronous form of {@link #callTriggerKeyUpdate}. */
  CompletableFuture<MethodCallResult<Void>> callTriggerKeyUpdateAsync();

  /** Asynchronous form of {@link #callTriggerKeyUpdateWith}. */
  CompletableFuture<MethodCallResult<Void>> callTriggerKeyUpdateWithAsync(
      MethodCallOptions options);
}

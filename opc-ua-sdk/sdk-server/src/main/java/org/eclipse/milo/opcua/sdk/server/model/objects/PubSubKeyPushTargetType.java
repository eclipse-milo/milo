/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.model.objects;

import com.digitalpetri.opcua.uanodeset.runtime.methods.MethodHandlerResult;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.sdk.core.nodes.MethodNode;
import org.eclipse.milo.opcua.sdk.server.methods.MethodBinding;
import org.eclipse.milo.opcua.sdk.server.methods.MethodBindings;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyType;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
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
  @Nullable String getApplicationUri();

  /** Sets the existing node's local value. */
  void setApplicationUri(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getApplicationUriNode();

  /** Gets the existing node's local value. */
  @Nullable String getEndpointUrl();

  /** Sets the existing node's local value. */
  void setEndpointUrl(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getEndpointUrlNode();

  /** Gets the existing node's local value. */
  @Nullable String getSecurityPolicyUri();

  /** Sets the existing node's local value. */
  void setSecurityPolicyUri(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getSecurityPolicyUriNode();

  /** Gets the existing node's local value. */
  @Nullable UserTokenPolicy getUserTokenType();

  /** Sets the existing node's local value. */
  void setUserTokenType(@Nullable UserTokenPolicy value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getUserTokenTypeNode();

  /** Gets the existing node's local value. */
  @Nullable UShort getRequestedKeyCount();

  /** Sets the existing node's local value. */
  void setRequestedKeyCount(@Nullable UShort value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getRequestedKeyCountNode();

  /** Gets the existing node's local value. */
  @Nullable Double getRetryInterval();

  /** Sets the existing node's local value. */
  void setRetryInterval(@Nullable Double value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getRetryIntervalNode();

  /** Gets the existing node's local value. */
  @Nullable DateTime getLastPushExecutionTime();

  /** Sets the existing node's local value. */
  void setLastPushExecutionTime(@Nullable DateTime value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getLastPushExecutionTimeNode();

  /** Gets the existing node's local value. */
  @Nullable DateTime getLastPushErrorTime();

  /** Sets the existing node's local value. */
  void setLastPushErrorTime(@Nullable DateTime value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getLastPushErrorTimeNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.6.3
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  MethodNode getConnectSecurityGroupsMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.6.3 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindConnectSecurityGroups(
      MethodBindings bindings, ConnectSecurityGroupsHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.6.3 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindConnectSecurityGroupsDetailed(
      MethodBindings bindings, ConnectSecurityGroupsDetailedHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.6.4
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  MethodNode getDisconnectSecurityGroupsMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.6.4 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindDisconnectSecurityGroups(
      MethodBindings bindings, DisconnectSecurityGroupsHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.6.4 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindDisconnectSecurityGroupsDetailed(
      MethodBindings bindings, DisconnectSecurityGroupsDetailedHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.6.6
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  MethodNode getTriggerKeyUpdateMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.6.6 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindTriggerKeyUpdate(MethodBindings bindings, TriggerKeyUpdateHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.6.6 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindTriggerKeyUpdateDetailed(
      MethodBindings bindings, TriggerKeyUpdateDetailedHandler handler) throws UaException;

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/8.6.3 */
  @FunctionalInterface
  interface ConnectSecurityGroupsHandler {
    /**
     * @return the output value, including null
     * @throws UaException for an operation failure
     */
    @Nullable StatusCode @Nullable [] invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable NodeId @Nullable [] securityGroupIds)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/8.6.3 */
  @FunctionalInterface
  interface ConnectSecurityGroupsDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable StatusCode @Nullable []> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable NodeId @Nullable [] securityGroupIds)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/8.6.4 */
  @FunctionalInterface
  interface DisconnectSecurityGroupsHandler {
    /**
     * @return the output value, including null
     * @throws UaException for an operation failure
     */
    @Nullable StatusCode @Nullable [] invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable NodeId @Nullable [] securityGroupIds)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/8.6.4 */
  @FunctionalInterface
  interface DisconnectSecurityGroupsDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable StatusCode @Nullable []> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable NodeId @Nullable [] securityGroupIds)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/8.6.6 */
  @FunctionalInterface
  interface TriggerKeyUpdateHandler {
    /**
     * @throws UaException for an operation failure
     */
    void invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/8.6.6 */
  @FunctionalInterface
  interface TriggerKeyUpdateDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable Void> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context)
        throws UaException;
  }
}

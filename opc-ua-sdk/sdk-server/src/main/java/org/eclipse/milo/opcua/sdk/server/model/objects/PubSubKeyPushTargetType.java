package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the PubSubKeyPushTargetType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.6.1">Model
 *     documentation</a>
 */
public interface PubSubKeyPushTargetType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 25337L);

  /**
   * Returns the mandatory ApplicationUri child, a PropertyType with DataType String.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getApplicationUriNode();

  /**
   * Returns the Value of the ApplicationUri child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getApplicationUri();

  /**
   * Sets the Value of the ApplicationUri child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setApplicationUri(@Nullable String value);

  /**
   * Returns the mandatory EndpointUrl child, a PropertyType with DataType String.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getEndpointUrlNode();

  /**
   * Returns the Value of the EndpointUrl child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getEndpointUrl();

  /**
   * Sets the Value of the EndpointUrl child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setEndpointUrl(@Nullable String value);

  /**
   * Returns the mandatory LastPushErrorTime child, a PropertyType with DataType DateTime.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getLastPushErrorTimeNode();

  /**
   * Returns the Value of the LastPushErrorTime child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable DateTime getLastPushErrorTime();

  /**
   * Sets the Value of the LastPushErrorTime child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setLastPushErrorTime(@Nullable DateTime value);

  /**
   * Returns the mandatory LastPushExecutionTime child, a PropertyType with DataType DateTime.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getLastPushExecutionTimeNode();

  /**
   * Returns the Value of the LastPushExecutionTime child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable DateTime getLastPushExecutionTime();

  /**
   * Sets the Value of the LastPushExecutionTime child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setLastPushExecutionTime(@Nullable DateTime value);

  /**
   * Returns the mandatory RequestedKeyCount child, a PropertyType with DataType UInt16.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getRequestedKeyCountNode();

  /**
   * Returns the Value of the RequestedKeyCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UShort getRequestedKeyCount();

  /**
   * Sets the Value of the RequestedKeyCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setRequestedKeyCount(@Nullable UShort value);

  /**
   * Returns the mandatory RetryInterval child, a PropertyType with DataType Duration.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getRetryIntervalNode();

  /**
   * Returns the Value of the RetryInterval child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Double getRetryInterval();

  /**
   * Sets the Value of the RetryInterval child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setRetryInterval(@Nullable Double value);

  /**
   * Returns the mandatory SecurityPolicyUri child, a PropertyType with DataType String.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getSecurityPolicyUriNode();

  /**
   * Returns the Value of the SecurityPolicyUri child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getSecurityPolicyUri();

  /**
   * Sets the Value of the SecurityPolicyUri child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSecurityPolicyUri(@Nullable String value);

  /**
   * Returns the mandatory UserTokenType child, a PropertyType with DataType UserTokenPolicy.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getUserTokenTypeNode();

  /**
   * Returns the Value of the UserTokenType child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UserTokenPolicy getUserTokenType();

  /**
   * Sets the Value of the UserTokenType child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setUserTokenType(@Nullable UserTokenPolicy value);

  /**
   * Returns the mandatory ConnectSecurityGroups Method node.
   *
   * @throws UaRuntimeException if the Method is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.6.3">Model
   *     documentation</a>
   */
  UaMethodNode getConnectSecurityGroupsMethodNode();

  /**
   * Sets this instance's ConnectSecurityGroups handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setConnectSecurityGroupsHandler(@Nullable ConnectSecurityGroupsHandler handler);

  /**
   * Returns the mandatory DisconnectSecurityGroups Method node.
   *
   * @throws UaRuntimeException if the Method is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.6.4">Model
   *     documentation</a>
   */
  UaMethodNode getDisconnectSecurityGroupsMethodNode();

  /**
   * Sets this instance's DisconnectSecurityGroups handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setDisconnectSecurityGroupsHandler(@Nullable DisconnectSecurityGroupsHandler handler);

  /**
   * Returns the mandatory TriggerKeyUpdate Method node.
   *
   * @throws UaRuntimeException if the Method is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.6.6">Model
   *     documentation</a>
   */
  UaMethodNode getTriggerKeyUpdateMethodNode();

  /**
   * Sets this instance's TriggerKeyUpdate handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setTriggerKeyUpdateHandler(@Nullable TriggerKeyUpdateHandler handler);

  /**
   * Sets this instance's Method handlers, including inherited handlers, from one implementation;
   * null clears them and restores Method-node fallback. Absent optional Methods are skipped.
   * Changes are applied in order; a failure does not roll back earlier changes.
   *
   * @throws UaRuntimeException if a mandatory Method is absent, or a Method is ambiguous or
   *     incompatible.
   */
  void setMethods(@Nullable Methods methods);

  /**
   * Handles calls to the ConnectSecurityGroups Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.6.3">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface ConnectSecurityGroupsHandler {
    /**
     * Handles a call to the ConnectSecurityGroups Method.
     *
     * @throws UaException if the call fails.
     */
    StatusCode @Nullable [] connectSecurityGroups(
        AbstractMethodInvocationHandler.InvocationContext context,
        NodeId @Nullable [] securityGroupIds)
        throws UaException;
  }

  /**
   * Handles calls to the DisconnectSecurityGroups Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.6.4">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface DisconnectSecurityGroupsHandler {
    /**
     * Handles a call to the DisconnectSecurityGroups Method.
     *
     * @throws UaException if the call fails.
     */
    StatusCode @Nullable [] disconnectSecurityGroups(
        AbstractMethodInvocationHandler.InvocationContext context,
        NodeId @Nullable [] securityGroupIds)
        throws UaException;
  }

  /**
   * Handles calls to the TriggerKeyUpdate Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.6.6">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface TriggerKeyUpdateHandler {
    /**
     * Handles a call to the TriggerKeyUpdate Method.
     *
     * @throws UaException if the call fails.
     */
    void triggerKeyUpdate(AbstractMethodInvocationHandler.InvocationContext context)
        throws UaException;
  }

  /** Implements this type's Methods. Unimplemented Methods report Bad_NotImplemented. */
  interface Methods {
    /**
     * Handles a call to the ConnectSecurityGroups Method; see {@link
     * ConnectSecurityGroupsHandler#connectSecurityGroups}.
     */
    default StatusCode @Nullable [] connectSecurityGroups(
        AbstractMethodInvocationHandler.InvocationContext context,
        NodeId @Nullable [] securityGroupIds)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /**
     * Handles a call to the DisconnectSecurityGroups Method; see {@link
     * DisconnectSecurityGroupsHandler#disconnectSecurityGroups}.
     */
    default StatusCode @Nullable [] disconnectSecurityGroups(
        AbstractMethodInvocationHandler.InvocationContext context,
        NodeId @Nullable [] securityGroupIds)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /**
     * Handles a call to the TriggerKeyUpdate Method; see {@link
     * TriggerKeyUpdateHandler#triggerKeyUpdate}.
     */
    default void triggerKeyUpdate(AbstractMethodInvocationHandler.InvocationContext context)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }
  }
}

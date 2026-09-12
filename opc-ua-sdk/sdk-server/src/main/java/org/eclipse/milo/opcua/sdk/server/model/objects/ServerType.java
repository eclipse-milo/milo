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
import org.eclipse.milo.opcua.sdk.core.model.methods.ServerTypeGetMonitoredItemsOutputs;
import org.eclipse.milo.opcua.sdk.core.nodes.MethodNode;
import org.eclipse.milo.opcua.sdk.server.methods.MethodBinding;
import org.eclipse.milo.opcua.sdk.server.methods.MethodBindings;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.server.model.variables.ServerStatusType;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ServerState;
import org.eclipse.milo.opcua.stack.core.types.structured.ServerStatusDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.TimeZoneDataType;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.1">https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.1</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface ServerType extends BaseObjectType {
  QualifiedProperty<String[]> SERVER_ARRAY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ServerArray",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          1,
          String[].class);

  QualifiedProperty<String[]> NAMESPACE_ARRAY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "NamespaceArray",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          1,
          String[].class);

  QualifiedProperty<UInteger> URIS_VERSION =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "UrisVersion",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=20998"),
          -1,
          UInteger.class);

  QualifiedProperty<UByte> SERVICE_LEVEL =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ServiceLevel",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=3"),
          -1,
          UByte.class);

  QualifiedProperty<Boolean> AUDITING =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Auditing",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<DateTime> ESTIMATED_RETURN_TIME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "EstimatedReturnTime",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=13"),
          -1,
          DateTime.class);

  QualifiedProperty<TimeZoneDataType> LOCAL_TIME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "LocalTime",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=8912"),
          -1,
          TimeZoneDataType.class);

  /** Gets the existing node's local value. */
  @Nullable String @Nullable [] getServerArray();

  /** Sets the existing node's local value. */
  void setServerArray(@Nullable String @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getServerArrayNode();

  /** Gets the existing node's local value. */
  @Nullable String @Nullable [] getNamespaceArray();

  /** Sets the existing node's local value. */
  void setNamespaceArray(@Nullable String @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getNamespaceArrayNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getUrisVersion();

  /** Sets the existing node's local value. */
  void setUrisVersion(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getUrisVersionNode();

  /** Gets the existing node's local value. */
  @Nullable UByte getServiceLevel();

  /** Sets the existing node's local value. */
  void setServiceLevel(@Nullable UByte value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getServiceLevelNode();

  /** Gets the existing node's local value. */
  @Nullable Boolean getAuditing();

  /** Sets the existing node's local value. */
  void setAuditing(@Nullable Boolean value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getAuditingNode();

  /** Gets the existing node's local value. */
  @Nullable DateTime getEstimatedReturnTime();

  /** Sets the existing node's local value. */
  void setEstimatedReturnTime(@Nullable DateTime value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getEstimatedReturnTimeNode();

  /** Gets the existing node's local value. */
  @Nullable TimeZoneDataType getLocalTime();

  /** Sets the existing node's local value. */
  void setLocalTime(@Nullable TimeZoneDataType value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getLocalTimeNode();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  ServerStatusType getServerStatusNode();

  /** Gets the existing node's local value. */
  @Nullable ServerStatusDataType getServerStatus();

  /** Sets the existing node's local value. */
  void setServerStatus(@Nullable ServerStatusDataType value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  ServerCapabilitiesType getServerCapabilitiesNode();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  ServerDiagnosticsType getServerDiagnosticsNode();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  VendorServerInfoType getVendorServerInfoNode();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  ServerRedundancyType getServerRedundancyNode();

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable NamespacesType getNamespacesNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part5/9.1
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable MethodNode getGetMonitoredItemsMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part5/9.1 Binds a synchronous callback for
   * this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindGetMonitoredItems(MethodBindings bindings, GetMonitoredItemsHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part5/9.1 Binds a synchronous callback for
   * this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindGetMonitoredItemsDetailed(
      MethodBindings bindings, GetMonitoredItemsDetailedHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part5/9.2
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable MethodNode getResendDataMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part5/9.2 Binds a synchronous callback for
   * this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindResendData(MethodBindings bindings, ResendDataHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part5/9.2 Binds a synchronous callback for
   * this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindResendDataDetailed(MethodBindings bindings, ResendDataDetailedHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part5/9.3
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable MethodNode getSetSubscriptionDurableMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part5/9.3 Binds a synchronous callback for
   * this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindSetSubscriptionDurable(
      MethodBindings bindings, SetSubscriptionDurableHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part5/9.3 Binds a synchronous callback for
   * this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindSetSubscriptionDurableDetailed(
      MethodBindings bindings, SetSubscriptionDurableDetailedHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part5/9.4
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable MethodNode getRequestServerStateChangeMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part5/9.4 Binds a synchronous callback for
   * this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindRequestServerStateChange(
      MethodBindings bindings, RequestServerStateChangeHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part5/9.4 Binds a synchronous callback for
   * this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindRequestServerStateChangeDetailed(
      MethodBindings bindings, RequestServerStateChangeDetailedHandler handler) throws UaException;

  /** https://reference.opcfoundation.org/v105/Core/docs/Part5/9.1 */
  @FunctionalInterface
  interface GetMonitoredItemsHandler {
    /**
     * @return a non-null container holding all output values
     * @throws UaException for an operation failure
     */
    ServerTypeGetMonitoredItemsOutputs invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable UInteger subscriptionId)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part5/9.1 */
  @FunctionalInterface
  interface GetMonitoredItemsDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<ServerTypeGetMonitoredItemsOutputs> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable UInteger subscriptionId)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part5/9.2 */
  @FunctionalInterface
  interface ResendDataHandler {
    /**
     * @throws UaException for an operation failure
     */
    void invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable UInteger subscriptionId)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part5/9.2 */
  @FunctionalInterface
  interface ResendDataDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable Void> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable UInteger subscriptionId)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part5/9.3 */
  @FunctionalInterface
  interface SetSubscriptionDurableHandler {
    /**
     * @return the output value, including null
     * @throws UaException for an operation failure
     */
    @Nullable UInteger invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable UInteger subscriptionId,
        @Nullable UInteger lifetimeInHours)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part5/9.3 */
  @FunctionalInterface
  interface SetSubscriptionDurableDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable UInteger> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable UInteger subscriptionId,
        @Nullable UInteger lifetimeInHours)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part5/9.4 */
  @FunctionalInterface
  interface RequestServerStateChangeHandler {
    /**
     * @throws UaException for an operation failure
     */
    void invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable ServerState state,
        @Nullable DateTime estimatedReturnTime,
        @Nullable UInteger secondsTillShutdown,
        @Nullable LocalizedText reason,
        @Nullable Boolean restart)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part5/9.4 */
  @FunctionalInterface
  interface RequestServerStateChangeDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable Void> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable ServerState state,
        @Nullable DateTime estimatedReturnTime,
        @Nullable UInteger secondsTillShutdown,
        @Nullable LocalizedText reason,
        @Nullable Boolean restart)
        throws UaException;
  }
}

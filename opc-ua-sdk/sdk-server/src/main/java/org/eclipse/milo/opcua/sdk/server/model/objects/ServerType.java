package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.core.model.methods.ServerTypeGetMonitoredItems;
import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.ServerStatusTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ServerState;
import org.eclipse.milo.opcua.stack.core.types.structured.ServerStatusDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.TimeZoneDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the ServerType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.1">Model
 *     documentation</a>
 */
public interface ServerType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2004L);

  /**
   * Returns the mandatory Auditing child, a PropertyType with DataType Boolean.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getAuditingNode();

  /**
   * Returns the Value of the Auditing child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Boolean getAuditing();

  /**
   * Sets the Value of the Auditing child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setAuditing(@Nullable Boolean value);

  /**
   * Returns the optional EstimatedReturnTime child, a PropertyType with DataType DateTime.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getEstimatedReturnTimeNode();

  /**
   * Returns the Value of the EstimatedReturnTime child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable DateTime getEstimatedReturnTime();

  /**
   * Sets the Value of the EstimatedReturnTime child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setEstimatedReturnTime(@Nullable DateTime value);

  /**
   * Returns the optional LocalTime child, a PropertyType with DataType TimeZoneDataType.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getLocalTimeNode();

  /**
   * Returns the Value of the LocalTime child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable TimeZoneDataType getLocalTime();

  /**
   * Sets the Value of the LocalTime child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setLocalTime(@Nullable TimeZoneDataType value);

  /**
   * Returns the mandatory NamespaceArray child, a PropertyType with DataType String.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getNamespaceArrayNode();

  /**
   * Returns the Value of the NamespaceArray child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String @Nullable [] getNamespaceArray();

  /**
   * Sets the Value of the NamespaceArray child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setNamespaceArray(@Nullable String @Nullable [] value);

  /**
   * Returns the optional Namespaces child, a NamespacesType.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.14">NamespacesType
   *     documentation</a>
   */
  @Nullable NamespacesTypeNode getNamespacesNode();

  /**
   * Returns the mandatory ServerArray child, a PropertyType with DataType String.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getServerArrayNode();

  /**
   * Returns the Value of the ServerArray child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String @Nullable [] getServerArray();

  /**
   * Sets the Value of the ServerArray child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setServerArray(@Nullable String @Nullable [] value);

  /**
   * Returns the mandatory ServerCapabilities child, a ServerCapabilitiesType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.2">ServerCapabilitiesType
   *     documentation</a>
   */
  ServerCapabilitiesTypeNode getServerCapabilitiesNode();

  /**
   * Returns the mandatory ServerDiagnostics child, a ServerDiagnosticsType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.3">ServerDiagnosticsType
   *     documentation</a>
   */
  ServerDiagnosticsTypeNode getServerDiagnosticsNode();

  /**
   * Returns the mandatory ServerRedundancy child, a ServerRedundancyType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.7">ServerRedundancyType
   *     documentation</a>
   */
  ServerRedundancyTypeNode getServerRedundancyNode();

  /**
   * Returns the mandatory ServerStatus child, a ServerStatusType with DataType
   * ServerStatusDataType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.6">ServerStatusType
   *     documentation</a>
   */
  ServerStatusTypeNode getServerStatusNode();

  /**
   * Returns the Value of the ServerStatus child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable ServerStatusDataType getServerStatus();

  /**
   * Sets the Value of the ServerStatus child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setServerStatus(@Nullable ServerStatusDataType value);

  /**
   * Returns the mandatory ServiceLevel child, a PropertyType with DataType Byte.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getServiceLevelNode();

  /**
   * Returns the Value of the ServiceLevel child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UByte getServiceLevel();

  /**
   * Sets the Value of the ServiceLevel child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setServiceLevel(@Nullable UByte value);

  /**
   * Returns the optional UrisVersion child, a PropertyType with DataType VersionTime.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getUrisVersionNode();

  /**
   * Returns the Value of the UrisVersion child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getUrisVersion();

  /**
   * Sets the Value of the UrisVersion child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setUrisVersion(@Nullable UInteger value);

  /**
   * Returns the mandatory VendorServerInfo child, a VendorServerInfoType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.6">VendorServerInfoType
   *     documentation</a>
   */
  VendorServerInfoTypeNode getVendorServerInfoNode();

  /**
   * Returns the optional GetMonitoredItems Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/9.1">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getGetMonitoredItemsMethodNode();

  /**
   * Sets this instance's GetMonitoredItems handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setGetMonitoredItemsHandler(@Nullable GetMonitoredItemsHandler handler);

  /**
   * Returns the optional RequestServerStateChange Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/9.4">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getRequestServerStateChangeMethodNode();

  /**
   * Sets this instance's RequestServerStateChange handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setRequestServerStateChangeHandler(@Nullable RequestServerStateChangeHandler handler);

  /**
   * Returns the optional ResendData Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/9.2">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getResendDataMethodNode();

  /**
   * Sets this instance's ResendData handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setResendDataHandler(@Nullable ResendDataHandler handler);

  /**
   * Returns the optional SetSubscriptionDurable Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/9.3">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getSetSubscriptionDurableMethodNode();

  /**
   * Sets this instance's SetSubscriptionDurable handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setSetSubscriptionDurableHandler(@Nullable SetSubscriptionDurableHandler handler);

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
   * Handles calls to the GetMonitoredItems Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/9.1">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface GetMonitoredItemsHandler {
    /**
     * Handles a call to the GetMonitoredItems Method.
     *
     * @throws UaException if the call fails.
     */
    ServerTypeGetMonitoredItems.Outputs getMonitoredItems(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable UInteger subscriptionId)
        throws UaException;
  }

  /**
   * Handles calls to the RequestServerStateChange Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/9.4">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface RequestServerStateChangeHandler {
    /**
     * Handles a call to the RequestServerStateChange Method.
     *
     * @throws UaException if the call fails.
     */
    void requestServerStateChange(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable ServerState state,
        @Nullable DateTime estimatedReturnTime,
        @Nullable UInteger secondsTillShutdown,
        @Nullable LocalizedText reason,
        @Nullable Boolean restart)
        throws UaException;
  }

  /**
   * Handles calls to the ResendData Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/9.2">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface ResendDataHandler {
    /**
     * Handles a call to the ResendData Method.
     *
     * @throws UaException if the call fails.
     */
    void resendData(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable UInteger subscriptionId)
        throws UaException;
  }

  /**
   * Handles calls to the SetSubscriptionDurable Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/9.3">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface SetSubscriptionDurableHandler {
    /**
     * Handles a call to the SetSubscriptionDurable Method.
     *
     * @throws UaException if the call fails.
     */
    @Nullable UInteger setSubscriptionDurable(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable UInteger subscriptionId,
        @Nullable UInteger lifetimeInHours)
        throws UaException;
  }

  /** Implements this type's Methods. Unimplemented Methods report Bad_NotImplemented. */
  interface Methods {
    /**
     * Handles a call to the GetMonitoredItems Method; see {@link
     * GetMonitoredItemsHandler#getMonitoredItems}.
     */
    default ServerTypeGetMonitoredItems.Outputs getMonitoredItems(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable UInteger subscriptionId)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /**
     * Handles a call to the RequestServerStateChange Method; see {@link
     * RequestServerStateChangeHandler#requestServerStateChange}.
     */
    default void requestServerStateChange(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable ServerState state,
        @Nullable DateTime estimatedReturnTime,
        @Nullable UInteger secondsTillShutdown,
        @Nullable LocalizedText reason,
        @Nullable Boolean restart)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /** Handles a call to the ResendData Method; see {@link ResendDataHandler#resendData}. */
    default void resendData(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable UInteger subscriptionId)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /**
     * Handles a call to the SetSubscriptionDurable Method; see {@link
     * SetSubscriptionDurableHandler#setSubscriptionDurable}.
     */
    default @Nullable UInteger setSubscriptionDurable(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable UInteger subscriptionId,
        @Nullable UInteger lifetimeInHours)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }
  }
}

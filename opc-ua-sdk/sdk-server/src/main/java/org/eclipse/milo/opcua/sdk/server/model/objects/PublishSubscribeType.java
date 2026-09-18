package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.KeyValuePair;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConnectionDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the PublishSubscribeType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.2">Model
 *     documentation</a>
 */
public interface PublishSubscribeType extends PubSubKeyServiceType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 14416L);

  /**
   * Returns the optional ConfigurationProperties child, a PropertyType with DataType KeyValuePair.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getConfigurationPropertiesNode();

  /**
   * Returns the Value of the ConfigurationProperties child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable KeyValuePair @Nullable [] getConfigurationProperties();

  /**
   * Sets the Value of the ConfigurationProperties child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setConfigurationProperties(@Nullable KeyValuePair @Nullable [] value);

  /**
   * Returns the optional ConfigurationVersion child, a PropertyType with DataType VersionTime.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getConfigurationVersionNode();

  /**
   * Returns the Value of the ConfigurationVersion child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getConfigurationVersion();

  /**
   * Sets the Value of the ConfigurationVersion child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setConfigurationVersion(@Nullable UInteger value);

  /**
   * Returns the optional DataSetClasses child, a FolderType.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.6">FolderType
   *     documentation</a>
   */
  @Nullable FolderTypeNode getDataSetClassesNode();

  /**
   * Returns the optional DefaultDatagramPublisherId child, a PropertyType with DataType UInt64.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getDefaultDatagramPublisherIdNode();

  /**
   * Returns the Value of the DefaultDatagramPublisherId child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable ULong getDefaultDatagramPublisherId();

  /**
   * Sets the Value of the DefaultDatagramPublisherId child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setDefaultDatagramPublisherId(@Nullable ULong value);

  /**
   * Returns the optional DefaultSecurityKeyServices child, a PropertyType with DataType
   * EndpointDescription.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getDefaultSecurityKeyServicesNode();

  /**
   * Returns the Value of the DefaultSecurityKeyServices child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable EndpointDescription @Nullable [] getDefaultSecurityKeyServices();

  /**
   * Sets the Value of the DefaultSecurityKeyServices child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setDefaultSecurityKeyServices(@Nullable EndpointDescription @Nullable [] value);

  /**
   * Returns the optional Diagnostics child, a PubSubDiagnosticsRootType.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.11/#9.1.11.7">PubSubDiagnosticsRootType
   *     documentation</a>
   */
  @Nullable PubSubDiagnosticsRootTypeNode getDiagnosticsNode();

  /**
   * Returns the optional PubSubCapablities child, a PubSubCapabilitiesType.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.12/#9.1.12.1">PubSubCapabilitiesType
   *     documentation</a>
   */
  @Nullable PubSubCapabilitiesTypeNode getPubSubCapablitiesNode();

  /**
   * Returns the optional PubSubConfiguration child, a PubSubConfigurationType.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.7.1">PubSubConfigurationType
   *     documentation</a>
   */
  @Nullable PubSubConfigurationTypeNode getPubSubConfigurationNode();

  /**
   * Returns the mandatory PublishedDataSets child, a DataSetFolderType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.1">DataSetFolderType
   *     documentation</a>
   */
  DataSetFolderTypeNode getPublishedDataSetsNode();

  /**
   * Returns the mandatory Status child, a PubSubStatusType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.10/#9.1.10.1">PubSubStatusType
   *     documentation</a>
   */
  PubSubStatusTypeNode getStatusNode();

  /**
   * Returns the optional SubscribedDataSets child, a SubscribedDataSetFolderType.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.4.1">SubscribedDataSetFolderType
   *     documentation</a>
   */
  @Nullable SubscribedDataSetFolderTypeNode getSubscribedDataSetsNode();

  /**
   * Returns the mandatory SupportedTransportProfiles child, a PropertyType with DataType String.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getSupportedTransportProfilesNode();

  /**
   * Returns the Value of the SupportedTransportProfiles child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String @Nullable [] getSupportedTransportProfiles();

  /**
   * Sets the Value of the SupportedTransportProfiles child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSupportedTransportProfiles(@Nullable String @Nullable [] value);

  /**
   * Returns the optional AddConnection Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.4">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getAddConnectionMethodNode();

  /**
   * Sets this instance's AddConnection handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setAddConnectionHandler(@Nullable AddConnectionHandler handler);

  /**
   * Returns the optional RemoveConnection Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.5">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getRemoveConnectionMethodNode();

  /**
   * Sets this instance's RemoveConnection handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setRemoveConnectionHandler(@Nullable RemoveConnectionHandler handler);

  /**
   * Returns the optional SetSecurityKeys Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.3">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getSetSecurityKeysMethodNode();

  /**
   * Sets this instance's SetSecurityKeys handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setSetSecurityKeysHandler(@Nullable SetSecurityKeysHandler handler);

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
   * Handles calls to the AddConnection Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.4">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface AddConnectionHandler {
    /**
     * Handles a call to the AddConnection Method.
     *
     * @throws UaException if the call fails.
     */
    @Nullable NodeId addConnection(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable PubSubConnectionDataType configuration)
        throws UaException;
  }

  /**
   * Handles calls to the RemoveConnection Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.5">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface RemoveConnectionHandler {
    /**
     * Handles a call to the RemoveConnection Method.
     *
     * @throws UaException if the call fails.
     */
    void removeConnection(
        AbstractMethodInvocationHandler.InvocationContext context, @Nullable NodeId connectionId)
        throws UaException;
  }

  /**
   * Handles calls to the SetSecurityKeys Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.3">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface SetSecurityKeysHandler {
    /**
     * Handles a call to the SetSecurityKeys Method.
     *
     * @throws UaException if the call fails.
     */
    void setSecurityKeys(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable String securityGroupId,
        @Nullable String securityPolicyUri,
        @Nullable UInteger currentTokenId,
        @Nullable ByteString currentKey,
        ByteString @Nullable [] futureKeys,
        @Nullable Double timeToNextKey,
        @Nullable Double keyLifetime)
        throws UaException;
  }

  /** Implements this type's Methods. Unimplemented Methods report Bad_NotImplemented. */
  interface Methods extends PubSubKeyServiceType.Methods {
    /**
     * Handles a call to the AddConnection Method; see {@link AddConnectionHandler#addConnection}.
     */
    default @Nullable NodeId addConnection(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable PubSubConnectionDataType configuration)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /**
     * Handles a call to the RemoveConnection Method; see {@link
     * RemoveConnectionHandler#removeConnection}.
     */
    default void removeConnection(
        AbstractMethodInvocationHandler.InvocationContext context, @Nullable NodeId connectionId)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /**
     * Handles a call to the SetSecurityKeys Method; see {@link
     * SetSecurityKeysHandler#setSecurityKeys}.
     */
    default void setSecurityKeys(
        AbstractMethodInvocationHandler.InvocationContext context,
        @Nullable String securityGroupId,
        @Nullable String securityPolicyUri,
        @Nullable UInteger currentTokenId,
        @Nullable ByteString currentKey,
        ByteString @Nullable [] futureKeys,
        @Nullable Double timeToNextKey,
        @Nullable Double keyLifetime)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }
  }
}

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
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.KeyValuePair;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConnectionDataType;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.2">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.2</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface PublishSubscribeType extends PubSubKeyServiceType {
  QualifiedProperty<String[]> SUPPORTED_TRANSPORT_PROFILES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SupportedTransportProfiles",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          1,
          String[].class);

  QualifiedProperty<ULong> DEFAULT_DATAGRAM_PUBLISHER_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DefaultDatagramPublisherId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=9"),
          -1,
          ULong.class);

  QualifiedProperty<UInteger> CONFIGURATION_VERSION =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ConfigurationVersion",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=20998"),
          -1,
          UInteger.class);

  QualifiedProperty<EndpointDescription[]> DEFAULT_SECURITY_KEY_SERVICES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DefaultSecurityKeyServices",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=312"),
          1,
          EndpointDescription[].class);

  QualifiedProperty<KeyValuePair[]> CONFIGURATION_PROPERTIES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ConfigurationProperties",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=14533"),
          1,
          KeyValuePair[].class);

  /** Gets the existing node's local value. */
  @Nullable String @Nullable [] getSupportedTransportProfiles();

  /** Sets the existing node's local value. */
  void setSupportedTransportProfiles(@Nullable String @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getSupportedTransportProfilesNode();

  /** Gets the existing node's local value. */
  @Nullable ULong getDefaultDatagramPublisherId();

  /** Sets the existing node's local value. */
  void setDefaultDatagramPublisherId(@Nullable ULong value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getDefaultDatagramPublisherIdNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getConfigurationVersion();

  /** Sets the existing node's local value. */
  void setConfigurationVersion(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getConfigurationVersionNode();

  /** Gets the existing node's local value. */
  @Nullable EndpointDescription @Nullable [] getDefaultSecurityKeyServices();

  /** Sets the existing node's local value. */
  void setDefaultSecurityKeyServices(@Nullable EndpointDescription @Nullable [] value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getDefaultSecurityKeyServicesNode();

  /** Gets the existing node's local value. */
  @Nullable KeyValuePair @Nullable [] getConfigurationProperties();

  /** Sets the existing node's local value. */
  void setConfigurationProperties(@Nullable KeyValuePair @Nullable [] value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getConfigurationPropertiesNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.3
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable MethodNode getSetSecurityKeysMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.3 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindSetSecurityKeys(MethodBindings bindings, SetSecurityKeysHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.3 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindSetSecurityKeysDetailed(
      MethodBindings bindings, SetSecurityKeysDetailedHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.4
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable MethodNode getAddConnectionMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.4 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindAddConnection(MethodBindings bindings, AddConnectionHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.4 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindAddConnectionDetailed(
      MethodBindings bindings, AddConnectionDetailedHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.5
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable MethodNode getRemoveConnectionMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.5 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindRemoveConnection(MethodBindings bindings, RemoveConnectionHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.5 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindRemoveConnectionDetailed(
      MethodBindings bindings, RemoveConnectionDetailedHandler handler) throws UaException;

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  DataSetFolderType getPublishedDataSetsNode();

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable SubscribedDataSetFolderType getSubscribedDataSetsNode();

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PubSubConfigurationType getPubSubConfigurationNode();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PubSubStatusType getStatusNode();

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PubSubDiagnosticsRootType getDiagnosticsNode();

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PubSubCapabilitiesType getPubSubCapablitiesNode();

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable FolderType getDataSetClassesNode();

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.3 */
  @FunctionalInterface
  interface SetSecurityKeysHandler {
    /**
     * @throws UaException for an operation failure
     */
    void invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable String securityGroupId,
        @Nullable String securityPolicyUri,
        @Nullable UInteger currentTokenId,
        @Nullable ByteString currentKey,
        @Nullable ByteString @Nullable [] futureKeys,
        @Nullable Double timeToNextKey,
        @Nullable Double keyLifetime)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.3 */
  @FunctionalInterface
  interface SetSecurityKeysDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable Void> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable String securityGroupId,
        @Nullable String securityPolicyUri,
        @Nullable UInteger currentTokenId,
        @Nullable ByteString currentKey,
        @Nullable ByteString @Nullable [] futureKeys,
        @Nullable Double timeToNextKey,
        @Nullable Double keyLifetime)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.4 */
  @FunctionalInterface
  interface AddConnectionHandler {
    /**
     * @return the output value, including null
     * @throws UaException for an operation failure
     */
    @Nullable NodeId invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable PubSubConnectionDataType configuration)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.4 */
  @FunctionalInterface
  interface AddConnectionDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable NodeId> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable PubSubConnectionDataType configuration)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.5 */
  @FunctionalInterface
  interface RemoveConnectionHandler {
    /**
     * @throws UaException for an operation failure
     */
    void invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable NodeId connectionId)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.5 */
  @FunctionalInterface
  interface RemoveConnectionDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable Void> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable NodeId connectionId)
        throws UaException;
  }
}

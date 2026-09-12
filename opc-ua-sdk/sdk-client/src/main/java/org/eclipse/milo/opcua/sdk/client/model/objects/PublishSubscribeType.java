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
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.KeyValuePair;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConnectionDataType;
import org.jspecify.annotations.NullMarked;
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
  @Nullable String @Nullable [] getSupportedTransportProfiles() throws UaException;

  /** Sets the existing node's local value. */
  void setSupportedTransportProfiles(@Nullable String @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String @Nullable [] readSupportedTransportProfiles() throws UaException;

  /** Writes the value remotely. */
  void writeSupportedTransportProfiles(@Nullable String @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String @Nullable []> readSupportedTransportProfilesAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSupportedTransportProfilesAsync(
      @Nullable String @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getSupportedTransportProfilesNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getSupportedTransportProfilesNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable ULong getDefaultDatagramPublisherId() throws UaException;

  /** Sets the existing node's local value. */
  void setDefaultDatagramPublisherId(@Nullable ULong value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable ULong readDefaultDatagramPublisherId() throws UaException;

  /** Writes the value remotely. */
  void writeDefaultDatagramPublisherId(@Nullable ULong value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable ULong> readDefaultDatagramPublisherIdAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeDefaultDatagramPublisherIdAsync(@Nullable ULong value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getDefaultDatagramPublisherIdNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getDefaultDatagramPublisherIdNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getConfigurationVersion() throws UaException;

  /** Sets the existing node's local value. */
  void setConfigurationVersion(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readConfigurationVersion() throws UaException;

  /** Writes the value remotely. */
  void writeConfigurationVersion(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readConfigurationVersionAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeConfigurationVersionAsync(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getConfigurationVersionNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getConfigurationVersionNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable EndpointDescription @Nullable [] getDefaultSecurityKeyServices() throws UaException;

  /** Sets the existing node's local value. */
  void setDefaultSecurityKeyServices(@Nullable EndpointDescription @Nullable [] value)
      throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable EndpointDescription @Nullable [] readDefaultSecurityKeyServices() throws UaException;

  /** Writes the value remotely. */
  void writeDefaultSecurityKeyServices(@Nullable EndpointDescription @Nullable [] value)
      throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable EndpointDescription @Nullable []>
      readDefaultSecurityKeyServicesAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeDefaultSecurityKeyServicesAsync(
      @Nullable EndpointDescription @Nullable [] value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getDefaultSecurityKeyServicesNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getDefaultSecurityKeyServicesNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable KeyValuePair @Nullable [] getConfigurationProperties() throws UaException;

  /** Sets the existing node's local value. */
  void setConfigurationProperties(@Nullable KeyValuePair @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable KeyValuePair @Nullable [] readConfigurationProperties() throws UaException;

  /** Writes the value remotely. */
  void writeConfigurationProperties(@Nullable KeyValuePair @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable KeyValuePair @Nullable []>
      readConfigurationPropertiesAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeConfigurationPropertiesAsync(
      @Nullable KeyValuePair @Nullable [] value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getConfigurationPropertiesNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getConfigurationPropertiesNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.3
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @NullMarked
  @Nullable UaMethodNode getSetSecurityKeysMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.3
   *
   * <p>Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode> getSetSecurityKeysMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.3
   *
   * <p>Invokes <code>SetSecurityKeys</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  void callSetSecurityKeys(
      @Nullable String securityGroupId,
      @Nullable String securityPolicyUri,
      @Nullable UInteger currentTokenId,
      @Nullable ByteString currentKey,
      @Nullable ByteString @Nullable [] futureKeys,
      @Nullable Double timeToNextKey,
      @Nullable Double keyLifetime)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.3
   *
   * <p>Invokes <code>SetSecurityKeys</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return a future whose successful payload is null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Void> callSetSecurityKeysAsync(
      @Nullable String securityGroupId,
      @Nullable String securityPolicyUri,
      @Nullable UInteger currentTokenId,
      @Nullable ByteString currentKey,
      @Nullable ByteString @Nullable [] futureKeys,
      @Nullable Double timeToNextKey,
      @Nullable Double keyLifetime);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.3
   *
   * <p>Invokes <code>SetSecurityKeys</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callSetSecurityKeysDetailed(
      @Nullable String securityGroupId,
      @Nullable String securityPolicyUri,
      @Nullable UInteger currentTokenId,
      @Nullable ByteString currentKey,
      @Nullable ByteString @Nullable [] futureKeys,
      @Nullable Double timeToNextKey,
      @Nullable Double keyLifetime)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.3
   *
   * <p>Invokes <code>SetSecurityKeys</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callSetSecurityKeysDetailed(
      MethodCallOptions options,
      @Nullable String securityGroupId,
      @Nullable String securityPolicyUri,
      @Nullable UInteger currentTokenId,
      @Nullable ByteString currentKey,
      @Nullable ByteString @Nullable [] futureKeys,
      @Nullable Double timeToNextKey,
      @Nullable Double keyLifetime)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.3
   *
   * <p>Invokes <code>SetSecurityKeys</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callSetSecurityKeysDetailedAsync(
          @Nullable String securityGroupId,
          @Nullable String securityPolicyUri,
          @Nullable UInteger currentTokenId,
          @Nullable ByteString currentKey,
          @Nullable ByteString @Nullable [] futureKeys,
          @Nullable Double timeToNextKey,
          @Nullable Double keyLifetime);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.3
   *
   * <p>Invokes <code>SetSecurityKeys</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callSetSecurityKeysDetailedAsync(
          MethodCallOptions options,
          @Nullable String securityGroupId,
          @Nullable String securityPolicyUri,
          @Nullable UInteger currentTokenId,
          @Nullable ByteString currentKey,
          @Nullable ByteString @Nullable [] futureKeys,
          @Nullable Double timeToNextKey,
          @Nullable Double keyLifetime);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.4
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @NullMarked
  @Nullable UaMethodNode getAddConnectionMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.4
   *
   * <p>Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode> getAddConnectionMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.4
   *
   * <p>Invokes <code>AddConnection</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  @Nullable NodeId callAddConnection(@Nullable PubSubConnectionDataType configuration)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.4
   *
   * <p>Invokes <code>AddConnection</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable NodeId> callAddConnectionAsync(
      @Nullable PubSubConnectionDataType configuration);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.4
   *
   * <p>Invokes <code>AddConnection</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable NodeId> callAddConnectionDetailed(
      @Nullable PubSubConnectionDataType configuration) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.4
   *
   * <p>Invokes <code>AddConnection</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable NodeId> callAddConnectionDetailed(
      MethodCallOptions options, @Nullable PubSubConnectionDataType configuration)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.4
   *
   * <p>Invokes <code>AddConnection</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable NodeId>>
      callAddConnectionDetailedAsync(@Nullable PubSubConnectionDataType configuration);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.4
   *
   * <p>Invokes <code>AddConnection</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable NodeId>>
      callAddConnectionDetailedAsync(
          MethodCallOptions options, @Nullable PubSubConnectionDataType configuration);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.5
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @NullMarked
  @Nullable UaMethodNode getRemoveConnectionMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.5
   *
   * <p>Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode> getRemoveConnectionMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.5
   *
   * <p>Invokes <code>RemoveConnection</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  void callRemoveConnection(@Nullable NodeId connectionId) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.5
   *
   * <p>Invokes <code>RemoveConnection</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return a future whose successful payload is null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Void> callRemoveConnectionAsync(
      @Nullable NodeId connectionId);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.5
   *
   * <p>Invokes <code>RemoveConnection</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callRemoveConnectionDetailed(
      @Nullable NodeId connectionId) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.5
   *
   * <p>Invokes <code>RemoveConnection</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callRemoveConnectionDetailed(
      MethodCallOptions options, @Nullable NodeId connectionId) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.5
   *
   * <p>Invokes <code>RemoveConnection</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callRemoveConnectionDetailedAsync(@Nullable NodeId connectionId);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.5
   *
   * <p>Invokes <code>RemoveConnection</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callRemoveConnectionDetailedAsync(MethodCallOptions options, @Nullable NodeId connectionId);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  DataSetFolderType getPublishedDataSetsNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends DataSetFolderType> getPublishedDataSetsNodeAsync();

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable SubscribedDataSetFolderType getSubscribedDataSetsNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable SubscribedDataSetFolderType>
      getSubscribedDataSetsNodeAsync();

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PubSubConfigurationType getPubSubConfigurationNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PubSubConfigurationType> getPubSubConfigurationNodeAsync();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PubSubStatusType getStatusNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PubSubStatusType> getStatusNodeAsync();

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PubSubDiagnosticsRootType getDiagnosticsNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PubSubDiagnosticsRootType> getDiagnosticsNodeAsync();

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PubSubCapabilitiesType getPubSubCapablitiesNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PubSubCapabilitiesType> getPubSubCapablitiesNodeAsync();

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable FolderType getDataSetClassesNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable FolderType> getDataSetClassesNodeAsync();
}

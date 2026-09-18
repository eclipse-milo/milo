package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
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
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the PublishSubscribeType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.2">Model
 *     documentation</a>
 */
public interface PublishSubscribeType extends PubSubKeyServiceType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 14416L);

  QualifiedProperty<UInteger> ConfigurationVersion_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ConfigurationVersion",
          ExpandedNodeId.of(Namespaces.OPC_UA, 20998L),
          -1,
          UInteger.class);

  QualifiedProperty<KeyValuePair[]> ConfigurationProperties_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ConfigurationProperties",
          ExpandedNodeId.of(Namespaces.OPC_UA, 14533L),
          1,
          KeyValuePair[].class);

  QualifiedProperty<ULong> DefaultDatagramPublisherId_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "DefaultDatagramPublisherId",
          ExpandedNodeId.of(Namespaces.OPC_UA, 9L),
          -1,
          ULong.class);

  QualifiedProperty<EndpointDescription[]> DefaultSecurityKeyServices_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "DefaultSecurityKeyServices",
          ExpandedNodeId.of(Namespaces.OPC_UA, 312L),
          1,
          EndpointDescription[].class);

  QualifiedProperty<String[]> SupportedTransportProfiles_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "SupportedTransportProfiles",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
          1,
          String[].class);

  /**
   * Resolves the optional Diagnostics child, a PubSubDiagnosticsRootType.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.11/#9.1.11.7">PubSubDiagnosticsRootType
   *     documentation</a>
   */
  @Nullable PubSubDiagnosticsRootType getDiagnosticsNode() throws UaException;

  /** Asynchronous form of {@link #getDiagnosticsNode()}. */
  CompletableFuture<? extends @Nullable PubSubDiagnosticsRootType> getDiagnosticsNodeAsync();

  /**
   * Resolves the optional DataSetClasses child, a FolderType.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.6">FolderType
   *     documentation</a>
   */
  @Nullable FolderType getDataSetClassesNode() throws UaException;

  /** Asynchronous form of {@link #getDataSetClassesNode()}. */
  CompletableFuture<? extends @Nullable FolderType> getDataSetClassesNodeAsync();

  /**
   * Resolves the optional PubSubCapablities child, a PubSubCapabilitiesType.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.12/#9.1.12.1">PubSubCapabilitiesType
   *     documentation</a>
   */
  @Nullable PubSubCapabilitiesType getPubSubCapablitiesNode() throws UaException;

  /** Asynchronous form of {@link #getPubSubCapablitiesNode()}. */
  CompletableFuture<? extends @Nullable PubSubCapabilitiesType> getPubSubCapablitiesNodeAsync();

  /**
   * Resolves the mandatory PublishedDataSets child, a DataSetFolderType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.1">DataSetFolderType
   *     documentation</a>
   */
  DataSetFolderType getPublishedDataSetsNode() throws UaException;

  /** Asynchronous form of {@link #getPublishedDataSetsNode()}. */
  CompletableFuture<? extends DataSetFolderType> getPublishedDataSetsNodeAsync();

  /**
   * Resolves the optional SubscribedDataSets child, a SubscribedDataSetFolderType.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.4.1">SubscribedDataSetFolderType
   *     documentation</a>
   */
  @Nullable SubscribedDataSetFolderType getSubscribedDataSetsNode() throws UaException;

  /** Asynchronous form of {@link #getSubscribedDataSetsNode()}. */
  CompletableFuture<? extends @Nullable SubscribedDataSetFolderType>
      getSubscribedDataSetsNodeAsync();

  /**
   * Resolves the optional PubSubConfiguration child, a PubSubConfigurationType.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.7.1">PubSubConfigurationType
   *     documentation</a>
   */
  @Nullable PubSubConfigurationType getPubSubConfigurationNode() throws UaException;

  /** Asynchronous form of {@link #getPubSubConfigurationNode()}. */
  CompletableFuture<? extends @Nullable PubSubConfigurationType> getPubSubConfigurationNodeAsync();

  /**
   * Resolves the optional ConfigurationVersion child, a PropertyType with DataType VersionTime.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getConfigurationVersionNode() throws UaException;

  /** Asynchronous form of {@link #getConfigurationVersionNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getConfigurationVersionNodeAsync();

  /**
   * Reads the Value of the ConfigurationVersion child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readConfigurationVersion() throws UaException;

  /**
   * Writes the Value of the ConfigurationVersion child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeConfigurationVersion(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readConfigurationVersion()}. */
  CompletableFuture<? extends @Nullable UInteger> readConfigurationVersionAsync();

  /**
   * Asynchronous form of {@link #writeConfigurationVersion}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeConfigurationVersionAsync(@Nullable UInteger value);

  /**
   * Resolves the optional ConfigurationProperties child, a PropertyType with DataType KeyValuePair.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getConfigurationPropertiesNode() throws UaException;

  /** Asynchronous form of {@link #getConfigurationPropertiesNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getConfigurationPropertiesNodeAsync();

  /**
   * Reads the Value of the ConfigurationProperties child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable KeyValuePair @Nullable [] readConfigurationProperties() throws UaException;

  /**
   * Writes the Value of the ConfigurationProperties child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeConfigurationProperties(@Nullable KeyValuePair @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readConfigurationProperties()}. */
  CompletableFuture<? extends @Nullable KeyValuePair @Nullable []>
      readConfigurationPropertiesAsync();

  /**
   * Asynchronous form of {@link #writeConfigurationProperties}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeConfigurationPropertiesAsync(
      @Nullable KeyValuePair @Nullable [] value);

  /**
   * Resolves the optional DefaultDatagramPublisherId child, a PropertyType with DataType UInt64.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getDefaultDatagramPublisherIdNode() throws UaException;

  /** Asynchronous form of {@link #getDefaultDatagramPublisherIdNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getDefaultDatagramPublisherIdNodeAsync();

  /**
   * Reads the Value of the DefaultDatagramPublisherId child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable ULong readDefaultDatagramPublisherId() throws UaException;

  /**
   * Writes the Value of the DefaultDatagramPublisherId child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeDefaultDatagramPublisherId(@Nullable ULong value) throws UaException;

  /** Asynchronous form of {@link #readDefaultDatagramPublisherId()}. */
  CompletableFuture<? extends @Nullable ULong> readDefaultDatagramPublisherIdAsync();

  /**
   * Asynchronous form of {@link #writeDefaultDatagramPublisherId}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeDefaultDatagramPublisherIdAsync(@Nullable ULong value);

  /**
   * Resolves the optional DefaultSecurityKeyServices child, a PropertyType with DataType
   * EndpointDescription.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getDefaultSecurityKeyServicesNode() throws UaException;

  /** Asynchronous form of {@link #getDefaultSecurityKeyServicesNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getDefaultSecurityKeyServicesNodeAsync();

  /**
   * Reads the Value of the DefaultSecurityKeyServices child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable EndpointDescription @Nullable [] readDefaultSecurityKeyServices() throws UaException;

  /**
   * Writes the Value of the DefaultSecurityKeyServices child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeDefaultSecurityKeyServices(@Nullable EndpointDescription @Nullable [] value)
      throws UaException;

  /** Asynchronous form of {@link #readDefaultSecurityKeyServices()}. */
  CompletableFuture<? extends @Nullable EndpointDescription @Nullable []>
      readDefaultSecurityKeyServicesAsync();

  /**
   * Asynchronous form of {@link #writeDefaultSecurityKeyServices}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeDefaultSecurityKeyServicesAsync(
      @Nullable EndpointDescription @Nullable [] value);

  /**
   * Resolves the mandatory SupportedTransportProfiles child, a PropertyType with DataType String.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getSupportedTransportProfilesNode() throws UaException;

  /** Asynchronous form of {@link #getSupportedTransportProfilesNode()}. */
  CompletableFuture<? extends PropertyType> getSupportedTransportProfilesNodeAsync();

  /**
   * Reads the Value of the SupportedTransportProfiles child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String @Nullable [] readSupportedTransportProfiles() throws UaException;

  /**
   * Writes the Value of the SupportedTransportProfiles child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSupportedTransportProfiles(@Nullable String @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readSupportedTransportProfiles()}. */
  CompletableFuture<? extends @Nullable String @Nullable []> readSupportedTransportProfilesAsync();

  /**
   * Asynchronous form of {@link #writeSupportedTransportProfiles}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeSupportedTransportProfilesAsync(
      @Nullable String @Nullable [] value);

  /**
   * Resolves the mandatory Status child, a PubSubStatusType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.10/#9.1.10.1">PubSubStatusType
   *     documentation</a>
   */
  PubSubStatusType getStatusNode() throws UaException;

  /** Asynchronous form of {@link #getStatusNode()}. */
  CompletableFuture<? extends PubSubStatusType> getStatusNodeAsync();

  /**
   * Resolves the optional AddConnection Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.4">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getAddConnectionMethodNode() throws UaException;

  /** Asynchronous form of {@link #getAddConnectionMethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getAddConnectionMethodNodeAsync();

  /**
   * Calls the AddConnection Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.4">Model
   *     documentation</a>
   */
  @Nullable NodeId addConnection(@Nullable PubSubConnectionDataType configuration)
      throws UaException;

  /**
   * Calls the AddConnection Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<@Nullable NodeId> callAddConnection(
      @Nullable PubSubConnectionDataType configuration) throws UaException;

  /**
   * Calls the AddConnection Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<@Nullable NodeId> callAddConnectionWith(
      MethodCallOptions options, @Nullable PubSubConnectionDataType configuration)
      throws UaException;

  /** Asynchronous form of {@link #addConnection}. */
  CompletableFuture<@Nullable NodeId> addConnectionAsync(
      @Nullable PubSubConnectionDataType configuration);

  /** Asynchronous form of {@link #callAddConnection}. */
  CompletableFuture<MethodCallResult<@Nullable NodeId>> callAddConnectionAsync(
      @Nullable PubSubConnectionDataType configuration);

  /** Asynchronous form of {@link #callAddConnectionWith}. */
  CompletableFuture<MethodCallResult<@Nullable NodeId>> callAddConnectionWithAsync(
      MethodCallOptions options, @Nullable PubSubConnectionDataType configuration);

  /**
   * Resolves the optional RemoveConnection Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.5">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getRemoveConnectionMethodNode() throws UaException;

  /** Asynchronous form of {@link #getRemoveConnectionMethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getRemoveConnectionMethodNodeAsync();

  /**
   * Calls the RemoveConnection Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.5">Model
   *     documentation</a>
   */
  void removeConnection(@Nullable NodeId connectionId) throws UaException;

  /**
   * Calls the RemoveConnection Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callRemoveConnection(@Nullable NodeId connectionId) throws UaException;

  /**
   * Calls the RemoveConnection Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callRemoveConnectionWith(
      MethodCallOptions options, @Nullable NodeId connectionId) throws UaException;

  /** Asynchronous form of {@link #removeConnection}. */
  CompletableFuture<Void> removeConnectionAsync(@Nullable NodeId connectionId);

  /** Asynchronous form of {@link #callRemoveConnection}. */
  CompletableFuture<MethodCallResult<Void>> callRemoveConnectionAsync(
      @Nullable NodeId connectionId);

  /** Asynchronous form of {@link #callRemoveConnectionWith}. */
  CompletableFuture<MethodCallResult<Void>> callRemoveConnectionWithAsync(
      MethodCallOptions options, @Nullable NodeId connectionId);

  /**
   * Resolves the optional SetSecurityKeys Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.3">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getSetSecurityKeysMethodNode() throws UaException;

  /** Asynchronous form of {@link #getSetSecurityKeysMethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getSetSecurityKeysMethodNodeAsync();

  /**
   * Calls the SetSecurityKeys Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.3">Model
   *     documentation</a>
   */
  void setSecurityKeys(
      @Nullable String securityGroupId,
      @Nullable String securityPolicyUri,
      @Nullable UInteger currentTokenId,
      @Nullable ByteString currentKey,
      ByteString @Nullable [] futureKeys,
      @Nullable Double timeToNextKey,
      @Nullable Double keyLifetime)
      throws UaException;

  /**
   * Calls the SetSecurityKeys Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callSetSecurityKeys(
      @Nullable String securityGroupId,
      @Nullable String securityPolicyUri,
      @Nullable UInteger currentTokenId,
      @Nullable ByteString currentKey,
      ByteString @Nullable [] futureKeys,
      @Nullable Double timeToNextKey,
      @Nullable Double keyLifetime)
      throws UaException;

  /**
   * Calls the SetSecurityKeys Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callSetSecurityKeysWith(
      MethodCallOptions options,
      @Nullable String securityGroupId,
      @Nullable String securityPolicyUri,
      @Nullable UInteger currentTokenId,
      @Nullable ByteString currentKey,
      ByteString @Nullable [] futureKeys,
      @Nullable Double timeToNextKey,
      @Nullable Double keyLifetime)
      throws UaException;

  /** Asynchronous form of {@link #setSecurityKeys}. */
  CompletableFuture<Void> setSecurityKeysAsync(
      @Nullable String securityGroupId,
      @Nullable String securityPolicyUri,
      @Nullable UInteger currentTokenId,
      @Nullable ByteString currentKey,
      ByteString @Nullable [] futureKeys,
      @Nullable Double timeToNextKey,
      @Nullable Double keyLifetime);

  /** Asynchronous form of {@link #callSetSecurityKeys}. */
  CompletableFuture<MethodCallResult<Void>> callSetSecurityKeysAsync(
      @Nullable String securityGroupId,
      @Nullable String securityPolicyUri,
      @Nullable UInteger currentTokenId,
      @Nullable ByteString currentKey,
      ByteString @Nullable [] futureKeys,
      @Nullable Double timeToNextKey,
      @Nullable Double keyLifetime);

  /** Asynchronous form of {@link #callSetSecurityKeysWith}. */
  CompletableFuture<MethodCallResult<Void>> callSetSecurityKeysWithAsync(
      MethodCallOptions options,
      @Nullable String securityGroupId,
      @Nullable String securityPolicyUri,
      @Nullable UInteger currentTokenId,
      @Nullable ByteString currentKey,
      ByteString @Nullable [] futureKeys,
      @Nullable Double timeToNextKey,
      @Nullable Double keyLifetime);
}

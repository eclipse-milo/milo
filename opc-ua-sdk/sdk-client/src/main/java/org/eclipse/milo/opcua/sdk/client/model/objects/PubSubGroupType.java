package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.KeyValuePair;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the PubSubGroupType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.2">Model
 *     documentation</a>
 */
public interface PubSubGroupType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 14232L);

  QualifiedProperty<MessageSecurityMode> SecurityMode_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "SecurityMode",
          ExpandedNodeId.of(Namespaces.OPC_UA, 302L),
          -1,
          MessageSecurityMode.class);

  QualifiedProperty<KeyValuePair[]> GroupProperties_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "GroupProperties",
          ExpandedNodeId.of(Namespaces.OPC_UA, 14533L),
          1,
          KeyValuePair[].class);

  QualifiedProperty<String> SecurityGroupId_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "SecurityGroupId",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
          -1,
          String.class);

  QualifiedProperty<EndpointDescription[]> SecurityKeyServices_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "SecurityKeyServices",
          ExpandedNodeId.of(Namespaces.OPC_UA, 312L),
          1,
          EndpointDescription[].class);

  QualifiedProperty<UInteger> MaxNetworkMessageSize_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "MaxNetworkMessageSize",
          ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
          -1,
          UInteger.class);

  /**
   * Resolves the mandatory SecurityMode child, a PropertyType with DataType MessageSecurityMode.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getSecurityModeNode() throws UaException;

  /** Asynchronous form of {@link #getSecurityModeNode()}. */
  CompletableFuture<? extends PropertyType> getSecurityModeNodeAsync();

  /**
   * Reads the Value of the SecurityMode child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable MessageSecurityMode readSecurityMode() throws UaException;

  /**
   * Writes the Value of the SecurityMode child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSecurityMode(@Nullable MessageSecurityMode value) throws UaException;

  /** Asynchronous form of {@link #readSecurityMode()}. */
  CompletableFuture<? extends @Nullable MessageSecurityMode> readSecurityModeAsync();

  /** Asynchronous form of {@link #writeSecurityMode}; completes with the operation status. */
  CompletableFuture<StatusCode> writeSecurityModeAsync(@Nullable MessageSecurityMode value);

  /**
   * Resolves the mandatory GroupProperties child, a PropertyType with DataType KeyValuePair.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getGroupPropertiesNode() throws UaException;

  /** Asynchronous form of {@link #getGroupPropertiesNode()}. */
  CompletableFuture<? extends PropertyType> getGroupPropertiesNodeAsync();

  /**
   * Reads the Value of the GroupProperties child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable KeyValuePair @Nullable [] readGroupProperties() throws UaException;

  /**
   * Writes the Value of the GroupProperties child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeGroupProperties(@Nullable KeyValuePair @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readGroupProperties()}. */
  CompletableFuture<? extends @Nullable KeyValuePair @Nullable []> readGroupPropertiesAsync();

  /** Asynchronous form of {@link #writeGroupProperties}; completes with the operation status. */
  CompletableFuture<StatusCode> writeGroupPropertiesAsync(
      @Nullable KeyValuePair @Nullable [] value);

  /**
   * Resolves the optional SecurityGroupId child, a PropertyType with DataType String.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getSecurityGroupIdNode() throws UaException;

  /** Asynchronous form of {@link #getSecurityGroupIdNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getSecurityGroupIdNodeAsync();

  /**
   * Reads the Value of the SecurityGroupId child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readSecurityGroupId() throws UaException;

  /**
   * Writes the Value of the SecurityGroupId child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSecurityGroupId(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readSecurityGroupId()}. */
  CompletableFuture<? extends @Nullable String> readSecurityGroupIdAsync();

  /** Asynchronous form of {@link #writeSecurityGroupId}; completes with the operation status. */
  CompletableFuture<StatusCode> writeSecurityGroupIdAsync(@Nullable String value);

  /**
   * Resolves the optional SecurityKeyServices child, a PropertyType with DataType
   * EndpointDescription.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getSecurityKeyServicesNode() throws UaException;

  /** Asynchronous form of {@link #getSecurityKeyServicesNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getSecurityKeyServicesNodeAsync();

  /**
   * Reads the Value of the SecurityKeyServices child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable EndpointDescription @Nullable [] readSecurityKeyServices() throws UaException;

  /**
   * Writes the Value of the SecurityKeyServices child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSecurityKeyServices(@Nullable EndpointDescription @Nullable [] value)
      throws UaException;

  /** Asynchronous form of {@link #readSecurityKeyServices()}. */
  CompletableFuture<? extends @Nullable EndpointDescription @Nullable []>
      readSecurityKeyServicesAsync();

  /**
   * Asynchronous form of {@link #writeSecurityKeyServices}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeSecurityKeyServicesAsync(
      @Nullable EndpointDescription @Nullable [] value);

  /**
   * Resolves the mandatory MaxNetworkMessageSize child, a PropertyType with DataType UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getMaxNetworkMessageSizeNode() throws UaException;

  /** Asynchronous form of {@link #getMaxNetworkMessageSizeNode()}. */
  CompletableFuture<? extends PropertyType> getMaxNetworkMessageSizeNodeAsync();

  /**
   * Reads the Value of the MaxNetworkMessageSize child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readMaxNetworkMessageSize() throws UaException;

  /**
   * Writes the Value of the MaxNetworkMessageSize child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMaxNetworkMessageSize(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readMaxNetworkMessageSize()}. */
  CompletableFuture<? extends @Nullable UInteger> readMaxNetworkMessageSizeAsync();

  /**
   * Asynchronous form of {@link #writeMaxNetworkMessageSize}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeMaxNetworkMessageSizeAsync(@Nullable UInteger value);

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
}

package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.EventFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the HistoricalExternalEventSourceType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part11/5.5.2">Model
 *     documentation</a>
 */
public interface HistoricalExternalEventSourceType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 32625L);

  QualifiedProperty<String> EndpointUrl_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "EndpointUrl",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
          -1,
          String.class);

  QualifiedProperty<MessageSecurityMode> SecurityMode_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "SecurityMode",
          ExpandedNodeId.of(Namespaces.OPC_UA, 302L),
          -1,
          MessageSecurityMode.class);

  QualifiedProperty<String> SecurityPolicyUri_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "SecurityPolicyUri",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
          -1,
          String.class);

  QualifiedProperty<UserTokenPolicy> IdentityTokenPolicy_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "IdentityTokenPolicy",
          ExpandedNodeId.of(Namespaces.OPC_UA, 304L),
          -1,
          UserTokenPolicy.class);

  QualifiedProperty<String> TransportProfileUri_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "TransportProfileUri",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
          -1,
          String.class);

  QualifiedProperty<EventFilter> HistoricalEventFilter_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "HistoricalEventFilter",
          ExpandedNodeId.of(Namespaces.OPC_UA, 725L),
          -1,
          EventFilter.class);

  QualifiedProperty<String> Server_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA, "Server", ExpandedNodeId.of(Namespaces.OPC_UA, 12L), -1, String.class);

  /**
   * Resolves the optional EndpointUrl child, a PropertyType with DataType String.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getEndpointUrlNode() throws UaException;

  /** Asynchronous form of {@link #getEndpointUrlNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getEndpointUrlNodeAsync();

  /**
   * Reads the Value of the EndpointUrl child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
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
   * Resolves the optional SecurityMode child, a PropertyType with DataType MessageSecurityMode.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getSecurityModeNode() throws UaException;

  /** Asynchronous form of {@link #getSecurityModeNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getSecurityModeNodeAsync();

  /**
   * Reads the Value of the SecurityMode child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
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
   * Resolves the optional SecurityPolicyUri child, a PropertyType with DataType String.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getSecurityPolicyUriNode() throws UaException;

  /** Asynchronous form of {@link #getSecurityPolicyUriNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getSecurityPolicyUriNodeAsync();

  /**
   * Reads the Value of the SecurityPolicyUri child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
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
   * Resolves the optional IdentityTokenPolicy child, a PropertyType with DataType UserTokenPolicy.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getIdentityTokenPolicyNode() throws UaException;

  /** Asynchronous form of {@link #getIdentityTokenPolicyNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getIdentityTokenPolicyNodeAsync();

  /**
   * Reads the Value of the IdentityTokenPolicy child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UserTokenPolicy readIdentityTokenPolicy() throws UaException;

  /**
   * Writes the Value of the IdentityTokenPolicy child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeIdentityTokenPolicy(@Nullable UserTokenPolicy value) throws UaException;

  /** Asynchronous form of {@link #readIdentityTokenPolicy()}. */
  CompletableFuture<? extends @Nullable UserTokenPolicy> readIdentityTokenPolicyAsync();

  /**
   * Asynchronous form of {@link #writeIdentityTokenPolicy}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeIdentityTokenPolicyAsync(@Nullable UserTokenPolicy value);

  /**
   * Resolves the optional TransportProfileUri child, a PropertyType with DataType String.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getTransportProfileUriNode() throws UaException;

  /** Asynchronous form of {@link #getTransportProfileUriNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getTransportProfileUriNodeAsync();

  /**
   * Reads the Value of the TransportProfileUri child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readTransportProfileUri() throws UaException;

  /**
   * Writes the Value of the TransportProfileUri child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeTransportProfileUri(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readTransportProfileUri()}. */
  CompletableFuture<? extends @Nullable String> readTransportProfileUriAsync();

  /**
   * Asynchronous form of {@link #writeTransportProfileUri}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeTransportProfileUriAsync(@Nullable String value);

  /**
   * Resolves the mandatory HistoricalEventFilter child, a PropertyType with DataType EventFilter.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getHistoricalEventFilterNode() throws UaException;

  /** Asynchronous form of {@link #getHistoricalEventFilterNode()}. */
  CompletableFuture<? extends PropertyType> getHistoricalEventFilterNodeAsync();

  /**
   * Reads the Value of the HistoricalEventFilter child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable EventFilter readHistoricalEventFilter() throws UaException;

  /**
   * Writes the Value of the HistoricalEventFilter child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeHistoricalEventFilter(@Nullable EventFilter value) throws UaException;

  /** Asynchronous form of {@link #readHistoricalEventFilter()}. */
  CompletableFuture<? extends @Nullable EventFilter> readHistoricalEventFilterAsync();

  /**
   * Asynchronous form of {@link #writeHistoricalEventFilter}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeHistoricalEventFilterAsync(@Nullable EventFilter value);

  /**
   * Resolves the optional Server child, a PropertyType with DataType String.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getServerNode() throws UaException;

  /** Asynchronous form of {@link #getServerNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getServerNodeAsync();

  /**
   * Reads the Value of the Server child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readServer() throws UaException;

  /**
   * Writes the Value of the Server child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeServer(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readServer()}. */
  CompletableFuture<? extends @Nullable String> readServerAsync();

  /** Asynchronous form of {@link #writeServer}; completes with the operation status. */
  CompletableFuture<StatusCode> writeServerAsync(@Nullable String value);
}

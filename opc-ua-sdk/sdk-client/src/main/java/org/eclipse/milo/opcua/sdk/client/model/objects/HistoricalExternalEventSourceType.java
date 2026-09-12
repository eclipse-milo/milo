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

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.EventFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part11/5.5.2">https://reference.opcfoundation.org/v105/Core/docs/Part11/5.5.2</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface HistoricalExternalEventSourceType extends BaseObjectType {
  QualifiedProperty<String> SERVER =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Server",
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

  QualifiedProperty<MessageSecurityMode> SECURITY_MODE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SecurityMode",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=302"),
          -1,
          MessageSecurityMode.class);

  QualifiedProperty<String> SECURITY_POLICY_URI =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SecurityPolicyUri",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<UserTokenPolicy> IDENTITY_TOKEN_POLICY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "IdentityTokenPolicy",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=304"),
          -1,
          UserTokenPolicy.class);

  QualifiedProperty<String> TRANSPORT_PROFILE_URI =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "TransportProfileUri",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<EventFilter> HISTORICAL_EVENT_FILTER =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "HistoricalEventFilter",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=725"),
          -1,
          EventFilter.class);

  /** Gets the existing node's local value. */
  @Nullable String getServer() throws UaException;

  /** Sets the existing node's local value. */
  void setServer(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readServer() throws UaException;

  /** Writes the value remotely. */
  void writeServer(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readServerAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeServerAsync(@Nullable String value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getServerNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getServerNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String getEndpointUrl() throws UaException;

  /** Sets the existing node's local value. */
  void setEndpointUrl(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readEndpointUrl() throws UaException;

  /** Writes the value remotely. */
  void writeEndpointUrl(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readEndpointUrlAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeEndpointUrlAsync(@Nullable String value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getEndpointUrlNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getEndpointUrlNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable MessageSecurityMode getSecurityMode() throws UaException;

  /** Sets the existing node's local value. */
  void setSecurityMode(@Nullable MessageSecurityMode value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable MessageSecurityMode readSecurityMode() throws UaException;

  /** Writes the value remotely. */
  void writeSecurityMode(@Nullable MessageSecurityMode value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable MessageSecurityMode> readSecurityModeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSecurityModeAsync(@Nullable MessageSecurityMode value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getSecurityModeNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getSecurityModeNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String getSecurityPolicyUri() throws UaException;

  /** Sets the existing node's local value. */
  void setSecurityPolicyUri(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readSecurityPolicyUri() throws UaException;

  /** Writes the value remotely. */
  void writeSecurityPolicyUri(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readSecurityPolicyUriAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSecurityPolicyUriAsync(@Nullable String value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getSecurityPolicyUriNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getSecurityPolicyUriNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UserTokenPolicy getIdentityTokenPolicy() throws UaException;

  /** Sets the existing node's local value. */
  void setIdentityTokenPolicy(@Nullable UserTokenPolicy value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UserTokenPolicy readIdentityTokenPolicy() throws UaException;

  /** Writes the value remotely. */
  void writeIdentityTokenPolicy(@Nullable UserTokenPolicy value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UserTokenPolicy> readIdentityTokenPolicyAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeIdentityTokenPolicyAsync(@Nullable UserTokenPolicy value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getIdentityTokenPolicyNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getIdentityTokenPolicyNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String getTransportProfileUri() throws UaException;

  /** Sets the existing node's local value. */
  void setTransportProfileUri(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readTransportProfileUri() throws UaException;

  /** Writes the value remotely. */
  void writeTransportProfileUri(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readTransportProfileUriAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeTransportProfileUriAsync(@Nullable String value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getTransportProfileUriNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getTransportProfileUriNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable EventFilter getHistoricalEventFilter() throws UaException;

  /** Sets the existing node's local value. */
  void setHistoricalEventFilter(@Nullable EventFilter value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable EventFilter readHistoricalEventFilter() throws UaException;

  /** Writes the value remotely. */
  void writeHistoricalEventFilter(@Nullable EventFilter value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable EventFilter> readHistoricalEventFilterAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeHistoricalEventFilterAsync(@Nullable EventFilter value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getHistoricalEventFilterNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getHistoricalEventFilterNodeAsync();
}

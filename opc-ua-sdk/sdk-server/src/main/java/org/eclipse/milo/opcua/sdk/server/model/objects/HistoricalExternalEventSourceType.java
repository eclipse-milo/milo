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

import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
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
  @Nullable String getServer();

  /** Sets the existing node's local value. */
  void setServer(@Nullable String value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getServerNode();

  /** Gets the existing node's local value. */
  @Nullable String getEndpointUrl();

  /** Sets the existing node's local value. */
  void setEndpointUrl(@Nullable String value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getEndpointUrlNode();

  /** Gets the existing node's local value. */
  @Nullable MessageSecurityMode getSecurityMode();

  /** Sets the existing node's local value. */
  void setSecurityMode(@Nullable MessageSecurityMode value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getSecurityModeNode();

  /** Gets the existing node's local value. */
  @Nullable String getSecurityPolicyUri();

  /** Sets the existing node's local value. */
  void setSecurityPolicyUri(@Nullable String value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getSecurityPolicyUriNode();

  /** Gets the existing node's local value. */
  @Nullable UserTokenPolicy getIdentityTokenPolicy();

  /** Sets the existing node's local value. */
  void setIdentityTokenPolicy(@Nullable UserTokenPolicy value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getIdentityTokenPolicyNode();

  /** Gets the existing node's local value. */
  @Nullable String getTransportProfileUri();

  /** Sets the existing node's local value. */
  void setTransportProfileUri(@Nullable String value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getTransportProfileUriNode();

  /** Gets the existing node's local value. */
  @Nullable EventFilter getHistoricalEventFilter();

  /** Sets the existing node's local value. */
  void setHistoricalEventFilter(@Nullable EventFilter value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getHistoricalEventFilterNode();
}

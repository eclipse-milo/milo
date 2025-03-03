/*
 * Copyright (c) 2025 the Eclipse Milo Authors
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

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part11/5.5.2">https://reference.opcfoundation.org/v105/Core/docs/Part11/5.5.2</a>
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

  String getServer();

  void setServer(String value);

  PropertyType getServerNode();

  String getEndpointUrl();

  void setEndpointUrl(String value);

  PropertyType getEndpointUrlNode();

  MessageSecurityMode getSecurityMode();

  void setSecurityMode(MessageSecurityMode value);

  PropertyType getSecurityModeNode();

  String getSecurityPolicyUri();

  void setSecurityPolicyUri(String value);

  PropertyType getSecurityPolicyUriNode();

  UserTokenPolicy getIdentityTokenPolicy();

  void setIdentityTokenPolicy(UserTokenPolicy value);

  PropertyType getIdentityTokenPolicyNode();

  String getTransportProfileUri();

  void setTransportProfileUri(String value);

  PropertyType getTransportProfileUriNode();

  EventFilter getHistoricalEventFilter();

  void setHistoricalEventFilter(EventFilter value);

  PropertyType getHistoricalEventFilterNode();
}

/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.servicesets.impl;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.eclipse.milo.opcua.sdk.server.EndpointConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfig;
import org.eclipse.milo.opcua.stack.core.security.DefaultCertificateManager;
import org.eclipse.milo.opcua.stack.core.security.MemoryCertificateQuarantine;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.FindServersRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.FindServersResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.RequestHeader;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;
import org.junit.jupiter.api.Test;

class DefaultDiscoveryServiceSetTest {

  private static final String LOCAL_APPLICATION_URI = "urn:eclipse:milo:test:server";
  private static final String PEER_APPLICATION_URI = "urn:eclipse:milo:test:peer";

  private static final ApplicationDescription PEER_DESCRIPTION =
      new ApplicationDescription(
          PEER_APPLICATION_URI,
          "urn:eclipse:milo:test",
          LocalizedText.english("peer server"),
          ApplicationType.Server,
          null,
          null,
          new String[] {"opc.tcp://peer-host:4840/milo/discovery"});

  @Test
  void emptyAdditionalServersMatchesDefaultBehavior() {
    OpcUaServer server = createServer();

    FindServersRequest request = findServersRequest("opc.tcp://localhost:4840/milo", null);

    FindServersResponse defaultResponse =
        new DefaultDiscoveryServiceSet(server)
            .onFindServers(mock(ServiceRequestContext.class), request);
    FindServersResponse response =
        new DefaultDiscoveryServiceSet(server, List::of)
            .onFindServers(mock(ServiceRequestContext.class), request);

    assertArrayEquals(defaultResponse.getServers(), response.getServers());
  }

  @Test
  void additionalServersAreIncludedInFindServersResults() {
    OpcUaServer server = createServer();

    DefaultDiscoveryServiceSet serviceSet =
        new DefaultDiscoveryServiceSet(server, () -> List.of(PEER_DESCRIPTION));

    FindServersResponse response =
        serviceSet.onFindServers(
            mock(ServiceRequestContext.class),
            findServersRequest("opc.tcp://localhost:4840/milo", null));

    ApplicationDescription[] servers = response.getServers();
    assertEquals(2, servers.length);
    assertEquals(LOCAL_APPLICATION_URI, servers[0].getApplicationUri());
    assertEquals(PEER_DESCRIPTION, servers[1]);
  }

  @Test
  void serverUrisFilterSelectsOnlyAdditionalServer() {
    OpcUaServer server = createServer();

    DefaultDiscoveryServiceSet serviceSet =
        new DefaultDiscoveryServiceSet(server, () -> List.of(PEER_DESCRIPTION));

    FindServersResponse response =
        serviceSet.onFindServers(
            mock(ServiceRequestContext.class),
            findServersRequest(
                "opc.tcp://localhost:4840/milo", new String[] {PEER_APPLICATION_URI}));

    assertArrayEquals(new ApplicationDescription[] {PEER_DESCRIPTION}, response.getServers());
  }

  @Test
  void serverUrisFilterSelectsOnlyLocalServer() {
    OpcUaServer server = createServer();

    DefaultDiscoveryServiceSet serviceSet =
        new DefaultDiscoveryServiceSet(server, () -> List.of(PEER_DESCRIPTION));

    FindServersResponse response =
        serviceSet.onFindServers(
            mock(ServiceRequestContext.class),
            findServersRequest(
                "opc.tcp://localhost:4840/milo", new String[] {LOCAL_APPLICATION_URI}));

    ApplicationDescription[] servers = response.getServers();
    assertEquals(1, servers.length);
    assertEquals(LOCAL_APPLICATION_URI, servers[0].getApplicationUri());
  }

  @Test
  void serverUrisFilterWithUnknownUriReturnsNoServers() {
    OpcUaServer server = createServer();

    DefaultDiscoveryServiceSet serviceSet =
        new DefaultDiscoveryServiceSet(server, () -> List.of(PEER_DESCRIPTION));

    FindServersResponse response =
        serviceSet.onFindServers(
            mock(ServiceRequestContext.class),
            findServersRequest("opc.tcp://localhost:4840/milo", new String[] {"urn:unknown"}));

    assertEquals(0, response.getServers().length);
  }

  private static OpcUaServer createServer() {
    Set<EndpointConfig> endpoints = new LinkedHashSet<>();
    endpoints.add(endpoint("/milo"));
    endpoints.add(endpoint("/milo/discovery"));

    OpcUaServerConfig config =
        OpcUaServerConfig.builder()
            .setApplicationUri(LOCAL_APPLICATION_URI)
            .setApplicationName(LocalizedText.english("test server"))
            .setProductUri("urn:eclipse:milo:test")
            .setCertificateManager(new DefaultCertificateManager(new MemoryCertificateQuarantine()))
            .setEndpoints(endpoints)
            .build();

    return new OpcUaServer(config, transportProfile -> null);
  }

  private static EndpointConfig endpoint(String path) {
    return EndpointConfig.newBuilder()
        .setBindPort(4840)
        .setHostname("localhost")
        .setPath(path)
        .setSecurityPolicy(SecurityPolicy.None)
        .setSecurityMode(MessageSecurityMode.None)
        .addTokenPolicy(OpcUaServerConfig.USER_TOKEN_POLICY_ANONYMOUS)
        .build();
  }

  private static FindServersRequest findServersRequest(String endpointUrl, String[] serverUris) {
    RequestHeader requestHeader =
        new RequestHeader(NodeId.NULL_VALUE, DateTime.now(), uint(1), uint(0), null, uint(0), null);

    return new FindServersRequest(requestHeader, endpointUrl, null, serverUris);
  }
}

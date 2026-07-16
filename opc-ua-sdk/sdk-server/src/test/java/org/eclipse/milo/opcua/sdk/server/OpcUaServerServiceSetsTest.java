/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import org.eclipse.milo.opcua.sdk.server.servicesets.AttributeServiceSet;
import org.eclipse.milo.opcua.sdk.server.servicesets.DiscoveryServiceSet;
import org.eclipse.milo.opcua.sdk.server.servicesets.Service;
import org.eclipse.milo.opcua.sdk.server.servicesets.impl.DefaultDiscoveryServiceSet;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.DefaultCertificateManager;
import org.eclipse.milo.opcua.stack.core.security.MemoryCertificateQuarantine;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.FindServersRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.FindServersResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryReadRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryReadResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryUpdateRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryUpdateResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.RequestHeader;
import org.eclipse.milo.opcua.stack.core.types.structured.WriteRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.WriteResponse;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;
import org.junit.jupiter.api.Test;

class OpcUaServerServiceSetsTest {

  private static final ApplicationDescription ADDITIONAL_SERVER =
      new ApplicationDescription(
          "urn:eclipse:milo:test:additional",
          "urn:eclipse:milo:test",
          LocalizedText.english("additional server"),
          ApplicationType.Server,
          null,
          null,
          new String[] {"opc.tcp://additional-host:4840/milo/discovery"});

  @Test
  void customDiscoveryServiceSetIsRegisteredOnEveryEndpointPath() throws Exception {
    OpcUaServer server =
        createServer(
            new ServiceSets() {
              @Override
              public DiscoveryServiceSet createDiscoveryServiceSet(OpcUaServer server) {
                return new DefaultDiscoveryServiceSet(server, () -> List.of(ADDITIONAL_SERVER));
              }
            });

    for (String path : List.of("/milo", "/milo/discovery")) {
      AbstractServiceHandler.ServiceHandler handler =
          server.getServiceHandler(path, Service.DISCOVERY_FIND_SERVERS);
      assertNotNull(handler, "expected a FindServers handler at path " + path);

      FindServersResponse response =
          (FindServersResponse)
              handler.handle(
                  mock(ServiceRequestContext.class),
                  findServersRequest("opc.tcp://localhost:4840" + path));

      ApplicationDescription[] servers = response.getServers();
      assertEquals(2, servers.length, "expected the custom service set at path " + path);
      assertEquals(ADDITIONAL_SERVER, servers[1]);
    }
  }

  @Test
  void customNonDiscoveryServiceSetIsRegisteredOnNonDiscoveryPathsOnly() throws Exception {
    ReadResponse cannedResponse = new ReadResponse(null, null, null);

    OpcUaServer server =
        createServer(
            new ServiceSets() {
              @Override
              public AttributeServiceSet createAttributeServiceSet(OpcUaServer server) {
                return new StubAttributeServiceSet(cannedResponse);
              }
            });

    AbstractServiceHandler.ServiceHandler handler =
        server.getServiceHandler("/milo", Service.ATTRIBUTE_READ);
    assertNotNull(handler);

    Object response = handler.handle(mock(ServiceRequestContext.class), readRequest());
    assertSame(cannedResponse, response);

    assertNull(
        server.getServiceHandler("/milo/discovery", Service.ATTRIBUTE_READ),
        "non-discovery service sets must not be registered on a /discovery path");
  }

  @Test
  void eachFactoryMethodIsInvokedOnceRegardlessOfPathCount() {
    AtomicInteger discoveryCount = new AtomicInteger();
    AtomicInteger attributeCount = new AtomicInteger();

    createServer(
        new ServiceSets() {
          @Override
          public DiscoveryServiceSet createDiscoveryServiceSet(OpcUaServer server) {
            discoveryCount.incrementAndGet();
            return ServiceSets.super.createDiscoveryServiceSet(server);
          }

          @Override
          public AttributeServiceSet createAttributeServiceSet(OpcUaServer server) {
            attributeCount.incrementAndGet();
            return ServiceSets.super.createAttributeServiceSet(server);
          }
        });

    assertEquals(1, discoveryCount.get());
    assertEquals(1, attributeCount.get());
  }

  @Test
  void defaultConstructorRegistersDefaultServiceSets() {
    OpcUaServer server = new OpcUaServer(serverConfig(), transportProfile -> null);

    for (String path : List.of("/milo", "/milo/discovery")) {
      assertNotNull(server.getServiceHandler(path, Service.DISCOVERY_FIND_SERVERS));
    }
    assertNotNull(server.getServiceHandler("/milo", Service.ATTRIBUTE_READ));
    assertNull(server.getServiceHandler("/milo/discovery", Service.ATTRIBUTE_READ));
  }

  private static OpcUaServer createServer(ServiceSets serviceSets) {
    return new OpcUaServer(serverConfig(), transportProfile -> null, serviceSets);
  }

  private static OpcUaServerConfig serverConfig() {
    Set<EndpointConfig> endpoints = new LinkedHashSet<>();
    endpoints.add(endpoint("/milo"));
    endpoints.add(endpoint("/milo/discovery"));

    return OpcUaServerConfig.builder()
        .setApplicationUri("urn:eclipse:milo:test:server")
        .setApplicationName(LocalizedText.english("test server"))
        .setProductUri("urn:eclipse:milo:test")
        .setCertificateManager(new DefaultCertificateManager(new MemoryCertificateQuarantine()))
        .setEndpoints(endpoints)
        .build();
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

  private static RequestHeader requestHeader() {
    return new RequestHeader(
        NodeId.NULL_VALUE, DateTime.now(), uint(1), uint(0), null, uint(0), null);
  }

  private static FindServersRequest findServersRequest(String endpointUrl) {
    return new FindServersRequest(requestHeader(), endpointUrl, null, null);
  }

  private static ReadRequest readRequest() {
    return new ReadRequest(requestHeader(), 0.0, TimestampsToReturn.Both, new ReadValueId[0]);
  }

  private static class StubAttributeServiceSet implements AttributeServiceSet {

    private final ReadResponse readResponse;

    private StubAttributeServiceSet(ReadResponse readResponse) {
      this.readResponse = readResponse;
    }

    @Override
    public ReadResponse onRead(ServiceRequestContext context, ReadRequest request) {
      return readResponse;
    }

    @Override
    public HistoryReadResponse onHistoryRead(
        ServiceRequestContext context, HistoryReadRequest request) throws UaException {
      throw new UaException(StatusCodes.Bad_ServiceUnsupported);
    }

    @Override
    public WriteResponse onWrite(ServiceRequestContext context, WriteRequest request)
        throws UaException {
      throw new UaException(StatusCodes.Bad_ServiceUnsupported);
    }

    @Override
    public HistoryUpdateResponse onHistoryUpdate(
        ServiceRequestContext context, HistoryUpdateRequest request) throws UaException {
      throw new UaException(StatusCodes.Bad_ServiceUnsupported);
    }
  }
}

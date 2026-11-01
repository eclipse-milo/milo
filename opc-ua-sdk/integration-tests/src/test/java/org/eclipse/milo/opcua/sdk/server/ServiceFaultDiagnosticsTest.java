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
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import org.eclipse.milo.opcua.sdk.client.OpcUaClientConfigBuilder;
import org.eclipse.milo.opcua.sdk.client.OpcUaSession;
import org.eclipse.milo.opcua.sdk.server.servicesets.AttributeServiceSet;
import org.eclipse.milo.opcua.sdk.server.servicesets.DiscoveryServiceSet;
import org.eclipse.milo.opcua.sdk.server.servicesets.impl.DefaultAttributeServiceSet;
import org.eclipse.milo.opcua.sdk.server.servicesets.impl.DefaultDiscoveryServiceSet;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.sdk.test.TestServer;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.Diagnostic;
import org.eclipse.milo.opcua.stack.core.ReturnDiagnostics;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaServiceFaultException;
import org.eclipse.milo.opcua.stack.core.types.UaRequestMessageType;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.FindServersOnNetworkRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.FindServersOnNetworkResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.FindServersRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.FindServersResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.GetEndpointsRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.GetEndpointsResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.RegisterServer2Request;
import org.eclipse.milo.opcua.stack.core.types.structured.RegisterServer2Response;
import org.eclipse.milo.opcua.stack.core.types.structured.RegisterServerRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.RegisterServerResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.RequestHeader;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Part 4 §7.34: a ServiceFault carries service-level diagnostics as requested by the request's
 * returnDiagnostics, subject to the server's {@link DiagnosticsPolicy}.
 */
class ServiceFaultDiagnosticsTest extends AbstractClientServerTest {

  private static final int SERVICE_LEVEL = 0x01F;

  private static final NodeId OFFLINE = new NodeId(1, "fault:offline");
  private static final NodeId DENIED = new NodeId(1, "fault:denied");

  /** A GetEndpoints locale that makes the test's discovery service set fail the request. */
  private static final String FAIL_LOCALE = "x-milo-fault-test";

  private static final Diagnostic OFFLINE_DIAGNOSTIC =
      Diagnostic.symbolic("urn:eclipse:milo:test:gateway", "E_OFFLINE")
          .withText("The device gateway is offline")
          .withInner(new StatusCode(StatusCodes.Bad_Timeout), Diagnostic.text("no reply"));

  private final AtomicReference<DiagnosticsPolicy> policy =
      new AtomicReference<>(DiagnosticsPolicy.defaults());

  // Service-level fields only, so faults explain themselves without operation-level traffic.
  @Override
  protected void customizeClientConfig(OpcUaClientConfigBuilder configBuilder) {
    configBuilder.setReturnDiagnostics(
        new ReturnDiagnostics(EnumSet.allOf(ReturnDiagnostics.Field.class), Set.of()));
  }

  @Override
  protected TestServer createTestServer() throws Exception {
    TestServer testServer =
        TestServer.create(
            builder ->
                builder.setDiagnosticsPolicy(
                    (request, status, diagnostic) ->
                        policy.get().filter(request, status, diagnostic)));

    OpcUaServer server = testServer.getServer();

    for (EndpointConfig endpoint : server.getConfig().getEndpoints()) {
      server.addServiceSet(endpoint.getPath(), (AttributeServiceSet) new FailingAttributes(server));
      server.addServiceSet(endpoint.getPath(), (DiscoveryServiceSet) new FailingDiscovery(server));
    }

    return testServer;
  }

  @Test
  void faultInAnActivatedSessionCarriesTheServiceDiagnostic() throws Exception {
    UaServiceFaultException fault = fault(read(SERVICE_LEVEL, OFFLINE));

    assertEquals(new StatusCode(StatusCodes.Bad_ResourceUnavailable), fault.getStatusCode());
    assertEquals(OFFLINE_DIAGNOSTIC, fault.getDiagnostic().orElseThrow());
    assertTrue(fault.getMessage().contains("E_OFFLINE"), fault.getMessage());
  }

  // Part 4 §7.32: only the requested fields are returned, at service level as elsewhere.
  @Test
  void faultCarriesOnlyTheRequestedServiceLevelFields() throws Exception {
    UaServiceFaultException fault = fault(read(0x002, OFFLINE));

    assertEquals(
        Diagnostic.text("The device gateway is offline"), fault.getDiagnostic().orElseThrow());
  }

  // Without service-level bits the fault is the one the transport has always built: no
  // diagnostics and no string table. Operation-level bits do not authorize service diagnostics.
  @ParameterizedTest
  @ValueSource(ints = {0, 0x3E0})
  void faultWithoutServiceLevelBitsHasNoDiagnostics(int mask) throws Exception {
    UaServiceFaultException fault = fault(read(mask, OFFLINE));

    assertEquals(new StatusCode(StatusCodes.Bad_ResourceUnavailable), fault.getStatusCode());
    assertTrue(fault.getDiagnostic().isEmpty());
    assertNull(fault.getServiceFault().getResponseHeader().getStringTable());
  }

  // Part 4 §7.12: "The DiagnosticInfo shall not contain any security related information."
  @Test
  void faultForASecurityStatusHasNoDiagnosticUnderTheDefaultPolicy() throws Exception {
    UaServiceFaultException fault = fault(read(SERVICE_LEVEL, DENIED));

    assertEquals(new StatusCode(StatusCodes.Bad_UserAccessDenied), fault.getStatusCode());
    assertTrue(fault.getDiagnostic().isEmpty());
  }

  // Part 2 §6.19: diagnostics are for authenticated clients. A discovery request has no
  // activated Session, so the default policy sends nothing.
  @Test
  void faultWithoutAnActivatedSessionHasNoDiagnosticUnderTheDefaultPolicy() throws Exception {
    UaServiceFaultException fault = fault(getEndpoints(SERVICE_LEVEL));

    assertEquals(new StatusCode(StatusCodes.Bad_ResourceUnavailable), fault.getStatusCode());
    assertTrue(fault.getDiagnostic().isEmpty());
  }

  // Control for the test above: the diagnostic is withheld by the policy, not lost on the way.
  @Test
  void faultWithoutAnActivatedSessionCarriesTheDiagnosticWhenThePolicyAllows() throws Exception {
    policy.set((request, status, diagnostic) -> diagnostic);
    try {
      UaServiceFaultException fault = fault(getEndpoints(SERVICE_LEVEL));

      assertEquals(OFFLINE_DIAGNOSTIC, fault.getDiagnostic().orElseThrow());
    } finally {
      policy.set(DiagnosticsPolicy.defaults());
    }
  }

  // The blocking API wraps the failed future's cause in a new UaException. A caller that catches
  // UaException must still see the server's explanation.
  @Test
  void blockingCallerSeesTheFaultDiagnostic() {
    var readValueId =
        new ReadValueId(OFFLINE, AttributeId.Value.uid(), null, QualifiedName.NULL_VALUE);

    UaException e =
        assertThrows(
            UaException.class,
            () -> client.read(0.0, TimestampsToReturn.Neither, List.of(readValueId)));

    assertEquals(new StatusCode(StatusCodes.Bad_ResourceUnavailable), e.getStatusCode());
    assertEquals(OFFLINE_DIAGNOSTIC, e.getDiagnostic().orElseThrow());
  }

  private UaServiceFaultException fault(UaRequestMessageType request) {
    ExecutionException e =
        assertThrows(
            ExecutionException.class,
            () -> client.sendRequestAsync(request).get(10, TimeUnit.SECONDS));

    return assertInstanceOf(UaServiceFaultException.class, e.getCause());
  }

  private ReadRequest read(int mask, NodeId nodeId) throws Exception {
    return new ReadRequest(
        header(mask),
        0.0,
        TimestampsToReturn.Neither,
        new ReadValueId[] {
          new ReadValueId(nodeId, AttributeId.Value.uid(), null, QualifiedName.NULL_VALUE)
        });
  }

  private GetEndpointsRequest getEndpoints(int mask) throws Exception {
    return new GetEndpointsRequest(
        header(mask),
        server.getConfig().getEndpoints().iterator().next().getEndpointUrl(),
        new String[] {FAIL_LOCALE},
        null);
  }

  private RequestHeader header(int mask) throws Exception {
    OpcUaSession session = client.getSessionAsync().get(10, TimeUnit.SECONDS);
    RequestHeader base = client.newRequestHeader(session.getAuthenticationToken());

    return new RequestHeader(
        base.getAuthenticationToken(),
        base.getTimestamp(),
        base.getRequestHandle(),
        uint(mask),
        base.getAuditEntryId(),
        base.getTimeoutHint(),
        base.getAdditionalHeader());
  }

  /** Fails a Read of {@link #OFFLINE} or {@link #DENIED} as a whole service. */
  private static final class FailingAttributes extends DefaultAttributeServiceSet {

    FailingAttributes(OpcUaServer server) {
      super(server);
    }

    @Override
    public ReadResponse onRead(ServiceRequestContext context, ReadRequest request)
        throws UaException {

      NodeId nodeId = request.getNodesToRead()[0].getNodeId();

      if (OFFLINE.equals(nodeId)) {
        throw new UaException(
            StatusCodes.Bad_ResourceUnavailable, "gateway offline", OFFLINE_DIAGNOSTIC);
      } else if (DENIED.equals(nodeId)) {
        throw new UaException(StatusCodes.Bad_UserAccessDenied, "denied", OFFLINE_DIAGNOSTIC);
      } else {
        return super.onRead(context, request);
      }
    }
  }

  /** Fails a GetEndpoints request that asks for {@link #FAIL_LOCALE}; delegates the rest. */
  private static final class FailingDiscovery implements DiscoveryServiceSet {

    private final DiscoveryServiceSet delegate;

    FailingDiscovery(OpcUaServer server) {
      this.delegate = new DefaultDiscoveryServiceSet(server);
    }

    @Override
    public GetEndpointsResponse onGetEndpoints(
        ServiceRequestContext context, GetEndpointsRequest request) throws UaException {

      String[] localeIds = request.getLocaleIds();

      if (localeIds != null && Arrays.asList(localeIds).contains(FAIL_LOCALE)) {
        throw new UaException(
            StatusCodes.Bad_ResourceUnavailable, "discovery failed", OFFLINE_DIAGNOSTIC);
      } else {
        return delegate.onGetEndpoints(context, request);
      }
    }

    @Override
    public FindServersResponse onFindServers(
        ServiceRequestContext context, FindServersRequest request) throws UaException {

      return delegate.onFindServers(context, request);
    }

    @Override
    public FindServersOnNetworkResponse onFindServersOnNetwork(
        ServiceRequestContext context, FindServersOnNetworkRequest request) throws UaException {

      return delegate.onFindServersOnNetwork(context, request);
    }

    @Override
    public RegisterServerResponse onRegisterServer(
        ServiceRequestContext context, RegisterServerRequest request) throws UaException {

      return delegate.onRegisterServer(context, request);
    }

    @Override
    public RegisterServer2Response onRegisterServer2(
        ServiceRequestContext context, RegisterServer2Request request) throws UaException {

      return delegate.onRegisterServer2(context, request);
    }
  }
}

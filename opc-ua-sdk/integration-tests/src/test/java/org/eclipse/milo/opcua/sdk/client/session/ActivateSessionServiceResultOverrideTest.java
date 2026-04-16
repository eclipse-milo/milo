/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.session;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.server.EndpointConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.test.DelegatingSessionServiceSet;
import org.eclipse.milo.opcua.sdk.test.TestClient;
import org.eclipse.milo.opcua.sdk.test.TestServer;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.ActivateSessionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.ActivateSessionResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ResponseHeader;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;
import org.junit.jupiter.api.Test;

public class ActivateSessionServiceResultOverrideTest {

  @Test
  void initialActivationCapturesAdvisoryCode() throws Exception {
    OpcUaServer server = TestServer.create().getServer();

    var sessionServiceSet =
        new DelegatingSessionServiceSet(server) {
          @Override
          public ActivateSessionResponse onActivateSession(
              ServiceRequestContext context, ActivateSessionRequest request) throws UaException {
            ActivateSessionResponse response = super.onActivateSession(context, request);

            return withServiceResult(
                response, new StatusCode(StatusCodes.Good_PasswordChangeRequired));
          }
        };

    for (EndpointConfig endpoint : server.getConfig().getEndpoints()) {
      server.addServiceSet(endpoint.getPath(), sessionServiceSet);
    }

    server.startup().get();

    OpcUaClient client = TestClient.create(server, cfg -> {});
    try {
      client.connect();

      StatusCode result = client.getSession().getLastActivateSessionServiceResult().orElseThrow();

      assertEquals(StatusCodes.Good_PasswordChangeRequired, result.getValue());
    } finally {
      try {
        client.disconnectAsync().get(2, TimeUnit.SECONDS);
      } finally {
        server.shutdown().get(2, TimeUnit.SECONDS);
      }
    }
  }

  private static ActivateSessionResponse withServiceResult(
      ActivateSessionResponse response, StatusCode serviceResult) {

    var header = response.getResponseHeader();

    return new ActivateSessionResponse(
        new ResponseHeader(
            header.getTimestamp(),
            header.getRequestHandle(),
            serviceResult,
            header.getServiceDiagnostics(),
            header.getStringTable(),
            header.getAdditionalHeader()),
        response.getServerNonce(),
        response.getResults(),
        response.getDiagnosticInfos());
  }
}

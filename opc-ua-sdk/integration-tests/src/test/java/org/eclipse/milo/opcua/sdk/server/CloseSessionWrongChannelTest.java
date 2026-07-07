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

import static org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_SessionIdInvalid;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.test.TestClient;
import org.eclipse.milo.opcua.sdk.test.TestServer;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.UaResponseMessageType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.CloseSessionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateSessionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateSessionResponse;
import org.junit.jupiter.api.Test;

public class CloseSessionWrongChannelTest {

  @Test
  void closeUnactivatedSessionFromDifferentSecureChannelReturnsBadSessionIdInvalid()
      throws Exception {

    OpcUaServer server = TestServer.create().getServer();
    server.startup().get();

    OpcUaClient ownerClient = TestClient.create(server, cfg -> {});
    OpcUaClient wrongChannelClient = TestClient.create(server, cfg -> {});
    try {
      ownerClient.connect();
      wrongChannelClient.connect();

      CreateSessionResponse createSessionResponse = createSession(ownerClient, "unactivated");
      NodeId authToken = createSessionResponse.getAuthenticationToken();

      assertCloseSessionServiceFault(wrongChannelClient, authToken, Bad_SessionIdInvalid);

      closeSession(ownerClient, authToken);
    } finally {
      try {
        ownerClient.disconnectAsync().get(2, TimeUnit.SECONDS);
        wrongChannelClient.disconnectAsync().get(2, TimeUnit.SECONDS);
      } finally {
        server.shutdown().get(2, TimeUnit.SECONDS);
      }
    }
  }

  @Test
  void closeActivatedSessionFromDifferentSecureChannelReturnsBadSessionIdInvalid()
      throws Exception {

    OpcUaServer server = TestServer.create().getServer();
    server.startup().get();

    OpcUaClient ownerClient = TestClient.create(server, cfg -> {});
    OpcUaClient wrongChannelClient = TestClient.create(server, cfg -> {});
    try {
      ownerClient.connect();
      wrongChannelClient.connect();

      NodeId authToken = ownerClient.getSession().getAuthenticationToken();

      assertCloseSessionServiceFault(wrongChannelClient, authToken, Bad_SessionIdInvalid);
    } finally {
      try {
        ownerClient.disconnectAsync().get(2, TimeUnit.SECONDS);
        wrongChannelClient.disconnectAsync().get(2, TimeUnit.SECONDS);
      } finally {
        server.shutdown().get(2, TimeUnit.SECONDS);
      }
    }
  }

  private static CreateSessionResponse createSession(OpcUaClient client, String sessionName)
      throws Exception {

    UInteger maxResponseMessageSize = client.getConfig().getMaxResponseMessageSize();

    ApplicationDescription clientDescription =
        new ApplicationDescription(
            client.getConfig().getApplicationUri(),
            client.getConfig().getProductUri(),
            client.getConfig().getApplicationName(),
            ApplicationType.Client,
            null,
            null,
            null);

    CreateSessionRequest request =
        new CreateSessionRequest(
            client.newRequestHeader(),
            clientDescription,
            null,
            client.getConfig().getEndpoint().getEndpointUrl(),
            sessionName,
            ByteString.NULL_VALUE,
            ByteString.NULL_VALUE,
            60_000.0,
            maxResponseMessageSize);

    return (CreateSessionResponse) client.getTransport().sendRequestMessage(request).get();
  }

  private static void assertCloseSessionServiceFault(
      OpcUaClient client, NodeId authToken, long expectedStatusCode) {

    ExecutionException exception =
        assertThrows(ExecutionException.class, () -> closeSession(client, authToken));

    Throwable cause = exception.getCause();

    if (cause instanceof UaException uaException) {
      assertEquals(expectedStatusCode, uaException.getStatusCode().getValue());
    } else {
      throw new AssertionError("expected UaException, got " + cause, cause);
    }
  }

  private static UaResponseMessageType closeSession(OpcUaClient client, NodeId authToken)
      throws Exception {

    CloseSessionRequest request = new CloseSessionRequest(client.newRequestHeader(authToken), true);

    return client.getTransport().sendRequestMessage(request).get();
  }
}

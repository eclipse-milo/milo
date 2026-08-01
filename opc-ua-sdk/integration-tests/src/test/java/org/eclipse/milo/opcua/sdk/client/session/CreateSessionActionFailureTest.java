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

import static org.eclipse.milo.opcua.sdk.server.OpcUaServerConfig.USER_TOKEN_POLICY_ANONYMOUS;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.OpcUaClientConfig;
import org.eclipse.milo.opcua.sdk.client.OpcUaClientConfigBuilder;
import org.eclipse.milo.opcua.sdk.client.OpcUaSession;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.transport.TransportProfile;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.junit.jupiter.api.Test;

/**
 * What the Session FSM does when the {@code createSession} that the {@code Creating} entry action
 * launches throws instead of returning a future.
 *
 * <p>The FSM library sets the next state before it runs the matching transition actions, and it
 * catches and logs whatever an action throws. An entry action that throws before it has attached
 * the {@code whenComplete} that fires {@code CreateSessionSuccess}/{@code CreateSessionFailure}
 * therefore leaves the FSM parked in {@code Creating} with no event pending, and no Session state
 * has a timeout to kick it out again — so every caller waiting on {@code openSession()}, {@code
 * getSession()} or {@code closeSession()} waits forever. {@code activateSession} and {@code
 * reactivateSession} are already total for exactly this reason: they wrap their bodies and return a
 * failed future rather than throwing.
 *
 * <p>Pins that {@code createSession} is total the same way, against the two synchronous throws it
 * can produce before the request is ever sent: a user-supplied sessionName {@code Supplier} that
 * throws, and an {@link EndpointDescription} carrying no {@code server} — the shape {@code
 * SessionFsmTest} itself builds — which NPEs on {@code endpoint.getServer().getGatewayServerUri()}.
 */
class CreateSessionActionFailureTest {

  /**
   * How long to give the FSM's executor to run the single {@code Creating} entry action. The wedge
   * this pins is permanent, so a longer wait cannot turn a failure into a pass.
   */
  private static final long TIMEOUT_SECONDS = 10;

  @Test
  void sessionNameSupplierThrows() throws Exception {
    var failure = new IllegalStateException("sessionName Supplier");

    OpcUaClientConfigBuilder config =
        config(
            new ApplicationDescription(
                "urn:eclipse:milo:test:server",
                "urn:eclipse:milo:test:server",
                LocalizedText.english("Eclipse Milo Test Server"),
                ApplicationType.Server,
                null,
                null,
                null));

    config.setSessionName(
        () -> {
          throw failure;
        });

    assertOpenSessionFails(config, failure);
  }

  @Test
  void endpointHasNoServer() throws Exception {
    assertOpenSessionFails(config(null), null);
  }

  /**
   * Opens a Session against a client that cannot get as far as sending the CreateSessionRequest,
   * and asserts that the failure reaches the caller instead of stranding it.
   *
   * @param expectedCause the Throwable the caller must be completed with, or {@code null} to accept
   *     any.
   */
  private static void assertOpenSessionFails(
      OpcUaClientConfigBuilder config, Throwable expectedCause) throws Exception {

    OpcUaClient client = OpcUaClient.create(config.build());
    SessionFsm sessionFsm = client.getSessionFsm();

    try {
      CompletableFuture<OpcUaSession> sessionFuture = sessionFsm.openSession();

      ExecutionException ex =
          assertThrows(
              ExecutionException.class,
              () -> sessionFuture.get(TIMEOUT_SECONDS, TimeUnit.SECONDS),
              "openSession() never completed: createSession() threw out of the Creating entry"
                  + " action, which leaves the FSM parked in Creating with nothing pending");

      if (expectedCause != null) {
        assertSame(expectedCause, ex.getCause());
      }
    } finally {
      // Stop the CreatingWait -> Creating retry loop that a failed CreateSession starts.
      sessionFsm.closeSession();
    }
  }

  private static OpcUaClientConfigBuilder config(ApplicationDescription server) {
    return OpcUaClientConfig.builder()
        .setEndpoint(
            new EndpointDescription(
                "opc.tcp://localhost:12685",
                server,
                null,
                MessageSecurityMode.None,
                SecurityPolicy.None.getUri(),
                new UserTokenPolicy[] {USER_TOKEN_POLICY_ANONYMOUS},
                TransportProfile.TCP_UASC_UABINARY.getUri(),
                null))
        .setApplicationName(LocalizedText.english("Eclipse Milo Test Client"))
        .setApplicationUri("urn:eclipse:milo:test:client");
  }
}

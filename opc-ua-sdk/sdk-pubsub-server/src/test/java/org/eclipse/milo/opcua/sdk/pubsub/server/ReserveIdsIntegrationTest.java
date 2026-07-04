/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.server;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.identity.AnonymousProvider;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpDatagramAddress;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.eclipse.milo.opcua.stack.core.types.structured.CallResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Client-driven ReserveIds rows the WP-X locals cannot reach (T5 §7.1/§7.2): R6 — session close
 * RELEASES the session's reservations, observed deterministically through real sessions — and R7 —
 * exhaustion of the 0x8000-0xFFFF id space answers {@code Bad_ResourceUnavailable} (D15), including
 * the up-front reject of a request larger than the whole 32768-id space.
 *
 * <p>SEAM NOTE (brief §7.1 R6 "assert via registry inspection"): the face's live {@link
 * PubSubIdReservations} registry is private with no accessor, so this class pins release-on-close
 * through the equally deterministic exhaustion gate instead — session A reserves the ENTIRE
 * WriterGroupId space, so session B's one-id request can only ever succeed if A's disconnect
 * released the block; no probabilistic reuse observation is involved. The remaining allocator unit
 * rows (grant disjointness, typing, consumption) are pinned in {@code PubSubIdReservationsTest} and
 * {@code PubSubConfigurationFaceTest}.
 */
class ReserveIdsIntegrationTest {

  private static final long TIMEOUT_SECONDS = 15;

  private static final String CONNECTION = "ri-conn";

  private static SksTestServer testServer;
  private static ServerPubSub serverPubSub;
  private static OpcUaClient client;

  @BeforeAll
  static void startServerAndClient() throws Exception {
    testServer = SksTestServer.create(null);

    serverPubSub =
        ServerPubSub.attach(
            testServer.getServer(),
            config(),
            ServerPubSubOptions.builder().allowRemoteConfiguration(true).build());
    serverPubSub.startup().get(TIMEOUT_SECONDS, TimeUnit.SECONDS);

    client = connect("urn:eclipse:milo:test:reserve-ids-client");
  }

  @AfterAll
  static void stopEverything() throws Exception {
    if (client != null) {
      client.disconnect();
    }
    if (serverPubSub != null) {
      serverPubSub.close();
    }
    if (testServer != null) {
      testServer.close();
    }
  }

  /**
   * R6 (+R7): session A reserves the entire 32768-id WriterGroupId space (typed grant asserted),
   * proving exhaustion against BOTH sessions; after A disconnects, B's one-id request succeeds —
   * impossible without the session-close eviction driving {@code releaseSession} — and, the space
   * being empty again, deterministically receives the first server-assigned id 0x8000.
   */
  @Test
  void sessionCloseReleasesTheSessionsReservations() throws Exception {
    OpcUaClient doomed = connect("urn:eclipse:milo:test:reserve-ids-doomed-client");
    try {
      // A reserves the whole space in one call
      CallMethodResult grant = reserveIds(doomed, ushort(32768), ushort(0));
      assertEquals(StatusCode.GOOD, grant.getStatusCode(), "the full-space grant");

      Variant[] outputs = grant.getOutputArguments();
      assertNotNull(outputs);
      assertEquals(3, outputs.length);
      // the UDP profile's DefaultPublisherId is the typed UInt64 (§6.2.7.1)
      assertInstanceOf(ULong.class, outputs[0].getValue(), "DefaultPublisherId");
      UShort[] writerGroupIds = (UShort[]) outputs[1].getValue();
      assertNotNull(writerGroupIds);
      assertEquals(32768, writerGroupIds.length);
      assertEquals(ushort(0x8000), writerGroupIds[0]);
      assertEquals(ushort(0xFFFF), writerGroupIds[writerGroupIds.length - 1]);

      // R7: the space is exhausted for EVERY session — the owner and a bystander alike
      assertEquals(
          StatusCodes.Bad_ResourceUnavailable,
          reserveIds(doomed, ushort(1), ushort(0)).getStatusCode().getValue(),
          "one more id for the owning session");
      assertEquals(
          StatusCodes.Bad_ResourceUnavailable,
          reserveIds(client, ushort(1), ushort(0)).getStatusCode().getValue(),
          "one id for another session (reservations are cross-session exclusive)");
    } finally {
      doomed.disconnect();
    }

    // eviction is driven by the server's SessionListener — poll until B's request succeeds,
    // which the exhaustion above made impossible without the release
    long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(TIMEOUT_SECONDS);
    while (true) {
      CallMethodResult result = reserveIds(client, ushort(1), ushort(0));
      if (result.getStatusCode().isGood()) {
        UShort[] granted = (UShort[]) result.getOutputArguments()[1].getValue();
        assertNotNull(granted);
        assertEquals(1, granted.length);
        // the whole block was released: the first-free allocator is back at 0x8000
        assertEquals(ushort(0x8000), granted[0], "the released block must be grantable again");
        return;
      }
      assertEquals(
          StatusCodes.Bad_ResourceUnavailable,
          result.getStatusCode().getValue(),
          "only exhaustion may answer while the eviction is still in flight");
      if (System.nanoTime() > deadline) {
        fail("session-close eviction never released the reservations");
      }
      Thread.onSpinWait();
    }
  }

  /**
   * R7's up-front bound: a count that exceeds the whole 32768-id space can never be satisfied and
   * answers {@code Bad_ResourceUnavailable} without sweeping the space (D15 — the code still means
   * id-space exhaustion). Nothing is recorded by the failed call.
   */
  @Test
  void requestBeyondTheIdSpaceIsResourceUnavailable() throws Exception {
    CallMethodResult result = reserveIds(client, ushort(65535), ushort(65535));
    assertEquals(StatusCodes.Bad_ResourceUnavailable, result.getStatusCode().getValue());
  }

  // region fixtures + helpers

  private static CallMethodResult reserveIds(
      OpcUaClient caller, UShort numWriterGroupIds, UShort numDataSetWriterIds) throws UaException {

    CallResponse response =
        caller.call(
            List.of(
                new CallMethodRequest(
                    NodeIds.PublishSubscribe_PubSubConfiguration,
                    NodeIds.PublishSubscribe_PubSubConfiguration_ReserveIds,
                    new Variant[] {
                      new Variant(PubSubIdReservations.PROFILE_UDP_UADP),
                      new Variant(numWriterGroupIds),
                      new Variant(numDataSetWriterIds)
                    })));

    CallMethodResult[] results = response.getResults();
    assertTrue(results != null && results.length == 1);
    return results[0];
  }

  /** One connection with no writer groups: the live configuration excludes no ids. */
  private static PubSubConfig config() throws SocketException {
    return PubSubConfig.builder()
        .connection(
            PubSubConnectionConfig.udp(CONNECTION)
                .publisherId(PublisherId.uint16(ushort(4891)))
                .address(UdpDatagramAddress.unicast("127.0.0.1", freeUdpPort()))
                .discoveryAddress(UdpDatagramAddress.unicast("127.0.0.1", freeUdpPort()))
                .build())
        .build();
  }

  private static OpcUaClient connect(String applicationUri) throws UaException {
    OpcUaClient newClient =
        OpcUaClient.create(
            testServer.getEndpointUrl(),
            endpoints ->
                endpoints.stream()
                    .filter(
                        e -> Objects.equals(e.getSecurityPolicyUri(), SecurityPolicy.None.getUri()))
                    .findFirst(),
            transportConfigBuilder -> {},
            clientConfigBuilder ->
                clientConfigBuilder
                    .setApplicationName(LocalizedText.english("reserve ids test client"))
                    .setApplicationUri(applicationUri)
                    .setIdentityProvider(new AnonymousProvider())
                    .setRequestTimeout(uint(10_000)));

    newClient.connect();

    return newClient;
  }

  /** Pick a currently free UDP port by binding and closing an ephemeral socket. */
  private static int freeUdpPort() throws SocketException {
    try (DatagramSocket socket = new DatagramSocket(0)) {
      return socket.getLocalPort();
    }
  }

  // endregion
}

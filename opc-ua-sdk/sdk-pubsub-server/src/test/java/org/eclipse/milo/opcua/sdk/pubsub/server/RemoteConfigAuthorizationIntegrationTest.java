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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ulong;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.identity.AnonymousProvider;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpDatagramAddress;
import org.eclipse.milo.opcua.sdk.server.RoleMapper;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.identity.Identity;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.eclipse.milo.opcua.stack.core.types.structured.CallResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * The remote-configuration authorization matrix over real channels and sessions, covering the
 * client-driven layer above the mocked-session {@link RemoteConfigAuthorizationTest}:
 *
 * <ul>
 *   <li>A custom authorizer's {@code checkConfigure} code surfaces VERBATIM on every consumer — the
 *       eight file methods, root and per-component Reset, and the Status Enable/Disable pair —
 *       through the real Call service.
 *   <li>The default authorizer's RoleMapper matrix end-to-end — with a mapper granting
 *       ConfigureAdmin the handler actually RUNS (OpenCount moves); with a mapper granting only the
 *       Anonymous well-known role the handler is the denying gate.
 *   <li>Handles are session-scoped — a second real session presenting the owner's handle answers
 *       {@code Bad_InvalidArgument} on all six handle-consuming methods.
 *   <li>Session-close eviction — the write lock and buffered bytes die with the session.
 *   <li>{@code allowRemoteConfiguration=false} keeps the eight methods {@code Bad_NotImplemented}
 *       and {@code diagnosticsEnabled=false} keeps Reset {@code Bad_NotImplemented} over the wire.
 * </ul>
 *
 * <p>The session-less {@code Bad_UserAccessDenied} case is already covered by direct invocation in
 * {@link RemoteConfigAuthorizationTest} and is not duplicated here.
 */
class RemoteConfigAuthorizationIntegrationTest {

  private static final long TIMEOUT_SECONDS = 10;

  private static final String CONNECTION = "az-conn";

  private static SksTestServer testServer;
  private static OpcUaClient client;
  private static FileModelTestClient file;

  private final List<ServerPubSub> attached = new CopyOnWriteArrayList<>();

  @BeforeAll
  static void startServerAndClient() throws Exception {
    testServer = SksTestServer.create(null);
    client = connect(testServer, "urn:eclipse:milo:test:authz-client");
    file = new FileModelTestClient(client);
  }

  @AfterAll
  static void stopEverything() throws Exception {
    if (client != null) {
      client.disconnect();
    }
    if (testServer != null) {
      testServer.close();
    }
  }

  @AfterEach
  void closeAttached() {
    attached.forEach(ServerPubSub::close);
    attached.clear();
  }

  @Test
  void customDenyCodeSurfacesVerbatimOnEveryConsumer() throws Exception {
    // checkConfigure gates ALL of: the eight file methods, ReserveIds and CloseAndUpdate
    // (both in the eight), Reset (root and per-component), and Enable/Disable
    var denyCode = new StatusCode(StatusCodes.Bad_ConfigurationError);

    attach(
        ServerPubSubOptions.builder()
            .exposeInformationModel(true)
            .allowRemoteConfiguration(true)
            .diagnosticsEnabled(true)
            .methodAuthorizer(denyingAuthorizer(denyCode)));

    // the eight file-model methods, well-shaped inputs so authorization is what answers
    for (NodeId methodId : fileMethodNodeIds()) {
      CallMethodResult result =
          call(
              client,
              NodeIds.PublishSubscribe_PubSubConfiguration,
              methodId,
              wellShapedInputs(methodId));
      assertEquals(denyCode, result.getStatusCode(), methodId.toString());
    }

    // root Reset (ns0 i=17421) and a per-component Reset
    assertEquals(
        denyCode,
        call(
                client,
                NodeIds.PublishSubscribe_Diagnostics,
                NodeIds.PublishSubscribe_Diagnostics_Reset)
            .getStatusCode(),
        "root Reset");
    assertEquals(
        denyCode,
        callFragment(CONNECTION + "/Diagnostics", CONNECTION + "/Diagnostics/Reset")
            .getStatusCode(),
        "per-component Reset");

    // the Status Enable/Disable pair (authorization precedes the state rule)
    assertEquals(
        denyCode,
        callFragment(CONNECTION + "/Status", CONNECTION + "/Status/Enable").getStatusCode(),
        "Enable");
    assertEquals(
        denyCode,
        callFragment(CONNECTION + "/Status", CONNECTION + "/Status/Disable").getStatusCode(),
        "Disable");
  }

  @Test
  void roleMapperMatrixEndToEnd() throws Exception {
    // over a real session: the access controller admits the Call (the loaded ns0
    // RolePermissions include the Call bit) and the HANDLER's checkConfigure is the gate.
    // Mapper granting ConfigureAdmin to the anonymous identity: allowed, and the handler
    // demonstrably RAN (OpenCount moved)
    RoleMapper granting =
        identity ->
            identity instanceof Identity.AnonymousIdentity
                ? List.of(NodeIds.WellKnownRole_Anonymous, NodeIds.WellKnownRole_ConfigureAdmin)
                : List.<NodeId>of();

    try (SksTestServer grantingServer = SksTestServer.create(granting)) {
      ServerPubSub serverPubSub = attachTo(grantingServer, defaultOptions());
      try {
        OpcUaClient mappedClient =
            connect(grantingServer, "urn:eclipse:milo:test:authz-mapped-client");
        try {
          var mappedFile = new FileModelTestClient(mappedClient);

          UInteger handle = mappedFile.openOk(ubyte(0x01));
          assertEquals(
              ushort(1),
              mappedFile
                  .readValue(NodeIds.PublishSubscribe_PubSubConfiguration_OpenCount)
                  .getValue()
                  .getValue(),
              "the handler ran: OpenCount moved");
          mappedFile.closeOk(handle);
        } finally {
          mappedClient.disconnect();
        }
      } finally {
        serverPubSub.close();
      }
    }

    // mapper present but the role missing: denied by the handler with Bad_UserAccessDenied
    RoleMapper withholding =
        identity ->
            identity instanceof Identity.AnonymousIdentity
                ? List.of(NodeIds.WellKnownRole_Anonymous)
                : List.<NodeId>of();

    try (SksTestServer withholdingServer = SksTestServer.create(withholding)) {
      ServerPubSub serverPubSub = attachTo(withholdingServer, defaultOptions());
      try {
        OpcUaClient unmappedClient =
            connect(withholdingServer, "urn:eclipse:milo:test:authz-unmapped-client");
        try {
          var unmappedFile = new FileModelTestClient(unmappedClient);
          assertEquals(
              new StatusCode(StatusCodes.Bad_UserAccessDenied),
              unmappedFile.open(ubyte(0x01)).status());
        } finally {
          unmappedClient.disconnect();
        }
      } finally {
        serverPubSub.close();
      }
    }
  }

  @Test
  void foreignSessionHandlesAreInvalidArgumentOnAllSixMethods() throws Exception {
    attach(defaultOptions());

    OpcUaClient other = connect(testServer, "urn:eclipse:milo:test:authz-foreign-client");
    try {
      var otherFile = new FileModelTestClient(other);

      // A owns a read+write handle; B presents it on every handle-consuming method
      UInteger handle = file.openOk(ubyte(0x03));
      try {
        var invalid = new StatusCode(StatusCodes.Bad_InvalidArgument);
        assertEquals(invalid, otherFile.read(handle, 16).status(), "Read");
        assertEquals(
            invalid, otherFile.write(handle, ByteString.of(new byte[] {1})).status(), "Write");
        assertEquals(invalid, otherFile.getPosition(handle).status(), "GetPosition");
        assertEquals(invalid, otherFile.setPosition(handle, ulong(0)).status(), "SetPosition");
        assertEquals(invalid, otherFile.close(handle).status(), "Close");
        assertEquals(
            invalid, otherFile.closeAndUpdate(handle, false, null).status(), "CloseAndUpdate");
      } finally {
        file.closeOk(handle);
      }
    } finally {
      other.disconnect();
    }
  }

  @Test
  void sessionCloseEvictsHandlesAndBufferedWritesDieWithTheSession() throws Exception {
    attach(defaultOptions());

    byte[] baseline = file.readWholeFile();

    // a short-lived session takes the write lock and buffers garbage
    OpcUaClient doomed = connect(testServer, "urn:eclipse:milo:test:authz-doomed-client");
    var doomedFile = new FileModelTestClient(doomed);
    UInteger handle = doomedFile.openOk(ubyte(0x06));
    doomedFile.writeAll(handle, new byte[] {1, 2, 3, 4});
    assertEquals(
        ushort(1),
        file.readValue(NodeIds.PublishSubscribe_PubSubConfiguration_OpenCount)
            .getValue()
            .getValue());

    doomed.disconnect();

    // eviction is driven by the session listener: poll OpenCount back to zero
    long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(TIMEOUT_SECONDS);
    while (true) {
      Object openCount =
          file.readValue(NodeIds.PublishSubscribe_PubSubConfiguration_OpenCount)
              .getValue()
              .getValue();
      if (ushort(0).equals(openCount)) {
        break;
      }
      if (System.nanoTime() > deadline) {
        fail("session-close eviction did not release the handle; OpenCount = " + openCount);
      }
      Thread.onSpinWait();
    }

    // the write lock is released and the buffered bytes died with the session
    UInteger reopened = file.openOk(ubyte(0x06));
    file.closeOk(reopened);
    assertArrayEquals(baseline, file.readWholeFile());
  }

  @Test
  void gatingFlagsKeepTheHandlersNotImplemented() throws Exception {
    // allowRemoteConfiguration=false + diagnosticsEnabled=false
    attach(
        ServerPubSubOptions.builder().exposeInformationModel(true).allowRemoteConfiguration(false));

    for (NodeId methodId : fileMethodNodeIds()) {
      CallMethodResult result =
          call(
              client,
              NodeIds.PublishSubscribe_PubSubConfiguration,
              methodId,
              wellShapedInputs(methodId));
      assertEquals(
          new StatusCode(StatusCodes.Bad_NotImplemented),
          result.getStatusCode(),
          methodId.toString());
    }

    // diagnosticsEnabled=false: the ns0 root Reset keeps the loader default too
    assertEquals(
        new StatusCode(StatusCodes.Bad_NotImplemented),
        call(
                client,
                NodeIds.PublishSubscribe_Diagnostics,
                NodeIds.PublishSubscribe_Diagnostics_Reset)
            .getStatusCode());
  }

  // region fixtures + helpers

  private static List<NodeId> fileMethodNodeIds() {
    return List.of(
        NodeIds.PublishSubscribe_PubSubConfiguration_Open,
        NodeIds.PublishSubscribe_PubSubConfiguration_Close,
        NodeIds.PublishSubscribe_PubSubConfiguration_Read,
        NodeIds.PublishSubscribe_PubSubConfiguration_Write,
        NodeIds.PublishSubscribe_PubSubConfiguration_GetPosition,
        NodeIds.PublishSubscribe_PubSubConfiguration_SetPosition,
        NodeIds.PublishSubscribe_PubSubConfiguration_ReserveIds,
        NodeIds.PublishSubscribe_PubSubConfiguration_CloseAndUpdate);
  }

  /** Correctly-typed inputs per method so the argument checks pass and authorization answers. */
  private static Variant[] wellShapedInputs(NodeId methodNodeId) {
    if (NodeIds.PublishSubscribe_PubSubConfiguration_Open.equals(methodNodeId)) {
      return new Variant[] {new Variant(ubyte(1))};
    } else if (NodeIds.PublishSubscribe_PubSubConfiguration_Close.equals(methodNodeId)) {
      return new Variant[] {new Variant(uint(1))};
    } else if (NodeIds.PublishSubscribe_PubSubConfiguration_Read.equals(methodNodeId)) {
      return new Variant[] {new Variant(uint(1)), new Variant(16)};
    } else if (NodeIds.PublishSubscribe_PubSubConfiguration_Write.equals(methodNodeId)) {
      return new Variant[] {new Variant(uint(1)), new Variant(ByteString.of(new byte[] {1}))};
    } else if (NodeIds.PublishSubscribe_PubSubConfiguration_GetPosition.equals(methodNodeId)) {
      return new Variant[] {new Variant(uint(1))};
    } else if (NodeIds.PublishSubscribe_PubSubConfiguration_SetPosition.equals(methodNodeId)) {
      return new Variant[] {new Variant(uint(1)), new Variant(ulong(0))};
    } else if (NodeIds.PublishSubscribe_PubSubConfiguration_ReserveIds.equals(methodNodeId)) {
      return new Variant[] {
        new Variant(PubSubIdReservations.PROFILE_UDP_UADP),
        new Variant(ushort(1)),
        new Variant(ushort(1))
      };
    } else {
      return new Variant[] {new Variant(uint(1)), new Variant(true), Variant.NULL_VALUE};
    }
  }

  private static PubSubMethodAuthorizer denyingAuthorizer(StatusCode code) {
    return new PubSubMethodAuthorizer() {
      @Override
      public StatusCode checkConfigure(Session session) {
        return code;
      }

      @Override
      public StatusCode checkSksAdmin(Session session) {
        return StatusCode.GOOD;
      }

      @Override
      public StatusCode checkKeyAccess(Session session, String securityGroupId) {
        return StatusCode.GOOD;
      }
    };
  }

  private static CallMethodResult call(
      OpcUaClient caller, NodeId objectId, NodeId methodId, Variant... inputs) throws UaException {

    CallResponse response = caller.call(List.of(new CallMethodRequest(objectId, methodId, inputs)));

    CallMethodResult[] results = response.getResults();
    assertTrue(results != null && results.length == 1);
    return results[0];
  }

  /** Call a fragment-minted method by its deterministic string identifiers. */
  private CallMethodResult callFragment(String objectIdentifier, String methodIdentifier)
      throws UaException {

    UShort idx = testServer.getServer().getServerNamespace().getNamespaceIndex();
    return call(
        client,
        new NodeId(idx, "PubSub/" + objectIdentifier),
        new NodeId(idx, "PubSub/" + methodIdentifier));
  }

  private static ServerPubSubOptions.Builder defaultOptions() {
    return ServerPubSubOptions.builder()
        .exposeInformationModel(true)
        .allowRemoteConfiguration(true);
  }

  private ServerPubSub attach(ServerPubSubOptions.Builder options) throws Exception {
    return attachTo(testServer, options);
  }

  private ServerPubSub attachTo(SksTestServer server, ServerPubSubOptions.Builder options)
      throws Exception {

    ServerPubSub serverPubSub = ServerPubSub.attach(server.getServer(), config(), options.build());
    attached.add(serverPubSub);
    serverPubSub.startup().get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
    return serverPubSub;
  }

  private static PubSubConfig config() throws SocketException {
    return PubSubConfig.builder()
        .connection(
            PubSubConnectionConfig.udp(CONNECTION)
                .publisherId(PublisherId.uint16(ushort(4861)))
                .address(UdpDatagramAddress.unicast("127.0.0.1", freeUdpPort()))
                .discoveryAddress(UdpDatagramAddress.unicast("127.0.0.1", freeUdpPort()))
                .build())
        .build();
  }

  private static OpcUaClient connect(SksTestServer server, String applicationUri)
      throws UaException {

    OpcUaClient newClient =
        OpcUaClient.create(
            server.getEndpointUrl(),
            endpoints ->
                endpoints.stream()
                    .filter(
                        e -> Objects.equals(e.getSecurityPolicyUri(), SecurityPolicy.None.getUri()))
                    .findFirst(),
            transportConfigBuilder -> {},
            clientConfigBuilder ->
                clientConfigBuilder
                    .setApplicationName(LocalizedText.english("authz integration test client"))
                    .setApplicationUri(applicationUri)
                    .setIdentityProvider(new AnonymousProvider())
                    .setRequestTimeout(uint(5_000)));

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

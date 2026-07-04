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
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.identity.AnonymousProvider;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfigFiles;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.SecurityGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpDatagramAddress;
import org.eclipse.milo.opcua.sdk.pubsub.config.WriterGroupConfig;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfiguration2DataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfigurationRefDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfigurationRefMask;
import org.eclipse.milo.opcua.stack.core.types.structured.SecurityGroupDataType;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * CloseAndUpdate bits 11 and 12 over the REAL wire (T5 §6.2 SecurityGroup/PushTarget rows + matrix
 * G bits): bit-11 SecurityGroup references are guarded by {@code checkSksAdmin} — the allow variant
 * actually applies the SecurityGroup operation (observable in the re-read configuration file), the
 * deny variant fails EXACTLY the bit-11 slots while non-SKS references in the same call still apply
 * and the method stays Good; bit-12 PushTarget references answer per-ref {@code
 * Bad_InvalidArgument} for EVERY operation (a modeling gap, never an authorization outcome, never a
 * method-level failure) with surrounding valid references applied in partial mode.
 */
class CloseAndUpdateSecurityRefsTest {

  private static final long TIMEOUT_SECONDS = 10;

  private static final String CONNECTION = "cs-conn";
  private static final String SECURITY_GROUP = "cs-sg-new";

  private static SksTestServer testServer;
  private static OpcUaClient client;
  private static FileModelTestClient file;

  private final List<ServerPubSub> attached = new CopyOnWriteArrayList<>();

  private int basePort;

  @BeforeAll
  static void startServerAndClient() throws Exception {
    testServer = SksTestServer.create(null);
    client = connect();
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
  void sksAdminAllowAppliesTheSecurityGroupOperation() throws Exception {
    attach(authorizer(StatusCode.GOOD));

    // Add the SecurityGroup (bit 11, guarded and allowed)
    FileModelTestClient.CallResult add =
        apply(
            fileWithSecurityGroup(),
            false,
            ref(0, 0, 0, Field.ElementAdd, Field.ReferenceSecurityGroup));
    assertEquals(StatusCode.GOOD, add.status());
    assertEquals(StatusCode.GOOD, FileModelTestClient.referencesResults(add)[0]);
    assertTrue(FileModelTestClient.changesApplied(add));

    // the applied group is visible in the re-read configuration file
    SecurityGroupDataType[] groups = currentSecurityGroups();
    assertTrue(
        Arrays.stream(groups).anyMatch(g -> SECURITY_GROUP.equals(g.getName())),
        "expected " + SECURITY_GROUP + " in " + Arrays.toString(groups));

    // removing the UNREFERENCED group succeeds
    FileModelTestClient.CallResult remove =
        apply(
            fileWithSecurityGroup(),
            false,
            ref(0, 0, 0, Field.ElementRemove, Field.ReferenceSecurityGroup));
    assertEquals(StatusCode.GOOD, FileModelTestClient.referencesResults(remove)[0]);
    assertTrue(
        Arrays.stream(currentSecurityGroups()).noneMatch(g -> SECURITY_GROUP.equals(g.getName())));
  }

  @Test
  void sksAdminDenialFailsOnlyTheBit11SlotsOverTheWire() throws Exception {
    ServerPubSub serverPubSub =
        attach(authorizer(new StatusCode(StatusCodes.Bad_UserAccessDenied)));

    // one call: a guarded SecurityGroup Add alongside an unguarded WriterGroup Add
    FileModelTestClient.CallResult result =
        apply(
            fileWithSecurityGroupAndWriterGroup(),
            false,
            ref(0, 0, 0, Field.ElementAdd, Field.ReferenceSecurityGroup),
            ref(0, 0, 0, Field.ElementAdd, Field.ReferenceWriterGroup));

    assertEquals(StatusCode.GOOD, result.status(), "never a method-level failure");
    StatusCode[] referencesResults = FileModelTestClient.referencesResults(result);
    assertEquals(new StatusCode(StatusCodes.Bad_UserAccessDenied), referencesResults[0]);
    assertEquals(StatusCode.GOOD, referencesResults[1]);
    assertTrue(FileModelTestClient.changesApplied(result));

    assertTrue(serverPubSub.runtime().components().writerGroup(CONNECTION, "cs-wg").isPresent());
    assertTrue(
        Arrays.stream(currentSecurityGroups()).noneMatch(g -> SECURITY_GROUP.equals(g.getName())));
  }

  @Test
  void pushTargetRefsAreRejectedPerElementForEveryOperation() throws Exception {
    ServerPubSub serverPubSub = attach(authorizer(StatusCode.GOOD));

    // every operation against bit 12 fails per-ref with Bad_InvalidArgument — never
    // Bad_UserAccessDenied (not an authorization outcome), never a method-level failure —
    // and the surrounding valid reference still applies in partial mode
    FileModelTestClient.CallResult result =
        apply(
            fileWithSecurityGroupAndWriterGroup(),
            false,
            ref(0, 0, 0, Field.ElementAdd, Field.ReferencePushTarget),
            ref(0, 0, 0, Field.ElementMatch, Field.ReferencePushTarget),
            ref(0, 0, 0, Field.ElementModify, Field.ReferencePushTarget),
            ref(0, 0, 0, Field.ElementRemove, Field.ReferencePushTarget),
            ref(0, 0, 0, Field.ElementAdd, Field.ReferenceWriterGroup));

    assertEquals(StatusCode.GOOD, result.status());
    StatusCode[] referencesResults = FileModelTestClient.referencesResults(result);
    assertEquals(5, referencesResults.length);
    for (int i = 0; i < 4; i++) {
      assertEquals(
          new StatusCode(StatusCodes.Bad_InvalidArgument),
          referencesResults[i],
          "push target op slot " + i);
    }
    assertEquals(StatusCode.GOOD, referencesResults[4]);
    assertTrue(FileModelTestClient.changesApplied(result));
    assertTrue(serverPubSub.runtime().components().writerGroup(CONNECTION, "cs-wg").isPresent());
  }

  // region fixtures + helpers

  private static final class Field {
    static final PubSubConfigurationRefMask.Field ElementAdd =
        PubSubConfigurationRefMask.Field.ElementAdd;
    static final PubSubConfigurationRefMask.Field ElementMatch =
        PubSubConfigurationRefMask.Field.ElementMatch;
    static final PubSubConfigurationRefMask.Field ElementModify =
        PubSubConfigurationRefMask.Field.ElementModify;
    static final PubSubConfigurationRefMask.Field ElementRemove =
        PubSubConfigurationRefMask.Field.ElementRemove;
    static final PubSubConfigurationRefMask.Field ReferenceWriterGroup =
        PubSubConfigurationRefMask.Field.ReferenceWriterGroup;
    static final PubSubConfigurationRefMask.Field ReferenceSecurityGroup =
        PubSubConfigurationRefMask.Field.ReferenceSecurityGroup;
    static final PubSubConfigurationRefMask.Field ReferencePushTarget =
        PubSubConfigurationRefMask.Field.ReferencePushTarget;
  }

  private static PubSubConfigurationRefDataType ref(
      int elementIndex,
      int connectionIndex,
      int groupIndex,
      PubSubConfigurationRefMask.Field... fields) {

    return new PubSubConfigurationRefDataType(
        PubSubConfigurationRefMask.of(fields),
        ushort(elementIndex),
        ushort(connectionIndex),
        ushort(groupIndex));
  }

  /**
   * An authorizer allowing configuration and answering {@code sksAdmin} for {@code checkSksAdmin}.
   */
  private static PubSubMethodAuthorizer authorizer(StatusCode sksAdmin) {
    return new PubSubMethodAuthorizer() {
      @Override
      public StatusCode checkConfigure(Session session) {
        return StatusCode.GOOD;
      }

      @Override
      public StatusCode checkSksAdmin(Session session) {
        return sksAdmin;
      }

      @Override
      public StatusCode checkKeyAccess(Session session, String securityGroupId) {
        return StatusCode.GOOD;
      }
    };
  }

  private SecurityGroupDataType[] currentSecurityGroups() throws Exception {
    PubSubConfiguration2DataType current =
        PubSubConfigFiles.decodeDataType(file.readWholeFile(), client.getStaticEncodingContext());
    SecurityGroupDataType[] groups = current.getSecurityGroups();
    return groups != null ? groups : new SecurityGroupDataType[0];
  }

  private FileModelTestClient.CallResult apply(
      PubSubConfig fileConfig,
      boolean requireCompleteUpdate,
      PubSubConfigurationRefDataType... refs)
      throws Exception {

    byte[] bytes =
        PubSubConfigFiles.encodeDataType(
            fileConfig.toDataType(testServer.getServer().getNamespaceTable()),
            client.getStaticEncodingContext());

    UInteger handle = file.openOk(ubyte(0x06));
    file.writeAll(handle, bytes);
    return file.closeAndUpdate(handle, requireCompleteUpdate, refs);
  }

  private PubSubConfig fileWithSecurityGroup() {
    return PubSubConfig.builder()
        .securityGroup(
            SecurityGroupConfig.builder(SECURITY_GROUP).keyLifeTime(Duration.ofHours(1)).build())
        .connection(connection())
        .build();
  }

  private PubSubConfig fileWithSecurityGroupAndWriterGroup() {
    return PubSubConfig.builder()
        .securityGroup(
            SecurityGroupConfig.builder(SECURITY_GROUP).keyLifeTime(Duration.ofHours(1)).build())
        .connection(
            PubSubConnectionConfig.udp(CONNECTION)
                .publisherId(PublisherId.uint16(ushort(4851)))
                .address(UdpDatagramAddress.unicast("127.0.0.1", basePort))
                .writerGroup(
                    WriterGroupConfig.builder("cs-wg")
                        .enabled(false)
                        .writerGroupId(ushort(0x7401))
                        .publishingInterval(Duration.ofMillis(500))
                        .build())
                .build())
        .build();
  }

  private PubSubConnectionConfig connection() {
    return PubSubConnectionConfig.udp(CONNECTION)
        .publisherId(PublisherId.uint16(ushort(4851)))
        .address(UdpDatagramAddress.unicast("127.0.0.1", basePort))
        .build();
  }

  private ServerPubSub attach(@Nullable PubSubMethodAuthorizer authorizer) throws Exception {
    basePort = freeUdpPort();

    ServerPubSubOptions.Builder options =
        ServerPubSubOptions.builder().exposeInformationModel(true).allowRemoteConfiguration(true);
    if (authorizer != null) {
      options.methodAuthorizer(authorizer);
    }

    ServerPubSub serverPubSub =
        ServerPubSub.attach(
            testServer.getServer(),
            PubSubConfig.builder().connection(connection()).build(),
            options.build());
    attached.add(serverPubSub);
    serverPubSub.startup().get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
    return serverPubSub;
  }

  private static OpcUaClient connect() throws UaException {
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
                    .setApplicationName(LocalizedText.english("security refs test client"))
                    .setApplicationUri("urn:eclipse:milo:test:security-refs-client")
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

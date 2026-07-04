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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpDatagramAddress;
import org.eclipse.milo.opcua.sdk.pubsub.config.WriterGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.server.PubSubIdReservations.Grant;
import org.eclipse.milo.opcua.sdk.pubsub.server.PubSubIdReservations.IdKind;
import org.eclipse.milo.opcua.sdk.pubsub.server.PubSubIdReservations.UsedReservation;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.junit.jupiter.api.Test;

/**
 * The ReserveIds registry: grant range and uniqueness (live config + ALL outstanding reservations),
 * consume-on-use, release-on-session-close, the unsupported-profile and id-space-exhaustion codes,
 * and the §6.2.7.1 DefaultPublisherId derivation/typing.
 */
class PubSubIdReservationsTest {

  private static final NodeId SESSION_A = new NodeId(1, "session-a");
  private static final NodeId SESSION_B = new NodeId(1, "session-b");

  private final PubSubIdReservations reservations = new PubSubIdReservations(4840);

  private static PubSubConfig emptyConfig() {
    return PubSubConfig.builder().build();
  }

  /** A config using WriterGroupId 0x8000 and DataSetWriterId 0x8001 (writer-less group). */
  private static PubSubConfig configUsing0x8000() {
    return PubSubConfig.builder()
        .connection(
            PubSubConnectionConfig.udp("conn")
                .publisherId(PublisherId.uint16(ushort(1)))
                .address(UdpDatagramAddress.unicast("127.0.0.1", 14840))
                .writerGroup(
                    WriterGroupConfig.builder("group")
                        .writerGroupId(ushort(0x8000))
                        .publishingInterval(Duration.ofSeconds(1))
                        .build())
                .build())
        .build();
  }

  @Test
  void grantsComeFromTheServerAssignedRangeAndSkipLiveConfigIds() throws Exception {
    Grant grant =
        reservations.reserve(
            SESSION_A, PubSubIdReservations.PROFILE_UDP_UADP, 3, 2, configUsing0x8000());

    assertEquals(3, grant.writerGroupIds().length);
    assertEquals(2, grant.dataSetWriterIds().length);

    for (UShort id : grant.writerGroupIds()) {
      assertTrue(id.intValue() >= 0x8000 && id.intValue() <= 0xFFFF);
      // 0x8000 is used by the live config
      assertTrue(id.intValue() != 0x8000);
    }
    for (UShort id : grant.dataSetWriterIds()) {
      assertTrue(id.intValue() >= 0x8000 && id.intValue() <= 0xFFFF);
    }

    // no duplicates within the grant
    assertEquals(3, Arrays.stream(grant.writerGroupIds()).distinct().count());
  }

  @Test
  void uniquenessSpansAllOutstandingReservationsIncludingOtherSessions() throws Exception {
    Grant first =
        reservations.reserve(SESSION_A, PubSubIdReservations.PROFILE_UDP_UADP, 5, 0, emptyConfig());
    Grant second =
        reservations.reserve(
            SESSION_B, PubSubIdReservations.PROFILE_MQTT_UADP, 5, 0, emptyConfig());

    Set<UShort> firstIds = Set.of(first.writerGroupIds());
    for (UShort id : second.writerGroupIds()) {
      assertFalse(firstIds.contains(id), "id " + id + " granted twice");
    }

    for (UShort id : first.writerGroupIds()) {
      assertTrue(reservations.isReservedBySession(SESSION_A, IdKind.WRITER_GROUP, id));
      assertTrue(reservations.isReservedByOtherSession(SESSION_B, IdKind.WRITER_GROUP, id));
      assertFalse(reservations.isReservedByOtherSession(SESSION_A, IdKind.WRITER_GROUP, id));
    }
  }

  @Test
  void consumeEndsTheReservationAndReleaseSessionDropsAll() throws Exception {
    Grant grant =
        reservations.reserve(SESSION_A, PubSubIdReservations.PROFILE_UDP_UADP, 2, 1, emptyConfig());

    UShort consumedId = grant.writerGroupIds()[0];
    reservations.consume(List.of(new UsedReservation(IdKind.WRITER_GROUP, consumedId)));

    List<UShort> outstanding =
        reservations.outstanding(
            SESSION_A, PubSubIdReservations.PROFILE_UDP_UADP, IdKind.WRITER_GROUP);
    assertEquals(List.of(grant.writerGroupIds()[1]), outstanding);

    reservations.releaseSession(SESSION_A);
    assertTrue(
        reservations
            .outstanding(SESSION_A, PubSubIdReservations.PROFILE_UDP_UADP, IdKind.WRITER_GROUP)
            .isEmpty());
    assertTrue(reservations.reservations().isEmpty());
  }

  @Test
  void unknownProfileIsInvalidArgumentAndNothingIsRecorded() {
    UaException e =
        assertThrows(
            UaException.class,
            () ->
                reservations.reserve(
                    SESSION_A, "http://example.com/not-a-profile", 1, 1, emptyConfig()));

    assertEquals(StatusCodes.Bad_InvalidArgument, e.getStatusCode().getValue());
    assertTrue(reservations.reservations().isEmpty());
  }

  @Test
  void theThreeConfigModelProfilesAreAcceptedRegardlessOfProviderPresence() throws Exception {
    // reservations are id currency, not runnability
    for (String profile :
        List.of(
            PubSubIdReservations.PROFILE_UDP_UADP,
            PubSubIdReservations.PROFILE_MQTT_UADP,
            PubSubIdReservations.PROFILE_MQTT_JSON)) {
      Grant grant = reservations.reserve(SESSION_A, profile, 1, 1, emptyConfig());
      assertEquals(1, grant.writerGroupIds().length);
    }
  }

  @Test
  void exhaustionOfTheIdSpaceIsResourceUnavailableAndAtomic() throws Exception {
    // the whole 0x8000-0xFFFF space per kind is 32768 ids
    Grant all =
        reservations.reserve(
            SESSION_A, PubSubIdReservations.PROFILE_UDP_UADP, 32768, 0, emptyConfig());
    assertEquals(32768, all.writerGroupIds().length);

    UaException e =
        assertThrows(
            UaException.class,
            () ->
                reservations.reserve(
                    SESSION_B, PubSubIdReservations.PROFILE_UDP_UADP, 1, 1, emptyConfig()));
    assertEquals(StatusCodes.Bad_ResourceUnavailable, e.getStatusCode().getValue());

    // the failed call recorded nothing (its DataSetWriter half must not survive)
    assertTrue(
        reservations
            .outstanding(SESSION_B, PubSubIdReservations.PROFILE_UDP_UADP, IdKind.DATA_SET_WRITER)
            .isEmpty());

    // the DataSetWriter id space is independent and still grantable
    Grant writers =
        reservations.reserve(SESSION_B, PubSubIdReservations.PROFILE_UDP_UADP, 0, 4, emptyConfig());
    assertEquals(4, writers.dataSetWriterIds().length);
  }

  @Test
  void allocateUnreservedSkipsReservationsAndExclusions() throws Exception {
    reservations.reserve(SESSION_A, PubSubIdReservations.PROFILE_UDP_UADP, 1, 0, emptyConfig());
    UShort reserved =
        reservations
            .outstanding(SESSION_A, PubSubIdReservations.PROFILE_UDP_UADP, IdKind.WRITER_GROUP)
            .get(0);

    UShort allocated = reservations.allocateUnreserved(IdKind.WRITER_GROUP, Set.of(ushort(0x8001)));
    assertFalse(allocated.equals(reserved));
    assertFalse(allocated.equals(ushort(0x8001)));
  }

  @Test
  void publisherIdDerivationPacksMacAndPort() {
    byte[] mac = {(byte) 0xAA, (byte) 0xBB, (byte) 0xCC, 0x01, 0x02, 0x03};

    ULong derived = PubSubIdReservations.deriveUInt64PublisherId(mac, 4840);

    // first 6 bytes MAC, last 2 bytes port
    assertEquals(ULong.valueOf(0xAABBCC010203_12E8L), derived);

    // stable when no MAC is obtainable (the process-stable fallback)
    assertEquals(
        PubSubIdReservations.deriveUInt64PublisherId(null, 4840),
        PubSubIdReservations.deriveUInt64PublisherId(null, 4840));
  }

  @Test
  void defaultPublisherIdIsTypedPerProfile() throws Exception {
    Object udp = reservations.defaultPublisherId(PubSubIdReservations.PROFILE_UDP_UADP);
    Object mqttUadp = reservations.defaultPublisherId(PubSubIdReservations.PROFILE_MQTT_UADP);
    Object mqttJson = reservations.defaultPublisherId(PubSubIdReservations.PROFILE_MQTT_JSON);

    ULong base = assertInstanceOf(ULong.class, udp);
    assertEquals(base, mqttUadp);
    // the JSON profile serves the decimal String conversion of the same UInt64
    assertEquals(base.toString(), assertInstanceOf(String.class, mqttJson));

    // stable across calls
    assertEquals(udp, reservations.defaultPublisherId(PubSubIdReservations.PROFILE_UDP_UADP));

    UaException e = assertThrows(UaException.class, () -> reservations.defaultPublisherId("bogus"));
    assertEquals(StatusCodes.Bad_InvalidArgument, e.getStatusCode().getValue());
  }
}

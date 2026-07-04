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

import java.net.NetworkInterface;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.WriterGroupConfig;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@code ReserveIds} registry and the server-assigned WriterGroupId/DataSetWriterId allocator
 * (Part 14 §9.1.3.7.5; pinned decision R5), shared with the CloseAndUpdate ElementAdd
 * auto-assignment (R4).
 *
 * <p>Registry semantics (verbatim-anchored):
 *
 * <ul>
 *   <li>Reservations are keyed {@code (Session, TransportProfileUri)}: valid while the Session
 *       lives, usable only through the same Session, consumed when an id is used in the
 *       configuration, and released on session close and face shutdown.
 *   <li>The allocator owns <b>0x8000–0xFFFF</b> per id kind ({@code 0x0001–0x7FFF} is the external
 *       configuration-tool range). Uniqueness for new grants spans the ids used anywhere in the
 *       live configuration <em>plus</em> ALL outstanding reservations, including other sessions'.
 *       Ids freed by component deletion become grantable again.
 *   <li>{@code Bad_ResourceUnavailable} means id-space exhaustion of the 0x8000–0xFFFF range only
 *       (D15) — the allocator imposes NO component-count limit, consistent with the R20
 *       recommendation that the advertised MaxWriterGroups/MaxDataSetWriters stay 0 (no limit).
 *   <li>An unknown or unsupported TransportProfileUri answers {@code Bad_InvalidArgument} (the
 *       documented R5 choice; the spec names no code). The supported set is the three URIs Milo's
 *       configuration model can express — deliberately independent of which transport providers are
 *       on the classpath (D45): reservations are configuration-tool currency; provider presence is
 *       enforced when a configuration is applied. This is also why the set is wider than the
 *       fragment's UDP-only {@code SupportedTransportProfiles} advertisement (blessed asymmetry,
 *       D16).
 * </ul>
 *
 * <p>DefaultPublisherId (Part 14 §6.2.7.1): datagram and UADP-over-broker profiles answer a UInt64
 * whose first 6 bytes are the MAC address of the first suitable network interface and last 2 bytes
 * are the server's first bound endpoint port (a process-stable random 6-byte value stands in when
 * no MAC is obtainable); the JSON-over-broker profile answers the decimal String conversion of that
 * UInt64. The derived value is cached and stable across calls.
 *
 * <p>Thread safety: none of its own — all mutating and reading calls are made while holding the
 * {@link PubSubConfigurationFace} lock.
 */
final class PubSubIdReservations {

  private static final Logger LOGGER = LoggerFactory.getLogger(PubSubIdReservations.class);

  /** The UDP UADP transport profile URI (Part 14 §6.4.1.1). */
  static final String PROFILE_UDP_UADP =
      "http://opcfoundation.org/UA-Profile/Transport/pubsub-udp-uadp";

  /** The MQTT UADP transport profile URI (Part 14 §6.4.2.2). */
  static final String PROFILE_MQTT_UADP =
      "http://opcfoundation.org/UA-Profile/Transport/pubsub-mqtt-uadp";

  /** The MQTT JSON transport profile URI (Part 14 §6.4.2.3). */
  static final String PROFILE_MQTT_JSON =
      "http://opcfoundation.org/UA-Profile/Transport/pubsub-mqtt-json";

  /** The first id of the server-assigned range (§6.2.4.1/§6.2.6.1). */
  static final int MIN_SERVER_ASSIGNED_ID = 0x8000;

  /** The last id of the server-assigned range. */
  static final int MAX_SERVER_ASSIGNED_ID = 0xFFFF;

  /** The default endpoint port used for the PublisherId derivation when none is bound. */
  static final int DEFAULT_PORT = 4840;

  /**
   * The process-stable random 6-byte MAC substitute, generated once: the derived DefaultPublisherId
   * must be stable across calls (and across attachments within one process) even when no hardware
   * address is obtainable.
   */
  private static final byte[] FALLBACK_ADDRESS = randomAddress();

  private final Map<Key, Reserved> reservations = new LinkedHashMap<>();

  private final ULong publisherIdBase;

  /**
   * Create a registry whose UInt64 DefaultPublisherId is derived from the first suitable network
   * interface's MAC address and {@code endpointPort}.
   */
  PubSubIdReservations(int endpointPort) {
    this.publisherIdBase = deriveUInt64PublisherId(firstHardwareAddress(), endpointPort);
  }

  /**
   * Reserve {@code numWriterGroupIds} WriterGroupIds and {@code numDataSetWriterIds}
   * DataSetWriterIds for {@code sessionId} under {@code transportProfileUri}.
   *
   * @param liveConfig the current configuration; its WriterGroupIds and DataSetWriterIds (across
   *     ALL connections and groups) are excluded from the grant.
   * @return the grant, including the typed DefaultPublisherId.
   * @throws UaException {@code Bad_InvalidArgument} for an unsupported profile URI, {@code
   *     Bad_ResourceUnavailable} when the requested count cannot be satisfied from 0x8000–0xFFFF
   *     after exclusions; nothing is recorded on failure.
   */
  Grant reserve(
      NodeId sessionId,
      String transportProfileUri,
      int numWriterGroupIds,
      int numDataSetWriterIds,
      PubSubConfig liveConfig)
      throws UaException {

    Object defaultPublisherId = defaultPublisherId(transportProfileUri);

    List<UShort> writerGroupIds =
        allocate(IdKind.WRITER_GROUP, numWriterGroupIds, liveIds(liveConfig, IdKind.WRITER_GROUP));
    List<UShort> dataSetWriterIds =
        allocate(
            IdKind.DATA_SET_WRITER,
            numDataSetWriterIds,
            // the writer-group grant is not yet recorded and the two kinds have separate id
            // spaces, so it cannot collide with this allocation
            liveIds(liveConfig, IdKind.DATA_SET_WRITER));

    // record only once BOTH allocations succeeded (an exhausted kind must grant nothing)
    Reserved reserved =
        reservations.computeIfAbsent(new Key(sessionId, transportProfileUri), k -> new Reserved());
    reserved.writerGroupIds.addAll(writerGroupIds);
    reserved.dataSetWriterIds.addAll(dataSetWriterIds);

    return new Grant(
        defaultPublisherId,
        writerGroupIds.toArray(new UShort[0]),
        dataSetWriterIds.toArray(new UShort[0]));
  }

  /**
   * The ids of {@code kind} currently reserved by {@code sessionId} under {@code
   * transportProfileUri}, in grant order; a snapshot.
   */
  List<UShort> outstanding(NodeId sessionId, String transportProfileUri, IdKind kind) {
    Reserved reserved = reservations.get(new Key(sessionId, transportProfileUri));
    if (reserved == null) {
      return List.of();
    }
    return List.copyOf(
        kind == IdKind.WRITER_GROUP ? reserved.writerGroupIds : reserved.dataSetWriterIds);
  }

  /** True if {@code id} of {@code kind} is reserved by a session other than {@code sessionId}. */
  boolean isReservedByOtherSession(NodeId sessionId, IdKind kind, UShort id) {
    return reservations.entrySet().stream()
        .anyMatch(
            entry ->
                !entry.getKey().sessionId().equals(sessionId)
                    && entry.getValue().ids(kind).contains(id));
  }

  /** True if {@code id} of {@code kind} is reserved by {@code sessionId} (under any profile). */
  boolean isReservedBySession(NodeId sessionId, IdKind kind, UShort id) {
    return reservations.entrySet().stream()
        .anyMatch(
            entry ->
                entry.getKey().sessionId().equals(sessionId)
                    && entry.getValue().ids(kind).contains(id));
  }

  /**
   * The first free id of {@code kind} from 0x8000–0xFFFF, excluding every outstanding reservation
   * (any session) and {@code excluded}; {@code null} when the space is exhausted. Pure lookup —
   * nothing is recorded.
   */
  @Nullable UShort allocateUnreserved(IdKind kind, Set<UShort> excluded) {
    Set<UShort> reserved = allReserved(kind);
    for (int id = MIN_SERVER_ASSIGNED_ID; id <= MAX_SERVER_ASSIGNED_ID; id++) {
      UShort candidate = UShort.valueOf(id);
      if (!reserved.contains(candidate) && !excluded.contains(candidate)) {
        return candidate;
      }
    }
    return null;
  }

  /**
   * Consume {@code used} reservations: the ids were used in an applied configuration and their
   * reservations end (§9.1.3.7.5 "valid until the ID is used in the configuration"). Ids not
   * currently reserved are ignored.
   */
  void consume(Collection<UsedReservation> used) {
    for (UsedReservation usedReservation : used) {
      for (Reserved reserved : reservations.values()) {
        if (reserved.ids(usedReservation.kind()).remove(usedReservation.id())) {
          break;
        }
      }
    }
    reservations.values().removeIf(Reserved::isEmpty);
  }

  /** Release every reservation of {@code sessionId} (session-close eviction). */
  void releaseSession(NodeId sessionId) {
    reservations.keySet().removeIf(key -> key.sessionId().equals(sessionId));
  }

  /** Release every reservation (face shutdown). */
  void clear() {
    reservations.clear();
  }

  /**
   * An inspection snapshot of the outstanding reservations, keyed by (session, profile); for tests
   * (S17).
   */
  Map<Key, Grant> reservations() {
    var snapshot = new LinkedHashMap<Key, Grant>();
    reservations.forEach(
        (key, reserved) ->
            snapshot.put(
                key,
                new Grant(
                    publisherIdBase,
                    reserved.writerGroupIds.toArray(new UShort[0]),
                    reserved.dataSetWriterIds.toArray(new UShort[0]))));
    return snapshot;
  }

  /**
   * The typed DefaultPublisherId of {@code transportProfileUri} (§6.2.7.1): {@link ULong} for the
   * datagram and UADP-over-broker profiles, decimal {@link String} for the JSON-over-broker
   * profile.
   *
   * @throws UaException {@code Bad_InvalidArgument} for an unsupported profile URI.
   */
  Object defaultPublisherId(String transportProfileUri) throws UaException {
    if (!isSupportedProfile(transportProfileUri)) {
      throw new UaException(
          StatusCodes.Bad_InvalidArgument,
          "unsupported TransportProfileUri: " + transportProfileUri);
    }
    return typedDefaultPublisherId(transportProfileUri, publisherIdBase);
  }

  /** True if {@code transportProfileUri} is one of the three configuration-model profiles. */
  static boolean isSupportedProfile(@Nullable String transportProfileUri) {
    return PROFILE_UDP_UADP.equals(transportProfileUri)
        || PROFILE_MQTT_UADP.equals(transportProfileUri)
        || PROFILE_MQTT_JSON.equals(transportProfileUri);
  }

  /**
   * The §6.2.7.1 typed default for a supported profile: {@code base} itself for UInt64 profiles,
   * its decimal String for the JSON-over-broker profile. Package-private test seam (S17).
   */
  static Object typedDefaultPublisherId(String transportProfileUri, ULong base) {
    return PROFILE_MQTT_JSON.equals(transportProfileUri) ? base.toString() : base;
  }

  /**
   * Derive the recommended §6.2.7.1 UInt64 PublisherId: the first 6 bytes are {@code address} (a
   * MAC address, or the process-stable random substitute when {@code null}), the last 2 bytes are
   * {@code port}. Package-private test seam (S17).
   */
  static ULong deriveUInt64PublisherId(byte @Nullable [] address, int port) {
    byte[] bytes = address != null && address.length >= 6 ? address : FALLBACK_ADDRESS;

    long value = 0;
    for (int i = 0; i < 6; i++) {
      value = (value << 8) | (bytes[i] & 0xFFL);
    }
    value = (value << 16) | (port & 0xFFFFL);

    return ULong.valueOf(value);
  }

  private List<UShort> allocate(IdKind kind, int count, Set<UShort> liveIds) throws UaException {
    var allocated = new ArrayList<UShort>(count);

    var excluded = new HashSet<>(liveIds);
    for (int i = 0; i < count; i++) {
      UShort id = allocateUnreserved(kind, excluded);
      if (id == null) {
        throw new UaException(
            StatusCodes.Bad_ResourceUnavailable,
            "cannot reserve %d %s ids: the 0x8000-0xFFFF id space is exhausted"
                .formatted(count, kind));
      }
      allocated.add(id);
      excluded.add(id);
    }

    return allocated;
  }

  /** The ids of {@code kind} used anywhere in {@code config}, across all connections and groups. */
  static Set<UShort> liveIds(PubSubConfig config, IdKind kind) {
    var ids = new HashSet<UShort>();
    for (PubSubConnectionConfig connection : config.connections()) {
      for (WriterGroupConfig group : connection.writerGroups()) {
        if (kind == IdKind.WRITER_GROUP) {
          ids.add(group.getWriterGroupId());
        } else {
          for (DataSetWriterConfig writer : group.getDataSetWriters()) {
            ids.add(writer.getDataSetWriterId());
          }
        }
      }
    }
    return ids;
  }

  private Set<UShort> allReserved(IdKind kind) {
    var ids = new HashSet<UShort>();
    for (Reserved reserved : reservations.values()) {
      ids.addAll(reserved.ids(kind));
    }
    return ids;
  }

  private static byte @Nullable [] firstHardwareAddress() {
    try {
      Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
      while (interfaces != null && interfaces.hasMoreElements()) {
        NetworkInterface networkInterface = interfaces.nextElement();
        if (networkInterface.isLoopback()) {
          continue;
        }
        byte[] address = networkInterface.getHardwareAddress();
        if (address != null && address.length >= 6) {
          return address;
        }
      }
    } catch (Exception e) {
      LOGGER.debug("Error enumerating network interfaces", e);
    }
    return null;
  }

  private static byte[] randomAddress() {
    byte[] bytes = new byte[6];
    new SecureRandom().nextBytes(bytes);
    return bytes;
  }

  /** The kind of a reservable wire id. */
  enum IdKind {
    WRITER_GROUP,
    DATA_SET_WRITER
  }

  /** A reservation consumed by an applied configuration. */
  record UsedReservation(IdKind kind, UShort id) {}

  /** A {@code ReserveIds} grant, shaped for the Method's Out parameters. */
  record Grant(Object defaultPublisherId, UShort[] writerGroupIds, UShort[] dataSetWriterIds) {}

  /** Registry key: reservations are per-Session, keyed by TransportProfileUri. */
  record Key(NodeId sessionId, String transportProfileUri) {}

  private static final class Reserved {

    private final Set<UShort> writerGroupIds = new LinkedHashSet<>();
    private final Set<UShort> dataSetWriterIds = new LinkedHashSet<>();

    private Set<UShort> ids(IdKind kind) {
      return kind == IdKind.WRITER_GROUP ? writerGroupIds : dataSetWriterIds;
    }

    private boolean isEmpty() {
      return writerGroupIds.isEmpty() && dataSetWriterIds.isEmpty();
    }
  }
}

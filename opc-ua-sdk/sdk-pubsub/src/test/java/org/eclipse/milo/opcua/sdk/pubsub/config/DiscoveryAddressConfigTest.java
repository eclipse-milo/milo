/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.config;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.ConnectionTransportDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DatagramConnectionTransport2DataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DatagramConnectionTransportDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.NetworkAddressUrlDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfiguration2DataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConnectionDataType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Tests for the optional {@code UdpConnectionConfig.discoveryAddress} slot: builder surface,
 * equality, the {@code DatagramConnectionTransportDataType.discoveryAddress} wire mapping in both
 * directions, the canonical EMPTY transport collapse when unset, raw escape hatch precedence, and
 * discovery url parsing (port normalization, multicast detection, validation failures).
 *
 * <p>No sockets are opened by these tests; addresses are config data only and deliberately avoid
 * the spec default {@code 224.0.2.14:4840}.
 */
class DiscoveryAddressConfigTest {

  private static final String PROFILE_UDP_UADP =
      "http://opcfoundation.org/UA-Profile/Transport/pubsub-udp-uadp";

  /** Baseline emission for a UDP connection with no transport settings to express. */
  private static final DatagramConnectionTransportDataType CANONICAL_EMPTY_TRANSPORT =
      new DatagramConnectionTransportDataType(new NetworkAddressUrlDataType(null, null));

  private static NamespaceTable table() {
    return new NamespaceTable();
  }

  private static ExtensionObject encode(UaStructuredType struct) {
    return ExtensionObject.encode(new DefaultEncodingContext(), struct);
  }

  private static UaStructuredType decode(ExtensionObject xo) {
    return xo.decode(new DefaultEncodingContext());
  }

  private static UdpConnectionConfig.Builder connectionBuilder() {
    return UdpConnectionConfig.builder("c")
        .address(UdpDatagramAddress.multicast("239.255.21.1", 15861).networkInterface("lo0"));
  }

  private static PubSubConfig configOf(UdpConnectionConfig connection) {
    return PubSubConfig.builder().connection(connection).build();
  }

  private static UdpConnectionConfig udpConnection(PubSubConfig config) {
    return assertInstanceOf(UdpConnectionConfig.class, config.connection("c").orElseThrow());
  }

  private static ConnectionTransportDataType emittedTransportSettings(PubSubConfig config) {
    return config.toDataType(table()).getConnections()[0].getTransportSettings();
  }

  /** A wire-form UDP connection carrying the given transport settings. */
  private static PubSubConfig fromWire(ConnectionTransportDataType transportSettings) {
    PubSubConnectionDataType connection =
        new PubSubConnectionDataType(
            "c",
            true,
            Variant.ofNull(),
            PROFILE_UDP_UADP,
            new NetworkAddressUrlDataType("lo0", "opc.udp://239.255.21.1:15861"),
            null,
            transportSettings,
            null,
            null);

    PubSubConfiguration2DataType dataType =
        new PubSubConfiguration2DataType(
            null,
            new PubSubConnectionDataType[] {connection},
            true,
            null,
            null,
            null,
            null,
            null,
            uint(0),
            null);

    return PubSubConfig.fromDataType(dataType, table());
  }

  private static DatagramConnectionTransport2DataType datagram2Transport() {
    return new DatagramConnectionTransport2DataType(
        new NetworkAddressUrlDataType("lo0", "opc.udp://239.255.21.9:15869"),
        uint(5),
        uint(8192),
        "qos-a",
        null);
  }

  // region Builder surface and equality

  @Test
  void discoveryAddressDefaultsToNull() {
    UdpConnectionConfig connection = connectionBuilder().build();

    // The Part 14 default discovery address is applied by the runtime, never materialized here.
    assertNull(connection.getDiscoveryAddress());
  }

  @Test
  void builderSetsDiscoveryAddress() {
    UdpDatagramAddress discoveryAddress =
        UdpDatagramAddress.multicast("239.255.21.2", 15862).networkInterface("lo0");

    UdpConnectionConfig connection = connectionBuilder().discoveryAddress(discoveryAddress).build();

    assertEquals(discoveryAddress, connection.getDiscoveryAddress());
  }

  @Test
  void toBuilderPreservesDiscoveryAddress() {
    UdpConnectionConfig connection =
        connectionBuilder()
            .discoveryAddress(
                UdpDatagramAddress.multicast("239.255.21.2", 15862).networkInterface("lo0"))
            .build();

    UdpConnectionConfig copy = connection.toBuilder().build();

    assertEquals(connection.getDiscoveryAddress(), copy.getDiscoveryAddress());
    assertEquals(connection, copy);
    assertEquals(connection.hashCode(), copy.hashCode());
  }

  @Test
  void toBuilderPreservesUnsetDiscoveryAddress() {
    UdpConnectionConfig connection = connectionBuilder().build();

    UdpConnectionConfig copy = connection.toBuilder().build();

    assertNull(copy.getDiscoveryAddress());
    assertEquals(connection, copy);
    assertEquals(connection.hashCode(), copy.hashCode());
  }

  @Test
  void equalsAndHashCodeIncludeDiscoveryAddress() {
    UdpConnectionConfig with =
        connectionBuilder()
            .discoveryAddress(UdpDatagramAddress.multicast("239.255.21.2", 15862))
            .build();
    UdpConnectionConfig same =
        connectionBuilder()
            .discoveryAddress(UdpDatagramAddress.multicast("239.255.21.2", 15862))
            .build();
    UdpConnectionConfig differentAddress =
        connectionBuilder()
            .discoveryAddress(UdpDatagramAddress.multicast("239.255.21.3", 15862))
            .build();
    UdpConnectionConfig without = connectionBuilder().build();

    assertEquals(with, same);
    assertEquals(with.hashCode(), same.hashCode());
    assertNotEquals(with, differentAddress);
    assertNotEquals(with, without);
    assertNotEquals(without, with);
  }

  // endregion

  // region toDataType emission

  @Test
  void configuredDiscoveryAddressEmitsTypedTransportSettings() {
    UdpConnectionConfig connection =
        connectionBuilder()
            .discoveryAddress(
                UdpDatagramAddress.multicast("239.255.21.2", 15862).networkInterface("lo0"))
            .build();

    ConnectionTransportDataType transportSettings = emittedTransportSettings(configOf(connection));

    assertEquals(
        new DatagramConnectionTransportDataType(
            new NetworkAddressUrlDataType("lo0", "opc.udp://239.255.21.2:15862")),
        transportSettings);
  }

  @Test
  void unsetDiscoveryAddressEmitsCanonicalEmptyTransportSettings() {
    PubSubConfig config = configOf(connectionBuilder().build());

    ConnectionTransportDataType transportSettings = emittedTransportSettings(config);

    assertEquals(CANONICAL_EMPTY_TRANSPORT, transportSettings);
    // Byte-equal to the baseline emission, not merely structurally similar.
    assertEquals(encode(CANONICAL_EMPTY_TRANSPORT), encode(transportSettings));
  }

  // endregion

  // region Round trips

  @Test
  void discoveryAddressSurvivesRoundTrip() {
    UdpConnectionConfig connection =
        connectionBuilder()
            .publisherId(PublisherId.uint16(ushort(7)))
            .discoveryAddress(
                UdpDatagramAddress.multicast("239.255.21.2", 15862).networkInterface("lo0"))
            .build();
    PubSubConfig config = configOf(connection);

    PubSubConfig roundTripped = PubSubConfig.fromDataType(config.toDataType(table()), table());

    UdpConnectionConfig udp = udpConnection(roundTripped);
    assertEquals(connection.getDiscoveryAddress(), udp.getDiscoveryAddress());
    assertNull(udp.rawTransportSettings());
    assertEquals(config, roundTripped);
  }

  @Test
  void unicastDiscoveryAddressSurvivesRoundTrip() {
    UdpConnectionConfig connection =
        connectionBuilder()
            .discoveryAddress(UdpDatagramAddress.unicast("127.0.0.1", 15863))
            .build();
    PubSubConfig config = configOf(connection);

    PubSubConfig roundTripped = PubSubConfig.fromDataType(config.toDataType(table()), table());

    UdpDatagramAddress discoveryAddress = udpConnection(roundTripped).getDiscoveryAddress();
    assertNotNull(discoveryAddress);
    assertEquals(UdpDatagramAddress.unicast("127.0.0.1", 15863), discoveryAddress);
    assertEquals(config, roundTripped);
  }

  @Test
  void unsetDiscoveryAddressRoundTripsToUnset() {
    PubSubConfig config = configOf(connectionBuilder().build());

    PubSubConfig roundTripped = PubSubConfig.fromDataType(config.toDataType(table()), table());

    UdpConnectionConfig udp = udpConnection(roundTripped);
    assertNull(udp.getDiscoveryAddress());
    assertNull(udp.rawTransportSettings());
    assertEquals(config, roundTripped);

    // The canonical EMPTY emission is a fixed point of the round trip.
    assertEquals(config.toDataType(table()), roundTripped.toDataType(table()));
  }

  @Test
  void canonicalEmptyTransportCollapsesFromWire() {
    PubSubConfig config =
        fromWire(
            new DatagramConnectionTransportDataType(new NetworkAddressUrlDataType(null, null)));

    UdpConnectionConfig udp = udpConnection(config);
    assertNull(udp.getDiscoveryAddress());
    assertNull(udp.rawTransportSettings());
  }

  // endregion

  // region Raw escape hatch precedence

  @Test
  void rawTransportSettingsWinOverTypedDiscoveryAddress() {
    DatagramConnectionTransport2DataType rawSettings = datagram2Transport();

    UdpConnectionConfig connection =
        connectionBuilder()
            .discoveryAddress(UdpDatagramAddress.multicast("239.255.21.2", 15862))
            .rawTransportSettings(encode(rawSettings))
            .build();
    PubSubConfig config = configOf(connection);

    // The raw escape hatch wins at emission: the typed discovery address never hits the wire.
    assertEquals(rawSettings, emittedTransportSettings(config));

    // The typed discovery address is lost on the round trip; the raw settings survive.
    PubSubConfig roundTripped = PubSubConfig.fromDataType(config.toDataType(table()), table());
    UdpConnectionConfig udp = udpConnection(roundTripped);
    assertNull(udp.getDiscoveryAddress());
    assertNotNull(udp.rawTransportSettings());
    assertEquals(rawSettings, decode(udp.rawTransportSettings()));
    assertNotEquals(config, roundTripped);
  }

  @Test
  void datagram2TransportStaysInRawHatch() {
    // DatagramConnectionTransport2DataType carries fields without config slots; even a discovery
    // url it carries must not map into the typed slot.
    DatagramConnectionTransport2DataType transportSettings = datagram2Transport();

    PubSubConfig config = fromWire(transportSettings);

    UdpConnectionConfig udp = udpConnection(config);
    assertNull(udp.getDiscoveryAddress());
    assertNotNull(udp.rawTransportSettings());
    assertEquals(transportSettings, decode(udp.rawTransportSettings()));

    // ...and the preserved settings are re-emitted unchanged.
    assertEquals(transportSettings, emittedTransportSettings(config));
  }

  @Test
  void datagramTransportWithoutUsableUrlStaysInRawHatch() {
    List<DatagramConnectionTransportDataType> shapes =
        List.of(
            // URL-form address with a network interface but no url.
            new DatagramConnectionTransportDataType(new NetworkAddressUrlDataType("eth0", null)),
            // URL-form address with an empty url.
            new DatagramConnectionTransportDataType(new NetworkAddressUrlDataType(null, "")));

    for (DatagramConnectionTransportDataType transportSettings : shapes) {
      PubSubConfig config = fromWire(transportSettings);

      UdpConnectionConfig udp = udpConnection(config);
      assertNull(udp.getDiscoveryAddress());
      assertNotNull(udp.rawTransportSettings());
      assertEquals(transportSettings, decode(udp.rawTransportSettings()));
    }
  }

  // endregion

  // region Discovery url parsing

  @Test
  void wireDiscoveryUrlMapsToTypedSlot() {
    PubSubConfig config =
        fromWire(
            new DatagramConnectionTransportDataType(
                new NetworkAddressUrlDataType("lo0", "opc.udp://239.255.21.4:15864")));

    UdpConnectionConfig udp = udpConnection(config);
    assertNull(udp.rawTransportSettings());
    assertEquals(
        UdpDatagramAddress.multicast("239.255.21.4", 15864).networkInterface("lo0"),
        udp.getDiscoveryAddress());
  }

  @Test
  void portlessDiscoveryUrlNormalizesTo4840() {
    PubSubConfig config =
        fromWire(
            new DatagramConnectionTransportDataType(
                new NetworkAddressUrlDataType(null, "opc.udp://239.255.21.5")));

    UdpDatagramAddress discoveryAddress = udpConnection(config).getDiscoveryAddress();
    assertNotNull(discoveryAddress);
    assertEquals(4840, discoveryAddress.port());
    assertEquals("opc.udp://239.255.21.5:4840", discoveryAddress.url());

    // The normalized port is what gets re-emitted.
    assertEquals(
        new DatagramConnectionTransportDataType(
            new NetworkAddressUrlDataType(null, "opc.udp://239.255.21.5:4840")),
        emittedTransportSettings(config));
  }

  @ParameterizedTest
  @CsvSource({
    "224.0.2.99, true",
    "239.255.255.250, true",
    "223.255.255.255, false",
    "240.0.0.1, false",
    "192.168.21.5, false",
    "host.example, false",
    "[ff02::1], true",
    "[FF02::1], true",
    "[2001:db8::1], false",
  })
  void multicastDetectionFromDiscoveryUrl(String host, boolean multicast) {
    PubSubConfig config =
        fromWire(
            new DatagramConnectionTransportDataType(
                new NetworkAddressUrlDataType(null, "opc.udp://" + host + ":15865")));

    UdpDatagramAddress discoveryAddress = udpConnection(config).getDiscoveryAddress();
    assertNotNull(discoveryAddress);
    assertEquals(multicast, discoveryAddress.isMulticast());
  }

  @Test
  void unparseableDiscoveryUrlThrowsValidationException() {
    DatagramConnectionTransportDataType invalidSyntax =
        new DatagramConnectionTransportDataType(
            new NetworkAddressUrlDataType(null, "opc.udp://exa mple:15866"));

    PubSubConfigValidationException e1 =
        assertThrows(PubSubConfigValidationException.class, () -> fromWire(invalidSyntax));
    assertTrue(e1.getMessage().contains("discoveryAddress"), e1.getMessage());

    DatagramConnectionTransportDataType noHost =
        new DatagramConnectionTransportDataType(
            new NetworkAddressUrlDataType(null, "opc.udp:///path-only"));

    PubSubConfigValidationException e2 =
        assertThrows(PubSubConfigValidationException.class, () -> fromWire(noHost));
    assertTrue(e2.getMessage().contains("discoveryAddress"), e2.getMessage());
  }

  // endregion
}

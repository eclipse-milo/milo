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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class UdpDatagramAddressTest {

  @Test
  void multicastFactory() {
    UdpDatagramAddress address = UdpDatagramAddress.multicast("224.0.2.14", 4840);

    assertTrue(address.isMulticast());
    assertEquals("224.0.2.14", address.host());
    assertEquals(4840, address.port());
    assertNull(address.networkInterface());
  }

  @Test
  void unicastFactory() {
    UdpDatagramAddress address = UdpDatagramAddress.unicast("127.0.0.1", 4840);

    assertFalse(address.isMulticast());
    assertEquals("127.0.0.1", address.host());
    assertEquals(4840, address.port());
    assertNull(address.networkInterface());
  }

  @Test
  void portRangeBoundsAccepted() {
    assertEquals(1, UdpDatagramAddress.unicast("127.0.0.1", 1).port());
    assertEquals(65535, UdpDatagramAddress.unicast("127.0.0.1", 65535).port());
    assertEquals(1, UdpDatagramAddress.multicast("224.0.2.14", 1).port());
    assertEquals(65535, UdpDatagramAddress.multicast("224.0.2.14", 65535).port());
  }

  @ParameterizedTest
  @ValueSource(ints = {0, -1, 65536})
  void outOfRangePortRejected(int port) {
    assertThrows(
        PubSubConfigValidationException.class, () -> UdpDatagramAddress.unicast("127.0.0.1", port));
    assertThrows(
        PubSubConfigValidationException.class,
        () -> UdpDatagramAddress.multicast("224.0.2.14", port));
  }

  @ParameterizedTest
  @ValueSource(strings = {"", "  "})
  void blankHostRejected(String host) {
    assertThrows(
        PubSubConfigValidationException.class, () -> UdpDatagramAddress.unicast(host, 4840));
    assertThrows(
        PubSubConfigValidationException.class, () -> UdpDatagramAddress.multicast(host, 4840));
  }

  @Test
  void networkInterfaceIsCopyOnWrite() {
    UdpDatagramAddress original = UdpDatagramAddress.multicast("224.0.2.14", 4840);
    UdpDatagramAddress restricted = original.networkInterface("lo0");

    assertNotSame(original, restricted);

    // the original is unchanged
    assertNull(original.networkInterface());

    // the copy carries the restriction and preserves everything else
    assertEquals("lo0", restricted.networkInterface());
    assertEquals(original.host(), restricted.host());
    assertEquals(original.port(), restricted.port());
    assertEquals(original.isMulticast(), restricted.isMulticast());

    assertNotEquals(original, restricted);
  }

  @Test
  void urlFormat() {
    assertEquals(
        "opc.udp://224.0.2.14:4840", UdpDatagramAddress.multicast("224.0.2.14", 4840).url());
    assertEquals("opc.udp://127.0.0.1:12345", UdpDatagramAddress.unicast("127.0.0.1", 12345).url());
  }

  @Test
  void equalsAndHashCode() {
    UdpDatagramAddress address = UdpDatagramAddress.unicast("127.0.0.1", 4840);
    UdpDatagramAddress same = UdpDatagramAddress.unicast("127.0.0.1", 4840);

    assertEquals(address, same);
    assertEquals(address.hashCode(), same.hashCode());

    // inequality on each varied component
    assertNotEquals(address, UdpDatagramAddress.unicast("127.0.0.2", 4840));
    assertNotEquals(address, UdpDatagramAddress.unicast("127.0.0.1", 4841));
    assertNotEquals(address, UdpDatagramAddress.multicast("127.0.0.1", 4840));
    assertNotEquals(address, UdpDatagramAddress.unicast("127.0.0.1", 4840).networkInterface("lo0"));

    assertNotEquals(address, null);
    assertNotEquals(address, "opc.udp://127.0.0.1:4840");
  }

  @Test
  void equalsIncludesNetworkInterface() {
    UdpDatagramAddress viaLo0 =
        UdpDatagramAddress.multicast("224.0.2.14", 4840).networkInterface("lo0");
    UdpDatagramAddress viaLo0Again =
        UdpDatagramAddress.multicast("224.0.2.14", 4840).networkInterface("lo0");
    UdpDatagramAddress viaEn0 =
        UdpDatagramAddress.multicast("224.0.2.14", 4840).networkInterface("en0");

    assertEquals(viaLo0, viaLo0Again);
    assertEquals(viaLo0.hashCode(), viaLo0Again.hashCode());
    assertNotEquals(viaLo0, viaEn0);
  }
}

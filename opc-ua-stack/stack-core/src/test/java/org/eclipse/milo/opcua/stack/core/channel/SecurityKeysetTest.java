/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.channel;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class SecurityKeysetTest {

  @Test
  void defensiveCopyOnConstruction() {
    byte[] clientKey = {0x01, 0x02, 0x03};
    byte[] clientIv = {0x04, 0x05, 0x06};
    byte[] serverKey = {0x07, 0x08, 0x09};
    byte[] serverIv = {0x0A, 0x0B, 0x0C};

    SecurityKeyset keyset =
        new SecurityKeyset(1L, 2L, clientKey, clientIv, serverKey, serverIv, 20);

    // Mutate the original arrays
    clientKey[0] = (byte) 0xFF;
    clientIv[0] = (byte) 0xFF;
    serverKey[0] = (byte) 0xFF;
    serverIv[0] = (byte) 0xFF;

    // Record should retain the original values
    assertEquals((byte) 0x01, keyset.clientEncryptionKey()[0]);
    assertEquals((byte) 0x04, keyset.clientInitializationVector()[0]);
    assertEquals((byte) 0x07, keyset.serverEncryptionKey()[0]);
    assertEquals((byte) 0x0A, keyset.serverInitializationVector()[0]);
  }

  @Test
  void componentValuesRoundTrip() {
    byte[] clientKey = {0x10, 0x20};
    byte[] clientIv = {0x30, 0x40};
    byte[] serverKey = {0x50, 0x60};
    byte[] serverIv = {0x70, (byte) 0x80};

    SecurityKeyset keyset =
        new SecurityKeyset(2265448448L, 1L, clientKey, clientIv, serverKey, serverIv, 32);

    assertEquals(2265448448L, keyset.channelId());
    assertEquals(1L, keyset.tokenId());
    assertArrayEquals(clientKey, keyset.clientEncryptionKey());
    assertArrayEquals(clientIv, keyset.clientInitializationVector());
    assertArrayEquals(serverKey, keyset.serverEncryptionKey());
    assertArrayEquals(serverIv, keyset.serverInitializationVector());
    assertEquals(32, keyset.signatureSize());
  }

  @Test
  void defensiveCopyOnAccessorReturn() {
    byte[] clientKey = {0x01, 0x02, 0x03};
    byte[] clientIv = {0x04, 0x05, 0x06};
    byte[] serverKey = {0x07, 0x08, 0x09};
    byte[] serverIv = {0x0A, 0x0B, 0x0C};

    SecurityKeyset keyset =
        new SecurityKeyset(1L, 2L, clientKey, clientIv, serverKey, serverIv, 20);

    // Mutate the arrays returned by accessors
    keyset.clientEncryptionKey()[0] = (byte) 0xFF;
    keyset.clientInitializationVector()[0] = (byte) 0xFF;
    keyset.serverEncryptionKey()[0] = (byte) 0xFF;
    keyset.serverInitializationVector()[0] = (byte) 0xFF;

    // Record should retain the original values
    assertEquals((byte) 0x01, keyset.clientEncryptionKey()[0]);
    assertEquals((byte) 0x04, keyset.clientInitializationVector()[0]);
    assertEquals((byte) 0x07, keyset.serverEncryptionKey()[0]);
    assertEquals((byte) 0x0A, keyset.serverInitializationVector()[0]);
  }
}

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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eclipse.milo.opcua.stack.core.channel.ChannelSecurity.SecretKeys;
import org.eclipse.milo.opcua.stack.core.channel.ChannelSecurity.SecurityKeys;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.ChannelSecurityToken;
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
    assertArrayEquals(new byte[] {0x01, 0x02, 0x03}, keyset.clientEncryptionKey());
    assertArrayEquals(new byte[] {0x04, 0x05, 0x06}, keyset.clientInitializationVector());
    assertArrayEquals(new byte[] {0x07, 0x08, 0x09}, keyset.serverEncryptionKey());
    assertArrayEquals(new byte[] {0x0A, 0x0B, 0x0C}, keyset.serverInitializationVector());
  }

  // Key-log consumers need directional material and unsigned IDs from the negotiated channel.
  @Test
  void fromPreservesClientAndServerKeyDirections() {
    var channel = new ServerSecureChannel();
    channel.setChannelId(2265448448L);
    channel.setSecurityPolicy(SecurityPolicy.Basic256Sha256);
    channel.setMessageSecurityMode(MessageSecurityMode.SignAndEncrypt);
    var keys =
        new SecurityKeys(
            new SecretKeys(new byte[] {1}, new byte[] {2, 3}, new byte[] {4, 5}),
            new SecretKeys(new byte[] {6}, new byte[] {7, 8}, new byte[] {9, 10}));
    var token =
        new ChannelSecurityToken(
            uint(2265448448L), uint(3000000000L), DateTime.MIN_VALUE, uint(1000));

    SecurityKeyset keyset = SecurityKeyset.from(channel, keys, token);

    assertEquals(2265448448L, keyset.channelId());
    assertEquals(3000000000L, keyset.tokenId());
    assertEquals(32, keyset.signatureSize());
    assertArrayEquals(new byte[] {2, 3}, keyset.clientEncryptionKey());
    assertArrayEquals(new byte[] {4, 5}, keyset.clientInitializationVector());
    assertArrayEquals(new byte[] {7, 8}, keyset.serverEncryptionKey());
    assertArrayEquals(new byte[] {9, 10}, keyset.serverInitializationVector());
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
    assertArrayEquals(new byte[] {0x01, 0x02, 0x03}, keyset.clientEncryptionKey());
    assertArrayEquals(new byte[] {0x04, 0x05, 0x06}, keyset.clientInitializationVector());
    assertArrayEquals(new byte[] {0x07, 0x08, 0x09}, keyset.serverEncryptionKey());
    assertArrayEquals(new byte[] {0x0A, 0x0B, 0x0C}, keyset.serverInitializationVector());
  }
}

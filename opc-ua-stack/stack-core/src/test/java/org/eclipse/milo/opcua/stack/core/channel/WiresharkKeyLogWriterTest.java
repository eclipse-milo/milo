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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class WiresharkKeyLogWriterTest {

  private static final HexFormat HEX = HexFormat.of().withUpperCase();

  @TempDir Path tempDir;

  @Test
  void singleEntryFormat() throws IOException {
    Path keyLogFile = tempDir.resolve("keys.log");

    // Use values that exercise the Wireshark format: large channelId, AES-256 keys
    byte[] clientKey =
        hexToBytes("0102030405060708090A0B0C0D0E0F101112131415161718191A1B1C1D1E1F20");
    byte[] clientIv = hexToBytes("A1A2A3A4A5A6A7A8A9AAABACADAEAF10");
    byte[] serverKey =
        hexToBytes("2122232425262728292A2B2C2D2E2F303132333435363738393A3B3C3D3E3F40");
    byte[] serverIv = hexToBytes("B1B2B3B4B5B6B7B8B9BABBBCBDBEBF10");

    SecurityKeyset keyset =
        new SecurityKeyset(2265448448L, 1L, clientKey, clientIv, serverKey, serverIv, 32);

    var writer = new WiresharkKeyLogWriter(keyLogFile);
    writer.onSecurityKeysCreated(keyset);
    writer.close();

    List<String> lines = Files.readAllLines(keyLogFile);
    assertEquals(6, lines.size());

    String suffix = "2265448448_1";
    assertEquals("client_iv_" + suffix + ": " + HEX.formatHex(clientIv), lines.get(0));
    assertEquals("client_key_" + suffix + ": " + HEX.formatHex(clientKey), lines.get(1));
    assertEquals("client_siglen_" + suffix + ": 32", lines.get(2));
    assertEquals("server_iv_" + suffix + ": " + HEX.formatHex(serverIv), lines.get(3));
    assertEquals("server_key_" + suffix + ": " + HEX.formatHex(serverKey), lines.get(4));
    assertEquals("server_siglen_" + suffix + ": 32", lines.get(5));
  }

  @Test
  void appendMode() throws IOException {
    Path keyLogFile = tempDir.resolve("keys.log");

    byte[] key = {0x01, 0x02};
    byte[] iv = {0x03, 0x04};

    SecurityKeyset keyset1 = new SecurityKeyset(100L, 1L, key, iv, key, iv, 20);
    SecurityKeyset keyset2 = new SecurityKeyset(100L, 2L, key, iv, key, iv, 20);

    var writer = new WiresharkKeyLogWriter(keyLogFile);
    writer.onSecurityKeysCreated(keyset1);
    writer.onSecurityKeysCreated(keyset2);
    writer.close();

    List<String> lines = Files.readAllLines(keyLogFile);
    assertEquals(12, lines.size());

    // First entry uses token 1
    assertTrue(lines.get(0).contains("100_1"));
    // Second entry uses token 2
    assertTrue(lines.get(6).contains("100_2"));
  }

  @Test
  void threadSafetyConcurrentWrites() throws Exception {
    Path keyLogFile = tempDir.resolve("keys.log");

    int numThreads = 20;
    byte[] key = {0x0A, 0x0B, 0x0C, 0x0D};
    byte[] iv = {0x01, 0x02, 0x03, 0x04};

    var writer = new WiresharkKeyLogWriter(keyLogFile);
    ExecutorService executor = Executors.newFixedThreadPool(numThreads);
    var latch = new CountDownLatch(numThreads);

    List<SecurityKeyset> keysets = new ArrayList<>();
    for (int i = 0; i < numThreads; i++) {
      keysets.add(new SecurityKeyset(1L, i + 1L, key, iv, key, iv, 20));
    }

    for (SecurityKeyset keyset : keysets) {
      executor.submit(
          () -> {
            try {
              writer.onSecurityKeysCreated(keyset);
            } finally {
              latch.countDown();
            }
          });
    }

    latch.await();
    executor.shutdown();
    writer.close();

    List<String> lines = Files.readAllLines(keyLogFile);

    // Each keyset produces 6 lines
    assertEquals(numThreads * 6, lines.size());

    // Verify no interleaved lines: every 6th line should start a new entry
    for (int i = 0; i < lines.size(); i++) {
      int posInEntry = i % 6;
      switch (posInEntry) {
        case 0 -> assertTrue(lines.get(i).startsWith("client_iv_"));
        case 1 -> assertTrue(lines.get(i).startsWith("client_key_"));
        case 2 -> assertTrue(lines.get(i).startsWith("client_siglen_"));
        case 3 -> assertTrue(lines.get(i).startsWith("server_iv_"));
        case 4 -> assertTrue(lines.get(i).startsWith("server_key_"));
        case 5 -> assertTrue(lines.get(i).startsWith("server_siglen_"));
      }
    }
  }

  private static byte[] hexToBytes(String hex) {
    return HexFormat.of().parseHex(hex);
  }
}

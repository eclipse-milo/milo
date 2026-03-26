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

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HexFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A {@link SecurityKeysListener} that writes keysets to a file in the Wireshark OPC UA key log
 * format.
 *
 * <p>The output file can be loaded into Wireshark (4.4+) via Edit → Preferences → Protocols → OPC
 * UA → Key log file.
 *
 * <p>This class is thread-safe. Each entry is written as a single synchronized operation.
 *
 * @see SecurityKeyset
 */
public class WiresharkKeyLogWriter implements SecurityKeysListener, Closeable {

  private static final Logger logger = LoggerFactory.getLogger(WiresharkKeyLogWriter.class);

  private static final HexFormat HEX = HexFormat.of().withUpperCase();

  private final BufferedWriter writer;
  private boolean closed = false;

  /**
   * Create a new writer that appends to {@code keyLogFile}.
   *
   * <p>The file is created if it does not exist.
   *
   * @param keyLogFile the path to the key log file.
   * @throws IOException if the file cannot be opened for writing.
   */
  public WiresharkKeyLogWriter(Path keyLogFile) throws IOException {
    this.writer =
        Files.newBufferedWriter(
            keyLogFile,
            StandardCharsets.US_ASCII,
            StandardOpenOption.CREATE,
            StandardOpenOption.APPEND);
  }

  @Override
  public synchronized void onSecurityKeysCreated(SecurityKeyset keyset) {
    if (closed) {
      return;
    }
    try {
      String suffix = keyset.channelId() + "_" + keyset.tokenId();

      writer.write(
          "client_iv_" + suffix + ": " + HEX.formatHex(keyset.clientInitializationVector()));
      writer.newLine();
      writer.write("client_key_" + suffix + ": " + HEX.formatHex(keyset.clientEncryptionKey()));
      writer.newLine();
      writer.write("client_siglen_" + suffix + ": " + keyset.signatureSize());
      writer.newLine();
      writer.write(
          "server_iv_" + suffix + ": " + HEX.formatHex(keyset.serverInitializationVector()));
      writer.newLine();
      writer.write("server_key_" + suffix + ": " + HEX.formatHex(keyset.serverEncryptionKey()));
      writer.newLine();
      writer.write("server_siglen_" + suffix + ": " + keyset.signatureSize());
      writer.newLine();
      writer.flush();
    } catch (IOException e) {
      logger.warn(
          "Failed to write key log entry for channel={}, token={}",
          keyset.channelId(),
          keyset.tokenId(),
          e);
    }
  }

  /**
   * Close the underlying file writer.
   *
   * @throws IOException if an I/O error occurs.
   */
  @Override
  public synchronized void close() throws IOException {
    if (!closed) {
      closed = true;
      writer.close();
    }
  }
}

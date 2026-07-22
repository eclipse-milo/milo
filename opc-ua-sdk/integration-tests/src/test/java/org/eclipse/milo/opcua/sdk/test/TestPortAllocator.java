/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/** Allocates loopback ports that remain reserved for Milo tests for the life of the test JVM. */
public final class TestPortAllocator {

  private static final int MAX_ALLOCATION_ATTEMPTS = 100;
  private static final Path LOCK_DIRECTORY =
      Path.of(System.getProperty("java.io.tmpdir"), "milo-test-port-locks");

  private static final List<PortReservation> RESERVATIONS = new ArrayList<>();

  private TestPortAllocator() {}

  /**
   * Allocate a currently available loopback port.
   *
   * <p>The allocation is coordinated with other Milo test JVMs and remains reserved to this JVM.
   * The caller may bind a server to the returned port immediately.
   *
   * @return an available loopback port.
   * @throws IOException if a port cannot be allocated.
   */
  public static synchronized int allocatePort() throws IOException {
    Files.createDirectories(LOCK_DIRECTORY);

    for (int attempt = 0; attempt < MAX_ALLOCATION_ATTEMPTS; attempt++) {
      try (var socket = new ServerSocket()) {
        socket.setReuseAddress(false);
        socket.bind(new InetSocketAddress(InetAddress.getByName("localhost"), 0));

        int port = socket.getLocalPort();
        FileChannel channel =
            FileChannel.open(
                LOCK_DIRECTORY.resolve(port + ".lock"),
                StandardOpenOption.CREATE,
                StandardOpenOption.WRITE);

        FileLock lock = tryLock(channel);
        if (lock != null) {
          RESERVATIONS.add(new PortReservation(channel, lock));
          return port;
        }

        channel.close();
      }
    }

    throw new IOException(
        "unable to allocate a test port after " + MAX_ALLOCATION_ATTEMPTS + " attempts");
  }

  private static FileLock tryLock(FileChannel channel) throws IOException {
    try {
      return channel.tryLock();
    } catch (OverlappingFileLockException ignored) {
      return null;
    }
  }

  @SuppressWarnings("unused")
  private record PortReservation(FileChannel channel, FileLock lock) {}
}

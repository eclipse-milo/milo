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

/**
 * An immutable snapshot of the symmetric keyset derived during an OpenSecureChannel handshake,
 * suitable for writing to a Wireshark OPC UA key log file.
 *
 * @param channelId the SecureChannelId (unsigned 32-bit, carried as long).
 * @param tokenId the SecurityTokenId (unsigned 32-bit, carried as long).
 * @param clientEncryptionKey the client-to-server AES encryption key.
 * @param clientInitializationVector the client-to-server initialization vector.
 * @param serverEncryptionKey the server-to-client AES encryption key.
 * @param serverInitializationVector the server-to-client initialization vector.
 * @param signatureSize the HMAC output length in bytes (20 for SHA-1, 32 for SHA-256).
 */
public record SecurityKeyset(
    long channelId,
    long tokenId,
    byte[] clientEncryptionKey,
    byte[] clientInitializationVector,
    byte[] serverEncryptionKey,
    byte[] serverInitializationVector,
    int signatureSize) {
  public SecurityKeyset {
    clientEncryptionKey = clientEncryptionKey.clone();
    clientInitializationVector = clientInitializationVector.clone();
    serverEncryptionKey = serverEncryptionKey.clone();
    serverInitializationVector = serverInitializationVector.clone();
  }

  @Override
  public byte[] clientEncryptionKey() {
    return clientEncryptionKey.clone();
  }

  @Override
  public byte[] clientInitializationVector() {
    return clientInitializationVector.clone();
  }

  @Override
  public byte[] serverEncryptionKey() {
    return serverEncryptionKey.clone();
  }

  @Override
  public byte[] serverInitializationVector() {
    return serverInitializationVector.clone();
  }
}

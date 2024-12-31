/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.channel;

public class ChannelParameters {

  private final int localMaxMessageSize;
  private final int localReceiveBufferSize;
  private final int localSendBufferSize;
  private final int localMaxChunkCount;

  private final int remoteMaxMessageSize;
  private final int remoteReceiveBufferSize;
  private final int remoteSendBufferSize;
  private final int remoteMaxChunkCount;

  public ChannelParameters(
      int localMaxMessageSize,
      int localReceiveBufferSize,
      int localSendBufferSize,
      int localMaxChunkCount,
      int remoteMaxMessageSize,
      int remoteReceiveBufferSize,
      int remoteSendBufferSize,
      int remoteMaxChunkCount) {

    this.localMaxMessageSize = localMaxMessageSize;
    this.localReceiveBufferSize = localReceiveBufferSize;
    this.localSendBufferSize = localSendBufferSize;
    this.localMaxChunkCount = localMaxChunkCount;
    this.remoteMaxMessageSize = remoteMaxMessageSize;
    this.remoteReceiveBufferSize = remoteReceiveBufferSize;
    this.remoteSendBufferSize = remoteSendBufferSize;
    this.remoteMaxChunkCount = remoteMaxChunkCount;
  }

  public int getLocalMaxMessageSize() {
    return localMaxMessageSize;
  }

  public int getLocalReceiveBufferSize() {
    return localReceiveBufferSize;
  }

  public int getLocalSendBufferSize() {
    return localSendBufferSize;
  }

  public int getLocalMaxChunkCount() {
    return localMaxChunkCount;
  }

  public int getRemoteMaxMessageSize() {
    return remoteMaxMessageSize;
  }

  public int getRemoteReceiveBufferSize() {
    return remoteReceiveBufferSize;
  }

  public int getRemoteSendBufferSize() {
    return remoteSendBufferSize;
  }

  public int getRemoteMaxChunkCount() {
    return remoteMaxChunkCount;
  }
}

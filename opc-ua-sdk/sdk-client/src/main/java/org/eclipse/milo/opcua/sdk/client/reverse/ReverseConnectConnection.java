/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.reverse;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import java.net.SocketAddress;
import java.time.Instant;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

/**
 * A reverse-connect channel claimed by a selector or explicit candidate claim.
 *
 * <p>Claiming transfers channel ownership out of the manager. The manager keeps immutable
 * observability snapshots, but it will not install the client UASC pipeline or close the claimed
 * channel during normal shutdown. Later transport integration code is expected to attach the normal
 * client pipeline and send {@code Hello} using {@link #endpointUrl()}. {@link
 * org.eclipse.milo.opcua.sdk.client.OpcUaClient#createReverseConnect(org.eclipse.milo.opcua.sdk.client.OpcUaClientConfig,
 * ReverseConnectConnection)} is the SDK entry point for consuming a pre-claimed connection.
 */
public final class ReverseConnectConnection {

  private final Channel channel;
  private final ReverseConnectCandidateSnapshot snapshot;

  ReverseConnectConnection(Channel channel, ReverseConnectCandidateSnapshot snapshot) {
    this.channel = channel;
    this.snapshot = snapshot;
  }

  /**
   * Get the candidate identifier used in manager snapshots and listener callbacks.
   *
   * @return the candidate identifier.
   */
  public UUID candidateId() {
    return snapshot.id();
  }

  /**
   * Get the claimed Netty channel.
   *
   * <p>Ownership of this channel has left the {@link ReverseConnectManager}. Callers that continue
   * the reverse-connect flow are responsible for attaching the normal client transport pipeline,
   * completing or failing the subsequent handshake, and closing the channel when it is no longer
   * usable.
   *
   * @return the claimed channel.
   */
  public Channel channel() {
    return channel;
  }

  /**
   * Get the server application URI from {@code ReverseHello}.
   *
   * <p>The value is a pre-SecureChannel routing hint and must not be treated as authenticated
   * server identity. Normal certificate and endpoint validation still owns server authentication
   * after the claimed channel enters the UASC client pipeline.
   *
   * @return the decoded server URI, or {@code null} if the peer encoded a null value.
   */
  public @Nullable String serverUri() {
    return snapshot.serverUri();
  }

  /**
   * Get the endpoint URL from {@code ReverseHello}.
   *
   * <p>Later transport integration should use this value when sending {@code Hello} on the claimed
   * reverse channel so the normal client handshake echoes the endpoint selected by the server.
   *
   * @return the decoded endpoint URL, or {@code null} if the peer encoded a null value.
   */
  public @Nullable String endpointUrl() {
    return snapshot.endpointUrl();
  }

  /**
   * Get the remote address of the server-opened socket.
   *
   * @return the remote socket address, or {@code null} if Netty does not expose it.
   */
  public @Nullable SocketAddress remoteAddress() {
    return snapshot.remoteAddress();
  }

  /**
   * Get the local listener address for the accepted socket.
   *
   * @return the local socket address, or {@code null} if Netty does not expose it.
   */
  public @Nullable SocketAddress localAddress() {
    return snapshot.localAddress();
  }

  /**
   * Get when the manager accepted the underlying TCP socket.
   *
   * @return the socket acceptance timestamp.
   */
  public Instant acceptedAt() {
    return snapshot.acceptedAt();
  }

  /**
   * Get the immutable candidate snapshot captured when this channel was claimed.
   *
   * <p>This is the stable handoff view. It does not continue to change as the claimed Netty channel
   * proceeds through later UASC and Session work.
   *
   * @return the claim-time candidate snapshot.
   */
  public ReverseConnectCandidateSnapshot snapshot() {
    return snapshot;
  }

  /**
   * Close the claimed Netty channel.
   *
   * @return the close future.
   */
  public ChannelFuture close() {
    return channel.close();
  }
}

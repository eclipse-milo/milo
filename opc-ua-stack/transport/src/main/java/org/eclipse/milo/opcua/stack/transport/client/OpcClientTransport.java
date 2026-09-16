/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.transport.client;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.stack.core.types.UaRequestMessageType;
import org.eclipse.milo.opcua.stack.core.types.UaResponseMessageType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.util.Unit;

public interface OpcClientTransport {

  /**
   * Get the {@link OpcClientTransportConfig} associated with this transport.
   *
   * @return the {@link OpcClientTransportConfig} associated with this transport.
   */
  OpcClientTransportConfig getConfig();

  /**
   * Get the channel thumbprint established by the active SecureChannel.
   *
   * <p>Session code uses this value when SecureChannel-enhancement policies bind
   * CreateSession/ActivateSession signatures to the channel that carried them. Transports without
   * such a binding return {@link ByteString#NULL_VALUE}.
   *
   * @return the active SecureChannel thumbprint, or {@link ByteString#NULL_VALUE}.
   */
  default ByteString getChannelThumbprint() {
    return ByteString.NULL_VALUE;
  }

  /**
   * Connect this transport implementation.
   *
   * @param applicationContext the {@link ClientApplicationContext} associated with this transport.
   * @return a {@link CompletableFuture} that completes successfully when this transport connects,
   *     or completes exceptionally if an error occurred.
   */
  CompletableFuture<Unit> connect(ClientApplicationContext applicationContext);

  /**
   * Disconnect this transport implementation.
   *
   * @return a {@link CompletableFuture} that completes successfully when this transport
   *     disconnects, or completes exceptionally if an error occurred.
   */
  CompletableFuture<Unit> disconnect();

  /**
   * Send a {@link UaRequestMessageType} on this transport implementation.
   *
   * @param requestMessage the {@link UaRequestMessageType} to send.
   * @return a {@link CompletableFuture} that completes successfully with the {@link
   *     UaResponseMessageType} or completes exceptionally if an error occurred.
   */
  CompletableFuture<UaResponseMessageType> sendRequestMessage(UaRequestMessageType requestMessage);

  /**
   * Build a channel-bound request once the channel is available and send it.
   *
   * <p>Transports with a channel binding invoke {@code requestSupplier} only after their channel is
   * established, so security inputs read while building (e.g. {@link #getChannelThumbprint()}) are
   * fresh. The default builds immediately and sends via {@link
   * #sendRequestMessage(UaRequestMessageType)}, retaining that method's request timeout.
   *
   * @param requestSupplier builds the request using the established channel's security inputs.
   * @param channelTimeoutMillis the maximum wait for a channel, or zero for no deadline. Once the
   *     request is built, its header supplies the response timeout.
   * @return the response future.
   */
  default CompletableFuture<UaResponseMessageType> sendRequestMessage(
      Callable<UaRequestMessageType> requestSupplier, long channelTimeoutMillis) {
    try {
      return sendRequestMessage(requestSupplier.call());
    } catch (Exception e) {
      return CompletableFuture.failedFuture(e);
    }
  }
}

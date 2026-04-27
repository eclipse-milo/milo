/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.transport.client.uasc;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.channel.messages.ReverseHelloMessage;
import org.eclipse.milo.opcua.stack.transport.client.ClientApplicationContext;
import org.eclipse.milo.opcua.stack.transport.client.uasc.InboundUascResponseHandler.DelegatingUascResponseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Starts a Reverse Connect client handshake on an already accepted and active client channel. */
public class UascClientReverseConnectHandshake {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * Start the Reverse Connect client handshake.
   *
   * @param channel the active channel accepted from the reverse connection listener.
   * @param reverseHello the pre-decoded ReverseHello sent by the server.
   * @param config the UASC client configuration.
   * @param application the client application context.
   * @param responseHandler the handler for normal UASC responses.
   * @param requestIdSupplier supplier for UASC request IDs.
   * @param allowedServerUris the allowed reverse-connect server URIs.
   * @return a future completed when the SecureChannel is ready or the channel fails/closes.
   */
  public CompletableFuture<ClientSecureChannel> start(
      Channel channel,
      ReverseHelloMessage reverseHello,
      UascClientConfig config,
      ClientApplicationContext application,
      UascResponseHandler responseHandler,
      Supplier<Long> requestIdSupplier,
      Set<String> allowedServerUris) {

    CompletableFuture<ClientSecureChannel> handshakeFuture = new CompletableFuture<>();

    channel
        .closeFuture()
        .addListener(
            f ->
                handshakeFuture.completeExceptionally(
                    new UaException(
                        StatusCodes.Bad_ConnectionClosed,
                        "channel closed during reverse connect handshake")));

    if (!channel.isActive()) {
      handshakeFuture.completeExceptionally(
          new UaException(
              StatusCodes.Bad_ConnectionClosed,
              "channel closed before reverse connect handshake started"));
      return handshakeFuture;
    }

    try {
      channel
          .eventLoop()
          .execute(
              () ->
                  installPipeline(
                      channel,
                      reverseHello,
                      config,
                      application,
                      responseHandler,
                      requestIdSupplier,
                      allowedServerUris,
                      handshakeFuture));
    } catch (Throwable t) {
      failHandshake(channel, handshakeFuture, t);
    }

    return handshakeFuture;
  }

  ChannelHandler newResponseHandler(UascResponseHandler responseHandler) {
    return new DelegatingUascResponseHandler(responseHandler);
  }

  UascClientReverseHelloHandler newReverseHelloHandler(
      UascClientConfig config,
      ClientApplicationContext application,
      Supplier<Long> requestIdSupplier,
      CompletableFuture<ClientSecureChannel> handshakeFuture,
      Set<String> allowedServerUris) {

    // The listener has already decoded ReverseHello; only the Ack timeout is needed.
    return new UascClientReverseHelloHandler(
        config, application, requestIdSupplier, handshakeFuture, allowedServerUris, 0);
  }

  private void installPipeline(
      Channel channel,
      ReverseHelloMessage reverseHello,
      UascClientConfig config,
      ClientApplicationContext application,
      UascResponseHandler responseHandler,
      Supplier<Long> requestIdSupplier,
      Set<String> allowedServerUris,
      CompletableFuture<ClientSecureChannel> handshakeFuture) {

    if (!channel.isActive()) {
      failHandshake(
          channel,
          handshakeFuture,
          new UaException(
              StatusCodes.Bad_ConnectionClosed,
              "channel closed before reverse connect handshake started"));
      return;
    }

    try {
      channel.pipeline().addLast(newResponseHandler(responseHandler));

      UascClientReverseHelloHandler handler =
          newReverseHelloHandler(
              config, application, requestIdSupplier, handshakeFuture, allowedServerUris);

      channel.pipeline().addLast(handler);

      handler.onReverseHello(channel.pipeline().context(handler), reverseHello);
    } catch (Throwable t) {
      logger.error("Failed to start reverse connect handshake", t);

      failHandshake(channel, handshakeFuture, t);
    }
  }

  private void failHandshake(
      Channel channel, CompletableFuture<ClientSecureChannel> handshakeFuture, Throwable failure) {

    handshakeFuture.completeExceptionally(failure);

    if (channel.isOpen()) {
      channel.close();
    }
  }
}

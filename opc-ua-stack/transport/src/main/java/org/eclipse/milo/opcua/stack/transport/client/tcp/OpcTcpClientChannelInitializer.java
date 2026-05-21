/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.transport.client.tcp;

import io.netty.channel.Channel;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import org.eclipse.milo.opcua.stack.transport.client.ClientApplicationContext;
import org.eclipse.milo.opcua.stack.transport.client.uasc.ClientSecureChannel;
import org.eclipse.milo.opcua.stack.transport.client.uasc.InboundUascResponseHandler.DelegatingUascResponseHandler;
import org.eclipse.milo.opcua.stack.transport.client.uasc.UascClientAcknowledgeHandler;
import org.eclipse.milo.opcua.stack.transport.client.uasc.UascResponseHandler;

/**
 * Installs the client-side UA-TCP UASC pipeline on a channel whose socket lifecycle has already
 * been chosen.
 *
 * <p>{@link OpcTcpClientTransport} still owns outbound socket creation and reconnect behavior, but
 * delegates the reusable pipeline wiring here. Future reverse-connect listeners can claim an
 * already-active channel after {@code ReverseHello} validation and attach the same response and
 * acknowledge handlers before SecureChannel setup begins.
 */
public final class OpcTcpClientChannelInitializer {

  private OpcTcpClientChannelInitializer() {}

  /**
   * Initializes a channel created by the normal outbound TCP client path.
   *
   * <p>The {@code Hello} endpoint URL continues to come from {@link
   * ClientApplicationContext#getEndpoint()}, preserving the existing outbound behavior while
   * sharing the same handler installation path as already-connected channels.
   *
   * @param channel the Netty channel created by the outbound TCP bootstrap.
   * @param config the TCP client transport configuration.
   * @param application the client application context used for SecureChannel setup.
   * @param responseHandler the response handler that receives decoded UASC responses.
   * @param requestIdSupplier the supplier used for SecureChannel request ids.
   * @param handshakeFuture the future completed when SecureChannel setup succeeds or fails.
   */
  public static void initializeOutboundChannel(
      Channel channel,
      OpcTcpClientTransportConfig config,
      ClientApplicationContext application,
      UascResponseHandler responseHandler,
      Supplier<Long> requestIdSupplier,
      CompletableFuture<ClientSecureChannel> handshakeFuture) {

    var acknowledgeHandler =
        new UascClientAcknowledgeHandler(config, application, requestIdSupplier, handshakeFuture);

    initializeChannel(channel, config, responseHandler, acknowledgeHandler);
  }

  /**
   * Initializes a channel that is already connected and has been accepted for use by a logical
   * client transport.
   *
   * <p>The supplied endpoint URL is encoded into {@code Hello}. Reverse-connect callers use this
   * after the pre-{@code Hello} {@code ReverseHello} exchange has selected the server endpoint for
   * this channel.
   *
   * @param channel the already-connected Netty channel claimed for client use.
   * @param config the TCP client transport configuration.
   * @param application the client application context used for SecureChannel setup.
   * @param responseHandler the response handler that receives decoded UASC responses.
   * @param requestIdSupplier the supplier used for SecureChannel request ids.
   * @param handshakeFuture the future completed when SecureChannel setup succeeds or fails.
   * @param endpointUrl the endpoint URL to encode into the client {@code Hello} message.
   */
  public static void initializeConnectedChannel(
      Channel channel,
      OpcTcpClientTransportConfig config,
      ClientApplicationContext application,
      UascResponseHandler responseHandler,
      Supplier<Long> requestIdSupplier,
      CompletableFuture<ClientSecureChannel> handshakeFuture,
      String endpointUrl) {

    var acknowledgeHandler =
        new UascClientAcknowledgeHandler(
            config, application, requestIdSupplier, handshakeFuture, () -> endpointUrl);

    initializeChannel(channel, config, responseHandler, acknowledgeHandler);
  }

  private static void initializeChannel(
      Channel channel,
      OpcTcpClientTransportConfig config,
      UascResponseHandler responseHandler,
      UascClientAcknowledgeHandler acknowledgeHandler) {

    channel.pipeline().addLast(new DelegatingUascResponseHandler(responseHandler));
    channel.pipeline().addLast(acknowledgeHandler);

    config.getChannelPipelineCustomizer().accept(channel.pipeline());
  }
}

/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.transport.websocket;

import static io.netty.handler.codec.http.websocketx.WebSocketClientProtocolHandler.ClientHandshakeStateEvent;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import org.eclipse.milo.opcua.stack.transport.client.ClientApplicationContext;
import org.eclipse.milo.opcua.stack.transport.client.uasc.ClientSecureChannel;
import org.eclipse.milo.opcua.stack.transport.client.uasc.UascClientAcknowledgeHandler;
import org.eclipse.milo.opcua.stack.transport.client.uasc.UascClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OpcClientWebSocketBinaryFrameCodec
    extends MessageToMessageCodec<WebSocketFrame, ByteBuf> {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final UascClientConfig config;
  private final ClientApplicationContext application;
  private final Supplier<Long> requestIdSupplier;
  private final CompletableFuture<ClientSecureChannel> handshake;

  public OpcClientWebSocketBinaryFrameCodec(
      UascClientConfig config,
      ClientApplicationContext application,
      Supplier<Long> requestIdSupplier,
      CompletableFuture<ClientSecureChannel> handshake) {

    this.config = config;
    this.application = application;
    this.requestIdSupplier = requestIdSupplier;
    this.handshake = handshake;
  }

  @Override
  public void userEventTriggered(ChannelHandlerContext ctx, Object event) throws Exception {
    if (event instanceof ClientHandshakeStateEvent) {
      logger.debug("WebSocket handshake event: " + event);

      if (event == ClientHandshakeStateEvent.HANDSHAKE_COMPLETE) {
        var acknowledgeHandler =
            new UascClientAcknowledgeHandler(config, application, requestIdSupplier, handshake);

        ctx.pipeline().addLast(acknowledgeHandler);
      }
    }
  }

  @Override
  protected void encode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) {
    out.add(new BinaryWebSocketFrame(msg.retain()));
  }

  @Override
  protected void decode(ChannelHandlerContext ctx, WebSocketFrame msg, List<Object> out) {
    if (msg instanceof BinaryWebSocketFrame) {
      out.add(msg.content().retain());
    } else if (msg instanceof TextWebSocketFrame) {
      TextWebSocketFrame textFrame = (TextWebSocketFrame) msg;

      logger.error("Received WebSocket frame:\n" + textFrame.text());
      ctx.close();
    }
  }
}

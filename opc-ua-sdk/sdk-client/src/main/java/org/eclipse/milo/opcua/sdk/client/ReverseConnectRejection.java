/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.stack.core.channel.messages.ErrorMessage;
import org.eclipse.milo.opcua.stack.core.channel.messages.TcpMessageEncoder;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class ReverseConnectRejection {

  private static final Logger logger = LoggerFactory.getLogger(ReverseConnectRejection.class);

  private ReverseConnectRejection() {}

  static CompletableFuture<Void> sendErrorAndClose(
      Channel channel, long statusCode, @Nullable String reason) {

    if (!channel.isOpen()) {
      return CompletableFuture.completedFuture(null);
    }

    var closeFuture = new CompletableFuture<Void>();

    try {
      ByteBuf messageBuffer = TcpMessageEncoder.encode(new ErrorMessage(statusCode, reason));

      channel
          .writeAndFlush(messageBuffer)
          .addListener(
              writeFuture -> {
                if (!writeFuture.isSuccess()) {
                  logger.debug(
                      "Failed to send reverse connect rejection to {}",
                      channel.remoteAddress(),
                      writeFuture.cause());
                }

                close(channel, closeFuture);
              });

      channel.eventLoop().schedule(() -> close(channel, closeFuture), 2, TimeUnit.SECONDS);
    } catch (Throwable t) {
      close(channel, closeFuture, t);
    }

    return closeFuture;
  }

  private static void close(Channel channel, CompletableFuture<Void> closeFuture) {
    close(channel, closeFuture, null);
  }

  private static void close(
      Channel channel, CompletableFuture<Void> closeFuture, @Nullable Throwable priorFailure) {

    channel
        .close()
        .addListener(
            close -> {
              Throwable failure = priorFailure;
              if (!close.isSuccess()) {
                if (failure != null) {
                  failure.addSuppressed(close.cause());
                } else {
                  failure = close.cause();
                }
              }

              if (failure == null) {
                closeFuture.complete(null);
              } else {
                closeFuture.completeExceptionally(failure);
              }
            });
  }
}

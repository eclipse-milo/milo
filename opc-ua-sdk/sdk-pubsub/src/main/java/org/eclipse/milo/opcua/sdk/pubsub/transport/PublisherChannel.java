/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.transport;

import io.netty.buffer.ByteBuf;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.slf4j.LoggerFactory;

/** An open publisher-side transport channel that sends encoded NetworkMessages. */
public interface PublisherChannel extends AutoCloseable {

  /**
   * Send an encoded NetworkMessage.
   *
   * <p>Implementations must not block the calling thread: the engine invokes send from its shared
   * scheduled executor and while holding internal locks, so a blocking send stalls every engine
   * operation. Initiate the send and complete the returned future asynchronously; transport or
   * broker unavailability must surface through the returned future (failing fast or buffering
   * internally), never by blocking.
   *
   * <p>Ownership of {@code message} transfers to the channel upon invocation: the channel releases
   * the buffer on every path — once the send completes, whether successfully or not, and including
   * when this method throws synchronously.
   *
   * @param message the encoded NetworkMessage to send.
   * @return a {@link CompletableFuture} that completes when the message has been sent, or completes
   *     exceptionally if sending fails.
   */
  CompletableFuture<Void> send(ByteBuf message);

  /**
   * Send an encoded NetworkMessage to {@code address}.
   *
   * <p>The engine always sends through this method, with every address value fully resolved (queue
   * name, delivery guarantee, retain flag, content type). Broker transports override it and apply
   * the address; transports without broker semantics inherit the default, which ignores the address
   * and delegates to {@link #send(ByteBuf)}.
   *
   * <p>Implementations must not block the calling thread: the engine invokes send from its shared
   * scheduled executor and while holding internal locks, so a blocking send stalls every engine
   * operation. Initiate the send and complete the returned future asynchronously; transport or
   * broker unavailability must surface through the returned future (failing fast or buffering
   * internally), never by blocking.
   *
   * <p>Ownership of {@code message} transfers to the channel upon invocation: the channel releases
   * the buffer on every path — once the send completes, whether successfully or not, and including
   * when this method throws synchronously.
   *
   * @param message the encoded NetworkMessage to send.
   * @param address the resolved address of the message.
   * @return a {@link CompletableFuture} that completes when the message has been sent, or completes
   *     exceptionally if sending fails.
   */
  default CompletableFuture<Void> send(ByteBuf message, MessageAddress address) {
    return send(message);
  }

  /**
   * Close this channel and release its transport resources.
   *
   * @return a {@link CompletableFuture} that completes when the channel has closed.
   */
  CompletableFuture<Void> closeAsync();

  /**
   * Close this channel synchronously, waiting up to 5 seconds for {@link #closeAsync()} to
   * complete. Failures are logged, not thrown.
   */
  @Override
  default void close() {
    try {
      closeAsync().get(5, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    } catch (ExecutionException | TimeoutException e) {
      LoggerFactory.getLogger(PublisherChannel.class).warn("Error closing PublisherChannel", e);
    }
  }
}

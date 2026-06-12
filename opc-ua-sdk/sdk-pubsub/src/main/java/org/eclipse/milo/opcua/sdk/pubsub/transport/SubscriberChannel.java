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

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.slf4j.LoggerFactory;

/**
 * An open subscriber-side transport channel.
 *
 * <p>Received messages are pushed to the {@code messageConsumer} supplied in the {@link
 * SubscriberTransportContext} at open time; the channel itself only manages the transport
 * resources.
 */
public interface SubscriberChannel extends AutoCloseable {

  /**
   * Close this channel and release its transport resources. No further messages are delivered to
   * the message consumer after the returned future completes.
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
      LoggerFactory.getLogger(SubscriberChannel.class).warn("Error closing SubscriberChannel", e);
    }
  }
}

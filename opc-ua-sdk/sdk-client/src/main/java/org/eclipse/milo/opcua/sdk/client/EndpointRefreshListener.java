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

import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;

/**
 * Callbacks notified when an {@link OpcUaClient}'s {@link EndpointResolver} refresh adopts a new
 * endpoint or fails.
 *
 * <p>All callbacks run on one sequential executor backed by the transport's executor, in the order
 * refresh completions occur. A listener that throws is logged and does not affect the client or the
 * other listeners. Return quickly: a callback that blocks delays every notification queued after
 * it.
 *
 * <pre>{@code
 * client.addEndpointRefreshListener(new EndpointRefreshListener() {
 *   @Override
 *   public void onEndpointRefreshed(
 *       EndpointConfiguration previous, EndpointConfiguration current) {
 *     // client.getEndpoint() already returns current.endpoint() here.
 *   }
 *
 *   @Override
 *   public void onEndpointRefreshFailed(Throwable failure) {
 *     long status = UaException.extractStatusCode(failure)
 *         .map(StatusCode::value)
 *         .orElse(StatusCodes.Good);
 *
 *     if (status == StatusCodes.Bad_ConfigurationError) {
 *       // Equivalence rejection is permanent; stop retrying and rediscover.
 *       client.disconnectAsync();
 *     }
 *   }
 * });
 * }</pre>
 *
 * @see OpcUaClient#addEndpointRefreshListener(EndpointRefreshListener)
 * @see OpcUaClient#removeEndpointRefreshListener(EndpointRefreshListener)
 * @see OpcUaClient#getEndpoint()
 */
public interface EndpointRefreshListener {

  /**
   * Called when an endpoint refresh adopts a replacement whose selected {@link EndpointDescription}
   * differs from the previous one, such as after a server certificate rotation.
   *
   * <p>When this callback runs, {@link OpcUaClient#getEndpoint()} already returns {@code
   * current.endpoint()}. This callback is not invoked when a refresh succeeds with an unchanged
   * selected endpoint, even if other entries in {@link EndpointConfiguration#discoveryEndpoints()}
   * changed.
   *
   * @param previous the {@link EndpointConfiguration} held before the refresh.
   * @param current the newly adopted {@link EndpointConfiguration}.
   */
  default void onEndpointRefreshed(EndpointConfiguration previous, EndpointConfiguration current) {}

  /**
   * Called when an endpoint refresh fails and the client retains its cached endpoint.
   *
   * <p>This is invoked for synchronous exceptions thrown by {@link EndpointResolver#resolve()},
   * asynchronous failures, {@code null} futures or results, refresh deadline timeouts, and
   * equivalence rejections ({@link StatusCodes#Bad_ConfigurationError}). Wrapper {@link
   * CompletionException} and {@link ExecutionException} instances are unwrapped, and refresh
   * deadline {@link TimeoutException}s are normalized to a {@link UaException} with {@link
   * StatusCodes#Bad_Timeout} so callers can uniformly inspect {@link
   * UaException#extractStatusCode(Throwable)}.
   *
   * <p>This callback is not invoked if the client disconnects before the refresh completes. To stop
   * retrying on a permanent refusal, call {@link OpcUaClient#disconnectAsync()}; do not call the
   * blocking {@link OpcUaClient#disconnect()} from inside this callback.
   *
   * @param failure the cause of the failed refresh.
   */
  default void onEndpointRefreshFailed(Throwable failure) {}
}

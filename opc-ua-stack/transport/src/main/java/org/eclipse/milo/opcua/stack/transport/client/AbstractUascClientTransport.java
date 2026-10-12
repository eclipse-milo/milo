/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.transport.client;

import io.netty.channel.Channel;
import io.netty.util.Timeout;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.UaRequestMessageType;
import org.eclipse.milo.opcua.stack.core.types.UaResponseMessageType;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.RequestHeader;
import org.eclipse.milo.opcua.stack.core.util.ExecutionQueue;
import org.eclipse.milo.opcua.stack.transport.client.uasc.UascRequest;
import org.eclipse.milo.opcua.stack.transport.client.uasc.UascResponseHandler;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractUascClientTransport
    implements OpcClientTransport, UascResponseHandler {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  protected final AtomicLong requestId = new AtomicLong(1L);

  // Every in-flight request, from submission until a response, failure, or timeout completes it.
  // See the package documentation for the channel and request lifecycle this bookkeeping supports.
  private final Map<Long, PendingRequest> pendingRequests = new ConcurrentHashMap<>();

  protected final ExecutionQueue publishResponseQueue;

  protected final OpcClientTransportConfig config;

  public AbstractUascClientTransport(OpcClientTransportConfig config) {
    this.config = config;

    publishResponseQueue = new ExecutionQueue(config.getExecutor());
  }

  protected abstract CompletableFuture<Channel> getChannel();

  @Override
  public CompletableFuture<UaResponseMessageType> sendRequestMessage(
      UaRequestMessageType requestMessage) {

    var request = new UascRequest(requestId.getAndIncrement(), requestMessage);
    PendingRequest pending = addPendingRequest(request);

    getChannel()
        .whenComplete(
            (channel, ex) -> {
              if (ex != null) {
                failPendingRequest(request.getRequestId(), ex);
                return;
              }

              writeRequestIfPending(request, channel);
            });

    return pending.future;
  }

  protected CompletableFuture<UaResponseMessageType> sendRequestMessage(
      UaRequestMessageType requestMessage, Channel channel) {

    var request = new UascRequest(requestId.getAndIncrement(), requestMessage);
    PendingRequest pending = addPendingRequest(request);

    writeRequestIfPending(request, channel);

    return pending.future;
  }

  private PendingRequest addPendingRequest(UascRequest request) {
    long timeoutHint = getTimeoutHint(request);

    var pending = new PendingRequest(timeoutHint > 0);

    pendingRequests.put(request.getRequestId(), pending);
    // Schedule the request timeout up front so a never-arriving channel (e.g., a reverse-connect
    // transport whose server is offline) still fails the future when the timeout hint elapses.
    // Without this, getChannel() can park forever waiting on the next claimed reverse connection.
    scheduleRequestTimeout(pending, request, timeoutHint);

    return pending;
  }

  private static long getTimeoutHint(UascRequest request) {
    RequestHeader requestHeader = request.getRequestMessage().getRequestHeader();

    return requestHeader.getTimeoutHint() != null ? requestHeader.getTimeoutHint().longValue() : 0L;
  }

  private void writeRequestIfPending(UascRequest request, Channel channel) {
    PendingRequest pending = pendingRequests.get(request.getRequestId());

    // Record the carrying channel before the write. A concurrent failPendingOn can observe the
    // request before markWritten and skip it, but the request still terminates: every error path
    // closes the channel, so a write that raced the failure either fails its write promise or is
    // failed by the channel's later channelInactive.
    if (pending != null && pending.markWritten(channel)) {
      writeRequest(request, channel);
    }
  }

  private void writeRequest(UascRequest request, Channel channel) {
    channel
        .writeAndFlush(request)
        .addListener(
            f -> {
              if (!f.isSuccess()) {
                if (failPendingRequest(request.getRequestId(), f.cause())) {
                  logger.debug(
                      "Write failed, request={}, requestHandle={}",
                      request.getRequestMessage().getClass().getSimpleName(),
                      request.getRequestId());
                }
              } else {
                if (logger.isTraceEnabled()) {
                  logger.trace(
                      "Write succeeded, request={}, requestId={}",
                      request.getRequestMessage().getClass().getSimpleName(),
                      request.getRequestId());
                }
              }
            });
  }

  private @Nullable PendingRequest removePendingRequest(long requestId) {
    PendingRequest pending = pendingRequests.get(requestId);

    if (pending == null || !pending.tryComplete()) {
      return null;
    }

    pendingRequests.remove(requestId);
    return pending;
  }

  /**
   * Remove the pending request identified by {@code requestId}, cancel its timeout, and complete
   * its future exceptionally with {@code exception}.
   *
   * @param requestId identifies the pending request to fail.
   * @param exception the exception to complete the request's future with.
   * @return {@code true} if a pending request was found and failed.
   */
  private boolean failPendingRequest(long requestId, Throwable exception) {
    PendingRequest pending = removePendingRequest(requestId);
    if (pending == null) {
      return false;
    }

    execute(() -> pending.future.completeExceptionally(exception));
    return true;
  }

  private void execute(Runnable command) {
    try {
      config.getExecutor().execute(command);
    } catch (RejectedExecutionException e) {
      // Executor is shutting down; run inline so the request still terminates.
      command.run();
    }
  }

  private void scheduleRequestTimeout(
      PendingRequest pending, UascRequest request, long timeoutHint) {
    if (timeoutHint <= 0) {
      return;
    }

    Timeout timeout =
        config
            .getWheelTimer()
            .newTimeout(
                t -> {
                  UaException exception =
                      new UaException(
                          StatusCodes.Bad_Timeout,
                          String.format(
                              "requestId=%s timed out after %sms",
                              request.getRequestId(), timeoutHint));

                  failPendingRequest(request.getRequestId(), exception);
                },
                timeoutHint,
                TimeUnit.MILLISECONDS);

    if (!pending.attachTimeout(timeout)) {
      // The request completed before the timeout could be attached; don't leave it in the wheel.
      timeout.cancel();
    }
  }

  @Override
  public void handleResponse(long requestId, UaResponseMessageType responseMessage) {
    PendingRequest pending = removePendingRequest(requestId);

    if (pending != null) {
      if (responseMessage instanceof PublishResponse) {
        publishResponseQueue.submit(() -> pending.future.complete(responseMessage));
      } else {
        execute(() -> pending.future.complete(responseMessage));
      }
    } else {
      logger.warn("Received response for unknown request, requestId={}", requestId);
    }
  }

  @Override
  public void handleSendFailure(long requestId, UaException exception) {
    if (!failPendingRequest(requestId, exception)) {
      logger.warn("Send failed for unknown request, requestId={}", requestId);
    }
  }

  @Override
  public void handleReceiveFailure(long requestId, UaException exception) {
    if (!failPendingRequest(requestId, exception)) {
      logger.warn("Receive failed for unknown request, requestId={}", requestId);
    }
  }

  @Override
  public void handleChannelError(Channel channel, UaException exception) {
    failPendingOn(channel, exception);
  }

  @Override
  public void handleChannelInactive(Channel channel) {
    failPendingOn(channel, new UaException(StatusCodes.Bad_ConnectionClosed, "connection closed"));
  }

  /**
   * Fail every pending request carried by {@code channel}, plus any unwritten request that has no
   * timeout hint: if the transport's next channel never arrives, nothing else would ever complete
   * it.
   *
   * <p>Unwritten requests with a timeout hint survive so they can be written to the transport's
   * next channel; see the package documentation for the channel and request lifecycle.
   *
   * @param channel the {@link Channel} that failed.
   * @param exception the {@link UaException} to fail the affected requests with.
   */
  private void failPendingOn(Channel channel, UaException exception) {
    pendingRequests.forEach(
        (requestId, pending) -> {
          if (pending.shouldFailOn(channel)) {
            failPendingRequest(requestId, exception);
          }
        });
  }

  /**
   * State for one in-flight request: the future returned to the caller, the timeout backing it, and
   * the channel it was written to, if any. The record guards its own state transitions with
   * synchronized methods; the containing map is only an index, cleaned up by whichever thread wins
   * {@link #tryComplete()}.
   */
  private static class PendingRequest {

    final CompletableFuture<UaResponseMessageType> future = new CompletableFuture<>();

    private final boolean hasTimeout;

    private boolean completed;
    private @Nullable Timeout timeout;
    private @Nullable Channel channel;

    /**
     * @param hasTimeout {@code true} if the request has a timeout hint, i.e. a timeout is or will
     *     shortly be scheduled for it. Recorded at construction so {@link #shouldFailOn} does not
     *     depend on when {@link #attachTimeout} runs.
     */
    PendingRequest(boolean hasTimeout) {
      this.hasTimeout = hasTimeout;
    }

    /**
     * Record {@code channel} as the request's carrying channel.
     *
     * @param channel the {@link Channel} the request is about to be written to.
     * @return {@code true} if the request is still pending and the caller should write it.
     */
    synchronized boolean markWritten(Channel channel) {
      if (completed) {
        return false;
      }
      this.channel = channel;
      return true;
    }

    /**
     * Attach the request's timeout.
     *
     * @param timeout the {@link Timeout} backing this request.
     * @return {@code true} if attached; {@code false} if the request already completed and the
     *     caller should cancel {@code timeout}.
     */
    synchronized boolean attachTimeout(Timeout timeout) {
      if (completed) {
        return false;
      }
      this.timeout = timeout;
      return true;
    }

    /**
     * Attempt the transition to completed, cancelling any attached timeout.
     *
     * @return {@code true} if this caller won the transition and now owns completing the future.
     */
    synchronized boolean tryComplete() {
      if (completed) {
        return false;
      }
      completed = true;
      if (timeout != null) {
        timeout.cancel();
      }
      return true;
    }

    /**
     * Decide whether the failure of {@code channel} fails this request: written requests fail with
     * their carrying channel; unwritten requests survive to be written to the transport's next
     * channel, unless they have no timeout hint, in which case nothing would ever complete them if
     * that next channel never arrives, so they fail now too.
     *
     * @param channel the {@link Channel} that failed.
     * @return {@code true} if this request must fail along with {@code channel}.
     */
    synchronized boolean shouldFailOn(Channel channel) {
      return this.channel == channel || (this.channel == null && !hasTimeout);
    }
  }
}

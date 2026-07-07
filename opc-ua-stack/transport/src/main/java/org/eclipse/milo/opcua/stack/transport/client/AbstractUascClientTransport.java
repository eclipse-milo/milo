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
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractUascClientTransport
    implements OpcClientTransport, UascResponseHandler {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  protected final AtomicLong requestId = new AtomicLong(1L);

  protected final Map<Long, CompletableFuture<UaResponseMessageType>> pendingRequests =
      new ConcurrentHashMap<>();
  protected final Map<Long, Timeout> pendingTimeouts = new ConcurrentHashMap<>();
  private final Map<Long, Object> pendingRequestWriteGates = new ConcurrentHashMap<>();

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
    var responseFuture = new CompletableFuture<UaResponseMessageType>();

    pendingRequests.put(request.getRequestId(), responseFuture);
    pendingRequestWriteGates.put(request.getRequestId(), new Object());
    // Schedule the request timeout up front so a never-arriving channel (e.g., a reverse-connect
    // transport whose server is offline) still fails the future when the timeout hint elapses.
    // Without this, getChannel() can park forever waiting on the next claimed reverse connection.
    scheduleRequestTimeout(request);

    getChannel()
        .whenComplete(
            (channel, ex) -> {
              if (ex != null) {
                CompletableFuture<UaResponseMessageType> pending =
                    removePendingRequest(request.getRequestId());
                if (pending != null) {
                  cancelRequestTimeout(request.getRequestId());
                  pending.completeExceptionally(ex);
                }
                return;
              }

              writeRequestIfPending(request, channel);
            });

    return responseFuture;
  }

  protected CompletableFuture<UaResponseMessageType> sendRequestMessage(
      UaRequestMessageType requestMessage, Channel channel) {

    var request = new UascRequest(requestId.getAndIncrement(), requestMessage);
    var responseFuture = new CompletableFuture<UaResponseMessageType>();

    pendingRequests.put(request.getRequestId(), responseFuture);
    pendingRequestWriteGates.put(request.getRequestId(), new Object());
    scheduleRequestTimeout(request);

    writeRequestIfPending(request, channel);

    return responseFuture;
  }

  private void writeRequestIfPending(UascRequest request, Channel channel) {
    long requestId = request.getRequestId();
    Object writeGate = pendingRequestWriteGates.get(requestId);

    if (writeGate == null) {
      return;
    }

    synchronized (writeGate) {
      try {
        if (pendingRequests.containsKey(requestId)) {
          writeRequest(request, channel);
        }
      } finally {
        pendingRequestWriteGates.remove(requestId, writeGate);
      }
    }
  }

  private void writeRequest(UascRequest request, Channel channel) {
    channel
        .writeAndFlush(request)
        .addListener(
            f -> {
              if (!f.isSuccess()) {
                CompletableFuture<UaResponseMessageType> pending =
                    removePendingRequest(request.getRequestId());
                if (pending != null) {
                  cancelRequestTimeout(request.getRequestId());
                  pending.completeExceptionally(f.cause());

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

  private CompletableFuture<UaResponseMessageType> removePendingRequest(long requestId) {
    Object writeGate = pendingRequestWriteGates.get(requestId);

    if (writeGate != null) {
      synchronized (writeGate) {
        pendingRequestWriteGates.remove(requestId, writeGate);
        return pendingRequests.remove(requestId);
      }
    } else {
      return pendingRequests.remove(requestId);
    }
  }

  private void scheduleRequestTimeout(UascRequest request) {
    RequestHeader requestHeader = request.getRequestMessage().getRequestHeader();

    long timeoutHint =
        requestHeader.getTimeoutHint() != null ? requestHeader.getTimeoutHint().longValue() : 0L;

    if (timeoutHint > 0) {
      Timeout timeout =
          config
              .getWheelTimer()
              .newTimeout(
                  t -> {
                    Timeout removed = pendingTimeouts.remove(request.getRequestId());

                    if (removed != null && !removed.isCancelled()) {
                      CompletableFuture<UaResponseMessageType> future =
                          removePendingRequest(request.getRequestId());

                      if (future != null) {
                        UaException exception =
                            new UaException(
                                StatusCodes.Bad_Timeout,
                                String.format(
                                    "requestId=%s timed out after %sms",
                                    request.getRequestId(), timeoutHint));

                        future.completeExceptionally(exception);
                      }
                    }
                  },
                  timeoutHint,
                  TimeUnit.MILLISECONDS);

      pendingTimeouts.put(request.getRequestId(), timeout);
    }
  }

  protected void cancelRequestTimeout(long requestId) {
    Timeout timeout = pendingTimeouts.remove(requestId);
    if (timeout != null) timeout.cancel();
  }

  @Override
  public void handleResponse(long requestId, UaResponseMessageType responseMessage) {
    CompletableFuture<UaResponseMessageType> responseFuture = removePendingRequest(requestId);

    if (responseFuture != null) {
      cancelRequestTimeout(requestId);

      if (responseMessage instanceof PublishResponse) {
        publishResponseQueue.submit(() -> responseFuture.complete(responseMessage));
      } else {
        config.getExecutor().execute(() -> responseFuture.complete(responseMessage));
      }
    } else {
      logger.warn("Received response for unknown request, requestId={}", requestId);
    }
  }

  @Override
  public void handleSendFailure(long requestId, UaException exception) {
    CompletableFuture<UaResponseMessageType> responseFuture = removePendingRequest(requestId);

    if (responseFuture != null) {
      cancelRequestTimeout(requestId);

      config.getExecutor().execute(() -> responseFuture.completeExceptionally(exception));
    } else {
      logger.warn("Send failed for unknown request, requestId={}", requestId);
    }
  }

  @Override
  public void handleReceiveFailure(long requestId, UaException exception) {
    CompletableFuture<UaResponseMessageType> responseFuture = removePendingRequest(requestId);

    if (responseFuture != null) {
      cancelRequestTimeout(requestId);

      config.getExecutor().execute(() -> responseFuture.completeExceptionally(exception));
    } else {
      logger.warn("Receive failed for unknown request, requestId={}", requestId);
    }
  }

  @Override
  public void handleChannelError(UaException exception) {
    failAndClearPending(exception);
  }

  @Override
  public void handleChannelInactive() {
    failAndClearPending(new UaException(StatusCodes.Bad_ConnectionClosed, "connection closed"));
  }

  private void failAndClearPending(UaException exception) {
    List<Long> requestIds = List.copyOf(pendingRequests.keySet());

    requestIds.forEach(
        requestId -> {
          CompletableFuture<UaResponseMessageType> f = removePendingRequest(requestId);
          if (f == null) {
            return;
          }

          cancelRequestTimeout(requestId);
          config.getExecutor().execute(() -> f.completeExceptionally(exception));
        });

    pendingRequests.clear();
    pendingRequestWriteGates.clear();
  }
}

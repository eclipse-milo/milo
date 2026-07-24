/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.test;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.DataChangeNotification;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemNotification;
import org.eclipse.milo.opcua.stack.core.types.structured.NotificationMessage;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.RepublishRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.RepublishResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ResponseHeader;
import org.eclipse.milo.opcua.stack.core.types.structured.SubscriptionAcknowledgement;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;

/**
 * A {@link DelegatingSubscriptionServiceSet} that gives a test deterministic control over the
 * {@code Publish} and {@code Republish} responses the client observes.
 *
 * <p>The client's {@code PublishingManager} continuously pipelines Publish requests. Each incoming
 * request is matched, in FIFO order, against a script of {@link PublishResponder}s enqueued by the
 * test. When the script is empty, the request is <i>parked</i> (an uncompleted future is returned)
 * and fulfilled as soon as the test enqueues another responder. Publish is dispatched
 * asynchronously by the server, so parked requests do not block any server thread.
 *
 * <p>This lets a test drive precise sequence-number scenarios — initial keep-alive, gaps, rollover,
 * duplicate keep-alives — and inspect exactly which acknowledgements the client sends back.
 *
 * <p>{@code Create}/{@code Modify}/{@code Delete}/{@code Transfer}/{@code SetPublishingMode}
 * delegate to the real server by default, so a real server-side subscription exists (useful for
 * transfer scenarios); override those methods in a subclass or via the delegate to script them too.
 *
 * <p>All state is guarded for concurrent access from server I/O threads and the test thread.
 */
public class ScriptableSubscriptionServiceSet extends DelegatingSubscriptionServiceSet {

  /** Produces the response (normal or exceptional) for a single Publish request. */
  @FunctionalInterface
  public interface PublishResponder {
    CompletableFuture<PublishResponse> respondTo(PublishRequest request);
  }

  /** Produces the response for a single Republish request. */
  @FunctionalInterface
  public interface RepublishResponder {
    RepublishResponse respondTo(RepublishRequest request) throws UaException;
  }

  private record ParkedRequest(PublishRequest request, CompletableFuture<PublishResponse> future) {}

  private final ReentrantLock lock = new ReentrantLock();
  private final ArrayDeque<PublishResponder> responders = new ArrayDeque<>();
  private final ArrayDeque<ParkedRequest> parked = new ArrayDeque<>();

  private final List<SubscriptionAcknowledgement> receivedAcknowledgements =
      Collections.synchronizedList(new ArrayList<>());
  private final AtomicInteger publishRequestCount = new AtomicInteger(0);

  private volatile RepublishResponder republishResponder;

  private final EncodingContext encodingContext;

  public ScriptableSubscriptionServiceSet(OpcUaServer server) {
    super(server);
    this.encodingContext = server.getStaticEncodingContext();
  }

  // region Publish scripting

  /**
   * Enqueue a responder that will service the next (or next parked) Publish request.
   *
   * @param responder the {@link PublishResponder} to enqueue.
   */
  public void enqueue(PublishResponder responder) {
    ParkedRequest parkedRequest;
    lock.lock();
    try {
      parkedRequest = parked.poll();
      if (parkedRequest == null) {
        responders.add(responder);
        return;
      }
    } finally {
      lock.unlock();
    }

    // Fulfill the parked request outside the lock.
    fulfill(responder, parkedRequest);
  }

  /**
   * Enqueue a keep-alive Publish response (no notification data) with the given sequence number.
   */
  public void enqueueKeepAlive(
      UInteger subscriptionId, long sequenceNumber, UInteger... available) {
    enqueueNotification(subscriptionId, sequenceNumber, null, available);
  }

  /** Enqueue a data-change Publish response carrying the given monitored-item notifications. */
  public void enqueueDataChange(
      UInteger subscriptionId,
      long sequenceNumber,
      List<MonitoredItemNotification> items,
      UInteger... available) {

    ExtensionObject[] notificationData = {
      encode(new DataChangeNotification(items.toArray(MonitoredItemNotification[]::new), null))
    };

    enqueueNotification(subscriptionId, sequenceNumber, notificationData, available);
  }

  /**
   * Enqueue a Publish response with arbitrary notification data. A {@code null} or empty {@code
   * notificationData} produces a keep-alive.
   */
  public void enqueueNotification(
      UInteger subscriptionId,
      long sequenceNumber,
      ExtensionObject[] notificationData,
      UInteger... available) {

    enqueue(
        request ->
            CompletableFuture.completedFuture(
                buildPublishResponse(
                    request, subscriptionId, sequenceNumber, notificationData, available, false)));
  }

  /** Enqueue a service-level failure (ServiceFault) for the next Publish request. */
  public void enqueueServiceFault(long statusCode) {
    enqueue(request -> CompletableFuture.failedFuture(new UaException(statusCode)));
  }

  /**
   * Build a well-formed {@link PublishResponse} for {@code request}, echoing its request handle and
   * acknowledging every acknowledgement in the request with {@code Good}.
   */
  public PublishResponse buildPublishResponse(
      PublishRequest request,
      UInteger subscriptionId,
      long sequenceNumber,
      ExtensionObject[] notificationData,
      UInteger[] available,
      boolean moreNotifications) {

    var responseHeader =
        new ResponseHeader(
            DateTime.now(),
            request.getRequestHeader().getRequestHandle(),
            StatusCode.GOOD,
            null,
            null,
            null);

    SubscriptionAcknowledgement[] acks = request.getSubscriptionAcknowledgements();
    int ackCount = acks != null ? acks.length : 0;
    var results = new StatusCode[ackCount];
    Arrays.fill(results, StatusCode.GOOD);

    ExtensionObject[] data =
        (notificationData == null || notificationData.length == 0) ? null : notificationData;

    var notificationMessage = new NotificationMessage(uint(sequenceNumber), DateTime.now(), data);

    UInteger[] availableSequenceNumbers =
        (available == null || available.length == 0) ? null : available;

    return new PublishResponse(
        responseHeader,
        subscriptionId,
        availableSequenceNumbers,
        moreNotifications,
        notificationMessage,
        results,
        null);
  }

  // endregion

  // region Republish scripting

  /**
   * Install a responder for Republish requests. When {@code null} (the default), Republish
   * delegates to the real server.
   */
  public void setRepublishResponder(RepublishResponder responder) {
    this.republishResponder = responder;
  }

  /**
   * Build a {@link RepublishResponse} carrying {@code notificationData} at {@code sequenceNumber}.
   */
  public RepublishResponse buildRepublishResponse(
      RepublishRequest request, long sequenceNumber, ExtensionObject[] notificationData) {

    var responseHeader =
        new ResponseHeader(
            DateTime.now(),
            request.getRequestHeader().getRequestHandle(),
            StatusCode.GOOD,
            null,
            null,
            null);

    var notificationMessage =
        new NotificationMessage(uint(sequenceNumber), DateTime.now(), notificationData);

    return new RepublishResponse(responseHeader, notificationMessage);
  }

  // endregion

  // region Inspection

  /**
   * @return every {@link SubscriptionAcknowledgement} the client has sent, in arrival order.
   */
  public List<SubscriptionAcknowledgement> getReceivedAcknowledgements() {
    return List.copyOf(receivedAcknowledgements);
  }

  /**
   * @return the number of Publish requests received so far.
   */
  public int getPublishRequestCount() {
    return publishRequestCount.get();
  }

  /**
   * Fail every currently parked Publish request. Call from test teardown to avoid leaking futures
   * held by the server dispatch.
   */
  public void failParkedRequests(long statusCode) {
    List<ParkedRequest> toFail;
    lock.lock();
    try {
      toFail = new ArrayList<>(parked);
      parked.clear();
    } finally {
      lock.unlock();
    }
    toFail.forEach(p -> p.future().completeExceptionally(new UaException(statusCode)));
  }

  /**
   * Encode a structured value into an {@link ExtensionObject} using the server encoding context.
   */
  public ExtensionObject encode(UaStructuredType value) {
    return ExtensionObject.encode(encodingContext, value);
  }

  // endregion

  @Override
  public CompletableFuture<PublishResponse> onPublish(
      ServiceRequestContext context, PublishRequest request) {

    SubscriptionAcknowledgement[] acks = request.getSubscriptionAcknowledgements();
    if (acks != null) {
      Collections.addAll(receivedAcknowledgements, acks);
    }
    publishRequestCount.incrementAndGet();

    PublishResponder responder;
    lock.lock();
    try {
      responder = responders.poll();
      if (responder == null) {
        var future = new CompletableFuture<PublishResponse>();
        parked.add(new ParkedRequest(request, future));
        return future;
      }
    } finally {
      lock.unlock();
    }

    return responder.respondTo(request);
  }

  @Override
  public RepublishResponse onRepublish(ServiceRequestContext context, RepublishRequest request)
      throws UaException {

    RepublishResponder responder = this.republishResponder;
    if (responder != null) {
      return responder.respondTo(request);
    }
    return super.onRepublish(context, request);
  }

  private static void fulfill(PublishResponder responder, ParkedRequest parkedRequest) {
    CompletableFuture<PublishResponse> source;
    try {
      source = responder.respondTo(parkedRequest.request());
    } catch (RuntimeException e) {
      parkedRequest.future().completeExceptionally(e);
      return;
    }
    source.whenComplete(
        (response, ex) -> {
          if (ex != null) {
            parkedRequest.future().completeExceptionally(ex);
          } else {
            parkedRequest.future().complete(response);
          }
        });
  }
}

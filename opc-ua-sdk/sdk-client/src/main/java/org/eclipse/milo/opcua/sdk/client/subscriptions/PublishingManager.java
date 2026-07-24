/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.subscriptions;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicLong;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.OpcUaSession;
import org.eclipse.milo.opcua.sdk.client.SessionActivityListener;
import org.eclipse.milo.opcua.sdk.client.UaSession;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.DataChangeNotification;
import org.eclipse.milo.opcua.stack.core.types.structured.EventFieldList;
import org.eclipse.milo.opcua.stack.core.types.structured.EventNotificationList;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemNotification;
import org.eclipse.milo.opcua.stack.core.types.structured.NotificationMessage;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.RequestHeader;
import org.eclipse.milo.opcua.stack.core.types.structured.StatusChangeNotification;
import org.eclipse.milo.opcua.stack.core.types.structured.SubscriptionAcknowledgement;
import org.eclipse.milo.opcua.stack.core.util.TaskQueue;
import org.eclipse.milo.opcua.stack.core.util.Unit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PublishingManager {

  /**
   * Upper bound on the number of missing NotificationMessages the client will try to recover when
   * the Server does not tell it what it is holding, i.e. when the PublishResponse carries no
   * availableSequenceNumbers.
   *
   * <p>Recovery is one Republish call per missing message, so the gap has to be bounded by
   * something: a sequence number that is far ahead — because it is corrupt, or because it comes
   * from a Subscription whose state the client has lost track of — would otherwise cost up to 2^32
   * round trips. When the Server does advertise availableSequenceNumbers, that list is the bound.
   */
  private static final long DEFAULT_MAX_RECOVERABLE_GAP = 64L;

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final ConcurrentMap<NodeId, AtomicLong> pendingCountMap = new ConcurrentHashMap<>();

  private final Map<UInteger, SubscriptionDetails> subscriptionDetails = new ConcurrentHashMap<>();

  private final OpcUaClient client;

  public PublishingManager(OpcUaClient client) {
    this.client = client;

    // When a Session gets re-activated after a connection loss we need to make sure PublishRequests
    // are being sent again.
    client.addSessionActivityListener(
        new SessionActivityListener() {
          @Override
          public void onSessionActive(UaSession session) {
            maybeSendPublishRequests();
          }
        });
  }

  void addSubscription(OpcUaSubscription subscription) {
    Executor executor = client.getTransport().getConfig().getExecutor();

    subscription
        .getSubscriptionId()
        .ifPresent(
            id -> subscriptionDetails.put(id, new SubscriptionDetails(subscription, executor)));

    maybeSendPublishRequests();
  }

  void removeSubscription(OpcUaSubscription subscription) {
    subscription.getSubscriptionId().ifPresent(subscriptionDetails::remove);

    maybeSendPublishRequests();
  }

  private void maybeSendPublishRequests() {
    long maxPendingPublishes = getMaxPendingPublishes();

    if (maxPendingPublishes > 0) {
      client
          .getSessionAsync()
          .whenComplete(
              (session, ex) -> {
                if (session != null) {
                  AtomicLong pendingCount =
                      pendingCountMap.computeIfAbsent(
                          session.getSessionId(), id -> new AtomicLong(0L));

                  for (long i = pendingCount.get(); i < maxPendingPublishes; i++) {
                    if (pendingCount.incrementAndGet() <= maxPendingPublishes) {
                      sendPublishRequest(session, pendingCount);
                    } else {
                      pendingCount.getAndUpdate(p -> (p > 0) ? p - 1 : 0);
                    }
                  }

                  if (pendingCountMap.size() > 1) {
                    // Prune any old sessions...
                    pendingCountMap
                        .entrySet()
                        .removeIf(e -> !e.getKey().equals(session.getSessionId()));
                  }
                } else {
                  logger.debug("Session not available", ex);

                  pendingCountMap.clear();
                }
              });
    }
  }

  void sendPublishRequest(OpcUaSession session, AtomicLong pendingCount) {
    try {
      var subscriptionAcknowledgements = new ArrayList<SubscriptionAcknowledgement>();

      subscriptionDetails
          .values()
          .forEach(
              subscription -> {
                synchronized (subscription.availableAcknowledgements) {
                  subscription.availableAcknowledgements.forEach(
                      sequenceNumber ->
                          subscription
                              .subscription
                              .getSubscriptionId()
                              .ifPresent(
                                  subscriptionId ->
                                      subscriptionAcknowledgements.add(
                                          new SubscriptionAcknowledgement(
                                              subscriptionId, sequenceNumber))));
                  subscription.availableAcknowledgements.clear();
                }
              });

      RequestHeader requestHeader =
          client.newRequestHeader(session.getAuthenticationToken(), getTimeoutHint());

      UInteger requestHandle = requestHeader.getRequestHandle();

      var request =
          new PublishRequest(
              requestHeader,
              subscriptionAcknowledgements.toArray(new SubscriptionAcknowledgement[0]));

      if (logger.isDebugEnabled()) {
        String[] ackStrings =
            subscriptionAcknowledgements.stream()
                .map(
                    ack ->
                        String.format(
                            "id=%s/seq=%s", ack.getSubscriptionId(), ack.getSequenceNumber()))
                .toArray(String[]::new);

        logger.debug(
            "Sending PublishRequest, requestHandle={}, acknowledgements={}",
            requestHandle,
            Arrays.toString(ackStrings));
      }

      client
          .sendRequestAsync(request)
          .whenComplete(
              (response, ex) -> {
                if (response instanceof PublishResponse publishResponse) {
                  // This handler runs inline on the transport's serial PublishResponse queue (see
                  // AbstractUascClientTransport#handleResponse), which is the only place the order
                  // the Server sent NotificationMessages in still exists. Queueing the work here,
                  // rather than hopping through the general-purpose executor first, is what carries
                  // that order into the Subscription's processing queue, which is itself serial.
                  // Nothing that can block belongs in this handler.
                  logger.debug(
                      "Received PublishResponse, requestHandle={}, sequenceNumber={}",
                      publishResponse.getResponseHeader().getRequestHandle(),
                      publishResponse.getNotificationMessage().getSequenceNumber());

                  UInteger subscriptionId = publishResponse.getSubscriptionId();
                  SubscriptionDetails details = subscriptionDetails.get(subscriptionId);

                  if (details != null) {
                    // Cheap and non-blocking: cancels and re-schedules a timer. The watchdog
                    // watches for the Server going quiet, so it is reset when the response is
                    // received rather than when it is eventually processed.
                    details.subscription.resetWatchdogTimer();

                    details.processingQueue.execute(
                        () -> processPublishResponse(publishResponse, pendingCount));
                  } else {
                    // Nothing to process, but the permit still has to be released, and doing it
                    // here would re-enter sendPublishRequest() on this thread.
                    client
                        .getTransport()
                        .getConfig()
                        .getExecutor()
                        .execute(() -> releasePendingPublish(pendingCount));
                  }
                } else {
                  // The failure path is dispatched asynchronously: it may run on a wheel timer
                  // thread (request timeout) or inline on the caller's thread (a request that
                  // fails before it is sent), and it re-enters maybeSendPublishRequests(), which
                  // would otherwise recurse into sendPublishRequest() on that same thread.
                  client
                      .getTransport()
                      .getConfig()
                      .getExecutor()
                      .execute(() -> handlePublishFailure(ex, requestHandle, pendingCount));
                }
              });
    } catch (Exception e) {
      // The caller took a pending-publish permit before invoking this method. If building or
      // sending the request fails synchronously no completion handler will ever run, so release
      // the permit here; otherwise it leaks and Publish traffic eventually stops for good.
      pendingCount.getAndUpdate(p -> (p > 0) ? p - 1 : 0);

      logger.error("Error sending PublishRequest", e);
    }
  }

  /**
   * Handle a PublishRequest that failed rather than returning a PublishResponse.
   *
   * @param ex the failure.
   * @param requestHandle the requestHandle of the PublishRequest that failed.
   * @param pendingCount the pending-publish permits held for the Session the request was sent on.
   */
  private void handlePublishFailure(Throwable ex, UInteger requestHandle, AtomicLong pendingCount) {
    StatusCode statusCode =
        UaException.extract(ex).map(UaException::getStatusCode).orElse(StatusCode.BAD);

    pendingCount.getAndUpdate(p -> (p > 0) ? p - 1 : 0);

    long code = statusCode.value();

    if (code == StatusCodes.Bad_SessionClosed || code == StatusCodes.Bad_SessionIdInvalid) {
      // The Session is gone, not the Subscription: no PublishResponse can arrive until the Session
      // is re-activated, so the watchdog must be suspended rather than destroyed. The Session FSM
      // treats both codes as Session faults and reconnects, after which TransferSubscriptions may
      // well keep the Subscription alive; cancelling here would de-register the watchdog's
      // SessionActivityListener and leave it unable to ever arm again.
      subscriptionDetails.values().forEach(d -> d.subscription.pauseWatchdogTimer());
    } else if (code != StatusCodes.Bad_NoSubscription
        && code != StatusCodes.Bad_TooManyPublishRequests) {

      maybeSendPublishRequests();
    }

    logger.debug("Publish service failure (requestHandle={}): {}", requestHandle, statusCode, ex);
  }

  /**
   * Process a PublishResponse.
   *
   * <p>Runs on the Subscription's own processing queue, which is serial, so the sequence-number
   * accounting below needs no further synchronization and sees PublishResponses in the order the
   * Server sent them. Nothing here may block: the queue runs on the transport's executor, which is
   * also what completes the responses to any request this method might make.
   *
   * @param response the {@link PublishResponse} to process.
   * @param pendingCount the pending-publish permits held for the Session the request was sent on.
   */
  private void processPublishResponse(PublishResponse response, AtomicLong pendingCount) {
    UInteger subscriptionId = response.getSubscriptionId();

    SubscriptionDetails details = subscriptionDetails.get(subscriptionId);

    if (details == null) {
      releasePendingPublish(pendingCount);
      return;
    }

    NotificationMessage notificationMessage = response.getNotificationMessage();

    boolean isKeepAlive =
        notificationMessage.getNotificationData() == null
            || notificationMessage.getNotificationData().length == 0;

    long receivedSequenceNumber = notificationMessage.getSequenceNumber().longValue();

    logger.debug(
        "Processing PublishResponse, subscriptionId={}, isKeepAlive={}, "
            + "lastSequenceNumber={}, receivedSequenceNumber={}",
        subscriptionId,
        isKeepAlive,
        details.lastSequenceNumber,
        receivedSequenceNumber);

    List<UInteger> missingSequenceNumbers = List.of();

    if (SequenceNumbers.isLegal(receivedSequenceNumber)) {
      if (!isKeepAlive) {
        // Acknowledge only NotificationMessages that were actually received: Part 4 §5.14.5.2 lets
        // the Server delete an acknowledged message from its retransmission queue, so
        // acknowledging one that never arrived destroys the only copy of it. A message that has
        // already been accounted for was still received, so it is still acknowledged.
        details.availableAcknowledgements.add(notificationMessage.getSequenceNumber());
      }

      long expectedSequenceNumber = SequenceNumbers.successor(details.lastSequenceNumber);

      if (receivedSequenceNumber != expectedSequenceNumber
          && !SequenceNumbers.isAhead(receivedSequenceNumber, expectedSequenceNumber)) {

        // Neither the message expected next nor ahead of it, so it has already been accounted for:
        // a duplicate, or a message the client recovered via Republish before this copy of it
        // arrived. Accounting must only ever move forwards; processing this message again would
        // hand it to the application a second time and roll lastSequenceNumber back, making every
        // message already received after it look missing and provoking a Republish for each one.
        logger.debug(
            "Discarding NotificationMessage already accounted for, subscriptionId={}, "
                + "lastSequenceNumber={}, receivedSequenceNumber={}",
            subscriptionId,
            details.lastSequenceNumber,
            receivedSequenceNumber);

        releasePendingPublish(pendingCount);
        return;
      }

      missingSequenceNumbers = missingSequenceNumbers(details, response, receivedSequenceNumber);

      // Part 4 §5.14.1.1: a keep-alive "contains the sequence number of the next
      // NotificationMessage that is to be sent", so it accounts for everything up to that sequence
      // number's predecessor and is *not* evidence that the sequence number it carries was
      // received. A data message accounts for itself.
      details.lastSequenceNumber =
          isKeepAlive
              ? SequenceNumbers.predecessor(receivedSequenceNumber)
              : receivedSequenceNumber;
    } else {
      // Part 4 §5.14.1.1: "The value 0 is never used for the sequence number." There is no sequence
      // arithmetic that can be done with an illegal value, so deliver the message but leave the
      // sequence accounting untouched.
      logger.warn(
          "Received NotificationMessage with illegal sequenceNumber={}, subscriptionId={}",
          receivedSequenceNumber,
          subscriptionId);
    }

    if (missingSequenceNumbers.isEmpty()) {
      deliverAndReleasePendingPublish(details, notificationMessage, pendingCount);
    } else {
      // Recovery is a Republish round trip per missing NotificationMessage and must not be waited
      // for here. Pausing this Subscription's processing queue is what keeps it ordered: no later
      // PublishResponse for this Subscription is processed — and, crucially, none is delivered —
      // until every recovered NotificationMessage, and then the one that revealed the gap, has
      // been handed to the delivery queue. Other Subscriptions have their own queues and are
      // unaffected. pause() is safe to call from here because this task is running on the queue it
      // pauses, so no other task for this Subscription can be in flight.
      details.processingQueue.pause();

      republishMissingNotificationMessages(details, subscriptionId, missingSequenceNumbers)
          .whenComplete(
              (unit, ex) -> {
                try {
                  deliverAndReleasePendingPublish(details, notificationMessage, pendingCount);
                } finally {
                  details.processingQueue.resume();
                }
              });
    }
  }

  /**
   * Deliver {@code notificationMessage} to the application and release the pending-publish permit
   * once it has been delivered.
   *
   * @param details the {@link SubscriptionDetails} for the Subscription the message belongs to.
   * @param notificationMessage the {@link NotificationMessage} to deliver.
   * @param pendingCount the pending-publish permits held for the Session the request was sent on.
   */
  private void deliverAndReleasePendingPublish(
      SubscriptionDetails details,
      NotificationMessage notificationMessage,
      AtomicLong pendingCount) {

    CompletionStage<Unit> callback =
        details
            .subscription
            .getDeliveryQueue()
            .submit(() -> deliverNotificationMessage(details, notificationMessage));

    if (callback != null) {
      // Once delivery of notifications is complete, we can consider sending another
      // PublishRequest. Waiting until the client has finished receiving notifications
      // is the backpressure mechanism that prevents the server from flooding the client
      // with data change notifications faster than it can process them.
      callback.whenCompleteAsync(
          (unit, ex) -> {
            if (ex != null) {
              logger.warn(
                  "Notification delivery threw an unexpected Exception: {}", ex.getMessage(), ex);
            }

            releasePendingPublish(pendingCount);
          },
          client.getTransport().getConfig().getExecutor());
    }
  }

  /**
   * Release the pending-publish permit taken for a PublishRequest whose response has been dealt
   * with, and send a replacement PublishRequest if one is wanted.
   *
   * @param pendingCount the pending-publish permits held for the Session the request was sent on.
   */
  private void releasePendingPublish(AtomicLong pendingCount) {
    pendingCount.getAndUpdate(p -> (p > 0) ? p - 1 : 0);

    maybeSendPublishRequests();
  }

  /**
   * Determine which NotificationMessages are missing between the last sequence number accounted for
   * and {@code receivedSequenceNumber}, and which of those the Server can still retransmit.
   *
   * <p>Part 4 §5.14.1.1: "In the case of a retransmission queue overflow, the oldest sent
   * NotificationMessage gets deleted." The availableSequenceNumbers of a PublishResponse are what
   * is left in that queue, so anything older than the oldest of them is gone for good and
   * Republishing it can only be answered Bad_MessageNotAvailable. Recovery therefore starts at the
   * oldest sequence number the Server says it still holds, and what precedes it is reported as lost
   * data. A gap too large to be recoverable — more than the Server is holding, or more than {@link
   * #DEFAULT_MAX_RECOVERABLE_GAP} when it does not say — is reported as lost data in its entirety.
   *
   * <p>Runs on the Subscription's processing queue and performs no I/O.
   *
   * @param details the {@link SubscriptionDetails} for the Subscription the response belongs to.
   * @param response the {@link PublishResponse} being processed.
   * @param receivedSequenceNumber the sequence number of the received NotificationMessage.
   * @return the sequence numbers to request via Republish, oldest first; empty if there is no gap
   *     or nothing in it can be recovered.
   */
  private List<UInteger> missingSequenceNumbers(
      SubscriptionDetails details, PublishResponse response, long receivedSequenceNumber) {

    long expectedSequenceNumber = SequenceNumbers.successor(details.lastSequenceNumber);

    if (!SequenceNumbers.isAhead(receivedSequenceNumber, expectedSequenceNumber)) {
      return List.of();
    }

    UInteger subscriptionId = response.getSubscriptionId();
    UInteger[] availableSequenceNumbers = response.getAvailableSequenceNumbers();
    boolean advertised = availableSequenceNumbers != null && availableSequenceNumbers.length > 0;

    long firstRecoverable = expectedSequenceNumber;
    long maxRecoverableGap = DEFAULT_MAX_RECOVERABLE_GAP;
    boolean dataLost = false;

    if (advertised) {
      maxRecoverableGap = availableSequenceNumbers.length;

      long oldestAvailable = oldestAvailable(availableSequenceNumbers, receivedSequenceNumber);

      if (oldestAvailable != SequenceNumbers.NONE
          && SequenceNumbers.isAhead(oldestAvailable, expectedSequenceNumber)) {

        logger.warn(
            "The oldest NotificationMessage the Server can retransmit is sequenceNumber={}, so "
                + "the {} starting at sequenceNumber={} are gone; treating them as lost data, "
                + "subscriptionId={}",
            oldestAvailable,
            SequenceNumbers.forwardDistance(expectedSequenceNumber, oldestAvailable),
            expectedSequenceNumber,
            subscriptionId);

        firstRecoverable = oldestAvailable;
        dataLost = true;
      }
    }

    long missingCount = SequenceNumbers.forwardDistance(firstRecoverable, receivedSequenceNumber);

    if (missingCount > maxRecoverableGap) {
      logger.warn(
          "Gap of {} NotificationMessage(s) starting at sequenceNumber={} exceeds the {} the "
              + "Server can retransmit; treating it as lost data and resynchronizing to "
              + "sequenceNumber={}, subscriptionId={}",
          missingCount,
          firstRecoverable,
          maxRecoverableGap,
          receivedSequenceNumber,
          subscriptionId);

      details.subscription.notifyNotificationDataLost();

      return List.of();
    }

    if (dataLost) {
      details.subscription.notifyNotificationDataLost();
    }

    var sequenceNumbers = new ArrayList<UInteger>((int) missingCount);
    long sequenceNumber = firstRecoverable;

    for (long i = 0; i < missingCount; i++) {
      sequenceNumbers.add(uint(sequenceNumber));

      sequenceNumber = SequenceNumbers.successor(sequenceNumber);
    }

    return sequenceNumbers;
  }

  /**
   * @param availableSequenceNumbers a non-empty availableSequenceNumbers from a PublishResponse.
   * @param receivedSequenceNumber the sequence number of the received NotificationMessage.
   * @return the oldest sequence number the Server advertised as available for retransmission, or
   *     {@link SequenceNumbers#NONE} if it advertised none the client can make sense of.
   */
  private static long oldestAvailable(
      UInteger[] availableSequenceNumbers, long receivedSequenceNumber) {

    long oldest = SequenceNumbers.NONE;
    long oldestAge = -1;

    for (UInteger availableSequenceNumber : availableSequenceNumbers) {
      if (availableSequenceNumber == null) {
        continue;
      }

      long sequenceNumber = availableSequenceNumber.longValue();

      // A sequence number that is illegal, or that the Server has not sent yet, says nothing about
      // what its retransmission queue still holds.
      if (!SequenceNumbers.isLegal(sequenceNumber)
          || SequenceNumbers.isAhead(sequenceNumber, receivedSequenceNumber)) {
        continue;
      }

      long age = SequenceNumbers.forwardDistance(sequenceNumber, receivedSequenceNumber);

      if (age > oldestAge) {
        oldestAge = age;
        oldest = sequenceNumber;
      }
    }

    return oldest;
  }

  /**
   * Recover, via Republish, the NotificationMessages identified by {@code sequenceNumbers}.
   *
   * <p>The requests are made one at a time and asynchronously: each recovered NotificationMessage
   * is handed to the delivery queue as it arrives, so the application sees them in sequence order
   * and ahead of the message that revealed the gap, and no thread ever waits for a round trip. If
   * any of them cannot be recovered the Subscription is notified that notification data was lost.
   *
   * @param details the {@link SubscriptionDetails} for the Subscription the messages belong to.
   * @param subscriptionId the Server-assigned identifier of that Subscription.
   * @param sequenceNumbers the sequence numbers to request, oldest first.
   * @return a {@link CompletableFuture} that completes when the last of them has been dealt with.
   */
  private CompletableFuture<Unit> republishMissingNotificationMessages(
      SubscriptionDetails details, UInteger subscriptionId, List<UInteger> sequenceNumbers) {

    var recovery = new Recovery();

    CompletableFuture<Unit> chain = CompletableFuture.completedFuture(Unit.VALUE);

    for (UInteger sequenceNumber : sequenceNumbers) {
      chain =
          chain.thenCompose(
              unit ->
                  republishNotificationMessage(details, subscriptionId, sequenceNumber, recovery));
    }

    return chain.whenComplete(
        (unit, ex) -> {
          if (ex != null || recovery.dataLost) {
            details.subscription.notifyNotificationDataLost();
          }
        });
  }

  /**
   * Request one NotificationMessage via Republish and, if it arrives, acknowledge it and hand it to
   * the delivery queue.
   *
   * @param details the {@link SubscriptionDetails} for the Subscription the message belongs to.
   * @param subscriptionId the Server-assigned identifier of that Subscription.
   * @param sequenceNumber the sequence number to request.
   * @param recovery the state shared by every step of this recovery.
   * @return a {@link CompletableFuture} that completes, never exceptionally, once the request has
   *     been answered one way or the other.
   */
  private CompletableFuture<Unit> republishNotificationMessage(
      SubscriptionDetails details,
      UInteger subscriptionId,
      UInteger sequenceNumber,
      Recovery recovery) {

    if (recovery.abandoned) {
      return CompletableFuture.completedFuture(Unit.VALUE);
    }

    return client
        .republishAsync(subscriptionId, sequenceNumber)
        .handle(
            (republishResponse, ex) -> {
              if (ex != null) {
                StatusCode statusCode =
                    UaException.extract(ex).map(UaException::getStatusCode).orElse(StatusCode.BAD);

                recovery.dataLost = true;

                if (statusCode.value() != StatusCodes.Bad_MessageNotAvailable) {
                  // Bad_MessageNotAvailable is an answer about this one NotificationMessage: the
                  // Server no longer holds it, but it may well still hold the ones after it, so
                  // the rest of the recovery is still worth attempting. Any other failure is the
                  // service call itself failing — a lost Session, a closed connection, a timeout —
                  // and repeating it for every remaining sequence number can only fail the same
                  // way.
                  recovery.abandoned = true;
                }

                logger.warn(
                    "Republish service failure, subscriptionId={}, sequenceNumber={}: {}",
                    subscriptionId,
                    sequenceNumber,
                    statusCode);
              } else {
                NotificationMessage notificationMessage =
                    republishResponse.getNotificationMessage();

                details.availableAcknowledgements.add(sequenceNumber);

                details
                    .subscription
                    .getDeliveryQueue()
                    .execute(() -> deliverNotificationMessage(details, notificationMessage));
              }

              return Unit.VALUE;
            });
  }

  private void deliverNotificationMessage(
      SubscriptionDetails details, NotificationMessage notificationMessage) {
    ExtensionObject[] notificationData = notificationMessage.getNotificationData();

    if (notificationData == null || notificationData.length == 0) {
      details.subscription.notifyKeepAliveReceived();
    } else {
      for (ExtensionObject xo : notificationData) {
        Object notification = xo.decode(client.getStaticEncodingContext());

        if (notification instanceof DataChangeNotification) {
          MonitoredItemNotification[] monitoredItems =
              ((DataChangeNotification) notification).getMonitoredItems();

          if (monitoredItems != null && monitoredItems.length > 0) {
            details.subscription.notifyDataReceived(monitoredItems);
          }
        } else if (notification instanceof EventNotificationList) {
          EventFieldList[] events = ((EventNotificationList) notification).getEvents();

          if (events != null && events.length > 0) {
            details.subscription.notifyEventsReceived(events);
          }
        } else if (notification instanceof StatusChangeNotification scn) {
          StatusCode status = scn.getStatus();

          if (status.value() == StatusCodes.Bad_Timeout) {
            details.subscription.getSubscriptionId().ifPresent(subscriptionDetails::remove);
          }

          details.subscription.notifyStatusChanged(status);
        } else {
          logger.warn("Unhandled notification type: {}", notification);
        }
      }
    }
  }

  private long getMaxPendingPublishes() {
    long maxPendingPublishRequests = client.getConfig().getMaxPendingPublishRequests().longValue();

    return subscriptionDetails.isEmpty()
        ? 0
        : Math.min(subscriptionDetails.size() + 1, maxPendingPublishRequests);
  }

  private UInteger getTimeoutHint() {
    double maxKeepAlive = client.getConfig().getRequestTimeout().doubleValue();

    List<SubscriptionDetails> subscriptions = List.copyOf(subscriptionDetails.values());

    for (SubscriptionDetails details : subscriptions) {
      Optional<Double> revisedPublishingInterval =
          details.subscription.getRevisedPublishingInterval();
      Optional<UInteger> revisedMaxKeepAliveCount =
          details.subscription.getRevisedMaxKeepAliveCount();

      if (revisedPublishingInterval.isPresent() && revisedMaxKeepAliveCount.isPresent()) {
        double keepAlive =
            revisedPublishingInterval.get() * revisedMaxKeepAliveCount.get().doubleValue();

        if (keepAlive >= maxKeepAlive) {
          maxKeepAlive = keepAlive;
        }
      }
    }

    long maxPendingPublishes = getMaxPendingPublishes();
    double timeoutHint = maxKeepAlive * maxPendingPublishes * 1.5;

    if (Double.isInfinite(timeoutHint) || timeoutHint > UInteger.MAX_VALUE) {
      // The timeoutHint is encoded as a UInt32; clamp rather than let an out-of-range value
      // reach uint(), which would throw and leave this request unsent.
      timeoutHint = UInteger.MAX_VALUE;
    }

    logger.debug(
        "getTimeoutHint() maxKeepAlive={} maxPendingPublishes={} timeoutHint={}",
        maxKeepAlive,
        maxPendingPublishes,
        timeoutHint);

    return uint((long) timeoutHint);
  }

  /** State shared by the steps of a single Republish recovery. */
  private static class Recovery {

    /** {@code true} if at least one NotificationMessage could not be recovered. */
    private volatile boolean dataLost = false;

    /** {@code true} if the sequence numbers not yet requested are not worth requesting. */
    private volatile boolean abandoned = false;
  }

  private static class SubscriptionDetails {

    /** Sequence numbers of received NotificationMessages awaiting acknowledgement. */
    private final List<UInteger> availableAcknowledgements =
        Collections.synchronizedList(new ArrayList<>());

    /**
     * Serial queue on which this Subscription's PublishResponses are processed, in the order the
     * Server sent them.
     *
     * <p>One queue per Subscription rather than one for the client: a Subscription that is
     * recovering a gap pauses its own queue for the duration, and a Subscription with nothing
     * missing must not have to wait behind it.
     */
    private final TaskQueue processingQueue;

    /**
     * The sequence number of the last NotificationMessage accounted for, i.e. received, recovered
     * via Republish, or given up on. {@link SequenceNumbers#NONE} until the first PublishResponse
     * has been processed, at which point the next NotificationMessage expected is {@link
     * SequenceNumbers#FIRST}.
     */
    private volatile long lastSequenceNumber = SequenceNumbers.NONE;

    private final OpcUaSubscription subscription;

    private SubscriptionDetails(OpcUaSubscription subscription, Executor executor) {
      this.subscription = subscription;

      processingQueue = new TaskQueue(executor);
    }
  }
}

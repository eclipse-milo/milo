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
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
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
import org.eclipse.milo.opcua.stack.core.types.structured.RepublishResponse;
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
   * the Server does not tell it how many it is holding, i.e. when the PublishResponse carries no
   * availableSequenceNumbers.
   *
   * <p>Recovery is one synchronous Republish call per missing message, so the gap has to be bounded
   * by something: a sequence number that is far ahead — because it is corrupt, or because it comes
   * from a Subscription whose state the client has lost track of — would otherwise block the
   * processing queue for up to 2^32 round trips.
   */
  private static final long DEFAULT_MAX_RECOVERABLE_GAP = 64L;

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final ConcurrentMap<NodeId, AtomicLong> pendingCountMap = new ConcurrentHashMap<>();

  private final Map<UInteger, SubscriptionDetails> subscriptionDetails = new ConcurrentHashMap<>();

  private final TaskQueue processingQueue;

  private final OpcUaClient client;

  public PublishingManager(OpcUaClient client) {
    this.client = client;

    processingQueue = new TaskQueue(client.getTransport().getConfig().getExecutor());

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
    subscription
        .getSubscriptionId()
        .ifPresent(id -> subscriptionDetails.put(id, new SubscriptionDetails(subscription)));

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
          .whenCompleteAsync(
              (response, ex) -> {
                if (response instanceof PublishResponse publishResponse) {
                  logger.debug(
                      "Received PublishResponse, requestHandle={}, sequenceNumber={}",
                      publishResponse.getResponseHeader().getRequestHandle(),
                      publishResponse.getNotificationMessage().getSequenceNumber());

                  UInteger subscriptionId = publishResponse.getSubscriptionId();
                  SubscriptionDetails details = subscriptionDetails.get(subscriptionId);

                  if (details != null) {
                    details.subscription.resetWatchdogTimer();
                  }

                  processingQueue.execute(
                      () -> processPublishResponse(publishResponse, pendingCount));
                } else {
                  StatusCode statusCode =
                      UaException.extract(ex)
                          .map(UaException::getStatusCode)
                          .orElse(StatusCode.BAD);

                  pendingCount.getAndUpdate(p -> (p > 0) ? p - 1 : 0);

                  long code = statusCode.value();

                  if (code == StatusCodes.Bad_SessionClosed
                      || code == StatusCodes.Bad_SessionIdInvalid) {
                    // The Session is gone, not the Subscription: no PublishResponse can arrive
                    // until the Session is re-activated, so the watchdog must be suspended rather
                    // than destroyed. The Session FSM treats both codes as Session faults and
                    // reconnects, after which TransferSubscriptions may well keep the Subscription
                    // alive; cancelling here would de-register the watchdog's
                    // SessionActivityListener and leave it unable to ever arm again.
                    subscriptionDetails.values().forEach(d -> d.subscription.pauseWatchdogTimer());
                  } else if (code != StatusCodes.Bad_NoSubscription
                      && code != StatusCodes.Bad_TooManyPublishRequests) {

                    maybeSendPublishRequests();
                  }

                  logger.debug(
                      "Publish service failure (requestHandle={}): {}",
                      requestHandle,
                      statusCode,
                      ex);
                }
              },
              client.getTransport().getConfig().getExecutor());
    } catch (Exception e) {
      // The caller took a pending-publish permit before invoking this method. If building or
      // sending the request fails synchronously no completion handler will ever run, so release
      // the permit here; otherwise it leaks and Publish traffic eventually stops for good.
      pendingCount.getAndUpdate(p -> (p > 0) ? p - 1 : 0);

      logger.error("Error sending PublishRequest", e);
    }
  }

  private void processPublishResponse(PublishResponse response, AtomicLong pendingCount) {
    UInteger subscriptionId = response.getSubscriptionId();

    SubscriptionDetails details = subscriptionDetails.get(subscriptionId);

    if (details == null) {
      pendingCount.getAndUpdate(p -> (p > 0) ? p - 1 : 0);
      maybeSendPublishRequests();
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

    if (SequenceNumbers.isLegal(receivedSequenceNumber)) {
      recoverMissingNotificationMessages(details, response, receivedSequenceNumber);

      // Part 4 §5.14.1.1: a keep-alive "contains the sequence number of the next
      // NotificationMessage that is to be sent", so it accounts for everything up to that sequence
      // number's predecessor and is *not* evidence that the sequence number it carries was
      // received. A data message accounts for itself.
      details.lastSequenceNumber =
          isKeepAlive
              ? SequenceNumbers.predecessor(receivedSequenceNumber)
              : receivedSequenceNumber;

      if (!isKeepAlive) {
        // Acknowledge only NotificationMessages that were actually received: Part 4 §5.14.5.2 lets
        // the Server delete an acknowledged message from its retransmission queue, so
        // acknowledging one that never arrived destroys the only copy of it.
        details.availableAcknowledgements.add(notificationMessage.getSequenceNumber());
      }
    } else {
      // Part 4 §5.14.1.1: "The value 0 is never used for the sequence number." There is no sequence
      // arithmetic that can be done with an illegal value, so deliver the message but leave the
      // sequence accounting untouched.
      logger.warn(
          "Received NotificationMessage with illegal sequenceNumber={}, subscriptionId={}",
          receivedSequenceNumber,
          subscriptionId);
    }

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

            pendingCount.getAndUpdate(p -> (p > 0) ? p - 1 : 0);

            maybeSendPublishRequests();
          },
          client.getTransport().getConfig().getExecutor());
    }
  }

  /**
   * Recover, via Republish, the NotificationMessages missing between the last sequence number
   * accounted for and {@code receivedSequenceNumber}.
   *
   * <p>Recovered messages are delivered ahead of the message that revealed the gap and are
   * acknowledged; if any of them cannot be recovered the Subscription is notified that notification
   * data was lost. A gap too large to be plausibly recoverable is reported as lost data instead of
   * being iterated: see {@link #DEFAULT_MAX_RECOVERABLE_GAP}.
   *
   * @param details the {@link SubscriptionDetails} for the Subscription the response belongs to.
   * @param response the {@link PublishResponse} being processed.
   * @param receivedSequenceNumber the sequence number of the received NotificationMessage.
   */
  private void recoverMissingNotificationMessages(
      SubscriptionDetails details, PublishResponse response, long receivedSequenceNumber) {

    long expectedSequenceNumber = SequenceNumbers.successor(details.lastSequenceNumber);

    if (!SequenceNumbers.isAhead(receivedSequenceNumber, expectedSequenceNumber)) {
      return;
    }

    UInteger subscriptionId = response.getSubscriptionId();

    long missingCount =
        SequenceNumbers.forwardDistance(expectedSequenceNumber, receivedSequenceNumber);
    long maxRecoverableGap = maxRecoverableGap(response.getAvailableSequenceNumbers());

    if (missingCount > maxRecoverableGap) {
      logger.warn(
          "Gap of {} NotificationMessage(s) starting at sequenceNumber={} exceeds the {} the "
              + "Server can retransmit; treating it as lost data and resynchronizing to "
              + "sequenceNumber={}, subscriptionId={}",
          missingCount,
          expectedSequenceNumber,
          maxRecoverableGap,
          receivedSequenceNumber,
          subscriptionId);

      details.subscription.notifyNotificationDataLost();

      return;
    }

    boolean republishSuccess = true;
    long sequenceNumber = expectedSequenceNumber;

    for (long i = 0; i < missingCount; i++) {
      UInteger retransmitSequenceNumber = uint(sequenceNumber);

      try {
        RepublishResponse republishResponse =
            client.republish(subscriptionId, retransmitSequenceNumber);

        NotificationMessage republishNotificationMessage =
            republishResponse.getNotificationMessage();

        details.availableAcknowledgements.add(retransmitSequenceNumber);

        details
            .subscription
            .getDeliveryQueue()
            .execute(() -> deliverNotificationMessage(details, republishNotificationMessage));
      } catch (UaException e) {
        logger.warn("Republish service failure, sequenceNumber={}", sequenceNumber, e);

        republishSuccess = false;
      }

      sequenceNumber = SequenceNumbers.successor(sequenceNumber);
    }

    if (!republishSuccess) {
      details.subscription.notifyNotificationDataLost();
    }
  }

  /**
   * @param availableSequenceNumbers the availableSequenceNumbers from a PublishResponse, possibly
   *     {@code null} or empty.
   * @return the largest gap the client will try to recover: what the Server says it is still
   *     holding, or {@link #DEFAULT_MAX_RECOVERABLE_GAP} if it says nothing.
   */
  private static long maxRecoverableGap(UInteger[] availableSequenceNumbers) {
    return (availableSequenceNumbers != null && availableSequenceNumbers.length > 0)
        ? availableSequenceNumbers.length
        : DEFAULT_MAX_RECOVERABLE_GAP;
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

  private static class SubscriptionDetails {

    /** Sequence numbers of received NotificationMessages awaiting acknowledgement. */
    private final List<UInteger> availableAcknowledgements =
        Collections.synchronizedList(new ArrayList<>());

    /**
     * The sequence number of the last NotificationMessage accounted for, i.e. received, recovered
     * via Republish, or given up on. {@link SequenceNumbers#NONE} until the first PublishResponse
     * has been processed, at which point the next NotificationMessage expected is {@link
     * SequenceNumbers#FIRST}.
     */
    private volatile long lastSequenceNumber = SequenceNumbers.NONE;

    private final OpcUaSubscription subscription;

    private SubscriptionDetails(OpcUaSubscription subscription) {
      this.subscription = subscription;
    }
  }
}

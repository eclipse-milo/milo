/*
 * Copyright (c) 2024 the Eclipse Milo Authors
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

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final ConcurrentMap<NodeId, AtomicLong> pendingCountMap = new ConcurrentHashMap<>();

  private final Map<UInteger, SubscriptionDetails> subscriptionDetails = new ConcurrentHashMap<>();

  private final TaskQueue processingQueue;

  private final OpcUaClient client;

  public PublishingManager(OpcUaClient client) {
    this.client = client;

    processingQueue = new TaskQueue(client.getTransport().getConfig().getExecutor());
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

  private void sendPublishRequest(OpcUaSession session, AtomicLong pendingCount) {
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
        .getTransport()
        .sendRequestMessage(request)
        .whenComplete(
            (response, ex) -> {
              if (response instanceof PublishResponse) {
                PublishResponse publishResponse = (PublishResponse) response;

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
                    UaException.extract(ex).map(UaException::getStatusCode).orElse(StatusCode.BAD);

                pendingCount.getAndUpdate(p -> (p > 0) ? p - 1 : 0);

                long code = statusCode.getValue();

                if (code == StatusCodes.Bad_SessionClosed
                    || code == StatusCodes.Bad_SessionIdInvalid) {
                  subscriptionDetails.values().forEach(d -> d.subscription.cancelWatchdogTimer());
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
            });
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
    long receivedSequenceNumber = notificationMessage.getSequenceNumber().longValue();
    long expectedSequenceNumber = details.lastSequenceNumber + 1;

    if (receivedSequenceNumber > expectedSequenceNumber) {
      boolean republishSuccess = true;

      for (long sequenceNumber = expectedSequenceNumber;
          sequenceNumber < receivedSequenceNumber;
          sequenceNumber++) {

        try {
          RepublishResponse republishResponse =
              client.republish(subscriptionId, uint(sequenceNumber));

          NotificationMessage republishNotificationMessage =
              republishResponse.getNotificationMessage();

          details
              .subscription
              .getDeliveryQueue()
              .execute(() -> deliverNotificationMessage(details, republishNotificationMessage));
        } catch (UaException e) {
          logger.warn("Republish service failure, sequenceNumber={}", sequenceNumber, e);

          republishSuccess = false;
        }
      }

      if (!republishSuccess) {
        details
            .subscription
            .getDeliveryQueue()
            .execute(details.subscription::notifyNotificationDataLost);
      }

      details.lastSequenceNumber = expectedSequenceNumber;
    }

    if (notificationMessage.getNotificationData() != null
        && notificationMessage.getNotificationData().length > 0) {

      // Set last sequence number only if this isn't a keep-alive
      details.lastSequenceNumber = receivedSequenceNumber;
    }

    UInteger[] availableSequenceNumbers = response.getAvailableSequenceNumbers();
    if (availableSequenceNumbers != null && availableSequenceNumbers.length > 0) {
      synchronized (details.availableAcknowledgements) {
        details.availableAcknowledgements.clear();

        Collections.addAll(details.availableAcknowledgements, availableSequenceNumbers);
      }
    }

    CompletionStage<Unit> callback =
        details
            .subscription
            .getDeliveryQueue()
            .submit(() -> deliverNotificationMessage(details, notificationMessage));

    if (callback != null) {
      // Once delivery of notifications is complete we can consider sending another
      // PublishRequest. Waiting until the client has finished receiving notifications
      // is the backpressure mechanism that prevents the server from flooding the client
      // with data change notifications faster than it can process them.
      callback.thenRunAsync(
          () -> {
            pendingCount.getAndUpdate(p -> (p > 0) ? p - 1 : 0);

            maybeSendPublishRequests();
          },
          client.getTransport().getConfig().getExecutor());
    }
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
        } else if (notification instanceof StatusChangeNotification) {
          StatusChangeNotification scn = (StatusChangeNotification) notification;

          StatusCode status = scn.getStatus();

          if (status.getValue() == StatusCodes.Bad_Timeout) {
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
      maxKeepAlive = 0d;
    }

    logger.debug(
        "getTimeoutHint() maxKeepAlive={} maxPendingPublishes={} timeoutHint={}",
        maxKeepAlive,
        maxPendingPublishes,
        timeoutHint);

    return uint((long) timeoutHint);
  }

  private static class SubscriptionDetails {

    private final List<UInteger> availableAcknowledgements =
        Collections.synchronizedList(new ArrayList<>());

    private volatile long lastSequenceNumber = 0L;

    private final OpcUaSubscription subscription;

    private SubscriptionDetails(OpcUaSubscription subscription) {
      this.subscription = subscription;
    }
  }
}

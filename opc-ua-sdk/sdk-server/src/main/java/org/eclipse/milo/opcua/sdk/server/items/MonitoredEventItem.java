/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.items;

import static java.util.Objects.requireNonNullElse;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.events.EventContentFilter;
import org.eclipse.milo.opcua.sdk.server.events.FilterContext;
import org.eclipse.milo.opcua.sdk.server.model.objects.BaseEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.subscriptions.Subscription;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MonitoringMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilterElementResult;
import org.eclipse.milo.opcua.stack.core.types.structured.EventFieldList;
import org.eclipse.milo.opcua.stack.core.types.structured.EventFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.EventFilterResult;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoringFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.SimpleAttributeOperand;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MonitoredEventItem extends BaseMonitoredItem<Variant[]> implements EventItem {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private volatile EventFilter filter;
  private volatile EventFilterResult filterResult;
  private volatile boolean filterResultGood;

  private final AtomicBoolean eventOverflow = new AtomicBoolean(false);

  private final FilterContext filterContext;

  public MonitoredEventItem(
      OpcUaServer server,
      Session session,
      UInteger id,
      UInteger subscriptionId,
      ReadValueId readValueId,
      MonitoringMode monitoringMode,
      TimestampsToReturn timestamps,
      UInteger clientHandle,
      double samplingInterval,
      UInteger queueSize,
      boolean discardOldest) {

    super(
        server,
        session,
        id,
        subscriptionId,
        readValueId,
        monitoringMode,
        timestamps,
        clientHandle,
        samplingInterval,
        queueSize,
        discardOldest);

    filterContext =
        new FilterContext() {
          @Override
          public OpcUaServer getServer() {
            return server;
          }

          @Override
          public Optional<Session> getSession() {
            return Optional.of(session);
          }
        };
  }

  @Override
  public void onEvent(BaseEventTypeNode eventNode) {
    try {
      if (filterResultGood) {
        ContentFilter whereClause = filter.getWhereClause();

        boolean matches = EventContentFilter.evaluate(filterContext, whereClause, eventNode);

        if (matches) {
          enqueue(selectEventFields(eventNode));
        }
      }
    } catch (UaException e) {
      logger.error("Filter evaluation failed: {}", e.getMessage(), e);
    }
  }

  @NonNull
  private Variant[] selectEventFields(BaseEventTypeNode eventNode) {
    SimpleAttributeOperand[] selectClauses = filter.getSelectClauses();

    if (selectClauses != null) {
      return EventContentFilter.select(filterContext, selectClauses, eventNode);
    } else {
      return new Variant[0];
    }
  }

  @Override
  protected synchronized void enqueue(Variant[] value) {
    if (queue.size() < queue.maxSize()) {
      queue.add(value);
    } else {
      if (getQueueSize() > 1) {
        eventOverflow.set(true);

        Subscription subscription =
            session.getSubscriptionManager().getSubscription(subscriptionId);

        if (subscription != null) {
          subscription.getSubscriptionDiagnostics().getEventQueueOverflowCount().increment();
        }
      }

      if (discardOldest) {
        queue.add(value);
      } else {
        queue.set(queue.maxSize() - 1, value);
      }
    }
  }

  @Override
  public synchronized boolean getNotifications(List<UaStructuredType> notifications, int max) {
    if (eventOverflow.compareAndSet(true, false)) {
      Variant[] eventFields = generateOverflowEventFields();

      if (discardOldest) {
        // insert overflow event at beginning
        notifications.add(wrapQueueValue(eventFields));

        return super.getNotifications(notifications, max);
      } else {
        // insert overflow event at end
        boolean more = super.getNotifications(notifications, max);
        notifications.add(wrapQueueValue(eventFields));

        return more;
      }
    } else {
      return super.getNotifications(notifications, max);
    }
  }

  @NonNull
  private Variant[] generateOverflowEventFields() {
    BaseEventTypeNode overflowEvent = null;

    try {
      UUID eventId = UUID.randomUUID();

      overflowEvent =
          server
              .getEventFactory()
              .createEvent(new NodeId(1, eventId), NodeIds.EventQueueOverflowEventType);

      overflowEvent.setBrowseName(new QualifiedName(1, "EventQueueOverflow"));
      overflowEvent.setDisplayName(LocalizedText.english("EventQueueOverflow"));

      ByteBuffer buffer = ByteBuffer.allocate(64);
      buffer.putLong(eventId.getMostSignificantBits());
      buffer.putLong(eventId.getLeastSignificantBits());

      overflowEvent.setEventId(ByteString.of(buffer.array()));
      overflowEvent.setEventType(NodeIds.EventQueueOverflowEventType);
      overflowEvent.setSourceNode(NodeIds.Server);
      overflowEvent.setSourceName("Server");
      overflowEvent.setTime(DateTime.now());
      overflowEvent.setReceiveTime(DateTime.NULL_VALUE);
      overflowEvent.setMessage(LocalizedText.english("Event Queue Overflow"));
      overflowEvent.setSeverity(ushort(0));

      return selectEventFields(overflowEvent);
    } catch (UaException e) {
      logger.error("Error creating overflow event: {}", e.getMessage(), e);

      return new Variant[0];
    } finally {
      if (overflowEvent != null) {
        overflowEvent.delete();
      }
    }
  }

  @Override
  public ExtensionObject getFilterResult() {
    return ExtensionObject.encode(server.getStaticEncodingContext(), filterResult);
  }

  @Override
  public void installFilter(MonitoringFilter filter) throws UaException {
    if (filter instanceof EventFilter) {
      this.filter = (EventFilter) filter;

      filterResult = EventContentFilter.validate(filterContext, this.filter);

      StatusCode[] selectClauseResults =
          requireNonNullElse(filterResult.getSelectClauseResults(), new StatusCode[0]);

      boolean selectClauseGood = Stream.of(selectClauseResults).allMatch(StatusCode::isGood);

      ContentFilterElementResult[] elementResults =
          requireNonNullElse(
              filterResult.getWhereClauseResult().getElementResults(),
              new ContentFilterElementResult[0]);

      boolean whereClauseGood =
          Stream.of(elementResults)
              .map(ContentFilterElementResult::getStatusCode)
              .allMatch(StatusCode::isGood);

      filterResultGood = selectClauseGood && whereClauseGood;
    } else {
      filterResultGood = false;

      throw new UaException(StatusCodes.Bad_MonitoredItemFilterUnsupported);
    }
  }

  @Override
  protected EventFieldList wrapQueueValue(Variant[] value) {
    return new EventFieldList(uint(getClientHandle()), value);
  }

  @Override
  public boolean isSamplingEnabled() {
    return getMonitoringMode() != MonitoringMode.Disabled;
  }
}

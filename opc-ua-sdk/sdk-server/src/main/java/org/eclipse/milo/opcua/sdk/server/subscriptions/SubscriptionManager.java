/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.subscriptions;

import static java.util.Objects.requireNonNullElse;
import static java.util.stream.Collectors.toList;
import static org.eclipse.milo.opcua.sdk.server.servicesets.AbstractServiceSet.createResponseHeader;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.util.FutureUtils.failedUaFuture;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.sdk.core.NumericRange;
import org.eclipse.milo.opcua.sdk.core.util.GroupMapCollate;
import org.eclipse.milo.opcua.sdk.server.AddressSpace.ReadContext;
import org.eclipse.milo.opcua.sdk.server.AddressSpace.RevisedDataItemParameters;
import org.eclipse.milo.opcua.sdk.server.AddressSpace.RevisedEventItemParameters;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.items.BaseMonitoredItem;
import org.eclipse.milo.opcua.sdk.server.items.DataItem;
import org.eclipse.milo.opcua.sdk.server.items.EventItem;
import org.eclipse.milo.opcua.sdk.server.items.MonitoredDataItem;
import org.eclipse.milo.opcua.sdk.server.items.MonitoredEventItem;
import org.eclipse.milo.opcua.sdk.server.items.MonitoredItem;
import org.eclipse.milo.opcua.sdk.server.servicesets.impl.AccessController.AccessResult;
import org.eclipse.milo.opcua.sdk.server.servicesets.impl.helpers.BrowseHelper;
import org.eclipse.milo.opcua.sdk.server.servicesets.impl.helpers.BrowsePathsHelper;
import org.eclipse.milo.opcua.sdk.server.subscriptions.PublishQueue.PendingPublish;
import org.eclipse.milo.opcua.sdk.server.subscriptions.Subscription.State;
import org.eclipse.milo.opcua.stack.core.*;
import org.eclipse.milo.opcua.stack.core.types.builtin.*;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseDirection;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseResultMask;
import org.eclipse.milo.opcua.stack.core.types.enumerated.DeadbandType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MonitoringMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.*;
import org.eclipse.milo.opcua.stack.core.util.Lists;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@NullMarked
public class SubscriptionManager {

  private static final AtomicLong SUBSCRIPTION_IDS = new AtomicLong(0L);
  private static final QualifiedName EU_RANGE_BROWSE_NAME = new QualifiedName(0, "EURange");
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final Map<UInteger, Subscription> subscriptions = new ConcurrentHashMap<>();
  private final List<Subscription> transferred = new CopyOnWriteArrayList<>();
  private final AtomicLong monitoredItemCount = new AtomicLong(0L);
  private final PublishQueue publishQueue;
  private final Session session;
  private final OpcUaServer server;

  public SubscriptionManager(Session session, OpcUaServer server) {
    this.session = session;
    this.server = server;

    publishQueue = new PublishQueue(server.getConfig().getExecutor());
  }

  private static UInteger nextSubscriptionId() {
    return uint(SUBSCRIPTION_IDS.incrementAndGet());
  }

  /**
   * Split {@code monitoredItems} into a list of {@link DataItem}s and a list of {@link EventItem}s
   * and invoke the corresponding {@link Consumer} for each list if non-empty.
   *
   * @param monitoredItems the list of MonitoredItems to group.
   * @param dataItemConsumer a {@link Consumer} that accepts a non-empty list of {@link DataItem}s.
   * @param eventItemConsumer a {@link Consumer} that accepts a non-empty list of {@link
   *     EventItem}s.
   */
  private static void byMonitoredItemType(
      Collection<BaseMonitoredItem<?>> monitoredItems,
      Consumer<List<DataItem>> dataItemConsumer,
      Consumer<List<EventItem>> eventItemConsumer) {

    var dataItems = new ArrayList<DataItem>();
    var eventItems = new ArrayList<EventItem>();

    for (BaseMonitoredItem<?> item : monitoredItems) {
      if (item instanceof MonitoredDataItem) {
        dataItems.add((DataItem) item);
      } else if (item instanceof MonitoredEventItem) {
        eventItems.add((EventItem) item);
      }
    }

    try {
      if (!dataItems.isEmpty()) {
        dataItemConsumer.accept(dataItems);
      }
    } catch (Throwable t) {
      LoggerFactory.getLogger(SubscriptionManager.class)
          .error("Uncaught Throwable in dataItemConsumer", t);
    }

    try {
      if (!eventItems.isEmpty()) {
        eventItemConsumer.accept(eventItems);
      }
    } catch (Throwable t) {
      LoggerFactory.getLogger(SubscriptionManager.class)
          .error("Uncaught Throwable in eventItemConsumer", t);
    }
  }

  public Session getSession() {
    return session;
  }

  public PublishQueue getPublishQueue() {
    return publishQueue;
  }

  public OpcUaServer getServer() {
    return server;
  }

  @Nullable
  public Subscription getSubscription(UInteger subscriptionId) {
    return subscriptions.get(subscriptionId);
  }

  public List<Subscription> getSubscriptions() {
    return new ArrayList<>(subscriptions.values());
  }

  public CompletableFuture<CreateSubscriptionResponse> createSubscription(
      CreateSubscriptionRequest request) {
    if (subscriptions.size()
        >= server.getConfig().getLimits().getMaxSubscriptionsPerSession().intValue()) {
      return failedUaFuture(StatusCodes.Bad_TooManySubscriptions);
    }

    if (server.getSubscriptions().size()
        >= server.getConfig().getLimits().getMaxSubscriptions().intValue()) {
      return failedUaFuture(StatusCodes.Bad_TooManySubscriptions);
    }

    UInteger subscriptionId = nextSubscriptionId();

    var subscription =
        new Subscription(
            this,
            subscriptionId,
            request.getRequestedPublishingInterval(),
            request.getRequestedMaxKeepAliveCount().longValue(),
            request.getRequestedLifetimeCount().longValue(),
            request.getMaxNotificationsPerPublish().longValue(),
            request.getPublishingEnabled(),
            request.getPriority().intValue());

    subscriptions.put(subscriptionId, subscription);
    server.getSubscriptions().put(subscriptionId, subscription);
    server.getDiagnosticsSummary().getCumulatedSubscriptionCount().increment();
    server.getInternalEventBus().post(new SubscriptionCreatedEvent(subscription));

    subscription.setStateListener(
        (s, ps, cs) -> {
          if (cs == State.Closing) {
            subscriptions.remove(s.getId());
            server.getSubscriptions().remove(s.getId());
            server.getInternalEventBus().post(new SubscriptionDeletedEvent(s));

            /*
             * Notify AddressSpaces the items for this subscription are deleted.
             */

            Map<UInteger, BaseMonitoredItem<?>> monitoredItems = s.getMonitoredItems();

            byMonitoredItemType(
                monitoredItems.values(),
                dataItems -> server.getAddressSpaceManager().onDataItemsDeleted(dataItems),
                eventItems -> server.getAddressSpaceManager().onEventItemsDeleted(eventItems));

            monitoredItemCount.getAndUpdate(count -> count - monitoredItems.size());
            server.getMonitoredItemCount().getAndUpdate(count -> count - monitoredItems.size());

            monitoredItems.clear();
          }
        });

    subscription.startPublishingTimer();

    ResponseHeader header = createResponseHeader(request);

    var response =
        new CreateSubscriptionResponse(
            header,
            subscriptionId,
            subscription.getPublishingInterval(),
            uint(subscription.getLifetimeCount()),
            uint(subscription.getMaxKeepAliveCount()));

    return CompletableFuture.completedFuture(response);
  }

  public CompletableFuture<ModifySubscriptionResponse> modifySubscription(
      ModifySubscriptionRequest request) {
    UInteger subscriptionId = request.getSubscriptionId();
    Subscription subscription = subscriptions.get(subscriptionId);

    if (subscription == null) {
      return failedUaFuture(StatusCodes.Bad_SubscriptionIdInvalid);
    }

    subscription.modifySubscription(request);

    ResponseHeader header = createResponseHeader(request);

    var response =
        new ModifySubscriptionResponse(
            header,
            subscription.getPublishingInterval(),
            uint(subscription.getLifetimeCount()),
            uint(subscription.getMaxKeepAliveCount()));

    return CompletableFuture.completedFuture(response);
  }

  public CompletableFuture<DeleteSubscriptionsResponse> deleteSubscriptions(
      DeleteSubscriptionsRequest request) {
    UInteger[] subscriptionIds = requireNonNullElse(request.getSubscriptionIds(), new UInteger[0]);

    if (subscriptionIds.length == 0) {
      return failedUaFuture(StatusCodes.Bad_NothingToDo);
    }

    StatusCode[] results = new StatusCode[subscriptionIds.length];

    for (int i = 0; i < subscriptionIds.length; i++) {
      UInteger subscriptionId = subscriptionIds[i];
      Subscription subscription = subscriptions.remove(subscriptionId);

      if (subscription != null) {
        server.getSubscriptions().remove(subscription.getId());
        server.getInternalEventBus().post(new SubscriptionDeletedEvent(subscription));

        List<BaseMonitoredItem<?>> deletedItems = subscription.deleteSubscription();

        /*
         * Notify AddressSpaces of the items we just deleted.
         */

        byMonitoredItemType(
            deletedItems,
            dataItems -> server.getAddressSpaceManager().onDataItemsDeleted(dataItems),
            eventItems -> server.getAddressSpaceManager().onEventItemsDeleted(eventItems));

        results[i] = StatusCode.GOOD;

        monitoredItemCount.getAndUpdate(count -> count - deletedItems.size());
        server.getMonitoredItemCount().getAndUpdate(count -> count - deletedItems.size());
      } else {
        results[i] = new StatusCode(StatusCodes.Bad_SubscriptionIdInvalid);
      }
    }

    ResponseHeader header = createResponseHeader(request);

    var response = new DeleteSubscriptionsResponse(header, results, new DiagnosticInfo[0]);

    while (subscriptions.isEmpty() && publishQueue.isNotEmpty()) {
      PendingPublish pending = publishQueue.poll();
      if (pending != null) {
        pending.responseFuture.completeExceptionally(
            new UaException(StatusCodes.Bad_NoSubscription));
      }
    }

    return CompletableFuture.completedFuture(response);
  }

  public CompletableFuture<SetPublishingModeResponse> setPublishingMode(
      SetPublishingModeRequest request) {
    UInteger[] subscriptionIds = request.getSubscriptionIds();

    if (subscriptionIds == null || subscriptionIds.length == 0) {
      return failedUaFuture(StatusCodes.Bad_NothingToDo);
    }

    StatusCode[] results = new StatusCode[subscriptionIds.length];

    for (int i = 0; i < subscriptionIds.length; i++) {
      Subscription subscription = subscriptions.get(subscriptionIds[i]);
      if (subscription == null) {
        results[i] = new StatusCode(StatusCodes.Bad_SubscriptionIdInvalid);
      } else {
        subscription.setPublishingMode(request);
        results[i] = StatusCode.GOOD;
      }
    }

    ResponseHeader header = createResponseHeader(request);

    var response = new SetPublishingModeResponse(header, results, new DiagnosticInfo[0]);

    return CompletableFuture.completedFuture(response);
  }

  public CreateMonitoredItemsResponse createMonitoredItems(
      ServiceRequestContext context, CreateMonitoredItemsRequest request) throws UaException {

    UInteger subscriptionId = request.getSubscriptionId();
    Subscription subscription = subscriptions.get(subscriptionId);
    TimestampsToReturn timestamps = request.getTimestampsToReturn();
    List<MonitoredItemCreateRequest> itemsToCreate = Lists.ofNullable(request.getItemsToCreate());

    if (subscription == null) {
      throw new UaException(StatusCodes.Bad_SubscriptionIdInvalid);
    }
    if (timestamps == null) {
      throw new UaException(StatusCodes.Bad_TimestampsToReturnInvalid);
    }
    if (itemsToCreate.isEmpty()) {
      throw new UaException(StatusCodes.Bad_NothingToDo);
    }

    Map<ReadValueId, AccessResult> accessResults =
        server
            .getAccessController()
            .checkReadAccess(
                session,
                itemsToCreate.stream().map(MonitoredItemCreateRequest::getItemToMonitor).toList());

    List<MonitoredItemCreateResult> results =
        GroupMapCollate.groupMapCollate(
            itemsToCreate,
            createRequest -> accessResults.get(createRequest.getItemToMonitor()),
            accessResult ->
                group -> {
                  if (accessResult instanceof AccessResult.Denied denied) {
                    var result =
                        new MonitoredItemCreateResult(
                            denied.statusCode(), uint(0), 0.0, uint(0), null);
                    return Collections.nCopies(group.size(), result);
                  } else {
                    return createMonitoredItems(subscription, timestamps, group);
                  }
                });

    ResponseHeader header = createResponseHeader(request);

    return new CreateMonitoredItemsResponse(
        header, results.toArray(new MonitoredItemCreateResult[0]), new DiagnosticInfo[0]);
  }

  private List<MonitoredItemCreateResult> createMonitoredItems(
      Subscription subscription,
      TimestampsToReturn timestamps,
      List<MonitoredItemCreateRequest> requests) {

    // Split requests by filter type to enable targeted attribute reading.
    // Only Percent Deadband requests on Value attributes need the expensive TypeDefinition +
    // EURange lookups, since Percent Deadband filtering only applies to Value attributes.
    var regularRequests = new ArrayList<MonitoredItemCreateRequest>();
    var percentDeadbandRequests = new ArrayList<MonitoredItemCreateRequest>();

    for (MonitoredItemCreateRequest request : requests) {
      boolean isValueAttribute =
          request.getItemToMonitor().getAttributeId().equals(AttributeId.Value.uid());

      if (isValueAttribute && hasPercentDeadbandFilter(request)) {
        percentDeadbandRequests.add(request);
      } else {
        regularRequests.add(request);
      }
    }

    Map<NodeId, AttributesResponse> regularAttributes;
    if (regularRequests.isEmpty()) {
      regularAttributes = Collections.emptyMap();
    } else {
      regularAttributes =
          readMonitoringAttributes(
              regularRequests.stream().map(r -> r.getItemToMonitor().getNodeId()).toList());
    }

    Map<NodeId, AttributesResponse> analogItemAttributes;
    if (percentDeadbandRequests.isEmpty()) {
      analogItemAttributes = Collections.emptyMap();
    } else {
      analogItemAttributes =
          readAnalogItemAttributes(
              percentDeadbandRequests.stream().map(r -> r.getItemToMonitor().getNodeId()).toList());
    }

    var results = new ArrayList<MonitoredItemCreateResult>();
    List<BaseMonitoredItem<?>> monitoredItems = new ArrayList<>();

    long globalMax = server.getConfig().getLimits().getMaxMonitoredItems().longValue();
    long sessionMax = server.getConfig().getLimits().getMaxMonitoredItemsPerSession().longValue();

    for (MonitoredItemCreateRequest request : requests) {
      try {
        long globalCount = server.getMonitoredItemCount().incrementAndGet();
        long sessionCount = monitoredItemCount.incrementAndGet();

        if (globalCount <= globalMax && sessionCount <= sessionMax) {
          BaseMonitoredItem<?> monitoredItem;

          boolean isValueAttribute =
              request.getItemToMonitor().getAttributeId().equals(AttributeId.Value.uid());

          AttributesResponse attributesResponse =
              (isValueAttribute && hasPercentDeadbandFilter(request))
                  ? analogItemAttributes.get(request.getItemToMonitor().getNodeId())
                  : regularAttributes.get(request.getItemToMonitor().getNodeId());

          monitoredItem =
              createMonitoredItem(request, subscription, timestamps, attributesResponse);

          monitoredItems.add(monitoredItem);

          results.add(
              new MonitoredItemCreateResult(
                  StatusCode.GOOD,
                  monitoredItem.getId(),
                  monitoredItem.getSamplingInterval(),
                  uint(monitoredItem.getQueueSize()),
                  monitoredItem.getFilterResult()));
        } else {
          throw new UaException(StatusCodes.Bad_TooManyMonitoredItems);
        }
      } catch (UaException e) {
        monitoredItemCount.decrementAndGet();
        server.getMonitoredItemCount().decrementAndGet();

        results.add(
            new MonitoredItemCreateResult(
                e.getStatusCode(), UInteger.MIN, 0.0, UInteger.MIN, null));
      }
    }

    subscription.addMonitoredItems(monitoredItems);

    byMonitoredItemType(
        monitoredItems,
        dataItems -> server.getAddressSpaceManager().onDataItemsCreated(dataItems),
        eventItems -> server.getAddressSpaceManager().onEventItemsCreated(eventItems));

    return results;
  }

  private boolean hasPercentDeadbandFilter(MonitoredItemCreateRequest request) {
    try {
      ExtensionObject filterXo = request.getRequestedParameters().getFilter();
      if (filterXo == null || filterXo.isNull()) {
        return false;
      }

      Object filterObject = filterXo.decode(server.getStaticEncodingContext());
      if (filterObject instanceof DataChangeFilter filter) {
        DeadbandType deadbandType = DeadbandType.from(filter.getDeadbandType().intValue());
        return deadbandType == DeadbandType.Percent;
      }

      return false;
    } catch (UaSerializationException e) {
      logger.debug("Failed to decode filter when checking for Percent Deadband", e);
      return false;
    }
  }

  /** Create a {@link MonitoredDataItem}. */
  private MonitoredDataItem createMonitoredDataItem(
      MonitoredItemCreateRequest request,
      Subscription subscription,
      TimestampsToReturn timestamps,
      AttributeId attributeId,
      MonitoringAttributes attributes,
      @Nullable Range euRange)
      throws UaException {

    NodeClass nodeClass = attributes.nodeClass();
    if (nodeClass == null) {
      throw new UaException(StatusCodes.Bad_NodeIdUnknown);
    }
    if (!AttributeId.getAttributes(nodeClass).contains(attributeId)) {
      throw new UaException(StatusCodes.Bad_AttributeIdInvalid);
    }

    // Validate the requested index range by parsing it.
    String indexRange = request.getItemToMonitor().getIndexRange();
    if (indexRange != null && !indexRange.isEmpty()) {
      NumericRange.parse(indexRange);
    }

    Double minimumSamplingInterval = attributes.minimumSamplingInterval();
    if (minimumSamplingInterval == null) {
      minimumSamplingInterval = server.getConfig().getLimits().getMinSupportedSampleRate();
    }

    MonitoringFilter filter = MonitoredDataItem.DEFAULT_FILTER;

    try {
      ExtensionObject filterXo = request.getRequestedParameters().getFilter();

      if (filterXo != null && !filterXo.isNull()) {
        Object filterObject = filterXo.decode(server.getStaticEncodingContext());

        filter = validateDataItemFilter(filterObject, attributeId, attributes);
      }
    } catch (UaSerializationException e) {
      logger.debug("error decoding MonitoringFilter", e);

      throw new UaException(StatusCodes.Bad_MonitoredItemFilterInvalid, e);
    }

    double requestedSamplingInterval =
        getSamplingInterval(
            subscription,
            minimumSamplingInterval,
            request.getRequestedParameters().getSamplingInterval());

    RevisedDataItemParameters revisedParameters;

    try {
      revisedParameters =
          server
              .getAddressSpaceManager()
              .onCreateDataItem(
                  request.getItemToMonitor(),
                  requestedSamplingInterval,
                  request.getRequestedParameters().getQueueSize());
    } catch (Throwable t) {
      throw new UaException(StatusCodes.Bad_InternalError, t);
    }

    MonitoredDataItem monitoredItem =
        new MonitoredDataItem(
            server,
            session,
            uint(subscription.nextItemId()),
            subscription.getId(),
            request.getItemToMonitor(),
            request.getMonitoringMode(),
            timestamps,
            request.getRequestedParameters().getClientHandle(),
            revisedParameters.revisedSamplingInterval(),
            revisedParameters.revisedQueueSize(),
            request.getRequestedParameters().getDiscardOldest());

    // Set euRange before installFilter() so Percent Deadband validation can access it
    if (euRange != null) {
      monitoredItem.setEuRange(euRange);
    }

    monitoredItem.installFilter(filter);

    return monitoredItem;
  }

  /** Create a {@link MonitoredEventItem} for EventNotifier attribute monitoring. */
  private MonitoredEventItem createMonitoredEventItem(
      MonitoredItemCreateRequest request,
      Subscription subscription,
      TimestampsToReturn timestamps,
      MonitoringAttributes attributes)
      throws UaException {

    UByte eventNotifier = attributes.eventNotifier();

    // Verify that the SubscribeToEvents bit is set
    if (eventNotifier == null || (eventNotifier.intValue() & 1) == 0) {
      throw new UaException(StatusCodes.Bad_AttributeIdInvalid);
    }

    Object filterObject =
        request.getRequestedParameters().getFilter().decode(server.getStaticEncodingContext());

    MonitoringFilter filter = validateEventItemFilter(filterObject);

    RevisedEventItemParameters revisedParameters;

    try {
      revisedParameters =
          server
              .getAddressSpaceManager()
              .onCreateEventItem(
                  request.getItemToMonitor(), request.getRequestedParameters().getQueueSize());
    } catch (Throwable t) {
      throw new UaException(StatusCodes.Bad_InternalError, t);
    }

    MonitoredEventItem monitoredEventItem =
        new MonitoredEventItem(
            server,
            session,
            uint(subscription.nextItemId()),
            subscription.getId(),
            request.getItemToMonitor(),
            request.getMonitoringMode(),
            timestamps,
            request.getRequestedParameters().getClientHandle(),
            0.0,
            revisedParameters.revisedQueueSize(),
            request.getRequestedParameters().getDiscardOldest());

    monitoredEventItem.installFilter(filter);

    return monitoredEventItem;
  }

  /**
   * Create a monitored item, dispatching to the appropriate specialized method based on the
   * response type. Pattern matches on AttributesResponse to route to event items or data items. For
   * AnalogItemType nodes with EURange, sets the euRange on the MonitoredDataItem to enable Percent
   * Deadband filtering.
   */
  private BaseMonitoredItem<?> createMonitoredItem(
      MonitoredItemCreateRequest request,
      Subscription subscription,
      TimestampsToReturn timestamps,
      AttributesResponse attributeResponse)
      throws UaException {

    QualifiedName dataEncoding = request.getItemToMonitor().getDataEncoding();
    AttributeId attributeId =
        AttributeId.from(request.getItemToMonitor().getAttributeId())
            .orElseThrow(() -> new UaException(StatusCodes.Bad_AttributeIdInvalid));

    if (dataEncoding.isNotNull()) {
      if (attributeId != AttributeId.Value) {
        throw new UaException(StatusCodes.Bad_DataEncodingInvalid);
      }

      if (!server.getEncodingManager().hasEncoding(dataEncoding)) {
        throw new UaException(StatusCodes.Bad_DataEncodingUnsupported);
      }
    }

    if (attributeResponse instanceof NegativeResponse negativeResponse) {
      throw new UaException(negativeResponse.statusCode());
    } else if (attributeResponse instanceof AnalogItemAttributes analogItemAttributes) {
      // AnalogItemAttributes should only be received for Value attribute requests.
      // This is guaranteed by the request splitting logic in createMonitoredItems().
      if (attributeId != AttributeId.Value) {
        throw new IllegalStateException(
            "AnalogItemAttributes received for non-Value attribute: " + attributeId);
      }

      MonitoringAttributes monitoringAttributes = analogItemAttributes.monitoringAttributes();
      Range euRange = analogItemAttributes.euRange();

      if (euRange == null) {
        throw new UaException(
            StatusCodes.Bad_MonitoredItemFilterUnsupported,
            "Percent Deadband requires AnalogItemType with valid EURange property");
      }

      return createMonitoredDataItem(
          request, subscription, timestamps, attributeId, monitoringAttributes, euRange);
    } else if (attributeResponse instanceof MonitoringAttributes monitoringAttributes) {
      if (attributeId == AttributeId.EventNotifier) {
        return createMonitoredEventItem(request, subscription, timestamps, monitoringAttributes);
      } else {
        return createMonitoredDataItem(
            request, subscription, timestamps, attributeId, monitoringAttributes, null);
      }
    } else {
      throw new UaException(StatusCodes.Bad_InternalError, "Unexpected AttributesResponse type");
    }
  }

  private MonitoringFilter validateDataItemFilter(
      Object filterObject, AttributeId attributeId, MonitoringAttributes monitoringAttributes)
      throws UaException {

    if (filterObject instanceof MonitoringFilter) {
      if (filterObject instanceof DataChangeFilter filter) {
        DeadbandType deadbandType = DeadbandType.from(filter.getDeadbandType().intValue());

        if (deadbandType == null) {
          throw new UaException(StatusCodes.Bad_DeadbandFilterInvalid);
        }

        // Percent Deadband is now supported for AnalogItemType nodes

        if ((deadbandType == DeadbandType.Absolute || deadbandType == DeadbandType.Percent)
            && attributeId != AttributeId.Value) {
          // Absolute deadband is only allowed for Value attributes
          throw new UaException(StatusCodes.Bad_FilterNotAllowed);
        }

        if (deadbandType != DeadbandType.None) {
          NodeId dataTypeId = monitoringAttributes.dataType();
          if (dataTypeId == null) {
            dataTypeId = NodeId.NULL_VALUE;
          }

          if (!NodeIds.Number.equals(dataTypeId)
              && !server.getDataTypeTree().isSubtypeOf(dataTypeId, NodeIds.Number)) {
            throw new UaException(StatusCodes.Bad_FilterNotAllowed);
          }
        }

        return filter;
      } else if (filterObject instanceof EventFilter) {
        throw new UaException(StatusCodes.Bad_FilterNotAllowed);
      } else {
        // AggregateFilter or some future unimplemented filter
        throw new UaException(StatusCodes.Bad_MonitoredItemFilterUnsupported);
      }
    } else {
      throw new UaException(StatusCodes.Bad_MonitoredItemFilterInvalid);
    }
  }

  private MonitoringFilter validateEventItemFilter(Object filterObject) throws UaException {
    if (filterObject instanceof MonitoringFilter) {
      if (!(filterObject instanceof EventFilter)) {
        throw new UaException(StatusCodes.Bad_FilterNotAllowed);
      }
      return (EventFilter) filterObject;
    } else {
      throw new UaException(StatusCodes.Bad_MonitoredItemFilterInvalid);
    }
  }

  public ModifyMonitoredItemsResponse modifyMonitoredItems(
      ServiceRequestContext context, ModifyMonitoredItemsRequest request) throws UaException {

    UInteger subscriptionId = request.getSubscriptionId();
    Subscription subscription = subscriptions.get(subscriptionId);
    TimestampsToReturn timestamps = request.getTimestampsToReturn();
    MonitoredItemModifyRequest[] itemsToModify = request.getItemsToModify();

    if (subscription == null) {
      throw new UaException(StatusCodes.Bad_SubscriptionIdInvalid);
    }
    if (timestamps == null) {
      throw new UaException(StatusCodes.Bad_TimestampsToReturnInvalid);
    }
    if (itemsToModify == null || itemsToModify.length == 0) {
      throw new UaException(StatusCodes.Bad_NothingToDo);
    }

    List<NodeId> nodeIds =
        Stream.of(itemsToModify)
            .map(
                item -> {
                  UInteger itemId = item.getMonitoredItemId();
                  BaseMonitoredItem<?> monitoredItem = subscription.getMonitoredItems().get(itemId);
                  return monitoredItem != null
                      ? monitoredItem.getReadValueId().getNodeId()
                      : NodeId.NULL_VALUE;
                })
            .filter(NodeId::isNotNull)
            .collect(toList());

    Map<NodeId, AttributesResponse> attributesMap = readMonitoringAttributes(nodeIds);

    MonitoredItemModifyResult[] modifyResults = new MonitoredItemModifyResult[itemsToModify.length];

    List<BaseMonitoredItem<?>> monitoredItems = new ArrayList<>();

    for (int i = 0; i < itemsToModify.length; i++) {
      MonitoredItemModifyRequest modifyRequest = itemsToModify[i];

      try {
        BaseMonitoredItem<?> monitoredItem =
            modifyMonitoredItem(modifyRequest, timestamps, subscription, attributesMap);

        monitoredItems.add(monitoredItem);

        modifyResults[i] =
            new MonitoredItemModifyResult(
                StatusCode.GOOD,
                monitoredItem.getSamplingInterval(),
                uint(monitoredItem.getQueueSize()),
                monitoredItem.getFilterResult());
      } catch (UaException e) {
        modifyResults[i] =
            new MonitoredItemModifyResult(e.getStatusCode(), 0.0, UInteger.MIN, null);
      }
    }

    subscription.resetLifetimeCounter();

    /*
     * Notify AddressSpaces of the items we just modified.
     */

    byMonitoredItemType(
        monitoredItems,
        dataItems -> server.getAddressSpaceManager().onDataItemsModified(dataItems),
        eventItems -> server.getAddressSpaceManager().onEventItemsModified(eventItems));

    /*
     * AddressSpaces have been notified; send the response.
     */

    ResponseHeader header = createResponseHeader(request);

    return new ModifyMonitoredItemsResponse(header, modifyResults, new DiagnosticInfo[0]);
  }

  private BaseMonitoredItem<?> modifyMonitoredItem(
      MonitoredItemModifyRequest request,
      TimestampsToReturn timestamps,
      Subscription subscription,
      Map<NodeId, AttributesResponse> attributeResponses)
      throws UaException {

    UInteger itemId = request.getMonitoredItemId();
    MonitoringParameters parameters = request.getRequestedParameters();

    BaseMonitoredItem<?> monitoredItem = subscription.getMonitoredItems().get(itemId);

    if (monitoredItem == null) {
      throw new UaException(StatusCodes.Bad_MonitoredItemIdInvalid);
    }

    NodeId nodeId = monitoredItem.getReadValueId().getNodeId();
    AttributeId attributeId =
        AttributeId.from(monitoredItem.getReadValueId().getAttributeId())
            .orElseThrow(() -> new UaException(StatusCodes.Bad_AttributeIdInvalid));

    MonitoringAttributes monitoringAttributes;
    AttributesResponse response = attributeResponses.get(nodeId);
    if (response instanceof NegativeResponse negativeResponse) {
      throw new UaException(negativeResponse.statusCode());
    } else {
      monitoringAttributes = (MonitoringAttributes) response;
    }

    if (attributeId == AttributeId.EventNotifier) {
      Object filterObject =
          request.getRequestedParameters().getFilter().decode(server.getStaticEncodingContext());

      MonitoringFilter filter = validateEventItemFilter(filterObject);

      RevisedEventItemParameters revisedParameters;

      try {
        revisedParameters =
            server
                .getAddressSpaceManager()
                .onModifyEventItem(monitoredItem.getReadValueId(), parameters.getQueueSize());
      } catch (Throwable t) {
        throw new UaException(StatusCodes.Bad_InternalError, t);
      }

      monitoredItem.modify(
          timestamps,
          parameters.getClientHandle(),
          monitoredItem.getSamplingInterval(),
          filter,
          revisedParameters.revisedQueueSize(),
          parameters.getDiscardOldest());
    } else {
      MonitoringFilter filter = MonitoredDataItem.DEFAULT_FILTER;

      try {
        ExtensionObject filterXo = request.getRequestedParameters().getFilter();

        if (filterXo != null && !filterXo.isNull()) {
          Object filterObject = filterXo.decode(server.getStaticEncodingContext());

          filter = validateDataItemFilter(filterObject, attributeId, monitoringAttributes);

          // If Percent Deadband is requested and the item doesn't have euRange yet, read it from
          // the address space.
          if (filterObject instanceof DataChangeFilter dataChangeFilter
              && monitoredItem instanceof MonitoredDataItem dataItem
              && attributeId == AttributeId.Value) {
            DeadbandType deadbandType =
                DeadbandType.from(dataChangeFilter.getDeadbandType().intValue());

            if (deadbandType == DeadbandType.Percent && dataItem.getEuRange() == null) {
              // Read AnalogItemAttributes to get EURange
              Map<NodeId, AttributesResponse> analogAttrs =
                  readAnalogItemAttributes(List.of(nodeId));
              AttributesResponse analogResponse = analogAttrs.get(nodeId);

              if (analogResponse instanceof AnalogItemAttributes analogItemAttributes) {
                Range euRange = analogItemAttributes.euRange();
                if (euRange == null) {
                  throw new UaException(
                      StatusCodes.Bad_MonitoredItemFilterUnsupported,
                      "Percent Deadband requires AnalogItemType with valid EURange property");
                }
                dataItem.setEuRange(euRange);
              } else if (analogResponse instanceof NegativeResponse negResp) {
                throw new UaException(negResp.statusCode());
              } else {
                // Node is not AnalogItemType
                throw new UaException(
                    StatusCodes.Bad_MonitoredItemFilterUnsupported,
                    "Percent Deadband requires AnalogItemType with valid EURange property");
              }
            }
          }
        }
      } catch (UaSerializationException e) {
        logger.debug("error decoding MonitoringFilter", e);

        throw new UaException(StatusCodes.Bad_MonitoredItemFilterInvalid, e);
      }

      Double minimumSamplingInterval = monitoringAttributes.minimumSamplingInterval();
      if (minimumSamplingInterval == null) {
        minimumSamplingInterval = server.getConfig().getLimits().getMinSupportedSampleRate();
      }

      double requestedSamplingInterval =
          getSamplingInterval(
              subscription,
              minimumSamplingInterval,
              request.getRequestedParameters().getSamplingInterval());

      UInteger requestedQueueSize = parameters.getQueueSize();

      RevisedDataItemParameters revisedParameters;

      try {
        revisedParameters =
            server
                .getAddressSpaceManager()
                .onModifyDataItem(
                    monitoredItem.getReadValueId(), requestedSamplingInterval, requestedQueueSize);
      } catch (Throwable t) {
        throw new UaException(StatusCodes.Bad_InternalError, t);
      }

      monitoredItem.modify(
          timestamps,
          parameters.getClientHandle(),
          revisedParameters.revisedSamplingInterval(),
          filter,
          revisedParameters.revisedQueueSize(),
          parameters.getDiscardOldest());
    }

    return monitoredItem;
  }

  private double getSamplingInterval(
      Subscription subscription, Double minimumSamplingInterval, Double requestedSamplingInterval) {

    double samplingInterval = requestedSamplingInterval;

    if (requestedSamplingInterval < 0) {
      samplingInterval = subscription.getPublishingInterval();
    } else if (requestedSamplingInterval == 0) {
      if (minimumSamplingInterval < 0) {
        // Node has no opinion on the sampling interval (indeterminate)
        samplingInterval = subscription.getPublishingInterval();
      } else if (minimumSamplingInterval == 0) {
        // Node allows report-by-exception
        samplingInterval = minimumSamplingInterval;
      } else if (minimumSamplingInterval > 0) {
        // Node has a defined minimum sampling interval, use that
        // because requested rate of 0 means "fastest practical rate"
        samplingInterval = minimumSamplingInterval;
      }
    } else {
      if (requestedSamplingInterval < minimumSamplingInterval) {
        samplingInterval = minimumSamplingInterval;
      }
    }

    double minSupportedSampleRate = server.getConfig().getLimits().getMinSupportedSampleRate();
    double maxSupportedSampleRate = server.getConfig().getLimits().getMaxSupportedSampleRate();

    if (samplingInterval < minSupportedSampleRate) {
      samplingInterval = minSupportedSampleRate;
    }
    if (samplingInterval > maxSupportedSampleRate) {
      samplingInterval = maxSupportedSampleRate;
    }

    return samplingInterval;
  }

  private Map<NodeId, AttributesResponse> readMonitoringAttributes(List<NodeId> nodeIds) {
    List<ReadValueId> readValueIds =
        nodeIds.stream()
            .distinct()
            .flatMap(
                nodeId -> {
                  Function<AttributeId, ReadValueId> f =
                      id -> new ReadValueId(nodeId, id.uid(), null, QualifiedName.NULL_VALUE);

                  return Stream.of(
                      f.apply(AttributeId.NodeClass),
                      f.apply(AttributeId.EventNotifier),
                      f.apply(AttributeId.DataType),
                      f.apply(AttributeId.MinimumSamplingInterval));
                })
            .collect(toList());

    var context = new ReadContext(server, session);

    List<DataValue> values =
        server
            .getAddressSpaceManager()
            .read(context, 0.0, TimestampsToReturn.Neither, readValueIds);

    var attributesMap = new HashMap<NodeId, AttributesResponse>();

    for (int i = 0; i < values.size(); i += 4) {
      NodeId nodeId = readValueIds.get(i).getNodeId();

      DataValue dv0 = values.get(i);
      if (dv0.statusCode().isBad()) {
        attributesMap.put(nodeId, new NegativeResponse(dv0.statusCode()));
      } else {
        Object v0 = dv0.value().value();
        Object v1 = values.get(i + 1).value().value();
        Object v2 = values.get(i + 2).value().value();
        Object v3 = values.get(i + 3).value().value();

        NodeClass nodeClass = (NodeClass) v0;
        NodeId dataType = null;
        UByte eventNotifier = null;
        Double minimumSamplingInterval = null;

        if (v1 instanceof UByte b) {
          eventNotifier = b;
        }
        if (v2 instanceof NodeId id) {
          dataType = id;
        }
        if (v3 instanceof Double d) {
          minimumSamplingInterval = d;
        }

        var attributes =
            new MonitoringAttributes(nodeClass, eventNotifier, dataType, minimumSamplingInterval);

        attributesMap.put(nodeId, attributes);
      }
    }

    return attributesMap;
  }

  /**
   * Read monitoring attributes plus AnalogItem-specific metadata for nodes with Percent Deadband
   * filters. This extended read includes TypeDefinition lookup and EURange property reading, which
   * are only needed when the client explicitly requests Percent Deadband filtering. Combining these
   * reads avoids the overhead for the common case where Percent Deadband isn't used.
   *
   * @param nodeIds the nodes to read attributes for.
   * @return map of nodeId to AttributesResponse (AnalogItemAttributes if AnalogItemType, else
   *     MonitoringAttributes).
   */
  private Map<NodeId, AttributesResponse> readAnalogItemAttributes(List<NodeId> nodeIds) {
    Map<NodeId, AttributesResponse> basicAttributes = readMonitoringAttributes(nodeIds);

    List<NodeId> distinctNodeIds = nodeIds.stream().distinct().toList();
    Map<NodeId, NodeId> typeDefinitions = readTypeDefinitions(distinctNodeIds);

    var analogItemNodeIds = new ArrayList<NodeId>();
    for (var entry : typeDefinitions.entrySet()) {
      if (isAnalogItemType(entry.getValue())) {
        analogItemNodeIds.add(entry.getKey());
      }
    }

    Map<NodeId, Range> euRanges = readEURanges(analogItemNodeIds);

    var result = new HashMap<NodeId, AttributesResponse>();

    for (NodeId nodeId : nodeIds) {
      AttributesResponse attrs = basicAttributes.get(nodeId);
      Range euRange = euRanges.get(nodeId);

      if (attrs instanceof MonitoringAttributes monitoringAttrs) {
        result.put(nodeId, new AnalogItemAttributes(monitoringAttrs, euRange));
      } else if (attrs instanceof NegativeResponse negResp) {
        result.put(nodeId, negResp);
      }
    }

    return result;
  }

  /**
   * Read TypeDefinition NodeIds for multiple nodes via bulk browse operation.
   *
   * @param nodeIds the nodes to read TypeDefinitions for.
   * @return map of nodeId to typeDefinitionId (null if not found).
   */
  private Map<NodeId, NodeId> readTypeDefinitions(List<NodeId> nodeIds) {
    var browseDescriptions =
        nodeIds.stream()
            .map(
                nodeId ->
                    new BrowseDescription(
                        nodeId,
                        BrowseDirection.Forward,
                        NodeIds.HasTypeDefinition,
                        false,
                        uint(NodeClass.ObjectType.getValue() | NodeClass.VariableType.getValue()),
                        uint(BrowseResultMask.None.getValue())))
            .toArray(BrowseDescription[]::new);

    var browseRequest =
        new BrowseRequest(
            new RequestHeader(
                NodeId.NULL_VALUE, DateTime.now(), uint(0), uint(0), null, uint(0), null),
            new ViewDescription(NodeId.NULL_VALUE, DateTime.NULL_VALUE, uint(0)),
            uint(0),
            browseDescriptions);

    List<BrowseResult> results =
        BrowseHelper.browse(server, () -> Optional.of(session), browseRequest);

    var typeDefinitions = new HashMap<NodeId, NodeId>();

    for (int i = 0; i < results.size(); i++) {
      BrowseResult result = results.get(i);
      if (result.getStatusCode().isGood() && result.getReferences() != null) {
        ReferenceDescription[] references = result.getReferences();
        if (references.length > 0) {
          Optional<NodeId> targetId =
              references[0].getNodeId().toNodeId(server.getNamespaceTable());
          if (targetId.isPresent()) {
            typeDefinitions.put(nodeIds.get(i), targetId.get());
          }
        }
      }
    }

    return typeDefinitions;
  }

  /**
   * Check if the type identified by {@code typeDefinitionId} is AnalogItemType or a subtype. Uses
   * the server's VariableTypeTree, which handles caching internally.
   *
   * @param typeDefinitionId the TypeDefinition NodeId to check.
   * @return true if typeDefId is AnalogItemType or a subtype.
   */
  private boolean isAnalogItemType(NodeId typeDefinitionId) {
    return typeDefinitionId.equals(NodeIds.AnalogItemType)
        || server.getVariableTypeTree().isSubtypeOf(typeDefinitionId, NodeIds.AnalogItemType);
  }

  /**
   * Read EURange properties for multiple AnalogItem nodes in bulk.
   *
   * @param analogItemNodeIds nodes that are AnalogItemType.
   * @return map of nodeId to Range (null if EURange not found).
   */
  private Map<NodeId, Range> readEURanges(List<NodeId> analogItemNodeIds) {
    if (analogItemNodeIds.isEmpty()) {
      return Collections.emptyMap();
    }

    var browsePaths =
        analogItemNodeIds.stream().map(this::createEURangeBrowsePath).toArray(BrowsePath[]::new);

    var request =
        new TranslateBrowsePathsToNodeIdsRequest(
            new RequestHeader(
                NodeId.NULL_VALUE, DateTime.now(), uint(0), uint(0), null, uint(0), null),
            browsePaths);

    TranslateBrowsePathsToNodeIdsResponse response;
    try {
      var helper = new BrowsePathsHelper(() -> Optional.of(session), server);
      response = helper.translateBrowsePaths(request);
    } catch (UaException e) {
      logger.warn("Failed to translate EURange browse paths", e);
      return Collections.emptyMap();
    }

    var euRangeNodeIds = new ArrayList<NodeId>();
    var nodeIdMapping = new HashMap<Integer, NodeId>();

    for (int i = 0; i < Objects.requireNonNull(response.getResults()).length; i++) {
      BrowsePathResult result = response.getResults()[i];
      if (result.getStatusCode().isGood()
          && Objects.requireNonNull(result.getTargets()).length > 0) {
        BrowsePathTarget target = result.getTargets()[0];
        Optional<NodeId> euRangeNodeId = target.getTargetId().toNodeId(server.getNamespaceTable());
        if (euRangeNodeId.isPresent()) {
          nodeIdMapping.put(euRangeNodeIds.size(), analogItemNodeIds.get(i));
          euRangeNodeIds.add(euRangeNodeId.get());
        }
      }
    }

    if (euRangeNodeIds.isEmpty()) {
      return Collections.emptyMap();
    }

    List<ReadValueId> readValueIds =
        euRangeNodeIds.stream()
            .map(id -> new ReadValueId(id, AttributeId.Value.uid(), null, QualifiedName.NULL_VALUE))
            .toList();

    var context = new ReadContext(server, session);
    List<DataValue> values =
        server
            .getAddressSpaceManager()
            .read(context, 0.0, TimestampsToReturn.Neither, readValueIds);

    var euRanges = new HashMap<NodeId, Range>();
    for (int i = 0; i < values.size(); i++) {
      DataValue value = values.get(i);
      if (value.getStatusCode().isGood() && value.getValue().isNotNull()) {
        Object rangeObj = value.getValue().getValue();
        if (rangeObj instanceof Range range) {
          NodeId originalNodeId = nodeIdMapping.get(i);
          euRanges.put(originalNodeId, range);
        }
      }
    }

    return euRanges;
  }

  private BrowsePath createEURangeBrowsePath(NodeId startNodeId) {
    var element = new RelativePathElement(NodeIds.HasProperty, false, true, EU_RANGE_BROWSE_NAME);

    return new BrowsePath(startNodeId, new RelativePath(new RelativePathElement[] {element}));
  }

  public CompletableFuture<DeleteMonitoredItemsResponse> deleteMonitoredItems(
      ServiceRequestContext context, DeleteMonitoredItemsRequest request) {

    UInteger subscriptionId = request.getSubscriptionId();
    Subscription subscription = subscriptions.get(subscriptionId);
    UInteger[] itemsToDelete = request.getMonitoredItemIds();

    if (subscription == null) {
      return failedUaFuture(StatusCodes.Bad_SubscriptionIdInvalid);
    }
    if (itemsToDelete == null || itemsToDelete.length == 0) {
      return failedUaFuture(StatusCodes.Bad_NothingToDo);
    }

    var deleteResults = new StatusCode[itemsToDelete.length];
    var deletedItems = new ArrayList<BaseMonitoredItem<?>>(itemsToDelete.length);

    synchronized (subscription) {
      for (int i = 0; i < itemsToDelete.length; i++) {
        UInteger itemId = itemsToDelete[i];
        BaseMonitoredItem<?> item = subscription.getMonitoredItems().get(itemId);

        if (item == null) {
          deleteResults[i] = new StatusCode(StatusCodes.Bad_MonitoredItemIdInvalid);
        } else {
          deletedItems.add(item);

          deleteResults[i] = StatusCode.GOOD;

          monitoredItemCount.decrementAndGet();
          server.getMonitoredItemCount().decrementAndGet();
        }
      }

      subscription.removeMonitoredItems(deletedItems);
    }

    /*
     * Notify AddressSpaces of the items that have been deleted.
     */

    byMonitoredItemType(
        deletedItems,
        dataItems -> server.getAddressSpaceManager().onDataItemsDeleted(dataItems),
        eventItems -> server.getAddressSpaceManager().onEventItemsDeleted(eventItems));

    /*
     * Build and return results.
     */

    ResponseHeader header = createResponseHeader(request);

    var response = new DeleteMonitoredItemsResponse(header, deleteResults, new DiagnosticInfo[0]);

    return CompletableFuture.completedFuture(response);
  }

  public CompletableFuture<SetMonitoringModeResponse> setMonitoringMode(
      ServiceRequestContext context, SetMonitoringModeRequest request) {

    UInteger subscriptionId = request.getSubscriptionId();
    Subscription subscription = subscriptions.get(subscriptionId);
    UInteger[] itemsToModify = request.getMonitoredItemIds();

    if (subscription == null) {
      return failedUaFuture(StatusCodes.Bad_SubscriptionIdInvalid);
    }
    if (itemsToModify == null || itemsToModify.length == 0) {
      return failedUaFuture(StatusCodes.Bad_NothingToDo);
    }

    /*
     * Set MonitoringMode on each monitored item, if it exists.
     */

    MonitoringMode monitoringMode = request.getMonitoringMode();
    var results = new StatusCode[itemsToModify.length];
    var modified = new ArrayList<MonitoredItem>(itemsToModify.length);

    for (int i = 0; i < itemsToModify.length; i++) {
      UInteger itemId = itemsToModify[i];
      BaseMonitoredItem<?> item = subscription.getMonitoredItems().get(itemId);

      if (item != null) {
        item.setMonitoringMode(monitoringMode);

        modified.add(item);

        results[i] = StatusCode.GOOD;
      } else {
        results[i] = new StatusCode(StatusCodes.Bad_MonitoredItemIdInvalid);
      }
    }

    /*
     * Notify AddressSpace of the items whose MonitoringMode has been modified.
     */

    server.getAddressSpaceManager().onMonitoringModeChanged(modified);

    /*
     * Build and return results.
     */

    ResponseHeader header = createResponseHeader(request);

    var response = new SetMonitoringModeResponse(header, results, new DiagnosticInfo[0]);

    return CompletableFuture.completedFuture(response);
  }

  public CompletableFuture<PublishResponse> publish(
      ServiceRequestContext context, PublishRequest request) {
    SubscriptionAcknowledgement[] acknowledgements = request.getSubscriptionAcknowledgements();

    StatusCode[] results;

    if (acknowledgements != null) {
      results = new StatusCode[acknowledgements.length];

      for (int i = 0; i < acknowledgements.length; i++) {
        SubscriptionAcknowledgement acknowledgement = acknowledgements[i];

        UInteger sequenceNumber = acknowledgement.getSequenceNumber();
        UInteger subscriptionId = acknowledgement.getSubscriptionId();

        Subscription subscription = subscriptions.get(subscriptionId);

        if (subscription == null) {
          logger.debug(
              "Can't acknowledge sequenceNumber={} on subscriptionId={}; id not valid for this"
                  + " session",
              sequenceNumber,
              subscriptionId);
          results[i] = new StatusCode(StatusCodes.Bad_SubscriptionIdInvalid);
        } else {
          logger.debug(
              "Acknowledging sequenceNumber={} on subscriptionId={}",
              sequenceNumber,
              subscriptionId);
          results[i] = subscription.acknowledge(sequenceNumber);
        }
      }
    } else {
      results = new StatusCode[0];
    }

    PendingPublish pending = new PendingPublish(context, request, results);

    if (!transferred.isEmpty()) {
      Subscription subscription = transferred.remove(0);

      subscription.publishStatusChangeNotification(
          pending, new StatusCode(StatusCodes.Good_SubscriptionTransferred));
    } else if (subscriptions.isEmpty() && publishQueue.isWaitListEmpty()) {
      // waitList must also be empty because the last remaining subscription could have
      // expired, which removes it from bookkeeping but leaves it in the PublishQueue
      // waitList if there were no available requests to send Bad_Timeout.

      pending.responseFuture.completeExceptionally(new UaException(StatusCodes.Bad_NoSubscription));
    } else {
      publishQueue.addRequest(pending);
    }

    return pending.responseFuture;
  }

  public CompletableFuture<RepublishResponse> republish(RepublishRequest request) {
    if (subscriptions.isEmpty()) {
      return failedUaFuture(StatusCodes.Bad_SubscriptionIdInvalid);
    }

    UInteger subscriptionId = request.getSubscriptionId();
    Subscription subscription = subscriptions.get(subscriptionId);

    if (subscription == null) {
      return failedUaFuture(StatusCodes.Bad_SubscriptionIdInvalid);
    }

    UInteger sequenceNumber = request.getRetransmitSequenceNumber();
    NotificationMessage notificationMessage = subscription.republish(sequenceNumber);

    if (notificationMessage == null) {
      return failedUaFuture(StatusCodes.Bad_MessageNotAvailable);
    }

    ResponseHeader header = createResponseHeader(request);
    var response = new RepublishResponse(header, notificationMessage);

    return CompletableFuture.completedFuture(response);
  }

  public CompletableFuture<SetTriggeringResponse> setTriggering(
      ServiceRequestContext context, SetTriggeringRequest request) {

    UInteger subscriptionId = request.getSubscriptionId();
    Subscription subscription = subscriptions.get(subscriptionId);

    if (subscription == null) {
      return failedUaFuture(StatusCodes.Bad_SubscriptionIdInvalid);
    }

    UInteger triggerId = request.getTriggeringItemId();
    UInteger[] linksToAdd = requireNonNullElse(request.getLinksToAdd(), new UInteger[0]);
    UInteger[] linksToRemove = requireNonNullElse(request.getLinksToRemove(), new UInteger[0]);

    if (linksToAdd.length == 0 && linksToRemove.length == 0) {
      return failedUaFuture(StatusCodes.Bad_NothingToDo);
    }

    StatusCode[] addResults;
    StatusCode[] removeResults;

    synchronized (subscription) {
      Map<UInteger, BaseMonitoredItem<?>> itemsById = subscription.getMonitoredItems();

      BaseMonitoredItem<?> triggerItem = itemsById.get(triggerId);
      if (triggerItem == null) {
        return failedUaFuture(StatusCodes.Bad_MonitoredItemIdInvalid);
      }

      removeResults =
          Stream.of(linksToRemove)
              .map(
                  linkedItemId -> {
                    BaseMonitoredItem<?> item = itemsById.get(linkedItemId);
                    if (item != null) {
                      if (triggerItem.getTriggeredItems().remove(linkedItemId) != null) {
                        return StatusCode.GOOD;
                      } else {
                        return new StatusCode(StatusCodes.Bad_MonitoredItemIdInvalid);
                      }
                    } else {
                      return new StatusCode(StatusCodes.Bad_MonitoredItemIdInvalid);
                    }
                  })
              .toArray(StatusCode[]::new);

      addResults =
          Stream.of(linksToAdd)
              .map(
                  linkedItemId -> {
                    BaseMonitoredItem<?> linkedItem = itemsById.get(linkedItemId);
                    if (linkedItem != null) {
                      triggerItem.getTriggeredItems().put(linkedItemId, linkedItem);
                      return StatusCode.GOOD;
                    } else {
                      return new StatusCode(StatusCodes.Bad_MonitoredItemIdInvalid);
                    }
                  })
              .toArray(StatusCode[]::new);
    }

    var response =
        new SetTriggeringResponse(
            createResponseHeader(request),
            addResults,
            new DiagnosticInfo[0],
            removeResults,
            new DiagnosticInfo[0]);

    return CompletableFuture.completedFuture(response);
  }

  public void sessionClosed(boolean deleteSubscriptions) {
    Iterator<Subscription> iterator = subscriptions.values().iterator();

    while (iterator.hasNext()) {
      Subscription s = iterator.next();
      s.setStateListener(null);

      if (deleteSubscriptions) {
        server.getSubscriptions().remove(s.getId());
        server.getInternalEventBus().post(new SubscriptionDeletedEvent(s));

        List<BaseMonitoredItem<?>> deletedItems = s.deleteSubscription();

        /*
         * Notify AddressSpaces the items for this subscription are deleted.
         */

        byMonitoredItemType(
            deletedItems,
            dataItems -> server.getAddressSpaceManager().onDataItemsDeleted(dataItems),
            eventItems -> server.getAddressSpaceManager().onEventItemsDeleted(eventItems));

        monitoredItemCount.getAndUpdate(count -> count - deletedItems.size());
        server.getMonitoredItemCount().getAndUpdate(count -> count - deletedItems.size());
      }

      iterator.remove();
    }

    if (deleteSubscriptions) {
      while (publishQueue.isNotEmpty()) {
        PendingPublish pending = publishQueue.poll();
        if (pending != null) {
          pending.responseFuture.completeExceptionally(
              new UaException(StatusCodes.Bad_SessionClosed));
        }
      }
    }
  }

  /**
   * Add (transfer) {@code subscription} to this {@link SubscriptionManager}.
   *
   * @param subscription the {@link Subscription} to add.
   */
  public void addSubscription(Subscription subscription) {
    subscriptions.put(subscription.getId(), subscription);
    server.getInternalEventBus().post(new SubscriptionCreatedEvent(subscription));

    subscription.setStateListener(
        (s, ps, cs) -> {
          if (cs == State.Closing) {
            subscriptions.remove(s.getId());
            server.getSubscriptions().remove(s.getId());
            server.getInternalEventBus().post(new SubscriptionDeletedEvent(s));

            /*
             * Notify AddressSpaces the items for this subscription are deleted.
             */

            Map<UInteger, BaseMonitoredItem<?>> monitoredItems = s.getMonitoredItems();

            byMonitoredItemType(
                monitoredItems.values(),
                dataItems -> server.getAddressSpaceManager().onDataItemsDeleted(dataItems),
                eventItems -> server.getAddressSpaceManager().onEventItemsDeleted(eventItems));

            monitoredItemCount.getAndUpdate(count -> count - monitoredItems.size());
            server.getMonitoredItemCount().getAndUpdate(count -> count - monitoredItems.size());

            monitoredItems.clear();
          }
        });
  }

  /**
   * Remove (transfer from) the Subscription by {@code subscriptionId} from this {@link
   * SubscriptionManager}.
   *
   * @param subscriptionId the id of the {@link Subscription} to remove.
   * @return the removed {@link Subscription}.
   */
  public @Nullable Subscription removeSubscription(UInteger subscriptionId) {
    Subscription subscription = subscriptions.remove(subscriptionId);

    if (subscription != null) {
      server.getInternalEventBus().post(new SubscriptionDeletedEvent(subscription));

      subscription.setStateListener(null);

      monitoredItemCount.getAndUpdate(count -> count - subscription.getMonitoredItems().size());
    }

    return subscription;
  }

  public void sendStatusChangeNotification(Subscription subscription, StatusCode status) {
    PendingPublish pending = publishQueue.poll();

    if (pending != null) {
      subscription.publishStatusChangeNotification(pending, status);
    } else {
      transferred.add(subscription);
    }
  }

  // ============================================================================================
  // Response Types for Monitoring Attribute Reads
  // ============================================================================================
  //
  // This sealed type hierarchy represents the results of bulk attribute reads during monitored
  // item creation. The response type determines which specialized creation method is invoked:
  //
  //   - MonitoringAttributes → regular data items or event items
  //   - AnalogItemAttributes → AnalogItemType nodes with Percent Deadband support
  //   - NegativeResponse → attribute read failures
  //
  // The splitting logic in createMonitoredItems() ensures that expensive AnalogItem attribute
  // reads (TypeDefinition + EURange) are only performed for Value attributes with percent
  // deadband filters, optimizing the common case where Percent Deadband isn't used.
  // ============================================================================================

  /**
   * Response from bulk attribute reads for monitored item creation.
   *
   * <p>This sealed interface provides type-safe responses that flow through the monitored item
   * creation process. Pattern matching on this type in {@link #createMonitoredItem} determines
   * which specialized creation method to invoke.
   *
   * <p>Implementations:
   *
   * <ul>
   *   <li>{@link MonitoringAttributes} - Standard attributes for regular monitored items
   *   <li>{@link AnalogItemAttributes} - Extended attributes for Percent Deadband items
   *   <li>{@link NegativeResponse} - Error case when attribute reads fail
   * </ul>
   */
  sealed interface AttributesResponse
      permits MonitoringAttributes, AnalogItemAttributes, NegativeResponse {}

  /**
   * Standard monitoring attributes read from the address space.
   *
   * <p>Contains the four core attributes needed to create any monitored item: NodeClass,
   * EventNotifier, DataType, and MinimumSamplingInterval. Returned by {@link
   * #readMonitoringAttributes} for regular monitored item requests.
   *
   * <p>Used for:
   *
   * <ul>
   *   <li>Regular data item monitoring (non-Percent Deadband)
   *   <li>Event item monitoring
   *   <li>All non-Value attributes
   * </ul>
   */
  private record MonitoringAttributes(
      @Nullable NodeClass nodeClass,
      @Nullable UByte eventNotifier,
      @Nullable NodeId dataType,
      @Nullable Double minimumSamplingInterval)
      implements AttributesResponse {}

  /**
   * Extended monitoring attributes for AnalogItemType nodes with Percent Deadband filtering.
   *
   * <p>Contains the standard {@link MonitoringAttributes} plus the EURange property needed for
   * Percent Deadband calculation. Only returned by {@link #readAnalogItemAttributes} when the
   * client requests Percent Deadband filtering on a Value attribute.
   *
   * <p>The {@code euRange} field may be {@code null} if:
   *
   * <ul>
   *   <li>The node is not an AnalogItemType
   *   <li>The node is an AnalogItemType but lacks the EURange property
   *   <li>The EURange property read failed
   * </ul>
   *
   * <p>A {@code null} euRange with Percent Deadband will cause {@link #createMonitoredItem} to
   * throw {@link UaException} with {@code Bad_MonitoredItemFilterUnsupported}.
   */
  private record AnalogItemAttributes(
      MonitoringAttributes monitoringAttributes, @Nullable Range euRange)
      implements AttributesResponse {}

  /**
   * Error response when attribute reads fail.
   *
   * <p>Represents a failed bulk attribute read operation, containing the bad {@link StatusCode}
   * that caused the failure. When received by {@link #createMonitoredItem}, immediately throws
   * {@link UaException} with this status code.
   */
  private record NegativeResponse(StatusCode statusCode) implements AttributesResponse {}
}

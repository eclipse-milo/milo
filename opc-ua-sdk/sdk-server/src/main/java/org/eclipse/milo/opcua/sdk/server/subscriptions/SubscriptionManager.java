/*
 * Copyright (c) 2022 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.subscriptions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import io.netty.util.AttributeKey;
import org.eclipse.milo.opcua.sdk.core.AccessLevel;
import org.eclipse.milo.opcua.sdk.core.NumericRange;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.server.MonitoredItem;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.items.BaseMonitoredItem;
import org.eclipse.milo.opcua.sdk.server.items.DataItem;
import org.eclipse.milo.opcua.sdk.server.items.EventItem;
import org.eclipse.milo.opcua.sdk.server.items.MonitoredDataItem;
import org.eclipse.milo.opcua.sdk.server.items.MonitoredEventItem;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.services.AttributeServices.ReadContext;
import org.eclipse.milo.opcua.sdk.server.subscriptions.PublishQueue.PendingPublish;
import org.eclipse.milo.opcua.sdk.server.subscriptions.Subscription.State;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.DeadbandType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MonitoringMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateMonitoredItemsRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateMonitoredItemsResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateSubscriptionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateSubscriptionResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.DataChangeFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteMonitoredItemsRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteMonitoredItemsResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteSubscriptionsRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteSubscriptionsResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.EventFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.ModifyMonitoredItemsRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.ModifyMonitoredItemsResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ModifySubscriptionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.ModifySubscriptionResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemCreateRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemCreateResult;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemModifyRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemModifyResult;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoringFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoringParameters;
import org.eclipse.milo.opcua.stack.core.types.structured.NotificationMessage;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.RepublishRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.RepublishResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ResponseHeader;
import org.eclipse.milo.opcua.stack.core.types.structured.SetMonitoringModeRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.SetMonitoringModeResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.SetPublishingModeRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.SetPublishingModeResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.SetTriggeringRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.SetTriggeringResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.SubscriptionAcknowledgement;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.stream.Collectors.toList;
import static org.eclipse.milo.opcua.sdk.server.services.AbstractServiceSet.createResponseHeader;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.util.ConversionUtil.l;
import static org.eclipse.milo.opcua.stack.core.util.FutureUtils.failedUaFuture;

public class SubscriptionManager {

    static final AttributeKey<StatusCode[]> KEY_ACK_RESULTS = AttributeKey.valueOf("ackResults");

    private static final AtomicLong SUBSCRIPTION_IDS = new AtomicLong(0L);

    private static UInteger nextSubscriptionId() {
        return uint(SUBSCRIPTION_IDS.incrementAndGet());
    }

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

    public CompletableFuture<CreateSubscriptionResponse> createSubscription(CreateSubscriptionRequest request) {
        if (subscriptions.size() >= server.getConfig().getLimits().getMaxSubscriptionsPerSession().intValue()) {
            return failedUaFuture(StatusCodes.Bad_TooManySubscriptions);
        }

        if (server.getSubscriptions().size() >= server.getConfig().getLimits().getMaxSubscriptions().intValue()) {
            return failedUaFuture(StatusCodes.Bad_TooManySubscriptions);
        }

        UInteger subscriptionId = nextSubscriptionId();

        var subscription = new Subscription(
            this,
            subscriptionId,
            request.getRequestedPublishingInterval(),
            request.getRequestedMaxKeepAliveCount().longValue(),
            request.getRequestedLifetimeCount().longValue(),
            request.getMaxNotificationsPerPublish().longValue(),
            request.getPublishingEnabled(),
            request.getPriority().intValue()
        );

        subscriptions.put(subscriptionId, subscription);
        server.getSubscriptions().put(subscriptionId, subscription);
        server.getDiagnosticsSummary().getCumulatedSubscriptionCount().increment();
        server.getInternalEventBus().post(new SubscriptionCreatedEvent(subscription));

        subscription.setStateListener((s, ps, cs) -> {
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
                    eventItems -> server.getAddressSpaceManager().onEventItemsDeleted(eventItems)
                );

                monitoredItemCount.getAndUpdate(count -> count - monitoredItems.size());
                server.getMonitoredItemCount().getAndUpdate(count -> count - monitoredItems.size());

                monitoredItems.clear();
            }
        });

        subscription.startPublishingTimer();

        ResponseHeader header = createResponseHeader(request);

        var response = new CreateSubscriptionResponse(
            header,
            subscriptionId,
            subscription.getPublishingInterval(),
            uint(subscription.getLifetimeCount()),
            uint(subscription.getMaxKeepAliveCount())
        );

        return CompletableFuture.completedFuture(response);
    }

    public CompletableFuture<ModifySubscriptionResponse> modifySubscription(ModifySubscriptionRequest request) {
        UInteger subscriptionId = request.getSubscriptionId();
        Subscription subscription = subscriptions.get(subscriptionId);

        if (subscription == null) {
            return failedUaFuture(StatusCodes.Bad_SubscriptionIdInvalid);
        }

        subscription.modifySubscription(request);

        ResponseHeader header = createResponseHeader(request);

        var response = new ModifySubscriptionResponse(
            header,
            subscription.getPublishingInterval(),
            uint(subscription.getLifetimeCount()),
            uint(subscription.getMaxKeepAliveCount())
        );

        return CompletableFuture.completedFuture(response);
    }

    public CompletableFuture<DeleteSubscriptionsResponse> deleteSubscriptions(DeleteSubscriptionsRequest request) {
        List<UInteger> subscriptionIds = l(request.getSubscriptionIds());

        if (subscriptionIds.isEmpty()) {
            return failedUaFuture(StatusCodes.Bad_NothingToDo);
        }

        StatusCode[] results = new StatusCode[subscriptionIds.size()];

        for (int i = 0; i < subscriptionIds.size(); i++) {
            UInteger subscriptionId = subscriptionIds.get(i);
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
                    eventItems -> server.getAddressSpaceManager().onEventItemsDeleted(eventItems)
                );

                results[i] = StatusCode.GOOD;

                monitoredItemCount.getAndUpdate(count -> count - deletedItems.size());
                server.getMonitoredItemCount().getAndUpdate(count -> count - deletedItems.size());
            } else {
                results[i] = new StatusCode(StatusCodes.Bad_SubscriptionIdInvalid);
            }
        }

        ResponseHeader header = createResponseHeader(request);

        var response = new DeleteSubscriptionsResponse(
            header,
            results,
            new DiagnosticInfo[0]
        );

        while (subscriptions.isEmpty() && publishQueue.isNotEmpty()) {
            PendingPublish pending = publishQueue.poll();
            if (pending != null) {
                pending.responseFuture.completeExceptionally(
                    new UaException(StatusCodes.Bad_NoSubscription)
                );
            }
        }

        return CompletableFuture.completedFuture(response);
    }

    public CompletableFuture<SetPublishingModeResponse> setPublishingMode(SetPublishingModeRequest request) {
        List<UInteger> subscriptionIds = l(request.getSubscriptionIds());

        StatusCode[] results = new StatusCode[subscriptionIds.size()];

        for (int i = 0; i < subscriptionIds.size(); i++) {
            Subscription subscription = subscriptions.get(subscriptionIds.get(i));
            if (subscription == null) {
                results[i] = new StatusCode(StatusCodes.Bad_SubscriptionIdInvalid);
            } else {
                subscription.setPublishingMode(request);
                results[i] = StatusCode.GOOD;
            }
        }

        ResponseHeader header = createResponseHeader(request);

        var response = new SetPublishingModeResponse(
            header,
            results,
            new DiagnosticInfo[0]
        );

        return CompletableFuture.completedFuture(response);
    }

    public CompletableFuture<CreateMonitoredItemsResponse> createMonitoredItems(
        ServiceRequestContext context,
        CreateMonitoredItemsRequest request
    ) {

        UInteger subscriptionId = request.getSubscriptionId();
        Subscription subscription = subscriptions.get(subscriptionId);
        TimestampsToReturn timestamps = request.getTimestampsToReturn();
        List<MonitoredItemCreateRequest> itemsToCreate = List.of(request.getItemsToCreate());

        if (subscription == null) {
            return failedUaFuture(StatusCodes.Bad_SubscriptionIdInvalid);
        }
        if (timestamps == null) {
            return failedUaFuture(StatusCodes.Bad_TimestampsToReturnInvalid);
        }
        if (itemsToCreate.isEmpty()) {
            return failedUaFuture(StatusCodes.Bad_NothingToDo);
        }

        List<NodeId> distinctNodeIds = itemsToCreate.stream()
            .map(item -> item.getItemToMonitor().getNodeId())
            .distinct()
            .collect(toList());

        CompletableFuture<Map<NodeId, AttributeGroup>> attributesFuture = readMonitoringAttributes(distinctNodeIds);

        return attributesFuture.thenApply(attributeGroups -> {
            MonitoredItemCreateResult[] createResults = new MonitoredItemCreateResult[itemsToCreate.size()];

            List<BaseMonitoredItem<?>> monitoredItems = new ArrayList<>();

            long globalMax = server.getConfig()
                .getLimits().getMaxMonitoredItems().longValue();

            long sessionMax = server.getConfig()
                .getLimits().getMaxMonitoredItemsPerSession().longValue();

            for (int i = 0; i < itemsToCreate.size(); i++) {
                MonitoredItemCreateRequest createRequest = itemsToCreate.get(i);

                try {
                    long globalCount = server.getMonitoredItemCount().incrementAndGet();
                    long sessionCount = monitoredItemCount.incrementAndGet();

                    if (globalCount <= globalMax && sessionCount <= sessionMax) {
                        BaseMonitoredItem<?> monitoredItem = createMonitoredItem(
                            createRequest,
                            subscription,
                            timestamps,
                            attributeGroups
                        );

                        monitoredItems.add(monitoredItem);

                        createResults[i] = new MonitoredItemCreateResult(
                            StatusCode.GOOD,
                            monitoredItem.getId(),
                            monitoredItem.getSamplingInterval(),
                            uint(monitoredItem.getQueueSize()),
                            monitoredItem.getFilterResult()
                        );
                    } else {
                        throw new UaException(StatusCodes.Bad_TooManyMonitoredItems);
                    }
                } catch (UaException e) {
                    monitoredItemCount.decrementAndGet();
                    server.getMonitoredItemCount().decrementAndGet();

                    createResults[i] = new MonitoredItemCreateResult(
                        e.getStatusCode(),
                        UInteger.MIN,
                        0.0,
                        UInteger.MIN,
                        null
                    );
                }
            }

            subscription.addMonitoredItems(monitoredItems);

            // Notify AddressSpaces of the items we just created.

            byMonitoredItemType(
                monitoredItems,
                dataItems -> server.getAddressSpaceManager().onDataItemsCreated(dataItems),
                eventItems -> server.getAddressSpaceManager().onEventItemsCreated(eventItems)
            );

            ResponseHeader header = createResponseHeader(request);

            return new CreateMonitoredItemsResponse(header, createResults, new DiagnosticInfo[0]);
        });
    }

    private BaseMonitoredItem<?> createMonitoredItem(
        MonitoredItemCreateRequest request,
        Subscription subscription,
        TimestampsToReturn timestamps,
        Map<NodeId, AttributeGroup> attributeGroups
    ) throws UaException {

        NodeId nodeId = request.getItemToMonitor().getNodeId();
        UInteger attributeId = request.getItemToMonitor().getAttributeId();
        QualifiedName dataEncoding = request.getItemToMonitor().getDataEncoding();

        if (!AttributeId.isValid(attributeId)) {
            throw new UaException(StatusCodes.Bad_AttributeIdInvalid);
        }

        if (dataEncoding.isNotNull()) {
            if (!AttributeId.Value.isEqual(attributeId)) {
                throw new UaException(StatusCodes.Bad_DataEncodingInvalid);
            }

            if (!server.getEncodingManager().hasEncoding(dataEncoding)) {
                throw new UaException(StatusCodes.Bad_DataEncodingUnsupported);
            }
        }

        AttributeGroup attributeGroup = attributeGroups.get(nodeId);

        if (attributeId.equals(AttributeId.EventNotifier.uid())) {
            UByte eventNotifier = attributeGroup.getEventNotifier();

            // Verify that the SubscribeToEvents bit is set
            if (eventNotifier == null || (eventNotifier.intValue() & 1) == 0) {
                throw new UaException(StatusCodes.Bad_AttributeIdInvalid);
            }

            Object filterObject = request.getRequestedParameters()
                .getFilter()
                .decode(server.getEncodingContext());

            MonitoringFilter filter = validateEventItemFilter(filterObject, attributeGroup);

            UInteger requestedQueueSize = request.getRequestedParameters().getQueueSize();
            AtomicReference<UInteger> revisedQueueSize = new AtomicReference<>(requestedQueueSize);

            try {
                server.getAddressSpaceManager().onCreateEventItem(
                    request.getItemToMonitor(),
                    requestedQueueSize,
                    revisedQueueSize::set
                );
            } catch (Throwable t) {
                throw new UaException(StatusCodes.Bad_InternalError, t);
            }

            MonitoredEventItem monitoredEventItem = new MonitoredEventItem(
                server,
                session,
                uint(subscription.nextItemId()),
                subscription.getId(),
                request.getItemToMonitor(),
                request.getMonitoringMode(),
                timestamps,
                request.getRequestedParameters().getClientHandle(),
                0.0,
                revisedQueueSize.get(),
                request.getRequestedParameters().getDiscardOldest()
            );

            monitoredEventItem.installFilter(filter);

            return monitoredEventItem;
        } else {
            if (attributeId.equals(AttributeId.Value.uid())) {
                UByte accessLevel = attributeGroup.getAccessLevel();
                if (accessLevel == null) accessLevel = ubyte(0);

                UByte userAccessLevel = attributeGroup.getUserAccessLevel();
                if (userAccessLevel == null) userAccessLevel = ubyte(0);

                EnumSet<AccessLevel> accessLevels = AccessLevel.fromValue(accessLevel);
                EnumSet<AccessLevel> userAccessLevels = AccessLevel.fromValue(userAccessLevel);

                if (!accessLevels.contains(AccessLevel.CurrentRead)) {
                    throw new UaException(StatusCodes.Bad_NotReadable);
                }
                if (!userAccessLevels.contains(AccessLevel.CurrentRead)) {
                    throw new UaException(StatusCodes.Bad_UserAccessDenied);
                }
            }

            // Validate the requested index range by parsing it.
            String indexRange = request.getItemToMonitor().getIndexRange();
            if (indexRange != null) NumericRange.parse(indexRange);

            Double minimumSamplingInterval = -1.0;
            try {
                minimumSamplingInterval = attributeGroup.getMinimumSamplingInterval();
                if (minimumSamplingInterval == null) {
                    minimumSamplingInterval = server.getConfig().getLimits().getMinSupportedSampleRate();
                }
            } catch (UaException e) {
                if (e.getStatusCode().getValue() != StatusCodes.Bad_AttributeIdInvalid) {
                    throw e;
                }
            }

            MonitoringFilter filter = MonitoredDataItem.DEFAULT_FILTER;

            try {
                ExtensionObject filterXo = request.getRequestedParameters().getFilter();

                if (filterXo != null && !filterXo.isNull()) {
                    Object filterObject = filterXo
                        .decode(server.getEncodingContext());

                    filter = validateDataItemFilter(filterObject, attributeId, attributeGroup);
                }
            } catch (UaSerializationException e) {
                logger.debug("error decoding MonitoringFilter", e);

                throw new UaException(StatusCodes.Bad_MonitoredItemFilterInvalid, e);
            }

            double requestedSamplingInterval = getSamplingInterval(
                subscription,
                minimumSamplingInterval,
                request.getRequestedParameters().getSamplingInterval()
            );

            UInteger requestedQueueSize = request.getRequestedParameters().getQueueSize();

            AtomicReference<Double> revisedSamplingInterval = new AtomicReference<>(requestedSamplingInterval);
            AtomicReference<UInteger> revisedQueueSize = new AtomicReference<>(requestedQueueSize);

            try {
                server.getAddressSpaceManager().onCreateDataItem(
                    request.getItemToMonitor(),
                    requestedSamplingInterval,
                    requestedQueueSize,
                    (rsi, rqs) -> {
                        revisedSamplingInterval.set(rsi);
                        revisedQueueSize.set(rqs);
                    }
                );
            } catch (Throwable t) {
                throw new UaException(StatusCodes.Bad_InternalError, t);
            }

            MonitoredDataItem monitoredDataItem = new MonitoredDataItem(
                server,
                session,
                uint(subscription.nextItemId()),
                subscription.getId(),
                request.getItemToMonitor(),
                request.getMonitoringMode(),
                timestamps,
                request.getRequestedParameters().getClientHandle(),
                revisedSamplingInterval.get(),
                revisedQueueSize.get(),
                request.getRequestedParameters().getDiscardOldest()
            );

            monitoredDataItem.installFilter(filter);

            return monitoredDataItem;
        }
    }

    private MonitoringFilter validateDataItemFilter(
        Object filterObject,
        UInteger attributeId,
        AttributeGroup attributeGroup
    ) throws UaException {

        if (filterObject instanceof MonitoringFilter) {
            if (filterObject instanceof DataChangeFilter) {
                DataChangeFilter filter = (DataChangeFilter) filterObject;

                DeadbandType deadbandType = DeadbandType.from(filter.getDeadbandType().intValue());

                if (deadbandType == null) {
                    throw new UaException(StatusCodes.Bad_DeadbandFilterInvalid);
                }

                if (deadbandType == DeadbandType.Percent) {
                    // Percent deadband is not currently implemented
                    throw new UaException(StatusCodes.Bad_MonitoredItemFilterUnsupported);
                }

                if (deadbandType == DeadbandType.Absolute &&
                    !AttributeId.Value.isEqual(attributeId)) {

                    // Absolute deadband is only allowed for Value attributes
                    throw new UaException(StatusCodes.Bad_FilterNotAllowed);
                }

                if (deadbandType != DeadbandType.None) {
                    NodeId dataTypeId = null;
                    try {
                        dataTypeId = attributeGroup.getDataType();
                    } catch (UaException ignored) {
                        // noop
                    }
                    if (dataTypeId == null) {
                        dataTypeId = NodeId.NULL_VALUE;
                    }

                    if (!NodeIds.Number.equals(dataTypeId) && !subtypeOf(server, dataTypeId, NodeIds.Number)) {
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

    private MonitoringFilter validateEventItemFilter(
        Object filterObject,
        AttributeGroup attributeGroup
    ) throws UaException {

        if (filterObject instanceof MonitoringFilter) {
            if (!(filterObject instanceof EventFilter)) {
                throw new UaException(StatusCodes.Bad_FilterNotAllowed);
            }
            return (EventFilter) filterObject;
        } else {
            throw new UaException(StatusCodes.Bad_MonitoredItemFilterInvalid);
        }
    }

    public CompletableFuture<ModifyMonitoredItemsResponse> modifyMonitoredItems(
        ServiceRequestContext context,
        ModifyMonitoredItemsRequest request
    ) {

        UInteger subscriptionId = request.getSubscriptionId();
        Subscription subscription = subscriptions.get(subscriptionId);
        TimestampsToReturn timestamps = request.getTimestampsToReturn();
        List<MonitoredItemModifyRequest> itemsToModify = List.of(request.getItemsToModify());

        if (subscription == null) {
            return failedUaFuture(StatusCodes.Bad_SubscriptionIdInvalid);
        }
        if (timestamps == null) {
            return failedUaFuture(StatusCodes.Bad_TimestampsToReturnInvalid);
        }
        if (itemsToModify.isEmpty()) {
            return failedUaFuture(StatusCodes.Bad_NothingToDo);
        }

        List<NodeId> distinctNodeIds = itemsToModify.stream()
            .map(item -> {
                UInteger itemId = item.getMonitoredItemId();
                BaseMonitoredItem<?> monitoredItem = subscription.getMonitoredItems().get(itemId);
                return monitoredItem != null ? monitoredItem.getReadValueId().getNodeId() : NodeId.NULL_VALUE;
            })
            .filter(NodeId::isNotNull)
            .distinct()
            .collect(toList());

        CompletableFuture<Map<NodeId, AttributeGroup>> attributesFuture = readMonitoringAttributes(distinctNodeIds);

        return attributesFuture.thenApply(attributeGroups -> {
            MonitoredItemModifyResult[] modifyResults = new MonitoredItemModifyResult[itemsToModify.size()];

            List<BaseMonitoredItem<?>> monitoredItems = new ArrayList<>();

            for (int i = 0; i < itemsToModify.size(); i++) {
                MonitoredItemModifyRequest modifyRequest = itemsToModify.get(i);

                try {
                    BaseMonitoredItem<?> monitoredItem = modifyMonitoredItem(
                        modifyRequest,
                        timestamps,
                        subscription,
                        attributeGroups
                    );

                    monitoredItems.add(monitoredItem);

                    modifyResults[i] = new MonitoredItemModifyResult(
                        StatusCode.GOOD,
                        monitoredItem.getSamplingInterval(),
                        uint(monitoredItem.getQueueSize()),
                        monitoredItem.getFilterResult()
                    );
                } catch (UaException e) {
                    modifyResults[i] = new MonitoredItemModifyResult(
                        e.getStatusCode(),
                        0.0,
                        UInteger.MIN,
                        null
                    );
                }
            }

            subscription.resetLifetimeCounter();

            /*
             * Notify AddressSpaces of the items we just modified.
             */

            byMonitoredItemType(
                monitoredItems,
                dataItems -> server.getAddressSpaceManager().onDataItemsModified(dataItems),
                eventItems -> server.getAddressSpaceManager().onEventItemsModified(eventItems)
            );

            /*
             * AddressSpaces have been notified; send response.
             */

            ResponseHeader header = createResponseHeader(request);

            return new ModifyMonitoredItemsResponse(
                header,
                modifyResults,
                new DiagnosticInfo[0]
            );
        });
    }

    private BaseMonitoredItem<?> modifyMonitoredItem(
        MonitoredItemModifyRequest request,
        TimestampsToReturn timestamps,
        Subscription subscription,
        Map<NodeId, AttributeGroup> attributeGroups
    ) throws UaException {

        UInteger itemId = request.getMonitoredItemId();
        MonitoringParameters parameters = request.getRequestedParameters();

        BaseMonitoredItem<?> monitoredItem = subscription.getMonitoredItems().get(itemId);

        if (monitoredItem == null) {
            throw new UaException(StatusCodes.Bad_MonitoredItemIdInvalid);
        }

        NodeId nodeId = monitoredItem.getReadValueId().getNodeId();
        UInteger attributeId = monitoredItem.getReadValueId().getAttributeId();
        AttributeGroup attributeGroup = attributeGroups.get(nodeId);

        if (attributeId.equals(AttributeId.EventNotifier.uid())) {
            Object filterObject = request.getRequestedParameters()
                .getFilter()
                .decode(server.getEncodingContext());

            MonitoringFilter filter = validateEventItemFilter(filterObject, attributeGroup);

            UInteger requestedQueueSize = parameters.getQueueSize();
            AtomicReference<UInteger> revisedQueueSize = new AtomicReference<>(requestedQueueSize);

            try {
                server.getAddressSpaceManager().onModifyEventItem(
                    monitoredItem.getReadValueId(),
                    requestedQueueSize,
                    revisedQueueSize::set
                );
            } catch (Throwable t) {
                throw new UaException(StatusCodes.Bad_InternalError, t);
            }

            monitoredItem.modify(
                timestamps,
                parameters.getClientHandle(),
                monitoredItem.getSamplingInterval(),
                filter,
                revisedQueueSize.get(),
                parameters.getDiscardOldest()
            );
        } else {
            MonitoringFilter filter = MonitoredDataItem.DEFAULT_FILTER;

            try {
                ExtensionObject filterXo = request.getRequestedParameters().getFilter();

                if (filterXo != null && !filterXo.isNull()) {
                    Object filterObject = filterXo
                        .decode(server.getEncodingContext());

                    filter = validateDataItemFilter(filterObject, attributeId, attributeGroup);
                }
            } catch (UaSerializationException e) {
                logger.debug("error decoding MonitoringFilter", e);

                throw new UaException(StatusCodes.Bad_MonitoredItemFilterInvalid, e);
            }

            Double minimumSamplingInterval = -1.0;
            try {
                minimumSamplingInterval = attributeGroup.getMinimumSamplingInterval();
                if (minimumSamplingInterval == null) {
                    minimumSamplingInterval = server.getConfig().getLimits().getMinSupportedSampleRate();
                }
            } catch (UaException e) {
                long statusCodeValue = e.getStatusCode().getValue();

                if (statusCodeValue != StatusCodes.Bad_AttributeIdInvalid &&
                    statusCodeValue != StatusCodes.Bad_NodeIdUnknown) {

                    throw e;
                }
            }

            double requestedSamplingInterval = getSamplingInterval(
                subscription,
                minimumSamplingInterval,
                request.getRequestedParameters().getSamplingInterval()
            );

            UInteger requestedQueueSize = parameters.getQueueSize();

            AtomicReference<Double> revisedSamplingInterval = new AtomicReference<>(requestedSamplingInterval);
            AtomicReference<UInteger> revisedQueueSize = new AtomicReference<>(requestedQueueSize);

            try {
                server.getAddressSpaceManager().onModifyDataItem(
                    monitoredItem.getReadValueId(),
                    requestedSamplingInterval,
                    requestedQueueSize,
                    (rsi, rqs) -> {
                        revisedSamplingInterval.set(rsi);
                        revisedQueueSize.set(rqs);
                    }
                );
            } catch (Throwable t) {
                throw new UaException(StatusCodes.Bad_InternalError, t);
            }

            monitoredItem.modify(
                timestamps,
                parameters.getClientHandle(),
                revisedSamplingInterval.get(),
                filter,
                revisedQueueSize.get(),
                parameters.getDiscardOldest()
            );
        }

        return monitoredItem;
    }

    private double getSamplingInterval(
        Subscription subscription,
        Double minimumSamplingInterval,
        Double requestedSamplingInterval
    ) {

        double samplingInterval = requestedSamplingInterval;

        if (requestedSamplingInterval < 0) {
            samplingInterval = subscription.getPublishingInterval();
        } else if (requestedSamplingInterval == 0) {
            if (minimumSamplingInterval < 0) {
                // Node has no opinion on sampling interval (indeterminate)
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

    private CompletableFuture<Map<NodeId, AttributeGroup>> readMonitoringAttributes(List<NodeId> nodeIds) {
        List<ReadValueId> attributesToRead = nodeIds.stream()
            .flatMap(nodeId -> {
                Function<AttributeId, ReadValueId> f = id -> new ReadValueId(
                    nodeId,
                    id.uid(),
                    null,
                    QualifiedName.NULL_VALUE
                );

                return Stream.of(
                    f.apply(AttributeId.AccessLevel),
                    f.apply(AttributeId.UserAccessLevel),
                    f.apply(AttributeId.EventNotifier),
                    f.apply(AttributeId.MinimumSamplingInterval),
                    f.apply(AttributeId.DataType)
                );
            })
            .collect(toList());

        ReadContext context = new ReadContext(server, session);

        server.getAddressSpaceManager().read(
            context,
            0.0,
            TimestampsToReturn.Neither,
            attributesToRead
        );

        return context.getFuture().thenApply(attributeValues -> {
            Map<NodeId, AttributeGroup> monitoringAttributes = new HashMap<>();

            for (int nodeIdx = 0, attrIdx = 0; nodeIdx < nodeIds.size(); nodeIdx++, attrIdx += 5) {
                monitoringAttributes.put(nodeIds.get(nodeIdx), new AttributeGroup(
                    attributeValues.get(attrIdx),
                    attributeValues.get(attrIdx + 1),
                    attributeValues.get(attrIdx + 2),
                    attributeValues.get(attrIdx + 3),
                    attributeValues.get(attrIdx + 4)
                ));
            }

            return monitoringAttributes;
        });
    }

    public CompletableFuture<DeleteMonitoredItemsResponse> deleteMonitoredItems(
        ServiceRequestContext context,
        DeleteMonitoredItemsRequest request
    ) {

        UInteger subscriptionId = request.getSubscriptionId();
        Subscription subscription = subscriptions.get(subscriptionId);
        List<UInteger> itemsToDelete = List.of(request.getMonitoredItemIds());

        if (subscription == null) {
            return failedUaFuture(StatusCodes.Bad_SubscriptionIdInvalid);
        }
        if (itemsToDelete.isEmpty()) {
            return failedUaFuture(StatusCodes.Bad_NothingToDo);
        }

        var deleteResults = new StatusCode[itemsToDelete.size()];
        var deletedItems = new ArrayList<BaseMonitoredItem<?>>(itemsToDelete.size());

        synchronized (subscription) {
            for (int i = 0; i < itemsToDelete.size(); i++) {
                UInteger itemId = itemsToDelete.get(i);
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
            eventItems -> server.getAddressSpaceManager().onEventItemsDeleted(eventItems)
        );

        /*
         * Build and return results.
         */

        ResponseHeader header = createResponseHeader(request);

        var response = new DeleteMonitoredItemsResponse(
            header,
            deleteResults,
            new DiagnosticInfo[0]
        );

        return CompletableFuture.completedFuture(response);
    }

    public CompletableFuture<SetMonitoringModeResponse> setMonitoringMode(ServiceRequestContext context, SetMonitoringModeRequest request) {
        UInteger subscriptionId = request.getSubscriptionId();
        Subscription subscription = subscriptions.get(subscriptionId);
        List<UInteger> itemsToModify = List.of(request.getMonitoredItemIds());

        if (subscription == null) {
            return failedUaFuture(StatusCodes.Bad_SubscriptionIdInvalid);
        }
        if (itemsToModify.isEmpty()) {
            return failedUaFuture(StatusCodes.Bad_NothingToDo);
        }

        /*
         * Set MonitoringMode on each monitored item, if it exists.
         */

        MonitoringMode monitoringMode = request.getMonitoringMode();
        var results = new StatusCode[itemsToModify.size()];
        var modified = new ArrayList<MonitoredItem>(itemsToModify.size());

        for (int i = 0; i < itemsToModify.size(); i++) {
            UInteger itemId = itemsToModify.get(i);
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

        var response = new SetMonitoringModeResponse(
            header,
            results,
            new DiagnosticInfo[0]
        );

        return CompletableFuture.completedFuture(response);
    }

    public CompletableFuture<PublishResponse> publish(ServiceRequestContext context, PublishRequest request) {
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
                        "Can't acknowledge sequenceNumber={} on subscriptionId={}; id not valid for this session",
                        sequenceNumber,
                        subscriptionId
                    );
                    results[i] = new StatusCode(StatusCodes.Bad_SubscriptionIdInvalid);
                } else {
                    logger.debug("Acknowledging sequenceNumber={} on subscriptionId={}",
                        sequenceNumber,
                        subscriptionId
                    );
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
                pending,
                new StatusCode(StatusCodes.Good_SubscriptionTransferred)
            );
        } else if (subscriptions.isEmpty() && publishQueue.isWaitListEmpty()) {
            // waitList must also be empty because the last remaining subscription could have
            // expired, which removes it from bookkeeping, but leaves it in the PublishQueue
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
        ServiceRequestContext context,
        SetTriggeringRequest request
    ) {

        UInteger subscriptionId = request.getSubscriptionId();
        Subscription subscription = subscriptions.get(subscriptionId);

        if (subscription == null) {
            return failedUaFuture(StatusCodes.Bad_SubscriptionIdInvalid);
        }

        UInteger triggerId = request.getTriggeringItemId();
        List<UInteger> linksToAdd = List.of(request.getLinksToAdd());
        List<UInteger> linksToRemove = List.of(request.getLinksToRemove());

        if (linksToAdd.isEmpty() && linksToRemove.isEmpty()) {
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

            removeResults = linksToRemove.stream()
                .map(linkedItemId -> {
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

            addResults = linksToAdd.stream()
                .map(linkedItemId -> {
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

        var response = new SetTriggeringResponse(
            createResponseHeader(request),
            addResults,
            new DiagnosticInfo[0],
            removeResults,
            new DiagnosticInfo[0]
        );

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
                    eventItems -> server.getAddressSpaceManager().onEventItemsDeleted(eventItems)
                );

                monitoredItemCount.getAndUpdate(count -> count - deletedItems.size());
                server.getMonitoredItemCount().getAndUpdate(count -> count - deletedItems.size());
            }

            iterator.remove();
        }

        if (deleteSubscriptions) {
            while (publishQueue.isNotEmpty()) {
                PendingPublish pending = publishQueue.poll();
                if (pending != null) {
                    pending.responseFuture.completeExceptionally(new UaException(StatusCodes.Bad_SessionClosed));
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

        subscription.setStateListener((s, ps, cs) -> {
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
                    eventItems -> server.getAddressSpaceManager().onEventItemsDeleted(eventItems)
                );

                monitoredItemCount.getAndUpdate(count -> count - monitoredItems.size());
                server.getMonitoredItemCount().getAndUpdate(count -> count - monitoredItems.size());

                monitoredItems.clear();
            }
        });
    }

    /**
     * Remove (transfer from) the Subscription by {@code subscriptionId} from this {@link SubscriptionManager}.
     *
     * @param subscriptionId the id of the {@link Subscription} to remove.
     * @return the removed {@link Subscription}.
     */
    public Subscription removeSubscription(UInteger subscriptionId) {
        Subscription subscription = subscriptions.remove(subscriptionId);
        server.getInternalEventBus().post(new SubscriptionDeletedEvent(subscription));

        if (subscription != null) {
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

    /**
     * Split {@code monitoredItems} into a list of {@link DataItem}s and a list of {@link EventItem}s and invoke the
     * corresponding {@link Consumer} for each list if non-empty.
     *
     * @param monitoredItems    the list of MonitoredItems to group.
     * @param dataItemConsumer  a {@link Consumer} that accepts a non-empty list of {@link DataItem}s.
     * @param eventItemConsumer a {@link Consumer} that accepts a non-empty list of {@link EventItem}s.
     */
    private static void byMonitoredItemType(
        Collection<BaseMonitoredItem<?>> monitoredItems,
        Consumer<List<DataItem>> dataItemConsumer,
        Consumer<List<EventItem>> eventItemConsumer
    ) {

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

    /**
     * @return {@code true} if {@code dataTypeId} is a subtype of {@code potentialSuperTypeId}.
     */
    private static boolean subtypeOf(OpcUaServer server, NodeId dataTypeId, NodeId potentialSuperTypeId) {
        UaNode dataTypeNode = server.getAddressSpaceManager()
            .getManagedNode(dataTypeId)
            .orElse(null);

        if (dataTypeNode != null) {
            NodeId superTypeId = getSuperTypeId(server, dataTypeId);

            if (superTypeId != null) {
                return superTypeId.equals(potentialSuperTypeId) ||
                    subtypeOf(server, superTypeId, potentialSuperTypeId);
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Nullable
    private static NodeId getSuperTypeId(OpcUaServer server, NodeId dataTypeId) {
        UaNode dataTypeNode = server.getAddressSpaceManager()
            .getManagedNode(dataTypeId)
            .orElse(null);

        if (dataTypeNode != null) {
            return dataTypeNode.getReferences()
                .stream()
                .filter(Reference.SUBTYPE_OF)
                .flatMap(r -> r.getTargetNodeId().toNodeId(server.getNamespaceTable()).stream())
                .findFirst()
                .orElse(null);
        } else {
            return null;
        }
    }

    private static class AttributeGroup {
        final DataValue accessLevelValue;
        final DataValue userAccessLevelValue;
        final DataValue eventNotifierValue;
        final DataValue minimumSamplingIntervalValue;
        final DataValue dataType;

        AttributeGroup(
            DataValue accessLevelValue,
            DataValue userAccessLevelValue,
            DataValue eventNotifierValue,
            DataValue minimumSamplingIntervalValue,
            DataValue dataType
        ) {

            this.accessLevelValue = accessLevelValue;
            this.userAccessLevelValue = userAccessLevelValue;
            this.eventNotifierValue = eventNotifierValue;
            this.minimumSamplingIntervalValue = minimumSamplingIntervalValue;
            this.dataType = dataType;
        }

        @Nullable
        UByte getAccessLevel() throws UaException {
            Object value = getValue(accessLevelValue);

            if (value instanceof UByte) {
                return (UByte) value;
            } else {
                return null;
            }
        }

        @Nullable
        UByte getUserAccessLevel() throws UaException {
            Object value = getValue(userAccessLevelValue);

            if (value instanceof UByte) {
                return (UByte) value;
            } else {
                return null;
            }
        }

        @Nullable
        UByte getEventNotifier() throws UaException {
            Object value = getValue(eventNotifierValue);

            if (value instanceof UByte) {
                return (UByte) value;
            } else {
                return null;
            }
        }

        @Nullable
        Double getMinimumSamplingInterval() throws UaException {
            Object value = getValue(minimumSamplingIntervalValue);

            if (value instanceof Double) {
                return (Double) value;
            } else {
                return null;
            }
        }

        @Nullable
        NodeId getDataType() throws UaException {
            Object value = getValue(dataType);

            if (value instanceof NodeId) {
                return (NodeId) value;
            } else {
                return null;
            }
        }

        private Object getValue(DataValue dataValue) throws UaException {
            StatusCode statusCode = dataValue.getStatusCode();

            if (statusCode == null) {
                throw new UaException(StatusCode.BAD);
            }

            if (statusCode.isBad()) {
                throw new UaException(statusCode);
            }

            return dataValue.getValue().getValue();
        }
    }

}







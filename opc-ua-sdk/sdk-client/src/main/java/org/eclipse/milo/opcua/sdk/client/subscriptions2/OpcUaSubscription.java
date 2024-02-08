/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.subscriptions2;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.primitives.Ints;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MonitoringMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateMonitoredItemsResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateSubscriptionResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteMonitoredItemsResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteSubscriptionsResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.EventFieldList;
import org.eclipse.milo.opcua.stack.core.types.structured.ModifyMonitoredItemsResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ModifySubscriptionResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemCreateResult;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemModifyResult;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemNotification;
import org.eclipse.milo.opcua.stack.core.types.structured.SetMonitoringModeResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.SetPublishingModeResponse;
import org.eclipse.milo.opcua.stack.core.util.Lazy;
import org.eclipse.milo.opcua.stack.core.util.Lists;
import org.eclipse.milo.opcua.stack.core.util.TaskQueue;
import org.eclipse.milo.opcua.stack.core.util.Unit;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.Objects.requireNonNull;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.util.FutureUtils.supplyAsyncCompose;

public class OpcUaSubscription {

    private static final int DEFAULT_MAX_MONITORED_ITEMS_PER_CALL = 10000;
    private static final UInteger DEFAULT_MAX_NOTIFICATIONS_PER_PUBLISH = uint(65535);
    private static final UByte DEFAULT_PRIORITY = ubyte(0);
    private static final double DEFAULT_PUBLISHING_INTERVAL = 1000.0;
    private static final double DEFAULT_TARGET_KEEP_ALIVE_INTERVAL = 10000.0;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private SyncState syncState = SyncState.INITIAL;

    private ServerState serverState;

    private WatchdogTimer watchdogTimer;

    private final AtomicReference<Modifications> modifications = new AtomicReference<>(null);

    /**
     * MonitoredItems added to this Subscription, by ClientHandle.
     */
    private final Map<UInteger, OpcUaMonitoredItem> monitoredItems = new ConcurrentHashMap<>();

    /**
     * MonitoredItems that have been removed from the Subscription and are pending deletion on the
     * Server.
     */
    private List<OpcUaMonitoredItem> itemsToDelete = new ArrayList<>();

    private final ClientHandleSequence clientHandleSequence =
        new ClientHandleSequence(monitoredItems::containsKey);

    private Double publishingInterval = DEFAULT_PUBLISHING_INTERVAL;
    private UInteger maxKeepAliveCount = calculateMaxKeepAliveCount(
        publishingInterval,
        DEFAULT_TARGET_KEEP_ALIVE_INTERVAL
    );
    private UInteger lifetimeCount = calculateLifetimeCount(maxKeepAliveCount);
    private UInteger maxNotificationsPerPublish = DEFAULT_MAX_NOTIFICATIONS_PER_PUBLISH;
    private UByte priority = DEFAULT_PRIORITY;

    private boolean lifetimeAndKeepAliveCalculated = true;

    private UInteger maxMonitoredItemsPerCall = uint(DEFAULT_MAX_MONITORED_ITEMS_PER_CALL);
    private final Lazy<UInteger> monitoredItemPartitionSize = new Lazy<>();

    private @Nullable SubscriptionListener listener;

    private final TaskQueue deliveryQueue;

    private final OpcUaClient client;

    public OpcUaSubscription(OpcUaClient client) {
        this.client = client;

        deliveryQueue = new TaskQueue(client.getTransport().getConfig().getExecutor());
    }

    public OpcUaSubscription(OpcUaClient client, double publishingInterval) {
        this.client = client;
        this.publishingInterval = publishingInterval;

        deliveryQueue = new TaskQueue(client.getTransport().getConfig().getExecutor());
    }

    /**
     * Get the client this Subscription belongs to.
     *
     * @return the {@link OpcUaClient} this Subscription belongs to.
     */
    public OpcUaClient getClient() {
        return client;
    }

    //region Subscription Management

    public void create() throws UaException {
        if (syncState == SyncState.INITIAL) {
            if (maxKeepAliveCount == null) {
                maxKeepAliveCount = calculateMaxKeepAliveCount(
                    publishingInterval,
                    DEFAULT_TARGET_KEEP_ALIVE_INTERVAL
                );
            }
            if (lifetimeCount == null) {
                lifetimeCount = calculateLifetimeCount(maxKeepAliveCount);
            }

            CreateSubscriptionResponse response = client.createSubscription(
                publishingInterval,
                lifetimeCount,
                maxKeepAliveCount,
                maxNotificationsPerPublish,
                true,
                priority
            );

            syncState = SyncState.SYNCHRONIZED;

            serverState = new ServerState(
                response.getSubscriptionId(),
                response.getRevisedPublishingInterval(),
                response.getRevisedLifetimeCount(),
                response.getRevisedMaxKeepAliveCount(),
                maxNotificationsPerPublish,
                priority,
                true
            );

            watchdogTimer = new WatchdogTimer(client.getConfig().getSubscriptionWatchdogMultiplier());
            kickWatchdogTimer();

            client.getPublishingManager().addSubscription(this);
        } else {
            throw new UaException(StatusCodes.Bad_InvalidState);
        }
    }

    public CompletionStage<Unit> createAsync() {
        return supplyAsyncCompose(() -> {
            try {
                create();
                return CompletableFuture.completedFuture(Unit.VALUE);
            } catch (UaException e) {
                return CompletableFuture.failedFuture(e);
            }
        }, client.getTransport().getConfig().getExecutor());
    }

    public void modify() throws UaException {
        if (syncState == SyncState.INITIAL) {
            throw new UaException(StatusCodes.Bad_InvalidState);
        } else if (syncState == SyncState.UNSYNCHRONIZED) {
            Modifications diff = modifications.getAndSet(null);
            assert diff != null;

            ModifySubscriptionResponse response = client.modifySubscription(
                serverState.getSubscriptionId(),
                diff.publishingInterval().orElse(serverState.getPublishingInterval()),
                diff.lifetimeCount().orElse(serverState.getLifetimeCount()),
                diff.maxKeepAliveCount().orElse(serverState.getMaxKeepAliveCount()),
                diff.maxNotificationsPerPublish().orElse(maxNotificationsPerPublish),
                diff.priority().orElse(priority)
            );

            kickWatchdogTimer();

            serverState = new ServerState(
                serverState.getSubscriptionId(),
                response.getRevisedPublishingInterval(),
                response.getRevisedLifetimeCount(),
                response.getRevisedMaxKeepAliveCount(),
                maxNotificationsPerPublish,
                priority,
                serverState.isPublishingEnabled()
            );

            syncState = SyncState.SYNCHRONIZED;

            if (modifications.get() == null) {
                syncState = SyncState.SYNCHRONIZED;
            }
        }
    }

    public CompletionStage<Unit> modifyAsync() {
        return supplyAsyncCompose(() -> {
            try {
                modify();
                return CompletableFuture.completedFuture(Unit.VALUE);
            } catch (UaException e) {
                return CompletableFuture.failedFuture(e);
            }
        }, client.getTransport().getConfig().getExecutor());
    }

    public void delete() throws UaException {
        if (syncState != SyncState.INITIAL) {
            DeleteSubscriptionsResponse response =
                client.deleteSubscriptions(List.of(serverState.getSubscriptionId()));

            StatusCode[] results = requireNonNull(response.getResults());

            if (results[0].isGood()) {
                serverState = null;
                syncState = SyncState.INITIAL;

                WatchdogTimer watchdog = this.watchdogTimer;
                if (watchdog != null) {
                    watchdog.cancel();
                    this.watchdogTimer = null;
                }

                client.getPublishingManager().removeSubscription(this);
            } else {
                throw new UaException(results[0]);
            }
        }
    }

    public CompletionStage<Unit> deleteAsync() {
        return supplyAsyncCompose(() -> {
            try {
                delete();
                return CompletableFuture.completedFuture(Unit.VALUE);
            } catch (UaException e) {
                return CompletableFuture.failedFuture(e);
            }
        }, client.getTransport().getConfig().getExecutor());
    }

    //endregion

    //region MonitoredItem Management

    public void addMonitoredItem(OpcUaMonitoredItem item) {
        if (!monitoredItems.containsValue(item)) {
            UInteger clientHandle = clientHandleSequence.nextClientHandle();
            item.setClientHandle(clientHandle);
            item.setSubscription(this);

            monitoredItems.put(clientHandle, item);
        }
    }

    public void addMonitoredItems(List<OpcUaMonitoredItem> items) {
        items.forEach(this::addMonitoredItem);
    }

    public void removeMonitoredItem(OpcUaMonitoredItem item) {
        OpcUaMonitoredItem removedItem =
            item.getClientHandle().map(monitoredItems::remove).orElse(null);

        if (removedItem != null) {
            removedItem.setSubscription(null);
            removedItem.setClientHandle(null);
            itemsToDelete.add(removedItem);
            syncState = SyncState.UNSYNCHRONIZED;
        }
    }

    public void removeMonitoredItems(List<OpcUaMonitoredItem> items) {
        items.forEach(this::removeMonitoredItem);
    }

    /**
     * Synchronize the Subscription's MonitoredItems with the Server.
     * <p>
     * This is a compound operation that deletes any MonitoredItems that have been removed from
     * the Subscription, modifies any existing MonitoredItems that have been changed, and creates
     * any new MonitoredItems that have been added but not yet created on the Server.
     *
     * @return the total number of MonitoredItems that were deleted, modified, or created.
     * @throws MonitoredItemSynchronizationException if one or more MonitoredItems failed to
     *     synchronize for any reason. This could be a service-level failure or an operation-level
     *     failure. Check the {@link MonitoredItemOperationResult}s for details.
     */
    public long synchronizeMonitoredItems() throws MonitoredItemSynchronizationException {
        List<MonitoredItemOperationResult> deleteResults = deleteMonitoredItems();
        List<MonitoredItemOperationResult> modifyResults = modifyMonitoredItems();
        List<MonitoredItemOperationResult> createResults = createMonitoredItems();

        if (Stream.of(deleteResults, modifyResults, createResults)
            .flatMap(List::stream)
            .anyMatch(r -> !r.serviceResult().isGood() || !r.operationResult().orElseThrow().isGood())) {

            throw new MonitoredItemSynchronizationException(
                "failed to synchronize one or more MonitoredItems",
                createResults,
                modifyResults,
                deleteResults
            );
        }

        return deleteResults.size() + modifyResults.size() + createResults.size();
    }

    /**
     * Create any MonitoredItems that have been added to the Subscription but not yet created on
     * the Server.
     *
     * @return a List of {@link MonitoredItemOperationResult}s that contain the MonitoredItem and
     *     the service- and operation-level results associated with the attempt to create it.
     */
    public List<MonitoredItemOperationResult> createMonitoredItems() {
        List<OpcUaMonitoredItem> itemsToCreate = monitoredItems.values()
            .stream()
            .filter(item -> item.getSyncState() == OpcUaMonitoredItem.SyncState.INITIAL)
            .collect(Collectors.toList());

        if (!itemsToCreate.isEmpty()) {
            return createMonitoredItems(itemsToCreate);
        } else {
            return Collections.emptyList();
        }
    }

    private List<MonitoredItemOperationResult> createMonitoredItems(List<OpcUaMonitoredItem> itemsToCreate) {
        var serviceOperationsResults = new ArrayList<MonitoredItemOperationResult>(itemsToCreate.size());

        UInteger partitionSize = getMonitoredItemPartitionSize();

        List<List<OpcUaMonitoredItem>> partitions =
            Lists.partition(itemsToCreate, partitionSize.intValue())
                .collect(Collectors.toList());

        for (List<OpcUaMonitoredItem> partition : partitions) {
            try {
                logger.debug("createMonitoredItems partition.size(): {}", partition.size());

                CreateMonitoredItemsResponse response = client.createMonitoredItems(
                    serverState.getSubscriptionId(),
                    TimestampsToReturn.Both,
                    partition.stream()
                        .map(OpcUaMonitoredItem::newCreateRequest)
                        .collect(Collectors.toList())
                );

                MonitoredItemCreateResult[] results = requireNonNull(response.getResults());

                for (int i = 0; i < results.length; i++) {
                    MonitoredItemCreateResult result = results[i];
                    OpcUaMonitoredItem monitoredItem = partition.get(i);

                    monitoredItem.applyCreateResult(result);

                    serviceOperationsResults.add(
                        new MonitoredItemOperationResult(monitoredItem, StatusCode.GOOD, result.getStatusCode())
                    );
                }
            } catch (UaException e) {
                for (OpcUaMonitoredItem item : partition) {
                    serviceOperationsResults.add(
                        new MonitoredItemOperationResult(item, e.getStatusCode(), null)
                    );
                }
            }
        }

        return serviceOperationsResults;
    }

    /**
     * Modify any MonitoredItems that have been changed.
     *
     * @return a List of the MonitoredItems that were modified.
     */
    public List<MonitoredItemOperationResult> modifyMonitoredItems() {
        List<OpcUaMonitoredItem> itemsToModify = monitoredItems.values()
            .stream()
            .filter(item -> item.getSyncState() == OpcUaMonitoredItem.SyncState.UNSYNCHRONIZED)
            .collect(Collectors.toList());

        if (!itemsToModify.isEmpty()) {
            return modifyMonitoredItems(itemsToModify);
        } else {
            return Collections.emptyList();
        }
    }

    private List<MonitoredItemOperationResult> modifyMonitoredItems(List<OpcUaMonitoredItem> itemsToModify) {
        var serviceOperationsResults = new ArrayList<MonitoredItemOperationResult>(itemsToModify.size());

        UInteger partitionSize = getMonitoredItemPartitionSize();

        List<List<OpcUaMonitoredItem>> partitions =
            Lists.partition(itemsToModify, partitionSize.intValue())
                .collect(Collectors.toList());

        for (List<OpcUaMonitoredItem> partition : partitions) {
            try {
                logger.debug("modifyMonitoredItems partition.size(): {}", partition.size());

                ModifyMonitoredItemsResponse response = client.modifyMonitoredItems(
                    serverState.getSubscriptionId(),
                    TimestampsToReturn.Both,
                    partition.stream()
                        .map(OpcUaMonitoredItem::newModifyRequest)
                        .collect(Collectors.toList())
                );

                MonitoredItemModifyResult[] results = requireNonNull(response.getResults());

                for (int i = 0; i < results.length; i++) {
                    MonitoredItemModifyResult result = results[i];
                    OpcUaMonitoredItem monitoredItem = partition.get(i);

                    monitoredItem.applyModifyResult(result);

                    serviceOperationsResults.add(
                        new MonitoredItemOperationResult(monitoredItem, StatusCode.GOOD, result.getStatusCode())
                    );
                }
            } catch (UaException e) {
                for (OpcUaMonitoredItem item : partition) {
                    serviceOperationsResults.add(
                        new MonitoredItemOperationResult(item, e.getStatusCode(), null)
                    );
                }
            }
        }

        return serviceOperationsResults;
    }

    /**
     * Delete any MonitoredItems that have been removed from the Subscription.
     *
     * @return a List of the MonitoredItems that were deleted.
     */
    public List<MonitoredItemOperationResult> deleteMonitoredItems() {
        List<OpcUaMonitoredItem> itemsToDelete = this.itemsToDelete;
        this.itemsToDelete = new ArrayList<>();

        if (!itemsToDelete.isEmpty()) {
            return deleteMonitoredItems(itemsToDelete);
        } else {
            return Collections.emptyList();
        }
    }

    private List<MonitoredItemOperationResult> deleteMonitoredItems(List<OpcUaMonitoredItem> itemsToDelete) {
        var serviceOperationsResults = new ArrayList<MonitoredItemOperationResult>(itemsToDelete.size());

        UInteger partitionSize = getMonitoredItemPartitionSize();

        List<List<OpcUaMonitoredItem>> partitions =
            Lists.partition(itemsToDelete, partitionSize.intValue())
                .collect(Collectors.toList());

        for (List<OpcUaMonitoredItem> partition : partitions) {
            try {
                logger.debug("deleteMonitoredItems partition.size(): {}", partition.size());

                DeleteMonitoredItemsResponse response = client.deleteMonitoredItems(
                    serverState.getSubscriptionId(),
                    partition.stream()
                        .map(item -> item.getMonitoredItemId().orElseThrow())
                        .collect(Collectors.toList())
                );

                StatusCode[] results = requireNonNull(response.getResults());

                for (int i = 0; i < results.length; i++) {
                    StatusCode result = results[i];

                    partition.get(i).applyDeleteResult(result);

                    serviceOperationsResults.add(
                        new MonitoredItemOperationResult(partition.get(i), StatusCode.GOOD, result)
                    );
                }
            } catch (UaException e) {
                for (OpcUaMonitoredItem item : partition) {
                    serviceOperationsResults.add(
                        new MonitoredItemOperationResult(item, e.getStatusCode(), null)
                    );
                }
            }
        }

        return serviceOperationsResults;
    }

    private UInteger getMonitoredItemPartitionSize() {
        return monitoredItemPartitionSize.getOrCompute(() -> {
            UInteger serverMaxMonitoredItemsPerCall = null;

            try {
                List<DataValue> values = client.readValues(
                    0.0,
                    TimestampsToReturn.Neither,
                    List.of(NodeIds.Server_ServerCapabilities_OperationLimits_MaxMonitoredItemsPerCall)
                );

                Object value = values.get(0).getValue().getValue();
                if (value instanceof UInteger) {
                    serverMaxMonitoredItemsPerCall = (UInteger) value;
                }
            } catch (UaException e) {
                logger.warn("Failed to read MaxMonitoredItemsPerCall from Server", e);
            }

            if (serverMaxMonitoredItemsPerCall == null) {
                serverMaxMonitoredItemsPerCall = maxMonitoredItemsPerCall;
            }

            int configuredMax = Ints.saturatedCast(maxMonitoredItemsPerCall.longValue());
            int serverMax = Ints.saturatedCast(serverMaxMonitoredItemsPerCall.longValue());

            return uint(Math.min(configuredMax, serverMax));
        });
    }

    //endregion

    //region MonitoringMode Management

    /**
     * Set the {@link MonitoringMode} for a group of MonitoredItems.
     * <p>
     * These MonitoredItems must exist on the Server before setting the MonitoringMode.
     *
     * @param monitoringMode the MonitoringMode to set.
     * @param monitoredItems the MonitoredItems to set the MonitoringMode for.
     * @return a List of {@link MonitoredItemOperationResult}s that contain the MonitoredItem and
     *     the service- and operation-level results associated with the attempt to set the
     *     MonitoringMode.
     */
    public List<MonitoredItemOperationResult> setMonitoringMode(
        MonitoringMode monitoringMode,
        List<OpcUaMonitoredItem> monitoredItems
    ) {

        if (monitoredItems.isEmpty()) {
            return Collections.emptyList();
        }

        if (monitoredItems.stream()
            .anyMatch(item -> item.getSyncState() == OpcUaMonitoredItem.SyncState.INITIAL)) {

            throw new IllegalArgumentException("MonitoredItems must exist before setting MonitoringMode");
        }

        var serviceOperationResults = new ArrayList<MonitoredItemOperationResult>(monitoredItems.size());

        UInteger partitionSize = getMonitoredItemPartitionSize();

        List<List<OpcUaMonitoredItem>> partitions =
            Lists.partition(monitoredItems, partitionSize.intValue())
                .collect(Collectors.toList());

        for (List<OpcUaMonitoredItem> partition : partitions) {
            try {
                logger.debug("setMonitoringMode partition.size(): {}", partition.size());

                SetMonitoringModeResponse response = client.setMonitoringMode(
                    serverState.getSubscriptionId(),
                    monitoringMode,
                    partition.stream()
                        .map(item -> item.getMonitoredItemId().orElseThrow())
                        .collect(Collectors.toList())
                );

                StatusCode[] results = requireNonNull(response.getResults());

                for (int i = 0; i < results.length; i++) {
                    StatusCode result = results[i];

                    OpcUaMonitoredItem item = partition.get(i);

                    item.applySetMonitoringModeResult(result);
                    if (result.isGood()) {
                        item.setMonitoringMode(monitoringMode);
                    }
                    serviceOperationResults.add(
                        new MonitoredItemOperationResult(item, StatusCode.GOOD, result)
                    );
                }
            } catch (UaException e) {
                for (OpcUaMonitoredItem item : partition) {
                    item.applySetMonitoringModeResult(e.getStatusCode());

                    serviceOperationResults.add(
                        new MonitoredItemOperationResult(item, e.getStatusCode(), null)
                    );
                }
            }
        }

        return serviceOperationResults;
    }

    //endregion

    //region Publishing Management

    /**
     * Set the publishing mode, i.e. enable or disable publishing, for this Subscription.
     *
     * @param enabled {@code true} to enable publishing, {@code false} to disable publishing.
     * @throws UaException if a service- or operation-level error occurs.
     */
    public void setPublishingMode(boolean enabled) throws UaException {
        if (syncState == SyncState.INITIAL) {
            throw new UaException(StatusCodes.Bad_InvalidState);
        } else {
            SetPublishingModeResponse response = client.setPublishingMode(
                enabled,
                List.of(serverState.getSubscriptionId())
            );

            StatusCode result = requireNonNull(response.getResults())[0];

            if (result.isGood()) {
                serverState = new ServerState(
                    serverState.getSubscriptionId(),
                    serverState.getPublishingInterval(),
                    serverState.getLifetimeCount(),
                    serverState.getMaxKeepAliveCount(),
                    maxNotificationsPerPublish,
                    priority,
                    enabled
                );
            } else {
                throw new UaException(result);
            }
        }
    }

    public CompletionStage<Unit> setPublishingModeAsync(boolean enabled) {
        return supplyAsyncCompose(() -> {
            try {
                setPublishingMode(enabled);
                return CompletableFuture.completedFuture(Unit.VALUE);
            } catch (UaException e) {
                return CompletableFuture.failedFuture(e);
            }
        }, client.getTransport().getConfig().getExecutor());
    }

    //endregion

    public SyncState getSyncState() {
        return syncState;
    }

    public Optional<ServerState> getServerState() {
        return Optional.ofNullable(serverState);
    }

    /**
     * Get a list of the MonitoredItems belonging to this Subscription.
     * <p>
     * If {@link #synchronizeMonitoredItems()} has not been called since adding or removing
     * MonitoredItems, this List may not represent the server's view of the MonitoredItems.
     *
     * @return a List of the MonitoredItems belonging to this Subscription.
     */
    public List<OpcUaMonitoredItem> getMonitoredItems() {
        return List.copyOf(monitoredItems.values());
    }

    public Double getPublishingInterval() {
        return publishingInterval;
    }

    public UInteger getLifetimeCount() {
        return lifetimeCount;
    }

    public UInteger getMaxKeepAliveCount() {
        return maxKeepAliveCount;
    }

    /**
     * Get the Subscription's Priority setting.
     * <p>
     * The Server does not revise this setting, so the value reflects the most recently requested
     * by a create or modify operation.
     *
     * @return the Subscription's Priority setting.
     */
    public UByte getPriority() {
        return priority;
    }

    /**
     * Get the Subscription's MaxNotificationsPerPublish setting.
     * <p>
     * The Server does not revise this setting, so the value reflects the most recently requested
     * by a create or modify operation.
     *
     * @return the Subscription's MaxNotificationsPerPublish setting.
     */
    public UInteger getMaxNotificationsPerPublish() {
        return maxNotificationsPerPublish;
    }

    /**
     * Get whether publishing is enabled for this Subscription.
     * <p>
     * This is available only after the Subscription has been created.
     *
     * @return {@code true} if publishing is enabled for this Subscription.
     */
    public Optional<Boolean> isPublishingEnabled() {
        return getServerState().map(ServerState::isPublishingEnabled);
    }

    /**
     * Get the SubscriptionId assigned to this Subscription by the Server.
     * <p>
     * The SubscriptionId is available only after the Subscription has been created.
     *
     * @return the SubscriptionId assigned to this Subscription by the Server.
     */
    public Optional<UInteger> getSubscriptionId() {
        return getServerState().map(ServerState::getSubscriptionId);
    }

    /**
     * Get the revised PublishingInterval from the most recent create or modify operation.
     * <p>
     * The revised PublishingInterval is available only after the Subscription has been created
     * or modified.
     *
     * @return the revised PublishingInterval from the most recent create or modify operation.
     */
    public Optional<Double> getRevisedPublishingInterval() {
        return getServerState().map(ServerState::getPublishingInterval);
    }

    /**
     * Get the revised LifetimeCount from the most recent create or modify operation.
     * <p>
     * The revised LifetimeCount is available only after the Subscription has been created or
     * modified.
     *
     * @return the revised LifetimeCount from the most recent create or modify operation.
     */
    public Optional<UInteger> getRevisedLifetimeCount() {
        return getServerState().map(ServerState::getLifetimeCount);
    }

    /**
     * Get the revised MaxKeepAliveCount from the most recent create or modify operation.
     * <p>
     * The revised MaxKeepAliveCount is available only after the Subscription has been created
     * or modified.
     *
     * @return the revised MaxKeepAliveCount from the most recent create or modify operation.
     */
    public Optional<UInteger> getRevisedMaxKeepAliveCount() {
        return getServerState().map(ServerState::getMaxKeepAliveCount);
    }

    /**
     * Set a new PublishingInterval for this Subscription.
     * <p>
     * If the Subscription has not yet been created, this will be the PublishingInterval used
     * during the create service call.
     * <p>
     * If the Subscription has already been created, this will be the PublishingInterval used
     * during the next modify service call.
     *
     * @param publishingInterval the new PublishingInterval.
     * @see #create()
     * @see #createAsync()
     * @see #modify()
     * @see #modifyAsync()
     */
    public void setPublishingInterval(Double publishingInterval) {
        this.publishingInterval = publishingInterval;

        Modifications diff = modifications.updateAndGet(
            d ->
                Objects.requireNonNullElseGet(d, Modifications::new)
        );

        diff.publishingInterval = publishingInterval;

        syncState = SyncState.UNSYNCHRONIZED;

        if (lifetimeAndKeepAliveCalculated) {
            UInteger maxKeepAliveCount = calculateMaxKeepAliveCount(
                publishingInterval,
                DEFAULT_TARGET_KEEP_ALIVE_INTERVAL
            );
            UInteger lifetimeCount = calculateLifetimeCount(maxKeepAliveCount);

            setMaxKeepAliveCount(maxKeepAliveCount);
            setLifetimeCount(lifetimeCount);
        }
    }

    /**
     * Set a new LifetimeCount for this Subscription.
     * <p>
     * If the Subscription has not yet been created, this will be the LifetimeCount used during
     * the create service call.
     * <p>
     * If the Subscription has already been created, this will be the LifetimeCount used during
     * the next modify service call.
     *
     * @param lifetimeCount the new LifetimeCount.
     * @see #create()
     * @see #createAsync()
     * @see #modify()
     * @see #modifyAsync()
     */
    public void setLifetimeCount(UInteger lifetimeCount) {
        this.lifetimeCount = lifetimeCount;

        Modifications diff = modifications.updateAndGet(
            d ->
                Objects.requireNonNullElseGet(d, Modifications::new)
        );

        diff.lifetimeCount = lifetimeCount;

        syncState = SyncState.UNSYNCHRONIZED;
    }

    /**
     * Set a new MaxKeepAliveCount for this Subscription.
     * <p>
     * If the Subscription has not yet been created, this will be the MaxKeepAliveCount used during
     * the create service call.
     * <p>
     * If the Subscription has already been created, this will be the MaxKeepAliveCount used during
     * the next modify service call.
     *
     * @param maxKeepAliveCount the new MaxKeepAliveCount.
     * @see #create()
     * @see #createAsync()
     * @see #modify()
     * @see #modifyAsync()
     */
    public void setMaxKeepAliveCount(UInteger maxKeepAliveCount) {
        this.maxKeepAliveCount = maxKeepAliveCount;

        Modifications diff = modifications.updateAndGet(
            d ->
                Objects.requireNonNullElseGet(d, Modifications::new)
        );

        diff.maxKeepAliveCount = maxKeepAliveCount;

        syncState = SyncState.UNSYNCHRONIZED;
    }

    /**
     * Set a new Priority for this Subscription.
     * <p>
     * If the Subscription has not yet been created, this will be the Priority used during the
     * create service call.
     * <p>
     * If the Subscription has already been created, this will be the Priority used during the
     * next modify service call.
     *
     * @param priority the new Priority.
     * @see #create()
     * @see #createAsync()
     * @see #modify()
     * @see #modifyAsync()
     */
    public void setPriority(UByte priority) {
        this.priority = priority;

        Modifications diff = modifications.updateAndGet(
            d ->
                Objects.requireNonNullElseGet(d, Modifications::new)
        );

        diff.priority = priority;

        syncState = SyncState.UNSYNCHRONIZED;
    }

    /**
     * Set a new MaxNotificationsPerPublish for this Subscription.
     * <p>
     * If the Subscription has not yet been created, this will be the MaxNotificationsPerPublish
     * used during the create service call.
     * <p>
     * If the Subscription has already been created, this will be the MaxNotificationsPerPublish
     * used during the next modify service call.
     *
     * @param maxNotificationsPerPublish the new MaxNotificationsPerPublish.
     * @see #create()
     * @see #createAsync()
     * @see #modify()
     * @see #modifyAsync()
     */
    public void setMaxNotificationsPerPublish(UInteger maxNotificationsPerPublish) {
        this.maxNotificationsPerPublish = maxNotificationsPerPublish;

        Modifications diff = modifications.updateAndGet(
            d ->
                Objects.requireNonNullElseGet(d, Modifications::new)
        );

        diff.maxNotificationsPerPublish = maxNotificationsPerPublish;

        syncState = SyncState.UNSYNCHRONIZED;
    }

    /**
     * Set whether the LifetimeCount and MaxKeepAliveCount should be calculated automatically any
     * time the PublishingInterval is set.
     *
     * @param lifetimeAndKeepAliveCalculated {@code true} if the LifetimeCount and
     *     MaxKeepAliveCount should be calculated automatically.
     * @see #isLifetimeAndKeepAliveCalculated()
     */
    public void setLifetimeAndKeepAliveCalculated(boolean lifetimeAndKeepAliveCalculated) {
        this.lifetimeAndKeepAliveCalculated = lifetimeAndKeepAliveCalculated;
    }

    /**
     * @return {@code true} if the LifetimeCount and MaxKeepAliveCount are calculated
     *     automatically any time the Publishing Interval is set.
     * @see #setLifetimeAndKeepAliveCalculated(boolean)
     */
    public boolean isLifetimeAndKeepAliveCalculated() {
        return lifetimeAndKeepAliveCalculated;
    }

    /**
     * Set the target keep-alive interval, in milliseconds, for this Subscription.
     * <p>
     * The Subscription must be configured to automatically calculate the Lifetime and KeepAlive
     * for this to have any effect. MaxKeepAliveCount and LifetimeCount will be recalculated based
     * on the target value. The Subscription settings must be synchronized before this change
     * takes effect.
     *
     * @param targetKeepAliveInterval the target keep-alive interval, in milliseconds.
     * @see #isLifetimeAndKeepAliveCalculated()
     * @see #setLifetimeAndKeepAliveCalculated(boolean)
     */
    public void setTargetKeepAliveInterval(double targetKeepAliveInterval) {
        if (isLifetimeAndKeepAliveCalculated()) {
            setMaxKeepAliveCount(calculateMaxKeepAliveCount(publishingInterval, targetKeepAliveInterval));
            setLifetimeCount(calculateLifetimeCount(maxKeepAliveCount));
        }
    }

    /**
     * Set the maximum number of MonitoredItems that can be created/modified/deleted in a single
     * service call.
     * <p>
     * This value is compared against the value read from the Server's OperationLimits object, and
     * the smaller of the two is used.
     *
     * @param maxMonitoredItemsPerCall the maximum number of MonitoredItems that can be
     *     created/modified/deleted in a single service call.
     */
    public void setMaxMonitoredItemsPerCall(UInteger maxMonitoredItemsPerCall) {
        this.maxMonitoredItemsPerCall = maxMonitoredItemsPerCall;

        // next service call will re-calculate the partition size
        monitoredItemPartitionSize.reset();
    }

    public void setSubscriptionListener(@Nullable SubscriptionListener listener) {
        this.listener = listener;
    }

    TaskQueue getDeliveryQueue() {
        return deliveryQueue;
    }

    void kickWatchdogTimer() {
        WatchdogTimer watchdog = this.watchdogTimer;
        if (watchdog != null) {
            watchdog.kick();
        }
    }

    private static UInteger calculateMaxKeepAliveCount(double publishingInterval, double targetKeepAliveInterval) {
        // Send a keep-alive every targetKeepAliveInterval milliseconds if the publishing
        // interval is faster, or every publishing interval otherwise.
        int count = (int) Math.ceil(targetKeepAliveInterval / Math.max(1, publishingInterval));

        return uint(Math.max(1, count));
    }

    private static UInteger calculateLifetimeCount(UInteger maxKeepAliveCount) {
        // Lifetime must be 3x (or greater) the keep-alive count.
        BigInteger lifetimeCount = maxKeepAliveCount.toBigInteger()
            .multiply(BigInteger.valueOf(5))
            .min(BigInteger.valueOf(UInteger.MAX_VALUE));

        return uint(lifetimeCount.longValue());
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", OpcUaSubscription.class.getSimpleName() + "[", "]")
            .add("subscriptionId=" + getSubscriptionId().orElse(null))
            .add("syncState=" + syncState)
            .toString();
    }


    void notifyDataReceived(MonitoredItemNotification[] notifications) {
        var items = new ArrayList<OpcUaMonitoredItem>(notifications.length);
        var values = new ArrayList<DataValue>(notifications.length);

        for (MonitoredItemNotification notification : notifications) {
            UInteger clientHandle = notification.getClientHandle();
            OpcUaMonitoredItem item = monitoredItems.get(clientHandle);
            if (item != null) {
                items.add(item);
                values.add(notification.getValue());
            } else {
                // This can happen if an item is deleted while a notification is in-flight.
                logger.debug("Received data for unknown ClientHandle: {}", clientHandle);
            }
        }

        SubscriptionListener listener = this.listener;
        if (listener != null) {
            listener.onDataReceived(this, items, values);
        }

        for (int i = 0; i < items.size(); i++) {
            OpcUaMonitoredItem item = items.get(i);
            DataValue value = values.get(i);
            item.notifyDataValueReceived(value);
        }
    }

    void notifyEventsReceived(EventFieldList[] events) {
        var items = new ArrayList<OpcUaMonitoredItem>(events.length);
        var eventValuesList = new ArrayList<Variant[]>(events.length);

        for (EventFieldList event : events) {
            UInteger clientHandle = event.getClientHandle();
            OpcUaMonitoredItem item = monitoredItems.get(clientHandle);
            if (item != null) {
                items.add(item);
                eventValuesList.add(event.getEventFields());
            } else {
                // This can happen if an item is deleted while a notification is in-flight.
                logger.debug("Received event for unknown ClientHandle: {}", clientHandle);
            }
        }

        SubscriptionListener listener = this.listener;
        if (listener != null) {
            listener.onEventReceived(this, items, eventValuesList);
        }

        for (int i = 0; i < items.size(); i++) {
            OpcUaMonitoredItem item = items.get(i);
            Variant[] eventValues = eventValuesList.get(i);
            item.notifyEventValuesReceived(eventValues);
        }
    }

    void notifyKeepAliveReceived() {
        SubscriptionListener listener = this.listener;
        if (listener != null) {
            listener.onKeepAliveReceived(this);
        }
    }

    void notifyStatusChanged(StatusCode status) {
        SubscriptionListener listener = this.listener;
        if (listener != null) {
            listener.onStatusChanged(this, status);
        }
    }

    void notifyNotificationDataLost() {
        SubscriptionListener listener = this.listener;
        if (listener != null) {
            listener.onNotificationDataLost(this);
        }
    }

    private static class Modifications {

        private @Nullable Double publishingInterval;
        private @Nullable UInteger lifetimeCount;
        private @Nullable UInteger maxKeepAliveCount;
        private @Nullable UInteger maxNotificationsPerPublish;
        private @Nullable UByte priority;

        private Optional<Double> publishingInterval() {
            return Optional.ofNullable(publishingInterval);
        }

        private Optional<UInteger> lifetimeCount() {
            return Optional.ofNullable(lifetimeCount);
        }

        private Optional<UInteger> maxKeepAliveCount() {
            return Optional.ofNullable(maxKeepAliveCount);
        }

        private Optional<UInteger> maxNotificationsPerPublish() {
            return Optional.ofNullable(maxNotificationsPerPublish);
        }

        private Optional<UByte> priority() {
            return Optional.ofNullable(priority);
        }

    }

    private class WatchdogTimer {

        private final AtomicReference<ScheduledFuture<?>> scheduledFuture = new AtomicReference<>();

        private final double multiplier;

        WatchdogTimer(double multiplier) {
            this.multiplier = Math.max(1.0, multiplier);
        }

        void kick() {
            ScheduledFuture<?> sf = scheduledFuture.get();
            if (sf != null) sf.cancel(false);

            scheduleNext();
        }

        void cancel() {
            ScheduledFuture<?> sf = scheduledFuture.getAndSet(null);
            if (sf != null) sf.cancel(false);
        }

        private void scheduleNext() {
            getServerState().ifPresent(state -> {
                long delay = Math.round(
                    state.publishingInterval *
                        state.maxKeepAliveCount.longValue() * multiplier
                );

                ScheduledFuture<?> nextSf = client.getTransport().getConfig().getScheduledExecutor().schedule(
                    () -> client.getTransport().getConfig().getExecutor().execute(() -> {
                        SubscriptionListener listener = OpcUaSubscription.this.listener;

                        if (listener != null) {
                            deliveryQueue.execute(
                                () ->
                                    listener.onWatchdogTimerElapsed(OpcUaSubscription.this)
                            );
                        }
                    }),
                    delay,
                    TimeUnit.MILLISECONDS
                );

                scheduledFuture.set(nextSf);
            });
        }

    }

    /**
     * The state of the Subscription as it exists on the server, after the most recent successful
     * operation.
     */
    public static class ServerState {

        private final UInteger subscriptionId;
        private final Double publishingInterval;
        private final UInteger lifetimeCount;
        private final UInteger maxKeepAliveCount;
        private final UInteger maxNotificationsPerPublish;
        private final UByte priority;
        private final boolean publishingEnabled;

        private ServerState(
            UInteger subscriptionId,
            Double publishingInterval,
            UInteger lifetimeCount,
            UInteger maxKeepAliveCount,
            UInteger maxNotificationsPerPublish,
            UByte priority,
            boolean publishingEnabled
        ) {

            this.subscriptionId = subscriptionId;
            this.publishingInterval = publishingInterval;
            this.lifetimeCount = lifetimeCount;
            this.maxKeepAliveCount = maxKeepAliveCount;
            this.maxNotificationsPerPublish = maxNotificationsPerPublish;
            this.priority = priority;
            this.publishingEnabled = publishingEnabled;
        }

        public UInteger getSubscriptionId() {
            return subscriptionId;
        }

        public Double getPublishingInterval() {
            return publishingInterval;
        }

        public UInteger getLifetimeCount() {
            return lifetimeCount;
        }

        public UInteger getMaxKeepAliveCount() {
            return maxKeepAliveCount;
        }

        public UInteger getMaxNotificationsPerPublish() {
            return maxNotificationsPerPublish;
        }

        public UByte getPriority() {
            return priority;
        }

        public boolean isPublishingEnabled() {
            return publishingEnabled;
        }

    }

    public enum SyncState {

        /**
         * The Subscription has been instantiated but does not exist on the server.
         */
        INITIAL,

        /**
         * The Subscription has been created on the server and has no outstanding modifications to
         * synchronize.
         */
        SYNCHRONIZED,

        /**
         * The Subscription has been created on the server but has outstanding modifications to
         * synchronize.
         */
        UNSYNCHRONIZED

    }


    public interface SubscriptionListener {

        default void onDataReceived(
            OpcUaSubscription subscription,
            List<OpcUaMonitoredItem> items, List<DataValue> values
        ) {}

        default void onEventReceived(
            OpcUaSubscription subscription,
            List<OpcUaMonitoredItem> items, List<Variant[]> fields
        ) {}

        default void onKeepAliveReceived(OpcUaSubscription subscription) {}

        default void onNotificationDataLost(OpcUaSubscription subscription) {}

        default void onWatchdogTimerElapsed(OpcUaSubscription subscription) {}

        default void onStatusChanged(OpcUaSubscription subscription, StatusCode status) {}

        default void onTransferFailed(OpcUaSubscription subscription, StatusCode status) {}

    }

}

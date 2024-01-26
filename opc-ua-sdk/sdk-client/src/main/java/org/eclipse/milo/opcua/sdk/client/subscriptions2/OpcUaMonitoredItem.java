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

import java.util.List;

import org.eclipse.milo.opcua.sdk.client.subscriptions.ManagedDataItem;
import org.eclipse.milo.opcua.sdk.client.subscriptions2.batching.CreateMonitoredItemBatch;
import org.eclipse.milo.opcua.sdk.client.subscriptions2.batching.DeleteMonitoredItemBatch;
import org.eclipse.milo.opcua.sdk.client.subscriptions2.batching.ModifyMonitoredItemBatch;
import org.eclipse.milo.opcua.sdk.client.subscriptions2.batching.SetMonitoringModeBatch;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MonitoringMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateMonitoredItemsResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.EventFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemCreateRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoringParameters;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;

import static java.util.Objects.requireNonNull;

public class OpcUaMonitoredItem {

    private double samplingInterval = 1000.0;
    private MonitoringMode monitoringMode = MonitoringMode.Reporting;

    private final OpcUaSubscription subscription;
    private final ReadValueId readValueId;

    public OpcUaMonitoredItem(OpcUaSubscription subscription, ReadValueId readValueId) {
        this.subscription = subscription;
        this.readValueId = readValueId;
    }

    public StatusCode create() throws UaException {
        // TODO

        UInteger subscriptionId = subscription.getSubscriptionId().orElseThrow();

        var request = new MonitoredItemCreateRequest(
            readValueId,
            monitoringMode,
            new MonitoringParameters(
                null, // clientHandle,
                samplingInterval,
                null,
                null, //queueSize,
                null //discardOldest
            )
        );

        CreateMonitoredItemsResponse response = subscription.getClient().createMonitoredItems(
            subscriptionId,
            TimestampsToReturn.Both,
            List.of(request)
        );

        StatusCode result = requireNonNull(response.getResults())[0].getStatusCode();

        if (result.isGood()) {
            subscription.register(this);
        }

        return result;
    }

    public void create(CreateMonitoredItemBatch batch) {}

    public void modify() throws UaException {}

    public void modify(ModifyMonitoredItemBatch batch) {}

    public void delete() throws UaException {}

    public void delete(DeleteMonitoredItemBatch batch) {}

    public void setMonitoringMode(MonitoringMode monitoringMode) throws UaException {
        // TODO
    }

    public void setMonitoringMode(MonitoringMode monitoringMode, SetMonitoringModeBatch batch) {
        // TODO
    }

    public void setSamplingInterval(double samplingInterval) {
        this.samplingInterval = samplingInterval;
    }

    public UInteger getClientHandle() {
        return null; // TODO
    }

    public UInteger getMonitoredItemId() {
        return null; // TODO
    }

    public void addDataValueListener(DataValueListener listener) {}

    public void removeDataValueListener(DataValueListener listener) {}

    public void addEventValueListener(EventValueListener listener) {}

    public void removeEventValueListener(EventValueListener listener) {}

    /**
     * A callback that receives notification of new values for a {@link ManagedDataItem}.
     */
    public interface DataValueListener {

        /**
         * A new {@link DataValue} for {@code item} has arrived.
         * <p>
         * Take care not to block unnecessarily in this callback because subscription notifications
         * are processed synchronously as a backpressure mechanism. Blocking inside this callback
         * will prevent subsequent notifications from being processed and new PublishRequests from
         * being sent.
         *
         * @param item the {@link OpcUaMonitoredItem} for which a new value has arrived.
         * @param value the new {@link DataValue}.
         */
        void onDataValueReceived(OpcUaMonitoredItem item, DataValue value);

    }

    /**
     * A callback that receives notification of new events for a {@link OpcUaMonitoredItem}.
     */
    public interface EventValueListener {

        /**
         * A new event for {@code item} has arrived.
         * <p>
         * Take care not to block unnecessarily in this callback because subscription notifications
         * are processed synchronously as a backpressure mechanism. Blocking inside this callback
         * will prevent subsequent notifications from being processed and new PublishRequests from
         * being sent.
         *
         * @param item the {@link OpcUaMonitoredItem} for which a new event has arrived.
         * @param eventValues the new event field values.
         */
        void onEventValueReceived(OpcUaMonitoredItem item, Variant[] eventValues);

    }

    private static class ModificationDiff {

        private Double requestedSamplingInterval;
        private UInteger requestedQueueSize;

    }

    enum State {
        INITIAL,

        SYNCHRONIZED,

        UNSYNCHRONIZED
    }

    public static OpcUaMonitoredItem newDataItem(OpcUaSubscription subscription, NodeId nodeId) {
        return null; // TODO
    }

    public static OpcUaMonitoredItem newDataItem(OpcUaSubscription subscription, NodeId nodeId, double samplingInterval) {
        return null; // TODO
    }

    public static OpcUaMonitoredItem newDataItem(OpcUaSubscription subscription, ReadValueId readValueId) {
        return null; // TODO
    }

    public static OpcUaMonitoredItem newDataItem(OpcUaSubscription subscription, ReadValueId readValueId, double samplingInterval) {
        return null; // TODO
    }

    public static OpcUaMonitoredItem newEventItem(OpcUaSubscription subscription, NodeId nodeId) {
        return null; // TODO
    }

    public static OpcUaMonitoredItem newEventItem(OpcUaSubscription subscription, NodeId nodeId, EventFilter eventFilter) {
        return null; // TODO
    }

}

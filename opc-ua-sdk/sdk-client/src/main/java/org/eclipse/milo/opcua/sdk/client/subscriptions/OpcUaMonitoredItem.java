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

import java.util.Optional;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MonitoringMode;
import org.eclipse.milo.opcua.stack.core.types.structured.EventFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemCreateRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemCreateResult;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemModifyRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemModifyResult;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoringFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoringParameters;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.jspecify.annotations.Nullable;

public class OpcUaMonitoredItem {

  private volatile SyncState syncState = SyncState.INITIAL;
  private volatile ServerState serverState;
  private volatile Modifications modifications;

  private volatile @Nullable Object userObject;
  private volatile @Nullable DataValueListener dataValueListener;
  private volatile @Nullable EventValueListener eventValueListener;

  // MonitoredItem parameters that a user might modify via SetMonitoringMode:
  private volatile MonitoringMode monitoringMode = MonitoringMode.Reporting;

  // MonitoredItem parameters that a user might modify via ModifyMonitoredItem:
  private volatile Double samplingInterval = 1000.0;
  private volatile @Nullable MonitoringFilter filter;
  private volatile UInteger queueSize = uint(1);
  private volatile boolean discardOldest = true;

  private volatile @Nullable StatusCode createResult;
  private volatile @Nullable StatusCode modifyResult;
  private volatile @Nullable StatusCode deleteResult;
  private volatile @Nullable StatusCode setMonitoringModeResult;

  private volatile @Nullable UInteger clientHandle;

  private final ReadValueId readValueId;

  public OpcUaMonitoredItem(ReadValueId readValueId) {
    this.readValueId = readValueId;
  }

  public OpcUaMonitoredItem(ReadValueId readValueId, MonitoringMode monitoringMode) {
    this.readValueId = readValueId;
    this.monitoringMode = monitoringMode;
  }

  /**
   * Set the SamplingInterval parameter for this MonitoredItem.
   *
   * <p>This change must be synchronized to the server before it takes effect.
   *
   * @param samplingInterval the new SamplingInterval parameter value.
   * @see OpcUaSubscription#synchronizeMonitoredItems()
   */
  public void setSamplingInterval(double samplingInterval) {
    this.samplingInterval = samplingInterval;

    if (syncState != SyncState.INITIAL) {
      if (modifications == null) {
        modifications = new Modifications();
      }

      modifications.samplingInterval = samplingInterval;

      syncState = SyncState.UNSYNCHRONIZED;
    }
  }

  /**
   * Modify the QueueSize parameter for this MonitoredItem.
   *
   * <p>This change must be synchronized to the server before it takes effect.
   *
   * @param queueSize the new QueueSize parameter value.
   * @see OpcUaSubscription#synchronizeMonitoredItems()
   */
  public void setQueueSize(UInteger queueSize) {
    this.queueSize = queueSize;

    if (syncState != SyncState.INITIAL) {
      if (modifications == null) {
        modifications = new Modifications();
      }

      modifications.queueSize = queueSize;

      syncState = SyncState.UNSYNCHRONIZED;
    }
  }

  /**
   * Modify the DiscardOldest parameter for this MonitoredItem.
   *
   * <p>This change must be synchronized to the server before it takes effect.
   *
   * @param discardOldest the new DiscardOldest parameter value.
   */
  public void setDiscardOldest(boolean discardOldest) {
    this.discardOldest = discardOldest;

    if (syncState != SyncState.INITIAL) {
      if (modifications == null) {
        modifications = new Modifications();
      }

      modifications.discardOldest = discardOldest;

      syncState = SyncState.UNSYNCHRONIZED;
    }
  }

  /**
   * Modify the Filter parameter for this MonitoredItem.
   *
   * <p>This change must be synchronized to the server before it takes effect.
   *
   * @param filter the new Filter parameter value.
   * @see OpcUaSubscription#synchronizeMonitoredItems()
   */
  public void setFilter(@Nullable MonitoringFilter filter) {
    this.filter = filter;

    if (syncState != SyncState.INITIAL) {
      if (modifications == null) {
        modifications = new Modifications();
      }

      modifications.filter = filter;

      syncState = SyncState.UNSYNCHRONIZED;
    }
  }

  /**
   * Get the {@link ReadValueId} for this MonitoredItem.
   *
   * @return the {@link ReadValueId} for this MonitoredItem.
   */
  public ReadValueId getReadValueId() {
    return readValueId;
  }

  /**
   * Get the Client-assigned id for this MonitoredItem.
   *
   * <p>Present only if the MonitoredItem has been added to a Subscription, see {@link
   * OpcUaSubscription#addMonitoredItem(OpcUaMonitoredItem)}.
   *
   * @return the Client-assigned id for this MonitoredItem.
   */
  public Optional<UInteger> getClientHandle() {
    return Optional.ofNullable(clientHandle);
  }

  /**
   * Get the Server-assigned id for this MonitoredItem.
   *
   * <p>Present only if the MonitoredItem has been created.
   *
   * @return the Server-assigned id for this MonitoredItem.
   */
  public Optional<UInteger> getMonitoredItemId() {
    return getServerState().map(ServerState::getMonitoredItemId);
  }

  /**
   * Get the most recent SamplingInterval this MonitoredItem was configured with.
   *
   * <p>It may differ from the actual SamplingInterval that the server is using, see {@link
   * #getRevisedSamplingInterval()}.
   *
   * @return the most recent SamplingInterval this MonitoredItem was configured with.
   */
  public Double getSamplingInterval() {
    return samplingInterval;
  }

  /**
   * Get the most recent QueueSize this MonitoredItem was configured with.
   *
   * <p>It may differ from the actual QueueSize that the server is using, see {@link
   * #getRevisedQueueSize()}.
   *
   * @return the most recent QueueSize this MonitoredItem was configured with.
   */
  public UInteger getQueueSize() {
    return queueSize;
  }

  /**
   * Get the most recent DiscardOldest this MonitoredItem was configured with.
   *
   * @return the most recent DiscardOldest this MonitoredItem was configured with.
   */
  public boolean getDiscardOldest() {
    return discardOldest;
  }

  /**
   * Get the most recent Filter this MonitoredItem was configured with.
   *
   * @return the most recent Filter this MonitoredItem was configured with.
   */
  public @Nullable MonitoringFilter getFilter() {
    return filter;
  }

  /**
   * Get the revised SamplingInterval that the server is using for this MonitoredItem.
   *
   * <p>Present only if the MonitoredItem has been created or modified.
   *
   * @return the revised SamplingInterval that the server is using for this MonitoredItem.
   */
  public Optional<Double> getRevisedSamplingInterval() {
    return getServerState().map(ServerState::getSamplingInterval);
  }

  /**
   * Get the revised QueueSize that the server is using for this MonitoredItem.
   *
   * <p>Present only if the MonitoredItem has been created or modified.
   *
   * @return the revised QueueSize that the server is using for this MonitoredItem.
   */
  public Optional<UInteger> getRevisedQueueSize() {
    return getServerState().map(ServerState::getQueueSize);
  }

  /**
   * Get the filter result structure for the most recently configured monitoring filter.
   *
   * @return the filter result structure for the most recently configured monitoring filter.
   */
  public Optional<ExtensionObject> getFilterResult() {
    return getServerState().map(ServerState::getFilterResult);
  }

  /**
   * Get the current MonitoringMode for this MonitoredItem.
   *
   * @return the current MonitoringMode for this MonitoredItem.
   */
  public MonitoringMode getMonitoringMode() {
    return monitoringMode;
  }

  /**
   * Get the result of the most recent CreateMonitoredItems service call, if one has been made.
   *
   * @return the result of the most recent CreateMonitoredItems service call, if one has been made.
   */
  public Optional<StatusCode> getCreateResult() {
    return Optional.ofNullable(createResult);
  }

  /**
   * Get the result of the most recent ModifyMonitoredItems service call, if one has been made.
   *
   * @return the result of the most recent ModifyMonitoredItems service call, if one has been made.
   */
  public Optional<StatusCode> getModifyResult() {
    return Optional.ofNullable(modifyResult);
  }

  /**
   * Get the result of the most recent DeleteMonitoredItems service call, if one has been made.
   *
   * @return the result of the most recent DeleteMonitoredItems service call, if one has been made.
   */
  public Optional<StatusCode> getDeleteResult() {
    return Optional.ofNullable(deleteResult);
  }

  /**
   * Get the result of the most recent SetMonitoringMode service call, if one has been made.
   *
   * @return the result of the most recent SetMonitoringMode service call, if one has been made.
   */
  public Optional<StatusCode> getSetMonitoringModeResult() {
    return Optional.ofNullable(setMonitoringModeResult);
  }

  /**
   * Set the {@link DataValueListener} for this MonitoredItem.
   *
   * @param listener the {@link DataValueListener} for this MonitoredItem.
   */
  public void setDataValueListener(@Nullable DataValueListener listener) {
    this.dataValueListener = listener;
  }

  /**
   * Set the {@link EventValueListener} for this MonitoredItem.
   *
   * @param listener the {@link EventValueListener} for this MonitoredItem.
   */
  public void setEventValueListener(@Nullable EventValueListener listener) {
    this.eventValueListener = listener;
  }

  /**
   * Associate an arbitrary user object with this MonitoredItem.
   *
   * @param userObject the user object to associate with this MonitoredItem.
   */
  public void setUserObject(@Nullable Object userObject) {
    this.userObject = userObject;
  }

  /**
   * @return the user object associated with this MonitoredItem.
   */
  public Optional<Object> getUserObject() {
    return Optional.ofNullable(userObject);
  }

  /**
   * Set the Client-assigned id for this MonitoredItem.
   *
   * @param clientHandle the Client-assigned id for this MonitoredItem.
   */
  void setClientHandle(@Nullable UInteger clientHandle) {
    this.clientHandle = clientHandle;
  }

  /**
   * Set the current MonitoringMode for this MonitoredItem.
   *
   * @param monitoringMode the current MonitoringMode for this MonitoredItem.
   */
  void setMonitoringMode(MonitoringMode monitoringMode) {
    this.monitoringMode = monitoringMode;
  }

  /**
   * @return the current {@link SyncState} of this MonitoredItem.
   */
  public SyncState getSyncState() {
    return syncState;
  }

  /**
   * @return the current {@link ServerState} of this MonitoredItem, if it exists on the server.
   */
  public Optional<ServerState> getServerState() {
    return Optional.ofNullable(serverState);
  }

  MonitoredItemCreateRequest newCreateRequest() {
    if (clientHandle == null) {
      throw new IllegalStateException("no clientHandle");
    }

    ExtensionObject filterXo = null;

    MonitoringFilter filter = this.filter;
    if (filter != null) {
      filterXo = ExtensionObject.encode(DefaultEncodingContext.INSTANCE, filter);
    }

    return new MonitoredItemCreateRequest(
        readValueId,
        monitoringMode,
        new MonitoringParameters(
            clientHandle, samplingInterval, filterXo, queueSize, discardOldest));
  }

  MonitoredItemModifyRequest newModifyRequest() {
    if (serverState == null) {
      throw new IllegalStateException("no ServerState");
    }
    if (modifications == null) {
      throw new IllegalStateException("no pending modification");
    }

    Double newRequestedSamplingInterval = modifications.samplingInterval().orElse(samplingInterval);
    MonitoringFilter newFilter = modifications.filter().orElse(filter);
    UInteger newRequestedQueueSize = modifications.queueSize().orElse(queueSize);
    Boolean newDiscardOldest = modifications.discardOldest().orElse(discardOldest);

    ExtensionObject newFilterXo = null;
    if (newFilter != null) {
      newFilterXo = ExtensionObject.encode(DefaultEncodingContext.INSTANCE, newFilter);
    }

    return new MonitoredItemModifyRequest(
        serverState.monitoredItemId,
        new MonitoringParameters(
            clientHandle,
            newRequestedSamplingInterval,
            newFilterXo,
            newRequestedQueueSize,
            newDiscardOldest));
  }

  void applyCreateResult(MonitoredItemCreateResult result) {
    StatusCode statusCode = result.getStatusCode();

    if (statusCode.isGood()) {
      serverState =
          new ServerState(
              result.getMonitoredItemId(),
              monitoringMode,
              result.getRevisedSamplingInterval(),
              result.getFilterResult(),
              result.getRevisedQueueSize(),
              discardOldest);

      syncState = SyncState.SYNCHRONIZED;
    } else {
      syncState = SyncState.INITIAL;
    }

    this.createResult = statusCode;
  }

  void applyModifyResult(MonitoredItemModifyResult result) {
    StatusCode statusCode = result.getStatusCode();

    if (statusCode.isGood()) {
      modifications = null;

      UInteger monitoredItemId = serverState.monitoredItemId;

      serverState =
          new ServerState(
              monitoredItemId,
              monitoringMode,
              result.getRevisedSamplingInterval(),
              result.getFilterResult(),
              result.getRevisedQueueSize(),
              discardOldest);

      syncState = SyncState.SYNCHRONIZED;
    } else {
      syncState = SyncState.UNSYNCHRONIZED;
    }

    this.modifyResult = statusCode;
  }

  void applyDeleteResult(StatusCode statusCode) {
    syncState = SyncState.INITIAL;
    serverState = null;
    modifications = null;
    clientHandle = null;

    deleteResult = statusCode;
  }

  void applySetMonitoringModeResult(StatusCode statusCode) {
    this.setMonitoringModeResult = statusCode;
  }

  void notifyDataValueReceived(DataValue value) {
    DataValueListener listener = dataValueListener;

    if (listener != null) {
      listener.onDataReceived(this, value);
    }
  }

  void notifyEventValuesReceived(Variant[] eventValues) {
    EventValueListener listener = eventValueListener;

    if (listener != null) {
      listener.onEventReceived(this, eventValues);
    }
  }

  void notifyTransferFailed() {
    syncState = SyncState.INITIAL;
    serverState = null;
    modifications = null;

    createResult = null;
    modifyResult = null;
    deleteResult = null;
    setMonitoringModeResult = null;
  }

  /** A callback that receives notification of new values for an {@link OpcUaMonitoredItem}. */
  public interface DataValueListener {

    /**
     * A new {@link DataValue} for {@code item} has arrived.
     *
     * <p>Take care not to block unnecessarily in this callback because subscription notifications
     * are processed synchronously as a backpressure mechanism. Blocking inside this callback will
     * prevent subsequent notifications from being processed and new PublishRequests from being
     * sent.
     *
     * @param item the {@link OpcUaMonitoredItem} for which a new value has arrived.
     * @param value the new {@link DataValue}.
     */
    void onDataReceived(OpcUaMonitoredItem item, DataValue value);
  }

  /** A callback that receives notification of new events for an {@link OpcUaMonitoredItem}. */
  public interface EventValueListener {

    /**
     * A new event for {@code item} has arrived.
     *
     * <p>Take care not to block unnecessarily in this callback because subscription notifications
     * are processed synchronously as a backpressure mechanism. Blocking inside this callback will
     * prevent subsequent notifications from being processed and new PublishRequests from being
     * sent.
     *
     * @param item the {@link OpcUaMonitoredItem} for which a new event has arrived.
     * @param eventValues the new event field values.
     */
    void onEventReceived(OpcUaMonitoredItem item, Variant[] eventValues);
  }

  static class Modifications {

    private volatile @Nullable Double samplingInterval;
    private volatile @Nullable MonitoringFilter filter;
    private volatile @Nullable UInteger queueSize;
    private volatile @Nullable Boolean discardOldest;

    Optional<Double> samplingInterval() {
      return Optional.ofNullable(samplingInterval);
    }

    Optional<MonitoringFilter> filter() {
      return Optional.ofNullable(filter);
    }

    Optional<UInteger> queueSize() {
      return Optional.ofNullable(queueSize);
    }

    Optional<Boolean> discardOldest() {
      return Optional.ofNullable(discardOldest);
    }
  }

  /**
   * The state of the MonitoredItem as it exists on the server, after the recent successful
   * operation.
   */
  public static class ServerState {

    private final UInteger monitoredItemId;
    private final MonitoringMode monitoringMode;
    private final double samplingInterval;
    private final @Nullable ExtensionObject filterResult;
    private final UInteger queueSize;
    private final boolean discardOldest;

    private ServerState(
        UInteger monitoredItemId,
        MonitoringMode monitoringMode,
        double samplingInterval,
        @Nullable ExtensionObject filterResult,
        UInteger queueSize,
        boolean discardOldest) {

      this.monitoredItemId = monitoredItemId;
      this.monitoringMode = monitoringMode;
      this.samplingInterval = samplingInterval;
      this.filterResult = filterResult;
      this.queueSize = queueSize;
      this.discardOldest = discardOldest;
    }

    public UInteger getMonitoredItemId() {
      return monitoredItemId;
    }

    public MonitoringMode getMonitoringMode() {
      return monitoringMode;
    }

    public double getSamplingInterval() {
      return samplingInterval;
    }

    public @Nullable ExtensionObject getFilterResult() {
      return filterResult;
    }

    public UInteger getQueueSize() {
      return queueSize;
    }

    public boolean getDiscardOldest() {
      return discardOldest;
    }
  }

  public enum SyncState {
    INITIAL,

    SYNCHRONIZED,

    UNSYNCHRONIZED
  }

  /**
   * Create a new MonitoredItem for the Value attribute of the Node identified by {@code nodeId}.
   *
   * <p>This item will not exist on the server until it has been added to an {@link
   * OpcUaSubscription} and the subscription has been synchronized.
   *
   * @param nodeId the {@link NodeId} of the Node to monitor.
   * @return a new MonitoredItem for the Value attribute of the Node identified by {@code nodeId}.
   * @see OpcUaSubscription#addMonitoredItem(OpcUaMonitoredItem)
   * @see OpcUaSubscription#synchronizeMonitoredItems()
   */
  public static OpcUaMonitoredItem newDataItem(NodeId nodeId) {
    var readValueId =
        new ReadValueId(nodeId, AttributeId.Value.uid(), null, QualifiedName.NULL_VALUE);

    return new OpcUaMonitoredItem(readValueId);
  }

  /**
   * Create a new MonitoredItem for the Value attribute of the Node identified by {@code nodeId},
   * with the specified {@code monitoringMode}.
   *
   * <p>This item will not exist on the server until it has been added to an {@link
   * OpcUaSubscription} and the subscription has been synchronized.
   *
   * @param nodeId the {@link NodeId} of the Node to monitor.
   * @param monitoringMode the initial {@link MonitoringMode} to use when the MonitoredItem is
   *     created.
   * @return a new MonitoredItem for the Value attribute of the Node identified by {@code nodeId},
   *     with the specified {@code monitoringMode}.
   * @see OpcUaSubscription#addMonitoredItem(OpcUaMonitoredItem)
   * @see OpcUaSubscription#synchronizeMonitoredItems()
   */
  public static OpcUaMonitoredItem newDataItem(NodeId nodeId, MonitoringMode monitoringMode) {
    var readValueId =
        new ReadValueId(nodeId, AttributeId.Value.uid(), null, QualifiedName.NULL_VALUE);

    return new OpcUaMonitoredItem(readValueId, monitoringMode);
  }

  /**
   * Create a new MonitoredItem for the EventNotifier attribute of the Node identified by {@code
   * nodeId}.
   *
   * <p>This item will not exist on the server until it has been added to an {@link
   * OpcUaSubscription} and the subscription has been synchronized.
   *
   * @param nodeId the {@link NodeId} of the Node to monitor.
   * @return a new MonitoredItem for the EventNotifier attribute of the Node identified by {@code
   *     nodeId}.
   * @see OpcUaSubscription#addMonitoredItem(OpcUaMonitoredItem)
   * @see OpcUaSubscription#synchronizeMonitoredItems()
   */
  public static OpcUaMonitoredItem newEventItem(NodeId nodeId) {
    return newEventItem(nodeId, null);
  }

  /**
   * Create a new MonitoredItem for the EventNotifier attribute of the Node identified by {@code
   * nodeId}, with the specified {@code eventFilter}.
   *
   * <p>This item will not exist on the server until it has been added to an {@link
   * OpcUaSubscription} and the subscription has been synchronized.
   *
   * @param nodeId the {@link NodeId} of the Node to monitor.
   * @param eventFilter the {@link EventFilter} to use for the MonitoredItem.
   * @return a new MonitoredItem for the EventNotifier attribute of the Node identified by {@code
   *     nodeId}, with the specified {@code eventFilter}.
   * @see OpcUaSubscription#addMonitoredItem(OpcUaMonitoredItem)
   * @see OpcUaSubscription#synchronizeMonitoredItems()
   */
  public static OpcUaMonitoredItem newEventItem(NodeId nodeId, @Nullable EventFilter eventFilter) {
    var readValueId =
        new ReadValueId(nodeId, AttributeId.EventNotifier.uid(), null, QualifiedName.NULL_VALUE);

    var item = new OpcUaMonitoredItem(readValueId);
    item.setSamplingInterval(0.0);
    item.setFilter(eventFilter);

    return item;
  }
}

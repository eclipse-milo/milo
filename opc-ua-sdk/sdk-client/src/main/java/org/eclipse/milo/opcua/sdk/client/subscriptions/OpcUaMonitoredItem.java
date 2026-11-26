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

  /**
   * Guards coordinated state transitions across {@link #syncState}, {@link #serverState}, {@link
   * #modifications}, {@link #itemIncarnation}, in-flight operation reservations, the configurable
   * parameters, the operation results, and {@link #clientHandle}.
   *
   * <p>Always acquired innermost, after any {@link OpcUaSubscription} lock, and never held across
   * service calls or listener callbacks.
   */
  private final Object stateLock = new Object();

  private volatile SyncState syncState = SyncState.INITIAL;
  private volatile @Nullable ServerState serverState;
  private @Nullable Modifications modifications;

  /**
   * Monotonic lifecycle incarnation for this Java item object, incremented under {@link #stateLock}
   * whenever a server-side MonitoredItem is created, deleted, reset, or detached. Prevents late
   * responses or reservation releases from an earlier lifecycle from mutating a newer one.
   */
  private long itemIncarnation = 0L;

  /**
   * {@code true} while a CreateMonitoredItems request for this item is in flight. Prevents
   * duplicate concurrent creates and prevents {@link #detachIfNotCreated()} from dropping a pending
   * removal before the create completes. Guarded by {@link #stateLock}.
   */
  private boolean createInFlight = false;

  /**
   * Number of ModifyMonitoredItems requests currently in flight for the active {@link
   * #itemIncarnation}. When overlapping modifies occur, {@link #parameterGeneration} is also
   * advanced so neither overlapping response can mark the item {@link SyncState#SYNCHRONIZED}
   * without a final non-overlapping reconciliation pass. Guarded by {@link #stateLock}.
   */
  private int activeModifies = 0;

  /**
   * {@code true} while a SetMonitoringMode request for the active {@link #itemIncarnation} is in
   * flight. Prevents overlapping mode changes from racing on the wire or applying out of order.
   * Guarded by {@link #stateLock}.
   */
  private boolean setMonitoringModeInFlight = false;

  /**
   * Monotonic counter incremented under {@link #stateLock} whenever a SetMonitoringMode request is
   * prepared. Prevents a delayed response from an earlier attempt from overwriting a newer mode on
   * the same {@link #itemIncarnation}.
   */
  private long monitoringModeGeneration = 0L;

  /**
   * Monotonic counter incremented under {@link #stateLock} whenever any configurable parameter
   * changes (including while {@link SyncState#INITIAL}) or when overlapping modifies begin.
   * Comparing the value captured when a Create or Modify request was built against the value when
   * its Good result is applied prevents in-flight parameter changes or reordered overlapping
   * modifies from leaving the item falsely synchronized.
   */
  private long parameterGeneration = 0L;

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

  /**
   * The {@link OpcUaSubscription} that currently owns this item's {@link #clientHandle}, or {@code
   * null} when detached. Guarded by {@link #stateLock}.
   */
  private @Nullable OpcUaSubscription owner;

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
    synchronized (stateLock) {
      this.samplingInterval = samplingInterval;
      parameterGeneration++;

      if (syncState != SyncState.INITIAL) {
        Modifications pending = getOrCreateModificationsLocked();
        pending.samplingInterval = samplingInterval;

        syncState = SyncState.UNSYNCHRONIZED;
      }
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
    synchronized (stateLock) {
      this.queueSize = queueSize;
      parameterGeneration++;

      if (syncState != SyncState.INITIAL) {
        Modifications pending = getOrCreateModificationsLocked();
        pending.queueSize = queueSize;

        syncState = SyncState.UNSYNCHRONIZED;
      }
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
    synchronized (stateLock) {
      this.discardOldest = discardOldest;
      parameterGeneration++;

      if (syncState != SyncState.INITIAL) {
        Modifications pending = getOrCreateModificationsLocked();
        pending.discardOldest = discardOldest;

        syncState = SyncState.UNSYNCHRONIZED;
      }
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
    synchronized (stateLock) {
      this.filter = filter;
      parameterGeneration++;

      if (syncState != SyncState.INITIAL) {
        Modifications pending = getOrCreateModificationsLocked();
        pending.filter = filter;

        syncState = SyncState.UNSYNCHRONIZED;
      }
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
    synchronized (stateLock) {
      this.clientHandle = clientHandle;
      if (clientHandle == null) {
        this.owner = null;
      }
    }
  }

  final @Nullable UInteger internalClientHandle() {
    return clientHandle;
  }

  final @Nullable UInteger internalClientHandleIfOwnedBy(OpcUaSubscription subscription) {
    synchronized (stateLock) {
      return this.owner == subscription ? this.clientHandle : null;
    }
  }

  final boolean isOwnedBy(OpcUaSubscription subscription) {
    synchronized (stateLock) {
      return this.owner == subscription && this.clientHandle != null;
    }
  }

  final boolean tryClaim(OpcUaSubscription subscription, UInteger clientHandle) {
    synchronized (stateLock) {
      if (this.owner != null || this.clientHandle != null) {
        return false;
      }

      this.owner = subscription;
      this.clientHandle = clientHandle;
      return true;
    }
  }

  final @Nullable UInteger internalMonitoredItemId() {
    ServerState current = this.serverState;
    return current != null ? current.monitoredItemId : null;
  }

  final SyncState internalSyncState() {
    return syncState;
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

  Optional<PreparedCreateRequest> tryBeginCreate() {
    synchronized (stateLock) {
      UInteger clientHandle = this.clientHandle;
      if (clientHandle == null || syncState != SyncState.INITIAL || createInFlight) {
        return Optional.empty();
      }

      createInFlight = true;

      return Optional.of(
          new PreparedCreateRequest(
              readValueId,
              clientHandle,
              monitoringMode,
              samplingInterval,
              filter,
              queueSize,
              discardOldest,
              itemIncarnation,
              parameterGeneration));
    }
  }

  void releaseCreate(PreparedCreateRequest prepared) {
    synchronized (stateLock) {
      releaseCreateLocked(prepared);
    }
  }

  Optional<PreparedModifyRequest> tryBeginModify() {
    synchronized (stateLock) {
      ServerState currentServerState = this.serverState;
      if (currentServerState == null) {
        if (syncState != SyncState.INITIAL) {
          syncState = SyncState.INITIAL;
          modifications = null;
        }
        return Optional.empty();
      }

      Modifications pending = this.modifications;
      if (pending == null) {
        return Optional.empty();
      }

      long capturedIncarnation = this.itemIncarnation;
      long capturedGeneration = this.parameterGeneration;
      if (activeModifies > 0) {
        // Overlapping modifies can reorder on the wire, so advance parameterGeneration after
        // capturing to guarantee neither overlapping response can mark the item SYNCHRONIZED.
        this.parameterGeneration++;
      }
      activeModifies++;

      return Optional.of(
          new PreparedModifyRequest(
              currentServerState.monitoredItemId,
              clientHandle,
              pending.samplingInterval().orElse(samplingInterval),
              pending.filter().orElse(filter),
              pending.queueSize().orElse(queueSize),
              pending.discardOldest().orElse(discardOldest),
              capturedIncarnation,
              capturedGeneration));
    }
  }

  void releaseModify(PreparedModifyRequest prepared) {
    synchronized (stateLock) {
      releaseModifyLocked(prepared);
    }
  }

  Optional<PreparedDeleteRequest> tryBeginDelete() {
    synchronized (stateLock) {
      ServerState currentServerState = this.serverState;
      if (currentServerState == null || activeModifies > 0 || setMonitoringModeInFlight) {
        return Optional.empty();
      }

      return Optional.of(
          new PreparedDeleteRequest(currentServerState.monitoredItemId, itemIncarnation));
    }
  }

  void applyCreateResult(MonitoredItemCreateResult result) {
    synchronized (stateLock) {
      applyCreateResultLocked(result, monitoringMode, parameterGeneration, discardOldest);
    }
  }

  void applyCreateResult(MonitoredItemCreateResult result, PreparedCreateRequest prepared) {
    synchronized (stateLock) {
      releaseCreateLocked(prepared);

      if (this.itemIncarnation != prepared.itemIncarnation) {
        return;
      }

      applyCreateResultLocked(
          result, prepared.monitoringMode, prepared.parameterGeneration, prepared.discardOldest);
    }
  }

  /** Must be called while holding {@link #stateLock}. */
  private void applyCreateResultLocked(
      MonitoredItemCreateResult result,
      MonitoringMode createdMonitoringMode,
      long sentParameterGeneration,
      boolean sentDiscardOldest) {

    StatusCode statusCode = result.getStatusCode();

    if (statusCode.isGood()) {
      itemIncarnation++;
      activeModifies = 0;
      setMonitoringModeInFlight = false;
      serverState =
          new ServerState(
              result.getMonitoredItemId(),
              createdMonitoringMode,
              result.getRevisedSamplingInterval(),
              result.getFilterResult(),
              result.getRevisedQueueSize(),
              sentDiscardOldest);

      if (parameterGeneration == sentParameterGeneration) {
        modifications = null;
        syncState = SyncState.SYNCHRONIZED;
      } else {
        getOrCreateModificationsLocked();
        syncState = SyncState.UNSYNCHRONIZED;
      }
    } else {
      syncState = SyncState.INITIAL;
    }

    this.createResult = statusCode;
  }

  /** Must be called while holding {@link #stateLock}. */
  private void releaseCreateLocked(PreparedCreateRequest prepared) {
    if (!prepared.released) {
      prepared.released = true;
      if (this.itemIncarnation == prepared.itemIncarnation) {
        createInFlight = false;
      }
    }
  }

  void applyModifyResult(MonitoredItemModifyResult result) {
    synchronized (stateLock) {
      ServerState currentServerState = this.serverState;
      if (currentServerState == null) {
        return;
      }

      applyModifyResultLocked(
          result, currentServerState.monitoredItemId, parameterGeneration, discardOldest);
    }
  }

  void applyModifyResult(MonitoredItemModifyResult result, PreparedModifyRequest prepared) {
    synchronized (stateLock) {
      releaseModifyLocked(prepared);

      if (this.itemIncarnation != prepared.itemIncarnation) {
        return;
      }

      applyModifyResultLocked(
          result, prepared.monitoredItemId, prepared.parameterGeneration, prepared.discardOldest);
    }
  }

  /** Must be called while holding {@link #stateLock}. */
  private void applyModifyResultLocked(
      MonitoredItemModifyResult result,
      UInteger sentMonitoredItemId,
      long sentParameterGeneration,
      boolean sentDiscardOldest) {

    ServerState currentServerState = this.serverState;
    if (currentServerState == null
        || !currentServerState.monitoredItemId.equals(sentMonitoredItemId)) {
      return;
    }

    StatusCode statusCode = result.getStatusCode();

    if (statusCode.isGood()) {
      serverState =
          new ServerState(
              currentServerState.monitoredItemId,
              monitoringMode,
              result.getRevisedSamplingInterval(),
              result.getFilterResult(),
              result.getRevisedQueueSize(),
              sentDiscardOldest);

      if (parameterGeneration == sentParameterGeneration && activeModifies == 0) {
        modifications = null;
        syncState = SyncState.SYNCHRONIZED;
      } else {
        getOrCreateModificationsLocked();
        syncState = SyncState.UNSYNCHRONIZED;
      }
    } else {
      getOrCreateModificationsLocked();
      syncState = SyncState.UNSYNCHRONIZED;
    }

    this.modifyResult = statusCode;
  }

  /** Must be called while holding {@link #stateLock}. */
  private void releaseModifyLocked(PreparedModifyRequest prepared) {
    if (!prepared.released) {
      prepared.released = true;
      if (this.itemIncarnation == prepared.itemIncarnation && activeModifies > 0) {
        activeModifies--;
      }
    }
  }

  void applyDeleteResult(StatusCode statusCode) {
    applyDeleteResult(statusCode, null);
  }

  void applyDeleteResult(StatusCode statusCode, @Nullable UInteger readdedClientHandle) {
    synchronized (stateLock) {
      applyDeleteResultLocked(statusCode, readdedClientHandle);
    }
  }

  boolean applyDeleteResult(
      StatusCode statusCode,
      @Nullable UInteger readdedClientHandle,
      PreparedDeleteRequest prepared) {

    synchronized (stateLock) {
      if (this.itemIncarnation != prepared.itemIncarnation) {
        return false;
      }

      applyDeleteResultLocked(statusCode, readdedClientHandle);
      return true;
    }
  }

  /** Must be called while holding {@link #stateLock}. */
  private void applyDeleteResultLocked(
      StatusCode statusCode, @Nullable UInteger readdedClientHandle) {

    itemIncarnation++;
    createInFlight = false;
    activeModifies = 0;
    setMonitoringModeInFlight = false;
    syncState = SyncState.INITIAL;
    serverState = null;
    modifications = null;

    if (readdedClientHandle != null) {
      clientHandle = readdedClientHandle;
    } else {
      clientHandle = null;
      owner = null;
    }

    deleteResult = statusCode;
  }

  Optional<PreparedSetMonitoringModeRequest> tryBeginSetMonitoringMode() {
    synchronized (stateLock) {
      ServerState currentServerState = this.serverState;
      if (currentServerState == null || setMonitoringModeInFlight) {
        return Optional.empty();
      }

      setMonitoringModeInFlight = true;
      long generation = ++monitoringModeGeneration;

      return Optional.of(
          new PreparedSetMonitoringModeRequest(
              currentServerState.monitoredItemId, this.itemIncarnation, generation));
    }
  }

  void releaseSetMonitoringMode(PreparedSetMonitoringModeRequest prepared) {
    synchronized (stateLock) {
      releaseSetMonitoringModeLocked(prepared);
    }
  }

  void applySetMonitoringModeResult(
      StatusCode statusCode, PreparedSetMonitoringModeRequest prepared) {

    synchronized (stateLock) {
      releaseSetMonitoringModeLocked(prepared);

      if (this.itemIncarnation != prepared.itemIncarnation
          || this.monitoringModeGeneration != prepared.modeGeneration) {
        return;
      }

      this.setMonitoringModeResult = statusCode;
    }
  }

  void applySetMonitoringModeResult(StatusCode statusCode, MonitoringMode monitoringMode) {
    synchronized (stateLock) {
      applySetMonitoringModeResultLocked(statusCode, monitoringMode);
    }
  }

  void applySetMonitoringModeResult(
      StatusCode statusCode,
      MonitoringMode monitoringMode,
      PreparedSetMonitoringModeRequest prepared) {

    synchronized (stateLock) {
      releaseSetMonitoringModeLocked(prepared);

      if (this.itemIncarnation != prepared.itemIncarnation
          || this.monitoringModeGeneration != prepared.modeGeneration) {
        return;
      }

      applySetMonitoringModeResultLocked(statusCode, monitoringMode);
    }
  }

  /** Must be called while holding {@link #stateLock}. */
  private void applySetMonitoringModeResultLocked(
      StatusCode statusCode, MonitoringMode monitoringMode) {

    this.setMonitoringModeResult = statusCode;

    if (statusCode.isGood()) {
      this.monitoringMode = monitoringMode;

      ServerState currentServerState = this.serverState;
      if (currentServerState != null) {
        this.serverState = currentServerState.withMonitoringMode(monitoringMode);
      }
    }
  }

  /** Must be called while holding {@link #stateLock}. */
  private void releaseSetMonitoringModeLocked(PreparedSetMonitoringModeRequest prepared) {
    if (!prepared.released) {
      prepared.released = true;
      if (this.itemIncarnation == prepared.itemIncarnation
          && this.monitoringModeGeneration == prepared.modeGeneration) {
        setMonitoringModeInFlight = false;
      }
    }
  }

  final boolean detachIfNotCreated() {
    synchronized (stateLock) {
      if (createInFlight || activeModifies > 0 || setMonitoringModeInFlight) {
        return false;
      }

      if (syncState == SyncState.INITIAL || serverState == null) {
        itemIncarnation++;
        syncState = SyncState.INITIAL;
        modifications = null;
        clientHandle = null;
        owner = null;
        return true;
      }

      return false;
    }
  }

  final boolean needsServerDeletion() {
    synchronized (stateLock) {
      return createInFlight
          || activeModifies > 0
          || setMonitoringModeInFlight
          || syncState != SyncState.INITIAL;
    }
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

  void reset() {
    synchronized (stateLock) {
      itemIncarnation++;
      createInFlight = false;
      activeModifies = 0;
      setMonitoringModeInFlight = false;
      syncState = SyncState.INITIAL;
      serverState = null;
      modifications = null;

      createResult = null;
      modifyResult = null;
      deleteResult = null;
      setMonitoringModeResult = null;
    }
  }

  /** Must be called while holding {@link #stateLock}. */
  private Modifications getOrCreateModificationsLocked() {
    Modifications pending = this.modifications;
    if (pending == null) {
      pending = new Modifications();
      this.modifications = pending;
    }
    return pending;
  }

  private static @Nullable ExtensionObject encodeFilter(@Nullable MonitoringFilter filter) {
    return filter != null ? ExtensionObject.encode(DefaultEncodingContext.INSTANCE, filter) : null;
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

  static final class PreparedCreateRequest {

    private final ReadValueId readValueId;
    private final UInteger clientHandle;
    private final MonitoringMode monitoringMode;
    private final Double samplingInterval;
    private final @Nullable MonitoringFilter filter;
    private final UInteger queueSize;
    private final boolean discardOldest;
    private final long itemIncarnation;
    private final long parameterGeneration;
    private boolean released;

    private PreparedCreateRequest(
        ReadValueId readValueId,
        UInteger clientHandle,
        MonitoringMode monitoringMode,
        Double samplingInterval,
        @Nullable MonitoringFilter filter,
        UInteger queueSize,
        boolean discardOldest,
        long itemIncarnation,
        long parameterGeneration) {

      this.readValueId = readValueId;
      this.clientHandle = clientHandle;
      this.monitoringMode = monitoringMode;
      this.samplingInterval = samplingInterval;
      this.filter = filter;
      this.queueSize = queueSize;
      this.discardOldest = discardOldest;
      this.itemIncarnation = itemIncarnation;
      this.parameterGeneration = parameterGeneration;
    }

    MonitoredItemCreateRequest toRequest() {
      return new MonitoredItemCreateRequest(
          readValueId,
          monitoringMode,
          new MonitoringParameters(
              clientHandle, samplingInterval, encodeFilter(filter), queueSize, discardOldest));
    }

    long itemIncarnation() {
      return itemIncarnation;
    }

    long parameterGeneration() {
      return parameterGeneration;
    }

    boolean discardOldest() {
      return discardOldest;
    }
  }

  static final class PreparedModifyRequest {

    private final UInteger monitoredItemId;
    private final @Nullable UInteger clientHandle;
    private final Double samplingInterval;
    private final @Nullable MonitoringFilter filter;
    private final UInteger queueSize;
    private final boolean discardOldest;
    private final long itemIncarnation;
    private final long parameterGeneration;
    private boolean released;

    private PreparedModifyRequest(
        UInteger monitoredItemId,
        @Nullable UInteger clientHandle,
        Double samplingInterval,
        @Nullable MonitoringFilter filter,
        UInteger queueSize,
        boolean discardOldest,
        long itemIncarnation,
        long parameterGeneration) {

      this.monitoredItemId = monitoredItemId;
      this.clientHandle = clientHandle;
      this.samplingInterval = samplingInterval;
      this.filter = filter;
      this.queueSize = queueSize;
      this.discardOldest = discardOldest;
      this.itemIncarnation = itemIncarnation;
      this.parameterGeneration = parameterGeneration;
    }

    MonitoredItemModifyRequest toRequest() {
      return new MonitoredItemModifyRequest(
          monitoredItemId,
          new MonitoringParameters(
              clientHandle, samplingInterval, encodeFilter(filter), queueSize, discardOldest));
    }

    long itemIncarnation() {
      return itemIncarnation;
    }

    long parameterGeneration() {
      return parameterGeneration;
    }

    boolean discardOldest() {
      return discardOldest;
    }
  }

  static final class PreparedDeleteRequest {

    private final UInteger monitoredItemId;
    private final long itemIncarnation;

    private PreparedDeleteRequest(UInteger monitoredItemId, long itemIncarnation) {
      this.monitoredItemId = monitoredItemId;
      this.itemIncarnation = itemIncarnation;
    }

    UInteger monitoredItemId() {
      return monitoredItemId;
    }

    long itemIncarnation() {
      return itemIncarnation;
    }
  }

  static final class PreparedSetMonitoringModeRequest {

    private final UInteger monitoredItemId;
    private final long itemIncarnation;
    private final long modeGeneration;
    private boolean released;

    private PreparedSetMonitoringModeRequest(
        UInteger monitoredItemId, long itemIncarnation, long modeGeneration) {

      this.monitoredItemId = monitoredItemId;
      this.itemIncarnation = itemIncarnation;
      this.modeGeneration = modeGeneration;
    }

    UInteger monitoredItemId() {
      return monitoredItemId;
    }

    long itemIncarnation() {
      return itemIncarnation;
    }
  }

  static class Modifications {

    private @Nullable Double samplingInterval;
    private @Nullable MonitoringFilter filter;
    private @Nullable UInteger queueSize;
    private @Nullable Boolean discardOldest;

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

    private ServerState withMonitoringMode(MonitoringMode monitoringMode) {
      return new ServerState(
          monitoredItemId,
          monitoringMode,
          samplingInterval,
          filterResult,
          queueSize,
          discardOldest);
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

  /**
   * Create a new MonitoredItem for the EventNotifier attribute of the Node identified by {@code
   * nodeId}, with the specified {@code monitoringMode} and {@code eventFilter}.
   *
   * <p>This item will not exist on the server until it has been added to an {@link
   * OpcUaSubscription} and the subscription has been synchronized.
   *
   * @param nodeId the {@link NodeId} of the Node to monitor.
   * @param monitoringMode the initial {@link MonitoringMode} to use when the MonitoredItem is
   *     created.
   * @param eventFilter the {@link EventFilter} to use for the MonitoredItem.
   * @return a new MonitoredItem for the EventNotifier attribute of the Node identified by {@code
   *     nodeId}, with the specified {@code monitoringMode} and {@code eventFilter}.
   * @see OpcUaSubscription#addMonitoredItem(OpcUaMonitoredItem)
   * @see OpcUaSubscription#synchronizeMonitoredItems()
   */
  public static OpcUaMonitoredItem newEventItem(
      NodeId nodeId, MonitoringMode monitoringMode, @Nullable EventFilter eventFilter) {
    var readValueId =
        new ReadValueId(nodeId, AttributeId.EventNotifier.uid(), null, QualifiedName.NULL_VALUE);

    var item = new OpcUaMonitoredItem(readValueId, monitoringMode);
    item.setSamplingInterval(0.0);
    item.setFilter(eventFilter);

    return item;
  }
}

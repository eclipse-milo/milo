/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.server;

import static java.util.Objects.requireNonNull;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetListener;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetReaderRef;
import org.eclipse.milo.opcua.sdk.pubsub.MetaDataListener;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubComponents;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubDiagnostics;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubDiagnosticsListener;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubHandle;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubService;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubStateListener;
import org.eclipse.milo.opcua.sdk.pubsub.PublishedDataSetSource;
import org.eclipse.milo.opcua.sdk.pubsub.PublisherStatusListener;
import org.eclipse.milo.opcua.sdk.pubsub.ReconfigureResult;
import org.eclipse.milo.opcua.sdk.pubsub.SecurityKeyInfo;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfigValidationException;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetRef;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The reconfiguration choke point returned by {@link ServerPubSub#runtime()}: a {@link
 * PubSubService} that delegates every call to the underlying runtime and intercepts {@link
 * #reconfigure} and {@link #update} to
 *
 * <ul>
 *   <li>pre-validate the new configuration against the attach-time rules before the engine applies
 *       anything ({@link ServerPubSub#validateNodeFieldAddresses} plus a dry-run wire mapping),
 *       throwing {@link PubSubConfigValidationException} with nothing applied and no hooks run;
 *   <li>advance the mediator-owned ConfigurationVersion exactly once per successful apply — the
 *       single source read by the ns0 {@code ConfigurationVersion} property, persisted
 *       configuration snapshots, and the file-model {@code Size}/{@code LastModifiedTime} values,
 *       so every surface observes one clock and one value per apply;
 *   <li>run the registered {@link ReconfigureHook}s synchronously, in registration order, before
 *       the call returns.
 * </ul>
 *
 * <p>Lock discipline: {@code reconfigureLock} is acquired before the engine lock (taken inside the
 * delegate) and never the other way around; nothing may call into this class while holding the
 * engine lock. Hooks run on the reconfiguring thread after the engine lock has been released but
 * inside {@code reconfigureLock}, so concurrent {@code reconfigure}/{@code update} calls — and
 * their (apply + hook) pairs — serialize. Reconfiguration is a management-plane operation; the
 * added serialization is intentional.
 */
final class ManagedPubSubService implements PubSubService {

  private static final Logger LOGGER = LoggerFactory.getLogger(ManagedPubSubService.class);

  /** The epoch of the OPC UA VersionTime data type: 2000-01-01T00:00:00 UTC. */
  private static final Instant VERSION_TIME_EPOCH = Instant.parse("2000-01-01T00:00:00Z");

  private final Object reconfigureLock = new Object();

  private final PubSubService delegate;
  private final NamespaceTable namespaceTable;
  private final List<ReconfigureHook> hooks;
  private final Consumer<PublishedDataSetRef> bindSourceObserver;

  /**
   * The ConfigurationVersion single source: a VersionTime, seeded at attach and advanced exactly
   * once per successful apply. Writes are guarded by {@code reconfigureLock}; volatile for
   * lock-free reads.
   */
  private volatile UInteger configurationVersion;

  /**
   * Create a mediator wrapping {@code delegate}.
   *
   * @param delegate the raw engine service; retained by {@link ServerPubSub} for internal use and
   *     never handed to API callers.
   * @param namespaceTable the server's {@link NamespaceTable}, used by pre-validation.
   * @param hooks the post-apply hooks in registration order; fixed at construction (registered in
   *     {@link ServerPubSub}'s constructor).
   * @param initialConfigurationVersion the seed version: the store-loaded non-zero version, else
   *     the attach-time {@link #versionTimeNow()}.
   * @param bindSourceObserver observes {@link #bindSource} calls (after successful delegation) so
   *     {@link ServerPubSub} can track user-bound sources and never override them when re-deriving
   *     the automatic bindings after a reconfiguration; pure observation, the delegation itself is
   *     unchanged.
   */
  ManagedPubSubService(
      PubSubService delegate,
      NamespaceTable namespaceTable,
      List<ReconfigureHook> hooks,
      UInteger initialConfigurationVersion,
      Consumer<PublishedDataSetRef> bindSourceObserver) {

    this.delegate = delegate;
    this.namespaceTable = namespaceTable;
    this.hooks = List.copyOf(hooks);
    this.configurationVersion = initialConfigurationVersion;
    this.bindSourceObserver = bindSourceObserver;
  }

  /**
   * Get the current ConfigurationVersion: a VersionTime that advances exactly once per successful
   * apply. All observable surfaces (the ns0 {@code ConfigurationVersion} property, persisted
   * snapshots, the file-model values) read this value — never a locally sampled clock.
   *
   * @return the current configuration version.
   */
  UInteger configurationVersion() {
    return configurationVersion;
  }

  /** The current time as an OPC UA VersionTime: seconds since 2000-01-01T00:00:00 UTC. */
  static UInteger versionTimeNow() {
    long seconds = Instant.now().getEpochSecond() - VERSION_TIME_EPOCH.getEpochSecond();
    return uint(seconds & 0xFFFFFFFFL);
  }

  @Override
  public ReconfigureResult reconfigure(PubSubConfig newConfig, ReconfigureMode mode) {
    synchronized (reconfigureLock) {
      preValidate(newConfig);

      ReconfigureResult result = delegate.reconfigure(newConfig, mode);

      bumpConfigurationVersion();
      runHooks(newConfig, result);

      return result;
    }
  }

  @Override
  public ReconfigureResult update(UnaryOperator<PubSubConfig> transform) {
    synchronized (reconfigureLock) {
      var captured = new AtomicReference<PubSubConfig>();

      // the wrapped transform runs under the engine lock; a preValidate throw propagates out
      // before the engine diffs/applies anything, so nothing was applied and the capture is
      // never consumed (the hooks are skipped because delegate.update threw)
      ReconfigureResult result =
          delegate.update(
              current -> {
                PubSubConfig next = transform.apply(current);
                preValidate(next);
                captured.set(next);
                return next;
              });

      PubSubConfig newConfig =
          requireNonNull(captured.get(), "delegate did not apply the transform");

      bumpConfigurationVersion();
      runHooks(newConfig, result);

      return result;
    }
  }

  /**
   * Enforce the attach-time validation rules on {@code newConfig} before any change is applied:
   * every NodeFieldAddress must resolve against the server's {@link NamespaceTable}, every
   * TargetVariables index range must parse, and the config must map to its wire form.
   *
   * <p>The dry-run {@code toDataType} matters because the post-apply hooks map the config again;
   * without it an unresolvable namespace URI would apply in the engine and then blow up in a hook —
   * a permanent model/engine desync.
   *
   * @throws PubSubConfigValidationException if validation fails; nothing has been applied.
   */
  private void preValidate(PubSubConfig newConfig) {
    ServerPubSub.validateNodeFieldAddresses(newConfig, namespaceTable);
    newConfig.toDataType(namespaceTable);
  }

  /**
   * Advance the configuration version. Called holding {@code reconfigureLock}, after the delegate
   * returned and before the hooks run, so every hook (and every surface it refreshes) observes the
   * new value. Strictly increasing even for applies within the same VersionTime second (modulo the
   * UInt32 wrap).
   */
  private void bumpConfigurationVersion() {
    UInteger now = versionTimeNow();
    UInteger current = configurationVersion;

    configurationVersion =
        now.longValue() > current.longValue() ? now : uint((current.longValue() + 1) & 0xFFFFFFFFL);
  }

  /**
   * Run the hooks in registration order. A hook failure is logged and isolated: the engine change
   * is already applied (it is the truth), the remaining hooks still run, and the result is still
   * returned — callers must be able to trust that a returned result means "applied".
   */
  private void runHooks(PubSubConfig newConfig, ReconfigureResult result) {
    for (ReconfigureHook hook : hooks) {
      try {
        hook.onApplied(newConfig, result);
      } catch (Exception e) {
        LOGGER.error("ReconfigureHook failed; the configuration change is already applied", e);
      }
    }
  }

  // region plain delegation

  @Override
  public CompletableFuture<PubSubService> startup() {
    // complete with this mediator, never the raw delegate: leaking the delegate would hand
    // callers a reconfigure path that bypasses validation, versioning, and the hooks
    return delegate.startup().thenApply(service -> this);
  }

  @Override
  public CompletableFuture<Void> shutdown() {
    return delegate.shutdown();
  }

  @Override
  public PubSubComponents components() {
    return delegate.components();
  }

  @Override
  public void enable(PubSubHandle handle) {
    delegate.enable(handle);
  }

  @Override
  public void disable(PubSubHandle handle) {
    delegate.disable(handle);
  }

  @Override
  public PubSubState state(PubSubHandle handle) {
    return delegate.state(handle);
  }

  @Override
  public void bindSource(PublishedDataSetRef dataSet, PublishedDataSetSource source) {
    delegate.bindSource(dataSet, source);
    bindSourceObserver.accept(dataSet);
  }

  @Override
  public void publishEvent(PublishedDataSetRef dataSet, List<Variant> fields) {
    delegate.publishEvent(dataSet, fields);
  }

  @Override
  public void addDataSetListener(DataSetListener listener) {
    delegate.addDataSetListener(listener);
  }

  @Override
  public void removeDataSetListener(DataSetListener listener) {
    delegate.removeDataSetListener(listener);
  }

  @Override
  public void addDataSetListener(DataSetReaderRef reader, DataSetListener listener) {
    delegate.addDataSetListener(reader, listener);
  }

  @Override
  public void removeDataSetListener(DataSetReaderRef reader, DataSetListener listener) {
    delegate.removeDataSetListener(reader, listener);
  }

  @Override
  public void addStateListener(PubSubStateListener listener) {
    delegate.addStateListener(listener);
  }

  @Override
  public void removeStateListener(PubSubStateListener listener) {
    delegate.removeStateListener(listener);
  }

  @Override
  public void addMetaDataListener(MetaDataListener listener) {
    delegate.addMetaDataListener(listener);
  }

  @Override
  public void removeMetaDataListener(MetaDataListener listener) {
    delegate.removeMetaDataListener(listener);
  }

  @Override
  public void addPublisherStatusListener(PublisherStatusListener listener) {
    delegate.addPublisherStatusListener(listener);
  }

  @Override
  public void removePublisherStatusListener(PublisherStatusListener listener) {
    delegate.removePublisherStatusListener(listener);
  }

  @Override
  public void addDiagnosticsListener(PubSubDiagnosticsListener listener) {
    delegate.addDiagnosticsListener(listener);
  }

  @Override
  public void removeDiagnosticsListener(PubSubDiagnosticsListener listener) {
    delegate.removeDiagnosticsListener(listener);
  }

  @Override
  public PubSubDiagnostics diagnostics() {
    return delegate.diagnostics();
  }

  @Override
  public @Nullable SecurityKeyInfo securityKeyInfo(PubSubHandle group) {
    return delegate.securityKeyInfo(group);
  }

  @Override
  public UInteger nextDataSetMessageSequenceNumber(PubSubHandle writer) {
    return delegate.nextDataSetMessageSequenceNumber(writer);
  }

  @Override
  public void close() {
    delegate.close();
  }

  // endregion

  /**
   * Invoked after a successful configuration apply, in registration order (fixed in {@link
   * ServerPubSub}'s constructor), on the reconfiguring thread, inside the mediator's critical
   * section — after the engine lock has been released and the ConfigurationVersion has advanced.
   *
   * <p>A hook failure is logged and isolated: the remaining hooks still run and the result is still
   * returned. Hooks MUST NOT call {@link PubSubService#reconfigure} or {@link PubSubService#update}
   * — the reentrant critical section would nest an apply inside a hook pass. Read-side engine API
   * ({@code components()}, {@code state()}, {@code diagnostics()}) is safe.
   */
  @FunctionalInterface
  interface ReconfigureHook {

    /**
     * Notified of a successful configuration apply.
     *
     * @param newConfig the configuration that is now current.
     * @param result the {@link ReconfigureResult} of the apply.
     */
    void onApplied(PubSubConfig newConfig, ReconfigureResult result);
  }
}

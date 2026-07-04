/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub;

import java.util.concurrent.CompletableFuture;
import java.util.function.UnaryOperator;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetRef;
import org.eclipse.milo.opcua.sdk.pubsub.internal.PubSubServiceImpl;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;
import org.jspecify.annotations.Nullable;

/**
 * The standalone OPC UA Part 14 PubSub runtime: publishes and subscribes to NetworkMessages
 * according to a {@link PubSubConfig}, independent of any OPC UA client or server.
 *
 * <p>Lifecycle: {@link #create create} an instance from a validated config, optionally with {@link
 * PubSubBindings} (data sources, listeners, key providers) and a {@link PubSubServiceConfig}
 * (executors, providers), then {@link #startup()}. Reconfiguration happens by replacement via
 * {@link #reconfigure} or {@link #update}; configuration objects are never mutated in place.
 */
public interface PubSubService extends AutoCloseable {

  /**
   * Create a new {@link PubSubService} from the given config.
   *
   * @param config the {@link PubSubConfig} describing the PubSub components.
   * @return a new {@link PubSubService}, not yet started.
   */
  static PubSubService create(PubSubConfig config) {
    return PubSubServiceImpl.create(config, null, null);
  }

  /**
   * Create a new {@link PubSubService} from the given config and bindings.
   *
   * @param config the {@link PubSubConfig} describing the PubSub components.
   * @param bindings the {@link PubSubBindings} supplying data sources, listeners, and key providers
   *     for the names in {@code config}.
   * @return a new {@link PubSubService}, not yet started.
   */
  static PubSubService create(PubSubConfig config, PubSubBindings bindings) {
    return PubSubServiceImpl.create(config, bindings, null);
  }

  /**
   * Create a new {@link PubSubService} from the given config, bindings, and service config.
   *
   * @param config the {@link PubSubConfig} describing the PubSub components.
   * @param bindings the {@link PubSubBindings} supplying data sources, listeners, and key providers
   *     for the names in {@code config}.
   * @param serviceConfig the {@link PubSubServiceConfig} describing the runtime environment.
   * @return a new {@link PubSubService}, not yet started.
   */
  static PubSubService create(
      PubSubConfig config, PubSubBindings bindings, PubSubServiceConfig serviceConfig) {

    return PubSubServiceImpl.create(config, bindings, serviceConfig);
  }

  /**
   * Start the service: open transports and start publishing and subscribing for all enabled
   * components.
   *
   * @return a {@link CompletableFuture} that completes with this service once startup has finished,
   *     or completes exceptionally if startup fails, e.g. because a transport could not be bound, a
   *     required binding is missing, or an unsupported feature is configured.
   */
  CompletableFuture<PubSubService> startup();

  /**
   * Stop the service: stop publishing and subscribing and release all transport resources.
   *
   * @return a {@link CompletableFuture} that completes once shutdown has finished, including
   *     delivery of the shutdown-induced state change events to registered listeners.
   */
  CompletableFuture<Void> shutdown();

  /**
   * Get the component registry used to look up {@link PubSubHandle}s by name.
   *
   * @return the {@link PubSubComponents} registry.
   */
  PubSubComponents components();

  /**
   * Enable the component identified by {@code handle}.
   *
   * @param handle the handle of the component to enable.
   * @throws IllegalArgumentException if the handle does not belong to this service or has been
   *     invalidated by reconfiguration.
   */
  void enable(PubSubHandle handle);

  /**
   * Disable the component identified by {@code handle}.
   *
   * @param handle the handle of the component to disable.
   * @throws IllegalArgumentException if the handle does not belong to this service or has been
   *     invalidated by reconfiguration.
   */
  void disable(PubSubHandle handle);

  /**
   * Get the current {@link PubSubState} of the component identified by {@code handle}.
   *
   * @param handle the handle of the component.
   * @return the current {@link PubSubState} of the component.
   * @throws IllegalArgumentException if the handle does not belong to this service or has been
   *     invalidated by reconfiguration.
   */
  PubSubState state(PubSubHandle handle);

  /**
   * Replace the current configuration with {@code newConfig}, applying the difference between the
   * two configs to the running components.
   *
   * <p>Components affected by the difference (changed components, their descendants, and removed or
   * added components) are stopped and replaced according to {@code mode}; handles for removed
   * components are invalidated.
   *
   * <p>Path-stable restarts (the {@code CHANGED} entries of the result) preserve sequence
   * numbering: a restarted DataSetWriter continues its DataSetMessage sequence where the replaced
   * runtime left off, and a restarted WriterGroup continues its NetworkMessage sequence, so
   * subscribers see one uninterrupted stream with no drop window — unless the change switches the
   * mapping's sequence-number wire width (UInt16 for UADP, UInt32 for JSON), in which case the
   * writer's sequence restarts at 0 (Part 14 §7.2.3: the sequence "starts at 0"). A component
   * removed and re-added by later reconfigurations also restarts at 0. Where a restart at 0 occurs,
   * subscribers see a backward jump on that stream and drop its messages until their
   * sequence-number records recover — by whichever of these comes first: immediately, when the
   * replaced writer's first keep-alive arrives and its carried next-expected value reseeds the
   * subscriber's window (§7.2.4.5.8 — the publisher is authoritative about its own counter; works
   * in both timeout modes, but requires the group to emit keep-alives); after two times their
   * {@code messageReceiveTimeout} of rejected traffic (the §7.2.3 record discard); or — when no
   * timeout is configured — after 16 consecutively rejected messages (Milo's restart-recovery
   * extension). NetworkMessage streams carry no keep-alive reseed.
   *
   * <p>Diagnostic counters follow the same rule: components on {@code restartedPaths} keep their
   * counters, {@code lastError}, and TimeFirstChange baselines; components on {@code removedPaths}
   * are zeroed, and removal plus re-addition across two calls starts fresh.
   *
   * @param newConfig the new {@link PubSubConfig}.
   * @param mode the {@link ReconfigureMode} governing how affected components are restarted.
   * @return a {@link ReconfigureResult} listing the added, removed, and restarted components.
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException, before any change is applied, if
   *     the new configuration fails a validation that {@code startup()} also enforces: {@code
   *     Bad_ConfigurationError} for a mapping name with no provider, an enabled DataSetReader on a
   *     broker connection without a configured data queueName, an enabled DataSetWriter on a broker
   *     connection overriding requestedDeliveryGuarantee without a writer-level queueName (Part 14
   *     §6.4.2.5.4), an enabled writer group on a UDP connection with a maxNetworkMessageSize above
   *     65535, or an enabled writer asking for delta frames ({@code keyFrameCount > 1}) its masks
   *     cannot express or safely carry; {@code Bad_NotSupported} for an enabled UADP-mapped writer
   *     group configuring PromotedFields emission, or an enabled writer in one configuring the
   *     RawData field encoding.
   */
  ReconfigureResult reconfigure(PubSubConfig newConfig, ReconfigureMode mode);

  /**
   * Replace the current configuration by applying {@code transform} to it, using {@link
   * ReconfigureMode#DISABLE_AFFECTED}.
   *
   * @param transform a function producing the new {@link PubSubConfig} from the current one.
   * @return a {@link ReconfigureResult} listing the added, removed, and restarted components.
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException, before any change is applied, if
   *     the transformed configuration fails a validation that {@code startup()} also enforces:
   *     {@code Bad_ConfigurationError} for a mapping name with no provider, an enabled
   *     DataSetReader on a broker connection without a configured data queueName, an enabled
   *     DataSetWriter on a broker connection overriding requestedDeliveryGuarantee without a
   *     writer-level queueName (Part 14 §6.4.2.5.4), an enabled writer group on a UDP connection
   *     with a maxNetworkMessageSize above 65535, or an enabled writer asking for delta frames
   *     ({@code keyFrameCount > 1}) its masks cannot express or safely carry; {@code
   *     Bad_NotSupported} for an enabled UADP-mapped writer group configuring PromotedFields
   *     emission, or an enabled writer in one configuring the RawData field encoding.
   */
  ReconfigureResult update(UnaryOperator<PubSubConfig> transform);

  /**
   * Bind a {@link PublishedDataSetSource} to the PublishedDataSet referenced by {@code dataSet},
   * replacing any existing binding.
   *
   * <p>Every PublishedDataSet referenced by an enabled DataSetWriter must have a source bound,
   * either here or via {@link PubSubBindings}, before startup.
   *
   * @param dataSet the reference to the PublishedDataSet to bind.
   * @param source the source pulled for a snapshot each publish cycle.
   */
  void bindSource(PublishedDataSetRef dataSet, PublishedDataSetSource source);

  /**
   * Add a listener notified of every DataSet received by any DataSetReader.
   *
   * @param listener the listener to add.
   */
  void addDataSetListener(DataSetListener listener);

  /**
   * Remove a listener added via {@link #addDataSetListener(DataSetListener)}. Identity-based;
   * removing a listener that was never added is a no-op.
   *
   * @param listener the listener to remove.
   */
  void removeDataSetListener(DataSetListener listener);

  /**
   * Add a listener notified of every DataSet received by the DataSetReader identified by {@code
   * reader}.
   *
   * @param reader the reference to the DataSetReader to listen to.
   * @param listener the listener to add.
   */
  void addDataSetListener(DataSetReaderRef reader, DataSetListener listener);

  /**
   * Remove a listener added via {@link #addDataSetListener(DataSetReaderRef, DataSetListener)} for
   * {@code reader}. Identity-based; removing a listener that was never added is a no-op. Per-reader
   * listeners registered through {@link PubSubBindings} share the same registry and are removable
   * the same way.
   *
   * @param reader the reference the listener was added for.
   * @param listener the listener to remove.
   */
  void removeDataSetListener(DataSetReaderRef reader, DataSetListener listener);

  /**
   * Add a listener notified of component state changes.
   *
   * @param listener the listener to add.
   */
  void addStateListener(PubSubStateListener listener);

  /**
   * Remove a listener added via {@link #addStateListener(PubSubStateListener)}. Identity-based;
   * removing a listener that was never added is a no-op.
   *
   * @param listener the listener to remove.
   */
  void removeStateListener(PubSubStateListener listener);

  /**
   * Add a listener notified of DataSetMetaData received from the wire.
   *
   * @param listener the listener to add.
   */
  void addMetaDataListener(MetaDataListener listener);

  /**
   * Remove a listener added via {@link #addMetaDataListener(MetaDataListener)}. Identity-based;
   * removing a listener that was never added is a no-op.
   *
   * @param listener the listener to remove.
   */
  void removeMetaDataListener(MetaDataListener listener);

  /**
   * Add a listener notified of diagnostic events. In v1 only error events are delivered.
   *
   * @param listener the listener to add.
   */
  void addDiagnosticsListener(PubSubDiagnosticsListener listener);

  /**
   * Remove a listener added via {@link #addDiagnosticsListener(PubSubDiagnosticsListener)}.
   * Identity-based; removing a listener that was never added is a no-op.
   *
   * @param listener the listener to remove.
   */
  void removeDiagnosticsListener(PubSubDiagnosticsListener listener);

  /**
   * Get the diagnostics view of this service.
   *
   * @return the {@link PubSubDiagnostics} for this service.
   */
  PubSubDiagnostics diagnostics();

  /**
   * Get the live security-key state of the secured writer or reader group identified by {@code
   * group}: SecurityGroupId, negotiated policy URI, current SecurityTokenId, and time to the next
   * key switch — token metadata only, never key material. Feeds the Part 14 §9.1.11 {@code
   * SecurityTokenID}/{@code TimeToNextTokenID} LiveValues (Tables 322/331).
   *
   * @param group the handle of a WriterGroup or ReaderGroup.
   * @return the current {@link SecurityKeyInfo}, or {@code null} while the group holds no key
   *     state: not secured, keys not yet fetched, or currently detached (Disabled/Paused).
   * @throws IllegalArgumentException if the handle is not a writer/reader group of this service or
   *     has been invalidated by reconfiguration.
   */
  @Nullable SecurityKeyInfo securityKeyInfo(PubSubHandle group);

  /**
   * Get the next DataSetMessage sequence number of the DataSetWriter identified by {@code writer} —
   * the value its next data message will carry and its keep-alives advertise (Part 14 §7.2.4.5.8).
   * Feeds the Table 329 {@code MessageSequenceNumber} LiveValue; the wire width follows the group's
   * mapping (UInt16 for UADP, UInt32 for JSON). A concurrent publish cycle may consume the returned
   * value immediately.
   *
   * @param writer the handle of a DataSetWriter.
   * @return the next DataSetMessage sequence number.
   * @throws IllegalArgumentException if the handle is not a DataSetWriter of this service or has
   *     been invalidated by reconfiguration.
   */
  UInteger nextDataSetMessageSequenceNumber(PubSubHandle writer);

  /** Shut down this service synchronously, waiting for {@link #shutdown()} to complete. */
  @Override
  void close();

  /** Governs how components affected by {@link #reconfigure} are restarted. */
  enum ReconfigureMode {

    /** Only the affected components are disabled, replaced, and re-enabled. */
    DISABLE_AFFECTED,

    /** The whole connection containing an affected component is stopped and restarted. */
    STOP_AND_RESTART
  }
}

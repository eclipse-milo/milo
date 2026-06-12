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
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;

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
   * @param newConfig the new {@link PubSubConfig}.
   * @param mode the {@link ReconfigureMode} governing how affected components are restarted.
   * @return a {@link ReconfigureResult} listing the added, removed, and restarted components.
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException with {@code
   *     Bad_ConfigurationError}, before any change is applied, if the new configuration fails a
   *     validation that {@code startup()} also enforces (a mapping name with no provider, or an
   *     enabled DataSetReader on a broker connection without a configured data queueName).
   */
  ReconfigureResult reconfigure(PubSubConfig newConfig, ReconfigureMode mode);

  /**
   * Replace the current configuration by applying {@code transform} to it, using {@link
   * ReconfigureMode#DISABLE_AFFECTED}.
   *
   * @param transform a function producing the new {@link PubSubConfig} from the current one.
   * @return a {@link ReconfigureResult} listing the added, removed, and restarted components.
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException with {@code
   *     Bad_ConfigurationError}, before any change is applied, if the transformed configuration
   *     fails a validation that {@code startup()} also enforces (a mapping name with no provider,
   *     or an enabled DataSetReader on a broker connection without a configured data queueName).
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
   * Add a listener notified of every DataSet received by the DataSetReader identified by {@code
   * reader}.
   *
   * @param reader the reference to the DataSetReader to listen to.
   * @param listener the listener to add.
   */
  void addDataSetListener(DataSetReaderRef reader, DataSetListener listener);

  /**
   * Add a listener notified of component state changes.
   *
   * @param listener the listener to add.
   */
  void addStateListener(PubSubStateListener listener);

  /**
   * Add a listener notified of DataSetMetaData received from the wire.
   *
   * @param listener the listener to add.
   */
  void addMetaDataListener(MetaDataListener listener);

  /**
   * Add a listener notified of diagnostic events. In v1 only error events are delivered.
   *
   * @param listener the listener to add.
   */
  void addDiagnosticsListener(PubSubDiagnosticsListener listener);

  /**
   * Get the diagnostics view of this service.
   *
   * @return the {@link PubSubDiagnostics} for this service.
   */
  PubSubDiagnostics diagnostics();

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

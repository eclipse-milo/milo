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

import java.util.Objects;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetListener;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubBindings;
import org.eclipse.milo.opcua.sdk.pubsub.PublishedDataSetSource;
import org.eclipse.milo.opcua.sdk.pubsub.config.KeyFieldAddress;
import org.jspecify.annotations.Nullable;

/**
 * Options governing how {@link ServerPubSub} attaches a PubSub runtime to a server.
 *
 * <p>Instances are immutable; obtain one via {@link #builder()}.
 */
public final class ServerPubSubOptions {

  private final boolean exposeInformationModel;
  private final boolean allowRemoteConfiguration;
  private final @Nullable PubSubConfigurationStore configurationStore;
  private final boolean diagnosticsEnabled;
  private final boolean statusEventsEnabled;
  private final PubSubBindings bindings;
  private final boolean securityKeyServerEnabled;
  private final @Nullable PubSubMethodAuthorizer methodAuthorizer;

  private ServerPubSubOptions(Builder builder) {
    this.exposeInformationModel = builder.exposeInformationModel;
    this.allowRemoteConfiguration = builder.allowRemoteConfiguration;
    this.configurationStore = builder.configurationStore;
    this.diagnosticsEnabled = builder.diagnosticsEnabled;
    this.statusEventsEnabled = builder.statusEventsEnabled;
    this.bindings = builder.bindings;
    this.securityKeyServerEnabled = builder.securityKeyServerEnabled;
    this.methodAuthorizer = builder.methodAuthorizer;
  }

  /**
   * Get whether the read-only PublishSubscribe information model subtree is exposed in the server's
   * address space.
   *
   * @return {@code true} if the information model is exposed; defaults to {@code false}.
   */
  public boolean isExposeInformationModel() {
    return exposeInformationModel;
  }

  /**
   * Get whether remote configuration of the PubSub runtime via the server's information model is
   * allowed.
   *
   * <p>See {@link Builder#allowRemoteConfiguration(boolean)} for what {@code true} activates.
   *
   * @return {@code true} if remote configuration is allowed; defaults to {@code false}.
   */
  public boolean isAllowRemoteConfiguration() {
    return allowRemoteConfiguration;
  }

  /**
   * Get the store used to persist and load the PubSub configuration.
   *
   * @return the configuration store, or {@code null} if none is configured.
   */
  public @Nullable PubSubConfigurationStore getConfigurationStore() {
    return configurationStore;
  }

  /**
   * Get whether the Part 14 §9.1.11 PubSub diagnostics exposure is enabled.
   *
   * <p>See {@link Builder#diagnosticsEnabled(boolean)} for what {@code true} activates; effective
   * only together with {@link #isExposeInformationModel()}.
   *
   * @return {@code true} if diagnostics exposure is enabled; defaults to {@code false}.
   */
  public boolean isDiagnosticsEnabled() {
    return diagnosticsEnabled;
  }

  /**
   * Get whether the Part 14 §9.1.13 PubSub status-event bridge is enabled.
   *
   * <p>See {@link Builder#statusEventsEnabled(boolean)} for what {@code true} activates;
   * independent of both {@link #isExposeInformationModel()} and {@link #isDiagnosticsEnabled()}.
   *
   * @return {@code true} if status events are enabled; defaults to {@code false}.
   */
  public boolean isStatusEventsEnabled() {
    return statusEventsEnabled;
  }

  /**
   * Get the caller-supplied {@link PubSubBindings} merged into the bindings derived by {@link
   * ServerPubSub}.
   *
   * @return the caller-supplied bindings; possibly empty.
   */
  public PubSubBindings getBindings() {
    return bindings;
  }

  /**
   * Get whether the opt-in Security Key Service server face is enabled: a {@code GetSecurityKeys}
   * handler attached to the ns0 method node ({@code i=15215}) that generates and rotates keys for
   * the configured SecurityGroups and advertises {@code SupportSecurityKeyServer=true}.
   *
   * @return {@code true} if the SKS server face is enabled; defaults to {@code false}.
   */
  public boolean isSecurityKeyServerEnabled() {
    return securityKeyServerEnabled;
  }

  /**
   * Get the authorizer consulted by the PubSub-related Method handlers — the ns0 file-model, {@code
   * GetSecurityKeys}, and root Reset handlers as well as the per-component Enable/Disable and Reset
   * handlers minted in the server namespace by the exposed information model.
   *
   * @return the configured {@link PubSubMethodAuthorizer}, or {@code null} if the default posture
   *     applies.
   */
  public @Nullable PubSubMethodAuthorizer getMethodAuthorizer() {
    return methodAuthorizer;
  }

  /**
   * Create a {@link Builder} pre-populated with the values of these options.
   *
   * @return a new {@link Builder}.
   */
  public Builder toBuilder() {
    Builder builder = new Builder();
    builder.exposeInformationModel = exposeInformationModel;
    builder.allowRemoteConfiguration = allowRemoteConfiguration;
    builder.configurationStore = configurationStore;
    builder.diagnosticsEnabled = diagnosticsEnabled;
    builder.statusEventsEnabled = statusEventsEnabled;
    builder.bindings = bindings;
    builder.securityKeyServerEnabled = securityKeyServerEnabled;
    builder.methodAuthorizer = methodAuthorizer;
    return builder;
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof ServerPubSubOptions that)) {
      return false;
    }
    return exposeInformationModel == that.exposeInformationModel
        && allowRemoteConfiguration == that.allowRemoteConfiguration
        && Objects.equals(configurationStore, that.configurationStore)
        && diagnosticsEnabled == that.diagnosticsEnabled
        && statusEventsEnabled == that.statusEventsEnabled
        && bindings.equals(that.bindings)
        && securityKeyServerEnabled == that.securityKeyServerEnabled
        && Objects.equals(methodAuthorizer, that.methodAuthorizer);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        exposeInformationModel,
        allowRemoteConfiguration,
        configurationStore,
        diagnosticsEnabled,
        statusEventsEnabled,
        bindings,
        securityKeyServerEnabled,
        methodAuthorizer);
  }

  /**
   * Create a new {@link Builder}.
   *
   * @return a new {@link Builder}.
   */
  public static Builder builder() {
    return new Builder();
  }

  /** A builder of {@link ServerPubSubOptions} instances. */
  public static final class Builder {

    private boolean exposeInformationModel = false;
    private boolean allowRemoteConfiguration = false;
    private @Nullable PubSubConfigurationStore configurationStore;
    private boolean diagnosticsEnabled = false;
    private boolean statusEventsEnabled = false;
    private PubSubBindings bindings = PubSubBindings.builder().build();
    private boolean securityKeyServerEnabled = false;
    private @Nullable PubSubMethodAuthorizer methodAuthorizer;

    private Builder() {}

    /**
     * Set whether the read-only PublishSubscribe information model subtree is exposed in the
     * server's address space.
     *
     * @param value {@code true} to expose the information model.
     * @return this {@link Builder}.
     */
    public Builder exposeInformationModel(boolean value) {
      this.exposeInformationModel = value;
      return this;
    }

    /**
     * Set whether remote configuration of the PubSub runtime via the server's information model is
     * allowed.
     *
     * <p>When {@code true}, the writable PubSub surfaces are activated: the exposed information
     * model (see {@link #exposeInformationModel(boolean)}) mints {@code Enable}/{@code Disable}
     * Methods on every component Status object. Every handler consults the effective {@link
     * PubSubMethodAuthorizer} (see {@link #methodAuthorizer(PubSubMethodAuthorizer)}) before
     * acting. The ns0 {@code PubSubConfiguration} file model ({@code i=25451}) is also served: its
     * FileType Methods together with {@code ReserveIds} and {@code CloseAndUpdate} accept remote
     * configuration edits, each applied through the same authorized, validated apply path.
     *
     * <p>Remote enable/disable is not a configuration mutation: it is never saved to a configured
     * {@link PubSubConfigurationStore}.
     *
     * <p>Defaults to {@code false}: remote configuration is an explicit opt-in, which is also what
     * justifies the default authorizer's allow-when-no-RoleMapper posture.
     *
     * @param value {@code true} to allow remote configuration.
     * @return this {@link Builder}.
     */
    public Builder allowRemoteConfiguration(boolean value) {
      this.allowRemoteConfiguration = value;
      return this;
    }

    /**
     * Set the store used to persist and load the PubSub configuration.
     *
     * @param store the configuration store.
     * @return this {@link Builder}.
     */
    public Builder configurationStore(PubSubConfigurationStore store) {
      this.configurationStore = store;
      return this;
    }

    /**
     * Set whether the Part 14 §9.1.11 PubSub diagnostics exposure is enabled (CU "PubSub Model
     * Diagnostics").
     *
     * <p>When {@code true}, the exposed information model serves the PubSub diagnostics: the
     * loader-built ns0 root {@code Diagnostics} subtree ({@code i=17409}) is backed with live
     * values and its {@code Reset} Method ({@code i=17421}) gains a handler, and every exposed
     * connection, writer/reader group, and dataset writer/reader gains a {@code Diagnostics} object
     * ({@code "PubSub/<path>/Diagnostics"}) whose counters and LiveValues are computed per read
     * from the runtime's diagnostics. Counter values saturate at the UInt32 maximum (Part 14
     * §9.1.11.5); DiagnosticsLevel is read-only {@code Basic} with no level-switching machinery (a
     * conformant simplification). Every {@code Reset} handler is gated by the effective {@link
     * PubSubMethodAuthorizer#checkConfigure(org.eclipse.milo.opcua.sdk.server.Session)},
     * independent of {@link #allowRemoteConfiguration(boolean)}.
     *
     * <p>Effective only together with {@link #exposeInformationModel(boolean)} — the diagnostics
     * exposure lives in the information model fragment. Enabling diagnostics without exposing the
     * information model is a WARN-logged no-op.
     *
     * @param value {@code true} to expose diagnostics.
     * @return this {@link Builder}.
     */
    public Builder diagnosticsEnabled(boolean value) {
      this.diagnosticsEnabled = value;
      return this;
    }

    /**
     * Set whether the Part 14 §9.1.13 PubSub status-event bridge is enabled (CUs "PubSub Model
     * Status Event" and "PubSub Model Diagnostics Events").
     *
     * <p>When {@code true}, {@link ServerPubSub} bridges the runtime's state changes and
     * transmission failures to OPC UA events fired with Server-object semantics: clients receive
     * them through event monitored items on the Server object ({@code i=2253}). Every state change
     * of a connection, writer/reader group, or dataset writer/reader emits a {@code
     * PubSubStatusEventType} ({@code i=15535}) event; a NetworkMessage transmission failure emits a
     * {@code PubSubCommunicationFailureEventType} ({@code i=15563}) event carrying the failure's
     * status code — the first failure per outage episode only, re-armed when the affected component
     * (or an ancestor) transitions back to Operational. Informational transitions carry severity
     * 100, Error entries and communication failures severity 500.
     *
     * <p>Independent of {@link #exposeInformationModel(boolean)}: the event fields ({@code
     * SourceNode}, {@code ConnectionId}, {@code GroupId}) always carry the deterministic NodeIds of
     * the information model fragment, so when the model is not exposed they reference
     * non-materialized (non-browsable) NodeIds. Separate from {@link #diagnosticsEnabled(boolean)}
     * (CU "PubSub Model Diagnostics"), which gates the §9.1.11 diagnostics exposure.
     *
     * <p>Firing events requires a started server: call {@code OpcUaServer.startup()} before {@code
     * ServerPubSub.startup()}. Events that cannot be constructed or fired are dropped with a WARN.
     *
     * @param value {@code true} to bridge status events.
     * @return this {@link Builder}.
     */
    public Builder statusEventsEnabled(boolean value) {
      this.statusEventsEnabled = value;
      return this;
    }

    /**
     * Set caller-supplied {@link PubSubBindings} merged into the bindings derived by {@link
     * ServerPubSub}, e.g. a {@link PublishedDataSetSource} for a dataset addressed by {@link
     * KeyFieldAddress} or additional {@link DataSetListener}s.
     *
     * <p>A caller-supplied source wins over the automatic address-space-backed source for the same
     * dataset reference.
     *
     * @param bindings the caller-supplied bindings.
     * @return this {@link Builder}.
     */
    public Builder bindings(PubSubBindings bindings) {
      this.bindings = bindings;
      return this;
    }

    /**
     * Set whether the opt-in Security Key Service server face is enabled.
     *
     * <p>When enabled, {@link ServerPubSub} attaches a {@code GetSecurityKeys} handler to the ns0
     * method node ({@code i=15215}) at startup, generates and rotates keys for the SecurityGroups
     * of the attach-time configuration, and advertises {@code SupportSecurityKeyPull=true}, {@code
     * SupportSecurityKeyPush=false}, and {@code SupportSecurityKeyServer=true}. The face works
     * independently of {@link #exposeInformationModel(boolean)}.
     *
     * <p>The served SecurityGroups follow the applied configuration: each {@code
     * ServerPubSub.runtime()} or remote-configuration apply refreshes the key store — retained
     * groups keep serving their existing keys (with the applied MaxPastKeyCount/MaxFutureKeyCount
     * window bounds), while groups whose SecurityPolicyUri or KeyLifetime changed have their keys
     * invalidated and regenerated (Part 14 §6.2.12.2).
     *
     * @param value {@code true} to enable the SKS server face.
     * @return this {@link Builder}.
     */
    public Builder securityKeyServerEnabled(boolean value) {
      this.securityKeyServerEnabled = value;
      return this;
    }

    /**
     * Set the authorizer consulted by the PubSub-related Method handlers — not only the ns0
     * file-model, {@code GetSecurityKeys}, and root Reset handlers, but also the per-component
     * Enable/Disable and Reset handlers minted in the server namespace for every (including
     * dynamically created) component when the information model is exposed.
     *
     * <p>When none is configured, the default posture described on {@link PubSubMethodAuthorizer}
     * applies.
     *
     * @param methodAuthorizer the {@link PubSubMethodAuthorizer} to consult.
     * @return this {@link Builder}.
     */
    public Builder methodAuthorizer(PubSubMethodAuthorizer methodAuthorizer) {
      this.methodAuthorizer = methodAuthorizer;
      return this;
    }

    /**
     * Build a {@link ServerPubSubOptions} from the configured values.
     *
     * @return a new {@link ServerPubSubOptions}.
     */
    public ServerPubSubOptions build() {
      return new ServerPubSubOptions(this);
    }
  }
}

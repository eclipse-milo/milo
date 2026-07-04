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
  private final PubSubBindings bindings;
  private final boolean securityKeyServerEnabled;
  private final @Nullable PubSubMethodAuthorizer methodAuthorizer;

  private ServerPubSubOptions(Builder builder) {
    this.exposeInformationModel = builder.exposeInformationModel;
    this.allowRemoteConfiguration = builder.allowRemoteConfiguration;
    this.configurationStore = builder.configurationStore;
    this.diagnosticsEnabled = builder.diagnosticsEnabled;
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
   * Get whether PubSub diagnostics are exposed in the server's information model.
   *
   * @return {@code true} if diagnostics exposure is enabled; defaults to {@code false}.
   */
  public boolean isDiagnosticsEnabled() {
    return diagnosticsEnabled;
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
   * Get the authorizer consulted by the PubSub-related ns0 Method handlers.
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
     * acting. The ns0 {@code PubSubConfiguration} file model does not accept remote configuration
     * updates in this version; its Methods answer {@code Bad_NotImplemented}.
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
     * Set whether PubSub diagnostics are exposed in the server's information model.
     *
     * @param value {@code true} to expose diagnostics.
     * @return this {@link Builder}.
     */
    public Builder diagnosticsEnabled(boolean value) {
      this.diagnosticsEnabled = value;
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
     * <p>The served SecurityGroups reflect the attach-time configuration: reconfiguring via {@code
     * ServerPubSub.runtime()} does not rebuild the key store.
     *
     * @param value {@code true} to enable the SKS server face.
     * @return this {@link Builder}.
     */
    public Builder securityKeyServerEnabled(boolean value) {
      this.securityKeyServerEnabled = value;
      return this;
    }

    /**
     * Set the authorizer consulted by the PubSub-related ns0 Method handlers.
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

/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.aliases;

import java.util.Comparator;
import java.util.Objects;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;

/**
 * Configuration for an alias manager.
 *
 * <p>Every setting has a usable default, so {@code AliasManagerConfig.builder().build()} yields a
 * working configuration — but note that the default {@link AliasVersionStore} is in-memory only and
 * does not satisfy the Part 17 persistence requirement; production applications should supply their
 * own via {@link Builder#versionStore(AliasVersionStore)}.
 */
public interface AliasManagerConfig {

  /**
   * The store used to persist category {@code LastChange} versions across restarts.
   *
   * @return the configured {@link AliasVersionStore}.
   */
  AliasVersionStore getVersionStore();

  /**
   * The policy deciding whether sessions may search or mutate categories over the network.
   *
   * @return the configured {@link AliasAuthorizationPolicy}.
   */
  AliasAuthorizationPolicy getAuthorizationPolicy();

  /**
   * The limits applied to alias lookup and mutation calls.
   *
   * @return the configured {@link AliasLimits}.
   */
  AliasLimits getLimits();

  /**
   * The ordering applied to the targets within each result entry, expressing the application's
   * target preference; Clients use the first usable entry.
   *
   * <p>The default is {@link AliasTarget#DEFAULT_ORDERING}: local targets before remote ones, then
   * by the target NodeId's parseable string form, so results are deterministic.
   *
   * @return the configured target {@link Comparator}.
   */
  Comparator<AliasTarget> getTargetOrdering();

  /**
   * The namespace index used to allocate NodeIds for the Nodes the manager itself creates: the
   * Method Nodes it materializes on the standard ns=0 alias Objects (the Optional Methods the
   * standard NodeSet does not define), and the alias Nodes produced by the default alias NodeId
   * factory of standard and adopted categories.
   *
   * <p>A Node's NodeId namespace is independent of its parent's; this only controls where the
   * manager-allocated identifiers live. Categories added via {@link
   * AliasManager#addCategory(AliasCategoryConfig)} supply their own factory and are unaffected.
   *
   * @return the configured namespace index.
   */
  UShort getNodeNamespaceIndex();

  /**
   * Whether {@code FindAliasVerbose} Method instances are materialized and bound on the standard
   * {@code Aliases}, {@code TagVariables}, and {@code Topics} Objects.
   *
   * @return {@code true} if {@code FindAliasVerbose} is enabled on the standard categories.
   */
  boolean isFindAliasVerboseEnabled();

  /**
   * Whether the {@code AddAliasesToCategory} and {@code DeleteAliasesFromCategory} Methods are
   * materialized and bound on the standard {@code Aliases}, {@code TagVariables}, and {@code
   * Topics} Objects.
   *
   * <p>Materialized Methods are network-callable only for sessions the {@link
   * AliasAuthorizationPolicy} grants mutation to; the default policy denies every session, so
   * enabling network mutation requires this flag <em>and</em> an explicit policy grant.
   *
   * @return {@code true} if the mutation Methods are enabled on the standard categories.
   */
  boolean isConfigurationEnabled();

  /**
   * Create a new {@link Builder} with every setting at its default.
   *
   * @return a new {@link Builder}.
   */
  static Builder builder() {
    return new Builder();
  }

  /** Builds immutable {@link AliasManagerConfig} instances. */
  final class Builder {

    private AliasVersionStore versionStore = new InMemoryAliasVersionStore();
    private AliasAuthorizationPolicy authorizationPolicy =
        AliasAuthorizationPolicy.ALLOW_FIND_DENY_MUTATE;
    private AliasLimits limits = AliasLimits.defaults();
    private Comparator<AliasTarget> targetOrdering = AliasTarget.DEFAULT_ORDERING;
    private UShort nodeNamespaceIndex = UShort.valueOf(1);
    private boolean findAliasVerboseEnabled = false;
    private boolean configurationEnabled = false;

    private Builder() {}

    /**
     * Set the store used to persist category {@code LastChange} versions.
     *
     * <p>Default: {@link InMemoryAliasVersionStore} (test/demo use only).
     *
     * @param versionStore the {@link AliasVersionStore} to use.
     * @return this {@link Builder}.
     */
    public Builder versionStore(AliasVersionStore versionStore) {
      this.versionStore = Objects.requireNonNull(versionStore, "versionStore must be non-null");
      return this;
    }

    /**
     * Set the policy deciding whether sessions may search or mutate categories over the network.
     *
     * <p>Default: {@link AliasAuthorizationPolicy#ALLOW_FIND_DENY_MUTATE}.
     *
     * @param authorizationPolicy the {@link AliasAuthorizationPolicy} to use.
     * @return this {@link Builder}.
     */
    public Builder authorizationPolicy(AliasAuthorizationPolicy authorizationPolicy) {
      this.authorizationPolicy =
          Objects.requireNonNull(authorizationPolicy, "authorizationPolicy must be non-null");
      return this;
    }

    /**
     * Set the limits applied to alias lookup and mutation calls.
     *
     * <p>Default: {@link AliasLimits#defaults()}.
     *
     * @param limits the {@link AliasLimits} to use.
     * @return this {@link Builder}.
     */
    public Builder limits(AliasLimits limits) {
      this.limits = Objects.requireNonNull(limits, "limits must be non-null");
      return this;
    }

    /**
     * Set the ordering applied to the targets within each result entry.
     *
     * <p>Default: local targets before remote, then by the target NodeId's parseable string form.
     *
     * @param targetOrdering the target {@link Comparator} to use.
     * @return this {@link Builder}.
     */
    public Builder targetOrdering(Comparator<AliasTarget> targetOrdering) {
      this.targetOrdering =
          Objects.requireNonNull(targetOrdering, "targetOrdering must be non-null");
      return this;
    }

    /**
     * Set the namespace index used to allocate NodeIds for the Nodes the manager itself creates:
     * materialized Method Nodes and default-factory alias Nodes.
     *
     * <p>Default: namespace index 1.
     *
     * @param nodeNamespaceIndex the namespace index to use.
     * @return this {@link Builder}.
     */
    public Builder nodeNamespaceIndex(UShort nodeNamespaceIndex) {
      this.nodeNamespaceIndex =
          Objects.requireNonNull(nodeNamespaceIndex, "nodeNamespaceIndex must be non-null");
      return this;
    }

    /**
     * Set whether {@code FindAliasVerbose} is enabled on the standard categories.
     *
     * <p>Default: {@code false}.
     *
     * @param findAliasVerboseEnabled {@code true} to enable {@code FindAliasVerbose}.
     * @return this {@link Builder}.
     */
    public Builder findAliasVerboseEnabled(boolean findAliasVerboseEnabled) {
      this.findAliasVerboseEnabled = findAliasVerboseEnabled;
      return this;
    }

    /**
     * Set whether the {@code AddAliasesToCategory} and {@code DeleteAliasesFromCategory} Methods
     * are materialized and bound on the standard categories.
     *
     * <p>Enabling the Methods does not by itself allow network mutation: the default {@link
     * AliasAuthorizationPolicy} denies every session, so an explicit policy grant is also required.
     *
     * <p>Default: {@code false}.
     *
     * @param configurationEnabled {@code true} to enable {@code AddAliasesToCategory} and {@code
     *     DeleteAliasesFromCategory}.
     * @return this {@link Builder}.
     */
    public Builder configurationEnabled(boolean configurationEnabled) {
      this.configurationEnabled = configurationEnabled;
      return this;
    }

    /**
     * Build an immutable {@link AliasManagerConfig} from the current settings.
     *
     * @return a new, immutable {@link AliasManagerConfig}.
     */
    public AliasManagerConfig build() {
      return new ConfigImpl(
          versionStore,
          authorizationPolicy,
          limits,
          targetOrdering,
          nodeNamespaceIndex,
          findAliasVerboseEnabled,
          configurationEnabled);
    }

    /** The immutable {@link AliasManagerConfig} produced by {@link Builder#build()}. */
    private record ConfigImpl(
        AliasVersionStore versionStore,
        AliasAuthorizationPolicy authorizationPolicy,
        AliasLimits limits,
        Comparator<AliasTarget> targetOrdering,
        UShort nodeNamespaceIndex,
        boolean findAliasVerboseEnabled,
        boolean configurationEnabled)
        implements AliasManagerConfig {

      @Override
      public AliasVersionStore getVersionStore() {
        return versionStore;
      }

      @Override
      public AliasAuthorizationPolicy getAuthorizationPolicy() {
        return authorizationPolicy;
      }

      @Override
      public AliasLimits getLimits() {
        return limits;
      }

      @Override
      public Comparator<AliasTarget> getTargetOrdering() {
        return targetOrdering;
      }

      @Override
      public UShort getNodeNamespaceIndex() {
        return nodeNamespaceIndex;
      }

      @Override
      public boolean isFindAliasVerboseEnabled() {
        return findAliasVerboseEnabled;
      }

      @Override
      public boolean isConfigurationEnabled() {
        return configurationEnabled;
      }
    }
  }
}

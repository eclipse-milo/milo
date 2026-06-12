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

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetRef;
import org.eclipse.milo.opcua.sdk.pubsub.config.SecurityGroupRef;
import org.eclipse.milo.opcua.sdk.pubsub.security.SecurityKeyProvider;
import org.jspecify.annotations.Nullable;

/**
 * Data plumbing for one {@code PubSubConfig} graph, supplied at {@link PubSubService} creation:
 * publisher-side data sources, subscriber-side listeners, and security key providers, each bound by
 * reference to a named element of the config.
 *
 * <p>Bindings are specific to the names in one config graph, while {@link PubSubServiceConfig}
 * describes the runtime environment and is reusable across config graphs.
 */
public final class PubSubBindings {

  private final Map<PublishedDataSetRef, PublishedDataSetSource> sources;
  private final Map<DataSetReaderRef, List<DataSetListener>> listeners;
  private final Map<SecurityGroupRef, SecurityKeyProvider> securityKeyProviders;

  private PubSubBindings(Builder builder) {
    this.sources = Collections.unmodifiableMap(new LinkedHashMap<>(builder.sources));

    var listeners = new LinkedHashMap<DataSetReaderRef, List<DataSetListener>>();
    builder.listeners.forEach((ref, list) -> listeners.put(ref, List.copyOf(list)));
    this.listeners = Collections.unmodifiableMap(listeners);

    this.securityKeyProviders =
        Collections.unmodifiableMap(new LinkedHashMap<>(builder.securityKeyProviders));
  }

  /**
   * Get the {@link PublishedDataSetSource}s bound to PublishedDataSets by reference.
   *
   * @return the bound sources; possibly empty.
   */
  public Map<PublishedDataSetRef, PublishedDataSetSource> getSources() {
    return sources;
  }

  /**
   * Get the {@link DataSetListener}s bound to DataSetReaders by reference.
   *
   * @return the bound listeners; possibly empty.
   */
  public Map<DataSetReaderRef, List<DataSetListener>> getListeners() {
    return listeners;
  }

  /**
   * Get the {@link SecurityKeyProvider}s bound to SecurityGroups by reference.
   *
   * @return the bound key providers; possibly empty.
   */
  public Map<SecurityGroupRef, SecurityKeyProvider> getSecurityKeyProviders() {
    return securityKeyProviders;
  }

  /**
   * Create a new {@link Builder} initialized with the values of these bindings.
   *
   * @return a new {@link Builder} initialized with the values of these bindings.
   */
  public Builder toBuilder() {
    Builder builder = new Builder();
    builder.sources.putAll(sources);
    listeners.forEach((ref, list) -> builder.listeners.put(ref, new ArrayList<>(list)));
    builder.securityKeyProviders.putAll(securityKeyProviders);
    return builder;
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof PubSubBindings that)) {
      return false;
    }
    return sources.equals(that.sources)
        && listeners.equals(that.listeners)
        && securityKeyProviders.equals(that.securityKeyProviders);
  }

  @Override
  public int hashCode() {
    int result = sources.hashCode();
    result = 31 * result + listeners.hashCode();
    result = 31 * result + securityKeyProviders.hashCode();
    return result;
  }

  /**
   * Create a new {@link Builder}.
   *
   * @return a new {@link Builder}.
   */
  public static Builder builder() {
    return new Builder();
  }

  /** A builder of {@link PubSubBindings} instances. */
  public static final class Builder {

    private final Map<PublishedDataSetRef, PublishedDataSetSource> sources = new LinkedHashMap<>();
    private final Map<DataSetReaderRef, List<DataSetListener>> listeners = new LinkedHashMap<>();
    private final Map<SecurityGroupRef, SecurityKeyProvider> securityKeyProviders =
        new LinkedHashMap<>();

    private Builder() {}

    /**
     * Bind a {@link PublishedDataSetSource} to the PublishedDataSet referenced by {@code ref},
     * replacing any source previously bound to the same reference.
     *
     * @param ref the reference to the PublishedDataSet to bind.
     * @param source the source pulled for a snapshot each publish cycle.
     * @return this {@link Builder}.
     */
    public Builder source(PublishedDataSetRef ref, PublishedDataSetSource source) {
      this.sources.put(ref, source);
      return this;
    }

    /**
     * Add a {@link DataSetListener} notified of every DataSet received by the DataSetReader
     * referenced by {@code ref}. May be called multiple times to add multiple listeners.
     *
     * @param ref the reference to the DataSetReader to listen to.
     * @param listener the listener to add.
     * @return this {@link Builder}.
     */
    public Builder listener(DataSetReaderRef ref, DataSetListener listener) {
      this.listeners.computeIfAbsent(ref, k -> new ArrayList<>()).add(listener);
      return this;
    }

    /**
     * Bind a {@link SecurityKeyProvider} to the SecurityGroup referenced by {@code ref}, replacing
     * any provider previously bound to the same reference.
     *
     * @param ref the reference to the SecurityGroup to bind.
     * @param provider the provider of key material for the SecurityGroup.
     * @return this {@link Builder}.
     */
    public Builder securityKeys(SecurityGroupRef ref, SecurityKeyProvider provider) {
      this.securityKeyProviders.put(ref, provider);
      return this;
    }

    /**
     * Build a {@link PubSubBindings} from the values configured on this {@link Builder}.
     *
     * @return a new {@link PubSubBindings}.
     */
    public PubSubBindings build() {
      return new PubSubBindings(this);
    }
  }
}

/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.config;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.jspecify.annotations.Nullable;

/**
 * A {@link PubSubConnectionConfig} that communicates via an MQTT broker, using the UADP or JSON
 * message mapping.
 *
 * <p>Create instances via {@link PubSubConnectionConfig#mqtt(String)} or {@link #builder(String)}.
 * The MQTT config type lives in this module so the sealed connection hierarchy is complete; the
 * MQTT transport implementation is provided by a separate module and registered as a transport
 * provider.
 */
public final class MqttConnectionConfig implements PubSubConnectionConfig {

  private final String name;
  private final boolean enabled;
  private final @Nullable PublisherId publisherId;
  private final URI brokerUri;
  private final @Nullable BrokerSecurityConfig brokerSecurity;
  private final List<WriterGroupConfig> writerGroups;
  private final List<ReaderGroupConfig> readerGroups;
  private final Map<QualifiedName, Variant> properties;
  private final @Nullable ExtensionObject rawTransportSettings;

  private MqttConnectionConfig(Builder builder, URI brokerUri) {
    this.name = builder.name;
    this.enabled = builder.enabled;
    this.publisherId = builder.publisherId;
    this.brokerUri = brokerUri;
    this.brokerSecurity = builder.brokerSecurity;
    this.writerGroups = List.copyOf(builder.writerGroups);
    this.readerGroups = List.copyOf(builder.readerGroups);
    this.properties = Collections.unmodifiableMap(new LinkedHashMap<>(builder.properties));
    this.rawTransportSettings = builder.rawTransportSettings;
  }

  @Override
  public String name() {
    return name;
  }

  @Override
  public boolean enabled() {
    return enabled;
  }

  @Override
  public @Nullable PublisherId publisherId() {
    return publisherId;
  }

  /**
   * Get the URI of the MQTT broker this connection communicates with.
   *
   * @return the broker URI.
   */
  public URI getBrokerUri() {
    return brokerUri;
  }

  /**
   * Get the broker session security settings.
   *
   * @return the {@link BrokerSecurityConfig}, or {@code null} if not configured.
   */
  public @Nullable BrokerSecurityConfig getBrokerSecurity() {
    return brokerSecurity;
  }

  @Override
  public List<WriterGroupConfig> writerGroups() {
    return writerGroups;
  }

  @Override
  public List<ReaderGroupConfig> readerGroups() {
    return readerGroups;
  }

  @Override
  public Map<QualifiedName, Variant> properties() {
    return properties;
  }

  @Override
  public @Nullable ExtensionObject rawTransportSettings() {
    return rawTransportSettings;
  }

  /**
   * Create a new {@link Builder} initialized with the values of this instance.
   *
   * @return a new {@link Builder}.
   */
  public Builder toBuilder() {
    Builder builder = new Builder(name);
    builder.enabled = enabled;
    builder.publisherId = publisherId;
    builder.brokerUri = brokerUri;
    builder.brokerSecurity = brokerSecurity;
    builder.writerGroups.addAll(writerGroups);
    builder.readerGroups.addAll(readerGroups);
    builder.properties.putAll(properties);
    builder.rawTransportSettings = rawTransportSettings;
    return builder;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof MqttConnectionConfig that)) {
      return false;
    }
    return name.equals(that.name)
        && enabled == that.enabled
        && Objects.equals(publisherId, that.publisherId)
        && brokerUri.equals(that.brokerUri)
        && Objects.equals(brokerSecurity, that.brokerSecurity)
        && writerGroups.equals(that.writerGroups)
        && readerGroups.equals(that.readerGroups)
        && properties.equals(that.properties)
        && Objects.equals(rawTransportSettings, that.rawTransportSettings);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        name,
        enabled,
        publisherId,
        brokerUri,
        brokerSecurity,
        writerGroups,
        readerGroups,
        properties,
        rawTransportSettings);
  }

  /**
   * Create a new {@link Builder}.
   *
   * @param name the connection name.
   * @return a new {@link Builder}.
   */
  public static Builder builder(String name) {
    return new Builder(name);
  }

  /** A builder of {@link MqttConnectionConfig} instances. */
  public static final class Builder {

    private final String name;
    private boolean enabled = true;
    private @Nullable PublisherId publisherId;
    private @Nullable URI brokerUri;
    private @Nullable BrokerSecurityConfig brokerSecurity;
    private final List<WriterGroupConfig> writerGroups = new ArrayList<>();
    private final List<ReaderGroupConfig> readerGroups = new ArrayList<>();
    private final Map<QualifiedName, Variant> properties = new LinkedHashMap<>();
    private @Nullable ExtensionObject rawTransportSettings;

    private Builder(String name) {
      this.name = name;
    }

    /**
     * Set whether this connection is enabled.
     *
     * @param enabled {@code true} to enable this connection; defaults to {@code true}.
     * @return this {@link Builder}.
     */
    public Builder enabled(boolean enabled) {
      this.enabled = enabled;
      return this;
    }

    /**
     * Set the PublisherId used by this connection's writer groups.
     *
     * @param publisherId the {@link PublisherId}. May be omitted for subscriber-only connections.
     * @return this {@link Builder}.
     */
    public Builder publisherId(PublisherId publisherId) {
      this.publisherId = publisherId;
      return this;
    }

    /**
     * Set the URI of the MQTT broker this connection communicates with.
     *
     * @param brokerUri the broker URI, e.g. {@code mqtt://broker.example:1883}.
     * @return this {@link Builder}.
     */
    public Builder brokerUri(URI brokerUri) {
      this.brokerUri = brokerUri;
      return this;
    }

    /**
     * Set the broker session security settings.
     *
     * @param security the {@link BrokerSecurityConfig}.
     * @return this {@link Builder}.
     */
    public Builder brokerSecurity(BrokerSecurityConfig security) {
      this.brokerSecurity = security;
      return this;
    }

    /**
     * Add a writer group to this connection.
     *
     * @param writerGroup the {@link WriterGroupConfig} to add.
     * @return this {@link Builder}.
     */
    public Builder writerGroup(WriterGroupConfig writerGroup) {
      writerGroups.add(writerGroup);
      return this;
    }

    /**
     * Add a reader group to this connection.
     *
     * @param readerGroup the {@link ReaderGroupConfig} to add.
     * @return this {@link Builder}.
     */
    public Builder readerGroup(ReaderGroupConfig readerGroup) {
      readerGroups.add(readerGroup);
      return this;
    }

    /**
     * Add a connection property, mapped to and from the Part 14 KeyValuePair array.
     *
     * @param name the property name.
     * @param value the property value.
     * @return this {@link Builder}.
     */
    public Builder property(QualifiedName name, Variant value) {
      properties.put(name, value);
      return this;
    }

    /**
     * Set the raw transport settings escape hatch, overriding the typed transport settings when
     * mapping to the Part 14 configuration model.
     *
     * @param value the raw transport settings {@link ExtensionObject}.
     * @return this {@link Builder}.
     */
    public Builder rawTransportSettings(ExtensionObject value) {
      this.rawTransportSettings = value;
      return this;
    }

    /**
     * Build a new {@link MqttConnectionConfig} from the values configured on this builder.
     *
     * @return a new {@link MqttConnectionConfig}.
     * @throws PubSubConfigValidationException if the name is blank or no broker URI is configured.
     */
    public MqttConnectionConfig build() {
      if (name.isBlank()) {
        throw new PubSubConfigValidationException("connection: name must not be blank");
      }
      if (brokerUri == null) {
        throw new PubSubConfigValidationException(
            "connection '" + name + "': brokerUri is required");
      }

      return new MqttConnectionConfig(this, brokerUri);
    }
  }
}

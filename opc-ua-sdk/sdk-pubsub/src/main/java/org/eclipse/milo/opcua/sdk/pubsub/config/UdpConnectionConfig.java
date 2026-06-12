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
 * A {@link PubSubConnectionConfig} that communicates over UDP datagrams, multicast or unicast,
 * using the UADP message mapping.
 *
 * <p>Create instances via {@link PubSubConnectionConfig#udp(String)} or {@link #builder(String)}.
 */
public final class UdpConnectionConfig implements PubSubConnectionConfig {

  private final String name;
  private final boolean enabled;
  private final @Nullable PublisherId publisherId;
  private final UdpDatagramAddress address;
  private final @Nullable UdpDatagramAddress discoveryAddress;
  private final List<WriterGroupConfig> writerGroups;
  private final List<ReaderGroupConfig> readerGroups;
  private final Map<QualifiedName, Variant> properties;
  private final @Nullable ExtensionObject rawTransportSettings;

  private UdpConnectionConfig(Builder builder, UdpDatagramAddress address) {
    this.name = builder.name;
    this.enabled = builder.enabled;
    this.publisherId = builder.publisherId;
    this.address = address;
    this.discoveryAddress = builder.discoveryAddress;
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
   * Get the datagram address this connection sends to and receives from.
   *
   * @return the {@link UdpDatagramAddress}.
   */
  public UdpDatagramAddress getAddress() {
    return address;
  }

  /**
   * Get the datagram address this connection's discovery probe and announcement messages are sent
   * to and received on, if configured.
   *
   * <p>The default is not materialized in the config: when this returns {@code null} the runtime
   * applies the Part 14 default discovery address, {@code opc.udp://224.0.2.14:4840}, on this
   * connection's network interface.
   *
   * <p>Discovery channels are opened whenever the connection has writer groups or any reader with
   * the {@link MetadataPolicy#REQUEST_IF_MISSING} policy, and always by the built-in UDP transport
   * provider — independent of any custom {@code TransportProvider} serving this connection's data
   * plane. With the default address that means binding the well-known port 4840 and joining the
   * {@code 224.0.2.14} multicast group; configure this address to scope discovery traffic, e.g. to
   * a loopback or organization-local group.
   *
   * @return the discovery {@link UdpDatagramAddress}, or {@code null} if not configured.
   */
  public @Nullable UdpDatagramAddress getDiscoveryAddress() {
    return discoveryAddress;
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
    builder.address = address;
    builder.discoveryAddress = discoveryAddress;
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
    if (!(o instanceof UdpConnectionConfig that)) {
      return false;
    }
    return name.equals(that.name)
        && enabled == that.enabled
        && Objects.equals(publisherId, that.publisherId)
        && address.equals(that.address)
        && Objects.equals(discoveryAddress, that.discoveryAddress)
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
        address,
        discoveryAddress,
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

  /** A builder of {@link UdpConnectionConfig} instances. */
  public static final class Builder {

    private final String name;
    private boolean enabled = true;
    private @Nullable PublisherId publisherId;
    private @Nullable UdpDatagramAddress address;
    private @Nullable UdpDatagramAddress discoveryAddress;
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
     * Set the datagram address this connection sends to and receives from.
     *
     * @param address the {@link UdpDatagramAddress}.
     * @return this {@link Builder}.
     */
    public Builder address(UdpDatagramAddress address) {
      this.address = address;
      return this;
    }

    /**
     * Set the datagram address this connection's discovery probe and announcement messages are sent
     * to and received on.
     *
     * <p>Optional. When unset, no default is materialized in the built config; the runtime applies
     * the Part 14 default discovery address, {@code opc.udp://224.0.2.14:4840}, on this
     * connection's network interface.
     *
     * <p>See {@link UdpConnectionConfig#getDiscoveryAddress()} for when and how the runtime opens
     * discovery channels.
     *
     * @param discoveryAddress the discovery {@link UdpDatagramAddress}.
     * @return this {@link Builder}.
     */
    public Builder discoveryAddress(UdpDatagramAddress discoveryAddress) {
      this.discoveryAddress = discoveryAddress;
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
     * Build a new {@link UdpConnectionConfig} from the values configured on this builder.
     *
     * @return a new {@link UdpConnectionConfig}.
     * @throws PubSubConfigValidationException if the name is blank, no address is configured, or
     *     any group, writer, or reader uses JSON message settings, which are not valid on a UDP
     *     connection.
     */
    public UdpConnectionConfig build() {
      if (name.isBlank()) {
        throw new PubSubConfigValidationException("connection: name must not be blank");
      }
      if (address == null) {
        throw new PubSubConfigValidationException("connection '" + name + "': address is required");
      }

      for (WriterGroupConfig group : writerGroups) {
        if (group.getMessageSettings() instanceof JsonWriterGroupSettings) {
          throw new PubSubConfigValidationException(
              "connection '"
                  + name
                  + "': writer group '"
                  + group.getName()
                  + "' uses JSON message settings, which are not valid on a UDP connection");
        }
        for (DataSetWriterConfig writer : group.getDataSetWriters()) {
          if (writer.getSettings() instanceof JsonDataSetWriterSettings) {
            throw new PubSubConfigValidationException(
                "connection '"
                    + name
                    + "': dataset writer '"
                    + writer.getName()
                    + "' uses JSON message settings, which are not valid on a UDP connection");
          }
        }
      }
      for (ReaderGroupConfig group : readerGroups) {
        for (DataSetReaderConfig reader : group.getDataSetReaders()) {
          if (reader.getSettings() instanceof JsonDataSetReaderSettings) {
            throw new PubSubConfigValidationException(
                "connection '"
                    + name
                    + "': dataset reader '"
                    + reader.getName()
                    + "' uses JSON message settings, which are not valid on a UDP connection");
          }
        }
      }

      return new UdpConnectionConfig(this, address);
    }
  }
}

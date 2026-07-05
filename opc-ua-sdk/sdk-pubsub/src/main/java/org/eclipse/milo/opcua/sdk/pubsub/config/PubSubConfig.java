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
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfiguration2DataType;
import org.jspecify.annotations.Nullable;

/**
 * The immutable root of an authored PubSub configuration: connections (with their writer and reader
 * groups), published datasets, standalone subscribed datasets, security groups, and additional
 * properties.
 *
 * <p>Instances are created with {@link #builder()}; {@code Builder#build()} performs cross-element
 * validation (name and wire-id uniqueness, reference resolution) so that a successfully built
 * config is internally consistent. Reconfiguration is by replacement: derive a modified copy via
 * {@link #toBuilder()} and hand it to the runtime.
 *
 * <p>{@link #toDataType(NamespaceTable)} and {@link #fromDataType(PubSubConfiguration2DataType,
 * NamespaceTable)} are the round-trip boundary to the Part 14 v1.05 {@code
 * PubSubConfiguration2DataType}.
 */
public final class PubSubConfig {

  private final boolean enabled;
  private final List<PubSubConnectionConfig> connections;
  private final List<PublishedDataSetConfig> publishedDataSets;
  private final List<StandaloneSubscribedDataSetConfig> standaloneSubscribedDataSets;
  private final List<SecurityGroupConfig> securityGroups;
  private final List<EndpointDescription> defaultSecurityKeyServices;
  private final Map<QualifiedName, Variant> properties;

  private PubSubConfig(Builder builder) {
    this.enabled = builder.enabled;
    this.connections = List.copyOf(builder.connections);
    this.publishedDataSets = List.copyOf(builder.publishedDataSets);
    this.standaloneSubscribedDataSets = List.copyOf(builder.standaloneSubscribedDataSets);
    this.securityGroups = List.copyOf(builder.securityGroups);
    this.defaultSecurityKeyServices = List.copyOf(builder.defaultSecurityKeyServices);
    this.properties = Collections.unmodifiableMap(new LinkedHashMap<>(builder.properties));
  }

  /**
   * Convert this config to its Part 14 {@link PubSubConfiguration2DataType} representation.
   *
   * @param namespaceTable the {@link NamespaceTable} used to convert namespace-URI {@link
   *     NodeFieldAddress}es to local NodeIds.
   * @return the {@link PubSubConfiguration2DataType} representation of this config.
   * @throws PubSubConfigValidationException if a namespace URI used by a field address cannot be
   *     resolved against {@code namespaceTable}.
   */
  public PubSubConfiguration2DataType toDataType(NamespaceTable namespaceTable) {
    return PubSubConfigMapper.toDataType(this, namespaceTable);
  }

  /**
   * Create a {@link PubSubConfig} from its Part 14 {@link PubSubConfiguration2DataType}
   * representation.
   *
   * @param value the {@link PubSubConfiguration2DataType} to convert.
   * @param namespaceTable the {@link NamespaceTable} used to convert local NodeIds to namespace-URI
   *     {@link NodeFieldAddress}es.
   * @return the {@link PubSubConfig} representation of {@code value}.
   * @throws PubSubConfigValidationException if {@code value} does not map to a valid config.
   */
  public static PubSubConfig fromDataType(
      PubSubConfiguration2DataType value, NamespaceTable namespaceTable) {

    return PubSubConfigMapper.fromDataType(value, namespaceTable);
  }

  /**
   * Get whether PubSub functionality as a whole is enabled.
   *
   * @return {@code true} if PubSub functionality is enabled.
   */
  public boolean isEnabled() {
    return enabled;
  }

  /**
   * Get the connections in this config.
   *
   * @return the connections in this config.
   */
  public List<PubSubConnectionConfig> connections() {
    return connections;
  }

  /**
   * Get the published datasets in this config.
   *
   * @return the published datasets in this config.
   */
  public List<PublishedDataSetConfig> publishedDataSets() {
    return publishedDataSets;
  }

  /**
   * Get the standalone subscribed datasets in this config.
   *
   * @return the standalone subscribed datasets in this config.
   */
  public List<StandaloneSubscribedDataSetConfig> standaloneSubscribedDataSets() {
    return standaloneSubscribedDataSets;
  }

  /**
   * Get the security groups in this config.
   *
   * @return the security groups in this config.
   */
  public List<SecurityGroupConfig> securityGroups() {
    return securityGroups;
  }

  /**
   * Get the default Security Key Service endpoints of this config, used for groups and dataset
   * readers that configure none of their own.
   *
   * @return the default key service endpoints; possibly empty.
   */
  public List<EndpointDescription> defaultSecurityKeyServices() {
    return defaultSecurityKeyServices;
  }

  /**
   * Get the additional properties of this config, in insertion order.
   *
   * @return an unmodifiable view of the additional properties.
   */
  public Map<QualifiedName, Variant> properties() {
    return properties;
  }

  /**
   * Look up a connection by name.
   *
   * @param name the name of the connection.
   * @return the connection with the given name, if present.
   */
  public Optional<PubSubConnectionConfig> connection(String name) {
    return connections.stream().filter(c -> c.name().equals(name)).findFirst();
  }

  /**
   * Look up a published dataset by name.
   *
   * @param name the name of the published dataset.
   * @return the published dataset with the given name, if present.
   */
  public Optional<PublishedDataSetConfig> publishedDataSet(String name) {
    return publishedDataSets.stream().filter(ds -> ds.getName().equals(name)).findFirst();
  }

  /**
   * Create a {@link Builder} pre-populated with the elements of this config.
   *
   * @return a new {@link Builder}.
   */
  public Builder toBuilder() {
    Builder builder = new Builder();
    builder.enabled = enabled;
    builder.connections.addAll(connections);
    builder.publishedDataSets.addAll(publishedDataSets);
    builder.standaloneSubscribedDataSets.addAll(standaloneSubscribedDataSets);
    builder.securityGroups.addAll(securityGroups);
    builder.defaultSecurityKeyServices.addAll(defaultSecurityKeyServices);
    builder.properties.putAll(properties);
    return builder;
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof PubSubConfig that)) {
      return false;
    }
    return enabled == that.enabled
        && connections.equals(that.connections)
        && publishedDataSets.equals(that.publishedDataSets)
        && standaloneSubscribedDataSets.equals(that.standaloneSubscribedDataSets)
        && securityGroups.equals(that.securityGroups)
        && defaultSecurityKeyServices.equals(that.defaultSecurityKeyServices)
        && properties.equals(that.properties);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        enabled,
        connections,
        publishedDataSets,
        standaloneSubscribedDataSets,
        securityGroups,
        defaultSecurityKeyServices,
        properties);
  }

  @Override
  public String toString() {
    return "PubSubConfig{enabled=%s, connections=%s, publishedDataSets=%s, standaloneSubscribedDataSets=%s, securityGroups=%s, defaultSecurityKeyServices=%s, properties=%s}"
        .formatted(
            enabled,
            connections,
            publishedDataSets,
            standaloneSubscribedDataSets,
            securityGroups,
            defaultSecurityKeyServices,
            properties);
  }

  /**
   * Create a new {@link Builder}.
   *
   * @return a new {@link Builder}.
   */
  public static Builder builder() {
    return new Builder();
  }

  /** A builder of {@link PubSubConfig} instances. */
  public static final class Builder {

    private boolean enabled = true;
    private final List<PubSubConnectionConfig> connections = new ArrayList<>();
    private final List<PublishedDataSetConfig> publishedDataSets = new ArrayList<>();
    private final List<StandaloneSubscribedDataSetConfig> standaloneSubscribedDataSets =
        new ArrayList<>();
    private final List<SecurityGroupConfig> securityGroups = new ArrayList<>();
    private final List<EndpointDescription> defaultSecurityKeyServices = new ArrayList<>();
    private final Map<QualifiedName, Variant> properties = new LinkedHashMap<>();

    private Builder() {}

    /**
     * Set whether PubSub functionality as a whole is enabled.
     *
     * @param enabled {@code true} if PubSub functionality is enabled.
     * @return this {@link Builder}.
     */
    public Builder enabled(boolean enabled) {
      this.enabled = enabled;
      return this;
    }

    /**
     * Add a connection.
     *
     * @param connection the connection to add.
     * @return this {@link Builder}.
     */
    public Builder connection(PubSubConnectionConfig connection) {
      connections.add(connection);
      return this;
    }

    /**
     * Add a published dataset.
     *
     * @param dataSet the published dataset to add.
     * @return this {@link Builder}.
     */
    public Builder publishedDataSet(PublishedDataSetConfig dataSet) {
      publishedDataSets.add(dataSet);
      return this;
    }

    /**
     * Add a standalone subscribed dataset.
     *
     * @param dataSet the standalone subscribed dataset to add.
     * @return this {@link Builder}.
     */
    public Builder standaloneSubscribedDataSet(StandaloneSubscribedDataSetConfig dataSet) {
      standaloneSubscribedDataSets.add(dataSet);
      return this;
    }

    /**
     * Add a security group.
     *
     * @param securityGroup the security group to add.
     * @return this {@link Builder}.
     */
    public Builder securityGroup(SecurityGroupConfig securityGroup) {
      securityGroups.add(securityGroup);
      return this;
    }

    /**
     * Add a default Security Key Service endpoint, used for groups and dataset readers that
     * configure none of their own.
     *
     * @param endpoint the key service endpoint to add.
     * @return this {@link Builder}.
     */
    public Builder defaultSecurityKeyService(EndpointDescription endpoint) {
      defaultSecurityKeyServices.add(endpoint);
      return this;
    }

    /**
     * Add an additional property.
     *
     * @param name the name of the property.
     * @param value the value of the property.
     * @return this {@link Builder}.
     */
    public Builder property(QualifiedName name, Variant value) {
      properties.put(name, value);
      return this;
    }

    /**
     * Build a {@link PubSubConfig} from the configured elements.
     *
     * <p>Validates:
     *
     * <ul>
     *   <li>name uniqueness per scope, including writer group names across connections sharing the
     *       same publisher id
     *   <li>non-zero WriterGroupId/DataSetWriterId, unique within the same publisher id scope
     *   <li>resolvable {@link PublishedDataSetRef} / {@link SecurityGroupRef} / {@link
     *       StandaloneSubscribedDataSetRef} links
     *   <li>a writer group with publishing interval zero (the event-triggered publishing mode)
     *       contains only writers referencing event-source datasets
     *   <li>a publisher id is configured on every connection that has writer groups
     *   <li>no JSON message settings on a UDP connection
     * </ul>
     *
     * @return a new {@link PubSubConfig}.
     * @throws PubSubConfigValidationException on violation, with a message naming the offending
     *     element.
     */
    public PubSubConfig build() {
      validate();
      return new PubSubConfig(this);
    }

    private void validate() {
      checkUniqueNames(
          connections.stream().map(PubSubConnectionConfig::name).toList(), "connection");
      checkUniqueNames(
          publishedDataSets.stream().map(PublishedDataSetConfig::getName).toList(),
          "publishedDataSet");
      checkUniqueNames(
          standaloneSubscribedDataSets.stream()
              .map(StandaloneSubscribedDataSetConfig::getName)
              .toList(),
          "standaloneSubscribedDataSet");
      checkUniqueNames(
          securityGroups.stream().map(SecurityGroupConfig::getName).toList(), "securityGroup");

      Map<String, PublishedDataSetConfig> publishedDataSetsByName =
          publishedDataSets.stream()
              .collect(Collectors.toMap(PublishedDataSetConfig::getName, dataSet -> dataSet));
      Set<String> standaloneNames =
          standaloneSubscribedDataSets.stream()
              .map(StandaloneSubscribedDataSetConfig::getName)
              .collect(Collectors.toSet());
      Set<String> securityGroupNames =
          securityGroups.stream().map(SecurityGroupConfig::getName).collect(Collectors.toSet());

      for (StandaloneSubscribedDataSetConfig dataSet : standaloneSubscribedDataSets) {
        if (dataSet.getSubscribedDataSet() instanceof StandaloneSubscribedDataSetRef ref
            && !standaloneNames.contains(ref.name())) {
          throw new PubSubConfigValidationException(
              "standaloneSubscribedDataSet '%s': unresolved StandaloneSubscribedDataSetRef '%s'"
                  .formatted(dataSet.getName(), ref.name()));
        }
      }

      for (PubSubConnectionConfig connection : connections) {
        validateConnection(
            connection, publishedDataSetsByName, standaloneNames, securityGroupNames);
      }

      validatePublisherIdScopes();
    }

    private static void checkUniqueNames(List<String> names, String elementType) {
      Set<String> seen = new HashSet<>();
      for (String name : names) {
        if (!seen.add(name)) {
          throw new PubSubConfigValidationException(
              "duplicate %s name '%s'".formatted(elementType, name));
        }
      }
    }

    private static void validateConnection(
        PubSubConnectionConfig connection,
        Map<String, PublishedDataSetConfig> publishedDataSetsByName,
        Set<String> standaloneNames,
        Set<String> securityGroupNames) {

      String connectionName = connection.name();

      if (!connection.writerGroups().isEmpty() && connection.publisherId() == null) {
        throw new PubSubConfigValidationException(
            "connection '%s': publisherId is required when writer groups are configured"
                .formatted(connectionName));
      }

      boolean udp = connection instanceof UdpConnectionConfig;

      // Writer group and reader group names share a single scope within the connection.
      Set<String> groupNames = new HashSet<>();

      for (WriterGroupConfig writerGroup : connection.writerGroups()) {
        String path =
            "connection '%s' writerGroup '%s'".formatted(connectionName, writerGroup.getName());

        if (!groupNames.add(writerGroup.getName())) {
          throw new PubSubConfigValidationException(
              path + ": duplicate group name within connection");
        }
        if (writerGroup.getWriterGroupId().intValue() == 0) {
          throw new PubSubConfigValidationException(path + ": writerGroupId must be non-zero");
        }
        if (udp && writerGroup.getMessageSettings() instanceof JsonWriterGroupSettings) {
          throw new PubSubConfigValidationException(
              path + ": JSON message settings are not allowed on a UDP connection");
        }
        checkSecurityGroupRef(writerGroup.getMessageSecurity(), securityGroupNames, path);

        Set<String> writerNames = new HashSet<>();
        for (DataSetWriterConfig writer : writerGroup.getDataSetWriters()) {
          String writerPath = path + " dataSetWriter '%s'".formatted(writer.getName());

          if (!writerNames.add(writer.getName())) {
            throw new PubSubConfigValidationException(
                writerPath + ": duplicate dataSetWriter name within writer group");
          }
          if (writer.getDataSetWriterId().intValue() == 0) {
            throw new PubSubConfigValidationException(
                writerPath + ": dataSetWriterId must be non-zero");
          }
          if (udp && writer.getSettings() instanceof JsonDataSetWriterSettings) {
            throw new PubSubConfigValidationException(
                writerPath + ": JSON message settings are not allowed on a UDP connection");
          }
          PublishedDataSetConfig dataSet = publishedDataSetsByName.get(writer.getDataSet().name());
          if (dataSet == null) {
            throw new PubSubConfigValidationException(
                writerPath
                    + ": unresolved PublishedDataSetRef '%s'"
                        .formatted(writer.getDataSet().name()));
          }
          if (writerGroup.getPublishingInterval().isZero()
              && !(dataSet.getSource() instanceof PublishedEventsConfig)) {
            throw new PubSubConfigValidationException(
                path
                    + ": publishingInterval 0 requires all writers to reference event-source datasets, but dataSetWriter '%s' references data-items dataset '%s'"
                        .formatted(writer.getName(), dataSet.getName()));
          }
        }
      }

      for (ReaderGroupConfig readerGroup : connection.readerGroups()) {
        String path =
            "connection '%s' readerGroup '%s'".formatted(connectionName, readerGroup.getName());

        if (!groupNames.add(readerGroup.getName())) {
          throw new PubSubConfigValidationException(
              path + ": duplicate group name within connection");
        }
        checkSecurityGroupRef(readerGroup.getMessageSecurity(), securityGroupNames, path);

        Set<String> readerNames = new HashSet<>();
        for (DataSetReaderConfig reader : readerGroup.getDataSetReaders()) {
          String readerPath = path + " dataSetReader '%s'".formatted(reader.getName());

          if (!readerNames.add(reader.getName())) {
            throw new PubSubConfigValidationException(
                readerPath + ": duplicate dataSetReader name within reader group");
          }
          if (udp && reader.getSettings() instanceof JsonDataSetReaderSettings) {
            throw new PubSubConfigValidationException(
                readerPath + ": JSON message settings are not allowed on a UDP connection");
          }
          if (reader.getSubscribedDataSet() instanceof StandaloneSubscribedDataSetRef ref
              && !standaloneNames.contains(ref.name())) {
            throw new PubSubConfigValidationException(
                readerPath
                    + ": unresolved StandaloneSubscribedDataSetRef '%s'".formatted(ref.name()));
          }
          checkSecurityGroupRef(reader.getMessageSecurity(), securityGroupNames, readerPath);
        }
      }
    }

    private static void checkSecurityGroupRef(
        @Nullable MessageSecurityConfig security, Set<String> securityGroupNames, String path) {

      if (security == null) {
        return;
      }

      SecurityGroupRef securityGroup = security.getSecurityGroup();
      if (securityGroup != null && !securityGroupNames.contains(securityGroup.name())) {
        throw new PubSubConfigValidationException(
            path + ": unresolved SecurityGroupRef '%s'".formatted(securityGroup.name()));
      }
    }

    private void validatePublisherIdScopes() {
      Map<PublisherId, List<PubSubConnectionConfig>> scopes = new LinkedHashMap<>();
      for (PubSubConnectionConfig connection : connections) {
        PublisherId publisherId = connection.publisherId();
        if (publisherId != null) {
          scopes.computeIfAbsent(publisherId, id -> new ArrayList<>()).add(connection);
        }
      }

      for (Map.Entry<PublisherId, List<PubSubConnectionConfig>> scope : scopes.entrySet()) {
        PublisherId publisherId = scope.getKey();

        Map<String, String> groupNameToConnection = new HashMap<>();
        Map<UShort, String> writerGroupIds = new HashMap<>();
        Map<UShort, String> dataSetWriterIds = new HashMap<>();

        for (PubSubConnectionConfig connection : scope.getValue()) {
          for (WriterGroupConfig writerGroup : connection.writerGroups()) {
            String path =
                "connection '%s' writerGroup '%s'"
                    .formatted(connection.name(), writerGroup.getName());

            String otherConnection =
                groupNameToConnection.putIfAbsent(writerGroup.getName(), connection.name());
            if (otherConnection != null && !otherConnection.equals(connection.name())) {
              throw new PubSubConfigValidationException(
                  "%s: duplicate writer group name within publisherId scope %s (also defined on connection '%s')"
                      .formatted(path, publisherId, otherConnection));
            }

            String otherGroup = writerGroupIds.putIfAbsent(writerGroup.getWriterGroupId(), path);
            if (otherGroup != null) {
              throw new PubSubConfigValidationException(
                  "%s: duplicate writerGroupId %s within publisherId scope %s (also used by %s)"
                      .formatted(path, writerGroup.getWriterGroupId(), publisherId, otherGroup));
            }

            for (DataSetWriterConfig writer : writerGroup.getDataSetWriters()) {
              String writerPath = path + " dataSetWriter '%s'".formatted(writer.getName());

              String otherWriter =
                  dataSetWriterIds.putIfAbsent(writer.getDataSetWriterId(), writerPath);
              if (otherWriter != null) {
                throw new PubSubConfigValidationException(
                    "%s: duplicate dataSetWriterId %s within publisherId scope %s (also used by %s)"
                        .formatted(
                            writerPath, writer.getDataSetWriterId(), publisherId, otherWriter));
              }
            }
          }
        }
      }
    }
  }
}

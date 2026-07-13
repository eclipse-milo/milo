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

import static org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfigMapperUtil.MILO_EVENT_QUEUE_CAPACITY;
import static org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfigMapperUtil.MILO_SOURCE_KEY;
import static org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfigMapperUtil.PROFILE_MQTT_JSON;
import static org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfigMapperUtil.PROFILE_MQTT_UADP;
import static org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfigMapperUtil.PROFILE_UDP_UADP;
import static org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfigMapperUtil.encodeRaw;
import static org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfigMapperUtil.fromKeyValuePairs;
import static org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfigMapperUtil.fromMillis;
import static org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfigMapperUtil.isEmptyMetaData;
import static org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfigMapperUtil.isMulticastAddress;
import static org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfigMapperUtil.isNullUuid;
import static org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfigMapperUtil.isZero;
import static org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfigMapperUtil.newEncodingContext;
import static org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfigMapperUtil.nullOrEmpty;
import static org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfigMapperUtil.requireName;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrokerTransportQualityOfService;
import org.eclipse.milo.opcua.stack.core.types.enumerated.DataSetOrderingType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.BrokerConnectionTransportDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.BrokerDataSetReaderTransportDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.BrokerDataSetWriterTransportDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.BrokerWriterGroupTransportDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.ConnectionTransportDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetReaderDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetReaderMessageDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetReaderTransportDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetWriterDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetWriterMessageDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetWriterTransportDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DatagramConnectionTransportDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DatagramWriterGroupTransportDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.EnumDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.FieldMetaData;
import org.eclipse.milo.opcua.stack.core.types.structured.FieldTargetDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.JsonDataSetMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.JsonDataSetReaderMessageDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.JsonDataSetWriterMessageDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.JsonNetworkMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.JsonWriterGroupMessageDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.NetworkAddressDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.NetworkAddressUrlDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfiguration2DataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConnectionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishedDataItemsDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishedDataSetDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishedDataSetSourceDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishedEventsDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishedVariableDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.ReaderGroupDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.ReaderGroupMessageDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.ReaderGroupTransportDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.SecurityGroupDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.SimpleAttributeOperand;
import org.eclipse.milo.opcua.stack.core.types.structured.SimpleTypeDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.StandaloneSubscribedDataSetDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.StandaloneSubscribedDataSetRefDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureDefinition;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureField;
import org.eclipse.milo.opcua.stack.core.types.structured.SubscribedDataSetDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.TargetVariablesDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpDataSetMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpDataSetReaderMessageDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpDataSetWriterMessageDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpNetworkMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpWriterGroupMessageDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.WriterGroupDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.WriterGroupMessageDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.WriterGroupTransportDataType;
import org.jspecify.annotations.Nullable;

/**
 * Maps a Part 14 {@link PubSubConfiguration2DataType} to an authored {@link PubSubConfig}. See
 * {@link PubSubConfigMapper} for the mapping contract, normalizations, and documented losses.
 *
 * <p>Generated datatype instances may carry null arrays, null entries within the top-level
 * component arrays (connections, groups, writers, readers, datasets, security groups), and null
 * boxed scalars; this mapper tolerates them. Settings structures with no typed config
 * representation are preserved in the raw escape hatches.
 *
 * <p>The per-element entry points are exposed publicly through {@link PubSubConfigElements}: an
 * instance constructed with a caller-supplied security-group resolution context maps one element
 * exactly like {@link #map(PubSubConfiguration2DataType, NamespaceTable)} maps the same element
 * within a whole configuration.
 */
final class DataTypeToConfigMapper {

  private static final DatagramConnectionTransportDataType DEFAULT_UDP_CONNECTION_TRANSPORT =
      new DatagramConnectionTransportDataType(new NetworkAddressUrlDataType(null, null));

  private static final BrokerConnectionTransportDataType DEFAULT_MQTT_CONNECTION_TRANSPORT =
      new BrokerConnectionTransportDataType(null, null);

  private static final DatagramWriterGroupTransportDataType DEFAULT_UDP_WRITER_GROUP_TRANSPORT =
      new DatagramWriterGroupTransportDataType(ubyte(0), 0.0);

  private final NamespaceTable namespaceTable;
  private final EncodingContext encodingContext;
  private final List<SecurityGroupConfig> securityGroups = new ArrayList<>();

  /**
   * @param namespaceTable the table NodeId namespace indices are resolved against.
   * @param securityGroups the security groups wire {@code SecurityGroupId} references resolve
   *     against (see {@link #resolveSecurityGroupRef}); empty for whole-config mapping, which
   *     accumulates the configuration's own groups instead.
   */
  DataTypeToConfigMapper(NamespaceTable namespaceTable, List<SecurityGroupConfig> securityGroups) {
    this.namespaceTable = namespaceTable;
    this.encodingContext = newEncodingContext(namespaceTable);
    this.securityGroups.addAll(securityGroups);
  }

  static PubSubConfig map(PubSubConfiguration2DataType value, NamespaceTable namespaceTable) {
    return new DataTypeToConfigMapper(namespaceTable, List.of()).map(value);
  }

  private PubSubConfig map(PubSubConfiguration2DataType value) {
    PubSubConfig.Builder builder = PubSubConfig.builder();

    builder.enabled(isEnabled(value.getEnabled()));

    // Security groups first: connection mapping resolves SecurityGroupRefs against them.
    for (SecurityGroupDataType securityGroup : nonNullElements(value.getSecurityGroups())) {
      SecurityGroupConfig config = mapSecurityGroup(securityGroup);
      securityGroups.add(config);
      builder.securityGroup(config);
    }

    for (EndpointDescription keyService : nonNullElements(value.getDefaultSecurityKeyServices())) {
      builder.defaultSecurityKeyService(keyService);
    }

    for (PublishedDataSetDataType publishedDataSet :
        nonNullElements(value.getPublishedDataSets())) {
      builder.publishedDataSet(mapPublishedDataSet(publishedDataSet));
    }

    for (StandaloneSubscribedDataSetDataType subscribedDataSet :
        nonNullElements(value.getSubscribedDataSets())) {
      builder.standaloneSubscribedDataSet(mapStandaloneSubscribedDataSet(subscribedDataSet));
    }

    for (PubSubConnectionDataType connection : nonNullElements(value.getConnections())) {
      builder.connection(mapConnection(connection));
    }

    fromKeyValuePairs(value.getConfigurationProperties()).forEach(builder::property);

    return builder.build();
  }

  /**
   * The non-null elements of {@code array}, or an empty list when {@code array} itself is null.
   *
   * <p>A DataType array decoded from the wire may be absent or carry null gaps (e.g. a sparse
   * {@code PublishedDataSetDataType[]}); both are tolerated by skipping them, so one malformed
   * entry never fails the whole mapping.
   */
  private static <T> List<T> nonNullElements(@Nullable T @Nullable [] array) {
    if (array == null) {
      return List.of();
    }
    return Arrays.stream(array).filter(Objects::nonNull).toList();
  }

  // region Connections

  PubSubConnectionConfig mapConnection(PubSubConnectionDataType connection) {
    String name = requireName(connection.getName(), "connection");
    String path = "connection '%s'".formatted(name);

    String url = null;
    String networkInterface = null;

    NetworkAddressDataType address = connection.getAddress();
    if (address instanceof NetworkAddressUrlDataType urlAddress) {
      url = urlAddress.getUrl();
      networkInterface = urlAddress.getNetworkInterface();
    } else if (address != null) {
      throw new PubSubConfigValidationException(
          path + ": unsupported address type " + address.getClass().getName());
    }

    if (nullOrEmpty(url)) {
      throw new PubSubConfigValidationException(path + ": address url is required");
    }

    String profile = connection.getTransportProfileUri();

    boolean udp;
    if (PROFILE_UDP_UADP.equals(profile)) {
      udp = true;
    } else if (PROFILE_MQTT_UADP.equals(profile) || PROFILE_MQTT_JSON.equals(profile)) {
      udp = false;
    } else if (nullOrEmpty(profile)) {
      // Fall back to sniffing the address scheme.
      String lower = url.toLowerCase();
      if (lower.startsWith("opc.udp:")) {
        udp = true;
      } else if (lower.startsWith("mqtt:") || lower.startsWith("mqtts:")) {
        udp = false;
      } else {
        throw new PubSubConfigValidationException(
            path + ": no transportProfileUri and unrecognized address url '" + url + "'");
      }
    } else {
      throw new PubSubConfigValidationException(
          path + ": unsupported transportProfileUri '" + profile + "'");
    }

    if (udp) {
      return mapUdpConnection(connection, name, url, networkInterface, path);
    } else {
      return mapMqttConnection(connection, name, url, path);
    }
  }

  private UdpConnectionConfig mapUdpConnection(
      PubSubConnectionDataType connection,
      String name,
      String url,
      @Nullable String networkInterface,
      String path) {

    UdpConnectionConfig.Builder builder = PubSubConnectionConfig.udp(name);

    builder.enabled(isEnabled(connection.getEnabled()));
    builder.address(parseUdpAddress(url, networkInterface, path));

    Variant publisherId = connection.getPublisherId();
    if (publisherId != null && publisherId.value() != null) {
      builder.publisherId(PublisherId.fromVariant(publisherId));
    }

    for (WriterGroupDataType writerGroup : nonNullElements(connection.getWriterGroups())) {
      builder.writerGroup(mapWriterGroup(writerGroup, path));
    }

    for (ReaderGroupDataType readerGroup : nonNullElements(connection.getReaderGroups())) {
      builder.readerGroup(mapReaderGroup(readerGroup, path));
    }

    fromKeyValuePairs(connection.getConnectionProperties()).forEach(builder::property);

    ConnectionTransportDataType transportSettings = connection.getTransportSettings();
    if (transportSettings != null && !DEFAULT_UDP_CONNECTION_TRANSPORT.equals(transportSettings)) {
      UdpDatagramAddress discoveryAddress = mapDiscoveryAddress(transportSettings, path);
      if (discoveryAddress != null) {
        builder.discoveryAddress(discoveryAddress);
      } else {
        builder.rawTransportSettings(encodeRaw(transportSettings, encodingContext, path));
      }
    }

    return builder.build();
  }

  /**
   * Map UDP connection transport settings to the typed discoveryAddress slot, or null if the shape
   * is not representable and must be preserved in the raw escape hatch instead: only an exact
   * {@link DatagramConnectionTransportDataType} carrying a URL-form discovery address is
   * representable; subtypes (e.g. DatagramConnectionTransport2DataType) carry fields without config
   * slots.
   */
  private static @Nullable UdpDatagramAddress mapDiscoveryAddress(
      ConnectionTransportDataType transportSettings, String path) {

    if (transportSettings.getClass() != DatagramConnectionTransportDataType.class) {
      return null;
    }

    NetworkAddressDataType discoveryAddress =
        ((DatagramConnectionTransportDataType) transportSettings).getDiscoveryAddress();

    if (!(discoveryAddress instanceof NetworkAddressUrlDataType urlAddress)
        || nullOrEmpty(urlAddress.getUrl())) {
      return null;
    }

    return parseUdpAddress(
        urlAddress.getUrl(), urlAddress.getNetworkInterface(), path + " discoveryAddress");
  }

  private MqttConnectionConfig mapMqttConnection(
      PubSubConnectionDataType connection, String name, String url, String path) {

    MqttConnectionConfig.Builder builder = PubSubConnectionConfig.mqtt(name);

    builder.enabled(isEnabled(connection.getEnabled()));

    try {
      builder.brokerUri(new URI(url));
    } catch (URISyntaxException e) {
      throw new PubSubConfigValidationException(path + ": invalid broker url '" + url + "'", e);
    }

    Variant publisherId = connection.getPublisherId();
    if (publisherId != null && publisherId.value() != null) {
      builder.publisherId(PublisherId.fromVariant(publisherId));
    }

    for (WriterGroupDataType writerGroup : nonNullElements(connection.getWriterGroups())) {
      builder.writerGroup(mapWriterGroup(writerGroup, path));
    }

    for (ReaderGroupDataType readerGroup : nonNullElements(connection.getReaderGroups())) {
      builder.readerGroup(mapReaderGroup(readerGroup, path));
    }

    fromKeyValuePairs(connection.getConnectionProperties()).forEach(builder::property);

    ConnectionTransportDataType transportSettings = connection.getTransportSettings();
    if (transportSettings != null && !DEFAULT_MQTT_CONNECTION_TRANSPORT.equals(transportSettings)) {
      builder.rawTransportSettings(encodeRaw(transportSettings, encodingContext, path));
    }

    return builder.build();
  }

  private static UdpDatagramAddress parseUdpAddress(
      String url, @Nullable String networkInterface, String path) {

    String host;
    int port;
    try {
      URI uri = new URI(url);
      host = uri.getHost();
      port = uri.getPort() != -1 ? uri.getPort() : 4840;
    } catch (URISyntaxException e) {
      throw new PubSubConfigValidationException(path + ": invalid address url '" + url + "'", e);
    }

    if (host == null) {
      throw new PubSubConfigValidationException(path + ": invalid address url '" + url + "'");
    }

    UdpDatagramAddress address =
        isMulticastAddress(host)
            ? UdpDatagramAddress.multicast(host, port)
            : UdpDatagramAddress.unicast(host, port);

    if (!nullOrEmpty(networkInterface)) {
      address = address.networkInterface(networkInterface);
    }

    return address;
  }

  // endregion

  // region Writer groups and dataset writers

  WriterGroupConfig mapWriterGroup(WriterGroupDataType group, String parentPath) {
    String name = requireName(group.getName(), parentPath + " writerGroup");
    String path = parentPath + " writerGroup '%s'".formatted(name);

    WriterGroupConfig.Builder builder = WriterGroupConfig.builder(name);

    builder.enabled(isEnabled(group.getEnabled()));
    if (group.getWriterGroupId() != null) {
      builder.writerGroupId(group.getWriterGroupId());
    }
    builder.publishingInterval(fromMillis(group.getPublishingInterval()));

    Duration keepAliveTime = fromMillis(group.getKeepAliveTime());
    if (!keepAliveTime.isZero()) {
      builder.keepAliveTime(keepAliveTime);
    }

    if (group.getPriority() != null) {
      builder.priority(group.getPriority());
    }
    if (group.getMaxNetworkMessageSize() != null) {
      builder.maxNetworkMessageSize(group.getMaxNetworkMessageSize());
    }

    MessageSecurityConfig messageSecurity =
        messageSecurity(
            group.getSecurityMode(), group.getSecurityGroupId(), group.getSecurityKeyServices());
    if (messageSecurity != null) {
      builder.messageSecurity(messageSecurity);
    }

    fromKeyValuePairs(group.getGroupProperties()).forEach(builder::property);

    WriterGroupTransportDataType transportSettings = group.getTransportSettings();
    if (transportSettings != null) {
      if (transportSettings.getClass() == BrokerWriterGroupTransportDataType.class) {
        BrokerWriterGroupTransportDataType broker =
            (BrokerWriterGroupTransportDataType) transportSettings;
        BrokerTransportSettings brokerTransport =
            brokerTransport(
                broker.getQueueName(),
                broker.getResourceUri(),
                broker.getAuthenticationProfileUri(),
                broker.getRequestedDeliveryGuarantee(),
                null,
                null);
        if (brokerTransport != null) {
          builder.brokerTransport(brokerTransport);
        }
      } else if (!DEFAULT_UDP_WRITER_GROUP_TRANSPORT.equals(transportSettings)) {
        builder.rawTransportSettings(encodeRaw(transportSettings, encodingContext, path));
      }
    }

    WriterGroupMessageDataType messageSettings = group.getMessageSettings();
    if (messageSettings != null) {
      if (messageSettings.getClass() == UadpWriterGroupMessageDataType.class
          && isRepresentable((UadpWriterGroupMessageDataType) messageSettings)) {
        UadpWriterGroupMessageDataType uadp = (UadpWriterGroupMessageDataType) messageSettings;
        UadpNetworkMessageContentMask mask = uadp.getNetworkMessageContentMask();
        builder.messageSettings(
            UadpWriterGroupSettings.builder()
                .groupVersion(uadp.getGroupVersion() != null ? uadp.getGroupVersion() : uint(0))
                .dataSetOrdering(
                    uadp.getDataSetOrdering() != null
                        ? uadp.getDataSetOrdering()
                        : DataSetOrderingType.Undefined)
                .networkMessageContentMask(mask != null ? mask : UadpNetworkMessageContentMask.of())
                .build());
      } else if (messageSettings.getClass() == JsonWriterGroupMessageDataType.class) {
        JsonNetworkMessageContentMask mask =
            ((JsonWriterGroupMessageDataType) messageSettings).getNetworkMessageContentMask();
        builder.messageSettings(
            JsonWriterGroupSettings.builder()
                .networkMessageContentMask(mask != null ? mask : JsonNetworkMessageContentMask.of())
                .build());
      } else {
        builder.rawMessageSettings(encodeRaw(messageSettings, encodingContext, path));
      }
    }

    for (DataSetWriterDataType writer : nonNullElements(group.getDataSetWriters())) {
      builder.dataSetWriter(mapDataSetWriter(writer, path));
    }

    return builder.build();
  }

  /** SamplingOffset and PublishingOffset have no typed config slots; non-defaults go raw. */
  private static boolean isRepresentable(UadpWriterGroupMessageDataType uadp) {
    Double[] publishingOffset = uadp.getPublishingOffset();
    return isZero(uadp.getSamplingOffset())
        && (publishingOffset == null || publishingOffset.length == 0);
  }

  DataSetWriterConfig mapDataSetWriter(DataSetWriterDataType writer, String parentPath) {
    String name = requireName(writer.getName(), parentPath + " dataSetWriter");
    String path = parentPath + " dataSetWriter '%s'".formatted(name);

    DataSetWriterConfig.Builder builder = DataSetWriterConfig.builder(name);

    builder.enabled(isEnabled(writer.getEnabled()));

    String dataSetName = writer.getDataSetName();
    if (nullOrEmpty(dataSetName)) {
      throw new PubSubConfigValidationException(path + ": dataSetName is required");
    }
    builder.dataSet(new PublishedDataSetRef(dataSetName));

    if (writer.getDataSetWriterId() != null) {
      builder.dataSetWriterId(writer.getDataSetWriterId());
    }
    if (writer.getKeyFrameCount() != null) {
      builder.keyFrameCount(writer.getKeyFrameCount());
    }
    if (writer.getDataSetFieldContentMask() != null) {
      builder.fieldContentMask(writer.getDataSetFieldContentMask());
    }

    Map<QualifiedName, Variant> properties = fromKeyValuePairs(writer.getDataSetWriterProperties());
    Variant eventQueueCapacity = properties.remove(MILO_EVENT_QUEUE_CAPACITY);
    if (eventQueueCapacity != null) {
      builder.eventQueueCapacity(eventQueueCapacity(eventQueueCapacity, path));
    }
    properties.forEach(builder::property);

    DataSetWriterTransportDataType transportSettings = writer.getTransportSettings();
    if (transportSettings != null) {
      if (transportSettings.getClass() == BrokerDataSetWriterTransportDataType.class) {
        BrokerDataSetWriterTransportDataType broker =
            (BrokerDataSetWriterTransportDataType) transportSettings;
        BrokerTransportSettings brokerTransport =
            brokerTransport(
                broker.getQueueName(),
                broker.getResourceUri(),
                broker.getAuthenticationProfileUri(),
                broker.getRequestedDeliveryGuarantee(),
                broker.getMetaDataQueueName(),
                broker.getMetaDataUpdateTime());
        if (brokerTransport != null) {
          builder.brokerTransport(brokerTransport);
        }
      } else {
        builder.rawTransportSettings(encodeRaw(transportSettings, encodingContext, path));
      }
    }

    DataSetWriterMessageDataType messageSettings = writer.getMessageSettings();
    if (messageSettings != null) {
      if (messageSettings.getClass() == UadpDataSetWriterMessageDataType.class) {
        UadpDataSetWriterMessageDataType uadp = (UadpDataSetWriterMessageDataType) messageSettings;
        UadpDataSetMessageContentMask mask = uadp.getDataSetMessageContentMask();
        UadpDataSetWriterSettings.Builder settings =
            UadpDataSetWriterSettings.builder()
                .dataSetMessageContentMask(
                    mask != null ? mask : UadpDataSetMessageContentMask.of());
        if (uadp.getConfiguredSize() != null) {
          settings.configuredSize(uadp.getConfiguredSize());
        }
        if (uadp.getNetworkMessageNumber() != null) {
          settings.networkMessageNumber(uadp.getNetworkMessageNumber());
        }
        if (uadp.getDataSetOffset() != null) {
          settings.dataSetOffset(uadp.getDataSetOffset());
        }
        builder.settings(settings.build());
      } else if (messageSettings.getClass() == JsonDataSetWriterMessageDataType.class) {
        JsonDataSetMessageContentMask mask =
            ((JsonDataSetWriterMessageDataType) messageSettings).getDataSetMessageContentMask();
        builder.settings(
            JsonDataSetWriterSettings.builder()
                .dataSetMessageContentMask(mask != null ? mask : JsonDataSetMessageContentMask.of())
                .build());
      } else {
        builder.rawMessageSettings(encodeRaw(messageSettings, encodingContext, path));
      }
    }

    return builder.build();
  }

  /**
   * Decode the reserved {@code 0:MiloEventQueueCapacity} writer property consumed from {@code
   * DataSetWriterProperties}.
   */
  private static int eventQueueCapacity(Variant value, String path) {
    long capacity;
    if (value.value() instanceof Integer i) {
      capacity = i;
    } else if (value.value() instanceof UInteger u) {
      capacity = u.longValue();
    } else {
      throw new PubSubConfigValidationException(
          path + ": MiloEventQueueCapacity must be an Int32 or UInt32 value, found " + value);
    }

    if (capacity < 1 || capacity > Integer.MAX_VALUE) {
      throw new PubSubConfigValidationException(
          path + ": MiloEventQueueCapacity must be between 1 and 2147483647, found " + capacity);
    }

    return (int) capacity;
  }

  // endregion

  // region Reader groups and dataset readers

  ReaderGroupConfig mapReaderGroup(ReaderGroupDataType group, String parentPath) {
    String name = requireName(group.getName(), parentPath + " readerGroup");
    String path = parentPath + " readerGroup '%s'".formatted(name);

    ReaderGroupConfig.Builder builder = ReaderGroupConfig.builder(name);

    builder.enabled(isEnabled(group.getEnabled()));
    if (group.getMaxNetworkMessageSize() != null) {
      builder.maxNetworkMessageSize(group.getMaxNetworkMessageSize());
    }

    MessageSecurityConfig messageSecurity =
        messageSecurity(
            group.getSecurityMode(), group.getSecurityGroupId(), group.getSecurityKeyServices());
    if (messageSecurity != null) {
      builder.messageSecurity(messageSecurity);
    }

    fromKeyValuePairs(group.getGroupProperties()).forEach(builder::property);

    // Part 14 defines no concrete reader group transport/message types; preserve raw.
    ReaderGroupTransportDataType transportSettings = group.getTransportSettings();
    if (transportSettings != null) {
      builder.rawTransportSettings(encodeRaw(transportSettings, encodingContext, path));
    }
    ReaderGroupMessageDataType messageSettings = group.getMessageSettings();
    if (messageSettings != null) {
      builder.rawMessageSettings(encodeRaw(messageSettings, encodingContext, path));
    }

    for (DataSetReaderDataType reader : nonNullElements(group.getDataSetReaders())) {
      builder.dataSetReader(mapDataSetReader(reader, path));
    }

    return builder.build();
  }

  DataSetReaderConfig mapDataSetReader(DataSetReaderDataType reader, String parentPath) {
    String name = requireName(reader.getName(), parentPath + " dataSetReader");
    String path = parentPath + " dataSetReader '%s'".formatted(name);

    DataSetReaderConfig.Builder builder = DataSetReaderConfig.builder(name);

    builder.enabled(isEnabled(reader.getEnabled()));

    Variant publisherId = reader.getPublisherId();
    if (publisherId != null && publisherId.value() != null) {
      builder.publisherId(PublisherId.fromVariant(publisherId));
    }
    if (reader.getWriterGroupId() != null && reader.getWriterGroupId().intValue() != 0) {
      builder.writerGroupId(reader.getWriterGroupId());
    }
    if (reader.getDataSetWriterId() != null && reader.getDataSetWriterId().intValue() != 0) {
      builder.dataSetWriterId(reader.getDataSetWriterId());
    }

    MessageSecurityConfig messageSecurity =
        readerMessageSecurity(
            reader.getSecurityMode(), reader.getSecurityGroupId(), reader.getSecurityKeyServices());
    if (messageSecurity != null) {
      builder.messageSecurity(messageSecurity);
    }

    DataSetMetaDataType metaData = reader.getDataSetMetaData();
    if (metaData != null && !isEmptyMetaData(metaData)) {
      builder.dataSetMetaData(mapMetaData(metaData, name, path));
    }

    Duration messageReceiveTimeout = fromMillis(reader.getMessageReceiveTimeout());
    if (!messageReceiveTimeout.isZero()) {
      builder.messageReceiveTimeout(messageReceiveTimeout);
    }
    if (reader.getKeyFrameCount() != null) {
      builder.keyFrameCount(reader.getKeyFrameCount());
    }

    fromKeyValuePairs(reader.getDataSetReaderProperties()).forEach(builder::property);

    DataSetReaderTransportDataType transportSettings = reader.getTransportSettings();
    if (transportSettings != null) {
      if (transportSettings.getClass() == BrokerDataSetReaderTransportDataType.class) {
        BrokerDataSetReaderTransportDataType broker =
            (BrokerDataSetReaderTransportDataType) transportSettings;
        BrokerTransportSettings brokerTransport =
            brokerTransport(
                broker.getQueueName(),
                broker.getResourceUri(),
                broker.getAuthenticationProfileUri(),
                broker.getRequestedDeliveryGuarantee(),
                broker.getMetaDataQueueName(),
                null);
        if (brokerTransport != null) {
          builder.brokerTransport(brokerTransport);
        }
      } else {
        builder.rawTransportSettings(encodeRaw(transportSettings, encodingContext, path));
      }
    }

    DataSetReaderMessageDataType messageSettings = reader.getMessageSettings();
    if (messageSettings != null) {
      if (messageSettings.getClass() == UadpDataSetReaderMessageDataType.class
          && isRepresentable((UadpDataSetReaderMessageDataType) messageSettings)) {
        UadpDataSetReaderMessageDataType uadp = (UadpDataSetReaderMessageDataType) messageSettings;
        UadpDataSetReaderSettings.Builder settings = UadpDataSetReaderSettings.builder();
        if (uadp.getGroupVersion() != null) {
          settings.groupVersion(uadp.getGroupVersion());
        }
        if (uadp.getNetworkMessageNumber() != null) {
          settings.networkMessageNumber(uadp.getNetworkMessageNumber());
        }
        if (uadp.getDataSetOffset() != null) {
          settings.dataSetOffset(uadp.getDataSetOffset());
        }
        UadpNetworkMessageContentMask networkMask = uadp.getNetworkMessageContentMask();
        settings.networkMessageContentMask(
            networkMask != null ? networkMask : UadpNetworkMessageContentMask.of());
        UadpDataSetMessageContentMask dataSetMask = uadp.getDataSetMessageContentMask();
        settings.dataSetMessageContentMask(
            dataSetMask != null ? dataSetMask : UadpDataSetMessageContentMask.of());
        builder.settings(settings.build());
      } else if (messageSettings.getClass() == JsonDataSetReaderMessageDataType.class) {
        JsonDataSetReaderMessageDataType json = (JsonDataSetReaderMessageDataType) messageSettings;
        JsonNetworkMessageContentMask networkMask = json.getNetworkMessageContentMask();
        JsonDataSetMessageContentMask dataSetMask = json.getDataSetMessageContentMask();
        builder.settings(
            JsonDataSetReaderSettings.builder()
                .networkMessageContentMask(
                    networkMask != null ? networkMask : JsonNetworkMessageContentMask.of())
                .dataSetMessageContentMask(
                    dataSetMask != null ? dataSetMask : JsonDataSetMessageContentMask.of())
                .build());
      } else {
        builder.rawMessageSettings(encodeRaw(messageSettings, encodingContext, path));
      }
    }

    SubscribedDataSetDataType subscribedDataSet = reader.getSubscribedDataSet();
    if (subscribedDataSet != null) {
      if (subscribedDataSet.getClass() == TargetVariablesDataType.class) {
        FieldTargetDataType[] targetVariables =
            ((TargetVariablesDataType) subscribedDataSet).getTargetVariables();
        if (targetVariables != null && targetVariables.length > 0) {
          builder.subscribedDataSet(mapTargetVariables(targetVariables, path));
        }
        // An empty TargetVariables is the shape emitted for "no subscribed dataset".
      } else if (subscribedDataSet.getClass() == StandaloneSubscribedDataSetRefDataType.class) {
        String dataSetName =
            ((StandaloneSubscribedDataSetRefDataType) subscribedDataSet).getDataSetName();
        builder.subscribedDataSet(
            new StandaloneSubscribedDataSetRef(
                requireName(dataSetName, path + " subscribedDataSet ref")));
      } else {
        throw new PubSubConfigValidationException(
            path
                + ": unsupported subscribed dataset type "
                + subscribedDataSet.getClass().getName());
      }
    }

    return builder.build();
  }

  /**
   * DataSetClassId, PublishingInterval, ReceiveOffset, and ProcessingOffset have no typed config
   * slots; non-defaults go raw.
   */
  private static boolean isRepresentable(UadpDataSetReaderMessageDataType uadp) {
    return isNullUuid(uadp.getDataSetClassId())
        && isZero(uadp.getPublishingInterval())
        && isZero(uadp.getReceiveOffset())
        && isZero(uadp.getProcessingOffset());
  }

  private TargetVariablesConfig mapTargetVariables(FieldTargetDataType[] targets, String path) {
    TargetVariablesConfig.Builder builder = TargetVariablesConfig.builder();

    for (FieldTargetDataType target : targets) {
      UUID dataSetFieldId = target.getDataSetFieldId();
      if (dataSetFieldId == null) {
        throw new PubSubConfigValidationException(
            path + ": target variable dataSetFieldId is required");
      }

      NodeId targetNodeId = target.getTargetNodeId();
      if (targetNodeId == null || targetNodeId.isNull()) {
        throw new PubSubConfigValidationException(
            path + ": target variable targetNodeId is required");
      }

      TargetVariableConfig.Builder targetBuilder = TargetVariableConfig.builder();
      targetBuilder.target(
          nodeFieldAddress(targetNodeId, attributeId(target.getAttributeId(), path), path));

      if (target.getReceiverIndexRange() != null) {
        targetBuilder.receiverIndexRange(target.getReceiverIndexRange());
      }
      if (target.getWriteIndexRange() != null) {
        targetBuilder.writeIndexRange(target.getWriteIndexRange());
      }
      if (target.getOverrideValueHandling() != null) {
        targetBuilder.overrideHandling(target.getOverrideValueHandling());
      }
      Variant overrideValue = target.getOverrideValue();
      if (overrideValue != null && overrideValue.value() != null) {
        targetBuilder.overrideValue(overrideValue);
      }

      builder.map(FieldSelector.byId(dataSetFieldId), targetBuilder.build());
    }

    return builder.build();
  }

  // endregion

  // region Published datasets, standalone subscribed datasets, security groups

  PublishedDataSetConfig mapPublishedDataSet(PublishedDataSetDataType dataSet) {
    String name = requireName(dataSet.getName(), "publishedDataSet");
    String path = "publishedDataSet '%s'".formatted(name);

    PublishedDataSetConfig.Builder builder = PublishedDataSetConfig.builder(name);

    DataSetMetaDataType metaData = dataSet.getDataSetMetaData();

    ConfigurationVersionDataType version =
        metaData != null ? metaData.getConfigurationVersion() : null;
    if (version != null) {
      builder.configurationVersion(
          version.getMajorVersion() != null ? version.getMajorVersion() : uint(0),
          version.getMinorVersion() != null ? version.getMinorVersion() : uint(0));
    }

    FieldMetaData[] fields =
        metaData != null && metaData.getFields() != null
            ? metaData.getFields()
            : new FieldMetaData[0];

    // NodeIds and names inside the metadata use metadata-local namespace indexes backed by the
    // DataTypeSchemaHeader namespaces array (OPC UA 10000-5 §12.31); resolve them back to
    // publisher-local indexes. Metadata carrying no namespaces array has nothing to resolve
    // against and passes indexes through unchanged.
    var metaDataNamespaces =
        new MetaDataNamespaceResolver(
            metaData != null ? metaData.getNamespaces() : null, namespaceTable, path);

    PublishedDataSetSourceDataType source = dataSet.getDataSetSource();
    if (source == null || source instanceof PublishedDataItemsDataType) {
      PublishedVariableDataType[] publishedData = new PublishedVariableDataType[0];
      if (source instanceof PublishedDataItemsDataType items && items.getPublishedData() != null) {
        publishedData = items.getPublishedData();
      }

      for (int i = 0; i < fields.length; i++) {
        FieldMetaData fieldMetaData = fields[i];
        PublishedVariableDataType variable = i < publishedData.length ? publishedData[i] : null;
        builder.field(mapFieldDefinition(fieldMetaData, variable, metaDataNamespaces, path));
      }
    } else if (source instanceof PublishedEventsDataType events) {
      builder.source(mapPublishedEvents(events, fields, metaDataNamespaces, path));
    } else {
      throw new PubSubConfigValidationException(
          path
              + ": only PublishedDataItems and PublishedEvents dataset sources are supported,"
              + " found "
              + source.getClass().getName());
    }

    if (metaData != null) {
      mapTypeDescriptions(metaData, metaDataNamespaces, builder);
    }

    fromKeyValuePairs(dataSet.getExtensionFields()).forEach(builder::property);

    return builder.build();
  }

  /** Map the DataTypeSchemaHeader type descriptions back to authored, publisher-local form. */
  private static void mapTypeDescriptions(
      DataSetMetaDataType metaData,
      MetaDataNamespaceResolver metaDataNamespaces,
      PublishedDataSetConfig.Builder builder) {

    StructureDescription[] structureDataTypes = metaData.getStructureDataTypes();
    if (structureDataTypes != null) {
      for (StructureDescription description : structureDataTypes) {
        if (description != null) {
          builder.structureDataType(
              new StructureDescription(
                  metaDataNamespaces.resolve(description.getDataTypeId()),
                  metaDataNamespaces.resolve(description.getName()),
                  resolveStructureDefinition(
                      description.getStructureDefinition(), metaDataNamespaces)));
        }
      }
    }

    EnumDescription[] enumDataTypes = metaData.getEnumDataTypes();
    if (enumDataTypes != null) {
      for (EnumDescription description : enumDataTypes) {
        if (description != null) {
          builder.enumDataType(
              new EnumDescription(
                  metaDataNamespaces.resolve(description.getDataTypeId()),
                  metaDataNamespaces.resolve(description.getName()),
                  description.getEnumDefinition(),
                  description.getBuiltInType()));
        }
      }
    }

    SimpleTypeDescription[] simpleDataTypes = metaData.getSimpleDataTypes();
    if (simpleDataTypes != null) {
      for (SimpleTypeDescription description : simpleDataTypes) {
        if (description != null) {
          builder.simpleDataType(
              new SimpleTypeDescription(
                  metaDataNamespaces.resolve(description.getDataTypeId()),
                  metaDataNamespaces.resolve(description.getName()),
                  metaDataNamespaces.resolve(description.getBaseDataType()),
                  description.getBuiltInType()));
        }
      }
    }
  }

  private static StructureDefinition resolveStructureDefinition(
      StructureDefinition definition, MetaDataNamespaceResolver metaDataNamespaces) {

    StructureField[] fields = definition.getFields();
    StructureField[] resolvedFields = null;
    if (fields != null) {
      resolvedFields = new StructureField[fields.length];
      for (int i = 0; i < fields.length; i++) {
        StructureField field = fields[i];
        resolvedFields[i] =
            new StructureField(
                field.getName(),
                field.getDescription(),
                metaDataNamespaces.resolve(field.getDataType()),
                field.getValueRank(),
                field.getArrayDimensions(),
                field.getMaxStringLength(),
                field.getIsOptional());
      }
    }

    return new StructureDefinition(
        metaDataNamespaces.resolve(definition.getDefaultEncodingId()),
        metaDataNamespaces.resolve(definition.getBaseDataType()),
        definition.getStructureType(),
        resolvedFields);
  }

  /**
   * Resolves metadata-local namespace indexes — backed by the DataSetMetaData's
   * DataTypeSchemaHeader namespaces array, where entry {@code k} corresponds to NamespaceIndex
   * {@code k + 1} and index 0 is the OPC UA namespace (OPC UA 10000-5 §12.31) — back to
   * publisher-local indexes in the target namespace table. Metadata carrying no namespaces array
   * passes indexes through unchanged (the shape emitted before the schema header was populated).
   */
  private static final class MetaDataNamespaceResolver {

    private final String @Nullable [] namespaces;
    private final NamespaceTable namespaceTable;
    private final String path;

    private MetaDataNamespaceResolver(
        String @Nullable [] namespaces, NamespaceTable namespaceTable, String path) {
      this.namespaces = namespaces;
      this.namespaceTable = namespaceTable;
      this.path = path;
    }

    NodeId resolve(NodeId nodeId) {
      if (namespaces == null || nodeId.getNamespaceIndex().intValue() == 0) {
        return nodeId;
      }
      return nodeId.withNamespaceIndex(publisherLocalIndex(nodeId.getNamespaceIndex()));
    }

    QualifiedName resolve(QualifiedName name) {
      if (namespaces == null || name.namespaceIndex().intValue() == 0) {
        return name;
      }
      return name.withNamespaceIndex(publisherLocalIndex(name.namespaceIndex()));
    }

    private UShort publisherLocalIndex(UShort metaDataIndex) {
      assert namespaces != null;
      int k = metaDataIndex.intValue();
      String uri = k <= namespaces.length ? namespaces[k - 1] : null;
      if (uri == null) {
        throw new PubSubConfigValidationException(
            "%s: metadata namespace index %s has no entry in the metadata namespaces array"
                .formatted(path, metaDataIndex));
      }
      UShort index = namespaceTable.getIndex(uri);
      if (index == null) {
        throw new PubSubConfigValidationException(
            "%s: metadata namespace %s not present in the namespace table".formatted(path, uri));
      }
      return index;
    }
  }

  private PublishedEventsConfig mapPublishedEvents(
      PublishedEventsDataType events,
      FieldMetaData[] fields,
      MetaDataNamespaceResolver metaDataNamespaces,
      String path) {

    NodeId eventNotifier = events.getEventNotifier();

    ExpandedNodeId notifier;
    try {
      // Mirror NodeFieldAddress.of so the canonical namespace-URI form of authored and imported
      // eventNotifiers agrees.
      notifier = eventNotifier.expanded(namespaceTable);
    } catch (IllegalStateException e) {
      throw new PubSubConfigValidationException(
          path + ": namespace of NodeId " + eventNotifier + " not present in the namespace table",
          e);
    }

    PublishedEventsConfig.Builder builder = PublishedEventsConfig.builder(notifier);

    SimpleAttributeOperand[] selectedFields =
        Objects.requireNonNullElse(events.getSelectedFields(), new SimpleAttributeOperand[0]);

    if (selectedFields.length != fields.length) {
      throw new PubSubConfigValidationException(
          "%s: selectedFields length %d does not match metadata field count %d"
              .formatted(path, selectedFields.length, fields.length));
    }

    for (int i = 0; i < fields.length; i++) {
      builder.field(
          mapEventFieldDefinition(fields[i], selectedFields[i], metaDataNamespaces, path));
    }

    ContentFilter filter = events.getFilter();
    builder.filter(filter != null ? filter : new ContentFilter(null));

    return builder.build();
  }

  private static EventFieldDefinition mapEventFieldDefinition(
      FieldMetaData fieldMetaData,
      @Nullable SimpleAttributeOperand selectedField,
      MetaDataNamespaceResolver metaDataNamespaces,
      String path) {

    String fieldName = requireName(fieldMetaData.getName(), path + " field");
    String fieldPath = path + " field '%s'".formatted(fieldName);

    if (selectedField == null) {
      throw new PubSubConfigValidationException(fieldPath + ": selectedField is required");
    }

    EventFieldDefinition.Builder builder = EventFieldDefinition.builder(fieldName);

    builder.selectedField(selectedField);

    if (fieldMetaData.getDataType() != null) {
      builder.dataType(metaDataNamespaces.resolve(fieldMetaData.getDataType()));
    }
    if (fieldMetaData.getDataSetFieldId() != null) {
      builder.dataSetFieldId(fieldMetaData.getDataSetFieldId());
    }
    builder.promoted(
        fieldMetaData.getFieldFlags() != null && fieldMetaData.getFieldFlags().getPromotedField());
    if (fieldMetaData.getValueRank() != null) {
      builder.valueRank(fieldMetaData.getValueRank());
    }
    builder.arrayDimensions(fieldMetaData.getArrayDimensions());

    fromKeyValuePairs(fieldMetaData.getProperties())
        .forEach((name, value) -> builder.property(metaDataNamespaces.resolve(name), value));

    return builder.build();
  }

  private FieldDefinition mapFieldDefinition(
      FieldMetaData fieldMetaData,
      @Nullable PublishedVariableDataType variable,
      MetaDataNamespaceResolver metaDataNamespaces,
      String path) {

    String fieldName = requireName(fieldMetaData.getName(), path + " field");
    String fieldPath = path + " field '%s'".formatted(fieldName);

    FieldDefinition.Builder builder = FieldDefinition.builder(fieldName);

    Map<QualifiedName, Variant> properties = new LinkedHashMap<>();
    fromKeyValuePairs(fieldMetaData.getProperties())
        .forEach((name, value) -> properties.put(metaDataNamespaces.resolve(name), value));
    Variant sourceKey = properties.remove(MILO_SOURCE_KEY);

    if (fieldMetaData.getDataType() != null) {
      builder.dataType(metaDataNamespaces.resolve(fieldMetaData.getDataType()));
    }
    if (fieldMetaData.getDataSetFieldId() != null) {
      builder.dataSetFieldId(fieldMetaData.getDataSetFieldId());
    }
    builder.promoted(
        fieldMetaData.getFieldFlags() != null && fieldMetaData.getFieldFlags().getPromotedField());
    if (fieldMetaData.getValueRank() != null) {
      builder.valueRank(fieldMetaData.getValueRank());
    }
    builder.arrayDimensions(fieldMetaData.getArrayDimensions());

    properties.forEach(builder::property);

    if (sourceKey != null && sourceKey.value() instanceof String key) {
      builder.source(KeyFieldAddress.of(key));
    } else if (variable != null
        && variable.getPublishedVariable() != null
        && !variable.getPublishedVariable().isNull()) {
      builder.source(
          nodeFieldAddress(
              variable.getPublishedVariable(),
              attributeId(variable.getAttributeId(), fieldPath),
              fieldPath));
    }
    // Otherwise FieldDefinition defaults the source to a KeyFieldAddress keyed by field name.

    return builder.build();
  }

  StandaloneSubscribedDataSetConfig mapStandaloneSubscribedDataSet(
      StandaloneSubscribedDataSetDataType dataSet) {

    String name = requireName(dataSet.getName(), "standaloneSubscribedDataSet");
    String path = "standaloneSubscribedDataSet '%s'".formatted(name);

    DataSetMetaDataType metaData = dataSet.getDataSetMetaData();
    if (metaData == null) {
      throw new PubSubConfigValidationException(path + ": dataSetMetaData is required");
    }

    SubscribedDataSetSpec spec;
    SubscribedDataSetDataType subscribedDataSet = dataSet.getSubscribedDataSet();
    if (subscribedDataSet == null) {
      throw new PubSubConfigValidationException(path + ": subscribedDataSet is required");
    } else if (subscribedDataSet.getClass() == TargetVariablesDataType.class) {
      FieldTargetDataType[] targetVariables =
          ((TargetVariablesDataType) subscribedDataSet).getTargetVariables();
      spec =
          mapTargetVariables(
              targetVariables != null ? targetVariables : new FieldTargetDataType[0], path);
    } else if (subscribedDataSet.getClass() == StandaloneSubscribedDataSetRefDataType.class) {
      String dataSetName =
          ((StandaloneSubscribedDataSetRefDataType) subscribedDataSet).getDataSetName();
      spec = new StandaloneSubscribedDataSetRef(requireName(dataSetName, path + " ref"));
    } else {
      throw new PubSubConfigValidationException(
          path + ": unsupported subscribed dataset type " + subscribedDataSet.getClass().getName());
    }

    return StandaloneSubscribedDataSetConfig.builder(name)
        .metaData(mapMetaData(metaData, name, path))
        .subscribedDataSet(spec)
        .build();
  }

  private static DataSetMetaDataConfig mapMetaData(
      DataSetMetaDataType metaData, String fallbackName, String path) {

    String name = !nullOrEmpty(metaData.getName()) ? metaData.getName() : fallbackName;

    DataSetMetaDataConfig.Builder builder = DataSetMetaDataConfig.builder(name);

    FieldMetaData[] fields = metaData.getFields();
    if (fields != null) {
      for (FieldMetaData field : fields) {
        String fieldName = requireName(field.getName(), path + " metadata field");
        NodeId dataTypeId =
            field.getDataType() != null ? field.getDataType() : NodeIds.BaseDataType;
        UUID dataSetFieldId =
            field.getDataSetFieldId() != null ? field.getDataSetFieldId() : UUID.randomUUID();
        int valueRank = field.getValueRank() != null ? field.getValueRank() : -1;
        builder.field(
            new DataSetMetaDataConfig.Field(
                fieldName, dataTypeId, dataSetFieldId, valueRank, field.getArrayDimensions()));
      }
    }

    if (!isNullUuid(metaData.getDataSetClassId())) {
      builder.dataSetClassId(metaData.getDataSetClassId());
    }

    ConfigurationVersionDataType version = metaData.getConfigurationVersion();
    if (version != null) {
      builder.configurationVersion(
          version.getMajorVersion() != null ? version.getMajorVersion() : uint(0),
          version.getMinorVersion() != null ? version.getMinorVersion() : uint(0));
    }

    return builder.build();
  }

  static SecurityGroupConfig mapSecurityGroup(SecurityGroupDataType securityGroup) {
    String name = requireName(securityGroup.getName(), "securityGroup");

    SecurityGroupConfig.Builder builder = SecurityGroupConfig.builder(name);

    if (!nullOrEmpty(securityGroup.getSecurityGroupId())) {
      builder.securityGroupId(securityGroup.getSecurityGroupId());
    }
    builder.securityGroupFolder(nonNullElements(securityGroup.getSecurityGroupFolder()));
    if (securityGroup.getSecurityPolicyUri() != null) {
      builder.securityPolicyUri(securityGroup.getSecurityPolicyUri());
    }
    if (securityGroup.getKeyLifetime() != null) {
      builder.keyLifeTime(fromMillis(securityGroup.getKeyLifetime()));
    }
    if (securityGroup.getMaxFutureKeyCount() != null) {
      builder.maxFutureKeyCount(securityGroup.getMaxFutureKeyCount());
    }
    if (securityGroup.getMaxPastKeyCount() != null) {
      builder.maxPastKeyCount(securityGroup.getMaxPastKeyCount());
    }
    for (RolePermissionType rolePermission : nonNullElements(securityGroup.getRolePermissions())) {
      builder.rolePermission(rolePermission);
    }

    fromKeyValuePairs(securityGroup.getGroupProperties()).forEach(builder::property);

    return builder.build();
  }

  // endregion

  // region Shared helpers

  private static boolean isEnabled(@Nullable Boolean enabled) {
    return enabled == null || enabled;
  }

  private NodeFieldAddress nodeFieldAddress(NodeId nodeId, AttributeId attributeId, String path) {
    try {
      return NodeFieldAddress.of(nodeId, attributeId, namespaceTable);
    } catch (IllegalStateException e) {
      throw new PubSubConfigValidationException(
          path + ": namespace of NodeId " + nodeId + " not present in the namespace table", e);
    }
  }

  private static AttributeId attributeId(@Nullable UInteger attributeId, String path) {
    if (attributeId == null || attributeId.intValue() == 0) {
      return AttributeId.Value;
    }
    return AttributeId.from(attributeId)
        .orElseThrow(
            () ->
                new PubSubConfigValidationException(path + ": invalid attributeId " + attributeId));
  }

  /**
   * Build a {@link MessageSecurityConfig} from group-level security fields; the all-default shape
   * (None mode, no group id, no key services) maps to absent.
   */
  private @Nullable MessageSecurityConfig messageSecurity(
      @Nullable MessageSecurityMode mode,
      @Nullable String securityGroupId,
      @Nullable EndpointDescription @Nullable [] keyServices) {

    List<EndpointDescription> services = nonNullElements(keyServices);

    boolean modeAbsent = mode == null || mode == MessageSecurityMode.None;
    boolean groupAbsent = nullOrEmpty(securityGroupId);
    boolean servicesAbsent = services.isEmpty();

    if (modeAbsent && groupAbsent && servicesAbsent) {
      return null;
    }

    MessageSecurityConfig.Builder builder = MessageSecurityConfig.builder();
    if (mode != null) {
      builder.mode(mode);
    }
    if (!groupAbsent) {
      builder.securityGroup(resolveSecurityGroupRef(securityGroupId));
    }
    if (!servicesAbsent) {
      builder.keyServices(services);
    }
    return builder.build();
  }

  /**
   * Build a {@link MessageSecurityConfig} from reader-level security override fields.
   *
   * <p>Deliberately separate from the group-level {@link #messageSecurity} helper: at the reader,
   * mode {@code None} is a legal <em>active</em> override, and the spec's no-override sentinel is
   * {@code Invalid} (Part 14 §6.2.9.9). The override maps to absent iff the mode is null or {@code
   * Invalid}, or the shape is the legacy all-default {@code None}/null/null emitted by earlier Milo
   * versions.
   */
  private @Nullable MessageSecurityConfig readerMessageSecurity(
      @Nullable MessageSecurityMode mode,
      @Nullable String securityGroupId,
      @Nullable EndpointDescription @Nullable [] keyServices) {

    List<EndpointDescription> services = nonNullElements(keyServices);

    boolean groupAbsent = nullOrEmpty(securityGroupId);
    boolean servicesAbsent = services.isEmpty();

    if (mode == null
        || mode == MessageSecurityMode.Invalid
        || (mode == MessageSecurityMode.None && groupAbsent && servicesAbsent)) {
      return null;
    }

    MessageSecurityConfig.Builder builder = MessageSecurityConfig.builder();
    builder.mode(mode);
    if (!groupAbsent) {
      builder.securityGroup(resolveSecurityGroupRef(securityGroupId));
    }
    if (!servicesAbsent) {
      builder.keyServices(services);
    }
    return builder.build();
  }

  /**
   * Resolve a wire SecurityGroupId back to a {@link SecurityGroupRef} naming the matching
   * SecurityGroup config; falls back to a ref carrying the id itself, which fails final validation
   * if it does not name a configured group.
   */
  private SecurityGroupRef resolveSecurityGroupRef(String securityGroupId) {
    return securityGroups.stream()
        .filter(group -> group.getSecurityGroupId().equals(securityGroupId))
        .findFirst()
        .map(SecurityGroupConfig::ref)
        .orElse(new SecurityGroupRef(securityGroupId));
  }

  /**
   * Build typed {@link BrokerTransportSettings} from a Broker*TransportDataType's fields; the
   * all-default shape maps to absent (null).
   */
  private static @Nullable BrokerTransportSettings brokerTransport(
      @Nullable String queueName,
      @Nullable String resourceUri,
      @Nullable String authenticationProfileUri,
      @Nullable BrokerTransportQualityOfService requestedDeliveryGuarantee,
      @Nullable String metaDataQueueName,
      @Nullable Double metaDataUpdateTime) {

    boolean empty =
        queueName == null
            && resourceUri == null
            && authenticationProfileUri == null
            && (requestedDeliveryGuarantee == null
                || requestedDeliveryGuarantee == BrokerTransportQualityOfService.NotSpecified)
            && metaDataQueueName == null
            && isZero(metaDataUpdateTime);

    if (empty) {
      return null;
    }

    BrokerTransportSettings.Builder builder = BrokerTransportSettings.builder();
    if (queueName != null) {
      builder.queueName(queueName);
    }
    if (resourceUri != null) {
      builder.resourceUri(resourceUri);
    }
    if (authenticationProfileUri != null) {
      builder.authenticationProfileUri(authenticationProfileUri);
    }
    if (requestedDeliveryGuarantee != null) {
      builder.requestedDeliveryGuarantee(requestedDeliveryGuarantee);
    }
    if (metaDataQueueName != null) {
      builder.metaDataQueueName(metaDataQueueName);
    }
    Duration updateTime = fromMillis(metaDataUpdateTime);
    if (!updateTime.isZero()) {
      builder.metaDataUpdateTime(updateTime);
    }
    return builder.build();
  }

  // endregion
}

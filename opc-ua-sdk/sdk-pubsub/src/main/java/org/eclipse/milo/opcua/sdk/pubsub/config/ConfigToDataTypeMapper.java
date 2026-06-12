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

import static org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfigMapperUtil.NULL_UUID;
import static org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfigMapperUtil.PROFILE_MQTT_JSON;
import static org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfigMapperUtil.PROFILE_MQTT_UADP;
import static org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfigMapperUtil.PROFILE_UDP_UADP;
import static org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfigMapperUtil.decodeRaw;
import static org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfigMapperUtil.deriveBuiltInType;
import static org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfigMapperUtil.emptyMetaData;
import static org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfigMapperUtil.newEncodingContext;
import static org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfigMapperUtil.toKeyValuePairs;
import static org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfigMapperUtil.toMillis;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrokerTransportQualityOfService;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.BrokerConnectionTransportDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.BrokerDataSetReaderTransportDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.BrokerDataSetWriterTransportDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.BrokerWriterGroupTransportDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.ConnectionTransportDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetFieldContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetFieldFlags;
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
import org.eclipse.milo.opcua.stack.core.types.structured.FieldMetaData;
import org.eclipse.milo.opcua.stack.core.types.structured.FieldTargetDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.JsonDataSetReaderMessageDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.JsonDataSetWriterMessageDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.JsonWriterGroupMessageDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.NetworkAddressUrlDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfiguration2DataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConnectionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishedDataItemsDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishedDataSetDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishedVariableDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.ReaderGroupDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.ReaderGroupMessageDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.ReaderGroupTransportDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.SecurityGroupDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.StandaloneSubscribedDataSetDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.StandaloneSubscribedDataSetRefDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.SubscribedDataSetDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.TargetVariablesDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpDataSetReaderMessageDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpDataSetWriterMessageDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpWriterGroupMessageDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.WriterGroupDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.WriterGroupMessageDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.WriterGroupTransportDataType;
import org.jspecify.annotations.Nullable;

/**
 * Maps an authored {@link PubSubConfig} to its Part 14 {@link PubSubConfiguration2DataType}
 * representation. See {@link PubSubConfigMapper} for the mapping contract, normalizations, and
 * documented losses.
 */
final class ConfigToDataTypeMapper {

  private final PubSubConfig config;
  private final NamespaceTable namespaceTable;
  private final EncodingContext encodingContext;

  private ConfigToDataTypeMapper(PubSubConfig config, NamespaceTable namespaceTable) {
    this.config = config;
    this.namespaceTable = namespaceTable;
    this.encodingContext = newEncodingContext(namespaceTable);
  }

  static PubSubConfiguration2DataType map(PubSubConfig config, NamespaceTable namespaceTable) {
    return new ConfigToDataTypeMapper(config, namespaceTable).map();
  }

  private PubSubConfiguration2DataType map() {
    PublishedDataSetDataType[] publishedDataSets =
        config.publishedDataSets().stream()
            .map(this::mapPublishedDataSet)
            .toArray(PublishedDataSetDataType[]::new);

    PubSubConnectionDataType[] connections =
        config.connections().stream()
            .map(this::mapConnection)
            .toArray(PubSubConnectionDataType[]::new);

    StandaloneSubscribedDataSetDataType[] subscribedDataSets =
        config.standaloneSubscribedDataSets().stream()
            .map(this::mapStandaloneSubscribedDataSet)
            .toArray(StandaloneSubscribedDataSetDataType[]::new);

    SecurityGroupDataType[] securityGroups =
        config.securityGroups().stream()
            .map(ConfigToDataTypeMapper::mapSecurityGroup)
            .toArray(SecurityGroupDataType[]::new);

    return new PubSubConfiguration2DataType(
        publishedDataSets.length == 0 ? null : publishedDataSets,
        connections.length == 0 ? null : connections,
        config.isEnabled(),
        subscribedDataSets.length == 0 ? null : subscribedDataSets,
        null,
        null,
        securityGroups.length == 0 ? null : securityGroups,
        null,
        uint(0),
        toKeyValuePairs(config.properties()));
  }

  // region Connections

  private PubSubConnectionDataType mapConnection(PubSubConnectionConfig connection) {
    String path = "connection '%s'".formatted(connection.name());

    String transportProfileUri;
    NetworkAddressUrlDataType address;
    ConnectionTransportDataType transportSettings;

    if (connection instanceof UdpConnectionConfig udp) {
      transportProfileUri = PROFILE_UDP_UADP;

      UdpDatagramAddress udpAddress = udp.getAddress();
      address = new NetworkAddressUrlDataType(udpAddress.networkInterface(), udpAddress.url());

      ExtensionObject raw = udp.rawTransportSettings();
      if (raw != null) {
        transportSettings =
            decodeRaw(raw, ConnectionTransportDataType.class, encodingContext, path);
      } else {
        UdpDatagramAddress discoveryAddress = udp.getDiscoveryAddress();
        NetworkAddressUrlDataType discoveryUrl =
            discoveryAddress != null
                ? new NetworkAddressUrlDataType(
                    discoveryAddress.networkInterface(), discoveryAddress.url())
                : new NetworkAddressUrlDataType(null, null);
        transportSettings = new DatagramConnectionTransportDataType(discoveryUrl);
      }
    } else if (connection instanceof MqttConnectionConfig mqtt) {
      transportProfileUri = usesJsonMapping(mqtt) ? PROFILE_MQTT_JSON : PROFILE_MQTT_UADP;

      // BrokerSecurityConfig is intentionally NOT mapped; see PubSubConfigMapper.
      address = new NetworkAddressUrlDataType(null, mqtt.getBrokerUri().toString());

      ExtensionObject raw = mqtt.rawTransportSettings();
      if (raw != null) {
        transportSettings =
            decodeRaw(raw, ConnectionTransportDataType.class, encodingContext, path);
      } else {
        transportSettings = new BrokerConnectionTransportDataType(null, null);
      }
    } else {
      throw new PubSubConfigValidationException(
          path + ": unsupported connection type " + connection.getClass().getName());
    }

    boolean udp = connection instanceof UdpConnectionConfig;

    WriterGroupDataType[] writerGroups =
        connection.writerGroups().stream()
            .map(group -> mapWriterGroup(group, udp, path))
            .toArray(WriterGroupDataType[]::new);

    ReaderGroupDataType[] readerGroups =
        connection.readerGroups().stream()
            .map(group -> mapReaderGroup(group, udp, path))
            .toArray(ReaderGroupDataType[]::new);

    PublisherId publisherId = connection.publisherId();

    return new PubSubConnectionDataType(
        connection.name(),
        connection.enabled(),
        publisherId != null ? publisherId.toVariant() : Variant.ofNull(),
        transportProfileUri,
        address,
        toKeyValuePairs(connection.properties()),
        transportSettings,
        writerGroups.length == 0 ? null : writerGroups,
        readerGroups.length == 0 ? null : readerGroups);
  }

  /** A connection maps to the MQTT+JSON profile if any of its groups use JSON settings. */
  private static boolean usesJsonMapping(MqttConnectionConfig connection) {
    for (WriterGroupConfig group : connection.writerGroups()) {
      if (group.getMessageSettings() instanceof JsonWriterGroupSettings) {
        return true;
      }
      for (DataSetWriterConfig writer : group.getDataSetWriters()) {
        if (writer.getSettings() instanceof JsonDataSetWriterSettings) {
          return true;
        }
      }
    }
    for (ReaderGroupConfig group : connection.readerGroups()) {
      for (DataSetReaderConfig reader : group.getDataSetReaders()) {
        if (reader.getSettings() instanceof JsonDataSetReaderSettings) {
          return true;
        }
      }
    }
    return false;
  }

  // endregion

  // region Writer groups and dataset writers

  private WriterGroupDataType mapWriterGroup(
      WriterGroupConfig group, boolean udp, String parentPath) {

    String path = parentPath + " writerGroup '%s'".formatted(group.getName());

    WriterGroupTransportDataType transportSettings;
    ExtensionObject rawTransport = group.getRawTransportSettings();
    BrokerTransportSettings brokerTransport = group.getBrokerTransport();
    if (rawTransport != null) {
      transportSettings =
          decodeRaw(rawTransport, WriterGroupTransportDataType.class, encodingContext, path);
    } else if (brokerTransport != null) {
      transportSettings =
          new BrokerWriterGroupTransportDataType(
              brokerTransport.getQueueName(),
              brokerTransport.getResourceUri(),
              brokerTransport.getAuthenticationProfileUri(),
              brokerTransport.getRequestedDeliveryGuarantee());
    } else if (udp) {
      transportSettings = new DatagramWriterGroupTransportDataType(ubyte(0), 0.0);
    } else {
      transportSettings =
          new BrokerWriterGroupTransportDataType(
              null, null, null, BrokerTransportQualityOfService.NotSpecified);
    }

    WriterGroupMessageDataType messageSettings;
    ExtensionObject rawMessage = group.getRawMessageSettings();
    if (rawMessage != null) {
      messageSettings =
          decodeRaw(rawMessage, WriterGroupMessageDataType.class, encodingContext, path);
    } else if (group.getMessageSettings() instanceof UadpWriterGroupSettings uadp) {
      messageSettings =
          new UadpWriterGroupMessageDataType(
              uadp.getGroupVersion(),
              uadp.getDataSetOrdering(),
              uadp.getNetworkMessageContentMask(),
              0.0,
              null);
    } else if (group.getMessageSettings() instanceof JsonWriterGroupSettings json) {
      messageSettings = new JsonWriterGroupMessageDataType(json.getNetworkMessageContentMask());
    } else {
      throw new PubSubConfigValidationException(
          path + ": unsupported message settings " + group.getMessageSettings().getClass());
    }

    DataSetWriterDataType[] dataSetWriters =
        group.getDataSetWriters().stream()
            .map(writer -> mapDataSetWriter(writer, udp, path))
            .toArray(DataSetWriterDataType[]::new);

    MessageSecurityConfig security = group.getMessageSecurity();
    Duration keepAliveTime = group.getKeepAliveTime();

    return new WriterGroupDataType(
        group.getName(),
        group.isEnabled(),
        securityMode(security),
        securityGroupId(security),
        keyServices(security),
        group.getMaxNetworkMessageSize(),
        toKeyValuePairs(group.getProperties()),
        group.getWriterGroupId(),
        toMillis(group.getPublishingInterval()),
        keepAliveTime != null ? toMillis(keepAliveTime) : 0.0,
        group.getPriority(),
        null,
        null,
        transportSettings,
        messageSettings,
        dataSetWriters.length == 0 ? null : dataSetWriters);
  }

  private DataSetWriterDataType mapDataSetWriter(
      DataSetWriterConfig writer, boolean udp, String parentPath) {

    String path = parentPath + " dataSetWriter '%s'".formatted(writer.getName());

    @Nullable DataSetWriterTransportDataType transportSettings;
    ExtensionObject rawTransport = writer.getRawTransportSettings();
    BrokerTransportSettings brokerTransport = writer.getBrokerTransport();
    if (rawTransport != null) {
      transportSettings =
          decodeRaw(rawTransport, DataSetWriterTransportDataType.class, encodingContext, path);
    } else if (brokerTransport != null) {
      transportSettings =
          new BrokerDataSetWriterTransportDataType(
              brokerTransport.getQueueName(),
              brokerTransport.getResourceUri(),
              brokerTransport.getAuthenticationProfileUri(),
              brokerTransport.getRequestedDeliveryGuarantee(),
              brokerTransport.getMetaDataQueueName(),
              toMillis(brokerTransport.getMetaDataUpdateTime()));
    } else if (udp) {
      // Part 14 defines no datagram DataSetWriter transport type.
      transportSettings = null;
    } else {
      transportSettings =
          new BrokerDataSetWriterTransportDataType(
              null, null, null, BrokerTransportQualityOfService.NotSpecified, null, 0.0);
    }

    DataSetWriterMessageDataType messageSettings;
    ExtensionObject rawMessage = writer.getRawMessageSettings();
    if (rawMessage != null) {
      messageSettings =
          decodeRaw(rawMessage, DataSetWriterMessageDataType.class, encodingContext, path);
    } else if (writer.getSettings() instanceof UadpDataSetWriterSettings uadp) {
      messageSettings =
          new UadpDataSetWriterMessageDataType(
              uadp.getDataSetMessageContentMask(),
              uadp.getConfiguredSize(),
              uadp.getNetworkMessageNumber(),
              uadp.getDataSetOffset());
    } else if (writer.getSettings() instanceof JsonDataSetWriterSettings json) {
      messageSettings = new JsonDataSetWriterMessageDataType(json.getDataSetMessageContentMask());
    } else {
      throw new PubSubConfigValidationException(
          path + ": unsupported message settings " + writer.getSettings().getClass());
    }

    return new DataSetWriterDataType(
        writer.getName(),
        writer.isEnabled(),
        writer.getDataSetWriterId(),
        writer.getFieldContentMask(),
        writer.getKeyFrameCount(),
        writer.getDataSet().name(),
        toKeyValuePairs(writer.getProperties()),
        transportSettings,
        messageSettings);
  }

  // endregion

  // region Reader groups and dataset readers

  private ReaderGroupDataType mapReaderGroup(
      ReaderGroupConfig group, boolean udp, String parentPath) {

    String path = parentPath + " readerGroup '%s'".formatted(group.getName());

    @Nullable ReaderGroupTransportDataType transportSettings = null;
    ExtensionObject rawTransport = group.getRawTransportSettings();
    if (rawTransport != null) {
      transportSettings =
          decodeRaw(rawTransport, ReaderGroupTransportDataType.class, encodingContext, path);
    }

    @Nullable ReaderGroupMessageDataType messageSettings = null;
    ExtensionObject rawMessage = group.getRawMessageSettings();
    if (rawMessage != null) {
      messageSettings =
          decodeRaw(rawMessage, ReaderGroupMessageDataType.class, encodingContext, path);
    }

    DataSetReaderDataType[] dataSetReaders =
        group.getDataSetReaders().stream()
            .map(reader -> mapDataSetReader(reader, udp, path))
            .toArray(DataSetReaderDataType[]::new);

    MessageSecurityConfig security = group.getMessageSecurity();

    return new ReaderGroupDataType(
        group.getName(),
        group.isEnabled(),
        securityMode(security),
        securityGroupId(security),
        keyServices(security),
        group.getMaxNetworkMessageSize(),
        toKeyValuePairs(group.getProperties()),
        transportSettings,
        messageSettings,
        dataSetReaders.length == 0 ? null : dataSetReaders);
  }

  private DataSetReaderDataType mapDataSetReader(
      DataSetReaderConfig reader, boolean udp, String parentPath) {

    String path = parentPath + " dataSetReader '%s'".formatted(reader.getName());

    @Nullable DataSetReaderTransportDataType transportSettings;
    ExtensionObject rawTransport = reader.getRawTransportSettings();
    BrokerTransportSettings brokerTransport = reader.getBrokerTransport();
    if (rawTransport != null) {
      transportSettings =
          decodeRaw(rawTransport, DataSetReaderTransportDataType.class, encodingContext, path);
    } else if (brokerTransport != null) {
      transportSettings =
          new BrokerDataSetReaderTransportDataType(
              brokerTransport.getQueueName(),
              brokerTransport.getResourceUri(),
              brokerTransport.getAuthenticationProfileUri(),
              brokerTransport.getRequestedDeliveryGuarantee(),
              brokerTransport.getMetaDataQueueName());
    } else if (udp) {
      // The reader inherits the connection address; no per-reader datagram settings are authored.
      transportSettings = null;
    } else {
      transportSettings =
          new BrokerDataSetReaderTransportDataType(
              null, null, null, BrokerTransportQualityOfService.NotSpecified, null);
    }

    DataSetReaderMessageDataType messageSettings;
    ExtensionObject rawMessage = reader.getRawMessageSettings();
    if (rawMessage != null) {
      messageSettings =
          decodeRaw(rawMessage, DataSetReaderMessageDataType.class, encodingContext, path);
    } else if (reader.getSettings() instanceof UadpDataSetReaderSettings uadp) {
      messageSettings =
          new UadpDataSetReaderMessageDataType(
              uadp.getGroupVersion(),
              uadp.getNetworkMessageNumber(),
              uadp.getDataSetOffset(),
              NULL_UUID,
              uadp.getNetworkMessageContentMask(),
              uadp.getDataSetMessageContentMask(),
              0.0,
              0.0,
              0.0);
    } else if (reader.getSettings() instanceof JsonDataSetReaderSettings json) {
      messageSettings =
          new JsonDataSetReaderMessageDataType(
              json.getNetworkMessageContentMask(), json.getDataSetMessageContentMask());
    } else {
      throw new PubSubConfigValidationException(
          path + ": unsupported message settings " + reader.getSettings().getClass());
    }

    DataSetMetaDataConfig metaData = reader.getDataSetMetaData();

    SubscribedDataSetDataType subscribedDataSet;
    SubscribedDataSetSpec spec = reader.getSubscribedDataSet();
    if (spec == null) {
      subscribedDataSet = new TargetVariablesDataType(new FieldTargetDataType[0]);
    } else if (spec instanceof TargetVariablesConfig targetVariables) {
      subscribedDataSet = mapTargetVariables(targetVariables, metaData, path);
    } else if (spec instanceof StandaloneSubscribedDataSetRef ref) {
      subscribedDataSet = new StandaloneSubscribedDataSetRefDataType(ref.name());
    } else {
      throw new PubSubConfigValidationException(
          path + ": unsupported subscribed dataset spec " + spec.getClass());
    }

    PublisherId publisherId = reader.getPublisherId();

    return new DataSetReaderDataType(
        reader.getName(),
        reader.isEnabled(),
        publisherId != null ? publisherId.toVariant() : Variant.ofNull(),
        reader.getWriterGroupId() != null ? reader.getWriterGroupId() : ushort(0),
        reader.getDataSetWriterId() != null ? reader.getDataSetWriterId() : ushort(0),
        metaData != null ? mapMetaData(metaData) : emptyMetaData(),
        DataSetFieldContentMask.of(),
        toMillis(reader.getMessageReceiveTimeout()),
        reader.getKeyFrameCount(),
        null,
        MessageSecurityMode.None,
        null,
        null,
        toKeyValuePairs(reader.getProperties()),
        transportSettings,
        messageSettings,
        subscribedDataSet);
  }

  private TargetVariablesDataType mapTargetVariables(
      TargetVariablesConfig targetVariables,
      @Nullable DataSetMetaDataConfig metaData,
      String path) {

    List<FieldTargetDataType> targets = new ArrayList<>(targetVariables.getMappings().size());

    for (TargetVariablesConfig.Mapping mapping : targetVariables.getMappings()) {
      UUID dataSetFieldId = resolveSelector(mapping.selector(), metaData, path);

      TargetVariableConfig target = mapping.target();
      NodeFieldAddress targetAddress = target.getTarget();

      NodeId targetNodeId =
          targetAddress
              .nodeId()
              .toNodeId(namespaceTable)
              .orElseThrow(
                  () ->
                      new PubSubConfigValidationException(
                          "%s: target node %s: namespace not present in the namespace table"
                              .formatted(path, targetAddress.nodeId())));

      targets.add(
          new FieldTargetDataType(
              dataSetFieldId,
              target.getReceiverIndexRange().orElse(null),
              targetNodeId,
              targetAddress.attributeId().uid(),
              target.getWriteIndexRange().orElse(null),
              target.getOverrideHandling(),
              target.getOverrideValue().orElse(Variant.ofNull())));
    }

    return new TargetVariablesDataType(targets.toArray(new FieldTargetDataType[0]));
  }

  /**
   * Resolve a {@link FieldSelector} to the DataSetFieldId carried by {@link FieldTargetDataType};
   * name and index selectors are resolved against the configured metadata.
   */
  private static UUID resolveSelector(
      FieldSelector selector, @Nullable DataSetMetaDataConfig metaData, String path) {

    if (selector instanceof FieldIdSelector byId) {
      return byId.fieldId();
    }

    if (metaData == null) {
      throw new PubSubConfigValidationException(
          "%s: %s requires configured DataSetMetaData to resolve the field id"
              .formatted(path, selector.getClass().getSimpleName()));
    }

    if (selector instanceof FieldNameSelector byName) {
      return metaData.fields().stream()
          .filter(field -> field.name().equals(byName.fieldName()))
          .findFirst()
          .map(DataSetMetaDataConfig.Field::dataSetFieldId)
          .orElseThrow(
              () ->
                  new PubSubConfigValidationException(
                      "%s: no metadata field named '%s'".formatted(path, byName.fieldName())));
    } else if (selector instanceof FieldIndexSelector byIndex) {
      List<DataSetMetaDataConfig.Field> fields = metaData.fields();
      if (byIndex.index() >= fields.size()) {
        throw new PubSubConfigValidationException(
            "%s: metadata field index %d out of bounds (%d fields)"
                .formatted(path, byIndex.index(), fields.size()));
      }
      return fields.get(byIndex.index()).dataSetFieldId();
    } else {
      throw new PubSubConfigValidationException(
          path + ": unsupported field selector " + selector.getClass());
    }
  }

  // endregion

  // region Published datasets, standalone subscribed datasets, security groups

  private PublishedDataSetDataType mapPublishedDataSet(PublishedDataSetConfig dataSet) {
    String path = "publishedDataSet '%s'".formatted(dataSet.getName());

    List<FieldDefinition> fields = dataSet.getFields();
    PublishedVariableDataType[] publishedData = new PublishedVariableDataType[fields.size()];

    for (int i = 0; i < fields.size(); i++) {
      FieldDefinition field = fields.get(i);

      NodeId publishedVariable;
      UInteger attributeId;

      FieldAddress source = field.getSource();
      if (source instanceof KeyFieldAddress) {
        publishedVariable = NodeId.NULL_VALUE;
        attributeId = AttributeId.Value.uid();
      } else if (source instanceof NodeFieldAddress node) {
        publishedVariable =
            node.nodeId()
                .toNodeId(namespaceTable)
                .orElseThrow(
                    () ->
                        new PubSubConfigValidationException(
                            "%s field '%s': source node %s: namespace not present in the namespace table"
                                .formatted(path, field.getName(), node.nodeId())));
        attributeId = node.attributeId().uid();
      } else {
        throw new PubSubConfigValidationException(
            "%s field '%s': unsupported field address %s"
                .formatted(path, field.getName(), source.getClass()));
      }

      publishedData[i] =
          new PublishedVariableDataType(
              publishedVariable, attributeId, 0.0, uint(0), 0.0, null, Variant.ofNull(), null);
    }

    // KeyFieldAddress keys ride along in FieldMetaData properties (MiloSourceKey); the discovery
    // responder derives announcement metadata through the same seam with stripMiloSourceKey=true.
    DataSetMetaDataType metaData = DataSetMetaDataMapper.toDataSetMetaDataType(dataSet, false);

    return new PublishedDataSetDataType(
        dataSet.getName(),
        null,
        metaData,
        toKeyValuePairs(dataSet.getProperties()),
        new PublishedDataItemsDataType(publishedData));
  }

  private StandaloneSubscribedDataSetDataType mapStandaloneSubscribedDataSet(
      StandaloneSubscribedDataSetConfig dataSet) {

    String path = "standaloneSubscribedDataSet '%s'".formatted(dataSet.getName());

    SubscribedDataSetDataType subscribedDataSet;
    SubscribedDataSetSpec spec = dataSet.getSubscribedDataSet();
    if (spec instanceof TargetVariablesConfig targetVariables) {
      subscribedDataSet = mapTargetVariables(targetVariables, dataSet.getMetaData(), path);
    } else if (spec instanceof StandaloneSubscribedDataSetRef ref) {
      subscribedDataSet = new StandaloneSubscribedDataSetRefDataType(ref.name());
    } else {
      throw new PubSubConfigValidationException(
          path + ": unsupported subscribed dataset spec " + spec.getClass());
    }

    return new StandaloneSubscribedDataSetDataType(
        dataSet.getName(), null, mapMetaData(dataSet.getMetaData()), subscribedDataSet);
  }

  private static DataSetMetaDataType mapMetaData(DataSetMetaDataConfig metaData) {
    FieldMetaData[] fields =
        metaData.fields().stream()
            .map(
                field ->
                    new FieldMetaData(
                        field.name(),
                        LocalizedText.NULL_VALUE,
                        DataSetFieldFlags.of(),
                        deriveBuiltInType(field.dataTypeId()),
                        field.dataTypeId(),
                        field.valueRank(),
                        field.arrayDimensions(),
                        uint(0),
                        field.dataSetFieldId(),
                        null))
            .toArray(FieldMetaData[]::new);

    return new DataSetMetaDataType(
        null,
        null,
        null,
        null,
        metaData.getName(),
        LocalizedText.NULL_VALUE,
        fields.length == 0 ? null : fields,
        metaData.getDataSetClassId().orElse(NULL_UUID),
        new ConfigurationVersionDataType(
            metaData.getConfigurationVersionMajor(), metaData.getConfigurationVersionMinor()));
  }

  private static SecurityGroupDataType mapSecurityGroup(SecurityGroupConfig securityGroup) {
    return new SecurityGroupDataType(
        securityGroup.getName(),
        null,
        toMillis(securityGroup.getKeyLifeTime()),
        securityGroup.getSecurityPolicyUri(),
        securityGroup.getMaxFutureKeyCount(),
        securityGroup.getMaxPastKeyCount(),
        securityGroup.getSecurityGroupId(),
        null,
        toKeyValuePairs(securityGroup.getProperties()));
  }

  // endregion

  // region Message security

  private static MessageSecurityMode securityMode(@Nullable MessageSecurityConfig security) {
    return security != null ? security.getMode() : MessageSecurityMode.None;
  }

  /**
   * Resolve the wire SecurityGroupId for a group's security settings: the referenced
   * SecurityGroup's id (which may differ from its config name), or the ref name itself if the
   * reference is dangling.
   */
  private @Nullable String securityGroupId(@Nullable MessageSecurityConfig security) {
    if (security == null) {
      return null;
    }
    SecurityGroupRef ref = security.getSecurityGroup();
    if (ref == null) {
      return null;
    }
    return config.securityGroups().stream()
        .filter(group -> group.getName().equals(ref.name()))
        .findFirst()
        .map(SecurityGroupConfig::getSecurityGroupId)
        .orElse(ref.name());
  }

  private static EndpointDescription @Nullable [] keyServices(
      @Nullable MessageSecurityConfig security) {

    if (security == null || security.getKeyServices().isEmpty()) {
      return null;
    }
    return security.getKeyServices().toArray(new EndpointDescription[0]);
  }

  // endregion
}

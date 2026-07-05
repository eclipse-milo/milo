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

import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfiguration2DataType;

/**
 * Maps between {@link PubSubConfig} and the Part 14 {@link PubSubConfiguration2DataType}.
 *
 * <p>Exposed only through {@link PubSubConfig#toDataType(NamespaceTable)} and {@link
 * PubSubConfig#fromDataType(PubSubConfiguration2DataType, NamespaceTable)}.
 *
 * <h2>Mapping contract</h2>
 *
 * <p>Both directions are total over the v1 config surface: every authored config field lands in the
 * datatype and is restored by the reverse mapping, subject to the documented losses and
 * normalizations below. {@code fromDataType} tolerates null arrays and null boxed scalars in
 * generated datatype instances and preserves settings structures it has no typed config slot for in
 * the raw escape hatches ({@code rawTransportSettings} / {@code rawMessageSettings}) instead of
 * failing.
 *
 * <h3>Key conversions</h3>
 *
 * <ul>
 *   <li>{@code java.time.Duration} ↔ spec Duration (Double milliseconds); on the way back, null,
 *       NaN, and non-positive values map to {@link java.time.Duration#ZERO} / "not configured".
 *   <li>{@link PublisherId} ↔ Variant via {@link PublisherId#toVariant()} / {@link
 *       PublisherId#fromVariant}; a null Variant value means no publisher id.
 *   <li>{@link NodeFieldAddress} ({@code ExpandedNodeId}) ↔ {@code NodeId} via the supplied {@link
 *       NamespaceTable}; an unresolvable namespace URI or index throws {@link
 *       PubSubConfigValidationException}.
 *   <li>{@link KeyFieldAddress} round-trips through {@code FieldMetaData.properties} under the
 *       reserved name {@code 0:MiloSourceKey}, with {@code publishedVariable = NodeId.NULL_VALUE};
 *       that property name must not be used for user field properties.
 *   <li>{@link PublishedEventsConfig} ↔ {@code PublishedEventsDataType}: the {@code eventNotifier}
 *       ({@code ExpandedNodeId}, canonically the namespace-URI form produced by {@code
 *       NodeId.expanded(NamespaceTable)}, as for {@link NodeFieldAddress}) ↔ local {@code NodeId}
 *       via the supplied {@link NamespaceTable}; an unresolvable namespace URI or index throws
 *       {@link PubSubConfigValidationException}. Event fields zip {@code FieldMetaData} with the
 *       {@code selectedFields} operand at the same index; a length mismatch between {@code
 *       selectedFields} and the metadata fields is rejected. NodeIds inside selected-field operands
 *       (e.g. {@code SimpleAttributeOperand.typeDefinitionId}) and ContentFilter operands are
 *       carried verbatim and NOT remapped through the {@link NamespaceTable}, so filters and
 *       selected fields referencing namespace indices other than 0 are only portable between peers
 *       with identical namespace tables.
 *   <li>{@code DataSetWriterConfig.eventQueueCapacity} round-trips through {@code
 *       DataSetWriterProperties} under the reserved name {@code 0:MiloEventQueueCapacity} (an Int32
 *       Variant); that property name must not be used for user writer properties. {@code
 *       fromDataType} consumes the pair wherever it sits — it never surfaces in the config's writer
 *       properties — and rejects values below 1 and non-integer Variants; {@code toDataType} emits
 *       the pair only when the capacity differs from the default (100).
 *   <li>Transport profile URIs are derived, not stored: UDP connections map to {@code
 *       .../pubsub-udp-uadp}; MQTT connections map to {@code .../pubsub-mqtt-json} if any of their
 *       groups, writers, or readers use JSON message settings, else {@code .../pubsub-mqtt-uadp}.
 *   <li>{@code FieldMetaData.builtInType} is derived from the field's DataType NodeId (builtin
 *       types map to their builtin type id, everything else to ExtensionObject/22) and is not
 *       consumed by {@code fromDataType}.
 *   <li>Wire {@code SecurityGroupId}s are the referenced {@link SecurityGroupConfig}'s {@code
 *       securityGroupId} (not its config name); {@code fromDataType} resolves them back to the
 *       matching group by id.
 *   <li>{@code UdpConnectionConfig.discoveryAddress} ↔ {@code
 *       DatagramConnectionTransportDataType.discoveryAddress} as a {@code
 *       NetworkAddressUrlDataType} carrying the address URL and network interface. {@code
 *       fromDataType} maps an exact {@code DatagramConnectionTransportDataType} carrying a URL-form
 *       discovery address back to the typed slot; subtypes (e.g. {@code
 *       DatagramConnectionTransport2DataType}) and other shapes are preserved in the raw escape
 *       hatch.
 * </ul>
 *
 * <h3>Raw escape hatches</h3>
 *
 * <p>When a raw settings ExtensionObject is set it wins over the typed settings, which are then not
 * mapped and do not survive a round trip. Raw ExtensionObjects are decoded into the strongly-typed
 * datatype slots using the shared OPC UA (namespace 0) codecs; vendor-defined settings types
 * without registered codecs are rejected with {@link PubSubConfigValidationException}. In the
 * reverse direction, settings structures that have no typed config representation (unknown
 * subtypes, reader group transport/message settings, or typed shapes carrying values without config
 * slots) are re-encoded (OPC UA Binary) into the raw escape hatches.
 *
 * <h3>Normalizations (round-trip safe but shape-changing)</h3>
 *
 * <ul>
 *   <li>Absent transport settings are emitted as canonical empty structures where Part 14 defines
 *       one (empty {@code DatagramConnectionTransportDataType} / {@code
 *       DatagramWriterGroupTransportDataType} on UDP, empty {@code Broker*TransportDataType} on
 *       MQTT) and those empty shapes map back to "absent"; consequently an authored all-default
 *       {@link BrokerTransportSettings} is indistinguishable from none. A UDP connection with a
 *       configured discovery address is the exception: its {@code
 *       DatagramConnectionTransportDataType} carries that address instead of the empty shape.
 *       DataSetWriter and DataSetReader transport settings on UDP connections are emitted as null
 *       (Part 14 defines no datagram type at the writer level and the reader inherits the
 *       connection address).
 *   <li>A reader with no subscribed dataset is emitted as an empty {@code TargetVariablesDataType};
 *       an empty one maps back to "absent", so an authored empty {@link TargetVariablesConfig} on a
 *       reader does not survive a round trip (it does on a standalone subscribed dataset, where the
 *       spec is mandatory).
 *   <li>{@link FieldSelector} name and index selectors are resolved against the configured metadata
 *       when mapping to {@code FieldTargetDataType} and come back as id selectors.
 *   <li>A reader with no configured metadata is emitted with an empty {@code DataSetMetaDataType}
 *       (no name, no fields, zero class id, version 0.0), which maps back to "absent".
 *   <li>Null boxed scalars, enums, and option set masks in hand-built datatypes are normalized to
 *       their config defaults (0, {@code Undefined}, {@code NotSpecified}, empty mask, enabled).
 *   <li>Reader-level all-default security — {@code securityMode} {@code Invalid} (the Part 14
 *       §6.2.9.9 no-override sentinel), or the legacy {@code None} with no group id and no key
 *       services emitted by earlier Milo versions — maps to an absent reader override; an absent
 *       override is emitted as {@code Invalid}/null/null. A {@code None} override <em>with</em> a
 *       group id or key services survives as an active override.
 *   <li>A dataset writer whose dataset has a {@link PublishedEventsConfig} source is emitted with
 *       {@code KeyFrameCount = 0} regardless of the configured value (event datasets are
 *       non-cyclic, Part 14 §6.2.4.3), so the configured key frame count of an event writer does
 *       not survive a round trip.
 *   <li>An explicit wire {@code 0:MiloEventQueueCapacity} equal to the default (100) imports
 *       cleanly but is dropped on re-export, and the pair's position is canonicalized: it is
 *       emitted after the writer's own properties.
 *   <li>A published events source with zero fields is emitted with an empty (non-null) {@code
 *       selectedFields} array, matching the empty {@code publishedData} sibling convention; an
 *       incoming null {@code selectedFields} array is tolerated only when the metadata has zero
 *       fields and normalizes to the empty array on re-export. A non-null {@code filter} is always
 *       emitted; an incoming null filter maps to the canonical empty {@code ContentFilter}.
 * </ul>
 *
 * <h3>Documented losses</h3>
 *
 * <p>Config fields with no Part 14 slot (dropped by {@code toDataType}, restored as defaults):
 *
 * <ul>
 *   <li>{@link MqttConnectionConfig#getBrokerSecurity() BrokerSecurityConfig} is intentionally
 *       never mapped: it carries local credentials and key material paths that must not be
 *       serialized into the Part 14 configuration.
 *   <li>{@link MqttConnectionConfig#getPublisherStatusMode() publisherStatusMode} is Milo-local
 *       MQTT runtime policy; restored as {@link PublisherStatusMode#AUTO}.
 *   <li>{@code PublishedDataSetConfig.enabled} (Part 14 has no enabled flag on published datasets;
 *       restored as {@code true}).
 *   <li>{@code DataSetReaderConfig.metadataPolicy} (a Milo-local concept; restored as {@code
 *       REQUIRE_CONFIGURED}).
 *   <li>{@code MessageSecurityConfig.securityPolicyUri} (Part 14 groups carry no policy URI; the
 *       policy comes from the referenced SecurityGroup).
 *   <li>{@code SecurityGroupConfig.keyServices} ({@code SecurityGroupDataType} has no key service
 *       slot; key services round-trip on groups via {@code MessageSecurityConfig.keyServices}).
 *   <li>{@code StandaloneSubscribedDataSetConfig.properties} ({@code
 *       StandaloneSubscribedDataSetDataType} has no KeyValuePair slot).
 *   <li>{@code BrokerTransportSettings.metaDataQueueName} and {@code metaDataUpdateTime} at the
 *       writer group level, and {@code metaDataUpdateTime} at the reader level (the Part 14 broker
 *       transport types at those levels have no such fields; all fields round-trip at the dataset
 *       writer level).
 * </ul>
 *
 * <p>Datatype fields with no config slot (emitted as defaults by {@code toDataType}, dropped by
 * {@code fromDataType}):
 *
 * <ul>
 *   <li>Top level: {@code dataSetClasses}, {@code pubSubKeyPushTargets}, {@code
 *       configurationVersion} (emitted as 0).
 *   <li>Writer group: {@code localeIds}, {@code headerLayoutUri}.
 *   <li>Dataset reader: {@code dataSetFieldContentMask}, {@code headerLayoutUri}.
 *   <li>Published dataset: {@code dataSetFolder}, metadata {@code description}, {@code
 *       dataSetClassId} (emitted as the all-zero Guid), the DataTypeSchemaHeader arrays, and the
 *       {@code PublishedVariableDataType} sampling/deadband/index-range/substitute-value fields
 *       (emitted as defaults). {@code PublishedDataSetConfig.properties} maps to {@code
 *       extensionFields}. Dataset sources other than {@code PublishedDataItems} and {@code
 *       PublishedEvents} are rejected.
 *   <li>Standalone subscribed dataset: {@code dataSetFolder}. {@code SubscribedDataSetMirror} and
 *       inline standalone subscribed datasets are rejected.
 *   <li>MQTT connection address: {@code networkInterface}.
 * </ul>
 */
final class PubSubConfigMapper {

  private PubSubConfigMapper() {}

  /**
   * Map {@code config} to its Part 14 {@link PubSubConfiguration2DataType} representation.
   *
   * @param config the validated {@link PubSubConfig} to map.
   * @param namespaceTable the {@link NamespaceTable} used to convert namespace-URI field addresses
   *     to local NodeIds.
   * @return the {@link PubSubConfiguration2DataType} representation of {@code config}.
   * @throws PubSubConfigValidationException if a namespace URI cannot be resolved, a field selector
   *     cannot be resolved against the configured metadata, or a raw settings ExtensionObject
   *     cannot be decoded into its Part 14 slot.
   */
  static PubSubConfiguration2DataType toDataType(
      PubSubConfig config, NamespaceTable namespaceTable) {

    return ConfigToDataTypeMapper.map(config, namespaceTable);
  }

  /**
   * Map {@code value} to an authored {@link PubSubConfig}.
   *
   * @param value the {@link PubSubConfiguration2DataType} to map.
   * @param namespaceTable the {@link NamespaceTable} used to convert local NodeIds to namespace-URI
   *     field addresses.
   * @return the {@link PubSubConfig} representation of {@code value}.
   * @throws PubSubConfigValidationException if {@code value} does not map to a valid config: a
   *     required name or reference is missing, an element fails builder or cross-element
   *     validation, or an unsupported connection profile, dataset source, or subscribed dataset
   *     type is encountered.
   */
  static PubSubConfig fromDataType(
      PubSubConfiguration2DataType value, NamespaceTable namespaceTable) {

    return DataTypeToConfigMapper.map(value, namespaceTable);
  }
}

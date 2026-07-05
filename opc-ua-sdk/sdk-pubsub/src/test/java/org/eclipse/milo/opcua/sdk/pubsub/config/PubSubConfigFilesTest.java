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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Arrays;
import java.util.UUID;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.binary.OpcUaBinaryDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.binary.OpcUaBinaryEncoder;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.FilterOperator;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilterElement;
import org.eclipse.milo.opcua.stack.core.types.structured.LiteralOperand;
import org.eclipse.milo.opcua.stack.core.types.structured.NetworkAddressUrlDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfiguration2DataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfigurationDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConnectionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.SimpleAttributeOperand;
import org.eclipse.milo.opcua.stack.core.types.structured.UABinaryFileDataType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests for {@link PubSubConfigFiles}: round trips at both API levels, the Table 88 namespaces
 * header binding and its per-level handling (portability vs strict positional match), base-type
 * Body rejection, malformed stream and wrong-Body failure codes, trailing byte tolerance, the
 * validation-failure path, and the write/read Path helpers.
 */
class PubSubConfigFilesTest {

  private static final String URI_1 = "urn:milo:test:ns1";
  private static final String URI_2 = "urn:milo:test:ns2";

  private static final UUID FIELD_TEMP_ID = new UUID(0xA1L, 1L);
  private static final UUID FIELD_COUNT_ID = new UUID(0xA1L, 2L);
  private static final UUID FIELD_EVT_SEVERITY_ID = new UUID(0xA3L, 1L);
  private static final UUID FIELD_EVT_MESSAGE_ID = new UUID(0xA3L, 2L);
  private static final UUID MD_F1_ID = new UUID(0xB1L, 1L);

  private static EncodingContext contextWith(String... uris) {
    var context = new DefaultEncodingContext();
    for (String uri : uris) {
      context.getNamespaceTable().add(uri);
    }
    return context;
  }

  private static ExtensionObject encode(UaStructuredType struct) {
    return ExtensionObject.encode(new DefaultEncodingContext(), struct);
  }

  private static SimpleAttributeOperand baseEventOperand(String browseName) {
    return new SimpleAttributeOperand(
        NodeIds.BaseEventType,
        new QualifiedName[] {new QualifiedName(0, browseName)},
        AttributeId.Value.uid(),
        null);
  }

  private static PublishedDataSetConfig publishedEventsDataSet() {
    return PublishedDataSetConfig.builder("pds-events")
        .source(
            PublishedEventsConfig.builder(ExpandedNodeId.of(URI_1, "Events.Notifier"))
                .field(
                    EventFieldDefinition.builder("severity")
                        .selectedField(baseEventOperand("Severity"))
                        .dataType(NodeIds.UInt16)
                        .dataSetFieldId(FIELD_EVT_SEVERITY_ID)
                        .promoted(true)
                        .build())
                .field(
                    EventFieldDefinition.builder("message")
                        .selectedField(baseEventOperand("Message"))
                        .dataType(NodeIds.LocalizedText)
                        .dataSetFieldId(FIELD_EVT_MESSAGE_ID)
                        .build())
                .filter(
                    new ContentFilter(
                        new ContentFilterElement[] {
                          new ContentFilterElement(
                              FilterOperator.OfType,
                              new ExtensionObject[] {
                                encode(new LiteralOperand(Variant.ofNodeId(NodeIds.BaseEventType)))
                              })
                        }))
                .build())
        .configurationVersion(uint(4), uint(13))
        .build();
  }

  /** A config whose field addresses span two non-zero namespaces (URI-based portability rows). */
  private static PubSubConfig portableConfig() {
    PublishedDataSetConfig pds =
        PublishedDataSetConfig.builder("pds-1")
            .field(
                FieldDefinition.builder("temperature")
                    .source(
                        new NodeFieldAddress(
                            ExpandedNodeId.of(URI_1, "Sensor.Temperature"), AttributeId.Value))
                    .dataType(NodeIds.Double)
                    .dataSetFieldId(FIELD_TEMP_ID)
                    .build())
            .field(
                FieldDefinition.builder("count")
                    .source(
                        new NodeFieldAddress(
                            ExpandedNodeId.of(URI_2, "Sensor.Count"), AttributeId.Value))
                    .dataType(NodeIds.Int32)
                    .dataSetFieldId(FIELD_COUNT_ID)
                    .build())
            .build();

    WriterGroupConfig wg =
        WriterGroupConfig.builder("wg-1")
            .writerGroupId(ushort(1))
            .publishingInterval(Duration.ofMillis(250))
            .dataSetWriter(
                DataSetWriterConfig.builder("dsw-1")
                    .dataSet(new PublishedDataSetRef("pds-1"))
                    .dataSetWriterId(ushort(11))
                    .build())
            .dataSetWriter(
                DataSetWriterConfig.builder("dsw-events")
                    .dataSet(new PublishedDataSetRef("pds-events"))
                    .dataSetWriterId(ushort(12))
                    .keyFrameCount(uint(0))
                    .eventQueueCapacity(500)
                    .build())
            .build();

    ReaderGroupConfig rg =
        ReaderGroupConfig.builder("rg-1")
            .dataSetReader(
                DataSetReaderConfig.builder("dsr-1")
                    .publisherId(PublisherId.uint16(ushort(42)))
                    .writerGroupId(ushort(1))
                    .dataSetWriterId(ushort(11))
                    .dataSetMetaData(
                        DataSetMetaDataConfig.builder("md-1")
                            .field("f1", NodeIds.Double, MD_F1_ID)
                            .build())
                    .subscribedDataSet(
                        TargetVariablesConfig.builder()
                            .map(
                                FieldSelector.byId(MD_F1_ID),
                                TargetVariableConfig.builder()
                                    .target(
                                        new NodeFieldAddress(
                                            ExpandedNodeId.of(URI_2, "Target.Node1"),
                                            AttributeId.Value))
                                    .build())
                            .build())
                    .build())
            .build();

    return PubSubConfig.builder()
        .connection(
            PubSubConnectionConfig.udp("udp-conn")
                .publisherId(PublisherId.uint16(ushort(42)))
                .address(UdpDatagramAddress.multicast("239.0.0.5", 4841))
                .writerGroup(wg)
                .readerGroup(rg)
                .build())
        .publishedDataSet(pds)
        .publishedDataSet(publishedEventsDataSet())
        .securityGroup(SecurityGroupConfig.builder("sg-1").securityGroupId("SG-001").build())
        .property(new QualifiedName(1, "cfgProp"), Variant.ofString("cfgVal"))
        .build();
  }

  /** Serialize an arbitrary hand-built container the same way the production encode does. */
  private static byte[] encodeFileStruct(UABinaryFileDataType file, EncodingContext context) {
    ByteBuf buffer = Unpooled.buffer();
    try {
      OpcUaBinaryEncoder encoder = new OpcUaBinaryEncoder(context).setBuffer(buffer);
      encoder.encodeStruct(null, file, UABinaryFileDataType.TYPE_ID);

      byte[] bytes = new byte[buffer.readableBytes()];
      buffer.readBytes(bytes);
      return bytes;
    } finally {
      buffer.release();
    }
  }

  private static UABinaryFileDataType decodeFileStruct(byte[] bytes, EncodingContext context) {
    ByteBuf buffer = Unpooled.wrappedBuffer(bytes);
    try {
      OpcUaBinaryDecoder decoder = new OpcUaBinaryDecoder(context).setBuffer(buffer);
      return (UABinaryFileDataType) decoder.decodeStruct(null, UABinaryFileDataType.TYPE_ID);
    } finally {
      buffer.release();
    }
  }

  private static void assertBadTypeMismatch(UaException e) {
    assertEquals(StatusCodes.Bad_TypeMismatch, e.getStatusCode().value(), e.getMessage());
  }

  // region Round trips

  @Test
  void pubSubConfigLevelRoundTripsToFixedPoint() throws UaException {
    EncodingContext context = contextWith(URI_1, URI_2);
    PubSubConfig config = portableConfig();

    byte[] fileBytes = PubSubConfigFiles.encode(config, context);
    PubSubConfig decoded = PubSubConfigFiles.decode(fileBytes, context);

    assertEquals(config, decoded);
  }

  @Test
  void dataTypeLevelRoundTripsToFixedPoint() throws UaException {
    EncodingContext context = contextWith(URI_1, URI_2);
    PubSubConfiguration2DataType dataType =
        portableConfig().toDataType(context.getNamespaceTable());

    byte[] fileBytes = PubSubConfigFiles.encodeDataType(dataType, context);
    PubSubConfiguration2DataType decoded = PubSubConfigFiles.decodeDataType(fileBytes, context);

    assertEquals(dataType, decoded);
  }

  @Test
  void writeReadRoundTripsThroughPathHelpers(@TempDir Path tempDir) throws Exception {
    EncodingContext context = contextWith(URI_1, URI_2);
    PubSubConfig config = portableConfig();

    Path file = tempDir.resolve("config" + PubSubConfigFiles.FILE_EXTENSION);
    PubSubConfigFiles.write(file, config, context);

    assertEquals(config, PubSubConfigFiles.read(file, context));
  }

  // endregion

  // region Namespaces header

  @Test
  void namespacesHeaderIsContextTableMinusNamespaceZeroAndOtherHeaderFieldsNull() {
    EncodingContext context = contextWith(URI_1, URI_2);

    byte[] fileBytes = PubSubConfigFiles.encode(portableConfig(), context);
    UABinaryFileDataType file = decodeFileStruct(fileBytes, context);

    assertArrayEquals(new String[] {URI_1, URI_2}, file.getNamespaces());
    assertNull(file.getStructureDataTypes());
    assertNull(file.getEnumDataTypes());
    assertNull(file.getSimpleDataTypes());
    assertNull(file.getSchemaLocation());
    assertNull(file.getFileHeader());
    assertInstanceOf(ExtensionObject.class, file.getBody().getValue());
  }

  @Test
  void pubSubConfigLevelDecodeResolvesThroughHeaderRegardlessOfLocalTableOrder()
      throws UaException {

    PubSubConfig config = portableConfig();
    byte[] fileBytes = PubSubConfigFiles.encode(config, contextWith(URI_1, URI_2));

    // The decode-side table registers the same URIs at different indices; the header, not the
    // local table, drives resolution, so the URI-based field addresses survive unchanged.
    PubSubConfig decoded = PubSubConfigFiles.decode(fileBytes, contextWith(URI_2, URI_1));

    assertEquals(config, decoded);
  }

  @Test
  void emptyHeaderFallsBackToContextTableAtBothLevels() throws UaException {
    EncodingContext context = contextWith(URI_1, URI_2);
    PubSubConfig config = portableConfig();
    PubSubConfiguration2DataType dataType = config.toDataType(context.getNamespaceTable());

    // Re-wrap the Body of a production-encoded file in a container with an empty header; the
    // Body ExtensionObject passes through the outer-struct round trip undecoded.
    UABinaryFileDataType encoded =
        decodeFileStruct(PubSubConfigFiles.encode(config, context), context);
    var file = new UABinaryFileDataType(null, null, null, null, null, null, encoded.getBody());
    byte[] fileBytes = encodeFileStruct(file, context);

    assertEquals(dataType, PubSubConfigFiles.decodeDataType(fileBytes, context));
    assertEquals(config, PubSubConfigFiles.decode(fileBytes, context));
  }

  @Test
  void dataTypeLevelDecodeRejectsHeaderMismatch() {
    byte[] fileBytes = PubSubConfigFiles.encode(portableConfig(), contextWith(URI_1, URI_2));

    // Same URIs, different order: positional match against the context table fails.
    UaException reordered =
        assertThrows(
            UaException.class,
            () -> PubSubConfigFiles.decodeDataType(fileBytes, contextWith(URI_2, URI_1)));
    assertBadTypeMismatch(reordered);

    // Header longer than the context table: the unmatched entry fails.
    UaException longer =
        assertThrows(
            UaException.class,
            () -> PubSubConfigFiles.decodeDataType(fileBytes, contextWith(URI_1)));
    assertBadTypeMismatch(longer);
  }

  // endregion

  // region Failure contract

  @Test
  void baseTypeBodyRejectedAtBothLevels() {
    EncodingContext context = contextWith(URI_1, URI_2);

    var base = new PubSubConfigurationDataType(null, null, true);
    var file =
        new UABinaryFileDataType(
            null,
            null,
            null,
            null,
            null,
            null,
            Variant.ofExtensionObject(ExtensionObject.encode(context, base)));
    byte[] fileBytes = encodeFileStruct(file, context);

    UaException dataTypeLevel =
        assertThrows(UaException.class, () -> PubSubConfigFiles.decodeDataType(fileBytes, context));
    assertBadTypeMismatch(dataTypeLevel);
    assertTrue(
        dataTypeLevel.getMessage().contains("PubSubConfigurationDataType"),
        dataTypeLevel.getMessage());

    UaException configLevel =
        assertThrows(UaException.class, () -> PubSubConfigFiles.decode(fileBytes, context));
    assertBadTypeMismatch(configLevel);
  }

  @Test
  void wrongBodyTypeRejected() {
    EncodingContext context = contextWith(URI_1, URI_2);

    // A structure that is not a PubSub configuration at all.
    var wrongStruct =
        new UABinaryFileDataType(
            null,
            null,
            null,
            null,
            null,
            null,
            Variant.ofExtensionObject(
                ExtensionObject.encode(context, new NetworkAddressUrlDataType(null, null))));
    UaException wrongType =
        assertThrows(
            UaException.class,
            () ->
                PubSubConfigFiles.decodeDataType(encodeFileStruct(wrongStruct, context), context));
    assertBadTypeMismatch(wrongType);

    // A Body Variant that does not hold an ExtensionObject.
    var notExtensionObject =
        new UABinaryFileDataType(null, null, null, null, null, null, Variant.ofString("nope"));
    UaException notXo =
        assertThrows(
            UaException.class,
            () -> PubSubConfigFiles.decode(encodeFileStruct(notExtensionObject, context), context));
    assertBadTypeMismatch(notXo);
  }

  @Test
  void malformedStreamRejectedAtBothLevels() {
    EncodingContext context = contextWith(URI_1, URI_2);
    byte[] garbage = {0x01, 0x02, 0x03};

    UaException dataTypeLevel =
        assertThrows(UaException.class, () -> PubSubConfigFiles.decodeDataType(garbage, context));
    assertBadTypeMismatch(dataTypeLevel);

    UaException configLevel =
        assertThrows(UaException.class, () -> PubSubConfigFiles.decode(garbage, context));
    assertBadTypeMismatch(configLevel);

    // A valid file truncated mid-Body.
    byte[] fileBytes = PubSubConfigFiles.encode(portableConfig(), context);
    byte[] truncated = Arrays.copyOf(fileBytes, fileBytes.length - 5);

    UaException truncatedFailure =
        assertThrows(UaException.class, () -> PubSubConfigFiles.decode(truncated, context));
    assertBadTypeMismatch(truncatedFailure);
  }

  @Test
  void trailingBytesIgnoredAtBothLevels() throws UaException {
    EncodingContext context = contextWith(URI_1, URI_2);
    PubSubConfig config = portableConfig();
    PubSubConfiguration2DataType dataType = config.toDataType(context.getNamespaceTable());

    byte[] fileBytes = PubSubConfigFiles.encode(config, context);
    byte[] padded = Arrays.copyOf(fileBytes, fileBytes.length + 7);

    assertEquals(dataType, PubSubConfigFiles.decodeDataType(padded, context));
    assertEquals(config, PubSubConfigFiles.decode(padded, context));
  }

  @Test
  void encodeRejectsUnresolvableNamespaceUri() {
    // The context table lacks URI_2, so the config's field addresses cannot be resolved.
    PubSubConfigValidationException e =
        assertThrows(
            PubSubConfigValidationException.class,
            () -> PubSubConfigFiles.encode(portableConfig(), contextWith(URI_1)));
    assertTrue(e.getMessage().contains(URI_2), e.getMessage());
  }

  @Test
  void decodeRejectsSemanticallyInvalidConfigThatPassesTheDataTypeLevel() throws UaException {
    EncodingContext context = contextWith(URI_1, URI_2);
    PubSubConfiguration2DataType dataType =
        portableConfig().toDataType(context.getNamespaceTable());

    PubSubConnectionDataType[] connections = dataType.getConnections();
    assertNotNull(connections);

    var invalid =
        new PubSubConfiguration2DataType(
            dataType.getPublishedDataSets(),
            new PubSubConnectionDataType[] {connections[0], connections[0]},
            dataType.getEnabled(),
            dataType.getSubscribedDataSets(),
            dataType.getDataSetClasses(),
            dataType.getDefaultSecurityKeyServices(),
            dataType.getSecurityGroups(),
            dataType.getPubSubKeyPushTargets(),
            dataType.getConfigurationVersion(),
            dataType.getConfigurationProperties());

    byte[] fileBytes = PubSubConfigFiles.encodeDataType(invalid, context);

    // The DataType level is validation-free: duplicate connection names pass through.
    assertEquals(invalid, PubSubConfigFiles.decodeDataType(fileBytes, context));

    // The PubSubConfig level applies builder validation.
    assertThrows(
        PubSubConfigValidationException.class, () -> PubSubConfigFiles.decode(fileBytes, context));
  }

  // endregion
}

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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.nio.file.Path;
import java.time.Duration;
import java.util.UUID;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.binary.OpcUaBinaryDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.binary.OpcUaBinaryEncoder;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfiguration2DataType;
import org.eclipse.milo.opcua.stack.core.types.structured.UABinaryFileDataType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Multi-namespace portability corners of {@link PubSubConfigFiles} beyond {@code
 * PubSubConfigFilesTest}: the PubSubConfig-level decode resolves entirely THROUGH the Table 88
 * namespaces header, so a buffer travels into a target whose local NamespaceTable lacks the
 * encoding-side URIs altogether (including via the {@code write}/{@code read} Path helpers and into
 * a shuffled superset table); with the header stripped, non-ns0 namespace indices become
 * unresolvable and the config level fails cleanly with {@link PubSubConfigValidationException}
 * while the validation-free DataType level still succeeds; and an ns0-only config carries an
 * effectively empty header and decodes into any table.
 */
class PubSubConfigFilesNegativeTest {

  private static final String URI_1 = "urn:milo:test:neg:ns1";
  private static final String URI_2 = "urn:milo:test:neg:ns2";
  private static final String URI_3 = "urn:milo:test:neg:ns3";

  private static final UUID FIELD_TEMP_ID = new UUID(0xE1L, 1L);
  private static final UUID FIELD_COUNT_ID = new UUID(0xE1L, 2L);
  private static final UUID FIELD_PLAIN_ID = new UUID(0xE1L, 3L);

  private static EncodingContext contextWith(String... uris) {
    var context = new DefaultEncodingContext();
    for (String uri : uris) {
      context.getNamespaceTable().add(uri);
    }
    return context;
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
            .build();

    return PubSubConfig.builder()
        .connection(
            PubSubConnectionConfig.udp("udp-conn")
                .publisherId(PublisherId.uint16(ushort(42)))
                .address(UdpDatagramAddress.multicast("239.0.0.5", 4841))
                .writerGroup(wg)
                .build())
        .publishedDataSet(pds)
        .build();
  }

  /** A config with no non-ns0 NodeId references at all. */
  private static PubSubConfig ns0OnlyConfig() {
    PublishedDataSetConfig pds =
        PublishedDataSetConfig.builder("pds-plain")
            .field(
                FieldDefinition.builder("plain")
                    .dataType(NodeIds.Int32)
                    .dataSetFieldId(FIELD_PLAIN_ID)
                    .build())
            .build();

    return PubSubConfig.builder()
        .connection(
            PubSubConnectionConfig.udp("udp-conn")
                .publisherId(PublisherId.uint16(ushort(43)))
                .address(UdpDatagramAddress.multicast("239.0.0.5", 4841))
                .writerGroup(
                    WriterGroupConfig.builder("wg-plain")
                        .writerGroupId(ushort(1))
                        .publishingInterval(Duration.ofMillis(250))
                        .dataSetWriter(
                            DataSetWriterConfig.builder("dsw-plain")
                                .dataSet(new PublishedDataSetRef("pds-plain"))
                                .dataSetWriterId(ushort(11))
                                .build())
                        .build())
                .build())
        .publishedDataSet(pds)
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

  @Test
  void configLevelDecodeResolvesThroughHeaderIntoTableLackingTheUris() throws UaException {
    PubSubConfig config = portableConfig();
    byte[] fileBytes = PubSubConfigFiles.encode(config, contextWith(URI_1, URI_2));

    // the target table knows NEITHER encoding-side URI: the header alone drives resolution,
    // so the URI-based field addresses survive unchanged
    assertEquals(config, PubSubConfigFiles.decode(fileBytes, contextWith()));

    // and into a shuffled superset table (extra URI first, originals reordered)
    assertEquals(config, PubSubConfigFiles.decode(fileBytes, contextWith(URI_3, URI_2, URI_1)));
  }

  @Test
  void readPathHelperResolvesThroughHeaderIntoForeignTable(@TempDir Path tempDir) throws Exception {
    PubSubConfig config = portableConfig();

    Path file = tempDir.resolve("portable" + PubSubConfigFiles.FILE_EXTENSION);
    PubSubConfigFiles.write(file, config, contextWith(URI_1, URI_2));

    // a different server reads the file without registering the namespaces first
    assertEquals(config, PubSubConfigFiles.read(file, contextWith()));
  }

  @Test
  void emptyHeaderWithUnresolvableIndicesFailsValidationAtTheConfigLevelOnly() throws UaException {
    EncodingContext encodeContext = contextWith(URI_1, URI_2);
    PubSubConfig config = portableConfig();
    PubSubConfiguration2DataType dataType = config.toDataType(encodeContext.getNamespaceTable());

    // strip the header: re-wrap the production Body in a container with no namespaces entry
    UABinaryFileDataType encoded =
        decodeFileStruct(PubSubConfigFiles.encode(config, encodeContext), encodeContext);
    var stripped = new UABinaryFileDataType(null, null, null, null, null, null, encoded.getBody());
    byte[] fileBytes = encodeFileStruct(stripped, encodeContext);

    // the validation-free DataType level decodes: indices are opaque there
    EncodingContext shortContext = contextWith(URI_1);
    assertEquals(dataType, PubSubConfigFiles.decodeDataType(fileBytes, shortContext));

    // the PubSubConfig level falls back to the context table (empty header), where index 2
    // (URI_2) is unresolvable: a clean validation failure, not a decode error or crash
    PubSubConfigValidationException e =
        assertThrows(
            PubSubConfigValidationException.class,
            () -> PubSubConfigFiles.decode(fileBytes, shortContext));
    assertTrue(e.getMessage() != null && !e.getMessage().isBlank());
  }

  @Test
  void ns0OnlyConfigCarriesEmptyHeaderAndDecodesIntoAnyTable() throws UaException {
    EncodingContext bare = contextWith();
    PubSubConfig config = ns0OnlyConfig();

    byte[] fileBytes = PubSubConfigFiles.encode(config, bare);

    // the Table 88 header is the context table minus ns0: empty here
    UABinaryFileDataType file = decodeFileStruct(fileBytes, bare);
    String[] namespaces = file.getNamespaces();
    assertTrue(
        namespaces == null || namespaces.length == 0,
        "ns0-only encode carries an empty namespaces header");

    // and the buffer decodes into any table, padded or not, at both levels
    assertEquals(config, PubSubConfigFiles.decode(fileBytes, bare));
    assertEquals(config, PubSubConfigFiles.decode(fileBytes, contextWith(URI_1, URI_2)));
    assertEquals(
        config.toDataType(bare.getNamespaceTable()),
        PubSubConfigFiles.decodeDataType(fileBytes, contextWith(URI_1, URI_2)));
  }
}

/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.uadp;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetRef;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.UadpDataSetWriterSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.UadpWriterGroupSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.WriterGroupConfig;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;
import org.junit.jupiter.api.Test;

/**
 * Model contracts of the discovery types and the {@code of(...)} factories added alongside them:
 * the factories mirror the canonical constructors exactly (including the {@link EncodeContext},
 * {@link DecodeContext}, and {@link DecodedNetworkMessage} factories added while touching, per the
 * established evolvability convention), the wire constants match the Part 14 table values, and list
 * components are defensively copied and immutable.
 */
class UadpDiscoveryModelTest {

  private static final EncodingContext ENCODING_CONTEXT = new DefaultEncodingContext();

  // region UadpDiscoveryProbe

  /** The Table 178/179 values pinned by the codec scope. */
  @Test
  void probeConstantsMatchPart14TableValues() {
    assertEquals(1, UadpDiscoveryProbe.PROBE_TYPE_PUBLISHER_INFORMATION);
    assertEquals(2, UadpDiscoveryProbe.INFORMATION_TYPE_DATA_SET_META_DATA);
  }

  @Test
  void probeOfMirrorsCanonicalConstructor() {
    PublisherId target = PublisherId.ubyte(ubyte(7));
    List<UShort> ids = List.of(ushort(1), ushort(2));

    assertEquals(
        new UadpDiscoveryProbe(1, 2, target, ids), UadpDiscoveryProbe.of(1, 2, target, ids));
  }

  /** The two-argument convenience factory builds the DataSetMetaData probe. */
  @Test
  void probeConvenienceFactoryBuildsMetaDataProbe() {
    PublisherId target = PublisherId.ubyte(ubyte(7));
    List<UShort> ids = List.of(ushort(100));

    UadpDiscoveryProbe probe = UadpDiscoveryProbe.of(target, ids);

    assertEquals(UadpDiscoveryProbe.PROBE_TYPE_PUBLISHER_INFORMATION, probe.probeType());
    assertEquals(UadpDiscoveryProbe.INFORMATION_TYPE_DATA_SET_META_DATA, probe.informationType());
    assertEquals(target, probe.targetPublisherId());
    assertEquals(ids, probe.dataSetWriterIds());
    assertEquals(new UadpDiscoveryProbe(1, 2, target, ids), probe);
  }

  @Test
  void probeDataSetWriterIdsAreDefensivelyCopiedAndImmutable() {
    List<UShort> ids = new ArrayList<>();
    ids.add(ushort(1));

    UadpDiscoveryProbe probe = UadpDiscoveryProbe.of(PublisherId.ubyte(ubyte(7)), ids);

    // Mutating the source list after construction does not affect the record.
    ids.add(ushort(2));
    assertEquals(List.of(ushort(1)), probe.dataSetWriterIds());

    assertThrows(
        UnsupportedOperationException.class, () -> probe.dataSetWriterIds().add(ushort(3)));
  }

  // endregion

  // region UadpMetaDataAnnouncement

  @Test
  void announcementOfMirrorsCanonicalConstructor() {
    PublisherId publisherId = PublisherId.uint16(ushort(0x1234));
    DataSetMetaDataType metaData = minimalMetaData();

    assertEquals(
        new UadpMetaDataAnnouncement(
            publisherId, ushort(12), ushort(100), metaData, StatusCode.GOOD),
        UadpMetaDataAnnouncement.of(
            publisherId, ushort(12), ushort(100), metaData, StatusCode.GOOD));
  }

  // endregion

  // region of() factories added alongside the discovery types

  @Test
  void decodedNetworkMessageOfMirrorsCanonicalConstructor() {
    PublisherId publisherId = PublisherId.ubyte(ubyte(42));
    DateTime timestamp = new DateTime(1_000L);

    List<DecodedDataSetMessage> messages =
        List.of(
            new DecodedDataSetMessage(
                ushort(1),
                DataSetMessageKind.KEY_FRAME,
                true,
                uint(7),
                null,
                null,
                new ConfigurationVersionDataType(uint(1), uint(2)),
                List.of(
                    new DecodedField(
                        0,
                        new DataValue(
                            Variant.ofInt32(42), StatusCode.GOOD, null, null, null, null)))));
    List<DecodedMetaData> metaData = List.of(new DecodedMetaData(ushort(1), minimalMetaData()));

    assertEquals(
        new DecodedNetworkMessage(
            publisherId,
            ushort(258),
            uint(5),
            ushort(1),
            ushort(16),
            timestamp,
            messages,
            metaData),
        DecodedNetworkMessage.of(
            publisherId,
            ushort(258),
            uint(5),
            ushort(1),
            ushort(16),
            timestamp,
            messages,
            metaData));
  }

  /** All header components are nullable; the factory passes nulls through like the constructor. */
  @Test
  void decodedNetworkMessageOfAcceptsAllNullHeaders() {
    assertEquals(
        new DecodedNetworkMessage(null, null, null, null, null, null, List.of(), List.of()),
        DecodedNetworkMessage.of(null, null, null, null, null, null, List.of(), List.of()));
  }

  @Test
  void encodeContextOfMirrorsCanonicalConstructor() {
    DataSetWriterConfig writer =
        DataSetWriterConfig.builder("writer")
            .dataSet(new PublishedDataSetRef("ds"))
            .dataSetWriterId(ushort(1))
            .settings(UadpDataSetWriterSettings.builder().build())
            .build();

    WriterGroupConfig group =
        WriterGroupConfig.builder("group")
            .writerGroupId(ushort(258))
            .messageSettings(UadpWriterGroupSettings.builder().build())
            .dataSetWriter(writer)
            .build();

    DataSetMessageDraft draft =
        new DataSetMessageDraft(
            writer,
            ushort(1),
            null,
            null,
            new ConfigurationVersionDataType(uint(0), uint(0)),
            false,
            List.of(new DataValue(Variant.ofInt32(1), StatusCode.GOOD, null, null, null, null)));

    PublisherId publisherId = PublisherId.ubyte(ubyte(1));
    DateTime timestamp = new DateTime(2_000L);

    assertEquals(
        new EncodeContext(
            ENCODING_CONTEXT,
            publisherId,
            group,
            uint(1),
            ushort(2),
            ushort(3),
            timestamp,
            List.of(draft)),
        EncodeContext.of(
            ENCODING_CONTEXT,
            publisherId,
            group,
            uint(1),
            ushort(2),
            ushort(3),
            timestamp,
            List.of(draft)));
  }

  @Test
  void decodeContextOfMirrorsCanonicalConstructor() {
    assertEquals(new DecodeContext(ENCODING_CONTEXT), DecodeContext.of(ENCODING_CONTEXT));
  }

  // endregion

  // region helpers

  private static DataSetMetaDataType minimalMetaData() {
    return new DataSetMetaDataType(
        null,
        null,
        null,
        null,
        "DS",
        new LocalizedText(null, null),
        null,
        new UUID(0L, 0L),
        new ConfigurationVersionDataType(uint(7), uint(3)));
  }

  // endregion
}

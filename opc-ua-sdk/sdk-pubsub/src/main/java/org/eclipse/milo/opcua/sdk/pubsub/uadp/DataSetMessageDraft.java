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

import java.util.HashSet;
import java.util.List;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;
import org.jspecify.annotations.Nullable;

/**
 * One DataSetMessage to be encoded into a NetworkMessage.
 *
 * <p>Key frame drafts carry one value per metadata field in {@link #fields()}; delta frame drafts
 * carry only the changed fields as explicit {@code (index, value)} pairs in {@link #deltaFields()}
 * (Part 14 §7.2.4.5.6 Table 164 for UADP, §7.2.5.4.1 Table 185 for JSON); keep-alive drafts carry
 * no fields at all.
 *
 * @param writer the config of the DataSetWriter producing the message.
 * @param sequenceNumber the DataSetMessage sequence number. The slot spans both wire widths of Part
 *     14 §7.2.3 Table 152: the engine counter wraps at the DataType maximum of the group's mapping
 *     — UInt16 for UADP (Table 162), UInt32 for JSON (Table 185) and for custom mappings — so the
 *     UADP mapping always receives a value that fits in the UInt16 it writes, while the JSON
 *     mapping writes the full UInt32 range. An externally constructed draft whose transmitted
 *     sequence number exceeds the UADP mapping's UInt16 range is rejected at encode time with
 *     {@code Bad_EncodingLimitsExceeded} rather than silently truncated.
 * @param timestamp the DataSetMessage timestamp, or {@code null} if not included.
 * @param status the DataSetMessage status, or {@code null} if not included.
 * @param configurationVersion the configuration version of the dataset.
 * @param kind the kind of DataSetMessage this draft encodes to. The built-in mappings emit {@link
 *     DataSetMessageKind#KEY_FRAME}, {@link DataSetMessageKind#DELTA_FRAME}, and {@link
 *     DataSetMessageKind#KEEP_ALIVE}; {@link DataSetMessageKind#EVENT} emission is not supported.
 * @param fields the field values of a key frame, in wire order; empty for keep-alive and delta
 *     frame messages.
 * @param deltaFields the changed fields of a delta frame, each carrying its explicit metadata
 *     position; empty for every other kind.
 * @param metaData the resolved metadata of the writer's PublishedDataSet — the wire-bound surface
 *     ({@code 0:MiloSourceKey} field properties stripped), giving mappings access to field names
 *     and types (e.g. the JSON mapping's name-keyed payloads) — or {@code null} when not available.
 *     The {@code fields} are in the order of {@code metaData}'s fields, and {@code deltaFields}
 *     indices are positions into them. The UADP mapping ignores it.
 * @apiNote Create instances via {@link #of(DataSetWriterConfig, UInteger, DateTime, StatusCode,
 *     ConfigurationVersionDataType, boolean, List, DataSetMetaDataType)} or {@link
 *     #ofDeltaFrame(DataSetWriterConfig, UInteger, DateTime, StatusCode,
 *     ConfigurationVersionDataType, List, DataSetMetaDataType)} rather than the canonical
 *     constructor; the factory methods are stable while the canonical constructor is not.
 */
public record DataSetMessageDraft(
    DataSetWriterConfig writer,
    UInteger sequenceNumber,
    @Nullable DateTime timestamp,
    @Nullable StatusCode status,
    ConfigurationVersionDataType configurationVersion,
    DataSetMessageKind kind,
    List<DataValue> fields,
    List<DeltaField> deltaFields,
    @Nullable DataSetMetaDataType metaData) {

  /**
   * One changed field of a delta frame draft, mirroring the decode-side {@link DecodedField}.
   *
   * @param index the position of the field in the DataSetMetaData under the ConfigurationVersion
   *     carried by the message (Part 14 §7.2.4.5.6 Table 164 FieldIndex). A publisher uses an index
   *     at most once per DataSetMessage; the JSON mapping resolves the field name from this index.
   * @param value the field value to transmit; the mappings apply the same per-field wire encoding
   *     as for key frame fields.
   */
  public record DeltaField(int index, DataValue value) {}

  /**
   * Create a new {@link DataSetMessageDraft}.
   *
   * @param writer the config of the DataSetWriter producing the message.
   * @param sequenceNumber the DataSetMessage sequence number.
   * @param timestamp the DataSetMessage timestamp, or {@code null} if not included.
   * @param status the DataSetMessage status, or {@code null} if not included.
   * @param configurationVersion the configuration version of the dataset.
   * @param kind the kind of DataSetMessage this draft encodes to.
   * @param fields the field values of a key frame, in wire order; empty for other kinds.
   * @param deltaFields the changed fields of a delta frame; empty for other kinds.
   * @param metaData the resolved metadata of the writer's PublishedDataSet, or {@code null} when
   *     not available.
   * @throws IllegalArgumentException if the field lists do not match the kind: keep-alives carry no
   *     fields, delta frames carry only {@code deltaFields}, every other kind carries only {@code
   *     fields}.
   */
  public DataSetMessageDraft {
    fields = List.copyOf(fields);
    deltaFields = List.copyOf(deltaFields);

    if (kind == DataSetMessageKind.KEEP_ALIVE && !(fields.isEmpty() && deltaFields.isEmpty())) {
      throw new IllegalArgumentException("keep-alive drafts carry no fields");
    }
    if (kind == DataSetMessageKind.DELTA_FRAME && !fields.isEmpty()) {
      throw new IllegalArgumentException("delta frame drafts carry their fields in deltaFields");
    }
    if (kind != DataSetMessageKind.DELTA_FRAME && !deltaFields.isEmpty()) {
      throw new IllegalArgumentException("only delta frame drafts carry deltaFields");
    }
  }

  /**
   * Whether this draft is a keep-alive message.
   *
   * @return {@code true} if {@link #kind()} is {@link DataSetMessageKind#KEEP_ALIVE}.
   */
  public boolean keepAlive() {
    return kind == DataSetMessageKind.KEEP_ALIVE;
  }

  /**
   * Create a key frame or keep-alive {@link DataSetMessageDraft}.
   *
   * @param writer the config of the DataSetWriter producing the message.
   * @param sequenceNumber the DataSetMessage sequence number.
   * @param timestamp the DataSetMessage timestamp, or {@code null} if not included.
   * @param status the DataSetMessage status, or {@code null} if not included.
   * @param configurationVersion the configuration version of the dataset.
   * @param keepAlive {@code true} if this is a keep-alive message; keep-alive messages carry no
   *     fields.
   * @param fields the field values of the message, in wire order; empty for keep-alive messages.
   * @param metaData the resolved metadata of the writer's PublishedDataSet, or {@code null} when
   *     not available.
   * @return a new {@link DataSetMessageDraft}.
   */
  public static DataSetMessageDraft of(
      DataSetWriterConfig writer,
      UInteger sequenceNumber,
      @Nullable DateTime timestamp,
      @Nullable StatusCode status,
      ConfigurationVersionDataType configurationVersion,
      boolean keepAlive,
      List<DataValue> fields,
      @Nullable DataSetMetaDataType metaData) {

    return new DataSetMessageDraft(
        writer,
        sequenceNumber,
        timestamp,
        status,
        configurationVersion,
        keepAlive ? DataSetMessageKind.KEEP_ALIVE : DataSetMessageKind.KEY_FRAME,
        fields,
        List.of(),
        metaData);
  }

  /**
   * Create a key frame or keep-alive {@link DataSetMessageDraft} without resolved metadata.
   *
   * @param writer the config of the DataSetWriter producing the message.
   * @param sequenceNumber the DataSetMessage sequence number.
   * @param timestamp the DataSetMessage timestamp, or {@code null} if not included.
   * @param status the DataSetMessage status, or {@code null} if not included.
   * @param configurationVersion the configuration version of the dataset.
   * @param keepAlive {@code true} if this is a keep-alive message; keep-alive messages carry no
   *     fields.
   * @param fields the field values of the message, in wire order; empty for keep-alive messages.
   * @return a new {@link DataSetMessageDraft}.
   */
  public static DataSetMessageDraft of(
      DataSetWriterConfig writer,
      UInteger sequenceNumber,
      @Nullable DateTime timestamp,
      @Nullable StatusCode status,
      ConfigurationVersionDataType configurationVersion,
      boolean keepAlive,
      List<DataValue> fields) {

    return of(
        writer, sequenceNumber, timestamp, status, configurationVersion, keepAlive, fields, null);
  }

  /**
   * Create a delta frame {@link DataSetMessageDraft} carrying only the changed fields (Part 14
   * §7.2.4.5.6 Table 164, §7.2.5.4.1 Table 185).
   *
   * @param writer the config of the DataSetWriter producing the message.
   * @param sequenceNumber the DataSetMessage sequence number.
   * @param timestamp the DataSetMessage timestamp, or {@code null} if not included.
   * @param status the DataSetMessage status, or {@code null} if not included.
   * @param configurationVersion the configuration version of the dataset; delta field indices are
   *     positions in the metadata of this version.
   * @param deltaFields the changed fields, each index used at most once.
   * @param metaData the resolved metadata of the writer's PublishedDataSet, or {@code null} when
   *     not available; the JSON mapping requires it to resolve field names by index.
   * @return a new delta frame {@link DataSetMessageDraft}.
   * @throws IllegalArgumentException if a field index occurs more than once in {@code deltaFields}
   *     (Part 14 §7.2.4.5.6 Table 164: "A Publisher shall use an index only once").
   */
  public static DataSetMessageDraft ofDeltaFrame(
      DataSetWriterConfig writer,
      UInteger sequenceNumber,
      @Nullable DateTime timestamp,
      @Nullable StatusCode status,
      ConfigurationVersionDataType configurationVersion,
      List<DeltaField> deltaFields,
      @Nullable DataSetMetaDataType metaData) {

    var indices = new HashSet<Integer>();
    for (DeltaField field : deltaFields) {
      if (!indices.add(field.index())) {
        throw new IllegalArgumentException(
            "duplicate delta field index "
                + field.index()
                + ": a Publisher shall use an index only once (Part 14 Table 164)");
      }
    }

    return new DataSetMessageDraft(
        writer,
        sequenceNumber,
        timestamp,
        status,
        configurationVersion,
        DataSetMessageKind.DELTA_FRAME,
        List.of(),
        deltaFields,
        metaData);
  }
}

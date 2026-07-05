/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetMetaDataConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DataSetMessageKind;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.jspecify.annotations.Nullable;

/**
 * A DataSet received and decoded by a DataSetReader, delivered to {@link DataSetListener}s.
 *
 * @param reader the handle of the DataSetReader that received the DataSet.
 * @param publisherId the id of the publisher the DataSet originated from.
 * @param writerGroupId the id of the WriterGroup the DataSet originated from.
 * @param dataSetWriterId the id of the DataSetWriter the DataSet originated from.
 * @param networkMessageSequenceNumber the Part 14 §7.2.3 sequence number of the NetworkMessage that
 *     carried the DataSet, as present on the wire (UADP GroupHeader, UInt16), or {@code null} when
 *     absent — the JSON mapping has no NetworkMessage sequence number, and the UADP GroupHeader is
 *     optional.
 * @param dataSetMessageSequenceNumber the Part 14 §7.2.3 sequence number of the DataSetMessage, as
 *     present on the wire, or {@code null} when absent; UInt32 to span both mappings (UADP carries
 *     UInt16 values, JSON UInt32).
 * @param dataSetName the name of the DataSet, if known from metadata, otherwise {@code null}.
 * @param metaData the metadata the DataSet was decoded against, or {@code null} if none.
 * @param kind the kind of the DataSetMessage that carried the DataSet (Part 14 §7.2.3), e.g. a key
 *     frame, delta frame, or event.
 * @param fields the decoded field values, in wire order.
 */
public record DataSetReceivedEvent(
    PubSubHandle reader,
    PublisherId publisherId,
    UShort writerGroupId,
    UShort dataSetWriterId,
    @Nullable UShort networkMessageSequenceNumber,
    @Nullable UInteger dataSetMessageSequenceNumber,
    @Nullable String dataSetName,
    @Nullable DataSetMetaDataConfig metaData,
    DataSetMessageKind kind,
    List<DataSetFieldValue> fields) {

  /**
   * Create a new {@link DataSetReceivedEvent}.
   *
   * @param reader the handle of the DataSetReader that received the DataSet.
   * @param publisherId the id of the publisher the DataSet originated from.
   * @param writerGroupId the id of the WriterGroup the DataSet originated from.
   * @param dataSetWriterId the id of the DataSetWriter the DataSet originated from.
   * @param networkMessageSequenceNumber the sequence number of the carrying NetworkMessage, or
   *     {@code null} when absent on the wire.
   * @param dataSetMessageSequenceNumber the sequence number of the DataSetMessage, or {@code null}
   *     when absent on the wire.
   * @param dataSetName the name of the DataSet, if known from metadata, otherwise {@code null}.
   * @param metaData the metadata the DataSet was decoded against, or {@code null} if none.
   * @param kind the kind of the DataSetMessage that carried the DataSet, e.g. a key frame, delta
   *     frame, or event.
   * @param fields the decoded field values, in wire order.
   */
  public DataSetReceivedEvent {
    fields = List.copyOf(fields);
  }

  /**
   * Get the decoded field values keyed by field name, in wire order.
   *
   * @return the decoded field values keyed by field name.
   */
  public Map<String, DataValue> fieldsByName() {
    var map = new LinkedHashMap<String, DataValue>(fields.size());
    for (DataSetFieldValue field : fields) {
      map.put(field.name(), field.value());
    }
    return Collections.unmodifiableMap(map);
  }
}

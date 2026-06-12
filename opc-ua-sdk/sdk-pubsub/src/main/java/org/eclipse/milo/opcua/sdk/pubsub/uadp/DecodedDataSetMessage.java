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

import java.util.List;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.jspecify.annotations.Nullable;

/**
 * One DataSetMessage decoded from a NetworkMessage.
 *
 * <p>Header components are {@code null} when the corresponding header was not present on the wire.
 *
 * @param dataSetWriterId the DataSetWriterId from the payload header, or {@code null} if not
 *     present.
 * @param kind the kind of DataSetMessage.
 * @param valid the DataSetFlags1 "message is valid" bit.
 * @param sequenceNumber the DataSetMessage sequence number, or {@code null} if not present. UInt32
 *     to cover the JSON mapping's range (Part 14 §7.2.5.4.1); UADP carries UInt16 values.
 * @param timestamp the DataSetMessage timestamp, or {@code null} if not present.
 * @param status the DataSetMessage status, or {@code null} if not present.
 * @param configurationVersion the configuration version of the dataset, or {@code null} if not
 *     present.
 * @param fields the decoded fields; for key frames the field index is the position in the dataset,
 *     for delta frames it is the explicit wire index. Empty for keep-alive messages.
 */
public record DecodedDataSetMessage(
    @Nullable UShort dataSetWriterId,
    DataSetMessageKind kind,
    boolean valid,
    @Nullable UInteger sequenceNumber,
    @Nullable DateTime timestamp,
    @Nullable StatusCode status,
    @Nullable ConfigurationVersionDataType configurationVersion,
    List<DecodedField> fields) {

  /**
   * Create a new {@link DecodedDataSetMessage}.
   *
   * @param dataSetWriterId the DataSetWriterId from the payload header, or {@code null} if not
   *     present.
   * @param kind the kind of DataSetMessage.
   * @param valid the DataSetFlags1 "message is valid" bit.
   * @param sequenceNumber the DataSetMessage sequence number, or {@code null} if not present.
   *     UInt32 to cover the JSON mapping's range; UADP carries UInt16 values.
   * @param timestamp the DataSetMessage timestamp, or {@code null} if not present.
   * @param status the DataSetMessage status, or {@code null} if not present.
   * @param configurationVersion the configuration version of the dataset, or {@code null} if not
   *     present.
   * @param fields the decoded fields; for key frames the field index is the position in the
   *     dataset, for delta frames it is the explicit wire index. Empty for keep-alive messages.
   */
  public DecodedDataSetMessage {
    fields = List.copyOf(fields);
  }
}

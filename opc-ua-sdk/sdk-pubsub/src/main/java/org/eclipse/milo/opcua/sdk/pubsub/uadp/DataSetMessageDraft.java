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
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;
import org.jspecify.annotations.Nullable;

/**
 * One DataSetMessage to be encoded into a NetworkMessage.
 *
 * @param writer the config of the DataSetWriter producing the message.
 * @param sequenceNumber the DataSetMessage sequence number.
 * @param timestamp the DataSetMessage timestamp, or {@code null} if not included.
 * @param status the DataSetMessage status, or {@code null} if not included.
 * @param configurationVersion the configuration version of the dataset.
 * @param keepAlive {@code true} if this is a keep-alive message; keep-alive messages carry no
 *     fields.
 * @param fields the field values of the message, in wire order; empty for keep-alive messages.
 * @param metaData the resolved metadata of the writer's PublishedDataSet — the wire-bound surface
 *     ({@code 0:MiloSourceKey} field properties stripped), giving mappings access to field names
 *     and types (e.g. the JSON mapping's name-keyed payloads) — or {@code null} when not available.
 *     The {@code fields} are in the order of {@code metaData}'s fields. The UADP mapping ignores
 *     it.
 * @apiNote Create instances via {@link #of(DataSetWriterConfig, UShort, DateTime, StatusCode,
 *     ConfigurationVersionDataType, boolean, List, DataSetMetaDataType)} rather than the canonical
 *     constructor; the factory methods are stable while the canonical constructor is not.
 */
public record DataSetMessageDraft(
    DataSetWriterConfig writer,
    UShort sequenceNumber,
    @Nullable DateTime timestamp,
    @Nullable StatusCode status,
    ConfigurationVersionDataType configurationVersion,
    boolean keepAlive,
    List<DataValue> fields,
    @Nullable DataSetMetaDataType metaData) {

  /**
   * Create a new {@link DataSetMessageDraft}.
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
   */
  public DataSetMessageDraft {
    fields = List.copyOf(fields);
  }

  /**
   * Create a new {@link DataSetMessageDraft} without resolved metadata.
   *
   * @param writer the config of the DataSetWriter producing the message.
   * @param sequenceNumber the DataSetMessage sequence number.
   * @param timestamp the DataSetMessage timestamp, or {@code null} if not included.
   * @param status the DataSetMessage status, or {@code null} if not included.
   * @param configurationVersion the configuration version of the dataset.
   * @param keepAlive {@code true} if this is a keep-alive message; keep-alive messages carry no
   *     fields.
   * @param fields the field values of the message, in wire order; empty for keep-alive messages.
   */
  public DataSetMessageDraft(
      DataSetWriterConfig writer,
      UShort sequenceNumber,
      @Nullable DateTime timestamp,
      @Nullable StatusCode status,
      ConfigurationVersionDataType configurationVersion,
      boolean keepAlive,
      List<DataValue> fields) {

    this(writer, sequenceNumber, timestamp, status, configurationVersion, keepAlive, fields, null);
  }

  /**
   * Create a {@link DataSetMessageDraft}.
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
      UShort sequenceNumber,
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
        keepAlive,
        fields,
        metaData);
  }

  /**
   * Create a {@link DataSetMessageDraft} without resolved metadata.
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
      UShort sequenceNumber,
      @Nullable DateTime timestamp,
      @Nullable StatusCode status,
      ConfigurationVersionDataType configurationVersion,
      boolean keepAlive,
      List<DataValue> fields) {

    return new DataSetMessageDraft(
        writer, sequenceNumber, timestamp, status, configurationVersion, keepAlive, fields, null);
  }
}

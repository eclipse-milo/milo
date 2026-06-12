/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.internal;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;

import java.util.Collections;
import java.util.List;
import org.eclipse.milo.opcua.sdk.pubsub.ComponentType;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetSnapshot;
import org.eclipse.milo.opcua.sdk.pubsub.PublishedDataSetReadContext;
import org.eclipse.milo.opcua.sdk.pubsub.PublishedDataSetSource;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetMetaDataMapper;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetWriterConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.UadpDataSetWriterSettings;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.DataSetMessageDraft;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;

/**
 * Runtime for one DataSetWriter: pulls its source for a snapshot and produces one DataSetMessage
 * draft per publish cycle.
 *
 * <p>Source failures never cause a missed cycle: the draft is produced with every field set to
 * {@code Bad_InternalError} and the failure is recorded in diagnostics. Always emits key frames in
 * this version; {@code keyFrameCount} is carried in config only.
 *
 * <p>Draft creation and the DataSetMessage sequence counter are confined to the owning group's
 * publish task thread.
 */
final class DataSetWriterRuntime extends AbstractComponentRuntime {

  private final PubSubServiceImpl service;
  private final WriterGroupRuntime group;
  private final DataSetWriterConfig config;
  private final PublishedDataSetConfig dataSet;
  private final PublishedDataSetReadContext readContext;
  private final ConfigurationVersionDataType configurationVersion;
  private final DataSetMetaDataType metaData;

  /** Only touched by the owning group's publish task. */
  private int sequenceNumber = 0;

  private volatile long lastSentNanos = System.nanoTime();

  DataSetWriterRuntime(
      PubSubServiceImpl service, WriterGroupRuntime group, DataSetWriterConfig config) {

    super(
        ComponentType.DATA_SET_WRITER,
        group.path() + "/" + config.getName(),
        group,
        config.isEnabled());

    this.service = service;
    this.group = group;
    this.config = config;

    this.dataSet = service.requirePublishedDataSet(config.getDataSet());
    this.readContext =
        new PublishedDataSetReadContext(config.getDataSet(), dataSet.getFields(), null);
    this.configurationVersion =
        new ConfigurationVersionDataType(
            dataSet.getConfigurationVersionMajor(), dataSet.getConfigurationVersionMinor());
    this.metaData = DataSetMetaDataMapper.toDataSetMetaDataType(dataSet, true);
  }

  DataSetWriterConfig config() {
    return config;
  }

  /**
   * The resolved metadata of this writer's PublishedDataSet, the wire-bound surface with
   * Milo-internal field properties stripped: carried on every draft and published to broker
   * metadata queues.
   */
  DataSetMetaDataType metaData() {
    return metaData;
  }

  long lastSentNanos() {
    return lastSentNanos;
  }

  void resetLastSent(long nanos) {
    lastSentNanos = nanos;
  }

  @Override
  List<? extends AbstractComponentRuntime> children() {
    return List.of();
  }

  @Override
  void activate() {
    // broker connections publish this writer's (retained) metadata when the writer activates;
    // failures are recorded in diagnostics, never thrown: metadata publication is auxiliary
    MetaDataPublisher metaDataPublisher = group.connectionRuntime().metaDataPublisher();
    if (metaDataPublisher != null) {
      metaDataPublisher.onWriterActivated(group, this);
    }
  }

  @Override
  void deactivate() {
    MetaDataPublisher metaDataPublisher = group.connectionRuntime().metaDataPublisher();
    if (metaDataPublisher != null) {
      metaDataPublisher.onWriterDeactivated(this);
    }
  }

  /** Release all resources of this runtime. */
  void dispose() {
    MetaDataPublisher metaDataPublisher = group.connectionRuntime().metaDataPublisher();
    if (metaDataPublisher != null) {
      metaDataPublisher.onWriterDeactivated(this);
    }
  }

  /** Create a key frame draft from the current source snapshot. Publish task thread only. */
  DataSetMessageDraft createDataDraft(DateTime timestamp, long nowNanos) {
    List<DataValue> values = readSnapshotValues();

    lastSentNanos = nowNanos;

    return new DataSetMessageDraft(
        config,
        nextSequenceNumber(),
        includeTimestamp() ? timestamp : null,
        includeStatus() ? StatusCode.GOOD : null,
        configurationVersion,
        false,
        values,
        metaData);
  }

  /**
   * Create a keep-alive draft. Carries the next expected sequence number without incrementing the
   * counter, per Part 14 §7.2.3. Publish task thread only.
   */
  DataSetMessageDraft createKeepAliveDraft(DateTime timestamp, long nowNanos) {
    lastSentNanos = nowNanos;

    return new DataSetMessageDraft(
        config,
        peekSequenceNumber(),
        includeTimestamp() ? timestamp : null,
        includeStatus() ? StatusCode.GOOD : null,
        configurationVersion,
        true,
        List.of(),
        metaData);
  }

  private List<DataValue> readSnapshotValues() {
    int fieldCount = readContext.fields().size();

    PublishedDataSetSource source = service.getSource(config.getDataSet());

    if (source == null) {
      service
          .getDiagnostics()
          .sourceError(
              path(),
              new StatusCode(StatusCodes.Bad_ConfigurationError),
              "no PublishedDataSetSource bound for PublishedDataSet '%s'"
                  .formatted(config.getDataSet().name()),
              null);

      return badValues(fieldCount);
    }

    try {
      DataSetSnapshot snapshot = source.read(readContext);
      List<DataValue> values = snapshot.values();

      if (values.size() != fieldCount) {
        service
            .getDiagnostics()
            .sourceError(
                path(),
                new StatusCode(StatusCodes.Bad_InternalError),
                "snapshot for PublishedDataSet '%s' has %d values, expected %d"
                    .formatted(config.getDataSet().name(), values.size(), fieldCount),
                null);

        return badValues(fieldCount);
      }

      return values;
    } catch (Exception e) {
      service
          .getDiagnostics()
          .sourceError(
              path(),
              UaException.extractStatusCode(e)
                  .orElse(new StatusCode(StatusCodes.Bad_InternalError)),
              "PublishedDataSetSource for '%s' failed: %s"
                  .formatted(config.getDataSet().name(), e.getMessage()),
              e);

      return badValues(fieldCount);
    }
  }

  private static List<DataValue> badValues(int fieldCount) {
    return Collections.nCopies(fieldCount, new DataValue(StatusCodes.Bad_InternalError));
  }

  private boolean includeTimestamp() {
    return config.getSettings() instanceof UadpDataSetWriterSettings settings
        && settings.getDataSetMessageContentMask().getTimestamp();
  }

  private boolean includeStatus() {
    return config.getSettings() instanceof UadpDataSetWriterSettings settings
        && settings.getDataSetMessageContentMask().getStatus();
  }

  private UShort nextSequenceNumber() {
    int value = sequenceNumber;
    sequenceNumber = (value + 1) & 0xFFFF;
    return ushort(value);
  }

  private UShort peekSequenceNumber() {
    return ushort(sequenceNumber);
  }
}

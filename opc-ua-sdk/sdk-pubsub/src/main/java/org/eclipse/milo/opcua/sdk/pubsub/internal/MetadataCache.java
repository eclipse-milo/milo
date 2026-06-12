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

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubHandle;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetMetaDataConfig;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.FieldMetaData;
import org.jspecify.annotations.Nullable;

/**
 * Per-reader storage for DataSetMetaData discovered from the wire (metadata announcement messages).
 *
 * <p>Entries are keyed by reader handle (identity), so entries for readers replaced or removed by
 * reconfiguration are dropped along with their handles. Whether discovered metadata is stored at
 * all is the dispatcher's policy decision; this class is just the store and the converter.
 */
final class MetadataCache {

  private final ConcurrentMap<PubSubHandle, DataSetMetaDataConfig> discovered =
      new ConcurrentHashMap<>();

  @Nullable DataSetMetaDataConfig getDiscovered(PubSubHandle reader) {
    return discovered.get(reader);
  }

  void putDiscovered(PubSubHandle reader, DataSetMetaDataConfig metaData) {
    discovered.put(reader, metaData);
  }

  void remove(PubSubHandle reader) {
    discovered.remove(reader);
  }

  /**
   * Convert wire metadata to the config-model representation used for field naming and version
   * checks.
   *
   * @throws org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfigValidationException if the
   *     announced metadata is structurally invalid, e.g. duplicate field names or ids.
   */
  static DataSetMetaDataConfig fromDataType(DataSetMetaDataType metaData) {
    String name = metaData.getName() != null ? metaData.getName() : "";

    DataSetMetaDataConfig.Builder builder = DataSetMetaDataConfig.builder(name);

    FieldMetaData[] fields = metaData.getFields();
    if (fields != null) {
      int index = 0;
      for (FieldMetaData field : fields) {
        String fieldName = field.getName() != null ? field.getName() : "Field_" + index;
        builder.field(
            new DataSetMetaDataConfig.Field(
                fieldName,
                field.getDataType(),
                field.getDataSetFieldId(),
                field.getValueRank(),
                field.getArrayDimensions()));
        index++;
      }
    }

    UUID dataSetClassId = metaData.getDataSetClassId();
    if (dataSetClassId != null
        && (dataSetClassId.getMostSignificantBits() != 0L
            || dataSetClassId.getLeastSignificantBits() != 0L)) {
      builder.dataSetClassId(dataSetClassId);
    }

    ConfigurationVersionDataType version = metaData.getConfigurationVersion();
    if (version != null) {
      builder.configurationVersion(version.getMajorVersion(), version.getMinorVersion());
    }

    return builder.build();
  }
}

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

import static org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfigMapperUtil.MILO_SOURCE_KEY;
import static org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfigMapperUtil.NULL_UUID;
import static org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfigMapperUtil.deriveBuiltInType;
import static org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfigMapperUtil.toKeyValuePairs;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetFieldFlags;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.FieldMetaData;

/**
 * Derives the Part 14 {@link DataSetMetaDataType} describing a {@link PublishedDataSetConfig}.
 *
 * <p>This class is public only so the runtime engine (the UADP discovery responder) can reach it
 * from another package; it is an internal seam, not part of the supported API. Applications obtain
 * the same metadata as part of {@link PubSubConfig#toDataType(NamespaceTable)}.
 *
 * <p>This is the single source of truth for published dataset metadata derivation: the config
 * mapper builds the {@code PublishedDataSetDataType.dataSetMetaData} emitted by {@link
 * PubSubConfig#toDataType(NamespaceTable)} through this class.
 */
public final class DataSetMetaDataMapper {

  private DataSetMetaDataMapper() {}

  /**
   * Derive the {@link DataSetMetaDataType} describing {@code dataSet}.
   *
   * <p>With {@code stripMiloSourceKey = false} the result is identical to the {@code
   * dataSetMetaData} of the {@code PublishedDataSetDataType} emitted for {@code dataSet} by {@link
   * PubSubConfig#toDataType(NamespaceTable)}. With {@code stripMiloSourceKey = true} the reserved
   * {@code 0:MiloSourceKey} property, under which a {@link KeyFieldAddress} source round-trips
   * through the Part 14 configuration model, is omitted from {@link FieldMetaData} properties:
   * discovery announcements strip it because a remote subscriber consuming announced metadata never
   * runs the Milo config mapping, and the reserved name is not a real namespace 0 property
   * BrowseName. User-authored field properties are included either way.
   *
   * <p>The metadata configuration version is the dataset's {@code (major, minor)} configuration
   * version. Per OPC UA 10000-14 §7.2.4.6.4 the ConfigurationVersion in DataSetMessage headers
   * shall match the ConfigurationVersion in the DataSetMetaData; the dataset writer runtime stamps
   * headers with the version read from the same {@link PublishedDataSetConfig} getters, so callers
   * must derive from the same (live) config generation the writers read, not from a stale snapshot.
   *
   * <p>The emitted {@link DataSetMetaDataType} carries {@code namespaces = null}, so {@link
   * FieldMetaData} dataType NodeIds outside namespace 0 use publisher-local namespace indexes that
   * a remote subscriber consuming announced metadata cannot resolve. This is a known limitation of
   * the discovery announcement path in this version; namespace-0 data types (the common case) are
   * unaffected.
   *
   * @param dataSet the {@link PublishedDataSetConfig} to derive metadata for.
   * @param stripMiloSourceKey {@code true} to omit the reserved {@code 0:MiloSourceKey} property
   *     from field properties.
   * @return the derived {@link DataSetMetaDataType}.
   */
  public static DataSetMetaDataType toDataSetMetaDataType(
      PublishedDataSetConfig dataSet, boolean stripMiloSourceKey) {

    List<FieldDefinition> fields = dataSet.getFields();
    FieldMetaData[] fieldMetaData = new FieldMetaData[fields.size()];

    for (int i = 0; i < fields.size(); i++) {
      FieldDefinition field = fields.get(i);

      Map<QualifiedName, Variant> properties = new LinkedHashMap<>(field.getProperties());

      if (!stripMiloSourceKey && field.getSource() instanceof KeyFieldAddress key) {
        properties.put(MILO_SOURCE_KEY, Variant.ofString(key.key()));
      }

      fieldMetaData[i] =
          new FieldMetaData(
              field.getName(),
              LocalizedText.NULL_VALUE,
              field.isPromoted()
                  ? DataSetFieldFlags.of(DataSetFieldFlags.Field.PromotedField)
                  : DataSetFieldFlags.of(),
              deriveBuiltInType(field.getDataType()),
              field.getDataType(),
              field.getValueRank(),
              field.getArrayDimensions(),
              uint(0),
              field.getDataSetFieldId(),
              toKeyValuePairs(properties));
    }

    return new DataSetMetaDataType(
        null,
        null,
        null,
        null,
        dataSet.getName(),
        LocalizedText.NULL_VALUE,
        fieldMetaData.length == 0 ? null : fieldMetaData,
        NULL_UUID,
        new ConfigurationVersionDataType(
            dataSet.getConfigurationVersionMajor(), dataSet.getConfigurationVersionMinor()));
  }
}

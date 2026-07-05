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

import java.util.List;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetReaderDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetWriterDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfiguration2DataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConnectionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishedDataSetDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.ReaderGroupDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.SecurityGroupDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.StandaloneSubscribedDataSetDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.WriterGroupDataType;

/**
 * Maps single Part 14 configuration elements to their config-model counterparts: the per-element
 * entry points behind the CloseAndUpdate element operations (Part 14 §9.1.3.7.3), also usable to
 * import individual elements from foreign configuration files.
 *
 * <p>Each method maps its element exactly like whole-config {@link
 * PubSubConfig#fromDataType(PubSubConfiguration2DataType, NamespaceTable)} maps the same element,
 * including null tolerance, normalizations, and the raw escape hatches — see {@link
 * PubSubConfigMapper} for the mapping contract.
 *
 * <p>Wire {@code SecurityGroupId} references are resolved against the supplied {@code
 * securityGroups} — for CloseAndUpdate, the post-apply security-group set: the live groups after
 * removals, plus the SecurityGroup additions from the same call. An id that matches no supplied
 * group falls back to a {@link SecurityGroupRef} carrying the wire id itself, which fails {@link
 * PubSubConfig} builder validation unless it names a configured group. Element kinds that carry no
 * security-group references take the list anyway, for call-site uniformity, and ignore it.
 *
 * <p>Mapping failures throw {@link PubSubConfigValidationException}; because a single element has
 * no configuration around it, the message locates the offending element under the generic parent
 * context {@code "file"} instead of a full configuration path.
 */
public final class PubSubConfigElements {

  /** The generic parent-path context used in per-element error messages. */
  private static final String PARENT_PATH = "file";

  private PubSubConfigElements() {}

  /**
   * Map a single {@link PubSubConnectionDataType}, including its writer and reader groups, to a
   * {@link PubSubConnectionConfig}.
   *
   * @param value the connection element to map.
   * @param namespaceTable the table NodeId namespace indices are resolved against.
   * @param securityGroups the security groups the element's {@code SecurityGroupId} references
   *     resolve against.
   * @return the mapped {@link PubSubConnectionConfig}.
   * @throws PubSubConfigValidationException if the element cannot be mapped, e.g. a required field
   *     is missing or a NodeId namespace index is not present in {@code namespaceTable}.
   */
  public static PubSubConnectionConfig mapConnection(
      PubSubConnectionDataType value,
      NamespaceTable namespaceTable,
      List<SecurityGroupConfig> securityGroups) {

    return new DataTypeToConfigMapper(namespaceTable, securityGroups).mapConnection(value);
  }

  /**
   * Map a single {@link WriterGroupDataType}, including its dataset writers, to a {@link
   * WriterGroupConfig}.
   *
   * @param value the writer group element to map.
   * @param namespaceTable the table NodeId namespace indices are resolved against.
   * @param securityGroups the security groups the element's {@code SecurityGroupId} references
   *     resolve against.
   * @return the mapped {@link WriterGroupConfig}.
   * @throws PubSubConfigValidationException if the element cannot be mapped, e.g. a required field
   *     is missing or a NodeId namespace index is not present in {@code namespaceTable}.
   */
  public static WriterGroupConfig mapWriterGroup(
      WriterGroupDataType value,
      NamespaceTable namespaceTable,
      List<SecurityGroupConfig> securityGroups) {

    return new DataTypeToConfigMapper(namespaceTable, securityGroups)
        .mapWriterGroup(value, PARENT_PATH);
  }

  /**
   * Map a single {@link ReaderGroupDataType}, including its dataset readers, to a {@link
   * ReaderGroupConfig}.
   *
   * @param value the reader group element to map.
   * @param namespaceTable the table NodeId namespace indices are resolved against.
   * @param securityGroups the security groups the element's {@code SecurityGroupId} references
   *     resolve against.
   * @return the mapped {@link ReaderGroupConfig}.
   * @throws PubSubConfigValidationException if the element cannot be mapped, e.g. a required field
   *     is missing or a NodeId namespace index is not present in {@code namespaceTable}.
   */
  public static ReaderGroupConfig mapReaderGroup(
      ReaderGroupDataType value,
      NamespaceTable namespaceTable,
      List<SecurityGroupConfig> securityGroups) {

    return new DataTypeToConfigMapper(namespaceTable, securityGroups)
        .mapReaderGroup(value, PARENT_PATH);
  }

  /**
   * Map a single {@link DataSetWriterDataType} to a {@link DataSetWriterConfig}.
   *
   * @param value the dataset writer element to map.
   * @param namespaceTable the table NodeId namespace indices are resolved against.
   * @param securityGroups ignored (dataset writers carry no security-group references); accepted
   *     for call-site uniformity.
   * @return the mapped {@link DataSetWriterConfig}.
   * @throws PubSubConfigValidationException if the element cannot be mapped, e.g. its required
   *     {@code dataSetName} is missing.
   */
  public static DataSetWriterConfig mapDataSetWriter(
      DataSetWriterDataType value,
      NamespaceTable namespaceTable,
      List<SecurityGroupConfig> securityGroups) {

    return new DataTypeToConfigMapper(namespaceTable, securityGroups)
        .mapDataSetWriter(value, PARENT_PATH);
  }

  /**
   * Map a single {@link DataSetReaderDataType} to a {@link DataSetReaderConfig}.
   *
   * @param value the dataset reader element to map.
   * @param namespaceTable the table NodeId namespace indices are resolved against.
   * @param securityGroups the security groups the element's {@code SecurityGroupId} references
   *     (reader-level security overrides) resolve against.
   * @return the mapped {@link DataSetReaderConfig}.
   * @throws PubSubConfigValidationException if the element cannot be mapped, e.g. a required field
   *     is missing or a NodeId namespace index is not present in {@code namespaceTable}.
   */
  public static DataSetReaderConfig mapDataSetReader(
      DataSetReaderDataType value,
      NamespaceTable namespaceTable,
      List<SecurityGroupConfig> securityGroups) {

    return new DataTypeToConfigMapper(namespaceTable, securityGroups)
        .mapDataSetReader(value, PARENT_PATH);
  }

  /**
   * Map a single {@link PublishedDataSetDataType} to a {@link PublishedDataSetConfig}.
   *
   * @param value the published dataset element to map.
   * @param namespaceTable the table NodeId namespace indices are resolved against.
   * @param securityGroups ignored (published datasets carry no security-group references); accepted
   *     for call-site uniformity.
   * @return the mapped {@link PublishedDataSetConfig}.
   * @throws PubSubConfigValidationException if the element cannot be mapped, e.g. its dataset
   *     source is neither {@code PublishedDataItems} nor {@code PublishedEvents}, its {@code
   *     selectedFields} do not match its metadata fields, or a NodeId namespace index is not
   *     present in {@code namespaceTable}.
   */
  public static PublishedDataSetConfig mapPublishedDataSet(
      PublishedDataSetDataType value,
      NamespaceTable namespaceTable,
      List<SecurityGroupConfig> securityGroups) {

    return new DataTypeToConfigMapper(namespaceTable, securityGroups).mapPublishedDataSet(value);
  }

  /**
   * Map a single {@link StandaloneSubscribedDataSetDataType} to a {@link
   * StandaloneSubscribedDataSetConfig}.
   *
   * @param value the standalone subscribed dataset element to map.
   * @param namespaceTable the table NodeId namespace indices are resolved against.
   * @param securityGroups ignored (standalone subscribed datasets carry no security-group
   *     references); accepted for call-site uniformity.
   * @return the mapped {@link StandaloneSubscribedDataSetConfig}.
   * @throws PubSubConfigValidationException if the element cannot be mapped, e.g. its required
   *     metadata or subscribed dataset is missing.
   */
  public static StandaloneSubscribedDataSetConfig mapStandaloneSubscribedDataSet(
      StandaloneSubscribedDataSetDataType value,
      NamespaceTable namespaceTable,
      List<SecurityGroupConfig> securityGroups) {

    return new DataTypeToConfigMapper(namespaceTable, securityGroups)
        .mapStandaloneSubscribedDataSet(value);
  }

  /**
   * Map a single {@link SecurityGroupDataType} to a {@link SecurityGroupConfig}.
   *
   * <p>Security groups need no resolution context of their own: they are the elements other
   * elements' {@code SecurityGroupId} references resolve against.
   *
   * @param value the security group element to map.
   * @return the mapped {@link SecurityGroupConfig}.
   * @throws PubSubConfigValidationException if the element cannot be mapped, e.g. its name is
   *     missing.
   */
  public static SecurityGroupConfig mapSecurityGroup(SecurityGroupDataType value) {
    return DataTypeToConfigMapper.mapSecurityGroup(value);
  }
}

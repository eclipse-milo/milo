/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.model.objects;

import java.util.UUID;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.2.1">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.2.1</a>
 */
public interface PublishedDataSetType extends BaseObjectType {
  QualifiedProperty<ConfigurationVersionDataType> CONFIGURATION_VERSION =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ConfigurationVersion",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=14593"),
          -1,
          ConfigurationVersionDataType.class);

  QualifiedProperty<DataSetMetaDataType> DATA_SET_META_DATA =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DataSetMetaData",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=14523"),
          -1,
          DataSetMetaDataType.class);

  QualifiedProperty<UUID> DATA_SET_CLASS_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DataSetClassId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=14"),
          -1,
          UUID.class);

  QualifiedProperty<Boolean> CYCLIC_DATA_SET =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "CyclicDataSet",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  ConfigurationVersionDataType getConfigurationVersion();

  void setConfigurationVersion(ConfigurationVersionDataType value);

  PropertyType getConfigurationVersionNode();

  DataSetMetaDataType getDataSetMetaData();

  void setDataSetMetaData(DataSetMetaDataType value);

  PropertyType getDataSetMetaDataNode();

  UUID getDataSetClassId();

  void setDataSetClassId(UUID value);

  PropertyType getDataSetClassIdNode();

  Boolean getCyclicDataSet();

  void setCyclicDataSet(Boolean value);

  PropertyType getCyclicDataSetNode();

  ExtensionFieldsType getExtensionFieldsNode();
}

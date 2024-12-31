/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.5">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.5</a>
 */
public interface StandaloneSubscribedDataSetType extends BaseObjectType {
  QualifiedProperty<DataSetMetaDataType> DATA_SET_META_DATA =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DataSetMetaData",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=14523"),
          -1,
          DataSetMetaDataType.class);

  QualifiedProperty<Boolean> IS_CONNECTED =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "IsConnected",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  DataSetMetaDataType getDataSetMetaData();

  void setDataSetMetaData(DataSetMetaDataType value);

  PropertyType getDataSetMetaDataNode();

  Boolean getIsConnected();

  void setIsConnected(Boolean value);

  PropertyType getIsConnectedNode();

  SubscribedDataSetType getSubscribedDataSetNode();
}

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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetFieldContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.KeyValuePair;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.7/#9.1.7.2">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.7/#9.1.7.2</a>
 */
public interface DataSetWriterType extends BaseObjectType {
  QualifiedProperty<UShort> DATA_SET_WRITER_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DataSetWriterId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=5"),
          -1,
          UShort.class);

  QualifiedProperty<DataSetFieldContentMask> DATA_SET_FIELD_CONTENT_MASK =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DataSetFieldContentMask",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=15583"),
          -1,
          DataSetFieldContentMask.class);

  QualifiedProperty<UInteger> KEY_FRAME_COUNT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "KeyFrameCount",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<KeyValuePair[]> DATA_SET_WRITER_PROPERTIES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DataSetWriterProperties",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=14533"),
          1,
          KeyValuePair[].class);

  UShort getDataSetWriterId();

  void setDataSetWriterId(UShort value);

  PropertyType getDataSetWriterIdNode();

  DataSetFieldContentMask getDataSetFieldContentMask();

  void setDataSetFieldContentMask(DataSetFieldContentMask value);

  PropertyType getDataSetFieldContentMaskNode();

  UInteger getKeyFrameCount();

  void setKeyFrameCount(UInteger value);

  PropertyType getKeyFrameCountNode();

  KeyValuePair[] getDataSetWriterProperties();

  void setDataSetWriterProperties(KeyValuePair[] value);

  PropertyType getDataSetWriterPropertiesNode();

  DataSetWriterTransportType getTransportSettingsNode();

  DataSetWriterMessageType getMessageSettingsNode();

  PubSubStatusType getStatusNode();

  PubSubDiagnosticsDataSetWriterType getDiagnosticsNode();
}

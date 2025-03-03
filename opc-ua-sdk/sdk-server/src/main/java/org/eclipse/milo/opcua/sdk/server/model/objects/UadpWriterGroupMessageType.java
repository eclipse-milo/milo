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

import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.DataSetOrderingType;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpNetworkMessageContentMask;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.2.1/#9.2.1.1">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.2.1/#9.2.1.1</a>
 */
public interface UadpWriterGroupMessageType extends WriterGroupMessageType {
  QualifiedProperty<UInteger> GROUP_VERSION =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "GroupVersion",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=20998"),
          -1,
          UInteger.class);

  QualifiedProperty<DataSetOrderingType> DATA_SET_ORDERING =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DataSetOrdering",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=20408"),
          -1,
          DataSetOrderingType.class);

  QualifiedProperty<UadpNetworkMessageContentMask> NETWORK_MESSAGE_CONTENT_MASK =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "NetworkMessageContentMask",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=15642"),
          -1,
          UadpNetworkMessageContentMask.class);

  QualifiedProperty<Double> SAMPLING_OFFSET =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SamplingOffset",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  QualifiedProperty<Double[]> PUBLISHING_OFFSET =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "PublishingOffset",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          1,
          Double[].class);

  UInteger getGroupVersion();

  void setGroupVersion(UInteger value);

  PropertyType getGroupVersionNode();

  DataSetOrderingType getDataSetOrdering();

  void setDataSetOrdering(DataSetOrderingType value);

  PropertyType getDataSetOrderingNode();

  UadpNetworkMessageContentMask getNetworkMessageContentMask();

  void setNetworkMessageContentMask(UadpNetworkMessageContentMask value);

  PropertyType getNetworkMessageContentMaskNode();

  Double getSamplingOffset();

  void setSamplingOffset(Double value);

  PropertyType getSamplingOffsetNode();

  Double[] getPublishingOffset();

  void setPublishingOffset(Double[] value);

  PropertyType getPublishingOffsetNode();
}

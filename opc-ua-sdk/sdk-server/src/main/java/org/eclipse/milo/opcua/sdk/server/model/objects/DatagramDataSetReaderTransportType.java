/*
 * Copyright (c) 2026 the Eclipse Milo Authors
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
import org.eclipse.milo.opcua.stack.core.types.structured.ReceiveQosDataType;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.3.1/#9.3.1.4">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.3.1/#9.3.1.4</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface DatagramDataSetReaderTransportType extends DataSetReaderTransportType {
  QualifiedProperty<String> QOS_CATEGORY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "QosCategory",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<ReceiveQosDataType[]> DATAGRAM_QOS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DatagramQos",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=23608"),
          1,
          ReceiveQosDataType[].class);

  QualifiedProperty<String> TOPIC =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Topic",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  /** Gets the existing node's local value. */
  @Nullable String getQosCategory();

  /** Sets the existing node's local value. */
  void setQosCategory(@Nullable String value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getQosCategoryNode();

  /** Gets the existing node's local value. */
  @Nullable ReceiveQosDataType @Nullable [] getDatagramQos();

  /** Sets the existing node's local value. */
  void setDatagramQos(@Nullable ReceiveQosDataType @Nullable [] value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getDatagramQosNode();

  /** Gets the existing node's local value. */
  @Nullable String getTopic();

  /** Sets the existing node's local value. */
  void setTopic(@Nullable String value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getTopicNode();

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable NetworkAddressType getAddressNode();
}

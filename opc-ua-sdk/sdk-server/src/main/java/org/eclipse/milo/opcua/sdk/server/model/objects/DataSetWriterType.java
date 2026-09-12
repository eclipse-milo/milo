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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetFieldContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.KeyValuePair;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.7/#9.1.7.2">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.7/#9.1.7.2</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
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

  /** Gets the existing node's local value. */
  @Nullable UShort getDataSetWriterId();

  /** Sets the existing node's local value. */
  void setDataSetWriterId(@Nullable UShort value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getDataSetWriterIdNode();

  /** Gets the existing node's local value. */
  @Nullable DataSetFieldContentMask getDataSetFieldContentMask();

  /** Sets the existing node's local value. */
  void setDataSetFieldContentMask(@Nullable DataSetFieldContentMask value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getDataSetFieldContentMaskNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getKeyFrameCount();

  /** Sets the existing node's local value. */
  void setKeyFrameCount(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getKeyFrameCountNode();

  /** Gets the existing node's local value. */
  @Nullable KeyValuePair @Nullable [] getDataSetWriterProperties();

  /** Sets the existing node's local value. */
  void setDataSetWriterProperties(@Nullable KeyValuePair @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getDataSetWriterPropertiesNode();

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable DataSetWriterTransportType getTransportSettingsNode();

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable DataSetWriterMessageType getMessageSettingsNode();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PubSubStatusType getStatusNode();

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PubSubDiagnosticsDataSetWriterType getDiagnosticsNode();
}

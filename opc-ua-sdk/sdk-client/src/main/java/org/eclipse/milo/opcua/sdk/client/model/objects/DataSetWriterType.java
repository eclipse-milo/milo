/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
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
  @Nullable UShort getDataSetWriterId() throws UaException;

  /** Sets the existing node's local value. */
  void setDataSetWriterId(@Nullable UShort value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UShort readDataSetWriterId() throws UaException;

  /** Writes the value remotely. */
  void writeDataSetWriterId(@Nullable UShort value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UShort> readDataSetWriterIdAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeDataSetWriterIdAsync(@Nullable UShort value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getDataSetWriterIdNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getDataSetWriterIdNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable DataSetFieldContentMask getDataSetFieldContentMask() throws UaException;

  /** Sets the existing node's local value. */
  void setDataSetFieldContentMask(@Nullable DataSetFieldContentMask value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable DataSetFieldContentMask readDataSetFieldContentMask() throws UaException;

  /** Writes the value remotely. */
  void writeDataSetFieldContentMask(@Nullable DataSetFieldContentMask value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable DataSetFieldContentMask> readDataSetFieldContentMaskAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeDataSetFieldContentMaskAsync(
      @Nullable DataSetFieldContentMask value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getDataSetFieldContentMaskNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getDataSetFieldContentMaskNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getKeyFrameCount() throws UaException;

  /** Sets the existing node's local value. */
  void setKeyFrameCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readKeyFrameCount() throws UaException;

  /** Writes the value remotely. */
  void writeKeyFrameCount(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readKeyFrameCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeKeyFrameCountAsync(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getKeyFrameCountNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getKeyFrameCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable KeyValuePair @Nullable [] getDataSetWriterProperties() throws UaException;

  /** Sets the existing node's local value. */
  void setDataSetWriterProperties(@Nullable KeyValuePair @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable KeyValuePair @Nullable [] readDataSetWriterProperties() throws UaException;

  /** Writes the value remotely. */
  void writeDataSetWriterProperties(@Nullable KeyValuePair @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable KeyValuePair @Nullable []>
      readDataSetWriterPropertiesAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeDataSetWriterPropertiesAsync(
      @Nullable KeyValuePair @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getDataSetWriterPropertiesNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getDataSetWriterPropertiesNodeAsync();

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable DataSetWriterTransportType getTransportSettingsNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable DataSetWriterTransportType> getTransportSettingsNodeAsync();

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable DataSetWriterMessageType getMessageSettingsNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable DataSetWriterMessageType> getMessageSettingsNodeAsync();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PubSubStatusType getStatusNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PubSubStatusType> getStatusNodeAsync();

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PubSubDiagnosticsDataSetWriterType getDiagnosticsNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PubSubDiagnosticsDataSetWriterType>
      getDiagnosticsNodeAsync();
}

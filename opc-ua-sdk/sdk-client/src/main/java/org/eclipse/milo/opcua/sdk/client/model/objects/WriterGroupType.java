/*
 * Copyright (c) 2025 the Eclipse Milo Authors
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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.3">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.3</a>
 */
public interface WriterGroupType extends PubSubGroupType {
  QualifiedProperty<UShort> WRITER_GROUP_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "WriterGroupId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=5"),
          -1,
          UShort.class);

  QualifiedProperty<Double> PUBLISHING_INTERVAL =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "PublishingInterval",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  QualifiedProperty<Double> KEEP_ALIVE_TIME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "KeepAliveTime",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  QualifiedProperty<UByte> PRIORITY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Priority",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=3"),
          -1,
          UByte.class);

  QualifiedProperty<String[]> LOCALE_IDS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "LocaleIds",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=295"),
          1,
          String[].class);

  QualifiedProperty<String> HEADER_LAYOUT_URI =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "HeaderLayoutUri",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  /**
   * Get the local value of the WriterGroupId Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the WriterGroupId Node.
   * @throws UaException if an error occurs creating or getting the WriterGroupId Node.
   */
  UShort getWriterGroupId() throws UaException;

  /**
   * Set the local value of the WriterGroupId Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the WriterGroupId Node.
   * @throws UaException if an error occurs creating or getting the WriterGroupId Node.
   */
  void setWriterGroupId(UShort value) throws UaException;

  /**
   * Read the value of the WriterGroupId Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link UShort} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UShort readWriterGroupId() throws UaException;

  /**
   * Write a new value for the WriterGroupId Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link UShort} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeWriterGroupId(UShort value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readWriterGroupId}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UShort> readWriterGroupIdAsync();

  /**
   * An asynchronous implementation of {@link #writeWriterGroupId}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeWriterGroupIdAsync(UShort value);

  /**
   * Get the WriterGroupId {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the WriterGroupId {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getWriterGroupIdNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getWriterGroupIdNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getWriterGroupIdNodeAsync();

  /**
   * Get the local value of the PublishingInterval Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the PublishingInterval Node.
   * @throws UaException if an error occurs creating or getting the PublishingInterval Node.
   */
  Double getPublishingInterval() throws UaException;

  /**
   * Set the local value of the PublishingInterval Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the PublishingInterval Node.
   * @throws UaException if an error occurs creating or getting the PublishingInterval Node.
   */
  void setPublishingInterval(Double value) throws UaException;

  /**
   * Read the value of the PublishingInterval Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link Double} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Double readPublishingInterval() throws UaException;

  /**
   * Write a new value for the PublishingInterval Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link Double} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writePublishingInterval(Double value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readPublishingInterval}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Double> readPublishingIntervalAsync();

  /**
   * An asynchronous implementation of {@link #writePublishingInterval}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writePublishingIntervalAsync(Double value);

  /**
   * Get the PublishingInterval {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the PublishingInterval {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getPublishingIntervalNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getPublishingIntervalNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getPublishingIntervalNodeAsync();

  /**
   * Get the local value of the KeepAliveTime Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the KeepAliveTime Node.
   * @throws UaException if an error occurs creating or getting the KeepAliveTime Node.
   */
  Double getKeepAliveTime() throws UaException;

  /**
   * Set the local value of the KeepAliveTime Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the KeepAliveTime Node.
   * @throws UaException if an error occurs creating or getting the KeepAliveTime Node.
   */
  void setKeepAliveTime(Double value) throws UaException;

  /**
   * Read the value of the KeepAliveTime Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link Double} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Double readKeepAliveTime() throws UaException;

  /**
   * Write a new value for the KeepAliveTime Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link Double} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeKeepAliveTime(Double value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readKeepAliveTime}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Double> readKeepAliveTimeAsync();

  /**
   * An asynchronous implementation of {@link #writeKeepAliveTime}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeKeepAliveTimeAsync(Double value);

  /**
   * Get the KeepAliveTime {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the KeepAliveTime {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getKeepAliveTimeNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getKeepAliveTimeNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getKeepAliveTimeNodeAsync();

  /**
   * Get the local value of the Priority Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the Priority Node.
   * @throws UaException if an error occurs creating or getting the Priority Node.
   */
  UByte getPriority() throws UaException;

  /**
   * Set the local value of the Priority Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the Priority Node.
   * @throws UaException if an error occurs creating or getting the Priority Node.
   */
  void setPriority(UByte value) throws UaException;

  /**
   * Read the value of the Priority Node from the server and update the local value if the operation
   * succeeds.
   *
   * @return the {@link UByte} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UByte readPriority() throws UaException;

  /**
   * Write a new value for the Priority Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link UByte} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writePriority(UByte value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readPriority}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UByte> readPriorityAsync();

  /**
   * An asynchronous implementation of {@link #writePriority}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writePriorityAsync(UByte value);

  /**
   * Get the Priority {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the Priority {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getPriorityNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getPriorityNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getPriorityNodeAsync();

  /**
   * Get the local value of the LocaleIds Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the LocaleIds Node.
   * @throws UaException if an error occurs creating or getting the LocaleIds Node.
   */
  String[] getLocaleIds() throws UaException;

  /**
   * Set the local value of the LocaleIds Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the LocaleIds Node.
   * @throws UaException if an error occurs creating or getting the LocaleIds Node.
   */
  void setLocaleIds(String[] value) throws UaException;

  /**
   * Read the value of the LocaleIds Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link String[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String[] readLocaleIds() throws UaException;

  /**
   * Write a new value for the LocaleIds Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link String[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeLocaleIds(String[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readLocaleIds}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String[]> readLocaleIdsAsync();

  /**
   * An asynchronous implementation of {@link #writeLocaleIds}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeLocaleIdsAsync(String[] value);

  /**
   * Get the LocaleIds {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the LocaleIds {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getLocaleIdsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getLocaleIdsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getLocaleIdsNodeAsync();

  /**
   * Get the local value of the HeaderLayoutUri Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the HeaderLayoutUri Node.
   * @throws UaException if an error occurs creating or getting the HeaderLayoutUri Node.
   */
  String getHeaderLayoutUri() throws UaException;

  /**
   * Set the local value of the HeaderLayoutUri Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the HeaderLayoutUri Node.
   * @throws UaException if an error occurs creating or getting the HeaderLayoutUri Node.
   */
  void setHeaderLayoutUri(String value) throws UaException;

  /**
   * Read the value of the HeaderLayoutUri Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link String} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String readHeaderLayoutUri() throws UaException;

  /**
   * Write a new value for the HeaderLayoutUri Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link String} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeHeaderLayoutUri(String value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readHeaderLayoutUri}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String> readHeaderLayoutUriAsync();

  /**
   * An asynchronous implementation of {@link #writeHeaderLayoutUri}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeHeaderLayoutUriAsync(String value);

  /**
   * Get the HeaderLayoutUri {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the HeaderLayoutUri {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getHeaderLayoutUriNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getHeaderLayoutUriNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getHeaderLayoutUriNodeAsync();

  /**
   * Get the TransportSettings {@link WriterGroupTransportType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the TransportSettings {@link WriterGroupTransportType} Node, or {@code null} if it does
   *     not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  WriterGroupTransportType getTransportSettingsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getTransportSettingsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the WriterGroupTransportType Node
   *     or completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends WriterGroupTransportType> getTransportSettingsNodeAsync();

  /**
   * Get the MessageSettings {@link WriterGroupMessageType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the MessageSettings {@link WriterGroupMessageType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  WriterGroupMessageType getMessageSettingsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getMessageSettingsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the WriterGroupMessageType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends WriterGroupMessageType> getMessageSettingsNodeAsync();

  /**
   * Get the Diagnostics {@link PubSubDiagnosticsWriterGroupType} Node, or {@code null} if it does
   * not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the Diagnostics {@link PubSubDiagnosticsWriterGroupType} Node, or {@code null} if it
   *     does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PubSubDiagnosticsWriterGroupType getDiagnosticsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getDiagnosticsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the
   *     PubSubDiagnosticsWriterGroupType Node or completes exceptionally if an error occurs
   *     creating or getting the Node.
   */
  CompletableFuture<? extends PubSubDiagnosticsWriterGroupType> getDiagnosticsNodeAsync();
}

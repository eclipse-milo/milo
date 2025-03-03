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
import org.eclipse.milo.opcua.sdk.client.model.variables.AudioVariableType;
import org.eclipse.milo.opcua.sdk.client.model.variables.BaseDataVariableType;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.client.model.variables.TwoStateVariableType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.2">https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.2</a>
 */
public interface AlarmConditionType extends AcknowledgeableConditionType {
  QualifiedProperty<NodeId> INPUT_NODE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "InputNode",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17"),
          -1,
          NodeId.class);

  QualifiedProperty<Boolean> SUPPRESSED_OR_SHELVED =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SuppressedOrShelved",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<Double> MAX_TIME_SHELVED =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxTimeShelved",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  QualifiedProperty<Boolean> AUDIBLE_ENABLED =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "AudibleEnabled",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<Double> ON_DELAY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "OnDelay",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  QualifiedProperty<Double> OFF_DELAY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "OffDelay",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  QualifiedProperty<Double> RE_ALARM_TIME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ReAlarmTime",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  /**
   * Get the local value of the InputNode Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the InputNode Node.
   * @throws UaException if an error occurs creating or getting the InputNode Node.
   */
  NodeId getInputNode() throws UaException;

  /**
   * Set the local value of the InputNode Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the InputNode Node.
   * @throws UaException if an error occurs creating or getting the InputNode Node.
   */
  void setInputNode(NodeId value) throws UaException;

  /**
   * Read the value of the InputNode Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link NodeId} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  NodeId readInputNode() throws UaException;

  /**
   * Write a new value for the InputNode Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link NodeId} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeInputNode(NodeId value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readInputNode}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends NodeId> readInputNodeAsync();

  /**
   * An asynchronous implementation of {@link #writeInputNode}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeInputNodeAsync(NodeId value);

  /**
   * Get the InputNode {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the InputNode {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getInputNodeNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getInputNodeNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getInputNodeNodeAsync();

  /**
   * Get the local value of the SuppressedOrShelved Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the SuppressedOrShelved Node.
   * @throws UaException if an error occurs creating or getting the SuppressedOrShelved Node.
   */
  Boolean getSuppressedOrShelved() throws UaException;

  /**
   * Set the local value of the SuppressedOrShelved Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the SuppressedOrShelved Node.
   * @throws UaException if an error occurs creating or getting the SuppressedOrShelved Node.
   */
  void setSuppressedOrShelved(Boolean value) throws UaException;

  /**
   * Read the value of the SuppressedOrShelved Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link Boolean} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Boolean readSuppressedOrShelved() throws UaException;

  /**
   * Write a new value for the SuppressedOrShelved Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link Boolean} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeSuppressedOrShelved(Boolean value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readSuppressedOrShelved}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Boolean> readSuppressedOrShelvedAsync();

  /**
   * An asynchronous implementation of {@link #writeSuppressedOrShelved}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeSuppressedOrShelvedAsync(Boolean value);

  /**
   * Get the SuppressedOrShelved {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the SuppressedOrShelved {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getSuppressedOrShelvedNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSuppressedOrShelvedNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getSuppressedOrShelvedNodeAsync();

  /**
   * Get the local value of the MaxTimeShelved Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the MaxTimeShelved Node.
   * @throws UaException if an error occurs creating or getting the MaxTimeShelved Node.
   */
  Double getMaxTimeShelved() throws UaException;

  /**
   * Set the local value of the MaxTimeShelved Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the MaxTimeShelved Node.
   * @throws UaException if an error occurs creating or getting the MaxTimeShelved Node.
   */
  void setMaxTimeShelved(Double value) throws UaException;

  /**
   * Read the value of the MaxTimeShelved Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link Double} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Double readMaxTimeShelved() throws UaException;

  /**
   * Write a new value for the MaxTimeShelved Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link Double} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeMaxTimeShelved(Double value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readMaxTimeShelved}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Double> readMaxTimeShelvedAsync();

  /**
   * An asynchronous implementation of {@link #writeMaxTimeShelved}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeMaxTimeShelvedAsync(Double value);

  /**
   * Get the MaxTimeShelved {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the MaxTimeShelved {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getMaxTimeShelvedNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getMaxTimeShelvedNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getMaxTimeShelvedNodeAsync();

  /**
   * Get the local value of the AudibleEnabled Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the AudibleEnabled Node.
   * @throws UaException if an error occurs creating or getting the AudibleEnabled Node.
   */
  Boolean getAudibleEnabled() throws UaException;

  /**
   * Set the local value of the AudibleEnabled Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the AudibleEnabled Node.
   * @throws UaException if an error occurs creating or getting the AudibleEnabled Node.
   */
  void setAudibleEnabled(Boolean value) throws UaException;

  /**
   * Read the value of the AudibleEnabled Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link Boolean} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Boolean readAudibleEnabled() throws UaException;

  /**
   * Write a new value for the AudibleEnabled Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link Boolean} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeAudibleEnabled(Boolean value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readAudibleEnabled}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Boolean> readAudibleEnabledAsync();

  /**
   * An asynchronous implementation of {@link #writeAudibleEnabled}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeAudibleEnabledAsync(Boolean value);

  /**
   * Get the AudibleEnabled {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the AudibleEnabled {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getAudibleEnabledNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getAudibleEnabledNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getAudibleEnabledNodeAsync();

  /**
   * Get the local value of the OnDelay Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the OnDelay Node.
   * @throws UaException if an error occurs creating or getting the OnDelay Node.
   */
  Double getOnDelay() throws UaException;

  /**
   * Set the local value of the OnDelay Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the OnDelay Node.
   * @throws UaException if an error occurs creating or getting the OnDelay Node.
   */
  void setOnDelay(Double value) throws UaException;

  /**
   * Read the value of the OnDelay Node from the server and update the local value if the operation
   * succeeds.
   *
   * @return the {@link Double} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Double readOnDelay() throws UaException;

  /**
   * Write a new value for the OnDelay Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link Double} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeOnDelay(Double value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readOnDelay}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Double> readOnDelayAsync();

  /**
   * An asynchronous implementation of {@link #writeOnDelay}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeOnDelayAsync(Double value);

  /**
   * Get the OnDelay {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the OnDelay {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getOnDelayNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getOnDelayNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getOnDelayNodeAsync();

  /**
   * Get the local value of the OffDelay Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the OffDelay Node.
   * @throws UaException if an error occurs creating or getting the OffDelay Node.
   */
  Double getOffDelay() throws UaException;

  /**
   * Set the local value of the OffDelay Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the OffDelay Node.
   * @throws UaException if an error occurs creating or getting the OffDelay Node.
   */
  void setOffDelay(Double value) throws UaException;

  /**
   * Read the value of the OffDelay Node from the server and update the local value if the operation
   * succeeds.
   *
   * @return the {@link Double} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Double readOffDelay() throws UaException;

  /**
   * Write a new value for the OffDelay Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link Double} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeOffDelay(Double value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readOffDelay}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Double> readOffDelayAsync();

  /**
   * An asynchronous implementation of {@link #writeOffDelay}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeOffDelayAsync(Double value);

  /**
   * Get the OffDelay {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the OffDelay {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getOffDelayNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getOffDelayNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getOffDelayNodeAsync();

  /**
   * Get the local value of the ReAlarmTime Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ReAlarmTime Node.
   * @throws UaException if an error occurs creating or getting the ReAlarmTime Node.
   */
  Double getReAlarmTime() throws UaException;

  /**
   * Set the local value of the ReAlarmTime Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ReAlarmTime Node.
   * @throws UaException if an error occurs creating or getting the ReAlarmTime Node.
   */
  void setReAlarmTime(Double value) throws UaException;

  /**
   * Read the value of the ReAlarmTime Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link Double} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Double readReAlarmTime() throws UaException;

  /**
   * Write a new value for the ReAlarmTime Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link Double} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeReAlarmTime(Double value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readReAlarmTime}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Double> readReAlarmTimeAsync();

  /**
   * An asynchronous implementation of {@link #writeReAlarmTime}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeReAlarmTimeAsync(Double value);

  /**
   * Get the ReAlarmTime {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ReAlarmTime {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getReAlarmTimeNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getReAlarmTimeNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getReAlarmTimeNodeAsync();

  /**
   * Get the local value of the EnabledState Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the EnabledState Node.
   * @throws UaException if an error occurs creating or getting the EnabledState Node.
   */
  LocalizedText getEnabledState() throws UaException;

  /**
   * Set the local value of the EnabledState Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the EnabledState Node.
   * @throws UaException if an error occurs creating or getting the EnabledState Node.
   */
  void setEnabledState(LocalizedText value) throws UaException;

  /**
   * Read the value of the EnabledState Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link LocalizedText} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  LocalizedText readEnabledState() throws UaException;

  /**
   * Write a new value for the EnabledState Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link LocalizedText} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeEnabledState(LocalizedText value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readEnabledState}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends LocalizedText> readEnabledStateAsync();

  /**
   * An asynchronous implementation of {@link #writeEnabledState}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeEnabledStateAsync(LocalizedText value);

  /**
   * Get the EnabledState {@link TwoStateVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the EnabledState {@link TwoStateVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  TwoStateVariableType getEnabledStateNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getEnabledStateNode()}.
   *
   * @return a CompletableFuture that completes successfully with the TwoStateVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends TwoStateVariableType> getEnabledStateNodeAsync();

  /**
   * Get the local value of the ActiveState Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ActiveState Node.
   * @throws UaException if an error occurs creating or getting the ActiveState Node.
   */
  LocalizedText getActiveState() throws UaException;

  /**
   * Set the local value of the ActiveState Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ActiveState Node.
   * @throws UaException if an error occurs creating or getting the ActiveState Node.
   */
  void setActiveState(LocalizedText value) throws UaException;

  /**
   * Read the value of the ActiveState Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link LocalizedText} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  LocalizedText readActiveState() throws UaException;

  /**
   * Write a new value for the ActiveState Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link LocalizedText} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeActiveState(LocalizedText value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readActiveState}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends LocalizedText> readActiveStateAsync();

  /**
   * An asynchronous implementation of {@link #writeActiveState}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeActiveStateAsync(LocalizedText value);

  /**
   * Get the ActiveState {@link TwoStateVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ActiveState {@link TwoStateVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  TwoStateVariableType getActiveStateNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getActiveStateNode()}.
   *
   * @return a CompletableFuture that completes successfully with the TwoStateVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends TwoStateVariableType> getActiveStateNodeAsync();

  /**
   * Get the local value of the SuppressedState Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the SuppressedState Node.
   * @throws UaException if an error occurs creating or getting the SuppressedState Node.
   */
  LocalizedText getSuppressedState() throws UaException;

  /**
   * Set the local value of the SuppressedState Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the SuppressedState Node.
   * @throws UaException if an error occurs creating or getting the SuppressedState Node.
   */
  void setSuppressedState(LocalizedText value) throws UaException;

  /**
   * Read the value of the SuppressedState Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link LocalizedText} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  LocalizedText readSuppressedState() throws UaException;

  /**
   * Write a new value for the SuppressedState Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link LocalizedText} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeSuppressedState(LocalizedText value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readSuppressedState}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends LocalizedText> readSuppressedStateAsync();

  /**
   * An asynchronous implementation of {@link #writeSuppressedState}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeSuppressedStateAsync(LocalizedText value);

  /**
   * Get the SuppressedState {@link TwoStateVariableType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the SuppressedState {@link TwoStateVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  TwoStateVariableType getSuppressedStateNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSuppressedStateNode()}.
   *
   * @return a CompletableFuture that completes successfully with the TwoStateVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends TwoStateVariableType> getSuppressedStateNodeAsync();

  /**
   * Get the local value of the OutOfServiceState Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the OutOfServiceState Node.
   * @throws UaException if an error occurs creating or getting the OutOfServiceState Node.
   */
  LocalizedText getOutOfServiceState() throws UaException;

  /**
   * Set the local value of the OutOfServiceState Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the OutOfServiceState Node.
   * @throws UaException if an error occurs creating or getting the OutOfServiceState Node.
   */
  void setOutOfServiceState(LocalizedText value) throws UaException;

  /**
   * Read the value of the OutOfServiceState Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link LocalizedText} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  LocalizedText readOutOfServiceState() throws UaException;

  /**
   * Write a new value for the OutOfServiceState Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link LocalizedText} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeOutOfServiceState(LocalizedText value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readOutOfServiceState}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends LocalizedText> readOutOfServiceStateAsync();

  /**
   * An asynchronous implementation of {@link #writeOutOfServiceState}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeOutOfServiceStateAsync(LocalizedText value);

  /**
   * Get the OutOfServiceState {@link TwoStateVariableType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the OutOfServiceState {@link TwoStateVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  TwoStateVariableType getOutOfServiceStateNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getOutOfServiceStateNode()}.
   *
   * @return a CompletableFuture that completes successfully with the TwoStateVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends TwoStateVariableType> getOutOfServiceStateNodeAsync();

  /**
   * Get the ShelvingState {@link ShelvedStateMachineType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ShelvingState {@link ShelvedStateMachineType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  ShelvedStateMachineType getShelvingStateNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getShelvingStateNode()}.
   *
   * @return a CompletableFuture that completes successfully with the ShelvedStateMachineType Node
   *     or completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends ShelvedStateMachineType> getShelvingStateNodeAsync();

  /**
   * Get the local value of the AudibleSound Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the AudibleSound Node.
   * @throws UaException if an error occurs creating or getting the AudibleSound Node.
   */
  ByteString getAudibleSound() throws UaException;

  /**
   * Set the local value of the AudibleSound Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the AudibleSound Node.
   * @throws UaException if an error occurs creating or getting the AudibleSound Node.
   */
  void setAudibleSound(ByteString value) throws UaException;

  /**
   * Read the value of the AudibleSound Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link ByteString} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  ByteString readAudibleSound() throws UaException;

  /**
   * Write a new value for the AudibleSound Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link ByteString} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeAudibleSound(ByteString value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readAudibleSound}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends ByteString> readAudibleSoundAsync();

  /**
   * An asynchronous implementation of {@link #writeAudibleSound}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeAudibleSoundAsync(ByteString value);

  /**
   * Get the AudibleSound {@link AudioVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the AudibleSound {@link AudioVariableType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  AudioVariableType getAudibleSoundNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getAudibleSoundNode()}.
   *
   * @return a CompletableFuture that completes successfully with the AudioVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends AudioVariableType> getAudibleSoundNodeAsync();

  /**
   * Get the local value of the SilenceState Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the SilenceState Node.
   * @throws UaException if an error occurs creating or getting the SilenceState Node.
   */
  LocalizedText getSilenceState() throws UaException;

  /**
   * Set the local value of the SilenceState Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the SilenceState Node.
   * @throws UaException if an error occurs creating or getting the SilenceState Node.
   */
  void setSilenceState(LocalizedText value) throws UaException;

  /**
   * Read the value of the SilenceState Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link LocalizedText} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  LocalizedText readSilenceState() throws UaException;

  /**
   * Write a new value for the SilenceState Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link LocalizedText} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeSilenceState(LocalizedText value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readSilenceState}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends LocalizedText> readSilenceStateAsync();

  /**
   * An asynchronous implementation of {@link #writeSilenceState}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeSilenceStateAsync(LocalizedText value);

  /**
   * Get the SilenceState {@link TwoStateVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the SilenceState {@link TwoStateVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  TwoStateVariableType getSilenceStateNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSilenceStateNode()}.
   *
   * @return a CompletableFuture that completes successfully with the TwoStateVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends TwoStateVariableType> getSilenceStateNodeAsync();

  /**
   * Get the local value of the FirstInGroupFlag Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the FirstInGroupFlag Node.
   * @throws UaException if an error occurs creating or getting the FirstInGroupFlag Node.
   */
  Boolean getFirstInGroupFlag() throws UaException;

  /**
   * Set the local value of the FirstInGroupFlag Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the FirstInGroupFlag Node.
   * @throws UaException if an error occurs creating or getting the FirstInGroupFlag Node.
   */
  void setFirstInGroupFlag(Boolean value) throws UaException;

  /**
   * Read the value of the FirstInGroupFlag Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link Boolean} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Boolean readFirstInGroupFlag() throws UaException;

  /**
   * Write a new value for the FirstInGroupFlag Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link Boolean} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeFirstInGroupFlag(Boolean value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readFirstInGroupFlag}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Boolean> readFirstInGroupFlagAsync();

  /**
   * An asynchronous implementation of {@link #writeFirstInGroupFlag}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeFirstInGroupFlagAsync(Boolean value);

  /**
   * Get the FirstInGroupFlag {@link BaseDataVariableType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the FirstInGroupFlag {@link BaseDataVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getFirstInGroupFlagNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getFirstInGroupFlagNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getFirstInGroupFlagNodeAsync();

  /**
   * Get the FirstInGroup {@link AlarmGroupType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the FirstInGroup {@link AlarmGroupType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  AlarmGroupType getFirstInGroupNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getFirstInGroupNode()}.
   *
   * @return a CompletableFuture that completes successfully with the AlarmGroupType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends AlarmGroupType> getFirstInGroupNodeAsync();

  /**
   * Get the local value of the LatchedState Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the LatchedState Node.
   * @throws UaException if an error occurs creating or getting the LatchedState Node.
   */
  LocalizedText getLatchedState() throws UaException;

  /**
   * Set the local value of the LatchedState Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the LatchedState Node.
   * @throws UaException if an error occurs creating or getting the LatchedState Node.
   */
  void setLatchedState(LocalizedText value) throws UaException;

  /**
   * Read the value of the LatchedState Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link LocalizedText} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  LocalizedText readLatchedState() throws UaException;

  /**
   * Write a new value for the LatchedState Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link LocalizedText} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeLatchedState(LocalizedText value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readLatchedState}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends LocalizedText> readLatchedStateAsync();

  /**
   * An asynchronous implementation of {@link #writeLatchedState}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeLatchedStateAsync(LocalizedText value);

  /**
   * Get the LatchedState {@link TwoStateVariableType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the LatchedState {@link TwoStateVariableType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  TwoStateVariableType getLatchedStateNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getLatchedStateNode()}.
   *
   * @return a CompletableFuture that completes successfully with the TwoStateVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends TwoStateVariableType> getLatchedStateNodeAsync();

  /**
   * Get the local value of the ReAlarmRepeatCount Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ReAlarmRepeatCount Node.
   * @throws UaException if an error occurs creating or getting the ReAlarmRepeatCount Node.
   */
  Short getReAlarmRepeatCount() throws UaException;

  /**
   * Set the local value of the ReAlarmRepeatCount Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ReAlarmRepeatCount Node.
   * @throws UaException if an error occurs creating or getting the ReAlarmRepeatCount Node.
   */
  void setReAlarmRepeatCount(Short value) throws UaException;

  /**
   * Read the value of the ReAlarmRepeatCount Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link Short} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Short readReAlarmRepeatCount() throws UaException;

  /**
   * Write a new value for the ReAlarmRepeatCount Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link Short} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeReAlarmRepeatCount(Short value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readReAlarmRepeatCount}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Short> readReAlarmRepeatCountAsync();

  /**
   * An asynchronous implementation of {@link #writeReAlarmRepeatCount}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeReAlarmRepeatCountAsync(Short value);

  /**
   * Get the ReAlarmRepeatCount {@link BaseDataVariableType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ReAlarmRepeatCount {@link BaseDataVariableType} Node, or {@code null} if it does
   *     not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  BaseDataVariableType getReAlarmRepeatCountNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getReAlarmRepeatCountNode()}.
   *
   * @return a CompletableFuture that completes successfully with the BaseDataVariableType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends BaseDataVariableType> getReAlarmRepeatCountNodeAsync();
}

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

import com.digitalpetri.opcua.uanodeset.runtime.methods.MethodCallOptions;
import com.digitalpetri.opcua.uanodeset.runtime.methods.MethodCallResult;
import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.AudioVariableType;
import org.eclipse.milo.opcua.sdk.client.model.variables.BaseDataVariableType;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.client.model.variables.TwoStateVariableType;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.2">https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.2</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
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

  /** Gets the existing node's local value. */
  @Nullable NodeId getInputNode() throws UaException;

  /** Sets the existing node's local value. */
  void setInputNode(@Nullable NodeId value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable NodeId readInputNode() throws UaException;

  /** Writes the value remotely. */
  void writeInputNode(@Nullable NodeId value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable NodeId> readInputNodeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeInputNodeAsync(@Nullable NodeId value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getInputNodeNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getInputNodeNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Boolean getSuppressedOrShelved() throws UaException;

  /** Sets the existing node's local value. */
  void setSuppressedOrShelved(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Boolean readSuppressedOrShelved() throws UaException;

  /** Writes the value remotely. */
  void writeSuppressedOrShelved(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Boolean> readSuppressedOrShelvedAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSuppressedOrShelvedAsync(@Nullable Boolean value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getSuppressedOrShelvedNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getSuppressedOrShelvedNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Double getMaxTimeShelved() throws UaException;

  /** Sets the existing node's local value. */
  void setMaxTimeShelved(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Double readMaxTimeShelved() throws UaException;

  /** Writes the value remotely. */
  void writeMaxTimeShelved(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Double> readMaxTimeShelvedAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMaxTimeShelvedAsync(@Nullable Double value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMaxTimeShelvedNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getMaxTimeShelvedNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Boolean getAudibleEnabled() throws UaException;

  /** Sets the existing node's local value. */
  void setAudibleEnabled(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Boolean readAudibleEnabled() throws UaException;

  /** Writes the value remotely. */
  void writeAudibleEnabled(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Boolean> readAudibleEnabledAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeAudibleEnabledAsync(@Nullable Boolean value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getAudibleEnabledNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getAudibleEnabledNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Double getOnDelay() throws UaException;

  /** Sets the existing node's local value. */
  void setOnDelay(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Double readOnDelay() throws UaException;

  /** Writes the value remotely. */
  void writeOnDelay(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Double> readOnDelayAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeOnDelayAsync(@Nullable Double value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getOnDelayNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getOnDelayNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Double getOffDelay() throws UaException;

  /** Sets the existing node's local value. */
  void setOffDelay(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Double readOffDelay() throws UaException;

  /** Writes the value remotely. */
  void writeOffDelay(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Double> readOffDelayAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeOffDelayAsync(@Nullable Double value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getOffDelayNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getOffDelayNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Double getReAlarmTime() throws UaException;

  /** Sets the existing node's local value. */
  void setReAlarmTime(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Double readReAlarmTime() throws UaException;

  /** Writes the value remotely. */
  void writeReAlarmTime(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Double> readReAlarmTimeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeReAlarmTimeAsync(@Nullable Double value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getReAlarmTimeNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getReAlarmTimeNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable LocalizedText getEnabledState() throws UaException;

  /** Sets the existing node's local value. */
  void setEnabledState(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable LocalizedText readEnabledState() throws UaException;

  /** Writes the value remotely. */
  void writeEnabledState(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable LocalizedText> readEnabledStateAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeEnabledStateAsync(@Nullable LocalizedText value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  TwoStateVariableType getEnabledStateNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends TwoStateVariableType> getEnabledStateNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable LocalizedText getActiveState() throws UaException;

  /** Sets the existing node's local value. */
  void setActiveState(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable LocalizedText readActiveState() throws UaException;

  /** Writes the value remotely. */
  void writeActiveState(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable LocalizedText> readActiveStateAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeActiveStateAsync(@Nullable LocalizedText value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  TwoStateVariableType getActiveStateNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends TwoStateVariableType> getActiveStateNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable LocalizedText getSuppressedState() throws UaException;

  /** Sets the existing node's local value. */
  void setSuppressedState(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable LocalizedText readSuppressedState() throws UaException;

  /** Writes the value remotely. */
  void writeSuppressedState(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable LocalizedText> readSuppressedStateAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSuppressedStateAsync(@Nullable LocalizedText value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable TwoStateVariableType getSuppressedStateNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable TwoStateVariableType> getSuppressedStateNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable LocalizedText getOutOfServiceState() throws UaException;

  /** Sets the existing node's local value. */
  void setOutOfServiceState(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable LocalizedText readOutOfServiceState() throws UaException;

  /** Writes the value remotely. */
  void writeOutOfServiceState(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable LocalizedText> readOutOfServiceStateAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeOutOfServiceStateAsync(@Nullable LocalizedText value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable TwoStateVariableType getOutOfServiceStateNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable TwoStateVariableType> getOutOfServiceStateNodeAsync();

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable ShelvedStateMachineType getShelvingStateNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable ShelvedStateMachineType> getShelvingStateNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable ByteString getAudibleSound() throws UaException;

  /** Sets the existing node's local value. */
  void setAudibleSound(@Nullable ByteString value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable ByteString readAudibleSound() throws UaException;

  /** Writes the value remotely. */
  void writeAudibleSound(@Nullable ByteString value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable ByteString> readAudibleSoundAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeAudibleSoundAsync(@Nullable ByteString value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable AudioVariableType getAudibleSoundNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable AudioVariableType> getAudibleSoundNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable LocalizedText getSilenceState() throws UaException;

  /** Sets the existing node's local value. */
  void setSilenceState(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable LocalizedText readSilenceState() throws UaException;

  /** Writes the value remotely. */
  void writeSilenceState(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable LocalizedText> readSilenceStateAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSilenceStateAsync(@Nullable LocalizedText value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable TwoStateVariableType getSilenceStateNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable TwoStateVariableType> getSilenceStateNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Boolean getFirstInGroupFlag() throws UaException;

  /** Sets the existing node's local value. */
  void setFirstInGroupFlag(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Boolean readFirstInGroupFlag() throws UaException;

  /** Writes the value remotely. */
  void writeFirstInGroupFlag(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Boolean> readFirstInGroupFlagAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeFirstInGroupFlagAsync(@Nullable Boolean value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable BaseDataVariableType getFirstInGroupFlagNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable BaseDataVariableType> getFirstInGroupFlagNodeAsync();

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable AlarmGroupType getFirstInGroupNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable AlarmGroupType> getFirstInGroupNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable LocalizedText getLatchedState() throws UaException;

  /** Sets the existing node's local value. */
  void setLatchedState(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable LocalizedText readLatchedState() throws UaException;

  /** Writes the value remotely. */
  void writeLatchedState(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable LocalizedText> readLatchedStateAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeLatchedStateAsync(@Nullable LocalizedText value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable TwoStateVariableType getLatchedStateNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable TwoStateVariableType> getLatchedStateNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Short getReAlarmRepeatCount() throws UaException;

  /** Sets the existing node's local value. */
  void setReAlarmRepeatCount(@Nullable Short value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Short readReAlarmRepeatCount() throws UaException;

  /** Writes the value remotely. */
  void writeReAlarmRepeatCount(@Nullable Short value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Short> readReAlarmRepeatCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeReAlarmRepeatCountAsync(@Nullable Short value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable BaseDataVariableType getReAlarmRepeatCountNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable BaseDataVariableType> getReAlarmRepeatCountNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.7
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @NullMarked
  @Nullable UaMethodNode getSilenceMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.7
   *
   * <p>Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode> getSilenceMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.7
   *
   * <p>Invokes <code>Silence</code> on this node's ObjectId using the effective Method contract.
   * Requires Good operation status.
   *
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  void callSilence() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.7
   *
   * <p>Invokes <code>Silence</code> on this node's ObjectId using the effective Method contract.
   * Requires Good operation status.
   *
   * @return a future whose successful payload is null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Void> callSilenceAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.7
   *
   * <p>Invokes <code>Silence</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callSilenceDetailed() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.7
   *
   * <p>Invokes <code>Silence</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callSilenceDetailed(MethodCallOptions options)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.7
   *
   * <p>Invokes <code>Silence</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callSilenceDetailedAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.7
   *
   * <p>Invokes <code>Silence</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>> callSilenceDetailedAsync(
      MethodCallOptions options);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.8
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @NullMarked
  @Nullable UaMethodNode getSuppressMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.8
   *
   * <p>Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode> getSuppressMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.8
   *
   * <p>Invokes <code>Suppress</code> on this node's ObjectId using the effective Method contract.
   * Requires Good operation status.
   *
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  void callSuppress() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.8
   *
   * <p>Invokes <code>Suppress</code> on this node's ObjectId using the effective Method contract.
   * Requires Good operation status.
   *
   * @return a future whose successful payload is null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Void> callSuppressAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.8
   *
   * <p>Invokes <code>Suppress</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callSuppressDetailed() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.8
   *
   * <p>Invokes <code>Suppress</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callSuppressDetailed(MethodCallOptions options)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.8
   *
   * <p>Invokes <code>Suppress</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callSuppressDetailedAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.8
   *
   * <p>Invokes <code>Suppress</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>> callSuppressDetailedAsync(
      MethodCallOptions options);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.9
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @NullMarked
  @Nullable UaMethodNode getSuppress2MethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.9
   *
   * <p>Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode> getSuppress2MethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.9
   *
   * <p>Invokes <code>Suppress2</code> on this node's ObjectId using the effective Method contract.
   * Requires Good operation status.
   *
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  void callSuppress2(@Nullable LocalizedText comment) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.9
   *
   * <p>Invokes <code>Suppress2</code> on this node's ObjectId using the effective Method contract.
   * Requires Good operation status.
   *
   * @return a future whose successful payload is null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Void> callSuppress2Async(@Nullable LocalizedText comment);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.9
   *
   * <p>Invokes <code>Suppress2</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callSuppress2Detailed(@Nullable LocalizedText comment)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.9
   *
   * <p>Invokes <code>Suppress2</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callSuppress2Detailed(
      MethodCallOptions options, @Nullable LocalizedText comment) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.9
   *
   * <p>Invokes <code>Suppress2</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callSuppress2DetailedAsync(@Nullable LocalizedText comment);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.9
   *
   * <p>Invokes <code>Suppress2</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callSuppress2DetailedAsync(MethodCallOptions options, @Nullable LocalizedText comment);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.10
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @NullMarked
  @Nullable UaMethodNode getUnsuppressMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.10
   *
   * <p>Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode> getUnsuppressMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.10
   *
   * <p>Invokes <code>Unsuppress</code> on this node's ObjectId using the effective Method contract.
   * Requires Good operation status.
   *
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  void callUnsuppress() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.10
   *
   * <p>Invokes <code>Unsuppress</code> on this node's ObjectId using the effective Method contract.
   * Requires Good operation status.
   *
   * @return a future whose successful payload is null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Void> callUnsuppressAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.10
   *
   * <p>Invokes <code>Unsuppress</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callUnsuppressDetailed() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.10
   *
   * <p>Invokes <code>Unsuppress</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callUnsuppressDetailed(MethodCallOptions options)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.10
   *
   * <p>Invokes <code>Unsuppress</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callUnsuppressDetailedAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.10
   *
   * <p>Invokes <code>Unsuppress</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callUnsuppressDetailedAsync(MethodCallOptions options);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.11
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @NullMarked
  @Nullable UaMethodNode getUnsuppress2MethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.11
   *
   * <p>Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode> getUnsuppress2MethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.11
   *
   * <p>Invokes <code>Unsuppress2</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  void callUnsuppress2(@Nullable LocalizedText comment) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.11
   *
   * <p>Invokes <code>Unsuppress2</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return a future whose successful payload is null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Void> callUnsuppress2Async(@Nullable LocalizedText comment);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.11
   *
   * <p>Invokes <code>Unsuppress2</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callUnsuppress2Detailed(
      @Nullable LocalizedText comment) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.11
   *
   * <p>Invokes <code>Unsuppress2</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callUnsuppress2Detailed(
      MethodCallOptions options, @Nullable LocalizedText comment) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.11
   *
   * <p>Invokes <code>Unsuppress2</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callUnsuppress2DetailedAsync(@Nullable LocalizedText comment);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.11
   *
   * <p>Invokes <code>Unsuppress2</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callUnsuppress2DetailedAsync(MethodCallOptions options, @Nullable LocalizedText comment);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.12
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @NullMarked
  @Nullable UaMethodNode getRemoveFromServiceMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.12
   *
   * <p>Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode> getRemoveFromServiceMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.12
   *
   * <p>Invokes <code>RemoveFromService</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  void callRemoveFromService() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.12
   *
   * <p>Invokes <code>RemoveFromService</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return a future whose successful payload is null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Void> callRemoveFromServiceAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.12
   *
   * <p>Invokes <code>RemoveFromService</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callRemoveFromServiceDetailed() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.12
   *
   * <p>Invokes <code>RemoveFromService</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callRemoveFromServiceDetailed(
      MethodCallOptions options) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.12
   *
   * <p>Invokes <code>RemoveFromService</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callRemoveFromServiceDetailedAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.12
   *
   * <p>Invokes <code>RemoveFromService</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callRemoveFromServiceDetailedAsync(MethodCallOptions options);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.13
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @NullMarked
  @Nullable UaMethodNode getRemoveFromService2MethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.13
   *
   * <p>Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode> getRemoveFromService2MethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.13
   *
   * <p>Invokes <code>RemoveFromService2</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  void callRemoveFromService2(@Nullable LocalizedText comment) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.13
   *
   * <p>Invokes <code>RemoveFromService2</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return a future whose successful payload is null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Void> callRemoveFromService2Async(
      @Nullable LocalizedText comment);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.13
   *
   * <p>Invokes <code>RemoveFromService2</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callRemoveFromService2Detailed(
      @Nullable LocalizedText comment) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.13
   *
   * <p>Invokes <code>RemoveFromService2</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callRemoveFromService2Detailed(
      MethodCallOptions options, @Nullable LocalizedText comment) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.13
   *
   * <p>Invokes <code>RemoveFromService2</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callRemoveFromService2DetailedAsync(@Nullable LocalizedText comment);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.13
   *
   * <p>Invokes <code>RemoveFromService2</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callRemoveFromService2DetailedAsync(
          MethodCallOptions options, @Nullable LocalizedText comment);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.14
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @NullMarked
  @Nullable UaMethodNode getPlaceInServiceMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.14
   *
   * <p>Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode> getPlaceInServiceMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.14
   *
   * <p>Invokes <code>PlaceInService</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  void callPlaceInService() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.14
   *
   * <p>Invokes <code>PlaceInService</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return a future whose successful payload is null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Void> callPlaceInServiceAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.14
   *
   * <p>Invokes <code>PlaceInService</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callPlaceInServiceDetailed() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.14
   *
   * <p>Invokes <code>PlaceInService</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callPlaceInServiceDetailed(MethodCallOptions options)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.14
   *
   * <p>Invokes <code>PlaceInService</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callPlaceInServiceDetailedAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.14
   *
   * <p>Invokes <code>PlaceInService</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callPlaceInServiceDetailedAsync(MethodCallOptions options);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.15
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @NullMarked
  @Nullable UaMethodNode getPlaceInService2MethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.15
   *
   * <p>Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode> getPlaceInService2MethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.15
   *
   * <p>Invokes <code>PlaceInService2</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  void callPlaceInService2(@Nullable LocalizedText comment) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.15
   *
   * <p>Invokes <code>PlaceInService2</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return a future whose successful payload is null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Void> callPlaceInService2Async(
      @Nullable LocalizedText comment);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.15
   *
   * <p>Invokes <code>PlaceInService2</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callPlaceInService2Detailed(
      @Nullable LocalizedText comment) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.15
   *
   * <p>Invokes <code>PlaceInService2</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callPlaceInService2Detailed(
      MethodCallOptions options, @Nullable LocalizedText comment) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.15
   *
   * <p>Invokes <code>PlaceInService2</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callPlaceInService2DetailedAsync(@Nullable LocalizedText comment);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.15
   *
   * <p>Invokes <code>PlaceInService2</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callPlaceInService2DetailedAsync(MethodCallOptions options, @Nullable LocalizedText comment);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.5
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @NullMarked
  @Nullable UaMethodNode getResetMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.5
   *
   * <p>Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode> getResetMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.5
   *
   * <p>Invokes <code>Reset</code> on this node's ObjectId using the effective Method contract.
   * Requires Good operation status.
   *
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  void callReset() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.5
   *
   * <p>Invokes <code>Reset</code> on this node's ObjectId using the effective Method contract.
   * Requires Good operation status.
   *
   * @return a future whose successful payload is null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Void> callResetAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.5
   *
   * <p>Invokes <code>Reset</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callResetDetailed() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.5
   *
   * <p>Invokes <code>Reset</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callResetDetailed(MethodCallOptions options)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.5
   *
   * <p>Invokes <code>Reset</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>> callResetDetailedAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.5
   *
   * <p>Invokes <code>Reset</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>> callResetDetailedAsync(
      MethodCallOptions options);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.6
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @NullMarked
  @Nullable UaMethodNode getReset2MethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.6
   *
   * <p>Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode> getReset2MethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.6
   *
   * <p>Invokes <code>Reset2</code> on this node's ObjectId using the effective Method contract.
   * Requires Good operation status.
   *
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  void callReset2(@Nullable LocalizedText comment) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.6
   *
   * <p>Invokes <code>Reset2</code> on this node's ObjectId using the effective Method contract.
   * Requires Good operation status.
   *
   * @return a future whose successful payload is null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Void> callReset2Async(@Nullable LocalizedText comment);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.6
   *
   * <p>Invokes <code>Reset2</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callReset2Detailed(@Nullable LocalizedText comment)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.6
   *
   * <p>Invokes <code>Reset2</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callReset2Detailed(
      MethodCallOptions options, @Nullable LocalizedText comment) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.6
   *
   * <p>Invokes <code>Reset2</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>> callReset2DetailedAsync(
      @Nullable LocalizedText comment);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.6
   *
   * <p>Invokes <code>Reset2</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>> callReset2DetailedAsync(
      MethodCallOptions options, @Nullable LocalizedText comment);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.16
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @NullMarked
  @Nullable UaMethodNode getGetGroupMembershipsMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.16
   *
   * <p>Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable UaMethodNode> getGetGroupMembershipsMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.16
   *
   * <p>Invokes <code>GetGroupMemberships</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  @Nullable NodeId @Nullable [] callGetGroupMemberships() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.16
   *
   * <p>Invokes <code>GetGroupMemberships</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable NodeId @Nullable []> callGetGroupMembershipsAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.16
   *
   * <p>Invokes <code>GetGroupMemberships</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable NodeId @Nullable []> callGetGroupMembershipsDetailed()
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.16
   *
   * <p>Invokes <code>GetGroupMemberships</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable NodeId @Nullable []> callGetGroupMembershipsDetailed(
      MethodCallOptions options) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.16
   *
   * <p>Invokes <code>GetGroupMemberships</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable NodeId @Nullable []>>
      callGetGroupMembershipsDetailedAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.16
   *
   * <p>Invokes <code>GetGroupMemberships</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable NodeId @Nullable []>>
      callGetGroupMembershipsDetailedAsync(MethodCallOptions options);
}

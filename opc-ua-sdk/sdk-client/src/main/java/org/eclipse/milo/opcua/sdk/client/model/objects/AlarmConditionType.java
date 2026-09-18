package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.model.variables.AudioVariableType;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.client.model.variables.TwoStateVariableType;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the AlarmConditionType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.2">Model
 *     documentation</a>
 */
public interface AlarmConditionType extends AcknowledgeableConditionType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2915L);

  QualifiedProperty<Double> ReAlarmTime_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ReAlarmTime",
          ExpandedNodeId.of(Namespaces.OPC_UA, 290L),
          -1,
          Double.class);

  QualifiedProperty<Boolean> AudibleEnabled_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "AudibleEnabled",
          ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
          -1,
          Boolean.class);

  QualifiedProperty<Double> MaxTimeShelved_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "MaxTimeShelved",
          ExpandedNodeId.of(Namespaces.OPC_UA, 290L),
          -1,
          Double.class);

  QualifiedProperty<Boolean> SuppressedOrShelved_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "SuppressedOrShelved",
          ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
          -1,
          Boolean.class);

  QualifiedProperty<Double> OnDelay_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "OnDelay",
          ExpandedNodeId.of(Namespaces.OPC_UA, 290L),
          -1,
          Double.class);

  QualifiedProperty<Double> OffDelay_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "OffDelay",
          ExpandedNodeId.of(Namespaces.OPC_UA, 290L),
          -1,
          Double.class);

  QualifiedProperty<NodeId> InputNode_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "InputNode",
          ExpandedNodeId.of(Namespaces.OPC_UA, 17L),
          -1,
          NodeId.class);

  /**
   * Resolves the mandatory ActiveState child, a TwoStateVariableType with DataType LocalizedText.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.2">TwoStateVariableType
   *     documentation</a>
   */
  TwoStateVariableType getActiveStateNode() throws UaException;

  /** Asynchronous form of {@link #getActiveStateNode()}. */
  CompletableFuture<? extends TwoStateVariableType> getActiveStateNodeAsync();

  /**
   * Reads the Value of the ActiveState child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable LocalizedText readActiveState() throws UaException;

  /**
   * Writes the Value of the ActiveState child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeActiveState(@Nullable LocalizedText value) throws UaException;

  /** Asynchronous form of {@link #readActiveState()}. */
  CompletableFuture<? extends @Nullable LocalizedText> readActiveStateAsync();

  /** Asynchronous form of {@link #writeActiveState}; completes with the operation status. */
  CompletableFuture<StatusCode> writeActiveStateAsync(@Nullable LocalizedText value);

  /**
   * Resolves the optional ReAlarmTime child, a PropertyType with DataType Duration.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getReAlarmTimeNode() throws UaException;

  /** Asynchronous form of {@link #getReAlarmTimeNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getReAlarmTimeNodeAsync();

  /**
   * Reads the Value of the ReAlarmTime child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Double readReAlarmTime() throws UaException;

  /**
   * Writes the Value of the ReAlarmTime child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeReAlarmTime(@Nullable Double value) throws UaException;

  /** Asynchronous form of {@link #readReAlarmTime()}. */
  CompletableFuture<? extends @Nullable Double> readReAlarmTimeAsync();

  /** Asynchronous form of {@link #writeReAlarmTime}; completes with the operation status. */
  CompletableFuture<StatusCode> writeReAlarmTimeAsync(@Nullable Double value);

  /**
   * Resolves the optional AudibleSound child, a AudioVariableType with DataType AudioDataType.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.19">AudioVariableType
   *     documentation</a>
   */
  @Nullable AudioVariableType getAudibleSoundNode() throws UaException;

  /** Asynchronous form of {@link #getAudibleSoundNode()}. */
  CompletableFuture<? extends @Nullable AudioVariableType> getAudibleSoundNodeAsync();

  /**
   * Reads the Value of the AudibleSound child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable ByteString readAudibleSound() throws UaException;

  /**
   * Writes the Value of the AudibleSound child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeAudibleSound(@Nullable ByteString value) throws UaException;

  /** Asynchronous form of {@link #readAudibleSound()}. */
  CompletableFuture<? extends @Nullable ByteString> readAudibleSoundAsync();

  /** Asynchronous form of {@link #writeAudibleSound}; completes with the operation status. */
  CompletableFuture<StatusCode> writeAudibleSoundAsync(@Nullable ByteString value);

  /**
   * Resolves the mandatory EnabledState child, a TwoStateVariableType with DataType LocalizedText.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.2">TwoStateVariableType
   *     documentation</a>
   */
  TwoStateVariableType getEnabledStateNode() throws UaException;

  /** Asynchronous form of {@link #getEnabledStateNode()}. */
  CompletableFuture<? extends TwoStateVariableType> getEnabledStateNodeAsync();

  /**
   * Resolves the optional FirstInGroup child, a AlarmGroupType.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.3">AlarmGroupType
   *     documentation</a>
   */
  @Nullable AlarmGroupType getFirstInGroupNode() throws UaException;

  /** Asynchronous form of {@link #getFirstInGroupNode()}. */
  CompletableFuture<? extends @Nullable AlarmGroupType> getFirstInGroupNodeAsync();

  /**
   * Resolves the optional LatchedState child, a TwoStateVariableType with DataType LocalizedText.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.2">TwoStateVariableType
   *     documentation</a>
   */
  @Nullable TwoStateVariableType getLatchedStateNode() throws UaException;

  /** Asynchronous form of {@link #getLatchedStateNode()}. */
  CompletableFuture<? extends @Nullable TwoStateVariableType> getLatchedStateNodeAsync();

  /**
   * Reads the Value of the LatchedState child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable LocalizedText readLatchedState() throws UaException;

  /**
   * Writes the Value of the LatchedState child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeLatchedState(@Nullable LocalizedText value) throws UaException;

  /** Asynchronous form of {@link #readLatchedState()}. */
  CompletableFuture<? extends @Nullable LocalizedText> readLatchedStateAsync();

  /** Asynchronous form of {@link #writeLatchedState}; completes with the operation status. */
  CompletableFuture<StatusCode> writeLatchedStateAsync(@Nullable LocalizedText value);

  /**
   * Resolves the optional SilenceState child, a TwoStateVariableType with DataType LocalizedText.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.2">TwoStateVariableType
   *     documentation</a>
   */
  @Nullable TwoStateVariableType getSilenceStateNode() throws UaException;

  /** Asynchronous form of {@link #getSilenceStateNode()}. */
  CompletableFuture<? extends @Nullable TwoStateVariableType> getSilenceStateNodeAsync();

  /**
   * Reads the Value of the SilenceState child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable LocalizedText readSilenceState() throws UaException;

  /**
   * Writes the Value of the SilenceState child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSilenceState(@Nullable LocalizedText value) throws UaException;

  /** Asynchronous form of {@link #readSilenceState()}. */
  CompletableFuture<? extends @Nullable LocalizedText> readSilenceStateAsync();

  /** Asynchronous form of {@link #writeSilenceState}; completes with the operation status. */
  CompletableFuture<StatusCode> writeSilenceStateAsync(@Nullable LocalizedText value);

  /**
   * Resolves the optional ShelvingState child, a ShelvedStateMachineType.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.1">ShelvedStateMachineType
   *     documentation</a>
   */
  @Nullable ShelvedStateMachineType getShelvingStateNode() throws UaException;

  /** Asynchronous form of {@link #getShelvingStateNode()}. */
  CompletableFuture<? extends @Nullable ShelvedStateMachineType> getShelvingStateNodeAsync();

  /**
   * Resolves the optional AudibleEnabled child, a PropertyType with DataType Boolean.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getAudibleEnabledNode() throws UaException;

  /** Asynchronous form of {@link #getAudibleEnabledNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getAudibleEnabledNodeAsync();

  /**
   * Reads the Value of the AudibleEnabled child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Boolean readAudibleEnabled() throws UaException;

  /**
   * Writes the Value of the AudibleEnabled child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeAudibleEnabled(@Nullable Boolean value) throws UaException;

  /** Asynchronous form of {@link #readAudibleEnabled()}. */
  CompletableFuture<? extends @Nullable Boolean> readAudibleEnabledAsync();

  /** Asynchronous form of {@link #writeAudibleEnabled}; completes with the operation status. */
  CompletableFuture<StatusCode> writeAudibleEnabledAsync(@Nullable Boolean value);

  /**
   * Resolves the optional MaxTimeShelved child, a PropertyType with DataType Duration.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getMaxTimeShelvedNode() throws UaException;

  /** Asynchronous form of {@link #getMaxTimeShelvedNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getMaxTimeShelvedNodeAsync();

  /**
   * Reads the Value of the MaxTimeShelved child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Double readMaxTimeShelved() throws UaException;

  /**
   * Writes the Value of the MaxTimeShelved child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMaxTimeShelved(@Nullable Double value) throws UaException;

  /** Asynchronous form of {@link #readMaxTimeShelved()}. */
  CompletableFuture<? extends @Nullable Double> readMaxTimeShelvedAsync();

  /** Asynchronous form of {@link #writeMaxTimeShelved}; completes with the operation status. */
  CompletableFuture<StatusCode> writeMaxTimeShelvedAsync(@Nullable Double value);

  /**
   * Resolves the optional SuppressedState child, a TwoStateVariableType with DataType
   * LocalizedText.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.2">TwoStateVariableType
   *     documentation</a>
   */
  @Nullable TwoStateVariableType getSuppressedStateNode() throws UaException;

  /** Asynchronous form of {@link #getSuppressedStateNode()}. */
  CompletableFuture<? extends @Nullable TwoStateVariableType> getSuppressedStateNodeAsync();

  /**
   * Reads the Value of the SuppressedState child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable LocalizedText readSuppressedState() throws UaException;

  /**
   * Writes the Value of the SuppressedState child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSuppressedState(@Nullable LocalizedText value) throws UaException;

  /** Asynchronous form of {@link #readSuppressedState()}. */
  CompletableFuture<? extends @Nullable LocalizedText> readSuppressedStateAsync();

  /** Asynchronous form of {@link #writeSuppressedState}; completes with the operation status. */
  CompletableFuture<StatusCode> writeSuppressedStateAsync(@Nullable LocalizedText value);

  /**
   * Resolves the optional FirstInGroupFlag child, a BaseDataVariableType with DataType Boolean.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  @Nullable VariableNode getFirstInGroupFlagNode() throws UaException;

  /** Asynchronous form of {@link #getFirstInGroupFlagNode()}. */
  CompletableFuture<? extends @Nullable VariableNode> getFirstInGroupFlagNodeAsync();

  /**
   * Reads the Value of the FirstInGroupFlag child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Boolean readFirstInGroupFlag() throws UaException;

  /**
   * Writes the Value of the FirstInGroupFlag child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeFirstInGroupFlag(@Nullable Boolean value) throws UaException;

  /** Asynchronous form of {@link #readFirstInGroupFlag()}. */
  CompletableFuture<? extends @Nullable Boolean> readFirstInGroupFlagAsync();

  /** Asynchronous form of {@link #writeFirstInGroupFlag}; completes with the operation status. */
  CompletableFuture<StatusCode> writeFirstInGroupFlagAsync(@Nullable Boolean value);

  /**
   * Resolves the optional OutOfServiceState child, a TwoStateVariableType with DataType
   * LocalizedText.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.2">TwoStateVariableType
   *     documentation</a>
   */
  @Nullable TwoStateVariableType getOutOfServiceStateNode() throws UaException;

  /** Asynchronous form of {@link #getOutOfServiceStateNode()}. */
  CompletableFuture<? extends @Nullable TwoStateVariableType> getOutOfServiceStateNodeAsync();

  /**
   * Reads the Value of the OutOfServiceState child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable LocalizedText readOutOfServiceState() throws UaException;

  /**
   * Writes the Value of the OutOfServiceState child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeOutOfServiceState(@Nullable LocalizedText value) throws UaException;

  /** Asynchronous form of {@link #readOutOfServiceState()}. */
  CompletableFuture<? extends @Nullable LocalizedText> readOutOfServiceStateAsync();

  /** Asynchronous form of {@link #writeOutOfServiceState}; completes with the operation status. */
  CompletableFuture<StatusCode> writeOutOfServiceStateAsync(@Nullable LocalizedText value);

  /**
   * Resolves the optional ReAlarmRepeatCount child, a BaseDataVariableType with DataType Int16.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  @Nullable VariableNode getReAlarmRepeatCountNode() throws UaException;

  /** Asynchronous form of {@link #getReAlarmRepeatCountNode()}. */
  CompletableFuture<? extends @Nullable VariableNode> getReAlarmRepeatCountNodeAsync();

  /**
   * Reads the Value of the ReAlarmRepeatCount child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Short readReAlarmRepeatCount() throws UaException;

  /**
   * Writes the Value of the ReAlarmRepeatCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeReAlarmRepeatCount(@Nullable Short value) throws UaException;

  /** Asynchronous form of {@link #readReAlarmRepeatCount()}. */
  CompletableFuture<? extends @Nullable Short> readReAlarmRepeatCountAsync();

  /** Asynchronous form of {@link #writeReAlarmRepeatCount}; completes with the operation status. */
  CompletableFuture<StatusCode> writeReAlarmRepeatCountAsync(@Nullable Short value);

  /**
   * Resolves the mandatory SuppressedOrShelved child, a PropertyType with DataType Boolean.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getSuppressedOrShelvedNode() throws UaException;

  /** Asynchronous form of {@link #getSuppressedOrShelvedNode()}. */
  CompletableFuture<? extends PropertyType> getSuppressedOrShelvedNodeAsync();

  /**
   * Reads the Value of the SuppressedOrShelved child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Boolean readSuppressedOrShelved() throws UaException;

  /**
   * Writes the Value of the SuppressedOrShelved child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSuppressedOrShelved(@Nullable Boolean value) throws UaException;

  /** Asynchronous form of {@link #readSuppressedOrShelved()}. */
  CompletableFuture<? extends @Nullable Boolean> readSuppressedOrShelvedAsync();

  /**
   * Asynchronous form of {@link #writeSuppressedOrShelved}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeSuppressedOrShelvedAsync(@Nullable Boolean value);

  /**
   * Resolves the optional OnDelay child, a PropertyType with DataType Duration.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getOnDelayNode() throws UaException;

  /** Asynchronous form of {@link #getOnDelayNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getOnDelayNodeAsync();

  /**
   * Reads the Value of the OnDelay child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Double readOnDelay() throws UaException;

  /**
   * Writes the Value of the OnDelay child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeOnDelay(@Nullable Double value) throws UaException;

  /** Asynchronous form of {@link #readOnDelay()}. */
  CompletableFuture<? extends @Nullable Double> readOnDelayAsync();

  /** Asynchronous form of {@link #writeOnDelay}; completes with the operation status. */
  CompletableFuture<StatusCode> writeOnDelayAsync(@Nullable Double value);

  /**
   * Resolves the optional OffDelay child, a PropertyType with DataType Duration.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getOffDelayNode() throws UaException;

  /** Asynchronous form of {@link #getOffDelayNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getOffDelayNodeAsync();

  /**
   * Reads the Value of the OffDelay child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Double readOffDelay() throws UaException;

  /**
   * Writes the Value of the OffDelay child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeOffDelay(@Nullable Double value) throws UaException;

  /** Asynchronous form of {@link #readOffDelay()}. */
  CompletableFuture<? extends @Nullable Double> readOffDelayAsync();

  /** Asynchronous form of {@link #writeOffDelay}; completes with the operation status. */
  CompletableFuture<StatusCode> writeOffDelayAsync(@Nullable Double value);

  /**
   * Resolves the mandatory InputNode child, a PropertyType with DataType NodeId.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getInputNodeNode() throws UaException;

  /** Asynchronous form of {@link #getInputNodeNode()}. */
  CompletableFuture<? extends PropertyType> getInputNodeNodeAsync();

  /**
   * Reads the Value of the InputNode child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable NodeId readInputNode() throws UaException;

  /**
   * Writes the Value of the InputNode child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeInputNode(@Nullable NodeId value) throws UaException;

  /** Asynchronous form of {@link #readInputNode()}. */
  CompletableFuture<? extends @Nullable NodeId> readInputNodeAsync();

  /** Asynchronous form of {@link #writeInputNode}; completes with the operation status. */
  CompletableFuture<StatusCode> writeInputNodeAsync(@Nullable NodeId value);

  /**
   * Resolves the optional GetGroupMemberships Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.16">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getGetGroupMembershipsMethodNode() throws UaException;

  /** Asynchronous form of {@link #getGetGroupMembershipsMethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getGetGroupMembershipsMethodNodeAsync();

  /**
   * Calls the GetGroupMemberships Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.16">Model
   *     documentation</a>
   */
  NodeId @Nullable [] getGroupMemberships() throws UaException;

  /**
   * Calls the GetGroupMemberships Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<NodeId @Nullable []> callGetGroupMemberships() throws UaException;

  /**
   * Calls the GetGroupMemberships Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<NodeId @Nullable []> callGetGroupMembershipsWith(MethodCallOptions options)
      throws UaException;

  /** Asynchronous form of {@link #getGroupMemberships}. */
  CompletableFuture<NodeId @Nullable []> getGroupMembershipsAsync();

  /** Asynchronous form of {@link #callGetGroupMemberships}. */
  CompletableFuture<MethodCallResult<NodeId @Nullable []>> callGetGroupMembershipsAsync();

  /** Asynchronous form of {@link #callGetGroupMembershipsWith}. */
  CompletableFuture<MethodCallResult<NodeId @Nullable []>> callGetGroupMembershipsWithAsync(
      MethodCallOptions options);

  /**
   * Resolves the optional PlaceInService Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.14">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getPlaceInServiceMethodNode() throws UaException;

  /** Asynchronous form of {@link #getPlaceInServiceMethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getPlaceInServiceMethodNodeAsync();

  /**
   * Calls the PlaceInService Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.14">Model
   *     documentation</a>
   */
  void placeInService() throws UaException;

  /**
   * Calls the PlaceInService Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callPlaceInService() throws UaException;

  /**
   * Calls the PlaceInService Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callPlaceInServiceWith(MethodCallOptions options) throws UaException;

  /** Asynchronous form of {@link #placeInService}. */
  CompletableFuture<Void> placeInServiceAsync();

  /** Asynchronous form of {@link #callPlaceInService}. */
  CompletableFuture<MethodCallResult<Void>> callPlaceInServiceAsync();

  /** Asynchronous form of {@link #callPlaceInServiceWith}. */
  CompletableFuture<MethodCallResult<Void>> callPlaceInServiceWithAsync(MethodCallOptions options);

  /**
   * Resolves the optional PlaceInService2 Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.15">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getPlaceInService2MethodNode() throws UaException;

  /** Asynchronous form of {@link #getPlaceInService2MethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getPlaceInService2MethodNodeAsync();

  /**
   * Calls the PlaceInService2 Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.15">Model
   *     documentation</a>
   */
  void placeInService2(@Nullable LocalizedText comment) throws UaException;

  /**
   * Calls the PlaceInService2 Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callPlaceInService2(@Nullable LocalizedText comment) throws UaException;

  /**
   * Calls the PlaceInService2 Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callPlaceInService2With(
      MethodCallOptions options, @Nullable LocalizedText comment) throws UaException;

  /** Asynchronous form of {@link #placeInService2}. */
  CompletableFuture<Void> placeInService2Async(@Nullable LocalizedText comment);

  /** Asynchronous form of {@link #callPlaceInService2}. */
  CompletableFuture<MethodCallResult<Void>> callPlaceInService2Async(
      @Nullable LocalizedText comment);

  /** Asynchronous form of {@link #callPlaceInService2With}. */
  CompletableFuture<MethodCallResult<Void>> callPlaceInService2WithAsync(
      MethodCallOptions options, @Nullable LocalizedText comment);

  /**
   * Resolves the optional RemoveFromService Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.12">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getRemoveFromServiceMethodNode() throws UaException;

  /** Asynchronous form of {@link #getRemoveFromServiceMethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getRemoveFromServiceMethodNodeAsync();

  /**
   * Calls the RemoveFromService Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.12">Model
   *     documentation</a>
   */
  void removeFromService() throws UaException;

  /**
   * Calls the RemoveFromService Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callRemoveFromService() throws UaException;

  /**
   * Calls the RemoveFromService Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callRemoveFromServiceWith(MethodCallOptions options) throws UaException;

  /** Asynchronous form of {@link #removeFromService}. */
  CompletableFuture<Void> removeFromServiceAsync();

  /** Asynchronous form of {@link #callRemoveFromService}. */
  CompletableFuture<MethodCallResult<Void>> callRemoveFromServiceAsync();

  /** Asynchronous form of {@link #callRemoveFromServiceWith}. */
  CompletableFuture<MethodCallResult<Void>> callRemoveFromServiceWithAsync(
      MethodCallOptions options);

  /**
   * Resolves the optional RemoveFromService2 Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.13">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getRemoveFromService2MethodNode() throws UaException;

  /** Asynchronous form of {@link #getRemoveFromService2MethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getRemoveFromService2MethodNodeAsync();

  /**
   * Calls the RemoveFromService2 Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.13">Model
   *     documentation</a>
   */
  void removeFromService2(@Nullable LocalizedText comment) throws UaException;

  /**
   * Calls the RemoveFromService2 Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callRemoveFromService2(@Nullable LocalizedText comment) throws UaException;

  /**
   * Calls the RemoveFromService2 Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callRemoveFromService2With(
      MethodCallOptions options, @Nullable LocalizedText comment) throws UaException;

  /** Asynchronous form of {@link #removeFromService2}. */
  CompletableFuture<Void> removeFromService2Async(@Nullable LocalizedText comment);

  /** Asynchronous form of {@link #callRemoveFromService2}. */
  CompletableFuture<MethodCallResult<Void>> callRemoveFromService2Async(
      @Nullable LocalizedText comment);

  /** Asynchronous form of {@link #callRemoveFromService2With}. */
  CompletableFuture<MethodCallResult<Void>> callRemoveFromService2WithAsync(
      MethodCallOptions options, @Nullable LocalizedText comment);

  /**
   * Resolves the optional Reset Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.5">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getResetMethodNode() throws UaException;

  /** Asynchronous form of {@link #getResetMethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getResetMethodNodeAsync();

  /**
   * Calls the Reset Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.5">Model
   *     documentation</a>
   */
  void reset() throws UaException;

  /**
   * Calls the Reset Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callReset() throws UaException;

  /**
   * Calls the Reset Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callResetWith(MethodCallOptions options) throws UaException;

  /** Asynchronous form of {@link #reset}. */
  CompletableFuture<Void> resetAsync();

  /** Asynchronous form of {@link #callReset}. */
  CompletableFuture<MethodCallResult<Void>> callResetAsync();

  /** Asynchronous form of {@link #callResetWith}. */
  CompletableFuture<MethodCallResult<Void>> callResetWithAsync(MethodCallOptions options);

  /**
   * Resolves the optional Reset2 Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.6">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getReset2MethodNode() throws UaException;

  /** Asynchronous form of {@link #getReset2MethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getReset2MethodNodeAsync();

  /**
   * Calls the Reset2 Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.6">Model
   *     documentation</a>
   */
  void reset2(@Nullable LocalizedText comment) throws UaException;

  /**
   * Calls the Reset2 Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callReset2(@Nullable LocalizedText comment) throws UaException;

  /**
   * Calls the Reset2 Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callReset2With(MethodCallOptions options, @Nullable LocalizedText comment)
      throws UaException;

  /** Asynchronous form of {@link #reset2}. */
  CompletableFuture<Void> reset2Async(@Nullable LocalizedText comment);

  /** Asynchronous form of {@link #callReset2}. */
  CompletableFuture<MethodCallResult<Void>> callReset2Async(@Nullable LocalizedText comment);

  /** Asynchronous form of {@link #callReset2With}. */
  CompletableFuture<MethodCallResult<Void>> callReset2WithAsync(
      MethodCallOptions options, @Nullable LocalizedText comment);

  /**
   * Resolves the optional Silence Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.7">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getSilenceMethodNode() throws UaException;

  /** Asynchronous form of {@link #getSilenceMethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getSilenceMethodNodeAsync();

  /**
   * Calls the Silence Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.7">Model
   *     documentation</a>
   */
  void silence() throws UaException;

  /**
   * Calls the Silence Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callSilence() throws UaException;

  /**
   * Calls the Silence Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callSilenceWith(MethodCallOptions options) throws UaException;

  /** Asynchronous form of {@link #silence}. */
  CompletableFuture<Void> silenceAsync();

  /** Asynchronous form of {@link #callSilence}. */
  CompletableFuture<MethodCallResult<Void>> callSilenceAsync();

  /** Asynchronous form of {@link #callSilenceWith}. */
  CompletableFuture<MethodCallResult<Void>> callSilenceWithAsync(MethodCallOptions options);

  /**
   * Resolves the optional Suppress Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.8">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getSuppressMethodNode() throws UaException;

  /** Asynchronous form of {@link #getSuppressMethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getSuppressMethodNodeAsync();

  /**
   * Calls the Suppress Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.8">Model
   *     documentation</a>
   */
  void suppress() throws UaException;

  /**
   * Calls the Suppress Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callSuppress() throws UaException;

  /**
   * Calls the Suppress Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callSuppressWith(MethodCallOptions options) throws UaException;

  /** Asynchronous form of {@link #suppress}. */
  CompletableFuture<Void> suppressAsync();

  /** Asynchronous form of {@link #callSuppress}. */
  CompletableFuture<MethodCallResult<Void>> callSuppressAsync();

  /** Asynchronous form of {@link #callSuppressWith}. */
  CompletableFuture<MethodCallResult<Void>> callSuppressWithAsync(MethodCallOptions options);

  /**
   * Resolves the optional Suppress2 Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.9">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getSuppress2MethodNode() throws UaException;

  /** Asynchronous form of {@link #getSuppress2MethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getSuppress2MethodNodeAsync();

  /**
   * Calls the Suppress2 Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.9">Model
   *     documentation</a>
   */
  void suppress2(@Nullable LocalizedText comment) throws UaException;

  /**
   * Calls the Suppress2 Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callSuppress2(@Nullable LocalizedText comment) throws UaException;

  /**
   * Calls the Suppress2 Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callSuppress2With(
      MethodCallOptions options, @Nullable LocalizedText comment) throws UaException;

  /** Asynchronous form of {@link #suppress2}. */
  CompletableFuture<Void> suppress2Async(@Nullable LocalizedText comment);

  /** Asynchronous form of {@link #callSuppress2}. */
  CompletableFuture<MethodCallResult<Void>> callSuppress2Async(@Nullable LocalizedText comment);

  /** Asynchronous form of {@link #callSuppress2With}. */
  CompletableFuture<MethodCallResult<Void>> callSuppress2WithAsync(
      MethodCallOptions options, @Nullable LocalizedText comment);

  /**
   * Resolves the optional Unsuppress Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.10">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getUnsuppressMethodNode() throws UaException;

  /** Asynchronous form of {@link #getUnsuppressMethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getUnsuppressMethodNodeAsync();

  /**
   * Calls the Unsuppress Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.10">Model
   *     documentation</a>
   */
  void unsuppress() throws UaException;

  /**
   * Calls the Unsuppress Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callUnsuppress() throws UaException;

  /**
   * Calls the Unsuppress Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callUnsuppressWith(MethodCallOptions options) throws UaException;

  /** Asynchronous form of {@link #unsuppress}. */
  CompletableFuture<Void> unsuppressAsync();

  /** Asynchronous form of {@link #callUnsuppress}. */
  CompletableFuture<MethodCallResult<Void>> callUnsuppressAsync();

  /** Asynchronous form of {@link #callUnsuppressWith}. */
  CompletableFuture<MethodCallResult<Void>> callUnsuppressWithAsync(MethodCallOptions options);

  /**
   * Resolves the optional Unsuppress2 Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.11">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getUnsuppress2MethodNode() throws UaException;

  /** Asynchronous form of {@link #getUnsuppress2MethodNode()}. */
  CompletableFuture<@Nullable UaMethodNode> getUnsuppress2MethodNodeAsync();

  /**
   * Calls the Unsuppress2 Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.11">Model
   *     documentation</a>
   */
  void unsuppress2(@Nullable LocalizedText comment) throws UaException;

  /**
   * Calls the Unsuppress2 Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callUnsuppress2(@Nullable LocalizedText comment) throws UaException;

  /**
   * Calls the Unsuppress2 Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callUnsuppress2With(
      MethodCallOptions options, @Nullable LocalizedText comment) throws UaException;

  /** Asynchronous form of {@link #unsuppress2}. */
  CompletableFuture<Void> unsuppress2Async(@Nullable LocalizedText comment);

  /** Asynchronous form of {@link #callUnsuppress2}. */
  CompletableFuture<MethodCallResult<Void>> callUnsuppress2Async(@Nullable LocalizedText comment);

  /** Asynchronous form of {@link #callUnsuppress2With}. */
  CompletableFuture<MethodCallResult<Void>> callUnsuppress2WithAsync(
      MethodCallOptions options, @Nullable LocalizedText comment);
}

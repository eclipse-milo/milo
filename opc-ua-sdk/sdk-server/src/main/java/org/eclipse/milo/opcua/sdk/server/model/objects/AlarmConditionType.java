package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.model.variables.AudioVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.BaseDataVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.TwoStateVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the AlarmConditionType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.2">Model
 *     documentation</a>
 */
public interface AlarmConditionType extends AcknowledgeableConditionType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2915L);

  /**
   * Returns the mandatory ActiveState child, a TwoStateVariableType with DataType LocalizedText.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.2">TwoStateVariableType
   *     documentation</a>
   */
  TwoStateVariableTypeNode getActiveStateNode();

  /**
   * Returns the Value of the ActiveState child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable LocalizedText getActiveState();

  /**
   * Sets the Value of the ActiveState child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setActiveState(@Nullable LocalizedText value);

  /**
   * Returns the optional AudibleEnabled child, a PropertyType with DataType Boolean.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getAudibleEnabledNode();

  /**
   * Returns the Value of the AudibleEnabled child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Boolean getAudibleEnabled();

  /**
   * Sets the Value of the AudibleEnabled child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setAudibleEnabled(@Nullable Boolean value);

  /**
   * Returns the optional AudibleSound child, a AudioVariableType with DataType AudioDataType.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.19">AudioVariableType
   *     documentation</a>
   */
  @Nullable AudioVariableTypeNode getAudibleSoundNode();

  /**
   * Returns the Value of the AudibleSound child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable ByteString getAudibleSound();

  /**
   * Sets the Value of the AudibleSound child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setAudibleSound(@Nullable ByteString value);

  /**
   * Returns the optional FirstInGroup child, a AlarmGroupType.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.3">AlarmGroupType
   *     documentation</a>
   */
  @Nullable AlarmGroupTypeNode getFirstInGroupNode();

  /**
   * Returns the optional FirstInGroupFlag child, a BaseDataVariableType with DataType Boolean.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  @Nullable BaseDataVariableTypeNode getFirstInGroupFlagNode();

  /**
   * Returns the Value of the FirstInGroupFlag child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Boolean getFirstInGroupFlag();

  /**
   * Sets the Value of the FirstInGroupFlag child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setFirstInGroupFlag(@Nullable Boolean value);

  /**
   * Returns the mandatory InputNode child, a PropertyType with DataType NodeId.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getInputNodeNode();

  /**
   * Returns the Value of the InputNode child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable NodeId getInputNode();

  /**
   * Sets the Value of the InputNode child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setInputNode(@Nullable NodeId value);

  /**
   * Returns the optional LatchedState child, a TwoStateVariableType with DataType LocalizedText.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.2">TwoStateVariableType
   *     documentation</a>
   */
  @Nullable TwoStateVariableTypeNode getLatchedStateNode();

  /**
   * Returns the Value of the LatchedState child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable LocalizedText getLatchedState();

  /**
   * Sets the Value of the LatchedState child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setLatchedState(@Nullable LocalizedText value);

  /**
   * Returns the optional MaxTimeShelved child, a PropertyType with DataType Duration.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getMaxTimeShelvedNode();

  /**
   * Returns the Value of the MaxTimeShelved child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Double getMaxTimeShelved();

  /**
   * Sets the Value of the MaxTimeShelved child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMaxTimeShelved(@Nullable Double value);

  /**
   * Returns the optional OffDelay child, a PropertyType with DataType Duration.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getOffDelayNode();

  /**
   * Returns the Value of the OffDelay child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Double getOffDelay();

  /**
   * Sets the Value of the OffDelay child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setOffDelay(@Nullable Double value);

  /**
   * Returns the optional OnDelay child, a PropertyType with DataType Duration.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getOnDelayNode();

  /**
   * Returns the Value of the OnDelay child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Double getOnDelay();

  /**
   * Sets the Value of the OnDelay child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setOnDelay(@Nullable Double value);

  /**
   * Returns the optional OutOfServiceState child, a TwoStateVariableType with DataType
   * LocalizedText.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.2">TwoStateVariableType
   *     documentation</a>
   */
  @Nullable TwoStateVariableTypeNode getOutOfServiceStateNode();

  /**
   * Returns the Value of the OutOfServiceState child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable LocalizedText getOutOfServiceState();

  /**
   * Sets the Value of the OutOfServiceState child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setOutOfServiceState(@Nullable LocalizedText value);

  /**
   * Returns the optional ReAlarmRepeatCount child, a BaseDataVariableType with DataType Int16.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  @Nullable BaseDataVariableTypeNode getReAlarmRepeatCountNode();

  /**
   * Returns the Value of the ReAlarmRepeatCount child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Short getReAlarmRepeatCount();

  /**
   * Sets the Value of the ReAlarmRepeatCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setReAlarmRepeatCount(@Nullable Short value);

  /**
   * Returns the optional ReAlarmTime child, a PropertyType with DataType Duration.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getReAlarmTimeNode();

  /**
   * Returns the Value of the ReAlarmTime child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Double getReAlarmTime();

  /**
   * Sets the Value of the ReAlarmTime child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setReAlarmTime(@Nullable Double value);

  /**
   * Returns the optional ShelvingState child, a ShelvedStateMachineType.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.1">ShelvedStateMachineType
   *     documentation</a>
   */
  @Nullable ShelvedStateMachineTypeNode getShelvingStateNode();

  /**
   * Returns the optional SilenceState child, a TwoStateVariableType with DataType LocalizedText.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.2">TwoStateVariableType
   *     documentation</a>
   */
  @Nullable TwoStateVariableTypeNode getSilenceStateNode();

  /**
   * Returns the Value of the SilenceState child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable LocalizedText getSilenceState();

  /**
   * Sets the Value of the SilenceState child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSilenceState(@Nullable LocalizedText value);

  /**
   * Returns the mandatory SuppressedOrShelved child, a PropertyType with DataType Boolean.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getSuppressedOrShelvedNode();

  /**
   * Returns the Value of the SuppressedOrShelved child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Boolean getSuppressedOrShelved();

  /**
   * Sets the Value of the SuppressedOrShelved child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSuppressedOrShelved(@Nullable Boolean value);

  /**
   * Returns the optional SuppressedState child, a TwoStateVariableType with DataType LocalizedText.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.2">TwoStateVariableType
   *     documentation</a>
   */
  @Nullable TwoStateVariableTypeNode getSuppressedStateNode();

  /**
   * Returns the Value of the SuppressedState child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable LocalizedText getSuppressedState();

  /**
   * Sets the Value of the SuppressedState child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSuppressedState(@Nullable LocalizedText value);

  /**
   * Returns the optional GetGroupMemberships Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.16">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getGetGroupMembershipsMethodNode();

  /**
   * Sets this instance's GetGroupMemberships handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setGetGroupMembershipsHandler(@Nullable GetGroupMembershipsHandler handler);

  /**
   * Returns the optional PlaceInService Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.14">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getPlaceInServiceMethodNode();

  /**
   * Sets this instance's PlaceInService handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setPlaceInServiceHandler(@Nullable PlaceInServiceHandler handler);

  /**
   * Returns the optional PlaceInService2 Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.15">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getPlaceInService2MethodNode();

  /**
   * Sets this instance's PlaceInService2 handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setPlaceInService2Handler(@Nullable PlaceInService2Handler handler);

  /**
   * Returns the optional RemoveFromService Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.12">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getRemoveFromServiceMethodNode();

  /**
   * Sets this instance's RemoveFromService handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setRemoveFromServiceHandler(@Nullable RemoveFromServiceHandler handler);

  /**
   * Returns the optional RemoveFromService2 Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.13">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getRemoveFromService2MethodNode();

  /**
   * Sets this instance's RemoveFromService2 handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setRemoveFromService2Handler(@Nullable RemoveFromService2Handler handler);

  /**
   * Returns the optional Reset Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.5">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getResetMethodNode();

  /**
   * Sets this instance's Reset handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setResetHandler(@Nullable ResetHandler handler);

  /**
   * Returns the optional Reset2 Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.6">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getReset2MethodNode();

  /**
   * Sets this instance's Reset2 handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setReset2Handler(@Nullable Reset2Handler handler);

  /**
   * Returns the optional Silence Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.7">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getSilenceMethodNode();

  /**
   * Sets this instance's Silence handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setSilenceHandler(@Nullable SilenceHandler handler);

  /**
   * Returns the optional Suppress Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.8">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getSuppressMethodNode();

  /**
   * Sets this instance's Suppress handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setSuppressHandler(@Nullable SuppressHandler handler);

  /**
   * Returns the optional Suppress2 Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.9">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getSuppress2MethodNode();

  /**
   * Sets this instance's Suppress2 handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setSuppress2Handler(@Nullable Suppress2Handler handler);

  /**
   * Returns the optional Unsuppress Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.10">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getUnsuppressMethodNode();

  /**
   * Sets this instance's Unsuppress handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setUnsuppressHandler(@Nullable UnsuppressHandler handler);

  /**
   * Returns the optional Unsuppress2 Method node.
   *
   * @return the Method node, or null if it is absent.
   * @throws UaRuntimeException if the Method is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.11">Model
   *     documentation</a>
   */
  @Nullable UaMethodNode getUnsuppress2MethodNode();

  /**
   * Sets this instance's Unsuppress2 handler; null clears it.
   *
   * @throws UaRuntimeException if the Method node is absent, ambiguous or incompatible.
   */
  void setUnsuppress2Handler(@Nullable Unsuppress2Handler handler);

  /**
   * Sets this instance's Method handlers, including inherited handlers, from one implementation;
   * null clears them and restores Method-node fallback. Absent optional Methods are skipped.
   * Changes are applied in order; a failure does not roll back earlier changes.
   *
   * @throws UaRuntimeException if a mandatory Method is absent, or a Method is ambiguous or
   *     incompatible.
   */
  void setMethods(@Nullable Methods methods);

  /**
   * Handles calls to the GetGroupMemberships Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.16">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface GetGroupMembershipsHandler {
    /**
     * Handles a call to the GetGroupMemberships Method.
     *
     * @throws UaException if the call fails.
     */
    NodeId @Nullable [] getGroupMemberships(
        AbstractMethodInvocationHandler.InvocationContext context) throws UaException;
  }

  /**
   * Handles calls to the PlaceInService Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.14">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface PlaceInServiceHandler {
    /**
     * Handles a call to the PlaceInService Method.
     *
     * @throws UaException if the call fails.
     */
    void placeInService(AbstractMethodInvocationHandler.InvocationContext context)
        throws UaException;
  }

  /**
   * Handles calls to the PlaceInService2 Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.15">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface PlaceInService2Handler {
    /**
     * Handles a call to the PlaceInService2 Method.
     *
     * @throws UaException if the call fails.
     */
    void placeInService2(
        AbstractMethodInvocationHandler.InvocationContext context, @Nullable LocalizedText comment)
        throws UaException;
  }

  /**
   * Handles calls to the RemoveFromService Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.12">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface RemoveFromServiceHandler {
    /**
     * Handles a call to the RemoveFromService Method.
     *
     * @throws UaException if the call fails.
     */
    void removeFromService(AbstractMethodInvocationHandler.InvocationContext context)
        throws UaException;
  }

  /**
   * Handles calls to the RemoveFromService2 Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.13">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface RemoveFromService2Handler {
    /**
     * Handles a call to the RemoveFromService2 Method.
     *
     * @throws UaException if the call fails.
     */
    void removeFromService2(
        AbstractMethodInvocationHandler.InvocationContext context, @Nullable LocalizedText comment)
        throws UaException;
  }

  /**
   * Handles calls to the Reset Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.5">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface ResetHandler {
    /**
     * Handles a call to the Reset Method.
     *
     * @throws UaException if the call fails.
     */
    void reset(AbstractMethodInvocationHandler.InvocationContext context) throws UaException;
  }

  /**
   * Handles calls to the Reset2 Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.6">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface Reset2Handler {
    /**
     * Handles a call to the Reset2 Method.
     *
     * @throws UaException if the call fails.
     */
    void reset2(
        AbstractMethodInvocationHandler.InvocationContext context, @Nullable LocalizedText comment)
        throws UaException;
  }

  /**
   * Handles calls to the Silence Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.7">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface SilenceHandler {
    /**
     * Handles a call to the Silence Method.
     *
     * @throws UaException if the call fails.
     */
    void silence(AbstractMethodInvocationHandler.InvocationContext context) throws UaException;
  }

  /**
   * Handles calls to the Suppress Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.8">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface SuppressHandler {
    /**
     * Handles a call to the Suppress Method.
     *
     * @throws UaException if the call fails.
     */
    void suppress(AbstractMethodInvocationHandler.InvocationContext context) throws UaException;
  }

  /**
   * Handles calls to the Suppress2 Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.9">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface Suppress2Handler {
    /**
     * Handles a call to the Suppress2 Method.
     *
     * @throws UaException if the call fails.
     */
    void suppress2(
        AbstractMethodInvocationHandler.InvocationContext context, @Nullable LocalizedText comment)
        throws UaException;
  }

  /**
   * Handles calls to the Unsuppress Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.10">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface UnsuppressHandler {
    /**
     * Handles a call to the Unsuppress Method.
     *
     * @throws UaException if the call fails.
     */
    void unsuppress(AbstractMethodInvocationHandler.InvocationContext context) throws UaException;
  }

  /**
   * Handles calls to the Unsuppress2 Method.
   *
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.11">Model
   *     documentation</a>
   */
  @FunctionalInterface
  interface Unsuppress2Handler {
    /**
     * Handles a call to the Unsuppress2 Method.
     *
     * @throws UaException if the call fails.
     */
    void unsuppress2(
        AbstractMethodInvocationHandler.InvocationContext context, @Nullable LocalizedText comment)
        throws UaException;
  }

  /** Implements this type's Methods. Unimplemented Methods report Bad_NotImplemented. */
  interface Methods extends AcknowledgeableConditionType.Methods {
    /**
     * Handles a call to the GetGroupMemberships Method; see {@link
     * GetGroupMembershipsHandler#getGroupMemberships}.
     */
    default NodeId @Nullable [] getGroupMemberships(
        AbstractMethodInvocationHandler.InvocationContext context) throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /**
     * Handles a call to the PlaceInService Method; see {@link
     * PlaceInServiceHandler#placeInService}.
     */
    default void placeInService(AbstractMethodInvocationHandler.InvocationContext context)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /**
     * Handles a call to the PlaceInService2 Method; see {@link
     * PlaceInService2Handler#placeInService2}.
     */
    default void placeInService2(
        AbstractMethodInvocationHandler.InvocationContext context, @Nullable LocalizedText comment)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /**
     * Handles a call to the RemoveFromService Method; see {@link
     * RemoveFromServiceHandler#removeFromService}.
     */
    default void removeFromService(AbstractMethodInvocationHandler.InvocationContext context)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /**
     * Handles a call to the RemoveFromService2 Method; see {@link
     * RemoveFromService2Handler#removeFromService2}.
     */
    default void removeFromService2(
        AbstractMethodInvocationHandler.InvocationContext context, @Nullable LocalizedText comment)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /** Handles a call to the Reset Method; see {@link ResetHandler#reset}. */
    default void reset(AbstractMethodInvocationHandler.InvocationContext context)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /** Handles a call to the Reset2 Method; see {@link Reset2Handler#reset2}. */
    default void reset2(
        AbstractMethodInvocationHandler.InvocationContext context, @Nullable LocalizedText comment)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /** Handles a call to the Silence Method; see {@link SilenceHandler#silence}. */
    default void silence(AbstractMethodInvocationHandler.InvocationContext context)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /** Handles a call to the Suppress Method; see {@link SuppressHandler#suppress}. */
    default void suppress(AbstractMethodInvocationHandler.InvocationContext context)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /** Handles a call to the Suppress2 Method; see {@link Suppress2Handler#suppress2}. */
    default void suppress2(
        AbstractMethodInvocationHandler.InvocationContext context, @Nullable LocalizedText comment)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /** Handles a call to the Unsuppress Method; see {@link UnsuppressHandler#unsuppress}. */
    default void unsuppress(AbstractMethodInvocationHandler.InvocationContext context)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }

    /** Handles a call to the Unsuppress2 Method; see {@link Unsuppress2Handler#unsuppress2}. */
    default void unsuppress2(
        AbstractMethodInvocationHandler.InvocationContext context, @Nullable LocalizedText comment)
        throws UaException {
      throw new UaException(StatusCodes.Bad_NotImplemented);
    }
  }
}

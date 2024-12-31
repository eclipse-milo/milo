/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;

abstract class NodeIds1 extends NodeIds2 {
  public static final NodeId ConditionType_ClientUserId = new NodeId(UShort.MIN, uint(9026));

  public static final NodeId ConditionType_Enable = new NodeId(UShort.MIN, uint(9027));

  public static final NodeId ConditionType_Disable = new NodeId(UShort.MIN, uint(9028));

  public static final NodeId ConditionType_AddComment = new NodeId(UShort.MIN, uint(9029));

  public static final NodeId ConditionType_AddComment_InputArguments =
      new NodeId(UShort.MIN, uint(9030));

  public static final NodeId DialogResponseMethodType = new NodeId(UShort.MIN, uint(9031));

  public static final NodeId DialogResponseMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(9032));

  public static final NodeId DialogConditionType_ConditionName = new NodeId(UShort.MIN, uint(9033));

  public static final NodeId DialogConditionType_BranchId = new NodeId(UShort.MIN, uint(9034));

  public static final NodeId DialogConditionType_EnabledState = new NodeId(UShort.MIN, uint(9035));

  public static final NodeId DialogConditionType_EnabledState_Id =
      new NodeId(UShort.MIN, uint(9036));

  public static final NodeId DialogConditionType_EnabledState_Name =
      new NodeId(UShort.MIN, uint(9037));

  public static final NodeId DialogConditionType_EnabledState_Number =
      new NodeId(UShort.MIN, uint(9038));

  public static final NodeId DialogConditionType_EnabledState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(9039));

  public static final NodeId DialogConditionType_EnabledState_TransitionTime =
      new NodeId(UShort.MIN, uint(9040));

  public static final NodeId DialogConditionType_EnabledState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(9041));

  public static final NodeId DialogConditionType_EnabledState_TrueState =
      new NodeId(UShort.MIN, uint(9042));

  public static final NodeId DialogConditionType_EnabledState_FalseState =
      new NodeId(UShort.MIN, uint(9043));

  public static final NodeId DialogConditionType_Quality = new NodeId(UShort.MIN, uint(9044));

  public static final NodeId DialogConditionType_Quality_SourceTimestamp =
      new NodeId(UShort.MIN, uint(9045));

  public static final NodeId DialogConditionType_LastSeverity = new NodeId(UShort.MIN, uint(9046));

  public static final NodeId DialogConditionType_LastSeverity_SourceTimestamp =
      new NodeId(UShort.MIN, uint(9047));

  public static final NodeId DialogConditionType_Comment = new NodeId(UShort.MIN, uint(9048));

  public static final NodeId DialogConditionType_Comment_SourceTimestamp =
      new NodeId(UShort.MIN, uint(9049));

  public static final NodeId DialogConditionType_ClientUserId = new NodeId(UShort.MIN, uint(9050));

  public static final NodeId DialogConditionType_Enable = new NodeId(UShort.MIN, uint(9051));

  public static final NodeId DialogConditionType_Disable = new NodeId(UShort.MIN, uint(9052));

  public static final NodeId DialogConditionType_AddComment = new NodeId(UShort.MIN, uint(9053));

  public static final NodeId DialogConditionType_AddComment_InputArguments =
      new NodeId(UShort.MIN, uint(9054));

  public static final NodeId DialogConditionType_DialogState = new NodeId(UShort.MIN, uint(9055));

  public static final NodeId DialogConditionType_DialogState_Id =
      new NodeId(UShort.MIN, uint(9056));

  public static final NodeId DialogConditionType_DialogState_Name =
      new NodeId(UShort.MIN, uint(9057));

  public static final NodeId DialogConditionType_DialogState_Number =
      new NodeId(UShort.MIN, uint(9058));

  public static final NodeId DialogConditionType_DialogState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(9059));

  public static final NodeId DialogConditionType_DialogState_TransitionTime =
      new NodeId(UShort.MIN, uint(9060));

  public static final NodeId DialogConditionType_DialogState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(9061));

  public static final NodeId DialogConditionType_DialogState_TrueState =
      new NodeId(UShort.MIN, uint(9062));

  public static final NodeId DialogConditionType_DialogState_FalseState =
      new NodeId(UShort.MIN, uint(9063));

  public static final NodeId DialogConditionType_ResponseOptionSet =
      new NodeId(UShort.MIN, uint(9064));

  public static final NodeId DialogConditionType_DefaultResponse =
      new NodeId(UShort.MIN, uint(9065));

  public static final NodeId DialogConditionType_OkResponse = new NodeId(UShort.MIN, uint(9066));

  public static final NodeId DialogConditionType_CancelResponse =
      new NodeId(UShort.MIN, uint(9067));

  public static final NodeId DialogConditionType_LastResponse = new NodeId(UShort.MIN, uint(9068));

  public static final NodeId DialogConditionType_Respond = new NodeId(UShort.MIN, uint(9069));

  public static final NodeId DialogConditionType_Respond_InputArguments =
      new NodeId(UShort.MIN, uint(9070));

  public static final NodeId AcknowledgeableConditionType_ConditionName =
      new NodeId(UShort.MIN, uint(9071));

  public static final NodeId AcknowledgeableConditionType_BranchId =
      new NodeId(UShort.MIN, uint(9072));

  public static final NodeId AcknowledgeableConditionType_EnabledState =
      new NodeId(UShort.MIN, uint(9073));

  public static final NodeId AcknowledgeableConditionType_EnabledState_Id =
      new NodeId(UShort.MIN, uint(9074));

  public static final NodeId AcknowledgeableConditionType_EnabledState_Name =
      new NodeId(UShort.MIN, uint(9075));

  public static final NodeId AcknowledgeableConditionType_EnabledState_Number =
      new NodeId(UShort.MIN, uint(9076));

  public static final NodeId AcknowledgeableConditionType_EnabledState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(9077));

  public static final NodeId AcknowledgeableConditionType_EnabledState_TransitionTime =
      new NodeId(UShort.MIN, uint(9078));

  public static final NodeId AcknowledgeableConditionType_EnabledState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(9079));

  public static final NodeId AcknowledgeableConditionType_EnabledState_TrueState =
      new NodeId(UShort.MIN, uint(9080));

  public static final NodeId AcknowledgeableConditionType_EnabledState_FalseState =
      new NodeId(UShort.MIN, uint(9081));

  public static final NodeId AcknowledgeableConditionType_Quality =
      new NodeId(UShort.MIN, uint(9082));

  public static final NodeId AcknowledgeableConditionType_Quality_SourceTimestamp =
      new NodeId(UShort.MIN, uint(9083));

  public static final NodeId AcknowledgeableConditionType_LastSeverity =
      new NodeId(UShort.MIN, uint(9084));

  public static final NodeId AcknowledgeableConditionType_LastSeverity_SourceTimestamp =
      new NodeId(UShort.MIN, uint(9085));

  public static final NodeId AcknowledgeableConditionType_Comment =
      new NodeId(UShort.MIN, uint(9086));

  public static final NodeId AcknowledgeableConditionType_Comment_SourceTimestamp =
      new NodeId(UShort.MIN, uint(9087));

  public static final NodeId AcknowledgeableConditionType_ClientUserId =
      new NodeId(UShort.MIN, uint(9088));

  public static final NodeId AcknowledgeableConditionType_Enable =
      new NodeId(UShort.MIN, uint(9089));

  public static final NodeId AcknowledgeableConditionType_Disable =
      new NodeId(UShort.MIN, uint(9090));

  public static final NodeId AcknowledgeableConditionType_AddComment =
      new NodeId(UShort.MIN, uint(9091));

  public static final NodeId AcknowledgeableConditionType_AddComment_InputArguments =
      new NodeId(UShort.MIN, uint(9092));

  public static final NodeId AcknowledgeableConditionType_AckedState =
      new NodeId(UShort.MIN, uint(9093));

  public static final NodeId AcknowledgeableConditionType_AckedState_Id =
      new NodeId(UShort.MIN, uint(9094));

  public static final NodeId AcknowledgeableConditionType_AckedState_Name =
      new NodeId(UShort.MIN, uint(9095));

  public static final NodeId AcknowledgeableConditionType_AckedState_Number =
      new NodeId(UShort.MIN, uint(9096));

  public static final NodeId AcknowledgeableConditionType_AckedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(9097));

  public static final NodeId AcknowledgeableConditionType_AckedState_TransitionTime =
      new NodeId(UShort.MIN, uint(9098));

  public static final NodeId AcknowledgeableConditionType_AckedState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(9099));

  public static final NodeId AcknowledgeableConditionType_AckedState_TrueState =
      new NodeId(UShort.MIN, uint(9100));

  public static final NodeId AcknowledgeableConditionType_AckedState_FalseState =
      new NodeId(UShort.MIN, uint(9101));

  public static final NodeId AcknowledgeableConditionType_ConfirmedState =
      new NodeId(UShort.MIN, uint(9102));

  public static final NodeId AcknowledgeableConditionType_ConfirmedState_Id =
      new NodeId(UShort.MIN, uint(9103));

  public static final NodeId AcknowledgeableConditionType_ConfirmedState_Name =
      new NodeId(UShort.MIN, uint(9104));

  public static final NodeId AcknowledgeableConditionType_ConfirmedState_Number =
      new NodeId(UShort.MIN, uint(9105));

  public static final NodeId AcknowledgeableConditionType_ConfirmedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(9106));

  public static final NodeId AcknowledgeableConditionType_ConfirmedState_TransitionTime =
      new NodeId(UShort.MIN, uint(9107));

  public static final NodeId AcknowledgeableConditionType_ConfirmedState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(9108));

  public static final NodeId AcknowledgeableConditionType_ConfirmedState_TrueState =
      new NodeId(UShort.MIN, uint(9109));

  public static final NodeId AcknowledgeableConditionType_ConfirmedState_FalseState =
      new NodeId(UShort.MIN, uint(9110));

  public static final NodeId AcknowledgeableConditionType_Acknowledge =
      new NodeId(UShort.MIN, uint(9111));

  public static final NodeId AcknowledgeableConditionType_Acknowledge_InputArguments =
      new NodeId(UShort.MIN, uint(9112));

  public static final NodeId AcknowledgeableConditionType_Confirm =
      new NodeId(UShort.MIN, uint(9113));

  public static final NodeId AcknowledgeableConditionType_Confirm_InputArguments =
      new NodeId(UShort.MIN, uint(9114));

  public static final NodeId ShelvedStateMachineType_UnshelveTime =
      new NodeId(UShort.MIN, uint(9115));

  public static final NodeId AlarmConditionType_ConditionName = new NodeId(UShort.MIN, uint(9116));

  public static final NodeId AlarmConditionType_BranchId = new NodeId(UShort.MIN, uint(9117));

  public static final NodeId AlarmConditionType_EnabledState = new NodeId(UShort.MIN, uint(9118));

  public static final NodeId AlarmConditionType_EnabledState_Id =
      new NodeId(UShort.MIN, uint(9119));

  public static final NodeId AlarmConditionType_EnabledState_Name =
      new NodeId(UShort.MIN, uint(9120));

  public static final NodeId AlarmConditionType_EnabledState_Number =
      new NodeId(UShort.MIN, uint(9121));

  public static final NodeId AlarmConditionType_EnabledState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(9122));

  public static final NodeId AlarmConditionType_EnabledState_TransitionTime =
      new NodeId(UShort.MIN, uint(9123));

  public static final NodeId AlarmConditionType_EnabledState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(9124));

  public static final NodeId AlarmConditionType_EnabledState_TrueState =
      new NodeId(UShort.MIN, uint(9125));

  public static final NodeId AlarmConditionType_EnabledState_FalseState =
      new NodeId(UShort.MIN, uint(9126));

  public static final NodeId AlarmConditionType_Quality = new NodeId(UShort.MIN, uint(9127));

  public static final NodeId AlarmConditionType_Quality_SourceTimestamp =
      new NodeId(UShort.MIN, uint(9128));

  public static final NodeId AlarmConditionType_LastSeverity = new NodeId(UShort.MIN, uint(9129));

  public static final NodeId AlarmConditionType_LastSeverity_SourceTimestamp =
      new NodeId(UShort.MIN, uint(9130));

  public static final NodeId AlarmConditionType_Comment = new NodeId(UShort.MIN, uint(9131));

  public static final NodeId AlarmConditionType_Comment_SourceTimestamp =
      new NodeId(UShort.MIN, uint(9132));

  public static final NodeId AlarmConditionType_ClientUserId = new NodeId(UShort.MIN, uint(9133));

  public static final NodeId AlarmConditionType_Enable = new NodeId(UShort.MIN, uint(9134));

  public static final NodeId AlarmConditionType_Disable = new NodeId(UShort.MIN, uint(9135));

  public static final NodeId AlarmConditionType_AddComment = new NodeId(UShort.MIN, uint(9136));

  public static final NodeId AlarmConditionType_AddComment_InputArguments =
      new NodeId(UShort.MIN, uint(9137));

  public static final NodeId AlarmConditionType_AckedState = new NodeId(UShort.MIN, uint(9138));

  public static final NodeId AlarmConditionType_AckedState_Id = new NodeId(UShort.MIN, uint(9139));

  public static final NodeId AlarmConditionType_AckedState_Name =
      new NodeId(UShort.MIN, uint(9140));

  public static final NodeId AlarmConditionType_AckedState_Number =
      new NodeId(UShort.MIN, uint(9141));

  public static final NodeId AlarmConditionType_AckedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(9142));

  public static final NodeId AlarmConditionType_AckedState_TransitionTime =
      new NodeId(UShort.MIN, uint(9143));

  public static final NodeId AlarmConditionType_AckedState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(9144));

  public static final NodeId AlarmConditionType_AckedState_TrueState =
      new NodeId(UShort.MIN, uint(9145));

  public static final NodeId AlarmConditionType_AckedState_FalseState =
      new NodeId(UShort.MIN, uint(9146));

  public static final NodeId AlarmConditionType_ConfirmedState = new NodeId(UShort.MIN, uint(9147));

  public static final NodeId AlarmConditionType_ConfirmedState_Id =
      new NodeId(UShort.MIN, uint(9148));

  public static final NodeId AlarmConditionType_ConfirmedState_Name =
      new NodeId(UShort.MIN, uint(9149));

  public static final NodeId AlarmConditionType_ConfirmedState_Number =
      new NodeId(UShort.MIN, uint(9150));

  public static final NodeId AlarmConditionType_ConfirmedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(9151));

  public static final NodeId AlarmConditionType_ConfirmedState_TransitionTime =
      new NodeId(UShort.MIN, uint(9152));

  public static final NodeId AlarmConditionType_ConfirmedState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(9153));

  public static final NodeId AlarmConditionType_ConfirmedState_TrueState =
      new NodeId(UShort.MIN, uint(9154));

  public static final NodeId AlarmConditionType_ConfirmedState_FalseState =
      new NodeId(UShort.MIN, uint(9155));

  public static final NodeId AlarmConditionType_Acknowledge = new NodeId(UShort.MIN, uint(9156));

  public static final NodeId AlarmConditionType_Acknowledge_InputArguments =
      new NodeId(UShort.MIN, uint(9157));

  public static final NodeId AlarmConditionType_Confirm = new NodeId(UShort.MIN, uint(9158));

  public static final NodeId AlarmConditionType_Confirm_InputArguments =
      new NodeId(UShort.MIN, uint(9159));

  public static final NodeId AlarmConditionType_ActiveState = new NodeId(UShort.MIN, uint(9160));

  public static final NodeId AlarmConditionType_ActiveState_Id = new NodeId(UShort.MIN, uint(9161));

  public static final NodeId AlarmConditionType_ActiveState_Name =
      new NodeId(UShort.MIN, uint(9162));

  public static final NodeId AlarmConditionType_ActiveState_Number =
      new NodeId(UShort.MIN, uint(9163));

  public static final NodeId AlarmConditionType_ActiveState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(9164));

  public static final NodeId AlarmConditionType_ActiveState_TransitionTime =
      new NodeId(UShort.MIN, uint(9165));

  public static final NodeId AlarmConditionType_ActiveState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(9166));

  public static final NodeId AlarmConditionType_ActiveState_TrueState =
      new NodeId(UShort.MIN, uint(9167));

  public static final NodeId AlarmConditionType_ActiveState_FalseState =
      new NodeId(UShort.MIN, uint(9168));

  public static final NodeId AlarmConditionType_SuppressedState =
      new NodeId(UShort.MIN, uint(9169));

  public static final NodeId AlarmConditionType_SuppressedState_Id =
      new NodeId(UShort.MIN, uint(9170));

  public static final NodeId AlarmConditionType_SuppressedState_Name =
      new NodeId(UShort.MIN, uint(9171));

  public static final NodeId AlarmConditionType_SuppressedState_Number =
      new NodeId(UShort.MIN, uint(9172));

  public static final NodeId AlarmConditionType_SuppressedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(9173));

  public static final NodeId AlarmConditionType_SuppressedState_TransitionTime =
      new NodeId(UShort.MIN, uint(9174));

  public static final NodeId AlarmConditionType_SuppressedState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(9175));

  public static final NodeId AlarmConditionType_SuppressedState_TrueState =
      new NodeId(UShort.MIN, uint(9176));

  public static final NodeId AlarmConditionType_SuppressedState_FalseState =
      new NodeId(UShort.MIN, uint(9177));

  public static final NodeId AlarmConditionType_ShelvingState = new NodeId(UShort.MIN, uint(9178));

  public static final NodeId AlarmConditionType_ShelvingState_CurrentState =
      new NodeId(UShort.MIN, uint(9179));

  public static final NodeId AlarmConditionType_ShelvingState_CurrentState_Id =
      new NodeId(UShort.MIN, uint(9180));

  public static final NodeId AlarmConditionType_ShelvingState_CurrentState_Name =
      new NodeId(UShort.MIN, uint(9181));

  public static final NodeId AlarmConditionType_ShelvingState_CurrentState_Number =
      new NodeId(UShort.MIN, uint(9182));

  public static final NodeId AlarmConditionType_ShelvingState_CurrentState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(9183));

  public static final NodeId AlarmConditionType_ShelvingState_LastTransition =
      new NodeId(UShort.MIN, uint(9184));

  public static final NodeId AlarmConditionType_ShelvingState_LastTransition_Id =
      new NodeId(UShort.MIN, uint(9185));

  public static final NodeId AlarmConditionType_ShelvingState_LastTransition_Name =
      new NodeId(UShort.MIN, uint(9186));

  public static final NodeId AlarmConditionType_ShelvingState_LastTransition_Number =
      new NodeId(UShort.MIN, uint(9187));

  public static final NodeId AlarmConditionType_ShelvingState_LastTransition_TransitionTime =
      new NodeId(UShort.MIN, uint(9188));

  public static final NodeId AlarmConditionType_ShelvingState_UnshelveTime =
      new NodeId(UShort.MIN, uint(9189));

  public static final NodeId AlarmConditionType_ShelvingState_Unshelve =
      new NodeId(UShort.MIN, uint(9211));

  public static final NodeId AlarmConditionType_ShelvingState_OneShotShelve =
      new NodeId(UShort.MIN, uint(9212));

  public static final NodeId AlarmConditionType_ShelvingState_TimedShelve =
      new NodeId(UShort.MIN, uint(9213));

  public static final NodeId AlarmConditionType_ShelvingState_TimedShelve_InputArguments =
      new NodeId(UShort.MIN, uint(9214));

  public static final NodeId AlarmConditionType_SuppressedOrShelved =
      new NodeId(UShort.MIN, uint(9215));

  public static final NodeId AlarmConditionType_MaxTimeShelved = new NodeId(UShort.MIN, uint(9216));

  public static final NodeId LimitAlarmType_ConditionName = new NodeId(UShort.MIN, uint(9217));

  public static final NodeId LimitAlarmType_BranchId = new NodeId(UShort.MIN, uint(9218));

  public static final NodeId LimitAlarmType_EnabledState = new NodeId(UShort.MIN, uint(9219));

  public static final NodeId LimitAlarmType_EnabledState_Id = new NodeId(UShort.MIN, uint(9220));

  public static final NodeId LimitAlarmType_EnabledState_Name = new NodeId(UShort.MIN, uint(9221));

  public static final NodeId LimitAlarmType_EnabledState_Number =
      new NodeId(UShort.MIN, uint(9222));

  public static final NodeId LimitAlarmType_EnabledState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(9223));

  public static final NodeId LimitAlarmType_EnabledState_TransitionTime =
      new NodeId(UShort.MIN, uint(9224));

  public static final NodeId LimitAlarmType_EnabledState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(9225));

  public static final NodeId LimitAlarmType_EnabledState_TrueState =
      new NodeId(UShort.MIN, uint(9226));

  public static final NodeId LimitAlarmType_EnabledState_FalseState =
      new NodeId(UShort.MIN, uint(9227));

  public static final NodeId LimitAlarmType_Quality = new NodeId(UShort.MIN, uint(9228));

  public static final NodeId LimitAlarmType_Quality_SourceTimestamp =
      new NodeId(UShort.MIN, uint(9229));

  public static final NodeId LimitAlarmType_LastSeverity = new NodeId(UShort.MIN, uint(9230));

  public static final NodeId LimitAlarmType_LastSeverity_SourceTimestamp =
      new NodeId(UShort.MIN, uint(9231));

  public static final NodeId LimitAlarmType_Comment = new NodeId(UShort.MIN, uint(9232));

  public static final NodeId LimitAlarmType_Comment_SourceTimestamp =
      new NodeId(UShort.MIN, uint(9233));

  public static final NodeId LimitAlarmType_ClientUserId = new NodeId(UShort.MIN, uint(9234));

  public static final NodeId LimitAlarmType_Enable = new NodeId(UShort.MIN, uint(9235));

  public static final NodeId LimitAlarmType_Disable = new NodeId(UShort.MIN, uint(9236));

  public static final NodeId LimitAlarmType_AddComment = new NodeId(UShort.MIN, uint(9237));

  public static final NodeId LimitAlarmType_AddComment_InputArguments =
      new NodeId(UShort.MIN, uint(9238));

  public static final NodeId LimitAlarmType_AckedState = new NodeId(UShort.MIN, uint(9239));

  public static final NodeId LimitAlarmType_AckedState_Id = new NodeId(UShort.MIN, uint(9240));

  public static final NodeId LimitAlarmType_AckedState_Name = new NodeId(UShort.MIN, uint(9241));

  public static final NodeId LimitAlarmType_AckedState_Number = new NodeId(UShort.MIN, uint(9242));

  public static final NodeId LimitAlarmType_AckedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(9243));

  public static final NodeId LimitAlarmType_AckedState_TransitionTime =
      new NodeId(UShort.MIN, uint(9244));

  public static final NodeId LimitAlarmType_AckedState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(9245));

  public static final NodeId LimitAlarmType_AckedState_TrueState =
      new NodeId(UShort.MIN, uint(9246));

  public static final NodeId LimitAlarmType_AckedState_FalseState =
      new NodeId(UShort.MIN, uint(9247));

  public static final NodeId LimitAlarmType_ConfirmedState = new NodeId(UShort.MIN, uint(9248));

  public static final NodeId LimitAlarmType_ConfirmedState_Id = new NodeId(UShort.MIN, uint(9249));

  public static final NodeId LimitAlarmType_ConfirmedState_Name =
      new NodeId(UShort.MIN, uint(9250));

  public static final NodeId LimitAlarmType_ConfirmedState_Number =
      new NodeId(UShort.MIN, uint(9251));

  public static final NodeId LimitAlarmType_ConfirmedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(9252));

  public static final NodeId LimitAlarmType_ConfirmedState_TransitionTime =
      new NodeId(UShort.MIN, uint(9253));

  public static final NodeId LimitAlarmType_ConfirmedState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(9254));

  public static final NodeId LimitAlarmType_ConfirmedState_TrueState =
      new NodeId(UShort.MIN, uint(9255));

  public static final NodeId LimitAlarmType_ConfirmedState_FalseState =
      new NodeId(UShort.MIN, uint(9256));

  public static final NodeId LimitAlarmType_Acknowledge = new NodeId(UShort.MIN, uint(9257));

  public static final NodeId LimitAlarmType_Acknowledge_InputArguments =
      new NodeId(UShort.MIN, uint(9258));

  public static final NodeId LimitAlarmType_Confirm = new NodeId(UShort.MIN, uint(9259));

  public static final NodeId LimitAlarmType_Confirm_InputArguments =
      new NodeId(UShort.MIN, uint(9260));

  public static final NodeId LimitAlarmType_ActiveState = new NodeId(UShort.MIN, uint(9261));

  public static final NodeId LimitAlarmType_ActiveState_Id = new NodeId(UShort.MIN, uint(9262));

  public static final NodeId LimitAlarmType_ActiveState_Name = new NodeId(UShort.MIN, uint(9263));

  public static final NodeId LimitAlarmType_ActiveState_Number = new NodeId(UShort.MIN, uint(9264));

  public static final NodeId LimitAlarmType_ActiveState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(9265));

  public static final NodeId LimitAlarmType_ActiveState_TransitionTime =
      new NodeId(UShort.MIN, uint(9266));

  public static final NodeId LimitAlarmType_ActiveState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(9267));

  public static final NodeId LimitAlarmType_ActiveState_TrueState =
      new NodeId(UShort.MIN, uint(9268));

  public static final NodeId LimitAlarmType_ActiveState_FalseState =
      new NodeId(UShort.MIN, uint(9269));

  public static final NodeId LimitAlarmType_SuppressedState = new NodeId(UShort.MIN, uint(9270));

  public static final NodeId LimitAlarmType_SuppressedState_Id = new NodeId(UShort.MIN, uint(9271));

  public static final NodeId LimitAlarmType_SuppressedState_Name =
      new NodeId(UShort.MIN, uint(9272));

  public static final NodeId LimitAlarmType_SuppressedState_Number =
      new NodeId(UShort.MIN, uint(9273));

  public static final NodeId LimitAlarmType_SuppressedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(9274));

  public static final NodeId LimitAlarmType_SuppressedState_TransitionTime =
      new NodeId(UShort.MIN, uint(9275));

  public static final NodeId LimitAlarmType_SuppressedState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(9276));

  public static final NodeId LimitAlarmType_SuppressedState_TrueState =
      new NodeId(UShort.MIN, uint(9277));

  public static final NodeId LimitAlarmType_SuppressedState_FalseState =
      new NodeId(UShort.MIN, uint(9278));

  public static final NodeId LimitAlarmType_ShelvingState = new NodeId(UShort.MIN, uint(9279));

  public static final NodeId LimitAlarmType_ShelvingState_CurrentState =
      new NodeId(UShort.MIN, uint(9280));

  public static final NodeId LimitAlarmType_ShelvingState_CurrentState_Id =
      new NodeId(UShort.MIN, uint(9281));

  public static final NodeId LimitAlarmType_ShelvingState_CurrentState_Name =
      new NodeId(UShort.MIN, uint(9282));

  public static final NodeId LimitAlarmType_ShelvingState_CurrentState_Number =
      new NodeId(UShort.MIN, uint(9283));

  public static final NodeId LimitAlarmType_ShelvingState_CurrentState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(9284));

  public static final NodeId LimitAlarmType_ShelvingState_LastTransition =
      new NodeId(UShort.MIN, uint(9285));

  public static final NodeId LimitAlarmType_ShelvingState_LastTransition_Id =
      new NodeId(UShort.MIN, uint(9286));

  public static final NodeId LimitAlarmType_ShelvingState_LastTransition_Name =
      new NodeId(UShort.MIN, uint(9287));

  public static final NodeId LimitAlarmType_ShelvingState_LastTransition_Number =
      new NodeId(UShort.MIN, uint(9288));

  public static final NodeId LimitAlarmType_ShelvingState_LastTransition_TransitionTime =
      new NodeId(UShort.MIN, uint(9289));

  public static final NodeId LimitAlarmType_ShelvingState_UnshelveTime =
      new NodeId(UShort.MIN, uint(9290));

  public static final NodeId LimitAlarmType_ShelvingState_Unshelve =
      new NodeId(UShort.MIN, uint(9312));

  public static final NodeId LimitAlarmType_ShelvingState_OneShotShelve =
      new NodeId(UShort.MIN, uint(9313));

  public static final NodeId LimitAlarmType_ShelvingState_TimedShelve =
      new NodeId(UShort.MIN, uint(9314));

  public static final NodeId LimitAlarmType_ShelvingState_TimedShelve_InputArguments =
      new NodeId(UShort.MIN, uint(9315));

  public static final NodeId LimitAlarmType_SuppressedOrShelved =
      new NodeId(UShort.MIN, uint(9316));

  public static final NodeId LimitAlarmType_MaxTimeShelved = new NodeId(UShort.MIN, uint(9317));

  public static final NodeId ExclusiveLimitStateMachineType = new NodeId(UShort.MIN, uint(9318));

  public static final NodeId ExclusiveLimitStateMachineType_CurrentState =
      new NodeId(UShort.MIN, uint(9319));

  public static final NodeId ExclusiveLimitStateMachineType_CurrentState_Id =
      new NodeId(UShort.MIN, uint(9320));

  public static final NodeId ExclusiveLimitStateMachineType_CurrentState_Name =
      new NodeId(UShort.MIN, uint(9321));

  public static final NodeId ExclusiveLimitStateMachineType_CurrentState_Number =
      new NodeId(UShort.MIN, uint(9322));

  public static final NodeId ExclusiveLimitStateMachineType_CurrentState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(9323));

  public static final NodeId ExclusiveLimitStateMachineType_LastTransition =
      new NodeId(UShort.MIN, uint(9324));

  public static final NodeId ExclusiveLimitStateMachineType_LastTransition_Id =
      new NodeId(UShort.MIN, uint(9325));

  public static final NodeId ExclusiveLimitStateMachineType_LastTransition_Name =
      new NodeId(UShort.MIN, uint(9326));

  public static final NodeId ExclusiveLimitStateMachineType_LastTransition_Number =
      new NodeId(UShort.MIN, uint(9327));

  public static final NodeId ExclusiveLimitStateMachineType_LastTransition_TransitionTime =
      new NodeId(UShort.MIN, uint(9328));

  public static final NodeId ExclusiveLimitStateMachineType_HighHigh =
      new NodeId(UShort.MIN, uint(9329));

  public static final NodeId ExclusiveLimitStateMachineType_HighHigh_StateNumber =
      new NodeId(UShort.MIN, uint(9330));

  public static final NodeId ExclusiveLimitStateMachineType_High =
      new NodeId(UShort.MIN, uint(9331));

  public static final NodeId ExclusiveLimitStateMachineType_High_StateNumber =
      new NodeId(UShort.MIN, uint(9332));

  public static final NodeId ExclusiveLimitStateMachineType_Low =
      new NodeId(UShort.MIN, uint(9333));

  public static final NodeId ExclusiveLimitStateMachineType_Low_StateNumber =
      new NodeId(UShort.MIN, uint(9334));

  public static final NodeId ExclusiveLimitStateMachineType_LowLow =
      new NodeId(UShort.MIN, uint(9335));

  public static final NodeId ExclusiveLimitStateMachineType_LowLow_StateNumber =
      new NodeId(UShort.MIN, uint(9336));

  public static final NodeId ExclusiveLimitStateMachineType_LowLowToLow =
      new NodeId(UShort.MIN, uint(9337));

  public static final NodeId ExclusiveLimitStateMachineType_LowToLowLow =
      new NodeId(UShort.MIN, uint(9338));

  public static final NodeId ExclusiveLimitStateMachineType_HighHighToHigh =
      new NodeId(UShort.MIN, uint(9339));

  public static final NodeId ExclusiveLimitStateMachineType_HighToHighHigh =
      new NodeId(UShort.MIN, uint(9340));

  public static final NodeId ExclusiveLimitAlarmType = new NodeId(UShort.MIN, uint(9341));

  public static final NodeId ExclusiveLimitAlarmType_EventId = new NodeId(UShort.MIN, uint(9342));

  public static final NodeId ExclusiveLimitAlarmType_EventType = new NodeId(UShort.MIN, uint(9343));

  public static final NodeId ExclusiveLimitAlarmType_SourceNode =
      new NodeId(UShort.MIN, uint(9344));

  public static final NodeId ExclusiveLimitAlarmType_SourceName =
      new NodeId(UShort.MIN, uint(9345));

  public static final NodeId ExclusiveLimitAlarmType_Time = new NodeId(UShort.MIN, uint(9346));

  public static final NodeId ExclusiveLimitAlarmType_ReceiveTime =
      new NodeId(UShort.MIN, uint(9347));

  public static final NodeId ExclusiveLimitAlarmType_LocalTime = new NodeId(UShort.MIN, uint(9348));

  public static final NodeId ExclusiveLimitAlarmType_Message = new NodeId(UShort.MIN, uint(9349));

  public static final NodeId ExclusiveLimitAlarmType_Severity = new NodeId(UShort.MIN, uint(9350));

  public static final NodeId ExclusiveLimitAlarmType_ConditionName =
      new NodeId(UShort.MIN, uint(9351));

  public static final NodeId ExclusiveLimitAlarmType_BranchId = new NodeId(UShort.MIN, uint(9352));

  public static final NodeId ExclusiveLimitAlarmType_Retain = new NodeId(UShort.MIN, uint(9353));

  public static final NodeId ExclusiveLimitAlarmType_EnabledState =
      new NodeId(UShort.MIN, uint(9354));

  public static final NodeId ExclusiveLimitAlarmType_EnabledState_Id =
      new NodeId(UShort.MIN, uint(9355));

  public static final NodeId ExclusiveLimitAlarmType_EnabledState_Name =
      new NodeId(UShort.MIN, uint(9356));

  public static final NodeId ExclusiveLimitAlarmType_EnabledState_Number =
      new NodeId(UShort.MIN, uint(9357));

  public static final NodeId ExclusiveLimitAlarmType_EnabledState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(9358));

  public static final NodeId ExclusiveLimitAlarmType_EnabledState_TransitionTime =
      new NodeId(UShort.MIN, uint(9359));

  public static final NodeId ExclusiveLimitAlarmType_EnabledState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(9360));

  public static final NodeId ExclusiveLimitAlarmType_EnabledState_TrueState =
      new NodeId(UShort.MIN, uint(9361));

  public static final NodeId ExclusiveLimitAlarmType_EnabledState_FalseState =
      new NodeId(UShort.MIN, uint(9362));

  public static final NodeId ExclusiveLimitAlarmType_Quality = new NodeId(UShort.MIN, uint(9363));

  public static final NodeId ExclusiveLimitAlarmType_Quality_SourceTimestamp =
      new NodeId(UShort.MIN, uint(9364));

  public static final NodeId ExclusiveLimitAlarmType_LastSeverity =
      new NodeId(UShort.MIN, uint(9365));

  public static final NodeId ExclusiveLimitAlarmType_LastSeverity_SourceTimestamp =
      new NodeId(UShort.MIN, uint(9366));

  public static final NodeId ExclusiveLimitAlarmType_Comment = new NodeId(UShort.MIN, uint(9367));

  public static final NodeId ExclusiveLimitAlarmType_Comment_SourceTimestamp =
      new NodeId(UShort.MIN, uint(9368));

  public static final NodeId ExclusiveLimitAlarmType_ClientUserId =
      new NodeId(UShort.MIN, uint(9369));

  public static final NodeId ExclusiveLimitAlarmType_Enable = new NodeId(UShort.MIN, uint(9370));

  public static final NodeId ExclusiveLimitAlarmType_Disable = new NodeId(UShort.MIN, uint(9371));

  public static final NodeId ExclusiveLimitAlarmType_AddComment =
      new NodeId(UShort.MIN, uint(9372));

  public static final NodeId ExclusiveLimitAlarmType_AddComment_InputArguments =
      new NodeId(UShort.MIN, uint(9373));

  public static final NodeId ExclusiveLimitAlarmType_ConditionRefresh =
      new NodeId(UShort.MIN, uint(9374));

  public static final NodeId ExclusiveLimitAlarmType_ConditionRefresh_InputArguments =
      new NodeId(UShort.MIN, uint(9375));

  public static final NodeId ExclusiveLimitAlarmType_AckedState =
      new NodeId(UShort.MIN, uint(9376));

  public static final NodeId ExclusiveLimitAlarmType_AckedState_Id =
      new NodeId(UShort.MIN, uint(9377));

  public static final NodeId ExclusiveLimitAlarmType_AckedState_Name =
      new NodeId(UShort.MIN, uint(9378));

  public static final NodeId ExclusiveLimitAlarmType_AckedState_Number =
      new NodeId(UShort.MIN, uint(9379));

  public static final NodeId ExclusiveLimitAlarmType_AckedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(9380));

  public static final NodeId ExclusiveLimitAlarmType_AckedState_TransitionTime =
      new NodeId(UShort.MIN, uint(9381));

  public static final NodeId ExclusiveLimitAlarmType_AckedState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(9382));

  public static final NodeId ExclusiveLimitAlarmType_AckedState_TrueState =
      new NodeId(UShort.MIN, uint(9383));

  public static final NodeId ExclusiveLimitAlarmType_AckedState_FalseState =
      new NodeId(UShort.MIN, uint(9384));

  public static final NodeId ExclusiveLimitAlarmType_ConfirmedState =
      new NodeId(UShort.MIN, uint(9385));

  public static final NodeId ExclusiveLimitAlarmType_ConfirmedState_Id =
      new NodeId(UShort.MIN, uint(9386));

  public static final NodeId ExclusiveLimitAlarmType_ConfirmedState_Name =
      new NodeId(UShort.MIN, uint(9387));

  public static final NodeId ExclusiveLimitAlarmType_ConfirmedState_Number =
      new NodeId(UShort.MIN, uint(9388));

  public static final NodeId ExclusiveLimitAlarmType_ConfirmedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(9389));

  public static final NodeId ExclusiveLimitAlarmType_ConfirmedState_TransitionTime =
      new NodeId(UShort.MIN, uint(9390));

  public static final NodeId ExclusiveLimitAlarmType_ConfirmedState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(9391));

  public static final NodeId ExclusiveLimitAlarmType_ConfirmedState_TrueState =
      new NodeId(UShort.MIN, uint(9392));

  public static final NodeId ExclusiveLimitAlarmType_ConfirmedState_FalseState =
      new NodeId(UShort.MIN, uint(9393));

  public static final NodeId ExclusiveLimitAlarmType_Acknowledge =
      new NodeId(UShort.MIN, uint(9394));

  public static final NodeId ExclusiveLimitAlarmType_Acknowledge_InputArguments =
      new NodeId(UShort.MIN, uint(9395));

  public static final NodeId ExclusiveLimitAlarmType_Confirm = new NodeId(UShort.MIN, uint(9396));

  public static final NodeId ExclusiveLimitAlarmType_Confirm_InputArguments =
      new NodeId(UShort.MIN, uint(9397));

  public static final NodeId ExclusiveLimitAlarmType_ActiveState =
      new NodeId(UShort.MIN, uint(9398));

  public static final NodeId ExclusiveLimitAlarmType_ActiveState_Id =
      new NodeId(UShort.MIN, uint(9399));

  public static final NodeId ExclusiveLimitAlarmType_ActiveState_Name =
      new NodeId(UShort.MIN, uint(9400));

  public static final NodeId ExclusiveLimitAlarmType_ActiveState_Number =
      new NodeId(UShort.MIN, uint(9401));

  public static final NodeId ExclusiveLimitAlarmType_ActiveState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(9402));

  public static final NodeId ExclusiveLimitAlarmType_ActiveState_TransitionTime =
      new NodeId(UShort.MIN, uint(9403));

  public static final NodeId ExclusiveLimitAlarmType_ActiveState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(9404));

  public static final NodeId ExclusiveLimitAlarmType_ActiveState_TrueState =
      new NodeId(UShort.MIN, uint(9405));

  public static final NodeId ExclusiveLimitAlarmType_ActiveState_FalseState =
      new NodeId(UShort.MIN, uint(9406));

  public static final NodeId ExclusiveLimitAlarmType_SuppressedState =
      new NodeId(UShort.MIN, uint(9407));

  public static final NodeId ExclusiveLimitAlarmType_SuppressedState_Id =
      new NodeId(UShort.MIN, uint(9408));

  public static final NodeId ExclusiveLimitAlarmType_SuppressedState_Name =
      new NodeId(UShort.MIN, uint(9409));

  public static final NodeId ExclusiveLimitAlarmType_SuppressedState_Number =
      new NodeId(UShort.MIN, uint(9410));

  public static final NodeId ExclusiveLimitAlarmType_SuppressedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(9411));

  public static final NodeId ExclusiveLimitAlarmType_SuppressedState_TransitionTime =
      new NodeId(UShort.MIN, uint(9412));

  public static final NodeId ExclusiveLimitAlarmType_SuppressedState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(9413));

  public static final NodeId ExclusiveLimitAlarmType_SuppressedState_TrueState =
      new NodeId(UShort.MIN, uint(9414));

  public static final NodeId ExclusiveLimitAlarmType_SuppressedState_FalseState =
      new NodeId(UShort.MIN, uint(9415));

  public static final NodeId ExclusiveLimitAlarmType_ShelvingState =
      new NodeId(UShort.MIN, uint(9416));

  public static final NodeId ExclusiveLimitAlarmType_ShelvingState_CurrentState =
      new NodeId(UShort.MIN, uint(9417));

  public static final NodeId ExclusiveLimitAlarmType_ShelvingState_CurrentState_Id =
      new NodeId(UShort.MIN, uint(9418));

  public static final NodeId ExclusiveLimitAlarmType_ShelvingState_CurrentState_Name =
      new NodeId(UShort.MIN, uint(9419));

  public static final NodeId ExclusiveLimitAlarmType_ShelvingState_CurrentState_Number =
      new NodeId(UShort.MIN, uint(9420));

  public static final NodeId
      ExclusiveLimitAlarmType_ShelvingState_CurrentState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(9421));

  public static final NodeId ExclusiveLimitAlarmType_ShelvingState_LastTransition =
      new NodeId(UShort.MIN, uint(9422));

  public static final NodeId ExclusiveLimitAlarmType_ShelvingState_LastTransition_Id =
      new NodeId(UShort.MIN, uint(9423));

  public static final NodeId ExclusiveLimitAlarmType_ShelvingState_LastTransition_Name =
      new NodeId(UShort.MIN, uint(9424));

  public static final NodeId ExclusiveLimitAlarmType_ShelvingState_LastTransition_Number =
      new NodeId(UShort.MIN, uint(9425));

  public static final NodeId ExclusiveLimitAlarmType_ShelvingState_LastTransition_TransitionTime =
      new NodeId(UShort.MIN, uint(9426));

  public static final NodeId ExclusiveLimitAlarmType_ShelvingState_UnshelveTime =
      new NodeId(UShort.MIN, uint(9427));

  public static final NodeId ExclusiveLimitAlarmType_ShelvingState_Unshelve =
      new NodeId(UShort.MIN, uint(9449));

  public static final NodeId ExclusiveLimitAlarmType_ShelvingState_OneShotShelve =
      new NodeId(UShort.MIN, uint(9450));

  public static final NodeId ExclusiveLimitAlarmType_ShelvingState_TimedShelve =
      new NodeId(UShort.MIN, uint(9451));

  public static final NodeId ExclusiveLimitAlarmType_ShelvingState_TimedShelve_InputArguments =
      new NodeId(UShort.MIN, uint(9452));

  public static final NodeId ExclusiveLimitAlarmType_SuppressedOrShelved =
      new NodeId(UShort.MIN, uint(9453));

  public static final NodeId ExclusiveLimitAlarmType_MaxTimeShelved =
      new NodeId(UShort.MIN, uint(9454));

  public static final NodeId ExclusiveLimitAlarmType_LimitState =
      new NodeId(UShort.MIN, uint(9455));

  public static final NodeId ExclusiveLimitAlarmType_LimitState_CurrentState =
      new NodeId(UShort.MIN, uint(9456));

  public static final NodeId ExclusiveLimitAlarmType_LimitState_CurrentState_Id =
      new NodeId(UShort.MIN, uint(9457));

  public static final NodeId ExclusiveLimitAlarmType_LimitState_CurrentState_Name =
      new NodeId(UShort.MIN, uint(9458));

  public static final NodeId ExclusiveLimitAlarmType_LimitState_CurrentState_Number =
      new NodeId(UShort.MIN, uint(9459));

  public static final NodeId ExclusiveLimitAlarmType_LimitState_CurrentState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(9460));

  public static final NodeId ExclusiveLimitAlarmType_LimitState_LastTransition =
      new NodeId(UShort.MIN, uint(9461));

  public static final NodeId ExclusiveLimitAlarmType_LimitState_LastTransition_Id =
      new NodeId(UShort.MIN, uint(9462));

  public static final NodeId ExclusiveLimitAlarmType_LimitState_LastTransition_Name =
      new NodeId(UShort.MIN, uint(9463));

  public static final NodeId ExclusiveLimitAlarmType_LimitState_LastTransition_Number =
      new NodeId(UShort.MIN, uint(9464));

  public static final NodeId ExclusiveLimitAlarmType_LimitState_LastTransition_TransitionTime =
      new NodeId(UShort.MIN, uint(9465));

  public static final NodeId ExclusiveLimitAlarmType_HighHighLimit =
      new NodeId(UShort.MIN, uint(9478));

  public static final NodeId ExclusiveLimitAlarmType_HighLimit = new NodeId(UShort.MIN, uint(9479));

  public static final NodeId ExclusiveLimitAlarmType_LowLimit = new NodeId(UShort.MIN, uint(9480));

  public static final NodeId ExclusiveLimitAlarmType_LowLowLimit =
      new NodeId(UShort.MIN, uint(9481));

  public static final NodeId ExclusiveLevelAlarmType = new NodeId(UShort.MIN, uint(9482));

  public static final NodeId ExclusiveLevelAlarmType_EventId = new NodeId(UShort.MIN, uint(9483));

  public static final NodeId ExclusiveLevelAlarmType_EventType = new NodeId(UShort.MIN, uint(9484));

  public static final NodeId ExclusiveLevelAlarmType_SourceNode =
      new NodeId(UShort.MIN, uint(9485));

  public static final NodeId ExclusiveLevelAlarmType_SourceName =
      new NodeId(UShort.MIN, uint(9486));

  public static final NodeId ExclusiveLevelAlarmType_Time = new NodeId(UShort.MIN, uint(9487));

  public static final NodeId ExclusiveLevelAlarmType_ReceiveTime =
      new NodeId(UShort.MIN, uint(9488));

  public static final NodeId ExclusiveLevelAlarmType_LocalTime = new NodeId(UShort.MIN, uint(9489));

  public static final NodeId ExclusiveLevelAlarmType_Message = new NodeId(UShort.MIN, uint(9490));

  public static final NodeId ExclusiveLevelAlarmType_Severity = new NodeId(UShort.MIN, uint(9491));

  public static final NodeId ExclusiveLevelAlarmType_ConditionName =
      new NodeId(UShort.MIN, uint(9492));

  public static final NodeId ExclusiveLevelAlarmType_BranchId = new NodeId(UShort.MIN, uint(9493));

  public static final NodeId ExclusiveLevelAlarmType_Retain = new NodeId(UShort.MIN, uint(9494));

  public static final NodeId ExclusiveLevelAlarmType_EnabledState =
      new NodeId(UShort.MIN, uint(9495));

  public static final NodeId ExclusiveLevelAlarmType_EnabledState_Id =
      new NodeId(UShort.MIN, uint(9496));

  public static final NodeId ExclusiveLevelAlarmType_EnabledState_Name =
      new NodeId(UShort.MIN, uint(9497));

  public static final NodeId ExclusiveLevelAlarmType_EnabledState_Number =
      new NodeId(UShort.MIN, uint(9498));

  public static final NodeId ExclusiveLevelAlarmType_EnabledState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(9499));

  public static final NodeId ExclusiveLevelAlarmType_EnabledState_TransitionTime =
      new NodeId(UShort.MIN, uint(9500));

  public static final NodeId ExclusiveLevelAlarmType_EnabledState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(9501));

  public static final NodeId ExclusiveLevelAlarmType_EnabledState_TrueState =
      new NodeId(UShort.MIN, uint(9502));

  public static final NodeId ExclusiveLevelAlarmType_EnabledState_FalseState =
      new NodeId(UShort.MIN, uint(9503));

  public static final NodeId ExclusiveLevelAlarmType_Quality = new NodeId(UShort.MIN, uint(9504));

  public static final NodeId ExclusiveLevelAlarmType_Quality_SourceTimestamp =
      new NodeId(UShort.MIN, uint(9505));

  public static final NodeId ExclusiveLevelAlarmType_LastSeverity =
      new NodeId(UShort.MIN, uint(9506));

  public static final NodeId ExclusiveLevelAlarmType_LastSeverity_SourceTimestamp =
      new NodeId(UShort.MIN, uint(9507));

  public static final NodeId ExclusiveLevelAlarmType_Comment = new NodeId(UShort.MIN, uint(9508));

  public static final NodeId ExclusiveLevelAlarmType_Comment_SourceTimestamp =
      new NodeId(UShort.MIN, uint(9509));

  public static final NodeId ExclusiveLevelAlarmType_ClientUserId =
      new NodeId(UShort.MIN, uint(9510));

  public static final NodeId ExclusiveLevelAlarmType_Enable = new NodeId(UShort.MIN, uint(9511));

  public static final NodeId ExclusiveLevelAlarmType_Disable = new NodeId(UShort.MIN, uint(9512));

  public static final NodeId ExclusiveLevelAlarmType_AddComment =
      new NodeId(UShort.MIN, uint(9513));

  public static final NodeId ExclusiveLevelAlarmType_AddComment_InputArguments =
      new NodeId(UShort.MIN, uint(9514));

  public static final NodeId ExclusiveLevelAlarmType_ConditionRefresh =
      new NodeId(UShort.MIN, uint(9515));

  public static final NodeId ExclusiveLevelAlarmType_ConditionRefresh_InputArguments =
      new NodeId(UShort.MIN, uint(9516));

  public static final NodeId ExclusiveLevelAlarmType_AckedState =
      new NodeId(UShort.MIN, uint(9517));

  public static final NodeId ExclusiveLevelAlarmType_AckedState_Id =
      new NodeId(UShort.MIN, uint(9518));

  public static final NodeId ExclusiveLevelAlarmType_AckedState_Name =
      new NodeId(UShort.MIN, uint(9519));

  public static final NodeId ExclusiveLevelAlarmType_AckedState_Number =
      new NodeId(UShort.MIN, uint(9520));

  public static final NodeId ExclusiveLevelAlarmType_AckedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(9521));

  public static final NodeId ExclusiveLevelAlarmType_AckedState_TransitionTime =
      new NodeId(UShort.MIN, uint(9522));

  public static final NodeId ExclusiveLevelAlarmType_AckedState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(9523));

  public static final NodeId ExclusiveLevelAlarmType_AckedState_TrueState =
      new NodeId(UShort.MIN, uint(9524));

  public static final NodeId ExclusiveLevelAlarmType_AckedState_FalseState =
      new NodeId(UShort.MIN, uint(9525));

  public static final NodeId ExclusiveLevelAlarmType_ConfirmedState =
      new NodeId(UShort.MIN, uint(9526));

  public static final NodeId ExclusiveLevelAlarmType_ConfirmedState_Id =
      new NodeId(UShort.MIN, uint(9527));

  public static final NodeId ExclusiveLevelAlarmType_ConfirmedState_Name =
      new NodeId(UShort.MIN, uint(9528));

  public static final NodeId ExclusiveLevelAlarmType_ConfirmedState_Number =
      new NodeId(UShort.MIN, uint(9529));

  public static final NodeId ExclusiveLevelAlarmType_ConfirmedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(9530));

  public static final NodeId ExclusiveLevelAlarmType_ConfirmedState_TransitionTime =
      new NodeId(UShort.MIN, uint(9531));

  public static final NodeId ExclusiveLevelAlarmType_ConfirmedState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(9532));

  public static final NodeId ExclusiveLevelAlarmType_ConfirmedState_TrueState =
      new NodeId(UShort.MIN, uint(9533));

  public static final NodeId ExclusiveLevelAlarmType_ConfirmedState_FalseState =
      new NodeId(UShort.MIN, uint(9534));

  public static final NodeId ExclusiveLevelAlarmType_Acknowledge =
      new NodeId(UShort.MIN, uint(9535));

  public static final NodeId ExclusiveLevelAlarmType_Acknowledge_InputArguments =
      new NodeId(UShort.MIN, uint(9536));

  public static final NodeId ExclusiveLevelAlarmType_Confirm = new NodeId(UShort.MIN, uint(9537));

  public static final NodeId ExclusiveLevelAlarmType_Confirm_InputArguments =
      new NodeId(UShort.MIN, uint(9538));

  public static final NodeId ExclusiveLevelAlarmType_ActiveState =
      new NodeId(UShort.MIN, uint(9539));

  public static final NodeId ExclusiveLevelAlarmType_ActiveState_Id =
      new NodeId(UShort.MIN, uint(9540));

  public static final NodeId ExclusiveLevelAlarmType_ActiveState_Name =
      new NodeId(UShort.MIN, uint(9541));

  public static final NodeId ExclusiveLevelAlarmType_ActiveState_Number =
      new NodeId(UShort.MIN, uint(9542));

  public static final NodeId ExclusiveLevelAlarmType_ActiveState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(9543));

  public static final NodeId ExclusiveLevelAlarmType_ActiveState_TransitionTime =
      new NodeId(UShort.MIN, uint(9544));

  public static final NodeId ExclusiveLevelAlarmType_ActiveState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(9545));

  public static final NodeId ExclusiveLevelAlarmType_ActiveState_TrueState =
      new NodeId(UShort.MIN, uint(9546));

  public static final NodeId ExclusiveLevelAlarmType_ActiveState_FalseState =
      new NodeId(UShort.MIN, uint(9547));

  public static final NodeId ExclusiveLevelAlarmType_SuppressedState =
      new NodeId(UShort.MIN, uint(9548));

  public static final NodeId ExclusiveLevelAlarmType_SuppressedState_Id =
      new NodeId(UShort.MIN, uint(9549));

  public static final NodeId ExclusiveLevelAlarmType_SuppressedState_Name =
      new NodeId(UShort.MIN, uint(9550));

  public static final NodeId ExclusiveLevelAlarmType_SuppressedState_Number =
      new NodeId(UShort.MIN, uint(9551));

  public static final NodeId ExclusiveLevelAlarmType_SuppressedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(9552));

  public static final NodeId ExclusiveLevelAlarmType_SuppressedState_TransitionTime =
      new NodeId(UShort.MIN, uint(9553));

  public static final NodeId ExclusiveLevelAlarmType_SuppressedState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(9554));

  public static final NodeId ExclusiveLevelAlarmType_SuppressedState_TrueState =
      new NodeId(UShort.MIN, uint(9555));

  public static final NodeId ExclusiveLevelAlarmType_SuppressedState_FalseState =
      new NodeId(UShort.MIN, uint(9556));

  public static final NodeId ExclusiveLevelAlarmType_ShelvingState =
      new NodeId(UShort.MIN, uint(9557));

  public static final NodeId ExclusiveLevelAlarmType_ShelvingState_CurrentState =
      new NodeId(UShort.MIN, uint(9558));

  public static final NodeId ExclusiveLevelAlarmType_ShelvingState_CurrentState_Id =
      new NodeId(UShort.MIN, uint(9559));

  public static final NodeId ExclusiveLevelAlarmType_ShelvingState_CurrentState_Name =
      new NodeId(UShort.MIN, uint(9560));

  public static final NodeId ExclusiveLevelAlarmType_ShelvingState_CurrentState_Number =
      new NodeId(UShort.MIN, uint(9561));

  public static final NodeId
      ExclusiveLevelAlarmType_ShelvingState_CurrentState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(9562));

  public static final NodeId ExclusiveLevelAlarmType_ShelvingState_LastTransition =
      new NodeId(UShort.MIN, uint(9563));

  public static final NodeId ExclusiveLevelAlarmType_ShelvingState_LastTransition_Id =
      new NodeId(UShort.MIN, uint(9564));

  public static final NodeId ExclusiveLevelAlarmType_ShelvingState_LastTransition_Name =
      new NodeId(UShort.MIN, uint(9565));

  public static final NodeId ExclusiveLevelAlarmType_ShelvingState_LastTransition_Number =
      new NodeId(UShort.MIN, uint(9566));

  public static final NodeId ExclusiveLevelAlarmType_ShelvingState_LastTransition_TransitionTime =
      new NodeId(UShort.MIN, uint(9567));

  public static final NodeId ExclusiveLevelAlarmType_ShelvingState_UnshelveTime =
      new NodeId(UShort.MIN, uint(9568));

  public static final NodeId ExclusiveLevelAlarmType_ShelvingState_Unshelve =
      new NodeId(UShort.MIN, uint(9590));

  public static final NodeId ExclusiveLevelAlarmType_ShelvingState_OneShotShelve =
      new NodeId(UShort.MIN, uint(9591));

  public static final NodeId ExclusiveLevelAlarmType_ShelvingState_TimedShelve =
      new NodeId(UShort.MIN, uint(9592));

  public static final NodeId ExclusiveLevelAlarmType_ShelvingState_TimedShelve_InputArguments =
      new NodeId(UShort.MIN, uint(9593));

  public static final NodeId ExclusiveLevelAlarmType_SuppressedOrShelved =
      new NodeId(UShort.MIN, uint(9594));

  public static final NodeId ExclusiveLevelAlarmType_MaxTimeShelved =
      new NodeId(UShort.MIN, uint(9595));

  public static final NodeId ExclusiveLevelAlarmType_LimitState =
      new NodeId(UShort.MIN, uint(9596));

  public static final NodeId ExclusiveLevelAlarmType_LimitState_CurrentState =
      new NodeId(UShort.MIN, uint(9597));

  public static final NodeId ExclusiveLevelAlarmType_LimitState_CurrentState_Id =
      new NodeId(UShort.MIN, uint(9598));

  public static final NodeId ExclusiveLevelAlarmType_LimitState_CurrentState_Name =
      new NodeId(UShort.MIN, uint(9599));

  public static final NodeId ExclusiveLevelAlarmType_LimitState_CurrentState_Number =
      new NodeId(UShort.MIN, uint(9600));

  public static final NodeId ExclusiveLevelAlarmType_LimitState_CurrentState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(9601));

  public static final NodeId ExclusiveLevelAlarmType_LimitState_LastTransition =
      new NodeId(UShort.MIN, uint(9602));

  public static final NodeId ExclusiveLevelAlarmType_LimitState_LastTransition_Id =
      new NodeId(UShort.MIN, uint(9603));

  public static final NodeId ExclusiveLevelAlarmType_LimitState_LastTransition_Name =
      new NodeId(UShort.MIN, uint(9604));

  public static final NodeId ExclusiveLevelAlarmType_LimitState_LastTransition_Number =
      new NodeId(UShort.MIN, uint(9605));

  public static final NodeId ExclusiveLevelAlarmType_LimitState_LastTransition_TransitionTime =
      new NodeId(UShort.MIN, uint(9606));

  public static final NodeId ExclusiveLevelAlarmType_HighHighLimit =
      new NodeId(UShort.MIN, uint(9619));

  public static final NodeId ExclusiveLevelAlarmType_HighLimit = new NodeId(UShort.MIN, uint(9620));

  public static final NodeId ExclusiveLevelAlarmType_LowLimit = new NodeId(UShort.MIN, uint(9621));

  public static final NodeId ExclusiveLevelAlarmType_LowLowLimit =
      new NodeId(UShort.MIN, uint(9622));

  public static final NodeId ExclusiveRateOfChangeAlarmType = new NodeId(UShort.MIN, uint(9623));

  public static final NodeId ExclusiveRateOfChangeAlarmType_EventId =
      new NodeId(UShort.MIN, uint(9624));

  public static final NodeId ExclusiveRateOfChangeAlarmType_EventType =
      new NodeId(UShort.MIN, uint(9625));

  public static final NodeId ExclusiveRateOfChangeAlarmType_SourceNode =
      new NodeId(UShort.MIN, uint(9626));

  public static final NodeId ExclusiveRateOfChangeAlarmType_SourceName =
      new NodeId(UShort.MIN, uint(9627));

  public static final NodeId ExclusiveRateOfChangeAlarmType_Time =
      new NodeId(UShort.MIN, uint(9628));

  public static final NodeId ExclusiveRateOfChangeAlarmType_ReceiveTime =
      new NodeId(UShort.MIN, uint(9629));

  public static final NodeId ExclusiveRateOfChangeAlarmType_LocalTime =
      new NodeId(UShort.MIN, uint(9630));

  public static final NodeId ExclusiveRateOfChangeAlarmType_Message =
      new NodeId(UShort.MIN, uint(9631));

  public static final NodeId ExclusiveRateOfChangeAlarmType_Severity =
      new NodeId(UShort.MIN, uint(9632));

  public static final NodeId ExclusiveRateOfChangeAlarmType_ConditionName =
      new NodeId(UShort.MIN, uint(9633));

  public static final NodeId ExclusiveRateOfChangeAlarmType_BranchId =
      new NodeId(UShort.MIN, uint(9634));

  public static final NodeId ExclusiveRateOfChangeAlarmType_Retain =
      new NodeId(UShort.MIN, uint(9635));

  public static final NodeId ExclusiveRateOfChangeAlarmType_EnabledState =
      new NodeId(UShort.MIN, uint(9636));

  public static final NodeId ExclusiveRateOfChangeAlarmType_EnabledState_Id =
      new NodeId(UShort.MIN, uint(9637));

  public static final NodeId ExclusiveRateOfChangeAlarmType_EnabledState_Name =
      new NodeId(UShort.MIN, uint(9638));

  public static final NodeId ExclusiveRateOfChangeAlarmType_EnabledState_Number =
      new NodeId(UShort.MIN, uint(9639));

  public static final NodeId ExclusiveRateOfChangeAlarmType_EnabledState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(9640));

  public static final NodeId ExclusiveRateOfChangeAlarmType_EnabledState_TransitionTime =
      new NodeId(UShort.MIN, uint(9641));

  public static final NodeId ExclusiveRateOfChangeAlarmType_EnabledState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(9642));

  public static final NodeId ExclusiveRateOfChangeAlarmType_EnabledState_TrueState =
      new NodeId(UShort.MIN, uint(9643));

  public static final NodeId ExclusiveRateOfChangeAlarmType_EnabledState_FalseState =
      new NodeId(UShort.MIN, uint(9644));

  public static final NodeId ExclusiveRateOfChangeAlarmType_Quality =
      new NodeId(UShort.MIN, uint(9645));

  public static final NodeId ExclusiveRateOfChangeAlarmType_Quality_SourceTimestamp =
      new NodeId(UShort.MIN, uint(9646));

  public static final NodeId ExclusiveRateOfChangeAlarmType_LastSeverity =
      new NodeId(UShort.MIN, uint(9647));

  public static final NodeId ExclusiveRateOfChangeAlarmType_LastSeverity_SourceTimestamp =
      new NodeId(UShort.MIN, uint(9648));

  public static final NodeId ExclusiveRateOfChangeAlarmType_Comment =
      new NodeId(UShort.MIN, uint(9649));

  public static final NodeId ExclusiveRateOfChangeAlarmType_Comment_SourceTimestamp =
      new NodeId(UShort.MIN, uint(9650));

  public static final NodeId ExclusiveRateOfChangeAlarmType_ClientUserId =
      new NodeId(UShort.MIN, uint(9651));

  public static final NodeId ExclusiveRateOfChangeAlarmType_Enable =
      new NodeId(UShort.MIN, uint(9652));

  public static final NodeId ExclusiveRateOfChangeAlarmType_Disable =
      new NodeId(UShort.MIN, uint(9653));

  public static final NodeId ExclusiveRateOfChangeAlarmType_AddComment =
      new NodeId(UShort.MIN, uint(9654));

  public static final NodeId ExclusiveRateOfChangeAlarmType_AddComment_InputArguments =
      new NodeId(UShort.MIN, uint(9655));

  public static final NodeId ExclusiveRateOfChangeAlarmType_ConditionRefresh =
      new NodeId(UShort.MIN, uint(9656));

  public static final NodeId ExclusiveRateOfChangeAlarmType_ConditionRefresh_InputArguments =
      new NodeId(UShort.MIN, uint(9657));

  public static final NodeId ExclusiveRateOfChangeAlarmType_AckedState =
      new NodeId(UShort.MIN, uint(9658));

  public static final NodeId ExclusiveRateOfChangeAlarmType_AckedState_Id =
      new NodeId(UShort.MIN, uint(9659));

  public static final NodeId ExclusiveRateOfChangeAlarmType_AckedState_Name =
      new NodeId(UShort.MIN, uint(9660));

  public static final NodeId ExclusiveRateOfChangeAlarmType_AckedState_Number =
      new NodeId(UShort.MIN, uint(9661));

  public static final NodeId ExclusiveRateOfChangeAlarmType_AckedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(9662));

  public static final NodeId ExclusiveRateOfChangeAlarmType_AckedState_TransitionTime =
      new NodeId(UShort.MIN, uint(9663));

  public static final NodeId ExclusiveRateOfChangeAlarmType_AckedState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(9664));

  public static final NodeId ExclusiveRateOfChangeAlarmType_AckedState_TrueState =
      new NodeId(UShort.MIN, uint(9665));

  public static final NodeId ExclusiveRateOfChangeAlarmType_AckedState_FalseState =
      new NodeId(UShort.MIN, uint(9666));

  public static final NodeId ExclusiveRateOfChangeAlarmType_ConfirmedState =
      new NodeId(UShort.MIN, uint(9667));

  public static final NodeId ExclusiveRateOfChangeAlarmType_ConfirmedState_Id =
      new NodeId(UShort.MIN, uint(9668));

  public static final NodeId ExclusiveRateOfChangeAlarmType_ConfirmedState_Name =
      new NodeId(UShort.MIN, uint(9669));

  public static final NodeId ExclusiveRateOfChangeAlarmType_ConfirmedState_Number =
      new NodeId(UShort.MIN, uint(9670));

  public static final NodeId ExclusiveRateOfChangeAlarmType_ConfirmedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(9671));

  public static final NodeId ExclusiveRateOfChangeAlarmType_ConfirmedState_TransitionTime =
      new NodeId(UShort.MIN, uint(9672));

  public static final NodeId ExclusiveRateOfChangeAlarmType_ConfirmedState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(9673));

  public static final NodeId ExclusiveRateOfChangeAlarmType_ConfirmedState_TrueState =
      new NodeId(UShort.MIN, uint(9674));

  public static final NodeId ExclusiveRateOfChangeAlarmType_ConfirmedState_FalseState =
      new NodeId(UShort.MIN, uint(9675));

  public static final NodeId ExclusiveRateOfChangeAlarmType_Acknowledge =
      new NodeId(UShort.MIN, uint(9676));

  public static final NodeId ExclusiveRateOfChangeAlarmType_Acknowledge_InputArguments =
      new NodeId(UShort.MIN, uint(9677));

  public static final NodeId ExclusiveRateOfChangeAlarmType_Confirm =
      new NodeId(UShort.MIN, uint(9678));

  public static final NodeId ExclusiveRateOfChangeAlarmType_Confirm_InputArguments =
      new NodeId(UShort.MIN, uint(9679));

  public static final NodeId ExclusiveRateOfChangeAlarmType_ActiveState =
      new NodeId(UShort.MIN, uint(9680));

  public static final NodeId ExclusiveRateOfChangeAlarmType_ActiveState_Id =
      new NodeId(UShort.MIN, uint(9681));

  public static final NodeId ExclusiveRateOfChangeAlarmType_ActiveState_Name =
      new NodeId(UShort.MIN, uint(9682));

  public static final NodeId ExclusiveRateOfChangeAlarmType_ActiveState_Number =
      new NodeId(UShort.MIN, uint(9683));

  public static final NodeId ExclusiveRateOfChangeAlarmType_ActiveState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(9684));

  public static final NodeId ExclusiveRateOfChangeAlarmType_ActiveState_TransitionTime =
      new NodeId(UShort.MIN, uint(9685));

  public static final NodeId ExclusiveRateOfChangeAlarmType_ActiveState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(9686));

  public static final NodeId ExclusiveRateOfChangeAlarmType_ActiveState_TrueState =
      new NodeId(UShort.MIN, uint(9687));

  public static final NodeId ExclusiveRateOfChangeAlarmType_ActiveState_FalseState =
      new NodeId(UShort.MIN, uint(9688));

  public static final NodeId ExclusiveRateOfChangeAlarmType_SuppressedState =
      new NodeId(UShort.MIN, uint(9689));

  public static final NodeId ExclusiveRateOfChangeAlarmType_SuppressedState_Id =
      new NodeId(UShort.MIN, uint(9690));

  public static final NodeId ExclusiveRateOfChangeAlarmType_SuppressedState_Name =
      new NodeId(UShort.MIN, uint(9691));

  public static final NodeId ExclusiveRateOfChangeAlarmType_SuppressedState_Number =
      new NodeId(UShort.MIN, uint(9692));

  public static final NodeId ExclusiveRateOfChangeAlarmType_SuppressedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(9693));

  public static final NodeId ExclusiveRateOfChangeAlarmType_SuppressedState_TransitionTime =
      new NodeId(UShort.MIN, uint(9694));

  public static final NodeId
      ExclusiveRateOfChangeAlarmType_SuppressedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(9695));

  public static final NodeId ExclusiveRateOfChangeAlarmType_SuppressedState_TrueState =
      new NodeId(UShort.MIN, uint(9696));

  public static final NodeId ExclusiveRateOfChangeAlarmType_SuppressedState_FalseState =
      new NodeId(UShort.MIN, uint(9697));

  public static final NodeId ExclusiveRateOfChangeAlarmType_ShelvingState =
      new NodeId(UShort.MIN, uint(9698));

  public static final NodeId ExclusiveRateOfChangeAlarmType_ShelvingState_CurrentState =
      new NodeId(UShort.MIN, uint(9699));

  public static final NodeId ExclusiveRateOfChangeAlarmType_ShelvingState_CurrentState_Id =
      new NodeId(UShort.MIN, uint(9700));

  public static final NodeId ExclusiveRateOfChangeAlarmType_ShelvingState_CurrentState_Name =
      new NodeId(UShort.MIN, uint(9701));

  public static final NodeId ExclusiveRateOfChangeAlarmType_ShelvingState_CurrentState_Number =
      new NodeId(UShort.MIN, uint(9702));

  public static final NodeId
      ExclusiveRateOfChangeAlarmType_ShelvingState_CurrentState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(9703));

  public static final NodeId ExclusiveRateOfChangeAlarmType_ShelvingState_LastTransition =
      new NodeId(UShort.MIN, uint(9704));

  public static final NodeId ExclusiveRateOfChangeAlarmType_ShelvingState_LastTransition_Id =
      new NodeId(UShort.MIN, uint(9705));

  public static final NodeId ExclusiveRateOfChangeAlarmType_ShelvingState_LastTransition_Name =
      new NodeId(UShort.MIN, uint(9706));

  public static final NodeId ExclusiveRateOfChangeAlarmType_ShelvingState_LastTransition_Number =
      new NodeId(UShort.MIN, uint(9707));

  public static final NodeId
      ExclusiveRateOfChangeAlarmType_ShelvingState_LastTransition_TransitionTime =
          new NodeId(UShort.MIN, uint(9708));

  public static final NodeId ExclusiveRateOfChangeAlarmType_ShelvingState_UnshelveTime =
      new NodeId(UShort.MIN, uint(9709));

  public static final NodeId ExclusiveRateOfChangeAlarmType_ShelvingState_Unshelve =
      new NodeId(UShort.MIN, uint(9731));

  public static final NodeId ExclusiveRateOfChangeAlarmType_ShelvingState_OneShotShelve =
      new NodeId(UShort.MIN, uint(9732));

  public static final NodeId ExclusiveRateOfChangeAlarmType_ShelvingState_TimedShelve =
      new NodeId(UShort.MIN, uint(9733));

  public static final NodeId
      ExclusiveRateOfChangeAlarmType_ShelvingState_TimedShelve_InputArguments =
          new NodeId(UShort.MIN, uint(9734));

  public static final NodeId ExclusiveRateOfChangeAlarmType_SuppressedOrShelved =
      new NodeId(UShort.MIN, uint(9735));

  public static final NodeId ExclusiveRateOfChangeAlarmType_MaxTimeShelved =
      new NodeId(UShort.MIN, uint(9736));

  public static final NodeId ExclusiveRateOfChangeAlarmType_LimitState =
      new NodeId(UShort.MIN, uint(9737));

  public static final NodeId ExclusiveRateOfChangeAlarmType_LimitState_CurrentState =
      new NodeId(UShort.MIN, uint(9738));

  public static final NodeId ExclusiveRateOfChangeAlarmType_LimitState_CurrentState_Id =
      new NodeId(UShort.MIN, uint(9739));

  public static final NodeId ExclusiveRateOfChangeAlarmType_LimitState_CurrentState_Name =
      new NodeId(UShort.MIN, uint(9740));

  public static final NodeId ExclusiveRateOfChangeAlarmType_LimitState_CurrentState_Number =
      new NodeId(UShort.MIN, uint(9741));

  public static final NodeId
      ExclusiveRateOfChangeAlarmType_LimitState_CurrentState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(9742));

  public static final NodeId ExclusiveRateOfChangeAlarmType_LimitState_LastTransition =
      new NodeId(UShort.MIN, uint(9743));

  public static final NodeId ExclusiveRateOfChangeAlarmType_LimitState_LastTransition_Id =
      new NodeId(UShort.MIN, uint(9744));

  public static final NodeId ExclusiveRateOfChangeAlarmType_LimitState_LastTransition_Name =
      new NodeId(UShort.MIN, uint(9745));

  public static final NodeId ExclusiveRateOfChangeAlarmType_LimitState_LastTransition_Number =
      new NodeId(UShort.MIN, uint(9746));

  public static final NodeId
      ExclusiveRateOfChangeAlarmType_LimitState_LastTransition_TransitionTime =
          new NodeId(UShort.MIN, uint(9747));

  public static final NodeId ExclusiveRateOfChangeAlarmType_HighHighLimit =
      new NodeId(UShort.MIN, uint(9760));

  public static final NodeId ExclusiveRateOfChangeAlarmType_HighLimit =
      new NodeId(UShort.MIN, uint(9761));

  public static final NodeId ExclusiveRateOfChangeAlarmType_LowLimit =
      new NodeId(UShort.MIN, uint(9762));

  public static final NodeId ExclusiveRateOfChangeAlarmType_LowLowLimit =
      new NodeId(UShort.MIN, uint(9763));

  public static final NodeId ExclusiveDeviationAlarmType = new NodeId(UShort.MIN, uint(9764));

  public static final NodeId ExclusiveDeviationAlarmType_EventId =
      new NodeId(UShort.MIN, uint(9765));

  public static final NodeId ExclusiveDeviationAlarmType_EventType =
      new NodeId(UShort.MIN, uint(9766));

  public static final NodeId ExclusiveDeviationAlarmType_SourceNode =
      new NodeId(UShort.MIN, uint(9767));

  public static final NodeId ExclusiveDeviationAlarmType_SourceName =
      new NodeId(UShort.MIN, uint(9768));

  public static final NodeId ExclusiveDeviationAlarmType_Time = new NodeId(UShort.MIN, uint(9769));

  public static final NodeId ExclusiveDeviationAlarmType_ReceiveTime =
      new NodeId(UShort.MIN, uint(9770));

  public static final NodeId ExclusiveDeviationAlarmType_LocalTime =
      new NodeId(UShort.MIN, uint(9771));

  public static final NodeId ExclusiveDeviationAlarmType_Message =
      new NodeId(UShort.MIN, uint(9772));

  public static final NodeId ExclusiveDeviationAlarmType_Severity =
      new NodeId(UShort.MIN, uint(9773));

  public static final NodeId ExclusiveDeviationAlarmType_ConditionName =
      new NodeId(UShort.MIN, uint(9774));

  public static final NodeId ExclusiveDeviationAlarmType_BranchId =
      new NodeId(UShort.MIN, uint(9775));

  public static final NodeId ExclusiveDeviationAlarmType_Retain =
      new NodeId(UShort.MIN, uint(9776));

  public static final NodeId ExclusiveDeviationAlarmType_EnabledState =
      new NodeId(UShort.MIN, uint(9777));

  public static final NodeId ExclusiveDeviationAlarmType_EnabledState_Id =
      new NodeId(UShort.MIN, uint(9778));

  public static final NodeId ExclusiveDeviationAlarmType_EnabledState_Name =
      new NodeId(UShort.MIN, uint(9779));

  public static final NodeId ExclusiveDeviationAlarmType_EnabledState_Number =
      new NodeId(UShort.MIN, uint(9780));

  public static final NodeId ExclusiveDeviationAlarmType_EnabledState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(9781));

  public static final NodeId ExclusiveDeviationAlarmType_EnabledState_TransitionTime =
      new NodeId(UShort.MIN, uint(9782));

  public static final NodeId ExclusiveDeviationAlarmType_EnabledState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(9783));

  public static final NodeId ExclusiveDeviationAlarmType_EnabledState_TrueState =
      new NodeId(UShort.MIN, uint(9784));

  public static final NodeId ExclusiveDeviationAlarmType_EnabledState_FalseState =
      new NodeId(UShort.MIN, uint(9785));

  public static final NodeId ExclusiveDeviationAlarmType_Quality =
      new NodeId(UShort.MIN, uint(9786));

  public static final NodeId ExclusiveDeviationAlarmType_Quality_SourceTimestamp =
      new NodeId(UShort.MIN, uint(9787));

  public static final NodeId ExclusiveDeviationAlarmType_LastSeverity =
      new NodeId(UShort.MIN, uint(9788));

  public static final NodeId ExclusiveDeviationAlarmType_LastSeverity_SourceTimestamp =
      new NodeId(UShort.MIN, uint(9789));

  public static final NodeId ExclusiveDeviationAlarmType_Comment =
      new NodeId(UShort.MIN, uint(9790));

  public static final NodeId ExclusiveDeviationAlarmType_Comment_SourceTimestamp =
      new NodeId(UShort.MIN, uint(9791));

  public static final NodeId ExclusiveDeviationAlarmType_ClientUserId =
      new NodeId(UShort.MIN, uint(9792));

  public static final NodeId ExclusiveDeviationAlarmType_Enable =
      new NodeId(UShort.MIN, uint(9793));

  public static final NodeId ExclusiveDeviationAlarmType_Disable =
      new NodeId(UShort.MIN, uint(9794));

  public static final NodeId ExclusiveDeviationAlarmType_AddComment =
      new NodeId(UShort.MIN, uint(9795));

  public static final NodeId ExclusiveDeviationAlarmType_AddComment_InputArguments =
      new NodeId(UShort.MIN, uint(9796));

  public static final NodeId ExclusiveDeviationAlarmType_ConditionRefresh =
      new NodeId(UShort.MIN, uint(9797));

  public static final NodeId ExclusiveDeviationAlarmType_ConditionRefresh_InputArguments =
      new NodeId(UShort.MIN, uint(9798));

  public static final NodeId ExclusiveDeviationAlarmType_AckedState =
      new NodeId(UShort.MIN, uint(9799));

  public static final NodeId ExclusiveDeviationAlarmType_AckedState_Id =
      new NodeId(UShort.MIN, uint(9800));

  public static final NodeId ExclusiveDeviationAlarmType_AckedState_Name =
      new NodeId(UShort.MIN, uint(9801));

  public static final NodeId ExclusiveDeviationAlarmType_AckedState_Number =
      new NodeId(UShort.MIN, uint(9802));

  public static final NodeId ExclusiveDeviationAlarmType_AckedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(9803));

  public static final NodeId ExclusiveDeviationAlarmType_AckedState_TransitionTime =
      new NodeId(UShort.MIN, uint(9804));

  public static final NodeId ExclusiveDeviationAlarmType_AckedState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(9805));

  public static final NodeId ExclusiveDeviationAlarmType_AckedState_TrueState =
      new NodeId(UShort.MIN, uint(9806));

  public static final NodeId ExclusiveDeviationAlarmType_AckedState_FalseState =
      new NodeId(UShort.MIN, uint(9807));

  public static final NodeId ExclusiveDeviationAlarmType_ConfirmedState =
      new NodeId(UShort.MIN, uint(9808));

  public static final NodeId ExclusiveDeviationAlarmType_ConfirmedState_Id =
      new NodeId(UShort.MIN, uint(9809));

  public static final NodeId ExclusiveDeviationAlarmType_ConfirmedState_Name =
      new NodeId(UShort.MIN, uint(9810));

  public static final NodeId ExclusiveDeviationAlarmType_ConfirmedState_Number =
      new NodeId(UShort.MIN, uint(9811));

  public static final NodeId ExclusiveDeviationAlarmType_ConfirmedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(9812));

  public static final NodeId ExclusiveDeviationAlarmType_ConfirmedState_TransitionTime =
      new NodeId(UShort.MIN, uint(9813));

  public static final NodeId ExclusiveDeviationAlarmType_ConfirmedState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(9814));

  public static final NodeId ExclusiveDeviationAlarmType_ConfirmedState_TrueState =
      new NodeId(UShort.MIN, uint(9815));

  public static final NodeId ExclusiveDeviationAlarmType_ConfirmedState_FalseState =
      new NodeId(UShort.MIN, uint(9816));

  public static final NodeId ExclusiveDeviationAlarmType_Acknowledge =
      new NodeId(UShort.MIN, uint(9817));

  public static final NodeId ExclusiveDeviationAlarmType_Acknowledge_InputArguments =
      new NodeId(UShort.MIN, uint(9818));

  public static final NodeId ExclusiveDeviationAlarmType_Confirm =
      new NodeId(UShort.MIN, uint(9819));

  public static final NodeId ExclusiveDeviationAlarmType_Confirm_InputArguments =
      new NodeId(UShort.MIN, uint(9820));

  public static final NodeId ExclusiveDeviationAlarmType_ActiveState =
      new NodeId(UShort.MIN, uint(9821));

  public static final NodeId ExclusiveDeviationAlarmType_ActiveState_Id =
      new NodeId(UShort.MIN, uint(9822));

  public static final NodeId ExclusiveDeviationAlarmType_ActiveState_Name =
      new NodeId(UShort.MIN, uint(9823));

  public static final NodeId ExclusiveDeviationAlarmType_ActiveState_Number =
      new NodeId(UShort.MIN, uint(9824));

  public static final NodeId ExclusiveDeviationAlarmType_ActiveState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(9825));

  public static final NodeId ExclusiveDeviationAlarmType_ActiveState_TransitionTime =
      new NodeId(UShort.MIN, uint(9826));

  public static final NodeId ExclusiveDeviationAlarmType_ActiveState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(9827));

  public static final NodeId ExclusiveDeviationAlarmType_ActiveState_TrueState =
      new NodeId(UShort.MIN, uint(9828));

  public static final NodeId ExclusiveDeviationAlarmType_ActiveState_FalseState =
      new NodeId(UShort.MIN, uint(9829));

  public static final NodeId ExclusiveDeviationAlarmType_SuppressedState =
      new NodeId(UShort.MIN, uint(9830));

  public static final NodeId ExclusiveDeviationAlarmType_SuppressedState_Id =
      new NodeId(UShort.MIN, uint(9831));

  public static final NodeId ExclusiveDeviationAlarmType_SuppressedState_Name =
      new NodeId(UShort.MIN, uint(9832));

  public static final NodeId ExclusiveDeviationAlarmType_SuppressedState_Number =
      new NodeId(UShort.MIN, uint(9833));

  public static final NodeId ExclusiveDeviationAlarmType_SuppressedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(9834));

  public static final NodeId ExclusiveDeviationAlarmType_SuppressedState_TransitionTime =
      new NodeId(UShort.MIN, uint(9835));

  public static final NodeId ExclusiveDeviationAlarmType_SuppressedState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(9836));

  public static final NodeId ExclusiveDeviationAlarmType_SuppressedState_TrueState =
      new NodeId(UShort.MIN, uint(9837));

  public static final NodeId ExclusiveDeviationAlarmType_SuppressedState_FalseState =
      new NodeId(UShort.MIN, uint(9838));

  public static final NodeId ExclusiveDeviationAlarmType_ShelvingState =
      new NodeId(UShort.MIN, uint(9839));

  public static final NodeId ExclusiveDeviationAlarmType_ShelvingState_CurrentState =
      new NodeId(UShort.MIN, uint(9840));

  public static final NodeId ExclusiveDeviationAlarmType_ShelvingState_CurrentState_Id =
      new NodeId(UShort.MIN, uint(9841));

  public static final NodeId ExclusiveDeviationAlarmType_ShelvingState_CurrentState_Name =
      new NodeId(UShort.MIN, uint(9842));

  public static final NodeId ExclusiveDeviationAlarmType_ShelvingState_CurrentState_Number =
      new NodeId(UShort.MIN, uint(9843));

  public static final NodeId
      ExclusiveDeviationAlarmType_ShelvingState_CurrentState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(9844));

  public static final NodeId ExclusiveDeviationAlarmType_ShelvingState_LastTransition =
      new NodeId(UShort.MIN, uint(9845));

  public static final NodeId ExclusiveDeviationAlarmType_ShelvingState_LastTransition_Id =
      new NodeId(UShort.MIN, uint(9846));

  public static final NodeId ExclusiveDeviationAlarmType_ShelvingState_LastTransition_Name =
      new NodeId(UShort.MIN, uint(9847));

  public static final NodeId ExclusiveDeviationAlarmType_ShelvingState_LastTransition_Number =
      new NodeId(UShort.MIN, uint(9848));

  public static final NodeId
      ExclusiveDeviationAlarmType_ShelvingState_LastTransition_TransitionTime =
          new NodeId(UShort.MIN, uint(9849));

  public static final NodeId ExclusiveDeviationAlarmType_ShelvingState_UnshelveTime =
      new NodeId(UShort.MIN, uint(9850));

  public static final NodeId ExclusiveDeviationAlarmType_ShelvingState_Unshelve =
      new NodeId(UShort.MIN, uint(9872));

  public static final NodeId ExclusiveDeviationAlarmType_ShelvingState_OneShotShelve =
      new NodeId(UShort.MIN, uint(9873));

  public static final NodeId ExclusiveDeviationAlarmType_ShelvingState_TimedShelve =
      new NodeId(UShort.MIN, uint(9874));

  public static final NodeId ExclusiveDeviationAlarmType_ShelvingState_TimedShelve_InputArguments =
      new NodeId(UShort.MIN, uint(9875));

  public static final NodeId ExclusiveDeviationAlarmType_SuppressedOrShelved =
      new NodeId(UShort.MIN, uint(9876));

  public static final NodeId ExclusiveDeviationAlarmType_MaxTimeShelved =
      new NodeId(UShort.MIN, uint(9877));

  public static final NodeId ExclusiveDeviationAlarmType_LimitState =
      new NodeId(UShort.MIN, uint(9878));

  public static final NodeId ExclusiveDeviationAlarmType_LimitState_CurrentState =
      new NodeId(UShort.MIN, uint(9879));

  public static final NodeId ExclusiveDeviationAlarmType_LimitState_CurrentState_Id =
      new NodeId(UShort.MIN, uint(9880));

  public static final NodeId ExclusiveDeviationAlarmType_LimitState_CurrentState_Name =
      new NodeId(UShort.MIN, uint(9881));

  public static final NodeId ExclusiveDeviationAlarmType_LimitState_CurrentState_Number =
      new NodeId(UShort.MIN, uint(9882));

  public static final NodeId
      ExclusiveDeviationAlarmType_LimitState_CurrentState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(9883));

  public static final NodeId ExclusiveDeviationAlarmType_LimitState_LastTransition =
      new NodeId(UShort.MIN, uint(9884));

  public static final NodeId ExclusiveDeviationAlarmType_LimitState_LastTransition_Id =
      new NodeId(UShort.MIN, uint(9885));

  public static final NodeId ExclusiveDeviationAlarmType_LimitState_LastTransition_Name =
      new NodeId(UShort.MIN, uint(9886));

  public static final NodeId ExclusiveDeviationAlarmType_LimitState_LastTransition_Number =
      new NodeId(UShort.MIN, uint(9887));

  public static final NodeId ExclusiveDeviationAlarmType_LimitState_LastTransition_TransitionTime =
      new NodeId(UShort.MIN, uint(9888));

  public static final NodeId ExclusiveDeviationAlarmType_HighHighLimit =
      new NodeId(UShort.MIN, uint(9901));

  public static final NodeId ExclusiveDeviationAlarmType_HighLimit =
      new NodeId(UShort.MIN, uint(9902));

  public static final NodeId ExclusiveDeviationAlarmType_LowLimit =
      new NodeId(UShort.MIN, uint(9903));

  public static final NodeId ExclusiveDeviationAlarmType_LowLowLimit =
      new NodeId(UShort.MIN, uint(9904));

  public static final NodeId ExclusiveDeviationAlarmType_SetpointNode =
      new NodeId(UShort.MIN, uint(9905));

  public static final NodeId NonExclusiveLimitAlarmType = new NodeId(UShort.MIN, uint(9906));

  public static final NodeId NonExclusiveLimitAlarmType_EventId =
      new NodeId(UShort.MIN, uint(9907));

  public static final NodeId NonExclusiveLimitAlarmType_EventType =
      new NodeId(UShort.MIN, uint(9908));

  public static final NodeId NonExclusiveLimitAlarmType_SourceNode =
      new NodeId(UShort.MIN, uint(9909));

  public static final NodeId NonExclusiveLimitAlarmType_SourceName =
      new NodeId(UShort.MIN, uint(9910));

  public static final NodeId NonExclusiveLimitAlarmType_Time = new NodeId(UShort.MIN, uint(9911));

  public static final NodeId NonExclusiveLimitAlarmType_ReceiveTime =
      new NodeId(UShort.MIN, uint(9912));

  public static final NodeId NonExclusiveLimitAlarmType_LocalTime =
      new NodeId(UShort.MIN, uint(9913));

  public static final NodeId NonExclusiveLimitAlarmType_Message =
      new NodeId(UShort.MIN, uint(9914));

  public static final NodeId NonExclusiveLimitAlarmType_Severity =
      new NodeId(UShort.MIN, uint(9915));

  public static final NodeId NonExclusiveLimitAlarmType_ConditionName =
      new NodeId(UShort.MIN, uint(9916));

  public static final NodeId NonExclusiveLimitAlarmType_BranchId =
      new NodeId(UShort.MIN, uint(9917));

  public static final NodeId NonExclusiveLimitAlarmType_Retain = new NodeId(UShort.MIN, uint(9918));

  public static final NodeId NonExclusiveLimitAlarmType_EnabledState =
      new NodeId(UShort.MIN, uint(9919));

  public static final NodeId NonExclusiveLimitAlarmType_EnabledState_Id =
      new NodeId(UShort.MIN, uint(9920));

  public static final NodeId NonExclusiveLimitAlarmType_EnabledState_Name =
      new NodeId(UShort.MIN, uint(9921));

  public static final NodeId NonExclusiveLimitAlarmType_EnabledState_Number =
      new NodeId(UShort.MIN, uint(9922));

  public static final NodeId NonExclusiveLimitAlarmType_EnabledState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(9923));

  public static final NodeId NonExclusiveLimitAlarmType_EnabledState_TransitionTime =
      new NodeId(UShort.MIN, uint(9924));

  public static final NodeId NonExclusiveLimitAlarmType_EnabledState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(9925));

  public static final NodeId NonExclusiveLimitAlarmType_EnabledState_TrueState =
      new NodeId(UShort.MIN, uint(9926));

  public static final NodeId NonExclusiveLimitAlarmType_EnabledState_FalseState =
      new NodeId(UShort.MIN, uint(9927));

  public static final NodeId NonExclusiveLimitAlarmType_Quality =
      new NodeId(UShort.MIN, uint(9928));

  public static final NodeId NonExclusiveLimitAlarmType_Quality_SourceTimestamp =
      new NodeId(UShort.MIN, uint(9929));

  public static final NodeId NonExclusiveLimitAlarmType_LastSeverity =
      new NodeId(UShort.MIN, uint(9930));

  public static final NodeId NonExclusiveLimitAlarmType_LastSeverity_SourceTimestamp =
      new NodeId(UShort.MIN, uint(9931));

  public static final NodeId NonExclusiveLimitAlarmType_Comment =
      new NodeId(UShort.MIN, uint(9932));

  public static final NodeId NonExclusiveLimitAlarmType_Comment_SourceTimestamp =
      new NodeId(UShort.MIN, uint(9933));

  public static final NodeId NonExclusiveLimitAlarmType_ClientUserId =
      new NodeId(UShort.MIN, uint(9934));

  public static final NodeId NonExclusiveLimitAlarmType_Enable = new NodeId(UShort.MIN, uint(9935));

  public static final NodeId NonExclusiveLimitAlarmType_Disable =
      new NodeId(UShort.MIN, uint(9936));

  public static final NodeId NonExclusiveLimitAlarmType_AddComment =
      new NodeId(UShort.MIN, uint(9937));

  public static final NodeId NonExclusiveLimitAlarmType_AddComment_InputArguments =
      new NodeId(UShort.MIN, uint(9938));

  public static final NodeId NonExclusiveLimitAlarmType_ConditionRefresh =
      new NodeId(UShort.MIN, uint(9939));

  public static final NodeId NonExclusiveLimitAlarmType_ConditionRefresh_InputArguments =
      new NodeId(UShort.MIN, uint(9940));

  public static final NodeId NonExclusiveLimitAlarmType_AckedState =
      new NodeId(UShort.MIN, uint(9941));

  public static final NodeId NonExclusiveLimitAlarmType_AckedState_Id =
      new NodeId(UShort.MIN, uint(9942));

  public static final NodeId NonExclusiveLimitAlarmType_AckedState_Name =
      new NodeId(UShort.MIN, uint(9943));

  public static final NodeId NonExclusiveLimitAlarmType_AckedState_Number =
      new NodeId(UShort.MIN, uint(9944));

  public static final NodeId NonExclusiveLimitAlarmType_AckedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(9945));

  public static final NodeId NonExclusiveLimitAlarmType_AckedState_TransitionTime =
      new NodeId(UShort.MIN, uint(9946));

  public static final NodeId NonExclusiveLimitAlarmType_AckedState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(9947));

  public static final NodeId NonExclusiveLimitAlarmType_AckedState_TrueState =
      new NodeId(UShort.MIN, uint(9948));

  public static final NodeId NonExclusiveLimitAlarmType_AckedState_FalseState =
      new NodeId(UShort.MIN, uint(9949));

  public static final NodeId NonExclusiveLimitAlarmType_ConfirmedState =
      new NodeId(UShort.MIN, uint(9950));

  public static final NodeId NonExclusiveLimitAlarmType_ConfirmedState_Id =
      new NodeId(UShort.MIN, uint(9951));

  public static final NodeId NonExclusiveLimitAlarmType_ConfirmedState_Name =
      new NodeId(UShort.MIN, uint(9952));

  public static final NodeId NonExclusiveLimitAlarmType_ConfirmedState_Number =
      new NodeId(UShort.MIN, uint(9953));

  public static final NodeId NonExclusiveLimitAlarmType_ConfirmedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(9954));

  public static final NodeId NonExclusiveLimitAlarmType_ConfirmedState_TransitionTime =
      new NodeId(UShort.MIN, uint(9955));

  public static final NodeId NonExclusiveLimitAlarmType_ConfirmedState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(9956));

  public static final NodeId NonExclusiveLimitAlarmType_ConfirmedState_TrueState =
      new NodeId(UShort.MIN, uint(9957));

  public static final NodeId NonExclusiveLimitAlarmType_ConfirmedState_FalseState =
      new NodeId(UShort.MIN, uint(9958));

  public static final NodeId NonExclusiveLimitAlarmType_Acknowledge =
      new NodeId(UShort.MIN, uint(9959));

  public static final NodeId NonExclusiveLimitAlarmType_Acknowledge_InputArguments =
      new NodeId(UShort.MIN, uint(9960));

  public static final NodeId NonExclusiveLimitAlarmType_Confirm =
      new NodeId(UShort.MIN, uint(9961));

  public static final NodeId NonExclusiveLimitAlarmType_Confirm_InputArguments =
      new NodeId(UShort.MIN, uint(9962));

  public static final NodeId NonExclusiveLimitAlarmType_ActiveState =
      new NodeId(UShort.MIN, uint(9963));

  public static final NodeId NonExclusiveLimitAlarmType_ActiveState_Id =
      new NodeId(UShort.MIN, uint(9964));

  public static final NodeId NonExclusiveLimitAlarmType_ActiveState_Name =
      new NodeId(UShort.MIN, uint(9965));

  public static final NodeId NonExclusiveLimitAlarmType_ActiveState_Number =
      new NodeId(UShort.MIN, uint(9966));

  public static final NodeId NonExclusiveLimitAlarmType_ActiveState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(9967));

  public static final NodeId NonExclusiveLimitAlarmType_ActiveState_TransitionTime =
      new NodeId(UShort.MIN, uint(9968));

  public static final NodeId NonExclusiveLimitAlarmType_ActiveState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(9969));

  public static final NodeId NonExclusiveLimitAlarmType_ActiveState_TrueState =
      new NodeId(UShort.MIN, uint(9970));

  public static final NodeId NonExclusiveLimitAlarmType_ActiveState_FalseState =
      new NodeId(UShort.MIN, uint(9971));

  public static final NodeId NonExclusiveLimitAlarmType_SuppressedState =
      new NodeId(UShort.MIN, uint(9972));

  public static final NodeId NonExclusiveLimitAlarmType_SuppressedState_Id =
      new NodeId(UShort.MIN, uint(9973));

  public static final NodeId NonExclusiveLimitAlarmType_SuppressedState_Name =
      new NodeId(UShort.MIN, uint(9974));

  public static final NodeId NonExclusiveLimitAlarmType_SuppressedState_Number =
      new NodeId(UShort.MIN, uint(9975));

  public static final NodeId NonExclusiveLimitAlarmType_SuppressedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(9976));

  public static final NodeId NonExclusiveLimitAlarmType_SuppressedState_TransitionTime =
      new NodeId(UShort.MIN, uint(9977));

  public static final NodeId NonExclusiveLimitAlarmType_SuppressedState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(9978));

  public static final NodeId NonExclusiveLimitAlarmType_SuppressedState_TrueState =
      new NodeId(UShort.MIN, uint(9979));

  public static final NodeId NonExclusiveLimitAlarmType_SuppressedState_FalseState =
      new NodeId(UShort.MIN, uint(9980));

  public static final NodeId NonExclusiveLimitAlarmType_ShelvingState =
      new NodeId(UShort.MIN, uint(9981));

  public static final NodeId NonExclusiveLimitAlarmType_ShelvingState_CurrentState =
      new NodeId(UShort.MIN, uint(9982));

  public static final NodeId NonExclusiveLimitAlarmType_ShelvingState_CurrentState_Id =
      new NodeId(UShort.MIN, uint(9983));

  public static final NodeId NonExclusiveLimitAlarmType_ShelvingState_CurrentState_Name =
      new NodeId(UShort.MIN, uint(9984));

  public static final NodeId NonExclusiveLimitAlarmType_ShelvingState_CurrentState_Number =
      new NodeId(UShort.MIN, uint(9985));

  public static final NodeId
      NonExclusiveLimitAlarmType_ShelvingState_CurrentState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(9986));

  public static final NodeId NonExclusiveLimitAlarmType_ShelvingState_LastTransition =
      new NodeId(UShort.MIN, uint(9987));

  public static final NodeId NonExclusiveLimitAlarmType_ShelvingState_LastTransition_Id =
      new NodeId(UShort.MIN, uint(9988));

  public static final NodeId NonExclusiveLimitAlarmType_ShelvingState_LastTransition_Name =
      new NodeId(UShort.MIN, uint(9989));

  public static final NodeId NonExclusiveLimitAlarmType_ShelvingState_LastTransition_Number =
      new NodeId(UShort.MIN, uint(9990));

  public static final NodeId
      NonExclusiveLimitAlarmType_ShelvingState_LastTransition_TransitionTime =
          new NodeId(UShort.MIN, uint(9991));

  public static final NodeId NonExclusiveLimitAlarmType_ShelvingState_UnshelveTime =
      new NodeId(UShort.MIN, uint(9992));

  public static final NodeId NonExclusiveLimitAlarmType_ShelvingState_Unshelve =
      new NodeId(UShort.MIN, uint(10014));

  public static final NodeId NonExclusiveLimitAlarmType_ShelvingState_OneShotShelve =
      new NodeId(UShort.MIN, uint(10015));

  public static final NodeId NonExclusiveLimitAlarmType_ShelvingState_TimedShelve =
      new NodeId(UShort.MIN, uint(10016));

  public static final NodeId NonExclusiveLimitAlarmType_ShelvingState_TimedShelve_InputArguments =
      new NodeId(UShort.MIN, uint(10017));

  public static final NodeId NonExclusiveLimitAlarmType_SuppressedOrShelved =
      new NodeId(UShort.MIN, uint(10018));

  public static final NodeId NonExclusiveLimitAlarmType_MaxTimeShelved =
      new NodeId(UShort.MIN, uint(10019));

  public static final NodeId NonExclusiveLimitAlarmType_HighHighState =
      new NodeId(UShort.MIN, uint(10020));

  public static final NodeId NonExclusiveLimitAlarmType_HighHighState_Id =
      new NodeId(UShort.MIN, uint(10021));

  public static final NodeId NonExclusiveLimitAlarmType_HighHighState_Name =
      new NodeId(UShort.MIN, uint(10022));

  public static final NodeId NonExclusiveLimitAlarmType_HighHighState_Number =
      new NodeId(UShort.MIN, uint(10023));

  public static final NodeId NonExclusiveLimitAlarmType_HighHighState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(10024));

  public static final NodeId NonExclusiveLimitAlarmType_HighHighState_TransitionTime =
      new NodeId(UShort.MIN, uint(10025));

  public static final NodeId NonExclusiveLimitAlarmType_HighHighState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(10026));

  public static final NodeId NonExclusiveLimitAlarmType_HighHighState_TrueState =
      new NodeId(UShort.MIN, uint(10027));

  public static final NodeId NonExclusiveLimitAlarmType_HighHighState_FalseState =
      new NodeId(UShort.MIN, uint(10028));

  public static final NodeId NonExclusiveLimitAlarmType_HighState =
      new NodeId(UShort.MIN, uint(10029));

  public static final NodeId NonExclusiveLimitAlarmType_HighState_Id =
      new NodeId(UShort.MIN, uint(10030));

  public static final NodeId NonExclusiveLimitAlarmType_HighState_Name =
      new NodeId(UShort.MIN, uint(10031));

  public static final NodeId NonExclusiveLimitAlarmType_HighState_Number =
      new NodeId(UShort.MIN, uint(10032));

  public static final NodeId NonExclusiveLimitAlarmType_HighState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(10033));

  public static final NodeId NonExclusiveLimitAlarmType_HighState_TransitionTime =
      new NodeId(UShort.MIN, uint(10034));

  public static final NodeId NonExclusiveLimitAlarmType_HighState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(10035));

  public static final NodeId NonExclusiveLimitAlarmType_HighState_TrueState =
      new NodeId(UShort.MIN, uint(10036));

  public static final NodeId NonExclusiveLimitAlarmType_HighState_FalseState =
      new NodeId(UShort.MIN, uint(10037));

  public static final NodeId NonExclusiveLimitAlarmType_LowState =
      new NodeId(UShort.MIN, uint(10038));

  public static final NodeId NonExclusiveLimitAlarmType_LowState_Id =
      new NodeId(UShort.MIN, uint(10039));

  public static final NodeId NonExclusiveLimitAlarmType_LowState_Name =
      new NodeId(UShort.MIN, uint(10040));

  public static final NodeId NonExclusiveLimitAlarmType_LowState_Number =
      new NodeId(UShort.MIN, uint(10041));

  public static final NodeId NonExclusiveLimitAlarmType_LowState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(10042));

  public static final NodeId NonExclusiveLimitAlarmType_LowState_TransitionTime =
      new NodeId(UShort.MIN, uint(10043));

  public static final NodeId NonExclusiveLimitAlarmType_LowState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(10044));

  public static final NodeId NonExclusiveLimitAlarmType_LowState_TrueState =
      new NodeId(UShort.MIN, uint(10045));

  public static final NodeId NonExclusiveLimitAlarmType_LowState_FalseState =
      new NodeId(UShort.MIN, uint(10046));

  public static final NodeId NonExclusiveLimitAlarmType_LowLowState =
      new NodeId(UShort.MIN, uint(10047));

  public static final NodeId NonExclusiveLimitAlarmType_LowLowState_Id =
      new NodeId(UShort.MIN, uint(10048));

  public static final NodeId NonExclusiveLimitAlarmType_LowLowState_Name =
      new NodeId(UShort.MIN, uint(10049));

  public static final NodeId NonExclusiveLimitAlarmType_LowLowState_Number =
      new NodeId(UShort.MIN, uint(10050));

  public static final NodeId NonExclusiveLimitAlarmType_LowLowState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(10051));

  public static final NodeId NonExclusiveLimitAlarmType_LowLowState_TransitionTime =
      new NodeId(UShort.MIN, uint(10052));

  public static final NodeId NonExclusiveLimitAlarmType_LowLowState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(10053));

  public static final NodeId NonExclusiveLimitAlarmType_LowLowState_TrueState =
      new NodeId(UShort.MIN, uint(10054));

  public static final NodeId NonExclusiveLimitAlarmType_LowLowState_FalseState =
      new NodeId(UShort.MIN, uint(10055));

  public static final NodeId NonExclusiveLimitAlarmType_HighHighLimit =
      new NodeId(UShort.MIN, uint(10056));

  public static final NodeId NonExclusiveLimitAlarmType_HighLimit =
      new NodeId(UShort.MIN, uint(10057));

  public static final NodeId NonExclusiveLimitAlarmType_LowLimit =
      new NodeId(UShort.MIN, uint(10058));

  public static final NodeId NonExclusiveLimitAlarmType_LowLowLimit =
      new NodeId(UShort.MIN, uint(10059));

  public static final NodeId NonExclusiveLevelAlarmType = new NodeId(UShort.MIN, uint(10060));

  public static final NodeId NonExclusiveLevelAlarmType_EventId =
      new NodeId(UShort.MIN, uint(10061));

  public static final NodeId NonExclusiveLevelAlarmType_EventType =
      new NodeId(UShort.MIN, uint(10062));

  public static final NodeId NonExclusiveLevelAlarmType_SourceNode =
      new NodeId(UShort.MIN, uint(10063));

  public static final NodeId NonExclusiveLevelAlarmType_SourceName =
      new NodeId(UShort.MIN, uint(10064));

  public static final NodeId NonExclusiveLevelAlarmType_Time = new NodeId(UShort.MIN, uint(10065));

  public static final NodeId NonExclusiveLevelAlarmType_ReceiveTime =
      new NodeId(UShort.MIN, uint(10066));

  public static final NodeId NonExclusiveLevelAlarmType_LocalTime =
      new NodeId(UShort.MIN, uint(10067));

  public static final NodeId NonExclusiveLevelAlarmType_Message =
      new NodeId(UShort.MIN, uint(10068));

  public static final NodeId NonExclusiveLevelAlarmType_Severity =
      new NodeId(UShort.MIN, uint(10069));

  public static final NodeId NonExclusiveLevelAlarmType_ConditionName =
      new NodeId(UShort.MIN, uint(10070));

  public static final NodeId NonExclusiveLevelAlarmType_BranchId =
      new NodeId(UShort.MIN, uint(10071));

  public static final NodeId NonExclusiveLevelAlarmType_Retain =
      new NodeId(UShort.MIN, uint(10072));

  public static final NodeId NonExclusiveLevelAlarmType_EnabledState =
      new NodeId(UShort.MIN, uint(10073));

  public static final NodeId NonExclusiveLevelAlarmType_EnabledState_Id =
      new NodeId(UShort.MIN, uint(10074));

  public static final NodeId NonExclusiveLevelAlarmType_EnabledState_Name =
      new NodeId(UShort.MIN, uint(10075));

  public static final NodeId NonExclusiveLevelAlarmType_EnabledState_Number =
      new NodeId(UShort.MIN, uint(10076));

  public static final NodeId NonExclusiveLevelAlarmType_EnabledState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(10077));

  public static final NodeId NonExclusiveLevelAlarmType_EnabledState_TransitionTime =
      new NodeId(UShort.MIN, uint(10078));

  public static final NodeId NonExclusiveLevelAlarmType_EnabledState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(10079));

  public static final NodeId NonExclusiveLevelAlarmType_EnabledState_TrueState =
      new NodeId(UShort.MIN, uint(10080));

  public static final NodeId NonExclusiveLevelAlarmType_EnabledState_FalseState =
      new NodeId(UShort.MIN, uint(10081));

  public static final NodeId NonExclusiveLevelAlarmType_Quality =
      new NodeId(UShort.MIN, uint(10082));

  public static final NodeId NonExclusiveLevelAlarmType_Quality_SourceTimestamp =
      new NodeId(UShort.MIN, uint(10083));

  public static final NodeId NonExclusiveLevelAlarmType_LastSeverity =
      new NodeId(UShort.MIN, uint(10084));

  public static final NodeId NonExclusiveLevelAlarmType_LastSeverity_SourceTimestamp =
      new NodeId(UShort.MIN, uint(10085));

  public static final NodeId NonExclusiveLevelAlarmType_Comment =
      new NodeId(UShort.MIN, uint(10086));

  public static final NodeId NonExclusiveLevelAlarmType_Comment_SourceTimestamp =
      new NodeId(UShort.MIN, uint(10087));

  public static final NodeId NonExclusiveLevelAlarmType_ClientUserId =
      new NodeId(UShort.MIN, uint(10088));

  public static final NodeId NonExclusiveLevelAlarmType_Enable =
      new NodeId(UShort.MIN, uint(10089));

  public static final NodeId NonExclusiveLevelAlarmType_Disable =
      new NodeId(UShort.MIN, uint(10090));

  public static final NodeId NonExclusiveLevelAlarmType_AddComment =
      new NodeId(UShort.MIN, uint(10091));

  public static final NodeId NonExclusiveLevelAlarmType_AddComment_InputArguments =
      new NodeId(UShort.MIN, uint(10092));

  public static final NodeId NonExclusiveLevelAlarmType_ConditionRefresh =
      new NodeId(UShort.MIN, uint(10093));

  public static final NodeId NonExclusiveLevelAlarmType_ConditionRefresh_InputArguments =
      new NodeId(UShort.MIN, uint(10094));

  public static final NodeId NonExclusiveLevelAlarmType_AckedState =
      new NodeId(UShort.MIN, uint(10095));

  public static final NodeId NonExclusiveLevelAlarmType_AckedState_Id =
      new NodeId(UShort.MIN, uint(10096));

  public static final NodeId NonExclusiveLevelAlarmType_AckedState_Name =
      new NodeId(UShort.MIN, uint(10097));

  public static final NodeId NonExclusiveLevelAlarmType_AckedState_Number =
      new NodeId(UShort.MIN, uint(10098));

  public static final NodeId NonExclusiveLevelAlarmType_AckedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(10099));

  public static final NodeId NonExclusiveLevelAlarmType_AckedState_TransitionTime =
      new NodeId(UShort.MIN, uint(10100));

  public static final NodeId NonExclusiveLevelAlarmType_AckedState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(10101));

  public static final NodeId NonExclusiveLevelAlarmType_AckedState_TrueState =
      new NodeId(UShort.MIN, uint(10102));

  public static final NodeId NonExclusiveLevelAlarmType_AckedState_FalseState =
      new NodeId(UShort.MIN, uint(10103));

  public static final NodeId NonExclusiveLevelAlarmType_ConfirmedState =
      new NodeId(UShort.MIN, uint(10104));

  public static final NodeId NonExclusiveLevelAlarmType_ConfirmedState_Id =
      new NodeId(UShort.MIN, uint(10105));

  public static final NodeId NonExclusiveLevelAlarmType_ConfirmedState_Name =
      new NodeId(UShort.MIN, uint(10106));

  public static final NodeId NonExclusiveLevelAlarmType_ConfirmedState_Number =
      new NodeId(UShort.MIN, uint(10107));

  public static final NodeId NonExclusiveLevelAlarmType_ConfirmedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(10108));

  public static final NodeId NonExclusiveLevelAlarmType_ConfirmedState_TransitionTime =
      new NodeId(UShort.MIN, uint(10109));

  public static final NodeId NonExclusiveLevelAlarmType_ConfirmedState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(10110));

  public static final NodeId NonExclusiveLevelAlarmType_ConfirmedState_TrueState =
      new NodeId(UShort.MIN, uint(10111));

  public static final NodeId NonExclusiveLevelAlarmType_ConfirmedState_FalseState =
      new NodeId(UShort.MIN, uint(10112));

  public static final NodeId NonExclusiveLevelAlarmType_Acknowledge =
      new NodeId(UShort.MIN, uint(10113));

  public static final NodeId NonExclusiveLevelAlarmType_Acknowledge_InputArguments =
      new NodeId(UShort.MIN, uint(10114));

  public static final NodeId NonExclusiveLevelAlarmType_Confirm =
      new NodeId(UShort.MIN, uint(10115));

  public static final NodeId NonExclusiveLevelAlarmType_Confirm_InputArguments =
      new NodeId(UShort.MIN, uint(10116));

  public static final NodeId NonExclusiveLevelAlarmType_ActiveState =
      new NodeId(UShort.MIN, uint(10117));

  public static final NodeId NonExclusiveLevelAlarmType_ActiveState_Id =
      new NodeId(UShort.MIN, uint(10118));

  public static final NodeId NonExclusiveLevelAlarmType_ActiveState_Name =
      new NodeId(UShort.MIN, uint(10119));

  public static final NodeId NonExclusiveLevelAlarmType_ActiveState_Number =
      new NodeId(UShort.MIN, uint(10120));

  public static final NodeId NonExclusiveLevelAlarmType_ActiveState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(10121));

  public static final NodeId NonExclusiveLevelAlarmType_ActiveState_TransitionTime =
      new NodeId(UShort.MIN, uint(10122));

  public static final NodeId NonExclusiveLevelAlarmType_ActiveState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(10123));

  public static final NodeId NonExclusiveLevelAlarmType_ActiveState_TrueState =
      new NodeId(UShort.MIN, uint(10124));

  public static final NodeId NonExclusiveLevelAlarmType_ActiveState_FalseState =
      new NodeId(UShort.MIN, uint(10125));

  public static final NodeId NonExclusiveLevelAlarmType_SuppressedState =
      new NodeId(UShort.MIN, uint(10126));

  public static final NodeId NonExclusiveLevelAlarmType_SuppressedState_Id =
      new NodeId(UShort.MIN, uint(10127));

  public static final NodeId NonExclusiveLevelAlarmType_SuppressedState_Name =
      new NodeId(UShort.MIN, uint(10128));

  public static final NodeId NonExclusiveLevelAlarmType_SuppressedState_Number =
      new NodeId(UShort.MIN, uint(10129));

  public static final NodeId NonExclusiveLevelAlarmType_SuppressedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(10130));

  public static final NodeId NonExclusiveLevelAlarmType_SuppressedState_TransitionTime =
      new NodeId(UShort.MIN, uint(10131));

  public static final NodeId NonExclusiveLevelAlarmType_SuppressedState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(10132));

  public static final NodeId NonExclusiveLevelAlarmType_SuppressedState_TrueState =
      new NodeId(UShort.MIN, uint(10133));

  public static final NodeId NonExclusiveLevelAlarmType_SuppressedState_FalseState =
      new NodeId(UShort.MIN, uint(10134));

  public static final NodeId NonExclusiveLevelAlarmType_ShelvingState =
      new NodeId(UShort.MIN, uint(10135));

  public static final NodeId NonExclusiveLevelAlarmType_ShelvingState_CurrentState =
      new NodeId(UShort.MIN, uint(10136));

  public static final NodeId NonExclusiveLevelAlarmType_ShelvingState_CurrentState_Id =
      new NodeId(UShort.MIN, uint(10137));

  public static final NodeId NonExclusiveLevelAlarmType_ShelvingState_CurrentState_Name =
      new NodeId(UShort.MIN, uint(10138));

  public static final NodeId NonExclusiveLevelAlarmType_ShelvingState_CurrentState_Number =
      new NodeId(UShort.MIN, uint(10139));

  public static final NodeId
      NonExclusiveLevelAlarmType_ShelvingState_CurrentState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(10140));

  public static final NodeId NonExclusiveLevelAlarmType_ShelvingState_LastTransition =
      new NodeId(UShort.MIN, uint(10141));

  public static final NodeId NonExclusiveLevelAlarmType_ShelvingState_LastTransition_Id =
      new NodeId(UShort.MIN, uint(10142));

  public static final NodeId NonExclusiveLevelAlarmType_ShelvingState_LastTransition_Name =
      new NodeId(UShort.MIN, uint(10143));

  public static final NodeId NonExclusiveLevelAlarmType_ShelvingState_LastTransition_Number =
      new NodeId(UShort.MIN, uint(10144));

  public static final NodeId
      NonExclusiveLevelAlarmType_ShelvingState_LastTransition_TransitionTime =
          new NodeId(UShort.MIN, uint(10145));

  public static final NodeId NonExclusiveLevelAlarmType_ShelvingState_UnshelveTime =
      new NodeId(UShort.MIN, uint(10146));

  public static final NodeId NonExclusiveLevelAlarmType_ShelvingState_Unshelve =
      new NodeId(UShort.MIN, uint(10168));

  public static final NodeId NonExclusiveLevelAlarmType_ShelvingState_OneShotShelve =
      new NodeId(UShort.MIN, uint(10169));

  public static final NodeId NonExclusiveLevelAlarmType_ShelvingState_TimedShelve =
      new NodeId(UShort.MIN, uint(10170));

  public static final NodeId NonExclusiveLevelAlarmType_ShelvingState_TimedShelve_InputArguments =
      new NodeId(UShort.MIN, uint(10171));

  public static final NodeId NonExclusiveLevelAlarmType_SuppressedOrShelved =
      new NodeId(UShort.MIN, uint(10172));

  public static final NodeId NonExclusiveLevelAlarmType_MaxTimeShelved =
      new NodeId(UShort.MIN, uint(10173));

  public static final NodeId NonExclusiveLevelAlarmType_HighHighState =
      new NodeId(UShort.MIN, uint(10174));

  public static final NodeId NonExclusiveLevelAlarmType_HighHighState_Id =
      new NodeId(UShort.MIN, uint(10175));

  public static final NodeId NonExclusiveLevelAlarmType_HighHighState_Name =
      new NodeId(UShort.MIN, uint(10176));

  public static final NodeId NonExclusiveLevelAlarmType_HighHighState_Number =
      new NodeId(UShort.MIN, uint(10177));

  public static final NodeId NonExclusiveLevelAlarmType_HighHighState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(10178));

  public static final NodeId NonExclusiveLevelAlarmType_HighHighState_TransitionTime =
      new NodeId(UShort.MIN, uint(10179));

  public static final NodeId NonExclusiveLevelAlarmType_HighHighState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(10180));

  public static final NodeId NonExclusiveLevelAlarmType_HighHighState_TrueState =
      new NodeId(UShort.MIN, uint(10181));

  public static final NodeId NonExclusiveLevelAlarmType_HighHighState_FalseState =
      new NodeId(UShort.MIN, uint(10182));

  public static final NodeId NonExclusiveLevelAlarmType_HighState =
      new NodeId(UShort.MIN, uint(10183));

  public static final NodeId NonExclusiveLevelAlarmType_HighState_Id =
      new NodeId(UShort.MIN, uint(10184));

  public static final NodeId NonExclusiveLevelAlarmType_HighState_Name =
      new NodeId(UShort.MIN, uint(10185));

  public static final NodeId NonExclusiveLevelAlarmType_HighState_Number =
      new NodeId(UShort.MIN, uint(10186));

  public static final NodeId NonExclusiveLevelAlarmType_HighState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(10187));

  public static final NodeId NonExclusiveLevelAlarmType_HighState_TransitionTime =
      new NodeId(UShort.MIN, uint(10188));

  public static final NodeId NonExclusiveLevelAlarmType_HighState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(10189));

  public static final NodeId NonExclusiveLevelAlarmType_HighState_TrueState =
      new NodeId(UShort.MIN, uint(10190));

  public static final NodeId NonExclusiveLevelAlarmType_HighState_FalseState =
      new NodeId(UShort.MIN, uint(10191));

  public static final NodeId NonExclusiveLevelAlarmType_LowState =
      new NodeId(UShort.MIN, uint(10192));

  public static final NodeId NonExclusiveLevelAlarmType_LowState_Id =
      new NodeId(UShort.MIN, uint(10193));

  public static final NodeId NonExclusiveLevelAlarmType_LowState_Name =
      new NodeId(UShort.MIN, uint(10194));

  public static final NodeId NonExclusiveLevelAlarmType_LowState_Number =
      new NodeId(UShort.MIN, uint(10195));

  public static final NodeId NonExclusiveLevelAlarmType_LowState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(10196));

  public static final NodeId NonExclusiveLevelAlarmType_LowState_TransitionTime =
      new NodeId(UShort.MIN, uint(10197));

  public static final NodeId NonExclusiveLevelAlarmType_LowState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(10198));

  public static final NodeId NonExclusiveLevelAlarmType_LowState_TrueState =
      new NodeId(UShort.MIN, uint(10199));

  public static final NodeId NonExclusiveLevelAlarmType_LowState_FalseState =
      new NodeId(UShort.MIN, uint(10200));

  public static final NodeId NonExclusiveLevelAlarmType_LowLowState =
      new NodeId(UShort.MIN, uint(10201));

  public static final NodeId NonExclusiveLevelAlarmType_LowLowState_Id =
      new NodeId(UShort.MIN, uint(10202));

  public static final NodeId NonExclusiveLevelAlarmType_LowLowState_Name =
      new NodeId(UShort.MIN, uint(10203));

  public static final NodeId NonExclusiveLevelAlarmType_LowLowState_Number =
      new NodeId(UShort.MIN, uint(10204));

  public static final NodeId NonExclusiveLevelAlarmType_LowLowState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(10205));

  public static final NodeId NonExclusiveLevelAlarmType_LowLowState_TransitionTime =
      new NodeId(UShort.MIN, uint(10206));

  public static final NodeId NonExclusiveLevelAlarmType_LowLowState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(10207));

  public static final NodeId NonExclusiveLevelAlarmType_LowLowState_TrueState =
      new NodeId(UShort.MIN, uint(10208));

  public static final NodeId NonExclusiveLevelAlarmType_LowLowState_FalseState =
      new NodeId(UShort.MIN, uint(10209));

  public static final NodeId NonExclusiveLevelAlarmType_HighHighLimit =
      new NodeId(UShort.MIN, uint(10210));

  public static final NodeId NonExclusiveLevelAlarmType_HighLimit =
      new NodeId(UShort.MIN, uint(10211));

  public static final NodeId NonExclusiveLevelAlarmType_LowLimit =
      new NodeId(UShort.MIN, uint(10212));

  public static final NodeId NonExclusiveLevelAlarmType_LowLowLimit =
      new NodeId(UShort.MIN, uint(10213));

  public static final NodeId NonExclusiveRateOfChangeAlarmType =
      new NodeId(UShort.MIN, uint(10214));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_EventId =
      new NodeId(UShort.MIN, uint(10215));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_EventType =
      new NodeId(UShort.MIN, uint(10216));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_SourceNode =
      new NodeId(UShort.MIN, uint(10217));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_SourceName =
      new NodeId(UShort.MIN, uint(10218));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_Time =
      new NodeId(UShort.MIN, uint(10219));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_ReceiveTime =
      new NodeId(UShort.MIN, uint(10220));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_LocalTime =
      new NodeId(UShort.MIN, uint(10221));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_Message =
      new NodeId(UShort.MIN, uint(10222));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_Severity =
      new NodeId(UShort.MIN, uint(10223));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_ConditionName =
      new NodeId(UShort.MIN, uint(10224));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_BranchId =
      new NodeId(UShort.MIN, uint(10225));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_Retain =
      new NodeId(UShort.MIN, uint(10226));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_EnabledState =
      new NodeId(UShort.MIN, uint(10227));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_EnabledState_Id =
      new NodeId(UShort.MIN, uint(10228));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_EnabledState_Name =
      new NodeId(UShort.MIN, uint(10229));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_EnabledState_Number =
      new NodeId(UShort.MIN, uint(10230));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_EnabledState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(10231));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_EnabledState_TransitionTime =
      new NodeId(UShort.MIN, uint(10232));

  public static final NodeId
      NonExclusiveRateOfChangeAlarmType_EnabledState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(10233));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_EnabledState_TrueState =
      new NodeId(UShort.MIN, uint(10234));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_EnabledState_FalseState =
      new NodeId(UShort.MIN, uint(10235));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_Quality =
      new NodeId(UShort.MIN, uint(10236));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_Quality_SourceTimestamp =
      new NodeId(UShort.MIN, uint(10237));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_LastSeverity =
      new NodeId(UShort.MIN, uint(10238));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_LastSeverity_SourceTimestamp =
      new NodeId(UShort.MIN, uint(10239));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_Comment =
      new NodeId(UShort.MIN, uint(10240));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_Comment_SourceTimestamp =
      new NodeId(UShort.MIN, uint(10241));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_ClientUserId =
      new NodeId(UShort.MIN, uint(10242));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_Enable =
      new NodeId(UShort.MIN, uint(10243));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_Disable =
      new NodeId(UShort.MIN, uint(10244));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_AddComment =
      new NodeId(UShort.MIN, uint(10245));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_AddComment_InputArguments =
      new NodeId(UShort.MIN, uint(10246));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_ConditionRefresh =
      new NodeId(UShort.MIN, uint(10247));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_ConditionRefresh_InputArguments =
      new NodeId(UShort.MIN, uint(10248));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_AckedState =
      new NodeId(UShort.MIN, uint(10249));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_AckedState_Id =
      new NodeId(UShort.MIN, uint(10250));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_AckedState_Name =
      new NodeId(UShort.MIN, uint(10251));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_AckedState_Number =
      new NodeId(UShort.MIN, uint(10252));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_AckedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(10253));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_AckedState_TransitionTime =
      new NodeId(UShort.MIN, uint(10254));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_AckedState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(10255));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_AckedState_TrueState =
      new NodeId(UShort.MIN, uint(10256));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_AckedState_FalseState =
      new NodeId(UShort.MIN, uint(10257));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_ConfirmedState =
      new NodeId(UShort.MIN, uint(10258));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_ConfirmedState_Id =
      new NodeId(UShort.MIN, uint(10259));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_ConfirmedState_Name =
      new NodeId(UShort.MIN, uint(10260));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_ConfirmedState_Number =
      new NodeId(UShort.MIN, uint(10261));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_ConfirmedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(10262));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_ConfirmedState_TransitionTime =
      new NodeId(UShort.MIN, uint(10263));

  public static final NodeId
      NonExclusiveRateOfChangeAlarmType_ConfirmedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(10264));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_ConfirmedState_TrueState =
      new NodeId(UShort.MIN, uint(10265));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_ConfirmedState_FalseState =
      new NodeId(UShort.MIN, uint(10266));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_Acknowledge =
      new NodeId(UShort.MIN, uint(10267));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_Acknowledge_InputArguments =
      new NodeId(UShort.MIN, uint(10268));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_Confirm =
      new NodeId(UShort.MIN, uint(10269));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_Confirm_InputArguments =
      new NodeId(UShort.MIN, uint(10270));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_ActiveState =
      new NodeId(UShort.MIN, uint(10271));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_ActiveState_Id =
      new NodeId(UShort.MIN, uint(10272));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_ActiveState_Name =
      new NodeId(UShort.MIN, uint(10273));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_ActiveState_Number =
      new NodeId(UShort.MIN, uint(10274));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_ActiveState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(10275));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_ActiveState_TransitionTime =
      new NodeId(UShort.MIN, uint(10276));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_ActiveState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(10277));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_ActiveState_TrueState =
      new NodeId(UShort.MIN, uint(10278));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_ActiveState_FalseState =
      new NodeId(UShort.MIN, uint(10279));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_SuppressedState =
      new NodeId(UShort.MIN, uint(10280));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_SuppressedState_Id =
      new NodeId(UShort.MIN, uint(10281));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_SuppressedState_Name =
      new NodeId(UShort.MIN, uint(10282));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_SuppressedState_Number =
      new NodeId(UShort.MIN, uint(10283));

  public static final NodeId
      NonExclusiveRateOfChangeAlarmType_SuppressedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(10284));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_SuppressedState_TransitionTime =
      new NodeId(UShort.MIN, uint(10285));

  public static final NodeId
      NonExclusiveRateOfChangeAlarmType_SuppressedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(10286));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_SuppressedState_TrueState =
      new NodeId(UShort.MIN, uint(10287));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_SuppressedState_FalseState =
      new NodeId(UShort.MIN, uint(10288));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_ShelvingState =
      new NodeId(UShort.MIN, uint(10289));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_ShelvingState_CurrentState =
      new NodeId(UShort.MIN, uint(10290));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_ShelvingState_CurrentState_Id =
      new NodeId(UShort.MIN, uint(10291));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_ShelvingState_CurrentState_Name =
      new NodeId(UShort.MIN, uint(10292));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_ShelvingState_CurrentState_Number =
      new NodeId(UShort.MIN, uint(10293));

  public static final NodeId
      NonExclusiveRateOfChangeAlarmType_ShelvingState_CurrentState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(10294));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_ShelvingState_LastTransition =
      new NodeId(UShort.MIN, uint(10295));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_ShelvingState_LastTransition_Id =
      new NodeId(UShort.MIN, uint(10296));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_ShelvingState_LastTransition_Name =
      new NodeId(UShort.MIN, uint(10297));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_ShelvingState_LastTransition_Number =
      new NodeId(UShort.MIN, uint(10298));

  public static final NodeId
      NonExclusiveRateOfChangeAlarmType_ShelvingState_LastTransition_TransitionTime =
          new NodeId(UShort.MIN, uint(10299));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_ShelvingState_UnshelveTime =
      new NodeId(UShort.MIN, uint(10300));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_ShelvingState_Unshelve =
      new NodeId(UShort.MIN, uint(10322));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_ShelvingState_OneShotShelve =
      new NodeId(UShort.MIN, uint(10323));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_ShelvingState_TimedShelve =
      new NodeId(UShort.MIN, uint(10324));

  public static final NodeId
      NonExclusiveRateOfChangeAlarmType_ShelvingState_TimedShelve_InputArguments =
          new NodeId(UShort.MIN, uint(10325));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_SuppressedOrShelved =
      new NodeId(UShort.MIN, uint(10326));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_MaxTimeShelved =
      new NodeId(UShort.MIN, uint(10327));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_HighHighState =
      new NodeId(UShort.MIN, uint(10328));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_HighHighState_Id =
      new NodeId(UShort.MIN, uint(10329));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_HighHighState_Name =
      new NodeId(UShort.MIN, uint(10330));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_HighHighState_Number =
      new NodeId(UShort.MIN, uint(10331));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_HighHighState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(10332));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_HighHighState_TransitionTime =
      new NodeId(UShort.MIN, uint(10333));

  public static final NodeId
      NonExclusiveRateOfChangeAlarmType_HighHighState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(10334));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_HighHighState_TrueState =
      new NodeId(UShort.MIN, uint(10335));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_HighHighState_FalseState =
      new NodeId(UShort.MIN, uint(10336));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_HighState =
      new NodeId(UShort.MIN, uint(10337));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_HighState_Id =
      new NodeId(UShort.MIN, uint(10338));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_HighState_Name =
      new NodeId(UShort.MIN, uint(10339));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_HighState_Number =
      new NodeId(UShort.MIN, uint(10340));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_HighState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(10341));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_HighState_TransitionTime =
      new NodeId(UShort.MIN, uint(10342));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_HighState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(10343));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_HighState_TrueState =
      new NodeId(UShort.MIN, uint(10344));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_HighState_FalseState =
      new NodeId(UShort.MIN, uint(10345));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_LowState =
      new NodeId(UShort.MIN, uint(10346));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_LowState_Id =
      new NodeId(UShort.MIN, uint(10347));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_LowState_Name =
      new NodeId(UShort.MIN, uint(10348));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_LowState_Number =
      new NodeId(UShort.MIN, uint(10349));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_LowState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(10350));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_LowState_TransitionTime =
      new NodeId(UShort.MIN, uint(10351));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_LowState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(10352));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_LowState_TrueState =
      new NodeId(UShort.MIN, uint(10353));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_LowState_FalseState =
      new NodeId(UShort.MIN, uint(10354));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_LowLowState =
      new NodeId(UShort.MIN, uint(10355));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_LowLowState_Id =
      new NodeId(UShort.MIN, uint(10356));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_LowLowState_Name =
      new NodeId(UShort.MIN, uint(10357));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_LowLowState_Number =
      new NodeId(UShort.MIN, uint(10358));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_LowLowState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(10359));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_LowLowState_TransitionTime =
      new NodeId(UShort.MIN, uint(10360));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_LowLowState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(10361));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_LowLowState_TrueState =
      new NodeId(UShort.MIN, uint(10362));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_LowLowState_FalseState =
      new NodeId(UShort.MIN, uint(10363));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_HighHighLimit =
      new NodeId(UShort.MIN, uint(10364));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_HighLimit =
      new NodeId(UShort.MIN, uint(10365));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_LowLimit =
      new NodeId(UShort.MIN, uint(10366));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_LowLowLimit =
      new NodeId(UShort.MIN, uint(10367));

  public static final NodeId NonExclusiveDeviationAlarmType = new NodeId(UShort.MIN, uint(10368));

  public static final NodeId NonExclusiveDeviationAlarmType_EventId =
      new NodeId(UShort.MIN, uint(10369));

  public static final NodeId NonExclusiveDeviationAlarmType_EventType =
      new NodeId(UShort.MIN, uint(10370));

  public static final NodeId NonExclusiveDeviationAlarmType_SourceNode =
      new NodeId(UShort.MIN, uint(10371));

  public static final NodeId NonExclusiveDeviationAlarmType_SourceName =
      new NodeId(UShort.MIN, uint(10372));

  public static final NodeId NonExclusiveDeviationAlarmType_Time =
      new NodeId(UShort.MIN, uint(10373));

  public static final NodeId NonExclusiveDeviationAlarmType_ReceiveTime =
      new NodeId(UShort.MIN, uint(10374));

  public static final NodeId NonExclusiveDeviationAlarmType_LocalTime =
      new NodeId(UShort.MIN, uint(10375));

  public static final NodeId NonExclusiveDeviationAlarmType_Message =
      new NodeId(UShort.MIN, uint(10376));

  public static final NodeId NonExclusiveDeviationAlarmType_Severity =
      new NodeId(UShort.MIN, uint(10377));

  public static final NodeId NonExclusiveDeviationAlarmType_ConditionName =
      new NodeId(UShort.MIN, uint(10378));

  public static final NodeId NonExclusiveDeviationAlarmType_BranchId =
      new NodeId(UShort.MIN, uint(10379));

  public static final NodeId NonExclusiveDeviationAlarmType_Retain =
      new NodeId(UShort.MIN, uint(10380));

  public static final NodeId NonExclusiveDeviationAlarmType_EnabledState =
      new NodeId(UShort.MIN, uint(10381));

  public static final NodeId NonExclusiveDeviationAlarmType_EnabledState_Id =
      new NodeId(UShort.MIN, uint(10382));

  public static final NodeId NonExclusiveDeviationAlarmType_EnabledState_Name =
      new NodeId(UShort.MIN, uint(10383));

  public static final NodeId NonExclusiveDeviationAlarmType_EnabledState_Number =
      new NodeId(UShort.MIN, uint(10384));

  public static final NodeId NonExclusiveDeviationAlarmType_EnabledState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(10385));

  public static final NodeId NonExclusiveDeviationAlarmType_EnabledState_TransitionTime =
      new NodeId(UShort.MIN, uint(10386));

  public static final NodeId NonExclusiveDeviationAlarmType_EnabledState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(10387));

  public static final NodeId NonExclusiveDeviationAlarmType_EnabledState_TrueState =
      new NodeId(UShort.MIN, uint(10388));

  public static final NodeId NonExclusiveDeviationAlarmType_EnabledState_FalseState =
      new NodeId(UShort.MIN, uint(10389));

  public static final NodeId NonExclusiveDeviationAlarmType_Quality =
      new NodeId(UShort.MIN, uint(10390));

  public static final NodeId NonExclusiveDeviationAlarmType_Quality_SourceTimestamp =
      new NodeId(UShort.MIN, uint(10391));

  public static final NodeId NonExclusiveDeviationAlarmType_LastSeverity =
      new NodeId(UShort.MIN, uint(10392));

  public static final NodeId NonExclusiveDeviationAlarmType_LastSeverity_SourceTimestamp =
      new NodeId(UShort.MIN, uint(10393));

  public static final NodeId NonExclusiveDeviationAlarmType_Comment =
      new NodeId(UShort.MIN, uint(10394));

  public static final NodeId NonExclusiveDeviationAlarmType_Comment_SourceTimestamp =
      new NodeId(UShort.MIN, uint(10395));

  public static final NodeId NonExclusiveDeviationAlarmType_ClientUserId =
      new NodeId(UShort.MIN, uint(10396));

  public static final NodeId NonExclusiveDeviationAlarmType_Enable =
      new NodeId(UShort.MIN, uint(10397));

  public static final NodeId NonExclusiveDeviationAlarmType_Disable =
      new NodeId(UShort.MIN, uint(10398));

  public static final NodeId NonExclusiveDeviationAlarmType_AddComment =
      new NodeId(UShort.MIN, uint(10399));

  public static final NodeId NonExclusiveDeviationAlarmType_AddComment_InputArguments =
      new NodeId(UShort.MIN, uint(10400));

  public static final NodeId NonExclusiveDeviationAlarmType_ConditionRefresh =
      new NodeId(UShort.MIN, uint(10401));

  public static final NodeId NonExclusiveDeviationAlarmType_ConditionRefresh_InputArguments =
      new NodeId(UShort.MIN, uint(10402));

  public static final NodeId NonExclusiveDeviationAlarmType_AckedState =
      new NodeId(UShort.MIN, uint(10403));

  public static final NodeId NonExclusiveDeviationAlarmType_AckedState_Id =
      new NodeId(UShort.MIN, uint(10404));

  public static final NodeId NonExclusiveDeviationAlarmType_AckedState_Name =
      new NodeId(UShort.MIN, uint(10405));

  public static final NodeId NonExclusiveDeviationAlarmType_AckedState_Number =
      new NodeId(UShort.MIN, uint(10406));

  public static final NodeId NonExclusiveDeviationAlarmType_AckedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(10407));

  public static final NodeId NonExclusiveDeviationAlarmType_AckedState_TransitionTime =
      new NodeId(UShort.MIN, uint(10408));

  public static final NodeId NonExclusiveDeviationAlarmType_AckedState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(10409));

  public static final NodeId NonExclusiveDeviationAlarmType_AckedState_TrueState =
      new NodeId(UShort.MIN, uint(10410));

  public static final NodeId NonExclusiveDeviationAlarmType_AckedState_FalseState =
      new NodeId(UShort.MIN, uint(10411));

  public static final NodeId NonExclusiveDeviationAlarmType_ConfirmedState =
      new NodeId(UShort.MIN, uint(10412));

  public static final NodeId NonExclusiveDeviationAlarmType_ConfirmedState_Id =
      new NodeId(UShort.MIN, uint(10413));

  public static final NodeId NonExclusiveDeviationAlarmType_ConfirmedState_Name =
      new NodeId(UShort.MIN, uint(10414));

  public static final NodeId NonExclusiveDeviationAlarmType_ConfirmedState_Number =
      new NodeId(UShort.MIN, uint(10415));

  public static final NodeId NonExclusiveDeviationAlarmType_ConfirmedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(10416));

  public static final NodeId NonExclusiveDeviationAlarmType_ConfirmedState_TransitionTime =
      new NodeId(UShort.MIN, uint(10417));

  public static final NodeId NonExclusiveDeviationAlarmType_ConfirmedState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(10418));

  public static final NodeId NonExclusiveDeviationAlarmType_ConfirmedState_TrueState =
      new NodeId(UShort.MIN, uint(10419));

  public static final NodeId NonExclusiveDeviationAlarmType_ConfirmedState_FalseState =
      new NodeId(UShort.MIN, uint(10420));

  public static final NodeId NonExclusiveDeviationAlarmType_Acknowledge =
      new NodeId(UShort.MIN, uint(10421));

  public static final NodeId NonExclusiveDeviationAlarmType_Acknowledge_InputArguments =
      new NodeId(UShort.MIN, uint(10422));

  public static final NodeId NonExclusiveDeviationAlarmType_Confirm =
      new NodeId(UShort.MIN, uint(10423));

  public static final NodeId NonExclusiveDeviationAlarmType_Confirm_InputArguments =
      new NodeId(UShort.MIN, uint(10424));

  public static final NodeId NonExclusiveDeviationAlarmType_ActiveState =
      new NodeId(UShort.MIN, uint(10425));

  public static final NodeId NonExclusiveDeviationAlarmType_ActiveState_Id =
      new NodeId(UShort.MIN, uint(10426));

  public static final NodeId NonExclusiveDeviationAlarmType_ActiveState_Name =
      new NodeId(UShort.MIN, uint(10427));

  public static final NodeId NonExclusiveDeviationAlarmType_ActiveState_Number =
      new NodeId(UShort.MIN, uint(10428));

  public static final NodeId NonExclusiveDeviationAlarmType_ActiveState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(10429));

  public static final NodeId NonExclusiveDeviationAlarmType_ActiveState_TransitionTime =
      new NodeId(UShort.MIN, uint(10430));

  public static final NodeId NonExclusiveDeviationAlarmType_ActiveState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(10431));

  public static final NodeId NonExclusiveDeviationAlarmType_ActiveState_TrueState =
      new NodeId(UShort.MIN, uint(10432));

  public static final NodeId NonExclusiveDeviationAlarmType_ActiveState_FalseState =
      new NodeId(UShort.MIN, uint(10433));

  public static final NodeId NonExclusiveDeviationAlarmType_SuppressedState =
      new NodeId(UShort.MIN, uint(10434));

  public static final NodeId NonExclusiveDeviationAlarmType_SuppressedState_Id =
      new NodeId(UShort.MIN, uint(10435));

  public static final NodeId NonExclusiveDeviationAlarmType_SuppressedState_Name =
      new NodeId(UShort.MIN, uint(10436));

  public static final NodeId NonExclusiveDeviationAlarmType_SuppressedState_Number =
      new NodeId(UShort.MIN, uint(10437));

  public static final NodeId NonExclusiveDeviationAlarmType_SuppressedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(10438));

  public static final NodeId NonExclusiveDeviationAlarmType_SuppressedState_TransitionTime =
      new NodeId(UShort.MIN, uint(10439));

  public static final NodeId
      NonExclusiveDeviationAlarmType_SuppressedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(10440));

  public static final NodeId NonExclusiveDeviationAlarmType_SuppressedState_TrueState =
      new NodeId(UShort.MIN, uint(10441));

  public static final NodeId NonExclusiveDeviationAlarmType_SuppressedState_FalseState =
      new NodeId(UShort.MIN, uint(10442));

  public static final NodeId NonExclusiveDeviationAlarmType_ShelvingState =
      new NodeId(UShort.MIN, uint(10443));

  public static final NodeId NonExclusiveDeviationAlarmType_ShelvingState_CurrentState =
      new NodeId(UShort.MIN, uint(10444));

  public static final NodeId NonExclusiveDeviationAlarmType_ShelvingState_CurrentState_Id =
      new NodeId(UShort.MIN, uint(10445));

  public static final NodeId NonExclusiveDeviationAlarmType_ShelvingState_CurrentState_Name =
      new NodeId(UShort.MIN, uint(10446));

  public static final NodeId NonExclusiveDeviationAlarmType_ShelvingState_CurrentState_Number =
      new NodeId(UShort.MIN, uint(10447));

  public static final NodeId
      NonExclusiveDeviationAlarmType_ShelvingState_CurrentState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(10448));

  public static final NodeId NonExclusiveDeviationAlarmType_ShelvingState_LastTransition =
      new NodeId(UShort.MIN, uint(10449));

  public static final NodeId NonExclusiveDeviationAlarmType_ShelvingState_LastTransition_Id =
      new NodeId(UShort.MIN, uint(10450));

  public static final NodeId NonExclusiveDeviationAlarmType_ShelvingState_LastTransition_Name =
      new NodeId(UShort.MIN, uint(10451));

  public static final NodeId NonExclusiveDeviationAlarmType_ShelvingState_LastTransition_Number =
      new NodeId(UShort.MIN, uint(10452));

  public static final NodeId
      NonExclusiveDeviationAlarmType_ShelvingState_LastTransition_TransitionTime =
          new NodeId(UShort.MIN, uint(10453));

  public static final NodeId NonExclusiveDeviationAlarmType_ShelvingState_UnshelveTime =
      new NodeId(UShort.MIN, uint(10454));

  public static final NodeId NonExclusiveDeviationAlarmType_ShelvingState_Unshelve =
      new NodeId(UShort.MIN, uint(10476));

  public static final NodeId NonExclusiveDeviationAlarmType_ShelvingState_OneShotShelve =
      new NodeId(UShort.MIN, uint(10477));

  public static final NodeId NonExclusiveDeviationAlarmType_ShelvingState_TimedShelve =
      new NodeId(UShort.MIN, uint(10478));

  public static final NodeId
      NonExclusiveDeviationAlarmType_ShelvingState_TimedShelve_InputArguments =
          new NodeId(UShort.MIN, uint(10479));

  public static final NodeId NonExclusiveDeviationAlarmType_SuppressedOrShelved =
      new NodeId(UShort.MIN, uint(10480));

  public static final NodeId NonExclusiveDeviationAlarmType_MaxTimeShelved =
      new NodeId(UShort.MIN, uint(10481));

  public static final NodeId NonExclusiveDeviationAlarmType_HighHighState =
      new NodeId(UShort.MIN, uint(10482));

  public static final NodeId NonExclusiveDeviationAlarmType_HighHighState_Id =
      new NodeId(UShort.MIN, uint(10483));

  public static final NodeId NonExclusiveDeviationAlarmType_HighHighState_Name =
      new NodeId(UShort.MIN, uint(10484));

  public static final NodeId NonExclusiveDeviationAlarmType_HighHighState_Number =
      new NodeId(UShort.MIN, uint(10485));

  public static final NodeId NonExclusiveDeviationAlarmType_HighHighState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(10486));

  public static final NodeId NonExclusiveDeviationAlarmType_HighHighState_TransitionTime =
      new NodeId(UShort.MIN, uint(10487));

  public static final NodeId NonExclusiveDeviationAlarmType_HighHighState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(10488));

  public static final NodeId NonExclusiveDeviationAlarmType_HighHighState_TrueState =
      new NodeId(UShort.MIN, uint(10489));

  public static final NodeId NonExclusiveDeviationAlarmType_HighHighState_FalseState =
      new NodeId(UShort.MIN, uint(10490));

  public static final NodeId NonExclusiveDeviationAlarmType_HighState =
      new NodeId(UShort.MIN, uint(10491));

  public static final NodeId NonExclusiveDeviationAlarmType_HighState_Id =
      new NodeId(UShort.MIN, uint(10492));

  public static final NodeId NonExclusiveDeviationAlarmType_HighState_Name =
      new NodeId(UShort.MIN, uint(10493));

  public static final NodeId NonExclusiveDeviationAlarmType_HighState_Number =
      new NodeId(UShort.MIN, uint(10494));

  public static final NodeId NonExclusiveDeviationAlarmType_HighState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(10495));

  public static final NodeId NonExclusiveDeviationAlarmType_HighState_TransitionTime =
      new NodeId(UShort.MIN, uint(10496));

  public static final NodeId NonExclusiveDeviationAlarmType_HighState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(10497));

  public static final NodeId NonExclusiveDeviationAlarmType_HighState_TrueState =
      new NodeId(UShort.MIN, uint(10498));

  public static final NodeId NonExclusiveDeviationAlarmType_HighState_FalseState =
      new NodeId(UShort.MIN, uint(10499));

  public static final NodeId NonExclusiveDeviationAlarmType_LowState =
      new NodeId(UShort.MIN, uint(10500));

  public static final NodeId NonExclusiveDeviationAlarmType_LowState_Id =
      new NodeId(UShort.MIN, uint(10501));

  public static final NodeId NonExclusiveDeviationAlarmType_LowState_Name =
      new NodeId(UShort.MIN, uint(10502));

  public static final NodeId NonExclusiveDeviationAlarmType_LowState_Number =
      new NodeId(UShort.MIN, uint(10503));

  public static final NodeId NonExclusiveDeviationAlarmType_LowState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(10504));

  public static final NodeId NonExclusiveDeviationAlarmType_LowState_TransitionTime =
      new NodeId(UShort.MIN, uint(10505));

  public static final NodeId NonExclusiveDeviationAlarmType_LowState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(10506));

  public static final NodeId NonExclusiveDeviationAlarmType_LowState_TrueState =
      new NodeId(UShort.MIN, uint(10507));

  public static final NodeId NonExclusiveDeviationAlarmType_LowState_FalseState =
      new NodeId(UShort.MIN, uint(10508));

  public static final NodeId NonExclusiveDeviationAlarmType_LowLowState =
      new NodeId(UShort.MIN, uint(10509));

  public static final NodeId NonExclusiveDeviationAlarmType_LowLowState_Id =
      new NodeId(UShort.MIN, uint(10510));

  public static final NodeId NonExclusiveDeviationAlarmType_LowLowState_Name =
      new NodeId(UShort.MIN, uint(10511));

  public static final NodeId NonExclusiveDeviationAlarmType_LowLowState_Number =
      new NodeId(UShort.MIN, uint(10512));

  public static final NodeId NonExclusiveDeviationAlarmType_LowLowState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(10513));

  public static final NodeId NonExclusiveDeviationAlarmType_LowLowState_TransitionTime =
      new NodeId(UShort.MIN, uint(10514));

  public static final NodeId NonExclusiveDeviationAlarmType_LowLowState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(10515));

  public static final NodeId NonExclusiveDeviationAlarmType_LowLowState_TrueState =
      new NodeId(UShort.MIN, uint(10516));

  public static final NodeId NonExclusiveDeviationAlarmType_LowLowState_FalseState =
      new NodeId(UShort.MIN, uint(10517));

  public static final NodeId NonExclusiveDeviationAlarmType_HighHighLimit =
      new NodeId(UShort.MIN, uint(10518));

  public static final NodeId NonExclusiveDeviationAlarmType_HighLimit =
      new NodeId(UShort.MIN, uint(10519));

  public static final NodeId NonExclusiveDeviationAlarmType_LowLimit =
      new NodeId(UShort.MIN, uint(10520));

  public static final NodeId NonExclusiveDeviationAlarmType_LowLowLimit =
      new NodeId(UShort.MIN, uint(10521));

  public static final NodeId NonExclusiveDeviationAlarmType_SetpointNode =
      new NodeId(UShort.MIN, uint(10522));

  public static final NodeId DiscreteAlarmType = new NodeId(UShort.MIN, uint(10523));

  public static final NodeId DiscreteAlarmType_EventId = new NodeId(UShort.MIN, uint(10524));

  public static final NodeId DiscreteAlarmType_EventType = new NodeId(UShort.MIN, uint(10525));

  public static final NodeId DiscreteAlarmType_SourceNode = new NodeId(UShort.MIN, uint(10526));

  public static final NodeId DiscreteAlarmType_SourceName = new NodeId(UShort.MIN, uint(10527));

  public static final NodeId DiscreteAlarmType_Time = new NodeId(UShort.MIN, uint(10528));

  public static final NodeId DiscreteAlarmType_ReceiveTime = new NodeId(UShort.MIN, uint(10529));

  public static final NodeId DiscreteAlarmType_LocalTime = new NodeId(UShort.MIN, uint(10530));

  public static final NodeId DiscreteAlarmType_Message = new NodeId(UShort.MIN, uint(10531));

  public static final NodeId DiscreteAlarmType_Severity = new NodeId(UShort.MIN, uint(10532));

  public static final NodeId DiscreteAlarmType_ConditionName = new NodeId(UShort.MIN, uint(10533));

  public static final NodeId DiscreteAlarmType_BranchId = new NodeId(UShort.MIN, uint(10534));

  public static final NodeId DiscreteAlarmType_Retain = new NodeId(UShort.MIN, uint(10535));

  public static final NodeId DiscreteAlarmType_EnabledState = new NodeId(UShort.MIN, uint(10536));

  public static final NodeId DiscreteAlarmType_EnabledState_Id =
      new NodeId(UShort.MIN, uint(10537));

  public static final NodeId DiscreteAlarmType_EnabledState_Name =
      new NodeId(UShort.MIN, uint(10538));

  public static final NodeId DiscreteAlarmType_EnabledState_Number =
      new NodeId(UShort.MIN, uint(10539));

  public static final NodeId DiscreteAlarmType_EnabledState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(10540));

  public static final NodeId DiscreteAlarmType_EnabledState_TransitionTime =
      new NodeId(UShort.MIN, uint(10541));

  public static final NodeId DiscreteAlarmType_EnabledState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(10542));

  public static final NodeId DiscreteAlarmType_EnabledState_TrueState =
      new NodeId(UShort.MIN, uint(10543));

  public static final NodeId DiscreteAlarmType_EnabledState_FalseState =
      new NodeId(UShort.MIN, uint(10544));

  public static final NodeId DiscreteAlarmType_Quality = new NodeId(UShort.MIN, uint(10545));

  public static final NodeId DiscreteAlarmType_Quality_SourceTimestamp =
      new NodeId(UShort.MIN, uint(10546));

  public static final NodeId DiscreteAlarmType_LastSeverity = new NodeId(UShort.MIN, uint(10547));

  public static final NodeId DiscreteAlarmType_LastSeverity_SourceTimestamp =
      new NodeId(UShort.MIN, uint(10548));

  public static final NodeId DiscreteAlarmType_Comment = new NodeId(UShort.MIN, uint(10549));

  public static final NodeId DiscreteAlarmType_Comment_SourceTimestamp =
      new NodeId(UShort.MIN, uint(10550));

  public static final NodeId DiscreteAlarmType_ClientUserId = new NodeId(UShort.MIN, uint(10551));

  public static final NodeId DiscreteAlarmType_Enable = new NodeId(UShort.MIN, uint(10552));

  public static final NodeId DiscreteAlarmType_Disable = new NodeId(UShort.MIN, uint(10553));

  public static final NodeId DiscreteAlarmType_AddComment = new NodeId(UShort.MIN, uint(10554));

  public static final NodeId DiscreteAlarmType_AddComment_InputArguments =
      new NodeId(UShort.MIN, uint(10555));

  public static final NodeId DiscreteAlarmType_ConditionRefresh =
      new NodeId(UShort.MIN, uint(10556));

  public static final NodeId DiscreteAlarmType_ConditionRefresh_InputArguments =
      new NodeId(UShort.MIN, uint(10557));

  public static final NodeId DiscreteAlarmType_AckedState = new NodeId(UShort.MIN, uint(10558));

  public static final NodeId DiscreteAlarmType_AckedState_Id = new NodeId(UShort.MIN, uint(10559));

  public static final NodeId DiscreteAlarmType_AckedState_Name =
      new NodeId(UShort.MIN, uint(10560));

  public static final NodeId DiscreteAlarmType_AckedState_Number =
      new NodeId(UShort.MIN, uint(10561));

  public static final NodeId DiscreteAlarmType_AckedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(10562));

  public static final NodeId DiscreteAlarmType_AckedState_TransitionTime =
      new NodeId(UShort.MIN, uint(10563));

  public static final NodeId DiscreteAlarmType_AckedState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(10564));

  public static final NodeId DiscreteAlarmType_AckedState_TrueState =
      new NodeId(UShort.MIN, uint(10565));

  public static final NodeId DiscreteAlarmType_AckedState_FalseState =
      new NodeId(UShort.MIN, uint(10566));

  public static final NodeId DiscreteAlarmType_ConfirmedState = new NodeId(UShort.MIN, uint(10567));

  public static final NodeId DiscreteAlarmType_ConfirmedState_Id =
      new NodeId(UShort.MIN, uint(10568));

  public static final NodeId DiscreteAlarmType_ConfirmedState_Name =
      new NodeId(UShort.MIN, uint(10569));

  public static final NodeId DiscreteAlarmType_ConfirmedState_Number =
      new NodeId(UShort.MIN, uint(10570));

  public static final NodeId DiscreteAlarmType_ConfirmedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(10571));

  public static final NodeId DiscreteAlarmType_ConfirmedState_TransitionTime =
      new NodeId(UShort.MIN, uint(10572));

  public static final NodeId DiscreteAlarmType_ConfirmedState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(10573));

  public static final NodeId DiscreteAlarmType_ConfirmedState_TrueState =
      new NodeId(UShort.MIN, uint(10574));

  public static final NodeId DiscreteAlarmType_ConfirmedState_FalseState =
      new NodeId(UShort.MIN, uint(10575));

  public static final NodeId DiscreteAlarmType_Acknowledge = new NodeId(UShort.MIN, uint(10576));

  public static final NodeId DiscreteAlarmType_Acknowledge_InputArguments =
      new NodeId(UShort.MIN, uint(10577));

  public static final NodeId DiscreteAlarmType_Confirm = new NodeId(UShort.MIN, uint(10578));

  public static final NodeId DiscreteAlarmType_Confirm_InputArguments =
      new NodeId(UShort.MIN, uint(10579));

  public static final NodeId DiscreteAlarmType_ActiveState = new NodeId(UShort.MIN, uint(10580));

  public static final NodeId DiscreteAlarmType_ActiveState_Id = new NodeId(UShort.MIN, uint(10581));

  public static final NodeId DiscreteAlarmType_ActiveState_Name =
      new NodeId(UShort.MIN, uint(10582));

  public static final NodeId DiscreteAlarmType_ActiveState_Number =
      new NodeId(UShort.MIN, uint(10583));

  public static final NodeId DiscreteAlarmType_ActiveState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(10584));

  public static final NodeId DiscreteAlarmType_ActiveState_TransitionTime =
      new NodeId(UShort.MIN, uint(10585));

  public static final NodeId DiscreteAlarmType_ActiveState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(10586));

  public static final NodeId DiscreteAlarmType_ActiveState_TrueState =
      new NodeId(UShort.MIN, uint(10587));

  public static final NodeId DiscreteAlarmType_ActiveState_FalseState =
      new NodeId(UShort.MIN, uint(10588));

  public static final NodeId DiscreteAlarmType_SuppressedState =
      new NodeId(UShort.MIN, uint(10589));

  public static final NodeId DiscreteAlarmType_SuppressedState_Id =
      new NodeId(UShort.MIN, uint(10590));

  public static final NodeId DiscreteAlarmType_SuppressedState_Name =
      new NodeId(UShort.MIN, uint(10591));

  public static final NodeId DiscreteAlarmType_SuppressedState_Number =
      new NodeId(UShort.MIN, uint(10592));

  public static final NodeId DiscreteAlarmType_SuppressedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(10593));

  public static final NodeId DiscreteAlarmType_SuppressedState_TransitionTime =
      new NodeId(UShort.MIN, uint(10594));

  public static final NodeId DiscreteAlarmType_SuppressedState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(10595));

  public static final NodeId DiscreteAlarmType_SuppressedState_TrueState =
      new NodeId(UShort.MIN, uint(10596));

  public static final NodeId DiscreteAlarmType_SuppressedState_FalseState =
      new NodeId(UShort.MIN, uint(10597));

  public static final NodeId DiscreteAlarmType_ShelvingState = new NodeId(UShort.MIN, uint(10598));

  public static final NodeId DiscreteAlarmType_ShelvingState_CurrentState =
      new NodeId(UShort.MIN, uint(10599));

  public static final NodeId DiscreteAlarmType_ShelvingState_CurrentState_Id =
      new NodeId(UShort.MIN, uint(10600));

  public static final NodeId DiscreteAlarmType_ShelvingState_CurrentState_Name =
      new NodeId(UShort.MIN, uint(10601));

  public static final NodeId DiscreteAlarmType_ShelvingState_CurrentState_Number =
      new NodeId(UShort.MIN, uint(10602));

  public static final NodeId DiscreteAlarmType_ShelvingState_CurrentState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(10603));

  public static final NodeId DiscreteAlarmType_ShelvingState_LastTransition =
      new NodeId(UShort.MIN, uint(10604));

  public static final NodeId DiscreteAlarmType_ShelvingState_LastTransition_Id =
      new NodeId(UShort.MIN, uint(10605));

  public static final NodeId DiscreteAlarmType_ShelvingState_LastTransition_Name =
      new NodeId(UShort.MIN, uint(10606));

  public static final NodeId DiscreteAlarmType_ShelvingState_LastTransition_Number =
      new NodeId(UShort.MIN, uint(10607));

  public static final NodeId DiscreteAlarmType_ShelvingState_LastTransition_TransitionTime =
      new NodeId(UShort.MIN, uint(10608));

  public static final NodeId DiscreteAlarmType_ShelvingState_UnshelveTime =
      new NodeId(UShort.MIN, uint(10609));

  public static final NodeId DiscreteAlarmType_ShelvingState_Unshelve =
      new NodeId(UShort.MIN, uint(10631));

  public static final NodeId DiscreteAlarmType_ShelvingState_OneShotShelve =
      new NodeId(UShort.MIN, uint(10632));

  public static final NodeId DiscreteAlarmType_ShelvingState_TimedShelve =
      new NodeId(UShort.MIN, uint(10633));

  public static final NodeId DiscreteAlarmType_ShelvingState_TimedShelve_InputArguments =
      new NodeId(UShort.MIN, uint(10634));

  public static final NodeId DiscreteAlarmType_SuppressedOrShelved =
      new NodeId(UShort.MIN, uint(10635));

  public static final NodeId DiscreteAlarmType_MaxTimeShelved = new NodeId(UShort.MIN, uint(10636));

  public static final NodeId OffNormalAlarmType = new NodeId(UShort.MIN, uint(10637));

  public static final NodeId OffNormalAlarmType_EventId = new NodeId(UShort.MIN, uint(10638));

  public static final NodeId OffNormalAlarmType_EventType = new NodeId(UShort.MIN, uint(10639));

  public static final NodeId OffNormalAlarmType_SourceNode = new NodeId(UShort.MIN, uint(10640));

  public static final NodeId OffNormalAlarmType_SourceName = new NodeId(UShort.MIN, uint(10641));

  public static final NodeId OffNormalAlarmType_Time = new NodeId(UShort.MIN, uint(10642));

  public static final NodeId OffNormalAlarmType_ReceiveTime = new NodeId(UShort.MIN, uint(10643));

  public static final NodeId OffNormalAlarmType_LocalTime = new NodeId(UShort.MIN, uint(10644));

  public static final NodeId OffNormalAlarmType_Message = new NodeId(UShort.MIN, uint(10645));

  public static final NodeId OffNormalAlarmType_Severity = new NodeId(UShort.MIN, uint(10646));

  public static final NodeId OffNormalAlarmType_ConditionName = new NodeId(UShort.MIN, uint(10647));

  public static final NodeId OffNormalAlarmType_BranchId = new NodeId(UShort.MIN, uint(10648));

  public static final NodeId OffNormalAlarmType_Retain = new NodeId(UShort.MIN, uint(10649));

  public static final NodeId OffNormalAlarmType_EnabledState = new NodeId(UShort.MIN, uint(10650));

  public static final NodeId OffNormalAlarmType_EnabledState_Id =
      new NodeId(UShort.MIN, uint(10651));

  public static final NodeId OffNormalAlarmType_EnabledState_Name =
      new NodeId(UShort.MIN, uint(10652));

  public static final NodeId OffNormalAlarmType_EnabledState_Number =
      new NodeId(UShort.MIN, uint(10653));

  public static final NodeId OffNormalAlarmType_EnabledState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(10654));

  public static final NodeId OffNormalAlarmType_EnabledState_TransitionTime =
      new NodeId(UShort.MIN, uint(10655));

  public static final NodeId OffNormalAlarmType_EnabledState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(10656));

  public static final NodeId OffNormalAlarmType_EnabledState_TrueState =
      new NodeId(UShort.MIN, uint(10657));

  public static final NodeId OffNormalAlarmType_EnabledState_FalseState =
      new NodeId(UShort.MIN, uint(10658));

  public static final NodeId OffNormalAlarmType_Quality = new NodeId(UShort.MIN, uint(10659));

  public static final NodeId OffNormalAlarmType_Quality_SourceTimestamp =
      new NodeId(UShort.MIN, uint(10660));

  public static final NodeId OffNormalAlarmType_LastSeverity = new NodeId(UShort.MIN, uint(10661));

  public static final NodeId OffNormalAlarmType_LastSeverity_SourceTimestamp =
      new NodeId(UShort.MIN, uint(10662));

  public static final NodeId OffNormalAlarmType_Comment = new NodeId(UShort.MIN, uint(10663));

  public static final NodeId OffNormalAlarmType_Comment_SourceTimestamp =
      new NodeId(UShort.MIN, uint(10664));

  public static final NodeId OffNormalAlarmType_ClientUserId = new NodeId(UShort.MIN, uint(10665));

  public static final NodeId OffNormalAlarmType_Enable = new NodeId(UShort.MIN, uint(10666));

  public static final NodeId OffNormalAlarmType_Disable = new NodeId(UShort.MIN, uint(10667));

  public static final NodeId OffNormalAlarmType_AddComment = new NodeId(UShort.MIN, uint(10668));

  public static final NodeId OffNormalAlarmType_AddComment_InputArguments =
      new NodeId(UShort.MIN, uint(10669));

  public static final NodeId OffNormalAlarmType_ConditionRefresh =
      new NodeId(UShort.MIN, uint(10670));

  public static final NodeId OffNormalAlarmType_ConditionRefresh_InputArguments =
      new NodeId(UShort.MIN, uint(10671));

  public static final NodeId OffNormalAlarmType_AckedState = new NodeId(UShort.MIN, uint(10672));

  public static final NodeId OffNormalAlarmType_AckedState_Id = new NodeId(UShort.MIN, uint(10673));

  public static final NodeId OffNormalAlarmType_AckedState_Name =
      new NodeId(UShort.MIN, uint(10674));

  public static final NodeId OffNormalAlarmType_AckedState_Number =
      new NodeId(UShort.MIN, uint(10675));

  public static final NodeId OffNormalAlarmType_AckedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(10676));

  public static final NodeId OffNormalAlarmType_AckedState_TransitionTime =
      new NodeId(UShort.MIN, uint(10677));

  public static final NodeId OffNormalAlarmType_AckedState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(10678));

  public static final NodeId OffNormalAlarmType_AckedState_TrueState =
      new NodeId(UShort.MIN, uint(10679));

  public static final NodeId OffNormalAlarmType_AckedState_FalseState =
      new NodeId(UShort.MIN, uint(10680));

  public static final NodeId OffNormalAlarmType_ConfirmedState =
      new NodeId(UShort.MIN, uint(10681));

  public static final NodeId OffNormalAlarmType_ConfirmedState_Id =
      new NodeId(UShort.MIN, uint(10682));

  public static final NodeId OffNormalAlarmType_ConfirmedState_Name =
      new NodeId(UShort.MIN, uint(10683));

  public static final NodeId OffNormalAlarmType_ConfirmedState_Number =
      new NodeId(UShort.MIN, uint(10684));

  public static final NodeId OffNormalAlarmType_ConfirmedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(10685));

  public static final NodeId OffNormalAlarmType_ConfirmedState_TransitionTime =
      new NodeId(UShort.MIN, uint(10686));

  public static final NodeId OffNormalAlarmType_ConfirmedState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(10687));

  public static final NodeId OffNormalAlarmType_ConfirmedState_TrueState =
      new NodeId(UShort.MIN, uint(10688));

  public static final NodeId OffNormalAlarmType_ConfirmedState_FalseState =
      new NodeId(UShort.MIN, uint(10689));

  public static final NodeId OffNormalAlarmType_Acknowledge = new NodeId(UShort.MIN, uint(10690));

  public static final NodeId OffNormalAlarmType_Acknowledge_InputArguments =
      new NodeId(UShort.MIN, uint(10691));

  public static final NodeId OffNormalAlarmType_Confirm = new NodeId(UShort.MIN, uint(10692));

  public static final NodeId OffNormalAlarmType_Confirm_InputArguments =
      new NodeId(UShort.MIN, uint(10693));

  public static final NodeId OffNormalAlarmType_ActiveState = new NodeId(UShort.MIN, uint(10694));

  public static final NodeId OffNormalAlarmType_ActiveState_Id =
      new NodeId(UShort.MIN, uint(10695));

  public static final NodeId OffNormalAlarmType_ActiveState_Name =
      new NodeId(UShort.MIN, uint(10696));

  public static final NodeId OffNormalAlarmType_ActiveState_Number =
      new NodeId(UShort.MIN, uint(10697));

  public static final NodeId OffNormalAlarmType_ActiveState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(10698));

  public static final NodeId OffNormalAlarmType_ActiveState_TransitionTime =
      new NodeId(UShort.MIN, uint(10699));

  public static final NodeId OffNormalAlarmType_ActiveState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(10700));

  public static final NodeId OffNormalAlarmType_ActiveState_TrueState =
      new NodeId(UShort.MIN, uint(10701));

  public static final NodeId OffNormalAlarmType_ActiveState_FalseState =
      new NodeId(UShort.MIN, uint(10702));

  public static final NodeId OffNormalAlarmType_SuppressedState =
      new NodeId(UShort.MIN, uint(10703));

  public static final NodeId OffNormalAlarmType_SuppressedState_Id =
      new NodeId(UShort.MIN, uint(10704));

  public static final NodeId OffNormalAlarmType_SuppressedState_Name =
      new NodeId(UShort.MIN, uint(10705));

  public static final NodeId OffNormalAlarmType_SuppressedState_Number =
      new NodeId(UShort.MIN, uint(10706));

  public static final NodeId OffNormalAlarmType_SuppressedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(10707));

  public static final NodeId OffNormalAlarmType_SuppressedState_TransitionTime =
      new NodeId(UShort.MIN, uint(10708));

  public static final NodeId OffNormalAlarmType_SuppressedState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(10709));

  public static final NodeId OffNormalAlarmType_SuppressedState_TrueState =
      new NodeId(UShort.MIN, uint(10710));

  public static final NodeId OffNormalAlarmType_SuppressedState_FalseState =
      new NodeId(UShort.MIN, uint(10711));

  public static final NodeId OffNormalAlarmType_ShelvingState = new NodeId(UShort.MIN, uint(10712));

  public static final NodeId OffNormalAlarmType_ShelvingState_CurrentState =
      new NodeId(UShort.MIN, uint(10713));

  public static final NodeId OffNormalAlarmType_ShelvingState_CurrentState_Id =
      new NodeId(UShort.MIN, uint(10714));

  public static final NodeId OffNormalAlarmType_ShelvingState_CurrentState_Name =
      new NodeId(UShort.MIN, uint(10715));

  public static final NodeId OffNormalAlarmType_ShelvingState_CurrentState_Number =
      new NodeId(UShort.MIN, uint(10716));

  public static final NodeId OffNormalAlarmType_ShelvingState_CurrentState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(10717));

  public static final NodeId OffNormalAlarmType_ShelvingState_LastTransition =
      new NodeId(UShort.MIN, uint(10718));

  public static final NodeId OffNormalAlarmType_ShelvingState_LastTransition_Id =
      new NodeId(UShort.MIN, uint(10719));

  public static final NodeId OffNormalAlarmType_ShelvingState_LastTransition_Name =
      new NodeId(UShort.MIN, uint(10720));

  public static final NodeId OffNormalAlarmType_ShelvingState_LastTransition_Number =
      new NodeId(UShort.MIN, uint(10721));

  public static final NodeId OffNormalAlarmType_ShelvingState_LastTransition_TransitionTime =
      new NodeId(UShort.MIN, uint(10722));

  public static final NodeId OffNormalAlarmType_ShelvingState_UnshelveTime =
      new NodeId(UShort.MIN, uint(10723));

  public static final NodeId OffNormalAlarmType_ShelvingState_Unshelve =
      new NodeId(UShort.MIN, uint(10745));

  public static final NodeId OffNormalAlarmType_ShelvingState_OneShotShelve =
      new NodeId(UShort.MIN, uint(10746));

  public static final NodeId OffNormalAlarmType_ShelvingState_TimedShelve =
      new NodeId(UShort.MIN, uint(10747));

  public static final NodeId OffNormalAlarmType_ShelvingState_TimedShelve_InputArguments =
      new NodeId(UShort.MIN, uint(10748));

  public static final NodeId OffNormalAlarmType_SuppressedOrShelved =
      new NodeId(UShort.MIN, uint(10749));

  public static final NodeId OffNormalAlarmType_MaxTimeShelved =
      new NodeId(UShort.MIN, uint(10750));

  public static final NodeId TripAlarmType = new NodeId(UShort.MIN, uint(10751));

  public static final NodeId TripAlarmType_EventId = new NodeId(UShort.MIN, uint(10752));

  public static final NodeId TripAlarmType_EventType = new NodeId(UShort.MIN, uint(10753));

  public static final NodeId TripAlarmType_SourceNode = new NodeId(UShort.MIN, uint(10754));

  public static final NodeId TripAlarmType_SourceName = new NodeId(UShort.MIN, uint(10755));

  public static final NodeId TripAlarmType_Time = new NodeId(UShort.MIN, uint(10756));

  public static final NodeId TripAlarmType_ReceiveTime = new NodeId(UShort.MIN, uint(10757));

  public static final NodeId TripAlarmType_LocalTime = new NodeId(UShort.MIN, uint(10758));

  public static final NodeId TripAlarmType_Message = new NodeId(UShort.MIN, uint(10759));

  public static final NodeId TripAlarmType_Severity = new NodeId(UShort.MIN, uint(10760));

  public static final NodeId TripAlarmType_ConditionName = new NodeId(UShort.MIN, uint(10761));

  public static final NodeId TripAlarmType_BranchId = new NodeId(UShort.MIN, uint(10762));

  public static final NodeId TripAlarmType_Retain = new NodeId(UShort.MIN, uint(10763));

  public static final NodeId TripAlarmType_EnabledState = new NodeId(UShort.MIN, uint(10764));

  public static final NodeId TripAlarmType_EnabledState_Id = new NodeId(UShort.MIN, uint(10765));

  public static final NodeId TripAlarmType_EnabledState_Name = new NodeId(UShort.MIN, uint(10766));

  public static final NodeId TripAlarmType_EnabledState_Number =
      new NodeId(UShort.MIN, uint(10767));

  public static final NodeId TripAlarmType_EnabledState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(10768));

  public static final NodeId TripAlarmType_EnabledState_TransitionTime =
      new NodeId(UShort.MIN, uint(10769));

  public static final NodeId TripAlarmType_EnabledState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(10770));

  public static final NodeId TripAlarmType_EnabledState_TrueState =
      new NodeId(UShort.MIN, uint(10771));

  public static final NodeId TripAlarmType_EnabledState_FalseState =
      new NodeId(UShort.MIN, uint(10772));

  public static final NodeId TripAlarmType_Quality = new NodeId(UShort.MIN, uint(10773));

  public static final NodeId TripAlarmType_Quality_SourceTimestamp =
      new NodeId(UShort.MIN, uint(10774));

  public static final NodeId TripAlarmType_LastSeverity = new NodeId(UShort.MIN, uint(10775));

  public static final NodeId TripAlarmType_LastSeverity_SourceTimestamp =
      new NodeId(UShort.MIN, uint(10776));

  public static final NodeId TripAlarmType_Comment = new NodeId(UShort.MIN, uint(10777));

  public static final NodeId TripAlarmType_Comment_SourceTimestamp =
      new NodeId(UShort.MIN, uint(10778));

  public static final NodeId TripAlarmType_ClientUserId = new NodeId(UShort.MIN, uint(10779));

  public static final NodeId TripAlarmType_Enable = new NodeId(UShort.MIN, uint(10780));

  public static final NodeId TripAlarmType_Disable = new NodeId(UShort.MIN, uint(10781));

  public static final NodeId TripAlarmType_AddComment = new NodeId(UShort.MIN, uint(10782));

  public static final NodeId TripAlarmType_AddComment_InputArguments =
      new NodeId(UShort.MIN, uint(10783));

  public static final NodeId TripAlarmType_ConditionRefresh = new NodeId(UShort.MIN, uint(10784));

  public static final NodeId TripAlarmType_ConditionRefresh_InputArguments =
      new NodeId(UShort.MIN, uint(10785));

  public static final NodeId TripAlarmType_AckedState = new NodeId(UShort.MIN, uint(10786));

  public static final NodeId TripAlarmType_AckedState_Id = new NodeId(UShort.MIN, uint(10787));

  public static final NodeId TripAlarmType_AckedState_Name = new NodeId(UShort.MIN, uint(10788));

  public static final NodeId TripAlarmType_AckedState_Number = new NodeId(UShort.MIN, uint(10789));

  public static final NodeId TripAlarmType_AckedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(10790));

  public static final NodeId TripAlarmType_AckedState_TransitionTime =
      new NodeId(UShort.MIN, uint(10791));

  public static final NodeId TripAlarmType_AckedState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(10792));

  public static final NodeId TripAlarmType_AckedState_TrueState =
      new NodeId(UShort.MIN, uint(10793));

  public static final NodeId TripAlarmType_AckedState_FalseState =
      new NodeId(UShort.MIN, uint(10794));

  public static final NodeId TripAlarmType_ConfirmedState = new NodeId(UShort.MIN, uint(10795));

  public static final NodeId TripAlarmType_ConfirmedState_Id = new NodeId(UShort.MIN, uint(10796));

  public static final NodeId TripAlarmType_ConfirmedState_Name =
      new NodeId(UShort.MIN, uint(10797));

  public static final NodeId TripAlarmType_ConfirmedState_Number =
      new NodeId(UShort.MIN, uint(10798));

  public static final NodeId TripAlarmType_ConfirmedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(10799));

  public static final NodeId TripAlarmType_ConfirmedState_TransitionTime =
      new NodeId(UShort.MIN, uint(10800));

  public static final NodeId TripAlarmType_ConfirmedState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(10801));

  public static final NodeId TripAlarmType_ConfirmedState_TrueState =
      new NodeId(UShort.MIN, uint(10802));

  public static final NodeId TripAlarmType_ConfirmedState_FalseState =
      new NodeId(UShort.MIN, uint(10803));

  public static final NodeId TripAlarmType_Acknowledge = new NodeId(UShort.MIN, uint(10804));

  public static final NodeId TripAlarmType_Acknowledge_InputArguments =
      new NodeId(UShort.MIN, uint(10805));

  public static final NodeId TripAlarmType_Confirm = new NodeId(UShort.MIN, uint(10806));

  public static final NodeId TripAlarmType_Confirm_InputArguments =
      new NodeId(UShort.MIN, uint(10807));

  public static final NodeId TripAlarmType_ActiveState = new NodeId(UShort.MIN, uint(10808));

  public static final NodeId TripAlarmType_ActiveState_Id = new NodeId(UShort.MIN, uint(10809));

  public static final NodeId TripAlarmType_ActiveState_Name = new NodeId(UShort.MIN, uint(10810));

  public static final NodeId TripAlarmType_ActiveState_Number = new NodeId(UShort.MIN, uint(10811));

  public static final NodeId TripAlarmType_ActiveState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(10812));

  public static final NodeId TripAlarmType_ActiveState_TransitionTime =
      new NodeId(UShort.MIN, uint(10813));

  public static final NodeId TripAlarmType_ActiveState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(10814));

  public static final NodeId TripAlarmType_ActiveState_TrueState =
      new NodeId(UShort.MIN, uint(10815));

  public static final NodeId TripAlarmType_ActiveState_FalseState =
      new NodeId(UShort.MIN, uint(10816));

  public static final NodeId TripAlarmType_SuppressedState = new NodeId(UShort.MIN, uint(10817));

  public static final NodeId TripAlarmType_SuppressedState_Id = new NodeId(UShort.MIN, uint(10818));

  public static final NodeId TripAlarmType_SuppressedState_Name =
      new NodeId(UShort.MIN, uint(10819));

  public static final NodeId TripAlarmType_SuppressedState_Number =
      new NodeId(UShort.MIN, uint(10820));

  public static final NodeId TripAlarmType_SuppressedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(10821));

  public static final NodeId TripAlarmType_SuppressedState_TransitionTime =
      new NodeId(UShort.MIN, uint(10822));

  public static final NodeId TripAlarmType_SuppressedState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(10823));

  public static final NodeId TripAlarmType_SuppressedState_TrueState =
      new NodeId(UShort.MIN, uint(10824));

  public static final NodeId TripAlarmType_SuppressedState_FalseState =
      new NodeId(UShort.MIN, uint(10825));

  public static final NodeId TripAlarmType_ShelvingState = new NodeId(UShort.MIN, uint(10826));

  public static final NodeId TripAlarmType_ShelvingState_CurrentState =
      new NodeId(UShort.MIN, uint(10827));

  public static final NodeId TripAlarmType_ShelvingState_CurrentState_Id =
      new NodeId(UShort.MIN, uint(10828));

  public static final NodeId TripAlarmType_ShelvingState_CurrentState_Name =
      new NodeId(UShort.MIN, uint(10829));

  public static final NodeId TripAlarmType_ShelvingState_CurrentState_Number =
      new NodeId(UShort.MIN, uint(10830));

  public static final NodeId TripAlarmType_ShelvingState_CurrentState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(10831));

  public static final NodeId TripAlarmType_ShelvingState_LastTransition =
      new NodeId(UShort.MIN, uint(10832));

  public static final NodeId TripAlarmType_ShelvingState_LastTransition_Id =
      new NodeId(UShort.MIN, uint(10833));

  public static final NodeId TripAlarmType_ShelvingState_LastTransition_Name =
      new NodeId(UShort.MIN, uint(10834));

  public static final NodeId TripAlarmType_ShelvingState_LastTransition_Number =
      new NodeId(UShort.MIN, uint(10835));

  public static final NodeId TripAlarmType_ShelvingState_LastTransition_TransitionTime =
      new NodeId(UShort.MIN, uint(10836));

  public static final NodeId TripAlarmType_ShelvingState_UnshelveTime =
      new NodeId(UShort.MIN, uint(10837));

  public static final NodeId TripAlarmType_ShelvingState_Unshelve =
      new NodeId(UShort.MIN, uint(10859));

  public static final NodeId TripAlarmType_ShelvingState_OneShotShelve =
      new NodeId(UShort.MIN, uint(10860));

  public static final NodeId TripAlarmType_ShelvingState_TimedShelve =
      new NodeId(UShort.MIN, uint(10861));

  public static final NodeId TripAlarmType_ShelvingState_TimedShelve_InputArguments =
      new NodeId(UShort.MIN, uint(10862));

  public static final NodeId TripAlarmType_SuppressedOrShelved =
      new NodeId(UShort.MIN, uint(10863));

  public static final NodeId TripAlarmType_MaxTimeShelved = new NodeId(UShort.MIN, uint(10864));

  public static final NodeId AuditConditionShelvingEventType = new NodeId(UShort.MIN, uint(11093));

  public static final NodeId AuditConditionShelvingEventType_EventId =
      new NodeId(UShort.MIN, uint(11094));

  public static final NodeId AuditConditionShelvingEventType_EventType =
      new NodeId(UShort.MIN, uint(11095));

  public static final NodeId AuditConditionShelvingEventType_SourceNode =
      new NodeId(UShort.MIN, uint(11096));

  public static final NodeId AuditConditionShelvingEventType_SourceName =
      new NodeId(UShort.MIN, uint(11097));

  public static final NodeId AuditConditionShelvingEventType_Time =
      new NodeId(UShort.MIN, uint(11098));

  public static final NodeId AuditConditionShelvingEventType_ReceiveTime =
      new NodeId(UShort.MIN, uint(11099));

  public static final NodeId AuditConditionShelvingEventType_LocalTime =
      new NodeId(UShort.MIN, uint(11100));

  public static final NodeId AuditConditionShelvingEventType_Message =
      new NodeId(UShort.MIN, uint(11101));

  public static final NodeId AuditConditionShelvingEventType_Severity =
      new NodeId(UShort.MIN, uint(11102));

  public static final NodeId AuditConditionShelvingEventType_ActionTimeStamp =
      new NodeId(UShort.MIN, uint(11103));

  public static final NodeId AuditConditionShelvingEventType_Status =
      new NodeId(UShort.MIN, uint(11104));

  public static final NodeId AuditConditionShelvingEventType_ServerId =
      new NodeId(UShort.MIN, uint(11105));

  public static final NodeId AuditConditionShelvingEventType_ClientAuditEntryId =
      new NodeId(UShort.MIN, uint(11106));

  public static final NodeId AuditConditionShelvingEventType_ClientUserId =
      new NodeId(UShort.MIN, uint(11107));

  public static final NodeId AuditConditionShelvingEventType_MethodId =
      new NodeId(UShort.MIN, uint(11108));

  public static final NodeId AuditConditionShelvingEventType_InputArguments =
      new NodeId(UShort.MIN, uint(11109));

  public static final NodeId TwoStateVariableType_TrueState = new NodeId(UShort.MIN, uint(11110));

  public static final NodeId TwoStateVariableType_FalseState = new NodeId(UShort.MIN, uint(11111));

  public static final NodeId ConditionType_ConditionClassId = new NodeId(UShort.MIN, uint(11112));

  public static final NodeId ConditionType_ConditionClassName = new NodeId(UShort.MIN, uint(11113));

  public static final NodeId DialogConditionType_ConditionClassId =
      new NodeId(UShort.MIN, uint(11114));

  public static final NodeId DialogConditionType_ConditionClassName =
      new NodeId(UShort.MIN, uint(11115));

  public static final NodeId AcknowledgeableConditionType_ConditionClassId =
      new NodeId(UShort.MIN, uint(11116));

  public static final NodeId AcknowledgeableConditionType_ConditionClassName =
      new NodeId(UShort.MIN, uint(11117));

  public static final NodeId AlarmConditionType_ConditionClassId =
      new NodeId(UShort.MIN, uint(11118));

  public static final NodeId AlarmConditionType_ConditionClassName =
      new NodeId(UShort.MIN, uint(11119));

  public static final NodeId AlarmConditionType_InputNode = new NodeId(UShort.MIN, uint(11120));

  public static final NodeId LimitAlarmType_ConditionClassId = new NodeId(UShort.MIN, uint(11121));

  public static final NodeId LimitAlarmType_ConditionClassName =
      new NodeId(UShort.MIN, uint(11122));

  public static final NodeId LimitAlarmType_InputNode = new NodeId(UShort.MIN, uint(11123));

  public static final NodeId LimitAlarmType_HighHighLimit = new NodeId(UShort.MIN, uint(11124));

  public static final NodeId LimitAlarmType_HighLimit = new NodeId(UShort.MIN, uint(11125));

  public static final NodeId LimitAlarmType_LowLimit = new NodeId(UShort.MIN, uint(11126));

  public static final NodeId LimitAlarmType_LowLowLimit = new NodeId(UShort.MIN, uint(11127));

  public static final NodeId ExclusiveLimitAlarmType_ConditionClassId =
      new NodeId(UShort.MIN, uint(11128));

  public static final NodeId ExclusiveLimitAlarmType_ConditionClassName =
      new NodeId(UShort.MIN, uint(11129));

  public static final NodeId ExclusiveLimitAlarmType_InputNode =
      new NodeId(UShort.MIN, uint(11130));

  public static final NodeId ExclusiveLevelAlarmType_ConditionClassId =
      new NodeId(UShort.MIN, uint(11131));

  public static final NodeId ExclusiveLevelAlarmType_ConditionClassName =
      new NodeId(UShort.MIN, uint(11132));

  public static final NodeId ExclusiveLevelAlarmType_InputNode =
      new NodeId(UShort.MIN, uint(11133));

  public static final NodeId ExclusiveRateOfChangeAlarmType_ConditionClassId =
      new NodeId(UShort.MIN, uint(11134));

  public static final NodeId ExclusiveRateOfChangeAlarmType_ConditionClassName =
      new NodeId(UShort.MIN, uint(11135));

  public static final NodeId ExclusiveRateOfChangeAlarmType_InputNode =
      new NodeId(UShort.MIN, uint(11136));

  public static final NodeId ExclusiveDeviationAlarmType_ConditionClassId =
      new NodeId(UShort.MIN, uint(11137));

  public static final NodeId ExclusiveDeviationAlarmType_ConditionClassName =
      new NodeId(UShort.MIN, uint(11138));

  public static final NodeId ExclusiveDeviationAlarmType_InputNode =
      new NodeId(UShort.MIN, uint(11139));

  public static final NodeId NonExclusiveLimitAlarmType_ConditionClassId =
      new NodeId(UShort.MIN, uint(11140));

  public static final NodeId NonExclusiveLimitAlarmType_ConditionClassName =
      new NodeId(UShort.MIN, uint(11141));

  public static final NodeId NonExclusiveLimitAlarmType_InputNode =
      new NodeId(UShort.MIN, uint(11142));

  public static final NodeId NonExclusiveLevelAlarmType_ConditionClassId =
      new NodeId(UShort.MIN, uint(11143));

  public static final NodeId NonExclusiveLevelAlarmType_ConditionClassName =
      new NodeId(UShort.MIN, uint(11144));

  public static final NodeId NonExclusiveLevelAlarmType_InputNode =
      new NodeId(UShort.MIN, uint(11145));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_ConditionClassId =
      new NodeId(UShort.MIN, uint(11146));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_ConditionClassName =
      new NodeId(UShort.MIN, uint(11147));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_InputNode =
      new NodeId(UShort.MIN, uint(11148));

  public static final NodeId NonExclusiveDeviationAlarmType_ConditionClassId =
      new NodeId(UShort.MIN, uint(11149));

  public static final NodeId NonExclusiveDeviationAlarmType_ConditionClassName =
      new NodeId(UShort.MIN, uint(11150));

  public static final NodeId NonExclusiveDeviationAlarmType_InputNode =
      new NodeId(UShort.MIN, uint(11151));

  public static final NodeId DiscreteAlarmType_ConditionClassId =
      new NodeId(UShort.MIN, uint(11152));

  public static final NodeId DiscreteAlarmType_ConditionClassName =
      new NodeId(UShort.MIN, uint(11153));

  public static final NodeId DiscreteAlarmType_InputNode = new NodeId(UShort.MIN, uint(11154));

  public static final NodeId OffNormalAlarmType_ConditionClassId =
      new NodeId(UShort.MIN, uint(11155));

  public static final NodeId OffNormalAlarmType_ConditionClassName =
      new NodeId(UShort.MIN, uint(11156));

  public static final NodeId OffNormalAlarmType_InputNode = new NodeId(UShort.MIN, uint(11157));

  public static final NodeId OffNormalAlarmType_NormalState = new NodeId(UShort.MIN, uint(11158));

  public static final NodeId TripAlarmType_ConditionClassId = new NodeId(UShort.MIN, uint(11159));

  public static final NodeId TripAlarmType_ConditionClassName = new NodeId(UShort.MIN, uint(11160));

  public static final NodeId TripAlarmType_InputNode = new NodeId(UShort.MIN, uint(11161));

  public static final NodeId TripAlarmType_NormalState = new NodeId(UShort.MIN, uint(11162));

  public static final NodeId BaseConditionClassType = new NodeId(UShort.MIN, uint(11163));

  public static final NodeId ProcessConditionClassType = new NodeId(UShort.MIN, uint(11164));

  public static final NodeId MaintenanceConditionClassType = new NodeId(UShort.MIN, uint(11165));

  public static final NodeId SystemConditionClassType = new NodeId(UShort.MIN, uint(11166));

  public static final NodeId
      HistoricalDataConfigurationType_AggregateConfiguration_TreatUncertainAsBad =
          new NodeId(UShort.MIN, uint(11168));

  public static final NodeId HistoricalDataConfigurationType_AggregateConfiguration_PercentDataBad =
      new NodeId(UShort.MIN, uint(11169));

  public static final NodeId
      HistoricalDataConfigurationType_AggregateConfiguration_PercentDataGood =
          new NodeId(UShort.MIN, uint(11170));

  public static final NodeId
      HistoricalDataConfigurationType_AggregateConfiguration_UseSlopedExtrapolation =
          new NodeId(UShort.MIN, uint(11171));

  public static final NodeId HistoryServerCapabilitiesType_AggregateFunctions =
      new NodeId(UShort.MIN, uint(11172));

  public static final NodeId AggregateConfigurationType = new NodeId(UShort.MIN, uint(11187));

  public static final NodeId AggregateConfigurationType_TreatUncertainAsBad =
      new NodeId(UShort.MIN, uint(11188));

  public static final NodeId AggregateConfigurationType_PercentDataBad =
      new NodeId(UShort.MIN, uint(11189));

  public static final NodeId AggregateConfigurationType_PercentDataGood =
      new NodeId(UShort.MIN, uint(11190));

  public static final NodeId AggregateConfigurationType_UseSlopedExtrapolation =
      new NodeId(UShort.MIN, uint(11191));

  public static final NodeId HistoryServerCapabilities = new NodeId(UShort.MIN, uint(11192));

  public static final NodeId HistoryServerCapabilities_AccessHistoryDataCapability =
      new NodeId(UShort.MIN, uint(11193));

  public static final NodeId HistoryServerCapabilities_InsertDataCapability =
      new NodeId(UShort.MIN, uint(11196));

  public static final NodeId HistoryServerCapabilities_ReplaceDataCapability =
      new NodeId(UShort.MIN, uint(11197));

  public static final NodeId HistoryServerCapabilities_UpdateDataCapability =
      new NodeId(UShort.MIN, uint(11198));

  public static final NodeId HistoryServerCapabilities_DeleteRawCapability =
      new NodeId(UShort.MIN, uint(11199));

  public static final NodeId HistoryServerCapabilities_DeleteAtTimeCapability =
      new NodeId(UShort.MIN, uint(11200));

  public static final NodeId HistoryServerCapabilities_AggregateFunctions =
      new NodeId(UShort.MIN, uint(11201));

  public static final NodeId HAConfiguration = new NodeId(UShort.MIN, uint(11202));

  public static final NodeId HAConfiguration_AggregateConfiguration =
      new NodeId(UShort.MIN, uint(11203));

  public static final NodeId HAConfiguration_AggregateConfiguration_TreatUncertainAsBad =
      new NodeId(UShort.MIN, uint(11204));

  public static final NodeId HAConfiguration_AggregateConfiguration_PercentDataBad =
      new NodeId(UShort.MIN, uint(11205));

  public static final NodeId HAConfiguration_AggregateConfiguration_PercentDataGood =
      new NodeId(UShort.MIN, uint(11206));

  public static final NodeId HAConfiguration_AggregateConfiguration_UseSlopedExtrapolation =
      new NodeId(UShort.MIN, uint(11207));

  public static final NodeId HAConfiguration_Stepped = new NodeId(UShort.MIN, uint(11208));

  public static final NodeId HAConfiguration_Definition = new NodeId(UShort.MIN, uint(11209));

  public static final NodeId HAConfiguration_MaxTimeInterval = new NodeId(UShort.MIN, uint(11210));

  public static final NodeId HAConfiguration_MinTimeInterval = new NodeId(UShort.MIN, uint(11211));

  public static final NodeId HAConfiguration_ExceptionDeviation =
      new NodeId(UShort.MIN, uint(11212));

  public static final NodeId HAConfiguration_ExceptionDeviationFormat =
      new NodeId(UShort.MIN, uint(11213));

  public static final NodeId Annotations = new NodeId(UShort.MIN, uint(11214));

  public static final NodeId HistoricalEventFilter = new NodeId(UShort.MIN, uint(11215));

  public static final NodeId ModificationInfo = new NodeId(UShort.MIN, uint(11216));

  public static final NodeId HistoryModifiedData = new NodeId(UShort.MIN, uint(11217));

  public static final NodeId ModificationInfo_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(11218));

  public static final NodeId HistoryModifiedData_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(11219));

  public static final NodeId ModificationInfo_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(11226));

  public static final NodeId HistoryModifiedData_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(11227));

  public static final NodeId HistoryUpdateType = new NodeId(UShort.MIN, uint(11234));

  public static final NodeId MultiStateValueDiscreteType = new NodeId(UShort.MIN, uint(11238));

  public static final NodeId MultiStateValueDiscreteType_Definition =
      new NodeId(UShort.MIN, uint(11239));

  public static final NodeId MultiStateValueDiscreteType_ValuePrecision =
      new NodeId(UShort.MIN, uint(11240));

  public static final NodeId MultiStateValueDiscreteType_EnumValues =
      new NodeId(UShort.MIN, uint(11241));

  public static final NodeId HistoryServerCapabilities_AccessHistoryEventsCapability =
      new NodeId(UShort.MIN, uint(11242));

  public static final NodeId HistoryServerCapabilitiesType_MaxReturnDataValues =
      new NodeId(UShort.MIN, uint(11268));

  public static final NodeId HistoryServerCapabilitiesType_MaxReturnEventValues =
      new NodeId(UShort.MIN, uint(11269));

  public static final NodeId HistoryServerCapabilitiesType_InsertAnnotationCapability =
      new NodeId(UShort.MIN, uint(11270));

  public static final NodeId HistoryServerCapabilities_MaxReturnDataValues =
      new NodeId(UShort.MIN, uint(11273));

  public static final NodeId HistoryServerCapabilities_MaxReturnEventValues =
      new NodeId(UShort.MIN, uint(11274));

  public static final NodeId HistoryServerCapabilities_InsertAnnotationCapability =
      new NodeId(UShort.MIN, uint(11275));

  public static final NodeId HistoryServerCapabilitiesType_InsertEventCapability =
      new NodeId(UShort.MIN, uint(11278));

  public static final NodeId HistoryServerCapabilitiesType_ReplaceEventCapability =
      new NodeId(UShort.MIN, uint(11279));

  public static final NodeId HistoryServerCapabilitiesType_UpdateEventCapability =
      new NodeId(UShort.MIN, uint(11280));

  public static final NodeId HistoryServerCapabilities_InsertEventCapability =
      new NodeId(UShort.MIN, uint(11281));

  public static final NodeId HistoryServerCapabilities_ReplaceEventCapability =
      new NodeId(UShort.MIN, uint(11282));

  public static final NodeId HistoryServerCapabilities_UpdateEventCapability =
      new NodeId(UShort.MIN, uint(11283));

  public static final NodeId AggregateFunction_TimeAverage2 = new NodeId(UShort.MIN, uint(11285));

  public static final NodeId AggregateFunction_Minimum2 = new NodeId(UShort.MIN, uint(11286));

  public static final NodeId AggregateFunction_Maximum2 = new NodeId(UShort.MIN, uint(11287));

  public static final NodeId AggregateFunction_Range2 = new NodeId(UShort.MIN, uint(11288));

  public static final NodeId AggregateFunction_WorstQuality2 = new NodeId(UShort.MIN, uint(11292));

  public static final NodeId PerformUpdateType = new NodeId(UShort.MIN, uint(11293));

  public static final NodeId UpdateStructureDataDetails = new NodeId(UShort.MIN, uint(11295));

  public static final NodeId UpdateStructureDataDetails_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(11296));

  public static final NodeId UpdateStructureDataDetails_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(11300));

  public static final NodeId AggregateFunction_Total2 = new NodeId(UShort.MIN, uint(11304));

  public static final NodeId AggregateFunction_MinimumActualTime2 =
      new NodeId(UShort.MIN, uint(11305));

  public static final NodeId AggregateFunction_MaximumActualTime2 =
      new NodeId(UShort.MIN, uint(11306));

  public static final NodeId AggregateFunction_DurationInStateZero =
      new NodeId(UShort.MIN, uint(11307));

  public static final NodeId AggregateFunction_DurationInStateNonZero =
      new NodeId(UShort.MIN, uint(11308));

  public static final NodeId Server_ServerRedundancy_CurrentServerId =
      new NodeId(UShort.MIN, uint(11312));

  public static final NodeId Server_ServerRedundancy_RedundantServerArray =
      new NodeId(UShort.MIN, uint(11313));

  public static final NodeId Server_ServerRedundancy_ServerUriArray =
      new NodeId(UShort.MIN, uint(11314));

  public static final NodeId ShelvedStateMachineType_UnshelvedToTimedShelved_TransitionNumber =
      new NodeId(UShort.MIN, uint(11322));

  public static final NodeId ShelvedStateMachineType_UnshelvedToOneShotShelved_TransitionNumber =
      new NodeId(UShort.MIN, uint(11323));

  public static final NodeId ShelvedStateMachineType_TimedShelvedToUnshelved_TransitionNumber =
      new NodeId(UShort.MIN, uint(11324));

  public static final NodeId ShelvedStateMachineType_TimedShelvedToOneShotShelved_TransitionNumber =
      new NodeId(UShort.MIN, uint(11325));

  public static final NodeId ShelvedStateMachineType_OneShotShelvedToUnshelved_TransitionNumber =
      new NodeId(UShort.MIN, uint(11326));

  public static final NodeId ShelvedStateMachineType_OneShotShelvedToTimedShelved_TransitionNumber =
      new NodeId(UShort.MIN, uint(11327));

  public static final NodeId ExclusiveLimitStateMachineType_LowLowToLow_TransitionNumber =
      new NodeId(UShort.MIN, uint(11340));

  public static final NodeId ExclusiveLimitStateMachineType_LowToLowLow_TransitionNumber =
      new NodeId(UShort.MIN, uint(11341));

  public static final NodeId ExclusiveLimitStateMachineType_HighHighToHigh_TransitionNumber =
      new NodeId(UShort.MIN, uint(11342));

  public static final NodeId ExclusiveLimitStateMachineType_HighToHighHigh_TransitionNumber =
      new NodeId(UShort.MIN, uint(11343));

  public static final NodeId AggregateFunction_StandardDeviationSample =
      new NodeId(UShort.MIN, uint(11426));

  public static final NodeId AggregateFunction_StandardDeviationPopulation =
      new NodeId(UShort.MIN, uint(11427));

  public static final NodeId AggregateFunction_VarianceSample = new NodeId(UShort.MIN, uint(11428));

  public static final NodeId AggregateFunction_VariancePopulation =
      new NodeId(UShort.MIN, uint(11429));

  public static final NodeId EnumStrings = new NodeId(UShort.MIN, uint(11432));

  public static final NodeId ValueAsText = new NodeId(UShort.MIN, uint(11433));

  public static final NodeId ProgressEventType = new NodeId(UShort.MIN, uint(11436));

  public static final NodeId ProgressEventType_EventId = new NodeId(UShort.MIN, uint(11437));

  public static final NodeId ProgressEventType_EventType = new NodeId(UShort.MIN, uint(11438));

  public static final NodeId ProgressEventType_SourceNode = new NodeId(UShort.MIN, uint(11439));

  public static final NodeId ProgressEventType_SourceName = new NodeId(UShort.MIN, uint(11440));

  public static final NodeId ProgressEventType_Time = new NodeId(UShort.MIN, uint(11441));

  public static final NodeId ProgressEventType_ReceiveTime = new NodeId(UShort.MIN, uint(11442));

  public static final NodeId ProgressEventType_LocalTime = new NodeId(UShort.MIN, uint(11443));

  public static final NodeId ProgressEventType_Message = new NodeId(UShort.MIN, uint(11444));

  public static final NodeId ProgressEventType_Severity = new NodeId(UShort.MIN, uint(11445));

  public static final NodeId SystemStatusChangeEventType = new NodeId(UShort.MIN, uint(11446));

  public static final NodeId SystemStatusChangeEventType_EventId =
      new NodeId(UShort.MIN, uint(11447));

  public static final NodeId SystemStatusChangeEventType_EventType =
      new NodeId(UShort.MIN, uint(11448));

  public static final NodeId SystemStatusChangeEventType_SourceNode =
      new NodeId(UShort.MIN, uint(11449));

  public static final NodeId SystemStatusChangeEventType_SourceName =
      new NodeId(UShort.MIN, uint(11450));

  public static final NodeId SystemStatusChangeEventType_Time = new NodeId(UShort.MIN, uint(11451));

  public static final NodeId SystemStatusChangeEventType_ReceiveTime =
      new NodeId(UShort.MIN, uint(11452));

  public static final NodeId SystemStatusChangeEventType_LocalTime =
      new NodeId(UShort.MIN, uint(11453));

  public static final NodeId SystemStatusChangeEventType_Message =
      new NodeId(UShort.MIN, uint(11454));

  public static final NodeId SystemStatusChangeEventType_Severity =
      new NodeId(UShort.MIN, uint(11455));

  public static final NodeId TransitionVariableType_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(11456));

  public static final NodeId FiniteTransitionVariableType_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(11457));

  public static final NodeId StateMachineType_LastTransition_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(11458));

  public static final NodeId FiniteStateMachineType_LastTransition_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(11459));

  public static final NodeId TransitionEventType_Transition_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(11460));

  public static final NodeId MultiStateValueDiscreteType_ValueAsText =
      new NodeId(UShort.MIN, uint(11461));

  public static final NodeId ProgramTransitionEventType_Transition_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(11462));

  public static final NodeId ProgramTransitionAuditEventType_Transition_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(11463));

  public static final NodeId ProgramStateMachineType_LastTransition_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(11464));

  public static final NodeId ShelvedStateMachineType_LastTransition_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(11465));

  public static final NodeId
      AlarmConditionType_ShelvingState_LastTransition_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(11466));

  public static final NodeId LimitAlarmType_ShelvingState_LastTransition_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(11467));

  public static final NodeId ExclusiveLimitStateMachineType_LastTransition_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(11468));

  public static final NodeId
      ExclusiveLimitAlarmType_ShelvingState_LastTransition_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(11469));

  public static final NodeId
      ExclusiveLimitAlarmType_LimitState_LastTransition_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(11470));

  public static final NodeId
      ExclusiveLevelAlarmType_ShelvingState_LastTransition_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(11471));

  public static final NodeId
      ExclusiveLevelAlarmType_LimitState_LastTransition_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(11472));

  public static final NodeId
      ExclusiveRateOfChangeAlarmType_ShelvingState_LastTransition_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(11473));

  public static final NodeId
      ExclusiveRateOfChangeAlarmType_LimitState_LastTransition_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(11474));

  public static final NodeId
      ExclusiveDeviationAlarmType_ShelvingState_LastTransition_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(11475));

  public static final NodeId
      ExclusiveDeviationAlarmType_LimitState_LastTransition_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(11476));

  public static final NodeId
      NonExclusiveLimitAlarmType_ShelvingState_LastTransition_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(11477));

  public static final NodeId
      NonExclusiveLevelAlarmType_ShelvingState_LastTransition_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(11478));

  public static final NodeId
      NonExclusiveRateOfChangeAlarmType_ShelvingState_LastTransition_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(11479));

  public static final NodeId
      NonExclusiveDeviationAlarmType_ShelvingState_LastTransition_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(11480));

  public static final NodeId
      DiscreteAlarmType_ShelvingState_LastTransition_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(11481));

  public static final NodeId
      OffNormalAlarmType_ShelvingState_LastTransition_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(11482));

  public static final NodeId TripAlarmType_ShelvingState_LastTransition_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(11483));

  public static final NodeId AuditActivateSessionEventType_SecureChannelId =
      new NodeId(UShort.MIN, uint(11485));

  public static final NodeId OptionSetType = new NodeId(UShort.MIN, uint(11487));

  public static final NodeId OptionSetType_OptionSetValues = new NodeId(UShort.MIN, uint(11488));

  public static final NodeId ServerType_GetMonitoredItems = new NodeId(UShort.MIN, uint(11489));

  public static final NodeId ServerType_GetMonitoredItems_InputArguments =
      new NodeId(UShort.MIN, uint(11490));

  public static final NodeId ServerType_GetMonitoredItems_OutputArguments =
      new NodeId(UShort.MIN, uint(11491));

  public static final NodeId Server_GetMonitoredItems = new NodeId(UShort.MIN, uint(11492));

  public static final NodeId Server_GetMonitoredItems_InputArguments =
      new NodeId(UShort.MIN, uint(11493));

  public static final NodeId Server_GetMonitoredItems_OutputArguments =
      new NodeId(UShort.MIN, uint(11494));

  public static final NodeId GetMonitoredItemsMethodType = new NodeId(UShort.MIN, uint(11495));

  public static final NodeId GetMonitoredItemsMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(11496));

  public static final NodeId GetMonitoredItemsMethodType_OutputArguments =
      new NodeId(UShort.MIN, uint(11497));

  public static final NodeId MaxStringLength = new NodeId(UShort.MIN, uint(11498));

  public static final NodeId HistoricalDataConfigurationType_StartOfArchive =
      new NodeId(UShort.MIN, uint(11499));

  public static final NodeId HistoricalDataConfigurationType_StartOfOnlineArchive =
      new NodeId(UShort.MIN, uint(11500));

  public static final NodeId HistoryServerCapabilitiesType_DeleteEventCapability =
      new NodeId(UShort.MIN, uint(11501));

  public static final NodeId HistoryServerCapabilities_DeleteEventCapability =
      new NodeId(UShort.MIN, uint(11502));

  public static final NodeId HAConfiguration_StartOfArchive = new NodeId(UShort.MIN, uint(11503));

  public static final NodeId HAConfiguration_StartOfOnlineArchive =
      new NodeId(UShort.MIN, uint(11504));

  public static final NodeId AggregateFunction_StartBound = new NodeId(UShort.MIN, uint(11505));

  public static final NodeId AggregateFunction_EndBound = new NodeId(UShort.MIN, uint(11506));

  public static final NodeId AggregateFunction_DeltaBounds = new NodeId(UShort.MIN, uint(11507));

  public static final NodeId ModellingRule_OptionalPlaceholder =
      new NodeId(UShort.MIN, uint(11508));

  public static final NodeId ModellingRule_MandatoryPlaceholder =
      new NodeId(UShort.MIN, uint(11510));

  public static final NodeId MaxArrayLength = new NodeId(UShort.MIN, uint(11512));

  public static final NodeId EngineeringUnits = new NodeId(UShort.MIN, uint(11513));

  public static final NodeId ServerType_ServerCapabilities_MaxArrayLength =
      new NodeId(UShort.MIN, uint(11514));

  public static final NodeId ServerType_ServerCapabilities_MaxStringLength =
      new NodeId(UShort.MIN, uint(11515));

  public static final NodeId ServerType_ServerCapabilities_OperationLimits =
      new NodeId(UShort.MIN, uint(11516));

  public static final NodeId ServerType_ServerCapabilities_OperationLimits_MaxNodesPerRead =
      new NodeId(UShort.MIN, uint(11517));

  public static final NodeId ServerType_ServerCapabilities_OperationLimits_MaxNodesPerWrite =
      new NodeId(UShort.MIN, uint(11519));

  public static final NodeId ServerType_ServerCapabilities_OperationLimits_MaxNodesPerMethodCall =
      new NodeId(UShort.MIN, uint(11521));

  public static final NodeId ServerType_ServerCapabilities_OperationLimits_MaxNodesPerBrowse =
      new NodeId(UShort.MIN, uint(11522));

  public static final NodeId
      ServerType_ServerCapabilities_OperationLimits_MaxNodesPerRegisterNodes =
          new NodeId(UShort.MIN, uint(11523));

  public static final NodeId
      ServerType_ServerCapabilities_OperationLimits_MaxNodesPerTranslateBrowsePathsToNodeIds =
          new NodeId(UShort.MIN, uint(11524));

  public static final NodeId
      ServerType_ServerCapabilities_OperationLimits_MaxNodesPerNodeManagement =
          new NodeId(UShort.MIN, uint(11525));

  public static final NodeId
      ServerType_ServerCapabilities_OperationLimits_MaxMonitoredItemsPerCall =
          new NodeId(UShort.MIN, uint(11526));

  public static final NodeId ServerType_Namespaces = new NodeId(UShort.MIN, uint(11527));

  public static final NodeId ServerCapabilitiesType_MaxArrayLength =
      new NodeId(UShort.MIN, uint(11549));

  public static final NodeId ServerCapabilitiesType_MaxStringLength =
      new NodeId(UShort.MIN, uint(11550));

  public static final NodeId ServerCapabilitiesType_OperationLimits =
      new NodeId(UShort.MIN, uint(11551));

  public static final NodeId ServerCapabilitiesType_OperationLimits_MaxNodesPerRead =
      new NodeId(UShort.MIN, uint(11552));

  public static final NodeId ServerCapabilitiesType_OperationLimits_MaxNodesPerWrite =
      new NodeId(UShort.MIN, uint(11554));

  public static final NodeId ServerCapabilitiesType_OperationLimits_MaxNodesPerMethodCall =
      new NodeId(UShort.MIN, uint(11556));

  public static final NodeId ServerCapabilitiesType_OperationLimits_MaxNodesPerBrowse =
      new NodeId(UShort.MIN, uint(11557));

  public static final NodeId ServerCapabilitiesType_OperationLimits_MaxNodesPerRegisterNodes =
      new NodeId(UShort.MIN, uint(11558));

  public static final NodeId
      ServerCapabilitiesType_OperationLimits_MaxNodesPerTranslateBrowsePathsToNodeIds =
          new NodeId(UShort.MIN, uint(11559));

  public static final NodeId ServerCapabilitiesType_OperationLimits_MaxNodesPerNodeManagement =
      new NodeId(UShort.MIN, uint(11560));

  public static final NodeId ServerCapabilitiesType_OperationLimits_MaxMonitoredItemsPerCall =
      new NodeId(UShort.MIN, uint(11561));

  public static final NodeId ServerCapabilitiesType_VendorCapability_Placeholder =
      new NodeId(UShort.MIN, uint(11562));

  public static final NodeId OperationLimitsType = new NodeId(UShort.MIN, uint(11564));

  public static final NodeId OperationLimitsType_MaxNodesPerRead =
      new NodeId(UShort.MIN, uint(11565));

  public static final NodeId OperationLimitsType_MaxNodesPerWrite =
      new NodeId(UShort.MIN, uint(11567));

  public static final NodeId OperationLimitsType_MaxNodesPerMethodCall =
      new NodeId(UShort.MIN, uint(11569));

  public static final NodeId OperationLimitsType_MaxNodesPerBrowse =
      new NodeId(UShort.MIN, uint(11570));

  public static final NodeId OperationLimitsType_MaxNodesPerRegisterNodes =
      new NodeId(UShort.MIN, uint(11571));

  public static final NodeId OperationLimitsType_MaxNodesPerTranslateBrowsePathsToNodeIds =
      new NodeId(UShort.MIN, uint(11572));

  public static final NodeId OperationLimitsType_MaxNodesPerNodeManagement =
      new NodeId(UShort.MIN, uint(11573));

  public static final NodeId OperationLimitsType_MaxMonitoredItemsPerCall =
      new NodeId(UShort.MIN, uint(11574));

  public static final NodeId FileType = new NodeId(UShort.MIN, uint(11575));

  public static final NodeId FileType_Size = new NodeId(UShort.MIN, uint(11576));

  public static final NodeId FileType_OpenCount = new NodeId(UShort.MIN, uint(11579));

  public static final NodeId FileType_Open = new NodeId(UShort.MIN, uint(11580));

  public static final NodeId FileType_Open_InputArguments = new NodeId(UShort.MIN, uint(11581));

  public static final NodeId FileType_Open_OutputArguments = new NodeId(UShort.MIN, uint(11582));

  public static final NodeId FileType_Close = new NodeId(UShort.MIN, uint(11583));

  public static final NodeId FileType_Close_InputArguments = new NodeId(UShort.MIN, uint(11584));

  public static final NodeId FileType_Read = new NodeId(UShort.MIN, uint(11585));

  public static final NodeId FileType_Read_InputArguments = new NodeId(UShort.MIN, uint(11586));

  public static final NodeId FileType_Read_OutputArguments = new NodeId(UShort.MIN, uint(11587));

  public static final NodeId FileType_Write = new NodeId(UShort.MIN, uint(11588));

  public static final NodeId FileType_Write_InputArguments = new NodeId(UShort.MIN, uint(11589));

  public static final NodeId FileType_GetPosition = new NodeId(UShort.MIN, uint(11590));

  public static final NodeId FileType_GetPosition_InputArguments =
      new NodeId(UShort.MIN, uint(11591));

  public static final NodeId FileType_GetPosition_OutputArguments =
      new NodeId(UShort.MIN, uint(11592));

  public static final NodeId FileType_SetPosition = new NodeId(UShort.MIN, uint(11593));

  public static final NodeId FileType_SetPosition_InputArguments =
      new NodeId(UShort.MIN, uint(11594));

  public static final NodeId AddressSpaceFileType = new NodeId(UShort.MIN, uint(11595));

  public static final NodeId AddressSpaceFileType_Size = new NodeId(UShort.MIN, uint(11596));

  public static final NodeId AddressSpaceFileType_OpenCount = new NodeId(UShort.MIN, uint(11599));

  public static final NodeId AddressSpaceFileType_Open = new NodeId(UShort.MIN, uint(11600));

  public static final NodeId AddressSpaceFileType_Open_InputArguments =
      new NodeId(UShort.MIN, uint(11601));

  public static final NodeId AddressSpaceFileType_Open_OutputArguments =
      new NodeId(UShort.MIN, uint(11602));

  public static final NodeId AddressSpaceFileType_Close = new NodeId(UShort.MIN, uint(11603));

  public static final NodeId AddressSpaceFileType_Close_InputArguments =
      new NodeId(UShort.MIN, uint(11604));

  public static final NodeId AddressSpaceFileType_Read = new NodeId(UShort.MIN, uint(11605));

  public static final NodeId AddressSpaceFileType_Read_InputArguments =
      new NodeId(UShort.MIN, uint(11606));

  public static final NodeId AddressSpaceFileType_Read_OutputArguments =
      new NodeId(UShort.MIN, uint(11607));

  public static final NodeId AddressSpaceFileType_Write = new NodeId(UShort.MIN, uint(11608));

  public static final NodeId AddressSpaceFileType_Write_InputArguments =
      new NodeId(UShort.MIN, uint(11609));

  public static final NodeId AddressSpaceFileType_GetPosition = new NodeId(UShort.MIN, uint(11610));

  public static final NodeId AddressSpaceFileType_GetPosition_InputArguments =
      new NodeId(UShort.MIN, uint(11611));

  public static final NodeId AddressSpaceFileType_GetPosition_OutputArguments =
      new NodeId(UShort.MIN, uint(11612));

  public static final NodeId AddressSpaceFileType_SetPosition = new NodeId(UShort.MIN, uint(11613));

  public static final NodeId AddressSpaceFileType_SetPosition_InputArguments =
      new NodeId(UShort.MIN, uint(11614));

  public static final NodeId AddressSpaceFileType_ExportNamespace =
      new NodeId(UShort.MIN, uint(11615));

  public static final NodeId NamespaceMetadataType = new NodeId(UShort.MIN, uint(11616));

  public static final NodeId NamespaceMetadataType_NamespaceUri =
      new NodeId(UShort.MIN, uint(11617));

  public static final NodeId NamespaceMetadataType_NamespaceVersion =
      new NodeId(UShort.MIN, uint(11618));

  public static final NodeId NamespaceMetadataType_NamespacePublicationDate =
      new NodeId(UShort.MIN, uint(11619));

  public static final NodeId NamespaceMetadataType_IsNamespaceSubset =
      new NodeId(UShort.MIN, uint(11620));

  public static final NodeId NamespaceMetadataType_StaticNodeIdTypes =
      new NodeId(UShort.MIN, uint(11621));

  public static final NodeId NamespaceMetadataType_StaticNumericNodeIdRange =
      new NodeId(UShort.MIN, uint(11622));

  public static final NodeId NamespaceMetadataType_StaticStringNodeIdPattern =
      new NodeId(UShort.MIN, uint(11623));

  public static final NodeId NamespaceMetadataType_NamespaceFile =
      new NodeId(UShort.MIN, uint(11624));

  public static final NodeId NamespaceMetadataType_NamespaceFile_Size =
      new NodeId(UShort.MIN, uint(11625));

  public static final NodeId NamespaceMetadataType_NamespaceFile_OpenCount =
      new NodeId(UShort.MIN, uint(11628));

  public static final NodeId NamespaceMetadataType_NamespaceFile_Open =
      new NodeId(UShort.MIN, uint(11629));

  public static final NodeId NamespaceMetadataType_NamespaceFile_Open_InputArguments =
      new NodeId(UShort.MIN, uint(11630));

  public static final NodeId NamespaceMetadataType_NamespaceFile_Open_OutputArguments =
      new NodeId(UShort.MIN, uint(11631));

  public static final NodeId NamespaceMetadataType_NamespaceFile_Close =
      new NodeId(UShort.MIN, uint(11632));

  public static final NodeId NamespaceMetadataType_NamespaceFile_Close_InputArguments =
      new NodeId(UShort.MIN, uint(11633));

  public static final NodeId NamespaceMetadataType_NamespaceFile_Read =
      new NodeId(UShort.MIN, uint(11634));

  public static final NodeId NamespaceMetadataType_NamespaceFile_Read_InputArguments =
      new NodeId(UShort.MIN, uint(11635));

  public static final NodeId NamespaceMetadataType_NamespaceFile_Read_OutputArguments =
      new NodeId(UShort.MIN, uint(11636));

  public static final NodeId NamespaceMetadataType_NamespaceFile_Write =
      new NodeId(UShort.MIN, uint(11637));

  public static final NodeId NamespaceMetadataType_NamespaceFile_Write_InputArguments =
      new NodeId(UShort.MIN, uint(11638));

  public static final NodeId NamespaceMetadataType_NamespaceFile_GetPosition =
      new NodeId(UShort.MIN, uint(11639));

  public static final NodeId NamespaceMetadataType_NamespaceFile_GetPosition_InputArguments =
      new NodeId(UShort.MIN, uint(11640));

  public static final NodeId NamespaceMetadataType_NamespaceFile_GetPosition_OutputArguments =
      new NodeId(UShort.MIN, uint(11641));

  public static final NodeId NamespaceMetadataType_NamespaceFile_SetPosition =
      new NodeId(UShort.MIN, uint(11642));

  public static final NodeId NamespaceMetadataType_NamespaceFile_SetPosition_InputArguments =
      new NodeId(UShort.MIN, uint(11643));

  public static final NodeId NamespaceMetadataType_NamespaceFile_ExportNamespace =
      new NodeId(UShort.MIN, uint(11644));

  public static final NodeId NamespacesType = new NodeId(UShort.MIN, uint(11645));

  public static final NodeId NamespacesType_NamespaceIdentifier_Placeholder =
      new NodeId(UShort.MIN, uint(11646));

  public static final NodeId NamespacesType_NamespaceIdentifier_Placeholder_NamespaceUri =
      new NodeId(UShort.MIN, uint(11647));

  public static final NodeId NamespacesType_NamespaceIdentifier_Placeholder_NamespaceVersion =
      new NodeId(UShort.MIN, uint(11648));

  public static final NodeId
      NamespacesType_NamespaceIdentifier_Placeholder_NamespacePublicationDate =
          new NodeId(UShort.MIN, uint(11649));

  public static final NodeId NamespacesType_NamespaceIdentifier_Placeholder_IsNamespaceSubset =
      new NodeId(UShort.MIN, uint(11650));

  public static final NodeId NamespacesType_NamespaceIdentifier_Placeholder_StaticNodeIdTypes =
      new NodeId(UShort.MIN, uint(11651));

  public static final NodeId
      NamespacesType_NamespaceIdentifier_Placeholder_StaticNumericNodeIdRange =
          new NodeId(UShort.MIN, uint(11652));

  public static final NodeId
      NamespacesType_NamespaceIdentifier_Placeholder_StaticStringNodeIdPattern =
          new NodeId(UShort.MIN, uint(11653));

  public static final NodeId NamespacesType_NamespaceIdentifier_Placeholder_NamespaceFile =
      new NodeId(UShort.MIN, uint(11654));

  public static final NodeId NamespacesType_NamespaceIdentifier_Placeholder_NamespaceFile_Size =
      new NodeId(UShort.MIN, uint(11655));

  public static final NodeId
      NamespacesType_NamespaceIdentifier_Placeholder_NamespaceFile_OpenCount =
          new NodeId(UShort.MIN, uint(11658));

  public static final NodeId NamespacesType_NamespaceIdentifier_Placeholder_NamespaceFile_Open =
      new NodeId(UShort.MIN, uint(11659));

  public static final NodeId
      NamespacesType_NamespaceIdentifier_Placeholder_NamespaceFile_Open_InputArguments =
          new NodeId(UShort.MIN, uint(11660));

  public static final NodeId
      NamespacesType_NamespaceIdentifier_Placeholder_NamespaceFile_Open_OutputArguments =
          new NodeId(UShort.MIN, uint(11661));

  public static final NodeId NamespacesType_NamespaceIdentifier_Placeholder_NamespaceFile_Close =
      new NodeId(UShort.MIN, uint(11662));

  public static final NodeId
      NamespacesType_NamespaceIdentifier_Placeholder_NamespaceFile_Close_InputArguments =
          new NodeId(UShort.MIN, uint(11663));

  public static final NodeId NamespacesType_NamespaceIdentifier_Placeholder_NamespaceFile_Read =
      new NodeId(UShort.MIN, uint(11664));

  public static final NodeId
      NamespacesType_NamespaceIdentifier_Placeholder_NamespaceFile_Read_InputArguments =
          new NodeId(UShort.MIN, uint(11665));

  public static final NodeId
      NamespacesType_NamespaceIdentifier_Placeholder_NamespaceFile_Read_OutputArguments =
          new NodeId(UShort.MIN, uint(11666));

  public static final NodeId NamespacesType_NamespaceIdentifier_Placeholder_NamespaceFile_Write =
      new NodeId(UShort.MIN, uint(11667));

  public static final NodeId
      NamespacesType_NamespaceIdentifier_Placeholder_NamespaceFile_Write_InputArguments =
          new NodeId(UShort.MIN, uint(11668));

  public static final NodeId
      NamespacesType_NamespaceIdentifier_Placeholder_NamespaceFile_GetPosition =
          new NodeId(UShort.MIN, uint(11669));

  public static final NodeId
      NamespacesType_NamespaceIdentifier_Placeholder_NamespaceFile_GetPosition_InputArguments =
          new NodeId(UShort.MIN, uint(11670));

  public static final NodeId
      NamespacesType_NamespaceIdentifier_Placeholder_NamespaceFile_GetPosition_OutputArguments =
          new NodeId(UShort.MIN, uint(11671));

  public static final NodeId
      NamespacesType_NamespaceIdentifier_Placeholder_NamespaceFile_SetPosition =
          new NodeId(UShort.MIN, uint(11672));

  public static final NodeId
      NamespacesType_NamespaceIdentifier_Placeholder_NamespaceFile_SetPosition_InputArguments =
          new NodeId(UShort.MIN, uint(11673));

  public static final NodeId
      NamespacesType_NamespaceIdentifier_Placeholder_NamespaceFile_ExportNamespace =
          new NodeId(UShort.MIN, uint(11674));

  public static final NodeId SystemStatusChangeEventType_SystemState =
      new NodeId(UShort.MIN, uint(11696));

  public static final NodeId SamplingIntervalDiagnosticsType_SampledMonitoredItemsCount =
      new NodeId(UShort.MIN, uint(11697));

  public static final NodeId SamplingIntervalDiagnosticsType_MaxSampledMonitoredItemsCount =
      new NodeId(UShort.MIN, uint(11698));

  public static final NodeId SamplingIntervalDiagnosticsType_DisabledMonitoredItemsSamplingCount =
      new NodeId(UShort.MIN, uint(11699));

  public static final NodeId OptionSetType_BitMask = new NodeId(UShort.MIN, uint(11701));

  public static final NodeId Server_ServerCapabilities_MaxArrayLength =
      new NodeId(UShort.MIN, uint(11702));

  public static final NodeId Server_ServerCapabilities_MaxStringLength =
      new NodeId(UShort.MIN, uint(11703));

  public static final NodeId Server_ServerCapabilities_OperationLimits =
      new NodeId(UShort.MIN, uint(11704));

  public static final NodeId Server_ServerCapabilities_OperationLimits_MaxNodesPerRead =
      new NodeId(UShort.MIN, uint(11705));

  public static final NodeId Server_ServerCapabilities_OperationLimits_MaxNodesPerWrite =
      new NodeId(UShort.MIN, uint(11707));

  public static final NodeId Server_ServerCapabilities_OperationLimits_MaxNodesPerMethodCall =
      new NodeId(UShort.MIN, uint(11709));

  public static final NodeId Server_ServerCapabilities_OperationLimits_MaxNodesPerBrowse =
      new NodeId(UShort.MIN, uint(11710));

  public static final NodeId Server_ServerCapabilities_OperationLimits_MaxNodesPerRegisterNodes =
      new NodeId(UShort.MIN, uint(11711));

  public static final NodeId
      Server_ServerCapabilities_OperationLimits_MaxNodesPerTranslateBrowsePathsToNodeIds =
          new NodeId(UShort.MIN, uint(11712));

  public static final NodeId Server_ServerCapabilities_OperationLimits_MaxNodesPerNodeManagement =
      new NodeId(UShort.MIN, uint(11713));

  public static final NodeId Server_ServerCapabilities_OperationLimits_MaxMonitoredItemsPerCall =
      new NodeId(UShort.MIN, uint(11714));

  public static final NodeId Server_Namespaces = new NodeId(UShort.MIN, uint(11715));

  public static final NodeId BitFieldMaskDataType = new NodeId(UShort.MIN, uint(11737));

  public static final NodeId OpenMethodType = new NodeId(UShort.MIN, uint(11738));

  public static final NodeId OpenMethodType_InputArguments = new NodeId(UShort.MIN, uint(11739));

  public static final NodeId OpenMethodType_OutputArguments = new NodeId(UShort.MIN, uint(11740));

  public static final NodeId CloseMethodType = new NodeId(UShort.MIN, uint(11741));

  public static final NodeId CloseMethodType_InputArguments = new NodeId(UShort.MIN, uint(11742));

  public static final NodeId ReadMethodType = new NodeId(UShort.MIN, uint(11743));

  public static final NodeId ReadMethodType_InputArguments = new NodeId(UShort.MIN, uint(11744));

  public static final NodeId ReadMethodType_OutputArguments = new NodeId(UShort.MIN, uint(11745));

  public static final NodeId WriteMethodType = new NodeId(UShort.MIN, uint(11746));

  public static final NodeId WriteMethodType_InputArguments = new NodeId(UShort.MIN, uint(11747));

  public static final NodeId GetPositionMethodType = new NodeId(UShort.MIN, uint(11748));

  public static final NodeId GetPositionMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(11749));

  public static final NodeId GetPositionMethodType_OutputArguments =
      new NodeId(UShort.MIN, uint(11750));

  public static final NodeId SetPositionMethodType = new NodeId(UShort.MIN, uint(11751));

  public static final NodeId SetPositionMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(11752));

  public static final NodeId SystemOffNormalAlarmType = new NodeId(UShort.MIN, uint(11753));

  public static final NodeId SystemOffNormalAlarmType_EventId = new NodeId(UShort.MIN, uint(11754));

  public static final NodeId SystemOffNormalAlarmType_EventType =
      new NodeId(UShort.MIN, uint(11755));

  public static final NodeId SystemOffNormalAlarmType_SourceNode =
      new NodeId(UShort.MIN, uint(11756));

  public static final NodeId SystemOffNormalAlarmType_SourceName =
      new NodeId(UShort.MIN, uint(11757));

  public static final NodeId SystemOffNormalAlarmType_Time = new NodeId(UShort.MIN, uint(11758));

  public static final NodeId SystemOffNormalAlarmType_ReceiveTime =
      new NodeId(UShort.MIN, uint(11759));

  public static final NodeId SystemOffNormalAlarmType_LocalTime =
      new NodeId(UShort.MIN, uint(11760));

  public static final NodeId SystemOffNormalAlarmType_Message = new NodeId(UShort.MIN, uint(11761));

  public static final NodeId SystemOffNormalAlarmType_Severity =
      new NodeId(UShort.MIN, uint(11762));

  public static final NodeId SystemOffNormalAlarmType_ConditionClassId =
      new NodeId(UShort.MIN, uint(11763));

  public static final NodeId SystemOffNormalAlarmType_ConditionClassName =
      new NodeId(UShort.MIN, uint(11764));

  public static final NodeId SystemOffNormalAlarmType_ConditionName =
      new NodeId(UShort.MIN, uint(11765));

  public static final NodeId SystemOffNormalAlarmType_BranchId =
      new NodeId(UShort.MIN, uint(11766));

  public static final NodeId SystemOffNormalAlarmType_Retain = new NodeId(UShort.MIN, uint(11767));

  public static final NodeId SystemOffNormalAlarmType_EnabledState =
      new NodeId(UShort.MIN, uint(11768));

  public static final NodeId SystemOffNormalAlarmType_EnabledState_Id =
      new NodeId(UShort.MIN, uint(11769));

  public static final NodeId SystemOffNormalAlarmType_EnabledState_Name =
      new NodeId(UShort.MIN, uint(11770));

  public static final NodeId SystemOffNormalAlarmType_EnabledState_Number =
      new NodeId(UShort.MIN, uint(11771));

  public static final NodeId SystemOffNormalAlarmType_EnabledState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(11772));

  public static final NodeId SystemOffNormalAlarmType_EnabledState_TransitionTime =
      new NodeId(UShort.MIN, uint(11773));

  public static final NodeId SystemOffNormalAlarmType_EnabledState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(11774));

  public static final NodeId SystemOffNormalAlarmType_EnabledState_TrueState =
      new NodeId(UShort.MIN, uint(11775));

  public static final NodeId SystemOffNormalAlarmType_EnabledState_FalseState =
      new NodeId(UShort.MIN, uint(11776));

  public static final NodeId SystemOffNormalAlarmType_Quality = new NodeId(UShort.MIN, uint(11777));

  public static final NodeId SystemOffNormalAlarmType_Quality_SourceTimestamp =
      new NodeId(UShort.MIN, uint(11778));

  public static final NodeId SystemOffNormalAlarmType_LastSeverity =
      new NodeId(UShort.MIN, uint(11779));

  public static final NodeId SystemOffNormalAlarmType_LastSeverity_SourceTimestamp =
      new NodeId(UShort.MIN, uint(11780));

  public static final NodeId SystemOffNormalAlarmType_Comment = new NodeId(UShort.MIN, uint(11781));

  public static final NodeId SystemOffNormalAlarmType_Comment_SourceTimestamp =
      new NodeId(UShort.MIN, uint(11782));

  public static final NodeId SystemOffNormalAlarmType_ClientUserId =
      new NodeId(UShort.MIN, uint(11783));

  public static final NodeId SystemOffNormalAlarmType_Disable = new NodeId(UShort.MIN, uint(11784));

  public static final NodeId SystemOffNormalAlarmType_Enable = new NodeId(UShort.MIN, uint(11785));

  public static final NodeId SystemOffNormalAlarmType_AddComment =
      new NodeId(UShort.MIN, uint(11786));

  public static final NodeId SystemOffNormalAlarmType_AddComment_InputArguments =
      new NodeId(UShort.MIN, uint(11787));

  public static final NodeId SystemOffNormalAlarmType_ConditionRefresh =
      new NodeId(UShort.MIN, uint(11788));

  public static final NodeId SystemOffNormalAlarmType_ConditionRefresh_InputArguments =
      new NodeId(UShort.MIN, uint(11789));

  public static final NodeId SystemOffNormalAlarmType_AckedState =
      new NodeId(UShort.MIN, uint(11790));

  public static final NodeId SystemOffNormalAlarmType_AckedState_Id =
      new NodeId(UShort.MIN, uint(11791));

  public static final NodeId SystemOffNormalAlarmType_AckedState_Name =
      new NodeId(UShort.MIN, uint(11792));

  public static final NodeId SystemOffNormalAlarmType_AckedState_Number =
      new NodeId(UShort.MIN, uint(11793));

  public static final NodeId SystemOffNormalAlarmType_AckedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(11794));

  public static final NodeId SystemOffNormalAlarmType_AckedState_TransitionTime =
      new NodeId(UShort.MIN, uint(11795));

  public static final NodeId SystemOffNormalAlarmType_AckedState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(11796));

  public static final NodeId SystemOffNormalAlarmType_AckedState_TrueState =
      new NodeId(UShort.MIN, uint(11797));

  public static final NodeId SystemOffNormalAlarmType_AckedState_FalseState =
      new NodeId(UShort.MIN, uint(11798));

  public static final NodeId SystemOffNormalAlarmType_ConfirmedState =
      new NodeId(UShort.MIN, uint(11799));

  public static final NodeId SystemOffNormalAlarmType_ConfirmedState_Id =
      new NodeId(UShort.MIN, uint(11800));

  public static final NodeId SystemOffNormalAlarmType_ConfirmedState_Name =
      new NodeId(UShort.MIN, uint(11801));

  public static final NodeId SystemOffNormalAlarmType_ConfirmedState_Number =
      new NodeId(UShort.MIN, uint(11802));

  public static final NodeId SystemOffNormalAlarmType_ConfirmedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(11803));

  public static final NodeId SystemOffNormalAlarmType_ConfirmedState_TransitionTime =
      new NodeId(UShort.MIN, uint(11804));

  public static final NodeId SystemOffNormalAlarmType_ConfirmedState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(11805));

  public static final NodeId SystemOffNormalAlarmType_ConfirmedState_TrueState =
      new NodeId(UShort.MIN, uint(11806));

  public static final NodeId SystemOffNormalAlarmType_ConfirmedState_FalseState =
      new NodeId(UShort.MIN, uint(11807));

  public static final NodeId SystemOffNormalAlarmType_Acknowledge =
      new NodeId(UShort.MIN, uint(11808));

  public static final NodeId SystemOffNormalAlarmType_Acknowledge_InputArguments =
      new NodeId(UShort.MIN, uint(11809));

  public static final NodeId SystemOffNormalAlarmType_Confirm = new NodeId(UShort.MIN, uint(11810));

  public static final NodeId SystemOffNormalAlarmType_Confirm_InputArguments =
      new NodeId(UShort.MIN, uint(11811));

  public static final NodeId SystemOffNormalAlarmType_ActiveState =
      new NodeId(UShort.MIN, uint(11812));

  public static final NodeId SystemOffNormalAlarmType_ActiveState_Id =
      new NodeId(UShort.MIN, uint(11813));

  public static final NodeId SystemOffNormalAlarmType_ActiveState_Name =
      new NodeId(UShort.MIN, uint(11814));

  public static final NodeId SystemOffNormalAlarmType_ActiveState_Number =
      new NodeId(UShort.MIN, uint(11815));

  public static final NodeId SystemOffNormalAlarmType_ActiveState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(11816));

  public static final NodeId SystemOffNormalAlarmType_ActiveState_TransitionTime =
      new NodeId(UShort.MIN, uint(11817));

  public static final NodeId SystemOffNormalAlarmType_ActiveState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(11818));

  public static final NodeId SystemOffNormalAlarmType_ActiveState_TrueState =
      new NodeId(UShort.MIN, uint(11819));

  public static final NodeId SystemOffNormalAlarmType_ActiveState_FalseState =
      new NodeId(UShort.MIN, uint(11820));

  public static final NodeId SystemOffNormalAlarmType_InputNode =
      new NodeId(UShort.MIN, uint(11821));

  public static final NodeId SystemOffNormalAlarmType_SuppressedState =
      new NodeId(UShort.MIN, uint(11822));

  public static final NodeId SystemOffNormalAlarmType_SuppressedState_Id =
      new NodeId(UShort.MIN, uint(11823));

  public static final NodeId SystemOffNormalAlarmType_SuppressedState_Name =
      new NodeId(UShort.MIN, uint(11824));

  public static final NodeId SystemOffNormalAlarmType_SuppressedState_Number =
      new NodeId(UShort.MIN, uint(11825));

  public static final NodeId SystemOffNormalAlarmType_SuppressedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(11826));

  public static final NodeId SystemOffNormalAlarmType_SuppressedState_TransitionTime =
      new NodeId(UShort.MIN, uint(11827));

  public static final NodeId SystemOffNormalAlarmType_SuppressedState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(11828));

  public static final NodeId SystemOffNormalAlarmType_SuppressedState_TrueState =
      new NodeId(UShort.MIN, uint(11829));

  public static final NodeId SystemOffNormalAlarmType_SuppressedState_FalseState =
      new NodeId(UShort.MIN, uint(11830));

  public static final NodeId SystemOffNormalAlarmType_ShelvingState =
      new NodeId(UShort.MIN, uint(11831));

  public static final NodeId SystemOffNormalAlarmType_ShelvingState_CurrentState =
      new NodeId(UShort.MIN, uint(11832));

  public static final NodeId SystemOffNormalAlarmType_ShelvingState_CurrentState_Id =
      new NodeId(UShort.MIN, uint(11833));

  public static final NodeId SystemOffNormalAlarmType_ShelvingState_CurrentState_Name =
      new NodeId(UShort.MIN, uint(11834));

  public static final NodeId SystemOffNormalAlarmType_ShelvingState_CurrentState_Number =
      new NodeId(UShort.MIN, uint(11835));

  public static final NodeId
      SystemOffNormalAlarmType_ShelvingState_CurrentState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(11836));

  public static final NodeId SystemOffNormalAlarmType_ShelvingState_LastTransition =
      new NodeId(UShort.MIN, uint(11837));

  public static final NodeId SystemOffNormalAlarmType_ShelvingState_LastTransition_Id =
      new NodeId(UShort.MIN, uint(11838));

  public static final NodeId SystemOffNormalAlarmType_ShelvingState_LastTransition_Name =
      new NodeId(UShort.MIN, uint(11839));

  public static final NodeId SystemOffNormalAlarmType_ShelvingState_LastTransition_Number =
      new NodeId(UShort.MIN, uint(11840));

  public static final NodeId SystemOffNormalAlarmType_ShelvingState_LastTransition_TransitionTime =
      new NodeId(UShort.MIN, uint(11841));

  public static final NodeId
      SystemOffNormalAlarmType_ShelvingState_LastTransition_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(11842));

  public static final NodeId SystemOffNormalAlarmType_ShelvingState_UnshelveTime =
      new NodeId(UShort.MIN, uint(11843));

  public static final NodeId SystemOffNormalAlarmType_ShelvingState_Unshelve =
      new NodeId(UShort.MIN, uint(11844));

  public static final NodeId SystemOffNormalAlarmType_ShelvingState_OneShotShelve =
      new NodeId(UShort.MIN, uint(11845));

  public static final NodeId SystemOffNormalAlarmType_ShelvingState_TimedShelve =
      new NodeId(UShort.MIN, uint(11846));

  public static final NodeId SystemOffNormalAlarmType_ShelvingState_TimedShelve_InputArguments =
      new NodeId(UShort.MIN, uint(11847));

  public static final NodeId SystemOffNormalAlarmType_SuppressedOrShelved =
      new NodeId(UShort.MIN, uint(11848));

  public static final NodeId SystemOffNormalAlarmType_MaxTimeShelved =
      new NodeId(UShort.MIN, uint(11849));

  public static final NodeId SystemOffNormalAlarmType_NormalState =
      new NodeId(UShort.MIN, uint(11850));

  public static final NodeId AuditConditionCommentEventType_Comment =
      new NodeId(UShort.MIN, uint(11851));

  public static final NodeId AuditConditionRespondEventType_SelectedResponse =
      new NodeId(UShort.MIN, uint(11852));

  public static final NodeId AuditConditionAcknowledgeEventType_Comment =
      new NodeId(UShort.MIN, uint(11853));

  public static final NodeId AuditConditionConfirmEventType_Comment =
      new NodeId(UShort.MIN, uint(11854));

  public static final NodeId AuditConditionShelvingEventType_ShelvingTime =
      new NodeId(UShort.MIN, uint(11855));

  public static final NodeId AuditProgramTransitionEventType = new NodeId(UShort.MIN, uint(11856));

  public static final NodeId AuditProgramTransitionEventType_EventId =
      new NodeId(UShort.MIN, uint(11857));

  public static final NodeId AuditProgramTransitionEventType_EventType =
      new NodeId(UShort.MIN, uint(11858));

  public static final NodeId AuditProgramTransitionEventType_SourceNode =
      new NodeId(UShort.MIN, uint(11859));

  public static final NodeId AuditProgramTransitionEventType_SourceName =
      new NodeId(UShort.MIN, uint(11860));

  public static final NodeId AuditProgramTransitionEventType_Time =
      new NodeId(UShort.MIN, uint(11861));

  public static final NodeId AuditProgramTransitionEventType_ReceiveTime =
      new NodeId(UShort.MIN, uint(11862));

  public static final NodeId AuditProgramTransitionEventType_LocalTime =
      new NodeId(UShort.MIN, uint(11863));

  public static final NodeId AuditProgramTransitionEventType_Message =
      new NodeId(UShort.MIN, uint(11864));

  public static final NodeId AuditProgramTransitionEventType_Severity =
      new NodeId(UShort.MIN, uint(11865));

  public static final NodeId AuditProgramTransitionEventType_ActionTimeStamp =
      new NodeId(UShort.MIN, uint(11866));

  public static final NodeId AuditProgramTransitionEventType_Status =
      new NodeId(UShort.MIN, uint(11867));

  public static final NodeId AuditProgramTransitionEventType_ServerId =
      new NodeId(UShort.MIN, uint(11868));

  public static final NodeId AuditProgramTransitionEventType_ClientAuditEntryId =
      new NodeId(UShort.MIN, uint(11869));

  public static final NodeId AuditProgramTransitionEventType_ClientUserId =
      new NodeId(UShort.MIN, uint(11870));

  public static final NodeId AuditProgramTransitionEventType_MethodId =
      new NodeId(UShort.MIN, uint(11871));

  public static final NodeId AuditProgramTransitionEventType_InputArguments =
      new NodeId(UShort.MIN, uint(11872));

  public static final NodeId AuditProgramTransitionEventType_OldStateId =
      new NodeId(UShort.MIN, uint(11873));

  public static final NodeId AuditProgramTransitionEventType_NewStateId =
      new NodeId(UShort.MIN, uint(11874));

  public static final NodeId AuditProgramTransitionEventType_TransitionNumber =
      new NodeId(UShort.MIN, uint(11875));

  public static final NodeId HistoricalDataConfigurationType_AggregateFunctions =
      new NodeId(UShort.MIN, uint(11876));

  public static final NodeId HAConfiguration_AggregateFunctions =
      new NodeId(UShort.MIN, uint(11877));

  public static final NodeId NodeClass_EnumValues = new NodeId(UShort.MIN, uint(11878));

  public static final NodeId InstanceNode = new NodeId(UShort.MIN, uint(11879));

  public static final NodeId TypeNode = new NodeId(UShort.MIN, uint(11880));

  public static final NodeId NodeAttributesMask_EnumValues = new NodeId(UShort.MIN, uint(11881));

  public static final NodeId BrowseResultMask_EnumValues = new NodeId(UShort.MIN, uint(11883));

  public static final NodeId HistoryUpdateType_EnumValues = new NodeId(UShort.MIN, uint(11884));

  public static final NodeId PerformUpdateType_EnumValues = new NodeId(UShort.MIN, uint(11885));

  public static final NodeId InstanceNode_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(11887));

  public static final NodeId TypeNode_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(11888));

  public static final NodeId InstanceNode_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(11889));

  public static final NodeId TypeNode_Encoding_DefaultBinary = new NodeId(UShort.MIN, uint(11890));

  public static final NodeId
      SessionDiagnosticsObjectType_SessionDiagnostics_UnauthorizedRequestCount =
          new NodeId(UShort.MIN, uint(11891));

  public static final NodeId SessionDiagnosticsVariableType_UnauthorizedRequestCount =
      new NodeId(UShort.MIN, uint(11892));

  public static final NodeId OpenFileMode = new NodeId(UShort.MIN, uint(11939));

  public static final NodeId OpenFileMode_EnumValues = new NodeId(UShort.MIN, uint(11940));

  public static final NodeId ModelChangeStructureVerbMask = new NodeId(UShort.MIN, uint(11941));

  public static final NodeId ModelChangeStructureVerbMask_EnumValues =
      new NodeId(UShort.MIN, uint(11942));

  public static final NodeId EndpointUrlListDataType = new NodeId(UShort.MIN, uint(11943));

  public static final NodeId NetworkGroupDataType = new NodeId(UShort.MIN, uint(11944));

  public static final NodeId NonTransparentNetworkRedundancyType =
      new NodeId(UShort.MIN, uint(11945));

  public static final NodeId NonTransparentNetworkRedundancyType_RedundancySupport =
      new NodeId(UShort.MIN, uint(11946));

  public static final NodeId NonTransparentNetworkRedundancyType_ServerUriArray =
      new NodeId(UShort.MIN, uint(11947));

  public static final NodeId NonTransparentNetworkRedundancyType_ServerNetworkGroups =
      new NodeId(UShort.MIN, uint(11948));

  public static final NodeId EndpointUrlListDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(11949));

  public static final NodeId NetworkGroupDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(11950));

  public static final NodeId OpcUa_XmlSchema_EndpointUrlListDataType =
      new NodeId(UShort.MIN, uint(11951));

  public static final NodeId OpcUa_XmlSchema_EndpointUrlListDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(11952));

  public static final NodeId OpcUa_XmlSchema_EndpointUrlListDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(11953));

  public static final NodeId OpcUa_XmlSchema_NetworkGroupDataType =
      new NodeId(UShort.MIN, uint(11954));

  public static final NodeId OpcUa_XmlSchema_NetworkGroupDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(11955));

  public static final NodeId OpcUa_XmlSchema_NetworkGroupDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(11956));

  public static final NodeId EndpointUrlListDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(11957));

  public static final NodeId NetworkGroupDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(11958));

  public static final NodeId OpcUa_BinarySchema_EndpointUrlListDataType =
      new NodeId(UShort.MIN, uint(11959));

  public static final NodeId OpcUa_BinarySchema_EndpointUrlListDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(11960));

  public static final NodeId OpcUa_BinarySchema_EndpointUrlListDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(11961));

  public static final NodeId OpcUa_BinarySchema_NetworkGroupDataType =
      new NodeId(UShort.MIN, uint(11962));

  public static final NodeId OpcUa_BinarySchema_NetworkGroupDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(11963));

  public static final NodeId OpcUa_BinarySchema_NetworkGroupDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(11964));

  public static final NodeId ArrayItemType = new NodeId(UShort.MIN, uint(12021));

  public static final NodeId ArrayItemType_Definition = new NodeId(UShort.MIN, uint(12022));

  public static final NodeId ArrayItemType_ValuePrecision = new NodeId(UShort.MIN, uint(12023));

  public static final NodeId ArrayItemType_InstrumentRange = new NodeId(UShort.MIN, uint(12024));

  public static final NodeId ArrayItemType_EURange = new NodeId(UShort.MIN, uint(12025));

  public static final NodeId ArrayItemType_EngineeringUnits = new NodeId(UShort.MIN, uint(12026));

  public static final NodeId ArrayItemType_Title = new NodeId(UShort.MIN, uint(12027));

  public static final NodeId ArrayItemType_AxisScaleType = new NodeId(UShort.MIN, uint(12028));

  public static final NodeId YArrayItemType = new NodeId(UShort.MIN, uint(12029));

  public static final NodeId YArrayItemType_Definition = new NodeId(UShort.MIN, uint(12030));

  public static final NodeId YArrayItemType_ValuePrecision = new NodeId(UShort.MIN, uint(12031));

  public static final NodeId YArrayItemType_InstrumentRange = new NodeId(UShort.MIN, uint(12032));

  public static final NodeId YArrayItemType_EURange = new NodeId(UShort.MIN, uint(12033));

  public static final NodeId YArrayItemType_EngineeringUnits = new NodeId(UShort.MIN, uint(12034));

  public static final NodeId YArrayItemType_Title = new NodeId(UShort.MIN, uint(12035));

  public static final NodeId YArrayItemType_AxisScaleType = new NodeId(UShort.MIN, uint(12036));

  public static final NodeId YArrayItemType_XAxisDefinition = new NodeId(UShort.MIN, uint(12037));

  public static final NodeId XYArrayItemType = new NodeId(UShort.MIN, uint(12038));

  public static final NodeId XYArrayItemType_Definition = new NodeId(UShort.MIN, uint(12039));

  public static final NodeId XYArrayItemType_ValuePrecision = new NodeId(UShort.MIN, uint(12040));

  public static final NodeId XYArrayItemType_InstrumentRange = new NodeId(UShort.MIN, uint(12041));

  public static final NodeId XYArrayItemType_EURange = new NodeId(UShort.MIN, uint(12042));

  public static final NodeId XYArrayItemType_EngineeringUnits = new NodeId(UShort.MIN, uint(12043));

  public static final NodeId XYArrayItemType_Title = new NodeId(UShort.MIN, uint(12044));

  public static final NodeId XYArrayItemType_AxisScaleType = new NodeId(UShort.MIN, uint(12045));

  public static final NodeId XYArrayItemType_XAxisDefinition = new NodeId(UShort.MIN, uint(12046));

  public static final NodeId ImageItemType = new NodeId(UShort.MIN, uint(12047));

  public static final NodeId ImageItemType_Definition = new NodeId(UShort.MIN, uint(12048));

  public static final NodeId ImageItemType_ValuePrecision = new NodeId(UShort.MIN, uint(12049));

  public static final NodeId ImageItemType_InstrumentRange = new NodeId(UShort.MIN, uint(12050));

  public static final NodeId ImageItemType_EURange = new NodeId(UShort.MIN, uint(12051));

  public static final NodeId ImageItemType_EngineeringUnits = new NodeId(UShort.MIN, uint(12052));

  public static final NodeId ImageItemType_Title = new NodeId(UShort.MIN, uint(12053));

  public static final NodeId ImageItemType_AxisScaleType = new NodeId(UShort.MIN, uint(12054));

  public static final NodeId ImageItemType_XAxisDefinition = new NodeId(UShort.MIN, uint(12055));

  public static final NodeId ImageItemType_YAxisDefinition = new NodeId(UShort.MIN, uint(12056));

  public static final NodeId CubeItemType = new NodeId(UShort.MIN, uint(12057));

  public static final NodeId CubeItemType_Definition = new NodeId(UShort.MIN, uint(12058));

  public static final NodeId CubeItemType_ValuePrecision = new NodeId(UShort.MIN, uint(12059));

  public static final NodeId CubeItemType_InstrumentRange = new NodeId(UShort.MIN, uint(12060));

  public static final NodeId CubeItemType_EURange = new NodeId(UShort.MIN, uint(12061));

  public static final NodeId CubeItemType_EngineeringUnits = new NodeId(UShort.MIN, uint(12062));

  public static final NodeId CubeItemType_Title = new NodeId(UShort.MIN, uint(12063));

  public static final NodeId CubeItemType_AxisScaleType = new NodeId(UShort.MIN, uint(12064));

  public static final NodeId CubeItemType_XAxisDefinition = new NodeId(UShort.MIN, uint(12065));

  public static final NodeId CubeItemType_YAxisDefinition = new NodeId(UShort.MIN, uint(12066));

  public static final NodeId CubeItemType_ZAxisDefinition = new NodeId(UShort.MIN, uint(12067));

  public static final NodeId NDimensionArrayItemType = new NodeId(UShort.MIN, uint(12068));

  public static final NodeId NDimensionArrayItemType_Definition =
      new NodeId(UShort.MIN, uint(12069));

  public static final NodeId NDimensionArrayItemType_ValuePrecision =
      new NodeId(UShort.MIN, uint(12070));

  public static final NodeId NDimensionArrayItemType_InstrumentRange =
      new NodeId(UShort.MIN, uint(12071));

  public static final NodeId NDimensionArrayItemType_EURange = new NodeId(UShort.MIN, uint(12072));

  public static final NodeId NDimensionArrayItemType_EngineeringUnits =
      new NodeId(UShort.MIN, uint(12073));

  public static final NodeId NDimensionArrayItemType_Title = new NodeId(UShort.MIN, uint(12074));

  public static final NodeId NDimensionArrayItemType_AxisScaleType =
      new NodeId(UShort.MIN, uint(12075));

  public static final NodeId NDimensionArrayItemType_AxisDefinition =
      new NodeId(UShort.MIN, uint(12076));

  public static final NodeId AxisScaleEnumeration = new NodeId(UShort.MIN, uint(12077));

  public static final NodeId AxisScaleEnumeration_EnumStrings = new NodeId(UShort.MIN, uint(12078));

  public static final NodeId AxisInformation = new NodeId(UShort.MIN, uint(12079));

  public static final NodeId XVType = new NodeId(UShort.MIN, uint(12080));

  public static final NodeId AxisInformation_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(12081));

  public static final NodeId XVType_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(12082));

  public static final NodeId OpcUa_XmlSchema_AxisInformation = new NodeId(UShort.MIN, uint(12083));

  public static final NodeId OpcUa_XmlSchema_AxisInformation_DataTypeVersion =
      new NodeId(UShort.MIN, uint(12084));

  public static final NodeId OpcUa_XmlSchema_AxisInformation_DictionaryFragment =
      new NodeId(UShort.MIN, uint(12085));

  public static final NodeId OpcUa_XmlSchema_XVType = new NodeId(UShort.MIN, uint(12086));

  public static final NodeId OpcUa_XmlSchema_XVType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(12087));

  public static final NodeId OpcUa_XmlSchema_XVType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(12088));

  public static final NodeId AxisInformation_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(12089));

  public static final NodeId XVType_Encoding_DefaultBinary = new NodeId(UShort.MIN, uint(12090));

  public static final NodeId OpcUa_BinarySchema_AxisInformation =
      new NodeId(UShort.MIN, uint(12091));

  public static final NodeId OpcUa_BinarySchema_AxisInformation_DataTypeVersion =
      new NodeId(UShort.MIN, uint(12092));

  public static final NodeId OpcUa_BinarySchema_AxisInformation_DictionaryFragment =
      new NodeId(UShort.MIN, uint(12093));

  public static final NodeId OpcUa_BinarySchema_XVType = new NodeId(UShort.MIN, uint(12094));

  public static final NodeId OpcUa_BinarySchema_XVType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(12095));

  public static final NodeId OpcUa_BinarySchema_XVType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(12096));

  public static final NodeId SessionsDiagnosticsSummaryType_ClientName_Placeholder =
      new NodeId(UShort.MIN, uint(12097));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics =
          new NodeId(UShort.MIN, uint(12098));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_SessionId =
          new NodeId(UShort.MIN, uint(12099));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_SessionName =
          new NodeId(UShort.MIN, uint(12100));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_ClientDescription =
          new NodeId(UShort.MIN, uint(12101));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_ServerUri =
          new NodeId(UShort.MIN, uint(12102));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_EndpointUrl =
          new NodeId(UShort.MIN, uint(12103));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_LocaleIds =
          new NodeId(UShort.MIN, uint(12104));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_ActualSessionTimeout =
          new NodeId(UShort.MIN, uint(12105));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_MaxResponseMessageSize =
          new NodeId(UShort.MIN, uint(12106));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_ClientConnectionTime =
          new NodeId(UShort.MIN, uint(12107));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_ClientLastContactTime =
          new NodeId(UShort.MIN, uint(12108));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_CurrentSubscriptionsCount =
          new NodeId(UShort.MIN, uint(12109));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_CurrentMonitoredItemsCount =
          new NodeId(UShort.MIN, uint(12110));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_CurrentPublishRequestsInQueue =
          new NodeId(UShort.MIN, uint(12111));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_TotalRequestCount =
          new NodeId(UShort.MIN, uint(12112));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_UnauthorizedRequestCount =
          new NodeId(UShort.MIN, uint(12113));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_ReadCount =
          new NodeId(UShort.MIN, uint(12114));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_HistoryReadCount =
          new NodeId(UShort.MIN, uint(12115));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_WriteCount =
          new NodeId(UShort.MIN, uint(12116));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_HistoryUpdateCount =
          new NodeId(UShort.MIN, uint(12117));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_CallCount =
          new NodeId(UShort.MIN, uint(12118));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_CreateMonitoredItemsCount =
          new NodeId(UShort.MIN, uint(12119));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_ModifyMonitoredItemsCount =
          new NodeId(UShort.MIN, uint(12120));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_SetMonitoringModeCount =
          new NodeId(UShort.MIN, uint(12121));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_SetTriggeringCount =
          new NodeId(UShort.MIN, uint(12122));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_DeleteMonitoredItemsCount =
          new NodeId(UShort.MIN, uint(12123));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_CreateSubscriptionCount =
          new NodeId(UShort.MIN, uint(12124));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_ModifySubscriptionCount =
          new NodeId(UShort.MIN, uint(12125));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_SetPublishingModeCount =
          new NodeId(UShort.MIN, uint(12126));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_PublishCount =
          new NodeId(UShort.MIN, uint(12127));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_RepublishCount =
          new NodeId(UShort.MIN, uint(12128));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_TransferSubscriptionsCount =
          new NodeId(UShort.MIN, uint(12129));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_DeleteSubscriptionsCount =
          new NodeId(UShort.MIN, uint(12130));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_AddNodesCount =
          new NodeId(UShort.MIN, uint(12131));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_AddReferencesCount =
          new NodeId(UShort.MIN, uint(12132));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_DeleteNodesCount =
          new NodeId(UShort.MIN, uint(12133));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_DeleteReferencesCount =
          new NodeId(UShort.MIN, uint(12134));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_BrowseCount =
          new NodeId(UShort.MIN, uint(12135));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_BrowseNextCount =
          new NodeId(UShort.MIN, uint(12136));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_TranslateBrowsePathsToNodeIdsCount =
          new NodeId(UShort.MIN, uint(12137));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_QueryFirstCount =
          new NodeId(UShort.MIN, uint(12138));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_QueryNextCount =
          new NodeId(UShort.MIN, uint(12139));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_RegisterNodesCount =
          new NodeId(UShort.MIN, uint(12140));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_UnregisterNodesCount =
          new NodeId(UShort.MIN, uint(12141));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionSecurityDiagnostics =
          new NodeId(UShort.MIN, uint(12142));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionSecurityDiagnostics_SessionId =
          new NodeId(UShort.MIN, uint(12143));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionSecurityDiagnostics_ClientUserIdOfSession =
          new NodeId(UShort.MIN, uint(12144));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionSecurityDiagnostics_ClientUserIdHistory =
          new NodeId(UShort.MIN, uint(12145));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionSecurityDiagnostics_AuthenticationMechanism =
          new NodeId(UShort.MIN, uint(12146));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionSecurityDiagnostics_Encoding =
          new NodeId(UShort.MIN, uint(12147));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionSecurityDiagnostics_TransportProtocol =
          new NodeId(UShort.MIN, uint(12148));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionSecurityDiagnostics_SecurityMode =
          new NodeId(UShort.MIN, uint(12149));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionSecurityDiagnostics_SecurityPolicyUri =
          new NodeId(UShort.MIN, uint(12150));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionSecurityDiagnostics_ClientCertificate =
          new NodeId(UShort.MIN, uint(12151));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SubscriptionDiagnosticsArray =
          new NodeId(UShort.MIN, uint(12152));

  public static final NodeId
      ServerType_ServerCapabilities_OperationLimits_MaxNodesPerHistoryReadData =
          new NodeId(UShort.MIN, uint(12153));

  public static final NodeId
      ServerType_ServerCapabilities_OperationLimits_MaxNodesPerHistoryReadEvents =
          new NodeId(UShort.MIN, uint(12154));

  public static final NodeId
      ServerType_ServerCapabilities_OperationLimits_MaxNodesPerHistoryUpdateData =
          new NodeId(UShort.MIN, uint(12155));

  public static final NodeId
      ServerType_ServerCapabilities_OperationLimits_MaxNodesPerHistoryUpdateEvents =
          new NodeId(UShort.MIN, uint(12156));

  public static final NodeId ServerCapabilitiesType_OperationLimits_MaxNodesPerHistoryReadData =
      new NodeId(UShort.MIN, uint(12157));

  public static final NodeId ServerCapabilitiesType_OperationLimits_MaxNodesPerHistoryReadEvents =
      new NodeId(UShort.MIN, uint(12158));

  public static final NodeId ServerCapabilitiesType_OperationLimits_MaxNodesPerHistoryUpdateData =
      new NodeId(UShort.MIN, uint(12159));

  public static final NodeId ServerCapabilitiesType_OperationLimits_MaxNodesPerHistoryUpdateEvents =
      new NodeId(UShort.MIN, uint(12160));

  public static final NodeId OperationLimitsType_MaxNodesPerHistoryReadData =
      new NodeId(UShort.MIN, uint(12161));

  public static final NodeId OperationLimitsType_MaxNodesPerHistoryReadEvents =
      new NodeId(UShort.MIN, uint(12162));

  public static final NodeId OperationLimitsType_MaxNodesPerHistoryUpdateData =
      new NodeId(UShort.MIN, uint(12163));

  public static final NodeId OperationLimitsType_MaxNodesPerHistoryUpdateEvents =
      new NodeId(UShort.MIN, uint(12164));

  public static final NodeId Server_ServerCapabilities_OperationLimits_MaxNodesPerHistoryReadData =
      new NodeId(UShort.MIN, uint(12165));

  public static final NodeId
      Server_ServerCapabilities_OperationLimits_MaxNodesPerHistoryReadEvents =
          new NodeId(UShort.MIN, uint(12166));

  public static final NodeId
      Server_ServerCapabilities_OperationLimits_MaxNodesPerHistoryUpdateData =
          new NodeId(UShort.MIN, uint(12167));

  public static final NodeId
      Server_ServerCapabilities_OperationLimits_MaxNodesPerHistoryUpdateEvents =
          new NodeId(UShort.MIN, uint(12168));

  public static final NodeId NamingRuleType_EnumValues = new NodeId(UShort.MIN, uint(12169));

  public static final NodeId ViewVersion = new NodeId(UShort.MIN, uint(12170));

  public static final NodeId ComplexNumberType = new NodeId(UShort.MIN, uint(12171));

  public static final NodeId DoubleComplexNumberType = new NodeId(UShort.MIN, uint(12172));

  public static final NodeId ComplexNumberType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(12173));

  public static final NodeId DoubleComplexNumberType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(12174));

  public static final NodeId OpcUa_XmlSchema_ComplexNumberType =
      new NodeId(UShort.MIN, uint(12175));

  public static final NodeId OpcUa_XmlSchema_ComplexNumberType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(12176));

  public static final NodeId OpcUa_XmlSchema_ComplexNumberType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(12177));

  public static final NodeId OpcUa_XmlSchema_DoubleComplexNumberType =
      new NodeId(UShort.MIN, uint(12178));

  public static final NodeId OpcUa_XmlSchema_DoubleComplexNumberType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(12179));

  public static final NodeId OpcUa_XmlSchema_DoubleComplexNumberType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(12180));

  public static final NodeId ComplexNumberType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(12181));

  public static final NodeId DoubleComplexNumberType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(12182));

  public static final NodeId OpcUa_BinarySchema_ComplexNumberType =
      new NodeId(UShort.MIN, uint(12183));

  public static final NodeId OpcUa_BinarySchema_ComplexNumberType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(12184));

  public static final NodeId OpcUa_BinarySchema_ComplexNumberType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(12185));

  public static final NodeId OpcUa_BinarySchema_DoubleComplexNumberType =
      new NodeId(UShort.MIN, uint(12186));

  public static final NodeId OpcUa_BinarySchema_DoubleComplexNumberType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(12187));

  public static final NodeId OpcUa_BinarySchema_DoubleComplexNumberType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(12188));

  public static final NodeId ServerOnNetwork = new NodeId(UShort.MIN, uint(12189));

  public static final NodeId FindServersOnNetworkRequest = new NodeId(UShort.MIN, uint(12190));

  public static final NodeId FindServersOnNetworkResponse = new NodeId(UShort.MIN, uint(12191));

  public static final NodeId RegisterServer2Request = new NodeId(UShort.MIN, uint(12193));

  public static final NodeId RegisterServer2Response = new NodeId(UShort.MIN, uint(12194));

  public static final NodeId ServerOnNetwork_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(12195));

  public static final NodeId FindServersOnNetworkRequest_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(12196));

  public static final NodeId FindServersOnNetworkResponse_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(12197));

  public static final NodeId RegisterServer2Request_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(12199));

  public static final NodeId RegisterServer2Response_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(12200));

  public static final NodeId OpcUa_XmlSchema_ServerOnNetwork = new NodeId(UShort.MIN, uint(12201));

  public static final NodeId OpcUa_XmlSchema_ServerOnNetwork_DataTypeVersion =
      new NodeId(UShort.MIN, uint(12202));

  public static final NodeId OpcUa_XmlSchema_ServerOnNetwork_DictionaryFragment =
      new NodeId(UShort.MIN, uint(12203));

  public static final NodeId ServerOnNetwork_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(12207));

  public static final NodeId FindServersOnNetworkRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(12208));

  public static final NodeId FindServersOnNetworkResponse_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(12209));

  public static final NodeId RegisterServer2Request_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(12211));

  public static final NodeId RegisterServer2Response_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(12212));

  public static final NodeId OpcUa_BinarySchema_ServerOnNetwork =
      new NodeId(UShort.MIN, uint(12213));

  public static final NodeId OpcUa_BinarySchema_ServerOnNetwork_DataTypeVersion =
      new NodeId(UShort.MIN, uint(12214));

  public static final NodeId OpcUa_BinarySchema_ServerOnNetwork_DictionaryFragment =
      new NodeId(UShort.MIN, uint(12215));

  public static final NodeId ProgressEventType_Context = new NodeId(UShort.MIN, uint(12502));

  public static final NodeId ProgressEventType_Progress = new NodeId(UShort.MIN, uint(12503));

  public static final NodeId OpenWithMasksMethodType = new NodeId(UShort.MIN, uint(12513));

  public static final NodeId OpenWithMasksMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(12514));

  public static final NodeId OpenWithMasksMethodType_OutputArguments =
      new NodeId(UShort.MIN, uint(12515));

  public static final NodeId CloseAndUpdateMethodType = new NodeId(UShort.MIN, uint(12516));

  public static final NodeId CloseAndUpdateMethodType_OutputArguments =
      new NodeId(UShort.MIN, uint(12517));

  public static final NodeId AddCertificateMethodType = new NodeId(UShort.MIN, uint(12518));

  public static final NodeId AddCertificateMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(12519));

  public static final NodeId RemoveCertificateMethodType = new NodeId(UShort.MIN, uint(12520));

  public static final NodeId RemoveCertificateMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(12521));

  public static final NodeId TrustListType = new NodeId(UShort.MIN, uint(12522));

  public static final NodeId TrustListType_Size = new NodeId(UShort.MIN, uint(12523));

  public static final NodeId TrustListType_OpenCount = new NodeId(UShort.MIN, uint(12526));

  public static final NodeId TrustListType_Open = new NodeId(UShort.MIN, uint(12527));

  public static final NodeId TrustListType_Open_InputArguments =
      new NodeId(UShort.MIN, uint(12528));

  public static final NodeId TrustListType_Open_OutputArguments =
      new NodeId(UShort.MIN, uint(12529));

  public static final NodeId TrustListType_Close = new NodeId(UShort.MIN, uint(12530));

  public static final NodeId TrustListType_Close_InputArguments =
      new NodeId(UShort.MIN, uint(12531));

  public static final NodeId TrustListType_Read = new NodeId(UShort.MIN, uint(12532));

  public static final NodeId TrustListType_Read_InputArguments =
      new NodeId(UShort.MIN, uint(12533));

  public static final NodeId TrustListType_Read_OutputArguments =
      new NodeId(UShort.MIN, uint(12534));

  public static final NodeId TrustListType_Write = new NodeId(UShort.MIN, uint(12535));

  public static final NodeId TrustListType_Write_InputArguments =
      new NodeId(UShort.MIN, uint(12536));

  public static final NodeId TrustListType_GetPosition = new NodeId(UShort.MIN, uint(12537));

  public static final NodeId TrustListType_GetPosition_InputArguments =
      new NodeId(UShort.MIN, uint(12538));

  public static final NodeId TrustListType_GetPosition_OutputArguments =
      new NodeId(UShort.MIN, uint(12539));

  public static final NodeId TrustListType_SetPosition = new NodeId(UShort.MIN, uint(12540));

  public static final NodeId TrustListType_SetPosition_InputArguments =
      new NodeId(UShort.MIN, uint(12541));

  public static final NodeId TrustListType_LastUpdateTime = new NodeId(UShort.MIN, uint(12542));

  public static final NodeId TrustListType_OpenWithMasks = new NodeId(UShort.MIN, uint(12543));

  public static final NodeId TrustListType_OpenWithMasks_InputArguments =
      new NodeId(UShort.MIN, uint(12544));

  public static final NodeId TrustListType_OpenWithMasks_OutputArguments =
      new NodeId(UShort.MIN, uint(12545));

  public static final NodeId TrustListType_CloseAndUpdate = new NodeId(UShort.MIN, uint(12546));

  public static final NodeId TrustListType_CloseAndUpdate_OutputArguments =
      new NodeId(UShort.MIN, uint(12547));

  public static final NodeId TrustListType_AddCertificate = new NodeId(UShort.MIN, uint(12548));

  public static final NodeId TrustListType_AddCertificate_InputArguments =
      new NodeId(UShort.MIN, uint(12549));

  public static final NodeId TrustListType_RemoveCertificate = new NodeId(UShort.MIN, uint(12550));

  public static final NodeId TrustListType_RemoveCertificate_InputArguments =
      new NodeId(UShort.MIN, uint(12551));

  public static final NodeId TrustListMasks = new NodeId(UShort.MIN, uint(12552));

  public static final NodeId TrustListMasks_EnumValues = new NodeId(UShort.MIN, uint(12553));

  public static final NodeId TrustListDataType = new NodeId(UShort.MIN, uint(12554));

  public static final NodeId CertificateGroupType = new NodeId(UShort.MIN, uint(12555));

  public static final NodeId CertificateType = new NodeId(UShort.MIN, uint(12556));

  public static final NodeId ApplicationCertificateType = new NodeId(UShort.MIN, uint(12557));

  public static final NodeId HttpsCertificateType = new NodeId(UShort.MIN, uint(12558));

  public static final NodeId RsaMinApplicationCertificateType = new NodeId(UShort.MIN, uint(12559));

  public static final NodeId RsaSha256ApplicationCertificateType =
      new NodeId(UShort.MIN, uint(12560));

  public static final NodeId TrustListUpdatedAuditEventType = new NodeId(UShort.MIN, uint(12561));

  public static final NodeId TrustListUpdatedAuditEventType_EventId =
      new NodeId(UShort.MIN, uint(12562));

  public static final NodeId TrustListUpdatedAuditEventType_EventType =
      new NodeId(UShort.MIN, uint(12563));

  public static final NodeId TrustListUpdatedAuditEventType_SourceNode =
      new NodeId(UShort.MIN, uint(12564));

  public static final NodeId TrustListUpdatedAuditEventType_SourceName =
      new NodeId(UShort.MIN, uint(12565));

  public static final NodeId TrustListUpdatedAuditEventType_Time =
      new NodeId(UShort.MIN, uint(12566));

  public static final NodeId TrustListUpdatedAuditEventType_ReceiveTime =
      new NodeId(UShort.MIN, uint(12567));

  public static final NodeId TrustListUpdatedAuditEventType_LocalTime =
      new NodeId(UShort.MIN, uint(12568));

  public static final NodeId TrustListUpdatedAuditEventType_Message =
      new NodeId(UShort.MIN, uint(12569));

  public static final NodeId TrustListUpdatedAuditEventType_Severity =
      new NodeId(UShort.MIN, uint(12570));

  public static final NodeId TrustListUpdatedAuditEventType_ActionTimeStamp =
      new NodeId(UShort.MIN, uint(12571));

  public static final NodeId TrustListUpdatedAuditEventType_Status =
      new NodeId(UShort.MIN, uint(12572));

  public static final NodeId TrustListUpdatedAuditEventType_ServerId =
      new NodeId(UShort.MIN, uint(12573));

  public static final NodeId TrustListUpdatedAuditEventType_ClientAuditEntryId =
      new NodeId(UShort.MIN, uint(12574));

  public static final NodeId TrustListUpdatedAuditEventType_ClientUserId =
      new NodeId(UShort.MIN, uint(12575));

  public static final NodeId TrustListUpdatedAuditEventType_MethodId =
      new NodeId(UShort.MIN, uint(12576));

  public static final NodeId TrustListUpdatedAuditEventType_InputArguments =
      new NodeId(UShort.MIN, uint(12577));

  public static final NodeId UpdateCertificateMethodType = new NodeId(UShort.MIN, uint(12578));

  public static final NodeId UpdateCertificateMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(12579));

  public static final NodeId UpdateCertificateMethodType_OutputArguments =
      new NodeId(UShort.MIN, uint(12580));

  public static final NodeId ServerConfigurationType = new NodeId(UShort.MIN, uint(12581));

  public static final NodeId ServerConfigurationType_SupportedPrivateKeyFormats =
      new NodeId(UShort.MIN, uint(12583));

  public static final NodeId ServerConfigurationType_MaxTrustListSize =
      new NodeId(UShort.MIN, uint(12584));

  public static final NodeId ServerConfigurationType_MulticastDnsEnabled =
      new NodeId(UShort.MIN, uint(12585));

  public static final NodeId ServerConfigurationType_UpdateCertificate =
      new NodeId(UShort.MIN, uint(12616));

  public static final NodeId ServerConfigurationType_UpdateCertificate_InputArguments =
      new NodeId(UShort.MIN, uint(12617));

  public static final NodeId ServerConfigurationType_UpdateCertificate_OutputArguments =
      new NodeId(UShort.MIN, uint(12618));

  public static final NodeId CertificateUpdatedAuditEventType = new NodeId(UShort.MIN, uint(12620));

  public static final NodeId CertificateUpdatedAuditEventType_EventId =
      new NodeId(UShort.MIN, uint(12621));

  public static final NodeId CertificateUpdatedAuditEventType_EventType =
      new NodeId(UShort.MIN, uint(12622));

  public static final NodeId CertificateUpdatedAuditEventType_SourceNode =
      new NodeId(UShort.MIN, uint(12623));

  public static final NodeId CertificateUpdatedAuditEventType_SourceName =
      new NodeId(UShort.MIN, uint(12624));

  public static final NodeId CertificateUpdatedAuditEventType_Time =
      new NodeId(UShort.MIN, uint(12625));

  public static final NodeId CertificateUpdatedAuditEventType_ReceiveTime =
      new NodeId(UShort.MIN, uint(12626));

  public static final NodeId CertificateUpdatedAuditEventType_LocalTime =
      new NodeId(UShort.MIN, uint(12627));

  public static final NodeId CertificateUpdatedAuditEventType_Message =
      new NodeId(UShort.MIN, uint(12628));

  public static final NodeId CertificateUpdatedAuditEventType_Severity =
      new NodeId(UShort.MIN, uint(12629));

  public static final NodeId CertificateUpdatedAuditEventType_ActionTimeStamp =
      new NodeId(UShort.MIN, uint(12630));

  public static final NodeId CertificateUpdatedAuditEventType_Status =
      new NodeId(UShort.MIN, uint(12631));

  public static final NodeId CertificateUpdatedAuditEventType_ServerId =
      new NodeId(UShort.MIN, uint(12632));

  public static final NodeId CertificateUpdatedAuditEventType_ClientAuditEntryId =
      new NodeId(UShort.MIN, uint(12633));

  public static final NodeId CertificateUpdatedAuditEventType_ClientUserId =
      new NodeId(UShort.MIN, uint(12634));

  public static final NodeId CertificateUpdatedAuditEventType_MethodId =
      new NodeId(UShort.MIN, uint(12635));

  public static final NodeId CertificateUpdatedAuditEventType_InputArguments =
      new NodeId(UShort.MIN, uint(12636));

  public static final NodeId ServerConfiguration = new NodeId(UShort.MIN, uint(12637));

  public static final NodeId ServerConfiguration_SupportedPrivateKeyFormats =
      new NodeId(UShort.MIN, uint(12639));

  public static final NodeId ServerConfiguration_MaxTrustListSize =
      new NodeId(UShort.MIN, uint(12640));

  public static final NodeId ServerConfiguration_MulticastDnsEnabled =
      new NodeId(UShort.MIN, uint(12641));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList =
          new NodeId(UShort.MIN, uint(12642));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_Size =
          new NodeId(UShort.MIN, uint(12643));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_OpenCount =
          new NodeId(UShort.MIN, uint(12646));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_Open =
          new NodeId(UShort.MIN, uint(12647));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_Open_InputArguments =
          new NodeId(UShort.MIN, uint(12648));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_Open_OutputArguments =
          new NodeId(UShort.MIN, uint(12649));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_Close =
          new NodeId(UShort.MIN, uint(12650));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_Close_InputArguments =
          new NodeId(UShort.MIN, uint(12651));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_Read =
          new NodeId(UShort.MIN, uint(12652));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_Read_InputArguments =
          new NodeId(UShort.MIN, uint(12653));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_Read_OutputArguments =
          new NodeId(UShort.MIN, uint(12654));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_Write =
          new NodeId(UShort.MIN, uint(12655));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_Write_InputArguments =
          new NodeId(UShort.MIN, uint(12656));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_GetPosition =
          new NodeId(UShort.MIN, uint(12657));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_GetPosition_InputArguments =
          new NodeId(UShort.MIN, uint(12658));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_GetPosition_OutputArguments =
          new NodeId(UShort.MIN, uint(12659));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_SetPosition =
          new NodeId(UShort.MIN, uint(12660));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_SetPosition_InputArguments =
          new NodeId(UShort.MIN, uint(12661));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_LastUpdateTime =
          new NodeId(UShort.MIN, uint(12662));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_OpenWithMasks =
          new NodeId(UShort.MIN, uint(12663));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_OpenWithMasks_InputArguments =
          new NodeId(UShort.MIN, uint(12664));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_OpenWithMasks_OutputArguments =
          new NodeId(UShort.MIN, uint(12665));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_CloseAndUpdate =
          new NodeId(UShort.MIN, uint(12666));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_CloseAndUpdate_OutputArguments =
          new NodeId(UShort.MIN, uint(12667));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_AddCertificate =
          new NodeId(UShort.MIN, uint(12668));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_AddCertificate_InputArguments =
          new NodeId(UShort.MIN, uint(12669));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_RemoveCertificate =
          new NodeId(UShort.MIN, uint(12670));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_RemoveCertificate_InputArguments =
          new NodeId(UShort.MIN, uint(12671));

  public static final NodeId TrustListDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(12676));

  public static final NodeId OpcUa_XmlSchema_TrustListDataType =
      new NodeId(UShort.MIN, uint(12677));

  public static final NodeId OpcUa_XmlSchema_TrustListDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(12678));

  public static final NodeId OpcUa_XmlSchema_TrustListDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(12679));

  public static final NodeId TrustListDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(12680));

  public static final NodeId OpcUa_BinarySchema_TrustListDataType =
      new NodeId(UShort.MIN, uint(12681));

  public static final NodeId OpcUa_BinarySchema_TrustListDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(12682));

  public static final NodeId OpcUa_BinarySchema_TrustListDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(12683));

  public static final NodeId FileType_Writable = new NodeId(UShort.MIN, uint(12686));

  public static final NodeId FileType_UserWritable = new NodeId(UShort.MIN, uint(12687));

  public static final NodeId AddressSpaceFileType_Writable = new NodeId(UShort.MIN, uint(12688));

  public static final NodeId AddressSpaceFileType_UserWritable =
      new NodeId(UShort.MIN, uint(12689));

  public static final NodeId NamespaceMetadataType_NamespaceFile_Writable =
      new NodeId(UShort.MIN, uint(12690));

  public static final NodeId NamespaceMetadataType_NamespaceFile_UserWritable =
      new NodeId(UShort.MIN, uint(12691));

  public static final NodeId NamespacesType_NamespaceIdentifier_Placeholder_NamespaceFile_Writable =
      new NodeId(UShort.MIN, uint(12692));

  public static final NodeId
      NamespacesType_NamespaceIdentifier_Placeholder_NamespaceFile_UserWritable =
          new NodeId(UShort.MIN, uint(12693));

  public static final NodeId TrustListType_Writable = new NodeId(UShort.MIN, uint(12698));

  public static final NodeId TrustListType_UserWritable = new NodeId(UShort.MIN, uint(12699));

  public static final NodeId CloseAndUpdateMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(12704));

  public static final NodeId TrustListType_CloseAndUpdate_InputArguments =
      new NodeId(UShort.MIN, uint(12705));

  public static final NodeId ServerConfigurationType_ServerCapabilities =
      new NodeId(UShort.MIN, uint(12708));

  public static final NodeId ServerConfiguration_ServerCapabilities =
      new NodeId(UShort.MIN, uint(12710));

  public static final NodeId OpcUa_XmlSchema_RelativePathElement =
      new NodeId(UShort.MIN, uint(12712));

  public static final NodeId OpcUa_XmlSchema_RelativePathElement_DataTypeVersion =
      new NodeId(UShort.MIN, uint(12713));

  public static final NodeId OpcUa_XmlSchema_RelativePathElement_DictionaryFragment =
      new NodeId(UShort.MIN, uint(12714));

  public static final NodeId OpcUa_XmlSchema_RelativePath = new NodeId(UShort.MIN, uint(12715));

  public static final NodeId OpcUa_XmlSchema_RelativePath_DataTypeVersion =
      new NodeId(UShort.MIN, uint(12716));

  public static final NodeId OpcUa_XmlSchema_RelativePath_DictionaryFragment =
      new NodeId(UShort.MIN, uint(12717));

  public static final NodeId OpcUa_BinarySchema_RelativePathElement =
      new NodeId(UShort.MIN, uint(12718));

  public static final NodeId OpcUa_BinarySchema_RelativePathElement_DataTypeVersion =
      new NodeId(UShort.MIN, uint(12719));

  public static final NodeId OpcUa_BinarySchema_RelativePathElement_DictionaryFragment =
      new NodeId(UShort.MIN, uint(12720));

  public static final NodeId OpcUa_BinarySchema_RelativePath = new NodeId(UShort.MIN, uint(12721));

  public static final NodeId OpcUa_BinarySchema_RelativePath_DataTypeVersion =
      new NodeId(UShort.MIN, uint(12722));

  public static final NodeId OpcUa_BinarySchema_RelativePath_DictionaryFragment =
      new NodeId(UShort.MIN, uint(12723));

  public static final NodeId ServerConfigurationType_CreateSigningRequest =
      new NodeId(UShort.MIN, uint(12731));

  public static final NodeId ServerConfigurationType_CreateSigningRequest_InputArguments =
      new NodeId(UShort.MIN, uint(12732));

  public static final NodeId ServerConfigurationType_CreateSigningRequest_OutputArguments =
      new NodeId(UShort.MIN, uint(12733));

  public static final NodeId ServerConfigurationType_ApplyChanges =
      new NodeId(UShort.MIN, uint(12734));

  public static final NodeId ServerConfiguration_CreateSigningRequest =
      new NodeId(UShort.MIN, uint(12737));

  public static final NodeId ServerConfiguration_CreateSigningRequest_InputArguments =
      new NodeId(UShort.MIN, uint(12738));

  public static final NodeId ServerConfiguration_CreateSigningRequest_OutputArguments =
      new NodeId(UShort.MIN, uint(12739));

  public static final NodeId ServerConfiguration_ApplyChanges = new NodeId(UShort.MIN, uint(12740));

  public static final NodeId CreateSigningRequestMethodType = new NodeId(UShort.MIN, uint(12741));

  public static final NodeId CreateSigningRequestMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(12742));

  public static final NodeId CreateSigningRequestMethodType_OutputArguments =
      new NodeId(UShort.MIN, uint(12743));

  public static final NodeId OptionSetValues = new NodeId(UShort.MIN, uint(12745));

  public static final NodeId ServerType_SetSubscriptionDurable =
      new NodeId(UShort.MIN, uint(12746));

  public static final NodeId ServerType_SetSubscriptionDurable_InputArguments =
      new NodeId(UShort.MIN, uint(12747));

  public static final NodeId ServerType_SetSubscriptionDurable_OutputArguments =
      new NodeId(UShort.MIN, uint(12748));

  public static final NodeId Server_SetSubscriptionDurable = new NodeId(UShort.MIN, uint(12749));

  public static final NodeId Server_SetSubscriptionDurable_InputArguments =
      new NodeId(UShort.MIN, uint(12750));

  public static final NodeId Server_SetSubscriptionDurable_OutputArguments =
      new NodeId(UShort.MIN, uint(12751));

  public static final NodeId SetSubscriptionDurableMethodType = new NodeId(UShort.MIN, uint(12752));

  public static final NodeId SetSubscriptionDurableMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(12753));

  public static final NodeId SetSubscriptionDurableMethodType_OutputArguments =
      new NodeId(UShort.MIN, uint(12754));

  public static final NodeId OptionSet = new NodeId(UShort.MIN, uint(12755));

  public static final NodeId Union = new NodeId(UShort.MIN, uint(12756));

  public static final NodeId OptionSet_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(12757));

  public static final NodeId Union_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(12758));

  public static final NodeId OpcUa_XmlSchema_OptionSet = new NodeId(UShort.MIN, uint(12759));

  public static final NodeId OpcUa_XmlSchema_OptionSet_DataTypeVersion =
      new NodeId(UShort.MIN, uint(12760));

  public static final NodeId OpcUa_XmlSchema_OptionSet_DictionaryFragment =
      new NodeId(UShort.MIN, uint(12761));

  public static final NodeId OpcUa_XmlSchema_Union = new NodeId(UShort.MIN, uint(12762));

  public static final NodeId OpcUa_XmlSchema_Union_DataTypeVersion =
      new NodeId(UShort.MIN, uint(12763));

  public static final NodeId OpcUa_XmlSchema_Union_DictionaryFragment =
      new NodeId(UShort.MIN, uint(12764));

  public static final NodeId OptionSet_Encoding_DefaultBinary = new NodeId(UShort.MIN, uint(12765));

  public static final NodeId Union_Encoding_DefaultBinary = new NodeId(UShort.MIN, uint(12766));

  public static final NodeId OpcUa_BinarySchema_OptionSet = new NodeId(UShort.MIN, uint(12767));

  public static final NodeId OpcUa_BinarySchema_OptionSet_DataTypeVersion =
      new NodeId(UShort.MIN, uint(12768));

  public static final NodeId OpcUa_BinarySchema_OptionSet_DictionaryFragment =
      new NodeId(UShort.MIN, uint(12769));

  public static final NodeId OpcUa_BinarySchema_Union = new NodeId(UShort.MIN, uint(12770));

  public static final NodeId OpcUa_BinarySchema_Union_DataTypeVersion =
      new NodeId(UShort.MIN, uint(12771));

  public static final NodeId OpcUa_BinarySchema_Union_DictionaryFragment =
      new NodeId(UShort.MIN, uint(12772));

  public static final NodeId GetRejectedListMethodType = new NodeId(UShort.MIN, uint(12773));

  public static final NodeId GetRejectedListMethodType_OutputArguments =
      new NodeId(UShort.MIN, uint(12774));

  public static final NodeId ServerConfigurationType_GetRejectedList =
      new NodeId(UShort.MIN, uint(12775));

  public static final NodeId ServerConfigurationType_GetRejectedList_OutputArguments =
      new NodeId(UShort.MIN, uint(12776));

  public static final NodeId ServerConfiguration_GetRejectedList =
      new NodeId(UShort.MIN, uint(12777));

  public static final NodeId ServerConfiguration_GetRejectedList_OutputArguments =
      new NodeId(UShort.MIN, uint(12778));

  public static final NodeId SamplingIntervalDiagnosticsArrayType_SamplingIntervalDiagnostics =
      new NodeId(UShort.MIN, uint(12779));

  public static final NodeId
      SamplingIntervalDiagnosticsArrayType_SamplingIntervalDiagnostics_SamplingInterval =
          new NodeId(UShort.MIN, uint(12780));

  public static final NodeId
      SamplingIntervalDiagnosticsArrayType_SamplingIntervalDiagnostics_SampledMonitoredItemsCount =
          new NodeId(UShort.MIN, uint(12781));

  public static final NodeId
      SamplingIntervalDiagnosticsArrayType_SamplingIntervalDiagnostics_MaxSampledMonitoredItemsCount =
          new NodeId(UShort.MIN, uint(12782));

  public static final NodeId
      SamplingIntervalDiagnosticsArrayType_SamplingIntervalDiagnostics_DisabledMonitoredItemsSamplingCount =
          new NodeId(UShort.MIN, uint(12783));

  public static final NodeId SubscriptionDiagnosticsArrayType_SubscriptionDiagnostics =
      new NodeId(UShort.MIN, uint(12784));

  public static final NodeId SubscriptionDiagnosticsArrayType_SubscriptionDiagnostics_SessionId =
      new NodeId(UShort.MIN, uint(12785));

  public static final NodeId
      SubscriptionDiagnosticsArrayType_SubscriptionDiagnostics_SubscriptionId =
          new NodeId(UShort.MIN, uint(12786));

  public static final NodeId SubscriptionDiagnosticsArrayType_SubscriptionDiagnostics_Priority =
      new NodeId(UShort.MIN, uint(12787));

  public static final NodeId
      SubscriptionDiagnosticsArrayType_SubscriptionDiagnostics_PublishingInterval =
          new NodeId(UShort.MIN, uint(12788));

  public static final NodeId
      SubscriptionDiagnosticsArrayType_SubscriptionDiagnostics_MaxKeepAliveCount =
          new NodeId(UShort.MIN, uint(12789));

  public static final NodeId
      SubscriptionDiagnosticsArrayType_SubscriptionDiagnostics_MaxLifetimeCount =
          new NodeId(UShort.MIN, uint(12790));

  public static final NodeId
      SubscriptionDiagnosticsArrayType_SubscriptionDiagnostics_MaxNotificationsPerPublish =
          new NodeId(UShort.MIN, uint(12791));

  public static final NodeId
      SubscriptionDiagnosticsArrayType_SubscriptionDiagnostics_PublishingEnabled =
          new NodeId(UShort.MIN, uint(12792));

  public static final NodeId SubscriptionDiagnosticsArrayType_SubscriptionDiagnostics_ModifyCount =
      new NodeId(UShort.MIN, uint(12793));

  public static final NodeId SubscriptionDiagnosticsArrayType_SubscriptionDiagnostics_EnableCount =
      new NodeId(UShort.MIN, uint(12794));

  public static final NodeId SubscriptionDiagnosticsArrayType_SubscriptionDiagnostics_DisableCount =
      new NodeId(UShort.MIN, uint(12795));

  public static final NodeId
      SubscriptionDiagnosticsArrayType_SubscriptionDiagnostics_RepublishRequestCount =
          new NodeId(UShort.MIN, uint(12796));

  public static final NodeId
      SubscriptionDiagnosticsArrayType_SubscriptionDiagnostics_RepublishMessageRequestCount =
          new NodeId(UShort.MIN, uint(12797));

  public static final NodeId
      SubscriptionDiagnosticsArrayType_SubscriptionDiagnostics_RepublishMessageCount =
          new NodeId(UShort.MIN, uint(12798));

  public static final NodeId
      SubscriptionDiagnosticsArrayType_SubscriptionDiagnostics_TransferRequestCount =
          new NodeId(UShort.MIN, uint(12799));

  public static final NodeId
      SubscriptionDiagnosticsArrayType_SubscriptionDiagnostics_TransferredToAltClientCount =
          new NodeId(UShort.MIN, uint(12800));

  public static final NodeId
      SubscriptionDiagnosticsArrayType_SubscriptionDiagnostics_TransferredToSameClientCount =
          new NodeId(UShort.MIN, uint(12801));

  public static final NodeId
      SubscriptionDiagnosticsArrayType_SubscriptionDiagnostics_PublishRequestCount =
          new NodeId(UShort.MIN, uint(12802));

  public static final NodeId
      SubscriptionDiagnosticsArrayType_SubscriptionDiagnostics_DataChangeNotificationsCount =
          new NodeId(UShort.MIN, uint(12803));

  public static final NodeId
      SubscriptionDiagnosticsArrayType_SubscriptionDiagnostics_EventNotificationsCount =
          new NodeId(UShort.MIN, uint(12804));

  public static final NodeId
      SubscriptionDiagnosticsArrayType_SubscriptionDiagnostics_NotificationsCount =
          new NodeId(UShort.MIN, uint(12805));

  public static final NodeId
      SubscriptionDiagnosticsArrayType_SubscriptionDiagnostics_LatePublishRequestCount =
          new NodeId(UShort.MIN, uint(12806));

  public static final NodeId
      SubscriptionDiagnosticsArrayType_SubscriptionDiagnostics_CurrentKeepAliveCount =
          new NodeId(UShort.MIN, uint(12807));

  public static final NodeId
      SubscriptionDiagnosticsArrayType_SubscriptionDiagnostics_CurrentLifetimeCount =
          new NodeId(UShort.MIN, uint(12808));

  public static final NodeId
      SubscriptionDiagnosticsArrayType_SubscriptionDiagnostics_UnacknowledgedMessageCount =
          new NodeId(UShort.MIN, uint(12809));
}

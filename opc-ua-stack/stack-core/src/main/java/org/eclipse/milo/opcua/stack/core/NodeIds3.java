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

abstract class NodeIds3 extends NodeIds4 {
  public static final NodeId NonExclusiveLevelAlarmType_FirstInGroupFlag =
      new NodeId(UShort.MIN, uint(16684));

  public static final NodeId NonExclusiveLevelAlarmType_FirstInGroup =
      new NodeId(UShort.MIN, uint(16685));

  public static final NodeId NonExclusiveLevelAlarmType_AlarmGroup_Placeholder =
      new NodeId(UShort.MIN, uint(16686));

  public static final NodeId NonExclusiveLevelAlarmType_ReAlarmTime =
      new NodeId(UShort.MIN, uint(16687));

  public static final NodeId NonExclusiveLevelAlarmType_ReAlarmRepeatCount =
      new NodeId(UShort.MIN, uint(16688));

  public static final NodeId NonExclusiveLevelAlarmType_Silence =
      new NodeId(UShort.MIN, uint(16689));

  public static final NodeId NonExclusiveLevelAlarmType_Suppress =
      new NodeId(UShort.MIN, uint(16690));

  public static final NodeId AddConnectionMethodType = new NodeId(UShort.MIN, uint(16691));

  public static final NodeId NonExclusiveLevelAlarmType_BaseHighHighLimit =
      new NodeId(UShort.MIN, uint(16692));

  public static final NodeId NonExclusiveLevelAlarmType_BaseHighLimit =
      new NodeId(UShort.MIN, uint(16693));

  public static final NodeId NonExclusiveLevelAlarmType_BaseLowLimit =
      new NodeId(UShort.MIN, uint(16694));

  public static final NodeId NonExclusiveLevelAlarmType_BaseLowLowLimit =
      new NodeId(UShort.MIN, uint(16695));

  public static final NodeId ExclusiveLevelAlarmType_ConditionSubClassId =
      new NodeId(UShort.MIN, uint(16696));

  public static final NodeId ExclusiveLevelAlarmType_ConditionSubClassName =
      new NodeId(UShort.MIN, uint(16697));

  public static final NodeId ExclusiveLevelAlarmType_OutOfServiceState =
      new NodeId(UShort.MIN, uint(16698));

  public static final NodeId ExclusiveLevelAlarmType_OutOfServiceState_Id =
      new NodeId(UShort.MIN, uint(16699));

  public static final NodeId ExclusiveLevelAlarmType_OutOfServiceState_Name =
      new NodeId(UShort.MIN, uint(16700));

  public static final NodeId ExclusiveLevelAlarmType_OutOfServiceState_Number =
      new NodeId(UShort.MIN, uint(16701));

  public static final NodeId ExclusiveLevelAlarmType_OutOfServiceState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(16702));

  public static final NodeId ExclusiveLevelAlarmType_OutOfServiceState_TransitionTime =
      new NodeId(UShort.MIN, uint(16703));

  public static final NodeId ExclusiveLevelAlarmType_OutOfServiceState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(16704));

  public static final NodeId ExclusiveLevelAlarmType_OutOfServiceState_TrueState =
      new NodeId(UShort.MIN, uint(16705));

  public static final NodeId ExclusiveLevelAlarmType_OutOfServiceState_FalseState =
      new NodeId(UShort.MIN, uint(16706));

  public static final NodeId ExclusiveLevelAlarmType_SilenceState =
      new NodeId(UShort.MIN, uint(16707));

  public static final NodeId ExclusiveLevelAlarmType_SilenceState_Id =
      new NodeId(UShort.MIN, uint(16708));

  public static final NodeId ExclusiveLevelAlarmType_SilenceState_Name =
      new NodeId(UShort.MIN, uint(16709));

  public static final NodeId ExclusiveLevelAlarmType_SilenceState_Number =
      new NodeId(UShort.MIN, uint(16710));

  public static final NodeId ExclusiveLevelAlarmType_SilenceState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(16711));

  public static final NodeId ExclusiveLevelAlarmType_SilenceState_TransitionTime =
      new NodeId(UShort.MIN, uint(16712));

  public static final NodeId ExclusiveLevelAlarmType_SilenceState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(16713));

  public static final NodeId ExclusiveLevelAlarmType_SilenceState_TrueState =
      new NodeId(UShort.MIN, uint(16714));

  public static final NodeId ExclusiveLevelAlarmType_SilenceState_FalseState =
      new NodeId(UShort.MIN, uint(16715));

  public static final NodeId ExclusiveLevelAlarmType_AudibleEnabled =
      new NodeId(UShort.MIN, uint(16716));

  public static final NodeId ExclusiveLevelAlarmType_AudibleSound =
      new NodeId(UShort.MIN, uint(16717));

  public static final NodeId AddConnectionMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(16718));

  public static final NodeId AddConnectionMethodType_OutputArguments =
      new NodeId(UShort.MIN, uint(16719));

  public static final NodeId PublishedDataSetType_DataSetWriterName_Placeholder_DataSetWriterId =
      new NodeId(UShort.MIN, uint(16720));

  public static final NodeId
      PublishedDataSetType_DataSetWriterName_Placeholder_DataSetFieldContentMask =
          new NodeId(UShort.MIN, uint(16721));

  public static final NodeId ExclusiveLevelAlarmType_OnDelay = new NodeId(UShort.MIN, uint(16722));

  public static final NodeId ExclusiveLevelAlarmType_OffDelay = new NodeId(UShort.MIN, uint(16723));

  public static final NodeId ExclusiveLevelAlarmType_FirstInGroupFlag =
      new NodeId(UShort.MIN, uint(16724));

  public static final NodeId ExclusiveLevelAlarmType_FirstInGroup =
      new NodeId(UShort.MIN, uint(16725));

  public static final NodeId ExclusiveLevelAlarmType_AlarmGroup_Placeholder =
      new NodeId(UShort.MIN, uint(16726));

  public static final NodeId ExclusiveLevelAlarmType_ReAlarmTime =
      new NodeId(UShort.MIN, uint(16727));

  public static final NodeId ExclusiveLevelAlarmType_ReAlarmRepeatCount =
      new NodeId(UShort.MIN, uint(16728));

  public static final NodeId ExclusiveLevelAlarmType_Silence = new NodeId(UShort.MIN, uint(16729));

  public static final NodeId ExclusiveLevelAlarmType_Suppress = new NodeId(UShort.MIN, uint(16730));

  public static final NodeId PublishedDataSetType_DataSetWriterName_Placeholder_KeyFrameCount =
      new NodeId(UShort.MIN, uint(16731));

  public static final NodeId ExclusiveLevelAlarmType_BaseHighHighLimit =
      new NodeId(UShort.MIN, uint(16732));

  public static final NodeId ExclusiveLevelAlarmType_BaseHighLimit =
      new NodeId(UShort.MIN, uint(16733));

  public static final NodeId ExclusiveLevelAlarmType_BaseLowLimit =
      new NodeId(UShort.MIN, uint(16734));

  public static final NodeId ExclusiveLevelAlarmType_BaseLowLowLimit =
      new NodeId(UShort.MIN, uint(16735));

  public static final NodeId NonExclusiveDeviationAlarmType_ConditionSubClassId =
      new NodeId(UShort.MIN, uint(16736));

  public static final NodeId NonExclusiveDeviationAlarmType_ConditionSubClassName =
      new NodeId(UShort.MIN, uint(16737));

  public static final NodeId NonExclusiveDeviationAlarmType_OutOfServiceState =
      new NodeId(UShort.MIN, uint(16738));

  public static final NodeId NonExclusiveDeviationAlarmType_OutOfServiceState_Id =
      new NodeId(UShort.MIN, uint(16739));

  public static final NodeId NonExclusiveDeviationAlarmType_OutOfServiceState_Name =
      new NodeId(UShort.MIN, uint(16740));

  public static final NodeId NonExclusiveDeviationAlarmType_OutOfServiceState_Number =
      new NodeId(UShort.MIN, uint(16741));

  public static final NodeId NonExclusiveDeviationAlarmType_OutOfServiceState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(16742));

  public static final NodeId NonExclusiveDeviationAlarmType_OutOfServiceState_TransitionTime =
      new NodeId(UShort.MIN, uint(16743));

  public static final NodeId
      NonExclusiveDeviationAlarmType_OutOfServiceState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(16744));

  public static final NodeId NonExclusiveDeviationAlarmType_OutOfServiceState_TrueState =
      new NodeId(UShort.MIN, uint(16745));

  public static final NodeId NonExclusiveDeviationAlarmType_OutOfServiceState_FalseState =
      new NodeId(UShort.MIN, uint(16746));

  public static final NodeId NonExclusiveDeviationAlarmType_SilenceState =
      new NodeId(UShort.MIN, uint(16747));

  public static final NodeId NonExclusiveDeviationAlarmType_SilenceState_Id =
      new NodeId(UShort.MIN, uint(16748));

  public static final NodeId NonExclusiveDeviationAlarmType_SilenceState_Name =
      new NodeId(UShort.MIN, uint(16749));

  public static final NodeId NonExclusiveDeviationAlarmType_SilenceState_Number =
      new NodeId(UShort.MIN, uint(16750));

  public static final NodeId NonExclusiveDeviationAlarmType_SilenceState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(16751));

  public static final NodeId NonExclusiveDeviationAlarmType_SilenceState_TransitionTime =
      new NodeId(UShort.MIN, uint(16752));

  public static final NodeId NonExclusiveDeviationAlarmType_SilenceState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(16753));

  public static final NodeId NonExclusiveDeviationAlarmType_SilenceState_TrueState =
      new NodeId(UShort.MIN, uint(16754));

  public static final NodeId NonExclusiveDeviationAlarmType_SilenceState_FalseState =
      new NodeId(UShort.MIN, uint(16755));

  public static final NodeId NonExclusiveDeviationAlarmType_AudibleEnabled =
      new NodeId(UShort.MIN, uint(16756));

  public static final NodeId NonExclusiveDeviationAlarmType_AudibleSound =
      new NodeId(UShort.MIN, uint(16757));

  public static final NodeId PublishedDataSetType_DataSetWriterName_Placeholder_MessageSettings =
      new NodeId(UShort.MIN, uint(16758));

  public static final NodeId PublishedDataSetType_DataSetClassId =
      new NodeId(UShort.MIN, uint(16759));

  public static final NodeId PublishedDataItemsType_DataSetWriterName_Placeholder_DataSetWriterId =
      new NodeId(UShort.MIN, uint(16760));

  public static final NodeId
      PublishedDataItemsType_DataSetWriterName_Placeholder_DataSetFieldContentMask =
          new NodeId(UShort.MIN, uint(16761));

  public static final NodeId NonExclusiveDeviationAlarmType_OnDelay =
      new NodeId(UShort.MIN, uint(16762));

  public static final NodeId NonExclusiveDeviationAlarmType_OffDelay =
      new NodeId(UShort.MIN, uint(16763));

  public static final NodeId NonExclusiveDeviationAlarmType_FirstInGroupFlag =
      new NodeId(UShort.MIN, uint(16764));

  public static final NodeId NonExclusiveDeviationAlarmType_FirstInGroup =
      new NodeId(UShort.MIN, uint(16765));

  public static final NodeId NonExclusiveDeviationAlarmType_AlarmGroup_Placeholder =
      new NodeId(UShort.MIN, uint(16766));

  public static final NodeId NonExclusiveDeviationAlarmType_ReAlarmTime =
      new NodeId(UShort.MIN, uint(16767));

  public static final NodeId NonExclusiveDeviationAlarmType_ReAlarmRepeatCount =
      new NodeId(UShort.MIN, uint(16768));

  public static final NodeId NonExclusiveDeviationAlarmType_Silence =
      new NodeId(UShort.MIN, uint(16769));

  public static final NodeId NonExclusiveDeviationAlarmType_Suppress =
      new NodeId(UShort.MIN, uint(16770));

  public static final NodeId PublishedDataItemsType_DataSetWriterName_Placeholder_KeyFrameCount =
      new NodeId(UShort.MIN, uint(16771));

  public static final NodeId NonExclusiveDeviationAlarmType_BaseHighHighLimit =
      new NodeId(UShort.MIN, uint(16772));

  public static final NodeId NonExclusiveDeviationAlarmType_BaseHighLimit =
      new NodeId(UShort.MIN, uint(16773));

  public static final NodeId NonExclusiveDeviationAlarmType_BaseLowLimit =
      new NodeId(UShort.MIN, uint(16774));

  public static final NodeId NonExclusiveDeviationAlarmType_BaseLowLowLimit =
      new NodeId(UShort.MIN, uint(16775));

  public static final NodeId NonExclusiveDeviationAlarmType_BaseSetpointNode =
      new NodeId(UShort.MIN, uint(16776));

  public static final NodeId ExclusiveDeviationAlarmType_ConditionSubClassId =
      new NodeId(UShort.MIN, uint(16777));

  public static final NodeId ExclusiveDeviationAlarmType_ConditionSubClassName =
      new NodeId(UShort.MIN, uint(16778));

  public static final NodeId ExclusiveDeviationAlarmType_OutOfServiceState =
      new NodeId(UShort.MIN, uint(16779));

  public static final NodeId ExclusiveDeviationAlarmType_OutOfServiceState_Id =
      new NodeId(UShort.MIN, uint(16780));

  public static final NodeId ExclusiveDeviationAlarmType_OutOfServiceState_Name =
      new NodeId(UShort.MIN, uint(16781));

  public static final NodeId ExclusiveDeviationAlarmType_OutOfServiceState_Number =
      new NodeId(UShort.MIN, uint(16782));

  public static final NodeId ExclusiveDeviationAlarmType_OutOfServiceState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(16783));

  public static final NodeId ExclusiveDeviationAlarmType_OutOfServiceState_TransitionTime =
      new NodeId(UShort.MIN, uint(16784));

  public static final NodeId ExclusiveDeviationAlarmType_OutOfServiceState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(16785));

  public static final NodeId ExclusiveDeviationAlarmType_OutOfServiceState_TrueState =
      new NodeId(UShort.MIN, uint(16786));

  public static final NodeId ExclusiveDeviationAlarmType_OutOfServiceState_FalseState =
      new NodeId(UShort.MIN, uint(16787));

  public static final NodeId ExclusiveDeviationAlarmType_SilenceState =
      new NodeId(UShort.MIN, uint(16788));

  public static final NodeId ExclusiveDeviationAlarmType_SilenceState_Id =
      new NodeId(UShort.MIN, uint(16789));

  public static final NodeId ExclusiveDeviationAlarmType_SilenceState_Name =
      new NodeId(UShort.MIN, uint(16790));

  public static final NodeId ExclusiveDeviationAlarmType_SilenceState_Number =
      new NodeId(UShort.MIN, uint(16791));

  public static final NodeId ExclusiveDeviationAlarmType_SilenceState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(16792));

  public static final NodeId ExclusiveDeviationAlarmType_SilenceState_TransitionTime =
      new NodeId(UShort.MIN, uint(16793));

  public static final NodeId ExclusiveDeviationAlarmType_SilenceState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(16794));

  public static final NodeId ExclusiveDeviationAlarmType_SilenceState_TrueState =
      new NodeId(UShort.MIN, uint(16795));

  public static final NodeId ExclusiveDeviationAlarmType_SilenceState_FalseState =
      new NodeId(UShort.MIN, uint(16796));

  public static final NodeId ExclusiveDeviationAlarmType_AudibleEnabled =
      new NodeId(UShort.MIN, uint(16797));

  public static final NodeId ExclusiveDeviationAlarmType_AudibleSound =
      new NodeId(UShort.MIN, uint(16798));

  public static final NodeId PublishedDataItemsType_DataSetWriterName_Placeholder_MessageSettings =
      new NodeId(UShort.MIN, uint(16799));

  public static final NodeId PublishedDataItemsType_DataSetClassId =
      new NodeId(UShort.MIN, uint(16800));

  public static final NodeId PublishedEventsType_DataSetWriterName_Placeholder_DataSetWriterId =
      new NodeId(UShort.MIN, uint(16801));

  public static final NodeId
      PublishedEventsType_DataSetWriterName_Placeholder_DataSetFieldContentMask =
          new NodeId(UShort.MIN, uint(16802));

  public static final NodeId ExclusiveDeviationAlarmType_OnDelay =
      new NodeId(UShort.MIN, uint(16803));

  public static final NodeId ExclusiveDeviationAlarmType_OffDelay =
      new NodeId(UShort.MIN, uint(16804));

  public static final NodeId ExclusiveDeviationAlarmType_FirstInGroupFlag =
      new NodeId(UShort.MIN, uint(16805));

  public static final NodeId ExclusiveDeviationAlarmType_FirstInGroup =
      new NodeId(UShort.MIN, uint(16806));

  public static final NodeId ExclusiveDeviationAlarmType_AlarmGroup_Placeholder =
      new NodeId(UShort.MIN, uint(16807));

  public static final NodeId ExclusiveDeviationAlarmType_ReAlarmTime =
      new NodeId(UShort.MIN, uint(16808));

  public static final NodeId ExclusiveDeviationAlarmType_ReAlarmRepeatCount =
      new NodeId(UShort.MIN, uint(16809));

  public static final NodeId ExclusiveDeviationAlarmType_Silence =
      new NodeId(UShort.MIN, uint(16810));

  public static final NodeId ExclusiveDeviationAlarmType_Suppress =
      new NodeId(UShort.MIN, uint(16811));

  public static final NodeId PublishedEventsType_DataSetWriterName_Placeholder_KeyFrameCount =
      new NodeId(UShort.MIN, uint(16812));

  public static final NodeId ExclusiveDeviationAlarmType_BaseHighHighLimit =
      new NodeId(UShort.MIN, uint(16813));

  public static final NodeId ExclusiveDeviationAlarmType_BaseHighLimit =
      new NodeId(UShort.MIN, uint(16814));

  public static final NodeId ExclusiveDeviationAlarmType_BaseLowLimit =
      new NodeId(UShort.MIN, uint(16815));

  public static final NodeId ExclusiveDeviationAlarmType_BaseLowLowLimit =
      new NodeId(UShort.MIN, uint(16816));

  public static final NodeId ExclusiveDeviationAlarmType_BaseSetpointNode =
      new NodeId(UShort.MIN, uint(16817));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_ConditionSubClassId =
      new NodeId(UShort.MIN, uint(16818));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_ConditionSubClassName =
      new NodeId(UShort.MIN, uint(16819));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_OutOfServiceState =
      new NodeId(UShort.MIN, uint(16820));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_OutOfServiceState_Id =
      new NodeId(UShort.MIN, uint(16821));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_OutOfServiceState_Name =
      new NodeId(UShort.MIN, uint(16822));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_OutOfServiceState_Number =
      new NodeId(UShort.MIN, uint(16823));

  public static final NodeId
      NonExclusiveRateOfChangeAlarmType_OutOfServiceState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(16824));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_OutOfServiceState_TransitionTime =
      new NodeId(UShort.MIN, uint(16825));

  public static final NodeId
      NonExclusiveRateOfChangeAlarmType_OutOfServiceState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(16826));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_OutOfServiceState_TrueState =
      new NodeId(UShort.MIN, uint(16827));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_OutOfServiceState_FalseState =
      new NodeId(UShort.MIN, uint(16828));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_SilenceState =
      new NodeId(UShort.MIN, uint(16829));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_SilenceState_Id =
      new NodeId(UShort.MIN, uint(16830));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_SilenceState_Name =
      new NodeId(UShort.MIN, uint(16831));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_SilenceState_Number =
      new NodeId(UShort.MIN, uint(16832));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_SilenceState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(16833));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_SilenceState_TransitionTime =
      new NodeId(UShort.MIN, uint(16834));

  public static final NodeId
      NonExclusiveRateOfChangeAlarmType_SilenceState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(16835));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_SilenceState_TrueState =
      new NodeId(UShort.MIN, uint(16836));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_SilenceState_FalseState =
      new NodeId(UShort.MIN, uint(16837));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_AudibleEnabled =
      new NodeId(UShort.MIN, uint(16838));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_AudibleSound =
      new NodeId(UShort.MIN, uint(16839));

  public static final NodeId PublishedEventsType_DataSetWriterName_Placeholder_MessageSettings =
      new NodeId(UShort.MIN, uint(16840));

  public static final NodeId PublishedEventsType_DataSetClassId =
      new NodeId(UShort.MIN, uint(16841));

  public static final NodeId
      DataSetFolderType_DataSetFolderName_Placeholder_AddPublishedDataItemsTemplate =
          new NodeId(UShort.MIN, uint(16842));

  public static final NodeId
      DataSetFolderType_DataSetFolderName_Placeholder_AddPublishedDataItemsTemplate_InputArguments =
          new NodeId(UShort.MIN, uint(16843));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_OnDelay =
      new NodeId(UShort.MIN, uint(16844));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_OffDelay =
      new NodeId(UShort.MIN, uint(16845));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_FirstInGroupFlag =
      new NodeId(UShort.MIN, uint(16846));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_FirstInGroup =
      new NodeId(UShort.MIN, uint(16847));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_AlarmGroup_Placeholder =
      new NodeId(UShort.MIN, uint(16848));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_ReAlarmTime =
      new NodeId(UShort.MIN, uint(16849));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_ReAlarmRepeatCount =
      new NodeId(UShort.MIN, uint(16850));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_Silence =
      new NodeId(UShort.MIN, uint(16851));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_Suppress =
      new NodeId(UShort.MIN, uint(16852));

  public static final NodeId
      DataSetFolderType_DataSetFolderName_Placeholder_AddPublishedDataItemsTemplate_OutputArguments =
          new NodeId(UShort.MIN, uint(16853));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_BaseHighHighLimit =
      new NodeId(UShort.MIN, uint(16854));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_BaseHighLimit =
      new NodeId(UShort.MIN, uint(16855));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_BaseLowLimit =
      new NodeId(UShort.MIN, uint(16856));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_BaseLowLowLimit =
      new NodeId(UShort.MIN, uint(16857));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_EngineeringUnits =
      new NodeId(UShort.MIN, uint(16858));

  public static final NodeId ExclusiveRateOfChangeAlarmType_ConditionSubClassId =
      new NodeId(UShort.MIN, uint(16859));

  public static final NodeId ExclusiveRateOfChangeAlarmType_ConditionSubClassName =
      new NodeId(UShort.MIN, uint(16860));

  public static final NodeId ExclusiveRateOfChangeAlarmType_OutOfServiceState =
      new NodeId(UShort.MIN, uint(16861));

  public static final NodeId ExclusiveRateOfChangeAlarmType_OutOfServiceState_Id =
      new NodeId(UShort.MIN, uint(16862));

  public static final NodeId ExclusiveRateOfChangeAlarmType_OutOfServiceState_Name =
      new NodeId(UShort.MIN, uint(16863));

  public static final NodeId ExclusiveRateOfChangeAlarmType_OutOfServiceState_Number =
      new NodeId(UShort.MIN, uint(16864));

  public static final NodeId ExclusiveRateOfChangeAlarmType_OutOfServiceState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(16865));

  public static final NodeId ExclusiveRateOfChangeAlarmType_OutOfServiceState_TransitionTime =
      new NodeId(UShort.MIN, uint(16866));

  public static final NodeId
      ExclusiveRateOfChangeAlarmType_OutOfServiceState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(16867));

  public static final NodeId ExclusiveRateOfChangeAlarmType_OutOfServiceState_TrueState =
      new NodeId(UShort.MIN, uint(16868));

  public static final NodeId ExclusiveRateOfChangeAlarmType_OutOfServiceState_FalseState =
      new NodeId(UShort.MIN, uint(16869));

  public static final NodeId ExclusiveRateOfChangeAlarmType_SilenceState =
      new NodeId(UShort.MIN, uint(16870));

  public static final NodeId ExclusiveRateOfChangeAlarmType_SilenceState_Id =
      new NodeId(UShort.MIN, uint(16871));

  public static final NodeId ExclusiveRateOfChangeAlarmType_SilenceState_Name =
      new NodeId(UShort.MIN, uint(16872));

  public static final NodeId ExclusiveRateOfChangeAlarmType_SilenceState_Number =
      new NodeId(UShort.MIN, uint(16873));

  public static final NodeId ExclusiveRateOfChangeAlarmType_SilenceState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(16874));

  public static final NodeId ExclusiveRateOfChangeAlarmType_SilenceState_TransitionTime =
      new NodeId(UShort.MIN, uint(16875));

  public static final NodeId ExclusiveRateOfChangeAlarmType_SilenceState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(16876));

  public static final NodeId ExclusiveRateOfChangeAlarmType_SilenceState_TrueState =
      new NodeId(UShort.MIN, uint(16877));

  public static final NodeId ExclusiveRateOfChangeAlarmType_SilenceState_FalseState =
      new NodeId(UShort.MIN, uint(16878));

  public static final NodeId ExclusiveRateOfChangeAlarmType_AudibleEnabled =
      new NodeId(UShort.MIN, uint(16879));

  public static final NodeId ExclusiveRateOfChangeAlarmType_AudibleSound =
      new NodeId(UShort.MIN, uint(16880));

  public static final NodeId
      DataSetFolderType_DataSetFolderName_Placeholder_AddPublishedEventsTemplate =
          new NodeId(UShort.MIN, uint(16881));

  public static final NodeId
      DataSetFolderType_DataSetFolderName_Placeholder_AddPublishedEventsTemplate_InputArguments =
          new NodeId(UShort.MIN, uint(16882));

  public static final NodeId
      DataSetFolderType_DataSetFolderName_Placeholder_AddPublishedEventsTemplate_OutputArguments =
          new NodeId(UShort.MIN, uint(16883));

  public static final NodeId DataSetFolderType_DataSetFolderName_Placeholder_AddDataSetFolder =
      new NodeId(UShort.MIN, uint(16884));

  public static final NodeId ExclusiveRateOfChangeAlarmType_OnDelay =
      new NodeId(UShort.MIN, uint(16885));

  public static final NodeId ExclusiveRateOfChangeAlarmType_OffDelay =
      new NodeId(UShort.MIN, uint(16886));

  public static final NodeId ExclusiveRateOfChangeAlarmType_FirstInGroupFlag =
      new NodeId(UShort.MIN, uint(16887));

  public static final NodeId ExclusiveRateOfChangeAlarmType_FirstInGroup =
      new NodeId(UShort.MIN, uint(16888));

  public static final NodeId ExclusiveRateOfChangeAlarmType_AlarmGroup_Placeholder =
      new NodeId(UShort.MIN, uint(16889));

  public static final NodeId ExclusiveRateOfChangeAlarmType_ReAlarmTime =
      new NodeId(UShort.MIN, uint(16890));

  public static final NodeId ExclusiveRateOfChangeAlarmType_ReAlarmRepeatCount =
      new NodeId(UShort.MIN, uint(16891));

  public static final NodeId ExclusiveRateOfChangeAlarmType_Silence =
      new NodeId(UShort.MIN, uint(16892));

  public static final NodeId ExclusiveRateOfChangeAlarmType_Suppress =
      new NodeId(UShort.MIN, uint(16893));

  public static final NodeId
      DataSetFolderType_DataSetFolderName_Placeholder_AddDataSetFolder_InputArguments =
          new NodeId(UShort.MIN, uint(16894));

  public static final NodeId ExclusiveRateOfChangeAlarmType_BaseHighHighLimit =
      new NodeId(UShort.MIN, uint(16895));

  public static final NodeId ExclusiveRateOfChangeAlarmType_BaseHighLimit =
      new NodeId(UShort.MIN, uint(16896));

  public static final NodeId ExclusiveRateOfChangeAlarmType_BaseLowLimit =
      new NodeId(UShort.MIN, uint(16897));

  public static final NodeId ExclusiveRateOfChangeAlarmType_BaseLowLowLimit =
      new NodeId(UShort.MIN, uint(16898));

  public static final NodeId ExclusiveRateOfChangeAlarmType_EngineeringUnits =
      new NodeId(UShort.MIN, uint(16899));

  public static final NodeId DiscreteAlarmType_ConditionSubClassId =
      new NodeId(UShort.MIN, uint(16900));

  public static final NodeId DiscreteAlarmType_ConditionSubClassName =
      new NodeId(UShort.MIN, uint(16901));

  public static final NodeId DiscreteAlarmType_OutOfServiceState =
      new NodeId(UShort.MIN, uint(16902));

  public static final NodeId DiscreteAlarmType_OutOfServiceState_Id =
      new NodeId(UShort.MIN, uint(16903));

  public static final NodeId DiscreteAlarmType_OutOfServiceState_Name =
      new NodeId(UShort.MIN, uint(16904));

  public static final NodeId DiscreteAlarmType_OutOfServiceState_Number =
      new NodeId(UShort.MIN, uint(16905));

  public static final NodeId DiscreteAlarmType_OutOfServiceState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(16906));

  public static final NodeId DiscreteAlarmType_OutOfServiceState_TransitionTime =
      new NodeId(UShort.MIN, uint(16907));

  public static final NodeId DiscreteAlarmType_OutOfServiceState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(16908));

  public static final NodeId DiscreteAlarmType_OutOfServiceState_TrueState =
      new NodeId(UShort.MIN, uint(16909));

  public static final NodeId DiscreteAlarmType_OutOfServiceState_FalseState =
      new NodeId(UShort.MIN, uint(16910));

  public static final NodeId DiscreteAlarmType_SilenceState = new NodeId(UShort.MIN, uint(16911));

  public static final NodeId DiscreteAlarmType_SilenceState_Id =
      new NodeId(UShort.MIN, uint(16912));

  public static final NodeId DiscreteAlarmType_SilenceState_Name =
      new NodeId(UShort.MIN, uint(16913));

  public static final NodeId DiscreteAlarmType_SilenceState_Number =
      new NodeId(UShort.MIN, uint(16914));

  public static final NodeId DiscreteAlarmType_SilenceState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(16915));

  public static final NodeId DiscreteAlarmType_SilenceState_TransitionTime =
      new NodeId(UShort.MIN, uint(16916));

  public static final NodeId DiscreteAlarmType_SilenceState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(16917));

  public static final NodeId DiscreteAlarmType_SilenceState_TrueState =
      new NodeId(UShort.MIN, uint(16918));

  public static final NodeId DiscreteAlarmType_SilenceState_FalseState =
      new NodeId(UShort.MIN, uint(16919));

  public static final NodeId DiscreteAlarmType_AudibleEnabled = new NodeId(UShort.MIN, uint(16920));

  public static final NodeId DiscreteAlarmType_AudibleSound = new NodeId(UShort.MIN, uint(16921));

  public static final NodeId
      DataSetFolderType_DataSetFolderName_Placeholder_AddDataSetFolder_OutputArguments =
          new NodeId(UShort.MIN, uint(16922));

  public static final NodeId DataSetFolderType_DataSetFolderName_Placeholder_RemoveDataSetFolder =
      new NodeId(UShort.MIN, uint(16923));

  public static final NodeId
      DataSetFolderType_DataSetFolderName_Placeholder_RemoveDataSetFolder_InputArguments =
          new NodeId(UShort.MIN, uint(16924));

  public static final NodeId DataSetFolderType_PublishedDataSetName_Placeholder_DataSetClassId =
      new NodeId(UShort.MIN, uint(16925));

  public static final NodeId DiscreteAlarmType_OnDelay = new NodeId(UShort.MIN, uint(16926));

  public static final NodeId DiscreteAlarmType_OffDelay = new NodeId(UShort.MIN, uint(16927));

  public static final NodeId DiscreteAlarmType_FirstInGroupFlag =
      new NodeId(UShort.MIN, uint(16928));

  public static final NodeId DiscreteAlarmType_FirstInGroup = new NodeId(UShort.MIN, uint(16929));

  public static final NodeId DiscreteAlarmType_AlarmGroup_Placeholder =
      new NodeId(UShort.MIN, uint(16930));

  public static final NodeId DiscreteAlarmType_ReAlarmTime = new NodeId(UShort.MIN, uint(16931));

  public static final NodeId DiscreteAlarmType_ReAlarmRepeatCount =
      new NodeId(UShort.MIN, uint(16932));

  public static final NodeId DiscreteAlarmType_Silence = new NodeId(UShort.MIN, uint(16933));

  public static final NodeId DiscreteAlarmType_Suppress = new NodeId(UShort.MIN, uint(16934));

  public static final NodeId DataSetFolderType_AddPublishedDataItemsTemplate =
      new NodeId(UShort.MIN, uint(16935));

  public static final NodeId OffNormalAlarmType_ConditionSubClassId =
      new NodeId(UShort.MIN, uint(16936));

  public static final NodeId OffNormalAlarmType_ConditionSubClassName =
      new NodeId(UShort.MIN, uint(16937));

  public static final NodeId OffNormalAlarmType_OutOfServiceState =
      new NodeId(UShort.MIN, uint(16938));

  public static final NodeId OffNormalAlarmType_OutOfServiceState_Id =
      new NodeId(UShort.MIN, uint(16939));

  public static final NodeId OffNormalAlarmType_OutOfServiceState_Name =
      new NodeId(UShort.MIN, uint(16940));

  public static final NodeId OffNormalAlarmType_OutOfServiceState_Number =
      new NodeId(UShort.MIN, uint(16941));

  public static final NodeId OffNormalAlarmType_OutOfServiceState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(16942));

  public static final NodeId OffNormalAlarmType_OutOfServiceState_TransitionTime =
      new NodeId(UShort.MIN, uint(16943));

  public static final NodeId OffNormalAlarmType_OutOfServiceState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(16944));

  public static final NodeId OffNormalAlarmType_OutOfServiceState_TrueState =
      new NodeId(UShort.MIN, uint(16945));

  public static final NodeId OffNormalAlarmType_OutOfServiceState_FalseState =
      new NodeId(UShort.MIN, uint(16946));

  public static final NodeId OffNormalAlarmType_SilenceState = new NodeId(UShort.MIN, uint(16947));

  public static final NodeId OffNormalAlarmType_SilenceState_Id =
      new NodeId(UShort.MIN, uint(16948));

  public static final NodeId OffNormalAlarmType_SilenceState_Name =
      new NodeId(UShort.MIN, uint(16949));

  public static final NodeId OffNormalAlarmType_SilenceState_Number =
      new NodeId(UShort.MIN, uint(16950));

  public static final NodeId OffNormalAlarmType_SilenceState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(16951));

  public static final NodeId OffNormalAlarmType_SilenceState_TransitionTime =
      new NodeId(UShort.MIN, uint(16952));

  public static final NodeId OffNormalAlarmType_SilenceState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(16953));

  public static final NodeId OffNormalAlarmType_SilenceState_TrueState =
      new NodeId(UShort.MIN, uint(16954));

  public static final NodeId OffNormalAlarmType_SilenceState_FalseState =
      new NodeId(UShort.MIN, uint(16955));

  public static final NodeId OffNormalAlarmType_AudibleEnabled =
      new NodeId(UShort.MIN, uint(16956));

  public static final NodeId OffNormalAlarmType_AudibleSound = new NodeId(UShort.MIN, uint(16957));

  public static final NodeId DataSetFolderType_AddPublishedDataItemsTemplate_InputArguments =
      new NodeId(UShort.MIN, uint(16958));

  public static final NodeId DataSetFolderType_AddPublishedDataItemsTemplate_OutputArguments =
      new NodeId(UShort.MIN, uint(16959));

  public static final NodeId DataSetFolderType_AddPublishedEventsTemplate =
      new NodeId(UShort.MIN, uint(16960));

  public static final NodeId DataSetFolderType_AddPublishedEventsTemplate_InputArguments =
      new NodeId(UShort.MIN, uint(16961));

  public static final NodeId OffNormalAlarmType_OnDelay = new NodeId(UShort.MIN, uint(16962));

  public static final NodeId OffNormalAlarmType_OffDelay = new NodeId(UShort.MIN, uint(16963));

  public static final NodeId OffNormalAlarmType_FirstInGroupFlag =
      new NodeId(UShort.MIN, uint(16964));

  public static final NodeId OffNormalAlarmType_FirstInGroup = new NodeId(UShort.MIN, uint(16965));

  public static final NodeId OffNormalAlarmType_AlarmGroup_Placeholder =
      new NodeId(UShort.MIN, uint(16966));

  public static final NodeId OffNormalAlarmType_ReAlarmTime = new NodeId(UShort.MIN, uint(16967));

  public static final NodeId OffNormalAlarmType_ReAlarmRepeatCount =
      new NodeId(UShort.MIN, uint(16968));

  public static final NodeId OffNormalAlarmType_Silence = new NodeId(UShort.MIN, uint(16969));

  public static final NodeId OffNormalAlarmType_Suppress = new NodeId(UShort.MIN, uint(16970));

  public static final NodeId DataSetFolderType_AddPublishedEventsTemplate_OutputArguments =
      new NodeId(UShort.MIN, uint(16971));

  public static final NodeId SystemOffNormalAlarmType_ConditionSubClassId =
      new NodeId(UShort.MIN, uint(16972));

  public static final NodeId SystemOffNormalAlarmType_ConditionSubClassName =
      new NodeId(UShort.MIN, uint(16973));

  public static final NodeId SystemOffNormalAlarmType_OutOfServiceState =
      new NodeId(UShort.MIN, uint(16974));

  public static final NodeId SystemOffNormalAlarmType_OutOfServiceState_Id =
      new NodeId(UShort.MIN, uint(16975));

  public static final NodeId SystemOffNormalAlarmType_OutOfServiceState_Name =
      new NodeId(UShort.MIN, uint(16976));

  public static final NodeId SystemOffNormalAlarmType_OutOfServiceState_Number =
      new NodeId(UShort.MIN, uint(16977));

  public static final NodeId SystemOffNormalAlarmType_OutOfServiceState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(16978));

  public static final NodeId SystemOffNormalAlarmType_OutOfServiceState_TransitionTime =
      new NodeId(UShort.MIN, uint(16979));

  public static final NodeId SystemOffNormalAlarmType_OutOfServiceState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(16980));

  public static final NodeId SystemOffNormalAlarmType_OutOfServiceState_TrueState =
      new NodeId(UShort.MIN, uint(16981));

  public static final NodeId SystemOffNormalAlarmType_OutOfServiceState_FalseState =
      new NodeId(UShort.MIN, uint(16982));

  public static final NodeId SystemOffNormalAlarmType_SilenceState =
      new NodeId(UShort.MIN, uint(16983));

  public static final NodeId SystemOffNormalAlarmType_SilenceState_Id =
      new NodeId(UShort.MIN, uint(16984));

  public static final NodeId SystemOffNormalAlarmType_SilenceState_Name =
      new NodeId(UShort.MIN, uint(16985));

  public static final NodeId SystemOffNormalAlarmType_SilenceState_Number =
      new NodeId(UShort.MIN, uint(16986));

  public static final NodeId SystemOffNormalAlarmType_SilenceState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(16987));

  public static final NodeId SystemOffNormalAlarmType_SilenceState_TransitionTime =
      new NodeId(UShort.MIN, uint(16988));

  public static final NodeId SystemOffNormalAlarmType_SilenceState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(16989));

  public static final NodeId SystemOffNormalAlarmType_SilenceState_TrueState =
      new NodeId(UShort.MIN, uint(16990));

  public static final NodeId SystemOffNormalAlarmType_SilenceState_FalseState =
      new NodeId(UShort.MIN, uint(16991));

  public static final NodeId SystemOffNormalAlarmType_AudibleEnabled =
      new NodeId(UShort.MIN, uint(16992));

  public static final NodeId SystemOffNormalAlarmType_AudibleSound =
      new NodeId(UShort.MIN, uint(16993));

  public static final NodeId DataSetFolderType_AddDataSetFolder =
      new NodeId(UShort.MIN, uint(16994));

  public static final NodeId DataSetFolderType_AddDataSetFolder_InputArguments =
      new NodeId(UShort.MIN, uint(16995));

  public static final NodeId DataSetFolderType_AddDataSetFolder_OutputArguments =
      new NodeId(UShort.MIN, uint(16996));

  public static final NodeId DataSetFolderType_RemoveDataSetFolder =
      new NodeId(UShort.MIN, uint(16997));

  public static final NodeId SystemOffNormalAlarmType_OnDelay = new NodeId(UShort.MIN, uint(16998));

  public static final NodeId SystemOffNormalAlarmType_OffDelay =
      new NodeId(UShort.MIN, uint(16999));

  public static final NodeId SystemOffNormalAlarmType_FirstInGroupFlag =
      new NodeId(UShort.MIN, uint(17000));

  public static final NodeId SystemOffNormalAlarmType_FirstInGroup =
      new NodeId(UShort.MIN, uint(17001));

  public static final NodeId SystemOffNormalAlarmType_AlarmGroup_Placeholder =
      new NodeId(UShort.MIN, uint(17002));

  public static final NodeId SystemOffNormalAlarmType_ReAlarmTime =
      new NodeId(UShort.MIN, uint(17003));

  public static final NodeId SystemOffNormalAlarmType_ReAlarmRepeatCount =
      new NodeId(UShort.MIN, uint(17004));

  public static final NodeId SystemOffNormalAlarmType_Silence = new NodeId(UShort.MIN, uint(17005));

  public static final NodeId SystemOffNormalAlarmType_Suppress =
      new NodeId(UShort.MIN, uint(17006));

  public static final NodeId DataSetFolderType_RemoveDataSetFolder_InputArguments =
      new NodeId(UShort.MIN, uint(17007));

  public static final NodeId TripAlarmType_ConditionSubClassId =
      new NodeId(UShort.MIN, uint(17008));

  public static final NodeId TripAlarmType_ConditionSubClassName =
      new NodeId(UShort.MIN, uint(17009));

  public static final NodeId TripAlarmType_OutOfServiceState = new NodeId(UShort.MIN, uint(17010));

  public static final NodeId TripAlarmType_OutOfServiceState_Id =
      new NodeId(UShort.MIN, uint(17011));

  public static final NodeId TripAlarmType_OutOfServiceState_Name =
      new NodeId(UShort.MIN, uint(17012));

  public static final NodeId TripAlarmType_OutOfServiceState_Number =
      new NodeId(UShort.MIN, uint(17013));

  public static final NodeId TripAlarmType_OutOfServiceState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(17014));

  public static final NodeId TripAlarmType_OutOfServiceState_TransitionTime =
      new NodeId(UShort.MIN, uint(17015));

  public static final NodeId TripAlarmType_OutOfServiceState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(17016));

  public static final NodeId TripAlarmType_OutOfServiceState_TrueState =
      new NodeId(UShort.MIN, uint(17017));

  public static final NodeId TripAlarmType_OutOfServiceState_FalseState =
      new NodeId(UShort.MIN, uint(17018));

  public static final NodeId TripAlarmType_SilenceState = new NodeId(UShort.MIN, uint(17019));

  public static final NodeId TripAlarmType_SilenceState_Id = new NodeId(UShort.MIN, uint(17020));

  public static final NodeId TripAlarmType_SilenceState_Name = new NodeId(UShort.MIN, uint(17021));

  public static final NodeId TripAlarmType_SilenceState_Number =
      new NodeId(UShort.MIN, uint(17022));

  public static final NodeId TripAlarmType_SilenceState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(17023));

  public static final NodeId TripAlarmType_SilenceState_TransitionTime =
      new NodeId(UShort.MIN, uint(17024));

  public static final NodeId TripAlarmType_SilenceState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(17025));

  public static final NodeId TripAlarmType_SilenceState_TrueState =
      new NodeId(UShort.MIN, uint(17026));

  public static final NodeId TripAlarmType_SilenceState_FalseState =
      new NodeId(UShort.MIN, uint(17027));

  public static final NodeId TripAlarmType_AudibleEnabled = new NodeId(UShort.MIN, uint(17028));

  public static final NodeId TripAlarmType_AudibleSound = new NodeId(UShort.MIN, uint(17029));

  public static final NodeId AddPublishedDataItemsTemplateMethodType =
      new NodeId(UShort.MIN, uint(17030));

  public static final NodeId AddPublishedDataItemsTemplateMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(17031));

  public static final NodeId AddPublishedDataItemsTemplateMethodType_OutputArguments =
      new NodeId(UShort.MIN, uint(17032));

  public static final NodeId AddPublishedEventsTemplateMethodType =
      new NodeId(UShort.MIN, uint(17033));

  public static final NodeId TripAlarmType_OnDelay = new NodeId(UShort.MIN, uint(17034));

  public static final NodeId TripAlarmType_OffDelay = new NodeId(UShort.MIN, uint(17035));

  public static final NodeId TripAlarmType_FirstInGroupFlag = new NodeId(UShort.MIN, uint(17036));

  public static final NodeId TripAlarmType_FirstInGroup = new NodeId(UShort.MIN, uint(17037));

  public static final NodeId TripAlarmType_AlarmGroup_Placeholder =
      new NodeId(UShort.MIN, uint(17038));

  public static final NodeId TripAlarmType_ReAlarmTime = new NodeId(UShort.MIN, uint(17039));

  public static final NodeId TripAlarmType_ReAlarmRepeatCount = new NodeId(UShort.MIN, uint(17040));

  public static final NodeId TripAlarmType_Silence = new NodeId(UShort.MIN, uint(17041));

  public static final NodeId TripAlarmType_Suppress = new NodeId(UShort.MIN, uint(17042));

  public static final NodeId AddPublishedEventsTemplateMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(17043));

  public static final NodeId CertificateExpirationAlarmType_ConditionSubClassId =
      new NodeId(UShort.MIN, uint(17044));

  public static final NodeId CertificateExpirationAlarmType_ConditionSubClassName =
      new NodeId(UShort.MIN, uint(17045));

  public static final NodeId CertificateExpirationAlarmType_OutOfServiceState =
      new NodeId(UShort.MIN, uint(17046));

  public static final NodeId CertificateExpirationAlarmType_OutOfServiceState_Id =
      new NodeId(UShort.MIN, uint(17047));

  public static final NodeId CertificateExpirationAlarmType_OutOfServiceState_Name =
      new NodeId(UShort.MIN, uint(17048));

  public static final NodeId CertificateExpirationAlarmType_OutOfServiceState_Number =
      new NodeId(UShort.MIN, uint(17049));

  public static final NodeId CertificateExpirationAlarmType_OutOfServiceState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(17050));

  public static final NodeId CertificateExpirationAlarmType_OutOfServiceState_TransitionTime =
      new NodeId(UShort.MIN, uint(17051));

  public static final NodeId
      CertificateExpirationAlarmType_OutOfServiceState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(17052));

  public static final NodeId CertificateExpirationAlarmType_OutOfServiceState_TrueState =
      new NodeId(UShort.MIN, uint(17053));

  public static final NodeId CertificateExpirationAlarmType_OutOfServiceState_FalseState =
      new NodeId(UShort.MIN, uint(17054));

  public static final NodeId CertificateExpirationAlarmType_SilenceState =
      new NodeId(UShort.MIN, uint(17055));

  public static final NodeId CertificateExpirationAlarmType_SilenceState_Id =
      new NodeId(UShort.MIN, uint(17056));

  public static final NodeId CertificateExpirationAlarmType_SilenceState_Name =
      new NodeId(UShort.MIN, uint(17057));

  public static final NodeId CertificateExpirationAlarmType_SilenceState_Number =
      new NodeId(UShort.MIN, uint(17058));

  public static final NodeId CertificateExpirationAlarmType_SilenceState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(17059));

  public static final NodeId CertificateExpirationAlarmType_SilenceState_TransitionTime =
      new NodeId(UShort.MIN, uint(17060));

  public static final NodeId CertificateExpirationAlarmType_SilenceState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(17061));

  public static final NodeId CertificateExpirationAlarmType_SilenceState_TrueState =
      new NodeId(UShort.MIN, uint(17062));

  public static final NodeId CertificateExpirationAlarmType_SilenceState_FalseState =
      new NodeId(UShort.MIN, uint(17063));

  public static final NodeId CertificateExpirationAlarmType_AudibleEnabled =
      new NodeId(UShort.MIN, uint(17064));

  public static final NodeId CertificateExpirationAlarmType_AudibleSound =
      new NodeId(UShort.MIN, uint(17065));

  public static final NodeId AddPublishedEventsTemplateMethodType_OutputArguments =
      new NodeId(UShort.MIN, uint(17066));

  public static final NodeId AddDataSetFolderMethodType = new NodeId(UShort.MIN, uint(17067));

  public static final NodeId AddDataSetFolderMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(17068));

  public static final NodeId AddDataSetFolderMethodType_OutputArguments =
      new NodeId(UShort.MIN, uint(17069));

  public static final NodeId CertificateExpirationAlarmType_OnDelay =
      new NodeId(UShort.MIN, uint(17070));

  public static final NodeId CertificateExpirationAlarmType_OffDelay =
      new NodeId(UShort.MIN, uint(17071));

  public static final NodeId CertificateExpirationAlarmType_FirstInGroupFlag =
      new NodeId(UShort.MIN, uint(17072));

  public static final NodeId CertificateExpirationAlarmType_FirstInGroup =
      new NodeId(UShort.MIN, uint(17073));

  public static final NodeId CertificateExpirationAlarmType_AlarmGroup_Placeholder =
      new NodeId(UShort.MIN, uint(17074));

  public static final NodeId CertificateExpirationAlarmType_ReAlarmTime =
      new NodeId(UShort.MIN, uint(17075));

  public static final NodeId CertificateExpirationAlarmType_ReAlarmRepeatCount =
      new NodeId(UShort.MIN, uint(17076));

  public static final NodeId CertificateExpirationAlarmType_Silence =
      new NodeId(UShort.MIN, uint(17077));

  public static final NodeId CertificateExpirationAlarmType_Suppress =
      new NodeId(UShort.MIN, uint(17078));

  public static final NodeId RemoveDataSetFolderMethodType = new NodeId(UShort.MIN, uint(17079));

  public static final NodeId DiscrepancyAlarmType = new NodeId(UShort.MIN, uint(17080));

  public static final NodeId DiscrepancyAlarmType_EventId = new NodeId(UShort.MIN, uint(17081));

  public static final NodeId DiscrepancyAlarmType_EventType = new NodeId(UShort.MIN, uint(17082));

  public static final NodeId DiscrepancyAlarmType_SourceNode = new NodeId(UShort.MIN, uint(17083));

  public static final NodeId DiscrepancyAlarmType_SourceName = new NodeId(UShort.MIN, uint(17084));

  public static final NodeId DiscrepancyAlarmType_Time = new NodeId(UShort.MIN, uint(17085));

  public static final NodeId DiscrepancyAlarmType_ReceiveTime = new NodeId(UShort.MIN, uint(17086));

  public static final NodeId DiscrepancyAlarmType_LocalTime = new NodeId(UShort.MIN, uint(17087));

  public static final NodeId DiscrepancyAlarmType_Message = new NodeId(UShort.MIN, uint(17088));

  public static final NodeId DiscrepancyAlarmType_Severity = new NodeId(UShort.MIN, uint(17089));

  public static final NodeId DiscrepancyAlarmType_ConditionClassId =
      new NodeId(UShort.MIN, uint(17090));

  public static final NodeId DiscrepancyAlarmType_ConditionClassName =
      new NodeId(UShort.MIN, uint(17091));

  public static final NodeId DiscrepancyAlarmType_ConditionSubClassId =
      new NodeId(UShort.MIN, uint(17092));

  public static final NodeId DiscrepancyAlarmType_ConditionSubClassName =
      new NodeId(UShort.MIN, uint(17093));

  public static final NodeId DiscrepancyAlarmType_ConditionName =
      new NodeId(UShort.MIN, uint(17094));

  public static final NodeId DiscrepancyAlarmType_BranchId = new NodeId(UShort.MIN, uint(17095));

  public static final NodeId DiscrepancyAlarmType_Retain = new NodeId(UShort.MIN, uint(17096));

  public static final NodeId DiscrepancyAlarmType_EnabledState =
      new NodeId(UShort.MIN, uint(17097));

  public static final NodeId DiscrepancyAlarmType_EnabledState_Id =
      new NodeId(UShort.MIN, uint(17098));

  public static final NodeId DiscrepancyAlarmType_EnabledState_Name =
      new NodeId(UShort.MIN, uint(17099));

  public static final NodeId DiscrepancyAlarmType_EnabledState_Number =
      new NodeId(UShort.MIN, uint(17100));

  public static final NodeId DiscrepancyAlarmType_EnabledState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(17101));

  public static final NodeId DiscrepancyAlarmType_EnabledState_TransitionTime =
      new NodeId(UShort.MIN, uint(17102));

  public static final NodeId DiscrepancyAlarmType_EnabledState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(17103));

  public static final NodeId DiscrepancyAlarmType_EnabledState_TrueState =
      new NodeId(UShort.MIN, uint(17104));

  public static final NodeId DiscrepancyAlarmType_EnabledState_FalseState =
      new NodeId(UShort.MIN, uint(17105));

  public static final NodeId DiscrepancyAlarmType_Quality = new NodeId(UShort.MIN, uint(17106));

  public static final NodeId DiscrepancyAlarmType_Quality_SourceTimestamp =
      new NodeId(UShort.MIN, uint(17107));

  public static final NodeId DiscrepancyAlarmType_LastSeverity =
      new NodeId(UShort.MIN, uint(17108));

  public static final NodeId DiscrepancyAlarmType_LastSeverity_SourceTimestamp =
      new NodeId(UShort.MIN, uint(17109));

  public static final NodeId DiscrepancyAlarmType_Comment = new NodeId(UShort.MIN, uint(17110));

  public static final NodeId DiscrepancyAlarmType_Comment_SourceTimestamp =
      new NodeId(UShort.MIN, uint(17111));

  public static final NodeId DiscrepancyAlarmType_ClientUserId =
      new NodeId(UShort.MIN, uint(17112));

  public static final NodeId DiscrepancyAlarmType_Disable = new NodeId(UShort.MIN, uint(17113));

  public static final NodeId DiscrepancyAlarmType_Enable = new NodeId(UShort.MIN, uint(17114));

  public static final NodeId DiscrepancyAlarmType_AddComment = new NodeId(UShort.MIN, uint(17115));

  public static final NodeId DiscrepancyAlarmType_AddComment_InputArguments =
      new NodeId(UShort.MIN, uint(17116));

  public static final NodeId DiscrepancyAlarmType_ConditionRefresh =
      new NodeId(UShort.MIN, uint(17117));

  public static final NodeId DiscrepancyAlarmType_ConditionRefresh_InputArguments =
      new NodeId(UShort.MIN, uint(17118));

  public static final NodeId DiscrepancyAlarmType_ConditionRefresh2 =
      new NodeId(UShort.MIN, uint(17119));

  public static final NodeId DiscrepancyAlarmType_ConditionRefresh2_InputArguments =
      new NodeId(UShort.MIN, uint(17120));

  public static final NodeId DiscrepancyAlarmType_AckedState = new NodeId(UShort.MIN, uint(17121));

  public static final NodeId DiscrepancyAlarmType_AckedState_Id =
      new NodeId(UShort.MIN, uint(17122));

  public static final NodeId DiscrepancyAlarmType_AckedState_Name =
      new NodeId(UShort.MIN, uint(17123));

  public static final NodeId DiscrepancyAlarmType_AckedState_Number =
      new NodeId(UShort.MIN, uint(17124));

  public static final NodeId DiscrepancyAlarmType_AckedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(17125));

  public static final NodeId DiscrepancyAlarmType_AckedState_TransitionTime =
      new NodeId(UShort.MIN, uint(17126));

  public static final NodeId DiscrepancyAlarmType_AckedState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(17127));

  public static final NodeId DiscrepancyAlarmType_AckedState_TrueState =
      new NodeId(UShort.MIN, uint(17128));

  public static final NodeId DiscrepancyAlarmType_AckedState_FalseState =
      new NodeId(UShort.MIN, uint(17129));

  public static final NodeId DiscrepancyAlarmType_ConfirmedState =
      new NodeId(UShort.MIN, uint(17130));

  public static final NodeId DiscrepancyAlarmType_ConfirmedState_Id =
      new NodeId(UShort.MIN, uint(17131));

  public static final NodeId DiscrepancyAlarmType_ConfirmedState_Name =
      new NodeId(UShort.MIN, uint(17132));

  public static final NodeId DiscrepancyAlarmType_ConfirmedState_Number =
      new NodeId(UShort.MIN, uint(17133));

  public static final NodeId DiscrepancyAlarmType_ConfirmedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(17134));

  public static final NodeId DiscrepancyAlarmType_ConfirmedState_TransitionTime =
      new NodeId(UShort.MIN, uint(17135));

  public static final NodeId DiscrepancyAlarmType_ConfirmedState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(17136));

  public static final NodeId DiscrepancyAlarmType_ConfirmedState_TrueState =
      new NodeId(UShort.MIN, uint(17137));

  public static final NodeId DiscrepancyAlarmType_ConfirmedState_FalseState =
      new NodeId(UShort.MIN, uint(17138));

  public static final NodeId DiscrepancyAlarmType_Acknowledge = new NodeId(UShort.MIN, uint(17139));

  public static final NodeId DiscrepancyAlarmType_Acknowledge_InputArguments =
      new NodeId(UShort.MIN, uint(17140));

  public static final NodeId DiscrepancyAlarmType_Confirm = new NodeId(UShort.MIN, uint(17141));

  public static final NodeId DiscrepancyAlarmType_Confirm_InputArguments =
      new NodeId(UShort.MIN, uint(17142));

  public static final NodeId DiscrepancyAlarmType_ActiveState = new NodeId(UShort.MIN, uint(17143));

  public static final NodeId DiscrepancyAlarmType_ActiveState_Id =
      new NodeId(UShort.MIN, uint(17144));

  public static final NodeId DiscrepancyAlarmType_ActiveState_Name =
      new NodeId(UShort.MIN, uint(17145));

  public static final NodeId DiscrepancyAlarmType_ActiveState_Number =
      new NodeId(UShort.MIN, uint(17146));

  public static final NodeId DiscrepancyAlarmType_ActiveState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(17147));

  public static final NodeId DiscrepancyAlarmType_ActiveState_TransitionTime =
      new NodeId(UShort.MIN, uint(17148));

  public static final NodeId DiscrepancyAlarmType_ActiveState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(17149));

  public static final NodeId DiscrepancyAlarmType_ActiveState_TrueState =
      new NodeId(UShort.MIN, uint(17150));

  public static final NodeId DiscrepancyAlarmType_ActiveState_FalseState =
      new NodeId(UShort.MIN, uint(17151));

  public static final NodeId DiscrepancyAlarmType_InputNode = new NodeId(UShort.MIN, uint(17152));

  public static final NodeId DiscrepancyAlarmType_SuppressedState =
      new NodeId(UShort.MIN, uint(17153));

  public static final NodeId DiscrepancyAlarmType_SuppressedState_Id =
      new NodeId(UShort.MIN, uint(17154));

  public static final NodeId DiscrepancyAlarmType_SuppressedState_Name =
      new NodeId(UShort.MIN, uint(17155));

  public static final NodeId DiscrepancyAlarmType_SuppressedState_Number =
      new NodeId(UShort.MIN, uint(17156));

  public static final NodeId DiscrepancyAlarmType_SuppressedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(17157));

  public static final NodeId DiscrepancyAlarmType_SuppressedState_TransitionTime =
      new NodeId(UShort.MIN, uint(17158));

  public static final NodeId DiscrepancyAlarmType_SuppressedState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(17159));

  public static final NodeId DiscrepancyAlarmType_SuppressedState_TrueState =
      new NodeId(UShort.MIN, uint(17160));

  public static final NodeId DiscrepancyAlarmType_SuppressedState_FalseState =
      new NodeId(UShort.MIN, uint(17161));

  public static final NodeId DiscrepancyAlarmType_OutOfServiceState =
      new NodeId(UShort.MIN, uint(17162));

  public static final NodeId DiscrepancyAlarmType_OutOfServiceState_Id =
      new NodeId(UShort.MIN, uint(17163));

  public static final NodeId DiscrepancyAlarmType_OutOfServiceState_Name =
      new NodeId(UShort.MIN, uint(17164));

  public static final NodeId DiscrepancyAlarmType_OutOfServiceState_Number =
      new NodeId(UShort.MIN, uint(17165));

  public static final NodeId DiscrepancyAlarmType_OutOfServiceState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(17166));

  public static final NodeId DiscrepancyAlarmType_OutOfServiceState_TransitionTime =
      new NodeId(UShort.MIN, uint(17167));

  public static final NodeId DiscrepancyAlarmType_OutOfServiceState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(17168));

  public static final NodeId DiscrepancyAlarmType_OutOfServiceState_TrueState =
      new NodeId(UShort.MIN, uint(17169));

  public static final NodeId DiscrepancyAlarmType_OutOfServiceState_FalseState =
      new NodeId(UShort.MIN, uint(17170));

  public static final NodeId DiscrepancyAlarmType_SilenceState =
      new NodeId(UShort.MIN, uint(17171));

  public static final NodeId DiscrepancyAlarmType_SilenceState_Id =
      new NodeId(UShort.MIN, uint(17172));

  public static final NodeId DiscrepancyAlarmType_SilenceState_Name =
      new NodeId(UShort.MIN, uint(17173));

  public static final NodeId DiscrepancyAlarmType_SilenceState_Number =
      new NodeId(UShort.MIN, uint(17174));

  public static final NodeId DiscrepancyAlarmType_SilenceState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(17175));

  public static final NodeId DiscrepancyAlarmType_SilenceState_TransitionTime =
      new NodeId(UShort.MIN, uint(17176));

  public static final NodeId DiscrepancyAlarmType_SilenceState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(17177));

  public static final NodeId DiscrepancyAlarmType_SilenceState_TrueState =
      new NodeId(UShort.MIN, uint(17178));

  public static final NodeId DiscrepancyAlarmType_SilenceState_FalseState =
      new NodeId(UShort.MIN, uint(17179));

  public static final NodeId DiscrepancyAlarmType_ShelvingState =
      new NodeId(UShort.MIN, uint(17180));

  public static final NodeId DiscrepancyAlarmType_ShelvingState_CurrentState =
      new NodeId(UShort.MIN, uint(17181));

  public static final NodeId DiscrepancyAlarmType_ShelvingState_CurrentState_Id =
      new NodeId(UShort.MIN, uint(17182));

  public static final NodeId DiscrepancyAlarmType_ShelvingState_CurrentState_Name =
      new NodeId(UShort.MIN, uint(17183));

  public static final NodeId DiscrepancyAlarmType_ShelvingState_CurrentState_Number =
      new NodeId(UShort.MIN, uint(17184));

  public static final NodeId DiscrepancyAlarmType_ShelvingState_CurrentState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(17185));

  public static final NodeId DiscrepancyAlarmType_ShelvingState_LastTransition =
      new NodeId(UShort.MIN, uint(17186));

  public static final NodeId DiscrepancyAlarmType_ShelvingState_LastTransition_Id =
      new NodeId(UShort.MIN, uint(17187));

  public static final NodeId DiscrepancyAlarmType_ShelvingState_LastTransition_Name =
      new NodeId(UShort.MIN, uint(17188));

  public static final NodeId DiscrepancyAlarmType_ShelvingState_LastTransition_Number =
      new NodeId(UShort.MIN, uint(17189));

  public static final NodeId DiscrepancyAlarmType_ShelvingState_LastTransition_TransitionTime =
      new NodeId(UShort.MIN, uint(17190));

  public static final NodeId
      DiscrepancyAlarmType_ShelvingState_LastTransition_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(17191));

  public static final NodeId DiscrepancyAlarmType_ShelvingState_UnshelveTime =
      new NodeId(UShort.MIN, uint(17192));

  public static final NodeId DiscrepancyAlarmType_ShelvingState_Unshelve =
      new NodeId(UShort.MIN, uint(17193));

  public static final NodeId DiscrepancyAlarmType_ShelvingState_OneShotShelve =
      new NodeId(UShort.MIN, uint(17194));

  public static final NodeId DiscrepancyAlarmType_ShelvingState_TimedShelve =
      new NodeId(UShort.MIN, uint(17195));

  public static final NodeId DiscrepancyAlarmType_ShelvingState_TimedShelve_InputArguments =
      new NodeId(UShort.MIN, uint(17196));

  public static final NodeId DiscrepancyAlarmType_SuppressedOrShelved =
      new NodeId(UShort.MIN, uint(17197));

  public static final NodeId DiscrepancyAlarmType_MaxTimeShelved =
      new NodeId(UShort.MIN, uint(17198));

  public static final NodeId DiscrepancyAlarmType_AudibleEnabled =
      new NodeId(UShort.MIN, uint(17199));

  public static final NodeId DiscrepancyAlarmType_AudibleSound =
      new NodeId(UShort.MIN, uint(17200));

  public static final NodeId RemoveDataSetFolderMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(17201));

  public static final NodeId PubSubConnectionType_Address_NetworkInterface =
      new NodeId(UShort.MIN, uint(17202));

  public static final NodeId PubSubConnectionType_TransportSettings =
      new NodeId(UShort.MIN, uint(17203));

  public static final NodeId
      PubSubConnectionType_WriterGroupName_Placeholder_MaxNetworkMessageSize =
          new NodeId(UShort.MIN, uint(17204));

  public static final NodeId DiscrepancyAlarmType_OnDelay = new NodeId(UShort.MIN, uint(17205));

  public static final NodeId DiscrepancyAlarmType_OffDelay = new NodeId(UShort.MIN, uint(17206));

  public static final NodeId DiscrepancyAlarmType_FirstInGroupFlag =
      new NodeId(UShort.MIN, uint(17207));

  public static final NodeId DiscrepancyAlarmType_FirstInGroup =
      new NodeId(UShort.MIN, uint(17208));

  public static final NodeId DiscrepancyAlarmType_AlarmGroup_Placeholder =
      new NodeId(UShort.MIN, uint(17209));

  public static final NodeId DiscrepancyAlarmType_ReAlarmTime = new NodeId(UShort.MIN, uint(17210));

  public static final NodeId DiscrepancyAlarmType_ReAlarmRepeatCount =
      new NodeId(UShort.MIN, uint(17211));

  public static final NodeId DiscrepancyAlarmType_Silence = new NodeId(UShort.MIN, uint(17212));

  public static final NodeId DiscrepancyAlarmType_Suppress = new NodeId(UShort.MIN, uint(17213));

  public static final NodeId PubSubConnectionType_WriterGroupName_Placeholder_WriterGroupId =
      new NodeId(UShort.MIN, uint(17214));

  public static final NodeId DiscrepancyAlarmType_TargetValueNode =
      new NodeId(UShort.MIN, uint(17215));

  public static final NodeId DiscrepancyAlarmType_ExpectedTime =
      new NodeId(UShort.MIN, uint(17216));

  public static final NodeId DiscrepancyAlarmType_Tolerance = new NodeId(UShort.MIN, uint(17217));

  public static final NodeId SafetyConditionClassType = new NodeId(UShort.MIN, uint(17218));

  public static final NodeId HighlyManagedAlarmConditionClassType =
      new NodeId(UShort.MIN, uint(17219));

  public static final NodeId TrainingConditionClassType = new NodeId(UShort.MIN, uint(17220));

  public static final NodeId TestingConditionClassType = new NodeId(UShort.MIN, uint(17221));

  public static final NodeId AuditConditionCommentEventType_ConditionEventId =
      new NodeId(UShort.MIN, uint(17222));

  public static final NodeId AuditConditionAcknowledgeEventType_ConditionEventId =
      new NodeId(UShort.MIN, uint(17223));

  public static final NodeId AuditConditionConfirmEventType_ConditionEventId =
      new NodeId(UShort.MIN, uint(17224));

  public static final NodeId AuditConditionSuppressionEventType =
      new NodeId(UShort.MIN, uint(17225));

  public static final NodeId AuditConditionSuppressionEventType_EventId =
      new NodeId(UShort.MIN, uint(17226));

  public static final NodeId AuditConditionSuppressionEventType_EventType =
      new NodeId(UShort.MIN, uint(17227));

  public static final NodeId AuditConditionSuppressionEventType_SourceNode =
      new NodeId(UShort.MIN, uint(17228));

  public static final NodeId AuditConditionSuppressionEventType_SourceName =
      new NodeId(UShort.MIN, uint(17229));

  public static final NodeId AuditConditionSuppressionEventType_Time =
      new NodeId(UShort.MIN, uint(17230));

  public static final NodeId AuditConditionSuppressionEventType_ReceiveTime =
      new NodeId(UShort.MIN, uint(17231));

  public static final NodeId AuditConditionSuppressionEventType_LocalTime =
      new NodeId(UShort.MIN, uint(17232));

  public static final NodeId AuditConditionSuppressionEventType_Message =
      new NodeId(UShort.MIN, uint(17233));

  public static final NodeId AuditConditionSuppressionEventType_Severity =
      new NodeId(UShort.MIN, uint(17234));

  public static final NodeId AuditConditionSuppressionEventType_ActionTimeStamp =
      new NodeId(UShort.MIN, uint(17235));

  public static final NodeId AuditConditionSuppressionEventType_Status =
      new NodeId(UShort.MIN, uint(17236));

  public static final NodeId AuditConditionSuppressionEventType_ServerId =
      new NodeId(UShort.MIN, uint(17237));

  public static final NodeId AuditConditionSuppressionEventType_ClientAuditEntryId =
      new NodeId(UShort.MIN, uint(17238));

  public static final NodeId AuditConditionSuppressionEventType_ClientUserId =
      new NodeId(UShort.MIN, uint(17239));

  public static final NodeId AuditConditionSuppressionEventType_MethodId =
      new NodeId(UShort.MIN, uint(17240));

  public static final NodeId AuditConditionSuppressionEventType_InputArguments =
      new NodeId(UShort.MIN, uint(17241));

  public static final NodeId AuditConditionSilenceEventType = new NodeId(UShort.MIN, uint(17242));

  public static final NodeId AuditConditionSilenceEventType_EventId =
      new NodeId(UShort.MIN, uint(17243));

  public static final NodeId AuditConditionSilenceEventType_EventType =
      new NodeId(UShort.MIN, uint(17244));

  public static final NodeId AuditConditionSilenceEventType_SourceNode =
      new NodeId(UShort.MIN, uint(17245));

  public static final NodeId AuditConditionSilenceEventType_SourceName =
      new NodeId(UShort.MIN, uint(17246));

  public static final NodeId AuditConditionSilenceEventType_Time =
      new NodeId(UShort.MIN, uint(17247));

  public static final NodeId AuditConditionSilenceEventType_ReceiveTime =
      new NodeId(UShort.MIN, uint(17248));

  public static final NodeId AuditConditionSilenceEventType_LocalTime =
      new NodeId(UShort.MIN, uint(17249));

  public static final NodeId AuditConditionSilenceEventType_Message =
      new NodeId(UShort.MIN, uint(17250));

  public static final NodeId AuditConditionSilenceEventType_Severity =
      new NodeId(UShort.MIN, uint(17251));

  public static final NodeId AuditConditionSilenceEventType_ActionTimeStamp =
      new NodeId(UShort.MIN, uint(17252));

  public static final NodeId AuditConditionSilenceEventType_Status =
      new NodeId(UShort.MIN, uint(17253));

  public static final NodeId AuditConditionSilenceEventType_ServerId =
      new NodeId(UShort.MIN, uint(17254));

  public static final NodeId AuditConditionSilenceEventType_ClientAuditEntryId =
      new NodeId(UShort.MIN, uint(17255));

  public static final NodeId AuditConditionSilenceEventType_ClientUserId =
      new NodeId(UShort.MIN, uint(17256));

  public static final NodeId AuditConditionSilenceEventType_MethodId =
      new NodeId(UShort.MIN, uint(17257));

  public static final NodeId AuditConditionSilenceEventType_InputArguments =
      new NodeId(UShort.MIN, uint(17258));

  public static final NodeId AuditConditionOutOfServiceEventType =
      new NodeId(UShort.MIN, uint(17259));

  public static final NodeId AuditConditionOutOfServiceEventType_EventId =
      new NodeId(UShort.MIN, uint(17260));

  public static final NodeId AuditConditionOutOfServiceEventType_EventType =
      new NodeId(UShort.MIN, uint(17261));

  public static final NodeId AuditConditionOutOfServiceEventType_SourceNode =
      new NodeId(UShort.MIN, uint(17262));

  public static final NodeId AuditConditionOutOfServiceEventType_SourceName =
      new NodeId(UShort.MIN, uint(17263));

  public static final NodeId AuditConditionOutOfServiceEventType_Time =
      new NodeId(UShort.MIN, uint(17264));

  public static final NodeId AuditConditionOutOfServiceEventType_ReceiveTime =
      new NodeId(UShort.MIN, uint(17265));

  public static final NodeId AuditConditionOutOfServiceEventType_LocalTime =
      new NodeId(UShort.MIN, uint(17266));

  public static final NodeId AuditConditionOutOfServiceEventType_Message =
      new NodeId(UShort.MIN, uint(17267));

  public static final NodeId AuditConditionOutOfServiceEventType_Severity =
      new NodeId(UShort.MIN, uint(17268));

  public static final NodeId AuditConditionOutOfServiceEventType_ActionTimeStamp =
      new NodeId(UShort.MIN, uint(17269));

  public static final NodeId AuditConditionOutOfServiceEventType_Status =
      new NodeId(UShort.MIN, uint(17270));

  public static final NodeId AuditConditionOutOfServiceEventType_ServerId =
      new NodeId(UShort.MIN, uint(17271));

  public static final NodeId AuditConditionOutOfServiceEventType_ClientAuditEntryId =
      new NodeId(UShort.MIN, uint(17272));

  public static final NodeId AuditConditionOutOfServiceEventType_ClientUserId =
      new NodeId(UShort.MIN, uint(17273));

  public static final NodeId AuditConditionOutOfServiceEventType_MethodId =
      new NodeId(UShort.MIN, uint(17274));

  public static final NodeId AuditConditionOutOfServiceEventType_InputArguments =
      new NodeId(UShort.MIN, uint(17275));

  public static final NodeId HasEffectDisable = new NodeId(UShort.MIN, uint(17276));

  public static final NodeId AlarmRateVariableType = new NodeId(UShort.MIN, uint(17277));

  public static final NodeId AlarmRateVariableType_Rate = new NodeId(UShort.MIN, uint(17278));

  public static final NodeId AlarmMetricsType = new NodeId(UShort.MIN, uint(17279));

  public static final NodeId AlarmMetricsType_AlarmCount = new NodeId(UShort.MIN, uint(17280));

  public static final NodeId AlarmMetricsType_MaximumActiveState =
      new NodeId(UShort.MIN, uint(17281));

  public static final NodeId AlarmMetricsType_MaximumUnAck = new NodeId(UShort.MIN, uint(17282));

  public static final NodeId AlarmMetricsType_MaximumReAlarmCount =
      new NodeId(UShort.MIN, uint(17283));

  public static final NodeId AlarmMetricsType_CurrentAlarmRate =
      new NodeId(UShort.MIN, uint(17284));

  public static final NodeId AlarmMetricsType_CurrentAlarmRate_Rate =
      new NodeId(UShort.MIN, uint(17285));

  public static final NodeId AlarmMetricsType_MaximumAlarmRate =
      new NodeId(UShort.MIN, uint(17286));

  public static final NodeId AlarmMetricsType_MaximumAlarmRate_Rate =
      new NodeId(UShort.MIN, uint(17287));

  public static final NodeId AlarmMetricsType_AverageAlarmRate =
      new NodeId(UShort.MIN, uint(17288));

  public static final NodeId AlarmMetricsType_AverageAlarmRate_Rate =
      new NodeId(UShort.MIN, uint(17289));

  public static final NodeId PubSubConnectionType_WriterGroupName_Placeholder_TransportSettings =
      new NodeId(UShort.MIN, uint(17290));

  public static final NodeId PubSubConnectionType_WriterGroupName_Placeholder_MessageSettings =
      new NodeId(UShort.MIN, uint(17291));

  public static final NodeId PublishSubscribeType_ConnectionName_Placeholder_TransportProfileUri =
      new NodeId(UShort.MIN, uint(17292));

  public static final NodeId PubSubConnectionType_WriterGroupName_Placeholder_AddDataSetWriter =
      new NodeId(UShort.MIN, uint(17293));

  public static final NodeId
      PubSubConnectionType_WriterGroupName_Placeholder_AddDataSetWriter_InputArguments =
          new NodeId(UShort.MIN, uint(17294));

  public static final NodeId
      PublishSubscribeType_ConnectionName_Placeholder_TransportProfileUri_RestrictToList =
          new NodeId(UShort.MIN, uint(17295));

  public static final NodeId PublishSubscribeType_SetSecurityKeys =
      new NodeId(UShort.MIN, uint(17296));

  public static final NodeId PublishSubscribeType_SetSecurityKeys_InputArguments =
      new NodeId(UShort.MIN, uint(17297));

  public static final NodeId SetSecurityKeysMethodType = new NodeId(UShort.MIN, uint(17298));

  public static final NodeId SetSecurityKeysMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(17299));

  public static final NodeId
      PublishSubscribe_ConnectionName_Placeholder_Diagnostics_Counters_StateOperationalByMethod_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(17300));

  public static final NodeId
      PubSubConnectionType_WriterGroupName_Placeholder_AddDataSetWriter_OutputArguments =
          new NodeId(UShort.MIN, uint(17301));

  public static final NodeId
      PubSubConnectionType_ReaderGroupName_Placeholder_MaxNetworkMessageSize =
          new NodeId(UShort.MIN, uint(17302));

  public static final NodeId
      PublishSubscribe_ConnectionName_Placeholder_Diagnostics_Counters_StateOperationalByMethod_TimeFirstChange =
          new NodeId(UShort.MIN, uint(17303));

  public static final NodeId
      PublishSubscribe_ConnectionName_Placeholder_Diagnostics_Counters_StateOperationalByParent =
          new NodeId(UShort.MIN, uint(17304));

  public static final NodeId
      PublishSubscribe_ConnectionName_Placeholder_Diagnostics_Counters_StateOperationalByParent_Active =
          new NodeId(UShort.MIN, uint(17305));

  public static final NodeId PubSubConnectionType_TransportProfileUri =
      new NodeId(UShort.MIN, uint(17306));

  public static final NodeId PubSubConnectionType_ReaderGroupName_Placeholder_TransportSettings =
      new NodeId(UShort.MIN, uint(17307));

  public static final NodeId PubSubConnectionType_ReaderGroupName_Placeholder_MessageSettings =
      new NodeId(UShort.MIN, uint(17308));

  public static final NodeId PubSubConnectionType_TransportProfileUri_RestrictToList =
      new NodeId(UShort.MIN, uint(17309));

  public static final NodeId PubSubConnectionType_WriterGroupName_Placeholder =
      new NodeId(UShort.MIN, uint(17310));

  public static final NodeId PubSubConnectionType_WriterGroupName_Placeholder_SecurityMode =
      new NodeId(UShort.MIN, uint(17311));

  public static final NodeId PubSubConnectionType_WriterGroupName_Placeholder_SecurityGroupId =
      new NodeId(UShort.MIN, uint(17312));

  public static final NodeId PubSubConnectionType_WriterGroupName_Placeholder_SecurityKeyServices =
      new NodeId(UShort.MIN, uint(17313));

  public static final NodeId PubSubConnectionType_WriterGroupName_Placeholder_Status =
      new NodeId(UShort.MIN, uint(17314));

  public static final NodeId PubSubConnectionType_WriterGroupName_Placeholder_Status_State =
      new NodeId(UShort.MIN, uint(17315));

  public static final NodeId PubSubConnectionType_WriterGroupName_Placeholder_Status_Enable =
      new NodeId(UShort.MIN, uint(17316));

  public static final NodeId PubSubConnectionType_WriterGroupName_Placeholder_Status_Disable =
      new NodeId(UShort.MIN, uint(17317));

  public static final NodeId PubSubConnectionType_WriterGroupName_Placeholder_PublishingInterval =
      new NodeId(UShort.MIN, uint(17318));

  public static final NodeId PubSubConnectionType_WriterGroupName_Placeholder_KeepAliveTime =
      new NodeId(UShort.MIN, uint(17319));

  public static final NodeId
      PublishSubscribe_ConnectionName_Placeholder_Diagnostics_Counters_StateOperationalByParent_Classification =
          new NodeId(UShort.MIN, uint(17320));

  public static final NodeId PubSubConnectionType_WriterGroupName_Placeholder_Priority =
      new NodeId(UShort.MIN, uint(17321));

  public static final NodeId PubSubConnectionType_WriterGroupName_Placeholder_LocaleIds =
      new NodeId(UShort.MIN, uint(17322));

  public static final NodeId PubSubConnectionType_WriterGroupName_Placeholder_RemoveDataSetWriter =
      new NodeId(UShort.MIN, uint(17323));

  public static final NodeId
      PubSubConnectionType_WriterGroupName_Placeholder_RemoveDataSetWriter_InputArguments =
          new NodeId(UShort.MIN, uint(17324));

  public static final NodeId PubSubConnectionType_ReaderGroupName_Placeholder =
      new NodeId(UShort.MIN, uint(17325));

  public static final NodeId PubSubConnectionType_ReaderGroupName_Placeholder_SecurityMode =
      new NodeId(UShort.MIN, uint(17326));

  public static final NodeId PubSubConnectionType_ReaderGroupName_Placeholder_SecurityGroupId =
      new NodeId(UShort.MIN, uint(17327));

  public static final NodeId PubSubConnectionType_ReaderGroupName_Placeholder_SecurityKeyServices =
      new NodeId(UShort.MIN, uint(17328));

  public static final NodeId PubSubConnectionType_ReaderGroupName_Placeholder_Status =
      new NodeId(UShort.MIN, uint(17329));

  public static final NodeId PubSubConnectionType_ReaderGroupName_Placeholder_Status_State =
      new NodeId(UShort.MIN, uint(17330));

  public static final NodeId PubSubConnectionType_ReaderGroupName_Placeholder_Status_Enable =
      new NodeId(UShort.MIN, uint(17331));

  public static final NodeId PubSubConnectionType_ReaderGroupName_Placeholder_Status_Disable =
      new NodeId(UShort.MIN, uint(17332));

  public static final NodeId PubSubConnectionType_ReaderGroupName_Placeholder_RemoveDataSetReader =
      new NodeId(UShort.MIN, uint(17333));

  public static final NodeId
      PubSubConnectionType_ReaderGroupName_Placeholder_RemoveDataSetReader_InputArguments =
          new NodeId(UShort.MIN, uint(17334));

  public static final NodeId
      PublishSubscribe_ConnectionName_Placeholder_Diagnostics_Counters_StateOperationalByParent_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(17335));

  public static final NodeId
      PublishSubscribe_ConnectionName_Placeholder_Diagnostics_Counters_StateOperationalByParent_TimeFirstChange =
          new NodeId(UShort.MIN, uint(17336));

  public static final NodeId
      PublishSubscribe_ConnectionName_Placeholder_Diagnostics_Counters_StateOperationalFromError =
          new NodeId(UShort.MIN, uint(17337));

  public static final NodeId
      PublishSubscribe_ConnectionName_Placeholder_Diagnostics_Counters_StateOperationalFromError_Active =
          new NodeId(UShort.MIN, uint(17338));

  public static final NodeId
      PublishSubscribe_ConnectionName_Placeholder_Diagnostics_Counters_StateOperationalFromError_Classification =
          new NodeId(UShort.MIN, uint(17339));

  public static final NodeId
      PublishSubscribe_ConnectionName_Placeholder_Diagnostics_Counters_StateOperationalFromError_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(17340));

  public static final NodeId
      PublishSubscribe_ConnectionName_Placeholder_Diagnostics_Counters_StateOperationalFromError_TimeFirstChange =
          new NodeId(UShort.MIN, uint(17341));

  public static final NodeId
      PublishSubscribe_ConnectionName_Placeholder_Diagnostics_Counters_StatePausedByParent =
          new NodeId(UShort.MIN, uint(17342));

  public static final NodeId
      PublishSubscribe_ConnectionName_Placeholder_Diagnostics_Counters_StatePausedByParent_Active =
          new NodeId(UShort.MIN, uint(17343));

  public static final NodeId
      PublishSubscribe_ConnectionName_Placeholder_Diagnostics_Counters_StatePausedByParent_Classification =
          new NodeId(UShort.MIN, uint(17344));

  public static final NodeId
      PublishSubscribe_ConnectionName_Placeholder_Diagnostics_Counters_StatePausedByParent_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(17345));

  public static final NodeId
      PublishSubscribe_ConnectionName_Placeholder_Diagnostics_Counters_StatePausedByParent_TimeFirstChange =
          new NodeId(UShort.MIN, uint(17346));

  public static final NodeId
      PublishSubscribe_ConnectionName_Placeholder_Diagnostics_Counters_StateDisabledByMethod =
          new NodeId(UShort.MIN, uint(17347));

  public static final NodeId
      PublishSubscribe_ConnectionName_Placeholder_Diagnostics_Counters_StateDisabledByMethod_Active =
          new NodeId(UShort.MIN, uint(17348));

  public static final NodeId
      PublishSubscribe_ConnectionName_Placeholder_Diagnostics_Counters_StateDisabledByMethod_Classification =
          new NodeId(UShort.MIN, uint(17349));

  public static final NodeId
      PublishSubscribe_ConnectionName_Placeholder_Diagnostics_Counters_StateDisabledByMethod_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(17350));

  public static final NodeId
      PublishSubscribe_ConnectionName_Placeholder_Diagnostics_Counters_StateDisabledByMethod_TimeFirstChange =
          new NodeId(UShort.MIN, uint(17351));

  public static final NodeId PublishSubscribe_ConnectionName_Placeholder_Diagnostics_LiveValues =
      new NodeId(UShort.MIN, uint(17352));

  public static final NodeId
      PublishSubscribe_ConnectionName_Placeholder_Diagnostics_LiveValues_ResolvedAddress =
          new NodeId(UShort.MIN, uint(17353));

  public static final NodeId
      PublishSubscribe_ConnectionName_Placeholder_Diagnostics_LiveValues_ResolvedAddress_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(17354));

  public static final NodeId PubSubConnectionType_ReaderGroupName_Placeholder_AddDataSetReader =
      new NodeId(UShort.MIN, uint(17355));

  public static final NodeId PublishSubscribe_ConnectionName_Placeholder_AddWriterGroup =
      new NodeId(UShort.MIN, uint(17356));

  public static final NodeId
      PublishSubscribe_ConnectionName_Placeholder_AddWriterGroup_InputArguments =
          new NodeId(UShort.MIN, uint(17357));

  public static final NodeId
      PublishSubscribe_ConnectionName_Placeholder_AddWriterGroup_OutputArguments =
          new NodeId(UShort.MIN, uint(17358));

  public static final NodeId PublishSubscribe_ConnectionName_Placeholder_AddReaderGroup =
      new NodeId(UShort.MIN, uint(17359));

  public static final NodeId
      PublishSubscribe_ConnectionName_Placeholder_AddReaderGroup_InputArguments =
          new NodeId(UShort.MIN, uint(17360));

  public static final NodeId
      PublishSubscribe_ConnectionName_Placeholder_AddReaderGroup_OutputArguments =
          new NodeId(UShort.MIN, uint(17361));

  public static final NodeId PublishSubscribe_ConnectionName_Placeholder_RemoveGroup =
      new NodeId(UShort.MIN, uint(17362));

  public static final NodeId
      PublishSubscribe_ConnectionName_Placeholder_RemoveGroup_InputArguments =
          new NodeId(UShort.MIN, uint(17363));

  public static final NodeId PublishSubscribe_SetSecurityKeys = new NodeId(UShort.MIN, uint(17364));

  public static final NodeId PublishSubscribe_SetSecurityKeys_InputArguments =
      new NodeId(UShort.MIN, uint(17365));

  public static final NodeId PublishSubscribe_AddConnection = new NodeId(UShort.MIN, uint(17366));

  public static final NodeId PublishSubscribe_AddConnection_InputArguments =
      new NodeId(UShort.MIN, uint(17367));

  public static final NodeId PublishSubscribe_AddConnection_OutputArguments =
      new NodeId(UShort.MIN, uint(17368));

  public static final NodeId PublishSubscribe_RemoveConnection =
      new NodeId(UShort.MIN, uint(17369));

  public static final NodeId PublishSubscribe_RemoveConnection_InputArguments =
      new NodeId(UShort.MIN, uint(17370));

  public static final NodeId PublishSubscribe_PublishedDataSets =
      new NodeId(UShort.MIN, uint(17371));

  public static final NodeId PublishSubscribe_PublishedDataSets_AddPublishedDataItems =
      new NodeId(UShort.MIN, uint(17372));

  public static final NodeId
      PublishSubscribe_PublishedDataSets_AddPublishedDataItems_InputArguments =
          new NodeId(UShort.MIN, uint(17373));

  public static final NodeId
      PublishSubscribe_PublishedDataSets_AddPublishedDataItems_OutputArguments =
          new NodeId(UShort.MIN, uint(17374));

  public static final NodeId PublishSubscribe_PublishedDataSets_AddPublishedEvents =
      new NodeId(UShort.MIN, uint(17375));

  public static final NodeId PublishSubscribe_PublishedDataSets_AddPublishedEvents_InputArguments =
      new NodeId(UShort.MIN, uint(17376));

  public static final NodeId PublishSubscribe_PublishedDataSets_AddPublishedEvents_OutputArguments =
      new NodeId(UShort.MIN, uint(17377));

  public static final NodeId PublishSubscribe_PublishedDataSets_AddPublishedDataItemsTemplate =
      new NodeId(UShort.MIN, uint(17378));

  public static final NodeId
      PublishSubscribe_PublishedDataSets_AddPublishedDataItemsTemplate_InputArguments =
          new NodeId(UShort.MIN, uint(17379));

  public static final NodeId
      PublishSubscribe_PublishedDataSets_AddPublishedDataItemsTemplate_OutputArguments =
          new NodeId(UShort.MIN, uint(17380));

  public static final NodeId PublishSubscribe_PublishedDataSets_AddPublishedEventsTemplate =
      new NodeId(UShort.MIN, uint(17381));

  public static final NodeId
      PublishSubscribe_PublishedDataSets_AddPublishedEventsTemplate_InputArguments =
          new NodeId(UShort.MIN, uint(17382));

  public static final NodeId
      PublishSubscribe_PublishedDataSets_AddPublishedEventsTemplate_OutputArguments =
          new NodeId(UShort.MIN, uint(17383));

  public static final NodeId PublishSubscribe_PublishedDataSets_RemovePublishedDataSet =
      new NodeId(UShort.MIN, uint(17384));

  public static final NodeId
      PublishSubscribe_PublishedDataSets_RemovePublishedDataSet_InputArguments =
          new NodeId(UShort.MIN, uint(17385));

  public static final NodeId DataSetReaderType_CreateTargetVariables =
      new NodeId(UShort.MIN, uint(17386));

  public static final NodeId DataSetReaderType_CreateTargetVariables_InputArguments =
      new NodeId(UShort.MIN, uint(17387));

  public static final NodeId DataSetReaderType_CreateTargetVariables_OutputArguments =
      new NodeId(UShort.MIN, uint(17388));

  public static final NodeId DataSetReaderType_CreateDataSetMirror =
      new NodeId(UShort.MIN, uint(17389));

  public static final NodeId DataSetReaderType_CreateDataSetMirror_InputArguments =
      new NodeId(UShort.MIN, uint(17390));

  public static final NodeId DataSetReaderType_CreateDataSetMirror_OutputArguments =
      new NodeId(UShort.MIN, uint(17391));

  public static final NodeId DataSetReaderTypeCreateTargetVariablesMethodType =
      new NodeId(UShort.MIN, uint(17392));

  public static final NodeId DataSetReaderTypeCreateTargetVariablesMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(17393));

  public static final NodeId DataSetReaderTypeCreateTargetVariablesMethodType_OutputArguments =
      new NodeId(UShort.MIN, uint(17394));

  public static final NodeId DataSetReaderTypeCreateDataSetMirrorMethodType =
      new NodeId(UShort.MIN, uint(17395));

  public static final NodeId DataSetReaderTypeCreateDataSetMirrorMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(17396));

  public static final NodeId DataSetReaderTypeCreateDataSetMirrorMethodType_OutputArguments =
      new NodeId(UShort.MIN, uint(17397));

  public static final NodeId PublishSubscribe_PublishedDataSets_AddDataSetFolder =
      new NodeId(UShort.MIN, uint(17398));

  public static final NodeId
      PubSubConnectionType_ReaderGroupName_Placeholder_AddDataSetReader_InputArguments =
          new NodeId(UShort.MIN, uint(17399));

  public static final NodeId
      PubSubConnectionType_ReaderGroupName_Placeholder_AddDataSetReader_OutputArguments =
          new NodeId(UShort.MIN, uint(17400));

  public static final NodeId PublishSubscribe_PublishedDataSets_AddDataSetFolder_InputArguments =
      new NodeId(UShort.MIN, uint(17401));

  public static final NodeId PublishSubscribe_PublishedDataSets_AddDataSetFolder_OutputArguments =
      new NodeId(UShort.MIN, uint(17402));

  public static final NodeId PublishSubscribe_PublishedDataSets_RemoveDataSetFolder =
      new NodeId(UShort.MIN, uint(17403));

  public static final NodeId PublishSubscribe_PublishedDataSets_RemoveDataSetFolder_InputArguments =
      new NodeId(UShort.MIN, uint(17404));

  public static final NodeId PublishSubscribe_Status = new NodeId(UShort.MIN, uint(17405));

  public static final NodeId PublishSubscribe_Status_State = new NodeId(UShort.MIN, uint(17406));

  public static final NodeId PublishSubscribe_Status_Enable = new NodeId(UShort.MIN, uint(17407));

  public static final NodeId PublishSubscribe_Status_Disable = new NodeId(UShort.MIN, uint(17408));

  public static final NodeId PublishSubscribe_Diagnostics = new NodeId(UShort.MIN, uint(17409));

  public static final NodeId PublishSubscribe_Diagnostics_DiagnosticsLevel =
      new NodeId(UShort.MIN, uint(17410));

  public static final NodeId PublishSubscribe_Diagnostics_TotalInformation =
      new NodeId(UShort.MIN, uint(17411));

  public static final NodeId PublishSubscribe_Diagnostics_TotalInformation_Active =
      new NodeId(UShort.MIN, uint(17412));

  public static final NodeId PublishSubscribe_Diagnostics_TotalInformation_Classification =
      new NodeId(UShort.MIN, uint(17413));

  public static final NodeId PublishSubscribe_Diagnostics_TotalInformation_DiagnosticsLevel =
      new NodeId(UShort.MIN, uint(17414));

  public static final NodeId PublishSubscribe_Diagnostics_TotalInformation_TimeFirstChange =
      new NodeId(UShort.MIN, uint(17415));

  public static final NodeId PublishSubscribe_Diagnostics_TotalError =
      new NodeId(UShort.MIN, uint(17416));

  public static final NodeId PublishSubscribe_Diagnostics_TotalError_Active =
      new NodeId(UShort.MIN, uint(17417));

  public static final NodeId PublishSubscribe_Diagnostics_TotalError_Classification =
      new NodeId(UShort.MIN, uint(17418));

  public static final NodeId PublishSubscribe_Diagnostics_TotalError_DiagnosticsLevel =
      new NodeId(UShort.MIN, uint(17419));

  public static final NodeId PublishSubscribe_Diagnostics_TotalError_TimeFirstChange =
      new NodeId(UShort.MIN, uint(17420));

  public static final NodeId PublishSubscribe_Diagnostics_Reset =
      new NodeId(UShort.MIN, uint(17421));

  public static final NodeId PublishSubscribe_Diagnostics_SubError =
      new NodeId(UShort.MIN, uint(17422));

  public static final NodeId PublishSubscribe_Diagnostics_Counters =
      new NodeId(UShort.MIN, uint(17423));

  public static final NodeId PublishSubscribe_Diagnostics_Counters_StateError =
      new NodeId(UShort.MIN, uint(17424));

  public static final NodeId PublishSubscribe_Diagnostics_Counters_StateError_Active =
      new NodeId(UShort.MIN, uint(17425));

  public static final NodeId PublishSubscribe_Diagnostics_Counters_StateError_Classification =
      new NodeId(UShort.MIN, uint(17426));

  public static final NodeId PubSubConnectionType_AddWriterGroup =
      new NodeId(UShort.MIN, uint(17427));

  public static final NodeId PubSubConnectionType_AddWriterGroup_InputArguments =
      new NodeId(UShort.MIN, uint(17428));

  public static final NodeId PublishSubscribe_Diagnostics_Counters_StateError_DiagnosticsLevel =
      new NodeId(UShort.MIN, uint(17429));

  public static final NodeId PublishSubscribe_Diagnostics_Counters_StateError_TimeFirstChange =
      new NodeId(UShort.MIN, uint(17430));

  public static final NodeId PublishSubscribe_Diagnostics_Counters_StateOperationalByMethod =
      new NodeId(UShort.MIN, uint(17431));

  public static final NodeId PublishSubscribe_Diagnostics_Counters_StateOperationalByMethod_Active =
      new NodeId(UShort.MIN, uint(17432));

  public static final NodeId
      PublishSubscribe_Diagnostics_Counters_StateOperationalByMethod_Classification =
          new NodeId(UShort.MIN, uint(17433));

  public static final NodeId
      PublishSubscribe_Diagnostics_Counters_StateOperationalByMethod_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(17434));

  public static final NodeId
      PublishSubscribe_Diagnostics_Counters_StateOperationalByMethod_TimeFirstChange =
          new NodeId(UShort.MIN, uint(17435));

  public static final NodeId PublishSubscribe_Diagnostics_Counters_StateOperationalByParent =
      new NodeId(UShort.MIN, uint(17436));

  public static final NodeId PublishSubscribe_Diagnostics_Counters_StateOperationalByParent_Active =
      new NodeId(UShort.MIN, uint(17437));

  public static final NodeId
      PublishSubscribe_Diagnostics_Counters_StateOperationalByParent_Classification =
          new NodeId(UShort.MIN, uint(17438));

  public static final NodeId
      PublishSubscribe_Diagnostics_Counters_StateOperationalByParent_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(17439));

  public static final NodeId
      PublishSubscribe_Diagnostics_Counters_StateOperationalByParent_TimeFirstChange =
          new NodeId(UShort.MIN, uint(17440));

  public static final NodeId PublishSubscribe_Diagnostics_Counters_StateOperationalFromError =
      new NodeId(UShort.MIN, uint(17441));

  public static final NodeId
      PublishSubscribe_Diagnostics_Counters_StateOperationalFromError_Active =
          new NodeId(UShort.MIN, uint(17442));

  public static final NodeId
      PublishSubscribe_Diagnostics_Counters_StateOperationalFromError_Classification =
          new NodeId(UShort.MIN, uint(17443));

  public static final NodeId
      PublishSubscribe_Diagnostics_Counters_StateOperationalFromError_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(17444));

  public static final NodeId
      PublishSubscribe_Diagnostics_Counters_StateOperationalFromError_TimeFirstChange =
          new NodeId(UShort.MIN, uint(17445));

  public static final NodeId PublishSubscribe_Diagnostics_Counters_StatePausedByParent =
      new NodeId(UShort.MIN, uint(17446));

  public static final NodeId PublishSubscribe_Diagnostics_Counters_StatePausedByParent_Active =
      new NodeId(UShort.MIN, uint(17447));

  public static final NodeId
      PublishSubscribe_Diagnostics_Counters_StatePausedByParent_Classification =
          new NodeId(UShort.MIN, uint(17448));

  public static final NodeId
      PublishSubscribe_Diagnostics_Counters_StatePausedByParent_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(17449));

  public static final NodeId
      PublishSubscribe_Diagnostics_Counters_StatePausedByParent_TimeFirstChange =
          new NodeId(UShort.MIN, uint(17450));

  public static final NodeId PublishSubscribe_Diagnostics_Counters_StateDisabledByMethod =
      new NodeId(UShort.MIN, uint(17451));

  public static final NodeId PublishSubscribe_Diagnostics_Counters_StateDisabledByMethod_Active =
      new NodeId(UShort.MIN, uint(17452));

  public static final NodeId
      PublishSubscribe_Diagnostics_Counters_StateDisabledByMethod_Classification =
          new NodeId(UShort.MIN, uint(17453));

  public static final NodeId
      PublishSubscribe_Diagnostics_Counters_StateDisabledByMethod_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(17454));

  public static final NodeId
      PublishSubscribe_Diagnostics_Counters_StateDisabledByMethod_TimeFirstChange =
          new NodeId(UShort.MIN, uint(17455));

  public static final NodeId PubSubConnectionType_AddWriterGroup_OutputArguments =
      new NodeId(UShort.MIN, uint(17456));

  public static final NodeId PublishSubscribe_Diagnostics_LiveValues =
      new NodeId(UShort.MIN, uint(17457));

  public static final NodeId PublishSubscribe_Diagnostics_LiveValues_ConfiguredDataSetWriters =
      new NodeId(UShort.MIN, uint(17458));

  public static final NodeId
      PublishSubscribe_Diagnostics_LiveValues_ConfiguredDataSetWriters_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(17459));

  public static final NodeId PublishSubscribe_Diagnostics_LiveValues_ConfiguredDataSetReaders =
      new NodeId(UShort.MIN, uint(17460));

  public static final NodeId
      PublishSubscribe_Diagnostics_LiveValues_ConfiguredDataSetReaders_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(17461));

  public static final NodeId PublishSubscribe_Diagnostics_LiveValues_OperationalDataSetWriters =
      new NodeId(UShort.MIN, uint(17462));

  public static final NodeId
      PublishSubscribe_Diagnostics_LiveValues_OperationalDataSetWriters_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(17463));

  public static final NodeId PublishSubscribe_Diagnostics_LiveValues_OperationalDataSetReaders =
      new NodeId(UShort.MIN, uint(17464));

  public static final NodeId PubSubConnectionType_AddReaderGroup =
      new NodeId(UShort.MIN, uint(17465));

  public static final NodeId
      PublishSubscribe_Diagnostics_LiveValues_OperationalDataSetReaders_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(17466));

  public static final NodeId DatagramConnectionTransportDataType =
      new NodeId(UShort.MIN, uint(17467));

  public static final NodeId DatagramConnectionTransportDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(17468));

  public static final NodeId OpcUa_BinarySchema_DatagramConnectionTransportDataType =
      new NodeId(UShort.MIN, uint(17469));

  public static final NodeId
      OpcUa_BinarySchema_DatagramConnectionTransportDataType_DataTypeVersion =
          new NodeId(UShort.MIN, uint(17470));

  public static final NodeId
      OpcUa_BinarySchema_DatagramConnectionTransportDataType_DictionaryFragment =
          new NodeId(UShort.MIN, uint(17471));

  public static final NodeId DatagramConnectionTransportDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(17472));

  public static final NodeId OpcUa_XmlSchema_DatagramConnectionTransportDataType =
      new NodeId(UShort.MIN, uint(17473));

  public static final NodeId OpcUa_XmlSchema_DatagramConnectionTransportDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(17474));

  public static final NodeId
      OpcUa_XmlSchema_DatagramConnectionTransportDataType_DictionaryFragment =
          new NodeId(UShort.MIN, uint(17475));

  public static final NodeId DatagramConnectionTransportDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(17476));

  public static final NodeId UadpDataSetReaderMessageType_DataSetOffset =
      new NodeId(UShort.MIN, uint(17477));

  public static final NodeId PublishSubscribeType_ConnectionName_Placeholder_ConnectionProperties =
      new NodeId(UShort.MIN, uint(17478));

  public static final NodeId PublishSubscribeType_SupportedTransportProfiles =
      new NodeId(UShort.MIN, uint(17479));

  public static final NodeId PublishSubscribe_ConnectionName_Placeholder_ConnectionProperties =
      new NodeId(UShort.MIN, uint(17480));

  public static final NodeId PublishSubscribe_SupportedTransportProfiles =
      new NodeId(UShort.MIN, uint(17481));

  public static final NodeId
      PublishedDataSetType_DataSetWriterName_Placeholder_DataSetWriterProperties =
          new NodeId(UShort.MIN, uint(17482));

  public static final NodeId
      PublishedDataItemsType_DataSetWriterName_Placeholder_DataSetWriterProperties =
          new NodeId(UShort.MIN, uint(17483));

  public static final NodeId
      PublishedEventsType_DataSetWriterName_Placeholder_DataSetWriterProperties =
          new NodeId(UShort.MIN, uint(17484));

  public static final NodeId PubSubConnectionType_ConnectionProperties =
      new NodeId(UShort.MIN, uint(17485));

  public static final NodeId PubSubConnectionType_WriterGroupName_Placeholder_GroupProperties =
      new NodeId(UShort.MIN, uint(17486));

  public static final NodeId PubSubConnectionType_ReaderGroupName_Placeholder_GroupProperties =
      new NodeId(UShort.MIN, uint(17487));

  public static final NodeId PubSubGroupType_GroupProperties = new NodeId(UShort.MIN, uint(17488));

  public static final NodeId WriterGroupType_GroupProperties = new NodeId(UShort.MIN, uint(17489));

  public static final NodeId WriterGroupType_DataSetWriterName_Placeholder_DataSetWriterProperties =
      new NodeId(UShort.MIN, uint(17490));

  public static final NodeId ReaderGroupType_GroupProperties = new NodeId(UShort.MIN, uint(17491));

  public static final NodeId ReaderGroupType_DataSetReaderName_Placeholder_DataSetReaderProperties =
      new NodeId(UShort.MIN, uint(17492));

  public static final NodeId DataSetWriterType_DataSetWriterProperties =
      new NodeId(UShort.MIN, uint(17493));

  public static final NodeId DataSetReaderType_DataSetReaderProperties =
      new NodeId(UShort.MIN, uint(17494));

  public static final NodeId CreateCredentialMethodType_OutputArguments =
      new NodeId(UShort.MIN, uint(17495));

  public static final NodeId KeyCredentialConfigurationFolderType =
      new NodeId(UShort.MIN, uint(17496));

  public static final NodeId AnalogUnitType = new NodeId(UShort.MIN, uint(17497));

  public static final NodeId AnalogUnitType_Definition = new NodeId(UShort.MIN, uint(17498));

  public static final NodeId AnalogUnitType_ValuePrecision = new NodeId(UShort.MIN, uint(17499));

  public static final NodeId AnalogUnitType_InstrumentRange = new NodeId(UShort.MIN, uint(17500));

  public static final NodeId AnalogUnitType_EURange = new NodeId(UShort.MIN, uint(17501));

  public static final NodeId AnalogUnitType_EngineeringUnits = new NodeId(UShort.MIN, uint(17502));

  public static final NodeId
      PublishSubscribeType_ConnectionName_Placeholder_Address_NetworkInterface_Selections =
          new NodeId(UShort.MIN, uint(17503));

  public static final NodeId
      PublishSubscribeType_ConnectionName_Placeholder_Address_NetworkInterface_SelectionDescriptions =
          new NodeId(UShort.MIN, uint(17504));

  public static final NodeId
      PublishSubscribeType_ConnectionName_Placeholder_Address_NetworkInterface_RestrictToList =
          new NodeId(UShort.MIN, uint(17505));

  public static final NodeId
      PublishSubscribe_ConnectionName_Placeholder_Address_NetworkInterface_Selections =
          new NodeId(UShort.MIN, uint(17506));

  public static final NodeId PubSubConnectionType_AddReaderGroup_InputArguments =
      new NodeId(UShort.MIN, uint(17507));

  public static final NodeId PubSubConnectionType_AddReaderGroup_OutputArguments =
      new NodeId(UShort.MIN, uint(17508));

  public static final NodeId
      PublishSubscribe_ConnectionName_Placeholder_Address_NetworkInterface_SelectionDescriptions =
          new NodeId(UShort.MIN, uint(17509));

  public static final NodeId
      PublishSubscribe_ConnectionName_Placeholder_Address_NetworkInterface_RestrictToList =
          new NodeId(UShort.MIN, uint(17510));

  public static final NodeId KeyCredentialConfigurationFolderType_ServiceName_Placeholder =
      new NodeId(UShort.MIN, uint(17511));

  public static final NodeId
      KeyCredentialConfigurationFolderType_ServiceName_Placeholder_ResourceUri =
          new NodeId(UShort.MIN, uint(17512));

  public static final NodeId
      KeyCredentialConfigurationFolderType_ServiceName_Placeholder_ProfileUri =
          new NodeId(UShort.MIN, uint(17513));

  public static final NodeId
      KeyCredentialConfigurationFolderType_ServiceName_Placeholder_EndpointUrls =
          new NodeId(UShort.MIN, uint(17514));

  public static final NodeId
      KeyCredentialConfigurationFolderType_ServiceName_Placeholder_ServiceStatus =
          new NodeId(UShort.MIN, uint(17515));

  public static final NodeId
      KeyCredentialConfigurationFolderType_ServiceName_Placeholder_GetEncryptingKey =
          new NodeId(UShort.MIN, uint(17516));

  public static final NodeId
      KeyCredentialConfigurationFolderType_ServiceName_Placeholder_GetEncryptingKey_InputArguments =
          new NodeId(UShort.MIN, uint(17517));

  public static final NodeId
      KeyCredentialConfigurationFolderType_ServiceName_Placeholder_GetEncryptingKey_OutputArguments =
          new NodeId(UShort.MIN, uint(17518));

  public static final NodeId
      KeyCredentialConfigurationFolderType_ServiceName_Placeholder_UpdateCredential =
          new NodeId(UShort.MIN, uint(17519));

  public static final NodeId
      KeyCredentialConfigurationFolderType_ServiceName_Placeholder_UpdateCredential_InputArguments =
          new NodeId(UShort.MIN, uint(17520));

  public static final NodeId
      KeyCredentialConfigurationFolderType_ServiceName_Placeholder_DeleteCredential =
          new NodeId(UShort.MIN, uint(17521));

  public static final NodeId KeyCredentialConfigurationFolderType_CreateCredential =
      new NodeId(UShort.MIN, uint(17522));

  public static final NodeId KeyCredentialConfigurationFolderType_CreateCredential_InputArguments =
      new NodeId(UShort.MIN, uint(17523));

  public static final NodeId KeyCredentialConfigurationFolderType_CreateCredential_OutputArguments =
      new NodeId(UShort.MIN, uint(17524));

  public static final NodeId KeyCredentialConfiguration_ServiceName_Placeholder_GetEncryptingKey =
      new NodeId(UShort.MIN, uint(17525));

  public static final NodeId
      KeyCredentialConfiguration_ServiceName_Placeholder_GetEncryptingKey_InputArguments =
          new NodeId(UShort.MIN, uint(17526));

  public static final NodeId
      KeyCredentialConfiguration_ServiceName_Placeholder_GetEncryptingKey_OutputArguments =
          new NodeId(UShort.MIN, uint(17527));

  public static final NodeId KeyCredentialConfiguration_CreateCredential =
      new NodeId(UShort.MIN, uint(17528));

  public static final NodeId KeyCredentialConfiguration_CreateCredential_InputArguments =
      new NodeId(UShort.MIN, uint(17529));

  public static final NodeId KeyCredentialConfiguration_CreateCredential_OutputArguments =
      new NodeId(UShort.MIN, uint(17530));

  public static final NodeId GetEncryptingKeyMethodType = new NodeId(UShort.MIN, uint(17531));

  public static final NodeId GetEncryptingKeyMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(17532));

  public static final NodeId GetEncryptingKeyMethodType_OutputArguments =
      new NodeId(UShort.MIN, uint(17533));

  public static final NodeId KeyCredentialConfigurationType_GetEncryptingKey =
      new NodeId(UShort.MIN, uint(17534));

  public static final NodeId KeyCredentialConfigurationType_GetEncryptingKey_InputArguments =
      new NodeId(UShort.MIN, uint(17535));

  public static final NodeId KeyCredentialConfigurationType_GetEncryptingKey_OutputArguments =
      new NodeId(UShort.MIN, uint(17536));

  public static final NodeId AdditionalParametersType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(17537));

  public static final NodeId OpcUa_BinarySchema_AdditionalParametersType =
      new NodeId(UShort.MIN, uint(17538));

  public static final NodeId OpcUa_BinarySchema_AdditionalParametersType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(17539));

  public static final NodeId OpcUa_BinarySchema_AdditionalParametersType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(17540));

  public static final NodeId AdditionalParametersType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(17541));

  public static final NodeId OpcUa_XmlSchema_AdditionalParametersType =
      new NodeId(UShort.MIN, uint(17542));

  public static final NodeId OpcUa_XmlSchema_AdditionalParametersType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(17543));

  public static final NodeId OpcUa_XmlSchema_AdditionalParametersType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(17544));

  public static final NodeId RsaEncryptedSecret = new NodeId(UShort.MIN, uint(17545));

  public static final NodeId EccEncryptedSecret = new NodeId(UShort.MIN, uint(17546));

  public static final NodeId AdditionalParametersType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(17547));

  public static final NodeId EphemeralKeyType = new NodeId(UShort.MIN, uint(17548));

  public static final NodeId EphemeralKeyType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(17549));

  public static final NodeId OpcUa_BinarySchema_EphemeralKeyType =
      new NodeId(UShort.MIN, uint(17550));

  public static final NodeId OpcUa_BinarySchema_EphemeralKeyType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(17551));

  public static final NodeId OpcUa_BinarySchema_EphemeralKeyType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(17552));

  public static final NodeId EphemeralKeyType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(17553));

  public static final NodeId OpcUa_XmlSchema_EphemeralKeyType = new NodeId(UShort.MIN, uint(17554));

  public static final NodeId OpcUa_XmlSchema_EphemeralKeyType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(17555));

  public static final NodeId OpcUa_XmlSchema_EphemeralKeyType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(17556));

  public static final NodeId EphemeralKeyType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(17557));

  public static final NodeId PubSubConnectionType_WriterGroupName_Placeholder_HeaderLayoutUri =
      new NodeId(UShort.MIN, uint(17558));

  public static final NodeId WriterGroupType_HeaderLayoutUri = new NodeId(UShort.MIN, uint(17559));

  public static final NodeId ReaderGroupType_DataSetReaderName_Placeholder_KeyFrameCount =
      new NodeId(UShort.MIN, uint(17560));

  public static final NodeId PubSubConnectionTypeAddWriterGroupMethodType =
      new NodeId(UShort.MIN, uint(17561));

  public static final NodeId ReaderGroupType_DataSetReaderName_Placeholder_HeaderLayoutUri =
      new NodeId(UShort.MIN, uint(17562));

  public static final NodeId DataSetReaderType_KeyFrameCount = new NodeId(UShort.MIN, uint(17563));

  public static final NodeId DataSetReaderType_HeaderLayoutUri =
      new NodeId(UShort.MIN, uint(17564));

  public static final NodeId BaseAnalogType_Definition = new NodeId(UShort.MIN, uint(17565));

  public static final NodeId BaseAnalogType_ValuePrecision = new NodeId(UShort.MIN, uint(17566));

  public static final NodeId BaseAnalogType_InstrumentRange = new NodeId(UShort.MIN, uint(17567));

  public static final NodeId BaseAnalogType_EURange = new NodeId(UShort.MIN, uint(17568));

  public static final NodeId BaseAnalogType_EngineeringUnits = new NodeId(UShort.MIN, uint(17569));

  public static final NodeId AnalogUnitRangeType = new NodeId(UShort.MIN, uint(17570));

  public static final NodeId AnalogUnitRangeType_Definition = new NodeId(UShort.MIN, uint(17571));

  public static final NodeId AnalogUnitRangeType_ValuePrecision =
      new NodeId(UShort.MIN, uint(17572));

  public static final NodeId AnalogUnitRangeType_InstrumentRange =
      new NodeId(UShort.MIN, uint(17573));

  public static final NodeId AnalogUnitRangeType_EURange = new NodeId(UShort.MIN, uint(17574));

  public static final NodeId AnalogUnitRangeType_EngineeringUnits =
      new NodeId(UShort.MIN, uint(17575));

  public static final NodeId PubSubConnectionType_Address_NetworkInterface_Selections =
      new NodeId(UShort.MIN, uint(17576));

  public static final NodeId PubSubConnectionType_Address_NetworkInterface_SelectionDescriptions =
      new NodeId(UShort.MIN, uint(17577));

  public static final NodeId PubSubConnectionType_Address_NetworkInterface_RestrictToList =
      new NodeId(UShort.MIN, uint(17578));

  public static final NodeId
      DatagramConnectionTransportType_DiscoveryAddress_NetworkInterface_Selections =
          new NodeId(UShort.MIN, uint(17579));

  public static final NodeId
      DatagramConnectionTransportType_DiscoveryAddress_NetworkInterface_SelectionDescriptions =
          new NodeId(UShort.MIN, uint(17580));

  public static final NodeId
      DatagramConnectionTransportType_DiscoveryAddress_NetworkInterface_RestrictToList =
          new NodeId(UShort.MIN, uint(17581));

  public static final NodeId NetworkAddressType_NetworkInterface_Selections =
      new NodeId(UShort.MIN, uint(17582));

  public static final NodeId NetworkAddressType_NetworkInterface_SelectionDescriptions =
      new NodeId(UShort.MIN, uint(17583));

  public static final NodeId NetworkAddressType_NetworkInterface_RestrictToList =
      new NodeId(UShort.MIN, uint(17584));

  public static final NodeId NetworkAddressUrlType_NetworkInterface_Selections =
      new NodeId(UShort.MIN, uint(17585));

  public static final NodeId NetworkAddressUrlType_NetworkInterface_SelectionDescriptions =
      new NodeId(UShort.MIN, uint(17586));

  public static final NodeId NetworkAddressUrlType_NetworkInterface_RestrictToList =
      new NodeId(UShort.MIN, uint(17587));

  public static final NodeId Index = new NodeId(UShort.MIN, uint(17588));

  public static final NodeId DictionaryEntryType = new NodeId(UShort.MIN, uint(17589));

  public static final NodeId DictionaryEntryType_DictionaryEntryName_Placeholder =
      new NodeId(UShort.MIN, uint(17590));

  public static final NodeId DictionaryFolderType = new NodeId(UShort.MIN, uint(17591));

  public static final NodeId DictionaryFolderType_DictionaryFolderName_Placeholder =
      new NodeId(UShort.MIN, uint(17592));

  public static final NodeId DictionaryFolderType_DictionaryEntryName_Placeholder =
      new NodeId(UShort.MIN, uint(17593));

  public static final NodeId Dictionaries = new NodeId(UShort.MIN, uint(17594));

  public static final NodeId Dictionaries_DictionaryFolderName_Placeholder =
      new NodeId(UShort.MIN, uint(17595));

  public static final NodeId Dictionaries_DictionaryEntryName_Placeholder =
      new NodeId(UShort.MIN, uint(17596));

  public static final NodeId HasDictionaryEntry = new NodeId(UShort.MIN, uint(17597));

  public static final NodeId IrdiDictionaryEntryType = new NodeId(UShort.MIN, uint(17598));

  public static final NodeId IrdiDictionaryEntryType_DictionaryEntryName_Placeholder =
      new NodeId(UShort.MIN, uint(17599));

  public static final NodeId UriDictionaryEntryType = new NodeId(UShort.MIN, uint(17600));

  public static final NodeId UriDictionaryEntryType_DictionaryEntryName_Placeholder =
      new NodeId(UShort.MIN, uint(17601));

  public static final NodeId BaseInterfaceType = new NodeId(UShort.MIN, uint(17602));

  public static final NodeId HasInterface = new NodeId(UShort.MIN, uint(17603));

  public static final NodeId HasAddIn = new NodeId(UShort.MIN, uint(17604));

  public static final NodeId DefaultInstanceBrowseName = new NodeId(UShort.MIN, uint(17605));

  public static final NodeId GenericAttributeValue = new NodeId(UShort.MIN, uint(17606));

  public static final NodeId GenericAttributes = new NodeId(UShort.MIN, uint(17607));

  public static final NodeId GenericAttributeValue_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(17608));

  public static final NodeId GenericAttributes_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(17609));

  public static final NodeId GenericAttributeValue_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(17610));

  public static final NodeId GenericAttributes_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(17611));

  public static final NodeId ServerType_LocalTime = new NodeId(UShort.MIN, uint(17612));

  public static final NodeId PubSubConnectionTypeAddWriterGroupMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(17613));

  public static final NodeId PubSubConnectionTypeAddWriterGroupMethodType_OutputArguments =
      new NodeId(UShort.MIN, uint(17614));

  public static final NodeId AuditSecurityEventType_StatusCodeId =
      new NodeId(UShort.MIN, uint(17615));

  public static final NodeId AuditChannelEventType_StatusCodeId =
      new NodeId(UShort.MIN, uint(17616));

  public static final NodeId AuditOpenSecureChannelEventType_StatusCodeId =
      new NodeId(UShort.MIN, uint(17617));

  public static final NodeId AuditSessionEventType_StatusCodeId =
      new NodeId(UShort.MIN, uint(17618));

  public static final NodeId AuditCreateSessionEventType_StatusCodeId =
      new NodeId(UShort.MIN, uint(17619));

  public static final NodeId AuditUrlMismatchEventType_StatusCodeId =
      new NodeId(UShort.MIN, uint(17620));

  public static final NodeId AuditActivateSessionEventType_StatusCodeId =
      new NodeId(UShort.MIN, uint(17621));

  public static final NodeId AuditCancelEventType_StatusCodeId =
      new NodeId(UShort.MIN, uint(17622));

  public static final NodeId AuditCertificateEventType_StatusCodeId =
      new NodeId(UShort.MIN, uint(17623));

  public static final NodeId AuditCertificateDataMismatchEventType_StatusCodeId =
      new NodeId(UShort.MIN, uint(17624));

  public static final NodeId AuditCertificateExpiredEventType_StatusCodeId =
      new NodeId(UShort.MIN, uint(17625));

  public static final NodeId AuditCertificateInvalidEventType_StatusCodeId =
      new NodeId(UShort.MIN, uint(17626));

  public static final NodeId AuditCertificateUntrustedEventType_StatusCodeId =
      new NodeId(UShort.MIN, uint(17627));

  public static final NodeId AuditCertificateRevokedEventType_StatusCodeId =
      new NodeId(UShort.MIN, uint(17628));

  public static final NodeId AuditCertificateMismatchEventType_StatusCodeId =
      new NodeId(UShort.MIN, uint(17629));

  public static final NodeId PubSubConnectionAddReaderGroupGroupMethodType =
      new NodeId(UShort.MIN, uint(17630));

  public static final NodeId PubSubConnectionAddReaderGroupGroupMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(17631));

  public static final NodeId SelectionListType_Selections = new NodeId(UShort.MIN, uint(17632));

  public static final NodeId SelectionListType_SelectionDescriptions =
      new NodeId(UShort.MIN, uint(17633));

  public static final NodeId Server_LocalTime = new NodeId(UShort.MIN, uint(17634));

  public static final NodeId FiniteStateMachineType_AvailableStates =
      new NodeId(UShort.MIN, uint(17635));

  public static final NodeId FiniteStateMachineType_AvailableTransitions =
      new NodeId(UShort.MIN, uint(17636));

  public static final NodeId TemporaryFileTransferType_TransferState_Placeholder_AvailableStates =
      new NodeId(UShort.MIN, uint(17637));

  public static final NodeId
      TemporaryFileTransferType_TransferState_Placeholder_AvailableTransitions =
          new NodeId(UShort.MIN, uint(17638));

  public static final NodeId FileTransferStateMachineType_AvailableStates =
      new NodeId(UShort.MIN, uint(17639));

  public static final NodeId FileTransferStateMachineType_AvailableTransitions =
      new NodeId(UShort.MIN, uint(17640));

  public static final NodeId RoleMappingRuleChangedAuditEventType =
      new NodeId(UShort.MIN, uint(17641));

  public static final NodeId RoleMappingRuleChangedAuditEventType_EventId =
      new NodeId(UShort.MIN, uint(17642));

  public static final NodeId RoleMappingRuleChangedAuditEventType_EventType =
      new NodeId(UShort.MIN, uint(17643));

  public static final NodeId RoleMappingRuleChangedAuditEventType_SourceNode =
      new NodeId(UShort.MIN, uint(17644));

  public static final NodeId RoleMappingRuleChangedAuditEventType_SourceName =
      new NodeId(UShort.MIN, uint(17645));

  public static final NodeId RoleMappingRuleChangedAuditEventType_Time =
      new NodeId(UShort.MIN, uint(17646));

  public static final NodeId RoleMappingRuleChangedAuditEventType_ReceiveTime =
      new NodeId(UShort.MIN, uint(17647));

  public static final NodeId RoleMappingRuleChangedAuditEventType_LocalTime =
      new NodeId(UShort.MIN, uint(17648));

  public static final NodeId RoleMappingRuleChangedAuditEventType_Message =
      new NodeId(UShort.MIN, uint(17649));

  public static final NodeId RoleMappingRuleChangedAuditEventType_Severity =
      new NodeId(UShort.MIN, uint(17650));

  public static final NodeId RoleMappingRuleChangedAuditEventType_ActionTimeStamp =
      new NodeId(UShort.MIN, uint(17651));

  public static final NodeId RoleMappingRuleChangedAuditEventType_Status =
      new NodeId(UShort.MIN, uint(17652));

  public static final NodeId RoleMappingRuleChangedAuditEventType_ServerId =
      new NodeId(UShort.MIN, uint(17653));

  public static final NodeId RoleMappingRuleChangedAuditEventType_ClientAuditEntryId =
      new NodeId(UShort.MIN, uint(17654));

  public static final NodeId RoleMappingRuleChangedAuditEventType_ClientUserId =
      new NodeId(UShort.MIN, uint(17655));

  public static final NodeId RoleMappingRuleChangedAuditEventType_MethodId =
      new NodeId(UShort.MIN, uint(17656));

  public static final NodeId RoleMappingRuleChangedAuditEventType_InputArguments =
      new NodeId(UShort.MIN, uint(17657));

  public static final NodeId AlarmConditionType_ShelvingState_AvailableStates =
      new NodeId(UShort.MIN, uint(17658));

  public static final NodeId AlarmConditionType_ShelvingState_AvailableTransitions =
      new NodeId(UShort.MIN, uint(17659));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_ShelvingState_AvailableStates =
          new NodeId(UShort.MIN, uint(17660));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_ShelvingState_AvailableTransitions =
          new NodeId(UShort.MIN, uint(17661));

  public static final NodeId ShelvedStateMachineType_AvailableStates =
      new NodeId(UShort.MIN, uint(17662));

  public static final NodeId ShelvedStateMachineType_AvailableTransitions =
      new NodeId(UShort.MIN, uint(17663));

  public static final NodeId LimitAlarmType_ShelvingState_AvailableStates =
      new NodeId(UShort.MIN, uint(17664));

  public static final NodeId LimitAlarmType_ShelvingState_AvailableTransitions =
      new NodeId(UShort.MIN, uint(17665));

  public static final NodeId ExclusiveLimitStateMachineType_AvailableStates =
      new NodeId(UShort.MIN, uint(17666));

  public static final NodeId ExclusiveLimitStateMachineType_AvailableTransitions =
      new NodeId(UShort.MIN, uint(17667));

  public static final NodeId ExclusiveLimitAlarmType_ShelvingState_AvailableStates =
      new NodeId(UShort.MIN, uint(17668));

  public static final NodeId ExclusiveLimitAlarmType_ShelvingState_AvailableTransitions =
      new NodeId(UShort.MIN, uint(17669));

  public static final NodeId ExclusiveLimitAlarmType_LimitState_AvailableStates =
      new NodeId(UShort.MIN, uint(17670));

  public static final NodeId ExclusiveLimitAlarmType_LimitState_AvailableTransitions =
      new NodeId(UShort.MIN, uint(17671));

  public static final NodeId NonExclusiveLimitAlarmType_ShelvingState_AvailableStates =
      new NodeId(UShort.MIN, uint(17672));

  public static final NodeId NonExclusiveLimitAlarmType_ShelvingState_AvailableTransitions =
      new NodeId(UShort.MIN, uint(17673));

  public static final NodeId NonExclusiveLevelAlarmType_ShelvingState_AvailableStates =
      new NodeId(UShort.MIN, uint(17674));

  public static final NodeId NonExclusiveLevelAlarmType_ShelvingState_AvailableTransitions =
      new NodeId(UShort.MIN, uint(17675));

  public static final NodeId ExclusiveLevelAlarmType_ShelvingState_AvailableStates =
      new NodeId(UShort.MIN, uint(17676));

  public static final NodeId ExclusiveLevelAlarmType_ShelvingState_AvailableTransitions =
      new NodeId(UShort.MIN, uint(17677));

  public static final NodeId ExclusiveLevelAlarmType_LimitState_AvailableStates =
      new NodeId(UShort.MIN, uint(17678));

  public static final NodeId ExclusiveLevelAlarmType_LimitState_AvailableTransitions =
      new NodeId(UShort.MIN, uint(17679));

  public static final NodeId NonExclusiveDeviationAlarmType_ShelvingState_AvailableStates =
      new NodeId(UShort.MIN, uint(17680));

  public static final NodeId NonExclusiveDeviationAlarmType_ShelvingState_AvailableTransitions =
      new NodeId(UShort.MIN, uint(17681));

  public static final NodeId ExclusiveDeviationAlarmType_ShelvingState_AvailableStates =
      new NodeId(UShort.MIN, uint(17682));

  public static final NodeId ExclusiveDeviationAlarmType_ShelvingState_AvailableTransitions =
      new NodeId(UShort.MIN, uint(17683));

  public static final NodeId ExclusiveDeviationAlarmType_LimitState_AvailableStates =
      new NodeId(UShort.MIN, uint(17684));

  public static final NodeId ExclusiveDeviationAlarmType_LimitState_AvailableTransitions =
      new NodeId(UShort.MIN, uint(17685));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_ShelvingState_AvailableStates =
      new NodeId(UShort.MIN, uint(17686));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_ShelvingState_AvailableTransitions =
      new NodeId(UShort.MIN, uint(17687));

  public static final NodeId ExclusiveRateOfChangeAlarmType_ShelvingState_AvailableStates =
      new NodeId(UShort.MIN, uint(17688));

  public static final NodeId ExclusiveRateOfChangeAlarmType_ShelvingState_AvailableTransitions =
      new NodeId(UShort.MIN, uint(17689));

  public static final NodeId ExclusiveRateOfChangeAlarmType_LimitState_AvailableStates =
      new NodeId(UShort.MIN, uint(17690));

  public static final NodeId ExclusiveRateOfChangeAlarmType_LimitState_AvailableTransitions =
      new NodeId(UShort.MIN, uint(17691));

  public static final NodeId DiscreteAlarmType_ShelvingState_AvailableStates =
      new NodeId(UShort.MIN, uint(17692));

  public static final NodeId DiscreteAlarmType_ShelvingState_AvailableTransitions =
      new NodeId(UShort.MIN, uint(17693));

  public static final NodeId OffNormalAlarmType_ShelvingState_AvailableStates =
      new NodeId(UShort.MIN, uint(17694));

  public static final NodeId OffNormalAlarmType_ShelvingState_AvailableTransitions =
      new NodeId(UShort.MIN, uint(17695));

  public static final NodeId SystemOffNormalAlarmType_ShelvingState_AvailableStates =
      new NodeId(UShort.MIN, uint(17696));

  public static final NodeId SystemOffNormalAlarmType_ShelvingState_AvailableTransitions =
      new NodeId(UShort.MIN, uint(17697));

  public static final NodeId TripAlarmType_ShelvingState_AvailableStates =
      new NodeId(UShort.MIN, uint(17698));

  public static final NodeId TripAlarmType_ShelvingState_AvailableTransitions =
      new NodeId(UShort.MIN, uint(17699));

  public static final NodeId CertificateExpirationAlarmType_ShelvingState_AvailableStates =
      new NodeId(UShort.MIN, uint(17700));

  public static final NodeId CertificateExpirationAlarmType_ShelvingState_AvailableTransitions =
      new NodeId(UShort.MIN, uint(17701));

  public static final NodeId DiscrepancyAlarmType_ShelvingState_AvailableStates =
      new NodeId(UShort.MIN, uint(17702));

  public static final NodeId DiscrepancyAlarmType_ShelvingState_AvailableTransitions =
      new NodeId(UShort.MIN, uint(17703));

  public static final NodeId ProgramStateMachineType_AvailableStates =
      new NodeId(UShort.MIN, uint(17704));

  public static final NodeId ProgramStateMachineType_AvailableTransitions =
      new NodeId(UShort.MIN, uint(17705));

  public static final NodeId
      PublishSubscribeType_ConnectionName_Placeholder_TransportProfileUri_Selections =
          new NodeId(UShort.MIN, uint(17706));

  public static final NodeId
      PublishSubscribeType_ConnectionName_Placeholder_TransportProfileUri_SelectionDescriptions =
          new NodeId(UShort.MIN, uint(17707));

  public static final NodeId InterfaceTypes = new NodeId(UShort.MIN, uint(17708));

  public static final NodeId RationalNumberType = new NodeId(UShort.MIN, uint(17709));

  public static final NodeId PubSubConnectionType_TransportProfileUri_Selections =
      new NodeId(UShort.MIN, uint(17710));

  public static final NodeId PubSubConnectionType_TransportProfileUri_SelectionDescriptions =
      new NodeId(UShort.MIN, uint(17711));

  public static final NodeId RationalNumberType_Numerator = new NodeId(UShort.MIN, uint(17712));

  public static final NodeId RationalNumberType_Denominator = new NodeId(UShort.MIN, uint(17713));

  public static final NodeId VectorType = new NodeId(UShort.MIN, uint(17714));

  public static final NodeId VectorType_VectorUnit = new NodeId(UShort.MIN, uint(17715));

  public static final NodeId ThreeDVectorType = new NodeId(UShort.MIN, uint(17716));

  public static final NodeId ThreeDVectorType_VectorUnit = new NodeId(UShort.MIN, uint(17717));

  public static final NodeId
      FileDirectoryType_FileDirectoryName_Placeholder_DeleteFileSystemObject =
          new NodeId(UShort.MIN, uint(17718));

  public static final NodeId
      FileDirectoryType_FileDirectoryName_Placeholder_DeleteFileSystemObject_InputArguments =
          new NodeId(UShort.MIN, uint(17719));

  public static final NodeId PubSubConnectionAddReaderGroupGroupMethodType_OutputArguments =
      new NodeId(UShort.MIN, uint(17720));

  public static final NodeId ConnectionTransportType = new NodeId(UShort.MIN, uint(17721));

  public static final NodeId FileSystem_FileDirectoryName_Placeholder_DeleteFileSystemObject =
      new NodeId(UShort.MIN, uint(17722));

  public static final NodeId
      FileSystem_FileDirectoryName_Placeholder_DeleteFileSystemObject_InputArguments =
          new NodeId(UShort.MIN, uint(17723));

  public static final NodeId PubSubGroupType_MaxNetworkMessageSize =
      new NodeId(UShort.MIN, uint(17724));

  public static final NodeId WriterGroupType = new NodeId(UShort.MIN, uint(17725));

  public static final NodeId WriterGroupType_SecurityMode = new NodeId(UShort.MIN, uint(17726));

  public static final NodeId WriterGroupType_SecurityGroupId = new NodeId(UShort.MIN, uint(17727));

  public static final NodeId WriterGroupType_SecurityKeyServices =
      new NodeId(UShort.MIN, uint(17728));

  public static final NodeId WriterGroupType_MaxNetworkMessageSize =
      new NodeId(UShort.MIN, uint(17729));

  public static final NodeId WriterGroupType_Status = new NodeId(UShort.MIN, uint(17730));

  public static final NodeId WriterGroupType_Status_State = new NodeId(UShort.MIN, uint(17731));

  public static final NodeId AuthorizationServices = new NodeId(UShort.MIN, uint(17732));

  public static final NodeId WriterGroupType_Status_Enable = new NodeId(UShort.MIN, uint(17734));

  public static final NodeId WriterGroupType_Status_Disable = new NodeId(UShort.MIN, uint(17735));

  public static final NodeId WriterGroupType_WriterGroupId = new NodeId(UShort.MIN, uint(17736));

  public static final NodeId WriterGroupType_PublishingInterval =
      new NodeId(UShort.MIN, uint(17737));

  public static final NodeId WriterGroupType_KeepAliveTime = new NodeId(UShort.MIN, uint(17738));

  public static final NodeId WriterGroupType_Priority = new NodeId(UShort.MIN, uint(17739));

  public static final NodeId WriterGroupType_LocaleIds = new NodeId(UShort.MIN, uint(17740));

  public static final NodeId WriterGroupType_TransportSettings =
      new NodeId(UShort.MIN, uint(17741));

  public static final NodeId WriterGroupType_MessageSettings = new NodeId(UShort.MIN, uint(17742));

  public static final NodeId WriterGroupType_DataSetWriterName_Placeholder =
      new NodeId(UShort.MIN, uint(17743));

  public static final NodeId WriterGroupType_DataSetWriterName_Placeholder_DataSetWriterId =
      new NodeId(UShort.MIN, uint(17744));

  public static final NodeId WriterGroupType_DataSetWriterName_Placeholder_DataSetFieldContentMask =
      new NodeId(UShort.MIN, uint(17745));

  public static final NodeId WriterGroupType_DataSetWriterName_Placeholder_KeyFrameCount =
      new NodeId(UShort.MIN, uint(17746));

  public static final NodeId WriterGroupType_DataSetWriterName_Placeholder_TransportSettings =
      new NodeId(UShort.MIN, uint(17747));

  public static final NodeId WriterGroupType_DataSetWriterName_Placeholder_MessageSettings =
      new NodeId(UShort.MIN, uint(17748));

  public static final NodeId WriterGroupType_DataSetWriterName_Placeholder_Status =
      new NodeId(UShort.MIN, uint(17749));

  public static final NodeId WriterGroupType_DataSetWriterName_Placeholder_Status_State =
      new NodeId(UShort.MIN, uint(17750));

  public static final NodeId WriterGroupType_DataSetWriterName_Placeholder_Status_Enable =
      new NodeId(UShort.MIN, uint(17751));

  public static final NodeId WriterGroupType_DataSetWriterName_Placeholder_Status_Disable =
      new NodeId(UShort.MIN, uint(17752));

  public static final NodeId WriterGroupType_DataSetWriterName_Placeholder_Diagnostics =
      new NodeId(UShort.MIN, uint(17753));

  public static final NodeId
      WriterGroupType_DataSetWriterName_Placeholder_Diagnostics_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(17754));

  public static final NodeId
      WriterGroupType_DataSetWriterName_Placeholder_Diagnostics_TotalInformation =
          new NodeId(UShort.MIN, uint(17755));

  public static final NodeId
      WriterGroupType_DataSetWriterName_Placeholder_Diagnostics_TotalInformation_Active =
          new NodeId(UShort.MIN, uint(17756));

  public static final NodeId
      WriterGroupType_DataSetWriterName_Placeholder_Diagnostics_TotalInformation_Classification =
          new NodeId(UShort.MIN, uint(17757));

  public static final NodeId
      WriterGroupType_DataSetWriterName_Placeholder_Diagnostics_TotalInformation_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(17758));

  public static final NodeId
      WriterGroupType_DataSetWriterName_Placeholder_Diagnostics_TotalInformation_TimeFirstChange =
          new NodeId(UShort.MIN, uint(17759));

  public static final NodeId WriterGroupType_DataSetWriterName_Placeholder_Diagnostics_TotalError =
      new NodeId(UShort.MIN, uint(17760));

  public static final NodeId
      WriterGroupType_DataSetWriterName_Placeholder_Diagnostics_TotalError_Active =
          new NodeId(UShort.MIN, uint(17761));

  public static final NodeId
      WriterGroupType_DataSetWriterName_Placeholder_Diagnostics_TotalError_Classification =
          new NodeId(UShort.MIN, uint(17762));

  public static final NodeId
      WriterGroupType_DataSetWriterName_Placeholder_Diagnostics_TotalError_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(17763));

  public static final NodeId
      WriterGroupType_DataSetWriterName_Placeholder_Diagnostics_TotalError_TimeFirstChange =
          new NodeId(UShort.MIN, uint(17764));

  public static final NodeId WriterGroupType_DataSetWriterName_Placeholder_Diagnostics_Reset =
      new NodeId(UShort.MIN, uint(17765));

  public static final NodeId WriterGroupType_DataSetWriterName_Placeholder_Diagnostics_SubError =
      new NodeId(UShort.MIN, uint(17766));

  public static final NodeId WriterGroupType_DataSetWriterName_Placeholder_Diagnostics_Counters =
      new NodeId(UShort.MIN, uint(17767));

  public static final NodeId
      WriterGroupType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateError =
          new NodeId(UShort.MIN, uint(17768));

  public static final NodeId
      WriterGroupType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateError_Active =
          new NodeId(UShort.MIN, uint(17769));

  public static final NodeId
      WriterGroupType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateError_Classification =
          new NodeId(UShort.MIN, uint(17770));

  public static final NodeId
      WriterGroupType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateError_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(17771));

  public static final NodeId
      WriterGroupType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateError_TimeFirstChange =
          new NodeId(UShort.MIN, uint(17772));

  public static final NodeId
      WriterGroupType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateOperationalByMethod =
          new NodeId(UShort.MIN, uint(17773));

  public static final NodeId
      WriterGroupType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateOperationalByMethod_Active =
          new NodeId(UShort.MIN, uint(17774));

  public static final NodeId
      WriterGroupType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateOperationalByMethod_Classification =
          new NodeId(UShort.MIN, uint(17775));

  public static final NodeId
      WriterGroupType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateOperationalByMethod_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(17776));

  public static final NodeId
      WriterGroupType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateOperationalByMethod_TimeFirstChange =
          new NodeId(UShort.MIN, uint(17777));

  public static final NodeId
      WriterGroupType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateOperationalByParent =
          new NodeId(UShort.MIN, uint(17778));

  public static final NodeId
      WriterGroupType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateOperationalByParent_Active =
          new NodeId(UShort.MIN, uint(17779));

  public static final NodeId
      WriterGroupType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateOperationalByParent_Classification =
          new NodeId(UShort.MIN, uint(17780));

  public static final NodeId
      WriterGroupType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateOperationalByParent_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(17781));

  public static final NodeId
      WriterGroupType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateOperationalByParent_TimeFirstChange =
          new NodeId(UShort.MIN, uint(17782));

  public static final NodeId
      WriterGroupType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateOperationalFromError =
          new NodeId(UShort.MIN, uint(17783));

  public static final NodeId
      WriterGroupType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateOperationalFromError_Active =
          new NodeId(UShort.MIN, uint(17784));

  public static final NodeId
      WriterGroupType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateOperationalFromError_Classification =
          new NodeId(UShort.MIN, uint(17785));

  public static final NodeId
      WriterGroupType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateOperationalFromError_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(17786));

  public static final NodeId
      WriterGroupType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateOperationalFromError_TimeFirstChange =
          new NodeId(UShort.MIN, uint(17787));

  public static final NodeId
      WriterGroupType_DataSetWriterName_Placeholder_Diagnostics_Counters_StatePausedByParent =
          new NodeId(UShort.MIN, uint(17788));

  public static final NodeId
      WriterGroupType_DataSetWriterName_Placeholder_Diagnostics_Counters_StatePausedByParent_Active =
          new NodeId(UShort.MIN, uint(17789));

  public static final NodeId
      WriterGroupType_DataSetWriterName_Placeholder_Diagnostics_Counters_StatePausedByParent_Classification =
          new NodeId(UShort.MIN, uint(17790));

  public static final NodeId
      WriterGroupType_DataSetWriterName_Placeholder_Diagnostics_Counters_StatePausedByParent_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(17791));

  public static final NodeId
      WriterGroupType_DataSetWriterName_Placeholder_Diagnostics_Counters_StatePausedByParent_TimeFirstChange =
          new NodeId(UShort.MIN, uint(17792));

  public static final NodeId
      WriterGroupType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateDisabledByMethod =
          new NodeId(UShort.MIN, uint(17793));

  public static final NodeId
      WriterGroupType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateDisabledByMethod_Active =
          new NodeId(UShort.MIN, uint(17794));

  public static final NodeId
      WriterGroupType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateDisabledByMethod_Classification =
          new NodeId(UShort.MIN, uint(17795));

  public static final NodeId
      WriterGroupType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateDisabledByMethod_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(17796));

  public static final NodeId
      WriterGroupType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateDisabledByMethod_TimeFirstChange =
          new NodeId(UShort.MIN, uint(17797));

  public static final NodeId WriterGroupType_DataSetWriterName_Placeholder_Diagnostics_LiveValues =
      new NodeId(UShort.MIN, uint(17798));

  public static final NodeId
      WriterGroupType_DataSetWriterName_Placeholder_Diagnostics_Counters_FailedDataSetMessages =
          new NodeId(UShort.MIN, uint(17799));

  public static final NodeId
      WriterGroupType_DataSetWriterName_Placeholder_Diagnostics_Counters_FailedDataSetMessages_Active =
          new NodeId(UShort.MIN, uint(17800));

  public static final NodeId
      WriterGroupType_DataSetWriterName_Placeholder_Diagnostics_Counters_FailedDataSetMessages_Classification =
          new NodeId(UShort.MIN, uint(17801));

  public static final NodeId
      WriterGroupType_DataSetWriterName_Placeholder_Diagnostics_Counters_FailedDataSetMessages_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(17802));

  public static final NodeId
      WriterGroupType_DataSetWriterName_Placeholder_Diagnostics_Counters_FailedDataSetMessages_TimeFirstChange =
          new NodeId(UShort.MIN, uint(17803));

  public static final NodeId
      WriterGroupType_DataSetWriterName_Placeholder_Diagnostics_LiveValues_MessageSequenceNumber =
          new NodeId(UShort.MIN, uint(17804));

  public static final NodeId
      WriterGroupType_DataSetWriterName_Placeholder_Diagnostics_LiveValues_MessageSequenceNumber_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(17805));

  public static final NodeId
      WriterGroupType_DataSetWriterName_Placeholder_Diagnostics_LiveValues_StatusCode =
          new NodeId(UShort.MIN, uint(17806));

  public static final NodeId
      WriterGroupType_DataSetWriterName_Placeholder_Diagnostics_LiveValues_StatusCode_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(17807));

  public static final NodeId
      WriterGroupType_DataSetWriterName_Placeholder_Diagnostics_LiveValues_MajorVersion =
          new NodeId(UShort.MIN, uint(17808));

  public static final NodeId
      WriterGroupType_DataSetWriterName_Placeholder_Diagnostics_LiveValues_MajorVersion_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(17809));

  public static final NodeId
      WriterGroupType_DataSetWriterName_Placeholder_Diagnostics_LiveValues_MinorVersion =
          new NodeId(UShort.MIN, uint(17810));

  public static final NodeId
      WriterGroupType_DataSetWriterName_Placeholder_Diagnostics_LiveValues_MinorVersion_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(17811));

  public static final NodeId WriterGroupType_Diagnostics = new NodeId(UShort.MIN, uint(17812));

  public static final NodeId WriterGroupType_Diagnostics_DiagnosticsLevel =
      new NodeId(UShort.MIN, uint(17813));

  public static final NodeId WriterGroupType_Diagnostics_TotalInformation =
      new NodeId(UShort.MIN, uint(17814));

  public static final NodeId WriterGroupType_Diagnostics_TotalInformation_Active =
      new NodeId(UShort.MIN, uint(17815));

  public static final NodeId WriterGroupType_Diagnostics_TotalInformation_Classification =
      new NodeId(UShort.MIN, uint(17816));

  public static final NodeId WriterGroupType_Diagnostics_TotalInformation_DiagnosticsLevel =
      new NodeId(UShort.MIN, uint(17817));

  public static final NodeId WriterGroupType_Diagnostics_TotalInformation_TimeFirstChange =
      new NodeId(UShort.MIN, uint(17818));

  public static final NodeId WriterGroupType_Diagnostics_TotalError =
      new NodeId(UShort.MIN, uint(17819));

  public static final NodeId WriterGroupType_Diagnostics_TotalError_Active =
      new NodeId(UShort.MIN, uint(17820));

  public static final NodeId WriterGroupType_Diagnostics_TotalError_Classification =
      new NodeId(UShort.MIN, uint(17821));

  public static final NodeId WriterGroupType_Diagnostics_TotalError_DiagnosticsLevel =
      new NodeId(UShort.MIN, uint(17822));

  public static final NodeId WriterGroupType_Diagnostics_TotalError_TimeFirstChange =
      new NodeId(UShort.MIN, uint(17823));

  public static final NodeId WriterGroupType_Diagnostics_Reset =
      new NodeId(UShort.MIN, uint(17824));

  public static final NodeId WriterGroupType_Diagnostics_SubError =
      new NodeId(UShort.MIN, uint(17825));

  public static final NodeId WriterGroupType_Diagnostics_Counters =
      new NodeId(UShort.MIN, uint(17826));

  public static final NodeId WriterGroupType_Diagnostics_Counters_StateError =
      new NodeId(UShort.MIN, uint(17827));

  public static final NodeId WriterGroupType_Diagnostics_Counters_StateError_Active =
      new NodeId(UShort.MIN, uint(17828));

  public static final NodeId WriterGroupType_Diagnostics_Counters_StateError_Classification =
      new NodeId(UShort.MIN, uint(17829));

  public static final NodeId WriterGroupType_Diagnostics_Counters_StateError_DiagnosticsLevel =
      new NodeId(UShort.MIN, uint(17830));

  public static final NodeId WriterGroupType_Diagnostics_Counters_StateError_TimeFirstChange =
      new NodeId(UShort.MIN, uint(17831));

  public static final NodeId WriterGroupType_Diagnostics_Counters_StateOperationalByMethod =
      new NodeId(UShort.MIN, uint(17832));

  public static final NodeId WriterGroupType_Diagnostics_Counters_StateOperationalByMethod_Active =
      new NodeId(UShort.MIN, uint(17833));

  public static final NodeId
      WriterGroupType_Diagnostics_Counters_StateOperationalByMethod_Classification =
          new NodeId(UShort.MIN, uint(17834));

  public static final NodeId
      WriterGroupType_Diagnostics_Counters_StateOperationalByMethod_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(17835));

  public static final NodeId
      WriterGroupType_Diagnostics_Counters_StateOperationalByMethod_TimeFirstChange =
          new NodeId(UShort.MIN, uint(17836));

  public static final NodeId WriterGroupType_Diagnostics_Counters_StateOperationalByParent =
      new NodeId(UShort.MIN, uint(17837));

  public static final NodeId WriterGroupType_Diagnostics_Counters_StateOperationalByParent_Active =
      new NodeId(UShort.MIN, uint(17838));

  public static final NodeId
      WriterGroupType_Diagnostics_Counters_StateOperationalByParent_Classification =
          new NodeId(UShort.MIN, uint(17839));

  public static final NodeId
      WriterGroupType_Diagnostics_Counters_StateOperationalByParent_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(17840));

  public static final NodeId
      WriterGroupType_Diagnostics_Counters_StateOperationalByParent_TimeFirstChange =
          new NodeId(UShort.MIN, uint(17841));

  public static final NodeId WriterGroupType_Diagnostics_Counters_StateOperationalFromError =
      new NodeId(UShort.MIN, uint(17842));

  public static final NodeId WriterGroupType_Diagnostics_Counters_StateOperationalFromError_Active =
      new NodeId(UShort.MIN, uint(17843));

  public static final NodeId
      WriterGroupType_Diagnostics_Counters_StateOperationalFromError_Classification =
          new NodeId(UShort.MIN, uint(17844));

  public static final NodeId
      WriterGroupType_Diagnostics_Counters_StateOperationalFromError_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(17845));

  public static final NodeId
      WriterGroupType_Diagnostics_Counters_StateOperationalFromError_TimeFirstChange =
          new NodeId(UShort.MIN, uint(17846));

  public static final NodeId WriterGroupType_Diagnostics_Counters_StatePausedByParent =
      new NodeId(UShort.MIN, uint(17847));

  public static final NodeId WriterGroupType_Diagnostics_Counters_StatePausedByParent_Active =
      new NodeId(UShort.MIN, uint(17848));

  public static final NodeId
      WriterGroupType_Diagnostics_Counters_StatePausedByParent_Classification =
          new NodeId(UShort.MIN, uint(17849));

  public static final NodeId
      WriterGroupType_Diagnostics_Counters_StatePausedByParent_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(17850));

  public static final NodeId
      WriterGroupType_Diagnostics_Counters_StatePausedByParent_TimeFirstChange =
          new NodeId(UShort.MIN, uint(17851));

  public static final NodeId AuthorizationServiceConfigurationType =
      new NodeId(UShort.MIN, uint(17852));

  public static final NodeId WriterGroupType_Diagnostics_Counters_StateDisabledByMethod =
      new NodeId(UShort.MIN, uint(17853));

  public static final NodeId WriterGroupType_Diagnostics_Counters_StateDisabledByMethod_Active =
      new NodeId(UShort.MIN, uint(17854));

  public static final NodeId
      WriterGroupType_Diagnostics_Counters_StateDisabledByMethod_Classification =
          new NodeId(UShort.MIN, uint(17855));

  public static final NodeId
      WriterGroupType_Diagnostics_Counters_StateDisabledByMethod_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(17856));

  public static final NodeId
      WriterGroupType_Diagnostics_Counters_StateDisabledByMethod_TimeFirstChange =
          new NodeId(UShort.MIN, uint(17857));

  public static final NodeId WriterGroupType_Diagnostics_LiveValues =
      new NodeId(UShort.MIN, uint(17858));

  public static final NodeId WriterGroupType_Diagnostics_Counters_SentNetworkMessages =
      new NodeId(UShort.MIN, uint(17859));

  public static final NodeId AuthorizationServiceConfigurationType_ServiceCertificate =
      new NodeId(UShort.MIN, uint(17860));

  public static final NodeId DecimalDataType = new NodeId(UShort.MIN, uint(17861));

  public static final NodeId DecimalDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(17862));

  public static final NodeId DecimalDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(17863));

  public static final NodeId WriterGroupType_Diagnostics_Counters_SentNetworkMessages_Active =
      new NodeId(UShort.MIN, uint(17864));

  public static final NodeId AlarmConditionType_AudibleSound_ListId =
      new NodeId(UShort.MIN, uint(17865));

  public static final NodeId AlarmConditionType_AudibleSound_AgencyId =
      new NodeId(UShort.MIN, uint(17866));

  public static final NodeId AlarmConditionType_AudibleSound_VersionId =
      new NodeId(UShort.MIN, uint(17867));

  public static final NodeId AlarmConditionType_Unsuppress = new NodeId(UShort.MIN, uint(17868));

  public static final NodeId AlarmConditionType_RemoveFromService =
      new NodeId(UShort.MIN, uint(17869));

  public static final NodeId AlarmConditionType_PlaceInService =
      new NodeId(UShort.MIN, uint(17870));

  public static final NodeId
      WriterGroupType_Diagnostics_Counters_SentNetworkMessages_Classification =
          new NodeId(UShort.MIN, uint(17871));

  public static final NodeId
      WriterGroupType_Diagnostics_Counters_SentNetworkMessages_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(17872));

  public static final NodeId
      WriterGroupType_Diagnostics_Counters_SentNetworkMessages_TimeFirstChange =
          new NodeId(UShort.MIN, uint(17873));

  public static final NodeId WriterGroupType_Diagnostics_Counters_FailedTransmissions =
      new NodeId(UShort.MIN, uint(17874));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_Unsuppress =
      new NodeId(UShort.MIN, uint(17875));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_RemoveFromService =
      new NodeId(UShort.MIN, uint(17876));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_PlaceInService =
      new NodeId(UShort.MIN, uint(17877));

  public static final NodeId WriterGroupType_Diagnostics_Counters_FailedTransmissions_Active =
      new NodeId(UShort.MIN, uint(17878));

  public static final NodeId LimitAlarmType_AudibleSound_ListId =
      new NodeId(UShort.MIN, uint(17879));

  public static final NodeId LimitAlarmType_AudibleSound_AgencyId =
      new NodeId(UShort.MIN, uint(17880));

  public static final NodeId LimitAlarmType_AudibleSound_VersionId =
      new NodeId(UShort.MIN, uint(17881));

  public static final NodeId LimitAlarmType_Unsuppress = new NodeId(UShort.MIN, uint(17882));

  public static final NodeId LimitAlarmType_RemoveFromService = new NodeId(UShort.MIN, uint(17883));

  public static final NodeId LimitAlarmType_PlaceInService = new NodeId(UShort.MIN, uint(17884));

  public static final NodeId
      WriterGroupType_Diagnostics_Counters_FailedTransmissions_Classification =
          new NodeId(UShort.MIN, uint(17885));

  public static final NodeId ExclusiveLimitAlarmType_AudibleSound_ListId =
      new NodeId(UShort.MIN, uint(17886));

  public static final NodeId ExclusiveLimitAlarmType_AudibleSound_AgencyId =
      new NodeId(UShort.MIN, uint(17887));

  public static final NodeId ExclusiveLimitAlarmType_AudibleSound_VersionId =
      new NodeId(UShort.MIN, uint(17888));

  public static final NodeId ExclusiveLimitAlarmType_Unsuppress =
      new NodeId(UShort.MIN, uint(17889));

  public static final NodeId ExclusiveLimitAlarmType_RemoveFromService =
      new NodeId(UShort.MIN, uint(17890));

  public static final NodeId ExclusiveLimitAlarmType_PlaceInService =
      new NodeId(UShort.MIN, uint(17891));

  public static final NodeId
      WriterGroupType_Diagnostics_Counters_FailedTransmissions_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(17892));

  public static final NodeId NonExclusiveLimitAlarmType_AudibleSound_ListId =
      new NodeId(UShort.MIN, uint(17893));

  public static final NodeId NonExclusiveLimitAlarmType_AudibleSound_AgencyId =
      new NodeId(UShort.MIN, uint(17894));

  public static final NodeId NonExclusiveLimitAlarmType_AudibleSound_VersionId =
      new NodeId(UShort.MIN, uint(17895));

  public static final NodeId NonExclusiveLimitAlarmType_Unsuppress =
      new NodeId(UShort.MIN, uint(17896));

  public static final NodeId NonExclusiveLimitAlarmType_RemoveFromService =
      new NodeId(UShort.MIN, uint(17897));

  public static final NodeId NonExclusiveLimitAlarmType_PlaceInService =
      new NodeId(UShort.MIN, uint(17898));

  public static final NodeId
      WriterGroupType_Diagnostics_Counters_FailedTransmissions_TimeFirstChange =
          new NodeId(UShort.MIN, uint(17899));

  public static final NodeId WriterGroupType_Diagnostics_Counters_EncryptionErrors =
      new NodeId(UShort.MIN, uint(17900));

  public static final NodeId WriterGroupType_Diagnostics_Counters_EncryptionErrors_Active =
      new NodeId(UShort.MIN, uint(17901));

  public static final NodeId WriterGroupType_Diagnostics_Counters_EncryptionErrors_Classification =
      new NodeId(UShort.MIN, uint(17902));

  public static final NodeId
      WriterGroupType_Diagnostics_Counters_EncryptionErrors_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(17903));

  public static final NodeId NonExclusiveLevelAlarmType_RemoveFromService =
      new NodeId(UShort.MIN, uint(17904));

  public static final NodeId NonExclusiveLevelAlarmType_PlaceInService =
      new NodeId(UShort.MIN, uint(17905));

  public static final NodeId WriterGroupType_Diagnostics_Counters_EncryptionErrors_TimeFirstChange =
      new NodeId(UShort.MIN, uint(17906));

  public static final NodeId ExclusiveLevelAlarmType_AudibleSound_ListId =
      new NodeId(UShort.MIN, uint(17907));

  public static final NodeId ExclusiveLevelAlarmType_AudibleSound_AgencyId =
      new NodeId(UShort.MIN, uint(17908));

  public static final NodeId ExclusiveLevelAlarmType_AudibleSound_VersionId =
      new NodeId(UShort.MIN, uint(17909));

  public static final NodeId ExclusiveLevelAlarmType_Unsuppress =
      new NodeId(UShort.MIN, uint(17910));

  public static final NodeId ExclusiveLevelAlarmType_RemoveFromService =
      new NodeId(UShort.MIN, uint(17911));

  public static final NodeId ExclusiveLevelAlarmType_PlaceInService =
      new NodeId(UShort.MIN, uint(17912));

  public static final NodeId WriterGroupType_Diagnostics_LiveValues_ConfiguredDataSetWriters =
      new NodeId(UShort.MIN, uint(17913));

  public static final NodeId NonExclusiveDeviationAlarmType_AudibleSound_ListId =
      new NodeId(UShort.MIN, uint(17914));

  public static final NodeId NonExclusiveDeviationAlarmType_AudibleSound_AgencyId =
      new NodeId(UShort.MIN, uint(17915));

  public static final NodeId NonExclusiveDeviationAlarmType_AudibleSound_VersionId =
      new NodeId(UShort.MIN, uint(17916));

  public static final NodeId NonExclusiveDeviationAlarmType_Unsuppress =
      new NodeId(UShort.MIN, uint(17917));

  public static final NodeId NonExclusiveDeviationAlarmType_RemoveFromService =
      new NodeId(UShort.MIN, uint(17918));

  public static final NodeId NonExclusiveDeviationAlarmType_PlaceInService =
      new NodeId(UShort.MIN, uint(17919));

  public static final NodeId
      WriterGroupType_Diagnostics_LiveValues_ConfiguredDataSetWriters_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(17920));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_AudibleSound_ListId =
      new NodeId(UShort.MIN, uint(17921));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_AudibleSound_AgencyId =
      new NodeId(UShort.MIN, uint(17922));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_AudibleSound_VersionId =
      new NodeId(UShort.MIN, uint(17923));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_Unsuppress =
      new NodeId(UShort.MIN, uint(17924));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_RemoveFromService =
      new NodeId(UShort.MIN, uint(17925));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_PlaceInService =
      new NodeId(UShort.MIN, uint(17926));

  public static final NodeId WriterGroupType_Diagnostics_LiveValues_OperationalDataSetWriters =
      new NodeId(UShort.MIN, uint(17927));

  public static final NodeId ExclusiveDeviationAlarmType_AudibleSound_ListId =
      new NodeId(UShort.MIN, uint(17928));

  public static final NodeId ExclusiveDeviationAlarmType_AudibleSound_AgencyId =
      new NodeId(UShort.MIN, uint(17929));

  public static final NodeId ExclusiveDeviationAlarmType_AudibleSound_VersionId =
      new NodeId(UShort.MIN, uint(17930));

  public static final NodeId ExclusiveDeviationAlarmType_Unsuppress =
      new NodeId(UShort.MIN, uint(17931));

  public static final NodeId ExclusiveDeviationAlarmType_RemoveFromService =
      new NodeId(UShort.MIN, uint(17932));

  public static final NodeId ExclusiveDeviationAlarmType_PlaceInService =
      new NodeId(UShort.MIN, uint(17933));

  public static final NodeId
      WriterGroupType_Diagnostics_LiveValues_OperationalDataSetWriters_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(17934));

  public static final NodeId ExclusiveRateOfChangeAlarmType_AudibleSound_ListId =
      new NodeId(UShort.MIN, uint(17935));

  public static final NodeId ExclusiveRateOfChangeAlarmType_AudibleSound_AgencyId =
      new NodeId(UShort.MIN, uint(17936));

  public static final NodeId ExclusiveRateOfChangeAlarmType_AudibleSound_VersionId =
      new NodeId(UShort.MIN, uint(17937));

  public static final NodeId ExclusiveRateOfChangeAlarmType_Unsuppress =
      new NodeId(UShort.MIN, uint(17938));

  public static final NodeId ExclusiveRateOfChangeAlarmType_RemoveFromService =
      new NodeId(UShort.MIN, uint(17939));

  public static final NodeId ExclusiveRateOfChangeAlarmType_PlaceInService =
      new NodeId(UShort.MIN, uint(17940));

  public static final NodeId WriterGroupType_Diagnostics_LiveValues_SecurityTokenID =
      new NodeId(UShort.MIN, uint(17941));

  public static final NodeId DiscreteAlarmType_AudibleSound_ListId =
      new NodeId(UShort.MIN, uint(17942));

  public static final NodeId DiscreteAlarmType_AudibleSound_AgencyId =
      new NodeId(UShort.MIN, uint(17943));

  public static final NodeId DiscreteAlarmType_AudibleSound_VersionId =
      new NodeId(UShort.MIN, uint(17944));

  public static final NodeId DiscreteAlarmType_Unsuppress = new NodeId(UShort.MIN, uint(17945));

  public static final NodeId DiscreteAlarmType_RemoveFromService =
      new NodeId(UShort.MIN, uint(17946));

  public static final NodeId DiscreteAlarmType_PlaceInService = new NodeId(UShort.MIN, uint(17947));

  public static final NodeId
      WriterGroupType_Diagnostics_LiveValues_SecurityTokenID_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(17948));

  public static final NodeId OffNormalAlarmType_AudibleSound_ListId =
      new NodeId(UShort.MIN, uint(17949));

  public static final NodeId OffNormalAlarmType_AudibleSound_AgencyId =
      new NodeId(UShort.MIN, uint(17950));

  public static final NodeId OffNormalAlarmType_AudibleSound_VersionId =
      new NodeId(UShort.MIN, uint(17951));

  public static final NodeId OffNormalAlarmType_Unsuppress = new NodeId(UShort.MIN, uint(17952));

  public static final NodeId OffNormalAlarmType_RemoveFromService =
      new NodeId(UShort.MIN, uint(17953));

  public static final NodeId OffNormalAlarmType_PlaceInService =
      new NodeId(UShort.MIN, uint(17954));

  public static final NodeId WriterGroupType_Diagnostics_LiveValues_TimeToNextTokenID =
      new NodeId(UShort.MIN, uint(17955));

  public static final NodeId SystemOffNormalAlarmType_AudibleSound_ListId =
      new NodeId(UShort.MIN, uint(17956));

  public static final NodeId SystemOffNormalAlarmType_AudibleSound_AgencyId =
      new NodeId(UShort.MIN, uint(17957));

  public static final NodeId SystemOffNormalAlarmType_AudibleSound_VersionId =
      new NodeId(UShort.MIN, uint(17958));

  public static final NodeId SystemOffNormalAlarmType_Unsuppress =
      new NodeId(UShort.MIN, uint(17959));

  public static final NodeId SystemOffNormalAlarmType_RemoveFromService =
      new NodeId(UShort.MIN, uint(17960));

  public static final NodeId SystemOffNormalAlarmType_PlaceInService =
      new NodeId(UShort.MIN, uint(17961));

  public static final NodeId
      WriterGroupType_Diagnostics_LiveValues_TimeToNextTokenID_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(17962));

  public static final NodeId TripAlarmType_AudibleSound_ListId =
      new NodeId(UShort.MIN, uint(17963));

  public static final NodeId TripAlarmType_AudibleSound_AgencyId =
      new NodeId(UShort.MIN, uint(17964));

  public static final NodeId TripAlarmType_AudibleSound_VersionId =
      new NodeId(UShort.MIN, uint(17965));

  public static final NodeId TripAlarmType_Unsuppress = new NodeId(UShort.MIN, uint(17966));

  public static final NodeId TripAlarmType_RemoveFromService = new NodeId(UShort.MIN, uint(17967));

  public static final NodeId TripAlarmType_PlaceInService = new NodeId(UShort.MIN, uint(17968));

  public static final NodeId WriterGroupType_AddDataSetWriter = new NodeId(UShort.MIN, uint(17969));

  public static final NodeId CertificateExpirationAlarmType_AudibleSound_ListId =
      new NodeId(UShort.MIN, uint(17970));

  public static final NodeId CertificateExpirationAlarmType_AudibleSound_AgencyId =
      new NodeId(UShort.MIN, uint(17971));

  public static final NodeId CertificateExpirationAlarmType_AudibleSound_VersionId =
      new NodeId(UShort.MIN, uint(17972));

  public static final NodeId CertificateExpirationAlarmType_Unsuppress =
      new NodeId(UShort.MIN, uint(17973));

  public static final NodeId CertificateExpirationAlarmType_RemoveFromService =
      new NodeId(UShort.MIN, uint(17974));

  public static final NodeId CertificateExpirationAlarmType_PlaceInService =
      new NodeId(UShort.MIN, uint(17975));

  public static final NodeId WriterGroupType_AddDataSetWriter_InputArguments =
      new NodeId(UShort.MIN, uint(17976));

  public static final NodeId DiscrepancyAlarmType_AudibleSound_ListId =
      new NodeId(UShort.MIN, uint(17977));

  public static final NodeId DiscrepancyAlarmType_AudibleSound_AgencyId =
      new NodeId(UShort.MIN, uint(17978));

  public static final NodeId DiscrepancyAlarmType_AudibleSound_VersionId =
      new NodeId(UShort.MIN, uint(17979));

  public static final NodeId DiscrepancyAlarmType_Unsuppress = new NodeId(UShort.MIN, uint(17980));

  public static final NodeId DiscrepancyAlarmType_RemoveFromService =
      new NodeId(UShort.MIN, uint(17981));

  public static final NodeId DiscrepancyAlarmType_PlaceInService =
      new NodeId(UShort.MIN, uint(17982));

  public static final NodeId HasEffectEnable = new NodeId(UShort.MIN, uint(17983));

  public static final NodeId HasEffectSuppressed = new NodeId(UShort.MIN, uint(17984));

  public static final NodeId HasEffectUnsuppressed = new NodeId(UShort.MIN, uint(17985));

  public static final NodeId AudioVariableType = new NodeId(UShort.MIN, uint(17986));

  public static final NodeId WriterGroupType_AddDataSetWriter_OutputArguments =
      new NodeId(UShort.MIN, uint(17987));

  public static final NodeId AudioVariableType_ListId = new NodeId(UShort.MIN, uint(17988));

  public static final NodeId AudioVariableType_AgencyId = new NodeId(UShort.MIN, uint(17989));

  public static final NodeId AudioVariableType_VersionId = new NodeId(UShort.MIN, uint(17990));

  public static final NodeId AlarmMetricsType_StartTime = new NodeId(UShort.MIN, uint(17991));

  public static final NodeId WriterGroupType_RemoveDataSetWriter =
      new NodeId(UShort.MIN, uint(17992));

  public static final NodeId WriterGroupType_RemoveDataSetWriter_InputArguments =
      new NodeId(UShort.MIN, uint(17993));

  public static final NodeId PubSubGroupTypeAddWriterMethodType =
      new NodeId(UShort.MIN, uint(17994));

  public static final NodeId PubSubGroupTypeAddWriterMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(17995));

  public static final NodeId PubSubGroupTypeAddWriterMethodType_OutputArguments =
      new NodeId(UShort.MIN, uint(17996));

  public static final NodeId WriterGroupTransportType = new NodeId(UShort.MIN, uint(17997));

  public static final NodeId WriterGroupMessageType = new NodeId(UShort.MIN, uint(17998));

  public static final NodeId ReaderGroupType = new NodeId(UShort.MIN, uint(17999));

  public static final NodeId ReaderGroupType_SecurityMode = new NodeId(UShort.MIN, uint(18000));

  public static final NodeId KeyCredentialConfigurationType = new NodeId(UShort.MIN, uint(18001));

  public static final NodeId ReaderGroupType_SecurityGroupId = new NodeId(UShort.MIN, uint(18002));

  public static final NodeId ReaderGroupType_SecurityKeyServices =
      new NodeId(UShort.MIN, uint(18003));

  public static final NodeId KeyCredentialConfigurationType_EndpointUrls =
      new NodeId(UShort.MIN, uint(18004));

  public static final NodeId KeyCredentialConfigurationType_ServiceStatus =
      new NodeId(UShort.MIN, uint(18005));

  public static final NodeId KeyCredentialConfigurationType_UpdateCredential =
      new NodeId(UShort.MIN, uint(18006));

  public static final NodeId KeyCredentialConfigurationType_UpdateCredential_InputArguments =
      new NodeId(UShort.MIN, uint(18007));

  public static final NodeId KeyCredentialConfigurationType_DeleteCredential =
      new NodeId(UShort.MIN, uint(18008));

  public static final NodeId KeyCredentialUpdateMethodType = new NodeId(UShort.MIN, uint(18009));

  public static final NodeId KeyCredentialUpdateMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(18010));

  public static final NodeId KeyCredentialAuditEventType = new NodeId(UShort.MIN, uint(18011));

  public static final NodeId KeyCredentialAuditEventType_EventId =
      new NodeId(UShort.MIN, uint(18012));

  public static final NodeId KeyCredentialAuditEventType_EventType =
      new NodeId(UShort.MIN, uint(18013));

  public static final NodeId KeyCredentialAuditEventType_SourceNode =
      new NodeId(UShort.MIN, uint(18014));

  public static final NodeId KeyCredentialAuditEventType_SourceName =
      new NodeId(UShort.MIN, uint(18015));

  public static final NodeId KeyCredentialAuditEventType_Time = new NodeId(UShort.MIN, uint(18016));

  public static final NodeId KeyCredentialAuditEventType_ReceiveTime =
      new NodeId(UShort.MIN, uint(18017));

  public static final NodeId KeyCredentialAuditEventType_LocalTime =
      new NodeId(UShort.MIN, uint(18018));

  public static final NodeId KeyCredentialAuditEventType_Message =
      new NodeId(UShort.MIN, uint(18019));

  public static final NodeId KeyCredentialAuditEventType_Severity =
      new NodeId(UShort.MIN, uint(18020));

  public static final NodeId KeyCredentialAuditEventType_ActionTimeStamp =
      new NodeId(UShort.MIN, uint(18021));

  public static final NodeId KeyCredentialAuditEventType_Status =
      new NodeId(UShort.MIN, uint(18022));

  public static final NodeId KeyCredentialAuditEventType_ServerId =
      new NodeId(UShort.MIN, uint(18023));

  public static final NodeId KeyCredentialAuditEventType_ClientAuditEntryId =
      new NodeId(UShort.MIN, uint(18024));

  public static final NodeId KeyCredentialAuditEventType_ClientUserId =
      new NodeId(UShort.MIN, uint(18025));

  public static final NodeId KeyCredentialAuditEventType_MethodId =
      new NodeId(UShort.MIN, uint(18026));

  public static final NodeId KeyCredentialAuditEventType_InputArguments =
      new NodeId(UShort.MIN, uint(18027));

  public static final NodeId KeyCredentialAuditEventType_ResourceUri =
      new NodeId(UShort.MIN, uint(18028));

  public static final NodeId KeyCredentialUpdatedAuditEventType =
      new NodeId(UShort.MIN, uint(18029));

  public static final NodeId KeyCredentialUpdatedAuditEventType_EventId =
      new NodeId(UShort.MIN, uint(18030));

  public static final NodeId KeyCredentialUpdatedAuditEventType_EventType =
      new NodeId(UShort.MIN, uint(18031));

  public static final NodeId KeyCredentialUpdatedAuditEventType_SourceNode =
      new NodeId(UShort.MIN, uint(18032));

  public static final NodeId KeyCredentialUpdatedAuditEventType_SourceName =
      new NodeId(UShort.MIN, uint(18033));

  public static final NodeId KeyCredentialUpdatedAuditEventType_Time =
      new NodeId(UShort.MIN, uint(18034));

  public static final NodeId KeyCredentialUpdatedAuditEventType_ReceiveTime =
      new NodeId(UShort.MIN, uint(18035));

  public static final NodeId KeyCredentialUpdatedAuditEventType_LocalTime =
      new NodeId(UShort.MIN, uint(18036));

  public static final NodeId KeyCredentialUpdatedAuditEventType_Message =
      new NodeId(UShort.MIN, uint(18037));

  public static final NodeId KeyCredentialUpdatedAuditEventType_Severity =
      new NodeId(UShort.MIN, uint(18038));

  public static final NodeId KeyCredentialUpdatedAuditEventType_ActionTimeStamp =
      new NodeId(UShort.MIN, uint(18039));

  public static final NodeId KeyCredentialUpdatedAuditEventType_Status =
      new NodeId(UShort.MIN, uint(18040));

  public static final NodeId KeyCredentialUpdatedAuditEventType_ServerId =
      new NodeId(UShort.MIN, uint(18041));

  public static final NodeId KeyCredentialUpdatedAuditEventType_ClientAuditEntryId =
      new NodeId(UShort.MIN, uint(18042));

  public static final NodeId KeyCredentialUpdatedAuditEventType_ClientUserId =
      new NodeId(UShort.MIN, uint(18043));

  public static final NodeId KeyCredentialUpdatedAuditEventType_MethodId =
      new NodeId(UShort.MIN, uint(18044));

  public static final NodeId KeyCredentialUpdatedAuditEventType_InputArguments =
      new NodeId(UShort.MIN, uint(18045));

  public static final NodeId KeyCredentialUpdatedAuditEventType_ResourceUri =
      new NodeId(UShort.MIN, uint(18046));

  public static final NodeId KeyCredentialDeletedAuditEventType =
      new NodeId(UShort.MIN, uint(18047));

  public static final NodeId KeyCredentialDeletedAuditEventType_EventId =
      new NodeId(UShort.MIN, uint(18048));

  public static final NodeId KeyCredentialDeletedAuditEventType_EventType =
      new NodeId(UShort.MIN, uint(18049));

  public static final NodeId KeyCredentialDeletedAuditEventType_SourceNode =
      new NodeId(UShort.MIN, uint(18050));

  public static final NodeId KeyCredentialDeletedAuditEventType_SourceName =
      new NodeId(UShort.MIN, uint(18051));

  public static final NodeId KeyCredentialDeletedAuditEventType_Time =
      new NodeId(UShort.MIN, uint(18052));

  public static final NodeId KeyCredentialDeletedAuditEventType_ReceiveTime =
      new NodeId(UShort.MIN, uint(18053));

  public static final NodeId KeyCredentialDeletedAuditEventType_LocalTime =
      new NodeId(UShort.MIN, uint(18054));

  public static final NodeId KeyCredentialDeletedAuditEventType_Message =
      new NodeId(UShort.MIN, uint(18055));

  public static final NodeId KeyCredentialDeletedAuditEventType_Severity =
      new NodeId(UShort.MIN, uint(18056));

  public static final NodeId KeyCredentialDeletedAuditEventType_ActionTimeStamp =
      new NodeId(UShort.MIN, uint(18057));

  public static final NodeId KeyCredentialDeletedAuditEventType_Status =
      new NodeId(UShort.MIN, uint(18058));

  public static final NodeId KeyCredentialDeletedAuditEventType_ServerId =
      new NodeId(UShort.MIN, uint(18059));

  public static final NodeId KeyCredentialDeletedAuditEventType_ClientAuditEntryId =
      new NodeId(UShort.MIN, uint(18060));

  public static final NodeId KeyCredentialDeletedAuditEventType_ClientUserId =
      new NodeId(UShort.MIN, uint(18061));

  public static final NodeId KeyCredentialDeletedAuditEventType_MethodId =
      new NodeId(UShort.MIN, uint(18062));

  public static final NodeId KeyCredentialDeletedAuditEventType_InputArguments =
      new NodeId(UShort.MIN, uint(18063));

  public static final NodeId KeyCredentialDeletedAuditEventType_ResourceUri =
      new NodeId(UShort.MIN, uint(18064));

  public static final NodeId ReaderGroupType_MaxNetworkMessageSize =
      new NodeId(UShort.MIN, uint(18065));

  public static final NodeId ReaderGroupType_Status = new NodeId(UShort.MIN, uint(18067));

  public static final NodeId ReaderGroupType_Status_State = new NodeId(UShort.MIN, uint(18068));

  public static final NodeId KeyCredentialConfigurationType_ResourceUri =
      new NodeId(UShort.MIN, uint(18069));

  public static final NodeId AuthorizationServiceConfigurationType_ServiceUri =
      new NodeId(UShort.MIN, uint(18072));

  public static final NodeId AuthorizationServiceConfigurationType_IssuerEndpointUrl =
      new NodeId(UShort.MIN, uint(18073));

  public static final NodeId ReaderGroupType_Status_Enable = new NodeId(UShort.MIN, uint(18074));

  public static final NodeId ReaderGroupType_Status_Disable = new NodeId(UShort.MIN, uint(18075));

  public static final NodeId ReaderGroupType_DataSetReaderName_Placeholder =
      new NodeId(UShort.MIN, uint(18076));

  public static final NodeId ReaderGroupType_DataSetReaderName_Placeholder_PublisherId =
      new NodeId(UShort.MIN, uint(18077));

  public static final NodeId ReaderGroupType_DataSetReaderName_Placeholder_WriterGroupId =
      new NodeId(UShort.MIN, uint(18078));

  public static final NodeId ReaderGroupType_DataSetReaderName_Placeholder_DataSetWriterId =
      new NodeId(UShort.MIN, uint(18079));

  public static final NodeId ReaderGroupType_DataSetReaderName_Placeholder_DataSetMetaData =
      new NodeId(UShort.MIN, uint(18080));

  public static final NodeId ReaderGroupType_DataSetReaderName_Placeholder_DataSetFieldContentMask =
      new NodeId(UShort.MIN, uint(18081));

  public static final NodeId ReaderGroupType_DataSetReaderName_Placeholder_MessageReceiveTimeout =
      new NodeId(UShort.MIN, uint(18082));

  public static final NodeId ReaderGroupType_DataSetReaderName_Placeholder_SecurityMode =
      new NodeId(UShort.MIN, uint(18083));

  public static final NodeId ReaderGroupType_DataSetReaderName_Placeholder_SecurityGroupId =
      new NodeId(UShort.MIN, uint(18084));

  public static final NodeId ReaderGroupType_DataSetReaderName_Placeholder_SecurityKeyServices =
      new NodeId(UShort.MIN, uint(18085));

  public static final NodeId ReaderGroupType_DataSetReaderName_Placeholder_TransportSettings =
      new NodeId(UShort.MIN, uint(18086));

  public static final NodeId ReaderGroupType_DataSetReaderName_Placeholder_MessageSettings =
      new NodeId(UShort.MIN, uint(18087));

  public static final NodeId ReaderGroupType_DataSetReaderName_Placeholder_Status =
      new NodeId(UShort.MIN, uint(18088));

  public static final NodeId ReaderGroupType_DataSetReaderName_Placeholder_Status_State =
      new NodeId(UShort.MIN, uint(18089));

  public static final NodeId ReaderGroupType_DataSetReaderName_Placeholder_Status_Enable =
      new NodeId(UShort.MIN, uint(18090));

  public static final NodeId ReaderGroupType_DataSetReaderName_Placeholder_Status_Disable =
      new NodeId(UShort.MIN, uint(18091));

  public static final NodeId ReaderGroupType_DataSetReaderName_Placeholder_Diagnostics =
      new NodeId(UShort.MIN, uint(18092));

  public static final NodeId
      ReaderGroupType_DataSetReaderName_Placeholder_Diagnostics_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(18093));

  public static final NodeId
      ReaderGroupType_DataSetReaderName_Placeholder_Diagnostics_TotalInformation =
          new NodeId(UShort.MIN, uint(18094));

  public static final NodeId
      ReaderGroupType_DataSetReaderName_Placeholder_Diagnostics_TotalInformation_Active =
          new NodeId(UShort.MIN, uint(18095));

  public static final NodeId
      ReaderGroupType_DataSetReaderName_Placeholder_Diagnostics_TotalInformation_Classification =
          new NodeId(UShort.MIN, uint(18096));

  public static final NodeId
      ReaderGroupType_DataSetReaderName_Placeholder_Diagnostics_TotalInformation_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(18097));

  public static final NodeId
      ReaderGroupType_DataSetReaderName_Placeholder_Diagnostics_TotalInformation_TimeFirstChange =
          new NodeId(UShort.MIN, uint(18098));

  public static final NodeId ReaderGroupType_DataSetReaderName_Placeholder_Diagnostics_TotalError =
      new NodeId(UShort.MIN, uint(18099));

  public static final NodeId
      ReaderGroupType_DataSetReaderName_Placeholder_Diagnostics_TotalError_Active =
          new NodeId(UShort.MIN, uint(18100));

  public static final NodeId
      ReaderGroupType_DataSetReaderName_Placeholder_Diagnostics_TotalError_Classification =
          new NodeId(UShort.MIN, uint(18101));

  public static final NodeId
      ReaderGroupType_DataSetReaderName_Placeholder_Diagnostics_TotalError_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(18102));

  public static final NodeId
      ReaderGroupType_DataSetReaderName_Placeholder_Diagnostics_TotalError_TimeFirstChange =
          new NodeId(UShort.MIN, uint(18103));

  public static final NodeId ReaderGroupType_DataSetReaderName_Placeholder_Diagnostics_Reset =
      new NodeId(UShort.MIN, uint(18104));

  public static final NodeId ReaderGroupType_DataSetReaderName_Placeholder_Diagnostics_SubError =
      new NodeId(UShort.MIN, uint(18105));

  public static final NodeId ReaderGroupType_DataSetReaderName_Placeholder_Diagnostics_Counters =
      new NodeId(UShort.MIN, uint(18106));

  public static final NodeId
      ReaderGroupType_DataSetReaderName_Placeholder_Diagnostics_Counters_StateError =
          new NodeId(UShort.MIN, uint(18107));

  public static final NodeId
      ReaderGroupType_DataSetReaderName_Placeholder_Diagnostics_Counters_StateError_Active =
          new NodeId(UShort.MIN, uint(18108));

  public static final NodeId
      ReaderGroupType_DataSetReaderName_Placeholder_Diagnostics_Counters_StateError_Classification =
          new NodeId(UShort.MIN, uint(18109));

  public static final NodeId
      ReaderGroupType_DataSetReaderName_Placeholder_Diagnostics_Counters_StateError_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(18110));

  public static final NodeId
      ReaderGroupType_DataSetReaderName_Placeholder_Diagnostics_Counters_StateError_TimeFirstChange =
          new NodeId(UShort.MIN, uint(18111));

  public static final NodeId
      ReaderGroupType_DataSetReaderName_Placeholder_Diagnostics_Counters_StateOperationalByMethod =
          new NodeId(UShort.MIN, uint(18112));

  public static final NodeId
      ReaderGroupType_DataSetReaderName_Placeholder_Diagnostics_Counters_StateOperationalByMethod_Active =
          new NodeId(UShort.MIN, uint(18113));

  public static final NodeId
      ReaderGroupType_DataSetReaderName_Placeholder_Diagnostics_Counters_StateOperationalByMethod_Classification =
          new NodeId(UShort.MIN, uint(18114));

  public static final NodeId
      ReaderGroupType_DataSetReaderName_Placeholder_Diagnostics_Counters_StateOperationalByMethod_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(18115));

  public static final NodeId
      ReaderGroupType_DataSetReaderName_Placeholder_Diagnostics_Counters_StateOperationalByMethod_TimeFirstChange =
          new NodeId(UShort.MIN, uint(18116));

  public static final NodeId
      ReaderGroupType_DataSetReaderName_Placeholder_Diagnostics_Counters_StateOperationalByParent =
          new NodeId(UShort.MIN, uint(18117));

  public static final NodeId
      ReaderGroupType_DataSetReaderName_Placeholder_Diagnostics_Counters_StateOperationalByParent_Active =
          new NodeId(UShort.MIN, uint(18118));

  public static final NodeId
      ReaderGroupType_DataSetReaderName_Placeholder_Diagnostics_Counters_StateOperationalByParent_Classification =
          new NodeId(UShort.MIN, uint(18119));

  public static final NodeId
      ReaderGroupType_DataSetReaderName_Placeholder_Diagnostics_Counters_StateOperationalByParent_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(18120));

  public static final NodeId
      ReaderGroupType_DataSetReaderName_Placeholder_Diagnostics_Counters_StateOperationalByParent_TimeFirstChange =
          new NodeId(UShort.MIN, uint(18121));

  public static final NodeId
      ReaderGroupType_DataSetReaderName_Placeholder_Diagnostics_Counters_StateOperationalFromError =
          new NodeId(UShort.MIN, uint(18122));

  public static final NodeId
      ReaderGroupType_DataSetReaderName_Placeholder_Diagnostics_Counters_StateOperationalFromError_Active =
          new NodeId(UShort.MIN, uint(18123));

  public static final NodeId
      ReaderGroupType_DataSetReaderName_Placeholder_Diagnostics_Counters_StateOperationalFromError_Classification =
          new NodeId(UShort.MIN, uint(18124));

  public static final NodeId
      ReaderGroupType_DataSetReaderName_Placeholder_Diagnostics_Counters_StateOperationalFromError_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(18125));

  public static final NodeId
      ReaderGroupType_DataSetReaderName_Placeholder_Diagnostics_Counters_StateOperationalFromError_TimeFirstChange =
          new NodeId(UShort.MIN, uint(18126));

  public static final NodeId
      ReaderGroupType_DataSetReaderName_Placeholder_Diagnostics_Counters_StatePausedByParent =
          new NodeId(UShort.MIN, uint(18127));

  public static final NodeId
      ReaderGroupType_DataSetReaderName_Placeholder_Diagnostics_Counters_StatePausedByParent_Active =
          new NodeId(UShort.MIN, uint(18128));

  public static final NodeId
      ReaderGroupType_DataSetReaderName_Placeholder_Diagnostics_Counters_StatePausedByParent_Classification =
          new NodeId(UShort.MIN, uint(18129));

  public static final NodeId
      ReaderGroupType_DataSetReaderName_Placeholder_Diagnostics_Counters_StatePausedByParent_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(18130));

  public static final NodeId
      ReaderGroupType_DataSetReaderName_Placeholder_Diagnostics_Counters_StatePausedByParent_TimeFirstChange =
          new NodeId(UShort.MIN, uint(18131));

  public static final NodeId
      ReaderGroupType_DataSetReaderName_Placeholder_Diagnostics_Counters_StateDisabledByMethod =
          new NodeId(UShort.MIN, uint(18132));

  public static final NodeId
      ReaderGroupType_DataSetReaderName_Placeholder_Diagnostics_Counters_StateDisabledByMethod_Active =
          new NodeId(UShort.MIN, uint(18133));

  public static final NodeId
      ReaderGroupType_DataSetReaderName_Placeholder_Diagnostics_Counters_StateDisabledByMethod_Classification =
          new NodeId(UShort.MIN, uint(18134));

  public static final NodeId
      ReaderGroupType_DataSetReaderName_Placeholder_Diagnostics_Counters_StateDisabledByMethod_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(18135));

  public static final NodeId
      ReaderGroupType_DataSetReaderName_Placeholder_Diagnostics_Counters_StateDisabledByMethod_TimeFirstChange =
          new NodeId(UShort.MIN, uint(18136));

  public static final NodeId ReaderGroupType_DataSetReaderName_Placeholder_Diagnostics_LiveValues =
      new NodeId(UShort.MIN, uint(18137));

  public static final NodeId
      ReaderGroupType_DataSetReaderName_Placeholder_Diagnostics_Counters_FailedDataSetMessages =
          new NodeId(UShort.MIN, uint(18138));

  public static final NodeId
      ReaderGroupType_DataSetReaderName_Placeholder_Diagnostics_Counters_FailedDataSetMessages_Active =
          new NodeId(UShort.MIN, uint(18139));

  public static final NodeId
      ReaderGroupType_DataSetReaderName_Placeholder_Diagnostics_Counters_FailedDataSetMessages_Classification =
          new NodeId(UShort.MIN, uint(18140));

  public static final NodeId
      ReaderGroupType_DataSetReaderName_Placeholder_Diagnostics_Counters_FailedDataSetMessages_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(18141));

  public static final NodeId
      ReaderGroupType_DataSetReaderName_Placeholder_Diagnostics_Counters_FailedDataSetMessages_TimeFirstChange =
          new NodeId(UShort.MIN, uint(18142));

  public static final NodeId
      ReaderGroupType_DataSetReaderName_Placeholder_Diagnostics_Counters_DecryptionErrors =
          new NodeId(UShort.MIN, uint(18143));

  public static final NodeId
      ReaderGroupType_DataSetReaderName_Placeholder_Diagnostics_Counters_DecryptionErrors_Active =
          new NodeId(UShort.MIN, uint(18144));

  public static final NodeId
      ReaderGroupType_DataSetReaderName_Placeholder_Diagnostics_Counters_DecryptionErrors_Classification =
          new NodeId(UShort.MIN, uint(18145));

  public static final NodeId
      ReaderGroupType_DataSetReaderName_Placeholder_Diagnostics_Counters_DecryptionErrors_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(18146));

  public static final NodeId
      ReaderGroupType_DataSetReaderName_Placeholder_Diagnostics_Counters_DecryptionErrors_TimeFirstChange =
          new NodeId(UShort.MIN, uint(18147));

  public static final NodeId
      ReaderGroupType_DataSetReaderName_Placeholder_Diagnostics_LiveValues_MessageSequenceNumber =
          new NodeId(UShort.MIN, uint(18148));

  public static final NodeId
      ReaderGroupType_DataSetReaderName_Placeholder_Diagnostics_LiveValues_MessageSequenceNumber_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(18149));

  public static final NodeId
      ReaderGroupType_DataSetReaderName_Placeholder_Diagnostics_LiveValues_StatusCode =
          new NodeId(UShort.MIN, uint(18150));

  public static final NodeId
      ReaderGroupType_DataSetReaderName_Placeholder_Diagnostics_LiveValues_StatusCode_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(18151));

  public static final NodeId
      ReaderGroupType_DataSetReaderName_Placeholder_Diagnostics_LiveValues_MajorVersion =
          new NodeId(UShort.MIN, uint(18152));

  public static final NodeId
      ReaderGroupType_DataSetReaderName_Placeholder_Diagnostics_LiveValues_MajorVersion_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(18153));

  public static final NodeId
      ReaderGroupType_DataSetReaderName_Placeholder_Diagnostics_LiveValues_MinorVersion =
          new NodeId(UShort.MIN, uint(18154));

  public static final NodeId KeyCredentialConfiguration = new NodeId(UShort.MIN, uint(18155));

  public static final NodeId KeyCredentialConfiguration_ServiceName_Placeholder =
      new NodeId(UShort.MIN, uint(18156));

  public static final NodeId KeyCredentialConfiguration_ServiceName_Placeholder_ResourceUri =
      new NodeId(UShort.MIN, uint(18157));

  public static final NodeId
      ReaderGroupType_DataSetReaderName_Placeholder_Diagnostics_LiveValues_MinorVersion_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(18158));

  public static final NodeId KeyCredentialConfiguration_ServiceName_Placeholder_EndpointUrls =
      new NodeId(UShort.MIN, uint(18159));

  public static final NodeId KeyCredentialConfiguration_ServiceName_Placeholder_ServiceStatus =
      new NodeId(UShort.MIN, uint(18160));

  public static final NodeId KeyCredentialConfiguration_ServiceName_Placeholder_UpdateCredential =
      new NodeId(UShort.MIN, uint(18161));

  public static final NodeId
      KeyCredentialConfiguration_ServiceName_Placeholder_UpdateCredential_InputArguments =
          new NodeId(UShort.MIN, uint(18162));

  public static final NodeId KeyCredentialConfiguration_ServiceName_Placeholder_DeleteCredential =
      new NodeId(UShort.MIN, uint(18163));

  public static final NodeId KeyCredentialConfiguration_ServiceName_Placeholder_ProfileUri =
      new NodeId(UShort.MIN, uint(18164));

  public static final NodeId KeyCredentialConfigurationType_ProfileUri =
      new NodeId(UShort.MIN, uint(18165));

  public static final NodeId OpcUa_XmlSchema_DataTypeDefinition =
      new NodeId(UShort.MIN, uint(18166));

  public static final NodeId OpcUa_XmlSchema_DataTypeDefinition_DataTypeVersion =
      new NodeId(UShort.MIN, uint(18167));

  public static final NodeId OpcUa_XmlSchema_DataTypeDefinition_DictionaryFragment =
      new NodeId(UShort.MIN, uint(18168));

  public static final NodeId OpcUa_XmlSchema_StructureField = new NodeId(UShort.MIN, uint(18169));

  public static final NodeId OpcUa_XmlSchema_StructureField_DataTypeVersion =
      new NodeId(UShort.MIN, uint(18170));

  public static final NodeId OpcUa_XmlSchema_StructureField_DictionaryFragment =
      new NodeId(UShort.MIN, uint(18171));

  public static final NodeId OpcUa_XmlSchema_StructureDefinition =
      new NodeId(UShort.MIN, uint(18172));

  public static final NodeId OpcUa_XmlSchema_StructureDefinition_DataTypeVersion =
      new NodeId(UShort.MIN, uint(18173));

  public static final NodeId OpcUa_XmlSchema_StructureDefinition_DictionaryFragment =
      new NodeId(UShort.MIN, uint(18174));

  public static final NodeId OpcUa_XmlSchema_EnumDefinition = new NodeId(UShort.MIN, uint(18175));

  public static final NodeId OpcUa_XmlSchema_EnumDefinition_DataTypeVersion =
      new NodeId(UShort.MIN, uint(18176));

  public static final NodeId OpcUa_XmlSchema_EnumDefinition_DictionaryFragment =
      new NodeId(UShort.MIN, uint(18177));

  public static final NodeId OpcUa_BinarySchema_DataTypeDefinition =
      new NodeId(UShort.MIN, uint(18178));

  public static final NodeId OpcUa_BinarySchema_DataTypeDefinition_DataTypeVersion =
      new NodeId(UShort.MIN, uint(18179));

  public static final NodeId OpcUa_BinarySchema_DataTypeDefinition_DictionaryFragment =
      new NodeId(UShort.MIN, uint(18180));

  public static final NodeId OpcUa_BinarySchema_StructureField =
      new NodeId(UShort.MIN, uint(18181));

  public static final NodeId OpcUa_BinarySchema_StructureField_DataTypeVersion =
      new NodeId(UShort.MIN, uint(18182));

  public static final NodeId OpcUa_BinarySchema_StructureField_DictionaryFragment =
      new NodeId(UShort.MIN, uint(18183));

  public static final NodeId OpcUa_BinarySchema_StructureDefinition =
      new NodeId(UShort.MIN, uint(18184));

  public static final NodeId OpcUa_BinarySchema_StructureDefinition_DataTypeVersion =
      new NodeId(UShort.MIN, uint(18185));

  public static final NodeId OpcUa_BinarySchema_StructureDefinition_DictionaryFragment =
      new NodeId(UShort.MIN, uint(18186));

  public static final NodeId OpcUa_BinarySchema_EnumDefinition =
      new NodeId(UShort.MIN, uint(18187));

  public static final NodeId OpcUa_BinarySchema_EnumDefinition_DataTypeVersion =
      new NodeId(UShort.MIN, uint(18188));

  public static final NodeId OpcUa_BinarySchema_EnumDefinition_DictionaryFragment =
      new NodeId(UShort.MIN, uint(18189));

  public static final NodeId AlarmConditionType_LatchedState = new NodeId(UShort.MIN, uint(18190));

  public static final NodeId AlarmConditionType_LatchedState_Id =
      new NodeId(UShort.MIN, uint(18191));

  public static final NodeId AlarmConditionType_LatchedState_Name =
      new NodeId(UShort.MIN, uint(18192));

  public static final NodeId AlarmConditionType_LatchedState_Number =
      new NodeId(UShort.MIN, uint(18193));

  public static final NodeId AlarmConditionType_LatchedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(18194));

  public static final NodeId AlarmConditionType_LatchedState_TransitionTime =
      new NodeId(UShort.MIN, uint(18195));

  public static final NodeId AlarmConditionType_LatchedState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(18196));

  public static final NodeId AlarmConditionType_LatchedState_TrueState =
      new NodeId(UShort.MIN, uint(18197));

  public static final NodeId AlarmConditionType_LatchedState_FalseState =
      new NodeId(UShort.MIN, uint(18198));

  public static final NodeId AlarmConditionType_Reset = new NodeId(UShort.MIN, uint(18199));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_AudibleSound_ListId =
      new NodeId(UShort.MIN, uint(18200));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_AudibleSound_AgencyId =
          new NodeId(UShort.MIN, uint(18201));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_AudibleSound_VersionId =
          new NodeId(UShort.MIN, uint(18202));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_LatchedState =
      new NodeId(UShort.MIN, uint(18203));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_LatchedState_Id =
      new NodeId(UShort.MIN, uint(18204));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_LatchedState_Name =
      new NodeId(UShort.MIN, uint(18205));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_LatchedState_Number =
      new NodeId(UShort.MIN, uint(18206));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_LatchedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(18207));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_LatchedState_TransitionTime =
          new NodeId(UShort.MIN, uint(18208));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_LatchedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(18209));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_LatchedState_TrueState =
          new NodeId(UShort.MIN, uint(18210));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_LatchedState_FalseState =
          new NodeId(UShort.MIN, uint(18211));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_Reset =
      new NodeId(UShort.MIN, uint(18212));

  public static final NodeId LimitAlarmType_LatchedState = new NodeId(UShort.MIN, uint(18213));

  public static final NodeId LimitAlarmType_LatchedState_Id = new NodeId(UShort.MIN, uint(18214));

  public static final NodeId LimitAlarmType_LatchedState_Name = new NodeId(UShort.MIN, uint(18215));

  public static final NodeId LimitAlarmType_LatchedState_Number =
      new NodeId(UShort.MIN, uint(18216));

  public static final NodeId LimitAlarmType_LatchedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(18217));

  public static final NodeId LimitAlarmType_LatchedState_TransitionTime =
      new NodeId(UShort.MIN, uint(18218));

  public static final NodeId LimitAlarmType_LatchedState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(18219));

  public static final NodeId LimitAlarmType_LatchedState_TrueState =
      new NodeId(UShort.MIN, uint(18220));

  public static final NodeId LimitAlarmType_LatchedState_FalseState =
      new NodeId(UShort.MIN, uint(18221));

  public static final NodeId LimitAlarmType_Reset = new NodeId(UShort.MIN, uint(18222));

  public static final NodeId ExclusiveLimitAlarmType_LatchedState =
      new NodeId(UShort.MIN, uint(18223));

  public static final NodeId ExclusiveLimitAlarmType_LatchedState_Id =
      new NodeId(UShort.MIN, uint(18224));

  public static final NodeId ExclusiveLimitAlarmType_LatchedState_Name =
      new NodeId(UShort.MIN, uint(18225));

  public static final NodeId ExclusiveLimitAlarmType_LatchedState_Number =
      new NodeId(UShort.MIN, uint(18226));

  public static final NodeId ExclusiveLimitAlarmType_LatchedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(18227));

  public static final NodeId ExclusiveLimitAlarmType_LatchedState_TransitionTime =
      new NodeId(UShort.MIN, uint(18228));

  public static final NodeId ExclusiveLimitAlarmType_LatchedState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(18229));

  public static final NodeId ExclusiveLimitAlarmType_LatchedState_TrueState =
      new NodeId(UShort.MIN, uint(18230));

  public static final NodeId ExclusiveLimitAlarmType_LatchedState_FalseState =
      new NodeId(UShort.MIN, uint(18231));

  public static final NodeId ExclusiveLimitAlarmType_Reset = new NodeId(UShort.MIN, uint(18232));

  public static final NodeId NonExclusiveLimitAlarmType_LatchedState =
      new NodeId(UShort.MIN, uint(18233));

  public static final NodeId NonExclusiveLimitAlarmType_LatchedState_Id =
      new NodeId(UShort.MIN, uint(18234));

  public static final NodeId NonExclusiveLimitAlarmType_LatchedState_Name =
      new NodeId(UShort.MIN, uint(18235));

  public static final NodeId NonExclusiveLimitAlarmType_LatchedState_Number =
      new NodeId(UShort.MIN, uint(18236));

  public static final NodeId NonExclusiveLimitAlarmType_LatchedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(18237));

  public static final NodeId NonExclusiveLimitAlarmType_LatchedState_TransitionTime =
      new NodeId(UShort.MIN, uint(18238));

  public static final NodeId NonExclusiveLimitAlarmType_LatchedState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(18239));

  public static final NodeId NonExclusiveLimitAlarmType_LatchedState_TrueState =
      new NodeId(UShort.MIN, uint(18240));

  public static final NodeId NonExclusiveLimitAlarmType_LatchedState_FalseState =
      new NodeId(UShort.MIN, uint(18241));

  public static final NodeId NonExclusiveLimitAlarmType_Reset = new NodeId(UShort.MIN, uint(18242));

  public static final NodeId NonExclusiveLevelAlarmType_AudibleSound_ListId =
      new NodeId(UShort.MIN, uint(18243));

  public static final NodeId NonExclusiveLevelAlarmType_AudibleSound_AgencyId =
      new NodeId(UShort.MIN, uint(18244));

  public static final NodeId NonExclusiveLevelAlarmType_AudibleSound_VersionId =
      new NodeId(UShort.MIN, uint(18245));

  public static final NodeId NonExclusiveLevelAlarmType_LatchedState =
      new NodeId(UShort.MIN, uint(18246));

  public static final NodeId NonExclusiveLevelAlarmType_LatchedState_Id =
      new NodeId(UShort.MIN, uint(18247));

  public static final NodeId NonExclusiveLevelAlarmType_LatchedState_Name =
      new NodeId(UShort.MIN, uint(18248));

  public static final NodeId NonExclusiveLevelAlarmType_LatchedState_Number =
      new NodeId(UShort.MIN, uint(18249));

  public static final NodeId NonExclusiveLevelAlarmType_LatchedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(18250));

  public static final NodeId NonExclusiveLevelAlarmType_LatchedState_TransitionTime =
      new NodeId(UShort.MIN, uint(18251));

  public static final NodeId NonExclusiveLevelAlarmType_LatchedState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(18252));

  public static final NodeId NonExclusiveLevelAlarmType_LatchedState_TrueState =
      new NodeId(UShort.MIN, uint(18253));

  public static final NodeId NonExclusiveLevelAlarmType_LatchedState_FalseState =
      new NodeId(UShort.MIN, uint(18254));

  public static final NodeId NonExclusiveLevelAlarmType_Unsuppress =
      new NodeId(UShort.MIN, uint(18255));

  public static final NodeId NonExclusiveLevelAlarmType_Reset = new NodeId(UShort.MIN, uint(18256));

  public static final NodeId ExclusiveLevelAlarmType_LatchedState =
      new NodeId(UShort.MIN, uint(18257));

  public static final NodeId ExclusiveLevelAlarmType_LatchedState_Id =
      new NodeId(UShort.MIN, uint(18258));

  public static final NodeId ExclusiveLevelAlarmType_LatchedState_Name =
      new NodeId(UShort.MIN, uint(18259));

  public static final NodeId ExclusiveLevelAlarmType_LatchedState_Number =
      new NodeId(UShort.MIN, uint(18260));

  public static final NodeId ExclusiveLevelAlarmType_LatchedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(18261));

  public static final NodeId ExclusiveLevelAlarmType_LatchedState_TransitionTime =
      new NodeId(UShort.MIN, uint(18262));

  public static final NodeId ExclusiveLevelAlarmType_LatchedState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(18263));

  public static final NodeId ExclusiveLevelAlarmType_LatchedState_TrueState =
      new NodeId(UShort.MIN, uint(18264));

  public static final NodeId ExclusiveLevelAlarmType_LatchedState_FalseState =
      new NodeId(UShort.MIN, uint(18265));

  public static final NodeId ExclusiveLevelAlarmType_Reset = new NodeId(UShort.MIN, uint(18266));

  public static final NodeId NonExclusiveDeviationAlarmType_LatchedState =
      new NodeId(UShort.MIN, uint(18267));

  public static final NodeId NonExclusiveDeviationAlarmType_LatchedState_Id =
      new NodeId(UShort.MIN, uint(18268));

  public static final NodeId NonExclusiveDeviationAlarmType_LatchedState_Name =
      new NodeId(UShort.MIN, uint(18269));

  public static final NodeId NonExclusiveDeviationAlarmType_LatchedState_Number =
      new NodeId(UShort.MIN, uint(18270));

  public static final NodeId NonExclusiveDeviationAlarmType_LatchedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(18271));

  public static final NodeId NonExclusiveDeviationAlarmType_LatchedState_TransitionTime =
      new NodeId(UShort.MIN, uint(18272));

  public static final NodeId NonExclusiveDeviationAlarmType_LatchedState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(18273));

  public static final NodeId NonExclusiveDeviationAlarmType_LatchedState_TrueState =
      new NodeId(UShort.MIN, uint(18274));

  public static final NodeId NonExclusiveDeviationAlarmType_LatchedState_FalseState =
      new NodeId(UShort.MIN, uint(18275));

  public static final NodeId NonExclusiveDeviationAlarmType_Reset =
      new NodeId(UShort.MIN, uint(18276));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_LatchedState =
      new NodeId(UShort.MIN, uint(18277));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_LatchedState_Id =
      new NodeId(UShort.MIN, uint(18278));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_LatchedState_Name =
      new NodeId(UShort.MIN, uint(18279));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_LatchedState_Number =
      new NodeId(UShort.MIN, uint(18280));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_LatchedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(18281));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_LatchedState_TransitionTime =
      new NodeId(UShort.MIN, uint(18282));

  public static final NodeId
      NonExclusiveRateOfChangeAlarmType_LatchedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(18283));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_LatchedState_TrueState =
      new NodeId(UShort.MIN, uint(18284));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_LatchedState_FalseState =
      new NodeId(UShort.MIN, uint(18285));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_Reset =
      new NodeId(UShort.MIN, uint(18286));

  public static final NodeId ExclusiveDeviationAlarmType_LatchedState =
      new NodeId(UShort.MIN, uint(18287));

  public static final NodeId ExclusiveDeviationAlarmType_LatchedState_Id =
      new NodeId(UShort.MIN, uint(18288));

  public static final NodeId ExclusiveDeviationAlarmType_LatchedState_Name =
      new NodeId(UShort.MIN, uint(18289));

  public static final NodeId ExclusiveDeviationAlarmType_LatchedState_Number =
      new NodeId(UShort.MIN, uint(18290));

  public static final NodeId ExclusiveDeviationAlarmType_LatchedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(18291));

  public static final NodeId ExclusiveDeviationAlarmType_LatchedState_TransitionTime =
      new NodeId(UShort.MIN, uint(18292));

  public static final NodeId ExclusiveDeviationAlarmType_LatchedState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(18293));

  public static final NodeId ExclusiveDeviationAlarmType_LatchedState_TrueState =
      new NodeId(UShort.MIN, uint(18294));

  public static final NodeId ExclusiveDeviationAlarmType_LatchedState_FalseState =
      new NodeId(UShort.MIN, uint(18295));

  public static final NodeId ExclusiveDeviationAlarmType_Reset =
      new NodeId(UShort.MIN, uint(18296));

  public static final NodeId ExclusiveRateOfChangeAlarmType_LatchedState =
      new NodeId(UShort.MIN, uint(18297));

  public static final NodeId ExclusiveRateOfChangeAlarmType_LatchedState_Id =
      new NodeId(UShort.MIN, uint(18298));

  public static final NodeId ExclusiveRateOfChangeAlarmType_LatchedState_Name =
      new NodeId(UShort.MIN, uint(18299));

  public static final NodeId ExclusiveRateOfChangeAlarmType_LatchedState_Number =
      new NodeId(UShort.MIN, uint(18300));

  public static final NodeId ExclusiveRateOfChangeAlarmType_LatchedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(18301));

  public static final NodeId ExclusiveRateOfChangeAlarmType_LatchedState_TransitionTime =
      new NodeId(UShort.MIN, uint(18302));

  public static final NodeId ExclusiveRateOfChangeAlarmType_LatchedState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(18303));

  public static final NodeId ExclusiveRateOfChangeAlarmType_LatchedState_TrueState =
      new NodeId(UShort.MIN, uint(18304));

  public static final NodeId ExclusiveRateOfChangeAlarmType_LatchedState_FalseState =
      new NodeId(UShort.MIN, uint(18305));

  public static final NodeId ExclusiveRateOfChangeAlarmType_Reset =
      new NodeId(UShort.MIN, uint(18306));

  public static final NodeId DiscreteAlarmType_LatchedState = new NodeId(UShort.MIN, uint(18307));

  public static final NodeId DiscreteAlarmType_LatchedState_Id =
      new NodeId(UShort.MIN, uint(18308));

  public static final NodeId DiscreteAlarmType_LatchedState_Name =
      new NodeId(UShort.MIN, uint(18309));

  public static final NodeId DiscreteAlarmType_LatchedState_Number =
      new NodeId(UShort.MIN, uint(18310));

  public static final NodeId DiscreteAlarmType_LatchedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(18311));

  public static final NodeId DiscreteAlarmType_LatchedState_TransitionTime =
      new NodeId(UShort.MIN, uint(18312));

  public static final NodeId DiscreteAlarmType_LatchedState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(18313));

  public static final NodeId DiscreteAlarmType_LatchedState_TrueState =
      new NodeId(UShort.MIN, uint(18314));

  public static final NodeId DiscreteAlarmType_LatchedState_FalseState =
      new NodeId(UShort.MIN, uint(18315));

  public static final NodeId DiscreteAlarmType_Reset = new NodeId(UShort.MIN, uint(18316));

  public static final NodeId OffNormalAlarmType_LatchedState = new NodeId(UShort.MIN, uint(18317));

  public static final NodeId OffNormalAlarmType_LatchedState_Id =
      new NodeId(UShort.MIN, uint(18318));

  public static final NodeId OffNormalAlarmType_LatchedState_Name =
      new NodeId(UShort.MIN, uint(18319));

  public static final NodeId OffNormalAlarmType_LatchedState_Number =
      new NodeId(UShort.MIN, uint(18320));

  public static final NodeId OffNormalAlarmType_LatchedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(18321));

  public static final NodeId OffNormalAlarmType_LatchedState_TransitionTime =
      new NodeId(UShort.MIN, uint(18322));

  public static final NodeId OffNormalAlarmType_LatchedState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(18323));

  public static final NodeId OffNormalAlarmType_LatchedState_TrueState =
      new NodeId(UShort.MIN, uint(18324));

  public static final NodeId OffNormalAlarmType_LatchedState_FalseState =
      new NodeId(UShort.MIN, uint(18325));

  public static final NodeId OffNormalAlarmType_Reset = new NodeId(UShort.MIN, uint(18326));

  public static final NodeId SystemOffNormalAlarmType_LatchedState =
      new NodeId(UShort.MIN, uint(18327));

  public static final NodeId SystemOffNormalAlarmType_LatchedState_Id =
      new NodeId(UShort.MIN, uint(18328));

  public static final NodeId SystemOffNormalAlarmType_LatchedState_Name =
      new NodeId(UShort.MIN, uint(18329));

  public static final NodeId SystemOffNormalAlarmType_LatchedState_Number =
      new NodeId(UShort.MIN, uint(18330));

  public static final NodeId SystemOffNormalAlarmType_LatchedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(18331));

  public static final NodeId SystemOffNormalAlarmType_LatchedState_TransitionTime =
      new NodeId(UShort.MIN, uint(18332));

  public static final NodeId SystemOffNormalAlarmType_LatchedState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(18333));

  public static final NodeId SystemOffNormalAlarmType_LatchedState_TrueState =
      new NodeId(UShort.MIN, uint(18334));

  public static final NodeId SystemOffNormalAlarmType_LatchedState_FalseState =
      new NodeId(UShort.MIN, uint(18335));

  public static final NodeId SystemOffNormalAlarmType_Reset = new NodeId(UShort.MIN, uint(18336));

  public static final NodeId TripAlarmType_LatchedState = new NodeId(UShort.MIN, uint(18337));

  public static final NodeId TripAlarmType_LatchedState_Id = new NodeId(UShort.MIN, uint(18338));

  public static final NodeId TripAlarmType_LatchedState_Name = new NodeId(UShort.MIN, uint(18339));

  public static final NodeId TripAlarmType_LatchedState_Number =
      new NodeId(UShort.MIN, uint(18340));

  public static final NodeId TripAlarmType_LatchedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(18341));

  public static final NodeId TripAlarmType_LatchedState_TransitionTime =
      new NodeId(UShort.MIN, uint(18342));

  public static final NodeId TripAlarmType_LatchedState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(18343));

  public static final NodeId TripAlarmType_LatchedState_TrueState =
      new NodeId(UShort.MIN, uint(18344));

  public static final NodeId TripAlarmType_LatchedState_FalseState =
      new NodeId(UShort.MIN, uint(18345));

  public static final NodeId TripAlarmType_Reset = new NodeId(UShort.MIN, uint(18346));

  public static final NodeId InstrumentDiagnosticAlarmType = new NodeId(UShort.MIN, uint(18347));

  public static final NodeId InstrumentDiagnosticAlarmType_EventId =
      new NodeId(UShort.MIN, uint(18348));

  public static final NodeId InstrumentDiagnosticAlarmType_EventType =
      new NodeId(UShort.MIN, uint(18349));

  public static final NodeId InstrumentDiagnosticAlarmType_SourceNode =
      new NodeId(UShort.MIN, uint(18350));

  public static final NodeId InstrumentDiagnosticAlarmType_SourceName =
      new NodeId(UShort.MIN, uint(18351));

  public static final NodeId InstrumentDiagnosticAlarmType_Time =
      new NodeId(UShort.MIN, uint(18352));

  public static final NodeId InstrumentDiagnosticAlarmType_ReceiveTime =
      new NodeId(UShort.MIN, uint(18353));

  public static final NodeId InstrumentDiagnosticAlarmType_LocalTime =
      new NodeId(UShort.MIN, uint(18354));

  public static final NodeId InstrumentDiagnosticAlarmType_Message =
      new NodeId(UShort.MIN, uint(18355));

  public static final NodeId InstrumentDiagnosticAlarmType_Severity =
      new NodeId(UShort.MIN, uint(18356));

  public static final NodeId InstrumentDiagnosticAlarmType_ConditionClassId =
      new NodeId(UShort.MIN, uint(18357));

  public static final NodeId InstrumentDiagnosticAlarmType_ConditionClassName =
      new NodeId(UShort.MIN, uint(18358));

  public static final NodeId InstrumentDiagnosticAlarmType_ConditionSubClassId =
      new NodeId(UShort.MIN, uint(18359));

  public static final NodeId InstrumentDiagnosticAlarmType_ConditionSubClassName =
      new NodeId(UShort.MIN, uint(18360));

  public static final NodeId InstrumentDiagnosticAlarmType_ConditionName =
      new NodeId(UShort.MIN, uint(18361));

  public static final NodeId InstrumentDiagnosticAlarmType_BranchId =
      new NodeId(UShort.MIN, uint(18362));

  public static final NodeId InstrumentDiagnosticAlarmType_Retain =
      new NodeId(UShort.MIN, uint(18363));

  public static final NodeId InstrumentDiagnosticAlarmType_EnabledState =
      new NodeId(UShort.MIN, uint(18364));

  public static final NodeId InstrumentDiagnosticAlarmType_EnabledState_Id =
      new NodeId(UShort.MIN, uint(18365));

  public static final NodeId InstrumentDiagnosticAlarmType_EnabledState_Name =
      new NodeId(UShort.MIN, uint(18366));

  public static final NodeId InstrumentDiagnosticAlarmType_EnabledState_Number =
      new NodeId(UShort.MIN, uint(18367));

  public static final NodeId InstrumentDiagnosticAlarmType_EnabledState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(18368));

  public static final NodeId InstrumentDiagnosticAlarmType_EnabledState_TransitionTime =
      new NodeId(UShort.MIN, uint(18369));

  public static final NodeId InstrumentDiagnosticAlarmType_EnabledState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(18370));

  public static final NodeId InstrumentDiagnosticAlarmType_EnabledState_TrueState =
      new NodeId(UShort.MIN, uint(18371));

  public static final NodeId InstrumentDiagnosticAlarmType_EnabledState_FalseState =
      new NodeId(UShort.MIN, uint(18372));

  public static final NodeId InstrumentDiagnosticAlarmType_Quality =
      new NodeId(UShort.MIN, uint(18373));

  public static final NodeId InstrumentDiagnosticAlarmType_Quality_SourceTimestamp =
      new NodeId(UShort.MIN, uint(18374));

  public static final NodeId InstrumentDiagnosticAlarmType_LastSeverity =
      new NodeId(UShort.MIN, uint(18375));

  public static final NodeId InstrumentDiagnosticAlarmType_LastSeverity_SourceTimestamp =
      new NodeId(UShort.MIN, uint(18376));

  public static final NodeId InstrumentDiagnosticAlarmType_Comment =
      new NodeId(UShort.MIN, uint(18377));

  public static final NodeId InstrumentDiagnosticAlarmType_Comment_SourceTimestamp =
      new NodeId(UShort.MIN, uint(18378));

  public static final NodeId InstrumentDiagnosticAlarmType_ClientUserId =
      new NodeId(UShort.MIN, uint(18379));

  public static final NodeId InstrumentDiagnosticAlarmType_Disable =
      new NodeId(UShort.MIN, uint(18380));

  public static final NodeId InstrumentDiagnosticAlarmType_Enable =
      new NodeId(UShort.MIN, uint(18381));

  public static final NodeId InstrumentDiagnosticAlarmType_AddComment =
      new NodeId(UShort.MIN, uint(18382));

  public static final NodeId InstrumentDiagnosticAlarmType_AddComment_InputArguments =
      new NodeId(UShort.MIN, uint(18383));

  public static final NodeId InstrumentDiagnosticAlarmType_ConditionRefresh =
      new NodeId(UShort.MIN, uint(18384));

  public static final NodeId InstrumentDiagnosticAlarmType_ConditionRefresh_InputArguments =
      new NodeId(UShort.MIN, uint(18385));

  public static final NodeId InstrumentDiagnosticAlarmType_ConditionRefresh2 =
      new NodeId(UShort.MIN, uint(18386));

  public static final NodeId InstrumentDiagnosticAlarmType_ConditionRefresh2_InputArguments =
      new NodeId(UShort.MIN, uint(18387));

  public static final NodeId InstrumentDiagnosticAlarmType_AckedState =
      new NodeId(UShort.MIN, uint(18388));

  public static final NodeId InstrumentDiagnosticAlarmType_AckedState_Id =
      new NodeId(UShort.MIN, uint(18389));

  public static final NodeId InstrumentDiagnosticAlarmType_AckedState_Name =
      new NodeId(UShort.MIN, uint(18390));

  public static final NodeId InstrumentDiagnosticAlarmType_AckedState_Number =
      new NodeId(UShort.MIN, uint(18391));

  public static final NodeId InstrumentDiagnosticAlarmType_AckedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(18392));

  public static final NodeId InstrumentDiagnosticAlarmType_AckedState_TransitionTime =
      new NodeId(UShort.MIN, uint(18393));

  public static final NodeId InstrumentDiagnosticAlarmType_AckedState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(18394));

  public static final NodeId InstrumentDiagnosticAlarmType_AckedState_TrueState =
      new NodeId(UShort.MIN, uint(18395));

  public static final NodeId InstrumentDiagnosticAlarmType_AckedState_FalseState =
      new NodeId(UShort.MIN, uint(18396));

  public static final NodeId InstrumentDiagnosticAlarmType_ConfirmedState =
      new NodeId(UShort.MIN, uint(18397));

  public static final NodeId InstrumentDiagnosticAlarmType_ConfirmedState_Id =
      new NodeId(UShort.MIN, uint(18398));

  public static final NodeId InstrumentDiagnosticAlarmType_ConfirmedState_Name =
      new NodeId(UShort.MIN, uint(18399));

  public static final NodeId InstrumentDiagnosticAlarmType_ConfirmedState_Number =
      new NodeId(UShort.MIN, uint(18400));

  public static final NodeId InstrumentDiagnosticAlarmType_ConfirmedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(18401));

  public static final NodeId InstrumentDiagnosticAlarmType_ConfirmedState_TransitionTime =
      new NodeId(UShort.MIN, uint(18402));

  public static final NodeId InstrumentDiagnosticAlarmType_ConfirmedState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(18403));

  public static final NodeId InstrumentDiagnosticAlarmType_ConfirmedState_TrueState =
      new NodeId(UShort.MIN, uint(18404));

  public static final NodeId InstrumentDiagnosticAlarmType_ConfirmedState_FalseState =
      new NodeId(UShort.MIN, uint(18405));

  public static final NodeId InstrumentDiagnosticAlarmType_Acknowledge =
      new NodeId(UShort.MIN, uint(18406));

  public static final NodeId InstrumentDiagnosticAlarmType_Acknowledge_InputArguments =
      new NodeId(UShort.MIN, uint(18407));

  public static final NodeId InstrumentDiagnosticAlarmType_Confirm =
      new NodeId(UShort.MIN, uint(18408));

  public static final NodeId InstrumentDiagnosticAlarmType_Confirm_InputArguments =
      new NodeId(UShort.MIN, uint(18409));

  public static final NodeId InstrumentDiagnosticAlarmType_ActiveState =
      new NodeId(UShort.MIN, uint(18410));

  public static final NodeId InstrumentDiagnosticAlarmType_ActiveState_Id =
      new NodeId(UShort.MIN, uint(18411));

  public static final NodeId InstrumentDiagnosticAlarmType_ActiveState_Name =
      new NodeId(UShort.MIN, uint(18412));

  public static final NodeId InstrumentDiagnosticAlarmType_ActiveState_Number =
      new NodeId(UShort.MIN, uint(18413));

  public static final NodeId InstrumentDiagnosticAlarmType_ActiveState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(18414));

  public static final NodeId InstrumentDiagnosticAlarmType_ActiveState_TransitionTime =
      new NodeId(UShort.MIN, uint(18415));

  public static final NodeId InstrumentDiagnosticAlarmType_ActiveState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(18416));

  public static final NodeId InstrumentDiagnosticAlarmType_ActiveState_TrueState =
      new NodeId(UShort.MIN, uint(18417));

  public static final NodeId InstrumentDiagnosticAlarmType_ActiveState_FalseState =
      new NodeId(UShort.MIN, uint(18418));

  public static final NodeId InstrumentDiagnosticAlarmType_InputNode =
      new NodeId(UShort.MIN, uint(18419));

  public static final NodeId InstrumentDiagnosticAlarmType_SuppressedState =
      new NodeId(UShort.MIN, uint(18420));

  public static final NodeId InstrumentDiagnosticAlarmType_SuppressedState_Id =
      new NodeId(UShort.MIN, uint(18421));

  public static final NodeId InstrumentDiagnosticAlarmType_SuppressedState_Name =
      new NodeId(UShort.MIN, uint(18422));

  public static final NodeId InstrumentDiagnosticAlarmType_SuppressedState_Number =
      new NodeId(UShort.MIN, uint(18423));

  public static final NodeId InstrumentDiagnosticAlarmType_SuppressedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(18424));

  public static final NodeId InstrumentDiagnosticAlarmType_SuppressedState_TransitionTime =
      new NodeId(UShort.MIN, uint(18425));

  public static final NodeId InstrumentDiagnosticAlarmType_SuppressedState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(18426));

  public static final NodeId InstrumentDiagnosticAlarmType_SuppressedState_TrueState =
      new NodeId(UShort.MIN, uint(18427));

  public static final NodeId InstrumentDiagnosticAlarmType_SuppressedState_FalseState =
      new NodeId(UShort.MIN, uint(18428));

  public static final NodeId InstrumentDiagnosticAlarmType_OutOfServiceState =
      new NodeId(UShort.MIN, uint(18429));

  public static final NodeId InstrumentDiagnosticAlarmType_OutOfServiceState_Id =
      new NodeId(UShort.MIN, uint(18430));

  public static final NodeId InstrumentDiagnosticAlarmType_OutOfServiceState_Name =
      new NodeId(UShort.MIN, uint(18431));

  public static final NodeId InstrumentDiagnosticAlarmType_OutOfServiceState_Number =
      new NodeId(UShort.MIN, uint(18432));

  public static final NodeId InstrumentDiagnosticAlarmType_OutOfServiceState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(18433));

  public static final NodeId InstrumentDiagnosticAlarmType_OutOfServiceState_TransitionTime =
      new NodeId(UShort.MIN, uint(18434));

  public static final NodeId
      InstrumentDiagnosticAlarmType_OutOfServiceState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(18435));

  public static final NodeId InstrumentDiagnosticAlarmType_OutOfServiceState_TrueState =
      new NodeId(UShort.MIN, uint(18436));

  public static final NodeId InstrumentDiagnosticAlarmType_OutOfServiceState_FalseState =
      new NodeId(UShort.MIN, uint(18437));

  public static final NodeId InstrumentDiagnosticAlarmType_ShelvingState =
      new NodeId(UShort.MIN, uint(18438));

  public static final NodeId InstrumentDiagnosticAlarmType_ShelvingState_CurrentState =
      new NodeId(UShort.MIN, uint(18439));

  public static final NodeId InstrumentDiagnosticAlarmType_ShelvingState_CurrentState_Id =
      new NodeId(UShort.MIN, uint(18440));

  public static final NodeId InstrumentDiagnosticAlarmType_ShelvingState_CurrentState_Name =
      new NodeId(UShort.MIN, uint(18441));

  public static final NodeId InstrumentDiagnosticAlarmType_ShelvingState_CurrentState_Number =
      new NodeId(UShort.MIN, uint(18442));

  public static final NodeId
      InstrumentDiagnosticAlarmType_ShelvingState_CurrentState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(18443));

  public static final NodeId InstrumentDiagnosticAlarmType_ShelvingState_LastTransition =
      new NodeId(UShort.MIN, uint(18444));

  public static final NodeId InstrumentDiagnosticAlarmType_ShelvingState_LastTransition_Id =
      new NodeId(UShort.MIN, uint(18445));

  public static final NodeId InstrumentDiagnosticAlarmType_ShelvingState_LastTransition_Name =
      new NodeId(UShort.MIN, uint(18446));

  public static final NodeId InstrumentDiagnosticAlarmType_ShelvingState_LastTransition_Number =
      new NodeId(UShort.MIN, uint(18447));

  public static final NodeId
      InstrumentDiagnosticAlarmType_ShelvingState_LastTransition_TransitionTime =
          new NodeId(UShort.MIN, uint(18448));

  public static final NodeId
      InstrumentDiagnosticAlarmType_ShelvingState_LastTransition_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(18449));

  public static final NodeId InstrumentDiagnosticAlarmType_ShelvingState_AvailableStates =
      new NodeId(UShort.MIN, uint(18450));

  public static final NodeId InstrumentDiagnosticAlarmType_ShelvingState_AvailableTransitions =
      new NodeId(UShort.MIN, uint(18451));

  public static final NodeId InstrumentDiagnosticAlarmType_ShelvingState_UnshelveTime =
      new NodeId(UShort.MIN, uint(18452));

  public static final NodeId InstrumentDiagnosticAlarmType_ShelvingState_TimedShelve =
      new NodeId(UShort.MIN, uint(18453));

  public static final NodeId
      InstrumentDiagnosticAlarmType_ShelvingState_TimedShelve_InputArguments =
          new NodeId(UShort.MIN, uint(18454));

  public static final NodeId InstrumentDiagnosticAlarmType_ShelvingState_Unshelve =
      new NodeId(UShort.MIN, uint(18455));

  public static final NodeId InstrumentDiagnosticAlarmType_ShelvingState_OneShotShelve =
      new NodeId(UShort.MIN, uint(18456));

  public static final NodeId InstrumentDiagnosticAlarmType_SuppressedOrShelved =
      new NodeId(UShort.MIN, uint(18457));

  public static final NodeId InstrumentDiagnosticAlarmType_MaxTimeShelved =
      new NodeId(UShort.MIN, uint(18458));

  public static final NodeId InstrumentDiagnosticAlarmType_AudibleEnabled =
      new NodeId(UShort.MIN, uint(18459));

  public static final NodeId InstrumentDiagnosticAlarmType_AudibleSound =
      new NodeId(UShort.MIN, uint(18460));

  public static final NodeId InstrumentDiagnosticAlarmType_AudibleSound_ListId =
      new NodeId(UShort.MIN, uint(18461));

  public static final NodeId InstrumentDiagnosticAlarmType_AudibleSound_AgencyId =
      new NodeId(UShort.MIN, uint(18462));

  public static final NodeId InstrumentDiagnosticAlarmType_AudibleSound_VersionId =
      new NodeId(UShort.MIN, uint(18463));

  public static final NodeId InstrumentDiagnosticAlarmType_SilenceState =
      new NodeId(UShort.MIN, uint(18464));

  public static final NodeId InstrumentDiagnosticAlarmType_SilenceState_Id =
      new NodeId(UShort.MIN, uint(18465));

  public static final NodeId InstrumentDiagnosticAlarmType_SilenceState_Name =
      new NodeId(UShort.MIN, uint(18466));

  public static final NodeId InstrumentDiagnosticAlarmType_SilenceState_Number =
      new NodeId(UShort.MIN, uint(18467));

  public static final NodeId InstrumentDiagnosticAlarmType_SilenceState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(18468));

  public static final NodeId InstrumentDiagnosticAlarmType_SilenceState_TransitionTime =
      new NodeId(UShort.MIN, uint(18469));

  public static final NodeId InstrumentDiagnosticAlarmType_SilenceState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(18470));

  public static final NodeId InstrumentDiagnosticAlarmType_SilenceState_TrueState =
      new NodeId(UShort.MIN, uint(18471));

  public static final NodeId InstrumentDiagnosticAlarmType_SilenceState_FalseState =
      new NodeId(UShort.MIN, uint(18472));

  public static final NodeId InstrumentDiagnosticAlarmType_OnDelay =
      new NodeId(UShort.MIN, uint(18473));

  public static final NodeId InstrumentDiagnosticAlarmType_OffDelay =
      new NodeId(UShort.MIN, uint(18474));

  public static final NodeId InstrumentDiagnosticAlarmType_FirstInGroupFlag =
      new NodeId(UShort.MIN, uint(18475));

  public static final NodeId InstrumentDiagnosticAlarmType_FirstInGroup =
      new NodeId(UShort.MIN, uint(18476));

  public static final NodeId InstrumentDiagnosticAlarmType_LatchedState =
      new NodeId(UShort.MIN, uint(18477));

  public static final NodeId InstrumentDiagnosticAlarmType_LatchedState_Id =
      new NodeId(UShort.MIN, uint(18478));

  public static final NodeId InstrumentDiagnosticAlarmType_LatchedState_Name =
      new NodeId(UShort.MIN, uint(18479));

  public static final NodeId InstrumentDiagnosticAlarmType_LatchedState_Number =
      new NodeId(UShort.MIN, uint(18480));

  public static final NodeId InstrumentDiagnosticAlarmType_LatchedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(18481));

  public static final NodeId InstrumentDiagnosticAlarmType_LatchedState_TransitionTime =
      new NodeId(UShort.MIN, uint(18482));

  public static final NodeId InstrumentDiagnosticAlarmType_LatchedState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(18483));

  public static final NodeId InstrumentDiagnosticAlarmType_LatchedState_TrueState =
      new NodeId(UShort.MIN, uint(18484));

  public static final NodeId InstrumentDiagnosticAlarmType_LatchedState_FalseState =
      new NodeId(UShort.MIN, uint(18485));

  public static final NodeId InstrumentDiagnosticAlarmType_AlarmGroup_Placeholder =
      new NodeId(UShort.MIN, uint(18486));

  public static final NodeId InstrumentDiagnosticAlarmType_ReAlarmTime =
      new NodeId(UShort.MIN, uint(18487));

  public static final NodeId InstrumentDiagnosticAlarmType_ReAlarmRepeatCount =
      new NodeId(UShort.MIN, uint(18488));

  public static final NodeId InstrumentDiagnosticAlarmType_Silence =
      new NodeId(UShort.MIN, uint(18489));

  public static final NodeId InstrumentDiagnosticAlarmType_Suppress =
      new NodeId(UShort.MIN, uint(18490));

  public static final NodeId InstrumentDiagnosticAlarmType_Unsuppress =
      new NodeId(UShort.MIN, uint(18491));

  public static final NodeId InstrumentDiagnosticAlarmType_RemoveFromService =
      new NodeId(UShort.MIN, uint(18492));

  public static final NodeId InstrumentDiagnosticAlarmType_PlaceInService =
      new NodeId(UShort.MIN, uint(18493));

  public static final NodeId InstrumentDiagnosticAlarmType_Reset =
      new NodeId(UShort.MIN, uint(18494));

  public static final NodeId InstrumentDiagnosticAlarmType_NormalState =
      new NodeId(UShort.MIN, uint(18495));

  public static final NodeId SystemDiagnosticAlarmType = new NodeId(UShort.MIN, uint(18496));

  public static final NodeId SystemDiagnosticAlarmType_EventId =
      new NodeId(UShort.MIN, uint(18497));

  public static final NodeId SystemDiagnosticAlarmType_EventType =
      new NodeId(UShort.MIN, uint(18498));

  public static final NodeId SystemDiagnosticAlarmType_SourceNode =
      new NodeId(UShort.MIN, uint(18499));

  public static final NodeId SystemDiagnosticAlarmType_SourceName =
      new NodeId(UShort.MIN, uint(18500));

  public static final NodeId SystemDiagnosticAlarmType_Time = new NodeId(UShort.MIN, uint(18501));

  public static final NodeId SystemDiagnosticAlarmType_ReceiveTime =
      new NodeId(UShort.MIN, uint(18502));

  public static final NodeId SystemDiagnosticAlarmType_LocalTime =
      new NodeId(UShort.MIN, uint(18503));

  public static final NodeId SystemDiagnosticAlarmType_Message =
      new NodeId(UShort.MIN, uint(18504));

  public static final NodeId SystemDiagnosticAlarmType_Severity =
      new NodeId(UShort.MIN, uint(18505));

  public static final NodeId SystemDiagnosticAlarmType_ConditionClassId =
      new NodeId(UShort.MIN, uint(18506));

  public static final NodeId SystemDiagnosticAlarmType_ConditionClassName =
      new NodeId(UShort.MIN, uint(18507));

  public static final NodeId SystemDiagnosticAlarmType_ConditionSubClassId =
      new NodeId(UShort.MIN, uint(18508));

  public static final NodeId SystemDiagnosticAlarmType_ConditionSubClassName =
      new NodeId(UShort.MIN, uint(18509));

  public static final NodeId SystemDiagnosticAlarmType_ConditionName =
      new NodeId(UShort.MIN, uint(18510));

  public static final NodeId SystemDiagnosticAlarmType_BranchId =
      new NodeId(UShort.MIN, uint(18511));

  public static final NodeId SystemDiagnosticAlarmType_Retain = new NodeId(UShort.MIN, uint(18512));

  public static final NodeId SystemDiagnosticAlarmType_EnabledState =
      new NodeId(UShort.MIN, uint(18513));

  public static final NodeId SystemDiagnosticAlarmType_EnabledState_Id =
      new NodeId(UShort.MIN, uint(18514));

  public static final NodeId SystemDiagnosticAlarmType_EnabledState_Name =
      new NodeId(UShort.MIN, uint(18515));

  public static final NodeId SystemDiagnosticAlarmType_EnabledState_Number =
      new NodeId(UShort.MIN, uint(18516));

  public static final NodeId SystemDiagnosticAlarmType_EnabledState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(18517));

  public static final NodeId SystemDiagnosticAlarmType_EnabledState_TransitionTime =
      new NodeId(UShort.MIN, uint(18518));

  public static final NodeId SystemDiagnosticAlarmType_EnabledState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(18519));

  public static final NodeId SystemDiagnosticAlarmType_EnabledState_TrueState =
      new NodeId(UShort.MIN, uint(18520));

  public static final NodeId SystemDiagnosticAlarmType_EnabledState_FalseState =
      new NodeId(UShort.MIN, uint(18521));

  public static final NodeId SystemDiagnosticAlarmType_Quality =
      new NodeId(UShort.MIN, uint(18522));

  public static final NodeId SystemDiagnosticAlarmType_Quality_SourceTimestamp =
      new NodeId(UShort.MIN, uint(18523));

  public static final NodeId SystemDiagnosticAlarmType_LastSeverity =
      new NodeId(UShort.MIN, uint(18524));

  public static final NodeId SystemDiagnosticAlarmType_LastSeverity_SourceTimestamp =
      new NodeId(UShort.MIN, uint(18525));

  public static final NodeId SystemDiagnosticAlarmType_Comment =
      new NodeId(UShort.MIN, uint(18526));

  public static final NodeId SystemDiagnosticAlarmType_Comment_SourceTimestamp =
      new NodeId(UShort.MIN, uint(18527));

  public static final NodeId SystemDiagnosticAlarmType_ClientUserId =
      new NodeId(UShort.MIN, uint(18528));

  public static final NodeId SystemDiagnosticAlarmType_Disable =
      new NodeId(UShort.MIN, uint(18529));

  public static final NodeId SystemDiagnosticAlarmType_Enable = new NodeId(UShort.MIN, uint(18530));

  public static final NodeId SystemDiagnosticAlarmType_AddComment =
      new NodeId(UShort.MIN, uint(18531));

  public static final NodeId SystemDiagnosticAlarmType_AddComment_InputArguments =
      new NodeId(UShort.MIN, uint(18532));

  public static final NodeId SystemDiagnosticAlarmType_ConditionRefresh =
      new NodeId(UShort.MIN, uint(18533));

  public static final NodeId SystemDiagnosticAlarmType_ConditionRefresh_InputArguments =
      new NodeId(UShort.MIN, uint(18534));

  public static final NodeId SystemDiagnosticAlarmType_ConditionRefresh2 =
      new NodeId(UShort.MIN, uint(18535));

  public static final NodeId SystemDiagnosticAlarmType_ConditionRefresh2_InputArguments =
      new NodeId(UShort.MIN, uint(18536));

  public static final NodeId SystemDiagnosticAlarmType_AckedState =
      new NodeId(UShort.MIN, uint(18537));

  public static final NodeId SystemDiagnosticAlarmType_AckedState_Id =
      new NodeId(UShort.MIN, uint(18538));

  public static final NodeId SystemDiagnosticAlarmType_AckedState_Name =
      new NodeId(UShort.MIN, uint(18539));

  public static final NodeId SystemDiagnosticAlarmType_AckedState_Number =
      new NodeId(UShort.MIN, uint(18540));

  public static final NodeId SystemDiagnosticAlarmType_AckedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(18541));

  public static final NodeId SystemDiagnosticAlarmType_AckedState_TransitionTime =
      new NodeId(UShort.MIN, uint(18542));

  public static final NodeId SystemDiagnosticAlarmType_AckedState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(18543));

  public static final NodeId SystemDiagnosticAlarmType_AckedState_TrueState =
      new NodeId(UShort.MIN, uint(18544));

  public static final NodeId SystemDiagnosticAlarmType_AckedState_FalseState =
      new NodeId(UShort.MIN, uint(18545));

  public static final NodeId SystemDiagnosticAlarmType_ConfirmedState =
      new NodeId(UShort.MIN, uint(18546));

  public static final NodeId SystemDiagnosticAlarmType_ConfirmedState_Id =
      new NodeId(UShort.MIN, uint(18547));

  public static final NodeId SystemDiagnosticAlarmType_ConfirmedState_Name =
      new NodeId(UShort.MIN, uint(18548));

  public static final NodeId SystemDiagnosticAlarmType_ConfirmedState_Number =
      new NodeId(UShort.MIN, uint(18549));

  public static final NodeId SystemDiagnosticAlarmType_ConfirmedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(18550));

  public static final NodeId SystemDiagnosticAlarmType_ConfirmedState_TransitionTime =
      new NodeId(UShort.MIN, uint(18551));

  public static final NodeId SystemDiagnosticAlarmType_ConfirmedState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(18552));

  public static final NodeId SystemDiagnosticAlarmType_ConfirmedState_TrueState =
      new NodeId(UShort.MIN, uint(18553));

  public static final NodeId SystemDiagnosticAlarmType_ConfirmedState_FalseState =
      new NodeId(UShort.MIN, uint(18554));

  public static final NodeId SystemDiagnosticAlarmType_Acknowledge =
      new NodeId(UShort.MIN, uint(18555));

  public static final NodeId SystemDiagnosticAlarmType_Acknowledge_InputArguments =
      new NodeId(UShort.MIN, uint(18556));

  public static final NodeId SystemDiagnosticAlarmType_Confirm =
      new NodeId(UShort.MIN, uint(18557));

  public static final NodeId SystemDiagnosticAlarmType_Confirm_InputArguments =
      new NodeId(UShort.MIN, uint(18558));

  public static final NodeId SystemDiagnosticAlarmType_ActiveState =
      new NodeId(UShort.MIN, uint(18559));

  public static final NodeId SystemDiagnosticAlarmType_ActiveState_Id =
      new NodeId(UShort.MIN, uint(18560));

  public static final NodeId SystemDiagnosticAlarmType_ActiveState_Name =
      new NodeId(UShort.MIN, uint(18561));

  public static final NodeId SystemDiagnosticAlarmType_ActiveState_Number =
      new NodeId(UShort.MIN, uint(18562));

  public static final NodeId SystemDiagnosticAlarmType_ActiveState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(18563));

  public static final NodeId SystemDiagnosticAlarmType_ActiveState_TransitionTime =
      new NodeId(UShort.MIN, uint(18564));

  public static final NodeId SystemDiagnosticAlarmType_ActiveState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(18565));

  public static final NodeId SystemDiagnosticAlarmType_ActiveState_TrueState =
      new NodeId(UShort.MIN, uint(18566));

  public static final NodeId SystemDiagnosticAlarmType_ActiveState_FalseState =
      new NodeId(UShort.MIN, uint(18567));

  public static final NodeId SystemDiagnosticAlarmType_InputNode =
      new NodeId(UShort.MIN, uint(18568));

  public static final NodeId SystemDiagnosticAlarmType_SuppressedState =
      new NodeId(UShort.MIN, uint(18569));

  public static final NodeId SystemDiagnosticAlarmType_SuppressedState_Id =
      new NodeId(UShort.MIN, uint(18570));

  public static final NodeId SystemDiagnosticAlarmType_SuppressedState_Name =
      new NodeId(UShort.MIN, uint(18571));

  public static final NodeId SystemDiagnosticAlarmType_SuppressedState_Number =
      new NodeId(UShort.MIN, uint(18572));

  public static final NodeId SystemDiagnosticAlarmType_SuppressedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(18573));

  public static final NodeId SystemDiagnosticAlarmType_SuppressedState_TransitionTime =
      new NodeId(UShort.MIN, uint(18574));

  public static final NodeId SystemDiagnosticAlarmType_SuppressedState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(18575));

  public static final NodeId SystemDiagnosticAlarmType_SuppressedState_TrueState =
      new NodeId(UShort.MIN, uint(18576));

  public static final NodeId SystemDiagnosticAlarmType_SuppressedState_FalseState =
      new NodeId(UShort.MIN, uint(18577));

  public static final NodeId SystemDiagnosticAlarmType_OutOfServiceState =
      new NodeId(UShort.MIN, uint(18578));

  public static final NodeId SystemDiagnosticAlarmType_OutOfServiceState_Id =
      new NodeId(UShort.MIN, uint(18579));

  public static final NodeId SystemDiagnosticAlarmType_OutOfServiceState_Name =
      new NodeId(UShort.MIN, uint(18580));

  public static final NodeId SystemDiagnosticAlarmType_OutOfServiceState_Number =
      new NodeId(UShort.MIN, uint(18581));

  public static final NodeId SystemDiagnosticAlarmType_OutOfServiceState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(18582));

  public static final NodeId SystemDiagnosticAlarmType_OutOfServiceState_TransitionTime =
      new NodeId(UShort.MIN, uint(18583));

  public static final NodeId SystemDiagnosticAlarmType_OutOfServiceState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(18584));

  public static final NodeId SystemDiagnosticAlarmType_OutOfServiceState_TrueState =
      new NodeId(UShort.MIN, uint(18585));

  public static final NodeId SystemDiagnosticAlarmType_OutOfServiceState_FalseState =
      new NodeId(UShort.MIN, uint(18586));

  public static final NodeId SystemDiagnosticAlarmType_ShelvingState =
      new NodeId(UShort.MIN, uint(18587));

  public static final NodeId SystemDiagnosticAlarmType_ShelvingState_CurrentState =
      new NodeId(UShort.MIN, uint(18588));

  public static final NodeId SystemDiagnosticAlarmType_ShelvingState_CurrentState_Id =
      new NodeId(UShort.MIN, uint(18589));

  public static final NodeId SystemDiagnosticAlarmType_ShelvingState_CurrentState_Name =
      new NodeId(UShort.MIN, uint(18590));

  public static final NodeId SystemDiagnosticAlarmType_ShelvingState_CurrentState_Number =
      new NodeId(UShort.MIN, uint(18591));

  public static final NodeId
      SystemDiagnosticAlarmType_ShelvingState_CurrentState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(18592));

  public static final NodeId SystemDiagnosticAlarmType_ShelvingState_LastTransition =
      new NodeId(UShort.MIN, uint(18593));

  public static final NodeId SystemDiagnosticAlarmType_ShelvingState_LastTransition_Id =
      new NodeId(UShort.MIN, uint(18594));

  public static final NodeId SystemDiagnosticAlarmType_ShelvingState_LastTransition_Name =
      new NodeId(UShort.MIN, uint(18595));

  public static final NodeId SystemDiagnosticAlarmType_ShelvingState_LastTransition_Number =
      new NodeId(UShort.MIN, uint(18596));

  public static final NodeId SystemDiagnosticAlarmType_ShelvingState_LastTransition_TransitionTime =
      new NodeId(UShort.MIN, uint(18597));

  public static final NodeId
      SystemDiagnosticAlarmType_ShelvingState_LastTransition_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(18598));

  public static final NodeId SystemDiagnosticAlarmType_ShelvingState_AvailableStates =
      new NodeId(UShort.MIN, uint(18599));

  public static final NodeId SystemDiagnosticAlarmType_ShelvingState_AvailableTransitions =
      new NodeId(UShort.MIN, uint(18600));

  public static final NodeId SystemDiagnosticAlarmType_ShelvingState_UnshelveTime =
      new NodeId(UShort.MIN, uint(18601));

  public static final NodeId SystemDiagnosticAlarmType_ShelvingState_TimedShelve =
      new NodeId(UShort.MIN, uint(18602));

  public static final NodeId SystemDiagnosticAlarmType_ShelvingState_TimedShelve_InputArguments =
      new NodeId(UShort.MIN, uint(18603));

  public static final NodeId SystemDiagnosticAlarmType_ShelvingState_Unshelve =
      new NodeId(UShort.MIN, uint(18604));

  public static final NodeId SystemDiagnosticAlarmType_ShelvingState_OneShotShelve =
      new NodeId(UShort.MIN, uint(18605));

  public static final NodeId SystemDiagnosticAlarmType_SuppressedOrShelved =
      new NodeId(UShort.MIN, uint(18606));

  public static final NodeId SystemDiagnosticAlarmType_MaxTimeShelved =
      new NodeId(UShort.MIN, uint(18607));

  public static final NodeId SystemDiagnosticAlarmType_AudibleEnabled =
      new NodeId(UShort.MIN, uint(18608));

  public static final NodeId SystemDiagnosticAlarmType_AudibleSound =
      new NodeId(UShort.MIN, uint(18609));

  public static final NodeId SystemDiagnosticAlarmType_AudibleSound_ListId =
      new NodeId(UShort.MIN, uint(18610));

  public static final NodeId SystemDiagnosticAlarmType_AudibleSound_AgencyId =
      new NodeId(UShort.MIN, uint(18611));

  public static final NodeId SystemDiagnosticAlarmType_AudibleSound_VersionId =
      new NodeId(UShort.MIN, uint(18612));

  public static final NodeId SystemDiagnosticAlarmType_SilenceState =
      new NodeId(UShort.MIN, uint(18613));

  public static final NodeId SystemDiagnosticAlarmType_SilenceState_Id =
      new NodeId(UShort.MIN, uint(18614));

  public static final NodeId SystemDiagnosticAlarmType_SilenceState_Name =
      new NodeId(UShort.MIN, uint(18615));

  public static final NodeId SystemDiagnosticAlarmType_SilenceState_Number =
      new NodeId(UShort.MIN, uint(18616));

  public static final NodeId SystemDiagnosticAlarmType_SilenceState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(18617));

  public static final NodeId SystemDiagnosticAlarmType_SilenceState_TransitionTime =
      new NodeId(UShort.MIN, uint(18618));

  public static final NodeId SystemDiagnosticAlarmType_SilenceState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(18619));

  public static final NodeId SystemDiagnosticAlarmType_SilenceState_TrueState =
      new NodeId(UShort.MIN, uint(18620));

  public static final NodeId SystemDiagnosticAlarmType_SilenceState_FalseState =
      new NodeId(UShort.MIN, uint(18621));

  public static final NodeId SystemDiagnosticAlarmType_OnDelay =
      new NodeId(UShort.MIN, uint(18622));

  public static final NodeId SystemDiagnosticAlarmType_OffDelay =
      new NodeId(UShort.MIN, uint(18623));

  public static final NodeId SystemDiagnosticAlarmType_FirstInGroupFlag =
      new NodeId(UShort.MIN, uint(18624));

  public static final NodeId SystemDiagnosticAlarmType_FirstInGroup =
      new NodeId(UShort.MIN, uint(18625));

  public static final NodeId SystemDiagnosticAlarmType_LatchedState =
      new NodeId(UShort.MIN, uint(18626));

  public static final NodeId SystemDiagnosticAlarmType_LatchedState_Id =
      new NodeId(UShort.MIN, uint(18627));

  public static final NodeId SystemDiagnosticAlarmType_LatchedState_Name =
      new NodeId(UShort.MIN, uint(18628));

  public static final NodeId SystemDiagnosticAlarmType_LatchedState_Number =
      new NodeId(UShort.MIN, uint(18629));

  public static final NodeId SystemDiagnosticAlarmType_LatchedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(18630));

  public static final NodeId SystemDiagnosticAlarmType_LatchedState_TransitionTime =
      new NodeId(UShort.MIN, uint(18631));

  public static final NodeId SystemDiagnosticAlarmType_LatchedState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(18632));

  public static final NodeId SystemDiagnosticAlarmType_LatchedState_TrueState =
      new NodeId(UShort.MIN, uint(18633));

  public static final NodeId SystemDiagnosticAlarmType_LatchedState_FalseState =
      new NodeId(UShort.MIN, uint(18634));

  public static final NodeId SystemDiagnosticAlarmType_AlarmGroup_Placeholder =
      new NodeId(UShort.MIN, uint(18635));

  public static final NodeId SystemDiagnosticAlarmType_ReAlarmTime =
      new NodeId(UShort.MIN, uint(18636));

  public static final NodeId SystemDiagnosticAlarmType_ReAlarmRepeatCount =
      new NodeId(UShort.MIN, uint(18637));

  public static final NodeId SystemDiagnosticAlarmType_Silence =
      new NodeId(UShort.MIN, uint(18638));

  public static final NodeId SystemDiagnosticAlarmType_Suppress =
      new NodeId(UShort.MIN, uint(18639));

  public static final NodeId SystemDiagnosticAlarmType_Unsuppress =
      new NodeId(UShort.MIN, uint(18640));

  public static final NodeId SystemDiagnosticAlarmType_RemoveFromService =
      new NodeId(UShort.MIN, uint(18641));

  public static final NodeId SystemDiagnosticAlarmType_PlaceInService =
      new NodeId(UShort.MIN, uint(18642));

  public static final NodeId SystemDiagnosticAlarmType_Reset = new NodeId(UShort.MIN, uint(18643));

  public static final NodeId SystemDiagnosticAlarmType_NormalState =
      new NodeId(UShort.MIN, uint(18644));

  public static final NodeId CertificateExpirationAlarmType_LatchedState =
      new NodeId(UShort.MIN, uint(18645));

  public static final NodeId CertificateExpirationAlarmType_LatchedState_Id =
      new NodeId(UShort.MIN, uint(18646));

  public static final NodeId CertificateExpirationAlarmType_LatchedState_Name =
      new NodeId(UShort.MIN, uint(18647));

  public static final NodeId CertificateExpirationAlarmType_LatchedState_Number =
      new NodeId(UShort.MIN, uint(18648));

  public static final NodeId CertificateExpirationAlarmType_LatchedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(18649));

  public static final NodeId CertificateExpirationAlarmType_LatchedState_TransitionTime =
      new NodeId(UShort.MIN, uint(18650));

  public static final NodeId CertificateExpirationAlarmType_LatchedState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(18651));

  public static final NodeId CertificateExpirationAlarmType_LatchedState_TrueState =
      new NodeId(UShort.MIN, uint(18652));

  public static final NodeId CertificateExpirationAlarmType_LatchedState_FalseState =
      new NodeId(UShort.MIN, uint(18653));

  public static final NodeId CertificateExpirationAlarmType_Reset =
      new NodeId(UShort.MIN, uint(18654));

  public static final NodeId DiscrepancyAlarmType_LatchedState =
      new NodeId(UShort.MIN, uint(18655));

  public static final NodeId DiscrepancyAlarmType_LatchedState_Id =
      new NodeId(UShort.MIN, uint(18656));

  public static final NodeId DiscrepancyAlarmType_LatchedState_Name =
      new NodeId(UShort.MIN, uint(18657));

  public static final NodeId DiscrepancyAlarmType_LatchedState_Number =
      new NodeId(UShort.MIN, uint(18658));

  public static final NodeId DiscrepancyAlarmType_LatchedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(18659));

  public static final NodeId DiscrepancyAlarmType_LatchedState_TransitionTime =
      new NodeId(UShort.MIN, uint(18660));

  public static final NodeId DiscrepancyAlarmType_LatchedState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(18661));

  public static final NodeId DiscrepancyAlarmType_LatchedState_TrueState =
      new NodeId(UShort.MIN, uint(18662));

  public static final NodeId DiscrepancyAlarmType_LatchedState_FalseState =
      new NodeId(UShort.MIN, uint(18663));

  public static final NodeId DiscrepancyAlarmType_Reset = new NodeId(UShort.MIN, uint(18664));

  public static final NodeId StatisticalConditionClassType = new NodeId(UShort.MIN, uint(18665));

  public static final NodeId AlarmMetricsType_Reset = new NodeId(UShort.MIN, uint(18666));

  public static final NodeId PublishSubscribeType_ConnectionName_Placeholder_Diagnostics =
      new NodeId(UShort.MIN, uint(18667));

  public static final NodeId
      PublishSubscribeType_ConnectionName_Placeholder_Diagnostics_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(18668));

  public static final NodeId
      PublishSubscribeType_ConnectionName_Placeholder_Diagnostics_TotalInformation =
          new NodeId(UShort.MIN, uint(18669));

  public static final NodeId
      PublishSubscribeType_ConnectionName_Placeholder_Diagnostics_TotalInformation_Active =
          new NodeId(UShort.MIN, uint(18670));

  public static final NodeId
      PublishSubscribeType_ConnectionName_Placeholder_Diagnostics_TotalInformation_Classification =
          new NodeId(UShort.MIN, uint(18671));

  public static final NodeId
      PublishSubscribeType_ConnectionName_Placeholder_Diagnostics_TotalInformation_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(18672));

  public static final NodeId
      PublishSubscribeType_ConnectionName_Placeholder_Diagnostics_TotalInformation_TimeFirstChange =
          new NodeId(UShort.MIN, uint(18673));

  public static final NodeId
      PublishSubscribeType_ConnectionName_Placeholder_Diagnostics_TotalError =
          new NodeId(UShort.MIN, uint(18674));

  public static final NodeId
      PublishSubscribeType_ConnectionName_Placeholder_Diagnostics_TotalError_Active =
          new NodeId(UShort.MIN, uint(18675));

  public static final NodeId
      PublishSubscribeType_ConnectionName_Placeholder_Diagnostics_TotalError_Classification =
          new NodeId(UShort.MIN, uint(18676));

  public static final NodeId
      PublishSubscribeType_ConnectionName_Placeholder_Diagnostics_TotalError_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(18677));

  public static final NodeId
      PublishSubscribeType_ConnectionName_Placeholder_Diagnostics_TotalError_TimeFirstChange =
          new NodeId(UShort.MIN, uint(18678));

  public static final NodeId PublishSubscribeType_ConnectionName_Placeholder_Diagnostics_Reset =
      new NodeId(UShort.MIN, uint(18679));

  public static final NodeId PublishSubscribeType_ConnectionName_Placeholder_Diagnostics_SubError =
      new NodeId(UShort.MIN, uint(18680));

  public static final NodeId PublishSubscribeType_ConnectionName_Placeholder_Diagnostics_Counters =
      new NodeId(UShort.MIN, uint(18681));

  public static final NodeId
      PublishSubscribeType_ConnectionName_Placeholder_Diagnostics_Counters_StateError =
          new NodeId(UShort.MIN, uint(18682));

  public static final NodeId
      PublishSubscribeType_ConnectionName_Placeholder_Diagnostics_Counters_StateError_Active =
          new NodeId(UShort.MIN, uint(18683));

  public static final NodeId
      PublishSubscribeType_ConnectionName_Placeholder_Diagnostics_Counters_StateError_Classification =
          new NodeId(UShort.MIN, uint(18684));

  public static final NodeId
      PublishSubscribeType_ConnectionName_Placeholder_Diagnostics_Counters_StateError_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(18685));

  public static final NodeId
      PublishSubscribeType_ConnectionName_Placeholder_Diagnostics_Counters_StateError_TimeFirstChange =
          new NodeId(UShort.MIN, uint(18686));

  public static final NodeId
      PublishSubscribeType_ConnectionName_Placeholder_Diagnostics_Counters_StateOperationalByMethod =
          new NodeId(UShort.MIN, uint(18687));

  public static final NodeId
      PublishSubscribeType_ConnectionName_Placeholder_Diagnostics_Counters_StateOperationalByMethod_Active =
          new NodeId(UShort.MIN, uint(18688));

  public static final NodeId
      PublishSubscribeType_ConnectionName_Placeholder_Diagnostics_Counters_StateOperationalByMethod_Classification =
          new NodeId(UShort.MIN, uint(18689));

  public static final NodeId
      PublishSubscribeType_ConnectionName_Placeholder_Diagnostics_Counters_StateOperationalByMethod_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(18690));

  public static final NodeId
      PublishSubscribeType_ConnectionName_Placeholder_Diagnostics_Counters_StateOperationalByMethod_TimeFirstChange =
          new NodeId(UShort.MIN, uint(18691));

  public static final NodeId
      PublishSubscribeType_ConnectionName_Placeholder_Diagnostics_Counters_StateOperationalByParent =
          new NodeId(UShort.MIN, uint(18692));

  public static final NodeId
      PublishSubscribeType_ConnectionName_Placeholder_Diagnostics_Counters_StateOperationalByParent_Active =
          new NodeId(UShort.MIN, uint(18693));

  public static final NodeId
      PublishSubscribeType_ConnectionName_Placeholder_Diagnostics_Counters_StateOperationalByParent_Classification =
          new NodeId(UShort.MIN, uint(18694));

  public static final NodeId
      PublishSubscribeType_ConnectionName_Placeholder_Diagnostics_Counters_StateOperationalByParent_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(18695));

  public static final NodeId
      PublishSubscribeType_ConnectionName_Placeholder_Diagnostics_Counters_StateOperationalByParent_TimeFirstChange =
          new NodeId(UShort.MIN, uint(18696));

  public static final NodeId
      PublishSubscribeType_ConnectionName_Placeholder_Diagnostics_Counters_StateOperationalFromError =
          new NodeId(UShort.MIN, uint(18697));

  public static final NodeId
      PublishSubscribeType_ConnectionName_Placeholder_Diagnostics_Counters_StateOperationalFromError_Active =
          new NodeId(UShort.MIN, uint(18698));

  public static final NodeId
      PublishSubscribeType_ConnectionName_Placeholder_Diagnostics_Counters_StateOperationalFromError_Classification =
          new NodeId(UShort.MIN, uint(18699));

  public static final NodeId
      PublishSubscribeType_ConnectionName_Placeholder_Diagnostics_Counters_StateOperationalFromError_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(18700));

  public static final NodeId
      PublishSubscribeType_ConnectionName_Placeholder_Diagnostics_Counters_StateOperationalFromError_TimeFirstChange =
          new NodeId(UShort.MIN, uint(18701));

  public static final NodeId
      PublishSubscribeType_ConnectionName_Placeholder_Diagnostics_Counters_StatePausedByParent =
          new NodeId(UShort.MIN, uint(18702));

  public static final NodeId
      PublishSubscribeType_ConnectionName_Placeholder_Diagnostics_Counters_StatePausedByParent_Active =
          new NodeId(UShort.MIN, uint(18703));

  public static final NodeId
      PublishSubscribeType_ConnectionName_Placeholder_Diagnostics_Counters_StatePausedByParent_Classification =
          new NodeId(UShort.MIN, uint(18704));

  public static final NodeId
      PublishSubscribeType_ConnectionName_Placeholder_Diagnostics_Counters_StatePausedByParent_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(18705));

  public static final NodeId
      PublishSubscribeType_ConnectionName_Placeholder_Diagnostics_Counters_StatePausedByParent_TimeFirstChange =
          new NodeId(UShort.MIN, uint(18706));

  public static final NodeId
      PublishSubscribeType_ConnectionName_Placeholder_Diagnostics_Counters_StateDisabledByMethod =
          new NodeId(UShort.MIN, uint(18707));

  public static final NodeId
      PublishSubscribeType_ConnectionName_Placeholder_Diagnostics_Counters_StateDisabledByMethod_Active =
          new NodeId(UShort.MIN, uint(18708));

  public static final NodeId
      PublishSubscribeType_ConnectionName_Placeholder_Diagnostics_Counters_StateDisabledByMethod_Classification =
          new NodeId(UShort.MIN, uint(18709));

  public static final NodeId
      PublishSubscribeType_ConnectionName_Placeholder_Diagnostics_Counters_StateDisabledByMethod_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(18710));

  public static final NodeId
      PublishSubscribeType_ConnectionName_Placeholder_Diagnostics_Counters_StateDisabledByMethod_TimeFirstChange =
          new NodeId(UShort.MIN, uint(18711));

  public static final NodeId
      PublishSubscribeType_ConnectionName_Placeholder_Diagnostics_LiveValues =
          new NodeId(UShort.MIN, uint(18712));

  public static final NodeId
      PublishSubscribeType_ConnectionName_Placeholder_Diagnostics_LiveValues_ResolvedAddress =
          new NodeId(UShort.MIN, uint(18713));

  public static final NodeId
      PublishSubscribeType_ConnectionName_Placeholder_Diagnostics_LiveValues_ResolvedAddress_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(18714));

  public static final NodeId PublishSubscribeType_Diagnostics = new NodeId(UShort.MIN, uint(18715));

  public static final NodeId PublishSubscribeType_Diagnostics_DiagnosticsLevel =
      new NodeId(UShort.MIN, uint(18716));

  public static final NodeId PublishSubscribeType_Diagnostics_TotalInformation =
      new NodeId(UShort.MIN, uint(18717));

  public static final NodeId PublishSubscribeType_Diagnostics_TotalInformation_Active =
      new NodeId(UShort.MIN, uint(18718));

  public static final NodeId PublishSubscribeType_Diagnostics_TotalInformation_Classification =
      new NodeId(UShort.MIN, uint(18719));

  public static final NodeId PublishSubscribeType_Diagnostics_TotalInformation_DiagnosticsLevel =
      new NodeId(UShort.MIN, uint(18720));

  public static final NodeId PublishSubscribeType_Diagnostics_TotalInformation_TimeFirstChange =
      new NodeId(UShort.MIN, uint(18721));

  public static final NodeId PublishSubscribeType_Diagnostics_TotalError =
      new NodeId(UShort.MIN, uint(18722));

  public static final NodeId PublishSubscribeType_Diagnostics_TotalError_Active =
      new NodeId(UShort.MIN, uint(18723));

  public static final NodeId PublishSubscribeType_Diagnostics_TotalError_Classification =
      new NodeId(UShort.MIN, uint(18724));

  public static final NodeId PublishSubscribeType_Diagnostics_TotalError_DiagnosticsLevel =
      new NodeId(UShort.MIN, uint(18725));

  public static final NodeId PublishSubscribeType_Diagnostics_TotalError_TimeFirstChange =
      new NodeId(UShort.MIN, uint(18726));

  public static final NodeId PublishSubscribeType_Diagnostics_Reset =
      new NodeId(UShort.MIN, uint(18727));

  public static final NodeId PublishSubscribeType_Diagnostics_SubError =
      new NodeId(UShort.MIN, uint(18728));

  public static final NodeId PublishSubscribeType_Diagnostics_Counters =
      new NodeId(UShort.MIN, uint(18729));

  public static final NodeId PublishSubscribeType_Diagnostics_Counters_StateError =
      new NodeId(UShort.MIN, uint(18730));

  public static final NodeId PublishSubscribeType_Diagnostics_Counters_StateError_Active =
      new NodeId(UShort.MIN, uint(18731));

  public static final NodeId PublishSubscribeType_Diagnostics_Counters_StateError_Classification =
      new NodeId(UShort.MIN, uint(18732));

  public static final NodeId PublishSubscribeType_Diagnostics_Counters_StateError_DiagnosticsLevel =
      new NodeId(UShort.MIN, uint(18733));

  public static final NodeId PublishSubscribeType_Diagnostics_Counters_StateError_TimeFirstChange =
      new NodeId(UShort.MIN, uint(18734));

  public static final NodeId PublishSubscribeType_Diagnostics_Counters_StateOperationalByMethod =
      new NodeId(UShort.MIN, uint(18735));

  public static final NodeId
      PublishSubscribeType_Diagnostics_Counters_StateOperationalByMethod_Active =
          new NodeId(UShort.MIN, uint(18736));

  public static final NodeId
      PublishSubscribeType_Diagnostics_Counters_StateOperationalByMethod_Classification =
          new NodeId(UShort.MIN, uint(18737));

  public static final NodeId
      PublishSubscribeType_Diagnostics_Counters_StateOperationalByMethod_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(18738));

  public static final NodeId
      PublishSubscribeType_Diagnostics_Counters_StateOperationalByMethod_TimeFirstChange =
          new NodeId(UShort.MIN, uint(18739));

  public static final NodeId PublishSubscribeType_Diagnostics_Counters_StateOperationalByParent =
      new NodeId(UShort.MIN, uint(18740));

  public static final NodeId
      PublishSubscribeType_Diagnostics_Counters_StateOperationalByParent_Active =
          new NodeId(UShort.MIN, uint(18741));

  public static final NodeId
      PublishSubscribeType_Diagnostics_Counters_StateOperationalByParent_Classification =
          new NodeId(UShort.MIN, uint(18742));

  public static final NodeId
      PublishSubscribeType_Diagnostics_Counters_StateOperationalByParent_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(18743));

  public static final NodeId
      PublishSubscribeType_Diagnostics_Counters_StateOperationalByParent_TimeFirstChange =
          new NodeId(UShort.MIN, uint(18744));

  public static final NodeId PublishSubscribeType_Diagnostics_Counters_StateOperationalFromError =
      new NodeId(UShort.MIN, uint(18745));

  public static final NodeId
      PublishSubscribeType_Diagnostics_Counters_StateOperationalFromError_Active =
          new NodeId(UShort.MIN, uint(18746));

  public static final NodeId
      PublishSubscribeType_Diagnostics_Counters_StateOperationalFromError_Classification =
          new NodeId(UShort.MIN, uint(18747));

  public static final NodeId
      PublishSubscribeType_Diagnostics_Counters_StateOperationalFromError_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(18748));

  public static final NodeId
      PublishSubscribeType_Diagnostics_Counters_StateOperationalFromError_TimeFirstChange =
          new NodeId(UShort.MIN, uint(18749));

  public static final NodeId PublishSubscribeType_Diagnostics_Counters_StatePausedByParent =
      new NodeId(UShort.MIN, uint(18750));

  public static final NodeId PublishSubscribeType_Diagnostics_Counters_StatePausedByParent_Active =
      new NodeId(UShort.MIN, uint(18751));

  public static final NodeId
      PublishSubscribeType_Diagnostics_Counters_StatePausedByParent_Classification =
          new NodeId(UShort.MIN, uint(18752));

  public static final NodeId
      PublishSubscribeType_Diagnostics_Counters_StatePausedByParent_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(18753));

  public static final NodeId
      PublishSubscribeType_Diagnostics_Counters_StatePausedByParent_TimeFirstChange =
          new NodeId(UShort.MIN, uint(18754));

  public static final NodeId PublishSubscribeType_Diagnostics_Counters_StateDisabledByMethod =
      new NodeId(UShort.MIN, uint(18755));

  public static final NodeId
      PublishSubscribeType_Diagnostics_Counters_StateDisabledByMethod_Active =
          new NodeId(UShort.MIN, uint(18756));

  public static final NodeId
      PublishSubscribeType_Diagnostics_Counters_StateDisabledByMethod_Classification =
          new NodeId(UShort.MIN, uint(18757));

  public static final NodeId
      PublishSubscribeType_Diagnostics_Counters_StateDisabledByMethod_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(18758));

  public static final NodeId
      PublishSubscribeType_Diagnostics_Counters_StateDisabledByMethod_TimeFirstChange =
          new NodeId(UShort.MIN, uint(18759));

  public static final NodeId PublishSubscribeType_Diagnostics_LiveValues =
      new NodeId(UShort.MIN, uint(18760));

  public static final NodeId PublishSubscribeType_Diagnostics_LiveValues_ConfiguredDataSetWriters =
      new NodeId(UShort.MIN, uint(18761));

  public static final NodeId
      PublishSubscribeType_Diagnostics_LiveValues_ConfiguredDataSetWriters_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(18762));

  public static final NodeId PublishSubscribeType_Diagnostics_LiveValues_ConfiguredDataSetReaders =
      new NodeId(UShort.MIN, uint(18763));

  public static final NodeId
      PublishSubscribeType_Diagnostics_LiveValues_ConfiguredDataSetReaders_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(18764));

  public static final NodeId PublishSubscribeType_Diagnostics_LiveValues_OperationalDataSetWriters =
      new NodeId(UShort.MIN, uint(18765));

  public static final NodeId
      PublishSubscribeType_Diagnostics_LiveValues_OperationalDataSetWriters_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(18766));

  public static final NodeId PublishSubscribeType_Diagnostics_LiveValues_OperationalDataSetReaders =
      new NodeId(UShort.MIN, uint(18767));

  public static final NodeId
      PublishSubscribeType_Diagnostics_LiveValues_OperationalDataSetReaders_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(18768));

  public static final NodeId ThreeDVectorType_X = new NodeId(UShort.MIN, uint(18769));

  public static final NodeId ThreeDVectorType_Y = new NodeId(UShort.MIN, uint(18770));

  public static final NodeId ThreeDVectorType_Z = new NodeId(UShort.MIN, uint(18771));

  public static final NodeId CartesianCoordinatesType = new NodeId(UShort.MIN, uint(18772));

  public static final NodeId CartesianCoordinatesType_LengthUnit =
      new NodeId(UShort.MIN, uint(18773));

  public static final NodeId ThreeDCartesianCoordinatesType = new NodeId(UShort.MIN, uint(18774));

  public static final NodeId ThreeDCartesianCoordinatesType_LengthUnit =
      new NodeId(UShort.MIN, uint(18775));

  public static final NodeId ThreeDCartesianCoordinatesType_X = new NodeId(UShort.MIN, uint(18776));

  public static final NodeId ThreeDCartesianCoordinatesType_Y = new NodeId(UShort.MIN, uint(18777));

  public static final NodeId ThreeDCartesianCoordinatesType_Z = new NodeId(UShort.MIN, uint(18778));

  public static final NodeId OrientationType = new NodeId(UShort.MIN, uint(18779));

  public static final NodeId OrientationType_AngleUnit = new NodeId(UShort.MIN, uint(18780));

  public static final NodeId ThreeDOrientationType = new NodeId(UShort.MIN, uint(18781));

  public static final NodeId ThreeDOrientationType_AngleUnit = new NodeId(UShort.MIN, uint(18782));

  public static final NodeId ThreeDOrientationType_A = new NodeId(UShort.MIN, uint(18783));

  public static final NodeId ThreeDOrientationType_B = new NodeId(UShort.MIN, uint(18784));

  public static final NodeId ThreeDOrientationType_C = new NodeId(UShort.MIN, uint(18785));

  public static final NodeId FrameType = new NodeId(UShort.MIN, uint(18786));

  public static final NodeId FrameType_Orientation = new NodeId(UShort.MIN, uint(18787));

  public static final NodeId FrameType_Constant = new NodeId(UShort.MIN, uint(18788));

  public static final NodeId FrameType_BaseFrame = new NodeId(UShort.MIN, uint(18789));

  public static final NodeId FrameType_FixedBase = new NodeId(UShort.MIN, uint(18790));

  public static final NodeId ThreeDFrameType = new NodeId(UShort.MIN, uint(18791));

  public static final NodeId ThreeDFrameType_Orientation = new NodeId(UShort.MIN, uint(18792));

  public static final NodeId ThreeDFrameType_Constant = new NodeId(UShort.MIN, uint(18793));

  public static final NodeId ThreeDFrameType_BaseFrame = new NodeId(UShort.MIN, uint(18794));

  public static final NodeId ThreeDFrameType_FixedBase = new NodeId(UShort.MIN, uint(18795));

  public static final NodeId ThreeDFrameType_CartesianCoordinates =
      new NodeId(UShort.MIN, uint(18796));

  public static final NodeId ThreeDFrameType_CartesianCoordinates_LengthUnit =
      new NodeId(UShort.MIN, uint(18797));

  public static final NodeId ThreeDFrameType_CartesianCoordinates_X =
      new NodeId(UShort.MIN, uint(18798));

  public static final NodeId ThreeDFrameType_CartesianCoordinates_Y =
      new NodeId(UShort.MIN, uint(18799));

  public static final NodeId ThreeDFrameType_CartesianCoordinates_Z =
      new NodeId(UShort.MIN, uint(18800));

  public static final NodeId FrameType_CartesianCoordinates = new NodeId(UShort.MIN, uint(18801));

  public static final NodeId FrameType_CartesianCoordinates_LengthUnit =
      new NodeId(UShort.MIN, uint(18802));

  public static final NodeId FrameType_Orientation_AngleUnit = new NodeId(UShort.MIN, uint(18803));

  public static final NodeId HasWriterGroup = new NodeId(UShort.MIN, uint(18804));

  public static final NodeId HasReaderGroup = new NodeId(UShort.MIN, uint(18805));

  public static final NodeId RationalNumber = new NodeId(UShort.MIN, uint(18806));

  public static final NodeId Vector = new NodeId(UShort.MIN, uint(18807));

  public static final NodeId ThreeDVector = new NodeId(UShort.MIN, uint(18808));

  public static final NodeId CartesianCoordinates = new NodeId(UShort.MIN, uint(18809));

  public static final NodeId ThreeDCartesianCoordinates = new NodeId(UShort.MIN, uint(18810));

  public static final NodeId Orientation = new NodeId(UShort.MIN, uint(18811));

  public static final NodeId ThreeDOrientation = new NodeId(UShort.MIN, uint(18812));

  public static final NodeId Frame = new NodeId(UShort.MIN, uint(18813));

  public static final NodeId ThreeDFrame = new NodeId(UShort.MIN, uint(18814));

  public static final NodeId RationalNumber_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(18815));

  public static final NodeId Vector_Encoding_DefaultBinary = new NodeId(UShort.MIN, uint(18816));

  public static final NodeId ThreeDVector_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(18817));

  public static final NodeId CartesianCoordinates_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(18818));

  public static final NodeId ThreeDCartesianCoordinates_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(18819));

  public static final NodeId Orientation_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(18820));

  public static final NodeId ThreeDOrientation_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(18821));

  public static final NodeId Frame_Encoding_DefaultBinary = new NodeId(UShort.MIN, uint(18822));

  public static final NodeId ThreeDFrame_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(18823));

  public static final NodeId OpcUa_BinarySchema_RationalNumber =
      new NodeId(UShort.MIN, uint(18824));

  public static final NodeId OpcUa_BinarySchema_RationalNumber_DataTypeVersion =
      new NodeId(UShort.MIN, uint(18825));

  public static final NodeId OpcUa_BinarySchema_RationalNumber_DictionaryFragment =
      new NodeId(UShort.MIN, uint(18826));

  public static final NodeId OpcUa_BinarySchema_Vector = new NodeId(UShort.MIN, uint(18827));

  public static final NodeId OpcUa_BinarySchema_Vector_DataTypeVersion =
      new NodeId(UShort.MIN, uint(18828));

  public static final NodeId OpcUa_BinarySchema_Vector_DictionaryFragment =
      new NodeId(UShort.MIN, uint(18829));

  public static final NodeId OpcUa_BinarySchema_ThreeDVector = new NodeId(UShort.MIN, uint(18830));

  public static final NodeId OpcUa_BinarySchema_ThreeDVector_DataTypeVersion =
      new NodeId(UShort.MIN, uint(18831));

  public static final NodeId OpcUa_BinarySchema_ThreeDVector_DictionaryFragment =
      new NodeId(UShort.MIN, uint(18832));

  public static final NodeId OpcUa_BinarySchema_CartesianCoordinates =
      new NodeId(UShort.MIN, uint(18833));

  public static final NodeId OpcUa_BinarySchema_CartesianCoordinates_DataTypeVersion =
      new NodeId(UShort.MIN, uint(18834));

  public static final NodeId OpcUa_BinarySchema_CartesianCoordinates_DictionaryFragment =
      new NodeId(UShort.MIN, uint(18835));

  public static final NodeId OpcUa_BinarySchema_ThreeDCartesianCoordinates =
      new NodeId(UShort.MIN, uint(18836));

  public static final NodeId OpcUa_BinarySchema_ThreeDCartesianCoordinates_DataTypeVersion =
      new NodeId(UShort.MIN, uint(18837));

  public static final NodeId OpcUa_BinarySchema_ThreeDCartesianCoordinates_DictionaryFragment =
      new NodeId(UShort.MIN, uint(18838));

  public static final NodeId OpcUa_BinarySchema_Orientation = new NodeId(UShort.MIN, uint(18839));

  public static final NodeId OpcUa_BinarySchema_Orientation_DataTypeVersion =
      new NodeId(UShort.MIN, uint(18840));

  public static final NodeId OpcUa_BinarySchema_Orientation_DictionaryFragment =
      new NodeId(UShort.MIN, uint(18841));

  public static final NodeId OpcUa_BinarySchema_ThreeDOrientation =
      new NodeId(UShort.MIN, uint(18842));

  public static final NodeId OpcUa_BinarySchema_ThreeDOrientation_DataTypeVersion =
      new NodeId(UShort.MIN, uint(18843));

  public static final NodeId OpcUa_BinarySchema_ThreeDOrientation_DictionaryFragment =
      new NodeId(UShort.MIN, uint(18844));

  public static final NodeId OpcUa_BinarySchema_Frame = new NodeId(UShort.MIN, uint(18845));

  public static final NodeId OpcUa_BinarySchema_Frame_DataTypeVersion =
      new NodeId(UShort.MIN, uint(18846));

  public static final NodeId OpcUa_BinarySchema_Frame_DictionaryFragment =
      new NodeId(UShort.MIN, uint(18847));

  public static final NodeId OpcUa_BinarySchema_ThreeDFrame = new NodeId(UShort.MIN, uint(18848));

  public static final NodeId OpcUa_BinarySchema_ThreeDFrame_DataTypeVersion =
      new NodeId(UShort.MIN, uint(18849));

  public static final NodeId OpcUa_BinarySchema_ThreeDFrame_DictionaryFragment =
      new NodeId(UShort.MIN, uint(18850));

  public static final NodeId RationalNumber_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(18851));

  public static final NodeId Vector_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(18852));

  public static final NodeId ThreeDVector_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(18853));

  public static final NodeId CartesianCoordinates_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(18854));

  public static final NodeId ThreeDCartesianCoordinates_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(18855));

  public static final NodeId Orientation_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(18856));

  public static final NodeId ThreeDOrientation_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(18857));

  public static final NodeId Frame_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(18858));

  public static final NodeId ThreeDFrame_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(18859));

  public static final NodeId OpcUa_XmlSchema_RationalNumber = new NodeId(UShort.MIN, uint(18860));

  public static final NodeId OpcUa_XmlSchema_RationalNumber_DataTypeVersion =
      new NodeId(UShort.MIN, uint(18861));

  public static final NodeId OpcUa_XmlSchema_RationalNumber_DictionaryFragment =
      new NodeId(UShort.MIN, uint(18862));

  public static final NodeId OpcUa_XmlSchema_Vector = new NodeId(UShort.MIN, uint(18863));

  public static final NodeId OpcUa_XmlSchema_Vector_DataTypeVersion =
      new NodeId(UShort.MIN, uint(18864));

  public static final NodeId OpcUa_XmlSchema_Vector_DictionaryFragment =
      new NodeId(UShort.MIN, uint(18865));

  public static final NodeId OpcUa_XmlSchema_ThreeDVector = new NodeId(UShort.MIN, uint(18866));

  public static final NodeId OpcUa_XmlSchema_ThreeDVector_DataTypeVersion =
      new NodeId(UShort.MIN, uint(18867));

  public static final NodeId OpcUa_XmlSchema_ThreeDVector_DictionaryFragment =
      new NodeId(UShort.MIN, uint(18868));

  public static final NodeId OpcUa_XmlSchema_CartesianCoordinates =
      new NodeId(UShort.MIN, uint(18869));

  public static final NodeId OpcUa_XmlSchema_CartesianCoordinates_DataTypeVersion =
      new NodeId(UShort.MIN, uint(18870));

  public static final NodeId PublishedDataSetType_DataSetWriterName_Placeholder_Diagnostics =
      new NodeId(UShort.MIN, uint(18871));

  public static final NodeId
      PublishedDataSetType_DataSetWriterName_Placeholder_Diagnostics_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(18872));

  public static final NodeId
      PublishedDataSetType_DataSetWriterName_Placeholder_Diagnostics_TotalInformation =
          new NodeId(UShort.MIN, uint(18873));

  public static final NodeId
      PublishedDataSetType_DataSetWriterName_Placeholder_Diagnostics_TotalInformation_Active =
          new NodeId(UShort.MIN, uint(18874));

  public static final NodeId
      PublishedDataSetType_DataSetWriterName_Placeholder_Diagnostics_TotalInformation_Classification =
          new NodeId(UShort.MIN, uint(18875));

  public static final NodeId
      PublishedDataSetType_DataSetWriterName_Placeholder_Diagnostics_TotalInformation_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(18876));

  public static final NodeId
      PublishedDataSetType_DataSetWriterName_Placeholder_Diagnostics_TotalInformation_TimeFirstChange =
          new NodeId(UShort.MIN, uint(18877));

  public static final NodeId
      PublishedDataSetType_DataSetWriterName_Placeholder_Diagnostics_TotalError =
          new NodeId(UShort.MIN, uint(18878));

  public static final NodeId
      PublishedDataSetType_DataSetWriterName_Placeholder_Diagnostics_TotalError_Active =
          new NodeId(UShort.MIN, uint(18879));

  public static final NodeId
      PublishedDataSetType_DataSetWriterName_Placeholder_Diagnostics_TotalError_Classification =
          new NodeId(UShort.MIN, uint(18880));

  public static final NodeId
      PublishedDataSetType_DataSetWriterName_Placeholder_Diagnostics_TotalError_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(18881));

  public static final NodeId
      PublishedDataSetType_DataSetWriterName_Placeholder_Diagnostics_TotalError_TimeFirstChange =
          new NodeId(UShort.MIN, uint(18882));

  public static final NodeId PublishedDataSetType_DataSetWriterName_Placeholder_Diagnostics_Reset =
      new NodeId(UShort.MIN, uint(18883));

  public static final NodeId
      PublishedDataSetType_DataSetWriterName_Placeholder_Diagnostics_SubError =
          new NodeId(UShort.MIN, uint(18884));

  public static final NodeId
      PublishedDataSetType_DataSetWriterName_Placeholder_Diagnostics_Counters =
          new NodeId(UShort.MIN, uint(18885));

  public static final NodeId
      PublishedDataSetType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateError =
          new NodeId(UShort.MIN, uint(18886));

  public static final NodeId
      PublishedDataSetType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateError_Active =
          new NodeId(UShort.MIN, uint(18887));

  public static final NodeId
      PublishedDataSetType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateError_Classification =
          new NodeId(UShort.MIN, uint(18888));

  public static final NodeId
      PublishedDataSetType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateError_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(18889));

  public static final NodeId
      PublishedDataSetType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateError_TimeFirstChange =
          new NodeId(UShort.MIN, uint(18890));

  public static final NodeId
      PublishedDataSetType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateOperationalByMethod =
          new NodeId(UShort.MIN, uint(18891));

  public static final NodeId
      PublishedDataSetType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateOperationalByMethod_Active =
          new NodeId(UShort.MIN, uint(18892));

  public static final NodeId
      PublishedDataSetType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateOperationalByMethod_Classification =
          new NodeId(UShort.MIN, uint(18893));

  public static final NodeId
      PublishedDataSetType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateOperationalByMethod_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(18894));

  public static final NodeId
      PublishedDataSetType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateOperationalByMethod_TimeFirstChange =
          new NodeId(UShort.MIN, uint(18895));

  public static final NodeId
      PublishedDataSetType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateOperationalByParent =
          new NodeId(UShort.MIN, uint(18896));

  public static final NodeId
      PublishedDataSetType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateOperationalByParent_Active =
          new NodeId(UShort.MIN, uint(18897));

  public static final NodeId
      PublishedDataSetType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateOperationalByParent_Classification =
          new NodeId(UShort.MIN, uint(18898));

  public static final NodeId
      PublishedDataSetType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateOperationalByParent_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(18899));

  public static final NodeId
      PublishedDataSetType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateOperationalByParent_TimeFirstChange =
          new NodeId(UShort.MIN, uint(18900));

  public static final NodeId
      PublishedDataSetType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateOperationalFromError =
          new NodeId(UShort.MIN, uint(18901));

  public static final NodeId
      PublishedDataSetType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateOperationalFromError_Active =
          new NodeId(UShort.MIN, uint(18902));

  public static final NodeId
      PublishedDataSetType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateOperationalFromError_Classification =
          new NodeId(UShort.MIN, uint(18903));

  public static final NodeId
      PublishedDataSetType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateOperationalFromError_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(18904));

  public static final NodeId
      PublishedDataSetType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateOperationalFromError_TimeFirstChange =
          new NodeId(UShort.MIN, uint(18905));

  public static final NodeId
      PublishedDataSetType_DataSetWriterName_Placeholder_Diagnostics_Counters_StatePausedByParent =
          new NodeId(UShort.MIN, uint(18906));

  public static final NodeId
      PublishedDataSetType_DataSetWriterName_Placeholder_Diagnostics_Counters_StatePausedByParent_Active =
          new NodeId(UShort.MIN, uint(18907));

  public static final NodeId
      PublishedDataSetType_DataSetWriterName_Placeholder_Diagnostics_Counters_StatePausedByParent_Classification =
          new NodeId(UShort.MIN, uint(18908));

  public static final NodeId
      PublishedDataSetType_DataSetWriterName_Placeholder_Diagnostics_Counters_StatePausedByParent_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(18909));

  public static final NodeId
      PublishedDataSetType_DataSetWriterName_Placeholder_Diagnostics_Counters_StatePausedByParent_TimeFirstChange =
          new NodeId(UShort.MIN, uint(18910));

  public static final NodeId
      PublishedDataSetType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateDisabledByMethod =
          new NodeId(UShort.MIN, uint(18911));

  public static final NodeId
      PublishedDataSetType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateDisabledByMethod_Active =
          new NodeId(UShort.MIN, uint(18912));

  public static final NodeId
      PublishedDataSetType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateDisabledByMethod_Classification =
          new NodeId(UShort.MIN, uint(18913));

  public static final NodeId
      PublishedDataSetType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateDisabledByMethod_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(18914));

  public static final NodeId
      PublishedDataSetType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateDisabledByMethod_TimeFirstChange =
          new NodeId(UShort.MIN, uint(18915));

  public static final NodeId
      PublishedDataSetType_DataSetWriterName_Placeholder_Diagnostics_LiveValues =
          new NodeId(UShort.MIN, uint(18916));

  public static final NodeId
      PublishedDataSetType_DataSetWriterName_Placeholder_Diagnostics_Counters_FailedDataSetMessages =
          new NodeId(UShort.MIN, uint(18917));

  public static final NodeId
      PublishedDataSetType_DataSetWriterName_Placeholder_Diagnostics_Counters_FailedDataSetMessages_Active =
          new NodeId(UShort.MIN, uint(18918));

  public static final NodeId
      PublishedDataSetType_DataSetWriterName_Placeholder_Diagnostics_Counters_FailedDataSetMessages_Classification =
          new NodeId(UShort.MIN, uint(18919));

  public static final NodeId
      PublishedDataSetType_DataSetWriterName_Placeholder_Diagnostics_Counters_FailedDataSetMessages_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(18920));

  public static final NodeId
      PublishedDataSetType_DataSetWriterName_Placeholder_Diagnostics_Counters_FailedDataSetMessages_TimeFirstChange =
          new NodeId(UShort.MIN, uint(18921));

  public static final NodeId
      PublishedDataSetType_DataSetWriterName_Placeholder_Diagnostics_LiveValues_MessageSequenceNumber =
          new NodeId(UShort.MIN, uint(18922));

  public static final NodeId
      PublishedDataSetType_DataSetWriterName_Placeholder_Diagnostics_LiveValues_MessageSequenceNumber_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(18923));

  public static final NodeId
      PublishedDataSetType_DataSetWriterName_Placeholder_Diagnostics_LiveValues_StatusCode =
          new NodeId(UShort.MIN, uint(18924));

  public static final NodeId
      PublishedDataSetType_DataSetWriterName_Placeholder_Diagnostics_LiveValues_StatusCode_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(18925));

  public static final NodeId
      PublishedDataSetType_DataSetWriterName_Placeholder_Diagnostics_LiveValues_MajorVersion =
          new NodeId(UShort.MIN, uint(18926));

  public static final NodeId
      PublishedDataSetType_DataSetWriterName_Placeholder_Diagnostics_LiveValues_MajorVersion_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(18927));

  public static final NodeId
      PublishedDataSetType_DataSetWriterName_Placeholder_Diagnostics_LiveValues_MinorVersion =
          new NodeId(UShort.MIN, uint(18928));

  public static final NodeId
      PublishedDataSetType_DataSetWriterName_Placeholder_Diagnostics_LiveValues_MinorVersion_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(18929));

  public static final NodeId PublishedDataItemsType_DataSetWriterName_Placeholder_Diagnostics =
      new NodeId(UShort.MIN, uint(18930));

  public static final NodeId
      PublishedDataItemsType_DataSetWriterName_Placeholder_Diagnostics_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(18931));

  public static final NodeId
      PublishedDataItemsType_DataSetWriterName_Placeholder_Diagnostics_TotalInformation =
          new NodeId(UShort.MIN, uint(18932));

  public static final NodeId
      PublishedDataItemsType_DataSetWriterName_Placeholder_Diagnostics_TotalInformation_Active =
          new NodeId(UShort.MIN, uint(18933));

  public static final NodeId
      PublishedDataItemsType_DataSetWriterName_Placeholder_Diagnostics_TotalInformation_Classification =
          new NodeId(UShort.MIN, uint(18934));

  public static final NodeId
      PublishedDataItemsType_DataSetWriterName_Placeholder_Diagnostics_TotalInformation_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(18935));

  public static final NodeId
      PublishedDataItemsType_DataSetWriterName_Placeholder_Diagnostics_TotalInformation_TimeFirstChange =
          new NodeId(UShort.MIN, uint(18936));

  public static final NodeId
      PublishedDataItemsType_DataSetWriterName_Placeholder_Diagnostics_TotalError =
          new NodeId(UShort.MIN, uint(18937));

  public static final NodeId
      PublishedDataItemsType_DataSetWriterName_Placeholder_Diagnostics_TotalError_Active =
          new NodeId(UShort.MIN, uint(18938));

  public static final NodeId
      PublishedDataItemsType_DataSetWriterName_Placeholder_Diagnostics_TotalError_Classification =
          new NodeId(UShort.MIN, uint(18939));

  public static final NodeId
      PublishedDataItemsType_DataSetWriterName_Placeholder_Diagnostics_TotalError_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(18940));

  public static final NodeId
      PublishedDataItemsType_DataSetWriterName_Placeholder_Diagnostics_TotalError_TimeFirstChange =
          new NodeId(UShort.MIN, uint(18941));

  public static final NodeId
      PublishedDataItemsType_DataSetWriterName_Placeholder_Diagnostics_Reset =
          new NodeId(UShort.MIN, uint(18942));

  public static final NodeId
      PublishedDataItemsType_DataSetWriterName_Placeholder_Diagnostics_SubError =
          new NodeId(UShort.MIN, uint(18943));

  public static final NodeId
      PublishedDataItemsType_DataSetWriterName_Placeholder_Diagnostics_Counters =
          new NodeId(UShort.MIN, uint(18944));

  public static final NodeId
      PublishedDataItemsType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateError =
          new NodeId(UShort.MIN, uint(18945));

  public static final NodeId
      PublishedDataItemsType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateError_Active =
          new NodeId(UShort.MIN, uint(18946));

  public static final NodeId
      PublishedDataItemsType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateError_Classification =
          new NodeId(UShort.MIN, uint(18947));

  public static final NodeId
      PublishedDataItemsType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateError_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(18948));

  public static final NodeId
      PublishedDataItemsType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateError_TimeFirstChange =
          new NodeId(UShort.MIN, uint(18949));

  public static final NodeId
      PublishedDataItemsType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateOperationalByMethod =
          new NodeId(UShort.MIN, uint(18950));

  public static final NodeId
      PublishedDataItemsType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateOperationalByMethod_Active =
          new NodeId(UShort.MIN, uint(18951));

  public static final NodeId
      PublishedDataItemsType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateOperationalByMethod_Classification =
          new NodeId(UShort.MIN, uint(18952));

  public static final NodeId
      PublishedDataItemsType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateOperationalByMethod_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(18953));

  public static final NodeId
      PublishedDataItemsType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateOperationalByMethod_TimeFirstChange =
          new NodeId(UShort.MIN, uint(18954));

  public static final NodeId
      PublishedDataItemsType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateOperationalByParent =
          new NodeId(UShort.MIN, uint(18955));

  public static final NodeId
      PublishedDataItemsType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateOperationalByParent_Active =
          new NodeId(UShort.MIN, uint(18956));

  public static final NodeId
      PublishedDataItemsType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateOperationalByParent_Classification =
          new NodeId(UShort.MIN, uint(18957));

  public static final NodeId
      PublishedDataItemsType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateOperationalByParent_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(18958));

  public static final NodeId
      PublishedDataItemsType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateOperationalByParent_TimeFirstChange =
          new NodeId(UShort.MIN, uint(18959));

  public static final NodeId
      PublishedDataItemsType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateOperationalFromError =
          new NodeId(UShort.MIN, uint(18960));

  public static final NodeId
      PublishedDataItemsType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateOperationalFromError_Active =
          new NodeId(UShort.MIN, uint(18961));

  public static final NodeId
      PublishedDataItemsType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateOperationalFromError_Classification =
          new NodeId(UShort.MIN, uint(18962));

  public static final NodeId
      PublishedDataItemsType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateOperationalFromError_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(18963));

  public static final NodeId
      PublishedDataItemsType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateOperationalFromError_TimeFirstChange =
          new NodeId(UShort.MIN, uint(18964));

  public static final NodeId
      PublishedDataItemsType_DataSetWriterName_Placeholder_Diagnostics_Counters_StatePausedByParent =
          new NodeId(UShort.MIN, uint(18965));

  public static final NodeId
      PublishedDataItemsType_DataSetWriterName_Placeholder_Diagnostics_Counters_StatePausedByParent_Active =
          new NodeId(UShort.MIN, uint(18966));

  public static final NodeId
      PublishedDataItemsType_DataSetWriterName_Placeholder_Diagnostics_Counters_StatePausedByParent_Classification =
          new NodeId(UShort.MIN, uint(18967));

  public static final NodeId
      PublishedDataItemsType_DataSetWriterName_Placeholder_Diagnostics_Counters_StatePausedByParent_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(18968));

  public static final NodeId
      PublishedDataItemsType_DataSetWriterName_Placeholder_Diagnostics_Counters_StatePausedByParent_TimeFirstChange =
          new NodeId(UShort.MIN, uint(18969));

  public static final NodeId
      PublishedDataItemsType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateDisabledByMethod =
          new NodeId(UShort.MIN, uint(18970));

  public static final NodeId
      PublishedDataItemsType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateDisabledByMethod_Active =
          new NodeId(UShort.MIN, uint(18971));

  public static final NodeId
      PublishedDataItemsType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateDisabledByMethod_Classification =
          new NodeId(UShort.MIN, uint(18972));

  public static final NodeId
      PublishedDataItemsType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateDisabledByMethod_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(18973));

  public static final NodeId
      PublishedDataItemsType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateDisabledByMethod_TimeFirstChange =
          new NodeId(UShort.MIN, uint(18974));

  public static final NodeId
      PublishedDataItemsType_DataSetWriterName_Placeholder_Diagnostics_LiveValues =
          new NodeId(UShort.MIN, uint(18975));

  public static final NodeId
      PublishedDataItemsType_DataSetWriterName_Placeholder_Diagnostics_Counters_FailedDataSetMessages =
          new NodeId(UShort.MIN, uint(18976));

  public static final NodeId
      PublishedDataItemsType_DataSetWriterName_Placeholder_Diagnostics_Counters_FailedDataSetMessages_Active =
          new NodeId(UShort.MIN, uint(18977));

  public static final NodeId
      PublishedDataItemsType_DataSetWriterName_Placeholder_Diagnostics_Counters_FailedDataSetMessages_Classification =
          new NodeId(UShort.MIN, uint(18978));

  public static final NodeId
      PublishedDataItemsType_DataSetWriterName_Placeholder_Diagnostics_Counters_FailedDataSetMessages_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(18979));

  public static final NodeId
      PublishedDataItemsType_DataSetWriterName_Placeholder_Diagnostics_Counters_FailedDataSetMessages_TimeFirstChange =
          new NodeId(UShort.MIN, uint(18980));

  public static final NodeId
      PublishedDataItemsType_DataSetWriterName_Placeholder_Diagnostics_LiveValues_MessageSequenceNumber =
          new NodeId(UShort.MIN, uint(18981));

  public static final NodeId
      PublishedDataItemsType_DataSetWriterName_Placeholder_Diagnostics_LiveValues_MessageSequenceNumber_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(18982));

  public static final NodeId
      PublishedDataItemsType_DataSetWriterName_Placeholder_Diagnostics_LiveValues_StatusCode =
          new NodeId(UShort.MIN, uint(18983));

  public static final NodeId
      PublishedDataItemsType_DataSetWriterName_Placeholder_Diagnostics_LiveValues_StatusCode_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(18984));

  public static final NodeId
      PublishedDataItemsType_DataSetWriterName_Placeholder_Diagnostics_LiveValues_MajorVersion =
          new NodeId(UShort.MIN, uint(18985));

  public static final NodeId
      PublishedDataItemsType_DataSetWriterName_Placeholder_Diagnostics_LiveValues_MajorVersion_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(18986));

  public static final NodeId
      PublishedDataItemsType_DataSetWriterName_Placeholder_Diagnostics_LiveValues_MinorVersion =
          new NodeId(UShort.MIN, uint(18987));

  public static final NodeId
      PublishedDataItemsType_DataSetWriterName_Placeholder_Diagnostics_LiveValues_MinorVersion_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(18988));

  public static final NodeId PublishedEventsType_DataSetWriterName_Placeholder_Diagnostics =
      new NodeId(UShort.MIN, uint(18989));

  public static final NodeId
      PublishedEventsType_DataSetWriterName_Placeholder_Diagnostics_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(18990));

  public static final NodeId
      PublishedEventsType_DataSetWriterName_Placeholder_Diagnostics_TotalInformation =
          new NodeId(UShort.MIN, uint(18991));

  public static final NodeId
      PublishedEventsType_DataSetWriterName_Placeholder_Diagnostics_TotalInformation_Active =
          new NodeId(UShort.MIN, uint(18992));

  public static final NodeId
      PublishedEventsType_DataSetWriterName_Placeholder_Diagnostics_TotalInformation_Classification =
          new NodeId(UShort.MIN, uint(18993));

  public static final NodeId
      PublishedEventsType_DataSetWriterName_Placeholder_Diagnostics_TotalInformation_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(18994));

  public static final NodeId
      PublishedEventsType_DataSetWriterName_Placeholder_Diagnostics_TotalInformation_TimeFirstChange =
          new NodeId(UShort.MIN, uint(18995));

  public static final NodeId
      PublishedEventsType_DataSetWriterName_Placeholder_Diagnostics_TotalError =
          new NodeId(UShort.MIN, uint(18996));

  public static final NodeId
      PublishedEventsType_DataSetWriterName_Placeholder_Diagnostics_TotalError_Active =
          new NodeId(UShort.MIN, uint(18997));

  public static final NodeId
      PublishedEventsType_DataSetWriterName_Placeholder_Diagnostics_TotalError_Classification =
          new NodeId(UShort.MIN, uint(18998));

  public static final NodeId
      PublishedEventsType_DataSetWriterName_Placeholder_Diagnostics_TotalError_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(18999));

  public static final NodeId
      PublishedEventsType_DataSetWriterName_Placeholder_Diagnostics_TotalError_TimeFirstChange =
          new NodeId(UShort.MIN, uint(19000));

  public static final NodeId PublishedEventsType_DataSetWriterName_Placeholder_Diagnostics_Reset =
      new NodeId(UShort.MIN, uint(19001));

  public static final NodeId
      PublishedEventsType_DataSetWriterName_Placeholder_Diagnostics_SubError =
          new NodeId(UShort.MIN, uint(19002));

  public static final NodeId
      PublishedEventsType_DataSetWriterName_Placeholder_Diagnostics_Counters =
          new NodeId(UShort.MIN, uint(19003));

  public static final NodeId
      PublishedEventsType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateError =
          new NodeId(UShort.MIN, uint(19004));

  public static final NodeId
      PublishedEventsType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateError_Active =
          new NodeId(UShort.MIN, uint(19005));

  public static final NodeId
      PublishedEventsType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateError_Classification =
          new NodeId(UShort.MIN, uint(19006));

  public static final NodeId
      PublishedEventsType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateError_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19007));

  public static final NodeId
      PublishedEventsType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateError_TimeFirstChange =
          new NodeId(UShort.MIN, uint(19008));

  public static final NodeId
      PublishedEventsType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateOperationalByMethod =
          new NodeId(UShort.MIN, uint(19009));

  public static final NodeId
      PublishedEventsType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateOperationalByMethod_Active =
          new NodeId(UShort.MIN, uint(19010));

  public static final NodeId
      PublishedEventsType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateOperationalByMethod_Classification =
          new NodeId(UShort.MIN, uint(19011));

  public static final NodeId
      PublishedEventsType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateOperationalByMethod_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19012));

  public static final NodeId
      PublishedEventsType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateOperationalByMethod_TimeFirstChange =
          new NodeId(UShort.MIN, uint(19013));

  public static final NodeId
      PublishedEventsType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateOperationalByParent =
          new NodeId(UShort.MIN, uint(19014));

  public static final NodeId
      PublishedEventsType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateOperationalByParent_Active =
          new NodeId(UShort.MIN, uint(19015));

  public static final NodeId
      PublishedEventsType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateOperationalByParent_Classification =
          new NodeId(UShort.MIN, uint(19016));

  public static final NodeId
      PublishedEventsType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateOperationalByParent_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19017));

  public static final NodeId
      PublishedEventsType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateOperationalByParent_TimeFirstChange =
          new NodeId(UShort.MIN, uint(19018));

  public static final NodeId
      PublishedEventsType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateOperationalFromError =
          new NodeId(UShort.MIN, uint(19019));

  public static final NodeId
      PublishedEventsType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateOperationalFromError_Active =
          new NodeId(UShort.MIN, uint(19020));

  public static final NodeId
      PublishedEventsType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateOperationalFromError_Classification =
          new NodeId(UShort.MIN, uint(19021));

  public static final NodeId
      PublishedEventsType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateOperationalFromError_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19022));

  public static final NodeId
      PublishedEventsType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateOperationalFromError_TimeFirstChange =
          new NodeId(UShort.MIN, uint(19023));

  public static final NodeId
      PublishedEventsType_DataSetWriterName_Placeholder_Diagnostics_Counters_StatePausedByParent =
          new NodeId(UShort.MIN, uint(19024));

  public static final NodeId
      PublishedEventsType_DataSetWriterName_Placeholder_Diagnostics_Counters_StatePausedByParent_Active =
          new NodeId(UShort.MIN, uint(19025));

  public static final NodeId
      PublishedEventsType_DataSetWriterName_Placeholder_Diagnostics_Counters_StatePausedByParent_Classification =
          new NodeId(UShort.MIN, uint(19026));

  public static final NodeId
      PublishedEventsType_DataSetWriterName_Placeholder_Diagnostics_Counters_StatePausedByParent_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19027));

  public static final NodeId
      PublishedEventsType_DataSetWriterName_Placeholder_Diagnostics_Counters_StatePausedByParent_TimeFirstChange =
          new NodeId(UShort.MIN, uint(19028));

  public static final NodeId
      PublishedEventsType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateDisabledByMethod =
          new NodeId(UShort.MIN, uint(19029));

  public static final NodeId
      PublishedEventsType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateDisabledByMethod_Active =
          new NodeId(UShort.MIN, uint(19030));

  public static final NodeId
      PublishedEventsType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateDisabledByMethod_Classification =
          new NodeId(UShort.MIN, uint(19031));

  public static final NodeId
      PublishedEventsType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateDisabledByMethod_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19032));

  public static final NodeId
      PublishedEventsType_DataSetWriterName_Placeholder_Diagnostics_Counters_StateDisabledByMethod_TimeFirstChange =
          new NodeId(UShort.MIN, uint(19033));

  public static final NodeId
      PublishedEventsType_DataSetWriterName_Placeholder_Diagnostics_LiveValues =
          new NodeId(UShort.MIN, uint(19034));

  public static final NodeId
      PublishedEventsType_DataSetWriterName_Placeholder_Diagnostics_Counters_FailedDataSetMessages =
          new NodeId(UShort.MIN, uint(19035));

  public static final NodeId
      PublishedEventsType_DataSetWriterName_Placeholder_Diagnostics_Counters_FailedDataSetMessages_Active =
          new NodeId(UShort.MIN, uint(19036));

  public static final NodeId
      PublishedEventsType_DataSetWriterName_Placeholder_Diagnostics_Counters_FailedDataSetMessages_Classification =
          new NodeId(UShort.MIN, uint(19037));

  public static final NodeId
      PublishedEventsType_DataSetWriterName_Placeholder_Diagnostics_Counters_FailedDataSetMessages_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19038));

  public static final NodeId
      PublishedEventsType_DataSetWriterName_Placeholder_Diagnostics_Counters_FailedDataSetMessages_TimeFirstChange =
          new NodeId(UShort.MIN, uint(19039));

  public static final NodeId
      PublishedEventsType_DataSetWriterName_Placeholder_Diagnostics_LiveValues_MessageSequenceNumber =
          new NodeId(UShort.MIN, uint(19040));

  public static final NodeId
      PublishedEventsType_DataSetWriterName_Placeholder_Diagnostics_LiveValues_MessageSequenceNumber_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19041));

  public static final NodeId
      PublishedEventsType_DataSetWriterName_Placeholder_Diagnostics_LiveValues_StatusCode =
          new NodeId(UShort.MIN, uint(19042));

  public static final NodeId
      PublishedEventsType_DataSetWriterName_Placeholder_Diagnostics_LiveValues_StatusCode_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19043));

  public static final NodeId
      PublishedEventsType_DataSetWriterName_Placeholder_Diagnostics_LiveValues_MajorVersion =
          new NodeId(UShort.MIN, uint(19044));

  public static final NodeId
      PublishedEventsType_DataSetWriterName_Placeholder_Diagnostics_LiveValues_MajorVersion_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19045));

  public static final NodeId
      PublishedEventsType_DataSetWriterName_Placeholder_Diagnostics_LiveValues_MinorVersion =
          new NodeId(UShort.MIN, uint(19046));

  public static final NodeId
      PublishedEventsType_DataSetWriterName_Placeholder_Diagnostics_LiveValues_MinorVersion_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19047));

  public static final NodeId OpcUa_XmlSchema_CartesianCoordinates_DictionaryFragment =
      new NodeId(UShort.MIN, uint(19048));

  public static final NodeId OpcUa_XmlSchema_ThreeDCartesianCoordinates =
      new NodeId(UShort.MIN, uint(19049));

  public static final NodeId OpcUa_XmlSchema_ThreeDCartesianCoordinates_DataTypeVersion =
      new NodeId(UShort.MIN, uint(19050));

  public static final NodeId OpcUa_XmlSchema_ThreeDCartesianCoordinates_DictionaryFragment =
      new NodeId(UShort.MIN, uint(19051));

  public static final NodeId OpcUa_XmlSchema_Orientation = new NodeId(UShort.MIN, uint(19052));

  public static final NodeId OpcUa_XmlSchema_Orientation_DataTypeVersion =
      new NodeId(UShort.MIN, uint(19053));

  public static final NodeId OpcUa_XmlSchema_Orientation_DictionaryFragment =
      new NodeId(UShort.MIN, uint(19054));

  public static final NodeId OpcUa_XmlSchema_ThreeDOrientation =
      new NodeId(UShort.MIN, uint(19055));

  public static final NodeId OpcUa_XmlSchema_ThreeDOrientation_DataTypeVersion =
      new NodeId(UShort.MIN, uint(19056));

  public static final NodeId OpcUa_XmlSchema_ThreeDOrientation_DictionaryFragment =
      new NodeId(UShort.MIN, uint(19057));

  public static final NodeId OpcUa_XmlSchema_Frame = new NodeId(UShort.MIN, uint(19058));

  public static final NodeId OpcUa_XmlSchema_Frame_DataTypeVersion =
      new NodeId(UShort.MIN, uint(19059));

  public static final NodeId OpcUa_XmlSchema_Frame_DictionaryFragment =
      new NodeId(UShort.MIN, uint(19060));

  public static final NodeId OpcUa_XmlSchema_ThreeDFrame = new NodeId(UShort.MIN, uint(19061));

  public static final NodeId OpcUa_XmlSchema_ThreeDFrame_DataTypeVersion =
      new NodeId(UShort.MIN, uint(19062));

  public static final NodeId OpcUa_XmlSchema_ThreeDFrame_DictionaryFragment =
      new NodeId(UShort.MIN, uint(19063));

  public static final NodeId RationalNumber_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(19064));

  public static final NodeId Vector_Encoding_DefaultJson = new NodeId(UShort.MIN, uint(19065));

  public static final NodeId ThreeDVector_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(19066));

  public static final NodeId CartesianCoordinates_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(19067));

  public static final NodeId ThreeDCartesianCoordinates_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(19068));

  public static final NodeId Orientation_Encoding_DefaultJson = new NodeId(UShort.MIN, uint(19069));

  public static final NodeId ThreeDOrientation_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(19070));

  public static final NodeId Frame_Encoding_DefaultJson = new NodeId(UShort.MIN, uint(19071));

  public static final NodeId ThreeDFrame_Encoding_DefaultJson = new NodeId(UShort.MIN, uint(19072));

  public static final NodeId ThreeDFrameType_Orientation_AngleUnit =
      new NodeId(UShort.MIN, uint(19073));

  public static final NodeId ThreeDFrameType_Orientation_A = new NodeId(UShort.MIN, uint(19074));

  public static final NodeId ThreeDFrameType_Orientation_B = new NodeId(UShort.MIN, uint(19075));

  public static final NodeId ThreeDFrameType_Orientation_C = new NodeId(UShort.MIN, uint(19076));

  public static final NodeId MultiStateDictionaryEntryDiscreteBaseType =
      new NodeId(UShort.MIN, uint(19077));

  public static final NodeId MultiStateDictionaryEntryDiscreteBaseType_Definition =
      new NodeId(UShort.MIN, uint(19078));

  public static final NodeId MultiStateDictionaryEntryDiscreteBaseType_ValuePrecision =
      new NodeId(UShort.MIN, uint(19079));

  public static final NodeId MultiStateDictionaryEntryDiscreteBaseType_EnumValues =
      new NodeId(UShort.MIN, uint(19080));

  public static final NodeId MultiStateDictionaryEntryDiscreteBaseType_ValueAsText =
      new NodeId(UShort.MIN, uint(19081));

  public static final NodeId MultiStateDictionaryEntryDiscreteBaseType_EnumDictionaryEntries =
      new NodeId(UShort.MIN, uint(19082));

  public static final NodeId MultiStateDictionaryEntryDiscreteBaseType_ValueAsDictionaryEntries =
      new NodeId(UShort.MIN, uint(19083));

  public static final NodeId MultiStateDictionaryEntryDiscreteType =
      new NodeId(UShort.MIN, uint(19084));

  public static final NodeId MultiStateDictionaryEntryDiscreteType_Definition =
      new NodeId(UShort.MIN, uint(19085));

  public static final NodeId MultiStateDictionaryEntryDiscreteType_ValuePrecision =
      new NodeId(UShort.MIN, uint(19086));

  public static final NodeId MultiStateDictionaryEntryDiscreteType_EnumValues =
      new NodeId(UShort.MIN, uint(19087));

  public static final NodeId MultiStateDictionaryEntryDiscreteType_ValueAsText =
      new NodeId(UShort.MIN, uint(19088));

  public static final NodeId MultiStateDictionaryEntryDiscreteType_EnumDictionaryEntries =
      new NodeId(UShort.MIN, uint(19089));

  public static final NodeId MultiStateDictionaryEntryDiscreteType_ValueAsDictionaryEntries =
      new NodeId(UShort.MIN, uint(19090));

  public static final NodeId HistoryServerCapabilities_ServerTimestampSupported =
      new NodeId(UShort.MIN, uint(19091));

  public static final NodeId HistoricalDataConfigurationType_ServerTimestampSupported =
      new NodeId(UShort.MIN, uint(19092));

  public static final NodeId HAConfiguration_ServerTimestampSupported =
      new NodeId(UShort.MIN, uint(19093));

  public static final NodeId HistoryServerCapabilitiesType_ServerTimestampSupported =
      new NodeId(UShort.MIN, uint(19094));

  public static final NodeId AuditHistoryAnnotationUpdateEventType =
      new NodeId(UShort.MIN, uint(19095));

  public static final NodeId AuditHistoryAnnotationUpdateEventType_EventId =
      new NodeId(UShort.MIN, uint(19096));

  public static final NodeId AuditHistoryAnnotationUpdateEventType_EventType =
      new NodeId(UShort.MIN, uint(19097));

  public static final NodeId AuditHistoryAnnotationUpdateEventType_SourceNode =
      new NodeId(UShort.MIN, uint(19098));

  public static final NodeId AuditHistoryAnnotationUpdateEventType_SourceName =
      new NodeId(UShort.MIN, uint(19099));

  public static final NodeId AuditHistoryAnnotationUpdateEventType_Time =
      new NodeId(UShort.MIN, uint(19100));

  public static final NodeId AuditHistoryAnnotationUpdateEventType_ReceiveTime =
      new NodeId(UShort.MIN, uint(19101));

  public static final NodeId AuditHistoryAnnotationUpdateEventType_LocalTime =
      new NodeId(UShort.MIN, uint(19102));

  public static final NodeId AuditHistoryAnnotationUpdateEventType_Message =
      new NodeId(UShort.MIN, uint(19103));

  public static final NodeId AuditHistoryAnnotationUpdateEventType_Severity =
      new NodeId(UShort.MIN, uint(19104));

  public static final NodeId AuditHistoryAnnotationUpdateEventType_ActionTimeStamp =
      new NodeId(UShort.MIN, uint(19105));

  public static final NodeId AuditHistoryAnnotationUpdateEventType_Status =
      new NodeId(UShort.MIN, uint(19106));

  public static final NodeId PubSubConnectionType_WriterGroupName_Placeholder_Diagnostics =
      new NodeId(UShort.MIN, uint(19107));

  public static final NodeId
      PubSubConnectionType_WriterGroupName_Placeholder_Diagnostics_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19108));

  public static final NodeId
      PubSubConnectionType_WriterGroupName_Placeholder_Diagnostics_TotalInformation =
          new NodeId(UShort.MIN, uint(19109));

  public static final NodeId
      PubSubConnectionType_WriterGroupName_Placeholder_Diagnostics_TotalInformation_Active =
          new NodeId(UShort.MIN, uint(19110));

  public static final NodeId
      PubSubConnectionType_WriterGroupName_Placeholder_Diagnostics_TotalInformation_Classification =
          new NodeId(UShort.MIN, uint(19111));

  public static final NodeId
      PubSubConnectionType_WriterGroupName_Placeholder_Diagnostics_TotalInformation_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19112));

  public static final NodeId
      PubSubConnectionType_WriterGroupName_Placeholder_Diagnostics_TotalInformation_TimeFirstChange =
          new NodeId(UShort.MIN, uint(19113));

  public static final NodeId
      PubSubConnectionType_WriterGroupName_Placeholder_Diagnostics_TotalError =
          new NodeId(UShort.MIN, uint(19114));

  public static final NodeId
      PubSubConnectionType_WriterGroupName_Placeholder_Diagnostics_TotalError_Active =
          new NodeId(UShort.MIN, uint(19115));

  public static final NodeId
      PubSubConnectionType_WriterGroupName_Placeholder_Diagnostics_TotalError_Classification =
          new NodeId(UShort.MIN, uint(19116));

  public static final NodeId
      PubSubConnectionType_WriterGroupName_Placeholder_Diagnostics_TotalError_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19117));

  public static final NodeId
      PubSubConnectionType_WriterGroupName_Placeholder_Diagnostics_TotalError_TimeFirstChange =
          new NodeId(UShort.MIN, uint(19118));

  public static final NodeId PubSubConnectionType_WriterGroupName_Placeholder_Diagnostics_Reset =
      new NodeId(UShort.MIN, uint(19119));

  public static final NodeId PubSubConnectionType_WriterGroupName_Placeholder_Diagnostics_SubError =
      new NodeId(UShort.MIN, uint(19120));

  public static final NodeId PubSubConnectionType_WriterGroupName_Placeholder_Diagnostics_Counters =
      new NodeId(UShort.MIN, uint(19121));

  public static final NodeId
      PubSubConnectionType_WriterGroupName_Placeholder_Diagnostics_Counters_StateError =
          new NodeId(UShort.MIN, uint(19122));

  public static final NodeId
      PubSubConnectionType_WriterGroupName_Placeholder_Diagnostics_Counters_StateError_Active =
          new NodeId(UShort.MIN, uint(19123));

  public static final NodeId
      PubSubConnectionType_WriterGroupName_Placeholder_Diagnostics_Counters_StateError_Classification =
          new NodeId(UShort.MIN, uint(19124));

  public static final NodeId
      PubSubConnectionType_WriterGroupName_Placeholder_Diagnostics_Counters_StateError_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19125));

  public static final NodeId
      PubSubConnectionType_WriterGroupName_Placeholder_Diagnostics_Counters_StateError_TimeFirstChange =
          new NodeId(UShort.MIN, uint(19126));

  public static final NodeId
      PubSubConnectionType_WriterGroupName_Placeholder_Diagnostics_Counters_StateOperationalByMethod =
          new NodeId(UShort.MIN, uint(19127));

  public static final NodeId
      PubSubConnectionType_WriterGroupName_Placeholder_Diagnostics_Counters_StateOperationalByMethod_Active =
          new NodeId(UShort.MIN, uint(19128));

  public static final NodeId
      PubSubConnectionType_WriterGroupName_Placeholder_Diagnostics_Counters_StateOperationalByMethod_Classification =
          new NodeId(UShort.MIN, uint(19129));

  public static final NodeId
      PubSubConnectionType_WriterGroupName_Placeholder_Diagnostics_Counters_StateOperationalByMethod_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19130));

  public static final NodeId
      PubSubConnectionType_WriterGroupName_Placeholder_Diagnostics_Counters_StateOperationalByMethod_TimeFirstChange =
          new NodeId(UShort.MIN, uint(19131));

  public static final NodeId
      PubSubConnectionType_WriterGroupName_Placeholder_Diagnostics_Counters_StateOperationalByParent =
          new NodeId(UShort.MIN, uint(19132));

  public static final NodeId
      PubSubConnectionType_WriterGroupName_Placeholder_Diagnostics_Counters_StateOperationalByParent_Active =
          new NodeId(UShort.MIN, uint(19133));

  public static final NodeId
      PubSubConnectionType_WriterGroupName_Placeholder_Diagnostics_Counters_StateOperationalByParent_Classification =
          new NodeId(UShort.MIN, uint(19134));

  public static final NodeId
      PubSubConnectionType_WriterGroupName_Placeholder_Diagnostics_Counters_StateOperationalByParent_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19135));

  public static final NodeId
      PubSubConnectionType_WriterGroupName_Placeholder_Diagnostics_Counters_StateOperationalByParent_TimeFirstChange =
          new NodeId(UShort.MIN, uint(19136));

  public static final NodeId
      PubSubConnectionType_WriterGroupName_Placeholder_Diagnostics_Counters_StateOperationalFromError =
          new NodeId(UShort.MIN, uint(19137));

  public static final NodeId
      PubSubConnectionType_WriterGroupName_Placeholder_Diagnostics_Counters_StateOperationalFromError_Active =
          new NodeId(UShort.MIN, uint(19138));

  public static final NodeId
      PubSubConnectionType_WriterGroupName_Placeholder_Diagnostics_Counters_StateOperationalFromError_Classification =
          new NodeId(UShort.MIN, uint(19139));

  public static final NodeId
      PubSubConnectionType_WriterGroupName_Placeholder_Diagnostics_Counters_StateOperationalFromError_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19140));

  public static final NodeId
      PubSubConnectionType_WriterGroupName_Placeholder_Diagnostics_Counters_StateOperationalFromError_TimeFirstChange =
          new NodeId(UShort.MIN, uint(19141));

  public static final NodeId
      PubSubConnectionType_WriterGroupName_Placeholder_Diagnostics_Counters_StatePausedByParent =
          new NodeId(UShort.MIN, uint(19142));

  public static final NodeId
      PubSubConnectionType_WriterGroupName_Placeholder_Diagnostics_Counters_StatePausedByParent_Active =
          new NodeId(UShort.MIN, uint(19143));

  public static final NodeId
      PubSubConnectionType_WriterGroupName_Placeholder_Diagnostics_Counters_StatePausedByParent_Classification =
          new NodeId(UShort.MIN, uint(19144));

  public static final NodeId
      PubSubConnectionType_WriterGroupName_Placeholder_Diagnostics_Counters_StatePausedByParent_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19145));

  public static final NodeId
      PubSubConnectionType_WriterGroupName_Placeholder_Diagnostics_Counters_StatePausedByParent_TimeFirstChange =
          new NodeId(UShort.MIN, uint(19146));

  public static final NodeId
      PubSubConnectionType_WriterGroupName_Placeholder_Diagnostics_Counters_StateDisabledByMethod =
          new NodeId(UShort.MIN, uint(19147));

  public static final NodeId
      PubSubConnectionType_WriterGroupName_Placeholder_Diagnostics_Counters_StateDisabledByMethod_Active =
          new NodeId(UShort.MIN, uint(19148));

  public static final NodeId
      PubSubConnectionType_WriterGroupName_Placeholder_Diagnostics_Counters_StateDisabledByMethod_Classification =
          new NodeId(UShort.MIN, uint(19149));

  public static final NodeId
      PubSubConnectionType_WriterGroupName_Placeholder_Diagnostics_Counters_StateDisabledByMethod_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19150));

  public static final NodeId
      PubSubConnectionType_WriterGroupName_Placeholder_Diagnostics_Counters_StateDisabledByMethod_TimeFirstChange =
          new NodeId(UShort.MIN, uint(19151));

  public static final NodeId
      PubSubConnectionType_WriterGroupName_Placeholder_Diagnostics_LiveValues =
          new NodeId(UShort.MIN, uint(19152));

  public static final NodeId
      PubSubConnectionType_WriterGroupName_Placeholder_Diagnostics_Counters_SentNetworkMessages =
          new NodeId(UShort.MIN, uint(19153));

  public static final NodeId
      PubSubConnectionType_WriterGroupName_Placeholder_Diagnostics_Counters_SentNetworkMessages_Active =
          new NodeId(UShort.MIN, uint(19154));

  public static final NodeId
      PubSubConnectionType_WriterGroupName_Placeholder_Diagnostics_Counters_SentNetworkMessages_Classification =
          new NodeId(UShort.MIN, uint(19155));

  public static final NodeId
      PubSubConnectionType_WriterGroupName_Placeholder_Diagnostics_Counters_SentNetworkMessages_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19156));

  public static final NodeId
      PubSubConnectionType_WriterGroupName_Placeholder_Diagnostics_Counters_SentNetworkMessages_TimeFirstChange =
          new NodeId(UShort.MIN, uint(19157));

  public static final NodeId
      PubSubConnectionType_WriterGroupName_Placeholder_Diagnostics_Counters_FailedTransmissions =
          new NodeId(UShort.MIN, uint(19158));

  public static final NodeId
      PubSubConnectionType_WriterGroupName_Placeholder_Diagnostics_Counters_FailedTransmissions_Active =
          new NodeId(UShort.MIN, uint(19159));

  public static final NodeId
      PubSubConnectionType_WriterGroupName_Placeholder_Diagnostics_Counters_FailedTransmissions_Classification =
          new NodeId(UShort.MIN, uint(19160));

  public static final NodeId
      PubSubConnectionType_WriterGroupName_Placeholder_Diagnostics_Counters_FailedTransmissions_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19161));

  public static final NodeId
      PubSubConnectionType_WriterGroupName_Placeholder_Diagnostics_Counters_FailedTransmissions_TimeFirstChange =
          new NodeId(UShort.MIN, uint(19162));

  public static final NodeId
      PubSubConnectionType_WriterGroupName_Placeholder_Diagnostics_Counters_EncryptionErrors =
          new NodeId(UShort.MIN, uint(19163));

  public static final NodeId
      PubSubConnectionType_WriterGroupName_Placeholder_Diagnostics_Counters_EncryptionErrors_Active =
          new NodeId(UShort.MIN, uint(19164));

  public static final NodeId
      PubSubConnectionType_WriterGroupName_Placeholder_Diagnostics_Counters_EncryptionErrors_Classification =
          new NodeId(UShort.MIN, uint(19165));

  public static final NodeId
      PubSubConnectionType_WriterGroupName_Placeholder_Diagnostics_Counters_EncryptionErrors_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19166));

  public static final NodeId
      PubSubConnectionType_WriterGroupName_Placeholder_Diagnostics_Counters_EncryptionErrors_TimeFirstChange =
          new NodeId(UShort.MIN, uint(19167));

  public static final NodeId
      PubSubConnectionType_WriterGroupName_Placeholder_Diagnostics_LiveValues_ConfiguredDataSetWriters =
          new NodeId(UShort.MIN, uint(19168));

  public static final NodeId
      PubSubConnectionType_WriterGroupName_Placeholder_Diagnostics_LiveValues_ConfiguredDataSetWriters_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19169));

  public static final NodeId
      PubSubConnectionType_WriterGroupName_Placeholder_Diagnostics_LiveValues_OperationalDataSetWriters =
          new NodeId(UShort.MIN, uint(19170));

  public static final NodeId
      PubSubConnectionType_WriterGroupName_Placeholder_Diagnostics_LiveValues_OperationalDataSetWriters_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19171));

  public static final NodeId
      PubSubConnectionType_WriterGroupName_Placeholder_Diagnostics_LiveValues_SecurityTokenID =
          new NodeId(UShort.MIN, uint(19172));

  public static final NodeId
      PubSubConnectionType_WriterGroupName_Placeholder_Diagnostics_LiveValues_SecurityTokenID_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19173));

  public static final NodeId
      PubSubConnectionType_WriterGroupName_Placeholder_Diagnostics_LiveValues_TimeToNextTokenID =
          new NodeId(UShort.MIN, uint(19174));

  public static final NodeId
      PubSubConnectionType_WriterGroupName_Placeholder_Diagnostics_LiveValues_TimeToNextTokenID_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19175));

  public static final NodeId PubSubConnectionType_ReaderGroupName_Placeholder_Diagnostics =
      new NodeId(UShort.MIN, uint(19176));

  public static final NodeId
      PubSubConnectionType_ReaderGroupName_Placeholder_Diagnostics_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19177));

  public static final NodeId
      PubSubConnectionType_ReaderGroupName_Placeholder_Diagnostics_TotalInformation =
          new NodeId(UShort.MIN, uint(19178));

  public static final NodeId
      PubSubConnectionType_ReaderGroupName_Placeholder_Diagnostics_TotalInformation_Active =
          new NodeId(UShort.MIN, uint(19179));

  public static final NodeId
      PubSubConnectionType_ReaderGroupName_Placeholder_Diagnostics_TotalInformation_Classification =
          new NodeId(UShort.MIN, uint(19180));

  public static final NodeId
      PubSubConnectionType_ReaderGroupName_Placeholder_Diagnostics_TotalInformation_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19181));

  public static final NodeId
      PubSubConnectionType_ReaderGroupName_Placeholder_Diagnostics_TotalInformation_TimeFirstChange =
          new NodeId(UShort.MIN, uint(19182));

  public static final NodeId
      PubSubConnectionType_ReaderGroupName_Placeholder_Diagnostics_TotalError =
          new NodeId(UShort.MIN, uint(19183));

  public static final NodeId
      PubSubConnectionType_ReaderGroupName_Placeholder_Diagnostics_TotalError_Active =
          new NodeId(UShort.MIN, uint(19184));

  public static final NodeId
      PubSubConnectionType_ReaderGroupName_Placeholder_Diagnostics_TotalError_Classification =
          new NodeId(UShort.MIN, uint(19185));

  public static final NodeId
      PubSubConnectionType_ReaderGroupName_Placeholder_Diagnostics_TotalError_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19186));

  public static final NodeId
      PubSubConnectionType_ReaderGroupName_Placeholder_Diagnostics_TotalError_TimeFirstChange =
          new NodeId(UShort.MIN, uint(19187));
}

/*
 *  Copyright (c) 2025 the Eclipse Milo Authors
 *
 *  This program and the accompanying materials are made
 *  available under the terms of the Eclipse Public License 2.0
 *  which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 *  SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;

abstract class NodeIds5 {
  public static final NodeId WellKnownRole_SecurityKeyServerAccess_Applications =
      new NodeId(UShort.MIN, uint(25606));

  public static final NodeId WellKnownRole_SecurityKeyServerAccess_EndpointsExclude =
      new NodeId(UShort.MIN, uint(25607));

  public static final NodeId WellKnownRole_SecurityKeyServerAccess_Endpoints =
      new NodeId(UShort.MIN, uint(25608));

  public static final NodeId WellKnownRole_SecurityKeyServerAccess_CustomConfiguration =
      new NodeId(UShort.MIN, uint(25609));

  public static final NodeId WellKnownRole_SecurityKeyServerAccess_AddIdentity =
      new NodeId(UShort.MIN, uint(25610));

  public static final NodeId WellKnownRole_SecurityKeyServerAccess_AddIdentity_InputArguments =
      new NodeId(UShort.MIN, uint(25611));

  public static final NodeId WellKnownRole_SecurityKeyServerAccess_RemoveIdentity =
      new NodeId(UShort.MIN, uint(25612));

  public static final NodeId WellKnownRole_SecurityKeyServerAccess_RemoveIdentity_InputArguments =
      new NodeId(UShort.MIN, uint(25613));

  public static final NodeId WellKnownRole_SecurityKeyServerAccess_AddApplication =
      new NodeId(UShort.MIN, uint(25614));

  public static final NodeId WellKnownRole_SecurityKeyServerAccess_AddApplication_InputArguments =
      new NodeId(UShort.MIN, uint(25615));

  public static final NodeId WellKnownRole_SecurityKeyServerAccess_RemoveApplication =
      new NodeId(UShort.MIN, uint(25616));

  public static final NodeId
      WellKnownRole_SecurityKeyServerAccess_RemoveApplication_InputArguments =
          new NodeId(UShort.MIN, uint(25617));

  public static final NodeId WellKnownRole_SecurityKeyServerAccess_AddEndpoint =
      new NodeId(UShort.MIN, uint(25618));

  public static final NodeId WellKnownRole_SecurityKeyServerAccess_AddEndpoint_InputArguments =
      new NodeId(UShort.MIN, uint(25619));

  public static final NodeId WellKnownRole_SecurityKeyServerAccess_RemoveEndpoint =
      new NodeId(UShort.MIN, uint(25620));

  public static final NodeId WellKnownRole_SecurityKeyServerAccess_RemoveEndpoint_InputArguments =
      new NodeId(UShort.MIN, uint(25621));

  public static final NodeId SecurityGroupFolderType_SecurityGroupName_Placeholder_InvalidateKeys =
      new NodeId(UShort.MIN, uint(25622));

  public static final NodeId
      SecurityGroupFolderType_SecurityGroupName_Placeholder_ForceKeyRotation =
          new NodeId(UShort.MIN, uint(25623));

  public static final NodeId SecurityGroupType_InvalidateKeys = new NodeId(UShort.MIN, uint(25624));

  public static final NodeId SecurityGroupType_ForceKeyRotation =
      new NodeId(UShort.MIN, uint(25625));

  public static final NodeId PubSubKeyPushTargetType_SecurityGroupName_Placeholder =
      new NodeId(UShort.MIN, uint(25626));

  public static final NodeId PubSubKeyPushTargetType_SecurityGroupName_Placeholder_SecurityGroupId =
      new NodeId(UShort.MIN, uint(25627));

  public static final NodeId PubSubKeyPushTargetType_SecurityGroupName_Placeholder_KeyLifetime =
      new NodeId(UShort.MIN, uint(25628));

  public static final NodeId
      PubSubKeyPushTargetType_SecurityGroupName_Placeholder_SecurityPolicyUri =
          new NodeId(UShort.MIN, uint(25629));

  public static final NodeId
      PubSubKeyPushTargetType_SecurityGroupName_Placeholder_MaxFutureKeyCount =
          new NodeId(UShort.MIN, uint(25630));

  public static final NodeId PubSubKeyPushTargetType_SecurityGroupName_Placeholder_MaxPastKeyCount =
      new NodeId(UShort.MIN, uint(25631));

  public static final NodeId PubSubKeyPushTargetType_SecurityGroupName_Placeholder_InvalidateKeys =
      new NodeId(UShort.MIN, uint(25632));

  public static final NodeId
      PubSubKeyPushTargetType_SecurityGroupName_Placeholder_ForceKeyRotation =
          new NodeId(UShort.MIN, uint(25633));

  public static final NodeId PubSubKeyPushTargetType_ApplicationUri =
      new NodeId(UShort.MIN, uint(25634));

  public static final NodeId PubSubKeyPushTargetType_EndpointUrl =
      new NodeId(UShort.MIN, uint(25635));

  public static final NodeId PubSubKeyPushTargetType_UserTokenType =
      new NodeId(UShort.MIN, uint(25636));

  public static final NodeId PubSubKeyPushTargetType_RequestedKeyCount =
      new NodeId(UShort.MIN, uint(25637));

  public static final NodeId PubSubKeyPushTargetType_RetryInterval =
      new NodeId(UShort.MIN, uint(25638));

  public static final NodeId PubSubKeyPushTargetType_LastPushExecutionTime =
      new NodeId(UShort.MIN, uint(25639));

  public static final NodeId PubSubKeyPushTargetType_LastPushErrorTime =
      new NodeId(UShort.MIN, uint(25640));

  public static final NodeId PubSubKeyPushTargetType_ConnectSecurityGroups =
      new NodeId(UShort.MIN, uint(25641));

  public static final NodeId PubSubKeyPushTargetType_ConnectSecurityGroups_InputArguments =
      new NodeId(UShort.MIN, uint(25642));

  public static final NodeId PubSubKeyPushTargetType_ConnectSecurityGroups_OutputArguments =
      new NodeId(UShort.MIN, uint(25643));

  public static final NodeId PubSubKeyPushTargetType_DisconnectSecurityGroups =
      new NodeId(UShort.MIN, uint(25644));

  public static final NodeId PubSubKeyPushTargetType_DisconnectSecurityGroups_InputArguments =
      new NodeId(UShort.MIN, uint(25645));

  public static final NodeId PubSubKeyPushTargetType_DisconnectSecurityGroups_OutputArguments =
      new NodeId(UShort.MIN, uint(25646));

  public static final NodeId PubSubKeyPushTargetType_TriggerKeyUpdate =
      new NodeId(UShort.MIN, uint(25647));

  public static final NodeId
      PubSubKeyPushTargetFolderType_PushTargetName_Placeholder_ApplicationUri =
          new NodeId(UShort.MIN, uint(25648));

  public static final NodeId PubSubKeyPushTargetFolderType_PushTargetName_Placeholder_EndpointUrl =
      new NodeId(UShort.MIN, uint(25649));

  public static final NodeId
      PubSubKeyPushTargetFolderType_PushTargetName_Placeholder_UserTokenType =
          new NodeId(UShort.MIN, uint(25650));

  public static final NodeId
      PubSubKeyPushTargetFolderType_PushTargetName_Placeholder_RequestedKeyCount =
          new NodeId(UShort.MIN, uint(25651));

  public static final NodeId
      PubSubKeyPushTargetFolderType_PushTargetName_Placeholder_RetryInterval =
          new NodeId(UShort.MIN, uint(25652));

  public static final NodeId
      PubSubKeyPushTargetFolderType_PushTargetName_Placeholder_LastPushExecutionTime =
          new NodeId(UShort.MIN, uint(25653));

  public static final NodeId
      PubSubKeyPushTargetFolderType_PushTargetName_Placeholder_LastPushErrorTime =
          new NodeId(UShort.MIN, uint(25654));

  public static final NodeId
      PubSubKeyPushTargetFolderType_PushTargetName_Placeholder_ConnectSecurityGroups =
          new NodeId(UShort.MIN, uint(25655));

  public static final NodeId
      PubSubKeyPushTargetFolderType_PushTargetName_Placeholder_ConnectSecurityGroups_InputArguments =
          new NodeId(UShort.MIN, uint(25656));

  public static final NodeId
      PubSubKeyPushTargetFolderType_PushTargetName_Placeholder_ConnectSecurityGroups_OutputArguments =
          new NodeId(UShort.MIN, uint(25657));

  public static final NodeId
      PubSubKeyPushTargetFolderType_PushTargetName_Placeholder_DisconnectSecurityGroups =
          new NodeId(UShort.MIN, uint(25658));

  public static final NodeId
      PubSubKeyPushTargetFolderType_PushTargetName_Placeholder_DisconnectSecurityGroups_InputArguments =
          new NodeId(UShort.MIN, uint(25659));

  public static final NodeId
      PubSubKeyPushTargetFolderType_PushTargetName_Placeholder_DisconnectSecurityGroups_OutputArguments =
          new NodeId(UShort.MIN, uint(25660));

  public static final NodeId
      PubSubKeyPushTargetFolderType_PushTargetName_Placeholder_TriggerKeyUpdate =
          new NodeId(UShort.MIN, uint(25661));

  public static final NodeId AuditClientUpdateMethodResultEventType_OutputArguments =
      new NodeId(UShort.MIN, uint(25684));

  public static final NodeId ServerConfigurationType_ApplicationUri =
      new NodeId(UShort.MIN, uint(25696));

  public static final NodeId ServerConfigurationType_ApplicationType =
      new NodeId(UShort.MIN, uint(25697));

  public static final NodeId ServerConfigurationType_CancelChanges =
      new NodeId(UShort.MIN, uint(25698));

  public static final NodeId ServerConfigurationType_ResetToServerDefaults =
      new NodeId(UShort.MIN, uint(25699));

  public static final NodeId ServerConfiguration_ApplicationUri =
      new NodeId(UShort.MIN, uint(25706));

  public static final NodeId ServerConfiguration_ApplicationType =
      new NodeId(UShort.MIN, uint(25707));

  public static final NodeId ServerConfiguration_CancelChanges =
      new NodeId(UShort.MIN, uint(25708));

  public static final NodeId ServerConfiguration_ResetToServerDefaults =
      new NodeId(UShort.MIN, uint(25709));

  public static final NodeId ServerConfigurationType_ProductUri =
      new NodeId(UShort.MIN, uint(25724));

  public static final NodeId ServerConfiguration_ProductUri = new NodeId(UShort.MIN, uint(25725));

  public static final NodeId EncodedTicket = new NodeId(UShort.MIN, uint(25726));

  public static final NodeId RequestTicketsMethodType = new NodeId(UShort.MIN, uint(25727));

  public static final NodeId RequestTicketsMethodType_OutputArguments =
      new NodeId(UShort.MIN, uint(25728));

  public static final NodeId SetRegistrarEndpointsMethodType = new NodeId(UShort.MIN, uint(25729));

  public static final NodeId SetRegistrarEndpointsMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(25730));

  public static final NodeId ApplicationConfigurationType = new NodeId(UShort.MIN, uint(25731));

  public static final NodeId ApplicationConfigurationType_Enabled =
      new NodeId(UShort.MIN, uint(26849));

  public static final NodeId ApplicationConfigurationType_ApplicationUri =
      new NodeId(UShort.MIN, uint(26850));

  public static final NodeId ApplicationConfigurationType_ProductUri =
      new NodeId(UShort.MIN, uint(26851));

  public static final NodeId ApplicationConfigurationType_ApplicationType =
      new NodeId(UShort.MIN, uint(26852));

  public static final NodeId ProvisionableDeviceType = new NodeId(UShort.MIN, uint(26871));

  public static final NodeId ProvisionableDeviceType_IsSingleton =
      new NodeId(UShort.MIN, uint(26872));

  public static final NodeId ProvisionableDeviceType_RequestTickets =
      new NodeId(UShort.MIN, uint(26873));

  public static final NodeId ProvisionableDeviceType_RequestTickets_OutputArguments =
      new NodeId(UShort.MIN, uint(26874));

  public static final NodeId ProvisionableDeviceType_SetRegistrarEndpoints =
      new NodeId(UShort.MIN, uint(26875));

  public static final NodeId ProvisionableDeviceType_SetRegistrarEndpoints_InputArguments =
      new NodeId(UShort.MIN, uint(26876));

  public static final NodeId ProvisionableDeviceType_ApplicationName_Placeholder =
      new NodeId(UShort.MIN, uint(26878));

  public static final NodeId ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups =
      new NodeId(UShort.MIN, uint(26879));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup =
          new NodeId(UShort.MIN, uint(26880));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustList =
          new NodeId(UShort.MIN, uint(26881));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustList_Size =
          new NodeId(UShort.MIN, uint(26882));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustList_Writable =
          new NodeId(UShort.MIN, uint(26883));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustList_UserWritable =
          new NodeId(UShort.MIN, uint(26884));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustList_OpenCount =
          new NodeId(UShort.MIN, uint(26885));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustList_MimeType =
          new NodeId(UShort.MIN, uint(26886));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustList_MaxByteStringLength =
          new NodeId(UShort.MIN, uint(26887));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustList_LastModifiedTime =
          new NodeId(UShort.MIN, uint(26888));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustList_Open =
          new NodeId(UShort.MIN, uint(26889));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustList_Open_InputArguments =
          new NodeId(UShort.MIN, uint(26890));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustList_Open_OutputArguments =
          new NodeId(UShort.MIN, uint(26891));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustList_Close =
          new NodeId(UShort.MIN, uint(26892));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustList_Close_InputArguments =
          new NodeId(UShort.MIN, uint(26893));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustList_Read =
          new NodeId(UShort.MIN, uint(26894));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustList_Read_InputArguments =
          new NodeId(UShort.MIN, uint(26895));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustList_Read_OutputArguments =
          new NodeId(UShort.MIN, uint(26896));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustList_Write =
          new NodeId(UShort.MIN, uint(26897));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustList_Write_InputArguments =
          new NodeId(UShort.MIN, uint(26898));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustList_GetPosition =
          new NodeId(UShort.MIN, uint(26899));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustList_GetPosition_InputArguments =
          new NodeId(UShort.MIN, uint(26900));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustList_GetPosition_OutputArguments =
          new NodeId(UShort.MIN, uint(26901));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustList_SetPosition =
          new NodeId(UShort.MIN, uint(26902));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustList_SetPosition_InputArguments =
          new NodeId(UShort.MIN, uint(26903));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustList_LastUpdateTime =
          new NodeId(UShort.MIN, uint(26904));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustList_UpdateFrequency =
          new NodeId(UShort.MIN, uint(26905));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustList_DefaultValidationOptions =
          new NodeId(UShort.MIN, uint(26906));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustList_OpenWithMasks =
          new NodeId(UShort.MIN, uint(26907));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustList_OpenWithMasks_InputArguments =
          new NodeId(UShort.MIN, uint(26908));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustList_OpenWithMasks_OutputArguments =
          new NodeId(UShort.MIN, uint(26909));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustList_CloseAndUpdate =
          new NodeId(UShort.MIN, uint(26910));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustList_CloseAndUpdate_InputArguments =
          new NodeId(UShort.MIN, uint(26911));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustList_CloseAndUpdate_OutputArguments =
          new NodeId(UShort.MIN, uint(26912));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustList_AddCertificate =
          new NodeId(UShort.MIN, uint(26913));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustList_AddCertificate_InputArguments =
          new NodeId(UShort.MIN, uint(26914));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustList_RemoveCertificate =
          new NodeId(UShort.MIN, uint(26915));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustList_RemoveCertificate_InputArguments =
          new NodeId(UShort.MIN, uint(26916));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateTypes =
          new NodeId(UShort.MIN, uint(26917));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_GetRejectedList =
          new NodeId(UShort.MIN, uint(26919));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_GetRejectedList_OutputArguments =
          new NodeId(UShort.MIN, uint(26920));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired =
          new NodeId(UShort.MIN, uint(26921));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_EventId =
          new NodeId(UShort.MIN, uint(26922));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_EventType =
          new NodeId(UShort.MIN, uint(26923));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_SourceNode =
          new NodeId(UShort.MIN, uint(26924));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_SourceName =
          new NodeId(UShort.MIN, uint(26925));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_Time =
          new NodeId(UShort.MIN, uint(26926));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ReceiveTime =
          new NodeId(UShort.MIN, uint(26927));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_LocalTime =
          new NodeId(UShort.MIN, uint(26928));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_Message =
          new NodeId(UShort.MIN, uint(26929));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_Severity =
          new NodeId(UShort.MIN, uint(26930));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ConditionClassId =
          new NodeId(UShort.MIN, uint(26931));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ConditionClassName =
          new NodeId(UShort.MIN, uint(26932));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ConditionSubClassId =
          new NodeId(UShort.MIN, uint(26933));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ConditionSubClassName =
          new NodeId(UShort.MIN, uint(26934));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ConditionName =
          new NodeId(UShort.MIN, uint(26935));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_BranchId =
          new NodeId(UShort.MIN, uint(26936));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_Retain =
          new NodeId(UShort.MIN, uint(26937));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_EnabledState =
          new NodeId(UShort.MIN, uint(26938));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_EnabledState_Id =
          new NodeId(UShort.MIN, uint(26939));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_EnabledState_Name =
          new NodeId(UShort.MIN, uint(26940));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_EnabledState_Number =
          new NodeId(UShort.MIN, uint(26941));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_EnabledState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(26942));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_EnabledState_TransitionTime =
          new NodeId(UShort.MIN, uint(26943));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_EnabledState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(26944));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_EnabledState_TrueState =
          new NodeId(UShort.MIN, uint(26945));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_EnabledState_FalseState =
          new NodeId(UShort.MIN, uint(26946));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_Quality =
          new NodeId(UShort.MIN, uint(26947));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_Quality_SourceTimestamp =
          new NodeId(UShort.MIN, uint(26948));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_LastSeverity =
          new NodeId(UShort.MIN, uint(26949));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_LastSeverity_SourceTimestamp =
          new NodeId(UShort.MIN, uint(26950));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_Comment =
          new NodeId(UShort.MIN, uint(26951));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_Comment_SourceTimestamp =
          new NodeId(UShort.MIN, uint(26952));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ClientUserId =
          new NodeId(UShort.MIN, uint(26953));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_Disable =
          new NodeId(UShort.MIN, uint(26954));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_Enable =
          new NodeId(UShort.MIN, uint(26955));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_AddComment =
          new NodeId(UShort.MIN, uint(26956));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_AddComment_InputArguments =
          new NodeId(UShort.MIN, uint(26957));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_AckedState =
          new NodeId(UShort.MIN, uint(26958));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_AckedState_Id =
          new NodeId(UShort.MIN, uint(26959));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_AckedState_Name =
          new NodeId(UShort.MIN, uint(26960));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_AckedState_Number =
          new NodeId(UShort.MIN, uint(26961));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_AckedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(26962));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_AckedState_TransitionTime =
          new NodeId(UShort.MIN, uint(26963));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_AckedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(26964));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_AckedState_TrueState =
          new NodeId(UShort.MIN, uint(26965));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_AckedState_FalseState =
          new NodeId(UShort.MIN, uint(26966));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ConfirmedState =
          new NodeId(UShort.MIN, uint(26967));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ConfirmedState_Id =
          new NodeId(UShort.MIN, uint(26968));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ConfirmedState_Name =
          new NodeId(UShort.MIN, uint(26969));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ConfirmedState_Number =
          new NodeId(UShort.MIN, uint(26970));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ConfirmedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(26971));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ConfirmedState_TransitionTime =
          new NodeId(UShort.MIN, uint(26972));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ConfirmedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(26973));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ConfirmedState_TrueState =
          new NodeId(UShort.MIN, uint(26974));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ConfirmedState_FalseState =
          new NodeId(UShort.MIN, uint(26975));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_Acknowledge =
          new NodeId(UShort.MIN, uint(26976));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_Acknowledge_InputArguments =
          new NodeId(UShort.MIN, uint(26977));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_Confirm =
          new NodeId(UShort.MIN, uint(26978));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_Confirm_InputArguments =
          new NodeId(UShort.MIN, uint(26979));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ActiveState =
          new NodeId(UShort.MIN, uint(26980));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ActiveState_Id =
          new NodeId(UShort.MIN, uint(26981));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ActiveState_Name =
          new NodeId(UShort.MIN, uint(26982));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ActiveState_Number =
          new NodeId(UShort.MIN, uint(26983));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ActiveState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(26984));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ActiveState_TransitionTime =
          new NodeId(UShort.MIN, uint(26985));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ActiveState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(26986));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ActiveState_TrueState =
          new NodeId(UShort.MIN, uint(26987));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ActiveState_FalseState =
          new NodeId(UShort.MIN, uint(26988));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_InputNode =
          new NodeId(UShort.MIN, uint(26989));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_SuppressedState =
          new NodeId(UShort.MIN, uint(26990));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_SuppressedState_Id =
          new NodeId(UShort.MIN, uint(26991));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_SuppressedState_Name =
          new NodeId(UShort.MIN, uint(26992));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_SuppressedState_Number =
          new NodeId(UShort.MIN, uint(26993));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_SuppressedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(26994));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_SuppressedState_TransitionTime =
          new NodeId(UShort.MIN, uint(26995));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_SuppressedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(26996));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_SuppressedState_TrueState =
          new NodeId(UShort.MIN, uint(26997));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_SuppressedState_FalseState =
          new NodeId(UShort.MIN, uint(26998));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_OutOfServiceState =
          new NodeId(UShort.MIN, uint(26999));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_OutOfServiceState_Id =
          new NodeId(UShort.MIN, uint(27000));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_OutOfServiceState_Name =
          new NodeId(UShort.MIN, uint(27001));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_OutOfServiceState_Number =
          new NodeId(UShort.MIN, uint(27002));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_OutOfServiceState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(27003));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_OutOfServiceState_TransitionTime =
          new NodeId(UShort.MIN, uint(27004));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_OutOfServiceState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(27005));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_OutOfServiceState_TrueState =
          new NodeId(UShort.MIN, uint(27006));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_OutOfServiceState_FalseState =
          new NodeId(UShort.MIN, uint(27007));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ShelvingState =
          new NodeId(UShort.MIN, uint(27008));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ShelvingState_CurrentState =
          new NodeId(UShort.MIN, uint(27009));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ShelvingState_CurrentState_Id =
          new NodeId(UShort.MIN, uint(27010));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ShelvingState_CurrentState_Name =
          new NodeId(UShort.MIN, uint(27011));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ShelvingState_CurrentState_Number =
          new NodeId(UShort.MIN, uint(27012));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ShelvingState_CurrentState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(27013));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ShelvingState_LastTransition =
          new NodeId(UShort.MIN, uint(27014));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ShelvingState_LastTransition_Id =
          new NodeId(UShort.MIN, uint(27015));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ShelvingState_LastTransition_Name =
          new NodeId(UShort.MIN, uint(27016));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ShelvingState_LastTransition_Number =
          new NodeId(UShort.MIN, uint(27017));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ShelvingState_LastTransition_TransitionTime =
          new NodeId(UShort.MIN, uint(27018));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ShelvingState_LastTransition_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(27019));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ShelvingState_AvailableStates =
          new NodeId(UShort.MIN, uint(27020));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ShelvingState_AvailableTransitions =
          new NodeId(UShort.MIN, uint(27021));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ShelvingState_UnshelveTime =
          new NodeId(UShort.MIN, uint(27022));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ShelvingState_TimedShelve =
          new NodeId(UShort.MIN, uint(27023));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ShelvingState_TimedShelve_InputArguments =
          new NodeId(UShort.MIN, uint(27024));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ShelvingState_TimedShelve2 =
          new NodeId(UShort.MIN, uint(27025));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ShelvingState_TimedShelve2_InputArguments =
          new NodeId(UShort.MIN, uint(27026));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ShelvingState_Unshelve =
          new NodeId(UShort.MIN, uint(27027));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ShelvingState_Unshelve2 =
          new NodeId(UShort.MIN, uint(27028));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ShelvingState_Unshelve2_InputArguments =
          new NodeId(UShort.MIN, uint(27029));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ShelvingState_OneShotShelve =
          new NodeId(UShort.MIN, uint(27030));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ShelvingState_OneShotShelve2 =
          new NodeId(UShort.MIN, uint(27031));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ShelvingState_OneShotShelve2_InputArguments =
          new NodeId(UShort.MIN, uint(27032));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_SuppressedOrShelved =
          new NodeId(UShort.MIN, uint(27033));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_MaxTimeShelved =
          new NodeId(UShort.MIN, uint(27034));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_AudibleEnabled =
          new NodeId(UShort.MIN, uint(27035));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_AudibleSound =
          new NodeId(UShort.MIN, uint(27036));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_AudibleSound_ListId =
          new NodeId(UShort.MIN, uint(27037));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_AudibleSound_AgencyId =
          new NodeId(UShort.MIN, uint(27038));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_AudibleSound_VersionId =
          new NodeId(UShort.MIN, uint(27039));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_SilenceState =
          new NodeId(UShort.MIN, uint(27040));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_SilenceState_Id =
          new NodeId(UShort.MIN, uint(27041));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_SilenceState_Name =
          new NodeId(UShort.MIN, uint(27042));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_SilenceState_Number =
          new NodeId(UShort.MIN, uint(27043));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_SilenceState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(27044));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_SilenceState_TransitionTime =
          new NodeId(UShort.MIN, uint(27045));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_SilenceState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(27046));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_SilenceState_TrueState =
          new NodeId(UShort.MIN, uint(27047));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_SilenceState_FalseState =
          new NodeId(UShort.MIN, uint(27048));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_OnDelay =
          new NodeId(UShort.MIN, uint(27049));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_OffDelay =
          new NodeId(UShort.MIN, uint(27050));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_FirstInGroupFlag =
          new NodeId(UShort.MIN, uint(27051));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_FirstInGroup =
          new NodeId(UShort.MIN, uint(27052));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_LatchedState =
          new NodeId(UShort.MIN, uint(27053));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_LatchedState_Id =
          new NodeId(UShort.MIN, uint(27054));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_LatchedState_Name =
          new NodeId(UShort.MIN, uint(27055));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_LatchedState_Number =
          new NodeId(UShort.MIN, uint(27056));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_LatchedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(27057));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_LatchedState_TransitionTime =
          new NodeId(UShort.MIN, uint(27058));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_LatchedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(27059));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_LatchedState_TrueState =
          new NodeId(UShort.MIN, uint(27060));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_LatchedState_FalseState =
          new NodeId(UShort.MIN, uint(27061));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ReAlarmTime =
          new NodeId(UShort.MIN, uint(27062));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ReAlarmRepeatCount =
          new NodeId(UShort.MIN, uint(27063));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_Silence =
          new NodeId(UShort.MIN, uint(27064));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_Suppress =
          new NodeId(UShort.MIN, uint(27065));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_Suppress2 =
          new NodeId(UShort.MIN, uint(27066));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_Suppress2_InputArguments =
          new NodeId(UShort.MIN, uint(27067));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_Unsuppress =
          new NodeId(UShort.MIN, uint(27068));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_Unsuppress2 =
          new NodeId(UShort.MIN, uint(27069));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_Unsuppress2_InputArguments =
          new NodeId(UShort.MIN, uint(27070));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_RemoveFromService =
          new NodeId(UShort.MIN, uint(27071));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_RemoveFromService2 =
          new NodeId(UShort.MIN, uint(27072));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_RemoveFromService2_InputArguments =
          new NodeId(UShort.MIN, uint(27073));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_PlaceInService =
          new NodeId(UShort.MIN, uint(27074));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_PlaceInService2 =
          new NodeId(UShort.MIN, uint(27075));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_PlaceInService2_InputArguments =
          new NodeId(UShort.MIN, uint(27076));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_Reset =
          new NodeId(UShort.MIN, uint(27077));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_Reset2 =
          new NodeId(UShort.MIN, uint(27078));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_Reset2_InputArguments =
          new NodeId(UShort.MIN, uint(27079));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_GetGroupMemberships =
          new NodeId(UShort.MIN, uint(27080));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_GetGroupMemberships_OutputArguments =
          new NodeId(UShort.MIN, uint(27081));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_NormalState =
          new NodeId(UShort.MIN, uint(27082));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ExpirationDate =
          new NodeId(UShort.MIN, uint(27083));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ExpirationLimit =
          new NodeId(UShort.MIN, uint(27084));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_CertificateType =
          new NodeId(UShort.MIN, uint(27085));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_Certificate =
          new NodeId(UShort.MIN, uint(27086));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate =
          new NodeId(UShort.MIN, uint(27087));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_EventId =
          new NodeId(UShort.MIN, uint(27088));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_EventType =
          new NodeId(UShort.MIN, uint(27089));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_SourceNode =
          new NodeId(UShort.MIN, uint(27090));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_SourceName =
          new NodeId(UShort.MIN, uint(27091));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_Time =
          new NodeId(UShort.MIN, uint(27092));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ReceiveTime =
          new NodeId(UShort.MIN, uint(27093));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_LocalTime =
          new NodeId(UShort.MIN, uint(27094));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_Message =
          new NodeId(UShort.MIN, uint(27095));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_Severity =
          new NodeId(UShort.MIN, uint(27096));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ConditionClassId =
          new NodeId(UShort.MIN, uint(27097));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ConditionClassName =
          new NodeId(UShort.MIN, uint(27098));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ConditionSubClassId =
          new NodeId(UShort.MIN, uint(27099));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ConditionSubClassName =
          new NodeId(UShort.MIN, uint(27100));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ConditionName =
          new NodeId(UShort.MIN, uint(27101));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_BranchId =
          new NodeId(UShort.MIN, uint(27102));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_Retain =
          new NodeId(UShort.MIN, uint(27103));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_EnabledState =
          new NodeId(UShort.MIN, uint(27104));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_EnabledState_Id =
          new NodeId(UShort.MIN, uint(27105));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_EnabledState_Name =
          new NodeId(UShort.MIN, uint(27106));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_EnabledState_Number =
          new NodeId(UShort.MIN, uint(27107));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_EnabledState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(27108));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_EnabledState_TransitionTime =
          new NodeId(UShort.MIN, uint(27109));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_EnabledState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(27110));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_EnabledState_TrueState =
          new NodeId(UShort.MIN, uint(27111));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_EnabledState_FalseState =
          new NodeId(UShort.MIN, uint(27112));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_Quality =
          new NodeId(UShort.MIN, uint(27113));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_Quality_SourceTimestamp =
          new NodeId(UShort.MIN, uint(27114));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_LastSeverity =
          new NodeId(UShort.MIN, uint(27115));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_LastSeverity_SourceTimestamp =
          new NodeId(UShort.MIN, uint(27116));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_Comment =
          new NodeId(UShort.MIN, uint(27117));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_Comment_SourceTimestamp =
          new NodeId(UShort.MIN, uint(27118));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ClientUserId =
          new NodeId(UShort.MIN, uint(27119));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_Disable =
          new NodeId(UShort.MIN, uint(27120));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_Enable =
          new NodeId(UShort.MIN, uint(27121));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_AddComment =
          new NodeId(UShort.MIN, uint(27122));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_AddComment_InputArguments =
          new NodeId(UShort.MIN, uint(27123));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_AckedState =
          new NodeId(UShort.MIN, uint(27124));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_AckedState_Id =
          new NodeId(UShort.MIN, uint(27125));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_AckedState_Name =
          new NodeId(UShort.MIN, uint(27126));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_AckedState_Number =
          new NodeId(UShort.MIN, uint(27127));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_AckedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(27128));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_AckedState_TransitionTime =
          new NodeId(UShort.MIN, uint(27129));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_AckedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(27130));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_AckedState_TrueState =
          new NodeId(UShort.MIN, uint(27131));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_AckedState_FalseState =
          new NodeId(UShort.MIN, uint(27132));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ConfirmedState =
          new NodeId(UShort.MIN, uint(27133));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ConfirmedState_Id =
          new NodeId(UShort.MIN, uint(27134));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ConfirmedState_Name =
          new NodeId(UShort.MIN, uint(27135));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ConfirmedState_Number =
          new NodeId(UShort.MIN, uint(27136));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ConfirmedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(27137));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ConfirmedState_TransitionTime =
          new NodeId(UShort.MIN, uint(27138));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ConfirmedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(27139));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ConfirmedState_TrueState =
          new NodeId(UShort.MIN, uint(27140));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ConfirmedState_FalseState =
          new NodeId(UShort.MIN, uint(27141));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_Acknowledge =
          new NodeId(UShort.MIN, uint(27142));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_Acknowledge_InputArguments =
          new NodeId(UShort.MIN, uint(27143));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_Confirm =
          new NodeId(UShort.MIN, uint(27144));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_Confirm_InputArguments =
          new NodeId(UShort.MIN, uint(27145));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ActiveState =
          new NodeId(UShort.MIN, uint(27146));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ActiveState_Id =
          new NodeId(UShort.MIN, uint(27147));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ActiveState_Name =
          new NodeId(UShort.MIN, uint(27148));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ActiveState_Number =
          new NodeId(UShort.MIN, uint(27149));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ActiveState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(27150));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ActiveState_TransitionTime =
          new NodeId(UShort.MIN, uint(27151));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ActiveState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(27152));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ActiveState_TrueState =
          new NodeId(UShort.MIN, uint(27153));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ActiveState_FalseState =
          new NodeId(UShort.MIN, uint(27154));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_InputNode =
          new NodeId(UShort.MIN, uint(27155));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_SuppressedState =
          new NodeId(UShort.MIN, uint(27156));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_SuppressedState_Id =
          new NodeId(UShort.MIN, uint(27157));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_SuppressedState_Name =
          new NodeId(UShort.MIN, uint(27158));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_SuppressedState_Number =
          new NodeId(UShort.MIN, uint(27159));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_SuppressedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(27160));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_SuppressedState_TransitionTime =
          new NodeId(UShort.MIN, uint(27161));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_SuppressedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(27162));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_SuppressedState_TrueState =
          new NodeId(UShort.MIN, uint(27163));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_SuppressedState_FalseState =
          new NodeId(UShort.MIN, uint(27164));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_OutOfServiceState =
          new NodeId(UShort.MIN, uint(27165));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_OutOfServiceState_Id =
          new NodeId(UShort.MIN, uint(27166));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_OutOfServiceState_Name =
          new NodeId(UShort.MIN, uint(27167));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_OutOfServiceState_Number =
          new NodeId(UShort.MIN, uint(27168));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_OutOfServiceState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(27169));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_OutOfServiceState_TransitionTime =
          new NodeId(UShort.MIN, uint(27170));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_OutOfServiceState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(27171));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_OutOfServiceState_TrueState =
          new NodeId(UShort.MIN, uint(27172));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_OutOfServiceState_FalseState =
          new NodeId(UShort.MIN, uint(27173));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState =
          new NodeId(UShort.MIN, uint(27174));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState_CurrentState =
          new NodeId(UShort.MIN, uint(27175));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState_CurrentState_Id =
          new NodeId(UShort.MIN, uint(27176));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState_CurrentState_Name =
          new NodeId(UShort.MIN, uint(27177));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState_CurrentState_Number =
          new NodeId(UShort.MIN, uint(27178));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState_CurrentState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(27179));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState_LastTransition =
          new NodeId(UShort.MIN, uint(27180));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState_LastTransition_Id =
          new NodeId(UShort.MIN, uint(27181));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState_LastTransition_Name =
          new NodeId(UShort.MIN, uint(27182));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState_LastTransition_Number =
          new NodeId(UShort.MIN, uint(27183));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState_LastTransition_TransitionTime =
          new NodeId(UShort.MIN, uint(27184));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState_LastTransition_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(27185));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState_AvailableStates =
          new NodeId(UShort.MIN, uint(27186));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState_AvailableTransitions =
          new NodeId(UShort.MIN, uint(27187));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState_UnshelveTime =
          new NodeId(UShort.MIN, uint(27188));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState_TimedShelve =
          new NodeId(UShort.MIN, uint(27189));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState_TimedShelve_InputArguments =
          new NodeId(UShort.MIN, uint(27190));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState_TimedShelve2 =
          new NodeId(UShort.MIN, uint(27191));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState_TimedShelve2_InputArguments =
          new NodeId(UShort.MIN, uint(27192));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState_Unshelve =
          new NodeId(UShort.MIN, uint(27193));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState_Unshelve2 =
          new NodeId(UShort.MIN, uint(27194));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState_Unshelve2_InputArguments =
          new NodeId(UShort.MIN, uint(27195));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState_OneShotShelve =
          new NodeId(UShort.MIN, uint(27196));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState_OneShotShelve2 =
          new NodeId(UShort.MIN, uint(27197));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState_OneShotShelve2_InputArguments =
          new NodeId(UShort.MIN, uint(27198));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_SuppressedOrShelved =
          new NodeId(UShort.MIN, uint(27199));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_MaxTimeShelved =
          new NodeId(UShort.MIN, uint(27200));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_AudibleEnabled =
          new NodeId(UShort.MIN, uint(27201));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_AudibleSound =
          new NodeId(UShort.MIN, uint(27202));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_AudibleSound_ListId =
          new NodeId(UShort.MIN, uint(27203));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_AudibleSound_AgencyId =
          new NodeId(UShort.MIN, uint(27204));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_AudibleSound_VersionId =
          new NodeId(UShort.MIN, uint(27205));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_SilenceState =
          new NodeId(UShort.MIN, uint(27206));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_SilenceState_Id =
          new NodeId(UShort.MIN, uint(27207));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_SilenceState_Name =
          new NodeId(UShort.MIN, uint(27208));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_SilenceState_Number =
          new NodeId(UShort.MIN, uint(27209));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_SilenceState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(27210));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_SilenceState_TransitionTime =
          new NodeId(UShort.MIN, uint(27211));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_SilenceState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(27212));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_SilenceState_TrueState =
          new NodeId(UShort.MIN, uint(27213));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_SilenceState_FalseState =
          new NodeId(UShort.MIN, uint(27214));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_OnDelay =
          new NodeId(UShort.MIN, uint(27215));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_OffDelay =
          new NodeId(UShort.MIN, uint(27216));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_FirstInGroupFlag =
          new NodeId(UShort.MIN, uint(27217));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_FirstInGroup =
          new NodeId(UShort.MIN, uint(27218));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_LatchedState =
          new NodeId(UShort.MIN, uint(27219));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_LatchedState_Id =
          new NodeId(UShort.MIN, uint(27220));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_LatchedState_Name =
          new NodeId(UShort.MIN, uint(27221));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_LatchedState_Number =
          new NodeId(UShort.MIN, uint(27222));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_LatchedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(27223));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_LatchedState_TransitionTime =
          new NodeId(UShort.MIN, uint(27224));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_LatchedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(27225));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_LatchedState_TrueState =
          new NodeId(UShort.MIN, uint(27226));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_LatchedState_FalseState =
          new NodeId(UShort.MIN, uint(27227));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ReAlarmTime =
          new NodeId(UShort.MIN, uint(27228));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ReAlarmRepeatCount =
          new NodeId(UShort.MIN, uint(27229));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_Silence =
          new NodeId(UShort.MIN, uint(27230));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_Suppress =
          new NodeId(UShort.MIN, uint(27231));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_Suppress2 =
          new NodeId(UShort.MIN, uint(27232));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_Suppress2_InputArguments =
          new NodeId(UShort.MIN, uint(27233));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_Unsuppress =
          new NodeId(UShort.MIN, uint(27234));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_Unsuppress2 =
          new NodeId(UShort.MIN, uint(27235));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_Unsuppress2_InputArguments =
          new NodeId(UShort.MIN, uint(27236));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_RemoveFromService =
          new NodeId(UShort.MIN, uint(27237));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_RemoveFromService2 =
          new NodeId(UShort.MIN, uint(27238));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_RemoveFromService2_InputArguments =
          new NodeId(UShort.MIN, uint(27239));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_PlaceInService =
          new NodeId(UShort.MIN, uint(27240));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_PlaceInService2 =
          new NodeId(UShort.MIN, uint(27241));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_PlaceInService2_InputArguments =
          new NodeId(UShort.MIN, uint(27242));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_Reset =
          new NodeId(UShort.MIN, uint(27243));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_Reset2 =
          new NodeId(UShort.MIN, uint(27244));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_Reset2_InputArguments =
          new NodeId(UShort.MIN, uint(27245));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_GetGroupMemberships =
          new NodeId(UShort.MIN, uint(27246));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_GetGroupMemberships_OutputArguments =
          new NodeId(UShort.MIN, uint(27247));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_NormalState =
          new NodeId(UShort.MIN, uint(27248));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_TrustListId =
          new NodeId(UShort.MIN, uint(27249));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_LastUpdateTime =
          new NodeId(UShort.MIN, uint(27250));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_UpdateFrequency =
          new NodeId(UShort.MIN, uint(27251));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup =
          new NodeId(UShort.MIN, uint(27252));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustList =
          new NodeId(UShort.MIN, uint(27253));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustList_Size =
          new NodeId(UShort.MIN, uint(27254));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustList_Writable =
          new NodeId(UShort.MIN, uint(27255));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustList_UserWritable =
          new NodeId(UShort.MIN, uint(27256));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustList_OpenCount =
          new NodeId(UShort.MIN, uint(27257));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustList_MimeType =
          new NodeId(UShort.MIN, uint(27258));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustList_MaxByteStringLength =
          new NodeId(UShort.MIN, uint(27259));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustList_LastModifiedTime =
          new NodeId(UShort.MIN, uint(27260));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustList_Open =
          new NodeId(UShort.MIN, uint(27261));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustList_Open_InputArguments =
          new NodeId(UShort.MIN, uint(27262));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustList_Open_OutputArguments =
          new NodeId(UShort.MIN, uint(27263));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustList_Close =
          new NodeId(UShort.MIN, uint(27264));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustList_Close_InputArguments =
          new NodeId(UShort.MIN, uint(27265));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustList_Read =
          new NodeId(UShort.MIN, uint(27266));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustList_Read_InputArguments =
          new NodeId(UShort.MIN, uint(27267));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustList_Read_OutputArguments =
          new NodeId(UShort.MIN, uint(27268));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustList_Write =
          new NodeId(UShort.MIN, uint(27269));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustList_Write_InputArguments =
          new NodeId(UShort.MIN, uint(27270));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustList_GetPosition =
          new NodeId(UShort.MIN, uint(27271));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustList_GetPosition_InputArguments =
          new NodeId(UShort.MIN, uint(27272));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustList_GetPosition_OutputArguments =
          new NodeId(UShort.MIN, uint(27273));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustList_SetPosition =
          new NodeId(UShort.MIN, uint(27274));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustList_SetPosition_InputArguments =
          new NodeId(UShort.MIN, uint(27275));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustList_LastUpdateTime =
          new NodeId(UShort.MIN, uint(27276));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustList_UpdateFrequency =
          new NodeId(UShort.MIN, uint(27277));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustList_DefaultValidationOptions =
          new NodeId(UShort.MIN, uint(27278));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustList_OpenWithMasks =
          new NodeId(UShort.MIN, uint(27279));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustList_OpenWithMasks_InputArguments =
          new NodeId(UShort.MIN, uint(27280));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustList_OpenWithMasks_OutputArguments =
          new NodeId(UShort.MIN, uint(27281));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustList_CloseAndUpdate =
          new NodeId(UShort.MIN, uint(27282));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustList_CloseAndUpdate_InputArguments =
          new NodeId(UShort.MIN, uint(27283));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustList_CloseAndUpdate_OutputArguments =
          new NodeId(UShort.MIN, uint(27284));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustList_AddCertificate =
          new NodeId(UShort.MIN, uint(27285));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustList_AddCertificate_InputArguments =
          new NodeId(UShort.MIN, uint(27286));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustList_RemoveCertificate =
          new NodeId(UShort.MIN, uint(27287));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustList_RemoveCertificate_InputArguments =
          new NodeId(UShort.MIN, uint(27288));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateTypes =
          new NodeId(UShort.MIN, uint(27289));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_GetRejectedList =
          new NodeId(UShort.MIN, uint(27291));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_GetRejectedList_OutputArguments =
          new NodeId(UShort.MIN, uint(27292));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired =
          new NodeId(UShort.MIN, uint(27293));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_EventId =
          new NodeId(UShort.MIN, uint(27294));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_EventType =
          new NodeId(UShort.MIN, uint(27295));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_SourceNode =
          new NodeId(UShort.MIN, uint(27296));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_SourceName =
          new NodeId(UShort.MIN, uint(27297));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_Time =
          new NodeId(UShort.MIN, uint(27298));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ReceiveTime =
          new NodeId(UShort.MIN, uint(27299));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_LocalTime =
          new NodeId(UShort.MIN, uint(27300));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_Message =
          new NodeId(UShort.MIN, uint(27301));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_Severity =
          new NodeId(UShort.MIN, uint(27302));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ConditionClassId =
          new NodeId(UShort.MIN, uint(27303));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ConditionClassName =
          new NodeId(UShort.MIN, uint(27304));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ConditionSubClassId =
          new NodeId(UShort.MIN, uint(27305));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ConditionSubClassName =
          new NodeId(UShort.MIN, uint(27306));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ConditionName =
          new NodeId(UShort.MIN, uint(27307));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_BranchId =
          new NodeId(UShort.MIN, uint(27308));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_Retain =
          new NodeId(UShort.MIN, uint(27309));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_EnabledState =
          new NodeId(UShort.MIN, uint(27310));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_EnabledState_Id =
          new NodeId(UShort.MIN, uint(27311));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_EnabledState_Name =
          new NodeId(UShort.MIN, uint(27312));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_EnabledState_Number =
          new NodeId(UShort.MIN, uint(27313));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_EnabledState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(27314));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_EnabledState_TransitionTime =
          new NodeId(UShort.MIN, uint(27315));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_EnabledState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(27316));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_EnabledState_TrueState =
          new NodeId(UShort.MIN, uint(27317));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_EnabledState_FalseState =
          new NodeId(UShort.MIN, uint(27318));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_Quality =
          new NodeId(UShort.MIN, uint(27319));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_Quality_SourceTimestamp =
          new NodeId(UShort.MIN, uint(27320));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_LastSeverity =
          new NodeId(UShort.MIN, uint(27321));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_LastSeverity_SourceTimestamp =
          new NodeId(UShort.MIN, uint(27322));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_Comment =
          new NodeId(UShort.MIN, uint(27323));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_Comment_SourceTimestamp =
          new NodeId(UShort.MIN, uint(27324));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ClientUserId =
          new NodeId(UShort.MIN, uint(27325));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_Disable =
          new NodeId(UShort.MIN, uint(27326));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_Enable =
          new NodeId(UShort.MIN, uint(27327));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_AddComment =
          new NodeId(UShort.MIN, uint(27328));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_AddComment_InputArguments =
          new NodeId(UShort.MIN, uint(27329));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_AckedState =
          new NodeId(UShort.MIN, uint(27330));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_AckedState_Id =
          new NodeId(UShort.MIN, uint(27331));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_AckedState_Name =
          new NodeId(UShort.MIN, uint(27332));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_AckedState_Number =
          new NodeId(UShort.MIN, uint(27333));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_AckedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(27334));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_AckedState_TransitionTime =
          new NodeId(UShort.MIN, uint(27335));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_AckedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(27336));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_AckedState_TrueState =
          new NodeId(UShort.MIN, uint(27337));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_AckedState_FalseState =
          new NodeId(UShort.MIN, uint(27338));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ConfirmedState =
          new NodeId(UShort.MIN, uint(27339));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ConfirmedState_Id =
          new NodeId(UShort.MIN, uint(27340));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ConfirmedState_Name =
          new NodeId(UShort.MIN, uint(27341));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ConfirmedState_Number =
          new NodeId(UShort.MIN, uint(27342));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ConfirmedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(27343));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ConfirmedState_TransitionTime =
          new NodeId(UShort.MIN, uint(27344));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ConfirmedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(27345));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ConfirmedState_TrueState =
          new NodeId(UShort.MIN, uint(27346));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ConfirmedState_FalseState =
          new NodeId(UShort.MIN, uint(27347));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_Acknowledge =
          new NodeId(UShort.MIN, uint(27348));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_Acknowledge_InputArguments =
          new NodeId(UShort.MIN, uint(27349));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_Confirm =
          new NodeId(UShort.MIN, uint(27350));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_Confirm_InputArguments =
          new NodeId(UShort.MIN, uint(27351));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ActiveState =
          new NodeId(UShort.MIN, uint(27352));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ActiveState_Id =
          new NodeId(UShort.MIN, uint(27353));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ActiveState_Name =
          new NodeId(UShort.MIN, uint(27354));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ActiveState_Number =
          new NodeId(UShort.MIN, uint(27355));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ActiveState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(27356));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ActiveState_TransitionTime =
          new NodeId(UShort.MIN, uint(27357));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ActiveState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(27358));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ActiveState_TrueState =
          new NodeId(UShort.MIN, uint(27359));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ActiveState_FalseState =
          new NodeId(UShort.MIN, uint(27360));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_InputNode =
          new NodeId(UShort.MIN, uint(27361));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_SuppressedState =
          new NodeId(UShort.MIN, uint(27362));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_SuppressedState_Id =
          new NodeId(UShort.MIN, uint(27363));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_SuppressedState_Name =
          new NodeId(UShort.MIN, uint(27364));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_SuppressedState_Number =
          new NodeId(UShort.MIN, uint(27365));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_SuppressedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(27366));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_SuppressedState_TransitionTime =
          new NodeId(UShort.MIN, uint(27367));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_SuppressedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(27368));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_SuppressedState_TrueState =
          new NodeId(UShort.MIN, uint(27369));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_SuppressedState_FalseState =
          new NodeId(UShort.MIN, uint(27370));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_OutOfServiceState =
          new NodeId(UShort.MIN, uint(27371));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_OutOfServiceState_Id =
          new NodeId(UShort.MIN, uint(27372));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_OutOfServiceState_Name =
          new NodeId(UShort.MIN, uint(27373));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_OutOfServiceState_Number =
          new NodeId(UShort.MIN, uint(27374));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_OutOfServiceState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(27375));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_OutOfServiceState_TransitionTime =
          new NodeId(UShort.MIN, uint(27376));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_OutOfServiceState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(27377));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_OutOfServiceState_TrueState =
          new NodeId(UShort.MIN, uint(27378));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_OutOfServiceState_FalseState =
          new NodeId(UShort.MIN, uint(27379));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ShelvingState =
          new NodeId(UShort.MIN, uint(27380));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ShelvingState_CurrentState =
          new NodeId(UShort.MIN, uint(27381));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ShelvingState_CurrentState_Id =
          new NodeId(UShort.MIN, uint(27382));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ShelvingState_CurrentState_Name =
          new NodeId(UShort.MIN, uint(27383));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ShelvingState_CurrentState_Number =
          new NodeId(UShort.MIN, uint(27384));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ShelvingState_CurrentState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(27385));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ShelvingState_LastTransition =
          new NodeId(UShort.MIN, uint(27386));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ShelvingState_LastTransition_Id =
          new NodeId(UShort.MIN, uint(27387));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ShelvingState_LastTransition_Name =
          new NodeId(UShort.MIN, uint(27388));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ShelvingState_LastTransition_Number =
          new NodeId(UShort.MIN, uint(27389));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ShelvingState_LastTransition_TransitionTime =
          new NodeId(UShort.MIN, uint(27390));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ShelvingState_LastTransition_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(27391));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ShelvingState_AvailableStates =
          new NodeId(UShort.MIN, uint(27392));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ShelvingState_AvailableTransitions =
          new NodeId(UShort.MIN, uint(27393));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ShelvingState_UnshelveTime =
          new NodeId(UShort.MIN, uint(27394));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ShelvingState_TimedShelve =
          new NodeId(UShort.MIN, uint(27395));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ShelvingState_TimedShelve_InputArguments =
          new NodeId(UShort.MIN, uint(27396));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ShelvingState_TimedShelve2 =
          new NodeId(UShort.MIN, uint(27397));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ShelvingState_TimedShelve2_InputArguments =
          new NodeId(UShort.MIN, uint(27398));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ShelvingState_Unshelve =
          new NodeId(UShort.MIN, uint(27399));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ShelvingState_Unshelve2 =
          new NodeId(UShort.MIN, uint(27400));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ShelvingState_Unshelve2_InputArguments =
          new NodeId(UShort.MIN, uint(27401));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ShelvingState_OneShotShelve =
          new NodeId(UShort.MIN, uint(27402));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ShelvingState_OneShotShelve2 =
          new NodeId(UShort.MIN, uint(27403));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ShelvingState_OneShotShelve2_InputArguments =
          new NodeId(UShort.MIN, uint(27404));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_SuppressedOrShelved =
          new NodeId(UShort.MIN, uint(27405));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_MaxTimeShelved =
          new NodeId(UShort.MIN, uint(27406));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_AudibleEnabled =
          new NodeId(UShort.MIN, uint(27407));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_AudibleSound =
          new NodeId(UShort.MIN, uint(27408));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_AudibleSound_ListId =
          new NodeId(UShort.MIN, uint(27409));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_AudibleSound_AgencyId =
          new NodeId(UShort.MIN, uint(27410));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_AudibleSound_VersionId =
          new NodeId(UShort.MIN, uint(27411));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_SilenceState =
          new NodeId(UShort.MIN, uint(27412));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_SilenceState_Id =
          new NodeId(UShort.MIN, uint(27413));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_SilenceState_Name =
          new NodeId(UShort.MIN, uint(27414));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_SilenceState_Number =
          new NodeId(UShort.MIN, uint(27415));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_SilenceState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(27416));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_SilenceState_TransitionTime =
          new NodeId(UShort.MIN, uint(27417));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_SilenceState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(27418));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_SilenceState_TrueState =
          new NodeId(UShort.MIN, uint(27419));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_SilenceState_FalseState =
          new NodeId(UShort.MIN, uint(27420));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_OnDelay =
          new NodeId(UShort.MIN, uint(27421));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_OffDelay =
          new NodeId(UShort.MIN, uint(27422));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_FirstInGroupFlag =
          new NodeId(UShort.MIN, uint(27423));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_FirstInGroup =
          new NodeId(UShort.MIN, uint(27424));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_LatchedState =
          new NodeId(UShort.MIN, uint(27425));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_LatchedState_Id =
          new NodeId(UShort.MIN, uint(27426));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_LatchedState_Name =
          new NodeId(UShort.MIN, uint(27427));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_LatchedState_Number =
          new NodeId(UShort.MIN, uint(27428));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_LatchedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(27429));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_LatchedState_TransitionTime =
          new NodeId(UShort.MIN, uint(27430));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_LatchedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(27431));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_LatchedState_TrueState =
          new NodeId(UShort.MIN, uint(27432));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_LatchedState_FalseState =
          new NodeId(UShort.MIN, uint(27433));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ReAlarmTime =
          new NodeId(UShort.MIN, uint(27434));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ReAlarmRepeatCount =
          new NodeId(UShort.MIN, uint(27435));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_Silence =
          new NodeId(UShort.MIN, uint(27436));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_Suppress =
          new NodeId(UShort.MIN, uint(27437));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_Suppress2 =
          new NodeId(UShort.MIN, uint(27438));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_Suppress2_InputArguments =
          new NodeId(UShort.MIN, uint(27439));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_Unsuppress =
          new NodeId(UShort.MIN, uint(27440));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_Unsuppress2 =
          new NodeId(UShort.MIN, uint(27441));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_Unsuppress2_InputArguments =
          new NodeId(UShort.MIN, uint(27442));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_RemoveFromService =
          new NodeId(UShort.MIN, uint(27443));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_RemoveFromService2 =
          new NodeId(UShort.MIN, uint(27444));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_RemoveFromService2_InputArguments =
          new NodeId(UShort.MIN, uint(27445));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_PlaceInService =
          new NodeId(UShort.MIN, uint(27446));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_PlaceInService2 =
          new NodeId(UShort.MIN, uint(27447));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_PlaceInService2_InputArguments =
          new NodeId(UShort.MIN, uint(27448));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_Reset =
          new NodeId(UShort.MIN, uint(27449));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_Reset2 =
          new NodeId(UShort.MIN, uint(27450));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_Reset2_InputArguments =
          new NodeId(UShort.MIN, uint(27451));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_GetGroupMemberships =
          new NodeId(UShort.MIN, uint(27452));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_GetGroupMemberships_OutputArguments =
          new NodeId(UShort.MIN, uint(27453));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_NormalState =
          new NodeId(UShort.MIN, uint(27454));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ExpirationDate =
          new NodeId(UShort.MIN, uint(27455));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ExpirationLimit =
          new NodeId(UShort.MIN, uint(27456));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_CertificateType =
          new NodeId(UShort.MIN, uint(27457));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_CertificateExpired_Certificate =
          new NodeId(UShort.MIN, uint(27458));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate =
          new NodeId(UShort.MIN, uint(27459));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_EventId =
          new NodeId(UShort.MIN, uint(27460));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_EventType =
          new NodeId(UShort.MIN, uint(27461));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_SourceNode =
          new NodeId(UShort.MIN, uint(27462));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_SourceName =
          new NodeId(UShort.MIN, uint(27463));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_Time =
          new NodeId(UShort.MIN, uint(27464));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ReceiveTime =
          new NodeId(UShort.MIN, uint(27465));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_LocalTime =
          new NodeId(UShort.MIN, uint(27466));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_Message =
          new NodeId(UShort.MIN, uint(27467));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_Severity =
          new NodeId(UShort.MIN, uint(27468));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ConditionClassId =
          new NodeId(UShort.MIN, uint(27469));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ConditionClassName =
          new NodeId(UShort.MIN, uint(27470));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ConditionSubClassId =
          new NodeId(UShort.MIN, uint(27471));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ConditionSubClassName =
          new NodeId(UShort.MIN, uint(27472));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ConditionName =
          new NodeId(UShort.MIN, uint(27473));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_BranchId =
          new NodeId(UShort.MIN, uint(27474));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_Retain =
          new NodeId(UShort.MIN, uint(27475));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_EnabledState =
          new NodeId(UShort.MIN, uint(27476));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_EnabledState_Id =
          new NodeId(UShort.MIN, uint(27477));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_EnabledState_Name =
          new NodeId(UShort.MIN, uint(27478));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_EnabledState_Number =
          new NodeId(UShort.MIN, uint(27479));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_EnabledState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(27480));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_EnabledState_TransitionTime =
          new NodeId(UShort.MIN, uint(27481));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_EnabledState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(27482));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_EnabledState_TrueState =
          new NodeId(UShort.MIN, uint(27483));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_EnabledState_FalseState =
          new NodeId(UShort.MIN, uint(27484));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_Quality =
          new NodeId(UShort.MIN, uint(27485));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_Quality_SourceTimestamp =
          new NodeId(UShort.MIN, uint(27486));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_LastSeverity =
          new NodeId(UShort.MIN, uint(27487));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_LastSeverity_SourceTimestamp =
          new NodeId(UShort.MIN, uint(27488));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_Comment =
          new NodeId(UShort.MIN, uint(27489));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_Comment_SourceTimestamp =
          new NodeId(UShort.MIN, uint(27490));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ClientUserId =
          new NodeId(UShort.MIN, uint(27491));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_Disable =
          new NodeId(UShort.MIN, uint(27492));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_Enable =
          new NodeId(UShort.MIN, uint(27493));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_AddComment =
          new NodeId(UShort.MIN, uint(27494));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_AddComment_InputArguments =
          new NodeId(UShort.MIN, uint(27495));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_AckedState =
          new NodeId(UShort.MIN, uint(27496));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_AckedState_Id =
          new NodeId(UShort.MIN, uint(27497));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_AckedState_Name =
          new NodeId(UShort.MIN, uint(27498));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_AckedState_Number =
          new NodeId(UShort.MIN, uint(27499));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_AckedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(27500));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_AckedState_TransitionTime =
          new NodeId(UShort.MIN, uint(27501));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_AckedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(27502));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_AckedState_TrueState =
          new NodeId(UShort.MIN, uint(27503));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_AckedState_FalseState =
          new NodeId(UShort.MIN, uint(27504));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ConfirmedState =
          new NodeId(UShort.MIN, uint(27505));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ConfirmedState_Id =
          new NodeId(UShort.MIN, uint(27506));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ConfirmedState_Name =
          new NodeId(UShort.MIN, uint(27507));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ConfirmedState_Number =
          new NodeId(UShort.MIN, uint(27508));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ConfirmedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(27509));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ConfirmedState_TransitionTime =
          new NodeId(UShort.MIN, uint(27510));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ConfirmedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(27511));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ConfirmedState_TrueState =
          new NodeId(UShort.MIN, uint(27512));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ConfirmedState_FalseState =
          new NodeId(UShort.MIN, uint(27513));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_Acknowledge =
          new NodeId(UShort.MIN, uint(27514));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_Acknowledge_InputArguments =
          new NodeId(UShort.MIN, uint(27515));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_Confirm =
          new NodeId(UShort.MIN, uint(27516));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_Confirm_InputArguments =
          new NodeId(UShort.MIN, uint(27517));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ActiveState =
          new NodeId(UShort.MIN, uint(27518));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ActiveState_Id =
          new NodeId(UShort.MIN, uint(27519));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ActiveState_Name =
          new NodeId(UShort.MIN, uint(27520));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ActiveState_Number =
          new NodeId(UShort.MIN, uint(27521));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ActiveState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(27522));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ActiveState_TransitionTime =
          new NodeId(UShort.MIN, uint(27523));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ActiveState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(27524));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ActiveState_TrueState =
          new NodeId(UShort.MIN, uint(27525));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ActiveState_FalseState =
          new NodeId(UShort.MIN, uint(27526));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_InputNode =
          new NodeId(UShort.MIN, uint(27527));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_SuppressedState =
          new NodeId(UShort.MIN, uint(27528));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_SuppressedState_Id =
          new NodeId(UShort.MIN, uint(27529));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_SuppressedState_Name =
          new NodeId(UShort.MIN, uint(27530));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_SuppressedState_Number =
          new NodeId(UShort.MIN, uint(27531));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_SuppressedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(27532));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_SuppressedState_TransitionTime =
          new NodeId(UShort.MIN, uint(27533));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_SuppressedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(27534));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_SuppressedState_TrueState =
          new NodeId(UShort.MIN, uint(27535));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_SuppressedState_FalseState =
          new NodeId(UShort.MIN, uint(27536));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_OutOfServiceState =
          new NodeId(UShort.MIN, uint(27537));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_OutOfServiceState_Id =
          new NodeId(UShort.MIN, uint(27538));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_OutOfServiceState_Name =
          new NodeId(UShort.MIN, uint(27539));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_OutOfServiceState_Number =
          new NodeId(UShort.MIN, uint(27540));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_OutOfServiceState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(27541));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_OutOfServiceState_TransitionTime =
          new NodeId(UShort.MIN, uint(27542));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_OutOfServiceState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(27543));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_OutOfServiceState_TrueState =
          new NodeId(UShort.MIN, uint(27544));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_OutOfServiceState_FalseState =
          new NodeId(UShort.MIN, uint(27545));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState =
          new NodeId(UShort.MIN, uint(27546));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState_CurrentState =
          new NodeId(UShort.MIN, uint(27547));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState_CurrentState_Id =
          new NodeId(UShort.MIN, uint(27548));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState_CurrentState_Name =
          new NodeId(UShort.MIN, uint(27549));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState_CurrentState_Number =
          new NodeId(UShort.MIN, uint(27550));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState_CurrentState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(27551));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState_LastTransition =
          new NodeId(UShort.MIN, uint(27552));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState_LastTransition_Id =
          new NodeId(UShort.MIN, uint(27553));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState_LastTransition_Name =
          new NodeId(UShort.MIN, uint(27554));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState_LastTransition_Number =
          new NodeId(UShort.MIN, uint(27555));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState_LastTransition_TransitionTime =
          new NodeId(UShort.MIN, uint(27556));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState_LastTransition_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(27557));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState_AvailableStates =
          new NodeId(UShort.MIN, uint(27558));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState_AvailableTransitions =
          new NodeId(UShort.MIN, uint(27559));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState_UnshelveTime =
          new NodeId(UShort.MIN, uint(27560));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState_TimedShelve =
          new NodeId(UShort.MIN, uint(27561));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState_TimedShelve_InputArguments =
          new NodeId(UShort.MIN, uint(27562));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState_TimedShelve2 =
          new NodeId(UShort.MIN, uint(27563));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState_TimedShelve2_InputArguments =
          new NodeId(UShort.MIN, uint(27564));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState_Unshelve =
          new NodeId(UShort.MIN, uint(27565));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState_Unshelve2 =
          new NodeId(UShort.MIN, uint(27566));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState_Unshelve2_InputArguments =
          new NodeId(UShort.MIN, uint(27567));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState_OneShotShelve =
          new NodeId(UShort.MIN, uint(27568));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState_OneShotShelve2 =
          new NodeId(UShort.MIN, uint(27569));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState_OneShotShelve2_InputArguments =
          new NodeId(UShort.MIN, uint(27570));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_SuppressedOrShelved =
          new NodeId(UShort.MIN, uint(27571));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_MaxTimeShelved =
          new NodeId(UShort.MIN, uint(27572));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_AudibleEnabled =
          new NodeId(UShort.MIN, uint(27573));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_AudibleSound =
          new NodeId(UShort.MIN, uint(27574));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_AudibleSound_ListId =
          new NodeId(UShort.MIN, uint(27575));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_AudibleSound_AgencyId =
          new NodeId(UShort.MIN, uint(27576));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_AudibleSound_VersionId =
          new NodeId(UShort.MIN, uint(27577));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_SilenceState =
          new NodeId(UShort.MIN, uint(27578));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_SilenceState_Id =
          new NodeId(UShort.MIN, uint(27579));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_SilenceState_Name =
          new NodeId(UShort.MIN, uint(27580));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_SilenceState_Number =
          new NodeId(UShort.MIN, uint(27581));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_SilenceState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(27582));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_SilenceState_TransitionTime =
          new NodeId(UShort.MIN, uint(27583));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_SilenceState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(27584));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_SilenceState_TrueState =
          new NodeId(UShort.MIN, uint(27585));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_SilenceState_FalseState =
          new NodeId(UShort.MIN, uint(27586));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_OnDelay =
          new NodeId(UShort.MIN, uint(27587));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_OffDelay =
          new NodeId(UShort.MIN, uint(27588));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_FirstInGroupFlag =
          new NodeId(UShort.MIN, uint(27589));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_FirstInGroup =
          new NodeId(UShort.MIN, uint(27590));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_LatchedState =
          new NodeId(UShort.MIN, uint(27591));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_LatchedState_Id =
          new NodeId(UShort.MIN, uint(27592));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_LatchedState_Name =
          new NodeId(UShort.MIN, uint(27593));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_LatchedState_Number =
          new NodeId(UShort.MIN, uint(27594));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_LatchedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(27595));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_LatchedState_TransitionTime =
          new NodeId(UShort.MIN, uint(27596));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_LatchedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(27597));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_LatchedState_TrueState =
          new NodeId(UShort.MIN, uint(27598));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_LatchedState_FalseState =
          new NodeId(UShort.MIN, uint(27599));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ReAlarmTime =
          new NodeId(UShort.MIN, uint(27600));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ReAlarmRepeatCount =
          new NodeId(UShort.MIN, uint(27601));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_Silence =
          new NodeId(UShort.MIN, uint(27602));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_Suppress =
          new NodeId(UShort.MIN, uint(27603));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_Suppress2 =
          new NodeId(UShort.MIN, uint(27604));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_Suppress2_InputArguments =
          new NodeId(UShort.MIN, uint(27605));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_Unsuppress =
          new NodeId(UShort.MIN, uint(27606));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_Unsuppress2 =
          new NodeId(UShort.MIN, uint(27607));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_Unsuppress2_InputArguments =
          new NodeId(UShort.MIN, uint(27608));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_RemoveFromService =
          new NodeId(UShort.MIN, uint(27609));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_RemoveFromService2 =
          new NodeId(UShort.MIN, uint(27610));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_RemoveFromService2_InputArguments =
          new NodeId(UShort.MIN, uint(27611));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_PlaceInService =
          new NodeId(UShort.MIN, uint(27612));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_PlaceInService2 =
          new NodeId(UShort.MIN, uint(27613));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_PlaceInService2_InputArguments =
          new NodeId(UShort.MIN, uint(27614));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_Reset =
          new NodeId(UShort.MIN, uint(27615));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_Reset2 =
          new NodeId(UShort.MIN, uint(27616));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_Reset2_InputArguments =
          new NodeId(UShort.MIN, uint(27617));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_GetGroupMemberships =
          new NodeId(UShort.MIN, uint(27618));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_GetGroupMemberships_OutputArguments =
          new NodeId(UShort.MIN, uint(27619));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_NormalState =
          new NodeId(UShort.MIN, uint(27620));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_TrustListId =
          new NodeId(UShort.MIN, uint(27621));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_LastUpdateTime =
          new NodeId(UShort.MIN, uint(27622));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_UpdateFrequency =
          new NodeId(UShort.MIN, uint(27623));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup =
          new NodeId(UShort.MIN, uint(27624));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustList =
          new NodeId(UShort.MIN, uint(27625));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustList_Size =
          new NodeId(UShort.MIN, uint(27626));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustList_Writable =
          new NodeId(UShort.MIN, uint(27627));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustList_UserWritable =
          new NodeId(UShort.MIN, uint(27628));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustList_OpenCount =
          new NodeId(UShort.MIN, uint(27629));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustList_MimeType =
          new NodeId(UShort.MIN, uint(27630));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustList_MaxByteStringLength =
          new NodeId(UShort.MIN, uint(27631));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustList_LastModifiedTime =
          new NodeId(UShort.MIN, uint(27632));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustList_Open =
          new NodeId(UShort.MIN, uint(27633));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustList_Open_InputArguments =
          new NodeId(UShort.MIN, uint(27634));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustList_Open_OutputArguments =
          new NodeId(UShort.MIN, uint(27635));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustList_Close =
          new NodeId(UShort.MIN, uint(27636));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustList_Close_InputArguments =
          new NodeId(UShort.MIN, uint(27637));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustList_Read =
          new NodeId(UShort.MIN, uint(27638));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustList_Read_InputArguments =
          new NodeId(UShort.MIN, uint(27639));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustList_Read_OutputArguments =
          new NodeId(UShort.MIN, uint(27640));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustList_Write =
          new NodeId(UShort.MIN, uint(27641));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustList_Write_InputArguments =
          new NodeId(UShort.MIN, uint(27642));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustList_GetPosition =
          new NodeId(UShort.MIN, uint(27643));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustList_GetPosition_InputArguments =
          new NodeId(UShort.MIN, uint(27644));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustList_GetPosition_OutputArguments =
          new NodeId(UShort.MIN, uint(27645));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustList_SetPosition =
          new NodeId(UShort.MIN, uint(27646));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustList_SetPosition_InputArguments =
          new NodeId(UShort.MIN, uint(27647));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustList_LastUpdateTime =
          new NodeId(UShort.MIN, uint(27648));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustList_UpdateFrequency =
          new NodeId(UShort.MIN, uint(27649));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustList_DefaultValidationOptions =
          new NodeId(UShort.MIN, uint(27650));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustList_OpenWithMasks =
          new NodeId(UShort.MIN, uint(27651));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustList_OpenWithMasks_InputArguments =
          new NodeId(UShort.MIN, uint(27652));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustList_OpenWithMasks_OutputArguments =
          new NodeId(UShort.MIN, uint(27653));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustList_CloseAndUpdate =
          new NodeId(UShort.MIN, uint(27654));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustList_CloseAndUpdate_InputArguments =
          new NodeId(UShort.MIN, uint(27655));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustList_CloseAndUpdate_OutputArguments =
          new NodeId(UShort.MIN, uint(27656));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustList_AddCertificate =
          new NodeId(UShort.MIN, uint(27657));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustList_AddCertificate_InputArguments =
          new NodeId(UShort.MIN, uint(27658));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustList_RemoveCertificate =
          new NodeId(UShort.MIN, uint(27659));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustList_RemoveCertificate_InputArguments =
          new NodeId(UShort.MIN, uint(27660));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateTypes =
          new NodeId(UShort.MIN, uint(27661));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_GetRejectedList =
          new NodeId(UShort.MIN, uint(27663));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_GetRejectedList_OutputArguments =
          new NodeId(UShort.MIN, uint(27664));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired =
          new NodeId(UShort.MIN, uint(27665));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_EventId =
          new NodeId(UShort.MIN, uint(27666));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_EventType =
          new NodeId(UShort.MIN, uint(27667));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_SourceNode =
          new NodeId(UShort.MIN, uint(27668));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_SourceName =
          new NodeId(UShort.MIN, uint(27669));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_Time =
          new NodeId(UShort.MIN, uint(27670));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_ReceiveTime =
          new NodeId(UShort.MIN, uint(27671));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_LocalTime =
          new NodeId(UShort.MIN, uint(27672));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_Message =
          new NodeId(UShort.MIN, uint(27673));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_Severity =
          new NodeId(UShort.MIN, uint(27674));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_ConditionClassId =
          new NodeId(UShort.MIN, uint(27675));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_ConditionClassName =
          new NodeId(UShort.MIN, uint(27676));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_ConditionSubClassId =
          new NodeId(UShort.MIN, uint(27677));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_ConditionSubClassName =
          new NodeId(UShort.MIN, uint(27678));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_ConditionName =
          new NodeId(UShort.MIN, uint(27679));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_BranchId =
          new NodeId(UShort.MIN, uint(27680));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_Retain =
          new NodeId(UShort.MIN, uint(27681));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_EnabledState =
          new NodeId(UShort.MIN, uint(27682));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_EnabledState_Id =
          new NodeId(UShort.MIN, uint(27683));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_EnabledState_Name =
          new NodeId(UShort.MIN, uint(27684));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_EnabledState_Number =
          new NodeId(UShort.MIN, uint(27685));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_EnabledState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(27686));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_EnabledState_TransitionTime =
          new NodeId(UShort.MIN, uint(27687));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_EnabledState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(27688));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_EnabledState_TrueState =
          new NodeId(UShort.MIN, uint(27689));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_EnabledState_FalseState =
          new NodeId(UShort.MIN, uint(27690));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_Quality =
          new NodeId(UShort.MIN, uint(27691));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_Quality_SourceTimestamp =
          new NodeId(UShort.MIN, uint(27692));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_LastSeverity =
          new NodeId(UShort.MIN, uint(27693));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_LastSeverity_SourceTimestamp =
          new NodeId(UShort.MIN, uint(27694));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_Comment =
          new NodeId(UShort.MIN, uint(27695));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_Comment_SourceTimestamp =
          new NodeId(UShort.MIN, uint(27696));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_ClientUserId =
          new NodeId(UShort.MIN, uint(27697));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_Disable =
          new NodeId(UShort.MIN, uint(27698));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_Enable =
          new NodeId(UShort.MIN, uint(27699));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_AddComment =
          new NodeId(UShort.MIN, uint(27700));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_AddComment_InputArguments =
          new NodeId(UShort.MIN, uint(27701));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_AckedState =
          new NodeId(UShort.MIN, uint(27702));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_AckedState_Id =
          new NodeId(UShort.MIN, uint(27703));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_AckedState_Name =
          new NodeId(UShort.MIN, uint(27704));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_AckedState_Number =
          new NodeId(UShort.MIN, uint(27705));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_AckedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(27706));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_AckedState_TransitionTime =
          new NodeId(UShort.MIN, uint(27707));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_AckedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(27708));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_AckedState_TrueState =
          new NodeId(UShort.MIN, uint(27709));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_AckedState_FalseState =
          new NodeId(UShort.MIN, uint(27710));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_ConfirmedState =
          new NodeId(UShort.MIN, uint(27711));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_ConfirmedState_Id =
          new NodeId(UShort.MIN, uint(27712));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_ConfirmedState_Name =
          new NodeId(UShort.MIN, uint(27713));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_ConfirmedState_Number =
          new NodeId(UShort.MIN, uint(27714));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_ConfirmedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(27715));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_ConfirmedState_TransitionTime =
          new NodeId(UShort.MIN, uint(27716));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_ConfirmedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(27717));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_ConfirmedState_TrueState =
          new NodeId(UShort.MIN, uint(27718));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_ConfirmedState_FalseState =
          new NodeId(UShort.MIN, uint(27719));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_Acknowledge =
          new NodeId(UShort.MIN, uint(27720));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_Acknowledge_InputArguments =
          new NodeId(UShort.MIN, uint(27721));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_Confirm =
          new NodeId(UShort.MIN, uint(27722));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_Confirm_InputArguments =
          new NodeId(UShort.MIN, uint(27723));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_ActiveState =
          new NodeId(UShort.MIN, uint(27724));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_ActiveState_Id =
          new NodeId(UShort.MIN, uint(27725));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_ActiveState_Name =
          new NodeId(UShort.MIN, uint(27726));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_ActiveState_Number =
          new NodeId(UShort.MIN, uint(27727));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_ActiveState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(27728));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_ActiveState_TransitionTime =
          new NodeId(UShort.MIN, uint(27729));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_ActiveState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(27730));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_ActiveState_TrueState =
          new NodeId(UShort.MIN, uint(27731));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_ActiveState_FalseState =
          new NodeId(UShort.MIN, uint(27732));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_InputNode =
          new NodeId(UShort.MIN, uint(27733));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_SuppressedState =
          new NodeId(UShort.MIN, uint(27734));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_SuppressedState_Id =
          new NodeId(UShort.MIN, uint(27735));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_SuppressedState_Name =
          new NodeId(UShort.MIN, uint(27736));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_SuppressedState_Number =
          new NodeId(UShort.MIN, uint(27737));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_SuppressedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(27738));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_SuppressedState_TransitionTime =
          new NodeId(UShort.MIN, uint(27739));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_SuppressedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(27740));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_SuppressedState_TrueState =
          new NodeId(UShort.MIN, uint(27741));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_SuppressedState_FalseState =
          new NodeId(UShort.MIN, uint(27742));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_OutOfServiceState =
          new NodeId(UShort.MIN, uint(27743));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_OutOfServiceState_Id =
          new NodeId(UShort.MIN, uint(27744));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_OutOfServiceState_Name =
          new NodeId(UShort.MIN, uint(27745));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_OutOfServiceState_Number =
          new NodeId(UShort.MIN, uint(27746));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_OutOfServiceState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(27747));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_OutOfServiceState_TransitionTime =
          new NodeId(UShort.MIN, uint(27748));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_OutOfServiceState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(27749));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_OutOfServiceState_TrueState =
          new NodeId(UShort.MIN, uint(27750));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_OutOfServiceState_FalseState =
          new NodeId(UShort.MIN, uint(27751));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_ShelvingState =
          new NodeId(UShort.MIN, uint(27752));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_ShelvingState_CurrentState =
          new NodeId(UShort.MIN, uint(27753));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_ShelvingState_CurrentState_Id =
          new NodeId(UShort.MIN, uint(27754));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_ShelvingState_CurrentState_Name =
          new NodeId(UShort.MIN, uint(27755));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_ShelvingState_CurrentState_Number =
          new NodeId(UShort.MIN, uint(27756));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_ShelvingState_CurrentState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(27757));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_ShelvingState_LastTransition =
          new NodeId(UShort.MIN, uint(27758));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_ShelvingState_LastTransition_Id =
          new NodeId(UShort.MIN, uint(27759));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_ShelvingState_LastTransition_Name =
          new NodeId(UShort.MIN, uint(27760));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_ShelvingState_LastTransition_Number =
          new NodeId(UShort.MIN, uint(27761));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_ShelvingState_LastTransition_TransitionTime =
          new NodeId(UShort.MIN, uint(27762));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_ShelvingState_LastTransition_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(27763));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_ShelvingState_AvailableStates =
          new NodeId(UShort.MIN, uint(27764));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_ShelvingState_AvailableTransitions =
          new NodeId(UShort.MIN, uint(27765));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_ShelvingState_UnshelveTime =
          new NodeId(UShort.MIN, uint(27766));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_ShelvingState_TimedShelve =
          new NodeId(UShort.MIN, uint(27767));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_ShelvingState_TimedShelve_InputArguments =
          new NodeId(UShort.MIN, uint(27768));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_ShelvingState_TimedShelve2 =
          new NodeId(UShort.MIN, uint(27769));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_ShelvingState_TimedShelve2_InputArguments =
          new NodeId(UShort.MIN, uint(27770));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_ShelvingState_Unshelve =
          new NodeId(UShort.MIN, uint(27771));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_ShelvingState_Unshelve2 =
          new NodeId(UShort.MIN, uint(27772));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_ShelvingState_Unshelve2_InputArguments =
          new NodeId(UShort.MIN, uint(27773));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_ShelvingState_OneShotShelve =
          new NodeId(UShort.MIN, uint(27774));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_ShelvingState_OneShotShelve2 =
          new NodeId(UShort.MIN, uint(27775));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_ShelvingState_OneShotShelve2_InputArguments =
          new NodeId(UShort.MIN, uint(27776));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_SuppressedOrShelved =
          new NodeId(UShort.MIN, uint(27777));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_MaxTimeShelved =
          new NodeId(UShort.MIN, uint(27778));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_AudibleEnabled =
          new NodeId(UShort.MIN, uint(27779));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_AudibleSound =
          new NodeId(UShort.MIN, uint(27780));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_AudibleSound_ListId =
          new NodeId(UShort.MIN, uint(27781));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_AudibleSound_AgencyId =
          new NodeId(UShort.MIN, uint(27782));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_AudibleSound_VersionId =
          new NodeId(UShort.MIN, uint(27783));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_SilenceState =
          new NodeId(UShort.MIN, uint(27784));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_SilenceState_Id =
          new NodeId(UShort.MIN, uint(27785));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_SilenceState_Name =
          new NodeId(UShort.MIN, uint(27786));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_SilenceState_Number =
          new NodeId(UShort.MIN, uint(27787));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_SilenceState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(27788));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_SilenceState_TransitionTime =
          new NodeId(UShort.MIN, uint(27789));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_SilenceState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(27790));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_SilenceState_TrueState =
          new NodeId(UShort.MIN, uint(27791));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_SilenceState_FalseState =
          new NodeId(UShort.MIN, uint(27792));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_OnDelay =
          new NodeId(UShort.MIN, uint(27793));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_OffDelay =
          new NodeId(UShort.MIN, uint(27794));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_FirstInGroupFlag =
          new NodeId(UShort.MIN, uint(27795));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_FirstInGroup =
          new NodeId(UShort.MIN, uint(27796));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_LatchedState =
          new NodeId(UShort.MIN, uint(27797));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_LatchedState_Id =
          new NodeId(UShort.MIN, uint(27798));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_LatchedState_Name =
          new NodeId(UShort.MIN, uint(27799));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_LatchedState_Number =
          new NodeId(UShort.MIN, uint(27800));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_LatchedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(27801));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_LatchedState_TransitionTime =
          new NodeId(UShort.MIN, uint(27802));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_LatchedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(27803));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_LatchedState_TrueState =
          new NodeId(UShort.MIN, uint(27804));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_LatchedState_FalseState =
          new NodeId(UShort.MIN, uint(27805));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_ReAlarmTime =
          new NodeId(UShort.MIN, uint(27806));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_ReAlarmRepeatCount =
          new NodeId(UShort.MIN, uint(27807));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_Silence =
          new NodeId(UShort.MIN, uint(27808));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_Suppress =
          new NodeId(UShort.MIN, uint(27809));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_Suppress2 =
          new NodeId(UShort.MIN, uint(27810));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_Suppress2_InputArguments =
          new NodeId(UShort.MIN, uint(27811));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_Unsuppress =
          new NodeId(UShort.MIN, uint(27812));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_Unsuppress2 =
          new NodeId(UShort.MIN, uint(27813));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_Unsuppress2_InputArguments =
          new NodeId(UShort.MIN, uint(27814));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_RemoveFromService =
          new NodeId(UShort.MIN, uint(27815));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_RemoveFromService2 =
          new NodeId(UShort.MIN, uint(27816));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_RemoveFromService2_InputArguments =
          new NodeId(UShort.MIN, uint(27817));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_PlaceInService =
          new NodeId(UShort.MIN, uint(27818));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_PlaceInService2 =
          new NodeId(UShort.MIN, uint(27819));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_PlaceInService2_InputArguments =
          new NodeId(UShort.MIN, uint(27820));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_Reset =
          new NodeId(UShort.MIN, uint(27821));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_Reset2 =
          new NodeId(UShort.MIN, uint(27822));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_Reset2_InputArguments =
          new NodeId(UShort.MIN, uint(27823));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_GetGroupMemberships =
          new NodeId(UShort.MIN, uint(27824));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_GetGroupMemberships_OutputArguments =
          new NodeId(UShort.MIN, uint(27825));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_NormalState =
          new NodeId(UShort.MIN, uint(27826));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_ExpirationDate =
          new NodeId(UShort.MIN, uint(27827));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_ExpirationLimit =
          new NodeId(UShort.MIN, uint(27828));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_CertificateType =
          new NodeId(UShort.MIN, uint(27829));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_Certificate =
          new NodeId(UShort.MIN, uint(27830));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate =
          new NodeId(UShort.MIN, uint(27831));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_EventId =
          new NodeId(UShort.MIN, uint(27832));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_EventType =
          new NodeId(UShort.MIN, uint(27833));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_SourceNode =
          new NodeId(UShort.MIN, uint(27834));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_SourceName =
          new NodeId(UShort.MIN, uint(27835));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_Time =
          new NodeId(UShort.MIN, uint(27836));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_ReceiveTime =
          new NodeId(UShort.MIN, uint(27837));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_LocalTime =
          new NodeId(UShort.MIN, uint(27838));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_Message =
          new NodeId(UShort.MIN, uint(27839));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_Severity =
          new NodeId(UShort.MIN, uint(27840));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_ConditionClassId =
          new NodeId(UShort.MIN, uint(27841));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_ConditionClassName =
          new NodeId(UShort.MIN, uint(27842));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_ConditionSubClassId =
          new NodeId(UShort.MIN, uint(27843));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_ConditionSubClassName =
          new NodeId(UShort.MIN, uint(27844));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_ConditionName =
          new NodeId(UShort.MIN, uint(27845));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_BranchId =
          new NodeId(UShort.MIN, uint(27846));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_Retain =
          new NodeId(UShort.MIN, uint(27847));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_EnabledState =
          new NodeId(UShort.MIN, uint(27848));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_EnabledState_Id =
          new NodeId(UShort.MIN, uint(27849));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_EnabledState_Name =
          new NodeId(UShort.MIN, uint(27850));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_EnabledState_Number =
          new NodeId(UShort.MIN, uint(27851));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_EnabledState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(27852));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_EnabledState_TransitionTime =
          new NodeId(UShort.MIN, uint(27853));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_EnabledState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(27854));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_EnabledState_TrueState =
          new NodeId(UShort.MIN, uint(27855));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_EnabledState_FalseState =
          new NodeId(UShort.MIN, uint(27856));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_Quality =
          new NodeId(UShort.MIN, uint(27857));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_Quality_SourceTimestamp =
          new NodeId(UShort.MIN, uint(27858));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_LastSeverity =
          new NodeId(UShort.MIN, uint(27859));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_LastSeverity_SourceTimestamp =
          new NodeId(UShort.MIN, uint(27860));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_Comment =
          new NodeId(UShort.MIN, uint(27861));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_Comment_SourceTimestamp =
          new NodeId(UShort.MIN, uint(27862));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_ClientUserId =
          new NodeId(UShort.MIN, uint(27863));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_Disable =
          new NodeId(UShort.MIN, uint(27864));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_Enable =
          new NodeId(UShort.MIN, uint(27865));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_AddComment =
          new NodeId(UShort.MIN, uint(27866));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_AddComment_InputArguments =
          new NodeId(UShort.MIN, uint(27867));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_AckedState =
          new NodeId(UShort.MIN, uint(27868));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_AckedState_Id =
          new NodeId(UShort.MIN, uint(27869));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_AckedState_Name =
          new NodeId(UShort.MIN, uint(27870));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_AckedState_Number =
          new NodeId(UShort.MIN, uint(27871));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_AckedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(27872));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_AckedState_TransitionTime =
          new NodeId(UShort.MIN, uint(27873));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_AckedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(27874));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_AckedState_TrueState =
          new NodeId(UShort.MIN, uint(27875));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_AckedState_FalseState =
          new NodeId(UShort.MIN, uint(27876));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_ConfirmedState =
          new NodeId(UShort.MIN, uint(27877));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_ConfirmedState_Id =
          new NodeId(UShort.MIN, uint(27878));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_ConfirmedState_Name =
          new NodeId(UShort.MIN, uint(27879));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_ConfirmedState_Number =
          new NodeId(UShort.MIN, uint(27880));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_ConfirmedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(27881));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_ConfirmedState_TransitionTime =
          new NodeId(UShort.MIN, uint(27882));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_ConfirmedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(27883));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_ConfirmedState_TrueState =
          new NodeId(UShort.MIN, uint(27884));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_ConfirmedState_FalseState =
          new NodeId(UShort.MIN, uint(27885));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_Acknowledge =
          new NodeId(UShort.MIN, uint(27886));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_Acknowledge_InputArguments =
          new NodeId(UShort.MIN, uint(27887));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_Confirm =
          new NodeId(UShort.MIN, uint(27888));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_Confirm_InputArguments =
          new NodeId(UShort.MIN, uint(27889));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_ActiveState =
          new NodeId(UShort.MIN, uint(27890));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_ActiveState_Id =
          new NodeId(UShort.MIN, uint(27891));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_ActiveState_Name =
          new NodeId(UShort.MIN, uint(27892));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_ActiveState_Number =
          new NodeId(UShort.MIN, uint(27893));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_ActiveState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(27894));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_ActiveState_TransitionTime =
          new NodeId(UShort.MIN, uint(27895));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_ActiveState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(27896));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_ActiveState_TrueState =
          new NodeId(UShort.MIN, uint(27897));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_ActiveState_FalseState =
          new NodeId(UShort.MIN, uint(27898));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_InputNode =
          new NodeId(UShort.MIN, uint(27899));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_SuppressedState =
          new NodeId(UShort.MIN, uint(27900));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_SuppressedState_Id =
          new NodeId(UShort.MIN, uint(27901));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_SuppressedState_Name =
          new NodeId(UShort.MIN, uint(27902));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_SuppressedState_Number =
          new NodeId(UShort.MIN, uint(27903));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_SuppressedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(27904));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_SuppressedState_TransitionTime =
          new NodeId(UShort.MIN, uint(27905));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_SuppressedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(27906));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_SuppressedState_TrueState =
          new NodeId(UShort.MIN, uint(27907));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_SuppressedState_FalseState =
          new NodeId(UShort.MIN, uint(27908));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_OutOfServiceState =
          new NodeId(UShort.MIN, uint(27909));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_OutOfServiceState_Id =
          new NodeId(UShort.MIN, uint(27910));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_OutOfServiceState_Name =
          new NodeId(UShort.MIN, uint(27911));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_OutOfServiceState_Number =
          new NodeId(UShort.MIN, uint(27912));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_OutOfServiceState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(27913));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_OutOfServiceState_TransitionTime =
          new NodeId(UShort.MIN, uint(27914));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_OutOfServiceState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(27915));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_OutOfServiceState_TrueState =
          new NodeId(UShort.MIN, uint(27916));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_OutOfServiceState_FalseState =
          new NodeId(UShort.MIN, uint(27917));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_ShelvingState =
          new NodeId(UShort.MIN, uint(27918));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_ShelvingState_CurrentState =
          new NodeId(UShort.MIN, uint(27919));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_ShelvingState_CurrentState_Id =
          new NodeId(UShort.MIN, uint(27920));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_ShelvingState_CurrentState_Name =
          new NodeId(UShort.MIN, uint(27921));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_ShelvingState_CurrentState_Number =
          new NodeId(UShort.MIN, uint(27922));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_ShelvingState_CurrentState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(27923));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_ShelvingState_LastTransition =
          new NodeId(UShort.MIN, uint(27924));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_ShelvingState_LastTransition_Id =
          new NodeId(UShort.MIN, uint(27925));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_ShelvingState_LastTransition_Name =
          new NodeId(UShort.MIN, uint(27926));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_ShelvingState_LastTransition_Number =
          new NodeId(UShort.MIN, uint(27927));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_ShelvingState_LastTransition_TransitionTime =
          new NodeId(UShort.MIN, uint(27928));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_ShelvingState_LastTransition_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(27929));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_ShelvingState_AvailableStates =
          new NodeId(UShort.MIN, uint(27930));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_ShelvingState_AvailableTransitions =
          new NodeId(UShort.MIN, uint(27931));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_ShelvingState_UnshelveTime =
          new NodeId(UShort.MIN, uint(27932));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_ShelvingState_TimedShelve =
          new NodeId(UShort.MIN, uint(27933));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_ShelvingState_TimedShelve_InputArguments =
          new NodeId(UShort.MIN, uint(27934));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_ShelvingState_TimedShelve2 =
          new NodeId(UShort.MIN, uint(27935));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_ShelvingState_TimedShelve2_InputArguments =
          new NodeId(UShort.MIN, uint(27936));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_ShelvingState_Unshelve =
          new NodeId(UShort.MIN, uint(27937));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_ShelvingState_Unshelve2 =
          new NodeId(UShort.MIN, uint(27938));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_ShelvingState_Unshelve2_InputArguments =
          new NodeId(UShort.MIN, uint(27939));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_ShelvingState_OneShotShelve =
          new NodeId(UShort.MIN, uint(27940));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_ShelvingState_OneShotShelve2 =
          new NodeId(UShort.MIN, uint(27941));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_ShelvingState_OneShotShelve2_InputArguments =
          new NodeId(UShort.MIN, uint(27942));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_SuppressedOrShelved =
          new NodeId(UShort.MIN, uint(27943));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_MaxTimeShelved =
          new NodeId(UShort.MIN, uint(27944));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_AudibleEnabled =
          new NodeId(UShort.MIN, uint(27945));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_AudibleSound =
          new NodeId(UShort.MIN, uint(27946));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_AudibleSound_ListId =
          new NodeId(UShort.MIN, uint(27947));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_AudibleSound_AgencyId =
          new NodeId(UShort.MIN, uint(27948));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_AudibleSound_VersionId =
          new NodeId(UShort.MIN, uint(27949));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_SilenceState =
          new NodeId(UShort.MIN, uint(27950));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_SilenceState_Id =
          new NodeId(UShort.MIN, uint(27951));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_SilenceState_Name =
          new NodeId(UShort.MIN, uint(27952));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_SilenceState_Number =
          new NodeId(UShort.MIN, uint(27953));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_SilenceState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(27954));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_SilenceState_TransitionTime =
          new NodeId(UShort.MIN, uint(27955));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_SilenceState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(27956));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_SilenceState_TrueState =
          new NodeId(UShort.MIN, uint(27957));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_SilenceState_FalseState =
          new NodeId(UShort.MIN, uint(27958));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_OnDelay =
          new NodeId(UShort.MIN, uint(27959));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_OffDelay =
          new NodeId(UShort.MIN, uint(27960));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_FirstInGroupFlag =
          new NodeId(UShort.MIN, uint(27961));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_FirstInGroup =
          new NodeId(UShort.MIN, uint(27962));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_LatchedState =
          new NodeId(UShort.MIN, uint(27963));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_LatchedState_Id =
          new NodeId(UShort.MIN, uint(27964));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_LatchedState_Name =
          new NodeId(UShort.MIN, uint(27965));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_LatchedState_Number =
          new NodeId(UShort.MIN, uint(27966));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_LatchedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(27967));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_LatchedState_TransitionTime =
          new NodeId(UShort.MIN, uint(27968));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_LatchedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(27969));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_LatchedState_TrueState =
          new NodeId(UShort.MIN, uint(27970));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_LatchedState_FalseState =
          new NodeId(UShort.MIN, uint(27971));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_ReAlarmTime =
          new NodeId(UShort.MIN, uint(27972));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_ReAlarmRepeatCount =
          new NodeId(UShort.MIN, uint(27973));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_Silence =
          new NodeId(UShort.MIN, uint(27974));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_Suppress =
          new NodeId(UShort.MIN, uint(27975));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_Suppress2 =
          new NodeId(UShort.MIN, uint(27976));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_Suppress2_InputArguments =
          new NodeId(UShort.MIN, uint(27977));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_Unsuppress =
          new NodeId(UShort.MIN, uint(27978));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_Unsuppress2 =
          new NodeId(UShort.MIN, uint(27979));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_Unsuppress2_InputArguments =
          new NodeId(UShort.MIN, uint(27980));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_RemoveFromService =
          new NodeId(UShort.MIN, uint(27981));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_RemoveFromService2 =
          new NodeId(UShort.MIN, uint(27982));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_RemoveFromService2_InputArguments =
          new NodeId(UShort.MIN, uint(27983));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_PlaceInService =
          new NodeId(UShort.MIN, uint(27984));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_PlaceInService2 =
          new NodeId(UShort.MIN, uint(27985));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_PlaceInService2_InputArguments =
          new NodeId(UShort.MIN, uint(27986));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_Reset =
          new NodeId(UShort.MIN, uint(27987));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_Reset2 =
          new NodeId(UShort.MIN, uint(27988));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_Reset2_InputArguments =
          new NodeId(UShort.MIN, uint(27989));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_GetGroupMemberships =
          new NodeId(UShort.MIN, uint(27990));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_GetGroupMemberships_OutputArguments =
          new NodeId(UShort.MIN, uint(27991));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_NormalState =
          new NodeId(UShort.MIN, uint(27992));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_TrustListId =
          new NodeId(UShort.MIN, uint(27993));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_LastUpdateTime =
          new NodeId(UShort.MIN, uint(27994));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustListOutOfDate_UpdateFrequency =
          new NodeId(UShort.MIN, uint(27995));

  public static final NodeId ProvisionableDeviceType_ApplicationName_Placeholder_Enabled =
      new NodeId(UShort.MIN, uint(27996));

  public static final NodeId ProvisionableDeviceType_ApplicationName_Placeholder_ApplicationUri =
      new NodeId(UShort.MIN, uint(27997));

  public static final NodeId ProvisionableDeviceType_ApplicationName_Placeholder_ProductUri =
      new NodeId(UShort.MIN, uint(27998));

  public static final NodeId ProvisionableDeviceType_ApplicationName_Placeholder_ApplicationType =
      new NodeId(UShort.MIN, uint(27999));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_ServerCapabilities =
          new NodeId(UShort.MIN, uint(28000));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_SupportedPrivateKeyFormats =
          new NodeId(UShort.MIN, uint(28001));

  public static final NodeId ProvisionableDeviceType_ApplicationName_Placeholder_MaxTrustListSize =
      new NodeId(UShort.MIN, uint(28002));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_MulticastDnsEnabled =
          new NodeId(UShort.MIN, uint(28003));

  public static final NodeId ProvisionableDeviceType_ApplicationName_Placeholder_HasSecureElement =
      new NodeId(UShort.MIN, uint(28004));

  public static final NodeId ProvisionableDeviceType_ApplicationName_Placeholder_UpdateCertificate =
      new NodeId(UShort.MIN, uint(28005));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_UpdateCertificate_InputArguments =
          new NodeId(UShort.MIN, uint(28006));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_UpdateCertificate_OutputArguments =
          new NodeId(UShort.MIN, uint(28007));

  public static final NodeId ProvisionableDeviceType_ApplicationName_Placeholder_ApplyChanges =
      new NodeId(UShort.MIN, uint(28008));

  public static final NodeId ProvisionableDeviceType_ApplicationName_Placeholder_CancelChanges =
      new NodeId(UShort.MIN, uint(28009));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CreateSigningRequest =
          new NodeId(UShort.MIN, uint(28010));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CreateSigningRequest_InputArguments =
          new NodeId(UShort.MIN, uint(28011));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CreateSigningRequest_OutputArguments =
          new NodeId(UShort.MIN, uint(28012));

  public static final NodeId ProvisionableDeviceType_ApplicationName_Placeholder_GetRejectedList =
      new NodeId(UShort.MIN, uint(28013));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_GetRejectedList_OutputArguments =
          new NodeId(UShort.MIN, uint(28014));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_ResetToServerDefaults =
          new NodeId(UShort.MIN, uint(28015));

  public static final NodeId ProvisionableDevice = new NodeId(UShort.MIN, uint(29878));

  public static final NodeId ProvisionableDevice_IsSingleton = new NodeId(UShort.MIN, uint(29879));

  public static final NodeId ProvisionableDevice_RequestTickets =
      new NodeId(UShort.MIN, uint(29880));

  public static final NodeId ProvisionableDevice_RequestTickets_OutputArguments =
      new NodeId(UShort.MIN, uint(29881));

  public static final NodeId ProvisionableDevice_SetRegistrarEndpoints =
      new NodeId(UShort.MIN, uint(29882));

  public static final NodeId ProvisionableDevice_SetRegistrarEndpoints_InputArguments =
      new NodeId(UShort.MIN, uint(29883));

  public static final NodeId ServerType_ServerCapabilities_MaxMonitoredItemsQueueSize =
      new NodeId(UShort.MIN, uint(31769));

  public static final NodeId ServerCapabilitiesType_MaxMonitoredItemsQueueSize =
      new NodeId(UShort.MIN, uint(31770));

  public static final NodeId BaseEventType_ConditionClassId = new NodeId(UShort.MIN, uint(31771));

  public static final NodeId BaseEventType_ConditionClassName = new NodeId(UShort.MIN, uint(31772));

  public static final NodeId BaseEventType_ConditionSubClassId =
      new NodeId(UShort.MIN, uint(31773));

  public static final NodeId BaseEventType_ConditionSubClassName =
      new NodeId(UShort.MIN, uint(31774));

  public static final NodeId Locations = new NodeId(UShort.MIN, uint(31915));

  public static final NodeId Server_ServerCapabilities_MaxMonitoredItemsQueueSize =
      new NodeId(UShort.MIN, uint(31916));

  public static final NodeId Handle = new NodeId(UShort.MIN, uint(31917));

  public static final NodeId TrimmedString = new NodeId(UShort.MIN, uint(31918));

  public static final NodeId AlarmSuppressionGroupMember = new NodeId(UShort.MIN, uint(32059));

  public static final NodeId ConditionType_SupportsFilteredRetain =
      new NodeId(UShort.MIN, uint(32060));

  public static final NodeId AlarmSuppressionGroupType = new NodeId(UShort.MIN, uint(32064));

  public static final NodeId AlarmSuppressionGroupType_DigitalVariable_Placeholder =
      new NodeId(UShort.MIN, uint(32226));

  public static final NodeId AlarmStateVariableType = new NodeId(UShort.MIN, uint(32244));

  public static final NodeId AlarmStateVariableType_HighestActiveSeverity =
      new NodeId(UShort.MIN, uint(32245));

  public static final NodeId AlarmStateVariableType_HighestUnackSeverity =
      new NodeId(UShort.MIN, uint(32246));

  public static final NodeId AlarmStateVariableType_ActiveCount =
      new NodeId(UShort.MIN, uint(32247));

  public static final NodeId AlarmStateVariableType_UnacknowledgedCount =
      new NodeId(UShort.MIN, uint(32248));

  public static final NodeId AlarmStateVariableType_UnconfirmedCount =
      new NodeId(UShort.MIN, uint(32249));

  public static final NodeId AlarmStateVariableType_Filter = new NodeId(UShort.MIN, uint(32250));

  public static final NodeId AlarmMask = new NodeId(UShort.MIN, uint(32251));

  public static final NodeId AlarmMask_OptionSetValues = new NodeId(UShort.MIN, uint(32252));

  public static final NodeId TrustListType_ActivityTimeout = new NodeId(UShort.MIN, uint(32254));

  public static final NodeId CertificateGroupType_TrustList_ActivityTimeout =
      new NodeId(UShort.MIN, uint(32255));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustList_ActivityTimeout =
          new NodeId(UShort.MIN, uint(32256));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustList_ActivityTimeout =
          new NodeId(UShort.MIN, uint(32257));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustList_ActivityTimeout =
          new NodeId(UShort.MIN, uint(32258));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustList_ActivityTimeout =
          new NodeId(UShort.MIN, uint(32259));

  public static final NodeId TrustListUpdateRequestedAuditEventType =
      new NodeId(UShort.MIN, uint(32260));

  public static final NodeId TrustListUpdatedAuditEventType_TrustListId =
      new NodeId(UShort.MIN, uint(32281));

  public static final NodeId GetCertificatesMethodType = new NodeId(UShort.MIN, uint(32282));

  public static final NodeId GetCertificatesMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(32283));

  public static final NodeId GetCertificatesMethodType_OutputArguments =
      new NodeId(UShort.MIN, uint(32284));

  public static final NodeId TransactionErrorType = new NodeId(UShort.MIN, uint(32285));

  public static final NodeId TransactionDiagnosticsType = new NodeId(UShort.MIN, uint(32286));

  public static final NodeId TransactionDiagnosticsType_StartTime =
      new NodeId(UShort.MIN, uint(32287));

  public static final NodeId TransactionDiagnosticsType_EndTime =
      new NodeId(UShort.MIN, uint(32288));

  public static final NodeId TransactionDiagnosticsType_Result =
      new NodeId(UShort.MIN, uint(32289));

  public static final NodeId TransactionDiagnosticsType_AffectedTrustLists =
      new NodeId(UShort.MIN, uint(32290));

  public static final NodeId TransactionDiagnosticsType_AffectedCertificateGroups =
      new NodeId(UShort.MIN, uint(32291));

  public static final NodeId TransactionDiagnosticsType_Errors =
      new NodeId(UShort.MIN, uint(32292));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustList_ActivityTimeout =
          new NodeId(UShort.MIN, uint(32293));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustList_ActivityTimeout =
          new NodeId(UShort.MIN, uint(32294));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_TrustList_ActivityTimeout =
          new NodeId(UShort.MIN, uint(32295));

  public static final NodeId ServerConfigurationType_GetCertificates =
      new NodeId(UShort.MIN, uint(32296));

  public static final NodeId ServerConfigurationType_GetCertificates_InputArguments =
      new NodeId(UShort.MIN, uint(32297));

  public static final NodeId ServerConfigurationType_GetCertificates_OutputArguments =
      new NodeId(UShort.MIN, uint(32298));

  public static final NodeId ServerConfigurationType_TransactionDiagnostics =
      new NodeId(UShort.MIN, uint(32299));

  public static final NodeId ServerConfigurationType_TransactionDiagnostics_StartTime =
      new NodeId(UShort.MIN, uint(32300));

  public static final NodeId ServerConfigurationType_TransactionDiagnostics_EndTime =
      new NodeId(UShort.MIN, uint(32301));

  public static final NodeId ServerConfigurationType_TransactionDiagnostics_Result =
      new NodeId(UShort.MIN, uint(32302));

  public static final NodeId ServerConfigurationType_TransactionDiagnostics_AffectedTrustLists =
      new NodeId(UShort.MIN, uint(32303));

  public static final NodeId
      ServerConfigurationType_TransactionDiagnostics_AffectedCertificateGroups =
          new NodeId(UShort.MIN, uint(32304));

  public static final NodeId ServerConfigurationType_TransactionDiagnostics_Errors =
      new NodeId(UShort.MIN, uint(32305));

  public static final NodeId CertificateUpdateRequestedAuditEventType =
      new NodeId(UShort.MIN, uint(32306));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_ActivityTimeout =
          new NodeId(UShort.MIN, uint(32330));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultHttpsGroup_TrustList_ActivityTimeout =
          new NodeId(UShort.MIN, uint(32331));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultUserTokenGroup_TrustList_ActivityTimeout =
          new NodeId(UShort.MIN, uint(32332));

  public static final NodeId ServerConfiguration_GetCertificates =
      new NodeId(UShort.MIN, uint(32333));

  public static final NodeId ServerConfiguration_GetCertificates_InputArguments =
      new NodeId(UShort.MIN, uint(32334));

  public static final NodeId ServerConfiguration_GetCertificates_OutputArguments =
      new NodeId(UShort.MIN, uint(32335));

  public static final NodeId ServerConfiguration_TransactionDiagnostics =
      new NodeId(UShort.MIN, uint(32336));

  public static final NodeId ServerConfiguration_TransactionDiagnostics_StartTime =
      new NodeId(UShort.MIN, uint(32337));

  public static final NodeId ServerConfiguration_TransactionDiagnostics_EndTime =
      new NodeId(UShort.MIN, uint(32338));

  public static final NodeId ServerConfiguration_TransactionDiagnostics_Result =
      new NodeId(UShort.MIN, uint(32339));

  public static final NodeId ServerConfiguration_TransactionDiagnostics_AffectedTrustLists =
      new NodeId(UShort.MIN, uint(32340));

  public static final NodeId ServerConfiguration_TransactionDiagnostics_AffectedCertificateGroups =
      new NodeId(UShort.MIN, uint(32341));

  public static final NodeId ServerConfiguration_TransactionDiagnostics_Errors =
      new NodeId(UShort.MIN, uint(32342));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustList_ActivityTimeout =
          new NodeId(UShort.MIN, uint(32356));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_TrustList_ActivityTimeout =
          new NodeId(UShort.MIN, uint(32357));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_TrustList_ActivityTimeout =
          new NodeId(UShort.MIN, uint(32358));

  public static final NodeId ProvisionableDeviceType_ApplicationName_Placeholder_GetCertificates =
      new NodeId(UShort.MIN, uint(32359));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_GetCertificates_InputArguments =
          new NodeId(UShort.MIN, uint(32360));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_GetCertificates_OutputArguments =
          new NodeId(UShort.MIN, uint(32361));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_TransactionDiagnostics =
          new NodeId(UShort.MIN, uint(32362));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_TransactionDiagnostics_StartTime =
          new NodeId(UShort.MIN, uint(32363));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_TransactionDiagnostics_EndTime =
          new NodeId(UShort.MIN, uint(32364));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_TransactionDiagnostics_Result =
          new NodeId(UShort.MIN, uint(32365));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_TransactionDiagnostics_AffectedTrustLists =
          new NodeId(UShort.MIN, uint(32366));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_TransactionDiagnostics_AffectedCertificateGroups =
          new NodeId(UShort.MIN, uint(32367));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_TransactionDiagnostics_Errors =
          new NodeId(UShort.MIN, uint(32368));

  public static final NodeId TransactionErrorType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(32382));

  public static final NodeId OpcUa_BinarySchema_TransactionErrorType =
      new NodeId(UShort.MIN, uint(32383));

  public static final NodeId OpcUa_BinarySchema_TransactionErrorType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(32384));

  public static final NodeId OpcUa_BinarySchema_TransactionErrorType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(32385));

  public static final NodeId TransactionErrorType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(32386));

  public static final NodeId OpcUa_XmlSchema_TransactionErrorType =
      new NodeId(UShort.MIN, uint(32387));

  public static final NodeId OpcUa_XmlSchema_TransactionErrorType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(32388));

  public static final NodeId OpcUa_XmlSchema_TransactionErrorType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(32389));

  public static final NodeId TransactionErrorType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(32390));

  public static final NodeId PublishSubscribeType_PubSubCapablities_MaxDataSetWritersPerGroup =
      new NodeId(UShort.MIN, uint(32391));

  public static final NodeId PublishSubscribeType_PubSubCapablities_MaxNetworkMessageSizeDatagram =
      new NodeId(UShort.MIN, uint(32392));

  public static final NodeId PublishSubscribeType_PubSubCapablities_MaxNetworkMessageSizeBroker =
      new NodeId(UShort.MIN, uint(32393));

  public static final NodeId PublishSubscribeType_PubSubCapablities_SupportSecurityKeyPull =
      new NodeId(UShort.MIN, uint(32394));

  public static final NodeId PublishSubscribeType_PubSubCapablities_SupportSecurityKeyPush =
      new NodeId(UShort.MIN, uint(32395));

  public static final NodeId PublishSubscribeType_DefaultSecurityKeyServices =
      new NodeId(UShort.MIN, uint(32396));

  public static final NodeId PublishSubscribeType_ConfigurationProperties =
      new NodeId(UShort.MIN, uint(32397));

  public static final NodeId PublishSubscribe_PubSubCapablities_MaxDataSetWritersPerGroup =
      new NodeId(UShort.MIN, uint(32398));

  public static final NodeId PublishSubscribe_PubSubCapablities_MaxNetworkMessageSizeDatagram =
      new NodeId(UShort.MIN, uint(32399));

  public static final NodeId PublishSubscribe_PubSubCapablities_MaxNetworkMessageSizeBroker =
      new NodeId(UShort.MIN, uint(32400));

  public static final NodeId PublishSubscribe_PubSubCapablities_SupportSecurityKeyPull =
      new NodeId(UShort.MIN, uint(32401));

  public static final NodeId PublishSubscribe_PubSubCapablities_SupportSecurityKeyPush =
      new NodeId(UShort.MIN, uint(32402));

  public static final NodeId PublishSubscribe_DefaultSecurityKeyServices =
      new NodeId(UShort.MIN, uint(32403));

  public static final NodeId PublishSubscribe_ConfigurationProperties =
      new NodeId(UShort.MIN, uint(32404));

  public static final NodeId HasKeyValueDescription = new NodeId(UShort.MIN, uint(32407));

  public static final NodeId OPCUANamespaceMetadata_ModelVersion =
      new NodeId(UShort.MIN, uint(32408));

  public static final NodeId ServerType_ServerRedundancy_RedundantServerArray =
      new NodeId(UShort.MIN, uint(32409));

  public static final NodeId ServerRedundancyType_RedundantServerArray =
      new NodeId(UShort.MIN, uint(32410));

  public static final NodeId NonTransparentBackupRedundancyType =
      new NodeId(UShort.MIN, uint(32411));

  public static final NodeId NonTransparentBackupRedundancyType_RedundantServerArray =
      new NodeId(UShort.MIN, uint(32413));

  public static final NodeId NonTransparentBackupRedundancyType_Mode =
      new NodeId(UShort.MIN, uint(32415));

  public static final NodeId NonTransparentBackupRedundancyType_Failover =
      new NodeId(UShort.MIN, uint(32416));

  public static final NodeId RedundantServerMode = new NodeId(UShort.MIN, uint(32417));

  public static final NodeId RedundantServerMode_EnumStrings = new NodeId(UShort.MIN, uint(32418));

  public static final NodeId NamespaceMetadataType_ModelVersion =
      new NodeId(UShort.MIN, uint(32419));

  public static final NodeId NamespacesType_NamespaceIdentifier_Placeholder_ModelVersion =
      new NodeId(UShort.MIN, uint(32420));

  public static final NodeId BitFieldDefinition = new NodeId(UShort.MIN, uint(32421));

  public static final NodeId BitFieldDefinition_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(32422));

  public static final NodeId OpcUa_BinarySchema_BitFieldDefinition =
      new NodeId(UShort.MIN, uint(32423));

  public static final NodeId OpcUa_BinarySchema_BitFieldDefinition_DataTypeVersion =
      new NodeId(UShort.MIN, uint(32424));

  public static final NodeId OpcUa_BinarySchema_BitFieldDefinition_DictionaryFragment =
      new NodeId(UShort.MIN, uint(32425));

  public static final NodeId BitFieldDefinition_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(32426));

  public static final NodeId OpcUa_XmlSchema_BitFieldDefinition =
      new NodeId(UShort.MIN, uint(32427));

  public static final NodeId OpcUa_XmlSchema_BitFieldDefinition_DataTypeVersion =
      new NodeId(UShort.MIN, uint(32428));

  public static final NodeId OpcUa_XmlSchema_BitFieldDefinition_DictionaryFragment =
      new NodeId(UShort.MIN, uint(32429));

  public static final NodeId BitFieldDefinition_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(32430));

  public static final NodeId BitFieldType = new NodeId(UShort.MIN, uint(32431));

  public static final NodeId BitFieldType_BitFieldsDefinitions =
      new NodeId(UShort.MIN, uint(32432));

  public static final NodeId BitFieldType_FieldName_Placeholder =
      new NodeId(UShort.MIN, uint(32433));

  public static final NodeId AnnotationDataType = new NodeId(UShort.MIN, uint(32434));

  public static final NodeId LinearConversionDataType = new NodeId(UShort.MIN, uint(32435));

  public static final NodeId ConversionLimitEnum = new NodeId(UShort.MIN, uint(32436));

  public static final NodeId ConversionLimitEnum_EnumStrings = new NodeId(UShort.MIN, uint(32437));

  public static final NodeId QuantityDimension = new NodeId(UShort.MIN, uint(32438));

  public static final NodeId SyntaxReferenceEntryType = new NodeId(UShort.MIN, uint(32439));

  public static final NodeId SyntaxReferenceEntryType_CommonName =
      new NodeId(UShort.MIN, uint(32441));

  public static final NodeId UnitType = new NodeId(UShort.MIN, uint(32442));

  public static final NodeId UnitType_Symbol = new NodeId(UShort.MIN, uint(32443));

  public static final NodeId UnitType_UnitSystem = new NodeId(UShort.MIN, uint(32445));

  public static final NodeId UnitType_Discipline = new NodeId(UShort.MIN, uint(32446));

  public static final NodeId ServerUnitType = new NodeId(UShort.MIN, uint(32447));

  public static final NodeId ServerUnitType_AlternativeUnits = new NodeId(UShort.MIN, uint(32452));

  public static final NodeId ServerUnitType_ConversionLimit = new NodeId(UShort.MIN, uint(32461));

  public static final NodeId ServerUnitType_CoherentUnit = new NodeId(UShort.MIN, uint(32462));

  public static final NodeId ServerUnitType_CoherentUnit_Symbol =
      new NodeId(UShort.MIN, uint(32463));

  public static final NodeId ServerUnitType_CoherentUnit_UnitSystem =
      new NodeId(UShort.MIN, uint(32465));

  public static final NodeId ServerUnitType_CoherentUnit_Discipline =
      new NodeId(UShort.MIN, uint(32466));

  public static final NodeId AlternativeUnitType = new NodeId(UShort.MIN, uint(32467));

  public static final NodeId AlternativeUnitType_LinearConversion =
      new NodeId(UShort.MIN, uint(32472));

  public static final NodeId AlternativeUnitType_MathMLConversion =
      new NodeId(UShort.MIN, uint(32473));

  public static final NodeId AlternativeUnitType_MathMLInverseConversion =
      new NodeId(UShort.MIN, uint(32474));

  public static final NodeId QuantityType = new NodeId(UShort.MIN, uint(32475));

  public static final NodeId QuantityType_Symbol = new NodeId(UShort.MIN, uint(32476));

  public static final NodeId QuantityType_Annotation = new NodeId(UShort.MIN, uint(32478));

  public static final NodeId QuantityType_ConversionService = new NodeId(UShort.MIN, uint(32479));

  public static final NodeId QuantityType_Dimension = new NodeId(UShort.MIN, uint(32480));

  public static final NodeId QuantityType_ServerUnits = new NodeId(UShort.MIN, uint(32481));

  public static final NodeId QuantityType_ServerUnits_ServerUnit_Placeholder =
      new NodeId(UShort.MIN, uint(32482));

  public static final NodeId QuantityType_ServerUnits_ServerUnit_Placeholder_Symbol =
      new NodeId(UShort.MIN, uint(32483));

  public static final NodeId QuantityType_ServerUnits_ServerUnit_Placeholder_UnitSystem =
      new NodeId(UShort.MIN, uint(32485));

  public static final NodeId QuantityType_ServerUnits_ServerUnit_Placeholder_Discipline =
      new NodeId(UShort.MIN, uint(32486));

  public static final NodeId QuantityType_ServerUnits_ServerUnit_Placeholder_AlternativeUnits =
      new NodeId(UShort.MIN, uint(32487));

  public static final NodeId QuantityType_ServerUnits_ServerUnit_Placeholder_ConversionLimit =
      new NodeId(UShort.MIN, uint(32496));

  public static final NodeId QuantityType_ServerUnits_ServerUnit_Placeholder_CoherentUnit =
      new NodeId(UShort.MIN, uint(32497));

  public static final NodeId QuantityType_ServerUnits_ServerUnit_Placeholder_CoherentUnit_Symbol =
      new NodeId(UShort.MIN, uint(32498));

  public static final NodeId
      QuantityType_ServerUnits_ServerUnit_Placeholder_CoherentUnit_UnitSystem =
          new NodeId(UShort.MIN, uint(32500));

  public static final NodeId
      QuantityType_ServerUnits_ServerUnit_Placeholder_CoherentUnit_Discipline =
          new NodeId(UShort.MIN, uint(32501));

  public static final NodeId QuantitiesFolderType = new NodeId(UShort.MIN, uint(32502));

  public static final NodeId QuantitiesFolderType_Quantity_Placeholder =
      new NodeId(UShort.MIN, uint(32503));

  public static final NodeId QuantitiesFolderType_Quantity_Placeholder_Symbol =
      new NodeId(UShort.MIN, uint(32504));

  public static final NodeId QuantitiesFolderType_Quantity_Placeholder_Annotation =
      new NodeId(UShort.MIN, uint(32506));

  public static final NodeId QuantitiesFolderType_Quantity_Placeholder_ConversionService =
      new NodeId(UShort.MIN, uint(32507));

  public static final NodeId QuantitiesFolderType_Quantity_Placeholder_Dimension =
      new NodeId(UShort.MIN, uint(32508));

  public static final NodeId QuantitiesFolderType_Quantity_Placeholder_ServerUnits =
      new NodeId(UShort.MIN, uint(32509));

  public static final NodeId Quantities = new NodeId(UShort.MIN, uint(32530));

  public static final NodeId HasEngineeringUnitDetails = new NodeId(UShort.MIN, uint(32558));

  public static final NodeId HasQuantity = new NodeId(UShort.MIN, uint(32559));

  public static final NodeId AnnotationDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(32560));

  public static final NodeId LinearConversionDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(32561));

  public static final NodeId QuantityDimension_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(32562));

  public static final NodeId OpcUa_BinarySchema_AnnotationDataType =
      new NodeId(UShort.MIN, uint(32563));

  public static final NodeId OpcUa_BinarySchema_AnnotationDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(32564));

  public static final NodeId OpcUa_BinarySchema_AnnotationDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(32565));

  public static final NodeId OpcUa_BinarySchema_LinearConversionDataType =
      new NodeId(UShort.MIN, uint(32566));

  public static final NodeId OpcUa_BinarySchema_LinearConversionDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(32567));

  public static final NodeId OpcUa_BinarySchema_LinearConversionDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(32568));

  public static final NodeId OpcUa_BinarySchema_QuantityDimension =
      new NodeId(UShort.MIN, uint(32569));

  public static final NodeId OpcUa_BinarySchema_QuantityDimension_DataTypeVersion =
      new NodeId(UShort.MIN, uint(32570));

  public static final NodeId OpcUa_BinarySchema_QuantityDimension_DictionaryFragment =
      new NodeId(UShort.MIN, uint(32571));

  public static final NodeId AnnotationDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(32572));

  public static final NodeId LinearConversionDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(32573));

  public static final NodeId QuantityDimension_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(32574));

  public static final NodeId OpcUa_XmlSchema_AnnotationDataType =
      new NodeId(UShort.MIN, uint(32575));

  public static final NodeId OpcUa_XmlSchema_AnnotationDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(32576));

  public static final NodeId OpcUa_XmlSchema_AnnotationDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(32577));

  public static final NodeId OpcUa_XmlSchema_LinearConversionDataType =
      new NodeId(UShort.MIN, uint(32578));

  public static final NodeId OpcUa_XmlSchema_LinearConversionDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(32579));

  public static final NodeId OpcUa_XmlSchema_LinearConversionDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(32580));

  public static final NodeId OpcUa_XmlSchema_QuantityDimension =
      new NodeId(UShort.MIN, uint(32581));

  public static final NodeId OpcUa_XmlSchema_QuantityDimension_DataTypeVersion =
      new NodeId(UShort.MIN, uint(32582));

  public static final NodeId OpcUa_XmlSchema_QuantityDimension_DictionaryFragment =
      new NodeId(UShort.MIN, uint(32583));

  public static final NodeId AnnotationDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(32584));

  public static final NodeId LinearConversionDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(32585));

  public static final NodeId QuantityDimension_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(32586));

  public static final NodeId ServerUnitType_AlternativeUnits_AlternativeUnit_Placeholder =
      new NodeId(UShort.MIN, uint(32587));

  public static final NodeId ServerUnitType_AlternativeUnits_AlternativeUnit_Placeholder_Symbol =
      new NodeId(UShort.MIN, uint(32588));

  public static final NodeId
      ServerUnitType_AlternativeUnits_AlternativeUnit_Placeholder_UnitSystem =
          new NodeId(UShort.MIN, uint(32590));

  public static final NodeId
      ServerUnitType_AlternativeUnits_AlternativeUnit_Placeholder_Discipline =
          new NodeId(UShort.MIN, uint(32591));

  public static final NodeId
      ServerUnitType_AlternativeUnits_AlternativeUnit_Placeholder_LinearConversion =
          new NodeId(UShort.MIN, uint(32592));

  public static final NodeId
      ServerUnitType_AlternativeUnits_AlternativeUnit_Placeholder_MathMLConversion =
          new NodeId(UShort.MIN, uint(32593));

  public static final NodeId
      ServerUnitType_AlternativeUnits_AlternativeUnit_Placeholder_MathMLInverseConversion =
          new NodeId(UShort.MIN, uint(32594));

  public static final NodeId HistoricalDataConfigurationType_MaxTimeStoredValues =
      new NodeId(UShort.MIN, uint(32619));

  public static final NodeId HistoricalDataConfigurationType_MaxCountStoredValues =
      new NodeId(UShort.MIN, uint(32620));

  public static final NodeId HistoricalEventConfigurationType = new NodeId(UShort.MIN, uint(32621));

  public static final NodeId HistoricalEventConfigurationType_EventTypes =
      new NodeId(UShort.MIN, uint(32622));

  public static final NodeId HistoricalEventConfigurationType_StartOfArchive =
      new NodeId(UShort.MIN, uint(32623));

  public static final NodeId HistoricalEventConfigurationType_StartOfOnlineArchive =
      new NodeId(UShort.MIN, uint(32624));

  public static final NodeId HistoricalExternalEventSourceType =
      new NodeId(UShort.MIN, uint(32625));

  public static final NodeId HistoricalExternalEventSourceType_Server =
      new NodeId(UShort.MIN, uint(32626));

  public static final NodeId HistoricalExternalEventSourceType_EndpointUrl =
      new NodeId(UShort.MIN, uint(32627));

  public static final NodeId HistoricalExternalEventSourceType_SecurityMode =
      new NodeId(UShort.MIN, uint(32628));

  public static final NodeId HistoricalExternalEventSourceType_SecurityPolicyUri =
      new NodeId(UShort.MIN, uint(32629));

  public static final NodeId HistoricalExternalEventSourceType_IdentityTokenPolicy =
      new NodeId(UShort.MIN, uint(32630));

  public static final NodeId HistoricalExternalEventSourceType_TransportProfileUri =
      new NodeId(UShort.MIN, uint(32631));

  public static final NodeId HistoricalExternalEventSourceType_HistoricalEventFilter =
      new NodeId(UShort.MIN, uint(32632));

  public static final NodeId HasCurrentData = new NodeId(UShort.MIN, uint(32633));

  public static final NodeId HasCurrentEvent = new NodeId(UShort.MIN, uint(32634));

  public static final NodeId HAConfiguration_MaxTimeStoredValues =
      new NodeId(UShort.MIN, uint(32635));

  public static final NodeId HAConfiguration_MaxCountStoredValues =
      new NodeId(UShort.MIN, uint(32636));

  public static final NodeId DefaultHAConfiguration = new NodeId(UShort.MIN, uint(32637));

  public static final NodeId DefaultHAConfiguration_AggregateConfiguration =
      new NodeId(UShort.MIN, uint(32638));

  public static final NodeId DefaultHAConfiguration_AggregateConfiguration_TreatUncertainAsBad =
      new NodeId(UShort.MIN, uint(32639));

  public static final NodeId DefaultHAConfiguration_AggregateConfiguration_PercentDataBad =
      new NodeId(UShort.MIN, uint(32640));

  public static final NodeId DefaultHAConfiguration_AggregateConfiguration_PercentDataGood =
      new NodeId(UShort.MIN, uint(32641));

  public static final NodeId DefaultHAConfiguration_AggregateConfiguration_UseSlopedExtrapolation =
      new NodeId(UShort.MIN, uint(32642));

  public static final NodeId DefaultHAConfiguration_AggregateFunctions =
      new NodeId(UShort.MIN, uint(32643));

  public static final NodeId DefaultHAConfiguration_Stepped = new NodeId(UShort.MIN, uint(32644));

  public static final NodeId DefaultHAConfiguration_Definition =
      new NodeId(UShort.MIN, uint(32645));

  public static final NodeId DefaultHAConfiguration_MaxTimeInterval =
      new NodeId(UShort.MIN, uint(32646));

  public static final NodeId DefaultHAConfiguration_MinTimeInterval =
      new NodeId(UShort.MIN, uint(32647));

  public static final NodeId DefaultHAConfiguration_ExceptionDeviation =
      new NodeId(UShort.MIN, uint(32648));

  public static final NodeId DefaultHAConfiguration_ExceptionDeviationFormat =
      new NodeId(UShort.MIN, uint(32649));

  public static final NodeId DefaultHAConfiguration_StartOfArchive =
      new NodeId(UShort.MIN, uint(32650));

  public static final NodeId PubSubCapabilitiesType_MaxDataSetWritersPerGroup =
      new NodeId(UShort.MIN, uint(32651));

  public static final NodeId PubSubCapabilitiesType_MaxNetworkMessageSizeDatagram =
      new NodeId(UShort.MIN, uint(32652));

  public static final NodeId PubSubCapabilitiesType_MaxNetworkMessageSizeBroker =
      new NodeId(UShort.MIN, uint(32653));

  public static final NodeId PubSubCapabilitiesType_SupportSecurityKeyPull =
      new NodeId(UShort.MIN, uint(32654));

  public static final NodeId PubSubCapabilitiesType_SupportSecurityKeyPush =
      new NodeId(UShort.MIN, uint(32655));

  public static final NodeId DefaultHAConfiguration_StartOfOnlineArchive =
      new NodeId(UShort.MIN, uint(32656));

  public static final NodeId ReferenceDescriptionVariableType = new NodeId(UShort.MIN, uint(32657));

  public static final NodeId ReferenceDescriptionVariableType_ReferenceRefinement =
      new NodeId(UShort.MIN, uint(32658));

  public static final NodeId ReferenceDescriptionDataType = new NodeId(UShort.MIN, uint(32659));

  public static final NodeId ReferenceListEntryDataType = new NodeId(UShort.MIN, uint(32660));

  public static final NodeId ReferenceDescriptionDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(32661));

  public static final NodeId ReferenceListEntryDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(32662));

  public static final NodeId OpcUa_BinarySchema_ReferenceDescriptionDataType =
      new NodeId(UShort.MIN, uint(32663));

  public static final NodeId OpcUa_BinarySchema_ReferenceDescriptionDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(32664));

  public static final NodeId OpcUa_BinarySchema_ReferenceDescriptionDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(32665));

  public static final NodeId OpcUa_BinarySchema_ReferenceListEntryDataType =
      new NodeId(UShort.MIN, uint(32666));

  public static final NodeId OpcUa_BinarySchema_ReferenceListEntryDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(32667));

  public static final NodeId OpcUa_BinarySchema_ReferenceListEntryDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(32668));

  public static final NodeId ReferenceDescriptionDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(32669));

  public static final NodeId ReferenceListEntryDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(32670));

  public static final NodeId OpcUa_XmlSchema_ReferenceDescriptionDataType =
      new NodeId(UShort.MIN, uint(32671));

  public static final NodeId OpcUa_XmlSchema_ReferenceDescriptionDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(32672));

  public static final NodeId OpcUa_XmlSchema_ReferenceDescriptionDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(32673));

  public static final NodeId OpcUa_XmlSchema_ReferenceListEntryDataType =
      new NodeId(UShort.MIN, uint(32674));

  public static final NodeId OpcUa_XmlSchema_ReferenceListEntryDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(32675));

  public static final NodeId OpcUa_XmlSchema_ReferenceListEntryDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(32676));

  public static final NodeId ReferenceDescriptionDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(32677));

  public static final NodeId ReferenceListEntryDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(32678));

  public static final NodeId HasReferenceDescription = new NodeId(UShort.MIN, uint(32679));

  public static final NodeId DefaultHAConfiguration_ServerTimestampSupported =
      new NodeId(UShort.MIN, uint(32682));

  public static final NodeId OptionSetLength = new NodeId(UShort.MIN, uint(32750));

  public static final NodeId DefaultHAConfiguration_MaxTimeStoredValues =
      new NodeId(UShort.MIN, uint(32752));

  public static final NodeId DefaultHAConfiguration_MaxCountStoredValues =
      new NodeId(UShort.MIN, uint(32753));

  public static final NodeId DefaultHEConfiguration = new NodeId(UShort.MIN, uint(32754));

  public static final NodeId DefaultHEConfiguration_EventTypes =
      new NodeId(UShort.MIN, uint(32755));

  public static final NodeId DefaultHEConfiguration_StartOfArchive =
      new NodeId(UShort.MIN, uint(32756));

  public static final NodeId DefaultHEConfiguration_StartOfOnlineArchive =
      new NodeId(UShort.MIN, uint(32757));

  public static final NodeId AuditHistoryConfigurationChangeEventType =
      new NodeId(UShort.MIN, uint(32758));

  public static final NodeId ReadEventDetails2 = new NodeId(UShort.MIN, uint(32799));

  public static final NodeId ReadEventDetails2_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(32800));

  public static final NodeId ReadEventDetails2_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(32801));

  public static final NodeId ReadEventDetails2_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(32802));

  public static final NodeId AuditHistoryBulkInsertEventType = new NodeId(UShort.MIN, uint(32803));

  public static final NodeId AuditHistoryBulkInsertEventType_UpdatedNode =
      new NodeId(UShort.MIN, uint(32821));

  public static final NodeId AuditHistoryBulkInsertEventType_StartTime =
      new NodeId(UShort.MIN, uint(32822));

  public static final NodeId AuditHistoryBulkInsertEventType_EndTime =
      new NodeId(UShort.MIN, uint(32823));

  public static final NodeId HistoryModifiedEvent = new NodeId(UShort.MIN, uint(32824));

  public static final NodeId HistoryModifiedEvent_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(32825));

  public static final NodeId OpcUa_BinarySchema_HistoryModifiedEvent =
      new NodeId(UShort.MIN, uint(32826));

  public static final NodeId OpcUa_BinarySchema_HistoryModifiedEvent_DataTypeVersion =
      new NodeId(UShort.MIN, uint(32827));

  public static final NodeId OpcUa_BinarySchema_HistoryModifiedEvent_DictionaryFragment =
      new NodeId(UShort.MIN, uint(32828));

  public static final NodeId HistoryModifiedEvent_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(32829));

  public static final NodeId OpcUa_XmlSchema_HistoryModifiedEvent =
      new NodeId(UShort.MIN, uint(32830));

  public static final NodeId OpcUa_XmlSchema_HistoryModifiedEvent_DataTypeVersion =
      new NodeId(UShort.MIN, uint(32831));

  public static final NodeId OpcUa_XmlSchema_HistoryModifiedEvent_DictionaryFragment =
      new NodeId(UShort.MIN, uint(32832));

  public static final NodeId HistoryModifiedEvent_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(32833));

  public static final NodeId PublishSubscribeType_PubSubCapablities_MaxSecurityGroups =
      new NodeId(UShort.MIN, uint(32834));

  public static final NodeId PublishSubscribeType_PubSubCapablities_MaxPushTargets =
      new NodeId(UShort.MIN, uint(32835));

  public static final NodeId PublishSubscribeType_PubSubCapablities_MaxPublishedDataSets =
      new NodeId(UShort.MIN, uint(32836));

  public static final NodeId
      PublishSubscribeType_PubSubCapablities_MaxStandaloneSubscribedDataSets =
          new NodeId(UShort.MIN, uint(32837));

  public static final NodeId PublishSubscribeType_PubSubCapablities_SupportSecurityKeyServer =
      new NodeId(UShort.MIN, uint(32838));

  public static final NodeId PublishSubscribe_PubSubCapablities_MaxSecurityGroups =
      new NodeId(UShort.MIN, uint(32839));

  public static final NodeId PublishSubscribe_PubSubCapablities_MaxPushTargets =
      new NodeId(UShort.MIN, uint(32840));

  public static final NodeId PublishSubscribe_PubSubCapablities_MaxPublishedDataSets =
      new NodeId(UShort.MIN, uint(32841));

  public static final NodeId PublishSubscribe_PubSubCapablities_MaxStandaloneSubscribedDataSets =
      new NodeId(UShort.MIN, uint(32842));

  public static final NodeId PublishSubscribe_PubSubCapablities_SupportSecurityKeyServer =
      new NodeId(UShort.MIN, uint(32843));

  public static final NodeId PubSubCapabilitiesType_MaxSecurityGroups =
      new NodeId(UShort.MIN, uint(32844));

  public static final NodeId PubSubCapabilitiesType_MaxPushTargets =
      new NodeId(UShort.MIN, uint(32845));

  public static final NodeId PubSubCapabilitiesType_MaxPublishedDataSets =
      new NodeId(UShort.MIN, uint(32846));

  public static final NodeId PubSubCapabilitiesType_MaxStandaloneSubscribedDataSets =
      new NodeId(UShort.MIN, uint(32847));

  public static final NodeId PubSubCapabilitiesType_SupportSecurityKeyServer =
      new NodeId(UShort.MIN, uint(32848));

  public static final NodeId AliasNameCategoryType_SubAliasNameCategories_Placeholder_LastChange =
      new NodeId(UShort.MIN, uint(32849));

  public static final NodeId AliasNameCategoryType_LastChange = new NodeId(UShort.MIN, uint(32850));

  public static final NodeId Aliases_LastChange = new NodeId(UShort.MIN, uint(32852));

  public static final NodeId TagVariables_LastChange = new NodeId(UShort.MIN, uint(32854));

  public static final NodeId Topics_LastChange = new NodeId(UShort.MIN, uint(32856));
}

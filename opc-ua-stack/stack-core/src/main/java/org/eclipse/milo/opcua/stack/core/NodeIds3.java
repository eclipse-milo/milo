/*
 * Copyright (c) 2026 the Eclipse Milo Authors
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
  public static final NodeId CreateSelfSignedCertificateMethodType_OutputArguments =
      new NodeId(UShort.MIN, uint(19329L));

  public static final NodeId DeleteCertificateMethodType = new NodeId(UShort.MIN, uint(19330L));

  public static final NodeId DeleteCertificateMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(19331L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CreateSelfSignedCertificate =
          new NodeId(UShort.MIN, uint(19332L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CreateSelfSignedCertificate_InputArguments =
          new NodeId(UShort.MIN, uint(19333L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CreateSelfSignedCertificate_OutputArguments =
          new NodeId(UShort.MIN, uint(19334L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_DeleteCertificate =
          new NodeId(UShort.MIN, uint(19335L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_DeleteCertificate_InputArguments =
          new NodeId(UShort.MIN, uint(19336L));

  public static final NodeId ServerConfigurationType_CreateSelfSignedCertificate =
      new NodeId(UShort.MIN, uint(19337L));

  public static final NodeId ServerConfigurationType_CreateSelfSignedCertificate_InputArguments =
      new NodeId(UShort.MIN, uint(19338L));

  public static final NodeId ServerConfigurationType_CreateSelfSignedCertificate_OutputArguments =
      new NodeId(UShort.MIN, uint(19339L));

  public static final NodeId ServerConfigurationType_DeleteCertificate =
      new NodeId(UShort.MIN, uint(19340L));

  public static final NodeId ServerConfigurationType_DeleteCertificate_InputArguments =
      new NodeId(UShort.MIN, uint(19341L));

  public static final NodeId ServerConfiguration_CreateSelfSignedCertificate =
      new NodeId(UShort.MIN, uint(19342L));

  public static final NodeId ServerConfiguration_CreateSelfSignedCertificate_InputArguments =
      new NodeId(UShort.MIN, uint(19343L));

  public static final NodeId ServerConfiguration_CreateSelfSignedCertificate_OutputArguments =
      new NodeId(UShort.MIN, uint(19344L));

  public static final NodeId ServerConfiguration_DeleteCertificate =
      new NodeId(UShort.MIN, uint(19345L));

  public static final NodeId ServerConfiguration_DeleteCertificate_InputArguments =
      new NodeId(UShort.MIN, uint(19346L));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CreateSelfSignedCertificate =
          new NodeId(UShort.MIN, uint(19347L));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CreateSelfSignedCertificate_InputArguments =
          new NodeId(UShort.MIN, uint(19348L));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CreateSelfSignedCertificate_OutputArguments =
          new NodeId(UShort.MIN, uint(19349L));

  public static final NodeId ProvisionableDeviceType_ApplicationName_Placeholder_DeleteCertificate =
      new NodeId(UShort.MIN, uint(19350L));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_DeleteCertificate_InputArguments =
          new NodeId(UShort.MIN, uint(19351L));

  public static final NodeId LogObjectType = new NodeId(UShort.MIN, uint(19352L));

  public static final NodeId LogObjectType_GetRecords = new NodeId(UShort.MIN, uint(19353L));

  public static final NodeId LogObjectType_GetRecords_InputArguments =
      new NodeId(UShort.MIN, uint(19354L));

  public static final NodeId LogObjectType_GetRecords_OutputArguments =
      new NodeId(UShort.MIN, uint(19355L));

  public static final NodeId LogObjectType_MaxRecords = new NodeId(UShort.MIN, uint(19356L));

  public static final NodeId LogObjectType_MaxStorageDuration =
      new NodeId(UShort.MIN, uint(19357L));

  public static final NodeId GetRecordsMethodType = new NodeId(UShort.MIN, uint(19358L));

  public static final NodeId GetRecordsMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(19359L));

  public static final NodeId GetRecordsMethodType_OutputArguments =
      new NodeId(UShort.MIN, uint(19360L));

  public static final NodeId LogRecord = new NodeId(UShort.MIN, uint(19361L));

  public static final NodeId BaseLogEventType = new NodeId(UShort.MIN, uint(19362L));

  public static final NodeId BaseLogEventType_ConditionClassId =
      new NodeId(UShort.MIN, uint(19363L));

  public static final NodeId BaseLogEventType_ConditionClassName =
      new NodeId(UShort.MIN, uint(19364L));

  public static final NodeId BaseLogEventType_ErrorCode = new NodeId(UShort.MIN, uint(19365L));

  public static final NodeId BaseLogEventType_ErrorCodeNode = new NodeId(UShort.MIN, uint(19366L));

  public static final NodeId LogOverflowEventType = new NodeId(UShort.MIN, uint(19369L));

  public static final NodeId LogEntryConditionClassType = new NodeId(UShort.MIN, uint(19370L));

  public static final NodeId ServerLog = new NodeId(UShort.MIN, uint(19372L));

  public static final NodeId ServerLog_GetRecords = new NodeId(UShort.MIN, uint(19373L));

  public static final NodeId ServerLog_GetRecords_InputArguments =
      new NodeId(UShort.MIN, uint(19374L));

  public static final NodeId ServerLog_GetRecords_OutputArguments =
      new NodeId(UShort.MIN, uint(19375L));

  public static final NodeId ServerLog_MaxRecords = new NodeId(UShort.MIN, uint(19376L));

  public static final NodeId ServerLog_MaxStorageDuration = new NodeId(UShort.MIN, uint(19377L));

  public static final NodeId Logs = new NodeId(UShort.MIN, uint(19378L));

  public static final NodeId LogRecord_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(19379L));

  public static final NodeId OpcUa_BinarySchema_LogRecord = new NodeId(UShort.MIN, uint(19380L));

  public static final NodeId OpcUa_BinarySchema_LogRecord_DataTypeVersion =
      new NodeId(UShort.MIN, uint(19381L));

  public static final NodeId OpcUa_BinarySchema_LogRecord_DictionaryFragment =
      new NodeId(UShort.MIN, uint(19382L));

  public static final NodeId LogRecord_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(19383L));

  public static final NodeId OpcUa_XmlSchema_LogRecord = new NodeId(UShort.MIN, uint(19384L));

  public static final NodeId OpcUa_XmlSchema_LogRecord_DataTypeVersion =
      new NodeId(UShort.MIN, uint(19385L));

  public static final NodeId OpcUa_XmlSchema_LogRecord_DictionaryFragment =
      new NodeId(UShort.MIN, uint(19386L));

  public static final NodeId LogRecord_Encoding_DefaultJson = new NodeId(UShort.MIN, uint(19387L));

  public static final NodeId CertificateGroupType_Purpose = new NodeId(UShort.MIN, uint(19398L));

  public static final NodeId CertificateGroupFolderType_DefaultApplicationGroup_Purpose =
      new NodeId(UShort.MIN, uint(19399L));

  public static final NodeId CertificateGroupFolderType_DefaultHttpsGroup_Purpose =
      new NodeId(UShort.MIN, uint(19400L));

  public static final NodeId CertificateGroupFolderType_DefaultUserTokenGroup_Purpose =
      new NodeId(UShort.MIN, uint(19401L));

  public static final NodeId CertificateGroupFolderType_AdditionalGroup_Placeholder_Purpose =
      new NodeId(UShort.MIN, uint(19402L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_Purpose =
          new NodeId(UShort.MIN, uint(19403L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_Purpose =
          new NodeId(UShort.MIN, uint(19404L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_Purpose =
          new NodeId(UShort.MIN, uint(19405L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_ConfigurationFile_MaxEndpoints =
          new NodeId(UShort.MIN, uint(19406L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_ConfigurationFile_MaxCertificateGroups =
          new NodeId(UShort.MIN, uint(19407L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_ConfigurationFile_CertificateGroupPurposes =
          new NodeId(UShort.MIN, uint(19408L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_KeyCredentials =
          new NodeId(UShort.MIN, uint(19409L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_KeyCredentials_CreateCredential =
          new NodeId(UShort.MIN, uint(19410L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_KeyCredentials_CreateCredential_InputArguments =
          new NodeId(UShort.MIN, uint(19411L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_KeyCredentials_CreateCredential_OutputArguments =
          new NodeId(UShort.MIN, uint(19412L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_AuthorizationServices =
          new NodeId(UShort.MIN, uint(19413L));

  public static final NodeId ApplicationConfigurationFileType_MaxEndpoints =
      new NodeId(UShort.MIN, uint(19414L));

  public static final NodeId ApplicationConfigurationFileType_MaxCertificateGroups =
      new NodeId(UShort.MIN, uint(19415L));

  public static final NodeId ApplicationConfigurationFileType_CertificateGroupPurposes =
      new NodeId(UShort.MIN, uint(19416L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_Purpose =
          new NodeId(UShort.MIN, uint(19417L));

  public static final NodeId ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_Purpose =
      new NodeId(UShort.MIN, uint(19418L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_Purpose =
          new NodeId(UShort.MIN, uint(19419L));

  public static final NodeId ServerConfigurationType_ConfigurationFile_MaxEndpoints =
      new NodeId(UShort.MIN, uint(19420L));

  public static final NodeId ServerConfigurationType_ConfigurationFile_MaxCertificateGroups =
      new NodeId(UShort.MIN, uint(19421L));

  public static final NodeId ServerConfigurationType_ConfigurationFile_CertificateGroupPurposes =
      new NodeId(UShort.MIN, uint(19422L));

  public static final NodeId ApplicationConfigurationType_KeyCredentials =
      new NodeId(UShort.MIN, uint(19423L));

  public static final NodeId ApplicationConfigurationType_KeyCredentials_CreateCredential =
      new NodeId(UShort.MIN, uint(19424L));

  public static final NodeId
      ApplicationConfigurationType_KeyCredentials_CreateCredential_InputArguments =
          new NodeId(UShort.MIN, uint(19425L));

  public static final NodeId
      ApplicationConfigurationType_KeyCredentials_CreateCredential_OutputArguments =
          new NodeId(UShort.MIN, uint(19426L));

  public static final NodeId ApplicationConfigurationType_AuthorizationServices =
      new NodeId(UShort.MIN, uint(19427L));

  public static final NodeId ServerConfiguration_CertificateGroups_DefaultApplicationGroup_Purpose =
      new NodeId(UShort.MIN, uint(19428L));

  public static final NodeId ServerConfiguration_CertificateGroups_DefaultHttpsGroup_Purpose =
      new NodeId(UShort.MIN, uint(19429L));

  public static final NodeId ServerConfiguration_CertificateGroups_DefaultUserTokenGroup_Purpose =
      new NodeId(UShort.MIN, uint(19430L));

  public static final NodeId ServerConfiguration_ConfigurationFile_MaxEndpoints =
      new NodeId(UShort.MIN, uint(19442L));

  public static final NodeId ServerConfiguration_ConfigurationFile_MaxCertificateGroups =
      new NodeId(UShort.MIN, uint(19443L));

  public static final NodeId ServerConfiguration_ConfigurationFile_CertificateGroupPurposes =
      new NodeId(UShort.MIN, uint(19444L));

  public static final NodeId TrustListOutOfDateAlarmType_TrustListId =
      new NodeId(UShort.MIN, uint(19446L));

  public static final NodeId TrustListOutOfDateAlarmType_LastUpdateTime =
      new NodeId(UShort.MIN, uint(19447L));

  public static final NodeId TrustListOutOfDateAlarmType_UpdateFrequency =
      new NodeId(UShort.MIN, uint(19448L));

  public static final NodeId CertificateGroupType_TrustList_UpdateFrequency =
      new NodeId(UShort.MIN, uint(19449L));

  public static final NodeId CertificateGroupType_CertificateExpired =
      new NodeId(UShort.MIN, uint(19450L));

  public static final NodeId CertificateGroupType_CertificateExpired_EventId =
      new NodeId(UShort.MIN, uint(19451L));

  public static final NodeId CertificateGroupType_CertificateExpired_EventType =
      new NodeId(UShort.MIN, uint(19452L));

  public static final NodeId CertificateGroupType_CertificateExpired_SourceNode =
      new NodeId(UShort.MIN, uint(19453L));

  public static final NodeId CertificateGroupType_CertificateExpired_SourceName =
      new NodeId(UShort.MIN, uint(19454L));

  public static final NodeId CertificateGroupType_CertificateExpired_Time =
      new NodeId(UShort.MIN, uint(19455L));

  public static final NodeId CertificateGroupType_CertificateExpired_ReceiveTime =
      new NodeId(UShort.MIN, uint(19456L));

  public static final NodeId CertificateGroupType_CertificateExpired_LocalTime =
      new NodeId(UShort.MIN, uint(19457L));

  public static final NodeId CertificateGroupType_CertificateExpired_Message =
      new NodeId(UShort.MIN, uint(19458L));

  public static final NodeId CertificateGroupType_CertificateExpired_Severity =
      new NodeId(UShort.MIN, uint(19459L));

  public static final NodeId CertificateGroupType_CertificateExpired_ConditionClassId =
      new NodeId(UShort.MIN, uint(19460L));

  public static final NodeId CertificateGroupType_CertificateExpired_ConditionClassName =
      new NodeId(UShort.MIN, uint(19461L));

  public static final NodeId CertificateGroupType_CertificateExpired_ConditionSubClassId =
      new NodeId(UShort.MIN, uint(19462L));

  public static final NodeId CertificateGroupType_CertificateExpired_ConditionSubClassName =
      new NodeId(UShort.MIN, uint(19463L));

  public static final NodeId CertificateGroupType_CertificateExpired_ConditionName =
      new NodeId(UShort.MIN, uint(19464L));

  public static final NodeId CertificateGroupType_CertificateExpired_BranchId =
      new NodeId(UShort.MIN, uint(19465L));

  public static final NodeId CertificateGroupType_CertificateExpired_Retain =
      new NodeId(UShort.MIN, uint(19466L));

  public static final NodeId CertificateGroupType_CertificateExpired_EnabledState =
      new NodeId(UShort.MIN, uint(19467L));

  public static final NodeId CertificateGroupType_CertificateExpired_EnabledState_Id =
      new NodeId(UShort.MIN, uint(19468L));

  public static final NodeId CertificateGroupType_CertificateExpired_EnabledState_Name =
      new NodeId(UShort.MIN, uint(19469L));

  public static final NodeId CertificateGroupType_CertificateExpired_EnabledState_Number =
      new NodeId(UShort.MIN, uint(19470L));

  public static final NodeId
      CertificateGroupType_CertificateExpired_EnabledState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(19471L));

  public static final NodeId CertificateGroupType_CertificateExpired_EnabledState_TransitionTime =
      new NodeId(UShort.MIN, uint(19472L));

  public static final NodeId
      CertificateGroupType_CertificateExpired_EnabledState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(19473L));

  public static final NodeId CertificateGroupType_CertificateExpired_EnabledState_TrueState =
      new NodeId(UShort.MIN, uint(19474L));

  public static final NodeId CertificateGroupType_CertificateExpired_EnabledState_FalseState =
      new NodeId(UShort.MIN, uint(19475L));

  public static final NodeId CertificateGroupType_CertificateExpired_Quality =
      new NodeId(UShort.MIN, uint(19476L));

  public static final NodeId CertificateGroupType_CertificateExpired_Quality_SourceTimestamp =
      new NodeId(UShort.MIN, uint(19477L));

  public static final NodeId CertificateGroupType_CertificateExpired_LastSeverity =
      new NodeId(UShort.MIN, uint(19478L));

  public static final NodeId CertificateGroupType_CertificateExpired_LastSeverity_SourceTimestamp =
      new NodeId(UShort.MIN, uint(19479L));

  public static final NodeId CertificateGroupType_CertificateExpired_Comment =
      new NodeId(UShort.MIN, uint(19480L));

  public static final NodeId CertificateGroupType_CertificateExpired_Comment_SourceTimestamp =
      new NodeId(UShort.MIN, uint(19481L));

  public static final NodeId CertificateGroupType_CertificateExpired_ClientUserId =
      new NodeId(UShort.MIN, uint(19482L));

  public static final NodeId CertificateGroupType_CertificateExpired_Disable =
      new NodeId(UShort.MIN, uint(19483L));

  public static final NodeId CertificateGroupType_CertificateExpired_Enable =
      new NodeId(UShort.MIN, uint(19484L));

  public static final NodeId CertificateGroupType_CertificateExpired_AddComment =
      new NodeId(UShort.MIN, uint(19485L));

  public static final NodeId CertificateGroupType_CertificateExpired_AddComment_InputArguments =
      new NodeId(UShort.MIN, uint(19486L));

  public static final NodeId CertificateGroupType_CertificateExpired_AckedState =
      new NodeId(UShort.MIN, uint(19487L));

  public static final NodeId CertificateGroupType_CertificateExpired_AckedState_Id =
      new NodeId(UShort.MIN, uint(19488L));

  public static final NodeId CertificateGroupType_CertificateExpired_AckedState_Name =
      new NodeId(UShort.MIN, uint(19489L));

  public static final NodeId CertificateGroupType_CertificateExpired_AckedState_Number =
      new NodeId(UShort.MIN, uint(19490L));

  public static final NodeId
      CertificateGroupType_CertificateExpired_AckedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(19491L));

  public static final NodeId CertificateGroupType_CertificateExpired_AckedState_TransitionTime =
      new NodeId(UShort.MIN, uint(19492L));

  public static final NodeId
      CertificateGroupType_CertificateExpired_AckedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(19493L));

  public static final NodeId CertificateGroupType_CertificateExpired_AckedState_TrueState =
      new NodeId(UShort.MIN, uint(19494L));

  public static final NodeId CertificateGroupType_CertificateExpired_AckedState_FalseState =
      new NodeId(UShort.MIN, uint(19495L));

  public static final NodeId CertificateGroupType_CertificateExpired_ConfirmedState =
      new NodeId(UShort.MIN, uint(19496L));

  public static final NodeId CertificateGroupType_CertificateExpired_ConfirmedState_Id =
      new NodeId(UShort.MIN, uint(19497L));

  public static final NodeId CertificateGroupType_CertificateExpired_ConfirmedState_Name =
      new NodeId(UShort.MIN, uint(19498L));

  public static final NodeId CertificateGroupType_CertificateExpired_ConfirmedState_Number =
      new NodeId(UShort.MIN, uint(19499L));

  public static final NodeId
      CertificateGroupType_CertificateExpired_ConfirmedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(19500L));

  public static final NodeId CertificateGroupType_CertificateExpired_ConfirmedState_TransitionTime =
      new NodeId(UShort.MIN, uint(19501L));

  public static final NodeId
      CertificateGroupType_CertificateExpired_ConfirmedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(19502L));

  public static final NodeId CertificateGroupType_CertificateExpired_ConfirmedState_TrueState =
      new NodeId(UShort.MIN, uint(19503L));

  public static final NodeId CertificateGroupType_CertificateExpired_ConfirmedState_FalseState =
      new NodeId(UShort.MIN, uint(19504L));

  public static final NodeId CertificateGroupType_CertificateExpired_Acknowledge =
      new NodeId(UShort.MIN, uint(19505L));

  public static final NodeId CertificateGroupType_CertificateExpired_Acknowledge_InputArguments =
      new NodeId(UShort.MIN, uint(19506L));

  public static final NodeId CertificateGroupType_CertificateExpired_Confirm =
      new NodeId(UShort.MIN, uint(19507L));

  public static final NodeId CertificateGroupType_CertificateExpired_Confirm_InputArguments =
      new NodeId(UShort.MIN, uint(19508L));

  public static final NodeId CertificateGroupType_CertificateExpired_ActiveState =
      new NodeId(UShort.MIN, uint(19509L));

  public static final NodeId CertificateGroupType_CertificateExpired_ActiveState_Id =
      new NodeId(UShort.MIN, uint(19510L));

  public static final NodeId CertificateGroupType_CertificateExpired_ActiveState_Name =
      new NodeId(UShort.MIN, uint(19511L));

  public static final NodeId CertificateGroupType_CertificateExpired_ActiveState_Number =
      new NodeId(UShort.MIN, uint(19512L));

  public static final NodeId
      CertificateGroupType_CertificateExpired_ActiveState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(19513L));

  public static final NodeId CertificateGroupType_CertificateExpired_ActiveState_TransitionTime =
      new NodeId(UShort.MIN, uint(19514L));

  public static final NodeId
      CertificateGroupType_CertificateExpired_ActiveState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(19515L));

  public static final NodeId CertificateGroupType_CertificateExpired_ActiveState_TrueState =
      new NodeId(UShort.MIN, uint(19516L));

  public static final NodeId CertificateGroupType_CertificateExpired_ActiveState_FalseState =
      new NodeId(UShort.MIN, uint(19517L));

  public static final NodeId CertificateGroupType_CertificateExpired_InputNode =
      new NodeId(UShort.MIN, uint(19518L));

  public static final NodeId CertificateGroupType_CertificateExpired_SuppressedState =
      new NodeId(UShort.MIN, uint(19519L));

  public static final NodeId CertificateGroupType_CertificateExpired_SuppressedState_Id =
      new NodeId(UShort.MIN, uint(19520L));

  public static final NodeId CertificateGroupType_CertificateExpired_SuppressedState_Name =
      new NodeId(UShort.MIN, uint(19521L));

  public static final NodeId CertificateGroupType_CertificateExpired_SuppressedState_Number =
      new NodeId(UShort.MIN, uint(19522L));

  public static final NodeId
      CertificateGroupType_CertificateExpired_SuppressedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(19523L));

  public static final NodeId
      CertificateGroupType_CertificateExpired_SuppressedState_TransitionTime =
          new NodeId(UShort.MIN, uint(19524L));

  public static final NodeId
      CertificateGroupType_CertificateExpired_SuppressedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(19525L));

  public static final NodeId CertificateGroupType_CertificateExpired_SuppressedState_TrueState =
      new NodeId(UShort.MIN, uint(19526L));

  public static final NodeId CertificateGroupType_CertificateExpired_SuppressedState_FalseState =
      new NodeId(UShort.MIN, uint(19527L));

  public static final NodeId CertificateGroupType_CertificateExpired_OutOfServiceState =
      new NodeId(UShort.MIN, uint(19528L));

  public static final NodeId CertificateGroupType_CertificateExpired_OutOfServiceState_Id =
      new NodeId(UShort.MIN, uint(19529L));

  public static final NodeId CertificateGroupType_CertificateExpired_OutOfServiceState_Name =
      new NodeId(UShort.MIN, uint(19530L));

  public static final NodeId CertificateGroupType_CertificateExpired_OutOfServiceState_Number =
      new NodeId(UShort.MIN, uint(19531L));

  public static final NodeId
      CertificateGroupType_CertificateExpired_OutOfServiceState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(19532L));

  public static final NodeId
      CertificateGroupType_CertificateExpired_OutOfServiceState_TransitionTime =
          new NodeId(UShort.MIN, uint(19533L));

  public static final NodeId
      CertificateGroupType_CertificateExpired_OutOfServiceState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(19534L));

  public static final NodeId CertificateGroupType_CertificateExpired_OutOfServiceState_TrueState =
      new NodeId(UShort.MIN, uint(19535L));

  public static final NodeId CertificateGroupType_CertificateExpired_OutOfServiceState_FalseState =
      new NodeId(UShort.MIN, uint(19536L));

  public static final NodeId CertificateGroupType_CertificateExpired_ShelvingState =
      new NodeId(UShort.MIN, uint(19537L));

  public static final NodeId CertificateGroupType_CertificateExpired_ShelvingState_CurrentState =
      new NodeId(UShort.MIN, uint(19538L));

  public static final NodeId CertificateGroupType_CertificateExpired_ShelvingState_CurrentState_Id =
      new NodeId(UShort.MIN, uint(19539L));

  public static final NodeId
      CertificateGroupType_CertificateExpired_ShelvingState_CurrentState_Name =
          new NodeId(UShort.MIN, uint(19540L));

  public static final NodeId
      CertificateGroupType_CertificateExpired_ShelvingState_CurrentState_Number =
          new NodeId(UShort.MIN, uint(19541L));

  public static final NodeId
      CertificateGroupType_CertificateExpired_ShelvingState_CurrentState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(19542L));

  public static final NodeId CertificateGroupType_CertificateExpired_ShelvingState_LastTransition =
      new NodeId(UShort.MIN, uint(19543L));

  public static final NodeId
      CertificateGroupType_CertificateExpired_ShelvingState_LastTransition_Id =
          new NodeId(UShort.MIN, uint(19544L));

  public static final NodeId
      CertificateGroupType_CertificateExpired_ShelvingState_LastTransition_Name =
          new NodeId(UShort.MIN, uint(19545L));

  public static final NodeId
      CertificateGroupType_CertificateExpired_ShelvingState_LastTransition_Number =
          new NodeId(UShort.MIN, uint(19546L));

  public static final NodeId
      CertificateGroupType_CertificateExpired_ShelvingState_LastTransition_TransitionTime =
          new NodeId(UShort.MIN, uint(19547L));

  public static final NodeId
      CertificateGroupType_CertificateExpired_ShelvingState_LastTransition_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(19548L));

  public static final NodeId CertificateGroupType_CertificateExpired_ShelvingState_AvailableStates =
      new NodeId(UShort.MIN, uint(19549L));

  public static final NodeId DataSetWriterType_Diagnostics = new NodeId(UShort.MIN, uint(19550L));

  public static final NodeId DataSetWriterType_Diagnostics_DiagnosticsLevel =
      new NodeId(UShort.MIN, uint(19551L));

  public static final NodeId DataSetWriterType_Diagnostics_TotalInformation =
      new NodeId(UShort.MIN, uint(19552L));

  public static final NodeId DataSetWriterType_Diagnostics_TotalInformation_Active =
      new NodeId(UShort.MIN, uint(19553L));

  public static final NodeId DataSetWriterType_Diagnostics_TotalInformation_Classification =
      new NodeId(UShort.MIN, uint(19554L));

  public static final NodeId DataSetWriterType_Diagnostics_TotalInformation_DiagnosticsLevel =
      new NodeId(UShort.MIN, uint(19555L));

  public static final NodeId DataSetWriterType_Diagnostics_TotalInformation_TimeFirstChange =
      new NodeId(UShort.MIN, uint(19556L));

  public static final NodeId DataSetWriterType_Diagnostics_TotalError =
      new NodeId(UShort.MIN, uint(19557L));

  public static final NodeId DataSetWriterType_Diagnostics_TotalError_Active =
      new NodeId(UShort.MIN, uint(19558L));

  public static final NodeId DataSetWriterType_Diagnostics_TotalError_Classification =
      new NodeId(UShort.MIN, uint(19559L));

  public static final NodeId DataSetWriterType_Diagnostics_TotalError_DiagnosticsLevel =
      new NodeId(UShort.MIN, uint(19560L));

  public static final NodeId DataSetWriterType_Diagnostics_TotalError_TimeFirstChange =
      new NodeId(UShort.MIN, uint(19561L));

  public static final NodeId DataSetWriterType_Diagnostics_Reset =
      new NodeId(UShort.MIN, uint(19562L));

  public static final NodeId DataSetWriterType_Diagnostics_SubError =
      new NodeId(UShort.MIN, uint(19563L));

  public static final NodeId DataSetWriterType_Diagnostics_Counters =
      new NodeId(UShort.MIN, uint(19564L));

  public static final NodeId DataSetWriterType_Diagnostics_Counters_StateError =
      new NodeId(UShort.MIN, uint(19565L));

  public static final NodeId DataSetWriterType_Diagnostics_Counters_StateError_Active =
      new NodeId(UShort.MIN, uint(19566L));

  public static final NodeId DataSetWriterType_Diagnostics_Counters_StateError_Classification =
      new NodeId(UShort.MIN, uint(19567L));

  public static final NodeId DataSetWriterType_Diagnostics_Counters_StateError_DiagnosticsLevel =
      new NodeId(UShort.MIN, uint(19568L));

  public static final NodeId DataSetWriterType_Diagnostics_Counters_StateError_TimeFirstChange =
      new NodeId(UShort.MIN, uint(19569L));

  public static final NodeId DataSetWriterType_Diagnostics_Counters_StateOperationalByMethod =
      new NodeId(UShort.MIN, uint(19570L));

  public static final NodeId
      DataSetWriterType_Diagnostics_Counters_StateOperationalByMethod_Active =
          new NodeId(UShort.MIN, uint(19571L));

  public static final NodeId
      DataSetWriterType_Diagnostics_Counters_StateOperationalByMethod_Classification =
          new NodeId(UShort.MIN, uint(19572L));

  public static final NodeId
      DataSetWriterType_Diagnostics_Counters_StateOperationalByMethod_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19573L));

  public static final NodeId
      DataSetWriterType_Diagnostics_Counters_StateOperationalByMethod_TimeFirstChange =
          new NodeId(UShort.MIN, uint(19574L));

  public static final NodeId DataSetWriterType_Diagnostics_Counters_StateOperationalByParent =
      new NodeId(UShort.MIN, uint(19575L));

  public static final NodeId
      DataSetWriterType_Diagnostics_Counters_StateOperationalByParent_Active =
          new NodeId(UShort.MIN, uint(19576L));

  public static final NodeId
      DataSetWriterType_Diagnostics_Counters_StateOperationalByParent_Classification =
          new NodeId(UShort.MIN, uint(19577L));

  public static final NodeId
      DataSetWriterType_Diagnostics_Counters_StateOperationalByParent_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19578L));

  public static final NodeId
      DataSetWriterType_Diagnostics_Counters_StateOperationalByParent_TimeFirstChange =
          new NodeId(UShort.MIN, uint(19579L));

  public static final NodeId DataSetWriterType_Diagnostics_Counters_StateOperationalFromError =
      new NodeId(UShort.MIN, uint(19580L));

  public static final NodeId
      DataSetWriterType_Diagnostics_Counters_StateOperationalFromError_Active =
          new NodeId(UShort.MIN, uint(19581L));

  public static final NodeId
      DataSetWriterType_Diagnostics_Counters_StateOperationalFromError_Classification =
          new NodeId(UShort.MIN, uint(19582L));

  public static final NodeId
      DataSetWriterType_Diagnostics_Counters_StateOperationalFromError_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19583L));

  public static final NodeId
      DataSetWriterType_Diagnostics_Counters_StateOperationalFromError_TimeFirstChange =
          new NodeId(UShort.MIN, uint(19584L));

  public static final NodeId DataSetWriterType_Diagnostics_Counters_StatePausedByParent =
      new NodeId(UShort.MIN, uint(19585L));

  public static final NodeId DataSetWriterType_Diagnostics_Counters_StatePausedByParent_Active =
      new NodeId(UShort.MIN, uint(19586L));

  public static final NodeId
      DataSetWriterType_Diagnostics_Counters_StatePausedByParent_Classification =
          new NodeId(UShort.MIN, uint(19587L));

  public static final NodeId
      DataSetWriterType_Diagnostics_Counters_StatePausedByParent_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19588L));

  public static final NodeId
      DataSetWriterType_Diagnostics_Counters_StatePausedByParent_TimeFirstChange =
          new NodeId(UShort.MIN, uint(19589L));

  public static final NodeId DataSetWriterType_Diagnostics_Counters_StateDisabledByMethod =
      new NodeId(UShort.MIN, uint(19590L));

  public static final NodeId DataSetWriterType_Diagnostics_Counters_StateDisabledByMethod_Active =
      new NodeId(UShort.MIN, uint(19591L));

  public static final NodeId
      DataSetWriterType_Diagnostics_Counters_StateDisabledByMethod_Classification =
          new NodeId(UShort.MIN, uint(19592L));

  public static final NodeId
      DataSetWriterType_Diagnostics_Counters_StateDisabledByMethod_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19593L));

  public static final NodeId
      DataSetWriterType_Diagnostics_Counters_StateDisabledByMethod_TimeFirstChange =
          new NodeId(UShort.MIN, uint(19594L));

  public static final NodeId DataSetWriterType_Diagnostics_LiveValues =
      new NodeId(UShort.MIN, uint(19595L));

  public static final NodeId DataSetWriterType_Diagnostics_Counters_FailedDataSetMessages =
      new NodeId(UShort.MIN, uint(19596L));

  public static final NodeId DataSetWriterType_Diagnostics_Counters_FailedDataSetMessages_Active =
      new NodeId(UShort.MIN, uint(19597L));

  public static final NodeId
      DataSetWriterType_Diagnostics_Counters_FailedDataSetMessages_Classification =
          new NodeId(UShort.MIN, uint(19598L));

  public static final NodeId
      DataSetWriterType_Diagnostics_Counters_FailedDataSetMessages_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19599L));

  public static final NodeId
      DataSetWriterType_Diagnostics_Counters_FailedDataSetMessages_TimeFirstChange =
          new NodeId(UShort.MIN, uint(19600L));

  public static final NodeId DataSetWriterType_Diagnostics_LiveValues_MessageSequenceNumber =
      new NodeId(UShort.MIN, uint(19601L));

  public static final NodeId
      DataSetWriterType_Diagnostics_LiveValues_MessageSequenceNumber_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19602L));

  public static final NodeId DataSetWriterType_Diagnostics_LiveValues_StatusCode =
      new NodeId(UShort.MIN, uint(19603L));

  public static final NodeId DataSetWriterType_Diagnostics_LiveValues_StatusCode_DiagnosticsLevel =
      new NodeId(UShort.MIN, uint(19604L));

  public static final NodeId DataSetWriterType_Diagnostics_LiveValues_MajorVersion =
      new NodeId(UShort.MIN, uint(19605L));

  public static final NodeId
      DataSetWriterType_Diagnostics_LiveValues_MajorVersion_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19606L));

  public static final NodeId DataSetWriterType_Diagnostics_LiveValues_MinorVersion =
      new NodeId(UShort.MIN, uint(19607L));

  public static final NodeId
      DataSetWriterType_Diagnostics_LiveValues_MinorVersion_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19608L));

  public static final NodeId DataSetReaderType_Diagnostics = new NodeId(UShort.MIN, uint(19609L));

  public static final NodeId DataSetReaderType_Diagnostics_DiagnosticsLevel =
      new NodeId(UShort.MIN, uint(19610L));

  public static final NodeId DataSetReaderType_Diagnostics_TotalInformation =
      new NodeId(UShort.MIN, uint(19611L));

  public static final NodeId DataSetReaderType_Diagnostics_TotalInformation_Active =
      new NodeId(UShort.MIN, uint(19612L));

  public static final NodeId DataSetReaderType_Diagnostics_TotalInformation_Classification =
      new NodeId(UShort.MIN, uint(19613L));

  public static final NodeId DataSetReaderType_Diagnostics_TotalInformation_DiagnosticsLevel =
      new NodeId(UShort.MIN, uint(19614L));

  public static final NodeId DataSetReaderType_Diagnostics_TotalInformation_TimeFirstChange =
      new NodeId(UShort.MIN, uint(19615L));

  public static final NodeId DataSetReaderType_Diagnostics_TotalError =
      new NodeId(UShort.MIN, uint(19616L));

  public static final NodeId DataSetReaderType_Diagnostics_TotalError_Active =
      new NodeId(UShort.MIN, uint(19617L));

  public static final NodeId DataSetReaderType_Diagnostics_TotalError_Classification =
      new NodeId(UShort.MIN, uint(19618L));

  public static final NodeId DataSetReaderType_Diagnostics_TotalError_DiagnosticsLevel =
      new NodeId(UShort.MIN, uint(19619L));

  public static final NodeId DataSetReaderType_Diagnostics_TotalError_TimeFirstChange =
      new NodeId(UShort.MIN, uint(19620L));

  public static final NodeId DataSetReaderType_Diagnostics_Reset =
      new NodeId(UShort.MIN, uint(19621L));

  public static final NodeId DataSetReaderType_Diagnostics_SubError =
      new NodeId(UShort.MIN, uint(19622L));

  public static final NodeId DataSetReaderType_Diagnostics_Counters =
      new NodeId(UShort.MIN, uint(19623L));

  public static final NodeId DataSetReaderType_Diagnostics_Counters_StateError =
      new NodeId(UShort.MIN, uint(19624L));

  public static final NodeId DataSetReaderType_Diagnostics_Counters_StateError_Active =
      new NodeId(UShort.MIN, uint(19625L));

  public static final NodeId DataSetReaderType_Diagnostics_Counters_StateError_Classification =
      new NodeId(UShort.MIN, uint(19626L));

  public static final NodeId DataSetReaderType_Diagnostics_Counters_StateError_DiagnosticsLevel =
      new NodeId(UShort.MIN, uint(19627L));

  public static final NodeId DataSetReaderType_Diagnostics_Counters_StateError_TimeFirstChange =
      new NodeId(UShort.MIN, uint(19628L));

  public static final NodeId DataSetReaderType_Diagnostics_Counters_StateOperationalByMethod =
      new NodeId(UShort.MIN, uint(19629L));

  public static final NodeId
      DataSetReaderType_Diagnostics_Counters_StateOperationalByMethod_Active =
          new NodeId(UShort.MIN, uint(19630L));

  public static final NodeId
      DataSetReaderType_Diagnostics_Counters_StateOperationalByMethod_Classification =
          new NodeId(UShort.MIN, uint(19631L));

  public static final NodeId
      DataSetReaderType_Diagnostics_Counters_StateOperationalByMethod_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19632L));

  public static final NodeId
      DataSetReaderType_Diagnostics_Counters_StateOperationalByMethod_TimeFirstChange =
          new NodeId(UShort.MIN, uint(19633L));

  public static final NodeId DataSetReaderType_Diagnostics_Counters_StateOperationalByParent =
      new NodeId(UShort.MIN, uint(19634L));

  public static final NodeId
      DataSetReaderType_Diagnostics_Counters_StateOperationalByParent_Active =
          new NodeId(UShort.MIN, uint(19635L));

  public static final NodeId
      DataSetReaderType_Diagnostics_Counters_StateOperationalByParent_Classification =
          new NodeId(UShort.MIN, uint(19636L));

  public static final NodeId
      DataSetReaderType_Diagnostics_Counters_StateOperationalByParent_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19637L));

  public static final NodeId
      DataSetReaderType_Diagnostics_Counters_StateOperationalByParent_TimeFirstChange =
          new NodeId(UShort.MIN, uint(19638L));

  public static final NodeId DataSetReaderType_Diagnostics_Counters_StateOperationalFromError =
      new NodeId(UShort.MIN, uint(19639L));

  public static final NodeId
      DataSetReaderType_Diagnostics_Counters_StateOperationalFromError_Active =
          new NodeId(UShort.MIN, uint(19640L));

  public static final NodeId
      DataSetReaderType_Diagnostics_Counters_StateOperationalFromError_Classification =
          new NodeId(UShort.MIN, uint(19641L));

  public static final NodeId
      DataSetReaderType_Diagnostics_Counters_StateOperationalFromError_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19642L));

  public static final NodeId
      DataSetReaderType_Diagnostics_Counters_StateOperationalFromError_TimeFirstChange =
          new NodeId(UShort.MIN, uint(19643L));

  public static final NodeId DataSetReaderType_Diagnostics_Counters_StatePausedByParent =
      new NodeId(UShort.MIN, uint(19644L));

  public static final NodeId DataSetReaderType_Diagnostics_Counters_StatePausedByParent_Active =
      new NodeId(UShort.MIN, uint(19645L));

  public static final NodeId
      DataSetReaderType_Diagnostics_Counters_StatePausedByParent_Classification =
          new NodeId(UShort.MIN, uint(19646L));

  public static final NodeId
      DataSetReaderType_Diagnostics_Counters_StatePausedByParent_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19647L));

  public static final NodeId
      DataSetReaderType_Diagnostics_Counters_StatePausedByParent_TimeFirstChange =
          new NodeId(UShort.MIN, uint(19648L));

  public static final NodeId DataSetReaderType_Diagnostics_Counters_StateDisabledByMethod =
      new NodeId(UShort.MIN, uint(19649L));

  public static final NodeId DataSetReaderType_Diagnostics_Counters_StateDisabledByMethod_Active =
      new NodeId(UShort.MIN, uint(19650L));

  public static final NodeId
      DataSetReaderType_Diagnostics_Counters_StateDisabledByMethod_Classification =
          new NodeId(UShort.MIN, uint(19651L));

  public static final NodeId
      DataSetReaderType_Diagnostics_Counters_StateDisabledByMethod_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19652L));

  public static final NodeId
      DataSetReaderType_Diagnostics_Counters_StateDisabledByMethod_TimeFirstChange =
          new NodeId(UShort.MIN, uint(19653L));

  public static final NodeId DataSetReaderType_Diagnostics_LiveValues =
      new NodeId(UShort.MIN, uint(19654L));

  public static final NodeId DataSetReaderType_Diagnostics_Counters_FailedDataSetMessages =
      new NodeId(UShort.MIN, uint(19655L));

  public static final NodeId DataSetReaderType_Diagnostics_Counters_FailedDataSetMessages_Active =
      new NodeId(UShort.MIN, uint(19656L));

  public static final NodeId
      DataSetReaderType_Diagnostics_Counters_FailedDataSetMessages_Classification =
          new NodeId(UShort.MIN, uint(19657L));

  public static final NodeId
      DataSetReaderType_Diagnostics_Counters_FailedDataSetMessages_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19658L));

  public static final NodeId
      DataSetReaderType_Diagnostics_Counters_FailedDataSetMessages_TimeFirstChange =
          new NodeId(UShort.MIN, uint(19659L));

  public static final NodeId DataSetReaderType_Diagnostics_Counters_DecryptionErrors =
      new NodeId(UShort.MIN, uint(19660L));

  public static final NodeId DataSetReaderType_Diagnostics_Counters_DecryptionErrors_Active =
      new NodeId(UShort.MIN, uint(19661L));

  public static final NodeId
      DataSetReaderType_Diagnostics_Counters_DecryptionErrors_Classification =
          new NodeId(UShort.MIN, uint(19662L));

  public static final NodeId
      DataSetReaderType_Diagnostics_Counters_DecryptionErrors_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19663L));

  public static final NodeId
      DataSetReaderType_Diagnostics_Counters_DecryptionErrors_TimeFirstChange =
          new NodeId(UShort.MIN, uint(19664L));

  public static final NodeId DataSetReaderType_Diagnostics_LiveValues_MessageSequenceNumber =
      new NodeId(UShort.MIN, uint(19665L));

  public static final NodeId
      DataSetReaderType_Diagnostics_LiveValues_MessageSequenceNumber_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19666L));

  public static final NodeId DataSetReaderType_Diagnostics_LiveValues_StatusCode =
      new NodeId(UShort.MIN, uint(19667L));

  public static final NodeId DataSetReaderType_Diagnostics_LiveValues_StatusCode_DiagnosticsLevel =
      new NodeId(UShort.MIN, uint(19668L));

  public static final NodeId DataSetReaderType_Diagnostics_LiveValues_MajorVersion =
      new NodeId(UShort.MIN, uint(19669L));

  public static final NodeId
      DataSetReaderType_Diagnostics_LiveValues_MajorVersion_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19670L));

  public static final NodeId DataSetReaderType_Diagnostics_LiveValues_MinorVersion =
      new NodeId(UShort.MIN, uint(19671L));

  public static final NodeId
      DataSetReaderType_Diagnostics_LiveValues_MinorVersion_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19672L));

  public static final NodeId DataSetReaderType_Diagnostics_LiveValues_SecurityTokenID =
      new NodeId(UShort.MIN, uint(19673L));

  public static final NodeId
      DataSetReaderType_Diagnostics_LiveValues_SecurityTokenID_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19674L));

  public static final NodeId DataSetReaderType_Diagnostics_LiveValues_TimeToNextTokenID =
      new NodeId(UShort.MIN, uint(19675L));

  public static final NodeId
      DataSetReaderType_Diagnostics_LiveValues_TimeToNextTokenID_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19676L));

  public static final NodeId PubSubDiagnosticsType = new NodeId(UShort.MIN, uint(19677L));

  public static final NodeId PubSubDiagnosticsType_DiagnosticsLevel =
      new NodeId(UShort.MIN, uint(19678L));

  public static final NodeId PubSubDiagnosticsType_TotalInformation =
      new NodeId(UShort.MIN, uint(19679L));

  public static final NodeId PubSubDiagnosticsType_TotalInformation_Active =
      new NodeId(UShort.MIN, uint(19680L));

  public static final NodeId PubSubDiagnosticsType_TotalInformation_Classification =
      new NodeId(UShort.MIN, uint(19681L));

  public static final NodeId PubSubDiagnosticsType_TotalInformation_DiagnosticsLevel =
      new NodeId(UShort.MIN, uint(19682L));

  public static final NodeId PubSubDiagnosticsType_TotalInformation_TimeFirstChange =
      new NodeId(UShort.MIN, uint(19683L));

  public static final NodeId PubSubDiagnosticsType_TotalError =
      new NodeId(UShort.MIN, uint(19684L));

  public static final NodeId PubSubDiagnosticsType_TotalError_Active =
      new NodeId(UShort.MIN, uint(19685L));

  public static final NodeId PubSubDiagnosticsType_TotalError_Classification =
      new NodeId(UShort.MIN, uint(19686L));

  public static final NodeId PubSubDiagnosticsType_TotalError_DiagnosticsLevel =
      new NodeId(UShort.MIN, uint(19687L));

  public static final NodeId PubSubDiagnosticsType_TotalError_TimeFirstChange =
      new NodeId(UShort.MIN, uint(19688L));

  public static final NodeId PubSubDiagnosticsType_Reset = new NodeId(UShort.MIN, uint(19689L));

  public static final NodeId PubSubDiagnosticsType_SubError = new NodeId(UShort.MIN, uint(19690L));

  public static final NodeId PubSubDiagnosticsType_Counters = new NodeId(UShort.MIN, uint(19691L));

  public static final NodeId PubSubDiagnosticsType_Counters_StateError =
      new NodeId(UShort.MIN, uint(19692L));

  public static final NodeId PubSubDiagnosticsType_Counters_StateError_Active =
      new NodeId(UShort.MIN, uint(19693L));

  public static final NodeId PubSubDiagnosticsType_Counters_StateError_Classification =
      new NodeId(UShort.MIN, uint(19694L));

  public static final NodeId PubSubDiagnosticsType_Counters_StateError_DiagnosticsLevel =
      new NodeId(UShort.MIN, uint(19695L));

  public static final NodeId PubSubDiagnosticsType_Counters_StateError_TimeFirstChange =
      new NodeId(UShort.MIN, uint(19696L));

  public static final NodeId PubSubDiagnosticsType_Counters_StateOperationalByMethod =
      new NodeId(UShort.MIN, uint(19697L));

  public static final NodeId PubSubDiagnosticsType_Counters_StateOperationalByMethod_Active =
      new NodeId(UShort.MIN, uint(19698L));

  public static final NodeId
      PubSubDiagnosticsType_Counters_StateOperationalByMethod_Classification =
          new NodeId(UShort.MIN, uint(19699L));

  public static final NodeId
      PubSubDiagnosticsType_Counters_StateOperationalByMethod_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19700L));

  public static final NodeId
      PubSubDiagnosticsType_Counters_StateOperationalByMethod_TimeFirstChange =
          new NodeId(UShort.MIN, uint(19701L));

  public static final NodeId PubSubDiagnosticsType_Counters_StateOperationalByParent =
      new NodeId(UShort.MIN, uint(19702L));

  public static final NodeId PubSubDiagnosticsType_Counters_StateOperationalByParent_Active =
      new NodeId(UShort.MIN, uint(19703L));

  public static final NodeId
      PubSubDiagnosticsType_Counters_StateOperationalByParent_Classification =
          new NodeId(UShort.MIN, uint(19704L));

  public static final NodeId
      PubSubDiagnosticsType_Counters_StateOperationalByParent_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19705L));

  public static final NodeId
      PubSubDiagnosticsType_Counters_StateOperationalByParent_TimeFirstChange =
          new NodeId(UShort.MIN, uint(19706L));

  public static final NodeId PubSubDiagnosticsType_Counters_StateOperationalFromError =
      new NodeId(UShort.MIN, uint(19707L));

  public static final NodeId PubSubDiagnosticsType_Counters_StateOperationalFromError_Active =
      new NodeId(UShort.MIN, uint(19708L));

  public static final NodeId
      PubSubDiagnosticsType_Counters_StateOperationalFromError_Classification =
          new NodeId(UShort.MIN, uint(19709L));

  public static final NodeId
      PubSubDiagnosticsType_Counters_StateOperationalFromError_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19710L));

  public static final NodeId
      PubSubDiagnosticsType_Counters_StateOperationalFromError_TimeFirstChange =
          new NodeId(UShort.MIN, uint(19711L));

  public static final NodeId PubSubDiagnosticsType_Counters_StatePausedByParent =
      new NodeId(UShort.MIN, uint(19712L));

  public static final NodeId PubSubDiagnosticsType_Counters_StatePausedByParent_Active =
      new NodeId(UShort.MIN, uint(19713L));

  public static final NodeId PubSubDiagnosticsType_Counters_StatePausedByParent_Classification =
      new NodeId(UShort.MIN, uint(19714L));

  public static final NodeId PubSubDiagnosticsType_Counters_StatePausedByParent_DiagnosticsLevel =
      new NodeId(UShort.MIN, uint(19715L));

  public static final NodeId PubSubDiagnosticsType_Counters_StatePausedByParent_TimeFirstChange =
      new NodeId(UShort.MIN, uint(19716L));

  public static final NodeId PubSubDiagnosticsType_Counters_StateDisabledByMethod =
      new NodeId(UShort.MIN, uint(19717L));

  public static final NodeId PubSubDiagnosticsType_Counters_StateDisabledByMethod_Active =
      new NodeId(UShort.MIN, uint(19718L));

  public static final NodeId PubSubDiagnosticsType_Counters_StateDisabledByMethod_Classification =
      new NodeId(UShort.MIN, uint(19719L));

  public static final NodeId PubSubDiagnosticsType_Counters_StateDisabledByMethod_DiagnosticsLevel =
      new NodeId(UShort.MIN, uint(19720L));

  public static final NodeId PubSubDiagnosticsType_Counters_StateDisabledByMethod_TimeFirstChange =
      new NodeId(UShort.MIN, uint(19721L));

  public static final NodeId PubSubDiagnosticsType_LiveValues =
      new NodeId(UShort.MIN, uint(19722L));

  public static final NodeId DiagnosticsLevel = new NodeId(UShort.MIN, uint(19723L));

  public static final NodeId DiagnosticsLevel_EnumStrings = new NodeId(UShort.MIN, uint(19724L));

  public static final NodeId PubSubDiagnosticsCounterType = new NodeId(UShort.MIN, uint(19725L));

  public static final NodeId PubSubDiagnosticsCounterType_Active =
      new NodeId(UShort.MIN, uint(19726L));

  public static final NodeId PubSubDiagnosticsCounterType_Classification =
      new NodeId(UShort.MIN, uint(19727L));

  public static final NodeId PubSubDiagnosticsCounterType_DiagnosticsLevel =
      new NodeId(UShort.MIN, uint(19728L));

  public static final NodeId PubSubDiagnosticsCounterType_TimeFirstChange =
      new NodeId(UShort.MIN, uint(19729L));

  public static final NodeId PubSubDiagnosticsCounterClassification =
      new NodeId(UShort.MIN, uint(19730L));

  public static final NodeId PubSubDiagnosticsCounterClassification_EnumStrings =
      new NodeId(UShort.MIN, uint(19731L));

  public static final NodeId PubSubDiagnosticsRootType = new NodeId(UShort.MIN, uint(19732L));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_Purpose =
          new NodeId(UShort.MIN, uint(19733L));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultHttpsGroup_Purpose =
          new NodeId(UShort.MIN, uint(19734L));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_CertificateGroups_DefaultUserTokenGroup_Purpose =
          new NodeId(UShort.MIN, uint(19735L));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_ConfigurationFile_MaxEndpoints =
          new NodeId(UShort.MIN, uint(19736L));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_ConfigurationFile_MaxCertificateGroups =
          new NodeId(UShort.MIN, uint(19737L));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_ConfigurationFile_CertificateGroupPurposes =
          new NodeId(UShort.MIN, uint(19738L));

  public static final NodeId ProvisionableDeviceType_ApplicationName_Placeholder_KeyCredentials =
      new NodeId(UShort.MIN, uint(19739L));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_KeyCredentials_CreateCredential =
          new NodeId(UShort.MIN, uint(19740L));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_KeyCredentials_CreateCredential_InputArguments =
          new NodeId(UShort.MIN, uint(19741L));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_KeyCredentials_CreateCredential_OutputArguments =
          new NodeId(UShort.MIN, uint(19742L));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_AuthorizationServices =
          new NodeId(UShort.MIN, uint(19743L));

  public static final NodeId LogObjectType_MinimumSeverity = new NodeId(UShort.MIN, uint(19744L));

  public static final NodeId LogRecordsDataType = new NodeId(UShort.MIN, uint(19745L));

  public static final NodeId SpanContextDataType = new NodeId(UShort.MIN, uint(19746L));

  public static final NodeId TraceContextDataType = new NodeId(UShort.MIN, uint(19747L));

  public static final NodeId NameValuePair = new NodeId(UShort.MIN, uint(19748L));

  public static final NodeId LogRecordMask = new NodeId(UShort.MIN, uint(19749L));

  public static final NodeId LogRecordMask_OptionSetValues = new NodeId(UShort.MIN, uint(19750L));

  public static final NodeId ServerLog_MinimumSeverity = new NodeId(UShort.MIN, uint(19751L));

  public static final NodeId LogRecordsDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(19753L));

  public static final NodeId SpanContextDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(19754L));

  public static final NodeId TraceContextDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(19755L));

  public static final NodeId NameValuePair_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(19756L));

  public static final NodeId OpcUa_BinarySchema_LogRecordsDataType =
      new NodeId(UShort.MIN, uint(19760L));

  public static final NodeId OpcUa_BinarySchema_LogRecordsDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(19761L));

  public static final NodeId OpcUa_BinarySchema_LogRecordsDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(19762L));

  public static final NodeId OpcUa_BinarySchema_SpanContextDataType =
      new NodeId(UShort.MIN, uint(19763L));

  public static final NodeId OpcUa_BinarySchema_SpanContextDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(19764L));

  public static final NodeId OpcUa_BinarySchema_SpanContextDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(19765L));

  public static final NodeId OpcUa_BinarySchema_TraceContextDataType =
      new NodeId(UShort.MIN, uint(19766L));

  public static final NodeId OpcUa_BinarySchema_TraceContextDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(19767L));

  public static final NodeId OpcUa_BinarySchema_TraceContextDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(19768L));

  public static final NodeId OpcUa_BinarySchema_NameValuePair =
      new NodeId(UShort.MIN, uint(19769L));

  public static final NodeId OpcUa_BinarySchema_NameValuePair_DataTypeVersion =
      new NodeId(UShort.MIN, uint(19770L));

  public static final NodeId OpcUa_BinarySchema_NameValuePair_DictionaryFragment =
      new NodeId(UShort.MIN, uint(19771L));

  public static final NodeId LogRecordsDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(19773L));

  public static final NodeId SpanContextDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(19774L));

  public static final NodeId TraceContextDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(19775L));

  public static final NodeId NameValuePair_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(19776L));

  public static final NodeId PubSubDiagnosticsRootType_LiveValues =
      new NodeId(UShort.MIN, uint(19777L));

  public static final NodeId PubSubDiagnosticsRootType_LiveValues_ConfiguredDataSetWriters =
      new NodeId(UShort.MIN, uint(19778L));

  public static final NodeId
      PubSubDiagnosticsRootType_LiveValues_ConfiguredDataSetWriters_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19779L));

  public static final NodeId PubSubDiagnosticsRootType_LiveValues_ConfiguredDataSetReaders =
      new NodeId(UShort.MIN, uint(19780L));

  public static final NodeId
      PubSubDiagnosticsRootType_LiveValues_ConfiguredDataSetReaders_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19781L));

  public static final NodeId PubSubDiagnosticsRootType_LiveValues_OperationalDataSetWriters =
      new NodeId(UShort.MIN, uint(19782L));

  public static final NodeId
      PubSubDiagnosticsRootType_LiveValues_OperationalDataSetWriters_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19783L));

  public static final NodeId PubSubDiagnosticsRootType_LiveValues_OperationalDataSetReaders =
      new NodeId(UShort.MIN, uint(19784L));

  public static final NodeId
      PubSubDiagnosticsRootType_LiveValues_OperationalDataSetReaders_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19785L));

  public static final NodeId PubSubDiagnosticsConnectionType = new NodeId(UShort.MIN, uint(19786L));

  public static final NodeId OpcUa_XmlSchema_LogRecordsDataType =
      new NodeId(UShort.MIN, uint(19790L));

  public static final NodeId OpcUa_XmlSchema_LogRecordsDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(19791L));

  public static final NodeId OpcUa_XmlSchema_LogRecordsDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(19792L));

  public static final NodeId OpcUa_XmlSchema_SpanContextDataType =
      new NodeId(UShort.MIN, uint(19793L));

  public static final NodeId OpcUa_XmlSchema_SpanContextDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(19794L));

  public static final NodeId OpcUa_XmlSchema_SpanContextDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(19795L));

  public static final NodeId OpcUa_XmlSchema_TraceContextDataType =
      new NodeId(UShort.MIN, uint(19796L));

  public static final NodeId OpcUa_XmlSchema_TraceContextDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(19797L));

  public static final NodeId OpcUa_XmlSchema_TraceContextDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(19798L));

  public static final NodeId OpcUa_XmlSchema_NameValuePair = new NodeId(UShort.MIN, uint(19799L));

  public static final NodeId OpcUa_XmlSchema_NameValuePair_DataTypeVersion =
      new NodeId(UShort.MIN, uint(19800L));

  public static final NodeId OpcUa_XmlSchema_NameValuePair_DictionaryFragment =
      new NodeId(UShort.MIN, uint(19801L));

  public static final NodeId LogRecordsDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(19803L));

  public static final NodeId SpanContextDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(19804L));

  public static final NodeId TraceContextDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(19805L));

  public static final NodeId NameValuePair_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(19806L));

  public static final NodeId ServerType_ServerCapabilities_MaxLogObjectContinuationPoints =
      new NodeId(UShort.MIN, uint(19807L));

  public static final NodeId ServerCapabilitiesType_MaxLogObjectContinuationPoints =
      new NodeId(UShort.MIN, uint(19809L));

  public static final NodeId AuditEventType_ClientApplicationUri =
      new NodeId(UShort.MIN, uint(19811L));

  public static final NodeId Server_ServerCapabilities_MaxLogObjectContinuationPoints =
      new NodeId(UShort.MIN, uint(19812L));

  public static final NodeId UsesDataTypeRefinement = new NodeId(UShort.MIN, uint(19814L));

  public static final NodeId HasFieldDescription = new NodeId(UShort.MIN, uint(19815L));

  public static final NodeId HasFieldDescriptionSetMandatory = new NodeId(UShort.MIN, uint(19816L));

  public static final NodeId IsDisabledOptionalField = new NodeId(UShort.MIN, uint(19817L));

  public static final NodeId UsesSubtypeRestriction = new NodeId(UShort.MIN, uint(19818L));

  public static final NodeId AllowedSubtype = new NodeId(UShort.MIN, uint(19819L));

  public static final NodeId DataTypeRefinementType = new NodeId(UShort.MIN, uint(19820L));

  public static final NodeId DataTypeRefinementType_FieldDescription_Placeholder =
      new NodeId(UShort.MIN, uint(19821L));

  public static final NodeId SubtypeRestrictionType = new NodeId(UShort.MIN, uint(19822L));

  public static final NodeId SubtypeRestrictionType_FieldDescription_Placeholder =
      new NodeId(UShort.MIN, uint(19823L));

  public static final NodeId SerializationEntityType = new NodeId(UShort.MIN, uint(19824L));

  public static final NodeId SerializationEntityType_SerializedData =
      new NodeId(UShort.MIN, uint(19825L));

  public static final NodeId SerializationEntityType_IncludeReferenceTypes =
      new NodeId(UShort.MIN, uint(19826L));

  public static final NodeId SerializationEntityType_ExcludeReferenceTypes =
      new NodeId(UShort.MIN, uint(19827L));

  public static final NodeId SerializationEntityType_SerializationDepth =
      new NodeId(UShort.MIN, uint(19828L));

  public static final NodeId SerializationEntityType_ConsiderSubElementSerializationProperties =
      new NodeId(UShort.MIN, uint(19829L));

  public static final NodeId SerializationEntityType_CustomMetaDataProperties =
      new NodeId(UShort.MIN, uint(19830L));

  public static final NodeId PubSubDiagnosticsConnectionType_LiveValues =
      new NodeId(UShort.MIN, uint(19831L));

  public static final NodeId PubSubDiagnosticsConnectionType_LiveValues_ResolvedAddress =
      new NodeId(UShort.MIN, uint(19832L));

  public static final NodeId
      PubSubDiagnosticsConnectionType_LiveValues_ResolvedAddress_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19833L));

  public static final NodeId PubSubDiagnosticsWriterGroupType =
      new NodeId(UShort.MIN, uint(19834L));

  public static final NodeId SerializationEntityType_CustomMetaDataRef =
      new NodeId(UShort.MIN, uint(19835L));

  public static final NodeId SerializationEntityType_IncludeStatus =
      new NodeId(UShort.MIN, uint(19836L));

  public static final NodeId SerializationEntityType_IncludeSourceTimestamp =
      new NodeId(UShort.MIN, uint(19837L));

  public static final NodeId SerializationEntityType_IncludeDictionaryReference =
      new NodeId(UShort.MIN, uint(19838L));

  public static final NodeId SerializationEntityType_ConfigureSerialization =
      new NodeId(UShort.MIN, uint(19839L));

  public static final NodeId SerializationEntityType_ConfigureSerialization_InputArguments =
      new NodeId(UShort.MIN, uint(19840L));

  public static final NodeId SerializationEntityType_ConfigureSerialization_OutputArguments =
      new NodeId(UShort.MIN, uint(19841L));

  public static final NodeId ConfigureSerializationMethodType =
      new NodeId(UShort.MIN, uint(19842L));

  public static final NodeId ConfigureSerializationMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(19843L));

  public static final NodeId ConfigureSerializationMethodType_OutputArguments =
      new NodeId(UShort.MIN, uint(19844L));

  public static final NodeId HasSerializationEntity = new NodeId(UShort.MIN, uint(19845L));

  public static final NodeId HasDataTypeRefinement = new NodeId(UShort.MIN, uint(19846L));

  public static final NodeId AlarmSuppressionGroupType_AlarmCondition_Placeholder =
      new NodeId(UShort.MIN, uint(19847L));

  public static final NodeId PubSubDiagnosticsWriterGroupType_Counters =
      new NodeId(UShort.MIN, uint(19848L));

  public static final NodeId PubSubDiagnosticsWriterGroupType_Counters_StateError =
      new NodeId(UShort.MIN, uint(19849L));

  public static final NodeId PubSubDiagnosticsWriterGroupType_Counters_StateError_Active =
      new NodeId(UShort.MIN, uint(19850L));

  public static final NodeId PubSubDiagnosticsWriterGroupType_Counters_StateError_Classification =
      new NodeId(UShort.MIN, uint(19851L));

  public static final NodeId PubSubDiagnosticsWriterGroupType_Counters_StateError_DiagnosticsLevel =
      new NodeId(UShort.MIN, uint(19852L));

  public static final NodeId PubSubDiagnosticsWriterGroupType_Counters_StateError_TimeFirstChange =
      new NodeId(UShort.MIN, uint(19853L));

  public static final NodeId PubSubDiagnosticsWriterGroupType_Counters_StateOperationalByMethod =
      new NodeId(UShort.MIN, uint(19854L));

  public static final NodeId
      PubSubDiagnosticsWriterGroupType_Counters_StateOperationalByMethod_Active =
          new NodeId(UShort.MIN, uint(19855L));

  public static final NodeId
      PubSubDiagnosticsWriterGroupType_Counters_StateOperationalByMethod_Classification =
          new NodeId(UShort.MIN, uint(19856L));

  public static final NodeId
      PubSubDiagnosticsWriterGroupType_Counters_StateOperationalByMethod_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19857L));

  public static final NodeId
      PubSubDiagnosticsWriterGroupType_Counters_StateOperationalByMethod_TimeFirstChange =
          new NodeId(UShort.MIN, uint(19858L));

  public static final NodeId PubSubDiagnosticsWriterGroupType_Counters_StateOperationalByParent =
      new NodeId(UShort.MIN, uint(19859L));

  public static final NodeId
      PubSubDiagnosticsWriterGroupType_Counters_StateOperationalByParent_Active =
          new NodeId(UShort.MIN, uint(19860L));

  public static final NodeId
      PubSubDiagnosticsWriterGroupType_Counters_StateOperationalByParent_Classification =
          new NodeId(UShort.MIN, uint(19861L));

  public static final NodeId
      PubSubDiagnosticsWriterGroupType_Counters_StateOperationalByParent_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19862L));

  public static final NodeId
      PubSubDiagnosticsWriterGroupType_Counters_StateOperationalByParent_TimeFirstChange =
          new NodeId(UShort.MIN, uint(19863L));

  public static final NodeId PubSubDiagnosticsWriterGroupType_Counters_StateOperationalFromError =
      new NodeId(UShort.MIN, uint(19864L));

  public static final NodeId
      PubSubDiagnosticsWriterGroupType_Counters_StateOperationalFromError_Active =
          new NodeId(UShort.MIN, uint(19865L));

  public static final NodeId
      PubSubDiagnosticsWriterGroupType_Counters_StateOperationalFromError_Classification =
          new NodeId(UShort.MIN, uint(19866L));

  public static final NodeId
      PubSubDiagnosticsWriterGroupType_Counters_StateOperationalFromError_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19867L));

  public static final NodeId
      PubSubDiagnosticsWriterGroupType_Counters_StateOperationalFromError_TimeFirstChange =
          new NodeId(UShort.MIN, uint(19868L));

  public static final NodeId PubSubDiagnosticsWriterGroupType_Counters_StatePausedByParent =
      new NodeId(UShort.MIN, uint(19869L));

  public static final NodeId PubSubDiagnosticsWriterGroupType_Counters_StatePausedByParent_Active =
      new NodeId(UShort.MIN, uint(19870L));

  public static final NodeId
      PubSubDiagnosticsWriterGroupType_Counters_StatePausedByParent_Classification =
          new NodeId(UShort.MIN, uint(19871L));

  public static final NodeId
      PubSubDiagnosticsWriterGroupType_Counters_StatePausedByParent_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19872L));

  public static final NodeId
      PubSubDiagnosticsWriterGroupType_Counters_StatePausedByParent_TimeFirstChange =
          new NodeId(UShort.MIN, uint(19873L));

  public static final NodeId PubSubDiagnosticsWriterGroupType_Counters_StateDisabledByMethod =
      new NodeId(UShort.MIN, uint(19874L));

  public static final NodeId
      PubSubDiagnosticsWriterGroupType_Counters_StateDisabledByMethod_Active =
          new NodeId(UShort.MIN, uint(19875L));

  public static final NodeId
      PubSubDiagnosticsWriterGroupType_Counters_StateDisabledByMethod_Classification =
          new NodeId(UShort.MIN, uint(19876L));

  public static final NodeId
      PubSubDiagnosticsWriterGroupType_Counters_StateDisabledByMethod_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19877L));

  public static final NodeId
      PubSubDiagnosticsWriterGroupType_Counters_StateDisabledByMethod_TimeFirstChange =
          new NodeId(UShort.MIN, uint(19878L));

  public static final NodeId PubSubDiagnosticsWriterGroupType_LiveValues =
      new NodeId(UShort.MIN, uint(19879L));

  public static final NodeId PubSubDiagnosticsWriterGroupType_Counters_SentNetworkMessages =
      new NodeId(UShort.MIN, uint(19880L));

  public static final NodeId PubSubDiagnosticsWriterGroupType_Counters_SentNetworkMessages_Active =
      new NodeId(UShort.MIN, uint(19881L));

  public static final NodeId
      PubSubDiagnosticsWriterGroupType_Counters_SentNetworkMessages_Classification =
          new NodeId(UShort.MIN, uint(19882L));

  public static final NodeId
      PubSubDiagnosticsWriterGroupType_Counters_SentNetworkMessages_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19883L));

  public static final NodeId
      PubSubDiagnosticsWriterGroupType_Counters_SentNetworkMessages_TimeFirstChange =
          new NodeId(UShort.MIN, uint(19884L));

  public static final NodeId PubSubDiagnosticsWriterGroupType_Counters_FailedTransmissions =
      new NodeId(UShort.MIN, uint(19885L));

  public static final NodeId PubSubDiagnosticsWriterGroupType_Counters_FailedTransmissions_Active =
      new NodeId(UShort.MIN, uint(19886L));

  public static final NodeId
      PubSubDiagnosticsWriterGroupType_Counters_FailedTransmissions_Classification =
          new NodeId(UShort.MIN, uint(19887L));

  public static final NodeId
      PubSubDiagnosticsWriterGroupType_Counters_FailedTransmissions_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19888L));

  public static final NodeId
      PubSubDiagnosticsWriterGroupType_Counters_FailedTransmissions_TimeFirstChange =
          new NodeId(UShort.MIN, uint(19889L));

  public static final NodeId PubSubDiagnosticsWriterGroupType_Counters_EncryptionErrors =
      new NodeId(UShort.MIN, uint(19890L));

  public static final NodeId PubSubDiagnosticsWriterGroupType_Counters_EncryptionErrors_Active =
      new NodeId(UShort.MIN, uint(19891L));

  public static final NodeId
      PubSubDiagnosticsWriterGroupType_Counters_EncryptionErrors_Classification =
          new NodeId(UShort.MIN, uint(19892L));

  public static final NodeId
      PubSubDiagnosticsWriterGroupType_Counters_EncryptionErrors_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19893L));

  public static final NodeId
      PubSubDiagnosticsWriterGroupType_Counters_EncryptionErrors_TimeFirstChange =
          new NodeId(UShort.MIN, uint(19894L));

  public static final NodeId PubSubDiagnosticsWriterGroupType_LiveValues_ConfiguredDataSetWriters =
      new NodeId(UShort.MIN, uint(19895L));

  public static final NodeId
      PubSubDiagnosticsWriterGroupType_LiveValues_ConfiguredDataSetWriters_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19896L));

  public static final NodeId PubSubDiagnosticsWriterGroupType_LiveValues_OperationalDataSetWriters =
      new NodeId(UShort.MIN, uint(19897L));

  public static final NodeId
      PubSubDiagnosticsWriterGroupType_LiveValues_OperationalDataSetWriters_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19898L));

  public static final NodeId PubSubDiagnosticsWriterGroupType_LiveValues_SecurityTokenID =
      new NodeId(UShort.MIN, uint(19899L));

  public static final NodeId
      PubSubDiagnosticsWriterGroupType_LiveValues_SecurityTokenID_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19900L));

  public static final NodeId PubSubDiagnosticsWriterGroupType_LiveValues_TimeToNextTokenID =
      new NodeId(UShort.MIN, uint(19901L));

  public static final NodeId
      PubSubDiagnosticsWriterGroupType_LiveValues_TimeToNextTokenID_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19902L));

  public static final NodeId PubSubDiagnosticsReaderGroupType =
      new NodeId(UShort.MIN, uint(19903L));

  public static final NodeId AlarmSuppressionGroupType_AlarmCondition_Placeholder_EventId =
      new NodeId(UShort.MIN, uint(19904L));

  public static final NodeId AlarmSuppressionGroupType_AlarmCondition_Placeholder_EventType =
      new NodeId(UShort.MIN, uint(19905L));

  public static final NodeId AlarmSuppressionGroupType_AlarmCondition_Placeholder_SourceNode =
      new NodeId(UShort.MIN, uint(19906L));

  public static final NodeId AlarmSuppressionGroupType_AlarmCondition_Placeholder_SourceName =
      new NodeId(UShort.MIN, uint(19907L));

  public static final NodeId AlarmSuppressionGroupType_AlarmCondition_Placeholder_Time =
      new NodeId(UShort.MIN, uint(19908L));

  public static final NodeId AlarmSuppressionGroupType_AlarmCondition_Placeholder_ReceiveTime =
      new NodeId(UShort.MIN, uint(19909L));

  public static final NodeId AlarmSuppressionGroupType_AlarmCondition_Placeholder_LocalTime =
      new NodeId(UShort.MIN, uint(19910L));

  public static final NodeId AlarmSuppressionGroupType_AlarmCondition_Placeholder_Message =
      new NodeId(UShort.MIN, uint(19911L));

  public static final NodeId AlarmSuppressionGroupType_AlarmCondition_Placeholder_Severity =
      new NodeId(UShort.MIN, uint(19912L));

  public static final NodeId AlarmSuppressionGroupType_AlarmCondition_Placeholder_ConditionClassId =
      new NodeId(UShort.MIN, uint(19913L));

  public static final NodeId
      AlarmSuppressionGroupType_AlarmCondition_Placeholder_ConditionClassName =
          new NodeId(UShort.MIN, uint(19914L));

  public static final NodeId
      AlarmSuppressionGroupType_AlarmCondition_Placeholder_ConditionSubClassId =
          new NodeId(UShort.MIN, uint(19915L));

  public static final NodeId
      AlarmSuppressionGroupType_AlarmCondition_Placeholder_ConditionSubClassName =
          new NodeId(UShort.MIN, uint(19916L));

  public static final NodeId PubSubDiagnosticsReaderGroupType_Counters =
      new NodeId(UShort.MIN, uint(19917L));

  public static final NodeId PubSubDiagnosticsReaderGroupType_Counters_StateError =
      new NodeId(UShort.MIN, uint(19918L));

  public static final NodeId PubSubDiagnosticsReaderGroupType_Counters_StateError_Active =
      new NodeId(UShort.MIN, uint(19919L));

  public static final NodeId PubSubDiagnosticsReaderGroupType_Counters_StateError_Classification =
      new NodeId(UShort.MIN, uint(19920L));

  public static final NodeId PubSubDiagnosticsReaderGroupType_Counters_StateError_DiagnosticsLevel =
      new NodeId(UShort.MIN, uint(19921L));

  public static final NodeId PubSubDiagnosticsReaderGroupType_Counters_StateError_TimeFirstChange =
      new NodeId(UShort.MIN, uint(19922L));

  public static final NodeId PubSubDiagnosticsReaderGroupType_Counters_StateOperationalByMethod =
      new NodeId(UShort.MIN, uint(19923L));

  public static final NodeId
      PubSubDiagnosticsReaderGroupType_Counters_StateOperationalByMethod_Active =
          new NodeId(UShort.MIN, uint(19924L));

  public static final NodeId
      PubSubDiagnosticsReaderGroupType_Counters_StateOperationalByMethod_Classification =
          new NodeId(UShort.MIN, uint(19925L));

  public static final NodeId
      PubSubDiagnosticsReaderGroupType_Counters_StateOperationalByMethod_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19926L));

  public static final NodeId
      PubSubDiagnosticsReaderGroupType_Counters_StateOperationalByMethod_TimeFirstChange =
          new NodeId(UShort.MIN, uint(19927L));

  public static final NodeId PubSubDiagnosticsReaderGroupType_Counters_StateOperationalByParent =
      new NodeId(UShort.MIN, uint(19928L));

  public static final NodeId
      PubSubDiagnosticsReaderGroupType_Counters_StateOperationalByParent_Active =
          new NodeId(UShort.MIN, uint(19929L));

  public static final NodeId
      PubSubDiagnosticsReaderGroupType_Counters_StateOperationalByParent_Classification =
          new NodeId(UShort.MIN, uint(19930L));

  public static final NodeId
      PubSubDiagnosticsReaderGroupType_Counters_StateOperationalByParent_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19931L));

  public static final NodeId
      PubSubDiagnosticsReaderGroupType_Counters_StateOperationalByParent_TimeFirstChange =
          new NodeId(UShort.MIN, uint(19932L));

  public static final NodeId PubSubDiagnosticsReaderGroupType_Counters_StateOperationalFromError =
      new NodeId(UShort.MIN, uint(19933L));

  public static final NodeId
      PubSubDiagnosticsReaderGroupType_Counters_StateOperationalFromError_Active =
          new NodeId(UShort.MIN, uint(19934L));

  public static final NodeId
      PubSubDiagnosticsReaderGroupType_Counters_StateOperationalFromError_Classification =
          new NodeId(UShort.MIN, uint(19935L));

  public static final NodeId
      PubSubDiagnosticsReaderGroupType_Counters_StateOperationalFromError_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19936L));

  public static final NodeId
      PubSubDiagnosticsReaderGroupType_Counters_StateOperationalFromError_TimeFirstChange =
          new NodeId(UShort.MIN, uint(19937L));

  public static final NodeId PubSubDiagnosticsReaderGroupType_Counters_StatePausedByParent =
      new NodeId(UShort.MIN, uint(19938L));

  public static final NodeId PubSubDiagnosticsReaderGroupType_Counters_StatePausedByParent_Active =
      new NodeId(UShort.MIN, uint(19939L));

  public static final NodeId
      PubSubDiagnosticsReaderGroupType_Counters_StatePausedByParent_Classification =
          new NodeId(UShort.MIN, uint(19940L));

  public static final NodeId
      PubSubDiagnosticsReaderGroupType_Counters_StatePausedByParent_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19941L));

  public static final NodeId
      PubSubDiagnosticsReaderGroupType_Counters_StatePausedByParent_TimeFirstChange =
          new NodeId(UShort.MIN, uint(19942L));

  public static final NodeId PubSubDiagnosticsReaderGroupType_Counters_StateDisabledByMethod =
      new NodeId(UShort.MIN, uint(19943L));

  public static final NodeId
      PubSubDiagnosticsReaderGroupType_Counters_StateDisabledByMethod_Active =
          new NodeId(UShort.MIN, uint(19944L));

  public static final NodeId
      PubSubDiagnosticsReaderGroupType_Counters_StateDisabledByMethod_Classification =
          new NodeId(UShort.MIN, uint(19945L));

  public static final NodeId
      PubSubDiagnosticsReaderGroupType_Counters_StateDisabledByMethod_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19946L));

  public static final NodeId
      PubSubDiagnosticsReaderGroupType_Counters_StateDisabledByMethod_TimeFirstChange =
          new NodeId(UShort.MIN, uint(19947L));

  public static final NodeId PubSubDiagnosticsReaderGroupType_LiveValues =
      new NodeId(UShort.MIN, uint(19948L));

  public static final NodeId PubSubDiagnosticsReaderGroupType_Counters_ReceivedNetworkMessages =
      new NodeId(UShort.MIN, uint(19949L));

  public static final NodeId
      PubSubDiagnosticsReaderGroupType_Counters_ReceivedNetworkMessages_Active =
          new NodeId(UShort.MIN, uint(19950L));

  public static final NodeId
      PubSubDiagnosticsReaderGroupType_Counters_ReceivedNetworkMessages_Classification =
          new NodeId(UShort.MIN, uint(19951L));

  public static final NodeId
      PubSubDiagnosticsReaderGroupType_Counters_ReceivedNetworkMessages_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19952L));

  public static final NodeId
      PubSubDiagnosticsReaderGroupType_Counters_ReceivedNetworkMessages_TimeFirstChange =
          new NodeId(UShort.MIN, uint(19953L));

  public static final NodeId
      PubSubDiagnosticsReaderGroupType_Counters_ReceivedInvalidNetworkMessages =
          new NodeId(UShort.MIN, uint(19954L));

  public static final NodeId
      PubSubDiagnosticsReaderGroupType_Counters_ReceivedInvalidNetworkMessages_Active =
          new NodeId(UShort.MIN, uint(19955L));

  public static final NodeId
      PubSubDiagnosticsReaderGroupType_Counters_ReceivedInvalidNetworkMessages_Classification =
          new NodeId(UShort.MIN, uint(19956L));

  public static final NodeId
      PubSubDiagnosticsReaderGroupType_Counters_ReceivedInvalidNetworkMessages_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19957L));

  public static final NodeId
      PubSubDiagnosticsReaderGroupType_Counters_ReceivedInvalidNetworkMessages_TimeFirstChange =
          new NodeId(UShort.MIN, uint(19958L));

  public static final NodeId PubSubDiagnosticsReaderGroupType_Counters_DecryptionErrors =
      new NodeId(UShort.MIN, uint(19959L));

  public static final NodeId PubSubDiagnosticsReaderGroupType_Counters_DecryptionErrors_Active =
      new NodeId(UShort.MIN, uint(19960L));

  public static final NodeId
      PubSubDiagnosticsReaderGroupType_Counters_DecryptionErrors_Classification =
          new NodeId(UShort.MIN, uint(19961L));

  public static final NodeId
      PubSubDiagnosticsReaderGroupType_Counters_DecryptionErrors_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19962L));

  public static final NodeId
      PubSubDiagnosticsReaderGroupType_Counters_DecryptionErrors_TimeFirstChange =
          new NodeId(UShort.MIN, uint(19963L));

  public static final NodeId PubSubDiagnosticsReaderGroupType_LiveValues_ConfiguredDataSetReaders =
      new NodeId(UShort.MIN, uint(19964L));

  public static final NodeId
      PubSubDiagnosticsReaderGroupType_LiveValues_ConfiguredDataSetReaders_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19965L));

  public static final NodeId PubSubDiagnosticsReaderGroupType_LiveValues_OperationalDataSetReaders =
      new NodeId(UShort.MIN, uint(19966L));

  public static final NodeId
      PubSubDiagnosticsReaderGroupType_LiveValues_OperationalDataSetReaders_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19967L));

  public static final NodeId PubSubDiagnosticsDataSetWriterType =
      new NodeId(UShort.MIN, uint(19968L));

  public static final NodeId AlarmSuppressionGroupType_AlarmCondition_Placeholder_ConditionName =
      new NodeId(UShort.MIN, uint(19969L));

  public static final NodeId AlarmSuppressionGroupType_AlarmCondition_Placeholder_BranchId =
      new NodeId(UShort.MIN, uint(19970L));

  public static final NodeId AlarmSuppressionGroupType_AlarmCondition_Placeholder_Retain =
      new NodeId(UShort.MIN, uint(19971L));

  public static final NodeId AlarmSuppressionGroupType_AlarmCondition_Placeholder_EnabledState =
      new NodeId(UShort.MIN, uint(19972L));

  public static final NodeId AlarmSuppressionGroupType_AlarmCondition_Placeholder_EnabledState_Id =
      new NodeId(UShort.MIN, uint(19973L));

  public static final NodeId
      AlarmSuppressionGroupType_AlarmCondition_Placeholder_EnabledState_Name =
          new NodeId(UShort.MIN, uint(19974L));

  public static final NodeId
      AlarmSuppressionGroupType_AlarmCondition_Placeholder_EnabledState_Number =
          new NodeId(UShort.MIN, uint(19975L));

  public static final NodeId
      AlarmSuppressionGroupType_AlarmCondition_Placeholder_EnabledState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(19976L));

  public static final NodeId
      AlarmSuppressionGroupType_AlarmCondition_Placeholder_EnabledState_TransitionTime =
          new NodeId(UShort.MIN, uint(19977L));

  public static final NodeId
      AlarmSuppressionGroupType_AlarmCondition_Placeholder_EnabledState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(19978L));

  public static final NodeId
      AlarmSuppressionGroupType_AlarmCondition_Placeholder_EnabledState_TrueState =
          new NodeId(UShort.MIN, uint(19979L));

  public static final NodeId
      AlarmSuppressionGroupType_AlarmCondition_Placeholder_EnabledState_FalseState =
          new NodeId(UShort.MIN, uint(19980L));

  public static final NodeId AlarmSuppressionGroupType_AlarmCondition_Placeholder_Quality =
      new NodeId(UShort.MIN, uint(19981L));

  public static final NodeId PubSubDiagnosticsDataSetWriterType_Counters =
      new NodeId(UShort.MIN, uint(19982L));

  public static final NodeId PubSubDiagnosticsDataSetWriterType_Counters_StateError =
      new NodeId(UShort.MIN, uint(19983L));

  public static final NodeId PubSubDiagnosticsDataSetWriterType_Counters_StateError_Active =
      new NodeId(UShort.MIN, uint(19984L));

  public static final NodeId PubSubDiagnosticsDataSetWriterType_Counters_StateError_Classification =
      new NodeId(UShort.MIN, uint(19985L));

  public static final NodeId
      PubSubDiagnosticsDataSetWriterType_Counters_StateError_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19986L));

  public static final NodeId
      PubSubDiagnosticsDataSetWriterType_Counters_StateError_TimeFirstChange =
          new NodeId(UShort.MIN, uint(19987L));

  public static final NodeId PubSubDiagnosticsDataSetWriterType_Counters_StateOperationalByMethod =
      new NodeId(UShort.MIN, uint(19988L));

  public static final NodeId
      PubSubDiagnosticsDataSetWriterType_Counters_StateOperationalByMethod_Active =
          new NodeId(UShort.MIN, uint(19989L));

  public static final NodeId
      PubSubDiagnosticsDataSetWriterType_Counters_StateOperationalByMethod_Classification =
          new NodeId(UShort.MIN, uint(19990L));

  public static final NodeId
      PubSubDiagnosticsDataSetWriterType_Counters_StateOperationalByMethod_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19991L));

  public static final NodeId
      PubSubDiagnosticsDataSetWriterType_Counters_StateOperationalByMethod_TimeFirstChange =
          new NodeId(UShort.MIN, uint(19992L));

  public static final NodeId PubSubDiagnosticsDataSetWriterType_Counters_StateOperationalByParent =
      new NodeId(UShort.MIN, uint(19993L));

  public static final NodeId
      PubSubDiagnosticsDataSetWriterType_Counters_StateOperationalByParent_Active =
          new NodeId(UShort.MIN, uint(19994L));

  public static final NodeId
      PubSubDiagnosticsDataSetWriterType_Counters_StateOperationalByParent_Classification =
          new NodeId(UShort.MIN, uint(19995L));

  public static final NodeId
      PubSubDiagnosticsDataSetWriterType_Counters_StateOperationalByParent_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19996L));

  public static final NodeId
      PubSubDiagnosticsDataSetWriterType_Counters_StateOperationalByParent_TimeFirstChange =
          new NodeId(UShort.MIN, uint(19997L));

  public static final NodeId PubSubDiagnosticsDataSetWriterType_Counters_StateOperationalFromError =
      new NodeId(UShort.MIN, uint(19998L));

  public static final NodeId
      PubSubDiagnosticsDataSetWriterType_Counters_StateOperationalFromError_Active =
          new NodeId(UShort.MIN, uint(19999L));

  public static final NodeId
      PubSubDiagnosticsDataSetWriterType_Counters_StateOperationalFromError_Classification =
          new NodeId(UShort.MIN, uint(20000L));

  public static final NodeId
      PubSubDiagnosticsDataSetWriterType_Counters_StateOperationalFromError_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(20001L));

  public static final NodeId
      PubSubDiagnosticsDataSetWriterType_Counters_StateOperationalFromError_TimeFirstChange =
          new NodeId(UShort.MIN, uint(20002L));

  public static final NodeId PubSubDiagnosticsDataSetWriterType_Counters_StatePausedByParent =
      new NodeId(UShort.MIN, uint(20003L));

  public static final NodeId
      PubSubDiagnosticsDataSetWriterType_Counters_StatePausedByParent_Active =
          new NodeId(UShort.MIN, uint(20004L));

  public static final NodeId
      PubSubDiagnosticsDataSetWriterType_Counters_StatePausedByParent_Classification =
          new NodeId(UShort.MIN, uint(20005L));

  public static final NodeId
      PubSubDiagnosticsDataSetWriterType_Counters_StatePausedByParent_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(20006L));

  public static final NodeId
      PubSubDiagnosticsDataSetWriterType_Counters_StatePausedByParent_TimeFirstChange =
          new NodeId(UShort.MIN, uint(20007L));

  public static final NodeId PubSubDiagnosticsDataSetWriterType_Counters_StateDisabledByMethod =
      new NodeId(UShort.MIN, uint(20008L));

  public static final NodeId
      PubSubDiagnosticsDataSetWriterType_Counters_StateDisabledByMethod_Active =
          new NodeId(UShort.MIN, uint(20009L));

  public static final NodeId
      PubSubDiagnosticsDataSetWriterType_Counters_StateDisabledByMethod_Classification =
          new NodeId(UShort.MIN, uint(20010L));

  public static final NodeId
      PubSubDiagnosticsDataSetWriterType_Counters_StateDisabledByMethod_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(20011L));

  public static final NodeId
      PubSubDiagnosticsDataSetWriterType_Counters_StateDisabledByMethod_TimeFirstChange =
          new NodeId(UShort.MIN, uint(20012L));

  public static final NodeId PubSubDiagnosticsDataSetWriterType_LiveValues =
      new NodeId(UShort.MIN, uint(20013L));

  public static final NodeId PubSubDiagnosticsDataSetWriterType_Counters_FailedDataSetMessages =
      new NodeId(UShort.MIN, uint(20014L));

  public static final NodeId
      PubSubDiagnosticsDataSetWriterType_Counters_FailedDataSetMessages_Active =
          new NodeId(UShort.MIN, uint(20015L));

  public static final NodeId
      PubSubDiagnosticsDataSetWriterType_Counters_FailedDataSetMessages_Classification =
          new NodeId(UShort.MIN, uint(20016L));

  public static final NodeId
      PubSubDiagnosticsDataSetWriterType_Counters_FailedDataSetMessages_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(20017L));

  public static final NodeId
      PubSubDiagnosticsDataSetWriterType_Counters_FailedDataSetMessages_TimeFirstChange =
          new NodeId(UShort.MIN, uint(20018L));

  public static final NodeId PubSubDiagnosticsDataSetWriterType_LiveValues_MessageSequenceNumber =
      new NodeId(UShort.MIN, uint(20019L));

  public static final NodeId
      PubSubDiagnosticsDataSetWriterType_LiveValues_MessageSequenceNumber_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(20020L));

  public static final NodeId PubSubDiagnosticsDataSetWriterType_LiveValues_StatusCode =
      new NodeId(UShort.MIN, uint(20021L));

  public static final NodeId
      PubSubDiagnosticsDataSetWriterType_LiveValues_StatusCode_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(20022L));

  public static final NodeId PubSubDiagnosticsDataSetWriterType_LiveValues_MajorVersion =
      new NodeId(UShort.MIN, uint(20023L));

  public static final NodeId
      PubSubDiagnosticsDataSetWriterType_LiveValues_MajorVersion_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(20024L));

  public static final NodeId PubSubDiagnosticsDataSetWriterType_LiveValues_MinorVersion =
      new NodeId(UShort.MIN, uint(20025L));

  public static final NodeId
      PubSubDiagnosticsDataSetWriterType_LiveValues_MinorVersion_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(20026L));

  public static final NodeId PubSubDiagnosticsDataSetReaderType =
      new NodeId(UShort.MIN, uint(20027L));

  public static final NodeId
      AlarmSuppressionGroupType_AlarmCondition_Placeholder_Quality_SourceTimestamp =
          new NodeId(UShort.MIN, uint(20028L));

  public static final NodeId AlarmSuppressionGroupType_AlarmCondition_Placeholder_LastSeverity =
      new NodeId(UShort.MIN, uint(20029L));

  public static final NodeId
      AlarmSuppressionGroupType_AlarmCondition_Placeholder_LastSeverity_SourceTimestamp =
          new NodeId(UShort.MIN, uint(20030L));

  public static final NodeId AlarmSuppressionGroupType_AlarmCondition_Placeholder_Comment =
      new NodeId(UShort.MIN, uint(20031L));

  public static final NodeId
      AlarmSuppressionGroupType_AlarmCondition_Placeholder_Comment_SourceTimestamp =
          new NodeId(UShort.MIN, uint(20032L));

  public static final NodeId AlarmSuppressionGroupType_AlarmCondition_Placeholder_ClientUserId =
      new NodeId(UShort.MIN, uint(20033L));

  public static final NodeId AlarmSuppressionGroupType_AlarmCondition_Placeholder_Disable =
      new NodeId(UShort.MIN, uint(20034L));

  public static final NodeId AlarmSuppressionGroupType_AlarmCondition_Placeholder_Enable =
      new NodeId(UShort.MIN, uint(20035L));

  public static final NodeId AlarmSuppressionGroupType_AlarmCondition_Placeholder_AddComment =
      new NodeId(UShort.MIN, uint(20036L));

  public static final NodeId
      AlarmSuppressionGroupType_AlarmCondition_Placeholder_AddComment_InputArguments =
          new NodeId(UShort.MIN, uint(20037L));

  public static final NodeId AlarmSuppressionGroupType_AlarmCondition_Placeholder_AckedState =
      new NodeId(UShort.MIN, uint(20038L));

  public static final NodeId AlarmSuppressionGroupType_AlarmCondition_Placeholder_AckedState_Id =
      new NodeId(UShort.MIN, uint(20039L));

  public static final NodeId AlarmSuppressionGroupType_AlarmCondition_Placeholder_AckedState_Name =
      new NodeId(UShort.MIN, uint(20040L));

  public static final NodeId PubSubDiagnosticsDataSetReaderType_Counters =
      new NodeId(UShort.MIN, uint(20041L));

  public static final NodeId PubSubDiagnosticsDataSetReaderType_Counters_StateError =
      new NodeId(UShort.MIN, uint(20042L));

  public static final NodeId PubSubDiagnosticsDataSetReaderType_Counters_StateError_Active =
      new NodeId(UShort.MIN, uint(20043L));

  public static final NodeId PubSubDiagnosticsDataSetReaderType_Counters_StateError_Classification =
      new NodeId(UShort.MIN, uint(20044L));

  public static final NodeId
      PubSubDiagnosticsDataSetReaderType_Counters_StateError_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(20045L));

  public static final NodeId
      PubSubDiagnosticsDataSetReaderType_Counters_StateError_TimeFirstChange =
          new NodeId(UShort.MIN, uint(20046L));

  public static final NodeId PubSubDiagnosticsDataSetReaderType_Counters_StateOperationalByMethod =
      new NodeId(UShort.MIN, uint(20047L));

  public static final NodeId
      PubSubDiagnosticsDataSetReaderType_Counters_StateOperationalByMethod_Active =
          new NodeId(UShort.MIN, uint(20048L));

  public static final NodeId
      PubSubDiagnosticsDataSetReaderType_Counters_StateOperationalByMethod_Classification =
          new NodeId(UShort.MIN, uint(20049L));

  public static final NodeId
      PubSubDiagnosticsDataSetReaderType_Counters_StateOperationalByMethod_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(20050L));

  public static final NodeId
      PubSubDiagnosticsDataSetReaderType_Counters_StateOperationalByMethod_TimeFirstChange =
          new NodeId(UShort.MIN, uint(20051L));

  public static final NodeId PubSubDiagnosticsDataSetReaderType_Counters_StateOperationalByParent =
      new NodeId(UShort.MIN, uint(20052L));

  public static final NodeId
      PubSubDiagnosticsDataSetReaderType_Counters_StateOperationalByParent_Active =
          new NodeId(UShort.MIN, uint(20053L));

  public static final NodeId
      PubSubDiagnosticsDataSetReaderType_Counters_StateOperationalByParent_Classification =
          new NodeId(UShort.MIN, uint(20054L));

  public static final NodeId
      PubSubDiagnosticsDataSetReaderType_Counters_StateOperationalByParent_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(20055L));

  public static final NodeId
      PubSubDiagnosticsDataSetReaderType_Counters_StateOperationalByParent_TimeFirstChange =
          new NodeId(UShort.MIN, uint(20056L));

  public static final NodeId PubSubDiagnosticsDataSetReaderType_Counters_StateOperationalFromError =
      new NodeId(UShort.MIN, uint(20057L));

  public static final NodeId
      PubSubDiagnosticsDataSetReaderType_Counters_StateOperationalFromError_Active =
          new NodeId(UShort.MIN, uint(20058L));

  public static final NodeId
      PubSubDiagnosticsDataSetReaderType_Counters_StateOperationalFromError_Classification =
          new NodeId(UShort.MIN, uint(20059L));

  public static final NodeId
      PubSubDiagnosticsDataSetReaderType_Counters_StateOperationalFromError_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(20060L));

  public static final NodeId
      PubSubDiagnosticsDataSetReaderType_Counters_StateOperationalFromError_TimeFirstChange =
          new NodeId(UShort.MIN, uint(20061L));

  public static final NodeId PubSubDiagnosticsDataSetReaderType_Counters_StatePausedByParent =
      new NodeId(UShort.MIN, uint(20062L));

  public static final NodeId
      PubSubDiagnosticsDataSetReaderType_Counters_StatePausedByParent_Active =
          new NodeId(UShort.MIN, uint(20063L));

  public static final NodeId
      PubSubDiagnosticsDataSetReaderType_Counters_StatePausedByParent_Classification =
          new NodeId(UShort.MIN, uint(20064L));

  public static final NodeId
      PubSubDiagnosticsDataSetReaderType_Counters_StatePausedByParent_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(20065L));

  public static final NodeId
      PubSubDiagnosticsDataSetReaderType_Counters_StatePausedByParent_TimeFirstChange =
          new NodeId(UShort.MIN, uint(20066L));

  public static final NodeId PubSubDiagnosticsDataSetReaderType_Counters_StateDisabledByMethod =
      new NodeId(UShort.MIN, uint(20067L));

  public static final NodeId
      PubSubDiagnosticsDataSetReaderType_Counters_StateDisabledByMethod_Active =
          new NodeId(UShort.MIN, uint(20068L));

  public static final NodeId
      PubSubDiagnosticsDataSetReaderType_Counters_StateDisabledByMethod_Classification =
          new NodeId(UShort.MIN, uint(20069L));

  public static final NodeId
      PubSubDiagnosticsDataSetReaderType_Counters_StateDisabledByMethod_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(20070L));

  public static final NodeId
      PubSubDiagnosticsDataSetReaderType_Counters_StateDisabledByMethod_TimeFirstChange =
          new NodeId(UShort.MIN, uint(20071L));

  public static final NodeId PubSubDiagnosticsDataSetReaderType_LiveValues =
      new NodeId(UShort.MIN, uint(20072L));

  public static final NodeId PubSubDiagnosticsDataSetReaderType_Counters_FailedDataSetMessages =
      new NodeId(UShort.MIN, uint(20073L));

  public static final NodeId
      PubSubDiagnosticsDataSetReaderType_Counters_FailedDataSetMessages_Active =
          new NodeId(UShort.MIN, uint(20074L));

  public static final NodeId
      PubSubDiagnosticsDataSetReaderType_Counters_FailedDataSetMessages_Classification =
          new NodeId(UShort.MIN, uint(20075L));

  public static final NodeId
      PubSubDiagnosticsDataSetReaderType_Counters_FailedDataSetMessages_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(20076L));

  public static final NodeId
      PubSubDiagnosticsDataSetReaderType_Counters_FailedDataSetMessages_TimeFirstChange =
          new NodeId(UShort.MIN, uint(20077L));

  public static final NodeId PubSubDiagnosticsDataSetReaderType_Counters_DecryptionErrors =
      new NodeId(UShort.MIN, uint(20078L));

  public static final NodeId PubSubDiagnosticsDataSetReaderType_Counters_DecryptionErrors_Active =
      new NodeId(UShort.MIN, uint(20079L));

  public static final NodeId
      PubSubDiagnosticsDataSetReaderType_Counters_DecryptionErrors_Classification =
          new NodeId(UShort.MIN, uint(20080L));

  public static final NodeId
      PubSubDiagnosticsDataSetReaderType_Counters_DecryptionErrors_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(20081L));

  public static final NodeId
      PubSubDiagnosticsDataSetReaderType_Counters_DecryptionErrors_TimeFirstChange =
          new NodeId(UShort.MIN, uint(20082L));

  public static final NodeId PubSubDiagnosticsDataSetReaderType_LiveValues_MessageSequenceNumber =
      new NodeId(UShort.MIN, uint(20083L));

  public static final NodeId
      PubSubDiagnosticsDataSetReaderType_LiveValues_MessageSequenceNumber_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(20084L));

  public static final NodeId PubSubDiagnosticsDataSetReaderType_LiveValues_StatusCode =
      new NodeId(UShort.MIN, uint(20085L));

  public static final NodeId
      PubSubDiagnosticsDataSetReaderType_LiveValues_StatusCode_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(20086L));

  public static final NodeId PubSubDiagnosticsDataSetReaderType_LiveValues_MajorVersion =
      new NodeId(UShort.MIN, uint(20087L));

  public static final NodeId
      PubSubDiagnosticsDataSetReaderType_LiveValues_MajorVersion_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(20088L));

  public static final NodeId PubSubDiagnosticsDataSetReaderType_LiveValues_MinorVersion =
      new NodeId(UShort.MIN, uint(20089L));

  public static final NodeId
      PubSubDiagnosticsDataSetReaderType_LiveValues_MinorVersion_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(20090L));

  public static final NodeId PubSubDiagnosticsDataSetReaderType_LiveValues_SecurityTokenID =
      new NodeId(UShort.MIN, uint(20091L));

  public static final NodeId
      PubSubDiagnosticsDataSetReaderType_LiveValues_SecurityTokenID_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(20092L));

  public static final NodeId PubSubDiagnosticsDataSetReaderType_LiveValues_TimeToNextTokenID =
      new NodeId(UShort.MIN, uint(20093L));

  public static final NodeId
      PubSubDiagnosticsDataSetReaderType_LiveValues_TimeToNextTokenID_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(20094L));

  public static final NodeId
      CertificateGroupType_CertificateExpired_ShelvingState_AvailableTransitions =
          new NodeId(UShort.MIN, uint(20095L));

  public static final NodeId CertificateGroupType_CertificateExpired_ShelvingState_UnshelveTime =
      new NodeId(UShort.MIN, uint(20096L));

  public static final NodeId CertificateGroupType_CertificateExpired_ShelvingState_TimedShelve =
      new NodeId(UShort.MIN, uint(20097L));

  public static final NodeId
      CertificateGroupType_CertificateExpired_ShelvingState_TimedShelve_InputArguments =
          new NodeId(UShort.MIN, uint(20098L));

  public static final NodeId CertificateGroupType_CertificateExpired_ShelvingState_Unshelve =
      new NodeId(UShort.MIN, uint(20099L));

  public static final NodeId CertificateGroupType_CertificateExpired_ShelvingState_OneShotShelve =
      new NodeId(UShort.MIN, uint(20100L));

  public static final NodeId CertificateGroupType_CertificateExpired_SuppressedOrShelved =
      new NodeId(UShort.MIN, uint(20101L));

  public static final NodeId CertificateGroupType_CertificateExpired_MaxTimeShelved =
      new NodeId(UShort.MIN, uint(20102L));

  public static final NodeId CertificateGroupType_CertificateExpired_AudibleEnabled =
      new NodeId(UShort.MIN, uint(20103L));

  public static final NodeId CertificateGroupType_CertificateExpired_AudibleSound =
      new NodeId(UShort.MIN, uint(20104L));

  public static final NodeId CertificateGroupType_CertificateExpired_AudibleSound_ListId =
      new NodeId(UShort.MIN, uint(20105L));

  public static final NodeId CertificateGroupType_CertificateExpired_AudibleSound_AgencyId =
      new NodeId(UShort.MIN, uint(20106L));

  public static final NodeId CertificateGroupType_CertificateExpired_AudibleSound_VersionId =
      new NodeId(UShort.MIN, uint(20107L));

  public static final NodeId CertificateGroupType_CertificateExpired_SilenceState =
      new NodeId(UShort.MIN, uint(20108L));

  public static final NodeId CertificateGroupType_CertificateExpired_SilenceState_Id =
      new NodeId(UShort.MIN, uint(20109L));

  public static final NodeId CertificateGroupType_CertificateExpired_SilenceState_Name =
      new NodeId(UShort.MIN, uint(20110L));

  public static final NodeId CertificateGroupType_CertificateExpired_SilenceState_Number =
      new NodeId(UShort.MIN, uint(20111L));

  public static final NodeId
      CertificateGroupType_CertificateExpired_SilenceState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20112L));

  public static final NodeId CertificateGroupType_CertificateExpired_SilenceState_TransitionTime =
      new NodeId(UShort.MIN, uint(20113L));

  public static final NodeId
      CertificateGroupType_CertificateExpired_SilenceState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20114L));

  public static final NodeId CertificateGroupType_CertificateExpired_SilenceState_TrueState =
      new NodeId(UShort.MIN, uint(20115L));

  public static final NodeId CertificateGroupType_CertificateExpired_SilenceState_FalseState =
      new NodeId(UShort.MIN, uint(20116L));

  public static final NodeId CertificateGroupType_CertificateExpired_OnDelay =
      new NodeId(UShort.MIN, uint(20117L));

  public static final NodeId CertificateGroupType_CertificateExpired_OffDelay =
      new NodeId(UShort.MIN, uint(20118L));

  public static final NodeId CertificateGroupType_CertificateExpired_FirstInGroupFlag =
      new NodeId(UShort.MIN, uint(20119L));

  public static final NodeId CertificateGroupType_CertificateExpired_FirstInGroup =
      new NodeId(UShort.MIN, uint(20120L));

  public static final NodeId CertificateGroupType_CertificateExpired_LatchedState =
      new NodeId(UShort.MIN, uint(20121L));

  public static final NodeId CertificateGroupType_CertificateExpired_LatchedState_Id =
      new NodeId(UShort.MIN, uint(20122L));

  public static final NodeId CertificateGroupType_CertificateExpired_LatchedState_Name =
      new NodeId(UShort.MIN, uint(20123L));

  public static final NodeId CertificateGroupType_CertificateExpired_LatchedState_Number =
      new NodeId(UShort.MIN, uint(20124L));

  public static final NodeId
      CertificateGroupType_CertificateExpired_LatchedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20125L));

  public static final NodeId CertificateGroupType_CertificateExpired_LatchedState_TransitionTime =
      new NodeId(UShort.MIN, uint(20126L));

  public static final NodeId
      CertificateGroupType_CertificateExpired_LatchedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20127L));

  public static final NodeId CertificateGroupType_CertificateExpired_LatchedState_TrueState =
      new NodeId(UShort.MIN, uint(20128L));

  public static final NodeId CertificateGroupType_CertificateExpired_LatchedState_FalseState =
      new NodeId(UShort.MIN, uint(20129L));

  public static final NodeId CertificateGroupType_CertificateExpired_ReAlarmTime =
      new NodeId(UShort.MIN, uint(20130L));

  public static final NodeId CertificateGroupType_CertificateExpired_ReAlarmRepeatCount =
      new NodeId(UShort.MIN, uint(20131L));

  public static final NodeId CertificateGroupType_CertificateExpired_Silence =
      new NodeId(UShort.MIN, uint(20132L));

  public static final NodeId CertificateGroupType_CertificateExpired_Suppress =
      new NodeId(UShort.MIN, uint(20133L));

  public static final NodeId CertificateGroupType_CertificateExpired_Unsuppress =
      new NodeId(UShort.MIN, uint(20134L));

  public static final NodeId CertificateGroupType_CertificateExpired_RemoveFromService =
      new NodeId(UShort.MIN, uint(20135L));

  public static final NodeId CertificateGroupType_CertificateExpired_PlaceInService =
      new NodeId(UShort.MIN, uint(20136L));

  public static final NodeId CertificateGroupType_CertificateExpired_Reset =
      new NodeId(UShort.MIN, uint(20137L));

  public static final NodeId CertificateGroupType_CertificateExpired_NormalState =
      new NodeId(UShort.MIN, uint(20138L));

  public static final NodeId CertificateGroupType_CertificateExpired_ExpirationDate =
      new NodeId(UShort.MIN, uint(20139L));

  public static final NodeId CertificateGroupType_CertificateExpired_ExpirationLimit =
      new NodeId(UShort.MIN, uint(20140L));

  public static final NodeId CertificateGroupType_CertificateExpired_CertificateType =
      new NodeId(UShort.MIN, uint(20141L));

  public static final NodeId CertificateGroupType_CertificateExpired_Certificate =
      new NodeId(UShort.MIN, uint(20142L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate =
      new NodeId(UShort.MIN, uint(20143L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_EventId =
      new NodeId(UShort.MIN, uint(20144L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_EventType =
      new NodeId(UShort.MIN, uint(20145L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_SourceNode =
      new NodeId(UShort.MIN, uint(20146L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_SourceName =
      new NodeId(UShort.MIN, uint(20147L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_Time =
      new NodeId(UShort.MIN, uint(20148L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_ReceiveTime =
      new NodeId(UShort.MIN, uint(20149L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_LocalTime =
      new NodeId(UShort.MIN, uint(20150L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_Message =
      new NodeId(UShort.MIN, uint(20151L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_Severity =
      new NodeId(UShort.MIN, uint(20152L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_ConditionClassId =
      new NodeId(UShort.MIN, uint(20153L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_ConditionClassName =
      new NodeId(UShort.MIN, uint(20154L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_ConditionSubClassId =
      new NodeId(UShort.MIN, uint(20155L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_ConditionSubClassName =
      new NodeId(UShort.MIN, uint(20156L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_ConditionName =
      new NodeId(UShort.MIN, uint(20157L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_BranchId =
      new NodeId(UShort.MIN, uint(20158L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_Retain =
      new NodeId(UShort.MIN, uint(20159L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_EnabledState =
      new NodeId(UShort.MIN, uint(20160L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_EnabledState_Id =
      new NodeId(UShort.MIN, uint(20161L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_EnabledState_Name =
      new NodeId(UShort.MIN, uint(20162L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_EnabledState_Number =
      new NodeId(UShort.MIN, uint(20163L));

  public static final NodeId
      CertificateGroupType_TrustListOutOfDate_EnabledState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20164L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_EnabledState_TransitionTime =
      new NodeId(UShort.MIN, uint(20165L));

  public static final NodeId
      CertificateGroupType_TrustListOutOfDate_EnabledState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20166L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_EnabledState_TrueState =
      new NodeId(UShort.MIN, uint(20167L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_EnabledState_FalseState =
      new NodeId(UShort.MIN, uint(20168L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_Quality =
      new NodeId(UShort.MIN, uint(20169L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_Quality_SourceTimestamp =
      new NodeId(UShort.MIN, uint(20170L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_LastSeverity =
      new NodeId(UShort.MIN, uint(20171L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_LastSeverity_SourceTimestamp =
      new NodeId(UShort.MIN, uint(20172L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_Comment =
      new NodeId(UShort.MIN, uint(20173L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_Comment_SourceTimestamp =
      new NodeId(UShort.MIN, uint(20174L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_ClientUserId =
      new NodeId(UShort.MIN, uint(20175L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_Disable =
      new NodeId(UShort.MIN, uint(20176L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_Enable =
      new NodeId(UShort.MIN, uint(20177L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_AddComment =
      new NodeId(UShort.MIN, uint(20178L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_AddComment_InputArguments =
      new NodeId(UShort.MIN, uint(20179L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_AckedState =
      new NodeId(UShort.MIN, uint(20180L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_AckedState_Id =
      new NodeId(UShort.MIN, uint(20181L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_AckedState_Name =
      new NodeId(UShort.MIN, uint(20182L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_AckedState_Number =
      new NodeId(UShort.MIN, uint(20183L));

  public static final NodeId
      CertificateGroupType_TrustListOutOfDate_AckedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20184L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_AckedState_TransitionTime =
      new NodeId(UShort.MIN, uint(20185L));

  public static final NodeId
      CertificateGroupType_TrustListOutOfDate_AckedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20186L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_AckedState_TrueState =
      new NodeId(UShort.MIN, uint(20187L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_AckedState_FalseState =
      new NodeId(UShort.MIN, uint(20188L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_ConfirmedState =
      new NodeId(UShort.MIN, uint(20189L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_ConfirmedState_Id =
      new NodeId(UShort.MIN, uint(20190L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_ConfirmedState_Name =
      new NodeId(UShort.MIN, uint(20191L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_ConfirmedState_Number =
      new NodeId(UShort.MIN, uint(20192L));

  public static final NodeId
      CertificateGroupType_TrustListOutOfDate_ConfirmedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20193L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_ConfirmedState_TransitionTime =
      new NodeId(UShort.MIN, uint(20194L));

  public static final NodeId
      CertificateGroupType_TrustListOutOfDate_ConfirmedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20195L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_ConfirmedState_TrueState =
      new NodeId(UShort.MIN, uint(20196L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_ConfirmedState_FalseState =
      new NodeId(UShort.MIN, uint(20197L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_Acknowledge =
      new NodeId(UShort.MIN, uint(20198L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_Acknowledge_InputArguments =
      new NodeId(UShort.MIN, uint(20199L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_Confirm =
      new NodeId(UShort.MIN, uint(20200L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_Confirm_InputArguments =
      new NodeId(UShort.MIN, uint(20201L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_ActiveState =
      new NodeId(UShort.MIN, uint(20202L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_ActiveState_Id =
      new NodeId(UShort.MIN, uint(20203L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_ActiveState_Name =
      new NodeId(UShort.MIN, uint(20204L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_ActiveState_Number =
      new NodeId(UShort.MIN, uint(20205L));

  public static final NodeId
      CertificateGroupType_TrustListOutOfDate_ActiveState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20206L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_ActiveState_TransitionTime =
      new NodeId(UShort.MIN, uint(20207L));

  public static final NodeId
      CertificateGroupType_TrustListOutOfDate_ActiveState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20208L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_ActiveState_TrueState =
      new NodeId(UShort.MIN, uint(20209L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_ActiveState_FalseState =
      new NodeId(UShort.MIN, uint(20210L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_InputNode =
      new NodeId(UShort.MIN, uint(20211L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_SuppressedState =
      new NodeId(UShort.MIN, uint(20212L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_SuppressedState_Id =
      new NodeId(UShort.MIN, uint(20213L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_SuppressedState_Name =
      new NodeId(UShort.MIN, uint(20214L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_SuppressedState_Number =
      new NodeId(UShort.MIN, uint(20215L));

  public static final NodeId
      CertificateGroupType_TrustListOutOfDate_SuppressedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20216L));

  public static final NodeId
      CertificateGroupType_TrustListOutOfDate_SuppressedState_TransitionTime =
          new NodeId(UShort.MIN, uint(20217L));

  public static final NodeId
      CertificateGroupType_TrustListOutOfDate_SuppressedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20218L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_SuppressedState_TrueState =
      new NodeId(UShort.MIN, uint(20219L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_SuppressedState_FalseState =
      new NodeId(UShort.MIN, uint(20220L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_OutOfServiceState =
      new NodeId(UShort.MIN, uint(20221L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_OutOfServiceState_Id =
      new NodeId(UShort.MIN, uint(20222L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_OutOfServiceState_Name =
      new NodeId(UShort.MIN, uint(20223L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_OutOfServiceState_Number =
      new NodeId(UShort.MIN, uint(20224L));

  public static final NodeId
      CertificateGroupType_TrustListOutOfDate_OutOfServiceState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20225L));

  public static final NodeId
      CertificateGroupType_TrustListOutOfDate_OutOfServiceState_TransitionTime =
          new NodeId(UShort.MIN, uint(20226L));

  public static final NodeId
      CertificateGroupType_TrustListOutOfDate_OutOfServiceState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20227L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_OutOfServiceState_TrueState =
      new NodeId(UShort.MIN, uint(20228L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_OutOfServiceState_FalseState =
      new NodeId(UShort.MIN, uint(20229L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_ShelvingState =
      new NodeId(UShort.MIN, uint(20230L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_ShelvingState_CurrentState =
      new NodeId(UShort.MIN, uint(20231L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_ShelvingState_CurrentState_Id =
      new NodeId(UShort.MIN, uint(20232L));

  public static final NodeId
      CertificateGroupType_TrustListOutOfDate_ShelvingState_CurrentState_Name =
          new NodeId(UShort.MIN, uint(20233L));

  public static final NodeId
      CertificateGroupType_TrustListOutOfDate_ShelvingState_CurrentState_Number =
          new NodeId(UShort.MIN, uint(20234L));

  public static final NodeId
      CertificateGroupType_TrustListOutOfDate_ShelvingState_CurrentState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20235L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_ShelvingState_LastTransition =
      new NodeId(UShort.MIN, uint(20236L));

  public static final NodeId
      CertificateGroupType_TrustListOutOfDate_ShelvingState_LastTransition_Id =
          new NodeId(UShort.MIN, uint(20237L));

  public static final NodeId
      CertificateGroupType_TrustListOutOfDate_ShelvingState_LastTransition_Name =
          new NodeId(UShort.MIN, uint(20238L));

  public static final NodeId
      CertificateGroupType_TrustListOutOfDate_ShelvingState_LastTransition_Number =
          new NodeId(UShort.MIN, uint(20239L));

  public static final NodeId
      CertificateGroupType_TrustListOutOfDate_ShelvingState_LastTransition_TransitionTime =
          new NodeId(UShort.MIN, uint(20240L));

  public static final NodeId
      CertificateGroupType_TrustListOutOfDate_ShelvingState_LastTransition_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20241L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_ShelvingState_AvailableStates =
      new NodeId(UShort.MIN, uint(20242L));

  public static final NodeId
      CertificateGroupType_TrustListOutOfDate_ShelvingState_AvailableTransitions =
          new NodeId(UShort.MIN, uint(20243L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_ShelvingState_UnshelveTime =
      new NodeId(UShort.MIN, uint(20244L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_ShelvingState_TimedShelve =
      new NodeId(UShort.MIN, uint(20245L));

  public static final NodeId
      CertificateGroupType_TrustListOutOfDate_ShelvingState_TimedShelve_InputArguments =
          new NodeId(UShort.MIN, uint(20246L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_ShelvingState_Unshelve =
      new NodeId(UShort.MIN, uint(20247L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_ShelvingState_OneShotShelve =
      new NodeId(UShort.MIN, uint(20248L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_SuppressedOrShelved =
      new NodeId(UShort.MIN, uint(20249L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_MaxTimeShelved =
      new NodeId(UShort.MIN, uint(20250L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_AudibleEnabled =
      new NodeId(UShort.MIN, uint(20251L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_AudibleSound =
      new NodeId(UShort.MIN, uint(20252L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_AudibleSound_ListId =
      new NodeId(UShort.MIN, uint(20253L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_AudibleSound_AgencyId =
      new NodeId(UShort.MIN, uint(20254L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_AudibleSound_VersionId =
      new NodeId(UShort.MIN, uint(20255L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_SilenceState =
      new NodeId(UShort.MIN, uint(20256L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_SilenceState_Id =
      new NodeId(UShort.MIN, uint(20257L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_SilenceState_Name =
      new NodeId(UShort.MIN, uint(20258L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_SilenceState_Number =
      new NodeId(UShort.MIN, uint(20259L));

  public static final NodeId
      CertificateGroupType_TrustListOutOfDate_SilenceState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20260L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_SilenceState_TransitionTime =
      new NodeId(UShort.MIN, uint(20261L));

  public static final NodeId
      CertificateGroupType_TrustListOutOfDate_SilenceState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20262L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_SilenceState_TrueState =
      new NodeId(UShort.MIN, uint(20263L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_SilenceState_FalseState =
      new NodeId(UShort.MIN, uint(20264L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_OnDelay =
      new NodeId(UShort.MIN, uint(20265L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_OffDelay =
      new NodeId(UShort.MIN, uint(20266L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_FirstInGroupFlag =
      new NodeId(UShort.MIN, uint(20267L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_FirstInGroup =
      new NodeId(UShort.MIN, uint(20268L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_LatchedState =
      new NodeId(UShort.MIN, uint(20269L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_LatchedState_Id =
      new NodeId(UShort.MIN, uint(20270L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_LatchedState_Name =
      new NodeId(UShort.MIN, uint(20271L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_LatchedState_Number =
      new NodeId(UShort.MIN, uint(20272L));

  public static final NodeId
      CertificateGroupType_TrustListOutOfDate_LatchedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20273L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_LatchedState_TransitionTime =
      new NodeId(UShort.MIN, uint(20274L));

  public static final NodeId
      CertificateGroupType_TrustListOutOfDate_LatchedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20275L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_LatchedState_TrueState =
      new NodeId(UShort.MIN, uint(20276L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_LatchedState_FalseState =
      new NodeId(UShort.MIN, uint(20277L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_ReAlarmTime =
      new NodeId(UShort.MIN, uint(20278L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_ReAlarmRepeatCount =
      new NodeId(UShort.MIN, uint(20279L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_Silence =
      new NodeId(UShort.MIN, uint(20280L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_Suppress =
      new NodeId(UShort.MIN, uint(20281L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_Unsuppress =
      new NodeId(UShort.MIN, uint(20282L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_RemoveFromService =
      new NodeId(UShort.MIN, uint(20283L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_PlaceInService =
      new NodeId(UShort.MIN, uint(20284L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_Reset =
      new NodeId(UShort.MIN, uint(20285L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_NormalState =
      new NodeId(UShort.MIN, uint(20286L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_TrustListId =
      new NodeId(UShort.MIN, uint(20287L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_LastUpdateTime =
      new NodeId(UShort.MIN, uint(20288L));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_UpdateFrequency =
      new NodeId(UShort.MIN, uint(20289L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustList_UpdateFrequency =
          new NodeId(UShort.MIN, uint(20290L));

  public static final NodeId CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired =
      new NodeId(UShort.MIN, uint(20291L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_EventId =
          new NodeId(UShort.MIN, uint(20292L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_EventType =
          new NodeId(UShort.MIN, uint(20293L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_SourceNode =
          new NodeId(UShort.MIN, uint(20294L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_SourceName =
          new NodeId(UShort.MIN, uint(20295L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_Time =
          new NodeId(UShort.MIN, uint(20296L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ReceiveTime =
          new NodeId(UShort.MIN, uint(20297L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_LocalTime =
          new NodeId(UShort.MIN, uint(20298L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_Message =
          new NodeId(UShort.MIN, uint(20299L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_Severity =
          new NodeId(UShort.MIN, uint(20300L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ConditionClassId =
          new NodeId(UShort.MIN, uint(20301L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ConditionClassName =
          new NodeId(UShort.MIN, uint(20302L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ConditionSubClassId =
          new NodeId(UShort.MIN, uint(20303L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ConditionSubClassName =
          new NodeId(UShort.MIN, uint(20304L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ConditionName =
          new NodeId(UShort.MIN, uint(20305L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_BranchId =
          new NodeId(UShort.MIN, uint(20306L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_Retain =
          new NodeId(UShort.MIN, uint(20307L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_EnabledState =
          new NodeId(UShort.MIN, uint(20308L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_EnabledState_Id =
          new NodeId(UShort.MIN, uint(20309L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_EnabledState_Name =
          new NodeId(UShort.MIN, uint(20310L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_EnabledState_Number =
          new NodeId(UShort.MIN, uint(20311L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_EnabledState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20312L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_EnabledState_TransitionTime =
          new NodeId(UShort.MIN, uint(20313L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_EnabledState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20314L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_EnabledState_TrueState =
          new NodeId(UShort.MIN, uint(20315L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_EnabledState_FalseState =
          new NodeId(UShort.MIN, uint(20316L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_Quality =
          new NodeId(UShort.MIN, uint(20317L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_Quality_SourceTimestamp =
          new NodeId(UShort.MIN, uint(20318L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_LastSeverity =
          new NodeId(UShort.MIN, uint(20319L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_LastSeverity_SourceTimestamp =
          new NodeId(UShort.MIN, uint(20320L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_Comment =
          new NodeId(UShort.MIN, uint(20321L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_Comment_SourceTimestamp =
          new NodeId(UShort.MIN, uint(20322L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ClientUserId =
          new NodeId(UShort.MIN, uint(20323L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_Disable =
          new NodeId(UShort.MIN, uint(20324L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_Enable =
          new NodeId(UShort.MIN, uint(20325L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_AddComment =
          new NodeId(UShort.MIN, uint(20326L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_AddComment_InputArguments =
          new NodeId(UShort.MIN, uint(20327L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_AckedState =
          new NodeId(UShort.MIN, uint(20328L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_AckedState_Id =
          new NodeId(UShort.MIN, uint(20329L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_AckedState_Name =
          new NodeId(UShort.MIN, uint(20330L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_AckedState_Number =
          new NodeId(UShort.MIN, uint(20331L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_AckedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20332L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_AckedState_TransitionTime =
          new NodeId(UShort.MIN, uint(20333L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_AckedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20334L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_AckedState_TrueState =
          new NodeId(UShort.MIN, uint(20335L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_AckedState_FalseState =
          new NodeId(UShort.MIN, uint(20336L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ConfirmedState =
          new NodeId(UShort.MIN, uint(20337L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ConfirmedState_Id =
          new NodeId(UShort.MIN, uint(20338L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ConfirmedState_Name =
          new NodeId(UShort.MIN, uint(20339L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ConfirmedState_Number =
          new NodeId(UShort.MIN, uint(20340L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ConfirmedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20341L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ConfirmedState_TransitionTime =
          new NodeId(UShort.MIN, uint(20342L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ConfirmedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20343L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ConfirmedState_TrueState =
          new NodeId(UShort.MIN, uint(20344L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ConfirmedState_FalseState =
          new NodeId(UShort.MIN, uint(20345L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_Acknowledge =
          new NodeId(UShort.MIN, uint(20346L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_Acknowledge_InputArguments =
          new NodeId(UShort.MIN, uint(20347L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_Confirm =
          new NodeId(UShort.MIN, uint(20348L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_Confirm_InputArguments =
          new NodeId(UShort.MIN, uint(20349L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ActiveState =
          new NodeId(UShort.MIN, uint(20350L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ActiveState_Id =
          new NodeId(UShort.MIN, uint(20351L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ActiveState_Name =
          new NodeId(UShort.MIN, uint(20352L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ActiveState_Number =
          new NodeId(UShort.MIN, uint(20353L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ActiveState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20354L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ActiveState_TransitionTime =
          new NodeId(UShort.MIN, uint(20355L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ActiveState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20356L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ActiveState_TrueState =
          new NodeId(UShort.MIN, uint(20357L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ActiveState_FalseState =
          new NodeId(UShort.MIN, uint(20358L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_InputNode =
          new NodeId(UShort.MIN, uint(20359L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_SuppressedState =
          new NodeId(UShort.MIN, uint(20360L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_SuppressedState_Id =
          new NodeId(UShort.MIN, uint(20361L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_SuppressedState_Name =
          new NodeId(UShort.MIN, uint(20362L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_SuppressedState_Number =
          new NodeId(UShort.MIN, uint(20363L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_SuppressedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20364L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_SuppressedState_TransitionTime =
          new NodeId(UShort.MIN, uint(20365L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_SuppressedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20366L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_SuppressedState_TrueState =
          new NodeId(UShort.MIN, uint(20367L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_SuppressedState_FalseState =
          new NodeId(UShort.MIN, uint(20368L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_OutOfServiceState =
          new NodeId(UShort.MIN, uint(20369L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_OutOfServiceState_Id =
          new NodeId(UShort.MIN, uint(20370L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_OutOfServiceState_Name =
          new NodeId(UShort.MIN, uint(20371L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_OutOfServiceState_Number =
          new NodeId(UShort.MIN, uint(20372L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_OutOfServiceState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20373L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_OutOfServiceState_TransitionTime =
          new NodeId(UShort.MIN, uint(20374L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_OutOfServiceState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20375L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_OutOfServiceState_TrueState =
          new NodeId(UShort.MIN, uint(20376L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_OutOfServiceState_FalseState =
          new NodeId(UShort.MIN, uint(20377L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ShelvingState =
          new NodeId(UShort.MIN, uint(20378L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ShelvingState_CurrentState =
          new NodeId(UShort.MIN, uint(20379L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ShelvingState_CurrentState_Id =
          new NodeId(UShort.MIN, uint(20380L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ShelvingState_CurrentState_Name =
          new NodeId(UShort.MIN, uint(20381L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ShelvingState_CurrentState_Number =
          new NodeId(UShort.MIN, uint(20382L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ShelvingState_CurrentState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20383L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ShelvingState_LastTransition =
          new NodeId(UShort.MIN, uint(20384L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ShelvingState_LastTransition_Id =
          new NodeId(UShort.MIN, uint(20385L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ShelvingState_LastTransition_Name =
          new NodeId(UShort.MIN, uint(20386L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ShelvingState_LastTransition_Number =
          new NodeId(UShort.MIN, uint(20387L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ShelvingState_LastTransition_TransitionTime =
          new NodeId(UShort.MIN, uint(20388L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ShelvingState_LastTransition_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20389L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ShelvingState_AvailableStates =
          new NodeId(UShort.MIN, uint(20390L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ShelvingState_AvailableTransitions =
          new NodeId(UShort.MIN, uint(20391L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ShelvingState_UnshelveTime =
          new NodeId(UShort.MIN, uint(20392L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ShelvingState_TimedShelve =
          new NodeId(UShort.MIN, uint(20393L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ShelvingState_TimedShelve_InputArguments =
          new NodeId(UShort.MIN, uint(20394L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ShelvingState_Unshelve =
          new NodeId(UShort.MIN, uint(20395L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ShelvingState_OneShotShelve =
          new NodeId(UShort.MIN, uint(20396L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_SuppressedOrShelved =
          new NodeId(UShort.MIN, uint(20397L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_MaxTimeShelved =
          new NodeId(UShort.MIN, uint(20398L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_AudibleEnabled =
          new NodeId(UShort.MIN, uint(20399L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_AudibleSound =
          new NodeId(UShort.MIN, uint(20400L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_AudibleSound_ListId =
          new NodeId(UShort.MIN, uint(20401L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_AudibleSound_AgencyId =
          new NodeId(UShort.MIN, uint(20402L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_AudibleSound_VersionId =
          new NodeId(UShort.MIN, uint(20403L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_SilenceState =
          new NodeId(UShort.MIN, uint(20404L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_SilenceState_Id =
          new NodeId(UShort.MIN, uint(20405L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_SilenceState_Name =
          new NodeId(UShort.MIN, uint(20406L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_SilenceState_Number =
          new NodeId(UShort.MIN, uint(20407L));

  public static final NodeId DataSetOrderingType = new NodeId(UShort.MIN, uint(20408L));

  public static final NodeId
      ReaderGroupType_DataSetReaderName_Placeholder_Diagnostics_LiveValues_SecurityTokenID =
          new NodeId(UShort.MIN, uint(20409L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_SilenceState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20410L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_SilenceState_TransitionTime =
          new NodeId(UShort.MIN, uint(20411L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_SilenceState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20412L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_SilenceState_TrueState =
          new NodeId(UShort.MIN, uint(20413L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_SilenceState_FalseState =
          new NodeId(UShort.MIN, uint(20414L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_OnDelay =
          new NodeId(UShort.MIN, uint(20415L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_OffDelay =
          new NodeId(UShort.MIN, uint(20416L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_FirstInGroupFlag =
          new NodeId(UShort.MIN, uint(20417L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_FirstInGroup =
          new NodeId(UShort.MIN, uint(20418L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_LatchedState =
          new NodeId(UShort.MIN, uint(20419L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_LatchedState_Id =
          new NodeId(UShort.MIN, uint(20420L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_LatchedState_Name =
          new NodeId(UShort.MIN, uint(20421L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_LatchedState_Number =
          new NodeId(UShort.MIN, uint(20422L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_LatchedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20423L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_LatchedState_TransitionTime =
          new NodeId(UShort.MIN, uint(20424L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_LatchedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20425L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_LatchedState_TrueState =
          new NodeId(UShort.MIN, uint(20426L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_LatchedState_FalseState =
          new NodeId(UShort.MIN, uint(20427L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ReAlarmTime =
          new NodeId(UShort.MIN, uint(20428L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ReAlarmRepeatCount =
          new NodeId(UShort.MIN, uint(20429L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_Silence =
          new NodeId(UShort.MIN, uint(20430L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_Suppress =
          new NodeId(UShort.MIN, uint(20431L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_Unsuppress =
          new NodeId(UShort.MIN, uint(20432L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_RemoveFromService =
          new NodeId(UShort.MIN, uint(20433L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_PlaceInService =
          new NodeId(UShort.MIN, uint(20434L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_Reset =
          new NodeId(UShort.MIN, uint(20435L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_NormalState =
          new NodeId(UShort.MIN, uint(20436L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ExpirationDate =
          new NodeId(UShort.MIN, uint(20437L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ExpirationLimit =
          new NodeId(UShort.MIN, uint(20438L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_CertificateType =
          new NodeId(UShort.MIN, uint(20439L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_Certificate =
          new NodeId(UShort.MIN, uint(20440L));

  public static final NodeId CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate =
      new NodeId(UShort.MIN, uint(20441L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_EventId =
          new NodeId(UShort.MIN, uint(20442L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_EventType =
          new NodeId(UShort.MIN, uint(20443L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_SourceNode =
          new NodeId(UShort.MIN, uint(20444L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_SourceName =
          new NodeId(UShort.MIN, uint(20445L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_Time =
          new NodeId(UShort.MIN, uint(20446L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ReceiveTime =
          new NodeId(UShort.MIN, uint(20447L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_LocalTime =
          new NodeId(UShort.MIN, uint(20448L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_Message =
          new NodeId(UShort.MIN, uint(20449L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_Severity =
          new NodeId(UShort.MIN, uint(20450L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ConditionClassId =
          new NodeId(UShort.MIN, uint(20451L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ConditionClassName =
          new NodeId(UShort.MIN, uint(20452L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ConditionSubClassId =
          new NodeId(UShort.MIN, uint(20453L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ConditionSubClassName =
          new NodeId(UShort.MIN, uint(20454L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ConditionName =
          new NodeId(UShort.MIN, uint(20455L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_BranchId =
          new NodeId(UShort.MIN, uint(20456L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_Retain =
          new NodeId(UShort.MIN, uint(20457L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_EnabledState =
          new NodeId(UShort.MIN, uint(20458L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_EnabledState_Id =
          new NodeId(UShort.MIN, uint(20459L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_EnabledState_Name =
          new NodeId(UShort.MIN, uint(20460L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_EnabledState_Number =
          new NodeId(UShort.MIN, uint(20461L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_EnabledState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20462L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_EnabledState_TransitionTime =
          new NodeId(UShort.MIN, uint(20463L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_EnabledState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20464L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_EnabledState_TrueState =
          new NodeId(UShort.MIN, uint(20465L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_EnabledState_FalseState =
          new NodeId(UShort.MIN, uint(20466L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_Quality =
          new NodeId(UShort.MIN, uint(20467L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_Quality_SourceTimestamp =
          new NodeId(UShort.MIN, uint(20468L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_LastSeverity =
          new NodeId(UShort.MIN, uint(20469L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_LastSeverity_SourceTimestamp =
          new NodeId(UShort.MIN, uint(20470L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_Comment =
          new NodeId(UShort.MIN, uint(20471L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_Comment_SourceTimestamp =
          new NodeId(UShort.MIN, uint(20472L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ClientUserId =
          new NodeId(UShort.MIN, uint(20473L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_Disable =
          new NodeId(UShort.MIN, uint(20474L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_Enable =
          new NodeId(UShort.MIN, uint(20475L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_AddComment =
          new NodeId(UShort.MIN, uint(20476L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_AddComment_InputArguments =
          new NodeId(UShort.MIN, uint(20477L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_AckedState =
          new NodeId(UShort.MIN, uint(20478L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_AckedState_Id =
          new NodeId(UShort.MIN, uint(20479L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_AckedState_Name =
          new NodeId(UShort.MIN, uint(20480L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_AckedState_Number =
          new NodeId(UShort.MIN, uint(20481L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_AckedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20482L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_AckedState_TransitionTime =
          new NodeId(UShort.MIN, uint(20483L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_AckedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20484L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_AckedState_TrueState =
          new NodeId(UShort.MIN, uint(20485L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_AckedState_FalseState =
          new NodeId(UShort.MIN, uint(20486L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ConfirmedState =
          new NodeId(UShort.MIN, uint(20487L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ConfirmedState_Id =
          new NodeId(UShort.MIN, uint(20488L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ConfirmedState_Name =
          new NodeId(UShort.MIN, uint(20489L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ConfirmedState_Number =
          new NodeId(UShort.MIN, uint(20490L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ConfirmedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20491L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ConfirmedState_TransitionTime =
          new NodeId(UShort.MIN, uint(20492L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ConfirmedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20493L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ConfirmedState_TrueState =
          new NodeId(UShort.MIN, uint(20494L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ConfirmedState_FalseState =
          new NodeId(UShort.MIN, uint(20495L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_Acknowledge =
          new NodeId(UShort.MIN, uint(20496L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_Acknowledge_InputArguments =
          new NodeId(UShort.MIN, uint(20497L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_Confirm =
          new NodeId(UShort.MIN, uint(20498L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_Confirm_InputArguments =
          new NodeId(UShort.MIN, uint(20499L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ActiveState =
          new NodeId(UShort.MIN, uint(20500L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ActiveState_Id =
          new NodeId(UShort.MIN, uint(20501L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ActiveState_Name =
          new NodeId(UShort.MIN, uint(20502L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ActiveState_Number =
          new NodeId(UShort.MIN, uint(20503L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ActiveState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20504L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ActiveState_TransitionTime =
          new NodeId(UShort.MIN, uint(20505L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ActiveState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20506L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ActiveState_TrueState =
          new NodeId(UShort.MIN, uint(20507L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ActiveState_FalseState =
          new NodeId(UShort.MIN, uint(20508L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_InputNode =
          new NodeId(UShort.MIN, uint(20509L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_SuppressedState =
          new NodeId(UShort.MIN, uint(20510L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_SuppressedState_Id =
          new NodeId(UShort.MIN, uint(20511L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_SuppressedState_Name =
          new NodeId(UShort.MIN, uint(20512L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_SuppressedState_Number =
          new NodeId(UShort.MIN, uint(20513L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_SuppressedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20514L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_SuppressedState_TransitionTime =
          new NodeId(UShort.MIN, uint(20515L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_SuppressedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20516L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_SuppressedState_TrueState =
          new NodeId(UShort.MIN, uint(20517L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_SuppressedState_FalseState =
          new NodeId(UShort.MIN, uint(20518L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_OutOfServiceState =
          new NodeId(UShort.MIN, uint(20519L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_OutOfServiceState_Id =
          new NodeId(UShort.MIN, uint(20520L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_OutOfServiceState_Name =
          new NodeId(UShort.MIN, uint(20521L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_OutOfServiceState_Number =
          new NodeId(UShort.MIN, uint(20522L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_OutOfServiceState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20523L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_OutOfServiceState_TransitionTime =
          new NodeId(UShort.MIN, uint(20524L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_OutOfServiceState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20525L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_OutOfServiceState_TrueState =
          new NodeId(UShort.MIN, uint(20526L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_OutOfServiceState_FalseState =
          new NodeId(UShort.MIN, uint(20527L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState =
          new NodeId(UShort.MIN, uint(20528L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState_CurrentState =
          new NodeId(UShort.MIN, uint(20529L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState_CurrentState_Id =
          new NodeId(UShort.MIN, uint(20530L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState_CurrentState_Name =
          new NodeId(UShort.MIN, uint(20531L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState_CurrentState_Number =
          new NodeId(UShort.MIN, uint(20532L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState_CurrentState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20533L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState_LastTransition =
          new NodeId(UShort.MIN, uint(20534L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState_LastTransition_Id =
          new NodeId(UShort.MIN, uint(20535L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState_LastTransition_Name =
          new NodeId(UShort.MIN, uint(20536L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState_LastTransition_Number =
          new NodeId(UShort.MIN, uint(20537L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState_LastTransition_TransitionTime =
          new NodeId(UShort.MIN, uint(20538L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState_LastTransition_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20539L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState_AvailableStates =
          new NodeId(UShort.MIN, uint(20540L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState_AvailableTransitions =
          new NodeId(UShort.MIN, uint(20541L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState_UnshelveTime =
          new NodeId(UShort.MIN, uint(20542L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState_TimedShelve =
          new NodeId(UShort.MIN, uint(20543L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState_TimedShelve_InputArguments =
          new NodeId(UShort.MIN, uint(20544L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState_Unshelve =
          new NodeId(UShort.MIN, uint(20545L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState_OneShotShelve =
          new NodeId(UShort.MIN, uint(20546L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_SuppressedOrShelved =
          new NodeId(UShort.MIN, uint(20547L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_MaxTimeShelved =
          new NodeId(UShort.MIN, uint(20548L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_AudibleEnabled =
          new NodeId(UShort.MIN, uint(20549L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_AudibleSound =
          new NodeId(UShort.MIN, uint(20550L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_AudibleSound_ListId =
          new NodeId(UShort.MIN, uint(20551L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_AudibleSound_AgencyId =
          new NodeId(UShort.MIN, uint(20552L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_AudibleSound_VersionId =
          new NodeId(UShort.MIN, uint(20553L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_SilenceState =
          new NodeId(UShort.MIN, uint(20554L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_SilenceState_Id =
          new NodeId(UShort.MIN, uint(20555L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_SilenceState_Name =
          new NodeId(UShort.MIN, uint(20556L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_SilenceState_Number =
          new NodeId(UShort.MIN, uint(20557L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_SilenceState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20558L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_SilenceState_TransitionTime =
          new NodeId(UShort.MIN, uint(20559L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_SilenceState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20560L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_SilenceState_TrueState =
          new NodeId(UShort.MIN, uint(20561L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_SilenceState_FalseState =
          new NodeId(UShort.MIN, uint(20562L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_OnDelay =
          new NodeId(UShort.MIN, uint(20563L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_OffDelay =
          new NodeId(UShort.MIN, uint(20564L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_FirstInGroupFlag =
          new NodeId(UShort.MIN, uint(20565L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_FirstInGroup =
          new NodeId(UShort.MIN, uint(20566L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_LatchedState =
          new NodeId(UShort.MIN, uint(20567L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_LatchedState_Id =
          new NodeId(UShort.MIN, uint(20568L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_LatchedState_Name =
          new NodeId(UShort.MIN, uint(20569L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_LatchedState_Number =
          new NodeId(UShort.MIN, uint(20570L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_LatchedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20571L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_LatchedState_TransitionTime =
          new NodeId(UShort.MIN, uint(20572L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_LatchedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20573L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_LatchedState_TrueState =
          new NodeId(UShort.MIN, uint(20574L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_LatchedState_FalseState =
          new NodeId(UShort.MIN, uint(20575L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ReAlarmTime =
          new NodeId(UShort.MIN, uint(20576L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ReAlarmRepeatCount =
          new NodeId(UShort.MIN, uint(20577L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_Silence =
          new NodeId(UShort.MIN, uint(20578L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_Suppress =
          new NodeId(UShort.MIN, uint(20579L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_Unsuppress =
          new NodeId(UShort.MIN, uint(20580L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_RemoveFromService =
          new NodeId(UShort.MIN, uint(20581L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_PlaceInService =
          new NodeId(UShort.MIN, uint(20582L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_Reset =
          new NodeId(UShort.MIN, uint(20583L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_NormalState =
          new NodeId(UShort.MIN, uint(20584L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_TrustListId =
          new NodeId(UShort.MIN, uint(20585L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_LastUpdateTime =
          new NodeId(UShort.MIN, uint(20586L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_UpdateFrequency =
          new NodeId(UShort.MIN, uint(20587L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustList_UpdateFrequency =
          new NodeId(UShort.MIN, uint(20588L));

  public static final NodeId CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired =
      new NodeId(UShort.MIN, uint(20589L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_EventId =
          new NodeId(UShort.MIN, uint(20590L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_EventType =
          new NodeId(UShort.MIN, uint(20591L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_SourceNode =
          new NodeId(UShort.MIN, uint(20592L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_SourceName =
          new NodeId(UShort.MIN, uint(20593L));

  public static final NodeId CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_Time =
      new NodeId(UShort.MIN, uint(20594L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ReceiveTime =
          new NodeId(UShort.MIN, uint(20595L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_LocalTime =
          new NodeId(UShort.MIN, uint(20596L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_Message =
          new NodeId(UShort.MIN, uint(20597L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_Severity =
          new NodeId(UShort.MIN, uint(20598L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ConditionClassId =
          new NodeId(UShort.MIN, uint(20599L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ConditionClassName =
          new NodeId(UShort.MIN, uint(20600L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ConditionSubClassId =
          new NodeId(UShort.MIN, uint(20601L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ConditionSubClassName =
          new NodeId(UShort.MIN, uint(20602L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ConditionName =
          new NodeId(UShort.MIN, uint(20603L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_BranchId =
          new NodeId(UShort.MIN, uint(20604L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_Retain =
          new NodeId(UShort.MIN, uint(20605L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_EnabledState =
          new NodeId(UShort.MIN, uint(20606L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_EnabledState_Id =
          new NodeId(UShort.MIN, uint(20607L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_EnabledState_Name =
          new NodeId(UShort.MIN, uint(20608L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_EnabledState_Number =
          new NodeId(UShort.MIN, uint(20609L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_EnabledState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20610L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_EnabledState_TransitionTime =
          new NodeId(UShort.MIN, uint(20611L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_EnabledState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20612L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_EnabledState_TrueState =
          new NodeId(UShort.MIN, uint(20613L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_EnabledState_FalseState =
          new NodeId(UShort.MIN, uint(20614L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_Quality =
          new NodeId(UShort.MIN, uint(20615L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_Quality_SourceTimestamp =
          new NodeId(UShort.MIN, uint(20616L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_LastSeverity =
          new NodeId(UShort.MIN, uint(20617L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_LastSeverity_SourceTimestamp =
          new NodeId(UShort.MIN, uint(20618L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_Comment =
          new NodeId(UShort.MIN, uint(20619L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_Comment_SourceTimestamp =
          new NodeId(UShort.MIN, uint(20620L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ClientUserId =
          new NodeId(UShort.MIN, uint(20621L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_Disable =
          new NodeId(UShort.MIN, uint(20622L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_Enable =
          new NodeId(UShort.MIN, uint(20623L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_AddComment =
          new NodeId(UShort.MIN, uint(20624L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_AddComment_InputArguments =
          new NodeId(UShort.MIN, uint(20625L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_AckedState =
          new NodeId(UShort.MIN, uint(20626L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_AckedState_Id =
          new NodeId(UShort.MIN, uint(20627L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_AckedState_Name =
          new NodeId(UShort.MIN, uint(20628L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_AckedState_Number =
          new NodeId(UShort.MIN, uint(20629L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_AckedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20630L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_AckedState_TransitionTime =
          new NodeId(UShort.MIN, uint(20631L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_AckedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20632L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_AckedState_TrueState =
          new NodeId(UShort.MIN, uint(20633L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_AckedState_FalseState =
          new NodeId(UShort.MIN, uint(20634L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ConfirmedState =
          new NodeId(UShort.MIN, uint(20635L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ConfirmedState_Id =
          new NodeId(UShort.MIN, uint(20636L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ConfirmedState_Name =
          new NodeId(UShort.MIN, uint(20637L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ConfirmedState_Number =
          new NodeId(UShort.MIN, uint(20638L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ConfirmedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20639L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ConfirmedState_TransitionTime =
          new NodeId(UShort.MIN, uint(20640L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ConfirmedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20641L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ConfirmedState_TrueState =
          new NodeId(UShort.MIN, uint(20642L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ConfirmedState_FalseState =
          new NodeId(UShort.MIN, uint(20643L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_Acknowledge =
          new NodeId(UShort.MIN, uint(20644L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_Acknowledge_InputArguments =
          new NodeId(UShort.MIN, uint(20645L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_Confirm =
          new NodeId(UShort.MIN, uint(20646L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_Confirm_InputArguments =
          new NodeId(UShort.MIN, uint(20647L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ActiveState =
          new NodeId(UShort.MIN, uint(20648L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ActiveState_Id =
          new NodeId(UShort.MIN, uint(20649L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ActiveState_Name =
          new NodeId(UShort.MIN, uint(20650L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ActiveState_Number =
          new NodeId(UShort.MIN, uint(20651L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ActiveState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20652L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ActiveState_TransitionTime =
          new NodeId(UShort.MIN, uint(20653L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ActiveState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20654L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ActiveState_TrueState =
          new NodeId(UShort.MIN, uint(20655L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ActiveState_FalseState =
          new NodeId(UShort.MIN, uint(20656L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_InputNode =
          new NodeId(UShort.MIN, uint(20657L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_SuppressedState =
          new NodeId(UShort.MIN, uint(20658L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_SuppressedState_Id =
          new NodeId(UShort.MIN, uint(20659L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_SuppressedState_Name =
          new NodeId(UShort.MIN, uint(20660L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_SuppressedState_Number =
          new NodeId(UShort.MIN, uint(20661L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_SuppressedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20662L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_SuppressedState_TransitionTime =
          new NodeId(UShort.MIN, uint(20663L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_SuppressedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20664L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_SuppressedState_TrueState =
          new NodeId(UShort.MIN, uint(20665L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_SuppressedState_FalseState =
          new NodeId(UShort.MIN, uint(20666L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_OutOfServiceState =
          new NodeId(UShort.MIN, uint(20667L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_OutOfServiceState_Id =
          new NodeId(UShort.MIN, uint(20668L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_OutOfServiceState_Name =
          new NodeId(UShort.MIN, uint(20669L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_OutOfServiceState_Number =
          new NodeId(UShort.MIN, uint(20670L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_OutOfServiceState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20671L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_OutOfServiceState_TransitionTime =
          new NodeId(UShort.MIN, uint(20672L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_OutOfServiceState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20673L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_OutOfServiceState_TrueState =
          new NodeId(UShort.MIN, uint(20674L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_OutOfServiceState_FalseState =
          new NodeId(UShort.MIN, uint(20675L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ShelvingState =
          new NodeId(UShort.MIN, uint(20676L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ShelvingState_CurrentState =
          new NodeId(UShort.MIN, uint(20677L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ShelvingState_CurrentState_Id =
          new NodeId(UShort.MIN, uint(20678L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ShelvingState_CurrentState_Name =
          new NodeId(UShort.MIN, uint(20679L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ShelvingState_CurrentState_Number =
          new NodeId(UShort.MIN, uint(20680L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ShelvingState_CurrentState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20681L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ShelvingState_LastTransition =
          new NodeId(UShort.MIN, uint(20682L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ShelvingState_LastTransition_Id =
          new NodeId(UShort.MIN, uint(20683L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ShelvingState_LastTransition_Name =
          new NodeId(UShort.MIN, uint(20684L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ShelvingState_LastTransition_Number =
          new NodeId(UShort.MIN, uint(20685L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ShelvingState_LastTransition_TransitionTime =
          new NodeId(UShort.MIN, uint(20686L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ShelvingState_LastTransition_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20687L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ShelvingState_AvailableStates =
          new NodeId(UShort.MIN, uint(20688L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ShelvingState_AvailableTransitions =
          new NodeId(UShort.MIN, uint(20689L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ShelvingState_UnshelveTime =
          new NodeId(UShort.MIN, uint(20690L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ShelvingState_TimedShelve =
          new NodeId(UShort.MIN, uint(20691L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ShelvingState_TimedShelve_InputArguments =
          new NodeId(UShort.MIN, uint(20692L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ShelvingState_Unshelve =
          new NodeId(UShort.MIN, uint(20693L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ShelvingState_OneShotShelve =
          new NodeId(UShort.MIN, uint(20694L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_SuppressedOrShelved =
          new NodeId(UShort.MIN, uint(20695L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_MaxTimeShelved =
          new NodeId(UShort.MIN, uint(20696L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_AudibleEnabled =
          new NodeId(UShort.MIN, uint(20697L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_AudibleSound =
          new NodeId(UShort.MIN, uint(20698L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_AudibleSound_ListId =
          new NodeId(UShort.MIN, uint(20699L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_AudibleSound_AgencyId =
          new NodeId(UShort.MIN, uint(20700L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_AudibleSound_VersionId =
          new NodeId(UShort.MIN, uint(20701L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_SilenceState =
          new NodeId(UShort.MIN, uint(20702L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_SilenceState_Id =
          new NodeId(UShort.MIN, uint(20703L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_SilenceState_Name =
          new NodeId(UShort.MIN, uint(20704L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_SilenceState_Number =
          new NodeId(UShort.MIN, uint(20705L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_SilenceState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20706L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_SilenceState_TransitionTime =
          new NodeId(UShort.MIN, uint(20707L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_SilenceState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20708L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_SilenceState_TrueState =
          new NodeId(UShort.MIN, uint(20709L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_SilenceState_FalseState =
          new NodeId(UShort.MIN, uint(20710L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_OnDelay =
          new NodeId(UShort.MIN, uint(20711L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_OffDelay =
          new NodeId(UShort.MIN, uint(20712L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_FirstInGroupFlag =
          new NodeId(UShort.MIN, uint(20713L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_FirstInGroup =
          new NodeId(UShort.MIN, uint(20714L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_LatchedState =
          new NodeId(UShort.MIN, uint(20715L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_LatchedState_Id =
          new NodeId(UShort.MIN, uint(20716L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_LatchedState_Name =
          new NodeId(UShort.MIN, uint(20717L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_LatchedState_Number =
          new NodeId(UShort.MIN, uint(20718L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_LatchedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20719L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_LatchedState_TransitionTime =
          new NodeId(UShort.MIN, uint(20720L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_LatchedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20721L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_LatchedState_TrueState =
          new NodeId(UShort.MIN, uint(20722L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_LatchedState_FalseState =
          new NodeId(UShort.MIN, uint(20723L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ReAlarmTime =
          new NodeId(UShort.MIN, uint(20724L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ReAlarmRepeatCount =
          new NodeId(UShort.MIN, uint(20725L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_Silence =
          new NodeId(UShort.MIN, uint(20726L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_Suppress =
          new NodeId(UShort.MIN, uint(20727L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_Unsuppress =
          new NodeId(UShort.MIN, uint(20728L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_RemoveFromService =
          new NodeId(UShort.MIN, uint(20729L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_PlaceInService =
          new NodeId(UShort.MIN, uint(20730L));

  public static final NodeId CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_Reset =
      new NodeId(UShort.MIN, uint(20731L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_NormalState =
          new NodeId(UShort.MIN, uint(20732L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ExpirationDate =
          new NodeId(UShort.MIN, uint(20733L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ExpirationLimit =
          new NodeId(UShort.MIN, uint(20734L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_CertificateType =
          new NodeId(UShort.MIN, uint(20735L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_Certificate =
          new NodeId(UShort.MIN, uint(20736L));

  public static final NodeId CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate =
      new NodeId(UShort.MIN, uint(20737L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_EventId =
          new NodeId(UShort.MIN, uint(20738L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_EventType =
          new NodeId(UShort.MIN, uint(20739L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_SourceNode =
          new NodeId(UShort.MIN, uint(20740L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_SourceName =
          new NodeId(UShort.MIN, uint(20741L));

  public static final NodeId CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_Time =
      new NodeId(UShort.MIN, uint(20742L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ReceiveTime =
          new NodeId(UShort.MIN, uint(20743L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_LocalTime =
          new NodeId(UShort.MIN, uint(20744L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_Message =
          new NodeId(UShort.MIN, uint(20745L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_Severity =
          new NodeId(UShort.MIN, uint(20746L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ConditionClassId =
          new NodeId(UShort.MIN, uint(20747L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ConditionClassName =
          new NodeId(UShort.MIN, uint(20748L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ConditionSubClassId =
          new NodeId(UShort.MIN, uint(20749L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ConditionSubClassName =
          new NodeId(UShort.MIN, uint(20750L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ConditionName =
          new NodeId(UShort.MIN, uint(20751L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_BranchId =
          new NodeId(UShort.MIN, uint(20752L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_Retain =
          new NodeId(UShort.MIN, uint(20753L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_EnabledState =
          new NodeId(UShort.MIN, uint(20754L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_EnabledState_Id =
          new NodeId(UShort.MIN, uint(20755L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_EnabledState_Name =
          new NodeId(UShort.MIN, uint(20756L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_EnabledState_Number =
          new NodeId(UShort.MIN, uint(20757L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_EnabledState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20758L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_EnabledState_TransitionTime =
          new NodeId(UShort.MIN, uint(20759L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_EnabledState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20760L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_EnabledState_TrueState =
          new NodeId(UShort.MIN, uint(20761L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_EnabledState_FalseState =
          new NodeId(UShort.MIN, uint(20762L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_Quality =
          new NodeId(UShort.MIN, uint(20763L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_Quality_SourceTimestamp =
          new NodeId(UShort.MIN, uint(20764L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_LastSeverity =
          new NodeId(UShort.MIN, uint(20765L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_LastSeverity_SourceTimestamp =
          new NodeId(UShort.MIN, uint(20766L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_Comment =
          new NodeId(UShort.MIN, uint(20767L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_Comment_SourceTimestamp =
          new NodeId(UShort.MIN, uint(20768L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ClientUserId =
          new NodeId(UShort.MIN, uint(20769L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_Disable =
          new NodeId(UShort.MIN, uint(20770L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_Enable =
          new NodeId(UShort.MIN, uint(20771L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_AddComment =
          new NodeId(UShort.MIN, uint(20772L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_AddComment_InputArguments =
          new NodeId(UShort.MIN, uint(20773L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_AckedState =
          new NodeId(UShort.MIN, uint(20774L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_AckedState_Id =
          new NodeId(UShort.MIN, uint(20775L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_AckedState_Name =
          new NodeId(UShort.MIN, uint(20776L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_AckedState_Number =
          new NodeId(UShort.MIN, uint(20777L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_AckedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20778L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_AckedState_TransitionTime =
          new NodeId(UShort.MIN, uint(20779L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_AckedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20780L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_AckedState_TrueState =
          new NodeId(UShort.MIN, uint(20781L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_AckedState_FalseState =
          new NodeId(UShort.MIN, uint(20782L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ConfirmedState =
          new NodeId(UShort.MIN, uint(20783L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ConfirmedState_Id =
          new NodeId(UShort.MIN, uint(20784L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ConfirmedState_Name =
          new NodeId(UShort.MIN, uint(20785L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ConfirmedState_Number =
          new NodeId(UShort.MIN, uint(20786L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ConfirmedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20787L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ConfirmedState_TransitionTime =
          new NodeId(UShort.MIN, uint(20788L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ConfirmedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20789L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ConfirmedState_TrueState =
          new NodeId(UShort.MIN, uint(20790L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ConfirmedState_FalseState =
          new NodeId(UShort.MIN, uint(20791L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_Acknowledge =
          new NodeId(UShort.MIN, uint(20792L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_Acknowledge_InputArguments =
          new NodeId(UShort.MIN, uint(20793L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_Confirm =
          new NodeId(UShort.MIN, uint(20794L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_Confirm_InputArguments =
          new NodeId(UShort.MIN, uint(20795L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ActiveState =
          new NodeId(UShort.MIN, uint(20796L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ActiveState_Id =
          new NodeId(UShort.MIN, uint(20797L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ActiveState_Name =
          new NodeId(UShort.MIN, uint(20798L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ActiveState_Number =
          new NodeId(UShort.MIN, uint(20799L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ActiveState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20800L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ActiveState_TransitionTime =
          new NodeId(UShort.MIN, uint(20801L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ActiveState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20802L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ActiveState_TrueState =
          new NodeId(UShort.MIN, uint(20803L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ActiveState_FalseState =
          new NodeId(UShort.MIN, uint(20804L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_InputNode =
          new NodeId(UShort.MIN, uint(20805L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_SuppressedState =
          new NodeId(UShort.MIN, uint(20806L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_SuppressedState_Id =
          new NodeId(UShort.MIN, uint(20807L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_SuppressedState_Name =
          new NodeId(UShort.MIN, uint(20808L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_SuppressedState_Number =
          new NodeId(UShort.MIN, uint(20809L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_SuppressedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20810L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_SuppressedState_TransitionTime =
          new NodeId(UShort.MIN, uint(20811L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_SuppressedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20812L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_SuppressedState_TrueState =
          new NodeId(UShort.MIN, uint(20813L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_SuppressedState_FalseState =
          new NodeId(UShort.MIN, uint(20814L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_OutOfServiceState =
          new NodeId(UShort.MIN, uint(20815L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_OutOfServiceState_Id =
          new NodeId(UShort.MIN, uint(20816L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_OutOfServiceState_Name =
          new NodeId(UShort.MIN, uint(20817L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_OutOfServiceState_Number =
          new NodeId(UShort.MIN, uint(20818L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_OutOfServiceState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20819L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_OutOfServiceState_TransitionTime =
          new NodeId(UShort.MIN, uint(20820L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_OutOfServiceState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20821L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_OutOfServiceState_TrueState =
          new NodeId(UShort.MIN, uint(20822L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_OutOfServiceState_FalseState =
          new NodeId(UShort.MIN, uint(20823L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState =
          new NodeId(UShort.MIN, uint(20824L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState_CurrentState =
          new NodeId(UShort.MIN, uint(20825L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState_CurrentState_Id =
          new NodeId(UShort.MIN, uint(20826L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState_CurrentState_Name =
          new NodeId(UShort.MIN, uint(20827L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState_CurrentState_Number =
          new NodeId(UShort.MIN, uint(20828L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState_CurrentState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20829L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState_LastTransition =
          new NodeId(UShort.MIN, uint(20830L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState_LastTransition_Id =
          new NodeId(UShort.MIN, uint(20831L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState_LastTransition_Name =
          new NodeId(UShort.MIN, uint(20832L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState_LastTransition_Number =
          new NodeId(UShort.MIN, uint(20833L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState_LastTransition_TransitionTime =
          new NodeId(UShort.MIN, uint(20834L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState_LastTransition_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20835L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState_AvailableStates =
          new NodeId(UShort.MIN, uint(20836L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState_AvailableTransitions =
          new NodeId(UShort.MIN, uint(20837L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState_UnshelveTime =
          new NodeId(UShort.MIN, uint(20838L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState_TimedShelve =
          new NodeId(UShort.MIN, uint(20839L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState_TimedShelve_InputArguments =
          new NodeId(UShort.MIN, uint(20840L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState_Unshelve =
          new NodeId(UShort.MIN, uint(20841L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState_OneShotShelve =
          new NodeId(UShort.MIN, uint(20842L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_SuppressedOrShelved =
          new NodeId(UShort.MIN, uint(20843L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_MaxTimeShelved =
          new NodeId(UShort.MIN, uint(20844L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_AudibleEnabled =
          new NodeId(UShort.MIN, uint(20845L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_AudibleSound =
          new NodeId(UShort.MIN, uint(20846L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_AudibleSound_ListId =
          new NodeId(UShort.MIN, uint(20847L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_AudibleSound_AgencyId =
          new NodeId(UShort.MIN, uint(20848L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_AudibleSound_VersionId =
          new NodeId(UShort.MIN, uint(20849L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_SilenceState =
          new NodeId(UShort.MIN, uint(20850L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_SilenceState_Id =
          new NodeId(UShort.MIN, uint(20851L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_SilenceState_Name =
          new NodeId(UShort.MIN, uint(20852L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_SilenceState_Number =
          new NodeId(UShort.MIN, uint(20853L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_SilenceState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20854L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_SilenceState_TransitionTime =
          new NodeId(UShort.MIN, uint(20855L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_SilenceState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20856L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_SilenceState_TrueState =
          new NodeId(UShort.MIN, uint(20857L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_SilenceState_FalseState =
          new NodeId(UShort.MIN, uint(20858L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_OnDelay =
          new NodeId(UShort.MIN, uint(20859L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_OffDelay =
          new NodeId(UShort.MIN, uint(20860L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_FirstInGroupFlag =
          new NodeId(UShort.MIN, uint(20861L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_FirstInGroup =
          new NodeId(UShort.MIN, uint(20862L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_LatchedState =
          new NodeId(UShort.MIN, uint(20863L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_LatchedState_Id =
          new NodeId(UShort.MIN, uint(20864L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_LatchedState_Name =
          new NodeId(UShort.MIN, uint(20865L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_LatchedState_Number =
          new NodeId(UShort.MIN, uint(20866L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_LatchedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20867L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_LatchedState_TransitionTime =
          new NodeId(UShort.MIN, uint(20868L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_LatchedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20869L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_LatchedState_TrueState =
          new NodeId(UShort.MIN, uint(20870L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_LatchedState_FalseState =
          new NodeId(UShort.MIN, uint(20871L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ReAlarmTime =
          new NodeId(UShort.MIN, uint(20872L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ReAlarmRepeatCount =
          new NodeId(UShort.MIN, uint(20873L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_Silence =
          new NodeId(UShort.MIN, uint(20874L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_Suppress =
          new NodeId(UShort.MIN, uint(20875L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_Unsuppress =
          new NodeId(UShort.MIN, uint(20876L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_RemoveFromService =
          new NodeId(UShort.MIN, uint(20877L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_PlaceInService =
          new NodeId(UShort.MIN, uint(20878L));

  public static final NodeId CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_Reset =
      new NodeId(UShort.MIN, uint(20879L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_NormalState =
          new NodeId(UShort.MIN, uint(20880L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_TrustListId =
          new NodeId(UShort.MIN, uint(20881L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_LastUpdateTime =
          new NodeId(UShort.MIN, uint(20882L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_UpdateFrequency =
          new NodeId(UShort.MIN, uint(20883L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustList_UpdateFrequency =
          new NodeId(UShort.MIN, uint(20884L));

  public static final NodeId CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired =
      new NodeId(UShort.MIN, uint(20885L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_EventId =
          new NodeId(UShort.MIN, uint(20886L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_EventType =
          new NodeId(UShort.MIN, uint(20887L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_SourceNode =
          new NodeId(UShort.MIN, uint(20888L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_SourceName =
          new NodeId(UShort.MIN, uint(20889L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_Time =
          new NodeId(UShort.MIN, uint(20890L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ReceiveTime =
          new NodeId(UShort.MIN, uint(20891L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_LocalTime =
          new NodeId(UShort.MIN, uint(20892L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_Message =
          new NodeId(UShort.MIN, uint(20893L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_Severity =
          new NodeId(UShort.MIN, uint(20894L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ConditionClassId =
          new NodeId(UShort.MIN, uint(20895L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ConditionClassName =
          new NodeId(UShort.MIN, uint(20896L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ConditionSubClassId =
          new NodeId(UShort.MIN, uint(20897L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ConditionSubClassName =
          new NodeId(UShort.MIN, uint(20898L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ConditionName =
          new NodeId(UShort.MIN, uint(20899L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_BranchId =
          new NodeId(UShort.MIN, uint(20900L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_Retain =
          new NodeId(UShort.MIN, uint(20901L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_EnabledState =
          new NodeId(UShort.MIN, uint(20902L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_EnabledState_Id =
          new NodeId(UShort.MIN, uint(20903L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_EnabledState_Name =
          new NodeId(UShort.MIN, uint(20904L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_EnabledState_Number =
          new NodeId(UShort.MIN, uint(20905L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_EnabledState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20906L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_EnabledState_TransitionTime =
          new NodeId(UShort.MIN, uint(20907L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_EnabledState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20908L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_EnabledState_TrueState =
          new NodeId(UShort.MIN, uint(20909L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_EnabledState_FalseState =
          new NodeId(UShort.MIN, uint(20910L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_Quality =
          new NodeId(UShort.MIN, uint(20911L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_Quality_SourceTimestamp =
          new NodeId(UShort.MIN, uint(20912L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_LastSeverity =
          new NodeId(UShort.MIN, uint(20913L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_LastSeverity_SourceTimestamp =
          new NodeId(UShort.MIN, uint(20914L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_Comment =
          new NodeId(UShort.MIN, uint(20915L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_Comment_SourceTimestamp =
          new NodeId(UShort.MIN, uint(20916L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ClientUserId =
          new NodeId(UShort.MIN, uint(20917L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_Disable =
          new NodeId(UShort.MIN, uint(20918L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_Enable =
          new NodeId(UShort.MIN, uint(20919L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_AddComment =
          new NodeId(UShort.MIN, uint(20920L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_AddComment_InputArguments =
          new NodeId(UShort.MIN, uint(20921L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_AckedState =
          new NodeId(UShort.MIN, uint(20922L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_AckedState_Id =
          new NodeId(UShort.MIN, uint(20923L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_AckedState_Name =
          new NodeId(UShort.MIN, uint(20924L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_AckedState_Number =
          new NodeId(UShort.MIN, uint(20925L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_AckedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20926L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_AckedState_TransitionTime =
          new NodeId(UShort.MIN, uint(20927L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_AckedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20928L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_AckedState_TrueState =
          new NodeId(UShort.MIN, uint(20929L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_AckedState_FalseState =
          new NodeId(UShort.MIN, uint(20930L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ConfirmedState =
          new NodeId(UShort.MIN, uint(20931L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ConfirmedState_Id =
          new NodeId(UShort.MIN, uint(20932L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ConfirmedState_Name =
          new NodeId(UShort.MIN, uint(20933L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ConfirmedState_Number =
          new NodeId(UShort.MIN, uint(20934L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ConfirmedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20935L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ConfirmedState_TransitionTime =
          new NodeId(UShort.MIN, uint(20936L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ConfirmedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20937L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ConfirmedState_TrueState =
          new NodeId(UShort.MIN, uint(20938L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ConfirmedState_FalseState =
          new NodeId(UShort.MIN, uint(20939L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_Acknowledge =
          new NodeId(UShort.MIN, uint(20940L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_Acknowledge_InputArguments =
          new NodeId(UShort.MIN, uint(20941L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_Confirm =
          new NodeId(UShort.MIN, uint(20942L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_Confirm_InputArguments =
          new NodeId(UShort.MIN, uint(20943L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ActiveState =
          new NodeId(UShort.MIN, uint(20944L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ActiveState_Id =
          new NodeId(UShort.MIN, uint(20945L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ActiveState_Name =
          new NodeId(UShort.MIN, uint(20946L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ActiveState_Number =
          new NodeId(UShort.MIN, uint(20947L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ActiveState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20948L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ActiveState_TransitionTime =
          new NodeId(UShort.MIN, uint(20949L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ActiveState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20950L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ActiveState_TrueState =
          new NodeId(UShort.MIN, uint(20951L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ActiveState_FalseState =
          new NodeId(UShort.MIN, uint(20952L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_InputNode =
          new NodeId(UShort.MIN, uint(20953L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_SuppressedState =
          new NodeId(UShort.MIN, uint(20954L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_SuppressedState_Id =
          new NodeId(UShort.MIN, uint(20955L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_SuppressedState_Name =
          new NodeId(UShort.MIN, uint(20956L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_SuppressedState_Number =
          new NodeId(UShort.MIN, uint(20957L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_SuppressedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20958L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_SuppressedState_TransitionTime =
          new NodeId(UShort.MIN, uint(20959L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_SuppressedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20960L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_SuppressedState_TrueState =
          new NodeId(UShort.MIN, uint(20961L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_SuppressedState_FalseState =
          new NodeId(UShort.MIN, uint(20962L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_OutOfServiceState =
          new NodeId(UShort.MIN, uint(20963L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_OutOfServiceState_Id =
          new NodeId(UShort.MIN, uint(20964L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_OutOfServiceState_Name =
          new NodeId(UShort.MIN, uint(20965L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_OutOfServiceState_Number =
          new NodeId(UShort.MIN, uint(20966L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_OutOfServiceState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20967L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_OutOfServiceState_TransitionTime =
          new NodeId(UShort.MIN, uint(20968L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_OutOfServiceState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20969L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_OutOfServiceState_TrueState =
          new NodeId(UShort.MIN, uint(20970L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_OutOfServiceState_FalseState =
          new NodeId(UShort.MIN, uint(20971L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ShelvingState =
          new NodeId(UShort.MIN, uint(20972L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ShelvingState_CurrentState =
          new NodeId(UShort.MIN, uint(20973L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ShelvingState_CurrentState_Id =
          new NodeId(UShort.MIN, uint(20974L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ShelvingState_CurrentState_Name =
          new NodeId(UShort.MIN, uint(20975L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ShelvingState_CurrentState_Number =
          new NodeId(UShort.MIN, uint(20976L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ShelvingState_CurrentState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20977L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ShelvingState_LastTransition =
          new NodeId(UShort.MIN, uint(20978L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ShelvingState_LastTransition_Id =
          new NodeId(UShort.MIN, uint(20979L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ShelvingState_LastTransition_Name =
          new NodeId(UShort.MIN, uint(20980L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ShelvingState_LastTransition_Number =
          new NodeId(UShort.MIN, uint(20981L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ShelvingState_LastTransition_TransitionTime =
          new NodeId(UShort.MIN, uint(20982L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ShelvingState_LastTransition_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20983L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ShelvingState_AvailableStates =
          new NodeId(UShort.MIN, uint(20984L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ShelvingState_AvailableTransitions =
          new NodeId(UShort.MIN, uint(20985L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ShelvingState_UnshelveTime =
          new NodeId(UShort.MIN, uint(20986L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ShelvingState_TimedShelve =
          new NodeId(UShort.MIN, uint(20987L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ShelvingState_TimedShelve_InputArguments =
          new NodeId(UShort.MIN, uint(20988L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ShelvingState_Unshelve =
          new NodeId(UShort.MIN, uint(20989L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ShelvingState_OneShotShelve =
          new NodeId(UShort.MIN, uint(20990L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_SuppressedOrShelved =
          new NodeId(UShort.MIN, uint(20991L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_MaxTimeShelved =
          new NodeId(UShort.MIN, uint(20992L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_AudibleEnabled =
          new NodeId(UShort.MIN, uint(20993L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_AudibleSound =
          new NodeId(UShort.MIN, uint(20994L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_AudibleSound_ListId =
          new NodeId(UShort.MIN, uint(20995L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_AudibleSound_AgencyId =
          new NodeId(UShort.MIN, uint(20996L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_AudibleSound_VersionId =
          new NodeId(UShort.MIN, uint(20997L));

  public static final NodeId VersionTime = new NodeId(UShort.MIN, uint(20998L));

  public static final NodeId SessionlessInvokeResponseType = new NodeId(UShort.MIN, uint(20999L));

  public static final NodeId SessionlessInvokeResponseType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(21000L));

  public static final NodeId SessionlessInvokeResponseType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(21001L));

  public static final NodeId OpcUa_BinarySchema_FieldTargetDataType =
      new NodeId(UShort.MIN, uint(21002L));

  public static final NodeId
      ReaderGroupType_DataSetReaderName_Placeholder_Diagnostics_LiveValues_SecurityTokenID_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(21003L));

  public static final NodeId
      ReaderGroupType_DataSetReaderName_Placeholder_Diagnostics_LiveValues_TimeToNextTokenID =
          new NodeId(UShort.MIN, uint(21004L));

  public static final NodeId
      ReaderGroupType_DataSetReaderName_Placeholder_Diagnostics_LiveValues_TimeToNextTokenID_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(21005L));

  public static final NodeId ReaderGroupType_DataSetReaderName_Placeholder_SubscribedDataSet =
      new NodeId(UShort.MIN, uint(21006L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_SilenceState =
          new NodeId(UShort.MIN, uint(21007L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_SilenceState_Id =
          new NodeId(UShort.MIN, uint(21008L));

  public static final NodeId ReaderGroupType_DataSetReaderName_Placeholder_CreateTargetVariables =
      new NodeId(UShort.MIN, uint(21009L));

  public static final NodeId
      ReaderGroupType_DataSetReaderName_Placeholder_CreateTargetVariables_InputArguments =
          new NodeId(UShort.MIN, uint(21010L));

  public static final NodeId
      ReaderGroupType_DataSetReaderName_Placeholder_CreateTargetVariables_OutputArguments =
          new NodeId(UShort.MIN, uint(21011L));

  public static final NodeId ReaderGroupType_DataSetReaderName_Placeholder_CreateDataSetMirror =
      new NodeId(UShort.MIN, uint(21012L));

  public static final NodeId
      ReaderGroupType_DataSetReaderName_Placeholder_CreateDataSetMirror_InputArguments =
          new NodeId(UShort.MIN, uint(21013L));

  public static final NodeId
      ReaderGroupType_DataSetReaderName_Placeholder_CreateDataSetMirror_OutputArguments =
          new NodeId(UShort.MIN, uint(21014L));

  public static final NodeId ReaderGroupType_Diagnostics = new NodeId(UShort.MIN, uint(21015L));

  public static final NodeId ReaderGroupType_Diagnostics_DiagnosticsLevel =
      new NodeId(UShort.MIN, uint(21016L));

  public static final NodeId ReaderGroupType_Diagnostics_TotalInformation =
      new NodeId(UShort.MIN, uint(21017L));

  public static final NodeId ReaderGroupType_Diagnostics_TotalInformation_Active =
      new NodeId(UShort.MIN, uint(21018L));

  public static final NodeId ReaderGroupType_Diagnostics_TotalInformation_Classification =
      new NodeId(UShort.MIN, uint(21019L));

  public static final NodeId ReaderGroupType_Diagnostics_TotalInformation_DiagnosticsLevel =
      new NodeId(UShort.MIN, uint(21020L));

  public static final NodeId ReaderGroupType_Diagnostics_TotalInformation_TimeFirstChange =
      new NodeId(UShort.MIN, uint(21021L));

  public static final NodeId ReaderGroupType_Diagnostics_TotalError =
      new NodeId(UShort.MIN, uint(21022L));

  public static final NodeId ReaderGroupType_Diagnostics_TotalError_Active =
      new NodeId(UShort.MIN, uint(21023L));

  public static final NodeId ReaderGroupType_Diagnostics_TotalError_Classification =
      new NodeId(UShort.MIN, uint(21024L));

  public static final NodeId ReaderGroupType_Diagnostics_TotalError_DiagnosticsLevel =
      new NodeId(UShort.MIN, uint(21025L));

  public static final NodeId ReaderGroupType_Diagnostics_TotalError_TimeFirstChange =
      new NodeId(UShort.MIN, uint(21026L));

  public static final NodeId ReaderGroupType_Diagnostics_Reset =
      new NodeId(UShort.MIN, uint(21027L));

  public static final NodeId ReaderGroupType_Diagnostics_SubError =
      new NodeId(UShort.MIN, uint(21028L));

  public static final NodeId ReaderGroupType_Diagnostics_Counters =
      new NodeId(UShort.MIN, uint(21029L));

  public static final NodeId ReaderGroupType_Diagnostics_Counters_StateError =
      new NodeId(UShort.MIN, uint(21030L));

  public static final NodeId ReaderGroupType_Diagnostics_Counters_StateError_Active =
      new NodeId(UShort.MIN, uint(21031L));

  public static final NodeId ReaderGroupType_Diagnostics_Counters_StateError_Classification =
      new NodeId(UShort.MIN, uint(21032L));

  public static final NodeId ReaderGroupType_Diagnostics_Counters_StateError_DiagnosticsLevel =
      new NodeId(UShort.MIN, uint(21033L));

  public static final NodeId ReaderGroupType_Diagnostics_Counters_StateError_TimeFirstChange =
      new NodeId(UShort.MIN, uint(21034L));

  public static final NodeId ReaderGroupType_Diagnostics_Counters_StateOperationalByMethod =
      new NodeId(UShort.MIN, uint(21035L));

  public static final NodeId ReaderGroupType_Diagnostics_Counters_StateOperationalByMethod_Active =
      new NodeId(UShort.MIN, uint(21036L));

  public static final NodeId
      ReaderGroupType_Diagnostics_Counters_StateOperationalByMethod_Classification =
          new NodeId(UShort.MIN, uint(21037L));

  public static final NodeId
      ReaderGroupType_Diagnostics_Counters_StateOperationalByMethod_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(21038L));

  public static final NodeId
      ReaderGroupType_Diagnostics_Counters_StateOperationalByMethod_TimeFirstChange =
          new NodeId(UShort.MIN, uint(21039L));

  public static final NodeId ReaderGroupType_Diagnostics_Counters_StateOperationalByParent =
      new NodeId(UShort.MIN, uint(21040L));

  public static final NodeId ReaderGroupType_Diagnostics_Counters_StateOperationalByParent_Active =
      new NodeId(UShort.MIN, uint(21041L));

  public static final NodeId
      ReaderGroupType_Diagnostics_Counters_StateOperationalByParent_Classification =
          new NodeId(UShort.MIN, uint(21042L));

  public static final NodeId
      ReaderGroupType_Diagnostics_Counters_StateOperationalByParent_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(21043L));

  public static final NodeId
      ReaderGroupType_Diagnostics_Counters_StateOperationalByParent_TimeFirstChange =
          new NodeId(UShort.MIN, uint(21044L));

  public static final NodeId ReaderGroupType_Diagnostics_Counters_StateOperationalFromError =
      new NodeId(UShort.MIN, uint(21045L));

  public static final NodeId ReaderGroupType_Diagnostics_Counters_StateOperationalFromError_Active =
      new NodeId(UShort.MIN, uint(21046L));

  public static final NodeId
      ReaderGroupType_Diagnostics_Counters_StateOperationalFromError_Classification =
          new NodeId(UShort.MIN, uint(21047L));

  public static final NodeId
      ReaderGroupType_Diagnostics_Counters_StateOperationalFromError_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(21048L));

  public static final NodeId
      ReaderGroupType_Diagnostics_Counters_StateOperationalFromError_TimeFirstChange =
          new NodeId(UShort.MIN, uint(21049L));

  public static final NodeId ReaderGroupType_Diagnostics_Counters_StatePausedByParent =
      new NodeId(UShort.MIN, uint(21050L));

  public static final NodeId ReaderGroupType_Diagnostics_Counters_StatePausedByParent_Active =
      new NodeId(UShort.MIN, uint(21051L));

  public static final NodeId
      ReaderGroupType_Diagnostics_Counters_StatePausedByParent_Classification =
          new NodeId(UShort.MIN, uint(21052L));

  public static final NodeId
      ReaderGroupType_Diagnostics_Counters_StatePausedByParent_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(21053L));

  public static final NodeId
      ReaderGroupType_Diagnostics_Counters_StatePausedByParent_TimeFirstChange =
          new NodeId(UShort.MIN, uint(21054L));

  public static final NodeId ReaderGroupType_Diagnostics_Counters_StateDisabledByMethod =
      new NodeId(UShort.MIN, uint(21055L));

  public static final NodeId ReaderGroupType_Diagnostics_Counters_StateDisabledByMethod_Active =
      new NodeId(UShort.MIN, uint(21056L));

  public static final NodeId
      ReaderGroupType_Diagnostics_Counters_StateDisabledByMethod_Classification =
          new NodeId(UShort.MIN, uint(21057L));

  public static final NodeId
      ReaderGroupType_Diagnostics_Counters_StateDisabledByMethod_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(21058L));

  public static final NodeId
      ReaderGroupType_Diagnostics_Counters_StateDisabledByMethod_TimeFirstChange =
          new NodeId(UShort.MIN, uint(21059L));

  public static final NodeId ReaderGroupType_Diagnostics_LiveValues =
      new NodeId(UShort.MIN, uint(21060L));

  public static final NodeId ReaderGroupType_Diagnostics_Counters_ReceivedNetworkMessages =
      new NodeId(UShort.MIN, uint(21061L));

  public static final NodeId ReaderGroupType_Diagnostics_Counters_ReceivedNetworkMessages_Active =
      new NodeId(UShort.MIN, uint(21062L));

  public static final NodeId
      ReaderGroupType_Diagnostics_Counters_ReceivedNetworkMessages_Classification =
          new NodeId(UShort.MIN, uint(21063L));

  public static final NodeId
      ReaderGroupType_Diagnostics_Counters_ReceivedNetworkMessages_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(21064L));

  public static final NodeId
      ReaderGroupType_Diagnostics_Counters_ReceivedNetworkMessages_TimeFirstChange =
          new NodeId(UShort.MIN, uint(21065L));

  public static final NodeId ReaderGroupType_Diagnostics_Counters_ReceivedInvalidNetworkMessages =
      new NodeId(UShort.MIN, uint(21066L));

  public static final NodeId
      ReaderGroupType_Diagnostics_Counters_ReceivedInvalidNetworkMessages_Active =
          new NodeId(UShort.MIN, uint(21067L));

  public static final NodeId
      ReaderGroupType_Diagnostics_Counters_ReceivedInvalidNetworkMessages_Classification =
          new NodeId(UShort.MIN, uint(21068L));

  public static final NodeId
      ReaderGroupType_Diagnostics_Counters_ReceivedInvalidNetworkMessages_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(21069L));

  public static final NodeId
      ReaderGroupType_Diagnostics_Counters_ReceivedInvalidNetworkMessages_TimeFirstChange =
          new NodeId(UShort.MIN, uint(21070L));

  public static final NodeId ReaderGroupType_Diagnostics_Counters_DecryptionErrors =
      new NodeId(UShort.MIN, uint(21071L));

  public static final NodeId ReaderGroupType_Diagnostics_Counters_DecryptionErrors_Active =
      new NodeId(UShort.MIN, uint(21072L));

  public static final NodeId ReaderGroupType_Diagnostics_Counters_DecryptionErrors_Classification =
      new NodeId(UShort.MIN, uint(21073L));

  public static final NodeId
      ReaderGroupType_Diagnostics_Counters_DecryptionErrors_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(21074L));

  public static final NodeId ReaderGroupType_Diagnostics_Counters_DecryptionErrors_TimeFirstChange =
      new NodeId(UShort.MIN, uint(21075L));

  public static final NodeId ReaderGroupType_Diagnostics_LiveValues_ConfiguredDataSetReaders =
      new NodeId(UShort.MIN, uint(21076L));

  public static final NodeId
      ReaderGroupType_Diagnostics_LiveValues_ConfiguredDataSetReaders_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(21077L));

  public static final NodeId ReaderGroupType_Diagnostics_LiveValues_OperationalDataSetReaders =
      new NodeId(UShort.MIN, uint(21078L));

  public static final NodeId
      ReaderGroupType_Diagnostics_LiveValues_OperationalDataSetReaders_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(21079L));

  public static final NodeId ReaderGroupType_TransportSettings =
      new NodeId(UShort.MIN, uint(21080L));

  public static final NodeId ReaderGroupType_MessageSettings = new NodeId(UShort.MIN, uint(21081L));

  public static final NodeId ReaderGroupType_AddDataSetReader =
      new NodeId(UShort.MIN, uint(21082L));

  public static final NodeId ReaderGroupType_AddDataSetReader_InputArguments =
      new NodeId(UShort.MIN, uint(21083L));

  public static final NodeId ReaderGroupType_AddDataSetReader_OutputArguments =
      new NodeId(UShort.MIN, uint(21084L));

  public static final NodeId ReaderGroupType_RemoveDataSetReader =
      new NodeId(UShort.MIN, uint(21085L));

  public static final NodeId ReaderGroupType_RemoveDataSetReader_InputArguments =
      new NodeId(UShort.MIN, uint(21086L));

  public static final NodeId PubSubGroupTypeAddReaderMethodType =
      new NodeId(UShort.MIN, uint(21087L));

  public static final NodeId PubSubGroupTypeAddReaderMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(21088L));

  public static final NodeId PubSubGroupTypeAddReaderMethodType_OutputArguments =
      new NodeId(UShort.MIN, uint(21089L));

  public static final NodeId ReaderGroupTransportType = new NodeId(UShort.MIN, uint(21090L));

  public static final NodeId ReaderGroupMessageType = new NodeId(UShort.MIN, uint(21091L));

  public static final NodeId DataSetWriterType_DataSetWriterId =
      new NodeId(UShort.MIN, uint(21092L));

  public static final NodeId DataSetWriterType_DataSetFieldContentMask =
      new NodeId(UShort.MIN, uint(21093L));

  public static final NodeId DataSetWriterType_KeyFrameCount = new NodeId(UShort.MIN, uint(21094L));

  public static final NodeId DataSetWriterType_MessageSettings =
      new NodeId(UShort.MIN, uint(21095L));

  public static final NodeId DataSetWriterMessageType = new NodeId(UShort.MIN, uint(21096L));

  public static final NodeId DataSetReaderType_PublisherId = new NodeId(UShort.MIN, uint(21097L));

  public static final NodeId DataSetReaderType_WriterGroupId = new NodeId(UShort.MIN, uint(21098L));

  public static final NodeId DataSetReaderType_DataSetWriterId =
      new NodeId(UShort.MIN, uint(21099L));

  public static final NodeId DataSetReaderType_DataSetMetaData =
      new NodeId(UShort.MIN, uint(21100L));

  public static final NodeId DataSetReaderType_DataSetFieldContentMask =
      new NodeId(UShort.MIN, uint(21101L));

  public static final NodeId DataSetReaderType_MessageReceiveTimeout =
      new NodeId(UShort.MIN, uint(21102L));

  public static final NodeId DataSetReaderType_MessageSettings =
      new NodeId(UShort.MIN, uint(21103L));

  public static final NodeId DataSetReaderMessageType = new NodeId(UShort.MIN, uint(21104L));

  public static final NodeId UadpWriterGroupMessageType = new NodeId(UShort.MIN, uint(21105L));

  public static final NodeId UadpWriterGroupMessageType_GroupVersion =
      new NodeId(UShort.MIN, uint(21106L));

  public static final NodeId UadpWriterGroupMessageType_DataSetOrdering =
      new NodeId(UShort.MIN, uint(21107L));

  public static final NodeId UadpWriterGroupMessageType_NetworkMessageContentMask =
      new NodeId(UShort.MIN, uint(21108L));

  public static final NodeId UadpWriterGroupMessageType_SamplingOffset =
      new NodeId(UShort.MIN, uint(21109L));

  public static final NodeId UadpWriterGroupMessageType_PublishingOffset =
      new NodeId(UShort.MIN, uint(21110L));

  public static final NodeId UadpDataSetWriterMessageType = new NodeId(UShort.MIN, uint(21111L));

  public static final NodeId UadpDataSetWriterMessageType_DataSetMessageContentMask =
      new NodeId(UShort.MIN, uint(21112L));

  public static final NodeId UadpDataSetWriterMessageType_ConfiguredSize =
      new NodeId(UShort.MIN, uint(21113L));

  public static final NodeId UadpDataSetWriterMessageType_NetworkMessageNumber =
      new NodeId(UShort.MIN, uint(21114L));

  public static final NodeId UadpDataSetWriterMessageType_DataSetOffset =
      new NodeId(UShort.MIN, uint(21115L));

  public static final NodeId UadpDataSetReaderMessageType = new NodeId(UShort.MIN, uint(21116L));

  public static final NodeId UadpDataSetReaderMessageType_GroupVersion =
      new NodeId(UShort.MIN, uint(21117L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_SilenceState_Name =
          new NodeId(UShort.MIN, uint(21118L));

  public static final NodeId UadpDataSetReaderMessageType_NetworkMessageNumber =
      new NodeId(UShort.MIN, uint(21119L));

  public static final NodeId UadpDataSetReaderMessageType_DataSetClassId =
      new NodeId(UShort.MIN, uint(21120L));

  public static final NodeId UadpDataSetReaderMessageType_NetworkMessageContentMask =
      new NodeId(UShort.MIN, uint(21121L));

  public static final NodeId UadpDataSetReaderMessageType_DataSetMessageContentMask =
      new NodeId(UShort.MIN, uint(21122L));

  public static final NodeId UadpDataSetReaderMessageType_PublishingInterval =
      new NodeId(UShort.MIN, uint(21123L));

  public static final NodeId UadpDataSetReaderMessageType_ProcessingOffset =
      new NodeId(UShort.MIN, uint(21124L));

  public static final NodeId UadpDataSetReaderMessageType_ReceiveOffset =
      new NodeId(UShort.MIN, uint(21125L));

  public static final NodeId JsonWriterGroupMessageType = new NodeId(UShort.MIN, uint(21126L));

  public static final NodeId JsonWriterGroupMessageType_NetworkMessageContentMask =
      new NodeId(UShort.MIN, uint(21127L));

  public static final NodeId JsonDataSetWriterMessageType = new NodeId(UShort.MIN, uint(21128L));

  public static final NodeId JsonDataSetWriterMessageType_DataSetMessageContentMask =
      new NodeId(UShort.MIN, uint(21129L));

  public static final NodeId JsonDataSetReaderMessageType = new NodeId(UShort.MIN, uint(21130L));

  public static final NodeId JsonDataSetReaderMessageType_NetworkMessageContentMask =
      new NodeId(UShort.MIN, uint(21131L));

  public static final NodeId JsonDataSetReaderMessageType_DataSetMessageContentMask =
      new NodeId(UShort.MIN, uint(21132L));

  public static final NodeId DatagramWriterGroupTransportType =
      new NodeId(UShort.MIN, uint(21133L));

  public static final NodeId DatagramWriterGroupTransportType_MessageRepeatCount =
      new NodeId(UShort.MIN, uint(21134L));

  public static final NodeId DatagramWriterGroupTransportType_MessageRepeatDelay =
      new NodeId(UShort.MIN, uint(21135L));

  public static final NodeId BrokerWriterGroupTransportType = new NodeId(UShort.MIN, uint(21136L));

  public static final NodeId BrokerWriterGroupTransportType_QueueName =
      new NodeId(UShort.MIN, uint(21137L));

  public static final NodeId BrokerDataSetWriterTransportType =
      new NodeId(UShort.MIN, uint(21138L));

  public static final NodeId BrokerDataSetWriterTransportType_QueueName =
      new NodeId(UShort.MIN, uint(21139L));

  public static final NodeId BrokerDataSetWriterTransportType_MetaDataQueueName =
      new NodeId(UShort.MIN, uint(21140L));

  public static final NodeId BrokerDataSetWriterTransportType_MetaDataUpdateTime =
      new NodeId(UShort.MIN, uint(21141L));

  public static final NodeId BrokerDataSetReaderTransportType =
      new NodeId(UShort.MIN, uint(21142L));

  public static final NodeId BrokerDataSetReaderTransportType_QueueName =
      new NodeId(UShort.MIN, uint(21143L));

  public static final NodeId BrokerDataSetReaderTransportType_MetaDataQueueName =
      new NodeId(UShort.MIN, uint(21144L));

  public static final NodeId NetworkAddressType = new NodeId(UShort.MIN, uint(21145L));

  public static final NodeId NetworkAddressType_NetworkInterface =
      new NodeId(UShort.MIN, uint(21146L));

  public static final NodeId NetworkAddressUrlType = new NodeId(UShort.MIN, uint(21147L));

  public static final NodeId
      AlarmSuppressionGroupType_AlarmCondition_Placeholder_AckedState_Number =
          new NodeId(UShort.MIN, uint(21148L));

  public static final NodeId NetworkAddressUrlType_Url = new NodeId(UShort.MIN, uint(21149L));

  public static final NodeId WriterGroupDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(21150L));

  public static final NodeId NetworkAddressDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(21151L));

  public static final NodeId NetworkAddressUrlDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(21152L));

  public static final NodeId ReaderGroupDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(21153L));

  public static final NodeId PubSubConfigurationDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(21154L));

  public static final NodeId DatagramWriterGroupTransportDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(21155L));

  public static final NodeId OpcUa_BinarySchema_WriterGroupDataType =
      new NodeId(UShort.MIN, uint(21156L));

  public static final NodeId OpcUa_BinarySchema_WriterGroupDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(21157L));

  public static final NodeId OpcUa_BinarySchema_WriterGroupDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(21158L));

  public static final NodeId OpcUa_BinarySchema_NetworkAddressDataType =
      new NodeId(UShort.MIN, uint(21159L));

  public static final NodeId OpcUa_BinarySchema_NetworkAddressDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(21160L));

  public static final NodeId OpcUa_BinarySchema_NetworkAddressDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(21161L));

  public static final NodeId OpcUa_BinarySchema_NetworkAddressUrlDataType =
      new NodeId(UShort.MIN, uint(21162L));

  public static final NodeId OpcUa_BinarySchema_NetworkAddressUrlDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(21163L));

  public static final NodeId OpcUa_BinarySchema_NetworkAddressUrlDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(21164L));

  public static final NodeId OpcUa_BinarySchema_ReaderGroupDataType =
      new NodeId(UShort.MIN, uint(21165L));

  public static final NodeId OpcUa_BinarySchema_ReaderGroupDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(21166L));

  public static final NodeId OpcUa_BinarySchema_ReaderGroupDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(21167L));

  public static final NodeId OpcUa_BinarySchema_PubSubConfigurationDataType =
      new NodeId(UShort.MIN, uint(21168L));

  public static final NodeId OpcUa_BinarySchema_PubSubConfigurationDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(21169L));

  public static final NodeId OpcUa_BinarySchema_PubSubConfigurationDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(21170L));

  public static final NodeId OpcUa_BinarySchema_DatagramWriterGroupTransportDataType =
      new NodeId(UShort.MIN, uint(21171L));

  public static final NodeId
      OpcUa_BinarySchema_DatagramWriterGroupTransportDataType_DataTypeVersion =
          new NodeId(UShort.MIN, uint(21172L));

  public static final NodeId
      OpcUa_BinarySchema_DatagramWriterGroupTransportDataType_DictionaryFragment =
          new NodeId(UShort.MIN, uint(21173L));

  public static final NodeId WriterGroupDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(21174L));

  public static final NodeId NetworkAddressDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(21175L));

  public static final NodeId NetworkAddressUrlDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(21176L));

  public static final NodeId ReaderGroupDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(21177L));

  public static final NodeId PubSubConfigurationDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(21178L));

  public static final NodeId DatagramWriterGroupTransportDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(21179L));

  public static final NodeId OpcUa_XmlSchema_WriterGroupDataType =
      new NodeId(UShort.MIN, uint(21180L));

  public static final NodeId OpcUa_XmlSchema_WriterGroupDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(21181L));

  public static final NodeId OpcUa_XmlSchema_WriterGroupDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(21182L));

  public static final NodeId OpcUa_XmlSchema_NetworkAddressDataType =
      new NodeId(UShort.MIN, uint(21183L));

  public static final NodeId OpcUa_XmlSchema_NetworkAddressDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(21184L));

  public static final NodeId OpcUa_XmlSchema_NetworkAddressDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(21185L));

  public static final NodeId OpcUa_XmlSchema_NetworkAddressUrlDataType =
      new NodeId(UShort.MIN, uint(21186L));

  public static final NodeId OpcUa_XmlSchema_NetworkAddressUrlDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(21187L));

  public static final NodeId OpcUa_XmlSchema_NetworkAddressUrlDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(21188L));

  public static final NodeId OpcUa_XmlSchema_ReaderGroupDataType =
      new NodeId(UShort.MIN, uint(21189L));

  public static final NodeId OpcUa_XmlSchema_ReaderGroupDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(21190L));

  public static final NodeId OpcUa_XmlSchema_ReaderGroupDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(21191L));

  public static final NodeId OpcUa_XmlSchema_PubSubConfigurationDataType =
      new NodeId(UShort.MIN, uint(21192L));

  public static final NodeId OpcUa_XmlSchema_PubSubConfigurationDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(21193L));

  public static final NodeId OpcUa_XmlSchema_PubSubConfigurationDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(21194L));

  public static final NodeId OpcUa_XmlSchema_DatagramWriterGroupTransportDataType =
      new NodeId(UShort.MIN, uint(21195L));

  public static final NodeId OpcUa_XmlSchema_DatagramWriterGroupTransportDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(21196L));

  public static final NodeId
      OpcUa_XmlSchema_DatagramWriterGroupTransportDataType_DictionaryFragment =
          new NodeId(UShort.MIN, uint(21197L));

  public static final NodeId WriterGroupDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(21198L));

  public static final NodeId NetworkAddressDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(21199L));

  public static final NodeId NetworkAddressUrlDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(21200L));

  public static final NodeId ReaderGroupDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(21201L));

  public static final NodeId PubSubConfigurationDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(21202L));

  public static final NodeId DatagramWriterGroupTransportDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(21203L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_SilenceState_Number =
          new NodeId(UShort.MIN, uint(21204L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_SilenceState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21205L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_SilenceState_TransitionTime =
          new NodeId(UShort.MIN, uint(21206L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_SilenceState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21207L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_SilenceState_TrueState =
          new NodeId(UShort.MIN, uint(21208L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_SilenceState_FalseState =
          new NodeId(UShort.MIN, uint(21209L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_OnDelay =
          new NodeId(UShort.MIN, uint(21210L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_OffDelay =
          new NodeId(UShort.MIN, uint(21211L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_FirstInGroupFlag =
          new NodeId(UShort.MIN, uint(21212L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_FirstInGroup =
          new NodeId(UShort.MIN, uint(21213L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_LatchedState =
          new NodeId(UShort.MIN, uint(21214L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_LatchedState_Id =
          new NodeId(UShort.MIN, uint(21215L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_LatchedState_Name =
          new NodeId(UShort.MIN, uint(21216L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_LatchedState_Number =
          new NodeId(UShort.MIN, uint(21217L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_LatchedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21218L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_LatchedState_TransitionTime =
          new NodeId(UShort.MIN, uint(21219L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_LatchedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21220L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_LatchedState_TrueState =
          new NodeId(UShort.MIN, uint(21221L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_LatchedState_FalseState =
          new NodeId(UShort.MIN, uint(21222L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ReAlarmTime =
          new NodeId(UShort.MIN, uint(21223L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ReAlarmRepeatCount =
          new NodeId(UShort.MIN, uint(21224L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_Silence =
          new NodeId(UShort.MIN, uint(21225L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_Suppress =
          new NodeId(UShort.MIN, uint(21226L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_Unsuppress =
          new NodeId(UShort.MIN, uint(21227L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_RemoveFromService =
          new NodeId(UShort.MIN, uint(21228L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_PlaceInService =
          new NodeId(UShort.MIN, uint(21229L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_Reset =
          new NodeId(UShort.MIN, uint(21230L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_NormalState =
          new NodeId(UShort.MIN, uint(21231L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ExpirationDate =
          new NodeId(UShort.MIN, uint(21232L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ExpirationLimit =
          new NodeId(UShort.MIN, uint(21233L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_CertificateType =
          new NodeId(UShort.MIN, uint(21234L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_Certificate =
          new NodeId(UShort.MIN, uint(21235L));

  public static final NodeId CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate =
      new NodeId(UShort.MIN, uint(21236L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_EventId =
          new NodeId(UShort.MIN, uint(21237L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_EventType =
          new NodeId(UShort.MIN, uint(21238L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_SourceNode =
          new NodeId(UShort.MIN, uint(21239L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_SourceName =
          new NodeId(UShort.MIN, uint(21240L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_Time =
          new NodeId(UShort.MIN, uint(21241L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ReceiveTime =
          new NodeId(UShort.MIN, uint(21242L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_LocalTime =
          new NodeId(UShort.MIN, uint(21243L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_Message =
          new NodeId(UShort.MIN, uint(21244L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_Severity =
          new NodeId(UShort.MIN, uint(21245L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ConditionClassId =
          new NodeId(UShort.MIN, uint(21246L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ConditionClassName =
          new NodeId(UShort.MIN, uint(21247L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ConditionSubClassId =
          new NodeId(UShort.MIN, uint(21248L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ConditionSubClassName =
          new NodeId(UShort.MIN, uint(21249L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ConditionName =
          new NodeId(UShort.MIN, uint(21250L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_BranchId =
          new NodeId(UShort.MIN, uint(21251L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_Retain =
          new NodeId(UShort.MIN, uint(21252L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_EnabledState =
          new NodeId(UShort.MIN, uint(21253L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_EnabledState_Id =
          new NodeId(UShort.MIN, uint(21254L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_EnabledState_Name =
          new NodeId(UShort.MIN, uint(21255L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_EnabledState_Number =
          new NodeId(UShort.MIN, uint(21256L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_EnabledState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21257L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_EnabledState_TransitionTime =
          new NodeId(UShort.MIN, uint(21258L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_EnabledState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21259L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_EnabledState_TrueState =
          new NodeId(UShort.MIN, uint(21260L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_EnabledState_FalseState =
          new NodeId(UShort.MIN, uint(21261L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_Quality =
          new NodeId(UShort.MIN, uint(21262L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_Quality_SourceTimestamp =
          new NodeId(UShort.MIN, uint(21263L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_LastSeverity =
          new NodeId(UShort.MIN, uint(21264L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_LastSeverity_SourceTimestamp =
          new NodeId(UShort.MIN, uint(21265L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_Comment =
          new NodeId(UShort.MIN, uint(21266L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_Comment_SourceTimestamp =
          new NodeId(UShort.MIN, uint(21267L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ClientUserId =
          new NodeId(UShort.MIN, uint(21268L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_Disable =
          new NodeId(UShort.MIN, uint(21269L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_Enable =
          new NodeId(UShort.MIN, uint(21270L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_AddComment =
          new NodeId(UShort.MIN, uint(21271L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_AddComment_InputArguments =
          new NodeId(UShort.MIN, uint(21272L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_AckedState =
          new NodeId(UShort.MIN, uint(21273L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_AckedState_Id =
          new NodeId(UShort.MIN, uint(21274L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_AckedState_Name =
          new NodeId(UShort.MIN, uint(21275L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_AckedState_Number =
          new NodeId(UShort.MIN, uint(21276L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_AckedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21277L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_AckedState_TransitionTime =
          new NodeId(UShort.MIN, uint(21278L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_AckedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21279L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_AckedState_TrueState =
          new NodeId(UShort.MIN, uint(21280L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_AckedState_FalseState =
          new NodeId(UShort.MIN, uint(21281L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ConfirmedState =
          new NodeId(UShort.MIN, uint(21282L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ConfirmedState_Id =
          new NodeId(UShort.MIN, uint(21283L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ConfirmedState_Name =
          new NodeId(UShort.MIN, uint(21284L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ConfirmedState_Number =
          new NodeId(UShort.MIN, uint(21285L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ConfirmedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21286L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ConfirmedState_TransitionTime =
          new NodeId(UShort.MIN, uint(21287L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ConfirmedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21288L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ConfirmedState_TrueState =
          new NodeId(UShort.MIN, uint(21289L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ConfirmedState_FalseState =
          new NodeId(UShort.MIN, uint(21290L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_Acknowledge =
          new NodeId(UShort.MIN, uint(21291L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_Acknowledge_InputArguments =
          new NodeId(UShort.MIN, uint(21292L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_Confirm =
          new NodeId(UShort.MIN, uint(21293L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_Confirm_InputArguments =
          new NodeId(UShort.MIN, uint(21294L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ActiveState =
          new NodeId(UShort.MIN, uint(21295L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ActiveState_Id =
          new NodeId(UShort.MIN, uint(21296L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ActiveState_Name =
          new NodeId(UShort.MIN, uint(21297L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ActiveState_Number =
          new NodeId(UShort.MIN, uint(21298L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ActiveState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21299L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ActiveState_TransitionTime =
          new NodeId(UShort.MIN, uint(21300L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ActiveState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21301L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ActiveState_TrueState =
          new NodeId(UShort.MIN, uint(21302L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ActiveState_FalseState =
          new NodeId(UShort.MIN, uint(21303L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_InputNode =
          new NodeId(UShort.MIN, uint(21304L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_SuppressedState =
          new NodeId(UShort.MIN, uint(21305L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_SuppressedState_Id =
          new NodeId(UShort.MIN, uint(21306L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_SuppressedState_Name =
          new NodeId(UShort.MIN, uint(21307L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_SuppressedState_Number =
          new NodeId(UShort.MIN, uint(21308L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_SuppressedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21309L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_SuppressedState_TransitionTime =
          new NodeId(UShort.MIN, uint(21310L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_SuppressedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21311L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_SuppressedState_TrueState =
          new NodeId(UShort.MIN, uint(21312L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_SuppressedState_FalseState =
          new NodeId(UShort.MIN, uint(21313L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_OutOfServiceState =
          new NodeId(UShort.MIN, uint(21314L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_OutOfServiceState_Id =
          new NodeId(UShort.MIN, uint(21315L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_OutOfServiceState_Name =
          new NodeId(UShort.MIN, uint(21316L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_OutOfServiceState_Number =
          new NodeId(UShort.MIN, uint(21317L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_OutOfServiceState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21318L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_OutOfServiceState_TransitionTime =
          new NodeId(UShort.MIN, uint(21319L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_OutOfServiceState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21320L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_OutOfServiceState_TrueState =
          new NodeId(UShort.MIN, uint(21321L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_OutOfServiceState_FalseState =
          new NodeId(UShort.MIN, uint(21322L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ShelvingState =
          new NodeId(UShort.MIN, uint(21323L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ShelvingState_CurrentState =
          new NodeId(UShort.MIN, uint(21324L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ShelvingState_CurrentState_Id =
          new NodeId(UShort.MIN, uint(21325L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ShelvingState_CurrentState_Name =
          new NodeId(UShort.MIN, uint(21326L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ShelvingState_CurrentState_Number =
          new NodeId(UShort.MIN, uint(21327L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ShelvingState_CurrentState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21328L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ShelvingState_LastTransition =
          new NodeId(UShort.MIN, uint(21329L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ShelvingState_LastTransition_Id =
          new NodeId(UShort.MIN, uint(21330L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ShelvingState_LastTransition_Name =
          new NodeId(UShort.MIN, uint(21331L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ShelvingState_LastTransition_Number =
          new NodeId(UShort.MIN, uint(21332L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ShelvingState_LastTransition_TransitionTime =
          new NodeId(UShort.MIN, uint(21333L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ShelvingState_LastTransition_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21334L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ShelvingState_AvailableStates =
          new NodeId(UShort.MIN, uint(21335L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ShelvingState_AvailableTransitions =
          new NodeId(UShort.MIN, uint(21336L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ShelvingState_UnshelveTime =
          new NodeId(UShort.MIN, uint(21337L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ShelvingState_TimedShelve =
          new NodeId(UShort.MIN, uint(21338L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ShelvingState_TimedShelve_InputArguments =
          new NodeId(UShort.MIN, uint(21339L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ShelvingState_Unshelve =
          new NodeId(UShort.MIN, uint(21340L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ShelvingState_OneShotShelve =
          new NodeId(UShort.MIN, uint(21341L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_SuppressedOrShelved =
          new NodeId(UShort.MIN, uint(21342L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_MaxTimeShelved =
          new NodeId(UShort.MIN, uint(21343L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_AudibleEnabled =
          new NodeId(UShort.MIN, uint(21344L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_AudibleSound =
          new NodeId(UShort.MIN, uint(21345L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_AudibleSound_ListId =
          new NodeId(UShort.MIN, uint(21346L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_AudibleSound_AgencyId =
          new NodeId(UShort.MIN, uint(21347L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_AudibleSound_VersionId =
          new NodeId(UShort.MIN, uint(21348L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_SilenceState =
          new NodeId(UShort.MIN, uint(21349L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_SilenceState_Id =
          new NodeId(UShort.MIN, uint(21350L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_SilenceState_Name =
          new NodeId(UShort.MIN, uint(21351L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_SilenceState_Number =
          new NodeId(UShort.MIN, uint(21352L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_SilenceState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21353L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_SilenceState_TransitionTime =
          new NodeId(UShort.MIN, uint(21354L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_SilenceState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21355L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_SilenceState_TrueState =
          new NodeId(UShort.MIN, uint(21356L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_SilenceState_FalseState =
          new NodeId(UShort.MIN, uint(21357L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_OnDelay =
          new NodeId(UShort.MIN, uint(21358L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_OffDelay =
          new NodeId(UShort.MIN, uint(21359L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_FirstInGroupFlag =
          new NodeId(UShort.MIN, uint(21360L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_FirstInGroup =
          new NodeId(UShort.MIN, uint(21361L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_LatchedState =
          new NodeId(UShort.MIN, uint(21362L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_LatchedState_Id =
          new NodeId(UShort.MIN, uint(21363L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_LatchedState_Name =
          new NodeId(UShort.MIN, uint(21364L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_LatchedState_Number =
          new NodeId(UShort.MIN, uint(21365L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_LatchedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21366L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_LatchedState_TransitionTime =
          new NodeId(UShort.MIN, uint(21367L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_LatchedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21368L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_LatchedState_TrueState =
          new NodeId(UShort.MIN, uint(21369L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_LatchedState_FalseState =
          new NodeId(UShort.MIN, uint(21370L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ReAlarmTime =
          new NodeId(UShort.MIN, uint(21371L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ReAlarmRepeatCount =
          new NodeId(UShort.MIN, uint(21372L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_Silence =
          new NodeId(UShort.MIN, uint(21373L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_Suppress =
          new NodeId(UShort.MIN, uint(21374L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_Unsuppress =
          new NodeId(UShort.MIN, uint(21375L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_RemoveFromService =
          new NodeId(UShort.MIN, uint(21376L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_PlaceInService =
          new NodeId(UShort.MIN, uint(21377L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_Reset =
          new NodeId(UShort.MIN, uint(21378L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_NormalState =
          new NodeId(UShort.MIN, uint(21379L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_TrustListId =
          new NodeId(UShort.MIN, uint(21380L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_LastUpdateTime =
          new NodeId(UShort.MIN, uint(21381L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_UpdateFrequency =
          new NodeId(UShort.MIN, uint(21382L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustList_UpdateFrequency =
          new NodeId(UShort.MIN, uint(21383L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired =
          new NodeId(UShort.MIN, uint(21384L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_EventId =
          new NodeId(UShort.MIN, uint(21385L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_EventType =
          new NodeId(UShort.MIN, uint(21386L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_SourceNode =
          new NodeId(UShort.MIN, uint(21387L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_SourceName =
          new NodeId(UShort.MIN, uint(21388L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_Time =
          new NodeId(UShort.MIN, uint(21389L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ReceiveTime =
          new NodeId(UShort.MIN, uint(21390L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_LocalTime =
          new NodeId(UShort.MIN, uint(21391L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_Message =
          new NodeId(UShort.MIN, uint(21392L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_Severity =
          new NodeId(UShort.MIN, uint(21393L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ConditionClassId =
          new NodeId(UShort.MIN, uint(21394L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ConditionClassName =
          new NodeId(UShort.MIN, uint(21395L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ConditionSubClassId =
          new NodeId(UShort.MIN, uint(21396L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ConditionSubClassName =
          new NodeId(UShort.MIN, uint(21397L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ConditionName =
          new NodeId(UShort.MIN, uint(21398L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_BranchId =
          new NodeId(UShort.MIN, uint(21399L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_Retain =
          new NodeId(UShort.MIN, uint(21400L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_EnabledState =
          new NodeId(UShort.MIN, uint(21401L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_EnabledState_Id =
          new NodeId(UShort.MIN, uint(21402L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_EnabledState_Name =
          new NodeId(UShort.MIN, uint(21403L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_EnabledState_Number =
          new NodeId(UShort.MIN, uint(21404L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_EnabledState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21405L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_EnabledState_TransitionTime =
          new NodeId(UShort.MIN, uint(21406L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_EnabledState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21407L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_EnabledState_TrueState =
          new NodeId(UShort.MIN, uint(21408L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_EnabledState_FalseState =
          new NodeId(UShort.MIN, uint(21409L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_Quality =
          new NodeId(UShort.MIN, uint(21410L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_Quality_SourceTimestamp =
          new NodeId(UShort.MIN, uint(21411L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_LastSeverity =
          new NodeId(UShort.MIN, uint(21412L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_LastSeverity_SourceTimestamp =
          new NodeId(UShort.MIN, uint(21413L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_Comment =
          new NodeId(UShort.MIN, uint(21414L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_Comment_SourceTimestamp =
          new NodeId(UShort.MIN, uint(21415L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ClientUserId =
          new NodeId(UShort.MIN, uint(21416L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_Disable =
          new NodeId(UShort.MIN, uint(21417L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_Enable =
          new NodeId(UShort.MIN, uint(21418L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_AddComment =
          new NodeId(UShort.MIN, uint(21419L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_AddComment_InputArguments =
          new NodeId(UShort.MIN, uint(21420L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_AckedState =
          new NodeId(UShort.MIN, uint(21421L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_AckedState_Id =
          new NodeId(UShort.MIN, uint(21422L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_AckedState_Name =
          new NodeId(UShort.MIN, uint(21423L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_AckedState_Number =
          new NodeId(UShort.MIN, uint(21424L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_AckedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21425L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_AckedState_TransitionTime =
          new NodeId(UShort.MIN, uint(21426L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_AckedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21427L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_AckedState_TrueState =
          new NodeId(UShort.MIN, uint(21428L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_AckedState_FalseState =
          new NodeId(UShort.MIN, uint(21429L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ConfirmedState =
          new NodeId(UShort.MIN, uint(21430L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ConfirmedState_Id =
          new NodeId(UShort.MIN, uint(21431L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ConfirmedState_Name =
          new NodeId(UShort.MIN, uint(21432L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ConfirmedState_Number =
          new NodeId(UShort.MIN, uint(21433L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ConfirmedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21434L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ConfirmedState_TransitionTime =
          new NodeId(UShort.MIN, uint(21435L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ConfirmedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21436L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ConfirmedState_TrueState =
          new NodeId(UShort.MIN, uint(21437L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ConfirmedState_FalseState =
          new NodeId(UShort.MIN, uint(21438L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_Acknowledge =
          new NodeId(UShort.MIN, uint(21439L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_Acknowledge_InputArguments =
          new NodeId(UShort.MIN, uint(21440L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_Confirm =
          new NodeId(UShort.MIN, uint(21441L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_Confirm_InputArguments =
          new NodeId(UShort.MIN, uint(21442L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ActiveState =
          new NodeId(UShort.MIN, uint(21443L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ActiveState_Id =
          new NodeId(UShort.MIN, uint(21444L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ActiveState_Name =
          new NodeId(UShort.MIN, uint(21445L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ActiveState_Number =
          new NodeId(UShort.MIN, uint(21446L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ActiveState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21447L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ActiveState_TransitionTime =
          new NodeId(UShort.MIN, uint(21448L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ActiveState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21449L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ActiveState_TrueState =
          new NodeId(UShort.MIN, uint(21450L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ActiveState_FalseState =
          new NodeId(UShort.MIN, uint(21451L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_InputNode =
          new NodeId(UShort.MIN, uint(21452L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_SuppressedState =
          new NodeId(UShort.MIN, uint(21453L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_SuppressedState_Id =
          new NodeId(UShort.MIN, uint(21454L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_SuppressedState_Name =
          new NodeId(UShort.MIN, uint(21455L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_SuppressedState_Number =
          new NodeId(UShort.MIN, uint(21456L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_SuppressedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21457L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_SuppressedState_TransitionTime =
          new NodeId(UShort.MIN, uint(21458L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_SuppressedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21459L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_SuppressedState_TrueState =
          new NodeId(UShort.MIN, uint(21460L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_SuppressedState_FalseState =
          new NodeId(UShort.MIN, uint(21461L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_OutOfServiceState =
          new NodeId(UShort.MIN, uint(21462L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_OutOfServiceState_Id =
          new NodeId(UShort.MIN, uint(21463L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_OutOfServiceState_Name =
          new NodeId(UShort.MIN, uint(21464L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_OutOfServiceState_Number =
          new NodeId(UShort.MIN, uint(21465L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_OutOfServiceState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21466L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_OutOfServiceState_TransitionTime =
          new NodeId(UShort.MIN, uint(21467L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_OutOfServiceState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21468L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_OutOfServiceState_TrueState =
          new NodeId(UShort.MIN, uint(21469L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_OutOfServiceState_FalseState =
          new NodeId(UShort.MIN, uint(21470L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ShelvingState =
          new NodeId(UShort.MIN, uint(21471L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ShelvingState_CurrentState =
          new NodeId(UShort.MIN, uint(21472L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ShelvingState_CurrentState_Id =
          new NodeId(UShort.MIN, uint(21473L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ShelvingState_CurrentState_Name =
          new NodeId(UShort.MIN, uint(21474L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ShelvingState_CurrentState_Number =
          new NodeId(UShort.MIN, uint(21475L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ShelvingState_CurrentState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21476L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ShelvingState_LastTransition =
          new NodeId(UShort.MIN, uint(21477L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ShelvingState_LastTransition_Id =
          new NodeId(UShort.MIN, uint(21478L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ShelvingState_LastTransition_Name =
          new NodeId(UShort.MIN, uint(21479L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ShelvingState_LastTransition_Number =
          new NodeId(UShort.MIN, uint(21480L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ShelvingState_LastTransition_TransitionTime =
          new NodeId(UShort.MIN, uint(21481L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ShelvingState_LastTransition_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21482L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ShelvingState_AvailableStates =
          new NodeId(UShort.MIN, uint(21483L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ShelvingState_AvailableTransitions =
          new NodeId(UShort.MIN, uint(21484L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ShelvingState_UnshelveTime =
          new NodeId(UShort.MIN, uint(21485L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ShelvingState_TimedShelve =
          new NodeId(UShort.MIN, uint(21486L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ShelvingState_TimedShelve_InputArguments =
          new NodeId(UShort.MIN, uint(21487L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ShelvingState_Unshelve =
          new NodeId(UShort.MIN, uint(21488L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ShelvingState_OneShotShelve =
          new NodeId(UShort.MIN, uint(21489L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_SuppressedOrShelved =
          new NodeId(UShort.MIN, uint(21490L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_MaxTimeShelved =
          new NodeId(UShort.MIN, uint(21491L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_AudibleEnabled =
          new NodeId(UShort.MIN, uint(21492L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_AudibleSound =
          new NodeId(UShort.MIN, uint(21493L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_AudibleSound_ListId =
          new NodeId(UShort.MIN, uint(21494L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_AudibleSound_AgencyId =
          new NodeId(UShort.MIN, uint(21495L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_AudibleSound_VersionId =
          new NodeId(UShort.MIN, uint(21496L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_SilenceState =
          new NodeId(UShort.MIN, uint(21497L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_SilenceState_Id =
          new NodeId(UShort.MIN, uint(21498L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_SilenceState_Name =
          new NodeId(UShort.MIN, uint(21499L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_SilenceState_Number =
          new NodeId(UShort.MIN, uint(21500L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_SilenceState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21501L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_SilenceState_TransitionTime =
          new NodeId(UShort.MIN, uint(21502L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_SilenceState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21503L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_SilenceState_TrueState =
          new NodeId(UShort.MIN, uint(21504L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_SilenceState_FalseState =
          new NodeId(UShort.MIN, uint(21505L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_OnDelay =
          new NodeId(UShort.MIN, uint(21506L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_OffDelay =
          new NodeId(UShort.MIN, uint(21507L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_FirstInGroupFlag =
          new NodeId(UShort.MIN, uint(21508L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_FirstInGroup =
          new NodeId(UShort.MIN, uint(21509L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_LatchedState =
          new NodeId(UShort.MIN, uint(21510L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_LatchedState_Id =
          new NodeId(UShort.MIN, uint(21511L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_LatchedState_Name =
          new NodeId(UShort.MIN, uint(21512L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_LatchedState_Number =
          new NodeId(UShort.MIN, uint(21513L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_LatchedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21514L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_LatchedState_TransitionTime =
          new NodeId(UShort.MIN, uint(21515L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_LatchedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21516L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_LatchedState_TrueState =
          new NodeId(UShort.MIN, uint(21517L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_LatchedState_FalseState =
          new NodeId(UShort.MIN, uint(21518L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ReAlarmTime =
          new NodeId(UShort.MIN, uint(21519L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ReAlarmRepeatCount =
          new NodeId(UShort.MIN, uint(21520L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_Silence =
          new NodeId(UShort.MIN, uint(21521L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_Suppress =
          new NodeId(UShort.MIN, uint(21522L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_Unsuppress =
          new NodeId(UShort.MIN, uint(21523L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_RemoveFromService =
          new NodeId(UShort.MIN, uint(21524L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_PlaceInService =
          new NodeId(UShort.MIN, uint(21525L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_Reset =
          new NodeId(UShort.MIN, uint(21526L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_NormalState =
          new NodeId(UShort.MIN, uint(21527L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ExpirationDate =
          new NodeId(UShort.MIN, uint(21528L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ExpirationLimit =
          new NodeId(UShort.MIN, uint(21529L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_CertificateType =
          new NodeId(UShort.MIN, uint(21530L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_Certificate =
          new NodeId(UShort.MIN, uint(21531L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate =
          new NodeId(UShort.MIN, uint(21532L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_EventId =
          new NodeId(UShort.MIN, uint(21533L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_EventType =
          new NodeId(UShort.MIN, uint(21534L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_SourceNode =
          new NodeId(UShort.MIN, uint(21535L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_SourceName =
          new NodeId(UShort.MIN, uint(21536L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_Time =
          new NodeId(UShort.MIN, uint(21537L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ReceiveTime =
          new NodeId(UShort.MIN, uint(21538L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_LocalTime =
          new NodeId(UShort.MIN, uint(21539L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_Message =
          new NodeId(UShort.MIN, uint(21540L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_Severity =
          new NodeId(UShort.MIN, uint(21541L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ConditionClassId =
          new NodeId(UShort.MIN, uint(21542L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ConditionClassName =
          new NodeId(UShort.MIN, uint(21543L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ConditionSubClassId =
          new NodeId(UShort.MIN, uint(21544L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ConditionSubClassName =
          new NodeId(UShort.MIN, uint(21545L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ConditionName =
          new NodeId(UShort.MIN, uint(21546L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_BranchId =
          new NodeId(UShort.MIN, uint(21547L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_Retain =
          new NodeId(UShort.MIN, uint(21548L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_EnabledState =
          new NodeId(UShort.MIN, uint(21549L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_EnabledState_Id =
          new NodeId(UShort.MIN, uint(21550L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_EnabledState_Name =
          new NodeId(UShort.MIN, uint(21551L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_EnabledState_Number =
          new NodeId(UShort.MIN, uint(21552L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_EnabledState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21553L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_EnabledState_TransitionTime =
          new NodeId(UShort.MIN, uint(21554L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_EnabledState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21555L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_EnabledState_TrueState =
          new NodeId(UShort.MIN, uint(21556L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_EnabledState_FalseState =
          new NodeId(UShort.MIN, uint(21557L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_Quality =
          new NodeId(UShort.MIN, uint(21558L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_Quality_SourceTimestamp =
          new NodeId(UShort.MIN, uint(21559L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_LastSeverity =
          new NodeId(UShort.MIN, uint(21560L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_LastSeverity_SourceTimestamp =
          new NodeId(UShort.MIN, uint(21561L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_Comment =
          new NodeId(UShort.MIN, uint(21562L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_Comment_SourceTimestamp =
          new NodeId(UShort.MIN, uint(21563L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ClientUserId =
          new NodeId(UShort.MIN, uint(21564L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_Disable =
          new NodeId(UShort.MIN, uint(21565L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_Enable =
          new NodeId(UShort.MIN, uint(21566L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_AddComment =
          new NodeId(UShort.MIN, uint(21567L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_AddComment_InputArguments =
          new NodeId(UShort.MIN, uint(21568L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_AckedState =
          new NodeId(UShort.MIN, uint(21569L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_AckedState_Id =
          new NodeId(UShort.MIN, uint(21570L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_AckedState_Name =
          new NodeId(UShort.MIN, uint(21571L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_AckedState_Number =
          new NodeId(UShort.MIN, uint(21572L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_AckedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21573L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_AckedState_TransitionTime =
          new NodeId(UShort.MIN, uint(21574L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_AckedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21575L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_AckedState_TrueState =
          new NodeId(UShort.MIN, uint(21576L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_AckedState_FalseState =
          new NodeId(UShort.MIN, uint(21577L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ConfirmedState =
          new NodeId(UShort.MIN, uint(21578L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ConfirmedState_Id =
          new NodeId(UShort.MIN, uint(21579L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ConfirmedState_Name =
          new NodeId(UShort.MIN, uint(21580L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ConfirmedState_Number =
          new NodeId(UShort.MIN, uint(21581L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ConfirmedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21582L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ConfirmedState_TransitionTime =
          new NodeId(UShort.MIN, uint(21583L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ConfirmedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21584L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ConfirmedState_TrueState =
          new NodeId(UShort.MIN, uint(21585L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ConfirmedState_FalseState =
          new NodeId(UShort.MIN, uint(21586L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_Acknowledge =
          new NodeId(UShort.MIN, uint(21587L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_Acknowledge_InputArguments =
          new NodeId(UShort.MIN, uint(21588L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_Confirm =
          new NodeId(UShort.MIN, uint(21589L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_Confirm_InputArguments =
          new NodeId(UShort.MIN, uint(21590L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ActiveState =
          new NodeId(UShort.MIN, uint(21591L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ActiveState_Id =
          new NodeId(UShort.MIN, uint(21592L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ActiveState_Name =
          new NodeId(UShort.MIN, uint(21593L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ActiveState_Number =
          new NodeId(UShort.MIN, uint(21594L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ActiveState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21595L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ActiveState_TransitionTime =
          new NodeId(UShort.MIN, uint(21596L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ActiveState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21597L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ActiveState_TrueState =
          new NodeId(UShort.MIN, uint(21598L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ActiveState_FalseState =
          new NodeId(UShort.MIN, uint(21599L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_InputNode =
          new NodeId(UShort.MIN, uint(21600L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_SuppressedState =
          new NodeId(UShort.MIN, uint(21601L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_SuppressedState_Id =
          new NodeId(UShort.MIN, uint(21602L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_SuppressedState_Name =
          new NodeId(UShort.MIN, uint(21603L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_SuppressedState_Number =
          new NodeId(UShort.MIN, uint(21604L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_SuppressedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21605L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_SuppressedState_TransitionTime =
          new NodeId(UShort.MIN, uint(21606L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_SuppressedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21607L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_SuppressedState_TrueState =
          new NodeId(UShort.MIN, uint(21608L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_SuppressedState_FalseState =
          new NodeId(UShort.MIN, uint(21609L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_OutOfServiceState =
          new NodeId(UShort.MIN, uint(21610L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_OutOfServiceState_Id =
          new NodeId(UShort.MIN, uint(21611L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_OutOfServiceState_Name =
          new NodeId(UShort.MIN, uint(21612L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_OutOfServiceState_Number =
          new NodeId(UShort.MIN, uint(21613L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_OutOfServiceState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21614L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_OutOfServiceState_TransitionTime =
          new NodeId(UShort.MIN, uint(21615L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_OutOfServiceState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21616L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_OutOfServiceState_TrueState =
          new NodeId(UShort.MIN, uint(21617L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_OutOfServiceState_FalseState =
          new NodeId(UShort.MIN, uint(21618L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ShelvingState =
          new NodeId(UShort.MIN, uint(21619L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ShelvingState_CurrentState =
          new NodeId(UShort.MIN, uint(21620L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ShelvingState_CurrentState_Id =
          new NodeId(UShort.MIN, uint(21621L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ShelvingState_CurrentState_Name =
          new NodeId(UShort.MIN, uint(21622L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ShelvingState_CurrentState_Number =
          new NodeId(UShort.MIN, uint(21623L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ShelvingState_CurrentState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21624L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ShelvingState_LastTransition =
          new NodeId(UShort.MIN, uint(21625L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ShelvingState_LastTransition_Id =
          new NodeId(UShort.MIN, uint(21626L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ShelvingState_LastTransition_Name =
          new NodeId(UShort.MIN, uint(21627L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ShelvingState_LastTransition_Number =
          new NodeId(UShort.MIN, uint(21628L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ShelvingState_LastTransition_TransitionTime =
          new NodeId(UShort.MIN, uint(21629L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ShelvingState_LastTransition_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21630L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ShelvingState_AvailableStates =
          new NodeId(UShort.MIN, uint(21631L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ShelvingState_AvailableTransitions =
          new NodeId(UShort.MIN, uint(21632L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ShelvingState_UnshelveTime =
          new NodeId(UShort.MIN, uint(21633L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ShelvingState_TimedShelve =
          new NodeId(UShort.MIN, uint(21634L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ShelvingState_TimedShelve_InputArguments =
          new NodeId(UShort.MIN, uint(21635L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ShelvingState_Unshelve =
          new NodeId(UShort.MIN, uint(21636L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ShelvingState_OneShotShelve =
          new NodeId(UShort.MIN, uint(21637L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_SuppressedOrShelved =
          new NodeId(UShort.MIN, uint(21638L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_MaxTimeShelved =
          new NodeId(UShort.MIN, uint(21639L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_AudibleEnabled =
          new NodeId(UShort.MIN, uint(21640L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_AudibleSound =
          new NodeId(UShort.MIN, uint(21641L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_AudibleSound_ListId =
          new NodeId(UShort.MIN, uint(21642L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_AudibleSound_AgencyId =
          new NodeId(UShort.MIN, uint(21643L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_AudibleSound_VersionId =
          new NodeId(UShort.MIN, uint(21644L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_SilenceState =
          new NodeId(UShort.MIN, uint(21645L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_SilenceState_Id =
          new NodeId(UShort.MIN, uint(21646L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_SilenceState_Name =
          new NodeId(UShort.MIN, uint(21647L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_SilenceState_Number =
          new NodeId(UShort.MIN, uint(21648L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_SilenceState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21649L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_SilenceState_TransitionTime =
          new NodeId(UShort.MIN, uint(21650L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_SilenceState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21651L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_SilenceState_TrueState =
          new NodeId(UShort.MIN, uint(21652L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_SilenceState_FalseState =
          new NodeId(UShort.MIN, uint(21653L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_OnDelay =
          new NodeId(UShort.MIN, uint(21654L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_OffDelay =
          new NodeId(UShort.MIN, uint(21655L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_FirstInGroupFlag =
          new NodeId(UShort.MIN, uint(21656L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_FirstInGroup =
          new NodeId(UShort.MIN, uint(21657L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_LatchedState =
          new NodeId(UShort.MIN, uint(21658L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_LatchedState_Id =
          new NodeId(UShort.MIN, uint(21659L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_LatchedState_Name =
          new NodeId(UShort.MIN, uint(21660L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_LatchedState_Number =
          new NodeId(UShort.MIN, uint(21661L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_LatchedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21662L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_LatchedState_TransitionTime =
          new NodeId(UShort.MIN, uint(21663L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_LatchedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21664L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_LatchedState_TrueState =
          new NodeId(UShort.MIN, uint(21665L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_LatchedState_FalseState =
          new NodeId(UShort.MIN, uint(21666L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ReAlarmTime =
          new NodeId(UShort.MIN, uint(21667L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ReAlarmRepeatCount =
          new NodeId(UShort.MIN, uint(21668L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_Silence =
          new NodeId(UShort.MIN, uint(21669L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_Suppress =
          new NodeId(UShort.MIN, uint(21670L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_Unsuppress =
          new NodeId(UShort.MIN, uint(21671L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_RemoveFromService =
          new NodeId(UShort.MIN, uint(21672L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_PlaceInService =
          new NodeId(UShort.MIN, uint(21673L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_Reset =
          new NodeId(UShort.MIN, uint(21674L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_NormalState =
          new NodeId(UShort.MIN, uint(21675L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_TrustListId =
          new NodeId(UShort.MIN, uint(21676L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_LastUpdateTime =
          new NodeId(UShort.MIN, uint(21677L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_UpdateFrequency =
          new NodeId(UShort.MIN, uint(21678L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustList_UpdateFrequency =
          new NodeId(UShort.MIN, uint(21679L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired =
          new NodeId(UShort.MIN, uint(21680L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_EventId =
          new NodeId(UShort.MIN, uint(21681L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_EventType =
          new NodeId(UShort.MIN, uint(21682L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_SourceNode =
          new NodeId(UShort.MIN, uint(21683L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_SourceName =
          new NodeId(UShort.MIN, uint(21684L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_Time =
          new NodeId(UShort.MIN, uint(21685L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ReceiveTime =
          new NodeId(UShort.MIN, uint(21686L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_LocalTime =
          new NodeId(UShort.MIN, uint(21687L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_Message =
          new NodeId(UShort.MIN, uint(21688L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_Severity =
          new NodeId(UShort.MIN, uint(21689L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ConditionClassId =
          new NodeId(UShort.MIN, uint(21690L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ConditionClassName =
          new NodeId(UShort.MIN, uint(21691L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ConditionSubClassId =
          new NodeId(UShort.MIN, uint(21692L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ConditionSubClassName =
          new NodeId(UShort.MIN, uint(21693L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ConditionName =
          new NodeId(UShort.MIN, uint(21694L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_BranchId =
          new NodeId(UShort.MIN, uint(21695L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_Retain =
          new NodeId(UShort.MIN, uint(21696L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_EnabledState =
          new NodeId(UShort.MIN, uint(21697L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_EnabledState_Id =
          new NodeId(UShort.MIN, uint(21698L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_EnabledState_Name =
          new NodeId(UShort.MIN, uint(21699L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_EnabledState_Number =
          new NodeId(UShort.MIN, uint(21700L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_EnabledState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21701L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_EnabledState_TransitionTime =
          new NodeId(UShort.MIN, uint(21702L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_EnabledState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21703L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_EnabledState_TrueState =
          new NodeId(UShort.MIN, uint(21704L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_EnabledState_FalseState =
          new NodeId(UShort.MIN, uint(21705L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_Quality =
          new NodeId(UShort.MIN, uint(21706L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_Quality_SourceTimestamp =
          new NodeId(UShort.MIN, uint(21707L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_LastSeverity =
          new NodeId(UShort.MIN, uint(21708L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_LastSeverity_SourceTimestamp =
          new NodeId(UShort.MIN, uint(21709L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_Comment =
          new NodeId(UShort.MIN, uint(21710L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_Comment_SourceTimestamp =
          new NodeId(UShort.MIN, uint(21711L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ClientUserId =
          new NodeId(UShort.MIN, uint(21712L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_Disable =
          new NodeId(UShort.MIN, uint(21713L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_Enable =
          new NodeId(UShort.MIN, uint(21714L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_AddComment =
          new NodeId(UShort.MIN, uint(21715L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_AddComment_InputArguments =
          new NodeId(UShort.MIN, uint(21716L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_AckedState =
          new NodeId(UShort.MIN, uint(21717L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_AckedState_Id =
          new NodeId(UShort.MIN, uint(21718L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_AckedState_Name =
          new NodeId(UShort.MIN, uint(21719L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_AckedState_Number =
          new NodeId(UShort.MIN, uint(21720L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_AckedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21721L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_AckedState_TransitionTime =
          new NodeId(UShort.MIN, uint(21722L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_AckedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21723L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_AckedState_TrueState =
          new NodeId(UShort.MIN, uint(21724L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_AckedState_FalseState =
          new NodeId(UShort.MIN, uint(21725L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ConfirmedState =
          new NodeId(UShort.MIN, uint(21726L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ConfirmedState_Id =
          new NodeId(UShort.MIN, uint(21727L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ConfirmedState_Name =
          new NodeId(UShort.MIN, uint(21728L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ConfirmedState_Number =
          new NodeId(UShort.MIN, uint(21729L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ConfirmedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21730L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ConfirmedState_TransitionTime =
          new NodeId(UShort.MIN, uint(21731L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ConfirmedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21732L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ConfirmedState_TrueState =
          new NodeId(UShort.MIN, uint(21733L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ConfirmedState_FalseState =
          new NodeId(UShort.MIN, uint(21734L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_Acknowledge =
          new NodeId(UShort.MIN, uint(21735L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_Acknowledge_InputArguments =
          new NodeId(UShort.MIN, uint(21736L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_Confirm =
          new NodeId(UShort.MIN, uint(21737L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_Confirm_InputArguments =
          new NodeId(UShort.MIN, uint(21738L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ActiveState =
          new NodeId(UShort.MIN, uint(21739L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ActiveState_Id =
          new NodeId(UShort.MIN, uint(21740L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ActiveState_Name =
          new NodeId(UShort.MIN, uint(21741L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ActiveState_Number =
          new NodeId(UShort.MIN, uint(21742L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ActiveState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21743L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ActiveState_TransitionTime =
          new NodeId(UShort.MIN, uint(21744L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ActiveState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21745L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ActiveState_TrueState =
          new NodeId(UShort.MIN, uint(21746L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ActiveState_FalseState =
          new NodeId(UShort.MIN, uint(21747L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_InputNode =
          new NodeId(UShort.MIN, uint(21748L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_SuppressedState =
          new NodeId(UShort.MIN, uint(21749L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_SuppressedState_Id =
          new NodeId(UShort.MIN, uint(21750L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_SuppressedState_Name =
          new NodeId(UShort.MIN, uint(21751L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_SuppressedState_Number =
          new NodeId(UShort.MIN, uint(21752L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_SuppressedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21753L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_SuppressedState_TransitionTime =
          new NodeId(UShort.MIN, uint(21754L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_SuppressedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21755L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_SuppressedState_TrueState =
          new NodeId(UShort.MIN, uint(21756L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_SuppressedState_FalseState =
          new NodeId(UShort.MIN, uint(21757L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_OutOfServiceState =
          new NodeId(UShort.MIN, uint(21758L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_OutOfServiceState_Id =
          new NodeId(UShort.MIN, uint(21759L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_OutOfServiceState_Name =
          new NodeId(UShort.MIN, uint(21760L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_OutOfServiceState_Number =
          new NodeId(UShort.MIN, uint(21761L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_OutOfServiceState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21762L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_OutOfServiceState_TransitionTime =
          new NodeId(UShort.MIN, uint(21763L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_OutOfServiceState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21764L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_OutOfServiceState_TrueState =
          new NodeId(UShort.MIN, uint(21765L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_OutOfServiceState_FalseState =
          new NodeId(UShort.MIN, uint(21766L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ShelvingState =
          new NodeId(UShort.MIN, uint(21767L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ShelvingState_CurrentState =
          new NodeId(UShort.MIN, uint(21768L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ShelvingState_CurrentState_Id =
          new NodeId(UShort.MIN, uint(21769L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ShelvingState_CurrentState_Name =
          new NodeId(UShort.MIN, uint(21770L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ShelvingState_CurrentState_Number =
          new NodeId(UShort.MIN, uint(21771L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ShelvingState_CurrentState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21772L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ShelvingState_LastTransition =
          new NodeId(UShort.MIN, uint(21773L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ShelvingState_LastTransition_Id =
          new NodeId(UShort.MIN, uint(21774L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ShelvingState_LastTransition_Name =
          new NodeId(UShort.MIN, uint(21775L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ShelvingState_LastTransition_Number =
          new NodeId(UShort.MIN, uint(21776L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ShelvingState_LastTransition_TransitionTime =
          new NodeId(UShort.MIN, uint(21777L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ShelvingState_LastTransition_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21778L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ShelvingState_AvailableStates =
          new NodeId(UShort.MIN, uint(21779L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ShelvingState_AvailableTransitions =
          new NodeId(UShort.MIN, uint(21780L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ShelvingState_UnshelveTime =
          new NodeId(UShort.MIN, uint(21781L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ShelvingState_TimedShelve =
          new NodeId(UShort.MIN, uint(21782L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ShelvingState_TimedShelve_InputArguments =
          new NodeId(UShort.MIN, uint(21783L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ShelvingState_Unshelve =
          new NodeId(UShort.MIN, uint(21784L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ShelvingState_OneShotShelve =
          new NodeId(UShort.MIN, uint(21785L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_SuppressedOrShelved =
          new NodeId(UShort.MIN, uint(21786L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_MaxTimeShelved =
          new NodeId(UShort.MIN, uint(21787L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_AudibleEnabled =
          new NodeId(UShort.MIN, uint(21788L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_AudibleSound =
          new NodeId(UShort.MIN, uint(21789L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_AudibleSound_ListId =
          new NodeId(UShort.MIN, uint(21790L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_AudibleSound_AgencyId =
          new NodeId(UShort.MIN, uint(21791L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_AudibleSound_VersionId =
          new NodeId(UShort.MIN, uint(21792L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_SilenceState =
          new NodeId(UShort.MIN, uint(21793L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_SilenceState_Id =
          new NodeId(UShort.MIN, uint(21794L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_SilenceState_Name =
          new NodeId(UShort.MIN, uint(21795L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_SilenceState_Number =
          new NodeId(UShort.MIN, uint(21796L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_SilenceState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21797L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_SilenceState_TransitionTime =
          new NodeId(UShort.MIN, uint(21798L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_SilenceState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21799L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_SilenceState_TrueState =
          new NodeId(UShort.MIN, uint(21800L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_SilenceState_FalseState =
          new NodeId(UShort.MIN, uint(21801L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_OnDelay =
          new NodeId(UShort.MIN, uint(21802L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_OffDelay =
          new NodeId(UShort.MIN, uint(21803L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_FirstInGroupFlag =
          new NodeId(UShort.MIN, uint(21804L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_FirstInGroup =
          new NodeId(UShort.MIN, uint(21805L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_LatchedState =
          new NodeId(UShort.MIN, uint(21806L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_LatchedState_Id =
          new NodeId(UShort.MIN, uint(21807L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_LatchedState_Name =
          new NodeId(UShort.MIN, uint(21808L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_LatchedState_Number =
          new NodeId(UShort.MIN, uint(21809L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_LatchedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21810L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_LatchedState_TransitionTime =
          new NodeId(UShort.MIN, uint(21811L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_LatchedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21812L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_LatchedState_TrueState =
          new NodeId(UShort.MIN, uint(21813L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_LatchedState_FalseState =
          new NodeId(UShort.MIN, uint(21814L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ReAlarmTime =
          new NodeId(UShort.MIN, uint(21815L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ReAlarmRepeatCount =
          new NodeId(UShort.MIN, uint(21816L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_Silence =
          new NodeId(UShort.MIN, uint(21817L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_Suppress =
          new NodeId(UShort.MIN, uint(21818L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_Unsuppress =
          new NodeId(UShort.MIN, uint(21819L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_RemoveFromService =
          new NodeId(UShort.MIN, uint(21820L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_PlaceInService =
          new NodeId(UShort.MIN, uint(21821L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_Reset =
          new NodeId(UShort.MIN, uint(21822L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_NormalState =
          new NodeId(UShort.MIN, uint(21823L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ExpirationDate =
          new NodeId(UShort.MIN, uint(21824L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ExpirationLimit =
          new NodeId(UShort.MIN, uint(21825L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_CertificateType =
          new NodeId(UShort.MIN, uint(21826L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_Certificate =
          new NodeId(UShort.MIN, uint(21827L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate =
          new NodeId(UShort.MIN, uint(21828L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_EventId =
          new NodeId(UShort.MIN, uint(21829L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_EventType =
          new NodeId(UShort.MIN, uint(21830L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_SourceNode =
          new NodeId(UShort.MIN, uint(21831L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_SourceName =
          new NodeId(UShort.MIN, uint(21832L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_Time =
          new NodeId(UShort.MIN, uint(21833L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ReceiveTime =
          new NodeId(UShort.MIN, uint(21834L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_LocalTime =
          new NodeId(UShort.MIN, uint(21835L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_Message =
          new NodeId(UShort.MIN, uint(21836L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_Severity =
          new NodeId(UShort.MIN, uint(21837L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ConditionClassId =
          new NodeId(UShort.MIN, uint(21838L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ConditionClassName =
          new NodeId(UShort.MIN, uint(21839L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ConditionSubClassId =
          new NodeId(UShort.MIN, uint(21840L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ConditionSubClassName =
          new NodeId(UShort.MIN, uint(21841L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ConditionName =
          new NodeId(UShort.MIN, uint(21842L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_BranchId =
          new NodeId(UShort.MIN, uint(21843L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_Retain =
          new NodeId(UShort.MIN, uint(21844L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_EnabledState =
          new NodeId(UShort.MIN, uint(21845L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_EnabledState_Id =
          new NodeId(UShort.MIN, uint(21846L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_EnabledState_Name =
          new NodeId(UShort.MIN, uint(21847L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_EnabledState_Number =
          new NodeId(UShort.MIN, uint(21848L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_EnabledState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21849L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_EnabledState_TransitionTime =
          new NodeId(UShort.MIN, uint(21850L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_EnabledState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21851L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_EnabledState_TrueState =
          new NodeId(UShort.MIN, uint(21852L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_EnabledState_FalseState =
          new NodeId(UShort.MIN, uint(21853L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_Quality =
          new NodeId(UShort.MIN, uint(21854L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_Quality_SourceTimestamp =
          new NodeId(UShort.MIN, uint(21855L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_LastSeverity =
          new NodeId(UShort.MIN, uint(21856L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_LastSeverity_SourceTimestamp =
          new NodeId(UShort.MIN, uint(21857L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_Comment =
          new NodeId(UShort.MIN, uint(21858L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_Comment_SourceTimestamp =
          new NodeId(UShort.MIN, uint(21859L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ClientUserId =
          new NodeId(UShort.MIN, uint(21860L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_Disable =
          new NodeId(UShort.MIN, uint(21861L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_Enable =
          new NodeId(UShort.MIN, uint(21862L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_AddComment =
          new NodeId(UShort.MIN, uint(21863L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_AddComment_InputArguments =
          new NodeId(UShort.MIN, uint(21864L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_AckedState =
          new NodeId(UShort.MIN, uint(21865L));
}

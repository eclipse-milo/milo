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

abstract class NodeIds2 extends NodeIds3 {
  public static final NodeId
      SubscriptionDiagnosticsArrayType_SubscriptionDiagnostics_DiscardedMessageCount =
          new NodeId(UShort.MIN, uint(12810));

  public static final NodeId
      SubscriptionDiagnosticsArrayType_SubscriptionDiagnostics_MonitoredItemCount =
          new NodeId(UShort.MIN, uint(12811));

  public static final NodeId
      SubscriptionDiagnosticsArrayType_SubscriptionDiagnostics_DisabledMonitoredItemCount =
          new NodeId(UShort.MIN, uint(12812));

  public static final NodeId
      SubscriptionDiagnosticsArrayType_SubscriptionDiagnostics_MonitoringQueueOverflowCount =
          new NodeId(UShort.MIN, uint(12813));

  public static final NodeId
      SubscriptionDiagnosticsArrayType_SubscriptionDiagnostics_NextSequenceNumber =
          new NodeId(UShort.MIN, uint(12814));

  public static final NodeId
      SubscriptionDiagnosticsArrayType_SubscriptionDiagnostics_EventQueueOverflowCount =
          new NodeId(UShort.MIN, uint(12815));

  public static final NodeId SessionDiagnosticsArrayType_SessionDiagnostics =
      new NodeId(UShort.MIN, uint(12816));

  public static final NodeId SessionDiagnosticsArrayType_SessionDiagnostics_SessionId =
      new NodeId(UShort.MIN, uint(12817));

  public static final NodeId SessionDiagnosticsArrayType_SessionDiagnostics_SessionName =
      new NodeId(UShort.MIN, uint(12818));

  public static final NodeId SessionDiagnosticsArrayType_SessionDiagnostics_ClientDescription =
      new NodeId(UShort.MIN, uint(12819));

  public static final NodeId SessionDiagnosticsArrayType_SessionDiagnostics_ServerUri =
      new NodeId(UShort.MIN, uint(12820));

  public static final NodeId SessionDiagnosticsArrayType_SessionDiagnostics_EndpointUrl =
      new NodeId(UShort.MIN, uint(12821));

  public static final NodeId SessionDiagnosticsArrayType_SessionDiagnostics_LocaleIds =
      new NodeId(UShort.MIN, uint(12822));

  public static final NodeId SessionDiagnosticsArrayType_SessionDiagnostics_ActualSessionTimeout =
      new NodeId(UShort.MIN, uint(12823));

  public static final NodeId SessionDiagnosticsArrayType_SessionDiagnostics_MaxResponseMessageSize =
      new NodeId(UShort.MIN, uint(12824));

  public static final NodeId SessionDiagnosticsArrayType_SessionDiagnostics_ClientConnectionTime =
      new NodeId(UShort.MIN, uint(12825));

  public static final NodeId SessionDiagnosticsArrayType_SessionDiagnostics_ClientLastContactTime =
      new NodeId(UShort.MIN, uint(12826));

  public static final NodeId
      SessionDiagnosticsArrayType_SessionDiagnostics_CurrentSubscriptionsCount =
          new NodeId(UShort.MIN, uint(12827));

  public static final NodeId
      SessionDiagnosticsArrayType_SessionDiagnostics_CurrentMonitoredItemsCount =
          new NodeId(UShort.MIN, uint(12828));

  public static final NodeId
      SessionDiagnosticsArrayType_SessionDiagnostics_CurrentPublishRequestsInQueue =
          new NodeId(UShort.MIN, uint(12829));

  public static final NodeId SessionDiagnosticsArrayType_SessionDiagnostics_TotalRequestCount =
      new NodeId(UShort.MIN, uint(12830));

  public static final NodeId
      SessionDiagnosticsArrayType_SessionDiagnostics_UnauthorizedRequestCount =
          new NodeId(UShort.MIN, uint(12831));

  public static final NodeId SessionDiagnosticsArrayType_SessionDiagnostics_ReadCount =
      new NodeId(UShort.MIN, uint(12832));

  public static final NodeId SessionDiagnosticsArrayType_SessionDiagnostics_HistoryReadCount =
      new NodeId(UShort.MIN, uint(12833));

  public static final NodeId SessionDiagnosticsArrayType_SessionDiagnostics_WriteCount =
      new NodeId(UShort.MIN, uint(12834));

  public static final NodeId SessionDiagnosticsArrayType_SessionDiagnostics_HistoryUpdateCount =
      new NodeId(UShort.MIN, uint(12835));

  public static final NodeId SessionDiagnosticsArrayType_SessionDiagnostics_CallCount =
      new NodeId(UShort.MIN, uint(12836));

  public static final NodeId
      SessionDiagnosticsArrayType_SessionDiagnostics_CreateMonitoredItemsCount =
          new NodeId(UShort.MIN, uint(12837));

  public static final NodeId
      SessionDiagnosticsArrayType_SessionDiagnostics_ModifyMonitoredItemsCount =
          new NodeId(UShort.MIN, uint(12838));

  public static final NodeId SessionDiagnosticsArrayType_SessionDiagnostics_SetMonitoringModeCount =
      new NodeId(UShort.MIN, uint(12839));

  public static final NodeId SessionDiagnosticsArrayType_SessionDiagnostics_SetTriggeringCount =
      new NodeId(UShort.MIN, uint(12840));

  public static final NodeId
      SessionDiagnosticsArrayType_SessionDiagnostics_DeleteMonitoredItemsCount =
          new NodeId(UShort.MIN, uint(12841));

  public static final NodeId
      SessionDiagnosticsArrayType_SessionDiagnostics_CreateSubscriptionCount =
          new NodeId(UShort.MIN, uint(12842));

  public static final NodeId
      SessionDiagnosticsArrayType_SessionDiagnostics_ModifySubscriptionCount =
          new NodeId(UShort.MIN, uint(12843));

  public static final NodeId SessionDiagnosticsArrayType_SessionDiagnostics_SetPublishingModeCount =
      new NodeId(UShort.MIN, uint(12844));

  public static final NodeId SessionDiagnosticsArrayType_SessionDiagnostics_PublishCount =
      new NodeId(UShort.MIN, uint(12845));

  public static final NodeId SessionDiagnosticsArrayType_SessionDiagnostics_RepublishCount =
      new NodeId(UShort.MIN, uint(12846));

  public static final NodeId
      SessionDiagnosticsArrayType_SessionDiagnostics_TransferSubscriptionsCount =
          new NodeId(UShort.MIN, uint(12847));

  public static final NodeId
      SessionDiagnosticsArrayType_SessionDiagnostics_DeleteSubscriptionsCount =
          new NodeId(UShort.MIN, uint(12848));

  public static final NodeId SessionDiagnosticsArrayType_SessionDiagnostics_AddNodesCount =
      new NodeId(UShort.MIN, uint(12849));

  public static final NodeId SessionDiagnosticsArrayType_SessionDiagnostics_AddReferencesCount =
      new NodeId(UShort.MIN, uint(12850));

  public static final NodeId SessionDiagnosticsArrayType_SessionDiagnostics_DeleteNodesCount =
      new NodeId(UShort.MIN, uint(12851));

  public static final NodeId SessionDiagnosticsArrayType_SessionDiagnostics_DeleteReferencesCount =
      new NodeId(UShort.MIN, uint(12852));

  public static final NodeId SessionDiagnosticsArrayType_SessionDiagnostics_BrowseCount =
      new NodeId(UShort.MIN, uint(12853));

  public static final NodeId SessionDiagnosticsArrayType_SessionDiagnostics_BrowseNextCount =
      new NodeId(UShort.MIN, uint(12854));

  public static final NodeId
      SessionDiagnosticsArrayType_SessionDiagnostics_TranslateBrowsePathsToNodeIdsCount =
          new NodeId(UShort.MIN, uint(12855));

  public static final NodeId SessionDiagnosticsArrayType_SessionDiagnostics_QueryFirstCount =
      new NodeId(UShort.MIN, uint(12856));

  public static final NodeId SessionDiagnosticsArrayType_SessionDiagnostics_QueryNextCount =
      new NodeId(UShort.MIN, uint(12857));

  public static final NodeId SessionDiagnosticsArrayType_SessionDiagnostics_RegisterNodesCount =
      new NodeId(UShort.MIN, uint(12858));

  public static final NodeId SessionDiagnosticsArrayType_SessionDiagnostics_UnregisterNodesCount =
      new NodeId(UShort.MIN, uint(12859));

  public static final NodeId SessionSecurityDiagnosticsArrayType_SessionSecurityDiagnostics =
      new NodeId(UShort.MIN, uint(12860));

  public static final NodeId
      SessionSecurityDiagnosticsArrayType_SessionSecurityDiagnostics_SessionId =
          new NodeId(UShort.MIN, uint(12861));

  public static final NodeId
      SessionSecurityDiagnosticsArrayType_SessionSecurityDiagnostics_ClientUserIdOfSession =
          new NodeId(UShort.MIN, uint(12862));

  public static final NodeId
      SessionSecurityDiagnosticsArrayType_SessionSecurityDiagnostics_ClientUserIdHistory =
          new NodeId(UShort.MIN, uint(12863));

  public static final NodeId
      SessionSecurityDiagnosticsArrayType_SessionSecurityDiagnostics_AuthenticationMechanism =
          new NodeId(UShort.MIN, uint(12864));

  public static final NodeId
      SessionSecurityDiagnosticsArrayType_SessionSecurityDiagnostics_Encoding =
          new NodeId(UShort.MIN, uint(12865));

  public static final NodeId
      SessionSecurityDiagnosticsArrayType_SessionSecurityDiagnostics_TransportProtocol =
          new NodeId(UShort.MIN, uint(12866));

  public static final NodeId
      SessionSecurityDiagnosticsArrayType_SessionSecurityDiagnostics_SecurityMode =
          new NodeId(UShort.MIN, uint(12867));

  public static final NodeId
      SessionSecurityDiagnosticsArrayType_SessionSecurityDiagnostics_SecurityPolicyUri =
          new NodeId(UShort.MIN, uint(12868));

  public static final NodeId
      SessionSecurityDiagnosticsArrayType_SessionSecurityDiagnostics_ClientCertificate =
          new NodeId(UShort.MIN, uint(12869));

  public static final NodeId ServerType_ResendData = new NodeId(UShort.MIN, uint(12871));

  public static final NodeId ServerType_ResendData_InputArguments =
      new NodeId(UShort.MIN, uint(12872));

  public static final NodeId Server_ResendData = new NodeId(UShort.MIN, uint(12873));

  public static final NodeId Server_ResendData_InputArguments = new NodeId(UShort.MIN, uint(12874));

  public static final NodeId ResendDataMethodType = new NodeId(UShort.MIN, uint(12875));

  public static final NodeId ResendDataMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(12876));

  public static final NodeId NormalizedString = new NodeId(UShort.MIN, uint(12877));

  public static final NodeId DecimalString = new NodeId(UShort.MIN, uint(12878));

  public static final NodeId DurationString = new NodeId(UShort.MIN, uint(12879));

  public static final NodeId TimeString = new NodeId(UShort.MIN, uint(12880));

  public static final NodeId DateString = new NodeId(UShort.MIN, uint(12881));

  public static final NodeId ServerType_EstimatedReturnTime = new NodeId(UShort.MIN, uint(12882));

  public static final NodeId ServerType_RequestServerStateChange =
      new NodeId(UShort.MIN, uint(12883));

  public static final NodeId ServerType_RequestServerStateChange_InputArguments =
      new NodeId(UShort.MIN, uint(12884));

  public static final NodeId Server_EstimatedReturnTime = new NodeId(UShort.MIN, uint(12885));

  public static final NodeId Server_RequestServerStateChange = new NodeId(UShort.MIN, uint(12886));

  public static final NodeId Server_RequestServerStateChange_InputArguments =
      new NodeId(UShort.MIN, uint(12887));

  public static final NodeId RequestServerStateChangeMethodType =
      new NodeId(UShort.MIN, uint(12888));

  public static final NodeId RequestServerStateChangeMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(12889));

  public static final NodeId DiscoveryConfiguration = new NodeId(UShort.MIN, uint(12890));

  public static final NodeId MdnsDiscoveryConfiguration = new NodeId(UShort.MIN, uint(12891));

  public static final NodeId DiscoveryConfiguration_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(12892));

  public static final NodeId MdnsDiscoveryConfiguration_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(12893));

  public static final NodeId OpcUa_XmlSchema_DiscoveryConfiguration =
      new NodeId(UShort.MIN, uint(12894));

  public static final NodeId OpcUa_XmlSchema_DiscoveryConfiguration_DataTypeVersion =
      new NodeId(UShort.MIN, uint(12895));

  public static final NodeId OpcUa_XmlSchema_DiscoveryConfiguration_DictionaryFragment =
      new NodeId(UShort.MIN, uint(12896));

  public static final NodeId OpcUa_XmlSchema_MdnsDiscoveryConfiguration =
      new NodeId(UShort.MIN, uint(12897));

  public static final NodeId OpcUa_XmlSchema_MdnsDiscoveryConfiguration_DataTypeVersion =
      new NodeId(UShort.MIN, uint(12898));

  public static final NodeId OpcUa_XmlSchema_MdnsDiscoveryConfiguration_DictionaryFragment =
      new NodeId(UShort.MIN, uint(12899));

  public static final NodeId DiscoveryConfiguration_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(12900));

  public static final NodeId MdnsDiscoveryConfiguration_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(12901));

  public static final NodeId OpcUa_BinarySchema_DiscoveryConfiguration =
      new NodeId(UShort.MIN, uint(12902));

  public static final NodeId OpcUa_BinarySchema_DiscoveryConfiguration_DataTypeVersion =
      new NodeId(UShort.MIN, uint(12903));

  public static final NodeId OpcUa_BinarySchema_DiscoveryConfiguration_DictionaryFragment =
      new NodeId(UShort.MIN, uint(12904));

  public static final NodeId OpcUa_BinarySchema_MdnsDiscoveryConfiguration =
      new NodeId(UShort.MIN, uint(12905));

  public static final NodeId OpcUa_BinarySchema_MdnsDiscoveryConfiguration_DataTypeVersion =
      new NodeId(UShort.MIN, uint(12906));

  public static final NodeId OpcUa_BinarySchema_MdnsDiscoveryConfiguration_DictionaryFragment =
      new NodeId(UShort.MIN, uint(12907));

  public static final NodeId MaxByteStringLength = new NodeId(UShort.MIN, uint(12908));

  public static final NodeId ServerType_ServerCapabilities_MaxByteStringLength =
      new NodeId(UShort.MIN, uint(12909));

  public static final NodeId ServerCapabilitiesType_MaxByteStringLength =
      new NodeId(UShort.MIN, uint(12910));

  public static final NodeId Server_ServerCapabilities_MaxByteStringLength =
      new NodeId(UShort.MIN, uint(12911));

  public static final NodeId ConditionType_ConditionRefresh2 = new NodeId(UShort.MIN, uint(12912));

  public static final NodeId ConditionType_ConditionRefresh2_InputArguments =
      new NodeId(UShort.MIN, uint(12913));

  public static final NodeId ConditionRefresh2MethodType = new NodeId(UShort.MIN, uint(12914));

  public static final NodeId ConditionRefresh2MethodType_InputArguments =
      new NodeId(UShort.MIN, uint(12915));

  public static final NodeId DialogConditionType_ConditionRefresh2 =
      new NodeId(UShort.MIN, uint(12916));

  public static final NodeId DialogConditionType_ConditionRefresh2_InputArguments =
      new NodeId(UShort.MIN, uint(12917));

  public static final NodeId AcknowledgeableConditionType_ConditionRefresh2 =
      new NodeId(UShort.MIN, uint(12918));

  public static final NodeId AcknowledgeableConditionType_ConditionRefresh2_InputArguments =
      new NodeId(UShort.MIN, uint(12919));

  public static final NodeId AlarmConditionType_ConditionRefresh2 =
      new NodeId(UShort.MIN, uint(12984));

  public static final NodeId AlarmConditionType_ConditionRefresh2_InputArguments =
      new NodeId(UShort.MIN, uint(12985));

  public static final NodeId LimitAlarmType_ConditionRefresh2 = new NodeId(UShort.MIN, uint(12986));

  public static final NodeId LimitAlarmType_ConditionRefresh2_InputArguments =
      new NodeId(UShort.MIN, uint(12987));

  public static final NodeId ExclusiveLimitAlarmType_ConditionRefresh2 =
      new NodeId(UShort.MIN, uint(12988));

  public static final NodeId ExclusiveLimitAlarmType_ConditionRefresh2_InputArguments =
      new NodeId(UShort.MIN, uint(12989));

  public static final NodeId NonExclusiveLimitAlarmType_ConditionRefresh2 =
      new NodeId(UShort.MIN, uint(12990));

  public static final NodeId NonExclusiveLimitAlarmType_ConditionRefresh2_InputArguments =
      new NodeId(UShort.MIN, uint(12991));

  public static final NodeId NonExclusiveLevelAlarmType_ConditionRefresh2 =
      new NodeId(UShort.MIN, uint(12992));

  public static final NodeId NonExclusiveLevelAlarmType_ConditionRefresh2_InputArguments =
      new NodeId(UShort.MIN, uint(12993));

  public static final NodeId ExclusiveLevelAlarmType_ConditionRefresh2 =
      new NodeId(UShort.MIN, uint(12994));

  public static final NodeId ExclusiveLevelAlarmType_ConditionRefresh2_InputArguments =
      new NodeId(UShort.MIN, uint(12995));

  public static final NodeId NonExclusiveDeviationAlarmType_ConditionRefresh2 =
      new NodeId(UShort.MIN, uint(12996));

  public static final NodeId NonExclusiveDeviationAlarmType_ConditionRefresh2_InputArguments =
      new NodeId(UShort.MIN, uint(12997));

  public static final NodeId ExclusiveDeviationAlarmType_ConditionRefresh2 =
      new NodeId(UShort.MIN, uint(12998));

  public static final NodeId ExclusiveDeviationAlarmType_ConditionRefresh2_InputArguments =
      new NodeId(UShort.MIN, uint(12999));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_ConditionRefresh2 =
      new NodeId(UShort.MIN, uint(13000));

  public static final NodeId NonExclusiveRateOfChangeAlarmType_ConditionRefresh2_InputArguments =
      new NodeId(UShort.MIN, uint(13001));

  public static final NodeId ExclusiveRateOfChangeAlarmType_ConditionRefresh2 =
      new NodeId(UShort.MIN, uint(13002));

  public static final NodeId ExclusiveRateOfChangeAlarmType_ConditionRefresh2_InputArguments =
      new NodeId(UShort.MIN, uint(13003));

  public static final NodeId DiscreteAlarmType_ConditionRefresh2 =
      new NodeId(UShort.MIN, uint(13004));

  public static final NodeId DiscreteAlarmType_ConditionRefresh2_InputArguments =
      new NodeId(UShort.MIN, uint(13005));

  public static final NodeId OffNormalAlarmType_ConditionRefresh2 =
      new NodeId(UShort.MIN, uint(13006));

  public static final NodeId OffNormalAlarmType_ConditionRefresh2_InputArguments =
      new NodeId(UShort.MIN, uint(13007));

  public static final NodeId SystemOffNormalAlarmType_ConditionRefresh2 =
      new NodeId(UShort.MIN, uint(13008));

  public static final NodeId SystemOffNormalAlarmType_ConditionRefresh2_InputArguments =
      new NodeId(UShort.MIN, uint(13009));

  public static final NodeId TripAlarmType_ConditionRefresh2 = new NodeId(UShort.MIN, uint(13010));

  public static final NodeId TripAlarmType_ConditionRefresh2_InputArguments =
      new NodeId(UShort.MIN, uint(13011));

  public static final NodeId CertificateExpirationAlarmType = new NodeId(UShort.MIN, uint(13225));

  public static final NodeId CertificateExpirationAlarmType_EventId =
      new NodeId(UShort.MIN, uint(13226));

  public static final NodeId CertificateExpirationAlarmType_EventType =
      new NodeId(UShort.MIN, uint(13227));

  public static final NodeId CertificateExpirationAlarmType_SourceNode =
      new NodeId(UShort.MIN, uint(13228));

  public static final NodeId CertificateExpirationAlarmType_SourceName =
      new NodeId(UShort.MIN, uint(13229));

  public static final NodeId CertificateExpirationAlarmType_Time =
      new NodeId(UShort.MIN, uint(13230));

  public static final NodeId CertificateExpirationAlarmType_ReceiveTime =
      new NodeId(UShort.MIN, uint(13231));

  public static final NodeId CertificateExpirationAlarmType_LocalTime =
      new NodeId(UShort.MIN, uint(13232));

  public static final NodeId CertificateExpirationAlarmType_Message =
      new NodeId(UShort.MIN, uint(13233));

  public static final NodeId CertificateExpirationAlarmType_Severity =
      new NodeId(UShort.MIN, uint(13234));

  public static final NodeId CertificateExpirationAlarmType_ConditionClassId =
      new NodeId(UShort.MIN, uint(13235));

  public static final NodeId CertificateExpirationAlarmType_ConditionClassName =
      new NodeId(UShort.MIN, uint(13236));

  public static final NodeId CertificateExpirationAlarmType_ConditionName =
      new NodeId(UShort.MIN, uint(13237));

  public static final NodeId CertificateExpirationAlarmType_BranchId =
      new NodeId(UShort.MIN, uint(13238));

  public static final NodeId CertificateExpirationAlarmType_Retain =
      new NodeId(UShort.MIN, uint(13239));

  public static final NodeId CertificateExpirationAlarmType_EnabledState =
      new NodeId(UShort.MIN, uint(13240));

  public static final NodeId CertificateExpirationAlarmType_EnabledState_Id =
      new NodeId(UShort.MIN, uint(13241));

  public static final NodeId CertificateExpirationAlarmType_EnabledState_Name =
      new NodeId(UShort.MIN, uint(13242));

  public static final NodeId CertificateExpirationAlarmType_EnabledState_Number =
      new NodeId(UShort.MIN, uint(13243));

  public static final NodeId CertificateExpirationAlarmType_EnabledState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(13244));

  public static final NodeId CertificateExpirationAlarmType_EnabledState_TransitionTime =
      new NodeId(UShort.MIN, uint(13245));

  public static final NodeId CertificateExpirationAlarmType_EnabledState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(13246));

  public static final NodeId CertificateExpirationAlarmType_EnabledState_TrueState =
      new NodeId(UShort.MIN, uint(13247));

  public static final NodeId CertificateExpirationAlarmType_EnabledState_FalseState =
      new NodeId(UShort.MIN, uint(13248));

  public static final NodeId CertificateExpirationAlarmType_Quality =
      new NodeId(UShort.MIN, uint(13249));

  public static final NodeId CertificateExpirationAlarmType_Quality_SourceTimestamp =
      new NodeId(UShort.MIN, uint(13250));

  public static final NodeId CertificateExpirationAlarmType_LastSeverity =
      new NodeId(UShort.MIN, uint(13251));

  public static final NodeId CertificateExpirationAlarmType_LastSeverity_SourceTimestamp =
      new NodeId(UShort.MIN, uint(13252));

  public static final NodeId CertificateExpirationAlarmType_Comment =
      new NodeId(UShort.MIN, uint(13253));

  public static final NodeId CertificateExpirationAlarmType_Comment_SourceTimestamp =
      new NodeId(UShort.MIN, uint(13254));

  public static final NodeId CertificateExpirationAlarmType_ClientUserId =
      new NodeId(UShort.MIN, uint(13255));

  public static final NodeId CertificateExpirationAlarmType_Disable =
      new NodeId(UShort.MIN, uint(13256));

  public static final NodeId CertificateExpirationAlarmType_Enable =
      new NodeId(UShort.MIN, uint(13257));

  public static final NodeId CertificateExpirationAlarmType_AddComment =
      new NodeId(UShort.MIN, uint(13258));

  public static final NodeId CertificateExpirationAlarmType_AddComment_InputArguments =
      new NodeId(UShort.MIN, uint(13259));

  public static final NodeId CertificateExpirationAlarmType_ConditionRefresh =
      new NodeId(UShort.MIN, uint(13260));

  public static final NodeId CertificateExpirationAlarmType_ConditionRefresh_InputArguments =
      new NodeId(UShort.MIN, uint(13261));

  public static final NodeId CertificateExpirationAlarmType_ConditionRefresh2 =
      new NodeId(UShort.MIN, uint(13262));

  public static final NodeId CertificateExpirationAlarmType_ConditionRefresh2_InputArguments =
      new NodeId(UShort.MIN, uint(13263));

  public static final NodeId CertificateExpirationAlarmType_AckedState =
      new NodeId(UShort.MIN, uint(13264));

  public static final NodeId CertificateExpirationAlarmType_AckedState_Id =
      new NodeId(UShort.MIN, uint(13265));

  public static final NodeId CertificateExpirationAlarmType_AckedState_Name =
      new NodeId(UShort.MIN, uint(13266));

  public static final NodeId CertificateExpirationAlarmType_AckedState_Number =
      new NodeId(UShort.MIN, uint(13267));

  public static final NodeId CertificateExpirationAlarmType_AckedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(13268));

  public static final NodeId CertificateExpirationAlarmType_AckedState_TransitionTime =
      new NodeId(UShort.MIN, uint(13269));

  public static final NodeId CertificateExpirationAlarmType_AckedState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(13270));

  public static final NodeId CertificateExpirationAlarmType_AckedState_TrueState =
      new NodeId(UShort.MIN, uint(13271));

  public static final NodeId CertificateExpirationAlarmType_AckedState_FalseState =
      new NodeId(UShort.MIN, uint(13272));

  public static final NodeId CertificateExpirationAlarmType_ConfirmedState =
      new NodeId(UShort.MIN, uint(13273));

  public static final NodeId CertificateExpirationAlarmType_ConfirmedState_Id =
      new NodeId(UShort.MIN, uint(13274));

  public static final NodeId CertificateExpirationAlarmType_ConfirmedState_Name =
      new NodeId(UShort.MIN, uint(13275));

  public static final NodeId CertificateExpirationAlarmType_ConfirmedState_Number =
      new NodeId(UShort.MIN, uint(13276));

  public static final NodeId CertificateExpirationAlarmType_ConfirmedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(13277));

  public static final NodeId CertificateExpirationAlarmType_ConfirmedState_TransitionTime =
      new NodeId(UShort.MIN, uint(13278));

  public static final NodeId CertificateExpirationAlarmType_ConfirmedState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(13279));

  public static final NodeId CertificateExpirationAlarmType_ConfirmedState_TrueState =
      new NodeId(UShort.MIN, uint(13280));

  public static final NodeId CertificateExpirationAlarmType_ConfirmedState_FalseState =
      new NodeId(UShort.MIN, uint(13281));

  public static final NodeId CertificateExpirationAlarmType_Acknowledge =
      new NodeId(UShort.MIN, uint(13282));

  public static final NodeId CertificateExpirationAlarmType_Acknowledge_InputArguments =
      new NodeId(UShort.MIN, uint(13283));

  public static final NodeId CertificateExpirationAlarmType_Confirm =
      new NodeId(UShort.MIN, uint(13284));

  public static final NodeId CertificateExpirationAlarmType_Confirm_InputArguments =
      new NodeId(UShort.MIN, uint(13285));

  public static final NodeId CertificateExpirationAlarmType_ActiveState =
      new NodeId(UShort.MIN, uint(13286));

  public static final NodeId CertificateExpirationAlarmType_ActiveState_Id =
      new NodeId(UShort.MIN, uint(13287));

  public static final NodeId CertificateExpirationAlarmType_ActiveState_Name =
      new NodeId(UShort.MIN, uint(13288));

  public static final NodeId CertificateExpirationAlarmType_ActiveState_Number =
      new NodeId(UShort.MIN, uint(13289));

  public static final NodeId CertificateExpirationAlarmType_ActiveState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(13290));

  public static final NodeId CertificateExpirationAlarmType_ActiveState_TransitionTime =
      new NodeId(UShort.MIN, uint(13291));

  public static final NodeId CertificateExpirationAlarmType_ActiveState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(13292));

  public static final NodeId CertificateExpirationAlarmType_ActiveState_TrueState =
      new NodeId(UShort.MIN, uint(13293));

  public static final NodeId CertificateExpirationAlarmType_ActiveState_FalseState =
      new NodeId(UShort.MIN, uint(13294));

  public static final NodeId CertificateExpirationAlarmType_InputNode =
      new NodeId(UShort.MIN, uint(13295));

  public static final NodeId CertificateExpirationAlarmType_SuppressedState =
      new NodeId(UShort.MIN, uint(13296));

  public static final NodeId CertificateExpirationAlarmType_SuppressedState_Id =
      new NodeId(UShort.MIN, uint(13297));

  public static final NodeId CertificateExpirationAlarmType_SuppressedState_Name =
      new NodeId(UShort.MIN, uint(13298));

  public static final NodeId CertificateExpirationAlarmType_SuppressedState_Number =
      new NodeId(UShort.MIN, uint(13299));

  public static final NodeId CertificateExpirationAlarmType_SuppressedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(13300));

  public static final NodeId CertificateExpirationAlarmType_SuppressedState_TransitionTime =
      new NodeId(UShort.MIN, uint(13301));

  public static final NodeId
      CertificateExpirationAlarmType_SuppressedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(13302));

  public static final NodeId CertificateExpirationAlarmType_SuppressedState_TrueState =
      new NodeId(UShort.MIN, uint(13303));

  public static final NodeId CertificateExpirationAlarmType_SuppressedState_FalseState =
      new NodeId(UShort.MIN, uint(13304));

  public static final NodeId CertificateExpirationAlarmType_ShelvingState =
      new NodeId(UShort.MIN, uint(13305));

  public static final NodeId CertificateExpirationAlarmType_ShelvingState_CurrentState =
      new NodeId(UShort.MIN, uint(13306));

  public static final NodeId CertificateExpirationAlarmType_ShelvingState_CurrentState_Id =
      new NodeId(UShort.MIN, uint(13307));

  public static final NodeId CertificateExpirationAlarmType_ShelvingState_CurrentState_Name =
      new NodeId(UShort.MIN, uint(13308));

  public static final NodeId CertificateExpirationAlarmType_ShelvingState_CurrentState_Number =
      new NodeId(UShort.MIN, uint(13309));

  public static final NodeId
      CertificateExpirationAlarmType_ShelvingState_CurrentState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(13310));

  public static final NodeId CertificateExpirationAlarmType_ShelvingState_LastTransition =
      new NodeId(UShort.MIN, uint(13311));

  public static final NodeId CertificateExpirationAlarmType_ShelvingState_LastTransition_Id =
      new NodeId(UShort.MIN, uint(13312));

  public static final NodeId CertificateExpirationAlarmType_ShelvingState_LastTransition_Name =
      new NodeId(UShort.MIN, uint(13313));

  public static final NodeId CertificateExpirationAlarmType_ShelvingState_LastTransition_Number =
      new NodeId(UShort.MIN, uint(13314));

  public static final NodeId
      CertificateExpirationAlarmType_ShelvingState_LastTransition_TransitionTime =
          new NodeId(UShort.MIN, uint(13315));

  public static final NodeId
      CertificateExpirationAlarmType_ShelvingState_LastTransition_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(13316));

  public static final NodeId CertificateExpirationAlarmType_ShelvingState_UnshelveTime =
      new NodeId(UShort.MIN, uint(13317));

  public static final NodeId CertificateExpirationAlarmType_ShelvingState_Unshelve =
      new NodeId(UShort.MIN, uint(13318));

  public static final NodeId CertificateExpirationAlarmType_ShelvingState_OneShotShelve =
      new NodeId(UShort.MIN, uint(13319));

  public static final NodeId CertificateExpirationAlarmType_ShelvingState_TimedShelve =
      new NodeId(UShort.MIN, uint(13320));

  public static final NodeId
      CertificateExpirationAlarmType_ShelvingState_TimedShelve_InputArguments =
          new NodeId(UShort.MIN, uint(13321));

  public static final NodeId CertificateExpirationAlarmType_SuppressedOrShelved =
      new NodeId(UShort.MIN, uint(13322));

  public static final NodeId CertificateExpirationAlarmType_MaxTimeShelved =
      new NodeId(UShort.MIN, uint(13323));

  public static final NodeId CertificateExpirationAlarmType_NormalState =
      new NodeId(UShort.MIN, uint(13324));

  public static final NodeId CertificateExpirationAlarmType_ExpirationDate =
      new NodeId(UShort.MIN, uint(13325));

  public static final NodeId CertificateExpirationAlarmType_CertificateType =
      new NodeId(UShort.MIN, uint(13326));

  public static final NodeId CertificateExpirationAlarmType_Certificate =
      new NodeId(UShort.MIN, uint(13327));

  public static final NodeId FileType_MimeType = new NodeId(UShort.MIN, uint(13341));

  public static final NodeId CreateDirectoryMethodType = new NodeId(UShort.MIN, uint(13342));

  public static final NodeId CreateDirectoryMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(13343));

  public static final NodeId CreateDirectoryMethodType_OutputArguments =
      new NodeId(UShort.MIN, uint(13344));

  public static final NodeId CreateFileMethodType = new NodeId(UShort.MIN, uint(13345));

  public static final NodeId CreateFileMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(13346));

  public static final NodeId CreateFileMethodType_OutputArguments =
      new NodeId(UShort.MIN, uint(13347));

  public static final NodeId DeleteFileMethodType = new NodeId(UShort.MIN, uint(13348));

  public static final NodeId DeleteFileMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(13349));

  public static final NodeId MoveOrCopyMethodType = new NodeId(UShort.MIN, uint(13350));

  public static final NodeId MoveOrCopyMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(13351));

  public static final NodeId MoveOrCopyMethodType_OutputArguments =
      new NodeId(UShort.MIN, uint(13352));

  public static final NodeId FileDirectoryType = new NodeId(UShort.MIN, uint(13353));

  public static final NodeId FileDirectoryType_FileDirectoryName_Placeholder =
      new NodeId(UShort.MIN, uint(13354));

  public static final NodeId FileDirectoryType_FileDirectoryName_Placeholder_CreateDirectory =
      new NodeId(UShort.MIN, uint(13355));

  public static final NodeId
      FileDirectoryType_FileDirectoryName_Placeholder_CreateDirectory_InputArguments =
          new NodeId(UShort.MIN, uint(13356));

  public static final NodeId
      FileDirectoryType_FileDirectoryName_Placeholder_CreateDirectory_OutputArguments =
          new NodeId(UShort.MIN, uint(13357));

  public static final NodeId FileDirectoryType_FileDirectoryName_Placeholder_CreateFile =
      new NodeId(UShort.MIN, uint(13358));

  public static final NodeId
      FileDirectoryType_FileDirectoryName_Placeholder_CreateFile_InputArguments =
          new NodeId(UShort.MIN, uint(13359));

  public static final NodeId
      FileDirectoryType_FileDirectoryName_Placeholder_CreateFile_OutputArguments =
          new NodeId(UShort.MIN, uint(13360));

  public static final NodeId FileDirectoryType_FileDirectoryName_Placeholder_MoveOrCopy =
      new NodeId(UShort.MIN, uint(13363));

  public static final NodeId
      FileDirectoryType_FileDirectoryName_Placeholder_MoveOrCopy_InputArguments =
          new NodeId(UShort.MIN, uint(13364));

  public static final NodeId
      FileDirectoryType_FileDirectoryName_Placeholder_MoveOrCopy_OutputArguments =
          new NodeId(UShort.MIN, uint(13365));

  public static final NodeId FileDirectoryType_FileName_Placeholder =
      new NodeId(UShort.MIN, uint(13366));

  public static final NodeId FileDirectoryType_FileName_Placeholder_Size =
      new NodeId(UShort.MIN, uint(13367));

  public static final NodeId FileDirectoryType_FileName_Placeholder_Writable =
      new NodeId(UShort.MIN, uint(13368));

  public static final NodeId FileDirectoryType_FileName_Placeholder_UserWritable =
      new NodeId(UShort.MIN, uint(13369));

  public static final NodeId FileDirectoryType_FileName_Placeholder_OpenCount =
      new NodeId(UShort.MIN, uint(13370));

  public static final NodeId FileDirectoryType_FileName_Placeholder_MimeType =
      new NodeId(UShort.MIN, uint(13371));

  public static final NodeId FileDirectoryType_FileName_Placeholder_Open =
      new NodeId(UShort.MIN, uint(13372));

  public static final NodeId FileDirectoryType_FileName_Placeholder_Open_InputArguments =
      new NodeId(UShort.MIN, uint(13373));

  public static final NodeId FileDirectoryType_FileName_Placeholder_Open_OutputArguments =
      new NodeId(UShort.MIN, uint(13374));

  public static final NodeId FileDirectoryType_FileName_Placeholder_Close =
      new NodeId(UShort.MIN, uint(13375));

  public static final NodeId FileDirectoryType_FileName_Placeholder_Close_InputArguments =
      new NodeId(UShort.MIN, uint(13376));

  public static final NodeId FileDirectoryType_FileName_Placeholder_Read =
      new NodeId(UShort.MIN, uint(13377));

  public static final NodeId FileDirectoryType_FileName_Placeholder_Read_InputArguments =
      new NodeId(UShort.MIN, uint(13378));

  public static final NodeId FileDirectoryType_FileName_Placeholder_Read_OutputArguments =
      new NodeId(UShort.MIN, uint(13379));

  public static final NodeId FileDirectoryType_FileName_Placeholder_Write =
      new NodeId(UShort.MIN, uint(13380));

  public static final NodeId FileDirectoryType_FileName_Placeholder_Write_InputArguments =
      new NodeId(UShort.MIN, uint(13381));

  public static final NodeId FileDirectoryType_FileName_Placeholder_GetPosition =
      new NodeId(UShort.MIN, uint(13382));

  public static final NodeId FileDirectoryType_FileName_Placeholder_GetPosition_InputArguments =
      new NodeId(UShort.MIN, uint(13383));

  public static final NodeId FileDirectoryType_FileName_Placeholder_GetPosition_OutputArguments =
      new NodeId(UShort.MIN, uint(13384));

  public static final NodeId FileDirectoryType_FileName_Placeholder_SetPosition =
      new NodeId(UShort.MIN, uint(13385));

  public static final NodeId FileDirectoryType_FileName_Placeholder_SetPosition_InputArguments =
      new NodeId(UShort.MIN, uint(13386));

  public static final NodeId FileDirectoryType_CreateDirectory =
      new NodeId(UShort.MIN, uint(13387));

  public static final NodeId FileDirectoryType_CreateDirectory_InputArguments =
      new NodeId(UShort.MIN, uint(13388));

  public static final NodeId FileDirectoryType_CreateDirectory_OutputArguments =
      new NodeId(UShort.MIN, uint(13389));

  public static final NodeId FileDirectoryType_CreateFile = new NodeId(UShort.MIN, uint(13390));

  public static final NodeId FileDirectoryType_CreateFile_InputArguments =
      new NodeId(UShort.MIN, uint(13391));

  public static final NodeId FileDirectoryType_CreateFile_OutputArguments =
      new NodeId(UShort.MIN, uint(13392));

  public static final NodeId FileDirectoryType_DeleteFileSystemObject =
      new NodeId(UShort.MIN, uint(13393));

  public static final NodeId FileDirectoryType_DeleteFileSystemObject_InputArguments =
      new NodeId(UShort.MIN, uint(13394));

  public static final NodeId FileDirectoryType_MoveOrCopy = new NodeId(UShort.MIN, uint(13395));

  public static final NodeId FileDirectoryType_MoveOrCopy_InputArguments =
      new NodeId(UShort.MIN, uint(13396));

  public static final NodeId FileDirectoryType_MoveOrCopy_OutputArguments =
      new NodeId(UShort.MIN, uint(13397));

  public static final NodeId AddressSpaceFileType_MimeType = new NodeId(UShort.MIN, uint(13398));

  public static final NodeId NamespaceMetadataType_NamespaceFile_MimeType =
      new NodeId(UShort.MIN, uint(13399));

  public static final NodeId NamespacesType_NamespaceIdentifier_Placeholder_NamespaceFile_MimeType =
      new NodeId(UShort.MIN, uint(13400));

  public static final NodeId TrustListType_MimeType = new NodeId(UShort.MIN, uint(13403));

  public static final NodeId CertificateGroupType_TrustList = new NodeId(UShort.MIN, uint(13599));

  public static final NodeId CertificateGroupType_TrustList_Size =
      new NodeId(UShort.MIN, uint(13600));

  public static final NodeId CertificateGroupType_TrustList_Writable =
      new NodeId(UShort.MIN, uint(13601));

  public static final NodeId CertificateGroupType_TrustList_UserWritable =
      new NodeId(UShort.MIN, uint(13602));

  public static final NodeId CertificateGroupType_TrustList_OpenCount =
      new NodeId(UShort.MIN, uint(13603));

  public static final NodeId CertificateGroupType_TrustList_MimeType =
      new NodeId(UShort.MIN, uint(13604));

  public static final NodeId CertificateGroupType_TrustList_Open =
      new NodeId(UShort.MIN, uint(13605));

  public static final NodeId CertificateGroupType_TrustList_Open_InputArguments =
      new NodeId(UShort.MIN, uint(13606));

  public static final NodeId CertificateGroupType_TrustList_Open_OutputArguments =
      new NodeId(UShort.MIN, uint(13607));

  public static final NodeId CertificateGroupType_TrustList_Close =
      new NodeId(UShort.MIN, uint(13608));

  public static final NodeId CertificateGroupType_TrustList_Close_InputArguments =
      new NodeId(UShort.MIN, uint(13609));

  public static final NodeId CertificateGroupType_TrustList_Read =
      new NodeId(UShort.MIN, uint(13610));

  public static final NodeId CertificateGroupType_TrustList_Read_InputArguments =
      new NodeId(UShort.MIN, uint(13611));

  public static final NodeId CertificateGroupType_TrustList_Read_OutputArguments =
      new NodeId(UShort.MIN, uint(13612));

  public static final NodeId CertificateGroupType_TrustList_Write =
      new NodeId(UShort.MIN, uint(13613));

  public static final NodeId CertificateGroupType_TrustList_Write_InputArguments =
      new NodeId(UShort.MIN, uint(13614));

  public static final NodeId CertificateGroupType_TrustList_GetPosition =
      new NodeId(UShort.MIN, uint(13615));

  public static final NodeId CertificateGroupType_TrustList_GetPosition_InputArguments =
      new NodeId(UShort.MIN, uint(13616));

  public static final NodeId CertificateGroupType_TrustList_GetPosition_OutputArguments =
      new NodeId(UShort.MIN, uint(13617));

  public static final NodeId CertificateGroupType_TrustList_SetPosition =
      new NodeId(UShort.MIN, uint(13618));

  public static final NodeId CertificateGroupType_TrustList_SetPosition_InputArguments =
      new NodeId(UShort.MIN, uint(13619));

  public static final NodeId CertificateGroupType_TrustList_LastUpdateTime =
      new NodeId(UShort.MIN, uint(13620));

  public static final NodeId CertificateGroupType_TrustList_OpenWithMasks =
      new NodeId(UShort.MIN, uint(13621));

  public static final NodeId CertificateGroupType_TrustList_OpenWithMasks_InputArguments =
      new NodeId(UShort.MIN, uint(13622));

  public static final NodeId CertificateGroupType_TrustList_OpenWithMasks_OutputArguments =
      new NodeId(UShort.MIN, uint(13623));

  public static final NodeId CertificateGroupType_TrustList_CloseAndUpdate =
      new NodeId(UShort.MIN, uint(13624));

  public static final NodeId CertificateGroupType_TrustList_CloseAndUpdate_InputArguments =
      new NodeId(UShort.MIN, uint(13625));

  public static final NodeId CertificateGroupType_TrustList_CloseAndUpdate_OutputArguments =
      new NodeId(UShort.MIN, uint(13626));

  public static final NodeId CertificateGroupType_TrustList_AddCertificate =
      new NodeId(UShort.MIN, uint(13627));

  public static final NodeId CertificateGroupType_TrustList_AddCertificate_InputArguments =
      new NodeId(UShort.MIN, uint(13628));

  public static final NodeId CertificateGroupType_TrustList_RemoveCertificate =
      new NodeId(UShort.MIN, uint(13629));

  public static final NodeId CertificateGroupType_TrustList_RemoveCertificate_InputArguments =
      new NodeId(UShort.MIN, uint(13630));

  public static final NodeId CertificateGroupType_CertificateTypes =
      new NodeId(UShort.MIN, uint(13631));

  public static final NodeId CertificateUpdatedAuditEventType_CertificateGroup =
      new NodeId(UShort.MIN, uint(13735));

  public static final NodeId CertificateUpdatedAuditEventType_CertificateType =
      new NodeId(UShort.MIN, uint(13736));

  public static final NodeId ServerConfiguration_UpdateCertificate =
      new NodeId(UShort.MIN, uint(13737));

  public static final NodeId ServerConfiguration_UpdateCertificate_InputArguments =
      new NodeId(UShort.MIN, uint(13738));

  public static final NodeId ServerConfiguration_UpdateCertificate_OutputArguments =
      new NodeId(UShort.MIN, uint(13739));

  public static final NodeId CertificateGroupFolderType = new NodeId(UShort.MIN, uint(13813));

  public static final NodeId CertificateGroupFolderType_DefaultApplicationGroup =
      new NodeId(UShort.MIN, uint(13814));

  public static final NodeId CertificateGroupFolderType_DefaultApplicationGroup_TrustList =
      new NodeId(UShort.MIN, uint(13815));

  public static final NodeId CertificateGroupFolderType_DefaultApplicationGroup_TrustList_Size =
      new NodeId(UShort.MIN, uint(13816));

  public static final NodeId CertificateGroupFolderType_DefaultApplicationGroup_TrustList_Writable =
      new NodeId(UShort.MIN, uint(13817));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustList_UserWritable =
          new NodeId(UShort.MIN, uint(13818));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustList_OpenCount =
          new NodeId(UShort.MIN, uint(13819));

  public static final NodeId CertificateGroupFolderType_DefaultApplicationGroup_TrustList_MimeType =
      new NodeId(UShort.MIN, uint(13820));

  public static final NodeId CertificateGroupFolderType_DefaultApplicationGroup_TrustList_Open =
      new NodeId(UShort.MIN, uint(13821));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustList_Open_InputArguments =
          new NodeId(UShort.MIN, uint(13822));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustList_Open_OutputArguments =
          new NodeId(UShort.MIN, uint(13823));

  public static final NodeId CertificateGroupFolderType_DefaultApplicationGroup_TrustList_Close =
      new NodeId(UShort.MIN, uint(13824));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustList_Close_InputArguments =
          new NodeId(UShort.MIN, uint(13825));

  public static final NodeId CertificateGroupFolderType_DefaultApplicationGroup_TrustList_Read =
      new NodeId(UShort.MIN, uint(13826));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustList_Read_InputArguments =
          new NodeId(UShort.MIN, uint(13827));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustList_Read_OutputArguments =
          new NodeId(UShort.MIN, uint(13828));

  public static final NodeId CertificateGroupFolderType_DefaultApplicationGroup_TrustList_Write =
      new NodeId(UShort.MIN, uint(13829));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustList_Write_InputArguments =
          new NodeId(UShort.MIN, uint(13830));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustList_GetPosition =
          new NodeId(UShort.MIN, uint(13831));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustList_GetPosition_InputArguments =
          new NodeId(UShort.MIN, uint(13832));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustList_GetPosition_OutputArguments =
          new NodeId(UShort.MIN, uint(13833));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustList_SetPosition =
          new NodeId(UShort.MIN, uint(13834));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustList_SetPosition_InputArguments =
          new NodeId(UShort.MIN, uint(13835));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustList_LastUpdateTime =
          new NodeId(UShort.MIN, uint(13836));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustList_OpenWithMasks =
          new NodeId(UShort.MIN, uint(13837));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustList_OpenWithMasks_InputArguments =
          new NodeId(UShort.MIN, uint(13838));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustList_OpenWithMasks_OutputArguments =
          new NodeId(UShort.MIN, uint(13839));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustList_CloseAndUpdate =
          new NodeId(UShort.MIN, uint(13840));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustList_CloseAndUpdate_InputArguments =
          new NodeId(UShort.MIN, uint(13841));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustList_CloseAndUpdate_OutputArguments =
          new NodeId(UShort.MIN, uint(13842));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustList_AddCertificate =
          new NodeId(UShort.MIN, uint(13843));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustList_AddCertificate_InputArguments =
          new NodeId(UShort.MIN, uint(13844));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustList_RemoveCertificate =
          new NodeId(UShort.MIN, uint(13845));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustList_RemoveCertificate_InputArguments =
          new NodeId(UShort.MIN, uint(13846));

  public static final NodeId CertificateGroupFolderType_DefaultApplicationGroup_CertificateTypes =
      new NodeId(UShort.MIN, uint(13847));

  public static final NodeId CertificateGroupFolderType_DefaultHttpsGroup =
      new NodeId(UShort.MIN, uint(13848));

  public static final NodeId CertificateGroupFolderType_DefaultHttpsGroup_TrustList =
      new NodeId(UShort.MIN, uint(13849));

  public static final NodeId CertificateGroupFolderType_DefaultHttpsGroup_TrustList_Size =
      new NodeId(UShort.MIN, uint(13850));

  public static final NodeId CertificateGroupFolderType_DefaultHttpsGroup_TrustList_Writable =
      new NodeId(UShort.MIN, uint(13851));

  public static final NodeId CertificateGroupFolderType_DefaultHttpsGroup_TrustList_UserWritable =
      new NodeId(UShort.MIN, uint(13852));

  public static final NodeId CertificateGroupFolderType_DefaultHttpsGroup_TrustList_OpenCount =
      new NodeId(UShort.MIN, uint(13853));

  public static final NodeId CertificateGroupFolderType_DefaultHttpsGroup_TrustList_MimeType =
      new NodeId(UShort.MIN, uint(13854));

  public static final NodeId CertificateGroupFolderType_DefaultHttpsGroup_TrustList_Open =
      new NodeId(UShort.MIN, uint(13855));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustList_Open_InputArguments =
          new NodeId(UShort.MIN, uint(13856));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustList_Open_OutputArguments =
          new NodeId(UShort.MIN, uint(13857));

  public static final NodeId CertificateGroupFolderType_DefaultHttpsGroup_TrustList_Close =
      new NodeId(UShort.MIN, uint(13858));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustList_Close_InputArguments =
          new NodeId(UShort.MIN, uint(13859));

  public static final NodeId CertificateGroupFolderType_DefaultHttpsGroup_TrustList_Read =
      new NodeId(UShort.MIN, uint(13860));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustList_Read_InputArguments =
          new NodeId(UShort.MIN, uint(13861));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustList_Read_OutputArguments =
          new NodeId(UShort.MIN, uint(13862));

  public static final NodeId CertificateGroupFolderType_DefaultHttpsGroup_TrustList_Write =
      new NodeId(UShort.MIN, uint(13863));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustList_Write_InputArguments =
          new NodeId(UShort.MIN, uint(13864));

  public static final NodeId CertificateGroupFolderType_DefaultHttpsGroup_TrustList_GetPosition =
      new NodeId(UShort.MIN, uint(13865));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustList_GetPosition_InputArguments =
          new NodeId(UShort.MIN, uint(13866));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustList_GetPosition_OutputArguments =
          new NodeId(UShort.MIN, uint(13867));

  public static final NodeId CertificateGroupFolderType_DefaultHttpsGroup_TrustList_SetPosition =
      new NodeId(UShort.MIN, uint(13868));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustList_SetPosition_InputArguments =
          new NodeId(UShort.MIN, uint(13869));

  public static final NodeId CertificateGroupFolderType_DefaultHttpsGroup_TrustList_LastUpdateTime =
      new NodeId(UShort.MIN, uint(13870));

  public static final NodeId CertificateGroupFolderType_DefaultHttpsGroup_TrustList_OpenWithMasks =
      new NodeId(UShort.MIN, uint(13871));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustList_OpenWithMasks_InputArguments =
          new NodeId(UShort.MIN, uint(13872));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustList_OpenWithMasks_OutputArguments =
          new NodeId(UShort.MIN, uint(13873));

  public static final NodeId CertificateGroupFolderType_DefaultHttpsGroup_TrustList_CloseAndUpdate =
      new NodeId(UShort.MIN, uint(13874));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustList_CloseAndUpdate_InputArguments =
          new NodeId(UShort.MIN, uint(13875));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustList_CloseAndUpdate_OutputArguments =
          new NodeId(UShort.MIN, uint(13876));

  public static final NodeId CertificateGroupFolderType_DefaultHttpsGroup_TrustList_AddCertificate =
      new NodeId(UShort.MIN, uint(13877));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustList_AddCertificate_InputArguments =
          new NodeId(UShort.MIN, uint(13878));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustList_RemoveCertificate =
          new NodeId(UShort.MIN, uint(13879));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustList_RemoveCertificate_InputArguments =
          new NodeId(UShort.MIN, uint(13880));

  public static final NodeId CertificateGroupFolderType_DefaultHttpsGroup_CertificateTypes =
      new NodeId(UShort.MIN, uint(13881));

  public static final NodeId CertificateGroupFolderType_DefaultUserTokenGroup =
      new NodeId(UShort.MIN, uint(13882));

  public static final NodeId CertificateGroupFolderType_DefaultUserTokenGroup_TrustList =
      new NodeId(UShort.MIN, uint(13883));

  public static final NodeId CertificateGroupFolderType_DefaultUserTokenGroup_TrustList_Size =
      new NodeId(UShort.MIN, uint(13884));

  public static final NodeId CertificateGroupFolderType_DefaultUserTokenGroup_TrustList_Writable =
      new NodeId(UShort.MIN, uint(13885));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustList_UserWritable =
          new NodeId(UShort.MIN, uint(13886));

  public static final NodeId CertificateGroupFolderType_DefaultUserTokenGroup_TrustList_OpenCount =
      new NodeId(UShort.MIN, uint(13887));

  public static final NodeId CertificateGroupFolderType_DefaultUserTokenGroup_TrustList_MimeType =
      new NodeId(UShort.MIN, uint(13888));

  public static final NodeId CertificateGroupFolderType_DefaultUserTokenGroup_TrustList_Open =
      new NodeId(UShort.MIN, uint(13889));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustList_Open_InputArguments =
          new NodeId(UShort.MIN, uint(13890));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustList_Open_OutputArguments =
          new NodeId(UShort.MIN, uint(13891));

  public static final NodeId CertificateGroupFolderType_DefaultUserTokenGroup_TrustList_Close =
      new NodeId(UShort.MIN, uint(13892));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustList_Close_InputArguments =
          new NodeId(UShort.MIN, uint(13893));

  public static final NodeId CertificateGroupFolderType_DefaultUserTokenGroup_TrustList_Read =
      new NodeId(UShort.MIN, uint(13894));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustList_Read_InputArguments =
          new NodeId(UShort.MIN, uint(13895));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustList_Read_OutputArguments =
          new NodeId(UShort.MIN, uint(13896));

  public static final NodeId CertificateGroupFolderType_DefaultUserTokenGroup_TrustList_Write =
      new NodeId(UShort.MIN, uint(13897));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustList_Write_InputArguments =
          new NodeId(UShort.MIN, uint(13898));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustList_GetPosition =
          new NodeId(UShort.MIN, uint(13899));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustList_GetPosition_InputArguments =
          new NodeId(UShort.MIN, uint(13900));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustList_GetPosition_OutputArguments =
          new NodeId(UShort.MIN, uint(13901));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustList_SetPosition =
          new NodeId(UShort.MIN, uint(13902));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustList_SetPosition_InputArguments =
          new NodeId(UShort.MIN, uint(13903));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustList_LastUpdateTime =
          new NodeId(UShort.MIN, uint(13904));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustList_OpenWithMasks =
          new NodeId(UShort.MIN, uint(13905));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustList_OpenWithMasks_InputArguments =
          new NodeId(UShort.MIN, uint(13906));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustList_OpenWithMasks_OutputArguments =
          new NodeId(UShort.MIN, uint(13907));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustList_CloseAndUpdate =
          new NodeId(UShort.MIN, uint(13908));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustList_CloseAndUpdate_InputArguments =
          new NodeId(UShort.MIN, uint(13909));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustList_CloseAndUpdate_OutputArguments =
          new NodeId(UShort.MIN, uint(13910));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustList_AddCertificate =
          new NodeId(UShort.MIN, uint(13911));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustList_AddCertificate_InputArguments =
          new NodeId(UShort.MIN, uint(13912));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustList_RemoveCertificate =
          new NodeId(UShort.MIN, uint(13913));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustList_RemoveCertificate_InputArguments =
          new NodeId(UShort.MIN, uint(13914));

  public static final NodeId CertificateGroupFolderType_DefaultUserTokenGroup_CertificateTypes =
      new NodeId(UShort.MIN, uint(13915));

  public static final NodeId CertificateGroupFolderType_AdditionalGroup_Placeholder =
      new NodeId(UShort.MIN, uint(13916));

  public static final NodeId CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustList =
      new NodeId(UShort.MIN, uint(13917));

  public static final NodeId CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustList_Size =
      new NodeId(UShort.MIN, uint(13918));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustList_Writable =
          new NodeId(UShort.MIN, uint(13919));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustList_UserWritable =
          new NodeId(UShort.MIN, uint(13920));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustList_OpenCount =
          new NodeId(UShort.MIN, uint(13921));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustList_MimeType =
          new NodeId(UShort.MIN, uint(13922));

  public static final NodeId CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustList_Open =
      new NodeId(UShort.MIN, uint(13923));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustList_Open_InputArguments =
          new NodeId(UShort.MIN, uint(13924));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustList_Open_OutputArguments =
          new NodeId(UShort.MIN, uint(13925));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustList_Close =
          new NodeId(UShort.MIN, uint(13926));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustList_Close_InputArguments =
          new NodeId(UShort.MIN, uint(13927));

  public static final NodeId CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustList_Read =
      new NodeId(UShort.MIN, uint(13928));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustList_Read_InputArguments =
          new NodeId(UShort.MIN, uint(13929));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustList_Read_OutputArguments =
          new NodeId(UShort.MIN, uint(13930));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustList_Write =
          new NodeId(UShort.MIN, uint(13931));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustList_Write_InputArguments =
          new NodeId(UShort.MIN, uint(13932));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustList_GetPosition =
          new NodeId(UShort.MIN, uint(13933));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustList_GetPosition_InputArguments =
          new NodeId(UShort.MIN, uint(13934));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustList_GetPosition_OutputArguments =
          new NodeId(UShort.MIN, uint(13935));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustList_SetPosition =
          new NodeId(UShort.MIN, uint(13936));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustList_SetPosition_InputArguments =
          new NodeId(UShort.MIN, uint(13937));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustList_LastUpdateTime =
          new NodeId(UShort.MIN, uint(13938));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustList_OpenWithMasks =
          new NodeId(UShort.MIN, uint(13939));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustList_OpenWithMasks_InputArguments =
          new NodeId(UShort.MIN, uint(13940));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustList_OpenWithMasks_OutputArguments =
          new NodeId(UShort.MIN, uint(13941));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustList_CloseAndUpdate =
          new NodeId(UShort.MIN, uint(13942));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustList_CloseAndUpdate_InputArguments =
          new NodeId(UShort.MIN, uint(13943));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustList_CloseAndUpdate_OutputArguments =
          new NodeId(UShort.MIN, uint(13944));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustList_AddCertificate =
          new NodeId(UShort.MIN, uint(13945));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustList_AddCertificate_InputArguments =
          new NodeId(UShort.MIN, uint(13946));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustList_RemoveCertificate =
          new NodeId(UShort.MIN, uint(13947));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustList_RemoveCertificate_InputArguments =
          new NodeId(UShort.MIN, uint(13948));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateTypes =
          new NodeId(UShort.MIN, uint(13949));

  public static final NodeId ServerConfigurationType_CertificateGroups =
      new NodeId(UShort.MIN, uint(13950));

  public static final NodeId ServerConfigurationType_CertificateGroups_DefaultApplicationGroup =
      new NodeId(UShort.MIN, uint(13951));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustList =
          new NodeId(UShort.MIN, uint(13952));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustList_Size =
          new NodeId(UShort.MIN, uint(13953));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustList_Writable =
          new NodeId(UShort.MIN, uint(13954));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustList_UserWritable =
          new NodeId(UShort.MIN, uint(13955));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustList_OpenCount =
          new NodeId(UShort.MIN, uint(13956));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustList_MimeType =
          new NodeId(UShort.MIN, uint(13957));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustList_Open =
          new NodeId(UShort.MIN, uint(13958));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustList_Open_InputArguments =
          new NodeId(UShort.MIN, uint(13959));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustList_Open_OutputArguments =
          new NodeId(UShort.MIN, uint(13960));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustList_Close =
          new NodeId(UShort.MIN, uint(13961));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustList_Close_InputArguments =
          new NodeId(UShort.MIN, uint(13962));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustList_Read =
          new NodeId(UShort.MIN, uint(13963));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustList_Read_InputArguments =
          new NodeId(UShort.MIN, uint(13964));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustList_Read_OutputArguments =
          new NodeId(UShort.MIN, uint(13965));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustList_Write =
          new NodeId(UShort.MIN, uint(13966));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustList_Write_InputArguments =
          new NodeId(UShort.MIN, uint(13967));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustList_GetPosition =
          new NodeId(UShort.MIN, uint(13968));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustList_GetPosition_InputArguments =
          new NodeId(UShort.MIN, uint(13969));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustList_GetPosition_OutputArguments =
          new NodeId(UShort.MIN, uint(13970));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustList_SetPosition =
          new NodeId(UShort.MIN, uint(13971));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustList_SetPosition_InputArguments =
          new NodeId(UShort.MIN, uint(13972));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustList_LastUpdateTime =
          new NodeId(UShort.MIN, uint(13973));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustList_OpenWithMasks =
          new NodeId(UShort.MIN, uint(13974));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustList_OpenWithMasks_InputArguments =
          new NodeId(UShort.MIN, uint(13975));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustList_OpenWithMasks_OutputArguments =
          new NodeId(UShort.MIN, uint(13976));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustList_CloseAndUpdate =
          new NodeId(UShort.MIN, uint(13977));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustList_CloseAndUpdate_InputArguments =
          new NodeId(UShort.MIN, uint(13978));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustList_CloseAndUpdate_OutputArguments =
          new NodeId(UShort.MIN, uint(13979));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustList_AddCertificate =
          new NodeId(UShort.MIN, uint(13980));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustList_AddCertificate_InputArguments =
          new NodeId(UShort.MIN, uint(13981));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustList_RemoveCertificate =
          new NodeId(UShort.MIN, uint(13982));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustList_RemoveCertificate_InputArguments =
          new NodeId(UShort.MIN, uint(13983));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateTypes =
          new NodeId(UShort.MIN, uint(13984));

  public static final NodeId ServerConfigurationType_CertificateGroups_DefaultHttpsGroup =
      new NodeId(UShort.MIN, uint(13985));

  public static final NodeId ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustList =
      new NodeId(UShort.MIN, uint(13986));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustList_Size =
          new NodeId(UShort.MIN, uint(13987));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustList_Writable =
          new NodeId(UShort.MIN, uint(13988));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustList_UserWritable =
          new NodeId(UShort.MIN, uint(13989));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustList_OpenCount =
          new NodeId(UShort.MIN, uint(13990));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustList_MimeType =
          new NodeId(UShort.MIN, uint(13991));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustList_Open =
          new NodeId(UShort.MIN, uint(13992));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustList_Open_InputArguments =
          new NodeId(UShort.MIN, uint(13993));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustList_Open_OutputArguments =
          new NodeId(UShort.MIN, uint(13994));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustList_Close =
          new NodeId(UShort.MIN, uint(13995));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustList_Close_InputArguments =
          new NodeId(UShort.MIN, uint(13996));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustList_Read =
          new NodeId(UShort.MIN, uint(13997));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustList_Read_InputArguments =
          new NodeId(UShort.MIN, uint(13998));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustList_Read_OutputArguments =
          new NodeId(UShort.MIN, uint(13999));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustList_Write =
          new NodeId(UShort.MIN, uint(14000));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustList_Write_InputArguments =
          new NodeId(UShort.MIN, uint(14001));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustList_GetPosition =
          new NodeId(UShort.MIN, uint(14002));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustList_GetPosition_InputArguments =
          new NodeId(UShort.MIN, uint(14003));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustList_GetPosition_OutputArguments =
          new NodeId(UShort.MIN, uint(14004));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustList_SetPosition =
          new NodeId(UShort.MIN, uint(14005));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustList_SetPosition_InputArguments =
          new NodeId(UShort.MIN, uint(14006));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustList_LastUpdateTime =
          new NodeId(UShort.MIN, uint(14007));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustList_OpenWithMasks =
          new NodeId(UShort.MIN, uint(14008));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustList_OpenWithMasks_InputArguments =
          new NodeId(UShort.MIN, uint(14009));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustList_OpenWithMasks_OutputArguments =
          new NodeId(UShort.MIN, uint(14010));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustList_CloseAndUpdate =
          new NodeId(UShort.MIN, uint(14011));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustList_CloseAndUpdate_InputArguments =
          new NodeId(UShort.MIN, uint(14012));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustList_CloseAndUpdate_OutputArguments =
          new NodeId(UShort.MIN, uint(14013));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustList_AddCertificate =
          new NodeId(UShort.MIN, uint(14014));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustList_AddCertificate_InputArguments =
          new NodeId(UShort.MIN, uint(14015));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustList_RemoveCertificate =
          new NodeId(UShort.MIN, uint(14016));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustList_RemoveCertificate_InputArguments =
          new NodeId(UShort.MIN, uint(14017));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateTypes =
          new NodeId(UShort.MIN, uint(14018));

  public static final NodeId ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup =
      new NodeId(UShort.MIN, uint(14019));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_TrustList =
          new NodeId(UShort.MIN, uint(14020));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_TrustList_Size =
          new NodeId(UShort.MIN, uint(14021));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_TrustList_Writable =
          new NodeId(UShort.MIN, uint(14022));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_TrustList_UserWritable =
          new NodeId(UShort.MIN, uint(14023));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_TrustList_OpenCount =
          new NodeId(UShort.MIN, uint(14024));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_TrustList_MimeType =
          new NodeId(UShort.MIN, uint(14025));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_TrustList_Open =
          new NodeId(UShort.MIN, uint(14026));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_TrustList_Open_InputArguments =
          new NodeId(UShort.MIN, uint(14027));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_TrustList_Open_OutputArguments =
          new NodeId(UShort.MIN, uint(14028));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_TrustList_Close =
          new NodeId(UShort.MIN, uint(14029));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_TrustList_Close_InputArguments =
          new NodeId(UShort.MIN, uint(14030));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_TrustList_Read =
          new NodeId(UShort.MIN, uint(14031));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_TrustList_Read_InputArguments =
          new NodeId(UShort.MIN, uint(14032));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_TrustList_Read_OutputArguments =
          new NodeId(UShort.MIN, uint(14033));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_TrustList_Write =
          new NodeId(UShort.MIN, uint(14034));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_TrustList_Write_InputArguments =
          new NodeId(UShort.MIN, uint(14035));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_TrustList_GetPosition =
          new NodeId(UShort.MIN, uint(14036));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_TrustList_GetPosition_InputArguments =
          new NodeId(UShort.MIN, uint(14037));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_TrustList_GetPosition_OutputArguments =
          new NodeId(UShort.MIN, uint(14038));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_TrustList_SetPosition =
          new NodeId(UShort.MIN, uint(14039));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_TrustList_SetPosition_InputArguments =
          new NodeId(UShort.MIN, uint(14040));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_TrustList_LastUpdateTime =
          new NodeId(UShort.MIN, uint(14041));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_TrustList_OpenWithMasks =
          new NodeId(UShort.MIN, uint(14042));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_TrustList_OpenWithMasks_InputArguments =
          new NodeId(UShort.MIN, uint(14043));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_TrustList_OpenWithMasks_OutputArguments =
          new NodeId(UShort.MIN, uint(14044));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_TrustList_CloseAndUpdate =
          new NodeId(UShort.MIN, uint(14045));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_TrustList_CloseAndUpdate_InputArguments =
          new NodeId(UShort.MIN, uint(14046));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_TrustList_CloseAndUpdate_OutputArguments =
          new NodeId(UShort.MIN, uint(14047));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_TrustList_AddCertificate =
          new NodeId(UShort.MIN, uint(14048));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_TrustList_AddCertificate_InputArguments =
          new NodeId(UShort.MIN, uint(14049));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_TrustList_RemoveCertificate =
          new NodeId(UShort.MIN, uint(14050));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_TrustList_RemoveCertificate_InputArguments =
          new NodeId(UShort.MIN, uint(14051));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_CertificateTypes =
          new NodeId(UShort.MIN, uint(14052));

  public static final NodeId ServerConfiguration_CertificateGroups =
      new NodeId(UShort.MIN, uint(14053));

  public static final NodeId ServerConfiguration_CertificateGroups_DefaultHttpsGroup =
      new NodeId(UShort.MIN, uint(14088));

  public static final NodeId ServerConfiguration_CertificateGroups_DefaultHttpsGroup_TrustList =
      new NodeId(UShort.MIN, uint(14089));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultHttpsGroup_TrustList_Size =
          new NodeId(UShort.MIN, uint(14090));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultHttpsGroup_TrustList_Writable =
          new NodeId(UShort.MIN, uint(14091));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultHttpsGroup_TrustList_UserWritable =
          new NodeId(UShort.MIN, uint(14092));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultHttpsGroup_TrustList_OpenCount =
          new NodeId(UShort.MIN, uint(14093));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultHttpsGroup_TrustList_MimeType =
          new NodeId(UShort.MIN, uint(14094));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultHttpsGroup_TrustList_Open =
          new NodeId(UShort.MIN, uint(14095));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultHttpsGroup_TrustList_Open_InputArguments =
          new NodeId(UShort.MIN, uint(14096));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultHttpsGroup_TrustList_Open_OutputArguments =
          new NodeId(UShort.MIN, uint(14097));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultHttpsGroup_TrustList_Close =
          new NodeId(UShort.MIN, uint(14098));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultHttpsGroup_TrustList_Close_InputArguments =
          new NodeId(UShort.MIN, uint(14099));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultHttpsGroup_TrustList_Read =
          new NodeId(UShort.MIN, uint(14100));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultHttpsGroup_TrustList_Read_InputArguments =
          new NodeId(UShort.MIN, uint(14101));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultHttpsGroup_TrustList_Read_OutputArguments =
          new NodeId(UShort.MIN, uint(14102));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultHttpsGroup_TrustList_Write =
          new NodeId(UShort.MIN, uint(14103));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultHttpsGroup_TrustList_Write_InputArguments =
          new NodeId(UShort.MIN, uint(14104));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultHttpsGroup_TrustList_GetPosition =
          new NodeId(UShort.MIN, uint(14105));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultHttpsGroup_TrustList_GetPosition_InputArguments =
          new NodeId(UShort.MIN, uint(14106));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultHttpsGroup_TrustList_GetPosition_OutputArguments =
          new NodeId(UShort.MIN, uint(14107));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultHttpsGroup_TrustList_SetPosition =
          new NodeId(UShort.MIN, uint(14108));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultHttpsGroup_TrustList_SetPosition_InputArguments =
          new NodeId(UShort.MIN, uint(14109));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultHttpsGroup_TrustList_LastUpdateTime =
          new NodeId(UShort.MIN, uint(14110));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultHttpsGroup_TrustList_OpenWithMasks =
          new NodeId(UShort.MIN, uint(14111));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultHttpsGroup_TrustList_OpenWithMasks_InputArguments =
          new NodeId(UShort.MIN, uint(14112));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultHttpsGroup_TrustList_OpenWithMasks_OutputArguments =
          new NodeId(UShort.MIN, uint(14113));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultHttpsGroup_TrustList_CloseAndUpdate =
          new NodeId(UShort.MIN, uint(14114));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultHttpsGroup_TrustList_CloseAndUpdate_InputArguments =
          new NodeId(UShort.MIN, uint(14115));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultHttpsGroup_TrustList_CloseAndUpdate_OutputArguments =
          new NodeId(UShort.MIN, uint(14116));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultHttpsGroup_TrustList_AddCertificate =
          new NodeId(UShort.MIN, uint(14117));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultHttpsGroup_TrustList_AddCertificate_InputArguments =
          new NodeId(UShort.MIN, uint(14118));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultHttpsGroup_TrustList_RemoveCertificate =
          new NodeId(UShort.MIN, uint(14119));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultHttpsGroup_TrustList_RemoveCertificate_InputArguments =
          new NodeId(UShort.MIN, uint(14120));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultHttpsGroup_CertificateTypes =
          new NodeId(UShort.MIN, uint(14121));

  public static final NodeId ServerConfiguration_CertificateGroups_DefaultUserTokenGroup =
      new NodeId(UShort.MIN, uint(14122));

  public static final NodeId ServerConfiguration_CertificateGroups_DefaultUserTokenGroup_TrustList =
      new NodeId(UShort.MIN, uint(14123));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultUserTokenGroup_TrustList_Size =
          new NodeId(UShort.MIN, uint(14124));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultUserTokenGroup_TrustList_Writable =
          new NodeId(UShort.MIN, uint(14125));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultUserTokenGroup_TrustList_UserWritable =
          new NodeId(UShort.MIN, uint(14126));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultUserTokenGroup_TrustList_OpenCount =
          new NodeId(UShort.MIN, uint(14127));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultUserTokenGroup_TrustList_MimeType =
          new NodeId(UShort.MIN, uint(14128));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultUserTokenGroup_TrustList_Open =
          new NodeId(UShort.MIN, uint(14129));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultUserTokenGroup_TrustList_Open_InputArguments =
          new NodeId(UShort.MIN, uint(14130));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultUserTokenGroup_TrustList_Open_OutputArguments =
          new NodeId(UShort.MIN, uint(14131));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultUserTokenGroup_TrustList_Close =
          new NodeId(UShort.MIN, uint(14132));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultUserTokenGroup_TrustList_Close_InputArguments =
          new NodeId(UShort.MIN, uint(14133));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultUserTokenGroup_TrustList_Read =
          new NodeId(UShort.MIN, uint(14134));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultUserTokenGroup_TrustList_Read_InputArguments =
          new NodeId(UShort.MIN, uint(14135));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultUserTokenGroup_TrustList_Read_OutputArguments =
          new NodeId(UShort.MIN, uint(14136));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultUserTokenGroup_TrustList_Write =
          new NodeId(UShort.MIN, uint(14137));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultUserTokenGroup_TrustList_Write_InputArguments =
          new NodeId(UShort.MIN, uint(14138));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultUserTokenGroup_TrustList_GetPosition =
          new NodeId(UShort.MIN, uint(14139));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultUserTokenGroup_TrustList_GetPosition_InputArguments =
          new NodeId(UShort.MIN, uint(14140));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultUserTokenGroup_TrustList_GetPosition_OutputArguments =
          new NodeId(UShort.MIN, uint(14141));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultUserTokenGroup_TrustList_SetPosition =
          new NodeId(UShort.MIN, uint(14142));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultUserTokenGroup_TrustList_SetPosition_InputArguments =
          new NodeId(UShort.MIN, uint(14143));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultUserTokenGroup_TrustList_LastUpdateTime =
          new NodeId(UShort.MIN, uint(14144));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultUserTokenGroup_TrustList_OpenWithMasks =
          new NodeId(UShort.MIN, uint(14145));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultUserTokenGroup_TrustList_OpenWithMasks_InputArguments =
          new NodeId(UShort.MIN, uint(14146));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultUserTokenGroup_TrustList_OpenWithMasks_OutputArguments =
          new NodeId(UShort.MIN, uint(14147));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultUserTokenGroup_TrustList_CloseAndUpdate =
          new NodeId(UShort.MIN, uint(14148));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultUserTokenGroup_TrustList_CloseAndUpdate_InputArguments =
          new NodeId(UShort.MIN, uint(14149));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultUserTokenGroup_TrustList_CloseAndUpdate_OutputArguments =
          new NodeId(UShort.MIN, uint(14150));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultUserTokenGroup_TrustList_AddCertificate =
          new NodeId(UShort.MIN, uint(14151));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultUserTokenGroup_TrustList_AddCertificate_InputArguments =
          new NodeId(UShort.MIN, uint(14152));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultUserTokenGroup_TrustList_RemoveCertificate =
          new NodeId(UShort.MIN, uint(14153));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultUserTokenGroup_TrustList_RemoveCertificate_InputArguments =
          new NodeId(UShort.MIN, uint(14154));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultUserTokenGroup_CertificateTypes =
          new NodeId(UShort.MIN, uint(14155));

  public static final NodeId ServerConfiguration_CertificateGroups_DefaultApplicationGroup =
      new NodeId(UShort.MIN, uint(14156));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_Writable =
          new NodeId(UShort.MIN, uint(14157));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_UserWritable =
          new NodeId(UShort.MIN, uint(14158));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_MimeType =
          new NodeId(UShort.MIN, uint(14159));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_CloseAndUpdate_InputArguments =
          new NodeId(UShort.MIN, uint(14160));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_CertificateTypes =
          new NodeId(UShort.MIN, uint(14161));

  public static final NodeId RemoveConnectionMethodType = new NodeId(UShort.MIN, uint(14183));

  public static final NodeId RemoveConnectionMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(14184));

  public static final NodeId PubSubConnectionType = new NodeId(UShort.MIN, uint(14209));

  public static final NodeId PubSubConnectionType_Address = new NodeId(UShort.MIN, uint(14221));

  public static final NodeId PubSubConnectionType_RemoveGroup = new NodeId(UShort.MIN, uint(14225));

  public static final NodeId PubSubConnectionType_RemoveGroup_InputArguments =
      new NodeId(UShort.MIN, uint(14226));

  public static final NodeId PubSubGroupType = new NodeId(UShort.MIN, uint(14232));

  public static final NodeId PublishedVariableDataType = new NodeId(UShort.MIN, uint(14273));

  public static final NodeId PublishedVariableDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(14319));

  public static final NodeId OpcUa_XmlSchema_PublishedVariableDataType =
      new NodeId(UShort.MIN, uint(14320));

  public static final NodeId OpcUa_XmlSchema_PublishedVariableDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(14321));

  public static final NodeId OpcUa_XmlSchema_PublishedVariableDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(14322));

  public static final NodeId PublishedVariableDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(14323));

  public static final NodeId OpcUa_BinarySchema_PublishedVariableDataType =
      new NodeId(UShort.MIN, uint(14324));

  public static final NodeId OpcUa_BinarySchema_PublishedVariableDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(14325));

  public static final NodeId OpcUa_BinarySchema_PublishedVariableDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(14326));

  public static final NodeId AuditCreateSessionEventType_SessionId =
      new NodeId(UShort.MIN, uint(14413));

  public static final NodeId AuditUrlMismatchEventType_SessionId =
      new NodeId(UShort.MIN, uint(14414));

  public static final NodeId Server_ServerRedundancy_ServerNetworkGroups =
      new NodeId(UShort.MIN, uint(14415));

  public static final NodeId PublishSubscribeType = new NodeId(UShort.MIN, uint(14416));

  public static final NodeId PublishSubscribeType_ConnectionName_Placeholder =
      new NodeId(UShort.MIN, uint(14417));

  public static final NodeId PublishSubscribeType_ConnectionName_Placeholder_PublisherId =
      new NodeId(UShort.MIN, uint(14418));

  public static final NodeId PublishSubscribeType_ConnectionName_Placeholder_Status =
      new NodeId(UShort.MIN, uint(14419));

  public static final NodeId PublishSubscribeType_ConnectionName_Placeholder_Status_State =
      new NodeId(UShort.MIN, uint(14420));

  public static final NodeId PublishSubscribeType_ConnectionName_Placeholder_Status_Enable =
      new NodeId(UShort.MIN, uint(14421));

  public static final NodeId PublishSubscribeType_ConnectionName_Placeholder_Status_Disable =
      new NodeId(UShort.MIN, uint(14422));

  public static final NodeId PublishSubscribeType_ConnectionName_Placeholder_Address =
      new NodeId(UShort.MIN, uint(14423));

  public static final NodeId PublishSubscribeType_ConnectionName_Placeholder_RemoveGroup =
      new NodeId(UShort.MIN, uint(14424));

  public static final NodeId
      PublishSubscribeType_ConnectionName_Placeholder_RemoveGroup_InputArguments =
          new NodeId(UShort.MIN, uint(14425));

  public static final NodeId PublishSubscribeType_RemoveConnection =
      new NodeId(UShort.MIN, uint(14432));

  public static final NodeId PublishSubscribeType_RemoveConnection_InputArguments =
      new NodeId(UShort.MIN, uint(14433));

  public static final NodeId PublishSubscribeType_PublishedDataSets =
      new NodeId(UShort.MIN, uint(14434));

  public static final NodeId PublishSubscribeType_PublishedDataSets_AddPublishedDataItems =
      new NodeId(UShort.MIN, uint(14435));

  public static final NodeId
      PublishSubscribeType_PublishedDataSets_AddPublishedDataItems_InputArguments =
          new NodeId(UShort.MIN, uint(14436));

  public static final NodeId
      PublishSubscribeType_PublishedDataSets_AddPublishedDataItems_OutputArguments =
          new NodeId(UShort.MIN, uint(14437));

  public static final NodeId PublishSubscribeType_PublishedDataSets_AddPublishedEvents =
      new NodeId(UShort.MIN, uint(14438));

  public static final NodeId
      PublishSubscribeType_PublishedDataSets_AddPublishedEvents_InputArguments =
          new NodeId(UShort.MIN, uint(14439));

  public static final NodeId
      PublishSubscribeType_PublishedDataSets_AddPublishedEvents_OutputArguments =
          new NodeId(UShort.MIN, uint(14440));

  public static final NodeId PublishSubscribeType_PublishedDataSets_RemovePublishedDataSet =
      new NodeId(UShort.MIN, uint(14441));

  public static final NodeId
      PublishSubscribeType_PublishedDataSets_RemovePublishedDataSet_InputArguments =
          new NodeId(UShort.MIN, uint(14442));

  public static final NodeId PublishSubscribe = new NodeId(UShort.MIN, uint(14443));

  public static final NodeId HasPubSubConnection = new NodeId(UShort.MIN, uint(14476));

  public static final NodeId DataSetFolderType = new NodeId(UShort.MIN, uint(14477));

  public static final NodeId DataSetFolderType_DataSetFolderName_Placeholder =
      new NodeId(UShort.MIN, uint(14478));

  public static final NodeId DataSetFolderType_DataSetFolderName_Placeholder_AddPublishedDataItems =
      new NodeId(UShort.MIN, uint(14479));

  public static final NodeId
      DataSetFolderType_DataSetFolderName_Placeholder_AddPublishedDataItems_InputArguments =
          new NodeId(UShort.MIN, uint(14480));

  public static final NodeId
      DataSetFolderType_DataSetFolderName_Placeholder_AddPublishedDataItems_OutputArguments =
          new NodeId(UShort.MIN, uint(14481));

  public static final NodeId DataSetFolderType_DataSetFolderName_Placeholder_AddPublishedEvents =
      new NodeId(UShort.MIN, uint(14482));

  public static final NodeId
      DataSetFolderType_DataSetFolderName_Placeholder_AddPublishedEvents_InputArguments =
          new NodeId(UShort.MIN, uint(14483));

  public static final NodeId
      DataSetFolderType_DataSetFolderName_Placeholder_AddPublishedEvents_OutputArguments =
          new NodeId(UShort.MIN, uint(14484));

  public static final NodeId
      DataSetFolderType_DataSetFolderName_Placeholder_RemovePublishedDataSet =
          new NodeId(UShort.MIN, uint(14485));

  public static final NodeId
      DataSetFolderType_DataSetFolderName_Placeholder_RemovePublishedDataSet_InputArguments =
          new NodeId(UShort.MIN, uint(14486));

  public static final NodeId DataSetFolderType_PublishedDataSetName_Placeholder =
      new NodeId(UShort.MIN, uint(14487));

  public static final NodeId
      DataSetFolderType_PublishedDataSetName_Placeholder_ConfigurationVersion =
          new NodeId(UShort.MIN, uint(14489));

  public static final NodeId DataSetFolderType_AddPublishedDataItems =
      new NodeId(UShort.MIN, uint(14493));

  public static final NodeId DataSetFolderType_AddPublishedDataItems_InputArguments =
      new NodeId(UShort.MIN, uint(14494));

  public static final NodeId DataSetFolderType_AddPublishedDataItems_OutputArguments =
      new NodeId(UShort.MIN, uint(14495));

  public static final NodeId DataSetFolderType_AddPublishedEvents =
      new NodeId(UShort.MIN, uint(14496));

  public static final NodeId DataSetFolderType_AddPublishedEvents_InputArguments =
      new NodeId(UShort.MIN, uint(14497));

  public static final NodeId DataSetFolderType_AddPublishedEvents_OutputArguments =
      new NodeId(UShort.MIN, uint(14498));

  public static final NodeId DataSetFolderType_RemovePublishedDataSet =
      new NodeId(UShort.MIN, uint(14499));

  public static final NodeId DataSetFolderType_RemovePublishedDataSet_InputArguments =
      new NodeId(UShort.MIN, uint(14500));

  public static final NodeId AddPublishedDataItemsMethodType = new NodeId(UShort.MIN, uint(14501));

  public static final NodeId AddPublishedDataItemsMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(14502));

  public static final NodeId AddPublishedDataItemsMethodType_OutputArguments =
      new NodeId(UShort.MIN, uint(14503));

  public static final NodeId AddPublishedEventsMethodType = new NodeId(UShort.MIN, uint(14504));

  public static final NodeId AddPublishedEventsMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(14505));

  public static final NodeId AddPublishedEventsMethodType_OutputArguments =
      new NodeId(UShort.MIN, uint(14506));

  public static final NodeId RemovePublishedDataSetMethodType = new NodeId(UShort.MIN, uint(14507));

  public static final NodeId RemovePublishedDataSetMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(14508));

  public static final NodeId PublishedDataSetType = new NodeId(UShort.MIN, uint(14509));

  public static final NodeId PublishedDataSetType_ConfigurationVersion =
      new NodeId(UShort.MIN, uint(14519));

  public static final NodeId DataSetMetaDataType = new NodeId(UShort.MIN, uint(14523));

  public static final NodeId FieldMetaData = new NodeId(UShort.MIN, uint(14524));

  public static final NodeId DataTypeDescription = new NodeId(UShort.MIN, uint(14525));

  public static final NodeId StructureType_EnumStrings = new NodeId(UShort.MIN, uint(14528));

  public static final NodeId KeyValuePair = new NodeId(UShort.MIN, uint(14533));

  public static final NodeId PublishedDataItemsType = new NodeId(UShort.MIN, uint(14534));

  public static final NodeId PublishedDataItemsType_ConfigurationVersion =
      new NodeId(UShort.MIN, uint(14544));

  public static final NodeId PublishedDataItemsType_PublishedData =
      new NodeId(UShort.MIN, uint(14548));

  public static final NodeId PublishedDataItemsType_AddVariables =
      new NodeId(UShort.MIN, uint(14555));

  public static final NodeId PublishedDataItemsType_AddVariables_InputArguments =
      new NodeId(UShort.MIN, uint(14556));

  public static final NodeId PublishedDataItemsType_AddVariables_OutputArguments =
      new NodeId(UShort.MIN, uint(14557));

  public static final NodeId PublishedDataItemsType_RemoveVariables =
      new NodeId(UShort.MIN, uint(14558));

  public static final NodeId PublishedDataItemsType_RemoveVariables_InputArguments =
      new NodeId(UShort.MIN, uint(14559));

  public static final NodeId PublishedDataItemsType_RemoveVariables_OutputArguments =
      new NodeId(UShort.MIN, uint(14560));

  public static final NodeId PublishedDataItemsAddVariablesMethodType =
      new NodeId(UShort.MIN, uint(14564));

  public static final NodeId PublishedDataItemsAddVariablesMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(14565));

  public static final NodeId PublishedDataItemsAddVariablesMethodType_OutputArguments =
      new NodeId(UShort.MIN, uint(14566));

  public static final NodeId PublishedDataItemsRemoveVariablesMethodType =
      new NodeId(UShort.MIN, uint(14567));

  public static final NodeId PublishedDataItemsRemoveVariablesMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(14568));

  public static final NodeId PublishedDataItemsRemoveVariablesMethodType_OutputArguments =
      new NodeId(UShort.MIN, uint(14569));

  public static final NodeId PublishedEventsType = new NodeId(UShort.MIN, uint(14572));

  public static final NodeId PublishedEventsType_ConfigurationVersion =
      new NodeId(UShort.MIN, uint(14582));

  public static final NodeId PublishedEventsType_PubSubEventNotifier =
      new NodeId(UShort.MIN, uint(14586));

  public static final NodeId PublishedEventsType_SelectedFields =
      new NodeId(UShort.MIN, uint(14587));

  public static final NodeId PublishedEventsType_Filter = new NodeId(UShort.MIN, uint(14588));

  public static final NodeId ConfigurationVersionDataType = new NodeId(UShort.MIN, uint(14593));

  public static final NodeId PubSubConnectionType_PublisherId = new NodeId(UShort.MIN, uint(14595));

  public static final NodeId PubSubConnectionType_Status = new NodeId(UShort.MIN, uint(14600));

  public static final NodeId PubSubConnectionType_Status_State =
      new NodeId(UShort.MIN, uint(14601));

  public static final NodeId PubSubConnectionType_Status_Enable =
      new NodeId(UShort.MIN, uint(14602));

  public static final NodeId PubSubConnectionType_Status_Disable =
      new NodeId(UShort.MIN, uint(14603));

  public static final NodeId PubSubConnectionTypeRemoveGroupMethodType =
      new NodeId(UShort.MIN, uint(14604));

  public static final NodeId PubSubConnectionTypeRemoveGroupMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(14605));

  public static final NodeId PubSubGroupTypeRemoveWriterMethodType =
      new NodeId(UShort.MIN, uint(14623));

  public static final NodeId PubSubGroupTypeRemoveWriterMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(14624));

  public static final NodeId PubSubGroupTypeRemoveReaderMethodType =
      new NodeId(UShort.MIN, uint(14625));

  public static final NodeId PubSubGroupTypeRemoveReaderMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(14626));

  public static final NodeId PubSubStatusType = new NodeId(UShort.MIN, uint(14643));

  public static final NodeId PubSubStatusType_State = new NodeId(UShort.MIN, uint(14644));

  public static final NodeId PubSubStatusType_Enable = new NodeId(UShort.MIN, uint(14645));

  public static final NodeId PubSubStatusType_Disable = new NodeId(UShort.MIN, uint(14646));

  public static final NodeId PubSubState = new NodeId(UShort.MIN, uint(14647));

  public static final NodeId PubSubState_EnumStrings = new NodeId(UShort.MIN, uint(14648));

  public static final NodeId FieldTargetDataType = new NodeId(UShort.MIN, uint(14744));

  public static final NodeId DataSetMetaDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(14794));

  public static final NodeId FieldMetaData_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(14795));

  public static final NodeId DataTypeDescription_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(14796));

  public static final NodeId DataTypeDefinition_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(14797));

  public static final NodeId StructureDefinition_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(14798));

  public static final NodeId EnumDefinition_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(14799));

  public static final NodeId StructureField_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(14800));

  public static final NodeId EnumField_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(14801));

  public static final NodeId KeyValuePair_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(14802));

  public static final NodeId ConfigurationVersionDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(14803));

  public static final NodeId FieldTargetDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(14804));

  public static final NodeId OpcUa_XmlSchema_DataSetMetaDataType =
      new NodeId(UShort.MIN, uint(14805));

  public static final NodeId OpcUa_XmlSchema_DataSetMetaDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(14806));

  public static final NodeId OpcUa_XmlSchema_DataSetMetaDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(14807));

  public static final NodeId OpcUa_XmlSchema_FieldMetaData = new NodeId(UShort.MIN, uint(14808));

  public static final NodeId OpcUa_XmlSchema_FieldMetaData_DataTypeVersion =
      new NodeId(UShort.MIN, uint(14809));

  public static final NodeId OpcUa_XmlSchema_FieldMetaData_DictionaryFragment =
      new NodeId(UShort.MIN, uint(14810));

  public static final NodeId OpcUa_XmlSchema_DataTypeDescription =
      new NodeId(UShort.MIN, uint(14811));

  public static final NodeId OpcUa_XmlSchema_DataTypeDescription_DataTypeVersion =
      new NodeId(UShort.MIN, uint(14812));

  public static final NodeId OpcUa_XmlSchema_DataTypeDescription_DictionaryFragment =
      new NodeId(UShort.MIN, uint(14813));

  public static final NodeId OpcUa_XmlSchema_EnumField = new NodeId(UShort.MIN, uint(14826));

  public static final NodeId OpcUa_XmlSchema_EnumField_DataTypeVersion =
      new NodeId(UShort.MIN, uint(14827));

  public static final NodeId OpcUa_XmlSchema_EnumField_DictionaryFragment =
      new NodeId(UShort.MIN, uint(14828));

  public static final NodeId OpcUa_XmlSchema_KeyValuePair = new NodeId(UShort.MIN, uint(14829));

  public static final NodeId OpcUa_XmlSchema_KeyValuePair_DataTypeVersion =
      new NodeId(UShort.MIN, uint(14830));

  public static final NodeId OpcUa_XmlSchema_KeyValuePair_DictionaryFragment =
      new NodeId(UShort.MIN, uint(14831));

  public static final NodeId OpcUa_XmlSchema_ConfigurationVersionDataType =
      new NodeId(UShort.MIN, uint(14832));

  public static final NodeId OpcUa_XmlSchema_ConfigurationVersionDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(14833));

  public static final NodeId OpcUa_XmlSchema_ConfigurationVersionDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(14834));

  public static final NodeId OpcUa_XmlSchema_FieldTargetDataType =
      new NodeId(UShort.MIN, uint(14835));

  public static final NodeId OpcUa_XmlSchema_FieldTargetDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(14836));

  public static final NodeId OpcUa_XmlSchema_FieldTargetDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(14837));

  public static final NodeId FieldMetaData_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(14839));

  public static final NodeId StructureField_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(14844));

  public static final NodeId EnumField_Encoding_DefaultBinary = new NodeId(UShort.MIN, uint(14845));

  public static final NodeId KeyValuePair_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(14846));

  public static final NodeId ConfigurationVersionDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(14847));

  public static final NodeId FieldTargetDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(14848));

  public static final NodeId OpcUa_BinarySchema_DataSetMetaDataType =
      new NodeId(UShort.MIN, uint(14849));

  public static final NodeId OpcUa_BinarySchema_DataSetMetaDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(14850));

  public static final NodeId OpcUa_BinarySchema_DataSetMetaDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(14851));

  public static final NodeId OpcUa_BinarySchema_FieldMetaData = new NodeId(UShort.MIN, uint(14852));

  public static final NodeId OpcUa_BinarySchema_FieldMetaData_DataTypeVersion =
      new NodeId(UShort.MIN, uint(14853));

  public static final NodeId OpcUa_BinarySchema_FieldMetaData_DictionaryFragment =
      new NodeId(UShort.MIN, uint(14854));

  public static final NodeId OpcUa_BinarySchema_DataTypeDescription =
      new NodeId(UShort.MIN, uint(14855));

  public static final NodeId OpcUa_BinarySchema_DataTypeDescription_DataTypeVersion =
      new NodeId(UShort.MIN, uint(14856));

  public static final NodeId OpcUa_BinarySchema_DataTypeDescription_DictionaryFragment =
      new NodeId(UShort.MIN, uint(14857));

  public static final NodeId OpcUa_BinarySchema_EnumField = new NodeId(UShort.MIN, uint(14870));

  public static final NodeId OpcUa_BinarySchema_EnumField_DataTypeVersion =
      new NodeId(UShort.MIN, uint(14871));

  public static final NodeId OpcUa_BinarySchema_EnumField_DictionaryFragment =
      new NodeId(UShort.MIN, uint(14872));

  public static final NodeId OpcUa_BinarySchema_KeyValuePair = new NodeId(UShort.MIN, uint(14873));

  public static final NodeId OpcUa_BinarySchema_KeyValuePair_DataTypeVersion =
      new NodeId(UShort.MIN, uint(14874));

  public static final NodeId OpcUa_BinarySchema_KeyValuePair_DictionaryFragment =
      new NodeId(UShort.MIN, uint(14875));

  public static final NodeId OpcUa_BinarySchema_ConfigurationVersionDataType =
      new NodeId(UShort.MIN, uint(14876));

  public static final NodeId OpcUa_BinarySchema_ConfigurationVersionDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(14877));

  public static final NodeId OpcUa_BinarySchema_ConfigurationVersionDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(14878));

  public static final NodeId OpcUa_BinarySchema_FieldTargetDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(14880));

  public static final NodeId OpcUa_BinarySchema_FieldTargetDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(14881));

  public static final NodeId CertificateExpirationAlarmType_ExpirationLimit =
      new NodeId(UShort.MIN, uint(14900));

  public static final NodeId DataSetToWriter = new NodeId(UShort.MIN, uint(14936));

  public static final NodeId DataTypeDictionaryType_Deprecated =
      new NodeId(UShort.MIN, uint(15001));

  public static final NodeId MaxCharacters = new NodeId(UShort.MIN, uint(15002));

  public static final NodeId ServerType_UrisVersion = new NodeId(UShort.MIN, uint(15003));

  public static final NodeId Server_UrisVersion = new NodeId(UShort.MIN, uint(15004));

  public static final NodeId SimpleTypeDescription = new NodeId(UShort.MIN, uint(15005));

  public static final NodeId UABinaryFileDataType = new NodeId(UShort.MIN, uint(15006));

  public static final NodeId BrokerConnectionTransportDataType =
      new NodeId(UShort.MIN, uint(15007));

  public static final NodeId BrokerTransportQualityOfService = new NodeId(UShort.MIN, uint(15008));

  public static final NodeId BrokerTransportQualityOfService_EnumStrings =
      new NodeId(UShort.MIN, uint(15009));

  public static final NodeId SecurityGroupFolderType_SecurityGroupName_Placeholder_KeyLifetime =
      new NodeId(UShort.MIN, uint(15010));

  public static final NodeId
      SecurityGroupFolderType_SecurityGroupName_Placeholder_SecurityPolicyUri =
          new NodeId(UShort.MIN, uint(15011));

  public static final NodeId
      SecurityGroupFolderType_SecurityGroupName_Placeholder_MaxFutureKeyCount =
          new NodeId(UShort.MIN, uint(15012));

  public static final NodeId AuditConditionResetEventType = new NodeId(UShort.MIN, uint(15013));

  public static final NodeId AuditConditionResetEventType_EventId =
      new NodeId(UShort.MIN, uint(15014));

  public static final NodeId AuditConditionResetEventType_EventType =
      new NodeId(UShort.MIN, uint(15015));

  public static final NodeId AuditConditionResetEventType_SourceNode =
      new NodeId(UShort.MIN, uint(15016));

  public static final NodeId AuditConditionResetEventType_SourceName =
      new NodeId(UShort.MIN, uint(15017));

  public static final NodeId AuditConditionResetEventType_Time =
      new NodeId(UShort.MIN, uint(15018));

  public static final NodeId AuditConditionResetEventType_ReceiveTime =
      new NodeId(UShort.MIN, uint(15019));

  public static final NodeId AuditConditionResetEventType_LocalTime =
      new NodeId(UShort.MIN, uint(15020));

  public static final NodeId AuditConditionResetEventType_Message =
      new NodeId(UShort.MIN, uint(15021));

  public static final NodeId AuditConditionResetEventType_Severity =
      new NodeId(UShort.MIN, uint(15022));

  public static final NodeId AuditConditionResetEventType_ActionTimeStamp =
      new NodeId(UShort.MIN, uint(15023));

  public static final NodeId AuditConditionResetEventType_Status =
      new NodeId(UShort.MIN, uint(15024));

  public static final NodeId AuditConditionResetEventType_ServerId =
      new NodeId(UShort.MIN, uint(15025));

  public static final NodeId AuditConditionResetEventType_ClientAuditEntryId =
      new NodeId(UShort.MIN, uint(15026));

  public static final NodeId AuditConditionResetEventType_ClientUserId =
      new NodeId(UShort.MIN, uint(15027));

  public static final NodeId AuditConditionResetEventType_MethodId =
      new NodeId(UShort.MIN, uint(15028));

  public static final NodeId AuditConditionResetEventType_InputArguments =
      new NodeId(UShort.MIN, uint(15029));

  public static final NodeId PermissionType_OptionSetValues = new NodeId(UShort.MIN, uint(15030));

  public static final NodeId AccessLevelType = new NodeId(UShort.MIN, uint(15031));

  public static final NodeId AccessLevelType_OptionSetValues = new NodeId(UShort.MIN, uint(15032));

  public static final NodeId EventNotifierType = new NodeId(UShort.MIN, uint(15033));

  public static final NodeId EventNotifierType_OptionSetValues =
      new NodeId(UShort.MIN, uint(15034));

  public static final NodeId AccessRestrictionType_OptionSetValues =
      new NodeId(UShort.MIN, uint(15035));

  public static final NodeId AttributeWriteMask_OptionSetValues =
      new NodeId(UShort.MIN, uint(15036));

  public static final NodeId OpcUa_BinarySchema_Deprecated = new NodeId(UShort.MIN, uint(15037));

  public static final NodeId ProgramStateMachineType_ProgramDiagnostic_LastMethodInputValues =
      new NodeId(UShort.MIN, uint(15038));

  public static final NodeId OpcUa_XmlSchema_Deprecated = new NodeId(UShort.MIN, uint(15039));

  public static final NodeId ProgramStateMachineType_ProgramDiagnostic_LastMethodOutputValues =
      new NodeId(UShort.MIN, uint(15040));

  public static final NodeId KeyValuePair_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15041));

  public static final NodeId IdentityMappingRuleType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15042));

  public static final NodeId SecurityGroupFolderType_SecurityGroupName_Placeholder_MaxPastKeyCount =
      new NodeId(UShort.MIN, uint(15043));

  public static final NodeId TrustListDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15044));

  public static final NodeId DecimalDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15045));

  public static final NodeId SecurityGroupType_KeyLifetime = new NodeId(UShort.MIN, uint(15046));

  public static final NodeId SecurityGroupType_SecurityPolicyUri =
      new NodeId(UShort.MIN, uint(15047));

  public static final NodeId SecurityGroupType_MaxFutureKeyCount =
      new NodeId(UShort.MIN, uint(15048));

  public static final NodeId ConfigurationVersionDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15049));

  public static final NodeId DataSetMetaDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15050));

  public static final NodeId FieldMetaData_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15051));

  public static final NodeId PublishedEventsType_ModifyFieldSelection =
      new NodeId(UShort.MIN, uint(15052));

  public static final NodeId PublishedEventsType_ModifyFieldSelection_InputArguments =
      new NodeId(UShort.MIN, uint(15053));

  public static final NodeId PublishedEventsTypeModifyFieldSelectionMethodType =
      new NodeId(UShort.MIN, uint(15054));

  public static final NodeId PublishedEventsTypeModifyFieldSelectionMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(15055));

  public static final NodeId SecurityGroupType_MaxPastKeyCount =
      new NodeId(UShort.MIN, uint(15056));

  public static final NodeId DataTypeDescription_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15057));

  public static final NodeId StructureDescription_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15058));

  public static final NodeId EnumDescription_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15059));

  public static final NodeId PublishedVariableDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15060));

  public static final NodeId FieldTargetDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15061));

  public static final NodeId RolePermissionType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15062));

  public static final NodeId DataTypeDefinition_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15063));

  public static final NodeId DatagramConnectionTransportType = new NodeId(UShort.MIN, uint(15064));

  public static final NodeId StructureField_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15065));

  public static final NodeId StructureDefinition_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15066));

  public static final NodeId EnumDefinition_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15067));

  public static final NodeId Node_Encoding_DefaultJson = new NodeId(UShort.MIN, uint(15068));

  public static final NodeId InstanceNode_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15069));

  public static final NodeId TypeNode_Encoding_DefaultJson = new NodeId(UShort.MIN, uint(15070));

  public static final NodeId ObjectNode_Encoding_DefaultJson = new NodeId(UShort.MIN, uint(15071));

  public static final NodeId DatagramConnectionTransportType_DiscoveryAddress =
      new NodeId(UShort.MIN, uint(15072));

  public static final NodeId ObjectTypeNode_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15073));

  public static final NodeId VariableNode_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15074));

  public static final NodeId VariableTypeNode_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15075));

  public static final NodeId ReferenceTypeNode_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15076));

  public static final NodeId MethodNode_Encoding_DefaultJson = new NodeId(UShort.MIN, uint(15077));

  public static final NodeId ViewNode_Encoding_DefaultJson = new NodeId(UShort.MIN, uint(15078));

  public static final NodeId DataTypeNode_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15079));

  public static final NodeId ReferenceNode_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15080));

  public static final NodeId Argument_Encoding_DefaultJson = new NodeId(UShort.MIN, uint(15081));

  public static final NodeId EnumValueType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15082));

  public static final NodeId EnumField_Encoding_DefaultJson = new NodeId(UShort.MIN, uint(15083));

  public static final NodeId OptionSet_Encoding_DefaultJson = new NodeId(UShort.MIN, uint(15084));

  public static final NodeId Union_Encoding_DefaultJson = new NodeId(UShort.MIN, uint(15085));

  public static final NodeId TimeZoneDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15086));

  public static final NodeId ApplicationDescription_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15087));

  public static final NodeId RequestHeader_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15088));

  public static final NodeId ResponseHeader_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15089));

  public static final NodeId ServiceFault_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15090));

  public static final NodeId SessionlessInvokeRequestType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15091));

  public static final NodeId SessionlessInvokeResponseType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15092));

  public static final NodeId FindServersRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15093));

  public static final NodeId FindServersResponse_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15094));

  public static final NodeId ServerOnNetwork_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15095));

  public static final NodeId FindServersOnNetworkRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15096));

  public static final NodeId FindServersOnNetworkResponse_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15097));

  public static final NodeId UserTokenPolicy_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15098));

  public static final NodeId EndpointDescription_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15099));

  public static final NodeId GetEndpointsRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15100));

  public static final NodeId GetEndpointsResponse_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15101));

  public static final NodeId RegisteredServer_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15102));

  public static final NodeId RegisterServerRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15103));

  public static final NodeId RegisterServerResponse_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15104));

  public static final NodeId DiscoveryConfiguration_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15105));

  public static final NodeId MdnsDiscoveryConfiguration_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15106));

  public static final NodeId RegisterServer2Request_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15107));

  public static final NodeId SubscribedDataSetType = new NodeId(UShort.MIN, uint(15108));

  public static final NodeId ChoiceStateType = new NodeId(UShort.MIN, uint(15109));

  public static final NodeId ChoiceStateType_StateNumber = new NodeId(UShort.MIN, uint(15110));

  public static final NodeId TargetVariablesType = new NodeId(UShort.MIN, uint(15111));

  public static final NodeId HasGuard = new NodeId(UShort.MIN, uint(15112));

  public static final NodeId GuardVariableType = new NodeId(UShort.MIN, uint(15113));

  public static final NodeId TargetVariablesType_TargetVariables =
      new NodeId(UShort.MIN, uint(15114));

  public static final NodeId TargetVariablesType_AddTargetVariables =
      new NodeId(UShort.MIN, uint(15115));

  public static final NodeId TargetVariablesType_AddTargetVariables_InputArguments =
      new NodeId(UShort.MIN, uint(15116));

  public static final NodeId TargetVariablesType_AddTargetVariables_OutputArguments =
      new NodeId(UShort.MIN, uint(15117));

  public static final NodeId TargetVariablesType_RemoveTargetVariables =
      new NodeId(UShort.MIN, uint(15118));

  public static final NodeId TargetVariablesType_RemoveTargetVariables_InputArguments =
      new NodeId(UShort.MIN, uint(15119));

  public static final NodeId TargetVariablesType_RemoveTargetVariables_OutputArguments =
      new NodeId(UShort.MIN, uint(15120));

  public static final NodeId TargetVariablesTypeAddTargetVariablesMethodType =
      new NodeId(UShort.MIN, uint(15121));

  public static final NodeId TargetVariablesTypeAddTargetVariablesMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(15122));

  public static final NodeId TargetVariablesTypeAddTargetVariablesMethodType_OutputArguments =
      new NodeId(UShort.MIN, uint(15123));

  public static final NodeId TargetVariablesTypeRemoveTargetVariablesMethodType =
      new NodeId(UShort.MIN, uint(15124));

  public static final NodeId TargetVariablesTypeRemoveTargetVariablesMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(15125));

  public static final NodeId TargetVariablesTypeRemoveTargetVariablesMethodType_OutputArguments =
      new NodeId(UShort.MIN, uint(15126));

  public static final NodeId SubscribedDataSetMirrorType = new NodeId(UShort.MIN, uint(15127));

  public static final NodeId ExpressionGuardVariableType = new NodeId(UShort.MIN, uint(15128));

  public static final NodeId ExpressionGuardVariableType_Expression =
      new NodeId(UShort.MIN, uint(15129));

  public static final NodeId RegisterServer2Response_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15130));

  public static final NodeId ChannelSecurityToken_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15131));

  public static final NodeId OpenSecureChannelRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15132));

  public static final NodeId OpenSecureChannelResponse_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15133));

  public static final NodeId CloseSecureChannelRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15134));

  public static final NodeId CloseSecureChannelResponse_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15135));

  public static final NodeId SignedSoftwareCertificate_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15136));

  public static final NodeId SignatureData_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15137));

  public static final NodeId CreateSessionRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15138));

  public static final NodeId CreateSessionResponse_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15139));

  public static final NodeId UserIdentityToken_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15140));

  public static final NodeId AnonymousIdentityToken_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15141));

  public static final NodeId UserNameIdentityToken_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15142));

  public static final NodeId X509IdentityToken_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15143));

  public static final NodeId IssuedIdentityToken_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15144));

  public static final NodeId ActivateSessionRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15145));

  public static final NodeId ActivateSessionResponse_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15146));

  public static final NodeId CloseSessionRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15147));

  public static final NodeId CloseSessionResponse_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15148));

  public static final NodeId CancelRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15149));

  public static final NodeId CancelResponse_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15150));

  public static final NodeId NodeAttributes_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15151));

  public static final NodeId ObjectAttributes_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15152));

  public static final NodeId VariableAttributes_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15153));

  public static final NodeId DatagramConnectionTransportType_DiscoveryAddress_NetworkInterface =
      new NodeId(UShort.MIN, uint(15154));

  public static final NodeId BrokerConnectionTransportType = new NodeId(UShort.MIN, uint(15155));

  public static final NodeId BrokerConnectionTransportType_ResourceUri =
      new NodeId(UShort.MIN, uint(15156));

  public static final NodeId MethodAttributes_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15157));

  public static final NodeId ObjectTypeAttributes_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15158));

  public static final NodeId VariableTypeAttributes_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15159));

  public static final NodeId ReferenceTypeAttributes_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15160));

  public static final NodeId DataTypeAttributes_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15161));

  public static final NodeId ViewAttributes_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15162));

  public static final NodeId GenericAttributeValue_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15163));

  public static final NodeId GenericAttributes_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15164));

  public static final NodeId AddNodesItem_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15165));

  public static final NodeId AddNodesResult_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15166));

  public static final NodeId AddNodesRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15167));

  public static final NodeId AddNodesResponse_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15168));

  public static final NodeId AddReferencesItem_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15169));

  public static final NodeId AddReferencesRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15170));

  public static final NodeId AddReferencesResponse_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15171));

  public static final NodeId DeleteNodesItem_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15172));

  public static final NodeId DeleteNodesRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15173));

  public static final NodeId DeleteNodesResponse_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15174));

  public static final NodeId DeleteReferencesItem_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15175));

  public static final NodeId DeleteReferencesRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15176));

  public static final NodeId DeleteReferencesResponse_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15177));

  public static final NodeId BrokerConnectionTransportType_AuthenticationProfileUri =
      new NodeId(UShort.MIN, uint(15178));

  public static final NodeId ViewDescription_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15179));

  public static final NodeId BrowseDescription_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15180));

  public static final NodeId UserCredentialCertificateType = new NodeId(UShort.MIN, uint(15181));

  public static final NodeId ReferenceDescription_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15182));

  public static final NodeId BrowseResult_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15183));

  public static final NodeId BrowseRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15184));

  public static final NodeId BrowseResponse_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15185));

  public static final NodeId BrowseNextRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15186));

  public static final NodeId BrowseNextResponse_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15187));

  public static final NodeId RelativePathElement_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15188));

  public static final NodeId RelativePath_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15189));

  public static final NodeId BrowsePath_Encoding_DefaultJson = new NodeId(UShort.MIN, uint(15190));

  public static final NodeId BrowsePathTarget_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15191));

  public static final NodeId BrowsePathResult_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15192));

  public static final NodeId TranslateBrowsePathsToNodeIdsRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15193));

  public static final NodeId TranslateBrowsePathsToNodeIdsResponse_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15194));

  public static final NodeId RegisterNodesRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15195));

  public static final NodeId RegisterNodesResponse_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15196));

  public static final NodeId UnregisterNodesRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15197));

  public static final NodeId UnregisterNodesResponse_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15198));

  public static final NodeId EndpointConfiguration_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15199));

  public static final NodeId QueryDataDescription_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15200));

  public static final NodeId NodeTypeDescription_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15201));

  public static final NodeId QueryDataSet_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15202));

  public static final NodeId NodeReference_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15203));

  public static final NodeId ContentFilterElement_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15204));

  public static final NodeId ContentFilter_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15205));

  public static final NodeId FilterOperand_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15206));

  public static final NodeId ElementOperand_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15207));

  public static final NodeId LiteralOperand_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15208));

  public static final NodeId AttributeOperand_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15209));

  public static final NodeId SimpleAttributeOperand_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15210));

  public static final NodeId ContentFilterElementResult_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15211));

  public static final NodeId PublishSubscribeType_GetSecurityKeys =
      new NodeId(UShort.MIN, uint(15212));

  public static final NodeId PublishSubscribeType_GetSecurityKeys_InputArguments =
      new NodeId(UShort.MIN, uint(15213));

  public static final NodeId PublishSubscribeType_GetSecurityKeys_OutputArguments =
      new NodeId(UShort.MIN, uint(15214));

  public static final NodeId PublishSubscribe_GetSecurityKeys = new NodeId(UShort.MIN, uint(15215));

  public static final NodeId PublishSubscribe_GetSecurityKeys_InputArguments =
      new NodeId(UShort.MIN, uint(15216));

  public static final NodeId PublishSubscribe_GetSecurityKeys_OutputArguments =
      new NodeId(UShort.MIN, uint(15217));

  public static final NodeId GetSecurityKeysMethodType = new NodeId(UShort.MIN, uint(15218));

  public static final NodeId GetSecurityKeysMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(15219));

  public static final NodeId GetSecurityKeysMethodType_OutputArguments =
      new NodeId(UShort.MIN, uint(15220));

  public static final NodeId DataSetFolderType_PublishedDataSetName_Placeholder_DataSetMetaData =
      new NodeId(UShort.MIN, uint(15221));

  public static final NodeId PublishedDataSetType_DataSetWriterName_Placeholder =
      new NodeId(UShort.MIN, uint(15222));

  public static final NodeId PublishedDataSetType_DataSetWriterName_Placeholder_Status =
      new NodeId(UShort.MIN, uint(15223));

  public static final NodeId PublishedDataSetType_DataSetWriterName_Placeholder_Status_State =
      new NodeId(UShort.MIN, uint(15224));

  public static final NodeId PublishedDataSetType_DataSetWriterName_Placeholder_Status_Enable =
      new NodeId(UShort.MIN, uint(15225));

  public static final NodeId PublishedDataSetType_DataSetWriterName_Placeholder_Status_Disable =
      new NodeId(UShort.MIN, uint(15226));

  public static final NodeId PublishedDataSetType_DataSetWriterName_Placeholder_TransportSettings =
      new NodeId(UShort.MIN, uint(15227));

  public static final NodeId ContentFilterResult_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15228));

  public static final NodeId PublishedDataSetType_DataSetMetaData =
      new NodeId(UShort.MIN, uint(15229));

  public static final NodeId PublishedDataItemsType_DataSetWriterName_Placeholder =
      new NodeId(UShort.MIN, uint(15230));

  public static final NodeId PublishedDataItemsType_DataSetWriterName_Placeholder_Status =
      new NodeId(UShort.MIN, uint(15231));

  public static final NodeId PublishedDataItemsType_DataSetWriterName_Placeholder_Status_State =
      new NodeId(UShort.MIN, uint(15232));

  public static final NodeId PublishedDataItemsType_DataSetWriterName_Placeholder_Status_Enable =
      new NodeId(UShort.MIN, uint(15233));

  public static final NodeId PublishedDataItemsType_DataSetWriterName_Placeholder_Status_Disable =
      new NodeId(UShort.MIN, uint(15234));

  public static final NodeId
      PublishedDataItemsType_DataSetWriterName_Placeholder_TransportSettings =
          new NodeId(UShort.MIN, uint(15235));

  public static final NodeId ParsingResult_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15236));

  public static final NodeId PublishedDataItemsType_DataSetMetaData =
      new NodeId(UShort.MIN, uint(15237));

  public static final NodeId PublishedEventsType_DataSetWriterName_Placeholder =
      new NodeId(UShort.MIN, uint(15238));

  public static final NodeId PublishedEventsType_DataSetWriterName_Placeholder_Status =
      new NodeId(UShort.MIN, uint(15239));

  public static final NodeId PublishedEventsType_DataSetWriterName_Placeholder_Status_State =
      new NodeId(UShort.MIN, uint(15240));

  public static final NodeId PublishedEventsType_DataSetWriterName_Placeholder_Status_Enable =
      new NodeId(UShort.MIN, uint(15241));

  public static final NodeId PublishedEventsType_DataSetWriterName_Placeholder_Status_Disable =
      new NodeId(UShort.MIN, uint(15242));

  public static final NodeId PublishedEventsType_DataSetWriterName_Placeholder_TransportSettings =
      new NodeId(UShort.MIN, uint(15243));

  public static final NodeId QueryFirstRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15244));

  public static final NodeId PublishedEventsType_DataSetMetaData =
      new NodeId(UShort.MIN, uint(15245));

  public static final NodeId BrokerWriterGroupTransportType_ResourceUri =
      new NodeId(UShort.MIN, uint(15246));

  public static final NodeId BrokerWriterGroupTransportType_AuthenticationProfileUri =
      new NodeId(UShort.MIN, uint(15247));

  public static final NodeId CreateCredentialMethodType = new NodeId(UShort.MIN, uint(15248));

  public static final NodeId BrokerWriterGroupTransportType_RequestedDeliveryGuarantee =
      new NodeId(UShort.MIN, uint(15249));

  public static final NodeId BrokerDataSetWriterTransportType_ResourceUri =
      new NodeId(UShort.MIN, uint(15250));

  public static final NodeId BrokerDataSetWriterTransportType_AuthenticationProfileUri =
      new NodeId(UShort.MIN, uint(15251));

  public static final NodeId QueryFirstResponse_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15252));

  public static final NodeId CreateCredentialMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(15253));

  public static final NodeId QueryNextRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15254));

  public static final NodeId QueryNextResponse_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15255));

  public static final NodeId ReadValueId_Encoding_DefaultJson = new NodeId(UShort.MIN, uint(15256));

  public static final NodeId ReadRequest_Encoding_DefaultJson = new NodeId(UShort.MIN, uint(15257));

  public static final NodeId ReadResponse_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15258));

  public static final NodeId HistoryReadValueId_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15259));

  public static final NodeId HistoryReadResult_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15260));

  public static final NodeId HistoryReadDetails_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15261));

  public static final NodeId ReadEventDetails_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15262));

  public static final NodeId ReadRawModifiedDetails_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15263));

  public static final NodeId ReadProcessedDetails_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15264));

  public static final NodeId PubSubGroupType_Status = new NodeId(UShort.MIN, uint(15265));

  public static final NodeId PubSubGroupType_Status_State = new NodeId(UShort.MIN, uint(15266));

  public static final NodeId PubSubGroupType_Status_Enable = new NodeId(UShort.MIN, uint(15267));

  public static final NodeId PubSubGroupType_Status_Disable = new NodeId(UShort.MIN, uint(15268));

  public static final NodeId ReadAtTimeDetails_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15269));

  public static final NodeId HistoryData_Encoding_DefaultJson = new NodeId(UShort.MIN, uint(15270));

  public static final NodeId ModificationInfo_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15271));

  public static final NodeId HistoryModifiedData_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15272));

  public static final NodeId HistoryEvent_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15273));

  public static final NodeId HistoryReadRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15274));

  public static final NodeId HistoryReadResponse_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15275));

  public static final NodeId WriteValue_Encoding_DefaultJson = new NodeId(UShort.MIN, uint(15276));

  public static final NodeId WriteRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15277));

  public static final NodeId WriteResponse_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15278));

  public static final NodeId HistoryUpdateDetails_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15279));

  public static final NodeId UpdateDataDetails_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15280));

  public static final NodeId UpdateStructureDataDetails_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15281));

  public static final NodeId UpdateEventDetails_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15282));

  public static final NodeId DeleteRawModifiedDetails_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15283));

  public static final NodeId DeleteAtTimeDetails_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15284));

  public static final NodeId DeleteEventDetails_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15285));

  public static final NodeId HistoryUpdateResult_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15286));

  public static final NodeId HistoryUpdateRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15287));

  public static final NodeId HistoryUpdateResponse_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15288));

  public static final NodeId CallMethodRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15289));

  public static final NodeId CallMethodResult_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15290));

  public static final NodeId CallRequest_Encoding_DefaultJson = new NodeId(UShort.MIN, uint(15291));

  public static final NodeId CallResponse_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15292));

  public static final NodeId MonitoringFilter_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15293));

  public static final NodeId DataChangeFilter_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15294));

  public static final NodeId EventFilter_Encoding_DefaultJson = new NodeId(UShort.MIN, uint(15295));

  public static final NodeId HasDataSetWriter = new NodeId(UShort.MIN, uint(15296));

  public static final NodeId HasDataSetReader = new NodeId(UShort.MIN, uint(15297));

  public static final NodeId DataSetWriterType = new NodeId(UShort.MIN, uint(15298));

  public static final NodeId DataSetWriterType_Status = new NodeId(UShort.MIN, uint(15299));

  public static final NodeId DataSetWriterType_Status_State = new NodeId(UShort.MIN, uint(15300));

  public static final NodeId DataSetWriterType_Status_Enable = new NodeId(UShort.MIN, uint(15301));

  public static final NodeId DataSetWriterType_Status_Disable = new NodeId(UShort.MIN, uint(15302));

  public static final NodeId DataSetWriterType_TransportSettings =
      new NodeId(UShort.MIN, uint(15303));

  public static final NodeId AggregateConfiguration_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15304));

  public static final NodeId DataSetWriterTransportType = new NodeId(UShort.MIN, uint(15305));

  public static final NodeId DataSetReaderType = new NodeId(UShort.MIN, uint(15306));

  public static final NodeId DataSetReaderType_Status = new NodeId(UShort.MIN, uint(15307));

  public static final NodeId DataSetReaderType_Status_State = new NodeId(UShort.MIN, uint(15308));

  public static final NodeId DataSetReaderType_Status_Enable = new NodeId(UShort.MIN, uint(15309));

  public static final NodeId DataSetReaderType_Status_Disable = new NodeId(UShort.MIN, uint(15310));

  public static final NodeId DataSetReaderType_TransportSettings =
      new NodeId(UShort.MIN, uint(15311));

  public static final NodeId AggregateFilter_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15312));

  public static final NodeId MonitoringFilterResult_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15313));

  public static final NodeId EventFilterResult_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15314));

  public static final NodeId AggregateFilterResult_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15315));

  public static final NodeId DataSetReaderType_SubscribedDataSet =
      new NodeId(UShort.MIN, uint(15316));

  public static final NodeId ElseGuardVariableType = new NodeId(UShort.MIN, uint(15317));

  public static final NodeId BaseAnalogType = new NodeId(UShort.MIN, uint(15318));

  public static final NodeId DataSetReaderTransportType = new NodeId(UShort.MIN, uint(15319));

  public static final NodeId MonitoringParameters_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15320));

  public static final NodeId MonitoredItemCreateRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15321));

  public static final NodeId MonitoredItemCreateResult_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15322));

  public static final NodeId CreateMonitoredItemsRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15323));

  public static final NodeId CreateMonitoredItemsResponse_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15324));

  public static final NodeId MonitoredItemModifyRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15325));

  public static final NodeId MonitoredItemModifyResult_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15326));

  public static final NodeId ModifyMonitoredItemsRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15327));

  public static final NodeId ModifyMonitoredItemsResponse_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15328));

  public static final NodeId SetMonitoringModeRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15329));

  public static final NodeId BrokerDataSetWriterTransportType_RequestedDeliveryGuarantee =
      new NodeId(UShort.MIN, uint(15330));

  public static final NodeId SetMonitoringModeResponse_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15331));

  public static final NodeId SetTriggeringRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15332));

  public static final NodeId SetTriggeringResponse_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15333));

  public static final NodeId BrokerDataSetReaderTransportType_ResourceUri =
      new NodeId(UShort.MIN, uint(15334));

  public static final NodeId DeleteMonitoredItemsRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15335));

  public static final NodeId DeleteMonitoredItemsResponse_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15336));

  public static final NodeId CreateSubscriptionRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15337));

  public static final NodeId CreateSubscriptionResponse_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15338));

  public static final NodeId ModifySubscriptionRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15339));

  public static final NodeId ModifySubscriptionResponse_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15340));

  public static final NodeId SetPublishingModeRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15341));

  public static final NodeId SetPublishingModeResponse_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15342));

  public static final NodeId NotificationMessage_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15343));

  public static final NodeId NotificationData_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15344));

  public static final NodeId DataChangeNotification_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15345));

  public static final NodeId MonitoredItemNotification_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15346));

  public static final NodeId EventNotificationList_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15347));

  public static final NodeId EventFieldList_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15348));

  public static final NodeId HistoryEventFieldList_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15349));

  public static final NodeId StatusChangeNotification_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15350));

  public static final NodeId SubscriptionAcknowledgement_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15351));

  public static final NodeId PublishRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15352));

  public static final NodeId PublishResponse_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15353));

  public static final NodeId RepublishRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15354));

  public static final NodeId RepublishResponse_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15355));

  public static final NodeId TransferResult_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15356));

  public static final NodeId TransferSubscriptionsRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15357));

  public static final NodeId TransferSubscriptionsResponse_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15358));

  public static final NodeId DeleteSubscriptionsRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15359));

  public static final NodeId DeleteSubscriptionsResponse_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15360));

  public static final NodeId BuildInfo_Encoding_DefaultJson = new NodeId(UShort.MIN, uint(15361));

  public static final NodeId RedundantServerDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15362));

  public static final NodeId EndpointUrlListDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15363));

  public static final NodeId NetworkGroupDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15364));

  public static final NodeId SamplingIntervalDiagnosticsDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15365));

  public static final NodeId ServerDiagnosticsSummaryDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15366));

  public static final NodeId ServerStatusDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15367));

  public static final NodeId SessionDiagnosticsDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15368));

  public static final NodeId SessionSecurityDiagnosticsDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15369));

  public static final NodeId ServiceCounterDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15370));

  public static final NodeId StatusResult_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15371));

  public static final NodeId SubscriptionDiagnosticsDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15372));

  public static final NodeId ModelChangeStructureDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15373));

  public static final NodeId SemanticChangeStructureDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15374));

  public static final NodeId Range_Encoding_DefaultJson = new NodeId(UShort.MIN, uint(15375));

  public static final NodeId EUInformation_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15376));

  public static final NodeId ComplexNumberType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15377));

  public static final NodeId DoubleComplexNumberType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15378));

  public static final NodeId AxisInformation_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15379));

  public static final NodeId XVType_Encoding_DefaultJson = new NodeId(UShort.MIN, uint(15380));

  public static final NodeId ProgramDiagnosticDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15381));

  public static final NodeId Annotation_Encoding_DefaultJson = new NodeId(UShort.MIN, uint(15382));

  public static final NodeId ProgramDiagnostic2Type = new NodeId(UShort.MIN, uint(15383));

  public static final NodeId ProgramDiagnostic2Type_CreateSessionId =
      new NodeId(UShort.MIN, uint(15384));

  public static final NodeId ProgramDiagnostic2Type_CreateClientName =
      new NodeId(UShort.MIN, uint(15385));

  public static final NodeId ProgramDiagnostic2Type_InvocationCreationTime =
      new NodeId(UShort.MIN, uint(15386));

  public static final NodeId ProgramDiagnostic2Type_LastTransitionTime =
      new NodeId(UShort.MIN, uint(15387));

  public static final NodeId ProgramDiagnostic2Type_LastMethodCall =
      new NodeId(UShort.MIN, uint(15388));

  public static final NodeId ProgramDiagnostic2Type_LastMethodSessionId =
      new NodeId(UShort.MIN, uint(15389));

  public static final NodeId ProgramDiagnostic2Type_LastMethodInputArguments =
      new NodeId(UShort.MIN, uint(15390));

  public static final NodeId ProgramDiagnostic2Type_LastMethodOutputArguments =
      new NodeId(UShort.MIN, uint(15391));

  public static final NodeId ProgramDiagnostic2Type_LastMethodInputValues =
      new NodeId(UShort.MIN, uint(15392));

  public static final NodeId ProgramDiagnostic2Type_LastMethodOutputValues =
      new NodeId(UShort.MIN, uint(15393));

  public static final NodeId ProgramDiagnostic2Type_LastMethodCallTime =
      new NodeId(UShort.MIN, uint(15394));

  public static final NodeId ProgramDiagnostic2Type_LastMethodReturnStatus =
      new NodeId(UShort.MIN, uint(15395));

  public static final NodeId AccessLevelExType = new NodeId(UShort.MIN, uint(15406));

  public static final NodeId AccessLevelExType_OptionSetValues =
      new NodeId(UShort.MIN, uint(15407));

  public static final NodeId RoleSetType_RoleName_Placeholder_ApplicationsExclude =
      new NodeId(UShort.MIN, uint(15408));

  public static final NodeId RoleSetType_RoleName_Placeholder_EndpointsExclude =
      new NodeId(UShort.MIN, uint(15409));

  public static final NodeId RoleType_ApplicationsExclude = new NodeId(UShort.MIN, uint(15410));

  public static final NodeId RoleType_EndpointsExclude = new NodeId(UShort.MIN, uint(15411));

  public static final NodeId WellKnownRole_Anonymous_ApplicationsExclude =
      new NodeId(UShort.MIN, uint(15412));

  public static final NodeId WellKnownRole_Anonymous_EndpointsExclude =
      new NodeId(UShort.MIN, uint(15413));

  public static final NodeId WellKnownRole_AuthenticatedUser_ApplicationsExclude =
      new NodeId(UShort.MIN, uint(15414));

  public static final NodeId WellKnownRole_AuthenticatedUser_EndpointsExclude =
      new NodeId(UShort.MIN, uint(15415));

  public static final NodeId WellKnownRole_Observer_ApplicationsExclude =
      new NodeId(UShort.MIN, uint(15416));

  public static final NodeId WellKnownRole_Observer_EndpointsExclude =
      new NodeId(UShort.MIN, uint(15417));

  public static final NodeId WellKnownRole_Operator_ApplicationsExclude =
      new NodeId(UShort.MIN, uint(15418));

  public static final NodeId BrokerDataSetReaderTransportType_AuthenticationProfileUri =
      new NodeId(UShort.MIN, uint(15419));

  public static final NodeId BrokerDataSetReaderTransportType_RequestedDeliveryGuarantee =
      new NodeId(UShort.MIN, uint(15420));

  public static final NodeId SimpleTypeDescription_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(15421));

  public static final NodeId UABinaryFileDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(15422));

  public static final NodeId WellKnownRole_Operator_EndpointsExclude =
      new NodeId(UShort.MIN, uint(15423));

  public static final NodeId WellKnownRole_Engineer_ApplicationsExclude =
      new NodeId(UShort.MIN, uint(15424));

  public static final NodeId WellKnownRole_Engineer_EndpointsExclude =
      new NodeId(UShort.MIN, uint(15425));

  public static final NodeId WellKnownRole_Supervisor_ApplicationsExclude =
      new NodeId(UShort.MIN, uint(15426));

  public static final NodeId WellKnownRole_Supervisor_EndpointsExclude =
      new NodeId(UShort.MIN, uint(15427));

  public static final NodeId WellKnownRole_ConfigureAdmin_ApplicationsExclude =
      new NodeId(UShort.MIN, uint(15428));

  public static final NodeId WellKnownRole_ConfigureAdmin_EndpointsExclude =
      new NodeId(UShort.MIN, uint(15429));

  public static final NodeId WellKnownRole_SecurityAdmin_ApplicationsExclude =
      new NodeId(UShort.MIN, uint(15430));

  public static final NodeId PublishSubscribeType_GetSecurityGroup =
      new NodeId(UShort.MIN, uint(15431));

  public static final NodeId PublishSubscribeType_GetSecurityGroup_InputArguments =
      new NodeId(UShort.MIN, uint(15432));

  public static final NodeId PublishSubscribeType_GetSecurityGroup_OutputArguments =
      new NodeId(UShort.MIN, uint(15433));

  public static final NodeId PublishSubscribeType_SecurityGroups =
      new NodeId(UShort.MIN, uint(15434));

  public static final NodeId PublishSubscribeType_SecurityGroups_AddSecurityGroup =
      new NodeId(UShort.MIN, uint(15435));

  public static final NodeId PublishSubscribeType_SecurityGroups_AddSecurityGroup_InputArguments =
      new NodeId(UShort.MIN, uint(15436));

  public static final NodeId PublishSubscribeType_SecurityGroups_AddSecurityGroup_OutputArguments =
      new NodeId(UShort.MIN, uint(15437));

  public static final NodeId PublishSubscribeType_SecurityGroups_RemoveSecurityGroup =
      new NodeId(UShort.MIN, uint(15438));

  public static final NodeId
      PublishSubscribeType_SecurityGroups_RemoveSecurityGroup_InputArguments =
          new NodeId(UShort.MIN, uint(15439));

  public static final NodeId PublishSubscribe_GetSecurityGroup =
      new NodeId(UShort.MIN, uint(15440));

  public static final NodeId PublishSubscribe_GetSecurityGroup_InputArguments =
      new NodeId(UShort.MIN, uint(15441));

  public static final NodeId PublishSubscribe_GetSecurityGroup_OutputArguments =
      new NodeId(UShort.MIN, uint(15442));

  public static final NodeId PublishSubscribe_SecurityGroups = new NodeId(UShort.MIN, uint(15443));

  public static final NodeId PublishSubscribe_SecurityGroups_AddSecurityGroup =
      new NodeId(UShort.MIN, uint(15444));

  public static final NodeId PublishSubscribe_SecurityGroups_AddSecurityGroup_InputArguments =
      new NodeId(UShort.MIN, uint(15445));

  public static final NodeId PublishSubscribe_SecurityGroups_AddSecurityGroup_OutputArguments =
      new NodeId(UShort.MIN, uint(15446));

  public static final NodeId PublishSubscribe_SecurityGroups_RemoveSecurityGroup =
      new NodeId(UShort.MIN, uint(15447));

  public static final NodeId PublishSubscribe_SecurityGroups_RemoveSecurityGroup_InputArguments =
      new NodeId(UShort.MIN, uint(15448));

  public static final NodeId GetSecurityGroupMethodType = new NodeId(UShort.MIN, uint(15449));

  public static final NodeId GetSecurityGroupMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(15450));

  public static final NodeId GetSecurityGroupMethodType_OutputArguments =
      new NodeId(UShort.MIN, uint(15451));

  public static final NodeId SecurityGroupFolderType = new NodeId(UShort.MIN, uint(15452));

  public static final NodeId SecurityGroupFolderType_SecurityGroupFolderName_Placeholder =
      new NodeId(UShort.MIN, uint(15453));

  public static final NodeId
      SecurityGroupFolderType_SecurityGroupFolderName_Placeholder_AddSecurityGroup =
          new NodeId(UShort.MIN, uint(15454));

  public static final NodeId
      SecurityGroupFolderType_SecurityGroupFolderName_Placeholder_AddSecurityGroup_InputArguments =
          new NodeId(UShort.MIN, uint(15455));

  public static final NodeId
      SecurityGroupFolderType_SecurityGroupFolderName_Placeholder_AddSecurityGroup_OutputArguments =
          new NodeId(UShort.MIN, uint(15456));

  public static final NodeId
      SecurityGroupFolderType_SecurityGroupFolderName_Placeholder_RemoveSecurityGroup =
          new NodeId(UShort.MIN, uint(15457));

  public static final NodeId
      SecurityGroupFolderType_SecurityGroupFolderName_Placeholder_RemoveSecurityGroup_InputArguments =
          new NodeId(UShort.MIN, uint(15458));

  public static final NodeId SecurityGroupFolderType_SecurityGroupName_Placeholder =
      new NodeId(UShort.MIN, uint(15459));

  public static final NodeId SecurityGroupFolderType_SecurityGroupName_Placeholder_SecurityGroupId =
      new NodeId(UShort.MIN, uint(15460));

  public static final NodeId SecurityGroupFolderType_AddSecurityGroup =
      new NodeId(UShort.MIN, uint(15461));

  public static final NodeId SecurityGroupFolderType_AddSecurityGroup_InputArguments =
      new NodeId(UShort.MIN, uint(15462));

  public static final NodeId SecurityGroupFolderType_AddSecurityGroup_OutputArguments =
      new NodeId(UShort.MIN, uint(15463));

  public static final NodeId SecurityGroupFolderType_RemoveSecurityGroup =
      new NodeId(UShort.MIN, uint(15464));

  public static final NodeId SecurityGroupFolderType_RemoveSecurityGroup_InputArguments =
      new NodeId(UShort.MIN, uint(15465));

  public static final NodeId AddSecurityGroupMethodType = new NodeId(UShort.MIN, uint(15466));

  public static final NodeId AddSecurityGroupMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(15467));

  public static final NodeId AddSecurityGroupMethodType_OutputArguments =
      new NodeId(UShort.MIN, uint(15468));

  public static final NodeId RemoveSecurityGroupMethodType = new NodeId(UShort.MIN, uint(15469));

  public static final NodeId RemoveSecurityGroupMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(15470));

  public static final NodeId SecurityGroupType = new NodeId(UShort.MIN, uint(15471));

  public static final NodeId SecurityGroupType_SecurityGroupId =
      new NodeId(UShort.MIN, uint(15472));

  public static final NodeId DataSetFolderType_PublishedDataSetName_Placeholder_ExtensionFields =
      new NodeId(UShort.MIN, uint(15473));

  public static final NodeId
      DataSetFolderType_PublishedDataSetName_Placeholder_ExtensionFields_AddExtensionField =
          new NodeId(UShort.MIN, uint(15474));

  public static final NodeId
      DataSetFolderType_PublishedDataSetName_Placeholder_ExtensionFields_AddExtensionField_InputArguments =
          new NodeId(UShort.MIN, uint(15475));

  public static final NodeId
      DataSetFolderType_PublishedDataSetName_Placeholder_ExtensionFields_AddExtensionField_OutputArguments =
          new NodeId(UShort.MIN, uint(15476));

  public static final NodeId
      DataSetFolderType_PublishedDataSetName_Placeholder_ExtensionFields_RemoveExtensionField =
          new NodeId(UShort.MIN, uint(15477));

  public static final NodeId
      DataSetFolderType_PublishedDataSetName_Placeholder_ExtensionFields_RemoveExtensionField_InputArguments =
          new NodeId(UShort.MIN, uint(15478));

  public static final NodeId BrokerConnectionTransportDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(15479));

  public static final NodeId WriterGroupDataType = new NodeId(UShort.MIN, uint(15480));

  public static final NodeId PublishedDataSetType_ExtensionFields =
      new NodeId(UShort.MIN, uint(15481));

  public static final NodeId PublishedDataSetType_ExtensionFields_AddExtensionField =
      new NodeId(UShort.MIN, uint(15482));

  public static final NodeId PublishedDataSetType_ExtensionFields_AddExtensionField_InputArguments =
      new NodeId(UShort.MIN, uint(15483));

  public static final NodeId
      PublishedDataSetType_ExtensionFields_AddExtensionField_OutputArguments =
          new NodeId(UShort.MIN, uint(15484));

  public static final NodeId PublishedDataSetType_ExtensionFields_RemoveExtensionField =
      new NodeId(UShort.MIN, uint(15485));

  public static final NodeId
      PublishedDataSetType_ExtensionFields_RemoveExtensionField_InputArguments =
          new NodeId(UShort.MIN, uint(15486));

  public static final NodeId StructureDescription = new NodeId(UShort.MIN, uint(15487));

  public static final NodeId EnumDescription = new NodeId(UShort.MIN, uint(15488));

  public static final NodeId ExtensionFieldsType = new NodeId(UShort.MIN, uint(15489));

  public static final NodeId ExtensionFieldsType_ExtensionFieldName_Placeholder =
      new NodeId(UShort.MIN, uint(15490));

  public static final NodeId ExtensionFieldsType_AddExtensionField =
      new NodeId(UShort.MIN, uint(15491));

  public static final NodeId ExtensionFieldsType_AddExtensionField_InputArguments =
      new NodeId(UShort.MIN, uint(15492));

  public static final NodeId ExtensionFieldsType_AddExtensionField_OutputArguments =
      new NodeId(UShort.MIN, uint(15493));

  public static final NodeId ExtensionFieldsType_RemoveExtensionField =
      new NodeId(UShort.MIN, uint(15494));

  public static final NodeId ExtensionFieldsType_RemoveExtensionField_InputArguments =
      new NodeId(UShort.MIN, uint(15495));

  public static final NodeId AddExtensionFieldMethodType = new NodeId(UShort.MIN, uint(15496));

  public static final NodeId AddExtensionFieldMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(15497));

  public static final NodeId AddExtensionFieldMethodType_OutputArguments =
      new NodeId(UShort.MIN, uint(15498));

  public static final NodeId RemoveExtensionFieldMethodType = new NodeId(UShort.MIN, uint(15499));

  public static final NodeId RemoveExtensionFieldMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(15500));

  public static final NodeId OpcUa_BinarySchema_SimpleTypeDescription =
      new NodeId(UShort.MIN, uint(15501));

  public static final NodeId NetworkAddressDataType = new NodeId(UShort.MIN, uint(15502));

  public static final NodeId PublishedDataItemsType_ExtensionFields =
      new NodeId(UShort.MIN, uint(15503));

  public static final NodeId PublishedDataItemsType_ExtensionFields_AddExtensionField =
      new NodeId(UShort.MIN, uint(15504));

  public static final NodeId
      PublishedDataItemsType_ExtensionFields_AddExtensionField_InputArguments =
          new NodeId(UShort.MIN, uint(15505));

  public static final NodeId
      PublishedDataItemsType_ExtensionFields_AddExtensionField_OutputArguments =
          new NodeId(UShort.MIN, uint(15506));

  public static final NodeId PublishedDataItemsType_ExtensionFields_RemoveExtensionField =
      new NodeId(UShort.MIN, uint(15507));

  public static final NodeId
      PublishedDataItemsType_ExtensionFields_RemoveExtensionField_InputArguments =
          new NodeId(UShort.MIN, uint(15508));

  public static final NodeId OpcUa_BinarySchema_SimpleTypeDescription_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15509));

  public static final NodeId NetworkAddressUrlDataType = new NodeId(UShort.MIN, uint(15510));

  public static final NodeId PublishedEventsType_ExtensionFields =
      new NodeId(UShort.MIN, uint(15511));

  public static final NodeId PublishedEventsType_ExtensionFields_AddExtensionField =
      new NodeId(UShort.MIN, uint(15512));

  public static final NodeId PublishedEventsType_ExtensionFields_AddExtensionField_InputArguments =
      new NodeId(UShort.MIN, uint(15513));

  public static final NodeId PublishedEventsType_ExtensionFields_AddExtensionField_OutputArguments =
      new NodeId(UShort.MIN, uint(15514));

  public static final NodeId PublishedEventsType_ExtensionFields_RemoveExtensionField =
      new NodeId(UShort.MIN, uint(15515));

  public static final NodeId
      PublishedEventsType_ExtensionFields_RemoveExtensionField_InputArguments =
          new NodeId(UShort.MIN, uint(15516));

  public static final NodeId PublishedEventsType_ModifyFieldSelection_OutputArguments =
      new NodeId(UShort.MIN, uint(15517));

  public static final NodeId PublishedEventsTypeModifyFieldSelectionMethodType_OutputArguments =
      new NodeId(UShort.MIN, uint(15518));

  public static final NodeId OpcUa_BinarySchema_SimpleTypeDescription_DictionaryFragment =
      new NodeId(UShort.MIN, uint(15519));

  public static final NodeId ReaderGroupDataType = new NodeId(UShort.MIN, uint(15520));

  public static final NodeId OpcUa_BinarySchema_UABinaryFileDataType =
      new NodeId(UShort.MIN, uint(15521));

  public static final NodeId OpcUa_BinarySchema_UABinaryFileDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15522));

  public static final NodeId OpcUa_BinarySchema_UABinaryFileDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(15523));

  public static final NodeId OpcUa_BinarySchema_BrokerConnectionTransportDataType =
      new NodeId(UShort.MIN, uint(15524));

  public static final NodeId OpcUa_BinarySchema_BrokerConnectionTransportDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15525));

  public static final NodeId
      OpcUa_BinarySchema_BrokerConnectionTransportDataType_DictionaryFragment =
          new NodeId(UShort.MIN, uint(15526));

  public static final NodeId WellKnownRole_SecurityAdmin_EndpointsExclude =
      new NodeId(UShort.MIN, uint(15527));

  public static final NodeId EndpointType = new NodeId(UShort.MIN, uint(15528));

  public static final NodeId SimpleTypeDescription_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(15529));

  public static final NodeId PubSubConfigurationDataType = new NodeId(UShort.MIN, uint(15530));

  public static final NodeId UABinaryFileDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(15531));

  public static final NodeId DatagramWriterGroupTransportDataType =
      new NodeId(UShort.MIN, uint(15532));

  public static final NodeId
      PublishSubscribeType_ConnectionName_Placeholder_Address_NetworkInterface =
          new NodeId(UShort.MIN, uint(15533));

  public static final NodeId DataTypeSchemaHeader = new NodeId(UShort.MIN, uint(15534));

  public static final NodeId PubSubStatusEventType = new NodeId(UShort.MIN, uint(15535));

  public static final NodeId PubSubStatusEventType_EventId = new NodeId(UShort.MIN, uint(15536));

  public static final NodeId PubSubStatusEventType_EventType = new NodeId(UShort.MIN, uint(15537));

  public static final NodeId PubSubStatusEventType_SourceNode = new NodeId(UShort.MIN, uint(15538));

  public static final NodeId PubSubStatusEventType_SourceName = new NodeId(UShort.MIN, uint(15539));

  public static final NodeId PubSubStatusEventType_Time = new NodeId(UShort.MIN, uint(15540));

  public static final NodeId PubSubStatusEventType_ReceiveTime =
      new NodeId(UShort.MIN, uint(15541));

  public static final NodeId PubSubStatusEventType_LocalTime = new NodeId(UShort.MIN, uint(15542));

  public static final NodeId PubSubStatusEventType_Message = new NodeId(UShort.MIN, uint(15543));

  public static final NodeId PubSubStatusEventType_Severity = new NodeId(UShort.MIN, uint(15544));

  public static final NodeId PubSubStatusEventType_ConnectionId =
      new NodeId(UShort.MIN, uint(15545));

  public static final NodeId PubSubStatusEventType_GroupId = new NodeId(UShort.MIN, uint(15546));

  public static final NodeId PubSubStatusEventType_State = new NodeId(UShort.MIN, uint(15547));

  public static final NodeId PubSubTransportLimitsExceedEventType =
      new NodeId(UShort.MIN, uint(15548));

  public static final NodeId PubSubTransportLimitsExceedEventType_EventId =
      new NodeId(UShort.MIN, uint(15549));

  public static final NodeId PubSubTransportLimitsExceedEventType_EventType =
      new NodeId(UShort.MIN, uint(15550));

  public static final NodeId PubSubTransportLimitsExceedEventType_SourceNode =
      new NodeId(UShort.MIN, uint(15551));

  public static final NodeId PubSubTransportLimitsExceedEventType_SourceName =
      new NodeId(UShort.MIN, uint(15552));

  public static final NodeId PubSubTransportLimitsExceedEventType_Time =
      new NodeId(UShort.MIN, uint(15553));

  public static final NodeId PubSubTransportLimitsExceedEventType_ReceiveTime =
      new NodeId(UShort.MIN, uint(15554));

  public static final NodeId PubSubTransportLimitsExceedEventType_LocalTime =
      new NodeId(UShort.MIN, uint(15555));

  public static final NodeId PubSubTransportLimitsExceedEventType_Message =
      new NodeId(UShort.MIN, uint(15556));

  public static final NodeId PubSubTransportLimitsExceedEventType_Severity =
      new NodeId(UShort.MIN, uint(15557));

  public static final NodeId PubSubTransportLimitsExceedEventType_ConnectionId =
      new NodeId(UShort.MIN, uint(15558));

  public static final NodeId PubSubTransportLimitsExceedEventType_GroupId =
      new NodeId(UShort.MIN, uint(15559));

  public static final NodeId PubSubTransportLimitsExceedEventType_State =
      new NodeId(UShort.MIN, uint(15560));

  public static final NodeId PubSubTransportLimitsExceedEventType_Actual =
      new NodeId(UShort.MIN, uint(15561));

  public static final NodeId PubSubTransportLimitsExceedEventType_Maximum =
      new NodeId(UShort.MIN, uint(15562));

  public static final NodeId PubSubCommunicationFailureEventType =
      new NodeId(UShort.MIN, uint(15563));

  public static final NodeId PubSubCommunicationFailureEventType_EventId =
      new NodeId(UShort.MIN, uint(15564));

  public static final NodeId PubSubCommunicationFailureEventType_EventType =
      new NodeId(UShort.MIN, uint(15565));

  public static final NodeId PubSubCommunicationFailureEventType_SourceNode =
      new NodeId(UShort.MIN, uint(15566));

  public static final NodeId PubSubCommunicationFailureEventType_SourceName =
      new NodeId(UShort.MIN, uint(15567));

  public static final NodeId PubSubCommunicationFailureEventType_Time =
      new NodeId(UShort.MIN, uint(15568));

  public static final NodeId PubSubCommunicationFailureEventType_ReceiveTime =
      new NodeId(UShort.MIN, uint(15569));

  public static final NodeId PubSubCommunicationFailureEventType_LocalTime =
      new NodeId(UShort.MIN, uint(15570));

  public static final NodeId PubSubCommunicationFailureEventType_Message =
      new NodeId(UShort.MIN, uint(15571));

  public static final NodeId PubSubCommunicationFailureEventType_Severity =
      new NodeId(UShort.MIN, uint(15572));

  public static final NodeId PubSubCommunicationFailureEventType_ConnectionId =
      new NodeId(UShort.MIN, uint(15573));

  public static final NodeId PubSubCommunicationFailureEventType_GroupId =
      new NodeId(UShort.MIN, uint(15574));

  public static final NodeId PubSubCommunicationFailureEventType_State =
      new NodeId(UShort.MIN, uint(15575));

  public static final NodeId PubSubCommunicationFailureEventType_Error =
      new NodeId(UShort.MIN, uint(15576));

  public static final NodeId DataSetFieldFlags_OptionSetValues =
      new NodeId(UShort.MIN, uint(15577));

  public static final NodeId PublishedDataSetDataType = new NodeId(UShort.MIN, uint(15578));

  public static final NodeId BrokerConnectionTransportDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(15579));

  public static final NodeId PublishedDataSetSourceDataType = new NodeId(UShort.MIN, uint(15580));

  public static final NodeId PublishedDataItemsDataType = new NodeId(UShort.MIN, uint(15581));

  public static final NodeId PublishedEventsDataType = new NodeId(UShort.MIN, uint(15582));

  public static final NodeId DataSetFieldContentMask = new NodeId(UShort.MIN, uint(15583));

  public static final NodeId DataSetFieldContentMask_OptionSetValues =
      new NodeId(UShort.MIN, uint(15584));

  public static final NodeId OpcUa_XmlSchema_SimpleTypeDescription =
      new NodeId(UShort.MIN, uint(15585));

  public static final NodeId OpcUa_XmlSchema_SimpleTypeDescription_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15586));

  public static final NodeId OpcUa_XmlSchema_SimpleTypeDescription_DictionaryFragment =
      new NodeId(UShort.MIN, uint(15587));

  public static final NodeId OpcUa_XmlSchema_UABinaryFileDataType =
      new NodeId(UShort.MIN, uint(15588));

  public static final NodeId StructureDescription_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(15589));

  public static final NodeId EnumDescription_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(15590));

  public static final NodeId OpcUa_XmlSchema_StructureDescription =
      new NodeId(UShort.MIN, uint(15591));

  public static final NodeId OpcUa_XmlSchema_StructureDescription_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15592));

  public static final NodeId OpcUa_XmlSchema_StructureDescription_DictionaryFragment =
      new NodeId(UShort.MIN, uint(15593));

  public static final NodeId OpcUa_XmlSchema_EnumDescription = new NodeId(UShort.MIN, uint(15594));

  public static final NodeId OpcUa_XmlSchema_EnumDescription_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15595));

  public static final NodeId OpcUa_XmlSchema_EnumDescription_DictionaryFragment =
      new NodeId(UShort.MIN, uint(15596));

  public static final NodeId DataSetWriterDataType = new NodeId(UShort.MIN, uint(15597));

  public static final NodeId DataSetWriterTransportDataType = new NodeId(UShort.MIN, uint(15598));

  public static final NodeId OpcUa_BinarySchema_StructureDescription =
      new NodeId(UShort.MIN, uint(15599));

  public static final NodeId OpcUa_BinarySchema_StructureDescription_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15600));

  public static final NodeId OpcUa_BinarySchema_StructureDescription_DictionaryFragment =
      new NodeId(UShort.MIN, uint(15601));

  public static final NodeId OpcUa_BinarySchema_EnumDescription =
      new NodeId(UShort.MIN, uint(15602));

  public static final NodeId OpcUa_BinarySchema_EnumDescription_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15603));

  public static final NodeId OpcUa_BinarySchema_EnumDescription_DictionaryFragment =
      new NodeId(UShort.MIN, uint(15604));

  public static final NodeId DataSetWriterMessageDataType = new NodeId(UShort.MIN, uint(15605));

  public static final NodeId Server_ServerCapabilities_RoleSet =
      new NodeId(UShort.MIN, uint(15606));

  public static final NodeId RoleSetType = new NodeId(UShort.MIN, uint(15607));

  public static final NodeId RoleSetType_RoleName_Placeholder = new NodeId(UShort.MIN, uint(15608));

  public static final NodeId PubSubGroupDataType = new NodeId(UShort.MIN, uint(15609));

  public static final NodeId OpcUa_XmlSchema_UABinaryFileDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15610));

  public static final NodeId WriterGroupTransportDataType = new NodeId(UShort.MIN, uint(15611));

  public static final NodeId RoleSetType_RoleName_Placeholder_AddIdentity =
      new NodeId(UShort.MIN, uint(15612));

  public static final NodeId RoleSetType_RoleName_Placeholder_AddIdentity_InputArguments =
      new NodeId(UShort.MIN, uint(15613));

  public static final NodeId RoleSetType_RoleName_Placeholder_RemoveIdentity =
      new NodeId(UShort.MIN, uint(15614));

  public static final NodeId RoleSetType_RoleName_Placeholder_RemoveIdentity_InputArguments =
      new NodeId(UShort.MIN, uint(15615));

  public static final NodeId WriterGroupMessageDataType = new NodeId(UShort.MIN, uint(15616));

  public static final NodeId PubSubConnectionDataType = new NodeId(UShort.MIN, uint(15617));

  public static final NodeId ConnectionTransportDataType = new NodeId(UShort.MIN, uint(15618));

  public static final NodeId OpcUa_XmlSchema_UABinaryFileDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(15619));

  public static final NodeId RoleType = new NodeId(UShort.MIN, uint(15620));

  public static final NodeId ReaderGroupTransportDataType = new NodeId(UShort.MIN, uint(15621));

  public static final NodeId ReaderGroupMessageDataType = new NodeId(UShort.MIN, uint(15622));

  public static final NodeId DataSetReaderDataType = new NodeId(UShort.MIN, uint(15623));

  public static final NodeId RoleType_AddIdentity = new NodeId(UShort.MIN, uint(15624));

  public static final NodeId RoleType_AddIdentity_InputArguments =
      new NodeId(UShort.MIN, uint(15625));

  public static final NodeId RoleType_RemoveIdentity = new NodeId(UShort.MIN, uint(15626));

  public static final NodeId RoleType_RemoveIdentity_InputArguments =
      new NodeId(UShort.MIN, uint(15627));

  public static final NodeId DataSetReaderTransportDataType = new NodeId(UShort.MIN, uint(15628));

  public static final NodeId DataSetReaderMessageDataType = new NodeId(UShort.MIN, uint(15629));

  public static final NodeId SubscribedDataSetDataType = new NodeId(UShort.MIN, uint(15630));

  public static final NodeId TargetVariablesDataType = new NodeId(UShort.MIN, uint(15631));

  public static final NodeId IdentityCriteriaType = new NodeId(UShort.MIN, uint(15632));

  public static final NodeId IdentityCriteriaType_EnumValues = new NodeId(UShort.MIN, uint(15633));

  public static final NodeId IdentityMappingRuleType = new NodeId(UShort.MIN, uint(15634));

  public static final NodeId SubscribedDataSetMirrorDataType = new NodeId(UShort.MIN, uint(15635));

  public static final NodeId AddIdentityMethodType = new NodeId(UShort.MIN, uint(15636));

  public static final NodeId AddIdentityMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(15637));

  public static final NodeId RemoveIdentityMethodType = new NodeId(UShort.MIN, uint(15638));

  public static final NodeId RemoveIdentityMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(15639));

  public static final NodeId OpcUa_XmlSchema_BrokerConnectionTransportDataType =
      new NodeId(UShort.MIN, uint(15640));

  public static final NodeId DataSetOrderingType_EnumStrings = new NodeId(UShort.MIN, uint(15641));

  public static final NodeId UadpNetworkMessageContentMask = new NodeId(UShort.MIN, uint(15642));

  public static final NodeId UadpNetworkMessageContentMask_OptionSetValues =
      new NodeId(UShort.MIN, uint(15643));

  public static final NodeId WellKnownRole_Anonymous = new NodeId(UShort.MIN, uint(15644));

  public static final NodeId UadpWriterGroupMessageDataType = new NodeId(UShort.MIN, uint(15645));

  public static final NodeId UadpDataSetMessageContentMask = new NodeId(UShort.MIN, uint(15646));

  public static final NodeId UadpDataSetMessageContentMask_OptionSetValues =
      new NodeId(UShort.MIN, uint(15647));

  public static final NodeId WellKnownRole_Anonymous_AddIdentity =
      new NodeId(UShort.MIN, uint(15648));

  public static final NodeId WellKnownRole_Anonymous_AddIdentity_InputArguments =
      new NodeId(UShort.MIN, uint(15649));

  public static final NodeId WellKnownRole_Anonymous_RemoveIdentity =
      new NodeId(UShort.MIN, uint(15650));

  public static final NodeId WellKnownRole_Anonymous_RemoveIdentity_InputArguments =
      new NodeId(UShort.MIN, uint(15651));

  public static final NodeId UadpDataSetWriterMessageDataType = new NodeId(UShort.MIN, uint(15652));

  public static final NodeId UadpDataSetReaderMessageDataType = new NodeId(UShort.MIN, uint(15653));

  public static final NodeId JsonNetworkMessageContentMask = new NodeId(UShort.MIN, uint(15654));

  public static final NodeId JsonNetworkMessageContentMask_OptionSetValues =
      new NodeId(UShort.MIN, uint(15655));

  public static final NodeId WellKnownRole_AuthenticatedUser = new NodeId(UShort.MIN, uint(15656));

  public static final NodeId JsonWriterGroupMessageDataType = new NodeId(UShort.MIN, uint(15657));

  public static final NodeId JsonDataSetMessageContentMask = new NodeId(UShort.MIN, uint(15658));

  public static final NodeId JsonDataSetMessageContentMask_OptionSetValues =
      new NodeId(UShort.MIN, uint(15659));

  public static final NodeId WellKnownRole_AuthenticatedUser_AddIdentity =
      new NodeId(UShort.MIN, uint(15660));

  public static final NodeId WellKnownRole_AuthenticatedUser_AddIdentity_InputArguments =
      new NodeId(UShort.MIN, uint(15661));

  public static final NodeId WellKnownRole_AuthenticatedUser_RemoveIdentity =
      new NodeId(UShort.MIN, uint(15662));

  public static final NodeId WellKnownRole_AuthenticatedUser_RemoveIdentity_InputArguments =
      new NodeId(UShort.MIN, uint(15663));

  public static final NodeId JsonDataSetWriterMessageDataType = new NodeId(UShort.MIN, uint(15664));

  public static final NodeId JsonDataSetReaderMessageDataType = new NodeId(UShort.MIN, uint(15665));

  public static final NodeId OpcUa_XmlSchema_BrokerConnectionTransportDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15666));

  public static final NodeId BrokerWriterGroupTransportDataType =
      new NodeId(UShort.MIN, uint(15667));

  public static final NodeId WellKnownRole_Observer = new NodeId(UShort.MIN, uint(15668));

  public static final NodeId BrokerDataSetWriterTransportDataType =
      new NodeId(UShort.MIN, uint(15669));

  public static final NodeId BrokerDataSetReaderTransportDataType =
      new NodeId(UShort.MIN, uint(15670));

  public static final NodeId EndpointType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(15671));

  public static final NodeId WellKnownRole_Observer_AddIdentity =
      new NodeId(UShort.MIN, uint(15672));

  public static final NodeId WellKnownRole_Observer_AddIdentity_InputArguments =
      new NodeId(UShort.MIN, uint(15673));

  public static final NodeId WellKnownRole_Observer_RemoveIdentity =
      new NodeId(UShort.MIN, uint(15674));

  public static final NodeId WellKnownRole_Observer_RemoveIdentity_InputArguments =
      new NodeId(UShort.MIN, uint(15675));

  public static final NodeId DataTypeSchemaHeader_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(15676));

  public static final NodeId PublishedDataSetDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(15677));

  public static final NodeId PublishedDataSetSourceDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(15678));

  public static final NodeId PublishedDataItemsDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(15679));

  public static final NodeId WellKnownRole_Operator = new NodeId(UShort.MIN, uint(15680));

  public static final NodeId PublishedEventsDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(15681));

  public static final NodeId DataSetWriterDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(15682));

  public static final NodeId DataSetWriterTransportDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(15683));

  public static final NodeId WellKnownRole_Operator_AddIdentity =
      new NodeId(UShort.MIN, uint(15684));

  public static final NodeId WellKnownRole_Operator_AddIdentity_InputArguments =
      new NodeId(UShort.MIN, uint(15685));

  public static final NodeId WellKnownRole_Operator_RemoveIdentity =
      new NodeId(UShort.MIN, uint(15686));

  public static final NodeId WellKnownRole_Operator_RemoveIdentity_InputArguments =
      new NodeId(UShort.MIN, uint(15687));

  public static final NodeId DataSetWriterMessageDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(15688));

  public static final NodeId PubSubGroupDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(15689));

  public static final NodeId OpcUa_XmlSchema_BrokerConnectionTransportDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(15690));

  public static final NodeId WriterGroupTransportDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(15691));

  public static final NodeId WellKnownRole_Supervisor = new NodeId(UShort.MIN, uint(15692));

  public static final NodeId WriterGroupMessageDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(15693));

  public static final NodeId PubSubConnectionDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(15694));

  public static final NodeId ConnectionTransportDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(15695));

  public static final NodeId WellKnownRole_Supervisor_AddIdentity =
      new NodeId(UShort.MIN, uint(15696));

  public static final NodeId WellKnownRole_Supervisor_AddIdentity_InputArguments =
      new NodeId(UShort.MIN, uint(15697));

  public static final NodeId WellKnownRole_Supervisor_RemoveIdentity =
      new NodeId(UShort.MIN, uint(15698));

  public static final NodeId WellKnownRole_Supervisor_RemoveIdentity_InputArguments =
      new NodeId(UShort.MIN, uint(15699));

  public static final NodeId SimpleTypeDescription_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15700));

  public static final NodeId ReaderGroupTransportDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(15701));

  public static final NodeId ReaderGroupMessageDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(15702));

  public static final NodeId DataSetReaderDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(15703));

  public static final NodeId WellKnownRole_SecurityAdmin = new NodeId(UShort.MIN, uint(15704));

  public static final NodeId DataSetReaderTransportDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(15705));

  public static final NodeId DataSetReaderMessageDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(15706));

  public static final NodeId SubscribedDataSetDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(15707));

  public static final NodeId WellKnownRole_SecurityAdmin_AddIdentity =
      new NodeId(UShort.MIN, uint(15708));

  public static final NodeId WellKnownRole_SecurityAdmin_AddIdentity_InputArguments =
      new NodeId(UShort.MIN, uint(15709));

  public static final NodeId WellKnownRole_SecurityAdmin_RemoveIdentity =
      new NodeId(UShort.MIN, uint(15710));

  public static final NodeId WellKnownRole_SecurityAdmin_RemoveIdentity_InputArguments =
      new NodeId(UShort.MIN, uint(15711));

  public static final NodeId TargetVariablesDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(15712));

  public static final NodeId SubscribedDataSetMirrorDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(15713));

  public static final NodeId UABinaryFileDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15714));

  public static final NodeId UadpWriterGroupMessageDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(15715));

  public static final NodeId WellKnownRole_ConfigureAdmin = new NodeId(UShort.MIN, uint(15716));

  public static final NodeId UadpDataSetWriterMessageDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(15717));

  public static final NodeId UadpDataSetReaderMessageDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(15718));

  public static final NodeId JsonWriterGroupMessageDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(15719));

  public static final NodeId WellKnownRole_ConfigureAdmin_AddIdentity =
      new NodeId(UShort.MIN, uint(15720));

  public static final NodeId WellKnownRole_ConfigureAdmin_AddIdentity_InputArguments =
      new NodeId(UShort.MIN, uint(15721));

  public static final NodeId WellKnownRole_ConfigureAdmin_RemoveIdentity =
      new NodeId(UShort.MIN, uint(15722));

  public static final NodeId WellKnownRole_ConfigureAdmin_RemoveIdentity_InputArguments =
      new NodeId(UShort.MIN, uint(15723));

  public static final NodeId JsonDataSetWriterMessageDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(15724));

  public static final NodeId JsonDataSetReaderMessageDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(15725));

  public static final NodeId BrokerConnectionTransportDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15726));

  public static final NodeId BrokerWriterGroupTransportDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(15727));

  public static final NodeId IdentityMappingRuleType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(15728));

  public static final NodeId BrokerDataSetWriterTransportDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(15729));

  public static final NodeId OpcUa_XmlSchema_IdentityMappingRuleType =
      new NodeId(UShort.MIN, uint(15730));

  public static final NodeId OpcUa_XmlSchema_IdentityMappingRuleType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15731));

  public static final NodeId OpcUa_XmlSchema_IdentityMappingRuleType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(15732));

  public static final NodeId BrokerDataSetReaderTransportDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(15733));

  public static final NodeId OpcUa_BinarySchema_EndpointType = new NodeId(UShort.MIN, uint(15734));

  public static final NodeId OpcUa_BinarySchema_EndpointType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15735));

  public static final NodeId IdentityMappingRuleType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(15736));

  public static final NodeId OpcUa_BinarySchema_EndpointType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(15737));

  public static final NodeId OpcUa_BinarySchema_IdentityMappingRuleType =
      new NodeId(UShort.MIN, uint(15738));

  public static final NodeId OpcUa_BinarySchema_IdentityMappingRuleType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15739));

  public static final NodeId OpcUa_BinarySchema_IdentityMappingRuleType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(15740));

  public static final NodeId OpcUa_BinarySchema_DataTypeSchemaHeader =
      new NodeId(UShort.MIN, uint(15741));

  public static final NodeId OpcUa_BinarySchema_DataTypeSchemaHeader_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15742));

  public static final NodeId OpcUa_BinarySchema_DataTypeSchemaHeader_DictionaryFragment =
      new NodeId(UShort.MIN, uint(15743));

  public static final NodeId TemporaryFileTransferType = new NodeId(UShort.MIN, uint(15744));

  public static final NodeId TemporaryFileTransferType_ClientProcessingTimeout =
      new NodeId(UShort.MIN, uint(15745));

  public static final NodeId TemporaryFileTransferType_GenerateFileForRead =
      new NodeId(UShort.MIN, uint(15746));

  public static final NodeId TemporaryFileTransferType_GenerateFileForRead_InputArguments =
      new NodeId(UShort.MIN, uint(15747));

  public static final NodeId TemporaryFileTransferType_GenerateFileForRead_OutputArguments =
      new NodeId(UShort.MIN, uint(15748));

  public static final NodeId TemporaryFileTransferType_GenerateFileForWrite =
      new NodeId(UShort.MIN, uint(15749));

  public static final NodeId TemporaryFileTransferType_GenerateFileForWrite_OutputArguments =
      new NodeId(UShort.MIN, uint(15750));

  public static final NodeId TemporaryFileTransferType_CloseAndCommit =
      new NodeId(UShort.MIN, uint(15751));

  public static final NodeId TemporaryFileTransferType_CloseAndCommit_InputArguments =
      new NodeId(UShort.MIN, uint(15752));

  public static final NodeId TemporaryFileTransferType_CloseAndCommit_OutputArguments =
      new NodeId(UShort.MIN, uint(15753));

  public static final NodeId TemporaryFileTransferType_TransferState_Placeholder =
      new NodeId(UShort.MIN, uint(15754));

  public static final NodeId TemporaryFileTransferType_TransferState_Placeholder_CurrentState =
      new NodeId(UShort.MIN, uint(15755));

  public static final NodeId TemporaryFileTransferType_TransferState_Placeholder_CurrentState_Id =
      new NodeId(UShort.MIN, uint(15756));

  public static final NodeId TemporaryFileTransferType_TransferState_Placeholder_CurrentState_Name =
      new NodeId(UShort.MIN, uint(15757));

  public static final NodeId
      TemporaryFileTransferType_TransferState_Placeholder_CurrentState_Number =
          new NodeId(UShort.MIN, uint(15758));

  public static final NodeId
      TemporaryFileTransferType_TransferState_Placeholder_CurrentState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(15759));

  public static final NodeId TemporaryFileTransferType_TransferState_Placeholder_LastTransition =
      new NodeId(UShort.MIN, uint(15760));

  public static final NodeId TemporaryFileTransferType_TransferState_Placeholder_LastTransition_Id =
      new NodeId(UShort.MIN, uint(15761));

  public static final NodeId
      TemporaryFileTransferType_TransferState_Placeholder_LastTransition_Name =
          new NodeId(UShort.MIN, uint(15762));

  public static final NodeId
      TemporaryFileTransferType_TransferState_Placeholder_LastTransition_Number =
          new NodeId(UShort.MIN, uint(15763));

  public static final NodeId
      TemporaryFileTransferType_TransferState_Placeholder_LastTransition_TransitionTime =
          new NodeId(UShort.MIN, uint(15764));

  public static final NodeId
      TemporaryFileTransferType_TransferState_Placeholder_LastTransition_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(15765));

  public static final NodeId OpcUa_BinarySchema_PublishedDataSetDataType =
      new NodeId(UShort.MIN, uint(15766));

  public static final NodeId OpcUa_BinarySchema_PublishedDataSetDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15767));

  public static final NodeId OpcUa_BinarySchema_PublishedDataSetDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(15768));

  public static final NodeId OpcUa_BinarySchema_PublishedDataSetSourceDataType =
      new NodeId(UShort.MIN, uint(15769));

  public static final NodeId OpcUa_BinarySchema_PublishedDataSetSourceDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15770));

  public static final NodeId OpcUa_BinarySchema_PublishedDataSetSourceDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(15771));

  public static final NodeId OpcUa_BinarySchema_PublishedDataItemsDataType =
      new NodeId(UShort.MIN, uint(15772));

  public static final NodeId OpcUa_BinarySchema_PublishedDataItemsDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15773));

  public static final NodeId OpcUa_BinarySchema_PublishedDataItemsDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(15774));

  public static final NodeId OpcUa_BinarySchema_PublishedEventsDataType =
      new NodeId(UShort.MIN, uint(15775));

  public static final NodeId OpcUa_BinarySchema_PublishedEventsDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15776));

  public static final NodeId OpcUa_BinarySchema_PublishedEventsDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(15777));

  public static final NodeId OpcUa_BinarySchema_DataSetWriterDataType =
      new NodeId(UShort.MIN, uint(15778));

  public static final NodeId OpcUa_BinarySchema_DataSetWriterDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15779));

  public static final NodeId OpcUa_BinarySchema_DataSetWriterDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(15780));

  public static final NodeId OpcUa_BinarySchema_DataSetWriterTransportDataType =
      new NodeId(UShort.MIN, uint(15781));

  public static final NodeId OpcUa_BinarySchema_DataSetWriterTransportDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15782));

  public static final NodeId OpcUa_BinarySchema_DataSetWriterTransportDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(15783));

  public static final NodeId OpcUa_BinarySchema_DataSetWriterMessageDataType =
      new NodeId(UShort.MIN, uint(15784));

  public static final NodeId OpcUa_BinarySchema_DataSetWriterMessageDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15785));

  public static final NodeId OpcUa_BinarySchema_DataSetWriterMessageDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(15786));

  public static final NodeId OpcUa_BinarySchema_PubSubGroupDataType =
      new NodeId(UShort.MIN, uint(15787));

  public static final NodeId OpcUa_BinarySchema_PubSubGroupDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15788));

  public static final NodeId OpcUa_BinarySchema_PubSubGroupDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(15789));

  public static final NodeId PublishSubscribe_ConnectionName_Placeholder =
      new NodeId(UShort.MIN, uint(15790));

  public static final NodeId PublishSubscribe_ConnectionName_Placeholder_PublisherId =
      new NodeId(UShort.MIN, uint(15791));

  public static final NodeId PublishSubscribe_ConnectionName_Placeholder_TransportProfileUri =
      new NodeId(UShort.MIN, uint(15792));

  public static final NodeId OpcUa_BinarySchema_WriterGroupTransportDataType =
      new NodeId(UShort.MIN, uint(15793));

  public static final NodeId TemporaryFileTransferType_TransferState_Placeholder_Reset =
      new NodeId(UShort.MIN, uint(15794));

  public static final NodeId GenerateFileForReadMethodType = new NodeId(UShort.MIN, uint(15795));

  public static final NodeId GenerateFileForReadMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(15796));

  public static final NodeId GenerateFileForReadMethodType_OutputArguments =
      new NodeId(UShort.MIN, uint(15797));

  public static final NodeId GenerateFileForWriteMethodType = new NodeId(UShort.MIN, uint(15798));

  public static final NodeId GenerateFileForWriteMethodType_OutputArguments =
      new NodeId(UShort.MIN, uint(15799));

  public static final NodeId CloseAndCommitMethodType = new NodeId(UShort.MIN, uint(15800));

  public static final NodeId CloseAndCommitMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(15801));

  public static final NodeId CloseAndCommitMethodType_OutputArguments =
      new NodeId(UShort.MIN, uint(15802));

  public static final NodeId FileTransferStateMachineType = new NodeId(UShort.MIN, uint(15803));

  public static final NodeId FileTransferStateMachineType_CurrentState =
      new NodeId(UShort.MIN, uint(15804));

  public static final NodeId FileTransferStateMachineType_CurrentState_Id =
      new NodeId(UShort.MIN, uint(15805));

  public static final NodeId FileTransferStateMachineType_CurrentState_Name =
      new NodeId(UShort.MIN, uint(15806));

  public static final NodeId FileTransferStateMachineType_CurrentState_Number =
      new NodeId(UShort.MIN, uint(15807));

  public static final NodeId FileTransferStateMachineType_CurrentState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(15808));

  public static final NodeId FileTransferStateMachineType_LastTransition =
      new NodeId(UShort.MIN, uint(15809));

  public static final NodeId FileTransferStateMachineType_LastTransition_Id =
      new NodeId(UShort.MIN, uint(15810));

  public static final NodeId FileTransferStateMachineType_LastTransition_Name =
      new NodeId(UShort.MIN, uint(15811));

  public static final NodeId FileTransferStateMachineType_LastTransition_Number =
      new NodeId(UShort.MIN, uint(15812));

  public static final NodeId FileTransferStateMachineType_LastTransition_TransitionTime =
      new NodeId(UShort.MIN, uint(15813));

  public static final NodeId FileTransferStateMachineType_LastTransition_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(15814));

  public static final NodeId FileTransferStateMachineType_Idle =
      new NodeId(UShort.MIN, uint(15815));

  public static final NodeId FileTransferStateMachineType_Idle_StateNumber =
      new NodeId(UShort.MIN, uint(15816));

  public static final NodeId FileTransferStateMachineType_ReadPrepare =
      new NodeId(UShort.MIN, uint(15817));

  public static final NodeId FileTransferStateMachineType_ReadPrepare_StateNumber =
      new NodeId(UShort.MIN, uint(15818));

  public static final NodeId FileTransferStateMachineType_ReadTransfer =
      new NodeId(UShort.MIN, uint(15819));

  public static final NodeId FileTransferStateMachineType_ReadTransfer_StateNumber =
      new NodeId(UShort.MIN, uint(15820));

  public static final NodeId FileTransferStateMachineType_ApplyWrite =
      new NodeId(UShort.MIN, uint(15821));

  public static final NodeId FileTransferStateMachineType_ApplyWrite_StateNumber =
      new NodeId(UShort.MIN, uint(15822));

  public static final NodeId FileTransferStateMachineType_Error =
      new NodeId(UShort.MIN, uint(15823));

  public static final NodeId FileTransferStateMachineType_Error_StateNumber =
      new NodeId(UShort.MIN, uint(15824));

  public static final NodeId FileTransferStateMachineType_IdleToReadPrepare =
      new NodeId(UShort.MIN, uint(15825));

  public static final NodeId FileTransferStateMachineType_IdleToReadPrepare_TransitionNumber =
      new NodeId(UShort.MIN, uint(15826));

  public static final NodeId FileTransferStateMachineType_ReadPrepareToReadTransfer =
      new NodeId(UShort.MIN, uint(15827));

  public static final NodeId
      FileTransferStateMachineType_ReadPrepareToReadTransfer_TransitionNumber =
          new NodeId(UShort.MIN, uint(15828));

  public static final NodeId FileTransferStateMachineType_ReadTransferToIdle =
      new NodeId(UShort.MIN, uint(15829));

  public static final NodeId FileTransferStateMachineType_ReadTransferToIdle_TransitionNumber =
      new NodeId(UShort.MIN, uint(15830));

  public static final NodeId FileTransferStateMachineType_IdleToApplyWrite =
      new NodeId(UShort.MIN, uint(15831));

  public static final NodeId FileTransferStateMachineType_IdleToApplyWrite_TransitionNumber =
      new NodeId(UShort.MIN, uint(15832));

  public static final NodeId FileTransferStateMachineType_ApplyWriteToIdle =
      new NodeId(UShort.MIN, uint(15833));

  public static final NodeId FileTransferStateMachineType_ApplyWriteToIdle_TransitionNumber =
      new NodeId(UShort.MIN, uint(15834));

  public static final NodeId FileTransferStateMachineType_ReadPrepareToError =
      new NodeId(UShort.MIN, uint(15835));

  public static final NodeId FileTransferStateMachineType_ReadPrepareToError_TransitionNumber =
      new NodeId(UShort.MIN, uint(15836));

  public static final NodeId FileTransferStateMachineType_ReadTransferToError =
      new NodeId(UShort.MIN, uint(15837));

  public static final NodeId FileTransferStateMachineType_ReadTransferToError_TransitionNumber =
      new NodeId(UShort.MIN, uint(15838));

  public static final NodeId FileTransferStateMachineType_ApplyWriteToError =
      new NodeId(UShort.MIN, uint(15839));

  public static final NodeId FileTransferStateMachineType_ApplyWriteToError_TransitionNumber =
      new NodeId(UShort.MIN, uint(15840));

  public static final NodeId FileTransferStateMachineType_ErrorToIdle =
      new NodeId(UShort.MIN, uint(15841));

  public static final NodeId FileTransferStateMachineType_ErrorToIdle_TransitionNumber =
      new NodeId(UShort.MIN, uint(15842));

  public static final NodeId FileTransferStateMachineType_Reset =
      new NodeId(UShort.MIN, uint(15843));

  public static final NodeId PublishSubscribeType_Status = new NodeId(UShort.MIN, uint(15844));

  public static final NodeId PublishSubscribeType_Status_State =
      new NodeId(UShort.MIN, uint(15845));

  public static final NodeId PublishSubscribeType_Status_Enable =
      new NodeId(UShort.MIN, uint(15846));

  public static final NodeId PublishSubscribeType_Status_Disable =
      new NodeId(UShort.MIN, uint(15847));

  public static final NodeId
      PublishSubscribe_ConnectionName_Placeholder_TransportProfileUri_Selections =
          new NodeId(UShort.MIN, uint(15848));

  public static final NodeId
      PublishSubscribe_ConnectionName_Placeholder_TransportProfileUri_SelectionDescriptions =
          new NodeId(UShort.MIN, uint(15849));

  public static final NodeId
      PublishSubscribe_ConnectionName_Placeholder_TransportProfileUri_RestrictToList =
          new NodeId(UShort.MIN, uint(15850));

  public static final NodeId PublishSubscribe_ConnectionName_Placeholder_Address =
      new NodeId(UShort.MIN, uint(15851));

  public static final NodeId OpcUa_BinarySchema_WriterGroupTransportDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15852));

  public static final NodeId OpcUa_BinarySchema_WriterGroupTransportDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(15853));

  public static final NodeId OpcUa_BinarySchema_WriterGroupMessageDataType =
      new NodeId(UShort.MIN, uint(15854));

  public static final NodeId OpcUa_BinarySchema_WriterGroupMessageDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15855));

  public static final NodeId OpcUa_BinarySchema_WriterGroupMessageDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(15856));

  public static final NodeId OpcUa_BinarySchema_PubSubConnectionDataType =
      new NodeId(UShort.MIN, uint(15857));

  public static final NodeId OpcUa_BinarySchema_PubSubConnectionDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15858));

  public static final NodeId OpcUa_BinarySchema_PubSubConnectionDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(15859));

  public static final NodeId OpcUa_BinarySchema_ConnectionTransportDataType =
      new NodeId(UShort.MIN, uint(15860));

  public static final NodeId OpcUa_BinarySchema_ConnectionTransportDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15861));

  public static final NodeId OpcUa_BinarySchema_ConnectionTransportDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(15862));

  public static final NodeId PublishSubscribe_ConnectionName_Placeholder_Address_NetworkInterface =
      new NodeId(UShort.MIN, uint(15863));

  public static final NodeId PublishSubscribe_ConnectionName_Placeholder_TransportSettings =
      new NodeId(UShort.MIN, uint(15864));

  public static final NodeId PublishSubscribe_ConnectionName_Placeholder_Status =
      new NodeId(UShort.MIN, uint(15865));

  public static final NodeId OpcUa_BinarySchema_ReaderGroupTransportDataType =
      new NodeId(UShort.MIN, uint(15866));

  public static final NodeId OpcUa_BinarySchema_ReaderGroupTransportDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15867));

  public static final NodeId OpcUa_BinarySchema_ReaderGroupTransportDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(15868));

  public static final NodeId OpcUa_BinarySchema_ReaderGroupMessageDataType =
      new NodeId(UShort.MIN, uint(15869));

  public static final NodeId OpcUa_BinarySchema_ReaderGroupMessageDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15870));

  public static final NodeId OpcUa_BinarySchema_ReaderGroupMessageDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(15871));

  public static final NodeId OpcUa_BinarySchema_DataSetReaderDataType =
      new NodeId(UShort.MIN, uint(15872));

  public static final NodeId OpcUa_BinarySchema_DataSetReaderDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15873));

  public static final NodeId OverrideValueHandling = new NodeId(UShort.MIN, uint(15874));

  public static final NodeId OverrideValueHandling_EnumStrings =
      new NodeId(UShort.MIN, uint(15875));

  public static final NodeId OpcUa_BinarySchema_DataSetReaderDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(15876));

  public static final NodeId OpcUa_BinarySchema_DataSetReaderTransportDataType =
      new NodeId(UShort.MIN, uint(15877));

  public static final NodeId OpcUa_BinarySchema_DataSetReaderTransportDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15878));

  public static final NodeId OpcUa_BinarySchema_DataSetReaderTransportDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(15879));

  public static final NodeId OpcUa_BinarySchema_DataSetReaderMessageDataType =
      new NodeId(UShort.MIN, uint(15880));

  public static final NodeId OpcUa_BinarySchema_DataSetReaderMessageDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15881));

  public static final NodeId OpcUa_BinarySchema_DataSetReaderMessageDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(15882));

  public static final NodeId OpcUa_BinarySchema_SubscribedDataSetDataType =
      new NodeId(UShort.MIN, uint(15883));

  public static final NodeId OpcUa_BinarySchema_SubscribedDataSetDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15884));

  public static final NodeId OpcUa_BinarySchema_SubscribedDataSetDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(15885));

  public static final NodeId OpcUa_BinarySchema_TargetVariablesDataType =
      new NodeId(UShort.MIN, uint(15886));

  public static final NodeId OpcUa_BinarySchema_TargetVariablesDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15887));

  public static final NodeId OpcUa_BinarySchema_TargetVariablesDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(15888));

  public static final NodeId OpcUa_BinarySchema_SubscribedDataSetMirrorDataType =
      new NodeId(UShort.MIN, uint(15889));

  public static final NodeId OpcUa_BinarySchema_SubscribedDataSetMirrorDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15890));

  public static final NodeId OpcUa_BinarySchema_SubscribedDataSetMirrorDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(15891));

  public static final NodeId PublishSubscribe_ConnectionName_Placeholder_Status_State =
      new NodeId(UShort.MIN, uint(15892));

  public static final NodeId PublishSubscribe_ConnectionName_Placeholder_Status_Enable =
      new NodeId(UShort.MIN, uint(15893));

  public static final NodeId PublishSubscribe_ConnectionName_Placeholder_Status_Disable =
      new NodeId(UShort.MIN, uint(15894));

  public static final NodeId OpcUa_BinarySchema_UadpWriterGroupMessageDataType =
      new NodeId(UShort.MIN, uint(15895));

  public static final NodeId OpcUa_BinarySchema_UadpWriterGroupMessageDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15896));

  public static final NodeId OpcUa_BinarySchema_UadpWriterGroupMessageDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(15897));

  public static final NodeId OpcUa_BinarySchema_UadpDataSetWriterMessageDataType =
      new NodeId(UShort.MIN, uint(15898));

  public static final NodeId OpcUa_BinarySchema_UadpDataSetWriterMessageDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15899));

  public static final NodeId
      OpcUa_BinarySchema_UadpDataSetWriterMessageDataType_DictionaryFragment =
          new NodeId(UShort.MIN, uint(15900));

  public static final NodeId SessionlessInvokeRequestType = new NodeId(UShort.MIN, uint(15901));

  public static final NodeId SessionlessInvokeRequestType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(15902));

  public static final NodeId SessionlessInvokeRequestType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(15903));

  public static final NodeId DataSetFieldFlags = new NodeId(UShort.MIN, uint(15904));

  public static final NodeId PublishSubscribeType_ConnectionName_Placeholder_TransportSettings =
      new NodeId(UShort.MIN, uint(15905));

  public static final NodeId PubSubKeyServiceType = new NodeId(UShort.MIN, uint(15906));

  public static final NodeId PubSubKeyServiceType_GetSecurityKeys =
      new NodeId(UShort.MIN, uint(15907));

  public static final NodeId PubSubKeyServiceType_GetSecurityKeys_InputArguments =
      new NodeId(UShort.MIN, uint(15908));

  public static final NodeId PubSubKeyServiceType_GetSecurityKeys_OutputArguments =
      new NodeId(UShort.MIN, uint(15909));

  public static final NodeId PubSubKeyServiceType_GetSecurityGroup =
      new NodeId(UShort.MIN, uint(15910));

  public static final NodeId PubSubKeyServiceType_GetSecurityGroup_InputArguments =
      new NodeId(UShort.MIN, uint(15911));

  public static final NodeId PubSubKeyServiceType_GetSecurityGroup_OutputArguments =
      new NodeId(UShort.MIN, uint(15912));

  public static final NodeId PubSubKeyServiceType_SecurityGroups =
      new NodeId(UShort.MIN, uint(15913));

  public static final NodeId PubSubKeyServiceType_SecurityGroups_AddSecurityGroup =
      new NodeId(UShort.MIN, uint(15914));

  public static final NodeId PubSubKeyServiceType_SecurityGroups_AddSecurityGroup_InputArguments =
      new NodeId(UShort.MIN, uint(15915));

  public static final NodeId PubSubKeyServiceType_SecurityGroups_AddSecurityGroup_OutputArguments =
      new NodeId(UShort.MIN, uint(15916));

  public static final NodeId PubSubKeyServiceType_SecurityGroups_RemoveSecurityGroup =
      new NodeId(UShort.MIN, uint(15917));

  public static final NodeId
      PubSubKeyServiceType_SecurityGroups_RemoveSecurityGroup_InputArguments =
          new NodeId(UShort.MIN, uint(15918));

  public static final NodeId OpcUa_BinarySchema_UadpDataSetReaderMessageDataType =
      new NodeId(UShort.MIN, uint(15919));

  public static final NodeId OpcUa_BinarySchema_UadpDataSetReaderMessageDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15920));

  public static final NodeId
      OpcUa_BinarySchema_UadpDataSetReaderMessageDataType_DictionaryFragment =
          new NodeId(UShort.MIN, uint(15921));

  public static final NodeId OpcUa_BinarySchema_JsonWriterGroupMessageDataType =
      new NodeId(UShort.MIN, uint(15922));

  public static final NodeId OpcUa_BinarySchema_JsonWriterGroupMessageDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15923));

  public static final NodeId OpcUa_BinarySchema_JsonWriterGroupMessageDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(15924));

  public static final NodeId OpcUa_BinarySchema_JsonDataSetWriterMessageDataType =
      new NodeId(UShort.MIN, uint(15925));

  public static final NodeId PubSubGroupType_SecurityMode = new NodeId(UShort.MIN, uint(15926));

  public static final NodeId PubSubGroupType_SecurityGroupId = new NodeId(UShort.MIN, uint(15927));

  public static final NodeId PubSubGroupType_SecurityKeyServices =
      new NodeId(UShort.MIN, uint(15928));

  public static final NodeId OpcUa_BinarySchema_JsonDataSetWriterMessageDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15929));

  public static final NodeId
      OpcUa_BinarySchema_JsonDataSetWriterMessageDataType_DictionaryFragment =
          new NodeId(UShort.MIN, uint(15930));

  public static final NodeId OpcUa_BinarySchema_JsonDataSetReaderMessageDataType =
      new NodeId(UShort.MIN, uint(15931));

  public static final NodeId DataSetReaderType_SecurityMode = new NodeId(UShort.MIN, uint(15932));

  public static final NodeId DataSetReaderType_SecurityGroupId =
      new NodeId(UShort.MIN, uint(15933));

  public static final NodeId DataSetReaderType_SecurityKeyServices =
      new NodeId(UShort.MIN, uint(15934));

  public static final NodeId OpcUa_BinarySchema_JsonDataSetReaderMessageDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15935));

  public static final NodeId
      OpcUa_BinarySchema_JsonDataSetReaderMessageDataType_DictionaryFragment =
          new NodeId(UShort.MIN, uint(15936));

  public static final NodeId PublishSubscribe_ConnectionName_Placeholder_Diagnostics =
      new NodeId(UShort.MIN, uint(15937));

  public static final NodeId
      PublishSubscribe_ConnectionName_Placeholder_Diagnostics_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(15938));

  public static final NodeId
      PublishSubscribe_ConnectionName_Placeholder_Diagnostics_TotalInformation =
          new NodeId(UShort.MIN, uint(15939));

  public static final NodeId OpcUa_BinarySchema_BrokerWriterGroupTransportDataType =
      new NodeId(UShort.MIN, uint(15940));

  public static final NodeId OpcUa_BinarySchema_BrokerWriterGroupTransportDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15941));

  public static final NodeId
      OpcUa_BinarySchema_BrokerWriterGroupTransportDataType_DictionaryFragment =
          new NodeId(UShort.MIN, uint(15942));

  public static final NodeId OpcUa_BinarySchema_BrokerDataSetWriterTransportDataType =
      new NodeId(UShort.MIN, uint(15943));

  public static final NodeId
      OpcUa_BinarySchema_BrokerDataSetWriterTransportDataType_DataTypeVersion =
          new NodeId(UShort.MIN, uint(15944));

  public static final NodeId
      OpcUa_BinarySchema_BrokerDataSetWriterTransportDataType_DictionaryFragment =
          new NodeId(UShort.MIN, uint(15945));

  public static final NodeId OpcUa_BinarySchema_BrokerDataSetReaderTransportDataType =
      new NodeId(UShort.MIN, uint(15946));

  public static final NodeId
      OpcUa_BinarySchema_BrokerDataSetReaderTransportDataType_DataTypeVersion =
          new NodeId(UShort.MIN, uint(15947));

  public static final NodeId
      OpcUa_BinarySchema_BrokerDataSetReaderTransportDataType_DictionaryFragment =
          new NodeId(UShort.MIN, uint(15948));

  public static final NodeId EndpointType_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(15949));

  public static final NodeId DataTypeSchemaHeader_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(15950));

  public static final NodeId PublishedDataSetDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(15951));

  public static final NodeId PublishedDataSetSourceDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(15952));

  public static final NodeId PublishedDataItemsDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(15953));

  public static final NodeId PublishedEventsDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(15954));

  public static final NodeId DataSetWriterDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(15955));

  public static final NodeId DataSetWriterTransportDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(15956));

  public static final NodeId OPCUANamespaceMetadata = new NodeId(UShort.MIN, uint(15957));

  public static final NodeId OPCUANamespaceMetadata_NamespaceUri =
      new NodeId(UShort.MIN, uint(15958));

  public static final NodeId OPCUANamespaceMetadata_NamespaceVersion =
      new NodeId(UShort.MIN, uint(15959));

  public static final NodeId OPCUANamespaceMetadata_NamespacePublicationDate =
      new NodeId(UShort.MIN, uint(15960));

  public static final NodeId OPCUANamespaceMetadata_IsNamespaceSubset =
      new NodeId(UShort.MIN, uint(15961));

  public static final NodeId OPCUANamespaceMetadata_StaticNodeIdTypes =
      new NodeId(UShort.MIN, uint(15962));

  public static final NodeId OPCUANamespaceMetadata_StaticNumericNodeIdRange =
      new NodeId(UShort.MIN, uint(15963));

  public static final NodeId OPCUANamespaceMetadata_StaticStringNodeIdPattern =
      new NodeId(UShort.MIN, uint(15964));

  public static final NodeId OPCUANamespaceMetadata_NamespaceFile =
      new NodeId(UShort.MIN, uint(15965));

  public static final NodeId OPCUANamespaceMetadata_NamespaceFile_Size =
      new NodeId(UShort.MIN, uint(15966));

  public static final NodeId OPCUANamespaceMetadata_NamespaceFile_Writable =
      new NodeId(UShort.MIN, uint(15967));

  public static final NodeId OPCUANamespaceMetadata_NamespaceFile_UserWritable =
      new NodeId(UShort.MIN, uint(15968));

  public static final NodeId OPCUANamespaceMetadata_NamespaceFile_OpenCount =
      new NodeId(UShort.MIN, uint(15969));

  public static final NodeId OPCUANamespaceMetadata_NamespaceFile_MimeType =
      new NodeId(UShort.MIN, uint(15970));

  public static final NodeId OPCUANamespaceMetadata_NamespaceFile_Open =
      new NodeId(UShort.MIN, uint(15971));

  public static final NodeId OPCUANamespaceMetadata_NamespaceFile_Open_InputArguments =
      new NodeId(UShort.MIN, uint(15972));

  public static final NodeId OPCUANamespaceMetadata_NamespaceFile_Open_OutputArguments =
      new NodeId(UShort.MIN, uint(15973));

  public static final NodeId OPCUANamespaceMetadata_NamespaceFile_Close =
      new NodeId(UShort.MIN, uint(15974));

  public static final NodeId OPCUANamespaceMetadata_NamespaceFile_Close_InputArguments =
      new NodeId(UShort.MIN, uint(15975));

  public static final NodeId OPCUANamespaceMetadata_NamespaceFile_Read =
      new NodeId(UShort.MIN, uint(15976));

  public static final NodeId OPCUANamespaceMetadata_NamespaceFile_Read_InputArguments =
      new NodeId(UShort.MIN, uint(15977));

  public static final NodeId OPCUANamespaceMetadata_NamespaceFile_Read_OutputArguments =
      new NodeId(UShort.MIN, uint(15978));

  public static final NodeId OPCUANamespaceMetadata_NamespaceFile_Write =
      new NodeId(UShort.MIN, uint(15979));

  public static final NodeId OPCUANamespaceMetadata_NamespaceFile_Write_InputArguments =
      new NodeId(UShort.MIN, uint(15980));

  public static final NodeId OPCUANamespaceMetadata_NamespaceFile_GetPosition =
      new NodeId(UShort.MIN, uint(15981));

  public static final NodeId OPCUANamespaceMetadata_NamespaceFile_GetPosition_InputArguments =
      new NodeId(UShort.MIN, uint(15982));

  public static final NodeId OPCUANamespaceMetadata_NamespaceFile_GetPosition_OutputArguments =
      new NodeId(UShort.MIN, uint(15983));

  public static final NodeId OPCUANamespaceMetadata_NamespaceFile_SetPosition =
      new NodeId(UShort.MIN, uint(15984));

  public static final NodeId OPCUANamespaceMetadata_NamespaceFile_SetPosition_InputArguments =
      new NodeId(UShort.MIN, uint(15985));

  public static final NodeId OPCUANamespaceMetadata_NamespaceFile_ExportNamespace =
      new NodeId(UShort.MIN, uint(15986));

  public static final NodeId DataSetWriterMessageDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(15987));

  public static final NodeId PubSubGroupDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(15988));

  public static final NodeId
      PublishSubscribe_ConnectionName_Placeholder_Diagnostics_TotalInformation_Active =
          new NodeId(UShort.MIN, uint(15989));

  public static final NodeId WriterGroupTransportDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(15990));

  public static final NodeId WriterGroupMessageDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(15991));

  public static final NodeId PubSubConnectionDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(15992));

  public static final NodeId ConnectionTransportDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(15993));

  public static final NodeId
      PublishSubscribe_ConnectionName_Placeholder_Diagnostics_TotalInformation_Classification =
          new NodeId(UShort.MIN, uint(15994));

  public static final NodeId ReaderGroupTransportDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(15995));

  public static final NodeId ReaderGroupMessageDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(15996));

  public static final NodeId RoleSetType_AddRole = new NodeId(UShort.MIN, uint(15997));

  public static final NodeId RoleSetType_AddRole_InputArguments =
      new NodeId(UShort.MIN, uint(15998));

  public static final NodeId RoleSetType_AddRole_OutputArguments =
      new NodeId(UShort.MIN, uint(15999));

  public static final NodeId RoleSetType_RemoveRole = new NodeId(UShort.MIN, uint(16000));

  public static final NodeId RoleSetType_RemoveRole_InputArguments =
      new NodeId(UShort.MIN, uint(16001));

  public static final NodeId AddRoleMethodType = new NodeId(UShort.MIN, uint(16002));

  public static final NodeId AddRoleMethodType_InputArguments = new NodeId(UShort.MIN, uint(16003));

  public static final NodeId AddRoleMethodType_OutputArguments =
      new NodeId(UShort.MIN, uint(16004));

  public static final NodeId RemoveRoleMethodType = new NodeId(UShort.MIN, uint(16005));

  public static final NodeId RemoveRoleMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(16006));

  public static final NodeId DataSetReaderDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(16007));

  public static final NodeId DataSetReaderTransportDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(16008));

  public static final NodeId DataSetReaderMessageDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(16009));

  public static final NodeId SubscribedDataSetDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(16010));

  public static final NodeId TargetVariablesDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(16011));

  public static final NodeId SubscribedDataSetMirrorDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(16012));

  public static final NodeId
      PublishSubscribe_ConnectionName_Placeholder_Diagnostics_TotalInformation_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(16013));

  public static final NodeId UadpWriterGroupMessageDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(16014));

  public static final NodeId UadpDataSetWriterMessageDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(16015));

  public static final NodeId UadpDataSetReaderMessageDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(16016));

  public static final NodeId JsonWriterGroupMessageDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(16017));

  public static final NodeId JsonDataSetWriterMessageDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(16018));

  public static final NodeId JsonDataSetReaderMessageDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(16019));

  public static final NodeId
      PublishSubscribe_ConnectionName_Placeholder_Diagnostics_TotalInformation_TimeFirstChange =
          new NodeId(UShort.MIN, uint(16020));

  public static final NodeId BrokerWriterGroupTransportDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(16021));

  public static final NodeId BrokerDataSetWriterTransportDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(16022));

  public static final NodeId BrokerDataSetReaderTransportDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(16023));

  public static final NodeId OpcUa_XmlSchema_EndpointType = new NodeId(UShort.MIN, uint(16024));

  public static final NodeId OpcUa_XmlSchema_EndpointType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16025));

  public static final NodeId OpcUa_XmlSchema_EndpointType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16026));

  public static final NodeId OpcUa_XmlSchema_DataTypeSchemaHeader =
      new NodeId(UShort.MIN, uint(16027));

  public static final NodeId OpcUa_XmlSchema_DataTypeSchemaHeader_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16028));

  public static final NodeId OpcUa_XmlSchema_DataTypeSchemaHeader_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16029));

  public static final NodeId OpcUa_XmlSchema_PublishedDataSetDataType =
      new NodeId(UShort.MIN, uint(16030));

  public static final NodeId OpcUa_XmlSchema_PublishedDataSetDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16031));

  public static final NodeId OpcUa_XmlSchema_PublishedDataSetDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16032));

  public static final NodeId OpcUa_XmlSchema_PublishedDataSetSourceDataType =
      new NodeId(UShort.MIN, uint(16033));

  public static final NodeId OpcUa_XmlSchema_PublishedDataSetSourceDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16034));

  public static final NodeId OpcUa_XmlSchema_PublishedDataSetSourceDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16035));

  public static final NodeId WellKnownRole_Engineer = new NodeId(UShort.MIN, uint(16036));

  public static final NodeId OpcUa_XmlSchema_PublishedDataItemsDataType =
      new NodeId(UShort.MIN, uint(16037));

  public static final NodeId OpcUa_XmlSchema_PublishedDataItemsDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16038));

  public static final NodeId OpcUa_XmlSchema_PublishedDataItemsDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16039));

  public static final NodeId OpcUa_XmlSchema_PublishedEventsDataType =
      new NodeId(UShort.MIN, uint(16040));

  public static final NodeId WellKnownRole_Engineer_AddIdentity =
      new NodeId(UShort.MIN, uint(16041));

  public static final NodeId WellKnownRole_Engineer_AddIdentity_InputArguments =
      new NodeId(UShort.MIN, uint(16042));

  public static final NodeId WellKnownRole_Engineer_RemoveIdentity =
      new NodeId(UShort.MIN, uint(16043));

  public static final NodeId WellKnownRole_Engineer_RemoveIdentity_InputArguments =
      new NodeId(UShort.MIN, uint(16044));

  public static final NodeId OpcUa_XmlSchema_PublishedEventsDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16045));

  public static final NodeId OpcUa_XmlSchema_PublishedEventsDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16046));

  public static final NodeId OpcUa_XmlSchema_DataSetWriterDataType =
      new NodeId(UShort.MIN, uint(16047));

  public static final NodeId OpcUa_XmlSchema_DataSetWriterDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16048));

  public static final NodeId OpcUa_XmlSchema_DataSetWriterDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16049));

  public static final NodeId OpcUa_XmlSchema_DataSetWriterTransportDataType =
      new NodeId(UShort.MIN, uint(16050));

  public static final NodeId OpcUa_XmlSchema_DataSetWriterTransportDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16051));

  public static final NodeId OpcUa_XmlSchema_DataSetWriterTransportDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16052));

  public static final NodeId OpcUa_XmlSchema_DataSetWriterMessageDataType =
      new NodeId(UShort.MIN, uint(16053));

  public static final NodeId OpcUa_XmlSchema_DataSetWriterMessageDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16054));

  public static final NodeId OpcUa_XmlSchema_DataSetWriterMessageDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16055));

  public static final NodeId OpcUa_XmlSchema_PubSubGroupDataType =
      new NodeId(UShort.MIN, uint(16056));

  public static final NodeId OpcUa_XmlSchema_PubSubGroupDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16057));

  public static final NodeId OpcUa_XmlSchema_PubSubGroupDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16058));

  public static final NodeId PublishSubscribe_ConnectionName_Placeholder_Diagnostics_TotalError =
      new NodeId(UShort.MIN, uint(16059));

  public static final NodeId
      PublishSubscribe_ConnectionName_Placeholder_Diagnostics_TotalError_Active =
          new NodeId(UShort.MIN, uint(16060));

  public static final NodeId
      PublishSubscribe_ConnectionName_Placeholder_Diagnostics_TotalError_Classification =
          new NodeId(UShort.MIN, uint(16061));

  public static final NodeId OpcUa_XmlSchema_WriterGroupTransportDataType =
      new NodeId(UShort.MIN, uint(16062));

  public static final NodeId OpcUa_XmlSchema_WriterGroupTransportDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16063));

  public static final NodeId OpcUa_XmlSchema_WriterGroupTransportDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16064));

  public static final NodeId OpcUa_XmlSchema_WriterGroupMessageDataType =
      new NodeId(UShort.MIN, uint(16065));

  public static final NodeId OpcUa_XmlSchema_WriterGroupMessageDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16066));

  public static final NodeId OpcUa_XmlSchema_WriterGroupMessageDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16067));

  public static final NodeId OpcUa_XmlSchema_PubSubConnectionDataType =
      new NodeId(UShort.MIN, uint(16068));

  public static final NodeId OpcUa_XmlSchema_PubSubConnectionDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16069));

  public static final NodeId OpcUa_XmlSchema_PubSubConnectionDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16070));

  public static final NodeId OpcUa_XmlSchema_ConnectionTransportDataType =
      new NodeId(UShort.MIN, uint(16071));

  public static final NodeId OpcUa_XmlSchema_ConnectionTransportDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16072));

  public static final NodeId OpcUa_XmlSchema_ConnectionTransportDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16073));

  public static final NodeId
      PublishSubscribe_ConnectionName_Placeholder_Diagnostics_TotalError_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(16074));

  public static final NodeId
      PublishSubscribe_ConnectionName_Placeholder_Diagnostics_TotalError_TimeFirstChange =
          new NodeId(UShort.MIN, uint(16075));

  public static final NodeId PublishSubscribe_ConnectionName_Placeholder_Diagnostics_Reset =
      new NodeId(UShort.MIN, uint(16076));

  public static final NodeId OpcUa_XmlSchema_ReaderGroupTransportDataType =
      new NodeId(UShort.MIN, uint(16077));

  public static final NodeId OpcUa_XmlSchema_ReaderGroupTransportDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16078));

  public static final NodeId OpcUa_XmlSchema_ReaderGroupTransportDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16079));

  public static final NodeId OpcUa_XmlSchema_ReaderGroupMessageDataType =
      new NodeId(UShort.MIN, uint(16080));

  public static final NodeId OpcUa_XmlSchema_ReaderGroupMessageDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16081));

  public static final NodeId OpcUa_XmlSchema_ReaderGroupMessageDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16082));

  public static final NodeId OpcUa_XmlSchema_DataSetReaderDataType =
      new NodeId(UShort.MIN, uint(16083));

  public static final NodeId OpcUa_XmlSchema_DataSetReaderDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16084));

  public static final NodeId OpcUa_XmlSchema_DataSetReaderDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16085));

  public static final NodeId OpcUa_XmlSchema_DataSetReaderTransportDataType =
      new NodeId(UShort.MIN, uint(16086));

  public static final NodeId OpcUa_XmlSchema_DataSetReaderTransportDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16087));

  public static final NodeId OpcUa_XmlSchema_DataSetReaderTransportDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16088));

  public static final NodeId OpcUa_XmlSchema_DataSetReaderMessageDataType =
      new NodeId(UShort.MIN, uint(16089));

  public static final NodeId OpcUa_XmlSchema_DataSetReaderMessageDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16090));

  public static final NodeId OpcUa_XmlSchema_DataSetReaderMessageDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16091));

  public static final NodeId OpcUa_XmlSchema_SubscribedDataSetDataType =
      new NodeId(UShort.MIN, uint(16092));

  public static final NodeId OpcUa_XmlSchema_SubscribedDataSetDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16093));

  public static final NodeId OpcUa_XmlSchema_SubscribedDataSetDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16094));

  public static final NodeId OpcUa_XmlSchema_TargetVariablesDataType =
      new NodeId(UShort.MIN, uint(16095));

  public static final NodeId OpcUa_XmlSchema_TargetVariablesDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16096));

  public static final NodeId OpcUa_XmlSchema_TargetVariablesDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16097));

  public static final NodeId OpcUa_XmlSchema_SubscribedDataSetMirrorDataType =
      new NodeId(UShort.MIN, uint(16098));

  public static final NodeId OpcUa_XmlSchema_SubscribedDataSetMirrorDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16099));

  public static final NodeId OpcUa_XmlSchema_SubscribedDataSetMirrorDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16100));

  public static final NodeId PublishSubscribe_ConnectionName_Placeholder_Diagnostics_SubError =
      new NodeId(UShort.MIN, uint(16101));

  public static final NodeId PublishSubscribe_ConnectionName_Placeholder_Diagnostics_Counters =
      new NodeId(UShort.MIN, uint(16102));

  public static final NodeId
      PublishSubscribe_ConnectionName_Placeholder_Diagnostics_Counters_StateError =
          new NodeId(UShort.MIN, uint(16103));

  public static final NodeId OpcUa_XmlSchema_UadpWriterGroupMessageDataType =
      new NodeId(UShort.MIN, uint(16104));

  public static final NodeId OpcUa_XmlSchema_UadpWriterGroupMessageDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16105));

  public static final NodeId OpcUa_XmlSchema_UadpWriterGroupMessageDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16106));

  public static final NodeId OpcUa_XmlSchema_UadpDataSetWriterMessageDataType =
      new NodeId(UShort.MIN, uint(16107));

  public static final NodeId OpcUa_XmlSchema_UadpDataSetWriterMessageDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16108));

  public static final NodeId OpcUa_XmlSchema_UadpDataSetWriterMessageDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16109));

  public static final NodeId OpcUa_XmlSchema_UadpDataSetReaderMessageDataType =
      new NodeId(UShort.MIN, uint(16110));

  public static final NodeId OpcUa_XmlSchema_UadpDataSetReaderMessageDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16111));

  public static final NodeId OpcUa_XmlSchema_UadpDataSetReaderMessageDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16112));

  public static final NodeId OpcUa_XmlSchema_JsonWriterGroupMessageDataType =
      new NodeId(UShort.MIN, uint(16113));

  public static final NodeId OpcUa_XmlSchema_JsonWriterGroupMessageDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16114));

  public static final NodeId OpcUa_XmlSchema_JsonWriterGroupMessageDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16115));

  public static final NodeId OpcUa_XmlSchema_JsonDataSetWriterMessageDataType =
      new NodeId(UShort.MIN, uint(16116));

  public static final NodeId OpcUa_XmlSchema_JsonDataSetWriterMessageDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16117));

  public static final NodeId OpcUa_XmlSchema_JsonDataSetWriterMessageDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16118));

  public static final NodeId OpcUa_XmlSchema_JsonDataSetReaderMessageDataType =
      new NodeId(UShort.MIN, uint(16119));

  public static final NodeId OpcUa_XmlSchema_JsonDataSetReaderMessageDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16120));

  public static final NodeId OpcUa_XmlSchema_JsonDataSetReaderMessageDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16121));

  public static final NodeId
      PublishSubscribe_ConnectionName_Placeholder_Diagnostics_Counters_StateError_Active =
          new NodeId(UShort.MIN, uint(16122));

  public static final NodeId
      PublishSubscribe_ConnectionName_Placeholder_Diagnostics_Counters_StateError_Classification =
          new NodeId(UShort.MIN, uint(16123));

  public static final NodeId
      PublishSubscribe_ConnectionName_Placeholder_Diagnostics_Counters_StateError_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(16124));

  public static final NodeId OpcUa_XmlSchema_BrokerWriterGroupTransportDataType =
      new NodeId(UShort.MIN, uint(16125));

  public static final NodeId RolePermissionType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(16126));

  public static final NodeId OpcUa_XmlSchema_RolePermissionType =
      new NodeId(UShort.MIN, uint(16127));

  public static final NodeId OpcUa_XmlSchema_RolePermissionType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16128));

  public static final NodeId OpcUa_XmlSchema_RolePermissionType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16129));

  public static final NodeId OpcUa_XmlSchema_BrokerWriterGroupTransportDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16130));

  public static final NodeId OpcUa_BinarySchema_RolePermissionType =
      new NodeId(UShort.MIN, uint(16131));

  public static final NodeId OpcUa_BinarySchema_RolePermissionType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16132));

  public static final NodeId OpcUa_BinarySchema_RolePermissionType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16133));

  public static final NodeId OPCUANamespaceMetadata_DefaultRolePermissions =
      new NodeId(UShort.MIN, uint(16134));

  public static final NodeId OPCUANamespaceMetadata_DefaultUserRolePermissions =
      new NodeId(UShort.MIN, uint(16135));

  public static final NodeId OPCUANamespaceMetadata_DefaultAccessRestrictions =
      new NodeId(UShort.MIN, uint(16136));

  public static final NodeId NamespaceMetadataType_DefaultRolePermissions =
      new NodeId(UShort.MIN, uint(16137));

  public static final NodeId NamespaceMetadataType_DefaultUserRolePermissions =
      new NodeId(UShort.MIN, uint(16138));

  public static final NodeId NamespaceMetadataType_DefaultAccessRestrictions =
      new NodeId(UShort.MIN, uint(16139));

  public static final NodeId NamespacesType_NamespaceIdentifier_Placeholder_DefaultRolePermissions =
      new NodeId(UShort.MIN, uint(16140));

  public static final NodeId
      NamespacesType_NamespaceIdentifier_Placeholder_DefaultUserRolePermissions =
          new NodeId(UShort.MIN, uint(16141));

  public static final NodeId
      NamespacesType_NamespaceIdentifier_Placeholder_DefaultAccessRestrictions =
          new NodeId(UShort.MIN, uint(16142));

  public static final NodeId OpcUa_XmlSchema_BrokerWriterGroupTransportDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16143));

  public static final NodeId OpcUa_XmlSchema_BrokerDataSetWriterTransportDataType =
      new NodeId(UShort.MIN, uint(16144));

  public static final NodeId OpcUa_XmlSchema_BrokerDataSetWriterTransportDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16145));

  public static final NodeId
      OpcUa_XmlSchema_BrokerDataSetWriterTransportDataType_DictionaryFragment =
          new NodeId(UShort.MIN, uint(16146));

  public static final NodeId OpcUa_XmlSchema_BrokerDataSetReaderTransportDataType =
      new NodeId(UShort.MIN, uint(16147));

  public static final NodeId OpcUa_XmlSchema_BrokerDataSetReaderTransportDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16148));

  public static final NodeId
      OpcUa_XmlSchema_BrokerDataSetReaderTransportDataType_DictionaryFragment =
          new NodeId(UShort.MIN, uint(16149));

  public static final NodeId EndpointType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(16150));

  public static final NodeId DataTypeSchemaHeader_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(16151));

  public static final NodeId PublishedDataSetDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(16152));

  public static final NodeId PublishedDataSetSourceDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(16153));

  public static final NodeId PublishedDataItemsDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(16154));

  public static final NodeId PublishedEventsDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(16155));

  public static final NodeId DataSetWriterDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(16156));

  public static final NodeId DataSetWriterTransportDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(16157));

  public static final NodeId DataSetWriterMessageDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(16158));

  public static final NodeId PubSubGroupDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(16159));

  public static final NodeId
      PublishSubscribe_ConnectionName_Placeholder_Diagnostics_Counters_StateError_TimeFirstChange =
          new NodeId(UShort.MIN, uint(16160));

  public static final NodeId WriterGroupTransportDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(16161));

  public static final NodeId RoleSetType_RoleName_Placeholder_Identities =
      new NodeId(UShort.MIN, uint(16162));

  public static final NodeId RoleSetType_RoleName_Placeholder_Applications =
      new NodeId(UShort.MIN, uint(16163));

  public static final NodeId RoleSetType_RoleName_Placeholder_Endpoints =
      new NodeId(UShort.MIN, uint(16164));

  public static final NodeId RoleSetType_RoleName_Placeholder_AddApplication =
      new NodeId(UShort.MIN, uint(16165));

  public static final NodeId RoleSetType_RoleName_Placeholder_AddApplication_InputArguments =
      new NodeId(UShort.MIN, uint(16166));

  public static final NodeId RoleSetType_RoleName_Placeholder_RemoveApplication =
      new NodeId(UShort.MIN, uint(16167));

  public static final NodeId RoleSetType_RoleName_Placeholder_RemoveApplication_InputArguments =
      new NodeId(UShort.MIN, uint(16168));

  public static final NodeId RoleSetType_RoleName_Placeholder_AddEndpoint =
      new NodeId(UShort.MIN, uint(16169));

  public static final NodeId RoleSetType_RoleName_Placeholder_AddEndpoint_InputArguments =
      new NodeId(UShort.MIN, uint(16170));

  public static final NodeId RoleSetType_RoleName_Placeholder_RemoveEndpoint =
      new NodeId(UShort.MIN, uint(16171));

  public static final NodeId RoleSetType_RoleName_Placeholder_RemoveEndpoint_InputArguments =
      new NodeId(UShort.MIN, uint(16172));

  public static final NodeId RoleType_Identities = new NodeId(UShort.MIN, uint(16173));

  public static final NodeId RoleType_Applications = new NodeId(UShort.MIN, uint(16174));

  public static final NodeId RoleType_Endpoints = new NodeId(UShort.MIN, uint(16175));

  public static final NodeId RoleType_AddApplication = new NodeId(UShort.MIN, uint(16176));

  public static final NodeId RoleType_AddApplication_InputArguments =
      new NodeId(UShort.MIN, uint(16177));

  public static final NodeId RoleType_RemoveApplication = new NodeId(UShort.MIN, uint(16178));

  public static final NodeId RoleType_RemoveApplication_InputArguments =
      new NodeId(UShort.MIN, uint(16179));

  public static final NodeId RoleType_AddEndpoint = new NodeId(UShort.MIN, uint(16180));

  public static final NodeId RoleType_AddEndpoint_InputArguments =
      new NodeId(UShort.MIN, uint(16181));

  public static final NodeId RoleType_RemoveEndpoint = new NodeId(UShort.MIN, uint(16182));

  public static final NodeId RoleType_RemoveEndpoint_InputArguments =
      new NodeId(UShort.MIN, uint(16183));

  public static final NodeId AddApplicationMethodType = new NodeId(UShort.MIN, uint(16184));

  public static final NodeId AddApplicationMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(16185));

  public static final NodeId RemoveApplicationMethodType = new NodeId(UShort.MIN, uint(16186));

  public static final NodeId RemoveApplicationMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(16187));

  public static final NodeId AddEndpointMethodType = new NodeId(UShort.MIN, uint(16188));

  public static final NodeId AddEndpointMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(16189));

  public static final NodeId RemoveEndpointMethodType = new NodeId(UShort.MIN, uint(16190));

  public static final NodeId RemoveEndpointMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(16191));

  public static final NodeId WellKnownRole_Anonymous_Identities =
      new NodeId(UShort.MIN, uint(16192));

  public static final NodeId WellKnownRole_Anonymous_Applications =
      new NodeId(UShort.MIN, uint(16193));

  public static final NodeId WellKnownRole_Anonymous_Endpoints =
      new NodeId(UShort.MIN, uint(16194));

  public static final NodeId WellKnownRole_Anonymous_AddApplication =
      new NodeId(UShort.MIN, uint(16195));

  public static final NodeId WellKnownRole_Anonymous_AddApplication_InputArguments =
      new NodeId(UShort.MIN, uint(16196));

  public static final NodeId WellKnownRole_Anonymous_RemoveApplication =
      new NodeId(UShort.MIN, uint(16197));

  public static final NodeId WellKnownRole_Anonymous_RemoveApplication_InputArguments =
      new NodeId(UShort.MIN, uint(16198));

  public static final NodeId WellKnownRole_Anonymous_AddEndpoint =
      new NodeId(UShort.MIN, uint(16199));

  public static final NodeId WellKnownRole_Anonymous_AddEndpoint_InputArguments =
      new NodeId(UShort.MIN, uint(16200));

  public static final NodeId WellKnownRole_Anonymous_RemoveEndpoint =
      new NodeId(UShort.MIN, uint(16201));

  public static final NodeId WellKnownRole_Anonymous_RemoveEndpoint_InputArguments =
      new NodeId(UShort.MIN, uint(16202));

  public static final NodeId WellKnownRole_AuthenticatedUser_Identities =
      new NodeId(UShort.MIN, uint(16203));

  public static final NodeId WellKnownRole_AuthenticatedUser_Applications =
      new NodeId(UShort.MIN, uint(16204));

  public static final NodeId WellKnownRole_AuthenticatedUser_Endpoints =
      new NodeId(UShort.MIN, uint(16205));

  public static final NodeId WellKnownRole_AuthenticatedUser_AddApplication =
      new NodeId(UShort.MIN, uint(16206));

  public static final NodeId WellKnownRole_AuthenticatedUser_AddApplication_InputArguments =
      new NodeId(UShort.MIN, uint(16207));

  public static final NodeId WellKnownRole_AuthenticatedUser_RemoveApplication =
      new NodeId(UShort.MIN, uint(16208));

  public static final NodeId WellKnownRole_AuthenticatedUser_RemoveApplication_InputArguments =
      new NodeId(UShort.MIN, uint(16209));

  public static final NodeId WellKnownRole_AuthenticatedUser_AddEndpoint =
      new NodeId(UShort.MIN, uint(16210));

  public static final NodeId WellKnownRole_AuthenticatedUser_AddEndpoint_InputArguments =
      new NodeId(UShort.MIN, uint(16211));

  public static final NodeId WellKnownRole_AuthenticatedUser_RemoveEndpoint =
      new NodeId(UShort.MIN, uint(16212));

  public static final NodeId WellKnownRole_AuthenticatedUser_RemoveEndpoint_InputArguments =
      new NodeId(UShort.MIN, uint(16213));

  public static final NodeId WellKnownRole_Observer_Identities =
      new NodeId(UShort.MIN, uint(16214));

  public static final NodeId WellKnownRole_Observer_Applications =
      new NodeId(UShort.MIN, uint(16215));

  public static final NodeId WellKnownRole_Observer_Endpoints = new NodeId(UShort.MIN, uint(16216));

  public static final NodeId WellKnownRole_Observer_AddApplication =
      new NodeId(UShort.MIN, uint(16217));

  public static final NodeId WellKnownRole_Observer_AddApplication_InputArguments =
      new NodeId(UShort.MIN, uint(16218));

  public static final NodeId WellKnownRole_Observer_RemoveApplication =
      new NodeId(UShort.MIN, uint(16219));

  public static final NodeId WellKnownRole_Observer_RemoveApplication_InputArguments =
      new NodeId(UShort.MIN, uint(16220));

  public static final NodeId WellKnownRole_Observer_AddEndpoint =
      new NodeId(UShort.MIN, uint(16221));

  public static final NodeId WellKnownRole_Observer_AddEndpoint_InputArguments =
      new NodeId(UShort.MIN, uint(16222));

  public static final NodeId WellKnownRole_Observer_RemoveEndpoint =
      new NodeId(UShort.MIN, uint(16223));

  public static final NodeId WellKnownRole_Observer_RemoveEndpoint_InputArguments =
      new NodeId(UShort.MIN, uint(16224));

  public static final NodeId WellKnownRole_Operator_Identities =
      new NodeId(UShort.MIN, uint(16225));

  public static final NodeId WellKnownRole_Operator_Applications =
      new NodeId(UShort.MIN, uint(16226));

  public static final NodeId WellKnownRole_Operator_Endpoints = new NodeId(UShort.MIN, uint(16227));

  public static final NodeId WellKnownRole_Operator_AddApplication =
      new NodeId(UShort.MIN, uint(16228));

  public static final NodeId WellKnownRole_Operator_AddApplication_InputArguments =
      new NodeId(UShort.MIN, uint(16229));

  public static final NodeId WellKnownRole_Operator_RemoveApplication =
      new NodeId(UShort.MIN, uint(16230));

  public static final NodeId WellKnownRole_Operator_RemoveApplication_InputArguments =
      new NodeId(UShort.MIN, uint(16231));

  public static final NodeId WellKnownRole_Operator_AddEndpoint =
      new NodeId(UShort.MIN, uint(16232));

  public static final NodeId WellKnownRole_Operator_AddEndpoint_InputArguments =
      new NodeId(UShort.MIN, uint(16233));

  public static final NodeId WellKnownRole_Operator_RemoveEndpoint =
      new NodeId(UShort.MIN, uint(16234));

  public static final NodeId WellKnownRole_Operator_RemoveEndpoint_InputArguments =
      new NodeId(UShort.MIN, uint(16235));

  public static final NodeId WellKnownRole_Engineer_Identities =
      new NodeId(UShort.MIN, uint(16236));

  public static final NodeId WellKnownRole_Engineer_Applications =
      new NodeId(UShort.MIN, uint(16237));

  public static final NodeId WellKnownRole_Engineer_Endpoints = new NodeId(UShort.MIN, uint(16238));

  public static final NodeId WellKnownRole_Engineer_AddApplication =
      new NodeId(UShort.MIN, uint(16239));

  public static final NodeId WellKnownRole_Engineer_AddApplication_InputArguments =
      new NodeId(UShort.MIN, uint(16240));

  public static final NodeId WellKnownRole_Engineer_RemoveApplication =
      new NodeId(UShort.MIN, uint(16241));

  public static final NodeId WellKnownRole_Engineer_RemoveApplication_InputArguments =
      new NodeId(UShort.MIN, uint(16242));

  public static final NodeId WellKnownRole_Engineer_AddEndpoint =
      new NodeId(UShort.MIN, uint(16243));

  public static final NodeId WellKnownRole_Engineer_AddEndpoint_InputArguments =
      new NodeId(UShort.MIN, uint(16244));

  public static final NodeId WellKnownRole_Engineer_RemoveEndpoint =
      new NodeId(UShort.MIN, uint(16245));

  public static final NodeId WellKnownRole_Engineer_RemoveEndpoint_InputArguments =
      new NodeId(UShort.MIN, uint(16246));

  public static final NodeId WellKnownRole_Supervisor_Identities =
      new NodeId(UShort.MIN, uint(16247));

  public static final NodeId WellKnownRole_Supervisor_Applications =
      new NodeId(UShort.MIN, uint(16248));

  public static final NodeId WellKnownRole_Supervisor_Endpoints =
      new NodeId(UShort.MIN, uint(16249));

  public static final NodeId WellKnownRole_Supervisor_AddApplication =
      new NodeId(UShort.MIN, uint(16250));

  public static final NodeId WellKnownRole_Supervisor_AddApplication_InputArguments =
      new NodeId(UShort.MIN, uint(16251));

  public static final NodeId WellKnownRole_Supervisor_RemoveApplication =
      new NodeId(UShort.MIN, uint(16252));

  public static final NodeId WellKnownRole_Supervisor_RemoveApplication_InputArguments =
      new NodeId(UShort.MIN, uint(16253));

  public static final NodeId WellKnownRole_Supervisor_AddEndpoint =
      new NodeId(UShort.MIN, uint(16254));

  public static final NodeId WellKnownRole_Supervisor_AddEndpoint_InputArguments =
      new NodeId(UShort.MIN, uint(16255));

  public static final NodeId WellKnownRole_Supervisor_RemoveEndpoint =
      new NodeId(UShort.MIN, uint(16256));

  public static final NodeId WellKnownRole_Supervisor_RemoveEndpoint_InputArguments =
      new NodeId(UShort.MIN, uint(16257));

  public static final NodeId WellKnownRole_SecurityAdmin_Identities =
      new NodeId(UShort.MIN, uint(16258));

  public static final NodeId WellKnownRole_SecurityAdmin_Applications =
      new NodeId(UShort.MIN, uint(16259));

  public static final NodeId WellKnownRole_SecurityAdmin_Endpoints =
      new NodeId(UShort.MIN, uint(16260));

  public static final NodeId WellKnownRole_SecurityAdmin_AddApplication =
      new NodeId(UShort.MIN, uint(16261));

  public static final NodeId WellKnownRole_SecurityAdmin_AddApplication_InputArguments =
      new NodeId(UShort.MIN, uint(16262));

  public static final NodeId WellKnownRole_SecurityAdmin_RemoveApplication =
      new NodeId(UShort.MIN, uint(16263));

  public static final NodeId WellKnownRole_SecurityAdmin_RemoveApplication_InputArguments =
      new NodeId(UShort.MIN, uint(16264));

  public static final NodeId WellKnownRole_SecurityAdmin_AddEndpoint =
      new NodeId(UShort.MIN, uint(16265));

  public static final NodeId WellKnownRole_SecurityAdmin_AddEndpoint_InputArguments =
      new NodeId(UShort.MIN, uint(16266));

  public static final NodeId WellKnownRole_SecurityAdmin_RemoveEndpoint =
      new NodeId(UShort.MIN, uint(16267));

  public static final NodeId WellKnownRole_SecurityAdmin_RemoveEndpoint_InputArguments =
      new NodeId(UShort.MIN, uint(16268));

  public static final NodeId WellKnownRole_ConfigureAdmin_Identities =
      new NodeId(UShort.MIN, uint(16269));

  public static final NodeId WellKnownRole_ConfigureAdmin_Applications =
      new NodeId(UShort.MIN, uint(16270));

  public static final NodeId WellKnownRole_ConfigureAdmin_Endpoints =
      new NodeId(UShort.MIN, uint(16271));

  public static final NodeId WellKnownRole_ConfigureAdmin_AddApplication =
      new NodeId(UShort.MIN, uint(16272));

  public static final NodeId WellKnownRole_ConfigureAdmin_AddApplication_InputArguments =
      new NodeId(UShort.MIN, uint(16273));

  public static final NodeId WellKnownRole_ConfigureAdmin_RemoveApplication =
      new NodeId(UShort.MIN, uint(16274));

  public static final NodeId WellKnownRole_ConfigureAdmin_RemoveApplication_InputArguments =
      new NodeId(UShort.MIN, uint(16275));

  public static final NodeId WellKnownRole_ConfigureAdmin_AddEndpoint =
      new NodeId(UShort.MIN, uint(16276));

  public static final NodeId WellKnownRole_ConfigureAdmin_AddEndpoint_InputArguments =
      new NodeId(UShort.MIN, uint(16277));

  public static final NodeId WellKnownRole_ConfigureAdmin_RemoveEndpoint =
      new NodeId(UShort.MIN, uint(16278));

  public static final NodeId WellKnownRole_ConfigureAdmin_RemoveEndpoint_InputArguments =
      new NodeId(UShort.MIN, uint(16279));

  public static final NodeId WriterGroupMessageDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(16280));

  public static final NodeId PubSubConnectionDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(16281));

  public static final NodeId ConnectionTransportDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(16282));

  public static final NodeId
      PublishSubscribe_ConnectionName_Placeholder_Diagnostics_Counters_StateOperationalByMethod =
          new NodeId(UShort.MIN, uint(16283));

  public static final NodeId ReaderGroupTransportDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(16284));

  public static final NodeId ReaderGroupMessageDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(16285));

  public static final NodeId DataSetReaderDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(16286));

  public static final NodeId DataSetReaderTransportDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(16287));

  public static final NodeId DataSetReaderMessageDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(16288));

  public static final NodeId ServerType_ServerCapabilities_RoleSet =
      new NodeId(UShort.MIN, uint(16289));

  public static final NodeId ServerType_ServerCapabilities_RoleSet_AddRole =
      new NodeId(UShort.MIN, uint(16290));

  public static final NodeId ServerType_ServerCapabilities_RoleSet_AddRole_InputArguments =
      new NodeId(UShort.MIN, uint(16291));

  public static final NodeId ServerType_ServerCapabilities_RoleSet_AddRole_OutputArguments =
      new NodeId(UShort.MIN, uint(16292));

  public static final NodeId ServerType_ServerCapabilities_RoleSet_RemoveRole =
      new NodeId(UShort.MIN, uint(16293));

  public static final NodeId ServerType_ServerCapabilities_RoleSet_RemoveRole_InputArguments =
      new NodeId(UShort.MIN, uint(16294));

  public static final NodeId ServerCapabilitiesType_RoleSet = new NodeId(UShort.MIN, uint(16295));

  public static final NodeId ServerCapabilitiesType_RoleSet_AddRole =
      new NodeId(UShort.MIN, uint(16296));

  public static final NodeId ServerCapabilitiesType_RoleSet_AddRole_InputArguments =
      new NodeId(UShort.MIN, uint(16297));

  public static final NodeId ServerCapabilitiesType_RoleSet_AddRole_OutputArguments =
      new NodeId(UShort.MIN, uint(16298));

  public static final NodeId ServerCapabilitiesType_RoleSet_RemoveRole =
      new NodeId(UShort.MIN, uint(16299));

  public static final NodeId ServerCapabilitiesType_RoleSet_RemoveRole_InputArguments =
      new NodeId(UShort.MIN, uint(16300));

  public static final NodeId Server_ServerCapabilities_RoleSet_AddRole =
      new NodeId(UShort.MIN, uint(16301));

  public static final NodeId Server_ServerCapabilities_RoleSet_AddRole_InputArguments =
      new NodeId(UShort.MIN, uint(16302));

  public static final NodeId Server_ServerCapabilities_RoleSet_AddRole_OutputArguments =
      new NodeId(UShort.MIN, uint(16303));

  public static final NodeId Server_ServerCapabilities_RoleSet_RemoveRole =
      new NodeId(UShort.MIN, uint(16304));

  public static final NodeId Server_ServerCapabilities_RoleSet_RemoveRole_InputArguments =
      new NodeId(UShort.MIN, uint(16305));

  public static final NodeId AudioDataType = new NodeId(UShort.MIN, uint(16307));

  public static final NodeId SubscribedDataSetDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(16308));

  public static final NodeId SelectionListType = new NodeId(UShort.MIN, uint(16309));

  public static final NodeId TargetVariablesDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(16310));

  public static final NodeId SubscribedDataSetMirrorDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(16311));

  public static final NodeId SelectionListType_RestrictToList = new NodeId(UShort.MIN, uint(16312));

  public static final NodeId AdditionalParametersType = new NodeId(UShort.MIN, uint(16313));

  public static final NodeId FileSystem = new NodeId(UShort.MIN, uint(16314));

  public static final NodeId FileSystem_FileDirectoryName_Placeholder =
      new NodeId(UShort.MIN, uint(16315));

  public static final NodeId FileSystem_FileDirectoryName_Placeholder_CreateDirectory =
      new NodeId(UShort.MIN, uint(16316));

  public static final NodeId
      FileSystem_FileDirectoryName_Placeholder_CreateDirectory_InputArguments =
          new NodeId(UShort.MIN, uint(16317));

  public static final NodeId
      FileSystem_FileDirectoryName_Placeholder_CreateDirectory_OutputArguments =
          new NodeId(UShort.MIN, uint(16318));

  public static final NodeId FileSystem_FileDirectoryName_Placeholder_CreateFile =
      new NodeId(UShort.MIN, uint(16319));

  public static final NodeId FileSystem_FileDirectoryName_Placeholder_CreateFile_InputArguments =
      new NodeId(UShort.MIN, uint(16320));

  public static final NodeId FileSystem_FileDirectoryName_Placeholder_CreateFile_OutputArguments =
      new NodeId(UShort.MIN, uint(16321));

  public static final NodeId
      PublishSubscribe_ConnectionName_Placeholder_Diagnostics_Counters_StateOperationalByMethod_Active =
          new NodeId(UShort.MIN, uint(16322));

  public static final NodeId UadpWriterGroupMessageDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(16323));

  public static final NodeId FileSystem_FileDirectoryName_Placeholder_MoveOrCopy =
      new NodeId(UShort.MIN, uint(16324));

  public static final NodeId FileSystem_FileDirectoryName_Placeholder_MoveOrCopy_InputArguments =
      new NodeId(UShort.MIN, uint(16325));

  public static final NodeId FileSystem_FileDirectoryName_Placeholder_MoveOrCopy_OutputArguments =
      new NodeId(UShort.MIN, uint(16326));

  public static final NodeId FileSystem_FileName_Placeholder = new NodeId(UShort.MIN, uint(16327));

  public static final NodeId FileSystem_FileName_Placeholder_Size =
      new NodeId(UShort.MIN, uint(16328));

  public static final NodeId FileSystem_FileName_Placeholder_Writable =
      new NodeId(UShort.MIN, uint(16329));

  public static final NodeId FileSystem_FileName_Placeholder_UserWritable =
      new NodeId(UShort.MIN, uint(16330));

  public static final NodeId FileSystem_FileName_Placeholder_OpenCount =
      new NodeId(UShort.MIN, uint(16331));

  public static final NodeId FileSystem_FileName_Placeholder_MimeType =
      new NodeId(UShort.MIN, uint(16332));

  public static final NodeId FileSystem_FileName_Placeholder_Open =
      new NodeId(UShort.MIN, uint(16333));

  public static final NodeId FileSystem_FileName_Placeholder_Open_InputArguments =
      new NodeId(UShort.MIN, uint(16334));

  public static final NodeId FileSystem_FileName_Placeholder_Open_OutputArguments =
      new NodeId(UShort.MIN, uint(16335));

  public static final NodeId FileSystem_FileName_Placeholder_Close =
      new NodeId(UShort.MIN, uint(16336));

  public static final NodeId FileSystem_FileName_Placeholder_Close_InputArguments =
      new NodeId(UShort.MIN, uint(16337));

  public static final NodeId FileSystem_FileName_Placeholder_Read =
      new NodeId(UShort.MIN, uint(16338));

  public static final NodeId FileSystem_FileName_Placeholder_Read_InputArguments =
      new NodeId(UShort.MIN, uint(16339));

  public static final NodeId FileSystem_FileName_Placeholder_Read_OutputArguments =
      new NodeId(UShort.MIN, uint(16340));

  public static final NodeId FileSystem_FileName_Placeholder_Write =
      new NodeId(UShort.MIN, uint(16341));

  public static final NodeId FileSystem_FileName_Placeholder_Write_InputArguments =
      new NodeId(UShort.MIN, uint(16342));

  public static final NodeId FileSystem_FileName_Placeholder_GetPosition =
      new NodeId(UShort.MIN, uint(16343));

  public static final NodeId FileSystem_FileName_Placeholder_GetPosition_InputArguments =
      new NodeId(UShort.MIN, uint(16344));

  public static final NodeId FileSystem_FileName_Placeholder_GetPosition_OutputArguments =
      new NodeId(UShort.MIN, uint(16345));

  public static final NodeId FileSystem_FileName_Placeholder_SetPosition =
      new NodeId(UShort.MIN, uint(16346));

  public static final NodeId FileSystem_FileName_Placeholder_SetPosition_InputArguments =
      new NodeId(UShort.MIN, uint(16347));

  public static final NodeId FileSystem_CreateDirectory = new NodeId(UShort.MIN, uint(16348));

  public static final NodeId FileSystem_CreateDirectory_InputArguments =
      new NodeId(UShort.MIN, uint(16349));

  public static final NodeId FileSystem_CreateDirectory_OutputArguments =
      new NodeId(UShort.MIN, uint(16350));

  public static final NodeId FileSystem_CreateFile = new NodeId(UShort.MIN, uint(16351));

  public static final NodeId FileSystem_CreateFile_InputArguments =
      new NodeId(UShort.MIN, uint(16352));

  public static final NodeId FileSystem_CreateFile_OutputArguments =
      new NodeId(UShort.MIN, uint(16353));

  public static final NodeId FileSystem_DeleteFileSystemObject =
      new NodeId(UShort.MIN, uint(16354));

  public static final NodeId FileSystem_DeleteFileSystemObject_InputArguments =
      new NodeId(UShort.MIN, uint(16355));

  public static final NodeId FileSystem_MoveOrCopy = new NodeId(UShort.MIN, uint(16356));

  public static final NodeId FileSystem_MoveOrCopy_InputArguments =
      new NodeId(UShort.MIN, uint(16357));

  public static final NodeId FileSystem_MoveOrCopy_OutputArguments =
      new NodeId(UShort.MIN, uint(16358));

  public static final NodeId TemporaryFileTransferType_GenerateFileForWrite_InputArguments =
      new NodeId(UShort.MIN, uint(16359));

  public static final NodeId GenerateFileForWriteMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(16360));

  public static final NodeId HasAlarmSuppressionGroup = new NodeId(UShort.MIN, uint(16361));

  public static final NodeId AlarmGroupMember = new NodeId(UShort.MIN, uint(16362));

  public static final NodeId ConditionType_ConditionSubClassId =
      new NodeId(UShort.MIN, uint(16363));

  public static final NodeId ConditionType_ConditionSubClassName =
      new NodeId(UShort.MIN, uint(16364));

  public static final NodeId DialogConditionType_ConditionSubClassId =
      new NodeId(UShort.MIN, uint(16365));

  public static final NodeId DialogConditionType_ConditionSubClassName =
      new NodeId(UShort.MIN, uint(16366));

  public static final NodeId AcknowledgeableConditionType_ConditionSubClassId =
      new NodeId(UShort.MIN, uint(16367));

  public static final NodeId AcknowledgeableConditionType_ConditionSubClassName =
      new NodeId(UShort.MIN, uint(16368));

  public static final NodeId AlarmConditionType_ConditionSubClassId =
      new NodeId(UShort.MIN, uint(16369));

  public static final NodeId AlarmConditionType_ConditionSubClassName =
      new NodeId(UShort.MIN, uint(16370));

  public static final NodeId AlarmConditionType_OutOfServiceState =
      new NodeId(UShort.MIN, uint(16371));

  public static final NodeId AlarmConditionType_OutOfServiceState_Id =
      new NodeId(UShort.MIN, uint(16372));

  public static final NodeId AlarmConditionType_OutOfServiceState_Name =
      new NodeId(UShort.MIN, uint(16373));

  public static final NodeId AlarmConditionType_OutOfServiceState_Number =
      new NodeId(UShort.MIN, uint(16374));

  public static final NodeId AlarmConditionType_OutOfServiceState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(16375));

  public static final NodeId AlarmConditionType_OutOfServiceState_TransitionTime =
      new NodeId(UShort.MIN, uint(16376));

  public static final NodeId AlarmConditionType_OutOfServiceState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(16377));

  public static final NodeId AlarmConditionType_OutOfServiceState_TrueState =
      new NodeId(UShort.MIN, uint(16378));

  public static final NodeId AlarmConditionType_OutOfServiceState_FalseState =
      new NodeId(UShort.MIN, uint(16379));

  public static final NodeId AlarmConditionType_SilenceState = new NodeId(UShort.MIN, uint(16380));

  public static final NodeId AlarmConditionType_SilenceState_Id =
      new NodeId(UShort.MIN, uint(16381));

  public static final NodeId AlarmConditionType_SilenceState_Name =
      new NodeId(UShort.MIN, uint(16382));

  public static final NodeId AlarmConditionType_SilenceState_Number =
      new NodeId(UShort.MIN, uint(16383));

  public static final NodeId AlarmConditionType_SilenceState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(16384));

  public static final NodeId AlarmConditionType_SilenceState_TransitionTime =
      new NodeId(UShort.MIN, uint(16385));

  public static final NodeId AlarmConditionType_SilenceState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(16386));

  public static final NodeId AlarmConditionType_SilenceState_TrueState =
      new NodeId(UShort.MIN, uint(16387));

  public static final NodeId AlarmConditionType_SilenceState_FalseState =
      new NodeId(UShort.MIN, uint(16388));

  public static final NodeId AlarmConditionType_AudibleEnabled =
      new NodeId(UShort.MIN, uint(16389));

  public static final NodeId AlarmConditionType_AudibleSound = new NodeId(UShort.MIN, uint(16390));

  public static final NodeId UadpDataSetWriterMessageDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(16391));

  public static final NodeId UadpDataSetReaderMessageDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(16392));

  public static final NodeId JsonWriterGroupMessageDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(16393));

  public static final NodeId JsonDataSetWriterMessageDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(16394));

  public static final NodeId AlarmConditionType_OnDelay = new NodeId(UShort.MIN, uint(16395));

  public static final NodeId AlarmConditionType_OffDelay = new NodeId(UShort.MIN, uint(16396));

  public static final NodeId AlarmConditionType_FirstInGroupFlag =
      new NodeId(UShort.MIN, uint(16397));

  public static final NodeId AlarmConditionType_FirstInGroup = new NodeId(UShort.MIN, uint(16398));

  public static final NodeId AlarmConditionType_AlarmGroup_Placeholder =
      new NodeId(UShort.MIN, uint(16399));

  public static final NodeId AlarmConditionType_ReAlarmTime = new NodeId(UShort.MIN, uint(16400));

  public static final NodeId AlarmConditionType_ReAlarmRepeatCount =
      new NodeId(UShort.MIN, uint(16401));

  public static final NodeId AlarmConditionType_Silence = new NodeId(UShort.MIN, uint(16402));

  public static final NodeId AlarmConditionType_Suppress = new NodeId(UShort.MIN, uint(16403));

  public static final NodeId JsonDataSetReaderMessageDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(16404));

  public static final NodeId AlarmGroupType = new NodeId(UShort.MIN, uint(16405));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder =
      new NodeId(UShort.MIN, uint(16406));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_EventId =
      new NodeId(UShort.MIN, uint(16407));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_EventType =
      new NodeId(UShort.MIN, uint(16408));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_SourceNode =
      new NodeId(UShort.MIN, uint(16409));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_SourceName =
      new NodeId(UShort.MIN, uint(16410));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_Time =
      new NodeId(UShort.MIN, uint(16411));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_ReceiveTime =
      new NodeId(UShort.MIN, uint(16412));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_LocalTime =
      new NodeId(UShort.MIN, uint(16413));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_Message =
      new NodeId(UShort.MIN, uint(16414));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_Severity =
      new NodeId(UShort.MIN, uint(16415));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_ConditionClassId =
      new NodeId(UShort.MIN, uint(16416));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_ConditionClassName =
      new NodeId(UShort.MIN, uint(16417));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_ConditionSubClassId =
      new NodeId(UShort.MIN, uint(16418));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_ConditionSubClassName =
          new NodeId(UShort.MIN, uint(16419));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_ConditionName =
      new NodeId(UShort.MIN, uint(16420));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_BranchId =
      new NodeId(UShort.MIN, uint(16421));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_Retain =
      new NodeId(UShort.MIN, uint(16422));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_EnabledState =
      new NodeId(UShort.MIN, uint(16423));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_EnabledState_Id =
      new NodeId(UShort.MIN, uint(16424));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_EnabledState_Name =
      new NodeId(UShort.MIN, uint(16425));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_EnabledState_Number =
      new NodeId(UShort.MIN, uint(16426));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_EnabledState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(16427));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_EnabledState_TransitionTime =
          new NodeId(UShort.MIN, uint(16428));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_EnabledState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(16429));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_EnabledState_TrueState =
          new NodeId(UShort.MIN, uint(16430));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_EnabledState_FalseState =
          new NodeId(UShort.MIN, uint(16431));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_Quality =
      new NodeId(UShort.MIN, uint(16432));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_Quality_SourceTimestamp =
          new NodeId(UShort.MIN, uint(16433));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_LastSeverity =
      new NodeId(UShort.MIN, uint(16434));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_LastSeverity_SourceTimestamp =
          new NodeId(UShort.MIN, uint(16435));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_Comment =
      new NodeId(UShort.MIN, uint(16436));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_Comment_SourceTimestamp =
          new NodeId(UShort.MIN, uint(16437));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_ClientUserId =
      new NodeId(UShort.MIN, uint(16438));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_Disable =
      new NodeId(UShort.MIN, uint(16439));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_Enable =
      new NodeId(UShort.MIN, uint(16440));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_AddComment =
      new NodeId(UShort.MIN, uint(16441));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_AddComment_InputArguments =
          new NodeId(UShort.MIN, uint(16442));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_AckedState =
      new NodeId(UShort.MIN, uint(16443));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_AckedState_Id =
      new NodeId(UShort.MIN, uint(16444));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_AckedState_Name =
      new NodeId(UShort.MIN, uint(16445));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_AckedState_Number =
      new NodeId(UShort.MIN, uint(16446));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_AckedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(16447));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_AckedState_TransitionTime =
          new NodeId(UShort.MIN, uint(16448));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_AckedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(16449));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_AckedState_TrueState =
          new NodeId(UShort.MIN, uint(16450));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_AckedState_FalseState =
          new NodeId(UShort.MIN, uint(16451));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_ConfirmedState =
      new NodeId(UShort.MIN, uint(16452));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_ConfirmedState_Id =
      new NodeId(UShort.MIN, uint(16453));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_ConfirmedState_Name =
      new NodeId(UShort.MIN, uint(16454));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_ConfirmedState_Number =
          new NodeId(UShort.MIN, uint(16455));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_ConfirmedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(16456));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_ConfirmedState_TransitionTime =
          new NodeId(UShort.MIN, uint(16457));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_ConfirmedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(16458));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_ConfirmedState_TrueState =
          new NodeId(UShort.MIN, uint(16459));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_ConfirmedState_FalseState =
          new NodeId(UShort.MIN, uint(16460));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_Acknowledge =
      new NodeId(UShort.MIN, uint(16461));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_Acknowledge_InputArguments =
          new NodeId(UShort.MIN, uint(16462));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_Confirm =
      new NodeId(UShort.MIN, uint(16463));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_Confirm_InputArguments =
          new NodeId(UShort.MIN, uint(16464));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_ActiveState =
      new NodeId(UShort.MIN, uint(16465));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_ActiveState_Id =
      new NodeId(UShort.MIN, uint(16466));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_ActiveState_Name =
      new NodeId(UShort.MIN, uint(16467));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_ActiveState_Number =
      new NodeId(UShort.MIN, uint(16468));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_ActiveState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(16469));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_ActiveState_TransitionTime =
          new NodeId(UShort.MIN, uint(16470));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_ActiveState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(16471));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_ActiveState_TrueState =
          new NodeId(UShort.MIN, uint(16472));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_ActiveState_FalseState =
          new NodeId(UShort.MIN, uint(16473));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_InputNode =
      new NodeId(UShort.MIN, uint(16474));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_SuppressedState =
      new NodeId(UShort.MIN, uint(16475));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_SuppressedState_Id =
      new NodeId(UShort.MIN, uint(16476));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_SuppressedState_Name =
          new NodeId(UShort.MIN, uint(16477));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_SuppressedState_Number =
          new NodeId(UShort.MIN, uint(16478));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_SuppressedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(16479));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_SuppressedState_TransitionTime =
          new NodeId(UShort.MIN, uint(16480));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_SuppressedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(16481));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_SuppressedState_TrueState =
          new NodeId(UShort.MIN, uint(16482));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_SuppressedState_FalseState =
          new NodeId(UShort.MIN, uint(16483));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_OutOfServiceState =
      new NodeId(UShort.MIN, uint(16484));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_OutOfServiceState_Id =
          new NodeId(UShort.MIN, uint(16485));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_OutOfServiceState_Name =
          new NodeId(UShort.MIN, uint(16486));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_OutOfServiceState_Number =
          new NodeId(UShort.MIN, uint(16487));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_OutOfServiceState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(16488));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_OutOfServiceState_TransitionTime =
          new NodeId(UShort.MIN, uint(16489));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_OutOfServiceState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(16490));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_OutOfServiceState_TrueState =
          new NodeId(UShort.MIN, uint(16491));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_OutOfServiceState_FalseState =
          new NodeId(UShort.MIN, uint(16492));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_SilenceState =
      new NodeId(UShort.MIN, uint(16493));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_SilenceState_Id =
      new NodeId(UShort.MIN, uint(16494));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_SilenceState_Name =
      new NodeId(UShort.MIN, uint(16495));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_SilenceState_Number =
      new NodeId(UShort.MIN, uint(16496));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_SilenceState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(16497));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_SilenceState_TransitionTime =
          new NodeId(UShort.MIN, uint(16498));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_SilenceState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(16499));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_SilenceState_TrueState =
          new NodeId(UShort.MIN, uint(16500));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_SilenceState_FalseState =
          new NodeId(UShort.MIN, uint(16501));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_ShelvingState =
      new NodeId(UShort.MIN, uint(16502));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_ShelvingState_CurrentState =
          new NodeId(UShort.MIN, uint(16503));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_ShelvingState_CurrentState_Id =
          new NodeId(UShort.MIN, uint(16504));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_ShelvingState_CurrentState_Name =
          new NodeId(UShort.MIN, uint(16505));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_ShelvingState_CurrentState_Number =
          new NodeId(UShort.MIN, uint(16506));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_ShelvingState_CurrentState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(16507));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_ShelvingState_LastTransition =
          new NodeId(UShort.MIN, uint(16508));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_ShelvingState_LastTransition_Id =
          new NodeId(UShort.MIN, uint(16509));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_ShelvingState_LastTransition_Name =
          new NodeId(UShort.MIN, uint(16510));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_ShelvingState_LastTransition_Number =
          new NodeId(UShort.MIN, uint(16511));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_ShelvingState_LastTransition_TransitionTime =
          new NodeId(UShort.MIN, uint(16512));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_ShelvingState_LastTransition_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(16513));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_ShelvingState_UnshelveTime =
          new NodeId(UShort.MIN, uint(16514));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_ShelvingState_Unshelve =
          new NodeId(UShort.MIN, uint(16515));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_ShelvingState_OneShotShelve =
          new NodeId(UShort.MIN, uint(16516));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_ShelvingState_TimedShelve =
          new NodeId(UShort.MIN, uint(16517));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_ShelvingState_TimedShelve_InputArguments =
          new NodeId(UShort.MIN, uint(16518));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_SuppressedOrShelved =
      new NodeId(UShort.MIN, uint(16519));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_MaxTimeShelved =
      new NodeId(UShort.MIN, uint(16520));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_AudibleEnabled =
      new NodeId(UShort.MIN, uint(16521));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_AudibleSound =
      new NodeId(UShort.MIN, uint(16522));

  public static final NodeId
      PublishSubscribe_ConnectionName_Placeholder_Diagnostics_Counters_StateOperationalByMethod_Classification =
          new NodeId(UShort.MIN, uint(16523));

  public static final NodeId BrokerWriterGroupTransportDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(16524));

  public static final NodeId BrokerDataSetWriterTransportDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(16525));

  public static final NodeId BrokerDataSetReaderTransportDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(16526));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_OnDelay =
      new NodeId(UShort.MIN, uint(16527));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_OffDelay =
      new NodeId(UShort.MIN, uint(16528));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_FirstInGroupFlag =
      new NodeId(UShort.MIN, uint(16529));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_FirstInGroup =
      new NodeId(UShort.MIN, uint(16530));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_ReAlarmTime =
      new NodeId(UShort.MIN, uint(16531));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_ReAlarmRepeatCount =
      new NodeId(UShort.MIN, uint(16532));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_Silence =
      new NodeId(UShort.MIN, uint(16533));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_Suppress =
      new NodeId(UShort.MIN, uint(16534));

  public static final NodeId PublishSubscribeType_ConnectionName_Placeholder_AddWriterGroup =
      new NodeId(UShort.MIN, uint(16535));

  public static final NodeId LimitAlarmType_ConditionSubClassId =
      new NodeId(UShort.MIN, uint(16536));

  public static final NodeId LimitAlarmType_ConditionSubClassName =
      new NodeId(UShort.MIN, uint(16537));

  public static final NodeId LimitAlarmType_OutOfServiceState = new NodeId(UShort.MIN, uint(16538));

  public static final NodeId LimitAlarmType_OutOfServiceState_Id =
      new NodeId(UShort.MIN, uint(16539));

  public static final NodeId LimitAlarmType_OutOfServiceState_Name =
      new NodeId(UShort.MIN, uint(16540));

  public static final NodeId LimitAlarmType_OutOfServiceState_Number =
      new NodeId(UShort.MIN, uint(16541));

  public static final NodeId LimitAlarmType_OutOfServiceState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(16542));

  public static final NodeId LimitAlarmType_OutOfServiceState_TransitionTime =
      new NodeId(UShort.MIN, uint(16543));

  public static final NodeId LimitAlarmType_OutOfServiceState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(16544));

  public static final NodeId LimitAlarmType_OutOfServiceState_TrueState =
      new NodeId(UShort.MIN, uint(16545));

  public static final NodeId LimitAlarmType_OutOfServiceState_FalseState =
      new NodeId(UShort.MIN, uint(16546));

  public static final NodeId LimitAlarmType_SilenceState = new NodeId(UShort.MIN, uint(16547));

  public static final NodeId LimitAlarmType_SilenceState_Id = new NodeId(UShort.MIN, uint(16548));

  public static final NodeId LimitAlarmType_SilenceState_Name = new NodeId(UShort.MIN, uint(16549));

  public static final NodeId LimitAlarmType_SilenceState_Number =
      new NodeId(UShort.MIN, uint(16550));

  public static final NodeId LimitAlarmType_SilenceState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(16551));

  public static final NodeId LimitAlarmType_SilenceState_TransitionTime =
      new NodeId(UShort.MIN, uint(16552));

  public static final NodeId LimitAlarmType_SilenceState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(16553));

  public static final NodeId LimitAlarmType_SilenceState_TrueState =
      new NodeId(UShort.MIN, uint(16554));

  public static final NodeId LimitAlarmType_SilenceState_FalseState =
      new NodeId(UShort.MIN, uint(16555));

  public static final NodeId LimitAlarmType_AudibleEnabled = new NodeId(UShort.MIN, uint(16556));

  public static final NodeId LimitAlarmType_AudibleSound = new NodeId(UShort.MIN, uint(16557));

  public static final NodeId
      PublishSubscribeType_ConnectionName_Placeholder_AddWriterGroup_InputArguments =
          new NodeId(UShort.MIN, uint(16558));

  public static final NodeId
      PublishSubscribeType_ConnectionName_Placeholder_AddWriterGroup_OutputArguments =
          new NodeId(UShort.MIN, uint(16559));

  public static final NodeId PublishSubscribeType_ConnectionName_Placeholder_AddReaderGroup =
      new NodeId(UShort.MIN, uint(16560));

  public static final NodeId
      PublishSubscribeType_ConnectionName_Placeholder_AddReaderGroup_InputArguments =
          new NodeId(UShort.MIN, uint(16561));

  public static final NodeId LimitAlarmType_OnDelay = new NodeId(UShort.MIN, uint(16562));

  public static final NodeId LimitAlarmType_OffDelay = new NodeId(UShort.MIN, uint(16563));

  public static final NodeId LimitAlarmType_FirstInGroupFlag = new NodeId(UShort.MIN, uint(16564));

  public static final NodeId LimitAlarmType_FirstInGroup = new NodeId(UShort.MIN, uint(16565));

  public static final NodeId LimitAlarmType_AlarmGroup_Placeholder =
      new NodeId(UShort.MIN, uint(16566));

  public static final NodeId LimitAlarmType_ReAlarmTime = new NodeId(UShort.MIN, uint(16567));

  public static final NodeId LimitAlarmType_ReAlarmRepeatCount =
      new NodeId(UShort.MIN, uint(16568));

  public static final NodeId LimitAlarmType_Silence = new NodeId(UShort.MIN, uint(16569));

  public static final NodeId LimitAlarmType_Suppress = new NodeId(UShort.MIN, uint(16570));

  public static final NodeId
      PublishSubscribeType_ConnectionName_Placeholder_AddReaderGroup_OutputArguments =
          new NodeId(UShort.MIN, uint(16571));

  public static final NodeId LimitAlarmType_BaseHighHighLimit = new NodeId(UShort.MIN, uint(16572));

  public static final NodeId LimitAlarmType_BaseHighLimit = new NodeId(UShort.MIN, uint(16573));

  public static final NodeId LimitAlarmType_BaseLowLimit = new NodeId(UShort.MIN, uint(16574));

  public static final NodeId LimitAlarmType_BaseLowLowLimit = new NodeId(UShort.MIN, uint(16575));

  public static final NodeId ExclusiveLimitAlarmType_ConditionSubClassId =
      new NodeId(UShort.MIN, uint(16576));

  public static final NodeId ExclusiveLimitAlarmType_ConditionSubClassName =
      new NodeId(UShort.MIN, uint(16577));

  public static final NodeId ExclusiveLimitAlarmType_OutOfServiceState =
      new NodeId(UShort.MIN, uint(16578));

  public static final NodeId ExclusiveLimitAlarmType_OutOfServiceState_Id =
      new NodeId(UShort.MIN, uint(16579));

  public static final NodeId ExclusiveLimitAlarmType_OutOfServiceState_Name =
      new NodeId(UShort.MIN, uint(16580));

  public static final NodeId ExclusiveLimitAlarmType_OutOfServiceState_Number =
      new NodeId(UShort.MIN, uint(16581));

  public static final NodeId ExclusiveLimitAlarmType_OutOfServiceState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(16582));

  public static final NodeId ExclusiveLimitAlarmType_OutOfServiceState_TransitionTime =
      new NodeId(UShort.MIN, uint(16583));

  public static final NodeId ExclusiveLimitAlarmType_OutOfServiceState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(16584));

  public static final NodeId ExclusiveLimitAlarmType_OutOfServiceState_TrueState =
      new NodeId(UShort.MIN, uint(16585));

  public static final NodeId ExclusiveLimitAlarmType_OutOfServiceState_FalseState =
      new NodeId(UShort.MIN, uint(16586));

  public static final NodeId ExclusiveLimitAlarmType_SilenceState =
      new NodeId(UShort.MIN, uint(16587));

  public static final NodeId ExclusiveLimitAlarmType_SilenceState_Id =
      new NodeId(UShort.MIN, uint(16588));

  public static final NodeId ExclusiveLimitAlarmType_SilenceState_Name =
      new NodeId(UShort.MIN, uint(16589));

  public static final NodeId ExclusiveLimitAlarmType_SilenceState_Number =
      new NodeId(UShort.MIN, uint(16590));

  public static final NodeId ExclusiveLimitAlarmType_SilenceState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(16591));

  public static final NodeId ExclusiveLimitAlarmType_SilenceState_TransitionTime =
      new NodeId(UShort.MIN, uint(16592));

  public static final NodeId ExclusiveLimitAlarmType_SilenceState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(16593));

  public static final NodeId ExclusiveLimitAlarmType_SilenceState_TrueState =
      new NodeId(UShort.MIN, uint(16594));

  public static final NodeId ExclusiveLimitAlarmType_SilenceState_FalseState =
      new NodeId(UShort.MIN, uint(16595));

  public static final NodeId ExclusiveLimitAlarmType_AudibleEnabled =
      new NodeId(UShort.MIN, uint(16596));

  public static final NodeId ExclusiveLimitAlarmType_AudibleSound =
      new NodeId(UShort.MIN, uint(16597));

  public static final NodeId PublishSubscribeType_AddConnection =
      new NodeId(UShort.MIN, uint(16598));

  public static final NodeId PublishSubscribeType_AddConnection_InputArguments =
      new NodeId(UShort.MIN, uint(16599));

  public static final NodeId PublishSubscribeType_AddConnection_OutputArguments =
      new NodeId(UShort.MIN, uint(16600));

  public static final NodeId PublishSubscribeType_PublishedDataSets_AddPublishedDataItemsTemplate =
      new NodeId(UShort.MIN, uint(16601));

  public static final NodeId ExclusiveLimitAlarmType_OnDelay = new NodeId(UShort.MIN, uint(16602));

  public static final NodeId ExclusiveLimitAlarmType_OffDelay = new NodeId(UShort.MIN, uint(16603));

  public static final NodeId ExclusiveLimitAlarmType_FirstInGroupFlag =
      new NodeId(UShort.MIN, uint(16604));

  public static final NodeId ExclusiveLimitAlarmType_FirstInGroup =
      new NodeId(UShort.MIN, uint(16605));

  public static final NodeId ExclusiveLimitAlarmType_AlarmGroup_Placeholder =
      new NodeId(UShort.MIN, uint(16606));

  public static final NodeId ExclusiveLimitAlarmType_ReAlarmTime =
      new NodeId(UShort.MIN, uint(16607));

  public static final NodeId ExclusiveLimitAlarmType_ReAlarmRepeatCount =
      new NodeId(UShort.MIN, uint(16608));

  public static final NodeId ExclusiveLimitAlarmType_Silence = new NodeId(UShort.MIN, uint(16609));

  public static final NodeId ExclusiveLimitAlarmType_Suppress = new NodeId(UShort.MIN, uint(16610));

  public static final NodeId
      PublishSubscribeType_PublishedDataSets_AddPublishedDataItemsTemplate_InputArguments =
          new NodeId(UShort.MIN, uint(16611));

  public static final NodeId ExclusiveLimitAlarmType_BaseHighHighLimit =
      new NodeId(UShort.MIN, uint(16612));

  public static final NodeId ExclusiveLimitAlarmType_BaseHighLimit =
      new NodeId(UShort.MIN, uint(16613));

  public static final NodeId ExclusiveLimitAlarmType_BaseLowLimit =
      new NodeId(UShort.MIN, uint(16614));

  public static final NodeId ExclusiveLimitAlarmType_BaseLowLowLimit =
      new NodeId(UShort.MIN, uint(16615));

  public static final NodeId NonExclusiveLimitAlarmType_ConditionSubClassId =
      new NodeId(UShort.MIN, uint(16616));

  public static final NodeId NonExclusiveLimitAlarmType_ConditionSubClassName =
      new NodeId(UShort.MIN, uint(16617));

  public static final NodeId NonExclusiveLimitAlarmType_OutOfServiceState =
      new NodeId(UShort.MIN, uint(16618));

  public static final NodeId NonExclusiveLimitAlarmType_OutOfServiceState_Id =
      new NodeId(UShort.MIN, uint(16619));

  public static final NodeId NonExclusiveLimitAlarmType_OutOfServiceState_Name =
      new NodeId(UShort.MIN, uint(16620));

  public static final NodeId NonExclusiveLimitAlarmType_OutOfServiceState_Number =
      new NodeId(UShort.MIN, uint(16621));

  public static final NodeId NonExclusiveLimitAlarmType_OutOfServiceState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(16622));

  public static final NodeId NonExclusiveLimitAlarmType_OutOfServiceState_TransitionTime =
      new NodeId(UShort.MIN, uint(16623));

  public static final NodeId NonExclusiveLimitAlarmType_OutOfServiceState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(16624));

  public static final NodeId NonExclusiveLimitAlarmType_OutOfServiceState_TrueState =
      new NodeId(UShort.MIN, uint(16625));

  public static final NodeId NonExclusiveLimitAlarmType_OutOfServiceState_FalseState =
      new NodeId(UShort.MIN, uint(16626));

  public static final NodeId NonExclusiveLimitAlarmType_SilenceState =
      new NodeId(UShort.MIN, uint(16627));

  public static final NodeId NonExclusiveLimitAlarmType_SilenceState_Id =
      new NodeId(UShort.MIN, uint(16628));

  public static final NodeId NonExclusiveLimitAlarmType_SilenceState_Name =
      new NodeId(UShort.MIN, uint(16629));

  public static final NodeId NonExclusiveLimitAlarmType_SilenceState_Number =
      new NodeId(UShort.MIN, uint(16630));

  public static final NodeId NonExclusiveLimitAlarmType_SilenceState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(16631));

  public static final NodeId NonExclusiveLimitAlarmType_SilenceState_TransitionTime =
      new NodeId(UShort.MIN, uint(16632));

  public static final NodeId NonExclusiveLimitAlarmType_SilenceState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(16633));

  public static final NodeId NonExclusiveLimitAlarmType_SilenceState_TrueState =
      new NodeId(UShort.MIN, uint(16634));

  public static final NodeId NonExclusiveLimitAlarmType_SilenceState_FalseState =
      new NodeId(UShort.MIN, uint(16635));

  public static final NodeId NonExclusiveLimitAlarmType_AudibleEnabled =
      new NodeId(UShort.MIN, uint(16636));

  public static final NodeId NonExclusiveLimitAlarmType_AudibleSound =
      new NodeId(UShort.MIN, uint(16637));

  public static final NodeId
      PublishSubscribeType_PublishedDataSets_AddPublishedDataItemsTemplate_OutputArguments =
          new NodeId(UShort.MIN, uint(16638));

  public static final NodeId PublishSubscribeType_PublishedDataSets_AddPublishedEventsTemplate =
      new NodeId(UShort.MIN, uint(16639));

  public static final NodeId
      PublishSubscribeType_PublishedDataSets_AddPublishedEventsTemplate_InputArguments =
          new NodeId(UShort.MIN, uint(16640));

  public static final NodeId
      PublishSubscribeType_PublishedDataSets_AddPublishedEventsTemplate_OutputArguments =
          new NodeId(UShort.MIN, uint(16641));

  public static final NodeId NonExclusiveLimitAlarmType_OnDelay =
      new NodeId(UShort.MIN, uint(16642));

  public static final NodeId NonExclusiveLimitAlarmType_OffDelay =
      new NodeId(UShort.MIN, uint(16643));

  public static final NodeId NonExclusiveLimitAlarmType_FirstInGroupFlag =
      new NodeId(UShort.MIN, uint(16644));

  public static final NodeId NonExclusiveLimitAlarmType_FirstInGroup =
      new NodeId(UShort.MIN, uint(16645));

  public static final NodeId NonExclusiveLimitAlarmType_AlarmGroup_Placeholder =
      new NodeId(UShort.MIN, uint(16646));

  public static final NodeId NonExclusiveLimitAlarmType_ReAlarmTime =
      new NodeId(UShort.MIN, uint(16647));

  public static final NodeId NonExclusiveLimitAlarmType_ReAlarmRepeatCount =
      new NodeId(UShort.MIN, uint(16648));

  public static final NodeId NonExclusiveLimitAlarmType_Silence =
      new NodeId(UShort.MIN, uint(16649));

  public static final NodeId NonExclusiveLimitAlarmType_Suppress =
      new NodeId(UShort.MIN, uint(16650));

  public static final NodeId PublishSubscribeType_PublishedDataSets_AddDataSetFolder =
      new NodeId(UShort.MIN, uint(16651));

  public static final NodeId NonExclusiveLimitAlarmType_BaseHighHighLimit =
      new NodeId(UShort.MIN, uint(16652));

  public static final NodeId NonExclusiveLimitAlarmType_BaseHighLimit =
      new NodeId(UShort.MIN, uint(16653));

  public static final NodeId NonExclusiveLimitAlarmType_BaseLowLimit =
      new NodeId(UShort.MIN, uint(16654));

  public static final NodeId NonExclusiveLimitAlarmType_BaseLowLowLimit =
      new NodeId(UShort.MIN, uint(16655));

  public static final NodeId NonExclusiveLevelAlarmType_ConditionSubClassId =
      new NodeId(UShort.MIN, uint(16656));

  public static final NodeId NonExclusiveLevelAlarmType_ConditionSubClassName =
      new NodeId(UShort.MIN, uint(16657));

  public static final NodeId NonExclusiveLevelAlarmType_OutOfServiceState =
      new NodeId(UShort.MIN, uint(16658));

  public static final NodeId NonExclusiveLevelAlarmType_OutOfServiceState_Id =
      new NodeId(UShort.MIN, uint(16659));

  public static final NodeId NonExclusiveLevelAlarmType_OutOfServiceState_Name =
      new NodeId(UShort.MIN, uint(16660));

  public static final NodeId NonExclusiveLevelAlarmType_OutOfServiceState_Number =
      new NodeId(UShort.MIN, uint(16661));

  public static final NodeId NonExclusiveLevelAlarmType_OutOfServiceState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(16662));

  public static final NodeId NonExclusiveLevelAlarmType_OutOfServiceState_TransitionTime =
      new NodeId(UShort.MIN, uint(16663));

  public static final NodeId NonExclusiveLevelAlarmType_OutOfServiceState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(16664));

  public static final NodeId NonExclusiveLevelAlarmType_OutOfServiceState_TrueState =
      new NodeId(UShort.MIN, uint(16665));

  public static final NodeId NonExclusiveLevelAlarmType_OutOfServiceState_FalseState =
      new NodeId(UShort.MIN, uint(16666));

  public static final NodeId NonExclusiveLevelAlarmType_SilenceState =
      new NodeId(UShort.MIN, uint(16667));

  public static final NodeId NonExclusiveLevelAlarmType_SilenceState_Id =
      new NodeId(UShort.MIN, uint(16668));

  public static final NodeId NonExclusiveLevelAlarmType_SilenceState_Name =
      new NodeId(UShort.MIN, uint(16669));

  public static final NodeId NonExclusiveLevelAlarmType_SilenceState_Number =
      new NodeId(UShort.MIN, uint(16670));

  public static final NodeId NonExclusiveLevelAlarmType_SilenceState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(16671));

  public static final NodeId NonExclusiveLevelAlarmType_SilenceState_TransitionTime =
      new NodeId(UShort.MIN, uint(16672));

  public static final NodeId NonExclusiveLevelAlarmType_SilenceState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(16673));

  public static final NodeId NonExclusiveLevelAlarmType_SilenceState_TrueState =
      new NodeId(UShort.MIN, uint(16674));

  public static final NodeId NonExclusiveLevelAlarmType_SilenceState_FalseState =
      new NodeId(UShort.MIN, uint(16675));

  public static final NodeId NonExclusiveLevelAlarmType_AudibleEnabled =
      new NodeId(UShort.MIN, uint(16676));

  public static final NodeId NonExclusiveLevelAlarmType_AudibleSound =
      new NodeId(UShort.MIN, uint(16677));

  public static final NodeId
      PublishSubscribeType_PublishedDataSets_AddDataSetFolder_InputArguments =
          new NodeId(UShort.MIN, uint(16678));

  public static final NodeId
      PublishSubscribeType_PublishedDataSets_AddDataSetFolder_OutputArguments =
          new NodeId(UShort.MIN, uint(16679));

  public static final NodeId PublishSubscribeType_PublishedDataSets_RemoveDataSetFolder =
      new NodeId(UShort.MIN, uint(16680));

  public static final NodeId
      PublishSubscribeType_PublishedDataSets_RemoveDataSetFolder_InputArguments =
          new NodeId(UShort.MIN, uint(16681));

  public static final NodeId NonExclusiveLevelAlarmType_OnDelay =
      new NodeId(UShort.MIN, uint(16682));

  public static final NodeId NonExclusiveLevelAlarmType_OffDelay =
      new NodeId(UShort.MIN, uint(16683));
}

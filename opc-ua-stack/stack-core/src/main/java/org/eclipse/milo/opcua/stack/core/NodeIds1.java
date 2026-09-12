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

abstract class NodeIds1 extends NodeIds2 {
  public static final NodeId ServerConfigurationType_CreateSigningRequest =
      new NodeId(UShort.MIN, uint(12731L));

  public static final NodeId ServerConfigurationType_CreateSigningRequest_InputArguments =
      new NodeId(UShort.MIN, uint(12732L));

  public static final NodeId ServerConfigurationType_CreateSigningRequest_OutputArguments =
      new NodeId(UShort.MIN, uint(12733L));

  public static final NodeId ServerConfigurationType_ApplyChanges =
      new NodeId(UShort.MIN, uint(12734L));

  public static final NodeId ServerConfiguration_CreateSigningRequest =
      new NodeId(UShort.MIN, uint(12737L));

  public static final NodeId ServerConfiguration_CreateSigningRequest_InputArguments =
      new NodeId(UShort.MIN, uint(12738L));

  public static final NodeId ServerConfiguration_CreateSigningRequest_OutputArguments =
      new NodeId(UShort.MIN, uint(12739L));

  public static final NodeId ServerConfiguration_ApplyChanges =
      new NodeId(UShort.MIN, uint(12740L));

  public static final NodeId CreateSigningRequestMethodType = new NodeId(UShort.MIN, uint(12741L));

  public static final NodeId CreateSigningRequestMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(12742L));

  public static final NodeId CreateSigningRequestMethodType_OutputArguments =
      new NodeId(UShort.MIN, uint(12743L));

  public static final NodeId OptionSetValues = new NodeId(UShort.MIN, uint(12745L));

  public static final NodeId ServerType_SetSubscriptionDurable =
      new NodeId(UShort.MIN, uint(12746L));

  public static final NodeId ServerType_SetSubscriptionDurable_InputArguments =
      new NodeId(UShort.MIN, uint(12747L));

  public static final NodeId ServerType_SetSubscriptionDurable_OutputArguments =
      new NodeId(UShort.MIN, uint(12748L));

  public static final NodeId Server_SetSubscriptionDurable = new NodeId(UShort.MIN, uint(12749L));

  public static final NodeId Server_SetSubscriptionDurable_InputArguments =
      new NodeId(UShort.MIN, uint(12750L));

  public static final NodeId Server_SetSubscriptionDurable_OutputArguments =
      new NodeId(UShort.MIN, uint(12751L));

  public static final NodeId SetSubscriptionDurableMethodType =
      new NodeId(UShort.MIN, uint(12752L));

  public static final NodeId SetSubscriptionDurableMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(12753L));

  public static final NodeId SetSubscriptionDurableMethodType_OutputArguments =
      new NodeId(UShort.MIN, uint(12754L));

  public static final NodeId OptionSet = new NodeId(UShort.MIN, uint(12755L));

  public static final NodeId Union = new NodeId(UShort.MIN, uint(12756L));

  public static final NodeId OptionSet_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(12757L));

  public static final NodeId Union_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(12758L));

  public static final NodeId OpcUa_XmlSchema_OptionSet = new NodeId(UShort.MIN, uint(12759L));

  public static final NodeId OpcUa_XmlSchema_OptionSet_DataTypeVersion =
      new NodeId(UShort.MIN, uint(12760L));

  public static final NodeId OpcUa_XmlSchema_OptionSet_DictionaryFragment =
      new NodeId(UShort.MIN, uint(12761L));

  public static final NodeId OpcUa_XmlSchema_Union = new NodeId(UShort.MIN, uint(12762L));

  public static final NodeId OpcUa_XmlSchema_Union_DataTypeVersion =
      new NodeId(UShort.MIN, uint(12763L));

  public static final NodeId OpcUa_XmlSchema_Union_DictionaryFragment =
      new NodeId(UShort.MIN, uint(12764L));

  public static final NodeId OptionSet_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(12765L));

  public static final NodeId Union_Encoding_DefaultBinary = new NodeId(UShort.MIN, uint(12766L));

  public static final NodeId OpcUa_BinarySchema_OptionSet = new NodeId(UShort.MIN, uint(12767L));

  public static final NodeId OpcUa_BinarySchema_OptionSet_DataTypeVersion =
      new NodeId(UShort.MIN, uint(12768L));

  public static final NodeId OpcUa_BinarySchema_OptionSet_DictionaryFragment =
      new NodeId(UShort.MIN, uint(12769L));

  public static final NodeId OpcUa_BinarySchema_Union = new NodeId(UShort.MIN, uint(12770L));

  public static final NodeId OpcUa_BinarySchema_Union_DataTypeVersion =
      new NodeId(UShort.MIN, uint(12771L));

  public static final NodeId OpcUa_BinarySchema_Union_DictionaryFragment =
      new NodeId(UShort.MIN, uint(12772L));

  public static final NodeId GetRejectedListMethodType = new NodeId(UShort.MIN, uint(12773L));

  public static final NodeId GetRejectedListMethodType_OutputArguments =
      new NodeId(UShort.MIN, uint(12774L));

  public static final NodeId ServerConfigurationType_GetRejectedList =
      new NodeId(UShort.MIN, uint(12775L));

  public static final NodeId ServerConfigurationType_GetRejectedList_OutputArguments =
      new NodeId(UShort.MIN, uint(12776L));

  public static final NodeId ServerConfiguration_GetRejectedList =
      new NodeId(UShort.MIN, uint(12777L));

  public static final NodeId ServerConfiguration_GetRejectedList_OutputArguments =
      new NodeId(UShort.MIN, uint(12778L));

  public static final NodeId SamplingIntervalDiagnosticsArrayType_SamplingIntervalDiagnostics =
      new NodeId(UShort.MIN, uint(12779L));

  public static final NodeId
      SamplingIntervalDiagnosticsArrayType_SamplingIntervalDiagnostics_SamplingInterval =
          new NodeId(UShort.MIN, uint(12780L));

  public static final NodeId
      SamplingIntervalDiagnosticsArrayType_SamplingIntervalDiagnostics_SampledMonitoredItemsCount =
          new NodeId(UShort.MIN, uint(12781L));

  public static final NodeId
      SamplingIntervalDiagnosticsArrayType_SamplingIntervalDiagnostics_MaxSampledMonitoredItemsCount =
          new NodeId(UShort.MIN, uint(12782L));

  public static final NodeId
      SamplingIntervalDiagnosticsArrayType_SamplingIntervalDiagnostics_DisabledMonitoredItemsSamplingCount =
          new NodeId(UShort.MIN, uint(12783L));

  public static final NodeId SubscriptionDiagnosticsArrayType_SubscriptionDiagnostics =
      new NodeId(UShort.MIN, uint(12784L));

  public static final NodeId SubscriptionDiagnosticsArrayType_SubscriptionDiagnostics_SessionId =
      new NodeId(UShort.MIN, uint(12785L));

  public static final NodeId
      SubscriptionDiagnosticsArrayType_SubscriptionDiagnostics_SubscriptionId =
          new NodeId(UShort.MIN, uint(12786L));

  public static final NodeId SubscriptionDiagnosticsArrayType_SubscriptionDiagnostics_Priority =
      new NodeId(UShort.MIN, uint(12787L));

  public static final NodeId
      SubscriptionDiagnosticsArrayType_SubscriptionDiagnostics_PublishingInterval =
          new NodeId(UShort.MIN, uint(12788L));

  public static final NodeId
      SubscriptionDiagnosticsArrayType_SubscriptionDiagnostics_MaxKeepAliveCount =
          new NodeId(UShort.MIN, uint(12789L));

  public static final NodeId
      SubscriptionDiagnosticsArrayType_SubscriptionDiagnostics_MaxLifetimeCount =
          new NodeId(UShort.MIN, uint(12790L));

  public static final NodeId
      SubscriptionDiagnosticsArrayType_SubscriptionDiagnostics_MaxNotificationsPerPublish =
          new NodeId(UShort.MIN, uint(12791L));

  public static final NodeId
      SubscriptionDiagnosticsArrayType_SubscriptionDiagnostics_PublishingEnabled =
          new NodeId(UShort.MIN, uint(12792L));

  public static final NodeId SubscriptionDiagnosticsArrayType_SubscriptionDiagnostics_ModifyCount =
      new NodeId(UShort.MIN, uint(12793L));

  public static final NodeId SubscriptionDiagnosticsArrayType_SubscriptionDiagnostics_EnableCount =
      new NodeId(UShort.MIN, uint(12794L));

  public static final NodeId SubscriptionDiagnosticsArrayType_SubscriptionDiagnostics_DisableCount =
      new NodeId(UShort.MIN, uint(12795L));

  public static final NodeId
      SubscriptionDiagnosticsArrayType_SubscriptionDiagnostics_RepublishRequestCount =
          new NodeId(UShort.MIN, uint(12796L));

  public static final NodeId
      SubscriptionDiagnosticsArrayType_SubscriptionDiagnostics_RepublishMessageRequestCount =
          new NodeId(UShort.MIN, uint(12797L));

  public static final NodeId
      SubscriptionDiagnosticsArrayType_SubscriptionDiagnostics_RepublishMessageCount =
          new NodeId(UShort.MIN, uint(12798L));

  public static final NodeId
      SubscriptionDiagnosticsArrayType_SubscriptionDiagnostics_TransferRequestCount =
          new NodeId(UShort.MIN, uint(12799L));

  public static final NodeId
      SubscriptionDiagnosticsArrayType_SubscriptionDiagnostics_TransferredToAltClientCount =
          new NodeId(UShort.MIN, uint(12800L));

  public static final NodeId
      SubscriptionDiagnosticsArrayType_SubscriptionDiagnostics_TransferredToSameClientCount =
          new NodeId(UShort.MIN, uint(12801L));

  public static final NodeId
      SubscriptionDiagnosticsArrayType_SubscriptionDiagnostics_PublishRequestCount =
          new NodeId(UShort.MIN, uint(12802L));

  public static final NodeId
      SubscriptionDiagnosticsArrayType_SubscriptionDiagnostics_DataChangeNotificationsCount =
          new NodeId(UShort.MIN, uint(12803L));

  public static final NodeId
      SubscriptionDiagnosticsArrayType_SubscriptionDiagnostics_EventNotificationsCount =
          new NodeId(UShort.MIN, uint(12804L));

  public static final NodeId
      SubscriptionDiagnosticsArrayType_SubscriptionDiagnostics_NotificationsCount =
          new NodeId(UShort.MIN, uint(12805L));

  public static final NodeId
      SubscriptionDiagnosticsArrayType_SubscriptionDiagnostics_LatePublishRequestCount =
          new NodeId(UShort.MIN, uint(12806L));

  public static final NodeId
      SubscriptionDiagnosticsArrayType_SubscriptionDiagnostics_CurrentKeepAliveCount =
          new NodeId(UShort.MIN, uint(12807L));

  public static final NodeId
      SubscriptionDiagnosticsArrayType_SubscriptionDiagnostics_CurrentLifetimeCount =
          new NodeId(UShort.MIN, uint(12808L));

  public static final NodeId
      SubscriptionDiagnosticsArrayType_SubscriptionDiagnostics_UnacknowledgedMessageCount =
          new NodeId(UShort.MIN, uint(12809L));

  public static final NodeId
      SubscriptionDiagnosticsArrayType_SubscriptionDiagnostics_DiscardedMessageCount =
          new NodeId(UShort.MIN, uint(12810L));

  public static final NodeId
      SubscriptionDiagnosticsArrayType_SubscriptionDiagnostics_MonitoredItemCount =
          new NodeId(UShort.MIN, uint(12811L));

  public static final NodeId
      SubscriptionDiagnosticsArrayType_SubscriptionDiagnostics_DisabledMonitoredItemCount =
          new NodeId(UShort.MIN, uint(12812L));

  public static final NodeId
      SubscriptionDiagnosticsArrayType_SubscriptionDiagnostics_MonitoringQueueOverflowCount =
          new NodeId(UShort.MIN, uint(12813L));

  public static final NodeId
      SubscriptionDiagnosticsArrayType_SubscriptionDiagnostics_NextSequenceNumber =
          new NodeId(UShort.MIN, uint(12814L));

  public static final NodeId
      SubscriptionDiagnosticsArrayType_SubscriptionDiagnostics_EventQueueOverflowCount =
          new NodeId(UShort.MIN, uint(12815L));

  public static final NodeId SessionDiagnosticsArrayType_SessionDiagnostics =
      new NodeId(UShort.MIN, uint(12816L));

  public static final NodeId SessionDiagnosticsArrayType_SessionDiagnostics_SessionId =
      new NodeId(UShort.MIN, uint(12817L));

  public static final NodeId SessionDiagnosticsArrayType_SessionDiagnostics_SessionName =
      new NodeId(UShort.MIN, uint(12818L));

  public static final NodeId SessionDiagnosticsArrayType_SessionDiagnostics_ClientDescription =
      new NodeId(UShort.MIN, uint(12819L));

  public static final NodeId SessionDiagnosticsArrayType_SessionDiagnostics_ServerUri =
      new NodeId(UShort.MIN, uint(12820L));

  public static final NodeId SessionDiagnosticsArrayType_SessionDiagnostics_EndpointUrl =
      new NodeId(UShort.MIN, uint(12821L));

  public static final NodeId SessionDiagnosticsArrayType_SessionDiagnostics_LocaleIds =
      new NodeId(UShort.MIN, uint(12822L));

  public static final NodeId SessionDiagnosticsArrayType_SessionDiagnostics_ActualSessionTimeout =
      new NodeId(UShort.MIN, uint(12823L));

  public static final NodeId SessionDiagnosticsArrayType_SessionDiagnostics_MaxResponseMessageSize =
      new NodeId(UShort.MIN, uint(12824L));

  public static final NodeId SessionDiagnosticsArrayType_SessionDiagnostics_ClientConnectionTime =
      new NodeId(UShort.MIN, uint(12825L));

  public static final NodeId SessionDiagnosticsArrayType_SessionDiagnostics_ClientLastContactTime =
      new NodeId(UShort.MIN, uint(12826L));

  public static final NodeId
      SessionDiagnosticsArrayType_SessionDiagnostics_CurrentSubscriptionsCount =
          new NodeId(UShort.MIN, uint(12827L));

  public static final NodeId
      SessionDiagnosticsArrayType_SessionDiagnostics_CurrentMonitoredItemsCount =
          new NodeId(UShort.MIN, uint(12828L));

  public static final NodeId
      SessionDiagnosticsArrayType_SessionDiagnostics_CurrentPublishRequestsInQueue =
          new NodeId(UShort.MIN, uint(12829L));

  public static final NodeId SessionDiagnosticsArrayType_SessionDiagnostics_TotalRequestCount =
      new NodeId(UShort.MIN, uint(12830L));

  public static final NodeId
      SessionDiagnosticsArrayType_SessionDiagnostics_UnauthorizedRequestCount =
          new NodeId(UShort.MIN, uint(12831L));

  public static final NodeId SessionDiagnosticsArrayType_SessionDiagnostics_ReadCount =
      new NodeId(UShort.MIN, uint(12832L));

  public static final NodeId SessionDiagnosticsArrayType_SessionDiagnostics_HistoryReadCount =
      new NodeId(UShort.MIN, uint(12833L));

  public static final NodeId SessionDiagnosticsArrayType_SessionDiagnostics_WriteCount =
      new NodeId(UShort.MIN, uint(12834L));

  public static final NodeId SessionDiagnosticsArrayType_SessionDiagnostics_HistoryUpdateCount =
      new NodeId(UShort.MIN, uint(12835L));

  public static final NodeId SessionDiagnosticsArrayType_SessionDiagnostics_CallCount =
      new NodeId(UShort.MIN, uint(12836L));

  public static final NodeId
      SessionDiagnosticsArrayType_SessionDiagnostics_CreateMonitoredItemsCount =
          new NodeId(UShort.MIN, uint(12837L));

  public static final NodeId
      SessionDiagnosticsArrayType_SessionDiagnostics_ModifyMonitoredItemsCount =
          new NodeId(UShort.MIN, uint(12838L));

  public static final NodeId SessionDiagnosticsArrayType_SessionDiagnostics_SetMonitoringModeCount =
      new NodeId(UShort.MIN, uint(12839L));

  public static final NodeId SessionDiagnosticsArrayType_SessionDiagnostics_SetTriggeringCount =
      new NodeId(UShort.MIN, uint(12840L));

  public static final NodeId
      SessionDiagnosticsArrayType_SessionDiagnostics_DeleteMonitoredItemsCount =
          new NodeId(UShort.MIN, uint(12841L));

  public static final NodeId
      SessionDiagnosticsArrayType_SessionDiagnostics_CreateSubscriptionCount =
          new NodeId(UShort.MIN, uint(12842L));

  public static final NodeId
      SessionDiagnosticsArrayType_SessionDiagnostics_ModifySubscriptionCount =
          new NodeId(UShort.MIN, uint(12843L));

  public static final NodeId SessionDiagnosticsArrayType_SessionDiagnostics_SetPublishingModeCount =
      new NodeId(UShort.MIN, uint(12844L));

  public static final NodeId SessionDiagnosticsArrayType_SessionDiagnostics_PublishCount =
      new NodeId(UShort.MIN, uint(12845L));

  public static final NodeId SessionDiagnosticsArrayType_SessionDiagnostics_RepublishCount =
      new NodeId(UShort.MIN, uint(12846L));

  public static final NodeId
      SessionDiagnosticsArrayType_SessionDiagnostics_TransferSubscriptionsCount =
          new NodeId(UShort.MIN, uint(12847L));

  public static final NodeId
      SessionDiagnosticsArrayType_SessionDiagnostics_DeleteSubscriptionsCount =
          new NodeId(UShort.MIN, uint(12848L));

  public static final NodeId SessionDiagnosticsArrayType_SessionDiagnostics_AddNodesCount =
      new NodeId(UShort.MIN, uint(12849L));

  public static final NodeId SessionDiagnosticsArrayType_SessionDiagnostics_AddReferencesCount =
      new NodeId(UShort.MIN, uint(12850L));

  public static final NodeId SessionDiagnosticsArrayType_SessionDiagnostics_DeleteNodesCount =
      new NodeId(UShort.MIN, uint(12851L));

  public static final NodeId SessionDiagnosticsArrayType_SessionDiagnostics_DeleteReferencesCount =
      new NodeId(UShort.MIN, uint(12852L));

  public static final NodeId SessionDiagnosticsArrayType_SessionDiagnostics_BrowseCount =
      new NodeId(UShort.MIN, uint(12853L));

  public static final NodeId SessionDiagnosticsArrayType_SessionDiagnostics_BrowseNextCount =
      new NodeId(UShort.MIN, uint(12854L));

  public static final NodeId
      SessionDiagnosticsArrayType_SessionDiagnostics_TranslateBrowsePathsToNodeIdsCount =
          new NodeId(UShort.MIN, uint(12855L));

  public static final NodeId SessionDiagnosticsArrayType_SessionDiagnostics_QueryFirstCount =
      new NodeId(UShort.MIN, uint(12856L));

  public static final NodeId SessionDiagnosticsArrayType_SessionDiagnostics_QueryNextCount =
      new NodeId(UShort.MIN, uint(12857L));

  public static final NodeId SessionDiagnosticsArrayType_SessionDiagnostics_RegisterNodesCount =
      new NodeId(UShort.MIN, uint(12858L));

  public static final NodeId SessionDiagnosticsArrayType_SessionDiagnostics_UnregisterNodesCount =
      new NodeId(UShort.MIN, uint(12859L));

  public static final NodeId SessionSecurityDiagnosticsArrayType_SessionSecurityDiagnostics =
      new NodeId(UShort.MIN, uint(12860L));

  public static final NodeId
      SessionSecurityDiagnosticsArrayType_SessionSecurityDiagnostics_SessionId =
          new NodeId(UShort.MIN, uint(12861L));

  public static final NodeId
      SessionSecurityDiagnosticsArrayType_SessionSecurityDiagnostics_ClientUserIdOfSession =
          new NodeId(UShort.MIN, uint(12862L));

  public static final NodeId
      SessionSecurityDiagnosticsArrayType_SessionSecurityDiagnostics_ClientUserIdHistory =
          new NodeId(UShort.MIN, uint(12863L));

  public static final NodeId
      SessionSecurityDiagnosticsArrayType_SessionSecurityDiagnostics_AuthenticationMechanism =
          new NodeId(UShort.MIN, uint(12864L));

  public static final NodeId
      SessionSecurityDiagnosticsArrayType_SessionSecurityDiagnostics_Encoding =
          new NodeId(UShort.MIN, uint(12865L));

  public static final NodeId
      SessionSecurityDiagnosticsArrayType_SessionSecurityDiagnostics_TransportProtocol =
          new NodeId(UShort.MIN, uint(12866L));

  public static final NodeId
      SessionSecurityDiagnosticsArrayType_SessionSecurityDiagnostics_SecurityMode =
          new NodeId(UShort.MIN, uint(12867L));

  public static final NodeId
      SessionSecurityDiagnosticsArrayType_SessionSecurityDiagnostics_SecurityPolicyUri =
          new NodeId(UShort.MIN, uint(12868L));

  public static final NodeId
      SessionSecurityDiagnosticsArrayType_SessionSecurityDiagnostics_ClientCertificate =
          new NodeId(UShort.MIN, uint(12869L));

  public static final NodeId ServerType_ResendData = new NodeId(UShort.MIN, uint(12871L));

  public static final NodeId ServerType_ResendData_InputArguments =
      new NodeId(UShort.MIN, uint(12872L));

  public static final NodeId Server_ResendData = new NodeId(UShort.MIN, uint(12873L));

  public static final NodeId Server_ResendData_InputArguments =
      new NodeId(UShort.MIN, uint(12874L));

  public static final NodeId ResendDataMethodType = new NodeId(UShort.MIN, uint(12875L));

  public static final NodeId ResendDataMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(12876L));

  public static final NodeId NormalizedString = new NodeId(UShort.MIN, uint(12877L));

  public static final NodeId DecimalString = new NodeId(UShort.MIN, uint(12878L));

  public static final NodeId DurationString = new NodeId(UShort.MIN, uint(12879L));

  public static final NodeId TimeString = new NodeId(UShort.MIN, uint(12880L));

  public static final NodeId DateString = new NodeId(UShort.MIN, uint(12881L));

  public static final NodeId ServerType_EstimatedReturnTime = new NodeId(UShort.MIN, uint(12882L));

  public static final NodeId ServerType_RequestServerStateChange =
      new NodeId(UShort.MIN, uint(12883L));

  public static final NodeId ServerType_RequestServerStateChange_InputArguments =
      new NodeId(UShort.MIN, uint(12884L));

  public static final NodeId Server_EstimatedReturnTime = new NodeId(UShort.MIN, uint(12885L));

  public static final NodeId Server_RequestServerStateChange = new NodeId(UShort.MIN, uint(12886L));

  public static final NodeId Server_RequestServerStateChange_InputArguments =
      new NodeId(UShort.MIN, uint(12887L));

  public static final NodeId RequestServerStateChangeMethodType =
      new NodeId(UShort.MIN, uint(12888L));

  public static final NodeId RequestServerStateChangeMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(12889L));

  public static final NodeId DiscoveryConfiguration = new NodeId(UShort.MIN, uint(12890L));

  public static final NodeId MdnsDiscoveryConfiguration = new NodeId(UShort.MIN, uint(12891L));

  public static final NodeId DiscoveryConfiguration_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(12892L));

  public static final NodeId MdnsDiscoveryConfiguration_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(12893L));

  public static final NodeId OpcUa_XmlSchema_DiscoveryConfiguration =
      new NodeId(UShort.MIN, uint(12894L));

  public static final NodeId OpcUa_XmlSchema_DiscoveryConfiguration_DataTypeVersion =
      new NodeId(UShort.MIN, uint(12895L));

  public static final NodeId OpcUa_XmlSchema_DiscoveryConfiguration_DictionaryFragment =
      new NodeId(UShort.MIN, uint(12896L));

  public static final NodeId OpcUa_XmlSchema_MdnsDiscoveryConfiguration =
      new NodeId(UShort.MIN, uint(12897L));

  public static final NodeId OpcUa_XmlSchema_MdnsDiscoveryConfiguration_DataTypeVersion =
      new NodeId(UShort.MIN, uint(12898L));

  public static final NodeId OpcUa_XmlSchema_MdnsDiscoveryConfiguration_DictionaryFragment =
      new NodeId(UShort.MIN, uint(12899L));

  public static final NodeId DiscoveryConfiguration_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(12900L));

  public static final NodeId MdnsDiscoveryConfiguration_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(12901L));

  public static final NodeId OpcUa_BinarySchema_DiscoveryConfiguration =
      new NodeId(UShort.MIN, uint(12902L));

  public static final NodeId OpcUa_BinarySchema_DiscoveryConfiguration_DataTypeVersion =
      new NodeId(UShort.MIN, uint(12903L));

  public static final NodeId OpcUa_BinarySchema_DiscoveryConfiguration_DictionaryFragment =
      new NodeId(UShort.MIN, uint(12904L));

  public static final NodeId OpcUa_BinarySchema_MdnsDiscoveryConfiguration =
      new NodeId(UShort.MIN, uint(12905L));

  public static final NodeId OpcUa_BinarySchema_MdnsDiscoveryConfiguration_DataTypeVersion =
      new NodeId(UShort.MIN, uint(12906L));

  public static final NodeId OpcUa_BinarySchema_MdnsDiscoveryConfiguration_DictionaryFragment =
      new NodeId(UShort.MIN, uint(12907L));

  public static final NodeId MaxByteStringLength = new NodeId(UShort.MIN, uint(12908L));

  public static final NodeId ServerType_ServerCapabilities_MaxByteStringLength =
      new NodeId(UShort.MIN, uint(12909L));

  public static final NodeId ServerCapabilitiesType_MaxByteStringLength =
      new NodeId(UShort.MIN, uint(12910L));

  public static final NodeId Server_ServerCapabilities_MaxByteStringLength =
      new NodeId(UShort.MIN, uint(12911L));

  public static final NodeId ConditionType_ConditionRefresh2 = new NodeId(UShort.MIN, uint(12912L));

  public static final NodeId ConditionType_ConditionRefresh2_InputArguments =
      new NodeId(UShort.MIN, uint(12913L));

  public static final NodeId ConditionRefresh2MethodType = new NodeId(UShort.MIN, uint(12914L));

  public static final NodeId ConditionRefresh2MethodType_InputArguments =
      new NodeId(UShort.MIN, uint(12915L));

  public static final NodeId CertificateExpirationAlarmType = new NodeId(UShort.MIN, uint(13225L));

  public static final NodeId CertificateExpirationAlarmType_ExpirationDate =
      new NodeId(UShort.MIN, uint(13325L));

  public static final NodeId CertificateExpirationAlarmType_CertificateType =
      new NodeId(UShort.MIN, uint(13326L));

  public static final NodeId CertificateExpirationAlarmType_Certificate =
      new NodeId(UShort.MIN, uint(13327L));

  public static final NodeId FileType_MimeType = new NodeId(UShort.MIN, uint(13341L));

  public static final NodeId CreateDirectoryMethodType = new NodeId(UShort.MIN, uint(13342L));

  public static final NodeId CreateDirectoryMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(13343L));

  public static final NodeId CreateDirectoryMethodType_OutputArguments =
      new NodeId(UShort.MIN, uint(13344L));

  public static final NodeId CreateFileMethodType = new NodeId(UShort.MIN, uint(13345L));

  public static final NodeId CreateFileMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(13346L));

  public static final NodeId CreateFileMethodType_OutputArguments =
      new NodeId(UShort.MIN, uint(13347L));

  public static final NodeId DeleteFileMethodType = new NodeId(UShort.MIN, uint(13348L));

  public static final NodeId DeleteFileMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(13349L));

  public static final NodeId MoveOrCopyMethodType = new NodeId(UShort.MIN, uint(13350L));

  public static final NodeId MoveOrCopyMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(13351L));

  public static final NodeId MoveOrCopyMethodType_OutputArguments =
      new NodeId(UShort.MIN, uint(13352L));

  public static final NodeId FileDirectoryType = new NodeId(UShort.MIN, uint(13353L));

  public static final NodeId FileDirectoryType_FileDirectoryName_Placeholder =
      new NodeId(UShort.MIN, uint(13354L));

  public static final NodeId FileDirectoryType_FileDirectoryName_Placeholder_CreateDirectory =
      new NodeId(UShort.MIN, uint(13355L));

  public static final NodeId
      FileDirectoryType_FileDirectoryName_Placeholder_CreateDirectory_InputArguments =
          new NodeId(UShort.MIN, uint(13356L));

  public static final NodeId
      FileDirectoryType_FileDirectoryName_Placeholder_CreateDirectory_OutputArguments =
          new NodeId(UShort.MIN, uint(13357L));

  public static final NodeId FileDirectoryType_FileDirectoryName_Placeholder_CreateFile =
      new NodeId(UShort.MIN, uint(13358L));

  public static final NodeId
      FileDirectoryType_FileDirectoryName_Placeholder_CreateFile_InputArguments =
          new NodeId(UShort.MIN, uint(13359L));

  public static final NodeId
      FileDirectoryType_FileDirectoryName_Placeholder_CreateFile_OutputArguments =
          new NodeId(UShort.MIN, uint(13360L));

  public static final NodeId FileDirectoryType_FileDirectoryName_Placeholder_MoveOrCopy =
      new NodeId(UShort.MIN, uint(13363L));

  public static final NodeId
      FileDirectoryType_FileDirectoryName_Placeholder_MoveOrCopy_InputArguments =
          new NodeId(UShort.MIN, uint(13364L));

  public static final NodeId
      FileDirectoryType_FileDirectoryName_Placeholder_MoveOrCopy_OutputArguments =
          new NodeId(UShort.MIN, uint(13365L));

  public static final NodeId FileDirectoryType_FileName_Placeholder =
      new NodeId(UShort.MIN, uint(13366L));

  public static final NodeId FileDirectoryType_FileName_Placeholder_Size =
      new NodeId(UShort.MIN, uint(13367L));

  public static final NodeId FileDirectoryType_FileName_Placeholder_Writable =
      new NodeId(UShort.MIN, uint(13368L));

  public static final NodeId FileDirectoryType_FileName_Placeholder_UserWritable =
      new NodeId(UShort.MIN, uint(13369L));

  public static final NodeId FileDirectoryType_FileName_Placeholder_OpenCount =
      new NodeId(UShort.MIN, uint(13370L));

  public static final NodeId FileDirectoryType_FileName_Placeholder_MimeType =
      new NodeId(UShort.MIN, uint(13371L));

  public static final NodeId FileDirectoryType_FileName_Placeholder_Open =
      new NodeId(UShort.MIN, uint(13372L));

  public static final NodeId FileDirectoryType_FileName_Placeholder_Open_InputArguments =
      new NodeId(UShort.MIN, uint(13373L));

  public static final NodeId FileDirectoryType_FileName_Placeholder_Open_OutputArguments =
      new NodeId(UShort.MIN, uint(13374L));

  public static final NodeId FileDirectoryType_FileName_Placeholder_Close =
      new NodeId(UShort.MIN, uint(13375L));

  public static final NodeId FileDirectoryType_FileName_Placeholder_Close_InputArguments =
      new NodeId(UShort.MIN, uint(13376L));

  public static final NodeId FileDirectoryType_FileName_Placeholder_Read =
      new NodeId(UShort.MIN, uint(13377L));

  public static final NodeId FileDirectoryType_FileName_Placeholder_Read_InputArguments =
      new NodeId(UShort.MIN, uint(13378L));

  public static final NodeId FileDirectoryType_FileName_Placeholder_Read_OutputArguments =
      new NodeId(UShort.MIN, uint(13379L));

  public static final NodeId FileDirectoryType_FileName_Placeholder_Write =
      new NodeId(UShort.MIN, uint(13380L));

  public static final NodeId FileDirectoryType_FileName_Placeholder_Write_InputArguments =
      new NodeId(UShort.MIN, uint(13381L));

  public static final NodeId FileDirectoryType_FileName_Placeholder_GetPosition =
      new NodeId(UShort.MIN, uint(13382L));

  public static final NodeId FileDirectoryType_FileName_Placeholder_GetPosition_InputArguments =
      new NodeId(UShort.MIN, uint(13383L));

  public static final NodeId FileDirectoryType_FileName_Placeholder_GetPosition_OutputArguments =
      new NodeId(UShort.MIN, uint(13384L));

  public static final NodeId FileDirectoryType_FileName_Placeholder_SetPosition =
      new NodeId(UShort.MIN, uint(13385L));

  public static final NodeId FileDirectoryType_FileName_Placeholder_SetPosition_InputArguments =
      new NodeId(UShort.MIN, uint(13386L));

  public static final NodeId FileDirectoryType_CreateDirectory =
      new NodeId(UShort.MIN, uint(13387L));

  public static final NodeId FileDirectoryType_CreateDirectory_InputArguments =
      new NodeId(UShort.MIN, uint(13388L));

  public static final NodeId FileDirectoryType_CreateDirectory_OutputArguments =
      new NodeId(UShort.MIN, uint(13389L));

  public static final NodeId FileDirectoryType_CreateFile = new NodeId(UShort.MIN, uint(13390L));

  public static final NodeId FileDirectoryType_CreateFile_InputArguments =
      new NodeId(UShort.MIN, uint(13391L));

  public static final NodeId FileDirectoryType_CreateFile_OutputArguments =
      new NodeId(UShort.MIN, uint(13392L));

  public static final NodeId FileDirectoryType_DeleteFileSystemObject =
      new NodeId(UShort.MIN, uint(13393L));

  public static final NodeId FileDirectoryType_DeleteFileSystemObject_InputArguments =
      new NodeId(UShort.MIN, uint(13394L));

  public static final NodeId FileDirectoryType_MoveOrCopy = new NodeId(UShort.MIN, uint(13395L));

  public static final NodeId FileDirectoryType_MoveOrCopy_InputArguments =
      new NodeId(UShort.MIN, uint(13396L));

  public static final NodeId FileDirectoryType_MoveOrCopy_OutputArguments =
      new NodeId(UShort.MIN, uint(13397L));

  public static final NodeId NamespaceMetadataType_NamespaceFile_MimeType =
      new NodeId(UShort.MIN, uint(13399L));

  public static final NodeId NamespacesType_NamespaceIdentifier_Placeholder_NamespaceFile_MimeType =
      new NodeId(UShort.MIN, uint(13400L));

  public static final NodeId CertificateGroupType_TrustList = new NodeId(UShort.MIN, uint(13599L));

  public static final NodeId CertificateGroupType_TrustList_Size =
      new NodeId(UShort.MIN, uint(13600L));

  public static final NodeId CertificateGroupType_TrustList_Writable =
      new NodeId(UShort.MIN, uint(13601L));

  public static final NodeId CertificateGroupType_TrustList_UserWritable =
      new NodeId(UShort.MIN, uint(13602L));

  public static final NodeId CertificateGroupType_TrustList_OpenCount =
      new NodeId(UShort.MIN, uint(13603L));

  public static final NodeId CertificateGroupType_TrustList_MimeType =
      new NodeId(UShort.MIN, uint(13604L));

  public static final NodeId CertificateGroupType_TrustList_Open =
      new NodeId(UShort.MIN, uint(13605L));

  public static final NodeId CertificateGroupType_TrustList_Open_InputArguments =
      new NodeId(UShort.MIN, uint(13606L));

  public static final NodeId CertificateGroupType_TrustList_Open_OutputArguments =
      new NodeId(UShort.MIN, uint(13607L));

  public static final NodeId CertificateGroupType_TrustList_Close =
      new NodeId(UShort.MIN, uint(13608L));

  public static final NodeId CertificateGroupType_TrustList_Close_InputArguments =
      new NodeId(UShort.MIN, uint(13609L));

  public static final NodeId CertificateGroupType_TrustList_Read =
      new NodeId(UShort.MIN, uint(13610L));

  public static final NodeId CertificateGroupType_TrustList_Read_InputArguments =
      new NodeId(UShort.MIN, uint(13611L));

  public static final NodeId CertificateGroupType_TrustList_Read_OutputArguments =
      new NodeId(UShort.MIN, uint(13612L));

  public static final NodeId CertificateGroupType_TrustList_Write =
      new NodeId(UShort.MIN, uint(13613L));

  public static final NodeId CertificateGroupType_TrustList_Write_InputArguments =
      new NodeId(UShort.MIN, uint(13614L));

  public static final NodeId CertificateGroupType_TrustList_GetPosition =
      new NodeId(UShort.MIN, uint(13615L));

  public static final NodeId CertificateGroupType_TrustList_GetPosition_InputArguments =
      new NodeId(UShort.MIN, uint(13616L));

  public static final NodeId CertificateGroupType_TrustList_GetPosition_OutputArguments =
      new NodeId(UShort.MIN, uint(13617L));

  public static final NodeId CertificateGroupType_TrustList_SetPosition =
      new NodeId(UShort.MIN, uint(13618L));

  public static final NodeId CertificateGroupType_TrustList_SetPosition_InputArguments =
      new NodeId(UShort.MIN, uint(13619L));

  public static final NodeId CertificateGroupType_TrustList_LastUpdateTime =
      new NodeId(UShort.MIN, uint(13620L));

  public static final NodeId CertificateGroupType_TrustList_OpenWithMasks =
      new NodeId(UShort.MIN, uint(13621L));

  public static final NodeId CertificateGroupType_TrustList_OpenWithMasks_InputArguments =
      new NodeId(UShort.MIN, uint(13622L));

  public static final NodeId CertificateGroupType_TrustList_OpenWithMasks_OutputArguments =
      new NodeId(UShort.MIN, uint(13623L));

  public static final NodeId CertificateGroupType_TrustList_CloseAndUpdate =
      new NodeId(UShort.MIN, uint(13624L));

  public static final NodeId CertificateGroupType_TrustList_CloseAndUpdate_InputArguments =
      new NodeId(UShort.MIN, uint(13625L));

  public static final NodeId CertificateGroupType_TrustList_CloseAndUpdate_OutputArguments =
      new NodeId(UShort.MIN, uint(13626L));

  public static final NodeId CertificateGroupType_TrustList_AddCertificate =
      new NodeId(UShort.MIN, uint(13627L));

  public static final NodeId CertificateGroupType_TrustList_AddCertificate_InputArguments =
      new NodeId(UShort.MIN, uint(13628L));

  public static final NodeId CertificateGroupType_TrustList_RemoveCertificate =
      new NodeId(UShort.MIN, uint(13629L));

  public static final NodeId CertificateGroupType_TrustList_RemoveCertificate_InputArguments =
      new NodeId(UShort.MIN, uint(13630L));

  public static final NodeId CertificateGroupType_CertificateTypes =
      new NodeId(UShort.MIN, uint(13631L));

  public static final NodeId CertificateUpdatedAuditEventType_CertificateGroup =
      new NodeId(UShort.MIN, uint(13735L));

  public static final NodeId CertificateUpdatedAuditEventType_CertificateType =
      new NodeId(UShort.MIN, uint(13736L));

  public static final NodeId ServerConfiguration_UpdateCertificate =
      new NodeId(UShort.MIN, uint(13737L));

  public static final NodeId ServerConfiguration_UpdateCertificate_InputArguments =
      new NodeId(UShort.MIN, uint(13738L));

  public static final NodeId ServerConfiguration_UpdateCertificate_OutputArguments =
      new NodeId(UShort.MIN, uint(13739L));

  public static final NodeId CertificateGroupFolderType = new NodeId(UShort.MIN, uint(13813L));

  public static final NodeId CertificateGroupFolderType_DefaultApplicationGroup =
      new NodeId(UShort.MIN, uint(13814L));

  public static final NodeId CertificateGroupFolderType_DefaultApplicationGroup_TrustList =
      new NodeId(UShort.MIN, uint(13815L));

  public static final NodeId CertificateGroupFolderType_DefaultApplicationGroup_TrustList_Size =
      new NodeId(UShort.MIN, uint(13816L));

  public static final NodeId CertificateGroupFolderType_DefaultApplicationGroup_TrustList_Writable =
      new NodeId(UShort.MIN, uint(13817L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustList_UserWritable =
          new NodeId(UShort.MIN, uint(13818L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustList_OpenCount =
          new NodeId(UShort.MIN, uint(13819L));

  public static final NodeId CertificateGroupFolderType_DefaultApplicationGroup_TrustList_MimeType =
      new NodeId(UShort.MIN, uint(13820L));

  public static final NodeId CertificateGroupFolderType_DefaultApplicationGroup_TrustList_Open =
      new NodeId(UShort.MIN, uint(13821L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustList_Open_InputArguments =
          new NodeId(UShort.MIN, uint(13822L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustList_Open_OutputArguments =
          new NodeId(UShort.MIN, uint(13823L));

  public static final NodeId CertificateGroupFolderType_DefaultApplicationGroup_TrustList_Close =
      new NodeId(UShort.MIN, uint(13824L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustList_Close_InputArguments =
          new NodeId(UShort.MIN, uint(13825L));

  public static final NodeId CertificateGroupFolderType_DefaultApplicationGroup_TrustList_Read =
      new NodeId(UShort.MIN, uint(13826L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustList_Read_InputArguments =
          new NodeId(UShort.MIN, uint(13827L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustList_Read_OutputArguments =
          new NodeId(UShort.MIN, uint(13828L));

  public static final NodeId CertificateGroupFolderType_DefaultApplicationGroup_TrustList_Write =
      new NodeId(UShort.MIN, uint(13829L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustList_Write_InputArguments =
          new NodeId(UShort.MIN, uint(13830L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustList_GetPosition =
          new NodeId(UShort.MIN, uint(13831L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustList_GetPosition_InputArguments =
          new NodeId(UShort.MIN, uint(13832L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustList_GetPosition_OutputArguments =
          new NodeId(UShort.MIN, uint(13833L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustList_SetPosition =
          new NodeId(UShort.MIN, uint(13834L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustList_SetPosition_InputArguments =
          new NodeId(UShort.MIN, uint(13835L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustList_LastUpdateTime =
          new NodeId(UShort.MIN, uint(13836L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustList_OpenWithMasks =
          new NodeId(UShort.MIN, uint(13837L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustList_OpenWithMasks_InputArguments =
          new NodeId(UShort.MIN, uint(13838L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustList_OpenWithMasks_OutputArguments =
          new NodeId(UShort.MIN, uint(13839L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustList_CloseAndUpdate =
          new NodeId(UShort.MIN, uint(13840L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustList_CloseAndUpdate_InputArguments =
          new NodeId(UShort.MIN, uint(13841L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustList_CloseAndUpdate_OutputArguments =
          new NodeId(UShort.MIN, uint(13842L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustList_AddCertificate =
          new NodeId(UShort.MIN, uint(13843L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustList_AddCertificate_InputArguments =
          new NodeId(UShort.MIN, uint(13844L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustList_RemoveCertificate =
          new NodeId(UShort.MIN, uint(13845L));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustList_RemoveCertificate_InputArguments =
          new NodeId(UShort.MIN, uint(13846L));

  public static final NodeId CertificateGroupFolderType_DefaultApplicationGroup_CertificateTypes =
      new NodeId(UShort.MIN, uint(13847L));

  public static final NodeId CertificateGroupFolderType_DefaultHttpsGroup =
      new NodeId(UShort.MIN, uint(13848L));

  public static final NodeId CertificateGroupFolderType_DefaultHttpsGroup_TrustList =
      new NodeId(UShort.MIN, uint(13849L));

  public static final NodeId CertificateGroupFolderType_DefaultHttpsGroup_TrustList_Size =
      new NodeId(UShort.MIN, uint(13850L));

  public static final NodeId CertificateGroupFolderType_DefaultHttpsGroup_TrustList_Writable =
      new NodeId(UShort.MIN, uint(13851L));

  public static final NodeId CertificateGroupFolderType_DefaultHttpsGroup_TrustList_UserWritable =
      new NodeId(UShort.MIN, uint(13852L));

  public static final NodeId CertificateGroupFolderType_DefaultHttpsGroup_TrustList_OpenCount =
      new NodeId(UShort.MIN, uint(13853L));

  public static final NodeId CertificateGroupFolderType_DefaultHttpsGroup_TrustList_MimeType =
      new NodeId(UShort.MIN, uint(13854L));

  public static final NodeId CertificateGroupFolderType_DefaultHttpsGroup_TrustList_Open =
      new NodeId(UShort.MIN, uint(13855L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustList_Open_InputArguments =
          new NodeId(UShort.MIN, uint(13856L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustList_Open_OutputArguments =
          new NodeId(UShort.MIN, uint(13857L));

  public static final NodeId CertificateGroupFolderType_DefaultHttpsGroup_TrustList_Close =
      new NodeId(UShort.MIN, uint(13858L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustList_Close_InputArguments =
          new NodeId(UShort.MIN, uint(13859L));

  public static final NodeId CertificateGroupFolderType_DefaultHttpsGroup_TrustList_Read =
      new NodeId(UShort.MIN, uint(13860L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustList_Read_InputArguments =
          new NodeId(UShort.MIN, uint(13861L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustList_Read_OutputArguments =
          new NodeId(UShort.MIN, uint(13862L));

  public static final NodeId CertificateGroupFolderType_DefaultHttpsGroup_TrustList_Write =
      new NodeId(UShort.MIN, uint(13863L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustList_Write_InputArguments =
          new NodeId(UShort.MIN, uint(13864L));

  public static final NodeId CertificateGroupFolderType_DefaultHttpsGroup_TrustList_GetPosition =
      new NodeId(UShort.MIN, uint(13865L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustList_GetPosition_InputArguments =
          new NodeId(UShort.MIN, uint(13866L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustList_GetPosition_OutputArguments =
          new NodeId(UShort.MIN, uint(13867L));

  public static final NodeId CertificateGroupFolderType_DefaultHttpsGroup_TrustList_SetPosition =
      new NodeId(UShort.MIN, uint(13868L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustList_SetPosition_InputArguments =
          new NodeId(UShort.MIN, uint(13869L));

  public static final NodeId CertificateGroupFolderType_DefaultHttpsGroup_TrustList_LastUpdateTime =
      new NodeId(UShort.MIN, uint(13870L));

  public static final NodeId CertificateGroupFolderType_DefaultHttpsGroup_TrustList_OpenWithMasks =
      new NodeId(UShort.MIN, uint(13871L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustList_OpenWithMasks_InputArguments =
          new NodeId(UShort.MIN, uint(13872L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustList_OpenWithMasks_OutputArguments =
          new NodeId(UShort.MIN, uint(13873L));

  public static final NodeId CertificateGroupFolderType_DefaultHttpsGroup_TrustList_CloseAndUpdate =
      new NodeId(UShort.MIN, uint(13874L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustList_CloseAndUpdate_InputArguments =
          new NodeId(UShort.MIN, uint(13875L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustList_CloseAndUpdate_OutputArguments =
          new NodeId(UShort.MIN, uint(13876L));

  public static final NodeId CertificateGroupFolderType_DefaultHttpsGroup_TrustList_AddCertificate =
      new NodeId(UShort.MIN, uint(13877L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustList_AddCertificate_InputArguments =
          new NodeId(UShort.MIN, uint(13878L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustList_RemoveCertificate =
          new NodeId(UShort.MIN, uint(13879L));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustList_RemoveCertificate_InputArguments =
          new NodeId(UShort.MIN, uint(13880L));

  public static final NodeId CertificateGroupFolderType_DefaultHttpsGroup_CertificateTypes =
      new NodeId(UShort.MIN, uint(13881L));

  public static final NodeId CertificateGroupFolderType_DefaultUserTokenGroup =
      new NodeId(UShort.MIN, uint(13882L));

  public static final NodeId CertificateGroupFolderType_DefaultUserTokenGroup_TrustList =
      new NodeId(UShort.MIN, uint(13883L));

  public static final NodeId CertificateGroupFolderType_DefaultUserTokenGroup_TrustList_Size =
      new NodeId(UShort.MIN, uint(13884L));

  public static final NodeId CertificateGroupFolderType_DefaultUserTokenGroup_TrustList_Writable =
      new NodeId(UShort.MIN, uint(13885L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustList_UserWritable =
          new NodeId(UShort.MIN, uint(13886L));

  public static final NodeId CertificateGroupFolderType_DefaultUserTokenGroup_TrustList_OpenCount =
      new NodeId(UShort.MIN, uint(13887L));

  public static final NodeId CertificateGroupFolderType_DefaultUserTokenGroup_TrustList_MimeType =
      new NodeId(UShort.MIN, uint(13888L));

  public static final NodeId CertificateGroupFolderType_DefaultUserTokenGroup_TrustList_Open =
      new NodeId(UShort.MIN, uint(13889L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustList_Open_InputArguments =
          new NodeId(UShort.MIN, uint(13890L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustList_Open_OutputArguments =
          new NodeId(UShort.MIN, uint(13891L));

  public static final NodeId CertificateGroupFolderType_DefaultUserTokenGroup_TrustList_Close =
      new NodeId(UShort.MIN, uint(13892L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustList_Close_InputArguments =
          new NodeId(UShort.MIN, uint(13893L));

  public static final NodeId CertificateGroupFolderType_DefaultUserTokenGroup_TrustList_Read =
      new NodeId(UShort.MIN, uint(13894L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustList_Read_InputArguments =
          new NodeId(UShort.MIN, uint(13895L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustList_Read_OutputArguments =
          new NodeId(UShort.MIN, uint(13896L));

  public static final NodeId CertificateGroupFolderType_DefaultUserTokenGroup_TrustList_Write =
      new NodeId(UShort.MIN, uint(13897L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustList_Write_InputArguments =
          new NodeId(UShort.MIN, uint(13898L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustList_GetPosition =
          new NodeId(UShort.MIN, uint(13899L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustList_GetPosition_InputArguments =
          new NodeId(UShort.MIN, uint(13900L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustList_GetPosition_OutputArguments =
          new NodeId(UShort.MIN, uint(13901L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustList_SetPosition =
          new NodeId(UShort.MIN, uint(13902L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustList_SetPosition_InputArguments =
          new NodeId(UShort.MIN, uint(13903L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustList_LastUpdateTime =
          new NodeId(UShort.MIN, uint(13904L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustList_OpenWithMasks =
          new NodeId(UShort.MIN, uint(13905L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustList_OpenWithMasks_InputArguments =
          new NodeId(UShort.MIN, uint(13906L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustList_OpenWithMasks_OutputArguments =
          new NodeId(UShort.MIN, uint(13907L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustList_CloseAndUpdate =
          new NodeId(UShort.MIN, uint(13908L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustList_CloseAndUpdate_InputArguments =
          new NodeId(UShort.MIN, uint(13909L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustList_CloseAndUpdate_OutputArguments =
          new NodeId(UShort.MIN, uint(13910L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustList_AddCertificate =
          new NodeId(UShort.MIN, uint(13911L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustList_AddCertificate_InputArguments =
          new NodeId(UShort.MIN, uint(13912L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustList_RemoveCertificate =
          new NodeId(UShort.MIN, uint(13913L));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustList_RemoveCertificate_InputArguments =
          new NodeId(UShort.MIN, uint(13914L));

  public static final NodeId CertificateGroupFolderType_DefaultUserTokenGroup_CertificateTypes =
      new NodeId(UShort.MIN, uint(13915L));

  public static final NodeId CertificateGroupFolderType_AdditionalGroup_Placeholder =
      new NodeId(UShort.MIN, uint(13916L));

  public static final NodeId CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustList =
      new NodeId(UShort.MIN, uint(13917L));

  public static final NodeId CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustList_Size =
      new NodeId(UShort.MIN, uint(13918L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustList_Writable =
          new NodeId(UShort.MIN, uint(13919L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustList_UserWritable =
          new NodeId(UShort.MIN, uint(13920L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustList_OpenCount =
          new NodeId(UShort.MIN, uint(13921L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustList_MimeType =
          new NodeId(UShort.MIN, uint(13922L));

  public static final NodeId CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustList_Open =
      new NodeId(UShort.MIN, uint(13923L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustList_Open_InputArguments =
          new NodeId(UShort.MIN, uint(13924L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustList_Open_OutputArguments =
          new NodeId(UShort.MIN, uint(13925L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustList_Close =
          new NodeId(UShort.MIN, uint(13926L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustList_Close_InputArguments =
          new NodeId(UShort.MIN, uint(13927L));

  public static final NodeId CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustList_Read =
      new NodeId(UShort.MIN, uint(13928L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustList_Read_InputArguments =
          new NodeId(UShort.MIN, uint(13929L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustList_Read_OutputArguments =
          new NodeId(UShort.MIN, uint(13930L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustList_Write =
          new NodeId(UShort.MIN, uint(13931L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustList_Write_InputArguments =
          new NodeId(UShort.MIN, uint(13932L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustList_GetPosition =
          new NodeId(UShort.MIN, uint(13933L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustList_GetPosition_InputArguments =
          new NodeId(UShort.MIN, uint(13934L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustList_GetPosition_OutputArguments =
          new NodeId(UShort.MIN, uint(13935L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustList_SetPosition =
          new NodeId(UShort.MIN, uint(13936L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustList_SetPosition_InputArguments =
          new NodeId(UShort.MIN, uint(13937L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustList_LastUpdateTime =
          new NodeId(UShort.MIN, uint(13938L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustList_OpenWithMasks =
          new NodeId(UShort.MIN, uint(13939L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustList_OpenWithMasks_InputArguments =
          new NodeId(UShort.MIN, uint(13940L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustList_OpenWithMasks_OutputArguments =
          new NodeId(UShort.MIN, uint(13941L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustList_CloseAndUpdate =
          new NodeId(UShort.MIN, uint(13942L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustList_CloseAndUpdate_InputArguments =
          new NodeId(UShort.MIN, uint(13943L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustList_CloseAndUpdate_OutputArguments =
          new NodeId(UShort.MIN, uint(13944L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustList_AddCertificate =
          new NodeId(UShort.MIN, uint(13945L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustList_AddCertificate_InputArguments =
          new NodeId(UShort.MIN, uint(13946L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustList_RemoveCertificate =
          new NodeId(UShort.MIN, uint(13947L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustList_RemoveCertificate_InputArguments =
          new NodeId(UShort.MIN, uint(13948L));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateTypes =
          new NodeId(UShort.MIN, uint(13949L));

  public static final NodeId ServerConfigurationType_CertificateGroups =
      new NodeId(UShort.MIN, uint(13950L));

  public static final NodeId ServerConfigurationType_CertificateGroups_DefaultApplicationGroup =
      new NodeId(UShort.MIN, uint(13951L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustList =
          new NodeId(UShort.MIN, uint(13952L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustList_Size =
          new NodeId(UShort.MIN, uint(13953L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustList_Writable =
          new NodeId(UShort.MIN, uint(13954L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustList_UserWritable =
          new NodeId(UShort.MIN, uint(13955L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustList_OpenCount =
          new NodeId(UShort.MIN, uint(13956L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustList_MimeType =
          new NodeId(UShort.MIN, uint(13957L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustList_Open =
          new NodeId(UShort.MIN, uint(13958L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustList_Open_InputArguments =
          new NodeId(UShort.MIN, uint(13959L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustList_Open_OutputArguments =
          new NodeId(UShort.MIN, uint(13960L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustList_Close =
          new NodeId(UShort.MIN, uint(13961L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustList_Close_InputArguments =
          new NodeId(UShort.MIN, uint(13962L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustList_Read =
          new NodeId(UShort.MIN, uint(13963L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustList_Read_InputArguments =
          new NodeId(UShort.MIN, uint(13964L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustList_Read_OutputArguments =
          new NodeId(UShort.MIN, uint(13965L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustList_Write =
          new NodeId(UShort.MIN, uint(13966L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustList_Write_InputArguments =
          new NodeId(UShort.MIN, uint(13967L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustList_GetPosition =
          new NodeId(UShort.MIN, uint(13968L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustList_GetPosition_InputArguments =
          new NodeId(UShort.MIN, uint(13969L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustList_GetPosition_OutputArguments =
          new NodeId(UShort.MIN, uint(13970L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustList_SetPosition =
          new NodeId(UShort.MIN, uint(13971L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustList_SetPosition_InputArguments =
          new NodeId(UShort.MIN, uint(13972L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustList_LastUpdateTime =
          new NodeId(UShort.MIN, uint(13973L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustList_OpenWithMasks =
          new NodeId(UShort.MIN, uint(13974L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustList_OpenWithMasks_InputArguments =
          new NodeId(UShort.MIN, uint(13975L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustList_OpenWithMasks_OutputArguments =
          new NodeId(UShort.MIN, uint(13976L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustList_CloseAndUpdate =
          new NodeId(UShort.MIN, uint(13977L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustList_CloseAndUpdate_InputArguments =
          new NodeId(UShort.MIN, uint(13978L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustList_CloseAndUpdate_OutputArguments =
          new NodeId(UShort.MIN, uint(13979L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustList_AddCertificate =
          new NodeId(UShort.MIN, uint(13980L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustList_AddCertificate_InputArguments =
          new NodeId(UShort.MIN, uint(13981L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustList_RemoveCertificate =
          new NodeId(UShort.MIN, uint(13982L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustList_RemoveCertificate_InputArguments =
          new NodeId(UShort.MIN, uint(13983L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateTypes =
          new NodeId(UShort.MIN, uint(13984L));

  public static final NodeId ServerConfigurationType_CertificateGroups_DefaultHttpsGroup =
      new NodeId(UShort.MIN, uint(13985L));

  public static final NodeId ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustList =
      new NodeId(UShort.MIN, uint(13986L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustList_Size =
          new NodeId(UShort.MIN, uint(13987L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustList_Writable =
          new NodeId(UShort.MIN, uint(13988L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustList_UserWritable =
          new NodeId(UShort.MIN, uint(13989L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustList_OpenCount =
          new NodeId(UShort.MIN, uint(13990L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustList_MimeType =
          new NodeId(UShort.MIN, uint(13991L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustList_Open =
          new NodeId(UShort.MIN, uint(13992L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustList_Open_InputArguments =
          new NodeId(UShort.MIN, uint(13993L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustList_Open_OutputArguments =
          new NodeId(UShort.MIN, uint(13994L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustList_Close =
          new NodeId(UShort.MIN, uint(13995L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustList_Close_InputArguments =
          new NodeId(UShort.MIN, uint(13996L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustList_Read =
          new NodeId(UShort.MIN, uint(13997L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustList_Read_InputArguments =
          new NodeId(UShort.MIN, uint(13998L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustList_Read_OutputArguments =
          new NodeId(UShort.MIN, uint(13999L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustList_Write =
          new NodeId(UShort.MIN, uint(14000L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustList_Write_InputArguments =
          new NodeId(UShort.MIN, uint(14001L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustList_GetPosition =
          new NodeId(UShort.MIN, uint(14002L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustList_GetPosition_InputArguments =
          new NodeId(UShort.MIN, uint(14003L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustList_GetPosition_OutputArguments =
          new NodeId(UShort.MIN, uint(14004L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustList_SetPosition =
          new NodeId(UShort.MIN, uint(14005L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustList_SetPosition_InputArguments =
          new NodeId(UShort.MIN, uint(14006L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustList_LastUpdateTime =
          new NodeId(UShort.MIN, uint(14007L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustList_OpenWithMasks =
          new NodeId(UShort.MIN, uint(14008L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustList_OpenWithMasks_InputArguments =
          new NodeId(UShort.MIN, uint(14009L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustList_OpenWithMasks_OutputArguments =
          new NodeId(UShort.MIN, uint(14010L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustList_CloseAndUpdate =
          new NodeId(UShort.MIN, uint(14011L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustList_CloseAndUpdate_InputArguments =
          new NodeId(UShort.MIN, uint(14012L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustList_CloseAndUpdate_OutputArguments =
          new NodeId(UShort.MIN, uint(14013L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustList_AddCertificate =
          new NodeId(UShort.MIN, uint(14014L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustList_AddCertificate_InputArguments =
          new NodeId(UShort.MIN, uint(14015L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustList_RemoveCertificate =
          new NodeId(UShort.MIN, uint(14016L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustList_RemoveCertificate_InputArguments =
          new NodeId(UShort.MIN, uint(14017L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateTypes =
          new NodeId(UShort.MIN, uint(14018L));

  public static final NodeId ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup =
      new NodeId(UShort.MIN, uint(14019L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_TrustList =
          new NodeId(UShort.MIN, uint(14020L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_TrustList_Size =
          new NodeId(UShort.MIN, uint(14021L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_TrustList_Writable =
          new NodeId(UShort.MIN, uint(14022L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_TrustList_UserWritable =
          new NodeId(UShort.MIN, uint(14023L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_TrustList_OpenCount =
          new NodeId(UShort.MIN, uint(14024L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_TrustList_MimeType =
          new NodeId(UShort.MIN, uint(14025L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_TrustList_Open =
          new NodeId(UShort.MIN, uint(14026L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_TrustList_Open_InputArguments =
          new NodeId(UShort.MIN, uint(14027L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_TrustList_Open_OutputArguments =
          new NodeId(UShort.MIN, uint(14028L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_TrustList_Close =
          new NodeId(UShort.MIN, uint(14029L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_TrustList_Close_InputArguments =
          new NodeId(UShort.MIN, uint(14030L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_TrustList_Read =
          new NodeId(UShort.MIN, uint(14031L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_TrustList_Read_InputArguments =
          new NodeId(UShort.MIN, uint(14032L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_TrustList_Read_OutputArguments =
          new NodeId(UShort.MIN, uint(14033L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_TrustList_Write =
          new NodeId(UShort.MIN, uint(14034L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_TrustList_Write_InputArguments =
          new NodeId(UShort.MIN, uint(14035L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_TrustList_GetPosition =
          new NodeId(UShort.MIN, uint(14036L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_TrustList_GetPosition_InputArguments =
          new NodeId(UShort.MIN, uint(14037L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_TrustList_GetPosition_OutputArguments =
          new NodeId(UShort.MIN, uint(14038L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_TrustList_SetPosition =
          new NodeId(UShort.MIN, uint(14039L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_TrustList_SetPosition_InputArguments =
          new NodeId(UShort.MIN, uint(14040L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_TrustList_LastUpdateTime =
          new NodeId(UShort.MIN, uint(14041L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_TrustList_OpenWithMasks =
          new NodeId(UShort.MIN, uint(14042L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_TrustList_OpenWithMasks_InputArguments =
          new NodeId(UShort.MIN, uint(14043L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_TrustList_OpenWithMasks_OutputArguments =
          new NodeId(UShort.MIN, uint(14044L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_TrustList_CloseAndUpdate =
          new NodeId(UShort.MIN, uint(14045L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_TrustList_CloseAndUpdate_InputArguments =
          new NodeId(UShort.MIN, uint(14046L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_TrustList_CloseAndUpdate_OutputArguments =
          new NodeId(UShort.MIN, uint(14047L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_TrustList_AddCertificate =
          new NodeId(UShort.MIN, uint(14048L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_TrustList_AddCertificate_InputArguments =
          new NodeId(UShort.MIN, uint(14049L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_TrustList_RemoveCertificate =
          new NodeId(UShort.MIN, uint(14050L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_TrustList_RemoveCertificate_InputArguments =
          new NodeId(UShort.MIN, uint(14051L));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_CertificateTypes =
          new NodeId(UShort.MIN, uint(14052L));

  public static final NodeId ServerConfiguration_CertificateGroups =
      new NodeId(UShort.MIN, uint(14053L));

  public static final NodeId ServerConfiguration_CertificateGroups_DefaultHttpsGroup =
      new NodeId(UShort.MIN, uint(14088L));

  public static final NodeId ServerConfiguration_CertificateGroups_DefaultHttpsGroup_TrustList =
      new NodeId(UShort.MIN, uint(14089L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultHttpsGroup_TrustList_Size =
          new NodeId(UShort.MIN, uint(14090L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultHttpsGroup_TrustList_Writable =
          new NodeId(UShort.MIN, uint(14091L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultHttpsGroup_TrustList_UserWritable =
          new NodeId(UShort.MIN, uint(14092L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultHttpsGroup_TrustList_OpenCount =
          new NodeId(UShort.MIN, uint(14093L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultHttpsGroup_TrustList_MimeType =
          new NodeId(UShort.MIN, uint(14094L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultHttpsGroup_TrustList_Open =
          new NodeId(UShort.MIN, uint(14095L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultHttpsGroup_TrustList_Open_InputArguments =
          new NodeId(UShort.MIN, uint(14096L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultHttpsGroup_TrustList_Open_OutputArguments =
          new NodeId(UShort.MIN, uint(14097L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultHttpsGroup_TrustList_Close =
          new NodeId(UShort.MIN, uint(14098L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultHttpsGroup_TrustList_Close_InputArguments =
          new NodeId(UShort.MIN, uint(14099L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultHttpsGroup_TrustList_Read =
          new NodeId(UShort.MIN, uint(14100L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultHttpsGroup_TrustList_Read_InputArguments =
          new NodeId(UShort.MIN, uint(14101L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultHttpsGroup_TrustList_Read_OutputArguments =
          new NodeId(UShort.MIN, uint(14102L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultHttpsGroup_TrustList_Write =
          new NodeId(UShort.MIN, uint(14103L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultHttpsGroup_TrustList_Write_InputArguments =
          new NodeId(UShort.MIN, uint(14104L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultHttpsGroup_TrustList_GetPosition =
          new NodeId(UShort.MIN, uint(14105L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultHttpsGroup_TrustList_GetPosition_InputArguments =
          new NodeId(UShort.MIN, uint(14106L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultHttpsGroup_TrustList_GetPosition_OutputArguments =
          new NodeId(UShort.MIN, uint(14107L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultHttpsGroup_TrustList_SetPosition =
          new NodeId(UShort.MIN, uint(14108L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultHttpsGroup_TrustList_SetPosition_InputArguments =
          new NodeId(UShort.MIN, uint(14109L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultHttpsGroup_TrustList_LastUpdateTime =
          new NodeId(UShort.MIN, uint(14110L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultHttpsGroup_TrustList_OpenWithMasks =
          new NodeId(UShort.MIN, uint(14111L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultHttpsGroup_TrustList_OpenWithMasks_InputArguments =
          new NodeId(UShort.MIN, uint(14112L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultHttpsGroup_TrustList_OpenWithMasks_OutputArguments =
          new NodeId(UShort.MIN, uint(14113L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultHttpsGroup_TrustList_CloseAndUpdate =
          new NodeId(UShort.MIN, uint(14114L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultHttpsGroup_TrustList_CloseAndUpdate_InputArguments =
          new NodeId(UShort.MIN, uint(14115L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultHttpsGroup_TrustList_CloseAndUpdate_OutputArguments =
          new NodeId(UShort.MIN, uint(14116L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultHttpsGroup_TrustList_AddCertificate =
          new NodeId(UShort.MIN, uint(14117L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultHttpsGroup_TrustList_AddCertificate_InputArguments =
          new NodeId(UShort.MIN, uint(14118L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultHttpsGroup_TrustList_RemoveCertificate =
          new NodeId(UShort.MIN, uint(14119L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultHttpsGroup_TrustList_RemoveCertificate_InputArguments =
          new NodeId(UShort.MIN, uint(14120L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultHttpsGroup_CertificateTypes =
          new NodeId(UShort.MIN, uint(14121L));

  public static final NodeId ServerConfiguration_CertificateGroups_DefaultUserTokenGroup =
      new NodeId(UShort.MIN, uint(14122L));

  public static final NodeId ServerConfiguration_CertificateGroups_DefaultUserTokenGroup_TrustList =
      new NodeId(UShort.MIN, uint(14123L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultUserTokenGroup_TrustList_Size =
          new NodeId(UShort.MIN, uint(14124L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultUserTokenGroup_TrustList_Writable =
          new NodeId(UShort.MIN, uint(14125L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultUserTokenGroup_TrustList_UserWritable =
          new NodeId(UShort.MIN, uint(14126L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultUserTokenGroup_TrustList_OpenCount =
          new NodeId(UShort.MIN, uint(14127L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultUserTokenGroup_TrustList_MimeType =
          new NodeId(UShort.MIN, uint(14128L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultUserTokenGroup_TrustList_Open =
          new NodeId(UShort.MIN, uint(14129L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultUserTokenGroup_TrustList_Open_InputArguments =
          new NodeId(UShort.MIN, uint(14130L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultUserTokenGroup_TrustList_Open_OutputArguments =
          new NodeId(UShort.MIN, uint(14131L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultUserTokenGroup_TrustList_Close =
          new NodeId(UShort.MIN, uint(14132L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultUserTokenGroup_TrustList_Close_InputArguments =
          new NodeId(UShort.MIN, uint(14133L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultUserTokenGroup_TrustList_Read =
          new NodeId(UShort.MIN, uint(14134L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultUserTokenGroup_TrustList_Read_InputArguments =
          new NodeId(UShort.MIN, uint(14135L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultUserTokenGroup_TrustList_Read_OutputArguments =
          new NodeId(UShort.MIN, uint(14136L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultUserTokenGroup_TrustList_Write =
          new NodeId(UShort.MIN, uint(14137L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultUserTokenGroup_TrustList_Write_InputArguments =
          new NodeId(UShort.MIN, uint(14138L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultUserTokenGroup_TrustList_GetPosition =
          new NodeId(UShort.MIN, uint(14139L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultUserTokenGroup_TrustList_GetPosition_InputArguments =
          new NodeId(UShort.MIN, uint(14140L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultUserTokenGroup_TrustList_GetPosition_OutputArguments =
          new NodeId(UShort.MIN, uint(14141L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultUserTokenGroup_TrustList_SetPosition =
          new NodeId(UShort.MIN, uint(14142L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultUserTokenGroup_TrustList_SetPosition_InputArguments =
          new NodeId(UShort.MIN, uint(14143L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultUserTokenGroup_TrustList_LastUpdateTime =
          new NodeId(UShort.MIN, uint(14144L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultUserTokenGroup_TrustList_OpenWithMasks =
          new NodeId(UShort.MIN, uint(14145L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultUserTokenGroup_TrustList_OpenWithMasks_InputArguments =
          new NodeId(UShort.MIN, uint(14146L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultUserTokenGroup_TrustList_OpenWithMasks_OutputArguments =
          new NodeId(UShort.MIN, uint(14147L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultUserTokenGroup_TrustList_CloseAndUpdate =
          new NodeId(UShort.MIN, uint(14148L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultUserTokenGroup_TrustList_CloseAndUpdate_InputArguments =
          new NodeId(UShort.MIN, uint(14149L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultUserTokenGroup_TrustList_CloseAndUpdate_OutputArguments =
          new NodeId(UShort.MIN, uint(14150L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultUserTokenGroup_TrustList_AddCertificate =
          new NodeId(UShort.MIN, uint(14151L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultUserTokenGroup_TrustList_AddCertificate_InputArguments =
          new NodeId(UShort.MIN, uint(14152L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultUserTokenGroup_TrustList_RemoveCertificate =
          new NodeId(UShort.MIN, uint(14153L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultUserTokenGroup_TrustList_RemoveCertificate_InputArguments =
          new NodeId(UShort.MIN, uint(14154L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultUserTokenGroup_CertificateTypes =
          new NodeId(UShort.MIN, uint(14155L));

  public static final NodeId ServerConfiguration_CertificateGroups_DefaultApplicationGroup =
      new NodeId(UShort.MIN, uint(14156L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_Writable =
          new NodeId(UShort.MIN, uint(14157L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_UserWritable =
          new NodeId(UShort.MIN, uint(14158L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_MimeType =
          new NodeId(UShort.MIN, uint(14159L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_CloseAndUpdate_InputArguments =
          new NodeId(UShort.MIN, uint(14160L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_CertificateTypes =
          new NodeId(UShort.MIN, uint(14161L));

  public static final NodeId RemoveConnectionMethodType = new NodeId(UShort.MIN, uint(14183L));

  public static final NodeId RemoveConnectionMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(14184L));

  public static final NodeId PubSubConnectionType = new NodeId(UShort.MIN, uint(14209L));

  public static final NodeId PubSubConnectionType_Address = new NodeId(UShort.MIN, uint(14221L));

  public static final NodeId PubSubConnectionType_RemoveGroup =
      new NodeId(UShort.MIN, uint(14225L));

  public static final NodeId PubSubConnectionType_RemoveGroup_InputArguments =
      new NodeId(UShort.MIN, uint(14226L));

  public static final NodeId PubSubGroupType = new NodeId(UShort.MIN, uint(14232L));

  public static final NodeId PublishedVariableDataType = new NodeId(UShort.MIN, uint(14273L));

  public static final NodeId PublishedVariableDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(14319L));

  public static final NodeId OpcUa_XmlSchema_PublishedVariableDataType =
      new NodeId(UShort.MIN, uint(14320L));

  public static final NodeId OpcUa_XmlSchema_PublishedVariableDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(14321L));

  public static final NodeId OpcUa_XmlSchema_PublishedVariableDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(14322L));

  public static final NodeId PublishedVariableDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(14323L));

  public static final NodeId OpcUa_BinarySchema_PublishedVariableDataType =
      new NodeId(UShort.MIN, uint(14324L));

  public static final NodeId OpcUa_BinarySchema_PublishedVariableDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(14325L));

  public static final NodeId OpcUa_BinarySchema_PublishedVariableDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(14326L));

  public static final NodeId Server_ServerRedundancy_ServerNetworkGroups =
      new NodeId(UShort.MIN, uint(14415L));

  public static final NodeId PublishSubscribeType = new NodeId(UShort.MIN, uint(14416L));

  public static final NodeId PublishSubscribeType_ConnectionName_Placeholder =
      new NodeId(UShort.MIN, uint(14417L));

  public static final NodeId PublishSubscribeType_ConnectionName_Placeholder_PublisherId =
      new NodeId(UShort.MIN, uint(14418L));

  public static final NodeId PublishSubscribeType_ConnectionName_Placeholder_Status =
      new NodeId(UShort.MIN, uint(14419L));

  public static final NodeId PublishSubscribeType_ConnectionName_Placeholder_Status_State =
      new NodeId(UShort.MIN, uint(14420L));

  public static final NodeId PublishSubscribeType_ConnectionName_Placeholder_Status_Enable =
      new NodeId(UShort.MIN, uint(14421L));

  public static final NodeId PublishSubscribeType_ConnectionName_Placeholder_Status_Disable =
      new NodeId(UShort.MIN, uint(14422L));

  public static final NodeId PublishSubscribeType_ConnectionName_Placeholder_Address =
      new NodeId(UShort.MIN, uint(14423L));

  public static final NodeId PublishSubscribeType_ConnectionName_Placeholder_RemoveGroup =
      new NodeId(UShort.MIN, uint(14424L));

  public static final NodeId
      PublishSubscribeType_ConnectionName_Placeholder_RemoveGroup_InputArguments =
          new NodeId(UShort.MIN, uint(14425L));

  public static final NodeId PublishSubscribeType_RemoveConnection =
      new NodeId(UShort.MIN, uint(14432L));

  public static final NodeId PublishSubscribeType_RemoveConnection_InputArguments =
      new NodeId(UShort.MIN, uint(14433L));

  public static final NodeId PublishSubscribeType_PublishedDataSets =
      new NodeId(UShort.MIN, uint(14434L));

  public static final NodeId PublishSubscribeType_PublishedDataSets_AddPublishedDataItems =
      new NodeId(UShort.MIN, uint(14435L));

  public static final NodeId
      PublishSubscribeType_PublishedDataSets_AddPublishedDataItems_InputArguments =
          new NodeId(UShort.MIN, uint(14436L));

  public static final NodeId
      PublishSubscribeType_PublishedDataSets_AddPublishedDataItems_OutputArguments =
          new NodeId(UShort.MIN, uint(14437L));

  public static final NodeId PublishSubscribeType_PublishedDataSets_AddPublishedEvents =
      new NodeId(UShort.MIN, uint(14438L));

  public static final NodeId
      PublishSubscribeType_PublishedDataSets_AddPublishedEvents_InputArguments =
          new NodeId(UShort.MIN, uint(14439L));

  public static final NodeId
      PublishSubscribeType_PublishedDataSets_AddPublishedEvents_OutputArguments =
          new NodeId(UShort.MIN, uint(14440L));

  public static final NodeId PublishSubscribeType_PublishedDataSets_RemovePublishedDataSet =
      new NodeId(UShort.MIN, uint(14441L));

  public static final NodeId
      PublishSubscribeType_PublishedDataSets_RemovePublishedDataSet_InputArguments =
          new NodeId(UShort.MIN, uint(14442L));

  public static final NodeId PublishSubscribe = new NodeId(UShort.MIN, uint(14443L));

  public static final NodeId HasPubSubConnection = new NodeId(UShort.MIN, uint(14476L));

  public static final NodeId DataSetFolderType = new NodeId(UShort.MIN, uint(14477L));

  public static final NodeId DataSetFolderType_DataSetFolderName_Placeholder =
      new NodeId(UShort.MIN, uint(14478L));

  public static final NodeId DataSetFolderType_DataSetFolderName_Placeholder_AddPublishedDataItems =
      new NodeId(UShort.MIN, uint(14479L));

  public static final NodeId
      DataSetFolderType_DataSetFolderName_Placeholder_AddPublishedDataItems_InputArguments =
          new NodeId(UShort.MIN, uint(14480L));

  public static final NodeId
      DataSetFolderType_DataSetFolderName_Placeholder_AddPublishedDataItems_OutputArguments =
          new NodeId(UShort.MIN, uint(14481L));

  public static final NodeId DataSetFolderType_DataSetFolderName_Placeholder_AddPublishedEvents =
      new NodeId(UShort.MIN, uint(14482L));

  public static final NodeId
      DataSetFolderType_DataSetFolderName_Placeholder_AddPublishedEvents_InputArguments =
          new NodeId(UShort.MIN, uint(14483L));

  public static final NodeId
      DataSetFolderType_DataSetFolderName_Placeholder_AddPublishedEvents_OutputArguments =
          new NodeId(UShort.MIN, uint(14484L));

  public static final NodeId
      DataSetFolderType_DataSetFolderName_Placeholder_RemovePublishedDataSet =
          new NodeId(UShort.MIN, uint(14485L));

  public static final NodeId
      DataSetFolderType_DataSetFolderName_Placeholder_RemovePublishedDataSet_InputArguments =
          new NodeId(UShort.MIN, uint(14486L));

  public static final NodeId DataSetFolderType_PublishedDataSetName_Placeholder =
      new NodeId(UShort.MIN, uint(14487L));

  public static final NodeId
      DataSetFolderType_PublishedDataSetName_Placeholder_ConfigurationVersion =
          new NodeId(UShort.MIN, uint(14489L));

  public static final NodeId DataSetFolderType_AddPublishedDataItems =
      new NodeId(UShort.MIN, uint(14493L));

  public static final NodeId DataSetFolderType_AddPublishedDataItems_InputArguments =
      new NodeId(UShort.MIN, uint(14494L));

  public static final NodeId DataSetFolderType_AddPublishedDataItems_OutputArguments =
      new NodeId(UShort.MIN, uint(14495L));

  public static final NodeId DataSetFolderType_AddPublishedEvents =
      new NodeId(UShort.MIN, uint(14496L));

  public static final NodeId DataSetFolderType_AddPublishedEvents_InputArguments =
      new NodeId(UShort.MIN, uint(14497L));

  public static final NodeId DataSetFolderType_AddPublishedEvents_OutputArguments =
      new NodeId(UShort.MIN, uint(14498L));

  public static final NodeId DataSetFolderType_RemovePublishedDataSet =
      new NodeId(UShort.MIN, uint(14499L));

  public static final NodeId DataSetFolderType_RemovePublishedDataSet_InputArguments =
      new NodeId(UShort.MIN, uint(14500L));

  public static final NodeId AddPublishedDataItemsMethodType = new NodeId(UShort.MIN, uint(14501L));

  public static final NodeId AddPublishedDataItemsMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(14502L));

  public static final NodeId AddPublishedDataItemsMethodType_OutputArguments =
      new NodeId(UShort.MIN, uint(14503L));

  public static final NodeId AddPublishedEventsMethodType = new NodeId(UShort.MIN, uint(14504L));

  public static final NodeId AddPublishedEventsMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(14505L));

  public static final NodeId AddPublishedEventsMethodType_OutputArguments =
      new NodeId(UShort.MIN, uint(14506L));

  public static final NodeId RemovePublishedDataSetMethodType =
      new NodeId(UShort.MIN, uint(14507L));

  public static final NodeId RemovePublishedDataSetMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(14508L));

  public static final NodeId PublishedDataSetType = new NodeId(UShort.MIN, uint(14509L));

  public static final NodeId PublishedDataSetType_ConfigurationVersion =
      new NodeId(UShort.MIN, uint(14519L));

  public static final NodeId DataSetMetaDataType = new NodeId(UShort.MIN, uint(14523L));

  public static final NodeId FieldMetaData = new NodeId(UShort.MIN, uint(14524L));

  public static final NodeId DataTypeDescription = new NodeId(UShort.MIN, uint(14525L));

  public static final NodeId StructureType_EnumStrings = new NodeId(UShort.MIN, uint(14528L));

  public static final NodeId KeyValuePair = new NodeId(UShort.MIN, uint(14533L));

  public static final NodeId PublishedDataItemsType = new NodeId(UShort.MIN, uint(14534L));

  public static final NodeId PublishedDataItemsType_PublishedData =
      new NodeId(UShort.MIN, uint(14548L));

  public static final NodeId PublishedDataItemsType_AddVariables =
      new NodeId(UShort.MIN, uint(14555L));

  public static final NodeId PublishedDataItemsType_AddVariables_InputArguments =
      new NodeId(UShort.MIN, uint(14556L));

  public static final NodeId PublishedDataItemsType_AddVariables_OutputArguments =
      new NodeId(UShort.MIN, uint(14557L));

  public static final NodeId PublishedDataItemsType_RemoveVariables =
      new NodeId(UShort.MIN, uint(14558L));

  public static final NodeId PublishedDataItemsType_RemoveVariables_InputArguments =
      new NodeId(UShort.MIN, uint(14559L));

  public static final NodeId PublishedDataItemsType_RemoveVariables_OutputArguments =
      new NodeId(UShort.MIN, uint(14560L));

  public static final NodeId PublishedDataItemsAddVariablesMethodType =
      new NodeId(UShort.MIN, uint(14564L));

  public static final NodeId PublishedDataItemsAddVariablesMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(14565L));

  public static final NodeId PublishedDataItemsAddVariablesMethodType_OutputArguments =
      new NodeId(UShort.MIN, uint(14566L));

  public static final NodeId PublishedDataItemsRemoveVariablesMethodType =
      new NodeId(UShort.MIN, uint(14567L));

  public static final NodeId PublishedDataItemsRemoveVariablesMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(14568L));

  public static final NodeId PublishedDataItemsRemoveVariablesMethodType_OutputArguments =
      new NodeId(UShort.MIN, uint(14569L));

  public static final NodeId PublishedEventsType = new NodeId(UShort.MIN, uint(14572L));

  public static final NodeId PublishedEventsType_PubSubEventNotifier =
      new NodeId(UShort.MIN, uint(14586L));

  public static final NodeId PublishedEventsType_SelectedFields =
      new NodeId(UShort.MIN, uint(14587L));

  public static final NodeId PublishedEventsType_Filter = new NodeId(UShort.MIN, uint(14588L));

  public static final NodeId ConfigurationVersionDataType = new NodeId(UShort.MIN, uint(14593L));

  public static final NodeId PubSubConnectionType_PublisherId =
      new NodeId(UShort.MIN, uint(14595L));

  public static final NodeId PubSubConnectionType_Status = new NodeId(UShort.MIN, uint(14600L));

  public static final NodeId PubSubConnectionType_Status_State =
      new NodeId(UShort.MIN, uint(14601L));

  public static final NodeId PubSubConnectionType_Status_Enable =
      new NodeId(UShort.MIN, uint(14602L));

  public static final NodeId PubSubConnectionType_Status_Disable =
      new NodeId(UShort.MIN, uint(14603L));

  public static final NodeId PubSubConnectionTypeRemoveGroupMethodType =
      new NodeId(UShort.MIN, uint(14604L));

  public static final NodeId PubSubConnectionTypeRemoveGroupMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(14605L));

  public static final NodeId PubSubGroupTypeRemoveWriterMethodType =
      new NodeId(UShort.MIN, uint(14623L));

  public static final NodeId PubSubGroupTypeRemoveWriterMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(14624L));

  public static final NodeId PubSubGroupTypeRemoveReaderMethodType =
      new NodeId(UShort.MIN, uint(14625L));

  public static final NodeId PubSubGroupTypeRemoveReaderMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(14626L));

  public static final NodeId PubSubStatusType = new NodeId(UShort.MIN, uint(14643L));

  public static final NodeId PubSubStatusType_State = new NodeId(UShort.MIN, uint(14644L));

  public static final NodeId PubSubStatusType_Enable = new NodeId(UShort.MIN, uint(14645L));

  public static final NodeId PubSubStatusType_Disable = new NodeId(UShort.MIN, uint(14646L));

  public static final NodeId PubSubState = new NodeId(UShort.MIN, uint(14647L));

  public static final NodeId PubSubState_EnumStrings = new NodeId(UShort.MIN, uint(14648L));

  public static final NodeId FieldTargetDataType = new NodeId(UShort.MIN, uint(14744L));

  public static final NodeId DataSetMetaDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(14794L));

  public static final NodeId FieldMetaData_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(14795L));

  public static final NodeId DataTypeDescription_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(14796L));

  public static final NodeId DataTypeDefinition_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(14797L));

  public static final NodeId StructureDefinition_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(14798L));

  public static final NodeId EnumDefinition_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(14799L));

  public static final NodeId StructureField_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(14800L));

  public static final NodeId EnumField_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(14801L));

  public static final NodeId KeyValuePair_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(14802L));

  public static final NodeId ConfigurationVersionDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(14803L));

  public static final NodeId FieldTargetDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(14804L));

  public static final NodeId OpcUa_XmlSchema_DataSetMetaDataType =
      new NodeId(UShort.MIN, uint(14805L));

  public static final NodeId OpcUa_XmlSchema_DataSetMetaDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(14806L));

  public static final NodeId OpcUa_XmlSchema_DataSetMetaDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(14807L));

  public static final NodeId OpcUa_XmlSchema_FieldMetaData = new NodeId(UShort.MIN, uint(14808L));

  public static final NodeId OpcUa_XmlSchema_FieldMetaData_DataTypeVersion =
      new NodeId(UShort.MIN, uint(14809L));

  public static final NodeId OpcUa_XmlSchema_FieldMetaData_DictionaryFragment =
      new NodeId(UShort.MIN, uint(14810L));

  public static final NodeId OpcUa_XmlSchema_DataTypeDescription =
      new NodeId(UShort.MIN, uint(14811L));

  public static final NodeId OpcUa_XmlSchema_DataTypeDescription_DataTypeVersion =
      new NodeId(UShort.MIN, uint(14812L));

  public static final NodeId OpcUa_XmlSchema_DataTypeDescription_DictionaryFragment =
      new NodeId(UShort.MIN, uint(14813L));

  public static final NodeId OpcUa_XmlSchema_EnumField = new NodeId(UShort.MIN, uint(14826L));

  public static final NodeId OpcUa_XmlSchema_EnumField_DataTypeVersion =
      new NodeId(UShort.MIN, uint(14827L));

  public static final NodeId OpcUa_XmlSchema_EnumField_DictionaryFragment =
      new NodeId(UShort.MIN, uint(14828L));

  public static final NodeId OpcUa_XmlSchema_KeyValuePair = new NodeId(UShort.MIN, uint(14829L));

  public static final NodeId OpcUa_XmlSchema_KeyValuePair_DataTypeVersion =
      new NodeId(UShort.MIN, uint(14830L));

  public static final NodeId OpcUa_XmlSchema_KeyValuePair_DictionaryFragment =
      new NodeId(UShort.MIN, uint(14831L));

  public static final NodeId OpcUa_XmlSchema_ConfigurationVersionDataType =
      new NodeId(UShort.MIN, uint(14832L));

  public static final NodeId OpcUa_XmlSchema_ConfigurationVersionDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(14833L));

  public static final NodeId OpcUa_XmlSchema_ConfigurationVersionDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(14834L));

  public static final NodeId OpcUa_XmlSchema_FieldTargetDataType =
      new NodeId(UShort.MIN, uint(14835L));

  public static final NodeId OpcUa_XmlSchema_FieldTargetDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(14836L));

  public static final NodeId OpcUa_XmlSchema_FieldTargetDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(14837L));

  public static final NodeId FieldMetaData_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(14839L));

  public static final NodeId StructureField_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(14844L));

  public static final NodeId EnumField_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(14845L));

  public static final NodeId KeyValuePair_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(14846L));

  public static final NodeId ConfigurationVersionDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(14847L));

  public static final NodeId FieldTargetDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(14848L));

  public static final NodeId OpcUa_BinarySchema_DataSetMetaDataType =
      new NodeId(UShort.MIN, uint(14849L));

  public static final NodeId OpcUa_BinarySchema_DataSetMetaDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(14850L));

  public static final NodeId OpcUa_BinarySchema_DataSetMetaDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(14851L));

  public static final NodeId OpcUa_BinarySchema_FieldMetaData =
      new NodeId(UShort.MIN, uint(14852L));

  public static final NodeId OpcUa_BinarySchema_FieldMetaData_DataTypeVersion =
      new NodeId(UShort.MIN, uint(14853L));

  public static final NodeId OpcUa_BinarySchema_FieldMetaData_DictionaryFragment =
      new NodeId(UShort.MIN, uint(14854L));

  public static final NodeId OpcUa_BinarySchema_DataTypeDescription =
      new NodeId(UShort.MIN, uint(14855L));

  public static final NodeId OpcUa_BinarySchema_DataTypeDescription_DataTypeVersion =
      new NodeId(UShort.MIN, uint(14856L));

  public static final NodeId OpcUa_BinarySchema_DataTypeDescription_DictionaryFragment =
      new NodeId(UShort.MIN, uint(14857L));

  public static final NodeId OpcUa_BinarySchema_EnumField = new NodeId(UShort.MIN, uint(14870L));

  public static final NodeId OpcUa_BinarySchema_EnumField_DataTypeVersion =
      new NodeId(UShort.MIN, uint(14871L));

  public static final NodeId OpcUa_BinarySchema_EnumField_DictionaryFragment =
      new NodeId(UShort.MIN, uint(14872L));

  public static final NodeId OpcUa_BinarySchema_KeyValuePair = new NodeId(UShort.MIN, uint(14873L));

  public static final NodeId OpcUa_BinarySchema_KeyValuePair_DataTypeVersion =
      new NodeId(UShort.MIN, uint(14874L));

  public static final NodeId OpcUa_BinarySchema_KeyValuePair_DictionaryFragment =
      new NodeId(UShort.MIN, uint(14875L));

  public static final NodeId OpcUa_BinarySchema_ConfigurationVersionDataType =
      new NodeId(UShort.MIN, uint(14876L));

  public static final NodeId OpcUa_BinarySchema_ConfigurationVersionDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(14877L));

  public static final NodeId OpcUa_BinarySchema_ConfigurationVersionDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(14878L));

  public static final NodeId OpcUa_BinarySchema_FieldTargetDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(14880L));

  public static final NodeId OpcUa_BinarySchema_FieldTargetDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(14881L));

  public static final NodeId CertificateExpirationAlarmType_ExpirationLimit =
      new NodeId(UShort.MIN, uint(14900L));

  public static final NodeId DataSetToWriter = new NodeId(UShort.MIN, uint(14936L));

  public static final NodeId DataTypeDictionaryType_Deprecated =
      new NodeId(UShort.MIN, uint(15001L));

  public static final NodeId MaxCharacters = new NodeId(UShort.MIN, uint(15002L));

  public static final NodeId ServerType_UrisVersion = new NodeId(UShort.MIN, uint(15003L));

  public static final NodeId Server_UrisVersion = new NodeId(UShort.MIN, uint(15004L));

  public static final NodeId SimpleTypeDescription = new NodeId(UShort.MIN, uint(15005L));

  public static final NodeId UABinaryFileDataType = new NodeId(UShort.MIN, uint(15006L));

  public static final NodeId BrokerConnectionTransportDataType =
      new NodeId(UShort.MIN, uint(15007L));

  public static final NodeId BrokerTransportQualityOfService = new NodeId(UShort.MIN, uint(15008L));

  public static final NodeId BrokerTransportQualityOfService_EnumStrings =
      new NodeId(UShort.MIN, uint(15009L));

  public static final NodeId SecurityGroupFolderType_SecurityGroupName_Placeholder_KeyLifetime =
      new NodeId(UShort.MIN, uint(15010L));

  public static final NodeId
      SecurityGroupFolderType_SecurityGroupName_Placeholder_SecurityPolicyUri =
          new NodeId(UShort.MIN, uint(15011L));

  public static final NodeId
      SecurityGroupFolderType_SecurityGroupName_Placeholder_MaxFutureKeyCount =
          new NodeId(UShort.MIN, uint(15012L));

  public static final NodeId AuditConditionResetEventType = new NodeId(UShort.MIN, uint(15013L));

  public static final NodeId BitFieldType_OptionalFieldNamee_Placeholder =
      new NodeId(UShort.MIN, uint(15014L));

  public static final NodeId OpcUa_BinarySchema_ModificationInfo =
      new NodeId(UShort.MIN, uint(15018L));

  public static final NodeId OpcUa_BinarySchema_ModificationInfo_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15019L));

  public static final NodeId OpcUa_BinarySchema_ModificationInfo_DictionaryFragment =
      new NodeId(UShort.MIN, uint(15020L));

  public static final NodeId OpcUa_XmlSchema_ModificationInfo =
      new NodeId(UShort.MIN, uint(15021L));

  public static final NodeId OpcUa_XmlSchema_ModificationInfo_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15022L));

  public static final NodeId OpcUa_XmlSchema_ModificationInfo_DictionaryFragment =
      new NodeId(UShort.MIN, uint(15023L));

  public static final NodeId PermissionType_OptionSetValues = new NodeId(UShort.MIN, uint(15030L));

  public static final NodeId AccessLevelType = new NodeId(UShort.MIN, uint(15031L));

  public static final NodeId AccessLevelType_OptionSetValues = new NodeId(UShort.MIN, uint(15032L));

  public static final NodeId EventNotifierType = new NodeId(UShort.MIN, uint(15033L));

  public static final NodeId EventNotifierType_OptionSetValues =
      new NodeId(UShort.MIN, uint(15034L));

  public static final NodeId AccessRestrictionType_OptionSetValues =
      new NodeId(UShort.MIN, uint(15035L));

  public static final NodeId AttributeWriteMask_OptionSetValues =
      new NodeId(UShort.MIN, uint(15036L));

  public static final NodeId OpcUa_BinarySchema_Deprecated = new NodeId(UShort.MIN, uint(15037L));

  public static final NodeId ProgramStateMachineType_ProgramDiagnostic_LastMethodInputValues =
      new NodeId(UShort.MIN, uint(15038L));

  public static final NodeId OpcUa_XmlSchema_Deprecated = new NodeId(UShort.MIN, uint(15039L));

  public static final NodeId ProgramStateMachineType_ProgramDiagnostic_LastMethodOutputValues =
      new NodeId(UShort.MIN, uint(15040L));

  public static final NodeId KeyValuePair_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15041L));

  public static final NodeId IdentityMappingRuleType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15042L));

  public static final NodeId SecurityGroupFolderType_SecurityGroupName_Placeholder_MaxPastKeyCount =
      new NodeId(UShort.MIN, uint(15043L));

  public static final NodeId TrustListDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15044L));

  public static final NodeId DecimalDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15045L));

  public static final NodeId SecurityGroupType_KeyLifetime = new NodeId(UShort.MIN, uint(15046L));

  public static final NodeId SecurityGroupType_SecurityPolicyUri =
      new NodeId(UShort.MIN, uint(15047L));

  public static final NodeId SecurityGroupType_MaxFutureKeyCount =
      new NodeId(UShort.MIN, uint(15048L));

  public static final NodeId ConfigurationVersionDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15049L));

  public static final NodeId DataSetMetaDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15050L));

  public static final NodeId FieldMetaData_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15051L));

  public static final NodeId PublishedEventsType_ModifyFieldSelection =
      new NodeId(UShort.MIN, uint(15052L));

  public static final NodeId PublishedEventsType_ModifyFieldSelection_InputArguments =
      new NodeId(UShort.MIN, uint(15053L));

  public static final NodeId PublishedEventsTypeModifyFieldSelectionMethodType =
      new NodeId(UShort.MIN, uint(15054L));

  public static final NodeId PublishedEventsTypeModifyFieldSelectionMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(15055L));

  public static final NodeId SecurityGroupType_MaxPastKeyCount =
      new NodeId(UShort.MIN, uint(15056L));

  public static final NodeId DataTypeDescription_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15057L));

  public static final NodeId StructureDescription_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15058L));

  public static final NodeId EnumDescription_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15059L));

  public static final NodeId PublishedVariableDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15060L));

  public static final NodeId FieldTargetDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15061L));

  public static final NodeId RolePermissionType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15062L));

  public static final NodeId DataTypeDefinition_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15063L));

  public static final NodeId DatagramConnectionTransportType = new NodeId(UShort.MIN, uint(15064L));

  public static final NodeId StructureField_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15065L));

  public static final NodeId StructureDefinition_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15066L));

  public static final NodeId EnumDefinition_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15067L));

  public static final NodeId Node_Encoding_DefaultJson = new NodeId(UShort.MIN, uint(15068L));

  public static final NodeId InstanceNode_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15069L));

  public static final NodeId TypeNode_Encoding_DefaultJson = new NodeId(UShort.MIN, uint(15070L));

  public static final NodeId ObjectNode_Encoding_DefaultJson = new NodeId(UShort.MIN, uint(15071L));

  public static final NodeId DatagramConnectionTransportType_DiscoveryAddress =
      new NodeId(UShort.MIN, uint(15072L));

  public static final NodeId ObjectTypeNode_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15073L));

  public static final NodeId VariableNode_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15074L));

  public static final NodeId VariableTypeNode_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15075L));

  public static final NodeId ReferenceTypeNode_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15076L));

  public static final NodeId MethodNode_Encoding_DefaultJson = new NodeId(UShort.MIN, uint(15077L));

  public static final NodeId ViewNode_Encoding_DefaultJson = new NodeId(UShort.MIN, uint(15078L));

  public static final NodeId DataTypeNode_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15079L));

  public static final NodeId ReferenceNode_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15080L));

  public static final NodeId Argument_Encoding_DefaultJson = new NodeId(UShort.MIN, uint(15081L));

  public static final NodeId EnumValueType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15082L));

  public static final NodeId EnumField_Encoding_DefaultJson = new NodeId(UShort.MIN, uint(15083L));

  public static final NodeId OptionSet_Encoding_DefaultJson = new NodeId(UShort.MIN, uint(15084L));

  public static final NodeId Union_Encoding_DefaultJson = new NodeId(UShort.MIN, uint(15085L));

  public static final NodeId TimeZoneDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15086L));

  public static final NodeId ApplicationDescription_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15087L));

  public static final NodeId RequestHeader_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15088L));

  public static final NodeId ResponseHeader_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15089L));

  public static final NodeId ServiceFault_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15090L));

  public static final NodeId SessionlessInvokeRequestType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15091L));

  public static final NodeId SessionlessInvokeResponseType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15092L));

  public static final NodeId FindServersRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15093L));

  public static final NodeId FindServersResponse_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15094L));

  public static final NodeId ServerOnNetwork_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15095L));

  public static final NodeId FindServersOnNetworkRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15096L));

  public static final NodeId FindServersOnNetworkResponse_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15097L));

  public static final NodeId UserTokenPolicy_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15098L));

  public static final NodeId EndpointDescription_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15099L));

  public static final NodeId GetEndpointsRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15100L));

  public static final NodeId GetEndpointsResponse_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15101L));

  public static final NodeId RegisteredServer_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15102L));

  public static final NodeId RegisterServerRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15103L));

  public static final NodeId RegisterServerResponse_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15104L));

  public static final NodeId DiscoveryConfiguration_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15105L));

  public static final NodeId MdnsDiscoveryConfiguration_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15106L));

  public static final NodeId RegisterServer2Request_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15107L));

  public static final NodeId SubscribedDataSetType = new NodeId(UShort.MIN, uint(15108L));

  public static final NodeId ChoiceStateType = new NodeId(UShort.MIN, uint(15109L));

  public static final NodeId TargetVariablesType = new NodeId(UShort.MIN, uint(15111L));

  public static final NodeId HasGuard = new NodeId(UShort.MIN, uint(15112L));

  public static final NodeId GuardVariableType = new NodeId(UShort.MIN, uint(15113L));

  public static final NodeId TargetVariablesType_TargetVariables =
      new NodeId(UShort.MIN, uint(15114L));

  public static final NodeId TargetVariablesType_AddTargetVariables =
      new NodeId(UShort.MIN, uint(15115L));

  public static final NodeId TargetVariablesType_AddTargetVariables_InputArguments =
      new NodeId(UShort.MIN, uint(15116L));

  public static final NodeId TargetVariablesType_AddTargetVariables_OutputArguments =
      new NodeId(UShort.MIN, uint(15117L));

  public static final NodeId TargetVariablesType_RemoveTargetVariables =
      new NodeId(UShort.MIN, uint(15118L));

  public static final NodeId TargetVariablesType_RemoveTargetVariables_InputArguments =
      new NodeId(UShort.MIN, uint(15119L));

  public static final NodeId TargetVariablesType_RemoveTargetVariables_OutputArguments =
      new NodeId(UShort.MIN, uint(15120L));

  public static final NodeId TargetVariablesTypeAddTargetVariablesMethodType =
      new NodeId(UShort.MIN, uint(15121L));

  public static final NodeId TargetVariablesTypeAddTargetVariablesMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(15122L));

  public static final NodeId TargetVariablesTypeAddTargetVariablesMethodType_OutputArguments =
      new NodeId(UShort.MIN, uint(15123L));

  public static final NodeId TargetVariablesTypeRemoveTargetVariablesMethodType =
      new NodeId(UShort.MIN, uint(15124L));

  public static final NodeId TargetVariablesTypeRemoveTargetVariablesMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(15125L));

  public static final NodeId TargetVariablesTypeRemoveTargetVariablesMethodType_OutputArguments =
      new NodeId(UShort.MIN, uint(15126L));

  public static final NodeId SubscribedDataSetMirrorType = new NodeId(UShort.MIN, uint(15127L));

  public static final NodeId ExpressionGuardVariableType = new NodeId(UShort.MIN, uint(15128L));

  public static final NodeId ExpressionGuardVariableType_Expression =
      new NodeId(UShort.MIN, uint(15129L));

  public static final NodeId RegisterServer2Response_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15130L));

  public static final NodeId ChannelSecurityToken_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15131L));

  public static final NodeId OpenSecureChannelRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15132L));

  public static final NodeId OpenSecureChannelResponse_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15133L));

  public static final NodeId CloseSecureChannelRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15134L));

  public static final NodeId CloseSecureChannelResponse_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15135L));

  public static final NodeId SignedSoftwareCertificate_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15136L));

  public static final NodeId SignatureData_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15137L));

  public static final NodeId CreateSessionRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15138L));

  public static final NodeId CreateSessionResponse_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15139L));

  public static final NodeId UserIdentityToken_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15140L));

  public static final NodeId AnonymousIdentityToken_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15141L));

  public static final NodeId UserNameIdentityToken_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15142L));

  public static final NodeId X509IdentityToken_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15143L));

  public static final NodeId IssuedIdentityToken_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15144L));

  public static final NodeId ActivateSessionRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15145L));

  public static final NodeId ActivateSessionResponse_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15146L));

  public static final NodeId CloseSessionRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15147L));

  public static final NodeId CloseSessionResponse_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15148L));

  public static final NodeId CancelRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15149L));

  public static final NodeId CancelResponse_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15150L));

  public static final NodeId NodeAttributes_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15151L));

  public static final NodeId ObjectAttributes_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15152L));

  public static final NodeId VariableAttributes_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15153L));

  public static final NodeId DatagramConnectionTransportType_DiscoveryAddress_NetworkInterface =
      new NodeId(UShort.MIN, uint(15154L));

  public static final NodeId BrokerConnectionTransportType = new NodeId(UShort.MIN, uint(15155L));

  public static final NodeId BrokerConnectionTransportType_ResourceUri =
      new NodeId(UShort.MIN, uint(15156L));

  public static final NodeId MethodAttributes_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15157L));

  public static final NodeId ObjectTypeAttributes_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15158L));

  public static final NodeId VariableTypeAttributes_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15159L));

  public static final NodeId ReferenceTypeAttributes_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15160L));

  public static final NodeId DataTypeAttributes_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15161L));

  public static final NodeId ViewAttributes_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15162L));

  public static final NodeId GenericAttributeValue_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15163L));

  public static final NodeId GenericAttributes_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15164L));

  public static final NodeId AddNodesItem_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15165L));

  public static final NodeId AddNodesResult_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15166L));

  public static final NodeId AddNodesRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15167L));

  public static final NodeId AddNodesResponse_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15168L));

  public static final NodeId AddReferencesItem_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15169L));

  public static final NodeId AddReferencesRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15170L));

  public static final NodeId AddReferencesResponse_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15171L));

  public static final NodeId DeleteNodesItem_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15172L));

  public static final NodeId DeleteNodesRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15173L));

  public static final NodeId DeleteNodesResponse_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15174L));

  public static final NodeId DeleteReferencesItem_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15175L));

  public static final NodeId DeleteReferencesRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15176L));

  public static final NodeId DeleteReferencesResponse_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15177L));

  public static final NodeId BrokerConnectionTransportType_AuthenticationProfileUri =
      new NodeId(UShort.MIN, uint(15178L));

  public static final NodeId ViewDescription_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15179L));

  public static final NodeId BrowseDescription_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15180L));

  public static final NodeId ReferenceDescription_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15182L));

  public static final NodeId BrowseResult_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15183L));

  public static final NodeId BrowseRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15184L));

  public static final NodeId BrowseResponse_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15185L));

  public static final NodeId BrowseNextRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15186L));

  public static final NodeId BrowseNextResponse_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15187L));

  public static final NodeId RelativePathElement_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15188L));

  public static final NodeId RelativePath_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15189L));

  public static final NodeId BrowsePath_Encoding_DefaultJson = new NodeId(UShort.MIN, uint(15190L));

  public static final NodeId BrowsePathTarget_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15191L));

  public static final NodeId BrowsePathResult_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15192L));

  public static final NodeId TranslateBrowsePathsToNodeIdsRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15193L));

  public static final NodeId TranslateBrowsePathsToNodeIdsResponse_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15194L));

  public static final NodeId RegisterNodesRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15195L));

  public static final NodeId RegisterNodesResponse_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15196L));

  public static final NodeId UnregisterNodesRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15197L));

  public static final NodeId UnregisterNodesResponse_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15198L));

  public static final NodeId EndpointConfiguration_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15199L));

  public static final NodeId QueryDataDescription_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15200L));

  public static final NodeId NodeTypeDescription_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15201L));

  public static final NodeId QueryDataSet_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15202L));

  public static final NodeId NodeReference_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15203L));

  public static final NodeId ContentFilterElement_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15204L));

  public static final NodeId ContentFilter_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15205L));

  public static final NodeId FilterOperand_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15206L));

  public static final NodeId ElementOperand_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15207L));

  public static final NodeId LiteralOperand_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15208L));

  public static final NodeId AttributeOperand_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15209L));

  public static final NodeId SimpleAttributeOperand_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15210L));

  public static final NodeId ContentFilterElementResult_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15211L));

  public static final NodeId PublishSubscribe_GetSecurityKeys =
      new NodeId(UShort.MIN, uint(15215L));

  public static final NodeId PublishSubscribe_GetSecurityKeys_InputArguments =
      new NodeId(UShort.MIN, uint(15216L));

  public static final NodeId PublishSubscribe_GetSecurityKeys_OutputArguments =
      new NodeId(UShort.MIN, uint(15217L));

  public static final NodeId GetSecurityKeysMethodType = new NodeId(UShort.MIN, uint(15218L));

  public static final NodeId GetSecurityKeysMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(15219L));

  public static final NodeId GetSecurityKeysMethodType_OutputArguments =
      new NodeId(UShort.MIN, uint(15220L));

  public static final NodeId DataSetFolderType_PublishedDataSetName_Placeholder_DataSetMetaData =
      new NodeId(UShort.MIN, uint(15221L));

  public static final NodeId PublishedDataSetType_DataSetWriterName_Placeholder =
      new NodeId(UShort.MIN, uint(15222L));

  public static final NodeId PublishedDataSetType_DataSetWriterName_Placeholder_Status =
      new NodeId(UShort.MIN, uint(15223L));

  public static final NodeId PublishedDataSetType_DataSetWriterName_Placeholder_Status_State =
      new NodeId(UShort.MIN, uint(15224L));

  public static final NodeId PublishedDataSetType_DataSetWriterName_Placeholder_Status_Enable =
      new NodeId(UShort.MIN, uint(15225L));

  public static final NodeId PublishedDataSetType_DataSetWriterName_Placeholder_Status_Disable =
      new NodeId(UShort.MIN, uint(15226L));

  public static final NodeId PublishedDataSetType_DataSetWriterName_Placeholder_TransportSettings =
      new NodeId(UShort.MIN, uint(15227L));

  public static final NodeId ContentFilterResult_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15228L));

  public static final NodeId PublishedDataSetType_DataSetMetaData =
      new NodeId(UShort.MIN, uint(15229L));

  public static final NodeId ParsingResult_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15236L));

  public static final NodeId QueryFirstRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15244L));

  public static final NodeId BrokerWriterGroupTransportType_ResourceUri =
      new NodeId(UShort.MIN, uint(15246L));

  public static final NodeId BrokerWriterGroupTransportType_AuthenticationProfileUri =
      new NodeId(UShort.MIN, uint(15247L));

  public static final NodeId CreateCredentialMethodType = new NodeId(UShort.MIN, uint(15248L));

  public static final NodeId BrokerWriterGroupTransportType_RequestedDeliveryGuarantee =
      new NodeId(UShort.MIN, uint(15249L));

  public static final NodeId BrokerDataSetWriterTransportType_ResourceUri =
      new NodeId(UShort.MIN, uint(15250L));

  public static final NodeId BrokerDataSetWriterTransportType_AuthenticationProfileUri =
      new NodeId(UShort.MIN, uint(15251L));

  public static final NodeId QueryFirstResponse_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15252L));

  public static final NodeId CreateCredentialMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(15253L));

  public static final NodeId QueryNextRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15254L));

  public static final NodeId QueryNextResponse_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15255L));

  public static final NodeId ReadValueId_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15256L));

  public static final NodeId ReadRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15257L));

  public static final NodeId ReadResponse_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15258L));

  public static final NodeId HistoryReadValueId_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15259L));

  public static final NodeId HistoryReadResult_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15260L));

  public static final NodeId HistoryReadDetails_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15261L));

  public static final NodeId ReadEventDetails_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15262L));

  public static final NodeId ReadRawModifiedDetails_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15263L));

  public static final NodeId ReadProcessedDetails_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15264L));

  public static final NodeId PubSubGroupType_Status = new NodeId(UShort.MIN, uint(15265L));

  public static final NodeId PubSubGroupType_Status_State = new NodeId(UShort.MIN, uint(15266L));

  public static final NodeId PubSubGroupType_Status_Enable = new NodeId(UShort.MIN, uint(15267L));

  public static final NodeId PubSubGroupType_Status_Disable = new NodeId(UShort.MIN, uint(15268L));

  public static final NodeId ReadAtTimeDetails_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15269L));

  public static final NodeId HistoryData_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15270L));

  public static final NodeId ModificationInfo_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15271L));

  public static final NodeId HistoryModifiedData_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15272L));

  public static final NodeId HistoryEvent_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15273L));

  public static final NodeId HistoryReadRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15274L));

  public static final NodeId HistoryReadResponse_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15275L));

  public static final NodeId WriteValue_Encoding_DefaultJson = new NodeId(UShort.MIN, uint(15276L));

  public static final NodeId WriteRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15277L));

  public static final NodeId WriteResponse_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15278L));

  public static final NodeId HistoryUpdateDetails_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15279L));

  public static final NodeId UpdateDataDetails_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15280L));

  public static final NodeId UpdateStructureDataDetails_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15281L));

  public static final NodeId UpdateEventDetails_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15282L));

  public static final NodeId DeleteRawModifiedDetails_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15283L));

  public static final NodeId DeleteAtTimeDetails_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15284L));

  public static final NodeId DeleteEventDetails_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15285L));

  public static final NodeId HistoryUpdateResult_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15286L));

  public static final NodeId HistoryUpdateRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15287L));

  public static final NodeId HistoryUpdateResponse_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15288L));

  public static final NodeId CallMethodRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15289L));

  public static final NodeId CallMethodResult_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15290L));

  public static final NodeId CallRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15291L));

  public static final NodeId CallResponse_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15292L));

  public static final NodeId MonitoringFilter_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15293L));

  public static final NodeId DataChangeFilter_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15294L));

  public static final NodeId EventFilter_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15295L));

  public static final NodeId HasDataSetWriter = new NodeId(UShort.MIN, uint(15296L));

  public static final NodeId HasDataSetReader = new NodeId(UShort.MIN, uint(15297L));

  public static final NodeId DataSetWriterType = new NodeId(UShort.MIN, uint(15298L));

  public static final NodeId DataSetWriterType_Status = new NodeId(UShort.MIN, uint(15299L));

  public static final NodeId DataSetWriterType_Status_State = new NodeId(UShort.MIN, uint(15300L));

  public static final NodeId DataSetWriterType_Status_Enable = new NodeId(UShort.MIN, uint(15301L));

  public static final NodeId DataSetWriterType_Status_Disable =
      new NodeId(UShort.MIN, uint(15302L));

  public static final NodeId DataSetWriterType_TransportSettings =
      new NodeId(UShort.MIN, uint(15303L));

  public static final NodeId AggregateConfiguration_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15304L));

  public static final NodeId DataSetWriterTransportType = new NodeId(UShort.MIN, uint(15305L));

  public static final NodeId DataSetReaderType = new NodeId(UShort.MIN, uint(15306L));

  public static final NodeId DataSetReaderType_Status = new NodeId(UShort.MIN, uint(15307L));

  public static final NodeId DataSetReaderType_Status_State = new NodeId(UShort.MIN, uint(15308L));

  public static final NodeId DataSetReaderType_Status_Enable = new NodeId(UShort.MIN, uint(15309L));

  public static final NodeId DataSetReaderType_Status_Disable =
      new NodeId(UShort.MIN, uint(15310L));

  public static final NodeId DataSetReaderType_TransportSettings =
      new NodeId(UShort.MIN, uint(15311L));

  public static final NodeId AggregateFilter_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15312L));

  public static final NodeId MonitoringFilterResult_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15313L));

  public static final NodeId EventFilterResult_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15314L));

  public static final NodeId AggregateFilterResult_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15315L));

  public static final NodeId DataSetReaderType_SubscribedDataSet =
      new NodeId(UShort.MIN, uint(15316L));

  public static final NodeId ElseGuardVariableType = new NodeId(UShort.MIN, uint(15317L));

  public static final NodeId BaseAnalogType = new NodeId(UShort.MIN, uint(15318L));

  public static final NodeId DataSetReaderTransportType = new NodeId(UShort.MIN, uint(15319L));

  public static final NodeId MonitoringParameters_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15320L));

  public static final NodeId MonitoredItemCreateRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15321L));

  public static final NodeId MonitoredItemCreateResult_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15322L));

  public static final NodeId CreateMonitoredItemsRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15323L));

  public static final NodeId CreateMonitoredItemsResponse_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15324L));

  public static final NodeId MonitoredItemModifyRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15325L));

  public static final NodeId MonitoredItemModifyResult_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15326L));

  public static final NodeId ModifyMonitoredItemsRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15327L));

  public static final NodeId ModifyMonitoredItemsResponse_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15328L));

  public static final NodeId SetMonitoringModeRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15329L));

  public static final NodeId BrokerDataSetWriterTransportType_RequestedDeliveryGuarantee =
      new NodeId(UShort.MIN, uint(15330L));

  public static final NodeId SetMonitoringModeResponse_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15331L));

  public static final NodeId SetTriggeringRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15332L));

  public static final NodeId SetTriggeringResponse_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15333L));

  public static final NodeId BrokerDataSetReaderTransportType_ResourceUri =
      new NodeId(UShort.MIN, uint(15334L));

  public static final NodeId DeleteMonitoredItemsRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15335L));

  public static final NodeId DeleteMonitoredItemsResponse_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15336L));

  public static final NodeId CreateSubscriptionRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15337L));

  public static final NodeId CreateSubscriptionResponse_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15338L));

  public static final NodeId ModifySubscriptionRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15339L));

  public static final NodeId ModifySubscriptionResponse_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15340L));

  public static final NodeId SetPublishingModeRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15341L));

  public static final NodeId SetPublishingModeResponse_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15342L));

  public static final NodeId NotificationMessage_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15343L));

  public static final NodeId NotificationData_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15344L));

  public static final NodeId DataChangeNotification_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15345L));

  public static final NodeId MonitoredItemNotification_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15346L));

  public static final NodeId EventNotificationList_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15347L));

  public static final NodeId EventFieldList_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15348L));

  public static final NodeId HistoryEventFieldList_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15349L));

  public static final NodeId StatusChangeNotification_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15350L));

  public static final NodeId SubscriptionAcknowledgement_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15351L));

  public static final NodeId PublishRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15352L));

  public static final NodeId PublishResponse_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15353L));

  public static final NodeId RepublishRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15354L));

  public static final NodeId RepublishResponse_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15355L));

  public static final NodeId TransferResult_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15356L));

  public static final NodeId TransferSubscriptionsRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15357L));

  public static final NodeId TransferSubscriptionsResponse_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15358L));

  public static final NodeId DeleteSubscriptionsRequest_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15359L));

  public static final NodeId DeleteSubscriptionsResponse_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15360L));

  public static final NodeId BuildInfo_Encoding_DefaultJson = new NodeId(UShort.MIN, uint(15361L));

  public static final NodeId RedundantServerDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15362L));

  public static final NodeId EndpointUrlListDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15363L));

  public static final NodeId NetworkGroupDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15364L));

  public static final NodeId SamplingIntervalDiagnosticsDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15365L));

  public static final NodeId ServerDiagnosticsSummaryDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15366L));

  public static final NodeId ServerStatusDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15367L));

  public static final NodeId SessionDiagnosticsDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15368L));

  public static final NodeId SessionSecurityDiagnosticsDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15369L));

  public static final NodeId ServiceCounterDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15370L));

  public static final NodeId StatusResult_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15371L));

  public static final NodeId SubscriptionDiagnosticsDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15372L));

  public static final NodeId ModelChangeStructureDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15373L));

  public static final NodeId SemanticChangeStructureDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15374L));

  public static final NodeId Range_Encoding_DefaultJson = new NodeId(UShort.MIN, uint(15375L));

  public static final NodeId EUInformation_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15376L));

  public static final NodeId ComplexNumberType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15377L));

  public static final NodeId DoubleComplexNumberType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15378L));

  public static final NodeId AxisInformation_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15379L));

  public static final NodeId XVType_Encoding_DefaultJson = new NodeId(UShort.MIN, uint(15380L));

  public static final NodeId ProgramDiagnosticDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15381L));

  public static final NodeId Annotation_Encoding_DefaultJson = new NodeId(UShort.MIN, uint(15382L));

  public static final NodeId ProgramDiagnostic2Type = new NodeId(UShort.MIN, uint(15383L));

  public static final NodeId ProgramDiagnostic2Type_CreateSessionId =
      new NodeId(UShort.MIN, uint(15384L));

  public static final NodeId ProgramDiagnostic2Type_CreateClientName =
      new NodeId(UShort.MIN, uint(15385L));

  public static final NodeId ProgramDiagnostic2Type_InvocationCreationTime =
      new NodeId(UShort.MIN, uint(15386L));

  public static final NodeId ProgramDiagnostic2Type_LastTransitionTime =
      new NodeId(UShort.MIN, uint(15387L));

  public static final NodeId ProgramDiagnostic2Type_LastMethodCall =
      new NodeId(UShort.MIN, uint(15388L));

  public static final NodeId ProgramDiagnostic2Type_LastMethodSessionId =
      new NodeId(UShort.MIN, uint(15389L));

  public static final NodeId ProgramDiagnostic2Type_LastMethodInputArguments =
      new NodeId(UShort.MIN, uint(15390L));

  public static final NodeId ProgramDiagnostic2Type_LastMethodOutputArguments =
      new NodeId(UShort.MIN, uint(15391L));

  public static final NodeId ProgramDiagnostic2Type_LastMethodInputValues =
      new NodeId(UShort.MIN, uint(15392L));

  public static final NodeId ProgramDiagnostic2Type_LastMethodOutputValues =
      new NodeId(UShort.MIN, uint(15393L));

  public static final NodeId ProgramDiagnostic2Type_LastMethodCallTime =
      new NodeId(UShort.MIN, uint(15394L));

  public static final NodeId ProgramDiagnostic2Type_LastMethodReturnStatus =
      new NodeId(UShort.MIN, uint(15395L));

  public static final NodeId AccessLevelExType = new NodeId(UShort.MIN, uint(15406L));

  public static final NodeId AccessLevelExType_OptionSetValues =
      new NodeId(UShort.MIN, uint(15407L));

  public static final NodeId RoleSetType_RoleName_Placeholder_ApplicationsExclude =
      new NodeId(UShort.MIN, uint(15408L));

  public static final NodeId RoleSetType_RoleName_Placeholder_EndpointsExclude =
      new NodeId(UShort.MIN, uint(15409L));

  public static final NodeId RoleType_ApplicationsExclude = new NodeId(UShort.MIN, uint(15410L));

  public static final NodeId RoleType_EndpointsExclude = new NodeId(UShort.MIN, uint(15411L));

  public static final NodeId WellKnownRole_Anonymous_ApplicationsExclude =
      new NodeId(UShort.MIN, uint(15412L));

  public static final NodeId WellKnownRole_Anonymous_EndpointsExclude =
      new NodeId(UShort.MIN, uint(15413L));

  public static final NodeId WellKnownRole_AuthenticatedUser_ApplicationsExclude =
      new NodeId(UShort.MIN, uint(15414L));

  public static final NodeId WellKnownRole_AuthenticatedUser_EndpointsExclude =
      new NodeId(UShort.MIN, uint(15415L));

  public static final NodeId WellKnownRole_Observer_ApplicationsExclude =
      new NodeId(UShort.MIN, uint(15416L));

  public static final NodeId WellKnownRole_Observer_EndpointsExclude =
      new NodeId(UShort.MIN, uint(15417L));

  public static final NodeId WellKnownRole_Operator_ApplicationsExclude =
      new NodeId(UShort.MIN, uint(15418L));

  public static final NodeId BrokerDataSetReaderTransportType_AuthenticationProfileUri =
      new NodeId(UShort.MIN, uint(15419L));

  public static final NodeId BrokerDataSetReaderTransportType_RequestedDeliveryGuarantee =
      new NodeId(UShort.MIN, uint(15420L));

  public static final NodeId SimpleTypeDescription_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(15421L));

  public static final NodeId UABinaryFileDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(15422L));

  public static final NodeId WellKnownRole_Operator_EndpointsExclude =
      new NodeId(UShort.MIN, uint(15423L));

  public static final NodeId WellKnownRole_Engineer_ApplicationsExclude =
      new NodeId(UShort.MIN, uint(15424L));

  public static final NodeId WellKnownRole_Engineer_EndpointsExclude =
      new NodeId(UShort.MIN, uint(15425L));

  public static final NodeId WellKnownRole_Supervisor_ApplicationsExclude =
      new NodeId(UShort.MIN, uint(15426L));

  public static final NodeId WellKnownRole_Supervisor_EndpointsExclude =
      new NodeId(UShort.MIN, uint(15427L));

  public static final NodeId WellKnownRole_ConfigureAdmin_ApplicationsExclude =
      new NodeId(UShort.MIN, uint(15428L));

  public static final NodeId WellKnownRole_ConfigureAdmin_EndpointsExclude =
      new NodeId(UShort.MIN, uint(15429L));

  public static final NodeId WellKnownRole_SecurityAdmin_ApplicationsExclude =
      new NodeId(UShort.MIN, uint(15430L));

  public static final NodeId BaseConfigurationDataType = new NodeId(UShort.MIN, uint(15434L));

  public static final NodeId BaseConfigurationRecordDataType = new NodeId(UShort.MIN, uint(15435L));

  public static final NodeId CertificateGroupDataType = new NodeId(UShort.MIN, uint(15436L));

  public static final NodeId ConfigurationFileType = new NodeId(UShort.MIN, uint(15437L));

  public static final NodeId ConfigurationFileType_LastUpdateTime =
      new NodeId(UShort.MIN, uint(15438L));

  public static final NodeId ConfigurationFileType_CurrentVersion =
      new NodeId(UShort.MIN, uint(15439L));

  public static final NodeId PublishSubscribe_GetSecurityGroup =
      new NodeId(UShort.MIN, uint(15440L));

  public static final NodeId PublishSubscribe_GetSecurityGroup_InputArguments =
      new NodeId(UShort.MIN, uint(15441L));

  public static final NodeId PublishSubscribe_GetSecurityGroup_OutputArguments =
      new NodeId(UShort.MIN, uint(15442L));

  public static final NodeId PublishSubscribe_SecurityGroups = new NodeId(UShort.MIN, uint(15443L));

  public static final NodeId PublishSubscribe_SecurityGroups_AddSecurityGroup =
      new NodeId(UShort.MIN, uint(15444L));

  public static final NodeId PublishSubscribe_SecurityGroups_AddSecurityGroup_InputArguments =
      new NodeId(UShort.MIN, uint(15445L));

  public static final NodeId PublishSubscribe_SecurityGroups_AddSecurityGroup_OutputArguments =
      new NodeId(UShort.MIN, uint(15446L));

  public static final NodeId PublishSubscribe_SecurityGroups_RemoveSecurityGroup =
      new NodeId(UShort.MIN, uint(15447L));

  public static final NodeId PublishSubscribe_SecurityGroups_RemoveSecurityGroup_InputArguments =
      new NodeId(UShort.MIN, uint(15448L));

  public static final NodeId GetSecurityGroupMethodType = new NodeId(UShort.MIN, uint(15449L));

  public static final NodeId GetSecurityGroupMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(15450L));

  public static final NodeId GetSecurityGroupMethodType_OutputArguments =
      new NodeId(UShort.MIN, uint(15451L));

  public static final NodeId SecurityGroupFolderType = new NodeId(UShort.MIN, uint(15452L));

  public static final NodeId SecurityGroupFolderType_SecurityGroupFolderName_Placeholder =
      new NodeId(UShort.MIN, uint(15453L));

  public static final NodeId
      SecurityGroupFolderType_SecurityGroupFolderName_Placeholder_AddSecurityGroup =
          new NodeId(UShort.MIN, uint(15454L));

  public static final NodeId
      SecurityGroupFolderType_SecurityGroupFolderName_Placeholder_AddSecurityGroup_InputArguments =
          new NodeId(UShort.MIN, uint(15455L));

  public static final NodeId
      SecurityGroupFolderType_SecurityGroupFolderName_Placeholder_AddSecurityGroup_OutputArguments =
          new NodeId(UShort.MIN, uint(15456L));

  public static final NodeId
      SecurityGroupFolderType_SecurityGroupFolderName_Placeholder_RemoveSecurityGroup =
          new NodeId(UShort.MIN, uint(15457L));

  public static final NodeId
      SecurityGroupFolderType_SecurityGroupFolderName_Placeholder_RemoveSecurityGroup_InputArguments =
          new NodeId(UShort.MIN, uint(15458L));

  public static final NodeId SecurityGroupFolderType_SecurityGroupName_Placeholder =
      new NodeId(UShort.MIN, uint(15459L));

  public static final NodeId SecurityGroupFolderType_SecurityGroupName_Placeholder_SecurityGroupId =
      new NodeId(UShort.MIN, uint(15460L));

  public static final NodeId SecurityGroupFolderType_AddSecurityGroup =
      new NodeId(UShort.MIN, uint(15461L));

  public static final NodeId SecurityGroupFolderType_AddSecurityGroup_InputArguments =
      new NodeId(UShort.MIN, uint(15462L));

  public static final NodeId SecurityGroupFolderType_AddSecurityGroup_OutputArguments =
      new NodeId(UShort.MIN, uint(15463L));

  public static final NodeId SecurityGroupFolderType_RemoveSecurityGroup =
      new NodeId(UShort.MIN, uint(15464L));

  public static final NodeId SecurityGroupFolderType_RemoveSecurityGroup_InputArguments =
      new NodeId(UShort.MIN, uint(15465L));

  public static final NodeId AddSecurityGroupMethodType = new NodeId(UShort.MIN, uint(15466L));

  public static final NodeId AddSecurityGroupMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(15467L));

  public static final NodeId AddSecurityGroupMethodType_OutputArguments =
      new NodeId(UShort.MIN, uint(15468L));

  public static final NodeId RemoveSecurityGroupMethodType = new NodeId(UShort.MIN, uint(15469L));

  public static final NodeId RemoveSecurityGroupMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(15470L));

  public static final NodeId SecurityGroupType = new NodeId(UShort.MIN, uint(15471L));

  public static final NodeId SecurityGroupType_SecurityGroupId =
      new NodeId(UShort.MIN, uint(15472L));

  public static final NodeId DataSetFolderType_PublishedDataSetName_Placeholder_ExtensionFields =
      new NodeId(UShort.MIN, uint(15473L));

  public static final NodeId
      DataSetFolderType_PublishedDataSetName_Placeholder_ExtensionFields_AddExtensionField =
          new NodeId(UShort.MIN, uint(15474L));

  public static final NodeId
      DataSetFolderType_PublishedDataSetName_Placeholder_ExtensionFields_AddExtensionField_InputArguments =
          new NodeId(UShort.MIN, uint(15475L));

  public static final NodeId
      DataSetFolderType_PublishedDataSetName_Placeholder_ExtensionFields_AddExtensionField_OutputArguments =
          new NodeId(UShort.MIN, uint(15476L));

  public static final NodeId
      DataSetFolderType_PublishedDataSetName_Placeholder_ExtensionFields_RemoveExtensionField =
          new NodeId(UShort.MIN, uint(15477L));

  public static final NodeId
      DataSetFolderType_PublishedDataSetName_Placeholder_ExtensionFields_RemoveExtensionField_InputArguments =
          new NodeId(UShort.MIN, uint(15478L));

  public static final NodeId BrokerConnectionTransportDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(15479L));

  public static final NodeId WriterGroupDataType = new NodeId(UShort.MIN, uint(15480L));

  public static final NodeId PublishedDataSetType_ExtensionFields =
      new NodeId(UShort.MIN, uint(15481L));

  public static final NodeId PublishedDataSetType_ExtensionFields_AddExtensionField =
      new NodeId(UShort.MIN, uint(15482L));

  public static final NodeId PublishedDataSetType_ExtensionFields_AddExtensionField_InputArguments =
      new NodeId(UShort.MIN, uint(15483L));

  public static final NodeId
      PublishedDataSetType_ExtensionFields_AddExtensionField_OutputArguments =
          new NodeId(UShort.MIN, uint(15484L));

  public static final NodeId PublishedDataSetType_ExtensionFields_RemoveExtensionField =
      new NodeId(UShort.MIN, uint(15485L));

  public static final NodeId
      PublishedDataSetType_ExtensionFields_RemoveExtensionField_InputArguments =
          new NodeId(UShort.MIN, uint(15486L));

  public static final NodeId StructureDescription = new NodeId(UShort.MIN, uint(15487L));

  public static final NodeId EnumDescription = new NodeId(UShort.MIN, uint(15488L));

  public static final NodeId ExtensionFieldsType = new NodeId(UShort.MIN, uint(15489L));

  public static final NodeId ExtensionFieldsType_ExtensionFieldName_Placeholder =
      new NodeId(UShort.MIN, uint(15490L));

  public static final NodeId ExtensionFieldsType_AddExtensionField =
      new NodeId(UShort.MIN, uint(15491L));

  public static final NodeId ExtensionFieldsType_AddExtensionField_InputArguments =
      new NodeId(UShort.MIN, uint(15492L));

  public static final NodeId ExtensionFieldsType_AddExtensionField_OutputArguments =
      new NodeId(UShort.MIN, uint(15493L));

  public static final NodeId ExtensionFieldsType_RemoveExtensionField =
      new NodeId(UShort.MIN, uint(15494L));

  public static final NodeId ExtensionFieldsType_RemoveExtensionField_InputArguments =
      new NodeId(UShort.MIN, uint(15495L));

  public static final NodeId AddExtensionFieldMethodType = new NodeId(UShort.MIN, uint(15496L));

  public static final NodeId AddExtensionFieldMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(15497L));

  public static final NodeId AddExtensionFieldMethodType_OutputArguments =
      new NodeId(UShort.MIN, uint(15498L));

  public static final NodeId RemoveExtensionFieldMethodType = new NodeId(UShort.MIN, uint(15499L));

  public static final NodeId RemoveExtensionFieldMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(15500L));

  public static final NodeId OpcUa_BinarySchema_SimpleTypeDescription =
      new NodeId(UShort.MIN, uint(15501L));

  public static final NodeId NetworkAddressDataType = new NodeId(UShort.MIN, uint(15502L));

  public static final NodeId ConfigurationFileType_ActivityTimeout =
      new NodeId(UShort.MIN, uint(15503L));

  public static final NodeId ConfigurationFileType_SupportedDataType =
      new NodeId(UShort.MIN, uint(15504L));

  public static final NodeId ConfigurationFileType_CloseAndUpdate =
      new NodeId(UShort.MIN, uint(15505L));

  public static final NodeId ConfigurationFileType_CloseAndUpdate_InputArguments =
      new NodeId(UShort.MIN, uint(15506L));

  public static final NodeId ConfigurationFileType_CloseAndUpdate_OutputArguments =
      new NodeId(UShort.MIN, uint(15507L));

  public static final NodeId ConfigurationFileType_ConfirmUpdate =
      new NodeId(UShort.MIN, uint(15508L));

  public static final NodeId OpcUa_BinarySchema_SimpleTypeDescription_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15509L));

  public static final NodeId NetworkAddressUrlDataType = new NodeId(UShort.MIN, uint(15510L));

  public static final NodeId ConfigurationFileType_ConfirmUpdate_InputArguments =
      new NodeId(UShort.MIN, uint(15511L));

  public static final NodeId ConfigurationFileCloseAndUpdateMethodType =
      new NodeId(UShort.MIN, uint(15513L));

  public static final NodeId ConfigurationFileCloseAndUpdateMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(15514L));

  public static final NodeId ConfigurationFileCloseAndUpdateMethodType_OutputArguments =
      new NodeId(UShort.MIN, uint(15515L));

  public static final NodeId ConfigurationFileConfirmUpdateMethodType =
      new NodeId(UShort.MIN, uint(15516L));

  public static final NodeId PublishedEventsType_ModifyFieldSelection_OutputArguments =
      new NodeId(UShort.MIN, uint(15517L));

  public static final NodeId PublishedEventsTypeModifyFieldSelectionMethodType_OutputArguments =
      new NodeId(UShort.MIN, uint(15518L));

  public static final NodeId OpcUa_BinarySchema_SimpleTypeDescription_DictionaryFragment =
      new NodeId(UShort.MIN, uint(15519L));

  public static final NodeId ReaderGroupDataType = new NodeId(UShort.MIN, uint(15520L));

  public static final NodeId OpcUa_BinarySchema_UABinaryFileDataType =
      new NodeId(UShort.MIN, uint(15521L));

  public static final NodeId OpcUa_BinarySchema_UABinaryFileDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15522L));

  public static final NodeId OpcUa_BinarySchema_UABinaryFileDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(15523L));

  public static final NodeId OpcUa_BinarySchema_BrokerConnectionTransportDataType =
      new NodeId(UShort.MIN, uint(15524L));

  public static final NodeId OpcUa_BinarySchema_BrokerConnectionTransportDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15525L));

  public static final NodeId
      OpcUa_BinarySchema_BrokerConnectionTransportDataType_DictionaryFragment =
          new NodeId(UShort.MIN, uint(15526L));

  public static final NodeId WellKnownRole_SecurityAdmin_EndpointsExclude =
      new NodeId(UShort.MIN, uint(15527L));

  public static final NodeId EndpointType = new NodeId(UShort.MIN, uint(15528L));

  public static final NodeId SimpleTypeDescription_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(15529L));

  public static final NodeId PubSubConfigurationDataType = new NodeId(UShort.MIN, uint(15530L));

  public static final NodeId UABinaryFileDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(15531L));

  public static final NodeId DatagramWriterGroupTransportDataType =
      new NodeId(UShort.MIN, uint(15532L));

  public static final NodeId
      PublishSubscribeType_ConnectionName_Placeholder_Address_NetworkInterface =
          new NodeId(UShort.MIN, uint(15533L));

  public static final NodeId DataTypeSchemaHeader = new NodeId(UShort.MIN, uint(15534L));

  public static final NodeId PubSubStatusEventType = new NodeId(UShort.MIN, uint(15535L));

  public static final NodeId ConfigurationFileConfirmUpdateMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(15536L));

  public static final NodeId ConfigurationUpdateTargetType = new NodeId(UShort.MIN, uint(15538L));

  public static final NodeId ConfigurationUpdateType = new NodeId(UShort.MIN, uint(15539L));

  public static final NodeId ConfigurationUpdateType_EnumValues =
      new NodeId(UShort.MIN, uint(15540L));

  public static final NodeId ConfigurationUpdatedAuditEventType =
      new NodeId(UShort.MIN, uint(15541L));

  public static final NodeId ConfigurationUpdatedAuditEventType_OldVersion =
      new NodeId(UShort.MIN, uint(15542L));

  public static final NodeId ConfigurationUpdatedAuditEventType_NewVersion =
      new NodeId(UShort.MIN, uint(15543L));

  public static final NodeId PubSubStatusEventType_ConnectionId =
      new NodeId(UShort.MIN, uint(15545L));

  public static final NodeId PubSubStatusEventType_GroupId = new NodeId(UShort.MIN, uint(15546L));

  public static final NodeId PubSubStatusEventType_State = new NodeId(UShort.MIN, uint(15547L));

  public static final NodeId PubSubTransportLimitsExceedEventType =
      new NodeId(UShort.MIN, uint(15548L));

  public static final NodeId ApplicationConfigurationFileType =
      new NodeId(UShort.MIN, uint(15550L));

  public static final NodeId ApplicationConfigurationFileType_AvailableNetworks =
      new NodeId(UShort.MIN, uint(15551L));

  public static final NodeId ApplicationConfigurationFileType_AvailablePorts =
      new NodeId(UShort.MIN, uint(15552L));

  public static final NodeId ApplicationConfigurationFileType_SecurityPolicyUris =
      new NodeId(UShort.MIN, uint(15553L));

  public static final NodeId ApplicationConfigurationFileType_UserTokenTypes =
      new NodeId(UShort.MIN, uint(15554L));

  public static final NodeId ApplicationConfigurationFileType_CertificateTypes =
      new NodeId(UShort.MIN, uint(15555L));

  public static final NodeId ApplicationIdentityDataType = new NodeId(UShort.MIN, uint(15556L));

  public static final NodeId EndpointDataType = new NodeId(UShort.MIN, uint(15557L));

  public static final NodeId ServerEndpointDataType = new NodeId(UShort.MIN, uint(15558L));

  public static final NodeId SecuritySettingsDataType = new NodeId(UShort.MIN, uint(15559L));

  public static final NodeId UserTokenSettingsDataType = new NodeId(UShort.MIN, uint(15560L));

  public static final NodeId PubSubTransportLimitsExceedEventType_Actual =
      new NodeId(UShort.MIN, uint(15561L));

  public static final NodeId PubSubTransportLimitsExceedEventType_Maximum =
      new NodeId(UShort.MIN, uint(15562L));

  public static final NodeId PubSubCommunicationFailureEventType =
      new NodeId(UShort.MIN, uint(15563L));

  public static final NodeId ServerConfigurationType_ConfigurationFile =
      new NodeId(UShort.MIN, uint(15564L));

  public static final NodeId ServerConfigurationType_ConfigurationFile_Size =
      new NodeId(UShort.MIN, uint(15565L));

  public static final NodeId ServerConfigurationType_ConfigurationFile_Writable =
      new NodeId(UShort.MIN, uint(15566L));

  public static final NodeId ServerConfigurationType_ConfigurationFile_UserWritable =
      new NodeId(UShort.MIN, uint(15567L));

  public static final NodeId ServerConfigurationType_ConfigurationFile_OpenCount =
      new NodeId(UShort.MIN, uint(15568L));

  public static final NodeId ServerConfigurationType_ConfigurationFile_MimeType =
      new NodeId(UShort.MIN, uint(15569L));

  public static final NodeId ServerConfigurationType_ConfigurationFile_MaxByteStringLength =
      new NodeId(UShort.MIN, uint(15570L));

  public static final NodeId ServerConfigurationType_ConfigurationFile_LastModifiedTime =
      new NodeId(UShort.MIN, uint(15571L));

  public static final NodeId ServerConfigurationType_ConfigurationFile_Open =
      new NodeId(UShort.MIN, uint(15572L));

  public static final NodeId ServerConfigurationType_ConfigurationFile_Open_InputArguments =
      new NodeId(UShort.MIN, uint(15573L));

  public static final NodeId ServerConfigurationType_ConfigurationFile_Open_OutputArguments =
      new NodeId(UShort.MIN, uint(15574L));

  public static final NodeId ServerConfigurationType_ConfigurationFile_Close =
      new NodeId(UShort.MIN, uint(15575L));

  public static final NodeId PubSubCommunicationFailureEventType_Error =
      new NodeId(UShort.MIN, uint(15576L));

  public static final NodeId DataSetFieldFlags_OptionSetValues =
      new NodeId(UShort.MIN, uint(15577L));

  public static final NodeId PublishedDataSetDataType = new NodeId(UShort.MIN, uint(15578L));

  public static final NodeId BrokerConnectionTransportDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(15579L));

  public static final NodeId PublishedDataSetSourceDataType = new NodeId(UShort.MIN, uint(15580L));

  public static final NodeId PublishedDataItemsDataType = new NodeId(UShort.MIN, uint(15581L));

  public static final NodeId PublishedEventsDataType = new NodeId(UShort.MIN, uint(15582L));

  public static final NodeId DataSetFieldContentMask = new NodeId(UShort.MIN, uint(15583L));

  public static final NodeId DataSetFieldContentMask_OptionSetValues =
      new NodeId(UShort.MIN, uint(15584L));

  public static final NodeId OpcUa_XmlSchema_SimpleTypeDescription =
      new NodeId(UShort.MIN, uint(15585L));

  public static final NodeId OpcUa_XmlSchema_SimpleTypeDescription_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15586L));

  public static final NodeId OpcUa_XmlSchema_SimpleTypeDescription_DictionaryFragment =
      new NodeId(UShort.MIN, uint(15587L));

  public static final NodeId OpcUa_XmlSchema_UABinaryFileDataType =
      new NodeId(UShort.MIN, uint(15588L));

  public static final NodeId StructureDescription_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(15589L));

  public static final NodeId EnumDescription_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(15590L));

  public static final NodeId OpcUa_XmlSchema_StructureDescription =
      new NodeId(UShort.MIN, uint(15591L));

  public static final NodeId OpcUa_XmlSchema_StructureDescription_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15592L));

  public static final NodeId OpcUa_XmlSchema_StructureDescription_DictionaryFragment =
      new NodeId(UShort.MIN, uint(15593L));

  public static final NodeId OpcUa_XmlSchema_EnumDescription = new NodeId(UShort.MIN, uint(15594L));

  public static final NodeId OpcUa_XmlSchema_EnumDescription_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15595L));

  public static final NodeId OpcUa_XmlSchema_EnumDescription_DictionaryFragment =
      new NodeId(UShort.MIN, uint(15596L));

  public static final NodeId DataSetWriterDataType = new NodeId(UShort.MIN, uint(15597L));

  public static final NodeId DataSetWriterTransportDataType = new NodeId(UShort.MIN, uint(15598L));

  public static final NodeId OpcUa_BinarySchema_StructureDescription =
      new NodeId(UShort.MIN, uint(15599L));

  public static final NodeId OpcUa_BinarySchema_StructureDescription_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15600L));

  public static final NodeId OpcUa_BinarySchema_StructureDescription_DictionaryFragment =
      new NodeId(UShort.MIN, uint(15601L));

  public static final NodeId OpcUa_BinarySchema_EnumDescription =
      new NodeId(UShort.MIN, uint(15602L));

  public static final NodeId OpcUa_BinarySchema_EnumDescription_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15603L));

  public static final NodeId OpcUa_BinarySchema_EnumDescription_DictionaryFragment =
      new NodeId(UShort.MIN, uint(15604L));

  public static final NodeId DataSetWriterMessageDataType = new NodeId(UShort.MIN, uint(15605L));

  public static final NodeId Server_ServerCapabilities_RoleSet =
      new NodeId(UShort.MIN, uint(15606L));

  public static final NodeId RoleSetType = new NodeId(UShort.MIN, uint(15607L));

  public static final NodeId RoleSetType_RoleName_Placeholder =
      new NodeId(UShort.MIN, uint(15608L));

  public static final NodeId PubSubGroupDataType = new NodeId(UShort.MIN, uint(15609L));

  public static final NodeId OpcUa_XmlSchema_UABinaryFileDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15610L));

  public static final NodeId WriterGroupTransportDataType = new NodeId(UShort.MIN, uint(15611L));

  public static final NodeId RoleSetType_RoleName_Placeholder_AddIdentity =
      new NodeId(UShort.MIN, uint(15612L));

  public static final NodeId RoleSetType_RoleName_Placeholder_AddIdentity_InputArguments =
      new NodeId(UShort.MIN, uint(15613L));

  public static final NodeId RoleSetType_RoleName_Placeholder_RemoveIdentity =
      new NodeId(UShort.MIN, uint(15614L));

  public static final NodeId RoleSetType_RoleName_Placeholder_RemoveIdentity_InputArguments =
      new NodeId(UShort.MIN, uint(15615L));

  public static final NodeId WriterGroupMessageDataType = new NodeId(UShort.MIN, uint(15616L));

  public static final NodeId PubSubConnectionDataType = new NodeId(UShort.MIN, uint(15617L));

  public static final NodeId ConnectionTransportDataType = new NodeId(UShort.MIN, uint(15618L));

  public static final NodeId OpcUa_XmlSchema_UABinaryFileDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(15619L));

  public static final NodeId RoleType = new NodeId(UShort.MIN, uint(15620L));

  public static final NodeId ReaderGroupTransportDataType = new NodeId(UShort.MIN, uint(15621L));

  public static final NodeId ReaderGroupMessageDataType = new NodeId(UShort.MIN, uint(15622L));

  public static final NodeId DataSetReaderDataType = new NodeId(UShort.MIN, uint(15623L));

  public static final NodeId RoleType_AddIdentity = new NodeId(UShort.MIN, uint(15624L));

  public static final NodeId RoleType_AddIdentity_InputArguments =
      new NodeId(UShort.MIN, uint(15625L));

  public static final NodeId RoleType_RemoveIdentity = new NodeId(UShort.MIN, uint(15626L));

  public static final NodeId RoleType_RemoveIdentity_InputArguments =
      new NodeId(UShort.MIN, uint(15627L));

  public static final NodeId DataSetReaderTransportDataType = new NodeId(UShort.MIN, uint(15628L));

  public static final NodeId DataSetReaderMessageDataType = new NodeId(UShort.MIN, uint(15629L));

  public static final NodeId SubscribedDataSetDataType = new NodeId(UShort.MIN, uint(15630L));

  public static final NodeId TargetVariablesDataType = new NodeId(UShort.MIN, uint(15631L));

  public static final NodeId IdentityCriteriaType = new NodeId(UShort.MIN, uint(15632L));

  public static final NodeId IdentityCriteriaType_EnumValues = new NodeId(UShort.MIN, uint(15633L));

  public static final NodeId IdentityMappingRuleType = new NodeId(UShort.MIN, uint(15634L));

  public static final NodeId SubscribedDataSetMirrorDataType = new NodeId(UShort.MIN, uint(15635L));

  public static final NodeId AddIdentityMethodType = new NodeId(UShort.MIN, uint(15636L));

  public static final NodeId AddIdentityMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(15637L));

  public static final NodeId RemoveIdentityMethodType = new NodeId(UShort.MIN, uint(15638L));

  public static final NodeId RemoveIdentityMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(15639L));

  public static final NodeId OpcUa_XmlSchema_BrokerConnectionTransportDataType =
      new NodeId(UShort.MIN, uint(15640L));

  public static final NodeId DataSetOrderingType_EnumStrings = new NodeId(UShort.MIN, uint(15641L));

  public static final NodeId UadpNetworkMessageContentMask = new NodeId(UShort.MIN, uint(15642L));

  public static final NodeId UadpNetworkMessageContentMask_OptionSetValues =
      new NodeId(UShort.MIN, uint(15643L));

  public static final NodeId WellKnownRole_Anonymous = new NodeId(UShort.MIN, uint(15644L));

  public static final NodeId UadpWriterGroupMessageDataType = new NodeId(UShort.MIN, uint(15645L));

  public static final NodeId UadpDataSetMessageContentMask = new NodeId(UShort.MIN, uint(15646L));

  public static final NodeId UadpDataSetMessageContentMask_OptionSetValues =
      new NodeId(UShort.MIN, uint(15647L));

  public static final NodeId WellKnownRole_Anonymous_AddIdentity =
      new NodeId(UShort.MIN, uint(15648L));

  public static final NodeId WellKnownRole_Anonymous_AddIdentity_InputArguments =
      new NodeId(UShort.MIN, uint(15649L));

  public static final NodeId WellKnownRole_Anonymous_RemoveIdentity =
      new NodeId(UShort.MIN, uint(15650L));

  public static final NodeId WellKnownRole_Anonymous_RemoveIdentity_InputArguments =
      new NodeId(UShort.MIN, uint(15651L));

  public static final NodeId UadpDataSetWriterMessageDataType =
      new NodeId(UShort.MIN, uint(15652L));

  public static final NodeId UadpDataSetReaderMessageDataType =
      new NodeId(UShort.MIN, uint(15653L));

  public static final NodeId JsonNetworkMessageContentMask = new NodeId(UShort.MIN, uint(15654L));

  public static final NodeId JsonNetworkMessageContentMask_OptionSetValues =
      new NodeId(UShort.MIN, uint(15655L));

  public static final NodeId WellKnownRole_AuthenticatedUser = new NodeId(UShort.MIN, uint(15656L));

  public static final NodeId JsonWriterGroupMessageDataType = new NodeId(UShort.MIN, uint(15657L));

  public static final NodeId JsonDataSetMessageContentMask = new NodeId(UShort.MIN, uint(15658L));

  public static final NodeId JsonDataSetMessageContentMask_OptionSetValues =
      new NodeId(UShort.MIN, uint(15659L));

  public static final NodeId WellKnownRole_AuthenticatedUser_AddIdentity =
      new NodeId(UShort.MIN, uint(15660L));

  public static final NodeId WellKnownRole_AuthenticatedUser_AddIdentity_InputArguments =
      new NodeId(UShort.MIN, uint(15661L));

  public static final NodeId WellKnownRole_AuthenticatedUser_RemoveIdentity =
      new NodeId(UShort.MIN, uint(15662L));

  public static final NodeId WellKnownRole_AuthenticatedUser_RemoveIdentity_InputArguments =
      new NodeId(UShort.MIN, uint(15663L));

  public static final NodeId JsonDataSetWriterMessageDataType =
      new NodeId(UShort.MIN, uint(15664L));

  public static final NodeId JsonDataSetReaderMessageDataType =
      new NodeId(UShort.MIN, uint(15665L));

  public static final NodeId OpcUa_XmlSchema_BrokerConnectionTransportDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15666L));

  public static final NodeId BrokerWriterGroupTransportDataType =
      new NodeId(UShort.MIN, uint(15667L));

  public static final NodeId WellKnownRole_Observer = new NodeId(UShort.MIN, uint(15668L));

  public static final NodeId BrokerDataSetWriterTransportDataType =
      new NodeId(UShort.MIN, uint(15669L));

  public static final NodeId BrokerDataSetReaderTransportDataType =
      new NodeId(UShort.MIN, uint(15670L));

  public static final NodeId EndpointType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(15671L));

  public static final NodeId WellKnownRole_Observer_AddIdentity =
      new NodeId(UShort.MIN, uint(15672L));

  public static final NodeId WellKnownRole_Observer_AddIdentity_InputArguments =
      new NodeId(UShort.MIN, uint(15673L));

  public static final NodeId WellKnownRole_Observer_RemoveIdentity =
      new NodeId(UShort.MIN, uint(15674L));

  public static final NodeId WellKnownRole_Observer_RemoveIdentity_InputArguments =
      new NodeId(UShort.MIN, uint(15675L));

  public static final NodeId DataTypeSchemaHeader_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(15676L));

  public static final NodeId PublishedDataSetDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(15677L));

  public static final NodeId PublishedDataSetSourceDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(15678L));

  public static final NodeId PublishedDataItemsDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(15679L));

  public static final NodeId WellKnownRole_Operator = new NodeId(UShort.MIN, uint(15680L));

  public static final NodeId PublishedEventsDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(15681L));

  public static final NodeId DataSetWriterDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(15682L));

  public static final NodeId DataSetWriterTransportDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(15683L));

  public static final NodeId WellKnownRole_Operator_AddIdentity =
      new NodeId(UShort.MIN, uint(15684L));

  public static final NodeId WellKnownRole_Operator_AddIdentity_InputArguments =
      new NodeId(UShort.MIN, uint(15685L));

  public static final NodeId WellKnownRole_Operator_RemoveIdentity =
      new NodeId(UShort.MIN, uint(15686L));

  public static final NodeId WellKnownRole_Operator_RemoveIdentity_InputArguments =
      new NodeId(UShort.MIN, uint(15687L));

  public static final NodeId DataSetWriterMessageDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(15688L));

  public static final NodeId PubSubGroupDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(15689L));

  public static final NodeId OpcUa_XmlSchema_BrokerConnectionTransportDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(15690L));

  public static final NodeId WriterGroupTransportDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(15691L));

  public static final NodeId WellKnownRole_Supervisor = new NodeId(UShort.MIN, uint(15692L));

  public static final NodeId WriterGroupMessageDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(15693L));

  public static final NodeId PubSubConnectionDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(15694L));

  public static final NodeId ConnectionTransportDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(15695L));

  public static final NodeId WellKnownRole_Supervisor_AddIdentity =
      new NodeId(UShort.MIN, uint(15696L));

  public static final NodeId WellKnownRole_Supervisor_AddIdentity_InputArguments =
      new NodeId(UShort.MIN, uint(15697L));

  public static final NodeId WellKnownRole_Supervisor_RemoveIdentity =
      new NodeId(UShort.MIN, uint(15698L));

  public static final NodeId WellKnownRole_Supervisor_RemoveIdentity_InputArguments =
      new NodeId(UShort.MIN, uint(15699L));

  public static final NodeId SimpleTypeDescription_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15700L));

  public static final NodeId ReaderGroupTransportDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(15701L));

  public static final NodeId ReaderGroupMessageDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(15702L));

  public static final NodeId DataSetReaderDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(15703L));

  public static final NodeId WellKnownRole_SecurityAdmin = new NodeId(UShort.MIN, uint(15704L));

  public static final NodeId DataSetReaderTransportDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(15705L));

  public static final NodeId DataSetReaderMessageDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(15706L));

  public static final NodeId SubscribedDataSetDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(15707L));

  public static final NodeId WellKnownRole_SecurityAdmin_AddIdentity =
      new NodeId(UShort.MIN, uint(15708L));

  public static final NodeId WellKnownRole_SecurityAdmin_AddIdentity_InputArguments =
      new NodeId(UShort.MIN, uint(15709L));

  public static final NodeId WellKnownRole_SecurityAdmin_RemoveIdentity =
      new NodeId(UShort.MIN, uint(15710L));

  public static final NodeId WellKnownRole_SecurityAdmin_RemoveIdentity_InputArguments =
      new NodeId(UShort.MIN, uint(15711L));

  public static final NodeId TargetVariablesDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(15712L));

  public static final NodeId SubscribedDataSetMirrorDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(15713L));

  public static final NodeId UABinaryFileDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15714L));

  public static final NodeId UadpWriterGroupMessageDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(15715L));

  public static final NodeId WellKnownRole_ConfigureAdmin = new NodeId(UShort.MIN, uint(15716L));

  public static final NodeId UadpDataSetWriterMessageDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(15717L));

  public static final NodeId UadpDataSetReaderMessageDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(15718L));

  public static final NodeId JsonWriterGroupMessageDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(15719L));

  public static final NodeId WellKnownRole_ConfigureAdmin_AddIdentity =
      new NodeId(UShort.MIN, uint(15720L));

  public static final NodeId WellKnownRole_ConfigureAdmin_AddIdentity_InputArguments =
      new NodeId(UShort.MIN, uint(15721L));

  public static final NodeId WellKnownRole_ConfigureAdmin_RemoveIdentity =
      new NodeId(UShort.MIN, uint(15722L));

  public static final NodeId WellKnownRole_ConfigureAdmin_RemoveIdentity_InputArguments =
      new NodeId(UShort.MIN, uint(15723L));

  public static final NodeId JsonDataSetWriterMessageDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(15724L));

  public static final NodeId JsonDataSetReaderMessageDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(15725L));

  public static final NodeId BrokerConnectionTransportDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(15726L));

  public static final NodeId BrokerWriterGroupTransportDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(15727L));

  public static final NodeId IdentityMappingRuleType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(15728L));

  public static final NodeId BrokerDataSetWriterTransportDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(15729L));

  public static final NodeId OpcUa_XmlSchema_IdentityMappingRuleType =
      new NodeId(UShort.MIN, uint(15730L));

  public static final NodeId OpcUa_XmlSchema_IdentityMappingRuleType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15731L));

  public static final NodeId OpcUa_XmlSchema_IdentityMappingRuleType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(15732L));

  public static final NodeId BrokerDataSetReaderTransportDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(15733L));

  public static final NodeId OpcUa_BinarySchema_EndpointType = new NodeId(UShort.MIN, uint(15734L));

  public static final NodeId OpcUa_BinarySchema_EndpointType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15735L));

  public static final NodeId IdentityMappingRuleType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(15736L));

  public static final NodeId OpcUa_BinarySchema_EndpointType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(15737L));

  public static final NodeId OpcUa_BinarySchema_IdentityMappingRuleType =
      new NodeId(UShort.MIN, uint(15738L));

  public static final NodeId OpcUa_BinarySchema_IdentityMappingRuleType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15739L));

  public static final NodeId OpcUa_BinarySchema_IdentityMappingRuleType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(15740L));

  public static final NodeId OpcUa_BinarySchema_DataTypeSchemaHeader =
      new NodeId(UShort.MIN, uint(15741L));

  public static final NodeId OpcUa_BinarySchema_DataTypeSchemaHeader_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15742L));

  public static final NodeId OpcUa_BinarySchema_DataTypeSchemaHeader_DictionaryFragment =
      new NodeId(UShort.MIN, uint(15743L));

  public static final NodeId TemporaryFileTransferType = new NodeId(UShort.MIN, uint(15744L));

  public static final NodeId TemporaryFileTransferType_ClientProcessingTimeout =
      new NodeId(UShort.MIN, uint(15745L));

  public static final NodeId TemporaryFileTransferType_GenerateFileForRead =
      new NodeId(UShort.MIN, uint(15746L));

  public static final NodeId TemporaryFileTransferType_GenerateFileForRead_InputArguments =
      new NodeId(UShort.MIN, uint(15747L));

  public static final NodeId TemporaryFileTransferType_GenerateFileForRead_OutputArguments =
      new NodeId(UShort.MIN, uint(15748L));

  public static final NodeId TemporaryFileTransferType_GenerateFileForWrite =
      new NodeId(UShort.MIN, uint(15749L));

  public static final NodeId TemporaryFileTransferType_GenerateFileForWrite_OutputArguments =
      new NodeId(UShort.MIN, uint(15750L));

  public static final NodeId TemporaryFileTransferType_CloseAndCommit =
      new NodeId(UShort.MIN, uint(15751L));

  public static final NodeId TemporaryFileTransferType_CloseAndCommit_InputArguments =
      new NodeId(UShort.MIN, uint(15752L));

  public static final NodeId TemporaryFileTransferType_CloseAndCommit_OutputArguments =
      new NodeId(UShort.MIN, uint(15753L));

  public static final NodeId TemporaryFileTransferType_TransferState_Placeholder =
      new NodeId(UShort.MIN, uint(15754L));

  public static final NodeId TemporaryFileTransferType_TransferState_Placeholder_CurrentState =
      new NodeId(UShort.MIN, uint(15755L));

  public static final NodeId TemporaryFileTransferType_TransferState_Placeholder_CurrentState_Id =
      new NodeId(UShort.MIN, uint(15756L));

  public static final NodeId TemporaryFileTransferType_TransferState_Placeholder_CurrentState_Name =
      new NodeId(UShort.MIN, uint(15757L));

  public static final NodeId
      TemporaryFileTransferType_TransferState_Placeholder_CurrentState_Number =
          new NodeId(UShort.MIN, uint(15758L));

  public static final NodeId
      TemporaryFileTransferType_TransferState_Placeholder_CurrentState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(15759L));

  public static final NodeId TemporaryFileTransferType_TransferState_Placeholder_LastTransition =
      new NodeId(UShort.MIN, uint(15760L));

  public static final NodeId TemporaryFileTransferType_TransferState_Placeholder_LastTransition_Id =
      new NodeId(UShort.MIN, uint(15761L));

  public static final NodeId
      TemporaryFileTransferType_TransferState_Placeholder_LastTransition_Name =
          new NodeId(UShort.MIN, uint(15762L));

  public static final NodeId
      TemporaryFileTransferType_TransferState_Placeholder_LastTransition_Number =
          new NodeId(UShort.MIN, uint(15763L));

  public static final NodeId
      TemporaryFileTransferType_TransferState_Placeholder_LastTransition_TransitionTime =
          new NodeId(UShort.MIN, uint(15764L));

  public static final NodeId
      TemporaryFileTransferType_TransferState_Placeholder_LastTransition_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(15765L));

  public static final NodeId OpcUa_BinarySchema_PublishedDataSetDataType =
      new NodeId(UShort.MIN, uint(15766L));

  public static final NodeId OpcUa_BinarySchema_PublishedDataSetDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15767L));

  public static final NodeId OpcUa_BinarySchema_PublishedDataSetDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(15768L));

  public static final NodeId OpcUa_BinarySchema_PublishedDataSetSourceDataType =
      new NodeId(UShort.MIN, uint(15769L));

  public static final NodeId OpcUa_BinarySchema_PublishedDataSetSourceDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15770L));

  public static final NodeId OpcUa_BinarySchema_PublishedDataSetSourceDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(15771L));

  public static final NodeId OpcUa_BinarySchema_PublishedDataItemsDataType =
      new NodeId(UShort.MIN, uint(15772L));

  public static final NodeId OpcUa_BinarySchema_PublishedDataItemsDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15773L));

  public static final NodeId OpcUa_BinarySchema_PublishedDataItemsDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(15774L));

  public static final NodeId OpcUa_BinarySchema_PublishedEventsDataType =
      new NodeId(UShort.MIN, uint(15775L));

  public static final NodeId OpcUa_BinarySchema_PublishedEventsDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15776L));

  public static final NodeId OpcUa_BinarySchema_PublishedEventsDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(15777L));

  public static final NodeId OpcUa_BinarySchema_DataSetWriterDataType =
      new NodeId(UShort.MIN, uint(15778L));

  public static final NodeId OpcUa_BinarySchema_DataSetWriterDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15779L));

  public static final NodeId OpcUa_BinarySchema_DataSetWriterDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(15780L));

  public static final NodeId OpcUa_BinarySchema_DataSetWriterTransportDataType =
      new NodeId(UShort.MIN, uint(15781L));

  public static final NodeId OpcUa_BinarySchema_DataSetWriterTransportDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15782L));

  public static final NodeId OpcUa_BinarySchema_DataSetWriterTransportDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(15783L));

  public static final NodeId OpcUa_BinarySchema_DataSetWriterMessageDataType =
      new NodeId(UShort.MIN, uint(15784L));

  public static final NodeId OpcUa_BinarySchema_DataSetWriterMessageDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15785L));

  public static final NodeId OpcUa_BinarySchema_DataSetWriterMessageDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(15786L));

  public static final NodeId OpcUa_BinarySchema_PubSubGroupDataType =
      new NodeId(UShort.MIN, uint(15787L));

  public static final NodeId OpcUa_BinarySchema_PubSubGroupDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15788L));

  public static final NodeId OpcUa_BinarySchema_PubSubGroupDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(15789L));

  public static final NodeId ServerConfigurationType_ConfigurationFile_Close_InputArguments =
      new NodeId(UShort.MIN, uint(15790L));

  public static final NodeId ServerConfigurationType_ConfigurationFile_Read =
      new NodeId(UShort.MIN, uint(15791L));

  public static final NodeId ServerConfigurationType_ConfigurationFile_Read_InputArguments =
      new NodeId(UShort.MIN, uint(15792L));

  public static final NodeId OpcUa_BinarySchema_WriterGroupTransportDataType =
      new NodeId(UShort.MIN, uint(15793L));

  public static final NodeId TemporaryFileTransferType_TransferState_Placeholder_Reset =
      new NodeId(UShort.MIN, uint(15794L));

  public static final NodeId GenerateFileForReadMethodType = new NodeId(UShort.MIN, uint(15795L));

  public static final NodeId GenerateFileForReadMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(15796L));

  public static final NodeId GenerateFileForReadMethodType_OutputArguments =
      new NodeId(UShort.MIN, uint(15797L));

  public static final NodeId GenerateFileForWriteMethodType = new NodeId(UShort.MIN, uint(15798L));

  public static final NodeId GenerateFileForWriteMethodType_OutputArguments =
      new NodeId(UShort.MIN, uint(15799L));

  public static final NodeId CloseAndCommitMethodType = new NodeId(UShort.MIN, uint(15800L));

  public static final NodeId CloseAndCommitMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(15801L));

  public static final NodeId CloseAndCommitMethodType_OutputArguments =
      new NodeId(UShort.MIN, uint(15802L));

  public static final NodeId FileTransferStateMachineType = new NodeId(UShort.MIN, uint(15803L));

  public static final NodeId ServerConfigurationType_ConfigurationFile_Read_OutputArguments =
      new NodeId(UShort.MIN, uint(15804L));

  public static final NodeId ServerConfigurationType_ConfigurationFile_Write =
      new NodeId(UShort.MIN, uint(15805L));

  public static final NodeId ServerConfigurationType_ConfigurationFile_Write_InputArguments =
      new NodeId(UShort.MIN, uint(15806L));

  public static final NodeId ServerConfigurationType_ConfigurationFile_GetPosition =
      new NodeId(UShort.MIN, uint(15807L));

  public static final NodeId ServerConfigurationType_ConfigurationFile_GetPosition_InputArguments =
      new NodeId(UShort.MIN, uint(15808L));

  public static final NodeId ServerConfigurationType_ConfigurationFile_GetPosition_OutputArguments =
      new NodeId(UShort.MIN, uint(15809L));

  public static final NodeId ServerConfigurationType_ConfigurationFile_SetPosition =
      new NodeId(UShort.MIN, uint(15810L));

  public static final NodeId ServerConfigurationType_ConfigurationFile_SetPosition_InputArguments =
      new NodeId(UShort.MIN, uint(15811L));

  public static final NodeId ServerConfigurationType_ConfigurationFile_LastUpdateTime =
      new NodeId(UShort.MIN, uint(15812L));

  public static final NodeId ServerConfigurationType_ConfigurationFile_CurrentVersion =
      new NodeId(UShort.MIN, uint(15813L));

  public static final NodeId ServerConfigurationType_ConfigurationFile_ActivityTimeout =
      new NodeId(UShort.MIN, uint(15814L));

  public static final NodeId FileTransferStateMachineType_Idle =
      new NodeId(UShort.MIN, uint(15815L));

  public static final NodeId FileTransferStateMachineType_Idle_StateNumber =
      new NodeId(UShort.MIN, uint(15816L));

  public static final NodeId FileTransferStateMachineType_ReadPrepare =
      new NodeId(UShort.MIN, uint(15817L));

  public static final NodeId FileTransferStateMachineType_ReadPrepare_StateNumber =
      new NodeId(UShort.MIN, uint(15818L));

  public static final NodeId FileTransferStateMachineType_ReadTransfer =
      new NodeId(UShort.MIN, uint(15819L));

  public static final NodeId FileTransferStateMachineType_ReadTransfer_StateNumber =
      new NodeId(UShort.MIN, uint(15820L));

  public static final NodeId FileTransferStateMachineType_ApplyWrite =
      new NodeId(UShort.MIN, uint(15821L));

  public static final NodeId FileTransferStateMachineType_ApplyWrite_StateNumber =
      new NodeId(UShort.MIN, uint(15822L));

  public static final NodeId FileTransferStateMachineType_Error =
      new NodeId(UShort.MIN, uint(15823L));

  public static final NodeId FileTransferStateMachineType_Error_StateNumber =
      new NodeId(UShort.MIN, uint(15824L));

  public static final NodeId FileTransferStateMachineType_IdleToReadPrepare =
      new NodeId(UShort.MIN, uint(15825L));

  public static final NodeId FileTransferStateMachineType_IdleToReadPrepare_TransitionNumber =
      new NodeId(UShort.MIN, uint(15826L));

  public static final NodeId FileTransferStateMachineType_ReadPrepareToReadTransfer =
      new NodeId(UShort.MIN, uint(15827L));

  public static final NodeId
      FileTransferStateMachineType_ReadPrepareToReadTransfer_TransitionNumber =
          new NodeId(UShort.MIN, uint(15828L));

  public static final NodeId FileTransferStateMachineType_ReadTransferToIdle =
      new NodeId(UShort.MIN, uint(15829L));

  public static final NodeId FileTransferStateMachineType_ReadTransferToIdle_TransitionNumber =
      new NodeId(UShort.MIN, uint(15830L));

  public static final NodeId FileTransferStateMachineType_IdleToApplyWrite =
      new NodeId(UShort.MIN, uint(15831L));

  public static final NodeId FileTransferStateMachineType_IdleToApplyWrite_TransitionNumber =
      new NodeId(UShort.MIN, uint(15832L));

  public static final NodeId FileTransferStateMachineType_ApplyWriteToIdle =
      new NodeId(UShort.MIN, uint(15833L));

  public static final NodeId FileTransferStateMachineType_ApplyWriteToIdle_TransitionNumber =
      new NodeId(UShort.MIN, uint(15834L));

  public static final NodeId FileTransferStateMachineType_ReadPrepareToError =
      new NodeId(UShort.MIN, uint(15835L));

  public static final NodeId FileTransferStateMachineType_ReadPrepareToError_TransitionNumber =
      new NodeId(UShort.MIN, uint(15836L));

  public static final NodeId FileTransferStateMachineType_ReadTransferToError =
      new NodeId(UShort.MIN, uint(15837L));

  public static final NodeId FileTransferStateMachineType_ReadTransferToError_TransitionNumber =
      new NodeId(UShort.MIN, uint(15838L));

  public static final NodeId FileTransferStateMachineType_ApplyWriteToError =
      new NodeId(UShort.MIN, uint(15839L));

  public static final NodeId FileTransferStateMachineType_ApplyWriteToError_TransitionNumber =
      new NodeId(UShort.MIN, uint(15840L));

  public static final NodeId FileTransferStateMachineType_ErrorToIdle =
      new NodeId(UShort.MIN, uint(15841L));

  public static final NodeId FileTransferStateMachineType_ErrorToIdle_TransitionNumber =
      new NodeId(UShort.MIN, uint(15842L));

  public static final NodeId FileTransferStateMachineType_Reset =
      new NodeId(UShort.MIN, uint(15843L));

  public static final NodeId PublishSubscribeType_Status = new NodeId(UShort.MIN, uint(15844L));

  public static final NodeId PublishSubscribeType_Status_State =
      new NodeId(UShort.MIN, uint(15845L));

  public static final NodeId PublishSubscribeType_Status_Enable =
      new NodeId(UShort.MIN, uint(15846L));

  public static final NodeId PublishSubscribeType_Status_Disable =
      new NodeId(UShort.MIN, uint(15847L));

  public static final NodeId ServerConfigurationType_ConfigurationFile_SupportedDataType =
      new NodeId(UShort.MIN, uint(15848L));

  public static final NodeId ServerConfigurationType_ConfigurationFile_CloseAndUpdate =
      new NodeId(UShort.MIN, uint(15849L));

  public static final NodeId
      ServerConfigurationType_ConfigurationFile_CloseAndUpdate_InputArguments =
          new NodeId(UShort.MIN, uint(15850L));

  public static final NodeId
      ServerConfigurationType_ConfigurationFile_CloseAndUpdate_OutputArguments =
          new NodeId(UShort.MIN, uint(15851L));

  public static final NodeId OpcUa_BinarySchema_WriterGroupTransportDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15852L));

  public static final NodeId OpcUa_BinarySchema_WriterGroupTransportDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(15853L));

  public static final NodeId OpcUa_BinarySchema_WriterGroupMessageDataType =
      new NodeId(UShort.MIN, uint(15854L));

  public static final NodeId OpcUa_BinarySchema_WriterGroupMessageDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15855L));

  public static final NodeId OpcUa_BinarySchema_WriterGroupMessageDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(15856L));

  public static final NodeId OpcUa_BinarySchema_PubSubConnectionDataType =
      new NodeId(UShort.MIN, uint(15857L));

  public static final NodeId OpcUa_BinarySchema_PubSubConnectionDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15858L));

  public static final NodeId OpcUa_BinarySchema_PubSubConnectionDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(15859L));

  public static final NodeId OpcUa_BinarySchema_ConnectionTransportDataType =
      new NodeId(UShort.MIN, uint(15860L));

  public static final NodeId OpcUa_BinarySchema_ConnectionTransportDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15861L));

  public static final NodeId OpcUa_BinarySchema_ConnectionTransportDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(15862L));

  public static final NodeId ServerConfigurationType_ConfigurationFile_ConfirmUpdate =
      new NodeId(UShort.MIN, uint(15863L));

  public static final NodeId
      ServerConfigurationType_ConfigurationFile_ConfirmUpdate_InputArguments =
          new NodeId(UShort.MIN, uint(15864L));

  public static final NodeId OpcUa_BinarySchema_ReaderGroupTransportDataType =
      new NodeId(UShort.MIN, uint(15866L));

  public static final NodeId OpcUa_BinarySchema_ReaderGroupTransportDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15867L));

  public static final NodeId OpcUa_BinarySchema_ReaderGroupTransportDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(15868L));

  public static final NodeId OpcUa_BinarySchema_ReaderGroupMessageDataType =
      new NodeId(UShort.MIN, uint(15869L));

  public static final NodeId OpcUa_BinarySchema_ReaderGroupMessageDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15870L));

  public static final NodeId OpcUa_BinarySchema_ReaderGroupMessageDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(15871L));

  public static final NodeId OpcUa_BinarySchema_DataSetReaderDataType =
      new NodeId(UShort.MIN, uint(15872L));

  public static final NodeId OpcUa_BinarySchema_DataSetReaderDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15873L));

  public static final NodeId OverrideValueHandling = new NodeId(UShort.MIN, uint(15874L));

  public static final NodeId OverrideValueHandling_EnumStrings =
      new NodeId(UShort.MIN, uint(15875L));

  public static final NodeId OpcUa_BinarySchema_DataSetReaderDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(15876L));

  public static final NodeId OpcUa_BinarySchema_DataSetReaderTransportDataType =
      new NodeId(UShort.MIN, uint(15877L));

  public static final NodeId OpcUa_BinarySchema_DataSetReaderTransportDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15878L));

  public static final NodeId OpcUa_BinarySchema_DataSetReaderTransportDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(15879L));

  public static final NodeId OpcUa_BinarySchema_DataSetReaderMessageDataType =
      new NodeId(UShort.MIN, uint(15880L));

  public static final NodeId OpcUa_BinarySchema_DataSetReaderMessageDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15881L));

  public static final NodeId OpcUa_BinarySchema_DataSetReaderMessageDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(15882L));

  public static final NodeId OpcUa_BinarySchema_SubscribedDataSetDataType =
      new NodeId(UShort.MIN, uint(15883L));

  public static final NodeId OpcUa_BinarySchema_SubscribedDataSetDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15884L));

  public static final NodeId OpcUa_BinarySchema_SubscribedDataSetDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(15885L));

  public static final NodeId OpcUa_BinarySchema_TargetVariablesDataType =
      new NodeId(UShort.MIN, uint(15886L));

  public static final NodeId OpcUa_BinarySchema_TargetVariablesDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15887L));

  public static final NodeId OpcUa_BinarySchema_TargetVariablesDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(15888L));

  public static final NodeId OpcUa_BinarySchema_SubscribedDataSetMirrorDataType =
      new NodeId(UShort.MIN, uint(15889L));

  public static final NodeId OpcUa_BinarySchema_SubscribedDataSetMirrorDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15890L));

  public static final NodeId OpcUa_BinarySchema_SubscribedDataSetMirrorDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(15891L));

  public static final NodeId ServerConfiguration_ConfigurationFile =
      new NodeId(UShort.MIN, uint(15892L));

  public static final NodeId ServerConfiguration_ConfigurationFile_Size =
      new NodeId(UShort.MIN, uint(15893L));

  public static final NodeId ServerConfiguration_ConfigurationFile_Writable =
      new NodeId(UShort.MIN, uint(15894L));

  public static final NodeId OpcUa_BinarySchema_UadpWriterGroupMessageDataType =
      new NodeId(UShort.MIN, uint(15895L));

  public static final NodeId OpcUa_BinarySchema_UadpWriterGroupMessageDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15896L));

  public static final NodeId OpcUa_BinarySchema_UadpWriterGroupMessageDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(15897L));

  public static final NodeId OpcUa_BinarySchema_UadpDataSetWriterMessageDataType =
      new NodeId(UShort.MIN, uint(15898L));

  public static final NodeId OpcUa_BinarySchema_UadpDataSetWriterMessageDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15899L));

  public static final NodeId
      OpcUa_BinarySchema_UadpDataSetWriterMessageDataType_DictionaryFragment =
          new NodeId(UShort.MIN, uint(15900L));

  public static final NodeId SessionlessInvokeRequestType = new NodeId(UShort.MIN, uint(15901L));

  public static final NodeId SessionlessInvokeRequestType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(15902L));

  public static final NodeId SessionlessInvokeRequestType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(15903L));

  public static final NodeId DataSetFieldFlags = new NodeId(UShort.MIN, uint(15904L));

  public static final NodeId PublishSubscribeType_ConnectionName_Placeholder_TransportSettings =
      new NodeId(UShort.MIN, uint(15905L));

  public static final NodeId PubSubKeyServiceType = new NodeId(UShort.MIN, uint(15906L));

  public static final NodeId PubSubKeyServiceType_GetSecurityKeys =
      new NodeId(UShort.MIN, uint(15907L));

  public static final NodeId PubSubKeyServiceType_GetSecurityKeys_InputArguments =
      new NodeId(UShort.MIN, uint(15908L));

  public static final NodeId PubSubKeyServiceType_GetSecurityKeys_OutputArguments =
      new NodeId(UShort.MIN, uint(15909L));

  public static final NodeId PubSubKeyServiceType_GetSecurityGroup =
      new NodeId(UShort.MIN, uint(15910L));

  public static final NodeId PubSubKeyServiceType_GetSecurityGroup_InputArguments =
      new NodeId(UShort.MIN, uint(15911L));

  public static final NodeId PubSubKeyServiceType_GetSecurityGroup_OutputArguments =
      new NodeId(UShort.MIN, uint(15912L));

  public static final NodeId PubSubKeyServiceType_SecurityGroups =
      new NodeId(UShort.MIN, uint(15913L));

  public static final NodeId PubSubKeyServiceType_SecurityGroups_AddSecurityGroup =
      new NodeId(UShort.MIN, uint(15914L));

  public static final NodeId PubSubKeyServiceType_SecurityGroups_AddSecurityGroup_InputArguments =
      new NodeId(UShort.MIN, uint(15915L));

  public static final NodeId PubSubKeyServiceType_SecurityGroups_AddSecurityGroup_OutputArguments =
      new NodeId(UShort.MIN, uint(15916L));

  public static final NodeId PubSubKeyServiceType_SecurityGroups_RemoveSecurityGroup =
      new NodeId(UShort.MIN, uint(15917L));

  public static final NodeId
      PubSubKeyServiceType_SecurityGroups_RemoveSecurityGroup_InputArguments =
          new NodeId(UShort.MIN, uint(15918L));

  public static final NodeId OpcUa_BinarySchema_UadpDataSetReaderMessageDataType =
      new NodeId(UShort.MIN, uint(15919L));

  public static final NodeId OpcUa_BinarySchema_UadpDataSetReaderMessageDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15920L));

  public static final NodeId
      OpcUa_BinarySchema_UadpDataSetReaderMessageDataType_DictionaryFragment =
          new NodeId(UShort.MIN, uint(15921L));

  public static final NodeId OpcUa_BinarySchema_JsonWriterGroupMessageDataType =
      new NodeId(UShort.MIN, uint(15922L));

  public static final NodeId OpcUa_BinarySchema_JsonWriterGroupMessageDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15923L));

  public static final NodeId OpcUa_BinarySchema_JsonWriterGroupMessageDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(15924L));

  public static final NodeId OpcUa_BinarySchema_JsonDataSetWriterMessageDataType =
      new NodeId(UShort.MIN, uint(15925L));

  public static final NodeId PubSubGroupType_SecurityMode = new NodeId(UShort.MIN, uint(15926L));

  public static final NodeId PubSubGroupType_SecurityGroupId = new NodeId(UShort.MIN, uint(15927L));

  public static final NodeId PubSubGroupType_SecurityKeyServices =
      new NodeId(UShort.MIN, uint(15928L));

  public static final NodeId OpcUa_BinarySchema_JsonDataSetWriterMessageDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15929L));

  public static final NodeId
      OpcUa_BinarySchema_JsonDataSetWriterMessageDataType_DictionaryFragment =
          new NodeId(UShort.MIN, uint(15930L));

  public static final NodeId OpcUa_BinarySchema_JsonDataSetReaderMessageDataType =
      new NodeId(UShort.MIN, uint(15931L));

  public static final NodeId DataSetReaderType_SecurityMode = new NodeId(UShort.MIN, uint(15932L));

  public static final NodeId DataSetReaderType_SecurityGroupId =
      new NodeId(UShort.MIN, uint(15933L));

  public static final NodeId DataSetReaderType_SecurityKeyServices =
      new NodeId(UShort.MIN, uint(15934L));

  public static final NodeId OpcUa_BinarySchema_JsonDataSetReaderMessageDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15935L));

  public static final NodeId
      OpcUa_BinarySchema_JsonDataSetReaderMessageDataType_DictionaryFragment =
          new NodeId(UShort.MIN, uint(15936L));

  public static final NodeId ServerConfiguration_ConfigurationFile_UserWritable =
      new NodeId(UShort.MIN, uint(15937L));

  public static final NodeId ServerConfiguration_ConfigurationFile_OpenCount =
      new NodeId(UShort.MIN, uint(15938L));

  public static final NodeId ServerConfiguration_ConfigurationFile_MimeType =
      new NodeId(UShort.MIN, uint(15939L));

  public static final NodeId OpcUa_BinarySchema_BrokerWriterGroupTransportDataType =
      new NodeId(UShort.MIN, uint(15940L));

  public static final NodeId OpcUa_BinarySchema_BrokerWriterGroupTransportDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(15941L));

  public static final NodeId
      OpcUa_BinarySchema_BrokerWriterGroupTransportDataType_DictionaryFragment =
          new NodeId(UShort.MIN, uint(15942L));

  public static final NodeId OpcUa_BinarySchema_BrokerDataSetWriterTransportDataType =
      new NodeId(UShort.MIN, uint(15943L));

  public static final NodeId
      OpcUa_BinarySchema_BrokerDataSetWriterTransportDataType_DataTypeVersion =
          new NodeId(UShort.MIN, uint(15944L));

  public static final NodeId
      OpcUa_BinarySchema_BrokerDataSetWriterTransportDataType_DictionaryFragment =
          new NodeId(UShort.MIN, uint(15945L));

  public static final NodeId OpcUa_BinarySchema_BrokerDataSetReaderTransportDataType =
      new NodeId(UShort.MIN, uint(15946L));

  public static final NodeId
      OpcUa_BinarySchema_BrokerDataSetReaderTransportDataType_DataTypeVersion =
          new NodeId(UShort.MIN, uint(15947L));

  public static final NodeId
      OpcUa_BinarySchema_BrokerDataSetReaderTransportDataType_DictionaryFragment =
          new NodeId(UShort.MIN, uint(15948L));

  public static final NodeId EndpointType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(15949L));

  public static final NodeId DataTypeSchemaHeader_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(15950L));

  public static final NodeId PublishedDataSetDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(15951L));

  public static final NodeId PublishedDataSetSourceDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(15952L));

  public static final NodeId PublishedDataItemsDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(15953L));

  public static final NodeId PublishedEventsDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(15954L));

  public static final NodeId DataSetWriterDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(15955L));

  public static final NodeId DataSetWriterTransportDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(15956L));

  public static final NodeId OPCUANamespaceMetadata = new NodeId(UShort.MIN, uint(15957L));

  public static final NodeId OPCUANamespaceMetadata_NamespaceUri =
      new NodeId(UShort.MIN, uint(15958L));

  public static final NodeId OPCUANamespaceMetadata_NamespaceVersion =
      new NodeId(UShort.MIN, uint(15959L));

  public static final NodeId OPCUANamespaceMetadata_NamespacePublicationDate =
      new NodeId(UShort.MIN, uint(15960L));

  public static final NodeId OPCUANamespaceMetadata_IsNamespaceSubset =
      new NodeId(UShort.MIN, uint(15961L));

  public static final NodeId OPCUANamespaceMetadata_StaticNodeIdTypes =
      new NodeId(UShort.MIN, uint(15962L));

  public static final NodeId OPCUANamespaceMetadata_StaticNumericNodeIdRange =
      new NodeId(UShort.MIN, uint(15963L));

  public static final NodeId OPCUANamespaceMetadata_StaticStringNodeIdPattern =
      new NodeId(UShort.MIN, uint(15964L));

  public static final NodeId OPCUANamespaceMetadata_NamespaceFile =
      new NodeId(UShort.MIN, uint(15965L));

  public static final NodeId OPCUANamespaceMetadata_NamespaceFile_Size =
      new NodeId(UShort.MIN, uint(15966L));

  public static final NodeId OPCUANamespaceMetadata_NamespaceFile_Writable =
      new NodeId(UShort.MIN, uint(15967L));

  public static final NodeId OPCUANamespaceMetadata_NamespaceFile_UserWritable =
      new NodeId(UShort.MIN, uint(15968L));

  public static final NodeId OPCUANamespaceMetadata_NamespaceFile_OpenCount =
      new NodeId(UShort.MIN, uint(15969L));

  public static final NodeId OPCUANamespaceMetadata_NamespaceFile_MimeType =
      new NodeId(UShort.MIN, uint(15970L));

  public static final NodeId OPCUANamespaceMetadata_NamespaceFile_Open =
      new NodeId(UShort.MIN, uint(15971L));

  public static final NodeId OPCUANamespaceMetadata_NamespaceFile_Open_InputArguments =
      new NodeId(UShort.MIN, uint(15972L));

  public static final NodeId OPCUANamespaceMetadata_NamespaceFile_Open_OutputArguments =
      new NodeId(UShort.MIN, uint(15973L));

  public static final NodeId OPCUANamespaceMetadata_NamespaceFile_Close =
      new NodeId(UShort.MIN, uint(15974L));

  public static final NodeId OPCUANamespaceMetadata_NamespaceFile_Close_InputArguments =
      new NodeId(UShort.MIN, uint(15975L));

  public static final NodeId OPCUANamespaceMetadata_NamespaceFile_Read =
      new NodeId(UShort.MIN, uint(15976L));

  public static final NodeId OPCUANamespaceMetadata_NamespaceFile_Read_InputArguments =
      new NodeId(UShort.MIN, uint(15977L));

  public static final NodeId OPCUANamespaceMetadata_NamespaceFile_Read_OutputArguments =
      new NodeId(UShort.MIN, uint(15978L));

  public static final NodeId OPCUANamespaceMetadata_NamespaceFile_Write =
      new NodeId(UShort.MIN, uint(15979L));

  public static final NodeId OPCUANamespaceMetadata_NamespaceFile_Write_InputArguments =
      new NodeId(UShort.MIN, uint(15980L));

  public static final NodeId OPCUANamespaceMetadata_NamespaceFile_GetPosition =
      new NodeId(UShort.MIN, uint(15981L));

  public static final NodeId OPCUANamespaceMetadata_NamespaceFile_GetPosition_InputArguments =
      new NodeId(UShort.MIN, uint(15982L));

  public static final NodeId OPCUANamespaceMetadata_NamespaceFile_GetPosition_OutputArguments =
      new NodeId(UShort.MIN, uint(15983L));

  public static final NodeId OPCUANamespaceMetadata_NamespaceFile_SetPosition =
      new NodeId(UShort.MIN, uint(15984L));

  public static final NodeId OPCUANamespaceMetadata_NamespaceFile_SetPosition_InputArguments =
      new NodeId(UShort.MIN, uint(15985L));

  public static final NodeId OPCUANamespaceMetadata_NamespaceFile_ExportNamespace =
      new NodeId(UShort.MIN, uint(15986L));

  public static final NodeId DataSetWriterMessageDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(15987L));

  public static final NodeId PubSubGroupDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(15988L));

  public static final NodeId ServerConfiguration_ConfigurationFile_MaxByteStringLength =
      new NodeId(UShort.MIN, uint(15989L));

  public static final NodeId WriterGroupTransportDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(15990L));

  public static final NodeId WriterGroupMessageDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(15991L));

  public static final NodeId PubSubConnectionDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(15992L));

  public static final NodeId ConnectionTransportDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(15993L));

  public static final NodeId ServerConfiguration_ConfigurationFile_LastModifiedTime =
      new NodeId(UShort.MIN, uint(15994L));

  public static final NodeId ReaderGroupTransportDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(15995L));

  public static final NodeId ReaderGroupMessageDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(15996L));

  public static final NodeId RoleSetType_AddRole = new NodeId(UShort.MIN, uint(15997L));

  public static final NodeId RoleSetType_AddRole_InputArguments =
      new NodeId(UShort.MIN, uint(15998L));

  public static final NodeId RoleSetType_AddRole_OutputArguments =
      new NodeId(UShort.MIN, uint(15999L));

  public static final NodeId RoleSetType_RemoveRole = new NodeId(UShort.MIN, uint(16000L));

  public static final NodeId RoleSetType_RemoveRole_InputArguments =
      new NodeId(UShort.MIN, uint(16001L));

  public static final NodeId AddRoleMethodType = new NodeId(UShort.MIN, uint(16002L));

  public static final NodeId AddRoleMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(16003L));

  public static final NodeId AddRoleMethodType_OutputArguments =
      new NodeId(UShort.MIN, uint(16004L));

  public static final NodeId RemoveRoleMethodType = new NodeId(UShort.MIN, uint(16005L));

  public static final NodeId RemoveRoleMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(16006L));

  public static final NodeId DataSetReaderDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(16007L));

  public static final NodeId DataSetReaderTransportDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(16008L));

  public static final NodeId DataSetReaderMessageDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(16009L));

  public static final NodeId SubscribedDataSetDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(16010L));

  public static final NodeId TargetVariablesDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(16011L));

  public static final NodeId SubscribedDataSetMirrorDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(16012L));

  public static final NodeId ServerConfiguration_ConfigurationFile_Open =
      new NodeId(UShort.MIN, uint(16013L));

  public static final NodeId UadpWriterGroupMessageDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(16014L));

  public static final NodeId UadpDataSetWriterMessageDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(16015L));

  public static final NodeId UadpDataSetReaderMessageDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(16016L));

  public static final NodeId JsonWriterGroupMessageDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(16017L));

  public static final NodeId JsonDataSetWriterMessageDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(16018L));

  public static final NodeId JsonDataSetReaderMessageDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(16019L));

  public static final NodeId ServerConfiguration_ConfigurationFile_Open_InputArguments =
      new NodeId(UShort.MIN, uint(16020L));

  public static final NodeId BrokerWriterGroupTransportDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(16021L));

  public static final NodeId BrokerDataSetWriterTransportDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(16022L));

  public static final NodeId BrokerDataSetReaderTransportDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(16023L));

  public static final NodeId OpcUa_XmlSchema_EndpointType = new NodeId(UShort.MIN, uint(16024L));

  public static final NodeId OpcUa_XmlSchema_EndpointType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16025L));

  public static final NodeId OpcUa_XmlSchema_EndpointType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16026L));

  public static final NodeId OpcUa_XmlSchema_DataTypeSchemaHeader =
      new NodeId(UShort.MIN, uint(16027L));

  public static final NodeId OpcUa_XmlSchema_DataTypeSchemaHeader_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16028L));

  public static final NodeId OpcUa_XmlSchema_DataTypeSchemaHeader_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16029L));

  public static final NodeId OpcUa_XmlSchema_PublishedDataSetDataType =
      new NodeId(UShort.MIN, uint(16030L));

  public static final NodeId OpcUa_XmlSchema_PublishedDataSetDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16031L));

  public static final NodeId OpcUa_XmlSchema_PublishedDataSetDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16032L));

  public static final NodeId OpcUa_XmlSchema_PublishedDataSetSourceDataType =
      new NodeId(UShort.MIN, uint(16033L));

  public static final NodeId OpcUa_XmlSchema_PublishedDataSetSourceDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16034L));

  public static final NodeId OpcUa_XmlSchema_PublishedDataSetSourceDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16035L));

  public static final NodeId WellKnownRole_Engineer = new NodeId(UShort.MIN, uint(16036L));

  public static final NodeId OpcUa_XmlSchema_PublishedDataItemsDataType =
      new NodeId(UShort.MIN, uint(16037L));

  public static final NodeId OpcUa_XmlSchema_PublishedDataItemsDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16038L));

  public static final NodeId OpcUa_XmlSchema_PublishedDataItemsDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16039L));

  public static final NodeId OpcUa_XmlSchema_PublishedEventsDataType =
      new NodeId(UShort.MIN, uint(16040L));

  public static final NodeId WellKnownRole_Engineer_AddIdentity =
      new NodeId(UShort.MIN, uint(16041L));

  public static final NodeId WellKnownRole_Engineer_AddIdentity_InputArguments =
      new NodeId(UShort.MIN, uint(16042L));

  public static final NodeId WellKnownRole_Engineer_RemoveIdentity =
      new NodeId(UShort.MIN, uint(16043L));

  public static final NodeId WellKnownRole_Engineer_RemoveIdentity_InputArguments =
      new NodeId(UShort.MIN, uint(16044L));

  public static final NodeId OpcUa_XmlSchema_PublishedEventsDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16045L));

  public static final NodeId OpcUa_XmlSchema_PublishedEventsDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16046L));

  public static final NodeId OpcUa_XmlSchema_DataSetWriterDataType =
      new NodeId(UShort.MIN, uint(16047L));

  public static final NodeId OpcUa_XmlSchema_DataSetWriterDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16048L));

  public static final NodeId OpcUa_XmlSchema_DataSetWriterDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16049L));

  public static final NodeId OpcUa_XmlSchema_DataSetWriterTransportDataType =
      new NodeId(UShort.MIN, uint(16050L));

  public static final NodeId OpcUa_XmlSchema_DataSetWriterTransportDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16051L));

  public static final NodeId OpcUa_XmlSchema_DataSetWriterTransportDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16052L));

  public static final NodeId OpcUa_XmlSchema_DataSetWriterMessageDataType =
      new NodeId(UShort.MIN, uint(16053L));

  public static final NodeId OpcUa_XmlSchema_DataSetWriterMessageDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16054L));

  public static final NodeId OpcUa_XmlSchema_DataSetWriterMessageDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16055L));

  public static final NodeId OpcUa_XmlSchema_PubSubGroupDataType =
      new NodeId(UShort.MIN, uint(16056L));

  public static final NodeId OpcUa_XmlSchema_PubSubGroupDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16057L));

  public static final NodeId OpcUa_XmlSchema_PubSubGroupDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16058L));

  public static final NodeId ServerConfiguration_ConfigurationFile_Open_OutputArguments =
      new NodeId(UShort.MIN, uint(16059L));

  public static final NodeId ServerConfiguration_ConfigurationFile_Close =
      new NodeId(UShort.MIN, uint(16060L));

  public static final NodeId ServerConfiguration_ConfigurationFile_Close_InputArguments =
      new NodeId(UShort.MIN, uint(16061L));

  public static final NodeId OpcUa_XmlSchema_WriterGroupTransportDataType =
      new NodeId(UShort.MIN, uint(16062L));

  public static final NodeId OpcUa_XmlSchema_WriterGroupTransportDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16063L));

  public static final NodeId OpcUa_XmlSchema_WriterGroupTransportDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16064L));

  public static final NodeId OpcUa_XmlSchema_WriterGroupMessageDataType =
      new NodeId(UShort.MIN, uint(16065L));

  public static final NodeId OpcUa_XmlSchema_WriterGroupMessageDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16066L));

  public static final NodeId OpcUa_XmlSchema_WriterGroupMessageDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16067L));

  public static final NodeId OpcUa_XmlSchema_PubSubConnectionDataType =
      new NodeId(UShort.MIN, uint(16068L));

  public static final NodeId OpcUa_XmlSchema_PubSubConnectionDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16069L));

  public static final NodeId OpcUa_XmlSchema_PubSubConnectionDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16070L));

  public static final NodeId OpcUa_XmlSchema_ConnectionTransportDataType =
      new NodeId(UShort.MIN, uint(16071L));

  public static final NodeId OpcUa_XmlSchema_ConnectionTransportDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16072L));

  public static final NodeId OpcUa_XmlSchema_ConnectionTransportDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16073L));

  public static final NodeId ServerConfiguration_ConfigurationFile_Read =
      new NodeId(UShort.MIN, uint(16074L));

  public static final NodeId ServerConfiguration_ConfigurationFile_Read_InputArguments =
      new NodeId(UShort.MIN, uint(16075L));

  public static final NodeId ServerConfiguration_ConfigurationFile_Read_OutputArguments =
      new NodeId(UShort.MIN, uint(16076L));

  public static final NodeId OpcUa_XmlSchema_ReaderGroupTransportDataType =
      new NodeId(UShort.MIN, uint(16077L));

  public static final NodeId OpcUa_XmlSchema_ReaderGroupTransportDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16078L));

  public static final NodeId OpcUa_XmlSchema_ReaderGroupTransportDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16079L));

  public static final NodeId OpcUa_XmlSchema_ReaderGroupMessageDataType =
      new NodeId(UShort.MIN, uint(16080L));

  public static final NodeId OpcUa_XmlSchema_ReaderGroupMessageDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16081L));

  public static final NodeId OpcUa_XmlSchema_ReaderGroupMessageDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16082L));

  public static final NodeId OpcUa_XmlSchema_DataSetReaderDataType =
      new NodeId(UShort.MIN, uint(16083L));

  public static final NodeId OpcUa_XmlSchema_DataSetReaderDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16084L));

  public static final NodeId OpcUa_XmlSchema_DataSetReaderDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16085L));

  public static final NodeId OpcUa_XmlSchema_DataSetReaderTransportDataType =
      new NodeId(UShort.MIN, uint(16086L));

  public static final NodeId OpcUa_XmlSchema_DataSetReaderTransportDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16087L));

  public static final NodeId OpcUa_XmlSchema_DataSetReaderTransportDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16088L));

  public static final NodeId OpcUa_XmlSchema_DataSetReaderMessageDataType =
      new NodeId(UShort.MIN, uint(16089L));

  public static final NodeId OpcUa_XmlSchema_DataSetReaderMessageDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16090L));

  public static final NodeId OpcUa_XmlSchema_DataSetReaderMessageDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16091L));

  public static final NodeId OpcUa_XmlSchema_SubscribedDataSetDataType =
      new NodeId(UShort.MIN, uint(16092L));

  public static final NodeId OpcUa_XmlSchema_SubscribedDataSetDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16093L));

  public static final NodeId OpcUa_XmlSchema_SubscribedDataSetDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16094L));

  public static final NodeId OpcUa_XmlSchema_TargetVariablesDataType =
      new NodeId(UShort.MIN, uint(16095L));

  public static final NodeId OpcUa_XmlSchema_TargetVariablesDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16096L));

  public static final NodeId OpcUa_XmlSchema_TargetVariablesDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16097L));

  public static final NodeId OpcUa_XmlSchema_SubscribedDataSetMirrorDataType =
      new NodeId(UShort.MIN, uint(16098L));

  public static final NodeId OpcUa_XmlSchema_SubscribedDataSetMirrorDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16099L));

  public static final NodeId OpcUa_XmlSchema_SubscribedDataSetMirrorDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16100L));

  public static final NodeId ServerConfiguration_ConfigurationFile_Write =
      new NodeId(UShort.MIN, uint(16101L));

  public static final NodeId ServerConfiguration_ConfigurationFile_Write_InputArguments =
      new NodeId(UShort.MIN, uint(16102L));

  public static final NodeId ServerConfiguration_ConfigurationFile_GetPosition =
      new NodeId(UShort.MIN, uint(16103L));

  public static final NodeId OpcUa_XmlSchema_UadpWriterGroupMessageDataType =
      new NodeId(UShort.MIN, uint(16104L));

  public static final NodeId OpcUa_XmlSchema_UadpWriterGroupMessageDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16105L));

  public static final NodeId OpcUa_XmlSchema_UadpWriterGroupMessageDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16106L));

  public static final NodeId OpcUa_XmlSchema_UadpDataSetWriterMessageDataType =
      new NodeId(UShort.MIN, uint(16107L));

  public static final NodeId OpcUa_XmlSchema_UadpDataSetWriterMessageDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16108L));

  public static final NodeId OpcUa_XmlSchema_UadpDataSetWriterMessageDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16109L));

  public static final NodeId OpcUa_XmlSchema_UadpDataSetReaderMessageDataType =
      new NodeId(UShort.MIN, uint(16110L));

  public static final NodeId OpcUa_XmlSchema_UadpDataSetReaderMessageDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16111L));

  public static final NodeId OpcUa_XmlSchema_UadpDataSetReaderMessageDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16112L));

  public static final NodeId OpcUa_XmlSchema_JsonWriterGroupMessageDataType =
      new NodeId(UShort.MIN, uint(16113L));

  public static final NodeId OpcUa_XmlSchema_JsonWriterGroupMessageDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16114L));

  public static final NodeId OpcUa_XmlSchema_JsonWriterGroupMessageDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16115L));

  public static final NodeId OpcUa_XmlSchema_JsonDataSetWriterMessageDataType =
      new NodeId(UShort.MIN, uint(16116L));

  public static final NodeId OpcUa_XmlSchema_JsonDataSetWriterMessageDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16117L));

  public static final NodeId OpcUa_XmlSchema_JsonDataSetWriterMessageDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16118L));

  public static final NodeId OpcUa_XmlSchema_JsonDataSetReaderMessageDataType =
      new NodeId(UShort.MIN, uint(16119L));

  public static final NodeId OpcUa_XmlSchema_JsonDataSetReaderMessageDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16120L));

  public static final NodeId OpcUa_XmlSchema_JsonDataSetReaderMessageDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16121L));

  public static final NodeId ServerConfiguration_ConfigurationFile_GetPosition_InputArguments =
      new NodeId(UShort.MIN, uint(16122L));

  public static final NodeId ServerConfiguration_ConfigurationFile_GetPosition_OutputArguments =
      new NodeId(UShort.MIN, uint(16123L));

  public static final NodeId ServerConfiguration_ConfigurationFile_SetPosition =
      new NodeId(UShort.MIN, uint(16124L));

  public static final NodeId OpcUa_XmlSchema_BrokerWriterGroupTransportDataType =
      new NodeId(UShort.MIN, uint(16125L));

  public static final NodeId RolePermissionType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(16126L));

  public static final NodeId OpcUa_XmlSchema_RolePermissionType =
      new NodeId(UShort.MIN, uint(16127L));

  public static final NodeId OpcUa_XmlSchema_RolePermissionType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16128L));

  public static final NodeId OpcUa_XmlSchema_RolePermissionType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16129L));

  public static final NodeId OpcUa_XmlSchema_BrokerWriterGroupTransportDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16130L));

  public static final NodeId OpcUa_BinarySchema_RolePermissionType =
      new NodeId(UShort.MIN, uint(16131L));

  public static final NodeId OpcUa_BinarySchema_RolePermissionType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16132L));

  public static final NodeId OpcUa_BinarySchema_RolePermissionType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16133L));

  public static final NodeId OPCUANamespaceMetadata_DefaultRolePermissions =
      new NodeId(UShort.MIN, uint(16134L));

  public static final NodeId OPCUANamespaceMetadata_DefaultUserRolePermissions =
      new NodeId(UShort.MIN, uint(16135L));

  public static final NodeId OPCUANamespaceMetadata_DefaultAccessRestrictions =
      new NodeId(UShort.MIN, uint(16136L));

  public static final NodeId NamespaceMetadataType_DefaultRolePermissions =
      new NodeId(UShort.MIN, uint(16137L));

  public static final NodeId NamespaceMetadataType_DefaultUserRolePermissions =
      new NodeId(UShort.MIN, uint(16138L));

  public static final NodeId NamespaceMetadataType_DefaultAccessRestrictions =
      new NodeId(UShort.MIN, uint(16139L));

  public static final NodeId NamespacesType_NamespaceIdentifier_Placeholder_DefaultRolePermissions =
      new NodeId(UShort.MIN, uint(16140L));

  public static final NodeId
      NamespacesType_NamespaceIdentifier_Placeholder_DefaultUserRolePermissions =
          new NodeId(UShort.MIN, uint(16141L));

  public static final NodeId
      NamespacesType_NamespaceIdentifier_Placeholder_DefaultAccessRestrictions =
          new NodeId(UShort.MIN, uint(16142L));

  public static final NodeId OpcUa_XmlSchema_BrokerWriterGroupTransportDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16143L));

  public static final NodeId OpcUa_XmlSchema_BrokerDataSetWriterTransportDataType =
      new NodeId(UShort.MIN, uint(16144L));

  public static final NodeId OpcUa_XmlSchema_BrokerDataSetWriterTransportDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16145L));

  public static final NodeId
      OpcUa_XmlSchema_BrokerDataSetWriterTransportDataType_DictionaryFragment =
          new NodeId(UShort.MIN, uint(16146L));

  public static final NodeId OpcUa_XmlSchema_BrokerDataSetReaderTransportDataType =
      new NodeId(UShort.MIN, uint(16147L));

  public static final NodeId OpcUa_XmlSchema_BrokerDataSetReaderTransportDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16148L));

  public static final NodeId
      OpcUa_XmlSchema_BrokerDataSetReaderTransportDataType_DictionaryFragment =
          new NodeId(UShort.MIN, uint(16149L));

  public static final NodeId EndpointType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(16150L));

  public static final NodeId DataTypeSchemaHeader_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(16151L));

  public static final NodeId PublishedDataSetDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(16152L));

  public static final NodeId PublishedDataSetSourceDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(16153L));

  public static final NodeId PublishedDataItemsDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(16154L));

  public static final NodeId PublishedEventsDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(16155L));

  public static final NodeId DataSetWriterDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(16156L));

  public static final NodeId DataSetWriterTransportDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(16157L));

  public static final NodeId DataSetWriterMessageDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(16158L));

  public static final NodeId PubSubGroupDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(16159L));

  public static final NodeId ServerConfiguration_ConfigurationFile_SetPosition_InputArguments =
      new NodeId(UShort.MIN, uint(16160L));

  public static final NodeId WriterGroupTransportDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(16161L));

  public static final NodeId RoleSetType_RoleName_Placeholder_Identities =
      new NodeId(UShort.MIN, uint(16162L));

  public static final NodeId RoleSetType_RoleName_Placeholder_Applications =
      new NodeId(UShort.MIN, uint(16163L));

  public static final NodeId RoleSetType_RoleName_Placeholder_Endpoints =
      new NodeId(UShort.MIN, uint(16164L));

  public static final NodeId RoleSetType_RoleName_Placeholder_AddApplication =
      new NodeId(UShort.MIN, uint(16165L));

  public static final NodeId RoleSetType_RoleName_Placeholder_AddApplication_InputArguments =
      new NodeId(UShort.MIN, uint(16166L));

  public static final NodeId RoleSetType_RoleName_Placeholder_RemoveApplication =
      new NodeId(UShort.MIN, uint(16167L));

  public static final NodeId RoleSetType_RoleName_Placeholder_RemoveApplication_InputArguments =
      new NodeId(UShort.MIN, uint(16168L));

  public static final NodeId RoleSetType_RoleName_Placeholder_AddEndpoint =
      new NodeId(UShort.MIN, uint(16169L));

  public static final NodeId RoleSetType_RoleName_Placeholder_AddEndpoint_InputArguments =
      new NodeId(UShort.MIN, uint(16170L));

  public static final NodeId RoleSetType_RoleName_Placeholder_RemoveEndpoint =
      new NodeId(UShort.MIN, uint(16171L));

  public static final NodeId RoleSetType_RoleName_Placeholder_RemoveEndpoint_InputArguments =
      new NodeId(UShort.MIN, uint(16172L));

  public static final NodeId RoleType_Identities = new NodeId(UShort.MIN, uint(16173L));

  public static final NodeId RoleType_Applications = new NodeId(UShort.MIN, uint(16174L));

  public static final NodeId RoleType_Endpoints = new NodeId(UShort.MIN, uint(16175L));

  public static final NodeId RoleType_AddApplication = new NodeId(UShort.MIN, uint(16176L));

  public static final NodeId RoleType_AddApplication_InputArguments =
      new NodeId(UShort.MIN, uint(16177L));

  public static final NodeId RoleType_RemoveApplication = new NodeId(UShort.MIN, uint(16178L));

  public static final NodeId RoleType_RemoveApplication_InputArguments =
      new NodeId(UShort.MIN, uint(16179L));

  public static final NodeId RoleType_AddEndpoint = new NodeId(UShort.MIN, uint(16180L));

  public static final NodeId RoleType_AddEndpoint_InputArguments =
      new NodeId(UShort.MIN, uint(16181L));

  public static final NodeId RoleType_RemoveEndpoint = new NodeId(UShort.MIN, uint(16182L));

  public static final NodeId RoleType_RemoveEndpoint_InputArguments =
      new NodeId(UShort.MIN, uint(16183L));

  public static final NodeId AddApplicationMethodType = new NodeId(UShort.MIN, uint(16184L));

  public static final NodeId AddApplicationMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(16185L));

  public static final NodeId RemoveApplicationMethodType = new NodeId(UShort.MIN, uint(16186L));

  public static final NodeId RemoveApplicationMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(16187L));

  public static final NodeId AddEndpointMethodType = new NodeId(UShort.MIN, uint(16188L));

  public static final NodeId AddEndpointMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(16189L));

  public static final NodeId RemoveEndpointMethodType = new NodeId(UShort.MIN, uint(16190L));

  public static final NodeId RemoveEndpointMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(16191L));

  public static final NodeId WellKnownRole_Anonymous_Identities =
      new NodeId(UShort.MIN, uint(16192L));

  public static final NodeId WellKnownRole_Anonymous_Applications =
      new NodeId(UShort.MIN, uint(16193L));

  public static final NodeId WellKnownRole_Anonymous_Endpoints =
      new NodeId(UShort.MIN, uint(16194L));

  public static final NodeId WellKnownRole_Anonymous_AddApplication =
      new NodeId(UShort.MIN, uint(16195L));

  public static final NodeId WellKnownRole_Anonymous_AddApplication_InputArguments =
      new NodeId(UShort.MIN, uint(16196L));

  public static final NodeId WellKnownRole_Anonymous_RemoveApplication =
      new NodeId(UShort.MIN, uint(16197L));

  public static final NodeId WellKnownRole_Anonymous_RemoveApplication_InputArguments =
      new NodeId(UShort.MIN, uint(16198L));

  public static final NodeId WellKnownRole_Anonymous_AddEndpoint =
      new NodeId(UShort.MIN, uint(16199L));

  public static final NodeId WellKnownRole_Anonymous_AddEndpoint_InputArguments =
      new NodeId(UShort.MIN, uint(16200L));

  public static final NodeId WellKnownRole_Anonymous_RemoveEndpoint =
      new NodeId(UShort.MIN, uint(16201L));

  public static final NodeId WellKnownRole_Anonymous_RemoveEndpoint_InputArguments =
      new NodeId(UShort.MIN, uint(16202L));

  public static final NodeId WellKnownRole_AuthenticatedUser_Identities =
      new NodeId(UShort.MIN, uint(16203L));

  public static final NodeId WellKnownRole_AuthenticatedUser_Applications =
      new NodeId(UShort.MIN, uint(16204L));

  public static final NodeId WellKnownRole_AuthenticatedUser_Endpoints =
      new NodeId(UShort.MIN, uint(16205L));

  public static final NodeId WellKnownRole_AuthenticatedUser_AddApplication =
      new NodeId(UShort.MIN, uint(16206L));

  public static final NodeId WellKnownRole_AuthenticatedUser_AddApplication_InputArguments =
      new NodeId(UShort.MIN, uint(16207L));

  public static final NodeId WellKnownRole_AuthenticatedUser_RemoveApplication =
      new NodeId(UShort.MIN, uint(16208L));

  public static final NodeId WellKnownRole_AuthenticatedUser_RemoveApplication_InputArguments =
      new NodeId(UShort.MIN, uint(16209L));

  public static final NodeId WellKnownRole_AuthenticatedUser_AddEndpoint =
      new NodeId(UShort.MIN, uint(16210L));

  public static final NodeId WellKnownRole_AuthenticatedUser_AddEndpoint_InputArguments =
      new NodeId(UShort.MIN, uint(16211L));

  public static final NodeId WellKnownRole_AuthenticatedUser_RemoveEndpoint =
      new NodeId(UShort.MIN, uint(16212L));

  public static final NodeId WellKnownRole_AuthenticatedUser_RemoveEndpoint_InputArguments =
      new NodeId(UShort.MIN, uint(16213L));

  public static final NodeId WellKnownRole_Observer_Identities =
      new NodeId(UShort.MIN, uint(16214L));

  public static final NodeId WellKnownRole_Observer_Applications =
      new NodeId(UShort.MIN, uint(16215L));

  public static final NodeId WellKnownRole_Observer_Endpoints =
      new NodeId(UShort.MIN, uint(16216L));

  public static final NodeId WellKnownRole_Observer_AddApplication =
      new NodeId(UShort.MIN, uint(16217L));

  public static final NodeId WellKnownRole_Observer_AddApplication_InputArguments =
      new NodeId(UShort.MIN, uint(16218L));

  public static final NodeId WellKnownRole_Observer_RemoveApplication =
      new NodeId(UShort.MIN, uint(16219L));

  public static final NodeId WellKnownRole_Observer_RemoveApplication_InputArguments =
      new NodeId(UShort.MIN, uint(16220L));

  public static final NodeId WellKnownRole_Observer_AddEndpoint =
      new NodeId(UShort.MIN, uint(16221L));

  public static final NodeId WellKnownRole_Observer_AddEndpoint_InputArguments =
      new NodeId(UShort.MIN, uint(16222L));

  public static final NodeId WellKnownRole_Observer_RemoveEndpoint =
      new NodeId(UShort.MIN, uint(16223L));

  public static final NodeId WellKnownRole_Observer_RemoveEndpoint_InputArguments =
      new NodeId(UShort.MIN, uint(16224L));

  public static final NodeId WellKnownRole_Operator_Identities =
      new NodeId(UShort.MIN, uint(16225L));

  public static final NodeId WellKnownRole_Operator_Applications =
      new NodeId(UShort.MIN, uint(16226L));

  public static final NodeId WellKnownRole_Operator_Endpoints =
      new NodeId(UShort.MIN, uint(16227L));

  public static final NodeId WellKnownRole_Operator_AddApplication =
      new NodeId(UShort.MIN, uint(16228L));

  public static final NodeId WellKnownRole_Operator_AddApplication_InputArguments =
      new NodeId(UShort.MIN, uint(16229L));

  public static final NodeId WellKnownRole_Operator_RemoveApplication =
      new NodeId(UShort.MIN, uint(16230L));

  public static final NodeId WellKnownRole_Operator_RemoveApplication_InputArguments =
      new NodeId(UShort.MIN, uint(16231L));

  public static final NodeId WellKnownRole_Operator_AddEndpoint =
      new NodeId(UShort.MIN, uint(16232L));

  public static final NodeId WellKnownRole_Operator_AddEndpoint_InputArguments =
      new NodeId(UShort.MIN, uint(16233L));

  public static final NodeId WellKnownRole_Operator_RemoveEndpoint =
      new NodeId(UShort.MIN, uint(16234L));

  public static final NodeId WellKnownRole_Operator_RemoveEndpoint_InputArguments =
      new NodeId(UShort.MIN, uint(16235L));

  public static final NodeId WellKnownRole_Engineer_Identities =
      new NodeId(UShort.MIN, uint(16236L));

  public static final NodeId WellKnownRole_Engineer_Applications =
      new NodeId(UShort.MIN, uint(16237L));

  public static final NodeId WellKnownRole_Engineer_Endpoints =
      new NodeId(UShort.MIN, uint(16238L));

  public static final NodeId WellKnownRole_Engineer_AddApplication =
      new NodeId(UShort.MIN, uint(16239L));

  public static final NodeId WellKnownRole_Engineer_AddApplication_InputArguments =
      new NodeId(UShort.MIN, uint(16240L));

  public static final NodeId WellKnownRole_Engineer_RemoveApplication =
      new NodeId(UShort.MIN, uint(16241L));

  public static final NodeId WellKnownRole_Engineer_RemoveApplication_InputArguments =
      new NodeId(UShort.MIN, uint(16242L));

  public static final NodeId WellKnownRole_Engineer_AddEndpoint =
      new NodeId(UShort.MIN, uint(16243L));

  public static final NodeId WellKnownRole_Engineer_AddEndpoint_InputArguments =
      new NodeId(UShort.MIN, uint(16244L));

  public static final NodeId WellKnownRole_Engineer_RemoveEndpoint =
      new NodeId(UShort.MIN, uint(16245L));

  public static final NodeId WellKnownRole_Engineer_RemoveEndpoint_InputArguments =
      new NodeId(UShort.MIN, uint(16246L));

  public static final NodeId WellKnownRole_Supervisor_Identities =
      new NodeId(UShort.MIN, uint(16247L));

  public static final NodeId WellKnownRole_Supervisor_Applications =
      new NodeId(UShort.MIN, uint(16248L));

  public static final NodeId WellKnownRole_Supervisor_Endpoints =
      new NodeId(UShort.MIN, uint(16249L));

  public static final NodeId WellKnownRole_Supervisor_AddApplication =
      new NodeId(UShort.MIN, uint(16250L));

  public static final NodeId WellKnownRole_Supervisor_AddApplication_InputArguments =
      new NodeId(UShort.MIN, uint(16251L));

  public static final NodeId WellKnownRole_Supervisor_RemoveApplication =
      new NodeId(UShort.MIN, uint(16252L));

  public static final NodeId WellKnownRole_Supervisor_RemoveApplication_InputArguments =
      new NodeId(UShort.MIN, uint(16253L));

  public static final NodeId WellKnownRole_Supervisor_AddEndpoint =
      new NodeId(UShort.MIN, uint(16254L));

  public static final NodeId WellKnownRole_Supervisor_AddEndpoint_InputArguments =
      new NodeId(UShort.MIN, uint(16255L));

  public static final NodeId WellKnownRole_Supervisor_RemoveEndpoint =
      new NodeId(UShort.MIN, uint(16256L));

  public static final NodeId WellKnownRole_Supervisor_RemoveEndpoint_InputArguments =
      new NodeId(UShort.MIN, uint(16257L));

  public static final NodeId WellKnownRole_SecurityAdmin_Identities =
      new NodeId(UShort.MIN, uint(16258L));

  public static final NodeId WellKnownRole_SecurityAdmin_Applications =
      new NodeId(UShort.MIN, uint(16259L));

  public static final NodeId WellKnownRole_SecurityAdmin_Endpoints =
      new NodeId(UShort.MIN, uint(16260L));

  public static final NodeId WellKnownRole_SecurityAdmin_AddApplication =
      new NodeId(UShort.MIN, uint(16261L));

  public static final NodeId WellKnownRole_SecurityAdmin_AddApplication_InputArguments =
      new NodeId(UShort.MIN, uint(16262L));

  public static final NodeId WellKnownRole_SecurityAdmin_RemoveApplication =
      new NodeId(UShort.MIN, uint(16263L));

  public static final NodeId WellKnownRole_SecurityAdmin_RemoveApplication_InputArguments =
      new NodeId(UShort.MIN, uint(16264L));

  public static final NodeId WellKnownRole_SecurityAdmin_AddEndpoint =
      new NodeId(UShort.MIN, uint(16265L));

  public static final NodeId WellKnownRole_SecurityAdmin_AddEndpoint_InputArguments =
      new NodeId(UShort.MIN, uint(16266L));

  public static final NodeId WellKnownRole_SecurityAdmin_RemoveEndpoint =
      new NodeId(UShort.MIN, uint(16267L));

  public static final NodeId WellKnownRole_SecurityAdmin_RemoveEndpoint_InputArguments =
      new NodeId(UShort.MIN, uint(16268L));

  public static final NodeId WellKnownRole_ConfigureAdmin_Identities =
      new NodeId(UShort.MIN, uint(16269L));

  public static final NodeId WellKnownRole_ConfigureAdmin_Applications =
      new NodeId(UShort.MIN, uint(16270L));

  public static final NodeId WellKnownRole_ConfigureAdmin_Endpoints =
      new NodeId(UShort.MIN, uint(16271L));

  public static final NodeId WellKnownRole_ConfigureAdmin_AddApplication =
      new NodeId(UShort.MIN, uint(16272L));

  public static final NodeId WellKnownRole_ConfigureAdmin_AddApplication_InputArguments =
      new NodeId(UShort.MIN, uint(16273L));

  public static final NodeId WellKnownRole_ConfigureAdmin_RemoveApplication =
      new NodeId(UShort.MIN, uint(16274L));

  public static final NodeId WellKnownRole_ConfigureAdmin_RemoveApplication_InputArguments =
      new NodeId(UShort.MIN, uint(16275L));

  public static final NodeId WellKnownRole_ConfigureAdmin_AddEndpoint =
      new NodeId(UShort.MIN, uint(16276L));

  public static final NodeId WellKnownRole_ConfigureAdmin_AddEndpoint_InputArguments =
      new NodeId(UShort.MIN, uint(16277L));

  public static final NodeId WellKnownRole_ConfigureAdmin_RemoveEndpoint =
      new NodeId(UShort.MIN, uint(16278L));

  public static final NodeId WellKnownRole_ConfigureAdmin_RemoveEndpoint_InputArguments =
      new NodeId(UShort.MIN, uint(16279L));

  public static final NodeId WriterGroupMessageDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(16280L));

  public static final NodeId PubSubConnectionDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(16281L));

  public static final NodeId ConnectionTransportDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(16282L));

  public static final NodeId ServerConfiguration_ConfigurationFile_LastUpdateTime =
      new NodeId(UShort.MIN, uint(16283L));

  public static final NodeId ReaderGroupTransportDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(16284L));

  public static final NodeId ReaderGroupMessageDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(16285L));

  public static final NodeId DataSetReaderDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(16286L));

  public static final NodeId DataSetReaderTransportDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(16287L));

  public static final NodeId DataSetReaderMessageDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(16288L));

  public static final NodeId ServerType_ServerCapabilities_RoleSet =
      new NodeId(UShort.MIN, uint(16289L));

  public static final NodeId ServerType_ServerCapabilities_RoleSet_AddRole =
      new NodeId(UShort.MIN, uint(16290L));

  public static final NodeId ServerType_ServerCapabilities_RoleSet_AddRole_InputArguments =
      new NodeId(UShort.MIN, uint(16291L));

  public static final NodeId ServerType_ServerCapabilities_RoleSet_AddRole_OutputArguments =
      new NodeId(UShort.MIN, uint(16292L));

  public static final NodeId ServerType_ServerCapabilities_RoleSet_RemoveRole =
      new NodeId(UShort.MIN, uint(16293L));

  public static final NodeId ServerType_ServerCapabilities_RoleSet_RemoveRole_InputArguments =
      new NodeId(UShort.MIN, uint(16294L));

  public static final NodeId ServerCapabilitiesType_RoleSet = new NodeId(UShort.MIN, uint(16295L));

  public static final NodeId ServerCapabilitiesType_RoleSet_AddRole =
      new NodeId(UShort.MIN, uint(16296L));

  public static final NodeId ServerCapabilitiesType_RoleSet_AddRole_InputArguments =
      new NodeId(UShort.MIN, uint(16297L));

  public static final NodeId ServerCapabilitiesType_RoleSet_AddRole_OutputArguments =
      new NodeId(UShort.MIN, uint(16298L));

  public static final NodeId ServerCapabilitiesType_RoleSet_RemoveRole =
      new NodeId(UShort.MIN, uint(16299L));

  public static final NodeId ServerCapabilitiesType_RoleSet_RemoveRole_InputArguments =
      new NodeId(UShort.MIN, uint(16300L));

  public static final NodeId Server_ServerCapabilities_RoleSet_AddRole =
      new NodeId(UShort.MIN, uint(16301L));

  public static final NodeId Server_ServerCapabilities_RoleSet_AddRole_InputArguments =
      new NodeId(UShort.MIN, uint(16302L));

  public static final NodeId Server_ServerCapabilities_RoleSet_AddRole_OutputArguments =
      new NodeId(UShort.MIN, uint(16303L));

  public static final NodeId Server_ServerCapabilities_RoleSet_RemoveRole =
      new NodeId(UShort.MIN, uint(16304L));

  public static final NodeId Server_ServerCapabilities_RoleSet_RemoveRole_InputArguments =
      new NodeId(UShort.MIN, uint(16305L));

  public static final NodeId ServerConfiguration_ConfigurationFile_CurrentVersion =
      new NodeId(UShort.MIN, uint(16306L));

  public static final NodeId AudioDataType = new NodeId(UShort.MIN, uint(16307L));

  public static final NodeId SubscribedDataSetDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(16308L));

  public static final NodeId SelectionListType = new NodeId(UShort.MIN, uint(16309L));

  public static final NodeId TargetVariablesDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(16310L));

  public static final NodeId SubscribedDataSetMirrorDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(16311L));

  public static final NodeId SelectionListType_RestrictToList =
      new NodeId(UShort.MIN, uint(16312L));

  public static final NodeId AdditionalParametersType = new NodeId(UShort.MIN, uint(16313L));

  public static final NodeId FileSystem = new NodeId(UShort.MIN, uint(16314L));

  public static final NodeId ServerConfiguration_ConfigurationFile_ActivityTimeout =
      new NodeId(UShort.MIN, uint(16315L));

  public static final NodeId ServerConfiguration_ConfigurationFile_SupportedDataType =
      new NodeId(UShort.MIN, uint(16316L));

  public static final NodeId ServerConfiguration_ConfigurationFile_CloseAndUpdate =
      new NodeId(UShort.MIN, uint(16317L));

  public static final NodeId ServerConfiguration_ConfigurationFile_CloseAndUpdate_InputArguments =
      new NodeId(UShort.MIN, uint(16318L));

  public static final NodeId ServerConfiguration_ConfigurationFile_CloseAndUpdate_OutputArguments =
      new NodeId(UShort.MIN, uint(16319L));

  public static final NodeId ServerConfiguration_ConfigurationFile_ConfirmUpdate =
      new NodeId(UShort.MIN, uint(16320L));

  public static final NodeId ServerConfiguration_ConfigurationFile_ConfirmUpdate_InputArguments =
      new NodeId(UShort.MIN, uint(16321L));

  public static final NodeId UadpWriterGroupMessageDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(16323L));

  public static final NodeId ProvisionableDeviceType_ApplicationName_Placeholder_ConfigurationFile =
      new NodeId(UShort.MIN, uint(16324L));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_ConfigurationFile_Size =
          new NodeId(UShort.MIN, uint(16325L));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_ConfigurationFile_Writable =
          new NodeId(UShort.MIN, uint(16326L));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_ConfigurationFile_UserWritable =
          new NodeId(UShort.MIN, uint(16327L));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_ConfigurationFile_OpenCount =
          new NodeId(UShort.MIN, uint(16328L));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_ConfigurationFile_MimeType =
          new NodeId(UShort.MIN, uint(16329L));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_ConfigurationFile_MaxByteStringLength =
          new NodeId(UShort.MIN, uint(16330L));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_ConfigurationFile_LastModifiedTime =
          new NodeId(UShort.MIN, uint(16331L));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_ConfigurationFile_Open =
          new NodeId(UShort.MIN, uint(16332L));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_ConfigurationFile_Open_InputArguments =
          new NodeId(UShort.MIN, uint(16333L));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_ConfigurationFile_Open_OutputArguments =
          new NodeId(UShort.MIN, uint(16334L));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_ConfigurationFile_Close =
          new NodeId(UShort.MIN, uint(16335L));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_ConfigurationFile_Close_InputArguments =
          new NodeId(UShort.MIN, uint(16336L));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_ConfigurationFile_Read =
          new NodeId(UShort.MIN, uint(16337L));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_ConfigurationFile_Read_InputArguments =
          new NodeId(UShort.MIN, uint(16338L));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_ConfigurationFile_Read_OutputArguments =
          new NodeId(UShort.MIN, uint(16339L));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_ConfigurationFile_Write =
          new NodeId(UShort.MIN, uint(16340L));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_ConfigurationFile_Write_InputArguments =
          new NodeId(UShort.MIN, uint(16341L));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_ConfigurationFile_GetPosition =
          new NodeId(UShort.MIN, uint(16342L));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_ConfigurationFile_GetPosition_InputArguments =
          new NodeId(UShort.MIN, uint(16343L));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_ConfigurationFile_GetPosition_OutputArguments =
          new NodeId(UShort.MIN, uint(16344L));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_ConfigurationFile_SetPosition =
          new NodeId(UShort.MIN, uint(16345L));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_ConfigurationFile_SetPosition_InputArguments =
          new NodeId(UShort.MIN, uint(16346L));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_ConfigurationFile_LastUpdateTime =
          new NodeId(UShort.MIN, uint(16347L));

  public static final NodeId FileSystem_CreateDirectory = new NodeId(UShort.MIN, uint(16348L));

  public static final NodeId FileSystem_CreateDirectory_InputArguments =
      new NodeId(UShort.MIN, uint(16349L));

  public static final NodeId FileSystem_CreateDirectory_OutputArguments =
      new NodeId(UShort.MIN, uint(16350L));

  public static final NodeId FileSystem_CreateFile = new NodeId(UShort.MIN, uint(16351L));

  public static final NodeId FileSystem_CreateFile_InputArguments =
      new NodeId(UShort.MIN, uint(16352L));

  public static final NodeId FileSystem_CreateFile_OutputArguments =
      new NodeId(UShort.MIN, uint(16353L));

  public static final NodeId FileSystem_DeleteFileSystemObject =
      new NodeId(UShort.MIN, uint(16354L));

  public static final NodeId FileSystem_DeleteFileSystemObject_InputArguments =
      new NodeId(UShort.MIN, uint(16355L));

  public static final NodeId FileSystem_MoveOrCopy = new NodeId(UShort.MIN, uint(16356L));

  public static final NodeId FileSystem_MoveOrCopy_InputArguments =
      new NodeId(UShort.MIN, uint(16357L));

  public static final NodeId FileSystem_MoveOrCopy_OutputArguments =
      new NodeId(UShort.MIN, uint(16358L));

  public static final NodeId TemporaryFileTransferType_GenerateFileForWrite_InputArguments =
      new NodeId(UShort.MIN, uint(16359L));

  public static final NodeId GenerateFileForWriteMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(16360L));

  public static final NodeId HasAlarmSuppressionGroup = new NodeId(UShort.MIN, uint(16361L));

  public static final NodeId AlarmGroupMember = new NodeId(UShort.MIN, uint(16362L));

  public static final NodeId ConditionType_ConditionSubClassId =
      new NodeId(UShort.MIN, uint(16363L));

  public static final NodeId ConditionType_ConditionSubClassName =
      new NodeId(UShort.MIN, uint(16364L));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_ConfigurationFile_CurrentVersion =
          new NodeId(UShort.MIN, uint(16365L));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_ConfigurationFile_ActivityTimeout =
          new NodeId(UShort.MIN, uint(16366L));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_ConfigurationFile_SupportedDataType =
          new NodeId(UShort.MIN, uint(16367L));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_ConfigurationFile_CloseAndUpdate =
          new NodeId(UShort.MIN, uint(16368L));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_ConfigurationFile_CloseAndUpdate_InputArguments =
          new NodeId(UShort.MIN, uint(16369L));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_ConfigurationFile_CloseAndUpdate_OutputArguments =
          new NodeId(UShort.MIN, uint(16370L));

  public static final NodeId AlarmConditionType_OutOfServiceState =
      new NodeId(UShort.MIN, uint(16371L));

  public static final NodeId AlarmConditionType_OutOfServiceState_Id =
      new NodeId(UShort.MIN, uint(16372L));

  public static final NodeId AlarmConditionType_OutOfServiceState_Name =
      new NodeId(UShort.MIN, uint(16373L));

  public static final NodeId AlarmConditionType_OutOfServiceState_Number =
      new NodeId(UShort.MIN, uint(16374L));

  public static final NodeId AlarmConditionType_OutOfServiceState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(16375L));

  public static final NodeId AlarmConditionType_OutOfServiceState_TransitionTime =
      new NodeId(UShort.MIN, uint(16376L));

  public static final NodeId AlarmConditionType_OutOfServiceState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(16377L));

  public static final NodeId AlarmConditionType_OutOfServiceState_TrueState =
      new NodeId(UShort.MIN, uint(16378L));

  public static final NodeId AlarmConditionType_OutOfServiceState_FalseState =
      new NodeId(UShort.MIN, uint(16379L));

  public static final NodeId AlarmConditionType_SilenceState = new NodeId(UShort.MIN, uint(16380L));

  public static final NodeId AlarmConditionType_SilenceState_Id =
      new NodeId(UShort.MIN, uint(16381L));

  public static final NodeId AlarmConditionType_SilenceState_Name =
      new NodeId(UShort.MIN, uint(16382L));

  public static final NodeId AlarmConditionType_SilenceState_Number =
      new NodeId(UShort.MIN, uint(16383L));

  public static final NodeId AlarmConditionType_SilenceState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(16384L));

  public static final NodeId AlarmConditionType_SilenceState_TransitionTime =
      new NodeId(UShort.MIN, uint(16385L));

  public static final NodeId AlarmConditionType_SilenceState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(16386L));

  public static final NodeId AlarmConditionType_SilenceState_TrueState =
      new NodeId(UShort.MIN, uint(16387L));

  public static final NodeId AlarmConditionType_SilenceState_FalseState =
      new NodeId(UShort.MIN, uint(16388L));

  public static final NodeId AlarmConditionType_AudibleEnabled =
      new NodeId(UShort.MIN, uint(16389L));

  public static final NodeId AlarmConditionType_AudibleSound = new NodeId(UShort.MIN, uint(16390L));

  public static final NodeId UadpDataSetWriterMessageDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(16391L));

  public static final NodeId UadpDataSetReaderMessageDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(16392L));

  public static final NodeId JsonWriterGroupMessageDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(16393L));

  public static final NodeId JsonDataSetWriterMessageDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(16394L));

  public static final NodeId AlarmConditionType_OnDelay = new NodeId(UShort.MIN, uint(16395L));

  public static final NodeId AlarmConditionType_OffDelay = new NodeId(UShort.MIN, uint(16396L));

  public static final NodeId AlarmConditionType_FirstInGroupFlag =
      new NodeId(UShort.MIN, uint(16397L));

  public static final NodeId AlarmConditionType_FirstInGroup = new NodeId(UShort.MIN, uint(16398L));

  public static final NodeId AlarmConditionType_AlarmGroup_Placeholder =
      new NodeId(UShort.MIN, uint(16399L));

  public static final NodeId AlarmConditionType_ReAlarmTime = new NodeId(UShort.MIN, uint(16400L));

  public static final NodeId AlarmConditionType_ReAlarmRepeatCount =
      new NodeId(UShort.MIN, uint(16401L));

  public static final NodeId AlarmConditionType_Silence = new NodeId(UShort.MIN, uint(16402L));

  public static final NodeId AlarmConditionType_Suppress = new NodeId(UShort.MIN, uint(16403L));

  public static final NodeId JsonDataSetReaderMessageDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(16404L));

  public static final NodeId AlarmGroupType = new NodeId(UShort.MIN, uint(16405L));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder =
      new NodeId(UShort.MIN, uint(16406L));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_EventId =
      new NodeId(UShort.MIN, uint(16407L));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_EventType =
      new NodeId(UShort.MIN, uint(16408L));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_SourceNode =
      new NodeId(UShort.MIN, uint(16409L));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_SourceName =
      new NodeId(UShort.MIN, uint(16410L));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_Time =
      new NodeId(UShort.MIN, uint(16411L));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_ReceiveTime =
      new NodeId(UShort.MIN, uint(16412L));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_LocalTime =
      new NodeId(UShort.MIN, uint(16413L));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_Message =
      new NodeId(UShort.MIN, uint(16414L));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_Severity =
      new NodeId(UShort.MIN, uint(16415L));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_ConditionClassId =
      new NodeId(UShort.MIN, uint(16416L));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_ConditionClassName =
      new NodeId(UShort.MIN, uint(16417L));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_ConditionSubClassId =
      new NodeId(UShort.MIN, uint(16418L));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_ConditionSubClassName =
          new NodeId(UShort.MIN, uint(16419L));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_ConditionName =
      new NodeId(UShort.MIN, uint(16420L));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_BranchId =
      new NodeId(UShort.MIN, uint(16421L));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_Retain =
      new NodeId(UShort.MIN, uint(16422L));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_EnabledState =
      new NodeId(UShort.MIN, uint(16423L));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_EnabledState_Id =
      new NodeId(UShort.MIN, uint(16424L));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_EnabledState_Name =
      new NodeId(UShort.MIN, uint(16425L));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_EnabledState_Number =
      new NodeId(UShort.MIN, uint(16426L));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_EnabledState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(16427L));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_EnabledState_TransitionTime =
          new NodeId(UShort.MIN, uint(16428L));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_EnabledState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(16429L));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_EnabledState_TrueState =
          new NodeId(UShort.MIN, uint(16430L));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_EnabledState_FalseState =
          new NodeId(UShort.MIN, uint(16431L));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_Quality =
      new NodeId(UShort.MIN, uint(16432L));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_Quality_SourceTimestamp =
          new NodeId(UShort.MIN, uint(16433L));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_LastSeverity =
      new NodeId(UShort.MIN, uint(16434L));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_LastSeverity_SourceTimestamp =
          new NodeId(UShort.MIN, uint(16435L));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_Comment =
      new NodeId(UShort.MIN, uint(16436L));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_Comment_SourceTimestamp =
          new NodeId(UShort.MIN, uint(16437L));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_ClientUserId =
      new NodeId(UShort.MIN, uint(16438L));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_Disable =
      new NodeId(UShort.MIN, uint(16439L));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_Enable =
      new NodeId(UShort.MIN, uint(16440L));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_AddComment =
      new NodeId(UShort.MIN, uint(16441L));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_AddComment_InputArguments =
          new NodeId(UShort.MIN, uint(16442L));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_AckedState =
      new NodeId(UShort.MIN, uint(16443L));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_AckedState_Id =
      new NodeId(UShort.MIN, uint(16444L));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_AckedState_Name =
      new NodeId(UShort.MIN, uint(16445L));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_AckedState_Number =
      new NodeId(UShort.MIN, uint(16446L));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_AckedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(16447L));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_AckedState_TransitionTime =
          new NodeId(UShort.MIN, uint(16448L));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_AckedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(16449L));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_AckedState_TrueState =
          new NodeId(UShort.MIN, uint(16450L));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_AckedState_FalseState =
          new NodeId(UShort.MIN, uint(16451L));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_ConfirmedState =
      new NodeId(UShort.MIN, uint(16452L));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_ConfirmedState_Id =
      new NodeId(UShort.MIN, uint(16453L));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_ConfirmedState_Name =
      new NodeId(UShort.MIN, uint(16454L));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_ConfirmedState_Number =
          new NodeId(UShort.MIN, uint(16455L));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_ConfirmedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(16456L));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_ConfirmedState_TransitionTime =
          new NodeId(UShort.MIN, uint(16457L));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_ConfirmedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(16458L));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_ConfirmedState_TrueState =
          new NodeId(UShort.MIN, uint(16459L));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_ConfirmedState_FalseState =
          new NodeId(UShort.MIN, uint(16460L));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_Acknowledge =
      new NodeId(UShort.MIN, uint(16461L));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_Acknowledge_InputArguments =
          new NodeId(UShort.MIN, uint(16462L));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_Confirm =
      new NodeId(UShort.MIN, uint(16463L));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_Confirm_InputArguments =
          new NodeId(UShort.MIN, uint(16464L));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_ActiveState =
      new NodeId(UShort.MIN, uint(16465L));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_ActiveState_Id =
      new NodeId(UShort.MIN, uint(16466L));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_ActiveState_Name =
      new NodeId(UShort.MIN, uint(16467L));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_ActiveState_Number =
      new NodeId(UShort.MIN, uint(16468L));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_ActiveState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(16469L));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_ActiveState_TransitionTime =
          new NodeId(UShort.MIN, uint(16470L));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_ActiveState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(16471L));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_ActiveState_TrueState =
          new NodeId(UShort.MIN, uint(16472L));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_ActiveState_FalseState =
          new NodeId(UShort.MIN, uint(16473L));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_InputNode =
      new NodeId(UShort.MIN, uint(16474L));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_SuppressedState =
      new NodeId(UShort.MIN, uint(16475L));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_SuppressedState_Id =
      new NodeId(UShort.MIN, uint(16476L));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_SuppressedState_Name =
          new NodeId(UShort.MIN, uint(16477L));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_SuppressedState_Number =
          new NodeId(UShort.MIN, uint(16478L));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_SuppressedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(16479L));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_SuppressedState_TransitionTime =
          new NodeId(UShort.MIN, uint(16480L));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_SuppressedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(16481L));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_SuppressedState_TrueState =
          new NodeId(UShort.MIN, uint(16482L));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_SuppressedState_FalseState =
          new NodeId(UShort.MIN, uint(16483L));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_OutOfServiceState =
      new NodeId(UShort.MIN, uint(16484L));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_OutOfServiceState_Id =
          new NodeId(UShort.MIN, uint(16485L));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_OutOfServiceState_Name =
          new NodeId(UShort.MIN, uint(16486L));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_OutOfServiceState_Number =
          new NodeId(UShort.MIN, uint(16487L));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_OutOfServiceState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(16488L));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_OutOfServiceState_TransitionTime =
          new NodeId(UShort.MIN, uint(16489L));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_OutOfServiceState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(16490L));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_OutOfServiceState_TrueState =
          new NodeId(UShort.MIN, uint(16491L));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_OutOfServiceState_FalseState =
          new NodeId(UShort.MIN, uint(16492L));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_SilenceState =
      new NodeId(UShort.MIN, uint(16493L));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_SilenceState_Id =
      new NodeId(UShort.MIN, uint(16494L));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_SilenceState_Name =
      new NodeId(UShort.MIN, uint(16495L));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_SilenceState_Number =
      new NodeId(UShort.MIN, uint(16496L));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_SilenceState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(16497L));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_SilenceState_TransitionTime =
          new NodeId(UShort.MIN, uint(16498L));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_SilenceState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(16499L));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_SilenceState_TrueState =
          new NodeId(UShort.MIN, uint(16500L));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_SilenceState_FalseState =
          new NodeId(UShort.MIN, uint(16501L));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_ShelvingState =
      new NodeId(UShort.MIN, uint(16502L));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_ShelvingState_CurrentState =
          new NodeId(UShort.MIN, uint(16503L));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_ShelvingState_CurrentState_Id =
          new NodeId(UShort.MIN, uint(16504L));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_ShelvingState_CurrentState_Name =
          new NodeId(UShort.MIN, uint(16505L));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_ShelvingState_CurrentState_Number =
          new NodeId(UShort.MIN, uint(16506L));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_ShelvingState_CurrentState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(16507L));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_ShelvingState_LastTransition =
          new NodeId(UShort.MIN, uint(16508L));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_ShelvingState_LastTransition_Id =
          new NodeId(UShort.MIN, uint(16509L));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_ShelvingState_LastTransition_Name =
          new NodeId(UShort.MIN, uint(16510L));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_ShelvingState_LastTransition_Number =
          new NodeId(UShort.MIN, uint(16511L));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_ShelvingState_LastTransition_TransitionTime =
          new NodeId(UShort.MIN, uint(16512L));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_ShelvingState_LastTransition_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(16513L));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_ShelvingState_UnshelveTime =
          new NodeId(UShort.MIN, uint(16514L));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_ShelvingState_Unshelve =
          new NodeId(UShort.MIN, uint(16515L));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_ShelvingState_OneShotShelve =
          new NodeId(UShort.MIN, uint(16516L));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_ShelvingState_TimedShelve =
          new NodeId(UShort.MIN, uint(16517L));

  public static final NodeId
      AlarmGroupType_AlarmConditionInstance_Placeholder_ShelvingState_TimedShelve_InputArguments =
          new NodeId(UShort.MIN, uint(16518L));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_SuppressedOrShelved =
      new NodeId(UShort.MIN, uint(16519L));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_MaxTimeShelved =
      new NodeId(UShort.MIN, uint(16520L));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_AudibleEnabled =
      new NodeId(UShort.MIN, uint(16521L));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_AudibleSound =
      new NodeId(UShort.MIN, uint(16522L));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_ConfigurationFile_ConfirmUpdate =
          new NodeId(UShort.MIN, uint(16523L));

  public static final NodeId BrokerWriterGroupTransportDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(16524L));

  public static final NodeId BrokerDataSetWriterTransportDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(16525L));

  public static final NodeId BrokerDataSetReaderTransportDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(16526L));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_OnDelay =
      new NodeId(UShort.MIN, uint(16527L));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_OffDelay =
      new NodeId(UShort.MIN, uint(16528L));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_FirstInGroupFlag =
      new NodeId(UShort.MIN, uint(16529L));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_FirstInGroup =
      new NodeId(UShort.MIN, uint(16530L));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_ReAlarmTime =
      new NodeId(UShort.MIN, uint(16531L));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_ReAlarmRepeatCount =
      new NodeId(UShort.MIN, uint(16532L));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_Silence =
      new NodeId(UShort.MIN, uint(16533L));

  public static final NodeId AlarmGroupType_AlarmConditionInstance_Placeholder_Suppress =
      new NodeId(UShort.MIN, uint(16534L));

  public static final NodeId PublishSubscribeType_ConnectionName_Placeholder_AddWriterGroup =
      new NodeId(UShort.MIN, uint(16535L));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_ConfigurationFile_ConfirmUpdate_InputArguments =
          new NodeId(UShort.MIN, uint(16536L));

  public static final NodeId BaseConfigurationDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(16538L));

  public static final NodeId BaseConfigurationRecordDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(16539L));

  public static final NodeId CertificateGroupDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(16540L));

  public static final NodeId ConfigurationUpdateTargetType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(16541L));

  public static final NodeId ApplicationIdentityDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(16543L));

  public static final NodeId EndpointDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(16544L));

  public static final NodeId ServerEndpointDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(16545L));

  public static final NodeId SecuritySettingsDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(16546L));

  public static final NodeId UserTokenSettingsDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(16547L));

  public static final NodeId OpcUa_BinarySchema_BaseConfigurationDataType =
      new NodeId(UShort.MIN, uint(16548L));

  public static final NodeId OpcUa_BinarySchema_BaseConfigurationDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16549L));

  public static final NodeId OpcUa_BinarySchema_BaseConfigurationDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16550L));

  public static final NodeId OpcUa_BinarySchema_BaseConfigurationRecordDataType =
      new NodeId(UShort.MIN, uint(16551L));

  public static final NodeId OpcUa_BinarySchema_BaseConfigurationRecordDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16552L));

  public static final NodeId OpcUa_BinarySchema_BaseConfigurationRecordDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16553L));

  public static final NodeId OpcUa_BinarySchema_CertificateGroupDataType =
      new NodeId(UShort.MIN, uint(16554L));

  public static final NodeId OpcUa_BinarySchema_CertificateGroupDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16555L));

  public static final NodeId OpcUa_BinarySchema_CertificateGroupDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16556L));

  public static final NodeId OpcUa_BinarySchema_ConfigurationUpdateTargetType =
      new NodeId(UShort.MIN, uint(16557L));

  public static final NodeId
      PublishSubscribeType_ConnectionName_Placeholder_AddWriterGroup_InputArguments =
          new NodeId(UShort.MIN, uint(16558L));

  public static final NodeId
      PublishSubscribeType_ConnectionName_Placeholder_AddWriterGroup_OutputArguments =
          new NodeId(UShort.MIN, uint(16559L));

  public static final NodeId PublishSubscribeType_ConnectionName_Placeholder_AddReaderGroup =
      new NodeId(UShort.MIN, uint(16560L));

  public static final NodeId
      PublishSubscribeType_ConnectionName_Placeholder_AddReaderGroup_InputArguments =
          new NodeId(UShort.MIN, uint(16561L));

  public static final NodeId OpcUa_BinarySchema_ConfigurationUpdateTargetType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16562L));

  public static final NodeId OpcUa_BinarySchema_ConfigurationUpdateTargetType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16563L));

  public static final NodeId OpcUa_BinarySchema_ApplicationIdentityDataType =
      new NodeId(UShort.MIN, uint(16567L));

  public static final NodeId OpcUa_BinarySchema_ApplicationIdentityDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16568L));

  public static final NodeId OpcUa_BinarySchema_ApplicationIdentityDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16569L));

  public static final NodeId OpcUa_BinarySchema_EndpointDataType =
      new NodeId(UShort.MIN, uint(16570L));

  public static final NodeId
      PublishSubscribeType_ConnectionName_Placeholder_AddReaderGroup_OutputArguments =
          new NodeId(UShort.MIN, uint(16571L));

  public static final NodeId LimitAlarmType_BaseHighHighLimit =
      new NodeId(UShort.MIN, uint(16572L));

  public static final NodeId LimitAlarmType_BaseHighLimit = new NodeId(UShort.MIN, uint(16573L));

  public static final NodeId LimitAlarmType_BaseLowLimit = new NodeId(UShort.MIN, uint(16574L));

  public static final NodeId LimitAlarmType_BaseLowLowLimit = new NodeId(UShort.MIN, uint(16575L));

  public static final NodeId OpcUa_BinarySchema_EndpointDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16576L));

  public static final NodeId OpcUa_BinarySchema_EndpointDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16577L));

  public static final NodeId OpcUa_BinarySchema_ServerEndpointDataType =
      new NodeId(UShort.MIN, uint(16578L));

  public static final NodeId OpcUa_BinarySchema_ServerEndpointDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16579L));

  public static final NodeId OpcUa_BinarySchema_ServerEndpointDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16580L));

  public static final NodeId OpcUa_BinarySchema_SecuritySettingsDataType =
      new NodeId(UShort.MIN, uint(16581L));

  public static final NodeId OpcUa_BinarySchema_SecuritySettingsDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16582L));

  public static final NodeId OpcUa_BinarySchema_SecuritySettingsDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16583L));

  public static final NodeId OpcUa_BinarySchema_UserTokenSettingsDataType =
      new NodeId(UShort.MIN, uint(16584L));

  public static final NodeId OpcUa_BinarySchema_UserTokenSettingsDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16585L));

  public static final NodeId OpcUa_BinarySchema_UserTokenSettingsDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16586L));

  public static final NodeId BaseConfigurationDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(16587L));

  public static final NodeId BaseConfigurationRecordDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(16588L));

  public static final NodeId CertificateGroupDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(16589L));

  public static final NodeId ConfigurationUpdateTargetType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(16590L));

  public static final NodeId ApplicationIdentityDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(16592L));

  public static final NodeId EndpointDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(16593L));

  public static final NodeId ServerEndpointDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(16594L));

  public static final NodeId SecuritySettingsDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(16595L));

  public static final NodeId UserTokenSettingsDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(16596L));

  public static final NodeId OpcUa_XmlSchema_BaseConfigurationDataType =
      new NodeId(UShort.MIN, uint(16597L));

  public static final NodeId PublishSubscribeType_AddConnection =
      new NodeId(UShort.MIN, uint(16598L));

  public static final NodeId PublishSubscribeType_AddConnection_InputArguments =
      new NodeId(UShort.MIN, uint(16599L));

  public static final NodeId PublishSubscribeType_AddConnection_OutputArguments =
      new NodeId(UShort.MIN, uint(16600L));

  public static final NodeId PublishSubscribeType_PublishedDataSets_AddPublishedDataItemsTemplate =
      new NodeId(UShort.MIN, uint(16601L));

  public static final NodeId OpcUa_XmlSchema_BaseConfigurationDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16602L));

  public static final NodeId OpcUa_XmlSchema_BaseConfigurationDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16603L));

  public static final NodeId OpcUa_XmlSchema_BaseConfigurationRecordDataType =
      new NodeId(UShort.MIN, uint(16604L));

  public static final NodeId OpcUa_XmlSchema_BaseConfigurationRecordDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16605L));

  public static final NodeId OpcUa_XmlSchema_BaseConfigurationRecordDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16606L));

  public static final NodeId OpcUa_XmlSchema_CertificateGroupDataType =
      new NodeId(UShort.MIN, uint(16607L));

  public static final NodeId OpcUa_XmlSchema_CertificateGroupDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16608L));

  public static final NodeId OpcUa_XmlSchema_CertificateGroupDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16609L));

  public static final NodeId OpcUa_XmlSchema_ConfigurationUpdateTargetType =
      new NodeId(UShort.MIN, uint(16610L));

  public static final NodeId
      PublishSubscribeType_PublishedDataSets_AddPublishedDataItemsTemplate_InputArguments =
          new NodeId(UShort.MIN, uint(16611L));

  public static final NodeId OpcUa_XmlSchema_ConfigurationUpdateTargetType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16612L));

  public static final NodeId OpcUa_XmlSchema_ConfigurationUpdateTargetType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16613L));

  public static final NodeId OpcUa_XmlSchema_ApplicationIdentityDataType =
      new NodeId(UShort.MIN, uint(16617L));

  public static final NodeId OpcUa_XmlSchema_ApplicationIdentityDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16618L));

  public static final NodeId OpcUa_XmlSchema_ApplicationIdentityDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16619L));

  public static final NodeId OpcUa_XmlSchema_EndpointDataType =
      new NodeId(UShort.MIN, uint(16620L));

  public static final NodeId OpcUa_XmlSchema_EndpointDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16621L));

  public static final NodeId OpcUa_XmlSchema_EndpointDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16622L));

  public static final NodeId OpcUa_XmlSchema_ServerEndpointDataType =
      new NodeId(UShort.MIN, uint(16623L));

  public static final NodeId OpcUa_XmlSchema_ServerEndpointDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16624L));

  public static final NodeId OpcUa_XmlSchema_ServerEndpointDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16625L));

  public static final NodeId OpcUa_XmlSchema_SecuritySettingsDataType =
      new NodeId(UShort.MIN, uint(16626L));

  public static final NodeId OpcUa_XmlSchema_SecuritySettingsDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16627L));

  public static final NodeId OpcUa_XmlSchema_SecuritySettingsDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16628L));

  public static final NodeId OpcUa_XmlSchema_UserTokenSettingsDataType =
      new NodeId(UShort.MIN, uint(16629L));

  public static final NodeId OpcUa_XmlSchema_UserTokenSettingsDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(16630L));

  public static final NodeId OpcUa_XmlSchema_UserTokenSettingsDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(16631L));

  public static final NodeId BaseConfigurationDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(16632L));

  public static final NodeId BaseConfigurationRecordDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(16633L));

  public static final NodeId CertificateGroupDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(16634L));

  public static final NodeId ConfigurationUpdateTargetType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(16635L));

  public static final NodeId ApplicationIdentityDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(16637L));

  public static final NodeId
      PublishSubscribeType_PublishedDataSets_AddPublishedDataItemsTemplate_OutputArguments =
          new NodeId(UShort.MIN, uint(16638L));

  public static final NodeId PublishSubscribeType_PublishedDataSets_AddPublishedEventsTemplate =
      new NodeId(UShort.MIN, uint(16639L));

  public static final NodeId
      PublishSubscribeType_PublishedDataSets_AddPublishedEventsTemplate_InputArguments =
          new NodeId(UShort.MIN, uint(16640L));

  public static final NodeId
      PublishSubscribeType_PublishedDataSets_AddPublishedEventsTemplate_OutputArguments =
          new NodeId(UShort.MIN, uint(16641L));

  public static final NodeId EndpointDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(16642L));

  public static final NodeId ServerEndpointDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(16643L));

  public static final NodeId SecuritySettingsDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(16644L));

  public static final NodeId UserTokenSettingsDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(16645L));

  public static final NodeId ServerConfigurationType_ConfigurationFile_AvailableNetworks =
      new NodeId(UShort.MIN, uint(16646L));

  public static final NodeId ServerConfigurationType_ConfigurationFile_AvailablePorts =
      new NodeId(UShort.MIN, uint(16647L));

  public static final NodeId ServerConfigurationType_ConfigurationFile_SecurityPolicyUris =
      new NodeId(UShort.MIN, uint(16648L));

  public static final NodeId ServerConfigurationType_ConfigurationFile_UserTokenTypes =
      new NodeId(UShort.MIN, uint(16649L));

  public static final NodeId ServerConfigurationType_ConfigurationFile_CertificateTypes =
      new NodeId(UShort.MIN, uint(16650L));

  public static final NodeId PublishSubscribeType_PublishedDataSets_AddDataSetFolder =
      new NodeId(UShort.MIN, uint(16651L));

  public static final NodeId ServerConfiguration_ConfigurationFile_AvailableNetworks =
      new NodeId(UShort.MIN, uint(16652L));

  public static final NodeId ServerConfiguration_ConfigurationFile_AvailablePorts =
      new NodeId(UShort.MIN, uint(16653L));

  public static final NodeId ServerConfiguration_ConfigurationFile_SecurityPolicyUris =
      new NodeId(UShort.MIN, uint(16654L));

  public static final NodeId ServerConfiguration_ConfigurationFile_UserTokenTypes =
      new NodeId(UShort.MIN, uint(16655L));

  public static final NodeId ServerConfiguration_ConfigurationFile_CertificateTypes =
      new NodeId(UShort.MIN, uint(16656L));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_ConfigurationFile_AvailableNetworks =
          new NodeId(UShort.MIN, uint(16657L));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_ConfigurationFile_AvailablePorts =
          new NodeId(UShort.MIN, uint(16658L));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_ConfigurationFile_SecurityPolicyUris =
          new NodeId(UShort.MIN, uint(16659L));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_ConfigurationFile_UserTokenTypes =
          new NodeId(UShort.MIN, uint(16660L));

  public static final NodeId
      ProvisionableDeviceType_ApplicationName_Placeholder_ConfigurationFile_CertificateTypes =
          new NodeId(UShort.MIN, uint(16661L));

  public static final NodeId ApplicationConfigurationFolderType =
      new NodeId(UShort.MIN, uint(16662L));

  public static final NodeId ApplicationConfigurationFolderType_ApplicationName_Placeholder =
      new NodeId(UShort.MIN, uint(16663L));

  public static final NodeId
      PublishSubscribeType_PublishedDataSets_AddDataSetFolder_InputArguments =
          new NodeId(UShort.MIN, uint(16678L));

  public static final NodeId
      PublishSubscribeType_PublishedDataSets_AddDataSetFolder_OutputArguments =
          new NodeId(UShort.MIN, uint(16679L));

  public static final NodeId PublishSubscribeType_PublishedDataSets_RemoveDataSetFolder =
      new NodeId(UShort.MIN, uint(16680L));

  public static final NodeId
      PublishSubscribeType_PublishedDataSets_RemoveDataSetFolder_InputArguments =
          new NodeId(UShort.MIN, uint(16681L));

  public static final NodeId AddConnectionMethodType = new NodeId(UShort.MIN, uint(16691L));

  public static final NodeId ManagedApplications = new NodeId(UShort.MIN, uint(16706L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups =
          new NodeId(UShort.MIN, uint(16707L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup =
          new NodeId(UShort.MIN, uint(16708L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustList =
          new NodeId(UShort.MIN, uint(16709L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustList_Size =
          new NodeId(UShort.MIN, uint(16710L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustList_Writable =
          new NodeId(UShort.MIN, uint(16711L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustList_UserWritable =
          new NodeId(UShort.MIN, uint(16712L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustList_OpenCount =
          new NodeId(UShort.MIN, uint(16713L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustList_MimeType =
          new NodeId(UShort.MIN, uint(16714L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustList_MaxByteStringLength =
          new NodeId(UShort.MIN, uint(16715L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustList_LastModifiedTime =
          new NodeId(UShort.MIN, uint(16716L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustList_Open =
          new NodeId(UShort.MIN, uint(16717L));

  public static final NodeId AddConnectionMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(16718L));

  public static final NodeId AddConnectionMethodType_OutputArguments =
      new NodeId(UShort.MIN, uint(16719L));

  public static final NodeId PublishedDataSetType_DataSetWriterName_Placeholder_DataSetWriterId =
      new NodeId(UShort.MIN, uint(16720L));

  public static final NodeId
      PublishedDataSetType_DataSetWriterName_Placeholder_DataSetFieldContentMask =
          new NodeId(UShort.MIN, uint(16721L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustList_Open_InputArguments =
          new NodeId(UShort.MIN, uint(16722L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustList_Open_OutputArguments =
          new NodeId(UShort.MIN, uint(16723L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustList_Close =
          new NodeId(UShort.MIN, uint(16724L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustList_Close_InputArguments =
          new NodeId(UShort.MIN, uint(16725L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustList_Read =
          new NodeId(UShort.MIN, uint(16726L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustList_Read_InputArguments =
          new NodeId(UShort.MIN, uint(16727L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustList_Read_OutputArguments =
          new NodeId(UShort.MIN, uint(16728L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustList_Write =
          new NodeId(UShort.MIN, uint(16729L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustList_Write_InputArguments =
          new NodeId(UShort.MIN, uint(16730L));

  public static final NodeId PublishedDataSetType_DataSetWriterName_Placeholder_KeyFrameCount =
      new NodeId(UShort.MIN, uint(16731L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustList_GetPosition =
          new NodeId(UShort.MIN, uint(16732L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustList_GetPosition_InputArguments =
          new NodeId(UShort.MIN, uint(16733L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustList_GetPosition_OutputArguments =
          new NodeId(UShort.MIN, uint(16734L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustList_SetPosition =
          new NodeId(UShort.MIN, uint(16735L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustList_SetPosition_InputArguments =
          new NodeId(UShort.MIN, uint(16736L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustList_LastUpdateTime =
          new NodeId(UShort.MIN, uint(16737L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustList_UpdateFrequency =
          new NodeId(UShort.MIN, uint(16738L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustList_ActivityTimeout =
          new NodeId(UShort.MIN, uint(16739L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustList_DefaultValidationOptions =
          new NodeId(UShort.MIN, uint(16740L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustList_OpenWithMasks =
          new NodeId(UShort.MIN, uint(16741L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustList_OpenWithMasks_InputArguments =
          new NodeId(UShort.MIN, uint(16742L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustList_OpenWithMasks_OutputArguments =
          new NodeId(UShort.MIN, uint(16743L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustList_CloseAndUpdate =
          new NodeId(UShort.MIN, uint(16744L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustList_CloseAndUpdate_InputArguments =
          new NodeId(UShort.MIN, uint(16745L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustList_CloseAndUpdate_OutputArguments =
          new NodeId(UShort.MIN, uint(16746L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustList_AddCertificate =
          new NodeId(UShort.MIN, uint(16747L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustList_AddCertificate_InputArguments =
          new NodeId(UShort.MIN, uint(16748L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustList_RemoveCertificate =
          new NodeId(UShort.MIN, uint(16749L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_TrustList_RemoveCertificate_InputArguments =
          new NodeId(UShort.MIN, uint(16750L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateTypes =
          new NodeId(UShort.MIN, uint(16751L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_GetRejectedList =
          new NodeId(UShort.MIN, uint(16752L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_GetRejectedList_OutputArguments =
          new NodeId(UShort.MIN, uint(16753L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired =
          new NodeId(UShort.MIN, uint(16754L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_EventId =
          new NodeId(UShort.MIN, uint(16755L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_EventType =
          new NodeId(UShort.MIN, uint(16756L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_SourceNode =
          new NodeId(UShort.MIN, uint(16757L));

  public static final NodeId PublishedDataSetType_DataSetWriterName_Placeholder_MessageSettings =
      new NodeId(UShort.MIN, uint(16758L));

  public static final NodeId PublishedDataSetType_DataSetClassId =
      new NodeId(UShort.MIN, uint(16759L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_SourceName =
          new NodeId(UShort.MIN, uint(16760L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_Time =
          new NodeId(UShort.MIN, uint(16761L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ReceiveTime =
          new NodeId(UShort.MIN, uint(16762L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_LocalTime =
          new NodeId(UShort.MIN, uint(16763L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_Message =
          new NodeId(UShort.MIN, uint(16764L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_Severity =
          new NodeId(UShort.MIN, uint(16765L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ConditionClassId =
          new NodeId(UShort.MIN, uint(16766L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ConditionClassName =
          new NodeId(UShort.MIN, uint(16767L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ConditionSubClassId =
          new NodeId(UShort.MIN, uint(16768L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ConditionSubClassName =
          new NodeId(UShort.MIN, uint(16769L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ConditionName =
          new NodeId(UShort.MIN, uint(16770L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_BranchId =
          new NodeId(UShort.MIN, uint(16771L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_Retain =
          new NodeId(UShort.MIN, uint(16772L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_EnabledState =
          new NodeId(UShort.MIN, uint(16773L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_EnabledState_Id =
          new NodeId(UShort.MIN, uint(16774L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_EnabledState_Name =
          new NodeId(UShort.MIN, uint(16775L));

  public static final NodeId NonExclusiveDeviationAlarmType_BaseSetpointNode =
      new NodeId(UShort.MIN, uint(16776L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_EnabledState_Number =
          new NodeId(UShort.MIN, uint(16777L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_EnabledState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(16778L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_EnabledState_TransitionTime =
          new NodeId(UShort.MIN, uint(16779L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_EnabledState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(16780L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_EnabledState_TrueState =
          new NodeId(UShort.MIN, uint(16781L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_EnabledState_FalseState =
          new NodeId(UShort.MIN, uint(16782L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_Quality =
          new NodeId(UShort.MIN, uint(16783L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_Quality_SourceTimestamp =
          new NodeId(UShort.MIN, uint(16784L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_LastSeverity =
          new NodeId(UShort.MIN, uint(16785L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_LastSeverity_SourceTimestamp =
          new NodeId(UShort.MIN, uint(16786L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_Comment =
          new NodeId(UShort.MIN, uint(16787L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_Comment_SourceTimestamp =
          new NodeId(UShort.MIN, uint(16788L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ClientUserId =
          new NodeId(UShort.MIN, uint(16789L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_Disable =
          new NodeId(UShort.MIN, uint(16790L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_Enable =
          new NodeId(UShort.MIN, uint(16791L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_AddComment =
          new NodeId(UShort.MIN, uint(16792L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_AddComment_InputArguments =
          new NodeId(UShort.MIN, uint(16793L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_AckedState =
          new NodeId(UShort.MIN, uint(16794L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_AckedState_Id =
          new NodeId(UShort.MIN, uint(16795L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_AckedState_Name =
          new NodeId(UShort.MIN, uint(16796L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_AckedState_Number =
          new NodeId(UShort.MIN, uint(16797L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_AckedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(16798L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_AckedState_TransitionTime =
          new NodeId(UShort.MIN, uint(16799L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_AckedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(16800L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_AckedState_TrueState =
          new NodeId(UShort.MIN, uint(16801L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_AckedState_FalseState =
          new NodeId(UShort.MIN, uint(16802L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ConfirmedState =
          new NodeId(UShort.MIN, uint(16803L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ConfirmedState_Id =
          new NodeId(UShort.MIN, uint(16804L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ConfirmedState_Name =
          new NodeId(UShort.MIN, uint(16805L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ConfirmedState_Number =
          new NodeId(UShort.MIN, uint(16806L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ConfirmedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(16807L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ConfirmedState_TransitionTime =
          new NodeId(UShort.MIN, uint(16808L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ConfirmedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(16809L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ConfirmedState_TrueState =
          new NodeId(UShort.MIN, uint(16810L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ConfirmedState_FalseState =
          new NodeId(UShort.MIN, uint(16811L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_Acknowledge =
          new NodeId(UShort.MIN, uint(16812L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_Acknowledge_InputArguments =
          new NodeId(UShort.MIN, uint(16813L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_Confirm =
          new NodeId(UShort.MIN, uint(16814L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_Confirm_InputArguments =
          new NodeId(UShort.MIN, uint(16815L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ActiveState =
          new NodeId(UShort.MIN, uint(16816L));

  public static final NodeId ExclusiveDeviationAlarmType_BaseSetpointNode =
      new NodeId(UShort.MIN, uint(16817L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ActiveState_Id =
          new NodeId(UShort.MIN, uint(16818L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ActiveState_Name =
          new NodeId(UShort.MIN, uint(16819L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ActiveState_Number =
          new NodeId(UShort.MIN, uint(16820L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ActiveState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(16821L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ActiveState_TransitionTime =
          new NodeId(UShort.MIN, uint(16822L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ActiveState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(16823L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ActiveState_TrueState =
          new NodeId(UShort.MIN, uint(16824L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ActiveState_FalseState =
          new NodeId(UShort.MIN, uint(16825L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_InputNode =
          new NodeId(UShort.MIN, uint(16826L));

  public static final NodeId
      ApplicationConfigurationFolderType_ApplicationName_Placeholder_CertificateGroups_DefaultApplicationGroup_CertificateExpired_SuppressedState =
          new NodeId(UShort.MIN, uint(16827L));
}

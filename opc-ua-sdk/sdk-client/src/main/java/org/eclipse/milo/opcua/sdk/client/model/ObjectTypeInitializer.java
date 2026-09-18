package org.eclipse.milo.opcua.sdk.client.model;

import org.eclipse.milo.opcua.sdk.client.ObjectTypeManager;
import org.eclipse.milo.opcua.sdk.client.model.objects.AcknowledgeableConditionType;
import org.eclipse.milo.opcua.sdk.client.model.objects.AcknowledgeableConditionTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AddressSpaceFileType;
import org.eclipse.milo.opcua.sdk.client.model.objects.AddressSpaceFileTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AggregateConfigurationType;
import org.eclipse.milo.opcua.sdk.client.model.objects.AggregateConfigurationTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AggregateFunctionType;
import org.eclipse.milo.opcua.sdk.client.model.objects.AggregateFunctionTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AlarmConditionType;
import org.eclipse.milo.opcua.sdk.client.model.objects.AlarmConditionTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AlarmGroupType;
import org.eclipse.milo.opcua.sdk.client.model.objects.AlarmGroupTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AlarmMetricsType;
import org.eclipse.milo.opcua.sdk.client.model.objects.AlarmMetricsTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AlarmSuppressionGroupType;
import org.eclipse.milo.opcua.sdk.client.model.objects.AlarmSuppressionGroupTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AliasNameCategoryType;
import org.eclipse.milo.opcua.sdk.client.model.objects.AliasNameCategoryTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AliasNameType;
import org.eclipse.milo.opcua.sdk.client.model.objects.AliasNameTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AlternativeUnitType;
import org.eclipse.milo.opcua.sdk.client.model.objects.AlternativeUnitTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.ApplicationCertificateType;
import org.eclipse.milo.opcua.sdk.client.model.objects.ApplicationCertificateTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.ApplicationConfigurationFileType;
import org.eclipse.milo.opcua.sdk.client.model.objects.ApplicationConfigurationFileTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.ApplicationConfigurationFolderType;
import org.eclipse.milo.opcua.sdk.client.model.objects.ApplicationConfigurationFolderTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.ApplicationConfigurationType;
import org.eclipse.milo.opcua.sdk.client.model.objects.ApplicationConfigurationTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditActivateSessionEventType;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditActivateSessionEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditAddNodesEventType;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditAddNodesEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditAddReferencesEventType;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditAddReferencesEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditCancelEventType;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditCancelEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditCertificateDataMismatchEventType;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditCertificateDataMismatchEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditCertificateEventType;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditCertificateEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditCertificateExpiredEventType;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditCertificateExpiredEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditCertificateInvalidEventType;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditCertificateInvalidEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditCertificateMismatchEventType;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditCertificateMismatchEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditCertificateRevokedEventType;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditCertificateRevokedEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditCertificateUntrustedEventType;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditCertificateUntrustedEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditChannelEventType;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditChannelEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditClientEventType;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditClientEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditClientUpdateMethodResultEventType;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditClientUpdateMethodResultEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditConditionAcknowledgeEventType;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditConditionAcknowledgeEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditConditionCommentEventType;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditConditionCommentEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditConditionConfirmEventType;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditConditionConfirmEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditConditionEnableEventType;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditConditionEnableEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditConditionEventType;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditConditionEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditConditionOutOfServiceEventType;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditConditionOutOfServiceEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditConditionResetEventType;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditConditionResetEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditConditionRespondEventType;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditConditionRespondEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditConditionShelvingEventType;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditConditionShelvingEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditConditionSilenceEventType;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditConditionSilenceEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditConditionSuppressionEventType;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditConditionSuppressionEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditCreateSessionEventType;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditCreateSessionEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditDeleteNodesEventType;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditDeleteNodesEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditDeleteReferencesEventType;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditDeleteReferencesEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditEventType;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditHistoryAnnotationUpdateEventType;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditHistoryAnnotationUpdateEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditHistoryAtTimeDeleteEventType;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditHistoryAtTimeDeleteEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditHistoryBulkInsertEventType;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditHistoryBulkInsertEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditHistoryConfigurationChangeEventType;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditHistoryConfigurationChangeEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditHistoryDeleteEventType;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditHistoryDeleteEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditHistoryEventDeleteEventType;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditHistoryEventDeleteEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditHistoryEventUpdateEventType;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditHistoryEventUpdateEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditHistoryRawModifyDeleteEventType;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditHistoryRawModifyDeleteEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditHistoryUpdateEventType;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditHistoryUpdateEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditHistoryValueUpdateEventType;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditHistoryValueUpdateEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditNodeManagementEventType;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditNodeManagementEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditOpenSecureChannelEventType;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditOpenSecureChannelEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditProgramTransitionEventType;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditProgramTransitionEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditSecurityEventType;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditSecurityEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditSessionEventType;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditSessionEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditUpdateEventType;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditUpdateEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditUpdateMethodEventType;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditUpdateMethodEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditUpdateStateEventType;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditUpdateStateEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditUrlMismatchEventType;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditUrlMismatchEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditWriteUpdateEventType;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditWriteUpdateEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuthorizationServiceConfigurationType;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuthorizationServiceConfigurationTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuthorizationServicesConfigurationFolderType;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuthorizationServicesConfigurationFolderTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.BaseConditionClassType;
import org.eclipse.milo.opcua.sdk.client.model.objects.BaseConditionClassTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.BaseEventType;
import org.eclipse.milo.opcua.sdk.client.model.objects.BaseEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.BaseInterfaceType;
import org.eclipse.milo.opcua.sdk.client.model.objects.BaseInterfaceTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.BaseLogEventType;
import org.eclipse.milo.opcua.sdk.client.model.objects.BaseLogEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.BaseModelChangeEventType;
import org.eclipse.milo.opcua.sdk.client.model.objects.BaseModelChangeEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.BrokerConnectionTransportType;
import org.eclipse.milo.opcua.sdk.client.model.objects.BrokerConnectionTransportTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.BrokerDataSetReaderTransportType;
import org.eclipse.milo.opcua.sdk.client.model.objects.BrokerDataSetReaderTransportTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.BrokerDataSetWriterTransportType;
import org.eclipse.milo.opcua.sdk.client.model.objects.BrokerDataSetWriterTransportTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.BrokerWriterGroupTransportType;
import org.eclipse.milo.opcua.sdk.client.model.objects.BrokerWriterGroupTransportTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.CertificateExpirationAlarmType;
import org.eclipse.milo.opcua.sdk.client.model.objects.CertificateExpirationAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.CertificateGroupFolderType;
import org.eclipse.milo.opcua.sdk.client.model.objects.CertificateGroupFolderTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.CertificateGroupType;
import org.eclipse.milo.opcua.sdk.client.model.objects.CertificateGroupTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.CertificateType;
import org.eclipse.milo.opcua.sdk.client.model.objects.CertificateTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.CertificateUpdateRequestedAuditEventType;
import org.eclipse.milo.opcua.sdk.client.model.objects.CertificateUpdateRequestedAuditEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.CertificateUpdatedAuditEventType;
import org.eclipse.milo.opcua.sdk.client.model.objects.CertificateUpdatedAuditEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.ChoiceStateType;
import org.eclipse.milo.opcua.sdk.client.model.objects.ChoiceStateTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.ConditionType;
import org.eclipse.milo.opcua.sdk.client.model.objects.ConditionTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.ConfigurationFileType;
import org.eclipse.milo.opcua.sdk.client.model.objects.ConfigurationFileTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.ConfigurationUpdatedAuditEventType;
import org.eclipse.milo.opcua.sdk.client.model.objects.ConfigurationUpdatedAuditEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.ConnectionTransportType;
import org.eclipse.milo.opcua.sdk.client.model.objects.ConnectionTransportTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.DataSetFolderType;
import org.eclipse.milo.opcua.sdk.client.model.objects.DataSetFolderTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.DataSetReaderMessageType;
import org.eclipse.milo.opcua.sdk.client.model.objects.DataSetReaderMessageTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.DataSetReaderTransportType;
import org.eclipse.milo.opcua.sdk.client.model.objects.DataSetReaderTransportTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.DataSetReaderType;
import org.eclipse.milo.opcua.sdk.client.model.objects.DataSetReaderTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.DataSetWriterMessageType;
import org.eclipse.milo.opcua.sdk.client.model.objects.DataSetWriterMessageTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.DataSetWriterTransportType;
import org.eclipse.milo.opcua.sdk.client.model.objects.DataSetWriterTransportTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.DataSetWriterType;
import org.eclipse.milo.opcua.sdk.client.model.objects.DataSetWriterTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.DataTypeEncodingType;
import org.eclipse.milo.opcua.sdk.client.model.objects.DataTypeEncodingTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.DataTypeRefinementType;
import org.eclipse.milo.opcua.sdk.client.model.objects.DataTypeRefinementTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.DataTypeSystemType;
import org.eclipse.milo.opcua.sdk.client.model.objects.DataTypeSystemTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.DatagramConnectionTransportType;
import org.eclipse.milo.opcua.sdk.client.model.objects.DatagramConnectionTransportTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.DatagramDataSetReaderTransportType;
import org.eclipse.milo.opcua.sdk.client.model.objects.DatagramDataSetReaderTransportTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.DatagramWriterGroupTransportType;
import org.eclipse.milo.opcua.sdk.client.model.objects.DatagramWriterGroupTransportTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.DeviceFailureEventType;
import org.eclipse.milo.opcua.sdk.client.model.objects.DeviceFailureEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.DialogConditionType;
import org.eclipse.milo.opcua.sdk.client.model.objects.DialogConditionTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.DictionaryEntryType;
import org.eclipse.milo.opcua.sdk.client.model.objects.DictionaryEntryTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.DictionaryFolderType;
import org.eclipse.milo.opcua.sdk.client.model.objects.DictionaryFolderTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.DiscrepancyAlarmType;
import org.eclipse.milo.opcua.sdk.client.model.objects.DiscrepancyAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.DiscreteAlarmType;
import org.eclipse.milo.opcua.sdk.client.model.objects.DiscreteAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.EccApplicationCertificateType;
import org.eclipse.milo.opcua.sdk.client.model.objects.EccApplicationCertificateTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.EccBrainpoolP256r1ApplicationCertificateType;
import org.eclipse.milo.opcua.sdk.client.model.objects.EccBrainpoolP256r1ApplicationCertificateTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.EccBrainpoolP384r1ApplicationCertificateType;
import org.eclipse.milo.opcua.sdk.client.model.objects.EccBrainpoolP384r1ApplicationCertificateTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.EccCurve25519ApplicationCertificateType;
import org.eclipse.milo.opcua.sdk.client.model.objects.EccCurve25519ApplicationCertificateTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.EccCurve448ApplicationCertificateType;
import org.eclipse.milo.opcua.sdk.client.model.objects.EccCurve448ApplicationCertificateTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.EccNistP256ApplicationCertificateType;
import org.eclipse.milo.opcua.sdk.client.model.objects.EccNistP256ApplicationCertificateTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.EccNistP384ApplicationCertificateType;
import org.eclipse.milo.opcua.sdk.client.model.objects.EccNistP384ApplicationCertificateTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.EventQueueOverflowEventType;
import org.eclipse.milo.opcua.sdk.client.model.objects.EventQueueOverflowEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.ExclusiveDeviationAlarmType;
import org.eclipse.milo.opcua.sdk.client.model.objects.ExclusiveDeviationAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.ExclusiveLevelAlarmType;
import org.eclipse.milo.opcua.sdk.client.model.objects.ExclusiveLevelAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.ExclusiveLimitAlarmType;
import org.eclipse.milo.opcua.sdk.client.model.objects.ExclusiveLimitAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.ExclusiveLimitStateMachineType;
import org.eclipse.milo.opcua.sdk.client.model.objects.ExclusiveLimitStateMachineTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.ExclusiveRateOfChangeAlarmType;
import org.eclipse.milo.opcua.sdk.client.model.objects.ExclusiveRateOfChangeAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.ExtensionFieldsType;
import org.eclipse.milo.opcua.sdk.client.model.objects.ExtensionFieldsTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.FileDirectoryType;
import org.eclipse.milo.opcua.sdk.client.model.objects.FileDirectoryTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.FileTransferStateMachineType;
import org.eclipse.milo.opcua.sdk.client.model.objects.FileTransferStateMachineTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.FileType;
import org.eclipse.milo.opcua.sdk.client.model.objects.FileTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.FiniteStateMachineType;
import org.eclipse.milo.opcua.sdk.client.model.objects.FiniteStateMachineTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.FolderType;
import org.eclipse.milo.opcua.sdk.client.model.objects.FolderTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.GeneralModelChangeEventType;
import org.eclipse.milo.opcua.sdk.client.model.objects.GeneralModelChangeEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.HighlyManagedAlarmConditionClassType;
import org.eclipse.milo.opcua.sdk.client.model.objects.HighlyManagedAlarmConditionClassTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.HistoricalDataConfigurationType;
import org.eclipse.milo.opcua.sdk.client.model.objects.HistoricalDataConfigurationTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.HistoricalEventConfigurationType;
import org.eclipse.milo.opcua.sdk.client.model.objects.HistoricalEventConfigurationTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.HistoricalExternalEventSourceType;
import org.eclipse.milo.opcua.sdk.client.model.objects.HistoricalExternalEventSourceTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.HistoryServerCapabilitiesType;
import org.eclipse.milo.opcua.sdk.client.model.objects.HistoryServerCapabilitiesTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.HttpsCertificateType;
import org.eclipse.milo.opcua.sdk.client.model.objects.HttpsCertificateTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.IBaseEthernetCapabilitiesType;
import org.eclipse.milo.opcua.sdk.client.model.objects.IBaseEthernetCapabilitiesTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.IIeeeAutoNegotiationStatusType;
import org.eclipse.milo.opcua.sdk.client.model.objects.IIeeeAutoNegotiationStatusTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.IIeeeBaseEthernetPortType;
import org.eclipse.milo.opcua.sdk.client.model.objects.IIeeeBaseEthernetPortTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.IIeeeBaseTsnStatusStreamType;
import org.eclipse.milo.opcua.sdk.client.model.objects.IIeeeBaseTsnStatusStreamTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.IIeeeBaseTsnStreamType;
import org.eclipse.milo.opcua.sdk.client.model.objects.IIeeeBaseTsnStreamTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.IIeeeBaseTsnTrafficSpecificationType;
import org.eclipse.milo.opcua.sdk.client.model.objects.IIeeeBaseTsnTrafficSpecificationTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.IIeeeTsnInterfaceConfigurationListenerType;
import org.eclipse.milo.opcua.sdk.client.model.objects.IIeeeTsnInterfaceConfigurationListenerTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.IIeeeTsnInterfaceConfigurationTalkerType;
import org.eclipse.milo.opcua.sdk.client.model.objects.IIeeeTsnInterfaceConfigurationTalkerTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.IIeeeTsnInterfaceConfigurationType;
import org.eclipse.milo.opcua.sdk.client.model.objects.IIeeeTsnInterfaceConfigurationTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.IIeeeTsnMacAddressType;
import org.eclipse.milo.opcua.sdk.client.model.objects.IIeeeTsnMacAddressTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.IIeeeTsnVlanTagType;
import org.eclipse.milo.opcua.sdk.client.model.objects.IIeeeTsnVlanTagTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.IIetfBaseNetworkInterfaceType;
import org.eclipse.milo.opcua.sdk.client.model.objects.IIetfBaseNetworkInterfaceTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.IOrderedObjectType;
import org.eclipse.milo.opcua.sdk.client.model.objects.IOrderedObjectTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.IPriorityMappingEntryType;
import org.eclipse.milo.opcua.sdk.client.model.objects.IPriorityMappingEntryTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.ISrClassType;
import org.eclipse.milo.opcua.sdk.client.model.objects.ISrClassTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.IVlanIdType;
import org.eclipse.milo.opcua.sdk.client.model.objects.IVlanIdTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.IetfBaseNetworkInterfaceType;
import org.eclipse.milo.opcua.sdk.client.model.objects.IetfBaseNetworkInterfaceTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.InitialStateType;
import org.eclipse.milo.opcua.sdk.client.model.objects.InitialStateTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.InstrumentDiagnosticAlarmType;
import org.eclipse.milo.opcua.sdk.client.model.objects.InstrumentDiagnosticAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.IrdiDictionaryEntryType;
import org.eclipse.milo.opcua.sdk.client.model.objects.IrdiDictionaryEntryTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.JsonDataSetReaderMessageType;
import org.eclipse.milo.opcua.sdk.client.model.objects.JsonDataSetReaderMessageTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.JsonDataSetWriterMessageType;
import org.eclipse.milo.opcua.sdk.client.model.objects.JsonDataSetWriterMessageTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.JsonWriterGroupMessageType;
import org.eclipse.milo.opcua.sdk.client.model.objects.JsonWriterGroupMessageTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.KeyCredentialAuditEventType;
import org.eclipse.milo.opcua.sdk.client.model.objects.KeyCredentialAuditEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.KeyCredentialConfigurationFolderType;
import org.eclipse.milo.opcua.sdk.client.model.objects.KeyCredentialConfigurationFolderTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.KeyCredentialConfigurationType;
import org.eclipse.milo.opcua.sdk.client.model.objects.KeyCredentialConfigurationTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.KeyCredentialDeletedAuditEventType;
import org.eclipse.milo.opcua.sdk.client.model.objects.KeyCredentialDeletedAuditEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.KeyCredentialUpdatedAuditEventType;
import org.eclipse.milo.opcua.sdk.client.model.objects.KeyCredentialUpdatedAuditEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.LimitAlarmType;
import org.eclipse.milo.opcua.sdk.client.model.objects.LimitAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.LldpInformationType;
import org.eclipse.milo.opcua.sdk.client.model.objects.LldpInformationTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.LldpLocalSystemType;
import org.eclipse.milo.opcua.sdk.client.model.objects.LldpLocalSystemTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.LldpPortInformationType;
import org.eclipse.milo.opcua.sdk.client.model.objects.LldpPortInformationTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.LldpRemoteStatisticsType;
import org.eclipse.milo.opcua.sdk.client.model.objects.LldpRemoteStatisticsTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.LldpRemoteSystemType;
import org.eclipse.milo.opcua.sdk.client.model.objects.LldpRemoteSystemTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.LogEntryConditionClassType;
import org.eclipse.milo.opcua.sdk.client.model.objects.LogEntryConditionClassTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.LogObjectType;
import org.eclipse.milo.opcua.sdk.client.model.objects.LogObjectTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.LogOverflowEventType;
import org.eclipse.milo.opcua.sdk.client.model.objects.LogOverflowEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.MaintenanceConditionClassType;
import org.eclipse.milo.opcua.sdk.client.model.objects.MaintenanceConditionClassTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.ModellingRuleType;
import org.eclipse.milo.opcua.sdk.client.model.objects.ModellingRuleTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.NamespaceMetadataType;
import org.eclipse.milo.opcua.sdk.client.model.objects.NamespaceMetadataTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.NamespacesType;
import org.eclipse.milo.opcua.sdk.client.model.objects.NamespacesTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.NetworkAddressType;
import org.eclipse.milo.opcua.sdk.client.model.objects.NetworkAddressTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.NetworkAddressUrlType;
import org.eclipse.milo.opcua.sdk.client.model.objects.NetworkAddressUrlTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.NonExclusiveDeviationAlarmType;
import org.eclipse.milo.opcua.sdk.client.model.objects.NonExclusiveDeviationAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.NonExclusiveLevelAlarmType;
import org.eclipse.milo.opcua.sdk.client.model.objects.NonExclusiveLevelAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.NonExclusiveLimitAlarmType;
import org.eclipse.milo.opcua.sdk.client.model.objects.NonExclusiveLimitAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.NonExclusiveRateOfChangeAlarmType;
import org.eclipse.milo.opcua.sdk.client.model.objects.NonExclusiveRateOfChangeAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.NonTransparentBackupRedundancyType;
import org.eclipse.milo.opcua.sdk.client.model.objects.NonTransparentBackupRedundancyTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.NonTransparentNetworkRedundancyType;
import org.eclipse.milo.opcua.sdk.client.model.objects.NonTransparentNetworkRedundancyTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.NonTransparentRedundancyType;
import org.eclipse.milo.opcua.sdk.client.model.objects.NonTransparentRedundancyTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.OffNormalAlarmType;
import org.eclipse.milo.opcua.sdk.client.model.objects.OffNormalAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.OperationLimitsType;
import org.eclipse.milo.opcua.sdk.client.model.objects.OperationLimitsTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.OrderedListType;
import org.eclipse.milo.opcua.sdk.client.model.objects.OrderedListTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.PriorityMappingTableType;
import org.eclipse.milo.opcua.sdk.client.model.objects.PriorityMappingTableTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.ProcessConditionClassType;
import org.eclipse.milo.opcua.sdk.client.model.objects.ProcessConditionClassTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.ProgramStateMachineType;
import org.eclipse.milo.opcua.sdk.client.model.objects.ProgramStateMachineTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.ProgramTransitionAuditEventType;
import org.eclipse.milo.opcua.sdk.client.model.objects.ProgramTransitionAuditEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.ProgramTransitionEventType;
import org.eclipse.milo.opcua.sdk.client.model.objects.ProgramTransitionEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.ProgressEventType;
import org.eclipse.milo.opcua.sdk.client.model.objects.ProgressEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.ProvisionableDeviceType;
import org.eclipse.milo.opcua.sdk.client.model.objects.ProvisionableDeviceTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.PubSubCapabilitiesType;
import org.eclipse.milo.opcua.sdk.client.model.objects.PubSubCapabilitiesTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.PubSubCommunicationFailureEventType;
import org.eclipse.milo.opcua.sdk.client.model.objects.PubSubCommunicationFailureEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.PubSubConfigurationType;
import org.eclipse.milo.opcua.sdk.client.model.objects.PubSubConfigurationTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.PubSubConnectionType;
import org.eclipse.milo.opcua.sdk.client.model.objects.PubSubConnectionTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.PubSubDiagnosticsConnectionType;
import org.eclipse.milo.opcua.sdk.client.model.objects.PubSubDiagnosticsConnectionTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.PubSubDiagnosticsDataSetReaderType;
import org.eclipse.milo.opcua.sdk.client.model.objects.PubSubDiagnosticsDataSetReaderTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.PubSubDiagnosticsDataSetWriterType;
import org.eclipse.milo.opcua.sdk.client.model.objects.PubSubDiagnosticsDataSetWriterTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.PubSubDiagnosticsReaderGroupType;
import org.eclipse.milo.opcua.sdk.client.model.objects.PubSubDiagnosticsReaderGroupTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.PubSubDiagnosticsRootType;
import org.eclipse.milo.opcua.sdk.client.model.objects.PubSubDiagnosticsRootTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.PubSubDiagnosticsType;
import org.eclipse.milo.opcua.sdk.client.model.objects.PubSubDiagnosticsTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.PubSubDiagnosticsWriterGroupType;
import org.eclipse.milo.opcua.sdk.client.model.objects.PubSubDiagnosticsWriterGroupTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.PubSubGroupType;
import org.eclipse.milo.opcua.sdk.client.model.objects.PubSubGroupTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.PubSubKeyPushTargetFolderType;
import org.eclipse.milo.opcua.sdk.client.model.objects.PubSubKeyPushTargetFolderTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.PubSubKeyPushTargetType;
import org.eclipse.milo.opcua.sdk.client.model.objects.PubSubKeyPushTargetTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.PubSubKeyServiceType;
import org.eclipse.milo.opcua.sdk.client.model.objects.PubSubKeyServiceTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.PubSubStatusEventType;
import org.eclipse.milo.opcua.sdk.client.model.objects.PubSubStatusEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.PubSubStatusType;
import org.eclipse.milo.opcua.sdk.client.model.objects.PubSubStatusTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.PubSubTransportLimitsExceedEventType;
import org.eclipse.milo.opcua.sdk.client.model.objects.PubSubTransportLimitsExceedEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.PublishSubscribeType;
import org.eclipse.milo.opcua.sdk.client.model.objects.PublishSubscribeTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.PublishedDataItemsType;
import org.eclipse.milo.opcua.sdk.client.model.objects.PublishedDataItemsTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.PublishedDataSetType;
import org.eclipse.milo.opcua.sdk.client.model.objects.PublishedDataSetTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.PublishedEventsType;
import org.eclipse.milo.opcua.sdk.client.model.objects.PublishedEventsTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.QuantityType;
import org.eclipse.milo.opcua.sdk.client.model.objects.QuantityTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.ReaderGroupMessageType;
import org.eclipse.milo.opcua.sdk.client.model.objects.ReaderGroupMessageTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.ReaderGroupTransportType;
import org.eclipse.milo.opcua.sdk.client.model.objects.ReaderGroupTransportTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.ReaderGroupType;
import org.eclipse.milo.opcua.sdk.client.model.objects.ReaderGroupTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.RefreshEndEventType;
import org.eclipse.milo.opcua.sdk.client.model.objects.RefreshEndEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.RefreshRequiredEventType;
import org.eclipse.milo.opcua.sdk.client.model.objects.RefreshRequiredEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.RefreshStartEventType;
import org.eclipse.milo.opcua.sdk.client.model.objects.RefreshStartEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.RoleMappingRuleChangedAuditEventType;
import org.eclipse.milo.opcua.sdk.client.model.objects.RoleMappingRuleChangedAuditEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.RoleSetType;
import org.eclipse.milo.opcua.sdk.client.model.objects.RoleSetTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.RoleType;
import org.eclipse.milo.opcua.sdk.client.model.objects.RoleTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.RsaMinApplicationCertificateType;
import org.eclipse.milo.opcua.sdk.client.model.objects.RsaMinApplicationCertificateTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.RsaSha256ApplicationCertificateType;
import org.eclipse.milo.opcua.sdk.client.model.objects.RsaSha256ApplicationCertificateTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.SafetyConditionClassType;
import org.eclipse.milo.opcua.sdk.client.model.objects.SafetyConditionClassTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.SecurityGroupFolderType;
import org.eclipse.milo.opcua.sdk.client.model.objects.SecurityGroupFolderTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.SecurityGroupType;
import org.eclipse.milo.opcua.sdk.client.model.objects.SecurityGroupTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.SemanticChangeEventType;
import org.eclipse.milo.opcua.sdk.client.model.objects.SemanticChangeEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.SerializationEntityType;
import org.eclipse.milo.opcua.sdk.client.model.objects.SerializationEntityTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.ServerCapabilitiesType;
import org.eclipse.milo.opcua.sdk.client.model.objects.ServerCapabilitiesTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.ServerConfigurationType;
import org.eclipse.milo.opcua.sdk.client.model.objects.ServerConfigurationTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.ServerDiagnosticsType;
import org.eclipse.milo.opcua.sdk.client.model.objects.ServerDiagnosticsTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.ServerRedundancyType;
import org.eclipse.milo.opcua.sdk.client.model.objects.ServerRedundancyTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.ServerType;
import org.eclipse.milo.opcua.sdk.client.model.objects.ServerTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.ServerUnitType;
import org.eclipse.milo.opcua.sdk.client.model.objects.ServerUnitTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.SessionDiagnosticsObjectType;
import org.eclipse.milo.opcua.sdk.client.model.objects.SessionDiagnosticsObjectTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.SessionsDiagnosticsSummaryType;
import org.eclipse.milo.opcua.sdk.client.model.objects.SessionsDiagnosticsSummaryTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.ShelvedStateMachineType;
import org.eclipse.milo.opcua.sdk.client.model.objects.ShelvedStateMachineTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.StandaloneSubscribedDataSetType;
import org.eclipse.milo.opcua.sdk.client.model.objects.StandaloneSubscribedDataSetTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.StateMachineType;
import org.eclipse.milo.opcua.sdk.client.model.objects.StateMachineTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.StateType;
import org.eclipse.milo.opcua.sdk.client.model.objects.StateTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.StatisticalConditionClassType;
import org.eclipse.milo.opcua.sdk.client.model.objects.StatisticalConditionClassTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.SubscribedDataSetFolderType;
import org.eclipse.milo.opcua.sdk.client.model.objects.SubscribedDataSetFolderTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.SubscribedDataSetMirrorType;
import org.eclipse.milo.opcua.sdk.client.model.objects.SubscribedDataSetMirrorTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.SubscribedDataSetType;
import org.eclipse.milo.opcua.sdk.client.model.objects.SubscribedDataSetTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.SubtypeRestrictionType;
import org.eclipse.milo.opcua.sdk.client.model.objects.SubtypeRestrictionTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.SyntaxReferenceEntryType;
import org.eclipse.milo.opcua.sdk.client.model.objects.SyntaxReferenceEntryTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.SystemConditionClassType;
import org.eclipse.milo.opcua.sdk.client.model.objects.SystemConditionClassTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.SystemDiagnosticAlarmType;
import org.eclipse.milo.opcua.sdk.client.model.objects.SystemDiagnosticAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.SystemEventType;
import org.eclipse.milo.opcua.sdk.client.model.objects.SystemEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.SystemOffNormalAlarmType;
import org.eclipse.milo.opcua.sdk.client.model.objects.SystemOffNormalAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.SystemStatusChangeEventType;
import org.eclipse.milo.opcua.sdk.client.model.objects.SystemStatusChangeEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.TargetVariablesType;
import org.eclipse.milo.opcua.sdk.client.model.objects.TargetVariablesTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.TemporaryFileTransferType;
import org.eclipse.milo.opcua.sdk.client.model.objects.TemporaryFileTransferTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.TestingConditionClassType;
import org.eclipse.milo.opcua.sdk.client.model.objects.TestingConditionClassTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.TlsCertificateType;
import org.eclipse.milo.opcua.sdk.client.model.objects.TlsCertificateTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.TlsClientCertificateType;
import org.eclipse.milo.opcua.sdk.client.model.objects.TlsClientCertificateTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.TlsServerCertificateType;
import org.eclipse.milo.opcua.sdk.client.model.objects.TlsServerCertificateTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.TrainingConditionClassType;
import org.eclipse.milo.opcua.sdk.client.model.objects.TrainingConditionClassTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.TransactionDiagnosticsType;
import org.eclipse.milo.opcua.sdk.client.model.objects.TransactionDiagnosticsTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.TransitionEventType;
import org.eclipse.milo.opcua.sdk.client.model.objects.TransitionEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.TransitionType;
import org.eclipse.milo.opcua.sdk.client.model.objects.TransitionTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.TransparentRedundancyType;
import org.eclipse.milo.opcua.sdk.client.model.objects.TransparentRedundancyTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.TripAlarmType;
import org.eclipse.milo.opcua.sdk.client.model.objects.TripAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.TrustListOutOfDateAlarmType;
import org.eclipse.milo.opcua.sdk.client.model.objects.TrustListOutOfDateAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.TrustListType;
import org.eclipse.milo.opcua.sdk.client.model.objects.TrustListTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.TrustListUpdateRequestedAuditEventType;
import org.eclipse.milo.opcua.sdk.client.model.objects.TrustListUpdateRequestedAuditEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.TrustListUpdatedAuditEventType;
import org.eclipse.milo.opcua.sdk.client.model.objects.TrustListUpdatedAuditEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.UadpDataSetReaderMessageType;
import org.eclipse.milo.opcua.sdk.client.model.objects.UadpDataSetReaderMessageTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.UadpDataSetWriterMessageType;
import org.eclipse.milo.opcua.sdk.client.model.objects.UadpDataSetWriterMessageTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.UadpWriterGroupMessageType;
import org.eclipse.milo.opcua.sdk.client.model.objects.UadpWriterGroupMessageTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.UnitType;
import org.eclipse.milo.opcua.sdk.client.model.objects.UnitTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.UriDictionaryEntryType;
import org.eclipse.milo.opcua.sdk.client.model.objects.UriDictionaryEntryTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.UserCertificateType;
import org.eclipse.milo.opcua.sdk.client.model.objects.UserCertificateTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.UserManagementType;
import org.eclipse.milo.opcua.sdk.client.model.objects.UserManagementTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.VendorServerInfoType;
import org.eclipse.milo.opcua.sdk.client.model.objects.VendorServerInfoTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.WriterGroupMessageType;
import org.eclipse.milo.opcua.sdk.client.model.objects.WriterGroupMessageTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.WriterGroupTransportType;
import org.eclipse.milo.opcua.sdk.client.model.objects.WriterGroupTransportTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.WriterGroupType;
import org.eclipse.milo.opcua.sdk.client.model.objects.WriterGroupTypeNode;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;

/**
 * Registers constructors for this client's lifetime. Serialize initialization after connecting and
 * before obtaining nodes. Existing cached instances are not upgraded. Registers this library's
 * namespace-zero constructors; initialize before any other standard registration.
 */
public final class ObjectTypeInitializer {
  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_0 =
      BaseEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_1 =
      ConditionTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_2 =
      AcknowledgeableConditionTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_3 =
      AlarmConditionTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_4 =
      LimitAlarmTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_5 =
      NonExclusiveLimitAlarmTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_6 =
      NonExclusiveLevelAlarmTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_7 =
      NonExclusiveRateOfChangeAlarmTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_8 =
      NonExclusiveDeviationAlarmTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_9 =
      DiscreteAlarmTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_10 =
      OffNormalAlarmTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_11 =
      TripAlarmTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_12 =
      AuditEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_13 =
      AuditUpdateMethodEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_14 =
      AuditConditionEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_15 =
      AuditConditionShelvingEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_16 =
      BaseConditionClassTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_17 =
      ProcessConditionClassTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_18 =
      MaintenanceConditionClassTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_19 =
      SystemConditionClassTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_20 =
      AggregateConfigurationTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_21 =
      ProgressEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_22 =
      SystemEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_23 =
      SystemStatusChangeEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_24 = FolderTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_25 =
      OperationLimitsTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_26 = FileTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_27 =
      AddressSpaceFileTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_28 =
      NamespaceMetadataTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_29 =
      NamespacesTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_30 =
      SystemOffNormalAlarmTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_31 =
      AuditUpdateStateEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_32 =
      AuditProgramTransitionEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_33 =
      ServerRedundancyTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_34 =
      NonTransparentRedundancyTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_35 =
      NonTransparentNetworkRedundancyTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_36 =
      TrustListTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_37 =
      CertificateGroupTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_38 =
      CertificateTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_39 =
      ApplicationCertificateTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_40 =
      HttpsCertificateTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_41 =
      RsaMinApplicationCertificateTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_42 =
      RsaSha256ApplicationCertificateTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_43 =
      TrustListUpdatedAuditEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_44 =
      ServerConfigurationTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_45 =
      CertificateUpdatedAuditEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_46 =
      CertificateExpirationAlarmTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_47 =
      FileDirectoryTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_48 =
      CertificateGroupFolderTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_49 =
      PubSubConnectionTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_50 =
      PubSubGroupTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_51 =
      PubSubKeyServiceTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_52 =
      PublishSubscribeTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_53 =
      DataSetFolderTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_54 =
      PublishedDataSetTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_55 =
      PublishedDataItemsTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_56 =
      PublishedEventsTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_57 =
      PubSubStatusTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_58 =
      AuditConditionResetEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_59 =
      ConnectionTransportTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_60 =
      DatagramConnectionTransportTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_61 =
      SubscribedDataSetTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_62 = StateTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_63 =
      ChoiceStateTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_64 =
      TargetVariablesTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_65 =
      SubscribedDataSetMirrorTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_66 =
      BrokerConnectionTransportTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_67 =
      DataSetWriterTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_68 =
      DataSetWriterTransportTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_69 =
      DataSetReaderTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_70 =
      DataSetReaderTransportTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_71 =
      ConfigurationFileTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_72 =
      SecurityGroupFolderTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_73 =
      SecurityGroupTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_74 =
      ExtensionFieldsTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_75 =
      PubSubStatusEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_76 =
      ConfigurationUpdatedAuditEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_77 =
      PubSubTransportLimitsExceedEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_78 =
      ApplicationConfigurationFileTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_79 =
      PubSubCommunicationFailureEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_80 =
      RoleSetTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_81 = RoleTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_82 =
      TemporaryFileTransferTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_83 =
      StateMachineTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_84 =
      FiniteStateMachineTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_85 =
      FileTransferStateMachineTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_86 =
      AlarmGroupTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_87 =
      ApplicationConfigurationFolderTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_88 =
      DiscrepancyAlarmTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_89 =
      SafetyConditionClassTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_90 =
      HighlyManagedAlarmConditionClassTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_91 =
      TrainingConditionClassTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_92 =
      TestingConditionClassTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_93 =
      AuditConditionSuppressionEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_94 =
      AuditConditionSilenceEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_95 =
      AuditConditionOutOfServiceEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_96 =
      AlarmMetricsTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_97 =
      KeyCredentialConfigurationFolderTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_98 =
      DictionaryEntryTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_99 =
      DictionaryFolderTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_100 =
      IrdiDictionaryEntryTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_101 =
      UriDictionaryEntryTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_102 =
      BaseInterfaceTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_103 =
      RoleMappingRuleChangedAuditEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_104 =
      WriterGroupTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_105 =
      AuthorizationServiceConfigurationTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_106 =
      WriterGroupTransportTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_107 =
      WriterGroupMessageTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_108 =
      ReaderGroupTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_109 =
      KeyCredentialConfigurationTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_110 =
      KeyCredentialAuditEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_111 =
      KeyCredentialUpdatedAuditEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_112 =
      KeyCredentialDeletedAuditEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_113 =
      InstrumentDiagnosticAlarmTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_114 =
      SystemDiagnosticAlarmTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_115 =
      StatisticalConditionClassTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_116 =
      LldpInformationTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_117 =
      LldpRemoteStatisticsTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_118 =
      LldpLocalSystemTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_119 =
      LldpPortInformationTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_120 =
      LldpRemoteSystemTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_121 =
      AuditUpdateEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_122 =
      AuditHistoryUpdateEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_123 =
      AuditHistoryAnnotationUpdateEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_124 =
      TrustListOutOfDateAlarmTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_125 =
      UserCertificateTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_126 =
      TlsCertificateTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_127 =
      TlsServerCertificateTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_128 =
      TlsClientCertificateTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_129 =
      LogObjectTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_130 =
      BaseLogEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_131 =
      LogOverflowEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_132 =
      LogEntryConditionClassTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_133 =
      PubSubDiagnosticsTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_134 =
      PubSubDiagnosticsRootTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_135 =
      PubSubDiagnosticsConnectionTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_136 =
      DataTypeRefinementTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_137 =
      SubtypeRestrictionTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_138 =
      SerializationEntityTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_139 =
      PubSubDiagnosticsWriterGroupTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_140 =
      PubSubDiagnosticsReaderGroupTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_141 =
      PubSubDiagnosticsDataSetWriterTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_142 =
      PubSubDiagnosticsDataSetReaderTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_143 =
      ServerTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_144 =
      ServerCapabilitiesTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_145 =
      ServerDiagnosticsTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_146 =
      SessionsDiagnosticsSummaryTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_147 =
      SessionDiagnosticsObjectTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_148 =
      VendorServerInfoTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_149 =
      TransparentRedundancyTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_150 =
      AuditSecurityEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_151 =
      AuditChannelEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_152 =
      AuditOpenSecureChannelEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_153 =
      AuditSessionEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_154 =
      AuditCreateSessionEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_155 =
      AuditActivateSessionEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_156 =
      AuditCancelEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_157 =
      AuditCertificateEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_158 =
      AuditCertificateDataMismatchEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_159 =
      AuditCertificateExpiredEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_160 =
      AuditCertificateInvalidEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_161 =
      AuditCertificateUntrustedEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_162 =
      AuditCertificateRevokedEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_163 =
      AuditCertificateMismatchEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_164 =
      AuditNodeManagementEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_165 =
      AuditAddNodesEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_166 =
      AuditDeleteNodesEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_167 =
      AuditAddReferencesEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_168 =
      AuditDeleteReferencesEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_169 =
      AuditWriteUpdateEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_170 =
      ReaderGroupTransportTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_171 =
      ReaderGroupMessageTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_172 =
      DataSetWriterMessageTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_173 =
      DataSetReaderMessageTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_174 =
      UadpWriterGroupMessageTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_175 =
      UadpDataSetWriterMessageTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_176 =
      UadpDataSetReaderMessageTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_177 =
      JsonWriterGroupMessageTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_178 =
      JsonDataSetWriterMessageTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_179 =
      JsonDataSetReaderMessageTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_180 =
      DatagramWriterGroupTransportTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_181 =
      BrokerWriterGroupTransportTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_182 =
      BrokerDataSetWriterTransportTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_183 =
      BrokerDataSetReaderTransportTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_184 =
      NetworkAddressTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_185 =
      NetworkAddressUrlTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_186 =
      DeviceFailureEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_187 =
      BaseModelChangeEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_188 =
      GeneralModelChangeEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_189 =
      InitialStateTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_190 =
      TransitionTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_191 =
      TransitionEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_192 =
      HistoricalDataConfigurationTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_193 =
      HistoryServerCapabilitiesTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_194 =
      AggregateFunctionTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_195 =
      AliasNameTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_196 =
      AliasNameCategoryTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_197 =
      IOrderedObjectTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_198 =
      OrderedListTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_199 =
      EccApplicationCertificateTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_200 =
      EccNistP256ApplicationCertificateTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_201 =
      EccNistP384ApplicationCertificateTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_202 =
      EccBrainpoolP256r1ApplicationCertificateTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_203 =
      EccBrainpoolP384r1ApplicationCertificateTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_204 =
      EccCurve25519ApplicationCertificateTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_205 =
      EccCurve448ApplicationCertificateTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_206 =
      AuthorizationServicesConfigurationFolderTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_207 =
      AuditClientEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_208 =
      ProgramTransitionEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_209 =
      SubscribedDataSetFolderTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_210 =
      StandaloneSubscribedDataSetTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_211 =
      PubSubCapabilitiesTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_212 =
      ProgramStateMachineTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_213 =
      AuditClientUpdateMethodResultEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_214 =
      DatagramDataSetReaderTransportTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_215 =
      IIetfBaseNetworkInterfaceTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_216 =
      IIeeeBaseEthernetPortTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_217 =
      IBaseEthernetCapabilitiesTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_218 =
      ISrClassTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_219 =
      IIeeeBaseTsnStreamTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_220 =
      IIeeeBaseTsnTrafficSpecificationTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_221 =
      IIeeeBaseTsnStatusStreamTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_222 =
      IIeeeTsnInterfaceConfigurationTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_223 =
      IIeeeTsnInterfaceConfigurationTalkerTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_224 =
      IIeeeTsnInterfaceConfigurationListenerTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_225 =
      IIeeeTsnMacAddressTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_226 =
      IIeeeTsnVlanTagTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_227 =
      IPriorityMappingEntryTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_228 =
      IIeeeAutoNegotiationStatusTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_229 =
      UserManagementTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_230 =
      IVlanIdTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_231 =
      IetfBaseNetworkInterfaceTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_232 =
      PriorityMappingTableTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_233 =
      PubSubKeyPushTargetTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_234 =
      PubSubKeyPushTargetFolderTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_235 =
      PubSubConfigurationTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_236 =
      ApplicationConfigurationTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_237 =
      ProvisionableDeviceTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_238 =
      SemanticChangeEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_239 =
      AuditUrlMismatchEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_240 =
      RefreshStartEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_241 =
      RefreshEndEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_242 =
      RefreshRequiredEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_243 =
      AuditConditionEnableEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_244 =
      AuditConditionCommentEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_245 =
      DialogConditionTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_246 =
      ShelvedStateMachineTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_247 =
      AuditHistoryEventUpdateEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_248 =
      AuditHistoryValueUpdateEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_249 =
      AuditHistoryDeleteEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_250 =
      AuditHistoryRawModifyDeleteEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_251 =
      AuditHistoryAtTimeDeleteEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_252 =
      AuditHistoryEventDeleteEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_253 =
      EventQueueOverflowEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_254 =
      AlarmSuppressionGroupTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_255 =
      TrustListUpdateRequestedAuditEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_256 =
      TransactionDiagnosticsTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_257 =
      CertificateUpdateRequestedAuditEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_258 =
      NonTransparentBackupRedundancyTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_259 =
      SyntaxReferenceEntryTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_260 = UnitTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_261 =
      ServerUnitTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_262 =
      AlternativeUnitTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_263 =
      QuantityTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_264 =
      HistoricalEventConfigurationTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_265 =
      HistoricalExternalEventSourceTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_266 =
      AuditHistoryConfigurationChangeEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_267 =
      AuditHistoryBulkInsertEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_268 =
      ProgramTransitionAuditEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_269 =
      DataTypeSystemTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_270 =
      DataTypeEncodingTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_271 =
      ModellingRuleTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_272 =
      AuditConditionRespondEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_273 =
      AuditConditionAcknowledgeEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_274 =
      AuditConditionConfirmEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_275 =
      ExclusiveLimitStateMachineTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_276 =
      ExclusiveLimitAlarmTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_277 =
      ExclusiveLevelAlarmTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_278 =
      ExclusiveRateOfChangeAlarmTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_279 =
      ExclusiveDeviationAlarmTypeNode::new;

  private ObjectTypeInitializer() {}

  private static void check0(NamespaceTable table, ObjectTypeManager manager, NodeId[] ids) {
    ids[0] = ClientNodeSupport.resolve(table, BaseEventType.TYPE_ID);
    if (manager.getNodeConstructor(ids[0]).filter(c -> c != CONSTRUCTOR_0).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + BaseEventType.TYPE_ID);
    }
    ids[1] = ClientNodeSupport.resolve(table, ConditionType.TYPE_ID);
    if (manager.getNodeConstructor(ids[1]).filter(c -> c != CONSTRUCTOR_1).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + ConditionType.TYPE_ID);
    }
    ids[2] = ClientNodeSupport.resolve(table, AcknowledgeableConditionType.TYPE_ID);
    if (manager.getNodeConstructor(ids[2]).filter(c -> c != CONSTRUCTOR_2).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + AcknowledgeableConditionType.TYPE_ID);
    }
    ids[3] = ClientNodeSupport.resolve(table, AlarmConditionType.TYPE_ID);
    if (manager.getNodeConstructor(ids[3]).filter(c -> c != CONSTRUCTOR_3).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + AlarmConditionType.TYPE_ID);
    }
    ids[4] = ClientNodeSupport.resolve(table, LimitAlarmType.TYPE_ID);
    if (manager.getNodeConstructor(ids[4]).filter(c -> c != CONSTRUCTOR_4).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + LimitAlarmType.TYPE_ID);
    }
    ids[5] = ClientNodeSupport.resolve(table, NonExclusiveLimitAlarmType.TYPE_ID);
    if (manager.getNodeConstructor(ids[5]).filter(c -> c != CONSTRUCTOR_5).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + NonExclusiveLimitAlarmType.TYPE_ID);
    }
    ids[6] = ClientNodeSupport.resolve(table, NonExclusiveLevelAlarmType.TYPE_ID);
    if (manager.getNodeConstructor(ids[6]).filter(c -> c != CONSTRUCTOR_6).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + NonExclusiveLevelAlarmType.TYPE_ID);
    }
    ids[7] = ClientNodeSupport.resolve(table, NonExclusiveRateOfChangeAlarmType.TYPE_ID);
    if (manager.getNodeConstructor(ids[7]).filter(c -> c != CONSTRUCTOR_7).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + NonExclusiveRateOfChangeAlarmType.TYPE_ID);
    }
    ids[8] = ClientNodeSupport.resolve(table, NonExclusiveDeviationAlarmType.TYPE_ID);
    if (manager.getNodeConstructor(ids[8]).filter(c -> c != CONSTRUCTOR_8).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + NonExclusiveDeviationAlarmType.TYPE_ID);
    }
    ids[9] = ClientNodeSupport.resolve(table, DiscreteAlarmType.TYPE_ID);
    if (manager.getNodeConstructor(ids[9]).filter(c -> c != CONSTRUCTOR_9).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + DiscreteAlarmType.TYPE_ID);
    }
    ids[10] = ClientNodeSupport.resolve(table, OffNormalAlarmType.TYPE_ID);
    if (manager.getNodeConstructor(ids[10]).filter(c -> c != CONSTRUCTOR_10).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + OffNormalAlarmType.TYPE_ID);
    }
    ids[11] = ClientNodeSupport.resolve(table, TripAlarmType.TYPE_ID);
    if (manager.getNodeConstructor(ids[11]).filter(c -> c != CONSTRUCTOR_11).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + TripAlarmType.TYPE_ID);
    }
    ids[12] = ClientNodeSupport.resolve(table, AuditEventType.TYPE_ID);
    if (manager.getNodeConstructor(ids[12]).filter(c -> c != CONSTRUCTOR_12).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + AuditEventType.TYPE_ID);
    }
    ids[13] = ClientNodeSupport.resolve(table, AuditUpdateMethodEventType.TYPE_ID);
    if (manager.getNodeConstructor(ids[13]).filter(c -> c != CONSTRUCTOR_13).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + AuditUpdateMethodEventType.TYPE_ID);
    }
    ids[14] = ClientNodeSupport.resolve(table, AuditConditionEventType.TYPE_ID);
    if (manager.getNodeConstructor(ids[14]).filter(c -> c != CONSTRUCTOR_14).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + AuditConditionEventType.TYPE_ID);
    }
    ids[15] = ClientNodeSupport.resolve(table, AuditConditionShelvingEventType.TYPE_ID);
    if (manager.getNodeConstructor(ids[15]).filter(c -> c != CONSTRUCTOR_15).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + AuditConditionShelvingEventType.TYPE_ID);
    }
    ids[16] = ClientNodeSupport.resolve(table, BaseConditionClassType.TYPE_ID);
    if (manager.getNodeConstructor(ids[16]).filter(c -> c != CONSTRUCTOR_16).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + BaseConditionClassType.TYPE_ID);
    }
    ids[17] = ClientNodeSupport.resolve(table, ProcessConditionClassType.TYPE_ID);
    if (manager.getNodeConstructor(ids[17]).filter(c -> c != CONSTRUCTOR_17).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + ProcessConditionClassType.TYPE_ID);
    }
    ids[18] = ClientNodeSupport.resolve(table, MaintenanceConditionClassType.TYPE_ID);
    if (manager.getNodeConstructor(ids[18]).filter(c -> c != CONSTRUCTOR_18).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + MaintenanceConditionClassType.TYPE_ID);
    }
    ids[19] = ClientNodeSupport.resolve(table, SystemConditionClassType.TYPE_ID);
    if (manager.getNodeConstructor(ids[19]).filter(c -> c != CONSTRUCTOR_19).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + SystemConditionClassType.TYPE_ID);
    }
    ids[20] = ClientNodeSupport.resolve(table, AggregateConfigurationType.TYPE_ID);
    if (manager.getNodeConstructor(ids[20]).filter(c -> c != CONSTRUCTOR_20).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + AggregateConfigurationType.TYPE_ID);
    }
    ids[21] = ClientNodeSupport.resolve(table, ProgressEventType.TYPE_ID);
    if (manager.getNodeConstructor(ids[21]).filter(c -> c != CONSTRUCTOR_21).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + ProgressEventType.TYPE_ID);
    }
    ids[22] = ClientNodeSupport.resolve(table, SystemEventType.TYPE_ID);
    if (manager.getNodeConstructor(ids[22]).filter(c -> c != CONSTRUCTOR_22).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + SystemEventType.TYPE_ID);
    }
    ids[23] = ClientNodeSupport.resolve(table, SystemStatusChangeEventType.TYPE_ID);
    if (manager.getNodeConstructor(ids[23]).filter(c -> c != CONSTRUCTOR_23).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + SystemStatusChangeEventType.TYPE_ID);
    }
    ids[24] = ClientNodeSupport.resolve(table, FolderType.TYPE_ID);
    if (manager.getNodeConstructor(ids[24]).filter(c -> c != CONSTRUCTOR_24).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + FolderType.TYPE_ID);
    }
    ids[25] = ClientNodeSupport.resolve(table, OperationLimitsType.TYPE_ID);
    if (manager.getNodeConstructor(ids[25]).filter(c -> c != CONSTRUCTOR_25).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + OperationLimitsType.TYPE_ID);
    }
    ids[26] = ClientNodeSupport.resolve(table, FileType.TYPE_ID);
    if (manager.getNodeConstructor(ids[26]).filter(c -> c != CONSTRUCTOR_26).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + FileType.TYPE_ID);
    }
    ids[27] = ClientNodeSupport.resolve(table, AddressSpaceFileType.TYPE_ID);
    if (manager.getNodeConstructor(ids[27]).filter(c -> c != CONSTRUCTOR_27).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + AddressSpaceFileType.TYPE_ID);
    }
    ids[28] = ClientNodeSupport.resolve(table, NamespaceMetadataType.TYPE_ID);
    if (manager.getNodeConstructor(ids[28]).filter(c -> c != CONSTRUCTOR_28).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + NamespaceMetadataType.TYPE_ID);
    }
    ids[29] = ClientNodeSupport.resolve(table, NamespacesType.TYPE_ID);
    if (manager.getNodeConstructor(ids[29]).filter(c -> c != CONSTRUCTOR_29).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + NamespacesType.TYPE_ID);
    }
    ids[30] = ClientNodeSupport.resolve(table, SystemOffNormalAlarmType.TYPE_ID);
    if (manager.getNodeConstructor(ids[30]).filter(c -> c != CONSTRUCTOR_30).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + SystemOffNormalAlarmType.TYPE_ID);
    }
    ids[31] = ClientNodeSupport.resolve(table, AuditUpdateStateEventType.TYPE_ID);
    if (manager.getNodeConstructor(ids[31]).filter(c -> c != CONSTRUCTOR_31).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + AuditUpdateStateEventType.TYPE_ID);
    }
    ids[32] = ClientNodeSupport.resolve(table, AuditProgramTransitionEventType.TYPE_ID);
    if (manager.getNodeConstructor(ids[32]).filter(c -> c != CONSTRUCTOR_32).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + AuditProgramTransitionEventType.TYPE_ID);
    }
    ids[33] = ClientNodeSupport.resolve(table, ServerRedundancyType.TYPE_ID);
    if (manager.getNodeConstructor(ids[33]).filter(c -> c != CONSTRUCTOR_33).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + ServerRedundancyType.TYPE_ID);
    }
    ids[34] = ClientNodeSupport.resolve(table, NonTransparentRedundancyType.TYPE_ID);
    if (manager.getNodeConstructor(ids[34]).filter(c -> c != CONSTRUCTOR_34).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + NonTransparentRedundancyType.TYPE_ID);
    }
    ids[35] = ClientNodeSupport.resolve(table, NonTransparentNetworkRedundancyType.TYPE_ID);
    if (manager.getNodeConstructor(ids[35]).filter(c -> c != CONSTRUCTOR_35).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + NonTransparentNetworkRedundancyType.TYPE_ID);
    }
    ids[36] = ClientNodeSupport.resolve(table, TrustListType.TYPE_ID);
    if (manager.getNodeConstructor(ids[36]).filter(c -> c != CONSTRUCTOR_36).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + TrustListType.TYPE_ID);
    }
    ids[37] = ClientNodeSupport.resolve(table, CertificateGroupType.TYPE_ID);
    if (manager.getNodeConstructor(ids[37]).filter(c -> c != CONSTRUCTOR_37).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + CertificateGroupType.TYPE_ID);
    }
    ids[38] = ClientNodeSupport.resolve(table, CertificateType.TYPE_ID);
    if (manager.getNodeConstructor(ids[38]).filter(c -> c != CONSTRUCTOR_38).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + CertificateType.TYPE_ID);
    }
    ids[39] = ClientNodeSupport.resolve(table, ApplicationCertificateType.TYPE_ID);
    if (manager.getNodeConstructor(ids[39]).filter(c -> c != CONSTRUCTOR_39).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + ApplicationCertificateType.TYPE_ID);
    }
    ids[40] = ClientNodeSupport.resolve(table, HttpsCertificateType.TYPE_ID);
    if (manager.getNodeConstructor(ids[40]).filter(c -> c != CONSTRUCTOR_40).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + HttpsCertificateType.TYPE_ID);
    }
    ids[41] = ClientNodeSupport.resolve(table, RsaMinApplicationCertificateType.TYPE_ID);
    if (manager.getNodeConstructor(ids[41]).filter(c -> c != CONSTRUCTOR_41).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + RsaMinApplicationCertificateType.TYPE_ID);
    }
    ids[42] = ClientNodeSupport.resolve(table, RsaSha256ApplicationCertificateType.TYPE_ID);
    if (manager.getNodeConstructor(ids[42]).filter(c -> c != CONSTRUCTOR_42).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + RsaSha256ApplicationCertificateType.TYPE_ID);
    }
    ids[43] = ClientNodeSupport.resolve(table, TrustListUpdatedAuditEventType.TYPE_ID);
    if (manager.getNodeConstructor(ids[43]).filter(c -> c != CONSTRUCTOR_43).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + TrustListUpdatedAuditEventType.TYPE_ID);
    }
    ids[44] = ClientNodeSupport.resolve(table, ServerConfigurationType.TYPE_ID);
    if (manager.getNodeConstructor(ids[44]).filter(c -> c != CONSTRUCTOR_44).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + ServerConfigurationType.TYPE_ID);
    }
    ids[45] = ClientNodeSupport.resolve(table, CertificateUpdatedAuditEventType.TYPE_ID);
    if (manager.getNodeConstructor(ids[45]).filter(c -> c != CONSTRUCTOR_45).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + CertificateUpdatedAuditEventType.TYPE_ID);
    }
    ids[46] = ClientNodeSupport.resolve(table, CertificateExpirationAlarmType.TYPE_ID);
    if (manager.getNodeConstructor(ids[46]).filter(c -> c != CONSTRUCTOR_46).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + CertificateExpirationAlarmType.TYPE_ID);
    }
    ids[47] = ClientNodeSupport.resolve(table, FileDirectoryType.TYPE_ID);
    if (manager.getNodeConstructor(ids[47]).filter(c -> c != CONSTRUCTOR_47).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + FileDirectoryType.TYPE_ID);
    }
    ids[48] = ClientNodeSupport.resolve(table, CertificateGroupFolderType.TYPE_ID);
    if (manager.getNodeConstructor(ids[48]).filter(c -> c != CONSTRUCTOR_48).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + CertificateGroupFolderType.TYPE_ID);
    }
    ids[49] = ClientNodeSupport.resolve(table, PubSubConnectionType.TYPE_ID);
    if (manager.getNodeConstructor(ids[49]).filter(c -> c != CONSTRUCTOR_49).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + PubSubConnectionType.TYPE_ID);
    }
    ids[50] = ClientNodeSupport.resolve(table, PubSubGroupType.TYPE_ID);
    if (manager.getNodeConstructor(ids[50]).filter(c -> c != CONSTRUCTOR_50).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + PubSubGroupType.TYPE_ID);
    }
    ids[51] = ClientNodeSupport.resolve(table, PubSubKeyServiceType.TYPE_ID);
    if (manager.getNodeConstructor(ids[51]).filter(c -> c != CONSTRUCTOR_51).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + PubSubKeyServiceType.TYPE_ID);
    }
    ids[52] = ClientNodeSupport.resolve(table, PublishSubscribeType.TYPE_ID);
    if (manager.getNodeConstructor(ids[52]).filter(c -> c != CONSTRUCTOR_52).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + PublishSubscribeType.TYPE_ID);
    }
    ids[53] = ClientNodeSupport.resolve(table, DataSetFolderType.TYPE_ID);
    if (manager.getNodeConstructor(ids[53]).filter(c -> c != CONSTRUCTOR_53).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + DataSetFolderType.TYPE_ID);
    }
    ids[54] = ClientNodeSupport.resolve(table, PublishedDataSetType.TYPE_ID);
    if (manager.getNodeConstructor(ids[54]).filter(c -> c != CONSTRUCTOR_54).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + PublishedDataSetType.TYPE_ID);
    }
    ids[55] = ClientNodeSupport.resolve(table, PublishedDataItemsType.TYPE_ID);
    if (manager.getNodeConstructor(ids[55]).filter(c -> c != CONSTRUCTOR_55).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + PublishedDataItemsType.TYPE_ID);
    }
    ids[56] = ClientNodeSupport.resolve(table, PublishedEventsType.TYPE_ID);
    if (manager.getNodeConstructor(ids[56]).filter(c -> c != CONSTRUCTOR_56).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + PublishedEventsType.TYPE_ID);
    }
    ids[57] = ClientNodeSupport.resolve(table, PubSubStatusType.TYPE_ID);
    if (manager.getNodeConstructor(ids[57]).filter(c -> c != CONSTRUCTOR_57).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + PubSubStatusType.TYPE_ID);
    }
    ids[58] = ClientNodeSupport.resolve(table, AuditConditionResetEventType.TYPE_ID);
    if (manager.getNodeConstructor(ids[58]).filter(c -> c != CONSTRUCTOR_58).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + AuditConditionResetEventType.TYPE_ID);
    }
    ids[59] = ClientNodeSupport.resolve(table, ConnectionTransportType.TYPE_ID);
    if (manager.getNodeConstructor(ids[59]).filter(c -> c != CONSTRUCTOR_59).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + ConnectionTransportType.TYPE_ID);
    }
    ids[60] = ClientNodeSupport.resolve(table, DatagramConnectionTransportType.TYPE_ID);
    if (manager.getNodeConstructor(ids[60]).filter(c -> c != CONSTRUCTOR_60).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + DatagramConnectionTransportType.TYPE_ID);
    }
    ids[61] = ClientNodeSupport.resolve(table, SubscribedDataSetType.TYPE_ID);
    if (manager.getNodeConstructor(ids[61]).filter(c -> c != CONSTRUCTOR_61).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + SubscribedDataSetType.TYPE_ID);
    }
    ids[62] = ClientNodeSupport.resolve(table, StateType.TYPE_ID);
    if (manager.getNodeConstructor(ids[62]).filter(c -> c != CONSTRUCTOR_62).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + StateType.TYPE_ID);
    }
    ids[63] = ClientNodeSupport.resolve(table, ChoiceStateType.TYPE_ID);
    if (manager.getNodeConstructor(ids[63]).filter(c -> c != CONSTRUCTOR_63).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + ChoiceStateType.TYPE_ID);
    }
    ids[64] = ClientNodeSupport.resolve(table, TargetVariablesType.TYPE_ID);
    if (manager.getNodeConstructor(ids[64]).filter(c -> c != CONSTRUCTOR_64).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + TargetVariablesType.TYPE_ID);
    }
    ids[65] = ClientNodeSupport.resolve(table, SubscribedDataSetMirrorType.TYPE_ID);
    if (manager.getNodeConstructor(ids[65]).filter(c -> c != CONSTRUCTOR_65).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + SubscribedDataSetMirrorType.TYPE_ID);
    }
    ids[66] = ClientNodeSupport.resolve(table, BrokerConnectionTransportType.TYPE_ID);
    if (manager.getNodeConstructor(ids[66]).filter(c -> c != CONSTRUCTOR_66).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + BrokerConnectionTransportType.TYPE_ID);
    }
    ids[67] = ClientNodeSupport.resolve(table, DataSetWriterType.TYPE_ID);
    if (manager.getNodeConstructor(ids[67]).filter(c -> c != CONSTRUCTOR_67).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + DataSetWriterType.TYPE_ID);
    }
    ids[68] = ClientNodeSupport.resolve(table, DataSetWriterTransportType.TYPE_ID);
    if (manager.getNodeConstructor(ids[68]).filter(c -> c != CONSTRUCTOR_68).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + DataSetWriterTransportType.TYPE_ID);
    }
    ids[69] = ClientNodeSupport.resolve(table, DataSetReaderType.TYPE_ID);
    if (manager.getNodeConstructor(ids[69]).filter(c -> c != CONSTRUCTOR_69).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + DataSetReaderType.TYPE_ID);
    }
    ids[70] = ClientNodeSupport.resolve(table, DataSetReaderTransportType.TYPE_ID);
    if (manager.getNodeConstructor(ids[70]).filter(c -> c != CONSTRUCTOR_70).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + DataSetReaderTransportType.TYPE_ID);
    }
    ids[71] = ClientNodeSupport.resolve(table, ConfigurationFileType.TYPE_ID);
    if (manager.getNodeConstructor(ids[71]).filter(c -> c != CONSTRUCTOR_71).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + ConfigurationFileType.TYPE_ID);
    }
    ids[72] = ClientNodeSupport.resolve(table, SecurityGroupFolderType.TYPE_ID);
    if (manager.getNodeConstructor(ids[72]).filter(c -> c != CONSTRUCTOR_72).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + SecurityGroupFolderType.TYPE_ID);
    }
    ids[73] = ClientNodeSupport.resolve(table, SecurityGroupType.TYPE_ID);
    if (manager.getNodeConstructor(ids[73]).filter(c -> c != CONSTRUCTOR_73).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + SecurityGroupType.TYPE_ID);
    }
    ids[74] = ClientNodeSupport.resolve(table, ExtensionFieldsType.TYPE_ID);
    if (manager.getNodeConstructor(ids[74]).filter(c -> c != CONSTRUCTOR_74).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + ExtensionFieldsType.TYPE_ID);
    }
    ids[75] = ClientNodeSupport.resolve(table, PubSubStatusEventType.TYPE_ID);
    if (manager.getNodeConstructor(ids[75]).filter(c -> c != CONSTRUCTOR_75).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + PubSubStatusEventType.TYPE_ID);
    }
    ids[76] = ClientNodeSupport.resolve(table, ConfigurationUpdatedAuditEventType.TYPE_ID);
    if (manager.getNodeConstructor(ids[76]).filter(c -> c != CONSTRUCTOR_76).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + ConfigurationUpdatedAuditEventType.TYPE_ID);
    }
    ids[77] = ClientNodeSupport.resolve(table, PubSubTransportLimitsExceedEventType.TYPE_ID);
    if (manager.getNodeConstructor(ids[77]).filter(c -> c != CONSTRUCTOR_77).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + PubSubTransportLimitsExceedEventType.TYPE_ID);
    }
    ids[78] = ClientNodeSupport.resolve(table, ApplicationConfigurationFileType.TYPE_ID);
    if (manager.getNodeConstructor(ids[78]).filter(c -> c != CONSTRUCTOR_78).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + ApplicationConfigurationFileType.TYPE_ID);
    }
    ids[79] = ClientNodeSupport.resolve(table, PubSubCommunicationFailureEventType.TYPE_ID);
    if (manager.getNodeConstructor(ids[79]).filter(c -> c != CONSTRUCTOR_79).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + PubSubCommunicationFailureEventType.TYPE_ID);
    }
    ids[80] = ClientNodeSupport.resolve(table, RoleSetType.TYPE_ID);
    if (manager.getNodeConstructor(ids[80]).filter(c -> c != CONSTRUCTOR_80).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + RoleSetType.TYPE_ID);
    }
    ids[81] = ClientNodeSupport.resolve(table, RoleType.TYPE_ID);
    if (manager.getNodeConstructor(ids[81]).filter(c -> c != CONSTRUCTOR_81).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + RoleType.TYPE_ID);
    }
    ids[82] = ClientNodeSupport.resolve(table, TemporaryFileTransferType.TYPE_ID);
    if (manager.getNodeConstructor(ids[82]).filter(c -> c != CONSTRUCTOR_82).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + TemporaryFileTransferType.TYPE_ID);
    }
    ids[83] = ClientNodeSupport.resolve(table, StateMachineType.TYPE_ID);
    if (manager.getNodeConstructor(ids[83]).filter(c -> c != CONSTRUCTOR_83).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + StateMachineType.TYPE_ID);
    }
    ids[84] = ClientNodeSupport.resolve(table, FiniteStateMachineType.TYPE_ID);
    if (manager.getNodeConstructor(ids[84]).filter(c -> c != CONSTRUCTOR_84).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + FiniteStateMachineType.TYPE_ID);
    }
    ids[85] = ClientNodeSupport.resolve(table, FileTransferStateMachineType.TYPE_ID);
    if (manager.getNodeConstructor(ids[85]).filter(c -> c != CONSTRUCTOR_85).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + FileTransferStateMachineType.TYPE_ID);
    }
    ids[86] = ClientNodeSupport.resolve(table, AlarmGroupType.TYPE_ID);
    if (manager.getNodeConstructor(ids[86]).filter(c -> c != CONSTRUCTOR_86).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + AlarmGroupType.TYPE_ID);
    }
    ids[87] = ClientNodeSupport.resolve(table, ApplicationConfigurationFolderType.TYPE_ID);
    if (manager.getNodeConstructor(ids[87]).filter(c -> c != CONSTRUCTOR_87).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + ApplicationConfigurationFolderType.TYPE_ID);
    }
    ids[88] = ClientNodeSupport.resolve(table, DiscrepancyAlarmType.TYPE_ID);
    if (manager.getNodeConstructor(ids[88]).filter(c -> c != CONSTRUCTOR_88).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + DiscrepancyAlarmType.TYPE_ID);
    }
    ids[89] = ClientNodeSupport.resolve(table, SafetyConditionClassType.TYPE_ID);
    if (manager.getNodeConstructor(ids[89]).filter(c -> c != CONSTRUCTOR_89).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + SafetyConditionClassType.TYPE_ID);
    }
    ids[90] = ClientNodeSupport.resolve(table, HighlyManagedAlarmConditionClassType.TYPE_ID);
    if (manager.getNodeConstructor(ids[90]).filter(c -> c != CONSTRUCTOR_90).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + HighlyManagedAlarmConditionClassType.TYPE_ID);
    }
    ids[91] = ClientNodeSupport.resolve(table, TrainingConditionClassType.TYPE_ID);
    if (manager.getNodeConstructor(ids[91]).filter(c -> c != CONSTRUCTOR_91).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + TrainingConditionClassType.TYPE_ID);
    }
    ids[92] = ClientNodeSupport.resolve(table, TestingConditionClassType.TYPE_ID);
    if (manager.getNodeConstructor(ids[92]).filter(c -> c != CONSTRUCTOR_92).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + TestingConditionClassType.TYPE_ID);
    }
    ids[93] = ClientNodeSupport.resolve(table, AuditConditionSuppressionEventType.TYPE_ID);
    if (manager.getNodeConstructor(ids[93]).filter(c -> c != CONSTRUCTOR_93).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + AuditConditionSuppressionEventType.TYPE_ID);
    }
    ids[94] = ClientNodeSupport.resolve(table, AuditConditionSilenceEventType.TYPE_ID);
    if (manager.getNodeConstructor(ids[94]).filter(c -> c != CONSTRUCTOR_94).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + AuditConditionSilenceEventType.TYPE_ID);
    }
    ids[95] = ClientNodeSupport.resolve(table, AuditConditionOutOfServiceEventType.TYPE_ID);
    if (manager.getNodeConstructor(ids[95]).filter(c -> c != CONSTRUCTOR_95).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + AuditConditionOutOfServiceEventType.TYPE_ID);
    }
    ids[96] = ClientNodeSupport.resolve(table, AlarmMetricsType.TYPE_ID);
    if (manager.getNodeConstructor(ids[96]).filter(c -> c != CONSTRUCTOR_96).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + AlarmMetricsType.TYPE_ID);
    }
    ids[97] = ClientNodeSupport.resolve(table, KeyCredentialConfigurationFolderType.TYPE_ID);
    if (manager.getNodeConstructor(ids[97]).filter(c -> c != CONSTRUCTOR_97).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + KeyCredentialConfigurationFolderType.TYPE_ID);
    }
    ids[98] = ClientNodeSupport.resolve(table, DictionaryEntryType.TYPE_ID);
    if (manager.getNodeConstructor(ids[98]).filter(c -> c != CONSTRUCTOR_98).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + DictionaryEntryType.TYPE_ID);
    }
    ids[99] = ClientNodeSupport.resolve(table, DictionaryFolderType.TYPE_ID);
    if (manager.getNodeConstructor(ids[99]).filter(c -> c != CONSTRUCTOR_99).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + DictionaryFolderType.TYPE_ID);
    }
    ids[100] = ClientNodeSupport.resolve(table, IrdiDictionaryEntryType.TYPE_ID);
    if (manager.getNodeConstructor(ids[100]).filter(c -> c != CONSTRUCTOR_100).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + IrdiDictionaryEntryType.TYPE_ID);
    }
    ids[101] = ClientNodeSupport.resolve(table, UriDictionaryEntryType.TYPE_ID);
    if (manager.getNodeConstructor(ids[101]).filter(c -> c != CONSTRUCTOR_101).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + UriDictionaryEntryType.TYPE_ID);
    }
    ids[102] = ClientNodeSupport.resolve(table, BaseInterfaceType.TYPE_ID);
    if (manager.getNodeConstructor(ids[102]).filter(c -> c != CONSTRUCTOR_102).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + BaseInterfaceType.TYPE_ID);
    }
    ids[103] = ClientNodeSupport.resolve(table, RoleMappingRuleChangedAuditEventType.TYPE_ID);
    if (manager.getNodeConstructor(ids[103]).filter(c -> c != CONSTRUCTOR_103).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + RoleMappingRuleChangedAuditEventType.TYPE_ID);
    }
    ids[104] = ClientNodeSupport.resolve(table, WriterGroupType.TYPE_ID);
    if (manager.getNodeConstructor(ids[104]).filter(c -> c != CONSTRUCTOR_104).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + WriterGroupType.TYPE_ID);
    }
    ids[105] = ClientNodeSupport.resolve(table, AuthorizationServiceConfigurationType.TYPE_ID);
    if (manager.getNodeConstructor(ids[105]).filter(c -> c != CONSTRUCTOR_105).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + AuthorizationServiceConfigurationType.TYPE_ID);
    }
    ids[106] = ClientNodeSupport.resolve(table, WriterGroupTransportType.TYPE_ID);
    if (manager.getNodeConstructor(ids[106]).filter(c -> c != CONSTRUCTOR_106).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + WriterGroupTransportType.TYPE_ID);
    }
    ids[107] = ClientNodeSupport.resolve(table, WriterGroupMessageType.TYPE_ID);
    if (manager.getNodeConstructor(ids[107]).filter(c -> c != CONSTRUCTOR_107).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + WriterGroupMessageType.TYPE_ID);
    }
    ids[108] = ClientNodeSupport.resolve(table, ReaderGroupType.TYPE_ID);
    if (manager.getNodeConstructor(ids[108]).filter(c -> c != CONSTRUCTOR_108).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + ReaderGroupType.TYPE_ID);
    }
    ids[109] = ClientNodeSupport.resolve(table, KeyCredentialConfigurationType.TYPE_ID);
    if (manager.getNodeConstructor(ids[109]).filter(c -> c != CONSTRUCTOR_109).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + KeyCredentialConfigurationType.TYPE_ID);
    }
    ids[110] = ClientNodeSupport.resolve(table, KeyCredentialAuditEventType.TYPE_ID);
    if (manager.getNodeConstructor(ids[110]).filter(c -> c != CONSTRUCTOR_110).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + KeyCredentialAuditEventType.TYPE_ID);
    }
    ids[111] = ClientNodeSupport.resolve(table, KeyCredentialUpdatedAuditEventType.TYPE_ID);
    if (manager.getNodeConstructor(ids[111]).filter(c -> c != CONSTRUCTOR_111).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + KeyCredentialUpdatedAuditEventType.TYPE_ID);
    }
    ids[112] = ClientNodeSupport.resolve(table, KeyCredentialDeletedAuditEventType.TYPE_ID);
    if (manager.getNodeConstructor(ids[112]).filter(c -> c != CONSTRUCTOR_112).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + KeyCredentialDeletedAuditEventType.TYPE_ID);
    }
    ids[113] = ClientNodeSupport.resolve(table, InstrumentDiagnosticAlarmType.TYPE_ID);
    if (manager.getNodeConstructor(ids[113]).filter(c -> c != CONSTRUCTOR_113).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + InstrumentDiagnosticAlarmType.TYPE_ID);
    }
    ids[114] = ClientNodeSupport.resolve(table, SystemDiagnosticAlarmType.TYPE_ID);
    if (manager.getNodeConstructor(ids[114]).filter(c -> c != CONSTRUCTOR_114).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + SystemDiagnosticAlarmType.TYPE_ID);
    }
    ids[115] = ClientNodeSupport.resolve(table, StatisticalConditionClassType.TYPE_ID);
    if (manager.getNodeConstructor(ids[115]).filter(c -> c != CONSTRUCTOR_115).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + StatisticalConditionClassType.TYPE_ID);
    }
    ids[116] = ClientNodeSupport.resolve(table, LldpInformationType.TYPE_ID);
    if (manager.getNodeConstructor(ids[116]).filter(c -> c != CONSTRUCTOR_116).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + LldpInformationType.TYPE_ID);
    }
    ids[117] = ClientNodeSupport.resolve(table, LldpRemoteStatisticsType.TYPE_ID);
    if (manager.getNodeConstructor(ids[117]).filter(c -> c != CONSTRUCTOR_117).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + LldpRemoteStatisticsType.TYPE_ID);
    }
    ids[118] = ClientNodeSupport.resolve(table, LldpLocalSystemType.TYPE_ID);
    if (manager.getNodeConstructor(ids[118]).filter(c -> c != CONSTRUCTOR_118).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + LldpLocalSystemType.TYPE_ID);
    }
    ids[119] = ClientNodeSupport.resolve(table, LldpPortInformationType.TYPE_ID);
    if (manager.getNodeConstructor(ids[119]).filter(c -> c != CONSTRUCTOR_119).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + LldpPortInformationType.TYPE_ID);
    }
    ids[120] = ClientNodeSupport.resolve(table, LldpRemoteSystemType.TYPE_ID);
    if (manager.getNodeConstructor(ids[120]).filter(c -> c != CONSTRUCTOR_120).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + LldpRemoteSystemType.TYPE_ID);
    }
    ids[121] = ClientNodeSupport.resolve(table, AuditUpdateEventType.TYPE_ID);
    if (manager.getNodeConstructor(ids[121]).filter(c -> c != CONSTRUCTOR_121).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + AuditUpdateEventType.TYPE_ID);
    }
    ids[122] = ClientNodeSupport.resolve(table, AuditHistoryUpdateEventType.TYPE_ID);
    if (manager.getNodeConstructor(ids[122]).filter(c -> c != CONSTRUCTOR_122).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + AuditHistoryUpdateEventType.TYPE_ID);
    }
    ids[123] = ClientNodeSupport.resolve(table, AuditHistoryAnnotationUpdateEventType.TYPE_ID);
    if (manager.getNodeConstructor(ids[123]).filter(c -> c != CONSTRUCTOR_123).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + AuditHistoryAnnotationUpdateEventType.TYPE_ID);
    }
    ids[124] = ClientNodeSupport.resolve(table, TrustListOutOfDateAlarmType.TYPE_ID);
    if (manager.getNodeConstructor(ids[124]).filter(c -> c != CONSTRUCTOR_124).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + TrustListOutOfDateAlarmType.TYPE_ID);
    }
    ids[125] = ClientNodeSupport.resolve(table, UserCertificateType.TYPE_ID);
    if (manager.getNodeConstructor(ids[125]).filter(c -> c != CONSTRUCTOR_125).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + UserCertificateType.TYPE_ID);
    }
    ids[126] = ClientNodeSupport.resolve(table, TlsCertificateType.TYPE_ID);
    if (manager.getNodeConstructor(ids[126]).filter(c -> c != CONSTRUCTOR_126).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + TlsCertificateType.TYPE_ID);
    }
    ids[127] = ClientNodeSupport.resolve(table, TlsServerCertificateType.TYPE_ID);
    if (manager.getNodeConstructor(ids[127]).filter(c -> c != CONSTRUCTOR_127).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + TlsServerCertificateType.TYPE_ID);
    }
  }

  private static void install0(ObjectTypeManager manager, NodeId[] ids) {
    if (manager.getNodeConstructor(ids[0]).isEmpty()) {
      manager.registerObjectType(ids[0], BaseEventTypeNode.class, CONSTRUCTOR_0);
    }
    if (manager.getNodeConstructor(ids[1]).isEmpty()) {
      manager.registerObjectType(ids[1], ConditionTypeNode.class, CONSTRUCTOR_1);
    }
    if (manager.getNodeConstructor(ids[2]).isEmpty()) {
      manager.registerObjectType(ids[2], AcknowledgeableConditionTypeNode.class, CONSTRUCTOR_2);
    }
    if (manager.getNodeConstructor(ids[3]).isEmpty()) {
      manager.registerObjectType(ids[3], AlarmConditionTypeNode.class, CONSTRUCTOR_3);
    }
    if (manager.getNodeConstructor(ids[4]).isEmpty()) {
      manager.registerObjectType(ids[4], LimitAlarmTypeNode.class, CONSTRUCTOR_4);
    }
    if (manager.getNodeConstructor(ids[5]).isEmpty()) {
      manager.registerObjectType(ids[5], NonExclusiveLimitAlarmTypeNode.class, CONSTRUCTOR_5);
    }
    if (manager.getNodeConstructor(ids[6]).isEmpty()) {
      manager.registerObjectType(ids[6], NonExclusiveLevelAlarmTypeNode.class, CONSTRUCTOR_6);
    }
    if (manager.getNodeConstructor(ids[7]).isEmpty()) {
      manager.registerObjectType(
          ids[7], NonExclusiveRateOfChangeAlarmTypeNode.class, CONSTRUCTOR_7);
    }
    if (manager.getNodeConstructor(ids[8]).isEmpty()) {
      manager.registerObjectType(ids[8], NonExclusiveDeviationAlarmTypeNode.class, CONSTRUCTOR_8);
    }
    if (manager.getNodeConstructor(ids[9]).isEmpty()) {
      manager.registerObjectType(ids[9], DiscreteAlarmTypeNode.class, CONSTRUCTOR_9);
    }
    if (manager.getNodeConstructor(ids[10]).isEmpty()) {
      manager.registerObjectType(ids[10], OffNormalAlarmTypeNode.class, CONSTRUCTOR_10);
    }
    if (manager.getNodeConstructor(ids[11]).isEmpty()) {
      manager.registerObjectType(ids[11], TripAlarmTypeNode.class, CONSTRUCTOR_11);
    }
    if (manager.getNodeConstructor(ids[12]).isEmpty()) {
      manager.registerObjectType(ids[12], AuditEventTypeNode.class, CONSTRUCTOR_12);
    }
    if (manager.getNodeConstructor(ids[13]).isEmpty()) {
      manager.registerObjectType(ids[13], AuditUpdateMethodEventTypeNode.class, CONSTRUCTOR_13);
    }
    if (manager.getNodeConstructor(ids[14]).isEmpty()) {
      manager.registerObjectType(ids[14], AuditConditionEventTypeNode.class, CONSTRUCTOR_14);
    }
    if (manager.getNodeConstructor(ids[15]).isEmpty()) {
      manager.registerObjectType(
          ids[15], AuditConditionShelvingEventTypeNode.class, CONSTRUCTOR_15);
    }
    if (manager.getNodeConstructor(ids[16]).isEmpty()) {
      manager.registerObjectType(ids[16], BaseConditionClassTypeNode.class, CONSTRUCTOR_16);
    }
    if (manager.getNodeConstructor(ids[17]).isEmpty()) {
      manager.registerObjectType(ids[17], ProcessConditionClassTypeNode.class, CONSTRUCTOR_17);
    }
    if (manager.getNodeConstructor(ids[18]).isEmpty()) {
      manager.registerObjectType(ids[18], MaintenanceConditionClassTypeNode.class, CONSTRUCTOR_18);
    }
    if (manager.getNodeConstructor(ids[19]).isEmpty()) {
      manager.registerObjectType(ids[19], SystemConditionClassTypeNode.class, CONSTRUCTOR_19);
    }
    if (manager.getNodeConstructor(ids[20]).isEmpty()) {
      manager.registerObjectType(ids[20], AggregateConfigurationTypeNode.class, CONSTRUCTOR_20);
    }
    if (manager.getNodeConstructor(ids[21]).isEmpty()) {
      manager.registerObjectType(ids[21], ProgressEventTypeNode.class, CONSTRUCTOR_21);
    }
    if (manager.getNodeConstructor(ids[22]).isEmpty()) {
      manager.registerObjectType(ids[22], SystemEventTypeNode.class, CONSTRUCTOR_22);
    }
    if (manager.getNodeConstructor(ids[23]).isEmpty()) {
      manager.registerObjectType(ids[23], SystemStatusChangeEventTypeNode.class, CONSTRUCTOR_23);
    }
    if (manager.getNodeConstructor(ids[24]).isEmpty()) {
      manager.registerObjectType(ids[24], FolderTypeNode.class, CONSTRUCTOR_24);
    }
    if (manager.getNodeConstructor(ids[25]).isEmpty()) {
      manager.registerObjectType(ids[25], OperationLimitsTypeNode.class, CONSTRUCTOR_25);
    }
    if (manager.getNodeConstructor(ids[26]).isEmpty()) {
      manager.registerObjectType(ids[26], FileTypeNode.class, CONSTRUCTOR_26);
    }
    if (manager.getNodeConstructor(ids[27]).isEmpty()) {
      manager.registerObjectType(ids[27], AddressSpaceFileTypeNode.class, CONSTRUCTOR_27);
    }
    if (manager.getNodeConstructor(ids[28]).isEmpty()) {
      manager.registerObjectType(ids[28], NamespaceMetadataTypeNode.class, CONSTRUCTOR_28);
    }
    if (manager.getNodeConstructor(ids[29]).isEmpty()) {
      manager.registerObjectType(ids[29], NamespacesTypeNode.class, CONSTRUCTOR_29);
    }
    if (manager.getNodeConstructor(ids[30]).isEmpty()) {
      manager.registerObjectType(ids[30], SystemOffNormalAlarmTypeNode.class, CONSTRUCTOR_30);
    }
    if (manager.getNodeConstructor(ids[31]).isEmpty()) {
      manager.registerObjectType(ids[31], AuditUpdateStateEventTypeNode.class, CONSTRUCTOR_31);
    }
    if (manager.getNodeConstructor(ids[32]).isEmpty()) {
      manager.registerObjectType(
          ids[32], AuditProgramTransitionEventTypeNode.class, CONSTRUCTOR_32);
    }
    if (manager.getNodeConstructor(ids[33]).isEmpty()) {
      manager.registerObjectType(ids[33], ServerRedundancyTypeNode.class, CONSTRUCTOR_33);
    }
    if (manager.getNodeConstructor(ids[34]).isEmpty()) {
      manager.registerObjectType(ids[34], NonTransparentRedundancyTypeNode.class, CONSTRUCTOR_34);
    }
    if (manager.getNodeConstructor(ids[35]).isEmpty()) {
      manager.registerObjectType(
          ids[35], NonTransparentNetworkRedundancyTypeNode.class, CONSTRUCTOR_35);
    }
    if (manager.getNodeConstructor(ids[36]).isEmpty()) {
      manager.registerObjectType(ids[36], TrustListTypeNode.class, CONSTRUCTOR_36);
    }
    if (manager.getNodeConstructor(ids[37]).isEmpty()) {
      manager.registerObjectType(ids[37], CertificateGroupTypeNode.class, CONSTRUCTOR_37);
    }
    if (manager.getNodeConstructor(ids[38]).isEmpty()) {
      manager.registerObjectType(ids[38], CertificateTypeNode.class, CONSTRUCTOR_38);
    }
    if (manager.getNodeConstructor(ids[39]).isEmpty()) {
      manager.registerObjectType(ids[39], ApplicationCertificateTypeNode.class, CONSTRUCTOR_39);
    }
    if (manager.getNodeConstructor(ids[40]).isEmpty()) {
      manager.registerObjectType(ids[40], HttpsCertificateTypeNode.class, CONSTRUCTOR_40);
    }
    if (manager.getNodeConstructor(ids[41]).isEmpty()) {
      manager.registerObjectType(
          ids[41], RsaMinApplicationCertificateTypeNode.class, CONSTRUCTOR_41);
    }
    if (manager.getNodeConstructor(ids[42]).isEmpty()) {
      manager.registerObjectType(
          ids[42], RsaSha256ApplicationCertificateTypeNode.class, CONSTRUCTOR_42);
    }
    if (manager.getNodeConstructor(ids[43]).isEmpty()) {
      manager.registerObjectType(ids[43], TrustListUpdatedAuditEventTypeNode.class, CONSTRUCTOR_43);
    }
    if (manager.getNodeConstructor(ids[44]).isEmpty()) {
      manager.registerObjectType(ids[44], ServerConfigurationTypeNode.class, CONSTRUCTOR_44);
    }
    if (manager.getNodeConstructor(ids[45]).isEmpty()) {
      manager.registerObjectType(
          ids[45], CertificateUpdatedAuditEventTypeNode.class, CONSTRUCTOR_45);
    }
    if (manager.getNodeConstructor(ids[46]).isEmpty()) {
      manager.registerObjectType(ids[46], CertificateExpirationAlarmTypeNode.class, CONSTRUCTOR_46);
    }
    if (manager.getNodeConstructor(ids[47]).isEmpty()) {
      manager.registerObjectType(ids[47], FileDirectoryTypeNode.class, CONSTRUCTOR_47);
    }
    if (manager.getNodeConstructor(ids[48]).isEmpty()) {
      manager.registerObjectType(ids[48], CertificateGroupFolderTypeNode.class, CONSTRUCTOR_48);
    }
    if (manager.getNodeConstructor(ids[49]).isEmpty()) {
      manager.registerObjectType(ids[49], PubSubConnectionTypeNode.class, CONSTRUCTOR_49);
    }
    if (manager.getNodeConstructor(ids[50]).isEmpty()) {
      manager.registerObjectType(ids[50], PubSubGroupTypeNode.class, CONSTRUCTOR_50);
    }
    if (manager.getNodeConstructor(ids[51]).isEmpty()) {
      manager.registerObjectType(ids[51], PubSubKeyServiceTypeNode.class, CONSTRUCTOR_51);
    }
    if (manager.getNodeConstructor(ids[52]).isEmpty()) {
      manager.registerObjectType(ids[52], PublishSubscribeTypeNode.class, CONSTRUCTOR_52);
    }
    if (manager.getNodeConstructor(ids[53]).isEmpty()) {
      manager.registerObjectType(ids[53], DataSetFolderTypeNode.class, CONSTRUCTOR_53);
    }
    if (manager.getNodeConstructor(ids[54]).isEmpty()) {
      manager.registerObjectType(ids[54], PublishedDataSetTypeNode.class, CONSTRUCTOR_54);
    }
    if (manager.getNodeConstructor(ids[55]).isEmpty()) {
      manager.registerObjectType(ids[55], PublishedDataItemsTypeNode.class, CONSTRUCTOR_55);
    }
    if (manager.getNodeConstructor(ids[56]).isEmpty()) {
      manager.registerObjectType(ids[56], PublishedEventsTypeNode.class, CONSTRUCTOR_56);
    }
    if (manager.getNodeConstructor(ids[57]).isEmpty()) {
      manager.registerObjectType(ids[57], PubSubStatusTypeNode.class, CONSTRUCTOR_57);
    }
    if (manager.getNodeConstructor(ids[58]).isEmpty()) {
      manager.registerObjectType(ids[58], AuditConditionResetEventTypeNode.class, CONSTRUCTOR_58);
    }
    if (manager.getNodeConstructor(ids[59]).isEmpty()) {
      manager.registerObjectType(ids[59], ConnectionTransportTypeNode.class, CONSTRUCTOR_59);
    }
    if (manager.getNodeConstructor(ids[60]).isEmpty()) {
      manager.registerObjectType(
          ids[60], DatagramConnectionTransportTypeNode.class, CONSTRUCTOR_60);
    }
    if (manager.getNodeConstructor(ids[61]).isEmpty()) {
      manager.registerObjectType(ids[61], SubscribedDataSetTypeNode.class, CONSTRUCTOR_61);
    }
    if (manager.getNodeConstructor(ids[62]).isEmpty()) {
      manager.registerObjectType(ids[62], StateTypeNode.class, CONSTRUCTOR_62);
    }
    if (manager.getNodeConstructor(ids[63]).isEmpty()) {
      manager.registerObjectType(ids[63], ChoiceStateTypeNode.class, CONSTRUCTOR_63);
    }
    if (manager.getNodeConstructor(ids[64]).isEmpty()) {
      manager.registerObjectType(ids[64], TargetVariablesTypeNode.class, CONSTRUCTOR_64);
    }
    if (manager.getNodeConstructor(ids[65]).isEmpty()) {
      manager.registerObjectType(ids[65], SubscribedDataSetMirrorTypeNode.class, CONSTRUCTOR_65);
    }
    if (manager.getNodeConstructor(ids[66]).isEmpty()) {
      manager.registerObjectType(ids[66], BrokerConnectionTransportTypeNode.class, CONSTRUCTOR_66);
    }
    if (manager.getNodeConstructor(ids[67]).isEmpty()) {
      manager.registerObjectType(ids[67], DataSetWriterTypeNode.class, CONSTRUCTOR_67);
    }
    if (manager.getNodeConstructor(ids[68]).isEmpty()) {
      manager.registerObjectType(ids[68], DataSetWriterTransportTypeNode.class, CONSTRUCTOR_68);
    }
    if (manager.getNodeConstructor(ids[69]).isEmpty()) {
      manager.registerObjectType(ids[69], DataSetReaderTypeNode.class, CONSTRUCTOR_69);
    }
    if (manager.getNodeConstructor(ids[70]).isEmpty()) {
      manager.registerObjectType(ids[70], DataSetReaderTransportTypeNode.class, CONSTRUCTOR_70);
    }
    if (manager.getNodeConstructor(ids[71]).isEmpty()) {
      manager.registerObjectType(ids[71], ConfigurationFileTypeNode.class, CONSTRUCTOR_71);
    }
    if (manager.getNodeConstructor(ids[72]).isEmpty()) {
      manager.registerObjectType(ids[72], SecurityGroupFolderTypeNode.class, CONSTRUCTOR_72);
    }
    if (manager.getNodeConstructor(ids[73]).isEmpty()) {
      manager.registerObjectType(ids[73], SecurityGroupTypeNode.class, CONSTRUCTOR_73);
    }
    if (manager.getNodeConstructor(ids[74]).isEmpty()) {
      manager.registerObjectType(ids[74], ExtensionFieldsTypeNode.class, CONSTRUCTOR_74);
    }
    if (manager.getNodeConstructor(ids[75]).isEmpty()) {
      manager.registerObjectType(ids[75], PubSubStatusEventTypeNode.class, CONSTRUCTOR_75);
    }
    if (manager.getNodeConstructor(ids[76]).isEmpty()) {
      manager.registerObjectType(
          ids[76], ConfigurationUpdatedAuditEventTypeNode.class, CONSTRUCTOR_76);
    }
    if (manager.getNodeConstructor(ids[77]).isEmpty()) {
      manager.registerObjectType(
          ids[77], PubSubTransportLimitsExceedEventTypeNode.class, CONSTRUCTOR_77);
    }
    if (manager.getNodeConstructor(ids[78]).isEmpty()) {
      manager.registerObjectType(
          ids[78], ApplicationConfigurationFileTypeNode.class, CONSTRUCTOR_78);
    }
    if (manager.getNodeConstructor(ids[79]).isEmpty()) {
      manager.registerObjectType(
          ids[79], PubSubCommunicationFailureEventTypeNode.class, CONSTRUCTOR_79);
    }
    if (manager.getNodeConstructor(ids[80]).isEmpty()) {
      manager.registerObjectType(ids[80], RoleSetTypeNode.class, CONSTRUCTOR_80);
    }
    if (manager.getNodeConstructor(ids[81]).isEmpty()) {
      manager.registerObjectType(ids[81], RoleTypeNode.class, CONSTRUCTOR_81);
    }
    if (manager.getNodeConstructor(ids[82]).isEmpty()) {
      manager.registerObjectType(ids[82], TemporaryFileTransferTypeNode.class, CONSTRUCTOR_82);
    }
    if (manager.getNodeConstructor(ids[83]).isEmpty()) {
      manager.registerObjectType(ids[83], StateMachineTypeNode.class, CONSTRUCTOR_83);
    }
    if (manager.getNodeConstructor(ids[84]).isEmpty()) {
      manager.registerObjectType(ids[84], FiniteStateMachineTypeNode.class, CONSTRUCTOR_84);
    }
    if (manager.getNodeConstructor(ids[85]).isEmpty()) {
      manager.registerObjectType(ids[85], FileTransferStateMachineTypeNode.class, CONSTRUCTOR_85);
    }
    if (manager.getNodeConstructor(ids[86]).isEmpty()) {
      manager.registerObjectType(ids[86], AlarmGroupTypeNode.class, CONSTRUCTOR_86);
    }
    if (manager.getNodeConstructor(ids[87]).isEmpty()) {
      manager.registerObjectType(
          ids[87], ApplicationConfigurationFolderTypeNode.class, CONSTRUCTOR_87);
    }
    if (manager.getNodeConstructor(ids[88]).isEmpty()) {
      manager.registerObjectType(ids[88], DiscrepancyAlarmTypeNode.class, CONSTRUCTOR_88);
    }
    if (manager.getNodeConstructor(ids[89]).isEmpty()) {
      manager.registerObjectType(ids[89], SafetyConditionClassTypeNode.class, CONSTRUCTOR_89);
    }
    if (manager.getNodeConstructor(ids[90]).isEmpty()) {
      manager.registerObjectType(
          ids[90], HighlyManagedAlarmConditionClassTypeNode.class, CONSTRUCTOR_90);
    }
    if (manager.getNodeConstructor(ids[91]).isEmpty()) {
      manager.registerObjectType(ids[91], TrainingConditionClassTypeNode.class, CONSTRUCTOR_91);
    }
    if (manager.getNodeConstructor(ids[92]).isEmpty()) {
      manager.registerObjectType(ids[92], TestingConditionClassTypeNode.class, CONSTRUCTOR_92);
    }
    if (manager.getNodeConstructor(ids[93]).isEmpty()) {
      manager.registerObjectType(
          ids[93], AuditConditionSuppressionEventTypeNode.class, CONSTRUCTOR_93);
    }
    if (manager.getNodeConstructor(ids[94]).isEmpty()) {
      manager.registerObjectType(ids[94], AuditConditionSilenceEventTypeNode.class, CONSTRUCTOR_94);
    }
    if (manager.getNodeConstructor(ids[95]).isEmpty()) {
      manager.registerObjectType(
          ids[95], AuditConditionOutOfServiceEventTypeNode.class, CONSTRUCTOR_95);
    }
    if (manager.getNodeConstructor(ids[96]).isEmpty()) {
      manager.registerObjectType(ids[96], AlarmMetricsTypeNode.class, CONSTRUCTOR_96);
    }
    if (manager.getNodeConstructor(ids[97]).isEmpty()) {
      manager.registerObjectType(
          ids[97], KeyCredentialConfigurationFolderTypeNode.class, CONSTRUCTOR_97);
    }
    if (manager.getNodeConstructor(ids[98]).isEmpty()) {
      manager.registerObjectType(ids[98], DictionaryEntryTypeNode.class, CONSTRUCTOR_98);
    }
    if (manager.getNodeConstructor(ids[99]).isEmpty()) {
      manager.registerObjectType(ids[99], DictionaryFolderTypeNode.class, CONSTRUCTOR_99);
    }
    if (manager.getNodeConstructor(ids[100]).isEmpty()) {
      manager.registerObjectType(ids[100], IrdiDictionaryEntryTypeNode.class, CONSTRUCTOR_100);
    }
    if (manager.getNodeConstructor(ids[101]).isEmpty()) {
      manager.registerObjectType(ids[101], UriDictionaryEntryTypeNode.class, CONSTRUCTOR_101);
    }
    if (manager.getNodeConstructor(ids[102]).isEmpty()) {
      manager.registerObjectType(ids[102], BaseInterfaceTypeNode.class, CONSTRUCTOR_102);
    }
    if (manager.getNodeConstructor(ids[103]).isEmpty()) {
      manager.registerObjectType(
          ids[103], RoleMappingRuleChangedAuditEventTypeNode.class, CONSTRUCTOR_103);
    }
    if (manager.getNodeConstructor(ids[104]).isEmpty()) {
      manager.registerObjectType(ids[104], WriterGroupTypeNode.class, CONSTRUCTOR_104);
    }
    if (manager.getNodeConstructor(ids[105]).isEmpty()) {
      manager.registerObjectType(
          ids[105], AuthorizationServiceConfigurationTypeNode.class, CONSTRUCTOR_105);
    }
    if (manager.getNodeConstructor(ids[106]).isEmpty()) {
      manager.registerObjectType(ids[106], WriterGroupTransportTypeNode.class, CONSTRUCTOR_106);
    }
    if (manager.getNodeConstructor(ids[107]).isEmpty()) {
      manager.registerObjectType(ids[107], WriterGroupMessageTypeNode.class, CONSTRUCTOR_107);
    }
    if (manager.getNodeConstructor(ids[108]).isEmpty()) {
      manager.registerObjectType(ids[108], ReaderGroupTypeNode.class, CONSTRUCTOR_108);
    }
    if (manager.getNodeConstructor(ids[109]).isEmpty()) {
      manager.registerObjectType(
          ids[109], KeyCredentialConfigurationTypeNode.class, CONSTRUCTOR_109);
    }
    if (manager.getNodeConstructor(ids[110]).isEmpty()) {
      manager.registerObjectType(ids[110], KeyCredentialAuditEventTypeNode.class, CONSTRUCTOR_110);
    }
    if (manager.getNodeConstructor(ids[111]).isEmpty()) {
      manager.registerObjectType(
          ids[111], KeyCredentialUpdatedAuditEventTypeNode.class, CONSTRUCTOR_111);
    }
    if (manager.getNodeConstructor(ids[112]).isEmpty()) {
      manager.registerObjectType(
          ids[112], KeyCredentialDeletedAuditEventTypeNode.class, CONSTRUCTOR_112);
    }
    if (manager.getNodeConstructor(ids[113]).isEmpty()) {
      manager.registerObjectType(
          ids[113], InstrumentDiagnosticAlarmTypeNode.class, CONSTRUCTOR_113);
    }
    if (manager.getNodeConstructor(ids[114]).isEmpty()) {
      manager.registerObjectType(ids[114], SystemDiagnosticAlarmTypeNode.class, CONSTRUCTOR_114);
    }
    if (manager.getNodeConstructor(ids[115]).isEmpty()) {
      manager.registerObjectType(
          ids[115], StatisticalConditionClassTypeNode.class, CONSTRUCTOR_115);
    }
    if (manager.getNodeConstructor(ids[116]).isEmpty()) {
      manager.registerObjectType(ids[116], LldpInformationTypeNode.class, CONSTRUCTOR_116);
    }
    if (manager.getNodeConstructor(ids[117]).isEmpty()) {
      manager.registerObjectType(ids[117], LldpRemoteStatisticsTypeNode.class, CONSTRUCTOR_117);
    }
    if (manager.getNodeConstructor(ids[118]).isEmpty()) {
      manager.registerObjectType(ids[118], LldpLocalSystemTypeNode.class, CONSTRUCTOR_118);
    }
    if (manager.getNodeConstructor(ids[119]).isEmpty()) {
      manager.registerObjectType(ids[119], LldpPortInformationTypeNode.class, CONSTRUCTOR_119);
    }
    if (manager.getNodeConstructor(ids[120]).isEmpty()) {
      manager.registerObjectType(ids[120], LldpRemoteSystemTypeNode.class, CONSTRUCTOR_120);
    }
    if (manager.getNodeConstructor(ids[121]).isEmpty()) {
      manager.registerObjectType(ids[121], AuditUpdateEventTypeNode.class, CONSTRUCTOR_121);
    }
    if (manager.getNodeConstructor(ids[122]).isEmpty()) {
      manager.registerObjectType(ids[122], AuditHistoryUpdateEventTypeNode.class, CONSTRUCTOR_122);
    }
    if (manager.getNodeConstructor(ids[123]).isEmpty()) {
      manager.registerObjectType(
          ids[123], AuditHistoryAnnotationUpdateEventTypeNode.class, CONSTRUCTOR_123);
    }
    if (manager.getNodeConstructor(ids[124]).isEmpty()) {
      manager.registerObjectType(ids[124], TrustListOutOfDateAlarmTypeNode.class, CONSTRUCTOR_124);
    }
    if (manager.getNodeConstructor(ids[125]).isEmpty()) {
      manager.registerObjectType(ids[125], UserCertificateTypeNode.class, CONSTRUCTOR_125);
    }
    if (manager.getNodeConstructor(ids[126]).isEmpty()) {
      manager.registerObjectType(ids[126], TlsCertificateTypeNode.class, CONSTRUCTOR_126);
    }
    if (manager.getNodeConstructor(ids[127]).isEmpty()) {
      manager.registerObjectType(ids[127], TlsServerCertificateTypeNode.class, CONSTRUCTOR_127);
    }
  }

  private static void check1(NamespaceTable table, ObjectTypeManager manager, NodeId[] ids) {
    ids[128] = ClientNodeSupport.resolve(table, TlsClientCertificateType.TYPE_ID);
    if (manager.getNodeConstructor(ids[128]).filter(c -> c != CONSTRUCTOR_128).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + TlsClientCertificateType.TYPE_ID);
    }
    ids[129] = ClientNodeSupport.resolve(table, LogObjectType.TYPE_ID);
    if (manager.getNodeConstructor(ids[129]).filter(c -> c != CONSTRUCTOR_129).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + LogObjectType.TYPE_ID);
    }
    ids[130] = ClientNodeSupport.resolve(table, BaseLogEventType.TYPE_ID);
    if (manager.getNodeConstructor(ids[130]).filter(c -> c != CONSTRUCTOR_130).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + BaseLogEventType.TYPE_ID);
    }
    ids[131] = ClientNodeSupport.resolve(table, LogOverflowEventType.TYPE_ID);
    if (manager.getNodeConstructor(ids[131]).filter(c -> c != CONSTRUCTOR_131).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + LogOverflowEventType.TYPE_ID);
    }
    ids[132] = ClientNodeSupport.resolve(table, LogEntryConditionClassType.TYPE_ID);
    if (manager.getNodeConstructor(ids[132]).filter(c -> c != CONSTRUCTOR_132).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + LogEntryConditionClassType.TYPE_ID);
    }
    ids[133] = ClientNodeSupport.resolve(table, PubSubDiagnosticsType.TYPE_ID);
    if (manager.getNodeConstructor(ids[133]).filter(c -> c != CONSTRUCTOR_133).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + PubSubDiagnosticsType.TYPE_ID);
    }
    ids[134] = ClientNodeSupport.resolve(table, PubSubDiagnosticsRootType.TYPE_ID);
    if (manager.getNodeConstructor(ids[134]).filter(c -> c != CONSTRUCTOR_134).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + PubSubDiagnosticsRootType.TYPE_ID);
    }
    ids[135] = ClientNodeSupport.resolve(table, PubSubDiagnosticsConnectionType.TYPE_ID);
    if (manager.getNodeConstructor(ids[135]).filter(c -> c != CONSTRUCTOR_135).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + PubSubDiagnosticsConnectionType.TYPE_ID);
    }
    ids[136] = ClientNodeSupport.resolve(table, DataTypeRefinementType.TYPE_ID);
    if (manager.getNodeConstructor(ids[136]).filter(c -> c != CONSTRUCTOR_136).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + DataTypeRefinementType.TYPE_ID);
    }
    ids[137] = ClientNodeSupport.resolve(table, SubtypeRestrictionType.TYPE_ID);
    if (manager.getNodeConstructor(ids[137]).filter(c -> c != CONSTRUCTOR_137).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + SubtypeRestrictionType.TYPE_ID);
    }
    ids[138] = ClientNodeSupport.resolve(table, SerializationEntityType.TYPE_ID);
    if (manager.getNodeConstructor(ids[138]).filter(c -> c != CONSTRUCTOR_138).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + SerializationEntityType.TYPE_ID);
    }
    ids[139] = ClientNodeSupport.resolve(table, PubSubDiagnosticsWriterGroupType.TYPE_ID);
    if (manager.getNodeConstructor(ids[139]).filter(c -> c != CONSTRUCTOR_139).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + PubSubDiagnosticsWriterGroupType.TYPE_ID);
    }
    ids[140] = ClientNodeSupport.resolve(table, PubSubDiagnosticsReaderGroupType.TYPE_ID);
    if (manager.getNodeConstructor(ids[140]).filter(c -> c != CONSTRUCTOR_140).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + PubSubDiagnosticsReaderGroupType.TYPE_ID);
    }
    ids[141] = ClientNodeSupport.resolve(table, PubSubDiagnosticsDataSetWriterType.TYPE_ID);
    if (manager.getNodeConstructor(ids[141]).filter(c -> c != CONSTRUCTOR_141).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + PubSubDiagnosticsDataSetWriterType.TYPE_ID);
    }
    ids[142] = ClientNodeSupport.resolve(table, PubSubDiagnosticsDataSetReaderType.TYPE_ID);
    if (manager.getNodeConstructor(ids[142]).filter(c -> c != CONSTRUCTOR_142).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + PubSubDiagnosticsDataSetReaderType.TYPE_ID);
    }
    ids[143] = ClientNodeSupport.resolve(table, ServerType.TYPE_ID);
    if (manager.getNodeConstructor(ids[143]).filter(c -> c != CONSTRUCTOR_143).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + ServerType.TYPE_ID);
    }
    ids[144] = ClientNodeSupport.resolve(table, ServerCapabilitiesType.TYPE_ID);
    if (manager.getNodeConstructor(ids[144]).filter(c -> c != CONSTRUCTOR_144).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + ServerCapabilitiesType.TYPE_ID);
    }
    ids[145] = ClientNodeSupport.resolve(table, ServerDiagnosticsType.TYPE_ID);
    if (manager.getNodeConstructor(ids[145]).filter(c -> c != CONSTRUCTOR_145).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + ServerDiagnosticsType.TYPE_ID);
    }
    ids[146] = ClientNodeSupport.resolve(table, SessionsDiagnosticsSummaryType.TYPE_ID);
    if (manager.getNodeConstructor(ids[146]).filter(c -> c != CONSTRUCTOR_146).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + SessionsDiagnosticsSummaryType.TYPE_ID);
    }
    ids[147] = ClientNodeSupport.resolve(table, SessionDiagnosticsObjectType.TYPE_ID);
    if (manager.getNodeConstructor(ids[147]).filter(c -> c != CONSTRUCTOR_147).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + SessionDiagnosticsObjectType.TYPE_ID);
    }
    ids[148] = ClientNodeSupport.resolve(table, VendorServerInfoType.TYPE_ID);
    if (manager.getNodeConstructor(ids[148]).filter(c -> c != CONSTRUCTOR_148).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + VendorServerInfoType.TYPE_ID);
    }
    ids[149] = ClientNodeSupport.resolve(table, TransparentRedundancyType.TYPE_ID);
    if (manager.getNodeConstructor(ids[149]).filter(c -> c != CONSTRUCTOR_149).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + TransparentRedundancyType.TYPE_ID);
    }
    ids[150] = ClientNodeSupport.resolve(table, AuditSecurityEventType.TYPE_ID);
    if (manager.getNodeConstructor(ids[150]).filter(c -> c != CONSTRUCTOR_150).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + AuditSecurityEventType.TYPE_ID);
    }
    ids[151] = ClientNodeSupport.resolve(table, AuditChannelEventType.TYPE_ID);
    if (manager.getNodeConstructor(ids[151]).filter(c -> c != CONSTRUCTOR_151).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + AuditChannelEventType.TYPE_ID);
    }
    ids[152] = ClientNodeSupport.resolve(table, AuditOpenSecureChannelEventType.TYPE_ID);
    if (manager.getNodeConstructor(ids[152]).filter(c -> c != CONSTRUCTOR_152).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + AuditOpenSecureChannelEventType.TYPE_ID);
    }
    ids[153] = ClientNodeSupport.resolve(table, AuditSessionEventType.TYPE_ID);
    if (manager.getNodeConstructor(ids[153]).filter(c -> c != CONSTRUCTOR_153).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + AuditSessionEventType.TYPE_ID);
    }
    ids[154] = ClientNodeSupport.resolve(table, AuditCreateSessionEventType.TYPE_ID);
    if (manager.getNodeConstructor(ids[154]).filter(c -> c != CONSTRUCTOR_154).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + AuditCreateSessionEventType.TYPE_ID);
    }
    ids[155] = ClientNodeSupport.resolve(table, AuditActivateSessionEventType.TYPE_ID);
    if (manager.getNodeConstructor(ids[155]).filter(c -> c != CONSTRUCTOR_155).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + AuditActivateSessionEventType.TYPE_ID);
    }
    ids[156] = ClientNodeSupport.resolve(table, AuditCancelEventType.TYPE_ID);
    if (manager.getNodeConstructor(ids[156]).filter(c -> c != CONSTRUCTOR_156).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + AuditCancelEventType.TYPE_ID);
    }
    ids[157] = ClientNodeSupport.resolve(table, AuditCertificateEventType.TYPE_ID);
    if (manager.getNodeConstructor(ids[157]).filter(c -> c != CONSTRUCTOR_157).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + AuditCertificateEventType.TYPE_ID);
    }
    ids[158] = ClientNodeSupport.resolve(table, AuditCertificateDataMismatchEventType.TYPE_ID);
    if (manager.getNodeConstructor(ids[158]).filter(c -> c != CONSTRUCTOR_158).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + AuditCertificateDataMismatchEventType.TYPE_ID);
    }
    ids[159] = ClientNodeSupport.resolve(table, AuditCertificateExpiredEventType.TYPE_ID);
    if (manager.getNodeConstructor(ids[159]).filter(c -> c != CONSTRUCTOR_159).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + AuditCertificateExpiredEventType.TYPE_ID);
    }
    ids[160] = ClientNodeSupport.resolve(table, AuditCertificateInvalidEventType.TYPE_ID);
    if (manager.getNodeConstructor(ids[160]).filter(c -> c != CONSTRUCTOR_160).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + AuditCertificateInvalidEventType.TYPE_ID);
    }
    ids[161] = ClientNodeSupport.resolve(table, AuditCertificateUntrustedEventType.TYPE_ID);
    if (manager.getNodeConstructor(ids[161]).filter(c -> c != CONSTRUCTOR_161).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + AuditCertificateUntrustedEventType.TYPE_ID);
    }
    ids[162] = ClientNodeSupport.resolve(table, AuditCertificateRevokedEventType.TYPE_ID);
    if (manager.getNodeConstructor(ids[162]).filter(c -> c != CONSTRUCTOR_162).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + AuditCertificateRevokedEventType.TYPE_ID);
    }
    ids[163] = ClientNodeSupport.resolve(table, AuditCertificateMismatchEventType.TYPE_ID);
    if (manager.getNodeConstructor(ids[163]).filter(c -> c != CONSTRUCTOR_163).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + AuditCertificateMismatchEventType.TYPE_ID);
    }
    ids[164] = ClientNodeSupport.resolve(table, AuditNodeManagementEventType.TYPE_ID);
    if (manager.getNodeConstructor(ids[164]).filter(c -> c != CONSTRUCTOR_164).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + AuditNodeManagementEventType.TYPE_ID);
    }
    ids[165] = ClientNodeSupport.resolve(table, AuditAddNodesEventType.TYPE_ID);
    if (manager.getNodeConstructor(ids[165]).filter(c -> c != CONSTRUCTOR_165).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + AuditAddNodesEventType.TYPE_ID);
    }
    ids[166] = ClientNodeSupport.resolve(table, AuditDeleteNodesEventType.TYPE_ID);
    if (manager.getNodeConstructor(ids[166]).filter(c -> c != CONSTRUCTOR_166).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + AuditDeleteNodesEventType.TYPE_ID);
    }
    ids[167] = ClientNodeSupport.resolve(table, AuditAddReferencesEventType.TYPE_ID);
    if (manager.getNodeConstructor(ids[167]).filter(c -> c != CONSTRUCTOR_167).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + AuditAddReferencesEventType.TYPE_ID);
    }
    ids[168] = ClientNodeSupport.resolve(table, AuditDeleteReferencesEventType.TYPE_ID);
    if (manager.getNodeConstructor(ids[168]).filter(c -> c != CONSTRUCTOR_168).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + AuditDeleteReferencesEventType.TYPE_ID);
    }
    ids[169] = ClientNodeSupport.resolve(table, AuditWriteUpdateEventType.TYPE_ID);
    if (manager.getNodeConstructor(ids[169]).filter(c -> c != CONSTRUCTOR_169).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + AuditWriteUpdateEventType.TYPE_ID);
    }
    ids[170] = ClientNodeSupport.resolve(table, ReaderGroupTransportType.TYPE_ID);
    if (manager.getNodeConstructor(ids[170]).filter(c -> c != CONSTRUCTOR_170).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + ReaderGroupTransportType.TYPE_ID);
    }
    ids[171] = ClientNodeSupport.resolve(table, ReaderGroupMessageType.TYPE_ID);
    if (manager.getNodeConstructor(ids[171]).filter(c -> c != CONSTRUCTOR_171).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + ReaderGroupMessageType.TYPE_ID);
    }
    ids[172] = ClientNodeSupport.resolve(table, DataSetWriterMessageType.TYPE_ID);
    if (manager.getNodeConstructor(ids[172]).filter(c -> c != CONSTRUCTOR_172).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + DataSetWriterMessageType.TYPE_ID);
    }
    ids[173] = ClientNodeSupport.resolve(table, DataSetReaderMessageType.TYPE_ID);
    if (manager.getNodeConstructor(ids[173]).filter(c -> c != CONSTRUCTOR_173).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + DataSetReaderMessageType.TYPE_ID);
    }
    ids[174] = ClientNodeSupport.resolve(table, UadpWriterGroupMessageType.TYPE_ID);
    if (manager.getNodeConstructor(ids[174]).filter(c -> c != CONSTRUCTOR_174).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + UadpWriterGroupMessageType.TYPE_ID);
    }
    ids[175] = ClientNodeSupport.resolve(table, UadpDataSetWriterMessageType.TYPE_ID);
    if (manager.getNodeConstructor(ids[175]).filter(c -> c != CONSTRUCTOR_175).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + UadpDataSetWriterMessageType.TYPE_ID);
    }
    ids[176] = ClientNodeSupport.resolve(table, UadpDataSetReaderMessageType.TYPE_ID);
    if (manager.getNodeConstructor(ids[176]).filter(c -> c != CONSTRUCTOR_176).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + UadpDataSetReaderMessageType.TYPE_ID);
    }
    ids[177] = ClientNodeSupport.resolve(table, JsonWriterGroupMessageType.TYPE_ID);
    if (manager.getNodeConstructor(ids[177]).filter(c -> c != CONSTRUCTOR_177).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + JsonWriterGroupMessageType.TYPE_ID);
    }
    ids[178] = ClientNodeSupport.resolve(table, JsonDataSetWriterMessageType.TYPE_ID);
    if (manager.getNodeConstructor(ids[178]).filter(c -> c != CONSTRUCTOR_178).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + JsonDataSetWriterMessageType.TYPE_ID);
    }
    ids[179] = ClientNodeSupport.resolve(table, JsonDataSetReaderMessageType.TYPE_ID);
    if (manager.getNodeConstructor(ids[179]).filter(c -> c != CONSTRUCTOR_179).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + JsonDataSetReaderMessageType.TYPE_ID);
    }
    ids[180] = ClientNodeSupport.resolve(table, DatagramWriterGroupTransportType.TYPE_ID);
    if (manager.getNodeConstructor(ids[180]).filter(c -> c != CONSTRUCTOR_180).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + DatagramWriterGroupTransportType.TYPE_ID);
    }
    ids[181] = ClientNodeSupport.resolve(table, BrokerWriterGroupTransportType.TYPE_ID);
    if (manager.getNodeConstructor(ids[181]).filter(c -> c != CONSTRUCTOR_181).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + BrokerWriterGroupTransportType.TYPE_ID);
    }
    ids[182] = ClientNodeSupport.resolve(table, BrokerDataSetWriterTransportType.TYPE_ID);
    if (manager.getNodeConstructor(ids[182]).filter(c -> c != CONSTRUCTOR_182).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + BrokerDataSetWriterTransportType.TYPE_ID);
    }
    ids[183] = ClientNodeSupport.resolve(table, BrokerDataSetReaderTransportType.TYPE_ID);
    if (manager.getNodeConstructor(ids[183]).filter(c -> c != CONSTRUCTOR_183).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + BrokerDataSetReaderTransportType.TYPE_ID);
    }
    ids[184] = ClientNodeSupport.resolve(table, NetworkAddressType.TYPE_ID);
    if (manager.getNodeConstructor(ids[184]).filter(c -> c != CONSTRUCTOR_184).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + NetworkAddressType.TYPE_ID);
    }
    ids[185] = ClientNodeSupport.resolve(table, NetworkAddressUrlType.TYPE_ID);
    if (manager.getNodeConstructor(ids[185]).filter(c -> c != CONSTRUCTOR_185).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + NetworkAddressUrlType.TYPE_ID);
    }
    ids[186] = ClientNodeSupport.resolve(table, DeviceFailureEventType.TYPE_ID);
    if (manager.getNodeConstructor(ids[186]).filter(c -> c != CONSTRUCTOR_186).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + DeviceFailureEventType.TYPE_ID);
    }
    ids[187] = ClientNodeSupport.resolve(table, BaseModelChangeEventType.TYPE_ID);
    if (manager.getNodeConstructor(ids[187]).filter(c -> c != CONSTRUCTOR_187).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + BaseModelChangeEventType.TYPE_ID);
    }
    ids[188] = ClientNodeSupport.resolve(table, GeneralModelChangeEventType.TYPE_ID);
    if (manager.getNodeConstructor(ids[188]).filter(c -> c != CONSTRUCTOR_188).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + GeneralModelChangeEventType.TYPE_ID);
    }
    ids[189] = ClientNodeSupport.resolve(table, InitialStateType.TYPE_ID);
    if (manager.getNodeConstructor(ids[189]).filter(c -> c != CONSTRUCTOR_189).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + InitialStateType.TYPE_ID);
    }
    ids[190] = ClientNodeSupport.resolve(table, TransitionType.TYPE_ID);
    if (manager.getNodeConstructor(ids[190]).filter(c -> c != CONSTRUCTOR_190).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + TransitionType.TYPE_ID);
    }
    ids[191] = ClientNodeSupport.resolve(table, TransitionEventType.TYPE_ID);
    if (manager.getNodeConstructor(ids[191]).filter(c -> c != CONSTRUCTOR_191).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + TransitionEventType.TYPE_ID);
    }
    ids[192] = ClientNodeSupport.resolve(table, HistoricalDataConfigurationType.TYPE_ID);
    if (manager.getNodeConstructor(ids[192]).filter(c -> c != CONSTRUCTOR_192).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + HistoricalDataConfigurationType.TYPE_ID);
    }
    ids[193] = ClientNodeSupport.resolve(table, HistoryServerCapabilitiesType.TYPE_ID);
    if (manager.getNodeConstructor(ids[193]).filter(c -> c != CONSTRUCTOR_193).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + HistoryServerCapabilitiesType.TYPE_ID);
    }
    ids[194] = ClientNodeSupport.resolve(table, AggregateFunctionType.TYPE_ID);
    if (manager.getNodeConstructor(ids[194]).filter(c -> c != CONSTRUCTOR_194).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + AggregateFunctionType.TYPE_ID);
    }
    ids[195] = ClientNodeSupport.resolve(table, AliasNameType.TYPE_ID);
    if (manager.getNodeConstructor(ids[195]).filter(c -> c != CONSTRUCTOR_195).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + AliasNameType.TYPE_ID);
    }
    ids[196] = ClientNodeSupport.resolve(table, AliasNameCategoryType.TYPE_ID);
    if (manager.getNodeConstructor(ids[196]).filter(c -> c != CONSTRUCTOR_196).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + AliasNameCategoryType.TYPE_ID);
    }
    ids[197] = ClientNodeSupport.resolve(table, IOrderedObjectType.TYPE_ID);
    if (manager.getNodeConstructor(ids[197]).filter(c -> c != CONSTRUCTOR_197).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + IOrderedObjectType.TYPE_ID);
    }
    ids[198] = ClientNodeSupport.resolve(table, OrderedListType.TYPE_ID);
    if (manager.getNodeConstructor(ids[198]).filter(c -> c != CONSTRUCTOR_198).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + OrderedListType.TYPE_ID);
    }
    ids[199] = ClientNodeSupport.resolve(table, EccApplicationCertificateType.TYPE_ID);
    if (manager.getNodeConstructor(ids[199]).filter(c -> c != CONSTRUCTOR_199).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + EccApplicationCertificateType.TYPE_ID);
    }
    ids[200] = ClientNodeSupport.resolve(table, EccNistP256ApplicationCertificateType.TYPE_ID);
    if (manager.getNodeConstructor(ids[200]).filter(c -> c != CONSTRUCTOR_200).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + EccNistP256ApplicationCertificateType.TYPE_ID);
    }
    ids[201] = ClientNodeSupport.resolve(table, EccNistP384ApplicationCertificateType.TYPE_ID);
    if (manager.getNodeConstructor(ids[201]).filter(c -> c != CONSTRUCTOR_201).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + EccNistP384ApplicationCertificateType.TYPE_ID);
    }
    ids[202] =
        ClientNodeSupport.resolve(table, EccBrainpoolP256r1ApplicationCertificateType.TYPE_ID);
    if (manager.getNodeConstructor(ids[202]).filter(c -> c != CONSTRUCTOR_202).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + EccBrainpoolP256r1ApplicationCertificateType.TYPE_ID);
    }
    ids[203] =
        ClientNodeSupport.resolve(table, EccBrainpoolP384r1ApplicationCertificateType.TYPE_ID);
    if (manager.getNodeConstructor(ids[203]).filter(c -> c != CONSTRUCTOR_203).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + EccBrainpoolP384r1ApplicationCertificateType.TYPE_ID);
    }
    ids[204] = ClientNodeSupport.resolve(table, EccCurve25519ApplicationCertificateType.TYPE_ID);
    if (manager.getNodeConstructor(ids[204]).filter(c -> c != CONSTRUCTOR_204).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + EccCurve25519ApplicationCertificateType.TYPE_ID);
    }
    ids[205] = ClientNodeSupport.resolve(table, EccCurve448ApplicationCertificateType.TYPE_ID);
    if (manager.getNodeConstructor(ids[205]).filter(c -> c != CONSTRUCTOR_205).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + EccCurve448ApplicationCertificateType.TYPE_ID);
    }
    ids[206] =
        ClientNodeSupport.resolve(table, AuthorizationServicesConfigurationFolderType.TYPE_ID);
    if (manager.getNodeConstructor(ids[206]).filter(c -> c != CONSTRUCTOR_206).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + AuthorizationServicesConfigurationFolderType.TYPE_ID);
    }
    ids[207] = ClientNodeSupport.resolve(table, AuditClientEventType.TYPE_ID);
    if (manager.getNodeConstructor(ids[207]).filter(c -> c != CONSTRUCTOR_207).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + AuditClientEventType.TYPE_ID);
    }
    ids[208] = ClientNodeSupport.resolve(table, ProgramTransitionEventType.TYPE_ID);
    if (manager.getNodeConstructor(ids[208]).filter(c -> c != CONSTRUCTOR_208).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + ProgramTransitionEventType.TYPE_ID);
    }
    ids[209] = ClientNodeSupport.resolve(table, SubscribedDataSetFolderType.TYPE_ID);
    if (manager.getNodeConstructor(ids[209]).filter(c -> c != CONSTRUCTOR_209).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + SubscribedDataSetFolderType.TYPE_ID);
    }
    ids[210] = ClientNodeSupport.resolve(table, StandaloneSubscribedDataSetType.TYPE_ID);
    if (manager.getNodeConstructor(ids[210]).filter(c -> c != CONSTRUCTOR_210).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + StandaloneSubscribedDataSetType.TYPE_ID);
    }
    ids[211] = ClientNodeSupport.resolve(table, PubSubCapabilitiesType.TYPE_ID);
    if (manager.getNodeConstructor(ids[211]).filter(c -> c != CONSTRUCTOR_211).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + PubSubCapabilitiesType.TYPE_ID);
    }
    ids[212] = ClientNodeSupport.resolve(table, ProgramStateMachineType.TYPE_ID);
    if (manager.getNodeConstructor(ids[212]).filter(c -> c != CONSTRUCTOR_212).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + ProgramStateMachineType.TYPE_ID);
    }
    ids[213] = ClientNodeSupport.resolve(table, AuditClientUpdateMethodResultEventType.TYPE_ID);
    if (manager.getNodeConstructor(ids[213]).filter(c -> c != CONSTRUCTOR_213).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + AuditClientUpdateMethodResultEventType.TYPE_ID);
    }
    ids[214] = ClientNodeSupport.resolve(table, DatagramDataSetReaderTransportType.TYPE_ID);
    if (manager.getNodeConstructor(ids[214]).filter(c -> c != CONSTRUCTOR_214).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + DatagramDataSetReaderTransportType.TYPE_ID);
    }
    ids[215] = ClientNodeSupport.resolve(table, IIetfBaseNetworkInterfaceType.TYPE_ID);
    if (manager.getNodeConstructor(ids[215]).filter(c -> c != CONSTRUCTOR_215).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + IIetfBaseNetworkInterfaceType.TYPE_ID);
    }
    ids[216] = ClientNodeSupport.resolve(table, IIeeeBaseEthernetPortType.TYPE_ID);
    if (manager.getNodeConstructor(ids[216]).filter(c -> c != CONSTRUCTOR_216).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + IIeeeBaseEthernetPortType.TYPE_ID);
    }
    ids[217] = ClientNodeSupport.resolve(table, IBaseEthernetCapabilitiesType.TYPE_ID);
    if (manager.getNodeConstructor(ids[217]).filter(c -> c != CONSTRUCTOR_217).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + IBaseEthernetCapabilitiesType.TYPE_ID);
    }
    ids[218] = ClientNodeSupport.resolve(table, ISrClassType.TYPE_ID);
    if (manager.getNodeConstructor(ids[218]).filter(c -> c != CONSTRUCTOR_218).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + ISrClassType.TYPE_ID);
    }
    ids[219] = ClientNodeSupport.resolve(table, IIeeeBaseTsnStreamType.TYPE_ID);
    if (manager.getNodeConstructor(ids[219]).filter(c -> c != CONSTRUCTOR_219).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + IIeeeBaseTsnStreamType.TYPE_ID);
    }
    ids[220] = ClientNodeSupport.resolve(table, IIeeeBaseTsnTrafficSpecificationType.TYPE_ID);
    if (manager.getNodeConstructor(ids[220]).filter(c -> c != CONSTRUCTOR_220).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + IIeeeBaseTsnTrafficSpecificationType.TYPE_ID);
    }
    ids[221] = ClientNodeSupport.resolve(table, IIeeeBaseTsnStatusStreamType.TYPE_ID);
    if (manager.getNodeConstructor(ids[221]).filter(c -> c != CONSTRUCTOR_221).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + IIeeeBaseTsnStatusStreamType.TYPE_ID);
    }
    ids[222] = ClientNodeSupport.resolve(table, IIeeeTsnInterfaceConfigurationType.TYPE_ID);
    if (manager.getNodeConstructor(ids[222]).filter(c -> c != CONSTRUCTOR_222).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + IIeeeTsnInterfaceConfigurationType.TYPE_ID);
    }
    ids[223] = ClientNodeSupport.resolve(table, IIeeeTsnInterfaceConfigurationTalkerType.TYPE_ID);
    if (manager.getNodeConstructor(ids[223]).filter(c -> c != CONSTRUCTOR_223).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + IIeeeTsnInterfaceConfigurationTalkerType.TYPE_ID);
    }
    ids[224] = ClientNodeSupport.resolve(table, IIeeeTsnInterfaceConfigurationListenerType.TYPE_ID);
    if (manager.getNodeConstructor(ids[224]).filter(c -> c != CONSTRUCTOR_224).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + IIeeeTsnInterfaceConfigurationListenerType.TYPE_ID);
    }
    ids[225] = ClientNodeSupport.resolve(table, IIeeeTsnMacAddressType.TYPE_ID);
    if (manager.getNodeConstructor(ids[225]).filter(c -> c != CONSTRUCTOR_225).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + IIeeeTsnMacAddressType.TYPE_ID);
    }
    ids[226] = ClientNodeSupport.resolve(table, IIeeeTsnVlanTagType.TYPE_ID);
    if (manager.getNodeConstructor(ids[226]).filter(c -> c != CONSTRUCTOR_226).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + IIeeeTsnVlanTagType.TYPE_ID);
    }
    ids[227] = ClientNodeSupport.resolve(table, IPriorityMappingEntryType.TYPE_ID);
    if (manager.getNodeConstructor(ids[227]).filter(c -> c != CONSTRUCTOR_227).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + IPriorityMappingEntryType.TYPE_ID);
    }
    ids[228] = ClientNodeSupport.resolve(table, IIeeeAutoNegotiationStatusType.TYPE_ID);
    if (manager.getNodeConstructor(ids[228]).filter(c -> c != CONSTRUCTOR_228).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + IIeeeAutoNegotiationStatusType.TYPE_ID);
    }
    ids[229] = ClientNodeSupport.resolve(table, UserManagementType.TYPE_ID);
    if (manager.getNodeConstructor(ids[229]).filter(c -> c != CONSTRUCTOR_229).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + UserManagementType.TYPE_ID);
    }
    ids[230] = ClientNodeSupport.resolve(table, IVlanIdType.TYPE_ID);
    if (manager.getNodeConstructor(ids[230]).filter(c -> c != CONSTRUCTOR_230).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + IVlanIdType.TYPE_ID);
    }
    ids[231] = ClientNodeSupport.resolve(table, IetfBaseNetworkInterfaceType.TYPE_ID);
    if (manager.getNodeConstructor(ids[231]).filter(c -> c != CONSTRUCTOR_231).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + IetfBaseNetworkInterfaceType.TYPE_ID);
    }
    ids[232] = ClientNodeSupport.resolve(table, PriorityMappingTableType.TYPE_ID);
    if (manager.getNodeConstructor(ids[232]).filter(c -> c != CONSTRUCTOR_232).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + PriorityMappingTableType.TYPE_ID);
    }
    ids[233] = ClientNodeSupport.resolve(table, PubSubKeyPushTargetType.TYPE_ID);
    if (manager.getNodeConstructor(ids[233]).filter(c -> c != CONSTRUCTOR_233).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + PubSubKeyPushTargetType.TYPE_ID);
    }
    ids[234] = ClientNodeSupport.resolve(table, PubSubKeyPushTargetFolderType.TYPE_ID);
    if (manager.getNodeConstructor(ids[234]).filter(c -> c != CONSTRUCTOR_234).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + PubSubKeyPushTargetFolderType.TYPE_ID);
    }
    ids[235] = ClientNodeSupport.resolve(table, PubSubConfigurationType.TYPE_ID);
    if (manager.getNodeConstructor(ids[235]).filter(c -> c != CONSTRUCTOR_235).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + PubSubConfigurationType.TYPE_ID);
    }
    ids[236] = ClientNodeSupport.resolve(table, ApplicationConfigurationType.TYPE_ID);
    if (manager.getNodeConstructor(ids[236]).filter(c -> c != CONSTRUCTOR_236).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + ApplicationConfigurationType.TYPE_ID);
    }
    ids[237] = ClientNodeSupport.resolve(table, ProvisionableDeviceType.TYPE_ID);
    if (manager.getNodeConstructor(ids[237]).filter(c -> c != CONSTRUCTOR_237).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + ProvisionableDeviceType.TYPE_ID);
    }
    ids[238] = ClientNodeSupport.resolve(table, SemanticChangeEventType.TYPE_ID);
    if (manager.getNodeConstructor(ids[238]).filter(c -> c != CONSTRUCTOR_238).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + SemanticChangeEventType.TYPE_ID);
    }
    ids[239] = ClientNodeSupport.resolve(table, AuditUrlMismatchEventType.TYPE_ID);
    if (manager.getNodeConstructor(ids[239]).filter(c -> c != CONSTRUCTOR_239).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + AuditUrlMismatchEventType.TYPE_ID);
    }
    ids[240] = ClientNodeSupport.resolve(table, RefreshStartEventType.TYPE_ID);
    if (manager.getNodeConstructor(ids[240]).filter(c -> c != CONSTRUCTOR_240).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + RefreshStartEventType.TYPE_ID);
    }
    ids[241] = ClientNodeSupport.resolve(table, RefreshEndEventType.TYPE_ID);
    if (manager.getNodeConstructor(ids[241]).filter(c -> c != CONSTRUCTOR_241).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + RefreshEndEventType.TYPE_ID);
    }
    ids[242] = ClientNodeSupport.resolve(table, RefreshRequiredEventType.TYPE_ID);
    if (manager.getNodeConstructor(ids[242]).filter(c -> c != CONSTRUCTOR_242).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + RefreshRequiredEventType.TYPE_ID);
    }
    ids[243] = ClientNodeSupport.resolve(table, AuditConditionEnableEventType.TYPE_ID);
    if (manager.getNodeConstructor(ids[243]).filter(c -> c != CONSTRUCTOR_243).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + AuditConditionEnableEventType.TYPE_ID);
    }
    ids[244] = ClientNodeSupport.resolve(table, AuditConditionCommentEventType.TYPE_ID);
    if (manager.getNodeConstructor(ids[244]).filter(c -> c != CONSTRUCTOR_244).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + AuditConditionCommentEventType.TYPE_ID);
    }
    ids[245] = ClientNodeSupport.resolve(table, DialogConditionType.TYPE_ID);
    if (manager.getNodeConstructor(ids[245]).filter(c -> c != CONSTRUCTOR_245).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + DialogConditionType.TYPE_ID);
    }
    ids[246] = ClientNodeSupport.resolve(table, ShelvedStateMachineType.TYPE_ID);
    if (manager.getNodeConstructor(ids[246]).filter(c -> c != CONSTRUCTOR_246).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + ShelvedStateMachineType.TYPE_ID);
    }
    ids[247] = ClientNodeSupport.resolve(table, AuditHistoryEventUpdateEventType.TYPE_ID);
    if (manager.getNodeConstructor(ids[247]).filter(c -> c != CONSTRUCTOR_247).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + AuditHistoryEventUpdateEventType.TYPE_ID);
    }
    ids[248] = ClientNodeSupport.resolve(table, AuditHistoryValueUpdateEventType.TYPE_ID);
    if (manager.getNodeConstructor(ids[248]).filter(c -> c != CONSTRUCTOR_248).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + AuditHistoryValueUpdateEventType.TYPE_ID);
    }
    ids[249] = ClientNodeSupport.resolve(table, AuditHistoryDeleteEventType.TYPE_ID);
    if (manager.getNodeConstructor(ids[249]).filter(c -> c != CONSTRUCTOR_249).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + AuditHistoryDeleteEventType.TYPE_ID);
    }
    ids[250] = ClientNodeSupport.resolve(table, AuditHistoryRawModifyDeleteEventType.TYPE_ID);
    if (manager.getNodeConstructor(ids[250]).filter(c -> c != CONSTRUCTOR_250).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + AuditHistoryRawModifyDeleteEventType.TYPE_ID);
    }
    ids[251] = ClientNodeSupport.resolve(table, AuditHistoryAtTimeDeleteEventType.TYPE_ID);
    if (manager.getNodeConstructor(ids[251]).filter(c -> c != CONSTRUCTOR_251).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + AuditHistoryAtTimeDeleteEventType.TYPE_ID);
    }
    ids[252] = ClientNodeSupport.resolve(table, AuditHistoryEventDeleteEventType.TYPE_ID);
    if (manager.getNodeConstructor(ids[252]).filter(c -> c != CONSTRUCTOR_252).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + AuditHistoryEventDeleteEventType.TYPE_ID);
    }
    ids[253] = ClientNodeSupport.resolve(table, EventQueueOverflowEventType.TYPE_ID);
    if (manager.getNodeConstructor(ids[253]).filter(c -> c != CONSTRUCTOR_253).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + EventQueueOverflowEventType.TYPE_ID);
    }
    ids[254] = ClientNodeSupport.resolve(table, AlarmSuppressionGroupType.TYPE_ID);
    if (manager.getNodeConstructor(ids[254]).filter(c -> c != CONSTRUCTOR_254).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + AlarmSuppressionGroupType.TYPE_ID);
    }
    ids[255] = ClientNodeSupport.resolve(table, TrustListUpdateRequestedAuditEventType.TYPE_ID);
    if (manager.getNodeConstructor(ids[255]).filter(c -> c != CONSTRUCTOR_255).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + TrustListUpdateRequestedAuditEventType.TYPE_ID);
    }
  }

  private static void install1(ObjectTypeManager manager, NodeId[] ids) {
    if (manager.getNodeConstructor(ids[128]).isEmpty()) {
      manager.registerObjectType(ids[128], TlsClientCertificateTypeNode.class, CONSTRUCTOR_128);
    }
    if (manager.getNodeConstructor(ids[129]).isEmpty()) {
      manager.registerObjectType(ids[129], LogObjectTypeNode.class, CONSTRUCTOR_129);
    }
    if (manager.getNodeConstructor(ids[130]).isEmpty()) {
      manager.registerObjectType(ids[130], BaseLogEventTypeNode.class, CONSTRUCTOR_130);
    }
    if (manager.getNodeConstructor(ids[131]).isEmpty()) {
      manager.registerObjectType(ids[131], LogOverflowEventTypeNode.class, CONSTRUCTOR_131);
    }
    if (manager.getNodeConstructor(ids[132]).isEmpty()) {
      manager.registerObjectType(ids[132], LogEntryConditionClassTypeNode.class, CONSTRUCTOR_132);
    }
    if (manager.getNodeConstructor(ids[133]).isEmpty()) {
      manager.registerObjectType(ids[133], PubSubDiagnosticsTypeNode.class, CONSTRUCTOR_133);
    }
    if (manager.getNodeConstructor(ids[134]).isEmpty()) {
      manager.registerObjectType(ids[134], PubSubDiagnosticsRootTypeNode.class, CONSTRUCTOR_134);
    }
    if (manager.getNodeConstructor(ids[135]).isEmpty()) {
      manager.registerObjectType(
          ids[135], PubSubDiagnosticsConnectionTypeNode.class, CONSTRUCTOR_135);
    }
    if (manager.getNodeConstructor(ids[136]).isEmpty()) {
      manager.registerObjectType(ids[136], DataTypeRefinementTypeNode.class, CONSTRUCTOR_136);
    }
    if (manager.getNodeConstructor(ids[137]).isEmpty()) {
      manager.registerObjectType(ids[137], SubtypeRestrictionTypeNode.class, CONSTRUCTOR_137);
    }
    if (manager.getNodeConstructor(ids[138]).isEmpty()) {
      manager.registerObjectType(ids[138], SerializationEntityTypeNode.class, CONSTRUCTOR_138);
    }
    if (manager.getNodeConstructor(ids[139]).isEmpty()) {
      manager.registerObjectType(
          ids[139], PubSubDiagnosticsWriterGroupTypeNode.class, CONSTRUCTOR_139);
    }
    if (manager.getNodeConstructor(ids[140]).isEmpty()) {
      manager.registerObjectType(
          ids[140], PubSubDiagnosticsReaderGroupTypeNode.class, CONSTRUCTOR_140);
    }
    if (manager.getNodeConstructor(ids[141]).isEmpty()) {
      manager.registerObjectType(
          ids[141], PubSubDiagnosticsDataSetWriterTypeNode.class, CONSTRUCTOR_141);
    }
    if (manager.getNodeConstructor(ids[142]).isEmpty()) {
      manager.registerObjectType(
          ids[142], PubSubDiagnosticsDataSetReaderTypeNode.class, CONSTRUCTOR_142);
    }
    if (manager.getNodeConstructor(ids[143]).isEmpty()) {
      manager.registerObjectType(ids[143], ServerTypeNode.class, CONSTRUCTOR_143);
    }
    if (manager.getNodeConstructor(ids[144]).isEmpty()) {
      manager.registerObjectType(ids[144], ServerCapabilitiesTypeNode.class, CONSTRUCTOR_144);
    }
    if (manager.getNodeConstructor(ids[145]).isEmpty()) {
      manager.registerObjectType(ids[145], ServerDiagnosticsTypeNode.class, CONSTRUCTOR_145);
    }
    if (manager.getNodeConstructor(ids[146]).isEmpty()) {
      manager.registerObjectType(
          ids[146], SessionsDiagnosticsSummaryTypeNode.class, CONSTRUCTOR_146);
    }
    if (manager.getNodeConstructor(ids[147]).isEmpty()) {
      manager.registerObjectType(ids[147], SessionDiagnosticsObjectTypeNode.class, CONSTRUCTOR_147);
    }
    if (manager.getNodeConstructor(ids[148]).isEmpty()) {
      manager.registerObjectType(ids[148], VendorServerInfoTypeNode.class, CONSTRUCTOR_148);
    }
    if (manager.getNodeConstructor(ids[149]).isEmpty()) {
      manager.registerObjectType(ids[149], TransparentRedundancyTypeNode.class, CONSTRUCTOR_149);
    }
    if (manager.getNodeConstructor(ids[150]).isEmpty()) {
      manager.registerObjectType(ids[150], AuditSecurityEventTypeNode.class, CONSTRUCTOR_150);
    }
    if (manager.getNodeConstructor(ids[151]).isEmpty()) {
      manager.registerObjectType(ids[151], AuditChannelEventTypeNode.class, CONSTRUCTOR_151);
    }
    if (manager.getNodeConstructor(ids[152]).isEmpty()) {
      manager.registerObjectType(
          ids[152], AuditOpenSecureChannelEventTypeNode.class, CONSTRUCTOR_152);
    }
    if (manager.getNodeConstructor(ids[153]).isEmpty()) {
      manager.registerObjectType(ids[153], AuditSessionEventTypeNode.class, CONSTRUCTOR_153);
    }
    if (manager.getNodeConstructor(ids[154]).isEmpty()) {
      manager.registerObjectType(ids[154], AuditCreateSessionEventTypeNode.class, CONSTRUCTOR_154);
    }
    if (manager.getNodeConstructor(ids[155]).isEmpty()) {
      manager.registerObjectType(
          ids[155], AuditActivateSessionEventTypeNode.class, CONSTRUCTOR_155);
    }
    if (manager.getNodeConstructor(ids[156]).isEmpty()) {
      manager.registerObjectType(ids[156], AuditCancelEventTypeNode.class, CONSTRUCTOR_156);
    }
    if (manager.getNodeConstructor(ids[157]).isEmpty()) {
      manager.registerObjectType(ids[157], AuditCertificateEventTypeNode.class, CONSTRUCTOR_157);
    }
    if (manager.getNodeConstructor(ids[158]).isEmpty()) {
      manager.registerObjectType(
          ids[158], AuditCertificateDataMismatchEventTypeNode.class, CONSTRUCTOR_158);
    }
    if (manager.getNodeConstructor(ids[159]).isEmpty()) {
      manager.registerObjectType(
          ids[159], AuditCertificateExpiredEventTypeNode.class, CONSTRUCTOR_159);
    }
    if (manager.getNodeConstructor(ids[160]).isEmpty()) {
      manager.registerObjectType(
          ids[160], AuditCertificateInvalidEventTypeNode.class, CONSTRUCTOR_160);
    }
    if (manager.getNodeConstructor(ids[161]).isEmpty()) {
      manager.registerObjectType(
          ids[161], AuditCertificateUntrustedEventTypeNode.class, CONSTRUCTOR_161);
    }
    if (manager.getNodeConstructor(ids[162]).isEmpty()) {
      manager.registerObjectType(
          ids[162], AuditCertificateRevokedEventTypeNode.class, CONSTRUCTOR_162);
    }
    if (manager.getNodeConstructor(ids[163]).isEmpty()) {
      manager.registerObjectType(
          ids[163], AuditCertificateMismatchEventTypeNode.class, CONSTRUCTOR_163);
    }
    if (manager.getNodeConstructor(ids[164]).isEmpty()) {
      manager.registerObjectType(ids[164], AuditNodeManagementEventTypeNode.class, CONSTRUCTOR_164);
    }
    if (manager.getNodeConstructor(ids[165]).isEmpty()) {
      manager.registerObjectType(ids[165], AuditAddNodesEventTypeNode.class, CONSTRUCTOR_165);
    }
    if (manager.getNodeConstructor(ids[166]).isEmpty()) {
      manager.registerObjectType(ids[166], AuditDeleteNodesEventTypeNode.class, CONSTRUCTOR_166);
    }
    if (manager.getNodeConstructor(ids[167]).isEmpty()) {
      manager.registerObjectType(ids[167], AuditAddReferencesEventTypeNode.class, CONSTRUCTOR_167);
    }
    if (manager.getNodeConstructor(ids[168]).isEmpty()) {
      manager.registerObjectType(
          ids[168], AuditDeleteReferencesEventTypeNode.class, CONSTRUCTOR_168);
    }
    if (manager.getNodeConstructor(ids[169]).isEmpty()) {
      manager.registerObjectType(ids[169], AuditWriteUpdateEventTypeNode.class, CONSTRUCTOR_169);
    }
    if (manager.getNodeConstructor(ids[170]).isEmpty()) {
      manager.registerObjectType(ids[170], ReaderGroupTransportTypeNode.class, CONSTRUCTOR_170);
    }
    if (manager.getNodeConstructor(ids[171]).isEmpty()) {
      manager.registerObjectType(ids[171], ReaderGroupMessageTypeNode.class, CONSTRUCTOR_171);
    }
    if (manager.getNodeConstructor(ids[172]).isEmpty()) {
      manager.registerObjectType(ids[172], DataSetWriterMessageTypeNode.class, CONSTRUCTOR_172);
    }
    if (manager.getNodeConstructor(ids[173]).isEmpty()) {
      manager.registerObjectType(ids[173], DataSetReaderMessageTypeNode.class, CONSTRUCTOR_173);
    }
    if (manager.getNodeConstructor(ids[174]).isEmpty()) {
      manager.registerObjectType(ids[174], UadpWriterGroupMessageTypeNode.class, CONSTRUCTOR_174);
    }
    if (manager.getNodeConstructor(ids[175]).isEmpty()) {
      manager.registerObjectType(ids[175], UadpDataSetWriterMessageTypeNode.class, CONSTRUCTOR_175);
    }
    if (manager.getNodeConstructor(ids[176]).isEmpty()) {
      manager.registerObjectType(ids[176], UadpDataSetReaderMessageTypeNode.class, CONSTRUCTOR_176);
    }
    if (manager.getNodeConstructor(ids[177]).isEmpty()) {
      manager.registerObjectType(ids[177], JsonWriterGroupMessageTypeNode.class, CONSTRUCTOR_177);
    }
    if (manager.getNodeConstructor(ids[178]).isEmpty()) {
      manager.registerObjectType(ids[178], JsonDataSetWriterMessageTypeNode.class, CONSTRUCTOR_178);
    }
    if (manager.getNodeConstructor(ids[179]).isEmpty()) {
      manager.registerObjectType(ids[179], JsonDataSetReaderMessageTypeNode.class, CONSTRUCTOR_179);
    }
    if (manager.getNodeConstructor(ids[180]).isEmpty()) {
      manager.registerObjectType(
          ids[180], DatagramWriterGroupTransportTypeNode.class, CONSTRUCTOR_180);
    }
    if (manager.getNodeConstructor(ids[181]).isEmpty()) {
      manager.registerObjectType(
          ids[181], BrokerWriterGroupTransportTypeNode.class, CONSTRUCTOR_181);
    }
    if (manager.getNodeConstructor(ids[182]).isEmpty()) {
      manager.registerObjectType(
          ids[182], BrokerDataSetWriterTransportTypeNode.class, CONSTRUCTOR_182);
    }
    if (manager.getNodeConstructor(ids[183]).isEmpty()) {
      manager.registerObjectType(
          ids[183], BrokerDataSetReaderTransportTypeNode.class, CONSTRUCTOR_183);
    }
    if (manager.getNodeConstructor(ids[184]).isEmpty()) {
      manager.registerObjectType(ids[184], NetworkAddressTypeNode.class, CONSTRUCTOR_184);
    }
    if (manager.getNodeConstructor(ids[185]).isEmpty()) {
      manager.registerObjectType(ids[185], NetworkAddressUrlTypeNode.class, CONSTRUCTOR_185);
    }
    if (manager.getNodeConstructor(ids[186]).isEmpty()) {
      manager.registerObjectType(ids[186], DeviceFailureEventTypeNode.class, CONSTRUCTOR_186);
    }
    if (manager.getNodeConstructor(ids[187]).isEmpty()) {
      manager.registerObjectType(ids[187], BaseModelChangeEventTypeNode.class, CONSTRUCTOR_187);
    }
    if (manager.getNodeConstructor(ids[188]).isEmpty()) {
      manager.registerObjectType(ids[188], GeneralModelChangeEventTypeNode.class, CONSTRUCTOR_188);
    }
    if (manager.getNodeConstructor(ids[189]).isEmpty()) {
      manager.registerObjectType(ids[189], InitialStateTypeNode.class, CONSTRUCTOR_189);
    }
    if (manager.getNodeConstructor(ids[190]).isEmpty()) {
      manager.registerObjectType(ids[190], TransitionTypeNode.class, CONSTRUCTOR_190);
    }
    if (manager.getNodeConstructor(ids[191]).isEmpty()) {
      manager.registerObjectType(ids[191], TransitionEventTypeNode.class, CONSTRUCTOR_191);
    }
    if (manager.getNodeConstructor(ids[192]).isEmpty()) {
      manager.registerObjectType(
          ids[192], HistoricalDataConfigurationTypeNode.class, CONSTRUCTOR_192);
    }
    if (manager.getNodeConstructor(ids[193]).isEmpty()) {
      manager.registerObjectType(
          ids[193], HistoryServerCapabilitiesTypeNode.class, CONSTRUCTOR_193);
    }
    if (manager.getNodeConstructor(ids[194]).isEmpty()) {
      manager.registerObjectType(ids[194], AggregateFunctionTypeNode.class, CONSTRUCTOR_194);
    }
    if (manager.getNodeConstructor(ids[195]).isEmpty()) {
      manager.registerObjectType(ids[195], AliasNameTypeNode.class, CONSTRUCTOR_195);
    }
    if (manager.getNodeConstructor(ids[196]).isEmpty()) {
      manager.registerObjectType(ids[196], AliasNameCategoryTypeNode.class, CONSTRUCTOR_196);
    }
    if (manager.getNodeConstructor(ids[197]).isEmpty()) {
      manager.registerObjectType(ids[197], IOrderedObjectTypeNode.class, CONSTRUCTOR_197);
    }
    if (manager.getNodeConstructor(ids[198]).isEmpty()) {
      manager.registerObjectType(ids[198], OrderedListTypeNode.class, CONSTRUCTOR_198);
    }
    if (manager.getNodeConstructor(ids[199]).isEmpty()) {
      manager.registerObjectType(
          ids[199], EccApplicationCertificateTypeNode.class, CONSTRUCTOR_199);
    }
    if (manager.getNodeConstructor(ids[200]).isEmpty()) {
      manager.registerObjectType(
          ids[200], EccNistP256ApplicationCertificateTypeNode.class, CONSTRUCTOR_200);
    }
    if (manager.getNodeConstructor(ids[201]).isEmpty()) {
      manager.registerObjectType(
          ids[201], EccNistP384ApplicationCertificateTypeNode.class, CONSTRUCTOR_201);
    }
    if (manager.getNodeConstructor(ids[202]).isEmpty()) {
      manager.registerObjectType(
          ids[202], EccBrainpoolP256r1ApplicationCertificateTypeNode.class, CONSTRUCTOR_202);
    }
    if (manager.getNodeConstructor(ids[203]).isEmpty()) {
      manager.registerObjectType(
          ids[203], EccBrainpoolP384r1ApplicationCertificateTypeNode.class, CONSTRUCTOR_203);
    }
    if (manager.getNodeConstructor(ids[204]).isEmpty()) {
      manager.registerObjectType(
          ids[204], EccCurve25519ApplicationCertificateTypeNode.class, CONSTRUCTOR_204);
    }
    if (manager.getNodeConstructor(ids[205]).isEmpty()) {
      manager.registerObjectType(
          ids[205], EccCurve448ApplicationCertificateTypeNode.class, CONSTRUCTOR_205);
    }
    if (manager.getNodeConstructor(ids[206]).isEmpty()) {
      manager.registerObjectType(
          ids[206], AuthorizationServicesConfigurationFolderTypeNode.class, CONSTRUCTOR_206);
    }
    if (manager.getNodeConstructor(ids[207]).isEmpty()) {
      manager.registerObjectType(ids[207], AuditClientEventTypeNode.class, CONSTRUCTOR_207);
    }
    if (manager.getNodeConstructor(ids[208]).isEmpty()) {
      manager.registerObjectType(ids[208], ProgramTransitionEventTypeNode.class, CONSTRUCTOR_208);
    }
    if (manager.getNodeConstructor(ids[209]).isEmpty()) {
      manager.registerObjectType(ids[209], SubscribedDataSetFolderTypeNode.class, CONSTRUCTOR_209);
    }
    if (manager.getNodeConstructor(ids[210]).isEmpty()) {
      manager.registerObjectType(
          ids[210], StandaloneSubscribedDataSetTypeNode.class, CONSTRUCTOR_210);
    }
    if (manager.getNodeConstructor(ids[211]).isEmpty()) {
      manager.registerObjectType(ids[211], PubSubCapabilitiesTypeNode.class, CONSTRUCTOR_211);
    }
    if (manager.getNodeConstructor(ids[212]).isEmpty()) {
      manager.registerObjectType(ids[212], ProgramStateMachineTypeNode.class, CONSTRUCTOR_212);
    }
    if (manager.getNodeConstructor(ids[213]).isEmpty()) {
      manager.registerObjectType(
          ids[213], AuditClientUpdateMethodResultEventTypeNode.class, CONSTRUCTOR_213);
    }
    if (manager.getNodeConstructor(ids[214]).isEmpty()) {
      manager.registerObjectType(
          ids[214], DatagramDataSetReaderTransportTypeNode.class, CONSTRUCTOR_214);
    }
    if (manager.getNodeConstructor(ids[215]).isEmpty()) {
      manager.registerObjectType(
          ids[215], IIetfBaseNetworkInterfaceTypeNode.class, CONSTRUCTOR_215);
    }
    if (manager.getNodeConstructor(ids[216]).isEmpty()) {
      manager.registerObjectType(ids[216], IIeeeBaseEthernetPortTypeNode.class, CONSTRUCTOR_216);
    }
    if (manager.getNodeConstructor(ids[217]).isEmpty()) {
      manager.registerObjectType(
          ids[217], IBaseEthernetCapabilitiesTypeNode.class, CONSTRUCTOR_217);
    }
    if (manager.getNodeConstructor(ids[218]).isEmpty()) {
      manager.registerObjectType(ids[218], ISrClassTypeNode.class, CONSTRUCTOR_218);
    }
    if (manager.getNodeConstructor(ids[219]).isEmpty()) {
      manager.registerObjectType(ids[219], IIeeeBaseTsnStreamTypeNode.class, CONSTRUCTOR_219);
    }
    if (manager.getNodeConstructor(ids[220]).isEmpty()) {
      manager.registerObjectType(
          ids[220], IIeeeBaseTsnTrafficSpecificationTypeNode.class, CONSTRUCTOR_220);
    }
    if (manager.getNodeConstructor(ids[221]).isEmpty()) {
      manager.registerObjectType(ids[221], IIeeeBaseTsnStatusStreamTypeNode.class, CONSTRUCTOR_221);
    }
    if (manager.getNodeConstructor(ids[222]).isEmpty()) {
      manager.registerObjectType(
          ids[222], IIeeeTsnInterfaceConfigurationTypeNode.class, CONSTRUCTOR_222);
    }
    if (manager.getNodeConstructor(ids[223]).isEmpty()) {
      manager.registerObjectType(
          ids[223], IIeeeTsnInterfaceConfigurationTalkerTypeNode.class, CONSTRUCTOR_223);
    }
    if (manager.getNodeConstructor(ids[224]).isEmpty()) {
      manager.registerObjectType(
          ids[224], IIeeeTsnInterfaceConfigurationListenerTypeNode.class, CONSTRUCTOR_224);
    }
    if (manager.getNodeConstructor(ids[225]).isEmpty()) {
      manager.registerObjectType(ids[225], IIeeeTsnMacAddressTypeNode.class, CONSTRUCTOR_225);
    }
    if (manager.getNodeConstructor(ids[226]).isEmpty()) {
      manager.registerObjectType(ids[226], IIeeeTsnVlanTagTypeNode.class, CONSTRUCTOR_226);
    }
    if (manager.getNodeConstructor(ids[227]).isEmpty()) {
      manager.registerObjectType(ids[227], IPriorityMappingEntryTypeNode.class, CONSTRUCTOR_227);
    }
    if (manager.getNodeConstructor(ids[228]).isEmpty()) {
      manager.registerObjectType(
          ids[228], IIeeeAutoNegotiationStatusTypeNode.class, CONSTRUCTOR_228);
    }
    if (manager.getNodeConstructor(ids[229]).isEmpty()) {
      manager.registerObjectType(ids[229], UserManagementTypeNode.class, CONSTRUCTOR_229);
    }
    if (manager.getNodeConstructor(ids[230]).isEmpty()) {
      manager.registerObjectType(ids[230], IVlanIdTypeNode.class, CONSTRUCTOR_230);
    }
    if (manager.getNodeConstructor(ids[231]).isEmpty()) {
      manager.registerObjectType(ids[231], IetfBaseNetworkInterfaceTypeNode.class, CONSTRUCTOR_231);
    }
    if (manager.getNodeConstructor(ids[232]).isEmpty()) {
      manager.registerObjectType(ids[232], PriorityMappingTableTypeNode.class, CONSTRUCTOR_232);
    }
    if (manager.getNodeConstructor(ids[233]).isEmpty()) {
      manager.registerObjectType(ids[233], PubSubKeyPushTargetTypeNode.class, CONSTRUCTOR_233);
    }
    if (manager.getNodeConstructor(ids[234]).isEmpty()) {
      manager.registerObjectType(
          ids[234], PubSubKeyPushTargetFolderTypeNode.class, CONSTRUCTOR_234);
    }
    if (manager.getNodeConstructor(ids[235]).isEmpty()) {
      manager.registerObjectType(ids[235], PubSubConfigurationTypeNode.class, CONSTRUCTOR_235);
    }
    if (manager.getNodeConstructor(ids[236]).isEmpty()) {
      manager.registerObjectType(ids[236], ApplicationConfigurationTypeNode.class, CONSTRUCTOR_236);
    }
    if (manager.getNodeConstructor(ids[237]).isEmpty()) {
      manager.registerObjectType(ids[237], ProvisionableDeviceTypeNode.class, CONSTRUCTOR_237);
    }
    if (manager.getNodeConstructor(ids[238]).isEmpty()) {
      manager.registerObjectType(ids[238], SemanticChangeEventTypeNode.class, CONSTRUCTOR_238);
    }
    if (manager.getNodeConstructor(ids[239]).isEmpty()) {
      manager.registerObjectType(ids[239], AuditUrlMismatchEventTypeNode.class, CONSTRUCTOR_239);
    }
    if (manager.getNodeConstructor(ids[240]).isEmpty()) {
      manager.registerObjectType(ids[240], RefreshStartEventTypeNode.class, CONSTRUCTOR_240);
    }
    if (manager.getNodeConstructor(ids[241]).isEmpty()) {
      manager.registerObjectType(ids[241], RefreshEndEventTypeNode.class, CONSTRUCTOR_241);
    }
    if (manager.getNodeConstructor(ids[242]).isEmpty()) {
      manager.registerObjectType(ids[242], RefreshRequiredEventTypeNode.class, CONSTRUCTOR_242);
    }
    if (manager.getNodeConstructor(ids[243]).isEmpty()) {
      manager.registerObjectType(
          ids[243], AuditConditionEnableEventTypeNode.class, CONSTRUCTOR_243);
    }
    if (manager.getNodeConstructor(ids[244]).isEmpty()) {
      manager.registerObjectType(
          ids[244], AuditConditionCommentEventTypeNode.class, CONSTRUCTOR_244);
    }
    if (manager.getNodeConstructor(ids[245]).isEmpty()) {
      manager.registerObjectType(ids[245], DialogConditionTypeNode.class, CONSTRUCTOR_245);
    }
    if (manager.getNodeConstructor(ids[246]).isEmpty()) {
      manager.registerObjectType(ids[246], ShelvedStateMachineTypeNode.class, CONSTRUCTOR_246);
    }
    if (manager.getNodeConstructor(ids[247]).isEmpty()) {
      manager.registerObjectType(
          ids[247], AuditHistoryEventUpdateEventTypeNode.class, CONSTRUCTOR_247);
    }
    if (manager.getNodeConstructor(ids[248]).isEmpty()) {
      manager.registerObjectType(
          ids[248], AuditHistoryValueUpdateEventTypeNode.class, CONSTRUCTOR_248);
    }
    if (manager.getNodeConstructor(ids[249]).isEmpty()) {
      manager.registerObjectType(ids[249], AuditHistoryDeleteEventTypeNode.class, CONSTRUCTOR_249);
    }
    if (manager.getNodeConstructor(ids[250]).isEmpty()) {
      manager.registerObjectType(
          ids[250], AuditHistoryRawModifyDeleteEventTypeNode.class, CONSTRUCTOR_250);
    }
    if (manager.getNodeConstructor(ids[251]).isEmpty()) {
      manager.registerObjectType(
          ids[251], AuditHistoryAtTimeDeleteEventTypeNode.class, CONSTRUCTOR_251);
    }
    if (manager.getNodeConstructor(ids[252]).isEmpty()) {
      manager.registerObjectType(
          ids[252], AuditHistoryEventDeleteEventTypeNode.class, CONSTRUCTOR_252);
    }
    if (manager.getNodeConstructor(ids[253]).isEmpty()) {
      manager.registerObjectType(ids[253], EventQueueOverflowEventTypeNode.class, CONSTRUCTOR_253);
    }
    if (manager.getNodeConstructor(ids[254]).isEmpty()) {
      manager.registerObjectType(ids[254], AlarmSuppressionGroupTypeNode.class, CONSTRUCTOR_254);
    }
    if (manager.getNodeConstructor(ids[255]).isEmpty()) {
      manager.registerObjectType(
          ids[255], TrustListUpdateRequestedAuditEventTypeNode.class, CONSTRUCTOR_255);
    }
  }

  private static void check2(NamespaceTable table, ObjectTypeManager manager, NodeId[] ids) {
    ids[256] = ClientNodeSupport.resolve(table, TransactionDiagnosticsType.TYPE_ID);
    if (manager.getNodeConstructor(ids[256]).filter(c -> c != CONSTRUCTOR_256).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + TransactionDiagnosticsType.TYPE_ID);
    }
    ids[257] = ClientNodeSupport.resolve(table, CertificateUpdateRequestedAuditEventType.TYPE_ID);
    if (manager.getNodeConstructor(ids[257]).filter(c -> c != CONSTRUCTOR_257).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + CertificateUpdateRequestedAuditEventType.TYPE_ID);
    }
    ids[258] = ClientNodeSupport.resolve(table, NonTransparentBackupRedundancyType.TYPE_ID);
    if (manager.getNodeConstructor(ids[258]).filter(c -> c != CONSTRUCTOR_258).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + NonTransparentBackupRedundancyType.TYPE_ID);
    }
    ids[259] = ClientNodeSupport.resolve(table, SyntaxReferenceEntryType.TYPE_ID);
    if (manager.getNodeConstructor(ids[259]).filter(c -> c != CONSTRUCTOR_259).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + SyntaxReferenceEntryType.TYPE_ID);
    }
    ids[260] = ClientNodeSupport.resolve(table, UnitType.TYPE_ID);
    if (manager.getNodeConstructor(ids[260]).filter(c -> c != CONSTRUCTOR_260).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + UnitType.TYPE_ID);
    }
    ids[261] = ClientNodeSupport.resolve(table, ServerUnitType.TYPE_ID);
    if (manager.getNodeConstructor(ids[261]).filter(c -> c != CONSTRUCTOR_261).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + ServerUnitType.TYPE_ID);
    }
    ids[262] = ClientNodeSupport.resolve(table, AlternativeUnitType.TYPE_ID);
    if (manager.getNodeConstructor(ids[262]).filter(c -> c != CONSTRUCTOR_262).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + AlternativeUnitType.TYPE_ID);
    }
    ids[263] = ClientNodeSupport.resolve(table, QuantityType.TYPE_ID);
    if (manager.getNodeConstructor(ids[263]).filter(c -> c != CONSTRUCTOR_263).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + QuantityType.TYPE_ID);
    }
    ids[264] = ClientNodeSupport.resolve(table, HistoricalEventConfigurationType.TYPE_ID);
    if (manager.getNodeConstructor(ids[264]).filter(c -> c != CONSTRUCTOR_264).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + HistoricalEventConfigurationType.TYPE_ID);
    }
    ids[265] = ClientNodeSupport.resolve(table, HistoricalExternalEventSourceType.TYPE_ID);
    if (manager.getNodeConstructor(ids[265]).filter(c -> c != CONSTRUCTOR_265).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + HistoricalExternalEventSourceType.TYPE_ID);
    }
    ids[266] = ClientNodeSupport.resolve(table, AuditHistoryConfigurationChangeEventType.TYPE_ID);
    if (manager.getNodeConstructor(ids[266]).filter(c -> c != CONSTRUCTOR_266).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + AuditHistoryConfigurationChangeEventType.TYPE_ID);
    }
    ids[267] = ClientNodeSupport.resolve(table, AuditHistoryBulkInsertEventType.TYPE_ID);
    if (manager.getNodeConstructor(ids[267]).filter(c -> c != CONSTRUCTOR_267).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + AuditHistoryBulkInsertEventType.TYPE_ID);
    }
    ids[268] = ClientNodeSupport.resolve(table, ProgramTransitionAuditEventType.TYPE_ID);
    if (manager.getNodeConstructor(ids[268]).filter(c -> c != CONSTRUCTOR_268).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + ProgramTransitionAuditEventType.TYPE_ID);
    }
    ids[269] = ClientNodeSupport.resolve(table, DataTypeSystemType.TYPE_ID);
    if (manager.getNodeConstructor(ids[269]).filter(c -> c != CONSTRUCTOR_269).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + DataTypeSystemType.TYPE_ID);
    }
    ids[270] = ClientNodeSupport.resolve(table, DataTypeEncodingType.TYPE_ID);
    if (manager.getNodeConstructor(ids[270]).filter(c -> c != CONSTRUCTOR_270).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + DataTypeEncodingType.TYPE_ID);
    }
    ids[271] = ClientNodeSupport.resolve(table, ModellingRuleType.TYPE_ID);
    if (manager.getNodeConstructor(ids[271]).filter(c -> c != CONSTRUCTOR_271).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + ModellingRuleType.TYPE_ID);
    }
    ids[272] = ClientNodeSupport.resolve(table, AuditConditionRespondEventType.TYPE_ID);
    if (manager.getNodeConstructor(ids[272]).filter(c -> c != CONSTRUCTOR_272).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + AuditConditionRespondEventType.TYPE_ID);
    }
    ids[273] = ClientNodeSupport.resolve(table, AuditConditionAcknowledgeEventType.TYPE_ID);
    if (manager.getNodeConstructor(ids[273]).filter(c -> c != CONSTRUCTOR_273).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + AuditConditionAcknowledgeEventType.TYPE_ID);
    }
    ids[274] = ClientNodeSupport.resolve(table, AuditConditionConfirmEventType.TYPE_ID);
    if (manager.getNodeConstructor(ids[274]).filter(c -> c != CONSTRUCTOR_274).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + AuditConditionConfirmEventType.TYPE_ID);
    }
    ids[275] = ClientNodeSupport.resolve(table, ExclusiveLimitStateMachineType.TYPE_ID);
    if (manager.getNodeConstructor(ids[275]).filter(c -> c != CONSTRUCTOR_275).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + ExclusiveLimitStateMachineType.TYPE_ID);
    }
    ids[276] = ClientNodeSupport.resolve(table, ExclusiveLimitAlarmType.TYPE_ID);
    if (manager.getNodeConstructor(ids[276]).filter(c -> c != CONSTRUCTOR_276).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + ExclusiveLimitAlarmType.TYPE_ID);
    }
    ids[277] = ClientNodeSupport.resolve(table, ExclusiveLevelAlarmType.TYPE_ID);
    if (manager.getNodeConstructor(ids[277]).filter(c -> c != CONSTRUCTOR_277).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + ExclusiveLevelAlarmType.TYPE_ID);
    }
    ids[278] = ClientNodeSupport.resolve(table, ExclusiveRateOfChangeAlarmType.TYPE_ID);
    if (manager.getNodeConstructor(ids[278]).filter(c -> c != CONSTRUCTOR_278).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + ExclusiveRateOfChangeAlarmType.TYPE_ID);
    }
    ids[279] = ClientNodeSupport.resolve(table, ExclusiveDeviationAlarmType.TYPE_ID);
    if (manager.getNodeConstructor(ids[279]).filter(c -> c != CONSTRUCTOR_279).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + ExclusiveDeviationAlarmType.TYPE_ID);
    }
  }

  private static void install2(ObjectTypeManager manager, NodeId[] ids) {
    if (manager.getNodeConstructor(ids[256]).isEmpty()) {
      manager.registerObjectType(ids[256], TransactionDiagnosticsTypeNode.class, CONSTRUCTOR_256);
    }
    if (manager.getNodeConstructor(ids[257]).isEmpty()) {
      manager.registerObjectType(
          ids[257], CertificateUpdateRequestedAuditEventTypeNode.class, CONSTRUCTOR_257);
    }
    if (manager.getNodeConstructor(ids[258]).isEmpty()) {
      manager.registerObjectType(
          ids[258], NonTransparentBackupRedundancyTypeNode.class, CONSTRUCTOR_258);
    }
    if (manager.getNodeConstructor(ids[259]).isEmpty()) {
      manager.registerObjectType(ids[259], SyntaxReferenceEntryTypeNode.class, CONSTRUCTOR_259);
    }
    if (manager.getNodeConstructor(ids[260]).isEmpty()) {
      manager.registerObjectType(ids[260], UnitTypeNode.class, CONSTRUCTOR_260);
    }
    if (manager.getNodeConstructor(ids[261]).isEmpty()) {
      manager.registerObjectType(ids[261], ServerUnitTypeNode.class, CONSTRUCTOR_261);
    }
    if (manager.getNodeConstructor(ids[262]).isEmpty()) {
      manager.registerObjectType(ids[262], AlternativeUnitTypeNode.class, CONSTRUCTOR_262);
    }
    if (manager.getNodeConstructor(ids[263]).isEmpty()) {
      manager.registerObjectType(ids[263], QuantityTypeNode.class, CONSTRUCTOR_263);
    }
    if (manager.getNodeConstructor(ids[264]).isEmpty()) {
      manager.registerObjectType(
          ids[264], HistoricalEventConfigurationTypeNode.class, CONSTRUCTOR_264);
    }
    if (manager.getNodeConstructor(ids[265]).isEmpty()) {
      manager.registerObjectType(
          ids[265], HistoricalExternalEventSourceTypeNode.class, CONSTRUCTOR_265);
    }
    if (manager.getNodeConstructor(ids[266]).isEmpty()) {
      manager.registerObjectType(
          ids[266], AuditHistoryConfigurationChangeEventTypeNode.class, CONSTRUCTOR_266);
    }
    if (manager.getNodeConstructor(ids[267]).isEmpty()) {
      manager.registerObjectType(
          ids[267], AuditHistoryBulkInsertEventTypeNode.class, CONSTRUCTOR_267);
    }
    if (manager.getNodeConstructor(ids[268]).isEmpty()) {
      manager.registerObjectType(
          ids[268], ProgramTransitionAuditEventTypeNode.class, CONSTRUCTOR_268);
    }
    if (manager.getNodeConstructor(ids[269]).isEmpty()) {
      manager.registerObjectType(ids[269], DataTypeSystemTypeNode.class, CONSTRUCTOR_269);
    }
    if (manager.getNodeConstructor(ids[270]).isEmpty()) {
      manager.registerObjectType(ids[270], DataTypeEncodingTypeNode.class, CONSTRUCTOR_270);
    }
    if (manager.getNodeConstructor(ids[271]).isEmpty()) {
      manager.registerObjectType(ids[271], ModellingRuleTypeNode.class, CONSTRUCTOR_271);
    }
    if (manager.getNodeConstructor(ids[272]).isEmpty()) {
      manager.registerObjectType(
          ids[272], AuditConditionRespondEventTypeNode.class, CONSTRUCTOR_272);
    }
    if (manager.getNodeConstructor(ids[273]).isEmpty()) {
      manager.registerObjectType(
          ids[273], AuditConditionAcknowledgeEventTypeNode.class, CONSTRUCTOR_273);
    }
    if (manager.getNodeConstructor(ids[274]).isEmpty()) {
      manager.registerObjectType(
          ids[274], AuditConditionConfirmEventTypeNode.class, CONSTRUCTOR_274);
    }
    if (manager.getNodeConstructor(ids[275]).isEmpty()) {
      manager.registerObjectType(
          ids[275], ExclusiveLimitStateMachineTypeNode.class, CONSTRUCTOR_275);
    }
    if (manager.getNodeConstructor(ids[276]).isEmpty()) {
      manager.registerObjectType(ids[276], ExclusiveLimitAlarmTypeNode.class, CONSTRUCTOR_276);
    }
    if (manager.getNodeConstructor(ids[277]).isEmpty()) {
      manager.registerObjectType(ids[277], ExclusiveLevelAlarmTypeNode.class, CONSTRUCTOR_277);
    }
    if (manager.getNodeConstructor(ids[278]).isEmpty()) {
      manager.registerObjectType(
          ids[278], ExclusiveRateOfChangeAlarmTypeNode.class, CONSTRUCTOR_278);
    }
    if (manager.getNodeConstructor(ids[279]).isEmpty()) {
      manager.registerObjectType(ids[279], ExclusiveDeviationAlarmTypeNode.class, CONSTRUCTOR_279);
    }
  }

  /**
   * Checks all namespaces and conflicts before mutation. Repeat calls retain identical
   * constructors.
   *
   * @param namespaceTable the connected client's namespace table.
   * @param manager the client's constructor manager.
   * @throws IllegalArgumentException if a namespace is missing.
   * @throws IllegalStateException if a different constructor is already registered.
   */
  public static void initialize(NamespaceTable namespaceTable, ObjectTypeManager manager) {
    NodeId[] ids = new NodeId[280];
    check0(namespaceTable, manager, ids);
    check1(namespaceTable, manager, ids);
    check2(namespaceTable, manager, ids);
    install0(manager, ids);
    install1(manager, ids);
    install2(manager, ids);
  }
}

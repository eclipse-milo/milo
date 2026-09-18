package org.eclipse.milo.opcua.sdk.server.model;

import org.eclipse.milo.opcua.sdk.server.ObjectTypeManager;
import org.eclipse.milo.opcua.sdk.server.model.objects.AcknowledgeableConditionType;
import org.eclipse.milo.opcua.sdk.server.model.objects.AcknowledgeableConditionTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.AddressSpaceFileType;
import org.eclipse.milo.opcua.sdk.server.model.objects.AddressSpaceFileTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.AggregateConfigurationType;
import org.eclipse.milo.opcua.sdk.server.model.objects.AggregateConfigurationTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.AggregateFunctionType;
import org.eclipse.milo.opcua.sdk.server.model.objects.AggregateFunctionTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.AlarmConditionType;
import org.eclipse.milo.opcua.sdk.server.model.objects.AlarmConditionTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.AlarmGroupType;
import org.eclipse.milo.opcua.sdk.server.model.objects.AlarmGroupTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.AlarmMetricsType;
import org.eclipse.milo.opcua.sdk.server.model.objects.AlarmMetricsTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.AlarmSuppressionGroupType;
import org.eclipse.milo.opcua.sdk.server.model.objects.AlarmSuppressionGroupTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.AliasNameCategoryType;
import org.eclipse.milo.opcua.sdk.server.model.objects.AliasNameCategoryTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.AliasNameType;
import org.eclipse.milo.opcua.sdk.server.model.objects.AliasNameTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.AlternativeUnitType;
import org.eclipse.milo.opcua.sdk.server.model.objects.AlternativeUnitTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.ApplicationCertificateType;
import org.eclipse.milo.opcua.sdk.server.model.objects.ApplicationCertificateTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.ApplicationConfigurationFileType;
import org.eclipse.milo.opcua.sdk.server.model.objects.ApplicationConfigurationFileTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.ApplicationConfigurationFolderType;
import org.eclipse.milo.opcua.sdk.server.model.objects.ApplicationConfigurationFolderTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.ApplicationConfigurationType;
import org.eclipse.milo.opcua.sdk.server.model.objects.ApplicationConfigurationTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditActivateSessionEventType;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditActivateSessionEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditAddNodesEventType;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditAddNodesEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditAddReferencesEventType;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditAddReferencesEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditCancelEventType;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditCancelEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditCertificateDataMismatchEventType;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditCertificateDataMismatchEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditCertificateEventType;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditCertificateEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditCertificateExpiredEventType;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditCertificateExpiredEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditCertificateInvalidEventType;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditCertificateInvalidEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditCertificateMismatchEventType;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditCertificateMismatchEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditCertificateRevokedEventType;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditCertificateRevokedEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditCertificateUntrustedEventType;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditCertificateUntrustedEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditChannelEventType;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditChannelEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditClientEventType;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditClientEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditClientUpdateMethodResultEventType;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditClientUpdateMethodResultEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditConditionAcknowledgeEventType;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditConditionAcknowledgeEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditConditionCommentEventType;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditConditionCommentEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditConditionConfirmEventType;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditConditionConfirmEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditConditionEnableEventType;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditConditionEnableEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditConditionEventType;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditConditionEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditConditionOutOfServiceEventType;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditConditionOutOfServiceEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditConditionResetEventType;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditConditionResetEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditConditionRespondEventType;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditConditionRespondEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditConditionShelvingEventType;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditConditionShelvingEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditConditionSilenceEventType;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditConditionSilenceEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditConditionSuppressionEventType;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditConditionSuppressionEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditCreateSessionEventType;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditCreateSessionEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditDeleteNodesEventType;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditDeleteNodesEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditDeleteReferencesEventType;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditDeleteReferencesEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditEventType;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditHistoryAnnotationUpdateEventType;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditHistoryAnnotationUpdateEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditHistoryAtTimeDeleteEventType;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditHistoryAtTimeDeleteEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditHistoryBulkInsertEventType;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditHistoryBulkInsertEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditHistoryConfigurationChangeEventType;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditHistoryConfigurationChangeEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditHistoryDeleteEventType;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditHistoryDeleteEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditHistoryEventDeleteEventType;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditHistoryEventDeleteEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditHistoryEventUpdateEventType;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditHistoryEventUpdateEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditHistoryRawModifyDeleteEventType;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditHistoryRawModifyDeleteEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditHistoryUpdateEventType;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditHistoryUpdateEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditHistoryValueUpdateEventType;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditHistoryValueUpdateEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditNodeManagementEventType;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditNodeManagementEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditOpenSecureChannelEventType;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditOpenSecureChannelEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditProgramTransitionEventType;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditProgramTransitionEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditSecurityEventType;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditSecurityEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditSessionEventType;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditSessionEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditUpdateEventType;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditUpdateEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditUpdateMethodEventType;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditUpdateMethodEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditUpdateStateEventType;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditUpdateStateEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditUrlMismatchEventType;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditUrlMismatchEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditWriteUpdateEventType;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuditWriteUpdateEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuthorizationServiceConfigurationType;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuthorizationServiceConfigurationTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuthorizationServicesConfigurationFolderType;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuthorizationServicesConfigurationFolderTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.BaseConditionClassType;
import org.eclipse.milo.opcua.sdk.server.model.objects.BaseConditionClassTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.BaseEventType;
import org.eclipse.milo.opcua.sdk.server.model.objects.BaseEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.BaseInterfaceType;
import org.eclipse.milo.opcua.sdk.server.model.objects.BaseInterfaceTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.BaseLogEventType;
import org.eclipse.milo.opcua.sdk.server.model.objects.BaseLogEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.BaseModelChangeEventType;
import org.eclipse.milo.opcua.sdk.server.model.objects.BaseModelChangeEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.BaseObjectType;
import org.eclipse.milo.opcua.sdk.server.model.objects.BaseObjectTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.BrokerConnectionTransportType;
import org.eclipse.milo.opcua.sdk.server.model.objects.BrokerConnectionTransportTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.BrokerDataSetReaderTransportType;
import org.eclipse.milo.opcua.sdk.server.model.objects.BrokerDataSetReaderTransportTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.BrokerDataSetWriterTransportType;
import org.eclipse.milo.opcua.sdk.server.model.objects.BrokerDataSetWriterTransportTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.BrokerWriterGroupTransportType;
import org.eclipse.milo.opcua.sdk.server.model.objects.BrokerWriterGroupTransportTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.CertificateExpirationAlarmType;
import org.eclipse.milo.opcua.sdk.server.model.objects.CertificateExpirationAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.CertificateGroupFolderType;
import org.eclipse.milo.opcua.sdk.server.model.objects.CertificateGroupFolderTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.CertificateGroupType;
import org.eclipse.milo.opcua.sdk.server.model.objects.CertificateGroupTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.CertificateType;
import org.eclipse.milo.opcua.sdk.server.model.objects.CertificateTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.CertificateUpdateRequestedAuditEventType;
import org.eclipse.milo.opcua.sdk.server.model.objects.CertificateUpdateRequestedAuditEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.CertificateUpdatedAuditEventType;
import org.eclipse.milo.opcua.sdk.server.model.objects.CertificateUpdatedAuditEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.ChoiceStateType;
import org.eclipse.milo.opcua.sdk.server.model.objects.ChoiceStateTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.ConditionType;
import org.eclipse.milo.opcua.sdk.server.model.objects.ConditionTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.ConfigurationFileType;
import org.eclipse.milo.opcua.sdk.server.model.objects.ConfigurationFileTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.ConfigurationUpdatedAuditEventType;
import org.eclipse.milo.opcua.sdk.server.model.objects.ConfigurationUpdatedAuditEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.ConnectionTransportType;
import org.eclipse.milo.opcua.sdk.server.model.objects.ConnectionTransportTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.DataSetFolderType;
import org.eclipse.milo.opcua.sdk.server.model.objects.DataSetFolderTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.DataSetReaderMessageType;
import org.eclipse.milo.opcua.sdk.server.model.objects.DataSetReaderMessageTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.DataSetReaderTransportType;
import org.eclipse.milo.opcua.sdk.server.model.objects.DataSetReaderTransportTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.DataSetReaderType;
import org.eclipse.milo.opcua.sdk.server.model.objects.DataSetReaderTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.DataSetWriterMessageType;
import org.eclipse.milo.opcua.sdk.server.model.objects.DataSetWriterMessageTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.DataSetWriterTransportType;
import org.eclipse.milo.opcua.sdk.server.model.objects.DataSetWriterTransportTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.DataSetWriterType;
import org.eclipse.milo.opcua.sdk.server.model.objects.DataSetWriterTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.DataTypeEncodingType;
import org.eclipse.milo.opcua.sdk.server.model.objects.DataTypeEncodingTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.DataTypeRefinementType;
import org.eclipse.milo.opcua.sdk.server.model.objects.DataTypeRefinementTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.DataTypeSystemType;
import org.eclipse.milo.opcua.sdk.server.model.objects.DataTypeSystemTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.DatagramConnectionTransportType;
import org.eclipse.milo.opcua.sdk.server.model.objects.DatagramConnectionTransportTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.DatagramDataSetReaderTransportType;
import org.eclipse.milo.opcua.sdk.server.model.objects.DatagramDataSetReaderTransportTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.DatagramWriterGroupTransportType;
import org.eclipse.milo.opcua.sdk.server.model.objects.DatagramWriterGroupTransportTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.DeviceFailureEventType;
import org.eclipse.milo.opcua.sdk.server.model.objects.DeviceFailureEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.DialogConditionType;
import org.eclipse.milo.opcua.sdk.server.model.objects.DialogConditionTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.DictionaryEntryType;
import org.eclipse.milo.opcua.sdk.server.model.objects.DictionaryEntryTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.DictionaryFolderType;
import org.eclipse.milo.opcua.sdk.server.model.objects.DictionaryFolderTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.DiscrepancyAlarmType;
import org.eclipse.milo.opcua.sdk.server.model.objects.DiscrepancyAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.DiscreteAlarmType;
import org.eclipse.milo.opcua.sdk.server.model.objects.DiscreteAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.EccApplicationCertificateType;
import org.eclipse.milo.opcua.sdk.server.model.objects.EccApplicationCertificateTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.EccBrainpoolP256r1ApplicationCertificateType;
import org.eclipse.milo.opcua.sdk.server.model.objects.EccBrainpoolP256r1ApplicationCertificateTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.EccBrainpoolP384r1ApplicationCertificateType;
import org.eclipse.milo.opcua.sdk.server.model.objects.EccBrainpoolP384r1ApplicationCertificateTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.EccCurve25519ApplicationCertificateType;
import org.eclipse.milo.opcua.sdk.server.model.objects.EccCurve25519ApplicationCertificateTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.EccCurve448ApplicationCertificateType;
import org.eclipse.milo.opcua.sdk.server.model.objects.EccCurve448ApplicationCertificateTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.EccNistP256ApplicationCertificateType;
import org.eclipse.milo.opcua.sdk.server.model.objects.EccNistP256ApplicationCertificateTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.EccNistP384ApplicationCertificateType;
import org.eclipse.milo.opcua.sdk.server.model.objects.EccNistP384ApplicationCertificateTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.EventQueueOverflowEventType;
import org.eclipse.milo.opcua.sdk.server.model.objects.EventQueueOverflowEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.ExclusiveDeviationAlarmType;
import org.eclipse.milo.opcua.sdk.server.model.objects.ExclusiveDeviationAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.ExclusiveLevelAlarmType;
import org.eclipse.milo.opcua.sdk.server.model.objects.ExclusiveLevelAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.ExclusiveLimitAlarmType;
import org.eclipse.milo.opcua.sdk.server.model.objects.ExclusiveLimitAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.ExclusiveLimitStateMachineType;
import org.eclipse.milo.opcua.sdk.server.model.objects.ExclusiveLimitStateMachineTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.ExclusiveRateOfChangeAlarmType;
import org.eclipse.milo.opcua.sdk.server.model.objects.ExclusiveRateOfChangeAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.ExtensionFieldsType;
import org.eclipse.milo.opcua.sdk.server.model.objects.ExtensionFieldsTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.FileDirectoryType;
import org.eclipse.milo.opcua.sdk.server.model.objects.FileDirectoryTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.FileTransferStateMachineType;
import org.eclipse.milo.opcua.sdk.server.model.objects.FileTransferStateMachineTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.FileType;
import org.eclipse.milo.opcua.sdk.server.model.objects.FileTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.FiniteStateMachineType;
import org.eclipse.milo.opcua.sdk.server.model.objects.FiniteStateMachineTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.FolderType;
import org.eclipse.milo.opcua.sdk.server.model.objects.FolderTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.GeneralModelChangeEventType;
import org.eclipse.milo.opcua.sdk.server.model.objects.GeneralModelChangeEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.HighlyManagedAlarmConditionClassType;
import org.eclipse.milo.opcua.sdk.server.model.objects.HighlyManagedAlarmConditionClassTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.HistoricalDataConfigurationType;
import org.eclipse.milo.opcua.sdk.server.model.objects.HistoricalDataConfigurationTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.HistoricalEventConfigurationType;
import org.eclipse.milo.opcua.sdk.server.model.objects.HistoricalEventConfigurationTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.HistoricalExternalEventSourceType;
import org.eclipse.milo.opcua.sdk.server.model.objects.HistoricalExternalEventSourceTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.HistoryServerCapabilitiesType;
import org.eclipse.milo.opcua.sdk.server.model.objects.HistoryServerCapabilitiesTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.HttpsCertificateType;
import org.eclipse.milo.opcua.sdk.server.model.objects.HttpsCertificateTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.IBaseEthernetCapabilitiesType;
import org.eclipse.milo.opcua.sdk.server.model.objects.IBaseEthernetCapabilitiesTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.IIeeeAutoNegotiationStatusType;
import org.eclipse.milo.opcua.sdk.server.model.objects.IIeeeAutoNegotiationStatusTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.IIeeeBaseEthernetPortType;
import org.eclipse.milo.opcua.sdk.server.model.objects.IIeeeBaseEthernetPortTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.IIeeeBaseTsnStatusStreamType;
import org.eclipse.milo.opcua.sdk.server.model.objects.IIeeeBaseTsnStatusStreamTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.IIeeeBaseTsnStreamType;
import org.eclipse.milo.opcua.sdk.server.model.objects.IIeeeBaseTsnStreamTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.IIeeeBaseTsnTrafficSpecificationType;
import org.eclipse.milo.opcua.sdk.server.model.objects.IIeeeBaseTsnTrafficSpecificationTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.IIeeeTsnInterfaceConfigurationListenerType;
import org.eclipse.milo.opcua.sdk.server.model.objects.IIeeeTsnInterfaceConfigurationListenerTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.IIeeeTsnInterfaceConfigurationTalkerType;
import org.eclipse.milo.opcua.sdk.server.model.objects.IIeeeTsnInterfaceConfigurationTalkerTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.IIeeeTsnInterfaceConfigurationType;
import org.eclipse.milo.opcua.sdk.server.model.objects.IIeeeTsnInterfaceConfigurationTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.IIeeeTsnMacAddressType;
import org.eclipse.milo.opcua.sdk.server.model.objects.IIeeeTsnMacAddressTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.IIeeeTsnVlanTagType;
import org.eclipse.milo.opcua.sdk.server.model.objects.IIeeeTsnVlanTagTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.IIetfBaseNetworkInterfaceType;
import org.eclipse.milo.opcua.sdk.server.model.objects.IIetfBaseNetworkInterfaceTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.IOrderedObjectType;
import org.eclipse.milo.opcua.sdk.server.model.objects.IOrderedObjectTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.IPriorityMappingEntryType;
import org.eclipse.milo.opcua.sdk.server.model.objects.IPriorityMappingEntryTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.ISrClassType;
import org.eclipse.milo.opcua.sdk.server.model.objects.ISrClassTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.IVlanIdType;
import org.eclipse.milo.opcua.sdk.server.model.objects.IVlanIdTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.IetfBaseNetworkInterfaceType;
import org.eclipse.milo.opcua.sdk.server.model.objects.IetfBaseNetworkInterfaceTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.InitialStateType;
import org.eclipse.milo.opcua.sdk.server.model.objects.InitialStateTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.InstrumentDiagnosticAlarmType;
import org.eclipse.milo.opcua.sdk.server.model.objects.InstrumentDiagnosticAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.IrdiDictionaryEntryType;
import org.eclipse.milo.opcua.sdk.server.model.objects.IrdiDictionaryEntryTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.JsonDataSetReaderMessageType;
import org.eclipse.milo.opcua.sdk.server.model.objects.JsonDataSetReaderMessageTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.JsonDataSetWriterMessageType;
import org.eclipse.milo.opcua.sdk.server.model.objects.JsonDataSetWriterMessageTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.JsonWriterGroupMessageType;
import org.eclipse.milo.opcua.sdk.server.model.objects.JsonWriterGroupMessageTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.KeyCredentialAuditEventType;
import org.eclipse.milo.opcua.sdk.server.model.objects.KeyCredentialAuditEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.KeyCredentialConfigurationFolderType;
import org.eclipse.milo.opcua.sdk.server.model.objects.KeyCredentialConfigurationFolderTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.KeyCredentialConfigurationType;
import org.eclipse.milo.opcua.sdk.server.model.objects.KeyCredentialConfigurationTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.KeyCredentialDeletedAuditEventType;
import org.eclipse.milo.opcua.sdk.server.model.objects.KeyCredentialDeletedAuditEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.KeyCredentialUpdatedAuditEventType;
import org.eclipse.milo.opcua.sdk.server.model.objects.KeyCredentialUpdatedAuditEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.LimitAlarmType;
import org.eclipse.milo.opcua.sdk.server.model.objects.LimitAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.LldpInformationType;
import org.eclipse.milo.opcua.sdk.server.model.objects.LldpInformationTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.LldpLocalSystemType;
import org.eclipse.milo.opcua.sdk.server.model.objects.LldpLocalSystemTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.LldpPortInformationType;
import org.eclipse.milo.opcua.sdk.server.model.objects.LldpPortInformationTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.LldpRemoteStatisticsType;
import org.eclipse.milo.opcua.sdk.server.model.objects.LldpRemoteStatisticsTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.LldpRemoteSystemType;
import org.eclipse.milo.opcua.sdk.server.model.objects.LldpRemoteSystemTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.LogEntryConditionClassType;
import org.eclipse.milo.opcua.sdk.server.model.objects.LogEntryConditionClassTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.LogObjectType;
import org.eclipse.milo.opcua.sdk.server.model.objects.LogObjectTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.LogOverflowEventType;
import org.eclipse.milo.opcua.sdk.server.model.objects.LogOverflowEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.MaintenanceConditionClassType;
import org.eclipse.milo.opcua.sdk.server.model.objects.MaintenanceConditionClassTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.ModellingRuleType;
import org.eclipse.milo.opcua.sdk.server.model.objects.ModellingRuleTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.NamespaceMetadataType;
import org.eclipse.milo.opcua.sdk.server.model.objects.NamespaceMetadataTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.NamespacesType;
import org.eclipse.milo.opcua.sdk.server.model.objects.NamespacesTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.NetworkAddressType;
import org.eclipse.milo.opcua.sdk.server.model.objects.NetworkAddressTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.NetworkAddressUrlType;
import org.eclipse.milo.opcua.sdk.server.model.objects.NetworkAddressUrlTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.NonExclusiveDeviationAlarmType;
import org.eclipse.milo.opcua.sdk.server.model.objects.NonExclusiveDeviationAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.NonExclusiveLevelAlarmType;
import org.eclipse.milo.opcua.sdk.server.model.objects.NonExclusiveLevelAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.NonExclusiveLimitAlarmType;
import org.eclipse.milo.opcua.sdk.server.model.objects.NonExclusiveLimitAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.NonExclusiveRateOfChangeAlarmType;
import org.eclipse.milo.opcua.sdk.server.model.objects.NonExclusiveRateOfChangeAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.NonTransparentBackupRedundancyType;
import org.eclipse.milo.opcua.sdk.server.model.objects.NonTransparentBackupRedundancyTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.NonTransparentNetworkRedundancyType;
import org.eclipse.milo.opcua.sdk.server.model.objects.NonTransparentNetworkRedundancyTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.NonTransparentRedundancyType;
import org.eclipse.milo.opcua.sdk.server.model.objects.NonTransparentRedundancyTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.OffNormalAlarmType;
import org.eclipse.milo.opcua.sdk.server.model.objects.OffNormalAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.OperationLimitsType;
import org.eclipse.milo.opcua.sdk.server.model.objects.OperationLimitsTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.OrderedListType;
import org.eclipse.milo.opcua.sdk.server.model.objects.OrderedListTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.PriorityMappingTableType;
import org.eclipse.milo.opcua.sdk.server.model.objects.PriorityMappingTableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.ProcessConditionClassType;
import org.eclipse.milo.opcua.sdk.server.model.objects.ProcessConditionClassTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.ProgramStateMachineType;
import org.eclipse.milo.opcua.sdk.server.model.objects.ProgramStateMachineTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.ProgramTransitionAuditEventType;
import org.eclipse.milo.opcua.sdk.server.model.objects.ProgramTransitionAuditEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.ProgramTransitionEventType;
import org.eclipse.milo.opcua.sdk.server.model.objects.ProgramTransitionEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.ProgressEventType;
import org.eclipse.milo.opcua.sdk.server.model.objects.ProgressEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.ProvisionableDeviceType;
import org.eclipse.milo.opcua.sdk.server.model.objects.ProvisionableDeviceTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.PubSubCapabilitiesType;
import org.eclipse.milo.opcua.sdk.server.model.objects.PubSubCapabilitiesTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.PubSubCommunicationFailureEventType;
import org.eclipse.milo.opcua.sdk.server.model.objects.PubSubCommunicationFailureEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.PubSubConfigurationType;
import org.eclipse.milo.opcua.sdk.server.model.objects.PubSubConfigurationTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.PubSubConnectionType;
import org.eclipse.milo.opcua.sdk.server.model.objects.PubSubConnectionTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.PubSubDiagnosticsConnectionType;
import org.eclipse.milo.opcua.sdk.server.model.objects.PubSubDiagnosticsConnectionTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.PubSubDiagnosticsDataSetReaderType;
import org.eclipse.milo.opcua.sdk.server.model.objects.PubSubDiagnosticsDataSetReaderTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.PubSubDiagnosticsDataSetWriterType;
import org.eclipse.milo.opcua.sdk.server.model.objects.PubSubDiagnosticsDataSetWriterTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.PubSubDiagnosticsReaderGroupType;
import org.eclipse.milo.opcua.sdk.server.model.objects.PubSubDiagnosticsReaderGroupTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.PubSubDiagnosticsRootType;
import org.eclipse.milo.opcua.sdk.server.model.objects.PubSubDiagnosticsRootTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.PubSubDiagnosticsType;
import org.eclipse.milo.opcua.sdk.server.model.objects.PubSubDiagnosticsTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.PubSubDiagnosticsWriterGroupType;
import org.eclipse.milo.opcua.sdk.server.model.objects.PubSubDiagnosticsWriterGroupTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.PubSubGroupType;
import org.eclipse.milo.opcua.sdk.server.model.objects.PubSubGroupTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.PubSubKeyPushTargetFolderType;
import org.eclipse.milo.opcua.sdk.server.model.objects.PubSubKeyPushTargetFolderTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.PubSubKeyPushTargetType;
import org.eclipse.milo.opcua.sdk.server.model.objects.PubSubKeyPushTargetTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.PubSubKeyServiceType;
import org.eclipse.milo.opcua.sdk.server.model.objects.PubSubKeyServiceTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.PubSubStatusEventType;
import org.eclipse.milo.opcua.sdk.server.model.objects.PubSubStatusEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.PubSubStatusType;
import org.eclipse.milo.opcua.sdk.server.model.objects.PubSubStatusTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.PubSubTransportLimitsExceedEventType;
import org.eclipse.milo.opcua.sdk.server.model.objects.PubSubTransportLimitsExceedEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.PublishSubscribeType;
import org.eclipse.milo.opcua.sdk.server.model.objects.PublishSubscribeTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.PublishedDataItemsType;
import org.eclipse.milo.opcua.sdk.server.model.objects.PublishedDataItemsTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.PublishedDataSetType;
import org.eclipse.milo.opcua.sdk.server.model.objects.PublishedDataSetTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.PublishedEventsType;
import org.eclipse.milo.opcua.sdk.server.model.objects.PublishedEventsTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.QuantityType;
import org.eclipse.milo.opcua.sdk.server.model.objects.QuantityTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.ReaderGroupMessageType;
import org.eclipse.milo.opcua.sdk.server.model.objects.ReaderGroupMessageTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.ReaderGroupTransportType;
import org.eclipse.milo.opcua.sdk.server.model.objects.ReaderGroupTransportTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.ReaderGroupType;
import org.eclipse.milo.opcua.sdk.server.model.objects.ReaderGroupTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.RefreshEndEventType;
import org.eclipse.milo.opcua.sdk.server.model.objects.RefreshEndEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.RefreshRequiredEventType;
import org.eclipse.milo.opcua.sdk.server.model.objects.RefreshRequiredEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.RefreshStartEventType;
import org.eclipse.milo.opcua.sdk.server.model.objects.RefreshStartEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.RoleMappingRuleChangedAuditEventType;
import org.eclipse.milo.opcua.sdk.server.model.objects.RoleMappingRuleChangedAuditEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.RoleSetType;
import org.eclipse.milo.opcua.sdk.server.model.objects.RoleSetTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.RoleType;
import org.eclipse.milo.opcua.sdk.server.model.objects.RoleTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.RsaMinApplicationCertificateType;
import org.eclipse.milo.opcua.sdk.server.model.objects.RsaMinApplicationCertificateTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.RsaSha256ApplicationCertificateType;
import org.eclipse.milo.opcua.sdk.server.model.objects.RsaSha256ApplicationCertificateTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.SafetyConditionClassType;
import org.eclipse.milo.opcua.sdk.server.model.objects.SafetyConditionClassTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.SecurityGroupFolderType;
import org.eclipse.milo.opcua.sdk.server.model.objects.SecurityGroupFolderTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.SecurityGroupType;
import org.eclipse.milo.opcua.sdk.server.model.objects.SecurityGroupTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.SemanticChangeEventType;
import org.eclipse.milo.opcua.sdk.server.model.objects.SemanticChangeEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.SerializationEntityType;
import org.eclipse.milo.opcua.sdk.server.model.objects.SerializationEntityTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.ServerCapabilitiesType;
import org.eclipse.milo.opcua.sdk.server.model.objects.ServerCapabilitiesTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.ServerConfigurationType;
import org.eclipse.milo.opcua.sdk.server.model.objects.ServerConfigurationTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.ServerDiagnosticsType;
import org.eclipse.milo.opcua.sdk.server.model.objects.ServerDiagnosticsTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.ServerRedundancyType;
import org.eclipse.milo.opcua.sdk.server.model.objects.ServerRedundancyTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.ServerType;
import org.eclipse.milo.opcua.sdk.server.model.objects.ServerTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.ServerUnitType;
import org.eclipse.milo.opcua.sdk.server.model.objects.ServerUnitTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.SessionDiagnosticsObjectType;
import org.eclipse.milo.opcua.sdk.server.model.objects.SessionDiagnosticsObjectTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.SessionsDiagnosticsSummaryType;
import org.eclipse.milo.opcua.sdk.server.model.objects.SessionsDiagnosticsSummaryTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.ShelvedStateMachineType;
import org.eclipse.milo.opcua.sdk.server.model.objects.ShelvedStateMachineTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.StandaloneSubscribedDataSetType;
import org.eclipse.milo.opcua.sdk.server.model.objects.StandaloneSubscribedDataSetTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.StateMachineType;
import org.eclipse.milo.opcua.sdk.server.model.objects.StateMachineTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.StateType;
import org.eclipse.milo.opcua.sdk.server.model.objects.StateTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.StatisticalConditionClassType;
import org.eclipse.milo.opcua.sdk.server.model.objects.StatisticalConditionClassTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.SubscribedDataSetFolderType;
import org.eclipse.milo.opcua.sdk.server.model.objects.SubscribedDataSetFolderTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.SubscribedDataSetMirrorType;
import org.eclipse.milo.opcua.sdk.server.model.objects.SubscribedDataSetMirrorTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.SubscribedDataSetType;
import org.eclipse.milo.opcua.sdk.server.model.objects.SubscribedDataSetTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.SubtypeRestrictionType;
import org.eclipse.milo.opcua.sdk.server.model.objects.SubtypeRestrictionTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.SyntaxReferenceEntryType;
import org.eclipse.milo.opcua.sdk.server.model.objects.SyntaxReferenceEntryTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.SystemConditionClassType;
import org.eclipse.milo.opcua.sdk.server.model.objects.SystemConditionClassTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.SystemDiagnosticAlarmType;
import org.eclipse.milo.opcua.sdk.server.model.objects.SystemDiagnosticAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.SystemEventType;
import org.eclipse.milo.opcua.sdk.server.model.objects.SystemEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.SystemOffNormalAlarmType;
import org.eclipse.milo.opcua.sdk.server.model.objects.SystemOffNormalAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.SystemStatusChangeEventType;
import org.eclipse.milo.opcua.sdk.server.model.objects.SystemStatusChangeEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.TargetVariablesType;
import org.eclipse.milo.opcua.sdk.server.model.objects.TargetVariablesTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.TemporaryFileTransferType;
import org.eclipse.milo.opcua.sdk.server.model.objects.TemporaryFileTransferTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.TestingConditionClassType;
import org.eclipse.milo.opcua.sdk.server.model.objects.TestingConditionClassTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.TlsCertificateType;
import org.eclipse.milo.opcua.sdk.server.model.objects.TlsCertificateTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.TlsClientCertificateType;
import org.eclipse.milo.opcua.sdk.server.model.objects.TlsClientCertificateTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.TlsServerCertificateType;
import org.eclipse.milo.opcua.sdk.server.model.objects.TlsServerCertificateTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.TrainingConditionClassType;
import org.eclipse.milo.opcua.sdk.server.model.objects.TrainingConditionClassTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.TransactionDiagnosticsType;
import org.eclipse.milo.opcua.sdk.server.model.objects.TransactionDiagnosticsTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.TransitionEventType;
import org.eclipse.milo.opcua.sdk.server.model.objects.TransitionEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.TransitionType;
import org.eclipse.milo.opcua.sdk.server.model.objects.TransitionTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.TransparentRedundancyType;
import org.eclipse.milo.opcua.sdk.server.model.objects.TransparentRedundancyTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.TripAlarmType;
import org.eclipse.milo.opcua.sdk.server.model.objects.TripAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.TrustListOutOfDateAlarmType;
import org.eclipse.milo.opcua.sdk.server.model.objects.TrustListOutOfDateAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.TrustListType;
import org.eclipse.milo.opcua.sdk.server.model.objects.TrustListTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.TrustListUpdateRequestedAuditEventType;
import org.eclipse.milo.opcua.sdk.server.model.objects.TrustListUpdateRequestedAuditEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.TrustListUpdatedAuditEventType;
import org.eclipse.milo.opcua.sdk.server.model.objects.TrustListUpdatedAuditEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.UadpDataSetReaderMessageType;
import org.eclipse.milo.opcua.sdk.server.model.objects.UadpDataSetReaderMessageTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.UadpDataSetWriterMessageType;
import org.eclipse.milo.opcua.sdk.server.model.objects.UadpDataSetWriterMessageTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.UadpWriterGroupMessageType;
import org.eclipse.milo.opcua.sdk.server.model.objects.UadpWriterGroupMessageTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.UnitType;
import org.eclipse.milo.opcua.sdk.server.model.objects.UnitTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.UriDictionaryEntryType;
import org.eclipse.milo.opcua.sdk.server.model.objects.UriDictionaryEntryTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.UserCertificateType;
import org.eclipse.milo.opcua.sdk.server.model.objects.UserCertificateTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.UserManagementType;
import org.eclipse.milo.opcua.sdk.server.model.objects.UserManagementTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.VendorServerInfoType;
import org.eclipse.milo.opcua.sdk.server.model.objects.VendorServerInfoTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.WriterGroupMessageType;
import org.eclipse.milo.opcua.sdk.server.model.objects.WriterGroupMessageTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.WriterGroupTransportType;
import org.eclipse.milo.opcua.sdk.server.model.objects.WriterGroupTransportTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.WriterGroupType;
import org.eclipse.milo.opcua.sdk.server.model.objects.WriterGroupTypeNode;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;

/**
 * Registers this library's ObjectType constructors for the server's lifetime. Serialize
 * initialization before loading.
 */
public final class ObjectTypeInitializer {
  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_0 =
      BaseObjectTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_1 =
      BaseEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_2 =
      ConditionTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_3 =
      AcknowledgeableConditionTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_4 =
      AlarmConditionTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_5 =
      LimitAlarmTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_6 =
      NonExclusiveLimitAlarmTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_7 =
      NonExclusiveLevelAlarmTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_8 =
      NonExclusiveRateOfChangeAlarmTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_9 =
      NonExclusiveDeviationAlarmTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_10 =
      DiscreteAlarmTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_11 =
      OffNormalAlarmTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_12 =
      TripAlarmTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_13 =
      AuditEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_14 =
      AuditUpdateMethodEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_15 =
      AuditConditionEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_16 =
      AuditConditionShelvingEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_17 =
      BaseConditionClassTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_18 =
      ProcessConditionClassTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_19 =
      MaintenanceConditionClassTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_20 =
      SystemConditionClassTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_21 =
      AggregateConfigurationTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_22 =
      ProgressEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_23 =
      SystemEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_24 =
      SystemStatusChangeEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_25 = FolderTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_26 =
      OperationLimitsTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_27 = FileTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_28 =
      AddressSpaceFileTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_29 =
      NamespaceMetadataTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_30 =
      NamespacesTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_31 =
      SystemOffNormalAlarmTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_32 =
      AuditUpdateStateEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_33 =
      AuditProgramTransitionEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_34 =
      ServerRedundancyTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_35 =
      NonTransparentRedundancyTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_36 =
      NonTransparentNetworkRedundancyTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_37 =
      TrustListTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_38 =
      CertificateGroupTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_39 =
      CertificateTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_40 =
      ApplicationCertificateTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_41 =
      HttpsCertificateTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_42 =
      RsaMinApplicationCertificateTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_43 =
      RsaSha256ApplicationCertificateTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_44 =
      TrustListUpdatedAuditEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_45 =
      ServerConfigurationTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_46 =
      CertificateUpdatedAuditEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_47 =
      CertificateExpirationAlarmTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_48 =
      FileDirectoryTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_49 =
      CertificateGroupFolderTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_50 =
      PubSubConnectionTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_51 =
      PubSubGroupTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_52 =
      PubSubKeyServiceTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_53 =
      PublishSubscribeTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_54 =
      DataSetFolderTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_55 =
      PublishedDataSetTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_56 =
      PublishedDataItemsTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_57 =
      PublishedEventsTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_58 =
      PubSubStatusTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_59 =
      AuditConditionResetEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_60 =
      ConnectionTransportTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_61 =
      DatagramConnectionTransportTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_62 =
      SubscribedDataSetTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_63 = StateTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_64 =
      ChoiceStateTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_65 =
      TargetVariablesTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_66 =
      SubscribedDataSetMirrorTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_67 =
      BrokerConnectionTransportTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_68 =
      DataSetWriterTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_69 =
      DataSetWriterTransportTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_70 =
      DataSetReaderTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_71 =
      DataSetReaderTransportTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_72 =
      ConfigurationFileTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_73 =
      SecurityGroupFolderTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_74 =
      SecurityGroupTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_75 =
      ExtensionFieldsTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_76 =
      PubSubStatusEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_77 =
      ConfigurationUpdatedAuditEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_78 =
      PubSubTransportLimitsExceedEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_79 =
      ApplicationConfigurationFileTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_80 =
      PubSubCommunicationFailureEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_81 =
      RoleSetTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_82 = RoleTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_83 =
      TemporaryFileTransferTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_84 =
      StateMachineTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_85 =
      FiniteStateMachineTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_86 =
      FileTransferStateMachineTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_87 =
      AlarmGroupTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_88 =
      ApplicationConfigurationFolderTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_89 =
      DiscrepancyAlarmTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_90 =
      SafetyConditionClassTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_91 =
      HighlyManagedAlarmConditionClassTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_92 =
      TrainingConditionClassTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_93 =
      TestingConditionClassTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_94 =
      AuditConditionSuppressionEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_95 =
      AuditConditionSilenceEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_96 =
      AuditConditionOutOfServiceEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_97 =
      AlarmMetricsTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_98 =
      KeyCredentialConfigurationFolderTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_99 =
      DictionaryEntryTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_100 =
      DictionaryFolderTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_101 =
      IrdiDictionaryEntryTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_102 =
      UriDictionaryEntryTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_103 =
      BaseInterfaceTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_104 =
      RoleMappingRuleChangedAuditEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_105 =
      WriterGroupTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_106 =
      AuthorizationServiceConfigurationTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_107 =
      WriterGroupTransportTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_108 =
      WriterGroupMessageTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_109 =
      ReaderGroupTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_110 =
      KeyCredentialConfigurationTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_111 =
      KeyCredentialAuditEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_112 =
      KeyCredentialUpdatedAuditEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_113 =
      KeyCredentialDeletedAuditEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_114 =
      InstrumentDiagnosticAlarmTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_115 =
      SystemDiagnosticAlarmTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_116 =
      StatisticalConditionClassTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_117 =
      LldpInformationTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_118 =
      LldpRemoteStatisticsTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_119 =
      LldpLocalSystemTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_120 =
      LldpPortInformationTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_121 =
      LldpRemoteSystemTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_122 =
      AuditUpdateEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_123 =
      AuditHistoryUpdateEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_124 =
      AuditHistoryAnnotationUpdateEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_125 =
      TrustListOutOfDateAlarmTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_126 =
      UserCertificateTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_127 =
      TlsCertificateTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_128 =
      TlsServerCertificateTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_129 =
      TlsClientCertificateTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_130 =
      LogObjectTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_131 =
      BaseLogEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_132 =
      LogOverflowEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_133 =
      LogEntryConditionClassTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_134 =
      PubSubDiagnosticsTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_135 =
      PubSubDiagnosticsRootTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_136 =
      PubSubDiagnosticsConnectionTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_137 =
      DataTypeRefinementTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_138 =
      SubtypeRestrictionTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_139 =
      SerializationEntityTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_140 =
      PubSubDiagnosticsWriterGroupTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_141 =
      PubSubDiagnosticsReaderGroupTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_142 =
      PubSubDiagnosticsDataSetWriterTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_143 =
      PubSubDiagnosticsDataSetReaderTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_144 =
      ServerTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_145 =
      ServerCapabilitiesTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_146 =
      ServerDiagnosticsTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_147 =
      SessionsDiagnosticsSummaryTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_148 =
      SessionDiagnosticsObjectTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_149 =
      VendorServerInfoTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_150 =
      TransparentRedundancyTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_151 =
      AuditSecurityEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_152 =
      AuditChannelEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_153 =
      AuditOpenSecureChannelEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_154 =
      AuditSessionEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_155 =
      AuditCreateSessionEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_156 =
      AuditActivateSessionEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_157 =
      AuditCancelEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_158 =
      AuditCertificateEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_159 =
      AuditCertificateDataMismatchEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_160 =
      AuditCertificateExpiredEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_161 =
      AuditCertificateInvalidEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_162 =
      AuditCertificateUntrustedEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_163 =
      AuditCertificateRevokedEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_164 =
      AuditCertificateMismatchEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_165 =
      AuditNodeManagementEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_166 =
      AuditAddNodesEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_167 =
      AuditDeleteNodesEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_168 =
      AuditAddReferencesEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_169 =
      AuditDeleteReferencesEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_170 =
      AuditWriteUpdateEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_171 =
      ReaderGroupTransportTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_172 =
      ReaderGroupMessageTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_173 =
      DataSetWriterMessageTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_174 =
      DataSetReaderMessageTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_175 =
      UadpWriterGroupMessageTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_176 =
      UadpDataSetWriterMessageTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_177 =
      UadpDataSetReaderMessageTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_178 =
      JsonWriterGroupMessageTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_179 =
      JsonDataSetWriterMessageTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_180 =
      JsonDataSetReaderMessageTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_181 =
      DatagramWriterGroupTransportTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_182 =
      BrokerWriterGroupTransportTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_183 =
      BrokerDataSetWriterTransportTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_184 =
      BrokerDataSetReaderTransportTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_185 =
      NetworkAddressTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_186 =
      NetworkAddressUrlTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_187 =
      DeviceFailureEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_188 =
      BaseModelChangeEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_189 =
      GeneralModelChangeEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_190 =
      InitialStateTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_191 =
      TransitionTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_192 =
      TransitionEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_193 =
      HistoricalDataConfigurationTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_194 =
      HistoryServerCapabilitiesTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_195 =
      AggregateFunctionTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_196 =
      AliasNameTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_197 =
      AliasNameCategoryTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_198 =
      IOrderedObjectTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_199 =
      OrderedListTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_200 =
      EccApplicationCertificateTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_201 =
      EccNistP256ApplicationCertificateTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_202 =
      EccNistP384ApplicationCertificateTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_203 =
      EccBrainpoolP256r1ApplicationCertificateTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_204 =
      EccBrainpoolP384r1ApplicationCertificateTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_205 =
      EccCurve25519ApplicationCertificateTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_206 =
      EccCurve448ApplicationCertificateTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_207 =
      AuthorizationServicesConfigurationFolderTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_208 =
      AuditClientEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_209 =
      ProgramTransitionEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_210 =
      SubscribedDataSetFolderTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_211 =
      StandaloneSubscribedDataSetTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_212 =
      PubSubCapabilitiesTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_213 =
      ProgramStateMachineTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_214 =
      AuditClientUpdateMethodResultEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_215 =
      DatagramDataSetReaderTransportTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_216 =
      IIetfBaseNetworkInterfaceTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_217 =
      IIeeeBaseEthernetPortTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_218 =
      IBaseEthernetCapabilitiesTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_219 =
      ISrClassTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_220 =
      IIeeeBaseTsnStreamTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_221 =
      IIeeeBaseTsnTrafficSpecificationTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_222 =
      IIeeeBaseTsnStatusStreamTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_223 =
      IIeeeTsnInterfaceConfigurationTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_224 =
      IIeeeTsnInterfaceConfigurationTalkerTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_225 =
      IIeeeTsnInterfaceConfigurationListenerTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_226 =
      IIeeeTsnMacAddressTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_227 =
      IIeeeTsnVlanTagTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_228 =
      IPriorityMappingEntryTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_229 =
      IIeeeAutoNegotiationStatusTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_230 =
      UserManagementTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_231 =
      IVlanIdTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_232 =
      IetfBaseNetworkInterfaceTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_233 =
      PriorityMappingTableTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_234 =
      PubSubKeyPushTargetTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_235 =
      PubSubKeyPushTargetFolderTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_236 =
      PubSubConfigurationTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_237 =
      ApplicationConfigurationTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_238 =
      ProvisionableDeviceTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_239 =
      SemanticChangeEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_240 =
      AuditUrlMismatchEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_241 =
      RefreshStartEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_242 =
      RefreshEndEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_243 =
      RefreshRequiredEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_244 =
      AuditConditionEnableEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_245 =
      AuditConditionCommentEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_246 =
      DialogConditionTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_247 =
      ShelvedStateMachineTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_248 =
      AuditHistoryEventUpdateEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_249 =
      AuditHistoryValueUpdateEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_250 =
      AuditHistoryDeleteEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_251 =
      AuditHistoryRawModifyDeleteEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_252 =
      AuditHistoryAtTimeDeleteEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_253 =
      AuditHistoryEventDeleteEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_254 =
      EventQueueOverflowEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_255 =
      AlarmSuppressionGroupTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_256 =
      TrustListUpdateRequestedAuditEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_257 =
      TransactionDiagnosticsTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_258 =
      CertificateUpdateRequestedAuditEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_259 =
      NonTransparentBackupRedundancyTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_260 =
      SyntaxReferenceEntryTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_261 = UnitTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_262 =
      ServerUnitTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_263 =
      AlternativeUnitTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_264 =
      QuantityTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_265 =
      HistoricalEventConfigurationTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_266 =
      HistoricalExternalEventSourceTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_267 =
      AuditHistoryConfigurationChangeEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_268 =
      AuditHistoryBulkInsertEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_269 =
      ProgramTransitionAuditEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_270 =
      DataTypeSystemTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_271 =
      DataTypeEncodingTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_272 =
      ModellingRuleTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_273 =
      AuditConditionRespondEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_274 =
      AuditConditionAcknowledgeEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_275 =
      AuditConditionConfirmEventTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_276 =
      ExclusiveLimitStateMachineTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_277 =
      ExclusiveLimitAlarmTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_278 =
      ExclusiveLevelAlarmTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_279 =
      ExclusiveRateOfChangeAlarmTypeNode::new;

  private static final ObjectTypeManager.ObjectNodeConstructor CONSTRUCTOR_280 =
      ExclusiveDeviationAlarmTypeNode::new;

  private ObjectTypeInitializer() {}

  /**
   * Prevalidates every namespace and registration before installing constructors. Repeated calls
   * preserve identical registrations.
   *
   * @param namespaceTable the server namespace table with required URIs already registered.
   * @param manager the server's type manager.
   * @throws IllegalStateException if any existing registration differs.
   * @throws IllegalArgumentException if a namespace is absent.
   */
  public static void initialize(NamespaceTable namespaceTable, ObjectTypeManager manager) {
    NodeId[] ids = new NodeId[281];
    boolean[] absent = new boolean[281];
    validate0(namespaceTable, manager, ids, absent);
    validate1(namespaceTable, manager, ids, absent);
    validate2(namespaceTable, manager, ids, absent);
    install0(manager, ids, absent);
    install1(manager, ids, absent);
    install2(manager, ids, absent);
  }

  private static void validate0(
      NamespaceTable namespaceTable, ObjectTypeManager manager, NodeId[] ids, boolean[] absent) {
    NodeId id0 = ServerNodeSupport.resolve(namespaceTable, BaseObjectType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered0 =
        manager.getRegisteredType(id0).orElse(null);
    if (registered0 != null
        && (registered0.nodeClass() != BaseObjectTypeNode.class
            || registered0.nodeConstructor() != CONSTRUCTOR_0
            || registered0.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + BaseObjectType.TYPE_ID
              + " expected "
              + BaseObjectTypeNode.class.getName());
    }
    ids[0] = id0;
    absent[0] = registered0 == null;
    NodeId id1 = ServerNodeSupport.resolve(namespaceTable, BaseEventType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered1 =
        manager.getRegisteredType(id1).orElse(null);
    if (registered1 != null
        && (registered1.nodeClass() != BaseEventTypeNode.class
            || registered1.nodeConstructor() != CONSTRUCTOR_1
            || registered1.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + BaseEventType.TYPE_ID
              + " expected "
              + BaseEventTypeNode.class.getName());
    }
    ids[1] = id1;
    absent[1] = registered1 == null;
    NodeId id2 = ServerNodeSupport.resolve(namespaceTable, ConditionType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered2 =
        manager.getRegisteredType(id2).orElse(null);
    if (registered2 != null
        && (registered2.nodeClass() != ConditionTypeNode.class
            || registered2.nodeConstructor() != CONSTRUCTOR_2
            || registered2.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + ConditionType.TYPE_ID
              + " expected "
              + ConditionTypeNode.class.getName());
    }
    ids[2] = id2;
    absent[2] = registered2 == null;
    NodeId id3 = ServerNodeSupport.resolve(namespaceTable, AcknowledgeableConditionType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered3 =
        manager.getRegisteredType(id3).orElse(null);
    if (registered3 != null
        && (registered3.nodeClass() != AcknowledgeableConditionTypeNode.class
            || registered3.nodeConstructor() != CONSTRUCTOR_3
            || registered3.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + AcknowledgeableConditionType.TYPE_ID
              + " expected "
              + AcknowledgeableConditionTypeNode.class.getName());
    }
    ids[3] = id3;
    absent[3] = registered3 == null;
    NodeId id4 = ServerNodeSupport.resolve(namespaceTable, AlarmConditionType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered4 =
        manager.getRegisteredType(id4).orElse(null);
    if (registered4 != null
        && (registered4.nodeClass() != AlarmConditionTypeNode.class
            || registered4.nodeConstructor() != CONSTRUCTOR_4
            || registered4.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + AlarmConditionType.TYPE_ID
              + " expected "
              + AlarmConditionTypeNode.class.getName());
    }
    ids[4] = id4;
    absent[4] = registered4 == null;
    NodeId id5 = ServerNodeSupport.resolve(namespaceTable, LimitAlarmType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered5 =
        manager.getRegisteredType(id5).orElse(null);
    if (registered5 != null
        && (registered5.nodeClass() != LimitAlarmTypeNode.class
            || registered5.nodeConstructor() != CONSTRUCTOR_5
            || registered5.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + LimitAlarmType.TYPE_ID
              + " expected "
              + LimitAlarmTypeNode.class.getName());
    }
    ids[5] = id5;
    absent[5] = registered5 == null;
    NodeId id6 = ServerNodeSupport.resolve(namespaceTable, NonExclusiveLimitAlarmType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered6 =
        manager.getRegisteredType(id6).orElse(null);
    if (registered6 != null
        && (registered6.nodeClass() != NonExclusiveLimitAlarmTypeNode.class
            || registered6.nodeConstructor() != CONSTRUCTOR_6
            || registered6.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + NonExclusiveLimitAlarmType.TYPE_ID
              + " expected "
              + NonExclusiveLimitAlarmTypeNode.class.getName());
    }
    ids[6] = id6;
    absent[6] = registered6 == null;
    NodeId id7 = ServerNodeSupport.resolve(namespaceTable, NonExclusiveLevelAlarmType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered7 =
        manager.getRegisteredType(id7).orElse(null);
    if (registered7 != null
        && (registered7.nodeClass() != NonExclusiveLevelAlarmTypeNode.class
            || registered7.nodeConstructor() != CONSTRUCTOR_7
            || registered7.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + NonExclusiveLevelAlarmType.TYPE_ID
              + " expected "
              + NonExclusiveLevelAlarmTypeNode.class.getName());
    }
    ids[7] = id7;
    absent[7] = registered7 == null;
    NodeId id8 =
        ServerNodeSupport.resolve(namespaceTable, NonExclusiveRateOfChangeAlarmType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered8 =
        manager.getRegisteredType(id8).orElse(null);
    if (registered8 != null
        && (registered8.nodeClass() != NonExclusiveRateOfChangeAlarmTypeNode.class
            || registered8.nodeConstructor() != CONSTRUCTOR_8
            || registered8.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + NonExclusiveRateOfChangeAlarmType.TYPE_ID
              + " expected "
              + NonExclusiveRateOfChangeAlarmTypeNode.class.getName());
    }
    ids[8] = id8;
    absent[8] = registered8 == null;
    NodeId id9 = ServerNodeSupport.resolve(namespaceTable, NonExclusiveDeviationAlarmType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered9 =
        manager.getRegisteredType(id9).orElse(null);
    if (registered9 != null
        && (registered9.nodeClass() != NonExclusiveDeviationAlarmTypeNode.class
            || registered9.nodeConstructor() != CONSTRUCTOR_9
            || registered9.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + NonExclusiveDeviationAlarmType.TYPE_ID
              + " expected "
              + NonExclusiveDeviationAlarmTypeNode.class.getName());
    }
    ids[9] = id9;
    absent[9] = registered9 == null;
    NodeId id10 = ServerNodeSupport.resolve(namespaceTable, DiscreteAlarmType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered10 =
        manager.getRegisteredType(id10).orElse(null);
    if (registered10 != null
        && (registered10.nodeClass() != DiscreteAlarmTypeNode.class
            || registered10.nodeConstructor() != CONSTRUCTOR_10
            || registered10.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + DiscreteAlarmType.TYPE_ID
              + " expected "
              + DiscreteAlarmTypeNode.class.getName());
    }
    ids[10] = id10;
    absent[10] = registered10 == null;
    NodeId id11 = ServerNodeSupport.resolve(namespaceTable, OffNormalAlarmType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered11 =
        manager.getRegisteredType(id11).orElse(null);
    if (registered11 != null
        && (registered11.nodeClass() != OffNormalAlarmTypeNode.class
            || registered11.nodeConstructor() != CONSTRUCTOR_11
            || registered11.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + OffNormalAlarmType.TYPE_ID
              + " expected "
              + OffNormalAlarmTypeNode.class.getName());
    }
    ids[11] = id11;
    absent[11] = registered11 == null;
    NodeId id12 = ServerNodeSupport.resolve(namespaceTable, TripAlarmType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered12 =
        manager.getRegisteredType(id12).orElse(null);
    if (registered12 != null
        && (registered12.nodeClass() != TripAlarmTypeNode.class
            || registered12.nodeConstructor() != CONSTRUCTOR_12
            || registered12.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + TripAlarmType.TYPE_ID
              + " expected "
              + TripAlarmTypeNode.class.getName());
    }
    ids[12] = id12;
    absent[12] = registered12 == null;
    NodeId id13 = ServerNodeSupport.resolve(namespaceTable, AuditEventType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered13 =
        manager.getRegisteredType(id13).orElse(null);
    if (registered13 != null
        && (registered13.nodeClass() != AuditEventTypeNode.class
            || registered13.nodeConstructor() != CONSTRUCTOR_13
            || registered13.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + AuditEventType.TYPE_ID
              + " expected "
              + AuditEventTypeNode.class.getName());
    }
    ids[13] = id13;
    absent[13] = registered13 == null;
    NodeId id14 = ServerNodeSupport.resolve(namespaceTable, AuditUpdateMethodEventType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered14 =
        manager.getRegisteredType(id14).orElse(null);
    if (registered14 != null
        && (registered14.nodeClass() != AuditUpdateMethodEventTypeNode.class
            || registered14.nodeConstructor() != CONSTRUCTOR_14
            || registered14.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + AuditUpdateMethodEventType.TYPE_ID
              + " expected "
              + AuditUpdateMethodEventTypeNode.class.getName());
    }
    ids[14] = id14;
    absent[14] = registered14 == null;
    NodeId id15 = ServerNodeSupport.resolve(namespaceTable, AuditConditionEventType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered15 =
        manager.getRegisteredType(id15).orElse(null);
    if (registered15 != null
        && (registered15.nodeClass() != AuditConditionEventTypeNode.class
            || registered15.nodeConstructor() != CONSTRUCTOR_15
            || registered15.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + AuditConditionEventType.TYPE_ID
              + " expected "
              + AuditConditionEventTypeNode.class.getName());
    }
    ids[15] = id15;
    absent[15] = registered15 == null;
    NodeId id16 =
        ServerNodeSupport.resolve(namespaceTable, AuditConditionShelvingEventType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered16 =
        manager.getRegisteredType(id16).orElse(null);
    if (registered16 != null
        && (registered16.nodeClass() != AuditConditionShelvingEventTypeNode.class
            || registered16.nodeConstructor() != CONSTRUCTOR_16
            || registered16.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + AuditConditionShelvingEventType.TYPE_ID
              + " expected "
              + AuditConditionShelvingEventTypeNode.class.getName());
    }
    ids[16] = id16;
    absent[16] = registered16 == null;
    NodeId id17 = ServerNodeSupport.resolve(namespaceTable, BaseConditionClassType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered17 =
        manager.getRegisteredType(id17).orElse(null);
    if (registered17 != null
        && (registered17.nodeClass() != BaseConditionClassTypeNode.class
            || registered17.nodeConstructor() != CONSTRUCTOR_17
            || registered17.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + BaseConditionClassType.TYPE_ID
              + " expected "
              + BaseConditionClassTypeNode.class.getName());
    }
    ids[17] = id17;
    absent[17] = registered17 == null;
    NodeId id18 = ServerNodeSupport.resolve(namespaceTable, ProcessConditionClassType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered18 =
        manager.getRegisteredType(id18).orElse(null);
    if (registered18 != null
        && (registered18.nodeClass() != ProcessConditionClassTypeNode.class
            || registered18.nodeConstructor() != CONSTRUCTOR_18
            || registered18.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + ProcessConditionClassType.TYPE_ID
              + " expected "
              + ProcessConditionClassTypeNode.class.getName());
    }
    ids[18] = id18;
    absent[18] = registered18 == null;
    NodeId id19 = ServerNodeSupport.resolve(namespaceTable, MaintenanceConditionClassType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered19 =
        manager.getRegisteredType(id19).orElse(null);
    if (registered19 != null
        && (registered19.nodeClass() != MaintenanceConditionClassTypeNode.class
            || registered19.nodeConstructor() != CONSTRUCTOR_19
            || registered19.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + MaintenanceConditionClassType.TYPE_ID
              + " expected "
              + MaintenanceConditionClassTypeNode.class.getName());
    }
    ids[19] = id19;
    absent[19] = registered19 == null;
    NodeId id20 = ServerNodeSupport.resolve(namespaceTable, SystemConditionClassType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered20 =
        manager.getRegisteredType(id20).orElse(null);
    if (registered20 != null
        && (registered20.nodeClass() != SystemConditionClassTypeNode.class
            || registered20.nodeConstructor() != CONSTRUCTOR_20
            || registered20.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + SystemConditionClassType.TYPE_ID
              + " expected "
              + SystemConditionClassTypeNode.class.getName());
    }
    ids[20] = id20;
    absent[20] = registered20 == null;
    NodeId id21 = ServerNodeSupport.resolve(namespaceTable, AggregateConfigurationType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered21 =
        manager.getRegisteredType(id21).orElse(null);
    if (registered21 != null
        && (registered21.nodeClass() != AggregateConfigurationTypeNode.class
            || registered21.nodeConstructor() != CONSTRUCTOR_21
            || registered21.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + AggregateConfigurationType.TYPE_ID
              + " expected "
              + AggregateConfigurationTypeNode.class.getName());
    }
    ids[21] = id21;
    absent[21] = registered21 == null;
    NodeId id22 = ServerNodeSupport.resolve(namespaceTable, ProgressEventType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered22 =
        manager.getRegisteredType(id22).orElse(null);
    if (registered22 != null
        && (registered22.nodeClass() != ProgressEventTypeNode.class
            || registered22.nodeConstructor() != CONSTRUCTOR_22
            || registered22.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + ProgressEventType.TYPE_ID
              + " expected "
              + ProgressEventTypeNode.class.getName());
    }
    ids[22] = id22;
    absent[22] = registered22 == null;
    NodeId id23 = ServerNodeSupport.resolve(namespaceTable, SystemEventType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered23 =
        manager.getRegisteredType(id23).orElse(null);
    if (registered23 != null
        && (registered23.nodeClass() != SystemEventTypeNode.class
            || registered23.nodeConstructor() != CONSTRUCTOR_23
            || registered23.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + SystemEventType.TYPE_ID
              + " expected "
              + SystemEventTypeNode.class.getName());
    }
    ids[23] = id23;
    absent[23] = registered23 == null;
    NodeId id24 = ServerNodeSupport.resolve(namespaceTable, SystemStatusChangeEventType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered24 =
        manager.getRegisteredType(id24).orElse(null);
    if (registered24 != null
        && (registered24.nodeClass() != SystemStatusChangeEventTypeNode.class
            || registered24.nodeConstructor() != CONSTRUCTOR_24
            || registered24.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + SystemStatusChangeEventType.TYPE_ID
              + " expected "
              + SystemStatusChangeEventTypeNode.class.getName());
    }
    ids[24] = id24;
    absent[24] = registered24 == null;
    NodeId id25 = ServerNodeSupport.resolve(namespaceTable, FolderType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered25 =
        manager.getRegisteredType(id25).orElse(null);
    if (registered25 != null
        && (registered25.nodeClass() != FolderTypeNode.class
            || registered25.nodeConstructor() != CONSTRUCTOR_25
            || registered25.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + FolderType.TYPE_ID
              + " expected "
              + FolderTypeNode.class.getName());
    }
    ids[25] = id25;
    absent[25] = registered25 == null;
    NodeId id26 = ServerNodeSupport.resolve(namespaceTable, OperationLimitsType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered26 =
        manager.getRegisteredType(id26).orElse(null);
    if (registered26 != null
        && (registered26.nodeClass() != OperationLimitsTypeNode.class
            || registered26.nodeConstructor() != CONSTRUCTOR_26
            || registered26.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + OperationLimitsType.TYPE_ID
              + " expected "
              + OperationLimitsTypeNode.class.getName());
    }
    ids[26] = id26;
    absent[26] = registered26 == null;
    NodeId id27 = ServerNodeSupport.resolve(namespaceTable, FileType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered27 =
        manager.getRegisteredType(id27).orElse(null);
    if (registered27 != null
        && (registered27.nodeClass() != FileTypeNode.class
            || registered27.nodeConstructor() != CONSTRUCTOR_27
            || registered27.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + FileType.TYPE_ID
              + " expected "
              + FileTypeNode.class.getName());
    }
    ids[27] = id27;
    absent[27] = registered27 == null;
    NodeId id28 = ServerNodeSupport.resolve(namespaceTable, AddressSpaceFileType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered28 =
        manager.getRegisteredType(id28).orElse(null);
    if (registered28 != null
        && (registered28.nodeClass() != AddressSpaceFileTypeNode.class
            || registered28.nodeConstructor() != CONSTRUCTOR_28
            || registered28.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + AddressSpaceFileType.TYPE_ID
              + " expected "
              + AddressSpaceFileTypeNode.class.getName());
    }
    ids[28] = id28;
    absent[28] = registered28 == null;
    NodeId id29 = ServerNodeSupport.resolve(namespaceTable, NamespaceMetadataType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered29 =
        manager.getRegisteredType(id29).orElse(null);
    if (registered29 != null
        && (registered29.nodeClass() != NamespaceMetadataTypeNode.class
            || registered29.nodeConstructor() != CONSTRUCTOR_29
            || registered29.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + NamespaceMetadataType.TYPE_ID
              + " expected "
              + NamespaceMetadataTypeNode.class.getName());
    }
    ids[29] = id29;
    absent[29] = registered29 == null;
    NodeId id30 = ServerNodeSupport.resolve(namespaceTable, NamespacesType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered30 =
        manager.getRegisteredType(id30).orElse(null);
    if (registered30 != null
        && (registered30.nodeClass() != NamespacesTypeNode.class
            || registered30.nodeConstructor() != CONSTRUCTOR_30
            || registered30.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + NamespacesType.TYPE_ID
              + " expected "
              + NamespacesTypeNode.class.getName());
    }
    ids[30] = id30;
    absent[30] = registered30 == null;
    NodeId id31 = ServerNodeSupport.resolve(namespaceTable, SystemOffNormalAlarmType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered31 =
        manager.getRegisteredType(id31).orElse(null);
    if (registered31 != null
        && (registered31.nodeClass() != SystemOffNormalAlarmTypeNode.class
            || registered31.nodeConstructor() != CONSTRUCTOR_31
            || registered31.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + SystemOffNormalAlarmType.TYPE_ID
              + " expected "
              + SystemOffNormalAlarmTypeNode.class.getName());
    }
    ids[31] = id31;
    absent[31] = registered31 == null;
    NodeId id32 = ServerNodeSupport.resolve(namespaceTable, AuditUpdateStateEventType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered32 =
        manager.getRegisteredType(id32).orElse(null);
    if (registered32 != null
        && (registered32.nodeClass() != AuditUpdateStateEventTypeNode.class
            || registered32.nodeConstructor() != CONSTRUCTOR_32
            || registered32.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + AuditUpdateStateEventType.TYPE_ID
              + " expected "
              + AuditUpdateStateEventTypeNode.class.getName());
    }
    ids[32] = id32;
    absent[32] = registered32 == null;
    NodeId id33 =
        ServerNodeSupport.resolve(namespaceTable, AuditProgramTransitionEventType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered33 =
        manager.getRegisteredType(id33).orElse(null);
    if (registered33 != null
        && (registered33.nodeClass() != AuditProgramTransitionEventTypeNode.class
            || registered33.nodeConstructor() != CONSTRUCTOR_33
            || registered33.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + AuditProgramTransitionEventType.TYPE_ID
              + " expected "
              + AuditProgramTransitionEventTypeNode.class.getName());
    }
    ids[33] = id33;
    absent[33] = registered33 == null;
    NodeId id34 = ServerNodeSupport.resolve(namespaceTable, ServerRedundancyType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered34 =
        manager.getRegisteredType(id34).orElse(null);
    if (registered34 != null
        && (registered34.nodeClass() != ServerRedundancyTypeNode.class
            || registered34.nodeConstructor() != CONSTRUCTOR_34
            || registered34.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + ServerRedundancyType.TYPE_ID
              + " expected "
              + ServerRedundancyTypeNode.class.getName());
    }
    ids[34] = id34;
    absent[34] = registered34 == null;
    NodeId id35 = ServerNodeSupport.resolve(namespaceTable, NonTransparentRedundancyType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered35 =
        manager.getRegisteredType(id35).orElse(null);
    if (registered35 != null
        && (registered35.nodeClass() != NonTransparentRedundancyTypeNode.class
            || registered35.nodeConstructor() != CONSTRUCTOR_35
            || registered35.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + NonTransparentRedundancyType.TYPE_ID
              + " expected "
              + NonTransparentRedundancyTypeNode.class.getName());
    }
    ids[35] = id35;
    absent[35] = registered35 == null;
    NodeId id36 =
        ServerNodeSupport.resolve(namespaceTable, NonTransparentNetworkRedundancyType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered36 =
        manager.getRegisteredType(id36).orElse(null);
    if (registered36 != null
        && (registered36.nodeClass() != NonTransparentNetworkRedundancyTypeNode.class
            || registered36.nodeConstructor() != CONSTRUCTOR_36
            || registered36.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + NonTransparentNetworkRedundancyType.TYPE_ID
              + " expected "
              + NonTransparentNetworkRedundancyTypeNode.class.getName());
    }
    ids[36] = id36;
    absent[36] = registered36 == null;
    NodeId id37 = ServerNodeSupport.resolve(namespaceTable, TrustListType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered37 =
        manager.getRegisteredType(id37).orElse(null);
    if (registered37 != null
        && (registered37.nodeClass() != TrustListTypeNode.class
            || registered37.nodeConstructor() != CONSTRUCTOR_37
            || registered37.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + TrustListType.TYPE_ID
              + " expected "
              + TrustListTypeNode.class.getName());
    }
    ids[37] = id37;
    absent[37] = registered37 == null;
    NodeId id38 = ServerNodeSupport.resolve(namespaceTable, CertificateGroupType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered38 =
        manager.getRegisteredType(id38).orElse(null);
    if (registered38 != null
        && (registered38.nodeClass() != CertificateGroupTypeNode.class
            || registered38.nodeConstructor() != CONSTRUCTOR_38
            || registered38.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + CertificateGroupType.TYPE_ID
              + " expected "
              + CertificateGroupTypeNode.class.getName());
    }
    ids[38] = id38;
    absent[38] = registered38 == null;
    NodeId id39 = ServerNodeSupport.resolve(namespaceTable, CertificateType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered39 =
        manager.getRegisteredType(id39).orElse(null);
    if (registered39 != null
        && (registered39.nodeClass() != CertificateTypeNode.class
            || registered39.nodeConstructor() != CONSTRUCTOR_39
            || registered39.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + CertificateType.TYPE_ID
              + " expected "
              + CertificateTypeNode.class.getName());
    }
    ids[39] = id39;
    absent[39] = registered39 == null;
    NodeId id40 = ServerNodeSupport.resolve(namespaceTable, ApplicationCertificateType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered40 =
        manager.getRegisteredType(id40).orElse(null);
    if (registered40 != null
        && (registered40.nodeClass() != ApplicationCertificateTypeNode.class
            || registered40.nodeConstructor() != CONSTRUCTOR_40
            || registered40.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + ApplicationCertificateType.TYPE_ID
              + " expected "
              + ApplicationCertificateTypeNode.class.getName());
    }
    ids[40] = id40;
    absent[40] = registered40 == null;
    NodeId id41 = ServerNodeSupport.resolve(namespaceTable, HttpsCertificateType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered41 =
        manager.getRegisteredType(id41).orElse(null);
    if (registered41 != null
        && (registered41.nodeClass() != HttpsCertificateTypeNode.class
            || registered41.nodeConstructor() != CONSTRUCTOR_41
            || registered41.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + HttpsCertificateType.TYPE_ID
              + " expected "
              + HttpsCertificateTypeNode.class.getName());
    }
    ids[41] = id41;
    absent[41] = registered41 == null;
    NodeId id42 =
        ServerNodeSupport.resolve(namespaceTable, RsaMinApplicationCertificateType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered42 =
        manager.getRegisteredType(id42).orElse(null);
    if (registered42 != null
        && (registered42.nodeClass() != RsaMinApplicationCertificateTypeNode.class
            || registered42.nodeConstructor() != CONSTRUCTOR_42
            || registered42.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + RsaMinApplicationCertificateType.TYPE_ID
              + " expected "
              + RsaMinApplicationCertificateTypeNode.class.getName());
    }
    ids[42] = id42;
    absent[42] = registered42 == null;
    NodeId id43 =
        ServerNodeSupport.resolve(namespaceTable, RsaSha256ApplicationCertificateType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered43 =
        manager.getRegisteredType(id43).orElse(null);
    if (registered43 != null
        && (registered43.nodeClass() != RsaSha256ApplicationCertificateTypeNode.class
            || registered43.nodeConstructor() != CONSTRUCTOR_43
            || registered43.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + RsaSha256ApplicationCertificateType.TYPE_ID
              + " expected "
              + RsaSha256ApplicationCertificateTypeNode.class.getName());
    }
    ids[43] = id43;
    absent[43] = registered43 == null;
    NodeId id44 = ServerNodeSupport.resolve(namespaceTable, TrustListUpdatedAuditEventType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered44 =
        manager.getRegisteredType(id44).orElse(null);
    if (registered44 != null
        && (registered44.nodeClass() != TrustListUpdatedAuditEventTypeNode.class
            || registered44.nodeConstructor() != CONSTRUCTOR_44
            || registered44.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + TrustListUpdatedAuditEventType.TYPE_ID
              + " expected "
              + TrustListUpdatedAuditEventTypeNode.class.getName());
    }
    ids[44] = id44;
    absent[44] = registered44 == null;
    NodeId id45 = ServerNodeSupport.resolve(namespaceTable, ServerConfigurationType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered45 =
        manager.getRegisteredType(id45).orElse(null);
    if (registered45 != null
        && (registered45.nodeClass() != ServerConfigurationTypeNode.class
            || registered45.nodeConstructor() != CONSTRUCTOR_45
            || registered45.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + ServerConfigurationType.TYPE_ID
              + " expected "
              + ServerConfigurationTypeNode.class.getName());
    }
    ids[45] = id45;
    absent[45] = registered45 == null;
    NodeId id46 =
        ServerNodeSupport.resolve(namespaceTable, CertificateUpdatedAuditEventType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered46 =
        manager.getRegisteredType(id46).orElse(null);
    if (registered46 != null
        && (registered46.nodeClass() != CertificateUpdatedAuditEventTypeNode.class
            || registered46.nodeConstructor() != CONSTRUCTOR_46
            || registered46.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + CertificateUpdatedAuditEventType.TYPE_ID
              + " expected "
              + CertificateUpdatedAuditEventTypeNode.class.getName());
    }
    ids[46] = id46;
    absent[46] = registered46 == null;
    NodeId id47 = ServerNodeSupport.resolve(namespaceTable, CertificateExpirationAlarmType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered47 =
        manager.getRegisteredType(id47).orElse(null);
    if (registered47 != null
        && (registered47.nodeClass() != CertificateExpirationAlarmTypeNode.class
            || registered47.nodeConstructor() != CONSTRUCTOR_47
            || registered47.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + CertificateExpirationAlarmType.TYPE_ID
              + " expected "
              + CertificateExpirationAlarmTypeNode.class.getName());
    }
    ids[47] = id47;
    absent[47] = registered47 == null;
    NodeId id48 = ServerNodeSupport.resolve(namespaceTable, FileDirectoryType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered48 =
        manager.getRegisteredType(id48).orElse(null);
    if (registered48 != null
        && (registered48.nodeClass() != FileDirectoryTypeNode.class
            || registered48.nodeConstructor() != CONSTRUCTOR_48
            || registered48.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + FileDirectoryType.TYPE_ID
              + " expected "
              + FileDirectoryTypeNode.class.getName());
    }
    ids[48] = id48;
    absent[48] = registered48 == null;
    NodeId id49 = ServerNodeSupport.resolve(namespaceTable, CertificateGroupFolderType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered49 =
        manager.getRegisteredType(id49).orElse(null);
    if (registered49 != null
        && (registered49.nodeClass() != CertificateGroupFolderTypeNode.class
            || registered49.nodeConstructor() != CONSTRUCTOR_49
            || registered49.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + CertificateGroupFolderType.TYPE_ID
              + " expected "
              + CertificateGroupFolderTypeNode.class.getName());
    }
    ids[49] = id49;
    absent[49] = registered49 == null;
    NodeId id50 = ServerNodeSupport.resolve(namespaceTable, PubSubConnectionType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered50 =
        manager.getRegisteredType(id50).orElse(null);
    if (registered50 != null
        && (registered50.nodeClass() != PubSubConnectionTypeNode.class
            || registered50.nodeConstructor() != CONSTRUCTOR_50
            || registered50.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + PubSubConnectionType.TYPE_ID
              + " expected "
              + PubSubConnectionTypeNode.class.getName());
    }
    ids[50] = id50;
    absent[50] = registered50 == null;
    NodeId id51 = ServerNodeSupport.resolve(namespaceTable, PubSubGroupType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered51 =
        manager.getRegisteredType(id51).orElse(null);
    if (registered51 != null
        && (registered51.nodeClass() != PubSubGroupTypeNode.class
            || registered51.nodeConstructor() != CONSTRUCTOR_51
            || registered51.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + PubSubGroupType.TYPE_ID
              + " expected "
              + PubSubGroupTypeNode.class.getName());
    }
    ids[51] = id51;
    absent[51] = registered51 == null;
    NodeId id52 = ServerNodeSupport.resolve(namespaceTable, PubSubKeyServiceType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered52 =
        manager.getRegisteredType(id52).orElse(null);
    if (registered52 != null
        && (registered52.nodeClass() != PubSubKeyServiceTypeNode.class
            || registered52.nodeConstructor() != CONSTRUCTOR_52
            || registered52.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + PubSubKeyServiceType.TYPE_ID
              + " expected "
              + PubSubKeyServiceTypeNode.class.getName());
    }
    ids[52] = id52;
    absent[52] = registered52 == null;
    NodeId id53 = ServerNodeSupport.resolve(namespaceTable, PublishSubscribeType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered53 =
        manager.getRegisteredType(id53).orElse(null);
    if (registered53 != null
        && (registered53.nodeClass() != PublishSubscribeTypeNode.class
            || registered53.nodeConstructor() != CONSTRUCTOR_53
            || registered53.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + PublishSubscribeType.TYPE_ID
              + " expected "
              + PublishSubscribeTypeNode.class.getName());
    }
    ids[53] = id53;
    absent[53] = registered53 == null;
    NodeId id54 = ServerNodeSupport.resolve(namespaceTable, DataSetFolderType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered54 =
        manager.getRegisteredType(id54).orElse(null);
    if (registered54 != null
        && (registered54.nodeClass() != DataSetFolderTypeNode.class
            || registered54.nodeConstructor() != CONSTRUCTOR_54
            || registered54.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + DataSetFolderType.TYPE_ID
              + " expected "
              + DataSetFolderTypeNode.class.getName());
    }
    ids[54] = id54;
    absent[54] = registered54 == null;
    NodeId id55 = ServerNodeSupport.resolve(namespaceTable, PublishedDataSetType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered55 =
        manager.getRegisteredType(id55).orElse(null);
    if (registered55 != null
        && (registered55.nodeClass() != PublishedDataSetTypeNode.class
            || registered55.nodeConstructor() != CONSTRUCTOR_55
            || registered55.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + PublishedDataSetType.TYPE_ID
              + " expected "
              + PublishedDataSetTypeNode.class.getName());
    }
    ids[55] = id55;
    absent[55] = registered55 == null;
    NodeId id56 = ServerNodeSupport.resolve(namespaceTable, PublishedDataItemsType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered56 =
        manager.getRegisteredType(id56).orElse(null);
    if (registered56 != null
        && (registered56.nodeClass() != PublishedDataItemsTypeNode.class
            || registered56.nodeConstructor() != CONSTRUCTOR_56
            || registered56.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + PublishedDataItemsType.TYPE_ID
              + " expected "
              + PublishedDataItemsTypeNode.class.getName());
    }
    ids[56] = id56;
    absent[56] = registered56 == null;
    NodeId id57 = ServerNodeSupport.resolve(namespaceTable, PublishedEventsType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered57 =
        manager.getRegisteredType(id57).orElse(null);
    if (registered57 != null
        && (registered57.nodeClass() != PublishedEventsTypeNode.class
            || registered57.nodeConstructor() != CONSTRUCTOR_57
            || registered57.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + PublishedEventsType.TYPE_ID
              + " expected "
              + PublishedEventsTypeNode.class.getName());
    }
    ids[57] = id57;
    absent[57] = registered57 == null;
    NodeId id58 = ServerNodeSupport.resolve(namespaceTable, PubSubStatusType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered58 =
        manager.getRegisteredType(id58).orElse(null);
    if (registered58 != null
        && (registered58.nodeClass() != PubSubStatusTypeNode.class
            || registered58.nodeConstructor() != CONSTRUCTOR_58
            || registered58.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + PubSubStatusType.TYPE_ID
              + " expected "
              + PubSubStatusTypeNode.class.getName());
    }
    ids[58] = id58;
    absent[58] = registered58 == null;
    NodeId id59 = ServerNodeSupport.resolve(namespaceTable, AuditConditionResetEventType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered59 =
        manager.getRegisteredType(id59).orElse(null);
    if (registered59 != null
        && (registered59.nodeClass() != AuditConditionResetEventTypeNode.class
            || registered59.nodeConstructor() != CONSTRUCTOR_59
            || registered59.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + AuditConditionResetEventType.TYPE_ID
              + " expected "
              + AuditConditionResetEventTypeNode.class.getName());
    }
    ids[59] = id59;
    absent[59] = registered59 == null;
    NodeId id60 = ServerNodeSupport.resolve(namespaceTable, ConnectionTransportType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered60 =
        manager.getRegisteredType(id60).orElse(null);
    if (registered60 != null
        && (registered60.nodeClass() != ConnectionTransportTypeNode.class
            || registered60.nodeConstructor() != CONSTRUCTOR_60
            || registered60.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + ConnectionTransportType.TYPE_ID
              + " expected "
              + ConnectionTransportTypeNode.class.getName());
    }
    ids[60] = id60;
    absent[60] = registered60 == null;
    NodeId id61 =
        ServerNodeSupport.resolve(namespaceTable, DatagramConnectionTransportType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered61 =
        manager.getRegisteredType(id61).orElse(null);
    if (registered61 != null
        && (registered61.nodeClass() != DatagramConnectionTransportTypeNode.class
            || registered61.nodeConstructor() != CONSTRUCTOR_61
            || registered61.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + DatagramConnectionTransportType.TYPE_ID
              + " expected "
              + DatagramConnectionTransportTypeNode.class.getName());
    }
    ids[61] = id61;
    absent[61] = registered61 == null;
    NodeId id62 = ServerNodeSupport.resolve(namespaceTable, SubscribedDataSetType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered62 =
        manager.getRegisteredType(id62).orElse(null);
    if (registered62 != null
        && (registered62.nodeClass() != SubscribedDataSetTypeNode.class
            || registered62.nodeConstructor() != CONSTRUCTOR_62
            || registered62.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + SubscribedDataSetType.TYPE_ID
              + " expected "
              + SubscribedDataSetTypeNode.class.getName());
    }
    ids[62] = id62;
    absent[62] = registered62 == null;
    NodeId id63 = ServerNodeSupport.resolve(namespaceTable, StateType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered63 =
        manager.getRegisteredType(id63).orElse(null);
    if (registered63 != null
        && (registered63.nodeClass() != StateTypeNode.class
            || registered63.nodeConstructor() != CONSTRUCTOR_63
            || registered63.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + StateType.TYPE_ID
              + " expected "
              + StateTypeNode.class.getName());
    }
    ids[63] = id63;
    absent[63] = registered63 == null;
    NodeId id64 = ServerNodeSupport.resolve(namespaceTable, ChoiceStateType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered64 =
        manager.getRegisteredType(id64).orElse(null);
    if (registered64 != null
        && (registered64.nodeClass() != ChoiceStateTypeNode.class
            || registered64.nodeConstructor() != CONSTRUCTOR_64
            || registered64.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + ChoiceStateType.TYPE_ID
              + " expected "
              + ChoiceStateTypeNode.class.getName());
    }
    ids[64] = id64;
    absent[64] = registered64 == null;
    NodeId id65 = ServerNodeSupport.resolve(namespaceTable, TargetVariablesType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered65 =
        manager.getRegisteredType(id65).orElse(null);
    if (registered65 != null
        && (registered65.nodeClass() != TargetVariablesTypeNode.class
            || registered65.nodeConstructor() != CONSTRUCTOR_65
            || registered65.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + TargetVariablesType.TYPE_ID
              + " expected "
              + TargetVariablesTypeNode.class.getName());
    }
    ids[65] = id65;
    absent[65] = registered65 == null;
    NodeId id66 = ServerNodeSupport.resolve(namespaceTable, SubscribedDataSetMirrorType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered66 =
        manager.getRegisteredType(id66).orElse(null);
    if (registered66 != null
        && (registered66.nodeClass() != SubscribedDataSetMirrorTypeNode.class
            || registered66.nodeConstructor() != CONSTRUCTOR_66
            || registered66.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + SubscribedDataSetMirrorType.TYPE_ID
              + " expected "
              + SubscribedDataSetMirrorTypeNode.class.getName());
    }
    ids[66] = id66;
    absent[66] = registered66 == null;
    NodeId id67 = ServerNodeSupport.resolve(namespaceTable, BrokerConnectionTransportType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered67 =
        manager.getRegisteredType(id67).orElse(null);
    if (registered67 != null
        && (registered67.nodeClass() != BrokerConnectionTransportTypeNode.class
            || registered67.nodeConstructor() != CONSTRUCTOR_67
            || registered67.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + BrokerConnectionTransportType.TYPE_ID
              + " expected "
              + BrokerConnectionTransportTypeNode.class.getName());
    }
    ids[67] = id67;
    absent[67] = registered67 == null;
    NodeId id68 = ServerNodeSupport.resolve(namespaceTable, DataSetWriterType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered68 =
        manager.getRegisteredType(id68).orElse(null);
    if (registered68 != null
        && (registered68.nodeClass() != DataSetWriterTypeNode.class
            || registered68.nodeConstructor() != CONSTRUCTOR_68
            || registered68.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + DataSetWriterType.TYPE_ID
              + " expected "
              + DataSetWriterTypeNode.class.getName());
    }
    ids[68] = id68;
    absent[68] = registered68 == null;
    NodeId id69 = ServerNodeSupport.resolve(namespaceTable, DataSetWriterTransportType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered69 =
        manager.getRegisteredType(id69).orElse(null);
    if (registered69 != null
        && (registered69.nodeClass() != DataSetWriterTransportTypeNode.class
            || registered69.nodeConstructor() != CONSTRUCTOR_69
            || registered69.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + DataSetWriterTransportType.TYPE_ID
              + " expected "
              + DataSetWriterTransportTypeNode.class.getName());
    }
    ids[69] = id69;
    absent[69] = registered69 == null;
    NodeId id70 = ServerNodeSupport.resolve(namespaceTable, DataSetReaderType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered70 =
        manager.getRegisteredType(id70).orElse(null);
    if (registered70 != null
        && (registered70.nodeClass() != DataSetReaderTypeNode.class
            || registered70.nodeConstructor() != CONSTRUCTOR_70
            || registered70.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + DataSetReaderType.TYPE_ID
              + " expected "
              + DataSetReaderTypeNode.class.getName());
    }
    ids[70] = id70;
    absent[70] = registered70 == null;
    NodeId id71 = ServerNodeSupport.resolve(namespaceTable, DataSetReaderTransportType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered71 =
        manager.getRegisteredType(id71).orElse(null);
    if (registered71 != null
        && (registered71.nodeClass() != DataSetReaderTransportTypeNode.class
            || registered71.nodeConstructor() != CONSTRUCTOR_71
            || registered71.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + DataSetReaderTransportType.TYPE_ID
              + " expected "
              + DataSetReaderTransportTypeNode.class.getName());
    }
    ids[71] = id71;
    absent[71] = registered71 == null;
    NodeId id72 = ServerNodeSupport.resolve(namespaceTable, ConfigurationFileType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered72 =
        manager.getRegisteredType(id72).orElse(null);
    if (registered72 != null
        && (registered72.nodeClass() != ConfigurationFileTypeNode.class
            || registered72.nodeConstructor() != CONSTRUCTOR_72
            || registered72.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + ConfigurationFileType.TYPE_ID
              + " expected "
              + ConfigurationFileTypeNode.class.getName());
    }
    ids[72] = id72;
    absent[72] = registered72 == null;
    NodeId id73 = ServerNodeSupport.resolve(namespaceTable, SecurityGroupFolderType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered73 =
        manager.getRegisteredType(id73).orElse(null);
    if (registered73 != null
        && (registered73.nodeClass() != SecurityGroupFolderTypeNode.class
            || registered73.nodeConstructor() != CONSTRUCTOR_73
            || registered73.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + SecurityGroupFolderType.TYPE_ID
              + " expected "
              + SecurityGroupFolderTypeNode.class.getName());
    }
    ids[73] = id73;
    absent[73] = registered73 == null;
    NodeId id74 = ServerNodeSupport.resolve(namespaceTable, SecurityGroupType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered74 =
        manager.getRegisteredType(id74).orElse(null);
    if (registered74 != null
        && (registered74.nodeClass() != SecurityGroupTypeNode.class
            || registered74.nodeConstructor() != CONSTRUCTOR_74
            || registered74.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + SecurityGroupType.TYPE_ID
              + " expected "
              + SecurityGroupTypeNode.class.getName());
    }
    ids[74] = id74;
    absent[74] = registered74 == null;
    NodeId id75 = ServerNodeSupport.resolve(namespaceTable, ExtensionFieldsType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered75 =
        manager.getRegisteredType(id75).orElse(null);
    if (registered75 != null
        && (registered75.nodeClass() != ExtensionFieldsTypeNode.class
            || registered75.nodeConstructor() != CONSTRUCTOR_75
            || registered75.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + ExtensionFieldsType.TYPE_ID
              + " expected "
              + ExtensionFieldsTypeNode.class.getName());
    }
    ids[75] = id75;
    absent[75] = registered75 == null;
    NodeId id76 = ServerNodeSupport.resolve(namespaceTable, PubSubStatusEventType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered76 =
        manager.getRegisteredType(id76).orElse(null);
    if (registered76 != null
        && (registered76.nodeClass() != PubSubStatusEventTypeNode.class
            || registered76.nodeConstructor() != CONSTRUCTOR_76
            || registered76.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + PubSubStatusEventType.TYPE_ID
              + " expected "
              + PubSubStatusEventTypeNode.class.getName());
    }
    ids[76] = id76;
    absent[76] = registered76 == null;
    NodeId id77 =
        ServerNodeSupport.resolve(namespaceTable, ConfigurationUpdatedAuditEventType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered77 =
        manager.getRegisteredType(id77).orElse(null);
    if (registered77 != null
        && (registered77.nodeClass() != ConfigurationUpdatedAuditEventTypeNode.class
            || registered77.nodeConstructor() != CONSTRUCTOR_77
            || registered77.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + ConfigurationUpdatedAuditEventType.TYPE_ID
              + " expected "
              + ConfigurationUpdatedAuditEventTypeNode.class.getName());
    }
    ids[77] = id77;
    absent[77] = registered77 == null;
    NodeId id78 =
        ServerNodeSupport.resolve(namespaceTable, PubSubTransportLimitsExceedEventType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered78 =
        manager.getRegisteredType(id78).orElse(null);
    if (registered78 != null
        && (registered78.nodeClass() != PubSubTransportLimitsExceedEventTypeNode.class
            || registered78.nodeConstructor() != CONSTRUCTOR_78
            || registered78.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + PubSubTransportLimitsExceedEventType.TYPE_ID
              + " expected "
              + PubSubTransportLimitsExceedEventTypeNode.class.getName());
    }
    ids[78] = id78;
    absent[78] = registered78 == null;
    NodeId id79 =
        ServerNodeSupport.resolve(namespaceTable, ApplicationConfigurationFileType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered79 =
        manager.getRegisteredType(id79).orElse(null);
    if (registered79 != null
        && (registered79.nodeClass() != ApplicationConfigurationFileTypeNode.class
            || registered79.nodeConstructor() != CONSTRUCTOR_79
            || registered79.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + ApplicationConfigurationFileType.TYPE_ID
              + " expected "
              + ApplicationConfigurationFileTypeNode.class.getName());
    }
    ids[79] = id79;
    absent[79] = registered79 == null;
    NodeId id80 =
        ServerNodeSupport.resolve(namespaceTable, PubSubCommunicationFailureEventType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered80 =
        manager.getRegisteredType(id80).orElse(null);
    if (registered80 != null
        && (registered80.nodeClass() != PubSubCommunicationFailureEventTypeNode.class
            || registered80.nodeConstructor() != CONSTRUCTOR_80
            || registered80.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + PubSubCommunicationFailureEventType.TYPE_ID
              + " expected "
              + PubSubCommunicationFailureEventTypeNode.class.getName());
    }
    ids[80] = id80;
    absent[80] = registered80 == null;
    NodeId id81 = ServerNodeSupport.resolve(namespaceTable, RoleSetType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered81 =
        manager.getRegisteredType(id81).orElse(null);
    if (registered81 != null
        && (registered81.nodeClass() != RoleSetTypeNode.class
            || registered81.nodeConstructor() != CONSTRUCTOR_81
            || registered81.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + RoleSetType.TYPE_ID
              + " expected "
              + RoleSetTypeNode.class.getName());
    }
    ids[81] = id81;
    absent[81] = registered81 == null;
    NodeId id82 = ServerNodeSupport.resolve(namespaceTable, RoleType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered82 =
        manager.getRegisteredType(id82).orElse(null);
    if (registered82 != null
        && (registered82.nodeClass() != RoleTypeNode.class
            || registered82.nodeConstructor() != CONSTRUCTOR_82
            || registered82.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + RoleType.TYPE_ID
              + " expected "
              + RoleTypeNode.class.getName());
    }
    ids[82] = id82;
    absent[82] = registered82 == null;
    NodeId id83 = ServerNodeSupport.resolve(namespaceTable, TemporaryFileTransferType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered83 =
        manager.getRegisteredType(id83).orElse(null);
    if (registered83 != null
        && (registered83.nodeClass() != TemporaryFileTransferTypeNode.class
            || registered83.nodeConstructor() != CONSTRUCTOR_83
            || registered83.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + TemporaryFileTransferType.TYPE_ID
              + " expected "
              + TemporaryFileTransferTypeNode.class.getName());
    }
    ids[83] = id83;
    absent[83] = registered83 == null;
    NodeId id84 = ServerNodeSupport.resolve(namespaceTable, StateMachineType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered84 =
        manager.getRegisteredType(id84).orElse(null);
    if (registered84 != null
        && (registered84.nodeClass() != StateMachineTypeNode.class
            || registered84.nodeConstructor() != CONSTRUCTOR_84
            || registered84.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + StateMachineType.TYPE_ID
              + " expected "
              + StateMachineTypeNode.class.getName());
    }
    ids[84] = id84;
    absent[84] = registered84 == null;
    NodeId id85 = ServerNodeSupport.resolve(namespaceTable, FiniteStateMachineType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered85 =
        manager.getRegisteredType(id85).orElse(null);
    if (registered85 != null
        && (registered85.nodeClass() != FiniteStateMachineTypeNode.class
            || registered85.nodeConstructor() != CONSTRUCTOR_85
            || registered85.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + FiniteStateMachineType.TYPE_ID
              + " expected "
              + FiniteStateMachineTypeNode.class.getName());
    }
    ids[85] = id85;
    absent[85] = registered85 == null;
    NodeId id86 = ServerNodeSupport.resolve(namespaceTable, FileTransferStateMachineType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered86 =
        manager.getRegisteredType(id86).orElse(null);
    if (registered86 != null
        && (registered86.nodeClass() != FileTransferStateMachineTypeNode.class
            || registered86.nodeConstructor() != CONSTRUCTOR_86
            || registered86.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + FileTransferStateMachineType.TYPE_ID
              + " expected "
              + FileTransferStateMachineTypeNode.class.getName());
    }
    ids[86] = id86;
    absent[86] = registered86 == null;
    NodeId id87 = ServerNodeSupport.resolve(namespaceTable, AlarmGroupType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered87 =
        manager.getRegisteredType(id87).orElse(null);
    if (registered87 != null
        && (registered87.nodeClass() != AlarmGroupTypeNode.class
            || registered87.nodeConstructor() != CONSTRUCTOR_87
            || registered87.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + AlarmGroupType.TYPE_ID
              + " expected "
              + AlarmGroupTypeNode.class.getName());
    }
    ids[87] = id87;
    absent[87] = registered87 == null;
    NodeId id88 =
        ServerNodeSupport.resolve(namespaceTable, ApplicationConfigurationFolderType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered88 =
        manager.getRegisteredType(id88).orElse(null);
    if (registered88 != null
        && (registered88.nodeClass() != ApplicationConfigurationFolderTypeNode.class
            || registered88.nodeConstructor() != CONSTRUCTOR_88
            || registered88.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + ApplicationConfigurationFolderType.TYPE_ID
              + " expected "
              + ApplicationConfigurationFolderTypeNode.class.getName());
    }
    ids[88] = id88;
    absent[88] = registered88 == null;
    NodeId id89 = ServerNodeSupport.resolve(namespaceTable, DiscrepancyAlarmType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered89 =
        manager.getRegisteredType(id89).orElse(null);
    if (registered89 != null
        && (registered89.nodeClass() != DiscrepancyAlarmTypeNode.class
            || registered89.nodeConstructor() != CONSTRUCTOR_89
            || registered89.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + DiscrepancyAlarmType.TYPE_ID
              + " expected "
              + DiscrepancyAlarmTypeNode.class.getName());
    }
    ids[89] = id89;
    absent[89] = registered89 == null;
    NodeId id90 = ServerNodeSupport.resolve(namespaceTable, SafetyConditionClassType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered90 =
        manager.getRegisteredType(id90).orElse(null);
    if (registered90 != null
        && (registered90.nodeClass() != SafetyConditionClassTypeNode.class
            || registered90.nodeConstructor() != CONSTRUCTOR_90
            || registered90.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + SafetyConditionClassType.TYPE_ID
              + " expected "
              + SafetyConditionClassTypeNode.class.getName());
    }
    ids[90] = id90;
    absent[90] = registered90 == null;
    NodeId id91 =
        ServerNodeSupport.resolve(namespaceTable, HighlyManagedAlarmConditionClassType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered91 =
        manager.getRegisteredType(id91).orElse(null);
    if (registered91 != null
        && (registered91.nodeClass() != HighlyManagedAlarmConditionClassTypeNode.class
            || registered91.nodeConstructor() != CONSTRUCTOR_91
            || registered91.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + HighlyManagedAlarmConditionClassType.TYPE_ID
              + " expected "
              + HighlyManagedAlarmConditionClassTypeNode.class.getName());
    }
    ids[91] = id91;
    absent[91] = registered91 == null;
    NodeId id92 = ServerNodeSupport.resolve(namespaceTable, TrainingConditionClassType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered92 =
        manager.getRegisteredType(id92).orElse(null);
    if (registered92 != null
        && (registered92.nodeClass() != TrainingConditionClassTypeNode.class
            || registered92.nodeConstructor() != CONSTRUCTOR_92
            || registered92.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + TrainingConditionClassType.TYPE_ID
              + " expected "
              + TrainingConditionClassTypeNode.class.getName());
    }
    ids[92] = id92;
    absent[92] = registered92 == null;
    NodeId id93 = ServerNodeSupport.resolve(namespaceTable, TestingConditionClassType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered93 =
        manager.getRegisteredType(id93).orElse(null);
    if (registered93 != null
        && (registered93.nodeClass() != TestingConditionClassTypeNode.class
            || registered93.nodeConstructor() != CONSTRUCTOR_93
            || registered93.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + TestingConditionClassType.TYPE_ID
              + " expected "
              + TestingConditionClassTypeNode.class.getName());
    }
    ids[93] = id93;
    absent[93] = registered93 == null;
    NodeId id94 =
        ServerNodeSupport.resolve(namespaceTable, AuditConditionSuppressionEventType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered94 =
        manager.getRegisteredType(id94).orElse(null);
    if (registered94 != null
        && (registered94.nodeClass() != AuditConditionSuppressionEventTypeNode.class
            || registered94.nodeConstructor() != CONSTRUCTOR_94
            || registered94.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + AuditConditionSuppressionEventType.TYPE_ID
              + " expected "
              + AuditConditionSuppressionEventTypeNode.class.getName());
    }
    ids[94] = id94;
    absent[94] = registered94 == null;
    NodeId id95 = ServerNodeSupport.resolve(namespaceTable, AuditConditionSilenceEventType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered95 =
        manager.getRegisteredType(id95).orElse(null);
    if (registered95 != null
        && (registered95.nodeClass() != AuditConditionSilenceEventTypeNode.class
            || registered95.nodeConstructor() != CONSTRUCTOR_95
            || registered95.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + AuditConditionSilenceEventType.TYPE_ID
              + " expected "
              + AuditConditionSilenceEventTypeNode.class.getName());
    }
    ids[95] = id95;
    absent[95] = registered95 == null;
    NodeId id96 =
        ServerNodeSupport.resolve(namespaceTable, AuditConditionOutOfServiceEventType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered96 =
        manager.getRegisteredType(id96).orElse(null);
    if (registered96 != null
        && (registered96.nodeClass() != AuditConditionOutOfServiceEventTypeNode.class
            || registered96.nodeConstructor() != CONSTRUCTOR_96
            || registered96.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + AuditConditionOutOfServiceEventType.TYPE_ID
              + " expected "
              + AuditConditionOutOfServiceEventTypeNode.class.getName());
    }
    ids[96] = id96;
    absent[96] = registered96 == null;
    NodeId id97 = ServerNodeSupport.resolve(namespaceTable, AlarmMetricsType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered97 =
        manager.getRegisteredType(id97).orElse(null);
    if (registered97 != null
        && (registered97.nodeClass() != AlarmMetricsTypeNode.class
            || registered97.nodeConstructor() != CONSTRUCTOR_97
            || registered97.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + AlarmMetricsType.TYPE_ID
              + " expected "
              + AlarmMetricsTypeNode.class.getName());
    }
    ids[97] = id97;
    absent[97] = registered97 == null;
    NodeId id98 =
        ServerNodeSupport.resolve(namespaceTable, KeyCredentialConfigurationFolderType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered98 =
        manager.getRegisteredType(id98).orElse(null);
    if (registered98 != null
        && (registered98.nodeClass() != KeyCredentialConfigurationFolderTypeNode.class
            || registered98.nodeConstructor() != CONSTRUCTOR_98
            || registered98.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + KeyCredentialConfigurationFolderType.TYPE_ID
              + " expected "
              + KeyCredentialConfigurationFolderTypeNode.class.getName());
    }
    ids[98] = id98;
    absent[98] = registered98 == null;
    NodeId id99 = ServerNodeSupport.resolve(namespaceTable, DictionaryEntryType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered99 =
        manager.getRegisteredType(id99).orElse(null);
    if (registered99 != null
        && (registered99.nodeClass() != DictionaryEntryTypeNode.class
            || registered99.nodeConstructor() != CONSTRUCTOR_99
            || registered99.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + DictionaryEntryType.TYPE_ID
              + " expected "
              + DictionaryEntryTypeNode.class.getName());
    }
    ids[99] = id99;
    absent[99] = registered99 == null;
    NodeId id100 = ServerNodeSupport.resolve(namespaceTable, DictionaryFolderType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered100 =
        manager.getRegisteredType(id100).orElse(null);
    if (registered100 != null
        && (registered100.nodeClass() != DictionaryFolderTypeNode.class
            || registered100.nodeConstructor() != CONSTRUCTOR_100
            || registered100.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + DictionaryFolderType.TYPE_ID
              + " expected "
              + DictionaryFolderTypeNode.class.getName());
    }
    ids[100] = id100;
    absent[100] = registered100 == null;
    NodeId id101 = ServerNodeSupport.resolve(namespaceTable, IrdiDictionaryEntryType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered101 =
        manager.getRegisteredType(id101).orElse(null);
    if (registered101 != null
        && (registered101.nodeClass() != IrdiDictionaryEntryTypeNode.class
            || registered101.nodeConstructor() != CONSTRUCTOR_101
            || registered101.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + IrdiDictionaryEntryType.TYPE_ID
              + " expected "
              + IrdiDictionaryEntryTypeNode.class.getName());
    }
    ids[101] = id101;
    absent[101] = registered101 == null;
    NodeId id102 = ServerNodeSupport.resolve(namespaceTable, UriDictionaryEntryType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered102 =
        manager.getRegisteredType(id102).orElse(null);
    if (registered102 != null
        && (registered102.nodeClass() != UriDictionaryEntryTypeNode.class
            || registered102.nodeConstructor() != CONSTRUCTOR_102
            || registered102.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + UriDictionaryEntryType.TYPE_ID
              + " expected "
              + UriDictionaryEntryTypeNode.class.getName());
    }
    ids[102] = id102;
    absent[102] = registered102 == null;
    NodeId id103 = ServerNodeSupport.resolve(namespaceTable, BaseInterfaceType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered103 =
        manager.getRegisteredType(id103).orElse(null);
    if (registered103 != null
        && (registered103.nodeClass() != BaseInterfaceTypeNode.class
            || registered103.nodeConstructor() != CONSTRUCTOR_103
            || registered103.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + BaseInterfaceType.TYPE_ID
              + " expected "
              + BaseInterfaceTypeNode.class.getName());
    }
    ids[103] = id103;
    absent[103] = registered103 == null;
    NodeId id104 =
        ServerNodeSupport.resolve(namespaceTable, RoleMappingRuleChangedAuditEventType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered104 =
        manager.getRegisteredType(id104).orElse(null);
    if (registered104 != null
        && (registered104.nodeClass() != RoleMappingRuleChangedAuditEventTypeNode.class
            || registered104.nodeConstructor() != CONSTRUCTOR_104
            || registered104.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + RoleMappingRuleChangedAuditEventType.TYPE_ID
              + " expected "
              + RoleMappingRuleChangedAuditEventTypeNode.class.getName());
    }
    ids[104] = id104;
    absent[104] = registered104 == null;
    NodeId id105 = ServerNodeSupport.resolve(namespaceTable, WriterGroupType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered105 =
        manager.getRegisteredType(id105).orElse(null);
    if (registered105 != null
        && (registered105.nodeClass() != WriterGroupTypeNode.class
            || registered105.nodeConstructor() != CONSTRUCTOR_105
            || registered105.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + WriterGroupType.TYPE_ID
              + " expected "
              + WriterGroupTypeNode.class.getName());
    }
    ids[105] = id105;
    absent[105] = registered105 == null;
    NodeId id106 =
        ServerNodeSupport.resolve(namespaceTable, AuthorizationServiceConfigurationType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered106 =
        manager.getRegisteredType(id106).orElse(null);
    if (registered106 != null
        && (registered106.nodeClass() != AuthorizationServiceConfigurationTypeNode.class
            || registered106.nodeConstructor() != CONSTRUCTOR_106
            || registered106.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + AuthorizationServiceConfigurationType.TYPE_ID
              + " expected "
              + AuthorizationServiceConfigurationTypeNode.class.getName());
    }
    ids[106] = id106;
    absent[106] = registered106 == null;
    NodeId id107 = ServerNodeSupport.resolve(namespaceTable, WriterGroupTransportType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered107 =
        manager.getRegisteredType(id107).orElse(null);
    if (registered107 != null
        && (registered107.nodeClass() != WriterGroupTransportTypeNode.class
            || registered107.nodeConstructor() != CONSTRUCTOR_107
            || registered107.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + WriterGroupTransportType.TYPE_ID
              + " expected "
              + WriterGroupTransportTypeNode.class.getName());
    }
    ids[107] = id107;
    absent[107] = registered107 == null;
    NodeId id108 = ServerNodeSupport.resolve(namespaceTable, WriterGroupMessageType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered108 =
        manager.getRegisteredType(id108).orElse(null);
    if (registered108 != null
        && (registered108.nodeClass() != WriterGroupMessageTypeNode.class
            || registered108.nodeConstructor() != CONSTRUCTOR_108
            || registered108.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + WriterGroupMessageType.TYPE_ID
              + " expected "
              + WriterGroupMessageTypeNode.class.getName());
    }
    ids[108] = id108;
    absent[108] = registered108 == null;
    NodeId id109 = ServerNodeSupport.resolve(namespaceTable, ReaderGroupType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered109 =
        manager.getRegisteredType(id109).orElse(null);
    if (registered109 != null
        && (registered109.nodeClass() != ReaderGroupTypeNode.class
            || registered109.nodeConstructor() != CONSTRUCTOR_109
            || registered109.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + ReaderGroupType.TYPE_ID
              + " expected "
              + ReaderGroupTypeNode.class.getName());
    }
    ids[109] = id109;
    absent[109] = registered109 == null;
    NodeId id110 =
        ServerNodeSupport.resolve(namespaceTable, KeyCredentialConfigurationType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered110 =
        manager.getRegisteredType(id110).orElse(null);
    if (registered110 != null
        && (registered110.nodeClass() != KeyCredentialConfigurationTypeNode.class
            || registered110.nodeConstructor() != CONSTRUCTOR_110
            || registered110.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + KeyCredentialConfigurationType.TYPE_ID
              + " expected "
              + KeyCredentialConfigurationTypeNode.class.getName());
    }
    ids[110] = id110;
    absent[110] = registered110 == null;
    NodeId id111 = ServerNodeSupport.resolve(namespaceTable, KeyCredentialAuditEventType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered111 =
        manager.getRegisteredType(id111).orElse(null);
    if (registered111 != null
        && (registered111.nodeClass() != KeyCredentialAuditEventTypeNode.class
            || registered111.nodeConstructor() != CONSTRUCTOR_111
            || registered111.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + KeyCredentialAuditEventType.TYPE_ID
              + " expected "
              + KeyCredentialAuditEventTypeNode.class.getName());
    }
    ids[111] = id111;
    absent[111] = registered111 == null;
    NodeId id112 =
        ServerNodeSupport.resolve(namespaceTable, KeyCredentialUpdatedAuditEventType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered112 =
        manager.getRegisteredType(id112).orElse(null);
    if (registered112 != null
        && (registered112.nodeClass() != KeyCredentialUpdatedAuditEventTypeNode.class
            || registered112.nodeConstructor() != CONSTRUCTOR_112
            || registered112.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + KeyCredentialUpdatedAuditEventType.TYPE_ID
              + " expected "
              + KeyCredentialUpdatedAuditEventTypeNode.class.getName());
    }
    ids[112] = id112;
    absent[112] = registered112 == null;
    NodeId id113 =
        ServerNodeSupport.resolve(namespaceTable, KeyCredentialDeletedAuditEventType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered113 =
        manager.getRegisteredType(id113).orElse(null);
    if (registered113 != null
        && (registered113.nodeClass() != KeyCredentialDeletedAuditEventTypeNode.class
            || registered113.nodeConstructor() != CONSTRUCTOR_113
            || registered113.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + KeyCredentialDeletedAuditEventType.TYPE_ID
              + " expected "
              + KeyCredentialDeletedAuditEventTypeNode.class.getName());
    }
    ids[113] = id113;
    absent[113] = registered113 == null;
    NodeId id114 = ServerNodeSupport.resolve(namespaceTable, InstrumentDiagnosticAlarmType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered114 =
        manager.getRegisteredType(id114).orElse(null);
    if (registered114 != null
        && (registered114.nodeClass() != InstrumentDiagnosticAlarmTypeNode.class
            || registered114.nodeConstructor() != CONSTRUCTOR_114
            || registered114.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + InstrumentDiagnosticAlarmType.TYPE_ID
              + " expected "
              + InstrumentDiagnosticAlarmTypeNode.class.getName());
    }
    ids[114] = id114;
    absent[114] = registered114 == null;
    NodeId id115 = ServerNodeSupport.resolve(namespaceTable, SystemDiagnosticAlarmType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered115 =
        manager.getRegisteredType(id115).orElse(null);
    if (registered115 != null
        && (registered115.nodeClass() != SystemDiagnosticAlarmTypeNode.class
            || registered115.nodeConstructor() != CONSTRUCTOR_115
            || registered115.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + SystemDiagnosticAlarmType.TYPE_ID
              + " expected "
              + SystemDiagnosticAlarmTypeNode.class.getName());
    }
    ids[115] = id115;
    absent[115] = registered115 == null;
    NodeId id116 = ServerNodeSupport.resolve(namespaceTable, StatisticalConditionClassType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered116 =
        manager.getRegisteredType(id116).orElse(null);
    if (registered116 != null
        && (registered116.nodeClass() != StatisticalConditionClassTypeNode.class
            || registered116.nodeConstructor() != CONSTRUCTOR_116
            || registered116.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + StatisticalConditionClassType.TYPE_ID
              + " expected "
              + StatisticalConditionClassTypeNode.class.getName());
    }
    ids[116] = id116;
    absent[116] = registered116 == null;
    NodeId id117 = ServerNodeSupport.resolve(namespaceTable, LldpInformationType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered117 =
        manager.getRegisteredType(id117).orElse(null);
    if (registered117 != null
        && (registered117.nodeClass() != LldpInformationTypeNode.class
            || registered117.nodeConstructor() != CONSTRUCTOR_117
            || registered117.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + LldpInformationType.TYPE_ID
              + " expected "
              + LldpInformationTypeNode.class.getName());
    }
    ids[117] = id117;
    absent[117] = registered117 == null;
    NodeId id118 = ServerNodeSupport.resolve(namespaceTable, LldpRemoteStatisticsType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered118 =
        manager.getRegisteredType(id118).orElse(null);
    if (registered118 != null
        && (registered118.nodeClass() != LldpRemoteStatisticsTypeNode.class
            || registered118.nodeConstructor() != CONSTRUCTOR_118
            || registered118.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + LldpRemoteStatisticsType.TYPE_ID
              + " expected "
              + LldpRemoteStatisticsTypeNode.class.getName());
    }
    ids[118] = id118;
    absent[118] = registered118 == null;
    NodeId id119 = ServerNodeSupport.resolve(namespaceTable, LldpLocalSystemType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered119 =
        manager.getRegisteredType(id119).orElse(null);
    if (registered119 != null
        && (registered119.nodeClass() != LldpLocalSystemTypeNode.class
            || registered119.nodeConstructor() != CONSTRUCTOR_119
            || registered119.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + LldpLocalSystemType.TYPE_ID
              + " expected "
              + LldpLocalSystemTypeNode.class.getName());
    }
    ids[119] = id119;
    absent[119] = registered119 == null;
    NodeId id120 = ServerNodeSupport.resolve(namespaceTable, LldpPortInformationType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered120 =
        manager.getRegisteredType(id120).orElse(null);
    if (registered120 != null
        && (registered120.nodeClass() != LldpPortInformationTypeNode.class
            || registered120.nodeConstructor() != CONSTRUCTOR_120
            || registered120.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + LldpPortInformationType.TYPE_ID
              + " expected "
              + LldpPortInformationTypeNode.class.getName());
    }
    ids[120] = id120;
    absent[120] = registered120 == null;
    NodeId id121 = ServerNodeSupport.resolve(namespaceTable, LldpRemoteSystemType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered121 =
        manager.getRegisteredType(id121).orElse(null);
    if (registered121 != null
        && (registered121.nodeClass() != LldpRemoteSystemTypeNode.class
            || registered121.nodeConstructor() != CONSTRUCTOR_121
            || registered121.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + LldpRemoteSystemType.TYPE_ID
              + " expected "
              + LldpRemoteSystemTypeNode.class.getName());
    }
    ids[121] = id121;
    absent[121] = registered121 == null;
    NodeId id122 = ServerNodeSupport.resolve(namespaceTable, AuditUpdateEventType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered122 =
        manager.getRegisteredType(id122).orElse(null);
    if (registered122 != null
        && (registered122.nodeClass() != AuditUpdateEventTypeNode.class
            || registered122.nodeConstructor() != CONSTRUCTOR_122
            || registered122.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + AuditUpdateEventType.TYPE_ID
              + " expected "
              + AuditUpdateEventTypeNode.class.getName());
    }
    ids[122] = id122;
    absent[122] = registered122 == null;
    NodeId id123 = ServerNodeSupport.resolve(namespaceTable, AuditHistoryUpdateEventType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered123 =
        manager.getRegisteredType(id123).orElse(null);
    if (registered123 != null
        && (registered123.nodeClass() != AuditHistoryUpdateEventTypeNode.class
            || registered123.nodeConstructor() != CONSTRUCTOR_123
            || registered123.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + AuditHistoryUpdateEventType.TYPE_ID
              + " expected "
              + AuditHistoryUpdateEventTypeNode.class.getName());
    }
    ids[123] = id123;
    absent[123] = registered123 == null;
    NodeId id124 =
        ServerNodeSupport.resolve(namespaceTable, AuditHistoryAnnotationUpdateEventType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered124 =
        manager.getRegisteredType(id124).orElse(null);
    if (registered124 != null
        && (registered124.nodeClass() != AuditHistoryAnnotationUpdateEventTypeNode.class
            || registered124.nodeConstructor() != CONSTRUCTOR_124
            || registered124.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + AuditHistoryAnnotationUpdateEventType.TYPE_ID
              + " expected "
              + AuditHistoryAnnotationUpdateEventTypeNode.class.getName());
    }
    ids[124] = id124;
    absent[124] = registered124 == null;
    NodeId id125 = ServerNodeSupport.resolve(namespaceTable, TrustListOutOfDateAlarmType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered125 =
        manager.getRegisteredType(id125).orElse(null);
    if (registered125 != null
        && (registered125.nodeClass() != TrustListOutOfDateAlarmTypeNode.class
            || registered125.nodeConstructor() != CONSTRUCTOR_125
            || registered125.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + TrustListOutOfDateAlarmType.TYPE_ID
              + " expected "
              + TrustListOutOfDateAlarmTypeNode.class.getName());
    }
    ids[125] = id125;
    absent[125] = registered125 == null;
    NodeId id126 = ServerNodeSupport.resolve(namespaceTable, UserCertificateType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered126 =
        manager.getRegisteredType(id126).orElse(null);
    if (registered126 != null
        && (registered126.nodeClass() != UserCertificateTypeNode.class
            || registered126.nodeConstructor() != CONSTRUCTOR_126
            || registered126.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + UserCertificateType.TYPE_ID
              + " expected "
              + UserCertificateTypeNode.class.getName());
    }
    ids[126] = id126;
    absent[126] = registered126 == null;
    NodeId id127 = ServerNodeSupport.resolve(namespaceTable, TlsCertificateType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered127 =
        manager.getRegisteredType(id127).orElse(null);
    if (registered127 != null
        && (registered127.nodeClass() != TlsCertificateTypeNode.class
            || registered127.nodeConstructor() != CONSTRUCTOR_127
            || registered127.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + TlsCertificateType.TYPE_ID
              + " expected "
              + TlsCertificateTypeNode.class.getName());
    }
    ids[127] = id127;
    absent[127] = registered127 == null;
  }

  private static void validate1(
      NamespaceTable namespaceTable, ObjectTypeManager manager, NodeId[] ids, boolean[] absent) {
    NodeId id128 = ServerNodeSupport.resolve(namespaceTable, TlsServerCertificateType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered128 =
        manager.getRegisteredType(id128).orElse(null);
    if (registered128 != null
        && (registered128.nodeClass() != TlsServerCertificateTypeNode.class
            || registered128.nodeConstructor() != CONSTRUCTOR_128
            || registered128.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + TlsServerCertificateType.TYPE_ID
              + " expected "
              + TlsServerCertificateTypeNode.class.getName());
    }
    ids[128] = id128;
    absent[128] = registered128 == null;
    NodeId id129 = ServerNodeSupport.resolve(namespaceTable, TlsClientCertificateType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered129 =
        manager.getRegisteredType(id129).orElse(null);
    if (registered129 != null
        && (registered129.nodeClass() != TlsClientCertificateTypeNode.class
            || registered129.nodeConstructor() != CONSTRUCTOR_129
            || registered129.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + TlsClientCertificateType.TYPE_ID
              + " expected "
              + TlsClientCertificateTypeNode.class.getName());
    }
    ids[129] = id129;
    absent[129] = registered129 == null;
    NodeId id130 = ServerNodeSupport.resolve(namespaceTable, LogObjectType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered130 =
        manager.getRegisteredType(id130).orElse(null);
    if (registered130 != null
        && (registered130.nodeClass() != LogObjectTypeNode.class
            || registered130.nodeConstructor() != CONSTRUCTOR_130
            || registered130.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + LogObjectType.TYPE_ID
              + " expected "
              + LogObjectTypeNode.class.getName());
    }
    ids[130] = id130;
    absent[130] = registered130 == null;
    NodeId id131 = ServerNodeSupport.resolve(namespaceTable, BaseLogEventType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered131 =
        manager.getRegisteredType(id131).orElse(null);
    if (registered131 != null
        && (registered131.nodeClass() != BaseLogEventTypeNode.class
            || registered131.nodeConstructor() != CONSTRUCTOR_131
            || registered131.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + BaseLogEventType.TYPE_ID
              + " expected "
              + BaseLogEventTypeNode.class.getName());
    }
    ids[131] = id131;
    absent[131] = registered131 == null;
    NodeId id132 = ServerNodeSupport.resolve(namespaceTable, LogOverflowEventType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered132 =
        manager.getRegisteredType(id132).orElse(null);
    if (registered132 != null
        && (registered132.nodeClass() != LogOverflowEventTypeNode.class
            || registered132.nodeConstructor() != CONSTRUCTOR_132
            || registered132.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + LogOverflowEventType.TYPE_ID
              + " expected "
              + LogOverflowEventTypeNode.class.getName());
    }
    ids[132] = id132;
    absent[132] = registered132 == null;
    NodeId id133 = ServerNodeSupport.resolve(namespaceTable, LogEntryConditionClassType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered133 =
        manager.getRegisteredType(id133).orElse(null);
    if (registered133 != null
        && (registered133.nodeClass() != LogEntryConditionClassTypeNode.class
            || registered133.nodeConstructor() != CONSTRUCTOR_133
            || registered133.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + LogEntryConditionClassType.TYPE_ID
              + " expected "
              + LogEntryConditionClassTypeNode.class.getName());
    }
    ids[133] = id133;
    absent[133] = registered133 == null;
    NodeId id134 = ServerNodeSupport.resolve(namespaceTable, PubSubDiagnosticsType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered134 =
        manager.getRegisteredType(id134).orElse(null);
    if (registered134 != null
        && (registered134.nodeClass() != PubSubDiagnosticsTypeNode.class
            || registered134.nodeConstructor() != CONSTRUCTOR_134
            || registered134.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + PubSubDiagnosticsType.TYPE_ID
              + " expected "
              + PubSubDiagnosticsTypeNode.class.getName());
    }
    ids[134] = id134;
    absent[134] = registered134 == null;
    NodeId id135 = ServerNodeSupport.resolve(namespaceTable, PubSubDiagnosticsRootType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered135 =
        manager.getRegisteredType(id135).orElse(null);
    if (registered135 != null
        && (registered135.nodeClass() != PubSubDiagnosticsRootTypeNode.class
            || registered135.nodeConstructor() != CONSTRUCTOR_135
            || registered135.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + PubSubDiagnosticsRootType.TYPE_ID
              + " expected "
              + PubSubDiagnosticsRootTypeNode.class.getName());
    }
    ids[135] = id135;
    absent[135] = registered135 == null;
    NodeId id136 =
        ServerNodeSupport.resolve(namespaceTable, PubSubDiagnosticsConnectionType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered136 =
        manager.getRegisteredType(id136).orElse(null);
    if (registered136 != null
        && (registered136.nodeClass() != PubSubDiagnosticsConnectionTypeNode.class
            || registered136.nodeConstructor() != CONSTRUCTOR_136
            || registered136.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + PubSubDiagnosticsConnectionType.TYPE_ID
              + " expected "
              + PubSubDiagnosticsConnectionTypeNode.class.getName());
    }
    ids[136] = id136;
    absent[136] = registered136 == null;
    NodeId id137 = ServerNodeSupport.resolve(namespaceTable, DataTypeRefinementType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered137 =
        manager.getRegisteredType(id137).orElse(null);
    if (registered137 != null
        && (registered137.nodeClass() != DataTypeRefinementTypeNode.class
            || registered137.nodeConstructor() != CONSTRUCTOR_137
            || registered137.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + DataTypeRefinementType.TYPE_ID
              + " expected "
              + DataTypeRefinementTypeNode.class.getName());
    }
    ids[137] = id137;
    absent[137] = registered137 == null;
    NodeId id138 = ServerNodeSupport.resolve(namespaceTable, SubtypeRestrictionType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered138 =
        manager.getRegisteredType(id138).orElse(null);
    if (registered138 != null
        && (registered138.nodeClass() != SubtypeRestrictionTypeNode.class
            || registered138.nodeConstructor() != CONSTRUCTOR_138
            || registered138.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + SubtypeRestrictionType.TYPE_ID
              + " expected "
              + SubtypeRestrictionTypeNode.class.getName());
    }
    ids[138] = id138;
    absent[138] = registered138 == null;
    NodeId id139 = ServerNodeSupport.resolve(namespaceTable, SerializationEntityType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered139 =
        manager.getRegisteredType(id139).orElse(null);
    if (registered139 != null
        && (registered139.nodeClass() != SerializationEntityTypeNode.class
            || registered139.nodeConstructor() != CONSTRUCTOR_139
            || registered139.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + SerializationEntityType.TYPE_ID
              + " expected "
              + SerializationEntityTypeNode.class.getName());
    }
    ids[139] = id139;
    absent[139] = registered139 == null;
    NodeId id140 =
        ServerNodeSupport.resolve(namespaceTable, PubSubDiagnosticsWriterGroupType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered140 =
        manager.getRegisteredType(id140).orElse(null);
    if (registered140 != null
        && (registered140.nodeClass() != PubSubDiagnosticsWriterGroupTypeNode.class
            || registered140.nodeConstructor() != CONSTRUCTOR_140
            || registered140.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + PubSubDiagnosticsWriterGroupType.TYPE_ID
              + " expected "
              + PubSubDiagnosticsWriterGroupTypeNode.class.getName());
    }
    ids[140] = id140;
    absent[140] = registered140 == null;
    NodeId id141 =
        ServerNodeSupport.resolve(namespaceTable, PubSubDiagnosticsReaderGroupType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered141 =
        manager.getRegisteredType(id141).orElse(null);
    if (registered141 != null
        && (registered141.nodeClass() != PubSubDiagnosticsReaderGroupTypeNode.class
            || registered141.nodeConstructor() != CONSTRUCTOR_141
            || registered141.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + PubSubDiagnosticsReaderGroupType.TYPE_ID
              + " expected "
              + PubSubDiagnosticsReaderGroupTypeNode.class.getName());
    }
    ids[141] = id141;
    absent[141] = registered141 == null;
    NodeId id142 =
        ServerNodeSupport.resolve(namespaceTable, PubSubDiagnosticsDataSetWriterType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered142 =
        manager.getRegisteredType(id142).orElse(null);
    if (registered142 != null
        && (registered142.nodeClass() != PubSubDiagnosticsDataSetWriterTypeNode.class
            || registered142.nodeConstructor() != CONSTRUCTOR_142
            || registered142.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + PubSubDiagnosticsDataSetWriterType.TYPE_ID
              + " expected "
              + PubSubDiagnosticsDataSetWriterTypeNode.class.getName());
    }
    ids[142] = id142;
    absent[142] = registered142 == null;
    NodeId id143 =
        ServerNodeSupport.resolve(namespaceTable, PubSubDiagnosticsDataSetReaderType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered143 =
        manager.getRegisteredType(id143).orElse(null);
    if (registered143 != null
        && (registered143.nodeClass() != PubSubDiagnosticsDataSetReaderTypeNode.class
            || registered143.nodeConstructor() != CONSTRUCTOR_143
            || registered143.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + PubSubDiagnosticsDataSetReaderType.TYPE_ID
              + " expected "
              + PubSubDiagnosticsDataSetReaderTypeNode.class.getName());
    }
    ids[143] = id143;
    absent[143] = registered143 == null;
    NodeId id144 = ServerNodeSupport.resolve(namespaceTable, ServerType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered144 =
        manager.getRegisteredType(id144).orElse(null);
    if (registered144 != null
        && (registered144.nodeClass() != ServerTypeNode.class
            || registered144.nodeConstructor() != CONSTRUCTOR_144
            || registered144.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + ServerType.TYPE_ID
              + " expected "
              + ServerTypeNode.class.getName());
    }
    ids[144] = id144;
    absent[144] = registered144 == null;
    NodeId id145 = ServerNodeSupport.resolve(namespaceTable, ServerCapabilitiesType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered145 =
        manager.getRegisteredType(id145).orElse(null);
    if (registered145 != null
        && (registered145.nodeClass() != ServerCapabilitiesTypeNode.class
            || registered145.nodeConstructor() != CONSTRUCTOR_145
            || registered145.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + ServerCapabilitiesType.TYPE_ID
              + " expected "
              + ServerCapabilitiesTypeNode.class.getName());
    }
    ids[145] = id145;
    absent[145] = registered145 == null;
    NodeId id146 = ServerNodeSupport.resolve(namespaceTable, ServerDiagnosticsType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered146 =
        manager.getRegisteredType(id146).orElse(null);
    if (registered146 != null
        && (registered146.nodeClass() != ServerDiagnosticsTypeNode.class
            || registered146.nodeConstructor() != CONSTRUCTOR_146
            || registered146.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + ServerDiagnosticsType.TYPE_ID
              + " expected "
              + ServerDiagnosticsTypeNode.class.getName());
    }
    ids[146] = id146;
    absent[146] = registered146 == null;
    NodeId id147 =
        ServerNodeSupport.resolve(namespaceTable, SessionsDiagnosticsSummaryType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered147 =
        manager.getRegisteredType(id147).orElse(null);
    if (registered147 != null
        && (registered147.nodeClass() != SessionsDiagnosticsSummaryTypeNode.class
            || registered147.nodeConstructor() != CONSTRUCTOR_147
            || registered147.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + SessionsDiagnosticsSummaryType.TYPE_ID
              + " expected "
              + SessionsDiagnosticsSummaryTypeNode.class.getName());
    }
    ids[147] = id147;
    absent[147] = registered147 == null;
    NodeId id148 = ServerNodeSupport.resolve(namespaceTable, SessionDiagnosticsObjectType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered148 =
        manager.getRegisteredType(id148).orElse(null);
    if (registered148 != null
        && (registered148.nodeClass() != SessionDiagnosticsObjectTypeNode.class
            || registered148.nodeConstructor() != CONSTRUCTOR_148
            || registered148.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + SessionDiagnosticsObjectType.TYPE_ID
              + " expected "
              + SessionDiagnosticsObjectTypeNode.class.getName());
    }
    ids[148] = id148;
    absent[148] = registered148 == null;
    NodeId id149 = ServerNodeSupport.resolve(namespaceTable, VendorServerInfoType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered149 =
        manager.getRegisteredType(id149).orElse(null);
    if (registered149 != null
        && (registered149.nodeClass() != VendorServerInfoTypeNode.class
            || registered149.nodeConstructor() != CONSTRUCTOR_149
            || registered149.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + VendorServerInfoType.TYPE_ID
              + " expected "
              + VendorServerInfoTypeNode.class.getName());
    }
    ids[149] = id149;
    absent[149] = registered149 == null;
    NodeId id150 = ServerNodeSupport.resolve(namespaceTable, TransparentRedundancyType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered150 =
        manager.getRegisteredType(id150).orElse(null);
    if (registered150 != null
        && (registered150.nodeClass() != TransparentRedundancyTypeNode.class
            || registered150.nodeConstructor() != CONSTRUCTOR_150
            || registered150.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + TransparentRedundancyType.TYPE_ID
              + " expected "
              + TransparentRedundancyTypeNode.class.getName());
    }
    ids[150] = id150;
    absent[150] = registered150 == null;
    NodeId id151 = ServerNodeSupport.resolve(namespaceTable, AuditSecurityEventType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered151 =
        manager.getRegisteredType(id151).orElse(null);
    if (registered151 != null
        && (registered151.nodeClass() != AuditSecurityEventTypeNode.class
            || registered151.nodeConstructor() != CONSTRUCTOR_151
            || registered151.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + AuditSecurityEventType.TYPE_ID
              + " expected "
              + AuditSecurityEventTypeNode.class.getName());
    }
    ids[151] = id151;
    absent[151] = registered151 == null;
    NodeId id152 = ServerNodeSupport.resolve(namespaceTable, AuditChannelEventType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered152 =
        manager.getRegisteredType(id152).orElse(null);
    if (registered152 != null
        && (registered152.nodeClass() != AuditChannelEventTypeNode.class
            || registered152.nodeConstructor() != CONSTRUCTOR_152
            || registered152.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + AuditChannelEventType.TYPE_ID
              + " expected "
              + AuditChannelEventTypeNode.class.getName());
    }
    ids[152] = id152;
    absent[152] = registered152 == null;
    NodeId id153 =
        ServerNodeSupport.resolve(namespaceTable, AuditOpenSecureChannelEventType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered153 =
        manager.getRegisteredType(id153).orElse(null);
    if (registered153 != null
        && (registered153.nodeClass() != AuditOpenSecureChannelEventTypeNode.class
            || registered153.nodeConstructor() != CONSTRUCTOR_153
            || registered153.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + AuditOpenSecureChannelEventType.TYPE_ID
              + " expected "
              + AuditOpenSecureChannelEventTypeNode.class.getName());
    }
    ids[153] = id153;
    absent[153] = registered153 == null;
    NodeId id154 = ServerNodeSupport.resolve(namespaceTable, AuditSessionEventType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered154 =
        manager.getRegisteredType(id154).orElse(null);
    if (registered154 != null
        && (registered154.nodeClass() != AuditSessionEventTypeNode.class
            || registered154.nodeConstructor() != CONSTRUCTOR_154
            || registered154.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + AuditSessionEventType.TYPE_ID
              + " expected "
              + AuditSessionEventTypeNode.class.getName());
    }
    ids[154] = id154;
    absent[154] = registered154 == null;
    NodeId id155 = ServerNodeSupport.resolve(namespaceTable, AuditCreateSessionEventType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered155 =
        manager.getRegisteredType(id155).orElse(null);
    if (registered155 != null
        && (registered155.nodeClass() != AuditCreateSessionEventTypeNode.class
            || registered155.nodeConstructor() != CONSTRUCTOR_155
            || registered155.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + AuditCreateSessionEventType.TYPE_ID
              + " expected "
              + AuditCreateSessionEventTypeNode.class.getName());
    }
    ids[155] = id155;
    absent[155] = registered155 == null;
    NodeId id156 = ServerNodeSupport.resolve(namespaceTable, AuditActivateSessionEventType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered156 =
        manager.getRegisteredType(id156).orElse(null);
    if (registered156 != null
        && (registered156.nodeClass() != AuditActivateSessionEventTypeNode.class
            || registered156.nodeConstructor() != CONSTRUCTOR_156
            || registered156.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + AuditActivateSessionEventType.TYPE_ID
              + " expected "
              + AuditActivateSessionEventTypeNode.class.getName());
    }
    ids[156] = id156;
    absent[156] = registered156 == null;
    NodeId id157 = ServerNodeSupport.resolve(namespaceTable, AuditCancelEventType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered157 =
        manager.getRegisteredType(id157).orElse(null);
    if (registered157 != null
        && (registered157.nodeClass() != AuditCancelEventTypeNode.class
            || registered157.nodeConstructor() != CONSTRUCTOR_157
            || registered157.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + AuditCancelEventType.TYPE_ID
              + " expected "
              + AuditCancelEventTypeNode.class.getName());
    }
    ids[157] = id157;
    absent[157] = registered157 == null;
    NodeId id158 = ServerNodeSupport.resolve(namespaceTable, AuditCertificateEventType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered158 =
        manager.getRegisteredType(id158).orElse(null);
    if (registered158 != null
        && (registered158.nodeClass() != AuditCertificateEventTypeNode.class
            || registered158.nodeConstructor() != CONSTRUCTOR_158
            || registered158.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + AuditCertificateEventType.TYPE_ID
              + " expected "
              + AuditCertificateEventTypeNode.class.getName());
    }
    ids[158] = id158;
    absent[158] = registered158 == null;
    NodeId id159 =
        ServerNodeSupport.resolve(namespaceTable, AuditCertificateDataMismatchEventType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered159 =
        manager.getRegisteredType(id159).orElse(null);
    if (registered159 != null
        && (registered159.nodeClass() != AuditCertificateDataMismatchEventTypeNode.class
            || registered159.nodeConstructor() != CONSTRUCTOR_159
            || registered159.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + AuditCertificateDataMismatchEventType.TYPE_ID
              + " expected "
              + AuditCertificateDataMismatchEventTypeNode.class.getName());
    }
    ids[159] = id159;
    absent[159] = registered159 == null;
    NodeId id160 =
        ServerNodeSupport.resolve(namespaceTable, AuditCertificateExpiredEventType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered160 =
        manager.getRegisteredType(id160).orElse(null);
    if (registered160 != null
        && (registered160.nodeClass() != AuditCertificateExpiredEventTypeNode.class
            || registered160.nodeConstructor() != CONSTRUCTOR_160
            || registered160.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + AuditCertificateExpiredEventType.TYPE_ID
              + " expected "
              + AuditCertificateExpiredEventTypeNode.class.getName());
    }
    ids[160] = id160;
    absent[160] = registered160 == null;
    NodeId id161 =
        ServerNodeSupport.resolve(namespaceTable, AuditCertificateInvalidEventType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered161 =
        manager.getRegisteredType(id161).orElse(null);
    if (registered161 != null
        && (registered161.nodeClass() != AuditCertificateInvalidEventTypeNode.class
            || registered161.nodeConstructor() != CONSTRUCTOR_161
            || registered161.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + AuditCertificateInvalidEventType.TYPE_ID
              + " expected "
              + AuditCertificateInvalidEventTypeNode.class.getName());
    }
    ids[161] = id161;
    absent[161] = registered161 == null;
    NodeId id162 =
        ServerNodeSupport.resolve(namespaceTable, AuditCertificateUntrustedEventType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered162 =
        manager.getRegisteredType(id162).orElse(null);
    if (registered162 != null
        && (registered162.nodeClass() != AuditCertificateUntrustedEventTypeNode.class
            || registered162.nodeConstructor() != CONSTRUCTOR_162
            || registered162.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + AuditCertificateUntrustedEventType.TYPE_ID
              + " expected "
              + AuditCertificateUntrustedEventTypeNode.class.getName());
    }
    ids[162] = id162;
    absent[162] = registered162 == null;
    NodeId id163 =
        ServerNodeSupport.resolve(namespaceTable, AuditCertificateRevokedEventType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered163 =
        manager.getRegisteredType(id163).orElse(null);
    if (registered163 != null
        && (registered163.nodeClass() != AuditCertificateRevokedEventTypeNode.class
            || registered163.nodeConstructor() != CONSTRUCTOR_163
            || registered163.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + AuditCertificateRevokedEventType.TYPE_ID
              + " expected "
              + AuditCertificateRevokedEventTypeNode.class.getName());
    }
    ids[163] = id163;
    absent[163] = registered163 == null;
    NodeId id164 =
        ServerNodeSupport.resolve(namespaceTable, AuditCertificateMismatchEventType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered164 =
        manager.getRegisteredType(id164).orElse(null);
    if (registered164 != null
        && (registered164.nodeClass() != AuditCertificateMismatchEventTypeNode.class
            || registered164.nodeConstructor() != CONSTRUCTOR_164
            || registered164.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + AuditCertificateMismatchEventType.TYPE_ID
              + " expected "
              + AuditCertificateMismatchEventTypeNode.class.getName());
    }
    ids[164] = id164;
    absent[164] = registered164 == null;
    NodeId id165 = ServerNodeSupport.resolve(namespaceTable, AuditNodeManagementEventType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered165 =
        manager.getRegisteredType(id165).orElse(null);
    if (registered165 != null
        && (registered165.nodeClass() != AuditNodeManagementEventTypeNode.class
            || registered165.nodeConstructor() != CONSTRUCTOR_165
            || registered165.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + AuditNodeManagementEventType.TYPE_ID
              + " expected "
              + AuditNodeManagementEventTypeNode.class.getName());
    }
    ids[165] = id165;
    absent[165] = registered165 == null;
    NodeId id166 = ServerNodeSupport.resolve(namespaceTable, AuditAddNodesEventType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered166 =
        manager.getRegisteredType(id166).orElse(null);
    if (registered166 != null
        && (registered166.nodeClass() != AuditAddNodesEventTypeNode.class
            || registered166.nodeConstructor() != CONSTRUCTOR_166
            || registered166.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + AuditAddNodesEventType.TYPE_ID
              + " expected "
              + AuditAddNodesEventTypeNode.class.getName());
    }
    ids[166] = id166;
    absent[166] = registered166 == null;
    NodeId id167 = ServerNodeSupport.resolve(namespaceTable, AuditDeleteNodesEventType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered167 =
        manager.getRegisteredType(id167).orElse(null);
    if (registered167 != null
        && (registered167.nodeClass() != AuditDeleteNodesEventTypeNode.class
            || registered167.nodeConstructor() != CONSTRUCTOR_167
            || registered167.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + AuditDeleteNodesEventType.TYPE_ID
              + " expected "
              + AuditDeleteNodesEventTypeNode.class.getName());
    }
    ids[167] = id167;
    absent[167] = registered167 == null;
    NodeId id168 = ServerNodeSupport.resolve(namespaceTable, AuditAddReferencesEventType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered168 =
        manager.getRegisteredType(id168).orElse(null);
    if (registered168 != null
        && (registered168.nodeClass() != AuditAddReferencesEventTypeNode.class
            || registered168.nodeConstructor() != CONSTRUCTOR_168
            || registered168.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + AuditAddReferencesEventType.TYPE_ID
              + " expected "
              + AuditAddReferencesEventTypeNode.class.getName());
    }
    ids[168] = id168;
    absent[168] = registered168 == null;
    NodeId id169 =
        ServerNodeSupport.resolve(namespaceTable, AuditDeleteReferencesEventType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered169 =
        manager.getRegisteredType(id169).orElse(null);
    if (registered169 != null
        && (registered169.nodeClass() != AuditDeleteReferencesEventTypeNode.class
            || registered169.nodeConstructor() != CONSTRUCTOR_169
            || registered169.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + AuditDeleteReferencesEventType.TYPE_ID
              + " expected "
              + AuditDeleteReferencesEventTypeNode.class.getName());
    }
    ids[169] = id169;
    absent[169] = registered169 == null;
    NodeId id170 = ServerNodeSupport.resolve(namespaceTable, AuditWriteUpdateEventType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered170 =
        manager.getRegisteredType(id170).orElse(null);
    if (registered170 != null
        && (registered170.nodeClass() != AuditWriteUpdateEventTypeNode.class
            || registered170.nodeConstructor() != CONSTRUCTOR_170
            || registered170.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + AuditWriteUpdateEventType.TYPE_ID
              + " expected "
              + AuditWriteUpdateEventTypeNode.class.getName());
    }
    ids[170] = id170;
    absent[170] = registered170 == null;
    NodeId id171 = ServerNodeSupport.resolve(namespaceTable, ReaderGroupTransportType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered171 =
        manager.getRegisteredType(id171).orElse(null);
    if (registered171 != null
        && (registered171.nodeClass() != ReaderGroupTransportTypeNode.class
            || registered171.nodeConstructor() != CONSTRUCTOR_171
            || registered171.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + ReaderGroupTransportType.TYPE_ID
              + " expected "
              + ReaderGroupTransportTypeNode.class.getName());
    }
    ids[171] = id171;
    absent[171] = registered171 == null;
    NodeId id172 = ServerNodeSupport.resolve(namespaceTable, ReaderGroupMessageType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered172 =
        manager.getRegisteredType(id172).orElse(null);
    if (registered172 != null
        && (registered172.nodeClass() != ReaderGroupMessageTypeNode.class
            || registered172.nodeConstructor() != CONSTRUCTOR_172
            || registered172.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + ReaderGroupMessageType.TYPE_ID
              + " expected "
              + ReaderGroupMessageTypeNode.class.getName());
    }
    ids[172] = id172;
    absent[172] = registered172 == null;
    NodeId id173 = ServerNodeSupport.resolve(namespaceTable, DataSetWriterMessageType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered173 =
        manager.getRegisteredType(id173).orElse(null);
    if (registered173 != null
        && (registered173.nodeClass() != DataSetWriterMessageTypeNode.class
            || registered173.nodeConstructor() != CONSTRUCTOR_173
            || registered173.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + DataSetWriterMessageType.TYPE_ID
              + " expected "
              + DataSetWriterMessageTypeNode.class.getName());
    }
    ids[173] = id173;
    absent[173] = registered173 == null;
    NodeId id174 = ServerNodeSupport.resolve(namespaceTable, DataSetReaderMessageType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered174 =
        manager.getRegisteredType(id174).orElse(null);
    if (registered174 != null
        && (registered174.nodeClass() != DataSetReaderMessageTypeNode.class
            || registered174.nodeConstructor() != CONSTRUCTOR_174
            || registered174.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + DataSetReaderMessageType.TYPE_ID
              + " expected "
              + DataSetReaderMessageTypeNode.class.getName());
    }
    ids[174] = id174;
    absent[174] = registered174 == null;
    NodeId id175 = ServerNodeSupport.resolve(namespaceTable, UadpWriterGroupMessageType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered175 =
        manager.getRegisteredType(id175).orElse(null);
    if (registered175 != null
        && (registered175.nodeClass() != UadpWriterGroupMessageTypeNode.class
            || registered175.nodeConstructor() != CONSTRUCTOR_175
            || registered175.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + UadpWriterGroupMessageType.TYPE_ID
              + " expected "
              + UadpWriterGroupMessageTypeNode.class.getName());
    }
    ids[175] = id175;
    absent[175] = registered175 == null;
    NodeId id176 = ServerNodeSupport.resolve(namespaceTable, UadpDataSetWriterMessageType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered176 =
        manager.getRegisteredType(id176).orElse(null);
    if (registered176 != null
        && (registered176.nodeClass() != UadpDataSetWriterMessageTypeNode.class
            || registered176.nodeConstructor() != CONSTRUCTOR_176
            || registered176.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + UadpDataSetWriterMessageType.TYPE_ID
              + " expected "
              + UadpDataSetWriterMessageTypeNode.class.getName());
    }
    ids[176] = id176;
    absent[176] = registered176 == null;
    NodeId id177 = ServerNodeSupport.resolve(namespaceTable, UadpDataSetReaderMessageType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered177 =
        manager.getRegisteredType(id177).orElse(null);
    if (registered177 != null
        && (registered177.nodeClass() != UadpDataSetReaderMessageTypeNode.class
            || registered177.nodeConstructor() != CONSTRUCTOR_177
            || registered177.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + UadpDataSetReaderMessageType.TYPE_ID
              + " expected "
              + UadpDataSetReaderMessageTypeNode.class.getName());
    }
    ids[177] = id177;
    absent[177] = registered177 == null;
    NodeId id178 = ServerNodeSupport.resolve(namespaceTable, JsonWriterGroupMessageType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered178 =
        manager.getRegisteredType(id178).orElse(null);
    if (registered178 != null
        && (registered178.nodeClass() != JsonWriterGroupMessageTypeNode.class
            || registered178.nodeConstructor() != CONSTRUCTOR_178
            || registered178.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + JsonWriterGroupMessageType.TYPE_ID
              + " expected "
              + JsonWriterGroupMessageTypeNode.class.getName());
    }
    ids[178] = id178;
    absent[178] = registered178 == null;
    NodeId id179 = ServerNodeSupport.resolve(namespaceTable, JsonDataSetWriterMessageType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered179 =
        manager.getRegisteredType(id179).orElse(null);
    if (registered179 != null
        && (registered179.nodeClass() != JsonDataSetWriterMessageTypeNode.class
            || registered179.nodeConstructor() != CONSTRUCTOR_179
            || registered179.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + JsonDataSetWriterMessageType.TYPE_ID
              + " expected "
              + JsonDataSetWriterMessageTypeNode.class.getName());
    }
    ids[179] = id179;
    absent[179] = registered179 == null;
    NodeId id180 = ServerNodeSupport.resolve(namespaceTable, JsonDataSetReaderMessageType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered180 =
        manager.getRegisteredType(id180).orElse(null);
    if (registered180 != null
        && (registered180.nodeClass() != JsonDataSetReaderMessageTypeNode.class
            || registered180.nodeConstructor() != CONSTRUCTOR_180
            || registered180.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + JsonDataSetReaderMessageType.TYPE_ID
              + " expected "
              + JsonDataSetReaderMessageTypeNode.class.getName());
    }
    ids[180] = id180;
    absent[180] = registered180 == null;
    NodeId id181 =
        ServerNodeSupport.resolve(namespaceTable, DatagramWriterGroupTransportType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered181 =
        manager.getRegisteredType(id181).orElse(null);
    if (registered181 != null
        && (registered181.nodeClass() != DatagramWriterGroupTransportTypeNode.class
            || registered181.nodeConstructor() != CONSTRUCTOR_181
            || registered181.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + DatagramWriterGroupTransportType.TYPE_ID
              + " expected "
              + DatagramWriterGroupTransportTypeNode.class.getName());
    }
    ids[181] = id181;
    absent[181] = registered181 == null;
    NodeId id182 =
        ServerNodeSupport.resolve(namespaceTable, BrokerWriterGroupTransportType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered182 =
        manager.getRegisteredType(id182).orElse(null);
    if (registered182 != null
        && (registered182.nodeClass() != BrokerWriterGroupTransportTypeNode.class
            || registered182.nodeConstructor() != CONSTRUCTOR_182
            || registered182.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + BrokerWriterGroupTransportType.TYPE_ID
              + " expected "
              + BrokerWriterGroupTransportTypeNode.class.getName());
    }
    ids[182] = id182;
    absent[182] = registered182 == null;
    NodeId id183 =
        ServerNodeSupport.resolve(namespaceTable, BrokerDataSetWriterTransportType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered183 =
        manager.getRegisteredType(id183).orElse(null);
    if (registered183 != null
        && (registered183.nodeClass() != BrokerDataSetWriterTransportTypeNode.class
            || registered183.nodeConstructor() != CONSTRUCTOR_183
            || registered183.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + BrokerDataSetWriterTransportType.TYPE_ID
              + " expected "
              + BrokerDataSetWriterTransportTypeNode.class.getName());
    }
    ids[183] = id183;
    absent[183] = registered183 == null;
    NodeId id184 =
        ServerNodeSupport.resolve(namespaceTable, BrokerDataSetReaderTransportType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered184 =
        manager.getRegisteredType(id184).orElse(null);
    if (registered184 != null
        && (registered184.nodeClass() != BrokerDataSetReaderTransportTypeNode.class
            || registered184.nodeConstructor() != CONSTRUCTOR_184
            || registered184.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + BrokerDataSetReaderTransportType.TYPE_ID
              + " expected "
              + BrokerDataSetReaderTransportTypeNode.class.getName());
    }
    ids[184] = id184;
    absent[184] = registered184 == null;
    NodeId id185 = ServerNodeSupport.resolve(namespaceTable, NetworkAddressType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered185 =
        manager.getRegisteredType(id185).orElse(null);
    if (registered185 != null
        && (registered185.nodeClass() != NetworkAddressTypeNode.class
            || registered185.nodeConstructor() != CONSTRUCTOR_185
            || registered185.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + NetworkAddressType.TYPE_ID
              + " expected "
              + NetworkAddressTypeNode.class.getName());
    }
    ids[185] = id185;
    absent[185] = registered185 == null;
    NodeId id186 = ServerNodeSupport.resolve(namespaceTable, NetworkAddressUrlType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered186 =
        manager.getRegisteredType(id186).orElse(null);
    if (registered186 != null
        && (registered186.nodeClass() != NetworkAddressUrlTypeNode.class
            || registered186.nodeConstructor() != CONSTRUCTOR_186
            || registered186.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + NetworkAddressUrlType.TYPE_ID
              + " expected "
              + NetworkAddressUrlTypeNode.class.getName());
    }
    ids[186] = id186;
    absent[186] = registered186 == null;
    NodeId id187 = ServerNodeSupport.resolve(namespaceTable, DeviceFailureEventType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered187 =
        manager.getRegisteredType(id187).orElse(null);
    if (registered187 != null
        && (registered187.nodeClass() != DeviceFailureEventTypeNode.class
            || registered187.nodeConstructor() != CONSTRUCTOR_187
            || registered187.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + DeviceFailureEventType.TYPE_ID
              + " expected "
              + DeviceFailureEventTypeNode.class.getName());
    }
    ids[187] = id187;
    absent[187] = registered187 == null;
    NodeId id188 = ServerNodeSupport.resolve(namespaceTable, BaseModelChangeEventType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered188 =
        manager.getRegisteredType(id188).orElse(null);
    if (registered188 != null
        && (registered188.nodeClass() != BaseModelChangeEventTypeNode.class
            || registered188.nodeConstructor() != CONSTRUCTOR_188
            || registered188.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + BaseModelChangeEventType.TYPE_ID
              + " expected "
              + BaseModelChangeEventTypeNode.class.getName());
    }
    ids[188] = id188;
    absent[188] = registered188 == null;
    NodeId id189 = ServerNodeSupport.resolve(namespaceTable, GeneralModelChangeEventType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered189 =
        manager.getRegisteredType(id189).orElse(null);
    if (registered189 != null
        && (registered189.nodeClass() != GeneralModelChangeEventTypeNode.class
            || registered189.nodeConstructor() != CONSTRUCTOR_189
            || registered189.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + GeneralModelChangeEventType.TYPE_ID
              + " expected "
              + GeneralModelChangeEventTypeNode.class.getName());
    }
    ids[189] = id189;
    absent[189] = registered189 == null;
    NodeId id190 = ServerNodeSupport.resolve(namespaceTable, InitialStateType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered190 =
        manager.getRegisteredType(id190).orElse(null);
    if (registered190 != null
        && (registered190.nodeClass() != InitialStateTypeNode.class
            || registered190.nodeConstructor() != CONSTRUCTOR_190
            || registered190.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + InitialStateType.TYPE_ID
              + " expected "
              + InitialStateTypeNode.class.getName());
    }
    ids[190] = id190;
    absent[190] = registered190 == null;
    NodeId id191 = ServerNodeSupport.resolve(namespaceTable, TransitionType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered191 =
        manager.getRegisteredType(id191).orElse(null);
    if (registered191 != null
        && (registered191.nodeClass() != TransitionTypeNode.class
            || registered191.nodeConstructor() != CONSTRUCTOR_191
            || registered191.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + TransitionType.TYPE_ID
              + " expected "
              + TransitionTypeNode.class.getName());
    }
    ids[191] = id191;
    absent[191] = registered191 == null;
    NodeId id192 = ServerNodeSupport.resolve(namespaceTable, TransitionEventType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered192 =
        manager.getRegisteredType(id192).orElse(null);
    if (registered192 != null
        && (registered192.nodeClass() != TransitionEventTypeNode.class
            || registered192.nodeConstructor() != CONSTRUCTOR_192
            || registered192.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + TransitionEventType.TYPE_ID
              + " expected "
              + TransitionEventTypeNode.class.getName());
    }
    ids[192] = id192;
    absent[192] = registered192 == null;
    NodeId id193 =
        ServerNodeSupport.resolve(namespaceTable, HistoricalDataConfigurationType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered193 =
        manager.getRegisteredType(id193).orElse(null);
    if (registered193 != null
        && (registered193.nodeClass() != HistoricalDataConfigurationTypeNode.class
            || registered193.nodeConstructor() != CONSTRUCTOR_193
            || registered193.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + HistoricalDataConfigurationType.TYPE_ID
              + " expected "
              + HistoricalDataConfigurationTypeNode.class.getName());
    }
    ids[193] = id193;
    absent[193] = registered193 == null;
    NodeId id194 = ServerNodeSupport.resolve(namespaceTable, HistoryServerCapabilitiesType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered194 =
        manager.getRegisteredType(id194).orElse(null);
    if (registered194 != null
        && (registered194.nodeClass() != HistoryServerCapabilitiesTypeNode.class
            || registered194.nodeConstructor() != CONSTRUCTOR_194
            || registered194.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + HistoryServerCapabilitiesType.TYPE_ID
              + " expected "
              + HistoryServerCapabilitiesTypeNode.class.getName());
    }
    ids[194] = id194;
    absent[194] = registered194 == null;
    NodeId id195 = ServerNodeSupport.resolve(namespaceTable, AggregateFunctionType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered195 =
        manager.getRegisteredType(id195).orElse(null);
    if (registered195 != null
        && (registered195.nodeClass() != AggregateFunctionTypeNode.class
            || registered195.nodeConstructor() != CONSTRUCTOR_195
            || registered195.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + AggregateFunctionType.TYPE_ID
              + " expected "
              + AggregateFunctionTypeNode.class.getName());
    }
    ids[195] = id195;
    absent[195] = registered195 == null;
    NodeId id196 = ServerNodeSupport.resolve(namespaceTable, AliasNameType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered196 =
        manager.getRegisteredType(id196).orElse(null);
    if (registered196 != null
        && (registered196.nodeClass() != AliasNameTypeNode.class
            || registered196.nodeConstructor() != CONSTRUCTOR_196
            || registered196.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + AliasNameType.TYPE_ID
              + " expected "
              + AliasNameTypeNode.class.getName());
    }
    ids[196] = id196;
    absent[196] = registered196 == null;
    NodeId id197 = ServerNodeSupport.resolve(namespaceTable, AliasNameCategoryType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered197 =
        manager.getRegisteredType(id197).orElse(null);
    if (registered197 != null
        && (registered197.nodeClass() != AliasNameCategoryTypeNode.class
            || registered197.nodeConstructor() != CONSTRUCTOR_197
            || registered197.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + AliasNameCategoryType.TYPE_ID
              + " expected "
              + AliasNameCategoryTypeNode.class.getName());
    }
    ids[197] = id197;
    absent[197] = registered197 == null;
    NodeId id198 = ServerNodeSupport.resolve(namespaceTable, IOrderedObjectType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered198 =
        manager.getRegisteredType(id198).orElse(null);
    if (registered198 != null
        && (registered198.nodeClass() != IOrderedObjectTypeNode.class
            || registered198.nodeConstructor() != CONSTRUCTOR_198
            || registered198.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + IOrderedObjectType.TYPE_ID
              + " expected "
              + IOrderedObjectTypeNode.class.getName());
    }
    ids[198] = id198;
    absent[198] = registered198 == null;
    NodeId id199 = ServerNodeSupport.resolve(namespaceTable, OrderedListType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered199 =
        manager.getRegisteredType(id199).orElse(null);
    if (registered199 != null
        && (registered199.nodeClass() != OrderedListTypeNode.class
            || registered199.nodeConstructor() != CONSTRUCTOR_199
            || registered199.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + OrderedListType.TYPE_ID
              + " expected "
              + OrderedListTypeNode.class.getName());
    }
    ids[199] = id199;
    absent[199] = registered199 == null;
    NodeId id200 = ServerNodeSupport.resolve(namespaceTable, EccApplicationCertificateType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered200 =
        manager.getRegisteredType(id200).orElse(null);
    if (registered200 != null
        && (registered200.nodeClass() != EccApplicationCertificateTypeNode.class
            || registered200.nodeConstructor() != CONSTRUCTOR_200
            || registered200.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + EccApplicationCertificateType.TYPE_ID
              + " expected "
              + EccApplicationCertificateTypeNode.class.getName());
    }
    ids[200] = id200;
    absent[200] = registered200 == null;
    NodeId id201 =
        ServerNodeSupport.resolve(namespaceTable, EccNistP256ApplicationCertificateType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered201 =
        manager.getRegisteredType(id201).orElse(null);
    if (registered201 != null
        && (registered201.nodeClass() != EccNistP256ApplicationCertificateTypeNode.class
            || registered201.nodeConstructor() != CONSTRUCTOR_201
            || registered201.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + EccNistP256ApplicationCertificateType.TYPE_ID
              + " expected "
              + EccNistP256ApplicationCertificateTypeNode.class.getName());
    }
    ids[201] = id201;
    absent[201] = registered201 == null;
    NodeId id202 =
        ServerNodeSupport.resolve(namespaceTable, EccNistP384ApplicationCertificateType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered202 =
        manager.getRegisteredType(id202).orElse(null);
    if (registered202 != null
        && (registered202.nodeClass() != EccNistP384ApplicationCertificateTypeNode.class
            || registered202.nodeConstructor() != CONSTRUCTOR_202
            || registered202.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + EccNistP384ApplicationCertificateType.TYPE_ID
              + " expected "
              + EccNistP384ApplicationCertificateTypeNode.class.getName());
    }
    ids[202] = id202;
    absent[202] = registered202 == null;
    NodeId id203 =
        ServerNodeSupport.resolve(
            namespaceTable, EccBrainpoolP256r1ApplicationCertificateType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered203 =
        manager.getRegisteredType(id203).orElse(null);
    if (registered203 != null
        && (registered203.nodeClass() != EccBrainpoolP256r1ApplicationCertificateTypeNode.class
            || registered203.nodeConstructor() != CONSTRUCTOR_203
            || registered203.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + EccBrainpoolP256r1ApplicationCertificateType.TYPE_ID
              + " expected "
              + EccBrainpoolP256r1ApplicationCertificateTypeNode.class.getName());
    }
    ids[203] = id203;
    absent[203] = registered203 == null;
    NodeId id204 =
        ServerNodeSupport.resolve(
            namespaceTable, EccBrainpoolP384r1ApplicationCertificateType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered204 =
        manager.getRegisteredType(id204).orElse(null);
    if (registered204 != null
        && (registered204.nodeClass() != EccBrainpoolP384r1ApplicationCertificateTypeNode.class
            || registered204.nodeConstructor() != CONSTRUCTOR_204
            || registered204.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + EccBrainpoolP384r1ApplicationCertificateType.TYPE_ID
              + " expected "
              + EccBrainpoolP384r1ApplicationCertificateTypeNode.class.getName());
    }
    ids[204] = id204;
    absent[204] = registered204 == null;
    NodeId id205 =
        ServerNodeSupport.resolve(namespaceTable, EccCurve25519ApplicationCertificateType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered205 =
        manager.getRegisteredType(id205).orElse(null);
    if (registered205 != null
        && (registered205.nodeClass() != EccCurve25519ApplicationCertificateTypeNode.class
            || registered205.nodeConstructor() != CONSTRUCTOR_205
            || registered205.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + EccCurve25519ApplicationCertificateType.TYPE_ID
              + " expected "
              + EccCurve25519ApplicationCertificateTypeNode.class.getName());
    }
    ids[205] = id205;
    absent[205] = registered205 == null;
    NodeId id206 =
        ServerNodeSupport.resolve(namespaceTable, EccCurve448ApplicationCertificateType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered206 =
        manager.getRegisteredType(id206).orElse(null);
    if (registered206 != null
        && (registered206.nodeClass() != EccCurve448ApplicationCertificateTypeNode.class
            || registered206.nodeConstructor() != CONSTRUCTOR_206
            || registered206.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + EccCurve448ApplicationCertificateType.TYPE_ID
              + " expected "
              + EccCurve448ApplicationCertificateTypeNode.class.getName());
    }
    ids[206] = id206;
    absent[206] = registered206 == null;
    NodeId id207 =
        ServerNodeSupport.resolve(
            namespaceTable, AuthorizationServicesConfigurationFolderType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered207 =
        manager.getRegisteredType(id207).orElse(null);
    if (registered207 != null
        && (registered207.nodeClass() != AuthorizationServicesConfigurationFolderTypeNode.class
            || registered207.nodeConstructor() != CONSTRUCTOR_207
            || registered207.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + AuthorizationServicesConfigurationFolderType.TYPE_ID
              + " expected "
              + AuthorizationServicesConfigurationFolderTypeNode.class.getName());
    }
    ids[207] = id207;
    absent[207] = registered207 == null;
    NodeId id208 = ServerNodeSupport.resolve(namespaceTable, AuditClientEventType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered208 =
        manager.getRegisteredType(id208).orElse(null);
    if (registered208 != null
        && (registered208.nodeClass() != AuditClientEventTypeNode.class
            || registered208.nodeConstructor() != CONSTRUCTOR_208
            || registered208.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + AuditClientEventType.TYPE_ID
              + " expected "
              + AuditClientEventTypeNode.class.getName());
    }
    ids[208] = id208;
    absent[208] = registered208 == null;
    NodeId id209 = ServerNodeSupport.resolve(namespaceTable, ProgramTransitionEventType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered209 =
        manager.getRegisteredType(id209).orElse(null);
    if (registered209 != null
        && (registered209.nodeClass() != ProgramTransitionEventTypeNode.class
            || registered209.nodeConstructor() != CONSTRUCTOR_209
            || registered209.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + ProgramTransitionEventType.TYPE_ID
              + " expected "
              + ProgramTransitionEventTypeNode.class.getName());
    }
    ids[209] = id209;
    absent[209] = registered209 == null;
    NodeId id210 = ServerNodeSupport.resolve(namespaceTable, SubscribedDataSetFolderType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered210 =
        manager.getRegisteredType(id210).orElse(null);
    if (registered210 != null
        && (registered210.nodeClass() != SubscribedDataSetFolderTypeNode.class
            || registered210.nodeConstructor() != CONSTRUCTOR_210
            || registered210.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + SubscribedDataSetFolderType.TYPE_ID
              + " expected "
              + SubscribedDataSetFolderTypeNode.class.getName());
    }
    ids[210] = id210;
    absent[210] = registered210 == null;
    NodeId id211 =
        ServerNodeSupport.resolve(namespaceTable, StandaloneSubscribedDataSetType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered211 =
        manager.getRegisteredType(id211).orElse(null);
    if (registered211 != null
        && (registered211.nodeClass() != StandaloneSubscribedDataSetTypeNode.class
            || registered211.nodeConstructor() != CONSTRUCTOR_211
            || registered211.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + StandaloneSubscribedDataSetType.TYPE_ID
              + " expected "
              + StandaloneSubscribedDataSetTypeNode.class.getName());
    }
    ids[211] = id211;
    absent[211] = registered211 == null;
    NodeId id212 = ServerNodeSupport.resolve(namespaceTable, PubSubCapabilitiesType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered212 =
        manager.getRegisteredType(id212).orElse(null);
    if (registered212 != null
        && (registered212.nodeClass() != PubSubCapabilitiesTypeNode.class
            || registered212.nodeConstructor() != CONSTRUCTOR_212
            || registered212.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + PubSubCapabilitiesType.TYPE_ID
              + " expected "
              + PubSubCapabilitiesTypeNode.class.getName());
    }
    ids[212] = id212;
    absent[212] = registered212 == null;
    NodeId id213 = ServerNodeSupport.resolve(namespaceTable, ProgramStateMachineType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered213 =
        manager.getRegisteredType(id213).orElse(null);
    if (registered213 != null
        && (registered213.nodeClass() != ProgramStateMachineTypeNode.class
            || registered213.nodeConstructor() != CONSTRUCTOR_213
            || registered213.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + ProgramStateMachineType.TYPE_ID
              + " expected "
              + ProgramStateMachineTypeNode.class.getName());
    }
    ids[213] = id213;
    absent[213] = registered213 == null;
    NodeId id214 =
        ServerNodeSupport.resolve(namespaceTable, AuditClientUpdateMethodResultEventType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered214 =
        manager.getRegisteredType(id214).orElse(null);
    if (registered214 != null
        && (registered214.nodeClass() != AuditClientUpdateMethodResultEventTypeNode.class
            || registered214.nodeConstructor() != CONSTRUCTOR_214
            || registered214.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + AuditClientUpdateMethodResultEventType.TYPE_ID
              + " expected "
              + AuditClientUpdateMethodResultEventTypeNode.class.getName());
    }
    ids[214] = id214;
    absent[214] = registered214 == null;
    NodeId id215 =
        ServerNodeSupport.resolve(namespaceTable, DatagramDataSetReaderTransportType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered215 =
        manager.getRegisteredType(id215).orElse(null);
    if (registered215 != null
        && (registered215.nodeClass() != DatagramDataSetReaderTransportTypeNode.class
            || registered215.nodeConstructor() != CONSTRUCTOR_215
            || registered215.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + DatagramDataSetReaderTransportType.TYPE_ID
              + " expected "
              + DatagramDataSetReaderTransportTypeNode.class.getName());
    }
    ids[215] = id215;
    absent[215] = registered215 == null;
    NodeId id216 = ServerNodeSupport.resolve(namespaceTable, IIetfBaseNetworkInterfaceType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered216 =
        manager.getRegisteredType(id216).orElse(null);
    if (registered216 != null
        && (registered216.nodeClass() != IIetfBaseNetworkInterfaceTypeNode.class
            || registered216.nodeConstructor() != CONSTRUCTOR_216
            || registered216.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + IIetfBaseNetworkInterfaceType.TYPE_ID
              + " expected "
              + IIetfBaseNetworkInterfaceTypeNode.class.getName());
    }
    ids[216] = id216;
    absent[216] = registered216 == null;
    NodeId id217 = ServerNodeSupport.resolve(namespaceTable, IIeeeBaseEthernetPortType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered217 =
        manager.getRegisteredType(id217).orElse(null);
    if (registered217 != null
        && (registered217.nodeClass() != IIeeeBaseEthernetPortTypeNode.class
            || registered217.nodeConstructor() != CONSTRUCTOR_217
            || registered217.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + IIeeeBaseEthernetPortType.TYPE_ID
              + " expected "
              + IIeeeBaseEthernetPortTypeNode.class.getName());
    }
    ids[217] = id217;
    absent[217] = registered217 == null;
    NodeId id218 = ServerNodeSupport.resolve(namespaceTable, IBaseEthernetCapabilitiesType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered218 =
        manager.getRegisteredType(id218).orElse(null);
    if (registered218 != null
        && (registered218.nodeClass() != IBaseEthernetCapabilitiesTypeNode.class
            || registered218.nodeConstructor() != CONSTRUCTOR_218
            || registered218.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + IBaseEthernetCapabilitiesType.TYPE_ID
              + " expected "
              + IBaseEthernetCapabilitiesTypeNode.class.getName());
    }
    ids[218] = id218;
    absent[218] = registered218 == null;
    NodeId id219 = ServerNodeSupport.resolve(namespaceTable, ISrClassType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered219 =
        manager.getRegisteredType(id219).orElse(null);
    if (registered219 != null
        && (registered219.nodeClass() != ISrClassTypeNode.class
            || registered219.nodeConstructor() != CONSTRUCTOR_219
            || registered219.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + ISrClassType.TYPE_ID
              + " expected "
              + ISrClassTypeNode.class.getName());
    }
    ids[219] = id219;
    absent[219] = registered219 == null;
    NodeId id220 = ServerNodeSupport.resolve(namespaceTable, IIeeeBaseTsnStreamType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered220 =
        manager.getRegisteredType(id220).orElse(null);
    if (registered220 != null
        && (registered220.nodeClass() != IIeeeBaseTsnStreamTypeNode.class
            || registered220.nodeConstructor() != CONSTRUCTOR_220
            || registered220.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + IIeeeBaseTsnStreamType.TYPE_ID
              + " expected "
              + IIeeeBaseTsnStreamTypeNode.class.getName());
    }
    ids[220] = id220;
    absent[220] = registered220 == null;
    NodeId id221 =
        ServerNodeSupport.resolve(namespaceTable, IIeeeBaseTsnTrafficSpecificationType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered221 =
        manager.getRegisteredType(id221).orElse(null);
    if (registered221 != null
        && (registered221.nodeClass() != IIeeeBaseTsnTrafficSpecificationTypeNode.class
            || registered221.nodeConstructor() != CONSTRUCTOR_221
            || registered221.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + IIeeeBaseTsnTrafficSpecificationType.TYPE_ID
              + " expected "
              + IIeeeBaseTsnTrafficSpecificationTypeNode.class.getName());
    }
    ids[221] = id221;
    absent[221] = registered221 == null;
    NodeId id222 = ServerNodeSupport.resolve(namespaceTable, IIeeeBaseTsnStatusStreamType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered222 =
        manager.getRegisteredType(id222).orElse(null);
    if (registered222 != null
        && (registered222.nodeClass() != IIeeeBaseTsnStatusStreamTypeNode.class
            || registered222.nodeConstructor() != CONSTRUCTOR_222
            || registered222.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + IIeeeBaseTsnStatusStreamType.TYPE_ID
              + " expected "
              + IIeeeBaseTsnStatusStreamTypeNode.class.getName());
    }
    ids[222] = id222;
    absent[222] = registered222 == null;
    NodeId id223 =
        ServerNodeSupport.resolve(namespaceTable, IIeeeTsnInterfaceConfigurationType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered223 =
        manager.getRegisteredType(id223).orElse(null);
    if (registered223 != null
        && (registered223.nodeClass() != IIeeeTsnInterfaceConfigurationTypeNode.class
            || registered223.nodeConstructor() != CONSTRUCTOR_223
            || registered223.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + IIeeeTsnInterfaceConfigurationType.TYPE_ID
              + " expected "
              + IIeeeTsnInterfaceConfigurationTypeNode.class.getName());
    }
    ids[223] = id223;
    absent[223] = registered223 == null;
    NodeId id224 =
        ServerNodeSupport.resolve(namespaceTable, IIeeeTsnInterfaceConfigurationTalkerType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered224 =
        manager.getRegisteredType(id224).orElse(null);
    if (registered224 != null
        && (registered224.nodeClass() != IIeeeTsnInterfaceConfigurationTalkerTypeNode.class
            || registered224.nodeConstructor() != CONSTRUCTOR_224
            || registered224.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + IIeeeTsnInterfaceConfigurationTalkerType.TYPE_ID
              + " expected "
              + IIeeeTsnInterfaceConfigurationTalkerTypeNode.class.getName());
    }
    ids[224] = id224;
    absent[224] = registered224 == null;
    NodeId id225 =
        ServerNodeSupport.resolve(
            namespaceTable, IIeeeTsnInterfaceConfigurationListenerType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered225 =
        manager.getRegisteredType(id225).orElse(null);
    if (registered225 != null
        && (registered225.nodeClass() != IIeeeTsnInterfaceConfigurationListenerTypeNode.class
            || registered225.nodeConstructor() != CONSTRUCTOR_225
            || registered225.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + IIeeeTsnInterfaceConfigurationListenerType.TYPE_ID
              + " expected "
              + IIeeeTsnInterfaceConfigurationListenerTypeNode.class.getName());
    }
    ids[225] = id225;
    absent[225] = registered225 == null;
    NodeId id226 = ServerNodeSupport.resolve(namespaceTable, IIeeeTsnMacAddressType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered226 =
        manager.getRegisteredType(id226).orElse(null);
    if (registered226 != null
        && (registered226.nodeClass() != IIeeeTsnMacAddressTypeNode.class
            || registered226.nodeConstructor() != CONSTRUCTOR_226
            || registered226.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + IIeeeTsnMacAddressType.TYPE_ID
              + " expected "
              + IIeeeTsnMacAddressTypeNode.class.getName());
    }
    ids[226] = id226;
    absent[226] = registered226 == null;
    NodeId id227 = ServerNodeSupport.resolve(namespaceTable, IIeeeTsnVlanTagType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered227 =
        manager.getRegisteredType(id227).orElse(null);
    if (registered227 != null
        && (registered227.nodeClass() != IIeeeTsnVlanTagTypeNode.class
            || registered227.nodeConstructor() != CONSTRUCTOR_227
            || registered227.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + IIeeeTsnVlanTagType.TYPE_ID
              + " expected "
              + IIeeeTsnVlanTagTypeNode.class.getName());
    }
    ids[227] = id227;
    absent[227] = registered227 == null;
    NodeId id228 = ServerNodeSupport.resolve(namespaceTable, IPriorityMappingEntryType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered228 =
        manager.getRegisteredType(id228).orElse(null);
    if (registered228 != null
        && (registered228.nodeClass() != IPriorityMappingEntryTypeNode.class
            || registered228.nodeConstructor() != CONSTRUCTOR_228
            || registered228.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + IPriorityMappingEntryType.TYPE_ID
              + " expected "
              + IPriorityMappingEntryTypeNode.class.getName());
    }
    ids[228] = id228;
    absent[228] = registered228 == null;
    NodeId id229 =
        ServerNodeSupport.resolve(namespaceTable, IIeeeAutoNegotiationStatusType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered229 =
        manager.getRegisteredType(id229).orElse(null);
    if (registered229 != null
        && (registered229.nodeClass() != IIeeeAutoNegotiationStatusTypeNode.class
            || registered229.nodeConstructor() != CONSTRUCTOR_229
            || registered229.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + IIeeeAutoNegotiationStatusType.TYPE_ID
              + " expected "
              + IIeeeAutoNegotiationStatusTypeNode.class.getName());
    }
    ids[229] = id229;
    absent[229] = registered229 == null;
    NodeId id230 = ServerNodeSupport.resolve(namespaceTable, UserManagementType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered230 =
        manager.getRegisteredType(id230).orElse(null);
    if (registered230 != null
        && (registered230.nodeClass() != UserManagementTypeNode.class
            || registered230.nodeConstructor() != CONSTRUCTOR_230
            || registered230.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + UserManagementType.TYPE_ID
              + " expected "
              + UserManagementTypeNode.class.getName());
    }
    ids[230] = id230;
    absent[230] = registered230 == null;
    NodeId id231 = ServerNodeSupport.resolve(namespaceTable, IVlanIdType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered231 =
        manager.getRegisteredType(id231).orElse(null);
    if (registered231 != null
        && (registered231.nodeClass() != IVlanIdTypeNode.class
            || registered231.nodeConstructor() != CONSTRUCTOR_231
            || registered231.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + IVlanIdType.TYPE_ID
              + " expected "
              + IVlanIdTypeNode.class.getName());
    }
    ids[231] = id231;
    absent[231] = registered231 == null;
    NodeId id232 = ServerNodeSupport.resolve(namespaceTable, IetfBaseNetworkInterfaceType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered232 =
        manager.getRegisteredType(id232).orElse(null);
    if (registered232 != null
        && (registered232.nodeClass() != IetfBaseNetworkInterfaceTypeNode.class
            || registered232.nodeConstructor() != CONSTRUCTOR_232
            || registered232.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + IetfBaseNetworkInterfaceType.TYPE_ID
              + " expected "
              + IetfBaseNetworkInterfaceTypeNode.class.getName());
    }
    ids[232] = id232;
    absent[232] = registered232 == null;
    NodeId id233 = ServerNodeSupport.resolve(namespaceTable, PriorityMappingTableType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered233 =
        manager.getRegisteredType(id233).orElse(null);
    if (registered233 != null
        && (registered233.nodeClass() != PriorityMappingTableTypeNode.class
            || registered233.nodeConstructor() != CONSTRUCTOR_233
            || registered233.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + PriorityMappingTableType.TYPE_ID
              + " expected "
              + PriorityMappingTableTypeNode.class.getName());
    }
    ids[233] = id233;
    absent[233] = registered233 == null;
    NodeId id234 = ServerNodeSupport.resolve(namespaceTable, PubSubKeyPushTargetType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered234 =
        manager.getRegisteredType(id234).orElse(null);
    if (registered234 != null
        && (registered234.nodeClass() != PubSubKeyPushTargetTypeNode.class
            || registered234.nodeConstructor() != CONSTRUCTOR_234
            || registered234.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + PubSubKeyPushTargetType.TYPE_ID
              + " expected "
              + PubSubKeyPushTargetTypeNode.class.getName());
    }
    ids[234] = id234;
    absent[234] = registered234 == null;
    NodeId id235 = ServerNodeSupport.resolve(namespaceTable, PubSubKeyPushTargetFolderType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered235 =
        manager.getRegisteredType(id235).orElse(null);
    if (registered235 != null
        && (registered235.nodeClass() != PubSubKeyPushTargetFolderTypeNode.class
            || registered235.nodeConstructor() != CONSTRUCTOR_235
            || registered235.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + PubSubKeyPushTargetFolderType.TYPE_ID
              + " expected "
              + PubSubKeyPushTargetFolderTypeNode.class.getName());
    }
    ids[235] = id235;
    absent[235] = registered235 == null;
    NodeId id236 = ServerNodeSupport.resolve(namespaceTable, PubSubConfigurationType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered236 =
        manager.getRegisteredType(id236).orElse(null);
    if (registered236 != null
        && (registered236.nodeClass() != PubSubConfigurationTypeNode.class
            || registered236.nodeConstructor() != CONSTRUCTOR_236
            || registered236.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + PubSubConfigurationType.TYPE_ID
              + " expected "
              + PubSubConfigurationTypeNode.class.getName());
    }
    ids[236] = id236;
    absent[236] = registered236 == null;
    NodeId id237 = ServerNodeSupport.resolve(namespaceTable, ApplicationConfigurationType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered237 =
        manager.getRegisteredType(id237).orElse(null);
    if (registered237 != null
        && (registered237.nodeClass() != ApplicationConfigurationTypeNode.class
            || registered237.nodeConstructor() != CONSTRUCTOR_237
            || registered237.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + ApplicationConfigurationType.TYPE_ID
              + " expected "
              + ApplicationConfigurationTypeNode.class.getName());
    }
    ids[237] = id237;
    absent[237] = registered237 == null;
    NodeId id238 = ServerNodeSupport.resolve(namespaceTable, ProvisionableDeviceType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered238 =
        manager.getRegisteredType(id238).orElse(null);
    if (registered238 != null
        && (registered238.nodeClass() != ProvisionableDeviceTypeNode.class
            || registered238.nodeConstructor() != CONSTRUCTOR_238
            || registered238.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + ProvisionableDeviceType.TYPE_ID
              + " expected "
              + ProvisionableDeviceTypeNode.class.getName());
    }
    ids[238] = id238;
    absent[238] = registered238 == null;
    NodeId id239 = ServerNodeSupport.resolve(namespaceTable, SemanticChangeEventType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered239 =
        manager.getRegisteredType(id239).orElse(null);
    if (registered239 != null
        && (registered239.nodeClass() != SemanticChangeEventTypeNode.class
            || registered239.nodeConstructor() != CONSTRUCTOR_239
            || registered239.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + SemanticChangeEventType.TYPE_ID
              + " expected "
              + SemanticChangeEventTypeNode.class.getName());
    }
    ids[239] = id239;
    absent[239] = registered239 == null;
    NodeId id240 = ServerNodeSupport.resolve(namespaceTable, AuditUrlMismatchEventType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered240 =
        manager.getRegisteredType(id240).orElse(null);
    if (registered240 != null
        && (registered240.nodeClass() != AuditUrlMismatchEventTypeNode.class
            || registered240.nodeConstructor() != CONSTRUCTOR_240
            || registered240.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + AuditUrlMismatchEventType.TYPE_ID
              + " expected "
              + AuditUrlMismatchEventTypeNode.class.getName());
    }
    ids[240] = id240;
    absent[240] = registered240 == null;
    NodeId id241 = ServerNodeSupport.resolve(namespaceTable, RefreshStartEventType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered241 =
        manager.getRegisteredType(id241).orElse(null);
    if (registered241 != null
        && (registered241.nodeClass() != RefreshStartEventTypeNode.class
            || registered241.nodeConstructor() != CONSTRUCTOR_241
            || registered241.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + RefreshStartEventType.TYPE_ID
              + " expected "
              + RefreshStartEventTypeNode.class.getName());
    }
    ids[241] = id241;
    absent[241] = registered241 == null;
    NodeId id242 = ServerNodeSupport.resolve(namespaceTable, RefreshEndEventType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered242 =
        manager.getRegisteredType(id242).orElse(null);
    if (registered242 != null
        && (registered242.nodeClass() != RefreshEndEventTypeNode.class
            || registered242.nodeConstructor() != CONSTRUCTOR_242
            || registered242.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + RefreshEndEventType.TYPE_ID
              + " expected "
              + RefreshEndEventTypeNode.class.getName());
    }
    ids[242] = id242;
    absent[242] = registered242 == null;
    NodeId id243 = ServerNodeSupport.resolve(namespaceTable, RefreshRequiredEventType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered243 =
        manager.getRegisteredType(id243).orElse(null);
    if (registered243 != null
        && (registered243.nodeClass() != RefreshRequiredEventTypeNode.class
            || registered243.nodeConstructor() != CONSTRUCTOR_243
            || registered243.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + RefreshRequiredEventType.TYPE_ID
              + " expected "
              + RefreshRequiredEventTypeNode.class.getName());
    }
    ids[243] = id243;
    absent[243] = registered243 == null;
    NodeId id244 = ServerNodeSupport.resolve(namespaceTable, AuditConditionEnableEventType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered244 =
        manager.getRegisteredType(id244).orElse(null);
    if (registered244 != null
        && (registered244.nodeClass() != AuditConditionEnableEventTypeNode.class
            || registered244.nodeConstructor() != CONSTRUCTOR_244
            || registered244.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + AuditConditionEnableEventType.TYPE_ID
              + " expected "
              + AuditConditionEnableEventTypeNode.class.getName());
    }
    ids[244] = id244;
    absent[244] = registered244 == null;
    NodeId id245 =
        ServerNodeSupport.resolve(namespaceTable, AuditConditionCommentEventType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered245 =
        manager.getRegisteredType(id245).orElse(null);
    if (registered245 != null
        && (registered245.nodeClass() != AuditConditionCommentEventTypeNode.class
            || registered245.nodeConstructor() != CONSTRUCTOR_245
            || registered245.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + AuditConditionCommentEventType.TYPE_ID
              + " expected "
              + AuditConditionCommentEventTypeNode.class.getName());
    }
    ids[245] = id245;
    absent[245] = registered245 == null;
    NodeId id246 = ServerNodeSupport.resolve(namespaceTable, DialogConditionType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered246 =
        manager.getRegisteredType(id246).orElse(null);
    if (registered246 != null
        && (registered246.nodeClass() != DialogConditionTypeNode.class
            || registered246.nodeConstructor() != CONSTRUCTOR_246
            || registered246.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + DialogConditionType.TYPE_ID
              + " expected "
              + DialogConditionTypeNode.class.getName());
    }
    ids[246] = id246;
    absent[246] = registered246 == null;
    NodeId id247 = ServerNodeSupport.resolve(namespaceTable, ShelvedStateMachineType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered247 =
        manager.getRegisteredType(id247).orElse(null);
    if (registered247 != null
        && (registered247.nodeClass() != ShelvedStateMachineTypeNode.class
            || registered247.nodeConstructor() != CONSTRUCTOR_247
            || registered247.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + ShelvedStateMachineType.TYPE_ID
              + " expected "
              + ShelvedStateMachineTypeNode.class.getName());
    }
    ids[247] = id247;
    absent[247] = registered247 == null;
    NodeId id248 =
        ServerNodeSupport.resolve(namespaceTable, AuditHistoryEventUpdateEventType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered248 =
        manager.getRegisteredType(id248).orElse(null);
    if (registered248 != null
        && (registered248.nodeClass() != AuditHistoryEventUpdateEventTypeNode.class
            || registered248.nodeConstructor() != CONSTRUCTOR_248
            || registered248.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + AuditHistoryEventUpdateEventType.TYPE_ID
              + " expected "
              + AuditHistoryEventUpdateEventTypeNode.class.getName());
    }
    ids[248] = id248;
    absent[248] = registered248 == null;
    NodeId id249 =
        ServerNodeSupport.resolve(namespaceTable, AuditHistoryValueUpdateEventType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered249 =
        manager.getRegisteredType(id249).orElse(null);
    if (registered249 != null
        && (registered249.nodeClass() != AuditHistoryValueUpdateEventTypeNode.class
            || registered249.nodeConstructor() != CONSTRUCTOR_249
            || registered249.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + AuditHistoryValueUpdateEventType.TYPE_ID
              + " expected "
              + AuditHistoryValueUpdateEventTypeNode.class.getName());
    }
    ids[249] = id249;
    absent[249] = registered249 == null;
    NodeId id250 = ServerNodeSupport.resolve(namespaceTable, AuditHistoryDeleteEventType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered250 =
        manager.getRegisteredType(id250).orElse(null);
    if (registered250 != null
        && (registered250.nodeClass() != AuditHistoryDeleteEventTypeNode.class
            || registered250.nodeConstructor() != CONSTRUCTOR_250
            || registered250.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + AuditHistoryDeleteEventType.TYPE_ID
              + " expected "
              + AuditHistoryDeleteEventTypeNode.class.getName());
    }
    ids[250] = id250;
    absent[250] = registered250 == null;
    NodeId id251 =
        ServerNodeSupport.resolve(namespaceTable, AuditHistoryRawModifyDeleteEventType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered251 =
        manager.getRegisteredType(id251).orElse(null);
    if (registered251 != null
        && (registered251.nodeClass() != AuditHistoryRawModifyDeleteEventTypeNode.class
            || registered251.nodeConstructor() != CONSTRUCTOR_251
            || registered251.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + AuditHistoryRawModifyDeleteEventType.TYPE_ID
              + " expected "
              + AuditHistoryRawModifyDeleteEventTypeNode.class.getName());
    }
    ids[251] = id251;
    absent[251] = registered251 == null;
    NodeId id252 =
        ServerNodeSupport.resolve(namespaceTable, AuditHistoryAtTimeDeleteEventType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered252 =
        manager.getRegisteredType(id252).orElse(null);
    if (registered252 != null
        && (registered252.nodeClass() != AuditHistoryAtTimeDeleteEventTypeNode.class
            || registered252.nodeConstructor() != CONSTRUCTOR_252
            || registered252.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + AuditHistoryAtTimeDeleteEventType.TYPE_ID
              + " expected "
              + AuditHistoryAtTimeDeleteEventTypeNode.class.getName());
    }
    ids[252] = id252;
    absent[252] = registered252 == null;
    NodeId id253 =
        ServerNodeSupport.resolve(namespaceTable, AuditHistoryEventDeleteEventType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered253 =
        manager.getRegisteredType(id253).orElse(null);
    if (registered253 != null
        && (registered253.nodeClass() != AuditHistoryEventDeleteEventTypeNode.class
            || registered253.nodeConstructor() != CONSTRUCTOR_253
            || registered253.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + AuditHistoryEventDeleteEventType.TYPE_ID
              + " expected "
              + AuditHistoryEventDeleteEventTypeNode.class.getName());
    }
    ids[253] = id253;
    absent[253] = registered253 == null;
    NodeId id254 = ServerNodeSupport.resolve(namespaceTable, EventQueueOverflowEventType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered254 =
        manager.getRegisteredType(id254).orElse(null);
    if (registered254 != null
        && (registered254.nodeClass() != EventQueueOverflowEventTypeNode.class
            || registered254.nodeConstructor() != CONSTRUCTOR_254
            || registered254.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + EventQueueOverflowEventType.TYPE_ID
              + " expected "
              + EventQueueOverflowEventTypeNode.class.getName());
    }
    ids[254] = id254;
    absent[254] = registered254 == null;
    NodeId id255 = ServerNodeSupport.resolve(namespaceTable, AlarmSuppressionGroupType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered255 =
        manager.getRegisteredType(id255).orElse(null);
    if (registered255 != null
        && (registered255.nodeClass() != AlarmSuppressionGroupTypeNode.class
            || registered255.nodeConstructor() != CONSTRUCTOR_255
            || registered255.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + AlarmSuppressionGroupType.TYPE_ID
              + " expected "
              + AlarmSuppressionGroupTypeNode.class.getName());
    }
    ids[255] = id255;
    absent[255] = registered255 == null;
  }

  private static void validate2(
      NamespaceTable namespaceTable, ObjectTypeManager manager, NodeId[] ids, boolean[] absent) {
    NodeId id256 =
        ServerNodeSupport.resolve(namespaceTable, TrustListUpdateRequestedAuditEventType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered256 =
        manager.getRegisteredType(id256).orElse(null);
    if (registered256 != null
        && (registered256.nodeClass() != TrustListUpdateRequestedAuditEventTypeNode.class
            || registered256.nodeConstructor() != CONSTRUCTOR_256
            || registered256.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + TrustListUpdateRequestedAuditEventType.TYPE_ID
              + " expected "
              + TrustListUpdateRequestedAuditEventTypeNode.class.getName());
    }
    ids[256] = id256;
    absent[256] = registered256 == null;
    NodeId id257 = ServerNodeSupport.resolve(namespaceTable, TransactionDiagnosticsType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered257 =
        manager.getRegisteredType(id257).orElse(null);
    if (registered257 != null
        && (registered257.nodeClass() != TransactionDiagnosticsTypeNode.class
            || registered257.nodeConstructor() != CONSTRUCTOR_257
            || registered257.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + TransactionDiagnosticsType.TYPE_ID
              + " expected "
              + TransactionDiagnosticsTypeNode.class.getName());
    }
    ids[257] = id257;
    absent[257] = registered257 == null;
    NodeId id258 =
        ServerNodeSupport.resolve(namespaceTable, CertificateUpdateRequestedAuditEventType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered258 =
        manager.getRegisteredType(id258).orElse(null);
    if (registered258 != null
        && (registered258.nodeClass() != CertificateUpdateRequestedAuditEventTypeNode.class
            || registered258.nodeConstructor() != CONSTRUCTOR_258
            || registered258.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + CertificateUpdateRequestedAuditEventType.TYPE_ID
              + " expected "
              + CertificateUpdateRequestedAuditEventTypeNode.class.getName());
    }
    ids[258] = id258;
    absent[258] = registered258 == null;
    NodeId id259 =
        ServerNodeSupport.resolve(namespaceTable, NonTransparentBackupRedundancyType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered259 =
        manager.getRegisteredType(id259).orElse(null);
    if (registered259 != null
        && (registered259.nodeClass() != NonTransparentBackupRedundancyTypeNode.class
            || registered259.nodeConstructor() != CONSTRUCTOR_259
            || registered259.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + NonTransparentBackupRedundancyType.TYPE_ID
              + " expected "
              + NonTransparentBackupRedundancyTypeNode.class.getName());
    }
    ids[259] = id259;
    absent[259] = registered259 == null;
    NodeId id260 = ServerNodeSupport.resolve(namespaceTable, SyntaxReferenceEntryType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered260 =
        manager.getRegisteredType(id260).orElse(null);
    if (registered260 != null
        && (registered260.nodeClass() != SyntaxReferenceEntryTypeNode.class
            || registered260.nodeConstructor() != CONSTRUCTOR_260
            || registered260.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + SyntaxReferenceEntryType.TYPE_ID
              + " expected "
              + SyntaxReferenceEntryTypeNode.class.getName());
    }
    ids[260] = id260;
    absent[260] = registered260 == null;
    NodeId id261 = ServerNodeSupport.resolve(namespaceTable, UnitType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered261 =
        manager.getRegisteredType(id261).orElse(null);
    if (registered261 != null
        && (registered261.nodeClass() != UnitTypeNode.class
            || registered261.nodeConstructor() != CONSTRUCTOR_261
            || registered261.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + UnitType.TYPE_ID
              + " expected "
              + UnitTypeNode.class.getName());
    }
    ids[261] = id261;
    absent[261] = registered261 == null;
    NodeId id262 = ServerNodeSupport.resolve(namespaceTable, ServerUnitType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered262 =
        manager.getRegisteredType(id262).orElse(null);
    if (registered262 != null
        && (registered262.nodeClass() != ServerUnitTypeNode.class
            || registered262.nodeConstructor() != CONSTRUCTOR_262
            || registered262.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + ServerUnitType.TYPE_ID
              + " expected "
              + ServerUnitTypeNode.class.getName());
    }
    ids[262] = id262;
    absent[262] = registered262 == null;
    NodeId id263 = ServerNodeSupport.resolve(namespaceTable, AlternativeUnitType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered263 =
        manager.getRegisteredType(id263).orElse(null);
    if (registered263 != null
        && (registered263.nodeClass() != AlternativeUnitTypeNode.class
            || registered263.nodeConstructor() != CONSTRUCTOR_263
            || registered263.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + AlternativeUnitType.TYPE_ID
              + " expected "
              + AlternativeUnitTypeNode.class.getName());
    }
    ids[263] = id263;
    absent[263] = registered263 == null;
    NodeId id264 = ServerNodeSupport.resolve(namespaceTable, QuantityType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered264 =
        manager.getRegisteredType(id264).orElse(null);
    if (registered264 != null
        && (registered264.nodeClass() != QuantityTypeNode.class
            || registered264.nodeConstructor() != CONSTRUCTOR_264
            || registered264.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + QuantityType.TYPE_ID
              + " expected "
              + QuantityTypeNode.class.getName());
    }
    ids[264] = id264;
    absent[264] = registered264 == null;
    NodeId id265 =
        ServerNodeSupport.resolve(namespaceTable, HistoricalEventConfigurationType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered265 =
        manager.getRegisteredType(id265).orElse(null);
    if (registered265 != null
        && (registered265.nodeClass() != HistoricalEventConfigurationTypeNode.class
            || registered265.nodeConstructor() != CONSTRUCTOR_265
            || registered265.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + HistoricalEventConfigurationType.TYPE_ID
              + " expected "
              + HistoricalEventConfigurationTypeNode.class.getName());
    }
    ids[265] = id265;
    absent[265] = registered265 == null;
    NodeId id266 =
        ServerNodeSupport.resolve(namespaceTable, HistoricalExternalEventSourceType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered266 =
        manager.getRegisteredType(id266).orElse(null);
    if (registered266 != null
        && (registered266.nodeClass() != HistoricalExternalEventSourceTypeNode.class
            || registered266.nodeConstructor() != CONSTRUCTOR_266
            || registered266.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + HistoricalExternalEventSourceType.TYPE_ID
              + " expected "
              + HistoricalExternalEventSourceTypeNode.class.getName());
    }
    ids[266] = id266;
    absent[266] = registered266 == null;
    NodeId id267 =
        ServerNodeSupport.resolve(namespaceTable, AuditHistoryConfigurationChangeEventType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered267 =
        manager.getRegisteredType(id267).orElse(null);
    if (registered267 != null
        && (registered267.nodeClass() != AuditHistoryConfigurationChangeEventTypeNode.class
            || registered267.nodeConstructor() != CONSTRUCTOR_267
            || registered267.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + AuditHistoryConfigurationChangeEventType.TYPE_ID
              + " expected "
              + AuditHistoryConfigurationChangeEventTypeNode.class.getName());
    }
    ids[267] = id267;
    absent[267] = registered267 == null;
    NodeId id268 =
        ServerNodeSupport.resolve(namespaceTable, AuditHistoryBulkInsertEventType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered268 =
        manager.getRegisteredType(id268).orElse(null);
    if (registered268 != null
        && (registered268.nodeClass() != AuditHistoryBulkInsertEventTypeNode.class
            || registered268.nodeConstructor() != CONSTRUCTOR_268
            || registered268.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + AuditHistoryBulkInsertEventType.TYPE_ID
              + " expected "
              + AuditHistoryBulkInsertEventTypeNode.class.getName());
    }
    ids[268] = id268;
    absent[268] = registered268 == null;
    NodeId id269 =
        ServerNodeSupport.resolve(namespaceTable, ProgramTransitionAuditEventType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered269 =
        manager.getRegisteredType(id269).orElse(null);
    if (registered269 != null
        && (registered269.nodeClass() != ProgramTransitionAuditEventTypeNode.class
            || registered269.nodeConstructor() != CONSTRUCTOR_269
            || registered269.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + ProgramTransitionAuditEventType.TYPE_ID
              + " expected "
              + ProgramTransitionAuditEventTypeNode.class.getName());
    }
    ids[269] = id269;
    absent[269] = registered269 == null;
    NodeId id270 = ServerNodeSupport.resolve(namespaceTable, DataTypeSystemType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered270 =
        manager.getRegisteredType(id270).orElse(null);
    if (registered270 != null
        && (registered270.nodeClass() != DataTypeSystemTypeNode.class
            || registered270.nodeConstructor() != CONSTRUCTOR_270
            || registered270.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + DataTypeSystemType.TYPE_ID
              + " expected "
              + DataTypeSystemTypeNode.class.getName());
    }
    ids[270] = id270;
    absent[270] = registered270 == null;
    NodeId id271 = ServerNodeSupport.resolve(namespaceTable, DataTypeEncodingType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered271 =
        manager.getRegisteredType(id271).orElse(null);
    if (registered271 != null
        && (registered271.nodeClass() != DataTypeEncodingTypeNode.class
            || registered271.nodeConstructor() != CONSTRUCTOR_271
            || registered271.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + DataTypeEncodingType.TYPE_ID
              + " expected "
              + DataTypeEncodingTypeNode.class.getName());
    }
    ids[271] = id271;
    absent[271] = registered271 == null;
    NodeId id272 = ServerNodeSupport.resolve(namespaceTable, ModellingRuleType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered272 =
        manager.getRegisteredType(id272).orElse(null);
    if (registered272 != null
        && (registered272.nodeClass() != ModellingRuleTypeNode.class
            || registered272.nodeConstructor() != CONSTRUCTOR_272
            || registered272.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + ModellingRuleType.TYPE_ID
              + " expected "
              + ModellingRuleTypeNode.class.getName());
    }
    ids[272] = id272;
    absent[272] = registered272 == null;
    NodeId id273 =
        ServerNodeSupport.resolve(namespaceTable, AuditConditionRespondEventType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered273 =
        manager.getRegisteredType(id273).orElse(null);
    if (registered273 != null
        && (registered273.nodeClass() != AuditConditionRespondEventTypeNode.class
            || registered273.nodeConstructor() != CONSTRUCTOR_273
            || registered273.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + AuditConditionRespondEventType.TYPE_ID
              + " expected "
              + AuditConditionRespondEventTypeNode.class.getName());
    }
    ids[273] = id273;
    absent[273] = registered273 == null;
    NodeId id274 =
        ServerNodeSupport.resolve(namespaceTable, AuditConditionAcknowledgeEventType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered274 =
        manager.getRegisteredType(id274).orElse(null);
    if (registered274 != null
        && (registered274.nodeClass() != AuditConditionAcknowledgeEventTypeNode.class
            || registered274.nodeConstructor() != CONSTRUCTOR_274
            || registered274.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + AuditConditionAcknowledgeEventType.TYPE_ID
              + " expected "
              + AuditConditionAcknowledgeEventTypeNode.class.getName());
    }
    ids[274] = id274;
    absent[274] = registered274 == null;
    NodeId id275 =
        ServerNodeSupport.resolve(namespaceTable, AuditConditionConfirmEventType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered275 =
        manager.getRegisteredType(id275).orElse(null);
    if (registered275 != null
        && (registered275.nodeClass() != AuditConditionConfirmEventTypeNode.class
            || registered275.nodeConstructor() != CONSTRUCTOR_275
            || registered275.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + AuditConditionConfirmEventType.TYPE_ID
              + " expected "
              + AuditConditionConfirmEventTypeNode.class.getName());
    }
    ids[275] = id275;
    absent[275] = registered275 == null;
    NodeId id276 =
        ServerNodeSupport.resolve(namespaceTable, ExclusiveLimitStateMachineType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered276 =
        manager.getRegisteredType(id276).orElse(null);
    if (registered276 != null
        && (registered276.nodeClass() != ExclusiveLimitStateMachineTypeNode.class
            || registered276.nodeConstructor() != CONSTRUCTOR_276
            || registered276.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + ExclusiveLimitStateMachineType.TYPE_ID
              + " expected "
              + ExclusiveLimitStateMachineTypeNode.class.getName());
    }
    ids[276] = id276;
    absent[276] = registered276 == null;
    NodeId id277 = ServerNodeSupport.resolve(namespaceTable, ExclusiveLimitAlarmType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered277 =
        manager.getRegisteredType(id277).orElse(null);
    if (registered277 != null
        && (registered277.nodeClass() != ExclusiveLimitAlarmTypeNode.class
            || registered277.nodeConstructor() != CONSTRUCTOR_277
            || registered277.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + ExclusiveLimitAlarmType.TYPE_ID
              + " expected "
              + ExclusiveLimitAlarmTypeNode.class.getName());
    }
    ids[277] = id277;
    absent[277] = registered277 == null;
    NodeId id278 = ServerNodeSupport.resolve(namespaceTable, ExclusiveLevelAlarmType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered278 =
        manager.getRegisteredType(id278).orElse(null);
    if (registered278 != null
        && (registered278.nodeClass() != ExclusiveLevelAlarmTypeNode.class
            || registered278.nodeConstructor() != CONSTRUCTOR_278
            || registered278.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + ExclusiveLevelAlarmType.TYPE_ID
              + " expected "
              + ExclusiveLevelAlarmTypeNode.class.getName());
    }
    ids[278] = id278;
    absent[278] = registered278 == null;
    NodeId id279 =
        ServerNodeSupport.resolve(namespaceTable, ExclusiveRateOfChangeAlarmType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered279 =
        manager.getRegisteredType(id279).orElse(null);
    if (registered279 != null
        && (registered279.nodeClass() != ExclusiveRateOfChangeAlarmTypeNode.class
            || registered279.nodeConstructor() != CONSTRUCTOR_279
            || registered279.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + ExclusiveRateOfChangeAlarmType.TYPE_ID
              + " expected "
              + ExclusiveRateOfChangeAlarmTypeNode.class.getName());
    }
    ids[279] = id279;
    absent[279] = registered279 == null;
    NodeId id280 = ServerNodeSupport.resolve(namespaceTable, ExclusiveDeviationAlarmType.TYPE_ID);
    ObjectTypeManager.RegisteredObjectType registered280 =
        manager.getRegisteredType(id280).orElse(null);
    if (registered280 != null
        && (registered280.nodeClass() != ExclusiveDeviationAlarmTypeNode.class
            || registered280.nodeConstructor() != CONSTRUCTOR_280
            || registered280.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + ExclusiveDeviationAlarmType.TYPE_ID
              + " expected "
              + ExclusiveDeviationAlarmTypeNode.class.getName());
    }
    ids[280] = id280;
    absent[280] = registered280 == null;
  }

  private static void install0(ObjectTypeManager manager, NodeId[] ids, boolean[] absent) {
    if (absent[0]) {
      manager.registerObjectType(ids[0], BaseObjectTypeNode.class, CONSTRUCTOR_0);
    }
    if (absent[1]) {
      manager.registerObjectType(ids[1], BaseEventTypeNode.class, CONSTRUCTOR_1);
    }
    if (absent[2]) {
      manager.registerObjectType(ids[2], ConditionTypeNode.class, CONSTRUCTOR_2);
    }
    if (absent[3]) {
      manager.registerObjectType(ids[3], AcknowledgeableConditionTypeNode.class, CONSTRUCTOR_3);
    }
    if (absent[4]) {
      manager.registerObjectType(ids[4], AlarmConditionTypeNode.class, CONSTRUCTOR_4);
    }
    if (absent[5]) {
      manager.registerObjectType(ids[5], LimitAlarmTypeNode.class, CONSTRUCTOR_5);
    }
    if (absent[6]) {
      manager.registerObjectType(ids[6], NonExclusiveLimitAlarmTypeNode.class, CONSTRUCTOR_6);
    }
    if (absent[7]) {
      manager.registerObjectType(ids[7], NonExclusiveLevelAlarmTypeNode.class, CONSTRUCTOR_7);
    }
    if (absent[8]) {
      manager.registerObjectType(
          ids[8], NonExclusiveRateOfChangeAlarmTypeNode.class, CONSTRUCTOR_8);
    }
    if (absent[9]) {
      manager.registerObjectType(ids[9], NonExclusiveDeviationAlarmTypeNode.class, CONSTRUCTOR_9);
    }
    if (absent[10]) {
      manager.registerObjectType(ids[10], DiscreteAlarmTypeNode.class, CONSTRUCTOR_10);
    }
    if (absent[11]) {
      manager.registerObjectType(ids[11], OffNormalAlarmTypeNode.class, CONSTRUCTOR_11);
    }
    if (absent[12]) {
      manager.registerObjectType(ids[12], TripAlarmTypeNode.class, CONSTRUCTOR_12);
    }
    if (absent[13]) {
      manager.registerObjectType(ids[13], AuditEventTypeNode.class, CONSTRUCTOR_13);
    }
    if (absent[14]) {
      manager.registerObjectType(ids[14], AuditUpdateMethodEventTypeNode.class, CONSTRUCTOR_14);
    }
    if (absent[15]) {
      manager.registerObjectType(ids[15], AuditConditionEventTypeNode.class, CONSTRUCTOR_15);
    }
    if (absent[16]) {
      manager.registerObjectType(
          ids[16], AuditConditionShelvingEventTypeNode.class, CONSTRUCTOR_16);
    }
    if (absent[17]) {
      manager.registerObjectType(ids[17], BaseConditionClassTypeNode.class, CONSTRUCTOR_17);
    }
    if (absent[18]) {
      manager.registerObjectType(ids[18], ProcessConditionClassTypeNode.class, CONSTRUCTOR_18);
    }
    if (absent[19]) {
      manager.registerObjectType(ids[19], MaintenanceConditionClassTypeNode.class, CONSTRUCTOR_19);
    }
    if (absent[20]) {
      manager.registerObjectType(ids[20], SystemConditionClassTypeNode.class, CONSTRUCTOR_20);
    }
    if (absent[21]) {
      manager.registerObjectType(ids[21], AggregateConfigurationTypeNode.class, CONSTRUCTOR_21);
    }
    if (absent[22]) {
      manager.registerObjectType(ids[22], ProgressEventTypeNode.class, CONSTRUCTOR_22);
    }
    if (absent[23]) {
      manager.registerObjectType(ids[23], SystemEventTypeNode.class, CONSTRUCTOR_23);
    }
    if (absent[24]) {
      manager.registerObjectType(ids[24], SystemStatusChangeEventTypeNode.class, CONSTRUCTOR_24);
    }
    if (absent[25]) {
      manager.registerObjectType(ids[25], FolderTypeNode.class, CONSTRUCTOR_25);
    }
    if (absent[26]) {
      manager.registerObjectType(ids[26], OperationLimitsTypeNode.class, CONSTRUCTOR_26);
    }
    if (absent[27]) {
      manager.registerObjectType(ids[27], FileTypeNode.class, CONSTRUCTOR_27);
    }
    if (absent[28]) {
      manager.registerObjectType(ids[28], AddressSpaceFileTypeNode.class, CONSTRUCTOR_28);
    }
    if (absent[29]) {
      manager.registerObjectType(ids[29], NamespaceMetadataTypeNode.class, CONSTRUCTOR_29);
    }
    if (absent[30]) {
      manager.registerObjectType(ids[30], NamespacesTypeNode.class, CONSTRUCTOR_30);
    }
    if (absent[31]) {
      manager.registerObjectType(ids[31], SystemOffNormalAlarmTypeNode.class, CONSTRUCTOR_31);
    }
    if (absent[32]) {
      manager.registerObjectType(ids[32], AuditUpdateStateEventTypeNode.class, CONSTRUCTOR_32);
    }
    if (absent[33]) {
      manager.registerObjectType(
          ids[33], AuditProgramTransitionEventTypeNode.class, CONSTRUCTOR_33);
    }
    if (absent[34]) {
      manager.registerObjectType(ids[34], ServerRedundancyTypeNode.class, CONSTRUCTOR_34);
    }
    if (absent[35]) {
      manager.registerObjectType(ids[35], NonTransparentRedundancyTypeNode.class, CONSTRUCTOR_35);
    }
    if (absent[36]) {
      manager.registerObjectType(
          ids[36], NonTransparentNetworkRedundancyTypeNode.class, CONSTRUCTOR_36);
    }
    if (absent[37]) {
      manager.registerObjectType(ids[37], TrustListTypeNode.class, CONSTRUCTOR_37);
    }
    if (absent[38]) {
      manager.registerObjectType(ids[38], CertificateGroupTypeNode.class, CONSTRUCTOR_38);
    }
    if (absent[39]) {
      manager.registerObjectType(ids[39], CertificateTypeNode.class, CONSTRUCTOR_39);
    }
    if (absent[40]) {
      manager.registerObjectType(ids[40], ApplicationCertificateTypeNode.class, CONSTRUCTOR_40);
    }
    if (absent[41]) {
      manager.registerObjectType(ids[41], HttpsCertificateTypeNode.class, CONSTRUCTOR_41);
    }
    if (absent[42]) {
      manager.registerObjectType(
          ids[42], RsaMinApplicationCertificateTypeNode.class, CONSTRUCTOR_42);
    }
    if (absent[43]) {
      manager.registerObjectType(
          ids[43], RsaSha256ApplicationCertificateTypeNode.class, CONSTRUCTOR_43);
    }
    if (absent[44]) {
      manager.registerObjectType(ids[44], TrustListUpdatedAuditEventTypeNode.class, CONSTRUCTOR_44);
    }
    if (absent[45]) {
      manager.registerObjectType(ids[45], ServerConfigurationTypeNode.class, CONSTRUCTOR_45);
    }
    if (absent[46]) {
      manager.registerObjectType(
          ids[46], CertificateUpdatedAuditEventTypeNode.class, CONSTRUCTOR_46);
    }
    if (absent[47]) {
      manager.registerObjectType(ids[47], CertificateExpirationAlarmTypeNode.class, CONSTRUCTOR_47);
    }
    if (absent[48]) {
      manager.registerObjectType(ids[48], FileDirectoryTypeNode.class, CONSTRUCTOR_48);
    }
    if (absent[49]) {
      manager.registerObjectType(ids[49], CertificateGroupFolderTypeNode.class, CONSTRUCTOR_49);
    }
    if (absent[50]) {
      manager.registerObjectType(ids[50], PubSubConnectionTypeNode.class, CONSTRUCTOR_50);
    }
    if (absent[51]) {
      manager.registerObjectType(ids[51], PubSubGroupTypeNode.class, CONSTRUCTOR_51);
    }
    if (absent[52]) {
      manager.registerObjectType(ids[52], PubSubKeyServiceTypeNode.class, CONSTRUCTOR_52);
    }
    if (absent[53]) {
      manager.registerObjectType(ids[53], PublishSubscribeTypeNode.class, CONSTRUCTOR_53);
    }
    if (absent[54]) {
      manager.registerObjectType(ids[54], DataSetFolderTypeNode.class, CONSTRUCTOR_54);
    }
    if (absent[55]) {
      manager.registerObjectType(ids[55], PublishedDataSetTypeNode.class, CONSTRUCTOR_55);
    }
    if (absent[56]) {
      manager.registerObjectType(ids[56], PublishedDataItemsTypeNode.class, CONSTRUCTOR_56);
    }
    if (absent[57]) {
      manager.registerObjectType(ids[57], PublishedEventsTypeNode.class, CONSTRUCTOR_57);
    }
    if (absent[58]) {
      manager.registerObjectType(ids[58], PubSubStatusTypeNode.class, CONSTRUCTOR_58);
    }
    if (absent[59]) {
      manager.registerObjectType(ids[59], AuditConditionResetEventTypeNode.class, CONSTRUCTOR_59);
    }
    if (absent[60]) {
      manager.registerObjectType(ids[60], ConnectionTransportTypeNode.class, CONSTRUCTOR_60);
    }
    if (absent[61]) {
      manager.registerObjectType(
          ids[61], DatagramConnectionTransportTypeNode.class, CONSTRUCTOR_61);
    }
    if (absent[62]) {
      manager.registerObjectType(ids[62], SubscribedDataSetTypeNode.class, CONSTRUCTOR_62);
    }
    if (absent[63]) {
      manager.registerObjectType(ids[63], StateTypeNode.class, CONSTRUCTOR_63);
    }
    if (absent[64]) {
      manager.registerObjectType(ids[64], ChoiceStateTypeNode.class, CONSTRUCTOR_64);
    }
    if (absent[65]) {
      manager.registerObjectType(ids[65], TargetVariablesTypeNode.class, CONSTRUCTOR_65);
    }
    if (absent[66]) {
      manager.registerObjectType(ids[66], SubscribedDataSetMirrorTypeNode.class, CONSTRUCTOR_66);
    }
    if (absent[67]) {
      manager.registerObjectType(ids[67], BrokerConnectionTransportTypeNode.class, CONSTRUCTOR_67);
    }
    if (absent[68]) {
      manager.registerObjectType(ids[68], DataSetWriterTypeNode.class, CONSTRUCTOR_68);
    }
    if (absent[69]) {
      manager.registerObjectType(ids[69], DataSetWriterTransportTypeNode.class, CONSTRUCTOR_69);
    }
    if (absent[70]) {
      manager.registerObjectType(ids[70], DataSetReaderTypeNode.class, CONSTRUCTOR_70);
    }
    if (absent[71]) {
      manager.registerObjectType(ids[71], DataSetReaderTransportTypeNode.class, CONSTRUCTOR_71);
    }
    if (absent[72]) {
      manager.registerObjectType(ids[72], ConfigurationFileTypeNode.class, CONSTRUCTOR_72);
    }
    if (absent[73]) {
      manager.registerObjectType(ids[73], SecurityGroupFolderTypeNode.class, CONSTRUCTOR_73);
    }
    if (absent[74]) {
      manager.registerObjectType(ids[74], SecurityGroupTypeNode.class, CONSTRUCTOR_74);
    }
    if (absent[75]) {
      manager.registerObjectType(ids[75], ExtensionFieldsTypeNode.class, CONSTRUCTOR_75);
    }
    if (absent[76]) {
      manager.registerObjectType(ids[76], PubSubStatusEventTypeNode.class, CONSTRUCTOR_76);
    }
    if (absent[77]) {
      manager.registerObjectType(
          ids[77], ConfigurationUpdatedAuditEventTypeNode.class, CONSTRUCTOR_77);
    }
    if (absent[78]) {
      manager.registerObjectType(
          ids[78], PubSubTransportLimitsExceedEventTypeNode.class, CONSTRUCTOR_78);
    }
    if (absent[79]) {
      manager.registerObjectType(
          ids[79], ApplicationConfigurationFileTypeNode.class, CONSTRUCTOR_79);
    }
    if (absent[80]) {
      manager.registerObjectType(
          ids[80], PubSubCommunicationFailureEventTypeNode.class, CONSTRUCTOR_80);
    }
    if (absent[81]) {
      manager.registerObjectType(ids[81], RoleSetTypeNode.class, CONSTRUCTOR_81);
    }
    if (absent[82]) {
      manager.registerObjectType(ids[82], RoleTypeNode.class, CONSTRUCTOR_82);
    }
    if (absent[83]) {
      manager.registerObjectType(ids[83], TemporaryFileTransferTypeNode.class, CONSTRUCTOR_83);
    }
    if (absent[84]) {
      manager.registerObjectType(ids[84], StateMachineTypeNode.class, CONSTRUCTOR_84);
    }
    if (absent[85]) {
      manager.registerObjectType(ids[85], FiniteStateMachineTypeNode.class, CONSTRUCTOR_85);
    }
    if (absent[86]) {
      manager.registerObjectType(ids[86], FileTransferStateMachineTypeNode.class, CONSTRUCTOR_86);
    }
    if (absent[87]) {
      manager.registerObjectType(ids[87], AlarmGroupTypeNode.class, CONSTRUCTOR_87);
    }
    if (absent[88]) {
      manager.registerObjectType(
          ids[88], ApplicationConfigurationFolderTypeNode.class, CONSTRUCTOR_88);
    }
    if (absent[89]) {
      manager.registerObjectType(ids[89], DiscrepancyAlarmTypeNode.class, CONSTRUCTOR_89);
    }
    if (absent[90]) {
      manager.registerObjectType(ids[90], SafetyConditionClassTypeNode.class, CONSTRUCTOR_90);
    }
    if (absent[91]) {
      manager.registerObjectType(
          ids[91], HighlyManagedAlarmConditionClassTypeNode.class, CONSTRUCTOR_91);
    }
    if (absent[92]) {
      manager.registerObjectType(ids[92], TrainingConditionClassTypeNode.class, CONSTRUCTOR_92);
    }
    if (absent[93]) {
      manager.registerObjectType(ids[93], TestingConditionClassTypeNode.class, CONSTRUCTOR_93);
    }
    if (absent[94]) {
      manager.registerObjectType(
          ids[94], AuditConditionSuppressionEventTypeNode.class, CONSTRUCTOR_94);
    }
    if (absent[95]) {
      manager.registerObjectType(ids[95], AuditConditionSilenceEventTypeNode.class, CONSTRUCTOR_95);
    }
    if (absent[96]) {
      manager.registerObjectType(
          ids[96], AuditConditionOutOfServiceEventTypeNode.class, CONSTRUCTOR_96);
    }
    if (absent[97]) {
      manager.registerObjectType(ids[97], AlarmMetricsTypeNode.class, CONSTRUCTOR_97);
    }
    if (absent[98]) {
      manager.registerObjectType(
          ids[98], KeyCredentialConfigurationFolderTypeNode.class, CONSTRUCTOR_98);
    }
    if (absent[99]) {
      manager.registerObjectType(ids[99], DictionaryEntryTypeNode.class, CONSTRUCTOR_99);
    }
    if (absent[100]) {
      manager.registerObjectType(ids[100], DictionaryFolderTypeNode.class, CONSTRUCTOR_100);
    }
    if (absent[101]) {
      manager.registerObjectType(ids[101], IrdiDictionaryEntryTypeNode.class, CONSTRUCTOR_101);
    }
    if (absent[102]) {
      manager.registerObjectType(ids[102], UriDictionaryEntryTypeNode.class, CONSTRUCTOR_102);
    }
    if (absent[103]) {
      manager.registerObjectType(ids[103], BaseInterfaceTypeNode.class, CONSTRUCTOR_103);
    }
    if (absent[104]) {
      manager.registerObjectType(
          ids[104], RoleMappingRuleChangedAuditEventTypeNode.class, CONSTRUCTOR_104);
    }
    if (absent[105]) {
      manager.registerObjectType(ids[105], WriterGroupTypeNode.class, CONSTRUCTOR_105);
    }
    if (absent[106]) {
      manager.registerObjectType(
          ids[106], AuthorizationServiceConfigurationTypeNode.class, CONSTRUCTOR_106);
    }
    if (absent[107]) {
      manager.registerObjectType(ids[107], WriterGroupTransportTypeNode.class, CONSTRUCTOR_107);
    }
    if (absent[108]) {
      manager.registerObjectType(ids[108], WriterGroupMessageTypeNode.class, CONSTRUCTOR_108);
    }
    if (absent[109]) {
      manager.registerObjectType(ids[109], ReaderGroupTypeNode.class, CONSTRUCTOR_109);
    }
    if (absent[110]) {
      manager.registerObjectType(
          ids[110], KeyCredentialConfigurationTypeNode.class, CONSTRUCTOR_110);
    }
    if (absent[111]) {
      manager.registerObjectType(ids[111], KeyCredentialAuditEventTypeNode.class, CONSTRUCTOR_111);
    }
    if (absent[112]) {
      manager.registerObjectType(
          ids[112], KeyCredentialUpdatedAuditEventTypeNode.class, CONSTRUCTOR_112);
    }
    if (absent[113]) {
      manager.registerObjectType(
          ids[113], KeyCredentialDeletedAuditEventTypeNode.class, CONSTRUCTOR_113);
    }
    if (absent[114]) {
      manager.registerObjectType(
          ids[114], InstrumentDiagnosticAlarmTypeNode.class, CONSTRUCTOR_114);
    }
    if (absent[115]) {
      manager.registerObjectType(ids[115], SystemDiagnosticAlarmTypeNode.class, CONSTRUCTOR_115);
    }
    if (absent[116]) {
      manager.registerObjectType(
          ids[116], StatisticalConditionClassTypeNode.class, CONSTRUCTOR_116);
    }
    if (absent[117]) {
      manager.registerObjectType(ids[117], LldpInformationTypeNode.class, CONSTRUCTOR_117);
    }
    if (absent[118]) {
      manager.registerObjectType(ids[118], LldpRemoteStatisticsTypeNode.class, CONSTRUCTOR_118);
    }
    if (absent[119]) {
      manager.registerObjectType(ids[119], LldpLocalSystemTypeNode.class, CONSTRUCTOR_119);
    }
    if (absent[120]) {
      manager.registerObjectType(ids[120], LldpPortInformationTypeNode.class, CONSTRUCTOR_120);
    }
    if (absent[121]) {
      manager.registerObjectType(ids[121], LldpRemoteSystemTypeNode.class, CONSTRUCTOR_121);
    }
    if (absent[122]) {
      manager.registerObjectType(ids[122], AuditUpdateEventTypeNode.class, CONSTRUCTOR_122);
    }
    if (absent[123]) {
      manager.registerObjectType(ids[123], AuditHistoryUpdateEventTypeNode.class, CONSTRUCTOR_123);
    }
    if (absent[124]) {
      manager.registerObjectType(
          ids[124], AuditHistoryAnnotationUpdateEventTypeNode.class, CONSTRUCTOR_124);
    }
    if (absent[125]) {
      manager.registerObjectType(ids[125], TrustListOutOfDateAlarmTypeNode.class, CONSTRUCTOR_125);
    }
    if (absent[126]) {
      manager.registerObjectType(ids[126], UserCertificateTypeNode.class, CONSTRUCTOR_126);
    }
    if (absent[127]) {
      manager.registerObjectType(ids[127], TlsCertificateTypeNode.class, CONSTRUCTOR_127);
    }
  }

  private static void install1(ObjectTypeManager manager, NodeId[] ids, boolean[] absent) {
    if (absent[128]) {
      manager.registerObjectType(ids[128], TlsServerCertificateTypeNode.class, CONSTRUCTOR_128);
    }
    if (absent[129]) {
      manager.registerObjectType(ids[129], TlsClientCertificateTypeNode.class, CONSTRUCTOR_129);
    }
    if (absent[130]) {
      manager.registerObjectType(ids[130], LogObjectTypeNode.class, CONSTRUCTOR_130);
    }
    if (absent[131]) {
      manager.registerObjectType(ids[131], BaseLogEventTypeNode.class, CONSTRUCTOR_131);
    }
    if (absent[132]) {
      manager.registerObjectType(ids[132], LogOverflowEventTypeNode.class, CONSTRUCTOR_132);
    }
    if (absent[133]) {
      manager.registerObjectType(ids[133], LogEntryConditionClassTypeNode.class, CONSTRUCTOR_133);
    }
    if (absent[134]) {
      manager.registerObjectType(ids[134], PubSubDiagnosticsTypeNode.class, CONSTRUCTOR_134);
    }
    if (absent[135]) {
      manager.registerObjectType(ids[135], PubSubDiagnosticsRootTypeNode.class, CONSTRUCTOR_135);
    }
    if (absent[136]) {
      manager.registerObjectType(
          ids[136], PubSubDiagnosticsConnectionTypeNode.class, CONSTRUCTOR_136);
    }
    if (absent[137]) {
      manager.registerObjectType(ids[137], DataTypeRefinementTypeNode.class, CONSTRUCTOR_137);
    }
    if (absent[138]) {
      manager.registerObjectType(ids[138], SubtypeRestrictionTypeNode.class, CONSTRUCTOR_138);
    }
    if (absent[139]) {
      manager.registerObjectType(ids[139], SerializationEntityTypeNode.class, CONSTRUCTOR_139);
    }
    if (absent[140]) {
      manager.registerObjectType(
          ids[140], PubSubDiagnosticsWriterGroupTypeNode.class, CONSTRUCTOR_140);
    }
    if (absent[141]) {
      manager.registerObjectType(
          ids[141], PubSubDiagnosticsReaderGroupTypeNode.class, CONSTRUCTOR_141);
    }
    if (absent[142]) {
      manager.registerObjectType(
          ids[142], PubSubDiagnosticsDataSetWriterTypeNode.class, CONSTRUCTOR_142);
    }
    if (absent[143]) {
      manager.registerObjectType(
          ids[143], PubSubDiagnosticsDataSetReaderTypeNode.class, CONSTRUCTOR_143);
    }
    if (absent[144]) {
      manager.registerObjectType(ids[144], ServerTypeNode.class, CONSTRUCTOR_144);
    }
    if (absent[145]) {
      manager.registerObjectType(ids[145], ServerCapabilitiesTypeNode.class, CONSTRUCTOR_145);
    }
    if (absent[146]) {
      manager.registerObjectType(ids[146], ServerDiagnosticsTypeNode.class, CONSTRUCTOR_146);
    }
    if (absent[147]) {
      manager.registerObjectType(
          ids[147], SessionsDiagnosticsSummaryTypeNode.class, CONSTRUCTOR_147);
    }
    if (absent[148]) {
      manager.registerObjectType(ids[148], SessionDiagnosticsObjectTypeNode.class, CONSTRUCTOR_148);
    }
    if (absent[149]) {
      manager.registerObjectType(ids[149], VendorServerInfoTypeNode.class, CONSTRUCTOR_149);
    }
    if (absent[150]) {
      manager.registerObjectType(ids[150], TransparentRedundancyTypeNode.class, CONSTRUCTOR_150);
    }
    if (absent[151]) {
      manager.registerObjectType(ids[151], AuditSecurityEventTypeNode.class, CONSTRUCTOR_151);
    }
    if (absent[152]) {
      manager.registerObjectType(ids[152], AuditChannelEventTypeNode.class, CONSTRUCTOR_152);
    }
    if (absent[153]) {
      manager.registerObjectType(
          ids[153], AuditOpenSecureChannelEventTypeNode.class, CONSTRUCTOR_153);
    }
    if (absent[154]) {
      manager.registerObjectType(ids[154], AuditSessionEventTypeNode.class, CONSTRUCTOR_154);
    }
    if (absent[155]) {
      manager.registerObjectType(ids[155], AuditCreateSessionEventTypeNode.class, CONSTRUCTOR_155);
    }
    if (absent[156]) {
      manager.registerObjectType(
          ids[156], AuditActivateSessionEventTypeNode.class, CONSTRUCTOR_156);
    }
    if (absent[157]) {
      manager.registerObjectType(ids[157], AuditCancelEventTypeNode.class, CONSTRUCTOR_157);
    }
    if (absent[158]) {
      manager.registerObjectType(ids[158], AuditCertificateEventTypeNode.class, CONSTRUCTOR_158);
    }
    if (absent[159]) {
      manager.registerObjectType(
          ids[159], AuditCertificateDataMismatchEventTypeNode.class, CONSTRUCTOR_159);
    }
    if (absent[160]) {
      manager.registerObjectType(
          ids[160], AuditCertificateExpiredEventTypeNode.class, CONSTRUCTOR_160);
    }
    if (absent[161]) {
      manager.registerObjectType(
          ids[161], AuditCertificateInvalidEventTypeNode.class, CONSTRUCTOR_161);
    }
    if (absent[162]) {
      manager.registerObjectType(
          ids[162], AuditCertificateUntrustedEventTypeNode.class, CONSTRUCTOR_162);
    }
    if (absent[163]) {
      manager.registerObjectType(
          ids[163], AuditCertificateRevokedEventTypeNode.class, CONSTRUCTOR_163);
    }
    if (absent[164]) {
      manager.registerObjectType(
          ids[164], AuditCertificateMismatchEventTypeNode.class, CONSTRUCTOR_164);
    }
    if (absent[165]) {
      manager.registerObjectType(ids[165], AuditNodeManagementEventTypeNode.class, CONSTRUCTOR_165);
    }
    if (absent[166]) {
      manager.registerObjectType(ids[166], AuditAddNodesEventTypeNode.class, CONSTRUCTOR_166);
    }
    if (absent[167]) {
      manager.registerObjectType(ids[167], AuditDeleteNodesEventTypeNode.class, CONSTRUCTOR_167);
    }
    if (absent[168]) {
      manager.registerObjectType(ids[168], AuditAddReferencesEventTypeNode.class, CONSTRUCTOR_168);
    }
    if (absent[169]) {
      manager.registerObjectType(
          ids[169], AuditDeleteReferencesEventTypeNode.class, CONSTRUCTOR_169);
    }
    if (absent[170]) {
      manager.registerObjectType(ids[170], AuditWriteUpdateEventTypeNode.class, CONSTRUCTOR_170);
    }
    if (absent[171]) {
      manager.registerObjectType(ids[171], ReaderGroupTransportTypeNode.class, CONSTRUCTOR_171);
    }
    if (absent[172]) {
      manager.registerObjectType(ids[172], ReaderGroupMessageTypeNode.class, CONSTRUCTOR_172);
    }
    if (absent[173]) {
      manager.registerObjectType(ids[173], DataSetWriterMessageTypeNode.class, CONSTRUCTOR_173);
    }
    if (absent[174]) {
      manager.registerObjectType(ids[174], DataSetReaderMessageTypeNode.class, CONSTRUCTOR_174);
    }
    if (absent[175]) {
      manager.registerObjectType(ids[175], UadpWriterGroupMessageTypeNode.class, CONSTRUCTOR_175);
    }
    if (absent[176]) {
      manager.registerObjectType(ids[176], UadpDataSetWriterMessageTypeNode.class, CONSTRUCTOR_176);
    }
    if (absent[177]) {
      manager.registerObjectType(ids[177], UadpDataSetReaderMessageTypeNode.class, CONSTRUCTOR_177);
    }
    if (absent[178]) {
      manager.registerObjectType(ids[178], JsonWriterGroupMessageTypeNode.class, CONSTRUCTOR_178);
    }
    if (absent[179]) {
      manager.registerObjectType(ids[179], JsonDataSetWriterMessageTypeNode.class, CONSTRUCTOR_179);
    }
    if (absent[180]) {
      manager.registerObjectType(ids[180], JsonDataSetReaderMessageTypeNode.class, CONSTRUCTOR_180);
    }
    if (absent[181]) {
      manager.registerObjectType(
          ids[181], DatagramWriterGroupTransportTypeNode.class, CONSTRUCTOR_181);
    }
    if (absent[182]) {
      manager.registerObjectType(
          ids[182], BrokerWriterGroupTransportTypeNode.class, CONSTRUCTOR_182);
    }
    if (absent[183]) {
      manager.registerObjectType(
          ids[183], BrokerDataSetWriterTransportTypeNode.class, CONSTRUCTOR_183);
    }
    if (absent[184]) {
      manager.registerObjectType(
          ids[184], BrokerDataSetReaderTransportTypeNode.class, CONSTRUCTOR_184);
    }
    if (absent[185]) {
      manager.registerObjectType(ids[185], NetworkAddressTypeNode.class, CONSTRUCTOR_185);
    }
    if (absent[186]) {
      manager.registerObjectType(ids[186], NetworkAddressUrlTypeNode.class, CONSTRUCTOR_186);
    }
    if (absent[187]) {
      manager.registerObjectType(ids[187], DeviceFailureEventTypeNode.class, CONSTRUCTOR_187);
    }
    if (absent[188]) {
      manager.registerObjectType(ids[188], BaseModelChangeEventTypeNode.class, CONSTRUCTOR_188);
    }
    if (absent[189]) {
      manager.registerObjectType(ids[189], GeneralModelChangeEventTypeNode.class, CONSTRUCTOR_189);
    }
    if (absent[190]) {
      manager.registerObjectType(ids[190], InitialStateTypeNode.class, CONSTRUCTOR_190);
    }
    if (absent[191]) {
      manager.registerObjectType(ids[191], TransitionTypeNode.class, CONSTRUCTOR_191);
    }
    if (absent[192]) {
      manager.registerObjectType(ids[192], TransitionEventTypeNode.class, CONSTRUCTOR_192);
    }
    if (absent[193]) {
      manager.registerObjectType(
          ids[193], HistoricalDataConfigurationTypeNode.class, CONSTRUCTOR_193);
    }
    if (absent[194]) {
      manager.registerObjectType(
          ids[194], HistoryServerCapabilitiesTypeNode.class, CONSTRUCTOR_194);
    }
    if (absent[195]) {
      manager.registerObjectType(ids[195], AggregateFunctionTypeNode.class, CONSTRUCTOR_195);
    }
    if (absent[196]) {
      manager.registerObjectType(ids[196], AliasNameTypeNode.class, CONSTRUCTOR_196);
    }
    if (absent[197]) {
      manager.registerObjectType(ids[197], AliasNameCategoryTypeNode.class, CONSTRUCTOR_197);
    }
    if (absent[198]) {
      manager.registerObjectType(ids[198], IOrderedObjectTypeNode.class, CONSTRUCTOR_198);
    }
    if (absent[199]) {
      manager.registerObjectType(ids[199], OrderedListTypeNode.class, CONSTRUCTOR_199);
    }
    if (absent[200]) {
      manager.registerObjectType(
          ids[200], EccApplicationCertificateTypeNode.class, CONSTRUCTOR_200);
    }
    if (absent[201]) {
      manager.registerObjectType(
          ids[201], EccNistP256ApplicationCertificateTypeNode.class, CONSTRUCTOR_201);
    }
    if (absent[202]) {
      manager.registerObjectType(
          ids[202], EccNistP384ApplicationCertificateTypeNode.class, CONSTRUCTOR_202);
    }
    if (absent[203]) {
      manager.registerObjectType(
          ids[203], EccBrainpoolP256r1ApplicationCertificateTypeNode.class, CONSTRUCTOR_203);
    }
    if (absent[204]) {
      manager.registerObjectType(
          ids[204], EccBrainpoolP384r1ApplicationCertificateTypeNode.class, CONSTRUCTOR_204);
    }
    if (absent[205]) {
      manager.registerObjectType(
          ids[205], EccCurve25519ApplicationCertificateTypeNode.class, CONSTRUCTOR_205);
    }
    if (absent[206]) {
      manager.registerObjectType(
          ids[206], EccCurve448ApplicationCertificateTypeNode.class, CONSTRUCTOR_206);
    }
    if (absent[207]) {
      manager.registerObjectType(
          ids[207], AuthorizationServicesConfigurationFolderTypeNode.class, CONSTRUCTOR_207);
    }
    if (absent[208]) {
      manager.registerObjectType(ids[208], AuditClientEventTypeNode.class, CONSTRUCTOR_208);
    }
    if (absent[209]) {
      manager.registerObjectType(ids[209], ProgramTransitionEventTypeNode.class, CONSTRUCTOR_209);
    }
    if (absent[210]) {
      manager.registerObjectType(ids[210], SubscribedDataSetFolderTypeNode.class, CONSTRUCTOR_210);
    }
    if (absent[211]) {
      manager.registerObjectType(
          ids[211], StandaloneSubscribedDataSetTypeNode.class, CONSTRUCTOR_211);
    }
    if (absent[212]) {
      manager.registerObjectType(ids[212], PubSubCapabilitiesTypeNode.class, CONSTRUCTOR_212);
    }
    if (absent[213]) {
      manager.registerObjectType(ids[213], ProgramStateMachineTypeNode.class, CONSTRUCTOR_213);
    }
    if (absent[214]) {
      manager.registerObjectType(
          ids[214], AuditClientUpdateMethodResultEventTypeNode.class, CONSTRUCTOR_214);
    }
    if (absent[215]) {
      manager.registerObjectType(
          ids[215], DatagramDataSetReaderTransportTypeNode.class, CONSTRUCTOR_215);
    }
    if (absent[216]) {
      manager.registerObjectType(
          ids[216], IIetfBaseNetworkInterfaceTypeNode.class, CONSTRUCTOR_216);
    }
    if (absent[217]) {
      manager.registerObjectType(ids[217], IIeeeBaseEthernetPortTypeNode.class, CONSTRUCTOR_217);
    }
    if (absent[218]) {
      manager.registerObjectType(
          ids[218], IBaseEthernetCapabilitiesTypeNode.class, CONSTRUCTOR_218);
    }
    if (absent[219]) {
      manager.registerObjectType(ids[219], ISrClassTypeNode.class, CONSTRUCTOR_219);
    }
    if (absent[220]) {
      manager.registerObjectType(ids[220], IIeeeBaseTsnStreamTypeNode.class, CONSTRUCTOR_220);
    }
    if (absent[221]) {
      manager.registerObjectType(
          ids[221], IIeeeBaseTsnTrafficSpecificationTypeNode.class, CONSTRUCTOR_221);
    }
    if (absent[222]) {
      manager.registerObjectType(ids[222], IIeeeBaseTsnStatusStreamTypeNode.class, CONSTRUCTOR_222);
    }
    if (absent[223]) {
      manager.registerObjectType(
          ids[223], IIeeeTsnInterfaceConfigurationTypeNode.class, CONSTRUCTOR_223);
    }
    if (absent[224]) {
      manager.registerObjectType(
          ids[224], IIeeeTsnInterfaceConfigurationTalkerTypeNode.class, CONSTRUCTOR_224);
    }
    if (absent[225]) {
      manager.registerObjectType(
          ids[225], IIeeeTsnInterfaceConfigurationListenerTypeNode.class, CONSTRUCTOR_225);
    }
    if (absent[226]) {
      manager.registerObjectType(ids[226], IIeeeTsnMacAddressTypeNode.class, CONSTRUCTOR_226);
    }
    if (absent[227]) {
      manager.registerObjectType(ids[227], IIeeeTsnVlanTagTypeNode.class, CONSTRUCTOR_227);
    }
    if (absent[228]) {
      manager.registerObjectType(ids[228], IPriorityMappingEntryTypeNode.class, CONSTRUCTOR_228);
    }
    if (absent[229]) {
      manager.registerObjectType(
          ids[229], IIeeeAutoNegotiationStatusTypeNode.class, CONSTRUCTOR_229);
    }
    if (absent[230]) {
      manager.registerObjectType(ids[230], UserManagementTypeNode.class, CONSTRUCTOR_230);
    }
    if (absent[231]) {
      manager.registerObjectType(ids[231], IVlanIdTypeNode.class, CONSTRUCTOR_231);
    }
    if (absent[232]) {
      manager.registerObjectType(ids[232], IetfBaseNetworkInterfaceTypeNode.class, CONSTRUCTOR_232);
    }
    if (absent[233]) {
      manager.registerObjectType(ids[233], PriorityMappingTableTypeNode.class, CONSTRUCTOR_233);
    }
    if (absent[234]) {
      manager.registerObjectType(ids[234], PubSubKeyPushTargetTypeNode.class, CONSTRUCTOR_234);
    }
    if (absent[235]) {
      manager.registerObjectType(
          ids[235], PubSubKeyPushTargetFolderTypeNode.class, CONSTRUCTOR_235);
    }
    if (absent[236]) {
      manager.registerObjectType(ids[236], PubSubConfigurationTypeNode.class, CONSTRUCTOR_236);
    }
    if (absent[237]) {
      manager.registerObjectType(ids[237], ApplicationConfigurationTypeNode.class, CONSTRUCTOR_237);
    }
    if (absent[238]) {
      manager.registerObjectType(ids[238], ProvisionableDeviceTypeNode.class, CONSTRUCTOR_238);
    }
    if (absent[239]) {
      manager.registerObjectType(ids[239], SemanticChangeEventTypeNode.class, CONSTRUCTOR_239);
    }
    if (absent[240]) {
      manager.registerObjectType(ids[240], AuditUrlMismatchEventTypeNode.class, CONSTRUCTOR_240);
    }
    if (absent[241]) {
      manager.registerObjectType(ids[241], RefreshStartEventTypeNode.class, CONSTRUCTOR_241);
    }
    if (absent[242]) {
      manager.registerObjectType(ids[242], RefreshEndEventTypeNode.class, CONSTRUCTOR_242);
    }
    if (absent[243]) {
      manager.registerObjectType(ids[243], RefreshRequiredEventTypeNode.class, CONSTRUCTOR_243);
    }
    if (absent[244]) {
      manager.registerObjectType(
          ids[244], AuditConditionEnableEventTypeNode.class, CONSTRUCTOR_244);
    }
    if (absent[245]) {
      manager.registerObjectType(
          ids[245], AuditConditionCommentEventTypeNode.class, CONSTRUCTOR_245);
    }
    if (absent[246]) {
      manager.registerObjectType(ids[246], DialogConditionTypeNode.class, CONSTRUCTOR_246);
    }
    if (absent[247]) {
      manager.registerObjectType(ids[247], ShelvedStateMachineTypeNode.class, CONSTRUCTOR_247);
    }
    if (absent[248]) {
      manager.registerObjectType(
          ids[248], AuditHistoryEventUpdateEventTypeNode.class, CONSTRUCTOR_248);
    }
    if (absent[249]) {
      manager.registerObjectType(
          ids[249], AuditHistoryValueUpdateEventTypeNode.class, CONSTRUCTOR_249);
    }
    if (absent[250]) {
      manager.registerObjectType(ids[250], AuditHistoryDeleteEventTypeNode.class, CONSTRUCTOR_250);
    }
    if (absent[251]) {
      manager.registerObjectType(
          ids[251], AuditHistoryRawModifyDeleteEventTypeNode.class, CONSTRUCTOR_251);
    }
    if (absent[252]) {
      manager.registerObjectType(
          ids[252], AuditHistoryAtTimeDeleteEventTypeNode.class, CONSTRUCTOR_252);
    }
    if (absent[253]) {
      manager.registerObjectType(
          ids[253], AuditHistoryEventDeleteEventTypeNode.class, CONSTRUCTOR_253);
    }
    if (absent[254]) {
      manager.registerObjectType(ids[254], EventQueueOverflowEventTypeNode.class, CONSTRUCTOR_254);
    }
    if (absent[255]) {
      manager.registerObjectType(ids[255], AlarmSuppressionGroupTypeNode.class, CONSTRUCTOR_255);
    }
  }

  private static void install2(ObjectTypeManager manager, NodeId[] ids, boolean[] absent) {
    if (absent[256]) {
      manager.registerObjectType(
          ids[256], TrustListUpdateRequestedAuditEventTypeNode.class, CONSTRUCTOR_256);
    }
    if (absent[257]) {
      manager.registerObjectType(ids[257], TransactionDiagnosticsTypeNode.class, CONSTRUCTOR_257);
    }
    if (absent[258]) {
      manager.registerObjectType(
          ids[258], CertificateUpdateRequestedAuditEventTypeNode.class, CONSTRUCTOR_258);
    }
    if (absent[259]) {
      manager.registerObjectType(
          ids[259], NonTransparentBackupRedundancyTypeNode.class, CONSTRUCTOR_259);
    }
    if (absent[260]) {
      manager.registerObjectType(ids[260], SyntaxReferenceEntryTypeNode.class, CONSTRUCTOR_260);
    }
    if (absent[261]) {
      manager.registerObjectType(ids[261], UnitTypeNode.class, CONSTRUCTOR_261);
    }
    if (absent[262]) {
      manager.registerObjectType(ids[262], ServerUnitTypeNode.class, CONSTRUCTOR_262);
    }
    if (absent[263]) {
      manager.registerObjectType(ids[263], AlternativeUnitTypeNode.class, CONSTRUCTOR_263);
    }
    if (absent[264]) {
      manager.registerObjectType(ids[264], QuantityTypeNode.class, CONSTRUCTOR_264);
    }
    if (absent[265]) {
      manager.registerObjectType(
          ids[265], HistoricalEventConfigurationTypeNode.class, CONSTRUCTOR_265);
    }
    if (absent[266]) {
      manager.registerObjectType(
          ids[266], HistoricalExternalEventSourceTypeNode.class, CONSTRUCTOR_266);
    }
    if (absent[267]) {
      manager.registerObjectType(
          ids[267], AuditHistoryConfigurationChangeEventTypeNode.class, CONSTRUCTOR_267);
    }
    if (absent[268]) {
      manager.registerObjectType(
          ids[268], AuditHistoryBulkInsertEventTypeNode.class, CONSTRUCTOR_268);
    }
    if (absent[269]) {
      manager.registerObjectType(
          ids[269], ProgramTransitionAuditEventTypeNode.class, CONSTRUCTOR_269);
    }
    if (absent[270]) {
      manager.registerObjectType(ids[270], DataTypeSystemTypeNode.class, CONSTRUCTOR_270);
    }
    if (absent[271]) {
      manager.registerObjectType(ids[271], DataTypeEncodingTypeNode.class, CONSTRUCTOR_271);
    }
    if (absent[272]) {
      manager.registerObjectType(ids[272], ModellingRuleTypeNode.class, CONSTRUCTOR_272);
    }
    if (absent[273]) {
      manager.registerObjectType(
          ids[273], AuditConditionRespondEventTypeNode.class, CONSTRUCTOR_273);
    }
    if (absent[274]) {
      manager.registerObjectType(
          ids[274], AuditConditionAcknowledgeEventTypeNode.class, CONSTRUCTOR_274);
    }
    if (absent[275]) {
      manager.registerObjectType(
          ids[275], AuditConditionConfirmEventTypeNode.class, CONSTRUCTOR_275);
    }
    if (absent[276]) {
      manager.registerObjectType(
          ids[276], ExclusiveLimitStateMachineTypeNode.class, CONSTRUCTOR_276);
    }
    if (absent[277]) {
      manager.registerObjectType(ids[277], ExclusiveLimitAlarmTypeNode.class, CONSTRUCTOR_277);
    }
    if (absent[278]) {
      manager.registerObjectType(ids[278], ExclusiveLevelAlarmTypeNode.class, CONSTRUCTOR_278);
    }
    if (absent[279]) {
      manager.registerObjectType(
          ids[279], ExclusiveRateOfChangeAlarmTypeNode.class, CONSTRUCTOR_279);
    }
    if (absent[280]) {
      manager.registerObjectType(ids[280], ExclusiveDeviationAlarmTypeNode.class, CONSTRUCTOR_280);
    }
  }
}

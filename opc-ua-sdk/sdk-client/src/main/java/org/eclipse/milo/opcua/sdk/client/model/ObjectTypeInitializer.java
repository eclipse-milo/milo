/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.model;

import org.eclipse.milo.opcua.sdk.client.ObjectTypeManager;
import org.eclipse.milo.opcua.sdk.client.model.objects.AcknowledgeableConditionTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AddressSpaceFileTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AggregateConfigurationTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AggregateFunctionTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AlarmConditionTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AlarmGroupTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AlarmMetricsTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AlarmSuppressionGroupTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AliasNameCategoryTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AliasNameTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AlternativeUnitTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.ApplicationCertificateTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.ApplicationConfigurationFolderTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.ApplicationConfigurationTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditActivateSessionEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditAddNodesEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditAddReferencesEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditCancelEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditCertificateDataMismatchEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditCertificateEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditCertificateExpiredEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditCertificateInvalidEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditCertificateMismatchEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditCertificateRevokedEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditCertificateUntrustedEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditChannelEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditClientEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditClientUpdateMethodResultEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditConditionAcknowledgeEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditConditionCommentEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditConditionConfirmEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditConditionEnableEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditConditionEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditConditionOutOfServiceEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditConditionResetEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditConditionRespondEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditConditionShelvingEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditConditionSilenceEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditConditionSuppressionEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditCreateSessionEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditDeleteNodesEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditDeleteReferencesEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditHistoryAnnotationUpdateEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditHistoryAtTimeDeleteEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditHistoryBulkInsertEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditHistoryConfigurationChangeEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditHistoryDeleteEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditHistoryEventDeleteEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditHistoryEventUpdateEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditHistoryRawModifyDeleteEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditHistoryUpdateEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditHistoryValueUpdateEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditNodeManagementEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditOpenSecureChannelEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditProgramTransitionEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditSecurityEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditSessionEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditUpdateEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditUpdateMethodEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditUpdateStateEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditUrlMismatchEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuditWriteUpdateEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuthorizationServiceConfigurationTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.AuthorizationServicesConfigurationFolderTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.BaseConditionClassTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.BaseEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.BaseInterfaceTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.BaseModelChangeEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.BaseObjectTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.BrokerConnectionTransportTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.BrokerDataSetReaderTransportTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.BrokerDataSetWriterTransportTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.BrokerWriterGroupTransportTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.CertificateExpirationAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.CertificateGroupFolderTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.CertificateGroupTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.CertificateTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.CertificateUpdateRequestedAuditEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.CertificateUpdatedAuditEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.ChoiceStateTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.ConditionTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.ConnectionTransportTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.DataSetFolderTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.DataSetReaderMessageTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.DataSetReaderTransportTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.DataSetReaderTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.DataSetWriterMessageTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.DataSetWriterTransportTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.DataSetWriterTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.DataTypeEncodingTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.DataTypeSystemTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.DatagramConnectionTransportTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.DatagramDataSetReaderTransportTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.DatagramWriterGroupTransportTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.DeviceFailureEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.DialogConditionTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.DictionaryEntryTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.DictionaryFolderTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.DiscrepancyAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.DiscreteAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.EccApplicationCertificateTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.EccBrainpoolP256r1ApplicationCertificateTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.EccBrainpoolP384r1ApplicationCertificateTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.EccCurve25519ApplicationCertificateTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.EccCurve448ApplicationCertificateTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.EccNistP256ApplicationCertificateTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.EccNistP384ApplicationCertificateTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.EventQueueOverflowEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.ExclusiveDeviationAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.ExclusiveLevelAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.ExclusiveLimitAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.ExclusiveLimitStateMachineTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.ExclusiveRateOfChangeAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.ExtensionFieldsTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.FileDirectoryTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.FileTransferStateMachineTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.FileTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.FiniteStateMachineTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.FolderTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.GeneralModelChangeEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.HighlyManagedAlarmConditionClassTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.HistoricalDataConfigurationTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.HistoricalEventConfigurationTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.HistoricalExternalEventSourceTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.HistoryServerCapabilitiesTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.HttpsCertificateTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.IBaseEthernetCapabilitiesTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.IIeeeAutoNegotiationStatusTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.IIeeeBaseEthernetPortTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.IIeeeBaseTsnStatusStreamTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.IIeeeBaseTsnStreamTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.IIeeeBaseTsnTrafficSpecificationTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.IIeeeTsnInterfaceConfigurationListenerTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.IIeeeTsnInterfaceConfigurationTalkerTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.IIeeeTsnInterfaceConfigurationTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.IIeeeTsnMacAddressTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.IIeeeTsnVlanTagTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.IIetfBaseNetworkInterfaceTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.IOrderedObjectTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.IPriorityMappingEntryTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.ISrClassTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.IVlanIdTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.IetfBaseNetworkInterfaceTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.InitialStateTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.InstrumentDiagnosticAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.IrdiDictionaryEntryTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.JsonDataSetReaderMessageTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.JsonDataSetWriterMessageTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.JsonWriterGroupMessageTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.KeyCredentialAuditEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.KeyCredentialConfigurationFolderTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.KeyCredentialConfigurationTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.KeyCredentialDeletedAuditEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.KeyCredentialUpdatedAuditEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.LimitAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.LldpInformationTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.LldpLocalSystemTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.LldpPortInformationTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.LldpRemoteStatisticsTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.LldpRemoteSystemTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.MaintenanceConditionClassTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.ModellingRuleTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.NamespaceMetadataTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.NamespacesTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.NetworkAddressTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.NetworkAddressUrlTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.NonExclusiveDeviationAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.NonExclusiveLevelAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.NonExclusiveLimitAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.NonExclusiveRateOfChangeAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.NonTransparentBackupRedundancyTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.NonTransparentNetworkRedundancyTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.NonTransparentRedundancyTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.OffNormalAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.OperationLimitsTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.OrderedListTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.PriorityMappingTableTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.ProcessConditionClassTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.ProgramStateMachineTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.ProgramTransitionAuditEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.ProgramTransitionEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.ProgressEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.ProvisionableDeviceTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.PubSubCapabilitiesTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.PubSubCommunicationFailureEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.PubSubConfigurationTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.PubSubConnectionTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.PubSubDiagnosticsConnectionTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.PubSubDiagnosticsDataSetReaderTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.PubSubDiagnosticsDataSetWriterTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.PubSubDiagnosticsReaderGroupTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.PubSubDiagnosticsRootTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.PubSubDiagnosticsTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.PubSubDiagnosticsWriterGroupTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.PubSubGroupTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.PubSubKeyPushTargetFolderTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.PubSubKeyPushTargetTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.PubSubKeyServiceTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.PubSubStatusEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.PubSubStatusTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.PubSubTransportLimitsExceedEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.PublishSubscribeTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.PublishedDataItemsTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.PublishedDataSetTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.PublishedEventsTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.QuantitiesFolderTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.QuantityTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.ReaderGroupMessageTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.ReaderGroupTransportTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.ReaderGroupTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.RefreshEndEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.RefreshRequiredEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.RefreshStartEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.RoleMappingRuleChangedAuditEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.RoleSetTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.RoleTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.RsaMinApplicationCertificateTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.RsaSha256ApplicationCertificateTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.SafetyConditionClassTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.SecurityGroupFolderTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.SecurityGroupTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.SemanticChangeEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.ServerCapabilitiesTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.ServerConfigurationTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.ServerDiagnosticsTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.ServerRedundancyTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.ServerTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.ServerUnitTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.SessionDiagnosticsObjectTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.SessionsDiagnosticsSummaryTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.ShelvedStateMachineTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.StandaloneSubscribedDataSetTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.StateMachineTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.StateTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.StatisticalConditionClassTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.SubscribedDataSetFolderTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.SubscribedDataSetMirrorTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.SubscribedDataSetTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.SyntaxReferenceEntryTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.SystemConditionClassTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.SystemDiagnosticAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.SystemEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.SystemOffNormalAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.SystemStatusChangeEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.TargetVariablesTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.TemporaryFileTransferTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.TestingConditionClassTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.TrainingConditionClassTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.TransactionDiagnosticsTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.TransitionEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.TransitionTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.TransparentRedundancyTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.TripAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.TrustListOutOfDateAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.TrustListTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.TrustListUpdateRequestedAuditEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.TrustListUpdatedAuditEventTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.UadpDataSetReaderMessageTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.UadpDataSetWriterMessageTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.UadpWriterGroupMessageTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.UnitTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.UriDictionaryEntryTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.UserCredentialCertificateTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.UserManagementTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.VendorServerInfoTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.WriterGroupMessageTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.WriterGroupTransportTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.objects.WriterGroupTypeNode;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;

public class ObjectTypeInitializer {
  public static void initialize(
      NamespaceTable namespaceTable, ObjectTypeManager objectTypeManager) {
    objectTypeManager.registerObjectType(
        NodeId.parse("i=58").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        BaseObjectTypeNode.class,
        BaseObjectTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=61").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        FolderTypeNode.class,
        FolderTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=11564").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        OperationLimitsTypeNode.class,
        OperationLimitsTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=13353").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        FileDirectoryTypeNode.class,
        FileDirectoryTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=17591").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        DictionaryFolderTypeNode.class,
        DictionaryFolderTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=32502").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        QuantitiesFolderTypeNode.class,
        QuantitiesFolderTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=16405").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        AlarmGroupTypeNode.class,
        AlarmGroupTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=32064").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        AlarmSuppressionGroupTypeNode.class,
        AlarmSuppressionGroupTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=13813").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        CertificateGroupFolderTypeNode.class,
        CertificateGroupFolderTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=16662").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        ApplicationConfigurationFolderTypeNode.class,
        ApplicationConfigurationFolderTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=17496").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        KeyCredentialConfigurationFolderTypeNode.class,
        KeyCredentialConfigurationFolderTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=23556").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        AuthorizationServicesConfigurationFolderTypeNode.class,
        AuthorizationServicesConfigurationFolderTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=15452").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        SecurityGroupFolderTypeNode.class,
        SecurityGroupFolderTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=25346").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        PubSubKeyPushTargetFolderTypeNode.class,
        PubSubKeyPushTargetFolderTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=14477").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        DataSetFolderTypeNode.class,
        DataSetFolderTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=23795").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        SubscribedDataSetFolderTypeNode.class,
        SubscribedDataSetFolderTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=23456").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        AliasNameCategoryTypeNode.class,
        AliasNameCategoryTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=75").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        DataTypeSystemTypeNode.class,
        DataTypeSystemTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=76").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        DataTypeEncodingTypeNode.class,
        DataTypeEncodingTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=77").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        ModellingRuleTypeNode.class,
        ModellingRuleTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=2004").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        ServerTypeNode.class,
        ServerTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=2013").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        ServerCapabilitiesTypeNode.class,
        ServerCapabilitiesTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=2020").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        ServerDiagnosticsTypeNode.class,
        ServerDiagnosticsTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=2026").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        SessionsDiagnosticsSummaryTypeNode.class,
        SessionsDiagnosticsSummaryTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=2029").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        SessionDiagnosticsObjectTypeNode.class,
        SessionDiagnosticsObjectTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=2033").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        VendorServerInfoTypeNode.class,
        VendorServerInfoTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=2034").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        ServerRedundancyTypeNode.class,
        ServerRedundancyTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=2036").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        TransparentRedundancyTypeNode.class,
        TransparentRedundancyTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=2039").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        NonTransparentRedundancyTypeNode.class,
        NonTransparentRedundancyTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=11945").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        NonTransparentNetworkRedundancyTypeNode.class,
        NonTransparentNetworkRedundancyTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=32411").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        NonTransparentBackupRedundancyTypeNode.class,
        NonTransparentBackupRedundancyTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=11575").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        FileTypeNode.class,
        FileTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=11595").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        AddressSpaceFileTypeNode.class,
        AddressSpaceFileTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=12522").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        TrustListTypeNode.class,
        TrustListTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=25482").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        PubSubConfigurationTypeNode.class,
        PubSubConfigurationTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=11616").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        NamespaceMetadataTypeNode.class,
        NamespaceMetadataTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=11645").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        NamespacesTypeNode.class,
        NamespacesTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=2041").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        BaseEventTypeNode.class,
        BaseEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=2052").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        AuditEventTypeNode.class,
        AuditEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=2058").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        AuditSecurityEventTypeNode.class,
        AuditSecurityEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=2059").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        AuditChannelEventTypeNode.class,
        AuditChannelEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=2060").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        AuditOpenSecureChannelEventTypeNode.class,
        AuditOpenSecureChannelEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=2069").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        AuditSessionEventTypeNode.class,
        AuditSessionEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=2071").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        AuditCreateSessionEventTypeNode.class,
        AuditCreateSessionEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=2748").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        AuditUrlMismatchEventTypeNode.class,
        AuditUrlMismatchEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=2075").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        AuditActivateSessionEventTypeNode.class,
        AuditActivateSessionEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=2078").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        AuditCancelEventTypeNode.class,
        AuditCancelEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=2080").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        AuditCertificateEventTypeNode.class,
        AuditCertificateEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=2082").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        AuditCertificateDataMismatchEventTypeNode.class,
        AuditCertificateDataMismatchEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=2085").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        AuditCertificateExpiredEventTypeNode.class,
        AuditCertificateExpiredEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=2086").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        AuditCertificateInvalidEventTypeNode.class,
        AuditCertificateInvalidEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=2087").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        AuditCertificateUntrustedEventTypeNode.class,
        AuditCertificateUntrustedEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=2088").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        AuditCertificateRevokedEventTypeNode.class,
        AuditCertificateRevokedEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=2089").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        AuditCertificateMismatchEventTypeNode.class,
        AuditCertificateMismatchEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=2090").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        AuditNodeManagementEventTypeNode.class,
        AuditNodeManagementEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=2091").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        AuditAddNodesEventTypeNode.class,
        AuditAddNodesEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=2093").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        AuditDeleteNodesEventTypeNode.class,
        AuditDeleteNodesEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=2095").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        AuditAddReferencesEventTypeNode.class,
        AuditAddReferencesEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=2097").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        AuditDeleteReferencesEventTypeNode.class,
        AuditDeleteReferencesEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=2099").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        AuditUpdateEventTypeNode.class,
        AuditUpdateEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=2100").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        AuditWriteUpdateEventTypeNode.class,
        AuditWriteUpdateEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=2104").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        AuditHistoryUpdateEventTypeNode.class,
        AuditHistoryUpdateEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=2999").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        AuditHistoryEventUpdateEventTypeNode.class,
        AuditHistoryEventUpdateEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=3006").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        AuditHistoryValueUpdateEventTypeNode.class,
        AuditHistoryValueUpdateEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=19095").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        AuditHistoryAnnotationUpdateEventTypeNode.class,
        AuditHistoryAnnotationUpdateEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=3012").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        AuditHistoryDeleteEventTypeNode.class,
        AuditHistoryDeleteEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=3014").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        AuditHistoryRawModifyDeleteEventTypeNode.class,
        AuditHistoryRawModifyDeleteEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=3019").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        AuditHistoryAtTimeDeleteEventTypeNode.class,
        AuditHistoryAtTimeDeleteEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=3022").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        AuditHistoryEventDeleteEventTypeNode.class,
        AuditHistoryEventDeleteEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=2127").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        AuditUpdateMethodEventTypeNode.class,
        AuditUpdateMethodEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=2315").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        AuditUpdateStateEventTypeNode.class,
        AuditUpdateStateEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=11856").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        AuditProgramTransitionEventTypeNode.class,
        AuditProgramTransitionEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=3806").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        ProgramTransitionAuditEventTypeNode.class,
        ProgramTransitionAuditEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=17641").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        RoleMappingRuleChangedAuditEventTypeNode.class,
        RoleMappingRuleChangedAuditEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=2790").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        AuditConditionEventTypeNode.class,
        AuditConditionEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=2803").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        AuditConditionEnableEventTypeNode.class,
        AuditConditionEnableEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=2829").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        AuditConditionCommentEventTypeNode.class,
        AuditConditionCommentEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=8927").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        AuditConditionRespondEventTypeNode.class,
        AuditConditionRespondEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=8944").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        AuditConditionAcknowledgeEventTypeNode.class,
        AuditConditionAcknowledgeEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=8961").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        AuditConditionConfirmEventTypeNode.class,
        AuditConditionConfirmEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=11093").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        AuditConditionShelvingEventTypeNode.class,
        AuditConditionShelvingEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=17225").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        AuditConditionSuppressionEventTypeNode.class,
        AuditConditionSuppressionEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=17242").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        AuditConditionSilenceEventTypeNode.class,
        AuditConditionSilenceEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=15013").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        AuditConditionResetEventTypeNode.class,
        AuditConditionResetEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=17259").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        AuditConditionOutOfServiceEventTypeNode.class,
        AuditConditionOutOfServiceEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=32260").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        TrustListUpdateRequestedAuditEventTypeNode.class,
        TrustListUpdateRequestedAuditEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=12561").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        TrustListUpdatedAuditEventTypeNode.class,
        TrustListUpdatedAuditEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=32306").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        CertificateUpdateRequestedAuditEventTypeNode.class,
        CertificateUpdateRequestedAuditEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=12620").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        CertificateUpdatedAuditEventTypeNode.class,
        CertificateUpdatedAuditEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=18011").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        KeyCredentialAuditEventTypeNode.class,
        KeyCredentialAuditEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=18029").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        KeyCredentialUpdatedAuditEventTypeNode.class,
        KeyCredentialUpdatedAuditEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=18047").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        KeyCredentialDeletedAuditEventTypeNode.class,
        KeyCredentialDeletedAuditEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=23606").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        AuditClientEventTypeNode.class,
        AuditClientEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=23926").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        AuditClientUpdateMethodResultEventTypeNode.class,
        AuditClientUpdateMethodResultEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=32758").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        AuditHistoryConfigurationChangeEventTypeNode.class,
        AuditHistoryConfigurationChangeEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=32803").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        AuditHistoryBulkInsertEventTypeNode.class,
        AuditHistoryBulkInsertEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=2130").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        SystemEventTypeNode.class,
        SystemEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=2131").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        DeviceFailureEventTypeNode.class,
        DeviceFailureEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=11446").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        SystemStatusChangeEventTypeNode.class,
        SystemStatusChangeEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=2787").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        RefreshStartEventTypeNode.class,
        RefreshStartEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=2788").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        RefreshEndEventTypeNode.class,
        RefreshEndEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=2789").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        RefreshRequiredEventTypeNode.class,
        RefreshRequiredEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=15535").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        PubSubStatusEventTypeNode.class,
        PubSubStatusEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=15548").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        PubSubTransportLimitsExceedEventTypeNode.class,
        PubSubTransportLimitsExceedEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=15563").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        PubSubCommunicationFailureEventTypeNode.class,
        PubSubCommunicationFailureEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=2132").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        BaseModelChangeEventTypeNode.class,
        BaseModelChangeEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=2133").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        GeneralModelChangeEventTypeNode.class,
        GeneralModelChangeEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=2738").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        SemanticChangeEventTypeNode.class,
        SemanticChangeEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=3035").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        EventQueueOverflowEventTypeNode.class,
        EventQueueOverflowEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=11436").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        ProgressEventTypeNode.class,
        ProgressEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=2311").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        TransitionEventTypeNode.class,
        TransitionEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=2378").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        ProgramTransitionEventTypeNode.class,
        ProgramTransitionEventTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=2782").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        ConditionTypeNode.class,
        ConditionTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=2830").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        DialogConditionTypeNode.class,
        DialogConditionTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=2881").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        AcknowledgeableConditionTypeNode.class,
        AcknowledgeableConditionTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=2915").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        AlarmConditionTypeNode.class,
        AlarmConditionTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=2955").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        LimitAlarmTypeNode.class,
        LimitAlarmTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=9341").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        ExclusiveLimitAlarmTypeNode.class,
        ExclusiveLimitAlarmTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=9482").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        ExclusiveLevelAlarmTypeNode.class,
        ExclusiveLevelAlarmTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=9764").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        ExclusiveDeviationAlarmTypeNode.class,
        ExclusiveDeviationAlarmTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=9623").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        ExclusiveRateOfChangeAlarmTypeNode.class,
        ExclusiveRateOfChangeAlarmTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=9906").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        NonExclusiveLimitAlarmTypeNode.class,
        NonExclusiveLimitAlarmTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=10060").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        NonExclusiveLevelAlarmTypeNode.class,
        NonExclusiveLevelAlarmTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=10368").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        NonExclusiveDeviationAlarmTypeNode.class,
        NonExclusiveDeviationAlarmTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=10214").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        NonExclusiveRateOfChangeAlarmTypeNode.class,
        NonExclusiveRateOfChangeAlarmTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=10523").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        DiscreteAlarmTypeNode.class,
        DiscreteAlarmTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=10637").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        OffNormalAlarmTypeNode.class,
        OffNormalAlarmTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=11753").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        SystemOffNormalAlarmTypeNode.class,
        SystemOffNormalAlarmTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=13225").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        CertificateExpirationAlarmTypeNode.class,
        CertificateExpirationAlarmTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=19297").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        TrustListOutOfDateAlarmTypeNode.class,
        TrustListOutOfDateAlarmTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=10751").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        TripAlarmTypeNode.class,
        TripAlarmTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=18347").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        InstrumentDiagnosticAlarmTypeNode.class,
        InstrumentDiagnosticAlarmTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=18496").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        SystemDiagnosticAlarmTypeNode.class,
        SystemDiagnosticAlarmTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=17080").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        DiscrepancyAlarmTypeNode.class,
        DiscrepancyAlarmTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=2340").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        AggregateFunctionTypeNode.class,
        AggregateFunctionTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=2299").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        StateMachineTypeNode.class,
        StateMachineTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=2771").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        FiniteStateMachineTypeNode.class,
        FiniteStateMachineTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=15803").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        FileTransferStateMachineTypeNode.class,
        FileTransferStateMachineTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=2929").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        ShelvedStateMachineTypeNode.class,
        ShelvedStateMachineTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=9318").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        ExclusiveLimitStateMachineTypeNode.class,
        ExclusiveLimitStateMachineTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=2391").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        ProgramStateMachineTypeNode.class,
        ProgramStateMachineTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=2307").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        StateTypeNode.class,
        StateTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=2309").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        InitialStateTypeNode.class,
        InitialStateTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=15109").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        ChoiceStateTypeNode.class,
        ChoiceStateTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=2310").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        TransitionTypeNode.class,
        TransitionTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=15744").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        TemporaryFileTransferTypeNode.class,
        TemporaryFileTransferTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=15607").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        RoleSetTypeNode.class,
        RoleSetTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=15620").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        RoleTypeNode.class,
        RoleTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=17589").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        DictionaryEntryTypeNode.class,
        DictionaryEntryTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=17598").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        IrdiDictionaryEntryTypeNode.class,
        IrdiDictionaryEntryTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=17600").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        UriDictionaryEntryTypeNode.class,
        UriDictionaryEntryTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=32439").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        SyntaxReferenceEntryTypeNode.class,
        SyntaxReferenceEntryTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=17602").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        BaseInterfaceTypeNode.class,
        BaseInterfaceTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=23513").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        IOrderedObjectTypeNode.class,
        IOrderedObjectTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=24148").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        IIetfBaseNetworkInterfaceTypeNode.class,
        IIetfBaseNetworkInterfaceTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=24158").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        IIeeeBaseEthernetPortTypeNode.class,
        IIeeeBaseEthernetPortTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=24233").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        IIeeeAutoNegotiationStatusTypeNode.class,
        IIeeeAutoNegotiationStatusTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=24167").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        IBaseEthernetCapabilitiesTypeNode.class,
        IBaseEthernetCapabilitiesTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=25218").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        IVlanIdTypeNode.class,
        IVlanIdTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=24169").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        ISrClassTypeNode.class,
        ISrClassTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=24173").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        IIeeeBaseTsnStreamTypeNode.class,
        IIeeeBaseTsnStreamTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=24179").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        IIeeeBaseTsnTrafficSpecificationTypeNode.class,
        IIeeeBaseTsnTrafficSpecificationTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=24183").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        IIeeeBaseTsnStatusStreamTypeNode.class,
        IIeeeBaseTsnStatusStreamTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=24188").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        IIeeeTsnInterfaceConfigurationTypeNode.class,
        IIeeeTsnInterfaceConfigurationTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=24191").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        IIeeeTsnInterfaceConfigurationTalkerTypeNode.class,
        IIeeeTsnInterfaceConfigurationTalkerTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=24195").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        IIeeeTsnInterfaceConfigurationListenerTypeNode.class,
        IIeeeTsnInterfaceConfigurationListenerTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=24199").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        IIeeeTsnMacAddressTypeNode.class,
        IIeeeTsnMacAddressTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=24202").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        IIeeeTsnVlanTagTypeNode.class,
        IIeeeTsnVlanTagTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=24205").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        IPriorityMappingEntryTypeNode.class,
        IPriorityMappingEntryTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=23518").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        OrderedListTypeNode.class,
        OrderedListTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=32442").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        UnitTypeNode.class,
        UnitTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=32447").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        ServerUnitTypeNode.class,
        ServerUnitTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=32467").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        AlternativeUnitTypeNode.class,
        AlternativeUnitTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=32475").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        QuantityTypeNode.class,
        QuantityTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=11163").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        BaseConditionClassTypeNode.class,
        BaseConditionClassTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=11164").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        ProcessConditionClassTypeNode.class,
        ProcessConditionClassTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=11165").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        MaintenanceConditionClassTypeNode.class,
        MaintenanceConditionClassTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=11166").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        SystemConditionClassTypeNode.class,
        SystemConditionClassTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=17218").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        SafetyConditionClassTypeNode.class,
        SafetyConditionClassTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=17219").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        HighlyManagedAlarmConditionClassTypeNode.class,
        HighlyManagedAlarmConditionClassTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=17220").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        TrainingConditionClassTypeNode.class,
        TrainingConditionClassTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=18665").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        StatisticalConditionClassTypeNode.class,
        StatisticalConditionClassTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=17221").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        TestingConditionClassTypeNode.class,
        TestingConditionClassTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=17279").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        AlarmMetricsTypeNode.class,
        AlarmMetricsTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=2318").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        HistoricalDataConfigurationTypeNode.class,
        HistoricalDataConfigurationTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=32621").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        HistoricalEventConfigurationTypeNode.class,
        HistoricalEventConfigurationTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=32625").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        HistoricalExternalEventSourceTypeNode.class,
        HistoricalExternalEventSourceTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=2330").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        HistoryServerCapabilitiesTypeNode.class,
        HistoryServerCapabilitiesTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=12555").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        CertificateGroupTypeNode.class,
        CertificateGroupTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=12556").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        CertificateTypeNode.class,
        CertificateTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=12557").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        ApplicationCertificateTypeNode.class,
        ApplicationCertificateTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=12559").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        RsaMinApplicationCertificateTypeNode.class,
        RsaMinApplicationCertificateTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=12560").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        RsaSha256ApplicationCertificateTypeNode.class,
        RsaSha256ApplicationCertificateTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=23537").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        EccApplicationCertificateTypeNode.class,
        EccApplicationCertificateTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=23538").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        EccNistP256ApplicationCertificateTypeNode.class,
        EccNistP256ApplicationCertificateTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=23539").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        EccNistP384ApplicationCertificateTypeNode.class,
        EccNistP384ApplicationCertificateTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=23540").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        EccBrainpoolP256r1ApplicationCertificateTypeNode.class,
        EccBrainpoolP256r1ApplicationCertificateTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=23541").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        EccBrainpoolP384r1ApplicationCertificateTypeNode.class,
        EccBrainpoolP384r1ApplicationCertificateTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=23542").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        EccCurve25519ApplicationCertificateTypeNode.class,
        EccCurve25519ApplicationCertificateTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=23543").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        EccCurve448ApplicationCertificateTypeNode.class,
        EccCurve448ApplicationCertificateTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=12558").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        HttpsCertificateTypeNode.class,
        HttpsCertificateTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=15181").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        UserCredentialCertificateTypeNode.class,
        UserCredentialCertificateTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=32286").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        TransactionDiagnosticsTypeNode.class,
        TransactionDiagnosticsTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=12581").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        ServerConfigurationTypeNode.class,
        ServerConfigurationTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=25731").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        ApplicationConfigurationTypeNode.class,
        ApplicationConfigurationTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=18001").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        KeyCredentialConfigurationTypeNode.class,
        KeyCredentialConfigurationTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=17852").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        AuthorizationServiceConfigurationTypeNode.class,
        AuthorizationServiceConfigurationTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=11187").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        AggregateConfigurationTypeNode.class,
        AggregateConfigurationTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=15906").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        PubSubKeyServiceTypeNode.class,
        PubSubKeyServiceTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=14416").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        PublishSubscribeTypeNode.class,
        PublishSubscribeTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=15471").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        SecurityGroupTypeNode.class,
        SecurityGroupTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=25337").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        PubSubKeyPushTargetTypeNode.class,
        PubSubKeyPushTargetTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=14509").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        PublishedDataSetTypeNode.class,
        PublishedDataSetTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=14534").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        PublishedDataItemsTypeNode.class,
        PublishedDataItemsTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=14572").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        PublishedEventsTypeNode.class,
        PublishedEventsTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=15489").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        ExtensionFieldsTypeNode.class,
        ExtensionFieldsTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=14209").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        PubSubConnectionTypeNode.class,
        PubSubConnectionTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=17721").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        ConnectionTransportTypeNode.class,
        ConnectionTransportTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=15064").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        DatagramConnectionTransportTypeNode.class,
        DatagramConnectionTransportTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=15155").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        BrokerConnectionTransportTypeNode.class,
        BrokerConnectionTransportTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=14232").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        PubSubGroupTypeNode.class,
        PubSubGroupTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=17725").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        WriterGroupTypeNode.class,
        WriterGroupTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=17999").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        ReaderGroupTypeNode.class,
        ReaderGroupTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=17997").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        WriterGroupTransportTypeNode.class,
        WriterGroupTransportTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=21133").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        DatagramWriterGroupTransportTypeNode.class,
        DatagramWriterGroupTransportTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=21136").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        BrokerWriterGroupTransportTypeNode.class,
        BrokerWriterGroupTransportTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=17998").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        WriterGroupMessageTypeNode.class,
        WriterGroupMessageTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=21105").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        UadpWriterGroupMessageTypeNode.class,
        UadpWriterGroupMessageTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=21126").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        JsonWriterGroupMessageTypeNode.class,
        JsonWriterGroupMessageTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=21090").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        ReaderGroupTransportTypeNode.class,
        ReaderGroupTransportTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=21091").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        ReaderGroupMessageTypeNode.class,
        ReaderGroupMessageTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=15298").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        DataSetWriterTypeNode.class,
        DataSetWriterTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=15305").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        DataSetWriterTransportTypeNode.class,
        DataSetWriterTransportTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=21138").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        BrokerDataSetWriterTransportTypeNode.class,
        BrokerDataSetWriterTransportTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=21096").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        DataSetWriterMessageTypeNode.class,
        DataSetWriterMessageTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=21111").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        UadpDataSetWriterMessageTypeNode.class,
        UadpDataSetWriterMessageTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=21128").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        JsonDataSetWriterMessageTypeNode.class,
        JsonDataSetWriterMessageTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=15306").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        DataSetReaderTypeNode.class,
        DataSetReaderTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=15319").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        DataSetReaderTransportTypeNode.class,
        DataSetReaderTransportTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=24016").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        DatagramDataSetReaderTransportTypeNode.class,
        DatagramDataSetReaderTransportTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=21142").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        BrokerDataSetReaderTransportTypeNode.class,
        BrokerDataSetReaderTransportTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=21104").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        DataSetReaderMessageTypeNode.class,
        DataSetReaderMessageTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=21116").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        UadpDataSetReaderMessageTypeNode.class,
        UadpDataSetReaderMessageTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=21130").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        JsonDataSetReaderMessageTypeNode.class,
        JsonDataSetReaderMessageTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=15108").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        SubscribedDataSetTypeNode.class,
        SubscribedDataSetTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=15111").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        TargetVariablesTypeNode.class,
        TargetVariablesTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=15127").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        SubscribedDataSetMirrorTypeNode.class,
        SubscribedDataSetMirrorTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=23828").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        StandaloneSubscribedDataSetTypeNode.class,
        StandaloneSubscribedDataSetTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=14643").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        PubSubStatusTypeNode.class,
        PubSubStatusTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=19677").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        PubSubDiagnosticsTypeNode.class,
        PubSubDiagnosticsTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=19732").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        PubSubDiagnosticsRootTypeNode.class,
        PubSubDiagnosticsRootTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=19786").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        PubSubDiagnosticsConnectionTypeNode.class,
        PubSubDiagnosticsConnectionTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=19834").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        PubSubDiagnosticsWriterGroupTypeNode.class,
        PubSubDiagnosticsWriterGroupTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=19903").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        PubSubDiagnosticsReaderGroupTypeNode.class,
        PubSubDiagnosticsReaderGroupTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=19968").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        PubSubDiagnosticsDataSetWriterTypeNode.class,
        PubSubDiagnosticsDataSetWriterTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=20027").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        PubSubDiagnosticsDataSetReaderTypeNode.class,
        PubSubDiagnosticsDataSetReaderTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=23832").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        PubSubCapabilitiesTypeNode.class,
        PubSubCapabilitiesTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=21145").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        NetworkAddressTypeNode.class,
        NetworkAddressTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=21147").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        NetworkAddressUrlTypeNode.class,
        NetworkAddressUrlTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=23455").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        AliasNameTypeNode.class,
        AliasNameTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=24264").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        UserManagementTypeNode.class,
        UserManagementTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=26871").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        ProvisionableDeviceTypeNode.class,
        ProvisionableDeviceTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=25221").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        IetfBaseNetworkInterfaceTypeNode.class,
        IetfBaseNetworkInterfaceTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=25227").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        PriorityMappingTableTypeNode.class,
        PriorityMappingTableTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=18973").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        LldpInformationTypeNode.class,
        LldpInformationTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=18996").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        LldpRemoteStatisticsTypeNode.class,
        LldpRemoteStatisticsTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=19002").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        LldpLocalSystemTypeNode.class,
        LldpLocalSystemTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=19009").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        LldpPortInformationTypeNode.class,
        LldpPortInformationTypeNode::new);
    objectTypeManager.registerObjectType(
        NodeId.parse("i=19033").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        LldpRemoteSystemTypeNode.class,
        LldpRemoteSystemTypeNode::new);
  }
}

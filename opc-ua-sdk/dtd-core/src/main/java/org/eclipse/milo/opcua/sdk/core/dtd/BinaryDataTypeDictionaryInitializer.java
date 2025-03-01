package org.eclipse.milo.opcua.sdk.core.dtd;

import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.types.DataTypeDictionary;
import org.eclipse.milo.opcua.stack.core.types.structured.ActionMethodDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.ActionTargetDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.ActivateSessionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.ActivateSessionResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.AddNodesItem;
import org.eclipse.milo.opcua.stack.core.types.structured.AddNodesRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.AddNodesResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.AddNodesResult;
import org.eclipse.milo.opcua.stack.core.types.structured.AddReferencesItem;
import org.eclipse.milo.opcua.stack.core.types.structured.AddReferencesRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.AddReferencesResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.AdditionalParametersType;
import org.eclipse.milo.opcua.stack.core.types.structured.AggregateConfiguration;
import org.eclipse.milo.opcua.stack.core.types.structured.AggregateFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.AggregateFilterResult;
import org.eclipse.milo.opcua.stack.core.types.structured.AliasNameDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.Annotation;
import org.eclipse.milo.opcua.stack.core.types.structured.AnnotationDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.AnonymousIdentityToken;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.AttributeOperand;
import org.eclipse.milo.opcua.stack.core.types.structured.AxisInformation;
import org.eclipse.milo.opcua.stack.core.types.structured.BitFieldDefinition;
import org.eclipse.milo.opcua.stack.core.types.structured.BrokerConnectionTransportDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.BrokerDataSetReaderTransportDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.BrokerDataSetWriterTransportDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.BrokerWriterGroupTransportDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseNextRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseNextResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowsePath;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowsePathResult;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowsePathTarget;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseResult;
import org.eclipse.milo.opcua.stack.core.types.structured.BuildInfo;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.eclipse.milo.opcua.stack.core.types.structured.CallRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.CancelRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CancelResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ChannelSecurityToken;
import org.eclipse.milo.opcua.stack.core.types.structured.CloseSecureChannelRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CloseSecureChannelResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.CloseSessionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CloseSessionResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ComplexNumberType;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilterElement;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilterElementResult;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilterResult;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateMonitoredItemsRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateMonitoredItemsResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateSessionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateSessionResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateSubscriptionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateSubscriptionResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.CurrencyUnitType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataChangeFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.DataChangeNotification;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetReaderDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetWriterDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataTypeAttributes;
import org.eclipse.milo.opcua.stack.core.types.structured.DataTypeNode;
import org.eclipse.milo.opcua.stack.core.types.structured.DatagramConnectionTransport2DataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DatagramConnectionTransportDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DatagramDataSetReaderTransportDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DatagramWriterGroupTransport2DataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DatagramWriterGroupTransportDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DecimalDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteAtTimeDetails;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteEventDetails;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteMonitoredItemsRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteMonitoredItemsResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteNodesItem;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteNodesRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteNodesResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteRawModifiedDetails;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteReferencesItem;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteReferencesRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteReferencesResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteSubscriptionsRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteSubscriptionsResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.DiscoveryConfiguration;
import org.eclipse.milo.opcua.stack.core.types.structured.DoubleComplexNumberType;
import org.eclipse.milo.opcua.stack.core.types.structured.DtlsPubSubConnectionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.EUInformation;
import org.eclipse.milo.opcua.stack.core.types.structured.ElementOperand;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointConfiguration;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointType;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointUrlListDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.EnumDefinition;
import org.eclipse.milo.opcua.stack.core.types.structured.EnumDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.EnumField;
import org.eclipse.milo.opcua.stack.core.types.structured.EnumValueType;
import org.eclipse.milo.opcua.stack.core.types.structured.EphemeralKeyType;
import org.eclipse.milo.opcua.stack.core.types.structured.EventFieldList;
import org.eclipse.milo.opcua.stack.core.types.structured.EventFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.EventFilterResult;
import org.eclipse.milo.opcua.stack.core.types.structured.EventNotificationList;
import org.eclipse.milo.opcua.stack.core.types.structured.FieldMetaData;
import org.eclipse.milo.opcua.stack.core.types.structured.FieldTargetDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.FindServersOnNetworkRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.FindServersOnNetworkResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.FindServersRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.FindServersResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.GenericAttributeValue;
import org.eclipse.milo.opcua.stack.core.types.structured.GenericAttributes;
import org.eclipse.milo.opcua.stack.core.types.structured.GetEndpointsRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.GetEndpointsResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryData;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryEvent;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryEventFieldList;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryModifiedData;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryModifiedEvent;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryReadRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryReadResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryReadResult;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryUpdateRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryUpdateResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryUpdateResult;
import org.eclipse.milo.opcua.stack.core.types.structured.IdentityMappingRuleType;
import org.eclipse.milo.opcua.stack.core.types.structured.InstanceNode;
import org.eclipse.milo.opcua.stack.core.types.structured.IssuedIdentityToken;
import org.eclipse.milo.opcua.stack.core.types.structured.JsonActionMetaDataMessage;
import org.eclipse.milo.opcua.stack.core.types.structured.JsonActionNetworkMessage;
import org.eclipse.milo.opcua.stack.core.types.structured.JsonActionRequestMessage;
import org.eclipse.milo.opcua.stack.core.types.structured.JsonActionResponderMessage;
import org.eclipse.milo.opcua.stack.core.types.structured.JsonActionResponseMessage;
import org.eclipse.milo.opcua.stack.core.types.structured.JsonApplicationDescriptionMessage;
import org.eclipse.milo.opcua.stack.core.types.structured.JsonDataSetMessage;
import org.eclipse.milo.opcua.stack.core.types.structured.JsonDataSetMetaDataMessage;
import org.eclipse.milo.opcua.stack.core.types.structured.JsonDataSetReaderMessageDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.JsonDataSetWriterMessageDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.JsonPubSubConnectionMessage;
import org.eclipse.milo.opcua.stack.core.types.structured.JsonServerEndpointsMessage;
import org.eclipse.milo.opcua.stack.core.types.structured.JsonStatusMessage;
import org.eclipse.milo.opcua.stack.core.types.structured.JsonWriterGroupMessageDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.KeyValuePair;
import org.eclipse.milo.opcua.stack.core.types.structured.LinearConversionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.LiteralOperand;
import org.eclipse.milo.opcua.stack.core.types.structured.LldpManagementAddressTxPortType;
import org.eclipse.milo.opcua.stack.core.types.structured.LldpManagementAddressType;
import org.eclipse.milo.opcua.stack.core.types.structured.LldpTlvType;
import org.eclipse.milo.opcua.stack.core.types.structured.MdnsDiscoveryConfiguration;
import org.eclipse.milo.opcua.stack.core.types.structured.MethodAttributes;
import org.eclipse.milo.opcua.stack.core.types.structured.MethodNode;
import org.eclipse.milo.opcua.stack.core.types.structured.ModelChangeStructureDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.ModificationInfo;
import org.eclipse.milo.opcua.stack.core.types.structured.ModifyMonitoredItemsRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.ModifyMonitoredItemsResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ModifySubscriptionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.ModifySubscriptionResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemCreateRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemCreateResult;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemModifyRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemModifyResult;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemNotification;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoringFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoringFilterResult;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoringParameters;
import org.eclipse.milo.opcua.stack.core.types.structured.NetworkAddressUrlDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.NetworkGroupDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.Node;
import org.eclipse.milo.opcua.stack.core.types.structured.NodeAttributes;
import org.eclipse.milo.opcua.stack.core.types.structured.NodeReference;
import org.eclipse.milo.opcua.stack.core.types.structured.NodeTypeDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.NotificationData;
import org.eclipse.milo.opcua.stack.core.types.structured.NotificationMessage;
import org.eclipse.milo.opcua.stack.core.types.structured.ObjectAttributes;
import org.eclipse.milo.opcua.stack.core.types.structured.ObjectNode;
import org.eclipse.milo.opcua.stack.core.types.structured.ObjectTypeAttributes;
import org.eclipse.milo.opcua.stack.core.types.structured.ObjectTypeNode;
import org.eclipse.milo.opcua.stack.core.types.structured.OpenSecureChannelRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.OpenSecureChannelResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ParsingResult;
import org.eclipse.milo.opcua.stack.core.types.structured.PortableNodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.PortableQualifiedName;
import org.eclipse.milo.opcua.stack.core.types.structured.PriorityMappingEntryType;
import org.eclipse.milo.opcua.stack.core.types.structured.ProgramDiagnostic2DataType;
import org.eclipse.milo.opcua.stack.core.types.structured.ProgramDiagnosticDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfiguration2DataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfigurationDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfigurationRefDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfigurationValueDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConnectionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubKeyPushTargetDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishedActionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishedActionMethodDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishedDataItemsDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishedDataSetCustomSourceDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishedDataSetDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishedEventsDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishedVariableDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.QuantityDimension;
import org.eclipse.milo.opcua.stack.core.types.structured.QueryDataDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.QueryDataSet;
import org.eclipse.milo.opcua.stack.core.types.structured.QueryFirstRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.QueryFirstResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.QueryNextRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.QueryNextResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.Range;
import org.eclipse.milo.opcua.stack.core.types.structured.RationalNumber;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadAnnotationDataDetails;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadAtTimeDetails;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadEventDetails;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadEventDetails2;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadEventDetailsSorted;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadProcessedDetails;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadRawModifiedDetails;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.ReaderGroupDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.ReceiveQosPriorityDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.RedundantServerDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.ReferenceDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.ReferenceDescriptionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.ReferenceListEntryDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.ReferenceNode;
import org.eclipse.milo.opcua.stack.core.types.structured.ReferenceTypeAttributes;
import org.eclipse.milo.opcua.stack.core.types.structured.ReferenceTypeNode;
import org.eclipse.milo.opcua.stack.core.types.structured.RegisterNodesRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.RegisterNodesResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.RegisterServer2Request;
import org.eclipse.milo.opcua.stack.core.types.structured.RegisterServer2Response;
import org.eclipse.milo.opcua.stack.core.types.structured.RegisterServerRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.RegisterServerResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.RegisteredServer;
import org.eclipse.milo.opcua.stack.core.types.structured.RelativePath;
import org.eclipse.milo.opcua.stack.core.types.structured.RelativePathElement;
import org.eclipse.milo.opcua.stack.core.types.structured.RepublishRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.RepublishResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.RequestHeader;
import org.eclipse.milo.opcua.stack.core.types.structured.ResponseHeader;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.SamplingIntervalDiagnosticsDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.SecurityGroupDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.SemanticChangeStructureDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.ServerDiagnosticsSummaryDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.ServerOnNetwork;
import org.eclipse.milo.opcua.stack.core.types.structured.ServerStatusDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.ServiceCounterDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.ServiceFault;
import org.eclipse.milo.opcua.stack.core.types.structured.SessionDiagnosticsDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.SessionSecurityDiagnosticsDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.SessionlessInvokeRequestType;
import org.eclipse.milo.opcua.stack.core.types.structured.SessionlessInvokeResponseType;
import org.eclipse.milo.opcua.stack.core.types.structured.SetMonitoringModeRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.SetMonitoringModeResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.SetPublishingModeRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.SetPublishingModeResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.SetTriggeringRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.SetTriggeringResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.SignatureData;
import org.eclipse.milo.opcua.stack.core.types.structured.SignedSoftwareCertificate;
import org.eclipse.milo.opcua.stack.core.types.structured.SimpleAttributeOperand;
import org.eclipse.milo.opcua.stack.core.types.structured.SimpleTypeDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.SortRuleElement;
import org.eclipse.milo.opcua.stack.core.types.structured.StandaloneSubscribedDataSetDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.StandaloneSubscribedDataSetRefDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.StatusChangeNotification;
import org.eclipse.milo.opcua.stack.core.types.structured.StatusResult;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureDefinition;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureField;
import org.eclipse.milo.opcua.stack.core.types.structured.SubscribedDataSetMirrorDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.SubscriptionAcknowledgement;
import org.eclipse.milo.opcua.stack.core.types.structured.SubscriptionDiagnosticsDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.TargetVariablesDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.ThreeDCartesianCoordinates;
import org.eclipse.milo.opcua.stack.core.types.structured.ThreeDFrame;
import org.eclipse.milo.opcua.stack.core.types.structured.ThreeDOrientation;
import org.eclipse.milo.opcua.stack.core.types.structured.ThreeDVector;
import org.eclipse.milo.opcua.stack.core.types.structured.TimeZoneDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.TransactionErrorType;
import org.eclipse.milo.opcua.stack.core.types.structured.TransferResult;
import org.eclipse.milo.opcua.stack.core.types.structured.TransferSubscriptionsRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.TransferSubscriptionsResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.TranslateBrowsePathsToNodeIdsRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.TranslateBrowsePathsToNodeIdsResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.TransmitQosPriorityDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.TrustListDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.TypeNode;
import org.eclipse.milo.opcua.stack.core.types.structured.UABinaryFileDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpDataSetReaderMessageDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpDataSetWriterMessageDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpWriterGroupMessageDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.UnregisterNodesRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.UnregisterNodesResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.UnsignedRationalNumber;
import org.eclipse.milo.opcua.stack.core.types.structured.UpdateDataDetails;
import org.eclipse.milo.opcua.stack.core.types.structured.UpdateEventDetails;
import org.eclipse.milo.opcua.stack.core.types.structured.UpdateStructureDataDetails;
import org.eclipse.milo.opcua.stack.core.types.structured.UserManagementDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.UserNameIdentityToken;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.core.types.structured.VariableAttributes;
import org.eclipse.milo.opcua.stack.core.types.structured.VariableNode;
import org.eclipse.milo.opcua.stack.core.types.structured.VariableTypeAttributes;
import org.eclipse.milo.opcua.stack.core.types.structured.VariableTypeNode;
import org.eclipse.milo.opcua.stack.core.types.structured.ViewAttributes;
import org.eclipse.milo.opcua.stack.core.types.structured.ViewDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.ViewNode;
import org.eclipse.milo.opcua.stack.core.types.structured.WriteRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.WriteResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.WriteValue;
import org.eclipse.milo.opcua.stack.core.types.structured.WriterGroupDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.X509IdentityToken;
import org.eclipse.milo.opcua.stack.core.types.structured.XVType;

public class BinaryDataTypeDictionaryInitializer extends DataTypeDictionaryInitializer {
  @Override
  protected void initializeStructs(
      NamespaceTable namespaceTable, DataTypeDictionary binaryDictionary) throws Exception {
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "KeyValuePair",
            KeyValuePair.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            KeyValuePair.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new KeyValuePair.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "AdditionalParametersType",
            AdditionalParametersType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            AdditionalParametersType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new AdditionalParametersType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "EphemeralKeyType",
            EphemeralKeyType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            EphemeralKeyType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new EphemeralKeyType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "EndpointType",
            EndpointType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            EndpointType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new EndpointType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "BitFieldDefinition",
            BitFieldDefinition.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            BitFieldDefinition.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new BitFieldDefinition.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "RationalNumber",
            RationalNumber.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            RationalNumber.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new RationalNumber.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "3DVector",
            ThreeDVector.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            ThreeDVector.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new ThreeDVector.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "3DCartesianCoordinates",
            ThreeDCartesianCoordinates.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            ThreeDCartesianCoordinates.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new ThreeDCartesianCoordinates.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "3DOrientation",
            ThreeDOrientation.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            ThreeDOrientation.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new ThreeDOrientation.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "3DFrame",
            ThreeDFrame.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            ThreeDFrame.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new ThreeDFrame.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "IdentityMappingRuleType",
            IdentityMappingRuleType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            IdentityMappingRuleType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new IdentityMappingRuleType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "CurrencyUnitType",
            CurrencyUnitType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            CurrencyUnitType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new CurrencyUnitType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "AnnotationDataType",
            AnnotationDataType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            AnnotationDataType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new AnnotationDataType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "LinearConversionDataType",
            LinearConversionDataType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            LinearConversionDataType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new LinearConversionDataType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "QuantityDimension",
            QuantityDimension.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            QuantityDimension.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new QuantityDimension.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "TrustListDataType",
            TrustListDataType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            TrustListDataType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new TrustListDataType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "TransactionErrorType",
            TransactionErrorType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            TransactionErrorType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new TransactionErrorType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "UABinaryFileDataType",
            UABinaryFileDataType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            UABinaryFileDataType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new UABinaryFileDataType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "DataSetMetaDataType",
            DataSetMetaDataType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            DataSetMetaDataType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new DataSetMetaDataType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "StructureDescription",
            StructureDescription.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            StructureDescription.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new StructureDescription.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "EnumDescription",
            EnumDescription.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            EnumDescription.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new EnumDescription.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "SimpleTypeDescription",
            SimpleTypeDescription.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            SimpleTypeDescription.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new SimpleTypeDescription.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "PortableQualifiedName",
            PortableQualifiedName.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            PortableQualifiedName.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new PortableQualifiedName.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "PortableNodeId",
            PortableNodeId.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            PortableNodeId.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new PortableNodeId.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "UnsignedRationalNumber",
            UnsignedRationalNumber.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            UnsignedRationalNumber.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new UnsignedRationalNumber.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "FieldMetaData",
            FieldMetaData.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            FieldMetaData.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new FieldMetaData.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "ConfigurationVersionDataType",
            ConfigurationVersionDataType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            ConfigurationVersionDataType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new ConfigurationVersionDataType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "PublishedDataSetDataType",
            PublishedDataSetDataType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            PublishedDataSetDataType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new PublishedDataSetDataType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "PublishedDataItemsDataType",
            PublishedDataItemsDataType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            PublishedDataItemsDataType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new PublishedDataItemsDataType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "PublishedEventsDataType",
            PublishedEventsDataType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            PublishedEventsDataType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new PublishedEventsDataType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "PublishedDataSetCustomSourceDataType",
            PublishedDataSetCustomSourceDataType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            PublishedDataSetCustomSourceDataType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new PublishedDataSetCustomSourceDataType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "PublishedActionDataType",
            PublishedActionDataType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            PublishedActionDataType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new PublishedActionDataType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "PublishedActionMethodDataType",
            PublishedActionMethodDataType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            PublishedActionMethodDataType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new PublishedActionMethodDataType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "PublishedVariableDataType",
            PublishedVariableDataType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            PublishedVariableDataType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new PublishedVariableDataType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "ActionTargetDataType",
            ActionTargetDataType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            ActionTargetDataType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new ActionTargetDataType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "ActionMethodDataType",
            ActionMethodDataType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            ActionMethodDataType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new ActionMethodDataType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "DataSetWriterDataType",
            DataSetWriterDataType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            DataSetWriterDataType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new DataSetWriterDataType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "BrokerDataSetWriterTransportDataType",
            BrokerDataSetWriterTransportDataType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            BrokerDataSetWriterTransportDataType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new BrokerDataSetWriterTransportDataType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "UadpDataSetWriterMessageDataType",
            UadpDataSetWriterMessageDataType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            UadpDataSetWriterMessageDataType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new UadpDataSetWriterMessageDataType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "JsonDataSetWriterMessageDataType",
            JsonDataSetWriterMessageDataType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            JsonDataSetWriterMessageDataType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new JsonDataSetWriterMessageDataType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "WriterGroupDataType",
            WriterGroupDataType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            WriterGroupDataType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new WriterGroupDataType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "ReaderGroupDataType",
            ReaderGroupDataType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            ReaderGroupDataType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new ReaderGroupDataType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "DatagramWriterGroupTransportDataType",
            DatagramWriterGroupTransportDataType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            DatagramWriterGroupTransportDataType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new DatagramWriterGroupTransportDataType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "DatagramWriterGroupTransport2DataType",
            DatagramWriterGroupTransport2DataType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            DatagramWriterGroupTransport2DataType.BINARY_ENCODING_ID.toNodeIdOrThrow(
                namespaceTable),
            BinaryDataTypeCodec.from(new DatagramWriterGroupTransport2DataType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "BrokerWriterGroupTransportDataType",
            BrokerWriterGroupTransportDataType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            BrokerWriterGroupTransportDataType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new BrokerWriterGroupTransportDataType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "UadpWriterGroupMessageDataType",
            UadpWriterGroupMessageDataType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            UadpWriterGroupMessageDataType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new UadpWriterGroupMessageDataType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "JsonWriterGroupMessageDataType",
            JsonWriterGroupMessageDataType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            JsonWriterGroupMessageDataType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new JsonWriterGroupMessageDataType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "PubSubConnectionDataType",
            PubSubConnectionDataType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            PubSubConnectionDataType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new PubSubConnectionDataType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "DatagramConnectionTransportDataType",
            DatagramConnectionTransportDataType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            DatagramConnectionTransportDataType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new DatagramConnectionTransportDataType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "DatagramConnectionTransport2DataType",
            DatagramConnectionTransport2DataType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            DatagramConnectionTransport2DataType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new DatagramConnectionTransport2DataType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "BrokerConnectionTransportDataType",
            BrokerConnectionTransportDataType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            BrokerConnectionTransportDataType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new BrokerConnectionTransportDataType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "NetworkAddressUrlDataType",
            NetworkAddressUrlDataType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            NetworkAddressUrlDataType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new NetworkAddressUrlDataType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "DataSetReaderDataType",
            DataSetReaderDataType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            DataSetReaderDataType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new DataSetReaderDataType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "DatagramDataSetReaderTransportDataType",
            DatagramDataSetReaderTransportDataType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            DatagramDataSetReaderTransportDataType.BINARY_ENCODING_ID.toNodeIdOrThrow(
                namespaceTable),
            BinaryDataTypeCodec.from(new DatagramDataSetReaderTransportDataType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "BrokerDataSetReaderTransportDataType",
            BrokerDataSetReaderTransportDataType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            BrokerDataSetReaderTransportDataType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new BrokerDataSetReaderTransportDataType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "UadpDataSetReaderMessageDataType",
            UadpDataSetReaderMessageDataType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            UadpDataSetReaderMessageDataType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new UadpDataSetReaderMessageDataType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "JsonDataSetReaderMessageDataType",
            JsonDataSetReaderMessageDataType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            JsonDataSetReaderMessageDataType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new JsonDataSetReaderMessageDataType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "TargetVariablesDataType",
            TargetVariablesDataType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            TargetVariablesDataType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new TargetVariablesDataType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "SubscribedDataSetMirrorDataType",
            SubscribedDataSetMirrorDataType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            SubscribedDataSetMirrorDataType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new SubscribedDataSetMirrorDataType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "StandaloneSubscribedDataSetRefDataType",
            StandaloneSubscribedDataSetRefDataType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            StandaloneSubscribedDataSetRefDataType.BINARY_ENCODING_ID.toNodeIdOrThrow(
                namespaceTable),
            BinaryDataTypeCodec.from(new StandaloneSubscribedDataSetRefDataType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "StandaloneSubscribedDataSetDataType",
            StandaloneSubscribedDataSetDataType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            StandaloneSubscribedDataSetDataType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new StandaloneSubscribedDataSetDataType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "FieldTargetDataType",
            FieldTargetDataType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            FieldTargetDataType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new FieldTargetDataType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "PubSubConfigurationDataType",
            PubSubConfigurationDataType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            PubSubConfigurationDataType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new PubSubConfigurationDataType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "PubSubConfiguration2DataType",
            PubSubConfiguration2DataType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            PubSubConfiguration2DataType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new PubSubConfiguration2DataType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "SecurityGroupDataType",
            SecurityGroupDataType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            SecurityGroupDataType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new SecurityGroupDataType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "PubSubKeyPushTargetDataType",
            PubSubKeyPushTargetDataType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            PubSubKeyPushTargetDataType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new PubSubKeyPushTargetDataType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "TransmitQosPriorityDataType",
            TransmitQosPriorityDataType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            TransmitQosPriorityDataType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new TransmitQosPriorityDataType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "ReceiveQosPriorityDataType",
            ReceiveQosPriorityDataType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            ReceiveQosPriorityDataType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new ReceiveQosPriorityDataType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "DtlsPubSubConnectionDataType",
            DtlsPubSubConnectionDataType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            DtlsPubSubConnectionDataType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new DtlsPubSubConnectionDataType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "PubSubConfigurationRefDataType",
            PubSubConfigurationRefDataType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            PubSubConfigurationRefDataType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new PubSubConfigurationRefDataType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "PubSubConfigurationValueDataType",
            PubSubConfigurationValueDataType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            PubSubConfigurationValueDataType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new PubSubConfigurationValueDataType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "AliasNameDataType",
            AliasNameDataType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            AliasNameDataType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new AliasNameDataType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "UserManagementDataType",
            UserManagementDataType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            UserManagementDataType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new UserManagementDataType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "PriorityMappingEntryType",
            PriorityMappingEntryType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            PriorityMappingEntryType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new PriorityMappingEntryType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "LldpManagementAddressTxPortType",
            LldpManagementAddressTxPortType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            LldpManagementAddressTxPortType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new LldpManagementAddressTxPortType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "LldpManagementAddressType",
            LldpManagementAddressType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            LldpManagementAddressType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new LldpManagementAddressType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "LldpTlvType",
            LldpTlvType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            LldpTlvType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new LldpTlvType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "ReferenceDescriptionDataType",
            ReferenceDescriptionDataType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            ReferenceDescriptionDataType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new ReferenceDescriptionDataType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "ReferenceListEntryDataType",
            ReferenceListEntryDataType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            ReferenceListEntryDataType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new ReferenceListEntryDataType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "RolePermissionType",
            RolePermissionType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            RolePermissionType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new RolePermissionType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "StructureDefinition",
            StructureDefinition.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            StructureDefinition.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new StructureDefinition.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "EnumDefinition",
            EnumDefinition.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            EnumDefinition.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new EnumDefinition.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "StructureField",
            StructureField.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            StructureField.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new StructureField.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "Argument",
            Argument.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            Argument.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new Argument.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "EnumValueType",
            EnumValueType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            EnumValueType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new EnumValueType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "EnumField",
            EnumField.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            EnumField.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new EnumField.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "TimeZoneDataType",
            TimeZoneDataType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            TimeZoneDataType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new TimeZoneDataType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "ApplicationDescription",
            ApplicationDescription.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            ApplicationDescription.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new ApplicationDescription.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "ServerOnNetwork",
            ServerOnNetwork.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            ServerOnNetwork.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new ServerOnNetwork.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "UserTokenPolicy",
            UserTokenPolicy.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            UserTokenPolicy.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new UserTokenPolicy.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "EndpointDescription",
            EndpointDescription.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            EndpointDescription.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new EndpointDescription.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "RegisteredServer",
            RegisteredServer.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            RegisteredServer.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new RegisteredServer.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "DiscoveryConfiguration",
            DiscoveryConfiguration.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            DiscoveryConfiguration.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new DiscoveryConfiguration.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "MdnsDiscoveryConfiguration",
            MdnsDiscoveryConfiguration.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            MdnsDiscoveryConfiguration.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new MdnsDiscoveryConfiguration.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "SignedSoftwareCertificate",
            SignedSoftwareCertificate.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            SignedSoftwareCertificate.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new SignedSoftwareCertificate.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "AnonymousIdentityToken",
            AnonymousIdentityToken.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            AnonymousIdentityToken.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new AnonymousIdentityToken.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "UserNameIdentityToken",
            UserNameIdentityToken.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            UserNameIdentityToken.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new UserNameIdentityToken.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "X509IdentityToken",
            X509IdentityToken.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            X509IdentityToken.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new X509IdentityToken.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "IssuedIdentityToken",
            IssuedIdentityToken.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            IssuedIdentityToken.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new IssuedIdentityToken.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "AddNodesItem",
            AddNodesItem.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            AddNodesItem.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new AddNodesItem.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "AddReferencesItem",
            AddReferencesItem.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            AddReferencesItem.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new AddReferencesItem.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "DeleteNodesItem",
            DeleteNodesItem.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            DeleteNodesItem.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new DeleteNodesItem.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "DeleteReferencesItem",
            DeleteReferencesItem.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            DeleteReferencesItem.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new DeleteReferencesItem.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "RelativePathElement",
            RelativePathElement.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            RelativePathElement.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new RelativePathElement.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "RelativePath",
            RelativePath.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            RelativePath.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new RelativePath.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "EndpointConfiguration",
            EndpointConfiguration.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            EndpointConfiguration.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new EndpointConfiguration.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "ContentFilterElement",
            ContentFilterElement.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            ContentFilterElement.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new ContentFilterElement.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "ContentFilter",
            ContentFilter.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            ContentFilter.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new ContentFilter.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "ElementOperand",
            ElementOperand.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            ElementOperand.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new ElementOperand.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "LiteralOperand",
            LiteralOperand.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            LiteralOperand.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new LiteralOperand.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "AttributeOperand",
            AttributeOperand.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            AttributeOperand.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new AttributeOperand.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "SimpleAttributeOperand",
            SimpleAttributeOperand.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            SimpleAttributeOperand.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new SimpleAttributeOperand.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "ModificationInfo",
            ModificationInfo.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            ModificationInfo.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new ModificationInfo.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "HistoryEvent",
            HistoryEvent.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            HistoryEvent.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new HistoryEvent.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "HistoryModifiedEvent",
            HistoryModifiedEvent.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            HistoryModifiedEvent.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new HistoryModifiedEvent.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "MonitoringFilter",
            MonitoringFilter.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            MonitoringFilter.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new MonitoringFilter.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "EventFilter",
            EventFilter.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            EventFilter.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new EventFilter.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "DataChangeFilter",
            DataChangeFilter.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            DataChangeFilter.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new DataChangeFilter.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "AggregateFilter",
            AggregateFilter.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            AggregateFilter.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new AggregateFilter.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "AggregateConfiguration",
            AggregateConfiguration.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            AggregateConfiguration.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new AggregateConfiguration.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "HistoryEventFieldList",
            HistoryEventFieldList.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            HistoryEventFieldList.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new HistoryEventFieldList.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "BuildInfo",
            BuildInfo.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            BuildInfo.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new BuildInfo.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "RedundantServerDataType",
            RedundantServerDataType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            RedundantServerDataType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new RedundantServerDataType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "EndpointUrlListDataType",
            EndpointUrlListDataType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            EndpointUrlListDataType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new EndpointUrlListDataType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "NetworkGroupDataType",
            NetworkGroupDataType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            NetworkGroupDataType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new NetworkGroupDataType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "SamplingIntervalDiagnosticsDataType",
            SamplingIntervalDiagnosticsDataType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            SamplingIntervalDiagnosticsDataType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new SamplingIntervalDiagnosticsDataType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "ServerDiagnosticsSummaryDataType",
            ServerDiagnosticsSummaryDataType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            ServerDiagnosticsSummaryDataType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new ServerDiagnosticsSummaryDataType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "ServerStatusDataType",
            ServerStatusDataType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            ServerStatusDataType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new ServerStatusDataType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "SessionDiagnosticsDataType",
            SessionDiagnosticsDataType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            SessionDiagnosticsDataType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new SessionDiagnosticsDataType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "SessionSecurityDiagnosticsDataType",
            SessionSecurityDiagnosticsDataType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            SessionSecurityDiagnosticsDataType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new SessionSecurityDiagnosticsDataType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "ServiceCounterDataType",
            ServiceCounterDataType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            ServiceCounterDataType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new ServiceCounterDataType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "StatusResult",
            StatusResult.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            StatusResult.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new StatusResult.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "SubscriptionDiagnosticsDataType",
            SubscriptionDiagnosticsDataType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            SubscriptionDiagnosticsDataType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new SubscriptionDiagnosticsDataType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "ModelChangeStructureDataType",
            ModelChangeStructureDataType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            ModelChangeStructureDataType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new ModelChangeStructureDataType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "SemanticChangeStructureDataType",
            SemanticChangeStructureDataType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            SemanticChangeStructureDataType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new SemanticChangeStructureDataType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "Range",
            Range.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            Range.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new Range.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "EUInformation",
            EUInformation.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            EUInformation.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new EUInformation.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "ComplexNumberType",
            ComplexNumberType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            ComplexNumberType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new ComplexNumberType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "DoubleComplexNumberType",
            DoubleComplexNumberType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            DoubleComplexNumberType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new DoubleComplexNumberType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "AxisInformation",
            AxisInformation.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            AxisInformation.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new AxisInformation.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "XVType",
            XVType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            XVType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new XVType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "ProgramDiagnosticDataType",
            ProgramDiagnosticDataType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            ProgramDiagnosticDataType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new ProgramDiagnosticDataType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "ProgramDiagnostic2DataType",
            ProgramDiagnostic2DataType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            ProgramDiagnostic2DataType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new ProgramDiagnostic2DataType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "Annotation",
            Annotation.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            Annotation.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new Annotation.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "DecimalDataType",
            DecimalDataType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            DecimalDataType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new DecimalDataType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "JsonDataSetMessage",
            JsonDataSetMessage.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            JsonDataSetMessage.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new JsonDataSetMessage.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "JsonDataSetMetaDataMessage",
            JsonDataSetMetaDataMessage.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            JsonDataSetMetaDataMessage.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new JsonDataSetMetaDataMessage.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "JsonApplicationDescriptionMessage",
            JsonApplicationDescriptionMessage.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            JsonApplicationDescriptionMessage.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new JsonApplicationDescriptionMessage.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "JsonServerEndpointsMessage",
            JsonServerEndpointsMessage.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            JsonServerEndpointsMessage.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new JsonServerEndpointsMessage.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "JsonStatusMessage",
            JsonStatusMessage.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            JsonStatusMessage.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new JsonStatusMessage.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "JsonPubSubConnectionMessage",
            JsonPubSubConnectionMessage.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            JsonPubSubConnectionMessage.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new JsonPubSubConnectionMessage.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "JsonActionMetaDataMessage",
            JsonActionMetaDataMessage.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            JsonActionMetaDataMessage.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new JsonActionMetaDataMessage.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "JsonActionResponderMessage",
            JsonActionResponderMessage.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            JsonActionResponderMessage.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new JsonActionResponderMessage.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "JsonActionNetworkMessage",
            JsonActionNetworkMessage.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            JsonActionNetworkMessage.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new JsonActionNetworkMessage.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "JsonActionRequestMessage",
            JsonActionRequestMessage.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            JsonActionRequestMessage.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new JsonActionRequestMessage.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "JsonActionResponseMessage",
            JsonActionResponseMessage.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            JsonActionResponseMessage.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new JsonActionResponseMessage.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "Node",
            Node.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            Node.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new Node.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "InstanceNode",
            InstanceNode.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            InstanceNode.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new InstanceNode.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "ObjectNode",
            ObjectNode.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            ObjectNode.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new ObjectNode.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "VariableNode",
            VariableNode.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            VariableNode.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new VariableNode.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "MethodNode",
            MethodNode.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            MethodNode.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new MethodNode.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "ViewNode",
            ViewNode.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            ViewNode.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new ViewNode.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "TypeNode",
            TypeNode.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            TypeNode.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new TypeNode.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "ObjectTypeNode",
            ObjectTypeNode.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            ObjectTypeNode.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new ObjectTypeNode.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "VariableTypeNode",
            VariableTypeNode.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            VariableTypeNode.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new VariableTypeNode.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "ReferenceTypeNode",
            ReferenceTypeNode.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            ReferenceTypeNode.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new ReferenceTypeNode.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "DataTypeNode",
            DataTypeNode.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            DataTypeNode.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new DataTypeNode.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "ReferenceNode",
            ReferenceNode.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            ReferenceNode.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new ReferenceNode.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "RequestHeader",
            RequestHeader.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            RequestHeader.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new RequestHeader.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "ResponseHeader",
            ResponseHeader.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            ResponseHeader.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new ResponseHeader.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "ServiceFault",
            ServiceFault.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            ServiceFault.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new ServiceFault.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "SessionlessInvokeRequestType",
            SessionlessInvokeRequestType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            SessionlessInvokeRequestType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new SessionlessInvokeRequestType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "SessionlessInvokeResponseType",
            SessionlessInvokeResponseType.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            SessionlessInvokeResponseType.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new SessionlessInvokeResponseType.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "FindServersRequest",
            FindServersRequest.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            FindServersRequest.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new FindServersRequest.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "FindServersResponse",
            FindServersResponse.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            FindServersResponse.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new FindServersResponse.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "FindServersOnNetworkRequest",
            FindServersOnNetworkRequest.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            FindServersOnNetworkRequest.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new FindServersOnNetworkRequest.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "FindServersOnNetworkResponse",
            FindServersOnNetworkResponse.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            FindServersOnNetworkResponse.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new FindServersOnNetworkResponse.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "GetEndpointsRequest",
            GetEndpointsRequest.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            GetEndpointsRequest.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new GetEndpointsRequest.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "GetEndpointsResponse",
            GetEndpointsResponse.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            GetEndpointsResponse.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new GetEndpointsResponse.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "RegisterServerRequest",
            RegisterServerRequest.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            RegisterServerRequest.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new RegisterServerRequest.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "RegisterServerResponse",
            RegisterServerResponse.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            RegisterServerResponse.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new RegisterServerResponse.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "RegisterServer2Request",
            RegisterServer2Request.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            RegisterServer2Request.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new RegisterServer2Request.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "RegisterServer2Response",
            RegisterServer2Response.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            RegisterServer2Response.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new RegisterServer2Response.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "ChannelSecurityToken",
            ChannelSecurityToken.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            ChannelSecurityToken.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new ChannelSecurityToken.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "OpenSecureChannelRequest",
            OpenSecureChannelRequest.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            OpenSecureChannelRequest.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new OpenSecureChannelRequest.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "OpenSecureChannelResponse",
            OpenSecureChannelResponse.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            OpenSecureChannelResponse.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new OpenSecureChannelResponse.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "CloseSecureChannelRequest",
            CloseSecureChannelRequest.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            CloseSecureChannelRequest.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new CloseSecureChannelRequest.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "CloseSecureChannelResponse",
            CloseSecureChannelResponse.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            CloseSecureChannelResponse.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new CloseSecureChannelResponse.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "SignatureData",
            SignatureData.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            SignatureData.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new SignatureData.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "CreateSessionRequest",
            CreateSessionRequest.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            CreateSessionRequest.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new CreateSessionRequest.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "CreateSessionResponse",
            CreateSessionResponse.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            CreateSessionResponse.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new CreateSessionResponse.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "ActivateSessionRequest",
            ActivateSessionRequest.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            ActivateSessionRequest.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new ActivateSessionRequest.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "ActivateSessionResponse",
            ActivateSessionResponse.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            ActivateSessionResponse.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new ActivateSessionResponse.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "CloseSessionRequest",
            CloseSessionRequest.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            CloseSessionRequest.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new CloseSessionRequest.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "CloseSessionResponse",
            CloseSessionResponse.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            CloseSessionResponse.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new CloseSessionResponse.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "CancelRequest",
            CancelRequest.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            CancelRequest.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new CancelRequest.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "CancelResponse",
            CancelResponse.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            CancelResponse.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new CancelResponse.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "NodeAttributes",
            NodeAttributes.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            NodeAttributes.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new NodeAttributes.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "ObjectAttributes",
            ObjectAttributes.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            ObjectAttributes.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new ObjectAttributes.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "VariableAttributes",
            VariableAttributes.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            VariableAttributes.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new VariableAttributes.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "MethodAttributes",
            MethodAttributes.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            MethodAttributes.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new MethodAttributes.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "ObjectTypeAttributes",
            ObjectTypeAttributes.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            ObjectTypeAttributes.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new ObjectTypeAttributes.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "VariableTypeAttributes",
            VariableTypeAttributes.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            VariableTypeAttributes.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new VariableTypeAttributes.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "ReferenceTypeAttributes",
            ReferenceTypeAttributes.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            ReferenceTypeAttributes.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new ReferenceTypeAttributes.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "DataTypeAttributes",
            DataTypeAttributes.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            DataTypeAttributes.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new DataTypeAttributes.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "ViewAttributes",
            ViewAttributes.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            ViewAttributes.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new ViewAttributes.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "GenericAttributes",
            GenericAttributes.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            GenericAttributes.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new GenericAttributes.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "GenericAttributeValue",
            GenericAttributeValue.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            GenericAttributeValue.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new GenericAttributeValue.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "AddNodesResult",
            AddNodesResult.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            AddNodesResult.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new AddNodesResult.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "AddNodesRequest",
            AddNodesRequest.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            AddNodesRequest.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new AddNodesRequest.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "AddNodesResponse",
            AddNodesResponse.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            AddNodesResponse.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new AddNodesResponse.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "AddReferencesRequest",
            AddReferencesRequest.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            AddReferencesRequest.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new AddReferencesRequest.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "AddReferencesResponse",
            AddReferencesResponse.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            AddReferencesResponse.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new AddReferencesResponse.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "DeleteNodesRequest",
            DeleteNodesRequest.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            DeleteNodesRequest.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new DeleteNodesRequest.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "DeleteNodesResponse",
            DeleteNodesResponse.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            DeleteNodesResponse.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new DeleteNodesResponse.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "DeleteReferencesRequest",
            DeleteReferencesRequest.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            DeleteReferencesRequest.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new DeleteReferencesRequest.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "DeleteReferencesResponse",
            DeleteReferencesResponse.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            DeleteReferencesResponse.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new DeleteReferencesResponse.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "ViewDescription",
            ViewDescription.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            ViewDescription.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new ViewDescription.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "BrowseDescription",
            BrowseDescription.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            BrowseDescription.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new BrowseDescription.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "ReferenceDescription",
            ReferenceDescription.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            ReferenceDescription.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new ReferenceDescription.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "BrowseResult",
            BrowseResult.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            BrowseResult.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new BrowseResult.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "BrowseRequest",
            BrowseRequest.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            BrowseRequest.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new BrowseRequest.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "BrowseResponse",
            BrowseResponse.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            BrowseResponse.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new BrowseResponse.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "BrowseNextRequest",
            BrowseNextRequest.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            BrowseNextRequest.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new BrowseNextRequest.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "BrowseNextResponse",
            BrowseNextResponse.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            BrowseNextResponse.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new BrowseNextResponse.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "BrowsePath",
            BrowsePath.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            BrowsePath.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new BrowsePath.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "BrowsePathTarget",
            BrowsePathTarget.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            BrowsePathTarget.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new BrowsePathTarget.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "BrowsePathResult",
            BrowsePathResult.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            BrowsePathResult.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new BrowsePathResult.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "TranslateBrowsePathsToNodeIdsRequest",
            TranslateBrowsePathsToNodeIdsRequest.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            TranslateBrowsePathsToNodeIdsRequest.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new TranslateBrowsePathsToNodeIdsRequest.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "TranslateBrowsePathsToNodeIdsResponse",
            TranslateBrowsePathsToNodeIdsResponse.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            TranslateBrowsePathsToNodeIdsResponse.BINARY_ENCODING_ID.toNodeIdOrThrow(
                namespaceTable),
            BinaryDataTypeCodec.from(new TranslateBrowsePathsToNodeIdsResponse.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "RegisterNodesRequest",
            RegisterNodesRequest.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            RegisterNodesRequest.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new RegisterNodesRequest.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "RegisterNodesResponse",
            RegisterNodesResponse.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            RegisterNodesResponse.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new RegisterNodesResponse.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "UnregisterNodesRequest",
            UnregisterNodesRequest.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            UnregisterNodesRequest.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new UnregisterNodesRequest.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "UnregisterNodesResponse",
            UnregisterNodesResponse.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            UnregisterNodesResponse.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new UnregisterNodesResponse.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "QueryDataDescription",
            QueryDataDescription.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            QueryDataDescription.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new QueryDataDescription.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "NodeTypeDescription",
            NodeTypeDescription.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            NodeTypeDescription.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new NodeTypeDescription.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "QueryDataSet",
            QueryDataSet.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            QueryDataSet.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new QueryDataSet.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "NodeReference",
            NodeReference.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            NodeReference.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new NodeReference.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "ContentFilterElementResult",
            ContentFilterElementResult.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            ContentFilterElementResult.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new ContentFilterElementResult.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "ContentFilterResult",
            ContentFilterResult.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            ContentFilterResult.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new ContentFilterResult.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "ParsingResult",
            ParsingResult.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            ParsingResult.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new ParsingResult.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "QueryFirstRequest",
            QueryFirstRequest.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            QueryFirstRequest.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new QueryFirstRequest.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "QueryFirstResponse",
            QueryFirstResponse.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            QueryFirstResponse.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new QueryFirstResponse.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "QueryNextRequest",
            QueryNextRequest.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            QueryNextRequest.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new QueryNextRequest.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "QueryNextResponse",
            QueryNextResponse.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            QueryNextResponse.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new QueryNextResponse.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "ReadValueId",
            ReadValueId.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            ReadValueId.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new ReadValueId.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "ReadRequest",
            ReadRequest.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            ReadRequest.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new ReadRequest.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "ReadResponse",
            ReadResponse.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            ReadResponse.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new ReadResponse.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "HistoryReadValueId",
            HistoryReadValueId.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            HistoryReadValueId.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new HistoryReadValueId.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "HistoryReadResult",
            HistoryReadResult.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            HistoryReadResult.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new HistoryReadResult.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "ReadEventDetails",
            ReadEventDetails.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            ReadEventDetails.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new ReadEventDetails.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "ReadEventDetails2",
            ReadEventDetails2.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            ReadEventDetails2.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new ReadEventDetails2.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "ReadEventDetailsSorted",
            ReadEventDetailsSorted.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            ReadEventDetailsSorted.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new ReadEventDetailsSorted.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "ReadRawModifiedDetails",
            ReadRawModifiedDetails.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            ReadRawModifiedDetails.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new ReadRawModifiedDetails.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "ReadProcessedDetails",
            ReadProcessedDetails.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            ReadProcessedDetails.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new ReadProcessedDetails.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "ReadAtTimeDetails",
            ReadAtTimeDetails.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            ReadAtTimeDetails.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new ReadAtTimeDetails.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "ReadAnnotationDataDetails",
            ReadAnnotationDataDetails.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            ReadAnnotationDataDetails.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new ReadAnnotationDataDetails.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "SortRuleElement",
            SortRuleElement.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            SortRuleElement.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new SortRuleElement.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "HistoryData",
            HistoryData.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            HistoryData.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new HistoryData.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "HistoryModifiedData",
            HistoryModifiedData.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            HistoryModifiedData.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new HistoryModifiedData.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "HistoryReadRequest",
            HistoryReadRequest.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            HistoryReadRequest.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new HistoryReadRequest.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "HistoryReadResponse",
            HistoryReadResponse.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            HistoryReadResponse.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new HistoryReadResponse.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "WriteValue",
            WriteValue.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            WriteValue.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new WriteValue.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "WriteRequest",
            WriteRequest.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            WriteRequest.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new WriteRequest.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "WriteResponse",
            WriteResponse.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            WriteResponse.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new WriteResponse.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "UpdateDataDetails",
            UpdateDataDetails.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            UpdateDataDetails.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new UpdateDataDetails.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "UpdateStructureDataDetails",
            UpdateStructureDataDetails.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            UpdateStructureDataDetails.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new UpdateStructureDataDetails.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "UpdateEventDetails",
            UpdateEventDetails.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            UpdateEventDetails.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new UpdateEventDetails.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "DeleteRawModifiedDetails",
            DeleteRawModifiedDetails.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            DeleteRawModifiedDetails.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new DeleteRawModifiedDetails.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "DeleteAtTimeDetails",
            DeleteAtTimeDetails.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            DeleteAtTimeDetails.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new DeleteAtTimeDetails.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "DeleteEventDetails",
            DeleteEventDetails.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            DeleteEventDetails.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new DeleteEventDetails.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "HistoryUpdateResult",
            HistoryUpdateResult.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            HistoryUpdateResult.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new HistoryUpdateResult.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "HistoryUpdateRequest",
            HistoryUpdateRequest.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            HistoryUpdateRequest.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new HistoryUpdateRequest.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "HistoryUpdateResponse",
            HistoryUpdateResponse.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            HistoryUpdateResponse.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new HistoryUpdateResponse.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "CallMethodRequest",
            CallMethodRequest.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            CallMethodRequest.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new CallMethodRequest.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "CallMethodResult",
            CallMethodResult.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            CallMethodResult.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new CallMethodResult.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "CallRequest",
            CallRequest.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            CallRequest.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new CallRequest.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "CallResponse",
            CallResponse.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            CallResponse.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new CallResponse.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "MonitoringFilterResult",
            MonitoringFilterResult.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            MonitoringFilterResult.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new MonitoringFilterResult.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "EventFilterResult",
            EventFilterResult.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            EventFilterResult.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new EventFilterResult.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "AggregateFilterResult",
            AggregateFilterResult.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            AggregateFilterResult.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new AggregateFilterResult.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "MonitoringParameters",
            MonitoringParameters.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            MonitoringParameters.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new MonitoringParameters.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "MonitoredItemCreateRequest",
            MonitoredItemCreateRequest.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            MonitoredItemCreateRequest.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new MonitoredItemCreateRequest.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "MonitoredItemCreateResult",
            MonitoredItemCreateResult.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            MonitoredItemCreateResult.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new MonitoredItemCreateResult.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "CreateMonitoredItemsRequest",
            CreateMonitoredItemsRequest.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            CreateMonitoredItemsRequest.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new CreateMonitoredItemsRequest.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "CreateMonitoredItemsResponse",
            CreateMonitoredItemsResponse.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            CreateMonitoredItemsResponse.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new CreateMonitoredItemsResponse.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "MonitoredItemModifyRequest",
            MonitoredItemModifyRequest.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            MonitoredItemModifyRequest.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new MonitoredItemModifyRequest.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "MonitoredItemModifyResult",
            MonitoredItemModifyResult.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            MonitoredItemModifyResult.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new MonitoredItemModifyResult.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "ModifyMonitoredItemsRequest",
            ModifyMonitoredItemsRequest.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            ModifyMonitoredItemsRequest.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new ModifyMonitoredItemsRequest.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "ModifyMonitoredItemsResponse",
            ModifyMonitoredItemsResponse.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            ModifyMonitoredItemsResponse.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new ModifyMonitoredItemsResponse.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "SetMonitoringModeRequest",
            SetMonitoringModeRequest.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            SetMonitoringModeRequest.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new SetMonitoringModeRequest.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "SetMonitoringModeResponse",
            SetMonitoringModeResponse.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            SetMonitoringModeResponse.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new SetMonitoringModeResponse.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "SetTriggeringRequest",
            SetTriggeringRequest.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            SetTriggeringRequest.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new SetTriggeringRequest.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "SetTriggeringResponse",
            SetTriggeringResponse.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            SetTriggeringResponse.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new SetTriggeringResponse.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "DeleteMonitoredItemsRequest",
            DeleteMonitoredItemsRequest.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            DeleteMonitoredItemsRequest.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new DeleteMonitoredItemsRequest.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "DeleteMonitoredItemsResponse",
            DeleteMonitoredItemsResponse.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            DeleteMonitoredItemsResponse.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new DeleteMonitoredItemsResponse.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "CreateSubscriptionRequest",
            CreateSubscriptionRequest.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            CreateSubscriptionRequest.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new CreateSubscriptionRequest.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "CreateSubscriptionResponse",
            CreateSubscriptionResponse.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            CreateSubscriptionResponse.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new CreateSubscriptionResponse.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "ModifySubscriptionRequest",
            ModifySubscriptionRequest.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            ModifySubscriptionRequest.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new ModifySubscriptionRequest.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "ModifySubscriptionResponse",
            ModifySubscriptionResponse.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            ModifySubscriptionResponse.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new ModifySubscriptionResponse.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "SetPublishingModeRequest",
            SetPublishingModeRequest.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            SetPublishingModeRequest.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new SetPublishingModeRequest.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "SetPublishingModeResponse",
            SetPublishingModeResponse.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            SetPublishingModeResponse.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new SetPublishingModeResponse.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "NotificationMessage",
            NotificationMessage.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            NotificationMessage.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new NotificationMessage.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "NotificationData",
            NotificationData.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            NotificationData.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new NotificationData.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "DataChangeNotification",
            DataChangeNotification.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            DataChangeNotification.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new DataChangeNotification.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "EventNotificationList",
            EventNotificationList.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            EventNotificationList.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new EventNotificationList.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "StatusChangeNotification",
            StatusChangeNotification.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            StatusChangeNotification.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new StatusChangeNotification.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "MonitoredItemNotification",
            MonitoredItemNotification.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            MonitoredItemNotification.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new MonitoredItemNotification.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "EventFieldList",
            EventFieldList.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            EventFieldList.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new EventFieldList.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "SubscriptionAcknowledgement",
            SubscriptionAcknowledgement.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            SubscriptionAcknowledgement.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new SubscriptionAcknowledgement.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "PublishRequest",
            PublishRequest.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            PublishRequest.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new PublishRequest.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "PublishResponse",
            PublishResponse.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            PublishResponse.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new PublishResponse.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "RepublishRequest",
            RepublishRequest.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            RepublishRequest.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new RepublishRequest.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "RepublishResponse",
            RepublishResponse.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            RepublishResponse.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new RepublishResponse.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "TransferResult",
            TransferResult.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            TransferResult.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new TransferResult.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "TransferSubscriptionsRequest",
            TransferSubscriptionsRequest.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            TransferSubscriptionsRequest.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new TransferSubscriptionsRequest.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "TransferSubscriptionsResponse",
            TransferSubscriptionsResponse.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            TransferSubscriptionsResponse.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new TransferSubscriptionsResponse.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "DeleteSubscriptionsRequest",
            DeleteSubscriptionsRequest.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            DeleteSubscriptionsRequest.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new DeleteSubscriptionsRequest.Codec())));
    binaryDictionary.registerType(
        new BinaryDataTypeDictionary.BinaryType(
            "DeleteSubscriptionsResponse",
            DeleteSubscriptionsResponse.TYPE_ID.toNodeIdOrThrow(namespaceTable),
            DeleteSubscriptionsResponse.BINARY_ENCODING_ID.toNodeIdOrThrow(namespaceTable),
            BinaryDataTypeCodec.from(new DeleteSubscriptionsResponse.Codec())));
  }
}

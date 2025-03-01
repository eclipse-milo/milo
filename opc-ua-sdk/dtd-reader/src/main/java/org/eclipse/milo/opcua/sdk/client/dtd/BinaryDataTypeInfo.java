package org.eclipse.milo.opcua.sdk.client.dtd;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.jspecify.annotations.Nullable;

abstract class BinaryDataTypeInfo {
  private static final Map<String, DataTypeInfo> DATA_TYPE_INFO_MAP;

  static {
    DATA_TYPE_INFO_MAP = new ConcurrentHashMap<>();
    DATA_TYPE_INFO_MAP.put("Union", new DataTypeInfo(new NodeId(0, 12756), new NodeId(0, 12766)));
    DATA_TYPE_INFO_MAP.put(
        "KeyValuePair", new DataTypeInfo(new NodeId(0, 14533), new NodeId(0, 14846)));
    DATA_TYPE_INFO_MAP.put(
        "AdditionalParametersType", new DataTypeInfo(new NodeId(0, 16313), new NodeId(0, 17537)));
    DATA_TYPE_INFO_MAP.put(
        "EphemeralKeyType", new DataTypeInfo(new NodeId(0, 17548), new NodeId(0, 17549)));
    DATA_TYPE_INFO_MAP.put(
        "EndpointType", new DataTypeInfo(new NodeId(0, 15528), new NodeId(0, 15671)));
    DATA_TYPE_INFO_MAP.put(
        "BitFieldDefinition", new DataTypeInfo(new NodeId(0, 32421), new NodeId(0, 32422)));
    DATA_TYPE_INFO_MAP.put(
        "RationalNumber", new DataTypeInfo(new NodeId(0, 18806), new NodeId(0, 18815)));
    DATA_TYPE_INFO_MAP.put("Vector", new DataTypeInfo(new NodeId(0, 18807), new NodeId(0, 18816)));
    DATA_TYPE_INFO_MAP.put(
        "3DVector", new DataTypeInfo(new NodeId(0, 18808), new NodeId(0, 18817)));
    DATA_TYPE_INFO_MAP.put(
        "CartesianCoordinates", new DataTypeInfo(new NodeId(0, 18809), new NodeId(0, 18818)));
    DATA_TYPE_INFO_MAP.put(
        "3DCartesianCoordinates", new DataTypeInfo(new NodeId(0, 18810), new NodeId(0, 18819)));
    DATA_TYPE_INFO_MAP.put(
        "Orientation", new DataTypeInfo(new NodeId(0, 18811), new NodeId(0, 18820)));
    DATA_TYPE_INFO_MAP.put(
        "3DOrientation", new DataTypeInfo(new NodeId(0, 18812), new NodeId(0, 18821)));
    DATA_TYPE_INFO_MAP.put("Frame", new DataTypeInfo(new NodeId(0, 18813), new NodeId(0, 18822)));
    DATA_TYPE_INFO_MAP.put("3DFrame", new DataTypeInfo(new NodeId(0, 18814), new NodeId(0, 18823)));
    DATA_TYPE_INFO_MAP.put(
        "IdentityMappingRuleType", new DataTypeInfo(new NodeId(0, 15634), new NodeId(0, 15736)));
    DATA_TYPE_INFO_MAP.put(
        "CurrencyUnitType", new DataTypeInfo(new NodeId(0, 23498), new NodeId(0, 23507)));
    DATA_TYPE_INFO_MAP.put(
        "AnnotationDataType", new DataTypeInfo(new NodeId(0, 32434), new NodeId(0, 32560)));
    DATA_TYPE_INFO_MAP.put(
        "LinearConversionDataType", new DataTypeInfo(new NodeId(0, 32435), new NodeId(0, 32561)));
    DATA_TYPE_INFO_MAP.put(
        "QuantityDimension", new DataTypeInfo(new NodeId(0, 32438), new NodeId(0, 32562)));
    DATA_TYPE_INFO_MAP.put(
        "TrustListDataType", new DataTypeInfo(new NodeId(0, 12554), new NodeId(0, 12680)));
    DATA_TYPE_INFO_MAP.put(
        "TransactionErrorType", new DataTypeInfo(new NodeId(0, 32285), new NodeId(0, 32382)));
    DATA_TYPE_INFO_MAP.put(
        "DataTypeSchemaHeader", new DataTypeInfo(new NodeId(0, 15534), new NodeId(0, 15676)));
    DATA_TYPE_INFO_MAP.put(
        "DataTypeDescription", new DataTypeInfo(new NodeId(0, 14525), new NodeId(0, 125)));
    DATA_TYPE_INFO_MAP.put(
        "StructureDescription", new DataTypeInfo(new NodeId(0, 15487), new NodeId(0, 126)));
    DATA_TYPE_INFO_MAP.put(
        "EnumDescription", new DataTypeInfo(new NodeId(0, 15488), new NodeId(0, 127)));
    DATA_TYPE_INFO_MAP.put(
        "SimpleTypeDescription", new DataTypeInfo(new NodeId(0, 15005), new NodeId(0, 15421)));
    DATA_TYPE_INFO_MAP.put(
        "UABinaryFileDataType", new DataTypeInfo(new NodeId(0, 15006), new NodeId(0, 15422)));
    DATA_TYPE_INFO_MAP.put(
        "PortableQualifiedName", new DataTypeInfo(new NodeId(0, 24105), new NodeId(0, 24108)));
    DATA_TYPE_INFO_MAP.put(
        "PortableNodeId", new DataTypeInfo(new NodeId(0, 24106), new NodeId(0, 24109)));
    DATA_TYPE_INFO_MAP.put(
        "UnsignedRationalNumber", new DataTypeInfo(new NodeId(0, 24107), new NodeId(0, 24110)));
    DATA_TYPE_INFO_MAP.put(
        "DataSetMetaDataType", new DataTypeInfo(new NodeId(0, 14523), new NodeId(0, 124)));
    DATA_TYPE_INFO_MAP.put(
        "FieldMetaData", new DataTypeInfo(new NodeId(0, 14524), new NodeId(0, 14839)));
    DATA_TYPE_INFO_MAP.put(
        "ConfigurationVersionDataType",
        new DataTypeInfo(new NodeId(0, 14593), new NodeId(0, 14847)));
    DATA_TYPE_INFO_MAP.put(
        "PublishedDataSetDataType", new DataTypeInfo(new NodeId(0, 15578), new NodeId(0, 15677)));
    DATA_TYPE_INFO_MAP.put(
        "PublishedDataSetSourceDataType",
        new DataTypeInfo(new NodeId(0, 15580), new NodeId(0, 15678)));
    DATA_TYPE_INFO_MAP.put(
        "PublishedVariableDataType", new DataTypeInfo(new NodeId(0, 14273), new NodeId(0, 14323)));
    DATA_TYPE_INFO_MAP.put(
        "PublishedDataItemsDataType", new DataTypeInfo(new NodeId(0, 15581), new NodeId(0, 15679)));
    DATA_TYPE_INFO_MAP.put(
        "PublishedEventsDataType", new DataTypeInfo(new NodeId(0, 15582), new NodeId(0, 15681)));
    DATA_TYPE_INFO_MAP.put(
        "PublishedDataSetCustomSourceDataType",
        new DataTypeInfo(new NodeId(0, 25269), new NodeId(0, 25529)));
    DATA_TYPE_INFO_MAP.put(
        "ActionTargetDataType", new DataTypeInfo(new NodeId(0, 18593), new NodeId(0, 18598)));
    DATA_TYPE_INFO_MAP.put(
        "PublishedActionDataType", new DataTypeInfo(new NodeId(0, 18594), new NodeId(0, 18599)));
    DATA_TYPE_INFO_MAP.put(
        "ActionMethodDataType", new DataTypeInfo(new NodeId(0, 18597), new NodeId(0, 18600)));
    DATA_TYPE_INFO_MAP.put(
        "PublishedActionMethodDataType",
        new DataTypeInfo(new NodeId(0, 18793), new NodeId(0, 18795)));
    DATA_TYPE_INFO_MAP.put(
        "DataSetWriterDataType", new DataTypeInfo(new NodeId(0, 15597), new NodeId(0, 15682)));
    DATA_TYPE_INFO_MAP.put(
        "DataSetWriterTransportDataType",
        new DataTypeInfo(new NodeId(0, 15598), new NodeId(0, 15683)));
    DATA_TYPE_INFO_MAP.put(
        "DataSetWriterMessageDataType",
        new DataTypeInfo(new NodeId(0, 15605), new NodeId(0, 15688)));
    DATA_TYPE_INFO_MAP.put(
        "PubSubGroupDataType", new DataTypeInfo(new NodeId(0, 15609), new NodeId(0, 15689)));
    DATA_TYPE_INFO_MAP.put(
        "WriterGroupDataType", new DataTypeInfo(new NodeId(0, 15480), new NodeId(0, 21150)));
    DATA_TYPE_INFO_MAP.put(
        "WriterGroupTransportDataType",
        new DataTypeInfo(new NodeId(0, 15611), new NodeId(0, 15691)));
    DATA_TYPE_INFO_MAP.put(
        "WriterGroupMessageDataType", new DataTypeInfo(new NodeId(0, 15616), new NodeId(0, 15693)));
    DATA_TYPE_INFO_MAP.put(
        "PubSubConnectionDataType", new DataTypeInfo(new NodeId(0, 15617), new NodeId(0, 15694)));
    DATA_TYPE_INFO_MAP.put(
        "ConnectionTransportDataType",
        new DataTypeInfo(new NodeId(0, 15618), new NodeId(0, 15695)));
    DATA_TYPE_INFO_MAP.put(
        "NetworkAddressDataType", new DataTypeInfo(new NodeId(0, 15502), new NodeId(0, 21151)));
    DATA_TYPE_INFO_MAP.put(
        "NetworkAddressUrlDataType", new DataTypeInfo(new NodeId(0, 15510), new NodeId(0, 21152)));
    DATA_TYPE_INFO_MAP.put(
        "ReaderGroupDataType", new DataTypeInfo(new NodeId(0, 15520), new NodeId(0, 21153)));
    DATA_TYPE_INFO_MAP.put(
        "ReaderGroupTransportDataType",
        new DataTypeInfo(new NodeId(0, 15621), new NodeId(0, 15701)));
    DATA_TYPE_INFO_MAP.put(
        "ReaderGroupMessageDataType", new DataTypeInfo(new NodeId(0, 15622), new NodeId(0, 15702)));
    DATA_TYPE_INFO_MAP.put(
        "DataSetReaderDataType", new DataTypeInfo(new NodeId(0, 15623), new NodeId(0, 15703)));
    DATA_TYPE_INFO_MAP.put(
        "DataSetReaderTransportDataType",
        new DataTypeInfo(new NodeId(0, 15628), new NodeId(0, 15705)));
    DATA_TYPE_INFO_MAP.put(
        "DataSetReaderMessageDataType",
        new DataTypeInfo(new NodeId(0, 15629), new NodeId(0, 15706)));
    DATA_TYPE_INFO_MAP.put(
        "SubscribedDataSetDataType", new DataTypeInfo(new NodeId(0, 15630), new NodeId(0, 15707)));
    DATA_TYPE_INFO_MAP.put(
        "TargetVariablesDataType", new DataTypeInfo(new NodeId(0, 15631), new NodeId(0, 15712)));
    DATA_TYPE_INFO_MAP.put(
        "FieldTargetDataType", new DataTypeInfo(new NodeId(0, 14744), new NodeId(0, 14848)));
    DATA_TYPE_INFO_MAP.put(
        "SubscribedDataSetMirrorDataType",
        new DataTypeInfo(new NodeId(0, 15635), new NodeId(0, 15713)));
    DATA_TYPE_INFO_MAP.put(
        "PubSubConfigurationDataType",
        new DataTypeInfo(new NodeId(0, 15530), new NodeId(0, 21154)));
    DATA_TYPE_INFO_MAP.put(
        "StandaloneSubscribedDataSetRefDataType",
        new DataTypeInfo(new NodeId(0, 23599), new NodeId(0, 23851)));
    DATA_TYPE_INFO_MAP.put(
        "StandaloneSubscribedDataSetDataType",
        new DataTypeInfo(new NodeId(0, 23600), new NodeId(0, 23852)));
    DATA_TYPE_INFO_MAP.put(
        "SecurityGroupDataType", new DataTypeInfo(new NodeId(0, 23601), new NodeId(0, 23853)));
    DATA_TYPE_INFO_MAP.put(
        "PubSubKeyPushTargetDataType",
        new DataTypeInfo(new NodeId(0, 25270), new NodeId(0, 25530)));
    DATA_TYPE_INFO_MAP.put(
        "PubSubConfiguration2DataType",
        new DataTypeInfo(new NodeId(0, 23602), new NodeId(0, 23854)));
    DATA_TYPE_INFO_MAP.put(
        "UadpWriterGroupMessageDataType",
        new DataTypeInfo(new NodeId(0, 15645), new NodeId(0, 15715)));
    DATA_TYPE_INFO_MAP.put(
        "UadpDataSetWriterMessageDataType",
        new DataTypeInfo(new NodeId(0, 15652), new NodeId(0, 15717)));
    DATA_TYPE_INFO_MAP.put(
        "UadpDataSetReaderMessageDataType",
        new DataTypeInfo(new NodeId(0, 15653), new NodeId(0, 15718)));
    DATA_TYPE_INFO_MAP.put(
        "JsonWriterGroupMessageDataType",
        new DataTypeInfo(new NodeId(0, 15657), new NodeId(0, 15719)));
    DATA_TYPE_INFO_MAP.put(
        "JsonDataSetWriterMessageDataType",
        new DataTypeInfo(new NodeId(0, 15664), new NodeId(0, 15724)));
    DATA_TYPE_INFO_MAP.put(
        "JsonDataSetReaderMessageDataType",
        new DataTypeInfo(new NodeId(0, 15665), new NodeId(0, 15725)));
    DATA_TYPE_INFO_MAP.put(
        "QosDataType", new DataTypeInfo(new NodeId(0, 23603), new NodeId(0, 23855)));
    DATA_TYPE_INFO_MAP.put(
        "TransmitQosDataType", new DataTypeInfo(new NodeId(0, 23604), new NodeId(0, 23856)));
    DATA_TYPE_INFO_MAP.put(
        "TransmitQosPriorityDataType",
        new DataTypeInfo(new NodeId(0, 23605), new NodeId(0, 23857)));
    DATA_TYPE_INFO_MAP.put(
        "ReceiveQosDataType", new DataTypeInfo(new NodeId(0, 23608), new NodeId(0, 23860)));
    DATA_TYPE_INFO_MAP.put(
        "ReceiveQosPriorityDataType", new DataTypeInfo(new NodeId(0, 23609), new NodeId(0, 23861)));
    DATA_TYPE_INFO_MAP.put(
        "DatagramConnectionTransportDataType",
        new DataTypeInfo(new NodeId(0, 17467), new NodeId(0, 17468)));
    DATA_TYPE_INFO_MAP.put(
        "DatagramConnectionTransport2DataType",
        new DataTypeInfo(new NodeId(0, 23612), new NodeId(0, 23864)));
    DATA_TYPE_INFO_MAP.put(
        "DatagramWriterGroupTransportDataType",
        new DataTypeInfo(new NodeId(0, 15532), new NodeId(0, 21155)));
    DATA_TYPE_INFO_MAP.put(
        "DatagramWriterGroupTransport2DataType",
        new DataTypeInfo(new NodeId(0, 23613), new NodeId(0, 23865)));
    DATA_TYPE_INFO_MAP.put(
        "DatagramDataSetReaderTransportDataType",
        new DataTypeInfo(new NodeId(0, 23614), new NodeId(0, 23866)));
    DATA_TYPE_INFO_MAP.put(
        "DtlsPubSubConnectionDataType",
        new DataTypeInfo(new NodeId(0, 18794), new NodeId(0, 18930)));
    DATA_TYPE_INFO_MAP.put(
        "BrokerConnectionTransportDataType",
        new DataTypeInfo(new NodeId(0, 15007), new NodeId(0, 15479)));
    DATA_TYPE_INFO_MAP.put(
        "BrokerWriterGroupTransportDataType",
        new DataTypeInfo(new NodeId(0, 15667), new NodeId(0, 15727)));
    DATA_TYPE_INFO_MAP.put(
        "BrokerDataSetWriterTransportDataType",
        new DataTypeInfo(new NodeId(0, 15669), new NodeId(0, 15729)));
    DATA_TYPE_INFO_MAP.put(
        "BrokerDataSetReaderTransportDataType",
        new DataTypeInfo(new NodeId(0, 15670), new NodeId(0, 15733)));
    DATA_TYPE_INFO_MAP.put(
        "PubSubConfigurationRefDataType",
        new DataTypeInfo(new NodeId(0, 25519), new NodeId(0, 25531)));
    DATA_TYPE_INFO_MAP.put(
        "PubSubConfigurationValueDataType",
        new DataTypeInfo(new NodeId(0, 25520), new NodeId(0, 25532)));
    DATA_TYPE_INFO_MAP.put(
        "AliasNameDataType", new DataTypeInfo(new NodeId(0, 23468), new NodeId(0, 23499)));
    DATA_TYPE_INFO_MAP.put(
        "UserManagementDataType", new DataTypeInfo(new NodeId(0, 24281), new NodeId(0, 24292)));
    DATA_TYPE_INFO_MAP.put(
        "PriorityMappingEntryType", new DataTypeInfo(new NodeId(0, 25220), new NodeId(0, 25239)));
    DATA_TYPE_INFO_MAP.put(
        "LldpManagementAddressTxPortType",
        new DataTypeInfo(new NodeId(0, 18953), new NodeId(0, 19079)));
    DATA_TYPE_INFO_MAP.put(
        "LldpManagementAddressType", new DataTypeInfo(new NodeId(0, 18954), new NodeId(0, 19080)));
    DATA_TYPE_INFO_MAP.put(
        "LldpTlvType", new DataTypeInfo(new NodeId(0, 18955), new NodeId(0, 19081)));
    DATA_TYPE_INFO_MAP.put(
        "ReferenceDescriptionDataType",
        new DataTypeInfo(new NodeId(0, 32659), new NodeId(0, 32661)));
    DATA_TYPE_INFO_MAP.put(
        "ReferenceListEntryDataType", new DataTypeInfo(new NodeId(0, 32660), new NodeId(0, 32662)));
    DATA_TYPE_INFO_MAP.put(
        "RolePermissionType", new DataTypeInfo(new NodeId(0, 96), new NodeId(0, 128)));
    DATA_TYPE_INFO_MAP.put(
        "DataTypeDefinition", new DataTypeInfo(new NodeId(0, 97), new NodeId(0, 121)));
    DATA_TYPE_INFO_MAP.put(
        "StructureField", new DataTypeInfo(new NodeId(0, 101), new NodeId(0, 14844)));
    DATA_TYPE_INFO_MAP.put(
        "StructureDefinition", new DataTypeInfo(new NodeId(0, 99), new NodeId(0, 122)));
    DATA_TYPE_INFO_MAP.put(
        "EnumDefinition", new DataTypeInfo(new NodeId(0, 100), new NodeId(0, 123)));
    DATA_TYPE_INFO_MAP.put("Argument", new DataTypeInfo(new NodeId(0, 296), new NodeId(0, 298)));
    DATA_TYPE_INFO_MAP.put(
        "EnumValueType", new DataTypeInfo(new NodeId(0, 7594), new NodeId(0, 8251)));
    DATA_TYPE_INFO_MAP.put("EnumField", new DataTypeInfo(new NodeId(0, 102), new NodeId(0, 14845)));
    DATA_TYPE_INFO_MAP.put(
        "OptionSet", new DataTypeInfo(new NodeId(0, 12755), new NodeId(0, 12765)));
    DATA_TYPE_INFO_MAP.put(
        "TimeZoneDataType", new DataTypeInfo(new NodeId(0, 8912), new NodeId(0, 8917)));
    DATA_TYPE_INFO_MAP.put(
        "ApplicationDescription", new DataTypeInfo(new NodeId(0, 308), new NodeId(0, 310)));
    DATA_TYPE_INFO_MAP.put(
        "ServerOnNetwork", new DataTypeInfo(new NodeId(0, 12189), new NodeId(0, 12207)));
    DATA_TYPE_INFO_MAP.put(
        "UserTokenPolicy", new DataTypeInfo(new NodeId(0, 304), new NodeId(0, 306)));
    DATA_TYPE_INFO_MAP.put(
        "EndpointDescription", new DataTypeInfo(new NodeId(0, 312), new NodeId(0, 314)));
    DATA_TYPE_INFO_MAP.put(
        "RegisteredServer", new DataTypeInfo(new NodeId(0, 432), new NodeId(0, 434)));
    DATA_TYPE_INFO_MAP.put(
        "DiscoveryConfiguration", new DataTypeInfo(new NodeId(0, 12890), new NodeId(0, 12900)));
    DATA_TYPE_INFO_MAP.put(
        "MdnsDiscoveryConfiguration", new DataTypeInfo(new NodeId(0, 12891), new NodeId(0, 12901)));
    DATA_TYPE_INFO_MAP.put(
        "SignedSoftwareCertificate", new DataTypeInfo(new NodeId(0, 344), new NodeId(0, 346)));
    DATA_TYPE_INFO_MAP.put(
        "UserIdentityToken", new DataTypeInfo(new NodeId(0, 316), new NodeId(0, 318)));
    DATA_TYPE_INFO_MAP.put(
        "AnonymousIdentityToken", new DataTypeInfo(new NodeId(0, 319), new NodeId(0, 321)));
    DATA_TYPE_INFO_MAP.put(
        "UserNameIdentityToken", new DataTypeInfo(new NodeId(0, 322), new NodeId(0, 324)));
    DATA_TYPE_INFO_MAP.put(
        "X509IdentityToken", new DataTypeInfo(new NodeId(0, 325), new NodeId(0, 327)));
    DATA_TYPE_INFO_MAP.put(
        "IssuedIdentityToken", new DataTypeInfo(new NodeId(0, 938), new NodeId(0, 940)));
    DATA_TYPE_INFO_MAP.put(
        "AddNodesItem", new DataTypeInfo(new NodeId(0, 376), new NodeId(0, 378)));
    DATA_TYPE_INFO_MAP.put(
        "AddReferencesItem", new DataTypeInfo(new NodeId(0, 379), new NodeId(0, 381)));
    DATA_TYPE_INFO_MAP.put(
        "DeleteNodesItem", new DataTypeInfo(new NodeId(0, 382), new NodeId(0, 384)));
    DATA_TYPE_INFO_MAP.put(
        "DeleteReferencesItem", new DataTypeInfo(new NodeId(0, 385), new NodeId(0, 387)));
    DATA_TYPE_INFO_MAP.put(
        "RelativePathElement", new DataTypeInfo(new NodeId(0, 537), new NodeId(0, 539)));
    DATA_TYPE_INFO_MAP.put(
        "RelativePath", new DataTypeInfo(new NodeId(0, 540), new NodeId(0, 542)));
    DATA_TYPE_INFO_MAP.put(
        "EndpointConfiguration", new DataTypeInfo(new NodeId(0, 331), new NodeId(0, 333)));
    DATA_TYPE_INFO_MAP.put(
        "ContentFilterElement", new DataTypeInfo(new NodeId(0, 583), new NodeId(0, 585)));
    DATA_TYPE_INFO_MAP.put(
        "ContentFilter", new DataTypeInfo(new NodeId(0, 586), new NodeId(0, 588)));
    DATA_TYPE_INFO_MAP.put(
        "FilterOperand", new DataTypeInfo(new NodeId(0, 589), new NodeId(0, 591)));
    DATA_TYPE_INFO_MAP.put(
        "ElementOperand", new DataTypeInfo(new NodeId(0, 592), new NodeId(0, 594)));
    DATA_TYPE_INFO_MAP.put(
        "LiteralOperand", new DataTypeInfo(new NodeId(0, 595), new NodeId(0, 597)));
    DATA_TYPE_INFO_MAP.put(
        "AttributeOperand", new DataTypeInfo(new NodeId(0, 598), new NodeId(0, 600)));
    DATA_TYPE_INFO_MAP.put(
        "SimpleAttributeOperand", new DataTypeInfo(new NodeId(0, 601), new NodeId(0, 603)));
    DATA_TYPE_INFO_MAP.put(
        "ModificationInfo", new DataTypeInfo(new NodeId(0, 11216), new NodeId(0, 11226)));
    DATA_TYPE_INFO_MAP.put(
        "HistoryEvent", new DataTypeInfo(new NodeId(0, 659), new NodeId(0, 661)));
    DATA_TYPE_INFO_MAP.put(
        "HistoryModifiedEvent", new DataTypeInfo(new NodeId(0, 32824), new NodeId(0, 32825)));
    DATA_TYPE_INFO_MAP.put(
        "MonitoringFilter", new DataTypeInfo(new NodeId(0, 719), new NodeId(0, 721)));
    DATA_TYPE_INFO_MAP.put("EventFilter", new DataTypeInfo(new NodeId(0, 725), new NodeId(0, 727)));
    DATA_TYPE_INFO_MAP.put(
        "AggregateConfiguration", new DataTypeInfo(new NodeId(0, 948), new NodeId(0, 950)));
    DATA_TYPE_INFO_MAP.put(
        "HistoryEventFieldList", new DataTypeInfo(new NodeId(0, 920), new NodeId(0, 922)));
    DATA_TYPE_INFO_MAP.put("BuildInfo", new DataTypeInfo(new NodeId(0, 338), new NodeId(0, 340)));
    DATA_TYPE_INFO_MAP.put(
        "RedundantServerDataType", new DataTypeInfo(new NodeId(0, 853), new NodeId(0, 855)));
    DATA_TYPE_INFO_MAP.put(
        "EndpointUrlListDataType", new DataTypeInfo(new NodeId(0, 11943), new NodeId(0, 11957)));
    DATA_TYPE_INFO_MAP.put(
        "NetworkGroupDataType", new DataTypeInfo(new NodeId(0, 11944), new NodeId(0, 11958)));
    DATA_TYPE_INFO_MAP.put(
        "SamplingIntervalDiagnosticsDataType",
        new DataTypeInfo(new NodeId(0, 856), new NodeId(0, 858)));
    DATA_TYPE_INFO_MAP.put(
        "ServerDiagnosticsSummaryDataType",
        new DataTypeInfo(new NodeId(0, 859), new NodeId(0, 861)));
    DATA_TYPE_INFO_MAP.put(
        "ServerStatusDataType", new DataTypeInfo(new NodeId(0, 862), new NodeId(0, 864)));
    DATA_TYPE_INFO_MAP.put(
        "SessionDiagnosticsDataType", new DataTypeInfo(new NodeId(0, 865), new NodeId(0, 867)));
    DATA_TYPE_INFO_MAP.put(
        "SessionSecurityDiagnosticsDataType",
        new DataTypeInfo(new NodeId(0, 868), new NodeId(0, 870)));
    DATA_TYPE_INFO_MAP.put(
        "ServiceCounterDataType", new DataTypeInfo(new NodeId(0, 871), new NodeId(0, 873)));
    DATA_TYPE_INFO_MAP.put(
        "StatusResult", new DataTypeInfo(new NodeId(0, 299), new NodeId(0, 301)));
    DATA_TYPE_INFO_MAP.put(
        "SubscriptionDiagnosticsDataType",
        new DataTypeInfo(new NodeId(0, 874), new NodeId(0, 876)));
    DATA_TYPE_INFO_MAP.put(
        "ModelChangeStructureDataType", new DataTypeInfo(new NodeId(0, 877), new NodeId(0, 879)));
    DATA_TYPE_INFO_MAP.put(
        "SemanticChangeStructureDataType",
        new DataTypeInfo(new NodeId(0, 897), new NodeId(0, 899)));
    DATA_TYPE_INFO_MAP.put("Range", new DataTypeInfo(new NodeId(0, 884), new NodeId(0, 886)));
    DATA_TYPE_INFO_MAP.put(
        "EUInformation", new DataTypeInfo(new NodeId(0, 887), new NodeId(0, 889)));
    DATA_TYPE_INFO_MAP.put(
        "ComplexNumberType", new DataTypeInfo(new NodeId(0, 12171), new NodeId(0, 12181)));
    DATA_TYPE_INFO_MAP.put(
        "DoubleComplexNumberType", new DataTypeInfo(new NodeId(0, 12172), new NodeId(0, 12182)));
    DATA_TYPE_INFO_MAP.put(
        "AxisInformation", new DataTypeInfo(new NodeId(0, 12079), new NodeId(0, 12089)));
    DATA_TYPE_INFO_MAP.put("XVType", new DataTypeInfo(new NodeId(0, 12080), new NodeId(0, 12090)));
    DATA_TYPE_INFO_MAP.put(
        "ProgramDiagnosticDataType", new DataTypeInfo(new NodeId(0, 894), new NodeId(0, 896)));
    DATA_TYPE_INFO_MAP.put(
        "ProgramDiagnostic2DataType", new DataTypeInfo(new NodeId(0, 24033), new NodeId(0, 24034)));
    DATA_TYPE_INFO_MAP.put("Annotation", new DataTypeInfo(new NodeId(0, 891), new NodeId(0, 893)));
    DATA_TYPE_INFO_MAP.put(
        "DecimalDataType", new DataTypeInfo(new NodeId(0, 17861), new NodeId(0, 17863)));
    DATA_TYPE_INFO_MAP.put("Node", new DataTypeInfo(new NodeId(0, 258), new NodeId(0, 260)));
    DATA_TYPE_INFO_MAP.put(
        "InstanceNode", new DataTypeInfo(new NodeId(0, 11879), new NodeId(0, 11889)));
    DATA_TYPE_INFO_MAP.put(
        "TypeNode", new DataTypeInfo(new NodeId(0, 11880), new NodeId(0, 11890)));
    DATA_TYPE_INFO_MAP.put("ObjectNode", new DataTypeInfo(new NodeId(0, 261), new NodeId(0, 263)));
    DATA_TYPE_INFO_MAP.put(
        "ObjectTypeNode", new DataTypeInfo(new NodeId(0, 264), new NodeId(0, 266)));
    DATA_TYPE_INFO_MAP.put(
        "VariableNode", new DataTypeInfo(new NodeId(0, 267), new NodeId(0, 269)));
    DATA_TYPE_INFO_MAP.put(
        "VariableTypeNode", new DataTypeInfo(new NodeId(0, 270), new NodeId(0, 272)));
    DATA_TYPE_INFO_MAP.put(
        "ReferenceTypeNode", new DataTypeInfo(new NodeId(0, 273), new NodeId(0, 275)));
    DATA_TYPE_INFO_MAP.put("MethodNode", new DataTypeInfo(new NodeId(0, 276), new NodeId(0, 278)));
    DATA_TYPE_INFO_MAP.put("ViewNode", new DataTypeInfo(new NodeId(0, 279), new NodeId(0, 281)));
    DATA_TYPE_INFO_MAP.put(
        "DataTypeNode", new DataTypeInfo(new NodeId(0, 282), new NodeId(0, 284)));
    DATA_TYPE_INFO_MAP.put(
        "ReferenceNode", new DataTypeInfo(new NodeId(0, 285), new NodeId(0, 287)));
    DATA_TYPE_INFO_MAP.put(
        "RequestHeader", new DataTypeInfo(new NodeId(0, 389), new NodeId(0, 391)));
    DATA_TYPE_INFO_MAP.put(
        "ResponseHeader", new DataTypeInfo(new NodeId(0, 392), new NodeId(0, 394)));
    DATA_TYPE_INFO_MAP.put(
        "ServiceFault", new DataTypeInfo(new NodeId(0, 395), new NodeId(0, 397)));
    DATA_TYPE_INFO_MAP.put(
        "SessionlessInvokeRequestType",
        new DataTypeInfo(new NodeId(0, 15901), new NodeId(0, 15903)));
    DATA_TYPE_INFO_MAP.put(
        "SessionlessInvokeResponseType",
        new DataTypeInfo(new NodeId(0, 20999), new NodeId(0, 21001)));
    DATA_TYPE_INFO_MAP.put(
        "FindServersRequest", new DataTypeInfo(new NodeId(0, 420), new NodeId(0, 422)));
    DATA_TYPE_INFO_MAP.put(
        "FindServersResponse", new DataTypeInfo(new NodeId(0, 423), new NodeId(0, 425)));
    DATA_TYPE_INFO_MAP.put(
        "FindServersOnNetworkRequest",
        new DataTypeInfo(new NodeId(0, 12190), new NodeId(0, 12208)));
    DATA_TYPE_INFO_MAP.put(
        "FindServersOnNetworkResponse",
        new DataTypeInfo(new NodeId(0, 12191), new NodeId(0, 12209)));
    DATA_TYPE_INFO_MAP.put(
        "GetEndpointsRequest", new DataTypeInfo(new NodeId(0, 426), new NodeId(0, 428)));
    DATA_TYPE_INFO_MAP.put(
        "GetEndpointsResponse", new DataTypeInfo(new NodeId(0, 429), new NodeId(0, 431)));
    DATA_TYPE_INFO_MAP.put(
        "RegisterServerRequest", new DataTypeInfo(new NodeId(0, 435), new NodeId(0, 437)));
    DATA_TYPE_INFO_MAP.put(
        "RegisterServerResponse", new DataTypeInfo(new NodeId(0, 438), new NodeId(0, 440)));
    DATA_TYPE_INFO_MAP.put(
        "RegisterServer2Request", new DataTypeInfo(new NodeId(0, 12193), new NodeId(0, 12211)));
    DATA_TYPE_INFO_MAP.put(
        "RegisterServer2Response", new DataTypeInfo(new NodeId(0, 12194), new NodeId(0, 12212)));
    DATA_TYPE_INFO_MAP.put(
        "ChannelSecurityToken", new DataTypeInfo(new NodeId(0, 441), new NodeId(0, 443)));
    DATA_TYPE_INFO_MAP.put(
        "OpenSecureChannelRequest", new DataTypeInfo(new NodeId(0, 444), new NodeId(0, 446)));
    DATA_TYPE_INFO_MAP.put(
        "OpenSecureChannelResponse", new DataTypeInfo(new NodeId(0, 447), new NodeId(0, 449)));
    DATA_TYPE_INFO_MAP.put(
        "CloseSecureChannelRequest", new DataTypeInfo(new NodeId(0, 450), new NodeId(0, 452)));
    DATA_TYPE_INFO_MAP.put(
        "CloseSecureChannelResponse", new DataTypeInfo(new NodeId(0, 453), new NodeId(0, 455)));
    DATA_TYPE_INFO_MAP.put(
        "SignatureData", new DataTypeInfo(new NodeId(0, 456), new NodeId(0, 458)));
    DATA_TYPE_INFO_MAP.put(
        "CreateSessionRequest", new DataTypeInfo(new NodeId(0, 459), new NodeId(0, 461)));
    DATA_TYPE_INFO_MAP.put(
        "CreateSessionResponse", new DataTypeInfo(new NodeId(0, 462), new NodeId(0, 464)));
    DATA_TYPE_INFO_MAP.put(
        "ActivateSessionRequest", new DataTypeInfo(new NodeId(0, 465), new NodeId(0, 467)));
    DATA_TYPE_INFO_MAP.put(
        "ActivateSessionResponse", new DataTypeInfo(new NodeId(0, 468), new NodeId(0, 470)));
    DATA_TYPE_INFO_MAP.put(
        "CloseSessionRequest", new DataTypeInfo(new NodeId(0, 471), new NodeId(0, 473)));
    DATA_TYPE_INFO_MAP.put(
        "CloseSessionResponse", new DataTypeInfo(new NodeId(0, 474), new NodeId(0, 476)));
    DATA_TYPE_INFO_MAP.put(
        "CancelRequest", new DataTypeInfo(new NodeId(0, 477), new NodeId(0, 479)));
    DATA_TYPE_INFO_MAP.put(
        "CancelResponse", new DataTypeInfo(new NodeId(0, 480), new NodeId(0, 482)));
    DATA_TYPE_INFO_MAP.put(
        "NodeAttributes", new DataTypeInfo(new NodeId(0, 349), new NodeId(0, 351)));
    DATA_TYPE_INFO_MAP.put(
        "ObjectAttributes", new DataTypeInfo(new NodeId(0, 352), new NodeId(0, 354)));
    DATA_TYPE_INFO_MAP.put(
        "VariableAttributes", new DataTypeInfo(new NodeId(0, 355), new NodeId(0, 357)));
    DATA_TYPE_INFO_MAP.put(
        "MethodAttributes", new DataTypeInfo(new NodeId(0, 358), new NodeId(0, 360)));
    DATA_TYPE_INFO_MAP.put(
        "ObjectTypeAttributes", new DataTypeInfo(new NodeId(0, 361), new NodeId(0, 363)));
    DATA_TYPE_INFO_MAP.put(
        "VariableTypeAttributes", new DataTypeInfo(new NodeId(0, 364), new NodeId(0, 366)));
    DATA_TYPE_INFO_MAP.put(
        "ReferenceTypeAttributes", new DataTypeInfo(new NodeId(0, 367), new NodeId(0, 369)));
    DATA_TYPE_INFO_MAP.put(
        "DataTypeAttributes", new DataTypeInfo(new NodeId(0, 370), new NodeId(0, 372)));
    DATA_TYPE_INFO_MAP.put(
        "ViewAttributes", new DataTypeInfo(new NodeId(0, 373), new NodeId(0, 375)));
    DATA_TYPE_INFO_MAP.put(
        "GenericAttributeValue", new DataTypeInfo(new NodeId(0, 17606), new NodeId(0, 17610)));
    DATA_TYPE_INFO_MAP.put(
        "GenericAttributes", new DataTypeInfo(new NodeId(0, 17607), new NodeId(0, 17611)));
    DATA_TYPE_INFO_MAP.put(
        "AddNodesResult", new DataTypeInfo(new NodeId(0, 483), new NodeId(0, 485)));
    DATA_TYPE_INFO_MAP.put(
        "AddNodesRequest", new DataTypeInfo(new NodeId(0, 486), new NodeId(0, 488)));
    DATA_TYPE_INFO_MAP.put(
        "AddNodesResponse", new DataTypeInfo(new NodeId(0, 489), new NodeId(0, 491)));
    DATA_TYPE_INFO_MAP.put(
        "AddReferencesRequest", new DataTypeInfo(new NodeId(0, 492), new NodeId(0, 494)));
    DATA_TYPE_INFO_MAP.put(
        "AddReferencesResponse", new DataTypeInfo(new NodeId(0, 495), new NodeId(0, 497)));
    DATA_TYPE_INFO_MAP.put(
        "DeleteNodesRequest", new DataTypeInfo(new NodeId(0, 498), new NodeId(0, 500)));
    DATA_TYPE_INFO_MAP.put(
        "DeleteNodesResponse", new DataTypeInfo(new NodeId(0, 501), new NodeId(0, 503)));
    DATA_TYPE_INFO_MAP.put(
        "DeleteReferencesRequest", new DataTypeInfo(new NodeId(0, 504), new NodeId(0, 506)));
    DATA_TYPE_INFO_MAP.put(
        "DeleteReferencesResponse", new DataTypeInfo(new NodeId(0, 507), new NodeId(0, 509)));
    DATA_TYPE_INFO_MAP.put(
        "ViewDescription", new DataTypeInfo(new NodeId(0, 511), new NodeId(0, 513)));
    DATA_TYPE_INFO_MAP.put(
        "BrowseDescription", new DataTypeInfo(new NodeId(0, 514), new NodeId(0, 516)));
    DATA_TYPE_INFO_MAP.put(
        "ReferenceDescription", new DataTypeInfo(new NodeId(0, 518), new NodeId(0, 520)));
    DATA_TYPE_INFO_MAP.put(
        "BrowseResult", new DataTypeInfo(new NodeId(0, 522), new NodeId(0, 524)));
    DATA_TYPE_INFO_MAP.put(
        "BrowseRequest", new DataTypeInfo(new NodeId(0, 525), new NodeId(0, 527)));
    DATA_TYPE_INFO_MAP.put(
        "BrowseResponse", new DataTypeInfo(new NodeId(0, 528), new NodeId(0, 530)));
    DATA_TYPE_INFO_MAP.put(
        "BrowseNextRequest", new DataTypeInfo(new NodeId(0, 531), new NodeId(0, 533)));
    DATA_TYPE_INFO_MAP.put(
        "BrowseNextResponse", new DataTypeInfo(new NodeId(0, 534), new NodeId(0, 536)));
    DATA_TYPE_INFO_MAP.put("BrowsePath", new DataTypeInfo(new NodeId(0, 543), new NodeId(0, 545)));
    DATA_TYPE_INFO_MAP.put(
        "BrowsePathTarget", new DataTypeInfo(new NodeId(0, 546), new NodeId(0, 548)));
    DATA_TYPE_INFO_MAP.put(
        "BrowsePathResult", new DataTypeInfo(new NodeId(0, 549), new NodeId(0, 551)));
    DATA_TYPE_INFO_MAP.put(
        "TranslateBrowsePathsToNodeIdsRequest",
        new DataTypeInfo(new NodeId(0, 552), new NodeId(0, 554)));
    DATA_TYPE_INFO_MAP.put(
        "TranslateBrowsePathsToNodeIdsResponse",
        new DataTypeInfo(new NodeId(0, 555), new NodeId(0, 557)));
    DATA_TYPE_INFO_MAP.put(
        "RegisterNodesRequest", new DataTypeInfo(new NodeId(0, 558), new NodeId(0, 560)));
    DATA_TYPE_INFO_MAP.put(
        "RegisterNodesResponse", new DataTypeInfo(new NodeId(0, 561), new NodeId(0, 563)));
    DATA_TYPE_INFO_MAP.put(
        "UnregisterNodesRequest", new DataTypeInfo(new NodeId(0, 564), new NodeId(0, 566)));
    DATA_TYPE_INFO_MAP.put(
        "UnregisterNodesResponse", new DataTypeInfo(new NodeId(0, 567), new NodeId(0, 569)));
    DATA_TYPE_INFO_MAP.put(
        "QueryDataDescription", new DataTypeInfo(new NodeId(0, 570), new NodeId(0, 572)));
    DATA_TYPE_INFO_MAP.put(
        "NodeTypeDescription", new DataTypeInfo(new NodeId(0, 573), new NodeId(0, 575)));
    DATA_TYPE_INFO_MAP.put(
        "QueryDataSet", new DataTypeInfo(new NodeId(0, 577), new NodeId(0, 579)));
    DATA_TYPE_INFO_MAP.put(
        "NodeReference", new DataTypeInfo(new NodeId(0, 580), new NodeId(0, 582)));
    DATA_TYPE_INFO_MAP.put(
        "ContentFilterElementResult", new DataTypeInfo(new NodeId(0, 604), new NodeId(0, 606)));
    DATA_TYPE_INFO_MAP.put(
        "ContentFilterResult", new DataTypeInfo(new NodeId(0, 607), new NodeId(0, 609)));
    DATA_TYPE_INFO_MAP.put(
        "ParsingResult", new DataTypeInfo(new NodeId(0, 610), new NodeId(0, 612)));
    DATA_TYPE_INFO_MAP.put(
        "QueryFirstRequest", new DataTypeInfo(new NodeId(0, 613), new NodeId(0, 615)));
    DATA_TYPE_INFO_MAP.put(
        "QueryFirstResponse", new DataTypeInfo(new NodeId(0, 616), new NodeId(0, 618)));
    DATA_TYPE_INFO_MAP.put(
        "QueryNextRequest", new DataTypeInfo(new NodeId(0, 619), new NodeId(0, 621)));
    DATA_TYPE_INFO_MAP.put(
        "QueryNextResponse", new DataTypeInfo(new NodeId(0, 622), new NodeId(0, 624)));
    DATA_TYPE_INFO_MAP.put("ReadValueId", new DataTypeInfo(new NodeId(0, 626), new NodeId(0, 628)));
    DATA_TYPE_INFO_MAP.put("ReadRequest", new DataTypeInfo(new NodeId(0, 629), new NodeId(0, 631)));
    DATA_TYPE_INFO_MAP.put(
        "ReadResponse", new DataTypeInfo(new NodeId(0, 632), new NodeId(0, 634)));
    DATA_TYPE_INFO_MAP.put(
        "HistoryReadValueId", new DataTypeInfo(new NodeId(0, 635), new NodeId(0, 637)));
    DATA_TYPE_INFO_MAP.put(
        "HistoryReadResult", new DataTypeInfo(new NodeId(0, 638), new NodeId(0, 640)));
    DATA_TYPE_INFO_MAP.put(
        "HistoryReadDetails", new DataTypeInfo(new NodeId(0, 641), new NodeId(0, 643)));
    DATA_TYPE_INFO_MAP.put(
        "ReadEventDetails", new DataTypeInfo(new NodeId(0, 644), new NodeId(0, 646)));
    DATA_TYPE_INFO_MAP.put(
        "ReadEventDetails2", new DataTypeInfo(new NodeId(0, 32799), new NodeId(0, 32800)));
    DATA_TYPE_INFO_MAP.put(
        "SortRuleElement", new DataTypeInfo(new NodeId(0, 18648), new NodeId(0, 18650)));
    DATA_TYPE_INFO_MAP.put(
        "ReadEventDetailsSorted", new DataTypeInfo(new NodeId(0, 18649), new NodeId(0, 18651)));
    DATA_TYPE_INFO_MAP.put(
        "ReadRawModifiedDetails", new DataTypeInfo(new NodeId(0, 647), new NodeId(0, 649)));
    DATA_TYPE_INFO_MAP.put(
        "ReadProcessedDetails", new DataTypeInfo(new NodeId(0, 650), new NodeId(0, 652)));
    DATA_TYPE_INFO_MAP.put(
        "ReadAtTimeDetails", new DataTypeInfo(new NodeId(0, 653), new NodeId(0, 655)));
    DATA_TYPE_INFO_MAP.put(
        "ReadAnnotationDataDetails", new DataTypeInfo(new NodeId(0, 23497), new NodeId(0, 23500)));
    DATA_TYPE_INFO_MAP.put("HistoryData", new DataTypeInfo(new NodeId(0, 656), new NodeId(0, 658)));
    DATA_TYPE_INFO_MAP.put(
        "HistoryModifiedData", new DataTypeInfo(new NodeId(0, 11217), new NodeId(0, 11227)));
    DATA_TYPE_INFO_MAP.put(
        "HistoryReadRequest", new DataTypeInfo(new NodeId(0, 662), new NodeId(0, 664)));
    DATA_TYPE_INFO_MAP.put(
        "HistoryReadResponse", new DataTypeInfo(new NodeId(0, 665), new NodeId(0, 667)));
    DATA_TYPE_INFO_MAP.put("WriteValue", new DataTypeInfo(new NodeId(0, 668), new NodeId(0, 670)));
    DATA_TYPE_INFO_MAP.put(
        "WriteRequest", new DataTypeInfo(new NodeId(0, 671), new NodeId(0, 673)));
    DATA_TYPE_INFO_MAP.put(
        "WriteResponse", new DataTypeInfo(new NodeId(0, 674), new NodeId(0, 676)));
    DATA_TYPE_INFO_MAP.put(
        "HistoryUpdateDetails", new DataTypeInfo(new NodeId(0, 677), new NodeId(0, 679)));
    DATA_TYPE_INFO_MAP.put(
        "UpdateDataDetails", new DataTypeInfo(new NodeId(0, 680), new NodeId(0, 682)));
    DATA_TYPE_INFO_MAP.put(
        "UpdateStructureDataDetails", new DataTypeInfo(new NodeId(0, 11295), new NodeId(0, 11300)));
    DATA_TYPE_INFO_MAP.put(
        "UpdateEventDetails", new DataTypeInfo(new NodeId(0, 683), new NodeId(0, 685)));
    DATA_TYPE_INFO_MAP.put(
        "DeleteRawModifiedDetails", new DataTypeInfo(new NodeId(0, 686), new NodeId(0, 688)));
    DATA_TYPE_INFO_MAP.put(
        "DeleteAtTimeDetails", new DataTypeInfo(new NodeId(0, 689), new NodeId(0, 691)));
    DATA_TYPE_INFO_MAP.put(
        "DeleteEventDetails", new DataTypeInfo(new NodeId(0, 692), new NodeId(0, 694)));
    DATA_TYPE_INFO_MAP.put(
        "HistoryUpdateResult", new DataTypeInfo(new NodeId(0, 695), new NodeId(0, 697)));
    DATA_TYPE_INFO_MAP.put(
        "HistoryUpdateRequest", new DataTypeInfo(new NodeId(0, 698), new NodeId(0, 700)));
    DATA_TYPE_INFO_MAP.put(
        "HistoryUpdateResponse", new DataTypeInfo(new NodeId(0, 701), new NodeId(0, 703)));
    DATA_TYPE_INFO_MAP.put(
        "CallMethodRequest", new DataTypeInfo(new NodeId(0, 704), new NodeId(0, 706)));
    DATA_TYPE_INFO_MAP.put(
        "CallMethodResult", new DataTypeInfo(new NodeId(0, 707), new NodeId(0, 709)));
    DATA_TYPE_INFO_MAP.put("CallRequest", new DataTypeInfo(new NodeId(0, 710), new NodeId(0, 712)));
    DATA_TYPE_INFO_MAP.put(
        "CallResponse", new DataTypeInfo(new NodeId(0, 713), new NodeId(0, 715)));
    DATA_TYPE_INFO_MAP.put(
        "DataChangeFilter", new DataTypeInfo(new NodeId(0, 722), new NodeId(0, 724)));
    DATA_TYPE_INFO_MAP.put(
        "AggregateFilter", new DataTypeInfo(new NodeId(0, 728), new NodeId(0, 730)));
    DATA_TYPE_INFO_MAP.put(
        "MonitoringFilterResult", new DataTypeInfo(new NodeId(0, 731), new NodeId(0, 733)));
    DATA_TYPE_INFO_MAP.put(
        "EventFilterResult", new DataTypeInfo(new NodeId(0, 734), new NodeId(0, 736)));
    DATA_TYPE_INFO_MAP.put(
        "AggregateFilterResult", new DataTypeInfo(new NodeId(0, 737), new NodeId(0, 739)));
    DATA_TYPE_INFO_MAP.put(
        "MonitoringParameters", new DataTypeInfo(new NodeId(0, 740), new NodeId(0, 742)));
    DATA_TYPE_INFO_MAP.put(
        "MonitoredItemCreateRequest", new DataTypeInfo(new NodeId(0, 743), new NodeId(0, 745)));
    DATA_TYPE_INFO_MAP.put(
        "MonitoredItemCreateResult", new DataTypeInfo(new NodeId(0, 746), new NodeId(0, 748)));
    DATA_TYPE_INFO_MAP.put(
        "CreateMonitoredItemsRequest", new DataTypeInfo(new NodeId(0, 749), new NodeId(0, 751)));
    DATA_TYPE_INFO_MAP.put(
        "CreateMonitoredItemsResponse", new DataTypeInfo(new NodeId(0, 752), new NodeId(0, 754)));
    DATA_TYPE_INFO_MAP.put(
        "MonitoredItemModifyRequest", new DataTypeInfo(new NodeId(0, 755), new NodeId(0, 757)));
    DATA_TYPE_INFO_MAP.put(
        "MonitoredItemModifyResult", new DataTypeInfo(new NodeId(0, 758), new NodeId(0, 760)));
    DATA_TYPE_INFO_MAP.put(
        "ModifyMonitoredItemsRequest", new DataTypeInfo(new NodeId(0, 761), new NodeId(0, 763)));
    DATA_TYPE_INFO_MAP.put(
        "ModifyMonitoredItemsResponse", new DataTypeInfo(new NodeId(0, 764), new NodeId(0, 766)));
    DATA_TYPE_INFO_MAP.put(
        "SetMonitoringModeRequest", new DataTypeInfo(new NodeId(0, 767), new NodeId(0, 769)));
    DATA_TYPE_INFO_MAP.put(
        "SetMonitoringModeResponse", new DataTypeInfo(new NodeId(0, 770), new NodeId(0, 772)));
    DATA_TYPE_INFO_MAP.put(
        "SetTriggeringRequest", new DataTypeInfo(new NodeId(0, 773), new NodeId(0, 775)));
    DATA_TYPE_INFO_MAP.put(
        "SetTriggeringResponse", new DataTypeInfo(new NodeId(0, 776), new NodeId(0, 778)));
    DATA_TYPE_INFO_MAP.put(
        "DeleteMonitoredItemsRequest", new DataTypeInfo(new NodeId(0, 779), new NodeId(0, 781)));
    DATA_TYPE_INFO_MAP.put(
        "DeleteMonitoredItemsResponse", new DataTypeInfo(new NodeId(0, 782), new NodeId(0, 784)));
    DATA_TYPE_INFO_MAP.put(
        "CreateSubscriptionRequest", new DataTypeInfo(new NodeId(0, 785), new NodeId(0, 787)));
    DATA_TYPE_INFO_MAP.put(
        "CreateSubscriptionResponse", new DataTypeInfo(new NodeId(0, 788), new NodeId(0, 790)));
    DATA_TYPE_INFO_MAP.put(
        "ModifySubscriptionRequest", new DataTypeInfo(new NodeId(0, 791), new NodeId(0, 793)));
    DATA_TYPE_INFO_MAP.put(
        "ModifySubscriptionResponse", new DataTypeInfo(new NodeId(0, 794), new NodeId(0, 796)));
    DATA_TYPE_INFO_MAP.put(
        "SetPublishingModeRequest", new DataTypeInfo(new NodeId(0, 797), new NodeId(0, 799)));
    DATA_TYPE_INFO_MAP.put(
        "SetPublishingModeResponse", new DataTypeInfo(new NodeId(0, 800), new NodeId(0, 802)));
    DATA_TYPE_INFO_MAP.put(
        "NotificationMessage", new DataTypeInfo(new NodeId(0, 803), new NodeId(0, 805)));
    DATA_TYPE_INFO_MAP.put(
        "NotificationData", new DataTypeInfo(new NodeId(0, 945), new NodeId(0, 947)));
    DATA_TYPE_INFO_MAP.put(
        "DataChangeNotification", new DataTypeInfo(new NodeId(0, 809), new NodeId(0, 811)));
    DATA_TYPE_INFO_MAP.put(
        "MonitoredItemNotification", new DataTypeInfo(new NodeId(0, 806), new NodeId(0, 808)));
    DATA_TYPE_INFO_MAP.put(
        "EventNotificationList", new DataTypeInfo(new NodeId(0, 914), new NodeId(0, 916)));
    DATA_TYPE_INFO_MAP.put(
        "EventFieldList", new DataTypeInfo(new NodeId(0, 917), new NodeId(0, 919)));
    DATA_TYPE_INFO_MAP.put(
        "StatusChangeNotification", new DataTypeInfo(new NodeId(0, 818), new NodeId(0, 820)));
    DATA_TYPE_INFO_MAP.put(
        "SubscriptionAcknowledgement", new DataTypeInfo(new NodeId(0, 821), new NodeId(0, 823)));
    DATA_TYPE_INFO_MAP.put(
        "PublishRequest", new DataTypeInfo(new NodeId(0, 824), new NodeId(0, 826)));
    DATA_TYPE_INFO_MAP.put(
        "PublishResponse", new DataTypeInfo(new NodeId(0, 827), new NodeId(0, 829)));
    DATA_TYPE_INFO_MAP.put(
        "RepublishRequest", new DataTypeInfo(new NodeId(0, 830), new NodeId(0, 832)));
    DATA_TYPE_INFO_MAP.put(
        "RepublishResponse", new DataTypeInfo(new NodeId(0, 833), new NodeId(0, 835)));
    DATA_TYPE_INFO_MAP.put(
        "TransferResult", new DataTypeInfo(new NodeId(0, 836), new NodeId(0, 838)));
    DATA_TYPE_INFO_MAP.put(
        "TransferSubscriptionsRequest", new DataTypeInfo(new NodeId(0, 839), new NodeId(0, 841)));
    DATA_TYPE_INFO_MAP.put(
        "TransferSubscriptionsResponse", new DataTypeInfo(new NodeId(0, 842), new NodeId(0, 844)));
    DATA_TYPE_INFO_MAP.put(
        "DeleteSubscriptionsRequest", new DataTypeInfo(new NodeId(0, 845), new NodeId(0, 847)));
    DATA_TYPE_INFO_MAP.put(
        "DeleteSubscriptionsResponse", new DataTypeInfo(new NodeId(0, 848), new NodeId(0, 850)));
  }

  static @Nullable DataTypeInfo getDataTypeInfo(String description) {
    return DATA_TYPE_INFO_MAP.get(description);
  }

  static class DataTypeInfo {
    final NodeId dataTypeId;

    final NodeId encodingId;

    DataTypeInfo(NodeId dataTypeId, NodeId encodingId) {
      this.dataTypeId = dataTypeId;
      this.encodingId = encodingId;
    }
  }
}

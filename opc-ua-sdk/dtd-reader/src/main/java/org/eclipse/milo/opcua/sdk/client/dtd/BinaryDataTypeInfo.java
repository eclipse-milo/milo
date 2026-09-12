/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.dtd;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.jspecify.annotations.Nullable;

abstract class BinaryDataTypeInfo {
  private static final Map<String, DataTypeInfo> DATA_TYPE_INFO_MAP;

  static {
    DATA_TYPE_INFO_MAP = new ConcurrentHashMap<>();
    DATA_TYPE_INFO_MAP.put(
        "Union", new DataTypeInfo(NodeId.parse("i=12756"), NodeId.parse("i=12766")));
    DATA_TYPE_INFO_MAP.put(
        "KeyValuePair", new DataTypeInfo(NodeId.parse("i=14533"), NodeId.parse("i=14846")));
    DATA_TYPE_INFO_MAP.put(
        "AdditionalParametersType",
        new DataTypeInfo(NodeId.parse("i=16313"), NodeId.parse("i=17537")));
    DATA_TYPE_INFO_MAP.put(
        "EphemeralKeyType", new DataTypeInfo(NodeId.parse("i=17548"), NodeId.parse("i=17549")));
    DATA_TYPE_INFO_MAP.put(
        "EndpointType", new DataTypeInfo(NodeId.parse("i=15528"), NodeId.parse("i=15671")));
    DATA_TYPE_INFO_MAP.put(
        "BitFieldDefinition", new DataTypeInfo(NodeId.parse("i=32421"), NodeId.parse("i=32422")));
    DATA_TYPE_INFO_MAP.put(
        "RationalNumber", new DataTypeInfo(NodeId.parse("i=18806"), NodeId.parse("i=18815")));
    DATA_TYPE_INFO_MAP.put(
        "Vector", new DataTypeInfo(NodeId.parse("i=18807"), NodeId.parse("i=18816")));
    DATA_TYPE_INFO_MAP.put(
        "3DVector", new DataTypeInfo(NodeId.parse("i=18808"), NodeId.parse("i=18817")));
    DATA_TYPE_INFO_MAP.put(
        "CartesianCoordinates", new DataTypeInfo(NodeId.parse("i=18809"), NodeId.parse("i=18818")));
    DATA_TYPE_INFO_MAP.put(
        "3DCartesianCoordinates",
        new DataTypeInfo(NodeId.parse("i=18810"), NodeId.parse("i=18819")));
    DATA_TYPE_INFO_MAP.put(
        "Orientation", new DataTypeInfo(NodeId.parse("i=18811"), NodeId.parse("i=18820")));
    DATA_TYPE_INFO_MAP.put(
        "3DOrientation", new DataTypeInfo(NodeId.parse("i=18812"), NodeId.parse("i=18821")));
    DATA_TYPE_INFO_MAP.put(
        "Frame", new DataTypeInfo(NodeId.parse("i=18813"), NodeId.parse("i=18822")));
    DATA_TYPE_INFO_MAP.put(
        "3DFrame", new DataTypeInfo(NodeId.parse("i=18814"), NodeId.parse("i=18823")));
    DATA_TYPE_INFO_MAP.put(
        "IdentityMappingRuleType",
        new DataTypeInfo(NodeId.parse("i=15634"), NodeId.parse("i=15736")));
    DATA_TYPE_INFO_MAP.put(
        "CurrencyUnitType", new DataTypeInfo(NodeId.parse("i=23498"), NodeId.parse("i=23507")));
    DATA_TYPE_INFO_MAP.put(
        "NumberRange", new DataTypeInfo(NodeId.parse("i=23903"), NodeId.parse("i=24250")));
    DATA_TYPE_INFO_MAP.put(
        "AnnotationDataType", new DataTypeInfo(NodeId.parse("i=32434"), NodeId.parse("i=32560")));
    DATA_TYPE_INFO_MAP.put(
        "LinearConversionDataType",
        new DataTypeInfo(NodeId.parse("i=32435"), NodeId.parse("i=32561")));
    DATA_TYPE_INFO_MAP.put(
        "QuantityDimension", new DataTypeInfo(NodeId.parse("i=32438"), NodeId.parse("i=32562")));
    DATA_TYPE_INFO_MAP.put(
        "TrustListDataType", new DataTypeInfo(NodeId.parse("i=12554"), NodeId.parse("i=12680")));
    DATA_TYPE_INFO_MAP.put(
        "BaseConfigurationDataType",
        new DataTypeInfo(NodeId.parse("i=15434"), NodeId.parse("i=16538")));
    DATA_TYPE_INFO_MAP.put(
        "BaseConfigurationRecordDataType",
        new DataTypeInfo(NodeId.parse("i=15435"), NodeId.parse("i=16539")));
    DATA_TYPE_INFO_MAP.put(
        "CertificateGroupDataType",
        new DataTypeInfo(NodeId.parse("i=15436"), NodeId.parse("i=16540")));
    DATA_TYPE_INFO_MAP.put(
        "ConfigurationUpdateTargetType",
        new DataTypeInfo(NodeId.parse("i=15538"), NodeId.parse("i=16541")));
    DATA_TYPE_INFO_MAP.put(
        "TransactionErrorType", new DataTypeInfo(NodeId.parse("i=32285"), NodeId.parse("i=32382")));
    DATA_TYPE_INFO_MAP.put(
        "ApplicationConfigurationDataType",
        new DataTypeInfo(NodeId.parse("i=23743"), NodeId.parse("i=23754")));
    DATA_TYPE_INFO_MAP.put(
        "ApplicationIdentityDataType",
        new DataTypeInfo(NodeId.parse("i=15556"), NodeId.parse("i=16543")));
    DATA_TYPE_INFO_MAP.put(
        "EndpointDataType", new DataTypeInfo(NodeId.parse("i=15557"), NodeId.parse("i=16544")));
    DATA_TYPE_INFO_MAP.put(
        "ServerEndpointDataType",
        new DataTypeInfo(NodeId.parse("i=15558"), NodeId.parse("i=16545")));
    DATA_TYPE_INFO_MAP.put(
        "SecuritySettingsDataType",
        new DataTypeInfo(NodeId.parse("i=15559"), NodeId.parse("i=16546")));
    DATA_TYPE_INFO_MAP.put(
        "UserTokenSettingsDataType",
        new DataTypeInfo(NodeId.parse("i=15560"), NodeId.parse("i=16547")));
    DATA_TYPE_INFO_MAP.put(
        "ServiceCertificateDataType",
        new DataTypeInfo(NodeId.parse("i=23724"), NodeId.parse("i=23725")));
    DATA_TYPE_INFO_MAP.put(
        "AuthorizationServiceConfigurationDataType",
        new DataTypeInfo(NodeId.parse("i=23744"), NodeId.parse("i=23755")));
    DATA_TYPE_INFO_MAP.put(
        "DataTypeSchemaHeader", new DataTypeInfo(NodeId.parse("i=15534"), NodeId.parse("i=15676")));
    DATA_TYPE_INFO_MAP.put(
        "DataTypeDescription", new DataTypeInfo(NodeId.parse("i=14525"), NodeId.parse("i=125")));
    DATA_TYPE_INFO_MAP.put(
        "StructureDescription", new DataTypeInfo(NodeId.parse("i=15487"), NodeId.parse("i=126")));
    DATA_TYPE_INFO_MAP.put(
        "EnumDescription", new DataTypeInfo(NodeId.parse("i=15488"), NodeId.parse("i=127")));
    DATA_TYPE_INFO_MAP.put(
        "SimpleTypeDescription",
        new DataTypeInfo(NodeId.parse("i=15005"), NodeId.parse("i=15421")));
    DATA_TYPE_INFO_MAP.put(
        "UABinaryFileDataType", new DataTypeInfo(NodeId.parse("i=15006"), NodeId.parse("i=15422")));
    DATA_TYPE_INFO_MAP.put(
        "PortableQualifiedName",
        new DataTypeInfo(NodeId.parse("i=24105"), NodeId.parse("i=24108")));
    DATA_TYPE_INFO_MAP.put(
        "PortableNodeId", new DataTypeInfo(NodeId.parse("i=24106"), NodeId.parse("i=24109")));
    DATA_TYPE_INFO_MAP.put(
        "UnsignedRationalNumber",
        new DataTypeInfo(NodeId.parse("i=24107"), NodeId.parse("i=24110")));
    DATA_TYPE_INFO_MAP.put(
        "DataSetMetaDataType", new DataTypeInfo(NodeId.parse("i=14523"), NodeId.parse("i=124")));
    DATA_TYPE_INFO_MAP.put(
        "FieldMetaData", new DataTypeInfo(NodeId.parse("i=14524"), NodeId.parse("i=14839")));
    DATA_TYPE_INFO_MAP.put(
        "ConfigurationVersionDataType",
        new DataTypeInfo(NodeId.parse("i=14593"), NodeId.parse("i=14847")));
    DATA_TYPE_INFO_MAP.put(
        "PublishedDataSetDataType",
        new DataTypeInfo(NodeId.parse("i=15578"), NodeId.parse("i=15677")));
    DATA_TYPE_INFO_MAP.put(
        "PublishedDataSetSourceDataType",
        new DataTypeInfo(NodeId.parse("i=15580"), NodeId.parse("i=15678")));
    DATA_TYPE_INFO_MAP.put(
        "PublishedVariableDataType",
        new DataTypeInfo(NodeId.parse("i=14273"), NodeId.parse("i=14323")));
    DATA_TYPE_INFO_MAP.put(
        "PublishedDataItemsDataType",
        new DataTypeInfo(NodeId.parse("i=15581"), NodeId.parse("i=15679")));
    DATA_TYPE_INFO_MAP.put(
        "PublishedEventsDataType",
        new DataTypeInfo(NodeId.parse("i=15582"), NodeId.parse("i=15681")));
    DATA_TYPE_INFO_MAP.put(
        "PublishedDataSetCustomSourceDataType",
        new DataTypeInfo(NodeId.parse("i=25269"), NodeId.parse("i=25529")));
    DATA_TYPE_INFO_MAP.put(
        "ActionTargetDataType", new DataTypeInfo(NodeId.parse("i=18593"), NodeId.parse("i=18598")));
    DATA_TYPE_INFO_MAP.put(
        "PublishedActionDataType",
        new DataTypeInfo(NodeId.parse("i=18594"), NodeId.parse("i=18599")));
    DATA_TYPE_INFO_MAP.put(
        "ActionMethodDataType", new DataTypeInfo(NodeId.parse("i=18597"), NodeId.parse("i=18600")));
    DATA_TYPE_INFO_MAP.put(
        "PublishedActionMethodDataType",
        new DataTypeInfo(NodeId.parse("i=18793"), NodeId.parse("i=18795")));
    DATA_TYPE_INFO_MAP.put(
        "DataSetWriterDataType",
        new DataTypeInfo(NodeId.parse("i=15597"), NodeId.parse("i=15682")));
    DATA_TYPE_INFO_MAP.put(
        "DataSetWriterTransportDataType",
        new DataTypeInfo(NodeId.parse("i=15598"), NodeId.parse("i=15683")));
    DATA_TYPE_INFO_MAP.put(
        "DataSetWriterMessageDataType",
        new DataTypeInfo(NodeId.parse("i=15605"), NodeId.parse("i=15688")));
    DATA_TYPE_INFO_MAP.put(
        "PubSubGroupDataType", new DataTypeInfo(NodeId.parse("i=15609"), NodeId.parse("i=15689")));
    DATA_TYPE_INFO_MAP.put(
        "WriterGroupDataType", new DataTypeInfo(NodeId.parse("i=15480"), NodeId.parse("i=21150")));
    DATA_TYPE_INFO_MAP.put(
        "WriterGroupTransportDataType",
        new DataTypeInfo(NodeId.parse("i=15611"), NodeId.parse("i=15691")));
    DATA_TYPE_INFO_MAP.put(
        "WriterGroupMessageDataType",
        new DataTypeInfo(NodeId.parse("i=15616"), NodeId.parse("i=15693")));
    DATA_TYPE_INFO_MAP.put(
        "PubSubConnectionDataType",
        new DataTypeInfo(NodeId.parse("i=15617"), NodeId.parse("i=15694")));
    DATA_TYPE_INFO_MAP.put(
        "ConnectionTransportDataType",
        new DataTypeInfo(NodeId.parse("i=15618"), NodeId.parse("i=15695")));
    DATA_TYPE_INFO_MAP.put(
        "NetworkAddressDataType",
        new DataTypeInfo(NodeId.parse("i=15502"), NodeId.parse("i=21151")));
    DATA_TYPE_INFO_MAP.put(
        "NetworkAddressUrlDataType",
        new DataTypeInfo(NodeId.parse("i=15510"), NodeId.parse("i=21152")));
    DATA_TYPE_INFO_MAP.put(
        "ReaderGroupDataType", new DataTypeInfo(NodeId.parse("i=15520"), NodeId.parse("i=21153")));
    DATA_TYPE_INFO_MAP.put(
        "ReaderGroupTransportDataType",
        new DataTypeInfo(NodeId.parse("i=15621"), NodeId.parse("i=15701")));
    DATA_TYPE_INFO_MAP.put(
        "ReaderGroupMessageDataType",
        new DataTypeInfo(NodeId.parse("i=15622"), NodeId.parse("i=15702")));
    DATA_TYPE_INFO_MAP.put(
        "DataSetReaderDataType",
        new DataTypeInfo(NodeId.parse("i=15623"), NodeId.parse("i=15703")));
    DATA_TYPE_INFO_MAP.put(
        "DataSetReaderTransportDataType",
        new DataTypeInfo(NodeId.parse("i=15628"), NodeId.parse("i=15705")));
    DATA_TYPE_INFO_MAP.put(
        "DataSetReaderMessageDataType",
        new DataTypeInfo(NodeId.parse("i=15629"), NodeId.parse("i=15706")));
    DATA_TYPE_INFO_MAP.put(
        "SubscribedDataSetDataType",
        new DataTypeInfo(NodeId.parse("i=15630"), NodeId.parse("i=15707")));
    DATA_TYPE_INFO_MAP.put(
        "TargetVariablesDataType",
        new DataTypeInfo(NodeId.parse("i=15631"), NodeId.parse("i=15712")));
    DATA_TYPE_INFO_MAP.put(
        "FieldTargetDataType", new DataTypeInfo(NodeId.parse("i=14744"), NodeId.parse("i=14848")));
    DATA_TYPE_INFO_MAP.put(
        "SubscribedDataSetMirrorDataType",
        new DataTypeInfo(NodeId.parse("i=15635"), NodeId.parse("i=15713")));
    DATA_TYPE_INFO_MAP.put(
        "PubSubConfigurationDataType",
        new DataTypeInfo(NodeId.parse("i=15530"), NodeId.parse("i=21154")));
    DATA_TYPE_INFO_MAP.put(
        "StandaloneSubscribedDataSetRefDataType",
        new DataTypeInfo(NodeId.parse("i=23599"), NodeId.parse("i=23851")));
    DATA_TYPE_INFO_MAP.put(
        "StandaloneSubscribedDataSetDataType",
        new DataTypeInfo(NodeId.parse("i=23600"), NodeId.parse("i=23852")));
    DATA_TYPE_INFO_MAP.put(
        "SecurityGroupDataType",
        new DataTypeInfo(NodeId.parse("i=23601"), NodeId.parse("i=23853")));
    DATA_TYPE_INFO_MAP.put(
        "PubSubKeyPushTargetDataType",
        new DataTypeInfo(NodeId.parse("i=25270"), NodeId.parse("i=25530")));
    DATA_TYPE_INFO_MAP.put(
        "PubSubConfiguration2DataType",
        new DataTypeInfo(NodeId.parse("i=23602"), NodeId.parse("i=23854")));
    DATA_TYPE_INFO_MAP.put(
        "UadpWriterGroupMessageDataType",
        new DataTypeInfo(NodeId.parse("i=15645"), NodeId.parse("i=15715")));
    DATA_TYPE_INFO_MAP.put(
        "UadpDataSetWriterMessageDataType",
        new DataTypeInfo(NodeId.parse("i=15652"), NodeId.parse("i=15717")));
    DATA_TYPE_INFO_MAP.put(
        "UadpDataSetReaderMessageDataType",
        new DataTypeInfo(NodeId.parse("i=15653"), NodeId.parse("i=15718")));
    DATA_TYPE_INFO_MAP.put(
        "JsonWriterGroupMessageDataType",
        new DataTypeInfo(NodeId.parse("i=15657"), NodeId.parse("i=15719")));
    DATA_TYPE_INFO_MAP.put(
        "JsonDataSetWriterMessageDataType",
        new DataTypeInfo(NodeId.parse("i=15664"), NodeId.parse("i=15724")));
    DATA_TYPE_INFO_MAP.put(
        "JsonDataSetReaderMessageDataType",
        new DataTypeInfo(NodeId.parse("i=15665"), NodeId.parse("i=15725")));
    DATA_TYPE_INFO_MAP.put(
        "QosDataType", new DataTypeInfo(NodeId.parse("i=23603"), NodeId.parse("i=23855")));
    DATA_TYPE_INFO_MAP.put(
        "TransmitQosDataType", new DataTypeInfo(NodeId.parse("i=23604"), NodeId.parse("i=23856")));
    DATA_TYPE_INFO_MAP.put(
        "TransmitQosPriorityDataType",
        new DataTypeInfo(NodeId.parse("i=23605"), NodeId.parse("i=23857")));
    DATA_TYPE_INFO_MAP.put(
        "ReceiveQosDataType", new DataTypeInfo(NodeId.parse("i=23608"), NodeId.parse("i=23860")));
    DATA_TYPE_INFO_MAP.put(
        "ReceiveQosPriorityDataType",
        new DataTypeInfo(NodeId.parse("i=23609"), NodeId.parse("i=23861")));
    DATA_TYPE_INFO_MAP.put(
        "DatagramConnectionTransportDataType",
        new DataTypeInfo(NodeId.parse("i=17467"), NodeId.parse("i=17468")));
    DATA_TYPE_INFO_MAP.put(
        "DatagramConnectionTransport2DataType",
        new DataTypeInfo(NodeId.parse("i=23612"), NodeId.parse("i=23864")));
    DATA_TYPE_INFO_MAP.put(
        "DatagramWriterGroupTransportDataType",
        new DataTypeInfo(NodeId.parse("i=15532"), NodeId.parse("i=21155")));
    DATA_TYPE_INFO_MAP.put(
        "DatagramWriterGroupTransport2DataType",
        new DataTypeInfo(NodeId.parse("i=23613"), NodeId.parse("i=23865")));
    DATA_TYPE_INFO_MAP.put(
        "DatagramDataSetReaderTransportDataType",
        new DataTypeInfo(NodeId.parse("i=23614"), NodeId.parse("i=23866")));
    DATA_TYPE_INFO_MAP.put(
        "DtlsPubSubConnectionDataType",
        new DataTypeInfo(NodeId.parse("i=18794"), NodeId.parse("i=18930")));
    DATA_TYPE_INFO_MAP.put(
        "BrokerConnectionTransportDataType",
        new DataTypeInfo(NodeId.parse("i=15007"), NodeId.parse("i=15479")));
    DATA_TYPE_INFO_MAP.put(
        "BrokerWriterGroupTransportDataType",
        new DataTypeInfo(NodeId.parse("i=15667"), NodeId.parse("i=15727")));
    DATA_TYPE_INFO_MAP.put(
        "BrokerDataSetWriterTransportDataType",
        new DataTypeInfo(NodeId.parse("i=15669"), NodeId.parse("i=15729")));
    DATA_TYPE_INFO_MAP.put(
        "BrokerDataSetReaderTransportDataType",
        new DataTypeInfo(NodeId.parse("i=15670"), NodeId.parse("i=15733")));
    DATA_TYPE_INFO_MAP.put(
        "PubSubConfigurationRefDataType",
        new DataTypeInfo(NodeId.parse("i=25519"), NodeId.parse("i=25531")));
    DATA_TYPE_INFO_MAP.put(
        "PubSubConfigurationValueDataType",
        new DataTypeInfo(NodeId.parse("i=25520"), NodeId.parse("i=25532")));
    DATA_TYPE_INFO_MAP.put(
        "AliasNameDataType", new DataTypeInfo(NodeId.parse("i=23468"), NodeId.parse("i=23499")));
    DATA_TYPE_INFO_MAP.put(
        "AliasNameVerboseDataType",
        new DataTypeInfo(NodeId.parse("i=24051"), NodeId.parse("i=24262")));
    DATA_TYPE_INFO_MAP.put(
        "AliasCategoryUpdateDataType",
        new DataTypeInfo(NodeId.parse("i=24052"), NodeId.parse("i=24338")));
    DATA_TYPE_INFO_MAP.put(
        "AliasUpdateDataType", new DataTypeInfo(NodeId.parse("i=24053"), NodeId.parse("i=24339")));
    DATA_TYPE_INFO_MAP.put(
        "UserManagementDataType",
        new DataTypeInfo(NodeId.parse("i=24281"), NodeId.parse("i=24292")));
    DATA_TYPE_INFO_MAP.put(
        "PriorityMappingEntryType",
        new DataTypeInfo(NodeId.parse("i=25220"), NodeId.parse("i=25239")));
    DATA_TYPE_INFO_MAP.put(
        "LldpManagementAddressTxPortType",
        new DataTypeInfo(NodeId.parse("i=18953"), NodeId.parse("i=19079")));
    DATA_TYPE_INFO_MAP.put(
        "LldpManagementAddressType",
        new DataTypeInfo(NodeId.parse("i=18954"), NodeId.parse("i=19080")));
    DATA_TYPE_INFO_MAP.put(
        "LldpTlvType", new DataTypeInfo(NodeId.parse("i=18955"), NodeId.parse("i=19081")));
    DATA_TYPE_INFO_MAP.put(
        "ReferenceDescriptionDataType",
        new DataTypeInfo(NodeId.parse("i=32659"), NodeId.parse("i=32661")));
    DATA_TYPE_INFO_MAP.put(
        "ReferenceListEntryDataType",
        new DataTypeInfo(NodeId.parse("i=32660"), NodeId.parse("i=32662")));
    DATA_TYPE_INFO_MAP.put(
        "LogRecord", new DataTypeInfo(NodeId.parse("i=19361"), NodeId.parse("i=19379")));
    DATA_TYPE_INFO_MAP.put(
        "LogRecordsDataType", new DataTypeInfo(NodeId.parse("i=19745"), NodeId.parse("i=19753")));
    DATA_TYPE_INFO_MAP.put(
        "SpanContextDataType", new DataTypeInfo(NodeId.parse("i=19746"), NodeId.parse("i=19754")));
    DATA_TYPE_INFO_MAP.put(
        "TraceContextDataType", new DataTypeInfo(NodeId.parse("i=19747"), NodeId.parse("i=19755")));
    DATA_TYPE_INFO_MAP.put(
        "NameValuePair", new DataTypeInfo(NodeId.parse("i=19748"), NodeId.parse("i=19756")));
    DATA_TYPE_INFO_MAP.put(
        "RolePermissionType", new DataTypeInfo(NodeId.parse("i=96"), NodeId.parse("i=128")));
    DATA_TYPE_INFO_MAP.put(
        "DataTypeDefinition", new DataTypeInfo(NodeId.parse("i=97"), NodeId.parse("i=121")));
    DATA_TYPE_INFO_MAP.put(
        "StructureField", new DataTypeInfo(NodeId.parse("i=101"), NodeId.parse("i=14844")));
    DATA_TYPE_INFO_MAP.put(
        "StructureDefinition", new DataTypeInfo(NodeId.parse("i=99"), NodeId.parse("i=122")));
    DATA_TYPE_INFO_MAP.put(
        "EnumDefinition", new DataTypeInfo(NodeId.parse("i=100"), NodeId.parse("i=123")));
    DATA_TYPE_INFO_MAP.put(
        "Argument", new DataTypeInfo(NodeId.parse("i=296"), NodeId.parse("i=298")));
    DATA_TYPE_INFO_MAP.put(
        "EnumValueType", new DataTypeInfo(NodeId.parse("i=7594"), NodeId.parse("i=8251")));
    DATA_TYPE_INFO_MAP.put(
        "EnumField", new DataTypeInfo(NodeId.parse("i=102"), NodeId.parse("i=14845")));
    DATA_TYPE_INFO_MAP.put(
        "OptionSet", new DataTypeInfo(NodeId.parse("i=12755"), NodeId.parse("i=12765")));
    DATA_TYPE_INFO_MAP.put(
        "TimeZoneDataType", new DataTypeInfo(NodeId.parse("i=8912"), NodeId.parse("i=8917")));
    DATA_TYPE_INFO_MAP.put(
        "ApplicationDescription", new DataTypeInfo(NodeId.parse("i=308"), NodeId.parse("i=310")));
    DATA_TYPE_INFO_MAP.put(
        "ServerOnNetwork", new DataTypeInfo(NodeId.parse("i=12189"), NodeId.parse("i=12207")));
    DATA_TYPE_INFO_MAP.put(
        "UserTokenPolicy", new DataTypeInfo(NodeId.parse("i=304"), NodeId.parse("i=306")));
    DATA_TYPE_INFO_MAP.put(
        "EndpointDescription", new DataTypeInfo(NodeId.parse("i=312"), NodeId.parse("i=314")));
    DATA_TYPE_INFO_MAP.put(
        "RegisteredServer", new DataTypeInfo(NodeId.parse("i=432"), NodeId.parse("i=434")));
    DATA_TYPE_INFO_MAP.put(
        "DiscoveryConfiguration",
        new DataTypeInfo(NodeId.parse("i=12890"), NodeId.parse("i=12900")));
    DATA_TYPE_INFO_MAP.put(
        "MdnsDiscoveryConfiguration",
        new DataTypeInfo(NodeId.parse("i=12891"), NodeId.parse("i=12901")));
    DATA_TYPE_INFO_MAP.put(
        "SignedSoftwareCertificate",
        new DataTypeInfo(NodeId.parse("i=344"), NodeId.parse("i=346")));
    DATA_TYPE_INFO_MAP.put(
        "SignatureData", new DataTypeInfo(NodeId.parse("i=456"), NodeId.parse("i=458")));
    DATA_TYPE_INFO_MAP.put(
        "UserIdentityToken", new DataTypeInfo(NodeId.parse("i=316"), NodeId.parse("i=318")));
    DATA_TYPE_INFO_MAP.put(
        "AnonymousIdentityToken", new DataTypeInfo(NodeId.parse("i=319"), NodeId.parse("i=321")));
    DATA_TYPE_INFO_MAP.put(
        "UserNameIdentityToken", new DataTypeInfo(NodeId.parse("i=322"), NodeId.parse("i=324")));
    DATA_TYPE_INFO_MAP.put(
        "X509IdentityToken", new DataTypeInfo(NodeId.parse("i=325"), NodeId.parse("i=327")));
    DATA_TYPE_INFO_MAP.put(
        "IssuedIdentityToken", new DataTypeInfo(NodeId.parse("i=938"), NodeId.parse("i=940")));
    DATA_TYPE_INFO_MAP.put(
        "AddNodesItem", new DataTypeInfo(NodeId.parse("i=376"), NodeId.parse("i=378")));
    DATA_TYPE_INFO_MAP.put(
        "AddReferencesItem", new DataTypeInfo(NodeId.parse("i=379"), NodeId.parse("i=381")));
    DATA_TYPE_INFO_MAP.put(
        "DeleteNodesItem", new DataTypeInfo(NodeId.parse("i=382"), NodeId.parse("i=384")));
    DATA_TYPE_INFO_MAP.put(
        "DeleteReferencesItem", new DataTypeInfo(NodeId.parse("i=385"), NodeId.parse("i=387")));
    DATA_TYPE_INFO_MAP.put(
        "RelativePathElement", new DataTypeInfo(NodeId.parse("i=537"), NodeId.parse("i=539")));
    DATA_TYPE_INFO_MAP.put(
        "RelativePath", new DataTypeInfo(NodeId.parse("i=540"), NodeId.parse("i=542")));
    DATA_TYPE_INFO_MAP.put(
        "EndpointConfiguration", new DataTypeInfo(NodeId.parse("i=331"), NodeId.parse("i=333")));
    DATA_TYPE_INFO_MAP.put(
        "ContentFilterElement", new DataTypeInfo(NodeId.parse("i=583"), NodeId.parse("i=585")));
    DATA_TYPE_INFO_MAP.put(
        "ContentFilter", new DataTypeInfo(NodeId.parse("i=586"), NodeId.parse("i=588")));
    DATA_TYPE_INFO_MAP.put(
        "FilterOperand", new DataTypeInfo(NodeId.parse("i=589"), NodeId.parse("i=591")));
    DATA_TYPE_INFO_MAP.put(
        "ElementOperand", new DataTypeInfo(NodeId.parse("i=592"), NodeId.parse("i=594")));
    DATA_TYPE_INFO_MAP.put(
        "LiteralOperand", new DataTypeInfo(NodeId.parse("i=595"), NodeId.parse("i=597")));
    DATA_TYPE_INFO_MAP.put(
        "AttributeOperand", new DataTypeInfo(NodeId.parse("i=598"), NodeId.parse("i=600")));
    DATA_TYPE_INFO_MAP.put(
        "SimpleAttributeOperand", new DataTypeInfo(NodeId.parse("i=601"), NodeId.parse("i=603")));
    DATA_TYPE_INFO_MAP.put(
        "ModificationInfo", new DataTypeInfo(NodeId.parse("i=11216"), NodeId.parse("i=11226")));
    DATA_TYPE_INFO_MAP.put(
        "HistoryEvent", new DataTypeInfo(NodeId.parse("i=659"), NodeId.parse("i=661")));
    DATA_TYPE_INFO_MAP.put(
        "HistoryModifiedEvent", new DataTypeInfo(NodeId.parse("i=32824"), NodeId.parse("i=32825")));
    DATA_TYPE_INFO_MAP.put(
        "MonitoringFilter", new DataTypeInfo(NodeId.parse("i=719"), NodeId.parse("i=721")));
    DATA_TYPE_INFO_MAP.put(
        "EventFilter", new DataTypeInfo(NodeId.parse("i=725"), NodeId.parse("i=727")));
    DATA_TYPE_INFO_MAP.put(
        "AggregateConfiguration", new DataTypeInfo(NodeId.parse("i=948"), NodeId.parse("i=950")));
    DATA_TYPE_INFO_MAP.put(
        "HistoryEventFieldList", new DataTypeInfo(NodeId.parse("i=920"), NodeId.parse("i=922")));
    DATA_TYPE_INFO_MAP.put(
        "BuildInfo", new DataTypeInfo(NodeId.parse("i=338"), NodeId.parse("i=340")));
    DATA_TYPE_INFO_MAP.put(
        "RedundantServerDataType", new DataTypeInfo(NodeId.parse("i=853"), NodeId.parse("i=855")));
    DATA_TYPE_INFO_MAP.put(
        "EndpointUrlListDataType",
        new DataTypeInfo(NodeId.parse("i=11943"), NodeId.parse("i=11957")));
    DATA_TYPE_INFO_MAP.put(
        "NetworkGroupDataType", new DataTypeInfo(NodeId.parse("i=11944"), NodeId.parse("i=11958")));
    DATA_TYPE_INFO_MAP.put(
        "SamplingIntervalDiagnosticsDataType",
        new DataTypeInfo(NodeId.parse("i=856"), NodeId.parse("i=858")));
    DATA_TYPE_INFO_MAP.put(
        "ServerDiagnosticsSummaryDataType",
        new DataTypeInfo(NodeId.parse("i=859"), NodeId.parse("i=861")));
    DATA_TYPE_INFO_MAP.put(
        "ServerStatusDataType", new DataTypeInfo(NodeId.parse("i=862"), NodeId.parse("i=864")));
    DATA_TYPE_INFO_MAP.put(
        "SessionDiagnosticsDataType",
        new DataTypeInfo(NodeId.parse("i=865"), NodeId.parse("i=867")));
    DATA_TYPE_INFO_MAP.put(
        "SessionSecurityDiagnosticsDataType",
        new DataTypeInfo(NodeId.parse("i=868"), NodeId.parse("i=870")));
    DATA_TYPE_INFO_MAP.put(
        "ServiceCounterDataType", new DataTypeInfo(NodeId.parse("i=871"), NodeId.parse("i=873")));
    DATA_TYPE_INFO_MAP.put(
        "StatusResult", new DataTypeInfo(NodeId.parse("i=299"), NodeId.parse("i=301")));
    DATA_TYPE_INFO_MAP.put(
        "SubscriptionDiagnosticsDataType",
        new DataTypeInfo(NodeId.parse("i=874"), NodeId.parse("i=876")));
    DATA_TYPE_INFO_MAP.put(
        "ModelChangeStructureDataType",
        new DataTypeInfo(NodeId.parse("i=877"), NodeId.parse("i=879")));
    DATA_TYPE_INFO_MAP.put(
        "SemanticChangeStructureDataType",
        new DataTypeInfo(NodeId.parse("i=897"), NodeId.parse("i=899")));
    DATA_TYPE_INFO_MAP.put("Range", new DataTypeInfo(NodeId.parse("i=884"), NodeId.parse("i=886")));
    DATA_TYPE_INFO_MAP.put(
        "EUInformation", new DataTypeInfo(NodeId.parse("i=887"), NodeId.parse("i=889")));
    DATA_TYPE_INFO_MAP.put(
        "ComplexNumberType", new DataTypeInfo(NodeId.parse("i=12171"), NodeId.parse("i=12181")));
    DATA_TYPE_INFO_MAP.put(
        "DoubleComplexNumberType",
        new DataTypeInfo(NodeId.parse("i=12172"), NodeId.parse("i=12182")));
    DATA_TYPE_INFO_MAP.put(
        "AxisInformation", new DataTypeInfo(NodeId.parse("i=12079"), NodeId.parse("i=12089")));
    DATA_TYPE_INFO_MAP.put(
        "XVType", new DataTypeInfo(NodeId.parse("i=12080"), NodeId.parse("i=12090")));
    DATA_TYPE_INFO_MAP.put(
        "ProgramDiagnosticDataType",
        new DataTypeInfo(NodeId.parse("i=894"), NodeId.parse("i=896")));
    DATA_TYPE_INFO_MAP.put(
        "ProgramDiagnostic2DataType",
        new DataTypeInfo(NodeId.parse("i=24033"), NodeId.parse("i=24034")));
    DATA_TYPE_INFO_MAP.put(
        "Annotation", new DataTypeInfo(NodeId.parse("i=891"), NodeId.parse("i=893")));
    DATA_TYPE_INFO_MAP.put(
        "DecimalDataType", new DataTypeInfo(NodeId.parse("i=17861"), NodeId.parse("i=17863")));
    DATA_TYPE_INFO_MAP.put("Node", new DataTypeInfo(NodeId.parse("i=258"), NodeId.parse("i=260")));
    DATA_TYPE_INFO_MAP.put(
        "InstanceNode", new DataTypeInfo(NodeId.parse("i=11879"), NodeId.parse("i=11889")));
    DATA_TYPE_INFO_MAP.put(
        "TypeNode", new DataTypeInfo(NodeId.parse("i=11880"), NodeId.parse("i=11890")));
    DATA_TYPE_INFO_MAP.put(
        "ObjectNode", new DataTypeInfo(NodeId.parse("i=261"), NodeId.parse("i=263")));
    DATA_TYPE_INFO_MAP.put(
        "ObjectTypeNode", new DataTypeInfo(NodeId.parse("i=264"), NodeId.parse("i=266")));
    DATA_TYPE_INFO_MAP.put(
        "VariableNode", new DataTypeInfo(NodeId.parse("i=267"), NodeId.parse("i=269")));
    DATA_TYPE_INFO_MAP.put(
        "VariableTypeNode", new DataTypeInfo(NodeId.parse("i=270"), NodeId.parse("i=272")));
    DATA_TYPE_INFO_MAP.put(
        "ReferenceTypeNode", new DataTypeInfo(NodeId.parse("i=273"), NodeId.parse("i=275")));
    DATA_TYPE_INFO_MAP.put(
        "MethodNode", new DataTypeInfo(NodeId.parse("i=276"), NodeId.parse("i=278")));
    DATA_TYPE_INFO_MAP.put(
        "ViewNode", new DataTypeInfo(NodeId.parse("i=279"), NodeId.parse("i=281")));
    DATA_TYPE_INFO_MAP.put(
        "DataTypeNode", new DataTypeInfo(NodeId.parse("i=282"), NodeId.parse("i=284")));
    DATA_TYPE_INFO_MAP.put(
        "ReferenceNode", new DataTypeInfo(NodeId.parse("i=285"), NodeId.parse("i=287")));
    DATA_TYPE_INFO_MAP.put(
        "RequestHeader", new DataTypeInfo(NodeId.parse("i=389"), NodeId.parse("i=391")));
    DATA_TYPE_INFO_MAP.put(
        "ResponseHeader", new DataTypeInfo(NodeId.parse("i=392"), NodeId.parse("i=394")));
    DATA_TYPE_INFO_MAP.put(
        "ServiceFault", new DataTypeInfo(NodeId.parse("i=395"), NodeId.parse("i=397")));
    DATA_TYPE_INFO_MAP.put(
        "SessionlessInvokeRequestType",
        new DataTypeInfo(NodeId.parse("i=15901"), NodeId.parse("i=15903")));
    DATA_TYPE_INFO_MAP.put(
        "SessionlessInvokeResponseType",
        new DataTypeInfo(NodeId.parse("i=20999"), NodeId.parse("i=21001")));
    DATA_TYPE_INFO_MAP.put(
        "FindServersRequest", new DataTypeInfo(NodeId.parse("i=420"), NodeId.parse("i=422")));
    DATA_TYPE_INFO_MAP.put(
        "FindServersResponse", new DataTypeInfo(NodeId.parse("i=423"), NodeId.parse("i=425")));
    DATA_TYPE_INFO_MAP.put(
        "FindServersOnNetworkRequest",
        new DataTypeInfo(NodeId.parse("i=12190"), NodeId.parse("i=12208")));
    DATA_TYPE_INFO_MAP.put(
        "FindServersOnNetworkResponse",
        new DataTypeInfo(NodeId.parse("i=12191"), NodeId.parse("i=12209")));
    DATA_TYPE_INFO_MAP.put(
        "GetEndpointsRequest", new DataTypeInfo(NodeId.parse("i=426"), NodeId.parse("i=428")));
    DATA_TYPE_INFO_MAP.put(
        "GetEndpointsResponse", new DataTypeInfo(NodeId.parse("i=429"), NodeId.parse("i=431")));
    DATA_TYPE_INFO_MAP.put(
        "RegisterServerRequest", new DataTypeInfo(NodeId.parse("i=435"), NodeId.parse("i=437")));
    DATA_TYPE_INFO_MAP.put(
        "RegisterServerResponse", new DataTypeInfo(NodeId.parse("i=438"), NodeId.parse("i=440")));
    DATA_TYPE_INFO_MAP.put(
        "RegisterServer2Request",
        new DataTypeInfo(NodeId.parse("i=12193"), NodeId.parse("i=12211")));
    DATA_TYPE_INFO_MAP.put(
        "RegisterServer2Response",
        new DataTypeInfo(NodeId.parse("i=12194"), NodeId.parse("i=12212")));
    DATA_TYPE_INFO_MAP.put(
        "ChannelSecurityToken", new DataTypeInfo(NodeId.parse("i=441"), NodeId.parse("i=443")));
    DATA_TYPE_INFO_MAP.put(
        "OpenSecureChannelRequest", new DataTypeInfo(NodeId.parse("i=444"), NodeId.parse("i=446")));
    DATA_TYPE_INFO_MAP.put(
        "OpenSecureChannelResponse",
        new DataTypeInfo(NodeId.parse("i=447"), NodeId.parse("i=449")));
    DATA_TYPE_INFO_MAP.put(
        "CloseSecureChannelRequest",
        new DataTypeInfo(NodeId.parse("i=450"), NodeId.parse("i=452")));
    DATA_TYPE_INFO_MAP.put(
        "CloseSecureChannelResponse",
        new DataTypeInfo(NodeId.parse("i=453"), NodeId.parse("i=455")));
    DATA_TYPE_INFO_MAP.put(
        "CreateSessionRequest", new DataTypeInfo(NodeId.parse("i=459"), NodeId.parse("i=461")));
    DATA_TYPE_INFO_MAP.put(
        "CreateSessionResponse", new DataTypeInfo(NodeId.parse("i=462"), NodeId.parse("i=464")));
    DATA_TYPE_INFO_MAP.put(
        "ActivateSessionRequest", new DataTypeInfo(NodeId.parse("i=465"), NodeId.parse("i=467")));
    DATA_TYPE_INFO_MAP.put(
        "ActivateSessionResponse", new DataTypeInfo(NodeId.parse("i=468"), NodeId.parse("i=470")));
    DATA_TYPE_INFO_MAP.put(
        "CloseSessionRequest", new DataTypeInfo(NodeId.parse("i=471"), NodeId.parse("i=473")));
    DATA_TYPE_INFO_MAP.put(
        "CloseSessionResponse", new DataTypeInfo(NodeId.parse("i=474"), NodeId.parse("i=476")));
    DATA_TYPE_INFO_MAP.put(
        "CancelRequest", new DataTypeInfo(NodeId.parse("i=477"), NodeId.parse("i=479")));
    DATA_TYPE_INFO_MAP.put(
        "CancelResponse", new DataTypeInfo(NodeId.parse("i=480"), NodeId.parse("i=482")));
    DATA_TYPE_INFO_MAP.put(
        "NodeAttributes", new DataTypeInfo(NodeId.parse("i=349"), NodeId.parse("i=351")));
    DATA_TYPE_INFO_MAP.put(
        "ObjectAttributes", new DataTypeInfo(NodeId.parse("i=352"), NodeId.parse("i=354")));
    DATA_TYPE_INFO_MAP.put(
        "VariableAttributes", new DataTypeInfo(NodeId.parse("i=355"), NodeId.parse("i=357")));
    DATA_TYPE_INFO_MAP.put(
        "MethodAttributes", new DataTypeInfo(NodeId.parse("i=358"), NodeId.parse("i=360")));
    DATA_TYPE_INFO_MAP.put(
        "ObjectTypeAttributes", new DataTypeInfo(NodeId.parse("i=361"), NodeId.parse("i=363")));
    DATA_TYPE_INFO_MAP.put(
        "VariableTypeAttributes", new DataTypeInfo(NodeId.parse("i=364"), NodeId.parse("i=366")));
    DATA_TYPE_INFO_MAP.put(
        "ReferenceTypeAttributes", new DataTypeInfo(NodeId.parse("i=367"), NodeId.parse("i=369")));
    DATA_TYPE_INFO_MAP.put(
        "DataTypeAttributes", new DataTypeInfo(NodeId.parse("i=370"), NodeId.parse("i=372")));
    DATA_TYPE_INFO_MAP.put(
        "ViewAttributes", new DataTypeInfo(NodeId.parse("i=373"), NodeId.parse("i=375")));
    DATA_TYPE_INFO_MAP.put(
        "GenericAttributeValue",
        new DataTypeInfo(NodeId.parse("i=17606"), NodeId.parse("i=17610")));
    DATA_TYPE_INFO_MAP.put(
        "GenericAttributes", new DataTypeInfo(NodeId.parse("i=17607"), NodeId.parse("i=17611")));
    DATA_TYPE_INFO_MAP.put(
        "AddNodesResult", new DataTypeInfo(NodeId.parse("i=483"), NodeId.parse("i=485")));
    DATA_TYPE_INFO_MAP.put(
        "AddNodesRequest", new DataTypeInfo(NodeId.parse("i=486"), NodeId.parse("i=488")));
    DATA_TYPE_INFO_MAP.put(
        "AddNodesResponse", new DataTypeInfo(NodeId.parse("i=489"), NodeId.parse("i=491")));
    DATA_TYPE_INFO_MAP.put(
        "AddReferencesRequest", new DataTypeInfo(NodeId.parse("i=492"), NodeId.parse("i=494")));
    DATA_TYPE_INFO_MAP.put(
        "AddReferencesResponse", new DataTypeInfo(NodeId.parse("i=495"), NodeId.parse("i=497")));
    DATA_TYPE_INFO_MAP.put(
        "DeleteNodesRequest", new DataTypeInfo(NodeId.parse("i=498"), NodeId.parse("i=500")));
    DATA_TYPE_INFO_MAP.put(
        "DeleteNodesResponse", new DataTypeInfo(NodeId.parse("i=501"), NodeId.parse("i=503")));
    DATA_TYPE_INFO_MAP.put(
        "DeleteReferencesRequest", new DataTypeInfo(NodeId.parse("i=504"), NodeId.parse("i=506")));
    DATA_TYPE_INFO_MAP.put(
        "DeleteReferencesResponse", new DataTypeInfo(NodeId.parse("i=507"), NodeId.parse("i=509")));
    DATA_TYPE_INFO_MAP.put(
        "ViewDescription", new DataTypeInfo(NodeId.parse("i=511"), NodeId.parse("i=513")));
    DATA_TYPE_INFO_MAP.put(
        "BrowseDescription", new DataTypeInfo(NodeId.parse("i=514"), NodeId.parse("i=516")));
    DATA_TYPE_INFO_MAP.put(
        "ReferenceDescription", new DataTypeInfo(NodeId.parse("i=518"), NodeId.parse("i=520")));
    DATA_TYPE_INFO_MAP.put(
        "BrowseResult", new DataTypeInfo(NodeId.parse("i=522"), NodeId.parse("i=524")));
    DATA_TYPE_INFO_MAP.put(
        "BrowseRequest", new DataTypeInfo(NodeId.parse("i=525"), NodeId.parse("i=527")));
    DATA_TYPE_INFO_MAP.put(
        "BrowseResponse", new DataTypeInfo(NodeId.parse("i=528"), NodeId.parse("i=530")));
    DATA_TYPE_INFO_MAP.put(
        "BrowseNextRequest", new DataTypeInfo(NodeId.parse("i=531"), NodeId.parse("i=533")));
    DATA_TYPE_INFO_MAP.put(
        "BrowseNextResponse", new DataTypeInfo(NodeId.parse("i=534"), NodeId.parse("i=536")));
    DATA_TYPE_INFO_MAP.put(
        "BrowsePath", new DataTypeInfo(NodeId.parse("i=543"), NodeId.parse("i=545")));
    DATA_TYPE_INFO_MAP.put(
        "BrowsePathTarget", new DataTypeInfo(NodeId.parse("i=546"), NodeId.parse("i=548")));
    DATA_TYPE_INFO_MAP.put(
        "BrowsePathResult", new DataTypeInfo(NodeId.parse("i=549"), NodeId.parse("i=551")));
    DATA_TYPE_INFO_MAP.put(
        "TranslateBrowsePathsToNodeIdsRequest",
        new DataTypeInfo(NodeId.parse("i=552"), NodeId.parse("i=554")));
    DATA_TYPE_INFO_MAP.put(
        "TranslateBrowsePathsToNodeIdsResponse",
        new DataTypeInfo(NodeId.parse("i=555"), NodeId.parse("i=557")));
    DATA_TYPE_INFO_MAP.put(
        "RegisterNodesRequest", new DataTypeInfo(NodeId.parse("i=558"), NodeId.parse("i=560")));
    DATA_TYPE_INFO_MAP.put(
        "RegisterNodesResponse", new DataTypeInfo(NodeId.parse("i=561"), NodeId.parse("i=563")));
    DATA_TYPE_INFO_MAP.put(
        "UnregisterNodesRequest", new DataTypeInfo(NodeId.parse("i=564"), NodeId.parse("i=566")));
    DATA_TYPE_INFO_MAP.put(
        "UnregisterNodesResponse", new DataTypeInfo(NodeId.parse("i=567"), NodeId.parse("i=569")));
    DATA_TYPE_INFO_MAP.put(
        "QueryDataDescription", new DataTypeInfo(NodeId.parse("i=570"), NodeId.parse("i=572")));
    DATA_TYPE_INFO_MAP.put(
        "NodeTypeDescription", new DataTypeInfo(NodeId.parse("i=573"), NodeId.parse("i=575")));
    DATA_TYPE_INFO_MAP.put(
        "QueryDataSet", new DataTypeInfo(NodeId.parse("i=577"), NodeId.parse("i=579")));
    DATA_TYPE_INFO_MAP.put(
        "NodeReference", new DataTypeInfo(NodeId.parse("i=580"), NodeId.parse("i=582")));
    DATA_TYPE_INFO_MAP.put(
        "ContentFilterElementResult",
        new DataTypeInfo(NodeId.parse("i=604"), NodeId.parse("i=606")));
    DATA_TYPE_INFO_MAP.put(
        "ContentFilterResult", new DataTypeInfo(NodeId.parse("i=607"), NodeId.parse("i=609")));
    DATA_TYPE_INFO_MAP.put(
        "ParsingResult", new DataTypeInfo(NodeId.parse("i=610"), NodeId.parse("i=612")));
    DATA_TYPE_INFO_MAP.put(
        "QueryFirstRequest", new DataTypeInfo(NodeId.parse("i=613"), NodeId.parse("i=615")));
    DATA_TYPE_INFO_MAP.put(
        "QueryFirstResponse", new DataTypeInfo(NodeId.parse("i=616"), NodeId.parse("i=618")));
    DATA_TYPE_INFO_MAP.put(
        "QueryNextRequest", new DataTypeInfo(NodeId.parse("i=619"), NodeId.parse("i=621")));
    DATA_TYPE_INFO_MAP.put(
        "QueryNextResponse", new DataTypeInfo(NodeId.parse("i=622"), NodeId.parse("i=624")));
    DATA_TYPE_INFO_MAP.put(
        "ReadValueId", new DataTypeInfo(NodeId.parse("i=626"), NodeId.parse("i=628")));
    DATA_TYPE_INFO_MAP.put(
        "ReadRequest", new DataTypeInfo(NodeId.parse("i=629"), NodeId.parse("i=631")));
    DATA_TYPE_INFO_MAP.put(
        "ReadResponse", new DataTypeInfo(NodeId.parse("i=632"), NodeId.parse("i=634")));
    DATA_TYPE_INFO_MAP.put(
        "HistoryReadValueId", new DataTypeInfo(NodeId.parse("i=635"), NodeId.parse("i=637")));
    DATA_TYPE_INFO_MAP.put(
        "HistoryReadResult", new DataTypeInfo(NodeId.parse("i=638"), NodeId.parse("i=640")));
    DATA_TYPE_INFO_MAP.put(
        "HistoryReadDetails", new DataTypeInfo(NodeId.parse("i=641"), NodeId.parse("i=643")));
    DATA_TYPE_INFO_MAP.put(
        "ReadEventDetails", new DataTypeInfo(NodeId.parse("i=644"), NodeId.parse("i=646")));
    DATA_TYPE_INFO_MAP.put(
        "ReadEventDetails2", new DataTypeInfo(NodeId.parse("i=32799"), NodeId.parse("i=32800")));
    DATA_TYPE_INFO_MAP.put(
        "SortRuleElement", new DataTypeInfo(NodeId.parse("i=18648"), NodeId.parse("i=18650")));
    DATA_TYPE_INFO_MAP.put(
        "ReadEventDetailsSorted",
        new DataTypeInfo(NodeId.parse("i=18649"), NodeId.parse("i=18651")));
    DATA_TYPE_INFO_MAP.put(
        "ReadRawModifiedDetails", new DataTypeInfo(NodeId.parse("i=647"), NodeId.parse("i=649")));
    DATA_TYPE_INFO_MAP.put(
        "ReadProcessedDetails", new DataTypeInfo(NodeId.parse("i=650"), NodeId.parse("i=652")));
    DATA_TYPE_INFO_MAP.put(
        "ReadAtTimeDetails", new DataTypeInfo(NodeId.parse("i=653"), NodeId.parse("i=655")));
    DATA_TYPE_INFO_MAP.put(
        "ReadAnnotationDataDetails",
        new DataTypeInfo(NodeId.parse("i=23497"), NodeId.parse("i=23500")));
    DATA_TYPE_INFO_MAP.put(
        "HistoryData", new DataTypeInfo(NodeId.parse("i=656"), NodeId.parse("i=658")));
    DATA_TYPE_INFO_MAP.put(
        "HistoryModifiedData", new DataTypeInfo(NodeId.parse("i=11217"), NodeId.parse("i=11227")));
    DATA_TYPE_INFO_MAP.put(
        "HistoryReadRequest", new DataTypeInfo(NodeId.parse("i=662"), NodeId.parse("i=664")));
    DATA_TYPE_INFO_MAP.put(
        "HistoryReadResponse", new DataTypeInfo(NodeId.parse("i=665"), NodeId.parse("i=667")));
    DATA_TYPE_INFO_MAP.put(
        "WriteValue", new DataTypeInfo(NodeId.parse("i=668"), NodeId.parse("i=670")));
    DATA_TYPE_INFO_MAP.put(
        "WriteRequest", new DataTypeInfo(NodeId.parse("i=671"), NodeId.parse("i=673")));
    DATA_TYPE_INFO_MAP.put(
        "WriteResponse", new DataTypeInfo(NodeId.parse("i=674"), NodeId.parse("i=676")));
    DATA_TYPE_INFO_MAP.put(
        "HistoryUpdateDetails", new DataTypeInfo(NodeId.parse("i=677"), NodeId.parse("i=679")));
    DATA_TYPE_INFO_MAP.put(
        "UpdateDataDetails", new DataTypeInfo(NodeId.parse("i=680"), NodeId.parse("i=682")));
    DATA_TYPE_INFO_MAP.put(
        "UpdateStructureDataDetails",
        new DataTypeInfo(NodeId.parse("i=11295"), NodeId.parse("i=11300")));
    DATA_TYPE_INFO_MAP.put(
        "UpdateEventDetails", new DataTypeInfo(NodeId.parse("i=683"), NodeId.parse("i=685")));
    DATA_TYPE_INFO_MAP.put(
        "DeleteRawModifiedDetails", new DataTypeInfo(NodeId.parse("i=686"), NodeId.parse("i=688")));
    DATA_TYPE_INFO_MAP.put(
        "DeleteAtTimeDetails", new DataTypeInfo(NodeId.parse("i=689"), NodeId.parse("i=691")));
    DATA_TYPE_INFO_MAP.put(
        "DeleteEventDetails", new DataTypeInfo(NodeId.parse("i=692"), NodeId.parse("i=694")));
    DATA_TYPE_INFO_MAP.put(
        "HistoryUpdateResult", new DataTypeInfo(NodeId.parse("i=695"), NodeId.parse("i=697")));
    DATA_TYPE_INFO_MAP.put(
        "HistoryUpdateRequest", new DataTypeInfo(NodeId.parse("i=698"), NodeId.parse("i=700")));
    DATA_TYPE_INFO_MAP.put(
        "HistoryUpdateResponse", new DataTypeInfo(NodeId.parse("i=701"), NodeId.parse("i=703")));
    DATA_TYPE_INFO_MAP.put(
        "CallMethodRequest", new DataTypeInfo(NodeId.parse("i=704"), NodeId.parse("i=706")));
    DATA_TYPE_INFO_MAP.put(
        "CallMethodResult", new DataTypeInfo(NodeId.parse("i=707"), NodeId.parse("i=709")));
    DATA_TYPE_INFO_MAP.put(
        "CallRequest", new DataTypeInfo(NodeId.parse("i=710"), NodeId.parse("i=712")));
    DATA_TYPE_INFO_MAP.put(
        "CallResponse", new DataTypeInfo(NodeId.parse("i=713"), NodeId.parse("i=715")));
    DATA_TYPE_INFO_MAP.put(
        "DataChangeFilter", new DataTypeInfo(NodeId.parse("i=722"), NodeId.parse("i=724")));
    DATA_TYPE_INFO_MAP.put(
        "AggregateFilter", new DataTypeInfo(NodeId.parse("i=728"), NodeId.parse("i=730")));
    DATA_TYPE_INFO_MAP.put(
        "MonitoringFilterResult", new DataTypeInfo(NodeId.parse("i=731"), NodeId.parse("i=733")));
    DATA_TYPE_INFO_MAP.put(
        "EventFilterResult", new DataTypeInfo(NodeId.parse("i=734"), NodeId.parse("i=736")));
    DATA_TYPE_INFO_MAP.put(
        "AggregateFilterResult", new DataTypeInfo(NodeId.parse("i=737"), NodeId.parse("i=739")));
    DATA_TYPE_INFO_MAP.put(
        "MonitoringParameters", new DataTypeInfo(NodeId.parse("i=740"), NodeId.parse("i=742")));
    DATA_TYPE_INFO_MAP.put(
        "MonitoredItemCreateRequest",
        new DataTypeInfo(NodeId.parse("i=743"), NodeId.parse("i=745")));
    DATA_TYPE_INFO_MAP.put(
        "MonitoredItemCreateResult",
        new DataTypeInfo(NodeId.parse("i=746"), NodeId.parse("i=748")));
    DATA_TYPE_INFO_MAP.put(
        "CreateMonitoredItemsRequest",
        new DataTypeInfo(NodeId.parse("i=749"), NodeId.parse("i=751")));
    DATA_TYPE_INFO_MAP.put(
        "CreateMonitoredItemsResponse",
        new DataTypeInfo(NodeId.parse("i=752"), NodeId.parse("i=754")));
    DATA_TYPE_INFO_MAP.put(
        "MonitoredItemModifyRequest",
        new DataTypeInfo(NodeId.parse("i=755"), NodeId.parse("i=757")));
    DATA_TYPE_INFO_MAP.put(
        "MonitoredItemModifyResult",
        new DataTypeInfo(NodeId.parse("i=758"), NodeId.parse("i=760")));
    DATA_TYPE_INFO_MAP.put(
        "ModifyMonitoredItemsRequest",
        new DataTypeInfo(NodeId.parse("i=761"), NodeId.parse("i=763")));
    DATA_TYPE_INFO_MAP.put(
        "ModifyMonitoredItemsResponse",
        new DataTypeInfo(NodeId.parse("i=764"), NodeId.parse("i=766")));
    DATA_TYPE_INFO_MAP.put(
        "SetMonitoringModeRequest", new DataTypeInfo(NodeId.parse("i=767"), NodeId.parse("i=769")));
    DATA_TYPE_INFO_MAP.put(
        "SetMonitoringModeResponse",
        new DataTypeInfo(NodeId.parse("i=770"), NodeId.parse("i=772")));
    DATA_TYPE_INFO_MAP.put(
        "SetTriggeringRequest", new DataTypeInfo(NodeId.parse("i=773"), NodeId.parse("i=775")));
    DATA_TYPE_INFO_MAP.put(
        "SetTriggeringResponse", new DataTypeInfo(NodeId.parse("i=776"), NodeId.parse("i=778")));
    DATA_TYPE_INFO_MAP.put(
        "DeleteMonitoredItemsRequest",
        new DataTypeInfo(NodeId.parse("i=779"), NodeId.parse("i=781")));
    DATA_TYPE_INFO_MAP.put(
        "DeleteMonitoredItemsResponse",
        new DataTypeInfo(NodeId.parse("i=782"), NodeId.parse("i=784")));
    DATA_TYPE_INFO_MAP.put(
        "CreateSubscriptionRequest",
        new DataTypeInfo(NodeId.parse("i=785"), NodeId.parse("i=787")));
    DATA_TYPE_INFO_MAP.put(
        "CreateSubscriptionResponse",
        new DataTypeInfo(NodeId.parse("i=788"), NodeId.parse("i=790")));
    DATA_TYPE_INFO_MAP.put(
        "ModifySubscriptionRequest",
        new DataTypeInfo(NodeId.parse("i=791"), NodeId.parse("i=793")));
    DATA_TYPE_INFO_MAP.put(
        "ModifySubscriptionResponse",
        new DataTypeInfo(NodeId.parse("i=794"), NodeId.parse("i=796")));
    DATA_TYPE_INFO_MAP.put(
        "SetPublishingModeRequest", new DataTypeInfo(NodeId.parse("i=797"), NodeId.parse("i=799")));
    DATA_TYPE_INFO_MAP.put(
        "SetPublishingModeResponse",
        new DataTypeInfo(NodeId.parse("i=800"), NodeId.parse("i=802")));
    DATA_TYPE_INFO_MAP.put(
        "NotificationMessage", new DataTypeInfo(NodeId.parse("i=803"), NodeId.parse("i=805")));
    DATA_TYPE_INFO_MAP.put(
        "NotificationData", new DataTypeInfo(NodeId.parse("i=945"), NodeId.parse("i=947")));
    DATA_TYPE_INFO_MAP.put(
        "DataChangeNotification", new DataTypeInfo(NodeId.parse("i=809"), NodeId.parse("i=811")));
    DATA_TYPE_INFO_MAP.put(
        "MonitoredItemNotification",
        new DataTypeInfo(NodeId.parse("i=806"), NodeId.parse("i=808")));
    DATA_TYPE_INFO_MAP.put(
        "EventNotificationList", new DataTypeInfo(NodeId.parse("i=914"), NodeId.parse("i=916")));
    DATA_TYPE_INFO_MAP.put(
        "EventFieldList", new DataTypeInfo(NodeId.parse("i=917"), NodeId.parse("i=919")));
    DATA_TYPE_INFO_MAP.put(
        "StatusChangeNotification", new DataTypeInfo(NodeId.parse("i=818"), NodeId.parse("i=820")));
    DATA_TYPE_INFO_MAP.put(
        "SubscriptionAcknowledgement",
        new DataTypeInfo(NodeId.parse("i=821"), NodeId.parse("i=823")));
    DATA_TYPE_INFO_MAP.put(
        "PublishRequest", new DataTypeInfo(NodeId.parse("i=824"), NodeId.parse("i=826")));
    DATA_TYPE_INFO_MAP.put(
        "PublishResponse", new DataTypeInfo(NodeId.parse("i=827"), NodeId.parse("i=829")));
    DATA_TYPE_INFO_MAP.put(
        "RepublishRequest", new DataTypeInfo(NodeId.parse("i=830"), NodeId.parse("i=832")));
    DATA_TYPE_INFO_MAP.put(
        "RepublishResponse", new DataTypeInfo(NodeId.parse("i=833"), NodeId.parse("i=835")));
    DATA_TYPE_INFO_MAP.put(
        "TransferResult", new DataTypeInfo(NodeId.parse("i=836"), NodeId.parse("i=838")));
    DATA_TYPE_INFO_MAP.put(
        "TransferSubscriptionsRequest",
        new DataTypeInfo(NodeId.parse("i=839"), NodeId.parse("i=841")));
    DATA_TYPE_INFO_MAP.put(
        "TransferSubscriptionsResponse",
        new DataTypeInfo(NodeId.parse("i=842"), NodeId.parse("i=844")));
    DATA_TYPE_INFO_MAP.put(
        "DeleteSubscriptionsRequest",
        new DataTypeInfo(NodeId.parse("i=845"), NodeId.parse("i=847")));
    DATA_TYPE_INFO_MAP.put(
        "DeleteSubscriptionsResponse",
        new DataTypeInfo(NodeId.parse("i=848"), NodeId.parse("i=850")));
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

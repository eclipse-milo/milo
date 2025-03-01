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

abstract class NodeIds0 extends NodeIds1 {
  public static final NodeId Boolean = new NodeId(UShort.MIN, uint(1));

  public static final NodeId SByte = new NodeId(UShort.MIN, uint(2));

  public static final NodeId Byte = new NodeId(UShort.MIN, uint(3));

  public static final NodeId Int16 = new NodeId(UShort.MIN, uint(4));

  public static final NodeId UInt16 = new NodeId(UShort.MIN, uint(5));

  public static final NodeId Int32 = new NodeId(UShort.MIN, uint(6));

  public static final NodeId UInt32 = new NodeId(UShort.MIN, uint(7));

  public static final NodeId Int64 = new NodeId(UShort.MIN, uint(8));

  public static final NodeId UInt64 = new NodeId(UShort.MIN, uint(9));

  public static final NodeId Float = new NodeId(UShort.MIN, uint(10));

  public static final NodeId Double = new NodeId(UShort.MIN, uint(11));

  public static final NodeId String = new NodeId(UShort.MIN, uint(12));

  public static final NodeId DateTime = new NodeId(UShort.MIN, uint(13));

  public static final NodeId Guid = new NodeId(UShort.MIN, uint(14));

  public static final NodeId ByteString = new NodeId(UShort.MIN, uint(15));

  public static final NodeId XmlElement = new NodeId(UShort.MIN, uint(16));

  public static final NodeId NodeId = new NodeId(UShort.MIN, uint(17));

  public static final NodeId ExpandedNodeId = new NodeId(UShort.MIN, uint(18));

  public static final NodeId StatusCode = new NodeId(UShort.MIN, uint(19));

  public static final NodeId QualifiedName = new NodeId(UShort.MIN, uint(20));

  public static final NodeId LocalizedText = new NodeId(UShort.MIN, uint(21));

  public static final NodeId Structure = new NodeId(UShort.MIN, uint(22));

  public static final NodeId DataValue = new NodeId(UShort.MIN, uint(23));

  public static final NodeId BaseDataType = new NodeId(UShort.MIN, uint(24));

  public static final NodeId DiagnosticInfo = new NodeId(UShort.MIN, uint(25));

  public static final NodeId Number = new NodeId(UShort.MIN, uint(26));

  public static final NodeId Integer = new NodeId(UShort.MIN, uint(27));

  public static final NodeId UInteger = new NodeId(UShort.MIN, uint(28));

  public static final NodeId Enumeration = new NodeId(UShort.MIN, uint(29));

  public static final NodeId Image = new NodeId(UShort.MIN, uint(30));

  public static final NodeId References = new NodeId(UShort.MIN, uint(31));

  public static final NodeId NonHierarchicalReferences = new NodeId(UShort.MIN, uint(32));

  public static final NodeId HierarchicalReferences = new NodeId(UShort.MIN, uint(33));

  public static final NodeId HasChild = new NodeId(UShort.MIN, uint(34));

  public static final NodeId Organizes = new NodeId(UShort.MIN, uint(35));

  public static final NodeId HasEventSource = new NodeId(UShort.MIN, uint(36));

  public static final NodeId HasModellingRule = new NodeId(UShort.MIN, uint(37));

  public static final NodeId HasEncoding = new NodeId(UShort.MIN, uint(38));

  public static final NodeId HasDescription = new NodeId(UShort.MIN, uint(39));

  public static final NodeId HasTypeDefinition = new NodeId(UShort.MIN, uint(40));

  public static final NodeId GeneratesEvent = new NodeId(UShort.MIN, uint(41));

  public static final NodeId Aggregates = new NodeId(UShort.MIN, uint(44));

  public static final NodeId HasSubtype = new NodeId(UShort.MIN, uint(45));

  public static final NodeId HasProperty = new NodeId(UShort.MIN, uint(46));

  public static final NodeId HasComponent = new NodeId(UShort.MIN, uint(47));

  public static final NodeId HasNotifier = new NodeId(UShort.MIN, uint(48));

  public static final NodeId HasOrderedComponent = new NodeId(UShort.MIN, uint(49));

  public static final NodeId Decimal = new NodeId(UShort.MIN, uint(50));

  public static final NodeId FromState = new NodeId(UShort.MIN, uint(51));

  public static final NodeId ToState = new NodeId(UShort.MIN, uint(52));

  public static final NodeId HasCause = new NodeId(UShort.MIN, uint(53));

  public static final NodeId HasEffect = new NodeId(UShort.MIN, uint(54));

  public static final NodeId HasHistoricalConfiguration = new NodeId(UShort.MIN, uint(56));

  public static final NodeId BaseObjectType = new NodeId(UShort.MIN, uint(58));

  public static final NodeId FolderType = new NodeId(UShort.MIN, uint(61));

  public static final NodeId BaseVariableType = new NodeId(UShort.MIN, uint(62));

  public static final NodeId BaseDataVariableType = new NodeId(UShort.MIN, uint(63));

  public static final NodeId PropertyType = new NodeId(UShort.MIN, uint(68));

  public static final NodeId DataTypeDescriptionType = new NodeId(UShort.MIN, uint(69));

  public static final NodeId DataTypeDictionaryType = new NodeId(UShort.MIN, uint(72));

  public static final NodeId DataTypeSystemType = new NodeId(UShort.MIN, uint(75));

  public static final NodeId DataTypeEncodingType = new NodeId(UShort.MIN, uint(76));

  public static final NodeId ModellingRuleType = new NodeId(UShort.MIN, uint(77));

  public static final NodeId ModellingRule_Mandatory = new NodeId(UShort.MIN, uint(78));

  public static final NodeId ModellingRule_Optional = new NodeId(UShort.MIN, uint(80));

  public static final NodeId ModellingRule_ExposesItsArray = new NodeId(UShort.MIN, uint(83));

  public static final NodeId RootFolder = new NodeId(UShort.MIN, uint(84));

  public static final NodeId ObjectsFolder = new NodeId(UShort.MIN, uint(85));

  public static final NodeId TypesFolder = new NodeId(UShort.MIN, uint(86));

  public static final NodeId ViewsFolder = new NodeId(UShort.MIN, uint(87));

  public static final NodeId ObjectTypesFolder = new NodeId(UShort.MIN, uint(88));

  public static final NodeId VariableTypesFolder = new NodeId(UShort.MIN, uint(89));

  public static final NodeId DataTypesFolder = new NodeId(UShort.MIN, uint(90));

  public static final NodeId ReferenceTypesFolder = new NodeId(UShort.MIN, uint(91));

  public static final NodeId XmlSchema_TypeSystem = new NodeId(UShort.MIN, uint(92));

  public static final NodeId OPCBinarySchema_TypeSystem = new NodeId(UShort.MIN, uint(93));

  public static final NodeId PermissionType = new NodeId(UShort.MIN, uint(94));

  public static final NodeId AccessRestrictionType = new NodeId(UShort.MIN, uint(95));

  public static final NodeId RolePermissionType = new NodeId(UShort.MIN, uint(96));

  public static final NodeId DataTypeDefinition = new NodeId(UShort.MIN, uint(97));

  public static final NodeId StructureType = new NodeId(UShort.MIN, uint(98));

  public static final NodeId StructureDefinition = new NodeId(UShort.MIN, uint(99));

  public static final NodeId EnumDefinition = new NodeId(UShort.MIN, uint(100));

  public static final NodeId StructureField = new NodeId(UShort.MIN, uint(101));

  public static final NodeId EnumField = new NodeId(UShort.MIN, uint(102));

  public static final NodeId DataTypeDescriptionType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(104));

  public static final NodeId DataTypeDescriptionType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(105));

  public static final NodeId DataTypeDictionaryType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(106));

  public static final NodeId DataTypeDictionaryType_NamespaceUri =
      new NodeId(UShort.MIN, uint(107));

  public static final NodeId HasSubStateMachine = new NodeId(UShort.MIN, uint(117));

  public static final NodeId NamingRuleType = new NodeId(UShort.MIN, uint(120));

  public static final NodeId DataTypeDefinition_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(121));

  public static final NodeId StructureDefinition_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(122));

  public static final NodeId EnumDefinition_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(123));

  public static final NodeId DataSetMetaDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(124));

  public static final NodeId DataTypeDescription_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(125));

  public static final NodeId StructureDescription_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(126));

  public static final NodeId EnumDescription_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(127));

  public static final NodeId RolePermissionType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(128));

  public static final NodeId HasArgumentDescription = new NodeId(UShort.MIN, uint(129));

  public static final NodeId HasOptionalInputArgumentDescription =
      new NodeId(UShort.MIN, uint(131));

  public static final NodeId IdType = new NodeId(UShort.MIN, uint(256));

  public static final NodeId NodeClass = new NodeId(UShort.MIN, uint(257));

  public static final NodeId Node = new NodeId(UShort.MIN, uint(258));

  public static final NodeId Node_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(259));

  public static final NodeId Node_Encoding_DefaultBinary = new NodeId(UShort.MIN, uint(260));

  public static final NodeId ObjectNode = new NodeId(UShort.MIN, uint(261));

  public static final NodeId ObjectNode_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(262));

  public static final NodeId ObjectNode_Encoding_DefaultBinary = new NodeId(UShort.MIN, uint(263));

  public static final NodeId ObjectTypeNode = new NodeId(UShort.MIN, uint(264));

  public static final NodeId ObjectTypeNode_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(265));

  public static final NodeId ObjectTypeNode_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(266));

  public static final NodeId VariableNode = new NodeId(UShort.MIN, uint(267));

  public static final NodeId VariableNode_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(268));

  public static final NodeId VariableNode_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(269));

  public static final NodeId VariableTypeNode = new NodeId(UShort.MIN, uint(270));

  public static final NodeId VariableTypeNode_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(271));

  public static final NodeId VariableTypeNode_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(272));

  public static final NodeId ReferenceTypeNode = new NodeId(UShort.MIN, uint(273));

  public static final NodeId ReferenceTypeNode_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(274));

  public static final NodeId ReferenceTypeNode_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(275));

  public static final NodeId MethodNode = new NodeId(UShort.MIN, uint(276));

  public static final NodeId MethodNode_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(277));

  public static final NodeId MethodNode_Encoding_DefaultBinary = new NodeId(UShort.MIN, uint(278));

  public static final NodeId ViewNode = new NodeId(UShort.MIN, uint(279));

  public static final NodeId ViewNode_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(280));

  public static final NodeId ViewNode_Encoding_DefaultBinary = new NodeId(UShort.MIN, uint(281));

  public static final NodeId DataTypeNode = new NodeId(UShort.MIN, uint(282));

  public static final NodeId DataTypeNode_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(283));

  public static final NodeId DataTypeNode_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(284));

  public static final NodeId ReferenceNode = new NodeId(UShort.MIN, uint(285));

  public static final NodeId ReferenceNode_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(286));

  public static final NodeId ReferenceNode_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(287));

  public static final NodeId IntegerId = new NodeId(UShort.MIN, uint(288));

  public static final NodeId Counter = new NodeId(UShort.MIN, uint(289));

  public static final NodeId Duration = new NodeId(UShort.MIN, uint(290));

  public static final NodeId NumericRange = new NodeId(UShort.MIN, uint(291));

  public static final NodeId UtcTime = new NodeId(UShort.MIN, uint(294));

  public static final NodeId LocaleId = new NodeId(UShort.MIN, uint(295));

  public static final NodeId Argument = new NodeId(UShort.MIN, uint(296));

  public static final NodeId Argument_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(297));

  public static final NodeId Argument_Encoding_DefaultBinary = new NodeId(UShort.MIN, uint(298));

  public static final NodeId StatusResult = new NodeId(UShort.MIN, uint(299));

  public static final NodeId StatusResult_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(300));

  public static final NodeId StatusResult_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(301));

  public static final NodeId MessageSecurityMode = new NodeId(UShort.MIN, uint(302));

  public static final NodeId UserTokenType = new NodeId(UShort.MIN, uint(303));

  public static final NodeId UserTokenPolicy = new NodeId(UShort.MIN, uint(304));

  public static final NodeId UserTokenPolicy_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(305));

  public static final NodeId UserTokenPolicy_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(306));

  public static final NodeId ApplicationType = new NodeId(UShort.MIN, uint(307));

  public static final NodeId ApplicationDescription = new NodeId(UShort.MIN, uint(308));

  public static final NodeId ApplicationDescription_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(309));

  public static final NodeId ApplicationDescription_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(310));

  public static final NodeId ApplicationInstanceCertificate = new NodeId(UShort.MIN, uint(311));

  public static final NodeId EndpointDescription = new NodeId(UShort.MIN, uint(312));

  public static final NodeId EndpointDescription_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(313));

  public static final NodeId EndpointDescription_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(314));

  public static final NodeId SecurityTokenRequestType = new NodeId(UShort.MIN, uint(315));

  public static final NodeId UserIdentityToken = new NodeId(UShort.MIN, uint(316));

  public static final NodeId UserIdentityToken_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(317));

  public static final NodeId UserIdentityToken_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(318));

  public static final NodeId AnonymousIdentityToken = new NodeId(UShort.MIN, uint(319));

  public static final NodeId AnonymousIdentityToken_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(320));

  public static final NodeId AnonymousIdentityToken_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(321));

  public static final NodeId UserNameIdentityToken = new NodeId(UShort.MIN, uint(322));

  public static final NodeId UserNameIdentityToken_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(323));

  public static final NodeId UserNameIdentityToken_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(324));

  public static final NodeId X509IdentityToken = new NodeId(UShort.MIN, uint(325));

  public static final NodeId X509IdentityToken_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(326));

  public static final NodeId X509IdentityToken_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(327));

  public static final NodeId EndpointConfiguration = new NodeId(UShort.MIN, uint(331));

  public static final NodeId EndpointConfiguration_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(332));

  public static final NodeId EndpointConfiguration_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(333));

  public static final NodeId BuildInfo = new NodeId(UShort.MIN, uint(338));

  public static final NodeId BuildInfo_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(339));

  public static final NodeId BuildInfo_Encoding_DefaultBinary = new NodeId(UShort.MIN, uint(340));

  public static final NodeId SignedSoftwareCertificate = new NodeId(UShort.MIN, uint(344));

  public static final NodeId SignedSoftwareCertificate_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(345));

  public static final NodeId SignedSoftwareCertificate_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(346));

  public static final NodeId AttributeWriteMask = new NodeId(UShort.MIN, uint(347));

  public static final NodeId NodeAttributesMask = new NodeId(UShort.MIN, uint(348));

  public static final NodeId NodeAttributes = new NodeId(UShort.MIN, uint(349));

  public static final NodeId NodeAttributes_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(350));

  public static final NodeId NodeAttributes_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(351));

  public static final NodeId ObjectAttributes = new NodeId(UShort.MIN, uint(352));

  public static final NodeId ObjectAttributes_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(353));

  public static final NodeId ObjectAttributes_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(354));

  public static final NodeId VariableAttributes = new NodeId(UShort.MIN, uint(355));

  public static final NodeId VariableAttributes_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(356));

  public static final NodeId VariableAttributes_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(357));

  public static final NodeId MethodAttributes = new NodeId(UShort.MIN, uint(358));

  public static final NodeId MethodAttributes_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(359));

  public static final NodeId MethodAttributes_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(360));

  public static final NodeId ObjectTypeAttributes = new NodeId(UShort.MIN, uint(361));

  public static final NodeId ObjectTypeAttributes_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(362));

  public static final NodeId ObjectTypeAttributes_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(363));

  public static final NodeId VariableTypeAttributes = new NodeId(UShort.MIN, uint(364));

  public static final NodeId VariableTypeAttributes_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(365));

  public static final NodeId VariableTypeAttributes_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(366));

  public static final NodeId ReferenceTypeAttributes = new NodeId(UShort.MIN, uint(367));

  public static final NodeId ReferenceTypeAttributes_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(368));

  public static final NodeId ReferenceTypeAttributes_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(369));

  public static final NodeId DataTypeAttributes = new NodeId(UShort.MIN, uint(370));

  public static final NodeId DataTypeAttributes_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(371));

  public static final NodeId DataTypeAttributes_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(372));

  public static final NodeId ViewAttributes = new NodeId(UShort.MIN, uint(373));

  public static final NodeId ViewAttributes_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(374));

  public static final NodeId ViewAttributes_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(375));

  public static final NodeId AddNodesItem = new NodeId(UShort.MIN, uint(376));

  public static final NodeId AddNodesItem_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(377));

  public static final NodeId AddNodesItem_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(378));

  public static final NodeId AddReferencesItem = new NodeId(UShort.MIN, uint(379));

  public static final NodeId AddReferencesItem_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(380));

  public static final NodeId AddReferencesItem_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(381));

  public static final NodeId DeleteNodesItem = new NodeId(UShort.MIN, uint(382));

  public static final NodeId DeleteNodesItem_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(383));

  public static final NodeId DeleteNodesItem_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(384));

  public static final NodeId DeleteReferencesItem = new NodeId(UShort.MIN, uint(385));

  public static final NodeId DeleteReferencesItem_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(386));

  public static final NodeId DeleteReferencesItem_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(387));

  public static final NodeId SessionAuthenticationToken = new NodeId(UShort.MIN, uint(388));

  public static final NodeId RequestHeader = new NodeId(UShort.MIN, uint(389));

  public static final NodeId RequestHeader_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(390));

  public static final NodeId RequestHeader_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(391));

  public static final NodeId ResponseHeader = new NodeId(UShort.MIN, uint(392));

  public static final NodeId ResponseHeader_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(393));

  public static final NodeId ResponseHeader_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(394));

  public static final NodeId ServiceFault = new NodeId(UShort.MIN, uint(395));

  public static final NodeId ServiceFault_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(396));

  public static final NodeId ServiceFault_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(397));

  public static final NodeId FindServersRequest = new NodeId(UShort.MIN, uint(420));

  public static final NodeId FindServersRequest_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(421));

  public static final NodeId FindServersRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(422));

  public static final NodeId FindServersResponse = new NodeId(UShort.MIN, uint(423));

  public static final NodeId FindServersResponse_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(424));

  public static final NodeId FindServersResponse_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(425));

  public static final NodeId GetEndpointsRequest = new NodeId(UShort.MIN, uint(426));

  public static final NodeId GetEndpointsRequest_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(427));

  public static final NodeId GetEndpointsRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(428));

  public static final NodeId GetEndpointsResponse = new NodeId(UShort.MIN, uint(429));

  public static final NodeId GetEndpointsResponse_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(430));

  public static final NodeId GetEndpointsResponse_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(431));

  public static final NodeId RegisteredServer = new NodeId(UShort.MIN, uint(432));

  public static final NodeId RegisteredServer_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(433));

  public static final NodeId RegisteredServer_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(434));

  public static final NodeId RegisterServerRequest = new NodeId(UShort.MIN, uint(435));

  public static final NodeId RegisterServerRequest_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(436));

  public static final NodeId RegisterServerRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(437));

  public static final NodeId RegisterServerResponse = new NodeId(UShort.MIN, uint(438));

  public static final NodeId RegisterServerResponse_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(439));

  public static final NodeId RegisterServerResponse_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(440));

  public static final NodeId ChannelSecurityToken = new NodeId(UShort.MIN, uint(441));

  public static final NodeId ChannelSecurityToken_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(442));

  public static final NodeId ChannelSecurityToken_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(443));

  public static final NodeId OpenSecureChannelRequest = new NodeId(UShort.MIN, uint(444));

  public static final NodeId OpenSecureChannelRequest_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(445));

  public static final NodeId OpenSecureChannelRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(446));

  public static final NodeId OpenSecureChannelResponse = new NodeId(UShort.MIN, uint(447));

  public static final NodeId OpenSecureChannelResponse_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(448));

  public static final NodeId OpenSecureChannelResponse_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(449));

  public static final NodeId CloseSecureChannelRequest = new NodeId(UShort.MIN, uint(450));

  public static final NodeId CloseSecureChannelRequest_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(451));

  public static final NodeId CloseSecureChannelRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(452));

  public static final NodeId CloseSecureChannelResponse = new NodeId(UShort.MIN, uint(453));

  public static final NodeId CloseSecureChannelResponse_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(454));

  public static final NodeId CloseSecureChannelResponse_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(455));

  public static final NodeId SignatureData = new NodeId(UShort.MIN, uint(456));

  public static final NodeId SignatureData_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(457));

  public static final NodeId SignatureData_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(458));

  public static final NodeId CreateSessionRequest = new NodeId(UShort.MIN, uint(459));

  public static final NodeId CreateSessionRequest_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(460));

  public static final NodeId CreateSessionRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(461));

  public static final NodeId CreateSessionResponse = new NodeId(UShort.MIN, uint(462));

  public static final NodeId CreateSessionResponse_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(463));

  public static final NodeId CreateSessionResponse_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(464));

  public static final NodeId ActivateSessionRequest = new NodeId(UShort.MIN, uint(465));

  public static final NodeId ActivateSessionRequest_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(466));

  public static final NodeId ActivateSessionRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(467));

  public static final NodeId ActivateSessionResponse = new NodeId(UShort.MIN, uint(468));

  public static final NodeId ActivateSessionResponse_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(469));

  public static final NodeId ActivateSessionResponse_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(470));

  public static final NodeId CloseSessionRequest = new NodeId(UShort.MIN, uint(471));

  public static final NodeId CloseSessionRequest_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(472));

  public static final NodeId CloseSessionRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(473));

  public static final NodeId CloseSessionResponse = new NodeId(UShort.MIN, uint(474));

  public static final NodeId CloseSessionResponse_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(475));

  public static final NodeId CloseSessionResponse_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(476));

  public static final NodeId CancelRequest = new NodeId(UShort.MIN, uint(477));

  public static final NodeId CancelRequest_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(478));

  public static final NodeId CancelRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(479));

  public static final NodeId CancelResponse = new NodeId(UShort.MIN, uint(480));

  public static final NodeId CancelResponse_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(481));

  public static final NodeId CancelResponse_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(482));

  public static final NodeId AddNodesResult = new NodeId(UShort.MIN, uint(483));

  public static final NodeId AddNodesResult_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(484));

  public static final NodeId AddNodesResult_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(485));

  public static final NodeId AddNodesRequest = new NodeId(UShort.MIN, uint(486));

  public static final NodeId AddNodesRequest_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(487));

  public static final NodeId AddNodesRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(488));

  public static final NodeId AddNodesResponse = new NodeId(UShort.MIN, uint(489));

  public static final NodeId AddNodesResponse_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(490));

  public static final NodeId AddNodesResponse_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(491));

  public static final NodeId AddReferencesRequest = new NodeId(UShort.MIN, uint(492));

  public static final NodeId AddReferencesRequest_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(493));

  public static final NodeId AddReferencesRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(494));

  public static final NodeId AddReferencesResponse = new NodeId(UShort.MIN, uint(495));

  public static final NodeId AddReferencesResponse_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(496));

  public static final NodeId AddReferencesResponse_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(497));

  public static final NodeId DeleteNodesRequest = new NodeId(UShort.MIN, uint(498));

  public static final NodeId DeleteNodesRequest_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(499));

  public static final NodeId DeleteNodesRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(500));

  public static final NodeId DeleteNodesResponse = new NodeId(UShort.MIN, uint(501));

  public static final NodeId DeleteNodesResponse_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(502));

  public static final NodeId DeleteNodesResponse_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(503));

  public static final NodeId DeleteReferencesRequest = new NodeId(UShort.MIN, uint(504));

  public static final NodeId DeleteReferencesRequest_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(505));

  public static final NodeId DeleteReferencesRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(506));

  public static final NodeId DeleteReferencesResponse = new NodeId(UShort.MIN, uint(507));

  public static final NodeId DeleteReferencesResponse_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(508));

  public static final NodeId DeleteReferencesResponse_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(509));

  public static final NodeId BrowseDirection = new NodeId(UShort.MIN, uint(510));

  public static final NodeId ViewDescription = new NodeId(UShort.MIN, uint(511));

  public static final NodeId ViewDescription_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(512));

  public static final NodeId ViewDescription_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(513));

  public static final NodeId BrowseDescription = new NodeId(UShort.MIN, uint(514));

  public static final NodeId BrowseDescription_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(515));

  public static final NodeId BrowseDescription_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(516));

  public static final NodeId BrowseResultMask = new NodeId(UShort.MIN, uint(517));

  public static final NodeId ReferenceDescription = new NodeId(UShort.MIN, uint(518));

  public static final NodeId ReferenceDescription_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(519));

  public static final NodeId ReferenceDescription_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(520));

  public static final NodeId ContinuationPoint = new NodeId(UShort.MIN, uint(521));

  public static final NodeId BrowseResult = new NodeId(UShort.MIN, uint(522));

  public static final NodeId BrowseResult_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(523));

  public static final NodeId BrowseResult_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(524));

  public static final NodeId BrowseRequest = new NodeId(UShort.MIN, uint(525));

  public static final NodeId BrowseRequest_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(526));

  public static final NodeId BrowseRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(527));

  public static final NodeId BrowseResponse = new NodeId(UShort.MIN, uint(528));

  public static final NodeId BrowseResponse_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(529));

  public static final NodeId BrowseResponse_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(530));

  public static final NodeId BrowseNextRequest = new NodeId(UShort.MIN, uint(531));

  public static final NodeId BrowseNextRequest_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(532));

  public static final NodeId BrowseNextRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(533));

  public static final NodeId BrowseNextResponse = new NodeId(UShort.MIN, uint(534));

  public static final NodeId BrowseNextResponse_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(535));

  public static final NodeId BrowseNextResponse_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(536));

  public static final NodeId RelativePathElement = new NodeId(UShort.MIN, uint(537));

  public static final NodeId RelativePathElement_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(538));

  public static final NodeId RelativePathElement_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(539));

  public static final NodeId RelativePath = new NodeId(UShort.MIN, uint(540));

  public static final NodeId RelativePath_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(541));

  public static final NodeId RelativePath_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(542));

  public static final NodeId BrowsePath = new NodeId(UShort.MIN, uint(543));

  public static final NodeId BrowsePath_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(544));

  public static final NodeId BrowsePath_Encoding_DefaultBinary = new NodeId(UShort.MIN, uint(545));

  public static final NodeId BrowsePathTarget = new NodeId(UShort.MIN, uint(546));

  public static final NodeId BrowsePathTarget_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(547));

  public static final NodeId BrowsePathTarget_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(548));

  public static final NodeId BrowsePathResult = new NodeId(UShort.MIN, uint(549));

  public static final NodeId BrowsePathResult_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(550));

  public static final NodeId BrowsePathResult_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(551));

  public static final NodeId TranslateBrowsePathsToNodeIdsRequest =
      new NodeId(UShort.MIN, uint(552));

  public static final NodeId TranslateBrowsePathsToNodeIdsRequest_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(553));

  public static final NodeId TranslateBrowsePathsToNodeIdsRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(554));

  public static final NodeId TranslateBrowsePathsToNodeIdsResponse =
      new NodeId(UShort.MIN, uint(555));

  public static final NodeId TranslateBrowsePathsToNodeIdsResponse_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(556));

  public static final NodeId TranslateBrowsePathsToNodeIdsResponse_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(557));

  public static final NodeId RegisterNodesRequest = new NodeId(UShort.MIN, uint(558));

  public static final NodeId RegisterNodesRequest_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(559));

  public static final NodeId RegisterNodesRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(560));

  public static final NodeId RegisterNodesResponse = new NodeId(UShort.MIN, uint(561));

  public static final NodeId RegisterNodesResponse_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(562));

  public static final NodeId RegisterNodesResponse_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(563));

  public static final NodeId UnregisterNodesRequest = new NodeId(UShort.MIN, uint(564));

  public static final NodeId UnregisterNodesRequest_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(565));

  public static final NodeId UnregisterNodesRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(566));

  public static final NodeId UnregisterNodesResponse = new NodeId(UShort.MIN, uint(567));

  public static final NodeId UnregisterNodesResponse_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(568));

  public static final NodeId UnregisterNodesResponse_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(569));

  public static final NodeId QueryDataDescription = new NodeId(UShort.MIN, uint(570));

  public static final NodeId QueryDataDescription_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(571));

  public static final NodeId QueryDataDescription_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(572));

  public static final NodeId NodeTypeDescription = new NodeId(UShort.MIN, uint(573));

  public static final NodeId NodeTypeDescription_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(574));

  public static final NodeId NodeTypeDescription_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(575));

  public static final NodeId FilterOperator = new NodeId(UShort.MIN, uint(576));

  public static final NodeId QueryDataSet = new NodeId(UShort.MIN, uint(577));

  public static final NodeId QueryDataSet_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(578));

  public static final NodeId QueryDataSet_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(579));

  public static final NodeId NodeReference = new NodeId(UShort.MIN, uint(580));

  public static final NodeId NodeReference_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(581));

  public static final NodeId NodeReference_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(582));

  public static final NodeId ContentFilterElement = new NodeId(UShort.MIN, uint(583));

  public static final NodeId ContentFilterElement_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(584));

  public static final NodeId ContentFilterElement_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(585));

  public static final NodeId ContentFilter = new NodeId(UShort.MIN, uint(586));

  public static final NodeId ContentFilter_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(587));

  public static final NodeId ContentFilter_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(588));

  public static final NodeId FilterOperand = new NodeId(UShort.MIN, uint(589));

  public static final NodeId FilterOperand_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(590));

  public static final NodeId FilterOperand_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(591));

  public static final NodeId ElementOperand = new NodeId(UShort.MIN, uint(592));

  public static final NodeId ElementOperand_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(593));

  public static final NodeId ElementOperand_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(594));

  public static final NodeId LiteralOperand = new NodeId(UShort.MIN, uint(595));

  public static final NodeId LiteralOperand_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(596));

  public static final NodeId LiteralOperand_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(597));

  public static final NodeId AttributeOperand = new NodeId(UShort.MIN, uint(598));

  public static final NodeId AttributeOperand_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(599));

  public static final NodeId AttributeOperand_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(600));

  public static final NodeId SimpleAttributeOperand = new NodeId(UShort.MIN, uint(601));

  public static final NodeId SimpleAttributeOperand_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(602));

  public static final NodeId SimpleAttributeOperand_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(603));

  public static final NodeId ContentFilterElementResult = new NodeId(UShort.MIN, uint(604));

  public static final NodeId ContentFilterElementResult_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(605));

  public static final NodeId ContentFilterElementResult_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(606));

  public static final NodeId ContentFilterResult = new NodeId(UShort.MIN, uint(607));

  public static final NodeId ContentFilterResult_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(608));

  public static final NodeId ContentFilterResult_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(609));

  public static final NodeId ParsingResult = new NodeId(UShort.MIN, uint(610));

  public static final NodeId ParsingResult_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(611));

  public static final NodeId ParsingResult_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(612));

  public static final NodeId QueryFirstRequest = new NodeId(UShort.MIN, uint(613));

  public static final NodeId QueryFirstRequest_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(614));

  public static final NodeId QueryFirstRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(615));

  public static final NodeId QueryFirstResponse = new NodeId(UShort.MIN, uint(616));

  public static final NodeId QueryFirstResponse_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(617));

  public static final NodeId QueryFirstResponse_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(618));

  public static final NodeId QueryNextRequest = new NodeId(UShort.MIN, uint(619));

  public static final NodeId QueryNextRequest_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(620));

  public static final NodeId QueryNextRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(621));

  public static final NodeId QueryNextResponse = new NodeId(UShort.MIN, uint(622));

  public static final NodeId QueryNextResponse_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(623));

  public static final NodeId QueryNextResponse_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(624));

  public static final NodeId TimestampsToReturn = new NodeId(UShort.MIN, uint(625));

  public static final NodeId ReadValueId = new NodeId(UShort.MIN, uint(626));

  public static final NodeId ReadValueId_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(627));

  public static final NodeId ReadValueId_Encoding_DefaultBinary = new NodeId(UShort.MIN, uint(628));

  public static final NodeId ReadRequest = new NodeId(UShort.MIN, uint(629));

  public static final NodeId ReadRequest_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(630));

  public static final NodeId ReadRequest_Encoding_DefaultBinary = new NodeId(UShort.MIN, uint(631));

  public static final NodeId ReadResponse = new NodeId(UShort.MIN, uint(632));

  public static final NodeId ReadResponse_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(633));

  public static final NodeId ReadResponse_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(634));

  public static final NodeId HistoryReadValueId = new NodeId(UShort.MIN, uint(635));

  public static final NodeId HistoryReadValueId_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(636));

  public static final NodeId HistoryReadValueId_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(637));

  public static final NodeId HistoryReadResult = new NodeId(UShort.MIN, uint(638));

  public static final NodeId HistoryReadResult_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(639));

  public static final NodeId HistoryReadResult_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(640));

  public static final NodeId HistoryReadDetails = new NodeId(UShort.MIN, uint(641));

  public static final NodeId HistoryReadDetails_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(642));

  public static final NodeId HistoryReadDetails_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(643));

  public static final NodeId ReadEventDetails = new NodeId(UShort.MIN, uint(644));

  public static final NodeId ReadEventDetails_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(645));

  public static final NodeId ReadEventDetails_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(646));

  public static final NodeId ReadRawModifiedDetails = new NodeId(UShort.MIN, uint(647));

  public static final NodeId ReadRawModifiedDetails_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(648));

  public static final NodeId ReadRawModifiedDetails_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(649));

  public static final NodeId ReadProcessedDetails = new NodeId(UShort.MIN, uint(650));

  public static final NodeId ReadProcessedDetails_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(651));

  public static final NodeId ReadProcessedDetails_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(652));

  public static final NodeId ReadAtTimeDetails = new NodeId(UShort.MIN, uint(653));

  public static final NodeId ReadAtTimeDetails_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(654));

  public static final NodeId ReadAtTimeDetails_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(655));

  public static final NodeId HistoryData = new NodeId(UShort.MIN, uint(656));

  public static final NodeId HistoryData_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(657));

  public static final NodeId HistoryData_Encoding_DefaultBinary = new NodeId(UShort.MIN, uint(658));

  public static final NodeId HistoryEvent = new NodeId(UShort.MIN, uint(659));

  public static final NodeId HistoryEvent_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(660));

  public static final NodeId HistoryEvent_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(661));

  public static final NodeId HistoryReadRequest = new NodeId(UShort.MIN, uint(662));

  public static final NodeId HistoryReadRequest_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(663));

  public static final NodeId HistoryReadRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(664));

  public static final NodeId HistoryReadResponse = new NodeId(UShort.MIN, uint(665));

  public static final NodeId HistoryReadResponse_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(666));

  public static final NodeId HistoryReadResponse_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(667));

  public static final NodeId WriteValue = new NodeId(UShort.MIN, uint(668));

  public static final NodeId WriteValue_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(669));

  public static final NodeId WriteValue_Encoding_DefaultBinary = new NodeId(UShort.MIN, uint(670));

  public static final NodeId WriteRequest = new NodeId(UShort.MIN, uint(671));

  public static final NodeId WriteRequest_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(672));

  public static final NodeId WriteRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(673));

  public static final NodeId WriteResponse = new NodeId(UShort.MIN, uint(674));

  public static final NodeId WriteResponse_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(675));

  public static final NodeId WriteResponse_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(676));

  public static final NodeId HistoryUpdateDetails = new NodeId(UShort.MIN, uint(677));

  public static final NodeId HistoryUpdateDetails_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(678));

  public static final NodeId HistoryUpdateDetails_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(679));

  public static final NodeId UpdateDataDetails = new NodeId(UShort.MIN, uint(680));

  public static final NodeId UpdateDataDetails_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(681));

  public static final NodeId UpdateDataDetails_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(682));

  public static final NodeId UpdateEventDetails = new NodeId(UShort.MIN, uint(683));

  public static final NodeId UpdateEventDetails_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(684));

  public static final NodeId UpdateEventDetails_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(685));

  public static final NodeId DeleteRawModifiedDetails = new NodeId(UShort.MIN, uint(686));

  public static final NodeId DeleteRawModifiedDetails_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(687));

  public static final NodeId DeleteRawModifiedDetails_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(688));

  public static final NodeId DeleteAtTimeDetails = new NodeId(UShort.MIN, uint(689));

  public static final NodeId DeleteAtTimeDetails_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(690));

  public static final NodeId DeleteAtTimeDetails_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(691));

  public static final NodeId DeleteEventDetails = new NodeId(UShort.MIN, uint(692));

  public static final NodeId DeleteEventDetails_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(693));

  public static final NodeId DeleteEventDetails_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(694));

  public static final NodeId HistoryUpdateResult = new NodeId(UShort.MIN, uint(695));

  public static final NodeId HistoryUpdateResult_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(696));

  public static final NodeId HistoryUpdateResult_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(697));

  public static final NodeId HistoryUpdateRequest = new NodeId(UShort.MIN, uint(698));

  public static final NodeId HistoryUpdateRequest_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(699));

  public static final NodeId HistoryUpdateRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(700));

  public static final NodeId HistoryUpdateResponse = new NodeId(UShort.MIN, uint(701));

  public static final NodeId HistoryUpdateResponse_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(702));

  public static final NodeId HistoryUpdateResponse_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(703));

  public static final NodeId CallMethodRequest = new NodeId(UShort.MIN, uint(704));

  public static final NodeId CallMethodRequest_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(705));

  public static final NodeId CallMethodRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(706));

  public static final NodeId CallMethodResult = new NodeId(UShort.MIN, uint(707));

  public static final NodeId CallMethodResult_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(708));

  public static final NodeId CallMethodResult_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(709));

  public static final NodeId CallRequest = new NodeId(UShort.MIN, uint(710));

  public static final NodeId CallRequest_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(711));

  public static final NodeId CallRequest_Encoding_DefaultBinary = new NodeId(UShort.MIN, uint(712));

  public static final NodeId CallResponse = new NodeId(UShort.MIN, uint(713));

  public static final NodeId CallResponse_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(714));

  public static final NodeId CallResponse_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(715));

  public static final NodeId MonitoringMode = new NodeId(UShort.MIN, uint(716));

  public static final NodeId DataChangeTrigger = new NodeId(UShort.MIN, uint(717));

  public static final NodeId DeadbandType = new NodeId(UShort.MIN, uint(718));

  public static final NodeId MonitoringFilter = new NodeId(UShort.MIN, uint(719));

  public static final NodeId MonitoringFilter_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(720));

  public static final NodeId MonitoringFilter_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(721));

  public static final NodeId DataChangeFilter = new NodeId(UShort.MIN, uint(722));

  public static final NodeId DataChangeFilter_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(723));

  public static final NodeId DataChangeFilter_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(724));

  public static final NodeId EventFilter = new NodeId(UShort.MIN, uint(725));

  public static final NodeId EventFilter_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(726));

  public static final NodeId EventFilter_Encoding_DefaultBinary = new NodeId(UShort.MIN, uint(727));

  public static final NodeId AggregateFilter = new NodeId(UShort.MIN, uint(728));

  public static final NodeId AggregateFilter_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(729));

  public static final NodeId AggregateFilter_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(730));

  public static final NodeId MonitoringFilterResult = new NodeId(UShort.MIN, uint(731));

  public static final NodeId MonitoringFilterResult_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(732));

  public static final NodeId MonitoringFilterResult_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(733));

  public static final NodeId EventFilterResult = new NodeId(UShort.MIN, uint(734));

  public static final NodeId EventFilterResult_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(735));

  public static final NodeId EventFilterResult_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(736));

  public static final NodeId AggregateFilterResult = new NodeId(UShort.MIN, uint(737));

  public static final NodeId AggregateFilterResult_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(738));

  public static final NodeId AggregateFilterResult_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(739));

  public static final NodeId MonitoringParameters = new NodeId(UShort.MIN, uint(740));

  public static final NodeId MonitoringParameters_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(741));

  public static final NodeId MonitoringParameters_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(742));

  public static final NodeId MonitoredItemCreateRequest = new NodeId(UShort.MIN, uint(743));

  public static final NodeId MonitoredItemCreateRequest_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(744));

  public static final NodeId MonitoredItemCreateRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(745));

  public static final NodeId MonitoredItemCreateResult = new NodeId(UShort.MIN, uint(746));

  public static final NodeId MonitoredItemCreateResult_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(747));

  public static final NodeId MonitoredItemCreateResult_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(748));

  public static final NodeId CreateMonitoredItemsRequest = new NodeId(UShort.MIN, uint(749));

  public static final NodeId CreateMonitoredItemsRequest_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(750));

  public static final NodeId CreateMonitoredItemsRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(751));

  public static final NodeId CreateMonitoredItemsResponse = new NodeId(UShort.MIN, uint(752));

  public static final NodeId CreateMonitoredItemsResponse_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(753));

  public static final NodeId CreateMonitoredItemsResponse_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(754));

  public static final NodeId MonitoredItemModifyRequest = new NodeId(UShort.MIN, uint(755));

  public static final NodeId MonitoredItemModifyRequest_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(756));

  public static final NodeId MonitoredItemModifyRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(757));

  public static final NodeId MonitoredItemModifyResult = new NodeId(UShort.MIN, uint(758));

  public static final NodeId MonitoredItemModifyResult_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(759));

  public static final NodeId MonitoredItemModifyResult_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(760));

  public static final NodeId ModifyMonitoredItemsRequest = new NodeId(UShort.MIN, uint(761));

  public static final NodeId ModifyMonitoredItemsRequest_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(762));

  public static final NodeId ModifyMonitoredItemsRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(763));

  public static final NodeId ModifyMonitoredItemsResponse = new NodeId(UShort.MIN, uint(764));

  public static final NodeId ModifyMonitoredItemsResponse_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(765));

  public static final NodeId ModifyMonitoredItemsResponse_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(766));

  public static final NodeId SetMonitoringModeRequest = new NodeId(UShort.MIN, uint(767));

  public static final NodeId SetMonitoringModeRequest_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(768));

  public static final NodeId SetMonitoringModeRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(769));

  public static final NodeId SetMonitoringModeResponse = new NodeId(UShort.MIN, uint(770));

  public static final NodeId SetMonitoringModeResponse_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(771));

  public static final NodeId SetMonitoringModeResponse_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(772));

  public static final NodeId SetTriggeringRequest = new NodeId(UShort.MIN, uint(773));

  public static final NodeId SetTriggeringRequest_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(774));

  public static final NodeId SetTriggeringRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(775));

  public static final NodeId SetTriggeringResponse = new NodeId(UShort.MIN, uint(776));

  public static final NodeId SetTriggeringResponse_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(777));

  public static final NodeId SetTriggeringResponse_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(778));

  public static final NodeId DeleteMonitoredItemsRequest = new NodeId(UShort.MIN, uint(779));

  public static final NodeId DeleteMonitoredItemsRequest_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(780));

  public static final NodeId DeleteMonitoredItemsRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(781));

  public static final NodeId DeleteMonitoredItemsResponse = new NodeId(UShort.MIN, uint(782));

  public static final NodeId DeleteMonitoredItemsResponse_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(783));

  public static final NodeId DeleteMonitoredItemsResponse_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(784));

  public static final NodeId CreateSubscriptionRequest = new NodeId(UShort.MIN, uint(785));

  public static final NodeId CreateSubscriptionRequest_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(786));

  public static final NodeId CreateSubscriptionRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(787));

  public static final NodeId CreateSubscriptionResponse = new NodeId(UShort.MIN, uint(788));

  public static final NodeId CreateSubscriptionResponse_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(789));

  public static final NodeId CreateSubscriptionResponse_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(790));

  public static final NodeId ModifySubscriptionRequest = new NodeId(UShort.MIN, uint(791));

  public static final NodeId ModifySubscriptionRequest_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(792));

  public static final NodeId ModifySubscriptionRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(793));

  public static final NodeId ModifySubscriptionResponse = new NodeId(UShort.MIN, uint(794));

  public static final NodeId ModifySubscriptionResponse_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(795));

  public static final NodeId ModifySubscriptionResponse_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(796));

  public static final NodeId SetPublishingModeRequest = new NodeId(UShort.MIN, uint(797));

  public static final NodeId SetPublishingModeRequest_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(798));

  public static final NodeId SetPublishingModeRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(799));

  public static final NodeId SetPublishingModeResponse = new NodeId(UShort.MIN, uint(800));

  public static final NodeId SetPublishingModeResponse_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(801));

  public static final NodeId SetPublishingModeResponse_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(802));

  public static final NodeId NotificationMessage = new NodeId(UShort.MIN, uint(803));

  public static final NodeId NotificationMessage_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(804));

  public static final NodeId NotificationMessage_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(805));

  public static final NodeId MonitoredItemNotification = new NodeId(UShort.MIN, uint(806));

  public static final NodeId MonitoredItemNotification_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(807));

  public static final NodeId MonitoredItemNotification_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(808));

  public static final NodeId DataChangeNotification = new NodeId(UShort.MIN, uint(809));

  public static final NodeId DataChangeNotification_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(810));

  public static final NodeId DataChangeNotification_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(811));

  public static final NodeId StatusChangeNotification = new NodeId(UShort.MIN, uint(818));

  public static final NodeId StatusChangeNotification_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(819));

  public static final NodeId StatusChangeNotification_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(820));

  public static final NodeId SubscriptionAcknowledgement = new NodeId(UShort.MIN, uint(821));

  public static final NodeId SubscriptionAcknowledgement_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(822));

  public static final NodeId SubscriptionAcknowledgement_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(823));

  public static final NodeId PublishRequest = new NodeId(UShort.MIN, uint(824));

  public static final NodeId PublishRequest_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(825));

  public static final NodeId PublishRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(826));

  public static final NodeId PublishResponse = new NodeId(UShort.MIN, uint(827));

  public static final NodeId PublishResponse_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(828));

  public static final NodeId PublishResponse_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(829));

  public static final NodeId RepublishRequest = new NodeId(UShort.MIN, uint(830));

  public static final NodeId RepublishRequest_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(831));

  public static final NodeId RepublishRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(832));

  public static final NodeId RepublishResponse = new NodeId(UShort.MIN, uint(833));

  public static final NodeId RepublishResponse_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(834));

  public static final NodeId RepublishResponse_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(835));

  public static final NodeId TransferResult = new NodeId(UShort.MIN, uint(836));

  public static final NodeId TransferResult_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(837));

  public static final NodeId TransferResult_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(838));

  public static final NodeId TransferSubscriptionsRequest = new NodeId(UShort.MIN, uint(839));

  public static final NodeId TransferSubscriptionsRequest_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(840));

  public static final NodeId TransferSubscriptionsRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(841));

  public static final NodeId TransferSubscriptionsResponse = new NodeId(UShort.MIN, uint(842));

  public static final NodeId TransferSubscriptionsResponse_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(843));

  public static final NodeId TransferSubscriptionsResponse_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(844));

  public static final NodeId DeleteSubscriptionsRequest = new NodeId(UShort.MIN, uint(845));

  public static final NodeId DeleteSubscriptionsRequest_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(846));

  public static final NodeId DeleteSubscriptionsRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(847));

  public static final NodeId DeleteSubscriptionsResponse = new NodeId(UShort.MIN, uint(848));

  public static final NodeId DeleteSubscriptionsResponse_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(849));

  public static final NodeId DeleteSubscriptionsResponse_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(850));

  public static final NodeId RedundancySupport = new NodeId(UShort.MIN, uint(851));

  public static final NodeId ServerState = new NodeId(UShort.MIN, uint(852));

  public static final NodeId RedundantServerDataType = new NodeId(UShort.MIN, uint(853));

  public static final NodeId RedundantServerDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(854));

  public static final NodeId RedundantServerDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(855));

  public static final NodeId SamplingIntervalDiagnosticsDataType =
      new NodeId(UShort.MIN, uint(856));

  public static final NodeId SamplingIntervalDiagnosticsDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(857));

  public static final NodeId SamplingIntervalDiagnosticsDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(858));

  public static final NodeId ServerDiagnosticsSummaryDataType = new NodeId(UShort.MIN, uint(859));

  public static final NodeId ServerDiagnosticsSummaryDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(860));

  public static final NodeId ServerDiagnosticsSummaryDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(861));

  public static final NodeId ServerStatusDataType = new NodeId(UShort.MIN, uint(862));

  public static final NodeId ServerStatusDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(863));

  public static final NodeId ServerStatusDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(864));

  public static final NodeId SessionDiagnosticsDataType = new NodeId(UShort.MIN, uint(865));

  public static final NodeId SessionDiagnosticsDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(866));

  public static final NodeId SessionDiagnosticsDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(867));

  public static final NodeId SessionSecurityDiagnosticsDataType = new NodeId(UShort.MIN, uint(868));

  public static final NodeId SessionSecurityDiagnosticsDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(869));

  public static final NodeId SessionSecurityDiagnosticsDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(870));

  public static final NodeId ServiceCounterDataType = new NodeId(UShort.MIN, uint(871));

  public static final NodeId ServiceCounterDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(872));

  public static final NodeId ServiceCounterDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(873));

  public static final NodeId SubscriptionDiagnosticsDataType = new NodeId(UShort.MIN, uint(874));

  public static final NodeId SubscriptionDiagnosticsDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(875));

  public static final NodeId SubscriptionDiagnosticsDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(876));

  public static final NodeId ModelChangeStructureDataType = new NodeId(UShort.MIN, uint(877));

  public static final NodeId ModelChangeStructureDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(878));

  public static final NodeId ModelChangeStructureDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(879));

  public static final NodeId Range = new NodeId(UShort.MIN, uint(884));

  public static final NodeId Range_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(885));

  public static final NodeId Range_Encoding_DefaultBinary = new NodeId(UShort.MIN, uint(886));

  public static final NodeId EUInformation = new NodeId(UShort.MIN, uint(887));

  public static final NodeId EUInformation_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(888));

  public static final NodeId EUInformation_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(889));

  public static final NodeId ExceptionDeviationFormat = new NodeId(UShort.MIN, uint(890));

  public static final NodeId Annotation = new NodeId(UShort.MIN, uint(891));

  public static final NodeId Annotation_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(892));

  public static final NodeId Annotation_Encoding_DefaultBinary = new NodeId(UShort.MIN, uint(893));

  public static final NodeId ProgramDiagnosticDataType = new NodeId(UShort.MIN, uint(894));

  public static final NodeId ProgramDiagnosticDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(895));

  public static final NodeId ProgramDiagnosticDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(896));

  public static final NodeId SemanticChangeStructureDataType = new NodeId(UShort.MIN, uint(897));

  public static final NodeId SemanticChangeStructureDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(898));

  public static final NodeId SemanticChangeStructureDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(899));

  public static final NodeId EventNotificationList = new NodeId(UShort.MIN, uint(914));

  public static final NodeId EventNotificationList_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(915));

  public static final NodeId EventNotificationList_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(916));

  public static final NodeId EventFieldList = new NodeId(UShort.MIN, uint(917));

  public static final NodeId EventFieldList_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(918));

  public static final NodeId EventFieldList_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(919));

  public static final NodeId HistoryEventFieldList = new NodeId(UShort.MIN, uint(920));

  public static final NodeId HistoryEventFieldList_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(921));

  public static final NodeId HistoryEventFieldList_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(922));

  public static final NodeId IssuedIdentityToken = new NodeId(UShort.MIN, uint(938));

  public static final NodeId IssuedIdentityToken_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(939));

  public static final NodeId IssuedIdentityToken_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(940));

  public static final NodeId NotificationData = new NodeId(UShort.MIN, uint(945));

  public static final NodeId NotificationData_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(946));

  public static final NodeId NotificationData_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(947));

  public static final NodeId AggregateConfiguration = new NodeId(UShort.MIN, uint(948));

  public static final NodeId AggregateConfiguration_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(949));

  public static final NodeId AggregateConfiguration_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(950));

  public static final NodeId ImageBMP = new NodeId(UShort.MIN, uint(2000));

  public static final NodeId ImageGIF = new NodeId(UShort.MIN, uint(2001));

  public static final NodeId ImageJPG = new NodeId(UShort.MIN, uint(2002));

  public static final NodeId ImagePNG = new NodeId(UShort.MIN, uint(2003));

  public static final NodeId ServerType = new NodeId(UShort.MIN, uint(2004));

  public static final NodeId ServerType_ServerArray = new NodeId(UShort.MIN, uint(2005));

  public static final NodeId ServerType_NamespaceArray = new NodeId(UShort.MIN, uint(2006));

  public static final NodeId ServerType_ServerStatus = new NodeId(UShort.MIN, uint(2007));

  public static final NodeId ServerType_ServiceLevel = new NodeId(UShort.MIN, uint(2008));

  public static final NodeId ServerType_ServerCapabilities = new NodeId(UShort.MIN, uint(2009));

  public static final NodeId ServerType_ServerDiagnostics = new NodeId(UShort.MIN, uint(2010));

  public static final NodeId ServerType_VendorServerInfo = new NodeId(UShort.MIN, uint(2011));

  public static final NodeId ServerType_ServerRedundancy = new NodeId(UShort.MIN, uint(2012));

  public static final NodeId ServerCapabilitiesType = new NodeId(UShort.MIN, uint(2013));

  public static final NodeId ServerCapabilitiesType_ServerProfileArray =
      new NodeId(UShort.MIN, uint(2014));

  public static final NodeId ServerCapabilitiesType_LocaleIdArray =
      new NodeId(UShort.MIN, uint(2016));

  public static final NodeId ServerCapabilitiesType_MinSupportedSampleRate =
      new NodeId(UShort.MIN, uint(2017));

  public static final NodeId ServerCapabilitiesType_ModellingRules =
      new NodeId(UShort.MIN, uint(2019));

  public static final NodeId ServerDiagnosticsType = new NodeId(UShort.MIN, uint(2020));

  public static final NodeId ServerDiagnosticsType_ServerDiagnosticsSummary =
      new NodeId(UShort.MIN, uint(2021));

  public static final NodeId ServerDiagnosticsType_SamplingIntervalDiagnosticsArray =
      new NodeId(UShort.MIN, uint(2022));

  public static final NodeId ServerDiagnosticsType_SubscriptionDiagnosticsArray =
      new NodeId(UShort.MIN, uint(2023));

  public static final NodeId ServerDiagnosticsType_EnabledFlag = new NodeId(UShort.MIN, uint(2025));

  public static final NodeId SessionsDiagnosticsSummaryType = new NodeId(UShort.MIN, uint(2026));

  public static final NodeId SessionsDiagnosticsSummaryType_SessionDiagnosticsArray =
      new NodeId(UShort.MIN, uint(2027));

  public static final NodeId SessionsDiagnosticsSummaryType_SessionSecurityDiagnosticsArray =
      new NodeId(UShort.MIN, uint(2028));

  public static final NodeId SessionDiagnosticsObjectType = new NodeId(UShort.MIN, uint(2029));

  public static final NodeId SessionDiagnosticsObjectType_SessionDiagnostics =
      new NodeId(UShort.MIN, uint(2030));

  public static final NodeId SessionDiagnosticsObjectType_SessionSecurityDiagnostics =
      new NodeId(UShort.MIN, uint(2031));

  public static final NodeId SessionDiagnosticsObjectType_SubscriptionDiagnosticsArray =
      new NodeId(UShort.MIN, uint(2032));

  public static final NodeId VendorServerInfoType = new NodeId(UShort.MIN, uint(2033));

  public static final NodeId ServerRedundancyType = new NodeId(UShort.MIN, uint(2034));

  public static final NodeId ServerRedundancyType_RedundancySupport =
      new NodeId(UShort.MIN, uint(2035));

  public static final NodeId TransparentRedundancyType = new NodeId(UShort.MIN, uint(2036));

  public static final NodeId TransparentRedundancyType_CurrentServerId =
      new NodeId(UShort.MIN, uint(2037));

  public static final NodeId TransparentRedundancyType_RedundantServerArray =
      new NodeId(UShort.MIN, uint(2038));

  public static final NodeId NonTransparentRedundancyType = new NodeId(UShort.MIN, uint(2039));

  public static final NodeId NonTransparentRedundancyType_ServerUriArray =
      new NodeId(UShort.MIN, uint(2040));

  public static final NodeId BaseEventType = new NodeId(UShort.MIN, uint(2041));

  public static final NodeId BaseEventType_EventId = new NodeId(UShort.MIN, uint(2042));

  public static final NodeId BaseEventType_EventType = new NodeId(UShort.MIN, uint(2043));

  public static final NodeId BaseEventType_SourceNode = new NodeId(UShort.MIN, uint(2044));

  public static final NodeId BaseEventType_SourceName = new NodeId(UShort.MIN, uint(2045));

  public static final NodeId BaseEventType_Time = new NodeId(UShort.MIN, uint(2046));

  public static final NodeId BaseEventType_ReceiveTime = new NodeId(UShort.MIN, uint(2047));

  public static final NodeId BaseEventType_Message = new NodeId(UShort.MIN, uint(2050));

  public static final NodeId BaseEventType_Severity = new NodeId(UShort.MIN, uint(2051));

  public static final NodeId AuditEventType = new NodeId(UShort.MIN, uint(2052));

  public static final NodeId AuditEventType_ActionTimeStamp = new NodeId(UShort.MIN, uint(2053));

  public static final NodeId AuditEventType_Status = new NodeId(UShort.MIN, uint(2054));

  public static final NodeId AuditEventType_ServerId = new NodeId(UShort.MIN, uint(2055));

  public static final NodeId AuditEventType_ClientAuditEntryId = new NodeId(UShort.MIN, uint(2056));

  public static final NodeId AuditEventType_ClientUserId = new NodeId(UShort.MIN, uint(2057));

  public static final NodeId AuditSecurityEventType = new NodeId(UShort.MIN, uint(2058));

  public static final NodeId AuditChannelEventType = new NodeId(UShort.MIN, uint(2059));

  public static final NodeId AuditOpenSecureChannelEventType = new NodeId(UShort.MIN, uint(2060));

  public static final NodeId AuditOpenSecureChannelEventType_ClientCertificate =
      new NodeId(UShort.MIN, uint(2061));

  public static final NodeId AuditOpenSecureChannelEventType_RequestType =
      new NodeId(UShort.MIN, uint(2062));

  public static final NodeId AuditOpenSecureChannelEventType_SecurityPolicyUri =
      new NodeId(UShort.MIN, uint(2063));

  public static final NodeId AuditOpenSecureChannelEventType_SecurityMode =
      new NodeId(UShort.MIN, uint(2065));

  public static final NodeId AuditOpenSecureChannelEventType_RequestedLifetime =
      new NodeId(UShort.MIN, uint(2066));

  public static final NodeId AuditSessionEventType = new NodeId(UShort.MIN, uint(2069));

  public static final NodeId AuditSessionEventType_SessionId = new NodeId(UShort.MIN, uint(2070));

  public static final NodeId AuditCreateSessionEventType = new NodeId(UShort.MIN, uint(2071));

  public static final NodeId AuditCreateSessionEventType_SecureChannelId =
      new NodeId(UShort.MIN, uint(2072));

  public static final NodeId AuditCreateSessionEventType_ClientCertificate =
      new NodeId(UShort.MIN, uint(2073));

  public static final NodeId AuditCreateSessionEventType_RevisedSessionTimeout =
      new NodeId(UShort.MIN, uint(2074));

  public static final NodeId AuditActivateSessionEventType = new NodeId(UShort.MIN, uint(2075));

  public static final NodeId AuditActivateSessionEventType_ClientSoftwareCertificates =
      new NodeId(UShort.MIN, uint(2076));

  public static final NodeId AuditActivateSessionEventType_UserIdentityToken =
      new NodeId(UShort.MIN, uint(2077));

  public static final NodeId AuditCancelEventType = new NodeId(UShort.MIN, uint(2078));

  public static final NodeId AuditCancelEventType_RequestHandle =
      new NodeId(UShort.MIN, uint(2079));

  public static final NodeId AuditCertificateEventType = new NodeId(UShort.MIN, uint(2080));

  public static final NodeId AuditCertificateEventType_Certificate =
      new NodeId(UShort.MIN, uint(2081));

  public static final NodeId AuditCertificateDataMismatchEventType =
      new NodeId(UShort.MIN, uint(2082));

  public static final NodeId AuditCertificateDataMismatchEventType_InvalidHostname =
      new NodeId(UShort.MIN, uint(2083));

  public static final NodeId AuditCertificateDataMismatchEventType_InvalidUri =
      new NodeId(UShort.MIN, uint(2084));

  public static final NodeId AuditCertificateExpiredEventType = new NodeId(UShort.MIN, uint(2085));

  public static final NodeId AuditCertificateInvalidEventType = new NodeId(UShort.MIN, uint(2086));

  public static final NodeId AuditCertificateUntrustedEventType =
      new NodeId(UShort.MIN, uint(2087));

  public static final NodeId AuditCertificateRevokedEventType = new NodeId(UShort.MIN, uint(2088));

  public static final NodeId AuditCertificateMismatchEventType = new NodeId(UShort.MIN, uint(2089));

  public static final NodeId AuditNodeManagementEventType = new NodeId(UShort.MIN, uint(2090));

  public static final NodeId AuditAddNodesEventType = new NodeId(UShort.MIN, uint(2091));

  public static final NodeId AuditAddNodesEventType_NodesToAdd = new NodeId(UShort.MIN, uint(2092));

  public static final NodeId AuditDeleteNodesEventType = new NodeId(UShort.MIN, uint(2093));

  public static final NodeId AuditDeleteNodesEventType_NodesToDelete =
      new NodeId(UShort.MIN, uint(2094));

  public static final NodeId AuditAddReferencesEventType = new NodeId(UShort.MIN, uint(2095));

  public static final NodeId AuditAddReferencesEventType_ReferencesToAdd =
      new NodeId(UShort.MIN, uint(2096));

  public static final NodeId AuditDeleteReferencesEventType = new NodeId(UShort.MIN, uint(2097));

  public static final NodeId AuditDeleteReferencesEventType_ReferencesToDelete =
      new NodeId(UShort.MIN, uint(2098));

  public static final NodeId AuditUpdateEventType = new NodeId(UShort.MIN, uint(2099));

  public static final NodeId AuditWriteUpdateEventType = new NodeId(UShort.MIN, uint(2100));

  public static final NodeId AuditWriteUpdateEventType_IndexRange =
      new NodeId(UShort.MIN, uint(2101));

  public static final NodeId AuditWriteUpdateEventType_OldValue =
      new NodeId(UShort.MIN, uint(2102));

  public static final NodeId AuditWriteUpdateEventType_NewValue =
      new NodeId(UShort.MIN, uint(2103));

  public static final NodeId AuditHistoryUpdateEventType = new NodeId(UShort.MIN, uint(2104));

  public static final NodeId AuditUpdateMethodEventType = new NodeId(UShort.MIN, uint(2127));

  public static final NodeId AuditUpdateMethodEventType_MethodId =
      new NodeId(UShort.MIN, uint(2128));

  public static final NodeId AuditUpdateMethodEventType_InputArguments =
      new NodeId(UShort.MIN, uint(2129));

  public static final NodeId SystemEventType = new NodeId(UShort.MIN, uint(2130));

  public static final NodeId DeviceFailureEventType = new NodeId(UShort.MIN, uint(2131));

  public static final NodeId BaseModelChangeEventType = new NodeId(UShort.MIN, uint(2132));

  public static final NodeId GeneralModelChangeEventType = new NodeId(UShort.MIN, uint(2133));

  public static final NodeId GeneralModelChangeEventType_Changes =
      new NodeId(UShort.MIN, uint(2134));

  public static final NodeId ServerVendorCapabilityType = new NodeId(UShort.MIN, uint(2137));

  public static final NodeId ServerStatusType = new NodeId(UShort.MIN, uint(2138));

  public static final NodeId ServerStatusType_StartTime = new NodeId(UShort.MIN, uint(2139));

  public static final NodeId ServerStatusType_CurrentTime = new NodeId(UShort.MIN, uint(2140));

  public static final NodeId ServerStatusType_State = new NodeId(UShort.MIN, uint(2141));

  public static final NodeId ServerStatusType_BuildInfo = new NodeId(UShort.MIN, uint(2142));

  public static final NodeId ServerDiagnosticsSummaryType = new NodeId(UShort.MIN, uint(2150));

  public static final NodeId ServerDiagnosticsSummaryType_ServerViewCount =
      new NodeId(UShort.MIN, uint(2151));

  public static final NodeId ServerDiagnosticsSummaryType_CurrentSessionCount =
      new NodeId(UShort.MIN, uint(2152));

  public static final NodeId ServerDiagnosticsSummaryType_CumulatedSessionCount =
      new NodeId(UShort.MIN, uint(2153));

  public static final NodeId ServerDiagnosticsSummaryType_SecurityRejectedSessionCount =
      new NodeId(UShort.MIN, uint(2154));

  public static final NodeId ServerDiagnosticsSummaryType_RejectedSessionCount =
      new NodeId(UShort.MIN, uint(2155));

  public static final NodeId ServerDiagnosticsSummaryType_SessionTimeoutCount =
      new NodeId(UShort.MIN, uint(2156));

  public static final NodeId ServerDiagnosticsSummaryType_SessionAbortCount =
      new NodeId(UShort.MIN, uint(2157));

  public static final NodeId ServerDiagnosticsSummaryType_PublishingIntervalCount =
      new NodeId(UShort.MIN, uint(2159));

  public static final NodeId ServerDiagnosticsSummaryType_CurrentSubscriptionCount =
      new NodeId(UShort.MIN, uint(2160));

  public static final NodeId ServerDiagnosticsSummaryType_CumulatedSubscriptionCount =
      new NodeId(UShort.MIN, uint(2161));

  public static final NodeId ServerDiagnosticsSummaryType_SecurityRejectedRequestsCount =
      new NodeId(UShort.MIN, uint(2162));

  public static final NodeId ServerDiagnosticsSummaryType_RejectedRequestsCount =
      new NodeId(UShort.MIN, uint(2163));

  public static final NodeId SamplingIntervalDiagnosticsArrayType =
      new NodeId(UShort.MIN, uint(2164));

  public static final NodeId SamplingIntervalDiagnosticsType = new NodeId(UShort.MIN, uint(2165));

  public static final NodeId SamplingIntervalDiagnosticsType_SamplingInterval =
      new NodeId(UShort.MIN, uint(2166));

  public static final NodeId SubscriptionDiagnosticsArrayType = new NodeId(UShort.MIN, uint(2171));

  public static final NodeId SubscriptionDiagnosticsType = new NodeId(UShort.MIN, uint(2172));

  public static final NodeId SubscriptionDiagnosticsType_SessionId =
      new NodeId(UShort.MIN, uint(2173));

  public static final NodeId SubscriptionDiagnosticsType_SubscriptionId =
      new NodeId(UShort.MIN, uint(2174));

  public static final NodeId SubscriptionDiagnosticsType_Priority =
      new NodeId(UShort.MIN, uint(2175));

  public static final NodeId SubscriptionDiagnosticsType_PublishingInterval =
      new NodeId(UShort.MIN, uint(2176));

  public static final NodeId SubscriptionDiagnosticsType_MaxKeepAliveCount =
      new NodeId(UShort.MIN, uint(2177));

  public static final NodeId SubscriptionDiagnosticsType_MaxNotificationsPerPublish =
      new NodeId(UShort.MIN, uint(2179));

  public static final NodeId SubscriptionDiagnosticsType_PublishingEnabled =
      new NodeId(UShort.MIN, uint(2180));

  public static final NodeId SubscriptionDiagnosticsType_ModifyCount =
      new NodeId(UShort.MIN, uint(2181));

  public static final NodeId SubscriptionDiagnosticsType_EnableCount =
      new NodeId(UShort.MIN, uint(2182));

  public static final NodeId SubscriptionDiagnosticsType_DisableCount =
      new NodeId(UShort.MIN, uint(2183));

  public static final NodeId SubscriptionDiagnosticsType_RepublishRequestCount =
      new NodeId(UShort.MIN, uint(2184));

  public static final NodeId SubscriptionDiagnosticsType_RepublishMessageRequestCount =
      new NodeId(UShort.MIN, uint(2185));

  public static final NodeId SubscriptionDiagnosticsType_RepublishMessageCount =
      new NodeId(UShort.MIN, uint(2186));

  public static final NodeId SubscriptionDiagnosticsType_TransferRequestCount =
      new NodeId(UShort.MIN, uint(2187));

  public static final NodeId SubscriptionDiagnosticsType_TransferredToAltClientCount =
      new NodeId(UShort.MIN, uint(2188));

  public static final NodeId SubscriptionDiagnosticsType_TransferredToSameClientCount =
      new NodeId(UShort.MIN, uint(2189));

  public static final NodeId SubscriptionDiagnosticsType_PublishRequestCount =
      new NodeId(UShort.MIN, uint(2190));

  public static final NodeId SubscriptionDiagnosticsType_DataChangeNotificationsCount =
      new NodeId(UShort.MIN, uint(2191));

  public static final NodeId SubscriptionDiagnosticsType_NotificationsCount =
      new NodeId(UShort.MIN, uint(2193));

  public static final NodeId SessionDiagnosticsArrayType = new NodeId(UShort.MIN, uint(2196));

  public static final NodeId SessionDiagnosticsVariableType = new NodeId(UShort.MIN, uint(2197));

  public static final NodeId SessionDiagnosticsVariableType_SessionId =
      new NodeId(UShort.MIN, uint(2198));

  public static final NodeId SessionDiagnosticsVariableType_SessionName =
      new NodeId(UShort.MIN, uint(2199));

  public static final NodeId SessionDiagnosticsVariableType_ClientDescription =
      new NodeId(UShort.MIN, uint(2200));

  public static final NodeId SessionDiagnosticsVariableType_ServerUri =
      new NodeId(UShort.MIN, uint(2201));

  public static final NodeId SessionDiagnosticsVariableType_EndpointUrl =
      new NodeId(UShort.MIN, uint(2202));

  public static final NodeId SessionDiagnosticsVariableType_LocaleIds =
      new NodeId(UShort.MIN, uint(2203));

  public static final NodeId SessionDiagnosticsVariableType_ActualSessionTimeout =
      new NodeId(UShort.MIN, uint(2204));

  public static final NodeId SessionDiagnosticsVariableType_ClientConnectionTime =
      new NodeId(UShort.MIN, uint(2205));

  public static final NodeId SessionDiagnosticsVariableType_ClientLastContactTime =
      new NodeId(UShort.MIN, uint(2206));

  public static final NodeId SessionDiagnosticsVariableType_CurrentSubscriptionsCount =
      new NodeId(UShort.MIN, uint(2207));

  public static final NodeId SessionDiagnosticsVariableType_CurrentMonitoredItemsCount =
      new NodeId(UShort.MIN, uint(2208));

  public static final NodeId SessionDiagnosticsVariableType_CurrentPublishRequestsInQueue =
      new NodeId(UShort.MIN, uint(2209));

  public static final NodeId SessionDiagnosticsVariableType_ReadCount =
      new NodeId(UShort.MIN, uint(2217));

  public static final NodeId SessionDiagnosticsVariableType_HistoryReadCount =
      new NodeId(UShort.MIN, uint(2218));

  public static final NodeId SessionDiagnosticsVariableType_WriteCount =
      new NodeId(UShort.MIN, uint(2219));

  public static final NodeId SessionDiagnosticsVariableType_HistoryUpdateCount =
      new NodeId(UShort.MIN, uint(2220));

  public static final NodeId SessionDiagnosticsVariableType_CallCount =
      new NodeId(UShort.MIN, uint(2221));

  public static final NodeId SessionDiagnosticsVariableType_CreateMonitoredItemsCount =
      new NodeId(UShort.MIN, uint(2222));

  public static final NodeId SessionDiagnosticsVariableType_ModifyMonitoredItemsCount =
      new NodeId(UShort.MIN, uint(2223));

  public static final NodeId SessionDiagnosticsVariableType_SetMonitoringModeCount =
      new NodeId(UShort.MIN, uint(2224));

  public static final NodeId SessionDiagnosticsVariableType_SetTriggeringCount =
      new NodeId(UShort.MIN, uint(2225));

  public static final NodeId SessionDiagnosticsVariableType_DeleteMonitoredItemsCount =
      new NodeId(UShort.MIN, uint(2226));

  public static final NodeId SessionDiagnosticsVariableType_CreateSubscriptionCount =
      new NodeId(UShort.MIN, uint(2227));

  public static final NodeId SessionDiagnosticsVariableType_ModifySubscriptionCount =
      new NodeId(UShort.MIN, uint(2228));

  public static final NodeId SessionDiagnosticsVariableType_SetPublishingModeCount =
      new NodeId(UShort.MIN, uint(2229));

  public static final NodeId SessionDiagnosticsVariableType_PublishCount =
      new NodeId(UShort.MIN, uint(2230));

  public static final NodeId SessionDiagnosticsVariableType_RepublishCount =
      new NodeId(UShort.MIN, uint(2231));

  public static final NodeId SessionDiagnosticsVariableType_TransferSubscriptionsCount =
      new NodeId(UShort.MIN, uint(2232));

  public static final NodeId SessionDiagnosticsVariableType_DeleteSubscriptionsCount =
      new NodeId(UShort.MIN, uint(2233));

  public static final NodeId SessionDiagnosticsVariableType_AddNodesCount =
      new NodeId(UShort.MIN, uint(2234));

  public static final NodeId SessionDiagnosticsVariableType_AddReferencesCount =
      new NodeId(UShort.MIN, uint(2235));

  public static final NodeId SessionDiagnosticsVariableType_DeleteNodesCount =
      new NodeId(UShort.MIN, uint(2236));

  public static final NodeId SessionDiagnosticsVariableType_DeleteReferencesCount =
      new NodeId(UShort.MIN, uint(2237));

  public static final NodeId SessionDiagnosticsVariableType_BrowseCount =
      new NodeId(UShort.MIN, uint(2238));

  public static final NodeId SessionDiagnosticsVariableType_BrowseNextCount =
      new NodeId(UShort.MIN, uint(2239));

  public static final NodeId SessionDiagnosticsVariableType_TranslateBrowsePathsToNodeIdsCount =
      new NodeId(UShort.MIN, uint(2240));

  public static final NodeId SessionDiagnosticsVariableType_QueryFirstCount =
      new NodeId(UShort.MIN, uint(2241));

  public static final NodeId SessionDiagnosticsVariableType_QueryNextCount =
      new NodeId(UShort.MIN, uint(2242));

  public static final NodeId SessionSecurityDiagnosticsArrayType =
      new NodeId(UShort.MIN, uint(2243));

  public static final NodeId SessionSecurityDiagnosticsType = new NodeId(UShort.MIN, uint(2244));

  public static final NodeId SessionSecurityDiagnosticsType_SessionId =
      new NodeId(UShort.MIN, uint(2245));

  public static final NodeId SessionSecurityDiagnosticsType_ClientUserIdOfSession =
      new NodeId(UShort.MIN, uint(2246));

  public static final NodeId SessionSecurityDiagnosticsType_ClientUserIdHistory =
      new NodeId(UShort.MIN, uint(2247));

  public static final NodeId SessionSecurityDiagnosticsType_AuthenticationMechanism =
      new NodeId(UShort.MIN, uint(2248));

  public static final NodeId SessionSecurityDiagnosticsType_Encoding =
      new NodeId(UShort.MIN, uint(2249));

  public static final NodeId SessionSecurityDiagnosticsType_TransportProtocol =
      new NodeId(UShort.MIN, uint(2250));

  public static final NodeId SessionSecurityDiagnosticsType_SecurityMode =
      new NodeId(UShort.MIN, uint(2251));

  public static final NodeId SessionSecurityDiagnosticsType_SecurityPolicyUri =
      new NodeId(UShort.MIN, uint(2252));

  public static final NodeId Server = new NodeId(UShort.MIN, uint(2253));

  public static final NodeId Server_ServerArray = new NodeId(UShort.MIN, uint(2254));

  public static final NodeId Server_NamespaceArray = new NodeId(UShort.MIN, uint(2255));

  public static final NodeId Server_ServerStatus = new NodeId(UShort.MIN, uint(2256));

  public static final NodeId Server_ServerStatus_StartTime = new NodeId(UShort.MIN, uint(2257));

  public static final NodeId Server_ServerStatus_CurrentTime = new NodeId(UShort.MIN, uint(2258));

  public static final NodeId Server_ServerStatus_State = new NodeId(UShort.MIN, uint(2259));

  public static final NodeId Server_ServerStatus_BuildInfo = new NodeId(UShort.MIN, uint(2260));

  public static final NodeId Server_ServerStatus_BuildInfo_ProductName =
      new NodeId(UShort.MIN, uint(2261));

  public static final NodeId Server_ServerStatus_BuildInfo_ProductUri =
      new NodeId(UShort.MIN, uint(2262));

  public static final NodeId Server_ServerStatus_BuildInfo_ManufacturerName =
      new NodeId(UShort.MIN, uint(2263));

  public static final NodeId Server_ServerStatus_BuildInfo_SoftwareVersion =
      new NodeId(UShort.MIN, uint(2264));

  public static final NodeId Server_ServerStatus_BuildInfo_BuildNumber =
      new NodeId(UShort.MIN, uint(2265));

  public static final NodeId Server_ServerStatus_BuildInfo_BuildDate =
      new NodeId(UShort.MIN, uint(2266));

  public static final NodeId Server_ServiceLevel = new NodeId(UShort.MIN, uint(2267));

  public static final NodeId Server_ServerCapabilities = new NodeId(UShort.MIN, uint(2268));

  public static final NodeId Server_ServerCapabilities_ServerProfileArray =
      new NodeId(UShort.MIN, uint(2269));

  public static final NodeId Server_ServerCapabilities_LocaleIdArray =
      new NodeId(UShort.MIN, uint(2271));

  public static final NodeId Server_ServerCapabilities_MinSupportedSampleRate =
      new NodeId(UShort.MIN, uint(2272));

  public static final NodeId Server_ServerDiagnostics = new NodeId(UShort.MIN, uint(2274));

  public static final NodeId Server_ServerDiagnostics_ServerDiagnosticsSummary =
      new NodeId(UShort.MIN, uint(2275));

  public static final NodeId Server_ServerDiagnostics_ServerDiagnosticsSummary_ServerViewCount =
      new NodeId(UShort.MIN, uint(2276));

  public static final NodeId Server_ServerDiagnostics_ServerDiagnosticsSummary_CurrentSessionCount =
      new NodeId(UShort.MIN, uint(2277));

  public static final NodeId
      Server_ServerDiagnostics_ServerDiagnosticsSummary_CumulatedSessionCount =
          new NodeId(UShort.MIN, uint(2278));

  public static final NodeId
      Server_ServerDiagnostics_ServerDiagnosticsSummary_SecurityRejectedSessionCount =
          new NodeId(UShort.MIN, uint(2279));

  public static final NodeId Server_ServerDiagnostics_ServerDiagnosticsSummary_SessionTimeoutCount =
      new NodeId(UShort.MIN, uint(2281));

  public static final NodeId Server_ServerDiagnostics_ServerDiagnosticsSummary_SessionAbortCount =
      new NodeId(UShort.MIN, uint(2282));

  public static final NodeId
      Server_ServerDiagnostics_ServerDiagnosticsSummary_PublishingIntervalCount =
          new NodeId(UShort.MIN, uint(2284));

  public static final NodeId
      Server_ServerDiagnostics_ServerDiagnosticsSummary_CurrentSubscriptionCount =
          new NodeId(UShort.MIN, uint(2285));

  public static final NodeId
      Server_ServerDiagnostics_ServerDiagnosticsSummary_CumulatedSubscriptionCount =
          new NodeId(UShort.MIN, uint(2286));

  public static final NodeId
      Server_ServerDiagnostics_ServerDiagnosticsSummary_SecurityRejectedRequestsCount =
          new NodeId(UShort.MIN, uint(2287));

  public static final NodeId
      Server_ServerDiagnostics_ServerDiagnosticsSummary_RejectedRequestsCount =
          new NodeId(UShort.MIN, uint(2288));

  public static final NodeId Server_ServerDiagnostics_SamplingIntervalDiagnosticsArray =
      new NodeId(UShort.MIN, uint(2289));

  public static final NodeId Server_ServerDiagnostics_SubscriptionDiagnosticsArray =
      new NodeId(UShort.MIN, uint(2290));

  public static final NodeId Server_ServerDiagnostics_EnabledFlag =
      new NodeId(UShort.MIN, uint(2294));

  public static final NodeId Server_VendorServerInfo = new NodeId(UShort.MIN, uint(2295));

  public static final NodeId Server_ServerRedundancy = new NodeId(UShort.MIN, uint(2296));

  public static final NodeId StateMachineType = new NodeId(UShort.MIN, uint(2299));

  public static final NodeId StateType = new NodeId(UShort.MIN, uint(2307));

  public static final NodeId StateType_StateNumber = new NodeId(UShort.MIN, uint(2308));

  public static final NodeId InitialStateType = new NodeId(UShort.MIN, uint(2309));

  public static final NodeId TransitionType = new NodeId(UShort.MIN, uint(2310));

  public static final NodeId TransitionEventType = new NodeId(UShort.MIN, uint(2311));

  public static final NodeId TransitionType_TransitionNumber = new NodeId(UShort.MIN, uint(2312));

  public static final NodeId AuditUpdateStateEventType = new NodeId(UShort.MIN, uint(2315));

  public static final NodeId HistoricalDataConfigurationType = new NodeId(UShort.MIN, uint(2318));

  public static final NodeId HistoricalDataConfigurationType_Stepped =
      new NodeId(UShort.MIN, uint(2323));

  public static final NodeId HistoricalDataConfigurationType_Definition =
      new NodeId(UShort.MIN, uint(2324));

  public static final NodeId HistoricalDataConfigurationType_MaxTimeInterval =
      new NodeId(UShort.MIN, uint(2325));

  public static final NodeId HistoricalDataConfigurationType_MinTimeInterval =
      new NodeId(UShort.MIN, uint(2326));

  public static final NodeId HistoricalDataConfigurationType_ExceptionDeviation =
      new NodeId(UShort.MIN, uint(2327));

  public static final NodeId HistoricalDataConfigurationType_ExceptionDeviationFormat =
      new NodeId(UShort.MIN, uint(2328));

  public static final NodeId HistoryServerCapabilitiesType = new NodeId(UShort.MIN, uint(2330));

  public static final NodeId HistoryServerCapabilitiesType_AccessHistoryDataCapability =
      new NodeId(UShort.MIN, uint(2331));

  public static final NodeId HistoryServerCapabilitiesType_AccessHistoryEventsCapability =
      new NodeId(UShort.MIN, uint(2332));

  public static final NodeId HistoryServerCapabilitiesType_InsertDataCapability =
      new NodeId(UShort.MIN, uint(2334));

  public static final NodeId HistoryServerCapabilitiesType_ReplaceDataCapability =
      new NodeId(UShort.MIN, uint(2335));

  public static final NodeId HistoryServerCapabilitiesType_UpdateDataCapability =
      new NodeId(UShort.MIN, uint(2336));

  public static final NodeId HistoryServerCapabilitiesType_DeleteRawCapability =
      new NodeId(UShort.MIN, uint(2337));

  public static final NodeId HistoryServerCapabilitiesType_DeleteAtTimeCapability =
      new NodeId(UShort.MIN, uint(2338));

  public static final NodeId AggregateFunctionType = new NodeId(UShort.MIN, uint(2340));

  public static final NodeId AggregateFunction_Interpolative = new NodeId(UShort.MIN, uint(2341));

  public static final NodeId AggregateFunction_Average = new NodeId(UShort.MIN, uint(2342));

  public static final NodeId AggregateFunction_TimeAverage = new NodeId(UShort.MIN, uint(2343));

  public static final NodeId AggregateFunction_Total = new NodeId(UShort.MIN, uint(2344));

  public static final NodeId AggregateFunction_Minimum = new NodeId(UShort.MIN, uint(2346));

  public static final NodeId AggregateFunction_Maximum = new NodeId(UShort.MIN, uint(2347));

  public static final NodeId AggregateFunction_MinimumActualTime =
      new NodeId(UShort.MIN, uint(2348));

  public static final NodeId AggregateFunction_MaximumActualTime =
      new NodeId(UShort.MIN, uint(2349));

  public static final NodeId AggregateFunction_Range = new NodeId(UShort.MIN, uint(2350));

  public static final NodeId AggregateFunction_AnnotationCount = new NodeId(UShort.MIN, uint(2351));

  public static final NodeId AggregateFunction_Count = new NodeId(UShort.MIN, uint(2352));

  public static final NodeId AggregateFunction_NumberOfTransitions =
      new NodeId(UShort.MIN, uint(2355));

  public static final NodeId AggregateFunction_Start = new NodeId(UShort.MIN, uint(2357));

  public static final NodeId AggregateFunction_End = new NodeId(UShort.MIN, uint(2358));

  public static final NodeId AggregateFunction_Delta = new NodeId(UShort.MIN, uint(2359));

  public static final NodeId AggregateFunction_DurationGood = new NodeId(UShort.MIN, uint(2360));

  public static final NodeId AggregateFunction_DurationBad = new NodeId(UShort.MIN, uint(2361));

  public static final NodeId AggregateFunction_PercentGood = new NodeId(UShort.MIN, uint(2362));

  public static final NodeId AggregateFunction_PercentBad = new NodeId(UShort.MIN, uint(2363));

  public static final NodeId AggregateFunction_WorstQuality = new NodeId(UShort.MIN, uint(2364));

  public static final NodeId DataItemType = new NodeId(UShort.MIN, uint(2365));

  public static final NodeId DataItemType_Definition = new NodeId(UShort.MIN, uint(2366));

  public static final NodeId DataItemType_ValuePrecision = new NodeId(UShort.MIN, uint(2367));

  public static final NodeId AnalogItemType = new NodeId(UShort.MIN, uint(2368));

  public static final NodeId AnalogItemType_EURange = new NodeId(UShort.MIN, uint(2369));

  public static final NodeId DiscreteItemType = new NodeId(UShort.MIN, uint(2372));

  public static final NodeId TwoStateDiscreteType = new NodeId(UShort.MIN, uint(2373));

  public static final NodeId TwoStateDiscreteType_FalseState = new NodeId(UShort.MIN, uint(2374));

  public static final NodeId TwoStateDiscreteType_TrueState = new NodeId(UShort.MIN, uint(2375));

  public static final NodeId MultiStateDiscreteType = new NodeId(UShort.MIN, uint(2376));

  public static final NodeId MultiStateDiscreteType_EnumStrings =
      new NodeId(UShort.MIN, uint(2377));

  public static final NodeId ProgramTransitionEventType = new NodeId(UShort.MIN, uint(2378));

  public static final NodeId ProgramTransitionEventType_IntermediateResult =
      new NodeId(UShort.MIN, uint(2379));

  public static final NodeId ProgramDiagnosticType = new NodeId(UShort.MIN, uint(2380));

  public static final NodeId ProgramDiagnosticType_CreateSessionId =
      new NodeId(UShort.MIN, uint(2381));

  public static final NodeId ProgramDiagnosticType_CreateClientName =
      new NodeId(UShort.MIN, uint(2382));

  public static final NodeId ProgramDiagnosticType_InvocationCreationTime =
      new NodeId(UShort.MIN, uint(2383));

  public static final NodeId ProgramDiagnosticType_LastTransitionTime =
      new NodeId(UShort.MIN, uint(2384));

  public static final NodeId ProgramDiagnosticType_LastMethodCall =
      new NodeId(UShort.MIN, uint(2385));

  public static final NodeId ProgramDiagnosticType_LastMethodSessionId =
      new NodeId(UShort.MIN, uint(2386));

  public static final NodeId ProgramDiagnosticType_LastMethodInputArguments =
      new NodeId(UShort.MIN, uint(2387));

  public static final NodeId ProgramDiagnosticType_LastMethodOutputArguments =
      new NodeId(UShort.MIN, uint(2388));

  public static final NodeId ProgramDiagnosticType_LastMethodCallTime =
      new NodeId(UShort.MIN, uint(2389));

  public static final NodeId ProgramDiagnosticType_LastMethodReturnStatus =
      new NodeId(UShort.MIN, uint(2390));

  public static final NodeId ProgramStateMachineType = new NodeId(UShort.MIN, uint(2391));

  public static final NodeId ProgramStateMachineType_Creatable = new NodeId(UShort.MIN, uint(2392));

  public static final NodeId ProgramStateMachineType_Deletable = new NodeId(UShort.MIN, uint(2393));

  public static final NodeId ProgramStateMachineType_AutoDelete =
      new NodeId(UShort.MIN, uint(2394));

  public static final NodeId ProgramStateMachineType_RecycleCount =
      new NodeId(UShort.MIN, uint(2395));

  public static final NodeId ProgramStateMachineType_InstanceCount =
      new NodeId(UShort.MIN, uint(2396));

  public static final NodeId ProgramStateMachineType_MaxInstanceCount =
      new NodeId(UShort.MIN, uint(2397));

  public static final NodeId ProgramStateMachineType_MaxRecycleCount =
      new NodeId(UShort.MIN, uint(2398));

  public static final NodeId ProgramStateMachineType_ProgramDiagnostic =
      new NodeId(UShort.MIN, uint(2399));

  public static final NodeId ProgramStateMachineType_Ready = new NodeId(UShort.MIN, uint(2400));

  public static final NodeId ProgramStateMachineType_Ready_StateNumber =
      new NodeId(UShort.MIN, uint(2401));

  public static final NodeId ProgramStateMachineType_Running = new NodeId(UShort.MIN, uint(2402));

  public static final NodeId ProgramStateMachineType_Running_StateNumber =
      new NodeId(UShort.MIN, uint(2403));

  public static final NodeId ProgramStateMachineType_Suspended = new NodeId(UShort.MIN, uint(2404));

  public static final NodeId ProgramStateMachineType_Suspended_StateNumber =
      new NodeId(UShort.MIN, uint(2405));

  public static final NodeId ProgramStateMachineType_Halted = new NodeId(UShort.MIN, uint(2406));

  public static final NodeId ProgramStateMachineType_Halted_StateNumber =
      new NodeId(UShort.MIN, uint(2407));

  public static final NodeId ProgramStateMachineType_HaltedToReady =
      new NodeId(UShort.MIN, uint(2408));

  public static final NodeId ProgramStateMachineType_HaltedToReady_TransitionNumber =
      new NodeId(UShort.MIN, uint(2409));

  public static final NodeId ProgramStateMachineType_ReadyToRunning =
      new NodeId(UShort.MIN, uint(2410));

  public static final NodeId ProgramStateMachineType_ReadyToRunning_TransitionNumber =
      new NodeId(UShort.MIN, uint(2411));

  public static final NodeId ProgramStateMachineType_RunningToHalted =
      new NodeId(UShort.MIN, uint(2412));

  public static final NodeId ProgramStateMachineType_RunningToHalted_TransitionNumber =
      new NodeId(UShort.MIN, uint(2413));

  public static final NodeId ProgramStateMachineType_RunningToReady =
      new NodeId(UShort.MIN, uint(2414));

  public static final NodeId ProgramStateMachineType_RunningToReady_TransitionNumber =
      new NodeId(UShort.MIN, uint(2415));

  public static final NodeId ProgramStateMachineType_RunningToSuspended =
      new NodeId(UShort.MIN, uint(2416));

  public static final NodeId ProgramStateMachineType_RunningToSuspended_TransitionNumber =
      new NodeId(UShort.MIN, uint(2417));

  public static final NodeId ProgramStateMachineType_SuspendedToRunning =
      new NodeId(UShort.MIN, uint(2418));

  public static final NodeId ProgramStateMachineType_SuspendedToRunning_TransitionNumber =
      new NodeId(UShort.MIN, uint(2419));

  public static final NodeId ProgramStateMachineType_SuspendedToHalted =
      new NodeId(UShort.MIN, uint(2420));

  public static final NodeId ProgramStateMachineType_SuspendedToHalted_TransitionNumber =
      new NodeId(UShort.MIN, uint(2421));

  public static final NodeId ProgramStateMachineType_SuspendedToReady =
      new NodeId(UShort.MIN, uint(2422));

  public static final NodeId ProgramStateMachineType_SuspendedToReady_TransitionNumber =
      new NodeId(UShort.MIN, uint(2423));

  public static final NodeId ProgramStateMachineType_ReadyToHalted =
      new NodeId(UShort.MIN, uint(2424));

  public static final NodeId ProgramStateMachineType_ReadyToHalted_TransitionNumber =
      new NodeId(UShort.MIN, uint(2425));

  public static final NodeId ProgramStateMachineType_Start = new NodeId(UShort.MIN, uint(2426));

  public static final NodeId ProgramStateMachineType_Suspend = new NodeId(UShort.MIN, uint(2427));

  public static final NodeId ProgramStateMachineType_Resume = new NodeId(UShort.MIN, uint(2428));

  public static final NodeId ProgramStateMachineType_Halt = new NodeId(UShort.MIN, uint(2429));

  public static final NodeId ProgramStateMachineType_Reset = new NodeId(UShort.MIN, uint(2430));

  public static final NodeId SessionDiagnosticsVariableType_RegisterNodesCount =
      new NodeId(UShort.MIN, uint(2730));

  public static final NodeId SessionDiagnosticsVariableType_UnregisterNodesCount =
      new NodeId(UShort.MIN, uint(2731));

  public static final NodeId ServerCapabilitiesType_MaxBrowseContinuationPoints =
      new NodeId(UShort.MIN, uint(2732));

  public static final NodeId ServerCapabilitiesType_MaxQueryContinuationPoints =
      new NodeId(UShort.MIN, uint(2733));

  public static final NodeId ServerCapabilitiesType_MaxHistoryContinuationPoints =
      new NodeId(UShort.MIN, uint(2734));

  public static final NodeId Server_ServerCapabilities_MaxBrowseContinuationPoints =
      new NodeId(UShort.MIN, uint(2735));

  public static final NodeId Server_ServerCapabilities_MaxQueryContinuationPoints =
      new NodeId(UShort.MIN, uint(2736));

  public static final NodeId Server_ServerCapabilities_MaxHistoryContinuationPoints =
      new NodeId(UShort.MIN, uint(2737));

  public static final NodeId SemanticChangeEventType = new NodeId(UShort.MIN, uint(2738));

  public static final NodeId SemanticChangeEventType_Changes = new NodeId(UShort.MIN, uint(2739));

  public static final NodeId ServerType_Auditing = new NodeId(UShort.MIN, uint(2742));

  public static final NodeId ServerDiagnosticsType_SessionsDiagnosticsSummary =
      new NodeId(UShort.MIN, uint(2744));

  public static final NodeId AuditChannelEventType_SecureChannelId =
      new NodeId(UShort.MIN, uint(2745));

  public static final NodeId AuditOpenSecureChannelEventType_ClientCertificateThumbprint =
      new NodeId(UShort.MIN, uint(2746));

  public static final NodeId AuditCreateSessionEventType_ClientCertificateThumbprint =
      new NodeId(UShort.MIN, uint(2747));

  public static final NodeId AuditUrlMismatchEventType = new NodeId(UShort.MIN, uint(2748));

  public static final NodeId AuditUrlMismatchEventType_EndpointUrl =
      new NodeId(UShort.MIN, uint(2749));

  public static final NodeId AuditWriteUpdateEventType_AttributeId =
      new NodeId(UShort.MIN, uint(2750));

  public static final NodeId AuditHistoryUpdateEventType_ParameterDataTypeId =
      new NodeId(UShort.MIN, uint(2751));

  public static final NodeId ServerStatusType_SecondsTillShutdown =
      new NodeId(UShort.MIN, uint(2752));

  public static final NodeId ServerStatusType_ShutdownReason = new NodeId(UShort.MIN, uint(2753));

  public static final NodeId ServerCapabilitiesType_AggregateFunctions =
      new NodeId(UShort.MIN, uint(2754));

  public static final NodeId StateVariableType = new NodeId(UShort.MIN, uint(2755));

  public static final NodeId StateVariableType_Id = new NodeId(UShort.MIN, uint(2756));

  public static final NodeId StateVariableType_Name = new NodeId(UShort.MIN, uint(2757));

  public static final NodeId StateVariableType_Number = new NodeId(UShort.MIN, uint(2758));

  public static final NodeId StateVariableType_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(2759));

  public static final NodeId FiniteStateVariableType = new NodeId(UShort.MIN, uint(2760));

  public static final NodeId FiniteStateVariableType_Id = new NodeId(UShort.MIN, uint(2761));

  public static final NodeId TransitionVariableType = new NodeId(UShort.MIN, uint(2762));

  public static final NodeId TransitionVariableType_Id = new NodeId(UShort.MIN, uint(2763));

  public static final NodeId TransitionVariableType_Name = new NodeId(UShort.MIN, uint(2764));

  public static final NodeId TransitionVariableType_Number = new NodeId(UShort.MIN, uint(2765));

  public static final NodeId TransitionVariableType_TransitionTime =
      new NodeId(UShort.MIN, uint(2766));

  public static final NodeId FiniteTransitionVariableType = new NodeId(UShort.MIN, uint(2767));

  public static final NodeId FiniteTransitionVariableType_Id = new NodeId(UShort.MIN, uint(2768));

  public static final NodeId StateMachineType_CurrentState = new NodeId(UShort.MIN, uint(2769));

  public static final NodeId StateMachineType_LastTransition = new NodeId(UShort.MIN, uint(2770));

  public static final NodeId FiniteStateMachineType = new NodeId(UShort.MIN, uint(2771));

  public static final NodeId FiniteStateMachineType_CurrentState =
      new NodeId(UShort.MIN, uint(2772));

  public static final NodeId FiniteStateMachineType_LastTransition =
      new NodeId(UShort.MIN, uint(2773));

  public static final NodeId TransitionEventType_Transition = new NodeId(UShort.MIN, uint(2774));

  public static final NodeId TransitionEventType_FromState = new NodeId(UShort.MIN, uint(2775));

  public static final NodeId TransitionEventType_ToState = new NodeId(UShort.MIN, uint(2776));

  public static final NodeId AuditUpdateStateEventType_OldStateId =
      new NodeId(UShort.MIN, uint(2777));

  public static final NodeId AuditUpdateStateEventType_NewStateId =
      new NodeId(UShort.MIN, uint(2778));

  public static final NodeId ConditionType = new NodeId(UShort.MIN, uint(2782));

  public static final NodeId RefreshStartEventType = new NodeId(UShort.MIN, uint(2787));

  public static final NodeId RefreshEndEventType = new NodeId(UShort.MIN, uint(2788));

  public static final NodeId RefreshRequiredEventType = new NodeId(UShort.MIN, uint(2789));

  public static final NodeId AuditConditionEventType = new NodeId(UShort.MIN, uint(2790));

  public static final NodeId AuditConditionEnableEventType = new NodeId(UShort.MIN, uint(2803));

  public static final NodeId AuditConditionCommentEventType = new NodeId(UShort.MIN, uint(2829));

  public static final NodeId DialogConditionType = new NodeId(UShort.MIN, uint(2830));

  public static final NodeId DialogConditionType_Prompt = new NodeId(UShort.MIN, uint(2831));

  public static final NodeId AcknowledgeableConditionType = new NodeId(UShort.MIN, uint(2881));

  public static final NodeId AlarmConditionType = new NodeId(UShort.MIN, uint(2915));

  public static final NodeId ShelvedStateMachineType = new NodeId(UShort.MIN, uint(2929));

  public static final NodeId ShelvedStateMachineType_Unshelved = new NodeId(UShort.MIN, uint(2930));

  public static final NodeId ShelvedStateMachineType_TimedShelved =
      new NodeId(UShort.MIN, uint(2932));

  public static final NodeId ShelvedStateMachineType_OneShotShelved =
      new NodeId(UShort.MIN, uint(2933));

  public static final NodeId ShelvedStateMachineType_UnshelvedToTimedShelved =
      new NodeId(UShort.MIN, uint(2935));

  public static final NodeId ShelvedStateMachineType_UnshelvedToOneShotShelved =
      new NodeId(UShort.MIN, uint(2936));

  public static final NodeId ShelvedStateMachineType_TimedShelvedToUnshelved =
      new NodeId(UShort.MIN, uint(2940));

  public static final NodeId ShelvedStateMachineType_TimedShelvedToOneShotShelved =
      new NodeId(UShort.MIN, uint(2942));

  public static final NodeId ShelvedStateMachineType_OneShotShelvedToUnshelved =
      new NodeId(UShort.MIN, uint(2943));

  public static final NodeId ShelvedStateMachineType_OneShotShelvedToTimedShelved =
      new NodeId(UShort.MIN, uint(2945));

  public static final NodeId ShelvedStateMachineType_Unshelve = new NodeId(UShort.MIN, uint(2947));

  public static final NodeId ShelvedStateMachineType_OneShotShelve =
      new NodeId(UShort.MIN, uint(2948));

  public static final NodeId ShelvedStateMachineType_TimedShelve =
      new NodeId(UShort.MIN, uint(2949));

  public static final NodeId LimitAlarmType = new NodeId(UShort.MIN, uint(2955));

  public static final NodeId ShelvedStateMachineType_TimedShelve_InputArguments =
      new NodeId(UShort.MIN, uint(2991));

  public static final NodeId Server_ServerStatus_SecondsTillShutdown =
      new NodeId(UShort.MIN, uint(2992));

  public static final NodeId Server_ServerStatus_ShutdownReason =
      new NodeId(UShort.MIN, uint(2993));

  public static final NodeId Server_Auditing = new NodeId(UShort.MIN, uint(2994));

  public static final NodeId Server_ServerCapabilities_ModellingRules =
      new NodeId(UShort.MIN, uint(2996));

  public static final NodeId Server_ServerCapabilities_AggregateFunctions =
      new NodeId(UShort.MIN, uint(2997));

  public static final NodeId SubscriptionDiagnosticsType_EventNotificationsCount =
      new NodeId(UShort.MIN, uint(2998));

  public static final NodeId AuditHistoryEventUpdateEventType = new NodeId(UShort.MIN, uint(2999));

  public static final NodeId AuditHistoryEventUpdateEventType_Filter =
      new NodeId(UShort.MIN, uint(3003));

  public static final NodeId AuditHistoryValueUpdateEventType = new NodeId(UShort.MIN, uint(3006));

  public static final NodeId AuditHistoryDeleteEventType = new NodeId(UShort.MIN, uint(3012));

  public static final NodeId AuditHistoryRawModifyDeleteEventType =
      new NodeId(UShort.MIN, uint(3014));

  public static final NodeId AuditHistoryRawModifyDeleteEventType_IsDeleteModified =
      new NodeId(UShort.MIN, uint(3015));

  public static final NodeId AuditHistoryRawModifyDeleteEventType_StartTime =
      new NodeId(UShort.MIN, uint(3016));

  public static final NodeId AuditHistoryRawModifyDeleteEventType_EndTime =
      new NodeId(UShort.MIN, uint(3017));

  public static final NodeId AuditHistoryAtTimeDeleteEventType = new NodeId(UShort.MIN, uint(3019));

  public static final NodeId AuditHistoryAtTimeDeleteEventType_ReqTimes =
      new NodeId(UShort.MIN, uint(3020));

  public static final NodeId AuditHistoryAtTimeDeleteEventType_OldValues =
      new NodeId(UShort.MIN, uint(3021));

  public static final NodeId AuditHistoryEventDeleteEventType = new NodeId(UShort.MIN, uint(3022));

  public static final NodeId AuditHistoryEventDeleteEventType_EventIds =
      new NodeId(UShort.MIN, uint(3023));

  public static final NodeId AuditHistoryEventDeleteEventType_OldValues =
      new NodeId(UShort.MIN, uint(3024));

  public static final NodeId AuditHistoryEventUpdateEventType_UpdatedNode =
      new NodeId(UShort.MIN, uint(3025));

  public static final NodeId AuditHistoryValueUpdateEventType_UpdatedNode =
      new NodeId(UShort.MIN, uint(3026));

  public static final NodeId AuditHistoryDeleteEventType_UpdatedNode =
      new NodeId(UShort.MIN, uint(3027));

  public static final NodeId AuditHistoryEventUpdateEventType_PerformInsertReplace =
      new NodeId(UShort.MIN, uint(3028));

  public static final NodeId AuditHistoryEventUpdateEventType_NewValues =
      new NodeId(UShort.MIN, uint(3029));

  public static final NodeId AuditHistoryEventUpdateEventType_OldValues =
      new NodeId(UShort.MIN, uint(3030));

  public static final NodeId AuditHistoryValueUpdateEventType_PerformInsertReplace =
      new NodeId(UShort.MIN, uint(3031));

  public static final NodeId AuditHistoryValueUpdateEventType_NewValues =
      new NodeId(UShort.MIN, uint(3032));

  public static final NodeId AuditHistoryValueUpdateEventType_OldValues =
      new NodeId(UShort.MIN, uint(3033));

  public static final NodeId AuditHistoryRawModifyDeleteEventType_OldValues =
      new NodeId(UShort.MIN, uint(3034));

  public static final NodeId EventQueueOverflowEventType = new NodeId(UShort.MIN, uint(3035));

  public static final NodeId EventTypesFolder = new NodeId(UShort.MIN, uint(3048));

  public static final NodeId ServerCapabilitiesType_SoftwareCertificates =
      new NodeId(UShort.MIN, uint(3049));

  public static final NodeId SessionDiagnosticsVariableType_MaxResponseMessageSize =
      new NodeId(UShort.MIN, uint(3050));

  public static final NodeId BuildInfoType = new NodeId(UShort.MIN, uint(3051));

  public static final NodeId BuildInfoType_ProductUri = new NodeId(UShort.MIN, uint(3052));

  public static final NodeId BuildInfoType_ManufacturerName = new NodeId(UShort.MIN, uint(3053));

  public static final NodeId BuildInfoType_ProductName = new NodeId(UShort.MIN, uint(3054));

  public static final NodeId BuildInfoType_SoftwareVersion = new NodeId(UShort.MIN, uint(3055));

  public static final NodeId BuildInfoType_BuildNumber = new NodeId(UShort.MIN, uint(3056));

  public static final NodeId BuildInfoType_BuildDate = new NodeId(UShort.MIN, uint(3057));

  public static final NodeId SessionSecurityDiagnosticsType_ClientCertificate =
      new NodeId(UShort.MIN, uint(3058));

  public static final NodeId HistoricalDataConfigurationType_AggregateConfiguration =
      new NodeId(UShort.MIN, uint(3059));

  public static final NodeId DefaultBinary = new NodeId(UShort.MIN, uint(3062));

  public static final NodeId DefaultXml = new NodeId(UShort.MIN, uint(3063));

  public static final NodeId AlwaysGeneratesEvent = new NodeId(UShort.MIN, uint(3065));

  public static final NodeId Icon = new NodeId(UShort.MIN, uint(3067));

  public static final NodeId NodeVersion = new NodeId(UShort.MIN, uint(3068));

  public static final NodeId LocalTime = new NodeId(UShort.MIN, uint(3069));

  public static final NodeId AllowNulls = new NodeId(UShort.MIN, uint(3070));

  public static final NodeId EnumValues = new NodeId(UShort.MIN, uint(3071));

  public static final NodeId InputArguments = new NodeId(UShort.MIN, uint(3072));

  public static final NodeId OutputArguments = new NodeId(UShort.MIN, uint(3073));

  public static final NodeId ServerType_ServerStatus_StartTime = new NodeId(UShort.MIN, uint(3074));

  public static final NodeId ServerType_ServerStatus_CurrentTime =
      new NodeId(UShort.MIN, uint(3075));

  public static final NodeId ServerType_ServerStatus_State = new NodeId(UShort.MIN, uint(3076));

  public static final NodeId ServerType_ServerStatus_BuildInfo = new NodeId(UShort.MIN, uint(3077));

  public static final NodeId ServerType_ServerStatus_BuildInfo_ProductUri =
      new NodeId(UShort.MIN, uint(3078));

  public static final NodeId ServerType_ServerStatus_BuildInfo_ManufacturerName =
      new NodeId(UShort.MIN, uint(3079));

  public static final NodeId ServerType_ServerStatus_BuildInfo_ProductName =
      new NodeId(UShort.MIN, uint(3080));

  public static final NodeId ServerType_ServerStatus_BuildInfo_SoftwareVersion =
      new NodeId(UShort.MIN, uint(3081));

  public static final NodeId ServerType_ServerStatus_BuildInfo_BuildNumber =
      new NodeId(UShort.MIN, uint(3082));

  public static final NodeId ServerType_ServerStatus_BuildInfo_BuildDate =
      new NodeId(UShort.MIN, uint(3083));

  public static final NodeId ServerType_ServerStatus_SecondsTillShutdown =
      new NodeId(UShort.MIN, uint(3084));

  public static final NodeId ServerType_ServerStatus_ShutdownReason =
      new NodeId(UShort.MIN, uint(3085));

  public static final NodeId ServerType_ServerCapabilities_ServerProfileArray =
      new NodeId(UShort.MIN, uint(3086));

  public static final NodeId ServerType_ServerCapabilities_LocaleIdArray =
      new NodeId(UShort.MIN, uint(3087));

  public static final NodeId ServerType_ServerCapabilities_MinSupportedSampleRate =
      new NodeId(UShort.MIN, uint(3088));

  public static final NodeId ServerType_ServerCapabilities_MaxBrowseContinuationPoints =
      new NodeId(UShort.MIN, uint(3089));

  public static final NodeId ServerType_ServerCapabilities_MaxQueryContinuationPoints =
      new NodeId(UShort.MIN, uint(3090));

  public static final NodeId ServerType_ServerCapabilities_MaxHistoryContinuationPoints =
      new NodeId(UShort.MIN, uint(3091));

  public static final NodeId ServerType_ServerCapabilities_SoftwareCertificates =
      new NodeId(UShort.MIN, uint(3092));

  public static final NodeId ServerType_ServerCapabilities_ModellingRules =
      new NodeId(UShort.MIN, uint(3093));

  public static final NodeId ServerType_ServerCapabilities_AggregateFunctions =
      new NodeId(UShort.MIN, uint(3094));

  public static final NodeId ServerType_ServerDiagnostics_ServerDiagnosticsSummary =
      new NodeId(UShort.MIN, uint(3095));

  public static final NodeId ServerType_ServerDiagnostics_ServerDiagnosticsSummary_ServerViewCount =
      new NodeId(UShort.MIN, uint(3096));

  public static final NodeId
      ServerType_ServerDiagnostics_ServerDiagnosticsSummary_CurrentSessionCount =
          new NodeId(UShort.MIN, uint(3097));

  public static final NodeId
      ServerType_ServerDiagnostics_ServerDiagnosticsSummary_CumulatedSessionCount =
          new NodeId(UShort.MIN, uint(3098));

  public static final NodeId
      ServerType_ServerDiagnostics_ServerDiagnosticsSummary_SecurityRejectedSessionCount =
          new NodeId(UShort.MIN, uint(3099));

  public static final NodeId
      ServerType_ServerDiagnostics_ServerDiagnosticsSummary_RejectedSessionCount =
          new NodeId(UShort.MIN, uint(3100));

  public static final NodeId
      ServerType_ServerDiagnostics_ServerDiagnosticsSummary_SessionTimeoutCount =
          new NodeId(UShort.MIN, uint(3101));

  public static final NodeId
      ServerType_ServerDiagnostics_ServerDiagnosticsSummary_SessionAbortCount =
          new NodeId(UShort.MIN, uint(3102));

  public static final NodeId
      ServerType_ServerDiagnostics_ServerDiagnosticsSummary_PublishingIntervalCount =
          new NodeId(UShort.MIN, uint(3104));

  public static final NodeId
      ServerType_ServerDiagnostics_ServerDiagnosticsSummary_CurrentSubscriptionCount =
          new NodeId(UShort.MIN, uint(3105));

  public static final NodeId
      ServerType_ServerDiagnostics_ServerDiagnosticsSummary_CumulatedSubscriptionCount =
          new NodeId(UShort.MIN, uint(3106));

  public static final NodeId
      ServerType_ServerDiagnostics_ServerDiagnosticsSummary_SecurityRejectedRequestsCount =
          new NodeId(UShort.MIN, uint(3107));

  public static final NodeId
      ServerType_ServerDiagnostics_ServerDiagnosticsSummary_RejectedRequestsCount =
          new NodeId(UShort.MIN, uint(3108));

  public static final NodeId ServerType_ServerDiagnostics_SamplingIntervalDiagnosticsArray =
      new NodeId(UShort.MIN, uint(3109));

  public static final NodeId ServerType_ServerDiagnostics_SubscriptionDiagnosticsArray =
      new NodeId(UShort.MIN, uint(3110));

  public static final NodeId ServerType_ServerDiagnostics_SessionsDiagnosticsSummary =
      new NodeId(UShort.MIN, uint(3111));

  public static final NodeId
      ServerType_ServerDiagnostics_SessionsDiagnosticsSummary_SessionDiagnosticsArray =
          new NodeId(UShort.MIN, uint(3112));

  public static final NodeId
      ServerType_ServerDiagnostics_SessionsDiagnosticsSummary_SessionSecurityDiagnosticsArray =
          new NodeId(UShort.MIN, uint(3113));

  public static final NodeId ServerType_ServerDiagnostics_EnabledFlag =
      new NodeId(UShort.MIN, uint(3114));

  public static final NodeId ServerType_ServerRedundancy_RedundancySupport =
      new NodeId(UShort.MIN, uint(3115));

  public static final NodeId ServerDiagnosticsType_ServerDiagnosticsSummary_ServerViewCount =
      new NodeId(UShort.MIN, uint(3116));

  public static final NodeId ServerDiagnosticsType_ServerDiagnosticsSummary_CurrentSessionCount =
      new NodeId(UShort.MIN, uint(3117));

  public static final NodeId ServerDiagnosticsType_ServerDiagnosticsSummary_CumulatedSessionCount =
      new NodeId(UShort.MIN, uint(3118));

  public static final NodeId
      ServerDiagnosticsType_ServerDiagnosticsSummary_SecurityRejectedSessionCount =
          new NodeId(UShort.MIN, uint(3119));

  public static final NodeId ServerDiagnosticsType_ServerDiagnosticsSummary_RejectedSessionCount =
      new NodeId(UShort.MIN, uint(3120));

  public static final NodeId ServerDiagnosticsType_ServerDiagnosticsSummary_SessionTimeoutCount =
      new NodeId(UShort.MIN, uint(3121));

  public static final NodeId ServerDiagnosticsType_ServerDiagnosticsSummary_SessionAbortCount =
      new NodeId(UShort.MIN, uint(3122));

  public static final NodeId
      ServerDiagnosticsType_ServerDiagnosticsSummary_PublishingIntervalCount =
          new NodeId(UShort.MIN, uint(3124));

  public static final NodeId
      ServerDiagnosticsType_ServerDiagnosticsSummary_CurrentSubscriptionCount =
          new NodeId(UShort.MIN, uint(3125));

  public static final NodeId
      ServerDiagnosticsType_ServerDiagnosticsSummary_CumulatedSubscriptionCount =
          new NodeId(UShort.MIN, uint(3126));

  public static final NodeId
      ServerDiagnosticsType_ServerDiagnosticsSummary_SecurityRejectedRequestsCount =
          new NodeId(UShort.MIN, uint(3127));

  public static final NodeId ServerDiagnosticsType_ServerDiagnosticsSummary_RejectedRequestsCount =
      new NodeId(UShort.MIN, uint(3128));

  public static final NodeId
      ServerDiagnosticsType_SessionsDiagnosticsSummary_SessionDiagnosticsArray =
          new NodeId(UShort.MIN, uint(3129));

  public static final NodeId
      ServerDiagnosticsType_SessionsDiagnosticsSummary_SessionSecurityDiagnosticsArray =
          new NodeId(UShort.MIN, uint(3130));

  public static final NodeId SessionDiagnosticsObjectType_SessionDiagnostics_SessionId =
      new NodeId(UShort.MIN, uint(3131));

  public static final NodeId SessionDiagnosticsObjectType_SessionDiagnostics_SessionName =
      new NodeId(UShort.MIN, uint(3132));

  public static final NodeId SessionDiagnosticsObjectType_SessionDiagnostics_ClientDescription =
      new NodeId(UShort.MIN, uint(3133));

  public static final NodeId SessionDiagnosticsObjectType_SessionDiagnostics_ServerUri =
      new NodeId(UShort.MIN, uint(3134));

  public static final NodeId SessionDiagnosticsObjectType_SessionDiagnostics_EndpointUrl =
      new NodeId(UShort.MIN, uint(3135));

  public static final NodeId SessionDiagnosticsObjectType_SessionDiagnostics_LocaleIds =
      new NodeId(UShort.MIN, uint(3136));

  public static final NodeId SessionDiagnosticsObjectType_SessionDiagnostics_ActualSessionTimeout =
      new NodeId(UShort.MIN, uint(3137));

  public static final NodeId
      SessionDiagnosticsObjectType_SessionDiagnostics_MaxResponseMessageSize =
          new NodeId(UShort.MIN, uint(3138));

  public static final NodeId SessionDiagnosticsObjectType_SessionDiagnostics_ClientConnectionTime =
      new NodeId(UShort.MIN, uint(3139));

  public static final NodeId SessionDiagnosticsObjectType_SessionDiagnostics_ClientLastContactTime =
      new NodeId(UShort.MIN, uint(3140));

  public static final NodeId
      SessionDiagnosticsObjectType_SessionDiagnostics_CurrentSubscriptionsCount =
          new NodeId(UShort.MIN, uint(3141));

  public static final NodeId
      SessionDiagnosticsObjectType_SessionDiagnostics_CurrentMonitoredItemsCount =
          new NodeId(UShort.MIN, uint(3142));

  public static final NodeId
      SessionDiagnosticsObjectType_SessionDiagnostics_CurrentPublishRequestsInQueue =
          new NodeId(UShort.MIN, uint(3143));

  public static final NodeId SessionDiagnosticsObjectType_SessionDiagnostics_ReadCount =
      new NodeId(UShort.MIN, uint(3151));

  public static final NodeId SessionDiagnosticsObjectType_SessionDiagnostics_HistoryReadCount =
      new NodeId(UShort.MIN, uint(3152));

  public static final NodeId SessionDiagnosticsObjectType_SessionDiagnostics_WriteCount =
      new NodeId(UShort.MIN, uint(3153));

  public static final NodeId SessionDiagnosticsObjectType_SessionDiagnostics_HistoryUpdateCount =
      new NodeId(UShort.MIN, uint(3154));

  public static final NodeId SessionDiagnosticsObjectType_SessionDiagnostics_CallCount =
      new NodeId(UShort.MIN, uint(3155));

  public static final NodeId
      SessionDiagnosticsObjectType_SessionDiagnostics_CreateMonitoredItemsCount =
          new NodeId(UShort.MIN, uint(3156));

  public static final NodeId
      SessionDiagnosticsObjectType_SessionDiagnostics_ModifyMonitoredItemsCount =
          new NodeId(UShort.MIN, uint(3157));

  public static final NodeId
      SessionDiagnosticsObjectType_SessionDiagnostics_SetMonitoringModeCount =
          new NodeId(UShort.MIN, uint(3158));

  public static final NodeId SessionDiagnosticsObjectType_SessionDiagnostics_SetTriggeringCount =
      new NodeId(UShort.MIN, uint(3159));

  public static final NodeId
      SessionDiagnosticsObjectType_SessionDiagnostics_DeleteMonitoredItemsCount =
          new NodeId(UShort.MIN, uint(3160));

  public static final NodeId
      SessionDiagnosticsObjectType_SessionDiagnostics_CreateSubscriptionCount =
          new NodeId(UShort.MIN, uint(3161));

  public static final NodeId
      SessionDiagnosticsObjectType_SessionDiagnostics_ModifySubscriptionCount =
          new NodeId(UShort.MIN, uint(3162));

  public static final NodeId
      SessionDiagnosticsObjectType_SessionDiagnostics_SetPublishingModeCount =
          new NodeId(UShort.MIN, uint(3163));

  public static final NodeId SessionDiagnosticsObjectType_SessionDiagnostics_PublishCount =
      new NodeId(UShort.MIN, uint(3164));

  public static final NodeId SessionDiagnosticsObjectType_SessionDiagnostics_RepublishCount =
      new NodeId(UShort.MIN, uint(3165));

  public static final NodeId
      SessionDiagnosticsObjectType_SessionDiagnostics_TransferSubscriptionsCount =
          new NodeId(UShort.MIN, uint(3166));

  public static final NodeId
      SessionDiagnosticsObjectType_SessionDiagnostics_DeleteSubscriptionsCount =
          new NodeId(UShort.MIN, uint(3167));

  public static final NodeId SessionDiagnosticsObjectType_SessionDiagnostics_AddNodesCount =
      new NodeId(UShort.MIN, uint(3168));

  public static final NodeId SessionDiagnosticsObjectType_SessionDiagnostics_AddReferencesCount =
      new NodeId(UShort.MIN, uint(3169));

  public static final NodeId SessionDiagnosticsObjectType_SessionDiagnostics_DeleteNodesCount =
      new NodeId(UShort.MIN, uint(3170));

  public static final NodeId SessionDiagnosticsObjectType_SessionDiagnostics_DeleteReferencesCount =
      new NodeId(UShort.MIN, uint(3171));

  public static final NodeId SessionDiagnosticsObjectType_SessionDiagnostics_BrowseCount =
      new NodeId(UShort.MIN, uint(3172));

  public static final NodeId SessionDiagnosticsObjectType_SessionDiagnostics_BrowseNextCount =
      new NodeId(UShort.MIN, uint(3173));

  public static final NodeId
      SessionDiagnosticsObjectType_SessionDiagnostics_TranslateBrowsePathsToNodeIdsCount =
          new NodeId(UShort.MIN, uint(3174));

  public static final NodeId SessionDiagnosticsObjectType_SessionDiagnostics_QueryFirstCount =
      new NodeId(UShort.MIN, uint(3175));

  public static final NodeId SessionDiagnosticsObjectType_SessionDiagnostics_QueryNextCount =
      new NodeId(UShort.MIN, uint(3176));

  public static final NodeId SessionDiagnosticsObjectType_SessionDiagnostics_RegisterNodesCount =
      new NodeId(UShort.MIN, uint(3177));

  public static final NodeId SessionDiagnosticsObjectType_SessionDiagnostics_UnregisterNodesCount =
      new NodeId(UShort.MIN, uint(3178));

  public static final NodeId SessionDiagnosticsObjectType_SessionSecurityDiagnostics_SessionId =
      new NodeId(UShort.MIN, uint(3179));

  public static final NodeId
      SessionDiagnosticsObjectType_SessionSecurityDiagnostics_ClientUserIdOfSession =
          new NodeId(UShort.MIN, uint(3180));

  public static final NodeId
      SessionDiagnosticsObjectType_SessionSecurityDiagnostics_ClientUserIdHistory =
          new NodeId(UShort.MIN, uint(3181));

  public static final NodeId
      SessionDiagnosticsObjectType_SessionSecurityDiagnostics_AuthenticationMechanism =
          new NodeId(UShort.MIN, uint(3182));

  public static final NodeId SessionDiagnosticsObjectType_SessionSecurityDiagnostics_Encoding =
      new NodeId(UShort.MIN, uint(3183));

  public static final NodeId
      SessionDiagnosticsObjectType_SessionSecurityDiagnostics_TransportProtocol =
          new NodeId(UShort.MIN, uint(3184));

  public static final NodeId SessionDiagnosticsObjectType_SessionSecurityDiagnostics_SecurityMode =
      new NodeId(UShort.MIN, uint(3185));

  public static final NodeId
      SessionDiagnosticsObjectType_SessionSecurityDiagnostics_SecurityPolicyUri =
          new NodeId(UShort.MIN, uint(3186));

  public static final NodeId
      SessionDiagnosticsObjectType_SessionSecurityDiagnostics_ClientCertificate =
          new NodeId(UShort.MIN, uint(3187));

  public static final NodeId BaseEventType_LocalTime = new NodeId(UShort.MIN, uint(3190));

  public static final NodeId ServerStatusType_BuildInfo_ProductUri =
      new NodeId(UShort.MIN, uint(3698));

  public static final NodeId ServerStatusType_BuildInfo_ManufacturerName =
      new NodeId(UShort.MIN, uint(3699));

  public static final NodeId ServerStatusType_BuildInfo_ProductName =
      new NodeId(UShort.MIN, uint(3700));

  public static final NodeId ServerStatusType_BuildInfo_SoftwareVersion =
      new NodeId(UShort.MIN, uint(3701));

  public static final NodeId ServerStatusType_BuildInfo_BuildNumber =
      new NodeId(UShort.MIN, uint(3702));

  public static final NodeId ServerStatusType_BuildInfo_BuildDate =
      new NodeId(UShort.MIN, uint(3703));

  public static final NodeId Server_ServerCapabilities_SoftwareCertificates =
      new NodeId(UShort.MIN, uint(3704));

  public static final NodeId
      Server_ServerDiagnostics_ServerDiagnosticsSummary_RejectedSessionCount =
          new NodeId(UShort.MIN, uint(3705));

  public static final NodeId Server_ServerDiagnostics_SessionsDiagnosticsSummary =
      new NodeId(UShort.MIN, uint(3706));

  public static final NodeId
      Server_ServerDiagnostics_SessionsDiagnosticsSummary_SessionDiagnosticsArray =
          new NodeId(UShort.MIN, uint(3707));

  public static final NodeId
      Server_ServerDiagnostics_SessionsDiagnosticsSummary_SessionSecurityDiagnosticsArray =
          new NodeId(UShort.MIN, uint(3708));

  public static final NodeId Server_ServerRedundancy_RedundancySupport =
      new NodeId(UShort.MIN, uint(3709));

  public static final NodeId StateMachineType_CurrentState_Id = new NodeId(UShort.MIN, uint(3720));

  public static final NodeId StateMachineType_CurrentState_Name =
      new NodeId(UShort.MIN, uint(3721));

  public static final NodeId StateMachineType_CurrentState_Number =
      new NodeId(UShort.MIN, uint(3722));

  public static final NodeId StateMachineType_CurrentState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(3723));

  public static final NodeId StateMachineType_LastTransition_Id =
      new NodeId(UShort.MIN, uint(3724));

  public static final NodeId StateMachineType_LastTransition_Name =
      new NodeId(UShort.MIN, uint(3725));

  public static final NodeId StateMachineType_LastTransition_Number =
      new NodeId(UShort.MIN, uint(3726));

  public static final NodeId StateMachineType_LastTransition_TransitionTime =
      new NodeId(UShort.MIN, uint(3727));

  public static final NodeId FiniteStateMachineType_CurrentState_Id =
      new NodeId(UShort.MIN, uint(3728));

  public static final NodeId FiniteStateMachineType_CurrentState_Name =
      new NodeId(UShort.MIN, uint(3729));

  public static final NodeId FiniteStateMachineType_CurrentState_Number =
      new NodeId(UShort.MIN, uint(3730));

  public static final NodeId FiniteStateMachineType_CurrentState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(3731));

  public static final NodeId FiniteStateMachineType_LastTransition_Id =
      new NodeId(UShort.MIN, uint(3732));

  public static final NodeId FiniteStateMachineType_LastTransition_Name =
      new NodeId(UShort.MIN, uint(3733));

  public static final NodeId FiniteStateMachineType_LastTransition_Number =
      new NodeId(UShort.MIN, uint(3734));

  public static final NodeId FiniteStateMachineType_LastTransition_TransitionTime =
      new NodeId(UShort.MIN, uint(3735));

  public static final NodeId TransitionEventType_FromState_Id = new NodeId(UShort.MIN, uint(3746));

  public static final NodeId TransitionEventType_FromState_Name =
      new NodeId(UShort.MIN, uint(3747));

  public static final NodeId TransitionEventType_FromState_Number =
      new NodeId(UShort.MIN, uint(3748));

  public static final NodeId TransitionEventType_FromState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(3749));

  public static final NodeId TransitionEventType_ToState_Id = new NodeId(UShort.MIN, uint(3750));

  public static final NodeId TransitionEventType_ToState_Name = new NodeId(UShort.MIN, uint(3751));

  public static final NodeId TransitionEventType_ToState_Number =
      new NodeId(UShort.MIN, uint(3752));

  public static final NodeId TransitionEventType_ToState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(3753));

  public static final NodeId TransitionEventType_Transition_Id = new NodeId(UShort.MIN, uint(3754));

  public static final NodeId TransitionEventType_Transition_Name =
      new NodeId(UShort.MIN, uint(3755));

  public static final NodeId TransitionEventType_Transition_Number =
      new NodeId(UShort.MIN, uint(3756));

  public static final NodeId TransitionEventType_Transition_TransitionTime =
      new NodeId(UShort.MIN, uint(3757));

  public static final NodeId ProgramTransitionAuditEventType = new NodeId(UShort.MIN, uint(3806));

  public static final NodeId ProgramTransitionAuditEventType_Transition =
      new NodeId(UShort.MIN, uint(3825));

  public static final NodeId ProgramTransitionAuditEventType_Transition_Id =
      new NodeId(UShort.MIN, uint(3826));

  public static final NodeId ProgramTransitionAuditEventType_Transition_Name =
      new NodeId(UShort.MIN, uint(3827));

  public static final NodeId ProgramTransitionAuditEventType_Transition_Number =
      new NodeId(UShort.MIN, uint(3828));

  public static final NodeId ProgramTransitionAuditEventType_Transition_TransitionTime =
      new NodeId(UShort.MIN, uint(3829));

  public static final NodeId ProgramStateMachineType_CurrentState =
      new NodeId(UShort.MIN, uint(3830));

  public static final NodeId ProgramStateMachineType_CurrentState_Id =
      new NodeId(UShort.MIN, uint(3831));

  public static final NodeId ProgramStateMachineType_CurrentState_Name =
      new NodeId(UShort.MIN, uint(3832));

  public static final NodeId ProgramStateMachineType_CurrentState_Number =
      new NodeId(UShort.MIN, uint(3833));

  public static final NodeId ProgramStateMachineType_CurrentState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(3834));

  public static final NodeId ProgramStateMachineType_LastTransition =
      new NodeId(UShort.MIN, uint(3835));

  public static final NodeId ProgramStateMachineType_LastTransition_Id =
      new NodeId(UShort.MIN, uint(3836));

  public static final NodeId ProgramStateMachineType_LastTransition_Name =
      new NodeId(UShort.MIN, uint(3837));

  public static final NodeId ProgramStateMachineType_LastTransition_Number =
      new NodeId(UShort.MIN, uint(3838));

  public static final NodeId ProgramStateMachineType_LastTransition_TransitionTime =
      new NodeId(UShort.MIN, uint(3839));

  public static final NodeId ProgramStateMachineType_ProgramDiagnostic_CreateSessionId =
      new NodeId(UShort.MIN, uint(3840));

  public static final NodeId ProgramStateMachineType_ProgramDiagnostic_CreateClientName =
      new NodeId(UShort.MIN, uint(3841));

  public static final NodeId ProgramStateMachineType_ProgramDiagnostic_InvocationCreationTime =
      new NodeId(UShort.MIN, uint(3842));

  public static final NodeId ProgramStateMachineType_ProgramDiagnostic_LastTransitionTime =
      new NodeId(UShort.MIN, uint(3843));

  public static final NodeId ProgramStateMachineType_ProgramDiagnostic_LastMethodCall =
      new NodeId(UShort.MIN, uint(3844));

  public static final NodeId ProgramStateMachineType_ProgramDiagnostic_LastMethodSessionId =
      new NodeId(UShort.MIN, uint(3845));

  public static final NodeId ProgramStateMachineType_ProgramDiagnostic_LastMethodInputArguments =
      new NodeId(UShort.MIN, uint(3846));

  public static final NodeId ProgramStateMachineType_ProgramDiagnostic_LastMethodOutputArguments =
      new NodeId(UShort.MIN, uint(3847));

  public static final NodeId ProgramStateMachineType_ProgramDiagnostic_LastMethodCallTime =
      new NodeId(UShort.MIN, uint(3848));

  public static final NodeId ProgramStateMachineType_ProgramDiagnostic_LastMethodReturnStatus =
      new NodeId(UShort.MIN, uint(3849));

  public static final NodeId ProgramStateMachineType_FinalResultData =
      new NodeId(UShort.MIN, uint(3850));

  public static final NodeId AddCommentMethodType = new NodeId(UShort.MIN, uint(3863));

  public static final NodeId AddCommentMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(3864));

  public static final NodeId ConditionType_Retain = new NodeId(UShort.MIN, uint(3874));

  public static final NodeId ConditionType_ConditionRefresh = new NodeId(UShort.MIN, uint(3875));

  public static final NodeId ConditionType_ConditionRefresh_InputArguments =
      new NodeId(UShort.MIN, uint(3876));

  public static final NodeId ShelvedStateMachineType_Unshelved_StateNumber =
      new NodeId(UShort.MIN, uint(6098));

  public static final NodeId ShelvedStateMachineType_TimedShelved_StateNumber =
      new NodeId(UShort.MIN, uint(6100));

  public static final NodeId ShelvedStateMachineType_OneShotShelved_StateNumber =
      new NodeId(UShort.MIN, uint(6101));

  public static final NodeId TimedShelveMethodType = new NodeId(UShort.MIN, uint(6102));

  public static final NodeId TimedShelveMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(6103));

  public static final NodeId IdType_EnumStrings = new NodeId(UShort.MIN, uint(7591));

  public static final NodeId EnumValueType = new NodeId(UShort.MIN, uint(7594));

  public static final NodeId MessageSecurityMode_EnumStrings = new NodeId(UShort.MIN, uint(7595));

  public static final NodeId UserTokenType_EnumStrings = new NodeId(UShort.MIN, uint(7596));

  public static final NodeId ApplicationType_EnumStrings = new NodeId(UShort.MIN, uint(7597));

  public static final NodeId SecurityTokenRequestType_EnumStrings =
      new NodeId(UShort.MIN, uint(7598));

  public static final NodeId BrowseDirection_EnumStrings = new NodeId(UShort.MIN, uint(7603));

  public static final NodeId FilterOperator_EnumStrings = new NodeId(UShort.MIN, uint(7605));

  public static final NodeId TimestampsToReturn_EnumStrings = new NodeId(UShort.MIN, uint(7606));

  public static final NodeId MonitoringMode_EnumStrings = new NodeId(UShort.MIN, uint(7608));

  public static final NodeId DataChangeTrigger_EnumStrings = new NodeId(UShort.MIN, uint(7609));

  public static final NodeId DeadbandType_EnumStrings = new NodeId(UShort.MIN, uint(7610));

  public static final NodeId RedundancySupport_EnumStrings = new NodeId(UShort.MIN, uint(7611));

  public static final NodeId ServerState_EnumStrings = new NodeId(UShort.MIN, uint(7612));

  public static final NodeId ExceptionDeviationFormat_EnumStrings =
      new NodeId(UShort.MIN, uint(7614));

  public static final NodeId EnumValueType_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(7616));

  public static final NodeId OpcUa_BinarySchema = new NodeId(UShort.MIN, uint(7617));

  public static final NodeId OpcUa_BinarySchema_DataTypeVersion =
      new NodeId(UShort.MIN, uint(7618));

  public static final NodeId OpcUa_BinarySchema_NamespaceUri = new NodeId(UShort.MIN, uint(7619));

  public static final NodeId OpcUa_BinarySchema_Argument = new NodeId(UShort.MIN, uint(7650));

  public static final NodeId OpcUa_BinarySchema_Argument_DataTypeVersion =
      new NodeId(UShort.MIN, uint(7651));

  public static final NodeId OpcUa_BinarySchema_Argument_DictionaryFragment =
      new NodeId(UShort.MIN, uint(7652));

  public static final NodeId OpcUa_BinarySchema_EnumValueType = new NodeId(UShort.MIN, uint(7656));

  public static final NodeId OpcUa_BinarySchema_EnumValueType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(7657));

  public static final NodeId OpcUa_BinarySchema_EnumValueType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(7658));

  public static final NodeId OpcUa_BinarySchema_StatusResult = new NodeId(UShort.MIN, uint(7659));

  public static final NodeId OpcUa_BinarySchema_StatusResult_DataTypeVersion =
      new NodeId(UShort.MIN, uint(7660));

  public static final NodeId OpcUa_BinarySchema_StatusResult_DictionaryFragment =
      new NodeId(UShort.MIN, uint(7661));

  public static final NodeId OpcUa_BinarySchema_UserTokenPolicy =
      new NodeId(UShort.MIN, uint(7662));

  public static final NodeId OpcUa_BinarySchema_UserTokenPolicy_DataTypeVersion =
      new NodeId(UShort.MIN, uint(7663));

  public static final NodeId OpcUa_BinarySchema_UserTokenPolicy_DictionaryFragment =
      new NodeId(UShort.MIN, uint(7664));

  public static final NodeId OpcUa_BinarySchema_ApplicationDescription =
      new NodeId(UShort.MIN, uint(7665));

  public static final NodeId OpcUa_BinarySchema_ApplicationDescription_DataTypeVersion =
      new NodeId(UShort.MIN, uint(7666));

  public static final NodeId OpcUa_BinarySchema_ApplicationDescription_DictionaryFragment =
      new NodeId(UShort.MIN, uint(7667));

  public static final NodeId OpcUa_BinarySchema_EndpointDescription =
      new NodeId(UShort.MIN, uint(7668));

  public static final NodeId OpcUa_BinarySchema_EndpointDescription_DataTypeVersion =
      new NodeId(UShort.MIN, uint(7669));

  public static final NodeId OpcUa_BinarySchema_EndpointDescription_DictionaryFragment =
      new NodeId(UShort.MIN, uint(7670));

  public static final NodeId OpcUa_BinarySchema_UserIdentityToken =
      new NodeId(UShort.MIN, uint(7671));

  public static final NodeId OpcUa_BinarySchema_UserIdentityToken_DataTypeVersion =
      new NodeId(UShort.MIN, uint(7672));

  public static final NodeId OpcUa_BinarySchema_UserIdentityToken_DictionaryFragment =
      new NodeId(UShort.MIN, uint(7673));

  public static final NodeId OpcUa_BinarySchema_AnonymousIdentityToken =
      new NodeId(UShort.MIN, uint(7674));

  public static final NodeId OpcUa_BinarySchema_AnonymousIdentityToken_DataTypeVersion =
      new NodeId(UShort.MIN, uint(7675));

  public static final NodeId OpcUa_BinarySchema_AnonymousIdentityToken_DictionaryFragment =
      new NodeId(UShort.MIN, uint(7676));

  public static final NodeId OpcUa_BinarySchema_UserNameIdentityToken =
      new NodeId(UShort.MIN, uint(7677));

  public static final NodeId OpcUa_BinarySchema_UserNameIdentityToken_DataTypeVersion =
      new NodeId(UShort.MIN, uint(7678));

  public static final NodeId OpcUa_BinarySchema_UserNameIdentityToken_DictionaryFragment =
      new NodeId(UShort.MIN, uint(7679));

  public static final NodeId OpcUa_BinarySchema_X509IdentityToken =
      new NodeId(UShort.MIN, uint(7680));

  public static final NodeId OpcUa_BinarySchema_X509IdentityToken_DataTypeVersion =
      new NodeId(UShort.MIN, uint(7681));

  public static final NodeId OpcUa_BinarySchema_X509IdentityToken_DictionaryFragment =
      new NodeId(UShort.MIN, uint(7682));

  public static final NodeId OpcUa_BinarySchema_IssuedIdentityToken =
      new NodeId(UShort.MIN, uint(7683));

  public static final NodeId OpcUa_BinarySchema_IssuedIdentityToken_DataTypeVersion =
      new NodeId(UShort.MIN, uint(7684));

  public static final NodeId OpcUa_BinarySchema_IssuedIdentityToken_DictionaryFragment =
      new NodeId(UShort.MIN, uint(7685));

  public static final NodeId OpcUa_BinarySchema_EndpointConfiguration =
      new NodeId(UShort.MIN, uint(7686));

  public static final NodeId OpcUa_BinarySchema_EndpointConfiguration_DataTypeVersion =
      new NodeId(UShort.MIN, uint(7687));

  public static final NodeId OpcUa_BinarySchema_EndpointConfiguration_DictionaryFragment =
      new NodeId(UShort.MIN, uint(7688));

  public static final NodeId OpcUa_BinarySchema_BuildInfo = new NodeId(UShort.MIN, uint(7692));

  public static final NodeId OpcUa_BinarySchema_BuildInfo_DataTypeVersion =
      new NodeId(UShort.MIN, uint(7693));

  public static final NodeId OpcUa_BinarySchema_BuildInfo_DictionaryFragment =
      new NodeId(UShort.MIN, uint(7694));

  public static final NodeId OpcUa_BinarySchema_SignedSoftwareCertificate =
      new NodeId(UShort.MIN, uint(7698));

  public static final NodeId OpcUa_BinarySchema_SignedSoftwareCertificate_DataTypeVersion =
      new NodeId(UShort.MIN, uint(7699));

  public static final NodeId OpcUa_BinarySchema_SignedSoftwareCertificate_DictionaryFragment =
      new NodeId(UShort.MIN, uint(7700));

  public static final NodeId OpcUa_BinarySchema_AddNodesItem = new NodeId(UShort.MIN, uint(7728));

  public static final NodeId OpcUa_BinarySchema_AddNodesItem_DataTypeVersion =
      new NodeId(UShort.MIN, uint(7729));

  public static final NodeId OpcUa_BinarySchema_AddNodesItem_DictionaryFragment =
      new NodeId(UShort.MIN, uint(7730));

  public static final NodeId OpcUa_BinarySchema_AddReferencesItem =
      new NodeId(UShort.MIN, uint(7731));

  public static final NodeId OpcUa_BinarySchema_AddReferencesItem_DataTypeVersion =
      new NodeId(UShort.MIN, uint(7732));

  public static final NodeId OpcUa_BinarySchema_AddReferencesItem_DictionaryFragment =
      new NodeId(UShort.MIN, uint(7733));

  public static final NodeId OpcUa_BinarySchema_DeleteNodesItem =
      new NodeId(UShort.MIN, uint(7734));

  public static final NodeId OpcUa_BinarySchema_DeleteNodesItem_DataTypeVersion =
      new NodeId(UShort.MIN, uint(7735));

  public static final NodeId OpcUa_BinarySchema_DeleteNodesItem_DictionaryFragment =
      new NodeId(UShort.MIN, uint(7736));

  public static final NodeId OpcUa_BinarySchema_DeleteReferencesItem =
      new NodeId(UShort.MIN, uint(7737));

  public static final NodeId OpcUa_BinarySchema_DeleteReferencesItem_DataTypeVersion =
      new NodeId(UShort.MIN, uint(7738));

  public static final NodeId OpcUa_BinarySchema_DeleteReferencesItem_DictionaryFragment =
      new NodeId(UShort.MIN, uint(7739));

  public static final NodeId OpcUa_BinarySchema_RegisteredServer =
      new NodeId(UShort.MIN, uint(7782));

  public static final NodeId OpcUa_BinarySchema_RegisteredServer_DataTypeVersion =
      new NodeId(UShort.MIN, uint(7783));

  public static final NodeId OpcUa_BinarySchema_RegisteredServer_DictionaryFragment =
      new NodeId(UShort.MIN, uint(7784));

  public static final NodeId OpcUa_BinarySchema_ContentFilterElement =
      new NodeId(UShort.MIN, uint(7929));

  public static final NodeId OpcUa_BinarySchema_ContentFilterElement_DataTypeVersion =
      new NodeId(UShort.MIN, uint(7930));

  public static final NodeId OpcUa_BinarySchema_ContentFilterElement_DictionaryFragment =
      new NodeId(UShort.MIN, uint(7931));

  public static final NodeId OpcUa_BinarySchema_ContentFilter = new NodeId(UShort.MIN, uint(7932));

  public static final NodeId OpcUa_BinarySchema_ContentFilter_DataTypeVersion =
      new NodeId(UShort.MIN, uint(7933));

  public static final NodeId OpcUa_BinarySchema_ContentFilter_DictionaryFragment =
      new NodeId(UShort.MIN, uint(7934));

  public static final NodeId OpcUa_BinarySchema_FilterOperand = new NodeId(UShort.MIN, uint(7935));

  public static final NodeId OpcUa_BinarySchema_FilterOperand_DataTypeVersion =
      new NodeId(UShort.MIN, uint(7936));

  public static final NodeId OpcUa_BinarySchema_FilterOperand_DictionaryFragment =
      new NodeId(UShort.MIN, uint(7937));

  public static final NodeId OpcUa_BinarySchema_ElementOperand = new NodeId(UShort.MIN, uint(7938));

  public static final NodeId OpcUa_BinarySchema_ElementOperand_DataTypeVersion =
      new NodeId(UShort.MIN, uint(7939));

  public static final NodeId OpcUa_BinarySchema_ElementOperand_DictionaryFragment =
      new NodeId(UShort.MIN, uint(7940));

  public static final NodeId OpcUa_BinarySchema_LiteralOperand = new NodeId(UShort.MIN, uint(7941));

  public static final NodeId OpcUa_BinarySchema_LiteralOperand_DataTypeVersion =
      new NodeId(UShort.MIN, uint(7942));

  public static final NodeId OpcUa_BinarySchema_LiteralOperand_DictionaryFragment =
      new NodeId(UShort.MIN, uint(7943));

  public static final NodeId OpcUa_BinarySchema_AttributeOperand =
      new NodeId(UShort.MIN, uint(7944));

  public static final NodeId OpcUa_BinarySchema_AttributeOperand_DataTypeVersion =
      new NodeId(UShort.MIN, uint(7945));

  public static final NodeId OpcUa_BinarySchema_AttributeOperand_DictionaryFragment =
      new NodeId(UShort.MIN, uint(7946));

  public static final NodeId OpcUa_BinarySchema_SimpleAttributeOperand =
      new NodeId(UShort.MIN, uint(7947));

  public static final NodeId OpcUa_BinarySchema_SimpleAttributeOperand_DataTypeVersion =
      new NodeId(UShort.MIN, uint(7948));

  public static final NodeId OpcUa_BinarySchema_SimpleAttributeOperand_DictionaryFragment =
      new NodeId(UShort.MIN, uint(7949));

  public static final NodeId OpcUa_BinarySchema_HistoryEvent = new NodeId(UShort.MIN, uint(8004));

  public static final NodeId OpcUa_BinarySchema_HistoryEvent_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8005));

  public static final NodeId OpcUa_BinarySchema_HistoryEvent_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8006));

  public static final NodeId OpcUa_BinarySchema_MonitoringFilter =
      new NodeId(UShort.MIN, uint(8067));

  public static final NodeId OpcUa_BinarySchema_MonitoringFilter_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8068));

  public static final NodeId OpcUa_BinarySchema_MonitoringFilter_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8069));

  public static final NodeId OpcUa_BinarySchema_EventFilter = new NodeId(UShort.MIN, uint(8073));

  public static final NodeId OpcUa_BinarySchema_EventFilter_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8074));

  public static final NodeId OpcUa_BinarySchema_EventFilter_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8075));

  public static final NodeId OpcUa_BinarySchema_AggregateConfiguration =
      new NodeId(UShort.MIN, uint(8076));

  public static final NodeId OpcUa_BinarySchema_AggregateConfiguration_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8077));

  public static final NodeId OpcUa_BinarySchema_AggregateConfiguration_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8078));

  public static final NodeId OpcUa_BinarySchema_HistoryEventFieldList =
      new NodeId(UShort.MIN, uint(8172));

  public static final NodeId OpcUa_BinarySchema_HistoryEventFieldList_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8173));

  public static final NodeId OpcUa_BinarySchema_HistoryEventFieldList_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8174));

  public static final NodeId OpcUa_BinarySchema_RedundantServerDataType =
      new NodeId(UShort.MIN, uint(8208));

  public static final NodeId OpcUa_BinarySchema_RedundantServerDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8209));

  public static final NodeId OpcUa_BinarySchema_RedundantServerDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8210));

  public static final NodeId OpcUa_BinarySchema_SamplingIntervalDiagnosticsDataType =
      new NodeId(UShort.MIN, uint(8211));

  public static final NodeId
      OpcUa_BinarySchema_SamplingIntervalDiagnosticsDataType_DataTypeVersion =
          new NodeId(UShort.MIN, uint(8212));

  public static final NodeId
      OpcUa_BinarySchema_SamplingIntervalDiagnosticsDataType_DictionaryFragment =
          new NodeId(UShort.MIN, uint(8213));

  public static final NodeId OpcUa_BinarySchema_ServerDiagnosticsSummaryDataType =
      new NodeId(UShort.MIN, uint(8214));

  public static final NodeId OpcUa_BinarySchema_ServerDiagnosticsSummaryDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8215));

  public static final NodeId
      OpcUa_BinarySchema_ServerDiagnosticsSummaryDataType_DictionaryFragment =
          new NodeId(UShort.MIN, uint(8216));

  public static final NodeId OpcUa_BinarySchema_ServerStatusDataType =
      new NodeId(UShort.MIN, uint(8217));

  public static final NodeId OpcUa_BinarySchema_ServerStatusDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8218));

  public static final NodeId OpcUa_BinarySchema_ServerStatusDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8219));

  public static final NodeId OpcUa_BinarySchema_SessionDiagnosticsDataType =
      new NodeId(UShort.MIN, uint(8220));

  public static final NodeId OpcUa_BinarySchema_SessionDiagnosticsDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8221));

  public static final NodeId OpcUa_BinarySchema_SessionDiagnosticsDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8222));

  public static final NodeId OpcUa_BinarySchema_SessionSecurityDiagnosticsDataType =
      new NodeId(UShort.MIN, uint(8223));

  public static final NodeId OpcUa_BinarySchema_SessionSecurityDiagnosticsDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8224));

  public static final NodeId
      OpcUa_BinarySchema_SessionSecurityDiagnosticsDataType_DictionaryFragment =
          new NodeId(UShort.MIN, uint(8225));

  public static final NodeId OpcUa_BinarySchema_ServiceCounterDataType =
      new NodeId(UShort.MIN, uint(8226));

  public static final NodeId OpcUa_BinarySchema_ServiceCounterDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8227));

  public static final NodeId OpcUa_BinarySchema_ServiceCounterDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8228));

  public static final NodeId OpcUa_BinarySchema_SubscriptionDiagnosticsDataType =
      new NodeId(UShort.MIN, uint(8229));

  public static final NodeId OpcUa_BinarySchema_SubscriptionDiagnosticsDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8230));

  public static final NodeId OpcUa_BinarySchema_SubscriptionDiagnosticsDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8231));

  public static final NodeId OpcUa_BinarySchema_ModelChangeStructureDataType =
      new NodeId(UShort.MIN, uint(8232));

  public static final NodeId OpcUa_BinarySchema_ModelChangeStructureDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8233));

  public static final NodeId OpcUa_BinarySchema_ModelChangeStructureDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8234));

  public static final NodeId OpcUa_BinarySchema_SemanticChangeStructureDataType =
      new NodeId(UShort.MIN, uint(8235));

  public static final NodeId OpcUa_BinarySchema_SemanticChangeStructureDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8236));

  public static final NodeId OpcUa_BinarySchema_SemanticChangeStructureDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8237));

  public static final NodeId OpcUa_BinarySchema_Range = new NodeId(UShort.MIN, uint(8238));

  public static final NodeId OpcUa_BinarySchema_Range_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8239));

  public static final NodeId OpcUa_BinarySchema_Range_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8240));

  public static final NodeId OpcUa_BinarySchema_EUInformation = new NodeId(UShort.MIN, uint(8241));

  public static final NodeId OpcUa_BinarySchema_EUInformation_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8242));

  public static final NodeId OpcUa_BinarySchema_EUInformation_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8243));

  public static final NodeId OpcUa_BinarySchema_Annotation = new NodeId(UShort.MIN, uint(8244));

  public static final NodeId OpcUa_BinarySchema_Annotation_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8245));

  public static final NodeId OpcUa_BinarySchema_Annotation_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8246));

  public static final NodeId OpcUa_BinarySchema_ProgramDiagnosticDataType =
      new NodeId(UShort.MIN, uint(8247));

  public static final NodeId OpcUa_BinarySchema_ProgramDiagnosticDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8248));

  public static final NodeId OpcUa_BinarySchema_ProgramDiagnosticDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8249));

  public static final NodeId EnumValueType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(8251));

  public static final NodeId OpcUa_XmlSchema = new NodeId(UShort.MIN, uint(8252));

  public static final NodeId OpcUa_XmlSchema_DataTypeVersion = new NodeId(UShort.MIN, uint(8253));

  public static final NodeId OpcUa_XmlSchema_NamespaceUri = new NodeId(UShort.MIN, uint(8254));

  public static final NodeId OpcUa_XmlSchema_Argument = new NodeId(UShort.MIN, uint(8285));

  public static final NodeId OpcUa_XmlSchema_Argument_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8286));

  public static final NodeId OpcUa_XmlSchema_Argument_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8287));

  public static final NodeId OpcUa_XmlSchema_EnumValueType = new NodeId(UShort.MIN, uint(8291));

  public static final NodeId OpcUa_XmlSchema_EnumValueType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8292));

  public static final NodeId OpcUa_XmlSchema_EnumValueType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8293));

  public static final NodeId OpcUa_XmlSchema_StatusResult = new NodeId(UShort.MIN, uint(8294));

  public static final NodeId OpcUa_XmlSchema_StatusResult_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8295));

  public static final NodeId OpcUa_XmlSchema_StatusResult_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8296));

  public static final NodeId OpcUa_XmlSchema_UserTokenPolicy = new NodeId(UShort.MIN, uint(8297));

  public static final NodeId OpcUa_XmlSchema_UserTokenPolicy_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8298));

  public static final NodeId OpcUa_XmlSchema_UserTokenPolicy_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8299));

  public static final NodeId OpcUa_XmlSchema_ApplicationDescription =
      new NodeId(UShort.MIN, uint(8300));

  public static final NodeId OpcUa_XmlSchema_ApplicationDescription_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8301));

  public static final NodeId OpcUa_XmlSchema_ApplicationDescription_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8302));

  public static final NodeId OpcUa_XmlSchema_EndpointDescription =
      new NodeId(UShort.MIN, uint(8303));

  public static final NodeId OpcUa_XmlSchema_EndpointDescription_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8304));

  public static final NodeId OpcUa_XmlSchema_EndpointDescription_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8305));

  public static final NodeId OpcUa_XmlSchema_UserIdentityToken = new NodeId(UShort.MIN, uint(8306));

  public static final NodeId OpcUa_XmlSchema_UserIdentityToken_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8307));

  public static final NodeId OpcUa_XmlSchema_UserIdentityToken_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8308));

  public static final NodeId OpcUa_XmlSchema_AnonymousIdentityToken =
      new NodeId(UShort.MIN, uint(8309));

  public static final NodeId OpcUa_XmlSchema_AnonymousIdentityToken_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8310));

  public static final NodeId OpcUa_XmlSchema_AnonymousIdentityToken_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8311));

  public static final NodeId OpcUa_XmlSchema_UserNameIdentityToken =
      new NodeId(UShort.MIN, uint(8312));

  public static final NodeId OpcUa_XmlSchema_UserNameIdentityToken_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8313));

  public static final NodeId OpcUa_XmlSchema_UserNameIdentityToken_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8314));

  public static final NodeId OpcUa_XmlSchema_X509IdentityToken = new NodeId(UShort.MIN, uint(8315));

  public static final NodeId OpcUa_XmlSchema_X509IdentityToken_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8316));

  public static final NodeId OpcUa_XmlSchema_X509IdentityToken_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8317));

  public static final NodeId OpcUa_XmlSchema_IssuedIdentityToken =
      new NodeId(UShort.MIN, uint(8318));

  public static final NodeId OpcUa_XmlSchema_IssuedIdentityToken_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8319));

  public static final NodeId OpcUa_XmlSchema_IssuedIdentityToken_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8320));

  public static final NodeId OpcUa_XmlSchema_EndpointConfiguration =
      new NodeId(UShort.MIN, uint(8321));

  public static final NodeId OpcUa_XmlSchema_EndpointConfiguration_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8322));

  public static final NodeId OpcUa_XmlSchema_EndpointConfiguration_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8323));

  public static final NodeId OpcUa_XmlSchema_BuildInfo = new NodeId(UShort.MIN, uint(8327));

  public static final NodeId OpcUa_XmlSchema_BuildInfo_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8328));

  public static final NodeId OpcUa_XmlSchema_BuildInfo_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8329));

  public static final NodeId OpcUa_XmlSchema_SignedSoftwareCertificate =
      new NodeId(UShort.MIN, uint(8333));

  public static final NodeId OpcUa_XmlSchema_SignedSoftwareCertificate_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8334));

  public static final NodeId OpcUa_XmlSchema_SignedSoftwareCertificate_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8335));

  public static final NodeId OpcUa_XmlSchema_AddNodesItem = new NodeId(UShort.MIN, uint(8363));

  public static final NodeId OpcUa_XmlSchema_AddNodesItem_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8364));

  public static final NodeId OpcUa_XmlSchema_AddNodesItem_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8365));

  public static final NodeId OpcUa_XmlSchema_AddReferencesItem = new NodeId(UShort.MIN, uint(8366));

  public static final NodeId OpcUa_XmlSchema_AddReferencesItem_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8367));

  public static final NodeId OpcUa_XmlSchema_AddReferencesItem_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8368));

  public static final NodeId OpcUa_XmlSchema_DeleteNodesItem = new NodeId(UShort.MIN, uint(8369));

  public static final NodeId OpcUa_XmlSchema_DeleteNodesItem_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8370));

  public static final NodeId OpcUa_XmlSchema_DeleteNodesItem_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8371));

  public static final NodeId OpcUa_XmlSchema_DeleteReferencesItem =
      new NodeId(UShort.MIN, uint(8372));

  public static final NodeId OpcUa_XmlSchema_DeleteReferencesItem_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8373));

  public static final NodeId OpcUa_XmlSchema_DeleteReferencesItem_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8374));

  public static final NodeId OpcUa_XmlSchema_RegisteredServer = new NodeId(UShort.MIN, uint(8417));

  public static final NodeId OpcUa_XmlSchema_RegisteredServer_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8418));

  public static final NodeId OpcUa_XmlSchema_RegisteredServer_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8419));

  public static final NodeId OpcUa_XmlSchema_ContentFilterElement =
      new NodeId(UShort.MIN, uint(8564));

  public static final NodeId OpcUa_XmlSchema_ContentFilterElement_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8565));

  public static final NodeId OpcUa_XmlSchema_ContentFilterElement_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8566));

  public static final NodeId OpcUa_XmlSchema_ContentFilter = new NodeId(UShort.MIN, uint(8567));

  public static final NodeId OpcUa_XmlSchema_ContentFilter_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8568));

  public static final NodeId OpcUa_XmlSchema_ContentFilter_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8569));

  public static final NodeId OpcUa_XmlSchema_FilterOperand = new NodeId(UShort.MIN, uint(8570));

  public static final NodeId OpcUa_XmlSchema_FilterOperand_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8571));

  public static final NodeId OpcUa_XmlSchema_FilterOperand_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8572));

  public static final NodeId OpcUa_XmlSchema_ElementOperand = new NodeId(UShort.MIN, uint(8573));

  public static final NodeId OpcUa_XmlSchema_ElementOperand_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8574));

  public static final NodeId OpcUa_XmlSchema_ElementOperand_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8575));

  public static final NodeId OpcUa_XmlSchema_LiteralOperand = new NodeId(UShort.MIN, uint(8576));

  public static final NodeId OpcUa_XmlSchema_LiteralOperand_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8577));

  public static final NodeId OpcUa_XmlSchema_LiteralOperand_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8578));

  public static final NodeId OpcUa_XmlSchema_AttributeOperand = new NodeId(UShort.MIN, uint(8579));

  public static final NodeId OpcUa_XmlSchema_AttributeOperand_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8580));

  public static final NodeId OpcUa_XmlSchema_AttributeOperand_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8581));

  public static final NodeId OpcUa_XmlSchema_SimpleAttributeOperand =
      new NodeId(UShort.MIN, uint(8582));

  public static final NodeId OpcUa_XmlSchema_SimpleAttributeOperand_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8583));

  public static final NodeId OpcUa_XmlSchema_SimpleAttributeOperand_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8584));

  public static final NodeId OpcUa_XmlSchema_HistoryEvent = new NodeId(UShort.MIN, uint(8639));

  public static final NodeId OpcUa_XmlSchema_HistoryEvent_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8640));

  public static final NodeId OpcUa_XmlSchema_HistoryEvent_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8641));

  public static final NodeId OpcUa_XmlSchema_MonitoringFilter = new NodeId(UShort.MIN, uint(8702));

  public static final NodeId OpcUa_XmlSchema_MonitoringFilter_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8703));

  public static final NodeId OpcUa_XmlSchema_MonitoringFilter_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8704));

  public static final NodeId OpcUa_XmlSchema_EventFilter = new NodeId(UShort.MIN, uint(8708));

  public static final NodeId OpcUa_XmlSchema_EventFilter_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8709));

  public static final NodeId OpcUa_XmlSchema_EventFilter_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8710));

  public static final NodeId OpcUa_XmlSchema_AggregateConfiguration =
      new NodeId(UShort.MIN, uint(8711));

  public static final NodeId OpcUa_XmlSchema_AggregateConfiguration_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8712));

  public static final NodeId OpcUa_XmlSchema_AggregateConfiguration_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8713));

  public static final NodeId OpcUa_XmlSchema_HistoryEventFieldList =
      new NodeId(UShort.MIN, uint(8807));

  public static final NodeId OpcUa_XmlSchema_HistoryEventFieldList_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8808));

  public static final NodeId OpcUa_XmlSchema_HistoryEventFieldList_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8809));

  public static final NodeId OpcUa_XmlSchema_RedundantServerDataType =
      new NodeId(UShort.MIN, uint(8843));

  public static final NodeId OpcUa_XmlSchema_RedundantServerDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8844));

  public static final NodeId OpcUa_XmlSchema_RedundantServerDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8845));

  public static final NodeId OpcUa_XmlSchema_SamplingIntervalDiagnosticsDataType =
      new NodeId(UShort.MIN, uint(8846));

  public static final NodeId OpcUa_XmlSchema_SamplingIntervalDiagnosticsDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8847));

  public static final NodeId
      OpcUa_XmlSchema_SamplingIntervalDiagnosticsDataType_DictionaryFragment =
          new NodeId(UShort.MIN, uint(8848));

  public static final NodeId OpcUa_XmlSchema_ServerDiagnosticsSummaryDataType =
      new NodeId(UShort.MIN, uint(8849));

  public static final NodeId OpcUa_XmlSchema_ServerDiagnosticsSummaryDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8850));

  public static final NodeId OpcUa_XmlSchema_ServerDiagnosticsSummaryDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8851));

  public static final NodeId OpcUa_XmlSchema_ServerStatusDataType =
      new NodeId(UShort.MIN, uint(8852));

  public static final NodeId OpcUa_XmlSchema_ServerStatusDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8853));

  public static final NodeId OpcUa_XmlSchema_ServerStatusDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8854));

  public static final NodeId OpcUa_XmlSchema_SessionDiagnosticsDataType =
      new NodeId(UShort.MIN, uint(8855));

  public static final NodeId OpcUa_XmlSchema_SessionDiagnosticsDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8856));

  public static final NodeId OpcUa_XmlSchema_SessionDiagnosticsDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8857));

  public static final NodeId OpcUa_XmlSchema_SessionSecurityDiagnosticsDataType =
      new NodeId(UShort.MIN, uint(8858));

  public static final NodeId OpcUa_XmlSchema_SessionSecurityDiagnosticsDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8859));

  public static final NodeId OpcUa_XmlSchema_SessionSecurityDiagnosticsDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8860));

  public static final NodeId OpcUa_XmlSchema_ServiceCounterDataType =
      new NodeId(UShort.MIN, uint(8861));

  public static final NodeId OpcUa_XmlSchema_ServiceCounterDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8862));

  public static final NodeId OpcUa_XmlSchema_ServiceCounterDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8863));

  public static final NodeId OpcUa_XmlSchema_SubscriptionDiagnosticsDataType =
      new NodeId(UShort.MIN, uint(8864));

  public static final NodeId OpcUa_XmlSchema_SubscriptionDiagnosticsDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8865));

  public static final NodeId OpcUa_XmlSchema_SubscriptionDiagnosticsDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8866));

  public static final NodeId OpcUa_XmlSchema_ModelChangeStructureDataType =
      new NodeId(UShort.MIN, uint(8867));

  public static final NodeId OpcUa_XmlSchema_ModelChangeStructureDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8868));

  public static final NodeId OpcUa_XmlSchema_ModelChangeStructureDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8869));

  public static final NodeId OpcUa_XmlSchema_SemanticChangeStructureDataType =
      new NodeId(UShort.MIN, uint(8870));

  public static final NodeId OpcUa_XmlSchema_SemanticChangeStructureDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8871));

  public static final NodeId OpcUa_XmlSchema_SemanticChangeStructureDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8872));

  public static final NodeId OpcUa_XmlSchema_Range = new NodeId(UShort.MIN, uint(8873));

  public static final NodeId OpcUa_XmlSchema_Range_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8874));

  public static final NodeId OpcUa_XmlSchema_Range_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8875));

  public static final NodeId OpcUa_XmlSchema_EUInformation = new NodeId(UShort.MIN, uint(8876));

  public static final NodeId OpcUa_XmlSchema_EUInformation_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8877));

  public static final NodeId OpcUa_XmlSchema_EUInformation_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8878));

  public static final NodeId OpcUa_XmlSchema_Annotation = new NodeId(UShort.MIN, uint(8879));

  public static final NodeId OpcUa_XmlSchema_Annotation_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8880));

  public static final NodeId OpcUa_XmlSchema_Annotation_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8881));

  public static final NodeId OpcUa_XmlSchema_ProgramDiagnosticDataType =
      new NodeId(UShort.MIN, uint(8882));

  public static final NodeId OpcUa_XmlSchema_ProgramDiagnosticDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8883));

  public static final NodeId OpcUa_XmlSchema_ProgramDiagnosticDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8884));

  public static final NodeId SubscriptionDiagnosticsType_MaxLifetimeCount =
      new NodeId(UShort.MIN, uint(8888));

  public static final NodeId SubscriptionDiagnosticsType_LatePublishRequestCount =
      new NodeId(UShort.MIN, uint(8889));

  public static final NodeId SubscriptionDiagnosticsType_CurrentKeepAliveCount =
      new NodeId(UShort.MIN, uint(8890));

  public static final NodeId SubscriptionDiagnosticsType_CurrentLifetimeCount =
      new NodeId(UShort.MIN, uint(8891));

  public static final NodeId SubscriptionDiagnosticsType_UnacknowledgedMessageCount =
      new NodeId(UShort.MIN, uint(8892));

  public static final NodeId SubscriptionDiagnosticsType_DiscardedMessageCount =
      new NodeId(UShort.MIN, uint(8893));

  public static final NodeId SubscriptionDiagnosticsType_MonitoredItemCount =
      new NodeId(UShort.MIN, uint(8894));

  public static final NodeId SubscriptionDiagnosticsType_DisabledMonitoredItemCount =
      new NodeId(UShort.MIN, uint(8895));

  public static final NodeId SubscriptionDiagnosticsType_MonitoringQueueOverflowCount =
      new NodeId(UShort.MIN, uint(8896));

  public static final NodeId SubscriptionDiagnosticsType_NextSequenceNumber =
      new NodeId(UShort.MIN, uint(8897));

  public static final NodeId SessionDiagnosticsObjectType_SessionDiagnostics_TotalRequestCount =
      new NodeId(UShort.MIN, uint(8898));

  public static final NodeId SessionDiagnosticsVariableType_TotalRequestCount =
      new NodeId(UShort.MIN, uint(8900));

  public static final NodeId SubscriptionDiagnosticsType_EventQueueOverflowCount =
      new NodeId(UShort.MIN, uint(8902));

  public static final NodeId TimeZoneDataType = new NodeId(UShort.MIN, uint(8912));

  public static final NodeId TimeZoneDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(8913));

  public static final NodeId OpcUa_BinarySchema_TimeZoneDataType =
      new NodeId(UShort.MIN, uint(8914));

  public static final NodeId OpcUa_BinarySchema_TimeZoneDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8915));

  public static final NodeId OpcUa_BinarySchema_TimeZoneDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8916));

  public static final NodeId TimeZoneDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(8917));

  public static final NodeId OpcUa_XmlSchema_TimeZoneDataType = new NodeId(UShort.MIN, uint(8918));

  public static final NodeId OpcUa_XmlSchema_TimeZoneDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8919));

  public static final NodeId OpcUa_XmlSchema_TimeZoneDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8920));

  public static final NodeId AuditConditionRespondEventType = new NodeId(UShort.MIN, uint(8927));

  public static final NodeId AuditConditionAcknowledgeEventType =
      new NodeId(UShort.MIN, uint(8944));

  public static final NodeId AuditConditionConfirmEventType = new NodeId(UShort.MIN, uint(8961));

  public static final NodeId TwoStateVariableType = new NodeId(UShort.MIN, uint(8995));

  public static final NodeId TwoStateVariableType_Id = new NodeId(UShort.MIN, uint(8996));

  public static final NodeId TwoStateVariableType_TransitionTime =
      new NodeId(UShort.MIN, uint(9000));

  public static final NodeId TwoStateVariableType_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(9001));

  public static final NodeId ConditionVariableType = new NodeId(UShort.MIN, uint(9002));

  public static final NodeId ConditionVariableType_SourceTimestamp =
      new NodeId(UShort.MIN, uint(9003));

  public static final NodeId HasTrueSubState = new NodeId(UShort.MIN, uint(9004));

  public static final NodeId HasFalseSubState = new NodeId(UShort.MIN, uint(9005));

  public static final NodeId HasCondition = new NodeId(UShort.MIN, uint(9006));

  public static final NodeId ConditionRefreshMethodType = new NodeId(UShort.MIN, uint(9007));

  public static final NodeId ConditionRefreshMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(9008));

  public static final NodeId ConditionType_ConditionName = new NodeId(UShort.MIN, uint(9009));

  public static final NodeId ConditionType_BranchId = new NodeId(UShort.MIN, uint(9010));

  public static final NodeId ConditionType_EnabledState = new NodeId(UShort.MIN, uint(9011));

  public static final NodeId ConditionType_EnabledState_Id = new NodeId(UShort.MIN, uint(9012));

  public static final NodeId ConditionType_EnabledState_Name = new NodeId(UShort.MIN, uint(9013));

  public static final NodeId ConditionType_EnabledState_Number = new NodeId(UShort.MIN, uint(9014));

  public static final NodeId ConditionType_EnabledState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(9015));

  public static final NodeId ConditionType_EnabledState_TransitionTime =
      new NodeId(UShort.MIN, uint(9016));

  public static final NodeId ConditionType_EnabledState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(9017));

  public static final NodeId ConditionType_EnabledState_TrueState =
      new NodeId(UShort.MIN, uint(9018));

  public static final NodeId ConditionType_EnabledState_FalseState =
      new NodeId(UShort.MIN, uint(9019));

  public static final NodeId ConditionType_Quality = new NodeId(UShort.MIN, uint(9020));

  public static final NodeId ConditionType_Quality_SourceTimestamp =
      new NodeId(UShort.MIN, uint(9021));

  public static final NodeId ConditionType_LastSeverity = new NodeId(UShort.MIN, uint(9022));

  public static final NodeId ConditionType_LastSeverity_SourceTimestamp =
      new NodeId(UShort.MIN, uint(9023));

  public static final NodeId ConditionType_Comment = new NodeId(UShort.MIN, uint(9024));

  public static final NodeId ConditionType_Comment_SourceTimestamp =
      new NodeId(UShort.MIN, uint(9025));

  public static final NodeId ConditionType_ClientUserId = new NodeId(UShort.MIN, uint(9026));

  public static final NodeId ConditionType_Enable = new NodeId(UShort.MIN, uint(9027));

  public static final NodeId ConditionType_Disable = new NodeId(UShort.MIN, uint(9028));

  public static final NodeId ConditionType_AddComment = new NodeId(UShort.MIN, uint(9029));

  public static final NodeId ConditionType_AddComment_InputArguments =
      new NodeId(UShort.MIN, uint(9030));

  public static final NodeId DialogResponseMethodType = new NodeId(UShort.MIN, uint(9031));

  public static final NodeId DialogResponseMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(9032));

  public static final NodeId DialogConditionType_EnabledState = new NodeId(UShort.MIN, uint(9035));

  public static final NodeId DialogConditionType_EnabledState_Id =
      new NodeId(UShort.MIN, uint(9036));

  public static final NodeId DialogConditionType_EnabledState_Name =
      new NodeId(UShort.MIN, uint(9037));

  public static final NodeId DialogConditionType_EnabledState_Number =
      new NodeId(UShort.MIN, uint(9038));

  public static final NodeId DialogConditionType_EnabledState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(9039));

  public static final NodeId DialogConditionType_EnabledState_TransitionTime =
      new NodeId(UShort.MIN, uint(9040));

  public static final NodeId DialogConditionType_EnabledState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(9041));

  public static final NodeId DialogConditionType_EnabledState_TrueState =
      new NodeId(UShort.MIN, uint(9042));

  public static final NodeId DialogConditionType_EnabledState_FalseState =
      new NodeId(UShort.MIN, uint(9043));

  public static final NodeId DialogConditionType_DialogState = new NodeId(UShort.MIN, uint(9055));

  public static final NodeId DialogConditionType_DialogState_Id =
      new NodeId(UShort.MIN, uint(9056));

  public static final NodeId DialogConditionType_DialogState_Name =
      new NodeId(UShort.MIN, uint(9057));

  public static final NodeId DialogConditionType_DialogState_Number =
      new NodeId(UShort.MIN, uint(9058));

  public static final NodeId DialogConditionType_DialogState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(9059));

  public static final NodeId DialogConditionType_DialogState_TransitionTime =
      new NodeId(UShort.MIN, uint(9060));

  public static final NodeId DialogConditionType_DialogState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(9061));

  public static final NodeId DialogConditionType_DialogState_TrueState =
      new NodeId(UShort.MIN, uint(9062));

  public static final NodeId DialogConditionType_DialogState_FalseState =
      new NodeId(UShort.MIN, uint(9063));

  public static final NodeId DialogConditionType_ResponseOptionSet =
      new NodeId(UShort.MIN, uint(9064));

  public static final NodeId DialogConditionType_DefaultResponse =
      new NodeId(UShort.MIN, uint(9065));

  public static final NodeId DialogConditionType_OkResponse = new NodeId(UShort.MIN, uint(9066));

  public static final NodeId DialogConditionType_CancelResponse =
      new NodeId(UShort.MIN, uint(9067));

  public static final NodeId DialogConditionType_LastResponse = new NodeId(UShort.MIN, uint(9068));

  public static final NodeId DialogConditionType_Respond = new NodeId(UShort.MIN, uint(9069));

  public static final NodeId DialogConditionType_Respond_InputArguments =
      new NodeId(UShort.MIN, uint(9070));

  public static final NodeId AcknowledgeableConditionType_EnabledState =
      new NodeId(UShort.MIN, uint(9073));

  public static final NodeId AcknowledgeableConditionType_EnabledState_Id =
      new NodeId(UShort.MIN, uint(9074));

  public static final NodeId AcknowledgeableConditionType_EnabledState_Name =
      new NodeId(UShort.MIN, uint(9075));

  public static final NodeId AcknowledgeableConditionType_EnabledState_Number =
      new NodeId(UShort.MIN, uint(9076));

  public static final NodeId AcknowledgeableConditionType_EnabledState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(9077));

  public static final NodeId AcknowledgeableConditionType_EnabledState_TransitionTime =
      new NodeId(UShort.MIN, uint(9078));

  public static final NodeId AcknowledgeableConditionType_EnabledState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(9079));

  public static final NodeId AcknowledgeableConditionType_EnabledState_TrueState =
      new NodeId(UShort.MIN, uint(9080));

  public static final NodeId AcknowledgeableConditionType_EnabledState_FalseState =
      new NodeId(UShort.MIN, uint(9081));

  public static final NodeId AcknowledgeableConditionType_AckedState =
      new NodeId(UShort.MIN, uint(9093));

  public static final NodeId AcknowledgeableConditionType_AckedState_Id =
      new NodeId(UShort.MIN, uint(9094));

  public static final NodeId AcknowledgeableConditionType_AckedState_Name =
      new NodeId(UShort.MIN, uint(9095));

  public static final NodeId AcknowledgeableConditionType_AckedState_Number =
      new NodeId(UShort.MIN, uint(9096));

  public static final NodeId AcknowledgeableConditionType_AckedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(9097));

  public static final NodeId AcknowledgeableConditionType_AckedState_TransitionTime =
      new NodeId(UShort.MIN, uint(9098));

  public static final NodeId AcknowledgeableConditionType_AckedState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(9099));

  public static final NodeId AcknowledgeableConditionType_AckedState_TrueState =
      new NodeId(UShort.MIN, uint(9100));

  public static final NodeId AcknowledgeableConditionType_AckedState_FalseState =
      new NodeId(UShort.MIN, uint(9101));

  public static final NodeId AcknowledgeableConditionType_ConfirmedState =
      new NodeId(UShort.MIN, uint(9102));

  public static final NodeId AcknowledgeableConditionType_ConfirmedState_Id =
      new NodeId(UShort.MIN, uint(9103));

  public static final NodeId AcknowledgeableConditionType_ConfirmedState_Name =
      new NodeId(UShort.MIN, uint(9104));

  public static final NodeId AcknowledgeableConditionType_ConfirmedState_Number =
      new NodeId(UShort.MIN, uint(9105));

  public static final NodeId AcknowledgeableConditionType_ConfirmedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(9106));

  public static final NodeId AcknowledgeableConditionType_ConfirmedState_TransitionTime =
      new NodeId(UShort.MIN, uint(9107));

  public static final NodeId AcknowledgeableConditionType_ConfirmedState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(9108));

  public static final NodeId AcknowledgeableConditionType_ConfirmedState_TrueState =
      new NodeId(UShort.MIN, uint(9109));

  public static final NodeId AcknowledgeableConditionType_ConfirmedState_FalseState =
      new NodeId(UShort.MIN, uint(9110));

  public static final NodeId AcknowledgeableConditionType_Acknowledge =
      new NodeId(UShort.MIN, uint(9111));

  public static final NodeId AcknowledgeableConditionType_Acknowledge_InputArguments =
      new NodeId(UShort.MIN, uint(9112));

  public static final NodeId AcknowledgeableConditionType_Confirm =
      new NodeId(UShort.MIN, uint(9113));

  public static final NodeId AcknowledgeableConditionType_Confirm_InputArguments =
      new NodeId(UShort.MIN, uint(9114));

  public static final NodeId ShelvedStateMachineType_UnshelveTime =
      new NodeId(UShort.MIN, uint(9115));

  public static final NodeId AlarmConditionType_EnabledState = new NodeId(UShort.MIN, uint(9118));

  public static final NodeId AlarmConditionType_EnabledState_Id =
      new NodeId(UShort.MIN, uint(9119));

  public static final NodeId AlarmConditionType_EnabledState_Name =
      new NodeId(UShort.MIN, uint(9120));

  public static final NodeId AlarmConditionType_EnabledState_Number =
      new NodeId(UShort.MIN, uint(9121));

  public static final NodeId AlarmConditionType_EnabledState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(9122));

  public static final NodeId AlarmConditionType_EnabledState_TransitionTime =
      new NodeId(UShort.MIN, uint(9123));

  public static final NodeId AlarmConditionType_EnabledState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(9124));

  public static final NodeId AlarmConditionType_EnabledState_TrueState =
      new NodeId(UShort.MIN, uint(9125));

  public static final NodeId AlarmConditionType_EnabledState_FalseState =
      new NodeId(UShort.MIN, uint(9126));

  public static final NodeId AlarmConditionType_ActiveState = new NodeId(UShort.MIN, uint(9160));

  public static final NodeId AlarmConditionType_ActiveState_Id = new NodeId(UShort.MIN, uint(9161));

  public static final NodeId AlarmConditionType_ActiveState_Name =
      new NodeId(UShort.MIN, uint(9162));

  public static final NodeId AlarmConditionType_ActiveState_Number =
      new NodeId(UShort.MIN, uint(9163));

  public static final NodeId AlarmConditionType_ActiveState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(9164));

  public static final NodeId AlarmConditionType_ActiveState_TransitionTime =
      new NodeId(UShort.MIN, uint(9165));

  public static final NodeId AlarmConditionType_ActiveState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(9166));

  public static final NodeId AlarmConditionType_ActiveState_TrueState =
      new NodeId(UShort.MIN, uint(9167));

  public static final NodeId AlarmConditionType_ActiveState_FalseState =
      new NodeId(UShort.MIN, uint(9168));

  public static final NodeId AlarmConditionType_SuppressedState =
      new NodeId(UShort.MIN, uint(9169));

  public static final NodeId AlarmConditionType_SuppressedState_Id =
      new NodeId(UShort.MIN, uint(9170));

  public static final NodeId AlarmConditionType_SuppressedState_Name =
      new NodeId(UShort.MIN, uint(9171));

  public static final NodeId AlarmConditionType_SuppressedState_Number =
      new NodeId(UShort.MIN, uint(9172));

  public static final NodeId AlarmConditionType_SuppressedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(9173));

  public static final NodeId AlarmConditionType_SuppressedState_TransitionTime =
      new NodeId(UShort.MIN, uint(9174));

  public static final NodeId AlarmConditionType_SuppressedState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(9175));

  public static final NodeId AlarmConditionType_SuppressedState_TrueState =
      new NodeId(UShort.MIN, uint(9176));

  public static final NodeId AlarmConditionType_SuppressedState_FalseState =
      new NodeId(UShort.MIN, uint(9177));

  public static final NodeId AlarmConditionType_ShelvingState = new NodeId(UShort.MIN, uint(9178));

  public static final NodeId AlarmConditionType_ShelvingState_CurrentState =
      new NodeId(UShort.MIN, uint(9179));

  public static final NodeId AlarmConditionType_ShelvingState_CurrentState_Id =
      new NodeId(UShort.MIN, uint(9180));

  public static final NodeId AlarmConditionType_ShelvingState_CurrentState_Name =
      new NodeId(UShort.MIN, uint(9181));

  public static final NodeId AlarmConditionType_ShelvingState_CurrentState_Number =
      new NodeId(UShort.MIN, uint(9182));

  public static final NodeId AlarmConditionType_ShelvingState_CurrentState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(9183));

  public static final NodeId AlarmConditionType_ShelvingState_LastTransition =
      new NodeId(UShort.MIN, uint(9184));

  public static final NodeId AlarmConditionType_ShelvingState_LastTransition_Id =
      new NodeId(UShort.MIN, uint(9185));

  public static final NodeId AlarmConditionType_ShelvingState_LastTransition_Name =
      new NodeId(UShort.MIN, uint(9186));

  public static final NodeId AlarmConditionType_ShelvingState_LastTransition_Number =
      new NodeId(UShort.MIN, uint(9187));

  public static final NodeId AlarmConditionType_ShelvingState_LastTransition_TransitionTime =
      new NodeId(UShort.MIN, uint(9188));

  public static final NodeId AlarmConditionType_ShelvingState_UnshelveTime =
      new NodeId(UShort.MIN, uint(9189));

  public static final NodeId AlarmConditionType_ShelvingState_Unshelve =
      new NodeId(UShort.MIN, uint(9211));

  public static final NodeId AlarmConditionType_ShelvingState_OneShotShelve =
      new NodeId(UShort.MIN, uint(9212));

  public static final NodeId AlarmConditionType_ShelvingState_TimedShelve =
      new NodeId(UShort.MIN, uint(9213));

  public static final NodeId AlarmConditionType_ShelvingState_TimedShelve_InputArguments =
      new NodeId(UShort.MIN, uint(9214));

  public static final NodeId AlarmConditionType_SuppressedOrShelved =
      new NodeId(UShort.MIN, uint(9215));

  public static final NodeId AlarmConditionType_MaxTimeShelved = new NodeId(UShort.MIN, uint(9216));

  public static final NodeId ExclusiveLimitStateMachineType = new NodeId(UShort.MIN, uint(9318));

  public static final NodeId ExclusiveLimitStateMachineType_HighHigh =
      new NodeId(UShort.MIN, uint(9329));

  public static final NodeId ExclusiveLimitStateMachineType_HighHigh_StateNumber =
      new NodeId(UShort.MIN, uint(9330));

  public static final NodeId ExclusiveLimitStateMachineType_High =
      new NodeId(UShort.MIN, uint(9331));

  public static final NodeId ExclusiveLimitStateMachineType_High_StateNumber =
      new NodeId(UShort.MIN, uint(9332));

  public static final NodeId ExclusiveLimitStateMachineType_Low =
      new NodeId(UShort.MIN, uint(9333));

  public static final NodeId ExclusiveLimitStateMachineType_Low_StateNumber =
      new NodeId(UShort.MIN, uint(9334));

  public static final NodeId ExclusiveLimitStateMachineType_LowLow =
      new NodeId(UShort.MIN, uint(9335));

  public static final NodeId ExclusiveLimitStateMachineType_LowLow_StateNumber =
      new NodeId(UShort.MIN, uint(9336));

  public static final NodeId ExclusiveLimitStateMachineType_LowLowToLow =
      new NodeId(UShort.MIN, uint(9337));

  public static final NodeId ExclusiveLimitStateMachineType_LowToLowLow =
      new NodeId(UShort.MIN, uint(9338));

  public static final NodeId ExclusiveLimitStateMachineType_HighHighToHigh =
      new NodeId(UShort.MIN, uint(9339));

  public static final NodeId ExclusiveLimitStateMachineType_HighToHighHigh =
      new NodeId(UShort.MIN, uint(9340));

  public static final NodeId ExclusiveLimitAlarmType = new NodeId(UShort.MIN, uint(9341));

  public static final NodeId ExclusiveLimitAlarmType_ActiveState =
      new NodeId(UShort.MIN, uint(9398));

  public static final NodeId ExclusiveLimitAlarmType_ActiveState_Id =
      new NodeId(UShort.MIN, uint(9399));

  public static final NodeId ExclusiveLimitAlarmType_ActiveState_Name =
      new NodeId(UShort.MIN, uint(9400));

  public static final NodeId ExclusiveLimitAlarmType_ActiveState_Number =
      new NodeId(UShort.MIN, uint(9401));

  public static final NodeId ExclusiveLimitAlarmType_ActiveState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(9402));

  public static final NodeId ExclusiveLimitAlarmType_ActiveState_TransitionTime =
      new NodeId(UShort.MIN, uint(9403));

  public static final NodeId ExclusiveLimitAlarmType_ActiveState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(9404));

  public static final NodeId ExclusiveLimitAlarmType_ActiveState_TrueState =
      new NodeId(UShort.MIN, uint(9405));

  public static final NodeId ExclusiveLimitAlarmType_ActiveState_FalseState =
      new NodeId(UShort.MIN, uint(9406));

  public static final NodeId ExclusiveLimitAlarmType_LimitState =
      new NodeId(UShort.MIN, uint(9455));

  public static final NodeId ExclusiveLimitAlarmType_LimitState_CurrentState =
      new NodeId(UShort.MIN, uint(9456));

  public static final NodeId ExclusiveLimitAlarmType_LimitState_CurrentState_Id =
      new NodeId(UShort.MIN, uint(9457));

  public static final NodeId ExclusiveLimitAlarmType_LimitState_CurrentState_Name =
      new NodeId(UShort.MIN, uint(9458));

  public static final NodeId ExclusiveLimitAlarmType_LimitState_CurrentState_Number =
      new NodeId(UShort.MIN, uint(9459));

  public static final NodeId ExclusiveLimitAlarmType_LimitState_CurrentState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(9460));

  public static final NodeId ExclusiveLimitAlarmType_LimitState_LastTransition =
      new NodeId(UShort.MIN, uint(9461));

  public static final NodeId ExclusiveLimitAlarmType_LimitState_LastTransition_Id =
      new NodeId(UShort.MIN, uint(9462));

  public static final NodeId ExclusiveLimitAlarmType_LimitState_LastTransition_Name =
      new NodeId(UShort.MIN, uint(9463));

  public static final NodeId ExclusiveLimitAlarmType_LimitState_LastTransition_Number =
      new NodeId(UShort.MIN, uint(9464));

  public static final NodeId ExclusiveLimitAlarmType_LimitState_LastTransition_TransitionTime =
      new NodeId(UShort.MIN, uint(9465));

  public static final NodeId ExclusiveLevelAlarmType = new NodeId(UShort.MIN, uint(9482));

  public static final NodeId ExclusiveRateOfChangeAlarmType = new NodeId(UShort.MIN, uint(9623));

  public static final NodeId ExclusiveDeviationAlarmType = new NodeId(UShort.MIN, uint(9764));

  public static final NodeId ExclusiveDeviationAlarmType_SetpointNode =
      new NodeId(UShort.MIN, uint(9905));

  public static final NodeId NonExclusiveLimitAlarmType = new NodeId(UShort.MIN, uint(9906));

  public static final NodeId NonExclusiveLimitAlarmType_ActiveState =
      new NodeId(UShort.MIN, uint(9963));

  public static final NodeId NonExclusiveLimitAlarmType_ActiveState_Id =
      new NodeId(UShort.MIN, uint(9964));

  public static final NodeId NonExclusiveLimitAlarmType_ActiveState_Name =
      new NodeId(UShort.MIN, uint(9965));

  public static final NodeId NonExclusiveLimitAlarmType_ActiveState_Number =
      new NodeId(UShort.MIN, uint(9966));

  public static final NodeId NonExclusiveLimitAlarmType_ActiveState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(9967));

  public static final NodeId NonExclusiveLimitAlarmType_ActiveState_TransitionTime =
      new NodeId(UShort.MIN, uint(9968));

  public static final NodeId NonExclusiveLimitAlarmType_ActiveState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(9969));

  public static final NodeId NonExclusiveLimitAlarmType_ActiveState_TrueState =
      new NodeId(UShort.MIN, uint(9970));

  public static final NodeId NonExclusiveLimitAlarmType_ActiveState_FalseState =
      new NodeId(UShort.MIN, uint(9971));

  public static final NodeId NonExclusiveLimitAlarmType_HighHighState =
      new NodeId(UShort.MIN, uint(10020));

  public static final NodeId NonExclusiveLimitAlarmType_HighHighState_Id =
      new NodeId(UShort.MIN, uint(10021));

  public static final NodeId NonExclusiveLimitAlarmType_HighHighState_Name =
      new NodeId(UShort.MIN, uint(10022));

  public static final NodeId NonExclusiveLimitAlarmType_HighHighState_Number =
      new NodeId(UShort.MIN, uint(10023));

  public static final NodeId NonExclusiveLimitAlarmType_HighHighState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(10024));

  public static final NodeId NonExclusiveLimitAlarmType_HighHighState_TransitionTime =
      new NodeId(UShort.MIN, uint(10025));

  public static final NodeId NonExclusiveLimitAlarmType_HighHighState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(10026));

  public static final NodeId NonExclusiveLimitAlarmType_HighHighState_TrueState =
      new NodeId(UShort.MIN, uint(10027));

  public static final NodeId NonExclusiveLimitAlarmType_HighHighState_FalseState =
      new NodeId(UShort.MIN, uint(10028));

  public static final NodeId NonExclusiveLimitAlarmType_HighState =
      new NodeId(UShort.MIN, uint(10029));

  public static final NodeId NonExclusiveLimitAlarmType_HighState_Id =
      new NodeId(UShort.MIN, uint(10030));

  public static final NodeId NonExclusiveLimitAlarmType_HighState_Name =
      new NodeId(UShort.MIN, uint(10031));

  public static final NodeId NonExclusiveLimitAlarmType_HighState_Number =
      new NodeId(UShort.MIN, uint(10032));

  public static final NodeId NonExclusiveLimitAlarmType_HighState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(10033));

  public static final NodeId NonExclusiveLimitAlarmType_HighState_TransitionTime =
      new NodeId(UShort.MIN, uint(10034));

  public static final NodeId NonExclusiveLimitAlarmType_HighState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(10035));

  public static final NodeId NonExclusiveLimitAlarmType_HighState_TrueState =
      new NodeId(UShort.MIN, uint(10036));

  public static final NodeId NonExclusiveLimitAlarmType_HighState_FalseState =
      new NodeId(UShort.MIN, uint(10037));

  public static final NodeId NonExclusiveLimitAlarmType_LowState =
      new NodeId(UShort.MIN, uint(10038));

  public static final NodeId NonExclusiveLimitAlarmType_LowState_Id =
      new NodeId(UShort.MIN, uint(10039));

  public static final NodeId NonExclusiveLimitAlarmType_LowState_Name =
      new NodeId(UShort.MIN, uint(10040));

  public static final NodeId NonExclusiveLimitAlarmType_LowState_Number =
      new NodeId(UShort.MIN, uint(10041));

  public static final NodeId NonExclusiveLimitAlarmType_LowState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(10042));

  public static final NodeId NonExclusiveLimitAlarmType_LowState_TransitionTime =
      new NodeId(UShort.MIN, uint(10043));

  public static final NodeId NonExclusiveLimitAlarmType_LowState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(10044));

  public static final NodeId NonExclusiveLimitAlarmType_LowState_TrueState =
      new NodeId(UShort.MIN, uint(10045));

  public static final NodeId NonExclusiveLimitAlarmType_LowState_FalseState =
      new NodeId(UShort.MIN, uint(10046));

  public static final NodeId NonExclusiveLimitAlarmType_LowLowState =
      new NodeId(UShort.MIN, uint(10047));

  public static final NodeId NonExclusiveLimitAlarmType_LowLowState_Id =
      new NodeId(UShort.MIN, uint(10048));

  public static final NodeId NonExclusiveLimitAlarmType_LowLowState_Name =
      new NodeId(UShort.MIN, uint(10049));

  public static final NodeId NonExclusiveLimitAlarmType_LowLowState_Number =
      new NodeId(UShort.MIN, uint(10050));

  public static final NodeId NonExclusiveLimitAlarmType_LowLowState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(10051));

  public static final NodeId NonExclusiveLimitAlarmType_LowLowState_TransitionTime =
      new NodeId(UShort.MIN, uint(10052));

  public static final NodeId NonExclusiveLimitAlarmType_LowLowState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(10053));

  public static final NodeId NonExclusiveLimitAlarmType_LowLowState_TrueState =
      new NodeId(UShort.MIN, uint(10054));

  public static final NodeId NonExclusiveLimitAlarmType_LowLowState_FalseState =
      new NodeId(UShort.MIN, uint(10055));

  public static final NodeId NonExclusiveLevelAlarmType = new NodeId(UShort.MIN, uint(10060));

  public static final NodeId NonExclusiveRateOfChangeAlarmType =
      new NodeId(UShort.MIN, uint(10214));

  public static final NodeId NonExclusiveDeviationAlarmType = new NodeId(UShort.MIN, uint(10368));

  public static final NodeId NonExclusiveDeviationAlarmType_SetpointNode =
      new NodeId(UShort.MIN, uint(10522));

  public static final NodeId DiscreteAlarmType = new NodeId(UShort.MIN, uint(10523));

  public static final NodeId OffNormalAlarmType = new NodeId(UShort.MIN, uint(10637));

  public static final NodeId TripAlarmType = new NodeId(UShort.MIN, uint(10751));

  public static final NodeId AuditConditionShelvingEventType = new NodeId(UShort.MIN, uint(11093));

  public static final NodeId TwoStateVariableType_TrueState = new NodeId(UShort.MIN, uint(11110));

  public static final NodeId TwoStateVariableType_FalseState = new NodeId(UShort.MIN, uint(11111));

  public static final NodeId ConditionType_ConditionClassId = new NodeId(UShort.MIN, uint(11112));

  public static final NodeId ConditionType_ConditionClassName = new NodeId(UShort.MIN, uint(11113));

  public static final NodeId AlarmConditionType_InputNode = new NodeId(UShort.MIN, uint(11120));

  public static final NodeId LimitAlarmType_HighHighLimit = new NodeId(UShort.MIN, uint(11124));

  public static final NodeId LimitAlarmType_HighLimit = new NodeId(UShort.MIN, uint(11125));

  public static final NodeId LimitAlarmType_LowLimit = new NodeId(UShort.MIN, uint(11126));

  public static final NodeId LimitAlarmType_LowLowLimit = new NodeId(UShort.MIN, uint(11127));

  public static final NodeId OffNormalAlarmType_NormalState = new NodeId(UShort.MIN, uint(11158));

  public static final NodeId BaseConditionClassType = new NodeId(UShort.MIN, uint(11163));

  public static final NodeId ProcessConditionClassType = new NodeId(UShort.MIN, uint(11164));

  public static final NodeId MaintenanceConditionClassType = new NodeId(UShort.MIN, uint(11165));

  public static final NodeId SystemConditionClassType = new NodeId(UShort.MIN, uint(11166));

  public static final NodeId
      HistoricalDataConfigurationType_AggregateConfiguration_TreatUncertainAsBad =
          new NodeId(UShort.MIN, uint(11168));

  public static final NodeId HistoricalDataConfigurationType_AggregateConfiguration_PercentDataBad =
      new NodeId(UShort.MIN, uint(11169));

  public static final NodeId
      HistoricalDataConfigurationType_AggregateConfiguration_PercentDataGood =
          new NodeId(UShort.MIN, uint(11170));

  public static final NodeId
      HistoricalDataConfigurationType_AggregateConfiguration_UseSlopedExtrapolation =
          new NodeId(UShort.MIN, uint(11171));

  public static final NodeId HistoryServerCapabilitiesType_AggregateFunctions =
      new NodeId(UShort.MIN, uint(11172));

  public static final NodeId AggregateConfigurationType = new NodeId(UShort.MIN, uint(11187));

  public static final NodeId AggregateConfigurationType_TreatUncertainAsBad =
      new NodeId(UShort.MIN, uint(11188));

  public static final NodeId AggregateConfigurationType_PercentDataBad =
      new NodeId(UShort.MIN, uint(11189));

  public static final NodeId AggregateConfigurationType_PercentDataGood =
      new NodeId(UShort.MIN, uint(11190));

  public static final NodeId AggregateConfigurationType_UseSlopedExtrapolation =
      new NodeId(UShort.MIN, uint(11191));

  public static final NodeId HistoryServerCapabilities = new NodeId(UShort.MIN, uint(11192));

  public static final NodeId HistoryServerCapabilities_AccessHistoryDataCapability =
      new NodeId(UShort.MIN, uint(11193));

  public static final NodeId HistoryServerCapabilities_InsertDataCapability =
      new NodeId(UShort.MIN, uint(11196));

  public static final NodeId HistoryServerCapabilities_ReplaceDataCapability =
      new NodeId(UShort.MIN, uint(11197));

  public static final NodeId HistoryServerCapabilities_UpdateDataCapability =
      new NodeId(UShort.MIN, uint(11198));

  public static final NodeId HistoryServerCapabilities_DeleteRawCapability =
      new NodeId(UShort.MIN, uint(11199));

  public static final NodeId HistoryServerCapabilities_DeleteAtTimeCapability =
      new NodeId(UShort.MIN, uint(11200));

  public static final NodeId HistoryServerCapabilities_AggregateFunctions =
      new NodeId(UShort.MIN, uint(11201));

  public static final NodeId HAConfiguration = new NodeId(UShort.MIN, uint(11202));

  public static final NodeId HAConfiguration_AggregateConfiguration =
      new NodeId(UShort.MIN, uint(11203));

  public static final NodeId HAConfiguration_AggregateConfiguration_TreatUncertainAsBad =
      new NodeId(UShort.MIN, uint(11204));

  public static final NodeId HAConfiguration_AggregateConfiguration_PercentDataBad =
      new NodeId(UShort.MIN, uint(11205));

  public static final NodeId HAConfiguration_AggregateConfiguration_PercentDataGood =
      new NodeId(UShort.MIN, uint(11206));

  public static final NodeId HAConfiguration_AggregateConfiguration_UseSlopedExtrapolation =
      new NodeId(UShort.MIN, uint(11207));

  public static final NodeId HAConfiguration_Stepped = new NodeId(UShort.MIN, uint(11208));

  public static final NodeId HAConfiguration_Definition = new NodeId(UShort.MIN, uint(11209));

  public static final NodeId HAConfiguration_MaxTimeInterval = new NodeId(UShort.MIN, uint(11210));

  public static final NodeId HAConfiguration_MinTimeInterval = new NodeId(UShort.MIN, uint(11211));

  public static final NodeId HAConfiguration_ExceptionDeviation =
      new NodeId(UShort.MIN, uint(11212));

  public static final NodeId HAConfiguration_ExceptionDeviationFormat =
      new NodeId(UShort.MIN, uint(11213));

  public static final NodeId Annotations = new NodeId(UShort.MIN, uint(11214));

  public static final NodeId HistoricalEventFilter = new NodeId(UShort.MIN, uint(11215));

  public static final NodeId ModificationInfo = new NodeId(UShort.MIN, uint(11216));

  public static final NodeId HistoryModifiedData = new NodeId(UShort.MIN, uint(11217));

  public static final NodeId ModificationInfo_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(11218));

  public static final NodeId HistoryModifiedData_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(11219));

  public static final NodeId ModificationInfo_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(11226));

  public static final NodeId HistoryModifiedData_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(11227));

  public static final NodeId HistoryUpdateType = new NodeId(UShort.MIN, uint(11234));

  public static final NodeId MultiStateValueDiscreteType = new NodeId(UShort.MIN, uint(11238));

  public static final NodeId MultiStateValueDiscreteType_EnumValues =
      new NodeId(UShort.MIN, uint(11241));

  public static final NodeId HistoryServerCapabilities_AccessHistoryEventsCapability =
      new NodeId(UShort.MIN, uint(11242));

  public static final NodeId HistoryServerCapabilitiesType_MaxReturnDataValues =
      new NodeId(UShort.MIN, uint(11268));

  public static final NodeId HistoryServerCapabilitiesType_MaxReturnEventValues =
      new NodeId(UShort.MIN, uint(11269));

  public static final NodeId HistoryServerCapabilitiesType_InsertAnnotationCapability =
      new NodeId(UShort.MIN, uint(11270));

  public static final NodeId HistoryServerCapabilities_MaxReturnDataValues =
      new NodeId(UShort.MIN, uint(11273));

  public static final NodeId HistoryServerCapabilities_MaxReturnEventValues =
      new NodeId(UShort.MIN, uint(11274));

  public static final NodeId HistoryServerCapabilities_InsertAnnotationCapability =
      new NodeId(UShort.MIN, uint(11275));

  public static final NodeId HistoryServerCapabilitiesType_InsertEventCapability =
      new NodeId(UShort.MIN, uint(11278));

  public static final NodeId HistoryServerCapabilitiesType_ReplaceEventCapability =
      new NodeId(UShort.MIN, uint(11279));

  public static final NodeId HistoryServerCapabilitiesType_UpdateEventCapability =
      new NodeId(UShort.MIN, uint(11280));

  public static final NodeId HistoryServerCapabilities_InsertEventCapability =
      new NodeId(UShort.MIN, uint(11281));

  public static final NodeId HistoryServerCapabilities_ReplaceEventCapability =
      new NodeId(UShort.MIN, uint(11282));

  public static final NodeId HistoryServerCapabilities_UpdateEventCapability =
      new NodeId(UShort.MIN, uint(11283));

  public static final NodeId AggregateFunction_TimeAverage2 = new NodeId(UShort.MIN, uint(11285));

  public static final NodeId AggregateFunction_Minimum2 = new NodeId(UShort.MIN, uint(11286));

  public static final NodeId AggregateFunction_Maximum2 = new NodeId(UShort.MIN, uint(11287));

  public static final NodeId AggregateFunction_Range2 = new NodeId(UShort.MIN, uint(11288));

  public static final NodeId AggregateFunction_WorstQuality2 = new NodeId(UShort.MIN, uint(11292));

  public static final NodeId PerformUpdateType = new NodeId(UShort.MIN, uint(11293));

  public static final NodeId UpdateStructureDataDetails = new NodeId(UShort.MIN, uint(11295));

  public static final NodeId UpdateStructureDataDetails_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(11296));

  public static final NodeId UpdateStructureDataDetails_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(11300));

  public static final NodeId AggregateFunction_Total2 = new NodeId(UShort.MIN, uint(11304));

  public static final NodeId AggregateFunction_MinimumActualTime2 =
      new NodeId(UShort.MIN, uint(11305));

  public static final NodeId AggregateFunction_MaximumActualTime2 =
      new NodeId(UShort.MIN, uint(11306));

  public static final NodeId AggregateFunction_DurationInStateZero =
      new NodeId(UShort.MIN, uint(11307));

  public static final NodeId AggregateFunction_DurationInStateNonZero =
      new NodeId(UShort.MIN, uint(11308));

  public static final NodeId Server_ServerRedundancy_CurrentServerId =
      new NodeId(UShort.MIN, uint(11312));

  public static final NodeId Server_ServerRedundancy_RedundantServerArray =
      new NodeId(UShort.MIN, uint(11313));

  public static final NodeId Server_ServerRedundancy_ServerUriArray =
      new NodeId(UShort.MIN, uint(11314));

  public static final NodeId ShelvedStateMachineType_UnshelvedToTimedShelved_TransitionNumber =
      new NodeId(UShort.MIN, uint(11322));

  public static final NodeId ShelvedStateMachineType_UnshelvedToOneShotShelved_TransitionNumber =
      new NodeId(UShort.MIN, uint(11323));

  public static final NodeId ShelvedStateMachineType_TimedShelvedToUnshelved_TransitionNumber =
      new NodeId(UShort.MIN, uint(11324));

  public static final NodeId ShelvedStateMachineType_TimedShelvedToOneShotShelved_TransitionNumber =
      new NodeId(UShort.MIN, uint(11325));

  public static final NodeId ShelvedStateMachineType_OneShotShelvedToUnshelved_TransitionNumber =
      new NodeId(UShort.MIN, uint(11326));

  public static final NodeId ShelvedStateMachineType_OneShotShelvedToTimedShelved_TransitionNumber =
      new NodeId(UShort.MIN, uint(11327));

  public static final NodeId ExclusiveLimitStateMachineType_LowLowToLow_TransitionNumber =
      new NodeId(UShort.MIN, uint(11340));

  public static final NodeId ExclusiveLimitStateMachineType_LowToLowLow_TransitionNumber =
      new NodeId(UShort.MIN, uint(11341));

  public static final NodeId ExclusiveLimitStateMachineType_HighHighToHigh_TransitionNumber =
      new NodeId(UShort.MIN, uint(11342));

  public static final NodeId ExclusiveLimitStateMachineType_HighToHighHigh_TransitionNumber =
      new NodeId(UShort.MIN, uint(11343));

  public static final NodeId AggregateFunction_StandardDeviationSample =
      new NodeId(UShort.MIN, uint(11426));

  public static final NodeId AggregateFunction_StandardDeviationPopulation =
      new NodeId(UShort.MIN, uint(11427));

  public static final NodeId AggregateFunction_VarianceSample = new NodeId(UShort.MIN, uint(11428));

  public static final NodeId AggregateFunction_VariancePopulation =
      new NodeId(UShort.MIN, uint(11429));

  public static final NodeId EnumStrings = new NodeId(UShort.MIN, uint(11432));

  public static final NodeId ValueAsText = new NodeId(UShort.MIN, uint(11433));

  public static final NodeId ProgressEventType = new NodeId(UShort.MIN, uint(11436));

  public static final NodeId SystemStatusChangeEventType = new NodeId(UShort.MIN, uint(11446));

  public static final NodeId TransitionVariableType_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(11456));

  public static final NodeId StateMachineType_LastTransition_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(11458));

  public static final NodeId FiniteStateMachineType_LastTransition_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(11459));

  public static final NodeId TransitionEventType_Transition_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(11460));

  public static final NodeId MultiStateValueDiscreteType_ValueAsText =
      new NodeId(UShort.MIN, uint(11461));

  public static final NodeId ProgramTransitionAuditEventType_Transition_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(11463));

  public static final NodeId ProgramStateMachineType_LastTransition_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(11464));

  public static final NodeId
      AlarmConditionType_ShelvingState_LastTransition_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(11466));

  public static final NodeId
      ExclusiveLimitAlarmType_LimitState_LastTransition_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(11470));

  public static final NodeId AuditActivateSessionEventType_SecureChannelId =
      new NodeId(UShort.MIN, uint(11485));

  public static final NodeId OptionSetType = new NodeId(UShort.MIN, uint(11487));

  public static final NodeId OptionSetType_OptionSetValues = new NodeId(UShort.MIN, uint(11488));

  public static final NodeId ServerType_GetMonitoredItems = new NodeId(UShort.MIN, uint(11489));

  public static final NodeId ServerType_GetMonitoredItems_InputArguments =
      new NodeId(UShort.MIN, uint(11490));

  public static final NodeId ServerType_GetMonitoredItems_OutputArguments =
      new NodeId(UShort.MIN, uint(11491));

  public static final NodeId Server_GetMonitoredItems = new NodeId(UShort.MIN, uint(11492));

  public static final NodeId Server_GetMonitoredItems_InputArguments =
      new NodeId(UShort.MIN, uint(11493));

  public static final NodeId Server_GetMonitoredItems_OutputArguments =
      new NodeId(UShort.MIN, uint(11494));

  public static final NodeId GetMonitoredItemsMethodType = new NodeId(UShort.MIN, uint(11495));

  public static final NodeId GetMonitoredItemsMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(11496));

  public static final NodeId GetMonitoredItemsMethodType_OutputArguments =
      new NodeId(UShort.MIN, uint(11497));

  public static final NodeId MaxStringLength = new NodeId(UShort.MIN, uint(11498));

  public static final NodeId HistoricalDataConfigurationType_StartOfArchive =
      new NodeId(UShort.MIN, uint(11499));

  public static final NodeId HistoricalDataConfigurationType_StartOfOnlineArchive =
      new NodeId(UShort.MIN, uint(11500));

  public static final NodeId HistoryServerCapabilitiesType_DeleteEventCapability =
      new NodeId(UShort.MIN, uint(11501));

  public static final NodeId HistoryServerCapabilities_DeleteEventCapability =
      new NodeId(UShort.MIN, uint(11502));

  public static final NodeId HAConfiguration_StartOfArchive = new NodeId(UShort.MIN, uint(11503));

  public static final NodeId HAConfiguration_StartOfOnlineArchive =
      new NodeId(UShort.MIN, uint(11504));

  public static final NodeId AggregateFunction_StartBound = new NodeId(UShort.MIN, uint(11505));

  public static final NodeId AggregateFunction_EndBound = new NodeId(UShort.MIN, uint(11506));

  public static final NodeId AggregateFunction_DeltaBounds = new NodeId(UShort.MIN, uint(11507));

  public static final NodeId ModellingRule_OptionalPlaceholder =
      new NodeId(UShort.MIN, uint(11508));

  public static final NodeId ModellingRule_MandatoryPlaceholder =
      new NodeId(UShort.MIN, uint(11510));

  public static final NodeId MaxArrayLength = new NodeId(UShort.MIN, uint(11512));

  public static final NodeId EngineeringUnits = new NodeId(UShort.MIN, uint(11513));

  public static final NodeId ServerType_ServerCapabilities_MaxArrayLength =
      new NodeId(UShort.MIN, uint(11514));

  public static final NodeId ServerType_ServerCapabilities_MaxStringLength =
      new NodeId(UShort.MIN, uint(11515));

  public static final NodeId ServerType_ServerCapabilities_OperationLimits =
      new NodeId(UShort.MIN, uint(11516));

  public static final NodeId ServerType_ServerCapabilities_OperationLimits_MaxNodesPerRead =
      new NodeId(UShort.MIN, uint(11517));

  public static final NodeId ServerType_ServerCapabilities_OperationLimits_MaxNodesPerWrite =
      new NodeId(UShort.MIN, uint(11519));

  public static final NodeId ServerType_ServerCapabilities_OperationLimits_MaxNodesPerMethodCall =
      new NodeId(UShort.MIN, uint(11521));

  public static final NodeId ServerType_ServerCapabilities_OperationLimits_MaxNodesPerBrowse =
      new NodeId(UShort.MIN, uint(11522));

  public static final NodeId
      ServerType_ServerCapabilities_OperationLimits_MaxNodesPerRegisterNodes =
          new NodeId(UShort.MIN, uint(11523));

  public static final NodeId
      ServerType_ServerCapabilities_OperationLimits_MaxNodesPerTranslateBrowsePathsToNodeIds =
          new NodeId(UShort.MIN, uint(11524));

  public static final NodeId
      ServerType_ServerCapabilities_OperationLimits_MaxNodesPerNodeManagement =
          new NodeId(UShort.MIN, uint(11525));

  public static final NodeId
      ServerType_ServerCapabilities_OperationLimits_MaxMonitoredItemsPerCall =
          new NodeId(UShort.MIN, uint(11526));

  public static final NodeId ServerType_Namespaces = new NodeId(UShort.MIN, uint(11527));

  public static final NodeId ServerCapabilitiesType_MaxArrayLength =
      new NodeId(UShort.MIN, uint(11549));

  public static final NodeId ServerCapabilitiesType_MaxStringLength =
      new NodeId(UShort.MIN, uint(11550));

  public static final NodeId ServerCapabilitiesType_OperationLimits =
      new NodeId(UShort.MIN, uint(11551));

  public static final NodeId ServerCapabilitiesType_OperationLimits_MaxNodesPerRead =
      new NodeId(UShort.MIN, uint(11552));

  public static final NodeId ServerCapabilitiesType_OperationLimits_MaxNodesPerWrite =
      new NodeId(UShort.MIN, uint(11554));

  public static final NodeId ServerCapabilitiesType_OperationLimits_MaxNodesPerMethodCall =
      new NodeId(UShort.MIN, uint(11556));

  public static final NodeId ServerCapabilitiesType_OperationLimits_MaxNodesPerBrowse =
      new NodeId(UShort.MIN, uint(11557));

  public static final NodeId ServerCapabilitiesType_OperationLimits_MaxNodesPerRegisterNodes =
      new NodeId(UShort.MIN, uint(11558));

  public static final NodeId
      ServerCapabilitiesType_OperationLimits_MaxNodesPerTranslateBrowsePathsToNodeIds =
          new NodeId(UShort.MIN, uint(11559));

  public static final NodeId ServerCapabilitiesType_OperationLimits_MaxNodesPerNodeManagement =
      new NodeId(UShort.MIN, uint(11560));

  public static final NodeId ServerCapabilitiesType_OperationLimits_MaxMonitoredItemsPerCall =
      new NodeId(UShort.MIN, uint(11561));

  public static final NodeId ServerCapabilitiesType_VendorCapability_Placeholder =
      new NodeId(UShort.MIN, uint(11562));

  public static final NodeId OperationLimitsType = new NodeId(UShort.MIN, uint(11564));

  public static final NodeId OperationLimitsType_MaxNodesPerRead =
      new NodeId(UShort.MIN, uint(11565));

  public static final NodeId OperationLimitsType_MaxNodesPerWrite =
      new NodeId(UShort.MIN, uint(11567));

  public static final NodeId OperationLimitsType_MaxNodesPerMethodCall =
      new NodeId(UShort.MIN, uint(11569));

  public static final NodeId OperationLimitsType_MaxNodesPerBrowse =
      new NodeId(UShort.MIN, uint(11570));

  public static final NodeId OperationLimitsType_MaxNodesPerRegisterNodes =
      new NodeId(UShort.MIN, uint(11571));

  public static final NodeId OperationLimitsType_MaxNodesPerTranslateBrowsePathsToNodeIds =
      new NodeId(UShort.MIN, uint(11572));

  public static final NodeId OperationLimitsType_MaxNodesPerNodeManagement =
      new NodeId(UShort.MIN, uint(11573));

  public static final NodeId OperationLimitsType_MaxMonitoredItemsPerCall =
      new NodeId(UShort.MIN, uint(11574));

  public static final NodeId FileType = new NodeId(UShort.MIN, uint(11575));

  public static final NodeId FileType_Size = new NodeId(UShort.MIN, uint(11576));

  public static final NodeId FileType_OpenCount = new NodeId(UShort.MIN, uint(11579));

  public static final NodeId FileType_Open = new NodeId(UShort.MIN, uint(11580));

  public static final NodeId FileType_Open_InputArguments = new NodeId(UShort.MIN, uint(11581));

  public static final NodeId FileType_Open_OutputArguments = new NodeId(UShort.MIN, uint(11582));

  public static final NodeId FileType_Close = new NodeId(UShort.MIN, uint(11583));

  public static final NodeId FileType_Close_InputArguments = new NodeId(UShort.MIN, uint(11584));

  public static final NodeId FileType_Read = new NodeId(UShort.MIN, uint(11585));

  public static final NodeId FileType_Read_InputArguments = new NodeId(UShort.MIN, uint(11586));

  public static final NodeId FileType_Read_OutputArguments = new NodeId(UShort.MIN, uint(11587));

  public static final NodeId FileType_Write = new NodeId(UShort.MIN, uint(11588));

  public static final NodeId FileType_Write_InputArguments = new NodeId(UShort.MIN, uint(11589));

  public static final NodeId FileType_GetPosition = new NodeId(UShort.MIN, uint(11590));

  public static final NodeId FileType_GetPosition_InputArguments =
      new NodeId(UShort.MIN, uint(11591));

  public static final NodeId FileType_GetPosition_OutputArguments =
      new NodeId(UShort.MIN, uint(11592));

  public static final NodeId FileType_SetPosition = new NodeId(UShort.MIN, uint(11593));

  public static final NodeId FileType_SetPosition_InputArguments =
      new NodeId(UShort.MIN, uint(11594));

  public static final NodeId AddressSpaceFileType = new NodeId(UShort.MIN, uint(11595));

  public static final NodeId AddressSpaceFileType_ExportNamespace =
      new NodeId(UShort.MIN, uint(11615));

  public static final NodeId NamespaceMetadataType = new NodeId(UShort.MIN, uint(11616));

  public static final NodeId NamespaceMetadataType_NamespaceUri =
      new NodeId(UShort.MIN, uint(11617));

  public static final NodeId NamespaceMetadataType_NamespaceVersion =
      new NodeId(UShort.MIN, uint(11618));

  public static final NodeId NamespaceMetadataType_NamespacePublicationDate =
      new NodeId(UShort.MIN, uint(11619));

  public static final NodeId NamespaceMetadataType_IsNamespaceSubset =
      new NodeId(UShort.MIN, uint(11620));

  public static final NodeId NamespaceMetadataType_StaticNodeIdTypes =
      new NodeId(UShort.MIN, uint(11621));

  public static final NodeId NamespaceMetadataType_StaticNumericNodeIdRange =
      new NodeId(UShort.MIN, uint(11622));

  public static final NodeId NamespaceMetadataType_StaticStringNodeIdPattern =
      new NodeId(UShort.MIN, uint(11623));

  public static final NodeId NamespaceMetadataType_NamespaceFile =
      new NodeId(UShort.MIN, uint(11624));

  public static final NodeId NamespaceMetadataType_NamespaceFile_Size =
      new NodeId(UShort.MIN, uint(11625));

  public static final NodeId NamespaceMetadataType_NamespaceFile_OpenCount =
      new NodeId(UShort.MIN, uint(11628));

  public static final NodeId NamespaceMetadataType_NamespaceFile_Open =
      new NodeId(UShort.MIN, uint(11629));

  public static final NodeId NamespaceMetadataType_NamespaceFile_Open_InputArguments =
      new NodeId(UShort.MIN, uint(11630));

  public static final NodeId NamespaceMetadataType_NamespaceFile_Open_OutputArguments =
      new NodeId(UShort.MIN, uint(11631));

  public static final NodeId NamespaceMetadataType_NamespaceFile_Close =
      new NodeId(UShort.MIN, uint(11632));

  public static final NodeId NamespaceMetadataType_NamespaceFile_Close_InputArguments =
      new NodeId(UShort.MIN, uint(11633));

  public static final NodeId NamespaceMetadataType_NamespaceFile_Read =
      new NodeId(UShort.MIN, uint(11634));

  public static final NodeId NamespaceMetadataType_NamespaceFile_Read_InputArguments =
      new NodeId(UShort.MIN, uint(11635));

  public static final NodeId NamespaceMetadataType_NamespaceFile_Read_OutputArguments =
      new NodeId(UShort.MIN, uint(11636));

  public static final NodeId NamespaceMetadataType_NamespaceFile_Write =
      new NodeId(UShort.MIN, uint(11637));

  public static final NodeId NamespaceMetadataType_NamespaceFile_Write_InputArguments =
      new NodeId(UShort.MIN, uint(11638));

  public static final NodeId NamespaceMetadataType_NamespaceFile_GetPosition =
      new NodeId(UShort.MIN, uint(11639));

  public static final NodeId NamespaceMetadataType_NamespaceFile_GetPosition_InputArguments =
      new NodeId(UShort.MIN, uint(11640));

  public static final NodeId NamespaceMetadataType_NamespaceFile_GetPosition_OutputArguments =
      new NodeId(UShort.MIN, uint(11641));

  public static final NodeId NamespaceMetadataType_NamespaceFile_SetPosition =
      new NodeId(UShort.MIN, uint(11642));

  public static final NodeId NamespaceMetadataType_NamespaceFile_SetPosition_InputArguments =
      new NodeId(UShort.MIN, uint(11643));

  public static final NodeId NamespaceMetadataType_NamespaceFile_ExportNamespace =
      new NodeId(UShort.MIN, uint(11644));

  public static final NodeId NamespacesType = new NodeId(UShort.MIN, uint(11645));

  public static final NodeId NamespacesType_NamespaceIdentifier_Placeholder =
      new NodeId(UShort.MIN, uint(11646));

  public static final NodeId NamespacesType_NamespaceIdentifier_Placeholder_NamespaceUri =
      new NodeId(UShort.MIN, uint(11647));

  public static final NodeId NamespacesType_NamespaceIdentifier_Placeholder_NamespaceVersion =
      new NodeId(UShort.MIN, uint(11648));

  public static final NodeId
      NamespacesType_NamespaceIdentifier_Placeholder_NamespacePublicationDate =
          new NodeId(UShort.MIN, uint(11649));

  public static final NodeId NamespacesType_NamespaceIdentifier_Placeholder_IsNamespaceSubset =
      new NodeId(UShort.MIN, uint(11650));

  public static final NodeId NamespacesType_NamespaceIdentifier_Placeholder_StaticNodeIdTypes =
      new NodeId(UShort.MIN, uint(11651));

  public static final NodeId
      NamespacesType_NamespaceIdentifier_Placeholder_StaticNumericNodeIdRange =
          new NodeId(UShort.MIN, uint(11652));

  public static final NodeId
      NamespacesType_NamespaceIdentifier_Placeholder_StaticStringNodeIdPattern =
          new NodeId(UShort.MIN, uint(11653));

  public static final NodeId NamespacesType_NamespaceIdentifier_Placeholder_NamespaceFile =
      new NodeId(UShort.MIN, uint(11654));

  public static final NodeId NamespacesType_NamespaceIdentifier_Placeholder_NamespaceFile_Size =
      new NodeId(UShort.MIN, uint(11655));

  public static final NodeId
      NamespacesType_NamespaceIdentifier_Placeholder_NamespaceFile_OpenCount =
          new NodeId(UShort.MIN, uint(11658));

  public static final NodeId NamespacesType_NamespaceIdentifier_Placeholder_NamespaceFile_Open =
      new NodeId(UShort.MIN, uint(11659));

  public static final NodeId
      NamespacesType_NamespaceIdentifier_Placeholder_NamespaceFile_Open_InputArguments =
          new NodeId(UShort.MIN, uint(11660));

  public static final NodeId
      NamespacesType_NamespaceIdentifier_Placeholder_NamespaceFile_Open_OutputArguments =
          new NodeId(UShort.MIN, uint(11661));

  public static final NodeId NamespacesType_NamespaceIdentifier_Placeholder_NamespaceFile_Close =
      new NodeId(UShort.MIN, uint(11662));

  public static final NodeId
      NamespacesType_NamespaceIdentifier_Placeholder_NamespaceFile_Close_InputArguments =
          new NodeId(UShort.MIN, uint(11663));

  public static final NodeId NamespacesType_NamespaceIdentifier_Placeholder_NamespaceFile_Read =
      new NodeId(UShort.MIN, uint(11664));

  public static final NodeId
      NamespacesType_NamespaceIdentifier_Placeholder_NamespaceFile_Read_InputArguments =
          new NodeId(UShort.MIN, uint(11665));

  public static final NodeId
      NamespacesType_NamespaceIdentifier_Placeholder_NamespaceFile_Read_OutputArguments =
          new NodeId(UShort.MIN, uint(11666));

  public static final NodeId NamespacesType_NamespaceIdentifier_Placeholder_NamespaceFile_Write =
      new NodeId(UShort.MIN, uint(11667));

  public static final NodeId
      NamespacesType_NamespaceIdentifier_Placeholder_NamespaceFile_Write_InputArguments =
          new NodeId(UShort.MIN, uint(11668));

  public static final NodeId
      NamespacesType_NamespaceIdentifier_Placeholder_NamespaceFile_GetPosition =
          new NodeId(UShort.MIN, uint(11669));

  public static final NodeId
      NamespacesType_NamespaceIdentifier_Placeholder_NamespaceFile_GetPosition_InputArguments =
          new NodeId(UShort.MIN, uint(11670));

  public static final NodeId
      NamespacesType_NamespaceIdentifier_Placeholder_NamespaceFile_GetPosition_OutputArguments =
          new NodeId(UShort.MIN, uint(11671));

  public static final NodeId
      NamespacesType_NamespaceIdentifier_Placeholder_NamespaceFile_SetPosition =
          new NodeId(UShort.MIN, uint(11672));

  public static final NodeId
      NamespacesType_NamespaceIdentifier_Placeholder_NamespaceFile_SetPosition_InputArguments =
          new NodeId(UShort.MIN, uint(11673));

  public static final NodeId
      NamespacesType_NamespaceIdentifier_Placeholder_NamespaceFile_ExportNamespace =
          new NodeId(UShort.MIN, uint(11674));

  public static final NodeId SystemStatusChangeEventType_SystemState =
      new NodeId(UShort.MIN, uint(11696));

  public static final NodeId SamplingIntervalDiagnosticsType_SampledMonitoredItemsCount =
      new NodeId(UShort.MIN, uint(11697));

  public static final NodeId SamplingIntervalDiagnosticsType_MaxSampledMonitoredItemsCount =
      new NodeId(UShort.MIN, uint(11698));

  public static final NodeId SamplingIntervalDiagnosticsType_DisabledMonitoredItemsSamplingCount =
      new NodeId(UShort.MIN, uint(11699));

  public static final NodeId OptionSetType_BitMask = new NodeId(UShort.MIN, uint(11701));

  public static final NodeId Server_ServerCapabilities_MaxArrayLength =
      new NodeId(UShort.MIN, uint(11702));

  public static final NodeId Server_ServerCapabilities_MaxStringLength =
      new NodeId(UShort.MIN, uint(11703));

  public static final NodeId Server_ServerCapabilities_OperationLimits =
      new NodeId(UShort.MIN, uint(11704));

  public static final NodeId Server_ServerCapabilities_OperationLimits_MaxNodesPerRead =
      new NodeId(UShort.MIN, uint(11705));

  public static final NodeId Server_ServerCapabilities_OperationLimits_MaxNodesPerWrite =
      new NodeId(UShort.MIN, uint(11707));

  public static final NodeId Server_ServerCapabilities_OperationLimits_MaxNodesPerMethodCall =
      new NodeId(UShort.MIN, uint(11709));

  public static final NodeId Server_ServerCapabilities_OperationLimits_MaxNodesPerBrowse =
      new NodeId(UShort.MIN, uint(11710));

  public static final NodeId Server_ServerCapabilities_OperationLimits_MaxNodesPerRegisterNodes =
      new NodeId(UShort.MIN, uint(11711));

  public static final NodeId
      Server_ServerCapabilities_OperationLimits_MaxNodesPerTranslateBrowsePathsToNodeIds =
          new NodeId(UShort.MIN, uint(11712));

  public static final NodeId Server_ServerCapabilities_OperationLimits_MaxNodesPerNodeManagement =
      new NodeId(UShort.MIN, uint(11713));

  public static final NodeId Server_ServerCapabilities_OperationLimits_MaxMonitoredItemsPerCall =
      new NodeId(UShort.MIN, uint(11714));

  public static final NodeId Server_Namespaces = new NodeId(UShort.MIN, uint(11715));

  public static final NodeId BitFieldMaskDataType = new NodeId(UShort.MIN, uint(11737));

  public static final NodeId OpenMethodType = new NodeId(UShort.MIN, uint(11738));

  public static final NodeId OpenMethodType_InputArguments = new NodeId(UShort.MIN, uint(11739));

  public static final NodeId OpenMethodType_OutputArguments = new NodeId(UShort.MIN, uint(11740));

  public static final NodeId CloseMethodType = new NodeId(UShort.MIN, uint(11741));

  public static final NodeId CloseMethodType_InputArguments = new NodeId(UShort.MIN, uint(11742));

  public static final NodeId ReadMethodType = new NodeId(UShort.MIN, uint(11743));

  public static final NodeId ReadMethodType_InputArguments = new NodeId(UShort.MIN, uint(11744));

  public static final NodeId ReadMethodType_OutputArguments = new NodeId(UShort.MIN, uint(11745));

  public static final NodeId WriteMethodType = new NodeId(UShort.MIN, uint(11746));

  public static final NodeId WriteMethodType_InputArguments = new NodeId(UShort.MIN, uint(11747));

  public static final NodeId GetPositionMethodType = new NodeId(UShort.MIN, uint(11748));

  public static final NodeId GetPositionMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(11749));

  public static final NodeId GetPositionMethodType_OutputArguments =
      new NodeId(UShort.MIN, uint(11750));

  public static final NodeId SetPositionMethodType = new NodeId(UShort.MIN, uint(11751));

  public static final NodeId SetPositionMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(11752));

  public static final NodeId SystemOffNormalAlarmType = new NodeId(UShort.MIN, uint(11753));

  public static final NodeId AuditConditionCommentEventType_Comment =
      new NodeId(UShort.MIN, uint(11851));

  public static final NodeId AuditConditionRespondEventType_SelectedResponse =
      new NodeId(UShort.MIN, uint(11852));

  public static final NodeId AuditConditionAcknowledgeEventType_Comment =
      new NodeId(UShort.MIN, uint(11853));

  public static final NodeId AuditConditionConfirmEventType_Comment =
      new NodeId(UShort.MIN, uint(11854));

  public static final NodeId AuditConditionShelvingEventType_ShelvingTime =
      new NodeId(UShort.MIN, uint(11855));

  public static final NodeId AuditProgramTransitionEventType = new NodeId(UShort.MIN, uint(11856));

  public static final NodeId AuditProgramTransitionEventType_TransitionNumber =
      new NodeId(UShort.MIN, uint(11875));

  public static final NodeId HistoricalDataConfigurationType_AggregateFunctions =
      new NodeId(UShort.MIN, uint(11876));

  public static final NodeId HAConfiguration_AggregateFunctions =
      new NodeId(UShort.MIN, uint(11877));

  public static final NodeId NodeClass_EnumValues = new NodeId(UShort.MIN, uint(11878));

  public static final NodeId InstanceNode = new NodeId(UShort.MIN, uint(11879));

  public static final NodeId TypeNode = new NodeId(UShort.MIN, uint(11880));

  public static final NodeId NodeAttributesMask_EnumValues = new NodeId(UShort.MIN, uint(11881));

  public static final NodeId BrowseResultMask_EnumValues = new NodeId(UShort.MIN, uint(11883));

  public static final NodeId HistoryUpdateType_EnumValues = new NodeId(UShort.MIN, uint(11884));

  public static final NodeId PerformUpdateType_EnumValues = new NodeId(UShort.MIN, uint(11885));

  public static final NodeId InstanceNode_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(11887));

  public static final NodeId TypeNode_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(11888));

  public static final NodeId InstanceNode_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(11889));

  public static final NodeId TypeNode_Encoding_DefaultBinary = new NodeId(UShort.MIN, uint(11890));

  public static final NodeId
      SessionDiagnosticsObjectType_SessionDiagnostics_UnauthorizedRequestCount =
          new NodeId(UShort.MIN, uint(11891));

  public static final NodeId SessionDiagnosticsVariableType_UnauthorizedRequestCount =
      new NodeId(UShort.MIN, uint(11892));

  public static final NodeId OpenFileMode = new NodeId(UShort.MIN, uint(11939));

  public static final NodeId OpenFileMode_EnumValues = new NodeId(UShort.MIN, uint(11940));

  public static final NodeId ModelChangeStructureVerbMask = new NodeId(UShort.MIN, uint(11941));

  public static final NodeId ModelChangeStructureVerbMask_EnumValues =
      new NodeId(UShort.MIN, uint(11942));

  public static final NodeId EndpointUrlListDataType = new NodeId(UShort.MIN, uint(11943));

  public static final NodeId NetworkGroupDataType = new NodeId(UShort.MIN, uint(11944));

  public static final NodeId NonTransparentNetworkRedundancyType =
      new NodeId(UShort.MIN, uint(11945));

  public static final NodeId NonTransparentNetworkRedundancyType_ServerNetworkGroups =
      new NodeId(UShort.MIN, uint(11948));

  public static final NodeId EndpointUrlListDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(11949));

  public static final NodeId NetworkGroupDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(11950));

  public static final NodeId OpcUa_XmlSchema_EndpointUrlListDataType =
      new NodeId(UShort.MIN, uint(11951));

  public static final NodeId OpcUa_XmlSchema_EndpointUrlListDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(11952));

  public static final NodeId OpcUa_XmlSchema_EndpointUrlListDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(11953));

  public static final NodeId OpcUa_XmlSchema_NetworkGroupDataType =
      new NodeId(UShort.MIN, uint(11954));

  public static final NodeId OpcUa_XmlSchema_NetworkGroupDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(11955));

  public static final NodeId OpcUa_XmlSchema_NetworkGroupDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(11956));

  public static final NodeId EndpointUrlListDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(11957));

  public static final NodeId NetworkGroupDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(11958));

  public static final NodeId OpcUa_BinarySchema_EndpointUrlListDataType =
      new NodeId(UShort.MIN, uint(11959));

  public static final NodeId OpcUa_BinarySchema_EndpointUrlListDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(11960));

  public static final NodeId OpcUa_BinarySchema_EndpointUrlListDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(11961));

  public static final NodeId OpcUa_BinarySchema_NetworkGroupDataType =
      new NodeId(UShort.MIN, uint(11962));

  public static final NodeId OpcUa_BinarySchema_NetworkGroupDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(11963));

  public static final NodeId OpcUa_BinarySchema_NetworkGroupDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(11964));

  public static final NodeId ArrayItemType = new NodeId(UShort.MIN, uint(12021));

  public static final NodeId ArrayItemType_InstrumentRange = new NodeId(UShort.MIN, uint(12024));

  public static final NodeId ArrayItemType_EURange = new NodeId(UShort.MIN, uint(12025));

  public static final NodeId ArrayItemType_EngineeringUnits = new NodeId(UShort.MIN, uint(12026));

  public static final NodeId ArrayItemType_Title = new NodeId(UShort.MIN, uint(12027));

  public static final NodeId ArrayItemType_AxisScaleType = new NodeId(UShort.MIN, uint(12028));

  public static final NodeId YArrayItemType = new NodeId(UShort.MIN, uint(12029));

  public static final NodeId YArrayItemType_XAxisDefinition = new NodeId(UShort.MIN, uint(12037));

  public static final NodeId XYArrayItemType = new NodeId(UShort.MIN, uint(12038));

  public static final NodeId XYArrayItemType_XAxisDefinition = new NodeId(UShort.MIN, uint(12046));

  public static final NodeId ImageItemType = new NodeId(UShort.MIN, uint(12047));

  public static final NodeId ImageItemType_XAxisDefinition = new NodeId(UShort.MIN, uint(12055));

  public static final NodeId ImageItemType_YAxisDefinition = new NodeId(UShort.MIN, uint(12056));

  public static final NodeId CubeItemType = new NodeId(UShort.MIN, uint(12057));

  public static final NodeId CubeItemType_XAxisDefinition = new NodeId(UShort.MIN, uint(12065));

  public static final NodeId CubeItemType_YAxisDefinition = new NodeId(UShort.MIN, uint(12066));

  public static final NodeId CubeItemType_ZAxisDefinition = new NodeId(UShort.MIN, uint(12067));

  public static final NodeId NDimensionArrayItemType = new NodeId(UShort.MIN, uint(12068));

  public static final NodeId NDimensionArrayItemType_AxisDefinition =
      new NodeId(UShort.MIN, uint(12076));

  public static final NodeId AxisScaleEnumeration = new NodeId(UShort.MIN, uint(12077));

  public static final NodeId AxisScaleEnumeration_EnumStrings = new NodeId(UShort.MIN, uint(12078));

  public static final NodeId AxisInformation = new NodeId(UShort.MIN, uint(12079));

  public static final NodeId XVType = new NodeId(UShort.MIN, uint(12080));

  public static final NodeId AxisInformation_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(12081));

  public static final NodeId XVType_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(12082));

  public static final NodeId OpcUa_XmlSchema_AxisInformation = new NodeId(UShort.MIN, uint(12083));

  public static final NodeId OpcUa_XmlSchema_AxisInformation_DataTypeVersion =
      new NodeId(UShort.MIN, uint(12084));

  public static final NodeId OpcUa_XmlSchema_AxisInformation_DictionaryFragment =
      new NodeId(UShort.MIN, uint(12085));

  public static final NodeId OpcUa_XmlSchema_XVType = new NodeId(UShort.MIN, uint(12086));

  public static final NodeId OpcUa_XmlSchema_XVType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(12087));

  public static final NodeId OpcUa_XmlSchema_XVType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(12088));

  public static final NodeId AxisInformation_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(12089));

  public static final NodeId XVType_Encoding_DefaultBinary = new NodeId(UShort.MIN, uint(12090));

  public static final NodeId OpcUa_BinarySchema_AxisInformation =
      new NodeId(UShort.MIN, uint(12091));

  public static final NodeId OpcUa_BinarySchema_AxisInformation_DataTypeVersion =
      new NodeId(UShort.MIN, uint(12092));

  public static final NodeId OpcUa_BinarySchema_AxisInformation_DictionaryFragment =
      new NodeId(UShort.MIN, uint(12093));

  public static final NodeId OpcUa_BinarySchema_XVType = new NodeId(UShort.MIN, uint(12094));

  public static final NodeId OpcUa_BinarySchema_XVType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(12095));

  public static final NodeId OpcUa_BinarySchema_XVType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(12096));

  public static final NodeId SessionsDiagnosticsSummaryType_ClientName_Placeholder =
      new NodeId(UShort.MIN, uint(12097));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics =
          new NodeId(UShort.MIN, uint(12098));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_SessionId =
          new NodeId(UShort.MIN, uint(12099));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_SessionName =
          new NodeId(UShort.MIN, uint(12100));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_ClientDescription =
          new NodeId(UShort.MIN, uint(12101));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_ServerUri =
          new NodeId(UShort.MIN, uint(12102));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_EndpointUrl =
          new NodeId(UShort.MIN, uint(12103));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_LocaleIds =
          new NodeId(UShort.MIN, uint(12104));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_ActualSessionTimeout =
          new NodeId(UShort.MIN, uint(12105));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_MaxResponseMessageSize =
          new NodeId(UShort.MIN, uint(12106));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_ClientConnectionTime =
          new NodeId(UShort.MIN, uint(12107));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_ClientLastContactTime =
          new NodeId(UShort.MIN, uint(12108));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_CurrentSubscriptionsCount =
          new NodeId(UShort.MIN, uint(12109));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_CurrentMonitoredItemsCount =
          new NodeId(UShort.MIN, uint(12110));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_CurrentPublishRequestsInQueue =
          new NodeId(UShort.MIN, uint(12111));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_TotalRequestCount =
          new NodeId(UShort.MIN, uint(12112));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_UnauthorizedRequestCount =
          new NodeId(UShort.MIN, uint(12113));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_ReadCount =
          new NodeId(UShort.MIN, uint(12114));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_HistoryReadCount =
          new NodeId(UShort.MIN, uint(12115));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_WriteCount =
          new NodeId(UShort.MIN, uint(12116));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_HistoryUpdateCount =
          new NodeId(UShort.MIN, uint(12117));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_CallCount =
          new NodeId(UShort.MIN, uint(12118));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_CreateMonitoredItemsCount =
          new NodeId(UShort.MIN, uint(12119));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_ModifyMonitoredItemsCount =
          new NodeId(UShort.MIN, uint(12120));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_SetMonitoringModeCount =
          new NodeId(UShort.MIN, uint(12121));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_SetTriggeringCount =
          new NodeId(UShort.MIN, uint(12122));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_DeleteMonitoredItemsCount =
          new NodeId(UShort.MIN, uint(12123));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_CreateSubscriptionCount =
          new NodeId(UShort.MIN, uint(12124));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_ModifySubscriptionCount =
          new NodeId(UShort.MIN, uint(12125));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_SetPublishingModeCount =
          new NodeId(UShort.MIN, uint(12126));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_PublishCount =
          new NodeId(UShort.MIN, uint(12127));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_RepublishCount =
          new NodeId(UShort.MIN, uint(12128));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_TransferSubscriptionsCount =
          new NodeId(UShort.MIN, uint(12129));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_DeleteSubscriptionsCount =
          new NodeId(UShort.MIN, uint(12130));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_AddNodesCount =
          new NodeId(UShort.MIN, uint(12131));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_AddReferencesCount =
          new NodeId(UShort.MIN, uint(12132));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_DeleteNodesCount =
          new NodeId(UShort.MIN, uint(12133));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_DeleteReferencesCount =
          new NodeId(UShort.MIN, uint(12134));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_BrowseCount =
          new NodeId(UShort.MIN, uint(12135));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_BrowseNextCount =
          new NodeId(UShort.MIN, uint(12136));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_TranslateBrowsePathsToNodeIdsCount =
          new NodeId(UShort.MIN, uint(12137));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_QueryFirstCount =
          new NodeId(UShort.MIN, uint(12138));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_QueryNextCount =
          new NodeId(UShort.MIN, uint(12139));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_RegisterNodesCount =
          new NodeId(UShort.MIN, uint(12140));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_UnregisterNodesCount =
          new NodeId(UShort.MIN, uint(12141));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionSecurityDiagnostics =
          new NodeId(UShort.MIN, uint(12142));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionSecurityDiagnostics_SessionId =
          new NodeId(UShort.MIN, uint(12143));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionSecurityDiagnostics_ClientUserIdOfSession =
          new NodeId(UShort.MIN, uint(12144));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionSecurityDiagnostics_ClientUserIdHistory =
          new NodeId(UShort.MIN, uint(12145));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionSecurityDiagnostics_AuthenticationMechanism =
          new NodeId(UShort.MIN, uint(12146));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionSecurityDiagnostics_Encoding =
          new NodeId(UShort.MIN, uint(12147));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionSecurityDiagnostics_TransportProtocol =
          new NodeId(UShort.MIN, uint(12148));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionSecurityDiagnostics_SecurityMode =
          new NodeId(UShort.MIN, uint(12149));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionSecurityDiagnostics_SecurityPolicyUri =
          new NodeId(UShort.MIN, uint(12150));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionSecurityDiagnostics_ClientCertificate =
          new NodeId(UShort.MIN, uint(12151));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SubscriptionDiagnosticsArray =
          new NodeId(UShort.MIN, uint(12152));

  public static final NodeId
      ServerType_ServerCapabilities_OperationLimits_MaxNodesPerHistoryReadData =
          new NodeId(UShort.MIN, uint(12153));

  public static final NodeId
      ServerType_ServerCapabilities_OperationLimits_MaxNodesPerHistoryReadEvents =
          new NodeId(UShort.MIN, uint(12154));

  public static final NodeId
      ServerType_ServerCapabilities_OperationLimits_MaxNodesPerHistoryUpdateData =
          new NodeId(UShort.MIN, uint(12155));

  public static final NodeId
      ServerType_ServerCapabilities_OperationLimits_MaxNodesPerHistoryUpdateEvents =
          new NodeId(UShort.MIN, uint(12156));

  public static final NodeId ServerCapabilitiesType_OperationLimits_MaxNodesPerHistoryReadData =
      new NodeId(UShort.MIN, uint(12157));

  public static final NodeId ServerCapabilitiesType_OperationLimits_MaxNodesPerHistoryReadEvents =
      new NodeId(UShort.MIN, uint(12158));

  public static final NodeId ServerCapabilitiesType_OperationLimits_MaxNodesPerHistoryUpdateData =
      new NodeId(UShort.MIN, uint(12159));

  public static final NodeId ServerCapabilitiesType_OperationLimits_MaxNodesPerHistoryUpdateEvents =
      new NodeId(UShort.MIN, uint(12160));

  public static final NodeId OperationLimitsType_MaxNodesPerHistoryReadData =
      new NodeId(UShort.MIN, uint(12161));

  public static final NodeId OperationLimitsType_MaxNodesPerHistoryReadEvents =
      new NodeId(UShort.MIN, uint(12162));

  public static final NodeId OperationLimitsType_MaxNodesPerHistoryUpdateData =
      new NodeId(UShort.MIN, uint(12163));

  public static final NodeId OperationLimitsType_MaxNodesPerHistoryUpdateEvents =
      new NodeId(UShort.MIN, uint(12164));

  public static final NodeId Server_ServerCapabilities_OperationLimits_MaxNodesPerHistoryReadData =
      new NodeId(UShort.MIN, uint(12165));

  public static final NodeId
      Server_ServerCapabilities_OperationLimits_MaxNodesPerHistoryReadEvents =
          new NodeId(UShort.MIN, uint(12166));

  public static final NodeId
      Server_ServerCapabilities_OperationLimits_MaxNodesPerHistoryUpdateData =
          new NodeId(UShort.MIN, uint(12167));

  public static final NodeId
      Server_ServerCapabilities_OperationLimits_MaxNodesPerHistoryUpdateEvents =
          new NodeId(UShort.MIN, uint(12168));

  public static final NodeId NamingRuleType_EnumValues = new NodeId(UShort.MIN, uint(12169));

  public static final NodeId ViewVersion = new NodeId(UShort.MIN, uint(12170));

  public static final NodeId ComplexNumberType = new NodeId(UShort.MIN, uint(12171));

  public static final NodeId DoubleComplexNumberType = new NodeId(UShort.MIN, uint(12172));

  public static final NodeId ComplexNumberType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(12173));

  public static final NodeId DoubleComplexNumberType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(12174));

  public static final NodeId OpcUa_XmlSchema_ComplexNumberType =
      new NodeId(UShort.MIN, uint(12175));

  public static final NodeId OpcUa_XmlSchema_ComplexNumberType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(12176));

  public static final NodeId OpcUa_XmlSchema_ComplexNumberType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(12177));

  public static final NodeId OpcUa_XmlSchema_DoubleComplexNumberType =
      new NodeId(UShort.MIN, uint(12178));

  public static final NodeId OpcUa_XmlSchema_DoubleComplexNumberType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(12179));

  public static final NodeId OpcUa_XmlSchema_DoubleComplexNumberType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(12180));

  public static final NodeId ComplexNumberType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(12181));

  public static final NodeId DoubleComplexNumberType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(12182));

  public static final NodeId OpcUa_BinarySchema_ComplexNumberType =
      new NodeId(UShort.MIN, uint(12183));

  public static final NodeId OpcUa_BinarySchema_ComplexNumberType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(12184));

  public static final NodeId OpcUa_BinarySchema_ComplexNumberType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(12185));

  public static final NodeId OpcUa_BinarySchema_DoubleComplexNumberType =
      new NodeId(UShort.MIN, uint(12186));

  public static final NodeId OpcUa_BinarySchema_DoubleComplexNumberType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(12187));

  public static final NodeId OpcUa_BinarySchema_DoubleComplexNumberType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(12188));

  public static final NodeId ServerOnNetwork = new NodeId(UShort.MIN, uint(12189));

  public static final NodeId FindServersOnNetworkRequest = new NodeId(UShort.MIN, uint(12190));

  public static final NodeId FindServersOnNetworkResponse = new NodeId(UShort.MIN, uint(12191));

  public static final NodeId RegisterServer2Request = new NodeId(UShort.MIN, uint(12193));

  public static final NodeId RegisterServer2Response = new NodeId(UShort.MIN, uint(12194));

  public static final NodeId ServerOnNetwork_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(12195));

  public static final NodeId FindServersOnNetworkRequest_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(12196));

  public static final NodeId FindServersOnNetworkResponse_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(12197));

  public static final NodeId RegisterServer2Request_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(12199));

  public static final NodeId RegisterServer2Response_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(12200));

  public static final NodeId OpcUa_XmlSchema_ServerOnNetwork = new NodeId(UShort.MIN, uint(12201));

  public static final NodeId OpcUa_XmlSchema_ServerOnNetwork_DataTypeVersion =
      new NodeId(UShort.MIN, uint(12202));

  public static final NodeId OpcUa_XmlSchema_ServerOnNetwork_DictionaryFragment =
      new NodeId(UShort.MIN, uint(12203));

  public static final NodeId ServerOnNetwork_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(12207));

  public static final NodeId FindServersOnNetworkRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(12208));

  public static final NodeId FindServersOnNetworkResponse_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(12209));

  public static final NodeId RegisterServer2Request_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(12211));

  public static final NodeId RegisterServer2Response_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(12212));

  public static final NodeId OpcUa_BinarySchema_ServerOnNetwork =
      new NodeId(UShort.MIN, uint(12213));

  public static final NodeId OpcUa_BinarySchema_ServerOnNetwork_DataTypeVersion =
      new NodeId(UShort.MIN, uint(12214));

  public static final NodeId OpcUa_BinarySchema_ServerOnNetwork_DictionaryFragment =
      new NodeId(UShort.MIN, uint(12215));

  public static final NodeId ProgressEventType_Context = new NodeId(UShort.MIN, uint(12502));

  public static final NodeId ProgressEventType_Progress = new NodeId(UShort.MIN, uint(12503));

  public static final NodeId OpenWithMasksMethodType = new NodeId(UShort.MIN, uint(12513));

  public static final NodeId OpenWithMasksMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(12514));

  public static final NodeId OpenWithMasksMethodType_OutputArguments =
      new NodeId(UShort.MIN, uint(12515));

  public static final NodeId CloseAndUpdateMethodType = new NodeId(UShort.MIN, uint(12516));

  public static final NodeId CloseAndUpdateMethodType_OutputArguments =
      new NodeId(UShort.MIN, uint(12517));

  public static final NodeId AddCertificateMethodType = new NodeId(UShort.MIN, uint(12518));

  public static final NodeId AddCertificateMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(12519));

  public static final NodeId RemoveCertificateMethodType = new NodeId(UShort.MIN, uint(12520));

  public static final NodeId RemoveCertificateMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(12521));

  public static final NodeId TrustListType = new NodeId(UShort.MIN, uint(12522));

  public static final NodeId TrustListType_LastUpdateTime = new NodeId(UShort.MIN, uint(12542));

  public static final NodeId TrustListType_OpenWithMasks = new NodeId(UShort.MIN, uint(12543));

  public static final NodeId TrustListType_OpenWithMasks_InputArguments =
      new NodeId(UShort.MIN, uint(12544));

  public static final NodeId TrustListType_OpenWithMasks_OutputArguments =
      new NodeId(UShort.MIN, uint(12545));

  public static final NodeId TrustListType_CloseAndUpdate = new NodeId(UShort.MIN, uint(12546));

  public static final NodeId TrustListType_CloseAndUpdate_OutputArguments =
      new NodeId(UShort.MIN, uint(12547));

  public static final NodeId TrustListType_AddCertificate = new NodeId(UShort.MIN, uint(12548));

  public static final NodeId TrustListType_AddCertificate_InputArguments =
      new NodeId(UShort.MIN, uint(12549));

  public static final NodeId TrustListType_RemoveCertificate = new NodeId(UShort.MIN, uint(12550));

  public static final NodeId TrustListType_RemoveCertificate_InputArguments =
      new NodeId(UShort.MIN, uint(12551));

  public static final NodeId TrustListMasks = new NodeId(UShort.MIN, uint(12552));

  public static final NodeId TrustListMasks_EnumValues = new NodeId(UShort.MIN, uint(12553));

  public static final NodeId TrustListDataType = new NodeId(UShort.MIN, uint(12554));

  public static final NodeId CertificateGroupType = new NodeId(UShort.MIN, uint(12555));

  public static final NodeId CertificateType = new NodeId(UShort.MIN, uint(12556));

  public static final NodeId ApplicationCertificateType = new NodeId(UShort.MIN, uint(12557));

  public static final NodeId HttpsCertificateType = new NodeId(UShort.MIN, uint(12558));

  public static final NodeId RsaMinApplicationCertificateType = new NodeId(UShort.MIN, uint(12559));

  public static final NodeId RsaSha256ApplicationCertificateType =
      new NodeId(UShort.MIN, uint(12560));

  public static final NodeId TrustListUpdatedAuditEventType = new NodeId(UShort.MIN, uint(12561));

  public static final NodeId UpdateCertificateMethodType = new NodeId(UShort.MIN, uint(12578));

  public static final NodeId UpdateCertificateMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(12579));

  public static final NodeId UpdateCertificateMethodType_OutputArguments =
      new NodeId(UShort.MIN, uint(12580));

  public static final NodeId ServerConfigurationType = new NodeId(UShort.MIN, uint(12581));

  public static final NodeId ServerConfigurationType_SupportedPrivateKeyFormats =
      new NodeId(UShort.MIN, uint(12583));

  public static final NodeId ServerConfigurationType_MaxTrustListSize =
      new NodeId(UShort.MIN, uint(12584));

  public static final NodeId ServerConfigurationType_MulticastDnsEnabled =
      new NodeId(UShort.MIN, uint(12585));

  public static final NodeId ServerConfigurationType_UpdateCertificate =
      new NodeId(UShort.MIN, uint(12616));

  public static final NodeId ServerConfigurationType_UpdateCertificate_InputArguments =
      new NodeId(UShort.MIN, uint(12617));

  public static final NodeId ServerConfigurationType_UpdateCertificate_OutputArguments =
      new NodeId(UShort.MIN, uint(12618));

  public static final NodeId CertificateUpdatedAuditEventType = new NodeId(UShort.MIN, uint(12620));

  public static final NodeId ServerConfiguration = new NodeId(UShort.MIN, uint(12637));

  public static final NodeId ServerConfiguration_SupportedPrivateKeyFormats =
      new NodeId(UShort.MIN, uint(12639));

  public static final NodeId ServerConfiguration_MaxTrustListSize =
      new NodeId(UShort.MIN, uint(12640));

  public static final NodeId ServerConfiguration_MulticastDnsEnabled =
      new NodeId(UShort.MIN, uint(12641));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList =
          new NodeId(UShort.MIN, uint(12642));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_Size =
          new NodeId(UShort.MIN, uint(12643));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_OpenCount =
          new NodeId(UShort.MIN, uint(12646));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_Open =
          new NodeId(UShort.MIN, uint(12647));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_Open_InputArguments =
          new NodeId(UShort.MIN, uint(12648));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_Open_OutputArguments =
          new NodeId(UShort.MIN, uint(12649));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_Close =
          new NodeId(UShort.MIN, uint(12650));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_Close_InputArguments =
          new NodeId(UShort.MIN, uint(12651));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_Read =
          new NodeId(UShort.MIN, uint(12652));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_Read_InputArguments =
          new NodeId(UShort.MIN, uint(12653));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_Read_OutputArguments =
          new NodeId(UShort.MIN, uint(12654));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_Write =
          new NodeId(UShort.MIN, uint(12655));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_Write_InputArguments =
          new NodeId(UShort.MIN, uint(12656));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_GetPosition =
          new NodeId(UShort.MIN, uint(12657));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_GetPosition_InputArguments =
          new NodeId(UShort.MIN, uint(12658));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_GetPosition_OutputArguments =
          new NodeId(UShort.MIN, uint(12659));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_SetPosition =
          new NodeId(UShort.MIN, uint(12660));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_SetPosition_InputArguments =
          new NodeId(UShort.MIN, uint(12661));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_LastUpdateTime =
          new NodeId(UShort.MIN, uint(12662));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_OpenWithMasks =
          new NodeId(UShort.MIN, uint(12663));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_OpenWithMasks_InputArguments =
          new NodeId(UShort.MIN, uint(12664));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_OpenWithMasks_OutputArguments =
          new NodeId(UShort.MIN, uint(12665));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_CloseAndUpdate =
          new NodeId(UShort.MIN, uint(12666));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_CloseAndUpdate_OutputArguments =
          new NodeId(UShort.MIN, uint(12667));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_AddCertificate =
          new NodeId(UShort.MIN, uint(12668));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_AddCertificate_InputArguments =
          new NodeId(UShort.MIN, uint(12669));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_RemoveCertificate =
          new NodeId(UShort.MIN, uint(12670));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_RemoveCertificate_InputArguments =
          new NodeId(UShort.MIN, uint(12671));

  public static final NodeId TrustListDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(12676));

  public static final NodeId OpcUa_XmlSchema_TrustListDataType =
      new NodeId(UShort.MIN, uint(12677));

  public static final NodeId OpcUa_XmlSchema_TrustListDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(12678));

  public static final NodeId OpcUa_XmlSchema_TrustListDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(12679));

  public static final NodeId TrustListDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(12680));

  public static final NodeId OpcUa_BinarySchema_TrustListDataType =
      new NodeId(UShort.MIN, uint(12681));

  public static final NodeId OpcUa_BinarySchema_TrustListDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(12682));

  public static final NodeId OpcUa_BinarySchema_TrustListDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(12683));

  public static final NodeId FileType_Writable = new NodeId(UShort.MIN, uint(12686));

  public static final NodeId FileType_UserWritable = new NodeId(UShort.MIN, uint(12687));

  public static final NodeId NamespaceMetadataType_NamespaceFile_Writable =
      new NodeId(UShort.MIN, uint(12690));

  public static final NodeId NamespaceMetadataType_NamespaceFile_UserWritable =
      new NodeId(UShort.MIN, uint(12691));

  public static final NodeId NamespacesType_NamespaceIdentifier_Placeholder_NamespaceFile_Writable =
      new NodeId(UShort.MIN, uint(12692));

  public static final NodeId
      NamespacesType_NamespaceIdentifier_Placeholder_NamespaceFile_UserWritable =
          new NodeId(UShort.MIN, uint(12693));

  public static final NodeId CloseAndUpdateMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(12704));

  public static final NodeId TrustListType_CloseAndUpdate_InputArguments =
      new NodeId(UShort.MIN, uint(12705));

  public static final NodeId ServerConfigurationType_ServerCapabilities =
      new NodeId(UShort.MIN, uint(12708));

  public static final NodeId ServerConfiguration_ServerCapabilities =
      new NodeId(UShort.MIN, uint(12710));

  public static final NodeId OpcUa_XmlSchema_RelativePathElement =
      new NodeId(UShort.MIN, uint(12712));

  public static final NodeId OpcUa_XmlSchema_RelativePathElement_DataTypeVersion =
      new NodeId(UShort.MIN, uint(12713));

  public static final NodeId OpcUa_XmlSchema_RelativePathElement_DictionaryFragment =
      new NodeId(UShort.MIN, uint(12714));

  public static final NodeId OpcUa_XmlSchema_RelativePath = new NodeId(UShort.MIN, uint(12715));

  public static final NodeId OpcUa_XmlSchema_RelativePath_DataTypeVersion =
      new NodeId(UShort.MIN, uint(12716));

  public static final NodeId OpcUa_XmlSchema_RelativePath_DictionaryFragment =
      new NodeId(UShort.MIN, uint(12717));

  public static final NodeId OpcUa_BinarySchema_RelativePathElement =
      new NodeId(UShort.MIN, uint(12718));

  public static final NodeId OpcUa_BinarySchema_RelativePathElement_DataTypeVersion =
      new NodeId(UShort.MIN, uint(12719));

  public static final NodeId OpcUa_BinarySchema_RelativePathElement_DictionaryFragment =
      new NodeId(UShort.MIN, uint(12720));

  public static final NodeId OpcUa_BinarySchema_RelativePath = new NodeId(UShort.MIN, uint(12721));

  public static final NodeId OpcUa_BinarySchema_RelativePath_DataTypeVersion =
      new NodeId(UShort.MIN, uint(12722));

  public static final NodeId OpcUa_BinarySchema_RelativePath_DictionaryFragment =
      new NodeId(UShort.MIN, uint(12723));
}

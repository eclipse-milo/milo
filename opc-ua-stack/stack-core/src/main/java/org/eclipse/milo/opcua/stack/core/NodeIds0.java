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

  public static final NodeId AnalogItemType_InstrumentRange = new NodeId(UShort.MIN, uint(2370));

  public static final NodeId AnalogItemType_EngineeringUnits = new NodeId(UShort.MIN, uint(2371));

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

  public static final NodeId TransparentRedundancyType_RedundancySupport =
      new NodeId(UShort.MIN, uint(3188));

  public static final NodeId NonTransparentRedundancyType_RedundancySupport =
      new NodeId(UShort.MIN, uint(3189));

  public static final NodeId BaseEventType_LocalTime = new NodeId(UShort.MIN, uint(3190));

  public static final NodeId EventQueueOverflowEventType_EventId =
      new NodeId(UShort.MIN, uint(3191));

  public static final NodeId EventQueueOverflowEventType_EventType =
      new NodeId(UShort.MIN, uint(3192));

  public static final NodeId EventQueueOverflowEventType_SourceNode =
      new NodeId(UShort.MIN, uint(3193));

  public static final NodeId EventQueueOverflowEventType_SourceName =
      new NodeId(UShort.MIN, uint(3194));

  public static final NodeId EventQueueOverflowEventType_Time = new NodeId(UShort.MIN, uint(3195));

  public static final NodeId EventQueueOverflowEventType_ReceiveTime =
      new NodeId(UShort.MIN, uint(3196));

  public static final NodeId EventQueueOverflowEventType_LocalTime =
      new NodeId(UShort.MIN, uint(3197));

  public static final NodeId EventQueueOverflowEventType_Message =
      new NodeId(UShort.MIN, uint(3198));

  public static final NodeId EventQueueOverflowEventType_Severity =
      new NodeId(UShort.MIN, uint(3199));

  public static final NodeId AuditEventType_EventId = new NodeId(UShort.MIN, uint(3200));

  public static final NodeId AuditEventType_EventType = new NodeId(UShort.MIN, uint(3201));

  public static final NodeId AuditEventType_SourceNode = new NodeId(UShort.MIN, uint(3202));

  public static final NodeId AuditEventType_SourceName = new NodeId(UShort.MIN, uint(3203));

  public static final NodeId AuditEventType_Time = new NodeId(UShort.MIN, uint(3204));

  public static final NodeId AuditEventType_ReceiveTime = new NodeId(UShort.MIN, uint(3205));

  public static final NodeId AuditEventType_LocalTime = new NodeId(UShort.MIN, uint(3206));

  public static final NodeId AuditEventType_Message = new NodeId(UShort.MIN, uint(3207));

  public static final NodeId AuditEventType_Severity = new NodeId(UShort.MIN, uint(3208));

  public static final NodeId AuditSecurityEventType_EventId = new NodeId(UShort.MIN, uint(3209));

  public static final NodeId AuditSecurityEventType_EventType = new NodeId(UShort.MIN, uint(3210));

  public static final NodeId AuditSecurityEventType_SourceNode = new NodeId(UShort.MIN, uint(3211));

  public static final NodeId AuditSecurityEventType_SourceName = new NodeId(UShort.MIN, uint(3212));

  public static final NodeId AuditSecurityEventType_Time = new NodeId(UShort.MIN, uint(3213));

  public static final NodeId AuditSecurityEventType_ReceiveTime =
      new NodeId(UShort.MIN, uint(3214));

  public static final NodeId AuditSecurityEventType_LocalTime = new NodeId(UShort.MIN, uint(3215));

  public static final NodeId AuditSecurityEventType_Message = new NodeId(UShort.MIN, uint(3216));

  public static final NodeId AuditSecurityEventType_Severity = new NodeId(UShort.MIN, uint(3217));

  public static final NodeId AuditSecurityEventType_ActionTimeStamp =
      new NodeId(UShort.MIN, uint(3218));

  public static final NodeId AuditSecurityEventType_Status = new NodeId(UShort.MIN, uint(3219));

  public static final NodeId AuditSecurityEventType_ServerId = new NodeId(UShort.MIN, uint(3220));

  public static final NodeId AuditSecurityEventType_ClientAuditEntryId =
      new NodeId(UShort.MIN, uint(3221));

  public static final NodeId AuditSecurityEventType_ClientUserId =
      new NodeId(UShort.MIN, uint(3222));

  public static final NodeId AuditChannelEventType_EventId = new NodeId(UShort.MIN, uint(3223));

  public static final NodeId AuditChannelEventType_EventType = new NodeId(UShort.MIN, uint(3224));

  public static final NodeId AuditChannelEventType_SourceNode = new NodeId(UShort.MIN, uint(3225));

  public static final NodeId AuditChannelEventType_SourceName = new NodeId(UShort.MIN, uint(3226));

  public static final NodeId AuditChannelEventType_Time = new NodeId(UShort.MIN, uint(3227));

  public static final NodeId AuditChannelEventType_ReceiveTime = new NodeId(UShort.MIN, uint(3228));

  public static final NodeId AuditChannelEventType_LocalTime = new NodeId(UShort.MIN, uint(3229));

  public static final NodeId AuditChannelEventType_Message = new NodeId(UShort.MIN, uint(3230));

  public static final NodeId AuditChannelEventType_Severity = new NodeId(UShort.MIN, uint(3231));

  public static final NodeId AuditChannelEventType_ActionTimeStamp =
      new NodeId(UShort.MIN, uint(3232));

  public static final NodeId AuditChannelEventType_Status = new NodeId(UShort.MIN, uint(3233));

  public static final NodeId AuditChannelEventType_ServerId = new NodeId(UShort.MIN, uint(3234));

  public static final NodeId AuditChannelEventType_ClientAuditEntryId =
      new NodeId(UShort.MIN, uint(3235));

  public static final NodeId AuditChannelEventType_ClientUserId =
      new NodeId(UShort.MIN, uint(3236));

  public static final NodeId AuditOpenSecureChannelEventType_EventId =
      new NodeId(UShort.MIN, uint(3237));

  public static final NodeId AuditOpenSecureChannelEventType_EventType =
      new NodeId(UShort.MIN, uint(3238));

  public static final NodeId AuditOpenSecureChannelEventType_SourceNode =
      new NodeId(UShort.MIN, uint(3239));

  public static final NodeId AuditOpenSecureChannelEventType_SourceName =
      new NodeId(UShort.MIN, uint(3240));

  public static final NodeId AuditOpenSecureChannelEventType_Time =
      new NodeId(UShort.MIN, uint(3241));

  public static final NodeId AuditOpenSecureChannelEventType_ReceiveTime =
      new NodeId(UShort.MIN, uint(3242));

  public static final NodeId AuditOpenSecureChannelEventType_LocalTime =
      new NodeId(UShort.MIN, uint(3243));

  public static final NodeId AuditOpenSecureChannelEventType_Message =
      new NodeId(UShort.MIN, uint(3244));

  public static final NodeId AuditOpenSecureChannelEventType_Severity =
      new NodeId(UShort.MIN, uint(3245));

  public static final NodeId AuditOpenSecureChannelEventType_ActionTimeStamp =
      new NodeId(UShort.MIN, uint(3246));

  public static final NodeId AuditOpenSecureChannelEventType_Status =
      new NodeId(UShort.MIN, uint(3247));

  public static final NodeId AuditOpenSecureChannelEventType_ServerId =
      new NodeId(UShort.MIN, uint(3248));

  public static final NodeId AuditOpenSecureChannelEventType_ClientAuditEntryId =
      new NodeId(UShort.MIN, uint(3249));

  public static final NodeId AuditOpenSecureChannelEventType_ClientUserId =
      new NodeId(UShort.MIN, uint(3250));

  public static final NodeId AuditOpenSecureChannelEventType_SecureChannelId =
      new NodeId(UShort.MIN, uint(3251));

  public static final NodeId AuditSessionEventType_EventId = new NodeId(UShort.MIN, uint(3252));

  public static final NodeId AuditSessionEventType_EventType = new NodeId(UShort.MIN, uint(3253));

  public static final NodeId AuditSessionEventType_SourceNode = new NodeId(UShort.MIN, uint(3254));

  public static final NodeId AuditSessionEventType_SourceName = new NodeId(UShort.MIN, uint(3255));

  public static final NodeId AuditSessionEventType_Time = new NodeId(UShort.MIN, uint(3256));

  public static final NodeId AuditSessionEventType_ReceiveTime = new NodeId(UShort.MIN, uint(3257));

  public static final NodeId AuditSessionEventType_LocalTime = new NodeId(UShort.MIN, uint(3258));

  public static final NodeId AuditSessionEventType_Message = new NodeId(UShort.MIN, uint(3259));

  public static final NodeId AuditSessionEventType_Severity = new NodeId(UShort.MIN, uint(3260));

  public static final NodeId AuditSessionEventType_ActionTimeStamp =
      new NodeId(UShort.MIN, uint(3261));

  public static final NodeId AuditSessionEventType_Status = new NodeId(UShort.MIN, uint(3262));

  public static final NodeId AuditSessionEventType_ServerId = new NodeId(UShort.MIN, uint(3263));

  public static final NodeId AuditSessionEventType_ClientAuditEntryId =
      new NodeId(UShort.MIN, uint(3264));

  public static final NodeId AuditSessionEventType_ClientUserId =
      new NodeId(UShort.MIN, uint(3265));

  public static final NodeId AuditCreateSessionEventType_EventId =
      new NodeId(UShort.MIN, uint(3266));

  public static final NodeId AuditCreateSessionEventType_EventType =
      new NodeId(UShort.MIN, uint(3267));

  public static final NodeId AuditCreateSessionEventType_SourceNode =
      new NodeId(UShort.MIN, uint(3268));

  public static final NodeId AuditCreateSessionEventType_SourceName =
      new NodeId(UShort.MIN, uint(3269));

  public static final NodeId AuditCreateSessionEventType_Time = new NodeId(UShort.MIN, uint(3270));

  public static final NodeId AuditCreateSessionEventType_ReceiveTime =
      new NodeId(UShort.MIN, uint(3271));

  public static final NodeId AuditCreateSessionEventType_LocalTime =
      new NodeId(UShort.MIN, uint(3272));

  public static final NodeId AuditCreateSessionEventType_Message =
      new NodeId(UShort.MIN, uint(3273));

  public static final NodeId AuditCreateSessionEventType_Severity =
      new NodeId(UShort.MIN, uint(3274));

  public static final NodeId AuditCreateSessionEventType_ActionTimeStamp =
      new NodeId(UShort.MIN, uint(3275));

  public static final NodeId AuditCreateSessionEventType_Status =
      new NodeId(UShort.MIN, uint(3276));

  public static final NodeId AuditCreateSessionEventType_ServerId =
      new NodeId(UShort.MIN, uint(3277));

  public static final NodeId AuditCreateSessionEventType_ClientAuditEntryId =
      new NodeId(UShort.MIN, uint(3278));

  public static final NodeId AuditCreateSessionEventType_ClientUserId =
      new NodeId(UShort.MIN, uint(3279));

  public static final NodeId AuditUrlMismatchEventType_EventId = new NodeId(UShort.MIN, uint(3281));

  public static final NodeId AuditUrlMismatchEventType_EventType =
      new NodeId(UShort.MIN, uint(3282));

  public static final NodeId AuditUrlMismatchEventType_SourceNode =
      new NodeId(UShort.MIN, uint(3283));

  public static final NodeId AuditUrlMismatchEventType_SourceName =
      new NodeId(UShort.MIN, uint(3284));

  public static final NodeId AuditUrlMismatchEventType_Time = new NodeId(UShort.MIN, uint(3285));

  public static final NodeId AuditUrlMismatchEventType_ReceiveTime =
      new NodeId(UShort.MIN, uint(3286));

  public static final NodeId AuditUrlMismatchEventType_LocalTime =
      new NodeId(UShort.MIN, uint(3287));

  public static final NodeId AuditUrlMismatchEventType_Message = new NodeId(UShort.MIN, uint(3288));

  public static final NodeId AuditUrlMismatchEventType_Severity =
      new NodeId(UShort.MIN, uint(3289));

  public static final NodeId AuditUrlMismatchEventType_ActionTimeStamp =
      new NodeId(UShort.MIN, uint(3290));

  public static final NodeId AuditUrlMismatchEventType_Status = new NodeId(UShort.MIN, uint(3291));

  public static final NodeId AuditUrlMismatchEventType_ServerId =
      new NodeId(UShort.MIN, uint(3292));

  public static final NodeId AuditUrlMismatchEventType_ClientAuditEntryId =
      new NodeId(UShort.MIN, uint(3293));

  public static final NodeId AuditUrlMismatchEventType_ClientUserId =
      new NodeId(UShort.MIN, uint(3294));

  public static final NodeId AuditUrlMismatchEventType_SecureChannelId =
      new NodeId(UShort.MIN, uint(3296));

  public static final NodeId AuditUrlMismatchEventType_ClientCertificate =
      new NodeId(UShort.MIN, uint(3297));

  public static final NodeId AuditUrlMismatchEventType_ClientCertificateThumbprint =
      new NodeId(UShort.MIN, uint(3298));

  public static final NodeId AuditUrlMismatchEventType_RevisedSessionTimeout =
      new NodeId(UShort.MIN, uint(3299));

  public static final NodeId AuditActivateSessionEventType_EventId =
      new NodeId(UShort.MIN, uint(3300));

  public static final NodeId AuditActivateSessionEventType_EventType =
      new NodeId(UShort.MIN, uint(3301));

  public static final NodeId AuditActivateSessionEventType_SourceNode =
      new NodeId(UShort.MIN, uint(3302));

  public static final NodeId AuditActivateSessionEventType_SourceName =
      new NodeId(UShort.MIN, uint(3303));

  public static final NodeId AuditActivateSessionEventType_Time =
      new NodeId(UShort.MIN, uint(3304));

  public static final NodeId AuditActivateSessionEventType_ReceiveTime =
      new NodeId(UShort.MIN, uint(3305));

  public static final NodeId AuditActivateSessionEventType_LocalTime =
      new NodeId(UShort.MIN, uint(3306));

  public static final NodeId AuditActivateSessionEventType_Message =
      new NodeId(UShort.MIN, uint(3307));

  public static final NodeId AuditActivateSessionEventType_Severity =
      new NodeId(UShort.MIN, uint(3308));

  public static final NodeId AuditActivateSessionEventType_ActionTimeStamp =
      new NodeId(UShort.MIN, uint(3309));

  public static final NodeId AuditActivateSessionEventType_Status =
      new NodeId(UShort.MIN, uint(3310));

  public static final NodeId AuditActivateSessionEventType_ServerId =
      new NodeId(UShort.MIN, uint(3311));

  public static final NodeId AuditActivateSessionEventType_ClientAuditEntryId =
      new NodeId(UShort.MIN, uint(3312));

  public static final NodeId AuditActivateSessionEventType_ClientUserId =
      new NodeId(UShort.MIN, uint(3313));

  public static final NodeId AuditActivateSessionEventType_SessionId =
      new NodeId(UShort.MIN, uint(3314));

  public static final NodeId AuditCancelEventType_EventId = new NodeId(UShort.MIN, uint(3315));

  public static final NodeId AuditCancelEventType_EventType = new NodeId(UShort.MIN, uint(3316));

  public static final NodeId AuditCancelEventType_SourceNode = new NodeId(UShort.MIN, uint(3317));

  public static final NodeId AuditCancelEventType_SourceName = new NodeId(UShort.MIN, uint(3318));

  public static final NodeId AuditCancelEventType_Time = new NodeId(UShort.MIN, uint(3319));

  public static final NodeId AuditCancelEventType_ReceiveTime = new NodeId(UShort.MIN, uint(3320));

  public static final NodeId AuditCancelEventType_LocalTime = new NodeId(UShort.MIN, uint(3321));

  public static final NodeId AuditCancelEventType_Message = new NodeId(UShort.MIN, uint(3322));

  public static final NodeId AuditCancelEventType_Severity = new NodeId(UShort.MIN, uint(3323));

  public static final NodeId AuditCancelEventType_ActionTimeStamp =
      new NodeId(UShort.MIN, uint(3324));

  public static final NodeId AuditCancelEventType_Status = new NodeId(UShort.MIN, uint(3325));

  public static final NodeId AuditCancelEventType_ServerId = new NodeId(UShort.MIN, uint(3326));

  public static final NodeId AuditCancelEventType_ClientAuditEntryId =
      new NodeId(UShort.MIN, uint(3327));

  public static final NodeId AuditCancelEventType_ClientUserId = new NodeId(UShort.MIN, uint(3328));

  public static final NodeId AuditCancelEventType_SessionId = new NodeId(UShort.MIN, uint(3329));

  public static final NodeId AuditCertificateEventType_EventId = new NodeId(UShort.MIN, uint(3330));

  public static final NodeId AuditCertificateEventType_EventType =
      new NodeId(UShort.MIN, uint(3331));

  public static final NodeId AuditCertificateEventType_SourceNode =
      new NodeId(UShort.MIN, uint(3332));

  public static final NodeId AuditCertificateEventType_SourceName =
      new NodeId(UShort.MIN, uint(3333));

  public static final NodeId AuditCertificateEventType_Time = new NodeId(UShort.MIN, uint(3334));

  public static final NodeId AuditCertificateEventType_ReceiveTime =
      new NodeId(UShort.MIN, uint(3335));

  public static final NodeId AuditCertificateEventType_LocalTime =
      new NodeId(UShort.MIN, uint(3336));

  public static final NodeId AuditCertificateEventType_Message = new NodeId(UShort.MIN, uint(3337));

  public static final NodeId AuditCertificateEventType_Severity =
      new NodeId(UShort.MIN, uint(3338));

  public static final NodeId AuditCertificateEventType_ActionTimeStamp =
      new NodeId(UShort.MIN, uint(3339));

  public static final NodeId AuditCertificateEventType_Status = new NodeId(UShort.MIN, uint(3340));

  public static final NodeId AuditCertificateEventType_ServerId =
      new NodeId(UShort.MIN, uint(3341));

  public static final NodeId AuditCertificateEventType_ClientAuditEntryId =
      new NodeId(UShort.MIN, uint(3342));

  public static final NodeId AuditCertificateEventType_ClientUserId =
      new NodeId(UShort.MIN, uint(3343));

  public static final NodeId AuditCertificateDataMismatchEventType_EventId =
      new NodeId(UShort.MIN, uint(3344));

  public static final NodeId AuditCertificateDataMismatchEventType_EventType =
      new NodeId(UShort.MIN, uint(3345));

  public static final NodeId AuditCertificateDataMismatchEventType_SourceNode =
      new NodeId(UShort.MIN, uint(3346));

  public static final NodeId AuditCertificateDataMismatchEventType_SourceName =
      new NodeId(UShort.MIN, uint(3347));

  public static final NodeId AuditCertificateDataMismatchEventType_Time =
      new NodeId(UShort.MIN, uint(3348));

  public static final NodeId AuditCertificateDataMismatchEventType_ReceiveTime =
      new NodeId(UShort.MIN, uint(3349));

  public static final NodeId AuditCertificateDataMismatchEventType_LocalTime =
      new NodeId(UShort.MIN, uint(3350));

  public static final NodeId AuditCertificateDataMismatchEventType_Message =
      new NodeId(UShort.MIN, uint(3351));

  public static final NodeId AuditCertificateDataMismatchEventType_Severity =
      new NodeId(UShort.MIN, uint(3352));

  public static final NodeId AuditCertificateDataMismatchEventType_ActionTimeStamp =
      new NodeId(UShort.MIN, uint(3353));

  public static final NodeId AuditCertificateDataMismatchEventType_Status =
      new NodeId(UShort.MIN, uint(3354));

  public static final NodeId AuditCertificateDataMismatchEventType_ServerId =
      new NodeId(UShort.MIN, uint(3355));

  public static final NodeId AuditCertificateDataMismatchEventType_ClientAuditEntryId =
      new NodeId(UShort.MIN, uint(3356));

  public static final NodeId AuditCertificateDataMismatchEventType_ClientUserId =
      new NodeId(UShort.MIN, uint(3357));

  public static final NodeId AuditCertificateDataMismatchEventType_Certificate =
      new NodeId(UShort.MIN, uint(3358));

  public static final NodeId AuditCertificateExpiredEventType_EventId =
      new NodeId(UShort.MIN, uint(3359));

  public static final NodeId AuditCertificateExpiredEventType_EventType =
      new NodeId(UShort.MIN, uint(3360));

  public static final NodeId AuditCertificateExpiredEventType_SourceNode =
      new NodeId(UShort.MIN, uint(3361));

  public static final NodeId AuditCertificateExpiredEventType_SourceName =
      new NodeId(UShort.MIN, uint(3362));

  public static final NodeId AuditCertificateExpiredEventType_Time =
      new NodeId(UShort.MIN, uint(3363));

  public static final NodeId AuditCertificateExpiredEventType_ReceiveTime =
      new NodeId(UShort.MIN, uint(3364));

  public static final NodeId AuditCertificateExpiredEventType_LocalTime =
      new NodeId(UShort.MIN, uint(3365));

  public static final NodeId AuditCertificateExpiredEventType_Message =
      new NodeId(UShort.MIN, uint(3366));

  public static final NodeId AuditCertificateExpiredEventType_Severity =
      new NodeId(UShort.MIN, uint(3367));

  public static final NodeId AuditCertificateExpiredEventType_ActionTimeStamp =
      new NodeId(UShort.MIN, uint(3368));

  public static final NodeId AuditCertificateExpiredEventType_Status =
      new NodeId(UShort.MIN, uint(3369));

  public static final NodeId AuditCertificateExpiredEventType_ServerId =
      new NodeId(UShort.MIN, uint(3370));

  public static final NodeId AuditCertificateExpiredEventType_ClientAuditEntryId =
      new NodeId(UShort.MIN, uint(3371));

  public static final NodeId AuditCertificateExpiredEventType_ClientUserId =
      new NodeId(UShort.MIN, uint(3372));

  public static final NodeId AuditCertificateExpiredEventType_Certificate =
      new NodeId(UShort.MIN, uint(3373));

  public static final NodeId AuditCertificateInvalidEventType_EventId =
      new NodeId(UShort.MIN, uint(3374));

  public static final NodeId AuditCertificateInvalidEventType_EventType =
      new NodeId(UShort.MIN, uint(3375));

  public static final NodeId AuditCertificateInvalidEventType_SourceNode =
      new NodeId(UShort.MIN, uint(3376));

  public static final NodeId AuditCertificateInvalidEventType_SourceName =
      new NodeId(UShort.MIN, uint(3377));

  public static final NodeId AuditCertificateInvalidEventType_Time =
      new NodeId(UShort.MIN, uint(3378));

  public static final NodeId AuditCertificateInvalidEventType_ReceiveTime =
      new NodeId(UShort.MIN, uint(3379));

  public static final NodeId AuditCertificateInvalidEventType_LocalTime =
      new NodeId(UShort.MIN, uint(3380));

  public static final NodeId AuditCertificateInvalidEventType_Message =
      new NodeId(UShort.MIN, uint(3381));

  public static final NodeId AuditCertificateInvalidEventType_Severity =
      new NodeId(UShort.MIN, uint(3382));

  public static final NodeId AuditCertificateInvalidEventType_ActionTimeStamp =
      new NodeId(UShort.MIN, uint(3383));

  public static final NodeId AuditCertificateInvalidEventType_Status =
      new NodeId(UShort.MIN, uint(3384));

  public static final NodeId AuditCertificateInvalidEventType_ServerId =
      new NodeId(UShort.MIN, uint(3385));

  public static final NodeId AuditCertificateInvalidEventType_ClientAuditEntryId =
      new NodeId(UShort.MIN, uint(3386));

  public static final NodeId AuditCertificateInvalidEventType_ClientUserId =
      new NodeId(UShort.MIN, uint(3387));

  public static final NodeId AuditCertificateInvalidEventType_Certificate =
      new NodeId(UShort.MIN, uint(3388));

  public static final NodeId AuditCertificateUntrustedEventType_EventId =
      new NodeId(UShort.MIN, uint(3389));

  public static final NodeId AuditCertificateUntrustedEventType_EventType =
      new NodeId(UShort.MIN, uint(3390));

  public static final NodeId AuditCertificateUntrustedEventType_SourceNode =
      new NodeId(UShort.MIN, uint(3391));

  public static final NodeId AuditCertificateUntrustedEventType_SourceName =
      new NodeId(UShort.MIN, uint(3392));

  public static final NodeId AuditCertificateUntrustedEventType_Time =
      new NodeId(UShort.MIN, uint(3393));

  public static final NodeId AuditCertificateUntrustedEventType_ReceiveTime =
      new NodeId(UShort.MIN, uint(3394));

  public static final NodeId AuditCertificateUntrustedEventType_LocalTime =
      new NodeId(UShort.MIN, uint(3395));

  public static final NodeId AuditCertificateUntrustedEventType_Message =
      new NodeId(UShort.MIN, uint(3396));

  public static final NodeId AuditCertificateUntrustedEventType_Severity =
      new NodeId(UShort.MIN, uint(3397));

  public static final NodeId AuditCertificateUntrustedEventType_ActionTimeStamp =
      new NodeId(UShort.MIN, uint(3398));

  public static final NodeId AuditCertificateUntrustedEventType_Status =
      new NodeId(UShort.MIN, uint(3399));

  public static final NodeId AuditCertificateUntrustedEventType_ServerId =
      new NodeId(UShort.MIN, uint(3400));

  public static final NodeId AuditCertificateUntrustedEventType_ClientAuditEntryId =
      new NodeId(UShort.MIN, uint(3401));

  public static final NodeId AuditCertificateUntrustedEventType_ClientUserId =
      new NodeId(UShort.MIN, uint(3402));

  public static final NodeId AuditCertificateUntrustedEventType_Certificate =
      new NodeId(UShort.MIN, uint(3403));

  public static final NodeId AuditCertificateRevokedEventType_EventId =
      new NodeId(UShort.MIN, uint(3404));

  public static final NodeId AuditCertificateRevokedEventType_EventType =
      new NodeId(UShort.MIN, uint(3405));

  public static final NodeId AuditCertificateRevokedEventType_SourceNode =
      new NodeId(UShort.MIN, uint(3406));

  public static final NodeId AuditCertificateRevokedEventType_SourceName =
      new NodeId(UShort.MIN, uint(3407));

  public static final NodeId AuditCertificateRevokedEventType_Time =
      new NodeId(UShort.MIN, uint(3408));

  public static final NodeId AuditCertificateRevokedEventType_ReceiveTime =
      new NodeId(UShort.MIN, uint(3409));

  public static final NodeId AuditCertificateRevokedEventType_LocalTime =
      new NodeId(UShort.MIN, uint(3410));

  public static final NodeId AuditCertificateRevokedEventType_Message =
      new NodeId(UShort.MIN, uint(3411));

  public static final NodeId AuditCertificateRevokedEventType_Severity =
      new NodeId(UShort.MIN, uint(3412));

  public static final NodeId AuditCertificateRevokedEventType_ActionTimeStamp =
      new NodeId(UShort.MIN, uint(3413));

  public static final NodeId AuditCertificateRevokedEventType_Status =
      new NodeId(UShort.MIN, uint(3414));

  public static final NodeId AuditCertificateRevokedEventType_ServerId =
      new NodeId(UShort.MIN, uint(3415));

  public static final NodeId AuditCertificateRevokedEventType_ClientAuditEntryId =
      new NodeId(UShort.MIN, uint(3416));

  public static final NodeId AuditCertificateRevokedEventType_ClientUserId =
      new NodeId(UShort.MIN, uint(3417));

  public static final NodeId AuditCertificateRevokedEventType_Certificate =
      new NodeId(UShort.MIN, uint(3418));

  public static final NodeId AuditCertificateMismatchEventType_EventId =
      new NodeId(UShort.MIN, uint(3419));

  public static final NodeId AuditCertificateMismatchEventType_EventType =
      new NodeId(UShort.MIN, uint(3420));

  public static final NodeId AuditCertificateMismatchEventType_SourceNode =
      new NodeId(UShort.MIN, uint(3421));

  public static final NodeId AuditCertificateMismatchEventType_SourceName =
      new NodeId(UShort.MIN, uint(3422));

  public static final NodeId AuditCertificateMismatchEventType_Time =
      new NodeId(UShort.MIN, uint(3423));

  public static final NodeId AuditCertificateMismatchEventType_ReceiveTime =
      new NodeId(UShort.MIN, uint(3424));

  public static final NodeId AuditCertificateMismatchEventType_LocalTime =
      new NodeId(UShort.MIN, uint(3425));

  public static final NodeId AuditCertificateMismatchEventType_Message =
      new NodeId(UShort.MIN, uint(3426));

  public static final NodeId AuditCertificateMismatchEventType_Severity =
      new NodeId(UShort.MIN, uint(3427));

  public static final NodeId AuditCertificateMismatchEventType_ActionTimeStamp =
      new NodeId(UShort.MIN, uint(3428));

  public static final NodeId AuditCertificateMismatchEventType_Status =
      new NodeId(UShort.MIN, uint(3429));

  public static final NodeId AuditCertificateMismatchEventType_ServerId =
      new NodeId(UShort.MIN, uint(3430));

  public static final NodeId AuditCertificateMismatchEventType_ClientAuditEntryId =
      new NodeId(UShort.MIN, uint(3431));

  public static final NodeId AuditCertificateMismatchEventType_ClientUserId =
      new NodeId(UShort.MIN, uint(3432));

  public static final NodeId AuditCertificateMismatchEventType_Certificate =
      new NodeId(UShort.MIN, uint(3433));

  public static final NodeId AuditNodeManagementEventType_EventId =
      new NodeId(UShort.MIN, uint(3434));

  public static final NodeId AuditNodeManagementEventType_EventType =
      new NodeId(UShort.MIN, uint(3435));

  public static final NodeId AuditNodeManagementEventType_SourceNode =
      new NodeId(UShort.MIN, uint(3436));

  public static final NodeId AuditNodeManagementEventType_SourceName =
      new NodeId(UShort.MIN, uint(3437));

  public static final NodeId AuditNodeManagementEventType_Time = new NodeId(UShort.MIN, uint(3438));

  public static final NodeId AuditNodeManagementEventType_ReceiveTime =
      new NodeId(UShort.MIN, uint(3439));

  public static final NodeId AuditNodeManagementEventType_LocalTime =
      new NodeId(UShort.MIN, uint(3440));

  public static final NodeId AuditNodeManagementEventType_Message =
      new NodeId(UShort.MIN, uint(3441));

  public static final NodeId AuditNodeManagementEventType_Severity =
      new NodeId(UShort.MIN, uint(3442));

  public static final NodeId AuditNodeManagementEventType_ActionTimeStamp =
      new NodeId(UShort.MIN, uint(3443));

  public static final NodeId AuditNodeManagementEventType_Status =
      new NodeId(UShort.MIN, uint(3444));

  public static final NodeId AuditNodeManagementEventType_ServerId =
      new NodeId(UShort.MIN, uint(3445));

  public static final NodeId AuditNodeManagementEventType_ClientAuditEntryId =
      new NodeId(UShort.MIN, uint(3446));

  public static final NodeId AuditNodeManagementEventType_ClientUserId =
      new NodeId(UShort.MIN, uint(3447));

  public static final NodeId AuditAddNodesEventType_EventId = new NodeId(UShort.MIN, uint(3448));

  public static final NodeId AuditAddNodesEventType_EventType = new NodeId(UShort.MIN, uint(3449));

  public static final NodeId AuditAddNodesEventType_SourceNode = new NodeId(UShort.MIN, uint(3450));

  public static final NodeId AuditAddNodesEventType_SourceName = new NodeId(UShort.MIN, uint(3451));

  public static final NodeId AuditAddNodesEventType_Time = new NodeId(UShort.MIN, uint(3452));

  public static final NodeId AuditAddNodesEventType_ReceiveTime =
      new NodeId(UShort.MIN, uint(3453));

  public static final NodeId AuditAddNodesEventType_LocalTime = new NodeId(UShort.MIN, uint(3454));

  public static final NodeId AuditAddNodesEventType_Message = new NodeId(UShort.MIN, uint(3455));

  public static final NodeId AuditAddNodesEventType_Severity = new NodeId(UShort.MIN, uint(3456));

  public static final NodeId AuditAddNodesEventType_ActionTimeStamp =
      new NodeId(UShort.MIN, uint(3457));

  public static final NodeId AuditAddNodesEventType_Status = new NodeId(UShort.MIN, uint(3458));

  public static final NodeId AuditAddNodesEventType_ServerId = new NodeId(UShort.MIN, uint(3459));

  public static final NodeId AuditAddNodesEventType_ClientAuditEntryId =
      new NodeId(UShort.MIN, uint(3460));

  public static final NodeId AuditAddNodesEventType_ClientUserId =
      new NodeId(UShort.MIN, uint(3461));

  public static final NodeId AuditDeleteNodesEventType_EventId = new NodeId(UShort.MIN, uint(3462));

  public static final NodeId AuditDeleteNodesEventType_EventType =
      new NodeId(UShort.MIN, uint(3463));

  public static final NodeId AuditDeleteNodesEventType_SourceNode =
      new NodeId(UShort.MIN, uint(3464));

  public static final NodeId AuditDeleteNodesEventType_SourceName =
      new NodeId(UShort.MIN, uint(3465));

  public static final NodeId AuditDeleteNodesEventType_Time = new NodeId(UShort.MIN, uint(3466));

  public static final NodeId AuditDeleteNodesEventType_ReceiveTime =
      new NodeId(UShort.MIN, uint(3467));

  public static final NodeId AuditDeleteNodesEventType_LocalTime =
      new NodeId(UShort.MIN, uint(3468));

  public static final NodeId AuditDeleteNodesEventType_Message = new NodeId(UShort.MIN, uint(3469));

  public static final NodeId AuditDeleteNodesEventType_Severity =
      new NodeId(UShort.MIN, uint(3470));

  public static final NodeId AuditDeleteNodesEventType_ActionTimeStamp =
      new NodeId(UShort.MIN, uint(3471));

  public static final NodeId AuditDeleteNodesEventType_Status = new NodeId(UShort.MIN, uint(3472));

  public static final NodeId AuditDeleteNodesEventType_ServerId =
      new NodeId(UShort.MIN, uint(3473));

  public static final NodeId AuditDeleteNodesEventType_ClientAuditEntryId =
      new NodeId(UShort.MIN, uint(3474));

  public static final NodeId AuditDeleteNodesEventType_ClientUserId =
      new NodeId(UShort.MIN, uint(3475));

  public static final NodeId AuditAddReferencesEventType_EventId =
      new NodeId(UShort.MIN, uint(3476));

  public static final NodeId AuditAddReferencesEventType_EventType =
      new NodeId(UShort.MIN, uint(3477));

  public static final NodeId AuditAddReferencesEventType_SourceNode =
      new NodeId(UShort.MIN, uint(3478));

  public static final NodeId AuditAddReferencesEventType_SourceName =
      new NodeId(UShort.MIN, uint(3479));

  public static final NodeId AuditAddReferencesEventType_Time = new NodeId(UShort.MIN, uint(3480));

  public static final NodeId AuditAddReferencesEventType_ReceiveTime =
      new NodeId(UShort.MIN, uint(3481));

  public static final NodeId AuditAddReferencesEventType_LocalTime =
      new NodeId(UShort.MIN, uint(3482));

  public static final NodeId AuditAddReferencesEventType_Message =
      new NodeId(UShort.MIN, uint(3483));

  public static final NodeId AuditAddReferencesEventType_Severity =
      new NodeId(UShort.MIN, uint(3484));

  public static final NodeId AuditAddReferencesEventType_ActionTimeStamp =
      new NodeId(UShort.MIN, uint(3485));

  public static final NodeId AuditAddReferencesEventType_Status =
      new NodeId(UShort.MIN, uint(3486));

  public static final NodeId AuditAddReferencesEventType_ServerId =
      new NodeId(UShort.MIN, uint(3487));

  public static final NodeId AuditAddReferencesEventType_ClientAuditEntryId =
      new NodeId(UShort.MIN, uint(3488));

  public static final NodeId AuditAddReferencesEventType_ClientUserId =
      new NodeId(UShort.MIN, uint(3489));

  public static final NodeId AuditDeleteReferencesEventType_EventId =
      new NodeId(UShort.MIN, uint(3490));

  public static final NodeId AuditDeleteReferencesEventType_EventType =
      new NodeId(UShort.MIN, uint(3491));

  public static final NodeId AuditDeleteReferencesEventType_SourceNode =
      new NodeId(UShort.MIN, uint(3492));

  public static final NodeId AuditDeleteReferencesEventType_SourceName =
      new NodeId(UShort.MIN, uint(3493));

  public static final NodeId AuditDeleteReferencesEventType_Time =
      new NodeId(UShort.MIN, uint(3494));

  public static final NodeId AuditDeleteReferencesEventType_ReceiveTime =
      new NodeId(UShort.MIN, uint(3495));

  public static final NodeId AuditDeleteReferencesEventType_LocalTime =
      new NodeId(UShort.MIN, uint(3496));

  public static final NodeId AuditDeleteReferencesEventType_Message =
      new NodeId(UShort.MIN, uint(3497));

  public static final NodeId AuditDeleteReferencesEventType_Severity =
      new NodeId(UShort.MIN, uint(3498));

  public static final NodeId AuditDeleteReferencesEventType_ActionTimeStamp =
      new NodeId(UShort.MIN, uint(3499));

  public static final NodeId AuditDeleteReferencesEventType_Status =
      new NodeId(UShort.MIN, uint(3500));

  public static final NodeId AuditDeleteReferencesEventType_ServerId =
      new NodeId(UShort.MIN, uint(3501));

  public static final NodeId AuditDeleteReferencesEventType_ClientAuditEntryId =
      new NodeId(UShort.MIN, uint(3502));

  public static final NodeId AuditDeleteReferencesEventType_ClientUserId =
      new NodeId(UShort.MIN, uint(3503));

  public static final NodeId AuditUpdateEventType_EventId = new NodeId(UShort.MIN, uint(3504));

  public static final NodeId AuditUpdateEventType_EventType = new NodeId(UShort.MIN, uint(3505));

  public static final NodeId AuditUpdateEventType_SourceNode = new NodeId(UShort.MIN, uint(3506));

  public static final NodeId AuditUpdateEventType_SourceName = new NodeId(UShort.MIN, uint(3507));

  public static final NodeId AuditUpdateEventType_Time = new NodeId(UShort.MIN, uint(3508));

  public static final NodeId AuditUpdateEventType_ReceiveTime = new NodeId(UShort.MIN, uint(3509));

  public static final NodeId AuditUpdateEventType_LocalTime = new NodeId(UShort.MIN, uint(3510));

  public static final NodeId AuditUpdateEventType_Message = new NodeId(UShort.MIN, uint(3511));

  public static final NodeId AuditUpdateEventType_Severity = new NodeId(UShort.MIN, uint(3512));

  public static final NodeId AuditUpdateEventType_ActionTimeStamp =
      new NodeId(UShort.MIN, uint(3513));

  public static final NodeId AuditUpdateEventType_Status = new NodeId(UShort.MIN, uint(3514));

  public static final NodeId AuditUpdateEventType_ServerId = new NodeId(UShort.MIN, uint(3515));

  public static final NodeId AuditUpdateEventType_ClientAuditEntryId =
      new NodeId(UShort.MIN, uint(3516));

  public static final NodeId AuditUpdateEventType_ClientUserId = new NodeId(UShort.MIN, uint(3517));

  public static final NodeId AuditWriteUpdateEventType_EventId = new NodeId(UShort.MIN, uint(3518));

  public static final NodeId AuditWriteUpdateEventType_EventType =
      new NodeId(UShort.MIN, uint(3519));

  public static final NodeId AuditWriteUpdateEventType_SourceNode =
      new NodeId(UShort.MIN, uint(3520));

  public static final NodeId AuditWriteUpdateEventType_SourceName =
      new NodeId(UShort.MIN, uint(3521));

  public static final NodeId AuditWriteUpdateEventType_Time = new NodeId(UShort.MIN, uint(3522));

  public static final NodeId AuditWriteUpdateEventType_ReceiveTime =
      new NodeId(UShort.MIN, uint(3523));

  public static final NodeId AuditWriteUpdateEventType_LocalTime =
      new NodeId(UShort.MIN, uint(3524));

  public static final NodeId AuditWriteUpdateEventType_Message = new NodeId(UShort.MIN, uint(3525));

  public static final NodeId AuditWriteUpdateEventType_Severity =
      new NodeId(UShort.MIN, uint(3526));

  public static final NodeId AuditWriteUpdateEventType_ActionTimeStamp =
      new NodeId(UShort.MIN, uint(3527));

  public static final NodeId AuditWriteUpdateEventType_Status = new NodeId(UShort.MIN, uint(3528));

  public static final NodeId AuditWriteUpdateEventType_ServerId =
      new NodeId(UShort.MIN, uint(3529));

  public static final NodeId AuditWriteUpdateEventType_ClientAuditEntryId =
      new NodeId(UShort.MIN, uint(3530));

  public static final NodeId AuditWriteUpdateEventType_ClientUserId =
      new NodeId(UShort.MIN, uint(3531));

  public static final NodeId AuditHistoryUpdateEventType_EventId =
      new NodeId(UShort.MIN, uint(3532));

  public static final NodeId AuditHistoryUpdateEventType_EventType =
      new NodeId(UShort.MIN, uint(3533));

  public static final NodeId AuditHistoryUpdateEventType_SourceNode =
      new NodeId(UShort.MIN, uint(3534));

  public static final NodeId AuditHistoryUpdateEventType_SourceName =
      new NodeId(UShort.MIN, uint(3535));

  public static final NodeId AuditHistoryUpdateEventType_Time = new NodeId(UShort.MIN, uint(3536));

  public static final NodeId AuditHistoryUpdateEventType_ReceiveTime =
      new NodeId(UShort.MIN, uint(3537));

  public static final NodeId AuditHistoryUpdateEventType_LocalTime =
      new NodeId(UShort.MIN, uint(3538));

  public static final NodeId AuditHistoryUpdateEventType_Message =
      new NodeId(UShort.MIN, uint(3539));

  public static final NodeId AuditHistoryUpdateEventType_Severity =
      new NodeId(UShort.MIN, uint(3540));

  public static final NodeId AuditHistoryUpdateEventType_ActionTimeStamp =
      new NodeId(UShort.MIN, uint(3541));

  public static final NodeId AuditHistoryUpdateEventType_Status =
      new NodeId(UShort.MIN, uint(3542));

  public static final NodeId AuditHistoryUpdateEventType_ServerId =
      new NodeId(UShort.MIN, uint(3543));

  public static final NodeId AuditHistoryUpdateEventType_ClientAuditEntryId =
      new NodeId(UShort.MIN, uint(3544));

  public static final NodeId AuditHistoryUpdateEventType_ClientUserId =
      new NodeId(UShort.MIN, uint(3545));

  public static final NodeId AuditHistoryEventUpdateEventType_EventId =
      new NodeId(UShort.MIN, uint(3546));

  public static final NodeId AuditHistoryEventUpdateEventType_EventType =
      new NodeId(UShort.MIN, uint(3547));

  public static final NodeId AuditHistoryEventUpdateEventType_SourceNode =
      new NodeId(UShort.MIN, uint(3548));

  public static final NodeId AuditHistoryEventUpdateEventType_SourceName =
      new NodeId(UShort.MIN, uint(3549));

  public static final NodeId AuditHistoryEventUpdateEventType_Time =
      new NodeId(UShort.MIN, uint(3550));

  public static final NodeId AuditHistoryEventUpdateEventType_ReceiveTime =
      new NodeId(UShort.MIN, uint(3551));

  public static final NodeId AuditHistoryEventUpdateEventType_LocalTime =
      new NodeId(UShort.MIN, uint(3552));

  public static final NodeId AuditHistoryEventUpdateEventType_Message =
      new NodeId(UShort.MIN, uint(3553));

  public static final NodeId AuditHistoryEventUpdateEventType_Severity =
      new NodeId(UShort.MIN, uint(3554));

  public static final NodeId AuditHistoryEventUpdateEventType_ActionTimeStamp =
      new NodeId(UShort.MIN, uint(3555));

  public static final NodeId AuditHistoryEventUpdateEventType_Status =
      new NodeId(UShort.MIN, uint(3556));

  public static final NodeId AuditHistoryEventUpdateEventType_ServerId =
      new NodeId(UShort.MIN, uint(3557));

  public static final NodeId AuditHistoryEventUpdateEventType_ClientAuditEntryId =
      new NodeId(UShort.MIN, uint(3558));

  public static final NodeId AuditHistoryEventUpdateEventType_ClientUserId =
      new NodeId(UShort.MIN, uint(3559));

  public static final NodeId AuditHistoryEventUpdateEventType_ParameterDataTypeId =
      new NodeId(UShort.MIN, uint(3560));

  public static final NodeId AuditHistoryValueUpdateEventType_EventId =
      new NodeId(UShort.MIN, uint(3561));

  public static final NodeId AuditHistoryValueUpdateEventType_EventType =
      new NodeId(UShort.MIN, uint(3562));

  public static final NodeId AuditHistoryValueUpdateEventType_SourceNode =
      new NodeId(UShort.MIN, uint(3563));

  public static final NodeId AuditHistoryValueUpdateEventType_SourceName =
      new NodeId(UShort.MIN, uint(3564));

  public static final NodeId AuditHistoryValueUpdateEventType_Time =
      new NodeId(UShort.MIN, uint(3565));

  public static final NodeId AuditHistoryValueUpdateEventType_ReceiveTime =
      new NodeId(UShort.MIN, uint(3566));

  public static final NodeId AuditHistoryValueUpdateEventType_LocalTime =
      new NodeId(UShort.MIN, uint(3567));

  public static final NodeId AuditHistoryValueUpdateEventType_Message =
      new NodeId(UShort.MIN, uint(3568));

  public static final NodeId AuditHistoryValueUpdateEventType_Severity =
      new NodeId(UShort.MIN, uint(3569));

  public static final NodeId AuditHistoryValueUpdateEventType_ActionTimeStamp =
      new NodeId(UShort.MIN, uint(3570));

  public static final NodeId AuditHistoryValueUpdateEventType_Status =
      new NodeId(UShort.MIN, uint(3571));

  public static final NodeId AuditHistoryValueUpdateEventType_ServerId =
      new NodeId(UShort.MIN, uint(3572));

  public static final NodeId AuditHistoryValueUpdateEventType_ClientAuditEntryId =
      new NodeId(UShort.MIN, uint(3573));

  public static final NodeId AuditHistoryValueUpdateEventType_ClientUserId =
      new NodeId(UShort.MIN, uint(3574));

  public static final NodeId AuditHistoryValueUpdateEventType_ParameterDataTypeId =
      new NodeId(UShort.MIN, uint(3575));

  public static final NodeId AuditHistoryDeleteEventType_EventId =
      new NodeId(UShort.MIN, uint(3576));

  public static final NodeId AuditHistoryDeleteEventType_EventType =
      new NodeId(UShort.MIN, uint(3577));

  public static final NodeId AuditHistoryDeleteEventType_SourceNode =
      new NodeId(UShort.MIN, uint(3578));

  public static final NodeId AuditHistoryDeleteEventType_SourceName =
      new NodeId(UShort.MIN, uint(3579));

  public static final NodeId AuditHistoryDeleteEventType_Time = new NodeId(UShort.MIN, uint(3580));

  public static final NodeId AuditHistoryDeleteEventType_ReceiveTime =
      new NodeId(UShort.MIN, uint(3581));

  public static final NodeId AuditHistoryDeleteEventType_LocalTime =
      new NodeId(UShort.MIN, uint(3582));

  public static final NodeId AuditHistoryDeleteEventType_Message =
      new NodeId(UShort.MIN, uint(3583));

  public static final NodeId AuditHistoryDeleteEventType_Severity =
      new NodeId(UShort.MIN, uint(3584));

  public static final NodeId AuditHistoryDeleteEventType_ActionTimeStamp =
      new NodeId(UShort.MIN, uint(3585));

  public static final NodeId AuditHistoryDeleteEventType_Status =
      new NodeId(UShort.MIN, uint(3586));

  public static final NodeId AuditHistoryDeleteEventType_ServerId =
      new NodeId(UShort.MIN, uint(3587));

  public static final NodeId AuditHistoryDeleteEventType_ClientAuditEntryId =
      new NodeId(UShort.MIN, uint(3588));

  public static final NodeId AuditHistoryDeleteEventType_ClientUserId =
      new NodeId(UShort.MIN, uint(3589));

  public static final NodeId AuditHistoryDeleteEventType_ParameterDataTypeId =
      new NodeId(UShort.MIN, uint(3590));

  public static final NodeId AuditHistoryRawModifyDeleteEventType_EventId =
      new NodeId(UShort.MIN, uint(3591));

  public static final NodeId AuditHistoryRawModifyDeleteEventType_EventType =
      new NodeId(UShort.MIN, uint(3592));

  public static final NodeId AuditHistoryRawModifyDeleteEventType_SourceNode =
      new NodeId(UShort.MIN, uint(3593));

  public static final NodeId AuditHistoryRawModifyDeleteEventType_SourceName =
      new NodeId(UShort.MIN, uint(3594));

  public static final NodeId AuditHistoryRawModifyDeleteEventType_Time =
      new NodeId(UShort.MIN, uint(3595));

  public static final NodeId AuditHistoryRawModifyDeleteEventType_ReceiveTime =
      new NodeId(UShort.MIN, uint(3596));

  public static final NodeId AuditHistoryRawModifyDeleteEventType_LocalTime =
      new NodeId(UShort.MIN, uint(3597));

  public static final NodeId AuditHistoryRawModifyDeleteEventType_Message =
      new NodeId(UShort.MIN, uint(3598));

  public static final NodeId AuditHistoryRawModifyDeleteEventType_Severity =
      new NodeId(UShort.MIN, uint(3599));

  public static final NodeId AuditHistoryRawModifyDeleteEventType_ActionTimeStamp =
      new NodeId(UShort.MIN, uint(3600));

  public static final NodeId AuditHistoryRawModifyDeleteEventType_Status =
      new NodeId(UShort.MIN, uint(3601));

  public static final NodeId AuditHistoryRawModifyDeleteEventType_ServerId =
      new NodeId(UShort.MIN, uint(3602));

  public static final NodeId AuditHistoryRawModifyDeleteEventType_ClientAuditEntryId =
      new NodeId(UShort.MIN, uint(3603));

  public static final NodeId AuditHistoryRawModifyDeleteEventType_ClientUserId =
      new NodeId(UShort.MIN, uint(3604));

  public static final NodeId AuditHistoryRawModifyDeleteEventType_ParameterDataTypeId =
      new NodeId(UShort.MIN, uint(3605));

  public static final NodeId AuditHistoryRawModifyDeleteEventType_UpdatedNode =
      new NodeId(UShort.MIN, uint(3606));

  public static final NodeId AuditHistoryAtTimeDeleteEventType_EventId =
      new NodeId(UShort.MIN, uint(3607));

  public static final NodeId AuditHistoryAtTimeDeleteEventType_EventType =
      new NodeId(UShort.MIN, uint(3608));

  public static final NodeId AuditHistoryAtTimeDeleteEventType_SourceNode =
      new NodeId(UShort.MIN, uint(3609));

  public static final NodeId AuditHistoryAtTimeDeleteEventType_SourceName =
      new NodeId(UShort.MIN, uint(3610));

  public static final NodeId AuditHistoryAtTimeDeleteEventType_Time =
      new NodeId(UShort.MIN, uint(3611));

  public static final NodeId AuditHistoryAtTimeDeleteEventType_ReceiveTime =
      new NodeId(UShort.MIN, uint(3612));

  public static final NodeId AuditHistoryAtTimeDeleteEventType_LocalTime =
      new NodeId(UShort.MIN, uint(3613));

  public static final NodeId AuditHistoryAtTimeDeleteEventType_Message =
      new NodeId(UShort.MIN, uint(3614));

  public static final NodeId AuditHistoryAtTimeDeleteEventType_Severity =
      new NodeId(UShort.MIN, uint(3615));

  public static final NodeId AuditHistoryAtTimeDeleteEventType_ActionTimeStamp =
      new NodeId(UShort.MIN, uint(3616));

  public static final NodeId AuditHistoryAtTimeDeleteEventType_Status =
      new NodeId(UShort.MIN, uint(3617));

  public static final NodeId AuditHistoryAtTimeDeleteEventType_ServerId =
      new NodeId(UShort.MIN, uint(3618));

  public static final NodeId AuditHistoryAtTimeDeleteEventType_ClientAuditEntryId =
      new NodeId(UShort.MIN, uint(3619));

  public static final NodeId AuditHistoryAtTimeDeleteEventType_ClientUserId =
      new NodeId(UShort.MIN, uint(3620));

  public static final NodeId AuditHistoryAtTimeDeleteEventType_ParameterDataTypeId =
      new NodeId(UShort.MIN, uint(3621));

  public static final NodeId AuditHistoryAtTimeDeleteEventType_UpdatedNode =
      new NodeId(UShort.MIN, uint(3622));

  public static final NodeId AuditHistoryEventDeleteEventType_EventId =
      new NodeId(UShort.MIN, uint(3623));

  public static final NodeId AuditHistoryEventDeleteEventType_EventType =
      new NodeId(UShort.MIN, uint(3624));

  public static final NodeId AuditHistoryEventDeleteEventType_SourceNode =
      new NodeId(UShort.MIN, uint(3625));

  public static final NodeId AuditHistoryEventDeleteEventType_SourceName =
      new NodeId(UShort.MIN, uint(3626));

  public static final NodeId AuditHistoryEventDeleteEventType_Time =
      new NodeId(UShort.MIN, uint(3627));

  public static final NodeId AuditHistoryEventDeleteEventType_ReceiveTime =
      new NodeId(UShort.MIN, uint(3628));

  public static final NodeId AuditHistoryEventDeleteEventType_LocalTime =
      new NodeId(UShort.MIN, uint(3629));

  public static final NodeId AuditHistoryEventDeleteEventType_Message =
      new NodeId(UShort.MIN, uint(3630));

  public static final NodeId AuditHistoryEventDeleteEventType_Severity =
      new NodeId(UShort.MIN, uint(3631));

  public static final NodeId AuditHistoryEventDeleteEventType_ActionTimeStamp =
      new NodeId(UShort.MIN, uint(3632));

  public static final NodeId AuditHistoryEventDeleteEventType_Status =
      new NodeId(UShort.MIN, uint(3633));

  public static final NodeId AuditHistoryEventDeleteEventType_ServerId =
      new NodeId(UShort.MIN, uint(3634));

  public static final NodeId AuditHistoryEventDeleteEventType_ClientAuditEntryId =
      new NodeId(UShort.MIN, uint(3635));

  public static final NodeId AuditHistoryEventDeleteEventType_ClientUserId =
      new NodeId(UShort.MIN, uint(3636));

  public static final NodeId AuditHistoryEventDeleteEventType_ParameterDataTypeId =
      new NodeId(UShort.MIN, uint(3637));

  public static final NodeId AuditHistoryEventDeleteEventType_UpdatedNode =
      new NodeId(UShort.MIN, uint(3638));

  public static final NodeId AuditUpdateMethodEventType_EventId =
      new NodeId(UShort.MIN, uint(3639));

  public static final NodeId AuditUpdateMethodEventType_EventType =
      new NodeId(UShort.MIN, uint(3640));

  public static final NodeId AuditUpdateMethodEventType_SourceNode =
      new NodeId(UShort.MIN, uint(3641));

  public static final NodeId AuditUpdateMethodEventType_SourceName =
      new NodeId(UShort.MIN, uint(3642));

  public static final NodeId AuditUpdateMethodEventType_Time = new NodeId(UShort.MIN, uint(3643));

  public static final NodeId AuditUpdateMethodEventType_ReceiveTime =
      new NodeId(UShort.MIN, uint(3644));

  public static final NodeId AuditUpdateMethodEventType_LocalTime =
      new NodeId(UShort.MIN, uint(3645));

  public static final NodeId AuditUpdateMethodEventType_Message =
      new NodeId(UShort.MIN, uint(3646));

  public static final NodeId AuditUpdateMethodEventType_Severity =
      new NodeId(UShort.MIN, uint(3647));

  public static final NodeId AuditUpdateMethodEventType_ActionTimeStamp =
      new NodeId(UShort.MIN, uint(3648));

  public static final NodeId AuditUpdateMethodEventType_Status = new NodeId(UShort.MIN, uint(3649));

  public static final NodeId AuditUpdateMethodEventType_ServerId =
      new NodeId(UShort.MIN, uint(3650));

  public static final NodeId AuditUpdateMethodEventType_ClientAuditEntryId =
      new NodeId(UShort.MIN, uint(3651));

  public static final NodeId AuditUpdateMethodEventType_ClientUserId =
      new NodeId(UShort.MIN, uint(3652));

  public static final NodeId SystemEventType_EventId = new NodeId(UShort.MIN, uint(3653));

  public static final NodeId SystemEventType_EventType = new NodeId(UShort.MIN, uint(3654));

  public static final NodeId SystemEventType_SourceNode = new NodeId(UShort.MIN, uint(3655));

  public static final NodeId SystemEventType_SourceName = new NodeId(UShort.MIN, uint(3656));

  public static final NodeId SystemEventType_Time = new NodeId(UShort.MIN, uint(3657));

  public static final NodeId SystemEventType_ReceiveTime = new NodeId(UShort.MIN, uint(3658));

  public static final NodeId SystemEventType_LocalTime = new NodeId(UShort.MIN, uint(3659));

  public static final NodeId SystemEventType_Message = new NodeId(UShort.MIN, uint(3660));

  public static final NodeId SystemEventType_Severity = new NodeId(UShort.MIN, uint(3661));

  public static final NodeId DeviceFailureEventType_EventId = new NodeId(UShort.MIN, uint(3662));

  public static final NodeId DeviceFailureEventType_EventType = new NodeId(UShort.MIN, uint(3663));

  public static final NodeId DeviceFailureEventType_SourceNode = new NodeId(UShort.MIN, uint(3664));

  public static final NodeId DeviceFailureEventType_SourceName = new NodeId(UShort.MIN, uint(3665));

  public static final NodeId DeviceFailureEventType_Time = new NodeId(UShort.MIN, uint(3666));

  public static final NodeId DeviceFailureEventType_ReceiveTime =
      new NodeId(UShort.MIN, uint(3667));

  public static final NodeId DeviceFailureEventType_LocalTime = new NodeId(UShort.MIN, uint(3668));

  public static final NodeId DeviceFailureEventType_Message = new NodeId(UShort.MIN, uint(3669));

  public static final NodeId DeviceFailureEventType_Severity = new NodeId(UShort.MIN, uint(3670));

  public static final NodeId BaseModelChangeEventType_EventId = new NodeId(UShort.MIN, uint(3671));

  public static final NodeId BaseModelChangeEventType_EventType =
      new NodeId(UShort.MIN, uint(3672));

  public static final NodeId BaseModelChangeEventType_SourceNode =
      new NodeId(UShort.MIN, uint(3673));

  public static final NodeId BaseModelChangeEventType_SourceName =
      new NodeId(UShort.MIN, uint(3674));

  public static final NodeId BaseModelChangeEventType_Time = new NodeId(UShort.MIN, uint(3675));

  public static final NodeId BaseModelChangeEventType_ReceiveTime =
      new NodeId(UShort.MIN, uint(3676));

  public static final NodeId BaseModelChangeEventType_LocalTime =
      new NodeId(UShort.MIN, uint(3677));

  public static final NodeId BaseModelChangeEventType_Message = new NodeId(UShort.MIN, uint(3678));

  public static final NodeId BaseModelChangeEventType_Severity = new NodeId(UShort.MIN, uint(3679));

  public static final NodeId GeneralModelChangeEventType_EventId =
      new NodeId(UShort.MIN, uint(3680));

  public static final NodeId GeneralModelChangeEventType_EventType =
      new NodeId(UShort.MIN, uint(3681));

  public static final NodeId GeneralModelChangeEventType_SourceNode =
      new NodeId(UShort.MIN, uint(3682));

  public static final NodeId GeneralModelChangeEventType_SourceName =
      new NodeId(UShort.MIN, uint(3683));

  public static final NodeId GeneralModelChangeEventType_Time = new NodeId(UShort.MIN, uint(3684));

  public static final NodeId GeneralModelChangeEventType_ReceiveTime =
      new NodeId(UShort.MIN, uint(3685));

  public static final NodeId GeneralModelChangeEventType_LocalTime =
      new NodeId(UShort.MIN, uint(3686));

  public static final NodeId GeneralModelChangeEventType_Message =
      new NodeId(UShort.MIN, uint(3687));

  public static final NodeId GeneralModelChangeEventType_Severity =
      new NodeId(UShort.MIN, uint(3688));

  public static final NodeId SemanticChangeEventType_EventId = new NodeId(UShort.MIN, uint(3689));

  public static final NodeId SemanticChangeEventType_EventType = new NodeId(UShort.MIN, uint(3690));

  public static final NodeId SemanticChangeEventType_SourceNode =
      new NodeId(UShort.MIN, uint(3691));

  public static final NodeId SemanticChangeEventType_SourceName =
      new NodeId(UShort.MIN, uint(3692));

  public static final NodeId SemanticChangeEventType_Time = new NodeId(UShort.MIN, uint(3693));

  public static final NodeId SemanticChangeEventType_ReceiveTime =
      new NodeId(UShort.MIN, uint(3694));

  public static final NodeId SemanticChangeEventType_LocalTime = new NodeId(UShort.MIN, uint(3695));

  public static final NodeId SemanticChangeEventType_Message = new NodeId(UShort.MIN, uint(3696));

  public static final NodeId SemanticChangeEventType_Severity = new NodeId(UShort.MIN, uint(3697));

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

  public static final NodeId FiniteStateVariableType_Name = new NodeId(UShort.MIN, uint(3714));

  public static final NodeId FiniteStateVariableType_Number = new NodeId(UShort.MIN, uint(3715));

  public static final NodeId FiniteStateVariableType_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(3716));

  public static final NodeId FiniteTransitionVariableType_Name = new NodeId(UShort.MIN, uint(3717));

  public static final NodeId FiniteTransitionVariableType_Number =
      new NodeId(UShort.MIN, uint(3718));

  public static final NodeId FiniteTransitionVariableType_TransitionTime =
      new NodeId(UShort.MIN, uint(3719));

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

  public static final NodeId InitialStateType_StateNumber = new NodeId(UShort.MIN, uint(3736));

  public static final NodeId TransitionEventType_EventId = new NodeId(UShort.MIN, uint(3737));

  public static final NodeId TransitionEventType_EventType = new NodeId(UShort.MIN, uint(3738));

  public static final NodeId TransitionEventType_SourceNode = new NodeId(UShort.MIN, uint(3739));

  public static final NodeId TransitionEventType_SourceName = new NodeId(UShort.MIN, uint(3740));

  public static final NodeId TransitionEventType_Time = new NodeId(UShort.MIN, uint(3741));

  public static final NodeId TransitionEventType_ReceiveTime = new NodeId(UShort.MIN, uint(3742));

  public static final NodeId TransitionEventType_LocalTime = new NodeId(UShort.MIN, uint(3743));

  public static final NodeId TransitionEventType_Message = new NodeId(UShort.MIN, uint(3744));

  public static final NodeId TransitionEventType_Severity = new NodeId(UShort.MIN, uint(3745));

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

  public static final NodeId AuditUpdateStateEventType_EventId = new NodeId(UShort.MIN, uint(3758));

  public static final NodeId AuditUpdateStateEventType_EventType =
      new NodeId(UShort.MIN, uint(3759));

  public static final NodeId AuditUpdateStateEventType_SourceNode =
      new NodeId(UShort.MIN, uint(3760));

  public static final NodeId AuditUpdateStateEventType_SourceName =
      new NodeId(UShort.MIN, uint(3761));

  public static final NodeId AuditUpdateStateEventType_Time = new NodeId(UShort.MIN, uint(3762));

  public static final NodeId AuditUpdateStateEventType_ReceiveTime =
      new NodeId(UShort.MIN, uint(3763));

  public static final NodeId AuditUpdateStateEventType_LocalTime =
      new NodeId(UShort.MIN, uint(3764));

  public static final NodeId AuditUpdateStateEventType_Message = new NodeId(UShort.MIN, uint(3765));

  public static final NodeId AuditUpdateStateEventType_Severity =
      new NodeId(UShort.MIN, uint(3766));

  public static final NodeId AuditUpdateStateEventType_ActionTimeStamp =
      new NodeId(UShort.MIN, uint(3767));

  public static final NodeId AuditUpdateStateEventType_Status = new NodeId(UShort.MIN, uint(3768));

  public static final NodeId AuditUpdateStateEventType_ServerId =
      new NodeId(UShort.MIN, uint(3769));

  public static final NodeId AuditUpdateStateEventType_ClientAuditEntryId =
      new NodeId(UShort.MIN, uint(3770));

  public static final NodeId AuditUpdateStateEventType_ClientUserId =
      new NodeId(UShort.MIN, uint(3771));

  public static final NodeId AuditUpdateStateEventType_MethodId =
      new NodeId(UShort.MIN, uint(3772));

  public static final NodeId AuditUpdateStateEventType_InputArguments =
      new NodeId(UShort.MIN, uint(3773));

  public static final NodeId AnalogItemType_Definition = new NodeId(UShort.MIN, uint(3774));

  public static final NodeId AnalogItemType_ValuePrecision = new NodeId(UShort.MIN, uint(3775));

  public static final NodeId DiscreteItemType_Definition = new NodeId(UShort.MIN, uint(3776));

  public static final NodeId DiscreteItemType_ValuePrecision = new NodeId(UShort.MIN, uint(3777));

  public static final NodeId TwoStateDiscreteType_Definition = new NodeId(UShort.MIN, uint(3778));

  public static final NodeId TwoStateDiscreteType_ValuePrecision =
      new NodeId(UShort.MIN, uint(3779));

  public static final NodeId MultiStateDiscreteType_Definition = new NodeId(UShort.MIN, uint(3780));

  public static final NodeId MultiStateDiscreteType_ValuePrecision =
      new NodeId(UShort.MIN, uint(3781));

  public static final NodeId ProgramTransitionEventType_EventId =
      new NodeId(UShort.MIN, uint(3782));

  public static final NodeId ProgramTransitionEventType_EventType =
      new NodeId(UShort.MIN, uint(3783));

  public static final NodeId ProgramTransitionEventType_SourceNode =
      new NodeId(UShort.MIN, uint(3784));

  public static final NodeId ProgramTransitionEventType_SourceName =
      new NodeId(UShort.MIN, uint(3785));

  public static final NodeId ProgramTransitionEventType_Time = new NodeId(UShort.MIN, uint(3786));

  public static final NodeId ProgramTransitionEventType_ReceiveTime =
      new NodeId(UShort.MIN, uint(3787));

  public static final NodeId ProgramTransitionEventType_LocalTime =
      new NodeId(UShort.MIN, uint(3788));

  public static final NodeId ProgramTransitionEventType_Message =
      new NodeId(UShort.MIN, uint(3789));

  public static final NodeId ProgramTransitionEventType_Severity =
      new NodeId(UShort.MIN, uint(3790));

  public static final NodeId ProgramTransitionEventType_FromState =
      new NodeId(UShort.MIN, uint(3791));

  public static final NodeId ProgramTransitionEventType_FromState_Id =
      new NodeId(UShort.MIN, uint(3792));

  public static final NodeId ProgramTransitionEventType_FromState_Name =
      new NodeId(UShort.MIN, uint(3793));

  public static final NodeId ProgramTransitionEventType_FromState_Number =
      new NodeId(UShort.MIN, uint(3794));

  public static final NodeId ProgramTransitionEventType_FromState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(3795));

  public static final NodeId ProgramTransitionEventType_ToState =
      new NodeId(UShort.MIN, uint(3796));

  public static final NodeId ProgramTransitionEventType_ToState_Id =
      new NodeId(UShort.MIN, uint(3797));

  public static final NodeId ProgramTransitionEventType_ToState_Name =
      new NodeId(UShort.MIN, uint(3798));

  public static final NodeId ProgramTransitionEventType_ToState_Number =
      new NodeId(UShort.MIN, uint(3799));

  public static final NodeId ProgramTransitionEventType_ToState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(3800));

  public static final NodeId ProgramTransitionEventType_Transition =
      new NodeId(UShort.MIN, uint(3801));

  public static final NodeId ProgramTransitionEventType_Transition_Id =
      new NodeId(UShort.MIN, uint(3802));

  public static final NodeId ProgramTransitionEventType_Transition_Name =
      new NodeId(UShort.MIN, uint(3803));

  public static final NodeId ProgramTransitionEventType_Transition_Number =
      new NodeId(UShort.MIN, uint(3804));

  public static final NodeId ProgramTransitionEventType_Transition_TransitionTime =
      new NodeId(UShort.MIN, uint(3805));

  public static final NodeId ProgramTransitionAuditEventType = new NodeId(UShort.MIN, uint(3806));

  public static final NodeId ProgramTransitionAuditEventType_EventId =
      new NodeId(UShort.MIN, uint(3807));

  public static final NodeId ProgramTransitionAuditEventType_EventType =
      new NodeId(UShort.MIN, uint(3808));

  public static final NodeId ProgramTransitionAuditEventType_SourceNode =
      new NodeId(UShort.MIN, uint(3809));

  public static final NodeId ProgramTransitionAuditEventType_SourceName =
      new NodeId(UShort.MIN, uint(3810));

  public static final NodeId ProgramTransitionAuditEventType_Time =
      new NodeId(UShort.MIN, uint(3811));

  public static final NodeId ProgramTransitionAuditEventType_ReceiveTime =
      new NodeId(UShort.MIN, uint(3812));

  public static final NodeId ProgramTransitionAuditEventType_LocalTime =
      new NodeId(UShort.MIN, uint(3813));

  public static final NodeId ProgramTransitionAuditEventType_Message =
      new NodeId(UShort.MIN, uint(3814));

  public static final NodeId ProgramTransitionAuditEventType_Severity =
      new NodeId(UShort.MIN, uint(3815));

  public static final NodeId ProgramTransitionAuditEventType_ActionTimeStamp =
      new NodeId(UShort.MIN, uint(3816));

  public static final NodeId ProgramTransitionAuditEventType_Status =
      new NodeId(UShort.MIN, uint(3817));

  public static final NodeId ProgramTransitionAuditEventType_ServerId =
      new NodeId(UShort.MIN, uint(3818));

  public static final NodeId ProgramTransitionAuditEventType_ClientAuditEntryId =
      new NodeId(UShort.MIN, uint(3819));

  public static final NodeId ProgramTransitionAuditEventType_ClientUserId =
      new NodeId(UShort.MIN, uint(3820));

  public static final NodeId ProgramTransitionAuditEventType_MethodId =
      new NodeId(UShort.MIN, uint(3821));

  public static final NodeId ProgramTransitionAuditEventType_InputArguments =
      new NodeId(UShort.MIN, uint(3822));

  public static final NodeId ProgramTransitionAuditEventType_OldStateId =
      new NodeId(UShort.MIN, uint(3823));

  public static final NodeId ProgramTransitionAuditEventType_NewStateId =
      new NodeId(UShort.MIN, uint(3824));

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

  public static final NodeId ConditionType_EventId = new NodeId(UShort.MIN, uint(3865));

  public static final NodeId ConditionType_EventType = new NodeId(UShort.MIN, uint(3866));

  public static final NodeId ConditionType_SourceNode = new NodeId(UShort.MIN, uint(3867));

  public static final NodeId ConditionType_SourceName = new NodeId(UShort.MIN, uint(3868));

  public static final NodeId ConditionType_Time = new NodeId(UShort.MIN, uint(3869));

  public static final NodeId ConditionType_ReceiveTime = new NodeId(UShort.MIN, uint(3870));

  public static final NodeId ConditionType_LocalTime = new NodeId(UShort.MIN, uint(3871));

  public static final NodeId ConditionType_Message = new NodeId(UShort.MIN, uint(3872));

  public static final NodeId ConditionType_Severity = new NodeId(UShort.MIN, uint(3873));

  public static final NodeId ConditionType_Retain = new NodeId(UShort.MIN, uint(3874));

  public static final NodeId ConditionType_ConditionRefresh = new NodeId(UShort.MIN, uint(3875));

  public static final NodeId ConditionType_ConditionRefresh_InputArguments =
      new NodeId(UShort.MIN, uint(3876));

  public static final NodeId RefreshStartEventType_EventId = new NodeId(UShort.MIN, uint(3969));

  public static final NodeId RefreshStartEventType_EventType = new NodeId(UShort.MIN, uint(3970));

  public static final NodeId RefreshStartEventType_SourceNode = new NodeId(UShort.MIN, uint(3971));

  public static final NodeId RefreshStartEventType_SourceName = new NodeId(UShort.MIN, uint(3972));

  public static final NodeId RefreshStartEventType_Time = new NodeId(UShort.MIN, uint(3973));

  public static final NodeId RefreshStartEventType_ReceiveTime = new NodeId(UShort.MIN, uint(3974));

  public static final NodeId RefreshStartEventType_LocalTime = new NodeId(UShort.MIN, uint(3975));

  public static final NodeId RefreshStartEventType_Message = new NodeId(UShort.MIN, uint(3976));

  public static final NodeId RefreshStartEventType_Severity = new NodeId(UShort.MIN, uint(3977));

  public static final NodeId RefreshEndEventType_EventId = new NodeId(UShort.MIN, uint(3978));

  public static final NodeId RefreshEndEventType_EventType = new NodeId(UShort.MIN, uint(3979));

  public static final NodeId RefreshEndEventType_SourceNode = new NodeId(UShort.MIN, uint(3980));

  public static final NodeId RefreshEndEventType_SourceName = new NodeId(UShort.MIN, uint(3981));

  public static final NodeId RefreshEndEventType_Time = new NodeId(UShort.MIN, uint(3982));

  public static final NodeId RefreshEndEventType_ReceiveTime = new NodeId(UShort.MIN, uint(3983));

  public static final NodeId RefreshEndEventType_LocalTime = new NodeId(UShort.MIN, uint(3984));

  public static final NodeId RefreshEndEventType_Message = new NodeId(UShort.MIN, uint(3985));

  public static final NodeId RefreshEndEventType_Severity = new NodeId(UShort.MIN, uint(3986));

  public static final NodeId RefreshRequiredEventType_EventId = new NodeId(UShort.MIN, uint(3987));

  public static final NodeId RefreshRequiredEventType_EventType =
      new NodeId(UShort.MIN, uint(3988));

  public static final NodeId RefreshRequiredEventType_SourceNode =
      new NodeId(UShort.MIN, uint(3989));

  public static final NodeId RefreshRequiredEventType_SourceName =
      new NodeId(UShort.MIN, uint(3990));

  public static final NodeId RefreshRequiredEventType_Time = new NodeId(UShort.MIN, uint(3991));

  public static final NodeId RefreshRequiredEventType_ReceiveTime =
      new NodeId(UShort.MIN, uint(3992));

  public static final NodeId RefreshRequiredEventType_LocalTime =
      new NodeId(UShort.MIN, uint(3993));

  public static final NodeId RefreshRequiredEventType_Message = new NodeId(UShort.MIN, uint(3994));

  public static final NodeId RefreshRequiredEventType_Severity = new NodeId(UShort.MIN, uint(3995));

  public static final NodeId AuditConditionEventType_EventId = new NodeId(UShort.MIN, uint(3996));

  public static final NodeId AuditConditionEventType_EventType = new NodeId(UShort.MIN, uint(3997));

  public static final NodeId AuditConditionEventType_SourceNode =
      new NodeId(UShort.MIN, uint(3998));

  public static final NodeId AuditConditionEventType_SourceName =
      new NodeId(UShort.MIN, uint(3999));

  public static final NodeId AuditConditionEventType_Time = new NodeId(UShort.MIN, uint(4000));

  public static final NodeId AuditConditionEventType_ReceiveTime =
      new NodeId(UShort.MIN, uint(4001));

  public static final NodeId AuditConditionEventType_LocalTime = new NodeId(UShort.MIN, uint(4002));

  public static final NodeId AuditConditionEventType_Message = new NodeId(UShort.MIN, uint(4003));

  public static final NodeId AuditConditionEventType_Severity = new NodeId(UShort.MIN, uint(4004));

  public static final NodeId AuditConditionEventType_ActionTimeStamp =
      new NodeId(UShort.MIN, uint(4005));

  public static final NodeId AuditConditionEventType_Status = new NodeId(UShort.MIN, uint(4006));

  public static final NodeId AuditConditionEventType_ServerId = new NodeId(UShort.MIN, uint(4007));

  public static final NodeId AuditConditionEventType_ClientAuditEntryId =
      new NodeId(UShort.MIN, uint(4008));

  public static final NodeId AuditConditionEventType_ClientUserId =
      new NodeId(UShort.MIN, uint(4009));

  public static final NodeId AuditConditionEventType_MethodId = new NodeId(UShort.MIN, uint(4010));

  public static final NodeId AuditConditionEventType_InputArguments =
      new NodeId(UShort.MIN, uint(4011));

  public static final NodeId AuditConditionEnableEventType_EventId =
      new NodeId(UShort.MIN, uint(4106));

  public static final NodeId AuditConditionEnableEventType_EventType =
      new NodeId(UShort.MIN, uint(4107));

  public static final NodeId AuditConditionEnableEventType_SourceNode =
      new NodeId(UShort.MIN, uint(4108));

  public static final NodeId AuditConditionEnableEventType_SourceName =
      new NodeId(UShort.MIN, uint(4109));

  public static final NodeId AuditConditionEnableEventType_Time =
      new NodeId(UShort.MIN, uint(4110));

  public static final NodeId AuditConditionEnableEventType_ReceiveTime =
      new NodeId(UShort.MIN, uint(4111));

  public static final NodeId AuditConditionEnableEventType_LocalTime =
      new NodeId(UShort.MIN, uint(4112));

  public static final NodeId AuditConditionEnableEventType_Message =
      new NodeId(UShort.MIN, uint(4113));

  public static final NodeId AuditConditionEnableEventType_Severity =
      new NodeId(UShort.MIN, uint(4114));

  public static final NodeId AuditConditionEnableEventType_ActionTimeStamp =
      new NodeId(UShort.MIN, uint(4115));

  public static final NodeId AuditConditionEnableEventType_Status =
      new NodeId(UShort.MIN, uint(4116));

  public static final NodeId AuditConditionEnableEventType_ServerId =
      new NodeId(UShort.MIN, uint(4117));

  public static final NodeId AuditConditionEnableEventType_ClientAuditEntryId =
      new NodeId(UShort.MIN, uint(4118));

  public static final NodeId AuditConditionEnableEventType_ClientUserId =
      new NodeId(UShort.MIN, uint(4119));

  public static final NodeId AuditConditionEnableEventType_MethodId =
      new NodeId(UShort.MIN, uint(4120));

  public static final NodeId AuditConditionEnableEventType_InputArguments =
      new NodeId(UShort.MIN, uint(4121));

  public static final NodeId AuditConditionCommentEventType_EventId =
      new NodeId(UShort.MIN, uint(4170));

  public static final NodeId AuditConditionCommentEventType_EventType =
      new NodeId(UShort.MIN, uint(4171));

  public static final NodeId AuditConditionCommentEventType_SourceNode =
      new NodeId(UShort.MIN, uint(4172));

  public static final NodeId AuditConditionCommentEventType_SourceName =
      new NodeId(UShort.MIN, uint(4173));

  public static final NodeId AuditConditionCommentEventType_Time =
      new NodeId(UShort.MIN, uint(4174));

  public static final NodeId AuditConditionCommentEventType_ReceiveTime =
      new NodeId(UShort.MIN, uint(4175));

  public static final NodeId AuditConditionCommentEventType_LocalTime =
      new NodeId(UShort.MIN, uint(4176));

  public static final NodeId AuditConditionCommentEventType_Message =
      new NodeId(UShort.MIN, uint(4177));

  public static final NodeId AuditConditionCommentEventType_Severity =
      new NodeId(UShort.MIN, uint(4178));

  public static final NodeId AuditConditionCommentEventType_ActionTimeStamp =
      new NodeId(UShort.MIN, uint(4179));

  public static final NodeId AuditConditionCommentEventType_Status =
      new NodeId(UShort.MIN, uint(4180));

  public static final NodeId AuditConditionCommentEventType_ServerId =
      new NodeId(UShort.MIN, uint(4181));

  public static final NodeId AuditConditionCommentEventType_ClientAuditEntryId =
      new NodeId(UShort.MIN, uint(4182));

  public static final NodeId AuditConditionCommentEventType_ClientUserId =
      new NodeId(UShort.MIN, uint(4183));

  public static final NodeId AuditConditionCommentEventType_MethodId =
      new NodeId(UShort.MIN, uint(4184));

  public static final NodeId AuditConditionCommentEventType_InputArguments =
      new NodeId(UShort.MIN, uint(4185));

  public static final NodeId DialogConditionType_EventId = new NodeId(UShort.MIN, uint(4188));

  public static final NodeId DialogConditionType_EventType = new NodeId(UShort.MIN, uint(4189));

  public static final NodeId DialogConditionType_SourceNode = new NodeId(UShort.MIN, uint(4190));

  public static final NodeId DialogConditionType_SourceName = new NodeId(UShort.MIN, uint(4191));

  public static final NodeId DialogConditionType_Time = new NodeId(UShort.MIN, uint(4192));

  public static final NodeId DialogConditionType_ReceiveTime = new NodeId(UShort.MIN, uint(4193));

  public static final NodeId DialogConditionType_LocalTime = new NodeId(UShort.MIN, uint(4194));

  public static final NodeId DialogConditionType_Message = new NodeId(UShort.MIN, uint(4195));

  public static final NodeId DialogConditionType_Severity = new NodeId(UShort.MIN, uint(4196));

  public static final NodeId DialogConditionType_Retain = new NodeId(UShort.MIN, uint(4197));

  public static final NodeId DialogConditionType_ConditionRefresh =
      new NodeId(UShort.MIN, uint(4198));

  public static final NodeId DialogConditionType_ConditionRefresh_InputArguments =
      new NodeId(UShort.MIN, uint(4199));

  public static final NodeId AcknowledgeableConditionType_EventId =
      new NodeId(UShort.MIN, uint(5113));

  public static final NodeId AcknowledgeableConditionType_EventType =
      new NodeId(UShort.MIN, uint(5114));

  public static final NodeId AcknowledgeableConditionType_SourceNode =
      new NodeId(UShort.MIN, uint(5115));

  public static final NodeId AcknowledgeableConditionType_SourceName =
      new NodeId(UShort.MIN, uint(5116));

  public static final NodeId AcknowledgeableConditionType_Time = new NodeId(UShort.MIN, uint(5117));

  public static final NodeId AcknowledgeableConditionType_ReceiveTime =
      new NodeId(UShort.MIN, uint(5118));

  public static final NodeId AcknowledgeableConditionType_LocalTime =
      new NodeId(UShort.MIN, uint(5119));

  public static final NodeId AcknowledgeableConditionType_Message =
      new NodeId(UShort.MIN, uint(5120));

  public static final NodeId AcknowledgeableConditionType_Severity =
      new NodeId(UShort.MIN, uint(5121));

  public static final NodeId AcknowledgeableConditionType_Retain =
      new NodeId(UShort.MIN, uint(5122));

  public static final NodeId AcknowledgeableConditionType_ConditionRefresh =
      new NodeId(UShort.MIN, uint(5123));

  public static final NodeId AcknowledgeableConditionType_ConditionRefresh_InputArguments =
      new NodeId(UShort.MIN, uint(5124));

  public static final NodeId AlarmConditionType_EventId = new NodeId(UShort.MIN, uint(5540));

  public static final NodeId AlarmConditionType_EventType = new NodeId(UShort.MIN, uint(5541));

  public static final NodeId AlarmConditionType_SourceNode = new NodeId(UShort.MIN, uint(5542));

  public static final NodeId AlarmConditionType_SourceName = new NodeId(UShort.MIN, uint(5543));

  public static final NodeId AlarmConditionType_Time = new NodeId(UShort.MIN, uint(5544));

  public static final NodeId AlarmConditionType_ReceiveTime = new NodeId(UShort.MIN, uint(5545));

  public static final NodeId AlarmConditionType_LocalTime = new NodeId(UShort.MIN, uint(5546));

  public static final NodeId AlarmConditionType_Message = new NodeId(UShort.MIN, uint(5547));

  public static final NodeId AlarmConditionType_Severity = new NodeId(UShort.MIN, uint(5548));

  public static final NodeId AlarmConditionType_Retain = new NodeId(UShort.MIN, uint(5549));

  public static final NodeId AlarmConditionType_ConditionRefresh =
      new NodeId(UShort.MIN, uint(5550));

  public static final NodeId AlarmConditionType_ConditionRefresh_InputArguments =
      new NodeId(UShort.MIN, uint(5551));

  public static final NodeId ShelvedStateMachineType_CurrentState =
      new NodeId(UShort.MIN, uint(6088));

  public static final NodeId ShelvedStateMachineType_CurrentState_Id =
      new NodeId(UShort.MIN, uint(6089));

  public static final NodeId ShelvedStateMachineType_CurrentState_Name =
      new NodeId(UShort.MIN, uint(6090));

  public static final NodeId ShelvedStateMachineType_CurrentState_Number =
      new NodeId(UShort.MIN, uint(6091));

  public static final NodeId ShelvedStateMachineType_CurrentState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(6092));

  public static final NodeId ShelvedStateMachineType_LastTransition =
      new NodeId(UShort.MIN, uint(6093));

  public static final NodeId ShelvedStateMachineType_LastTransition_Id =
      new NodeId(UShort.MIN, uint(6094));

  public static final NodeId ShelvedStateMachineType_LastTransition_Name =
      new NodeId(UShort.MIN, uint(6095));

  public static final NodeId ShelvedStateMachineType_LastTransition_Number =
      new NodeId(UShort.MIN, uint(6096));

  public static final NodeId ShelvedStateMachineType_LastTransition_TransitionTime =
      new NodeId(UShort.MIN, uint(6097));

  public static final NodeId ShelvedStateMachineType_Unshelved_StateNumber =
      new NodeId(UShort.MIN, uint(6098));

  public static final NodeId ShelvedStateMachineType_TimedShelved_StateNumber =
      new NodeId(UShort.MIN, uint(6100));

  public static final NodeId ShelvedStateMachineType_OneShotShelved_StateNumber =
      new NodeId(UShort.MIN, uint(6101));

  public static final NodeId TimedShelveMethodType = new NodeId(UShort.MIN, uint(6102));

  public static final NodeId TimedShelveMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(6103));

  public static final NodeId LimitAlarmType_EventId = new NodeId(UShort.MIN, uint(6116));

  public static final NodeId LimitAlarmType_EventType = new NodeId(UShort.MIN, uint(6117));

  public static final NodeId LimitAlarmType_SourceNode = new NodeId(UShort.MIN, uint(6118));

  public static final NodeId LimitAlarmType_SourceName = new NodeId(UShort.MIN, uint(6119));

  public static final NodeId LimitAlarmType_Time = new NodeId(UShort.MIN, uint(6120));

  public static final NodeId LimitAlarmType_ReceiveTime = new NodeId(UShort.MIN, uint(6121));

  public static final NodeId LimitAlarmType_LocalTime = new NodeId(UShort.MIN, uint(6122));

  public static final NodeId LimitAlarmType_Message = new NodeId(UShort.MIN, uint(6123));

  public static final NodeId LimitAlarmType_Severity = new NodeId(UShort.MIN, uint(6124));

  public static final NodeId LimitAlarmType_Retain = new NodeId(UShort.MIN, uint(6125));

  public static final NodeId LimitAlarmType_ConditionRefresh = new NodeId(UShort.MIN, uint(6126));

  public static final NodeId LimitAlarmType_ConditionRefresh_InputArguments =
      new NodeId(UShort.MIN, uint(6127));

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

  public static final NodeId AuditConditionRespondEventType_EventId =
      new NodeId(UShort.MIN, uint(8928));

  public static final NodeId AuditConditionRespondEventType_EventType =
      new NodeId(UShort.MIN, uint(8929));

  public static final NodeId AuditConditionRespondEventType_SourceNode =
      new NodeId(UShort.MIN, uint(8930));

  public static final NodeId AuditConditionRespondEventType_SourceName =
      new NodeId(UShort.MIN, uint(8931));

  public static final NodeId AuditConditionRespondEventType_Time =
      new NodeId(UShort.MIN, uint(8932));

  public static final NodeId AuditConditionRespondEventType_ReceiveTime =
      new NodeId(UShort.MIN, uint(8933));

  public static final NodeId AuditConditionRespondEventType_LocalTime =
      new NodeId(UShort.MIN, uint(8934));

  public static final NodeId AuditConditionRespondEventType_Message =
      new NodeId(UShort.MIN, uint(8935));

  public static final NodeId AuditConditionRespondEventType_Severity =
      new NodeId(UShort.MIN, uint(8936));

  public static final NodeId AuditConditionRespondEventType_ActionTimeStamp =
      new NodeId(UShort.MIN, uint(8937));

  public static final NodeId AuditConditionRespondEventType_Status =
      new NodeId(UShort.MIN, uint(8938));

  public static final NodeId AuditConditionRespondEventType_ServerId =
      new NodeId(UShort.MIN, uint(8939));

  public static final NodeId AuditConditionRespondEventType_ClientAuditEntryId =
      new NodeId(UShort.MIN, uint(8940));

  public static final NodeId AuditConditionRespondEventType_ClientUserId =
      new NodeId(UShort.MIN, uint(8941));

  public static final NodeId AuditConditionRespondEventType_MethodId =
      new NodeId(UShort.MIN, uint(8942));

  public static final NodeId AuditConditionRespondEventType_InputArguments =
      new NodeId(UShort.MIN, uint(8943));

  public static final NodeId AuditConditionAcknowledgeEventType =
      new NodeId(UShort.MIN, uint(8944));

  public static final NodeId AuditConditionAcknowledgeEventType_EventId =
      new NodeId(UShort.MIN, uint(8945));

  public static final NodeId AuditConditionAcknowledgeEventType_EventType =
      new NodeId(UShort.MIN, uint(8946));

  public static final NodeId AuditConditionAcknowledgeEventType_SourceNode =
      new NodeId(UShort.MIN, uint(8947));

  public static final NodeId AuditConditionAcknowledgeEventType_SourceName =
      new NodeId(UShort.MIN, uint(8948));

  public static final NodeId AuditConditionAcknowledgeEventType_Time =
      new NodeId(UShort.MIN, uint(8949));

  public static final NodeId AuditConditionAcknowledgeEventType_ReceiveTime =
      new NodeId(UShort.MIN, uint(8950));

  public static final NodeId AuditConditionAcknowledgeEventType_LocalTime =
      new NodeId(UShort.MIN, uint(8951));

  public static final NodeId AuditConditionAcknowledgeEventType_Message =
      new NodeId(UShort.MIN, uint(8952));

  public static final NodeId AuditConditionAcknowledgeEventType_Severity =
      new NodeId(UShort.MIN, uint(8953));

  public static final NodeId AuditConditionAcknowledgeEventType_ActionTimeStamp =
      new NodeId(UShort.MIN, uint(8954));

  public static final NodeId AuditConditionAcknowledgeEventType_Status =
      new NodeId(UShort.MIN, uint(8955));

  public static final NodeId AuditConditionAcknowledgeEventType_ServerId =
      new NodeId(UShort.MIN, uint(8956));

  public static final NodeId AuditConditionAcknowledgeEventType_ClientAuditEntryId =
      new NodeId(UShort.MIN, uint(8957));

  public static final NodeId AuditConditionAcknowledgeEventType_ClientUserId =
      new NodeId(UShort.MIN, uint(8958));

  public static final NodeId AuditConditionAcknowledgeEventType_MethodId =
      new NodeId(UShort.MIN, uint(8959));

  public static final NodeId AuditConditionAcknowledgeEventType_InputArguments =
      new NodeId(UShort.MIN, uint(8960));

  public static final NodeId AuditConditionConfirmEventType = new NodeId(UShort.MIN, uint(8961));

  public static final NodeId AuditConditionConfirmEventType_EventId =
      new NodeId(UShort.MIN, uint(8962));

  public static final NodeId AuditConditionConfirmEventType_EventType =
      new NodeId(UShort.MIN, uint(8963));

  public static final NodeId AuditConditionConfirmEventType_SourceNode =
      new NodeId(UShort.MIN, uint(8964));

  public static final NodeId AuditConditionConfirmEventType_SourceName =
      new NodeId(UShort.MIN, uint(8965));

  public static final NodeId AuditConditionConfirmEventType_Time =
      new NodeId(UShort.MIN, uint(8966));

  public static final NodeId AuditConditionConfirmEventType_ReceiveTime =
      new NodeId(UShort.MIN, uint(8967));

  public static final NodeId AuditConditionConfirmEventType_LocalTime =
      new NodeId(UShort.MIN, uint(8968));

  public static final NodeId AuditConditionConfirmEventType_Message =
      new NodeId(UShort.MIN, uint(8969));

  public static final NodeId AuditConditionConfirmEventType_Severity =
      new NodeId(UShort.MIN, uint(8970));

  public static final NodeId AuditConditionConfirmEventType_ActionTimeStamp =
      new NodeId(UShort.MIN, uint(8971));

  public static final NodeId AuditConditionConfirmEventType_Status =
      new NodeId(UShort.MIN, uint(8972));

  public static final NodeId AuditConditionConfirmEventType_ServerId =
      new NodeId(UShort.MIN, uint(8973));

  public static final NodeId AuditConditionConfirmEventType_ClientAuditEntryId =
      new NodeId(UShort.MIN, uint(8974));

  public static final NodeId AuditConditionConfirmEventType_ClientUserId =
      new NodeId(UShort.MIN, uint(8975));

  public static final NodeId AuditConditionConfirmEventType_MethodId =
      new NodeId(UShort.MIN, uint(8976));

  public static final NodeId AuditConditionConfirmEventType_InputArguments =
      new NodeId(UShort.MIN, uint(8977));

  public static final NodeId TwoStateVariableType = new NodeId(UShort.MIN, uint(8995));

  public static final NodeId TwoStateVariableType_Id = new NodeId(UShort.MIN, uint(8996));

  public static final NodeId TwoStateVariableType_Name = new NodeId(UShort.MIN, uint(8997));

  public static final NodeId TwoStateVariableType_Number = new NodeId(UShort.MIN, uint(8998));

  public static final NodeId TwoStateVariableType_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(8999));

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
}

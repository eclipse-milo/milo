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

abstract class NodeIds0 extends NodeIds1 {
  public static final NodeId Boolean = new NodeId(UShort.MIN, uint(1L));

  public static final NodeId SByte = new NodeId(UShort.MIN, uint(2L));

  public static final NodeId Byte = new NodeId(UShort.MIN, uint(3L));

  public static final NodeId Int16 = new NodeId(UShort.MIN, uint(4L));

  public static final NodeId UInt16 = new NodeId(UShort.MIN, uint(5L));

  public static final NodeId Int32 = new NodeId(UShort.MIN, uint(6L));

  public static final NodeId UInt32 = new NodeId(UShort.MIN, uint(7L));

  public static final NodeId Int64 = new NodeId(UShort.MIN, uint(8L));

  public static final NodeId UInt64 = new NodeId(UShort.MIN, uint(9L));

  public static final NodeId Float = new NodeId(UShort.MIN, uint(10L));

  public static final NodeId Double = new NodeId(UShort.MIN, uint(11L));

  public static final NodeId String = new NodeId(UShort.MIN, uint(12L));

  public static final NodeId DateTime = new NodeId(UShort.MIN, uint(13L));

  public static final NodeId Guid = new NodeId(UShort.MIN, uint(14L));

  public static final NodeId ByteString = new NodeId(UShort.MIN, uint(15L));

  public static final NodeId XmlElement = new NodeId(UShort.MIN, uint(16L));

  public static final NodeId NodeId = new NodeId(UShort.MIN, uint(17L));

  public static final NodeId ExpandedNodeId = new NodeId(UShort.MIN, uint(18L));

  public static final NodeId StatusCode = new NodeId(UShort.MIN, uint(19L));

  public static final NodeId QualifiedName = new NodeId(UShort.MIN, uint(20L));

  public static final NodeId LocalizedText = new NodeId(UShort.MIN, uint(21L));

  public static final NodeId Structure = new NodeId(UShort.MIN, uint(22L));

  public static final NodeId DataValue = new NodeId(UShort.MIN, uint(23L));

  public static final NodeId BaseDataType = new NodeId(UShort.MIN, uint(24L));

  public static final NodeId DiagnosticInfo = new NodeId(UShort.MIN, uint(25L));

  public static final NodeId Number = new NodeId(UShort.MIN, uint(26L));

  public static final NodeId Integer = new NodeId(UShort.MIN, uint(27L));

  public static final NodeId UInteger = new NodeId(UShort.MIN, uint(28L));

  public static final NodeId Enumeration = new NodeId(UShort.MIN, uint(29L));

  public static final NodeId Image = new NodeId(UShort.MIN, uint(30L));

  public static final NodeId References = new NodeId(UShort.MIN, uint(31L));

  public static final NodeId NonHierarchicalReferences = new NodeId(UShort.MIN, uint(32L));

  public static final NodeId HierarchicalReferences = new NodeId(UShort.MIN, uint(33L));

  public static final NodeId HasChild = new NodeId(UShort.MIN, uint(34L));

  public static final NodeId Organizes = new NodeId(UShort.MIN, uint(35L));

  public static final NodeId HasEventSource = new NodeId(UShort.MIN, uint(36L));

  public static final NodeId HasModellingRule = new NodeId(UShort.MIN, uint(37L));

  public static final NodeId HasEncoding = new NodeId(UShort.MIN, uint(38L));

  public static final NodeId HasDescription = new NodeId(UShort.MIN, uint(39L));

  public static final NodeId HasTypeDefinition = new NodeId(UShort.MIN, uint(40L));

  public static final NodeId GeneratesEvent = new NodeId(UShort.MIN, uint(41L));

  public static final NodeId Aggregates = new NodeId(UShort.MIN, uint(44L));

  public static final NodeId HasSubtype = new NodeId(UShort.MIN, uint(45L));

  public static final NodeId HasProperty = new NodeId(UShort.MIN, uint(46L));

  public static final NodeId HasComponent = new NodeId(UShort.MIN, uint(47L));

  public static final NodeId HasNotifier = new NodeId(UShort.MIN, uint(48L));

  public static final NodeId HasOrderedComponent = new NodeId(UShort.MIN, uint(49L));

  public static final NodeId Decimal = new NodeId(UShort.MIN, uint(50L));

  public static final NodeId FromState = new NodeId(UShort.MIN, uint(51L));

  public static final NodeId ToState = new NodeId(UShort.MIN, uint(52L));

  public static final NodeId HasCause = new NodeId(UShort.MIN, uint(53L));

  public static final NodeId HasEffect = new NodeId(UShort.MIN, uint(54L));

  public static final NodeId HasHistoricalConfiguration = new NodeId(UShort.MIN, uint(56L));

  public static final NodeId BaseObjectType = new NodeId(UShort.MIN, uint(58L));

  public static final NodeId FolderType = new NodeId(UShort.MIN, uint(61L));

  public static final NodeId BaseVariableType = new NodeId(UShort.MIN, uint(62L));

  public static final NodeId BaseDataVariableType = new NodeId(UShort.MIN, uint(63L));

  public static final NodeId PropertyType = new NodeId(UShort.MIN, uint(68L));

  public static final NodeId DataTypeDescriptionType = new NodeId(UShort.MIN, uint(69L));

  public static final NodeId DataTypeDictionaryType = new NodeId(UShort.MIN, uint(72L));

  public static final NodeId DataTypeSystemType = new NodeId(UShort.MIN, uint(75L));

  public static final NodeId DataTypeEncodingType = new NodeId(UShort.MIN, uint(76L));

  public static final NodeId ModellingRuleType = new NodeId(UShort.MIN, uint(77L));

  public static final NodeId ModellingRule_Mandatory = new NodeId(UShort.MIN, uint(78L));

  public static final NodeId ModellingRule_Optional = new NodeId(UShort.MIN, uint(80L));

  public static final NodeId ModellingRule_ExposesItsArray = new NodeId(UShort.MIN, uint(83L));

  public static final NodeId RootFolder = new NodeId(UShort.MIN, uint(84L));

  public static final NodeId ObjectsFolder = new NodeId(UShort.MIN, uint(85L));

  public static final NodeId TypesFolder = new NodeId(UShort.MIN, uint(86L));

  public static final NodeId ViewsFolder = new NodeId(UShort.MIN, uint(87L));

  public static final NodeId ObjectTypesFolder = new NodeId(UShort.MIN, uint(88L));

  public static final NodeId VariableTypesFolder = new NodeId(UShort.MIN, uint(89L));

  public static final NodeId DataTypesFolder = new NodeId(UShort.MIN, uint(90L));

  public static final NodeId ReferenceTypesFolder = new NodeId(UShort.MIN, uint(91L));

  public static final NodeId XmlSchema_TypeSystem = new NodeId(UShort.MIN, uint(92L));

  public static final NodeId OPCBinarySchema_TypeSystem = new NodeId(UShort.MIN, uint(93L));

  public static final NodeId PermissionType = new NodeId(UShort.MIN, uint(94L));

  public static final NodeId AccessRestrictionType = new NodeId(UShort.MIN, uint(95L));

  public static final NodeId RolePermissionType = new NodeId(UShort.MIN, uint(96L));

  public static final NodeId DataTypeDefinition = new NodeId(UShort.MIN, uint(97L));

  public static final NodeId StructureType = new NodeId(UShort.MIN, uint(98L));

  public static final NodeId StructureDefinition = new NodeId(UShort.MIN, uint(99L));

  public static final NodeId EnumDefinition = new NodeId(UShort.MIN, uint(100L));

  public static final NodeId StructureField = new NodeId(UShort.MIN, uint(101L));

  public static final NodeId EnumField = new NodeId(UShort.MIN, uint(102L));

  public static final NodeId DataTypeDescriptionType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(104L));

  public static final NodeId DataTypeDescriptionType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(105L));

  public static final NodeId DataTypeDictionaryType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(106L));

  public static final NodeId DataTypeDictionaryType_NamespaceUri =
      new NodeId(UShort.MIN, uint(107L));

  public static final NodeId HasSubStateMachine = new NodeId(UShort.MIN, uint(117L));

  public static final NodeId NamingRuleType = new NodeId(UShort.MIN, uint(120L));

  public static final NodeId DataTypeDefinition_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(121L));

  public static final NodeId StructureDefinition_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(122L));

  public static final NodeId EnumDefinition_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(123L));

  public static final NodeId DataSetMetaDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(124L));

  public static final NodeId DataTypeDescription_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(125L));

  public static final NodeId StructureDescription_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(126L));

  public static final NodeId EnumDescription_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(127L));

  public static final NodeId RolePermissionType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(128L));

  public static final NodeId HasArgumentDescription = new NodeId(UShort.MIN, uint(129L));

  public static final NodeId HasOptionalInputArgumentDescription =
      new NodeId(UShort.MIN, uint(131L));

  public static final NodeId IdType = new NodeId(UShort.MIN, uint(256L));

  public static final NodeId NodeClass = new NodeId(UShort.MIN, uint(257L));

  public static final NodeId Node = new NodeId(UShort.MIN, uint(258L));

  public static final NodeId Node_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(259L));

  public static final NodeId Node_Encoding_DefaultBinary = new NodeId(UShort.MIN, uint(260L));

  public static final NodeId ObjectNode = new NodeId(UShort.MIN, uint(261L));

  public static final NodeId ObjectNode_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(262L));

  public static final NodeId ObjectNode_Encoding_DefaultBinary = new NodeId(UShort.MIN, uint(263L));

  public static final NodeId ObjectTypeNode = new NodeId(UShort.MIN, uint(264L));

  public static final NodeId ObjectTypeNode_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(265L));

  public static final NodeId ObjectTypeNode_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(266L));

  public static final NodeId VariableNode = new NodeId(UShort.MIN, uint(267L));

  public static final NodeId VariableNode_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(268L));

  public static final NodeId VariableNode_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(269L));

  public static final NodeId VariableTypeNode = new NodeId(UShort.MIN, uint(270L));

  public static final NodeId VariableTypeNode_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(271L));

  public static final NodeId VariableTypeNode_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(272L));

  public static final NodeId ReferenceTypeNode = new NodeId(UShort.MIN, uint(273L));

  public static final NodeId ReferenceTypeNode_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(274L));

  public static final NodeId ReferenceTypeNode_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(275L));

  public static final NodeId MethodNode = new NodeId(UShort.MIN, uint(276L));

  public static final NodeId MethodNode_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(277L));

  public static final NodeId MethodNode_Encoding_DefaultBinary = new NodeId(UShort.MIN, uint(278L));

  public static final NodeId ViewNode = new NodeId(UShort.MIN, uint(279L));

  public static final NodeId ViewNode_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(280L));

  public static final NodeId ViewNode_Encoding_DefaultBinary = new NodeId(UShort.MIN, uint(281L));

  public static final NodeId DataTypeNode = new NodeId(UShort.MIN, uint(282L));

  public static final NodeId DataTypeNode_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(283L));

  public static final NodeId DataTypeNode_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(284L));

  public static final NodeId ReferenceNode = new NodeId(UShort.MIN, uint(285L));

  public static final NodeId ReferenceNode_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(286L));

  public static final NodeId ReferenceNode_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(287L));

  public static final NodeId IntegerId = new NodeId(UShort.MIN, uint(288L));

  public static final NodeId Counter = new NodeId(UShort.MIN, uint(289L));

  public static final NodeId Duration = new NodeId(UShort.MIN, uint(290L));

  public static final NodeId NumericRange = new NodeId(UShort.MIN, uint(291L));

  public static final NodeId UtcTime = new NodeId(UShort.MIN, uint(294L));

  public static final NodeId LocaleId = new NodeId(UShort.MIN, uint(295L));

  public static final NodeId Argument = new NodeId(UShort.MIN, uint(296L));

  public static final NodeId Argument_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(297L));

  public static final NodeId Argument_Encoding_DefaultBinary = new NodeId(UShort.MIN, uint(298L));

  public static final NodeId StatusResult = new NodeId(UShort.MIN, uint(299L));

  public static final NodeId StatusResult_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(300L));

  public static final NodeId StatusResult_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(301L));

  public static final NodeId MessageSecurityMode = new NodeId(UShort.MIN, uint(302L));

  public static final NodeId UserTokenType = new NodeId(UShort.MIN, uint(303L));

  public static final NodeId UserTokenPolicy = new NodeId(UShort.MIN, uint(304L));

  public static final NodeId UserTokenPolicy_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(305L));

  public static final NodeId UserTokenPolicy_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(306L));

  public static final NodeId ApplicationType = new NodeId(UShort.MIN, uint(307L));

  public static final NodeId ApplicationDescription = new NodeId(UShort.MIN, uint(308L));

  public static final NodeId ApplicationDescription_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(309L));

  public static final NodeId ApplicationDescription_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(310L));

  public static final NodeId ApplicationInstanceCertificate = new NodeId(UShort.MIN, uint(311L));

  public static final NodeId EndpointDescription = new NodeId(UShort.MIN, uint(312L));

  public static final NodeId EndpointDescription_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(313L));

  public static final NodeId EndpointDescription_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(314L));

  public static final NodeId SecurityTokenRequestType = new NodeId(UShort.MIN, uint(315L));

  public static final NodeId UserIdentityToken = new NodeId(UShort.MIN, uint(316L));

  public static final NodeId UserIdentityToken_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(317L));

  public static final NodeId UserIdentityToken_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(318L));

  public static final NodeId AnonymousIdentityToken = new NodeId(UShort.MIN, uint(319L));

  public static final NodeId AnonymousIdentityToken_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(320L));

  public static final NodeId AnonymousIdentityToken_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(321L));

  public static final NodeId UserNameIdentityToken = new NodeId(UShort.MIN, uint(322L));

  public static final NodeId UserNameIdentityToken_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(323L));

  public static final NodeId UserNameIdentityToken_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(324L));

  public static final NodeId X509IdentityToken = new NodeId(UShort.MIN, uint(325L));

  public static final NodeId X509IdentityToken_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(326L));

  public static final NodeId X509IdentityToken_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(327L));

  public static final NodeId EndpointConfiguration = new NodeId(UShort.MIN, uint(331L));

  public static final NodeId EndpointConfiguration_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(332L));

  public static final NodeId EndpointConfiguration_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(333L));

  public static final NodeId BuildInfo = new NodeId(UShort.MIN, uint(338L));

  public static final NodeId BuildInfo_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(339L));

  public static final NodeId BuildInfo_Encoding_DefaultBinary = new NodeId(UShort.MIN, uint(340L));

  public static final NodeId SignedSoftwareCertificate = new NodeId(UShort.MIN, uint(344L));

  public static final NodeId SignedSoftwareCertificate_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(345L));

  public static final NodeId SignedSoftwareCertificate_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(346L));

  public static final NodeId AttributeWriteMask = new NodeId(UShort.MIN, uint(347L));

  public static final NodeId NodeAttributesMask = new NodeId(UShort.MIN, uint(348L));

  public static final NodeId NodeAttributes = new NodeId(UShort.MIN, uint(349L));

  public static final NodeId NodeAttributes_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(350L));

  public static final NodeId NodeAttributes_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(351L));

  public static final NodeId ObjectAttributes = new NodeId(UShort.MIN, uint(352L));

  public static final NodeId ObjectAttributes_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(353L));

  public static final NodeId ObjectAttributes_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(354L));

  public static final NodeId VariableAttributes = new NodeId(UShort.MIN, uint(355L));

  public static final NodeId VariableAttributes_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(356L));

  public static final NodeId VariableAttributes_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(357L));

  public static final NodeId MethodAttributes = new NodeId(UShort.MIN, uint(358L));

  public static final NodeId MethodAttributes_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(359L));

  public static final NodeId MethodAttributes_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(360L));

  public static final NodeId ObjectTypeAttributes = new NodeId(UShort.MIN, uint(361L));

  public static final NodeId ObjectTypeAttributes_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(362L));

  public static final NodeId ObjectTypeAttributes_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(363L));

  public static final NodeId VariableTypeAttributes = new NodeId(UShort.MIN, uint(364L));

  public static final NodeId VariableTypeAttributes_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(365L));

  public static final NodeId VariableTypeAttributes_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(366L));

  public static final NodeId ReferenceTypeAttributes = new NodeId(UShort.MIN, uint(367L));

  public static final NodeId ReferenceTypeAttributes_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(368L));

  public static final NodeId ReferenceTypeAttributes_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(369L));

  public static final NodeId DataTypeAttributes = new NodeId(UShort.MIN, uint(370L));

  public static final NodeId DataTypeAttributes_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(371L));

  public static final NodeId DataTypeAttributes_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(372L));

  public static final NodeId ViewAttributes = new NodeId(UShort.MIN, uint(373L));

  public static final NodeId ViewAttributes_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(374L));

  public static final NodeId ViewAttributes_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(375L));

  public static final NodeId AddNodesItem = new NodeId(UShort.MIN, uint(376L));

  public static final NodeId AddNodesItem_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(377L));

  public static final NodeId AddNodesItem_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(378L));

  public static final NodeId AddReferencesItem = new NodeId(UShort.MIN, uint(379L));

  public static final NodeId AddReferencesItem_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(380L));

  public static final NodeId AddReferencesItem_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(381L));

  public static final NodeId DeleteNodesItem = new NodeId(UShort.MIN, uint(382L));

  public static final NodeId DeleteNodesItem_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(383L));

  public static final NodeId DeleteNodesItem_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(384L));

  public static final NodeId DeleteReferencesItem = new NodeId(UShort.MIN, uint(385L));

  public static final NodeId DeleteReferencesItem_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(386L));

  public static final NodeId DeleteReferencesItem_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(387L));

  public static final NodeId SessionAuthenticationToken = new NodeId(UShort.MIN, uint(388L));

  public static final NodeId RequestHeader = new NodeId(UShort.MIN, uint(389L));

  public static final NodeId RequestHeader_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(390L));

  public static final NodeId RequestHeader_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(391L));

  public static final NodeId ResponseHeader = new NodeId(UShort.MIN, uint(392L));

  public static final NodeId ResponseHeader_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(393L));

  public static final NodeId ResponseHeader_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(394L));

  public static final NodeId ServiceFault = new NodeId(UShort.MIN, uint(395L));

  public static final NodeId ServiceFault_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(396L));

  public static final NodeId ServiceFault_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(397L));

  public static final NodeId FindServersRequest = new NodeId(UShort.MIN, uint(420L));

  public static final NodeId FindServersRequest_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(421L));

  public static final NodeId FindServersRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(422L));

  public static final NodeId FindServersResponse = new NodeId(UShort.MIN, uint(423L));

  public static final NodeId FindServersResponse_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(424L));

  public static final NodeId FindServersResponse_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(425L));

  public static final NodeId GetEndpointsRequest = new NodeId(UShort.MIN, uint(426L));

  public static final NodeId GetEndpointsRequest_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(427L));

  public static final NodeId GetEndpointsRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(428L));

  public static final NodeId GetEndpointsResponse = new NodeId(UShort.MIN, uint(429L));

  public static final NodeId GetEndpointsResponse_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(430L));

  public static final NodeId GetEndpointsResponse_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(431L));

  public static final NodeId RegisteredServer = new NodeId(UShort.MIN, uint(432L));

  public static final NodeId RegisteredServer_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(433L));

  public static final NodeId RegisteredServer_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(434L));

  public static final NodeId RegisterServerRequest = new NodeId(UShort.MIN, uint(435L));

  public static final NodeId RegisterServerRequest_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(436L));

  public static final NodeId RegisterServerRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(437L));

  public static final NodeId RegisterServerResponse = new NodeId(UShort.MIN, uint(438L));

  public static final NodeId RegisterServerResponse_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(439L));

  public static final NodeId RegisterServerResponse_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(440L));

  public static final NodeId ChannelSecurityToken = new NodeId(UShort.MIN, uint(441L));

  public static final NodeId ChannelSecurityToken_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(442L));

  public static final NodeId ChannelSecurityToken_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(443L));

  public static final NodeId OpenSecureChannelRequest = new NodeId(UShort.MIN, uint(444L));

  public static final NodeId OpenSecureChannelRequest_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(445L));

  public static final NodeId OpenSecureChannelRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(446L));

  public static final NodeId OpenSecureChannelResponse = new NodeId(UShort.MIN, uint(447L));

  public static final NodeId OpenSecureChannelResponse_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(448L));

  public static final NodeId OpenSecureChannelResponse_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(449L));

  public static final NodeId CloseSecureChannelRequest = new NodeId(UShort.MIN, uint(450L));

  public static final NodeId CloseSecureChannelRequest_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(451L));

  public static final NodeId CloseSecureChannelRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(452L));

  public static final NodeId CloseSecureChannelResponse = new NodeId(UShort.MIN, uint(453L));

  public static final NodeId CloseSecureChannelResponse_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(454L));

  public static final NodeId CloseSecureChannelResponse_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(455L));

  public static final NodeId SignatureData = new NodeId(UShort.MIN, uint(456L));

  public static final NodeId SignatureData_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(457L));

  public static final NodeId SignatureData_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(458L));

  public static final NodeId CreateSessionRequest = new NodeId(UShort.MIN, uint(459L));

  public static final NodeId CreateSessionRequest_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(460L));

  public static final NodeId CreateSessionRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(461L));

  public static final NodeId CreateSessionResponse = new NodeId(UShort.MIN, uint(462L));

  public static final NodeId CreateSessionResponse_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(463L));

  public static final NodeId CreateSessionResponse_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(464L));

  public static final NodeId ActivateSessionRequest = new NodeId(UShort.MIN, uint(465L));

  public static final NodeId ActivateSessionRequest_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(466L));

  public static final NodeId ActivateSessionRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(467L));

  public static final NodeId ActivateSessionResponse = new NodeId(UShort.MIN, uint(468L));

  public static final NodeId ActivateSessionResponse_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(469L));

  public static final NodeId ActivateSessionResponse_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(470L));

  public static final NodeId CloseSessionRequest = new NodeId(UShort.MIN, uint(471L));

  public static final NodeId CloseSessionRequest_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(472L));

  public static final NodeId CloseSessionRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(473L));

  public static final NodeId CloseSessionResponse = new NodeId(UShort.MIN, uint(474L));

  public static final NodeId CloseSessionResponse_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(475L));

  public static final NodeId CloseSessionResponse_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(476L));

  public static final NodeId CancelRequest = new NodeId(UShort.MIN, uint(477L));

  public static final NodeId CancelRequest_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(478L));

  public static final NodeId CancelRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(479L));

  public static final NodeId CancelResponse = new NodeId(UShort.MIN, uint(480L));

  public static final NodeId CancelResponse_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(481L));

  public static final NodeId CancelResponse_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(482L));

  public static final NodeId AddNodesResult = new NodeId(UShort.MIN, uint(483L));

  public static final NodeId AddNodesResult_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(484L));

  public static final NodeId AddNodesResult_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(485L));

  public static final NodeId AddNodesRequest = new NodeId(UShort.MIN, uint(486L));

  public static final NodeId AddNodesRequest_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(487L));

  public static final NodeId AddNodesRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(488L));

  public static final NodeId AddNodesResponse = new NodeId(UShort.MIN, uint(489L));

  public static final NodeId AddNodesResponse_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(490L));

  public static final NodeId AddNodesResponse_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(491L));

  public static final NodeId AddReferencesRequest = new NodeId(UShort.MIN, uint(492L));

  public static final NodeId AddReferencesRequest_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(493L));

  public static final NodeId AddReferencesRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(494L));

  public static final NodeId AddReferencesResponse = new NodeId(UShort.MIN, uint(495L));

  public static final NodeId AddReferencesResponse_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(496L));

  public static final NodeId AddReferencesResponse_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(497L));

  public static final NodeId DeleteNodesRequest = new NodeId(UShort.MIN, uint(498L));

  public static final NodeId DeleteNodesRequest_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(499L));

  public static final NodeId DeleteNodesRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(500L));

  public static final NodeId DeleteNodesResponse = new NodeId(UShort.MIN, uint(501L));

  public static final NodeId DeleteNodesResponse_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(502L));

  public static final NodeId DeleteNodesResponse_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(503L));

  public static final NodeId DeleteReferencesRequest = new NodeId(UShort.MIN, uint(504L));

  public static final NodeId DeleteReferencesRequest_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(505L));

  public static final NodeId DeleteReferencesRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(506L));

  public static final NodeId DeleteReferencesResponse = new NodeId(UShort.MIN, uint(507L));

  public static final NodeId DeleteReferencesResponse_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(508L));

  public static final NodeId DeleteReferencesResponse_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(509L));

  public static final NodeId BrowseDirection = new NodeId(UShort.MIN, uint(510L));

  public static final NodeId ViewDescription = new NodeId(UShort.MIN, uint(511L));

  public static final NodeId ViewDescription_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(512L));

  public static final NodeId ViewDescription_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(513L));

  public static final NodeId BrowseDescription = new NodeId(UShort.MIN, uint(514L));

  public static final NodeId BrowseDescription_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(515L));

  public static final NodeId BrowseDescription_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(516L));

  public static final NodeId BrowseResultMask = new NodeId(UShort.MIN, uint(517L));

  public static final NodeId ReferenceDescription = new NodeId(UShort.MIN, uint(518L));

  public static final NodeId ReferenceDescription_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(519L));

  public static final NodeId ReferenceDescription_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(520L));

  public static final NodeId ContinuationPoint = new NodeId(UShort.MIN, uint(521L));

  public static final NodeId BrowseResult = new NodeId(UShort.MIN, uint(522L));

  public static final NodeId BrowseResult_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(523L));

  public static final NodeId BrowseResult_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(524L));

  public static final NodeId BrowseRequest = new NodeId(UShort.MIN, uint(525L));

  public static final NodeId BrowseRequest_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(526L));

  public static final NodeId BrowseRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(527L));

  public static final NodeId BrowseResponse = new NodeId(UShort.MIN, uint(528L));

  public static final NodeId BrowseResponse_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(529L));

  public static final NodeId BrowseResponse_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(530L));

  public static final NodeId BrowseNextRequest = new NodeId(UShort.MIN, uint(531L));

  public static final NodeId BrowseNextRequest_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(532L));

  public static final NodeId BrowseNextRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(533L));

  public static final NodeId BrowseNextResponse = new NodeId(UShort.MIN, uint(534L));

  public static final NodeId BrowseNextResponse_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(535L));

  public static final NodeId BrowseNextResponse_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(536L));

  public static final NodeId RelativePathElement = new NodeId(UShort.MIN, uint(537L));

  public static final NodeId RelativePathElement_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(538L));

  public static final NodeId RelativePathElement_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(539L));

  public static final NodeId RelativePath = new NodeId(UShort.MIN, uint(540L));

  public static final NodeId RelativePath_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(541L));

  public static final NodeId RelativePath_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(542L));

  public static final NodeId BrowsePath = new NodeId(UShort.MIN, uint(543L));

  public static final NodeId BrowsePath_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(544L));

  public static final NodeId BrowsePath_Encoding_DefaultBinary = new NodeId(UShort.MIN, uint(545L));

  public static final NodeId BrowsePathTarget = new NodeId(UShort.MIN, uint(546L));

  public static final NodeId BrowsePathTarget_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(547L));

  public static final NodeId BrowsePathTarget_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(548L));

  public static final NodeId BrowsePathResult = new NodeId(UShort.MIN, uint(549L));

  public static final NodeId BrowsePathResult_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(550L));

  public static final NodeId BrowsePathResult_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(551L));

  public static final NodeId TranslateBrowsePathsToNodeIdsRequest =
      new NodeId(UShort.MIN, uint(552L));

  public static final NodeId TranslateBrowsePathsToNodeIdsRequest_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(553L));

  public static final NodeId TranslateBrowsePathsToNodeIdsRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(554L));

  public static final NodeId TranslateBrowsePathsToNodeIdsResponse =
      new NodeId(UShort.MIN, uint(555L));

  public static final NodeId TranslateBrowsePathsToNodeIdsResponse_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(556L));

  public static final NodeId TranslateBrowsePathsToNodeIdsResponse_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(557L));

  public static final NodeId RegisterNodesRequest = new NodeId(UShort.MIN, uint(558L));

  public static final NodeId RegisterNodesRequest_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(559L));

  public static final NodeId RegisterNodesRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(560L));

  public static final NodeId RegisterNodesResponse = new NodeId(UShort.MIN, uint(561L));

  public static final NodeId RegisterNodesResponse_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(562L));

  public static final NodeId RegisterNodesResponse_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(563L));

  public static final NodeId UnregisterNodesRequest = new NodeId(UShort.MIN, uint(564L));

  public static final NodeId UnregisterNodesRequest_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(565L));

  public static final NodeId UnregisterNodesRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(566L));

  public static final NodeId UnregisterNodesResponse = new NodeId(UShort.MIN, uint(567L));

  public static final NodeId UnregisterNodesResponse_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(568L));

  public static final NodeId UnregisterNodesResponse_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(569L));

  public static final NodeId QueryDataDescription = new NodeId(UShort.MIN, uint(570L));

  public static final NodeId QueryDataDescription_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(571L));

  public static final NodeId QueryDataDescription_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(572L));

  public static final NodeId NodeTypeDescription = new NodeId(UShort.MIN, uint(573L));

  public static final NodeId NodeTypeDescription_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(574L));

  public static final NodeId NodeTypeDescription_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(575L));

  public static final NodeId FilterOperator = new NodeId(UShort.MIN, uint(576L));

  public static final NodeId QueryDataSet = new NodeId(UShort.MIN, uint(577L));

  public static final NodeId QueryDataSet_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(578L));

  public static final NodeId QueryDataSet_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(579L));

  public static final NodeId NodeReference = new NodeId(UShort.MIN, uint(580L));

  public static final NodeId NodeReference_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(581L));

  public static final NodeId NodeReference_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(582L));

  public static final NodeId ContentFilterElement = new NodeId(UShort.MIN, uint(583L));

  public static final NodeId ContentFilterElement_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(584L));

  public static final NodeId ContentFilterElement_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(585L));

  public static final NodeId ContentFilter = new NodeId(UShort.MIN, uint(586L));

  public static final NodeId ContentFilter_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(587L));

  public static final NodeId ContentFilter_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(588L));

  public static final NodeId FilterOperand = new NodeId(UShort.MIN, uint(589L));

  public static final NodeId FilterOperand_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(590L));

  public static final NodeId FilterOperand_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(591L));

  public static final NodeId ElementOperand = new NodeId(UShort.MIN, uint(592L));

  public static final NodeId ElementOperand_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(593L));

  public static final NodeId ElementOperand_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(594L));

  public static final NodeId LiteralOperand = new NodeId(UShort.MIN, uint(595L));

  public static final NodeId LiteralOperand_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(596L));

  public static final NodeId LiteralOperand_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(597L));

  public static final NodeId AttributeOperand = new NodeId(UShort.MIN, uint(598L));

  public static final NodeId AttributeOperand_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(599L));

  public static final NodeId AttributeOperand_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(600L));

  public static final NodeId SimpleAttributeOperand = new NodeId(UShort.MIN, uint(601L));

  public static final NodeId SimpleAttributeOperand_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(602L));

  public static final NodeId SimpleAttributeOperand_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(603L));

  public static final NodeId ContentFilterElementResult = new NodeId(UShort.MIN, uint(604L));

  public static final NodeId ContentFilterElementResult_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(605L));

  public static final NodeId ContentFilterElementResult_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(606L));

  public static final NodeId ContentFilterResult = new NodeId(UShort.MIN, uint(607L));

  public static final NodeId ContentFilterResult_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(608L));

  public static final NodeId ContentFilterResult_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(609L));

  public static final NodeId ParsingResult = new NodeId(UShort.MIN, uint(610L));

  public static final NodeId ParsingResult_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(611L));

  public static final NodeId ParsingResult_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(612L));

  public static final NodeId QueryFirstRequest = new NodeId(UShort.MIN, uint(613L));

  public static final NodeId QueryFirstRequest_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(614L));

  public static final NodeId QueryFirstRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(615L));

  public static final NodeId QueryFirstResponse = new NodeId(UShort.MIN, uint(616L));

  public static final NodeId QueryFirstResponse_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(617L));

  public static final NodeId QueryFirstResponse_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(618L));

  public static final NodeId QueryNextRequest = new NodeId(UShort.MIN, uint(619L));

  public static final NodeId QueryNextRequest_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(620L));

  public static final NodeId QueryNextRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(621L));

  public static final NodeId QueryNextResponse = new NodeId(UShort.MIN, uint(622L));

  public static final NodeId QueryNextResponse_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(623L));

  public static final NodeId QueryNextResponse_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(624L));

  public static final NodeId TimestampsToReturn = new NodeId(UShort.MIN, uint(625L));

  public static final NodeId ReadValueId = new NodeId(UShort.MIN, uint(626L));

  public static final NodeId ReadValueId_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(627L));

  public static final NodeId ReadValueId_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(628L));

  public static final NodeId ReadRequest = new NodeId(UShort.MIN, uint(629L));

  public static final NodeId ReadRequest_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(630L));

  public static final NodeId ReadRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(631L));

  public static final NodeId ReadResponse = new NodeId(UShort.MIN, uint(632L));

  public static final NodeId ReadResponse_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(633L));

  public static final NodeId ReadResponse_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(634L));

  public static final NodeId HistoryReadValueId = new NodeId(UShort.MIN, uint(635L));

  public static final NodeId HistoryReadValueId_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(636L));

  public static final NodeId HistoryReadValueId_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(637L));

  public static final NodeId HistoryReadResult = new NodeId(UShort.MIN, uint(638L));

  public static final NodeId HistoryReadResult_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(639L));

  public static final NodeId HistoryReadResult_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(640L));

  public static final NodeId HistoryReadDetails = new NodeId(UShort.MIN, uint(641L));

  public static final NodeId HistoryReadDetails_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(642L));

  public static final NodeId HistoryReadDetails_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(643L));

  public static final NodeId ReadEventDetails = new NodeId(UShort.MIN, uint(644L));

  public static final NodeId ReadEventDetails_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(645L));

  public static final NodeId ReadEventDetails_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(646L));

  public static final NodeId ReadRawModifiedDetails = new NodeId(UShort.MIN, uint(647L));

  public static final NodeId ReadRawModifiedDetails_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(648L));

  public static final NodeId ReadRawModifiedDetails_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(649L));

  public static final NodeId ReadProcessedDetails = new NodeId(UShort.MIN, uint(650L));

  public static final NodeId ReadProcessedDetails_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(651L));

  public static final NodeId ReadProcessedDetails_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(652L));

  public static final NodeId ReadAtTimeDetails = new NodeId(UShort.MIN, uint(653L));

  public static final NodeId ReadAtTimeDetails_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(654L));

  public static final NodeId ReadAtTimeDetails_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(655L));

  public static final NodeId HistoryData = new NodeId(UShort.MIN, uint(656L));

  public static final NodeId HistoryData_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(657L));

  public static final NodeId HistoryData_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(658L));

  public static final NodeId HistoryEvent = new NodeId(UShort.MIN, uint(659L));

  public static final NodeId HistoryEvent_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(660L));

  public static final NodeId HistoryEvent_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(661L));

  public static final NodeId HistoryReadRequest = new NodeId(UShort.MIN, uint(662L));

  public static final NodeId HistoryReadRequest_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(663L));

  public static final NodeId HistoryReadRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(664L));

  public static final NodeId HistoryReadResponse = new NodeId(UShort.MIN, uint(665L));

  public static final NodeId HistoryReadResponse_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(666L));

  public static final NodeId HistoryReadResponse_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(667L));

  public static final NodeId WriteValue = new NodeId(UShort.MIN, uint(668L));

  public static final NodeId WriteValue_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(669L));

  public static final NodeId WriteValue_Encoding_DefaultBinary = new NodeId(UShort.MIN, uint(670L));

  public static final NodeId WriteRequest = new NodeId(UShort.MIN, uint(671L));

  public static final NodeId WriteRequest_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(672L));

  public static final NodeId WriteRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(673L));

  public static final NodeId WriteResponse = new NodeId(UShort.MIN, uint(674L));

  public static final NodeId WriteResponse_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(675L));

  public static final NodeId WriteResponse_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(676L));

  public static final NodeId HistoryUpdateDetails = new NodeId(UShort.MIN, uint(677L));

  public static final NodeId HistoryUpdateDetails_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(678L));

  public static final NodeId HistoryUpdateDetails_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(679L));

  public static final NodeId UpdateDataDetails = new NodeId(UShort.MIN, uint(680L));

  public static final NodeId UpdateDataDetails_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(681L));

  public static final NodeId UpdateDataDetails_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(682L));

  public static final NodeId UpdateEventDetails = new NodeId(UShort.MIN, uint(683L));

  public static final NodeId UpdateEventDetails_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(684L));

  public static final NodeId UpdateEventDetails_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(685L));

  public static final NodeId DeleteRawModifiedDetails = new NodeId(UShort.MIN, uint(686L));

  public static final NodeId DeleteRawModifiedDetails_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(687L));

  public static final NodeId DeleteRawModifiedDetails_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(688L));

  public static final NodeId DeleteAtTimeDetails = new NodeId(UShort.MIN, uint(689L));

  public static final NodeId DeleteAtTimeDetails_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(690L));

  public static final NodeId DeleteAtTimeDetails_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(691L));

  public static final NodeId DeleteEventDetails = new NodeId(UShort.MIN, uint(692L));

  public static final NodeId DeleteEventDetails_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(693L));

  public static final NodeId DeleteEventDetails_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(694L));

  public static final NodeId HistoryUpdateResult = new NodeId(UShort.MIN, uint(695L));

  public static final NodeId HistoryUpdateResult_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(696L));

  public static final NodeId HistoryUpdateResult_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(697L));

  public static final NodeId HistoryUpdateRequest = new NodeId(UShort.MIN, uint(698L));

  public static final NodeId HistoryUpdateRequest_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(699L));

  public static final NodeId HistoryUpdateRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(700L));

  public static final NodeId HistoryUpdateResponse = new NodeId(UShort.MIN, uint(701L));

  public static final NodeId HistoryUpdateResponse_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(702L));

  public static final NodeId HistoryUpdateResponse_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(703L));

  public static final NodeId CallMethodRequest = new NodeId(UShort.MIN, uint(704L));

  public static final NodeId CallMethodRequest_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(705L));

  public static final NodeId CallMethodRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(706L));

  public static final NodeId CallMethodResult = new NodeId(UShort.MIN, uint(707L));

  public static final NodeId CallMethodResult_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(708L));

  public static final NodeId CallMethodResult_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(709L));

  public static final NodeId CallRequest = new NodeId(UShort.MIN, uint(710L));

  public static final NodeId CallRequest_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(711L));

  public static final NodeId CallRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(712L));

  public static final NodeId CallResponse = new NodeId(UShort.MIN, uint(713L));

  public static final NodeId CallResponse_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(714L));

  public static final NodeId CallResponse_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(715L));

  public static final NodeId MonitoringMode = new NodeId(UShort.MIN, uint(716L));

  public static final NodeId DataChangeTrigger = new NodeId(UShort.MIN, uint(717L));

  public static final NodeId DeadbandType = new NodeId(UShort.MIN, uint(718L));

  public static final NodeId MonitoringFilter = new NodeId(UShort.MIN, uint(719L));

  public static final NodeId MonitoringFilter_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(720L));

  public static final NodeId MonitoringFilter_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(721L));

  public static final NodeId DataChangeFilter = new NodeId(UShort.MIN, uint(722L));

  public static final NodeId DataChangeFilter_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(723L));

  public static final NodeId DataChangeFilter_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(724L));

  public static final NodeId EventFilter = new NodeId(UShort.MIN, uint(725L));

  public static final NodeId EventFilter_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(726L));

  public static final NodeId EventFilter_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(727L));

  public static final NodeId AggregateFilter = new NodeId(UShort.MIN, uint(728L));

  public static final NodeId AggregateFilter_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(729L));

  public static final NodeId AggregateFilter_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(730L));

  public static final NodeId MonitoringFilterResult = new NodeId(UShort.MIN, uint(731L));

  public static final NodeId MonitoringFilterResult_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(732L));

  public static final NodeId MonitoringFilterResult_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(733L));

  public static final NodeId EventFilterResult = new NodeId(UShort.MIN, uint(734L));

  public static final NodeId EventFilterResult_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(735L));

  public static final NodeId EventFilterResult_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(736L));

  public static final NodeId AggregateFilterResult = new NodeId(UShort.MIN, uint(737L));

  public static final NodeId AggregateFilterResult_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(738L));

  public static final NodeId AggregateFilterResult_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(739L));

  public static final NodeId MonitoringParameters = new NodeId(UShort.MIN, uint(740L));

  public static final NodeId MonitoringParameters_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(741L));

  public static final NodeId MonitoringParameters_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(742L));

  public static final NodeId MonitoredItemCreateRequest = new NodeId(UShort.MIN, uint(743L));

  public static final NodeId MonitoredItemCreateRequest_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(744L));

  public static final NodeId MonitoredItemCreateRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(745L));

  public static final NodeId MonitoredItemCreateResult = new NodeId(UShort.MIN, uint(746L));

  public static final NodeId MonitoredItemCreateResult_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(747L));

  public static final NodeId MonitoredItemCreateResult_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(748L));

  public static final NodeId CreateMonitoredItemsRequest = new NodeId(UShort.MIN, uint(749L));

  public static final NodeId CreateMonitoredItemsRequest_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(750L));

  public static final NodeId CreateMonitoredItemsRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(751L));

  public static final NodeId CreateMonitoredItemsResponse = new NodeId(UShort.MIN, uint(752L));

  public static final NodeId CreateMonitoredItemsResponse_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(753L));

  public static final NodeId CreateMonitoredItemsResponse_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(754L));

  public static final NodeId MonitoredItemModifyRequest = new NodeId(UShort.MIN, uint(755L));

  public static final NodeId MonitoredItemModifyRequest_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(756L));

  public static final NodeId MonitoredItemModifyRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(757L));

  public static final NodeId MonitoredItemModifyResult = new NodeId(UShort.MIN, uint(758L));

  public static final NodeId MonitoredItemModifyResult_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(759L));

  public static final NodeId MonitoredItemModifyResult_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(760L));

  public static final NodeId ModifyMonitoredItemsRequest = new NodeId(UShort.MIN, uint(761L));

  public static final NodeId ModifyMonitoredItemsRequest_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(762L));

  public static final NodeId ModifyMonitoredItemsRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(763L));

  public static final NodeId ModifyMonitoredItemsResponse = new NodeId(UShort.MIN, uint(764L));

  public static final NodeId ModifyMonitoredItemsResponse_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(765L));

  public static final NodeId ModifyMonitoredItemsResponse_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(766L));

  public static final NodeId SetMonitoringModeRequest = new NodeId(UShort.MIN, uint(767L));

  public static final NodeId SetMonitoringModeRequest_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(768L));

  public static final NodeId SetMonitoringModeRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(769L));

  public static final NodeId SetMonitoringModeResponse = new NodeId(UShort.MIN, uint(770L));

  public static final NodeId SetMonitoringModeResponse_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(771L));

  public static final NodeId SetMonitoringModeResponse_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(772L));

  public static final NodeId SetTriggeringRequest = new NodeId(UShort.MIN, uint(773L));

  public static final NodeId SetTriggeringRequest_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(774L));

  public static final NodeId SetTriggeringRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(775L));

  public static final NodeId SetTriggeringResponse = new NodeId(UShort.MIN, uint(776L));

  public static final NodeId SetTriggeringResponse_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(777L));

  public static final NodeId SetTriggeringResponse_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(778L));

  public static final NodeId DeleteMonitoredItemsRequest = new NodeId(UShort.MIN, uint(779L));

  public static final NodeId DeleteMonitoredItemsRequest_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(780L));

  public static final NodeId DeleteMonitoredItemsRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(781L));

  public static final NodeId DeleteMonitoredItemsResponse = new NodeId(UShort.MIN, uint(782L));

  public static final NodeId DeleteMonitoredItemsResponse_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(783L));

  public static final NodeId DeleteMonitoredItemsResponse_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(784L));

  public static final NodeId CreateSubscriptionRequest = new NodeId(UShort.MIN, uint(785L));

  public static final NodeId CreateSubscriptionRequest_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(786L));

  public static final NodeId CreateSubscriptionRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(787L));

  public static final NodeId CreateSubscriptionResponse = new NodeId(UShort.MIN, uint(788L));

  public static final NodeId CreateSubscriptionResponse_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(789L));

  public static final NodeId CreateSubscriptionResponse_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(790L));

  public static final NodeId ModifySubscriptionRequest = new NodeId(UShort.MIN, uint(791L));

  public static final NodeId ModifySubscriptionRequest_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(792L));

  public static final NodeId ModifySubscriptionRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(793L));

  public static final NodeId ModifySubscriptionResponse = new NodeId(UShort.MIN, uint(794L));

  public static final NodeId ModifySubscriptionResponse_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(795L));

  public static final NodeId ModifySubscriptionResponse_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(796L));

  public static final NodeId SetPublishingModeRequest = new NodeId(UShort.MIN, uint(797L));

  public static final NodeId SetPublishingModeRequest_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(798L));

  public static final NodeId SetPublishingModeRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(799L));

  public static final NodeId SetPublishingModeResponse = new NodeId(UShort.MIN, uint(800L));

  public static final NodeId SetPublishingModeResponse_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(801L));

  public static final NodeId SetPublishingModeResponse_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(802L));

  public static final NodeId NotificationMessage = new NodeId(UShort.MIN, uint(803L));

  public static final NodeId NotificationMessage_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(804L));

  public static final NodeId NotificationMessage_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(805L));

  public static final NodeId MonitoredItemNotification = new NodeId(UShort.MIN, uint(806L));

  public static final NodeId MonitoredItemNotification_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(807L));

  public static final NodeId MonitoredItemNotification_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(808L));

  public static final NodeId DataChangeNotification = new NodeId(UShort.MIN, uint(809L));

  public static final NodeId DataChangeNotification_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(810L));

  public static final NodeId DataChangeNotification_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(811L));

  public static final NodeId StatusChangeNotification = new NodeId(UShort.MIN, uint(818L));

  public static final NodeId StatusChangeNotification_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(819L));

  public static final NodeId StatusChangeNotification_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(820L));

  public static final NodeId SubscriptionAcknowledgement = new NodeId(UShort.MIN, uint(821L));

  public static final NodeId SubscriptionAcknowledgement_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(822L));

  public static final NodeId SubscriptionAcknowledgement_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(823L));

  public static final NodeId PublishRequest = new NodeId(UShort.MIN, uint(824L));

  public static final NodeId PublishRequest_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(825L));

  public static final NodeId PublishRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(826L));

  public static final NodeId PublishResponse = new NodeId(UShort.MIN, uint(827L));

  public static final NodeId PublishResponse_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(828L));

  public static final NodeId PublishResponse_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(829L));

  public static final NodeId RepublishRequest = new NodeId(UShort.MIN, uint(830L));

  public static final NodeId RepublishRequest_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(831L));

  public static final NodeId RepublishRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(832L));

  public static final NodeId RepublishResponse = new NodeId(UShort.MIN, uint(833L));

  public static final NodeId RepublishResponse_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(834L));

  public static final NodeId RepublishResponse_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(835L));

  public static final NodeId TransferResult = new NodeId(UShort.MIN, uint(836L));

  public static final NodeId TransferResult_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(837L));

  public static final NodeId TransferResult_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(838L));

  public static final NodeId TransferSubscriptionsRequest = new NodeId(UShort.MIN, uint(839L));

  public static final NodeId TransferSubscriptionsRequest_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(840L));

  public static final NodeId TransferSubscriptionsRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(841L));

  public static final NodeId TransferSubscriptionsResponse = new NodeId(UShort.MIN, uint(842L));

  public static final NodeId TransferSubscriptionsResponse_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(843L));

  public static final NodeId TransferSubscriptionsResponse_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(844L));

  public static final NodeId DeleteSubscriptionsRequest = new NodeId(UShort.MIN, uint(845L));

  public static final NodeId DeleteSubscriptionsRequest_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(846L));

  public static final NodeId DeleteSubscriptionsRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(847L));

  public static final NodeId DeleteSubscriptionsResponse = new NodeId(UShort.MIN, uint(848L));

  public static final NodeId DeleteSubscriptionsResponse_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(849L));

  public static final NodeId DeleteSubscriptionsResponse_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(850L));

  public static final NodeId RedundancySupport = new NodeId(UShort.MIN, uint(851L));

  public static final NodeId ServerState = new NodeId(UShort.MIN, uint(852L));

  public static final NodeId RedundantServerDataType = new NodeId(UShort.MIN, uint(853L));

  public static final NodeId RedundantServerDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(854L));

  public static final NodeId RedundantServerDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(855L));

  public static final NodeId SamplingIntervalDiagnosticsDataType =
      new NodeId(UShort.MIN, uint(856L));

  public static final NodeId SamplingIntervalDiagnosticsDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(857L));

  public static final NodeId SamplingIntervalDiagnosticsDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(858L));

  public static final NodeId ServerDiagnosticsSummaryDataType = new NodeId(UShort.MIN, uint(859L));

  public static final NodeId ServerDiagnosticsSummaryDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(860L));

  public static final NodeId ServerDiagnosticsSummaryDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(861L));

  public static final NodeId ServerStatusDataType = new NodeId(UShort.MIN, uint(862L));

  public static final NodeId ServerStatusDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(863L));

  public static final NodeId ServerStatusDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(864L));

  public static final NodeId SessionDiagnosticsDataType = new NodeId(UShort.MIN, uint(865L));

  public static final NodeId SessionDiagnosticsDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(866L));

  public static final NodeId SessionDiagnosticsDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(867L));

  public static final NodeId SessionSecurityDiagnosticsDataType =
      new NodeId(UShort.MIN, uint(868L));

  public static final NodeId SessionSecurityDiagnosticsDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(869L));

  public static final NodeId SessionSecurityDiagnosticsDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(870L));

  public static final NodeId ServiceCounterDataType = new NodeId(UShort.MIN, uint(871L));

  public static final NodeId ServiceCounterDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(872L));

  public static final NodeId ServiceCounterDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(873L));

  public static final NodeId SubscriptionDiagnosticsDataType = new NodeId(UShort.MIN, uint(874L));

  public static final NodeId SubscriptionDiagnosticsDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(875L));

  public static final NodeId SubscriptionDiagnosticsDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(876L));

  public static final NodeId ModelChangeStructureDataType = new NodeId(UShort.MIN, uint(877L));

  public static final NodeId ModelChangeStructureDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(878L));

  public static final NodeId ModelChangeStructureDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(879L));

  public static final NodeId Range = new NodeId(UShort.MIN, uint(884L));

  public static final NodeId Range_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(885L));

  public static final NodeId Range_Encoding_DefaultBinary = new NodeId(UShort.MIN, uint(886L));

  public static final NodeId EUInformation = new NodeId(UShort.MIN, uint(887L));

  public static final NodeId EUInformation_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(888L));

  public static final NodeId EUInformation_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(889L));

  public static final NodeId ExceptionDeviationFormat = new NodeId(UShort.MIN, uint(890L));

  public static final NodeId Annotation = new NodeId(UShort.MIN, uint(891L));

  public static final NodeId Annotation_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(892L));

  public static final NodeId Annotation_Encoding_DefaultBinary = new NodeId(UShort.MIN, uint(893L));

  public static final NodeId ProgramDiagnosticDataType = new NodeId(UShort.MIN, uint(894L));

  public static final NodeId ProgramDiagnosticDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(895L));

  public static final NodeId ProgramDiagnosticDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(896L));

  public static final NodeId SemanticChangeStructureDataType = new NodeId(UShort.MIN, uint(897L));

  public static final NodeId SemanticChangeStructureDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(898L));

  public static final NodeId SemanticChangeStructureDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(899L));

  public static final NodeId EventNotificationList = new NodeId(UShort.MIN, uint(914L));

  public static final NodeId EventNotificationList_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(915L));

  public static final NodeId EventNotificationList_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(916L));

  public static final NodeId EventFieldList = new NodeId(UShort.MIN, uint(917L));

  public static final NodeId EventFieldList_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(918L));

  public static final NodeId EventFieldList_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(919L));

  public static final NodeId HistoryEventFieldList = new NodeId(UShort.MIN, uint(920L));

  public static final NodeId HistoryEventFieldList_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(921L));

  public static final NodeId HistoryEventFieldList_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(922L));

  public static final NodeId IssuedIdentityToken = new NodeId(UShort.MIN, uint(938L));

  public static final NodeId IssuedIdentityToken_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(939L));

  public static final NodeId IssuedIdentityToken_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(940L));

  public static final NodeId NotificationData = new NodeId(UShort.MIN, uint(945L));

  public static final NodeId NotificationData_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(946L));

  public static final NodeId NotificationData_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(947L));

  public static final NodeId AggregateConfiguration = new NodeId(UShort.MIN, uint(948L));

  public static final NodeId AggregateConfiguration_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(949L));

  public static final NodeId AggregateConfiguration_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(950L));

  public static final NodeId ImageBMP = new NodeId(UShort.MIN, uint(2000L));

  public static final NodeId ImageGIF = new NodeId(UShort.MIN, uint(2001L));

  public static final NodeId ImageJPG = new NodeId(UShort.MIN, uint(2002L));

  public static final NodeId ImagePNG = new NodeId(UShort.MIN, uint(2003L));

  public static final NodeId ServerType = new NodeId(UShort.MIN, uint(2004L));

  public static final NodeId ServerType_ServerArray = new NodeId(UShort.MIN, uint(2005L));

  public static final NodeId ServerType_NamespaceArray = new NodeId(UShort.MIN, uint(2006L));

  public static final NodeId ServerType_ServerStatus = new NodeId(UShort.MIN, uint(2007L));

  public static final NodeId ServerType_ServiceLevel = new NodeId(UShort.MIN, uint(2008L));

  public static final NodeId ServerType_ServerCapabilities = new NodeId(UShort.MIN, uint(2009L));

  public static final NodeId ServerType_ServerDiagnostics = new NodeId(UShort.MIN, uint(2010L));

  public static final NodeId ServerType_VendorServerInfo = new NodeId(UShort.MIN, uint(2011L));

  public static final NodeId ServerType_ServerRedundancy = new NodeId(UShort.MIN, uint(2012L));

  public static final NodeId ServerCapabilitiesType = new NodeId(UShort.MIN, uint(2013L));

  public static final NodeId ServerCapabilitiesType_ServerProfileArray =
      new NodeId(UShort.MIN, uint(2014L));

  public static final NodeId ServerCapabilitiesType_LocaleIdArray =
      new NodeId(UShort.MIN, uint(2016L));

  public static final NodeId ServerCapabilitiesType_MinSupportedSampleRate =
      new NodeId(UShort.MIN, uint(2017L));

  public static final NodeId ServerCapabilitiesType_ModellingRules =
      new NodeId(UShort.MIN, uint(2019L));

  public static final NodeId ServerDiagnosticsType = new NodeId(UShort.MIN, uint(2020L));

  public static final NodeId ServerDiagnosticsType_ServerDiagnosticsSummary =
      new NodeId(UShort.MIN, uint(2021L));

  public static final NodeId ServerDiagnosticsType_SamplingIntervalDiagnosticsArray =
      new NodeId(UShort.MIN, uint(2022L));

  public static final NodeId ServerDiagnosticsType_SubscriptionDiagnosticsArray =
      new NodeId(UShort.MIN, uint(2023L));

  public static final NodeId ServerDiagnosticsType_EnabledFlag =
      new NodeId(UShort.MIN, uint(2025L));

  public static final NodeId SessionsDiagnosticsSummaryType = new NodeId(UShort.MIN, uint(2026L));

  public static final NodeId SessionsDiagnosticsSummaryType_SessionDiagnosticsArray =
      new NodeId(UShort.MIN, uint(2027L));

  public static final NodeId SessionsDiagnosticsSummaryType_SessionSecurityDiagnosticsArray =
      new NodeId(UShort.MIN, uint(2028L));

  public static final NodeId SessionDiagnosticsObjectType = new NodeId(UShort.MIN, uint(2029L));

  public static final NodeId SessionDiagnosticsObjectType_SessionDiagnostics =
      new NodeId(UShort.MIN, uint(2030L));

  public static final NodeId SessionDiagnosticsObjectType_SessionSecurityDiagnostics =
      new NodeId(UShort.MIN, uint(2031L));

  public static final NodeId SessionDiagnosticsObjectType_SubscriptionDiagnosticsArray =
      new NodeId(UShort.MIN, uint(2032L));

  public static final NodeId VendorServerInfoType = new NodeId(UShort.MIN, uint(2033L));

  public static final NodeId ServerRedundancyType = new NodeId(UShort.MIN, uint(2034L));

  public static final NodeId ServerRedundancyType_RedundancySupport =
      new NodeId(UShort.MIN, uint(2035L));

  public static final NodeId TransparentRedundancyType = new NodeId(UShort.MIN, uint(2036L));

  public static final NodeId TransparentRedundancyType_CurrentServerId =
      new NodeId(UShort.MIN, uint(2037L));

  public static final NodeId TransparentRedundancyType_RedundantServerArray =
      new NodeId(UShort.MIN, uint(2038L));

  public static final NodeId NonTransparentRedundancyType = new NodeId(UShort.MIN, uint(2039L));

  public static final NodeId NonTransparentRedundancyType_ServerUriArray =
      new NodeId(UShort.MIN, uint(2040L));

  public static final NodeId BaseEventType = new NodeId(UShort.MIN, uint(2041L));

  public static final NodeId BaseEventType_EventId = new NodeId(UShort.MIN, uint(2042L));

  public static final NodeId BaseEventType_EventType = new NodeId(UShort.MIN, uint(2043L));

  public static final NodeId BaseEventType_SourceNode = new NodeId(UShort.MIN, uint(2044L));

  public static final NodeId BaseEventType_SourceName = new NodeId(UShort.MIN, uint(2045L));

  public static final NodeId BaseEventType_Time = new NodeId(UShort.MIN, uint(2046L));

  public static final NodeId BaseEventType_ReceiveTime = new NodeId(UShort.MIN, uint(2047L));

  public static final NodeId BaseEventType_Message = new NodeId(UShort.MIN, uint(2050L));

  public static final NodeId BaseEventType_Severity = new NodeId(UShort.MIN, uint(2051L));

  public static final NodeId AuditEventType = new NodeId(UShort.MIN, uint(2052L));

  public static final NodeId AuditEventType_ActionTimeStamp = new NodeId(UShort.MIN, uint(2053L));

  public static final NodeId AuditEventType_Status = new NodeId(UShort.MIN, uint(2054L));

  public static final NodeId AuditEventType_ServerId = new NodeId(UShort.MIN, uint(2055L));

  public static final NodeId AuditEventType_ClientAuditEntryId =
      new NodeId(UShort.MIN, uint(2056L));

  public static final NodeId AuditEventType_ClientUserId = new NodeId(UShort.MIN, uint(2057L));

  public static final NodeId AuditSecurityEventType = new NodeId(UShort.MIN, uint(2058L));

  public static final NodeId AuditChannelEventType = new NodeId(UShort.MIN, uint(2059L));

  public static final NodeId AuditOpenSecureChannelEventType = new NodeId(UShort.MIN, uint(2060L));

  public static final NodeId AuditOpenSecureChannelEventType_ClientCertificate =
      new NodeId(UShort.MIN, uint(2061L));

  public static final NodeId AuditOpenSecureChannelEventType_RequestType =
      new NodeId(UShort.MIN, uint(2062L));

  public static final NodeId AuditOpenSecureChannelEventType_SecurityPolicyUri =
      new NodeId(UShort.MIN, uint(2063L));

  public static final NodeId AuditOpenSecureChannelEventType_SecurityMode =
      new NodeId(UShort.MIN, uint(2065L));

  public static final NodeId AuditOpenSecureChannelEventType_RequestedLifetime =
      new NodeId(UShort.MIN, uint(2066L));

  public static final NodeId AuditSessionEventType = new NodeId(UShort.MIN, uint(2069L));

  public static final NodeId AuditSessionEventType_SessionId = new NodeId(UShort.MIN, uint(2070L));

  public static final NodeId AuditCreateSessionEventType = new NodeId(UShort.MIN, uint(2071L));

  public static final NodeId AuditCreateSessionEventType_SecureChannelId =
      new NodeId(UShort.MIN, uint(2072L));

  public static final NodeId AuditCreateSessionEventType_ClientCertificate =
      new NodeId(UShort.MIN, uint(2073L));

  public static final NodeId AuditCreateSessionEventType_RevisedSessionTimeout =
      new NodeId(UShort.MIN, uint(2074L));

  public static final NodeId AuditActivateSessionEventType = new NodeId(UShort.MIN, uint(2075L));

  public static final NodeId AuditActivateSessionEventType_ClientSoftwareCertificates =
      new NodeId(UShort.MIN, uint(2076L));

  public static final NodeId AuditActivateSessionEventType_UserIdentityToken =
      new NodeId(UShort.MIN, uint(2077L));

  public static final NodeId AuditCancelEventType = new NodeId(UShort.MIN, uint(2078L));

  public static final NodeId AuditCancelEventType_RequestHandle =
      new NodeId(UShort.MIN, uint(2079L));

  public static final NodeId AuditCertificateEventType = new NodeId(UShort.MIN, uint(2080L));

  public static final NodeId AuditCertificateEventType_Certificate =
      new NodeId(UShort.MIN, uint(2081L));

  public static final NodeId AuditCertificateDataMismatchEventType =
      new NodeId(UShort.MIN, uint(2082L));

  public static final NodeId AuditCertificateDataMismatchEventType_InvalidHostname =
      new NodeId(UShort.MIN, uint(2083L));

  public static final NodeId AuditCertificateDataMismatchEventType_InvalidUri =
      new NodeId(UShort.MIN, uint(2084L));

  public static final NodeId AuditCertificateExpiredEventType = new NodeId(UShort.MIN, uint(2085L));

  public static final NodeId AuditCertificateInvalidEventType = new NodeId(UShort.MIN, uint(2086L));

  public static final NodeId AuditCertificateUntrustedEventType =
      new NodeId(UShort.MIN, uint(2087L));

  public static final NodeId AuditCertificateRevokedEventType = new NodeId(UShort.MIN, uint(2088L));

  public static final NodeId AuditCertificateMismatchEventType =
      new NodeId(UShort.MIN, uint(2089L));

  public static final NodeId AuditNodeManagementEventType = new NodeId(UShort.MIN, uint(2090L));

  public static final NodeId AuditAddNodesEventType = new NodeId(UShort.MIN, uint(2091L));

  public static final NodeId AuditAddNodesEventType_NodesToAdd =
      new NodeId(UShort.MIN, uint(2092L));

  public static final NodeId AuditDeleteNodesEventType = new NodeId(UShort.MIN, uint(2093L));

  public static final NodeId AuditDeleteNodesEventType_NodesToDelete =
      new NodeId(UShort.MIN, uint(2094L));

  public static final NodeId AuditAddReferencesEventType = new NodeId(UShort.MIN, uint(2095L));

  public static final NodeId AuditAddReferencesEventType_ReferencesToAdd =
      new NodeId(UShort.MIN, uint(2096L));

  public static final NodeId AuditDeleteReferencesEventType = new NodeId(UShort.MIN, uint(2097L));

  public static final NodeId AuditDeleteReferencesEventType_ReferencesToDelete =
      new NodeId(UShort.MIN, uint(2098L));

  public static final NodeId AuditUpdateEventType = new NodeId(UShort.MIN, uint(2099L));

  public static final NodeId AuditWriteUpdateEventType = new NodeId(UShort.MIN, uint(2100L));

  public static final NodeId AuditWriteUpdateEventType_IndexRange =
      new NodeId(UShort.MIN, uint(2101L));

  public static final NodeId AuditWriteUpdateEventType_OldValue =
      new NodeId(UShort.MIN, uint(2102L));

  public static final NodeId AuditWriteUpdateEventType_NewValue =
      new NodeId(UShort.MIN, uint(2103L));

  public static final NodeId AuditHistoryUpdateEventType = new NodeId(UShort.MIN, uint(2104L));

  public static final NodeId AuditUpdateMethodEventType = new NodeId(UShort.MIN, uint(2127L));

  public static final NodeId AuditUpdateMethodEventType_MethodId =
      new NodeId(UShort.MIN, uint(2128L));

  public static final NodeId AuditUpdateMethodEventType_InputArguments =
      new NodeId(UShort.MIN, uint(2129L));

  public static final NodeId SystemEventType = new NodeId(UShort.MIN, uint(2130L));

  public static final NodeId DeviceFailureEventType = new NodeId(UShort.MIN, uint(2131L));

  public static final NodeId BaseModelChangeEventType = new NodeId(UShort.MIN, uint(2132L));

  public static final NodeId GeneralModelChangeEventType = new NodeId(UShort.MIN, uint(2133L));

  public static final NodeId GeneralModelChangeEventType_Changes =
      new NodeId(UShort.MIN, uint(2134L));

  public static final NodeId ServerVendorCapabilityType = new NodeId(UShort.MIN, uint(2137L));

  public static final NodeId ServerStatusType = new NodeId(UShort.MIN, uint(2138L));

  public static final NodeId ServerStatusType_StartTime = new NodeId(UShort.MIN, uint(2139L));

  public static final NodeId ServerStatusType_CurrentTime = new NodeId(UShort.MIN, uint(2140L));

  public static final NodeId ServerStatusType_State = new NodeId(UShort.MIN, uint(2141L));

  public static final NodeId ServerStatusType_BuildInfo = new NodeId(UShort.MIN, uint(2142L));

  public static final NodeId ServerDiagnosticsSummaryType = new NodeId(UShort.MIN, uint(2150L));

  public static final NodeId ServerDiagnosticsSummaryType_ServerViewCount =
      new NodeId(UShort.MIN, uint(2151L));

  public static final NodeId ServerDiagnosticsSummaryType_CurrentSessionCount =
      new NodeId(UShort.MIN, uint(2152L));

  public static final NodeId ServerDiagnosticsSummaryType_CumulatedSessionCount =
      new NodeId(UShort.MIN, uint(2153L));

  public static final NodeId ServerDiagnosticsSummaryType_SecurityRejectedSessionCount =
      new NodeId(UShort.MIN, uint(2154L));

  public static final NodeId ServerDiagnosticsSummaryType_RejectedSessionCount =
      new NodeId(UShort.MIN, uint(2155L));

  public static final NodeId ServerDiagnosticsSummaryType_SessionTimeoutCount =
      new NodeId(UShort.MIN, uint(2156L));

  public static final NodeId ServerDiagnosticsSummaryType_SessionAbortCount =
      new NodeId(UShort.MIN, uint(2157L));

  public static final NodeId ServerDiagnosticsSummaryType_PublishingIntervalCount =
      new NodeId(UShort.MIN, uint(2159L));

  public static final NodeId ServerDiagnosticsSummaryType_CurrentSubscriptionCount =
      new NodeId(UShort.MIN, uint(2160L));

  public static final NodeId ServerDiagnosticsSummaryType_CumulatedSubscriptionCount =
      new NodeId(UShort.MIN, uint(2161L));

  public static final NodeId ServerDiagnosticsSummaryType_SecurityRejectedRequestsCount =
      new NodeId(UShort.MIN, uint(2162L));

  public static final NodeId ServerDiagnosticsSummaryType_RejectedRequestsCount =
      new NodeId(UShort.MIN, uint(2163L));

  public static final NodeId SamplingIntervalDiagnosticsArrayType =
      new NodeId(UShort.MIN, uint(2164L));

  public static final NodeId SamplingIntervalDiagnosticsType = new NodeId(UShort.MIN, uint(2165L));

  public static final NodeId SamplingIntervalDiagnosticsType_SamplingInterval =
      new NodeId(UShort.MIN, uint(2166L));

  public static final NodeId SubscriptionDiagnosticsArrayType = new NodeId(UShort.MIN, uint(2171L));

  public static final NodeId SubscriptionDiagnosticsType = new NodeId(UShort.MIN, uint(2172L));

  public static final NodeId SubscriptionDiagnosticsType_SessionId =
      new NodeId(UShort.MIN, uint(2173L));

  public static final NodeId SubscriptionDiagnosticsType_SubscriptionId =
      new NodeId(UShort.MIN, uint(2174L));

  public static final NodeId SubscriptionDiagnosticsType_Priority =
      new NodeId(UShort.MIN, uint(2175L));

  public static final NodeId SubscriptionDiagnosticsType_PublishingInterval =
      new NodeId(UShort.MIN, uint(2176L));

  public static final NodeId SubscriptionDiagnosticsType_MaxKeepAliveCount =
      new NodeId(UShort.MIN, uint(2177L));

  public static final NodeId SubscriptionDiagnosticsType_MaxNotificationsPerPublish =
      new NodeId(UShort.MIN, uint(2179L));

  public static final NodeId SubscriptionDiagnosticsType_PublishingEnabled =
      new NodeId(UShort.MIN, uint(2180L));

  public static final NodeId SubscriptionDiagnosticsType_ModifyCount =
      new NodeId(UShort.MIN, uint(2181L));

  public static final NodeId SubscriptionDiagnosticsType_EnableCount =
      new NodeId(UShort.MIN, uint(2182L));

  public static final NodeId SubscriptionDiagnosticsType_DisableCount =
      new NodeId(UShort.MIN, uint(2183L));

  public static final NodeId SubscriptionDiagnosticsType_RepublishRequestCount =
      new NodeId(UShort.MIN, uint(2184L));

  public static final NodeId SubscriptionDiagnosticsType_RepublishMessageRequestCount =
      new NodeId(UShort.MIN, uint(2185L));

  public static final NodeId SubscriptionDiagnosticsType_RepublishMessageCount =
      new NodeId(UShort.MIN, uint(2186L));

  public static final NodeId SubscriptionDiagnosticsType_TransferRequestCount =
      new NodeId(UShort.MIN, uint(2187L));

  public static final NodeId SubscriptionDiagnosticsType_TransferredToAltClientCount =
      new NodeId(UShort.MIN, uint(2188L));

  public static final NodeId SubscriptionDiagnosticsType_TransferredToSameClientCount =
      new NodeId(UShort.MIN, uint(2189L));

  public static final NodeId SubscriptionDiagnosticsType_PublishRequestCount =
      new NodeId(UShort.MIN, uint(2190L));

  public static final NodeId SubscriptionDiagnosticsType_DataChangeNotificationsCount =
      new NodeId(UShort.MIN, uint(2191L));

  public static final NodeId SubscriptionDiagnosticsType_NotificationsCount =
      new NodeId(UShort.MIN, uint(2193L));

  public static final NodeId SessionDiagnosticsArrayType = new NodeId(UShort.MIN, uint(2196L));

  public static final NodeId SessionDiagnosticsVariableType = new NodeId(UShort.MIN, uint(2197L));

  public static final NodeId SessionDiagnosticsVariableType_SessionId =
      new NodeId(UShort.MIN, uint(2198L));

  public static final NodeId SessionDiagnosticsVariableType_SessionName =
      new NodeId(UShort.MIN, uint(2199L));

  public static final NodeId SessionDiagnosticsVariableType_ClientDescription =
      new NodeId(UShort.MIN, uint(2200L));

  public static final NodeId SessionDiagnosticsVariableType_ServerUri =
      new NodeId(UShort.MIN, uint(2201L));

  public static final NodeId SessionDiagnosticsVariableType_EndpointUrl =
      new NodeId(UShort.MIN, uint(2202L));

  public static final NodeId SessionDiagnosticsVariableType_LocaleIds =
      new NodeId(UShort.MIN, uint(2203L));

  public static final NodeId SessionDiagnosticsVariableType_ActualSessionTimeout =
      new NodeId(UShort.MIN, uint(2204L));

  public static final NodeId SessionDiagnosticsVariableType_ClientConnectionTime =
      new NodeId(UShort.MIN, uint(2205L));

  public static final NodeId SessionDiagnosticsVariableType_ClientLastContactTime =
      new NodeId(UShort.MIN, uint(2206L));

  public static final NodeId SessionDiagnosticsVariableType_CurrentSubscriptionsCount =
      new NodeId(UShort.MIN, uint(2207L));

  public static final NodeId SessionDiagnosticsVariableType_CurrentMonitoredItemsCount =
      new NodeId(UShort.MIN, uint(2208L));

  public static final NodeId SessionDiagnosticsVariableType_CurrentPublishRequestsInQueue =
      new NodeId(UShort.MIN, uint(2209L));

  public static final NodeId SessionDiagnosticsVariableType_ReadCount =
      new NodeId(UShort.MIN, uint(2217L));

  public static final NodeId SessionDiagnosticsVariableType_HistoryReadCount =
      new NodeId(UShort.MIN, uint(2218L));

  public static final NodeId SessionDiagnosticsVariableType_WriteCount =
      new NodeId(UShort.MIN, uint(2219L));

  public static final NodeId SessionDiagnosticsVariableType_HistoryUpdateCount =
      new NodeId(UShort.MIN, uint(2220L));

  public static final NodeId SessionDiagnosticsVariableType_CallCount =
      new NodeId(UShort.MIN, uint(2221L));

  public static final NodeId SessionDiagnosticsVariableType_CreateMonitoredItemsCount =
      new NodeId(UShort.MIN, uint(2222L));

  public static final NodeId SessionDiagnosticsVariableType_ModifyMonitoredItemsCount =
      new NodeId(UShort.MIN, uint(2223L));

  public static final NodeId SessionDiagnosticsVariableType_SetMonitoringModeCount =
      new NodeId(UShort.MIN, uint(2224L));

  public static final NodeId SessionDiagnosticsVariableType_SetTriggeringCount =
      new NodeId(UShort.MIN, uint(2225L));

  public static final NodeId SessionDiagnosticsVariableType_DeleteMonitoredItemsCount =
      new NodeId(UShort.MIN, uint(2226L));

  public static final NodeId SessionDiagnosticsVariableType_CreateSubscriptionCount =
      new NodeId(UShort.MIN, uint(2227L));

  public static final NodeId SessionDiagnosticsVariableType_ModifySubscriptionCount =
      new NodeId(UShort.MIN, uint(2228L));

  public static final NodeId SessionDiagnosticsVariableType_SetPublishingModeCount =
      new NodeId(UShort.MIN, uint(2229L));

  public static final NodeId SessionDiagnosticsVariableType_PublishCount =
      new NodeId(UShort.MIN, uint(2230L));

  public static final NodeId SessionDiagnosticsVariableType_RepublishCount =
      new NodeId(UShort.MIN, uint(2231L));

  public static final NodeId SessionDiagnosticsVariableType_TransferSubscriptionsCount =
      new NodeId(UShort.MIN, uint(2232L));

  public static final NodeId SessionDiagnosticsVariableType_DeleteSubscriptionsCount =
      new NodeId(UShort.MIN, uint(2233L));

  public static final NodeId SessionDiagnosticsVariableType_AddNodesCount =
      new NodeId(UShort.MIN, uint(2234L));

  public static final NodeId SessionDiagnosticsVariableType_AddReferencesCount =
      new NodeId(UShort.MIN, uint(2235L));

  public static final NodeId SessionDiagnosticsVariableType_DeleteNodesCount =
      new NodeId(UShort.MIN, uint(2236L));

  public static final NodeId SessionDiagnosticsVariableType_DeleteReferencesCount =
      new NodeId(UShort.MIN, uint(2237L));

  public static final NodeId SessionDiagnosticsVariableType_BrowseCount =
      new NodeId(UShort.MIN, uint(2238L));

  public static final NodeId SessionDiagnosticsVariableType_BrowseNextCount =
      new NodeId(UShort.MIN, uint(2239L));

  public static final NodeId SessionDiagnosticsVariableType_TranslateBrowsePathsToNodeIdsCount =
      new NodeId(UShort.MIN, uint(2240L));

  public static final NodeId SessionDiagnosticsVariableType_QueryFirstCount =
      new NodeId(UShort.MIN, uint(2241L));

  public static final NodeId SessionDiagnosticsVariableType_QueryNextCount =
      new NodeId(UShort.MIN, uint(2242L));

  public static final NodeId SessionSecurityDiagnosticsArrayType =
      new NodeId(UShort.MIN, uint(2243L));

  public static final NodeId SessionSecurityDiagnosticsType = new NodeId(UShort.MIN, uint(2244L));

  public static final NodeId SessionSecurityDiagnosticsType_SessionId =
      new NodeId(UShort.MIN, uint(2245L));

  public static final NodeId SessionSecurityDiagnosticsType_ClientUserIdOfSession =
      new NodeId(UShort.MIN, uint(2246L));

  public static final NodeId SessionSecurityDiagnosticsType_ClientUserIdHistory =
      new NodeId(UShort.MIN, uint(2247L));

  public static final NodeId SessionSecurityDiagnosticsType_AuthenticationMechanism =
      new NodeId(UShort.MIN, uint(2248L));

  public static final NodeId SessionSecurityDiagnosticsType_Encoding =
      new NodeId(UShort.MIN, uint(2249L));

  public static final NodeId SessionSecurityDiagnosticsType_TransportProtocol =
      new NodeId(UShort.MIN, uint(2250L));

  public static final NodeId SessionSecurityDiagnosticsType_SecurityMode =
      new NodeId(UShort.MIN, uint(2251L));

  public static final NodeId SessionSecurityDiagnosticsType_SecurityPolicyUri =
      new NodeId(UShort.MIN, uint(2252L));

  public static final NodeId Server = new NodeId(UShort.MIN, uint(2253L));

  public static final NodeId Server_ServerArray = new NodeId(UShort.MIN, uint(2254L));

  public static final NodeId Server_NamespaceArray = new NodeId(UShort.MIN, uint(2255L));

  public static final NodeId Server_ServerStatus = new NodeId(UShort.MIN, uint(2256L));

  public static final NodeId Server_ServerStatus_StartTime = new NodeId(UShort.MIN, uint(2257L));

  public static final NodeId Server_ServerStatus_CurrentTime = new NodeId(UShort.MIN, uint(2258L));

  public static final NodeId Server_ServerStatus_State = new NodeId(UShort.MIN, uint(2259L));

  public static final NodeId Server_ServerStatus_BuildInfo = new NodeId(UShort.MIN, uint(2260L));

  public static final NodeId Server_ServerStatus_BuildInfo_ProductName =
      new NodeId(UShort.MIN, uint(2261L));

  public static final NodeId Server_ServerStatus_BuildInfo_ProductUri =
      new NodeId(UShort.MIN, uint(2262L));

  public static final NodeId Server_ServerStatus_BuildInfo_ManufacturerName =
      new NodeId(UShort.MIN, uint(2263L));

  public static final NodeId Server_ServerStatus_BuildInfo_SoftwareVersion =
      new NodeId(UShort.MIN, uint(2264L));

  public static final NodeId Server_ServerStatus_BuildInfo_BuildNumber =
      new NodeId(UShort.MIN, uint(2265L));

  public static final NodeId Server_ServerStatus_BuildInfo_BuildDate =
      new NodeId(UShort.MIN, uint(2266L));

  public static final NodeId Server_ServiceLevel = new NodeId(UShort.MIN, uint(2267L));

  public static final NodeId Server_ServerCapabilities = new NodeId(UShort.MIN, uint(2268L));

  public static final NodeId Server_ServerCapabilities_ServerProfileArray =
      new NodeId(UShort.MIN, uint(2269L));

  public static final NodeId Server_ServerCapabilities_LocaleIdArray =
      new NodeId(UShort.MIN, uint(2271L));

  public static final NodeId Server_ServerCapabilities_MinSupportedSampleRate =
      new NodeId(UShort.MIN, uint(2272L));

  public static final NodeId Server_ServerDiagnostics = new NodeId(UShort.MIN, uint(2274L));

  public static final NodeId Server_ServerDiagnostics_ServerDiagnosticsSummary =
      new NodeId(UShort.MIN, uint(2275L));

  public static final NodeId Server_ServerDiagnostics_ServerDiagnosticsSummary_ServerViewCount =
      new NodeId(UShort.MIN, uint(2276L));

  public static final NodeId Server_ServerDiagnostics_ServerDiagnosticsSummary_CurrentSessionCount =
      new NodeId(UShort.MIN, uint(2277L));

  public static final NodeId
      Server_ServerDiagnostics_ServerDiagnosticsSummary_CumulatedSessionCount =
          new NodeId(UShort.MIN, uint(2278L));

  public static final NodeId
      Server_ServerDiagnostics_ServerDiagnosticsSummary_SecurityRejectedSessionCount =
          new NodeId(UShort.MIN, uint(2279L));

  public static final NodeId Server_ServerDiagnostics_ServerDiagnosticsSummary_SessionTimeoutCount =
      new NodeId(UShort.MIN, uint(2281L));

  public static final NodeId Server_ServerDiagnostics_ServerDiagnosticsSummary_SessionAbortCount =
      new NodeId(UShort.MIN, uint(2282L));

  public static final NodeId
      Server_ServerDiagnostics_ServerDiagnosticsSummary_PublishingIntervalCount =
          new NodeId(UShort.MIN, uint(2284L));

  public static final NodeId
      Server_ServerDiagnostics_ServerDiagnosticsSummary_CurrentSubscriptionCount =
          new NodeId(UShort.MIN, uint(2285L));

  public static final NodeId
      Server_ServerDiagnostics_ServerDiagnosticsSummary_CumulatedSubscriptionCount =
          new NodeId(UShort.MIN, uint(2286L));

  public static final NodeId
      Server_ServerDiagnostics_ServerDiagnosticsSummary_SecurityRejectedRequestsCount =
          new NodeId(UShort.MIN, uint(2287L));

  public static final NodeId
      Server_ServerDiagnostics_ServerDiagnosticsSummary_RejectedRequestsCount =
          new NodeId(UShort.MIN, uint(2288L));

  public static final NodeId Server_ServerDiagnostics_SamplingIntervalDiagnosticsArray =
      new NodeId(UShort.MIN, uint(2289L));

  public static final NodeId Server_ServerDiagnostics_SubscriptionDiagnosticsArray =
      new NodeId(UShort.MIN, uint(2290L));

  public static final NodeId Server_ServerDiagnostics_EnabledFlag =
      new NodeId(UShort.MIN, uint(2294L));

  public static final NodeId Server_VendorServerInfo = new NodeId(UShort.MIN, uint(2295L));

  public static final NodeId Server_ServerRedundancy = new NodeId(UShort.MIN, uint(2296L));

  public static final NodeId StateMachineType = new NodeId(UShort.MIN, uint(2299L));

  public static final NodeId StateType = new NodeId(UShort.MIN, uint(2307L));

  public static final NodeId StateType_StateNumber = new NodeId(UShort.MIN, uint(2308L));

  public static final NodeId InitialStateType = new NodeId(UShort.MIN, uint(2309L));

  public static final NodeId TransitionType = new NodeId(UShort.MIN, uint(2310L));

  public static final NodeId TransitionEventType = new NodeId(UShort.MIN, uint(2311L));

  public static final NodeId TransitionType_TransitionNumber = new NodeId(UShort.MIN, uint(2312L));

  public static final NodeId AuditUpdateStateEventType = new NodeId(UShort.MIN, uint(2315L));

  public static final NodeId HistoricalDataConfigurationType = new NodeId(UShort.MIN, uint(2318L));

  public static final NodeId HistoricalDataConfigurationType_Stepped =
      new NodeId(UShort.MIN, uint(2323L));

  public static final NodeId HistoricalDataConfigurationType_Definition =
      new NodeId(UShort.MIN, uint(2324L));

  public static final NodeId HistoricalDataConfigurationType_MaxTimeInterval =
      new NodeId(UShort.MIN, uint(2325L));

  public static final NodeId HistoricalDataConfigurationType_MinTimeInterval =
      new NodeId(UShort.MIN, uint(2326L));

  public static final NodeId HistoricalDataConfigurationType_ExceptionDeviation =
      new NodeId(UShort.MIN, uint(2327L));

  public static final NodeId HistoricalDataConfigurationType_ExceptionDeviationFormat =
      new NodeId(UShort.MIN, uint(2328L));

  public static final NodeId HistoryServerCapabilitiesType = new NodeId(UShort.MIN, uint(2330L));

  public static final NodeId HistoryServerCapabilitiesType_AccessHistoryDataCapability =
      new NodeId(UShort.MIN, uint(2331L));

  public static final NodeId HistoryServerCapabilitiesType_AccessHistoryEventsCapability =
      new NodeId(UShort.MIN, uint(2332L));

  public static final NodeId HistoryServerCapabilitiesType_InsertDataCapability =
      new NodeId(UShort.MIN, uint(2334L));

  public static final NodeId HistoryServerCapabilitiesType_ReplaceDataCapability =
      new NodeId(UShort.MIN, uint(2335L));

  public static final NodeId HistoryServerCapabilitiesType_UpdateDataCapability =
      new NodeId(UShort.MIN, uint(2336L));

  public static final NodeId HistoryServerCapabilitiesType_DeleteRawCapability =
      new NodeId(UShort.MIN, uint(2337L));

  public static final NodeId HistoryServerCapabilitiesType_DeleteAtTimeCapability =
      new NodeId(UShort.MIN, uint(2338L));

  public static final NodeId AggregateFunctionType = new NodeId(UShort.MIN, uint(2340L));

  public static final NodeId AggregateFunction_Interpolative = new NodeId(UShort.MIN, uint(2341L));

  public static final NodeId AggregateFunction_Average = new NodeId(UShort.MIN, uint(2342L));

  public static final NodeId AggregateFunction_TimeAverage = new NodeId(UShort.MIN, uint(2343L));

  public static final NodeId AggregateFunction_Total = new NodeId(UShort.MIN, uint(2344L));

  public static final NodeId AggregateFunction_Minimum = new NodeId(UShort.MIN, uint(2346L));

  public static final NodeId AggregateFunction_Maximum = new NodeId(UShort.MIN, uint(2347L));

  public static final NodeId AggregateFunction_MinimumActualTime =
      new NodeId(UShort.MIN, uint(2348L));

  public static final NodeId AggregateFunction_MaximumActualTime =
      new NodeId(UShort.MIN, uint(2349L));

  public static final NodeId AggregateFunction_Range = new NodeId(UShort.MIN, uint(2350L));

  public static final NodeId AggregateFunction_AnnotationCount =
      new NodeId(UShort.MIN, uint(2351L));

  public static final NodeId AggregateFunction_Count = new NodeId(UShort.MIN, uint(2352L));

  public static final NodeId AggregateFunction_NumberOfTransitions =
      new NodeId(UShort.MIN, uint(2355L));

  public static final NodeId AggregateFunction_Start = new NodeId(UShort.MIN, uint(2357L));

  public static final NodeId AggregateFunction_End = new NodeId(UShort.MIN, uint(2358L));

  public static final NodeId AggregateFunction_Delta = new NodeId(UShort.MIN, uint(2359L));

  public static final NodeId AggregateFunction_DurationGood = new NodeId(UShort.MIN, uint(2360L));

  public static final NodeId AggregateFunction_DurationBad = new NodeId(UShort.MIN, uint(2361L));

  public static final NodeId AggregateFunction_PercentGood = new NodeId(UShort.MIN, uint(2362L));

  public static final NodeId AggregateFunction_PercentBad = new NodeId(UShort.MIN, uint(2363L));

  public static final NodeId AggregateFunction_WorstQuality = new NodeId(UShort.MIN, uint(2364L));

  public static final NodeId DataItemType = new NodeId(UShort.MIN, uint(2365L));

  public static final NodeId DataItemType_Definition = new NodeId(UShort.MIN, uint(2366L));

  public static final NodeId DataItemType_ValuePrecision = new NodeId(UShort.MIN, uint(2367L));

  public static final NodeId AnalogItemType = new NodeId(UShort.MIN, uint(2368L));

  public static final NodeId AnalogItemType_EURange = new NodeId(UShort.MIN, uint(2369L));

  public static final NodeId DiscreteItemType = new NodeId(UShort.MIN, uint(2372L));

  public static final NodeId TwoStateDiscreteType = new NodeId(UShort.MIN, uint(2373L));

  public static final NodeId TwoStateDiscreteType_FalseState = new NodeId(UShort.MIN, uint(2374L));

  public static final NodeId TwoStateDiscreteType_TrueState = new NodeId(UShort.MIN, uint(2375L));

  public static final NodeId MultiStateDiscreteType = new NodeId(UShort.MIN, uint(2376L));

  public static final NodeId MultiStateDiscreteType_EnumStrings =
      new NodeId(UShort.MIN, uint(2377L));

  public static final NodeId ProgramTransitionEventType = new NodeId(UShort.MIN, uint(2378L));

  public static final NodeId ProgramTransitionEventType_IntermediateResult =
      new NodeId(UShort.MIN, uint(2379L));

  public static final NodeId ProgramDiagnosticType = new NodeId(UShort.MIN, uint(2380L));

  public static final NodeId ProgramDiagnosticType_CreateSessionId =
      new NodeId(UShort.MIN, uint(2381L));

  public static final NodeId ProgramDiagnosticType_CreateClientName =
      new NodeId(UShort.MIN, uint(2382L));

  public static final NodeId ProgramDiagnosticType_InvocationCreationTime =
      new NodeId(UShort.MIN, uint(2383L));

  public static final NodeId ProgramDiagnosticType_LastTransitionTime =
      new NodeId(UShort.MIN, uint(2384L));

  public static final NodeId ProgramDiagnosticType_LastMethodCall =
      new NodeId(UShort.MIN, uint(2385L));

  public static final NodeId ProgramDiagnosticType_LastMethodSessionId =
      new NodeId(UShort.MIN, uint(2386L));

  public static final NodeId ProgramDiagnosticType_LastMethodInputArguments =
      new NodeId(UShort.MIN, uint(2387L));

  public static final NodeId ProgramDiagnosticType_LastMethodOutputArguments =
      new NodeId(UShort.MIN, uint(2388L));

  public static final NodeId ProgramDiagnosticType_LastMethodCallTime =
      new NodeId(UShort.MIN, uint(2389L));

  public static final NodeId ProgramDiagnosticType_LastMethodReturnStatus =
      new NodeId(UShort.MIN, uint(2390L));

  public static final NodeId ProgramStateMachineType = new NodeId(UShort.MIN, uint(2391L));

  public static final NodeId ProgramStateMachineType_Creatable =
      new NodeId(UShort.MIN, uint(2392L));

  public static final NodeId ProgramStateMachineType_Deletable =
      new NodeId(UShort.MIN, uint(2393L));

  public static final NodeId ProgramStateMachineType_AutoDelete =
      new NodeId(UShort.MIN, uint(2394L));

  public static final NodeId ProgramStateMachineType_RecycleCount =
      new NodeId(UShort.MIN, uint(2395L));

  public static final NodeId ProgramStateMachineType_InstanceCount =
      new NodeId(UShort.MIN, uint(2396L));

  public static final NodeId ProgramStateMachineType_MaxInstanceCount =
      new NodeId(UShort.MIN, uint(2397L));

  public static final NodeId ProgramStateMachineType_MaxRecycleCount =
      new NodeId(UShort.MIN, uint(2398L));

  public static final NodeId ProgramStateMachineType_ProgramDiagnostic =
      new NodeId(UShort.MIN, uint(2399L));

  public static final NodeId ProgramStateMachineType_Ready = new NodeId(UShort.MIN, uint(2400L));

  public static final NodeId ProgramStateMachineType_Ready_StateNumber =
      new NodeId(UShort.MIN, uint(2401L));

  public static final NodeId ProgramStateMachineType_Running = new NodeId(UShort.MIN, uint(2402L));

  public static final NodeId ProgramStateMachineType_Running_StateNumber =
      new NodeId(UShort.MIN, uint(2403L));

  public static final NodeId ProgramStateMachineType_Suspended =
      new NodeId(UShort.MIN, uint(2404L));

  public static final NodeId ProgramStateMachineType_Suspended_StateNumber =
      new NodeId(UShort.MIN, uint(2405L));

  public static final NodeId ProgramStateMachineType_Halted = new NodeId(UShort.MIN, uint(2406L));

  public static final NodeId ProgramStateMachineType_Halted_StateNumber =
      new NodeId(UShort.MIN, uint(2407L));

  public static final NodeId ProgramStateMachineType_HaltedToReady =
      new NodeId(UShort.MIN, uint(2408L));

  public static final NodeId ProgramStateMachineType_HaltedToReady_TransitionNumber =
      new NodeId(UShort.MIN, uint(2409L));

  public static final NodeId ProgramStateMachineType_ReadyToRunning =
      new NodeId(UShort.MIN, uint(2410L));

  public static final NodeId ProgramStateMachineType_ReadyToRunning_TransitionNumber =
      new NodeId(UShort.MIN, uint(2411L));

  public static final NodeId ProgramStateMachineType_RunningToHalted =
      new NodeId(UShort.MIN, uint(2412L));

  public static final NodeId ProgramStateMachineType_RunningToHalted_TransitionNumber =
      new NodeId(UShort.MIN, uint(2413L));

  public static final NodeId ProgramStateMachineType_RunningToReady =
      new NodeId(UShort.MIN, uint(2414L));

  public static final NodeId ProgramStateMachineType_RunningToReady_TransitionNumber =
      new NodeId(UShort.MIN, uint(2415L));

  public static final NodeId ProgramStateMachineType_RunningToSuspended =
      new NodeId(UShort.MIN, uint(2416L));

  public static final NodeId ProgramStateMachineType_RunningToSuspended_TransitionNumber =
      new NodeId(UShort.MIN, uint(2417L));

  public static final NodeId ProgramStateMachineType_SuspendedToRunning =
      new NodeId(UShort.MIN, uint(2418L));

  public static final NodeId ProgramStateMachineType_SuspendedToRunning_TransitionNumber =
      new NodeId(UShort.MIN, uint(2419L));

  public static final NodeId ProgramStateMachineType_SuspendedToHalted =
      new NodeId(UShort.MIN, uint(2420L));

  public static final NodeId ProgramStateMachineType_SuspendedToHalted_TransitionNumber =
      new NodeId(UShort.MIN, uint(2421L));

  public static final NodeId ProgramStateMachineType_SuspendedToReady =
      new NodeId(UShort.MIN, uint(2422L));

  public static final NodeId ProgramStateMachineType_SuspendedToReady_TransitionNumber =
      new NodeId(UShort.MIN, uint(2423L));

  public static final NodeId ProgramStateMachineType_ReadyToHalted =
      new NodeId(UShort.MIN, uint(2424L));

  public static final NodeId ProgramStateMachineType_ReadyToHalted_TransitionNumber =
      new NodeId(UShort.MIN, uint(2425L));

  public static final NodeId ProgramStateMachineType_Start = new NodeId(UShort.MIN, uint(2426L));

  public static final NodeId ProgramStateMachineType_Suspend = new NodeId(UShort.MIN, uint(2427L));

  public static final NodeId ProgramStateMachineType_Resume = new NodeId(UShort.MIN, uint(2428L));

  public static final NodeId ProgramStateMachineType_Halt = new NodeId(UShort.MIN, uint(2429L));

  public static final NodeId ProgramStateMachineType_Reset = new NodeId(UShort.MIN, uint(2430L));

  public static final NodeId SessionDiagnosticsVariableType_RegisterNodesCount =
      new NodeId(UShort.MIN, uint(2730L));

  public static final NodeId SessionDiagnosticsVariableType_UnregisterNodesCount =
      new NodeId(UShort.MIN, uint(2731L));

  public static final NodeId ServerCapabilitiesType_MaxBrowseContinuationPoints =
      new NodeId(UShort.MIN, uint(2732L));

  public static final NodeId ServerCapabilitiesType_MaxQueryContinuationPoints =
      new NodeId(UShort.MIN, uint(2733L));

  public static final NodeId ServerCapabilitiesType_MaxHistoryContinuationPoints =
      new NodeId(UShort.MIN, uint(2734L));

  public static final NodeId Server_ServerCapabilities_MaxBrowseContinuationPoints =
      new NodeId(UShort.MIN, uint(2735L));

  public static final NodeId Server_ServerCapabilities_MaxQueryContinuationPoints =
      new NodeId(UShort.MIN, uint(2736L));

  public static final NodeId Server_ServerCapabilities_MaxHistoryContinuationPoints =
      new NodeId(UShort.MIN, uint(2737L));

  public static final NodeId SemanticChangeEventType = new NodeId(UShort.MIN, uint(2738L));

  public static final NodeId SemanticChangeEventType_Changes = new NodeId(UShort.MIN, uint(2739L));

  public static final NodeId ServerType_Auditing = new NodeId(UShort.MIN, uint(2742L));

  public static final NodeId ServerDiagnosticsType_SessionsDiagnosticsSummary =
      new NodeId(UShort.MIN, uint(2744L));

  public static final NodeId AuditChannelEventType_SecureChannelId =
      new NodeId(UShort.MIN, uint(2745L));

  public static final NodeId AuditOpenSecureChannelEventType_ClientCertificateThumbprint =
      new NodeId(UShort.MIN, uint(2746L));

  public static final NodeId AuditCreateSessionEventType_ClientCertificateThumbprint =
      new NodeId(UShort.MIN, uint(2747L));

  public static final NodeId AuditUrlMismatchEventType = new NodeId(UShort.MIN, uint(2748L));

  public static final NodeId AuditUrlMismatchEventType_EndpointUrl =
      new NodeId(UShort.MIN, uint(2749L));

  public static final NodeId AuditWriteUpdateEventType_AttributeId =
      new NodeId(UShort.MIN, uint(2750L));

  public static final NodeId AuditHistoryUpdateEventType_ParameterDataTypeId =
      new NodeId(UShort.MIN, uint(2751L));

  public static final NodeId ServerStatusType_SecondsTillShutdown =
      new NodeId(UShort.MIN, uint(2752L));

  public static final NodeId ServerStatusType_ShutdownReason = new NodeId(UShort.MIN, uint(2753L));

  public static final NodeId ServerCapabilitiesType_AggregateFunctions =
      new NodeId(UShort.MIN, uint(2754L));

  public static final NodeId StateVariableType = new NodeId(UShort.MIN, uint(2755L));

  public static final NodeId StateVariableType_Id = new NodeId(UShort.MIN, uint(2756L));

  public static final NodeId StateVariableType_Name = new NodeId(UShort.MIN, uint(2757L));

  public static final NodeId StateVariableType_Number = new NodeId(UShort.MIN, uint(2758L));

  public static final NodeId StateVariableType_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(2759L));

  public static final NodeId FiniteStateVariableType = new NodeId(UShort.MIN, uint(2760L));

  public static final NodeId FiniteStateVariableType_Id = new NodeId(UShort.MIN, uint(2761L));

  public static final NodeId TransitionVariableType = new NodeId(UShort.MIN, uint(2762L));

  public static final NodeId TransitionVariableType_Id = new NodeId(UShort.MIN, uint(2763L));

  public static final NodeId TransitionVariableType_Name = new NodeId(UShort.MIN, uint(2764L));

  public static final NodeId TransitionVariableType_Number = new NodeId(UShort.MIN, uint(2765L));

  public static final NodeId TransitionVariableType_TransitionTime =
      new NodeId(UShort.MIN, uint(2766L));

  public static final NodeId FiniteTransitionVariableType = new NodeId(UShort.MIN, uint(2767L));

  public static final NodeId FiniteTransitionVariableType_Id = new NodeId(UShort.MIN, uint(2768L));

  public static final NodeId StateMachineType_CurrentState = new NodeId(UShort.MIN, uint(2769L));

  public static final NodeId StateMachineType_LastTransition = new NodeId(UShort.MIN, uint(2770L));

  public static final NodeId FiniteStateMachineType = new NodeId(UShort.MIN, uint(2771L));

  public static final NodeId FiniteStateMachineType_CurrentState =
      new NodeId(UShort.MIN, uint(2772L));

  public static final NodeId FiniteStateMachineType_LastTransition =
      new NodeId(UShort.MIN, uint(2773L));

  public static final NodeId TransitionEventType_Transition = new NodeId(UShort.MIN, uint(2774L));

  public static final NodeId TransitionEventType_FromState = new NodeId(UShort.MIN, uint(2775L));

  public static final NodeId TransitionEventType_ToState = new NodeId(UShort.MIN, uint(2776L));

  public static final NodeId AuditUpdateStateEventType_OldStateId =
      new NodeId(UShort.MIN, uint(2777L));

  public static final NodeId AuditUpdateStateEventType_NewStateId =
      new NodeId(UShort.MIN, uint(2778L));

  public static final NodeId ConditionType = new NodeId(UShort.MIN, uint(2782L));

  public static final NodeId RefreshStartEventType = new NodeId(UShort.MIN, uint(2787L));

  public static final NodeId RefreshEndEventType = new NodeId(UShort.MIN, uint(2788L));

  public static final NodeId RefreshRequiredEventType = new NodeId(UShort.MIN, uint(2789L));

  public static final NodeId AuditConditionEventType = new NodeId(UShort.MIN, uint(2790L));

  public static final NodeId AuditConditionEnableEventType = new NodeId(UShort.MIN, uint(2803L));

  public static final NodeId AuditConditionCommentEventType = new NodeId(UShort.MIN, uint(2829L));

  public static final NodeId DialogConditionType = new NodeId(UShort.MIN, uint(2830L));

  public static final NodeId DialogConditionType_Prompt = new NodeId(UShort.MIN, uint(2831L));

  public static final NodeId AcknowledgeableConditionType = new NodeId(UShort.MIN, uint(2881L));

  public static final NodeId AlarmConditionType = new NodeId(UShort.MIN, uint(2915L));

  public static final NodeId ShelvedStateMachineType = new NodeId(UShort.MIN, uint(2929L));

  public static final NodeId ShelvedStateMachineType_Unshelved =
      new NodeId(UShort.MIN, uint(2930L));

  public static final NodeId ShelvedStateMachineType_TimedShelved =
      new NodeId(UShort.MIN, uint(2932L));

  public static final NodeId ShelvedStateMachineType_OneShotShelved =
      new NodeId(UShort.MIN, uint(2933L));

  public static final NodeId ShelvedStateMachineType_UnshelvedToTimedShelved =
      new NodeId(UShort.MIN, uint(2935L));

  public static final NodeId ShelvedStateMachineType_UnshelvedToOneShotShelved =
      new NodeId(UShort.MIN, uint(2936L));

  public static final NodeId ShelvedStateMachineType_TimedShelvedToUnshelved =
      new NodeId(UShort.MIN, uint(2940L));

  public static final NodeId ShelvedStateMachineType_TimedShelvedToOneShotShelved =
      new NodeId(UShort.MIN, uint(2942L));

  public static final NodeId ShelvedStateMachineType_OneShotShelvedToUnshelved =
      new NodeId(UShort.MIN, uint(2943L));

  public static final NodeId ShelvedStateMachineType_OneShotShelvedToTimedShelved =
      new NodeId(UShort.MIN, uint(2945L));

  public static final NodeId ShelvedStateMachineType_Unshelve = new NodeId(UShort.MIN, uint(2947L));

  public static final NodeId ShelvedStateMachineType_OneShotShelve =
      new NodeId(UShort.MIN, uint(2948L));

  public static final NodeId ShelvedStateMachineType_TimedShelve =
      new NodeId(UShort.MIN, uint(2949L));

  public static final NodeId LimitAlarmType = new NodeId(UShort.MIN, uint(2955L));

  public static final NodeId ShelvedStateMachineType_TimedShelve_InputArguments =
      new NodeId(UShort.MIN, uint(2991L));

  public static final NodeId Server_ServerStatus_SecondsTillShutdown =
      new NodeId(UShort.MIN, uint(2992L));

  public static final NodeId Server_ServerStatus_ShutdownReason =
      new NodeId(UShort.MIN, uint(2993L));

  public static final NodeId Server_Auditing = new NodeId(UShort.MIN, uint(2994L));

  public static final NodeId Server_ServerCapabilities_ModellingRules =
      new NodeId(UShort.MIN, uint(2996L));

  public static final NodeId Server_ServerCapabilities_AggregateFunctions =
      new NodeId(UShort.MIN, uint(2997L));

  public static final NodeId SubscriptionDiagnosticsType_EventNotificationsCount =
      new NodeId(UShort.MIN, uint(2998L));

  public static final NodeId AuditHistoryEventUpdateEventType = new NodeId(UShort.MIN, uint(2999L));

  public static final NodeId AuditHistoryEventUpdateEventType_Filter =
      new NodeId(UShort.MIN, uint(3003L));

  public static final NodeId AuditHistoryValueUpdateEventType = new NodeId(UShort.MIN, uint(3006L));

  public static final NodeId AuditHistoryDeleteEventType = new NodeId(UShort.MIN, uint(3012L));

  public static final NodeId AuditHistoryRawModifyDeleteEventType =
      new NodeId(UShort.MIN, uint(3014L));

  public static final NodeId AuditHistoryRawModifyDeleteEventType_IsDeleteModified =
      new NodeId(UShort.MIN, uint(3015L));

  public static final NodeId AuditHistoryRawModifyDeleteEventType_StartTime =
      new NodeId(UShort.MIN, uint(3016L));

  public static final NodeId AuditHistoryRawModifyDeleteEventType_EndTime =
      new NodeId(UShort.MIN, uint(3017L));

  public static final NodeId AuditHistoryAtTimeDeleteEventType =
      new NodeId(UShort.MIN, uint(3019L));

  public static final NodeId AuditHistoryAtTimeDeleteEventType_ReqTimes =
      new NodeId(UShort.MIN, uint(3020L));

  public static final NodeId AuditHistoryAtTimeDeleteEventType_OldValues =
      new NodeId(UShort.MIN, uint(3021L));

  public static final NodeId AuditHistoryEventDeleteEventType = new NodeId(UShort.MIN, uint(3022L));

  public static final NodeId AuditHistoryEventDeleteEventType_EventIds =
      new NodeId(UShort.MIN, uint(3023L));

  public static final NodeId AuditHistoryEventDeleteEventType_OldValues =
      new NodeId(UShort.MIN, uint(3024L));

  public static final NodeId AuditHistoryEventUpdateEventType_UpdatedNode =
      new NodeId(UShort.MIN, uint(3025L));

  public static final NodeId AuditHistoryValueUpdateEventType_UpdatedNode =
      new NodeId(UShort.MIN, uint(3026L));

  public static final NodeId AuditHistoryDeleteEventType_UpdatedNode =
      new NodeId(UShort.MIN, uint(3027L));

  public static final NodeId AuditHistoryEventUpdateEventType_PerformInsertReplace =
      new NodeId(UShort.MIN, uint(3028L));

  public static final NodeId AuditHistoryEventUpdateEventType_NewValues =
      new NodeId(UShort.MIN, uint(3029L));

  public static final NodeId AuditHistoryEventUpdateEventType_OldValues =
      new NodeId(UShort.MIN, uint(3030L));

  public static final NodeId AuditHistoryValueUpdateEventType_PerformInsertReplace =
      new NodeId(UShort.MIN, uint(3031L));

  public static final NodeId AuditHistoryValueUpdateEventType_NewValues =
      new NodeId(UShort.MIN, uint(3032L));

  public static final NodeId AuditHistoryValueUpdateEventType_OldValues =
      new NodeId(UShort.MIN, uint(3033L));

  public static final NodeId AuditHistoryRawModifyDeleteEventType_OldValues =
      new NodeId(UShort.MIN, uint(3034L));

  public static final NodeId EventQueueOverflowEventType = new NodeId(UShort.MIN, uint(3035L));

  public static final NodeId EventTypesFolder = new NodeId(UShort.MIN, uint(3048L));

  public static final NodeId ServerCapabilitiesType_SoftwareCertificates =
      new NodeId(UShort.MIN, uint(3049L));

  public static final NodeId SessionDiagnosticsVariableType_MaxResponseMessageSize =
      new NodeId(UShort.MIN, uint(3050L));

  public static final NodeId BuildInfoType = new NodeId(UShort.MIN, uint(3051L));

  public static final NodeId BuildInfoType_ProductUri = new NodeId(UShort.MIN, uint(3052L));

  public static final NodeId BuildInfoType_ManufacturerName = new NodeId(UShort.MIN, uint(3053L));

  public static final NodeId BuildInfoType_ProductName = new NodeId(UShort.MIN, uint(3054L));

  public static final NodeId BuildInfoType_SoftwareVersion = new NodeId(UShort.MIN, uint(3055L));

  public static final NodeId BuildInfoType_BuildNumber = new NodeId(UShort.MIN, uint(3056L));

  public static final NodeId BuildInfoType_BuildDate = new NodeId(UShort.MIN, uint(3057L));

  public static final NodeId SessionSecurityDiagnosticsType_ClientCertificate =
      new NodeId(UShort.MIN, uint(3058L));

  public static final NodeId HistoricalDataConfigurationType_AggregateConfiguration =
      new NodeId(UShort.MIN, uint(3059L));

  public static final NodeId DefaultBinary = new NodeId(UShort.MIN, uint(3062L));

  public static final NodeId DefaultXml = new NodeId(UShort.MIN, uint(3063L));

  public static final NodeId AlwaysGeneratesEvent = new NodeId(UShort.MIN, uint(3065L));

  public static final NodeId Icon = new NodeId(UShort.MIN, uint(3067L));

  public static final NodeId NodeVersion = new NodeId(UShort.MIN, uint(3068L));

  public static final NodeId LocalTime = new NodeId(UShort.MIN, uint(3069L));

  public static final NodeId AllowNulls = new NodeId(UShort.MIN, uint(3070L));

  public static final NodeId EnumValues = new NodeId(UShort.MIN, uint(3071L));

  public static final NodeId InputArguments = new NodeId(UShort.MIN, uint(3072L));

  public static final NodeId OutputArguments = new NodeId(UShort.MIN, uint(3073L));

  public static final NodeId ServerType_ServerStatus_StartTime =
      new NodeId(UShort.MIN, uint(3074L));

  public static final NodeId ServerType_ServerStatus_CurrentTime =
      new NodeId(UShort.MIN, uint(3075L));

  public static final NodeId ServerType_ServerStatus_State = new NodeId(UShort.MIN, uint(3076L));

  public static final NodeId ServerType_ServerStatus_BuildInfo =
      new NodeId(UShort.MIN, uint(3077L));

  public static final NodeId ServerType_ServerStatus_BuildInfo_ProductUri =
      new NodeId(UShort.MIN, uint(3078L));

  public static final NodeId ServerType_ServerStatus_BuildInfo_ManufacturerName =
      new NodeId(UShort.MIN, uint(3079L));

  public static final NodeId ServerType_ServerStatus_BuildInfo_ProductName =
      new NodeId(UShort.MIN, uint(3080L));

  public static final NodeId ServerType_ServerStatus_BuildInfo_SoftwareVersion =
      new NodeId(UShort.MIN, uint(3081L));

  public static final NodeId ServerType_ServerStatus_BuildInfo_BuildNumber =
      new NodeId(UShort.MIN, uint(3082L));

  public static final NodeId ServerType_ServerStatus_BuildInfo_BuildDate =
      new NodeId(UShort.MIN, uint(3083L));

  public static final NodeId ServerType_ServerStatus_SecondsTillShutdown =
      new NodeId(UShort.MIN, uint(3084L));

  public static final NodeId ServerType_ServerStatus_ShutdownReason =
      new NodeId(UShort.MIN, uint(3085L));

  public static final NodeId ServerType_ServerCapabilities_ServerProfileArray =
      new NodeId(UShort.MIN, uint(3086L));

  public static final NodeId ServerType_ServerCapabilities_LocaleIdArray =
      new NodeId(UShort.MIN, uint(3087L));

  public static final NodeId ServerType_ServerCapabilities_MinSupportedSampleRate =
      new NodeId(UShort.MIN, uint(3088L));

  public static final NodeId ServerType_ServerCapabilities_MaxBrowseContinuationPoints =
      new NodeId(UShort.MIN, uint(3089L));

  public static final NodeId ServerType_ServerCapabilities_MaxQueryContinuationPoints =
      new NodeId(UShort.MIN, uint(3090L));

  public static final NodeId ServerType_ServerCapabilities_MaxHistoryContinuationPoints =
      new NodeId(UShort.MIN, uint(3091L));

  public static final NodeId ServerType_ServerCapabilities_SoftwareCertificates =
      new NodeId(UShort.MIN, uint(3092L));

  public static final NodeId ServerType_ServerCapabilities_ModellingRules =
      new NodeId(UShort.MIN, uint(3093L));

  public static final NodeId ServerType_ServerCapabilities_AggregateFunctions =
      new NodeId(UShort.MIN, uint(3094L));

  public static final NodeId ServerType_ServerDiagnostics_ServerDiagnosticsSummary =
      new NodeId(UShort.MIN, uint(3095L));

  public static final NodeId ServerType_ServerDiagnostics_ServerDiagnosticsSummary_ServerViewCount =
      new NodeId(UShort.MIN, uint(3096L));

  public static final NodeId
      ServerType_ServerDiagnostics_ServerDiagnosticsSummary_CurrentSessionCount =
          new NodeId(UShort.MIN, uint(3097L));

  public static final NodeId
      ServerType_ServerDiagnostics_ServerDiagnosticsSummary_CumulatedSessionCount =
          new NodeId(UShort.MIN, uint(3098L));

  public static final NodeId
      ServerType_ServerDiagnostics_ServerDiagnosticsSummary_SecurityRejectedSessionCount =
          new NodeId(UShort.MIN, uint(3099L));

  public static final NodeId
      ServerType_ServerDiagnostics_ServerDiagnosticsSummary_RejectedSessionCount =
          new NodeId(UShort.MIN, uint(3100L));

  public static final NodeId
      ServerType_ServerDiagnostics_ServerDiagnosticsSummary_SessionTimeoutCount =
          new NodeId(UShort.MIN, uint(3101L));

  public static final NodeId
      ServerType_ServerDiagnostics_ServerDiagnosticsSummary_SessionAbortCount =
          new NodeId(UShort.MIN, uint(3102L));

  public static final NodeId
      ServerType_ServerDiagnostics_ServerDiagnosticsSummary_PublishingIntervalCount =
          new NodeId(UShort.MIN, uint(3104L));

  public static final NodeId
      ServerType_ServerDiagnostics_ServerDiagnosticsSummary_CurrentSubscriptionCount =
          new NodeId(UShort.MIN, uint(3105L));

  public static final NodeId
      ServerType_ServerDiagnostics_ServerDiagnosticsSummary_CumulatedSubscriptionCount =
          new NodeId(UShort.MIN, uint(3106L));

  public static final NodeId
      ServerType_ServerDiagnostics_ServerDiagnosticsSummary_SecurityRejectedRequestsCount =
          new NodeId(UShort.MIN, uint(3107L));

  public static final NodeId
      ServerType_ServerDiagnostics_ServerDiagnosticsSummary_RejectedRequestsCount =
          new NodeId(UShort.MIN, uint(3108L));

  public static final NodeId ServerType_ServerDiagnostics_SamplingIntervalDiagnosticsArray =
      new NodeId(UShort.MIN, uint(3109L));

  public static final NodeId ServerType_ServerDiagnostics_SubscriptionDiagnosticsArray =
      new NodeId(UShort.MIN, uint(3110L));

  public static final NodeId ServerType_ServerDiagnostics_SessionsDiagnosticsSummary =
      new NodeId(UShort.MIN, uint(3111L));

  public static final NodeId
      ServerType_ServerDiagnostics_SessionsDiagnosticsSummary_SessionDiagnosticsArray =
          new NodeId(UShort.MIN, uint(3112L));

  public static final NodeId
      ServerType_ServerDiagnostics_SessionsDiagnosticsSummary_SessionSecurityDiagnosticsArray =
          new NodeId(UShort.MIN, uint(3113L));

  public static final NodeId ServerType_ServerDiagnostics_EnabledFlag =
      new NodeId(UShort.MIN, uint(3114L));

  public static final NodeId ServerType_ServerRedundancy_RedundancySupport =
      new NodeId(UShort.MIN, uint(3115L));

  public static final NodeId ServerDiagnosticsType_ServerDiagnosticsSummary_ServerViewCount =
      new NodeId(UShort.MIN, uint(3116L));

  public static final NodeId ServerDiagnosticsType_ServerDiagnosticsSummary_CurrentSessionCount =
      new NodeId(UShort.MIN, uint(3117L));

  public static final NodeId ServerDiagnosticsType_ServerDiagnosticsSummary_CumulatedSessionCount =
      new NodeId(UShort.MIN, uint(3118L));

  public static final NodeId
      ServerDiagnosticsType_ServerDiagnosticsSummary_SecurityRejectedSessionCount =
          new NodeId(UShort.MIN, uint(3119L));

  public static final NodeId ServerDiagnosticsType_ServerDiagnosticsSummary_RejectedSessionCount =
      new NodeId(UShort.MIN, uint(3120L));

  public static final NodeId ServerDiagnosticsType_ServerDiagnosticsSummary_SessionTimeoutCount =
      new NodeId(UShort.MIN, uint(3121L));

  public static final NodeId ServerDiagnosticsType_ServerDiagnosticsSummary_SessionAbortCount =
      new NodeId(UShort.MIN, uint(3122L));

  public static final NodeId
      ServerDiagnosticsType_ServerDiagnosticsSummary_PublishingIntervalCount =
          new NodeId(UShort.MIN, uint(3124L));

  public static final NodeId
      ServerDiagnosticsType_ServerDiagnosticsSummary_CurrentSubscriptionCount =
          new NodeId(UShort.MIN, uint(3125L));

  public static final NodeId
      ServerDiagnosticsType_ServerDiagnosticsSummary_CumulatedSubscriptionCount =
          new NodeId(UShort.MIN, uint(3126L));

  public static final NodeId
      ServerDiagnosticsType_ServerDiagnosticsSummary_SecurityRejectedRequestsCount =
          new NodeId(UShort.MIN, uint(3127L));

  public static final NodeId ServerDiagnosticsType_ServerDiagnosticsSummary_RejectedRequestsCount =
      new NodeId(UShort.MIN, uint(3128L));

  public static final NodeId
      ServerDiagnosticsType_SessionsDiagnosticsSummary_SessionDiagnosticsArray =
          new NodeId(UShort.MIN, uint(3129L));

  public static final NodeId
      ServerDiagnosticsType_SessionsDiagnosticsSummary_SessionSecurityDiagnosticsArray =
          new NodeId(UShort.MIN, uint(3130L));

  public static final NodeId SessionDiagnosticsObjectType_SessionDiagnostics_SessionId =
      new NodeId(UShort.MIN, uint(3131L));

  public static final NodeId SessionDiagnosticsObjectType_SessionDiagnostics_SessionName =
      new NodeId(UShort.MIN, uint(3132L));

  public static final NodeId SessionDiagnosticsObjectType_SessionDiagnostics_ClientDescription =
      new NodeId(UShort.MIN, uint(3133L));

  public static final NodeId SessionDiagnosticsObjectType_SessionDiagnostics_ServerUri =
      new NodeId(UShort.MIN, uint(3134L));

  public static final NodeId SessionDiagnosticsObjectType_SessionDiagnostics_EndpointUrl =
      new NodeId(UShort.MIN, uint(3135L));

  public static final NodeId SessionDiagnosticsObjectType_SessionDiagnostics_LocaleIds =
      new NodeId(UShort.MIN, uint(3136L));

  public static final NodeId SessionDiagnosticsObjectType_SessionDiagnostics_ActualSessionTimeout =
      new NodeId(UShort.MIN, uint(3137L));

  public static final NodeId
      SessionDiagnosticsObjectType_SessionDiagnostics_MaxResponseMessageSize =
          new NodeId(UShort.MIN, uint(3138L));

  public static final NodeId SessionDiagnosticsObjectType_SessionDiagnostics_ClientConnectionTime =
      new NodeId(UShort.MIN, uint(3139L));

  public static final NodeId SessionDiagnosticsObjectType_SessionDiagnostics_ClientLastContactTime =
      new NodeId(UShort.MIN, uint(3140L));

  public static final NodeId
      SessionDiagnosticsObjectType_SessionDiagnostics_CurrentSubscriptionsCount =
          new NodeId(UShort.MIN, uint(3141L));

  public static final NodeId
      SessionDiagnosticsObjectType_SessionDiagnostics_CurrentMonitoredItemsCount =
          new NodeId(UShort.MIN, uint(3142L));

  public static final NodeId
      SessionDiagnosticsObjectType_SessionDiagnostics_CurrentPublishRequestsInQueue =
          new NodeId(UShort.MIN, uint(3143L));

  public static final NodeId SessionDiagnosticsObjectType_SessionDiagnostics_ReadCount =
      new NodeId(UShort.MIN, uint(3151L));

  public static final NodeId SessionDiagnosticsObjectType_SessionDiagnostics_HistoryReadCount =
      new NodeId(UShort.MIN, uint(3152L));

  public static final NodeId SessionDiagnosticsObjectType_SessionDiagnostics_WriteCount =
      new NodeId(UShort.MIN, uint(3153L));

  public static final NodeId SessionDiagnosticsObjectType_SessionDiagnostics_HistoryUpdateCount =
      new NodeId(UShort.MIN, uint(3154L));

  public static final NodeId SessionDiagnosticsObjectType_SessionDiagnostics_CallCount =
      new NodeId(UShort.MIN, uint(3155L));

  public static final NodeId
      SessionDiagnosticsObjectType_SessionDiagnostics_CreateMonitoredItemsCount =
          new NodeId(UShort.MIN, uint(3156L));

  public static final NodeId
      SessionDiagnosticsObjectType_SessionDiagnostics_ModifyMonitoredItemsCount =
          new NodeId(UShort.MIN, uint(3157L));

  public static final NodeId
      SessionDiagnosticsObjectType_SessionDiagnostics_SetMonitoringModeCount =
          new NodeId(UShort.MIN, uint(3158L));

  public static final NodeId SessionDiagnosticsObjectType_SessionDiagnostics_SetTriggeringCount =
      new NodeId(UShort.MIN, uint(3159L));

  public static final NodeId
      SessionDiagnosticsObjectType_SessionDiagnostics_DeleteMonitoredItemsCount =
          new NodeId(UShort.MIN, uint(3160L));

  public static final NodeId
      SessionDiagnosticsObjectType_SessionDiagnostics_CreateSubscriptionCount =
          new NodeId(UShort.MIN, uint(3161L));

  public static final NodeId
      SessionDiagnosticsObjectType_SessionDiagnostics_ModifySubscriptionCount =
          new NodeId(UShort.MIN, uint(3162L));

  public static final NodeId
      SessionDiagnosticsObjectType_SessionDiagnostics_SetPublishingModeCount =
          new NodeId(UShort.MIN, uint(3163L));

  public static final NodeId SessionDiagnosticsObjectType_SessionDiagnostics_PublishCount =
      new NodeId(UShort.MIN, uint(3164L));

  public static final NodeId SessionDiagnosticsObjectType_SessionDiagnostics_RepublishCount =
      new NodeId(UShort.MIN, uint(3165L));

  public static final NodeId
      SessionDiagnosticsObjectType_SessionDiagnostics_TransferSubscriptionsCount =
          new NodeId(UShort.MIN, uint(3166L));

  public static final NodeId
      SessionDiagnosticsObjectType_SessionDiagnostics_DeleteSubscriptionsCount =
          new NodeId(UShort.MIN, uint(3167L));

  public static final NodeId SessionDiagnosticsObjectType_SessionDiagnostics_AddNodesCount =
      new NodeId(UShort.MIN, uint(3168L));

  public static final NodeId SessionDiagnosticsObjectType_SessionDiagnostics_AddReferencesCount =
      new NodeId(UShort.MIN, uint(3169L));

  public static final NodeId SessionDiagnosticsObjectType_SessionDiagnostics_DeleteNodesCount =
      new NodeId(UShort.MIN, uint(3170L));

  public static final NodeId SessionDiagnosticsObjectType_SessionDiagnostics_DeleteReferencesCount =
      new NodeId(UShort.MIN, uint(3171L));

  public static final NodeId SessionDiagnosticsObjectType_SessionDiagnostics_BrowseCount =
      new NodeId(UShort.MIN, uint(3172L));

  public static final NodeId SessionDiagnosticsObjectType_SessionDiagnostics_BrowseNextCount =
      new NodeId(UShort.MIN, uint(3173L));

  public static final NodeId
      SessionDiagnosticsObjectType_SessionDiagnostics_TranslateBrowsePathsToNodeIdsCount =
          new NodeId(UShort.MIN, uint(3174L));

  public static final NodeId SessionDiagnosticsObjectType_SessionDiagnostics_QueryFirstCount =
      new NodeId(UShort.MIN, uint(3175L));

  public static final NodeId SessionDiagnosticsObjectType_SessionDiagnostics_QueryNextCount =
      new NodeId(UShort.MIN, uint(3176L));

  public static final NodeId SessionDiagnosticsObjectType_SessionDiagnostics_RegisterNodesCount =
      new NodeId(UShort.MIN, uint(3177L));

  public static final NodeId SessionDiagnosticsObjectType_SessionDiagnostics_UnregisterNodesCount =
      new NodeId(UShort.MIN, uint(3178L));

  public static final NodeId SessionDiagnosticsObjectType_SessionSecurityDiagnostics_SessionId =
      new NodeId(UShort.MIN, uint(3179L));

  public static final NodeId
      SessionDiagnosticsObjectType_SessionSecurityDiagnostics_ClientUserIdOfSession =
          new NodeId(UShort.MIN, uint(3180L));

  public static final NodeId
      SessionDiagnosticsObjectType_SessionSecurityDiagnostics_ClientUserIdHistory =
          new NodeId(UShort.MIN, uint(3181L));

  public static final NodeId
      SessionDiagnosticsObjectType_SessionSecurityDiagnostics_AuthenticationMechanism =
          new NodeId(UShort.MIN, uint(3182L));

  public static final NodeId SessionDiagnosticsObjectType_SessionSecurityDiagnostics_Encoding =
      new NodeId(UShort.MIN, uint(3183L));

  public static final NodeId
      SessionDiagnosticsObjectType_SessionSecurityDiagnostics_TransportProtocol =
          new NodeId(UShort.MIN, uint(3184L));

  public static final NodeId SessionDiagnosticsObjectType_SessionSecurityDiagnostics_SecurityMode =
      new NodeId(UShort.MIN, uint(3185L));

  public static final NodeId
      SessionDiagnosticsObjectType_SessionSecurityDiagnostics_SecurityPolicyUri =
          new NodeId(UShort.MIN, uint(3186L));

  public static final NodeId
      SessionDiagnosticsObjectType_SessionSecurityDiagnostics_ClientCertificate =
          new NodeId(UShort.MIN, uint(3187L));

  public static final NodeId BaseEventType_LocalTime = new NodeId(UShort.MIN, uint(3190L));

  public static final NodeId ServerStatusType_BuildInfo_ProductUri =
      new NodeId(UShort.MIN, uint(3698L));

  public static final NodeId ServerStatusType_BuildInfo_ManufacturerName =
      new NodeId(UShort.MIN, uint(3699L));

  public static final NodeId ServerStatusType_BuildInfo_ProductName =
      new NodeId(UShort.MIN, uint(3700L));

  public static final NodeId ServerStatusType_BuildInfo_SoftwareVersion =
      new NodeId(UShort.MIN, uint(3701L));

  public static final NodeId ServerStatusType_BuildInfo_BuildNumber =
      new NodeId(UShort.MIN, uint(3702L));

  public static final NodeId ServerStatusType_BuildInfo_BuildDate =
      new NodeId(UShort.MIN, uint(3703L));

  public static final NodeId Server_ServerCapabilities_SoftwareCertificates =
      new NodeId(UShort.MIN, uint(3704L));

  public static final NodeId
      Server_ServerDiagnostics_ServerDiagnosticsSummary_RejectedSessionCount =
          new NodeId(UShort.MIN, uint(3705L));

  public static final NodeId Server_ServerDiagnostics_SessionsDiagnosticsSummary =
      new NodeId(UShort.MIN, uint(3706L));

  public static final NodeId
      Server_ServerDiagnostics_SessionsDiagnosticsSummary_SessionDiagnosticsArray =
          new NodeId(UShort.MIN, uint(3707L));

  public static final NodeId
      Server_ServerDiagnostics_SessionsDiagnosticsSummary_SessionSecurityDiagnosticsArray =
          new NodeId(UShort.MIN, uint(3708L));

  public static final NodeId Server_ServerRedundancy_RedundancySupport =
      new NodeId(UShort.MIN, uint(3709L));

  public static final NodeId StateMachineType_CurrentState_Id = new NodeId(UShort.MIN, uint(3720L));

  public static final NodeId StateMachineType_CurrentState_Name =
      new NodeId(UShort.MIN, uint(3721L));

  public static final NodeId StateMachineType_CurrentState_Number =
      new NodeId(UShort.MIN, uint(3722L));

  public static final NodeId StateMachineType_CurrentState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(3723L));

  public static final NodeId StateMachineType_LastTransition_Id =
      new NodeId(UShort.MIN, uint(3724L));

  public static final NodeId StateMachineType_LastTransition_Name =
      new NodeId(UShort.MIN, uint(3725L));

  public static final NodeId StateMachineType_LastTransition_Number =
      new NodeId(UShort.MIN, uint(3726L));

  public static final NodeId StateMachineType_LastTransition_TransitionTime =
      new NodeId(UShort.MIN, uint(3727L));

  public static final NodeId FiniteStateMachineType_CurrentState_Id =
      new NodeId(UShort.MIN, uint(3728L));

  public static final NodeId FiniteStateMachineType_CurrentState_Name =
      new NodeId(UShort.MIN, uint(3729L));

  public static final NodeId FiniteStateMachineType_CurrentState_Number =
      new NodeId(UShort.MIN, uint(3730L));

  public static final NodeId FiniteStateMachineType_CurrentState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(3731L));

  public static final NodeId FiniteStateMachineType_LastTransition_Id =
      new NodeId(UShort.MIN, uint(3732L));

  public static final NodeId FiniteStateMachineType_LastTransition_Name =
      new NodeId(UShort.MIN, uint(3733L));

  public static final NodeId FiniteStateMachineType_LastTransition_Number =
      new NodeId(UShort.MIN, uint(3734L));

  public static final NodeId FiniteStateMachineType_LastTransition_TransitionTime =
      new NodeId(UShort.MIN, uint(3735L));

  public static final NodeId TransitionEventType_FromState_Id = new NodeId(UShort.MIN, uint(3746L));

  public static final NodeId TransitionEventType_FromState_Name =
      new NodeId(UShort.MIN, uint(3747L));

  public static final NodeId TransitionEventType_FromState_Number =
      new NodeId(UShort.MIN, uint(3748L));

  public static final NodeId TransitionEventType_FromState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(3749L));

  public static final NodeId TransitionEventType_ToState_Id = new NodeId(UShort.MIN, uint(3750L));

  public static final NodeId TransitionEventType_ToState_Name = new NodeId(UShort.MIN, uint(3751L));

  public static final NodeId TransitionEventType_ToState_Number =
      new NodeId(UShort.MIN, uint(3752L));

  public static final NodeId TransitionEventType_ToState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(3753L));

  public static final NodeId TransitionEventType_Transition_Id =
      new NodeId(UShort.MIN, uint(3754L));

  public static final NodeId TransitionEventType_Transition_Name =
      new NodeId(UShort.MIN, uint(3755L));

  public static final NodeId TransitionEventType_Transition_Number =
      new NodeId(UShort.MIN, uint(3756L));

  public static final NodeId TransitionEventType_Transition_TransitionTime =
      new NodeId(UShort.MIN, uint(3757L));

  public static final NodeId ProgramTransitionAuditEventType = new NodeId(UShort.MIN, uint(3806L));

  public static final NodeId ProgramTransitionAuditEventType_Transition =
      new NodeId(UShort.MIN, uint(3825L));

  public static final NodeId ProgramTransitionAuditEventType_Transition_Id =
      new NodeId(UShort.MIN, uint(3826L));

  public static final NodeId ProgramTransitionAuditEventType_Transition_Name =
      new NodeId(UShort.MIN, uint(3827L));

  public static final NodeId ProgramTransitionAuditEventType_Transition_Number =
      new NodeId(UShort.MIN, uint(3828L));

  public static final NodeId ProgramTransitionAuditEventType_Transition_TransitionTime =
      new NodeId(UShort.MIN, uint(3829L));

  public static final NodeId ProgramStateMachineType_CurrentState =
      new NodeId(UShort.MIN, uint(3830L));

  public static final NodeId ProgramStateMachineType_CurrentState_Id =
      new NodeId(UShort.MIN, uint(3831L));

  public static final NodeId ProgramStateMachineType_CurrentState_Name =
      new NodeId(UShort.MIN, uint(3832L));

  public static final NodeId ProgramStateMachineType_CurrentState_Number =
      new NodeId(UShort.MIN, uint(3833L));

  public static final NodeId ProgramStateMachineType_CurrentState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(3834L));

  public static final NodeId ProgramStateMachineType_LastTransition =
      new NodeId(UShort.MIN, uint(3835L));

  public static final NodeId ProgramStateMachineType_LastTransition_Id =
      new NodeId(UShort.MIN, uint(3836L));

  public static final NodeId ProgramStateMachineType_LastTransition_Name =
      new NodeId(UShort.MIN, uint(3837L));

  public static final NodeId ProgramStateMachineType_LastTransition_Number =
      new NodeId(UShort.MIN, uint(3838L));

  public static final NodeId ProgramStateMachineType_LastTransition_TransitionTime =
      new NodeId(UShort.MIN, uint(3839L));

  public static final NodeId ProgramStateMachineType_ProgramDiagnostic_CreateSessionId =
      new NodeId(UShort.MIN, uint(3840L));

  public static final NodeId ProgramStateMachineType_ProgramDiagnostic_CreateClientName =
      new NodeId(UShort.MIN, uint(3841L));

  public static final NodeId ProgramStateMachineType_ProgramDiagnostic_InvocationCreationTime =
      new NodeId(UShort.MIN, uint(3842L));

  public static final NodeId ProgramStateMachineType_ProgramDiagnostic_LastTransitionTime =
      new NodeId(UShort.MIN, uint(3843L));

  public static final NodeId ProgramStateMachineType_ProgramDiagnostic_LastMethodCall =
      new NodeId(UShort.MIN, uint(3844L));

  public static final NodeId ProgramStateMachineType_ProgramDiagnostic_LastMethodSessionId =
      new NodeId(UShort.MIN, uint(3845L));

  public static final NodeId ProgramStateMachineType_ProgramDiagnostic_LastMethodInputArguments =
      new NodeId(UShort.MIN, uint(3846L));

  public static final NodeId ProgramStateMachineType_ProgramDiagnostic_LastMethodOutputArguments =
      new NodeId(UShort.MIN, uint(3847L));

  public static final NodeId ProgramStateMachineType_ProgramDiagnostic_LastMethodCallTime =
      new NodeId(UShort.MIN, uint(3848L));

  public static final NodeId ProgramStateMachineType_ProgramDiagnostic_LastMethodReturnStatus =
      new NodeId(UShort.MIN, uint(3849L));

  public static final NodeId ProgramStateMachineType_FinalResultData =
      new NodeId(UShort.MIN, uint(3850L));

  public static final NodeId AddCommentMethodType = new NodeId(UShort.MIN, uint(3863L));

  public static final NodeId AddCommentMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(3864L));

  public static final NodeId ConditionType_Retain = new NodeId(UShort.MIN, uint(3874L));

  public static final NodeId ConditionType_ConditionRefresh = new NodeId(UShort.MIN, uint(3875L));

  public static final NodeId ConditionType_ConditionRefresh_InputArguments =
      new NodeId(UShort.MIN, uint(3876L));

  public static final NodeId ShelvedStateMachineType_Unshelved_StateNumber =
      new NodeId(UShort.MIN, uint(6098L));

  public static final NodeId ShelvedStateMachineType_TimedShelved_StateNumber =
      new NodeId(UShort.MIN, uint(6100L));

  public static final NodeId ShelvedStateMachineType_OneShotShelved_StateNumber =
      new NodeId(UShort.MIN, uint(6101L));

  public static final NodeId TimedShelveMethodType = new NodeId(UShort.MIN, uint(6102L));

  public static final NodeId TimedShelveMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(6103L));

  public static final NodeId IdType_EnumStrings = new NodeId(UShort.MIN, uint(7591L));

  public static final NodeId EnumValueType = new NodeId(UShort.MIN, uint(7594L));

  public static final NodeId MessageSecurityMode_EnumStrings = new NodeId(UShort.MIN, uint(7595L));

  public static final NodeId UserTokenType_EnumStrings = new NodeId(UShort.MIN, uint(7596L));

  public static final NodeId ApplicationType_EnumStrings = new NodeId(UShort.MIN, uint(7597L));

  public static final NodeId SecurityTokenRequestType_EnumStrings =
      new NodeId(UShort.MIN, uint(7598L));

  public static final NodeId BrowseDirection_EnumStrings = new NodeId(UShort.MIN, uint(7603L));

  public static final NodeId FilterOperator_EnumStrings = new NodeId(UShort.MIN, uint(7605L));

  public static final NodeId TimestampsToReturn_EnumStrings = new NodeId(UShort.MIN, uint(7606L));

  public static final NodeId MonitoringMode_EnumStrings = new NodeId(UShort.MIN, uint(7608L));

  public static final NodeId DataChangeTrigger_EnumStrings = new NodeId(UShort.MIN, uint(7609L));

  public static final NodeId DeadbandType_EnumStrings = new NodeId(UShort.MIN, uint(7610L));

  public static final NodeId RedundancySupport_EnumStrings = new NodeId(UShort.MIN, uint(7611L));

  public static final NodeId ServerState_EnumStrings = new NodeId(UShort.MIN, uint(7612L));

  public static final NodeId ExceptionDeviationFormat_EnumStrings =
      new NodeId(UShort.MIN, uint(7614L));

  public static final NodeId EnumValueType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(7616L));

  public static final NodeId OpcUa_BinarySchema = new NodeId(UShort.MIN, uint(7617L));

  public static final NodeId OpcUa_BinarySchema_DataTypeVersion =
      new NodeId(UShort.MIN, uint(7618L));

  public static final NodeId OpcUa_BinarySchema_NamespaceUri = new NodeId(UShort.MIN, uint(7619L));

  public static final NodeId OpcUa_BinarySchema_Argument = new NodeId(UShort.MIN, uint(7650L));

  public static final NodeId OpcUa_BinarySchema_Argument_DataTypeVersion =
      new NodeId(UShort.MIN, uint(7651L));

  public static final NodeId OpcUa_BinarySchema_Argument_DictionaryFragment =
      new NodeId(UShort.MIN, uint(7652L));

  public static final NodeId OpcUa_BinarySchema_EnumValueType = new NodeId(UShort.MIN, uint(7656L));

  public static final NodeId OpcUa_BinarySchema_EnumValueType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(7657L));

  public static final NodeId OpcUa_BinarySchema_EnumValueType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(7658L));

  public static final NodeId OpcUa_BinarySchema_StatusResult = new NodeId(UShort.MIN, uint(7659L));

  public static final NodeId OpcUa_BinarySchema_StatusResult_DataTypeVersion =
      new NodeId(UShort.MIN, uint(7660L));

  public static final NodeId OpcUa_BinarySchema_StatusResult_DictionaryFragment =
      new NodeId(UShort.MIN, uint(7661L));

  public static final NodeId OpcUa_BinarySchema_UserTokenPolicy =
      new NodeId(UShort.MIN, uint(7662L));

  public static final NodeId OpcUa_BinarySchema_UserTokenPolicy_DataTypeVersion =
      new NodeId(UShort.MIN, uint(7663L));

  public static final NodeId OpcUa_BinarySchema_UserTokenPolicy_DictionaryFragment =
      new NodeId(UShort.MIN, uint(7664L));

  public static final NodeId OpcUa_BinarySchema_ApplicationDescription =
      new NodeId(UShort.MIN, uint(7665L));

  public static final NodeId OpcUa_BinarySchema_ApplicationDescription_DataTypeVersion =
      new NodeId(UShort.MIN, uint(7666L));

  public static final NodeId OpcUa_BinarySchema_ApplicationDescription_DictionaryFragment =
      new NodeId(UShort.MIN, uint(7667L));

  public static final NodeId OpcUa_BinarySchema_EndpointDescription =
      new NodeId(UShort.MIN, uint(7668L));

  public static final NodeId OpcUa_BinarySchema_EndpointDescription_DataTypeVersion =
      new NodeId(UShort.MIN, uint(7669L));

  public static final NodeId OpcUa_BinarySchema_EndpointDescription_DictionaryFragment =
      new NodeId(UShort.MIN, uint(7670L));

  public static final NodeId OpcUa_BinarySchema_UserIdentityToken =
      new NodeId(UShort.MIN, uint(7671L));

  public static final NodeId OpcUa_BinarySchema_UserIdentityToken_DataTypeVersion =
      new NodeId(UShort.MIN, uint(7672L));

  public static final NodeId OpcUa_BinarySchema_UserIdentityToken_DictionaryFragment =
      new NodeId(UShort.MIN, uint(7673L));

  public static final NodeId OpcUa_BinarySchema_AnonymousIdentityToken =
      new NodeId(UShort.MIN, uint(7674L));

  public static final NodeId OpcUa_BinarySchema_AnonymousIdentityToken_DataTypeVersion =
      new NodeId(UShort.MIN, uint(7675L));

  public static final NodeId OpcUa_BinarySchema_AnonymousIdentityToken_DictionaryFragment =
      new NodeId(UShort.MIN, uint(7676L));

  public static final NodeId OpcUa_BinarySchema_UserNameIdentityToken =
      new NodeId(UShort.MIN, uint(7677L));

  public static final NodeId OpcUa_BinarySchema_UserNameIdentityToken_DataTypeVersion =
      new NodeId(UShort.MIN, uint(7678L));

  public static final NodeId OpcUa_BinarySchema_UserNameIdentityToken_DictionaryFragment =
      new NodeId(UShort.MIN, uint(7679L));

  public static final NodeId OpcUa_BinarySchema_X509IdentityToken =
      new NodeId(UShort.MIN, uint(7680L));

  public static final NodeId OpcUa_BinarySchema_X509IdentityToken_DataTypeVersion =
      new NodeId(UShort.MIN, uint(7681L));

  public static final NodeId OpcUa_BinarySchema_X509IdentityToken_DictionaryFragment =
      new NodeId(UShort.MIN, uint(7682L));

  public static final NodeId OpcUa_BinarySchema_IssuedIdentityToken =
      new NodeId(UShort.MIN, uint(7683L));

  public static final NodeId OpcUa_BinarySchema_IssuedIdentityToken_DataTypeVersion =
      new NodeId(UShort.MIN, uint(7684L));

  public static final NodeId OpcUa_BinarySchema_IssuedIdentityToken_DictionaryFragment =
      new NodeId(UShort.MIN, uint(7685L));

  public static final NodeId OpcUa_BinarySchema_EndpointConfiguration =
      new NodeId(UShort.MIN, uint(7686L));

  public static final NodeId OpcUa_BinarySchema_EndpointConfiguration_DataTypeVersion =
      new NodeId(UShort.MIN, uint(7687L));

  public static final NodeId OpcUa_BinarySchema_EndpointConfiguration_DictionaryFragment =
      new NodeId(UShort.MIN, uint(7688L));

  public static final NodeId OpcUa_BinarySchema_BuildInfo = new NodeId(UShort.MIN, uint(7692L));

  public static final NodeId OpcUa_BinarySchema_BuildInfo_DataTypeVersion =
      new NodeId(UShort.MIN, uint(7693L));

  public static final NodeId OpcUa_BinarySchema_BuildInfo_DictionaryFragment =
      new NodeId(UShort.MIN, uint(7694L));

  public static final NodeId OpcUa_BinarySchema_SignedSoftwareCertificate =
      new NodeId(UShort.MIN, uint(7698L));

  public static final NodeId OpcUa_BinarySchema_SignedSoftwareCertificate_DataTypeVersion =
      new NodeId(UShort.MIN, uint(7699L));

  public static final NodeId OpcUa_BinarySchema_SignedSoftwareCertificate_DictionaryFragment =
      new NodeId(UShort.MIN, uint(7700L));

  public static final NodeId OpcUa_BinarySchema_AddNodesItem = new NodeId(UShort.MIN, uint(7728L));

  public static final NodeId OpcUa_BinarySchema_AddNodesItem_DataTypeVersion =
      new NodeId(UShort.MIN, uint(7729L));

  public static final NodeId OpcUa_BinarySchema_AddNodesItem_DictionaryFragment =
      new NodeId(UShort.MIN, uint(7730L));

  public static final NodeId OpcUa_BinarySchema_AddReferencesItem =
      new NodeId(UShort.MIN, uint(7731L));

  public static final NodeId OpcUa_BinarySchema_AddReferencesItem_DataTypeVersion =
      new NodeId(UShort.MIN, uint(7732L));

  public static final NodeId OpcUa_BinarySchema_AddReferencesItem_DictionaryFragment =
      new NodeId(UShort.MIN, uint(7733L));

  public static final NodeId OpcUa_BinarySchema_DeleteNodesItem =
      new NodeId(UShort.MIN, uint(7734L));

  public static final NodeId OpcUa_BinarySchema_DeleteNodesItem_DataTypeVersion =
      new NodeId(UShort.MIN, uint(7735L));

  public static final NodeId OpcUa_BinarySchema_DeleteNodesItem_DictionaryFragment =
      new NodeId(UShort.MIN, uint(7736L));

  public static final NodeId OpcUa_BinarySchema_DeleteReferencesItem =
      new NodeId(UShort.MIN, uint(7737L));

  public static final NodeId OpcUa_BinarySchema_DeleteReferencesItem_DataTypeVersion =
      new NodeId(UShort.MIN, uint(7738L));

  public static final NodeId OpcUa_BinarySchema_DeleteReferencesItem_DictionaryFragment =
      new NodeId(UShort.MIN, uint(7739L));

  public static final NodeId OpcUa_BinarySchema_RegisteredServer =
      new NodeId(UShort.MIN, uint(7782L));

  public static final NodeId OpcUa_BinarySchema_RegisteredServer_DataTypeVersion =
      new NodeId(UShort.MIN, uint(7783L));

  public static final NodeId OpcUa_BinarySchema_RegisteredServer_DictionaryFragment =
      new NodeId(UShort.MIN, uint(7784L));

  public static final NodeId OpcUa_BinarySchema_ContentFilterElement =
      new NodeId(UShort.MIN, uint(7929L));

  public static final NodeId OpcUa_BinarySchema_ContentFilterElement_DataTypeVersion =
      new NodeId(UShort.MIN, uint(7930L));

  public static final NodeId OpcUa_BinarySchema_ContentFilterElement_DictionaryFragment =
      new NodeId(UShort.MIN, uint(7931L));

  public static final NodeId OpcUa_BinarySchema_ContentFilter = new NodeId(UShort.MIN, uint(7932L));

  public static final NodeId OpcUa_BinarySchema_ContentFilter_DataTypeVersion =
      new NodeId(UShort.MIN, uint(7933L));

  public static final NodeId OpcUa_BinarySchema_ContentFilter_DictionaryFragment =
      new NodeId(UShort.MIN, uint(7934L));

  public static final NodeId OpcUa_BinarySchema_FilterOperand = new NodeId(UShort.MIN, uint(7935L));

  public static final NodeId OpcUa_BinarySchema_FilterOperand_DataTypeVersion =
      new NodeId(UShort.MIN, uint(7936L));

  public static final NodeId OpcUa_BinarySchema_FilterOperand_DictionaryFragment =
      new NodeId(UShort.MIN, uint(7937L));

  public static final NodeId OpcUa_BinarySchema_ElementOperand =
      new NodeId(UShort.MIN, uint(7938L));

  public static final NodeId OpcUa_BinarySchema_ElementOperand_DataTypeVersion =
      new NodeId(UShort.MIN, uint(7939L));

  public static final NodeId OpcUa_BinarySchema_ElementOperand_DictionaryFragment =
      new NodeId(UShort.MIN, uint(7940L));

  public static final NodeId OpcUa_BinarySchema_LiteralOperand =
      new NodeId(UShort.MIN, uint(7941L));

  public static final NodeId OpcUa_BinarySchema_LiteralOperand_DataTypeVersion =
      new NodeId(UShort.MIN, uint(7942L));

  public static final NodeId OpcUa_BinarySchema_LiteralOperand_DictionaryFragment =
      new NodeId(UShort.MIN, uint(7943L));

  public static final NodeId OpcUa_BinarySchema_AttributeOperand =
      new NodeId(UShort.MIN, uint(7944L));

  public static final NodeId OpcUa_BinarySchema_AttributeOperand_DataTypeVersion =
      new NodeId(UShort.MIN, uint(7945L));

  public static final NodeId OpcUa_BinarySchema_AttributeOperand_DictionaryFragment =
      new NodeId(UShort.MIN, uint(7946L));

  public static final NodeId OpcUa_BinarySchema_SimpleAttributeOperand =
      new NodeId(UShort.MIN, uint(7947L));

  public static final NodeId OpcUa_BinarySchema_SimpleAttributeOperand_DataTypeVersion =
      new NodeId(UShort.MIN, uint(7948L));

  public static final NodeId OpcUa_BinarySchema_SimpleAttributeOperand_DictionaryFragment =
      new NodeId(UShort.MIN, uint(7949L));

  public static final NodeId OpcUa_BinarySchema_HistoryEvent = new NodeId(UShort.MIN, uint(8004L));

  public static final NodeId OpcUa_BinarySchema_HistoryEvent_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8005L));

  public static final NodeId OpcUa_BinarySchema_HistoryEvent_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8006L));

  public static final NodeId OpcUa_BinarySchema_MonitoringFilter =
      new NodeId(UShort.MIN, uint(8067L));

  public static final NodeId OpcUa_BinarySchema_MonitoringFilter_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8068L));

  public static final NodeId OpcUa_BinarySchema_MonitoringFilter_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8069L));

  public static final NodeId OpcUa_BinarySchema_EventFilter = new NodeId(UShort.MIN, uint(8073L));

  public static final NodeId OpcUa_BinarySchema_EventFilter_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8074L));

  public static final NodeId OpcUa_BinarySchema_EventFilter_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8075L));

  public static final NodeId OpcUa_BinarySchema_AggregateConfiguration =
      new NodeId(UShort.MIN, uint(8076L));

  public static final NodeId OpcUa_BinarySchema_AggregateConfiguration_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8077L));

  public static final NodeId OpcUa_BinarySchema_AggregateConfiguration_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8078L));

  public static final NodeId OpcUa_BinarySchema_HistoryEventFieldList =
      new NodeId(UShort.MIN, uint(8172L));

  public static final NodeId OpcUa_BinarySchema_HistoryEventFieldList_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8173L));

  public static final NodeId OpcUa_BinarySchema_HistoryEventFieldList_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8174L));

  public static final NodeId OpcUa_BinarySchema_RedundantServerDataType =
      new NodeId(UShort.MIN, uint(8208L));

  public static final NodeId OpcUa_BinarySchema_RedundantServerDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8209L));

  public static final NodeId OpcUa_BinarySchema_RedundantServerDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8210L));

  public static final NodeId OpcUa_BinarySchema_SamplingIntervalDiagnosticsDataType =
      new NodeId(UShort.MIN, uint(8211L));

  public static final NodeId
      OpcUa_BinarySchema_SamplingIntervalDiagnosticsDataType_DataTypeVersion =
          new NodeId(UShort.MIN, uint(8212L));

  public static final NodeId
      OpcUa_BinarySchema_SamplingIntervalDiagnosticsDataType_DictionaryFragment =
          new NodeId(UShort.MIN, uint(8213L));

  public static final NodeId OpcUa_BinarySchema_ServerDiagnosticsSummaryDataType =
      new NodeId(UShort.MIN, uint(8214L));

  public static final NodeId OpcUa_BinarySchema_ServerDiagnosticsSummaryDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8215L));

  public static final NodeId
      OpcUa_BinarySchema_ServerDiagnosticsSummaryDataType_DictionaryFragment =
          new NodeId(UShort.MIN, uint(8216L));

  public static final NodeId OpcUa_BinarySchema_ServerStatusDataType =
      new NodeId(UShort.MIN, uint(8217L));

  public static final NodeId OpcUa_BinarySchema_ServerStatusDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8218L));

  public static final NodeId OpcUa_BinarySchema_ServerStatusDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8219L));

  public static final NodeId OpcUa_BinarySchema_SessionDiagnosticsDataType =
      new NodeId(UShort.MIN, uint(8220L));

  public static final NodeId OpcUa_BinarySchema_SessionDiagnosticsDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8221L));

  public static final NodeId OpcUa_BinarySchema_SessionDiagnosticsDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8222L));

  public static final NodeId OpcUa_BinarySchema_SessionSecurityDiagnosticsDataType =
      new NodeId(UShort.MIN, uint(8223L));

  public static final NodeId OpcUa_BinarySchema_SessionSecurityDiagnosticsDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8224L));

  public static final NodeId
      OpcUa_BinarySchema_SessionSecurityDiagnosticsDataType_DictionaryFragment =
          new NodeId(UShort.MIN, uint(8225L));

  public static final NodeId OpcUa_BinarySchema_ServiceCounterDataType =
      new NodeId(UShort.MIN, uint(8226L));

  public static final NodeId OpcUa_BinarySchema_ServiceCounterDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8227L));

  public static final NodeId OpcUa_BinarySchema_ServiceCounterDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8228L));

  public static final NodeId OpcUa_BinarySchema_SubscriptionDiagnosticsDataType =
      new NodeId(UShort.MIN, uint(8229L));

  public static final NodeId OpcUa_BinarySchema_SubscriptionDiagnosticsDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8230L));

  public static final NodeId OpcUa_BinarySchema_SubscriptionDiagnosticsDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8231L));

  public static final NodeId OpcUa_BinarySchema_ModelChangeStructureDataType =
      new NodeId(UShort.MIN, uint(8232L));

  public static final NodeId OpcUa_BinarySchema_ModelChangeStructureDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8233L));

  public static final NodeId OpcUa_BinarySchema_ModelChangeStructureDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8234L));

  public static final NodeId OpcUa_BinarySchema_SemanticChangeStructureDataType =
      new NodeId(UShort.MIN, uint(8235L));

  public static final NodeId OpcUa_BinarySchema_SemanticChangeStructureDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8236L));

  public static final NodeId OpcUa_BinarySchema_SemanticChangeStructureDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8237L));

  public static final NodeId OpcUa_BinarySchema_Range = new NodeId(UShort.MIN, uint(8238L));

  public static final NodeId OpcUa_BinarySchema_Range_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8239L));

  public static final NodeId OpcUa_BinarySchema_Range_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8240L));

  public static final NodeId OpcUa_BinarySchema_EUInformation = new NodeId(UShort.MIN, uint(8241L));

  public static final NodeId OpcUa_BinarySchema_EUInformation_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8242L));

  public static final NodeId OpcUa_BinarySchema_EUInformation_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8243L));

  public static final NodeId OpcUa_BinarySchema_Annotation = new NodeId(UShort.MIN, uint(8244L));

  public static final NodeId OpcUa_BinarySchema_Annotation_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8245L));

  public static final NodeId OpcUa_BinarySchema_Annotation_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8246L));

  public static final NodeId OpcUa_BinarySchema_ProgramDiagnosticDataType =
      new NodeId(UShort.MIN, uint(8247L));

  public static final NodeId OpcUa_BinarySchema_ProgramDiagnosticDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8248L));

  public static final NodeId OpcUa_BinarySchema_ProgramDiagnosticDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8249L));

  public static final NodeId EnumValueType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(8251L));

  public static final NodeId OpcUa_XmlSchema = new NodeId(UShort.MIN, uint(8252L));

  public static final NodeId OpcUa_XmlSchema_DataTypeVersion = new NodeId(UShort.MIN, uint(8253L));

  public static final NodeId OpcUa_XmlSchema_NamespaceUri = new NodeId(UShort.MIN, uint(8254L));

  public static final NodeId OpcUa_XmlSchema_Argument = new NodeId(UShort.MIN, uint(8285L));

  public static final NodeId OpcUa_XmlSchema_Argument_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8286L));

  public static final NodeId OpcUa_XmlSchema_Argument_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8287L));

  public static final NodeId OpcUa_XmlSchema_EnumValueType = new NodeId(UShort.MIN, uint(8291L));

  public static final NodeId OpcUa_XmlSchema_EnumValueType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8292L));

  public static final NodeId OpcUa_XmlSchema_EnumValueType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8293L));

  public static final NodeId OpcUa_XmlSchema_StatusResult = new NodeId(UShort.MIN, uint(8294L));

  public static final NodeId OpcUa_XmlSchema_StatusResult_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8295L));

  public static final NodeId OpcUa_XmlSchema_StatusResult_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8296L));

  public static final NodeId OpcUa_XmlSchema_UserTokenPolicy = new NodeId(UShort.MIN, uint(8297L));

  public static final NodeId OpcUa_XmlSchema_UserTokenPolicy_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8298L));

  public static final NodeId OpcUa_XmlSchema_UserTokenPolicy_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8299L));

  public static final NodeId OpcUa_XmlSchema_ApplicationDescription =
      new NodeId(UShort.MIN, uint(8300L));

  public static final NodeId OpcUa_XmlSchema_ApplicationDescription_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8301L));

  public static final NodeId OpcUa_XmlSchema_ApplicationDescription_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8302L));

  public static final NodeId OpcUa_XmlSchema_EndpointDescription =
      new NodeId(UShort.MIN, uint(8303L));

  public static final NodeId OpcUa_XmlSchema_EndpointDescription_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8304L));

  public static final NodeId OpcUa_XmlSchema_EndpointDescription_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8305L));

  public static final NodeId OpcUa_XmlSchema_UserIdentityToken =
      new NodeId(UShort.MIN, uint(8306L));

  public static final NodeId OpcUa_XmlSchema_UserIdentityToken_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8307L));

  public static final NodeId OpcUa_XmlSchema_UserIdentityToken_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8308L));

  public static final NodeId OpcUa_XmlSchema_AnonymousIdentityToken =
      new NodeId(UShort.MIN, uint(8309L));

  public static final NodeId OpcUa_XmlSchema_AnonymousIdentityToken_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8310L));

  public static final NodeId OpcUa_XmlSchema_AnonymousIdentityToken_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8311L));

  public static final NodeId OpcUa_XmlSchema_UserNameIdentityToken =
      new NodeId(UShort.MIN, uint(8312L));

  public static final NodeId OpcUa_XmlSchema_UserNameIdentityToken_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8313L));

  public static final NodeId OpcUa_XmlSchema_UserNameIdentityToken_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8314L));

  public static final NodeId OpcUa_XmlSchema_X509IdentityToken =
      new NodeId(UShort.MIN, uint(8315L));

  public static final NodeId OpcUa_XmlSchema_X509IdentityToken_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8316L));

  public static final NodeId OpcUa_XmlSchema_X509IdentityToken_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8317L));

  public static final NodeId OpcUa_XmlSchema_IssuedIdentityToken =
      new NodeId(UShort.MIN, uint(8318L));

  public static final NodeId OpcUa_XmlSchema_IssuedIdentityToken_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8319L));

  public static final NodeId OpcUa_XmlSchema_IssuedIdentityToken_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8320L));

  public static final NodeId OpcUa_XmlSchema_EndpointConfiguration =
      new NodeId(UShort.MIN, uint(8321L));

  public static final NodeId OpcUa_XmlSchema_EndpointConfiguration_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8322L));

  public static final NodeId OpcUa_XmlSchema_EndpointConfiguration_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8323L));

  public static final NodeId OpcUa_XmlSchema_BuildInfo = new NodeId(UShort.MIN, uint(8327L));

  public static final NodeId OpcUa_XmlSchema_BuildInfo_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8328L));

  public static final NodeId OpcUa_XmlSchema_BuildInfo_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8329L));

  public static final NodeId OpcUa_XmlSchema_SignedSoftwareCertificate =
      new NodeId(UShort.MIN, uint(8333L));

  public static final NodeId OpcUa_XmlSchema_SignedSoftwareCertificate_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8334L));

  public static final NodeId OpcUa_XmlSchema_SignedSoftwareCertificate_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8335L));

  public static final NodeId OpcUa_XmlSchema_AddNodesItem = new NodeId(UShort.MIN, uint(8363L));

  public static final NodeId OpcUa_XmlSchema_AddNodesItem_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8364L));

  public static final NodeId OpcUa_XmlSchema_AddNodesItem_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8365L));

  public static final NodeId OpcUa_XmlSchema_AddReferencesItem =
      new NodeId(UShort.MIN, uint(8366L));

  public static final NodeId OpcUa_XmlSchema_AddReferencesItem_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8367L));

  public static final NodeId OpcUa_XmlSchema_AddReferencesItem_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8368L));

  public static final NodeId OpcUa_XmlSchema_DeleteNodesItem = new NodeId(UShort.MIN, uint(8369L));

  public static final NodeId OpcUa_XmlSchema_DeleteNodesItem_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8370L));

  public static final NodeId OpcUa_XmlSchema_DeleteNodesItem_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8371L));

  public static final NodeId OpcUa_XmlSchema_DeleteReferencesItem =
      new NodeId(UShort.MIN, uint(8372L));

  public static final NodeId OpcUa_XmlSchema_DeleteReferencesItem_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8373L));

  public static final NodeId OpcUa_XmlSchema_DeleteReferencesItem_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8374L));

  public static final NodeId OpcUa_XmlSchema_RegisteredServer = new NodeId(UShort.MIN, uint(8417L));

  public static final NodeId OpcUa_XmlSchema_RegisteredServer_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8418L));

  public static final NodeId OpcUa_XmlSchema_RegisteredServer_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8419L));

  public static final NodeId OpcUa_XmlSchema_ContentFilterElement =
      new NodeId(UShort.MIN, uint(8564L));

  public static final NodeId OpcUa_XmlSchema_ContentFilterElement_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8565L));

  public static final NodeId OpcUa_XmlSchema_ContentFilterElement_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8566L));

  public static final NodeId OpcUa_XmlSchema_ContentFilter = new NodeId(UShort.MIN, uint(8567L));

  public static final NodeId OpcUa_XmlSchema_ContentFilter_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8568L));

  public static final NodeId OpcUa_XmlSchema_ContentFilter_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8569L));

  public static final NodeId OpcUa_XmlSchema_FilterOperand = new NodeId(UShort.MIN, uint(8570L));

  public static final NodeId OpcUa_XmlSchema_FilterOperand_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8571L));

  public static final NodeId OpcUa_XmlSchema_FilterOperand_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8572L));

  public static final NodeId OpcUa_XmlSchema_ElementOperand = new NodeId(UShort.MIN, uint(8573L));

  public static final NodeId OpcUa_XmlSchema_ElementOperand_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8574L));

  public static final NodeId OpcUa_XmlSchema_ElementOperand_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8575L));

  public static final NodeId OpcUa_XmlSchema_LiteralOperand = new NodeId(UShort.MIN, uint(8576L));

  public static final NodeId OpcUa_XmlSchema_LiteralOperand_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8577L));

  public static final NodeId OpcUa_XmlSchema_LiteralOperand_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8578L));

  public static final NodeId OpcUa_XmlSchema_AttributeOperand = new NodeId(UShort.MIN, uint(8579L));

  public static final NodeId OpcUa_XmlSchema_AttributeOperand_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8580L));

  public static final NodeId OpcUa_XmlSchema_AttributeOperand_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8581L));

  public static final NodeId OpcUa_XmlSchema_SimpleAttributeOperand =
      new NodeId(UShort.MIN, uint(8582L));

  public static final NodeId OpcUa_XmlSchema_SimpleAttributeOperand_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8583L));

  public static final NodeId OpcUa_XmlSchema_SimpleAttributeOperand_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8584L));

  public static final NodeId OpcUa_XmlSchema_HistoryEvent = new NodeId(UShort.MIN, uint(8639L));

  public static final NodeId OpcUa_XmlSchema_HistoryEvent_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8640L));

  public static final NodeId OpcUa_XmlSchema_HistoryEvent_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8641L));

  public static final NodeId OpcUa_XmlSchema_MonitoringFilter = new NodeId(UShort.MIN, uint(8702L));

  public static final NodeId OpcUa_XmlSchema_MonitoringFilter_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8703L));

  public static final NodeId OpcUa_XmlSchema_MonitoringFilter_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8704L));

  public static final NodeId OpcUa_XmlSchema_EventFilter = new NodeId(UShort.MIN, uint(8708L));

  public static final NodeId OpcUa_XmlSchema_EventFilter_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8709L));

  public static final NodeId OpcUa_XmlSchema_EventFilter_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8710L));

  public static final NodeId OpcUa_XmlSchema_AggregateConfiguration =
      new NodeId(UShort.MIN, uint(8711L));

  public static final NodeId OpcUa_XmlSchema_AggregateConfiguration_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8712L));

  public static final NodeId OpcUa_XmlSchema_AggregateConfiguration_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8713L));

  public static final NodeId OpcUa_XmlSchema_HistoryEventFieldList =
      new NodeId(UShort.MIN, uint(8807L));

  public static final NodeId OpcUa_XmlSchema_HistoryEventFieldList_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8808L));

  public static final NodeId OpcUa_XmlSchema_HistoryEventFieldList_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8809L));

  public static final NodeId OpcUa_XmlSchema_RedundantServerDataType =
      new NodeId(UShort.MIN, uint(8843L));

  public static final NodeId OpcUa_XmlSchema_RedundantServerDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8844L));

  public static final NodeId OpcUa_XmlSchema_RedundantServerDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8845L));

  public static final NodeId OpcUa_XmlSchema_SamplingIntervalDiagnosticsDataType =
      new NodeId(UShort.MIN, uint(8846L));

  public static final NodeId OpcUa_XmlSchema_SamplingIntervalDiagnosticsDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8847L));

  public static final NodeId
      OpcUa_XmlSchema_SamplingIntervalDiagnosticsDataType_DictionaryFragment =
          new NodeId(UShort.MIN, uint(8848L));

  public static final NodeId OpcUa_XmlSchema_ServerDiagnosticsSummaryDataType =
      new NodeId(UShort.MIN, uint(8849L));

  public static final NodeId OpcUa_XmlSchema_ServerDiagnosticsSummaryDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8850L));

  public static final NodeId OpcUa_XmlSchema_ServerDiagnosticsSummaryDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8851L));

  public static final NodeId OpcUa_XmlSchema_ServerStatusDataType =
      new NodeId(UShort.MIN, uint(8852L));

  public static final NodeId OpcUa_XmlSchema_ServerStatusDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8853L));

  public static final NodeId OpcUa_XmlSchema_ServerStatusDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8854L));

  public static final NodeId OpcUa_XmlSchema_SessionDiagnosticsDataType =
      new NodeId(UShort.MIN, uint(8855L));

  public static final NodeId OpcUa_XmlSchema_SessionDiagnosticsDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8856L));

  public static final NodeId OpcUa_XmlSchema_SessionDiagnosticsDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8857L));

  public static final NodeId OpcUa_XmlSchema_SessionSecurityDiagnosticsDataType =
      new NodeId(UShort.MIN, uint(8858L));

  public static final NodeId OpcUa_XmlSchema_SessionSecurityDiagnosticsDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8859L));

  public static final NodeId OpcUa_XmlSchema_SessionSecurityDiagnosticsDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8860L));

  public static final NodeId OpcUa_XmlSchema_ServiceCounterDataType =
      new NodeId(UShort.MIN, uint(8861L));

  public static final NodeId OpcUa_XmlSchema_ServiceCounterDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8862L));

  public static final NodeId OpcUa_XmlSchema_ServiceCounterDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8863L));

  public static final NodeId OpcUa_XmlSchema_SubscriptionDiagnosticsDataType =
      new NodeId(UShort.MIN, uint(8864L));

  public static final NodeId OpcUa_XmlSchema_SubscriptionDiagnosticsDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8865L));

  public static final NodeId OpcUa_XmlSchema_SubscriptionDiagnosticsDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8866L));

  public static final NodeId OpcUa_XmlSchema_ModelChangeStructureDataType =
      new NodeId(UShort.MIN, uint(8867L));

  public static final NodeId OpcUa_XmlSchema_ModelChangeStructureDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8868L));

  public static final NodeId OpcUa_XmlSchema_ModelChangeStructureDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8869L));

  public static final NodeId OpcUa_XmlSchema_SemanticChangeStructureDataType =
      new NodeId(UShort.MIN, uint(8870L));

  public static final NodeId OpcUa_XmlSchema_SemanticChangeStructureDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8871L));

  public static final NodeId OpcUa_XmlSchema_SemanticChangeStructureDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8872L));

  public static final NodeId OpcUa_XmlSchema_Range = new NodeId(UShort.MIN, uint(8873L));

  public static final NodeId OpcUa_XmlSchema_Range_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8874L));

  public static final NodeId OpcUa_XmlSchema_Range_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8875L));

  public static final NodeId OpcUa_XmlSchema_EUInformation = new NodeId(UShort.MIN, uint(8876L));

  public static final NodeId OpcUa_XmlSchema_EUInformation_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8877L));

  public static final NodeId OpcUa_XmlSchema_EUInformation_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8878L));

  public static final NodeId OpcUa_XmlSchema_Annotation = new NodeId(UShort.MIN, uint(8879L));

  public static final NodeId OpcUa_XmlSchema_Annotation_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8880L));

  public static final NodeId OpcUa_XmlSchema_Annotation_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8881L));

  public static final NodeId OpcUa_XmlSchema_ProgramDiagnosticDataType =
      new NodeId(UShort.MIN, uint(8882L));

  public static final NodeId OpcUa_XmlSchema_ProgramDiagnosticDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8883L));

  public static final NodeId OpcUa_XmlSchema_ProgramDiagnosticDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8884L));

  public static final NodeId SubscriptionDiagnosticsType_MaxLifetimeCount =
      new NodeId(UShort.MIN, uint(8888L));

  public static final NodeId SubscriptionDiagnosticsType_LatePublishRequestCount =
      new NodeId(UShort.MIN, uint(8889L));

  public static final NodeId SubscriptionDiagnosticsType_CurrentKeepAliveCount =
      new NodeId(UShort.MIN, uint(8890L));

  public static final NodeId SubscriptionDiagnosticsType_CurrentLifetimeCount =
      new NodeId(UShort.MIN, uint(8891L));

  public static final NodeId SubscriptionDiagnosticsType_UnacknowledgedMessageCount =
      new NodeId(UShort.MIN, uint(8892L));

  public static final NodeId SubscriptionDiagnosticsType_DiscardedMessageCount =
      new NodeId(UShort.MIN, uint(8893L));

  public static final NodeId SubscriptionDiagnosticsType_MonitoredItemCount =
      new NodeId(UShort.MIN, uint(8894L));

  public static final NodeId SubscriptionDiagnosticsType_DisabledMonitoredItemCount =
      new NodeId(UShort.MIN, uint(8895L));

  public static final NodeId SubscriptionDiagnosticsType_MonitoringQueueOverflowCount =
      new NodeId(UShort.MIN, uint(8896L));

  public static final NodeId SubscriptionDiagnosticsType_NextSequenceNumber =
      new NodeId(UShort.MIN, uint(8897L));

  public static final NodeId SessionDiagnosticsObjectType_SessionDiagnostics_TotalRequestCount =
      new NodeId(UShort.MIN, uint(8898L));

  public static final NodeId SessionDiagnosticsVariableType_TotalRequestCount =
      new NodeId(UShort.MIN, uint(8900L));

  public static final NodeId SubscriptionDiagnosticsType_EventQueueOverflowCount =
      new NodeId(UShort.MIN, uint(8902L));

  public static final NodeId TimeZoneDataType = new NodeId(UShort.MIN, uint(8912L));

  public static final NodeId TimeZoneDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(8913L));

  public static final NodeId OpcUa_BinarySchema_TimeZoneDataType =
      new NodeId(UShort.MIN, uint(8914L));

  public static final NodeId OpcUa_BinarySchema_TimeZoneDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8915L));

  public static final NodeId OpcUa_BinarySchema_TimeZoneDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8916L));

  public static final NodeId TimeZoneDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(8917L));

  public static final NodeId OpcUa_XmlSchema_TimeZoneDataType = new NodeId(UShort.MIN, uint(8918L));

  public static final NodeId OpcUa_XmlSchema_TimeZoneDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(8919L));

  public static final NodeId OpcUa_XmlSchema_TimeZoneDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(8920L));

  public static final NodeId AuditConditionRespondEventType = new NodeId(UShort.MIN, uint(8927L));

  public static final NodeId AuditConditionAcknowledgeEventType =
      new NodeId(UShort.MIN, uint(8944L));

  public static final NodeId AuditConditionConfirmEventType = new NodeId(UShort.MIN, uint(8961L));

  public static final NodeId TwoStateVariableType = new NodeId(UShort.MIN, uint(8995L));

  public static final NodeId TwoStateVariableType_Id = new NodeId(UShort.MIN, uint(8996L));

  public static final NodeId TwoStateVariableType_TransitionTime =
      new NodeId(UShort.MIN, uint(9000L));

  public static final NodeId TwoStateVariableType_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(9001L));

  public static final NodeId ConditionVariableType = new NodeId(UShort.MIN, uint(9002L));

  public static final NodeId ConditionVariableType_SourceTimestamp =
      new NodeId(UShort.MIN, uint(9003L));

  public static final NodeId HasTrueSubState = new NodeId(UShort.MIN, uint(9004L));

  public static final NodeId HasFalseSubState = new NodeId(UShort.MIN, uint(9005L));

  public static final NodeId HasCondition = new NodeId(UShort.MIN, uint(9006L));

  public static final NodeId ConditionRefreshMethodType = new NodeId(UShort.MIN, uint(9007L));

  public static final NodeId ConditionRefreshMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(9008L));

  public static final NodeId ConditionType_ConditionName = new NodeId(UShort.MIN, uint(9009L));

  public static final NodeId ConditionType_BranchId = new NodeId(UShort.MIN, uint(9010L));

  public static final NodeId ConditionType_EnabledState = new NodeId(UShort.MIN, uint(9011L));

  public static final NodeId ConditionType_EnabledState_Id = new NodeId(UShort.MIN, uint(9012L));

  public static final NodeId ConditionType_EnabledState_Name = new NodeId(UShort.MIN, uint(9013L));

  public static final NodeId ConditionType_EnabledState_Number =
      new NodeId(UShort.MIN, uint(9014L));

  public static final NodeId ConditionType_EnabledState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(9015L));

  public static final NodeId ConditionType_EnabledState_TransitionTime =
      new NodeId(UShort.MIN, uint(9016L));

  public static final NodeId ConditionType_EnabledState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(9017L));

  public static final NodeId ConditionType_EnabledState_TrueState =
      new NodeId(UShort.MIN, uint(9018L));

  public static final NodeId ConditionType_EnabledState_FalseState =
      new NodeId(UShort.MIN, uint(9019L));

  public static final NodeId ConditionType_Quality = new NodeId(UShort.MIN, uint(9020L));

  public static final NodeId ConditionType_Quality_SourceTimestamp =
      new NodeId(UShort.MIN, uint(9021L));

  public static final NodeId ConditionType_LastSeverity = new NodeId(UShort.MIN, uint(9022L));

  public static final NodeId ConditionType_LastSeverity_SourceTimestamp =
      new NodeId(UShort.MIN, uint(9023L));

  public static final NodeId ConditionType_Comment = new NodeId(UShort.MIN, uint(9024L));

  public static final NodeId ConditionType_Comment_SourceTimestamp =
      new NodeId(UShort.MIN, uint(9025L));

  public static final NodeId ConditionType_ClientUserId = new NodeId(UShort.MIN, uint(9026L));

  public static final NodeId ConditionType_Enable = new NodeId(UShort.MIN, uint(9027L));

  public static final NodeId ConditionType_Disable = new NodeId(UShort.MIN, uint(9028L));

  public static final NodeId ConditionType_AddComment = new NodeId(UShort.MIN, uint(9029L));

  public static final NodeId ConditionType_AddComment_InputArguments =
      new NodeId(UShort.MIN, uint(9030L));

  public static final NodeId DialogResponseMethodType = new NodeId(UShort.MIN, uint(9031L));

  public static final NodeId DialogResponseMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(9032L));

  public static final NodeId DialogConditionType_EnabledState = new NodeId(UShort.MIN, uint(9035L));

  public static final NodeId DialogConditionType_EnabledState_Id =
      new NodeId(UShort.MIN, uint(9036L));

  public static final NodeId DialogConditionType_EnabledState_Name =
      new NodeId(UShort.MIN, uint(9037L));

  public static final NodeId DialogConditionType_EnabledState_Number =
      new NodeId(UShort.MIN, uint(9038L));

  public static final NodeId DialogConditionType_EnabledState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(9039L));

  public static final NodeId DialogConditionType_EnabledState_TransitionTime =
      new NodeId(UShort.MIN, uint(9040L));

  public static final NodeId DialogConditionType_EnabledState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(9041L));

  public static final NodeId DialogConditionType_EnabledState_TrueState =
      new NodeId(UShort.MIN, uint(9042L));

  public static final NodeId DialogConditionType_EnabledState_FalseState =
      new NodeId(UShort.MIN, uint(9043L));

  public static final NodeId DialogConditionType_DialogState = new NodeId(UShort.MIN, uint(9055L));

  public static final NodeId DialogConditionType_DialogState_Id =
      new NodeId(UShort.MIN, uint(9056L));

  public static final NodeId DialogConditionType_DialogState_Name =
      new NodeId(UShort.MIN, uint(9057L));

  public static final NodeId DialogConditionType_DialogState_Number =
      new NodeId(UShort.MIN, uint(9058L));

  public static final NodeId DialogConditionType_DialogState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(9059L));

  public static final NodeId DialogConditionType_DialogState_TransitionTime =
      new NodeId(UShort.MIN, uint(9060L));

  public static final NodeId DialogConditionType_DialogState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(9061L));

  public static final NodeId DialogConditionType_DialogState_TrueState =
      new NodeId(UShort.MIN, uint(9062L));

  public static final NodeId DialogConditionType_DialogState_FalseState =
      new NodeId(UShort.MIN, uint(9063L));

  public static final NodeId DialogConditionType_ResponseOptionSet =
      new NodeId(UShort.MIN, uint(9064L));

  public static final NodeId DialogConditionType_DefaultResponse =
      new NodeId(UShort.MIN, uint(9065L));

  public static final NodeId DialogConditionType_OkResponse = new NodeId(UShort.MIN, uint(9066L));

  public static final NodeId DialogConditionType_CancelResponse =
      new NodeId(UShort.MIN, uint(9067L));

  public static final NodeId DialogConditionType_LastResponse = new NodeId(UShort.MIN, uint(9068L));

  public static final NodeId DialogConditionType_Respond = new NodeId(UShort.MIN, uint(9069L));

  public static final NodeId DialogConditionType_Respond_InputArguments =
      new NodeId(UShort.MIN, uint(9070L));

  public static final NodeId AcknowledgeableConditionType_EnabledState =
      new NodeId(UShort.MIN, uint(9073L));

  public static final NodeId AcknowledgeableConditionType_EnabledState_Id =
      new NodeId(UShort.MIN, uint(9074L));

  public static final NodeId AcknowledgeableConditionType_EnabledState_Name =
      new NodeId(UShort.MIN, uint(9075L));

  public static final NodeId AcknowledgeableConditionType_EnabledState_Number =
      new NodeId(UShort.MIN, uint(9076L));

  public static final NodeId AcknowledgeableConditionType_EnabledState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(9077L));

  public static final NodeId AcknowledgeableConditionType_EnabledState_TransitionTime =
      new NodeId(UShort.MIN, uint(9078L));

  public static final NodeId AcknowledgeableConditionType_EnabledState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(9079L));

  public static final NodeId AcknowledgeableConditionType_EnabledState_TrueState =
      new NodeId(UShort.MIN, uint(9080L));

  public static final NodeId AcknowledgeableConditionType_EnabledState_FalseState =
      new NodeId(UShort.MIN, uint(9081L));

  public static final NodeId AcknowledgeableConditionType_AckedState =
      new NodeId(UShort.MIN, uint(9093L));

  public static final NodeId AcknowledgeableConditionType_AckedState_Id =
      new NodeId(UShort.MIN, uint(9094L));

  public static final NodeId AcknowledgeableConditionType_AckedState_Name =
      new NodeId(UShort.MIN, uint(9095L));

  public static final NodeId AcknowledgeableConditionType_AckedState_Number =
      new NodeId(UShort.MIN, uint(9096L));

  public static final NodeId AcknowledgeableConditionType_AckedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(9097L));

  public static final NodeId AcknowledgeableConditionType_AckedState_TransitionTime =
      new NodeId(UShort.MIN, uint(9098L));

  public static final NodeId AcknowledgeableConditionType_AckedState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(9099L));

  public static final NodeId AcknowledgeableConditionType_AckedState_TrueState =
      new NodeId(UShort.MIN, uint(9100L));

  public static final NodeId AcknowledgeableConditionType_AckedState_FalseState =
      new NodeId(UShort.MIN, uint(9101L));

  public static final NodeId AcknowledgeableConditionType_ConfirmedState =
      new NodeId(UShort.MIN, uint(9102L));

  public static final NodeId AcknowledgeableConditionType_ConfirmedState_Id =
      new NodeId(UShort.MIN, uint(9103L));

  public static final NodeId AcknowledgeableConditionType_ConfirmedState_Name =
      new NodeId(UShort.MIN, uint(9104L));

  public static final NodeId AcknowledgeableConditionType_ConfirmedState_Number =
      new NodeId(UShort.MIN, uint(9105L));

  public static final NodeId AcknowledgeableConditionType_ConfirmedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(9106L));

  public static final NodeId AcknowledgeableConditionType_ConfirmedState_TransitionTime =
      new NodeId(UShort.MIN, uint(9107L));

  public static final NodeId AcknowledgeableConditionType_ConfirmedState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(9108L));

  public static final NodeId AcknowledgeableConditionType_ConfirmedState_TrueState =
      new NodeId(UShort.MIN, uint(9109L));

  public static final NodeId AcknowledgeableConditionType_ConfirmedState_FalseState =
      new NodeId(UShort.MIN, uint(9110L));

  public static final NodeId AcknowledgeableConditionType_Acknowledge =
      new NodeId(UShort.MIN, uint(9111L));

  public static final NodeId AcknowledgeableConditionType_Acknowledge_InputArguments =
      new NodeId(UShort.MIN, uint(9112L));

  public static final NodeId AcknowledgeableConditionType_Confirm =
      new NodeId(UShort.MIN, uint(9113L));

  public static final NodeId AcknowledgeableConditionType_Confirm_InputArguments =
      new NodeId(UShort.MIN, uint(9114L));

  public static final NodeId ShelvedStateMachineType_UnshelveTime =
      new NodeId(UShort.MIN, uint(9115L));

  public static final NodeId AlarmConditionType_EnabledState = new NodeId(UShort.MIN, uint(9118L));

  public static final NodeId AlarmConditionType_EnabledState_Id =
      new NodeId(UShort.MIN, uint(9119L));

  public static final NodeId AlarmConditionType_EnabledState_Name =
      new NodeId(UShort.MIN, uint(9120L));

  public static final NodeId AlarmConditionType_EnabledState_Number =
      new NodeId(UShort.MIN, uint(9121L));

  public static final NodeId AlarmConditionType_EnabledState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(9122L));

  public static final NodeId AlarmConditionType_EnabledState_TransitionTime =
      new NodeId(UShort.MIN, uint(9123L));

  public static final NodeId AlarmConditionType_EnabledState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(9124L));

  public static final NodeId AlarmConditionType_EnabledState_TrueState =
      new NodeId(UShort.MIN, uint(9125L));

  public static final NodeId AlarmConditionType_EnabledState_FalseState =
      new NodeId(UShort.MIN, uint(9126L));

  public static final NodeId AlarmConditionType_ActiveState = new NodeId(UShort.MIN, uint(9160L));

  public static final NodeId AlarmConditionType_ActiveState_Id =
      new NodeId(UShort.MIN, uint(9161L));

  public static final NodeId AlarmConditionType_ActiveState_Name =
      new NodeId(UShort.MIN, uint(9162L));

  public static final NodeId AlarmConditionType_ActiveState_Number =
      new NodeId(UShort.MIN, uint(9163L));

  public static final NodeId AlarmConditionType_ActiveState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(9164L));

  public static final NodeId AlarmConditionType_ActiveState_TransitionTime =
      new NodeId(UShort.MIN, uint(9165L));

  public static final NodeId AlarmConditionType_ActiveState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(9166L));

  public static final NodeId AlarmConditionType_ActiveState_TrueState =
      new NodeId(UShort.MIN, uint(9167L));

  public static final NodeId AlarmConditionType_ActiveState_FalseState =
      new NodeId(UShort.MIN, uint(9168L));

  public static final NodeId AlarmConditionType_SuppressedState =
      new NodeId(UShort.MIN, uint(9169L));

  public static final NodeId AlarmConditionType_SuppressedState_Id =
      new NodeId(UShort.MIN, uint(9170L));

  public static final NodeId AlarmConditionType_SuppressedState_Name =
      new NodeId(UShort.MIN, uint(9171L));

  public static final NodeId AlarmConditionType_SuppressedState_Number =
      new NodeId(UShort.MIN, uint(9172L));

  public static final NodeId AlarmConditionType_SuppressedState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(9173L));

  public static final NodeId AlarmConditionType_SuppressedState_TransitionTime =
      new NodeId(UShort.MIN, uint(9174L));

  public static final NodeId AlarmConditionType_SuppressedState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(9175L));

  public static final NodeId AlarmConditionType_SuppressedState_TrueState =
      new NodeId(UShort.MIN, uint(9176L));

  public static final NodeId AlarmConditionType_SuppressedState_FalseState =
      new NodeId(UShort.MIN, uint(9177L));

  public static final NodeId AlarmConditionType_ShelvingState = new NodeId(UShort.MIN, uint(9178L));

  public static final NodeId AlarmConditionType_ShelvingState_CurrentState =
      new NodeId(UShort.MIN, uint(9179L));

  public static final NodeId AlarmConditionType_ShelvingState_CurrentState_Id =
      new NodeId(UShort.MIN, uint(9180L));

  public static final NodeId AlarmConditionType_ShelvingState_CurrentState_Name =
      new NodeId(UShort.MIN, uint(9181L));

  public static final NodeId AlarmConditionType_ShelvingState_CurrentState_Number =
      new NodeId(UShort.MIN, uint(9182L));

  public static final NodeId AlarmConditionType_ShelvingState_CurrentState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(9183L));

  public static final NodeId AlarmConditionType_ShelvingState_LastTransition =
      new NodeId(UShort.MIN, uint(9184L));

  public static final NodeId AlarmConditionType_ShelvingState_LastTransition_Id =
      new NodeId(UShort.MIN, uint(9185L));

  public static final NodeId AlarmConditionType_ShelvingState_LastTransition_Name =
      new NodeId(UShort.MIN, uint(9186L));

  public static final NodeId AlarmConditionType_ShelvingState_LastTransition_Number =
      new NodeId(UShort.MIN, uint(9187L));

  public static final NodeId AlarmConditionType_ShelvingState_LastTransition_TransitionTime =
      new NodeId(UShort.MIN, uint(9188L));

  public static final NodeId AlarmConditionType_ShelvingState_UnshelveTime =
      new NodeId(UShort.MIN, uint(9189L));

  public static final NodeId AlarmConditionType_ShelvingState_Unshelve =
      new NodeId(UShort.MIN, uint(9211L));

  public static final NodeId AlarmConditionType_ShelvingState_OneShotShelve =
      new NodeId(UShort.MIN, uint(9212L));

  public static final NodeId AlarmConditionType_ShelvingState_TimedShelve =
      new NodeId(UShort.MIN, uint(9213L));

  public static final NodeId AlarmConditionType_ShelvingState_TimedShelve_InputArguments =
      new NodeId(UShort.MIN, uint(9214L));

  public static final NodeId AlarmConditionType_SuppressedOrShelved =
      new NodeId(UShort.MIN, uint(9215L));

  public static final NodeId AlarmConditionType_MaxTimeShelved =
      new NodeId(UShort.MIN, uint(9216L));

  public static final NodeId ExclusiveLimitStateMachineType = new NodeId(UShort.MIN, uint(9318L));

  public static final NodeId ExclusiveLimitStateMachineType_HighHigh =
      new NodeId(UShort.MIN, uint(9329L));

  public static final NodeId ExclusiveLimitStateMachineType_HighHigh_StateNumber =
      new NodeId(UShort.MIN, uint(9330L));

  public static final NodeId ExclusiveLimitStateMachineType_High =
      new NodeId(UShort.MIN, uint(9331L));

  public static final NodeId ExclusiveLimitStateMachineType_High_StateNumber =
      new NodeId(UShort.MIN, uint(9332L));

  public static final NodeId ExclusiveLimitStateMachineType_Low =
      new NodeId(UShort.MIN, uint(9333L));

  public static final NodeId ExclusiveLimitStateMachineType_Low_StateNumber =
      new NodeId(UShort.MIN, uint(9334L));

  public static final NodeId ExclusiveLimitStateMachineType_LowLow =
      new NodeId(UShort.MIN, uint(9335L));

  public static final NodeId ExclusiveLimitStateMachineType_LowLow_StateNumber =
      new NodeId(UShort.MIN, uint(9336L));

  public static final NodeId ExclusiveLimitStateMachineType_LowLowToLow =
      new NodeId(UShort.MIN, uint(9337L));

  public static final NodeId ExclusiveLimitStateMachineType_LowToLowLow =
      new NodeId(UShort.MIN, uint(9338L));

  public static final NodeId ExclusiveLimitStateMachineType_HighHighToHigh =
      new NodeId(UShort.MIN, uint(9339L));

  public static final NodeId ExclusiveLimitStateMachineType_HighToHighHigh =
      new NodeId(UShort.MIN, uint(9340L));

  public static final NodeId ExclusiveLimitAlarmType = new NodeId(UShort.MIN, uint(9341L));

  public static final NodeId ExclusiveLimitAlarmType_ActiveState =
      new NodeId(UShort.MIN, uint(9398L));

  public static final NodeId ExclusiveLimitAlarmType_ActiveState_Id =
      new NodeId(UShort.MIN, uint(9399L));

  public static final NodeId ExclusiveLimitAlarmType_ActiveState_Name =
      new NodeId(UShort.MIN, uint(9400L));

  public static final NodeId ExclusiveLimitAlarmType_ActiveState_Number =
      new NodeId(UShort.MIN, uint(9401L));

  public static final NodeId ExclusiveLimitAlarmType_ActiveState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(9402L));

  public static final NodeId ExclusiveLimitAlarmType_ActiveState_TransitionTime =
      new NodeId(UShort.MIN, uint(9403L));

  public static final NodeId ExclusiveLimitAlarmType_ActiveState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(9404L));

  public static final NodeId ExclusiveLimitAlarmType_ActiveState_TrueState =
      new NodeId(UShort.MIN, uint(9405L));

  public static final NodeId ExclusiveLimitAlarmType_ActiveState_FalseState =
      new NodeId(UShort.MIN, uint(9406L));

  public static final NodeId ExclusiveLimitAlarmType_LimitState =
      new NodeId(UShort.MIN, uint(9455L));

  public static final NodeId ExclusiveLimitAlarmType_LimitState_CurrentState =
      new NodeId(UShort.MIN, uint(9456L));

  public static final NodeId ExclusiveLimitAlarmType_LimitState_CurrentState_Id =
      new NodeId(UShort.MIN, uint(9457L));

  public static final NodeId ExclusiveLimitAlarmType_LimitState_CurrentState_Name =
      new NodeId(UShort.MIN, uint(9458L));

  public static final NodeId ExclusiveLimitAlarmType_LimitState_CurrentState_Number =
      new NodeId(UShort.MIN, uint(9459L));

  public static final NodeId ExclusiveLimitAlarmType_LimitState_CurrentState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(9460L));

  public static final NodeId ExclusiveLimitAlarmType_LimitState_LastTransition =
      new NodeId(UShort.MIN, uint(9461L));

  public static final NodeId ExclusiveLimitAlarmType_LimitState_LastTransition_Id =
      new NodeId(UShort.MIN, uint(9462L));

  public static final NodeId ExclusiveLimitAlarmType_LimitState_LastTransition_Name =
      new NodeId(UShort.MIN, uint(9463L));

  public static final NodeId ExclusiveLimitAlarmType_LimitState_LastTransition_Number =
      new NodeId(UShort.MIN, uint(9464L));

  public static final NodeId ExclusiveLimitAlarmType_LimitState_LastTransition_TransitionTime =
      new NodeId(UShort.MIN, uint(9465L));

  public static final NodeId ExclusiveLevelAlarmType = new NodeId(UShort.MIN, uint(9482L));

  public static final NodeId ExclusiveRateOfChangeAlarmType = new NodeId(UShort.MIN, uint(9623L));

  public static final NodeId ExclusiveDeviationAlarmType = new NodeId(UShort.MIN, uint(9764L));

  public static final NodeId ExclusiveDeviationAlarmType_SetpointNode =
      new NodeId(UShort.MIN, uint(9905L));

  public static final NodeId NonExclusiveLimitAlarmType = new NodeId(UShort.MIN, uint(9906L));

  public static final NodeId NonExclusiveLimitAlarmType_ActiveState =
      new NodeId(UShort.MIN, uint(9963L));

  public static final NodeId NonExclusiveLimitAlarmType_ActiveState_Id =
      new NodeId(UShort.MIN, uint(9964L));

  public static final NodeId NonExclusiveLimitAlarmType_ActiveState_Name =
      new NodeId(UShort.MIN, uint(9965L));

  public static final NodeId NonExclusiveLimitAlarmType_ActiveState_Number =
      new NodeId(UShort.MIN, uint(9966L));

  public static final NodeId NonExclusiveLimitAlarmType_ActiveState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(9967L));

  public static final NodeId NonExclusiveLimitAlarmType_ActiveState_TransitionTime =
      new NodeId(UShort.MIN, uint(9968L));

  public static final NodeId NonExclusiveLimitAlarmType_ActiveState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(9969L));

  public static final NodeId NonExclusiveLimitAlarmType_ActiveState_TrueState =
      new NodeId(UShort.MIN, uint(9970L));

  public static final NodeId NonExclusiveLimitAlarmType_ActiveState_FalseState =
      new NodeId(UShort.MIN, uint(9971L));

  public static final NodeId NonExclusiveLimitAlarmType_HighHighState =
      new NodeId(UShort.MIN, uint(10020L));

  public static final NodeId NonExclusiveLimitAlarmType_HighHighState_Id =
      new NodeId(UShort.MIN, uint(10021L));

  public static final NodeId NonExclusiveLimitAlarmType_HighHighState_Name =
      new NodeId(UShort.MIN, uint(10022L));

  public static final NodeId NonExclusiveLimitAlarmType_HighHighState_Number =
      new NodeId(UShort.MIN, uint(10023L));

  public static final NodeId NonExclusiveLimitAlarmType_HighHighState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(10024L));

  public static final NodeId NonExclusiveLimitAlarmType_HighHighState_TransitionTime =
      new NodeId(UShort.MIN, uint(10025L));

  public static final NodeId NonExclusiveLimitAlarmType_HighHighState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(10026L));

  public static final NodeId NonExclusiveLimitAlarmType_HighHighState_TrueState =
      new NodeId(UShort.MIN, uint(10027L));

  public static final NodeId NonExclusiveLimitAlarmType_HighHighState_FalseState =
      new NodeId(UShort.MIN, uint(10028L));

  public static final NodeId NonExclusiveLimitAlarmType_HighState =
      new NodeId(UShort.MIN, uint(10029L));

  public static final NodeId NonExclusiveLimitAlarmType_HighState_Id =
      new NodeId(UShort.MIN, uint(10030L));

  public static final NodeId NonExclusiveLimitAlarmType_HighState_Name =
      new NodeId(UShort.MIN, uint(10031L));

  public static final NodeId NonExclusiveLimitAlarmType_HighState_Number =
      new NodeId(UShort.MIN, uint(10032L));

  public static final NodeId NonExclusiveLimitAlarmType_HighState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(10033L));

  public static final NodeId NonExclusiveLimitAlarmType_HighState_TransitionTime =
      new NodeId(UShort.MIN, uint(10034L));

  public static final NodeId NonExclusiveLimitAlarmType_HighState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(10035L));

  public static final NodeId NonExclusiveLimitAlarmType_HighState_TrueState =
      new NodeId(UShort.MIN, uint(10036L));

  public static final NodeId NonExclusiveLimitAlarmType_HighState_FalseState =
      new NodeId(UShort.MIN, uint(10037L));

  public static final NodeId NonExclusiveLimitAlarmType_LowState =
      new NodeId(UShort.MIN, uint(10038L));

  public static final NodeId NonExclusiveLimitAlarmType_LowState_Id =
      new NodeId(UShort.MIN, uint(10039L));

  public static final NodeId NonExclusiveLimitAlarmType_LowState_Name =
      new NodeId(UShort.MIN, uint(10040L));

  public static final NodeId NonExclusiveLimitAlarmType_LowState_Number =
      new NodeId(UShort.MIN, uint(10041L));

  public static final NodeId NonExclusiveLimitAlarmType_LowState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(10042L));

  public static final NodeId NonExclusiveLimitAlarmType_LowState_TransitionTime =
      new NodeId(UShort.MIN, uint(10043L));

  public static final NodeId NonExclusiveLimitAlarmType_LowState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(10044L));

  public static final NodeId NonExclusiveLimitAlarmType_LowState_TrueState =
      new NodeId(UShort.MIN, uint(10045L));

  public static final NodeId NonExclusiveLimitAlarmType_LowState_FalseState =
      new NodeId(UShort.MIN, uint(10046L));

  public static final NodeId NonExclusiveLimitAlarmType_LowLowState =
      new NodeId(UShort.MIN, uint(10047L));

  public static final NodeId NonExclusiveLimitAlarmType_LowLowState_Id =
      new NodeId(UShort.MIN, uint(10048L));

  public static final NodeId NonExclusiveLimitAlarmType_LowLowState_Name =
      new NodeId(UShort.MIN, uint(10049L));

  public static final NodeId NonExclusiveLimitAlarmType_LowLowState_Number =
      new NodeId(UShort.MIN, uint(10050L));

  public static final NodeId NonExclusiveLimitAlarmType_LowLowState_EffectiveDisplayName =
      new NodeId(UShort.MIN, uint(10051L));

  public static final NodeId NonExclusiveLimitAlarmType_LowLowState_TransitionTime =
      new NodeId(UShort.MIN, uint(10052L));

  public static final NodeId NonExclusiveLimitAlarmType_LowLowState_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(10053L));

  public static final NodeId NonExclusiveLimitAlarmType_LowLowState_TrueState =
      new NodeId(UShort.MIN, uint(10054L));

  public static final NodeId NonExclusiveLimitAlarmType_LowLowState_FalseState =
      new NodeId(UShort.MIN, uint(10055L));

  public static final NodeId NonExclusiveLevelAlarmType = new NodeId(UShort.MIN, uint(10060L));

  public static final NodeId NonExclusiveRateOfChangeAlarmType =
      new NodeId(UShort.MIN, uint(10214L));

  public static final NodeId NonExclusiveDeviationAlarmType = new NodeId(UShort.MIN, uint(10368L));

  public static final NodeId NonExclusiveDeviationAlarmType_SetpointNode =
      new NodeId(UShort.MIN, uint(10522L));

  public static final NodeId DiscreteAlarmType = new NodeId(UShort.MIN, uint(10523L));

  public static final NodeId OffNormalAlarmType = new NodeId(UShort.MIN, uint(10637L));

  public static final NodeId TripAlarmType = new NodeId(UShort.MIN, uint(10751L));

  public static final NodeId AuditConditionShelvingEventType = new NodeId(UShort.MIN, uint(11093L));

  public static final NodeId TwoStateVariableType_TrueState = new NodeId(UShort.MIN, uint(11110L));

  public static final NodeId TwoStateVariableType_FalseState = new NodeId(UShort.MIN, uint(11111L));

  public static final NodeId ConditionType_ConditionClassId = new NodeId(UShort.MIN, uint(11112L));

  public static final NodeId ConditionType_ConditionClassName =
      new NodeId(UShort.MIN, uint(11113L));

  public static final NodeId AlarmConditionType_InputNode = new NodeId(UShort.MIN, uint(11120L));

  public static final NodeId LimitAlarmType_HighHighLimit = new NodeId(UShort.MIN, uint(11124L));

  public static final NodeId LimitAlarmType_HighLimit = new NodeId(UShort.MIN, uint(11125L));

  public static final NodeId LimitAlarmType_LowLimit = new NodeId(UShort.MIN, uint(11126L));

  public static final NodeId LimitAlarmType_LowLowLimit = new NodeId(UShort.MIN, uint(11127L));

  public static final NodeId OffNormalAlarmType_NormalState = new NodeId(UShort.MIN, uint(11158L));

  public static final NodeId BaseConditionClassType = new NodeId(UShort.MIN, uint(11163L));

  public static final NodeId ProcessConditionClassType = new NodeId(UShort.MIN, uint(11164L));

  public static final NodeId MaintenanceConditionClassType = new NodeId(UShort.MIN, uint(11165L));

  public static final NodeId SystemConditionClassType = new NodeId(UShort.MIN, uint(11166L));

  public static final NodeId
      HistoricalDataConfigurationType_AggregateConfiguration_TreatUncertainAsBad =
          new NodeId(UShort.MIN, uint(11168L));

  public static final NodeId HistoricalDataConfigurationType_AggregateConfiguration_PercentDataBad =
      new NodeId(UShort.MIN, uint(11169L));

  public static final NodeId
      HistoricalDataConfigurationType_AggregateConfiguration_PercentDataGood =
          new NodeId(UShort.MIN, uint(11170L));

  public static final NodeId
      HistoricalDataConfigurationType_AggregateConfiguration_UseSlopedExtrapolation =
          new NodeId(UShort.MIN, uint(11171L));

  public static final NodeId HistoryServerCapabilitiesType_AggregateFunctions =
      new NodeId(UShort.MIN, uint(11172L));

  public static final NodeId AggregateConfigurationType = new NodeId(UShort.MIN, uint(11187L));

  public static final NodeId AggregateConfigurationType_TreatUncertainAsBad =
      new NodeId(UShort.MIN, uint(11188L));

  public static final NodeId AggregateConfigurationType_PercentDataBad =
      new NodeId(UShort.MIN, uint(11189L));

  public static final NodeId AggregateConfigurationType_PercentDataGood =
      new NodeId(UShort.MIN, uint(11190L));

  public static final NodeId AggregateConfigurationType_UseSlopedExtrapolation =
      new NodeId(UShort.MIN, uint(11191L));

  public static final NodeId HistoryServerCapabilities = new NodeId(UShort.MIN, uint(11192L));

  public static final NodeId HistoryServerCapabilities_AccessHistoryDataCapability =
      new NodeId(UShort.MIN, uint(11193L));

  public static final NodeId HistoryServerCapabilities_InsertDataCapability =
      new NodeId(UShort.MIN, uint(11196L));

  public static final NodeId HistoryServerCapabilities_ReplaceDataCapability =
      new NodeId(UShort.MIN, uint(11197L));

  public static final NodeId HistoryServerCapabilities_UpdateDataCapability =
      new NodeId(UShort.MIN, uint(11198L));

  public static final NodeId HistoryServerCapabilities_DeleteRawCapability =
      new NodeId(UShort.MIN, uint(11199L));

  public static final NodeId HistoryServerCapabilities_DeleteAtTimeCapability =
      new NodeId(UShort.MIN, uint(11200L));

  public static final NodeId HistoryServerCapabilities_AggregateFunctions =
      new NodeId(UShort.MIN, uint(11201L));

  public static final NodeId HAConfiguration = new NodeId(UShort.MIN, uint(11202L));

  public static final NodeId HAConfiguration_AggregateConfiguration =
      new NodeId(UShort.MIN, uint(11203L));

  public static final NodeId HAConfiguration_AggregateConfiguration_TreatUncertainAsBad =
      new NodeId(UShort.MIN, uint(11204L));

  public static final NodeId HAConfiguration_AggregateConfiguration_PercentDataBad =
      new NodeId(UShort.MIN, uint(11205L));

  public static final NodeId HAConfiguration_AggregateConfiguration_PercentDataGood =
      new NodeId(UShort.MIN, uint(11206L));

  public static final NodeId HAConfiguration_AggregateConfiguration_UseSlopedExtrapolation =
      new NodeId(UShort.MIN, uint(11207L));

  public static final NodeId HAConfiguration_Stepped = new NodeId(UShort.MIN, uint(11208L));

  public static final NodeId HAConfiguration_Definition = new NodeId(UShort.MIN, uint(11209L));

  public static final NodeId HAConfiguration_MaxTimeInterval = new NodeId(UShort.MIN, uint(11210L));

  public static final NodeId HAConfiguration_MinTimeInterval = new NodeId(UShort.MIN, uint(11211L));

  public static final NodeId HAConfiguration_ExceptionDeviation =
      new NodeId(UShort.MIN, uint(11212L));

  public static final NodeId HAConfiguration_ExceptionDeviationFormat =
      new NodeId(UShort.MIN, uint(11213L));

  public static final NodeId Annotations = new NodeId(UShort.MIN, uint(11214L));

  public static final NodeId HistoricalEventFilter = new NodeId(UShort.MIN, uint(11215L));

  public static final NodeId ModificationInfo = new NodeId(UShort.MIN, uint(11216L));

  public static final NodeId HistoryModifiedData = new NodeId(UShort.MIN, uint(11217L));

  public static final NodeId ModificationInfo_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(11218L));

  public static final NodeId HistoryModifiedData_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(11219L));

  public static final NodeId ModificationInfo_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(11226L));

  public static final NodeId HistoryModifiedData_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(11227L));

  public static final NodeId HistoryUpdateType = new NodeId(UShort.MIN, uint(11234L));

  public static final NodeId MultiStateValueDiscreteType = new NodeId(UShort.MIN, uint(11238L));

  public static final NodeId MultiStateValueDiscreteType_EnumValues =
      new NodeId(UShort.MIN, uint(11241L));

  public static final NodeId HistoryServerCapabilities_AccessHistoryEventsCapability =
      new NodeId(UShort.MIN, uint(11242L));

  public static final NodeId HistoryServerCapabilitiesType_MaxReturnDataValues =
      new NodeId(UShort.MIN, uint(11268L));

  public static final NodeId HistoryServerCapabilitiesType_MaxReturnEventValues =
      new NodeId(UShort.MIN, uint(11269L));

  public static final NodeId HistoryServerCapabilitiesType_InsertAnnotationCapability =
      new NodeId(UShort.MIN, uint(11270L));

  public static final NodeId HistoryServerCapabilities_MaxReturnDataValues =
      new NodeId(UShort.MIN, uint(11273L));

  public static final NodeId HistoryServerCapabilities_MaxReturnEventValues =
      new NodeId(UShort.MIN, uint(11274L));

  public static final NodeId HistoryServerCapabilities_InsertAnnotationCapability =
      new NodeId(UShort.MIN, uint(11275L));

  public static final NodeId HistoryServerCapabilitiesType_InsertEventCapability =
      new NodeId(UShort.MIN, uint(11278L));

  public static final NodeId HistoryServerCapabilitiesType_ReplaceEventCapability =
      new NodeId(UShort.MIN, uint(11279L));

  public static final NodeId HistoryServerCapabilitiesType_UpdateEventCapability =
      new NodeId(UShort.MIN, uint(11280L));

  public static final NodeId HistoryServerCapabilities_InsertEventCapability =
      new NodeId(UShort.MIN, uint(11281L));

  public static final NodeId HistoryServerCapabilities_ReplaceEventCapability =
      new NodeId(UShort.MIN, uint(11282L));

  public static final NodeId HistoryServerCapabilities_UpdateEventCapability =
      new NodeId(UShort.MIN, uint(11283L));

  public static final NodeId AggregateFunction_TimeAverage2 = new NodeId(UShort.MIN, uint(11285L));

  public static final NodeId AggregateFunction_Minimum2 = new NodeId(UShort.MIN, uint(11286L));

  public static final NodeId AggregateFunction_Maximum2 = new NodeId(UShort.MIN, uint(11287L));

  public static final NodeId AggregateFunction_Range2 = new NodeId(UShort.MIN, uint(11288L));

  public static final NodeId AggregateFunction_WorstQuality2 = new NodeId(UShort.MIN, uint(11292L));

  public static final NodeId PerformUpdateType = new NodeId(UShort.MIN, uint(11293L));

  public static final NodeId UpdateStructureDataDetails = new NodeId(UShort.MIN, uint(11295L));

  public static final NodeId UpdateStructureDataDetails_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(11296L));

  public static final NodeId UpdateStructureDataDetails_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(11300L));

  public static final NodeId AggregateFunction_Total2 = new NodeId(UShort.MIN, uint(11304L));

  public static final NodeId AggregateFunction_MinimumActualTime2 =
      new NodeId(UShort.MIN, uint(11305L));

  public static final NodeId AggregateFunction_MaximumActualTime2 =
      new NodeId(UShort.MIN, uint(11306L));

  public static final NodeId AggregateFunction_DurationInStateZero =
      new NodeId(UShort.MIN, uint(11307L));

  public static final NodeId AggregateFunction_DurationInStateNonZero =
      new NodeId(UShort.MIN, uint(11308L));

  public static final NodeId Server_ServerRedundancy_CurrentServerId =
      new NodeId(UShort.MIN, uint(11312L));

  public static final NodeId Server_ServerRedundancy_RedundantServerArray =
      new NodeId(UShort.MIN, uint(11313L));

  public static final NodeId Server_ServerRedundancy_ServerUriArray =
      new NodeId(UShort.MIN, uint(11314L));

  public static final NodeId ShelvedStateMachineType_UnshelvedToTimedShelved_TransitionNumber =
      new NodeId(UShort.MIN, uint(11322L));

  public static final NodeId ShelvedStateMachineType_UnshelvedToOneShotShelved_TransitionNumber =
      new NodeId(UShort.MIN, uint(11323L));

  public static final NodeId ShelvedStateMachineType_TimedShelvedToUnshelved_TransitionNumber =
      new NodeId(UShort.MIN, uint(11324L));

  public static final NodeId ShelvedStateMachineType_TimedShelvedToOneShotShelved_TransitionNumber =
      new NodeId(UShort.MIN, uint(11325L));

  public static final NodeId ShelvedStateMachineType_OneShotShelvedToUnshelved_TransitionNumber =
      new NodeId(UShort.MIN, uint(11326L));

  public static final NodeId ShelvedStateMachineType_OneShotShelvedToTimedShelved_TransitionNumber =
      new NodeId(UShort.MIN, uint(11327L));

  public static final NodeId ExclusiveLimitStateMachineType_LowLowToLow_TransitionNumber =
      new NodeId(UShort.MIN, uint(11340L));

  public static final NodeId ExclusiveLimitStateMachineType_LowToLowLow_TransitionNumber =
      new NodeId(UShort.MIN, uint(11341L));

  public static final NodeId ExclusiveLimitStateMachineType_HighHighToHigh_TransitionNumber =
      new NodeId(UShort.MIN, uint(11342L));

  public static final NodeId ExclusiveLimitStateMachineType_HighToHighHigh_TransitionNumber =
      new NodeId(UShort.MIN, uint(11343L));

  public static final NodeId AggregateFunction_StandardDeviationSample =
      new NodeId(UShort.MIN, uint(11426L));

  public static final NodeId AggregateFunction_StandardDeviationPopulation =
      new NodeId(UShort.MIN, uint(11427L));

  public static final NodeId AggregateFunction_VarianceSample =
      new NodeId(UShort.MIN, uint(11428L));

  public static final NodeId AggregateFunction_VariancePopulation =
      new NodeId(UShort.MIN, uint(11429L));

  public static final NodeId EnumStrings = new NodeId(UShort.MIN, uint(11432L));

  public static final NodeId ValueAsText = new NodeId(UShort.MIN, uint(11433L));

  public static final NodeId ProgressEventType = new NodeId(UShort.MIN, uint(11436L));

  public static final NodeId SystemStatusChangeEventType = new NodeId(UShort.MIN, uint(11446L));

  public static final NodeId TransitionVariableType_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(11456L));

  public static final NodeId StateMachineType_LastTransition_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(11458L));

  public static final NodeId FiniteStateMachineType_LastTransition_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(11459L));

  public static final NodeId TransitionEventType_Transition_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(11460L));

  public static final NodeId MultiStateValueDiscreteType_ValueAsText =
      new NodeId(UShort.MIN, uint(11461L));

  public static final NodeId ProgramTransitionAuditEventType_Transition_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(11463L));

  public static final NodeId ProgramStateMachineType_LastTransition_EffectiveTransitionTime =
      new NodeId(UShort.MIN, uint(11464L));

  public static final NodeId
      AlarmConditionType_ShelvingState_LastTransition_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(11466L));

  public static final NodeId
      ExclusiveLimitAlarmType_LimitState_LastTransition_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(11470L));

  public static final NodeId AuditActivateSessionEventType_SecureChannelId =
      new NodeId(UShort.MIN, uint(11485L));

  public static final NodeId OptionSetType = new NodeId(UShort.MIN, uint(11487L));

  public static final NodeId OptionSetType_OptionSetValues = new NodeId(UShort.MIN, uint(11488L));

  public static final NodeId ServerType_GetMonitoredItems = new NodeId(UShort.MIN, uint(11489L));

  public static final NodeId ServerType_GetMonitoredItems_InputArguments =
      new NodeId(UShort.MIN, uint(11490L));

  public static final NodeId ServerType_GetMonitoredItems_OutputArguments =
      new NodeId(UShort.MIN, uint(11491L));

  public static final NodeId Server_GetMonitoredItems = new NodeId(UShort.MIN, uint(11492L));

  public static final NodeId Server_GetMonitoredItems_InputArguments =
      new NodeId(UShort.MIN, uint(11493L));

  public static final NodeId Server_GetMonitoredItems_OutputArguments =
      new NodeId(UShort.MIN, uint(11494L));

  public static final NodeId GetMonitoredItemsMethodType = new NodeId(UShort.MIN, uint(11495L));

  public static final NodeId GetMonitoredItemsMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(11496L));

  public static final NodeId GetMonitoredItemsMethodType_OutputArguments =
      new NodeId(UShort.MIN, uint(11497L));

  public static final NodeId MaxStringLength = new NodeId(UShort.MIN, uint(11498L));

  public static final NodeId HistoricalDataConfigurationType_StartOfArchive =
      new NodeId(UShort.MIN, uint(11499L));

  public static final NodeId HistoricalDataConfigurationType_StartOfOnlineArchive =
      new NodeId(UShort.MIN, uint(11500L));

  public static final NodeId HistoryServerCapabilitiesType_DeleteEventCapability =
      new NodeId(UShort.MIN, uint(11501L));

  public static final NodeId HistoryServerCapabilities_DeleteEventCapability =
      new NodeId(UShort.MIN, uint(11502L));

  public static final NodeId HAConfiguration_StartOfArchive = new NodeId(UShort.MIN, uint(11503L));

  public static final NodeId HAConfiguration_StartOfOnlineArchive =
      new NodeId(UShort.MIN, uint(11504L));

  public static final NodeId AggregateFunction_StartBound = new NodeId(UShort.MIN, uint(11505L));

  public static final NodeId AggregateFunction_EndBound = new NodeId(UShort.MIN, uint(11506L));

  public static final NodeId AggregateFunction_DeltaBounds = new NodeId(UShort.MIN, uint(11507L));

  public static final NodeId ModellingRule_OptionalPlaceholder =
      new NodeId(UShort.MIN, uint(11508L));

  public static final NodeId ModellingRule_MandatoryPlaceholder =
      new NodeId(UShort.MIN, uint(11510L));

  public static final NodeId MaxArrayLength = new NodeId(UShort.MIN, uint(11512L));

  public static final NodeId EngineeringUnits = new NodeId(UShort.MIN, uint(11513L));

  public static final NodeId ServerType_ServerCapabilities_MaxArrayLength =
      new NodeId(UShort.MIN, uint(11514L));

  public static final NodeId ServerType_ServerCapabilities_MaxStringLength =
      new NodeId(UShort.MIN, uint(11515L));

  public static final NodeId ServerType_ServerCapabilities_OperationLimits =
      new NodeId(UShort.MIN, uint(11516L));

  public static final NodeId ServerType_ServerCapabilities_OperationLimits_MaxNodesPerRead =
      new NodeId(UShort.MIN, uint(11517L));

  public static final NodeId ServerType_ServerCapabilities_OperationLimits_MaxNodesPerWrite =
      new NodeId(UShort.MIN, uint(11519L));

  public static final NodeId ServerType_ServerCapabilities_OperationLimits_MaxNodesPerMethodCall =
      new NodeId(UShort.MIN, uint(11521L));

  public static final NodeId ServerType_ServerCapabilities_OperationLimits_MaxNodesPerBrowse =
      new NodeId(UShort.MIN, uint(11522L));

  public static final NodeId
      ServerType_ServerCapabilities_OperationLimits_MaxNodesPerRegisterNodes =
          new NodeId(UShort.MIN, uint(11523L));

  public static final NodeId
      ServerType_ServerCapabilities_OperationLimits_MaxNodesPerTranslateBrowsePathsToNodeIds =
          new NodeId(UShort.MIN, uint(11524L));

  public static final NodeId
      ServerType_ServerCapabilities_OperationLimits_MaxNodesPerNodeManagement =
          new NodeId(UShort.MIN, uint(11525L));

  public static final NodeId
      ServerType_ServerCapabilities_OperationLimits_MaxMonitoredItemsPerCall =
          new NodeId(UShort.MIN, uint(11526L));

  public static final NodeId ServerType_Namespaces = new NodeId(UShort.MIN, uint(11527L));

  public static final NodeId ServerCapabilitiesType_MaxArrayLength =
      new NodeId(UShort.MIN, uint(11549L));

  public static final NodeId ServerCapabilitiesType_MaxStringLength =
      new NodeId(UShort.MIN, uint(11550L));

  public static final NodeId ServerCapabilitiesType_OperationLimits =
      new NodeId(UShort.MIN, uint(11551L));

  public static final NodeId ServerCapabilitiesType_OperationLimits_MaxNodesPerRead =
      new NodeId(UShort.MIN, uint(11552L));

  public static final NodeId ServerCapabilitiesType_OperationLimits_MaxNodesPerWrite =
      new NodeId(UShort.MIN, uint(11554L));

  public static final NodeId ServerCapabilitiesType_OperationLimits_MaxNodesPerMethodCall =
      new NodeId(UShort.MIN, uint(11556L));

  public static final NodeId ServerCapabilitiesType_OperationLimits_MaxNodesPerBrowse =
      new NodeId(UShort.MIN, uint(11557L));

  public static final NodeId ServerCapabilitiesType_OperationLimits_MaxNodesPerRegisterNodes =
      new NodeId(UShort.MIN, uint(11558L));

  public static final NodeId
      ServerCapabilitiesType_OperationLimits_MaxNodesPerTranslateBrowsePathsToNodeIds =
          new NodeId(UShort.MIN, uint(11559L));

  public static final NodeId ServerCapabilitiesType_OperationLimits_MaxNodesPerNodeManagement =
      new NodeId(UShort.MIN, uint(11560L));

  public static final NodeId ServerCapabilitiesType_OperationLimits_MaxMonitoredItemsPerCall =
      new NodeId(UShort.MIN, uint(11561L));

  public static final NodeId ServerCapabilitiesType_VendorCapability_Placeholder =
      new NodeId(UShort.MIN, uint(11562L));

  public static final NodeId OperationLimitsType = new NodeId(UShort.MIN, uint(11564L));

  public static final NodeId OperationLimitsType_MaxNodesPerRead =
      new NodeId(UShort.MIN, uint(11565L));

  public static final NodeId OperationLimitsType_MaxNodesPerWrite =
      new NodeId(UShort.MIN, uint(11567L));

  public static final NodeId OperationLimitsType_MaxNodesPerMethodCall =
      new NodeId(UShort.MIN, uint(11569L));

  public static final NodeId OperationLimitsType_MaxNodesPerBrowse =
      new NodeId(UShort.MIN, uint(11570L));

  public static final NodeId OperationLimitsType_MaxNodesPerRegisterNodes =
      new NodeId(UShort.MIN, uint(11571L));

  public static final NodeId OperationLimitsType_MaxNodesPerTranslateBrowsePathsToNodeIds =
      new NodeId(UShort.MIN, uint(11572L));

  public static final NodeId OperationLimitsType_MaxNodesPerNodeManagement =
      new NodeId(UShort.MIN, uint(11573L));

  public static final NodeId OperationLimitsType_MaxMonitoredItemsPerCall =
      new NodeId(UShort.MIN, uint(11574L));

  public static final NodeId FileType = new NodeId(UShort.MIN, uint(11575L));

  public static final NodeId FileType_Size = new NodeId(UShort.MIN, uint(11576L));

  public static final NodeId FileType_OpenCount = new NodeId(UShort.MIN, uint(11579L));

  public static final NodeId FileType_Open = new NodeId(UShort.MIN, uint(11580L));

  public static final NodeId FileType_Open_InputArguments = new NodeId(UShort.MIN, uint(11581L));

  public static final NodeId FileType_Open_OutputArguments = new NodeId(UShort.MIN, uint(11582L));

  public static final NodeId FileType_Close = new NodeId(UShort.MIN, uint(11583L));

  public static final NodeId FileType_Close_InputArguments = new NodeId(UShort.MIN, uint(11584L));

  public static final NodeId FileType_Read = new NodeId(UShort.MIN, uint(11585L));

  public static final NodeId FileType_Read_InputArguments = new NodeId(UShort.MIN, uint(11586L));

  public static final NodeId FileType_Read_OutputArguments = new NodeId(UShort.MIN, uint(11587L));

  public static final NodeId FileType_Write = new NodeId(UShort.MIN, uint(11588L));

  public static final NodeId FileType_Write_InputArguments = new NodeId(UShort.MIN, uint(11589L));

  public static final NodeId FileType_GetPosition = new NodeId(UShort.MIN, uint(11590L));

  public static final NodeId FileType_GetPosition_InputArguments =
      new NodeId(UShort.MIN, uint(11591L));

  public static final NodeId FileType_GetPosition_OutputArguments =
      new NodeId(UShort.MIN, uint(11592L));

  public static final NodeId FileType_SetPosition = new NodeId(UShort.MIN, uint(11593L));

  public static final NodeId FileType_SetPosition_InputArguments =
      new NodeId(UShort.MIN, uint(11594L));

  public static final NodeId AddressSpaceFileType = new NodeId(UShort.MIN, uint(11595L));

  public static final NodeId AddressSpaceFileType_ExportNamespace =
      new NodeId(UShort.MIN, uint(11615L));

  public static final NodeId NamespaceMetadataType = new NodeId(UShort.MIN, uint(11616L));

  public static final NodeId NamespaceMetadataType_NamespaceUri =
      new NodeId(UShort.MIN, uint(11617L));

  public static final NodeId NamespaceMetadataType_NamespaceVersion =
      new NodeId(UShort.MIN, uint(11618L));

  public static final NodeId NamespaceMetadataType_NamespacePublicationDate =
      new NodeId(UShort.MIN, uint(11619L));

  public static final NodeId NamespaceMetadataType_IsNamespaceSubset =
      new NodeId(UShort.MIN, uint(11620L));

  public static final NodeId NamespaceMetadataType_StaticNodeIdTypes =
      new NodeId(UShort.MIN, uint(11621L));

  public static final NodeId NamespaceMetadataType_StaticNumericNodeIdRange =
      new NodeId(UShort.MIN, uint(11622L));

  public static final NodeId NamespaceMetadataType_StaticStringNodeIdPattern =
      new NodeId(UShort.MIN, uint(11623L));

  public static final NodeId NamespaceMetadataType_NamespaceFile =
      new NodeId(UShort.MIN, uint(11624L));

  public static final NodeId NamespaceMetadataType_NamespaceFile_Size =
      new NodeId(UShort.MIN, uint(11625L));

  public static final NodeId NamespaceMetadataType_NamespaceFile_OpenCount =
      new NodeId(UShort.MIN, uint(11628L));

  public static final NodeId NamespaceMetadataType_NamespaceFile_Open =
      new NodeId(UShort.MIN, uint(11629L));

  public static final NodeId NamespaceMetadataType_NamespaceFile_Open_InputArguments =
      new NodeId(UShort.MIN, uint(11630L));

  public static final NodeId NamespaceMetadataType_NamespaceFile_Open_OutputArguments =
      new NodeId(UShort.MIN, uint(11631L));

  public static final NodeId NamespaceMetadataType_NamespaceFile_Close =
      new NodeId(UShort.MIN, uint(11632L));

  public static final NodeId NamespaceMetadataType_NamespaceFile_Close_InputArguments =
      new NodeId(UShort.MIN, uint(11633L));

  public static final NodeId NamespaceMetadataType_NamespaceFile_Read =
      new NodeId(UShort.MIN, uint(11634L));

  public static final NodeId NamespaceMetadataType_NamespaceFile_Read_InputArguments =
      new NodeId(UShort.MIN, uint(11635L));

  public static final NodeId NamespaceMetadataType_NamespaceFile_Read_OutputArguments =
      new NodeId(UShort.MIN, uint(11636L));

  public static final NodeId NamespaceMetadataType_NamespaceFile_Write =
      new NodeId(UShort.MIN, uint(11637L));

  public static final NodeId NamespaceMetadataType_NamespaceFile_Write_InputArguments =
      new NodeId(UShort.MIN, uint(11638L));

  public static final NodeId NamespaceMetadataType_NamespaceFile_GetPosition =
      new NodeId(UShort.MIN, uint(11639L));

  public static final NodeId NamespaceMetadataType_NamespaceFile_GetPosition_InputArguments =
      new NodeId(UShort.MIN, uint(11640L));

  public static final NodeId NamespaceMetadataType_NamespaceFile_GetPosition_OutputArguments =
      new NodeId(UShort.MIN, uint(11641L));

  public static final NodeId NamespaceMetadataType_NamespaceFile_SetPosition =
      new NodeId(UShort.MIN, uint(11642L));

  public static final NodeId NamespaceMetadataType_NamespaceFile_SetPosition_InputArguments =
      new NodeId(UShort.MIN, uint(11643L));

  public static final NodeId NamespaceMetadataType_NamespaceFile_ExportNamespace =
      new NodeId(UShort.MIN, uint(11644L));

  public static final NodeId NamespacesType = new NodeId(UShort.MIN, uint(11645L));

  public static final NodeId NamespacesType_NamespaceIdentifier_Placeholder =
      new NodeId(UShort.MIN, uint(11646L));

  public static final NodeId NamespacesType_NamespaceIdentifier_Placeholder_NamespaceUri =
      new NodeId(UShort.MIN, uint(11647L));

  public static final NodeId NamespacesType_NamespaceIdentifier_Placeholder_NamespaceVersion =
      new NodeId(UShort.MIN, uint(11648L));

  public static final NodeId
      NamespacesType_NamespaceIdentifier_Placeholder_NamespacePublicationDate =
          new NodeId(UShort.MIN, uint(11649L));

  public static final NodeId NamespacesType_NamespaceIdentifier_Placeholder_IsNamespaceSubset =
      new NodeId(UShort.MIN, uint(11650L));

  public static final NodeId NamespacesType_NamespaceIdentifier_Placeholder_StaticNodeIdTypes =
      new NodeId(UShort.MIN, uint(11651L));

  public static final NodeId
      NamespacesType_NamespaceIdentifier_Placeholder_StaticNumericNodeIdRange =
          new NodeId(UShort.MIN, uint(11652L));

  public static final NodeId
      NamespacesType_NamespaceIdentifier_Placeholder_StaticStringNodeIdPattern =
          new NodeId(UShort.MIN, uint(11653L));

  public static final NodeId NamespacesType_NamespaceIdentifier_Placeholder_NamespaceFile =
      new NodeId(UShort.MIN, uint(11654L));

  public static final NodeId NamespacesType_NamespaceIdentifier_Placeholder_NamespaceFile_Size =
      new NodeId(UShort.MIN, uint(11655L));

  public static final NodeId
      NamespacesType_NamespaceIdentifier_Placeholder_NamespaceFile_OpenCount =
          new NodeId(UShort.MIN, uint(11658L));

  public static final NodeId NamespacesType_NamespaceIdentifier_Placeholder_NamespaceFile_Open =
      new NodeId(UShort.MIN, uint(11659L));

  public static final NodeId
      NamespacesType_NamespaceIdentifier_Placeholder_NamespaceFile_Open_InputArguments =
          new NodeId(UShort.MIN, uint(11660L));

  public static final NodeId
      NamespacesType_NamespaceIdentifier_Placeholder_NamespaceFile_Open_OutputArguments =
          new NodeId(UShort.MIN, uint(11661L));

  public static final NodeId NamespacesType_NamespaceIdentifier_Placeholder_NamespaceFile_Close =
      new NodeId(UShort.MIN, uint(11662L));

  public static final NodeId
      NamespacesType_NamespaceIdentifier_Placeholder_NamespaceFile_Close_InputArguments =
          new NodeId(UShort.MIN, uint(11663L));

  public static final NodeId NamespacesType_NamespaceIdentifier_Placeholder_NamespaceFile_Read =
      new NodeId(UShort.MIN, uint(11664L));

  public static final NodeId
      NamespacesType_NamespaceIdentifier_Placeholder_NamespaceFile_Read_InputArguments =
          new NodeId(UShort.MIN, uint(11665L));

  public static final NodeId
      NamespacesType_NamespaceIdentifier_Placeholder_NamespaceFile_Read_OutputArguments =
          new NodeId(UShort.MIN, uint(11666L));

  public static final NodeId NamespacesType_NamespaceIdentifier_Placeholder_NamespaceFile_Write =
      new NodeId(UShort.MIN, uint(11667L));

  public static final NodeId
      NamespacesType_NamespaceIdentifier_Placeholder_NamespaceFile_Write_InputArguments =
          new NodeId(UShort.MIN, uint(11668L));

  public static final NodeId
      NamespacesType_NamespaceIdentifier_Placeholder_NamespaceFile_GetPosition =
          new NodeId(UShort.MIN, uint(11669L));

  public static final NodeId
      NamespacesType_NamespaceIdentifier_Placeholder_NamespaceFile_GetPosition_InputArguments =
          new NodeId(UShort.MIN, uint(11670L));

  public static final NodeId
      NamespacesType_NamespaceIdentifier_Placeholder_NamespaceFile_GetPosition_OutputArguments =
          new NodeId(UShort.MIN, uint(11671L));

  public static final NodeId
      NamespacesType_NamespaceIdentifier_Placeholder_NamespaceFile_SetPosition =
          new NodeId(UShort.MIN, uint(11672L));

  public static final NodeId
      NamespacesType_NamespaceIdentifier_Placeholder_NamespaceFile_SetPosition_InputArguments =
          new NodeId(UShort.MIN, uint(11673L));

  public static final NodeId
      NamespacesType_NamespaceIdentifier_Placeholder_NamespaceFile_ExportNamespace =
          new NodeId(UShort.MIN, uint(11674L));

  public static final NodeId SystemStatusChangeEventType_SystemState =
      new NodeId(UShort.MIN, uint(11696L));

  public static final NodeId SamplingIntervalDiagnosticsType_SampledMonitoredItemsCount =
      new NodeId(UShort.MIN, uint(11697L));

  public static final NodeId SamplingIntervalDiagnosticsType_MaxSampledMonitoredItemsCount =
      new NodeId(UShort.MIN, uint(11698L));

  public static final NodeId SamplingIntervalDiagnosticsType_DisabledMonitoredItemsSamplingCount =
      new NodeId(UShort.MIN, uint(11699L));

  public static final NodeId OptionSetType_BitMask = new NodeId(UShort.MIN, uint(11701L));

  public static final NodeId Server_ServerCapabilities_MaxArrayLength =
      new NodeId(UShort.MIN, uint(11702L));

  public static final NodeId Server_ServerCapabilities_MaxStringLength =
      new NodeId(UShort.MIN, uint(11703L));

  public static final NodeId Server_ServerCapabilities_OperationLimits =
      new NodeId(UShort.MIN, uint(11704L));

  public static final NodeId Server_ServerCapabilities_OperationLimits_MaxNodesPerRead =
      new NodeId(UShort.MIN, uint(11705L));

  public static final NodeId Server_ServerCapabilities_OperationLimits_MaxNodesPerWrite =
      new NodeId(UShort.MIN, uint(11707L));

  public static final NodeId Server_ServerCapabilities_OperationLimits_MaxNodesPerMethodCall =
      new NodeId(UShort.MIN, uint(11709L));

  public static final NodeId Server_ServerCapabilities_OperationLimits_MaxNodesPerBrowse =
      new NodeId(UShort.MIN, uint(11710L));

  public static final NodeId Server_ServerCapabilities_OperationLimits_MaxNodesPerRegisterNodes =
      new NodeId(UShort.MIN, uint(11711L));

  public static final NodeId
      Server_ServerCapabilities_OperationLimits_MaxNodesPerTranslateBrowsePathsToNodeIds =
          new NodeId(UShort.MIN, uint(11712L));

  public static final NodeId Server_ServerCapabilities_OperationLimits_MaxNodesPerNodeManagement =
      new NodeId(UShort.MIN, uint(11713L));

  public static final NodeId Server_ServerCapabilities_OperationLimits_MaxMonitoredItemsPerCall =
      new NodeId(UShort.MIN, uint(11714L));

  public static final NodeId Server_Namespaces = new NodeId(UShort.MIN, uint(11715L));

  public static final NodeId BitFieldMaskDataType = new NodeId(UShort.MIN, uint(11737L));

  public static final NodeId OpenMethodType = new NodeId(UShort.MIN, uint(11738L));

  public static final NodeId OpenMethodType_InputArguments = new NodeId(UShort.MIN, uint(11739L));

  public static final NodeId OpenMethodType_OutputArguments = new NodeId(UShort.MIN, uint(11740L));

  public static final NodeId CloseMethodType = new NodeId(UShort.MIN, uint(11741L));

  public static final NodeId CloseMethodType_InputArguments = new NodeId(UShort.MIN, uint(11742L));

  public static final NodeId ReadMethodType = new NodeId(UShort.MIN, uint(11743L));

  public static final NodeId ReadMethodType_InputArguments = new NodeId(UShort.MIN, uint(11744L));

  public static final NodeId ReadMethodType_OutputArguments = new NodeId(UShort.MIN, uint(11745L));

  public static final NodeId WriteMethodType = new NodeId(UShort.MIN, uint(11746L));

  public static final NodeId WriteMethodType_InputArguments = new NodeId(UShort.MIN, uint(11747L));

  public static final NodeId GetPositionMethodType = new NodeId(UShort.MIN, uint(11748L));

  public static final NodeId GetPositionMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(11749L));

  public static final NodeId GetPositionMethodType_OutputArguments =
      new NodeId(UShort.MIN, uint(11750L));

  public static final NodeId SetPositionMethodType = new NodeId(UShort.MIN, uint(11751L));

  public static final NodeId SetPositionMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(11752L));

  public static final NodeId SystemOffNormalAlarmType = new NodeId(UShort.MIN, uint(11753L));

  public static final NodeId AuditConditionCommentEventType_Comment =
      new NodeId(UShort.MIN, uint(11851L));

  public static final NodeId AuditConditionRespondEventType_SelectedResponse =
      new NodeId(UShort.MIN, uint(11852L));

  public static final NodeId AuditConditionAcknowledgeEventType_Comment =
      new NodeId(UShort.MIN, uint(11853L));

  public static final NodeId AuditConditionConfirmEventType_Comment =
      new NodeId(UShort.MIN, uint(11854L));

  public static final NodeId AuditConditionShelvingEventType_ShelvingTime =
      new NodeId(UShort.MIN, uint(11855L));

  public static final NodeId AuditProgramTransitionEventType = new NodeId(UShort.MIN, uint(11856L));

  public static final NodeId AuditProgramTransitionEventType_TransitionNumber =
      new NodeId(UShort.MIN, uint(11875L));

  public static final NodeId HistoricalDataConfigurationType_AggregateFunctions =
      new NodeId(UShort.MIN, uint(11876L));

  public static final NodeId HAConfiguration_AggregateFunctions =
      new NodeId(UShort.MIN, uint(11877L));

  public static final NodeId NodeClass_EnumValues = new NodeId(UShort.MIN, uint(11878L));

  public static final NodeId InstanceNode = new NodeId(UShort.MIN, uint(11879L));

  public static final NodeId TypeNode = new NodeId(UShort.MIN, uint(11880L));

  public static final NodeId NodeAttributesMask_EnumValues = new NodeId(UShort.MIN, uint(11881L));

  public static final NodeId BrowseResultMask_EnumValues = new NodeId(UShort.MIN, uint(11883L));

  public static final NodeId HistoryUpdateType_EnumValues = new NodeId(UShort.MIN, uint(11884L));

  public static final NodeId PerformUpdateType_EnumValues = new NodeId(UShort.MIN, uint(11885L));

  public static final NodeId InstanceNode_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(11887L));

  public static final NodeId TypeNode_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(11888L));

  public static final NodeId InstanceNode_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(11889L));

  public static final NodeId TypeNode_Encoding_DefaultBinary = new NodeId(UShort.MIN, uint(11890L));

  public static final NodeId
      SessionDiagnosticsObjectType_SessionDiagnostics_UnauthorizedRequestCount =
          new NodeId(UShort.MIN, uint(11891L));

  public static final NodeId SessionDiagnosticsVariableType_UnauthorizedRequestCount =
      new NodeId(UShort.MIN, uint(11892L));

  public static final NodeId OpenFileMode = new NodeId(UShort.MIN, uint(11939L));

  public static final NodeId OpenFileMode_EnumValues = new NodeId(UShort.MIN, uint(11940L));

  public static final NodeId ModelChangeStructureVerbMask = new NodeId(UShort.MIN, uint(11941L));

  public static final NodeId ModelChangeStructureVerbMask_EnumValues =
      new NodeId(UShort.MIN, uint(11942L));

  public static final NodeId EndpointUrlListDataType = new NodeId(UShort.MIN, uint(11943L));

  public static final NodeId NetworkGroupDataType = new NodeId(UShort.MIN, uint(11944L));

  public static final NodeId NonTransparentNetworkRedundancyType =
      new NodeId(UShort.MIN, uint(11945L));

  public static final NodeId NonTransparentNetworkRedundancyType_ServerNetworkGroups =
      new NodeId(UShort.MIN, uint(11948L));

  public static final NodeId EndpointUrlListDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(11949L));

  public static final NodeId NetworkGroupDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(11950L));

  public static final NodeId OpcUa_XmlSchema_EndpointUrlListDataType =
      new NodeId(UShort.MIN, uint(11951L));

  public static final NodeId OpcUa_XmlSchema_EndpointUrlListDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(11952L));

  public static final NodeId OpcUa_XmlSchema_EndpointUrlListDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(11953L));

  public static final NodeId OpcUa_XmlSchema_NetworkGroupDataType =
      new NodeId(UShort.MIN, uint(11954L));

  public static final NodeId OpcUa_XmlSchema_NetworkGroupDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(11955L));

  public static final NodeId OpcUa_XmlSchema_NetworkGroupDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(11956L));

  public static final NodeId EndpointUrlListDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(11957L));

  public static final NodeId NetworkGroupDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(11958L));

  public static final NodeId OpcUa_BinarySchema_EndpointUrlListDataType =
      new NodeId(UShort.MIN, uint(11959L));

  public static final NodeId OpcUa_BinarySchema_EndpointUrlListDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(11960L));

  public static final NodeId OpcUa_BinarySchema_EndpointUrlListDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(11961L));

  public static final NodeId OpcUa_BinarySchema_NetworkGroupDataType =
      new NodeId(UShort.MIN, uint(11962L));

  public static final NodeId OpcUa_BinarySchema_NetworkGroupDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(11963L));

  public static final NodeId OpcUa_BinarySchema_NetworkGroupDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(11964L));

  public static final NodeId ArrayItemType = new NodeId(UShort.MIN, uint(12021L));

  public static final NodeId ArrayItemType_InstrumentRange = new NodeId(UShort.MIN, uint(12024L));

  public static final NodeId ArrayItemType_EURange = new NodeId(UShort.MIN, uint(12025L));

  public static final NodeId ArrayItemType_EngineeringUnits = new NodeId(UShort.MIN, uint(12026L));

  public static final NodeId ArrayItemType_Title = new NodeId(UShort.MIN, uint(12027L));

  public static final NodeId ArrayItemType_AxisScaleType = new NodeId(UShort.MIN, uint(12028L));

  public static final NodeId YArrayItemType = new NodeId(UShort.MIN, uint(12029L));

  public static final NodeId YArrayItemType_XAxisDefinition = new NodeId(UShort.MIN, uint(12037L));

  public static final NodeId XYArrayItemType = new NodeId(UShort.MIN, uint(12038L));

  public static final NodeId XYArrayItemType_XAxisDefinition = new NodeId(UShort.MIN, uint(12046L));

  public static final NodeId ImageItemType = new NodeId(UShort.MIN, uint(12047L));

  public static final NodeId ImageItemType_XAxisDefinition = new NodeId(UShort.MIN, uint(12055L));

  public static final NodeId ImageItemType_YAxisDefinition = new NodeId(UShort.MIN, uint(12056L));

  public static final NodeId CubeItemType = new NodeId(UShort.MIN, uint(12057L));

  public static final NodeId CubeItemType_XAxisDefinition = new NodeId(UShort.MIN, uint(12065L));

  public static final NodeId CubeItemType_YAxisDefinition = new NodeId(UShort.MIN, uint(12066L));

  public static final NodeId CubeItemType_ZAxisDefinition = new NodeId(UShort.MIN, uint(12067L));

  public static final NodeId NDimensionArrayItemType = new NodeId(UShort.MIN, uint(12068L));

  public static final NodeId NDimensionArrayItemType_AxisDefinition =
      new NodeId(UShort.MIN, uint(12076L));

  public static final NodeId AxisScaleEnumeration = new NodeId(UShort.MIN, uint(12077L));

  public static final NodeId AxisScaleEnumeration_EnumStrings =
      new NodeId(UShort.MIN, uint(12078L));

  public static final NodeId AxisInformation = new NodeId(UShort.MIN, uint(12079L));

  public static final NodeId XVType = new NodeId(UShort.MIN, uint(12080L));

  public static final NodeId AxisInformation_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(12081L));

  public static final NodeId XVType_Encoding_DefaultXml = new NodeId(UShort.MIN, uint(12082L));

  public static final NodeId OpcUa_XmlSchema_AxisInformation = new NodeId(UShort.MIN, uint(12083L));

  public static final NodeId OpcUa_XmlSchema_AxisInformation_DataTypeVersion =
      new NodeId(UShort.MIN, uint(12084L));

  public static final NodeId OpcUa_XmlSchema_AxisInformation_DictionaryFragment =
      new NodeId(UShort.MIN, uint(12085L));

  public static final NodeId OpcUa_XmlSchema_XVType = new NodeId(UShort.MIN, uint(12086L));

  public static final NodeId OpcUa_XmlSchema_XVType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(12087L));

  public static final NodeId OpcUa_XmlSchema_XVType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(12088L));

  public static final NodeId AxisInformation_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(12089L));

  public static final NodeId XVType_Encoding_DefaultBinary = new NodeId(UShort.MIN, uint(12090L));

  public static final NodeId OpcUa_BinarySchema_AxisInformation =
      new NodeId(UShort.MIN, uint(12091L));

  public static final NodeId OpcUa_BinarySchema_AxisInformation_DataTypeVersion =
      new NodeId(UShort.MIN, uint(12092L));

  public static final NodeId OpcUa_BinarySchema_AxisInformation_DictionaryFragment =
      new NodeId(UShort.MIN, uint(12093L));

  public static final NodeId OpcUa_BinarySchema_XVType = new NodeId(UShort.MIN, uint(12094L));

  public static final NodeId OpcUa_BinarySchema_XVType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(12095L));

  public static final NodeId OpcUa_BinarySchema_XVType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(12096L));

  public static final NodeId SessionsDiagnosticsSummaryType_ClientName_Placeholder =
      new NodeId(UShort.MIN, uint(12097L));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics =
          new NodeId(UShort.MIN, uint(12098L));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_SessionId =
          new NodeId(UShort.MIN, uint(12099L));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_SessionName =
          new NodeId(UShort.MIN, uint(12100L));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_ClientDescription =
          new NodeId(UShort.MIN, uint(12101L));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_ServerUri =
          new NodeId(UShort.MIN, uint(12102L));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_EndpointUrl =
          new NodeId(UShort.MIN, uint(12103L));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_LocaleIds =
          new NodeId(UShort.MIN, uint(12104L));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_ActualSessionTimeout =
          new NodeId(UShort.MIN, uint(12105L));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_MaxResponseMessageSize =
          new NodeId(UShort.MIN, uint(12106L));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_ClientConnectionTime =
          new NodeId(UShort.MIN, uint(12107L));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_ClientLastContactTime =
          new NodeId(UShort.MIN, uint(12108L));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_CurrentSubscriptionsCount =
          new NodeId(UShort.MIN, uint(12109L));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_CurrentMonitoredItemsCount =
          new NodeId(UShort.MIN, uint(12110L));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_CurrentPublishRequestsInQueue =
          new NodeId(UShort.MIN, uint(12111L));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_TotalRequestCount =
          new NodeId(UShort.MIN, uint(12112L));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_UnauthorizedRequestCount =
          new NodeId(UShort.MIN, uint(12113L));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_ReadCount =
          new NodeId(UShort.MIN, uint(12114L));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_HistoryReadCount =
          new NodeId(UShort.MIN, uint(12115L));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_WriteCount =
          new NodeId(UShort.MIN, uint(12116L));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_HistoryUpdateCount =
          new NodeId(UShort.MIN, uint(12117L));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_CallCount =
          new NodeId(UShort.MIN, uint(12118L));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_CreateMonitoredItemsCount =
          new NodeId(UShort.MIN, uint(12119L));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_ModifyMonitoredItemsCount =
          new NodeId(UShort.MIN, uint(12120L));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_SetMonitoringModeCount =
          new NodeId(UShort.MIN, uint(12121L));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_SetTriggeringCount =
          new NodeId(UShort.MIN, uint(12122L));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_DeleteMonitoredItemsCount =
          new NodeId(UShort.MIN, uint(12123L));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_CreateSubscriptionCount =
          new NodeId(UShort.MIN, uint(12124L));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_ModifySubscriptionCount =
          new NodeId(UShort.MIN, uint(12125L));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_SetPublishingModeCount =
          new NodeId(UShort.MIN, uint(12126L));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_PublishCount =
          new NodeId(UShort.MIN, uint(12127L));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_RepublishCount =
          new NodeId(UShort.MIN, uint(12128L));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_TransferSubscriptionsCount =
          new NodeId(UShort.MIN, uint(12129L));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_DeleteSubscriptionsCount =
          new NodeId(UShort.MIN, uint(12130L));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_AddNodesCount =
          new NodeId(UShort.MIN, uint(12131L));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_AddReferencesCount =
          new NodeId(UShort.MIN, uint(12132L));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_DeleteNodesCount =
          new NodeId(UShort.MIN, uint(12133L));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_DeleteReferencesCount =
          new NodeId(UShort.MIN, uint(12134L));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_BrowseCount =
          new NodeId(UShort.MIN, uint(12135L));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_BrowseNextCount =
          new NodeId(UShort.MIN, uint(12136L));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_TranslateBrowsePathsToNodeIdsCount =
          new NodeId(UShort.MIN, uint(12137L));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_QueryFirstCount =
          new NodeId(UShort.MIN, uint(12138L));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_QueryNextCount =
          new NodeId(UShort.MIN, uint(12139L));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_RegisterNodesCount =
          new NodeId(UShort.MIN, uint(12140L));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionDiagnostics_UnregisterNodesCount =
          new NodeId(UShort.MIN, uint(12141L));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionSecurityDiagnostics =
          new NodeId(UShort.MIN, uint(12142L));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionSecurityDiagnostics_SessionId =
          new NodeId(UShort.MIN, uint(12143L));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionSecurityDiagnostics_ClientUserIdOfSession =
          new NodeId(UShort.MIN, uint(12144L));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionSecurityDiagnostics_ClientUserIdHistory =
          new NodeId(UShort.MIN, uint(12145L));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionSecurityDiagnostics_AuthenticationMechanism =
          new NodeId(UShort.MIN, uint(12146L));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionSecurityDiagnostics_Encoding =
          new NodeId(UShort.MIN, uint(12147L));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionSecurityDiagnostics_TransportProtocol =
          new NodeId(UShort.MIN, uint(12148L));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionSecurityDiagnostics_SecurityMode =
          new NodeId(UShort.MIN, uint(12149L));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionSecurityDiagnostics_SecurityPolicyUri =
          new NodeId(UShort.MIN, uint(12150L));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SessionSecurityDiagnostics_ClientCertificate =
          new NodeId(UShort.MIN, uint(12151L));

  public static final NodeId
      SessionsDiagnosticsSummaryType_ClientName_Placeholder_SubscriptionDiagnosticsArray =
          new NodeId(UShort.MIN, uint(12152L));

  public static final NodeId
      ServerType_ServerCapabilities_OperationLimits_MaxNodesPerHistoryReadData =
          new NodeId(UShort.MIN, uint(12153L));

  public static final NodeId
      ServerType_ServerCapabilities_OperationLimits_MaxNodesPerHistoryReadEvents =
          new NodeId(UShort.MIN, uint(12154L));

  public static final NodeId
      ServerType_ServerCapabilities_OperationLimits_MaxNodesPerHistoryUpdateData =
          new NodeId(UShort.MIN, uint(12155L));

  public static final NodeId
      ServerType_ServerCapabilities_OperationLimits_MaxNodesPerHistoryUpdateEvents =
          new NodeId(UShort.MIN, uint(12156L));

  public static final NodeId ServerCapabilitiesType_OperationLimits_MaxNodesPerHistoryReadData =
      new NodeId(UShort.MIN, uint(12157L));

  public static final NodeId ServerCapabilitiesType_OperationLimits_MaxNodesPerHistoryReadEvents =
      new NodeId(UShort.MIN, uint(12158L));

  public static final NodeId ServerCapabilitiesType_OperationLimits_MaxNodesPerHistoryUpdateData =
      new NodeId(UShort.MIN, uint(12159L));

  public static final NodeId ServerCapabilitiesType_OperationLimits_MaxNodesPerHistoryUpdateEvents =
      new NodeId(UShort.MIN, uint(12160L));

  public static final NodeId OperationLimitsType_MaxNodesPerHistoryReadData =
      new NodeId(UShort.MIN, uint(12161L));

  public static final NodeId OperationLimitsType_MaxNodesPerHistoryReadEvents =
      new NodeId(UShort.MIN, uint(12162L));

  public static final NodeId OperationLimitsType_MaxNodesPerHistoryUpdateData =
      new NodeId(UShort.MIN, uint(12163L));

  public static final NodeId OperationLimitsType_MaxNodesPerHistoryUpdateEvents =
      new NodeId(UShort.MIN, uint(12164L));

  public static final NodeId Server_ServerCapabilities_OperationLimits_MaxNodesPerHistoryReadData =
      new NodeId(UShort.MIN, uint(12165L));

  public static final NodeId
      Server_ServerCapabilities_OperationLimits_MaxNodesPerHistoryReadEvents =
          new NodeId(UShort.MIN, uint(12166L));

  public static final NodeId
      Server_ServerCapabilities_OperationLimits_MaxNodesPerHistoryUpdateData =
          new NodeId(UShort.MIN, uint(12167L));

  public static final NodeId
      Server_ServerCapabilities_OperationLimits_MaxNodesPerHistoryUpdateEvents =
          new NodeId(UShort.MIN, uint(12168L));

  public static final NodeId NamingRuleType_EnumValues = new NodeId(UShort.MIN, uint(12169L));

  public static final NodeId ViewVersion = new NodeId(UShort.MIN, uint(12170L));

  public static final NodeId ComplexNumberType = new NodeId(UShort.MIN, uint(12171L));

  public static final NodeId DoubleComplexNumberType = new NodeId(UShort.MIN, uint(12172L));

  public static final NodeId ComplexNumberType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(12173L));

  public static final NodeId DoubleComplexNumberType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(12174L));

  public static final NodeId OpcUa_XmlSchema_ComplexNumberType =
      new NodeId(UShort.MIN, uint(12175L));

  public static final NodeId OpcUa_XmlSchema_ComplexNumberType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(12176L));

  public static final NodeId OpcUa_XmlSchema_ComplexNumberType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(12177L));

  public static final NodeId OpcUa_XmlSchema_DoubleComplexNumberType =
      new NodeId(UShort.MIN, uint(12178L));

  public static final NodeId OpcUa_XmlSchema_DoubleComplexNumberType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(12179L));

  public static final NodeId OpcUa_XmlSchema_DoubleComplexNumberType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(12180L));

  public static final NodeId ComplexNumberType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(12181L));

  public static final NodeId DoubleComplexNumberType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(12182L));

  public static final NodeId OpcUa_BinarySchema_ComplexNumberType =
      new NodeId(UShort.MIN, uint(12183L));

  public static final NodeId OpcUa_BinarySchema_ComplexNumberType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(12184L));

  public static final NodeId OpcUa_BinarySchema_ComplexNumberType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(12185L));

  public static final NodeId OpcUa_BinarySchema_DoubleComplexNumberType =
      new NodeId(UShort.MIN, uint(12186L));

  public static final NodeId OpcUa_BinarySchema_DoubleComplexNumberType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(12187L));

  public static final NodeId OpcUa_BinarySchema_DoubleComplexNumberType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(12188L));

  public static final NodeId ServerOnNetwork = new NodeId(UShort.MIN, uint(12189L));

  public static final NodeId FindServersOnNetworkRequest = new NodeId(UShort.MIN, uint(12190L));

  public static final NodeId FindServersOnNetworkResponse = new NodeId(UShort.MIN, uint(12191L));

  public static final NodeId RegisterServer2Request = new NodeId(UShort.MIN, uint(12193L));

  public static final NodeId RegisterServer2Response = new NodeId(UShort.MIN, uint(12194L));

  public static final NodeId ServerOnNetwork_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(12195L));

  public static final NodeId FindServersOnNetworkRequest_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(12196L));

  public static final NodeId FindServersOnNetworkResponse_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(12197L));

  public static final NodeId RegisterServer2Request_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(12199L));

  public static final NodeId RegisterServer2Response_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(12200L));

  public static final NodeId OpcUa_XmlSchema_ServerOnNetwork = new NodeId(UShort.MIN, uint(12201L));

  public static final NodeId OpcUa_XmlSchema_ServerOnNetwork_DataTypeVersion =
      new NodeId(UShort.MIN, uint(12202L));

  public static final NodeId OpcUa_XmlSchema_ServerOnNetwork_DictionaryFragment =
      new NodeId(UShort.MIN, uint(12203L));

  public static final NodeId ServerOnNetwork_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(12207L));

  public static final NodeId FindServersOnNetworkRequest_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(12208L));

  public static final NodeId FindServersOnNetworkResponse_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(12209L));

  public static final NodeId RegisterServer2Request_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(12211L));

  public static final NodeId RegisterServer2Response_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(12212L));

  public static final NodeId OpcUa_BinarySchema_ServerOnNetwork =
      new NodeId(UShort.MIN, uint(12213L));

  public static final NodeId OpcUa_BinarySchema_ServerOnNetwork_DataTypeVersion =
      new NodeId(UShort.MIN, uint(12214L));

  public static final NodeId OpcUa_BinarySchema_ServerOnNetwork_DictionaryFragment =
      new NodeId(UShort.MIN, uint(12215L));

  public static final NodeId ProgressEventType_Context = new NodeId(UShort.MIN, uint(12502L));

  public static final NodeId ProgressEventType_Progress = new NodeId(UShort.MIN, uint(12503L));

  public static final NodeId OpenWithMasksMethodType = new NodeId(UShort.MIN, uint(12513L));

  public static final NodeId OpenWithMasksMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(12514L));

  public static final NodeId OpenWithMasksMethodType_OutputArguments =
      new NodeId(UShort.MIN, uint(12515L));

  public static final NodeId CloseAndUpdateMethodType = new NodeId(UShort.MIN, uint(12516L));

  public static final NodeId CloseAndUpdateMethodType_OutputArguments =
      new NodeId(UShort.MIN, uint(12517L));

  public static final NodeId AddCertificateMethodType = new NodeId(UShort.MIN, uint(12518L));

  public static final NodeId AddCertificateMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(12519L));

  public static final NodeId RemoveCertificateMethodType = new NodeId(UShort.MIN, uint(12520L));

  public static final NodeId RemoveCertificateMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(12521L));

  public static final NodeId TrustListType = new NodeId(UShort.MIN, uint(12522L));

  public static final NodeId TrustListType_LastUpdateTime = new NodeId(UShort.MIN, uint(12542L));

  public static final NodeId TrustListType_OpenWithMasks = new NodeId(UShort.MIN, uint(12543L));

  public static final NodeId TrustListType_OpenWithMasks_InputArguments =
      new NodeId(UShort.MIN, uint(12544L));

  public static final NodeId TrustListType_OpenWithMasks_OutputArguments =
      new NodeId(UShort.MIN, uint(12545L));

  public static final NodeId TrustListType_CloseAndUpdate = new NodeId(UShort.MIN, uint(12546L));

  public static final NodeId TrustListType_CloseAndUpdate_OutputArguments =
      new NodeId(UShort.MIN, uint(12547L));

  public static final NodeId TrustListType_AddCertificate = new NodeId(UShort.MIN, uint(12548L));

  public static final NodeId TrustListType_AddCertificate_InputArguments =
      new NodeId(UShort.MIN, uint(12549L));

  public static final NodeId TrustListType_RemoveCertificate = new NodeId(UShort.MIN, uint(12550L));

  public static final NodeId TrustListType_RemoveCertificate_InputArguments =
      new NodeId(UShort.MIN, uint(12551L));

  public static final NodeId TrustListMasks = new NodeId(UShort.MIN, uint(12552L));

  public static final NodeId TrustListMasks_EnumValues = new NodeId(UShort.MIN, uint(12553L));

  public static final NodeId TrustListDataType = new NodeId(UShort.MIN, uint(12554L));

  public static final NodeId CertificateGroupType = new NodeId(UShort.MIN, uint(12555L));

  public static final NodeId CertificateType = new NodeId(UShort.MIN, uint(12556L));

  public static final NodeId ApplicationCertificateType = new NodeId(UShort.MIN, uint(12557L));

  public static final NodeId HttpsCertificateType = new NodeId(UShort.MIN, uint(12558L));

  public static final NodeId RsaMinApplicationCertificateType =
      new NodeId(UShort.MIN, uint(12559L));

  public static final NodeId RsaSha256ApplicationCertificateType =
      new NodeId(UShort.MIN, uint(12560L));

  public static final NodeId TrustListUpdatedAuditEventType = new NodeId(UShort.MIN, uint(12561L));

  public static final NodeId UpdateCertificateMethodType = new NodeId(UShort.MIN, uint(12578L));

  public static final NodeId UpdateCertificateMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(12579L));

  public static final NodeId UpdateCertificateMethodType_OutputArguments =
      new NodeId(UShort.MIN, uint(12580L));

  public static final NodeId ServerConfigurationType = new NodeId(UShort.MIN, uint(12581L));

  public static final NodeId ServerConfigurationType_SupportedPrivateKeyFormats =
      new NodeId(UShort.MIN, uint(12583L));

  public static final NodeId ServerConfigurationType_MaxTrustListSize =
      new NodeId(UShort.MIN, uint(12584L));

  public static final NodeId ServerConfigurationType_MulticastDnsEnabled =
      new NodeId(UShort.MIN, uint(12585L));

  public static final NodeId ServerConfigurationType_UpdateCertificate =
      new NodeId(UShort.MIN, uint(12616L));

  public static final NodeId ServerConfigurationType_UpdateCertificate_InputArguments =
      new NodeId(UShort.MIN, uint(12617L));

  public static final NodeId ServerConfigurationType_UpdateCertificate_OutputArguments =
      new NodeId(UShort.MIN, uint(12618L));

  public static final NodeId CertificateUpdatedAuditEventType =
      new NodeId(UShort.MIN, uint(12620L));

  public static final NodeId ServerConfiguration = new NodeId(UShort.MIN, uint(12637L));

  public static final NodeId ServerConfiguration_SupportedPrivateKeyFormats =
      new NodeId(UShort.MIN, uint(12639L));

  public static final NodeId ServerConfiguration_MaxTrustListSize =
      new NodeId(UShort.MIN, uint(12640L));

  public static final NodeId ServerConfiguration_MulticastDnsEnabled =
      new NodeId(UShort.MIN, uint(12641L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList =
          new NodeId(UShort.MIN, uint(12642L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_Size =
          new NodeId(UShort.MIN, uint(12643L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_OpenCount =
          new NodeId(UShort.MIN, uint(12646L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_Open =
          new NodeId(UShort.MIN, uint(12647L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_Open_InputArguments =
          new NodeId(UShort.MIN, uint(12648L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_Open_OutputArguments =
          new NodeId(UShort.MIN, uint(12649L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_Close =
          new NodeId(UShort.MIN, uint(12650L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_Close_InputArguments =
          new NodeId(UShort.MIN, uint(12651L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_Read =
          new NodeId(UShort.MIN, uint(12652L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_Read_InputArguments =
          new NodeId(UShort.MIN, uint(12653L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_Read_OutputArguments =
          new NodeId(UShort.MIN, uint(12654L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_Write =
          new NodeId(UShort.MIN, uint(12655L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_Write_InputArguments =
          new NodeId(UShort.MIN, uint(12656L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_GetPosition =
          new NodeId(UShort.MIN, uint(12657L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_GetPosition_InputArguments =
          new NodeId(UShort.MIN, uint(12658L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_GetPosition_OutputArguments =
          new NodeId(UShort.MIN, uint(12659L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_SetPosition =
          new NodeId(UShort.MIN, uint(12660L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_SetPosition_InputArguments =
          new NodeId(UShort.MIN, uint(12661L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_LastUpdateTime =
          new NodeId(UShort.MIN, uint(12662L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_OpenWithMasks =
          new NodeId(UShort.MIN, uint(12663L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_OpenWithMasks_InputArguments =
          new NodeId(UShort.MIN, uint(12664L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_OpenWithMasks_OutputArguments =
          new NodeId(UShort.MIN, uint(12665L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_CloseAndUpdate =
          new NodeId(UShort.MIN, uint(12666L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_CloseAndUpdate_OutputArguments =
          new NodeId(UShort.MIN, uint(12667L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_AddCertificate =
          new NodeId(UShort.MIN, uint(12668L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_AddCertificate_InputArguments =
          new NodeId(UShort.MIN, uint(12669L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_RemoveCertificate =
          new NodeId(UShort.MIN, uint(12670L));

  public static final NodeId
      ServerConfiguration_CertificateGroups_DefaultApplicationGroup_TrustList_RemoveCertificate_InputArguments =
          new NodeId(UShort.MIN, uint(12671L));

  public static final NodeId TrustListDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(12676L));

  public static final NodeId OpcUa_XmlSchema_TrustListDataType =
      new NodeId(UShort.MIN, uint(12677L));

  public static final NodeId OpcUa_XmlSchema_TrustListDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(12678L));

  public static final NodeId OpcUa_XmlSchema_TrustListDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(12679L));

  public static final NodeId TrustListDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(12680L));

  public static final NodeId OpcUa_BinarySchema_TrustListDataType =
      new NodeId(UShort.MIN, uint(12681L));

  public static final NodeId OpcUa_BinarySchema_TrustListDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(12682L));

  public static final NodeId OpcUa_BinarySchema_TrustListDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(12683L));

  public static final NodeId FileType_Writable = new NodeId(UShort.MIN, uint(12686L));

  public static final NodeId FileType_UserWritable = new NodeId(UShort.MIN, uint(12687L));

  public static final NodeId NamespaceMetadataType_NamespaceFile_Writable =
      new NodeId(UShort.MIN, uint(12690L));

  public static final NodeId NamespaceMetadataType_NamespaceFile_UserWritable =
      new NodeId(UShort.MIN, uint(12691L));

  public static final NodeId NamespacesType_NamespaceIdentifier_Placeholder_NamespaceFile_Writable =
      new NodeId(UShort.MIN, uint(12692L));

  public static final NodeId
      NamespacesType_NamespaceIdentifier_Placeholder_NamespaceFile_UserWritable =
          new NodeId(UShort.MIN, uint(12693L));

  public static final NodeId CloseAndUpdateMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(12704L));

  public static final NodeId TrustListType_CloseAndUpdate_InputArguments =
      new NodeId(UShort.MIN, uint(12705L));

  public static final NodeId ServerConfigurationType_ServerCapabilities =
      new NodeId(UShort.MIN, uint(12708L));

  public static final NodeId ServerConfiguration_ServerCapabilities =
      new NodeId(UShort.MIN, uint(12710L));

  public static final NodeId OpcUa_XmlSchema_RelativePathElement =
      new NodeId(UShort.MIN, uint(12712L));

  public static final NodeId OpcUa_XmlSchema_RelativePathElement_DataTypeVersion =
      new NodeId(UShort.MIN, uint(12713L));

  public static final NodeId OpcUa_XmlSchema_RelativePathElement_DictionaryFragment =
      new NodeId(UShort.MIN, uint(12714L));

  public static final NodeId OpcUa_XmlSchema_RelativePath = new NodeId(UShort.MIN, uint(12715L));

  public static final NodeId OpcUa_XmlSchema_RelativePath_DataTypeVersion =
      new NodeId(UShort.MIN, uint(12716L));

  public static final NodeId OpcUa_XmlSchema_RelativePath_DictionaryFragment =
      new NodeId(UShort.MIN, uint(12717L));

  public static final NodeId OpcUa_BinarySchema_RelativePathElement =
      new NodeId(UShort.MIN, uint(12718L));

  public static final NodeId OpcUa_BinarySchema_RelativePathElement_DataTypeVersion =
      new NodeId(UShort.MIN, uint(12719L));

  public static final NodeId OpcUa_BinarySchema_RelativePathElement_DictionaryFragment =
      new NodeId(UShort.MIN, uint(12720L));

  public static final NodeId OpcUa_BinarySchema_RelativePath = new NodeId(UShort.MIN, uint(12721L));

  public static final NodeId OpcUa_BinarySchema_RelativePath_DataTypeVersion =
      new NodeId(UShort.MIN, uint(12722L));

  public static final NodeId OpcUa_BinarySchema_RelativePath_DictionaryFragment =
      new NodeId(UShort.MIN, uint(12723L));
}

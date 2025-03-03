/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.namespaces.loader;

import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.server.NodeManager;
import org.eclipse.milo.opcua.sdk.server.nodes.UaDataTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ActionState;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.AxisScaleEnumeration;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrokerTransportQualityOfService;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ChassisIdSubtype;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ConversionLimitEnum;
import org.eclipse.milo.opcua.stack.core.types.enumerated.DataSetOrderingType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.DiagnosticsLevel;
import org.eclipse.milo.opcua.stack.core.types.enumerated.Duplex;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ExceptionDeviationFormat;
import org.eclipse.milo.opcua.stack.core.types.enumerated.FilterOperator;
import org.eclipse.milo.opcua.stack.core.types.enumerated.HistoryUpdateType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.IdType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.IdentityCriteriaType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.InterfaceAdminStatus;
import org.eclipse.milo.opcua.stack.core.types.enumerated.InterfaceOperStatus;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ManAddrIfSubtype;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NamingRuleType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NegotiationStatus;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeAttributesMask;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.enumerated.OpenFileMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.OverrideValueHandling;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PerformUpdateType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PortIdSubtype;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubDiagnosticsCounterClassification;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;
import org.eclipse.milo.opcua.stack.core.types.enumerated.RedundancySupport;
import org.eclipse.milo.opcua.stack.core.types.enumerated.RedundantServerMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.SecurityTokenRequestType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ServerState;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TrustListMasks;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TsnFailureCode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TsnListenerStatus;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TsnStreamState;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TsnTalkerStatus;
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.ActionMethodDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.ActionTargetDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.AddNodesItem;
import org.eclipse.milo.opcua.stack.core.types.structured.AddReferencesItem;
import org.eclipse.milo.opcua.stack.core.types.structured.AdditionalParametersType;
import org.eclipse.milo.opcua.stack.core.types.structured.AggregateConfiguration;
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
import org.eclipse.milo.opcua.stack.core.types.structured.BuildInfo;
import org.eclipse.milo.opcua.stack.core.types.structured.CartesianCoordinates;
import org.eclipse.milo.opcua.stack.core.types.structured.ComplexNumberType;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.ConnectionTransportDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilterElement;
import org.eclipse.milo.opcua.stack.core.types.structured.CurrencyUnitType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetReaderDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetReaderMessageDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetReaderTransportDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetWriterDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetWriterMessageDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetWriterTransportDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataTypeDefinition;
import org.eclipse.milo.opcua.stack.core.types.structured.DataTypeDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.DataTypeSchemaHeader;
import org.eclipse.milo.opcua.stack.core.types.structured.DatagramConnectionTransport2DataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DatagramConnectionTransportDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DatagramDataSetReaderTransportDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DatagramWriterGroupTransport2DataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DatagramWriterGroupTransportDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteNodesItem;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteReferencesItem;
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
import org.eclipse.milo.opcua.stack.core.types.structured.EventFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.FieldMetaData;
import org.eclipse.milo.opcua.stack.core.types.structured.FieldTargetDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.FilterOperand;
import org.eclipse.milo.opcua.stack.core.types.structured.Frame;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryEvent;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryEventFieldList;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryModifiedEvent;
import org.eclipse.milo.opcua.stack.core.types.structured.IdentityMappingRuleType;
import org.eclipse.milo.opcua.stack.core.types.structured.IssuedIdentityToken;
import org.eclipse.milo.opcua.stack.core.types.structured.JsonDataSetReaderMessageDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.JsonDataSetWriterMessageDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.JsonWriterGroupMessageDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.KeyValuePair;
import org.eclipse.milo.opcua.stack.core.types.structured.LinearConversionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.LiteralOperand;
import org.eclipse.milo.opcua.stack.core.types.structured.LldpManagementAddressTxPortType;
import org.eclipse.milo.opcua.stack.core.types.structured.LldpManagementAddressType;
import org.eclipse.milo.opcua.stack.core.types.structured.LldpTlvType;
import org.eclipse.milo.opcua.stack.core.types.structured.MdnsDiscoveryConfiguration;
import org.eclipse.milo.opcua.stack.core.types.structured.ModelChangeStructureDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.ModificationInfo;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoringFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.NetworkAddressDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.NetworkAddressUrlDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.NetworkGroupDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.OptionSet;
import org.eclipse.milo.opcua.stack.core.types.structured.Orientation;
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
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubGroupDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubKeyPushTargetDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishedActionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishedActionMethodDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishedDataItemsDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishedDataSetCustomSourceDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishedDataSetDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishedDataSetSourceDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishedEventsDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishedVariableDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.QosDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.QuantityDimension;
import org.eclipse.milo.opcua.stack.core.types.structured.Range;
import org.eclipse.milo.opcua.stack.core.types.structured.RationalNumber;
import org.eclipse.milo.opcua.stack.core.types.structured.ReaderGroupDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.ReaderGroupMessageDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.ReaderGroupTransportDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.ReceiveQosDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.ReceiveQosPriorityDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.RedundantServerDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.ReferenceDescriptionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.ReferenceListEntryDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.RegisteredServer;
import org.eclipse.milo.opcua.stack.core.types.structured.RelativePath;
import org.eclipse.milo.opcua.stack.core.types.structured.RelativePathElement;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.SamplingIntervalDiagnosticsDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.SecurityGroupDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.SemanticChangeStructureDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.ServerDiagnosticsSummaryDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.ServerOnNetwork;
import org.eclipse.milo.opcua.stack.core.types.structured.ServerStatusDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.ServiceCounterDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.SessionDiagnosticsDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.SessionSecurityDiagnosticsDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.SignedSoftwareCertificate;
import org.eclipse.milo.opcua.stack.core.types.structured.SimpleAttributeOperand;
import org.eclipse.milo.opcua.stack.core.types.structured.SimpleTypeDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.StandaloneSubscribedDataSetDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.StandaloneSubscribedDataSetRefDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.StatusResult;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureDefinition;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureField;
import org.eclipse.milo.opcua.stack.core.types.structured.SubscribedDataSetDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.SubscribedDataSetMirrorDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.SubscriptionDiagnosticsDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.TargetVariablesDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.ThreeDCartesianCoordinates;
import org.eclipse.milo.opcua.stack.core.types.structured.ThreeDFrame;
import org.eclipse.milo.opcua.stack.core.types.structured.ThreeDOrientation;
import org.eclipse.milo.opcua.stack.core.types.structured.ThreeDVector;
import org.eclipse.milo.opcua.stack.core.types.structured.TimeZoneDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.TransactionErrorType;
import org.eclipse.milo.opcua.stack.core.types.structured.TransmitQosDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.TransmitQosPriorityDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.TrustListDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.UABinaryFileDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpDataSetReaderMessageDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpDataSetWriterMessageDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpWriterGroupMessageDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.Union;
import org.eclipse.milo.opcua.stack.core.types.structured.UnsignedRationalNumber;
import org.eclipse.milo.opcua.stack.core.types.structured.UserIdentityToken;
import org.eclipse.milo.opcua.stack.core.types.structured.UserManagementDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.UserNameIdentityToken;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.core.types.structured.Vector;
import org.eclipse.milo.opcua.stack.core.types.structured.WriterGroupDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.WriterGroupMessageDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.WriterGroupTransportDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.X509IdentityToken;
import org.eclipse.milo.opcua.stack.core.types.structured.XVType;

class DataTypeNodeLoader {
  private final UaNodeContext context;

  private final NodeManager<UaNode> nodeManager;

  DataTypeNodeLoader(UaNodeContext context, NodeManager<UaNode> nodeManager) {
    this.context = context;
    this.nodeManager = nodeManager;
  }

  void loadNode0() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 24),
            new QualifiedName(0, "BaseDataType"),
            new LocalizedText("", "BaseDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            null);
    this.nodeManager.addNode(node);
  }

  void loadNode1() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 26),
            new QualifiedName(0, "Number"),
            new LocalizedText("", "Number"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            null);
    node.addReference(
        new Reference(new NodeId(0, 26), new NodeId(0, 45), new NodeId(0, 24).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode2() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 27),
            new QualifiedName(0, "Integer"),
            new LocalizedText("", "Integer"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            null);
    node.addReference(
        new Reference(new NodeId(0, 27), new NodeId(0, 45), new NodeId(0, 26).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode3() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 28),
            new QualifiedName(0, "UInteger"),
            new LocalizedText("", "UInteger"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            null);
    node.addReference(
        new Reference(new NodeId(0, 28), new NodeId(0, 45), new NodeId(0, 26).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode4() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 29),
            new QualifiedName(0, "Enumeration"),
            new LocalizedText("", "Enumeration"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            null);
    node.addReference(
        new Reference(new NodeId(0, 29), new NodeId(0, 45), new NodeId(0, 24).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode5() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 1),
            new QualifiedName(0, "Boolean"),
            new LocalizedText("", "Boolean"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            null);
    node.addReference(
        new Reference(new NodeId(0, 1), new NodeId(0, 45), new NodeId(0, 24).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode6() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 2),
            new QualifiedName(0, "SByte"),
            new LocalizedText("", "SByte"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            null);
    node.addReference(
        new Reference(new NodeId(0, 2), new NodeId(0, 45), new NodeId(0, 27).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode7() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 3),
            new QualifiedName(0, "Byte"),
            new LocalizedText("", "Byte"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            null);
    node.addReference(
        new Reference(new NodeId(0, 3), new NodeId(0, 45), new NodeId(0, 28).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode8() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 4),
            new QualifiedName(0, "Int16"),
            new LocalizedText("", "Int16"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            null);
    node.addReference(
        new Reference(new NodeId(0, 4), new NodeId(0, 45), new NodeId(0, 27).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode9() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 5),
            new QualifiedName(0, "UInt16"),
            new LocalizedText("", "UInt16"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            null);
    node.addReference(
        new Reference(new NodeId(0, 5), new NodeId(0, 45), new NodeId(0, 28).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode10() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 6),
            new QualifiedName(0, "Int32"),
            new LocalizedText("", "Int32"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            null);
    node.addReference(
        new Reference(new NodeId(0, 6), new NodeId(0, 45), new NodeId(0, 27).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode11() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 7),
            new QualifiedName(0, "UInt32"),
            new LocalizedText("", "UInt32"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            null);
    node.addReference(
        new Reference(new NodeId(0, 7), new NodeId(0, 45), new NodeId(0, 28).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode12() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 8),
            new QualifiedName(0, "Int64"),
            new LocalizedText("", "Int64"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            null);
    node.addReference(
        new Reference(new NodeId(0, 8), new NodeId(0, 45), new NodeId(0, 27).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode13() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 9),
            new QualifiedName(0, "UInt64"),
            new LocalizedText("", "UInt64"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            null);
    node.addReference(
        new Reference(new NodeId(0, 9), new NodeId(0, 45), new NodeId(0, 28).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode14() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 10),
            new QualifiedName(0, "Float"),
            new LocalizedText("", "Float"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            null);
    node.addReference(
        new Reference(new NodeId(0, 10), new NodeId(0, 45), new NodeId(0, 26).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode15() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 11),
            new QualifiedName(0, "Double"),
            new LocalizedText("", "Double"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            null);
    node.addReference(
        new Reference(new NodeId(0, 11), new NodeId(0, 45), new NodeId(0, 26).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode16() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 12),
            new QualifiedName(0, "String"),
            new LocalizedText("", "String"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            null);
    node.addReference(
        new Reference(new NodeId(0, 12), new NodeId(0, 45), new NodeId(0, 24).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode17() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 13),
            new QualifiedName(0, "DateTime"),
            new LocalizedText("", "DateTime"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            null);
    node.addReference(
        new Reference(new NodeId(0, 13), new NodeId(0, 45), new NodeId(0, 24).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode18() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 14),
            new QualifiedName(0, "Guid"),
            new LocalizedText("", "Guid"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            null);
    node.addReference(
        new Reference(new NodeId(0, 14), new NodeId(0, 45), new NodeId(0, 24).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode19() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 15),
            new QualifiedName(0, "ByteString"),
            new LocalizedText("", "ByteString"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            null);
    node.addReference(
        new Reference(new NodeId(0, 15), new NodeId(0, 45), new NodeId(0, 24).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode20() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 16),
            new QualifiedName(0, "XmlElement"),
            new LocalizedText("", "XmlElement"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            null);
    node.addReference(
        new Reference(new NodeId(0, 16), new NodeId(0, 45), new NodeId(0, 24).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode21() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 17),
            new QualifiedName(0, "NodeId"),
            new LocalizedText("", "NodeId"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            null);
    node.addReference(
        new Reference(new NodeId(0, 17), new NodeId(0, 45), new NodeId(0, 24).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode22() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 18),
            new QualifiedName(0, "ExpandedNodeId"),
            new LocalizedText("", "ExpandedNodeId"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            null);
    node.addReference(
        new Reference(new NodeId(0, 18), new NodeId(0, 45), new NodeId(0, 24).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode23() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 19),
            new QualifiedName(0, "StatusCode"),
            new LocalizedText("", "StatusCode"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            null);
    node.addReference(
        new Reference(new NodeId(0, 19), new NodeId(0, 45), new NodeId(0, 24).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode24() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 20),
            new QualifiedName(0, "QualifiedName"),
            new LocalizedText("", "QualifiedName"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            null);
    node.addReference(
        new Reference(new NodeId(0, 20), new NodeId(0, 45), new NodeId(0, 24).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode25() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 21),
            new QualifiedName(0, "LocalizedText"),
            new LocalizedText("", "LocalizedText"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            null);
    node.addReference(
        new Reference(new NodeId(0, 21), new NodeId(0, 45), new NodeId(0, 24).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode26() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 22),
            new QualifiedName(0, "Structure"),
            new LocalizedText("", "Structure"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            null);
    node.addReference(
        new Reference(new NodeId(0, 22), new NodeId(0, 45), new NodeId(0, 24).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode27() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 23),
            new QualifiedName(0, "DataValue"),
            new LocalizedText("", "DataValue"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            null);
    node.addReference(
        new Reference(new NodeId(0, 23), new NodeId(0, 45), new NodeId(0, 24).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode28() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 25),
            new QualifiedName(0, "DiagnosticInfo"),
            new LocalizedText("", "DiagnosticInfo"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            null);
    node.addReference(
        new Reference(new NodeId(0, 25), new NodeId(0, 45), new NodeId(0, 24).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode29() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 30),
            new QualifiedName(0, "Image"),
            new LocalizedText("", "Image"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            null);
    node.addReference(
        new Reference(new NodeId(0, 30), new NodeId(0, 45), new NodeId(0, 15).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode30() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 50),
            new QualifiedName(0, "Decimal"),
            new LocalizedText("", "Decimal"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            null);
    node.addReference(
        new Reference(new NodeId(0, 50), new NodeId(0, 45), new NodeId(0, 26).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode31() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 120),
            new QualifiedName(0, "NamingRuleType"),
            new LocalizedText("", "NamingRuleType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            NamingRuleType.definition());
    node.addReference(
        new Reference(
            new NodeId(0, 120), new NodeId(0, 46), new NodeId(0, 12169).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 120), new NodeId(0, 45), new NodeId(0, 29).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode32() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 2000),
            new QualifiedName(0, "ImageBMP"),
            new LocalizedText("", "ImageBMP"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            null);
    node.addReference(
        new Reference(new NodeId(0, 2000), new NodeId(0, 45), new NodeId(0, 30).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode33() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 2001),
            new QualifiedName(0, "ImageGIF"),
            new LocalizedText("", "ImageGIF"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            null);
    node.addReference(
        new Reference(new NodeId(0, 2001), new NodeId(0, 45), new NodeId(0, 30).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode34() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 2002),
            new QualifiedName(0, "ImageJPG"),
            new LocalizedText("", "ImageJPG"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            null);
    node.addReference(
        new Reference(new NodeId(0, 2002), new NodeId(0, 45), new NodeId(0, 30).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode35() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 2003),
            new QualifiedName(0, "ImagePNG"),
            new LocalizedText("", "ImagePNG"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            null);
    node.addReference(
        new Reference(new NodeId(0, 2003), new NodeId(0, 45), new NodeId(0, 30).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode36() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 16307),
            new QualifiedName(0, "AudioDataType"),
            new LocalizedText("", "AudioDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            null);
    node.addReference(
        new Reference(
            new NodeId(0, 16307), new NodeId(0, 45), new NodeId(0, 15).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode37() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 12756),
            new QualifiedName(0, "Union"),
            new LocalizedText("", "Union"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            Union.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 12756), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode38() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 23751),
            new QualifiedName(0, "UriString"),
            new LocalizedText("", "UriString"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            null);
    node.addReference(
        new Reference(
            new NodeId(0, 23751), new NodeId(0, 45), new NodeId(0, 12).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode39() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 32417),
            new QualifiedName(0, "RedundantServerMode"),
            new LocalizedText("", "RedundantServerMode"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            RedundantServerMode.definition());
    node.addReference(
        new Reference(
            new NodeId(0, 32417), new NodeId(0, 46), new NodeId(0, 32418).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32417), new NodeId(0, 45), new NodeId(0, 29).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode40() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 11737),
            new QualifiedName(0, "BitFieldMaskDataType"),
            new LocalizedText("", "BitFieldMaskDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            null);
    node.addReference(
        new Reference(new NodeId(0, 11737), new NodeId(0, 45), new NodeId(0, 9).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode41() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 24263),
            new QualifiedName(0, "SemanticVersionString"),
            new LocalizedText("", "SemanticVersionString"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            null);
    node.addReference(
        new Reference(
            new NodeId(0, 24263), new NodeId(0, 45), new NodeId(0, 12).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode42() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 14533),
            new QualifiedName(0, "KeyValuePair"),
            new LocalizedText("", "KeyValuePair"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            KeyValuePair.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 14533), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode43() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 16313),
            new QualifiedName(0, "AdditionalParametersType"),
            new LocalizedText("", "AdditionalParametersType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            AdditionalParametersType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 16313), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode44() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 17548),
            new QualifiedName(0, "EphemeralKeyType"),
            new LocalizedText("", "EphemeralKeyType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            EphemeralKeyType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 17548), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode45() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 15528),
            new QualifiedName(0, "EndpointType"),
            new LocalizedText("", "EndpointType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            EndpointType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 15528), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode46() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 31917),
            new QualifiedName(0, "Handle"),
            new LocalizedText("", "Handle"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            null);
    node.addReference(
        new Reference(new NodeId(0, 31917), new NodeId(0, 45), new NodeId(0, 7).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode47() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 31918),
            new QualifiedName(0, "TrimmedString"),
            new LocalizedText("", "TrimmedString"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            null);
    node.addReference(
        new Reference(
            new NodeId(0, 31918), new NodeId(0, 45), new NodeId(0, 12).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode48() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 32421),
            new QualifiedName(0, "BitFieldDefinition"),
            new LocalizedText("", "BitFieldDefinition"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            BitFieldDefinition.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 32421), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode49() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 18806),
            new QualifiedName(0, "RationalNumber"),
            new LocalizedText("", "RationalNumber"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            RationalNumber.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 18806), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode50() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 18807),
            new QualifiedName(0, "Vector"),
            new LocalizedText("", "Vector"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            Vector.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 18807), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode51() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 18808),
            new QualifiedName(0, "ThreeDVector"),
            new LocalizedText("", "3DVector"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            ThreeDVector.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 18808), new NodeId(0, 45), new NodeId(0, 18807).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode52() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 18809),
            new QualifiedName(0, "CartesianCoordinates"),
            new LocalizedText("", "CartesianCoordinates"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            CartesianCoordinates.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 18809), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode53() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 18810),
            new QualifiedName(0, "ThreeDCartesianCoordinates"),
            new LocalizedText("", "3DCartesianCoordinates"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            ThreeDCartesianCoordinates.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 18810), new NodeId(0, 45), new NodeId(0, 18809).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode54() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 18811),
            new QualifiedName(0, "Orientation"),
            new LocalizedText("", "Orientation"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            Orientation.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 18811), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode55() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 18812),
            new QualifiedName(0, "ThreeDOrientation"),
            new LocalizedText("", "3DOrientation"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            ThreeDOrientation.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 18812), new NodeId(0, 45), new NodeId(0, 18811).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode56() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 18813),
            new QualifiedName(0, "Frame"),
            new LocalizedText("", "Frame"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            Frame.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 18813), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode57() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 18814),
            new QualifiedName(0, "ThreeDFrame"),
            new LocalizedText("", "3DFrame"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            ThreeDFrame.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 18814), new NodeId(0, 45), new NodeId(0, 18813).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode58() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 11939),
            new QualifiedName(0, "OpenFileMode"),
            new LocalizedText("", "OpenFileMode"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            OpenFileMode.definition());
    node.addReference(
        new Reference(
            new NodeId(0, 11939), new NodeId(0, 46), new NodeId(0, 11940).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11939), new NodeId(0, 45), new NodeId(0, 29).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode59() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 15632),
            new QualifiedName(0, "IdentityCriteriaType"),
            new LocalizedText("", "IdentityCriteriaType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            IdentityCriteriaType.definition());
    node.addReference(
        new Reference(
            new NodeId(0, 15632), new NodeId(0, 46), new NodeId(0, 15633).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15632), new NodeId(0, 45), new NodeId(0, 29).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode60() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 15634),
            new QualifiedName(0, "IdentityMappingRuleType"),
            new LocalizedText("", "IdentityMappingRuleType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            IdentityMappingRuleType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 15634), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode61() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 23498),
            new QualifiedName(0, "CurrencyUnitType"),
            new LocalizedText("", "CurrencyUnitType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            CurrencyUnitType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 23498), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode62() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 32434),
            new QualifiedName(0, "AnnotationDataType"),
            new LocalizedText("", "AnnotationDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            AnnotationDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 32434), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode63() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 32435),
            new QualifiedName(0, "LinearConversionDataType"),
            new LocalizedText("", "LinearConversionDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            LinearConversionDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 32435), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode64() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 32436),
            new QualifiedName(0, "ConversionLimitEnum"),
            new LocalizedText("", "ConversionLimitEnum"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            ConversionLimitEnum.definition());
    node.addReference(
        new Reference(
            new NodeId(0, 32436), new NodeId(0, 46), new NodeId(0, 32437).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32436), new NodeId(0, 45), new NodeId(0, 29).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode65() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 32438),
            new QualifiedName(0, "QuantityDimension"),
            new LocalizedText("", "QuantityDimension"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            QuantityDimension.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 32438), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode66() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 32251),
            new QualifiedName(0, "AlarmMask"),
            new LocalizedText("", "AlarmMask"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            null);
    node.addReference(
        new Reference(
            new NodeId(0, 32251), new NodeId(0, 46), new NodeId(0, 32252).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 32251), new NodeId(0, 45), new NodeId(0, 5).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode67() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 23564),
            new QualifiedName(0, "TrustListValidationOptions"),
            new LocalizedText("", "TrustListValidationOptions"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            null);
    node.addReference(
        new Reference(
            new NodeId(0, 23564), new NodeId(0, 46), new NodeId(0, 23565).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 23564), new NodeId(0, 45), new NodeId(0, 7).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode68() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 12552),
            new QualifiedName(0, "TrustListMasks"),
            new LocalizedText("", "TrustListMasks"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            TrustListMasks.definition());
    node.addReference(
        new Reference(
            new NodeId(0, 12552), new NodeId(0, 46), new NodeId(0, 12553).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12552), new NodeId(0, 45), new NodeId(0, 29).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode69() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 12554),
            new QualifiedName(0, "TrustListDataType"),
            new LocalizedText("", "TrustListDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            TrustListDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 12554), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode70() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 32285),
            new QualifiedName(0, "TransactionErrorType"),
            new LocalizedText("", "TransactionErrorType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            TransactionErrorType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 32285), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode71() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 15534),
            new QualifiedName(0, "DataTypeSchemaHeader"),
            new LocalizedText("", "DataTypeSchemaHeader"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            DataTypeSchemaHeader.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 15534), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode72() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 14525),
            new QualifiedName(0, "DataTypeDescription"),
            new LocalizedText("", "DataTypeDescription"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            DataTypeDescription.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 14525), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode73() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 15487),
            new QualifiedName(0, "StructureDescription"),
            new LocalizedText("", "StructureDescription"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            StructureDescription.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 15487), new NodeId(0, 45), new NodeId(0, 14525).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode74() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 15488),
            new QualifiedName(0, "EnumDescription"),
            new LocalizedText("", "EnumDescription"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            EnumDescription.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 15488), new NodeId(0, 45), new NodeId(0, 14525).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode75() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 15005),
            new QualifiedName(0, "SimpleTypeDescription"),
            new LocalizedText("", "SimpleTypeDescription"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            SimpleTypeDescription.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 15005), new NodeId(0, 45), new NodeId(0, 14525).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode76() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 15006),
            new QualifiedName(0, "UABinaryFileDataType"),
            new LocalizedText("", "UABinaryFileDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            UABinaryFileDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 15006), new NodeId(0, 45), new NodeId(0, 15534).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode77() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 24105),
            new QualifiedName(0, "PortableQualifiedName"),
            new LocalizedText("", "PortableQualifiedName"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            PortableQualifiedName.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 24105), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode78() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 24106),
            new QualifiedName(0, "PortableNodeId"),
            new LocalizedText("", "PortableNodeId"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            PortableNodeId.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 24106), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode79() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 24107),
            new QualifiedName(0, "UnsignedRationalNumber"),
            new LocalizedText("", "UnsignedRationalNumber"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            UnsignedRationalNumber.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 24107), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode80() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 14647),
            new QualifiedName(0, "PubSubState"),
            new LocalizedText("", "PubSubState"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            PubSubState.definition());
    node.addReference(
        new Reference(
            new NodeId(0, 14647), new NodeId(0, 46), new NodeId(0, 14648).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14647), new NodeId(0, 45), new NodeId(0, 29).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode81() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 14523),
            new QualifiedName(0, "DataSetMetaDataType"),
            new LocalizedText("", "DataSetMetaDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            DataSetMetaDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 14523), new NodeId(0, 45), new NodeId(0, 15534).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode82() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 14524),
            new QualifiedName(0, "FieldMetaData"),
            new LocalizedText("", "FieldMetaData"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            FieldMetaData.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 14524), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode83() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 15904),
            new QualifiedName(0, "DataSetFieldFlags"),
            new LocalizedText("", "DataSetFieldFlags"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            null);
    node.addReference(
        new Reference(
            new NodeId(0, 15904), new NodeId(0, 46), new NodeId(0, 15577).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 15904), new NodeId(0, 45), new NodeId(0, 5).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode84() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 14593),
            new QualifiedName(0, "ConfigurationVersionDataType"),
            new LocalizedText("", "ConfigurationVersionDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            ConfigurationVersionDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 14593), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode85() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 15578),
            new QualifiedName(0, "PublishedDataSetDataType"),
            new LocalizedText("", "PublishedDataSetDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            PublishedDataSetDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 15578), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode86() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 15580),
            new QualifiedName(0, "PublishedDataSetSourceDataType"),
            new LocalizedText("", "PublishedDataSetSourceDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            PublishedDataSetSourceDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 15580), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode87() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 14273),
            new QualifiedName(0, "PublishedVariableDataType"),
            new LocalizedText("", "PublishedVariableDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            PublishedVariableDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 14273), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode88() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 15581),
            new QualifiedName(0, "PublishedDataItemsDataType"),
            new LocalizedText("", "PublishedDataItemsDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            PublishedDataItemsDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 15581), new NodeId(0, 45), new NodeId(0, 15580).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode89() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 15582),
            new QualifiedName(0, "PublishedEventsDataType"),
            new LocalizedText("", "PublishedEventsDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            PublishedEventsDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 15582), new NodeId(0, 45), new NodeId(0, 15580).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode90() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 25269),
            new QualifiedName(0, "PublishedDataSetCustomSourceDataType"),
            new LocalizedText("", "PublishedDataSetCustomSourceDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            PublishedDataSetCustomSourceDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 25269), new NodeId(0, 45), new NodeId(0, 15580).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode91() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 18593),
            new QualifiedName(0, "ActionTargetDataType"),
            new LocalizedText("", "ActionTargetDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            ActionTargetDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 18593), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode92() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 18594),
            new QualifiedName(0, "PublishedActionDataType"),
            new LocalizedText("", "PublishedActionDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            PublishedActionDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 18594), new NodeId(0, 45), new NodeId(0, 15580).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode93() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 18595),
            new QualifiedName(0, "ActionState"),
            new LocalizedText("", "ActionState"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            ActionState.definition());
    node.addReference(
        new Reference(
            new NodeId(0, 18595), new NodeId(0, 46), new NodeId(0, 18596).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18595), new NodeId(0, 45), new NodeId(0, 29).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode94() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 18597),
            new QualifiedName(0, "ActionMethodDataType"),
            new LocalizedText("", "ActionMethodDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            ActionMethodDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 18597), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode95() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 18793),
            new QualifiedName(0, "PublishedActionMethodDataType"),
            new LocalizedText("", "PublishedActionMethodDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            PublishedActionMethodDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 18793), new NodeId(0, 45), new NodeId(0, 18594).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode96() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 15583),
            new QualifiedName(0, "DataSetFieldContentMask"),
            new LocalizedText("", "DataSetFieldContentMask"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            null);
    node.addReference(
        new Reference(
            new NodeId(0, 15583), new NodeId(0, 46), new NodeId(0, 15584).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 15583), new NodeId(0, 45), new NodeId(0, 7).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode97() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 15597),
            new QualifiedName(0, "DataSetWriterDataType"),
            new LocalizedText("", "DataSetWriterDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            DataSetWriterDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 15597), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode98() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 15598),
            new QualifiedName(0, "DataSetWriterTransportDataType"),
            new LocalizedText("", "DataSetWriterTransportDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            DataSetWriterTransportDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 15598), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode99() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 15605),
            new QualifiedName(0, "DataSetWriterMessageDataType"),
            new LocalizedText("", "DataSetWriterMessageDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            DataSetWriterMessageDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 15605), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode100() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 15609),
            new QualifiedName(0, "PubSubGroupDataType"),
            new LocalizedText("", "PubSubGroupDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            PubSubGroupDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 15609), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode101() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 15480),
            new QualifiedName(0, "WriterGroupDataType"),
            new LocalizedText("", "WriterGroupDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            WriterGroupDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 15480), new NodeId(0, 45), new NodeId(0, 15609).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode102() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 15611),
            new QualifiedName(0, "WriterGroupTransportDataType"),
            new LocalizedText("", "WriterGroupTransportDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            WriterGroupTransportDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 15611), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode103() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 15616),
            new QualifiedName(0, "WriterGroupMessageDataType"),
            new LocalizedText("", "WriterGroupMessageDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            WriterGroupMessageDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 15616), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode104() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 15617),
            new QualifiedName(0, "PubSubConnectionDataType"),
            new LocalizedText("", "PubSubConnectionDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            PubSubConnectionDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 15617), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode105() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 15618),
            new QualifiedName(0, "ConnectionTransportDataType"),
            new LocalizedText("", "ConnectionTransportDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            ConnectionTransportDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 15618), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode106() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 15502),
            new QualifiedName(0, "NetworkAddressDataType"),
            new LocalizedText("", "NetworkAddressDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            NetworkAddressDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 15502), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode107() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 15510),
            new QualifiedName(0, "NetworkAddressUrlDataType"),
            new LocalizedText("", "NetworkAddressUrlDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            NetworkAddressUrlDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 15510), new NodeId(0, 45), new NodeId(0, 15502).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode108() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 15520),
            new QualifiedName(0, "ReaderGroupDataType"),
            new LocalizedText("", "ReaderGroupDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            ReaderGroupDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 15520), new NodeId(0, 45), new NodeId(0, 15609).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode109() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 15621),
            new QualifiedName(0, "ReaderGroupTransportDataType"),
            new LocalizedText("", "ReaderGroupTransportDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            ReaderGroupTransportDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 15621), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode110() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 15622),
            new QualifiedName(0, "ReaderGroupMessageDataType"),
            new LocalizedText("", "ReaderGroupMessageDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            ReaderGroupMessageDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 15622), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode111() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 15623),
            new QualifiedName(0, "DataSetReaderDataType"),
            new LocalizedText("", "DataSetReaderDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            DataSetReaderDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 15623), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode112() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 15628),
            new QualifiedName(0, "DataSetReaderTransportDataType"),
            new LocalizedText("", "DataSetReaderTransportDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            DataSetReaderTransportDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 15628), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode113() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 15629),
            new QualifiedName(0, "DataSetReaderMessageDataType"),
            new LocalizedText("", "DataSetReaderMessageDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            DataSetReaderMessageDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 15629), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode114() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 15630),
            new QualifiedName(0, "SubscribedDataSetDataType"),
            new LocalizedText("", "SubscribedDataSetDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            SubscribedDataSetDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 15630), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode115() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 15631),
            new QualifiedName(0, "TargetVariablesDataType"),
            new LocalizedText("", "TargetVariablesDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            TargetVariablesDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 15631), new NodeId(0, 45), new NodeId(0, 15630).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode116() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 14744),
            new QualifiedName(0, "FieldTargetDataType"),
            new LocalizedText("", "FieldTargetDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            FieldTargetDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 14744), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode117() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 15874),
            new QualifiedName(0, "OverrideValueHandling"),
            new LocalizedText("", "OverrideValueHandling"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            OverrideValueHandling.definition());
    node.addReference(
        new Reference(
            new NodeId(0, 15874), new NodeId(0, 46), new NodeId(0, 15875).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15874), new NodeId(0, 45), new NodeId(0, 29).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode118() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 15635),
            new QualifiedName(0, "SubscribedDataSetMirrorDataType"),
            new LocalizedText("", "SubscribedDataSetMirrorDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            SubscribedDataSetMirrorDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 15635), new NodeId(0, 45), new NodeId(0, 15630).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode119() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 15530),
            new QualifiedName(0, "PubSubConfigurationDataType"),
            new LocalizedText("", "PubSubConfigurationDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            PubSubConfigurationDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 15530), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode120() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 23599),
            new QualifiedName(0, "StandaloneSubscribedDataSetRefDataType"),
            new LocalizedText("", "StandaloneSubscribedDataSetRefDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            StandaloneSubscribedDataSetRefDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 23599), new NodeId(0, 45), new NodeId(0, 15630).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode121() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 23600),
            new QualifiedName(0, "StandaloneSubscribedDataSetDataType"),
            new LocalizedText("", "StandaloneSubscribedDataSetDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            StandaloneSubscribedDataSetDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 23600), new NodeId(0, 45), new NodeId(0, 15630).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode122() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 23601),
            new QualifiedName(0, "SecurityGroupDataType"),
            new LocalizedText("", "SecurityGroupDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            SecurityGroupDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 23601), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode123() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 25270),
            new QualifiedName(0, "PubSubKeyPushTargetDataType"),
            new LocalizedText("", "PubSubKeyPushTargetDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            PubSubKeyPushTargetDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 25270), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode124() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 23602),
            new QualifiedName(0, "PubSubConfiguration2DataType"),
            new LocalizedText("", "PubSubConfiguration2DataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            PubSubConfiguration2DataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 23602), new NodeId(0, 45), new NodeId(0, 15530).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode125() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 20408),
            new QualifiedName(0, "DataSetOrderingType"),
            new LocalizedText("", "DataSetOrderingType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            DataSetOrderingType.definition());
    node.addReference(
        new Reference(
            new NodeId(0, 20408), new NodeId(0, 46), new NodeId(0, 15641).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 20408), new NodeId(0, 45), new NodeId(0, 29).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode126() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 15642),
            new QualifiedName(0, "UadpNetworkMessageContentMask"),
            new LocalizedText("", "UadpNetworkMessageContentMask"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            null);
    node.addReference(
        new Reference(
            new NodeId(0, 15642), new NodeId(0, 46), new NodeId(0, 15643).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 15642), new NodeId(0, 45), new NodeId(0, 7).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode127() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 15645),
            new QualifiedName(0, "UadpWriterGroupMessageDataType"),
            new LocalizedText("", "UadpWriterGroupMessageDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            UadpWriterGroupMessageDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 15645), new NodeId(0, 45), new NodeId(0, 15616).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode128() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 15646),
            new QualifiedName(0, "UadpDataSetMessageContentMask"),
            new LocalizedText("", "UadpDataSetMessageContentMask"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            null);
    node.addReference(
        new Reference(
            new NodeId(0, 15646), new NodeId(0, 46), new NodeId(0, 15647).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 15646), new NodeId(0, 45), new NodeId(0, 7).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode129() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 15652),
            new QualifiedName(0, "UadpDataSetWriterMessageDataType"),
            new LocalizedText("", "UadpDataSetWriterMessageDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            UadpDataSetWriterMessageDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 15652), new NodeId(0, 45), new NodeId(0, 15605).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode130() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 15653),
            new QualifiedName(0, "UadpDataSetReaderMessageDataType"),
            new LocalizedText("", "UadpDataSetReaderMessageDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            UadpDataSetReaderMessageDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 15653), new NodeId(0, 45), new NodeId(0, 15629).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode131() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 15654),
            new QualifiedName(0, "JsonNetworkMessageContentMask"),
            new LocalizedText("", "JsonNetworkMessageContentMask"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            null);
    node.addReference(
        new Reference(
            new NodeId(0, 15654), new NodeId(0, 46), new NodeId(0, 15655).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 15654), new NodeId(0, 45), new NodeId(0, 7).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode132() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 15657),
            new QualifiedName(0, "JsonWriterGroupMessageDataType"),
            new LocalizedText("", "JsonWriterGroupMessageDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            JsonWriterGroupMessageDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 15657), new NodeId(0, 45), new NodeId(0, 15616).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode133() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 15658),
            new QualifiedName(0, "JsonDataSetMessageContentMask"),
            new LocalizedText("", "JsonDataSetMessageContentMask"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            null);
    node.addReference(
        new Reference(
            new NodeId(0, 15658), new NodeId(0, 46), new NodeId(0, 15659).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 15658), new NodeId(0, 45), new NodeId(0, 7).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode134() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 15664),
            new QualifiedName(0, "JsonDataSetWriterMessageDataType"),
            new LocalizedText("", "JsonDataSetWriterMessageDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            JsonDataSetWriterMessageDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 15664), new NodeId(0, 45), new NodeId(0, 15605).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode135() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 15665),
            new QualifiedName(0, "JsonDataSetReaderMessageDataType"),
            new LocalizedText("", "JsonDataSetReaderMessageDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            JsonDataSetReaderMessageDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 15665), new NodeId(0, 45), new NodeId(0, 15629).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode136() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 23603),
            new QualifiedName(0, "QosDataType"),
            new LocalizedText("", "QosDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            QosDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 23603), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode137() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 23604),
            new QualifiedName(0, "TransmitQosDataType"),
            new LocalizedText("", "TransmitQosDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            TransmitQosDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 23604), new NodeId(0, 45), new NodeId(0, 23603).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode138() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 23605),
            new QualifiedName(0, "TransmitQosPriorityDataType"),
            new LocalizedText("", "TransmitQosPriorityDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            TransmitQosPriorityDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 23605), new NodeId(0, 45), new NodeId(0, 23604).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode139() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 23608),
            new QualifiedName(0, "ReceiveQosDataType"),
            new LocalizedText("", "ReceiveQosDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            ReceiveQosDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 23608), new NodeId(0, 45), new NodeId(0, 23603).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode140() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 23609),
            new QualifiedName(0, "ReceiveQosPriorityDataType"),
            new LocalizedText("", "ReceiveQosPriorityDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            ReceiveQosPriorityDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 23609), new NodeId(0, 45), new NodeId(0, 23608).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode141() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 17467),
            new QualifiedName(0, "DatagramConnectionTransportDataType"),
            new LocalizedText("", "DatagramConnectionTransportDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            DatagramConnectionTransportDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 17467), new NodeId(0, 45), new NodeId(0, 15618).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode142() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 23612),
            new QualifiedName(0, "DatagramConnectionTransport2DataType"),
            new LocalizedText("", "DatagramConnectionTransport2DataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            DatagramConnectionTransport2DataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 23612), new NodeId(0, 45), new NodeId(0, 17467).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode143() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 15532),
            new QualifiedName(0, "DatagramWriterGroupTransportDataType"),
            new LocalizedText("", "DatagramWriterGroupTransportDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            DatagramWriterGroupTransportDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 15532), new NodeId(0, 45), new NodeId(0, 15611).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode144() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 23613),
            new QualifiedName(0, "DatagramWriterGroupTransport2DataType"),
            new LocalizedText("", "DatagramWriterGroupTransport2DataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            DatagramWriterGroupTransport2DataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 23613), new NodeId(0, 45), new NodeId(0, 15532).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode145() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 23614),
            new QualifiedName(0, "DatagramDataSetReaderTransportDataType"),
            new LocalizedText("", "DatagramDataSetReaderTransportDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            DatagramDataSetReaderTransportDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 23614), new NodeId(0, 45), new NodeId(0, 15628).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode146() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 18794),
            new QualifiedName(0, "DtlsPubSubConnectionDataType"),
            new LocalizedText("", "DtlsPubSubConnectionDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            DtlsPubSubConnectionDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 18794), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode147() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 15007),
            new QualifiedName(0, "BrokerConnectionTransportDataType"),
            new LocalizedText("", "BrokerConnectionTransportDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            BrokerConnectionTransportDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 15007), new NodeId(0, 45), new NodeId(0, 15618).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode148() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 15008),
            new QualifiedName(0, "BrokerTransportQualityOfService"),
            new LocalizedText("", "BrokerTransportQualityOfService"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            BrokerTransportQualityOfService.definition());
    node.addReference(
        new Reference(
            new NodeId(0, 15008), new NodeId(0, 46), new NodeId(0, 15009).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15008), new NodeId(0, 45), new NodeId(0, 29).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode149() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 15667),
            new QualifiedName(0, "BrokerWriterGroupTransportDataType"),
            new LocalizedText("", "BrokerWriterGroupTransportDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            BrokerWriterGroupTransportDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 15667), new NodeId(0, 45), new NodeId(0, 15611).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode150() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 15669),
            new QualifiedName(0, "BrokerDataSetWriterTransportDataType"),
            new LocalizedText("", "BrokerDataSetWriterTransportDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            BrokerDataSetWriterTransportDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 15669), new NodeId(0, 45), new NodeId(0, 15598).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode151() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 15670),
            new QualifiedName(0, "BrokerDataSetReaderTransportDataType"),
            new LocalizedText("", "BrokerDataSetReaderTransportDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            BrokerDataSetReaderTransportDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 15670), new NodeId(0, 45), new NodeId(0, 15628).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode152() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 25517),
            new QualifiedName(0, "PubSubConfigurationRefMask"),
            new LocalizedText("", "PubSubConfigurationRefMask"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            null);
    node.addReference(
        new Reference(
            new NodeId(0, 25517), new NodeId(0, 46), new NodeId(0, 25518).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 25517), new NodeId(0, 45), new NodeId(0, 7).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode153() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 25519),
            new QualifiedName(0, "PubSubConfigurationRefDataType"),
            new LocalizedText("", "PubSubConfigurationRefDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            PubSubConfigurationRefDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 25519), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode154() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 25520),
            new QualifiedName(0, "PubSubConfigurationValueDataType"),
            new LocalizedText("", "PubSubConfigurationValueDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            PubSubConfigurationValueDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 25520), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode155() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 19723),
            new QualifiedName(0, "DiagnosticsLevel"),
            new LocalizedText("", "DiagnosticsLevel"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            DiagnosticsLevel.definition());
    node.addReference(
        new Reference(
            new NodeId(0, 19723), new NodeId(0, 46), new NodeId(0, 19724).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19723), new NodeId(0, 45), new NodeId(0, 29).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode156() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 19730),
            new QualifiedName(0, "PubSubDiagnosticsCounterClassification"),
            new LocalizedText("", "PubSubDiagnosticsCounterClassification"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            PubSubDiagnosticsCounterClassification.definition());
    node.addReference(
        new Reference(
            new NodeId(0, 19730), new NodeId(0, 46), new NodeId(0, 19731).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19730), new NodeId(0, 45), new NodeId(0, 29).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode157() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 23468),
            new QualifiedName(0, "AliasNameDataType"),
            new LocalizedText("", "AliasNameDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            AliasNameDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 23468), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode158() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 24277),
            new QualifiedName(0, "PasswordOptionsMask"),
            new LocalizedText("", "PasswordOptionsMask"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            null);
    node.addReference(
        new Reference(
            new NodeId(0, 24277), new NodeId(0, 46), new NodeId(0, 24278).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 24277), new NodeId(0, 45), new NodeId(0, 7).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode159() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 24279),
            new QualifiedName(0, "UserConfigurationMask"),
            new LocalizedText("", "UserConfigurationMask"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            null);
    node.addReference(
        new Reference(
            new NodeId(0, 24279), new NodeId(0, 46), new NodeId(0, 24280).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 24279), new NodeId(0, 45), new NodeId(0, 7).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode160() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 24281),
            new QualifiedName(0, "UserManagementDataType"),
            new LocalizedText("", "UserManagementDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            UserManagementDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 24281), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode161() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 25726),
            new QualifiedName(0, "EncodedTicket"),
            new LocalizedText("", "EncodedTicket"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            null);
    node.addReference(
        new Reference(
            new NodeId(0, 25726), new NodeId(0, 45), new NodeId(0, 12).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode162() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 24210),
            new QualifiedName(0, "Duplex"),
            new LocalizedText("", "Duplex"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            Duplex.definition());
    node.addReference(
        new Reference(
            new NodeId(0, 24210), new NodeId(0, 46), new NodeId(0, 24235).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24210), new NodeId(0, 45), new NodeId(0, 29).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode163() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 24212),
            new QualifiedName(0, "InterfaceAdminStatus"),
            new LocalizedText("", "InterfaceAdminStatus"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            InterfaceAdminStatus.definition());
    node.addReference(
        new Reference(
            new NodeId(0, 24212), new NodeId(0, 46), new NodeId(0, 24236).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24212), new NodeId(0, 45), new NodeId(0, 29).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode164() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 24214),
            new QualifiedName(0, "InterfaceOperStatus"),
            new LocalizedText("", "InterfaceOperStatus"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            InterfaceOperStatus.definition());
    node.addReference(
        new Reference(
            new NodeId(0, 24214), new NodeId(0, 46), new NodeId(0, 24237).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24214), new NodeId(0, 45), new NodeId(0, 29).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode165() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 24216),
            new QualifiedName(0, "NegotiationStatus"),
            new LocalizedText("", "NegotiationStatus"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            NegotiationStatus.definition());
    node.addReference(
        new Reference(
            new NodeId(0, 24216), new NodeId(0, 46), new NodeId(0, 24238).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24216), new NodeId(0, 45), new NodeId(0, 29).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode166() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 24218),
            new QualifiedName(0, "TsnFailureCode"),
            new LocalizedText("", "TsnFailureCode"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            TsnFailureCode.definition());
    node.addReference(
        new Reference(
            new NodeId(0, 24218), new NodeId(0, 46), new NodeId(0, 24239).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24218), new NodeId(0, 45), new NodeId(0, 29).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode167() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 24220),
            new QualifiedName(0, "TsnStreamState"),
            new LocalizedText("", "TsnStreamState"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            TsnStreamState.definition());
    node.addReference(
        new Reference(
            new NodeId(0, 24220), new NodeId(0, 46), new NodeId(0, 24240).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24220), new NodeId(0, 45), new NodeId(0, 29).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode168() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 24222),
            new QualifiedName(0, "TsnTalkerStatus"),
            new LocalizedText("", "TsnTalkerStatus"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            TsnTalkerStatus.definition());
    node.addReference(
        new Reference(
            new NodeId(0, 24222), new NodeId(0, 46), new NodeId(0, 24241).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24222), new NodeId(0, 45), new NodeId(0, 29).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode169() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 24224),
            new QualifiedName(0, "TsnListenerStatus"),
            new LocalizedText("", "TsnListenerStatus"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            TsnListenerStatus.definition());
    node.addReference(
        new Reference(
            new NodeId(0, 24224), new NodeId(0, 46), new NodeId(0, 24242).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24224), new NodeId(0, 45), new NodeId(0, 29).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode170() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 18947),
            new QualifiedName(0, "ChassisIdSubtype"),
            new LocalizedText("", "ChassisIdSubtype"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            ChassisIdSubtype.definition());
    node.addReference(
        new Reference(
            new NodeId(0, 18947), new NodeId(0, 46), new NodeId(0, 18948).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18947), new NodeId(0, 45), new NodeId(0, 29).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode171() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 18949),
            new QualifiedName(0, "PortIdSubtype"),
            new LocalizedText("", "PortIdSubtype"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            PortIdSubtype.definition());
    node.addReference(
        new Reference(
            new NodeId(0, 18949), new NodeId(0, 46), new NodeId(0, 18950).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18949), new NodeId(0, 45), new NodeId(0, 29).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode172() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 18951),
            new QualifiedName(0, "ManAddrIfSubtype"),
            new LocalizedText("", "ManAddrIfSubtype"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            ManAddrIfSubtype.definition());
    node.addReference(
        new Reference(
            new NodeId(0, 18951), new NodeId(0, 46), new NodeId(0, 18952).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18951), new NodeId(0, 45), new NodeId(0, 29).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode173() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 25220),
            new QualifiedName(0, "PriorityMappingEntryType"),
            new LocalizedText("", "PriorityMappingEntryType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            PriorityMappingEntryType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 25220), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode174() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 18953),
            new QualifiedName(0, "LldpManagementAddressTxPortType"),
            new LocalizedText("", "LldpManagementAddressTxPortType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            LldpManagementAddressTxPortType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 18953), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode175() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 18954),
            new QualifiedName(0, "LldpManagementAddressType"),
            new LocalizedText("", "LldpManagementAddressType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            LldpManagementAddressType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 18954), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode176() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 18955),
            new QualifiedName(0, "LldpTlvType"),
            new LocalizedText("", "LldpTlvType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            LldpTlvType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 18955), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode177() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 18956),
            new QualifiedName(0, "LldpSystemCapabilitiesMap"),
            new LocalizedText("", "LldpSystemCapabilitiesMap"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            null);
    node.addReference(
        new Reference(
            new NodeId(0, 18956), new NodeId(0, 46), new NodeId(0, 18957).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 18956), new NodeId(0, 45), new NodeId(0, 7).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode178() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 32659),
            new QualifiedName(0, "ReferenceDescriptionDataType"),
            new LocalizedText("", "ReferenceDescriptionDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            ReferenceDescriptionDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 32659), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode179() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 32660),
            new QualifiedName(0, "ReferenceListEntryDataType"),
            new LocalizedText("", "ReferenceListEntryDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            ReferenceListEntryDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 32660), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode180() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 256),
            new QualifiedName(0, "IdType"),
            new LocalizedText("", "IdType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            IdType.definition());
    node.addReference(
        new Reference(new NodeId(0, 256), new NodeId(0, 46), new NodeId(0, 7591).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 256), new NodeId(0, 45), new NodeId(0, 29).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode181() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 257),
            new QualifiedName(0, "NodeClass"),
            new LocalizedText("", "NodeClass"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            NodeClass.definition());
    node.addReference(
        new Reference(
            new NodeId(0, 257), new NodeId(0, 46), new NodeId(0, 11878).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 257), new NodeId(0, 45), new NodeId(0, 29).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode182() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 94),
            new QualifiedName(0, "PermissionType"),
            new LocalizedText("", "PermissionType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            null);
    node.addReference(
        new Reference(new NodeId(0, 94), new NodeId(0, 46), new NodeId(0, 15030).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 94), new NodeId(0, 45), new NodeId(0, 7).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode183() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 15031),
            new QualifiedName(0, "AccessLevelType"),
            new LocalizedText("", "AccessLevelType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            null);
    node.addReference(
        new Reference(
            new NodeId(0, 15031), new NodeId(0, 46), new NodeId(0, 15032).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 15031), new NodeId(0, 45), new NodeId(0, 3).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode184() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 15406),
            new QualifiedName(0, "AccessLevelExType"),
            new LocalizedText("", "AccessLevelExType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            null);
    node.addReference(
        new Reference(
            new NodeId(0, 15406), new NodeId(0, 46), new NodeId(0, 15407).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 15406), new NodeId(0, 45), new NodeId(0, 7).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode185() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 15033),
            new QualifiedName(0, "EventNotifierType"),
            new LocalizedText("", "EventNotifierType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            null);
    node.addReference(
        new Reference(
            new NodeId(0, 15033), new NodeId(0, 46), new NodeId(0, 15034).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 15033), new NodeId(0, 45), new NodeId(0, 3).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode186() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 95),
            new QualifiedName(0, "AccessRestrictionType"),
            new LocalizedText("", "AccessRestrictionType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            null);
    node.addReference(
        new Reference(new NodeId(0, 95), new NodeId(0, 46), new NodeId(0, 15035).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 95), new NodeId(0, 45), new NodeId(0, 5).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode187() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 96),
            new QualifiedName(0, "RolePermissionType"),
            new LocalizedText("", "RolePermissionType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            RolePermissionType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(new NodeId(0, 96), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode188() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 97),
            new QualifiedName(0, "DataTypeDefinition"),
            new LocalizedText("", "DataTypeDefinition"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            DataTypeDefinition.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(new NodeId(0, 97), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode189() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 98),
            new QualifiedName(0, "StructureType"),
            new LocalizedText("", "StructureType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            StructureType.definition());
    node.addReference(
        new Reference(new NodeId(0, 98), new NodeId(0, 46), new NodeId(0, 14528).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 98), new NodeId(0, 45), new NodeId(0, 29).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode190() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 101),
            new QualifiedName(0, "StructureField"),
            new LocalizedText("", "StructureField"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            StructureField.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(new NodeId(0, 101), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode191() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 99),
            new QualifiedName(0, "StructureDefinition"),
            new LocalizedText("", "StructureDefinition"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            StructureDefinition.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(new NodeId(0, 99), new NodeId(0, 45), new NodeId(0, 97).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode192() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 100),
            new QualifiedName(0, "EnumDefinition"),
            new LocalizedText("", "EnumDefinition"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            EnumDefinition.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(new NodeId(0, 100), new NodeId(0, 45), new NodeId(0, 97).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode193() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 296),
            new QualifiedName(0, "Argument"),
            new LocalizedText("", "Argument"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            Argument.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(new NodeId(0, 296), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode194() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 7594),
            new QualifiedName(0, "EnumValueType"),
            new LocalizedText("", "EnumValueType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            EnumValueType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(new NodeId(0, 7594), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode195() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 102),
            new QualifiedName(0, "EnumField"),
            new LocalizedText("", "EnumField"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            EnumField.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 102), new NodeId(0, 45), new NodeId(0, 7594).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode196() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 12755),
            new QualifiedName(0, "OptionSet"),
            new LocalizedText("", "OptionSet"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            OptionSet.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 12755), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode197() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 12877),
            new QualifiedName(0, "NormalizedString"),
            new LocalizedText("", "NormalizedString"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            null);
    node.addReference(
        new Reference(
            new NodeId(0, 12877), new NodeId(0, 45), new NodeId(0, 12).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode198() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 12878),
            new QualifiedName(0, "DecimalString"),
            new LocalizedText("", "DecimalString"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            null);
    node.addReference(
        new Reference(
            new NodeId(0, 12878), new NodeId(0, 45), new NodeId(0, 12).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode199() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 12879),
            new QualifiedName(0, "DurationString"),
            new LocalizedText("", "DurationString"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            null);
    node.addReference(
        new Reference(
            new NodeId(0, 12879), new NodeId(0, 45), new NodeId(0, 12).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode200() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 12880),
            new QualifiedName(0, "TimeString"),
            new LocalizedText("", "TimeString"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            null);
    node.addReference(
        new Reference(
            new NodeId(0, 12880), new NodeId(0, 45), new NodeId(0, 12).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode201() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 12881),
            new QualifiedName(0, "DateString"),
            new LocalizedText("", "DateString"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            null);
    node.addReference(
        new Reference(
            new NodeId(0, 12881), new NodeId(0, 45), new NodeId(0, 12).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode202() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 290),
            new QualifiedName(0, "Duration"),
            new LocalizedText("", "Duration"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            null);
    node.addReference(
        new Reference(new NodeId(0, 290), new NodeId(0, 45), new NodeId(0, 11).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode203() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 294),
            new QualifiedName(0, "UtcTime"),
            new LocalizedText("", "UtcTime"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            null);
    node.addReference(
        new Reference(new NodeId(0, 294), new NodeId(0, 45), new NodeId(0, 13).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode204() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 295),
            new QualifiedName(0, "LocaleId"),
            new LocalizedText("", "LocaleId"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            null);
    node.addReference(
        new Reference(new NodeId(0, 295), new NodeId(0, 45), new NodeId(0, 12).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode205() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 8912),
            new QualifiedName(0, "TimeZoneDataType"),
            new LocalizedText("", "TimeZoneDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            TimeZoneDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(new NodeId(0, 8912), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode206() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 17588),
            new QualifiedName(0, "Index"),
            new LocalizedText("", "Index"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            null);
    node.addReference(
        new Reference(new NodeId(0, 17588), new NodeId(0, 45), new NodeId(0, 7).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode207() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 288),
            new QualifiedName(0, "IntegerId"),
            new LocalizedText("", "IntegerId"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            null);
    node.addReference(
        new Reference(new NodeId(0, 288), new NodeId(0, 45), new NodeId(0, 7).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode208() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 307),
            new QualifiedName(0, "ApplicationType"),
            new LocalizedText("", "ApplicationType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            ApplicationType.definition());
    node.addReference(
        new Reference(new NodeId(0, 307), new NodeId(0, 46), new NodeId(0, 7597).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 307), new NodeId(0, 45), new NodeId(0, 29).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode209() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 308),
            new QualifiedName(0, "ApplicationDescription"),
            new LocalizedText("", "ApplicationDescription"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            ApplicationDescription.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(new NodeId(0, 308), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode210() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 20998),
            new QualifiedName(0, "VersionTime"),
            new LocalizedText("", "VersionTime"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            null);
    node.addReference(
        new Reference(new NodeId(0, 20998), new NodeId(0, 45), new NodeId(0, 7).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode211() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 12189),
            new QualifiedName(0, "ServerOnNetwork"),
            new LocalizedText("", "ServerOnNetwork"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            ServerOnNetwork.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 12189), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode212() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 311),
            new QualifiedName(0, "ApplicationInstanceCertificate"),
            new LocalizedText("", "ApplicationInstanceCertificate"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            null);
    node.addReference(
        new Reference(new NodeId(0, 311), new NodeId(0, 45), new NodeId(0, 15).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode213() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 302),
            new QualifiedName(0, "MessageSecurityMode"),
            new LocalizedText("", "MessageSecurityMode"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            MessageSecurityMode.definition());
    node.addReference(
        new Reference(new NodeId(0, 302), new NodeId(0, 46), new NodeId(0, 7595).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 302), new NodeId(0, 45), new NodeId(0, 29).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode214() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 303),
            new QualifiedName(0, "UserTokenType"),
            new LocalizedText("", "UserTokenType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            UserTokenType.definition());
    node.addReference(
        new Reference(new NodeId(0, 303), new NodeId(0, 46), new NodeId(0, 7596).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 303), new NodeId(0, 45), new NodeId(0, 29).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode215() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 304),
            new QualifiedName(0, "UserTokenPolicy"),
            new LocalizedText("", "UserTokenPolicy"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            UserTokenPolicy.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(new NodeId(0, 304), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode216() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 312),
            new QualifiedName(0, "EndpointDescription"),
            new LocalizedText("", "EndpointDescription"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            EndpointDescription.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(new NodeId(0, 312), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode217() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 432),
            new QualifiedName(0, "RegisteredServer"),
            new LocalizedText("", "RegisteredServer"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            RegisteredServer.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(new NodeId(0, 432), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode218() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 12890),
            new QualifiedName(0, "DiscoveryConfiguration"),
            new LocalizedText("", "DiscoveryConfiguration"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            DiscoveryConfiguration.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 12890), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode219() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 12891),
            new QualifiedName(0, "MdnsDiscoveryConfiguration"),
            new LocalizedText("", "MdnsDiscoveryConfiguration"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            MdnsDiscoveryConfiguration.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 12891), new NodeId(0, 45), new NodeId(0, 12890).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode220() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 315),
            new QualifiedName(0, "SecurityTokenRequestType"),
            new LocalizedText("", "SecurityTokenRequestType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            SecurityTokenRequestType.definition());
    node.addReference(
        new Reference(new NodeId(0, 315), new NodeId(0, 46), new NodeId(0, 7598).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 315), new NodeId(0, 45), new NodeId(0, 29).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode221() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 344),
            new QualifiedName(0, "SignedSoftwareCertificate"),
            new LocalizedText("", "SignedSoftwareCertificate"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            SignedSoftwareCertificate.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(new NodeId(0, 344), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode222() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 388),
            new QualifiedName(0, "SessionAuthenticationToken"),
            new LocalizedText("", "SessionAuthenticationToken"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            null);
    node.addReference(
        new Reference(new NodeId(0, 388), new NodeId(0, 45), new NodeId(0, 17).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode223() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 316),
            new QualifiedName(0, "UserIdentityToken"),
            new LocalizedText("", "UserIdentityToken"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            UserIdentityToken.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(new NodeId(0, 316), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode224() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 319),
            new QualifiedName(0, "AnonymousIdentityToken"),
            new LocalizedText("", "AnonymousIdentityToken"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            AnonymousIdentityToken.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(new NodeId(0, 319), new NodeId(0, 45), new NodeId(0, 316).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode225() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 322),
            new QualifiedName(0, "UserNameIdentityToken"),
            new LocalizedText("", "UserNameIdentityToken"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            UserNameIdentityToken.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(new NodeId(0, 322), new NodeId(0, 45), new NodeId(0, 316).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode226() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 325),
            new QualifiedName(0, "X509IdentityToken"),
            new LocalizedText("", "X509IdentityToken"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            X509IdentityToken.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(new NodeId(0, 325), new NodeId(0, 45), new NodeId(0, 316).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode227() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 938),
            new QualifiedName(0, "IssuedIdentityToken"),
            new LocalizedText("", "IssuedIdentityToken"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            IssuedIdentityToken.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(new NodeId(0, 938), new NodeId(0, 45), new NodeId(0, 316).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode228() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 348),
            new QualifiedName(0, "NodeAttributesMask"),
            new LocalizedText("", "NodeAttributesMask"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            NodeAttributesMask.definition());
    node.addReference(
        new Reference(
            new NodeId(0, 348), new NodeId(0, 46), new NodeId(0, 11881).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 348), new NodeId(0, 45), new NodeId(0, 29).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode229() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 376),
            new QualifiedName(0, "AddNodesItem"),
            new LocalizedText("", "AddNodesItem"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            AddNodesItem.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(new NodeId(0, 376), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode230() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 379),
            new QualifiedName(0, "AddReferencesItem"),
            new LocalizedText("", "AddReferencesItem"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            AddReferencesItem.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(new NodeId(0, 379), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode231() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 382),
            new QualifiedName(0, "DeleteNodesItem"),
            new LocalizedText("", "DeleteNodesItem"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            DeleteNodesItem.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(new NodeId(0, 382), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode232() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 385),
            new QualifiedName(0, "DeleteReferencesItem"),
            new LocalizedText("", "DeleteReferencesItem"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            DeleteReferencesItem.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(new NodeId(0, 385), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode233() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 347),
            new QualifiedName(0, "AttributeWriteMask"),
            new LocalizedText("", "AttributeWriteMask"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            null);
    node.addReference(
        new Reference(
            new NodeId(0, 347), new NodeId(0, 46), new NodeId(0, 15036).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 347), new NodeId(0, 45), new NodeId(0, 7).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode234() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 521),
            new QualifiedName(0, "ContinuationPoint"),
            new LocalizedText("", "ContinuationPoint"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            null);
    node.addReference(
        new Reference(new NodeId(0, 521), new NodeId(0, 45), new NodeId(0, 15).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode235() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 537),
            new QualifiedName(0, "RelativePathElement"),
            new LocalizedText("", "RelativePathElement"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            RelativePathElement.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(new NodeId(0, 537), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode236() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 540),
            new QualifiedName(0, "RelativePath"),
            new LocalizedText("", "RelativePath"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            RelativePath.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(new NodeId(0, 540), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode237() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 289),
            new QualifiedName(0, "Counter"),
            new LocalizedText("", "Counter"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            null);
    node.addReference(
        new Reference(new NodeId(0, 289), new NodeId(0, 45), new NodeId(0, 7).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode238() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 291),
            new QualifiedName(0, "NumericRange"),
            new LocalizedText("", "NumericRange"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            null);
    node.addReference(
        new Reference(new NodeId(0, 291), new NodeId(0, 45), new NodeId(0, 12).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode239() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 331),
            new QualifiedName(0, "EndpointConfiguration"),
            new LocalizedText("", "EndpointConfiguration"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            EndpointConfiguration.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(new NodeId(0, 331), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode240() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 576),
            new QualifiedName(0, "FilterOperator"),
            new LocalizedText("", "FilterOperator"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            FilterOperator.definition());
    node.addReference(
        new Reference(new NodeId(0, 576), new NodeId(0, 46), new NodeId(0, 7605).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 576), new NodeId(0, 45), new NodeId(0, 29).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode241() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 583),
            new QualifiedName(0, "ContentFilterElement"),
            new LocalizedText("", "ContentFilterElement"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            ContentFilterElement.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(new NodeId(0, 583), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode242() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 586),
            new QualifiedName(0, "ContentFilter"),
            new LocalizedText("", "ContentFilter"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            ContentFilter.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(new NodeId(0, 586), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode243() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 589),
            new QualifiedName(0, "FilterOperand"),
            new LocalizedText("", "FilterOperand"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true,
            FilterOperand.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(new NodeId(0, 589), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode244() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 592),
            new QualifiedName(0, "ElementOperand"),
            new LocalizedText("", "ElementOperand"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            ElementOperand.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(new NodeId(0, 592), new NodeId(0, 45), new NodeId(0, 589).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode245() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 595),
            new QualifiedName(0, "LiteralOperand"),
            new LocalizedText("", "LiteralOperand"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            LiteralOperand.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(new NodeId(0, 595), new NodeId(0, 45), new NodeId(0, 589).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode246() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 598),
            new QualifiedName(0, "AttributeOperand"),
            new LocalizedText("", "AttributeOperand"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            AttributeOperand.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(new NodeId(0, 598), new NodeId(0, 45), new NodeId(0, 589).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode247() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 601),
            new QualifiedName(0, "SimpleAttributeOperand"),
            new LocalizedText("", "SimpleAttributeOperand"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            SimpleAttributeOperand.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(new NodeId(0, 601), new NodeId(0, 45), new NodeId(0, 589).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode248() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 11216),
            new QualifiedName(0, "ModificationInfo"),
            new LocalizedText("", "ModificationInfo"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            ModificationInfo.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 11216), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode249() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 659),
            new QualifiedName(0, "HistoryEvent"),
            new LocalizedText("", "HistoryEvent"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            HistoryEvent.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(new NodeId(0, 659), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode250() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 32824),
            new QualifiedName(0, "HistoryModifiedEvent"),
            new LocalizedText("", "HistoryModifiedEvent"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            HistoryModifiedEvent.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 32824), new NodeId(0, 45), new NodeId(0, 659).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode251() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 11234),
            new QualifiedName(0, "HistoryUpdateType"),
            new LocalizedText("", "HistoryUpdateType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            HistoryUpdateType.definition());
    node.addReference(
        new Reference(
            new NodeId(0, 11234), new NodeId(0, 46), new NodeId(0, 11884).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11234), new NodeId(0, 45), new NodeId(0, 29).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode252() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 11293),
            new QualifiedName(0, "PerformUpdateType"),
            new LocalizedText("", "PerformUpdateType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            PerformUpdateType.definition());
    node.addReference(
        new Reference(
            new NodeId(0, 11293), new NodeId(0, 46), new NodeId(0, 11885).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11293), new NodeId(0, 45), new NodeId(0, 29).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode253() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 719),
            new QualifiedName(0, "MonitoringFilter"),
            new LocalizedText("", "MonitoringFilter"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            MonitoringFilter.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(new NodeId(0, 719), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode254() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 725),
            new QualifiedName(0, "EventFilter"),
            new LocalizedText("", "EventFilter"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            EventFilter.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(new NodeId(0, 725), new NodeId(0, 45), new NodeId(0, 719).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode255() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 948),
            new QualifiedName(0, "AggregateConfiguration"),
            new LocalizedText("", "AggregateConfiguration"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            AggregateConfiguration.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(new NodeId(0, 948), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode256() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 920),
            new QualifiedName(0, "HistoryEventFieldList"),
            new LocalizedText("", "HistoryEventFieldList"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            HistoryEventFieldList.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(new NodeId(0, 920), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode257() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 338),
            new QualifiedName(0, "BuildInfo"),
            new LocalizedText("", "BuildInfo"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            BuildInfo.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(new NodeId(0, 338), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode258() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 851),
            new QualifiedName(0, "RedundancySupport"),
            new LocalizedText("", "RedundancySupport"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            RedundancySupport.definition());
    node.addReference(
        new Reference(new NodeId(0, 851), new NodeId(0, 46), new NodeId(0, 7611).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 851), new NodeId(0, 45), new NodeId(0, 29).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode259() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 852),
            new QualifiedName(0, "ServerState"),
            new LocalizedText("", "ServerState"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            ServerState.definition());
    node.addReference(
        new Reference(new NodeId(0, 852), new NodeId(0, 46), new NodeId(0, 7612).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 852), new NodeId(0, 45), new NodeId(0, 29).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode260() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 853),
            new QualifiedName(0, "RedundantServerDataType"),
            new LocalizedText("", "RedundantServerDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            RedundantServerDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(new NodeId(0, 853), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode261() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 11943),
            new QualifiedName(0, "EndpointUrlListDataType"),
            new LocalizedText("", "EndpointUrlListDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            EndpointUrlListDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 11943), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode262() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 11944),
            new QualifiedName(0, "NetworkGroupDataType"),
            new LocalizedText("", "NetworkGroupDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            NetworkGroupDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 11944), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode263() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 856),
            new QualifiedName(0, "SamplingIntervalDiagnosticsDataType"),
            new LocalizedText("", "SamplingIntervalDiagnosticsDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            SamplingIntervalDiagnosticsDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(new NodeId(0, 856), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode264() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 859),
            new QualifiedName(0, "ServerDiagnosticsSummaryDataType"),
            new LocalizedText("", "ServerDiagnosticsSummaryDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            ServerDiagnosticsSummaryDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(new NodeId(0, 859), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode265() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 862),
            new QualifiedName(0, "ServerStatusDataType"),
            new LocalizedText("", "ServerStatusDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            ServerStatusDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(new NodeId(0, 862), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode266() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 865),
            new QualifiedName(0, "SessionDiagnosticsDataType"),
            new LocalizedText("", "SessionDiagnosticsDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            SessionDiagnosticsDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(new NodeId(0, 865), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode267() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 868),
            new QualifiedName(0, "SessionSecurityDiagnosticsDataType"),
            new LocalizedText("", "SessionSecurityDiagnosticsDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            SessionSecurityDiagnosticsDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(new NodeId(0, 868), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode268() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 871),
            new QualifiedName(0, "ServiceCounterDataType"),
            new LocalizedText("", "ServiceCounterDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            ServiceCounterDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(new NodeId(0, 871), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode269() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 299),
            new QualifiedName(0, "StatusResult"),
            new LocalizedText("", "StatusResult"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            StatusResult.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(new NodeId(0, 299), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode270() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 874),
            new QualifiedName(0, "SubscriptionDiagnosticsDataType"),
            new LocalizedText("", "SubscriptionDiagnosticsDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            SubscriptionDiagnosticsDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(new NodeId(0, 874), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode271() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 877),
            new QualifiedName(0, "ModelChangeStructureDataType"),
            new LocalizedText("", "ModelChangeStructureDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            ModelChangeStructureDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(new NodeId(0, 877), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode272() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 897),
            new QualifiedName(0, "SemanticChangeStructureDataType"),
            new LocalizedText("", "SemanticChangeStructureDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            SemanticChangeStructureDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(new NodeId(0, 897), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode273() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 884),
            new QualifiedName(0, "Range"),
            new LocalizedText("", "Range"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            Range.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(new NodeId(0, 884), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode274() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 887),
            new QualifiedName(0, "EUInformation"),
            new LocalizedText("", "EUInformation"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            EUInformation.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(new NodeId(0, 887), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode275() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 12077),
            new QualifiedName(0, "AxisScaleEnumeration"),
            new LocalizedText("", "AxisScaleEnumeration"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            AxisScaleEnumeration.definition());
    node.addReference(
        new Reference(
            new NodeId(0, 12077), new NodeId(0, 46), new NodeId(0, 12078).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12077), new NodeId(0, 45), new NodeId(0, 29).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode276() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 12171),
            new QualifiedName(0, "ComplexNumberType"),
            new LocalizedText("", "ComplexNumberType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            ComplexNumberType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 12171), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode277() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 12172),
            new QualifiedName(0, "DoubleComplexNumberType"),
            new LocalizedText("", "DoubleComplexNumberType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            DoubleComplexNumberType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 12172), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode278() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 12079),
            new QualifiedName(0, "AxisInformation"),
            new LocalizedText("", "AxisInformation"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            AxisInformation.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 12079), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode279() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 12080),
            new QualifiedName(0, "XVType"),
            new LocalizedText("", "XVType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            XVType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 12080), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode280() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 894),
            new QualifiedName(0, "ProgramDiagnosticDataType"),
            new LocalizedText("", "ProgramDiagnosticDataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            ProgramDiagnosticDataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(new NodeId(0, 894), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode281() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 24033),
            new QualifiedName(0, "ProgramDiagnostic2DataType"),
            new LocalizedText("", "ProgramDiagnostic2DataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            ProgramDiagnostic2DataType.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(
            new NodeId(0, 24033), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode282() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 891),
            new QualifiedName(0, "Annotation"),
            new LocalizedText("", "Annotation"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            Annotation.definition(context.getNamespaceTable()));
    node.addReference(
        new Reference(new NodeId(0, 891), new NodeId(0, 45), new NodeId(0, 22).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode283() {
    var node =
        new UaDataTypeNode(
            this.context,
            new NodeId(0, 890),
            new QualifiedName(0, "ExceptionDeviationFormat"),
            new LocalizedText("", "ExceptionDeviationFormat"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false,
            ExceptionDeviationFormat.definition());
    node.addReference(
        new Reference(new NodeId(0, 890), new NodeId(0, 46), new NodeId(0, 7614).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 890), new NodeId(0, 45), new NodeId(0, 29).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void load() throws Exception {
    loadNode0();
    loadNode1();
    loadNode2();
    loadNode3();
    loadNode4();
    loadNode5();
    loadNode6();
    loadNode7();
    loadNode8();
    loadNode9();
    loadNode10();
    loadNode11();
    loadNode12();
    loadNode13();
    loadNode14();
    loadNode15();
    loadNode16();
    loadNode17();
    loadNode18();
    loadNode19();
    loadNode20();
    loadNode21();
    loadNode22();
    loadNode23();
    loadNode24();
    loadNode25();
    loadNode26();
    loadNode27();
    loadNode28();
    loadNode29();
    loadNode30();
    loadNode31();
    loadNode32();
    loadNode33();
    loadNode34();
    loadNode35();
    loadNode36();
    loadNode37();
    loadNode38();
    loadNode39();
    loadNode40();
    loadNode41();
    loadNode42();
    loadNode43();
    loadNode44();
    loadNode45();
    loadNode46();
    loadNode47();
    loadNode48();
    loadNode49();
    loadNode50();
    loadNode51();
    loadNode52();
    loadNode53();
    loadNode54();
    loadNode55();
    loadNode56();
    loadNode57();
    loadNode58();
    loadNode59();
    loadNode60();
    loadNode61();
    loadNode62();
    loadNode63();
    loadNode64();
    loadNode65();
    loadNode66();
    loadNode67();
    loadNode68();
    loadNode69();
    loadNode70();
    loadNode71();
    loadNode72();
    loadNode73();
    loadNode74();
    loadNode75();
    loadNode76();
    loadNode77();
    loadNode78();
    loadNode79();
    loadNode80();
    loadNode81();
    loadNode82();
    loadNode83();
    loadNode84();
    loadNode85();
    loadNode86();
    loadNode87();
    loadNode88();
    loadNode89();
    loadNode90();
    loadNode91();
    loadNode92();
    loadNode93();
    loadNode94();
    loadNode95();
    loadNode96();
    loadNode97();
    loadNode98();
    loadNode99();
    loadNode100();
    loadNode101();
    loadNode102();
    loadNode103();
    loadNode104();
    loadNode105();
    loadNode106();
    loadNode107();
    loadNode108();
    loadNode109();
    loadNode110();
    loadNode111();
    loadNode112();
    loadNode113();
    loadNode114();
    loadNode115();
    loadNode116();
    loadNode117();
    loadNode118();
    loadNode119();
    loadNode120();
    loadNode121();
    loadNode122();
    loadNode123();
    loadNode124();
    loadNode125();
    loadNode126();
    loadNode127();
    loadNode128();
    loadNode129();
    loadNode130();
    loadNode131();
    loadNode132();
    loadNode133();
    loadNode134();
    loadNode135();
    loadNode136();
    loadNode137();
    loadNode138();
    loadNode139();
    loadNode140();
    loadNode141();
    loadNode142();
    loadNode143();
    loadNode144();
    loadNode145();
    loadNode146();
    loadNode147();
    loadNode148();
    loadNode149();
    loadNode150();
    loadNode151();
    loadNode152();
    loadNode153();
    loadNode154();
    loadNode155();
    loadNode156();
    loadNode157();
    loadNode158();
    loadNode159();
    loadNode160();
    loadNode161();
    loadNode162();
    loadNode163();
    loadNode164();
    loadNode165();
    loadNode166();
    loadNode167();
    loadNode168();
    loadNode169();
    loadNode170();
    loadNode171();
    loadNode172();
    loadNode173();
    loadNode174();
    loadNode175();
    loadNode176();
    loadNode177();
    loadNode178();
    loadNode179();
    loadNode180();
    loadNode181();
    loadNode182();
    loadNode183();
    loadNode184();
    loadNode185();
    loadNode186();
    loadNode187();
    loadNode188();
    loadNode189();
    loadNode190();
    loadNode191();
    loadNode192();
    loadNode193();
    loadNode194();
    loadNode195();
    loadNode196();
    loadNode197();
    loadNode198();
    loadNode199();
    loadNode200();
    loadNode201();
    loadNode202();
    loadNode203();
    loadNode204();
    loadNode205();
    loadNode206();
    loadNode207();
    loadNode208();
    loadNode209();
    loadNode210();
    loadNode211();
    loadNode212();
    loadNode213();
    loadNode214();
    loadNode215();
    loadNode216();
    loadNode217();
    loadNode218();
    loadNode219();
    loadNode220();
    loadNode221();
    loadNode222();
    loadNode223();
    loadNode224();
    loadNode225();
    loadNode226();
    loadNode227();
    loadNode228();
    loadNode229();
    loadNode230();
    loadNode231();
    loadNode232();
    loadNode233();
    loadNode234();
    loadNode235();
    loadNode236();
    loadNode237();
    loadNode238();
    loadNode239();
    loadNode240();
    loadNode241();
    loadNode242();
    loadNode243();
    loadNode244();
    loadNode245();
    loadNode246();
    loadNode247();
    loadNode248();
    loadNode249();
    loadNode250();
    loadNode251();
    loadNode252();
    loadNode253();
    loadNode254();
    loadNode255();
    loadNode256();
    loadNode257();
    loadNode258();
    loadNode259();
    loadNode260();
    loadNode261();
    loadNode262();
    loadNode263();
    loadNode264();
    loadNode265();
    loadNode266();
    loadNode267();
    loadNode268();
    loadNode269();
    loadNode270();
    loadNode271();
    loadNode272();
    loadNode273();
    loadNode274();
    loadNode275();
    loadNode276();
    loadNode277();
    loadNode278();
    loadNode279();
    loadNode280();
    loadNode281();
    loadNode282();
    loadNode283();
  }
}

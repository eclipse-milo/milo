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
import org.eclipse.milo.opcua.sdk.server.model.objects.AddressSpaceFileTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.AggregateConfigurationTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.AggregateFunctionTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.AlarmConditionTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.AlarmGroupTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.AliasNameCategoryTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.AliasNameTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.AlternativeUnitTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.ApplicationConfigurationFolderTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.ApplicationConfigurationTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.AuthorizationServiceConfigurationTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.BaseObjectTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.CertificateExpirationAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.CertificateGroupFolderTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.CertificateGroupTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.ConnectionTransportTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.DataSetFolderTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.DataSetReaderMessageTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.DataSetReaderTransportTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.DataSetReaderTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.DataSetWriterMessageTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.DataSetWriterTransportTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.DataSetWriterTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.DataTypeEncodingTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.DataTypeSystemTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.DictionaryEntryTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.DictionaryFolderTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.ExclusiveLimitStateMachineTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.ExtensionFieldsTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.FileDirectoryTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.FileTransferStateMachineTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.FileTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.FolderTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.HistoricalDataConfigurationTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.HistoricalEventConfigurationTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.HistoryServerCapabilitiesTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.InitialStateTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.KeyCredentialConfigurationFolderTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.KeyCredentialConfigurationTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.LldpInformationTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.LldpLocalSystemTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.LldpPortInformationTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.LldpRemoteStatisticsTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.LldpRemoteSystemTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.ModellingRuleTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.NamespaceMetadataTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.NamespacesTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.NetworkAddressTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.OperationLimitsTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.ProvisionableDeviceTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.PubSubCapabilitiesTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.PubSubConfigurationTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.PubSubConnectionTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.PubSubDiagnosticsConnectionTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.PubSubDiagnosticsDataSetReaderTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.PubSubDiagnosticsDataSetWriterTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.PubSubDiagnosticsReaderGroupTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.PubSubDiagnosticsRootTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.PubSubDiagnosticsWriterGroupTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.PubSubKeyPushTargetFolderTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.PubSubKeyPushTargetTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.PubSubStatusTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.PublishSubscribeTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.PublishedDataSetTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.QuantitiesFolderTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.QuantityTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.ReaderGroupMessageTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.ReaderGroupTransportTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.ReaderGroupTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.RoleSetTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.RoleTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.SecurityGroupFolderTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.SecurityGroupTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.ServerCapabilitiesTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.ServerConfigurationTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.ServerDiagnosticsTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.ServerRedundancyTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.ServerTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.ServerUnitTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.SessionDiagnosticsObjectTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.SessionsDiagnosticsSummaryTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.ShelvedStateMachineTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.StandaloneSubscribedDataSetTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.StateTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.SubscribedDataSetFolderTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.SubscribedDataSetTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.TransactionDiagnosticsTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.TransitionTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.TrustListOutOfDateAlarmTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.TrustListTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.UnitTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.UserManagementTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.VendorServerInfoTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.WriterGroupMessageTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.WriterGroupTransportTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.WriterGroupTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.DataTypeEncoding;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.PermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

class ObjectNodeLoader {
  private final UaNodeContext context;

  private final NodeManager<UaNode> nodeManager;

  ObjectNodeLoader(UaNodeContext context, NodeManager<UaNode> nodeManager) {
    this.context = context;
    this.nodeManager = nodeManager;
  }

  void loadNode0() {
    var node =
        new BaseObjectTypeNode(
            this.context,
            new NodeId(0, 3062),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            new LocalizedText("", "The default binary encoding for a data type."),
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 3062), new NodeId(0, 40), new NodeId(0, 58).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode1() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new BaseObjectTypeNode(
              this.context,
              new NodeId(0, 3063),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              new LocalizedText("", "The default XML encoding for a data type."),
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 3063), new NodeId(0, 40), new NodeId(0, 58).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode2() {
    var node =
        new ModellingRuleTypeNode(
            this.context,
            new NodeId(0, 78),
            new QualifiedName(0, "Mandatory"),
            new LocalizedText("", "Mandatory"),
            new LocalizedText(
                "",
                "Specifies that an instance with the attributes and references of the instance"
                    + " declaration must appear when a type is instantiated."),
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 78), new NodeId(0, 40), new NodeId(0, 77).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode3() {
    var node =
        new ModellingRuleTypeNode(
            this.context,
            new NodeId(0, 80),
            new QualifiedName(0, "Optional"),
            new LocalizedText("", "Optional"),
            new LocalizedText(
                "",
                "Specifies that an instance with the attributes and references of the instance"
                    + " declaration may appear when a type is instantiated."),
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 80), new NodeId(0, 40), new NodeId(0, 77).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode4() {
    var node =
        new ModellingRuleTypeNode(
            this.context,
            new NodeId(0, 83),
            new QualifiedName(0, "ExposesItsArray"),
            new LocalizedText("", "ExposesItsArray"),
            new LocalizedText(
                "",
                "Specifies that an instance appears for each element of the containing array"
                    + " variable."),
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 83), new NodeId(0, 40), new NodeId(0, 77).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode5() {
    var node =
        new ModellingRuleTypeNode(
            this.context,
            new NodeId(0, 11508),
            new QualifiedName(0, "OptionalPlaceholder"),
            new LocalizedText("", "OptionalPlaceholder"),
            new LocalizedText(
                "",
                "Specifies that zero or more instances with the attributes and references of the"
                    + " instance declaration may appear when a type is instantiated."),
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 11508), new NodeId(0, 40), new NodeId(0, 77).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode6() {
    var node =
        new ModellingRuleTypeNode(
            this.context,
            new NodeId(0, 11510),
            new QualifiedName(0, "MandatoryPlaceholder"),
            new LocalizedText("", "MandatoryPlaceholder"),
            new LocalizedText(
                "",
                "Specifies that one or more instances with the attributes and references of the"
                    + " instance declaration must appear when a type is instantiated."),
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 11510), new NodeId(0, 40), new NodeId(0, 77).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode7() {
    var node =
        new FolderTypeNode(
            this.context,
            new NodeId(0, 84),
            new QualifiedName(0, "Root"),
            new LocalizedText("", "Root"),
            new LocalizedText("", "The root of the server address space."),
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 84), new NodeId(0, 40), new NodeId(0, 61).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode8() {
    var node =
        new FolderTypeNode(
            this.context,
            new NodeId(0, 85),
            new QualifiedName(0, "Objects"),
            new LocalizedText("", "Objects"),
            new LocalizedText(
                "", "The browse entry point when looking for objects in the server address space."),
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 85), new NodeId(0, 35), new NodeId(0, 84).expanded(), false));
    node.addReference(
        new Reference(new NodeId(0, 85), new NodeId(0, 40), new NodeId(0, 61).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode9() {
    var node =
        new FolderTypeNode(
            this.context,
            new NodeId(0, 86),
            new QualifiedName(0, "Types"),
            new LocalizedText("", "Types"),
            new LocalizedText(
                "", "The browse entry point when looking for types in the server address space."),
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 86), new NodeId(0, 35), new NodeId(0, 84).expanded(), false));
    node.addReference(
        new Reference(new NodeId(0, 86), new NodeId(0, 40), new NodeId(0, 61).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode10() {
    var node =
        new FolderTypeNode(
            this.context,
            new NodeId(0, 87),
            new QualifiedName(0, "Views"),
            new LocalizedText("", "Views"),
            new LocalizedText(
                "", "The browse entry point when looking for views in the server address space."),
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 87), new NodeId(0, 35), new NodeId(0, 84).expanded(), false));
    node.addReference(
        new Reference(new NodeId(0, 87), new NodeId(0, 40), new NodeId(0, 61).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode11() {
    var node =
        new FolderTypeNode(
            this.context,
            new NodeId(0, 88),
            new QualifiedName(0, "ObjectTypes"),
            new LocalizedText("", "ObjectTypes"),
            new LocalizedText(
                "",
                "The browse entry point when looking for object types in the server address"
                    + " space."),
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 88), new NodeId(0, 35), new NodeId(0, 86).expanded(), false));
    node.addReference(
        new Reference(new NodeId(0, 88), new NodeId(0, 35), new NodeId(0, 58).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 88), new NodeId(0, 40), new NodeId(0, 61).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode12() {
    var node =
        new FolderTypeNode(
            this.context,
            new NodeId(0, 89),
            new QualifiedName(0, "VariableTypes"),
            new LocalizedText("", "VariableTypes"),
            new LocalizedText(
                "",
                "The browse entry point when looking for variable types in the server address"
                    + " space."),
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 89), new NodeId(0, 35), new NodeId(0, 86).expanded(), false));
    node.addReference(
        new Reference(new NodeId(0, 89), new NodeId(0, 35), new NodeId(0, 62).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 89), new NodeId(0, 40), new NodeId(0, 61).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode13() {
    var node =
        new FolderTypeNode(
            this.context,
            new NodeId(0, 90),
            new QualifiedName(0, "DataTypes"),
            new LocalizedText("", "DataTypes"),
            new LocalizedText(
                "",
                "The browse entry point when looking for data types in the server address space."),
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 90), new NodeId(0, 35), new NodeId(0, 86).expanded(), false));
    node.addReference(
        new Reference(new NodeId(0, 90), new NodeId(0, 35), new NodeId(0, 24).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 90), new NodeId(0, 40), new NodeId(0, 61).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode14() {
    var node =
        new FolderTypeNode(
            this.context,
            new NodeId(0, 91),
            new QualifiedName(0, "ReferenceTypes"),
            new LocalizedText("", "ReferenceTypes"),
            new LocalizedText(
                "",
                "The browse entry point when looking for reference types in the server address"
                    + " space."),
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 91), new NodeId(0, 35), new NodeId(0, 86).expanded(), false));
    node.addReference(
        new Reference(new NodeId(0, 91), new NodeId(0, 35), new NodeId(0, 31).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 91), new NodeId(0, 40), new NodeId(0, 61).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode15() {
    var node =
        new DataTypeSystemTypeNode(
            this.context,
            new NodeId(0, 92),
            new QualifiedName(0, "XML Schema"),
            new LocalizedText("", "XML Schema"),
            new LocalizedText(
                "", "A type system which uses XML schema to describe the encoding of data types."),
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 92), new NodeId(0, 35), new NodeId(0, 90).expanded(), false));
    node.addReference(
        new Reference(new NodeId(0, 92), new NodeId(0, 40), new NodeId(0, 75).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode16() {
    var node =
        new DataTypeSystemTypeNode(
            this.context,
            new NodeId(0, 93),
            new QualifiedName(0, "OPC Binary"),
            new LocalizedText("", "OPC Binary"),
            new LocalizedText(
                "",
                "A type system which uses OPC binary schema to describe the encoding of data"
                    + " types."),
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 93), new NodeId(0, 35), new NodeId(0, 90).expanded(), false));
    node.addReference(
        new Reference(new NodeId(0, 93), new NodeId(0, 40), new NodeId(0, 75).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode17() {
    var node =
        new NamespaceMetadataTypeNode(
            this.context,
            new NodeId(0, 15957),
            new QualifiedName(0, "http://opcfoundation.org/UA/"),
            new LocalizedText("", "http://opcfoundation.org/UA/"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 15957), new NodeId(0, 46), new NodeId(0, 15958).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15957), new NodeId(0, 46), new NodeId(0, 15959).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15957), new NodeId(0, 46), new NodeId(0, 15960).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15957), new NodeId(0, 46), new NodeId(0, 15961).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15957), new NodeId(0, 46), new NodeId(0, 15962).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15957), new NodeId(0, 46), new NodeId(0, 15963).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15957), new NodeId(0, 46), new NodeId(0, 15964).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15957), new NodeId(0, 46), new NodeId(0, 16134).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15957), new NodeId(0, 46), new NodeId(0, 16135).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15957), new NodeId(0, 46), new NodeId(0, 16136).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15957), new NodeId(0, 46), new NodeId(0, 32408).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15957), new NodeId(0, 47), new NodeId(0, 11715).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 15957), new NodeId(0, 40), new NodeId(0, 11616).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode18() {
    var node =
        new ServerCapabilitiesTypeNode(
            this.context,
            new NodeId(0, 2009),
            new QualifiedName(0, "ServerCapabilities"),
            new LocalizedText("", "ServerCapabilities"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 2009), new NodeId(0, 46), new NodeId(0, 3086).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2009), new NodeId(0, 46), new NodeId(0, 3087).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2009), new NodeId(0, 46), new NodeId(0, 3088).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2009), new NodeId(0, 46), new NodeId(0, 3089).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2009), new NodeId(0, 46), new NodeId(0, 3090).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2009), new NodeId(0, 46), new NodeId(0, 3091).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2009), new NodeId(0, 46), new NodeId(0, 3092).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2009), new NodeId(0, 47), new NodeId(0, 3093).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2009), new NodeId(0, 47), new NodeId(0, 3094).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2009), new NodeId(0, 40), new NodeId(0, 2013).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 2009), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2009), new NodeId(0, 47), new NodeId(0, 2004).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode19() {
    var node =
        new FolderTypeNode(
            this.context,
            new NodeId(0, 3093),
            new QualifiedName(0, "ModellingRules"),
            new LocalizedText("", "ModellingRules"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 3093), new NodeId(0, 40), new NodeId(0, 61).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 3093), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 3093), new NodeId(0, 47), new NodeId(0, 2009).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode20() {
    var node =
        new FolderTypeNode(
            this.context,
            new NodeId(0, 3094),
            new QualifiedName(0, "AggregateFunctions"),
            new LocalizedText("", "AggregateFunctions"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 3094), new NodeId(0, 40), new NodeId(0, 61).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 3094), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 3094), new NodeId(0, 47), new NodeId(0, 2009).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode21() {
    var node =
        new ServerDiagnosticsTypeNode(
            this.context,
            new NodeId(0, 2010),
            new QualifiedName(0, "ServerDiagnostics"),
            new LocalizedText("", "ServerDiagnostics"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 2010), new NodeId(0, 47), new NodeId(0, 3095).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2010), new NodeId(0, 47), new NodeId(0, 3110).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2010), new NodeId(0, 47), new NodeId(0, 3111).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2010), new NodeId(0, 46), new NodeId(0, 3114).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2010), new NodeId(0, 40), new NodeId(0, 2020).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 2010), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2010), new NodeId(0, 47), new NodeId(0, 2004).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode22() {
    var node =
        new SessionsDiagnosticsSummaryTypeNode(
            this.context,
            new NodeId(0, 3111),
            new QualifiedName(0, "SessionsDiagnosticsSummary"),
            new LocalizedText("", "SessionsDiagnosticsSummary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 3111), new NodeId(0, 47), new NodeId(0, 3112).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 3111), new NodeId(0, 47), new NodeId(0, 3113).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 3111), new NodeId(0, 40), new NodeId(0, 2026).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 3111), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 3111), new NodeId(0, 47), new NodeId(0, 2010).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode23() {
    var node =
        new VendorServerInfoTypeNode(
            this.context,
            new NodeId(0, 2011),
            new QualifiedName(0, "VendorServerInfo"),
            new LocalizedText("", "VendorServerInfo"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 2011), new NodeId(0, 40), new NodeId(0, 2033).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 2011), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2011), new NodeId(0, 47), new NodeId(0, 2004).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode24() {
    var node =
        new ServerRedundancyTypeNode(
            this.context,
            new NodeId(0, 2012),
            new QualifiedName(0, "ServerRedundancy"),
            new LocalizedText("", "ServerRedundancy"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 2012), new NodeId(0, 46), new NodeId(0, 3115).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2012), new NodeId(0, 40), new NodeId(0, 2034).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 2012), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2012), new NodeId(0, 47), new NodeId(0, 2004).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode25() {
    var node =
        new NamespacesTypeNode(
            this.context,
            new NodeId(0, 11527),
            new QualifiedName(0, "Namespaces"),
            new LocalizedText("", "Namespaces"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 11527), new NodeId(0, 40), new NodeId(0, 11645).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 11527), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11527), new NodeId(0, 47), new NodeId(0, 2004).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode26() {
    var node =
        new OperationLimitsTypeNode(
            this.context,
            new NodeId(0, 11551),
            new QualifiedName(0, "OperationLimits"),
            new LocalizedText("", "OperationLimits"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 11551), new NodeId(0, 40), new NodeId(0, 11564).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 11551), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11551), new NodeId(0, 47), new NodeId(0, 2013).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode27() {
    var node =
        new FolderTypeNode(
            this.context,
            new NodeId(0, 2019),
            new QualifiedName(0, "ModellingRules"),
            new LocalizedText("", "ModellingRules"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 2019), new NodeId(0, 40), new NodeId(0, 61).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 2019), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2019), new NodeId(0, 47), new NodeId(0, 2013).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode28() {
    var node =
        new FolderTypeNode(
            this.context,
            new NodeId(0, 2754),
            new QualifiedName(0, "AggregateFunctions"),
            new LocalizedText("", "AggregateFunctions"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 2754), new NodeId(0, 40), new NodeId(0, 61).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 2754), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2754), new NodeId(0, 47), new NodeId(0, 2013).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode29() {
    var node =
        new RoleSetTypeNode(
            this.context,
            new NodeId(0, 16295),
            new QualifiedName(0, "RoleSet"),
            new LocalizedText("", "RoleSet"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 16295), new NodeId(0, 47), new NodeId(0, 16296).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16295), new NodeId(0, 47), new NodeId(0, 16299).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16295), new NodeId(0, 40), new NodeId(0, 15607).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 16295), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16295), new NodeId(0, 47), new NodeId(0, 2013).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode30() {
    var node =
        new SessionsDiagnosticsSummaryTypeNode(
            this.context,
            new NodeId(0, 2744),
            new QualifiedName(0, "SessionsDiagnosticsSummary"),
            new LocalizedText("", "SessionsDiagnosticsSummary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 2744), new NodeId(0, 47), new NodeId(0, 3129).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2744), new NodeId(0, 47), new NodeId(0, 3130).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2744), new NodeId(0, 40), new NodeId(0, 2026).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 2744), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2744), new NodeId(0, 47), new NodeId(0, 2020).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode31() {
    var node =
        new SessionDiagnosticsObjectTypeNode(
            this.context,
            new NodeId(0, 12097),
            new QualifiedName(0, "<ClientName>"),
            new LocalizedText("", "<ClientName>"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 12097), new NodeId(0, 47), new NodeId(0, 12098).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12097), new NodeId(0, 47), new NodeId(0, 12142).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12097), new NodeId(0, 47), new NodeId(0, 12152).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12097), new NodeId(0, 40), new NodeId(0, 2029).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12097), new NodeId(0, 37), new NodeId(0, 11508).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12097), new NodeId(0, 47), new NodeId(0, 2026).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode32() {
    var node =
        new AddressSpaceFileTypeNode(
            this.context,
            new NodeId(0, 11624),
            new QualifiedName(0, "NamespaceFile"),
            new LocalizedText("", "NamespaceFile"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 11624), new NodeId(0, 46), new NodeId(0, 11625).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11624), new NodeId(0, 46), new NodeId(0, 12690).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11624), new NodeId(0, 46), new NodeId(0, 12691).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11624), new NodeId(0, 46), new NodeId(0, 11628).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11624), new NodeId(0, 47), new NodeId(0, 11629).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11624), new NodeId(0, 47), new NodeId(0, 11632).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11624), new NodeId(0, 47), new NodeId(0, 11634).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11624), new NodeId(0, 47), new NodeId(0, 11637).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11624), new NodeId(0, 47), new NodeId(0, 11639).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11624), new NodeId(0, 47), new NodeId(0, 11642).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11624), new NodeId(0, 40), new NodeId(0, 11595).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 11624), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11624), new NodeId(0, 47), new NodeId(0, 11616).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode33() {
    var node =
        new NamespaceMetadataTypeNode(
            this.context,
            new NodeId(0, 11646),
            new QualifiedName(0, "<NamespaceIdentifier>"),
            new LocalizedText("", "<NamespaceIdentifier>"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 11646), new NodeId(0, 46), new NodeId(0, 11647).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11646), new NodeId(0, 46), new NodeId(0, 11648).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11646), new NodeId(0, 46), new NodeId(0, 11649).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11646), new NodeId(0, 46), new NodeId(0, 11650).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11646), new NodeId(0, 46), new NodeId(0, 11651).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11646), new NodeId(0, 46), new NodeId(0, 11652).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11646), new NodeId(0, 46), new NodeId(0, 11653).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11646), new NodeId(0, 40), new NodeId(0, 11616).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11646), new NodeId(0, 37), new NodeId(0, 11508).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11646), new NodeId(0, 47), new NodeId(0, 11645).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode34() {
    var node =
        new FolderTypeNode(
            this.context,
            new NodeId(0, 3048),
            new QualifiedName(0, "EventTypes"),
            new LocalizedText("", "EventTypes"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 3048), new NodeId(0, 35), new NodeId(0, 86).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 3048), new NodeId(0, 35), new NodeId(0, 2041).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 3048), new NodeId(0, 40), new NodeId(0, 61).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode35() {
    var node =
        new FolderTypeNode(
            this.context,
            new NodeId(0, 31915),
            new QualifiedName(0, "Locations"),
            new LocalizedText("", "Locations"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 31915), new NodeId(0, 35), new NodeId(0, 85).expanded(), false));
    node.addReference(
        new Reference(new NodeId(0, 31915), new NodeId(0, 40), new NodeId(0, 61).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode36() {
    var node =
        new ServerTypeNode(
            this.context,
            new NodeId(0, 2253),
            new QualifiedName(0, "Server"),
            new LocalizedText("", "Server"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15644), new PermissionType(UInteger.valueOf(4097))),
              new RolePermissionType(
                  new NodeId(0, 15716), new PermissionType(UInteger.valueOf(65423))),
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(65423)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(1));
    node.addReference(
        new Reference(
            new NodeId(0, 2253), new NodeId(0, 46), new NodeId(0, 2254).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2253), new NodeId(0, 46), new NodeId(0, 2255).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2253), new NodeId(0, 46), new NodeId(0, 15004).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2253), new NodeId(0, 47), new NodeId(0, 2256).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2253), new NodeId(0, 46), new NodeId(0, 2267).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2253), new NodeId(0, 46), new NodeId(0, 2994).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2253), new NodeId(0, 46), new NodeId(0, 12885).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2253), new NodeId(0, 46), new NodeId(0, 17634).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2253), new NodeId(0, 47), new NodeId(0, 2268).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2253), new NodeId(0, 47), new NodeId(0, 2274).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2253), new NodeId(0, 47), new NodeId(0, 2295).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2253), new NodeId(0, 47), new NodeId(0, 2296).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2253), new NodeId(0, 47), new NodeId(0, 11715).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2253), new NodeId(0, 47), new NodeId(0, 11492).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2253), new NodeId(0, 47), new NodeId(0, 12873).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2253), new NodeId(0, 47), new NodeId(0, 12749).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2253), new NodeId(0, 47), new NodeId(0, 12886).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 2253), new NodeId(0, 35), new NodeId(0, 85).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 2253), new NodeId(0, 40), new NodeId(0, 2004).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode37() {
    var node =
        new ServerCapabilitiesTypeNode(
            this.context,
            new NodeId(0, 2268),
            new QualifiedName(0, "ServerCapabilities"),
            new LocalizedText("", "ServerCapabilities"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15644), new PermissionType(UInteger.valueOf(4097))),
              new RolePermissionType(
                  new NodeId(0, 15716), new PermissionType(UInteger.valueOf(65423))),
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(65423)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 2268), new NodeId(0, 46), new NodeId(0, 2269).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2268), new NodeId(0, 46), new NodeId(0, 2271).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2268), new NodeId(0, 46), new NodeId(0, 2272).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2268), new NodeId(0, 46), new NodeId(0, 2735).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2268), new NodeId(0, 46), new NodeId(0, 2736).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2268), new NodeId(0, 46), new NodeId(0, 2737).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2268), new NodeId(0, 46), new NodeId(0, 3704).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2268), new NodeId(0, 46), new NodeId(0, 11702).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2268), new NodeId(0, 46), new NodeId(0, 11703).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2268), new NodeId(0, 46), new NodeId(0, 12911).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2268), new NodeId(0, 47), new NodeId(0, 11704).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2268), new NodeId(0, 47), new NodeId(0, 2996).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2268), new NodeId(0, 47), new NodeId(0, 2997).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2268), new NodeId(0, 47), new NodeId(0, 15606).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2268), new NodeId(0, 46), new NodeId(0, 24095).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2268), new NodeId(0, 46), new NodeId(0, 24096).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2268), new NodeId(0, 46), new NodeId(0, 24097).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2268), new NodeId(0, 46), new NodeId(0, 24098).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2268), new NodeId(0, 46), new NodeId(0, 24104).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2268), new NodeId(0, 46), new NodeId(0, 24099).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2268), new NodeId(0, 46), new NodeId(0, 24100).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2268), new NodeId(0, 46), new NodeId(0, 31916).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2268), new NodeId(0, 46), new NodeId(0, 24101).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2268), new NodeId(0, 40), new NodeId(0, 2013).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2268), new NodeId(0, 47), new NodeId(0, 2253).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode38() {
    var node =
        new OperationLimitsTypeNode(
            this.context,
            new NodeId(0, 11704),
            new QualifiedName(0, "OperationLimits"),
            new LocalizedText("", "OperationLimits"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15644), new PermissionType(UInteger.valueOf(4097))),
              new RolePermissionType(
                  new NodeId(0, 15716), new PermissionType(UInteger.valueOf(65423))),
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(65423)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 11704), new NodeId(0, 46), new NodeId(0, 11705).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11704), new NodeId(0, 46), new NodeId(0, 12165).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11704), new NodeId(0, 46), new NodeId(0, 12166).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11704), new NodeId(0, 46), new NodeId(0, 11707).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11704), new NodeId(0, 46), new NodeId(0, 12167).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11704), new NodeId(0, 46), new NodeId(0, 12168).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11704), new NodeId(0, 46), new NodeId(0, 11709).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11704), new NodeId(0, 46), new NodeId(0, 11710).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11704), new NodeId(0, 46), new NodeId(0, 11711).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11704), new NodeId(0, 46), new NodeId(0, 11712).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11704), new NodeId(0, 46), new NodeId(0, 11713).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11704), new NodeId(0, 46), new NodeId(0, 11714).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11704), new NodeId(0, 40), new NodeId(0, 11564).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11704), new NodeId(0, 47), new NodeId(0, 2268).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode39() {
    var node =
        new FolderTypeNode(
            this.context,
            new NodeId(0, 2996),
            new QualifiedName(0, "ModellingRules"),
            new LocalizedText("", "ModellingRules"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15644), new PermissionType(UInteger.valueOf(4097))),
              new RolePermissionType(
                  new NodeId(0, 15716), new PermissionType(UInteger.valueOf(65423))),
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(65423)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 2996), new NodeId(0, 40), new NodeId(0, 61).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2996), new NodeId(0, 47), new NodeId(0, 2268).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode40() {
    var node =
        new FolderTypeNode(
            this.context,
            new NodeId(0, 2997),
            new QualifiedName(0, "AggregateFunctions"),
            new LocalizedText("", "AggregateFunctions"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15644), new PermissionType(UInteger.valueOf(4097))),
              new RolePermissionType(
                  new NodeId(0, 15716), new PermissionType(UInteger.valueOf(65423))),
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(65423)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 2997), new NodeId(0, 40), new NodeId(0, 61).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2997), new NodeId(0, 47), new NodeId(0, 2268).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode41() {
    var node =
        new RoleSetTypeNode(
            this.context,
            new NodeId(0, 15606),
            new QualifiedName(0, "RoleSet"),
            new LocalizedText("", "RoleSet"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(new NodeId(0, 15644), new PermissionType(UInteger.valueOf(1))),
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(65423)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 15606), new NodeId(0, 47), new NodeId(0, 16301).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15606), new NodeId(0, 47), new NodeId(0, 16304).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15606), new NodeId(0, 40), new NodeId(0, 15607).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15606), new NodeId(0, 47), new NodeId(0, 2268).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode42() {
    var node =
        new ServerDiagnosticsTypeNode(
            this.context,
            new NodeId(0, 2274),
            new QualifiedName(0, "ServerDiagnostics"),
            new LocalizedText("", "ServerDiagnostics"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15644), new PermissionType(UInteger.valueOf(4097))),
              new RolePermissionType(
                  new NodeId(0, 15716), new PermissionType(UInteger.valueOf(65423))),
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(65423)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 2274), new NodeId(0, 47), new NodeId(0, 2275).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2274), new NodeId(0, 47), new NodeId(0, 2289).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2274), new NodeId(0, 47), new NodeId(0, 2290).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2274), new NodeId(0, 47), new NodeId(0, 3706).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2274), new NodeId(0, 46), new NodeId(0, 2294).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2274), new NodeId(0, 40), new NodeId(0, 2020).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2274), new NodeId(0, 47), new NodeId(0, 2253).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode43() {
    var node =
        new SessionsDiagnosticsSummaryTypeNode(
            this.context,
            new NodeId(0, 3706),
            new QualifiedName(0, "SessionsDiagnosticsSummary"),
            new LocalizedText("", "SessionsDiagnosticsSummary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15644), new PermissionType(UInteger.valueOf(4097))),
              new RolePermissionType(
                  new NodeId(0, 15716), new PermissionType(UInteger.valueOf(65423))),
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(65423)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 3706), new NodeId(0, 47), new NodeId(0, 3707).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 3706), new NodeId(0, 47), new NodeId(0, 3708).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 3706), new NodeId(0, 40), new NodeId(0, 2026).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 3706), new NodeId(0, 47), new NodeId(0, 2274).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode44() {
    var node =
        new VendorServerInfoTypeNode(
            this.context,
            new NodeId(0, 2295),
            new QualifiedName(0, "VendorServerInfo"),
            new LocalizedText("", "VendorServerInfo"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15644), new PermissionType(UInteger.valueOf(4097))),
              new RolePermissionType(
                  new NodeId(0, 15716), new PermissionType(UInteger.valueOf(65423))),
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(65423)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 2295), new NodeId(0, 40), new NodeId(0, 2033).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2295), new NodeId(0, 47), new NodeId(0, 2253).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode45() {
    var node =
        new ServerRedundancyTypeNode(
            this.context,
            new NodeId(0, 2296),
            new QualifiedName(0, "ServerRedundancy"),
            new LocalizedText("", "ServerRedundancy"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15644), new PermissionType(UInteger.valueOf(4097))),
              new RolePermissionType(
                  new NodeId(0, 15716), new PermissionType(UInteger.valueOf(65423))),
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(65423)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 2296), new NodeId(0, 46), new NodeId(0, 3709).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2296), new NodeId(0, 46), new NodeId(0, 11313).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2296), new NodeId(0, 40), new NodeId(0, 2034).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2296), new NodeId(0, 47), new NodeId(0, 2253).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode46() {
    var node =
        new NamespacesTypeNode(
            this.context,
            new NodeId(0, 11715),
            new QualifiedName(0, "Namespaces"),
            new LocalizedText("", "Namespaces"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15644), new PermissionType(UInteger.valueOf(4097))),
              new RolePermissionType(
                  new NodeId(0, 15716), new PermissionType(UInteger.valueOf(65423))),
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(65423)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 11715), new NodeId(0, 40), new NodeId(0, 11645).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11715), new NodeId(0, 47), new NodeId(0, 2253).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode47() {
    var node =
        new HistoryServerCapabilitiesTypeNode(
            this.context,
            new NodeId(0, 11192),
            new QualifiedName(0, "HistoryServerCapabilities"),
            new LocalizedText("", "HistoryServerCapabilities"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 11192), new NodeId(0, 46), new NodeId(0, 11193).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11192), new NodeId(0, 46), new NodeId(0, 11242).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11192), new NodeId(0, 46), new NodeId(0, 11273).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11192), new NodeId(0, 46), new NodeId(0, 11274).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11192), new NodeId(0, 46), new NodeId(0, 11196).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11192), new NodeId(0, 46), new NodeId(0, 11197).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11192), new NodeId(0, 46), new NodeId(0, 11198).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11192), new NodeId(0, 46), new NodeId(0, 11199).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11192), new NodeId(0, 46), new NodeId(0, 11200).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11192), new NodeId(0, 46), new NodeId(0, 11281).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11192), new NodeId(0, 46), new NodeId(0, 11282).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11192), new NodeId(0, 46), new NodeId(0, 11283).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11192), new NodeId(0, 46), new NodeId(0, 11502).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11192), new NodeId(0, 46), new NodeId(0, 11275).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11192), new NodeId(0, 47), new NodeId(0, 11201).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11192), new NodeId(0, 46), new NodeId(0, 19091).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11192), new NodeId(0, 47), new NodeId(0, 2268).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 11192), new NodeId(0, 40), new NodeId(0, 2330).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode48() {
    var node =
        new FolderTypeNode(
            this.context,
            new NodeId(0, 11201),
            new QualifiedName(0, "AggregateFunctions"),
            new LocalizedText("", "AggregateFunctions"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 11201), new NodeId(0, 40), new NodeId(0, 61).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11201), new NodeId(0, 47), new NodeId(0, 11192).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode49() {
    var node =
        new FileDirectoryTypeNode(
            this.context,
            new NodeId(0, 13354),
            new QualifiedName(0, "<FileDirectoryName>"),
            new LocalizedText("", "<FileDirectoryName>"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 13354), new NodeId(0, 47), new NodeId(0, 13355).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13354), new NodeId(0, 47), new NodeId(0, 13358).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13354), new NodeId(0, 47), new NodeId(0, 17718).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13354), new NodeId(0, 47), new NodeId(0, 13363).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13354), new NodeId(0, 40), new NodeId(0, 13353).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13354), new NodeId(0, 37), new NodeId(0, 11508).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13354), new NodeId(0, 35), new NodeId(0, 13353).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode50() {
    var node =
        new FileTypeNode(
            this.context,
            new NodeId(0, 13366),
            new QualifiedName(0, "<FileName>"),
            new LocalizedText("", "<FileName>"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 13366), new NodeId(0, 46), new NodeId(0, 13367).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13366), new NodeId(0, 46), new NodeId(0, 13368).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13366), new NodeId(0, 46), new NodeId(0, 13369).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13366), new NodeId(0, 46), new NodeId(0, 13370).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13366), new NodeId(0, 47), new NodeId(0, 13372).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13366), new NodeId(0, 47), new NodeId(0, 13375).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13366), new NodeId(0, 47), new NodeId(0, 13377).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13366), new NodeId(0, 47), new NodeId(0, 13380).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13366), new NodeId(0, 47), new NodeId(0, 13382).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13366), new NodeId(0, 47), new NodeId(0, 13385).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13366), new NodeId(0, 40), new NodeId(0, 11575).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13366), new NodeId(0, 37), new NodeId(0, 11508).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13366), new NodeId(0, 35), new NodeId(0, 13353).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode51() {
    var node =
        new FileDirectoryTypeNode(
            this.context,
            new NodeId(0, 16314),
            new QualifiedName(0, "FileSystem"),
            new LocalizedText("", "FileSystem"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 16314), new NodeId(0, 47), new NodeId(0, 16348).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16314), new NodeId(0, 47), new NodeId(0, 16351).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16314), new NodeId(0, 47), new NodeId(0, 16354).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16314), new NodeId(0, 47), new NodeId(0, 16356).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16314), new NodeId(0, 40), new NodeId(0, 13353).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode52() {
    var node =
        new FileTransferStateMachineTypeNode(
            this.context,
            new NodeId(0, 15754),
            new QualifiedName(0, "<TransferState>"),
            new LocalizedText("", "<TransferState>"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 15754), new NodeId(0, 47), new NodeId(0, 15755).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15754), new NodeId(0, 47), new NodeId(0, 15794).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15754), new NodeId(0, 40), new NodeId(0, 15803).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15754), new NodeId(0, 37), new NodeId(0, 11508).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15754), new NodeId(0, 47), new NodeId(0, 15744).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode53() {
    var node =
        new InitialStateTypeNode(
            this.context,
            new NodeId(0, 15815),
            new QualifiedName(0, "Idle"),
            new LocalizedText("", "Idle"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 15815), new NodeId(0, 46), new NodeId(0, 15816).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15815), new NodeId(0, 51), new NodeId(0, 15825).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 15815), new NodeId(0, 52), new NodeId(0, 15829).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 15815), new NodeId(0, 51), new NodeId(0, 15831).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 15815), new NodeId(0, 52), new NodeId(0, 15833).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 15815), new NodeId(0, 52), new NodeId(0, 15841).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 15815), new NodeId(0, 40), new NodeId(0, 2309).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15815), new NodeId(0, 47), new NodeId(0, 15803).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode54() {
    var node =
        new StateTypeNode(
            this.context,
            new NodeId(0, 15817),
            new QualifiedName(0, "ReadPrepare"),
            new LocalizedText("", "ReadPrepare"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 15817), new NodeId(0, 46), new NodeId(0, 15818).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15817), new NodeId(0, 52), new NodeId(0, 15825).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 15817), new NodeId(0, 51), new NodeId(0, 15827).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 15817), new NodeId(0, 51), new NodeId(0, 15835).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 15817), new NodeId(0, 40), new NodeId(0, 2307).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15817), new NodeId(0, 47), new NodeId(0, 15803).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode55() {
    var node =
        new StateTypeNode(
            this.context,
            new NodeId(0, 15819),
            new QualifiedName(0, "ReadTransfer"),
            new LocalizedText("", "ReadTransfer"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 15819), new NodeId(0, 46), new NodeId(0, 15820).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15819), new NodeId(0, 52), new NodeId(0, 15827).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 15819), new NodeId(0, 51), new NodeId(0, 15829).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 15819), new NodeId(0, 51), new NodeId(0, 15837).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 15819), new NodeId(0, 40), new NodeId(0, 2307).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15819), new NodeId(0, 47), new NodeId(0, 15803).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode56() {
    var node =
        new StateTypeNode(
            this.context,
            new NodeId(0, 15821),
            new QualifiedName(0, "ApplyWrite"),
            new LocalizedText("", "ApplyWrite"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 15821), new NodeId(0, 46), new NodeId(0, 15822).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15821), new NodeId(0, 52), new NodeId(0, 15831).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 15821), new NodeId(0, 51), new NodeId(0, 15833).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 15821), new NodeId(0, 51), new NodeId(0, 15839).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 15821), new NodeId(0, 40), new NodeId(0, 2307).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15821), new NodeId(0, 47), new NodeId(0, 15803).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode57() {
    var node =
        new StateTypeNode(
            this.context,
            new NodeId(0, 15823),
            new QualifiedName(0, "Error"),
            new LocalizedText("", "Error"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 15823), new NodeId(0, 46), new NodeId(0, 15824).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15823), new NodeId(0, 52), new NodeId(0, 15835).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 15823), new NodeId(0, 52), new NodeId(0, 15837).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 15823), new NodeId(0, 52), new NodeId(0, 15839).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 15823), new NodeId(0, 51), new NodeId(0, 15841).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 15823), new NodeId(0, 40), new NodeId(0, 2307).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15823), new NodeId(0, 47), new NodeId(0, 15803).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode58() {
    var node =
        new TransitionTypeNode(
            this.context,
            new NodeId(0, 15825),
            new QualifiedName(0, "IdleToReadPrepare"),
            new LocalizedText("", "IdleToReadPrepare"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 15825), new NodeId(0, 46), new NodeId(0, 15826).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15825), new NodeId(0, 51), new NodeId(0, 15815).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15825), new NodeId(0, 52), new NodeId(0, 15817).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15825), new NodeId(0, 54), new NodeId(0, 2311).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15825), new NodeId(0, 40), new NodeId(0, 2310).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15825), new NodeId(0, 47), new NodeId(0, 15803).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode59() {
    var node =
        new TransitionTypeNode(
            this.context,
            new NodeId(0, 15827),
            new QualifiedName(0, "ReadPrepareToReadTransfer"),
            new LocalizedText("", "ReadPrepareToReadTransfer"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 15827), new NodeId(0, 46), new NodeId(0, 15828).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15827), new NodeId(0, 51), new NodeId(0, 15817).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15827), new NodeId(0, 52), new NodeId(0, 15819).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15827), new NodeId(0, 54), new NodeId(0, 2311).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15827), new NodeId(0, 40), new NodeId(0, 2310).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15827), new NodeId(0, 47), new NodeId(0, 15803).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode60() {
    var node =
        new TransitionTypeNode(
            this.context,
            new NodeId(0, 15829),
            new QualifiedName(0, "ReadTransferToIdle"),
            new LocalizedText("", "ReadTransferToIdle"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 15829), new NodeId(0, 46), new NodeId(0, 15830).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15829), new NodeId(0, 51), new NodeId(0, 15819).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15829), new NodeId(0, 52), new NodeId(0, 15815).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15829), new NodeId(0, 54), new NodeId(0, 2311).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15829), new NodeId(0, 40), new NodeId(0, 2310).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15829), new NodeId(0, 47), new NodeId(0, 15803).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode61() {
    var node =
        new TransitionTypeNode(
            this.context,
            new NodeId(0, 15831),
            new QualifiedName(0, "IdleToApplyWrite"),
            new LocalizedText("", "IdleToApplyWrite"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 15831), new NodeId(0, 46), new NodeId(0, 15832).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15831), new NodeId(0, 51), new NodeId(0, 15815).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15831), new NodeId(0, 52), new NodeId(0, 15821).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15831), new NodeId(0, 54), new NodeId(0, 2311).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15831), new NodeId(0, 40), new NodeId(0, 2310).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15831), new NodeId(0, 47), new NodeId(0, 15803).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode62() {
    var node =
        new TransitionTypeNode(
            this.context,
            new NodeId(0, 15833),
            new QualifiedName(0, "ApplyWriteToIdle"),
            new LocalizedText("", "ApplyWriteToIdle"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 15833), new NodeId(0, 46), new NodeId(0, 15834).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15833), new NodeId(0, 51), new NodeId(0, 15821).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15833), new NodeId(0, 52), new NodeId(0, 15815).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15833), new NodeId(0, 54), new NodeId(0, 2311).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15833), new NodeId(0, 40), new NodeId(0, 2310).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15833), new NodeId(0, 47), new NodeId(0, 15803).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode63() {
    var node =
        new TransitionTypeNode(
            this.context,
            new NodeId(0, 15835),
            new QualifiedName(0, "ReadPrepareToError"),
            new LocalizedText("", "ReadPrepareToError"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 15835), new NodeId(0, 46), new NodeId(0, 15836).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15835), new NodeId(0, 51), new NodeId(0, 15817).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15835), new NodeId(0, 52), new NodeId(0, 15823).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15835), new NodeId(0, 54), new NodeId(0, 2311).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15835), new NodeId(0, 40), new NodeId(0, 2310).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15835), new NodeId(0, 47), new NodeId(0, 15803).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode64() {
    var node =
        new TransitionTypeNode(
            this.context,
            new NodeId(0, 15837),
            new QualifiedName(0, "ReadTransferToError"),
            new LocalizedText("", "ReadTransferToError"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 15837), new NodeId(0, 46), new NodeId(0, 15838).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15837), new NodeId(0, 51), new NodeId(0, 15819).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15837), new NodeId(0, 52), new NodeId(0, 15823).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15837), new NodeId(0, 54), new NodeId(0, 2311).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15837), new NodeId(0, 40), new NodeId(0, 2310).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15837), new NodeId(0, 47), new NodeId(0, 15803).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode65() {
    var node =
        new TransitionTypeNode(
            this.context,
            new NodeId(0, 15839),
            new QualifiedName(0, "ApplyWriteToError"),
            new LocalizedText("", "ApplyWriteToError"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 15839), new NodeId(0, 46), new NodeId(0, 15840).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15839), new NodeId(0, 51), new NodeId(0, 15821).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15839), new NodeId(0, 52), new NodeId(0, 15823).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15839), new NodeId(0, 54), new NodeId(0, 2311).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15839), new NodeId(0, 40), new NodeId(0, 2310).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15839), new NodeId(0, 47), new NodeId(0, 15803).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode66() {
    var node =
        new TransitionTypeNode(
            this.context,
            new NodeId(0, 15841),
            new QualifiedName(0, "ErrorToIdle"),
            new LocalizedText("", "ErrorToIdle"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 15841), new NodeId(0, 46), new NodeId(0, 15842).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15841), new NodeId(0, 51), new NodeId(0, 15823).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15841), new NodeId(0, 52), new NodeId(0, 15815).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15841), new NodeId(0, 54), new NodeId(0, 2311).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15841), new NodeId(0, 40), new NodeId(0, 2310).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15841), new NodeId(0, 47), new NodeId(0, 15803).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode67() {
    var node =
        new RoleTypeNode(
            this.context,
            new NodeId(0, 15608),
            new QualifiedName(0, "<RoleName>"),
            new LocalizedText("", "<RoleName>"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 15608), new NodeId(0, 46), new NodeId(0, 16162).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15608), new NodeId(0, 40), new NodeId(0, 15620).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15608), new NodeId(0, 37), new NodeId(0, 11508).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15608), new NodeId(0, 47), new NodeId(0, 15607).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode68() {
    var node =
        new RoleTypeNode(
            this.context,
            new NodeId(0, 15644),
            new QualifiedName(0, "Anonymous"),
            new LocalizedText("", "Anonymous"),
            new LocalizedText(
                "",
                "The Role has very limited access for use when a Session has anonymous"
                    + " credentials."),
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(new NodeId(0, 15644), new PermissionType(UInteger.valueOf(1))),
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(65423)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 15644), new NodeId(0, 46), new NodeId(0, 16192).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15644), new NodeId(0, 47), new NodeId(0, 15606).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 15644), new NodeId(0, 40), new NodeId(0, 15620).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode69() {
    var node =
        new RoleTypeNode(
            this.context,
            new NodeId(0, 15656),
            new QualifiedName(0, "AuthenticatedUser"),
            new LocalizedText("", "AuthenticatedUser"),
            new LocalizedText(
                "",
                "The Role has limited access for use when a Session has valid non-anonymous"
                    + " credentials but has not been explicitly granted access to a Role."),
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(new NodeId(0, 15644), new PermissionType(UInteger.valueOf(1))),
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(65423)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 15656), new NodeId(0, 46), new NodeId(0, 16203).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15656), new NodeId(0, 47), new NodeId(0, 15606).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 15656), new NodeId(0, 40), new NodeId(0, 15620).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode70() {
    var node =
        new RoleTypeNode(
            this.context,
            new NodeId(0, 18625),
            new QualifiedName(0, "TrustedApplication"),
            new LocalizedText("", "TrustedApplication"),
            new LocalizedText(
                "", "The Role has limited access for use when a Client has been authenticated."),
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(new NodeId(0, 15644), new PermissionType(UInteger.valueOf(1))),
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(65423)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 18625), new NodeId(0, 46), new NodeId(0, 18626).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18625), new NodeId(0, 47), new NodeId(0, 15606).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 18625), new NodeId(0, 40), new NodeId(0, 15620).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode71() {
    var node =
        new RoleTypeNode(
            this.context,
            new NodeId(0, 15668),
            new QualifiedName(0, "Observer"),
            new LocalizedText("", "Observer"),
            new LocalizedText(
                "",
                "The Role is allowed to browse, read live data, read historical data/events or"
                    + " subscribe to data/events."),
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(new NodeId(0, 15644), new PermissionType(UInteger.valueOf(1))),
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(65423)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 15668), new NodeId(0, 46), new NodeId(0, 16214).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15668), new NodeId(0, 46), new NodeId(0, 15416).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15668), new NodeId(0, 46), new NodeId(0, 16215).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15668), new NodeId(0, 46), new NodeId(0, 15417).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15668), new NodeId(0, 46), new NodeId(0, 16216).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15668), new NodeId(0, 46), new NodeId(0, 24142).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15668), new NodeId(0, 47), new NodeId(0, 15672).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15668), new NodeId(0, 47), new NodeId(0, 15674).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15668), new NodeId(0, 47), new NodeId(0, 16217).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15668), new NodeId(0, 47), new NodeId(0, 16219).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15668), new NodeId(0, 47), new NodeId(0, 16221).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15668), new NodeId(0, 47), new NodeId(0, 16223).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15668), new NodeId(0, 47), new NodeId(0, 15606).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 15668), new NodeId(0, 40), new NodeId(0, 15620).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode72() {
    var node =
        new RoleTypeNode(
            this.context,
            new NodeId(0, 15680),
            new QualifiedName(0, "Operator"),
            new LocalizedText("", "Operator"),
            new LocalizedText(
                "",
                "The Role is allowed to browse, read live data, read historical data/events or"
                    + " subscribe to data/events."),
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(new NodeId(0, 15644), new PermissionType(UInteger.valueOf(1))),
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(65423)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 15680), new NodeId(0, 46), new NodeId(0, 16225).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15680), new NodeId(0, 46), new NodeId(0, 15418).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15680), new NodeId(0, 46), new NodeId(0, 16226).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15680), new NodeId(0, 46), new NodeId(0, 15423).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15680), new NodeId(0, 46), new NodeId(0, 16227).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15680), new NodeId(0, 46), new NodeId(0, 24143).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15680), new NodeId(0, 47), new NodeId(0, 15684).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15680), new NodeId(0, 47), new NodeId(0, 15686).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15680), new NodeId(0, 47), new NodeId(0, 16228).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15680), new NodeId(0, 47), new NodeId(0, 16230).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15680), new NodeId(0, 47), new NodeId(0, 16232).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15680), new NodeId(0, 47), new NodeId(0, 16234).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15680), new NodeId(0, 47), new NodeId(0, 15606).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 15680), new NodeId(0, 40), new NodeId(0, 15620).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode73() {
    var node =
        new RoleTypeNode(
            this.context,
            new NodeId(0, 16036),
            new QualifiedName(0, "Engineer"),
            new LocalizedText("", "Engineer"),
            new LocalizedText(
                "",
                "The Role is allowed to browse, read live data, read and update historical"
                    + " data/events, call methods or subscribe to data/events."),
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(new NodeId(0, 15644), new PermissionType(UInteger.valueOf(1))),
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(65423)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 16036), new NodeId(0, 46), new NodeId(0, 16236).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16036), new NodeId(0, 46), new NodeId(0, 15424).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16036), new NodeId(0, 46), new NodeId(0, 16237).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16036), new NodeId(0, 46), new NodeId(0, 15425).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16036), new NodeId(0, 46), new NodeId(0, 16238).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16036), new NodeId(0, 46), new NodeId(0, 24144).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16036), new NodeId(0, 47), new NodeId(0, 16041).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16036), new NodeId(0, 47), new NodeId(0, 16043).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16036), new NodeId(0, 47), new NodeId(0, 16239).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16036), new NodeId(0, 47), new NodeId(0, 16241).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16036), new NodeId(0, 47), new NodeId(0, 16243).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16036), new NodeId(0, 47), new NodeId(0, 16245).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16036), new NodeId(0, 47), new NodeId(0, 15606).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 16036), new NodeId(0, 40), new NodeId(0, 15620).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode74() {
    var node =
        new RoleTypeNode(
            this.context,
            new NodeId(0, 15692),
            new QualifiedName(0, "Supervisor"),
            new LocalizedText("", "Supervisor"),
            new LocalizedText(
                "",
                "The Role is allowed to browse, read live data, read and historical data/events,"
                    + " call methods or subscribe to data/events."),
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(new NodeId(0, 15644), new PermissionType(UInteger.valueOf(1))),
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(65423)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 15692), new NodeId(0, 46), new NodeId(0, 16247).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15692), new NodeId(0, 46), new NodeId(0, 15426).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15692), new NodeId(0, 46), new NodeId(0, 16248).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15692), new NodeId(0, 46), new NodeId(0, 15427).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15692), new NodeId(0, 46), new NodeId(0, 16249).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15692), new NodeId(0, 46), new NodeId(0, 24145).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15692), new NodeId(0, 47), new NodeId(0, 15696).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15692), new NodeId(0, 47), new NodeId(0, 15698).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15692), new NodeId(0, 47), new NodeId(0, 16250).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15692), new NodeId(0, 47), new NodeId(0, 16252).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15692), new NodeId(0, 47), new NodeId(0, 16254).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15692), new NodeId(0, 47), new NodeId(0, 16256).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15692), new NodeId(0, 47), new NodeId(0, 15606).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 15692), new NodeId(0, 40), new NodeId(0, 15620).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode75() {
    var node =
        new RoleTypeNode(
            this.context,
            new NodeId(0, 15716),
            new QualifiedName(0, "ConfigureAdmin"),
            new LocalizedText("", "ConfigureAdmin"),
            new LocalizedText(
                "",
                "The Role is allowed to change the non-security related configuration settings."),
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(new NodeId(0, 15644), new PermissionType(UInteger.valueOf(1))),
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(65423)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 15716), new NodeId(0, 46), new NodeId(0, 16269).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15716), new NodeId(0, 46), new NodeId(0, 15428).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15716), new NodeId(0, 46), new NodeId(0, 16270).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15716), new NodeId(0, 46), new NodeId(0, 15429).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15716), new NodeId(0, 46), new NodeId(0, 16271).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15716), new NodeId(0, 46), new NodeId(0, 24146).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15716), new NodeId(0, 47), new NodeId(0, 15720).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15716), new NodeId(0, 47), new NodeId(0, 15722).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15716), new NodeId(0, 47), new NodeId(0, 16272).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15716), new NodeId(0, 47), new NodeId(0, 16274).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15716), new NodeId(0, 47), new NodeId(0, 16276).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15716), new NodeId(0, 47), new NodeId(0, 16278).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15716), new NodeId(0, 47), new NodeId(0, 15606).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 15716), new NodeId(0, 40), new NodeId(0, 15620).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode76() {
    var node =
        new RoleTypeNode(
            this.context,
            new NodeId(0, 15704),
            new QualifiedName(0, "SecurityAdmin"),
            new LocalizedText("", "SecurityAdmin"),
            new LocalizedText("", "The Role is allowed to change security related settings."),
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(new NodeId(0, 15644), new PermissionType(UInteger.valueOf(1))),
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(65423)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 15704), new NodeId(0, 46), new NodeId(0, 16258).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15704), new NodeId(0, 46), new NodeId(0, 15430).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15704), new NodeId(0, 46), new NodeId(0, 16259).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15704), new NodeId(0, 46), new NodeId(0, 15527).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15704), new NodeId(0, 46), new NodeId(0, 16260).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15704), new NodeId(0, 46), new NodeId(0, 24147).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15704), new NodeId(0, 47), new NodeId(0, 15708).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15704), new NodeId(0, 47), new NodeId(0, 15710).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15704), new NodeId(0, 47), new NodeId(0, 16261).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15704), new NodeId(0, 47), new NodeId(0, 16263).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15704), new NodeId(0, 47), new NodeId(0, 16265).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15704), new NodeId(0, 47), new NodeId(0, 16267).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15704), new NodeId(0, 47), new NodeId(0, 15606).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 15704), new NodeId(0, 40), new NodeId(0, 15620).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode77() {
    var node =
        new RoleTypeNode(
            this.context,
            new NodeId(0, 25565),
            new QualifiedName(0, "SecurityKeyServerAdmin"),
            new LocalizedText("", "SecurityKeyServerAdmin"),
            new LocalizedText(
                "",
                "This Role allows an administrator to manage SecurityGroups and PushTargets on a"
                    + " SKS. This includes executing methods related to management of"
                    + " SecurityGroups and PushTargets on an SKS."),
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(new NodeId(0, 15644), new PermissionType(UInteger.valueOf(1))),
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(65423)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 25565), new NodeId(0, 46), new NodeId(0, 25566).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25565), new NodeId(0, 46), new NodeId(0, 25567).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25565), new NodeId(0, 46), new NodeId(0, 25568).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25565), new NodeId(0, 46), new NodeId(0, 25569).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25565), new NodeId(0, 46), new NodeId(0, 25570).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25565), new NodeId(0, 46), new NodeId(0, 25571).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25565), new NodeId(0, 47), new NodeId(0, 25572).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25565), new NodeId(0, 47), new NodeId(0, 25574).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25565), new NodeId(0, 47), new NodeId(0, 25576).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25565), new NodeId(0, 47), new NodeId(0, 25578).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25565), new NodeId(0, 47), new NodeId(0, 25580).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25565), new NodeId(0, 47), new NodeId(0, 25582).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25565), new NodeId(0, 47), new NodeId(0, 15606).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 25565), new NodeId(0, 40), new NodeId(0, 15620).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode78() {
    var node =
        new RoleTypeNode(
            this.context,
            new NodeId(0, 25603),
            new QualifiedName(0, "SecurityKeyServerAccess"),
            new LocalizedText("", "SecurityKeyServerAccess"),
            new LocalizedText(
                "",
                "This Role allows a PubSub Application to access an SKS to pull keys. It is the"
                    + " default Role for pull but it is expected that different custom Roles are"
                    + " used for different SecurityGroups."),
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(new NodeId(0, 15644), new PermissionType(UInteger.valueOf(1))),
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(65423)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 25603), new NodeId(0, 46), new NodeId(0, 25604).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25603), new NodeId(0, 46), new NodeId(0, 25605).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25603), new NodeId(0, 46), new NodeId(0, 25606).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25603), new NodeId(0, 46), new NodeId(0, 25607).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25603), new NodeId(0, 46), new NodeId(0, 25608).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25603), new NodeId(0, 46), new NodeId(0, 25609).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25603), new NodeId(0, 47), new NodeId(0, 25610).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25603), new NodeId(0, 47), new NodeId(0, 25612).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25603), new NodeId(0, 47), new NodeId(0, 25614).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25603), new NodeId(0, 47), new NodeId(0, 25616).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25603), new NodeId(0, 47), new NodeId(0, 25618).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25603), new NodeId(0, 47), new NodeId(0, 25620).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25603), new NodeId(0, 47), new NodeId(0, 15606).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 25603), new NodeId(0, 40), new NodeId(0, 15620).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode79() {
    var node =
        new RoleTypeNode(
            this.context,
            new NodeId(0, 25584),
            new QualifiedName(0, "SecurityKeyServerPush"),
            new LocalizedText("", "SecurityKeyServerPush"),
            new LocalizedText(
                "",
                "This Role allows an SKS to push security keys to PubSub Applications. This"
                    + " includes executing methods related to PubSub security."),
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(new NodeId(0, 15644), new PermissionType(UInteger.valueOf(1))),
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(65423)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 25584), new NodeId(0, 46), new NodeId(0, 25585).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25584), new NodeId(0, 46), new NodeId(0, 25586).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25584), new NodeId(0, 46), new NodeId(0, 25587).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25584), new NodeId(0, 46), new NodeId(0, 25588).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25584), new NodeId(0, 46), new NodeId(0, 25589).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25584), new NodeId(0, 46), new NodeId(0, 25590).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25584), new NodeId(0, 47), new NodeId(0, 25591).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25584), new NodeId(0, 47), new NodeId(0, 25593).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25584), new NodeId(0, 47), new NodeId(0, 25595).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25584), new NodeId(0, 47), new NodeId(0, 25597).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25584), new NodeId(0, 47), new NodeId(0, 25599).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25584), new NodeId(0, 47), new NodeId(0, 25601).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25584), new NodeId(0, 47), new NodeId(0, 15606).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 25584), new NodeId(0, 40), new NodeId(0, 15620).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode80() {
    var node =
        new DictionaryEntryTypeNode(
            this.context,
            new NodeId(0, 17590),
            new QualifiedName(0, "<DictionaryEntryName>"),
            new LocalizedText("", "<DictionaryEntryName>"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 17590), new NodeId(0, 40), new NodeId(0, 17589).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17590), new NodeId(0, 37), new NodeId(0, 11508).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17590), new NodeId(0, 47), new NodeId(0, 17589).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode81() {
    var node =
        new DictionaryFolderTypeNode(
            this.context,
            new NodeId(0, 17592),
            new QualifiedName(0, "<DictionaryFolderName>"),
            new LocalizedText("", "<DictionaryFolderName>"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 17592), new NodeId(0, 40), new NodeId(0, 17591).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17592), new NodeId(0, 37), new NodeId(0, 11508).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17592), new NodeId(0, 47), new NodeId(0, 17591).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode82() {
    var node =
        new DictionaryEntryTypeNode(
            this.context,
            new NodeId(0, 17593),
            new QualifiedName(0, "<DictionaryEntryName>"),
            new LocalizedText("", "<DictionaryEntryName>"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 17593), new NodeId(0, 40), new NodeId(0, 17589).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17593), new NodeId(0, 37), new NodeId(0, 11508).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17593), new NodeId(0, 47), new NodeId(0, 17591).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode83() {
    var node =
        new DictionaryFolderTypeNode(
            this.context,
            new NodeId(0, 17594),
            new QualifiedName(0, "Dictionaries"),
            new LocalizedText("", "Dictionaries"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 17594), new NodeId(0, 47), new NodeId(0, 2253).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 17594), new NodeId(0, 40), new NodeId(0, 17591).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode84() {
    var node =
        new FolderTypeNode(
            this.context,
            new NodeId(0, 17708),
            new QualifiedName(0, "InterfaceTypes"),
            new LocalizedText("", "InterfaceTypes"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 17708), new NodeId(0, 35), new NodeId(0, 17602).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17708), new NodeId(0, 35), new NodeId(0, 86).expanded(), false));
    node.addReference(
        new Reference(new NodeId(0, 17708), new NodeId(0, 40), new NodeId(0, 61).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode85() {
    var node =
        new BaseObjectTypeNode(
            this.context,
            new NodeId(0, 23519),
            new QualifiedName(0, "<OrderedObject>"),
            new LocalizedText("", "<OrderedObject>"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 23519), new NodeId(0, 46), new NodeId(0, 23521).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23519), new NodeId(0, 17603), new NodeId(0, 23513).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 23519), new NodeId(0, 40), new NodeId(0, 58).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23519), new NodeId(0, 37), new NodeId(0, 11508).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23519), new NodeId(0, 49), new NodeId(0, 23518).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode86() {
    var node =
        new BaseObjectTypeNode(
            this.context,
            new NodeId(0, 32452),
            new QualifiedName(0, "AlternativeUnits"),
            new LocalizedText("", "AlternativeUnits"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 32452), new NodeId(0, 47), new NodeId(0, 32587).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 32452), new NodeId(0, 40), new NodeId(0, 58).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 32452), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32452), new NodeId(0, 47), new NodeId(0, 32447).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode87() {
    var node =
        new AlternativeUnitTypeNode(
            this.context,
            new NodeId(0, 32587),
            new QualifiedName(0, "<AlternativeUnit>"),
            new LocalizedText("", "<AlternativeUnit>"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 32587), new NodeId(0, 46), new NodeId(0, 32588).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32587), new NodeId(0, 46), new NodeId(0, 32590).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32587), new NodeId(0, 40), new NodeId(0, 32467).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32587), new NodeId(0, 37), new NodeId(0, 11510).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32587), new NodeId(0, 47), new NodeId(0, 32452).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode88() {
    var node =
        new UnitTypeNode(
            this.context,
            new NodeId(0, 32462),
            new QualifiedName(0, "CoherentUnit"),
            new LocalizedText("", "CoherentUnit"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 32462), new NodeId(0, 46), new NodeId(0, 32463).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32462), new NodeId(0, 46), new NodeId(0, 32465).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32462), new NodeId(0, 40), new NodeId(0, 32442).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 32462), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32462), new NodeId(0, 47), new NodeId(0, 32447).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode89() {
    var node =
        new BaseObjectTypeNode(
            this.context,
            new NodeId(0, 32481),
            new QualifiedName(0, "ServerUnits"),
            new LocalizedText("", "ServerUnits"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 32481), new NodeId(0, 47), new NodeId(0, 32482).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 32481), new NodeId(0, 40), new NodeId(0, 58).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 32481), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32481), new NodeId(0, 47), new NodeId(0, 32475).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode90() {
    var node =
        new ServerUnitTypeNode(
            this.context,
            new NodeId(0, 32482),
            new QualifiedName(0, "<ServerUnit>"),
            new LocalizedText("", "<ServerUnit>"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 32482), new NodeId(0, 46), new NodeId(0, 32483).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32482), new NodeId(0, 46), new NodeId(0, 32485).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32482), new NodeId(0, 46), new NodeId(0, 32496).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32482), new NodeId(0, 40), new NodeId(0, 32447).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32482), new NodeId(0, 37), new NodeId(0, 11510).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32482), new NodeId(0, 47), new NodeId(0, 32481).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode91() {
    var node =
        new QuantityTypeNode(
            this.context,
            new NodeId(0, 32503),
            new QualifiedName(0, "<Quantity>"),
            new LocalizedText("", "<Quantity>"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 32503), new NodeId(0, 46), new NodeId(0, 32508).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32503), new NodeId(0, 47), new NodeId(0, 32509).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32503), new NodeId(0, 40), new NodeId(0, 32475).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32503), new NodeId(0, 37), new NodeId(0, 11508).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32503), new NodeId(0, 35), new NodeId(0, 32502).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode92() {
    var node =
        new BaseObjectTypeNode(
            this.context,
            new NodeId(0, 32509),
            new QualifiedName(0, "ServerUnits"),
            new LocalizedText("", "ServerUnits"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 32509), new NodeId(0, 40), new NodeId(0, 58).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 32509), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32509), new NodeId(0, 47), new NodeId(0, 32503).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode93() {
    var node =
        new QuantitiesFolderTypeNode(
            this.context,
            new NodeId(0, 32530),
            new QualifiedName(0, "Quantities"),
            new LocalizedText("", "Quantities"),
            new LocalizedText(
                "",
                "This Object is the entry point to quantities and their units managed in the"
                    + " Server."),
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 32530), new NodeId(0, 35), new NodeId(0, 2253).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 32530), new NodeId(0, 40), new NodeId(0, 32502).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode94() {
    var node =
        new ShelvedStateMachineTypeNode(
            this.context,
            new NodeId(0, 9178),
            new QualifiedName(0, "ShelvingState"),
            new LocalizedText("", "ShelvingState"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 9178), new NodeId(0, 47), new NodeId(0, 9179).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 9178), new NodeId(0, 47), new NodeId(0, 9184).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 9178), new NodeId(0, 46), new NodeId(0, 9189).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 9178), new NodeId(0, 47), new NodeId(0, 9213).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 9178), new NodeId(0, 47), new NodeId(0, 9211).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 9178), new NodeId(0, 47), new NodeId(0, 9212).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 9178), new NodeId(0, 9004), new NodeId(0, 9118).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 9178), new NodeId(0, 40), new NodeId(0, 2929).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 9178), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 9178), new NodeId(0, 47), new NodeId(0, 2915).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode95() {
    var node =
        new AlarmGroupTypeNode(
            this.context,
            new NodeId(0, 16398),
            new QualifiedName(0, "FirstInGroup"),
            new LocalizedText("", "FirstInGroup"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 16398), new NodeId(0, 40), new NodeId(0, 16405).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 16398), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16398), new NodeId(0, 47), new NodeId(0, 2915).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode96() {
    var node =
        new AlarmGroupTypeNode(
            this.context,
            new NodeId(0, 16399),
            new QualifiedName(0, "<AlarmGroup>"),
            new LocalizedText("", "<AlarmGroup>"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 16399), new NodeId(0, 40), new NodeId(0, 16405).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16399), new NodeId(0, 37), new NodeId(0, 11508).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16399), new NodeId(0, 16361), new NodeId(0, 2915).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode97() {
    var node =
        new AlarmConditionTypeNode(
            this.context,
            new NodeId(0, 16406),
            new QualifiedName(0, "<AlarmConditionInstance>"),
            new LocalizedText("", "<AlarmConditionInstance>"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 16406), new NodeId(0, 46), new NodeId(0, 16407).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16406), new NodeId(0, 46), new NodeId(0, 16408).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16406), new NodeId(0, 46), new NodeId(0, 16409).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16406), new NodeId(0, 46), new NodeId(0, 16410).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16406), new NodeId(0, 46), new NodeId(0, 16411).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16406), new NodeId(0, 46), new NodeId(0, 16412).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16406), new NodeId(0, 46), new NodeId(0, 16414).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16406), new NodeId(0, 46), new NodeId(0, 16415).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16406), new NodeId(0, 46), new NodeId(0, 16416).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16406), new NodeId(0, 46), new NodeId(0, 16417).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16406), new NodeId(0, 46), new NodeId(0, 16420).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16406), new NodeId(0, 46), new NodeId(0, 16421).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16406), new NodeId(0, 46), new NodeId(0, 16422).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16406), new NodeId(0, 47), new NodeId(0, 16423).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16406), new NodeId(0, 47), new NodeId(0, 16432).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16406), new NodeId(0, 47), new NodeId(0, 16434).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16406), new NodeId(0, 47), new NodeId(0, 16436).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16406), new NodeId(0, 46), new NodeId(0, 16438).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16406), new NodeId(0, 47), new NodeId(0, 16439).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16406), new NodeId(0, 47), new NodeId(0, 16440).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16406), new NodeId(0, 47), new NodeId(0, 16441).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16406), new NodeId(0, 47), new NodeId(0, 16443).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16406), new NodeId(0, 47), new NodeId(0, 16461).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16406), new NodeId(0, 47), new NodeId(0, 16465).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16406), new NodeId(0, 46), new NodeId(0, 16474).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16406), new NodeId(0, 46), new NodeId(0, 16519).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16406), new NodeId(0, 40), new NodeId(0, 2915).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16406), new NodeId(0, 37), new NodeId(0, 11508).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16406), new NodeId(0, 16362), new NodeId(0, 16405).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode98() {
    var node =
        new StateTypeNode(
            this.context,
            new NodeId(0, 2930),
            new QualifiedName(0, "Unshelved"),
            new LocalizedText("", "Unshelved"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 2930), new NodeId(0, 46), new NodeId(0, 6098).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2930), new NodeId(0, 51), new NodeId(0, 2935).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 2930), new NodeId(0, 51), new NodeId(0, 2936).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 2930), new NodeId(0, 52), new NodeId(0, 2940).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 2930), new NodeId(0, 52), new NodeId(0, 2943).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 2930), new NodeId(0, 40), new NodeId(0, 2307).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2930), new NodeId(0, 47), new NodeId(0, 2929).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode99() {
    var node =
        new StateTypeNode(
            this.context,
            new NodeId(0, 2932),
            new QualifiedName(0, "TimedShelved"),
            new LocalizedText("", "TimedShelved"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 2932), new NodeId(0, 46), new NodeId(0, 6100).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2932), new NodeId(0, 52), new NodeId(0, 2935).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 2932), new NodeId(0, 51), new NodeId(0, 2940).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 2932), new NodeId(0, 51), new NodeId(0, 2942).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 2932), new NodeId(0, 52), new NodeId(0, 2945).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 2932), new NodeId(0, 40), new NodeId(0, 2307).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2932), new NodeId(0, 47), new NodeId(0, 2929).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode100() {
    var node =
        new StateTypeNode(
            this.context,
            new NodeId(0, 2933),
            new QualifiedName(0, "OneShotShelved"),
            new LocalizedText("", "OneShotShelved"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 2933), new NodeId(0, 46), new NodeId(0, 6101).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2933), new NodeId(0, 52), new NodeId(0, 2936).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 2933), new NodeId(0, 52), new NodeId(0, 2942).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 2933), new NodeId(0, 51), new NodeId(0, 2943).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 2933), new NodeId(0, 51), new NodeId(0, 2945).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 2933), new NodeId(0, 40), new NodeId(0, 2307).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2933), new NodeId(0, 47), new NodeId(0, 2929).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode101() {
    var node =
        new TransitionTypeNode(
            this.context,
            new NodeId(0, 2935),
            new QualifiedName(0, "UnshelvedToTimedShelved"),
            new LocalizedText("", "UnshelvedToTimedShelved"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 2935), new NodeId(0, 46), new NodeId(0, 11322).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2935), new NodeId(0, 51), new NodeId(0, 2930).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2935), new NodeId(0, 52), new NodeId(0, 2932).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2935), new NodeId(0, 54), new NodeId(0, 2915).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2935), new NodeId(0, 53), new NodeId(0, 2949).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2935), new NodeId(0, 53), new NodeId(0, 24756).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2935), new NodeId(0, 40), new NodeId(0, 2310).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2935), new NodeId(0, 47), new NodeId(0, 2929).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode102() {
    var node =
        new TransitionTypeNode(
            this.context,
            new NodeId(0, 2936),
            new QualifiedName(0, "UnshelvedToOneShotShelved"),
            new LocalizedText("", "UnshelvedToOneShotShelved"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 2936), new NodeId(0, 46), new NodeId(0, 11323).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2936), new NodeId(0, 51), new NodeId(0, 2930).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2936), new NodeId(0, 52), new NodeId(0, 2933).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2936), new NodeId(0, 54), new NodeId(0, 2915).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2936), new NodeId(0, 53), new NodeId(0, 2948).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2936), new NodeId(0, 53), new NodeId(0, 24760).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2936), new NodeId(0, 40), new NodeId(0, 2310).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2936), new NodeId(0, 47), new NodeId(0, 2929).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode103() {
    var node =
        new TransitionTypeNode(
            this.context,
            new NodeId(0, 2940),
            new QualifiedName(0, "TimedShelvedToUnshelved"),
            new LocalizedText("", "TimedShelvedToUnshelved"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 2940), new NodeId(0, 46), new NodeId(0, 11324).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2940), new NodeId(0, 51), new NodeId(0, 2932).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2940), new NodeId(0, 52), new NodeId(0, 2930).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2940), new NodeId(0, 54), new NodeId(0, 2915).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2940), new NodeId(0, 53), new NodeId(0, 2947).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2940), new NodeId(0, 53), new NodeId(0, 24758).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2940), new NodeId(0, 40), new NodeId(0, 2310).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2940), new NodeId(0, 47), new NodeId(0, 2929).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode104() {
    var node =
        new TransitionTypeNode(
            this.context,
            new NodeId(0, 2942),
            new QualifiedName(0, "TimedShelvedToOneShotShelved"),
            new LocalizedText("", "TimedShelvedToOneShotShelved"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 2942), new NodeId(0, 46), new NodeId(0, 11325).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2942), new NodeId(0, 51), new NodeId(0, 2932).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2942), new NodeId(0, 52), new NodeId(0, 2933).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2942), new NodeId(0, 54), new NodeId(0, 2915).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2942), new NodeId(0, 53), new NodeId(0, 2948).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2942), new NodeId(0, 53), new NodeId(0, 24760).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2942), new NodeId(0, 40), new NodeId(0, 2310).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2942), new NodeId(0, 47), new NodeId(0, 2929).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode105() {
    var node =
        new TransitionTypeNode(
            this.context,
            new NodeId(0, 2943),
            new QualifiedName(0, "OneShotShelvedToUnshelved"),
            new LocalizedText("", "OneShotShelvedToUnshelved"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 2943), new NodeId(0, 46), new NodeId(0, 11326).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2943), new NodeId(0, 51), new NodeId(0, 2933).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2943), new NodeId(0, 52), new NodeId(0, 2930).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2943), new NodeId(0, 54), new NodeId(0, 2915).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2943), new NodeId(0, 53), new NodeId(0, 2947).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2943), new NodeId(0, 53), new NodeId(0, 24758).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2943), new NodeId(0, 40), new NodeId(0, 2310).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2943), new NodeId(0, 47), new NodeId(0, 2929).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode106() {
    var node =
        new TransitionTypeNode(
            this.context,
            new NodeId(0, 2945),
            new QualifiedName(0, "OneShotShelvedToTimedShelved"),
            new LocalizedText("", "OneShotShelvedToTimedShelved"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 2945), new NodeId(0, 46), new NodeId(0, 11327).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2945), new NodeId(0, 51), new NodeId(0, 2933).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2945), new NodeId(0, 52), new NodeId(0, 2932).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2945), new NodeId(0, 54), new NodeId(0, 2915).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2945), new NodeId(0, 53), new NodeId(0, 2949).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2945), new NodeId(0, 53), new NodeId(0, 24756).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2945), new NodeId(0, 40), new NodeId(0, 2310).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2945), new NodeId(0, 47), new NodeId(0, 2929).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode107() {
    var node =
        new StateTypeNode(
            this.context,
            new NodeId(0, 9329),
            new QualifiedName(0, "HighHigh"),
            new LocalizedText("", "HighHigh"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 9329), new NodeId(0, 46), new NodeId(0, 9330).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 9329), new NodeId(0, 51), new NodeId(0, 9339).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 9329), new NodeId(0, 52), new NodeId(0, 9340).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 9329), new NodeId(0, 40), new NodeId(0, 2307).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 9329), new NodeId(0, 47), new NodeId(0, 9318).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode108() {
    var node =
        new StateTypeNode(
            this.context,
            new NodeId(0, 9331),
            new QualifiedName(0, "High"),
            new LocalizedText("", "High"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 9331), new NodeId(0, 46), new NodeId(0, 9332).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 9331), new NodeId(0, 52), new NodeId(0, 9339).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 9331), new NodeId(0, 51), new NodeId(0, 9340).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 9331), new NodeId(0, 40), new NodeId(0, 2307).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 9331), new NodeId(0, 47), new NodeId(0, 9318).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode109() {
    var node =
        new StateTypeNode(
            this.context,
            new NodeId(0, 9333),
            new QualifiedName(0, "Low"),
            new LocalizedText("", "Low"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 9333), new NodeId(0, 46), new NodeId(0, 9334).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 9333), new NodeId(0, 52), new NodeId(0, 9337).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 9333), new NodeId(0, 51), new NodeId(0, 9338).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 9333), new NodeId(0, 40), new NodeId(0, 2307).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 9333), new NodeId(0, 47), new NodeId(0, 9318).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode110() {
    var node =
        new StateTypeNode(
            this.context,
            new NodeId(0, 9335),
            new QualifiedName(0, "LowLow"),
            new LocalizedText("", "LowLow"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 9335), new NodeId(0, 46), new NodeId(0, 9336).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 9335), new NodeId(0, 51), new NodeId(0, 9337).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 9335), new NodeId(0, 52), new NodeId(0, 9338).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 9335), new NodeId(0, 40), new NodeId(0, 2307).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 9335), new NodeId(0, 47), new NodeId(0, 9318).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode111() {
    var node =
        new TransitionTypeNode(
            this.context,
            new NodeId(0, 9337),
            new QualifiedName(0, "LowLowToLow"),
            new LocalizedText("", "LowLowToLow"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 9337), new NodeId(0, 46), new NodeId(0, 11340).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 9337), new NodeId(0, 51), new NodeId(0, 9335).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 9337), new NodeId(0, 52), new NodeId(0, 9333).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 9337), new NodeId(0, 54), new NodeId(0, 2915).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 9337), new NodeId(0, 40), new NodeId(0, 2310).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 9337), new NodeId(0, 47), new NodeId(0, 9318).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode112() {
    var node =
        new TransitionTypeNode(
            this.context,
            new NodeId(0, 9338),
            new QualifiedName(0, "LowToLowLow"),
            new LocalizedText("", "LowToLowLow"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 9338), new NodeId(0, 46), new NodeId(0, 11341).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 9338), new NodeId(0, 51), new NodeId(0, 9333).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 9338), new NodeId(0, 52), new NodeId(0, 9335).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 9338), new NodeId(0, 54), new NodeId(0, 2915).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 9338), new NodeId(0, 40), new NodeId(0, 2310).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 9338), new NodeId(0, 47), new NodeId(0, 9318).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode113() {
    var node =
        new TransitionTypeNode(
            this.context,
            new NodeId(0, 9339),
            new QualifiedName(0, "HighHighToHigh"),
            new LocalizedText("", "HighHighToHigh"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 9339), new NodeId(0, 46), new NodeId(0, 11342).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 9339), new NodeId(0, 51), new NodeId(0, 9329).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 9339), new NodeId(0, 52), new NodeId(0, 9331).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 9339), new NodeId(0, 54), new NodeId(0, 2915).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 9339), new NodeId(0, 40), new NodeId(0, 2310).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 9339), new NodeId(0, 47), new NodeId(0, 9318).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode114() {
    var node =
        new TransitionTypeNode(
            this.context,
            new NodeId(0, 9340),
            new QualifiedName(0, "HighToHighHigh"),
            new LocalizedText("", "HighToHighHigh"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 9340), new NodeId(0, 46), new NodeId(0, 11343).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 9340), new NodeId(0, 51), new NodeId(0, 9331).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 9340), new NodeId(0, 52), new NodeId(0, 9329).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 9340), new NodeId(0, 54), new NodeId(0, 2915).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 9340), new NodeId(0, 40), new NodeId(0, 2310).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 9340), new NodeId(0, 47), new NodeId(0, 9318).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode115() {
    var node =
        new ExclusiveLimitStateMachineTypeNode(
            this.context,
            new NodeId(0, 9455),
            new QualifiedName(0, "LimitState"),
            new LocalizedText("", "LimitState"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 9455), new NodeId(0, 47), new NodeId(0, 9456).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 9455), new NodeId(0, 47), new NodeId(0, 9461).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 9455), new NodeId(0, 9004), new NodeId(0, 9398).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 9455), new NodeId(0, 40), new NodeId(0, 9318).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 9455), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 9455), new NodeId(0, 47), new NodeId(0, 9341).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode116() {
    var node =
        new BaseObjectTypeNode(
            this.context,
            new NodeId(0, 3850),
            new QualifiedName(0, "FinalResultData"),
            new LocalizedText("", "FinalResultData"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 3850), new NodeId(0, 40), new NodeId(0, 58).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 3850), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 3850), new NodeId(0, 47), new NodeId(0, 2391).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode117() {
    var node =
        new StateTypeNode(
            this.context,
            new NodeId(0, 2406),
            new QualifiedName(0, "Halted"),
            new LocalizedText("", "Halted"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 2406), new NodeId(0, 46), new NodeId(0, 2407).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2406), new NodeId(0, 51), new NodeId(0, 2408).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 2406), new NodeId(0, 52), new NodeId(0, 2412).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 2406), new NodeId(0, 52), new NodeId(0, 2420).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 2406), new NodeId(0, 52), new NodeId(0, 2424).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 2406), new NodeId(0, 40), new NodeId(0, 2307).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2406), new NodeId(0, 47), new NodeId(0, 2391).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode118() {
    var node =
        new StateTypeNode(
            this.context,
            new NodeId(0, 2400),
            new QualifiedName(0, "Ready"),
            new LocalizedText("", "Ready"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 2400), new NodeId(0, 46), new NodeId(0, 2401).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2400), new NodeId(0, 52), new NodeId(0, 2408).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 2400), new NodeId(0, 51), new NodeId(0, 2410).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 2400), new NodeId(0, 52), new NodeId(0, 2414).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 2400), new NodeId(0, 52), new NodeId(0, 2422).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 2400), new NodeId(0, 51), new NodeId(0, 2424).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 2400), new NodeId(0, 40), new NodeId(0, 2307).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2400), new NodeId(0, 47), new NodeId(0, 2391).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode119() {
    var node =
        new StateTypeNode(
            this.context,
            new NodeId(0, 2402),
            new QualifiedName(0, "Running"),
            new LocalizedText("", "Running"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 2402), new NodeId(0, 46), new NodeId(0, 2403).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2402), new NodeId(0, 52), new NodeId(0, 2410).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 2402), new NodeId(0, 51), new NodeId(0, 2412).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 2402), new NodeId(0, 51), new NodeId(0, 2414).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 2402), new NodeId(0, 51), new NodeId(0, 2416).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 2402), new NodeId(0, 52), new NodeId(0, 2418).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 2402), new NodeId(0, 40), new NodeId(0, 2307).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2402), new NodeId(0, 47), new NodeId(0, 2391).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode120() {
    var node =
        new StateTypeNode(
            this.context,
            new NodeId(0, 2404),
            new QualifiedName(0, "Suspended"),
            new LocalizedText("", "Suspended"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 2404), new NodeId(0, 46), new NodeId(0, 2405).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2404), new NodeId(0, 52), new NodeId(0, 2416).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 2404), new NodeId(0, 51), new NodeId(0, 2418).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 2404), new NodeId(0, 51), new NodeId(0, 2420).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 2404), new NodeId(0, 51), new NodeId(0, 2422).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 2404), new NodeId(0, 40), new NodeId(0, 2307).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2404), new NodeId(0, 47), new NodeId(0, 2391).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode121() {
    var node =
        new TransitionTypeNode(
            this.context,
            new NodeId(0, 2408),
            new QualifiedName(0, "HaltedToReady"),
            new LocalizedText("", "HaltedToReady"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 2408), new NodeId(0, 46), new NodeId(0, 2409).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2408), new NodeId(0, 51), new NodeId(0, 2406).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2408), new NodeId(0, 52), new NodeId(0, 2400).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2408), new NodeId(0, 53), new NodeId(0, 2430).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2408), new NodeId(0, 54), new NodeId(0, 2378).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2408), new NodeId(0, 54), new NodeId(0, 11856).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2408), new NodeId(0, 40), new NodeId(0, 2310).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2408), new NodeId(0, 47), new NodeId(0, 2391).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode122() {
    var node =
        new TransitionTypeNode(
            this.context,
            new NodeId(0, 2410),
            new QualifiedName(0, "ReadyToRunning"),
            new LocalizedText("", "ReadyToRunning"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 2410), new NodeId(0, 46), new NodeId(0, 2411).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2410), new NodeId(0, 51), new NodeId(0, 2400).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2410), new NodeId(0, 52), new NodeId(0, 2402).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2410), new NodeId(0, 53), new NodeId(0, 2426).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2410), new NodeId(0, 54), new NodeId(0, 2378).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2410), new NodeId(0, 54), new NodeId(0, 11856).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2410), new NodeId(0, 40), new NodeId(0, 2310).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2410), new NodeId(0, 47), new NodeId(0, 2391).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode123() {
    var node =
        new TransitionTypeNode(
            this.context,
            new NodeId(0, 2412),
            new QualifiedName(0, "RunningToHalted"),
            new LocalizedText("", "RunningToHalted"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 2412), new NodeId(0, 46), new NodeId(0, 2413).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2412), new NodeId(0, 51), new NodeId(0, 2402).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2412), new NodeId(0, 52), new NodeId(0, 2406).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2412), new NodeId(0, 53), new NodeId(0, 2429).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2412), new NodeId(0, 54), new NodeId(0, 2378).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2412), new NodeId(0, 54), new NodeId(0, 11856).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2412), new NodeId(0, 40), new NodeId(0, 2310).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2412), new NodeId(0, 47), new NodeId(0, 2391).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode124() {
    var node =
        new TransitionTypeNode(
            this.context,
            new NodeId(0, 2414),
            new QualifiedName(0, "RunningToReady"),
            new LocalizedText("", "RunningToReady"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 2414), new NodeId(0, 46), new NodeId(0, 2415).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2414), new NodeId(0, 51), new NodeId(0, 2402).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2414), new NodeId(0, 52), new NodeId(0, 2400).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2414), new NodeId(0, 54), new NodeId(0, 2378).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2414), new NodeId(0, 54), new NodeId(0, 11856).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2414), new NodeId(0, 40), new NodeId(0, 2310).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2414), new NodeId(0, 47), new NodeId(0, 2391).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode125() {
    var node =
        new TransitionTypeNode(
            this.context,
            new NodeId(0, 2416),
            new QualifiedName(0, "RunningToSuspended"),
            new LocalizedText("", "RunningToSuspended"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 2416), new NodeId(0, 46), new NodeId(0, 2417).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2416), new NodeId(0, 51), new NodeId(0, 2402).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2416), new NodeId(0, 52), new NodeId(0, 2404).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2416), new NodeId(0, 53), new NodeId(0, 2427).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2416), new NodeId(0, 54), new NodeId(0, 2378).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2416), new NodeId(0, 54), new NodeId(0, 11856).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2416), new NodeId(0, 40), new NodeId(0, 2310).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2416), new NodeId(0, 47), new NodeId(0, 2391).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode126() {
    var node =
        new TransitionTypeNode(
            this.context,
            new NodeId(0, 2418),
            new QualifiedName(0, "SuspendedToRunning"),
            new LocalizedText("", "SuspendedToRunning"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 2418), new NodeId(0, 46), new NodeId(0, 2419).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2418), new NodeId(0, 51), new NodeId(0, 2404).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2418), new NodeId(0, 52), new NodeId(0, 2402).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2418), new NodeId(0, 53), new NodeId(0, 2428).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2418), new NodeId(0, 54), new NodeId(0, 2378).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2418), new NodeId(0, 54), new NodeId(0, 11856).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2418), new NodeId(0, 40), new NodeId(0, 2310).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2418), new NodeId(0, 47), new NodeId(0, 2391).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode127() {
    var node =
        new TransitionTypeNode(
            this.context,
            new NodeId(0, 2420),
            new QualifiedName(0, "SuspendedToHalted"),
            new LocalizedText("", "SuspendedToHalted"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 2420), new NodeId(0, 46), new NodeId(0, 2421).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2420), new NodeId(0, 51), new NodeId(0, 2404).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2420), new NodeId(0, 52), new NodeId(0, 2406).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2420), new NodeId(0, 53), new NodeId(0, 2429).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2420), new NodeId(0, 53), new NodeId(0, 2430).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2420), new NodeId(0, 54), new NodeId(0, 2378).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2420), new NodeId(0, 54), new NodeId(0, 11856).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2420), new NodeId(0, 40), new NodeId(0, 2310).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2420), new NodeId(0, 47), new NodeId(0, 2391).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode128() {
    var node =
        new TransitionTypeNode(
            this.context,
            new NodeId(0, 2422),
            new QualifiedName(0, "SuspendedToReady"),
            new LocalizedText("", "SuspendedToReady"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 2422), new NodeId(0, 46), new NodeId(0, 2423).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2422), new NodeId(0, 51), new NodeId(0, 2404).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2422), new NodeId(0, 52), new NodeId(0, 2400).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2422), new NodeId(0, 54), new NodeId(0, 2378).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2422), new NodeId(0, 54), new NodeId(0, 11856).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2422), new NodeId(0, 53), new NodeId(0, 2430).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2422), new NodeId(0, 40), new NodeId(0, 2310).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2422), new NodeId(0, 47), new NodeId(0, 2391).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode129() {
    var node =
        new TransitionTypeNode(
            this.context,
            new NodeId(0, 2424),
            new QualifiedName(0, "ReadyToHalted"),
            new LocalizedText("", "ReadyToHalted"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 2424), new NodeId(0, 46), new NodeId(0, 2425).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2424), new NodeId(0, 51), new NodeId(0, 2400).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2424), new NodeId(0, 52), new NodeId(0, 2406).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2424), new NodeId(0, 53), new NodeId(0, 2429).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2424), new NodeId(0, 54), new NodeId(0, 2378).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2424), new NodeId(0, 54), new NodeId(0, 11856).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2424), new NodeId(0, 40), new NodeId(0, 2310).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2424), new NodeId(0, 47), new NodeId(0, 2391).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode130() {
    var node =
        new AggregateConfigurationTypeNode(
            this.context,
            new NodeId(0, 3059),
            new QualifiedName(0, "AggregateConfiguration"),
            new LocalizedText("", "AggregateConfiguration"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 3059), new NodeId(0, 46), new NodeId(0, 11168).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 3059), new NodeId(0, 46), new NodeId(0, 11169).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 3059), new NodeId(0, 46), new NodeId(0, 11170).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 3059), new NodeId(0, 46), new NodeId(0, 11171).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 3059), new NodeId(0, 40), new NodeId(0, 11187).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 3059), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 3059), new NodeId(0, 47), new NodeId(0, 2318).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode131() {
    var node =
        new FolderTypeNode(
            this.context,
            new NodeId(0, 11876),
            new QualifiedName(0, "AggregateFunctions"),
            new LocalizedText("", "AggregateFunctions"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 11876), new NodeId(0, 40), new NodeId(0, 61).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 11876), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11876), new NodeId(0, 47), new NodeId(0, 2318).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode132() {
    var node =
        new FolderTypeNode(
            this.context,
            new NodeId(0, 32622),
            new QualifiedName(0, "EventTypes"),
            new LocalizedText("", "EventTypes"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 32622), new NodeId(0, 40), new NodeId(0, 61).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 32622), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32622), new NodeId(0, 47), new NodeId(0, 32621).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode133() {
    var node =
        new HistoricalDataConfigurationTypeNode(
            this.context,
            new NodeId(0, 11202),
            new QualifiedName(0, "HA Configuration"),
            new LocalizedText("", "HA Configuration"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 11202), new NodeId(0, 47), new NodeId(0, 11203).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11202), new NodeId(0, 46), new NodeId(0, 11208).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11202), new NodeId(0, 40), new NodeId(0, 2318).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode134() {
    var node =
        new AggregateConfigurationTypeNode(
            this.context,
            new NodeId(0, 11203),
            new QualifiedName(0, "AggregateConfiguration"),
            new LocalizedText("", "AggregateConfiguration"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 11203), new NodeId(0, 46), new NodeId(0, 11204).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11203), new NodeId(0, 46), new NodeId(0, 11205).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11203), new NodeId(0, 46), new NodeId(0, 11206).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11203), new NodeId(0, 46), new NodeId(0, 11207).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11203), new NodeId(0, 40), new NodeId(0, 11187).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11203), new NodeId(0, 47), new NodeId(0, 11202).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode135() {
    var node =
        new HistoricalDataConfigurationTypeNode(
            this.context,
            new NodeId(0, 32637),
            new QualifiedName(0, "DefaultHAConfiguration"),
            new LocalizedText("", "DefaultHAConfiguration"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 32637), new NodeId(0, 47), new NodeId(0, 32638).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32637), new NodeId(0, 47), new NodeId(0, 32643).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32637), new NodeId(0, 46), new NodeId(0, 32644).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32637), new NodeId(0, 46), new NodeId(0, 32645).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32637), new NodeId(0, 46), new NodeId(0, 32646).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32637), new NodeId(0, 46), new NodeId(0, 32647).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32637), new NodeId(0, 46), new NodeId(0, 32648).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32637), new NodeId(0, 46), new NodeId(0, 32649).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32637), new NodeId(0, 46), new NodeId(0, 32650).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32637), new NodeId(0, 46), new NodeId(0, 32656).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32637), new NodeId(0, 46), new NodeId(0, 32682).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32637), new NodeId(0, 46), new NodeId(0, 32752).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32637), new NodeId(0, 46), new NodeId(0, 32753).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32637), new NodeId(0, 35), new NodeId(0, 2253).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 32637), new NodeId(0, 40), new NodeId(0, 2318).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode136() {
    var node =
        new AggregateConfigurationTypeNode(
            this.context,
            new NodeId(0, 32638),
            new QualifiedName(0, "AggregateConfiguration"),
            new LocalizedText("", "AggregateConfiguration"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 32638), new NodeId(0, 46), new NodeId(0, 32639).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32638), new NodeId(0, 46), new NodeId(0, 32640).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32638), new NodeId(0, 46), new NodeId(0, 32641).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32638), new NodeId(0, 46), new NodeId(0, 32642).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32638), new NodeId(0, 40), new NodeId(0, 11187).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32638), new NodeId(0, 47), new NodeId(0, 32637).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode137() {
    var node =
        new FolderTypeNode(
            this.context,
            new NodeId(0, 32643),
            new QualifiedName(0, "AggregateFunctions"),
            new LocalizedText("", "AggregateFunctions"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 32643), new NodeId(0, 40), new NodeId(0, 61).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32643), new NodeId(0, 47), new NodeId(0, 32637).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode138() {
    var node =
        new HistoricalEventConfigurationTypeNode(
            this.context,
            new NodeId(0, 32754),
            new QualifiedName(0, "DefaultHEConfiguration"),
            new LocalizedText("", "DefaultHEConfiguration"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 32754), new NodeId(0, 47), new NodeId(0, 32755).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32754), new NodeId(0, 46), new NodeId(0, 32756).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32754), new NodeId(0, 46), new NodeId(0, 32757).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32754), new NodeId(0, 35), new NodeId(0, 2253).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 32754), new NodeId(0, 40), new NodeId(0, 32621).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode139() {
    var node =
        new FolderTypeNode(
            this.context,
            new NodeId(0, 32755),
            new QualifiedName(0, "EventTypes"),
            new LocalizedText("", "EventTypes"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 32755), new NodeId(0, 40), new NodeId(0, 61).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32755), new NodeId(0, 47), new NodeId(0, 32754).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode140() {
    var node =
        new FolderTypeNode(
            this.context,
            new NodeId(0, 11172),
            new QualifiedName(0, "AggregateFunctions"),
            new LocalizedText("", "AggregateFunctions"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 11172), new NodeId(0, 40), new NodeId(0, 61).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 11172), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11172), new NodeId(0, 47), new NodeId(0, 2330).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode141() {
    var node =
        new TrustListTypeNode(
            this.context,
            new NodeId(0, 13599),
            new QualifiedName(0, "TrustList"),
            new LocalizedText("", "TrustList"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 13599), new NodeId(0, 46), new NodeId(0, 13600).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13599), new NodeId(0, 46), new NodeId(0, 13601).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13599), new NodeId(0, 46), new NodeId(0, 13602).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13599), new NodeId(0, 46), new NodeId(0, 13603).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13599), new NodeId(0, 47), new NodeId(0, 13605).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13599), new NodeId(0, 47), new NodeId(0, 13608).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13599), new NodeId(0, 47), new NodeId(0, 13610).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13599), new NodeId(0, 47), new NodeId(0, 13613).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13599), new NodeId(0, 47), new NodeId(0, 13615).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13599), new NodeId(0, 47), new NodeId(0, 13618).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13599), new NodeId(0, 46), new NodeId(0, 13620).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13599), new NodeId(0, 47), new NodeId(0, 13621).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13599), new NodeId(0, 47), new NodeId(0, 13624).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13599), new NodeId(0, 47), new NodeId(0, 13627).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13599), new NodeId(0, 47), new NodeId(0, 13629).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13599), new NodeId(0, 40), new NodeId(0, 12522).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13599), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13599), new NodeId(0, 47), new NodeId(0, 12555).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode142() {
    var node =
        new CertificateExpirationAlarmTypeNode(
            this.context,
            new NodeId(0, 19450),
            new QualifiedName(0, "CertificateExpired"),
            new LocalizedText("", "CertificateExpired"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 19450), new NodeId(0, 46), new NodeId(0, 19451).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19450), new NodeId(0, 46), new NodeId(0, 19452).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19450), new NodeId(0, 46), new NodeId(0, 19453).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19450), new NodeId(0, 46), new NodeId(0, 19454).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19450), new NodeId(0, 46), new NodeId(0, 19455).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19450), new NodeId(0, 46), new NodeId(0, 19456).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19450), new NodeId(0, 46), new NodeId(0, 19458).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19450), new NodeId(0, 46), new NodeId(0, 19459).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19450), new NodeId(0, 46), new NodeId(0, 19460).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19450), new NodeId(0, 46), new NodeId(0, 19461).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19450), new NodeId(0, 46), new NodeId(0, 19464).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19450), new NodeId(0, 46), new NodeId(0, 19465).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19450), new NodeId(0, 46), new NodeId(0, 19466).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19450), new NodeId(0, 47), new NodeId(0, 19467).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19450), new NodeId(0, 47), new NodeId(0, 19476).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19450), new NodeId(0, 47), new NodeId(0, 19478).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19450), new NodeId(0, 47), new NodeId(0, 19480).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19450), new NodeId(0, 46), new NodeId(0, 19482).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19450), new NodeId(0, 47), new NodeId(0, 19483).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19450), new NodeId(0, 47), new NodeId(0, 19484).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19450), new NodeId(0, 47), new NodeId(0, 19485).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19450), new NodeId(0, 47), new NodeId(0, 19487).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19450), new NodeId(0, 47), new NodeId(0, 19505).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19450), new NodeId(0, 47), new NodeId(0, 19509).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19450), new NodeId(0, 46), new NodeId(0, 19518).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19450), new NodeId(0, 46), new NodeId(0, 20101).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19450), new NodeId(0, 46), new NodeId(0, 20138).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19450), new NodeId(0, 46), new NodeId(0, 20139).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19450), new NodeId(0, 46), new NodeId(0, 20141).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19450), new NodeId(0, 46), new NodeId(0, 20142).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19450), new NodeId(0, 40), new NodeId(0, 13225).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 19450), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19450), new NodeId(0, 47), new NodeId(0, 12555).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode143() {
    var node =
        new TrustListOutOfDateAlarmTypeNode(
            this.context,
            new NodeId(0, 20143),
            new QualifiedName(0, "TrustListOutOfDate"),
            new LocalizedText("", "TrustListOutOfDate"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 20143), new NodeId(0, 46), new NodeId(0, 20144).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 20143), new NodeId(0, 46), new NodeId(0, 20145).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 20143), new NodeId(0, 46), new NodeId(0, 20146).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 20143), new NodeId(0, 46), new NodeId(0, 20147).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 20143), new NodeId(0, 46), new NodeId(0, 20148).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 20143), new NodeId(0, 46), new NodeId(0, 20149).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 20143), new NodeId(0, 46), new NodeId(0, 20151).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 20143), new NodeId(0, 46), new NodeId(0, 20152).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 20143), new NodeId(0, 46), new NodeId(0, 20153).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 20143), new NodeId(0, 46), new NodeId(0, 20154).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 20143), new NodeId(0, 46), new NodeId(0, 20157).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 20143), new NodeId(0, 46), new NodeId(0, 20158).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 20143), new NodeId(0, 46), new NodeId(0, 20159).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 20143), new NodeId(0, 47), new NodeId(0, 20160).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 20143), new NodeId(0, 47), new NodeId(0, 20169).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 20143), new NodeId(0, 47), new NodeId(0, 20171).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 20143), new NodeId(0, 47), new NodeId(0, 20173).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 20143), new NodeId(0, 46), new NodeId(0, 20175).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 20143), new NodeId(0, 47), new NodeId(0, 20176).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 20143), new NodeId(0, 47), new NodeId(0, 20177).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 20143), new NodeId(0, 47), new NodeId(0, 20178).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 20143), new NodeId(0, 47), new NodeId(0, 20180).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 20143), new NodeId(0, 47), new NodeId(0, 20198).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 20143), new NodeId(0, 47), new NodeId(0, 20202).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 20143), new NodeId(0, 46), new NodeId(0, 20211).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 20143), new NodeId(0, 46), new NodeId(0, 20249).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 20143), new NodeId(0, 46), new NodeId(0, 20286).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 20143), new NodeId(0, 46), new NodeId(0, 20287).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 20143), new NodeId(0, 46), new NodeId(0, 20288).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 20143), new NodeId(0, 46), new NodeId(0, 20289).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 20143), new NodeId(0, 40), new NodeId(0, 19297).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 20143), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 20143), new NodeId(0, 47), new NodeId(0, 12555).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode144() {
    var node =
        new CertificateGroupTypeNode(
            this.context,
            new NodeId(0, 13814),
            new QualifiedName(0, "DefaultApplicationGroup"),
            new LocalizedText("", "DefaultApplicationGroup"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 13814), new NodeId(0, 47), new NodeId(0, 13815).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13814), new NodeId(0, 46), new NodeId(0, 13847).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13814), new NodeId(0, 9006), new NodeId(0, 13225).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13814), new NodeId(0, 40), new NodeId(0, 12555).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13814), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13814), new NodeId(0, 47), new NodeId(0, 13813).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode145() {
    var node =
        new TrustListTypeNode(
            this.context,
            new NodeId(0, 13815),
            new QualifiedName(0, "TrustList"),
            new LocalizedText("", "TrustList"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 13815), new NodeId(0, 46), new NodeId(0, 13816).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13815), new NodeId(0, 46), new NodeId(0, 13817).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13815), new NodeId(0, 46), new NodeId(0, 13818).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13815), new NodeId(0, 46), new NodeId(0, 13819).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13815), new NodeId(0, 47), new NodeId(0, 13821).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13815), new NodeId(0, 47), new NodeId(0, 13824).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13815), new NodeId(0, 47), new NodeId(0, 13826).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13815), new NodeId(0, 47), new NodeId(0, 13829).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13815), new NodeId(0, 47), new NodeId(0, 13831).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13815), new NodeId(0, 47), new NodeId(0, 13834).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13815), new NodeId(0, 46), new NodeId(0, 13836).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13815), new NodeId(0, 47), new NodeId(0, 13837).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13815), new NodeId(0, 47), new NodeId(0, 13840).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13815), new NodeId(0, 47), new NodeId(0, 13843).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13815), new NodeId(0, 47), new NodeId(0, 13845).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13815), new NodeId(0, 40), new NodeId(0, 12522).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13815), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13815), new NodeId(0, 47), new NodeId(0, 13814).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode146() {
    var node =
        new CertificateGroupTypeNode(
            this.context,
            new NodeId(0, 13848),
            new QualifiedName(0, "DefaultHttpsGroup"),
            new LocalizedText("", "DefaultHttpsGroup"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 13848), new NodeId(0, 47), new NodeId(0, 13849).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13848), new NodeId(0, 46), new NodeId(0, 13881).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13848), new NodeId(0, 9006), new NodeId(0, 13225).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13848), new NodeId(0, 40), new NodeId(0, 12555).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13848), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13848), new NodeId(0, 47), new NodeId(0, 13813).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode147() {
    var node =
        new TrustListTypeNode(
            this.context,
            new NodeId(0, 13849),
            new QualifiedName(0, "TrustList"),
            new LocalizedText("", "TrustList"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 13849), new NodeId(0, 46), new NodeId(0, 13850).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13849), new NodeId(0, 46), new NodeId(0, 13851).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13849), new NodeId(0, 46), new NodeId(0, 13852).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13849), new NodeId(0, 46), new NodeId(0, 13853).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13849), new NodeId(0, 47), new NodeId(0, 13855).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13849), new NodeId(0, 47), new NodeId(0, 13858).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13849), new NodeId(0, 47), new NodeId(0, 13860).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13849), new NodeId(0, 47), new NodeId(0, 13863).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13849), new NodeId(0, 47), new NodeId(0, 13865).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13849), new NodeId(0, 47), new NodeId(0, 13868).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13849), new NodeId(0, 46), new NodeId(0, 13870).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13849), new NodeId(0, 47), new NodeId(0, 13871).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13849), new NodeId(0, 47), new NodeId(0, 13874).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13849), new NodeId(0, 47), new NodeId(0, 13877).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13849), new NodeId(0, 47), new NodeId(0, 13879).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13849), new NodeId(0, 40), new NodeId(0, 12522).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13849), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13849), new NodeId(0, 47), new NodeId(0, 13848).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode148() {
    var node =
        new CertificateGroupTypeNode(
            this.context,
            new NodeId(0, 13882),
            new QualifiedName(0, "DefaultUserTokenGroup"),
            new LocalizedText("", "DefaultUserTokenGroup"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 13882), new NodeId(0, 47), new NodeId(0, 13883).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13882), new NodeId(0, 46), new NodeId(0, 13915).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13882), new NodeId(0, 9006), new NodeId(0, 13225).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13882), new NodeId(0, 40), new NodeId(0, 12555).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13882), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13882), new NodeId(0, 47), new NodeId(0, 13813).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode149() {
    var node =
        new TrustListTypeNode(
            this.context,
            new NodeId(0, 13883),
            new QualifiedName(0, "TrustList"),
            new LocalizedText("", "TrustList"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 13883), new NodeId(0, 46), new NodeId(0, 13884).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13883), new NodeId(0, 46), new NodeId(0, 13885).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13883), new NodeId(0, 46), new NodeId(0, 13886).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13883), new NodeId(0, 46), new NodeId(0, 13887).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13883), new NodeId(0, 47), new NodeId(0, 13889).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13883), new NodeId(0, 47), new NodeId(0, 13892).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13883), new NodeId(0, 47), new NodeId(0, 13894).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13883), new NodeId(0, 47), new NodeId(0, 13897).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13883), new NodeId(0, 47), new NodeId(0, 13899).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13883), new NodeId(0, 47), new NodeId(0, 13902).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13883), new NodeId(0, 46), new NodeId(0, 13904).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13883), new NodeId(0, 47), new NodeId(0, 13905).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13883), new NodeId(0, 47), new NodeId(0, 13908).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13883), new NodeId(0, 47), new NodeId(0, 13911).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13883), new NodeId(0, 47), new NodeId(0, 13913).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13883), new NodeId(0, 40), new NodeId(0, 12522).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13883), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13883), new NodeId(0, 47), new NodeId(0, 13882).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode150() {
    var node =
        new CertificateGroupTypeNode(
            this.context,
            new NodeId(0, 13916),
            new QualifiedName(0, "<AdditionalGroup>"),
            new LocalizedText("", "<AdditionalGroup>"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 13916), new NodeId(0, 47), new NodeId(0, 13917).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13916), new NodeId(0, 46), new NodeId(0, 13949).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13916), new NodeId(0, 9006), new NodeId(0, 13225).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13916), new NodeId(0, 40), new NodeId(0, 12555).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13916), new NodeId(0, 37), new NodeId(0, 11508).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13916), new NodeId(0, 35), new NodeId(0, 13813).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode151() {
    var node =
        new TrustListTypeNode(
            this.context,
            new NodeId(0, 13917),
            new QualifiedName(0, "TrustList"),
            new LocalizedText("", "TrustList"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 13917), new NodeId(0, 46), new NodeId(0, 13918).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13917), new NodeId(0, 46), new NodeId(0, 13919).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13917), new NodeId(0, 46), new NodeId(0, 13920).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13917), new NodeId(0, 46), new NodeId(0, 13921).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13917), new NodeId(0, 47), new NodeId(0, 13923).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13917), new NodeId(0, 47), new NodeId(0, 13926).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13917), new NodeId(0, 47), new NodeId(0, 13928).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13917), new NodeId(0, 47), new NodeId(0, 13931).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13917), new NodeId(0, 47), new NodeId(0, 13933).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13917), new NodeId(0, 47), new NodeId(0, 13936).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13917), new NodeId(0, 46), new NodeId(0, 13938).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13917), new NodeId(0, 47), new NodeId(0, 13939).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13917), new NodeId(0, 47), new NodeId(0, 13942).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13917), new NodeId(0, 47), new NodeId(0, 13945).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13917), new NodeId(0, 47), new NodeId(0, 13947).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13917), new NodeId(0, 40), new NodeId(0, 12522).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13917), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13917), new NodeId(0, 47), new NodeId(0, 13916).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode152() {
    var node =
        new ApplicationConfigurationTypeNode(
            this.context,
            new NodeId(0, 16663),
            new QualifiedName(0, "<ApplicationName>"),
            new LocalizedText("", "<ApplicationName>"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 16663), new NodeId(0, 47), new NodeId(0, 16707).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16663), new NodeId(0, 46), new NodeId(0, 18525).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16663), new NodeId(0, 46), new NodeId(0, 18526).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16663), new NodeId(0, 46), new NodeId(0, 18527).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16663), new NodeId(0, 46), new NodeId(0, 18528).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16663), new NodeId(0, 46), new NodeId(0, 18529).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16663), new NodeId(0, 46), new NodeId(0, 18530).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16663), new NodeId(0, 46), new NodeId(0, 18531).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16663), new NodeId(0, 47), new NodeId(0, 18533).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16663), new NodeId(0, 47), new NodeId(0, 18539).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16663), new NodeId(0, 47), new NodeId(0, 18541).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16663), new NodeId(0, 47), new NodeId(0, 18544).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16663), new NodeId(0, 46), new NodeId(0, 18592).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16663), new NodeId(0, 40), new NodeId(0, 25731).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16663), new NodeId(0, 37), new NodeId(0, 11508).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16663), new NodeId(0, 35), new NodeId(0, 16662).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode153() {
    var node =
        new CertificateGroupFolderTypeNode(
            this.context,
            new NodeId(0, 16707),
            new QualifiedName(0, "CertificateGroups"),
            new LocalizedText("", "CertificateGroups"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 16707), new NodeId(0, 47), new NodeId(0, 16708).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16707), new NodeId(0, 40), new NodeId(0, 13813).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 16707), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16707), new NodeId(0, 47), new NodeId(0, 16663).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode154() {
    var node =
        new CertificateGroupTypeNode(
            this.context,
            new NodeId(0, 16708),
            new QualifiedName(0, "DefaultApplicationGroup"),
            new LocalizedText("", "DefaultApplicationGroup"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 16708), new NodeId(0, 47), new NodeId(0, 16709).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16708), new NodeId(0, 46), new NodeId(0, 16751).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16708), new NodeId(0, 40), new NodeId(0, 12555).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 16708), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16708), new NodeId(0, 47), new NodeId(0, 16707).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode155() {
    var node =
        new TrustListTypeNode(
            this.context,
            new NodeId(0, 16709),
            new QualifiedName(0, "TrustList"),
            new LocalizedText("", "TrustList"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 16709), new NodeId(0, 46), new NodeId(0, 16710).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16709), new NodeId(0, 46), new NodeId(0, 16711).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16709), new NodeId(0, 46), new NodeId(0, 16712).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16709), new NodeId(0, 46), new NodeId(0, 16713).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16709), new NodeId(0, 47), new NodeId(0, 16717).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16709), new NodeId(0, 47), new NodeId(0, 16724).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16709), new NodeId(0, 47), new NodeId(0, 16726).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16709), new NodeId(0, 47), new NodeId(0, 16729).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16709), new NodeId(0, 47), new NodeId(0, 16732).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16709), new NodeId(0, 47), new NodeId(0, 16735).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16709), new NodeId(0, 46), new NodeId(0, 16737).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16709), new NodeId(0, 47), new NodeId(0, 16741).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16709), new NodeId(0, 47), new NodeId(0, 16744).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16709), new NodeId(0, 47), new NodeId(0, 16747).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16709), new NodeId(0, 47), new NodeId(0, 16749).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16709), new NodeId(0, 40), new NodeId(0, 12522).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 16709), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16709), new NodeId(0, 47), new NodeId(0, 16708).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode156() {
    var node =
        new ApplicationConfigurationFolderTypeNode(
            this.context,
            new NodeId(0, 16706),
            new QualifiedName(0, "ManagedApplications"),
            new LocalizedText("", "ManagedApplications"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 16706), new NodeId(0, 35), new NodeId(0, 24226).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 16706), new NodeId(0, 40), new NodeId(0, 16662).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode157() {
    var node =
        new CertificateGroupFolderTypeNode(
            this.context,
            new NodeId(0, 13950),
            new QualifiedName(0, "CertificateGroups"),
            new LocalizedText("", "CertificateGroups"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 13950), new NodeId(0, 47), new NodeId(0, 13951).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13950), new NodeId(0, 40), new NodeId(0, 13813).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13950), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13950), new NodeId(0, 47), new NodeId(0, 12581).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode158() {
    var node =
        new CertificateGroupTypeNode(
            this.context,
            new NodeId(0, 13951),
            new QualifiedName(0, "DefaultApplicationGroup"),
            new LocalizedText("", "DefaultApplicationGroup"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 13951), new NodeId(0, 47), new NodeId(0, 13952).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13951), new NodeId(0, 46), new NodeId(0, 13984).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13951), new NodeId(0, 9006), new NodeId(0, 13225).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13951), new NodeId(0, 40), new NodeId(0, 12555).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13951), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13951), new NodeId(0, 47), new NodeId(0, 13950).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode159() {
    var node =
        new TrustListTypeNode(
            this.context,
            new NodeId(0, 13952),
            new QualifiedName(0, "TrustList"),
            new LocalizedText("", "TrustList"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 13952), new NodeId(0, 46), new NodeId(0, 13953).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13952), new NodeId(0, 46), new NodeId(0, 13954).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13952), new NodeId(0, 46), new NodeId(0, 13955).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13952), new NodeId(0, 46), new NodeId(0, 13956).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13952), new NodeId(0, 47), new NodeId(0, 13958).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13952), new NodeId(0, 47), new NodeId(0, 13961).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13952), new NodeId(0, 47), new NodeId(0, 13963).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13952), new NodeId(0, 47), new NodeId(0, 13966).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13952), new NodeId(0, 47), new NodeId(0, 13968).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13952), new NodeId(0, 47), new NodeId(0, 13971).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13952), new NodeId(0, 46), new NodeId(0, 13973).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13952), new NodeId(0, 47), new NodeId(0, 13974).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13952), new NodeId(0, 47), new NodeId(0, 13977).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13952), new NodeId(0, 47), new NodeId(0, 13980).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13952), new NodeId(0, 47), new NodeId(0, 13982).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13952), new NodeId(0, 40), new NodeId(0, 12522).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 13952), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13952), new NodeId(0, 47), new NodeId(0, 13951).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode160() {
    var node =
        new TransactionDiagnosticsTypeNode(
            this.context,
            new NodeId(0, 32299),
            new QualifiedName(0, "TransactionDiagnostics"),
            new LocalizedText("", "TransactionDiagnostics"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 32299), new NodeId(0, 46), new NodeId(0, 32300).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32299), new NodeId(0, 46), new NodeId(0, 32301).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32299), new NodeId(0, 46), new NodeId(0, 32302).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32299), new NodeId(0, 46), new NodeId(0, 32303).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32299), new NodeId(0, 46), new NodeId(0, 32304).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32299), new NodeId(0, 46), new NodeId(0, 32305).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32299), new NodeId(0, 40), new NodeId(0, 32286).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 32299), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32299), new NodeId(0, 47), new NodeId(0, 12581).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode161() {
    var node =
        new ServerConfigurationTypeNode(
            this.context,
            new NodeId(0, 12637),
            new QualifiedName(0, "ServerConfiguration"),
            new LocalizedText("", "ServerConfiguration"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(new NodeId(0, 15644), new PermissionType(UInteger.valueOf(1))),
              new RolePermissionType(
                  new NodeId(0, 15716), new PermissionType(UInteger.valueOf(65423))),
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(65423)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 12637), new NodeId(0, 47), new NodeId(0, 14053).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12637), new NodeId(0, 46), new NodeId(0, 25706).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12637), new NodeId(0, 46), new NodeId(0, 25725).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12637), new NodeId(0, 46), new NodeId(0, 25707).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12637), new NodeId(0, 46), new NodeId(0, 12710).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12637), new NodeId(0, 46), new NodeId(0, 12639).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12637), new NodeId(0, 46), new NodeId(0, 12640).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12637), new NodeId(0, 46), new NodeId(0, 12641).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12637), new NodeId(0, 46), new NodeId(0, 23597).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12637), new NodeId(0, 47), new NodeId(0, 13737).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12637), new NodeId(0, 47), new NodeId(0, 12740).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12637), new NodeId(0, 47), new NodeId(0, 25708).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12637), new NodeId(0, 47), new NodeId(0, 12737).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12637), new NodeId(0, 47), new NodeId(0, 12777).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12637), new NodeId(0, 47), new NodeId(0, 25709).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12637), new NodeId(0, 47), new NodeId(0, 32336).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12637), new NodeId(0, 47), new NodeId(0, 2253).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 12637), new NodeId(0, 40), new NodeId(0, 12581).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode162() {
    var node =
        new CertificateGroupFolderTypeNode(
            this.context,
            new NodeId(0, 14053),
            new QualifiedName(0, "CertificateGroups"),
            new LocalizedText("", "CertificateGroups"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(new NodeId(0, 15644), new PermissionType(UInteger.valueOf(1))),
              new RolePermissionType(
                  new NodeId(0, 15716), new PermissionType(UInteger.valueOf(65423))),
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(65423)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 14053), new NodeId(0, 47), new NodeId(0, 14156).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14053), new NodeId(0, 47), new NodeId(0, 14088).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14053), new NodeId(0, 47), new NodeId(0, 14122).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14053), new NodeId(0, 40), new NodeId(0, 13813).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14053), new NodeId(0, 47), new NodeId(0, 12637).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode163() {
    var node =
        new CertificateGroupTypeNode(
            this.context,
            new NodeId(0, 14156),
            new QualifiedName(0, "DefaultApplicationGroup"),
            new LocalizedText("", "DefaultApplicationGroup"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(65423)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 14156), new NodeId(0, 47), new NodeId(0, 12642).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14156), new NodeId(0, 46), new NodeId(0, 14161).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14156), new NodeId(0, 9006), new NodeId(0, 13225).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14156), new NodeId(0, 40), new NodeId(0, 12555).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14156), new NodeId(0, 47), new NodeId(0, 14053).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode164() {
    var node =
        new TrustListTypeNode(
            this.context,
            new NodeId(0, 12642),
            new QualifiedName(0, "TrustList"),
            new LocalizedText("", "TrustList"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(65423)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 12642), new NodeId(0, 46), new NodeId(0, 12643).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12642), new NodeId(0, 46), new NodeId(0, 14157).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12642), new NodeId(0, 46), new NodeId(0, 14158).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12642), new NodeId(0, 46), new NodeId(0, 12646).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12642), new NodeId(0, 47), new NodeId(0, 12647).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12642), new NodeId(0, 47), new NodeId(0, 12650).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12642), new NodeId(0, 47), new NodeId(0, 12652).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12642), new NodeId(0, 47), new NodeId(0, 12655).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12642), new NodeId(0, 47), new NodeId(0, 12657).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12642), new NodeId(0, 47), new NodeId(0, 12660).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12642), new NodeId(0, 46), new NodeId(0, 12662).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12642), new NodeId(0, 47), new NodeId(0, 12663).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12642), new NodeId(0, 47), new NodeId(0, 12666).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12642), new NodeId(0, 47), new NodeId(0, 12668).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12642), new NodeId(0, 47), new NodeId(0, 12670).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12642), new NodeId(0, 40), new NodeId(0, 12522).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12642), new NodeId(0, 47), new NodeId(0, 14156).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode165() {
    var node =
        new CertificateGroupTypeNode(
            this.context,
            new NodeId(0, 14088),
            new QualifiedName(0, "DefaultHttpsGroup"),
            new LocalizedText("", "DefaultHttpsGroup"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(65423)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 14088), new NodeId(0, 47), new NodeId(0, 14089).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14088), new NodeId(0, 46), new NodeId(0, 14121).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14088), new NodeId(0, 9006), new NodeId(0, 13225).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14088), new NodeId(0, 40), new NodeId(0, 12555).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14088), new NodeId(0, 47), new NodeId(0, 14053).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode166() {
    var node =
        new TrustListTypeNode(
            this.context,
            new NodeId(0, 14089),
            new QualifiedName(0, "TrustList"),
            new LocalizedText("", "TrustList"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(65423)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 14089), new NodeId(0, 46), new NodeId(0, 14090).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14089), new NodeId(0, 46), new NodeId(0, 14091).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14089), new NodeId(0, 46), new NodeId(0, 14092).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14089), new NodeId(0, 46), new NodeId(0, 14093).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14089), new NodeId(0, 47), new NodeId(0, 14095).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14089), new NodeId(0, 47), new NodeId(0, 14098).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14089), new NodeId(0, 47), new NodeId(0, 14100).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14089), new NodeId(0, 47), new NodeId(0, 14103).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14089), new NodeId(0, 47), new NodeId(0, 14105).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14089), new NodeId(0, 47), new NodeId(0, 14108).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14089), new NodeId(0, 46), new NodeId(0, 14110).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14089), new NodeId(0, 47), new NodeId(0, 14111).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14089), new NodeId(0, 47), new NodeId(0, 14114).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14089), new NodeId(0, 47), new NodeId(0, 14117).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14089), new NodeId(0, 47), new NodeId(0, 14119).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14089), new NodeId(0, 40), new NodeId(0, 12522).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14089), new NodeId(0, 47), new NodeId(0, 14088).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode167() {
    var node =
        new CertificateGroupTypeNode(
            this.context,
            new NodeId(0, 14122),
            new QualifiedName(0, "DefaultUserTokenGroup"),
            new LocalizedText("", "DefaultUserTokenGroup"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(65423)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 14122), new NodeId(0, 47), new NodeId(0, 14123).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14122), new NodeId(0, 46), new NodeId(0, 14155).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14122), new NodeId(0, 9006), new NodeId(0, 13225).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14122), new NodeId(0, 40), new NodeId(0, 12555).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14122), new NodeId(0, 47), new NodeId(0, 14053).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode168() {
    var node =
        new TrustListTypeNode(
            this.context,
            new NodeId(0, 14123),
            new QualifiedName(0, "TrustList"),
            new LocalizedText("", "TrustList"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(65423)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 14123), new NodeId(0, 46), new NodeId(0, 14124).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14123), new NodeId(0, 46), new NodeId(0, 14125).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14123), new NodeId(0, 46), new NodeId(0, 14126).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14123), new NodeId(0, 46), new NodeId(0, 14127).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14123), new NodeId(0, 47), new NodeId(0, 14129).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14123), new NodeId(0, 47), new NodeId(0, 14132).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14123), new NodeId(0, 47), new NodeId(0, 14134).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14123), new NodeId(0, 47), new NodeId(0, 14137).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14123), new NodeId(0, 47), new NodeId(0, 14139).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14123), new NodeId(0, 47), new NodeId(0, 14142).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14123), new NodeId(0, 46), new NodeId(0, 14144).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14123), new NodeId(0, 47), new NodeId(0, 14145).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14123), new NodeId(0, 47), new NodeId(0, 14148).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14123), new NodeId(0, 47), new NodeId(0, 14151).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14123), new NodeId(0, 47), new NodeId(0, 14153).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14123), new NodeId(0, 40), new NodeId(0, 12522).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14123), new NodeId(0, 47), new NodeId(0, 14122).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode169() {
    var node =
        new TransactionDiagnosticsTypeNode(
            this.context,
            new NodeId(0, 32336),
            new QualifiedName(0, "TransactionDiagnostics"),
            new LocalizedText("", "TransactionDiagnostics"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(65423)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 32336), new NodeId(0, 46), new NodeId(0, 32337).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32336), new NodeId(0, 46), new NodeId(0, 32338).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32336), new NodeId(0, 46), new NodeId(0, 32339).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32336), new NodeId(0, 46), new NodeId(0, 32340).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32336), new NodeId(0, 46), new NodeId(0, 32341).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32336), new NodeId(0, 46), new NodeId(0, 32342).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32336), new NodeId(0, 40), new NodeId(0, 32286).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32336), new NodeId(0, 47), new NodeId(0, 12637).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode170() {
    var node =
        new KeyCredentialConfigurationTypeNode(
            this.context,
            new NodeId(0, 17511),
            new QualifiedName(0, "<ServiceName>"),
            new LocalizedText("", "<ServiceName>"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 17511), new NodeId(0, 46), new NodeId(0, 17512).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17511), new NodeId(0, 46), new NodeId(0, 17513).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17511), new NodeId(0, 40), new NodeId(0, 18001).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17511), new NodeId(0, 37), new NodeId(0, 11508).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17511), new NodeId(0, 47), new NodeId(0, 17496).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode171() {
    var node =
        new KeyCredentialConfigurationFolderTypeNode(
            this.context,
            new NodeId(0, 18155),
            new QualifiedName(0, "KeyCredentialConfiguration"),
            new LocalizedText("", "KeyCredentialConfiguration"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 18155), new NodeId(0, 47), new NodeId(0, 17528).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18155), new NodeId(0, 47), new NodeId(0, 12637).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 18155), new NodeId(0, 40), new NodeId(0, 17496).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode172() {
    var node =
        new AuthorizationServiceConfigurationTypeNode(
            this.context,
            new NodeId(0, 23557),
            new QualifiedName(0, "<ServiceName>"),
            new LocalizedText("", "<ServiceName>"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 23557), new NodeId(0, 46), new NodeId(0, 23558).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23557), new NodeId(0, 46), new NodeId(0, 23559).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23557), new NodeId(0, 46), new NodeId(0, 23560).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23557), new NodeId(0, 40), new NodeId(0, 17852).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23557), new NodeId(0, 37), new NodeId(0, 11508).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23557), new NodeId(0, 47), new NodeId(0, 23556).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode173() {
    var node =
        new FolderTypeNode(
            this.context,
            new NodeId(0, 17732),
            new QualifiedName(0, "AuthorizationServices"),
            new LocalizedText("", "AuthorizationServices"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 17732), new NodeId(0, 47), new NodeId(0, 12637).expanded(), false));
    node.addReference(
        new Reference(new NodeId(0, 17732), new NodeId(0, 40), new NodeId(0, 61).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode174() {
    var node =
        new AggregateFunctionTypeNode(
            this.context,
            new NodeId(0, 2341),
            new QualifiedName(0, "Interpolative"),
            new LocalizedText("", "Interpolative"),
            new LocalizedText(
                "",
                "At the beginning of each interval, retrieve the calculated value from the data"
                    + " points on either side of the requested timestamp."),
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 2341), new NodeId(0, 40), new NodeId(0, 2340).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode175() {
    var node =
        new AggregateFunctionTypeNode(
            this.context,
            new NodeId(0, 2342),
            new QualifiedName(0, "Average"),
            new LocalizedText("", "Average"),
            new LocalizedText("", "Retrieve the average value of the data over the interval."),
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 2342), new NodeId(0, 40), new NodeId(0, 2340).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode176() {
    var node =
        new AggregateFunctionTypeNode(
            this.context,
            new NodeId(0, 2343),
            new QualifiedName(0, "TimeAverage"),
            new LocalizedText("", "TimeAverage"),
            new LocalizedText(
                "",
                "Retrieve the time weighted average data over the interval using Interpolated"
                    + " Bounding Values."),
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 2343), new NodeId(0, 40), new NodeId(0, 2340).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode177() {
    var node =
        new AggregateFunctionTypeNode(
            this.context,
            new NodeId(0, 11285),
            new QualifiedName(0, "TimeAverage2"),
            new LocalizedText("", "TimeAverage2"),
            new LocalizedText(
                "",
                "Retrieve the time weighted average data over the interval using Simple Bounding"
                    + " Values."),
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 11285), new NodeId(0, 40), new NodeId(0, 2340).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode178() {
    var node =
        new AggregateFunctionTypeNode(
            this.context,
            new NodeId(0, 2344),
            new QualifiedName(0, "Total"),
            new LocalizedText("", "Total"),
            new LocalizedText(
                "",
                "Retrieve the total (time integral) of the data over the interval using"
                    + " Interpolated Bounding Values."),
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 2344), new NodeId(0, 40), new NodeId(0, 2340).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode179() {
    var node =
        new AggregateFunctionTypeNode(
            this.context,
            new NodeId(0, 11304),
            new QualifiedName(0, "Total2"),
            new LocalizedText("", "Total2"),
            new LocalizedText(
                "",
                "Retrieve the total (time integral) of the data over the interval using Simple"
                    + " Bounding Values."),
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 11304), new NodeId(0, 40), new NodeId(0, 2340).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode180() {
    var node =
        new AggregateFunctionTypeNode(
            this.context,
            new NodeId(0, 2346),
            new QualifiedName(0, "Minimum"),
            new LocalizedText("", "Minimum"),
            new LocalizedText(
                "",
                "Retrieve the minimum raw value in the interval with the timestamp of the start of"
                    + " the interval."),
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 2346), new NodeId(0, 40), new NodeId(0, 2340).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode181() {
    var node =
        new AggregateFunctionTypeNode(
            this.context,
            new NodeId(0, 2347),
            new QualifiedName(0, "Maximum"),
            new LocalizedText("", "Maximum"),
            new LocalizedText(
                "",
                "Retrieve the maximum raw value in the interval with the timestamp of the start of"
                    + " the interval."),
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 2347), new NodeId(0, 40), new NodeId(0, 2340).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode182() {
    var node =
        new AggregateFunctionTypeNode(
            this.context,
            new NodeId(0, 2348),
            new QualifiedName(0, "MinimumActualTime"),
            new LocalizedText("", "MinimumActualTime"),
            new LocalizedText(
                "",
                "Retrieve the minimum value in the interval and the Timestamp of the minimum"
                    + " value."),
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 2348), new NodeId(0, 40), new NodeId(0, 2340).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode183() {
    var node =
        new AggregateFunctionTypeNode(
            this.context,
            new NodeId(0, 2349),
            new QualifiedName(0, "MaximumActualTime"),
            new LocalizedText("", "MaximumActualTime"),
            new LocalizedText(
                "",
                "Retrieve the maximum value in the interval and the Timestamp of the maximum"
                    + " value."),
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 2349), new NodeId(0, 40), new NodeId(0, 2340).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode184() {
    var node =
        new AggregateFunctionTypeNode(
            this.context,
            new NodeId(0, 2350),
            new QualifiedName(0, "Range"),
            new LocalizedText("", "Range"),
            new LocalizedText(
                "",
                "Retrieve the difference between the minimum and maximum Value over the interval."),
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 2350), new NodeId(0, 40), new NodeId(0, 2340).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode185() {
    var node =
        new AggregateFunctionTypeNode(
            this.context,
            new NodeId(0, 11286),
            new QualifiedName(0, "Minimum2"),
            new LocalizedText("", "Minimum2"),
            new LocalizedText(
                "",
                "Retrieve the minimum value in the interval including the Simple Bounding Values."),
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 11286), new NodeId(0, 40), new NodeId(0, 2340).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode186() {
    var node =
        new AggregateFunctionTypeNode(
            this.context,
            new NodeId(0, 11287),
            new QualifiedName(0, "Maximum2"),
            new LocalizedText("", "Maximum2"),
            new LocalizedText(
                "",
                "Retrieve the maximum value in the interval including the Simple Bounding Values."),
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 11287), new NodeId(0, 40), new NodeId(0, 2340).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode187() {
    var node =
        new AggregateFunctionTypeNode(
            this.context,
            new NodeId(0, 11305),
            new QualifiedName(0, "MinimumActualTime2"),
            new LocalizedText("", "MinimumActualTime2"),
            new LocalizedText(
                "",
                "Retrieve the minimum value with the actual timestamp including the Simple Bounding"
                    + " Values."),
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 11305), new NodeId(0, 40), new NodeId(0, 2340).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode188() {
    var node =
        new AggregateFunctionTypeNode(
            this.context,
            new NodeId(0, 11306),
            new QualifiedName(0, "MaximumActualTime2"),
            new LocalizedText("", "MaximumActualTime2"),
            new LocalizedText(
                "",
                "Retrieve the maximum value with the actual timestamp including the Simple Bounding"
                    + " Values."),
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 11306), new NodeId(0, 40), new NodeId(0, 2340).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode189() {
    var node =
        new AggregateFunctionTypeNode(
            this.context,
            new NodeId(0, 11288),
            new QualifiedName(0, "Range2"),
            new LocalizedText("", "Range2"),
            new LocalizedText(
                "",
                "Retrieve the difference between the Minimum2 and Maximum2 value over the"
                    + " interval."),
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 11288), new NodeId(0, 40), new NodeId(0, 2340).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode190() {
    var node =
        new AggregateFunctionTypeNode(
            this.context,
            new NodeId(0, 2351),
            new QualifiedName(0, "AnnotationCount"),
            new LocalizedText("", "AnnotationCount"),
            new LocalizedText("", "Retrieve the number of Annotations in the interval."),
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 2351), new NodeId(0, 40), new NodeId(0, 2340).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode191() {
    var node =
        new AggregateFunctionTypeNode(
            this.context,
            new NodeId(0, 2352),
            new QualifiedName(0, "Count"),
            new LocalizedText("", "Count"),
            new LocalizedText("", "Retrieve the number of raw values over the interval."),
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 2352), new NodeId(0, 40), new NodeId(0, 2340).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode192() {
    var node =
        new AggregateFunctionTypeNode(
            this.context,
            new NodeId(0, 11307),
            new QualifiedName(0, "DurationInStateZero"),
            new LocalizedText("", "DurationInStateZero"),
            new LocalizedText(
                "",
                "Retrieve the time a Boolean or numeric was in a zero state using Simple Bounding"
                    + " Values."),
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 11307), new NodeId(0, 40), new NodeId(0, 2340).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode193() {
    var node =
        new AggregateFunctionTypeNode(
            this.context,
            new NodeId(0, 11308),
            new QualifiedName(0, "DurationInStateNonZero"),
            new LocalizedText("", "DurationInStateNonZero"),
            new LocalizedText(
                "",
                "Retrieve the time a Boolean or numeric was in a non-zero state using Simple"
                    + " Bounding Values."),
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 11308), new NodeId(0, 40), new NodeId(0, 2340).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode194() {
    var node =
        new AggregateFunctionTypeNode(
            this.context,
            new NodeId(0, 2355),
            new QualifiedName(0, "NumberOfTransitions"),
            new LocalizedText("", "NumberOfTransitions"),
            new LocalizedText(
                "",
                "Retrieve the number of changes between zero and non-zero that a Boolean or Numeric"
                    + " value experienced in the interval."),
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 2355), new NodeId(0, 40), new NodeId(0, 2340).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode195() {
    var node =
        new AggregateFunctionTypeNode(
            this.context,
            new NodeId(0, 2357),
            new QualifiedName(0, "Start"),
            new LocalizedText("", "Start"),
            new LocalizedText(
                "",
                "Retrieve the value at the beginning of the interval using Interpolated Bounding"
                    + " Values."),
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 2357), new NodeId(0, 40), new NodeId(0, 2340).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode196() {
    var node =
        new AggregateFunctionTypeNode(
            this.context,
            new NodeId(0, 2358),
            new QualifiedName(0, "End"),
            new LocalizedText("", "End"),
            new LocalizedText(
                "",
                "Retrieve the value at the end of the interval using Interpolated Bounding"
                    + " Values."),
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 2358), new NodeId(0, 40), new NodeId(0, 2340).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode197() {
    var node =
        new AggregateFunctionTypeNode(
            this.context,
            new NodeId(0, 2359),
            new QualifiedName(0, "Delta"),
            new LocalizedText("", "Delta"),
            new LocalizedText(
                "", "Retrieve the difference between the Start and End value in the interval."),
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 2359), new NodeId(0, 40), new NodeId(0, 2340).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode198() {
    var node =
        new AggregateFunctionTypeNode(
            this.context,
            new NodeId(0, 11505),
            new QualifiedName(0, "StartBound"),
            new LocalizedText("", "StartBound"),
            new LocalizedText(
                "",
                "Retrieve the value at the beginning of the interval using Simple Bounding"
                    + " Values."),
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 11505), new NodeId(0, 40), new NodeId(0, 2340).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode199() {
    var node =
        new AggregateFunctionTypeNode(
            this.context,
            new NodeId(0, 11506),
            new QualifiedName(0, "EndBound"),
            new LocalizedText("", "EndBound"),
            new LocalizedText(
                "", "Retrieve the value at the end of the interval using Simple Bounding Values."),
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 11506), new NodeId(0, 40), new NodeId(0, 2340).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode200() {
    var node =
        new AggregateFunctionTypeNode(
            this.context,
            new NodeId(0, 11507),
            new QualifiedName(0, "DeltaBounds"),
            new LocalizedText("", "DeltaBounds"),
            new LocalizedText(
                "",
                "Retrieve the difference between the StartBound and EndBound value in the"
                    + " interval."),
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 11507), new NodeId(0, 40), new NodeId(0, 2340).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode201() {
    var node =
        new AggregateFunctionTypeNode(
            this.context,
            new NodeId(0, 2360),
            new QualifiedName(0, "DurationGood"),
            new LocalizedText("", "DurationGood"),
            new LocalizedText(
                "",
                "Retrieve the total duration of time in the interval during which the data is"
                    + " good."),
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 2360), new NodeId(0, 40), new NodeId(0, 2340).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode202() {
    var node =
        new AggregateFunctionTypeNode(
            this.context,
            new NodeId(0, 2361),
            new QualifiedName(0, "DurationBad"),
            new LocalizedText("", "DurationBad"),
            new LocalizedText(
                "",
                "Retrieve the total duration of time in the interval during which the data is"
                    + " bad."),
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 2361), new NodeId(0, 40), new NodeId(0, 2340).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode203() {
    var node =
        new AggregateFunctionTypeNode(
            this.context,
            new NodeId(0, 2362),
            new QualifiedName(0, "PercentGood"),
            new LocalizedText("", "PercentGood"),
            new LocalizedText(
                "",
                "Retrieve the percent of data (0 to 100) in the interval which has a good"
                    + " StatusCode."),
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 2362), new NodeId(0, 40), new NodeId(0, 2340).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode204() {
    var node =
        new AggregateFunctionTypeNode(
            this.context,
            new NodeId(0, 2363),
            new QualifiedName(0, "PercentBad"),
            new LocalizedText("", "PercentBad"),
            new LocalizedText(
                "",
                "Retrieve the percent of data (0 to 100) in the interval which has a bad"
                    + " StatusCode."),
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 2363), new NodeId(0, 40), new NodeId(0, 2340).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode205() {
    var node =
        new AggregateFunctionTypeNode(
            this.context,
            new NodeId(0, 2364),
            new QualifiedName(0, "WorstQuality"),
            new LocalizedText("", "WorstQuality"),
            new LocalizedText("", "Retrieve the worst StatusCode of data in the interval."),
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 2364), new NodeId(0, 40), new NodeId(0, 2340).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode206() {
    var node =
        new AggregateFunctionTypeNode(
            this.context,
            new NodeId(0, 11292),
            new QualifiedName(0, "WorstQuality2"),
            new LocalizedText("", "WorstQuality2"),
            new LocalizedText(
                "",
                "Retrieve the worst StatusCode of data in the interval including the Simple"
                    + " Bounding Values."),
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 11292), new NodeId(0, 40), new NodeId(0, 2340).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode207() {
    var node =
        new AggregateFunctionTypeNode(
            this.context,
            new NodeId(0, 11426),
            new QualifiedName(0, "StandardDeviationSample"),
            new LocalizedText("", "StandardDeviationSample"),
            new LocalizedText(
                "",
                "Retrieve the standard deviation for the interval for a sample of the population"
                    + " (n-1)."),
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 11426), new NodeId(0, 40), new NodeId(0, 2340).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode208() {
    var node =
        new AggregateFunctionTypeNode(
            this.context,
            new NodeId(0, 11427),
            new QualifiedName(0, "StandardDeviationPopulation"),
            new LocalizedText("", "StandardDeviationPopulation"),
            new LocalizedText(
                "",
                "Retrieve the standard deviation for the interval for a complete population (n)"
                    + " which includes Simple Bounding Values."),
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 11427), new NodeId(0, 40), new NodeId(0, 2340).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode209() {
    var node =
        new AggregateFunctionTypeNode(
            this.context,
            new NodeId(0, 11428),
            new QualifiedName(0, "VarianceSample"),
            new LocalizedText("", "VarianceSample"),
            new LocalizedText(
                "",
                "Retrieve the variance for the interval as calculated by the"
                    + " StandardDeviationSample."),
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 11428), new NodeId(0, 40), new NodeId(0, 2340).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode210() {
    var node =
        new AggregateFunctionTypeNode(
            this.context,
            new NodeId(0, 11429),
            new QualifiedName(0, "VariancePopulation"),
            new LocalizedText("", "VariancePopulation"),
            new LocalizedText(
                "",
                "Retrieve the variance for the interval as calculated by the"
                    + " StandardDeviationPopulation which includes Simple Bounding Values."),
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 11429), new NodeId(0, 40), new NodeId(0, 2340).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode211() {
    var node =
        new SecurityGroupFolderTypeNode(
            this.context,
            new NodeId(0, 15913),
            new QualifiedName(0, "SecurityGroups"),
            new LocalizedText("", "SecurityGroups"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 15913), new NodeId(0, 47), new NodeId(0, 15914).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15913), new NodeId(0, 47), new NodeId(0, 15917).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15913), new NodeId(0, 40), new NodeId(0, 15452).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 15913), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15913), new NodeId(0, 47), new NodeId(0, 15906).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode212() {
    var node =
        new PubSubKeyPushTargetFolderTypeNode(
            this.context,
            new NodeId(0, 25277),
            new QualifiedName(0, "KeyPushTargets"),
            new LocalizedText("", "KeyPushTargets"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 25277), new NodeId(0, 47), new NodeId(0, 25278).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25277), new NodeId(0, 47), new NodeId(0, 25281).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25277), new NodeId(0, 40), new NodeId(0, 25346).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 25277), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25277), new NodeId(0, 47), new NodeId(0, 15906).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode213() {
    var node =
        new SecurityGroupFolderTypeNode(
            this.context,
            new NodeId(0, 15453),
            new QualifiedName(0, "<SecurityGroupFolderName>"),
            new LocalizedText("", "<SecurityGroupFolderName>"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 15453), new NodeId(0, 47), new NodeId(0, 15454).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15453), new NodeId(0, 47), new NodeId(0, 15457).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15453), new NodeId(0, 47), new NodeId(0, 25293).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15453), new NodeId(0, 47), new NodeId(0, 25296).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15453), new NodeId(0, 46), new NodeId(0, 25298).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15453), new NodeId(0, 40), new NodeId(0, 15452).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15453), new NodeId(0, 37), new NodeId(0, 11508).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15453), new NodeId(0, 35), new NodeId(0, 15452).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode214() {
    var node =
        new SecurityGroupTypeNode(
            this.context,
            new NodeId(0, 15459),
            new QualifiedName(0, "<SecurityGroupName>"),
            new LocalizedText("", "<SecurityGroupName>"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 15459), new NodeId(0, 46), new NodeId(0, 15460).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15459), new NodeId(0, 46), new NodeId(0, 15010).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15459), new NodeId(0, 46), new NodeId(0, 15011).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15459), new NodeId(0, 46), new NodeId(0, 15012).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15459), new NodeId(0, 46), new NodeId(0, 15043).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15459), new NodeId(0, 40), new NodeId(0, 15471).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15459), new NodeId(0, 37), new NodeId(0, 11508).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15459), new NodeId(0, 47), new NodeId(0, 15452).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode215() {
    var node =
        new SecurityGroupTypeNode(
            this.context,
            new NodeId(0, 25626),
            new QualifiedName(0, "<SecurityGroupName>"),
            new LocalizedText("", "<SecurityGroupName>"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 25626), new NodeId(0, 46), new NodeId(0, 25627).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25626), new NodeId(0, 46), new NodeId(0, 25628).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25626), new NodeId(0, 46), new NodeId(0, 25629).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25626), new NodeId(0, 46), new NodeId(0, 25630).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25626), new NodeId(0, 46), new NodeId(0, 25631).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25626), new NodeId(0, 40), new NodeId(0, 15471).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25626), new NodeId(0, 37), new NodeId(0, 11508).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25626), new NodeId(0, 25345), new NodeId(0, 25337).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode216() {
    var node =
        new PubSubKeyPushTargetFolderTypeNode(
            this.context,
            new NodeId(0, 25347),
            new QualifiedName(0, "<PushTargetFolderName>"),
            new LocalizedText("", "<PushTargetFolderName>"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 25347), new NodeId(0, 47), new NodeId(0, 25348).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25347), new NodeId(0, 47), new NodeId(0, 25351).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25347), new NodeId(0, 47), new NodeId(0, 25353).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25347), new NodeId(0, 47), new NodeId(0, 25356).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25347), new NodeId(0, 40), new NodeId(0, 25346).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25347), new NodeId(0, 37), new NodeId(0, 11508).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25347), new NodeId(0, 35), new NodeId(0, 25346).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode217() {
    var node =
        new PubSubKeyPushTargetTypeNode(
            this.context,
            new NodeId(0, 25358),
            new QualifiedName(0, "<PushTargetName>"),
            new LocalizedText("", "<PushTargetName>"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 25358), new NodeId(0, 46), new NodeId(0, 25648).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25358), new NodeId(0, 46), new NodeId(0, 25649).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25358), new NodeId(0, 46), new NodeId(0, 25361).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25358), new NodeId(0, 46), new NodeId(0, 25650).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25358), new NodeId(0, 46), new NodeId(0, 25651).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25358), new NodeId(0, 46), new NodeId(0, 25652).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25358), new NodeId(0, 46), new NodeId(0, 25653).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25358), new NodeId(0, 46), new NodeId(0, 25654).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25358), new NodeId(0, 47), new NodeId(0, 25655).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25358), new NodeId(0, 47), new NodeId(0, 25658).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25358), new NodeId(0, 47), new NodeId(0, 25661).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25358), new NodeId(0, 40), new NodeId(0, 25337).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25358), new NodeId(0, 37), new NodeId(0, 11508).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25358), new NodeId(0, 47), new NodeId(0, 25346).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode218() {
    var node =
        new PubSubConnectionTypeNode(
            this.context,
            new NodeId(0, 14417),
            new QualifiedName(0, "<ConnectionName>"),
            new LocalizedText("", "<ConnectionName>"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 14417), new NodeId(0, 46), new NodeId(0, 14418).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14417), new NodeId(0, 47), new NodeId(0, 17292).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14417), new NodeId(0, 46), new NodeId(0, 17478).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14417), new NodeId(0, 47), new NodeId(0, 14423).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14417), new NodeId(0, 47), new NodeId(0, 14419).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14417), new NodeId(0, 40), new NodeId(0, 14209).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14417), new NodeId(0, 37), new NodeId(0, 11508).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14417), new NodeId(0, 14476), new NodeId(0, 14416).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode219() {
    var node =
        new NetworkAddressTypeNode(
            this.context,
            new NodeId(0, 14423),
            new QualifiedName(0, "Address"),
            new LocalizedText("", "Address"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 14423), new NodeId(0, 47), new NodeId(0, 15533).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14423), new NodeId(0, 40), new NodeId(0, 21145).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 14423), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14423), new NodeId(0, 47), new NodeId(0, 14417).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode220() {
    var node =
        new PubSubStatusTypeNode(
            this.context,
            new NodeId(0, 14419),
            new QualifiedName(0, "Status"),
            new LocalizedText("", "Status"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 14419), new NodeId(0, 47), new NodeId(0, 14420).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14419), new NodeId(0, 40), new NodeId(0, 14643).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 14419), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14419), new NodeId(0, 47), new NodeId(0, 14417).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode221() {
    var node =
        new DataSetFolderTypeNode(
            this.context,
            new NodeId(0, 14434),
            new QualifiedName(0, "PublishedDataSets"),
            new LocalizedText("", "PublishedDataSets"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 14434), new NodeId(0, 40), new NodeId(0, 14477).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 14434), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14434), new NodeId(0, 47), new NodeId(0, 14416).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode222() {
    var node =
        new SubscribedDataSetFolderTypeNode(
            this.context,
            new NodeId(0, 23622),
            new QualifiedName(0, "SubscribedDataSets"),
            new LocalizedText("", "SubscribedDataSets"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 23622), new NodeId(0, 40), new NodeId(0, 23795).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 23622), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23622), new NodeId(0, 47), new NodeId(0, 14416).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode223() {
    var node =
        new PubSubConfigurationTypeNode(
            this.context,
            new NodeId(0, 25403),
            new QualifiedName(0, "PubSubConfiguration"),
            new LocalizedText("", "PubSubConfiguration"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 25403), new NodeId(0, 46), new NodeId(0, 25404).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25403), new NodeId(0, 46), new NodeId(0, 25405).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25403), new NodeId(0, 46), new NodeId(0, 25406).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25403), new NodeId(0, 46), new NodeId(0, 25407).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25403), new NodeId(0, 47), new NodeId(0, 25411).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25403), new NodeId(0, 47), new NodeId(0, 25414).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25403), new NodeId(0, 47), new NodeId(0, 25416).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25403), new NodeId(0, 47), new NodeId(0, 25419).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25403), new NodeId(0, 47), new NodeId(0, 25421).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25403), new NodeId(0, 47), new NodeId(0, 25424).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25403), new NodeId(0, 47), new NodeId(0, 25426).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25403), new NodeId(0, 47), new NodeId(0, 25429).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25403), new NodeId(0, 40), new NodeId(0, 25482).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 25403), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25403), new NodeId(0, 47), new NodeId(0, 14416).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode224() {
    var node =
        new PubSubStatusTypeNode(
            this.context,
            new NodeId(0, 15844),
            new QualifiedName(0, "Status"),
            new LocalizedText("", "Status"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 15844), new NodeId(0, 47), new NodeId(0, 15845).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15844), new NodeId(0, 40), new NodeId(0, 14643).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 15844), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15844), new NodeId(0, 47), new NodeId(0, 14416).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode225() {
    var node =
        new PubSubDiagnosticsRootTypeNode(
            this.context,
            new NodeId(0, 18715),
            new QualifiedName(0, "Diagnostics"),
            new LocalizedText("", "Diagnostics"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 18715), new NodeId(0, 47), new NodeId(0, 18716).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18715), new NodeId(0, 47), new NodeId(0, 18717).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18715), new NodeId(0, 47), new NodeId(0, 18722).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18715), new NodeId(0, 47), new NodeId(0, 18727).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18715), new NodeId(0, 47), new NodeId(0, 18728).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18715), new NodeId(0, 47), new NodeId(0, 18729).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18715), new NodeId(0, 47), new NodeId(0, 18760).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18715), new NodeId(0, 40), new NodeId(0, 19732).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 18715), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18715), new NodeId(0, 47), new NodeId(0, 14416).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode226() {
    var node =
        new BaseObjectTypeNode(
            this.context,
            new NodeId(0, 18729),
            new QualifiedName(0, "Counters"),
            new LocalizedText("", "Counters"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 18729), new NodeId(0, 47), new NodeId(0, 18730).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18729), new NodeId(0, 47), new NodeId(0, 18735).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18729), new NodeId(0, 47), new NodeId(0, 18740).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18729), new NodeId(0, 47), new NodeId(0, 18745).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18729), new NodeId(0, 47), new NodeId(0, 18750).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18729), new NodeId(0, 47), new NodeId(0, 18755).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 18729), new NodeId(0, 40), new NodeId(0, 58).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 18729), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18729), new NodeId(0, 47), new NodeId(0, 18715).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode227() {
    var node =
        new BaseObjectTypeNode(
            this.context,
            new NodeId(0, 18760),
            new QualifiedName(0, "LiveValues"),
            new LocalizedText("", "LiveValues"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 18760), new NodeId(0, 47), new NodeId(0, 18761).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18760), new NodeId(0, 47), new NodeId(0, 18763).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18760), new NodeId(0, 47), new NodeId(0, 18765).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18760), new NodeId(0, 47), new NodeId(0, 18767).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 18760), new NodeId(0, 40), new NodeId(0, 58).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 18760), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18760), new NodeId(0, 47), new NodeId(0, 18715).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode228() {
    var node =
        new PubSubCapabilitiesTypeNode(
            this.context,
            new NodeId(0, 23642),
            new QualifiedName(0, "PubSubCapablities"),
            new LocalizedText("", "PubSubCapabilities"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 23642), new NodeId(0, 46), new NodeId(0, 23643).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23642), new NodeId(0, 46), new NodeId(0, 23644).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23642), new NodeId(0, 46), new NodeId(0, 23645).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23642), new NodeId(0, 46), new NodeId(0, 23646).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23642), new NodeId(0, 46), new NodeId(0, 23647).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23642), new NodeId(0, 46), new NodeId(0, 23648).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23642), new NodeId(0, 40), new NodeId(0, 23832).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 23642), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23642), new NodeId(0, 47), new NodeId(0, 14416).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode229() {
    var node =
        new FolderTypeNode(
            this.context,
            new NodeId(0, 23649),
            new QualifiedName(0, "DataSetClasses"),
            new LocalizedText("", "DataSetClasses"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 23649), new NodeId(0, 47), new NodeId(0, 24009).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 23649), new NodeId(0, 40), new NodeId(0, 61).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 23649), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23649), new NodeId(0, 47), new NodeId(0, 14416).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode230() {
    var node =
        new PublishSubscribeTypeNode(
            this.context,
            new NodeId(0, 14443),
            new QualifiedName(0, "PublishSubscribe"),
            new LocalizedText("", "PublishSubscribe"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15644), new PermissionType(UInteger.valueOf(4097))),
              new RolePermissionType(
                  new NodeId(0, 15716), new PermissionType(UInteger.valueOf(65423)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 14443), new NodeId(0, 47), new NodeId(0, 15215).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14443), new NodeId(0, 47), new NodeId(0, 15440).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14443), new NodeId(0, 47), new NodeId(0, 15443).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14443), new NodeId(0, 47), new NodeId(0, 25440).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14443), new NodeId(0, 47), new NodeId(0, 17364).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14443), new NodeId(0, 47), new NodeId(0, 17366).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14443), new NodeId(0, 47), new NodeId(0, 17369).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14443), new NodeId(0, 47), new NodeId(0, 17371).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14443), new NodeId(0, 47), new NodeId(0, 23658).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14443), new NodeId(0, 47), new NodeId(0, 25451).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14443), new NodeId(0, 47), new NodeId(0, 17405).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14443), new NodeId(0, 47), new NodeId(0, 17409).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14443), new NodeId(0, 47), new NodeId(0, 23678).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14443), new NodeId(0, 47), new NodeId(0, 23685).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14443), new NodeId(0, 46), new NodeId(0, 17481).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14443), new NodeId(0, 46), new NodeId(0, 25480).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14443), new NodeId(0, 46), new NodeId(0, 25481).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14443), new NodeId(0, 46), new NodeId(0, 32403).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14443), new NodeId(0, 46), new NodeId(0, 32404).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14443), new NodeId(0, 47), new NodeId(0, 2253).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 14443), new NodeId(0, 40), new NodeId(0, 14416).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode231() {
    var node =
        new SecurityGroupFolderTypeNode(
            this.context,
            new NodeId(0, 15443),
            new QualifiedName(0, "SecurityGroups"),
            new LocalizedText("", "SecurityGroups"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15644), new PermissionType(UInteger.valueOf(4097))),
              new RolePermissionType(
                  new NodeId(0, 25565), new PermissionType(UInteger.valueOf(65423)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 15443), new NodeId(0, 47), new NodeId(0, 15444).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15443), new NodeId(0, 47), new NodeId(0, 15447).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15443), new NodeId(0, 40), new NodeId(0, 15452).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15443), new NodeId(0, 47), new NodeId(0, 14443).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode232() {
    var node =
        new PubSubKeyPushTargetFolderTypeNode(
            this.context,
            new NodeId(0, 25440),
            new QualifiedName(0, "KeyPushTargets"),
            new LocalizedText("", "KeyPushTargets"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 25565), new PermissionType(UInteger.valueOf(65423)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(1)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 25440), new NodeId(0, 47), new NodeId(0, 25441).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25440), new NodeId(0, 47), new NodeId(0, 25444).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25440), new NodeId(0, 40), new NodeId(0, 25346).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25440), new NodeId(0, 47), new NodeId(0, 14443).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode233() {
    var node =
        new DataSetFolderTypeNode(
            this.context,
            new NodeId(0, 17371),
            new QualifiedName(0, "PublishedDataSets"),
            new LocalizedText("", "PublishedDataSets"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 17371), new NodeId(0, 40), new NodeId(0, 14477).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17371), new NodeId(0, 47), new NodeId(0, 14443).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode234() {
    var node =
        new SubscribedDataSetFolderTypeNode(
            this.context,
            new NodeId(0, 23658),
            new QualifiedName(0, "SubscribedDataSets"),
            new LocalizedText("", "SubscribedDataSets"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 23658), new NodeId(0, 40), new NodeId(0, 23795).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23658), new NodeId(0, 47), new NodeId(0, 14443).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode235() {
    var node =
        new PubSubConfigurationTypeNode(
            this.context,
            new NodeId(0, 25451),
            new QualifiedName(0, "PubSubConfiguration"),
            new LocalizedText("", "PubSubConfiguration"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15644), new PermissionType(UInteger.valueOf(4097))),
              new RolePermissionType(
                  new NodeId(0, 15716), new PermissionType(UInteger.valueOf(65423)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 25451), new NodeId(0, 46), new NodeId(0, 25452).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25451), new NodeId(0, 46), new NodeId(0, 25453).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25451), new NodeId(0, 46), new NodeId(0, 25454).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25451), new NodeId(0, 46), new NodeId(0, 25455).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25451), new NodeId(0, 47), new NodeId(0, 25459).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25451), new NodeId(0, 47), new NodeId(0, 25462).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25451), new NodeId(0, 47), new NodeId(0, 25464).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25451), new NodeId(0, 47), new NodeId(0, 25467).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25451), new NodeId(0, 47), new NodeId(0, 25469).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25451), new NodeId(0, 47), new NodeId(0, 25472).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25451), new NodeId(0, 47), new NodeId(0, 25474).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25451), new NodeId(0, 47), new NodeId(0, 25477).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25451), new NodeId(0, 40), new NodeId(0, 25482).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25451), new NodeId(0, 47), new NodeId(0, 14443).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode236() {
    var node =
        new PubSubStatusTypeNode(
            this.context,
            new NodeId(0, 17405),
            new QualifiedName(0, "Status"),
            new LocalizedText("", "Status"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 17405), new NodeId(0, 47), new NodeId(0, 17406).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17405), new NodeId(0, 40), new NodeId(0, 14643).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17405), new NodeId(0, 47), new NodeId(0, 14443).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode237() {
    var node =
        new PubSubDiagnosticsRootTypeNode(
            this.context,
            new NodeId(0, 17409),
            new QualifiedName(0, "Diagnostics"),
            new LocalizedText("", "Diagnostics"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15644), new PermissionType(UInteger.valueOf(4097))),
              new RolePermissionType(
                  new NodeId(0, 15716), new PermissionType(UInteger.valueOf(65423)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 17409), new NodeId(0, 47), new NodeId(0, 17410).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17409), new NodeId(0, 47), new NodeId(0, 17411).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17409), new NodeId(0, 47), new NodeId(0, 17416).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17409), new NodeId(0, 47), new NodeId(0, 17421).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17409), new NodeId(0, 47), new NodeId(0, 17422).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17409), new NodeId(0, 47), new NodeId(0, 17423).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17409), new NodeId(0, 47), new NodeId(0, 17457).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17409), new NodeId(0, 40), new NodeId(0, 19732).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17409), new NodeId(0, 47), new NodeId(0, 14443).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode238() {
    var node =
        new BaseObjectTypeNode(
            this.context,
            new NodeId(0, 17423),
            new QualifiedName(0, "Counters"),
            new LocalizedText("", "Counters"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 17423), new NodeId(0, 47), new NodeId(0, 17424).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17423), new NodeId(0, 47), new NodeId(0, 17431).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17423), new NodeId(0, 47), new NodeId(0, 17436).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17423), new NodeId(0, 47), new NodeId(0, 17441).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17423), new NodeId(0, 47), new NodeId(0, 17446).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17423), new NodeId(0, 47), new NodeId(0, 17451).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 17423), new NodeId(0, 40), new NodeId(0, 58).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17423), new NodeId(0, 47), new NodeId(0, 17409).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode239() {
    var node =
        new BaseObjectTypeNode(
            this.context,
            new NodeId(0, 17457),
            new QualifiedName(0, "LiveValues"),
            new LocalizedText("", "LiveValues"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 17457), new NodeId(0, 47), new NodeId(0, 17458).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17457), new NodeId(0, 47), new NodeId(0, 17460).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17457), new NodeId(0, 47), new NodeId(0, 17462).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17457), new NodeId(0, 47), new NodeId(0, 17464).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 17457), new NodeId(0, 40), new NodeId(0, 58).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17457), new NodeId(0, 47), new NodeId(0, 17409).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode240() {
    var node =
        new PubSubCapabilitiesTypeNode(
            this.context,
            new NodeId(0, 23678),
            new QualifiedName(0, "PubSubCapablities"),
            new LocalizedText("", "PubSubCapabilities"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 23678), new NodeId(0, 46), new NodeId(0, 23679).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23678), new NodeId(0, 46), new NodeId(0, 23680).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23678), new NodeId(0, 46), new NodeId(0, 23681).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23678), new NodeId(0, 46), new NodeId(0, 23682).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23678), new NodeId(0, 46), new NodeId(0, 23683).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23678), new NodeId(0, 46), new NodeId(0, 23684).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23678), new NodeId(0, 46), new NodeId(0, 32398).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23678), new NodeId(0, 46), new NodeId(0, 32839).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23678), new NodeId(0, 46), new NodeId(0, 32840).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23678), new NodeId(0, 46), new NodeId(0, 32841).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23678), new NodeId(0, 46), new NodeId(0, 32842).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23678), new NodeId(0, 46), new NodeId(0, 32399).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23678), new NodeId(0, 46), new NodeId(0, 32400).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23678), new NodeId(0, 46), new NodeId(0, 32401).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23678), new NodeId(0, 46), new NodeId(0, 32402).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23678), new NodeId(0, 46), new NodeId(0, 32843).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23678), new NodeId(0, 40), new NodeId(0, 23832).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23678), new NodeId(0, 47), new NodeId(0, 14443).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode241() {
    var node =
        new FolderTypeNode(
            this.context,
            new NodeId(0, 23685),
            new QualifiedName(0, "DataSetClasses"),
            new LocalizedText("", "DataSetClasses"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 23685), new NodeId(0, 40), new NodeId(0, 61).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23685), new NodeId(0, 47), new NodeId(0, 14443).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode242() {
    var node =
        new DataSetWriterTypeNode(
            this.context,
            new NodeId(0, 15222),
            new QualifiedName(0, "<DataSetWriterName>"),
            new LocalizedText("", "<DataSetWriterName>"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 15222), new NodeId(0, 46), new NodeId(0, 16720).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15222), new NodeId(0, 46), new NodeId(0, 16721).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15222), new NodeId(0, 46), new NodeId(0, 17482).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15222), new NodeId(0, 47), new NodeId(0, 15223).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15222), new NodeId(0, 40), new NodeId(0, 15298).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15222), new NodeId(0, 37), new NodeId(0, 11508).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15222), new NodeId(0, 14936), new NodeId(0, 14509).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode243() {
    var node =
        new PubSubStatusTypeNode(
            this.context,
            new NodeId(0, 15223),
            new QualifiedName(0, "Status"),
            new LocalizedText("", "Status"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 15223), new NodeId(0, 47), new NodeId(0, 15224).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15223), new NodeId(0, 40), new NodeId(0, 14643).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 15223), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15223), new NodeId(0, 47), new NodeId(0, 15222).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode244() {
    var node =
        new ExtensionFieldsTypeNode(
            this.context,
            new NodeId(0, 15481),
            new QualifiedName(0, "ExtensionFields"),
            new LocalizedText("", "ExtensionFields"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 15481), new NodeId(0, 47), new NodeId(0, 15482).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15481), new NodeId(0, 47), new NodeId(0, 15485).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15481), new NodeId(0, 40), new NodeId(0, 15489).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 15481), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15481), new NodeId(0, 47), new NodeId(0, 14509).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode245() {
    var node =
        new DataSetFolderTypeNode(
            this.context,
            new NodeId(0, 14478),
            new QualifiedName(0, "<DataSetFolderName>"),
            new LocalizedText("", "<DataSetFolderName>"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 14478), new NodeId(0, 47), new NodeId(0, 14479).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14478), new NodeId(0, 47), new NodeId(0, 14482).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14478), new NodeId(0, 47), new NodeId(0, 16842).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14478), new NodeId(0, 47), new NodeId(0, 16881).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14478), new NodeId(0, 47), new NodeId(0, 14485).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14478), new NodeId(0, 47), new NodeId(0, 16884).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14478), new NodeId(0, 47), new NodeId(0, 16923).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14478), new NodeId(0, 40), new NodeId(0, 14477).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14478), new NodeId(0, 37), new NodeId(0, 11508).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14478), new NodeId(0, 35), new NodeId(0, 14477).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode246() {
    var node =
        new PublishedDataSetTypeNode(
            this.context,
            new NodeId(0, 14487),
            new QualifiedName(0, "<PublishedDataSetName>"),
            new LocalizedText("", "<PublishedDataSetName>"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 14487), new NodeId(0, 46), new NodeId(0, 14489).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14487), new NodeId(0, 46), new NodeId(0, 15221).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14487), new NodeId(0, 40), new NodeId(0, 14509).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14487), new NodeId(0, 37), new NodeId(0, 11508).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14487), new NodeId(0, 47), new NodeId(0, 14477).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode247() {
    var node =
        new NetworkAddressTypeNode(
            this.context,
            new NodeId(0, 14221),
            new QualifiedName(0, "Address"),
            new LocalizedText("", "Address"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 14221), new NodeId(0, 47), new NodeId(0, 17202).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14221), new NodeId(0, 40), new NodeId(0, 21145).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 14221), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14221), new NodeId(0, 47), new NodeId(0, 14209).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode248() {
    var node =
        new ConnectionTransportTypeNode(
            this.context,
            new NodeId(0, 17203),
            new QualifiedName(0, "TransportSettings"),
            new LocalizedText("", "TransportSettings"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 17203), new NodeId(0, 40), new NodeId(0, 17721).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 17203), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17203), new NodeId(0, 47), new NodeId(0, 14209).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode249() {
    var node =
        new WriterGroupTypeNode(
            this.context,
            new NodeId(0, 17310),
            new QualifiedName(0, "<WriterGroupName>"),
            new LocalizedText("", "<WriterGroupName>"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 17310), new NodeId(0, 46), new NodeId(0, 17311).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17310), new NodeId(0, 46), new NodeId(0, 17204).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17310), new NodeId(0, 46), new NodeId(0, 17486).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17310), new NodeId(0, 47), new NodeId(0, 17314).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17310), new NodeId(0, 46), new NodeId(0, 17214).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17310), new NodeId(0, 46), new NodeId(0, 17318).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17310), new NodeId(0, 46), new NodeId(0, 17319).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17310), new NodeId(0, 46), new NodeId(0, 17321).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17310), new NodeId(0, 46), new NodeId(0, 17322).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17310), new NodeId(0, 46), new NodeId(0, 17558).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17310), new NodeId(0, 40), new NodeId(0, 17725).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17310), new NodeId(0, 37), new NodeId(0, 11508).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17310), new NodeId(0, 18804), new NodeId(0, 14209).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode250() {
    var node =
        new PubSubStatusTypeNode(
            this.context,
            new NodeId(0, 17314),
            new QualifiedName(0, "Status"),
            new LocalizedText("", "Status"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 17314), new NodeId(0, 47), new NodeId(0, 17315).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17314), new NodeId(0, 40), new NodeId(0, 14643).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 17314), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17314), new NodeId(0, 47), new NodeId(0, 17310).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode251() {
    var node =
        new ReaderGroupTypeNode(
            this.context,
            new NodeId(0, 17325),
            new QualifiedName(0, "<ReaderGroupName>"),
            new LocalizedText("", "<ReaderGroupName>"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 17325), new NodeId(0, 46), new NodeId(0, 17326).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17325), new NodeId(0, 46), new NodeId(0, 17302).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17325), new NodeId(0, 46), new NodeId(0, 17487).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17325), new NodeId(0, 47), new NodeId(0, 17329).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17325), new NodeId(0, 40), new NodeId(0, 17999).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17325), new NodeId(0, 37), new NodeId(0, 11508).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17325), new NodeId(0, 18805), new NodeId(0, 14209).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode252() {
    var node =
        new PubSubStatusTypeNode(
            this.context,
            new NodeId(0, 17329),
            new QualifiedName(0, "Status"),
            new LocalizedText("", "Status"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 17329), new NodeId(0, 47), new NodeId(0, 17330).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17329), new NodeId(0, 40), new NodeId(0, 14643).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 17329), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17329), new NodeId(0, 47), new NodeId(0, 17325).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode253() {
    var node =
        new PubSubStatusTypeNode(
            this.context,
            new NodeId(0, 14600),
            new QualifiedName(0, "Status"),
            new LocalizedText("", "Status"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 14600), new NodeId(0, 47), new NodeId(0, 14601).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14600), new NodeId(0, 40), new NodeId(0, 14643).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 14600), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14600), new NodeId(0, 47), new NodeId(0, 14209).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode254() {
    var node =
        new PubSubDiagnosticsConnectionTypeNode(
            this.context,
            new NodeId(0, 19241),
            new QualifiedName(0, "Diagnostics"),
            new LocalizedText("", "Diagnostics"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 19241), new NodeId(0, 47), new NodeId(0, 19242).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19241), new NodeId(0, 47), new NodeId(0, 19243).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19241), new NodeId(0, 47), new NodeId(0, 19248).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19241), new NodeId(0, 47), new NodeId(0, 19253).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19241), new NodeId(0, 47), new NodeId(0, 19254).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19241), new NodeId(0, 47), new NodeId(0, 19255).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19241), new NodeId(0, 47), new NodeId(0, 19286).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19241), new NodeId(0, 40), new NodeId(0, 19786).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 19241), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19241), new NodeId(0, 47), new NodeId(0, 14209).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode255() {
    var node =
        new BaseObjectTypeNode(
            this.context,
            new NodeId(0, 19255),
            new QualifiedName(0, "Counters"),
            new LocalizedText("", "Counters"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 19255), new NodeId(0, 47), new NodeId(0, 19256).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19255), new NodeId(0, 47), new NodeId(0, 19261).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19255), new NodeId(0, 47), new NodeId(0, 19266).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19255), new NodeId(0, 47), new NodeId(0, 19271).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19255), new NodeId(0, 47), new NodeId(0, 19276).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19255), new NodeId(0, 47), new NodeId(0, 19281).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 19255), new NodeId(0, 40), new NodeId(0, 58).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 19255), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19255), new NodeId(0, 47), new NodeId(0, 19241).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode256() {
    var node =
        new BaseObjectTypeNode(
            this.context,
            new NodeId(0, 19286),
            new QualifiedName(0, "LiveValues"),
            new LocalizedText("", "LiveValues"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 19286), new NodeId(0, 47), new NodeId(0, 19287).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 19286), new NodeId(0, 40), new NodeId(0, 58).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 19286), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19286), new NodeId(0, 47), new NodeId(0, 19241).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode257() {
    var node =
        new PubSubStatusTypeNode(
            this.context,
            new NodeId(0, 15265),
            new QualifiedName(0, "Status"),
            new LocalizedText("", "Status"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 15265), new NodeId(0, 47), new NodeId(0, 15266).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15265), new NodeId(0, 40), new NodeId(0, 14643).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 15265), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15265), new NodeId(0, 47), new NodeId(0, 14232).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode258() {
    var node =
        new WriterGroupTransportTypeNode(
            this.context,
            new NodeId(0, 17741),
            new QualifiedName(0, "TransportSettings"),
            new LocalizedText("", "TransportSettings"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 17741), new NodeId(0, 40), new NodeId(0, 17997).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 17741), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17741), new NodeId(0, 47), new NodeId(0, 17725).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode259() {
    var node =
        new WriterGroupMessageTypeNode(
            this.context,
            new NodeId(0, 17742),
            new QualifiedName(0, "MessageSettings"),
            new LocalizedText("", "MessageSettings"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 17742), new NodeId(0, 40), new NodeId(0, 17998).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 17742), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17742), new NodeId(0, 47), new NodeId(0, 17725).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode260() {
    var node =
        new DataSetWriterTypeNode(
            this.context,
            new NodeId(0, 17743),
            new QualifiedName(0, "<DataSetWriterName>"),
            new LocalizedText("", "<DataSetWriterName>"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 17743), new NodeId(0, 46), new NodeId(0, 17744).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17743), new NodeId(0, 46), new NodeId(0, 17745).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17743), new NodeId(0, 46), new NodeId(0, 17490).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17743), new NodeId(0, 47), new NodeId(0, 17749).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17743), new NodeId(0, 40), new NodeId(0, 15298).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17743), new NodeId(0, 37), new NodeId(0, 11508).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17743), new NodeId(0, 15296), new NodeId(0, 17725).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode261() {
    var node =
        new PubSubStatusTypeNode(
            this.context,
            new NodeId(0, 17749),
            new QualifiedName(0, "Status"),
            new LocalizedText("", "Status"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 17749), new NodeId(0, 47), new NodeId(0, 17750).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17749), new NodeId(0, 40), new NodeId(0, 14643).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 17749), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17749), new NodeId(0, 47), new NodeId(0, 17743).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode262() {
    var node =
        new PubSubDiagnosticsWriterGroupTypeNode(
            this.context,
            new NodeId(0, 17812),
            new QualifiedName(0, "Diagnostics"),
            new LocalizedText("", "Diagnostics"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 17812), new NodeId(0, 47), new NodeId(0, 17813).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17812), new NodeId(0, 47), new NodeId(0, 17814).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17812), new NodeId(0, 47), new NodeId(0, 17819).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17812), new NodeId(0, 47), new NodeId(0, 17824).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17812), new NodeId(0, 47), new NodeId(0, 17825).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17812), new NodeId(0, 47), new NodeId(0, 17826).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17812), new NodeId(0, 47), new NodeId(0, 17858).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17812), new NodeId(0, 40), new NodeId(0, 19834).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 17812), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17812), new NodeId(0, 47), new NodeId(0, 17725).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode263() {
    var node =
        new BaseObjectTypeNode(
            this.context,
            new NodeId(0, 17826),
            new QualifiedName(0, "Counters"),
            new LocalizedText("", "Counters"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 17826), new NodeId(0, 47), new NodeId(0, 17827).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17826), new NodeId(0, 47), new NodeId(0, 17832).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17826), new NodeId(0, 47), new NodeId(0, 17837).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17826), new NodeId(0, 47), new NodeId(0, 17842).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17826), new NodeId(0, 47), new NodeId(0, 17847).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17826), new NodeId(0, 47), new NodeId(0, 17853).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17826), new NodeId(0, 47), new NodeId(0, 17859).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17826), new NodeId(0, 47), new NodeId(0, 17874).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17826), new NodeId(0, 47), new NodeId(0, 17900).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 17826), new NodeId(0, 40), new NodeId(0, 58).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 17826), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17826), new NodeId(0, 47), new NodeId(0, 17812).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode264() {
    var node =
        new BaseObjectTypeNode(
            this.context,
            new NodeId(0, 17858),
            new QualifiedName(0, "LiveValues"),
            new LocalizedText("", "LiveValues"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 17858), new NodeId(0, 47), new NodeId(0, 17913).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17858), new NodeId(0, 47), new NodeId(0, 17927).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 17858), new NodeId(0, 40), new NodeId(0, 58).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 17858), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17858), new NodeId(0, 47), new NodeId(0, 17812).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode265() {
    var node =
        new DataSetReaderTypeNode(
            this.context,
            new NodeId(0, 18076),
            new QualifiedName(0, "<DataSetReaderName>"),
            new LocalizedText("", "<DataSetReaderName>"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 18076), new NodeId(0, 46), new NodeId(0, 18077).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18076), new NodeId(0, 46), new NodeId(0, 18078).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18076), new NodeId(0, 46), new NodeId(0, 18079).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18076), new NodeId(0, 46), new NodeId(0, 18080).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18076), new NodeId(0, 46), new NodeId(0, 18081).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18076), new NodeId(0, 46), new NodeId(0, 18082).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18076), new NodeId(0, 46), new NodeId(0, 17560).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18076), new NodeId(0, 46), new NodeId(0, 17562).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18076), new NodeId(0, 46), new NodeId(0, 17492).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18076), new NodeId(0, 47), new NodeId(0, 18088).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18076), new NodeId(0, 47), new NodeId(0, 21006).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18076), new NodeId(0, 40), new NodeId(0, 15306).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18076), new NodeId(0, 37), new NodeId(0, 11508).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18076), new NodeId(0, 15297), new NodeId(0, 17999).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode266() {
    var node =
        new PubSubStatusTypeNode(
            this.context,
            new NodeId(0, 18088),
            new QualifiedName(0, "Status"),
            new LocalizedText("", "Status"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 18088), new NodeId(0, 47), new NodeId(0, 18089).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18088), new NodeId(0, 40), new NodeId(0, 14643).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 18088), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18088), new NodeId(0, 47), new NodeId(0, 18076).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode267() {
    var node =
        new SubscribedDataSetTypeNode(
            this.context,
            new NodeId(0, 21006),
            new QualifiedName(0, "SubscribedDataSet"),
            new LocalizedText("", "SubscribedDataSet"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 21006), new NodeId(0, 40), new NodeId(0, 15108).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 21006), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 21006), new NodeId(0, 47), new NodeId(0, 18076).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode268() {
    var node =
        new PubSubDiagnosticsReaderGroupTypeNode(
            this.context,
            new NodeId(0, 21015),
            new QualifiedName(0, "Diagnostics"),
            new LocalizedText("", "Diagnostics"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 21015), new NodeId(0, 47), new NodeId(0, 21016).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 21015), new NodeId(0, 47), new NodeId(0, 21017).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 21015), new NodeId(0, 47), new NodeId(0, 21022).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 21015), new NodeId(0, 47), new NodeId(0, 21027).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 21015), new NodeId(0, 47), new NodeId(0, 21028).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 21015), new NodeId(0, 47), new NodeId(0, 21029).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 21015), new NodeId(0, 47), new NodeId(0, 21060).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 21015), new NodeId(0, 40), new NodeId(0, 19903).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 21015), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 21015), new NodeId(0, 47), new NodeId(0, 17999).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode269() {
    var node =
        new BaseObjectTypeNode(
            this.context,
            new NodeId(0, 21029),
            new QualifiedName(0, "Counters"),
            new LocalizedText("", "Counters"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 21029), new NodeId(0, 47), new NodeId(0, 21030).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 21029), new NodeId(0, 47), new NodeId(0, 21035).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 21029), new NodeId(0, 47), new NodeId(0, 21040).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 21029), new NodeId(0, 47), new NodeId(0, 21045).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 21029), new NodeId(0, 47), new NodeId(0, 21050).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 21029), new NodeId(0, 47), new NodeId(0, 21055).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 21029), new NodeId(0, 47), new NodeId(0, 21061).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 21029), new NodeId(0, 40), new NodeId(0, 58).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 21029), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 21029), new NodeId(0, 47), new NodeId(0, 21015).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode270() {
    var node =
        new BaseObjectTypeNode(
            this.context,
            new NodeId(0, 21060),
            new QualifiedName(0, "LiveValues"),
            new LocalizedText("", "LiveValues"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 21060), new NodeId(0, 47), new NodeId(0, 21076).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 21060), new NodeId(0, 47), new NodeId(0, 21078).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 21060), new NodeId(0, 40), new NodeId(0, 58).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 21060), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 21060), new NodeId(0, 47), new NodeId(0, 21015).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode271() {
    var node =
        new ReaderGroupTransportTypeNode(
            this.context,
            new NodeId(0, 21080),
            new QualifiedName(0, "TransportSettings"),
            new LocalizedText("", "TransportSettings"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 21080), new NodeId(0, 40), new NodeId(0, 21090).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 21080), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 21080), new NodeId(0, 47), new NodeId(0, 17999).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode272() {
    var node =
        new ReaderGroupMessageTypeNode(
            this.context,
            new NodeId(0, 21081),
            new QualifiedName(0, "MessageSettings"),
            new LocalizedText("", "MessageSettings"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 21081), new NodeId(0, 40), new NodeId(0, 21091).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 21081), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 21081), new NodeId(0, 47), new NodeId(0, 17999).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode273() {
    var node =
        new DataSetWriterTransportTypeNode(
            this.context,
            new NodeId(0, 15303),
            new QualifiedName(0, "TransportSettings"),
            new LocalizedText("", "TransportSettings"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 15303), new NodeId(0, 40), new NodeId(0, 15305).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 15303), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15303), new NodeId(0, 47), new NodeId(0, 15298).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode274() {
    var node =
        new DataSetWriterMessageTypeNode(
            this.context,
            new NodeId(0, 21095),
            new QualifiedName(0, "MessageSettings"),
            new LocalizedText("", "MessageSettings"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 21095), new NodeId(0, 40), new NodeId(0, 21096).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 21095), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 21095), new NodeId(0, 47), new NodeId(0, 15298).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode275() {
    var node =
        new PubSubStatusTypeNode(
            this.context,
            new NodeId(0, 15299),
            new QualifiedName(0, "Status"),
            new LocalizedText("", "Status"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 15299), new NodeId(0, 47), new NodeId(0, 15300).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15299), new NodeId(0, 40), new NodeId(0, 14643).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 15299), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15299), new NodeId(0, 47), new NodeId(0, 15298).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode276() {
    var node =
        new PubSubDiagnosticsDataSetWriterTypeNode(
            this.context,
            new NodeId(0, 19550),
            new QualifiedName(0, "Diagnostics"),
            new LocalizedText("", "Diagnostics"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 19550), new NodeId(0, 47), new NodeId(0, 19551).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19550), new NodeId(0, 47), new NodeId(0, 19552).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19550), new NodeId(0, 47), new NodeId(0, 19557).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19550), new NodeId(0, 47), new NodeId(0, 19562).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19550), new NodeId(0, 47), new NodeId(0, 19563).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19550), new NodeId(0, 47), new NodeId(0, 19564).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19550), new NodeId(0, 47), new NodeId(0, 19595).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19550), new NodeId(0, 40), new NodeId(0, 19968).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 19550), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19550), new NodeId(0, 47), new NodeId(0, 15298).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode277() {
    var node =
        new BaseObjectTypeNode(
            this.context,
            new NodeId(0, 19564),
            new QualifiedName(0, "Counters"),
            new LocalizedText("", "Counters"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 19564), new NodeId(0, 47), new NodeId(0, 19565).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19564), new NodeId(0, 47), new NodeId(0, 19570).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19564), new NodeId(0, 47), new NodeId(0, 19575).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19564), new NodeId(0, 47), new NodeId(0, 19580).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19564), new NodeId(0, 47), new NodeId(0, 19585).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19564), new NodeId(0, 47), new NodeId(0, 19590).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19564), new NodeId(0, 47), new NodeId(0, 19596).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 19564), new NodeId(0, 40), new NodeId(0, 58).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 19564), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19564), new NodeId(0, 47), new NodeId(0, 19550).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode278() {
    var node =
        new BaseObjectTypeNode(
            this.context,
            new NodeId(0, 19595),
            new QualifiedName(0, "LiveValues"),
            new LocalizedText("", "LiveValues"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 19595), new NodeId(0, 40), new NodeId(0, 58).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 19595), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19595), new NodeId(0, 47), new NodeId(0, 19550).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode279() {
    var node =
        new DataSetReaderTransportTypeNode(
            this.context,
            new NodeId(0, 15311),
            new QualifiedName(0, "TransportSettings"),
            new LocalizedText("", "TransportSettings"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 15311), new NodeId(0, 40), new NodeId(0, 15319).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 15311), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15311), new NodeId(0, 47), new NodeId(0, 15306).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode280() {
    var node =
        new DataSetReaderMessageTypeNode(
            this.context,
            new NodeId(0, 21103),
            new QualifiedName(0, "MessageSettings"),
            new LocalizedText("", "MessageSettings"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 21103), new NodeId(0, 40), new NodeId(0, 21104).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 21103), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 21103), new NodeId(0, 47), new NodeId(0, 15306).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode281() {
    var node =
        new PubSubStatusTypeNode(
            this.context,
            new NodeId(0, 15307),
            new QualifiedName(0, "Status"),
            new LocalizedText("", "Status"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 15307), new NodeId(0, 47), new NodeId(0, 15308).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15307), new NodeId(0, 40), new NodeId(0, 14643).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 15307), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15307), new NodeId(0, 47), new NodeId(0, 15306).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode282() {
    var node =
        new PubSubDiagnosticsDataSetReaderTypeNode(
            this.context,
            new NodeId(0, 19609),
            new QualifiedName(0, "Diagnostics"),
            new LocalizedText("", "Diagnostics"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 19609), new NodeId(0, 47), new NodeId(0, 19610).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19609), new NodeId(0, 47), new NodeId(0, 19611).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19609), new NodeId(0, 47), new NodeId(0, 19616).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19609), new NodeId(0, 47), new NodeId(0, 19621).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19609), new NodeId(0, 47), new NodeId(0, 19622).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19609), new NodeId(0, 47), new NodeId(0, 19623).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19609), new NodeId(0, 47), new NodeId(0, 19654).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19609), new NodeId(0, 40), new NodeId(0, 20027).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 19609), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19609), new NodeId(0, 47), new NodeId(0, 15306).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode283() {
    var node =
        new BaseObjectTypeNode(
            this.context,
            new NodeId(0, 19623),
            new QualifiedName(0, "Counters"),
            new LocalizedText("", "Counters"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 19623), new NodeId(0, 47), new NodeId(0, 19624).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19623), new NodeId(0, 47), new NodeId(0, 19629).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19623), new NodeId(0, 47), new NodeId(0, 19634).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19623), new NodeId(0, 47), new NodeId(0, 19639).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19623), new NodeId(0, 47), new NodeId(0, 19644).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19623), new NodeId(0, 47), new NodeId(0, 19649).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19623), new NodeId(0, 47), new NodeId(0, 19655).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 19623), new NodeId(0, 40), new NodeId(0, 58).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 19623), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19623), new NodeId(0, 47), new NodeId(0, 19609).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode284() {
    var node =
        new BaseObjectTypeNode(
            this.context,
            new NodeId(0, 19654),
            new QualifiedName(0, "LiveValues"),
            new LocalizedText("", "LiveValues"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 19654), new NodeId(0, 40), new NodeId(0, 58).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 19654), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19654), new NodeId(0, 47), new NodeId(0, 19609).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode285() {
    var node =
        new SubscribedDataSetTypeNode(
            this.context,
            new NodeId(0, 15316),
            new QualifiedName(0, "SubscribedDataSet"),
            new LocalizedText("", "SubscribedDataSet"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 15316), new NodeId(0, 40), new NodeId(0, 15108).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 15316), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15316), new NodeId(0, 47), new NodeId(0, 15306).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode286() {
    var node =
        new SubscribedDataSetFolderTypeNode(
            this.context,
            new NodeId(0, 23796),
            new QualifiedName(0, "<SubscribedDataSetFolderName>"),
            new LocalizedText("", "<SubscribedDataSetFolderName>"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 23796), new NodeId(0, 47), new NodeId(0, 23797).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23796), new NodeId(0, 47), new NodeId(0, 23800).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23796), new NodeId(0, 47), new NodeId(0, 23802).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23796), new NodeId(0, 47), new NodeId(0, 23805).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23796), new NodeId(0, 40), new NodeId(0, 23795).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23796), new NodeId(0, 37), new NodeId(0, 11508).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23796), new NodeId(0, 35), new NodeId(0, 23795).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode287() {
    var node =
        new StandaloneSubscribedDataSetTypeNode(
            this.context,
            new NodeId(0, 23807),
            new QualifiedName(0, "<StandaloneSubscribedDataSetName>"),
            new LocalizedText("", "<StandaloneSubscribedDataSetName>"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 23807), new NodeId(0, 47), new NodeId(0, 23808).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23807), new NodeId(0, 46), new NodeId(0, 23809).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23807), new NodeId(0, 46), new NodeId(0, 23810).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23807), new NodeId(0, 40), new NodeId(0, 23828).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23807), new NodeId(0, 37), new NodeId(0, 11508).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23807), new NodeId(0, 47), new NodeId(0, 23795).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode288() {
    var node =
        new SubscribedDataSetTypeNode(
            this.context,
            new NodeId(0, 23808),
            new QualifiedName(0, "SubscribedDataSet"),
            new LocalizedText("", "SubscribedDataSet"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 23808), new NodeId(0, 40), new NodeId(0, 15108).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 23808), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23808), new NodeId(0, 47), new NodeId(0, 23807).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode289() {
    var node =
        new SubscribedDataSetTypeNode(
            this.context,
            new NodeId(0, 23829),
            new QualifiedName(0, "SubscribedDataSet"),
            new LocalizedText("", "SubscribedDataSet"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 23829), new NodeId(0, 40), new NodeId(0, 15108).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 23829), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23829), new NodeId(0, 47), new NodeId(0, 23828).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode290() {
    var node =
        new BaseObjectTypeNode(
            this.context,
            new NodeId(0, 19691),
            new QualifiedName(0, "Counters"),
            new LocalizedText("", "Counters"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 19691), new NodeId(0, 47), new NodeId(0, 19692).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19691), new NodeId(0, 47), new NodeId(0, 19697).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19691), new NodeId(0, 47), new NodeId(0, 19702).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19691), new NodeId(0, 47), new NodeId(0, 19707).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19691), new NodeId(0, 47), new NodeId(0, 19712).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19691), new NodeId(0, 47), new NodeId(0, 19717).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 19691), new NodeId(0, 40), new NodeId(0, 58).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 19691), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19691), new NodeId(0, 47), new NodeId(0, 19677).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode291() {
    var node =
        new BaseObjectTypeNode(
            this.context,
            new NodeId(0, 19722),
            new QualifiedName(0, "LiveValues"),
            new LocalizedText("", "LiveValues"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 19722), new NodeId(0, 40), new NodeId(0, 58).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 19722), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19722), new NodeId(0, 47), new NodeId(0, 19677).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode292() {
    var node =
        new BaseObjectTypeNode(
            this.context,
            new NodeId(0, 19777),
            new QualifiedName(0, "LiveValues"),
            new LocalizedText("", "LiveValues"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 19777), new NodeId(0, 47), new NodeId(0, 19778).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19777), new NodeId(0, 47), new NodeId(0, 19780).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19777), new NodeId(0, 47), new NodeId(0, 19782).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19777), new NodeId(0, 47), new NodeId(0, 19784).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 19777), new NodeId(0, 40), new NodeId(0, 58).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 19777), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19777), new NodeId(0, 47), new NodeId(0, 19732).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode293() {
    var node =
        new BaseObjectTypeNode(
            this.context,
            new NodeId(0, 19831),
            new QualifiedName(0, "LiveValues"),
            new LocalizedText("", "LiveValues"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 19831), new NodeId(0, 47), new NodeId(0, 19832).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 19831), new NodeId(0, 40), new NodeId(0, 58).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 19831), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19831), new NodeId(0, 47), new NodeId(0, 19786).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode294() {
    var node =
        new BaseObjectTypeNode(
            this.context,
            new NodeId(0, 19848),
            new QualifiedName(0, "Counters"),
            new LocalizedText("", "Counters"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 19848), new NodeId(0, 47), new NodeId(0, 19880).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19848), new NodeId(0, 47), new NodeId(0, 19885).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19848), new NodeId(0, 47), new NodeId(0, 19890).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 19848), new NodeId(0, 40), new NodeId(0, 58).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 19848), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19848), new NodeId(0, 47), new NodeId(0, 19834).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode295() {
    var node =
        new BaseObjectTypeNode(
            this.context,
            new NodeId(0, 19879),
            new QualifiedName(0, "LiveValues"),
            new LocalizedText("", "LiveValues"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 19879), new NodeId(0, 47), new NodeId(0, 19895).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19879), new NodeId(0, 47), new NodeId(0, 19897).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19879), new NodeId(0, 47), new NodeId(0, 19899).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19879), new NodeId(0, 47), new NodeId(0, 19901).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 19879), new NodeId(0, 40), new NodeId(0, 58).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 19879), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19879), new NodeId(0, 47), new NodeId(0, 19834).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode296() {
    var node =
        new BaseObjectTypeNode(
            this.context,
            new NodeId(0, 19917),
            new QualifiedName(0, "Counters"),
            new LocalizedText("", "Counters"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 19917), new NodeId(0, 47), new NodeId(0, 19949).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19917), new NodeId(0, 47), new NodeId(0, 19954).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19917), new NodeId(0, 47), new NodeId(0, 19959).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 19917), new NodeId(0, 40), new NodeId(0, 58).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 19917), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19917), new NodeId(0, 47), new NodeId(0, 19903).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode297() {
    var node =
        new BaseObjectTypeNode(
            this.context,
            new NodeId(0, 19948),
            new QualifiedName(0, "LiveValues"),
            new LocalizedText("", "LiveValues"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 19948), new NodeId(0, 47), new NodeId(0, 19964).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19948), new NodeId(0, 47), new NodeId(0, 19966).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 19948), new NodeId(0, 40), new NodeId(0, 58).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 19948), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19948), new NodeId(0, 47), new NodeId(0, 19903).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode298() {
    var node =
        new BaseObjectTypeNode(
            this.context,
            new NodeId(0, 19982),
            new QualifiedName(0, "Counters"),
            new LocalizedText("", "Counters"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 19982), new NodeId(0, 47), new NodeId(0, 20014).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 19982), new NodeId(0, 40), new NodeId(0, 58).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 19982), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19982), new NodeId(0, 47), new NodeId(0, 19968).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode299() {
    var node =
        new BaseObjectTypeNode(
            this.context,
            new NodeId(0, 20013),
            new QualifiedName(0, "LiveValues"),
            new LocalizedText("", "LiveValues"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 20013), new NodeId(0, 47), new NodeId(0, 20019).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 20013), new NodeId(0, 47), new NodeId(0, 20021).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 20013), new NodeId(0, 47), new NodeId(0, 20023).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 20013), new NodeId(0, 47), new NodeId(0, 20025).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 20013), new NodeId(0, 40), new NodeId(0, 58).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 20013), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 20013), new NodeId(0, 47), new NodeId(0, 19968).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode300() {
    var node =
        new BaseObjectTypeNode(
            this.context,
            new NodeId(0, 20041),
            new QualifiedName(0, "Counters"),
            new LocalizedText("", "Counters"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 20041), new NodeId(0, 47), new NodeId(0, 20073).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 20041), new NodeId(0, 47), new NodeId(0, 20078).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 20041), new NodeId(0, 40), new NodeId(0, 58).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 20041), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 20041), new NodeId(0, 47), new NodeId(0, 20027).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode301() {
    var node =
        new BaseObjectTypeNode(
            this.context,
            new NodeId(0, 20072),
            new QualifiedName(0, "LiveValues"),
            new LocalizedText("", "LiveValues"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 20072), new NodeId(0, 47), new NodeId(0, 20083).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 20072), new NodeId(0, 47), new NodeId(0, 20085).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 20072), new NodeId(0, 47), new NodeId(0, 20087).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 20072), new NodeId(0, 47), new NodeId(0, 20089).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 20072), new NodeId(0, 47), new NodeId(0, 20091).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 20072), new NodeId(0, 47), new NodeId(0, 20093).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 20072), new NodeId(0, 40), new NodeId(0, 58).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 20072), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 20072), new NodeId(0, 47), new NodeId(0, 20027).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode302() {
    var node =
        new NetworkAddressTypeNode(
            this.context,
            new NodeId(0, 15072),
            new QualifiedName(0, "DiscoveryAddress"),
            new LocalizedText("", "DiscoveryAddress"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 15072), new NodeId(0, 47), new NodeId(0, 15154).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15072), new NodeId(0, 40), new NodeId(0, 21145).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 15072), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15072), new NodeId(0, 47), new NodeId(0, 15064).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode303() {
    var node =
        new NetworkAddressTypeNode(
            this.context,
            new NodeId(0, 23842),
            new QualifiedName(0, "Address"),
            new LocalizedText("", "Address"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 23842), new NodeId(0, 47), new NodeId(0, 23843).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23842), new NodeId(0, 40), new NodeId(0, 21145).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 23842), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23842), new NodeId(0, 47), new NodeId(0, 21133).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode304() {
    var node =
        new NetworkAddressTypeNode(
            this.context,
            new NodeId(0, 24017),
            new QualifiedName(0, "Address"),
            new LocalizedText("", "Address"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 24017), new NodeId(0, 47), new NodeId(0, 24018).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24017), new NodeId(0, 40), new NodeId(0, 21145).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 24017), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24017), new NodeId(0, 47), new NodeId(0, 24016).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode305() {
    var node =
        new AliasNameTypeNode(
            this.context,
            new NodeId(0, 23457),
            new QualifiedName(0, "<Alias>"),
            new LocalizedText("", "<Alias>"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 23457), new NodeId(0, 40), new NodeId(0, 23455).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23457), new NodeId(0, 37), new NodeId(0, 11508).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23457), new NodeId(0, 35), new NodeId(0, 23456).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode306() {
    var node =
        new AliasNameCategoryTypeNode(
            this.context,
            new NodeId(0, 23458),
            new QualifiedName(0, "<SubAliasNameCategories>"),
            new LocalizedText("", "<SubAliasNameCategories>"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 23458), new NodeId(0, 47), new NodeId(0, 23459).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23458), new NodeId(0, 46), new NodeId(0, 32849).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23458), new NodeId(0, 40), new NodeId(0, 23456).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23458), new NodeId(0, 37), new NodeId(0, 11508).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23458), new NodeId(0, 35), new NodeId(0, 23456).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode307() {
    var node =
        new AliasNameCategoryTypeNode(
            this.context,
            new NodeId(0, 23470),
            new QualifiedName(0, "Aliases"),
            new LocalizedText("", "Aliases"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 23470), new NodeId(0, 47), new NodeId(0, 23476).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23470), new NodeId(0, 46), new NodeId(0, 32852).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23470), new NodeId(0, 35), new NodeId(0, 85).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 23470), new NodeId(0, 40), new NodeId(0, 23456).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode308() {
    var node =
        new AliasNameCategoryTypeNode(
            this.context,
            new NodeId(0, 23479),
            new QualifiedName(0, "TagVariables"),
            new LocalizedText("", "TagVariables"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 23479), new NodeId(0, 47), new NodeId(0, 23485).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23479), new NodeId(0, 35), new NodeId(0, 23470).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 23479), new NodeId(0, 40), new NodeId(0, 23456).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode309() {
    var node =
        new AliasNameCategoryTypeNode(
            this.context,
            new NodeId(0, 23488),
            new QualifiedName(0, "Topics"),
            new LocalizedText("", "Topics"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 23488), new NodeId(0, 47), new NodeId(0, 23494).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23488), new NodeId(0, 35), new NodeId(0, 23470).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 23488), new NodeId(0, 40), new NodeId(0, 23456).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode310() {
    var node =
        new UserManagementTypeNode(
            this.context,
            new NodeId(0, 24290),
            new QualifiedName(0, "UserManagement"),
            new LocalizedText("", "UserManagement"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  new NodeId(0, 15644), new PermissionType(UInteger.valueOf(4097))),
              new RolePermissionType(
                  new NodeId(0, 15704), new PermissionType(UInteger.valueOf(65423)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 24290), new NodeId(0, 46), new NodeId(0, 24301).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24290), new NodeId(0, 46), new NodeId(0, 24302).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24290), new NodeId(0, 46), new NodeId(0, 24303).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24290), new NodeId(0, 46), new NodeId(0, 24291).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24290), new NodeId(0, 47), new NodeId(0, 24304).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24290), new NodeId(0, 47), new NodeId(0, 24306).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24290), new NodeId(0, 47), new NodeId(0, 24308).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24290), new NodeId(0, 47), new NodeId(0, 24310).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24290), new NodeId(0, 47), new NodeId(0, 12637).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 24290), new NodeId(0, 40), new NodeId(0, 24264).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode311() {
    var node =
        new ApplicationConfigurationTypeNode(
            this.context,
            new NodeId(0, 26878),
            new QualifiedName(0, "<ApplicationName>"),
            new LocalizedText("", "<ApplicationName>"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 26878), new NodeId(0, 47), new NodeId(0, 26879).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 26878), new NodeId(0, 46), new NodeId(0, 27997).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 26878), new NodeId(0, 46), new NodeId(0, 27998).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 26878), new NodeId(0, 46), new NodeId(0, 27999).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 26878), new NodeId(0, 46), new NodeId(0, 28000).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 26878), new NodeId(0, 46), new NodeId(0, 28001).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 26878), new NodeId(0, 46), new NodeId(0, 28002).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 26878), new NodeId(0, 46), new NodeId(0, 28003).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 26878), new NodeId(0, 47), new NodeId(0, 28005).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 26878), new NodeId(0, 47), new NodeId(0, 28008).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 26878), new NodeId(0, 47), new NodeId(0, 28010).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 26878), new NodeId(0, 47), new NodeId(0, 28013).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 26878), new NodeId(0, 46), new NodeId(0, 27996).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 26878), new NodeId(0, 40), new NodeId(0, 25731).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 26878), new NodeId(0, 37), new NodeId(0, 11508).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 26878), new NodeId(0, 47), new NodeId(0, 26871).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode312() {
    var node =
        new CertificateGroupFolderTypeNode(
            this.context,
            new NodeId(0, 26879),
            new QualifiedName(0, "CertificateGroups"),
            new LocalizedText("", "CertificateGroups"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 26879), new NodeId(0, 47), new NodeId(0, 26880).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 26879), new NodeId(0, 40), new NodeId(0, 13813).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 26879), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 26879), new NodeId(0, 47), new NodeId(0, 26878).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode313() {
    var node =
        new CertificateGroupTypeNode(
            this.context,
            new NodeId(0, 26880),
            new QualifiedName(0, "DefaultApplicationGroup"),
            new LocalizedText("", "DefaultApplicationGroup"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 26880), new NodeId(0, 47), new NodeId(0, 26881).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 26880), new NodeId(0, 46), new NodeId(0, 26917).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 26880), new NodeId(0, 40), new NodeId(0, 12555).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 26880), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 26880), new NodeId(0, 47), new NodeId(0, 26879).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode314() {
    var node =
        new TrustListTypeNode(
            this.context,
            new NodeId(0, 26881),
            new QualifiedName(0, "TrustList"),
            new LocalizedText("", "TrustList"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 26881), new NodeId(0, 46), new NodeId(0, 26882).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 26881), new NodeId(0, 46), new NodeId(0, 26883).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 26881), new NodeId(0, 46), new NodeId(0, 26884).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 26881), new NodeId(0, 46), new NodeId(0, 26885).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 26881), new NodeId(0, 47), new NodeId(0, 26889).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 26881), new NodeId(0, 47), new NodeId(0, 26892).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 26881), new NodeId(0, 47), new NodeId(0, 26894).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 26881), new NodeId(0, 47), new NodeId(0, 26897).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 26881), new NodeId(0, 47), new NodeId(0, 26899).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 26881), new NodeId(0, 47), new NodeId(0, 26902).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 26881), new NodeId(0, 46), new NodeId(0, 26904).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 26881), new NodeId(0, 47), new NodeId(0, 26907).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 26881), new NodeId(0, 47), new NodeId(0, 26910).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 26881), new NodeId(0, 47), new NodeId(0, 26913).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 26881), new NodeId(0, 47), new NodeId(0, 26915).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 26881), new NodeId(0, 40), new NodeId(0, 12522).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 26881), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 26881), new NodeId(0, 47), new NodeId(0, 26880).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode315() {
    var node =
        new ProvisionableDeviceTypeNode(
            this.context,
            new NodeId(0, 29878),
            new QualifiedName(0, "ProvisionableDevice"),
            new LocalizedText("", "ProvisionableDevice"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 29878), new NodeId(0, 46), new NodeId(0, 29879).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 29878), new NodeId(0, 47), new NodeId(0, 29880).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 29878), new NodeId(0, 35), new NodeId(0, 24226).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 29878), new NodeId(0, 40), new NodeId(0, 26871).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode316() {
    var node =
        new FolderTypeNode(
            this.context,
            new NodeId(0, 24226),
            new QualifiedName(0, "Resources"),
            new LocalizedText("", "Resources"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 24226), new NodeId(0, 47), new NodeId(0, 2253).expanded(), false));
    node.addReference(
        new Reference(new NodeId(0, 24226), new NodeId(0, 40), new NodeId(0, 61).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode317() {
    var node =
        new FolderTypeNode(
            this.context,
            new NodeId(0, 24227),
            new QualifiedName(0, "Communication"),
            new LocalizedText("", "Communication"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 24227), new NodeId(0, 35), new NodeId(0, 24226).expanded(), false));
    node.addReference(
        new Reference(new NodeId(0, 24227), new NodeId(0, 40), new NodeId(0, 61).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode318() {
    var node =
        new FolderTypeNode(
            this.context,
            new NodeId(0, 24228),
            new QualifiedName(0, "MappingTables"),
            new LocalizedText("", "MappingTables"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 24228), new NodeId(0, 35), new NodeId(0, 24227).expanded(), false));
    node.addReference(
        new Reference(new NodeId(0, 24228), new NodeId(0, 40), new NodeId(0, 61).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode319() {
    var node =
        new FolderTypeNode(
            this.context,
            new NodeId(0, 24229),
            new QualifiedName(0, "NetworkInterfaces"),
            new LocalizedText("", "NetworkInterfaces"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 24229), new NodeId(0, 35), new NodeId(0, 24227).expanded(), false));
    node.addReference(
        new Reference(new NodeId(0, 24229), new NodeId(0, 40), new NodeId(0, 61).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode320() {
    var node =
        new FolderTypeNode(
            this.context,
            new NodeId(0, 24230),
            new QualifiedName(0, "Streams"),
            new LocalizedText("", "Streams"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 24230), new NodeId(0, 35), new NodeId(0, 24227).expanded(), false));
    node.addReference(
        new Reference(new NodeId(0, 24230), new NodeId(0, 40), new NodeId(0, 61).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode321() {
    var node =
        new FolderTypeNode(
            this.context,
            new NodeId(0, 24231),
            new QualifiedName(0, "TalkerStreams"),
            new LocalizedText("", "TalkerStreams"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 24231), new NodeId(0, 35), new NodeId(0, 24230).expanded(), false));
    node.addReference(
        new Reference(new NodeId(0, 24231), new NodeId(0, 40), new NodeId(0, 61).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode322() {
    var node =
        new FolderTypeNode(
            this.context,
            new NodeId(0, 24232),
            new QualifiedName(0, "ListenerStreams"),
            new LocalizedText("", "ListenerStreams"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 24232), new NodeId(0, 35), new NodeId(0, 24230).expanded(), false));
    node.addReference(
        new Reference(new NodeId(0, 24232), new NodeId(0, 40), new NodeId(0, 61).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode323() {
    var node =
        new LldpInformationTypeNode(
            this.context,
            new NodeId(0, 18958),
            new QualifiedName(0, "LLDP"),
            new LocalizedText("", "LLDP"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 18958), new NodeId(0, 47), new NodeId(0, 18965).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18958), new NodeId(0, 47), new NodeId(0, 18972).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18958), new NodeId(0, 35), new NodeId(0, 24227).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 18958), new NodeId(0, 40), new NodeId(0, 18973).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode324() {
    var node =
        new LldpLocalSystemTypeNode(
            this.context,
            new NodeId(0, 18965),
            new QualifiedName(0, "LocalSystemData"),
            new LocalizedText("", "LocalSystemData"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 18965), new NodeId(0, 46), new NodeId(0, 18966).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18965), new NodeId(0, 46), new NodeId(0, 18967).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18965), new NodeId(0, 46), new NodeId(0, 18968).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18965), new NodeId(0, 46), new NodeId(0, 18969).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18965), new NodeId(0, 40), new NodeId(0, 19002).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18965), new NodeId(0, 47), new NodeId(0, 18958).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode325() {
    var node =
        new FolderTypeNode(
            this.context,
            new NodeId(0, 18972),
            new QualifiedName(0, "Ports"),
            new LocalizedText("", "Ports"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 18972), new NodeId(0, 40), new NodeId(0, 61).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18972), new NodeId(0, 47), new NodeId(0, 18958).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode326() {
    var node =
        new BaseObjectTypeNode(
            this.context,
            new NodeId(0, 25226),
            new QualifiedName(0, "<InterfaceName>"),
            new LocalizedText("", "<InterfaceName>"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 25226), new NodeId(0, 17603), new NodeId(0, 24148).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 25226), new NodeId(0, 40), new NodeId(0, 58).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25226), new NodeId(0, 37), new NodeId(0, 11508).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25226), new NodeId(0, 25238), new NodeId(0, 25221).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode327() {
    var node =
        new LldpRemoteStatisticsTypeNode(
            this.context,
            new NodeId(0, 18974),
            new QualifiedName(0, "RemoteStatistics"),
            new LocalizedText("", "RemoteStatistics"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 18974), new NodeId(0, 47), new NodeId(0, 18975).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18974), new NodeId(0, 47), new NodeId(0, 18976).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18974), new NodeId(0, 47), new NodeId(0, 18977).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18974), new NodeId(0, 47), new NodeId(0, 18978).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18974), new NodeId(0, 47), new NodeId(0, 18979).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18974), new NodeId(0, 40), new NodeId(0, 18996).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 18974), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18974), new NodeId(0, 47), new NodeId(0, 18973).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode328() {
    var node =
        new LldpLocalSystemTypeNode(
            this.context,
            new NodeId(0, 18980),
            new QualifiedName(0, "LocalSystemData"),
            new LocalizedText("", "LocalSystemData"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 18980), new NodeId(0, 46), new NodeId(0, 18981).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18980), new NodeId(0, 46), new NodeId(0, 18982).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18980), new NodeId(0, 46), new NodeId(0, 18983).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18980), new NodeId(0, 46), new NodeId(0, 18984).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18980), new NodeId(0, 40), new NodeId(0, 19002).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 18980), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18980), new NodeId(0, 47), new NodeId(0, 18973).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode329() {
    var node =
        new FolderTypeNode(
            this.context,
            new NodeId(0, 18987),
            new QualifiedName(0, "Ports"),
            new LocalizedText("", "Ports"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 18987), new NodeId(0, 35), new NodeId(0, 18988).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 18987), new NodeId(0, 40), new NodeId(0, 61).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 18987), new NodeId(0, 37), new NodeId(0, 78).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18987), new NodeId(0, 47), new NodeId(0, 18973).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode330() {
    var node =
        new LldpPortInformationTypeNode(
            this.context,
            new NodeId(0, 18988),
            new QualifiedName(0, "<LldpPortInformation>"),
            new LocalizedText("", "<LldpPortInformation>"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 18988), new NodeId(0, 46), new NodeId(0, 18989).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18988), new NodeId(0, 46), new NodeId(0, 18990).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18988), new NodeId(0, 46), new NodeId(0, 18991).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18988), new NodeId(0, 46), new NodeId(0, 18992).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18988), new NodeId(0, 40), new NodeId(0, 19009).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18988), new NodeId(0, 37), new NodeId(0, 11508).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18988), new NodeId(0, 35), new NodeId(0, 18987).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode331() {
    var node =
        new FolderTypeNode(
            this.context,
            new NodeId(0, 19016),
            new QualifiedName(0, "RemoteSystemsData"),
            new LocalizedText("", "RemoteSystemsData"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 19016), new NodeId(0, 35), new NodeId(0, 19017).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 19016), new NodeId(0, 40), new NodeId(0, 61).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 19016), new NodeId(0, 37), new NodeId(0, 80).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19016), new NodeId(0, 47), new NodeId(0, 19009).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode332() {
    var node =
        new LldpRemoteSystemTypeNode(
            this.context,
            new NodeId(0, 19017),
            new QualifiedName(0, "<LldpRemoteSystem>"),
            new LocalizedText("", "<LldpRemoteSystem>"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 19017), new NodeId(0, 47), new NodeId(0, 19018).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19017), new NodeId(0, 47), new NodeId(0, 19019).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19017), new NodeId(0, 47), new NodeId(0, 19020).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19017), new NodeId(0, 47), new NodeId(0, 19021).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19017), new NodeId(0, 47), new NodeId(0, 19022).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19017), new NodeId(0, 47), new NodeId(0, 19023).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19017), new NodeId(0, 40), new NodeId(0, 19033).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19017), new NodeId(0, 37), new NodeId(0, 11508).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19017), new NodeId(0, 35), new NodeId(0, 19016).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode333() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 12766),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 12766), new NodeId(0, 38), new NodeId(0, 12756).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 12766), new NodeId(0, 39), new NodeId(0, 12770).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 12766), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode334() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 14846),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 14846), new NodeId(0, 38), new NodeId(0, 14533).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 14846), new NodeId(0, 39), new NodeId(0, 14873).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 14846), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode335() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 17537),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 17537), new NodeId(0, 38), new NodeId(0, 16313).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 17537), new NodeId(0, 39), new NodeId(0, 17538).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 17537), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode336() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 17549),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 17549), new NodeId(0, 38), new NodeId(0, 17548).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 17549), new NodeId(0, 39), new NodeId(0, 17550).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 17549), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode337() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 15671),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 15671), new NodeId(0, 38), new NodeId(0, 15528).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 15671), new NodeId(0, 39), new NodeId(0, 15734).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 15671), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode338() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 32422),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 32422), new NodeId(0, 38), new NodeId(0, 32421).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 32422), new NodeId(0, 39), new NodeId(0, 32423).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 32422), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode339() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 18815),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 18815), new NodeId(0, 38), new NodeId(0, 18806).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 18815), new NodeId(0, 39), new NodeId(0, 18824).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 18815), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode340() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 18816),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 18816), new NodeId(0, 38), new NodeId(0, 18807).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 18816), new NodeId(0, 39), new NodeId(0, 18827).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 18816), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode341() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 18817),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 18817), new NodeId(0, 38), new NodeId(0, 18808).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 18817), new NodeId(0, 39), new NodeId(0, 18830).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 18817), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode342() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 18818),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 18818), new NodeId(0, 38), new NodeId(0, 18809).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 18818), new NodeId(0, 39), new NodeId(0, 18833).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 18818), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode343() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 18819),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 18819), new NodeId(0, 38), new NodeId(0, 18810).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 18819), new NodeId(0, 39), new NodeId(0, 18836).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 18819), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode344() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 18820),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 18820), new NodeId(0, 38), new NodeId(0, 18811).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 18820), new NodeId(0, 39), new NodeId(0, 18839).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 18820), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode345() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 18821),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 18821), new NodeId(0, 38), new NodeId(0, 18812).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 18821), new NodeId(0, 39), new NodeId(0, 18842).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 18821), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode346() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 18822),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 18822), new NodeId(0, 38), new NodeId(0, 18813).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 18822), new NodeId(0, 39), new NodeId(0, 18845).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 18822), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode347() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 18823),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 18823), new NodeId(0, 38), new NodeId(0, 18814).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 18823), new NodeId(0, 39), new NodeId(0, 18848).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 18823), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode348() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 15736),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 15736), new NodeId(0, 38), new NodeId(0, 15634).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 15736), new NodeId(0, 39), new NodeId(0, 15738).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 15736), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode349() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 23507),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 23507), new NodeId(0, 38), new NodeId(0, 23498).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 23507), new NodeId(0, 39), new NodeId(0, 23514).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 23507), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode350() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 32560),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 32560), new NodeId(0, 38), new NodeId(0, 32434).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 32560), new NodeId(0, 39), new NodeId(0, 32563).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 32560), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode351() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 32561),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 32561), new NodeId(0, 38), new NodeId(0, 32435).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 32561), new NodeId(0, 39), new NodeId(0, 32566).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 32561), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode352() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 32562),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 32562), new NodeId(0, 38), new NodeId(0, 32438).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 32562), new NodeId(0, 39), new NodeId(0, 32569).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 32562), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode353() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 12680),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 12680), new NodeId(0, 38), new NodeId(0, 12554).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 12680), new NodeId(0, 39), new NodeId(0, 12681).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 12680), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode354() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 32382),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 32382), new NodeId(0, 38), new NodeId(0, 32285).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 32382), new NodeId(0, 39), new NodeId(0, 32383).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 32382), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode355() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 15676),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 15676), new NodeId(0, 38), new NodeId(0, 15534).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 15676), new NodeId(0, 39), new NodeId(0, 15741).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 15676), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode356() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 125),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 125), new NodeId(0, 38), new NodeId(0, 14525).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 125), new NodeId(0, 39), new NodeId(0, 14855).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 125), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode357() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 126),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 126), new NodeId(0, 38), new NodeId(0, 15487).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 126), new NodeId(0, 39), new NodeId(0, 15599).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 126), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode358() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 127),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 127), new NodeId(0, 38), new NodeId(0, 15488).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 127), new NodeId(0, 39), new NodeId(0, 15602).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 127), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode359() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 15421),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 15421), new NodeId(0, 38), new NodeId(0, 15005).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 15421), new NodeId(0, 39), new NodeId(0, 15501).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 15421), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode360() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 15422),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 15422), new NodeId(0, 38), new NodeId(0, 15006).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 15422), new NodeId(0, 39), new NodeId(0, 15521).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 15422), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode361() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 24108),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 24108), new NodeId(0, 38), new NodeId(0, 24105).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 24108), new NodeId(0, 39), new NodeId(0, 24111).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 24108), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode362() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 24109),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 24109), new NodeId(0, 38), new NodeId(0, 24106).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 24109), new NodeId(0, 39), new NodeId(0, 24114).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 24109), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode363() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 24110),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 24110), new NodeId(0, 38), new NodeId(0, 24107).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 24110), new NodeId(0, 39), new NodeId(0, 24117).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 24110), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode364() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 124),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 124), new NodeId(0, 38), new NodeId(0, 14523).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 124), new NodeId(0, 39), new NodeId(0, 14849).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 124), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode365() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 14839),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 14839), new NodeId(0, 38), new NodeId(0, 14524).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 14839), new NodeId(0, 39), new NodeId(0, 14852).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 14839), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode366() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 14847),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 14847), new NodeId(0, 38), new NodeId(0, 14593).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 14847), new NodeId(0, 39), new NodeId(0, 14876).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 14847), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode367() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 15677),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 15677), new NodeId(0, 38), new NodeId(0, 15578).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 15677), new NodeId(0, 39), new NodeId(0, 15766).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 15677), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode368() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 15678),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 15678), new NodeId(0, 38), new NodeId(0, 15580).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 15678), new NodeId(0, 39), new NodeId(0, 15769).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 15678), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode369() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 14323),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 14323), new NodeId(0, 38), new NodeId(0, 14273).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 14323), new NodeId(0, 39), new NodeId(0, 14324).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 14323), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode370() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 15679),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 15679), new NodeId(0, 38), new NodeId(0, 15581).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 15679), new NodeId(0, 39), new NodeId(0, 15772).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 15679), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode371() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 15681),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 15681), new NodeId(0, 38), new NodeId(0, 15582).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 15681), new NodeId(0, 39), new NodeId(0, 15775).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 15681), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode372() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 25529),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 25529), new NodeId(0, 38), new NodeId(0, 25269).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 25529), new NodeId(0, 39), new NodeId(0, 25533).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 25529), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode373() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 18598),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 18598), new NodeId(0, 38), new NodeId(0, 18593).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 18598), new NodeId(0, 39), new NodeId(0, 18601).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 18598), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode374() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 18599),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 18599), new NodeId(0, 38), new NodeId(0, 18594).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 18599), new NodeId(0, 39), new NodeId(0, 18604).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 18599), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode375() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 18600),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 18600), new NodeId(0, 38), new NodeId(0, 18597).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 18600), new NodeId(0, 39), new NodeId(0, 18607).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 18600), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode376() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 18795),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 18795), new NodeId(0, 38), new NodeId(0, 18793).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 18795), new NodeId(0, 39), new NodeId(0, 18931).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 18795), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode377() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 15682),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 15682), new NodeId(0, 38), new NodeId(0, 15597).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 15682), new NodeId(0, 39), new NodeId(0, 15778).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 15682), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode378() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 15683),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 15683), new NodeId(0, 38), new NodeId(0, 15598).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 15683), new NodeId(0, 39), new NodeId(0, 15781).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 15683), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode379() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 15688),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 15688), new NodeId(0, 38), new NodeId(0, 15605).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 15688), new NodeId(0, 39), new NodeId(0, 15784).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 15688), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode380() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 15689),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 15689), new NodeId(0, 38), new NodeId(0, 15609).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 15689), new NodeId(0, 39), new NodeId(0, 15787).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 15689), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode381() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 21150),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 21150), new NodeId(0, 38), new NodeId(0, 15480).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 21150), new NodeId(0, 39), new NodeId(0, 21156).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 21150), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode382() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 15691),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 15691), new NodeId(0, 38), new NodeId(0, 15611).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 15691), new NodeId(0, 39), new NodeId(0, 15793).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 15691), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode383() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 15693),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 15693), new NodeId(0, 38), new NodeId(0, 15616).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 15693), new NodeId(0, 39), new NodeId(0, 15854).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 15693), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode384() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 15694),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 15694), new NodeId(0, 38), new NodeId(0, 15617).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 15694), new NodeId(0, 39), new NodeId(0, 15857).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 15694), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode385() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 15695),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 15695), new NodeId(0, 38), new NodeId(0, 15618).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 15695), new NodeId(0, 39), new NodeId(0, 15860).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 15695), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode386() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 21151),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 21151), new NodeId(0, 38), new NodeId(0, 15502).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 21151), new NodeId(0, 39), new NodeId(0, 21159).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 21151), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode387() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 21152),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 21152), new NodeId(0, 38), new NodeId(0, 15510).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 21152), new NodeId(0, 39), new NodeId(0, 21162).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 21152), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode388() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 21153),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 21153), new NodeId(0, 38), new NodeId(0, 15520).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 21153), new NodeId(0, 39), new NodeId(0, 21165).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 21153), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode389() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 15701),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 15701), new NodeId(0, 38), new NodeId(0, 15621).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 15701), new NodeId(0, 39), new NodeId(0, 15866).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 15701), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode390() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 15702),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 15702), new NodeId(0, 38), new NodeId(0, 15622).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 15702), new NodeId(0, 39), new NodeId(0, 15869).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 15702), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode391() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 15703),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 15703), new NodeId(0, 38), new NodeId(0, 15623).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 15703), new NodeId(0, 39), new NodeId(0, 15872).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 15703), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode392() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 15705),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 15705), new NodeId(0, 38), new NodeId(0, 15628).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 15705), new NodeId(0, 39), new NodeId(0, 15877).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 15705), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode393() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 15706),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 15706), new NodeId(0, 38), new NodeId(0, 15629).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 15706), new NodeId(0, 39), new NodeId(0, 15880).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 15706), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode394() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 15707),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 15707), new NodeId(0, 38), new NodeId(0, 15630).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 15707), new NodeId(0, 39), new NodeId(0, 15883).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 15707), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode395() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 15712),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 15712), new NodeId(0, 38), new NodeId(0, 15631).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 15712), new NodeId(0, 39), new NodeId(0, 15886).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 15712), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode396() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 14848),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 14848), new NodeId(0, 38), new NodeId(0, 14744).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 14848), new NodeId(0, 39), new NodeId(0, 21002).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 14848), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode397() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 15713),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 15713), new NodeId(0, 38), new NodeId(0, 15635).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 15713), new NodeId(0, 39), new NodeId(0, 15889).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 15713), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode398() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 21154),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 21154), new NodeId(0, 38), new NodeId(0, 15530).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 21154), new NodeId(0, 39), new NodeId(0, 21168).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 21154), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode399() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 23851),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 23851), new NodeId(0, 38), new NodeId(0, 23599).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 23851), new NodeId(0, 39), new NodeId(0, 23870).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 23851), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode400() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 23852),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 23852), new NodeId(0, 38), new NodeId(0, 23600).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 23852), new NodeId(0, 39), new NodeId(0, 23873).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 23852), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode401() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 23853),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 23853), new NodeId(0, 38), new NodeId(0, 23601).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 23853), new NodeId(0, 39), new NodeId(0, 23876).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 23853), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode402() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 25530),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 25530), new NodeId(0, 38), new NodeId(0, 25270).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 25530), new NodeId(0, 39), new NodeId(0, 25536).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 25530), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode403() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 23854),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 23854), new NodeId(0, 38), new NodeId(0, 23602).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 23854), new NodeId(0, 39), new NodeId(0, 23879).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 23854), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode404() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 15715),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 15715), new NodeId(0, 38), new NodeId(0, 15645).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 15715), new NodeId(0, 39), new NodeId(0, 15895).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 15715), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode405() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 15717),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 15717), new NodeId(0, 38), new NodeId(0, 15652).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 15717), new NodeId(0, 39), new NodeId(0, 15898).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 15717), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode406() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 15718),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 15718), new NodeId(0, 38), new NodeId(0, 15653).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 15718), new NodeId(0, 39), new NodeId(0, 15919).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 15718), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode407() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 15719),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 15719), new NodeId(0, 38), new NodeId(0, 15657).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 15719), new NodeId(0, 39), new NodeId(0, 15922).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 15719), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode408() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 15724),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 15724), new NodeId(0, 38), new NodeId(0, 15664).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 15724), new NodeId(0, 39), new NodeId(0, 15925).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 15724), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode409() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 15725),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 15725), new NodeId(0, 38), new NodeId(0, 15665).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 15725), new NodeId(0, 39), new NodeId(0, 15931).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 15725), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode410() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 23855),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 23855), new NodeId(0, 38), new NodeId(0, 23603).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 23855), new NodeId(0, 39), new NodeId(0, 23882).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 23855), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode411() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 23856),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 23856), new NodeId(0, 38), new NodeId(0, 23604).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 23856), new NodeId(0, 39), new NodeId(0, 23885).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 23856), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode412() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 23857),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 23857), new NodeId(0, 38), new NodeId(0, 23605).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 23857), new NodeId(0, 39), new NodeId(0, 23888).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 23857), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode413() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 23860),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 23860), new NodeId(0, 38), new NodeId(0, 23608).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 23860), new NodeId(0, 39), new NodeId(0, 23897).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 23860), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode414() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 23861),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 23861), new NodeId(0, 38), new NodeId(0, 23609).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 23861), new NodeId(0, 39), new NodeId(0, 23900).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 23861), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode415() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 17468),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 17468), new NodeId(0, 38), new NodeId(0, 17467).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 17468), new NodeId(0, 39), new NodeId(0, 17469).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 17468), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode416() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 23864),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 23864), new NodeId(0, 38), new NodeId(0, 23612).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 23864), new NodeId(0, 39), new NodeId(0, 23909).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 23864), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode417() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 21155),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 21155), new NodeId(0, 38), new NodeId(0, 15532).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 21155), new NodeId(0, 39), new NodeId(0, 21171).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 21155), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode418() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 23865),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 23865), new NodeId(0, 38), new NodeId(0, 23613).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 23865), new NodeId(0, 39), new NodeId(0, 23912).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 23865), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode419() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 23866),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 23866), new NodeId(0, 38), new NodeId(0, 23614).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 23866), new NodeId(0, 39), new NodeId(0, 23915).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 23866), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode420() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 18930),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 18930), new NodeId(0, 38), new NodeId(0, 18794).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 18930), new NodeId(0, 39), new NodeId(0, 18934).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 18930), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode421() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 15479),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 15479), new NodeId(0, 38), new NodeId(0, 15007).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 15479), new NodeId(0, 39), new NodeId(0, 15524).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 15479), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode422() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 15727),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 15727), new NodeId(0, 38), new NodeId(0, 15667).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 15727), new NodeId(0, 39), new NodeId(0, 15940).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 15727), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode423() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 15729),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 15729), new NodeId(0, 38), new NodeId(0, 15669).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 15729), new NodeId(0, 39), new NodeId(0, 15943).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 15729), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode424() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 15733),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 15733), new NodeId(0, 38), new NodeId(0, 15670).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 15733), new NodeId(0, 39), new NodeId(0, 15946).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 15733), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode425() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 25531),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 25531), new NodeId(0, 38), new NodeId(0, 25519).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 25531), new NodeId(0, 39), new NodeId(0, 25539).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 25531), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode426() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 25532),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 25532), new NodeId(0, 38), new NodeId(0, 25520).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 25532), new NodeId(0, 39), new NodeId(0, 25542).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 25532), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode427() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 23499),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 23499), new NodeId(0, 38), new NodeId(0, 23468).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 23499), new NodeId(0, 39), new NodeId(0, 23502).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 23499), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode428() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 24292),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 24292), new NodeId(0, 38), new NodeId(0, 24281).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 24292), new NodeId(0, 39), new NodeId(0, 24293).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 24292), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode429() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 25239),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 25239), new NodeId(0, 38), new NodeId(0, 25220).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 25239), new NodeId(0, 39), new NodeId(0, 25240).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 25239), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode430() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 19079),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 19079), new NodeId(0, 38), new NodeId(0, 18953).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 19079), new NodeId(0, 39), new NodeId(0, 19085).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 19079), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode431() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 19080),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 19080), new NodeId(0, 38), new NodeId(0, 18954).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 19080), new NodeId(0, 39), new NodeId(0, 19088).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 19080), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode432() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 19081),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 19081), new NodeId(0, 38), new NodeId(0, 18955).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 19081), new NodeId(0, 39), new NodeId(0, 19097).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 19081), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode433() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 32661),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 32661), new NodeId(0, 38), new NodeId(0, 32659).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 32661), new NodeId(0, 39), new NodeId(0, 32663).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 32661), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode434() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 32662),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 32662), new NodeId(0, 38), new NodeId(0, 32660).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 32662), new NodeId(0, 39), new NodeId(0, 32666).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 32662), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode435() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 128),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 128), new NodeId(0, 38), new NodeId(0, 96).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 128), new NodeId(0, 39), new NodeId(0, 16131).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 128), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode436() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 121),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 121), new NodeId(0, 38), new NodeId(0, 97).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 121), new NodeId(0, 39), new NodeId(0, 18178).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 121), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode437() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 14844),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 14844), new NodeId(0, 38), new NodeId(0, 101).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 14844), new NodeId(0, 39), new NodeId(0, 18181).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 14844), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode438() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 122),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 122), new NodeId(0, 38), new NodeId(0, 99).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 122), new NodeId(0, 39), new NodeId(0, 18184).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 122), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode439() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 123),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 123), new NodeId(0, 38), new NodeId(0, 100).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 123), new NodeId(0, 39), new NodeId(0, 18187).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 123), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode440() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 298),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 298), new NodeId(0, 38), new NodeId(0, 296).expanded(), false));
    node.addReference(
        new Reference(new NodeId(0, 298), new NodeId(0, 39), new NodeId(0, 7650).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 298), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode441() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 8251),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 8251), new NodeId(0, 38), new NodeId(0, 7594).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 8251), new NodeId(0, 39), new NodeId(0, 7656).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 8251), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode442() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 14845),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 14845), new NodeId(0, 38), new NodeId(0, 102).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 14845), new NodeId(0, 39), new NodeId(0, 14870).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 14845), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode443() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 12765),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 12765), new NodeId(0, 38), new NodeId(0, 12755).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 12765), new NodeId(0, 39), new NodeId(0, 12767).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 12765), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode444() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 8917),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 8917), new NodeId(0, 38), new NodeId(0, 8912).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 8917), new NodeId(0, 39), new NodeId(0, 8914).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 8917), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode445() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 310),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 310), new NodeId(0, 38), new NodeId(0, 308).expanded(), false));
    node.addReference(
        new Reference(new NodeId(0, 310), new NodeId(0, 39), new NodeId(0, 7665).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 310), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode446() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 12207),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 12207), new NodeId(0, 38), new NodeId(0, 12189).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 12207), new NodeId(0, 39), new NodeId(0, 12213).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 12207), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode447() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 306),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 306), new NodeId(0, 38), new NodeId(0, 304).expanded(), false));
    node.addReference(
        new Reference(new NodeId(0, 306), new NodeId(0, 39), new NodeId(0, 7662).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 306), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode448() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 314),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 314), new NodeId(0, 38), new NodeId(0, 312).expanded(), false));
    node.addReference(
        new Reference(new NodeId(0, 314), new NodeId(0, 39), new NodeId(0, 7668).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 314), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode449() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 434),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 434), new NodeId(0, 38), new NodeId(0, 432).expanded(), false));
    node.addReference(
        new Reference(new NodeId(0, 434), new NodeId(0, 39), new NodeId(0, 7782).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 434), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode450() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 12900),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 12900), new NodeId(0, 38), new NodeId(0, 12890).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 12900), new NodeId(0, 39), new NodeId(0, 12902).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 12900), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode451() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 12901),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 12901), new NodeId(0, 38), new NodeId(0, 12891).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 12901), new NodeId(0, 39), new NodeId(0, 12905).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 12901), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode452() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 346),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 346), new NodeId(0, 38), new NodeId(0, 344).expanded(), false));
    node.addReference(
        new Reference(new NodeId(0, 346), new NodeId(0, 39), new NodeId(0, 7698).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 346), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode453() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 318),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 318), new NodeId(0, 38), new NodeId(0, 316).expanded(), false));
    node.addReference(
        new Reference(new NodeId(0, 318), new NodeId(0, 39), new NodeId(0, 7671).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 318), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode454() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 321),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 321), new NodeId(0, 38), new NodeId(0, 319).expanded(), false));
    node.addReference(
        new Reference(new NodeId(0, 321), new NodeId(0, 39), new NodeId(0, 7674).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 321), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode455() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 324),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 324), new NodeId(0, 38), new NodeId(0, 322).expanded(), false));
    node.addReference(
        new Reference(new NodeId(0, 324), new NodeId(0, 39), new NodeId(0, 7677).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 324), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode456() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 327),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 327), new NodeId(0, 38), new NodeId(0, 325).expanded(), false));
    node.addReference(
        new Reference(new NodeId(0, 327), new NodeId(0, 39), new NodeId(0, 7680).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 327), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode457() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 940),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 940), new NodeId(0, 38), new NodeId(0, 938).expanded(), false));
    node.addReference(
        new Reference(new NodeId(0, 940), new NodeId(0, 39), new NodeId(0, 7683).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 940), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode458() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 378),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 378), new NodeId(0, 38), new NodeId(0, 376).expanded(), false));
    node.addReference(
        new Reference(new NodeId(0, 378), new NodeId(0, 39), new NodeId(0, 7728).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 378), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode459() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 381),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 381), new NodeId(0, 38), new NodeId(0, 379).expanded(), false));
    node.addReference(
        new Reference(new NodeId(0, 381), new NodeId(0, 39), new NodeId(0, 7731).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 381), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode460() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 384),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 384), new NodeId(0, 38), new NodeId(0, 382).expanded(), false));
    node.addReference(
        new Reference(new NodeId(0, 384), new NodeId(0, 39), new NodeId(0, 7734).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 384), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode461() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 387),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 387), new NodeId(0, 38), new NodeId(0, 385).expanded(), false));
    node.addReference(
        new Reference(new NodeId(0, 387), new NodeId(0, 39), new NodeId(0, 7737).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 387), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode462() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 539),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 539), new NodeId(0, 38), new NodeId(0, 537).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 539), new NodeId(0, 39), new NodeId(0, 12718).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 539), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode463() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 542),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 542), new NodeId(0, 38), new NodeId(0, 540).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 542), new NodeId(0, 39), new NodeId(0, 12721).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 542), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode464() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 333),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 333), new NodeId(0, 38), new NodeId(0, 331).expanded(), false));
    node.addReference(
        new Reference(new NodeId(0, 333), new NodeId(0, 39), new NodeId(0, 7686).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 333), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode465() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 585),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 585), new NodeId(0, 38), new NodeId(0, 583).expanded(), false));
    node.addReference(
        new Reference(new NodeId(0, 585), new NodeId(0, 39), new NodeId(0, 7929).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 585), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode466() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 588),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 588), new NodeId(0, 38), new NodeId(0, 586).expanded(), false));
    node.addReference(
        new Reference(new NodeId(0, 588), new NodeId(0, 39), new NodeId(0, 7932).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 588), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode467() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 591),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 591), new NodeId(0, 38), new NodeId(0, 589).expanded(), false));
    node.addReference(
        new Reference(new NodeId(0, 591), new NodeId(0, 39), new NodeId(0, 7935).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 591), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode468() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 594),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 594), new NodeId(0, 38), new NodeId(0, 592).expanded(), false));
    node.addReference(
        new Reference(new NodeId(0, 594), new NodeId(0, 39), new NodeId(0, 7938).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 594), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode469() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 597),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 597), new NodeId(0, 38), new NodeId(0, 595).expanded(), false));
    node.addReference(
        new Reference(new NodeId(0, 597), new NodeId(0, 39), new NodeId(0, 7941).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 597), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode470() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 600),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 600), new NodeId(0, 38), new NodeId(0, 598).expanded(), false));
    node.addReference(
        new Reference(new NodeId(0, 600), new NodeId(0, 39), new NodeId(0, 7944).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 600), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode471() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 603),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 603), new NodeId(0, 38), new NodeId(0, 601).expanded(), false));
    node.addReference(
        new Reference(new NodeId(0, 603), new NodeId(0, 39), new NodeId(0, 7947).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 603), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode472() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 11226),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 11226), new NodeId(0, 38), new NodeId(0, 11216).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 11226), new NodeId(0, 39), new NodeId(0, 15018).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 11226), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode473() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 661),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 661), new NodeId(0, 38), new NodeId(0, 659).expanded(), false));
    node.addReference(
        new Reference(new NodeId(0, 661), new NodeId(0, 39), new NodeId(0, 8004).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 661), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode474() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 32825),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 32825), new NodeId(0, 38), new NodeId(0, 32824).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 32825), new NodeId(0, 39), new NodeId(0, 32826).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 32825), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode475() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 721),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 721), new NodeId(0, 38), new NodeId(0, 719).expanded(), false));
    node.addReference(
        new Reference(new NodeId(0, 721), new NodeId(0, 39), new NodeId(0, 8067).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 721), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode476() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 727),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 727), new NodeId(0, 38), new NodeId(0, 725).expanded(), false));
    node.addReference(
        new Reference(new NodeId(0, 727), new NodeId(0, 39), new NodeId(0, 8073).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 727), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode477() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 950),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 950), new NodeId(0, 38), new NodeId(0, 948).expanded(), false));
    node.addReference(
        new Reference(new NodeId(0, 950), new NodeId(0, 39), new NodeId(0, 8076).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 950), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode478() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 922),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 922), new NodeId(0, 38), new NodeId(0, 920).expanded(), false));
    node.addReference(
        new Reference(new NodeId(0, 922), new NodeId(0, 39), new NodeId(0, 8172).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 922), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode479() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 340),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 340), new NodeId(0, 38), new NodeId(0, 338).expanded(), false));
    node.addReference(
        new Reference(new NodeId(0, 340), new NodeId(0, 39), new NodeId(0, 7692).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 340), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode480() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 855),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 855), new NodeId(0, 38), new NodeId(0, 853).expanded(), false));
    node.addReference(
        new Reference(new NodeId(0, 855), new NodeId(0, 39), new NodeId(0, 8208).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 855), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode481() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 11957),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 11957), new NodeId(0, 38), new NodeId(0, 11943).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 11957), new NodeId(0, 39), new NodeId(0, 11959).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 11957), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode482() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 11958),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 11958), new NodeId(0, 38), new NodeId(0, 11944).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 11958), new NodeId(0, 39), new NodeId(0, 11962).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 11958), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode483() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 858),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 858), new NodeId(0, 38), new NodeId(0, 856).expanded(), false));
    node.addReference(
        new Reference(new NodeId(0, 858), new NodeId(0, 39), new NodeId(0, 8211).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 858), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode484() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 861),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 861), new NodeId(0, 38), new NodeId(0, 859).expanded(), false));
    node.addReference(
        new Reference(new NodeId(0, 861), new NodeId(0, 39), new NodeId(0, 8214).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 861), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode485() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 864),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 864), new NodeId(0, 38), new NodeId(0, 862).expanded(), false));
    node.addReference(
        new Reference(new NodeId(0, 864), new NodeId(0, 39), new NodeId(0, 8217).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 864), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode486() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 867),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 867), new NodeId(0, 38), new NodeId(0, 865).expanded(), false));
    node.addReference(
        new Reference(new NodeId(0, 867), new NodeId(0, 39), new NodeId(0, 8220).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 867), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode487() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 870),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 870), new NodeId(0, 38), new NodeId(0, 868).expanded(), false));
    node.addReference(
        new Reference(new NodeId(0, 870), new NodeId(0, 39), new NodeId(0, 8223).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 870), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode488() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 873),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 873), new NodeId(0, 38), new NodeId(0, 871).expanded(), false));
    node.addReference(
        new Reference(new NodeId(0, 873), new NodeId(0, 39), new NodeId(0, 8226).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 873), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode489() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 301),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 301), new NodeId(0, 38), new NodeId(0, 299).expanded(), false));
    node.addReference(
        new Reference(new NodeId(0, 301), new NodeId(0, 39), new NodeId(0, 7659).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 301), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode490() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 876),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 876), new NodeId(0, 38), new NodeId(0, 874).expanded(), false));
    node.addReference(
        new Reference(new NodeId(0, 876), new NodeId(0, 39), new NodeId(0, 8229).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 876), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode491() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 879),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 879), new NodeId(0, 38), new NodeId(0, 877).expanded(), false));
    node.addReference(
        new Reference(new NodeId(0, 879), new NodeId(0, 39), new NodeId(0, 8232).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 879), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode492() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 899),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 899), new NodeId(0, 38), new NodeId(0, 897).expanded(), false));
    node.addReference(
        new Reference(new NodeId(0, 899), new NodeId(0, 39), new NodeId(0, 8235).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 899), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode493() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 886),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 886), new NodeId(0, 38), new NodeId(0, 884).expanded(), false));
    node.addReference(
        new Reference(new NodeId(0, 886), new NodeId(0, 39), new NodeId(0, 8238).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 886), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode494() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 889),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 889), new NodeId(0, 38), new NodeId(0, 887).expanded(), false));
    node.addReference(
        new Reference(new NodeId(0, 889), new NodeId(0, 39), new NodeId(0, 8241).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 889), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode495() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 12181),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 12181), new NodeId(0, 38), new NodeId(0, 12171).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 12181), new NodeId(0, 39), new NodeId(0, 12183).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 12181), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode496() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 12182),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 12182), new NodeId(0, 38), new NodeId(0, 12172).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 12182), new NodeId(0, 39), new NodeId(0, 12186).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 12182), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode497() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 12089),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 12089), new NodeId(0, 38), new NodeId(0, 12079).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 12089), new NodeId(0, 39), new NodeId(0, 12091).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 12089), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode498() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 12090),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 12090), new NodeId(0, 38), new NodeId(0, 12080).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 12090), new NodeId(0, 39), new NodeId(0, 12094).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 12090), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode499() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 896),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 896), new NodeId(0, 38), new NodeId(0, 894).expanded(), false));
    node.addReference(
        new Reference(new NodeId(0, 896), new NodeId(0, 39), new NodeId(0, 8247).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 896), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode500() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 24034),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(
            new NodeId(0, 24034), new NodeId(0, 38), new NodeId(0, 24033).expanded(), false));
    node.addReference(
        new Reference(
            new NodeId(0, 24034), new NodeId(0, 39), new NodeId(0, 24035).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 24034), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode501() {
    var node =
        new DataTypeEncodingTypeNode(
            this.context,
            new NodeId(0, 893),
            new QualifiedName(0, "Default Binary"),
            new LocalizedText("", "Default Binary"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            UByte.valueOf(0));
    node.addReference(
        new Reference(new NodeId(0, 893), new NodeId(0, 38), new NodeId(0, 891).expanded(), false));
    node.addReference(
        new Reference(new NodeId(0, 893), new NodeId(0, 39), new NodeId(0, 8244).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 893), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
    this.nodeManager.addNode(node);
  }

  void loadNode502() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 12758),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 12758), new NodeId(0, 38), new NodeId(0, 12756).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 12758), new NodeId(0, 39), new NodeId(0, 12762).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 12758), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode503() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 14802),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 14802), new NodeId(0, 38), new NodeId(0, 14533).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 14802), new NodeId(0, 39), new NodeId(0, 14829).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 14802), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode504() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 17541),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 17541), new NodeId(0, 38), new NodeId(0, 16313).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 17541), new NodeId(0, 39), new NodeId(0, 17542).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 17541), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode505() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 17553),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 17553), new NodeId(0, 38), new NodeId(0, 17548).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 17553), new NodeId(0, 39), new NodeId(0, 17554).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 17553), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode506() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15949),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15949), new NodeId(0, 38), new NodeId(0, 15528).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15949), new NodeId(0, 39), new NodeId(0, 16024).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 15949), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode507() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 32426),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 32426), new NodeId(0, 38), new NodeId(0, 32421).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 32426), new NodeId(0, 39), new NodeId(0, 32427).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 32426), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode508() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 18851),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 18851), new NodeId(0, 38), new NodeId(0, 18806).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 18851), new NodeId(0, 39), new NodeId(0, 18860).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 18851), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode509() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 18852),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 18852), new NodeId(0, 38), new NodeId(0, 18807).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 18852), new NodeId(0, 39), new NodeId(0, 18863).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 18852), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode510() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 18853),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 18853), new NodeId(0, 38), new NodeId(0, 18808).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 18853), new NodeId(0, 39), new NodeId(0, 18866).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 18853), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode511() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 18854),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 18854), new NodeId(0, 38), new NodeId(0, 18809).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 18854), new NodeId(0, 39), new NodeId(0, 18869).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 18854), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode512() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 18855),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 18855), new NodeId(0, 38), new NodeId(0, 18810).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 18855), new NodeId(0, 39), new NodeId(0, 19049).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 18855), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode513() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 18856),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 18856), new NodeId(0, 38), new NodeId(0, 18811).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 18856), new NodeId(0, 39), new NodeId(0, 19052).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 18856), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode514() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 18857),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 18857), new NodeId(0, 38), new NodeId(0, 18812).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 18857), new NodeId(0, 39), new NodeId(0, 19055).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 18857), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode515() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 18858),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 18858), new NodeId(0, 38), new NodeId(0, 18813).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 18858), new NodeId(0, 39), new NodeId(0, 19058).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 18858), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode516() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 18859),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 18859), new NodeId(0, 38), new NodeId(0, 18814).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 18859), new NodeId(0, 39), new NodeId(0, 19061).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 18859), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode517() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15728),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15728), new NodeId(0, 38), new NodeId(0, 15634).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15728), new NodeId(0, 39), new NodeId(0, 15730).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 15728), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode518() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 23520),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 23520), new NodeId(0, 38), new NodeId(0, 23498).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 23520), new NodeId(0, 39), new NodeId(0, 23522).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 23520), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode519() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 32572),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 32572), new NodeId(0, 38), new NodeId(0, 32434).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 32572), new NodeId(0, 39), new NodeId(0, 32575).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 32572), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode520() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 32573),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 32573), new NodeId(0, 38), new NodeId(0, 32435).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 32573), new NodeId(0, 39), new NodeId(0, 32578).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 32573), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode521() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 32574),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 32574), new NodeId(0, 38), new NodeId(0, 32438).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 32574), new NodeId(0, 39), new NodeId(0, 32581).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 32574), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode522() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 12676),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 12676), new NodeId(0, 38), new NodeId(0, 12554).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 12676), new NodeId(0, 39), new NodeId(0, 12677).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 12676), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode523() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 32386),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 32386), new NodeId(0, 38), new NodeId(0, 32285).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 32386), new NodeId(0, 39), new NodeId(0, 32387).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 32386), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode524() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15950),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15950), new NodeId(0, 38), new NodeId(0, 15534).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15950), new NodeId(0, 39), new NodeId(0, 16027).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 15950), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode525() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 14796),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 14796), new NodeId(0, 38), new NodeId(0, 14525).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 14796), new NodeId(0, 39), new NodeId(0, 14811).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 14796), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode526() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15589),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15589), new NodeId(0, 38), new NodeId(0, 15487).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15589), new NodeId(0, 39), new NodeId(0, 15591).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 15589), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode527() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15590),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15590), new NodeId(0, 38), new NodeId(0, 15488).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15590), new NodeId(0, 39), new NodeId(0, 15594).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 15590), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode528() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15529),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15529), new NodeId(0, 38), new NodeId(0, 15005).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15529), new NodeId(0, 39), new NodeId(0, 15585).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 15529), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode529() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15531),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15531), new NodeId(0, 38), new NodeId(0, 15006).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15531), new NodeId(0, 39), new NodeId(0, 15588).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 15531), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode530() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 24120),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 24120), new NodeId(0, 38), new NodeId(0, 24105).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 24120), new NodeId(0, 39), new NodeId(0, 24123).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 24120), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode531() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 24121),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 24121), new NodeId(0, 38), new NodeId(0, 24106).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 24121), new NodeId(0, 39), new NodeId(0, 24126).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 24121), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode532() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 24122),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 24122), new NodeId(0, 38), new NodeId(0, 24107).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 24122), new NodeId(0, 39), new NodeId(0, 24129).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 24122), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode533() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 14794),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 14794), new NodeId(0, 38), new NodeId(0, 14523).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 14794), new NodeId(0, 39), new NodeId(0, 14805).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 14794), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode534() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 14795),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 14795), new NodeId(0, 38), new NodeId(0, 14524).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 14795), new NodeId(0, 39), new NodeId(0, 14808).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 14795), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode535() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 14803),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 14803), new NodeId(0, 38), new NodeId(0, 14593).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 14803), new NodeId(0, 39), new NodeId(0, 14832).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 14803), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode536() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15951),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15951), new NodeId(0, 38), new NodeId(0, 15578).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15951), new NodeId(0, 39), new NodeId(0, 16030).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 15951), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode537() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15952),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15952), new NodeId(0, 38), new NodeId(0, 15580).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15952), new NodeId(0, 39), new NodeId(0, 16033).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 15952), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode538() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 14319),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 14319), new NodeId(0, 38), new NodeId(0, 14273).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 14319), new NodeId(0, 39), new NodeId(0, 14320).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 14319), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode539() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15953),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15953), new NodeId(0, 38), new NodeId(0, 15581).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15953), new NodeId(0, 39), new NodeId(0, 16037).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 15953), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode540() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15954),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15954), new NodeId(0, 38), new NodeId(0, 15582).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15954), new NodeId(0, 39), new NodeId(0, 16040).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 15954), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode541() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 25545),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 25545), new NodeId(0, 38), new NodeId(0, 25269).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 25545), new NodeId(0, 39), new NodeId(0, 25549).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 25545), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode542() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 18610),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 18610), new NodeId(0, 38), new NodeId(0, 18593).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 18610), new NodeId(0, 39), new NodeId(0, 18613).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 18610), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode543() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 18611),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 18611), new NodeId(0, 38), new NodeId(0, 18594).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 18611), new NodeId(0, 39), new NodeId(0, 18616).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 18611), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode544() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 18612),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 18612), new NodeId(0, 38), new NodeId(0, 18597).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 18612), new NodeId(0, 39), new NodeId(0, 18619).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 18612), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode545() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 18937),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 18937), new NodeId(0, 38), new NodeId(0, 18793).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 18937), new NodeId(0, 39), new NodeId(0, 18939).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 18937), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode546() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15955),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15955), new NodeId(0, 38), new NodeId(0, 15597).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15955), new NodeId(0, 39), new NodeId(0, 16047).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 15955), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode547() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15956),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15956), new NodeId(0, 38), new NodeId(0, 15598).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15956), new NodeId(0, 39), new NodeId(0, 16050).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 15956), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode548() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15987),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15987), new NodeId(0, 38), new NodeId(0, 15605).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15987), new NodeId(0, 39), new NodeId(0, 16053).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 15987), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode549() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15988),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15988), new NodeId(0, 38), new NodeId(0, 15609).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15988), new NodeId(0, 39), new NodeId(0, 16056).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 15988), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode550() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 21174),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 21174), new NodeId(0, 38), new NodeId(0, 15480).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 21174), new NodeId(0, 39), new NodeId(0, 21180).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 21174), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode551() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15990),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15990), new NodeId(0, 38), new NodeId(0, 15611).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15990), new NodeId(0, 39), new NodeId(0, 16062).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 15990), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode552() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15991),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15991), new NodeId(0, 38), new NodeId(0, 15616).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15991), new NodeId(0, 39), new NodeId(0, 16065).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 15991), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode553() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15992),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15992), new NodeId(0, 38), new NodeId(0, 15617).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15992), new NodeId(0, 39), new NodeId(0, 16068).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 15992), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode554() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15993),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15993), new NodeId(0, 38), new NodeId(0, 15618).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15993), new NodeId(0, 39), new NodeId(0, 16071).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 15993), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode555() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 21175),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 21175), new NodeId(0, 38), new NodeId(0, 15502).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 21175), new NodeId(0, 39), new NodeId(0, 21183).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 21175), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode556() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 21176),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 21176), new NodeId(0, 38), new NodeId(0, 15510).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 21176), new NodeId(0, 39), new NodeId(0, 21186).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 21176), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode557() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 21177),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 21177), new NodeId(0, 38), new NodeId(0, 15520).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 21177), new NodeId(0, 39), new NodeId(0, 21189).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 21177), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode558() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15995),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15995), new NodeId(0, 38), new NodeId(0, 15621).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15995), new NodeId(0, 39), new NodeId(0, 16077).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 15995), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode559() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15996),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15996), new NodeId(0, 38), new NodeId(0, 15622).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15996), new NodeId(0, 39), new NodeId(0, 16080).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 15996), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode560() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 16007),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 16007), new NodeId(0, 38), new NodeId(0, 15623).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 16007), new NodeId(0, 39), new NodeId(0, 16083).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 16007), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode561() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 16008),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 16008), new NodeId(0, 38), new NodeId(0, 15628).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 16008), new NodeId(0, 39), new NodeId(0, 16086).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 16008), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode562() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 16009),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 16009), new NodeId(0, 38), new NodeId(0, 15629).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 16009), new NodeId(0, 39), new NodeId(0, 16089).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 16009), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode563() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 16010),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 16010), new NodeId(0, 38), new NodeId(0, 15630).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 16010), new NodeId(0, 39), new NodeId(0, 16092).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 16010), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode564() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 16011),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 16011), new NodeId(0, 38), new NodeId(0, 15631).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 16011), new NodeId(0, 39), new NodeId(0, 16095).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 16011), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode565() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 14804),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 14804), new NodeId(0, 38), new NodeId(0, 14744).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 14804), new NodeId(0, 39), new NodeId(0, 14835).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 14804), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode566() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 16012),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 16012), new NodeId(0, 38), new NodeId(0, 15635).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 16012), new NodeId(0, 39), new NodeId(0, 16098).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 16012), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode567() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 21178),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 21178), new NodeId(0, 38), new NodeId(0, 15530).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 21178), new NodeId(0, 39), new NodeId(0, 21192).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 21178), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode568() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 23919),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 23919), new NodeId(0, 38), new NodeId(0, 23599).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 23919), new NodeId(0, 39), new NodeId(0, 23938).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 23919), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode569() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 23920),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 23920), new NodeId(0, 38), new NodeId(0, 23600).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 23920), new NodeId(0, 39), new NodeId(0, 23941).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 23920), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode570() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 23921),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 23921), new NodeId(0, 38), new NodeId(0, 23601).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 23921), new NodeId(0, 39), new NodeId(0, 23944).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 23921), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode571() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 25546),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 25546), new NodeId(0, 38), new NodeId(0, 25270).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 25546), new NodeId(0, 39), new NodeId(0, 25552).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 25546), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode572() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 23922),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 23922), new NodeId(0, 38), new NodeId(0, 23602).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 23922), new NodeId(0, 39), new NodeId(0, 23947).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 23922), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode573() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 16014),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 16014), new NodeId(0, 38), new NodeId(0, 15645).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 16014), new NodeId(0, 39), new NodeId(0, 16104).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 16014), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode574() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 16015),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 16015), new NodeId(0, 38), new NodeId(0, 15652).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 16015), new NodeId(0, 39), new NodeId(0, 16107).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 16015), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode575() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 16016),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 16016), new NodeId(0, 38), new NodeId(0, 15653).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 16016), new NodeId(0, 39), new NodeId(0, 16110).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 16016), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode576() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 16017),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 16017), new NodeId(0, 38), new NodeId(0, 15657).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 16017), new NodeId(0, 39), new NodeId(0, 16113).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 16017), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode577() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 16018),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 16018), new NodeId(0, 38), new NodeId(0, 15664).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 16018), new NodeId(0, 39), new NodeId(0, 16116).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 16018), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode578() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 16019),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 16019), new NodeId(0, 38), new NodeId(0, 15665).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 16019), new NodeId(0, 39), new NodeId(0, 16119).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 16019), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode579() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 23923),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 23923), new NodeId(0, 38), new NodeId(0, 23603).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 23923), new NodeId(0, 39), new NodeId(0, 23950).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 23923), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode580() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 23924),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 23924), new NodeId(0, 38), new NodeId(0, 23604).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 23924), new NodeId(0, 39), new NodeId(0, 23953).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 23924), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode581() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 23925),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 23925), new NodeId(0, 38), new NodeId(0, 23605).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 23925), new NodeId(0, 39), new NodeId(0, 23956).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 23925), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode582() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 23928),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 23928), new NodeId(0, 38), new NodeId(0, 23608).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 23928), new NodeId(0, 39), new NodeId(0, 23965).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 23928), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode583() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 23929),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 23929), new NodeId(0, 38), new NodeId(0, 23609).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 23929), new NodeId(0, 39), new NodeId(0, 23968).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 23929), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode584() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 17472),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 17472), new NodeId(0, 38), new NodeId(0, 17467).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 17472), new NodeId(0, 39), new NodeId(0, 17473).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 17472), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode585() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 23932),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 23932), new NodeId(0, 38), new NodeId(0, 23612).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 23932), new NodeId(0, 39), new NodeId(0, 23977).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 23932), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode586() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 21179),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 21179), new NodeId(0, 38), new NodeId(0, 15532).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 21179), new NodeId(0, 39), new NodeId(0, 21195).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 21179), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode587() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 23933),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 23933), new NodeId(0, 38), new NodeId(0, 23613).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 23933), new NodeId(0, 39), new NodeId(0, 23980).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 23933), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode588() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 23934),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 23934), new NodeId(0, 38), new NodeId(0, 23614).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 23934), new NodeId(0, 39), new NodeId(0, 23983).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 23934), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode589() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 18938),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 18938), new NodeId(0, 38), new NodeId(0, 18794).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 18938), new NodeId(0, 39), new NodeId(0, 18942).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 18938), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode590() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15579),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15579), new NodeId(0, 38), new NodeId(0, 15007).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15579), new NodeId(0, 39), new NodeId(0, 15640).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 15579), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode591() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 16021),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 16021), new NodeId(0, 38), new NodeId(0, 15667).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 16021), new NodeId(0, 39), new NodeId(0, 16125).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 16021), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode592() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 16022),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 16022), new NodeId(0, 38), new NodeId(0, 15669).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 16022), new NodeId(0, 39), new NodeId(0, 16144).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 16022), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode593() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 16023),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 16023), new NodeId(0, 38), new NodeId(0, 15670).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 16023), new NodeId(0, 39), new NodeId(0, 16147).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 16023), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode594() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 25547),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 25547), new NodeId(0, 38), new NodeId(0, 25519).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 25547), new NodeId(0, 39), new NodeId(0, 25555).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 25547), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode595() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 25548),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 25548), new NodeId(0, 38), new NodeId(0, 25520).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 25548), new NodeId(0, 39), new NodeId(0, 25558).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 25548), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode596() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 23505),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 23505), new NodeId(0, 38), new NodeId(0, 23468).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 23505), new NodeId(0, 39), new NodeId(0, 23508).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 23505), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode597() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 24296),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 24296), new NodeId(0, 38), new NodeId(0, 24281).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 24296), new NodeId(0, 39), new NodeId(0, 24297).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 24296), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode598() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 25243),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 25243), new NodeId(0, 38), new NodeId(0, 25220).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 25243), new NodeId(0, 39), new NodeId(0, 25244).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 25243), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode599() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 19100),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 19100), new NodeId(0, 38), new NodeId(0, 18953).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 19100), new NodeId(0, 39), new NodeId(0, 19103).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 19100), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode600() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 19101),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 19101), new NodeId(0, 38), new NodeId(0, 18954).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 19101), new NodeId(0, 39), new NodeId(0, 19106).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 19101), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode601() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 19102),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 19102), new NodeId(0, 38), new NodeId(0, 18955).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 19102), new NodeId(0, 39), new NodeId(0, 19291).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 19102), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode602() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 32669),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 32669), new NodeId(0, 38), new NodeId(0, 32659).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 32669), new NodeId(0, 39), new NodeId(0, 32671).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 32669), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode603() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 32670),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 32670), new NodeId(0, 38), new NodeId(0, 32660).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 32670), new NodeId(0, 39), new NodeId(0, 32674).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 32670), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode604() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 16126),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 16126), new NodeId(0, 38), new NodeId(0, 96).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 16126), new NodeId(0, 39), new NodeId(0, 16127).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 16126), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode605() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 14797),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 14797), new NodeId(0, 38), new NodeId(0, 97).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 14797), new NodeId(0, 39), new NodeId(0, 18166).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 14797), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode606() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 14800),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 14800), new NodeId(0, 38), new NodeId(0, 101).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 14800), new NodeId(0, 39), new NodeId(0, 18169).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 14800), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode607() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 14798),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 14798), new NodeId(0, 38), new NodeId(0, 99).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 14798), new NodeId(0, 39), new NodeId(0, 18172).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 14798), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode608() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 14799),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 14799), new NodeId(0, 38), new NodeId(0, 100).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 14799), new NodeId(0, 39), new NodeId(0, 18175).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 14799), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode609() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 297),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 297), new NodeId(0, 38), new NodeId(0, 296).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 297), new NodeId(0, 39), new NodeId(0, 8285).expanded(), true));
      node.addReference(
          new Reference(new NodeId(0, 297), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode610() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 7616),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 7616), new NodeId(0, 38), new NodeId(0, 7594).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 7616), new NodeId(0, 39), new NodeId(0, 8291).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 7616), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode611() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 14801),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 14801), new NodeId(0, 38), new NodeId(0, 102).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 14801), new NodeId(0, 39), new NodeId(0, 14826).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 14801), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode612() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 12757),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 12757), new NodeId(0, 38), new NodeId(0, 12755).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 12757), new NodeId(0, 39), new NodeId(0, 12759).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 12757), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode613() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 8913),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 8913), new NodeId(0, 38), new NodeId(0, 8912).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 8913), new NodeId(0, 39), new NodeId(0, 8918).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 8913), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode614() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 309),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 309), new NodeId(0, 38), new NodeId(0, 308).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 309), new NodeId(0, 39), new NodeId(0, 8300).expanded(), true));
      node.addReference(
          new Reference(new NodeId(0, 309), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode615() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 12195),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 12195), new NodeId(0, 38), new NodeId(0, 12189).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 12195), new NodeId(0, 39), new NodeId(0, 12201).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 12195), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode616() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 305),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 305), new NodeId(0, 38), new NodeId(0, 304).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 305), new NodeId(0, 39), new NodeId(0, 8297).expanded(), true));
      node.addReference(
          new Reference(new NodeId(0, 305), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode617() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 313),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 313), new NodeId(0, 38), new NodeId(0, 312).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 313), new NodeId(0, 39), new NodeId(0, 8303).expanded(), true));
      node.addReference(
          new Reference(new NodeId(0, 313), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode618() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 433),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 433), new NodeId(0, 38), new NodeId(0, 432).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 433), new NodeId(0, 39), new NodeId(0, 8417).expanded(), true));
      node.addReference(
          new Reference(new NodeId(0, 433), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode619() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 12892),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 12892), new NodeId(0, 38), new NodeId(0, 12890).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 12892), new NodeId(0, 39), new NodeId(0, 12894).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 12892), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode620() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 12893),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 12893), new NodeId(0, 38), new NodeId(0, 12891).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 12893), new NodeId(0, 39), new NodeId(0, 12897).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 12893), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode621() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 345),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 345), new NodeId(0, 38), new NodeId(0, 344).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 345), new NodeId(0, 39), new NodeId(0, 8333).expanded(), true));
      node.addReference(
          new Reference(new NodeId(0, 345), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode622() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 317),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 317), new NodeId(0, 38), new NodeId(0, 316).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 317), new NodeId(0, 39), new NodeId(0, 8306).expanded(), true));
      node.addReference(
          new Reference(new NodeId(0, 317), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode623() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 320),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 320), new NodeId(0, 38), new NodeId(0, 319).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 320), new NodeId(0, 39), new NodeId(0, 8309).expanded(), true));
      node.addReference(
          new Reference(new NodeId(0, 320), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode624() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 323),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 323), new NodeId(0, 38), new NodeId(0, 322).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 323), new NodeId(0, 39), new NodeId(0, 8312).expanded(), true));
      node.addReference(
          new Reference(new NodeId(0, 323), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode625() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 326),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 326), new NodeId(0, 38), new NodeId(0, 325).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 326), new NodeId(0, 39), new NodeId(0, 8315).expanded(), true));
      node.addReference(
          new Reference(new NodeId(0, 326), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode626() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 939),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 939), new NodeId(0, 38), new NodeId(0, 938).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 939), new NodeId(0, 39), new NodeId(0, 8318).expanded(), true));
      node.addReference(
          new Reference(new NodeId(0, 939), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode627() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 377),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 377), new NodeId(0, 38), new NodeId(0, 376).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 377), new NodeId(0, 39), new NodeId(0, 8363).expanded(), true));
      node.addReference(
          new Reference(new NodeId(0, 377), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode628() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 380),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 380), new NodeId(0, 38), new NodeId(0, 379).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 380), new NodeId(0, 39), new NodeId(0, 8366).expanded(), true));
      node.addReference(
          new Reference(new NodeId(0, 380), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode629() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 383),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 383), new NodeId(0, 38), new NodeId(0, 382).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 383), new NodeId(0, 39), new NodeId(0, 8369).expanded(), true));
      node.addReference(
          new Reference(new NodeId(0, 383), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode630() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 386),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 386), new NodeId(0, 38), new NodeId(0, 385).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 386), new NodeId(0, 39), new NodeId(0, 8372).expanded(), true));
      node.addReference(
          new Reference(new NodeId(0, 386), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode631() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 538),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 538), new NodeId(0, 38), new NodeId(0, 537).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 538), new NodeId(0, 39), new NodeId(0, 12712).expanded(), true));
      node.addReference(
          new Reference(new NodeId(0, 538), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode632() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 541),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 541), new NodeId(0, 38), new NodeId(0, 540).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 541), new NodeId(0, 39), new NodeId(0, 12715).expanded(), true));
      node.addReference(
          new Reference(new NodeId(0, 541), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode633() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 332),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 332), new NodeId(0, 38), new NodeId(0, 331).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 332), new NodeId(0, 39), new NodeId(0, 8321).expanded(), true));
      node.addReference(
          new Reference(new NodeId(0, 332), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode634() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 584),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 584), new NodeId(0, 38), new NodeId(0, 583).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 584), new NodeId(0, 39), new NodeId(0, 8564).expanded(), true));
      node.addReference(
          new Reference(new NodeId(0, 584), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode635() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 587),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 587), new NodeId(0, 38), new NodeId(0, 586).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 587), new NodeId(0, 39), new NodeId(0, 8567).expanded(), true));
      node.addReference(
          new Reference(new NodeId(0, 587), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode636() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 590),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 590), new NodeId(0, 38), new NodeId(0, 589).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 590), new NodeId(0, 39), new NodeId(0, 8570).expanded(), true));
      node.addReference(
          new Reference(new NodeId(0, 590), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode637() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 593),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 593), new NodeId(0, 38), new NodeId(0, 592).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 593), new NodeId(0, 39), new NodeId(0, 8573).expanded(), true));
      node.addReference(
          new Reference(new NodeId(0, 593), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode638() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 596),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 596), new NodeId(0, 38), new NodeId(0, 595).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 596), new NodeId(0, 39), new NodeId(0, 8576).expanded(), true));
      node.addReference(
          new Reference(new NodeId(0, 596), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode639() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 599),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 599), new NodeId(0, 38), new NodeId(0, 598).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 599), new NodeId(0, 39), new NodeId(0, 8579).expanded(), true));
      node.addReference(
          new Reference(new NodeId(0, 599), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode640() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 602),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 602), new NodeId(0, 38), new NodeId(0, 601).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 602), new NodeId(0, 39), new NodeId(0, 8582).expanded(), true));
      node.addReference(
          new Reference(new NodeId(0, 602), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode641() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 11218),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 11218), new NodeId(0, 38), new NodeId(0, 11216).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 11218), new NodeId(0, 39), new NodeId(0, 15021).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 11218), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode642() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 660),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 660), new NodeId(0, 38), new NodeId(0, 659).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 660), new NodeId(0, 39), new NodeId(0, 8639).expanded(), true));
      node.addReference(
          new Reference(new NodeId(0, 660), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode643() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 32829),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 32829), new NodeId(0, 38), new NodeId(0, 32824).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 32829), new NodeId(0, 39), new NodeId(0, 32830).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 32829), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode644() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 720),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 720), new NodeId(0, 38), new NodeId(0, 719).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 720), new NodeId(0, 39), new NodeId(0, 8702).expanded(), true));
      node.addReference(
          new Reference(new NodeId(0, 720), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode645() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 726),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 726), new NodeId(0, 38), new NodeId(0, 725).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 726), new NodeId(0, 39), new NodeId(0, 8708).expanded(), true));
      node.addReference(
          new Reference(new NodeId(0, 726), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode646() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 949),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 949), new NodeId(0, 38), new NodeId(0, 948).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 949), new NodeId(0, 39), new NodeId(0, 8711).expanded(), true));
      node.addReference(
          new Reference(new NodeId(0, 949), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode647() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 921),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 921), new NodeId(0, 38), new NodeId(0, 920).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 921), new NodeId(0, 39), new NodeId(0, 8807).expanded(), true));
      node.addReference(
          new Reference(new NodeId(0, 921), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode648() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 339),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 339), new NodeId(0, 38), new NodeId(0, 338).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 339), new NodeId(0, 39), new NodeId(0, 8327).expanded(), true));
      node.addReference(
          new Reference(new NodeId(0, 339), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode649() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 854),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 854), new NodeId(0, 38), new NodeId(0, 853).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 854), new NodeId(0, 39), new NodeId(0, 8843).expanded(), true));
      node.addReference(
          new Reference(new NodeId(0, 854), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode650() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 11949),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 11949), new NodeId(0, 38), new NodeId(0, 11943).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 11949), new NodeId(0, 39), new NodeId(0, 11951).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 11949), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode651() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 11950),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 11950), new NodeId(0, 38), new NodeId(0, 11944).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 11950), new NodeId(0, 39), new NodeId(0, 11954).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 11950), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode652() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 857),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 857), new NodeId(0, 38), new NodeId(0, 856).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 857), new NodeId(0, 39), new NodeId(0, 8846).expanded(), true));
      node.addReference(
          new Reference(new NodeId(0, 857), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode653() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 860),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 860), new NodeId(0, 38), new NodeId(0, 859).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 860), new NodeId(0, 39), new NodeId(0, 8849).expanded(), true));
      node.addReference(
          new Reference(new NodeId(0, 860), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode654() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 863),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 863), new NodeId(0, 38), new NodeId(0, 862).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 863), new NodeId(0, 39), new NodeId(0, 8852).expanded(), true));
      node.addReference(
          new Reference(new NodeId(0, 863), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode655() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 866),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 866), new NodeId(0, 38), new NodeId(0, 865).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 866), new NodeId(0, 39), new NodeId(0, 8855).expanded(), true));
      node.addReference(
          new Reference(new NodeId(0, 866), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode656() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 869),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 869), new NodeId(0, 38), new NodeId(0, 868).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 869), new NodeId(0, 39), new NodeId(0, 8858).expanded(), true));
      node.addReference(
          new Reference(new NodeId(0, 869), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode657() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 872),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 872), new NodeId(0, 38), new NodeId(0, 871).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 872), new NodeId(0, 39), new NodeId(0, 8861).expanded(), true));
      node.addReference(
          new Reference(new NodeId(0, 872), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode658() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 300),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 300), new NodeId(0, 38), new NodeId(0, 299).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 300), new NodeId(0, 39), new NodeId(0, 8294).expanded(), true));
      node.addReference(
          new Reference(new NodeId(0, 300), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode659() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 875),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 875), new NodeId(0, 38), new NodeId(0, 874).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 875), new NodeId(0, 39), new NodeId(0, 8864).expanded(), true));
      node.addReference(
          new Reference(new NodeId(0, 875), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode660() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 878),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 878), new NodeId(0, 38), new NodeId(0, 877).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 878), new NodeId(0, 39), new NodeId(0, 8867).expanded(), true));
      node.addReference(
          new Reference(new NodeId(0, 878), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode661() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 898),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 898), new NodeId(0, 38), new NodeId(0, 897).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 898), new NodeId(0, 39), new NodeId(0, 8870).expanded(), true));
      node.addReference(
          new Reference(new NodeId(0, 898), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode662() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 885),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 885), new NodeId(0, 38), new NodeId(0, 884).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 885), new NodeId(0, 39), new NodeId(0, 8873).expanded(), true));
      node.addReference(
          new Reference(new NodeId(0, 885), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode663() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 888),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 888), new NodeId(0, 38), new NodeId(0, 887).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 888), new NodeId(0, 39), new NodeId(0, 8876).expanded(), true));
      node.addReference(
          new Reference(new NodeId(0, 888), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode664() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 12173),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 12173), new NodeId(0, 38), new NodeId(0, 12171).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 12173), new NodeId(0, 39), new NodeId(0, 12175).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 12173), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode665() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 12174),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 12174), new NodeId(0, 38), new NodeId(0, 12172).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 12174), new NodeId(0, 39), new NodeId(0, 12178).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 12174), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode666() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 12081),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 12081), new NodeId(0, 38), new NodeId(0, 12079).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 12081), new NodeId(0, 39), new NodeId(0, 12083).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 12081), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode667() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 12082),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 12082), new NodeId(0, 38), new NodeId(0, 12080).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 12082), new NodeId(0, 39), new NodeId(0, 12086).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 12082), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode668() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 895),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 895), new NodeId(0, 38), new NodeId(0, 894).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 895), new NodeId(0, 39), new NodeId(0, 8882).expanded(), true));
      node.addReference(
          new Reference(new NodeId(0, 895), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode669() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 24038),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 24038), new NodeId(0, 38), new NodeId(0, 24033).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 24038), new NodeId(0, 39), new NodeId(0, 24039).expanded(), true));
      node.addReference(
          new Reference(
              new NodeId(0, 24038), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode670() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.XML_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 892),
              new QualifiedName(0, "Default XML"),
              new LocalizedText("", "Default XML"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 892), new NodeId(0, 38), new NodeId(0, 891).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 892), new NodeId(0, 39), new NodeId(0, 8879).expanded(), true));
      node.addReference(
          new Reference(new NodeId(0, 892), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode671() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15085),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15085), new NodeId(0, 38), new NodeId(0, 12756).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15085), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode672() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15041),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15041), new NodeId(0, 38), new NodeId(0, 14533).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15041), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode673() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 17547),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 17547), new NodeId(0, 38), new NodeId(0, 16313).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 17547), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode674() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 17557),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 17557), new NodeId(0, 38), new NodeId(0, 17548).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 17557), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode675() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 16150),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 16150), new NodeId(0, 38), new NodeId(0, 15528).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 16150), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode676() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 32430),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 32430), new NodeId(0, 38), new NodeId(0, 32421).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 32430), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode677() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 19064),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 19064), new NodeId(0, 38), new NodeId(0, 18806).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 19064), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode678() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 19065),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 19065), new NodeId(0, 38), new NodeId(0, 18807).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 19065), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode679() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 19066),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 19066), new NodeId(0, 38), new NodeId(0, 18808).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 19066), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode680() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 19067),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 19067), new NodeId(0, 38), new NodeId(0, 18809).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 19067), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode681() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 19068),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 19068), new NodeId(0, 38), new NodeId(0, 18810).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 19068), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode682() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 19069),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 19069), new NodeId(0, 38), new NodeId(0, 18811).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 19069), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode683() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 19070),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 19070), new NodeId(0, 38), new NodeId(0, 18812).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 19070), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode684() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 19071),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 19071), new NodeId(0, 38), new NodeId(0, 18813).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 19071), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode685() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 19072),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 19072), new NodeId(0, 38), new NodeId(0, 18814).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 19072), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode686() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15042),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15042), new NodeId(0, 38), new NodeId(0, 15634).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15042), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode687() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 23528),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 23528), new NodeId(0, 38), new NodeId(0, 23498).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 23528), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode688() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 32584),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 32584), new NodeId(0, 38), new NodeId(0, 32434).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 32584), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode689() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 32585),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 32585), new NodeId(0, 38), new NodeId(0, 32435).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 32585), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode690() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 32586),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 32586), new NodeId(0, 38), new NodeId(0, 32438).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 32586), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode691() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15044),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15044), new NodeId(0, 38), new NodeId(0, 12554).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15044), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode692() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 32390),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 32390), new NodeId(0, 38), new NodeId(0, 32285).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 32390), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode693() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 16151),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 16151), new NodeId(0, 38), new NodeId(0, 15534).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 16151), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode694() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15057),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15057), new NodeId(0, 38), new NodeId(0, 14525).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15057), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode695() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15058),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15058), new NodeId(0, 38), new NodeId(0, 15487).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15058), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode696() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15059),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15059), new NodeId(0, 38), new NodeId(0, 15488).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15059), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode697() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15700),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15700), new NodeId(0, 38), new NodeId(0, 15005).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15700), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode698() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15714),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15714), new NodeId(0, 38), new NodeId(0, 15006).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15714), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode699() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 24132),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 24132), new NodeId(0, 38), new NodeId(0, 24105).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 24132), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode700() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 24133),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 24133), new NodeId(0, 38), new NodeId(0, 24106).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 24133), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode701() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 24134),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 24134), new NodeId(0, 38), new NodeId(0, 24107).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 24134), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode702() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15050),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15050), new NodeId(0, 38), new NodeId(0, 14523).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15050), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode703() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15051),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15051), new NodeId(0, 38), new NodeId(0, 14524).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15051), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode704() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15049),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15049), new NodeId(0, 38), new NodeId(0, 14593).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15049), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode705() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 16152),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 16152), new NodeId(0, 38), new NodeId(0, 15578).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 16152), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode706() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 16153),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 16153), new NodeId(0, 38), new NodeId(0, 15580).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 16153), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode707() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15060),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15060), new NodeId(0, 38), new NodeId(0, 14273).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15060), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode708() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 16154),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 16154), new NodeId(0, 38), new NodeId(0, 15581).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 16154), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode709() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 16155),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 16155), new NodeId(0, 38), new NodeId(0, 15582).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 16155), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode710() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 25561),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 25561), new NodeId(0, 38), new NodeId(0, 25269).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 25561), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode711() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 18622),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 18622), new NodeId(0, 38), new NodeId(0, 18593).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 18622), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode712() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 18623),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 18623), new NodeId(0, 38), new NodeId(0, 18594).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 18623), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode713() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 18624),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 18624), new NodeId(0, 38), new NodeId(0, 18597).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 18624), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode714() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 18945),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 18945), new NodeId(0, 38), new NodeId(0, 18793).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 18945), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode715() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 16156),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 16156), new NodeId(0, 38), new NodeId(0, 15597).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 16156), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode716() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 16157),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 16157), new NodeId(0, 38), new NodeId(0, 15598).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 16157), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode717() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 16158),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 16158), new NodeId(0, 38), new NodeId(0, 15605).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 16158), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode718() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 16159),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 16159), new NodeId(0, 38), new NodeId(0, 15609).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 16159), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode719() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 21198),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 21198), new NodeId(0, 38), new NodeId(0, 15480).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 21198), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode720() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 16161),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 16161), new NodeId(0, 38), new NodeId(0, 15611).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 16161), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode721() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 16280),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 16280), new NodeId(0, 38), new NodeId(0, 15616).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 16280), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode722() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 16281),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 16281), new NodeId(0, 38), new NodeId(0, 15617).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 16281), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode723() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 16282),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 16282), new NodeId(0, 38), new NodeId(0, 15618).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 16282), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode724() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 21199),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 21199), new NodeId(0, 38), new NodeId(0, 15502).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 21199), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode725() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 21200),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 21200), new NodeId(0, 38), new NodeId(0, 15510).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 21200), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode726() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 21201),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 21201), new NodeId(0, 38), new NodeId(0, 15520).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 21201), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode727() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 16284),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 16284), new NodeId(0, 38), new NodeId(0, 15621).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 16284), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode728() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 16285),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 16285), new NodeId(0, 38), new NodeId(0, 15622).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 16285), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode729() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 16286),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 16286), new NodeId(0, 38), new NodeId(0, 15623).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 16286), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode730() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 16287),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 16287), new NodeId(0, 38), new NodeId(0, 15628).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 16287), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode731() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 16288),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 16288), new NodeId(0, 38), new NodeId(0, 15629).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 16288), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode732() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 16308),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 16308), new NodeId(0, 38), new NodeId(0, 15630).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 16308), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode733() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 16310),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 16310), new NodeId(0, 38), new NodeId(0, 15631).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 16310), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode734() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15061),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15061), new NodeId(0, 38), new NodeId(0, 14744).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15061), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode735() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 16311),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 16311), new NodeId(0, 38), new NodeId(0, 15635).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 16311), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode736() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 21202),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 21202), new NodeId(0, 38), new NodeId(0, 15530).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 21202), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode737() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 23987),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 23987), new NodeId(0, 38), new NodeId(0, 23599).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 23987), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode738() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 23988),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 23988), new NodeId(0, 38), new NodeId(0, 23600).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 23988), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode739() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 23989),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 23989), new NodeId(0, 38), new NodeId(0, 23601).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 23989), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode740() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 25562),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 25562), new NodeId(0, 38), new NodeId(0, 25270).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 25562), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode741() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 23990),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 23990), new NodeId(0, 38), new NodeId(0, 23602).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 23990), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode742() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 16323),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 16323), new NodeId(0, 38), new NodeId(0, 15645).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 16323), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode743() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 16391),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 16391), new NodeId(0, 38), new NodeId(0, 15652).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 16391), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode744() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 16392),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 16392), new NodeId(0, 38), new NodeId(0, 15653).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 16392), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode745() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 16393),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 16393), new NodeId(0, 38), new NodeId(0, 15657).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 16393), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode746() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 16394),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 16394), new NodeId(0, 38), new NodeId(0, 15664).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 16394), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode747() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 16404),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 16404), new NodeId(0, 38), new NodeId(0, 15665).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 16404), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode748() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 23991),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 23991), new NodeId(0, 38), new NodeId(0, 23603).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 23991), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode749() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 23992),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 23992), new NodeId(0, 38), new NodeId(0, 23604).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 23992), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode750() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 23993),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 23993), new NodeId(0, 38), new NodeId(0, 23605).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 23993), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode751() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 23996),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 23996), new NodeId(0, 38), new NodeId(0, 23608).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 23996), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode752() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 23997),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 23997), new NodeId(0, 38), new NodeId(0, 23609).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 23997), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode753() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 17476),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 17476), new NodeId(0, 38), new NodeId(0, 17467).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 17476), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode754() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 24000),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 24000), new NodeId(0, 38), new NodeId(0, 23612).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 24000), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode755() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 21203),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 21203), new NodeId(0, 38), new NodeId(0, 15532).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 21203), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode756() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 24001),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 24001), new NodeId(0, 38), new NodeId(0, 23613).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 24001), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode757() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 24002),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 24002), new NodeId(0, 38), new NodeId(0, 23614).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 24002), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode758() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 18946),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 18946), new NodeId(0, 38), new NodeId(0, 18794).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 18946), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode759() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15726),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15726), new NodeId(0, 38), new NodeId(0, 15007).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15726), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode760() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 16524),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 16524), new NodeId(0, 38), new NodeId(0, 15667).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 16524), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode761() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 16525),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 16525), new NodeId(0, 38), new NodeId(0, 15669).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 16525), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode762() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 16526),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 16526), new NodeId(0, 38), new NodeId(0, 15670).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 16526), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode763() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 25563),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 25563), new NodeId(0, 38), new NodeId(0, 25519).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 25563), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode764() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 25564),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 25564), new NodeId(0, 38), new NodeId(0, 25520).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 25564), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode765() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 23511),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 23511), new NodeId(0, 38), new NodeId(0, 23468).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 23511), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode766() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 24300),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 24300), new NodeId(0, 38), new NodeId(0, 24281).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 24300), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode767() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 25247),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 25247), new NodeId(0, 38), new NodeId(0, 25220).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 25247), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode768() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 19299),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 19299), new NodeId(0, 38), new NodeId(0, 18953).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 19299), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode769() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 19300),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 19300), new NodeId(0, 38), new NodeId(0, 18954).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 19300), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode770() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 19301),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 19301), new NodeId(0, 38), new NodeId(0, 18955).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 19301), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode771() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 32677),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 32677), new NodeId(0, 38), new NodeId(0, 32659).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 32677), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode772() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 32678),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 32678), new NodeId(0, 38), new NodeId(0, 32660).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 32678), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode773() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15062),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15062), new NodeId(0, 38), new NodeId(0, 96).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15062), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode774() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15063),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15063), new NodeId(0, 38), new NodeId(0, 97).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15063), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode775() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15065),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15065), new NodeId(0, 38), new NodeId(0, 101).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15065), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode776() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15066),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15066), new NodeId(0, 38), new NodeId(0, 99).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15066), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode777() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15067),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15067), new NodeId(0, 38), new NodeId(0, 100).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15067), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode778() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15081),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15081), new NodeId(0, 38), new NodeId(0, 296).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15081), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode779() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15082),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15082), new NodeId(0, 38), new NodeId(0, 7594).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15082), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode780() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15083),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15083), new NodeId(0, 38), new NodeId(0, 102).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15083), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode781() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15084),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15084), new NodeId(0, 38), new NodeId(0, 12755).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15084), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode782() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15086),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15086), new NodeId(0, 38), new NodeId(0, 8912).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15086), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode783() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15087),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15087), new NodeId(0, 38), new NodeId(0, 308).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15087), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode784() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15095),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15095), new NodeId(0, 38), new NodeId(0, 12189).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15095), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode785() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15098),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15098), new NodeId(0, 38), new NodeId(0, 304).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15098), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode786() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15099),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15099), new NodeId(0, 38), new NodeId(0, 312).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15099), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode787() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15102),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15102), new NodeId(0, 38), new NodeId(0, 432).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15102), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode788() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15105),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15105), new NodeId(0, 38), new NodeId(0, 12890).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15105), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode789() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15106),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15106), new NodeId(0, 38), new NodeId(0, 12891).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15106), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode790() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15136),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15136), new NodeId(0, 38), new NodeId(0, 344).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15136), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode791() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15140),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15140), new NodeId(0, 38), new NodeId(0, 316).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15140), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode792() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15141),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15141), new NodeId(0, 38), new NodeId(0, 319).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15141), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode793() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15142),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15142), new NodeId(0, 38), new NodeId(0, 322).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15142), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode794() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15143),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15143), new NodeId(0, 38), new NodeId(0, 325).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15143), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode795() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15144),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15144), new NodeId(0, 38), new NodeId(0, 938).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15144), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode796() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15165),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15165), new NodeId(0, 38), new NodeId(0, 376).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15165), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode797() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15169),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15169), new NodeId(0, 38), new NodeId(0, 379).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15169), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode798() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15172),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15172), new NodeId(0, 38), new NodeId(0, 382).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15172), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode799() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15175),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15175), new NodeId(0, 38), new NodeId(0, 385).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15175), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode800() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15188),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15188), new NodeId(0, 38), new NodeId(0, 537).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15188), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode801() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15189),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15189), new NodeId(0, 38), new NodeId(0, 540).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15189), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode802() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15199),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15199), new NodeId(0, 38), new NodeId(0, 331).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15199), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode803() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15204),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15204), new NodeId(0, 38), new NodeId(0, 583).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15204), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode804() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15205),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15205), new NodeId(0, 38), new NodeId(0, 586).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15205), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode805() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15206),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15206), new NodeId(0, 38), new NodeId(0, 589).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15206), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode806() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15207),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15207), new NodeId(0, 38), new NodeId(0, 592).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15207), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode807() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15208),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15208), new NodeId(0, 38), new NodeId(0, 595).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15208), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode808() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15209),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15209), new NodeId(0, 38), new NodeId(0, 598).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15209), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode809() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15210),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15210), new NodeId(0, 38), new NodeId(0, 601).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15210), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode810() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15271),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15271), new NodeId(0, 38), new NodeId(0, 11216).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15271), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode811() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15273),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15273), new NodeId(0, 38), new NodeId(0, 659).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15273), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode812() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 32833),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 32833), new NodeId(0, 38), new NodeId(0, 32824).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 32833), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode813() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15293),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15293), new NodeId(0, 38), new NodeId(0, 719).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15293), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode814() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15295),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15295), new NodeId(0, 38), new NodeId(0, 725).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15295), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode815() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15304),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15304), new NodeId(0, 38), new NodeId(0, 948).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15304), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode816() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15349),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15349), new NodeId(0, 38), new NodeId(0, 920).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15349), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode817() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15361),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15361), new NodeId(0, 38), new NodeId(0, 338).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15361), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode818() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15362),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15362), new NodeId(0, 38), new NodeId(0, 853).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15362), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode819() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15363),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15363), new NodeId(0, 38), new NodeId(0, 11943).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15363), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode820() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15364),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15364), new NodeId(0, 38), new NodeId(0, 11944).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15364), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode821() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15365),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15365), new NodeId(0, 38), new NodeId(0, 856).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15365), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode822() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15366),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15366), new NodeId(0, 38), new NodeId(0, 859).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15366), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode823() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15367),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15367), new NodeId(0, 38), new NodeId(0, 862).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15367), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode824() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15368),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15368), new NodeId(0, 38), new NodeId(0, 865).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15368), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode825() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15369),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15369), new NodeId(0, 38), new NodeId(0, 868).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15369), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode826() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15370),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15370), new NodeId(0, 38), new NodeId(0, 871).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15370), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode827() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15371),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15371), new NodeId(0, 38), new NodeId(0, 299).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15371), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode828() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15372),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15372), new NodeId(0, 38), new NodeId(0, 874).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15372), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode829() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15373),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15373), new NodeId(0, 38), new NodeId(0, 877).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15373), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode830() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15374),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15374), new NodeId(0, 38), new NodeId(0, 897).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15374), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode831() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15375),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15375), new NodeId(0, 38), new NodeId(0, 884).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15375), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode832() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15376),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15376), new NodeId(0, 38), new NodeId(0, 887).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15376), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode833() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15377),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15377), new NodeId(0, 38), new NodeId(0, 12171).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15377), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode834() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15378),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15378), new NodeId(0, 38), new NodeId(0, 12172).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15378), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode835() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15379),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15379), new NodeId(0, 38), new NodeId(0, 12079).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15379), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode836() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15380),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15380), new NodeId(0, 38), new NodeId(0, 12080).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15380), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode837() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15381),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15381), new NodeId(0, 38), new NodeId(0, 894).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15381), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode838() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 24042),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 24042), new NodeId(0, 38), new NodeId(0, 24033).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 24042), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
  }

  void loadNode839() {
    if (context.getServer().getEncodingManager().hasEncoding(DataTypeEncoding.JSON_ENCODING_NAME)) {
      var node =
          new DataTypeEncodingTypeNode(
              this.context,
              new NodeId(0, 15382),
              new QualifiedName(0, "Default JSON"),
              new LocalizedText("", "Default JSON"),
              LocalizedText.NULL_VALUE,
              UInteger.valueOf(0),
              UInteger.valueOf(0),
              null,
              null,
              new AccessRestrictionType(UShort.valueOf(0)),
              UByte.valueOf(0));
      node.addReference(
          new Reference(
              new NodeId(0, 15382), new NodeId(0, 38), new NodeId(0, 891).expanded(), false));
      node.addReference(
          new Reference(
              new NodeId(0, 15382), new NodeId(0, 40), new NodeId(0, 76).expanded(), true));
      this.nodeManager.addNode(node);
    }
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
    loadNode284();
    loadNode285();
    loadNode286();
    loadNode287();
    loadNode288();
    loadNode289();
    loadNode290();
    loadNode291();
    loadNode292();
    loadNode293();
    loadNode294();
    loadNode295();
    loadNode296();
    loadNode297();
    loadNode298();
    loadNode299();
    loadNode300();
    loadNode301();
    loadNode302();
    loadNode303();
    loadNode304();
    loadNode305();
    loadNode306();
    loadNode307();
    loadNode308();
    loadNode309();
    loadNode310();
    loadNode311();
    loadNode312();
    loadNode313();
    loadNode314();
    loadNode315();
    loadNode316();
    loadNode317();
    loadNode318();
    loadNode319();
    loadNode320();
    loadNode321();
    loadNode322();
    loadNode323();
    loadNode324();
    loadNode325();
    loadNode326();
    loadNode327();
    loadNode328();
    loadNode329();
    loadNode330();
    loadNode331();
    loadNode332();
    loadNode333();
    loadNode334();
    loadNode335();
    loadNode336();
    loadNode337();
    loadNode338();
    loadNode339();
    loadNode340();
    loadNode341();
    loadNode342();
    loadNode343();
    loadNode344();
    loadNode345();
    loadNode346();
    loadNode347();
    loadNode348();
    loadNode349();
    loadNode350();
    loadNode351();
    loadNode352();
    loadNode353();
    loadNode354();
    loadNode355();
    loadNode356();
    loadNode357();
    loadNode358();
    loadNode359();
    loadNode360();
    loadNode361();
    loadNode362();
    loadNode363();
    loadNode364();
    loadNode365();
    loadNode366();
    loadNode367();
    loadNode368();
    loadNode369();
    loadNode370();
    loadNode371();
    loadNode372();
    loadNode373();
    loadNode374();
    loadNode375();
    loadNode376();
    loadNode377();
    loadNode378();
    loadNode379();
    loadNode380();
    loadNode381();
    loadNode382();
    loadNode383();
    loadNode384();
    loadNode385();
    loadNode386();
    loadNode387();
    loadNode388();
    loadNode389();
    loadNode390();
    loadNode391();
    loadNode392();
    loadNode393();
    loadNode394();
    loadNode395();
    loadNode396();
    loadNode397();
    loadNode398();
    loadNode399();
    loadNode400();
    loadNode401();
    loadNode402();
    loadNode403();
    loadNode404();
    loadNode405();
    loadNode406();
    loadNode407();
    loadNode408();
    loadNode409();
    loadNode410();
    loadNode411();
    loadNode412();
    loadNode413();
    loadNode414();
    loadNode415();
    loadNode416();
    loadNode417();
    loadNode418();
    loadNode419();
    loadNode420();
    loadNode421();
    loadNode422();
    loadNode423();
    loadNode424();
    loadNode425();
    loadNode426();
    loadNode427();
    loadNode428();
    loadNode429();
    loadNode430();
    loadNode431();
    loadNode432();
    loadNode433();
    loadNode434();
    loadNode435();
    loadNode436();
    loadNode437();
    loadNode438();
    loadNode439();
    loadNode440();
    loadNode441();
    loadNode442();
    loadNode443();
    loadNode444();
    loadNode445();
    loadNode446();
    loadNode447();
    loadNode448();
    loadNode449();
    loadNode450();
    loadNode451();
    loadNode452();
    loadNode453();
    loadNode454();
    loadNode455();
    loadNode456();
    loadNode457();
    loadNode458();
    loadNode459();
    loadNode460();
    loadNode461();
    loadNode462();
    loadNode463();
    loadNode464();
    loadNode465();
    loadNode466();
    loadNode467();
    loadNode468();
    loadNode469();
    loadNode470();
    loadNode471();
    loadNode472();
    loadNode473();
    loadNode474();
    loadNode475();
    loadNode476();
    loadNode477();
    loadNode478();
    loadNode479();
    loadNode480();
    loadNode481();
    loadNode482();
    loadNode483();
    loadNode484();
    loadNode485();
    loadNode486();
    loadNode487();
    loadNode488();
    loadNode489();
    loadNode490();
    loadNode491();
    loadNode492();
    loadNode493();
    loadNode494();
    loadNode495();
    loadNode496();
    loadNode497();
    loadNode498();
    loadNode499();
    loadNode500();
    loadNode501();
    loadNode502();
    loadNode503();
    loadNode504();
    loadNode505();
    loadNode506();
    loadNode507();
    loadNode508();
    loadNode509();
    loadNode510();
    loadNode511();
    loadNode512();
    loadNode513();
    loadNode514();
    loadNode515();
    loadNode516();
    loadNode517();
    loadNode518();
    loadNode519();
    loadNode520();
    loadNode521();
    loadNode522();
    loadNode523();
    loadNode524();
    loadNode525();
    loadNode526();
    loadNode527();
    loadNode528();
    loadNode529();
    loadNode530();
    loadNode531();
    loadNode532();
    loadNode533();
    loadNode534();
    loadNode535();
    loadNode536();
    loadNode537();
    loadNode538();
    loadNode539();
    loadNode540();
    loadNode541();
    loadNode542();
    loadNode543();
    loadNode544();
    loadNode545();
    loadNode546();
    loadNode547();
    loadNode548();
    loadNode549();
    loadNode550();
    loadNode551();
    loadNode552();
    loadNode553();
    loadNode554();
    loadNode555();
    loadNode556();
    loadNode557();
    loadNode558();
    loadNode559();
    loadNode560();
    loadNode561();
    loadNode562();
    loadNode563();
    loadNode564();
    loadNode565();
    loadNode566();
    loadNode567();
    loadNode568();
    loadNode569();
    loadNode570();
    loadNode571();
    loadNode572();
    loadNode573();
    loadNode574();
    loadNode575();
    loadNode576();
    loadNode577();
    loadNode578();
    loadNode579();
    loadNode580();
    loadNode581();
    loadNode582();
    loadNode583();
    loadNode584();
    loadNode585();
    loadNode586();
    loadNode587();
    loadNode588();
    loadNode589();
    loadNode590();
    loadNode591();
    loadNode592();
    loadNode593();
    loadNode594();
    loadNode595();
    loadNode596();
    loadNode597();
    loadNode598();
    loadNode599();
    loadNode600();
    loadNode601();
    loadNode602();
    loadNode603();
    loadNode604();
    loadNode605();
    loadNode606();
    loadNode607();
    loadNode608();
    loadNode609();
    loadNode610();
    loadNode611();
    loadNode612();
    loadNode613();
    loadNode614();
    loadNode615();
    loadNode616();
    loadNode617();
    loadNode618();
    loadNode619();
    loadNode620();
    loadNode621();
    loadNode622();
    loadNode623();
    loadNode624();
    loadNode625();
    loadNode626();
    loadNode627();
    loadNode628();
    loadNode629();
    loadNode630();
    loadNode631();
    loadNode632();
    loadNode633();
    loadNode634();
    loadNode635();
    loadNode636();
    loadNode637();
    loadNode638();
    loadNode639();
    loadNode640();
    loadNode641();
    loadNode642();
    loadNode643();
    loadNode644();
    loadNode645();
    loadNode646();
    loadNode647();
    loadNode648();
    loadNode649();
    loadNode650();
    loadNode651();
    loadNode652();
    loadNode653();
    loadNode654();
    loadNode655();
    loadNode656();
    loadNode657();
    loadNode658();
    loadNode659();
    loadNode660();
    loadNode661();
    loadNode662();
    loadNode663();
    loadNode664();
    loadNode665();
    loadNode666();
    loadNode667();
    loadNode668();
    loadNode669();
    loadNode670();
    loadNode671();
    loadNode672();
    loadNode673();
    loadNode674();
    loadNode675();
    loadNode676();
    loadNode677();
    loadNode678();
    loadNode679();
    loadNode680();
    loadNode681();
    loadNode682();
    loadNode683();
    loadNode684();
    loadNode685();
    loadNode686();
    loadNode687();
    loadNode688();
    loadNode689();
    loadNode690();
    loadNode691();
    loadNode692();
    loadNode693();
    loadNode694();
    loadNode695();
    loadNode696();
    loadNode697();
    loadNode698();
    loadNode699();
    loadNode700();
    loadNode701();
    loadNode702();
    loadNode703();
    loadNode704();
    loadNode705();
    loadNode706();
    loadNode707();
    loadNode708();
    loadNode709();
    loadNode710();
    loadNode711();
    loadNode712();
    loadNode713();
    loadNode714();
    loadNode715();
    loadNode716();
    loadNode717();
    loadNode718();
    loadNode719();
    loadNode720();
    loadNode721();
    loadNode722();
    loadNode723();
    loadNode724();
    loadNode725();
    loadNode726();
    loadNode727();
    loadNode728();
    loadNode729();
    loadNode730();
    loadNode731();
    loadNode732();
    loadNode733();
    loadNode734();
    loadNode735();
    loadNode736();
    loadNode737();
    loadNode738();
    loadNode739();
    loadNode740();
    loadNode741();
    loadNode742();
    loadNode743();
    loadNode744();
    loadNode745();
    loadNode746();
    loadNode747();
    loadNode748();
    loadNode749();
    loadNode750();
    loadNode751();
    loadNode752();
    loadNode753();
    loadNode754();
    loadNode755();
    loadNode756();
    loadNode757();
    loadNode758();
    loadNode759();
    loadNode760();
    loadNode761();
    loadNode762();
    loadNode763();
    loadNode764();
    loadNode765();
    loadNode766();
    loadNode767();
    loadNode768();
    loadNode769();
    loadNode770();
    loadNode771();
    loadNode772();
    loadNode773();
    loadNode774();
    loadNode775();
    loadNode776();
    loadNode777();
    loadNode778();
    loadNode779();
    loadNode780();
    loadNode781();
    loadNode782();
    loadNode783();
    loadNode784();
    loadNode785();
    loadNode786();
    loadNode787();
    loadNode788();
    loadNode789();
    loadNode790();
    loadNode791();
    loadNode792();
    loadNode793();
    loadNode794();
    loadNode795();
    loadNode796();
    loadNode797();
    loadNode798();
    loadNode799();
    loadNode800();
    loadNode801();
    loadNode802();
    loadNode803();
    loadNode804();
    loadNode805();
    loadNode806();
    loadNode807();
    loadNode808();
    loadNode809();
    loadNode810();
    loadNode811();
    loadNode812();
    loadNode813();
    loadNode814();
    loadNode815();
    loadNode816();
    loadNode817();
    loadNode818();
    loadNode819();
    loadNode820();
    loadNode821();
    loadNode822();
    loadNode823();
    loadNode824();
    loadNode825();
    loadNode826();
    loadNode827();
    loadNode828();
    loadNode829();
    loadNode830();
    loadNode831();
    loadNode832();
    loadNode833();
    loadNode834();
    loadNode835();
    loadNode836();
    loadNode837();
    loadNode838();
    loadNode839();
  }
}

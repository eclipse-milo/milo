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
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectTypeNode;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;

class ObjectTypeNodeLoader {
  private final UaNodeContext context;

  private final NodeManager<UaNode> nodeManager;

  ObjectTypeNodeLoader(UaNodeContext context, NodeManager<UaNode> nodeManager) {
    this.context = context;
    this.nodeManager = nodeManager;
  }

  void loadNode0() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 58),
            new QualifiedName(0, "BaseObjectType"),
            new LocalizedText("", "BaseObjectType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    this.nodeManager.addNode(node);
  }

  void loadNode1() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 61),
            new QualifiedName(0, "FolderType"),
            new LocalizedText("", "FolderType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(new NodeId(0, 61), new NodeId(0, 45), new NodeId(0, 58).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode2() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 75),
            new QualifiedName(0, "DataTypeSystemType"),
            new LocalizedText("", "DataTypeSystemType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(new NodeId(0, 75), new NodeId(0, 45), new NodeId(0, 58).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode3() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 76),
            new QualifiedName(0, "DataTypeEncodingType"),
            new LocalizedText("", "DataTypeEncodingType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(new NodeId(0, 76), new NodeId(0, 45), new NodeId(0, 58).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode4() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 77),
            new QualifiedName(0, "ModellingRuleType"),
            new LocalizedText("", "ModellingRuleType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(new NodeId(0, 77), new NodeId(0, 45), new NodeId(0, 58).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode5() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 2004),
            new QualifiedName(0, "ServerType"),
            new LocalizedText("", "ServerType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 2004), new NodeId(0, 46), new NodeId(0, 2005).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2004), new NodeId(0, 46), new NodeId(0, 2006).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2004), new NodeId(0, 46), new NodeId(0, 15003).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2004), new NodeId(0, 47), new NodeId(0, 2007).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2004), new NodeId(0, 46), new NodeId(0, 2008).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2004), new NodeId(0, 46), new NodeId(0, 2742).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2004), new NodeId(0, 46), new NodeId(0, 12882).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2004), new NodeId(0, 46), new NodeId(0, 17612).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2004), new NodeId(0, 47), new NodeId(0, 2009).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2004), new NodeId(0, 47), new NodeId(0, 2010).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2004), new NodeId(0, 47), new NodeId(0, 2011).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2004), new NodeId(0, 47), new NodeId(0, 2012).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2004), new NodeId(0, 47), new NodeId(0, 11527).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2004), new NodeId(0, 47), new NodeId(0, 11489).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2004), new NodeId(0, 47), new NodeId(0, 12871).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2004), new NodeId(0, 47), new NodeId(0, 12746).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2004), new NodeId(0, 47), new NodeId(0, 12883).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 2004), new NodeId(0, 45), new NodeId(0, 58).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode6() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 2013),
            new QualifiedName(0, "ServerCapabilitiesType"),
            new LocalizedText("", "ServerCapabilitiesType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 2013), new NodeId(0, 46), new NodeId(0, 2014).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2013), new NodeId(0, 46), new NodeId(0, 2016).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2013), new NodeId(0, 46), new NodeId(0, 2017).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2013), new NodeId(0, 46), new NodeId(0, 2732).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2013), new NodeId(0, 46), new NodeId(0, 2733).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2013), new NodeId(0, 46), new NodeId(0, 2734).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2013), new NodeId(0, 46), new NodeId(0, 3049).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2013), new NodeId(0, 46), new NodeId(0, 11549).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2013), new NodeId(0, 46), new NodeId(0, 11550).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2013), new NodeId(0, 46), new NodeId(0, 12910).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2013), new NodeId(0, 47), new NodeId(0, 11551).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2013), new NodeId(0, 47), new NodeId(0, 2019).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2013), new NodeId(0, 47), new NodeId(0, 2754).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2013), new NodeId(0, 47), new NodeId(0, 11562).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2013), new NodeId(0, 47), new NodeId(0, 16295).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2013), new NodeId(0, 46), new NodeId(0, 24088).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2013), new NodeId(0, 46), new NodeId(0, 24089).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2013), new NodeId(0, 46), new NodeId(0, 24090).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2013), new NodeId(0, 46), new NodeId(0, 24091).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2013), new NodeId(0, 46), new NodeId(0, 24103).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2013), new NodeId(0, 46), new NodeId(0, 24092).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2013), new NodeId(0, 46), new NodeId(0, 24093).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2013), new NodeId(0, 46), new NodeId(0, 31770).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2013), new NodeId(0, 46), new NodeId(0, 24094).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 2013), new NodeId(0, 45), new NodeId(0, 58).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode7() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 2020),
            new QualifiedName(0, "ServerDiagnosticsType"),
            new LocalizedText("", "ServerDiagnosticsType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 2020), new NodeId(0, 47), new NodeId(0, 2021).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2020), new NodeId(0, 47), new NodeId(0, 2022).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2020), new NodeId(0, 47), new NodeId(0, 2023).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2020), new NodeId(0, 47), new NodeId(0, 2744).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2020), new NodeId(0, 46), new NodeId(0, 2025).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 2020), new NodeId(0, 45), new NodeId(0, 58).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode8() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 2026),
            new QualifiedName(0, "SessionsDiagnosticsSummaryType"),
            new LocalizedText("", "SessionsDiagnosticsSummaryType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 2026), new NodeId(0, 47), new NodeId(0, 2027).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2026), new NodeId(0, 47), new NodeId(0, 2028).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2026), new NodeId(0, 47), new NodeId(0, 12097).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 2026), new NodeId(0, 45), new NodeId(0, 58).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode9() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 2029),
            new QualifiedName(0, "SessionDiagnosticsObjectType"),
            new LocalizedText("", "SessionDiagnosticsObjectType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 2029), new NodeId(0, 47), new NodeId(0, 2030).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2029), new NodeId(0, 47), new NodeId(0, 2031).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2029), new NodeId(0, 47), new NodeId(0, 2032).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2029), new NodeId(0, 46), new NodeId(0, 19303).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 2029), new NodeId(0, 45), new NodeId(0, 58).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode10() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 2033),
            new QualifiedName(0, "VendorServerInfoType"),
            new LocalizedText("", "VendorServerInfoType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(new NodeId(0, 2033), new NodeId(0, 45), new NodeId(0, 58).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode11() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 2034),
            new QualifiedName(0, "ServerRedundancyType"),
            new LocalizedText("", "ServerRedundancyType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 2034), new NodeId(0, 46), new NodeId(0, 2035).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2034), new NodeId(0, 46), new NodeId(0, 32410).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 2034), new NodeId(0, 45), new NodeId(0, 58).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode12() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 2036),
            new QualifiedName(0, "TransparentRedundancyType"),
            new LocalizedText("", "TransparentRedundancyType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 2036), new NodeId(0, 46), new NodeId(0, 2038).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2036), new NodeId(0, 46), new NodeId(0, 2037).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2036), new NodeId(0, 45), new NodeId(0, 2034).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode13() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 2039),
            new QualifiedName(0, "NonTransparentRedundancyType"),
            new LocalizedText("", "NonTransparentRedundancyType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 2039), new NodeId(0, 46), new NodeId(0, 2040).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2039), new NodeId(0, 45), new NodeId(0, 2034).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode14() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 11945),
            new QualifiedName(0, "NonTransparentNetworkRedundancyType"),
            new LocalizedText("", "NonTransparentNetworkRedundancyType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 11945), new NodeId(0, 46), new NodeId(0, 11948).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11945), new NodeId(0, 45), new NodeId(0, 2039).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode15() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 32411),
            new QualifiedName(0, "NonTransparentBackupRedundancyType"),
            new LocalizedText("", "NonTransparentBackupRedundancyType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 32411), new NodeId(0, 46), new NodeId(0, 32413).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32411), new NodeId(0, 46), new NodeId(0, 32415).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32411), new NodeId(0, 47), new NodeId(0, 32416).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32411), new NodeId(0, 45), new NodeId(0, 2039).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode16() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 11564),
            new QualifiedName(0, "OperationLimitsType"),
            new LocalizedText("", "OperationLimitsType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 11564), new NodeId(0, 46), new NodeId(0, 11565).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11564), new NodeId(0, 46), new NodeId(0, 12161).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11564), new NodeId(0, 46), new NodeId(0, 12162).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11564), new NodeId(0, 46), new NodeId(0, 11567).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11564), new NodeId(0, 46), new NodeId(0, 12163).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11564), new NodeId(0, 46), new NodeId(0, 12164).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11564), new NodeId(0, 46), new NodeId(0, 11569).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11564), new NodeId(0, 46), new NodeId(0, 11570).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11564), new NodeId(0, 46), new NodeId(0, 11571).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11564), new NodeId(0, 46), new NodeId(0, 11572).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11564), new NodeId(0, 46), new NodeId(0, 11573).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11564), new NodeId(0, 46), new NodeId(0, 11574).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11564), new NodeId(0, 45), new NodeId(0, 61).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode17() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 11575),
            new QualifiedName(0, "FileType"),
            new LocalizedText("", "FileType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 11575), new NodeId(0, 46), new NodeId(0, 11576).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11575), new NodeId(0, 46), new NodeId(0, 12686).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11575), new NodeId(0, 46), new NodeId(0, 12687).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11575), new NodeId(0, 46), new NodeId(0, 11579).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11575), new NodeId(0, 46), new NodeId(0, 13341).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11575), new NodeId(0, 46), new NodeId(0, 24244).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11575), new NodeId(0, 46), new NodeId(0, 25200).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11575), new NodeId(0, 47), new NodeId(0, 11580).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11575), new NodeId(0, 47), new NodeId(0, 11583).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11575), new NodeId(0, 47), new NodeId(0, 11585).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11575), new NodeId(0, 47), new NodeId(0, 11588).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11575), new NodeId(0, 47), new NodeId(0, 11590).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11575), new NodeId(0, 47), new NodeId(0, 11593).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11575), new NodeId(0, 45), new NodeId(0, 58).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode18() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 11595),
            new QualifiedName(0, "AddressSpaceFileType"),
            new LocalizedText("", "AddressSpaceFileType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 11595), new NodeId(0, 47), new NodeId(0, 11615).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11595), new NodeId(0, 45), new NodeId(0, 11575).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode19() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 11616),
            new QualifiedName(0, "NamespaceMetadataType"),
            new LocalizedText("", "NamespaceMetadataType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 11616), new NodeId(0, 46), new NodeId(0, 11617).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11616), new NodeId(0, 46), new NodeId(0, 11618).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11616), new NodeId(0, 46), new NodeId(0, 11619).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11616), new NodeId(0, 46), new NodeId(0, 11620).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11616), new NodeId(0, 46), new NodeId(0, 11621).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11616), new NodeId(0, 46), new NodeId(0, 11622).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11616), new NodeId(0, 46), new NodeId(0, 11623).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11616), new NodeId(0, 47), new NodeId(0, 11624).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11616), new NodeId(0, 46), new NodeId(0, 16137).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11616), new NodeId(0, 46), new NodeId(0, 16138).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11616), new NodeId(0, 46), new NodeId(0, 16139).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11616), new NodeId(0, 46), new NodeId(0, 25267).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11616), new NodeId(0, 46), new NodeId(0, 32419).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11616), new NodeId(0, 45), new NodeId(0, 58).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode20() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 11645),
            new QualifiedName(0, "NamespacesType"),
            new LocalizedText("", "NamespacesType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 11645), new NodeId(0, 47), new NodeId(0, 11646).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11645), new NodeId(0, 45), new NodeId(0, 58).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode21() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 2041),
            new QualifiedName(0, "BaseEventType"),
            new LocalizedText("", "BaseEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 2041), new NodeId(0, 46), new NodeId(0, 2042).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2041), new NodeId(0, 46), new NodeId(0, 2043).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2041), new NodeId(0, 46), new NodeId(0, 2044).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2041), new NodeId(0, 46), new NodeId(0, 2045).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2041), new NodeId(0, 46), new NodeId(0, 2046).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2041), new NodeId(0, 46), new NodeId(0, 2047).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2041), new NodeId(0, 46), new NodeId(0, 3190).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2041), new NodeId(0, 46), new NodeId(0, 2050).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2041), new NodeId(0, 46), new NodeId(0, 2051).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2041), new NodeId(0, 46), new NodeId(0, 31771).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2041), new NodeId(0, 46), new NodeId(0, 31772).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2041), new NodeId(0, 46), new NodeId(0, 31773).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2041), new NodeId(0, 46), new NodeId(0, 31774).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 2041), new NodeId(0, 45), new NodeId(0, 58).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode22() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 2052),
            new QualifiedName(0, "AuditEventType"),
            new LocalizedText("", "AuditEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 2052), new NodeId(0, 46), new NodeId(0, 2053).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2052), new NodeId(0, 46), new NodeId(0, 2054).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2052), new NodeId(0, 46), new NodeId(0, 2055).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2052), new NodeId(0, 46), new NodeId(0, 2056).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2052), new NodeId(0, 46), new NodeId(0, 2057).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2052), new NodeId(0, 45), new NodeId(0, 2041).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode23() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 2058),
            new QualifiedName(0, "AuditSecurityEventType"),
            new LocalizedText("", "AuditSecurityEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 2058), new NodeId(0, 46), new NodeId(0, 17615).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2058), new NodeId(0, 45), new NodeId(0, 2052).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode24() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 2059),
            new QualifiedName(0, "AuditChannelEventType"),
            new LocalizedText("", "AuditChannelEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 2059), new NodeId(0, 46), new NodeId(0, 2745).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2059), new NodeId(0, 45), new NodeId(0, 2058).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode25() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 2060),
            new QualifiedName(0, "AuditOpenSecureChannelEventType"),
            new LocalizedText("", "AuditOpenSecureChannelEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 2060), new NodeId(0, 46), new NodeId(0, 2061).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2060), new NodeId(0, 46), new NodeId(0, 2746).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2060), new NodeId(0, 46), new NodeId(0, 2062).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2060), new NodeId(0, 46), new NodeId(0, 2063).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2060), new NodeId(0, 46), new NodeId(0, 2065).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2060), new NodeId(0, 46), new NodeId(0, 2066).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2060), new NodeId(0, 46), new NodeId(0, 24135).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2060), new NodeId(0, 45), new NodeId(0, 2059).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode26() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 2069),
            new QualifiedName(0, "AuditSessionEventType"),
            new LocalizedText("", "AuditSessionEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 2069), new NodeId(0, 46), new NodeId(0, 2070).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2069), new NodeId(0, 45), new NodeId(0, 2058).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode27() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 2071),
            new QualifiedName(0, "AuditCreateSessionEventType"),
            new LocalizedText("", "AuditCreateSessionEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 2071), new NodeId(0, 46), new NodeId(0, 2072).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2071), new NodeId(0, 46), new NodeId(0, 2073).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2071), new NodeId(0, 46), new NodeId(0, 2747).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2071), new NodeId(0, 46), new NodeId(0, 2074).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2071), new NodeId(0, 45), new NodeId(0, 2069).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode28() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 2748),
            new QualifiedName(0, "AuditUrlMismatchEventType"),
            new LocalizedText("", "AuditUrlMismatchEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 2748), new NodeId(0, 46), new NodeId(0, 2749).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2748), new NodeId(0, 45), new NodeId(0, 2071).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode29() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 2075),
            new QualifiedName(0, "AuditActivateSessionEventType"),
            new LocalizedText("", "AuditActivateSessionEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 2075), new NodeId(0, 46), new NodeId(0, 2076).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2075), new NodeId(0, 46), new NodeId(0, 2077).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2075), new NodeId(0, 46), new NodeId(0, 11485).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2075), new NodeId(0, 46), new NodeId(0, 19304).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2075), new NodeId(0, 45), new NodeId(0, 2069).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode30() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 2078),
            new QualifiedName(0, "AuditCancelEventType"),
            new LocalizedText("", "AuditCancelEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 2078), new NodeId(0, 46), new NodeId(0, 2079).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2078), new NodeId(0, 45), new NodeId(0, 2069).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode31() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 2080),
            new QualifiedName(0, "AuditCertificateEventType"),
            new LocalizedText("", "AuditCertificateEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 2080), new NodeId(0, 46), new NodeId(0, 2081).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2080), new NodeId(0, 45), new NodeId(0, 2058).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode32() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 2082),
            new QualifiedName(0, "AuditCertificateDataMismatchEventType"),
            new LocalizedText("", "AuditCertificateDataMismatchEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 2082), new NodeId(0, 46), new NodeId(0, 2083).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2082), new NodeId(0, 46), new NodeId(0, 2084).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2082), new NodeId(0, 45), new NodeId(0, 2080).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode33() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 2085),
            new QualifiedName(0, "AuditCertificateExpiredEventType"),
            new LocalizedText("", "AuditCertificateExpiredEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 2085), new NodeId(0, 45), new NodeId(0, 2080).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode34() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 2086),
            new QualifiedName(0, "AuditCertificateInvalidEventType"),
            new LocalizedText("", "AuditCertificateInvalidEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 2086), new NodeId(0, 45), new NodeId(0, 2080).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode35() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 2087),
            new QualifiedName(0, "AuditCertificateUntrustedEventType"),
            new LocalizedText("", "AuditCertificateUntrustedEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 2087), new NodeId(0, 45), new NodeId(0, 2080).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode36() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 2088),
            new QualifiedName(0, "AuditCertificateRevokedEventType"),
            new LocalizedText("", "AuditCertificateRevokedEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 2088), new NodeId(0, 45), new NodeId(0, 2080).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode37() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 2089),
            new QualifiedName(0, "AuditCertificateMismatchEventType"),
            new LocalizedText("", "AuditCertificateMismatchEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 2089), new NodeId(0, 45), new NodeId(0, 2080).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode38() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 2090),
            new QualifiedName(0, "AuditNodeManagementEventType"),
            new LocalizedText("", "AuditNodeManagementEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 2090), new NodeId(0, 45), new NodeId(0, 2052).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode39() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 2091),
            new QualifiedName(0, "AuditAddNodesEventType"),
            new LocalizedText("", "AuditAddNodesEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 2091), new NodeId(0, 46), new NodeId(0, 2092).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2091), new NodeId(0, 45), new NodeId(0, 2090).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode40() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 2093),
            new QualifiedName(0, "AuditDeleteNodesEventType"),
            new LocalizedText("", "AuditDeleteNodesEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 2093), new NodeId(0, 46), new NodeId(0, 2094).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2093), new NodeId(0, 45), new NodeId(0, 2090).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode41() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 2095),
            new QualifiedName(0, "AuditAddReferencesEventType"),
            new LocalizedText("", "AuditAddReferencesEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 2095), new NodeId(0, 46), new NodeId(0, 2096).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2095), new NodeId(0, 45), new NodeId(0, 2090).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode42() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 2097),
            new QualifiedName(0, "AuditDeleteReferencesEventType"),
            new LocalizedText("", "AuditDeleteReferencesEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 2097), new NodeId(0, 46), new NodeId(0, 2098).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2097), new NodeId(0, 45), new NodeId(0, 2090).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode43() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 2099),
            new QualifiedName(0, "AuditUpdateEventType"),
            new LocalizedText("", "AuditUpdateEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 2099), new NodeId(0, 45), new NodeId(0, 2052).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode44() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 2100),
            new QualifiedName(0, "AuditWriteUpdateEventType"),
            new LocalizedText("", "AuditWriteUpdateEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 2100), new NodeId(0, 46), new NodeId(0, 2750).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2100), new NodeId(0, 46), new NodeId(0, 2101).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2100), new NodeId(0, 46), new NodeId(0, 2102).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2100), new NodeId(0, 46), new NodeId(0, 2103).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2100), new NodeId(0, 45), new NodeId(0, 2099).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode45() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 2104),
            new QualifiedName(0, "AuditHistoryUpdateEventType"),
            new LocalizedText("", "AuditHistoryUpdateEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 2104), new NodeId(0, 46), new NodeId(0, 2751).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2104), new NodeId(0, 45), new NodeId(0, 2099).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode46() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 2127),
            new QualifiedName(0, "AuditUpdateMethodEventType"),
            new LocalizedText("", "AuditUpdateMethodEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 2127), new NodeId(0, 46), new NodeId(0, 2128).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2127), new NodeId(0, 46), new NodeId(0, 19305).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2127), new NodeId(0, 46), new NodeId(0, 2129).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2127), new NodeId(0, 46), new NodeId(0, 19306).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2127), new NodeId(0, 45), new NodeId(0, 2052).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode47() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 2130),
            new QualifiedName(0, "SystemEventType"),
            new LocalizedText("", "SystemEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 2130), new NodeId(0, 45), new NodeId(0, 2041).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode48() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 2131),
            new QualifiedName(0, "DeviceFailureEventType"),
            new LocalizedText("", "DeviceFailureEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 2131), new NodeId(0, 45), new NodeId(0, 2130).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode49() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 11446),
            new QualifiedName(0, "SystemStatusChangeEventType"),
            new LocalizedText("", "SystemStatusChangeEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 11446), new NodeId(0, 46), new NodeId(0, 11696).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11446), new NodeId(0, 45), new NodeId(0, 2130).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode50() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 2132),
            new QualifiedName(0, "BaseModelChangeEventType"),
            new LocalizedText("", "BaseModelChangeEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 2132), new NodeId(0, 45), new NodeId(0, 2041).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode51() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 2133),
            new QualifiedName(0, "GeneralModelChangeEventType"),
            new LocalizedText("", "GeneralModelChangeEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 2133), new NodeId(0, 46), new NodeId(0, 2134).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2133), new NodeId(0, 45), new NodeId(0, 2132).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode52() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 2738),
            new QualifiedName(0, "SemanticChangeEventType"),
            new LocalizedText("", "SemanticChangeEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 2738), new NodeId(0, 46), new NodeId(0, 2739).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2738), new NodeId(0, 45), new NodeId(0, 2041).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode53() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 3035),
            new QualifiedName(0, "EventQueueOverflowEventType"),
            new LocalizedText("", "EventQueueOverflowEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 3035), new NodeId(0, 45), new NodeId(0, 2041).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode54() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 11436),
            new QualifiedName(0, "ProgressEventType"),
            new LocalizedText("", "ProgressEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 11436), new NodeId(0, 46), new NodeId(0, 12502).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11436), new NodeId(0, 46), new NodeId(0, 12503).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11436), new NodeId(0, 45), new NodeId(0, 2041).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode55() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 23606),
            new QualifiedName(0, "AuditClientEventType"),
            new LocalizedText("", "AuditClientEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 23606), new NodeId(0, 46), new NodeId(0, 23908).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23606), new NodeId(0, 45), new NodeId(0, 2052).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode56() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 23926),
            new QualifiedName(0, "AuditClientUpdateMethodResultEventType"),
            new LocalizedText("", "AuditClientUpdateMethodResultEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 23926), new NodeId(0, 46), new NodeId(0, 23994).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23926), new NodeId(0, 46), new NodeId(0, 23995).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23926), new NodeId(0, 46), new NodeId(0, 23998).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23926), new NodeId(0, 46), new NodeId(0, 23999).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23926), new NodeId(0, 46), new NodeId(0, 25684).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23926), new NodeId(0, 45), new NodeId(0, 23606).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode57() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 2340),
            new QualifiedName(0, "AggregateFunctionType"),
            new LocalizedText("", "AggregateFunctionType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(new NodeId(0, 2340), new NodeId(0, 45), new NodeId(0, 58).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode58() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 2299),
            new QualifiedName(0, "StateMachineType"),
            new LocalizedText("", "StateMachineType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 2299), new NodeId(0, 47), new NodeId(0, 2769).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2299), new NodeId(0, 47), new NodeId(0, 2770).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 2299), new NodeId(0, 45), new NodeId(0, 58).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode59() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 2771),
            new QualifiedName(0, "FiniteStateMachineType"),
            new LocalizedText("", "FiniteStateMachineType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 2771), new NodeId(0, 47), new NodeId(0, 2772).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2771), new NodeId(0, 47), new NodeId(0, 2773).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2771), new NodeId(0, 47), new NodeId(0, 17635).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2771), new NodeId(0, 47), new NodeId(0, 17636).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2771), new NodeId(0, 45), new NodeId(0, 2299).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode60() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 2307),
            new QualifiedName(0, "StateType"),
            new LocalizedText("", "StateType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 2307), new NodeId(0, 46), new NodeId(0, 2308).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 2307), new NodeId(0, 45), new NodeId(0, 58).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode61() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 2309),
            new QualifiedName(0, "InitialStateType"),
            new LocalizedText("", "InitialStateType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 2309), new NodeId(0, 45), new NodeId(0, 2307).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode62() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 2310),
            new QualifiedName(0, "TransitionType"),
            new LocalizedText("", "TransitionType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 2310), new NodeId(0, 46), new NodeId(0, 2312).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 2310), new NodeId(0, 45), new NodeId(0, 58).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode63() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 15109),
            new QualifiedName(0, "ChoiceStateType"),
            new LocalizedText("", "ChoiceStateType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 15109), new NodeId(0, 45), new NodeId(0, 2307).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode64() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 2311),
            new QualifiedName(0, "TransitionEventType"),
            new LocalizedText("", "TransitionEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 2311), new NodeId(0, 47), new NodeId(0, 2774).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2311), new NodeId(0, 47), new NodeId(0, 2775).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2311), new NodeId(0, 47), new NodeId(0, 2776).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2311), new NodeId(0, 45), new NodeId(0, 2041).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode65() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 2315),
            new QualifiedName(0, "AuditUpdateStateEventType"),
            new LocalizedText("", "AuditUpdateStateEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 2315), new NodeId(0, 46), new NodeId(0, 2777).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2315), new NodeId(0, 46), new NodeId(0, 2778).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2315), new NodeId(0, 45), new NodeId(0, 2127).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode66() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 13353),
            new QualifiedName(0, "FileDirectoryType"),
            new LocalizedText("", "FileDirectoryType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 13353), new NodeId(0, 35), new NodeId(0, 13354).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13353), new NodeId(0, 35), new NodeId(0, 13366).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13353), new NodeId(0, 47), new NodeId(0, 13387).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13353), new NodeId(0, 47), new NodeId(0, 13390).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13353), new NodeId(0, 47), new NodeId(0, 13393).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13353), new NodeId(0, 47), new NodeId(0, 13395).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13353), new NodeId(0, 45), new NodeId(0, 61).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode67() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 15744),
            new QualifiedName(0, "TemporaryFileTransferType"),
            new LocalizedText("", "TemporaryFileTransferType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 15744), new NodeId(0, 46), new NodeId(0, 15745).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15744), new NodeId(0, 47), new NodeId(0, 15746).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15744), new NodeId(0, 47), new NodeId(0, 15749).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15744), new NodeId(0, 47), new NodeId(0, 15751).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15744), new NodeId(0, 47), new NodeId(0, 15754).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15744), new NodeId(0, 45), new NodeId(0, 58).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode68() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 15803),
            new QualifiedName(0, "FileTransferStateMachineType"),
            new LocalizedText("", "FileTransferStateMachineType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 15803), new NodeId(0, 47), new NodeId(0, 15815).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15803), new NodeId(0, 47), new NodeId(0, 15817).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15803), new NodeId(0, 47), new NodeId(0, 15819).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15803), new NodeId(0, 47), new NodeId(0, 15821).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15803), new NodeId(0, 47), new NodeId(0, 15823).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15803), new NodeId(0, 47), new NodeId(0, 15825).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15803), new NodeId(0, 47), new NodeId(0, 15827).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15803), new NodeId(0, 47), new NodeId(0, 15829).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15803), new NodeId(0, 47), new NodeId(0, 15831).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15803), new NodeId(0, 47), new NodeId(0, 15833).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15803), new NodeId(0, 47), new NodeId(0, 15835).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15803), new NodeId(0, 47), new NodeId(0, 15837).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15803), new NodeId(0, 47), new NodeId(0, 15839).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15803), new NodeId(0, 47), new NodeId(0, 15841).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15803), new NodeId(0, 47), new NodeId(0, 15843).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15803), new NodeId(0, 45), new NodeId(0, 2771).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode69() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 15607),
            new QualifiedName(0, "RoleSetType"),
            new LocalizedText("", "RoleSetType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 15607), new NodeId(0, 47), new NodeId(0, 15608).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15607), new NodeId(0, 47), new NodeId(0, 15997).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15607), new NodeId(0, 47), new NodeId(0, 16000).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15607), new NodeId(0, 45), new NodeId(0, 58).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode70() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 15620),
            new QualifiedName(0, "RoleType"),
            new LocalizedText("", "RoleType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 15620), new NodeId(0, 46), new NodeId(0, 16173).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15620), new NodeId(0, 46), new NodeId(0, 15410).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15620), new NodeId(0, 46), new NodeId(0, 16174).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15620), new NodeId(0, 46), new NodeId(0, 15411).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15620), new NodeId(0, 46), new NodeId(0, 16175).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15620), new NodeId(0, 46), new NodeId(0, 24139).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15620), new NodeId(0, 47), new NodeId(0, 15624).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15620), new NodeId(0, 47), new NodeId(0, 15626).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15620), new NodeId(0, 47), new NodeId(0, 16176).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15620), new NodeId(0, 47), new NodeId(0, 16178).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15620), new NodeId(0, 47), new NodeId(0, 16180).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15620), new NodeId(0, 47), new NodeId(0, 16182).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15620), new NodeId(0, 45), new NodeId(0, 58).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode71() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 17641),
            new QualifiedName(0, "RoleMappingRuleChangedAuditEventType"),
            new LocalizedText("", "RoleMappingRuleChangedAuditEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 17641), new NodeId(0, 45), new NodeId(0, 2127).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode72() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 17589),
            new QualifiedName(0, "DictionaryEntryType"),
            new LocalizedText("", "DictionaryEntryType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 17589), new NodeId(0, 47), new NodeId(0, 17590).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17589), new NodeId(0, 45), new NodeId(0, 58).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode73() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 17591),
            new QualifiedName(0, "DictionaryFolderType"),
            new LocalizedText("", "DictionaryFolderType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 17591), new NodeId(0, 47), new NodeId(0, 17592).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17591), new NodeId(0, 47), new NodeId(0, 17593).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17591), new NodeId(0, 45), new NodeId(0, 61).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode74() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 17598),
            new QualifiedName(0, "IrdiDictionaryEntryType"),
            new LocalizedText("", "IrdiDictionaryEntryType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 17598), new NodeId(0, 45), new NodeId(0, 17589).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode75() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 17600),
            new QualifiedName(0, "UriDictionaryEntryType"),
            new LocalizedText("", "UriDictionaryEntryType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 17600), new NodeId(0, 45), new NodeId(0, 17589).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode76() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 17602),
            new QualifiedName(0, "BaseInterfaceType"),
            new LocalizedText("", "BaseInterfaceType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 17602), new NodeId(0, 45), new NodeId(0, 58).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode77() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 23513),
            new QualifiedName(0, "IOrderedObjectType"),
            new LocalizedText("", "IOrderedObjectType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 23513), new NodeId(0, 46), new NodeId(0, 23517).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23513), new NodeId(0, 45), new NodeId(0, 17602).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode78() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 23518),
            new QualifiedName(0, "OrderedListType"),
            new LocalizedText("", "OrderedListType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 23518), new NodeId(0, 49), new NodeId(0, 23519).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23518), new NodeId(0, 46), new NodeId(0, 23525).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23518), new NodeId(0, 41), new NodeId(0, 2133).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23518), new NodeId(0, 45), new NodeId(0, 58).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode79() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 32439),
            new QualifiedName(0, "SyntaxReferenceEntryType"),
            new LocalizedText("", "SyntaxReferenceEntryType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 32439), new NodeId(0, 46), new NodeId(0, 32441).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32439), new NodeId(0, 45), new NodeId(0, 17589).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode80() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 32442),
            new QualifiedName(0, "UnitType"),
            new LocalizedText("", "UnitType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 32442), new NodeId(0, 46), new NodeId(0, 32443).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32442), new NodeId(0, 46), new NodeId(0, 32445).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32442), new NodeId(0, 46), new NodeId(0, 32446).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32442), new NodeId(0, 45), new NodeId(0, 58).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode81() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 32447),
            new QualifiedName(0, "ServerUnitType"),
            new LocalizedText("", "ServerUnitType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 32447), new NodeId(0, 47), new NodeId(0, 32452).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32447), new NodeId(0, 46), new NodeId(0, 32461).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32447), new NodeId(0, 47), new NodeId(0, 32462).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32447), new NodeId(0, 45), new NodeId(0, 32442).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode82() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 32467),
            new QualifiedName(0, "AlternativeUnitType"),
            new LocalizedText("", "AlternativeUnitType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 32467), new NodeId(0, 46), new NodeId(0, 32472).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32467), new NodeId(0, 46), new NodeId(0, 32473).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32467), new NodeId(0, 46), new NodeId(0, 32474).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32467), new NodeId(0, 45), new NodeId(0, 32442).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode83() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 32475),
            new QualifiedName(0, "QuantityType"),
            new LocalizedText("", "QuantityType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 32475), new NodeId(0, 46), new NodeId(0, 32476).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32475), new NodeId(0, 46), new NodeId(0, 32478).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32475), new NodeId(0, 46), new NodeId(0, 32479).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32475), new NodeId(0, 46), new NodeId(0, 32480).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32475), new NodeId(0, 47), new NodeId(0, 32481).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32475), new NodeId(0, 45), new NodeId(0, 58).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode84() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 32502),
            new QualifiedName(0, "QuantitiesFolderType"),
            new LocalizedText("", "QuantitiesFolderType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 32502), new NodeId(0, 35), new NodeId(0, 32503).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32502), new NodeId(0, 45), new NodeId(0, 61).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode85() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 2782),
            new QualifiedName(0, "ConditionType"),
            new LocalizedText("", "ConditionType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 2782), new NodeId(0, 46), new NodeId(0, 11112).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2782), new NodeId(0, 46), new NodeId(0, 11113).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2782), new NodeId(0, 46), new NodeId(0, 16363).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2782), new NodeId(0, 46), new NodeId(0, 16364).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2782), new NodeId(0, 46), new NodeId(0, 9009).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2782), new NodeId(0, 46), new NodeId(0, 9010).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2782), new NodeId(0, 46), new NodeId(0, 3874).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2782), new NodeId(0, 46), new NodeId(0, 32060).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2782), new NodeId(0, 47), new NodeId(0, 9011).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2782), new NodeId(0, 47), new NodeId(0, 9020).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2782), new NodeId(0, 47), new NodeId(0, 9022).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2782), new NodeId(0, 47), new NodeId(0, 9024).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2782), new NodeId(0, 46), new NodeId(0, 9026).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2782), new NodeId(0, 47), new NodeId(0, 9028).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2782), new NodeId(0, 47), new NodeId(0, 9027).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2782), new NodeId(0, 47), new NodeId(0, 9029).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2782), new NodeId(0, 47), new NodeId(0, 3875).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2782), new NodeId(0, 47), new NodeId(0, 12912).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2782), new NodeId(0, 45), new NodeId(0, 2041).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode86() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 2830),
            new QualifiedName(0, "DialogConditionType"),
            new LocalizedText("", "DialogConditionType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 2830), new NodeId(0, 47), new NodeId(0, 9035).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2830), new NodeId(0, 47), new NodeId(0, 9055).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2830), new NodeId(0, 46), new NodeId(0, 2831).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2830), new NodeId(0, 46), new NodeId(0, 9064).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2830), new NodeId(0, 46), new NodeId(0, 9065).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2830), new NodeId(0, 46), new NodeId(0, 9066).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2830), new NodeId(0, 46), new NodeId(0, 9067).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2830), new NodeId(0, 46), new NodeId(0, 9068).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2830), new NodeId(0, 47), new NodeId(0, 9069).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2830), new NodeId(0, 47), new NodeId(0, 24312).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2830), new NodeId(0, 45), new NodeId(0, 2782).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode87() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 2881),
            new QualifiedName(0, "AcknowledgeableConditionType"),
            new LocalizedText("", "AcknowledgeableConditionType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 2881), new NodeId(0, 47), new NodeId(0, 9073).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2881), new NodeId(0, 47), new NodeId(0, 9093).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2881), new NodeId(0, 47), new NodeId(0, 9102).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2881), new NodeId(0, 47), new NodeId(0, 9111).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2881), new NodeId(0, 47), new NodeId(0, 9113).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2881), new NodeId(0, 45), new NodeId(0, 2782).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode88() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 2915),
            new QualifiedName(0, "AlarmConditionType"),
            new LocalizedText("", "AlarmConditionType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 2915), new NodeId(0, 47), new NodeId(0, 9118).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2915), new NodeId(0, 47), new NodeId(0, 9160).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2915), new NodeId(0, 46), new NodeId(0, 11120).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2915), new NodeId(0, 47), new NodeId(0, 9169).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2915), new NodeId(0, 47), new NodeId(0, 16371).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2915), new NodeId(0, 47), new NodeId(0, 9178).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2915), new NodeId(0, 46), new NodeId(0, 9215).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2915), new NodeId(0, 46), new NodeId(0, 9216).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2915), new NodeId(0, 46), new NodeId(0, 16389).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2915), new NodeId(0, 47), new NodeId(0, 16390).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2915), new NodeId(0, 47), new NodeId(0, 16380).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2915), new NodeId(0, 46), new NodeId(0, 16395).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2915), new NodeId(0, 46), new NodeId(0, 16396).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2915), new NodeId(0, 47), new NodeId(0, 16397).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2915), new NodeId(0, 47), new NodeId(0, 16398).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2915), new NodeId(0, 47), new NodeId(0, 18190).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2915), new NodeId(0, 16361), new NodeId(0, 16399).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2915), new NodeId(0, 46), new NodeId(0, 16400).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2915), new NodeId(0, 47), new NodeId(0, 16401).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2915), new NodeId(0, 47), new NodeId(0, 16402).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2915), new NodeId(0, 47), new NodeId(0, 16403).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2915), new NodeId(0, 47), new NodeId(0, 24316).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2915), new NodeId(0, 47), new NodeId(0, 17868).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2915), new NodeId(0, 47), new NodeId(0, 24318).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2915), new NodeId(0, 47), new NodeId(0, 17869).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2915), new NodeId(0, 47), new NodeId(0, 24320).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2915), new NodeId(0, 47), new NodeId(0, 17870).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2915), new NodeId(0, 47), new NodeId(0, 24322).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2915), new NodeId(0, 47), new NodeId(0, 18199).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2915), new NodeId(0, 47), new NodeId(0, 24324).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2915), new NodeId(0, 47), new NodeId(0, 24744).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2915), new NodeId(0, 45), new NodeId(0, 2881).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode89() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 16405),
            new QualifiedName(0, "AlarmGroupType"),
            new LocalizedText("", "AlarmGroupType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 16405), new NodeId(0, 16362), new NodeId(0, 16406).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16405), new NodeId(0, 45), new NodeId(0, 61).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode90() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 32064),
            new QualifiedName(0, "AlarmSuppressionGroupType"),
            new LocalizedText("", "AlarmSuppressionGroupType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 32064), new NodeId(0, 32059), new NodeId(0, 32226).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32064), new NodeId(0, 45), new NodeId(0, 16405).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode91() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 2929),
            new QualifiedName(0, "ShelvedStateMachineType"),
            new LocalizedText("", "ShelvedStateMachineType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 2929), new NodeId(0, 46), new NodeId(0, 9115).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2929), new NodeId(0, 47), new NodeId(0, 2930).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2929), new NodeId(0, 47), new NodeId(0, 2932).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2929), new NodeId(0, 47), new NodeId(0, 2933).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2929), new NodeId(0, 47), new NodeId(0, 2935).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2929), new NodeId(0, 47), new NodeId(0, 2936).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2929), new NodeId(0, 47), new NodeId(0, 2940).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2929), new NodeId(0, 47), new NodeId(0, 2942).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2929), new NodeId(0, 47), new NodeId(0, 2943).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2929), new NodeId(0, 47), new NodeId(0, 2945).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2929), new NodeId(0, 47), new NodeId(0, 2949).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2929), new NodeId(0, 47), new NodeId(0, 24756).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2929), new NodeId(0, 47), new NodeId(0, 2947).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2929), new NodeId(0, 47), new NodeId(0, 24758).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2929), new NodeId(0, 47), new NodeId(0, 2948).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2929), new NodeId(0, 47), new NodeId(0, 24760).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2929), new NodeId(0, 45), new NodeId(0, 2771).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode92() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 2955),
            new QualifiedName(0, "LimitAlarmType"),
            new LocalizedText("", "LimitAlarmType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 2955), new NodeId(0, 46), new NodeId(0, 11124).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2955), new NodeId(0, 46), new NodeId(0, 11125).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2955), new NodeId(0, 46), new NodeId(0, 11126).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2955), new NodeId(0, 46), new NodeId(0, 11127).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2955), new NodeId(0, 46), new NodeId(0, 16572).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2955), new NodeId(0, 46), new NodeId(0, 16573).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2955), new NodeId(0, 46), new NodeId(0, 16574).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2955), new NodeId(0, 46), new NodeId(0, 16575).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2955), new NodeId(0, 46), new NodeId(0, 24770).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2955), new NodeId(0, 46), new NodeId(0, 24771).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2955), new NodeId(0, 46), new NodeId(0, 24772).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2955), new NodeId(0, 46), new NodeId(0, 24773).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2955), new NodeId(0, 46), new NodeId(0, 24774).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2955), new NodeId(0, 46), new NodeId(0, 24775).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2955), new NodeId(0, 46), new NodeId(0, 24776).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2955), new NodeId(0, 46), new NodeId(0, 24777).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2955), new NodeId(0, 45), new NodeId(0, 2915).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode93() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 9318),
            new QualifiedName(0, "ExclusiveLimitStateMachineType"),
            new LocalizedText("", "ExclusiveLimitStateMachineType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 9318), new NodeId(0, 47), new NodeId(0, 9329).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 9318), new NodeId(0, 47), new NodeId(0, 9331).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 9318), new NodeId(0, 47), new NodeId(0, 9333).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 9318), new NodeId(0, 47), new NodeId(0, 9335).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 9318), new NodeId(0, 47), new NodeId(0, 9337).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 9318), new NodeId(0, 47), new NodeId(0, 9338).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 9318), new NodeId(0, 47), new NodeId(0, 9339).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 9318), new NodeId(0, 47), new NodeId(0, 9340).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 9318), new NodeId(0, 45), new NodeId(0, 2771).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode94() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 9341),
            new QualifiedName(0, "ExclusiveLimitAlarmType"),
            new LocalizedText("", "ExclusiveLimitAlarmType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 9341), new NodeId(0, 47), new NodeId(0, 9398).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 9341), new NodeId(0, 47), new NodeId(0, 9455).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 9341), new NodeId(0, 45), new NodeId(0, 2955).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode95() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 9906),
            new QualifiedName(0, "NonExclusiveLimitAlarmType"),
            new LocalizedText("", "NonExclusiveLimitAlarmType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 9906), new NodeId(0, 47), new NodeId(0, 9963).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 9906), new NodeId(0, 47), new NodeId(0, 10020).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 9906), new NodeId(0, 47), new NodeId(0, 10029).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 9906), new NodeId(0, 47), new NodeId(0, 10038).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 9906), new NodeId(0, 47), new NodeId(0, 10047).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 9906), new NodeId(0, 45), new NodeId(0, 2955).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode96() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 10060),
            new QualifiedName(0, "NonExclusiveLevelAlarmType"),
            new LocalizedText("", "NonExclusiveLevelAlarmType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 10060), new NodeId(0, 45), new NodeId(0, 9906).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode97() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 9482),
            new QualifiedName(0, "ExclusiveLevelAlarmType"),
            new LocalizedText("", "ExclusiveLevelAlarmType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 9482), new NodeId(0, 45), new NodeId(0, 9341).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode98() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 10368),
            new QualifiedName(0, "NonExclusiveDeviationAlarmType"),
            new LocalizedText("", "NonExclusiveDeviationAlarmType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 10368), new NodeId(0, 46), new NodeId(0, 10522).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 10368), new NodeId(0, 46), new NodeId(0, 16776).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 10368), new NodeId(0, 45), new NodeId(0, 9906).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode99() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 10214),
            new QualifiedName(0, "NonExclusiveRateOfChangeAlarmType"),
            new LocalizedText("", "NonExclusiveRateOfChangeAlarmType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 10214), new NodeId(0, 46), new NodeId(0, 16858).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 10214), new NodeId(0, 45), new NodeId(0, 9906).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode100() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 9764),
            new QualifiedName(0, "ExclusiveDeviationAlarmType"),
            new LocalizedText("", "ExclusiveDeviationAlarmType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 9764), new NodeId(0, 46), new NodeId(0, 9905).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 9764), new NodeId(0, 46), new NodeId(0, 16817).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 9764), new NodeId(0, 45), new NodeId(0, 9341).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode101() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 9623),
            new QualifiedName(0, "ExclusiveRateOfChangeAlarmType"),
            new LocalizedText("", "ExclusiveRateOfChangeAlarmType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 9623), new NodeId(0, 46), new NodeId(0, 16899).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 9623), new NodeId(0, 45), new NodeId(0, 9341).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode102() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 10523),
            new QualifiedName(0, "DiscreteAlarmType"),
            new LocalizedText("", "DiscreteAlarmType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 10523), new NodeId(0, 45), new NodeId(0, 2915).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode103() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 10637),
            new QualifiedName(0, "OffNormalAlarmType"),
            new LocalizedText("", "OffNormalAlarmType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 10637), new NodeId(0, 46), new NodeId(0, 11158).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 10637), new NodeId(0, 45), new NodeId(0, 10523).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode104() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 11753),
            new QualifiedName(0, "SystemOffNormalAlarmType"),
            new LocalizedText("", "SystemOffNormalAlarmType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 11753), new NodeId(0, 45), new NodeId(0, 10637).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode105() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 10751),
            new QualifiedName(0, "TripAlarmType"),
            new LocalizedText("", "TripAlarmType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 10751), new NodeId(0, 45), new NodeId(0, 10637).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode106() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 18347),
            new QualifiedName(0, "InstrumentDiagnosticAlarmType"),
            new LocalizedText("", "InstrumentDiagnosticAlarmType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 18347), new NodeId(0, 45), new NodeId(0, 10637).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode107() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 18496),
            new QualifiedName(0, "SystemDiagnosticAlarmType"),
            new LocalizedText("", "SystemDiagnosticAlarmType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 18496), new NodeId(0, 45), new NodeId(0, 10637).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode108() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 13225),
            new QualifiedName(0, "CertificateExpirationAlarmType"),
            new LocalizedText("", "CertificateExpirationAlarmType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 13225), new NodeId(0, 46), new NodeId(0, 13325).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13225), new NodeId(0, 46), new NodeId(0, 14900).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13225), new NodeId(0, 46), new NodeId(0, 13326).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13225), new NodeId(0, 46), new NodeId(0, 13327).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13225), new NodeId(0, 45), new NodeId(0, 11753).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode109() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 17080),
            new QualifiedName(0, "DiscrepancyAlarmType"),
            new LocalizedText("", "DiscrepancyAlarmType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 17080), new NodeId(0, 46), new NodeId(0, 17215).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17080), new NodeId(0, 46), new NodeId(0, 17216).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17080), new NodeId(0, 46), new NodeId(0, 17217).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17080), new NodeId(0, 45), new NodeId(0, 2915).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode110() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 11163),
            new QualifiedName(0, "BaseConditionClassType"),
            new LocalizedText("", "BaseConditionClassType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 11163), new NodeId(0, 45), new NodeId(0, 58).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode111() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 11164),
            new QualifiedName(0, "ProcessConditionClassType"),
            new LocalizedText("", "ProcessConditionClassType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 11164), new NodeId(0, 45), new NodeId(0, 11163).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode112() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 11165),
            new QualifiedName(0, "MaintenanceConditionClassType"),
            new LocalizedText("", "MaintenanceConditionClassType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 11165), new NodeId(0, 45), new NodeId(0, 11163).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode113() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 11166),
            new QualifiedName(0, "SystemConditionClassType"),
            new LocalizedText("", "SystemConditionClassType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 11166), new NodeId(0, 45), new NodeId(0, 11163).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode114() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 17218),
            new QualifiedName(0, "SafetyConditionClassType"),
            new LocalizedText("", "SafetyConditionClassType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 17218), new NodeId(0, 45), new NodeId(0, 11163).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode115() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 17219),
            new QualifiedName(0, "HighlyManagedAlarmConditionClassType"),
            new LocalizedText("", "HighlyManagedAlarmConditionClassType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 17219), new NodeId(0, 45), new NodeId(0, 11163).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode116() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 17220),
            new QualifiedName(0, "TrainingConditionClassType"),
            new LocalizedText("", "TrainingConditionClassType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 17220), new NodeId(0, 45), new NodeId(0, 11163).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode117() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 18665),
            new QualifiedName(0, "StatisticalConditionClassType"),
            new LocalizedText("", "StatisticalConditionClassType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 18665), new NodeId(0, 45), new NodeId(0, 11163).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode118() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 17221),
            new QualifiedName(0, "TestingConditionClassType"),
            new LocalizedText("", "TestingConditionClassType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 17221), new NodeId(0, 45), new NodeId(0, 11163).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode119() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 2790),
            new QualifiedName(0, "AuditConditionEventType"),
            new LocalizedText("", "AuditConditionEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 2790), new NodeId(0, 45), new NodeId(0, 2127).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode120() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 2803),
            new QualifiedName(0, "AuditConditionEnableEventType"),
            new LocalizedText("", "AuditConditionEnableEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 2803), new NodeId(0, 45), new NodeId(0, 2790).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode121() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 2829),
            new QualifiedName(0, "AuditConditionCommentEventType"),
            new LocalizedText("", "AuditConditionCommentEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 2829), new NodeId(0, 46), new NodeId(0, 17222).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2829), new NodeId(0, 46), new NodeId(0, 11851).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2829), new NodeId(0, 45), new NodeId(0, 2790).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode122() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 8927),
            new QualifiedName(0, "AuditConditionRespondEventType"),
            new LocalizedText("", "AuditConditionRespondEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 8927), new NodeId(0, 46), new NodeId(0, 11852).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 8927), new NodeId(0, 45), new NodeId(0, 2790).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode123() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 8944),
            new QualifiedName(0, "AuditConditionAcknowledgeEventType"),
            new LocalizedText("", "AuditConditionAcknowledgeEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 8944), new NodeId(0, 46), new NodeId(0, 17223).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 8944), new NodeId(0, 46), new NodeId(0, 11853).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 8944), new NodeId(0, 45), new NodeId(0, 2790).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode124() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 8961),
            new QualifiedName(0, "AuditConditionConfirmEventType"),
            new LocalizedText("", "AuditConditionConfirmEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 8961), new NodeId(0, 46), new NodeId(0, 17224).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 8961), new NodeId(0, 46), new NodeId(0, 11854).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 8961), new NodeId(0, 45), new NodeId(0, 2790).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode125() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 11093),
            new QualifiedName(0, "AuditConditionShelvingEventType"),
            new LocalizedText("", "AuditConditionShelvingEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 11093), new NodeId(0, 46), new NodeId(0, 11855).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11093), new NodeId(0, 45), new NodeId(0, 2790).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode126() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 17225),
            new QualifiedName(0, "AuditConditionSuppressionEventType"),
            new LocalizedText("", "AuditConditionSuppressionEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 17225), new NodeId(0, 45), new NodeId(0, 2790).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode127() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 17242),
            new QualifiedName(0, "AuditConditionSilenceEventType"),
            new LocalizedText("", "AuditConditionSilenceEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 17242), new NodeId(0, 45), new NodeId(0, 2790).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode128() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 15013),
            new QualifiedName(0, "AuditConditionResetEventType"),
            new LocalizedText("", "AuditConditionResetEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 15013), new NodeId(0, 45), new NodeId(0, 2790).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode129() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 17259),
            new QualifiedName(0, "AuditConditionOutOfServiceEventType"),
            new LocalizedText("", "AuditConditionOutOfServiceEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 17259), new NodeId(0, 45), new NodeId(0, 2790).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode130() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 2787),
            new QualifiedName(0, "RefreshStartEventType"),
            new LocalizedText("", "RefreshStartEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 2787), new NodeId(0, 45), new NodeId(0, 2130).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode131() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 2788),
            new QualifiedName(0, "RefreshEndEventType"),
            new LocalizedText("", "RefreshEndEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 2788), new NodeId(0, 45), new NodeId(0, 2130).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode132() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 2789),
            new QualifiedName(0, "RefreshRequiredEventType"),
            new LocalizedText("", "RefreshRequiredEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 2789), new NodeId(0, 45), new NodeId(0, 2130).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode133() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 17279),
            new QualifiedName(0, "AlarmMetricsType"),
            new LocalizedText("", "AlarmMetricsType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 17279), new NodeId(0, 47), new NodeId(0, 17280).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17279), new NodeId(0, 47), new NodeId(0, 17991).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17279), new NodeId(0, 47), new NodeId(0, 17281).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17279), new NodeId(0, 47), new NodeId(0, 17282).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17279), new NodeId(0, 47), new NodeId(0, 17284).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17279), new NodeId(0, 47), new NodeId(0, 17286).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17279), new NodeId(0, 47), new NodeId(0, 17283).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17279), new NodeId(0, 47), new NodeId(0, 17288).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17279), new NodeId(0, 47), new NodeId(0, 18666).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17279), new NodeId(0, 45), new NodeId(0, 58).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode134() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 2391),
            new QualifiedName(0, "ProgramStateMachineType"),
            new LocalizedText("", "ProgramStateMachineType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 2391), new NodeId(0, 47), new NodeId(0, 3830).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2391), new NodeId(0, 47), new NodeId(0, 3835).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2391), new NodeId(0, 46), new NodeId(0, 2392).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2391), new NodeId(0, 46), new NodeId(0, 2393).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2391), new NodeId(0, 46), new NodeId(0, 2394).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2391), new NodeId(0, 46), new NodeId(0, 2395).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2391), new NodeId(0, 46), new NodeId(0, 2396).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2391), new NodeId(0, 46), new NodeId(0, 2397).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2391), new NodeId(0, 46), new NodeId(0, 2398).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2391), new NodeId(0, 47), new NodeId(0, 2399).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2391), new NodeId(0, 47), new NodeId(0, 3850).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2391), new NodeId(0, 47), new NodeId(0, 2406).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2391), new NodeId(0, 47), new NodeId(0, 2400).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2391), new NodeId(0, 47), new NodeId(0, 2402).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2391), new NodeId(0, 47), new NodeId(0, 2404).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2391), new NodeId(0, 47), new NodeId(0, 2408).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2391), new NodeId(0, 47), new NodeId(0, 2410).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2391), new NodeId(0, 47), new NodeId(0, 2412).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2391), new NodeId(0, 47), new NodeId(0, 2414).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2391), new NodeId(0, 47), new NodeId(0, 2416).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2391), new NodeId(0, 47), new NodeId(0, 2418).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2391), new NodeId(0, 47), new NodeId(0, 2420).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2391), new NodeId(0, 47), new NodeId(0, 2422).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2391), new NodeId(0, 47), new NodeId(0, 2424).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2391), new NodeId(0, 47), new NodeId(0, 2426).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2391), new NodeId(0, 47), new NodeId(0, 2427).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2391), new NodeId(0, 47), new NodeId(0, 2428).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2391), new NodeId(0, 47), new NodeId(0, 2429).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2391), new NodeId(0, 47), new NodeId(0, 2430).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2391), new NodeId(0, 45), new NodeId(0, 2771).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode135() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 2378),
            new QualifiedName(0, "ProgramTransitionEventType"),
            new LocalizedText("", "ProgramTransitionEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 2378), new NodeId(0, 47), new NodeId(0, 2379).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2378), new NodeId(0, 45), new NodeId(0, 2311).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode136() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 11856),
            new QualifiedName(0, "AuditProgramTransitionEventType"),
            new LocalizedText("", "AuditProgramTransitionEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 11856), new NodeId(0, 46), new NodeId(0, 11875).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11856), new NodeId(0, 45), new NodeId(0, 2315).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode137() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 3806),
            new QualifiedName(0, "ProgramTransitionAuditEventType"),
            new LocalizedText("", "ProgramTransitionAuditEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 3806), new NodeId(0, 47), new NodeId(0, 3825).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 3806), new NodeId(0, 45), new NodeId(0, 2315).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode138() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 2318),
            new QualifiedName(0, "HistoricalDataConfigurationType"),
            new LocalizedText("", "HistoricalDataConfigurationType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 2318), new NodeId(0, 47), new NodeId(0, 3059).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2318), new NodeId(0, 47), new NodeId(0, 11876).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2318), new NodeId(0, 46), new NodeId(0, 2323).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2318), new NodeId(0, 46), new NodeId(0, 2324).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2318), new NodeId(0, 46), new NodeId(0, 2325).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2318), new NodeId(0, 46), new NodeId(0, 2326).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2318), new NodeId(0, 46), new NodeId(0, 2327).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2318), new NodeId(0, 46), new NodeId(0, 2328).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2318), new NodeId(0, 46), new NodeId(0, 11499).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2318), new NodeId(0, 46), new NodeId(0, 11500).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2318), new NodeId(0, 46), new NodeId(0, 19092).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2318), new NodeId(0, 46), new NodeId(0, 32619).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2318), new NodeId(0, 46), new NodeId(0, 32620).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 2318), new NodeId(0, 45), new NodeId(0, 58).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode139() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 32621),
            new QualifiedName(0, "HistoricalEventConfigurationType"),
            new LocalizedText("", "HistoricalEventConfigurationType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 32621), new NodeId(0, 47), new NodeId(0, 32622).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32621), new NodeId(0, 46), new NodeId(0, 32623).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32621), new NodeId(0, 46), new NodeId(0, 32624).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32621), new NodeId(0, 46), new NodeId(0, 18644).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32621), new NodeId(0, 45), new NodeId(0, 58).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode140() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 32625),
            new QualifiedName(0, "HistoricalExternalEventSourceType"),
            new LocalizedText("", "HistoricalExternalEventSourceType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 32625), new NodeId(0, 46), new NodeId(0, 32626).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32625), new NodeId(0, 46), new NodeId(0, 32627).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32625), new NodeId(0, 46), new NodeId(0, 32628).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32625), new NodeId(0, 46), new NodeId(0, 32629).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32625), new NodeId(0, 46), new NodeId(0, 32630).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32625), new NodeId(0, 46), new NodeId(0, 32631).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32625), new NodeId(0, 46), new NodeId(0, 32632).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32625), new NodeId(0, 45), new NodeId(0, 58).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode141() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 2330),
            new QualifiedName(0, "HistoryServerCapabilitiesType"),
            new LocalizedText("", "HistoryServerCapabilitiesType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 2330), new NodeId(0, 46), new NodeId(0, 2331).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2330), new NodeId(0, 46), new NodeId(0, 2332).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2330), new NodeId(0, 46), new NodeId(0, 11268).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2330), new NodeId(0, 46), new NodeId(0, 11269).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2330), new NodeId(0, 46), new NodeId(0, 2334).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2330), new NodeId(0, 46), new NodeId(0, 2335).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2330), new NodeId(0, 46), new NodeId(0, 2336).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2330), new NodeId(0, 46), new NodeId(0, 2337).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2330), new NodeId(0, 46), new NodeId(0, 2338).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2330), new NodeId(0, 46), new NodeId(0, 11278).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2330), new NodeId(0, 46), new NodeId(0, 11279).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2330), new NodeId(0, 46), new NodeId(0, 11280).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2330), new NodeId(0, 46), new NodeId(0, 11501).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2330), new NodeId(0, 46), new NodeId(0, 11270).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2330), new NodeId(0, 47), new NodeId(0, 11172).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2330), new NodeId(0, 46), new NodeId(0, 19094).expanded(), true));
    node.addReference(
        new Reference(new NodeId(0, 2330), new NodeId(0, 45), new NodeId(0, 58).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode142() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 2999),
            new QualifiedName(0, "AuditHistoryEventUpdateEventType"),
            new LocalizedText("", "AuditHistoryEventUpdateEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 2999), new NodeId(0, 46), new NodeId(0, 3025).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2999), new NodeId(0, 46), new NodeId(0, 3028).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2999), new NodeId(0, 46), new NodeId(0, 3003).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2999), new NodeId(0, 46), new NodeId(0, 3029).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2999), new NodeId(0, 46), new NodeId(0, 3030).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 2999), new NodeId(0, 45), new NodeId(0, 2104).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode143() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 3006),
            new QualifiedName(0, "AuditHistoryValueUpdateEventType"),
            new LocalizedText("", "AuditHistoryValueUpdateEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 3006), new NodeId(0, 46), new NodeId(0, 3026).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 3006), new NodeId(0, 46), new NodeId(0, 3031).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 3006), new NodeId(0, 46), new NodeId(0, 3032).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 3006), new NodeId(0, 46), new NodeId(0, 3033).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 3006), new NodeId(0, 45), new NodeId(0, 2104).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode144() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 19095),
            new QualifiedName(0, "AuditHistoryAnnotationUpdateEventType"),
            new LocalizedText("", "AuditHistoryAnnotationUpdateEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 19095), new NodeId(0, 46), new NodeId(0, 19293).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19095), new NodeId(0, 46), new NodeId(0, 19294).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19095), new NodeId(0, 46), new NodeId(0, 19295).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19095), new NodeId(0, 45), new NodeId(0, 2104).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode145() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 3012),
            new QualifiedName(0, "AuditHistoryDeleteEventType"),
            new LocalizedText("", "AuditHistoryDeleteEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 3012), new NodeId(0, 46), new NodeId(0, 3027).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 3012), new NodeId(0, 45), new NodeId(0, 2104).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode146() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 3014),
            new QualifiedName(0, "AuditHistoryRawModifyDeleteEventType"),
            new LocalizedText("", "AuditHistoryRawModifyDeleteEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 3014), new NodeId(0, 46), new NodeId(0, 3015).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 3014), new NodeId(0, 46), new NodeId(0, 3016).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 3014), new NodeId(0, 46), new NodeId(0, 3017).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 3014), new NodeId(0, 46), new NodeId(0, 3034).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 3014), new NodeId(0, 45), new NodeId(0, 3012).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode147() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 3019),
            new QualifiedName(0, "AuditHistoryAtTimeDeleteEventType"),
            new LocalizedText("", "AuditHistoryAtTimeDeleteEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 3019), new NodeId(0, 46), new NodeId(0, 3020).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 3019), new NodeId(0, 46), new NodeId(0, 3021).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 3019), new NodeId(0, 45), new NodeId(0, 3012).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode148() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 3022),
            new QualifiedName(0, "AuditHistoryEventDeleteEventType"),
            new LocalizedText("", "AuditHistoryEventDeleteEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 3022), new NodeId(0, 46), new NodeId(0, 3023).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 3022), new NodeId(0, 46), new NodeId(0, 3024).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 3022), new NodeId(0, 45), new NodeId(0, 3012).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode149() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 32758),
            new QualifiedName(0, "AuditHistoryConfigurationChangeEventType"),
            new LocalizedText("", "AuditHistoryConfigurationChangeEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 32758), new NodeId(0, 45), new NodeId(0, 2052).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode150() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 32803),
            new QualifiedName(0, "AuditHistoryBulkInsertEventType"),
            new LocalizedText("", "AuditHistoryBulkInsertEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 32803), new NodeId(0, 46), new NodeId(0, 32821).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32803), new NodeId(0, 46), new NodeId(0, 32822).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32803), new NodeId(0, 46), new NodeId(0, 32823).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32803), new NodeId(0, 45), new NodeId(0, 2052).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode151() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 12522),
            new QualifiedName(0, "TrustListType"),
            new LocalizedText("", "TrustListType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 12522), new NodeId(0, 46), new NodeId(0, 12542).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12522), new NodeId(0, 46), new NodeId(0, 19296).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12522), new NodeId(0, 46), new NodeId(0, 32254).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12522), new NodeId(0, 46), new NodeId(0, 23563).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12522), new NodeId(0, 47), new NodeId(0, 12543).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12522), new NodeId(0, 47), new NodeId(0, 12546).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12522), new NodeId(0, 47), new NodeId(0, 12548).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12522), new NodeId(0, 47), new NodeId(0, 12550).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12522), new NodeId(0, 45), new NodeId(0, 11575).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode152() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 19297),
            new QualifiedName(0, "TrustListOutOfDateAlarmType"),
            new LocalizedText("", "TrustListOutOfDateAlarmType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 19297), new NodeId(0, 46), new NodeId(0, 19446).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19297), new NodeId(0, 46), new NodeId(0, 19447).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19297), new NodeId(0, 46), new NodeId(0, 19448).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19297), new NodeId(0, 45), new NodeId(0, 11753).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode153() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 12555),
            new QualifiedName(0, "CertificateGroupType"),
            new LocalizedText("", "CertificateGroupType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 12555), new NodeId(0, 47), new NodeId(0, 13599).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12555), new NodeId(0, 46), new NodeId(0, 13631).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12555), new NodeId(0, 47), new NodeId(0, 23526).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12555), new NodeId(0, 47), new NodeId(0, 19450).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12555), new NodeId(0, 47), new NodeId(0, 20143).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12555), new NodeId(0, 9006), new NodeId(0, 13225).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12555), new NodeId(0, 45), new NodeId(0, 58).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode154() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 13813),
            new QualifiedName(0, "CertificateGroupFolderType"),
            new LocalizedText("", "CertificateGroupFolderType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 13813), new NodeId(0, 47), new NodeId(0, 13814).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13813), new NodeId(0, 47), new NodeId(0, 13848).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13813), new NodeId(0, 47), new NodeId(0, 13882).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13813), new NodeId(0, 35), new NodeId(0, 13916).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 13813), new NodeId(0, 45), new NodeId(0, 61).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode155() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 12556),
            new QualifiedName(0, "CertificateType"),
            new LocalizedText("", "CertificateType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 12556), new NodeId(0, 45), new NodeId(0, 58).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode156() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 12557),
            new QualifiedName(0, "ApplicationCertificateType"),
            new LocalizedText("", "ApplicationCertificateType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 12557), new NodeId(0, 45), new NodeId(0, 12556).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode157() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 12558),
            new QualifiedName(0, "HttpsCertificateType"),
            new LocalizedText("", "HttpsCertificateType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 12558), new NodeId(0, 45), new NodeId(0, 12556).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode158() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 15181),
            new QualifiedName(0, "UserCredentialCertificateType"),
            new LocalizedText("", "UserCredentialCertificateType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 15181), new NodeId(0, 45), new NodeId(0, 12556).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode159() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 12559),
            new QualifiedName(0, "RsaMinApplicationCertificateType"),
            new LocalizedText("", "RsaMinApplicationCertificateType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 12559), new NodeId(0, 45), new NodeId(0, 12557).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode160() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 12560),
            new QualifiedName(0, "RsaSha256ApplicationCertificateType"),
            new LocalizedText("", "RsaSha256ApplicationCertificateType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 12560), new NodeId(0, 45), new NodeId(0, 12557).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode161() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 23537),
            new QualifiedName(0, "EccApplicationCertificateType"),
            new LocalizedText("", "EccApplicationCertificateType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 23537), new NodeId(0, 45), new NodeId(0, 12557).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode162() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 23538),
            new QualifiedName(0, "EccNistP256ApplicationCertificateType"),
            new LocalizedText("", "EccNistP256ApplicationCertificateType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 23538), new NodeId(0, 45), new NodeId(0, 23537).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode163() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 23539),
            new QualifiedName(0, "EccNistP384ApplicationCertificateType"),
            new LocalizedText("", "EccNistP384ApplicationCertificateType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 23539), new NodeId(0, 45), new NodeId(0, 23537).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode164() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 23540),
            new QualifiedName(0, "EccBrainpoolP256r1ApplicationCertificateType"),
            new LocalizedText("", "EccBrainpoolP256r1ApplicationCertificateType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 23540), new NodeId(0, 45), new NodeId(0, 23537).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode165() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 23541),
            new QualifiedName(0, "EccBrainpoolP384r1ApplicationCertificateType"),
            new LocalizedText("", "EccBrainpoolP384r1ApplicationCertificateType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 23541), new NodeId(0, 45), new NodeId(0, 23537).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode166() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 23542),
            new QualifiedName(0, "EccCurve25519ApplicationCertificateType"),
            new LocalizedText("", "EccCurve25519ApplicationCertificateType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 23542), new NodeId(0, 45), new NodeId(0, 23537).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode167() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 23543),
            new QualifiedName(0, "EccCurve448ApplicationCertificateType"),
            new LocalizedText("", "EccCurve448ApplicationCertificateType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 23543), new NodeId(0, 45), new NodeId(0, 23537).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode168() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 32260),
            new QualifiedName(0, "TrustListUpdateRequestedAuditEventType"),
            new LocalizedText("", "TrustListUpdateRequestedAuditEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 32260), new NodeId(0, 45), new NodeId(0, 2127).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode169() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 12561),
            new QualifiedName(0, "TrustListUpdatedAuditEventType"),
            new LocalizedText("", "TrustListUpdatedAuditEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 12561), new NodeId(0, 46), new NodeId(0, 32281).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12561), new NodeId(0, 45), new NodeId(0, 2127).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode170() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 32286),
            new QualifiedName(0, "TransactionDiagnosticsType"),
            new LocalizedText("", "TransactionDiagnosticsType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 32286), new NodeId(0, 46), new NodeId(0, 32287).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32286), new NodeId(0, 46), new NodeId(0, 32288).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32286), new NodeId(0, 46), new NodeId(0, 32289).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32286), new NodeId(0, 46), new NodeId(0, 32290).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32286), new NodeId(0, 46), new NodeId(0, 32291).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32286), new NodeId(0, 46), new NodeId(0, 32292).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 32286), new NodeId(0, 45), new NodeId(0, 58).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode171() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 16662),
            new QualifiedName(0, "ApplicationConfigurationFolderType"),
            new LocalizedText("", "ApplicationConfigurationFolderType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 16662), new NodeId(0, 35), new NodeId(0, 16663).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 16662), new NodeId(0, 45), new NodeId(0, 61).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode172() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 12581),
            new QualifiedName(0, "ServerConfigurationType"),
            new LocalizedText("", "ServerConfigurationType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 12581), new NodeId(0, 47), new NodeId(0, 13950).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12581), new NodeId(0, 46), new NodeId(0, 25696).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12581), new NodeId(0, 46), new NodeId(0, 25724).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12581), new NodeId(0, 46), new NodeId(0, 25697).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12581), new NodeId(0, 46), new NodeId(0, 18660).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12581), new NodeId(0, 46), new NodeId(0, 12708).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12581), new NodeId(0, 46), new NodeId(0, 12583).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12581), new NodeId(0, 46), new NodeId(0, 12584).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12581), new NodeId(0, 46), new NodeId(0, 12585).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12581), new NodeId(0, 46), new NodeId(0, 23593).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12581), new NodeId(0, 46), new NodeId(0, 18661).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12581), new NodeId(0, 46), new NodeId(0, 19308).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12581), new NodeId(0, 47), new NodeId(0, 12616).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12581), new NodeId(0, 47), new NodeId(0, 32296).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12581), new NodeId(0, 47), new NodeId(0, 12734).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12581), new NodeId(0, 47), new NodeId(0, 25698).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12581), new NodeId(0, 47), new NodeId(0, 12731).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12581), new NodeId(0, 47), new NodeId(0, 12775).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12581), new NodeId(0, 47), new NodeId(0, 25699).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12581), new NodeId(0, 47), new NodeId(0, 32299).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12581), new NodeId(0, 45), new NodeId(0, 58).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode173() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 32306),
            new QualifiedName(0, "CertificateUpdateRequestedAuditEventType"),
            new LocalizedText("", "CertificateUpdateRequestedAuditEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 32306), new NodeId(0, 45), new NodeId(0, 2127).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode174() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 12620),
            new QualifiedName(0, "CertificateUpdatedAuditEventType"),
            new LocalizedText("", "CertificateUpdatedAuditEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 12620), new NodeId(0, 46), new NodeId(0, 13735).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12620), new NodeId(0, 46), new NodeId(0, 13736).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 12620), new NodeId(0, 45), new NodeId(0, 2127).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode175() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 17496),
            new QualifiedName(0, "KeyCredentialConfigurationFolderType"),
            new LocalizedText("", "KeyCredentialConfigurationFolderType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 17496), new NodeId(0, 47), new NodeId(0, 17511).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17496), new NodeId(0, 47), new NodeId(0, 17522).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17496), new NodeId(0, 45), new NodeId(0, 61).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode176() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 18001),
            new QualifiedName(0, "KeyCredentialConfigurationType"),
            new LocalizedText("", "KeyCredentialConfigurationType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 18001), new NodeId(0, 46), new NodeId(0, 18069).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18001), new NodeId(0, 46), new NodeId(0, 18165).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18001), new NodeId(0, 46), new NodeId(0, 18004).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18001), new NodeId(0, 46), new NodeId(0, 18657).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18001), new NodeId(0, 46), new NodeId(0, 18005).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18001), new NodeId(0, 47), new NodeId(0, 17534).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18001), new NodeId(0, 47), new NodeId(0, 18006).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18001), new NodeId(0, 47), new NodeId(0, 18008).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18001), new NodeId(0, 45), new NodeId(0, 58).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode177() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 18011),
            new QualifiedName(0, "KeyCredentialAuditEventType"),
            new LocalizedText("", "KeyCredentialAuditEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 18011), new NodeId(0, 46), new NodeId(0, 18028).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18011), new NodeId(0, 45), new NodeId(0, 2127).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode178() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 18029),
            new QualifiedName(0, "KeyCredentialUpdatedAuditEventType"),
            new LocalizedText("", "KeyCredentialUpdatedAuditEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 18029), new NodeId(0, 45), new NodeId(0, 18011).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode179() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 18047),
            new QualifiedName(0, "KeyCredentialDeletedAuditEventType"),
            new LocalizedText("", "KeyCredentialDeletedAuditEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 18047), new NodeId(0, 46), new NodeId(0, 18064).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18047), new NodeId(0, 45), new NodeId(0, 18011).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode180() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 23556),
            new QualifiedName(0, "AuthorizationServicesConfigurationFolderType"),
            new LocalizedText("", "AuthorizationServicesConfigurationFolderType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 23556), new NodeId(0, 47), new NodeId(0, 23557).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23556), new NodeId(0, 45), new NodeId(0, 61).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode181() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 17852),
            new QualifiedName(0, "AuthorizationServiceConfigurationType"),
            new LocalizedText("", "AuthorizationServiceConfigurationType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 17852), new NodeId(0, 46), new NodeId(0, 18072).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17852), new NodeId(0, 46), new NodeId(0, 17860).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17852), new NodeId(0, 46), new NodeId(0, 18073).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17852), new NodeId(0, 45), new NodeId(0, 58).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode182() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 11187),
            new QualifiedName(0, "AggregateConfigurationType"),
            new LocalizedText("", "AggregateConfigurationType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 11187), new NodeId(0, 46), new NodeId(0, 11188).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11187), new NodeId(0, 46), new NodeId(0, 11189).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11187), new NodeId(0, 46), new NodeId(0, 11190).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11187), new NodeId(0, 46), new NodeId(0, 11191).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 11187), new NodeId(0, 45), new NodeId(0, 58).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode183() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 15906),
            new QualifiedName(0, "PubSubKeyServiceType"),
            new LocalizedText("", "PubSubKeyServiceType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 15906), new NodeId(0, 47), new NodeId(0, 15907).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15906), new NodeId(0, 47), new NodeId(0, 15910).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15906), new NodeId(0, 47), new NodeId(0, 15913).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15906), new NodeId(0, 47), new NodeId(0, 25277).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15906), new NodeId(0, 45), new NodeId(0, 58).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode184() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 15452),
            new QualifiedName(0, "SecurityGroupFolderType"),
            new LocalizedText("", "SecurityGroupFolderType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 15452), new NodeId(0, 35), new NodeId(0, 15453).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15452), new NodeId(0, 47), new NodeId(0, 15459).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15452), new NodeId(0, 47), new NodeId(0, 15461).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15452), new NodeId(0, 47), new NodeId(0, 15464).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15452), new NodeId(0, 47), new NodeId(0, 25312).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15452), new NodeId(0, 47), new NodeId(0, 25315).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15452), new NodeId(0, 46), new NodeId(0, 25317).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15452), new NodeId(0, 45), new NodeId(0, 61).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode185() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 15471),
            new QualifiedName(0, "SecurityGroupType"),
            new LocalizedText("", "SecurityGroupType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 15471), new NodeId(0, 46), new NodeId(0, 15472).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15471), new NodeId(0, 46), new NodeId(0, 15046).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15471), new NodeId(0, 46), new NodeId(0, 15047).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15471), new NodeId(0, 46), new NodeId(0, 15048).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15471), new NodeId(0, 46), new NodeId(0, 15056).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15471), new NodeId(0, 47), new NodeId(0, 25624).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15471), new NodeId(0, 47), new NodeId(0, 25625).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15471), new NodeId(0, 45), new NodeId(0, 58).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode186() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 25337),
            new QualifiedName(0, "PubSubKeyPushTargetType"),
            new LocalizedText("", "PubSubKeyPushTargetType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 25337), new NodeId(0, 25345), new NodeId(0, 25626).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25337), new NodeId(0, 46), new NodeId(0, 25634).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25337), new NodeId(0, 46), new NodeId(0, 25635).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25337), new NodeId(0, 46), new NodeId(0, 25340).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25337), new NodeId(0, 46), new NodeId(0, 25636).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25337), new NodeId(0, 46), new NodeId(0, 25637).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25337), new NodeId(0, 46), new NodeId(0, 25638).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25337), new NodeId(0, 46), new NodeId(0, 25639).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25337), new NodeId(0, 46), new NodeId(0, 25640).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25337), new NodeId(0, 47), new NodeId(0, 25641).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25337), new NodeId(0, 47), new NodeId(0, 25644).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25337), new NodeId(0, 47), new NodeId(0, 25647).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25337), new NodeId(0, 45), new NodeId(0, 58).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode187() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 25346),
            new QualifiedName(0, "PubSubKeyPushTargetFolderType"),
            new LocalizedText("", "PubSubKeyPushTargetFolderType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 25346), new NodeId(0, 35), new NodeId(0, 25347).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25346), new NodeId(0, 47), new NodeId(0, 25358).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25346), new NodeId(0, 47), new NodeId(0, 25366).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25346), new NodeId(0, 47), new NodeId(0, 25369).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25346), new NodeId(0, 47), new NodeId(0, 25371).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25346), new NodeId(0, 47), new NodeId(0, 25374).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25346), new NodeId(0, 45), new NodeId(0, 61).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode188() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 14416),
            new QualifiedName(0, "PublishSubscribeType"),
            new LocalizedText("", "PublishSubscribeType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 14416), new NodeId(0, 14476), new NodeId(0, 14417).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14416), new NodeId(0, 47), new NodeId(0, 17296).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14416), new NodeId(0, 47), new NodeId(0, 16598).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14416), new NodeId(0, 47), new NodeId(0, 14432).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14416), new NodeId(0, 47), new NodeId(0, 14434).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14416), new NodeId(0, 47), new NodeId(0, 23622).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14416), new NodeId(0, 47), new NodeId(0, 25403).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14416), new NodeId(0, 47), new NodeId(0, 15844).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14416), new NodeId(0, 47), new NodeId(0, 18715).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14416), new NodeId(0, 47), new NodeId(0, 23642).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14416), new NodeId(0, 47), new NodeId(0, 23649).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14416), new NodeId(0, 46), new NodeId(0, 17479).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14416), new NodeId(0, 46), new NodeId(0, 25432).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14416), new NodeId(0, 46), new NodeId(0, 25433).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14416), new NodeId(0, 46), new NodeId(0, 32396).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14416), new NodeId(0, 46), new NodeId(0, 32397).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14416), new NodeId(0, 45), new NodeId(0, 15906).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode189() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 25482),
            new QualifiedName(0, "PubSubConfigurationType"),
            new LocalizedText("", "PubSubConfigurationType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 25482), new NodeId(0, 47), new NodeId(0, 25505).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25482), new NodeId(0, 47), new NodeId(0, 25508).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25482), new NodeId(0, 45), new NodeId(0, 11575).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode190() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 14509),
            new QualifiedName(0, "PublishedDataSetType"),
            new LocalizedText("", "PublishedDataSetType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 14509), new NodeId(0, 14936), new NodeId(0, 15222).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14509), new NodeId(0, 46), new NodeId(0, 14519).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14509), new NodeId(0, 46), new NodeId(0, 15229).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14509), new NodeId(0, 46), new NodeId(0, 16759).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14509), new NodeId(0, 46), new NodeId(0, 25521).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14509), new NodeId(0, 47), new NodeId(0, 15481).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14509), new NodeId(0, 45), new NodeId(0, 58).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode191() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 15489),
            new QualifiedName(0, "ExtensionFieldsType"),
            new LocalizedText("", "ExtensionFieldsType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 15489), new NodeId(0, 46), new NodeId(0, 15490).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15489), new NodeId(0, 47), new NodeId(0, 15491).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15489), new NodeId(0, 47), new NodeId(0, 15494).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15489), new NodeId(0, 45), new NodeId(0, 58).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode192() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 14534),
            new QualifiedName(0, "PublishedDataItemsType"),
            new LocalizedText("", "PublishedDataItemsType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 14534), new NodeId(0, 46), new NodeId(0, 14548).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14534), new NodeId(0, 47), new NodeId(0, 14555).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14534), new NodeId(0, 47), new NodeId(0, 14558).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14534), new NodeId(0, 45), new NodeId(0, 14509).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode193() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 14572),
            new QualifiedName(0, "PublishedEventsType"),
            new LocalizedText("", "PublishedEventsType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 14572), new NodeId(0, 46), new NodeId(0, 14586).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14572), new NodeId(0, 46), new NodeId(0, 14587).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14572), new NodeId(0, 46), new NodeId(0, 14588).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14572), new NodeId(0, 47), new NodeId(0, 15052).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14572), new NodeId(0, 45), new NodeId(0, 14509).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode194() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 14477),
            new QualifiedName(0, "DataSetFolderType"),
            new LocalizedText("", "DataSetFolderType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 14477), new NodeId(0, 35), new NodeId(0, 14478).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14477), new NodeId(0, 47), new NodeId(0, 14487).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14477), new NodeId(0, 47), new NodeId(0, 14493).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14477), new NodeId(0, 47), new NodeId(0, 14496).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14477), new NodeId(0, 47), new NodeId(0, 16935).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14477), new NodeId(0, 47), new NodeId(0, 16960).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14477), new NodeId(0, 47), new NodeId(0, 14499).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14477), new NodeId(0, 47), new NodeId(0, 16994).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14477), new NodeId(0, 47), new NodeId(0, 16997).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14477), new NodeId(0, 45), new NodeId(0, 61).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode195() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 14209),
            new QualifiedName(0, "PubSubConnectionType"),
            new LocalizedText("", "PubSubConnectionType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 14209), new NodeId(0, 46), new NodeId(0, 14595).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14209), new NodeId(0, 47), new NodeId(0, 17306).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14209), new NodeId(0, 46), new NodeId(0, 17485).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14209), new NodeId(0, 47), new NodeId(0, 14221).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14209), new NodeId(0, 47), new NodeId(0, 17203).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14209), new NodeId(0, 18804), new NodeId(0, 17310).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14209), new NodeId(0, 18805), new NodeId(0, 17325).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14209), new NodeId(0, 47), new NodeId(0, 14600).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14209), new NodeId(0, 47), new NodeId(0, 19241).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14209), new NodeId(0, 47), new NodeId(0, 17427).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14209), new NodeId(0, 47), new NodeId(0, 17465).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14209), new NodeId(0, 47), new NodeId(0, 14225).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14209), new NodeId(0, 45), new NodeId(0, 58).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode196() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 17721),
            new QualifiedName(0, "ConnectionTransportType"),
            new LocalizedText("", "ConnectionTransportType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 17721), new NodeId(0, 45), new NodeId(0, 58).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode197() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 14232),
            new QualifiedName(0, "PubSubGroupType"),
            new LocalizedText("", "PubSubGroupType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 14232), new NodeId(0, 46), new NodeId(0, 15926).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14232), new NodeId(0, 46), new NodeId(0, 15927).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14232), new NodeId(0, 46), new NodeId(0, 15928).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14232), new NodeId(0, 46), new NodeId(0, 17724).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14232), new NodeId(0, 46), new NodeId(0, 17488).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14232), new NodeId(0, 47), new NodeId(0, 15265).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14232), new NodeId(0, 45), new NodeId(0, 58).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode198() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 17725),
            new QualifiedName(0, "WriterGroupType"),
            new LocalizedText("", "WriterGroupType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 17725), new NodeId(0, 46), new NodeId(0, 17736).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17725), new NodeId(0, 46), new NodeId(0, 17737).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17725), new NodeId(0, 46), new NodeId(0, 17738).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17725), new NodeId(0, 46), new NodeId(0, 17739).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17725), new NodeId(0, 46), new NodeId(0, 17740).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17725), new NodeId(0, 46), new NodeId(0, 17559).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17725), new NodeId(0, 47), new NodeId(0, 17741).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17725), new NodeId(0, 47), new NodeId(0, 17742).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17725), new NodeId(0, 15296), new NodeId(0, 17743).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17725), new NodeId(0, 47), new NodeId(0, 17812).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17725), new NodeId(0, 47), new NodeId(0, 17969).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17725), new NodeId(0, 47), new NodeId(0, 17992).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17725), new NodeId(0, 45), new NodeId(0, 14232).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode199() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 17997),
            new QualifiedName(0, "WriterGroupTransportType"),
            new LocalizedText("", "WriterGroupTransportType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 17997), new NodeId(0, 45), new NodeId(0, 58).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode200() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 17998),
            new QualifiedName(0, "WriterGroupMessageType"),
            new LocalizedText("", "WriterGroupMessageType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 17998), new NodeId(0, 45), new NodeId(0, 58).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode201() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 17999),
            new QualifiedName(0, "ReaderGroupType"),
            new LocalizedText("", "ReaderGroupType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 17999), new NodeId(0, 15297), new NodeId(0, 18076).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17999), new NodeId(0, 47), new NodeId(0, 21015).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17999), new NodeId(0, 47), new NodeId(0, 21080).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17999), new NodeId(0, 47), new NodeId(0, 21081).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17999), new NodeId(0, 47), new NodeId(0, 21082).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17999), new NodeId(0, 47), new NodeId(0, 21085).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 17999), new NodeId(0, 45), new NodeId(0, 14232).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode202() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 21090),
            new QualifiedName(0, "ReaderGroupTransportType"),
            new LocalizedText("", "ReaderGroupTransportType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 21090), new NodeId(0, 45), new NodeId(0, 58).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode203() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 21091),
            new QualifiedName(0, "ReaderGroupMessageType"),
            new LocalizedText("", "ReaderGroupMessageType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 21091), new NodeId(0, 45), new NodeId(0, 58).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode204() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 15298),
            new QualifiedName(0, "DataSetWriterType"),
            new LocalizedText("", "DataSetWriterType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 15298), new NodeId(0, 46), new NodeId(0, 21092).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15298), new NodeId(0, 46), new NodeId(0, 21093).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15298), new NodeId(0, 46), new NodeId(0, 21094).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15298), new NodeId(0, 46), new NodeId(0, 17493).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15298), new NodeId(0, 47), new NodeId(0, 15303).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15298), new NodeId(0, 47), new NodeId(0, 21095).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15298), new NodeId(0, 47), new NodeId(0, 15299).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15298), new NodeId(0, 47), new NodeId(0, 19550).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15298), new NodeId(0, 45), new NodeId(0, 58).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode205() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 15305),
            new QualifiedName(0, "DataSetWriterTransportType"),
            new LocalizedText("", "DataSetWriterTransportType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 15305), new NodeId(0, 45), new NodeId(0, 58).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode206() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 21096),
            new QualifiedName(0, "DataSetWriterMessageType"),
            new LocalizedText("", "DataSetWriterMessageType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 21096), new NodeId(0, 45), new NodeId(0, 58).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode207() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 15306),
            new QualifiedName(0, "DataSetReaderType"),
            new LocalizedText("", "DataSetReaderType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 15306), new NodeId(0, 46), new NodeId(0, 21097).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15306), new NodeId(0, 46), new NodeId(0, 21098).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15306), new NodeId(0, 46), new NodeId(0, 21099).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15306), new NodeId(0, 46), new NodeId(0, 21100).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15306), new NodeId(0, 46), new NodeId(0, 21101).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15306), new NodeId(0, 46), new NodeId(0, 21102).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15306), new NodeId(0, 46), new NodeId(0, 17563).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15306), new NodeId(0, 46), new NodeId(0, 17564).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15306), new NodeId(0, 46), new NodeId(0, 15932).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15306), new NodeId(0, 46), new NodeId(0, 15933).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15306), new NodeId(0, 46), new NodeId(0, 15934).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15306), new NodeId(0, 46), new NodeId(0, 17494).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15306), new NodeId(0, 47), new NodeId(0, 15311).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15306), new NodeId(0, 47), new NodeId(0, 21103).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15306), new NodeId(0, 47), new NodeId(0, 15307).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15306), new NodeId(0, 47), new NodeId(0, 19609).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15306), new NodeId(0, 47), new NodeId(0, 15316).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15306), new NodeId(0, 47), new NodeId(0, 17386).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15306), new NodeId(0, 47), new NodeId(0, 17389).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15306), new NodeId(0, 45), new NodeId(0, 58).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode208() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 15319),
            new QualifiedName(0, "DataSetReaderTransportType"),
            new LocalizedText("", "DataSetReaderTransportType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 15319), new NodeId(0, 45), new NodeId(0, 58).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode209() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 21104),
            new QualifiedName(0, "DataSetReaderMessageType"),
            new LocalizedText("", "DataSetReaderMessageType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 21104), new NodeId(0, 45), new NodeId(0, 58).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode210() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 15108),
            new QualifiedName(0, "SubscribedDataSetType"),
            new LocalizedText("", "SubscribedDataSetType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 15108), new NodeId(0, 45), new NodeId(0, 58).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode211() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 15111),
            new QualifiedName(0, "TargetVariablesType"),
            new LocalizedText("", "TargetVariablesType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 15111), new NodeId(0, 46), new NodeId(0, 15114).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15111), new NodeId(0, 47), new NodeId(0, 15115).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15111), new NodeId(0, 47), new NodeId(0, 15118).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15111), new NodeId(0, 45), new NodeId(0, 15108).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode212() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 15127),
            new QualifiedName(0, "SubscribedDataSetMirrorType"),
            new LocalizedText("", "SubscribedDataSetMirrorType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 15127), new NodeId(0, 45), new NodeId(0, 15108).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode213() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 23795),
            new QualifiedName(0, "SubscribedDataSetFolderType"),
            new LocalizedText("", "SubscribedDataSetFolderType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 23795), new NodeId(0, 35), new NodeId(0, 23796).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23795), new NodeId(0, 47), new NodeId(0, 23807).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23795), new NodeId(0, 47), new NodeId(0, 23811).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23795), new NodeId(0, 47), new NodeId(0, 23814).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23795), new NodeId(0, 47), new NodeId(0, 23816).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23795), new NodeId(0, 47), new NodeId(0, 23819).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23795), new NodeId(0, 45), new NodeId(0, 61).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode214() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 23828),
            new QualifiedName(0, "StandaloneSubscribedDataSetType"),
            new LocalizedText("", "StandaloneSubscribedDataSetType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 23828), new NodeId(0, 47), new NodeId(0, 23829).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23828), new NodeId(0, 46), new NodeId(0, 23830).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23828), new NodeId(0, 46), new NodeId(0, 23831).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23828), new NodeId(0, 45), new NodeId(0, 58).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode215() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 14643),
            new QualifiedName(0, "PubSubStatusType"),
            new LocalizedText("", "PubSubStatusType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 14643), new NodeId(0, 47), new NodeId(0, 14644).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14643), new NodeId(0, 47), new NodeId(0, 14645).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14643), new NodeId(0, 47), new NodeId(0, 14646).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 14643), new NodeId(0, 45), new NodeId(0, 58).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode216() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 19677),
            new QualifiedName(0, "PubSubDiagnosticsType"),
            new LocalizedText("", "PubSubDiagnosticsType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 19677), new NodeId(0, 47), new NodeId(0, 19678).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19677), new NodeId(0, 47), new NodeId(0, 19679).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19677), new NodeId(0, 47), new NodeId(0, 19684).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19677), new NodeId(0, 47), new NodeId(0, 19689).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19677), new NodeId(0, 47), new NodeId(0, 19690).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19677), new NodeId(0, 47), new NodeId(0, 19691).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19677), new NodeId(0, 47), new NodeId(0, 19722).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19677), new NodeId(0, 45), new NodeId(0, 58).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode217() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 19732),
            new QualifiedName(0, "PubSubDiagnosticsRootType"),
            new LocalizedText("", "PubSubDiagnosticsRootType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 19732), new NodeId(0, 47), new NodeId(0, 19777).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19732), new NodeId(0, 45), new NodeId(0, 19677).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode218() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 19786),
            new QualifiedName(0, "PubSubDiagnosticsConnectionType"),
            new LocalizedText("", "PubSubDiagnosticsConnectionType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 19786), new NodeId(0, 47), new NodeId(0, 19831).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19786), new NodeId(0, 45), new NodeId(0, 19677).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode219() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 19834),
            new QualifiedName(0, "PubSubDiagnosticsWriterGroupType"),
            new LocalizedText("", "PubSubDiagnosticsWriterGroupType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 19834), new NodeId(0, 47), new NodeId(0, 19848).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19834), new NodeId(0, 47), new NodeId(0, 19879).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19834), new NodeId(0, 45), new NodeId(0, 19677).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode220() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 19903),
            new QualifiedName(0, "PubSubDiagnosticsReaderGroupType"),
            new LocalizedText("", "PubSubDiagnosticsReaderGroupType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 19903), new NodeId(0, 47), new NodeId(0, 19917).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19903), new NodeId(0, 47), new NodeId(0, 19948).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19903), new NodeId(0, 45), new NodeId(0, 19677).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode221() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 19968),
            new QualifiedName(0, "PubSubDiagnosticsDataSetWriterType"),
            new LocalizedText("", "PubSubDiagnosticsDataSetWriterType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 19968), new NodeId(0, 47), new NodeId(0, 19982).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19968), new NodeId(0, 47), new NodeId(0, 20013).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19968), new NodeId(0, 45), new NodeId(0, 19677).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode222() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 20027),
            new QualifiedName(0, "PubSubDiagnosticsDataSetReaderType"),
            new LocalizedText("", "PubSubDiagnosticsDataSetReaderType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 20027), new NodeId(0, 47), new NodeId(0, 20041).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 20027), new NodeId(0, 47), new NodeId(0, 20072).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 20027), new NodeId(0, 45), new NodeId(0, 19677).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode223() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 23832),
            new QualifiedName(0, "PubSubCapabilitiesType"),
            new LocalizedText("", "PubSubCapabilitiesType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 23832), new NodeId(0, 46), new NodeId(0, 23833).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23832), new NodeId(0, 46), new NodeId(0, 23834).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23832), new NodeId(0, 46), new NodeId(0, 23835).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23832), new NodeId(0, 46), new NodeId(0, 23836).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23832), new NodeId(0, 46), new NodeId(0, 23837).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23832), new NodeId(0, 46), new NodeId(0, 23838).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23832), new NodeId(0, 46), new NodeId(0, 32651).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23832), new NodeId(0, 46), new NodeId(0, 32844).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23832), new NodeId(0, 46), new NodeId(0, 32845).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23832), new NodeId(0, 46), new NodeId(0, 32846).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23832), new NodeId(0, 46), new NodeId(0, 32847).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23832), new NodeId(0, 46), new NodeId(0, 32652).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23832), new NodeId(0, 46), new NodeId(0, 32653).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23832), new NodeId(0, 46), new NodeId(0, 32654).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23832), new NodeId(0, 46), new NodeId(0, 32655).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23832), new NodeId(0, 46), new NodeId(0, 32848).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23832), new NodeId(0, 45), new NodeId(0, 58).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode224() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 15535),
            new QualifiedName(0, "PubSubStatusEventType"),
            new LocalizedText("", "PubSubStatusEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 15535), new NodeId(0, 46), new NodeId(0, 15545).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15535), new NodeId(0, 46), new NodeId(0, 15546).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15535), new NodeId(0, 46), new NodeId(0, 15547).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15535), new NodeId(0, 45), new NodeId(0, 2130).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode225() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 15548),
            new QualifiedName(0, "PubSubTransportLimitsExceedEventType"),
            new LocalizedText("", "PubSubTransportLimitsExceedEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 15548), new NodeId(0, 46), new NodeId(0, 15561).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15548), new NodeId(0, 46), new NodeId(0, 15562).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15548), new NodeId(0, 45), new NodeId(0, 15535).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode226() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 15563),
            new QualifiedName(0, "PubSubCommunicationFailureEventType"),
            new LocalizedText("", "PubSubCommunicationFailureEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 15563), new NodeId(0, 46), new NodeId(0, 15576).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15563), new NodeId(0, 45), new NodeId(0, 15535).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode227() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 21105),
            new QualifiedName(0, "UadpWriterGroupMessageType"),
            new LocalizedText("", "UadpWriterGroupMessageType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 21105), new NodeId(0, 46), new NodeId(0, 21106).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 21105), new NodeId(0, 46), new NodeId(0, 21107).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 21105), new NodeId(0, 46), new NodeId(0, 21108).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 21105), new NodeId(0, 46), new NodeId(0, 21109).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 21105), new NodeId(0, 46), new NodeId(0, 21110).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 21105), new NodeId(0, 45), new NodeId(0, 17998).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode228() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 21111),
            new QualifiedName(0, "UadpDataSetWriterMessageType"),
            new LocalizedText("", "UadpDataSetWriterMessageType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 21111), new NodeId(0, 46), new NodeId(0, 21112).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 21111), new NodeId(0, 46), new NodeId(0, 21113).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 21111), new NodeId(0, 46), new NodeId(0, 21114).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 21111), new NodeId(0, 46), new NodeId(0, 21115).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 21111), new NodeId(0, 45), new NodeId(0, 21096).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode229() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 21116),
            new QualifiedName(0, "UadpDataSetReaderMessageType"),
            new LocalizedText("", "UadpDataSetReaderMessageType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 21116), new NodeId(0, 46), new NodeId(0, 21117).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 21116), new NodeId(0, 46), new NodeId(0, 21119).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 21116), new NodeId(0, 46), new NodeId(0, 17477).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 21116), new NodeId(0, 46), new NodeId(0, 21120).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 21116), new NodeId(0, 46), new NodeId(0, 21121).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 21116), new NodeId(0, 46), new NodeId(0, 21122).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 21116), new NodeId(0, 46), new NodeId(0, 21123).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 21116), new NodeId(0, 46), new NodeId(0, 21124).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 21116), new NodeId(0, 46), new NodeId(0, 21125).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 21116), new NodeId(0, 45), new NodeId(0, 21104).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode230() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 21126),
            new QualifiedName(0, "JsonWriterGroupMessageType"),
            new LocalizedText("", "JsonWriterGroupMessageType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 21126), new NodeId(0, 46), new NodeId(0, 21127).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 21126), new NodeId(0, 45), new NodeId(0, 17998).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode231() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 21128),
            new QualifiedName(0, "JsonDataSetWriterMessageType"),
            new LocalizedText("", "JsonDataSetWriterMessageType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 21128), new NodeId(0, 46), new NodeId(0, 21129).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 21128), new NodeId(0, 45), new NodeId(0, 21096).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode232() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 21130),
            new QualifiedName(0, "JsonDataSetReaderMessageType"),
            new LocalizedText("", "JsonDataSetReaderMessageType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 21130), new NodeId(0, 46), new NodeId(0, 21131).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 21130), new NodeId(0, 46), new NodeId(0, 21132).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 21130), new NodeId(0, 45), new NodeId(0, 21104).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode233() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 15064),
            new QualifiedName(0, "DatagramConnectionTransportType"),
            new LocalizedText("", "DatagramConnectionTransportType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 15064), new NodeId(0, 47), new NodeId(0, 15072).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15064), new NodeId(0, 46), new NodeId(0, 23839).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15064), new NodeId(0, 46), new NodeId(0, 23840).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15064), new NodeId(0, 46), new NodeId(0, 25525).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15064), new NodeId(0, 46), new NodeId(0, 25526).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15064), new NodeId(0, 45), new NodeId(0, 17721).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode234() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 21133),
            new QualifiedName(0, "DatagramWriterGroupTransportType"),
            new LocalizedText("", "DatagramWriterGroupTransportType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 21133), new NodeId(0, 46), new NodeId(0, 21134).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 21133), new NodeId(0, 46), new NodeId(0, 21135).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 21133), new NodeId(0, 47), new NodeId(0, 23842).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 21133), new NodeId(0, 46), new NodeId(0, 25527).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 21133), new NodeId(0, 46), new NodeId(0, 23847).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 21133), new NodeId(0, 46), new NodeId(0, 23848).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 21133), new NodeId(0, 46), new NodeId(0, 23849).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 21133), new NodeId(0, 45), new NodeId(0, 17997).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode235() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 24016),
            new QualifiedName(0, "DatagramDataSetReaderTransportType"),
            new LocalizedText("", "DatagramDataSetReaderTransportType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 24016), new NodeId(0, 47), new NodeId(0, 24017).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24016), new NodeId(0, 46), new NodeId(0, 25528).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24016), new NodeId(0, 46), new NodeId(0, 24022).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24016), new NodeId(0, 46), new NodeId(0, 24023).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24016), new NodeId(0, 45), new NodeId(0, 15319).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode236() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 15155),
            new QualifiedName(0, "BrokerConnectionTransportType"),
            new LocalizedText("", "BrokerConnectionTransportType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 15155), new NodeId(0, 46), new NodeId(0, 15156).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15155), new NodeId(0, 46), new NodeId(0, 15178).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 15155), new NodeId(0, 45), new NodeId(0, 17721).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode237() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 21136),
            new QualifiedName(0, "BrokerWriterGroupTransportType"),
            new LocalizedText("", "BrokerWriterGroupTransportType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 21136), new NodeId(0, 46), new NodeId(0, 21137).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 21136), new NodeId(0, 46), new NodeId(0, 15246).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 21136), new NodeId(0, 46), new NodeId(0, 15247).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 21136), new NodeId(0, 46), new NodeId(0, 15249).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 21136), new NodeId(0, 45), new NodeId(0, 17997).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode238() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 21138),
            new QualifiedName(0, "BrokerDataSetWriterTransportType"),
            new LocalizedText("", "BrokerDataSetWriterTransportType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 21138), new NodeId(0, 46), new NodeId(0, 21139).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 21138), new NodeId(0, 46), new NodeId(0, 21140).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 21138), new NodeId(0, 46), new NodeId(0, 15250).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 21138), new NodeId(0, 46), new NodeId(0, 15251).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 21138), new NodeId(0, 46), new NodeId(0, 15330).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 21138), new NodeId(0, 46), new NodeId(0, 21141).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 21138), new NodeId(0, 45), new NodeId(0, 15305).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode239() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 21142),
            new QualifiedName(0, "BrokerDataSetReaderTransportType"),
            new LocalizedText("", "BrokerDataSetReaderTransportType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 21142), new NodeId(0, 46), new NodeId(0, 21143).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 21142), new NodeId(0, 46), new NodeId(0, 15334).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 21142), new NodeId(0, 46), new NodeId(0, 15419).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 21142), new NodeId(0, 46), new NodeId(0, 15420).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 21142), new NodeId(0, 46), new NodeId(0, 21144).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 21142), new NodeId(0, 45), new NodeId(0, 15319).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode240() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 21145),
            new QualifiedName(0, "NetworkAddressType"),
            new LocalizedText("", "NetworkAddressType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 21145), new NodeId(0, 47), new NodeId(0, 21146).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 21145), new NodeId(0, 45), new NodeId(0, 58).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode241() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 21147),
            new QualifiedName(0, "NetworkAddressUrlType"),
            new LocalizedText("", "NetworkAddressUrlType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 21147), new NodeId(0, 47), new NodeId(0, 21149).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 21147), new NodeId(0, 45), new NodeId(0, 21145).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode242() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 23455),
            new QualifiedName(0, "AliasNameType"),
            new LocalizedText("", "AliasNameType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 23455), new NodeId(0, 45), new NodeId(0, 58).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode243() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 23456),
            new QualifiedName(0, "AliasNameCategoryType"),
            new LocalizedText("", "AliasNameCategoryType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 23456), new NodeId(0, 35), new NodeId(0, 23457).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23456), new NodeId(0, 35), new NodeId(0, 23458).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23456), new NodeId(0, 47), new NodeId(0, 23462).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23456), new NodeId(0, 46), new NodeId(0, 32850).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 23456), new NodeId(0, 45), new NodeId(0, 61).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode244() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 24264),
            new QualifiedName(0, "UserManagementType"),
            new LocalizedText("", "UserManagementType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 24264), new NodeId(0, 46), new NodeId(0, 24265).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24264), new NodeId(0, 46), new NodeId(0, 24266).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24264), new NodeId(0, 46), new NodeId(0, 24267).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24264), new NodeId(0, 46), new NodeId(0, 24268).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24264), new NodeId(0, 47), new NodeId(0, 24269).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24264), new NodeId(0, 47), new NodeId(0, 24271).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24264), new NodeId(0, 47), new NodeId(0, 24273).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24264), new NodeId(0, 47), new NodeId(0, 24275).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24264), new NodeId(0, 45), new NodeId(0, 58).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode245() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 25731),
            new QualifiedName(0, "ApplicationConfigurationType"),
            new LocalizedText("", "ApplicationConfigurationType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 25731), new NodeId(0, 46), new NodeId(0, 26850).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25731), new NodeId(0, 46), new NodeId(0, 26851).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25731), new NodeId(0, 46), new NodeId(0, 26852).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25731), new NodeId(0, 46), new NodeId(0, 26849).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25731), new NodeId(0, 45), new NodeId(0, 12581).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode246() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 26871),
            new QualifiedName(0, "ProvisionableDeviceType"),
            new LocalizedText("", "ProvisionableDeviceType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 26871), new NodeId(0, 46), new NodeId(0, 26872).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 26871), new NodeId(0, 47), new NodeId(0, 26873).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 26871), new NodeId(0, 47), new NodeId(0, 26875).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 26871), new NodeId(0, 47), new NodeId(0, 26878).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 26871), new NodeId(0, 45), new NodeId(0, 58).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode247() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 24148),
            new QualifiedName(0, "IIetfBaseNetworkInterfaceType"),
            new LocalizedText("", "IIetfBaseNetworkInterfaceType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 24148), new NodeId(0, 47), new NodeId(0, 24149).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24148), new NodeId(0, 47), new NodeId(0, 24150).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24148), new NodeId(0, 47), new NodeId(0, 24151).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24148), new NodeId(0, 47), new NodeId(0, 24152).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24148), new NodeId(0, 45), new NodeId(0, 17602).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode248() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 24158),
            new QualifiedName(0, "IIeeeBaseEthernetPortType"),
            new LocalizedText("", "IIeeeBaseEthernetPortType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 24158), new NodeId(0, 47), new NodeId(0, 24159).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24158), new NodeId(0, 47), new NodeId(0, 24165).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24158), new NodeId(0, 47), new NodeId(0, 24166).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24158), new NodeId(0, 45), new NodeId(0, 17602).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode249() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 24233),
            new QualifiedName(0, "IIeeeAutoNegotiationStatusType"),
            new LocalizedText("", "IIeeeAutoNegotiationStatusType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 24233), new NodeId(0, 47), new NodeId(0, 24234).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24233), new NodeId(0, 45), new NodeId(0, 17602).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode250() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 24167),
            new QualifiedName(0, "IBaseEthernetCapabilitiesType"),
            new LocalizedText("", "IBaseEthernetCapabilitiesType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 24167), new NodeId(0, 47), new NodeId(0, 24168).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24167), new NodeId(0, 45), new NodeId(0, 17602).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode251() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 25218),
            new QualifiedName(0, "IVlanIdType"),
            new LocalizedText("", "IVlanIdType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 25218), new NodeId(0, 47), new NodeId(0, 25219).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25218), new NodeId(0, 45), new NodeId(0, 17602).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode252() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 24169),
            new QualifiedName(0, "ISrClassType"),
            new LocalizedText("", "ISrClassType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 24169), new NodeId(0, 47), new NodeId(0, 24170).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24169), new NodeId(0, 47), new NodeId(0, 24171).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24169), new NodeId(0, 47), new NodeId(0, 24172).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24169), new NodeId(0, 45), new NodeId(0, 17602).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode253() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 24173),
            new QualifiedName(0, "IIeeeBaseTsnStreamType"),
            new LocalizedText("", "IIeeeBaseTsnStreamType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 24173), new NodeId(0, 47), new NodeId(0, 24174).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24173), new NodeId(0, 47), new NodeId(0, 24175).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24173), new NodeId(0, 47), new NodeId(0, 24176).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24173), new NodeId(0, 47), new NodeId(0, 24177).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24173), new NodeId(0, 47), new NodeId(0, 24178).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24173), new NodeId(0, 45), new NodeId(0, 17602).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode254() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 24179),
            new QualifiedName(0, "IIeeeBaseTsnTrafficSpecificationType"),
            new LocalizedText("", "IIeeeBaseTsnTrafficSpecificationType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 24179), new NodeId(0, 47), new NodeId(0, 24180).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24179), new NodeId(0, 47), new NodeId(0, 24181).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24179), new NodeId(0, 47), new NodeId(0, 24182).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24179), new NodeId(0, 45), new NodeId(0, 17602).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode255() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 24183),
            new QualifiedName(0, "IIeeeBaseTsnStatusStreamType"),
            new LocalizedText("", "IIeeeBaseTsnStatusStreamType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 24183), new NodeId(0, 47), new NodeId(0, 24184).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24183), new NodeId(0, 47), new NodeId(0, 24185).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24183), new NodeId(0, 47), new NodeId(0, 24186).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24183), new NodeId(0, 47), new NodeId(0, 24187).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24183), new NodeId(0, 45), new NodeId(0, 17602).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode256() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 24188),
            new QualifiedName(0, "IIeeeTsnInterfaceConfigurationType"),
            new LocalizedText("", "IIeeeTsnInterfaceConfigurationType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 24188), new NodeId(0, 47), new NodeId(0, 24189).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24188), new NodeId(0, 47), new NodeId(0, 24190).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24188), new NodeId(0, 45), new NodeId(0, 17602).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode257() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 24191),
            new QualifiedName(0, "IIeeeTsnInterfaceConfigurationTalkerType"),
            new LocalizedText("", "IIeeeTsnInterfaceConfigurationTalkerType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 24191), new NodeId(0, 47), new NodeId(0, 24194).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24191), new NodeId(0, 45), new NodeId(0, 24188).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode258() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 24195),
            new QualifiedName(0, "IIeeeTsnInterfaceConfigurationListenerType"),
            new LocalizedText("", "IIeeeTsnInterfaceConfigurationListenerType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 24195), new NodeId(0, 47), new NodeId(0, 24198).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24195), new NodeId(0, 45), new NodeId(0, 24188).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode259() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 24199),
            new QualifiedName(0, "IIeeeTsnMacAddressType"),
            new LocalizedText("", "IIeeeTsnMacAddressType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 24199), new NodeId(0, 47), new NodeId(0, 24200).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24199), new NodeId(0, 47), new NodeId(0, 24201).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24199), new NodeId(0, 45), new NodeId(0, 17602).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode260() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 24202),
            new QualifiedName(0, "IIeeeTsnVlanTagType"),
            new LocalizedText("", "IIeeeTsnVlanTagType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 24202), new NodeId(0, 47), new NodeId(0, 24203).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24202), new NodeId(0, 47), new NodeId(0, 24204).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24202), new NodeId(0, 45), new NodeId(0, 17602).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode261() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 24205),
            new QualifiedName(0, "IPriorityMappingEntryType"),
            new LocalizedText("", "IPriorityMappingEntryType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            new NodeId(0, 24205), new NodeId(0, 47), new NodeId(0, 24206).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24205), new NodeId(0, 47), new NodeId(0, 24207).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24205), new NodeId(0, 47), new NodeId(0, 24208).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24205), new NodeId(0, 47), new NodeId(0, 24209).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 24205), new NodeId(0, 45), new NodeId(0, 17602).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode262() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 25221),
            new QualifiedName(0, "IetfBaseNetworkInterfaceType"),
            new LocalizedText("", "IetfBaseNetworkInterfaceType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 25221), new NodeId(0, 47), new NodeId(0, 25222).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25221), new NodeId(0, 47), new NodeId(0, 25223).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25221), new NodeId(0, 47), new NodeId(0, 25224).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25221), new NodeId(0, 47), new NodeId(0, 25225).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25221), new NodeId(0, 25238), new NodeId(0, 25226).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25221), new NodeId(0, 17603), new NodeId(0, 24148).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25221), new NodeId(0, 45), new NodeId(0, 58).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode263() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 25227),
            new QualifiedName(0, "PriorityMappingTableType"),
            new LocalizedText("", "PriorityMappingTableType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 25227), new NodeId(0, 46), new NodeId(0, 25228).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25227), new NodeId(0, 47), new NodeId(0, 25229).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25227), new NodeId(0, 47), new NodeId(0, 25231).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 25227), new NodeId(0, 45), new NodeId(0, 58).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode264() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 18973),
            new QualifiedName(0, "LldpInformationType"),
            new LocalizedText("", "LldpInformationType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 18973), new NodeId(0, 47), new NodeId(0, 18974).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18973), new NodeId(0, 47), new NodeId(0, 18980).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18973), new NodeId(0, 47), new NodeId(0, 18987).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18973), new NodeId(0, 45), new NodeId(0, 58).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode265() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 18996),
            new QualifiedName(0, "LldpRemoteStatisticsType"),
            new LocalizedText("", "LldpRemoteStatisticsType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 18996), new NodeId(0, 47), new NodeId(0, 18997).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18996), new NodeId(0, 47), new NodeId(0, 18998).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18996), new NodeId(0, 47), new NodeId(0, 18999).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18996), new NodeId(0, 47), new NodeId(0, 19000).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18996), new NodeId(0, 47), new NodeId(0, 19001).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 18996), new NodeId(0, 45), new NodeId(0, 58).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode266() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 19002),
            new QualifiedName(0, "LldpLocalSystemType"),
            new LocalizedText("", "LldpLocalSystemType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 19002), new NodeId(0, 46), new NodeId(0, 19003).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19002), new NodeId(0, 46), new NodeId(0, 19004).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19002), new NodeId(0, 46), new NodeId(0, 19005).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19002), new NodeId(0, 46), new NodeId(0, 19006).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19002), new NodeId(0, 46), new NodeId(0, 19007).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19002), new NodeId(0, 46), new NodeId(0, 19008).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19002), new NodeId(0, 45), new NodeId(0, 58).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode267() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 19009),
            new QualifiedName(0, "LldpPortInformationType"),
            new LocalizedText("", "LldpPortInformationType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 19009), new NodeId(0, 46), new NodeId(0, 19010).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19009), new NodeId(0, 46), new NodeId(0, 19011).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19009), new NodeId(0, 46), new NodeId(0, 19012).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19009), new NodeId(0, 46), new NodeId(0, 19013).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19009), new NodeId(0, 46), new NodeId(0, 19014).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19009), new NodeId(0, 46), new NodeId(0, 19015).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19009), new NodeId(0, 47), new NodeId(0, 19016).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19009), new NodeId(0, 45), new NodeId(0, 58).expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode268() {
    var node =
        new UaObjectTypeNode(
            this.context,
            new NodeId(0, 19033),
            new QualifiedName(0, "LldpRemoteSystemType"),
            new LocalizedText("", "LldpRemoteSystemType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            new NodeId(0, 19033), new NodeId(0, 47), new NodeId(0, 19034).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19033), new NodeId(0, 47), new NodeId(0, 19035).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19033), new NodeId(0, 47), new NodeId(0, 19036).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19033), new NodeId(0, 47), new NodeId(0, 19037).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19033), new NodeId(0, 47), new NodeId(0, 19038).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19033), new NodeId(0, 47), new NodeId(0, 19039).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19033), new NodeId(0, 47), new NodeId(0, 19040).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19033), new NodeId(0, 47), new NodeId(0, 19041).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19033), new NodeId(0, 47), new NodeId(0, 19042).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19033), new NodeId(0, 47), new NodeId(0, 19043).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19033), new NodeId(0, 47), new NodeId(0, 19044).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19033), new NodeId(0, 47), new NodeId(0, 19045).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19033), new NodeId(0, 47), new NodeId(0, 19046).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19033), new NodeId(0, 47), new NodeId(0, 19047).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19033), new NodeId(0, 47), new NodeId(0, 19078).expanded(), true));
    node.addReference(
        new Reference(
            new NodeId(0, 19033), new NodeId(0, 45), new NodeId(0, 58).expanded(), false));
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
  }
}

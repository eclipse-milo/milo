/*
 * Copyright (c) 2026 the Eclipse Milo Authors
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
import org.eclipse.milo.opcua.stack.core.types.structured.PermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

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
            NodeId.parse("i=58"),
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
            NodeId.parse("i=61"),
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
        new Reference(
            NodeId.parse("i=61"), NodeId.parse("i=45"), NodeId.parse("i=58").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode2() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=75"),
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
        new Reference(
            NodeId.parse("i=75"), NodeId.parse("i=45"), NodeId.parse("i=58").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode3() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=76"),
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
        new Reference(
            NodeId.parse("i=76"), NodeId.parse("i=45"), NodeId.parse("i=58").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode4() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=77"),
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
        new Reference(
            NodeId.parse("i=77"), NodeId.parse("i=45"), NodeId.parse("i=58").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode5() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=2004"),
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
            NodeId.parse("i=2004"), NodeId.parse("i=46"), NodeId.parse("i=2005").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2004"), NodeId.parse("i=46"), NodeId.parse("i=2006").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2004"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15003").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2004"), NodeId.parse("i=47"), NodeId.parse("i=2007").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2004"), NodeId.parse("i=46"), NodeId.parse("i=2008").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2004"), NodeId.parse("i=46"), NodeId.parse("i=2742").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2004"),
            NodeId.parse("i=46"),
            NodeId.parse("i=12882").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2004"),
            NodeId.parse("i=46"),
            NodeId.parse("i=17612").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2004"), NodeId.parse("i=47"), NodeId.parse("i=2009").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2004"), NodeId.parse("i=47"), NodeId.parse("i=2010").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2004"), NodeId.parse("i=47"), NodeId.parse("i=2011").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2004"), NodeId.parse("i=47"), NodeId.parse("i=2012").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2004"),
            NodeId.parse("i=47"),
            NodeId.parse("i=11527").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2004"),
            NodeId.parse("i=47"),
            NodeId.parse("i=11489").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2004"),
            NodeId.parse("i=47"),
            NodeId.parse("i=12871").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2004"),
            NodeId.parse("i=47"),
            NodeId.parse("i=12746").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2004"),
            NodeId.parse("i=47"),
            NodeId.parse("i=12883").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2004"), NodeId.parse("i=45"), NodeId.parse("i=58").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode6() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=2013"),
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
            NodeId.parse("i=2013"), NodeId.parse("i=46"), NodeId.parse("i=2014").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2013"), NodeId.parse("i=46"), NodeId.parse("i=2016").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2013"), NodeId.parse("i=46"), NodeId.parse("i=2017").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2013"), NodeId.parse("i=46"), NodeId.parse("i=2732").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2013"), NodeId.parse("i=46"), NodeId.parse("i=2733").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2013"), NodeId.parse("i=46"), NodeId.parse("i=2734").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2013"),
            NodeId.parse("i=46"),
            NodeId.parse("i=19809").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2013"), NodeId.parse("i=46"), NodeId.parse("i=3049").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2013"),
            NodeId.parse("i=46"),
            NodeId.parse("i=11549").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2013"),
            NodeId.parse("i=46"),
            NodeId.parse("i=11550").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2013"),
            NodeId.parse("i=46"),
            NodeId.parse("i=12910").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2013"),
            NodeId.parse("i=47"),
            NodeId.parse("i=11551").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2013"), NodeId.parse("i=47"), NodeId.parse("i=2019").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2013"), NodeId.parse("i=47"), NodeId.parse("i=2754").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2013"),
            NodeId.parse("i=47"),
            NodeId.parse("i=11562").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2013"),
            NodeId.parse("i=47"),
            NodeId.parse("i=16295").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2013"),
            NodeId.parse("i=46"),
            NodeId.parse("i=24088").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2013"),
            NodeId.parse("i=46"),
            NodeId.parse("i=24089").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2013"),
            NodeId.parse("i=46"),
            NodeId.parse("i=24090").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2013"),
            NodeId.parse("i=46"),
            NodeId.parse("i=24091").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2013"),
            NodeId.parse("i=46"),
            NodeId.parse("i=24103").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2013"),
            NodeId.parse("i=46"),
            NodeId.parse("i=24092").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2013"),
            NodeId.parse("i=46"),
            NodeId.parse("i=24093").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2013"),
            NodeId.parse("i=46"),
            NodeId.parse("i=31770").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2013"),
            NodeId.parse("i=46"),
            NodeId.parse("i=24094").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2013"), NodeId.parse("i=45"), NodeId.parse("i=58").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode7() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=2020"),
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
            NodeId.parse("i=2020"), NodeId.parse("i=47"), NodeId.parse("i=2021").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2020"), NodeId.parse("i=47"), NodeId.parse("i=2022").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2020"), NodeId.parse("i=47"), NodeId.parse("i=2023").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2020"), NodeId.parse("i=47"), NodeId.parse("i=2744").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2020"), NodeId.parse("i=46"), NodeId.parse("i=2025").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2020"), NodeId.parse("i=45"), NodeId.parse("i=58").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode8() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=2026"),
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
            NodeId.parse("i=2026"), NodeId.parse("i=47"), NodeId.parse("i=2027").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2026"), NodeId.parse("i=47"), NodeId.parse("i=2028").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2026"),
            NodeId.parse("i=47"),
            NodeId.parse("i=12097").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2026"), NodeId.parse("i=45"), NodeId.parse("i=58").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode9() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=2029"),
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
            NodeId.parse("i=2029"), NodeId.parse("i=47"), NodeId.parse("i=2030").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2029"), NodeId.parse("i=47"), NodeId.parse("i=2031").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2029"), NodeId.parse("i=47"), NodeId.parse("i=2032").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2029"),
            NodeId.parse("i=46"),
            NodeId.parse("i=19303").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2029"), NodeId.parse("i=45"), NodeId.parse("i=58").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode10() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=2033"),
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
        new Reference(
            NodeId.parse("i=2033"), NodeId.parse("i=45"), NodeId.parse("i=58").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode11() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=2034"),
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
            NodeId.parse("i=2034"), NodeId.parse("i=46"), NodeId.parse("i=2035").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2034"),
            NodeId.parse("i=46"),
            NodeId.parse("i=32410").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2034"), NodeId.parse("i=45"), NodeId.parse("i=58").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode12() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=2036"),
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
            NodeId.parse("i=2036"), NodeId.parse("i=46"), NodeId.parse("i=2038").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2036"), NodeId.parse("i=46"), NodeId.parse("i=2037").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2036"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2034").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode13() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=2039"),
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
            NodeId.parse("i=2039"), NodeId.parse("i=46"), NodeId.parse("i=2040").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2039"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2034").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode14() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=11945"),
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
            NodeId.parse("i=11945"),
            NodeId.parse("i=46"),
            NodeId.parse("i=11948").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11945"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2039").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode15() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=32411"),
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
            NodeId.parse("i=32411"),
            NodeId.parse("i=46"),
            NodeId.parse("i=32413").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=32411"),
            NodeId.parse("i=46"),
            NodeId.parse("i=32415").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=32411"),
            NodeId.parse("i=47"),
            NodeId.parse("i=32416").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=32411"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2039").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode16() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=11564"),
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
            NodeId.parse("i=11564"),
            NodeId.parse("i=46"),
            NodeId.parse("i=11565").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11564"),
            NodeId.parse("i=46"),
            NodeId.parse("i=12161").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11564"),
            NodeId.parse("i=46"),
            NodeId.parse("i=12162").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11564"),
            NodeId.parse("i=46"),
            NodeId.parse("i=11567").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11564"),
            NodeId.parse("i=46"),
            NodeId.parse("i=12163").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11564"),
            NodeId.parse("i=46"),
            NodeId.parse("i=12164").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11564"),
            NodeId.parse("i=46"),
            NodeId.parse("i=11569").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11564"),
            NodeId.parse("i=46"),
            NodeId.parse("i=11570").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11564"),
            NodeId.parse("i=46"),
            NodeId.parse("i=11571").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11564"),
            NodeId.parse("i=46"),
            NodeId.parse("i=11572").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11564"),
            NodeId.parse("i=46"),
            NodeId.parse("i=11573").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11564"),
            NodeId.parse("i=46"),
            NodeId.parse("i=11574").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11564"), NodeId.parse("i=45"), NodeId.parse("i=61").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode17() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=11575"),
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
            NodeId.parse("i=11575"),
            NodeId.parse("i=46"),
            NodeId.parse("i=11576").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11575"),
            NodeId.parse("i=46"),
            NodeId.parse("i=12686").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11575"),
            NodeId.parse("i=46"),
            NodeId.parse("i=12687").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11575"),
            NodeId.parse("i=46"),
            NodeId.parse("i=11579").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11575"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13341").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11575"),
            NodeId.parse("i=46"),
            NodeId.parse("i=24244").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11575"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25200").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11575"),
            NodeId.parse("i=47"),
            NodeId.parse("i=11580").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11575"),
            NodeId.parse("i=47"),
            NodeId.parse("i=11583").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11575"),
            NodeId.parse("i=47"),
            NodeId.parse("i=11585").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11575"),
            NodeId.parse("i=47"),
            NodeId.parse("i=11588").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11575"),
            NodeId.parse("i=47"),
            NodeId.parse("i=11590").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11575"),
            NodeId.parse("i=47"),
            NodeId.parse("i=11593").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11575"), NodeId.parse("i=45"), NodeId.parse("i=58").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode18() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=11595"),
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
            NodeId.parse("i=11595"),
            NodeId.parse("i=47"),
            NodeId.parse("i=11615").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11595"),
            NodeId.parse("i=45"),
            NodeId.parse("i=11575").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode19() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=11616"),
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
            NodeId.parse("i=11616"),
            NodeId.parse("i=46"),
            NodeId.parse("i=11617").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11616"),
            NodeId.parse("i=46"),
            NodeId.parse("i=11618").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11616"),
            NodeId.parse("i=46"),
            NodeId.parse("i=11619").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11616"),
            NodeId.parse("i=46"),
            NodeId.parse("i=11620").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11616"),
            NodeId.parse("i=46"),
            NodeId.parse("i=11621").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11616"),
            NodeId.parse("i=46"),
            NodeId.parse("i=11622").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11616"),
            NodeId.parse("i=46"),
            NodeId.parse("i=11623").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11616"),
            NodeId.parse("i=47"),
            NodeId.parse("i=11624").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11616"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16137").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11616"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16138").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11616"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16139").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11616"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25267").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11616"),
            NodeId.parse("i=46"),
            NodeId.parse("i=32419").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11616"), NodeId.parse("i=45"), NodeId.parse("i=58").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode20() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=11645"),
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
            NodeId.parse("i=11645"),
            NodeId.parse("i=47"),
            NodeId.parse("i=11646").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11645"), NodeId.parse("i=45"), NodeId.parse("i=58").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode21() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=2041"),
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
            NodeId.parse("i=2041"), NodeId.parse("i=46"), NodeId.parse("i=2042").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2041"), NodeId.parse("i=46"), NodeId.parse("i=2043").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2041"), NodeId.parse("i=46"), NodeId.parse("i=2044").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2041"), NodeId.parse("i=46"), NodeId.parse("i=2045").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2041"), NodeId.parse("i=46"), NodeId.parse("i=2046").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2041"), NodeId.parse("i=46"), NodeId.parse("i=2047").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2041"), NodeId.parse("i=46"), NodeId.parse("i=3190").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2041"), NodeId.parse("i=46"), NodeId.parse("i=2050").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2041"), NodeId.parse("i=46"), NodeId.parse("i=2051").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2041"),
            NodeId.parse("i=46"),
            NodeId.parse("i=31771").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2041"),
            NodeId.parse("i=46"),
            NodeId.parse("i=31772").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2041"),
            NodeId.parse("i=46"),
            NodeId.parse("i=31773").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2041"),
            NodeId.parse("i=46"),
            NodeId.parse("i=31774").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2041"), NodeId.parse("i=45"), NodeId.parse("i=58").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode22() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=2052"),
            new QualifiedName(0, "AuditEventType"),
            new LocalizedText("", "AuditEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15644"), new PermissionType(UInteger.valueOf(33))),
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(65535)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=2052"), NodeId.parse("i=46"), NodeId.parse("i=2053").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2052"), NodeId.parse("i=46"), NodeId.parse("i=2054").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2052"), NodeId.parse("i=46"), NodeId.parse("i=2055").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2052"), NodeId.parse("i=46"), NodeId.parse("i=2056").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2052"), NodeId.parse("i=46"), NodeId.parse("i=2057").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2052"),
            NodeId.parse("i=46"),
            NodeId.parse("i=19811").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2052"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2041").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode23() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=2058"),
            new QualifiedName(0, "AuditSecurityEventType"),
            new LocalizedText("", "AuditSecurityEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15644"), new PermissionType(UInteger.valueOf(33))),
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(65535)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=2058"),
            NodeId.parse("i=46"),
            NodeId.parse("i=17615").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2058"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2052").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode24() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=2059"),
            new QualifiedName(0, "AuditChannelEventType"),
            new LocalizedText("", "AuditChannelEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15644"), new PermissionType(UInteger.valueOf(33))),
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(65535)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=2059"), NodeId.parse("i=46"), NodeId.parse("i=2745").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2059"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2058").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode25() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=2060"),
            new QualifiedName(0, "AuditOpenSecureChannelEventType"),
            new LocalizedText("", "AuditOpenSecureChannelEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15644"), new PermissionType(UInteger.valueOf(33))),
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(65535)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=2060"), NodeId.parse("i=46"), NodeId.parse("i=2061").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2060"), NodeId.parse("i=46"), NodeId.parse("i=2746").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2060"), NodeId.parse("i=46"), NodeId.parse("i=2062").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2060"), NodeId.parse("i=46"), NodeId.parse("i=2063").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2060"), NodeId.parse("i=46"), NodeId.parse("i=2065").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2060"), NodeId.parse("i=46"), NodeId.parse("i=2066").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2060"),
            NodeId.parse("i=46"),
            NodeId.parse("i=24135").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2060"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2059").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode26() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=2069"),
            new QualifiedName(0, "AuditSessionEventType"),
            new LocalizedText("", "AuditSessionEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15644"), new PermissionType(UInteger.valueOf(33))),
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(65535)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=2069"), NodeId.parse("i=46"), NodeId.parse("i=2070").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2069"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2058").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode27() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=2071"),
            new QualifiedName(0, "AuditCreateSessionEventType"),
            new LocalizedText("", "AuditCreateSessionEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15644"), new PermissionType(UInteger.valueOf(33))),
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(65535)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=2071"), NodeId.parse("i=46"), NodeId.parse("i=2072").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2071"), NodeId.parse("i=46"), NodeId.parse("i=2073").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2071"), NodeId.parse("i=46"), NodeId.parse("i=2747").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2071"), NodeId.parse("i=46"), NodeId.parse("i=2074").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2071"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2069").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode28() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=2748"),
            new QualifiedName(0, "AuditUrlMismatchEventType"),
            new LocalizedText("", "AuditUrlMismatchEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15644"), new PermissionType(UInteger.valueOf(33))),
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(65535)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=2748"), NodeId.parse("i=46"), NodeId.parse("i=2749").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2748"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2071").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode29() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=2075"),
            new QualifiedName(0, "AuditActivateSessionEventType"),
            new LocalizedText("", "AuditActivateSessionEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15644"), new PermissionType(UInteger.valueOf(33))),
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(65535)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=2075"), NodeId.parse("i=46"), NodeId.parse("i=2076").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2075"), NodeId.parse("i=46"), NodeId.parse("i=2077").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2075"),
            NodeId.parse("i=46"),
            NodeId.parse("i=11485").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2075"),
            NodeId.parse("i=46"),
            NodeId.parse("i=19304").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2075"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2069").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode30() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=2078"),
            new QualifiedName(0, "AuditCancelEventType"),
            new LocalizedText("", "AuditCancelEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15644"), new PermissionType(UInteger.valueOf(33))),
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(65535)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=2078"), NodeId.parse("i=46"), NodeId.parse("i=2079").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2078"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2069").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode31() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=2080"),
            new QualifiedName(0, "AuditCertificateEventType"),
            new LocalizedText("", "AuditCertificateEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15644"), new PermissionType(UInteger.valueOf(33))),
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(65535)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=2080"), NodeId.parse("i=46"), NodeId.parse("i=2081").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2080"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2058").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode32() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=2082"),
            new QualifiedName(0, "AuditCertificateDataMismatchEventType"),
            new LocalizedText("", "AuditCertificateDataMismatchEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15644"), new PermissionType(UInteger.valueOf(33))),
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(65535)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=2082"), NodeId.parse("i=46"), NodeId.parse("i=2083").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2082"), NodeId.parse("i=46"), NodeId.parse("i=2084").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2082"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2080").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode33() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=2085"),
            new QualifiedName(0, "AuditCertificateExpiredEventType"),
            new LocalizedText("", "AuditCertificateExpiredEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15644"), new PermissionType(UInteger.valueOf(33))),
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(65535)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=2085"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2080").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode34() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=2086"),
            new QualifiedName(0, "AuditCertificateInvalidEventType"),
            new LocalizedText("", "AuditCertificateInvalidEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15644"), new PermissionType(UInteger.valueOf(33))),
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(65535)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=2086"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2080").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode35() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=2087"),
            new QualifiedName(0, "AuditCertificateUntrustedEventType"),
            new LocalizedText("", "AuditCertificateUntrustedEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15644"), new PermissionType(UInteger.valueOf(33))),
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(65535)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=2087"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2080").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode36() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=2088"),
            new QualifiedName(0, "AuditCertificateRevokedEventType"),
            new LocalizedText("", "AuditCertificateRevokedEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15644"), new PermissionType(UInteger.valueOf(33))),
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(65535)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=2088"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2080").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode37() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=2089"),
            new QualifiedName(0, "AuditCertificateMismatchEventType"),
            new LocalizedText("", "AuditCertificateMismatchEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15644"), new PermissionType(UInteger.valueOf(33))),
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(65535)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=2089"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2080").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode38() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=2090"),
            new QualifiedName(0, "AuditNodeManagementEventType"),
            new LocalizedText("", "AuditNodeManagementEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15644"), new PermissionType(UInteger.valueOf(33))),
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(65535)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=2090"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2052").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode39() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=2091"),
            new QualifiedName(0, "AuditAddNodesEventType"),
            new LocalizedText("", "AuditAddNodesEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15644"), new PermissionType(UInteger.valueOf(33))),
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(65535)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=2091"), NodeId.parse("i=46"), NodeId.parse("i=2092").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2091"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2090").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode40() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=2093"),
            new QualifiedName(0, "AuditDeleteNodesEventType"),
            new LocalizedText("", "AuditDeleteNodesEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15644"), new PermissionType(UInteger.valueOf(33))),
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(65535)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=2093"), NodeId.parse("i=46"), NodeId.parse("i=2094").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2093"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2090").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode41() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=2095"),
            new QualifiedName(0, "AuditAddReferencesEventType"),
            new LocalizedText("", "AuditAddReferencesEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15644"), new PermissionType(UInteger.valueOf(33))),
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(65535)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=2095"), NodeId.parse("i=46"), NodeId.parse("i=2096").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2095"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2090").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode42() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=2097"),
            new QualifiedName(0, "AuditDeleteReferencesEventType"),
            new LocalizedText("", "AuditDeleteReferencesEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15644"), new PermissionType(UInteger.valueOf(33))),
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(65535)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=2097"), NodeId.parse("i=46"), NodeId.parse("i=2098").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2097"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2090").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode43() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=2099"),
            new QualifiedName(0, "AuditUpdateEventType"),
            new LocalizedText("", "AuditUpdateEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15644"), new PermissionType(UInteger.valueOf(33))),
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(65535)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=2099"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2052").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode44() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=2100"),
            new QualifiedName(0, "AuditWriteUpdateEventType"),
            new LocalizedText("", "AuditWriteUpdateEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15644"), new PermissionType(UInteger.valueOf(33))),
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(65535)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=2100"), NodeId.parse("i=46"), NodeId.parse("i=2750").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2100"), NodeId.parse("i=46"), NodeId.parse("i=2101").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2100"), NodeId.parse("i=46"), NodeId.parse("i=2102").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2100"), NodeId.parse("i=46"), NodeId.parse("i=2103").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2100"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2099").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode45() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=2104"),
            new QualifiedName(0, "AuditHistoryUpdateEventType"),
            new LocalizedText("", "AuditHistoryUpdateEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15644"), new PermissionType(UInteger.valueOf(33))),
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(65535)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=2104"), NodeId.parse("i=46"), NodeId.parse("i=2751").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2104"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2099").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode46() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=2127"),
            new QualifiedName(0, "AuditUpdateMethodEventType"),
            new LocalizedText("", "AuditUpdateMethodEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15644"), new PermissionType(UInteger.valueOf(33))),
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(65535)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=2127"), NodeId.parse("i=46"), NodeId.parse("i=2128").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2127"),
            NodeId.parse("i=46"),
            NodeId.parse("i=19305").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2127"), NodeId.parse("i=46"), NodeId.parse("i=2129").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2127"),
            NodeId.parse("i=46"),
            NodeId.parse("i=19306").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2127"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2052").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode47() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=2130"),
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
            NodeId.parse("i=2130"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2041").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode48() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=2131"),
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
            NodeId.parse("i=2131"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2130").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode49() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=11446"),
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
            NodeId.parse("i=11446"),
            NodeId.parse("i=46"),
            NodeId.parse("i=11696").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11446"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2130").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode50() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=2132"),
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
            NodeId.parse("i=2132"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2041").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode51() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=2133"),
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
            NodeId.parse("i=2133"), NodeId.parse("i=46"), NodeId.parse("i=2134").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2133"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2132").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode52() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=2738"),
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
            NodeId.parse("i=2738"), NodeId.parse("i=46"), NodeId.parse("i=2739").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2738"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2041").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode53() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=3035"),
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
            NodeId.parse("i=3035"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2041").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode54() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=11436"),
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
            NodeId.parse("i=11436"),
            NodeId.parse("i=46"),
            NodeId.parse("i=12502").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11436"),
            NodeId.parse("i=46"),
            NodeId.parse("i=12503").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11436"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2041").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode55() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=23606"),
            new QualifiedName(0, "AuditClientEventType"),
            new LocalizedText("", "AuditClientEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15644"), new PermissionType(UInteger.valueOf(33))),
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(65535)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=23606"),
            NodeId.parse("i=46"),
            NodeId.parse("i=23908").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23606"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2052").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode56() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=23926"),
            new QualifiedName(0, "AuditClientUpdateMethodResultEventType"),
            new LocalizedText("", "AuditClientUpdateMethodResultEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15644"), new PermissionType(UInteger.valueOf(33))),
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(65535)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=23926"),
            NodeId.parse("i=46"),
            NodeId.parse("i=23994").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23926"),
            NodeId.parse("i=46"),
            NodeId.parse("i=23995").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23926"),
            NodeId.parse("i=46"),
            NodeId.parse("i=23998").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23926"),
            NodeId.parse("i=46"),
            NodeId.parse("i=23999").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23926"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25684").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23926"),
            NodeId.parse("i=45"),
            NodeId.parse("i=23606").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode57() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=2340"),
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
        new Reference(
            NodeId.parse("i=2340"), NodeId.parse("i=45"), NodeId.parse("i=58").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode58() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=19820"),
            new QualifiedName(0, "DataTypeRefinementType"),
            new LocalizedText("", "DataTypeRefinementType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            NodeId.parse("i=19820"),
            NodeId.parse("i=19815"),
            NodeId.parse("i=19821").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19820"), NodeId.parse("i=45"), NodeId.parse("i=58").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode59() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=19822"),
            new QualifiedName(0, "SubtypeRestrictionType"),
            new LocalizedText("", "SubtypeRestrictionType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            NodeId.parse("i=19822"),
            NodeId.parse("i=19819"),
            NodeId.parse("i=19823").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19822"), NodeId.parse("i=45"), NodeId.parse("i=58").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode60() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=2299"),
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
            NodeId.parse("i=2299"), NodeId.parse("i=47"), NodeId.parse("i=2769").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2299"), NodeId.parse("i=47"), NodeId.parse("i=2770").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2299"), NodeId.parse("i=45"), NodeId.parse("i=58").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode61() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=2771"),
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
            NodeId.parse("i=2771"), NodeId.parse("i=47"), NodeId.parse("i=2772").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2771"), NodeId.parse("i=47"), NodeId.parse("i=2773").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2771"),
            NodeId.parse("i=47"),
            NodeId.parse("i=17635").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2771"),
            NodeId.parse("i=47"),
            NodeId.parse("i=17636").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2771"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2299").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode62() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=2307"),
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
            NodeId.parse("i=2307"), NodeId.parse("i=46"), NodeId.parse("i=2308").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2307"), NodeId.parse("i=45"), NodeId.parse("i=58").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode63() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=2309"),
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
            NodeId.parse("i=2309"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2307").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode64() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=2310"),
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
            NodeId.parse("i=2310"), NodeId.parse("i=46"), NodeId.parse("i=2312").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2310"), NodeId.parse("i=45"), NodeId.parse("i=58").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode65() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=15109"),
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
            NodeId.parse("i=15109"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2307").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode66() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=2311"),
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
            NodeId.parse("i=2311"), NodeId.parse("i=47"), NodeId.parse("i=2774").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2311"), NodeId.parse("i=47"), NodeId.parse("i=2775").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2311"), NodeId.parse("i=47"), NodeId.parse("i=2776").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2311"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2041").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode67() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=2315"),
            new QualifiedName(0, "AuditUpdateStateEventType"),
            new LocalizedText("", "AuditUpdateStateEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15644"), new PermissionType(UInteger.valueOf(33))),
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(65535)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=2315"), NodeId.parse("i=46"), NodeId.parse("i=2777").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2315"), NodeId.parse("i=46"), NodeId.parse("i=2778").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2315"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2127").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode68() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=13353"),
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
            NodeId.parse("i=13353"),
            NodeId.parse("i=35"),
            NodeId.parse("i=13354").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13353"),
            NodeId.parse("i=35"),
            NodeId.parse("i=13366").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13353"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13387").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13353"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13390").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13353"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13393").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13353"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13395").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13353"), NodeId.parse("i=45"), NodeId.parse("i=61").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode69() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=15744"),
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
            NodeId.parse("i=15744"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15745").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15744"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15746").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15744"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15749").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15744"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15751").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15744"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15754").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15744"), NodeId.parse("i=45"), NodeId.parse("i=58").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode70() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=15803"),
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
            NodeId.parse("i=15803"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15815").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15803"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15817").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15803"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15819").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15803"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15821").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15803"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15823").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15803"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15825").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15803"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15827").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15803"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15829").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15803"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15831").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15803"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15833").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15803"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15835").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15803"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15837").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15803"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15839").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15803"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15841").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15803"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15843").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15803"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2771").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode71() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=15607"),
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
            NodeId.parse("i=15607"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15608").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15607"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15997").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15607"),
            NodeId.parse("i=47"),
            NodeId.parse("i=16000").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15607"), NodeId.parse("i=45"), NodeId.parse("i=58").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode72() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=15620"),
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
            NodeId.parse("i=15620"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16173").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15620"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15410").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15620"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16174").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15620"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15411").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15620"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16175").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15620"),
            NodeId.parse("i=46"),
            NodeId.parse("i=24139").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15620"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15624").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15620"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15626").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15620"),
            NodeId.parse("i=47"),
            NodeId.parse("i=16176").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15620"),
            NodeId.parse("i=47"),
            NodeId.parse("i=16178").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15620"),
            NodeId.parse("i=47"),
            NodeId.parse("i=16180").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15620"),
            NodeId.parse("i=47"),
            NodeId.parse("i=16182").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15620"), NodeId.parse("i=45"), NodeId.parse("i=58").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode73() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=17641"),
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
            NodeId.parse("i=17641"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2127").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode74() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=17589"),
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
            NodeId.parse("i=17589"),
            NodeId.parse("i=47"),
            NodeId.parse("i=17590").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17589"), NodeId.parse("i=45"), NodeId.parse("i=58").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode75() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=17591"),
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
            NodeId.parse("i=17591"),
            NodeId.parse("i=47"),
            NodeId.parse("i=17592").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17591"),
            NodeId.parse("i=47"),
            NodeId.parse("i=17593").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17591"), NodeId.parse("i=45"), NodeId.parse("i=61").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode76() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=17598"),
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
            NodeId.parse("i=17598"),
            NodeId.parse("i=45"),
            NodeId.parse("i=17589").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode77() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=17600"),
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
            NodeId.parse("i=17600"),
            NodeId.parse("i=45"),
            NodeId.parse("i=17589").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode78() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=17602"),
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
            NodeId.parse("i=17602"), NodeId.parse("i=45"), NodeId.parse("i=58").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode79() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=23513"),
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
            NodeId.parse("i=23513"),
            NodeId.parse("i=46"),
            NodeId.parse("i=23517").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23513"),
            NodeId.parse("i=45"),
            NodeId.parse("i=17602").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode80() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=23518"),
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
            NodeId.parse("i=23518"),
            NodeId.parse("i=49"),
            NodeId.parse("i=23519").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23518"),
            NodeId.parse("i=46"),
            NodeId.parse("i=23525").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23518"),
            NodeId.parse("i=41"),
            NodeId.parse("i=2133").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23518"), NodeId.parse("i=45"), NodeId.parse("i=58").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode81() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=32439"),
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
            NodeId.parse("i=32439"),
            NodeId.parse("i=46"),
            NodeId.parse("i=32441").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=32439"),
            NodeId.parse("i=45"),
            NodeId.parse("i=17589").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode82() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=32442"),
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
            NodeId.parse("i=32442"),
            NodeId.parse("i=46"),
            NodeId.parse("i=32443").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=32442"),
            NodeId.parse("i=46"),
            NodeId.parse("i=32445").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=32442"),
            NodeId.parse("i=46"),
            NodeId.parse("i=32446").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=32442"), NodeId.parse("i=45"), NodeId.parse("i=58").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode83() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=32447"),
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
            NodeId.parse("i=32447"),
            NodeId.parse("i=47"),
            NodeId.parse("i=32452").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=32447"),
            NodeId.parse("i=46"),
            NodeId.parse("i=32461").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=32447"),
            NodeId.parse("i=47"),
            NodeId.parse("i=32462").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=32447"),
            NodeId.parse("i=45"),
            NodeId.parse("i=32442").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode84() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=32467"),
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
            NodeId.parse("i=32467"),
            NodeId.parse("i=46"),
            NodeId.parse("i=32472").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=32467"),
            NodeId.parse("i=46"),
            NodeId.parse("i=32473").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=32467"),
            NodeId.parse("i=46"),
            NodeId.parse("i=32474").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=32467"),
            NodeId.parse("i=45"),
            NodeId.parse("i=32442").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode85() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=32475"),
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
            NodeId.parse("i=32475"),
            NodeId.parse("i=46"),
            NodeId.parse("i=32476").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=32475"),
            NodeId.parse("i=46"),
            NodeId.parse("i=32478").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=32475"),
            NodeId.parse("i=46"),
            NodeId.parse("i=32479").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=32475"),
            NodeId.parse("i=46"),
            NodeId.parse("i=32480").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=32475"),
            NodeId.parse("i=47"),
            NodeId.parse("i=32481").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=32475"), NodeId.parse("i=45"), NodeId.parse("i=58").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode86() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=2782"),
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
            NodeId.parse("i=2782"),
            NodeId.parse("i=46"),
            NodeId.parse("i=11112").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2782"),
            NodeId.parse("i=46"),
            NodeId.parse("i=11113").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2782"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16363").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2782"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16364").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2782"), NodeId.parse("i=46"), NodeId.parse("i=9009").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2782"), NodeId.parse("i=46"), NodeId.parse("i=9010").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2782"), NodeId.parse("i=46"), NodeId.parse("i=3874").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2782"),
            NodeId.parse("i=46"),
            NodeId.parse("i=32060").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2782"), NodeId.parse("i=47"), NodeId.parse("i=9011").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2782"), NodeId.parse("i=47"), NodeId.parse("i=9020").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2782"), NodeId.parse("i=47"), NodeId.parse("i=9022").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2782"), NodeId.parse("i=47"), NodeId.parse("i=9024").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2782"), NodeId.parse("i=46"), NodeId.parse("i=9026").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2782"), NodeId.parse("i=47"), NodeId.parse("i=9028").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2782"), NodeId.parse("i=47"), NodeId.parse("i=9027").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2782"), NodeId.parse("i=47"), NodeId.parse("i=9029").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2782"), NodeId.parse("i=47"), NodeId.parse("i=3875").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2782"),
            NodeId.parse("i=47"),
            NodeId.parse("i=12912").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2782"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2041").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode87() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=2830"),
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
            NodeId.parse("i=2830"), NodeId.parse("i=47"), NodeId.parse("i=9035").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2830"), NodeId.parse("i=47"), NodeId.parse("i=9055").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2830"), NodeId.parse("i=46"), NodeId.parse("i=2831").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2830"), NodeId.parse("i=46"), NodeId.parse("i=9064").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2830"), NodeId.parse("i=46"), NodeId.parse("i=9065").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2830"), NodeId.parse("i=46"), NodeId.parse("i=9066").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2830"), NodeId.parse("i=46"), NodeId.parse("i=9067").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2830"), NodeId.parse("i=46"), NodeId.parse("i=9068").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2830"), NodeId.parse("i=47"), NodeId.parse("i=9069").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2830"),
            NodeId.parse("i=47"),
            NodeId.parse("i=24312").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2830"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2782").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode88() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=2881"),
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
            NodeId.parse("i=2881"), NodeId.parse("i=47"), NodeId.parse("i=9073").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2881"), NodeId.parse("i=47"), NodeId.parse("i=9093").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2881"), NodeId.parse("i=47"), NodeId.parse("i=9102").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2881"), NodeId.parse("i=47"), NodeId.parse("i=9111").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2881"), NodeId.parse("i=47"), NodeId.parse("i=9113").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2881"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2782").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode89() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=2915"),
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
            NodeId.parse("i=2915"), NodeId.parse("i=47"), NodeId.parse("i=9118").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2915"), NodeId.parse("i=47"), NodeId.parse("i=9160").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2915"),
            NodeId.parse("i=46"),
            NodeId.parse("i=11120").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2915"), NodeId.parse("i=47"), NodeId.parse("i=9169").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2915"),
            NodeId.parse("i=47"),
            NodeId.parse("i=16371").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2915"), NodeId.parse("i=47"), NodeId.parse("i=9178").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2915"), NodeId.parse("i=46"), NodeId.parse("i=9215").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2915"), NodeId.parse("i=46"), NodeId.parse("i=9216").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2915"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16389").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2915"),
            NodeId.parse("i=47"),
            NodeId.parse("i=16390").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2915"),
            NodeId.parse("i=47"),
            NodeId.parse("i=16380").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2915"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16395").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2915"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16396").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2915"),
            NodeId.parse("i=47"),
            NodeId.parse("i=16397").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2915"),
            NodeId.parse("i=47"),
            NodeId.parse("i=16398").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2915"),
            NodeId.parse("i=47"),
            NodeId.parse("i=18190").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2915"),
            NodeId.parse("i=16361"),
            NodeId.parse("i=16399").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2915"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16400").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2915"),
            NodeId.parse("i=47"),
            NodeId.parse("i=16401").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2915"),
            NodeId.parse("i=47"),
            NodeId.parse("i=16402").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2915"),
            NodeId.parse("i=47"),
            NodeId.parse("i=16403").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2915"),
            NodeId.parse("i=47"),
            NodeId.parse("i=24316").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2915"),
            NodeId.parse("i=47"),
            NodeId.parse("i=17868").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2915"),
            NodeId.parse("i=47"),
            NodeId.parse("i=24318").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2915"),
            NodeId.parse("i=47"),
            NodeId.parse("i=17869").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2915"),
            NodeId.parse("i=47"),
            NodeId.parse("i=24320").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2915"),
            NodeId.parse("i=47"),
            NodeId.parse("i=17870").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2915"),
            NodeId.parse("i=47"),
            NodeId.parse("i=24322").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2915"),
            NodeId.parse("i=47"),
            NodeId.parse("i=18199").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2915"),
            NodeId.parse("i=47"),
            NodeId.parse("i=24324").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2915"),
            NodeId.parse("i=47"),
            NodeId.parse("i=24744").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2915"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2881").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode90() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=16405"),
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
            NodeId.parse("i=16405"),
            NodeId.parse("i=16362"),
            NodeId.parse("i=16406").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16405"), NodeId.parse("i=45"), NodeId.parse("i=61").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode91() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=32064"),
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
            NodeId.parse("i=32064"),
            NodeId.parse("i=32059"),
            NodeId.parse("i=32226").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=32064"),
            NodeId.parse("i=32059"),
            NodeId.parse("i=19847").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=32064"),
            NodeId.parse("i=45"),
            NodeId.parse("i=16405").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode92() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=2929"),
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
            NodeId.parse("i=2929"), NodeId.parse("i=46"), NodeId.parse("i=9115").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2929"), NodeId.parse("i=47"), NodeId.parse("i=2930").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2929"), NodeId.parse("i=47"), NodeId.parse("i=2932").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2929"), NodeId.parse("i=47"), NodeId.parse("i=2933").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2929"), NodeId.parse("i=47"), NodeId.parse("i=2935").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2929"), NodeId.parse("i=47"), NodeId.parse("i=2936").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2929"), NodeId.parse("i=47"), NodeId.parse("i=2940").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2929"), NodeId.parse("i=47"), NodeId.parse("i=2942").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2929"), NodeId.parse("i=47"), NodeId.parse("i=2943").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2929"), NodeId.parse("i=47"), NodeId.parse("i=2945").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2929"), NodeId.parse("i=47"), NodeId.parse("i=2949").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2929"),
            NodeId.parse("i=47"),
            NodeId.parse("i=24756").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2929"), NodeId.parse("i=47"), NodeId.parse("i=2947").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2929"),
            NodeId.parse("i=47"),
            NodeId.parse("i=24758").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2929"), NodeId.parse("i=47"), NodeId.parse("i=2948").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2929"),
            NodeId.parse("i=47"),
            NodeId.parse("i=24760").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2929"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2771").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode93() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=2955"),
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
            NodeId.parse("i=2955"),
            NodeId.parse("i=46"),
            NodeId.parse("i=11124").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2955"),
            NodeId.parse("i=46"),
            NodeId.parse("i=11125").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2955"),
            NodeId.parse("i=46"),
            NodeId.parse("i=11126").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2955"),
            NodeId.parse("i=46"),
            NodeId.parse("i=11127").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2955"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16572").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2955"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16573").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2955"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16574").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2955"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16575").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2955"),
            NodeId.parse("i=46"),
            NodeId.parse("i=24770").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2955"),
            NodeId.parse("i=46"),
            NodeId.parse("i=24771").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2955"),
            NodeId.parse("i=46"),
            NodeId.parse("i=24772").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2955"),
            NodeId.parse("i=46"),
            NodeId.parse("i=24773").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2955"),
            NodeId.parse("i=46"),
            NodeId.parse("i=24774").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2955"),
            NodeId.parse("i=46"),
            NodeId.parse("i=24775").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2955"),
            NodeId.parse("i=46"),
            NodeId.parse("i=24776").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2955"),
            NodeId.parse("i=46"),
            NodeId.parse("i=24777").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2955"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2915").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode94() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=9318"),
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
            NodeId.parse("i=9318"), NodeId.parse("i=47"), NodeId.parse("i=9329").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=9318"), NodeId.parse("i=47"), NodeId.parse("i=9331").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=9318"), NodeId.parse("i=47"), NodeId.parse("i=9333").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=9318"), NodeId.parse("i=47"), NodeId.parse("i=9335").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=9318"), NodeId.parse("i=47"), NodeId.parse("i=9337").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=9318"), NodeId.parse("i=47"), NodeId.parse("i=9338").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=9318"), NodeId.parse("i=47"), NodeId.parse("i=9339").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=9318"), NodeId.parse("i=47"), NodeId.parse("i=9340").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=9318"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2771").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode95() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=9341"),
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
            NodeId.parse("i=9341"), NodeId.parse("i=47"), NodeId.parse("i=9398").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=9341"), NodeId.parse("i=47"), NodeId.parse("i=9455").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=9341"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2955").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode96() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=9906"),
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
            NodeId.parse("i=9906"), NodeId.parse("i=47"), NodeId.parse("i=9963").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=9906"),
            NodeId.parse("i=47"),
            NodeId.parse("i=10020").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=9906"),
            NodeId.parse("i=47"),
            NodeId.parse("i=10029").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=9906"),
            NodeId.parse("i=47"),
            NodeId.parse("i=10038").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=9906"),
            NodeId.parse("i=47"),
            NodeId.parse("i=10047").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=9906"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2955").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode97() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=10060"),
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
            NodeId.parse("i=10060"),
            NodeId.parse("i=45"),
            NodeId.parse("i=9906").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode98() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=9482"),
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
            NodeId.parse("i=9482"),
            NodeId.parse("i=45"),
            NodeId.parse("i=9341").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode99() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=10368"),
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
            NodeId.parse("i=10368"),
            NodeId.parse("i=46"),
            NodeId.parse("i=10522").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=10368"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16776").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=10368"),
            NodeId.parse("i=45"),
            NodeId.parse("i=9906").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode100() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=10214"),
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
            NodeId.parse("i=10214"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16858").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=10214"),
            NodeId.parse("i=45"),
            NodeId.parse("i=9906").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode101() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=9764"),
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
            NodeId.parse("i=9764"), NodeId.parse("i=46"), NodeId.parse("i=9905").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=9764"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16817").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=9764"),
            NodeId.parse("i=45"),
            NodeId.parse("i=9341").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode102() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=9623"),
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
            NodeId.parse("i=9623"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16899").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=9623"),
            NodeId.parse("i=45"),
            NodeId.parse("i=9341").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode103() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=10523"),
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
            NodeId.parse("i=10523"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2915").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode104() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=10637"),
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
            NodeId.parse("i=10637"),
            NodeId.parse("i=46"),
            NodeId.parse("i=11158").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=10637"),
            NodeId.parse("i=45"),
            NodeId.parse("i=10523").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode105() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=11753"),
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
            NodeId.parse("i=11753"),
            NodeId.parse("i=45"),
            NodeId.parse("i=10637").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode106() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=10751"),
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
            NodeId.parse("i=10751"),
            NodeId.parse("i=45"),
            NodeId.parse("i=10637").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode107() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=18347"),
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
            NodeId.parse("i=18347"),
            NodeId.parse("i=45"),
            NodeId.parse("i=10637").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode108() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=18496"),
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
            NodeId.parse("i=18496"),
            NodeId.parse("i=45"),
            NodeId.parse("i=10637").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode109() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=13225"),
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
            NodeId.parse("i=13225"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13325").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13225"),
            NodeId.parse("i=46"),
            NodeId.parse("i=14900").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13225"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13326").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13225"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13327").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13225"),
            NodeId.parse("i=45"),
            NodeId.parse("i=11753").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode110() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=17080"),
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
            NodeId.parse("i=17080"),
            NodeId.parse("i=46"),
            NodeId.parse("i=17215").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17080"),
            NodeId.parse("i=46"),
            NodeId.parse("i=17216").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17080"),
            NodeId.parse("i=46"),
            NodeId.parse("i=17217").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17080"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2915").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode111() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=11163"),
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
            NodeId.parse("i=11163"), NodeId.parse("i=45"), NodeId.parse("i=58").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode112() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=11164"),
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
            NodeId.parse("i=11164"),
            NodeId.parse("i=45"),
            NodeId.parse("i=11163").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode113() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=11165"),
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
            NodeId.parse("i=11165"),
            NodeId.parse("i=45"),
            NodeId.parse("i=11163").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode114() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=11166"),
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
            NodeId.parse("i=11166"),
            NodeId.parse("i=45"),
            NodeId.parse("i=11163").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode115() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=17218"),
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
            NodeId.parse("i=17218"),
            NodeId.parse("i=45"),
            NodeId.parse("i=11163").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode116() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=17219"),
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
            NodeId.parse("i=17219"),
            NodeId.parse("i=45"),
            NodeId.parse("i=11163").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode117() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=17220"),
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
            NodeId.parse("i=17220"),
            NodeId.parse("i=45"),
            NodeId.parse("i=11163").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode118() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=18665"),
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
            NodeId.parse("i=18665"),
            NodeId.parse("i=45"),
            NodeId.parse("i=11163").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode119() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=17221"),
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
            NodeId.parse("i=17221"),
            NodeId.parse("i=45"),
            NodeId.parse("i=11163").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode120() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=2790"),
            new QualifiedName(0, "AuditConditionEventType"),
            new LocalizedText("", "AuditConditionEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15644"), new PermissionType(UInteger.valueOf(33))),
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(65535)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            NodeId.parse("i=2790"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2127").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode121() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=2803"),
            new QualifiedName(0, "AuditConditionEnableEventType"),
            new LocalizedText("", "AuditConditionEnableEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15644"), new PermissionType(UInteger.valueOf(33))),
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(65535)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            NodeId.parse("i=2803"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2790").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode122() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=2829"),
            new QualifiedName(0, "AuditConditionCommentEventType"),
            new LocalizedText("", "AuditConditionCommentEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15644"), new PermissionType(UInteger.valueOf(33))),
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(65535)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            NodeId.parse("i=2829"),
            NodeId.parse("i=46"),
            NodeId.parse("i=17222").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2829"),
            NodeId.parse("i=46"),
            NodeId.parse("i=11851").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2829"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2790").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode123() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=8927"),
            new QualifiedName(0, "AuditConditionRespondEventType"),
            new LocalizedText("", "AuditConditionRespondEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15644"), new PermissionType(UInteger.valueOf(33))),
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(65535)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            NodeId.parse("i=8927"),
            NodeId.parse("i=46"),
            NodeId.parse("i=11852").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=8927"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2790").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode124() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=8944"),
            new QualifiedName(0, "AuditConditionAcknowledgeEventType"),
            new LocalizedText("", "AuditConditionAcknowledgeEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15644"), new PermissionType(UInteger.valueOf(33))),
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(65535)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            NodeId.parse("i=8944"),
            NodeId.parse("i=46"),
            NodeId.parse("i=17223").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=8944"),
            NodeId.parse("i=46"),
            NodeId.parse("i=11853").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=8944"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2790").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode125() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=8961"),
            new QualifiedName(0, "AuditConditionConfirmEventType"),
            new LocalizedText("", "AuditConditionConfirmEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15644"), new PermissionType(UInteger.valueOf(33))),
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(65535)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            NodeId.parse("i=8961"),
            NodeId.parse("i=46"),
            NodeId.parse("i=17224").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=8961"),
            NodeId.parse("i=46"),
            NodeId.parse("i=11854").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=8961"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2790").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode126() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=11093"),
            new QualifiedName(0, "AuditConditionShelvingEventType"),
            new LocalizedText("", "AuditConditionShelvingEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15644"), new PermissionType(UInteger.valueOf(33))),
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(65535)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            NodeId.parse("i=11093"),
            NodeId.parse("i=46"),
            NodeId.parse("i=11855").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11093"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2790").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode127() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=17225"),
            new QualifiedName(0, "AuditConditionSuppressionEventType"),
            new LocalizedText("", "AuditConditionSuppressionEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15644"), new PermissionType(UInteger.valueOf(33))),
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(65535)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            NodeId.parse("i=17225"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2790").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode128() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=17242"),
            new QualifiedName(0, "AuditConditionSilenceEventType"),
            new LocalizedText("", "AuditConditionSilenceEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15644"), new PermissionType(UInteger.valueOf(33))),
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(65535)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            NodeId.parse("i=17242"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2790").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode129() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=15013"),
            new QualifiedName(0, "AuditConditionResetEventType"),
            new LocalizedText("", "AuditConditionResetEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15644"), new PermissionType(UInteger.valueOf(33))),
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(65535)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            NodeId.parse("i=15013"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2790").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode130() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=17259"),
            new QualifiedName(0, "AuditConditionOutOfServiceEventType"),
            new LocalizedText("", "AuditConditionOutOfServiceEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15644"), new PermissionType(UInteger.valueOf(33))),
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(65535)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            NodeId.parse("i=17259"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2790").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode131() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=2787"),
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
            NodeId.parse("i=2787"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2130").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode132() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=2788"),
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
            NodeId.parse("i=2788"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2130").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode133() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=2789"),
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
            NodeId.parse("i=2789"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2130").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode134() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=17279"),
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
            NodeId.parse("i=17279"),
            NodeId.parse("i=47"),
            NodeId.parse("i=17280").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17279"),
            NodeId.parse("i=47"),
            NodeId.parse("i=17991").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17279"),
            NodeId.parse("i=47"),
            NodeId.parse("i=17281").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17279"),
            NodeId.parse("i=47"),
            NodeId.parse("i=17282").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17279"),
            NodeId.parse("i=47"),
            NodeId.parse("i=17284").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17279"),
            NodeId.parse("i=47"),
            NodeId.parse("i=17286").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17279"),
            NodeId.parse("i=47"),
            NodeId.parse("i=17283").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17279"),
            NodeId.parse("i=47"),
            NodeId.parse("i=17288").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17279"),
            NodeId.parse("i=47"),
            NodeId.parse("i=18666").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17279"), NodeId.parse("i=45"), NodeId.parse("i=58").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode135() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=2391"),
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
            NodeId.parse("i=2391"), NodeId.parse("i=47"), NodeId.parse("i=3830").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2391"), NodeId.parse("i=47"), NodeId.parse("i=3835").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2391"), NodeId.parse("i=46"), NodeId.parse("i=2392").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2391"), NodeId.parse("i=46"), NodeId.parse("i=2393").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2391"), NodeId.parse("i=46"), NodeId.parse("i=2394").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2391"), NodeId.parse("i=46"), NodeId.parse("i=2395").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2391"), NodeId.parse("i=46"), NodeId.parse("i=2396").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2391"), NodeId.parse("i=46"), NodeId.parse("i=2397").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2391"), NodeId.parse("i=46"), NodeId.parse("i=2398").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2391"), NodeId.parse("i=47"), NodeId.parse("i=2399").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2391"), NodeId.parse("i=47"), NodeId.parse("i=3850").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2391"), NodeId.parse("i=47"), NodeId.parse("i=2406").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2391"), NodeId.parse("i=47"), NodeId.parse("i=2400").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2391"), NodeId.parse("i=47"), NodeId.parse("i=2402").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2391"), NodeId.parse("i=47"), NodeId.parse("i=2404").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2391"), NodeId.parse("i=47"), NodeId.parse("i=2408").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2391"), NodeId.parse("i=47"), NodeId.parse("i=2410").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2391"), NodeId.parse("i=47"), NodeId.parse("i=2412").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2391"), NodeId.parse("i=47"), NodeId.parse("i=2414").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2391"), NodeId.parse("i=47"), NodeId.parse("i=2416").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2391"), NodeId.parse("i=47"), NodeId.parse("i=2418").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2391"), NodeId.parse("i=47"), NodeId.parse("i=2420").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2391"), NodeId.parse("i=47"), NodeId.parse("i=2422").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2391"), NodeId.parse("i=47"), NodeId.parse("i=2424").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2391"), NodeId.parse("i=47"), NodeId.parse("i=2426").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2391"), NodeId.parse("i=47"), NodeId.parse("i=2427").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2391"), NodeId.parse("i=47"), NodeId.parse("i=2428").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2391"), NodeId.parse("i=47"), NodeId.parse("i=2429").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2391"), NodeId.parse("i=47"), NodeId.parse("i=2430").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2391"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2771").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode136() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=2378"),
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
            NodeId.parse("i=2378"), NodeId.parse("i=47"), NodeId.parse("i=2379").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2378"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2311").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode137() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=11856"),
            new QualifiedName(0, "AuditProgramTransitionEventType"),
            new LocalizedText("", "AuditProgramTransitionEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15644"), new PermissionType(UInteger.valueOf(33))),
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(65535)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=11856"),
            NodeId.parse("i=46"),
            NodeId.parse("i=11875").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11856"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2315").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode138() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=3806"),
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
            NodeId.parse("i=3806"), NodeId.parse("i=47"), NodeId.parse("i=3825").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=3806"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2315").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode139() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=2318"),
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
            NodeId.parse("i=2318"), NodeId.parse("i=47"), NodeId.parse("i=3059").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2318"),
            NodeId.parse("i=47"),
            NodeId.parse("i=11876").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2318"), NodeId.parse("i=46"), NodeId.parse("i=2323").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2318"), NodeId.parse("i=46"), NodeId.parse("i=2324").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2318"), NodeId.parse("i=46"), NodeId.parse("i=2325").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2318"), NodeId.parse("i=46"), NodeId.parse("i=2326").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2318"), NodeId.parse("i=46"), NodeId.parse("i=2327").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2318"), NodeId.parse("i=46"), NodeId.parse("i=2328").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2318"),
            NodeId.parse("i=46"),
            NodeId.parse("i=11499").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2318"),
            NodeId.parse("i=46"),
            NodeId.parse("i=11500").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2318"),
            NodeId.parse("i=46"),
            NodeId.parse("i=19092").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2318"),
            NodeId.parse("i=46"),
            NodeId.parse("i=32619").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2318"),
            NodeId.parse("i=46"),
            NodeId.parse("i=32620").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2318"), NodeId.parse("i=45"), NodeId.parse("i=58").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode140() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=32621"),
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
            NodeId.parse("i=32621"),
            NodeId.parse("i=47"),
            NodeId.parse("i=32622").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=32621"),
            NodeId.parse("i=46"),
            NodeId.parse("i=32623").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=32621"),
            NodeId.parse("i=46"),
            NodeId.parse("i=32624").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=32621"),
            NodeId.parse("i=46"),
            NodeId.parse("i=18644").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=32621"), NodeId.parse("i=45"), NodeId.parse("i=58").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode141() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=32625"),
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
            NodeId.parse("i=32625"),
            NodeId.parse("i=46"),
            NodeId.parse("i=32626").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=32625"),
            NodeId.parse("i=46"),
            NodeId.parse("i=32627").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=32625"),
            NodeId.parse("i=46"),
            NodeId.parse("i=32628").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=32625"),
            NodeId.parse("i=46"),
            NodeId.parse("i=32629").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=32625"),
            NodeId.parse("i=46"),
            NodeId.parse("i=32630").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=32625"),
            NodeId.parse("i=46"),
            NodeId.parse("i=32631").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=32625"),
            NodeId.parse("i=46"),
            NodeId.parse("i=32632").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=32625"), NodeId.parse("i=45"), NodeId.parse("i=58").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode142() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=2330"),
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
            NodeId.parse("i=2330"), NodeId.parse("i=46"), NodeId.parse("i=2331").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2330"), NodeId.parse("i=46"), NodeId.parse("i=2332").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2330"),
            NodeId.parse("i=46"),
            NodeId.parse("i=11268").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2330"),
            NodeId.parse("i=46"),
            NodeId.parse("i=11269").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2330"), NodeId.parse("i=46"), NodeId.parse("i=2334").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2330"), NodeId.parse("i=46"), NodeId.parse("i=2335").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2330"), NodeId.parse("i=46"), NodeId.parse("i=2336").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2330"), NodeId.parse("i=46"), NodeId.parse("i=2337").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2330"), NodeId.parse("i=46"), NodeId.parse("i=2338").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2330"),
            NodeId.parse("i=46"),
            NodeId.parse("i=11278").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2330"),
            NodeId.parse("i=46"),
            NodeId.parse("i=11279").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2330"),
            NodeId.parse("i=46"),
            NodeId.parse("i=11280").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2330"),
            NodeId.parse("i=46"),
            NodeId.parse("i=11501").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2330"),
            NodeId.parse("i=46"),
            NodeId.parse("i=11270").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2330"),
            NodeId.parse("i=47"),
            NodeId.parse("i=11172").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2330"),
            NodeId.parse("i=46"),
            NodeId.parse("i=19094").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2330"), NodeId.parse("i=45"), NodeId.parse("i=58").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode143() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=2999"),
            new QualifiedName(0, "AuditHistoryEventUpdateEventType"),
            new LocalizedText("", "AuditHistoryEventUpdateEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15644"), new PermissionType(UInteger.valueOf(33))),
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(65535)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=2999"), NodeId.parse("i=46"), NodeId.parse("i=3025").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2999"), NodeId.parse("i=46"), NodeId.parse("i=3028").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2999"), NodeId.parse("i=46"), NodeId.parse("i=3003").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2999"), NodeId.parse("i=46"), NodeId.parse("i=3029").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2999"), NodeId.parse("i=46"), NodeId.parse("i=3030").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=2999"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2104").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode144() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=3006"),
            new QualifiedName(0, "AuditHistoryValueUpdateEventType"),
            new LocalizedText("", "AuditHistoryValueUpdateEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15644"), new PermissionType(UInteger.valueOf(33))),
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(65535)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=3006"), NodeId.parse("i=46"), NodeId.parse("i=3026").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=3006"), NodeId.parse("i=46"), NodeId.parse("i=3031").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=3006"), NodeId.parse("i=46"), NodeId.parse("i=3032").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=3006"), NodeId.parse("i=46"), NodeId.parse("i=3033").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=3006"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2104").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode145() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=19095"),
            new QualifiedName(0, "AuditHistoryAnnotationUpdateEventType"),
            new LocalizedText("", "AuditHistoryAnnotationUpdateEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15644"), new PermissionType(UInteger.valueOf(33))),
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(65535)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=19095"),
            NodeId.parse("i=46"),
            NodeId.parse("i=19293").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19095"),
            NodeId.parse("i=46"),
            NodeId.parse("i=19294").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19095"),
            NodeId.parse("i=46"),
            NodeId.parse("i=19295").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19095"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2104").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode146() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=3012"),
            new QualifiedName(0, "AuditHistoryDeleteEventType"),
            new LocalizedText("", "AuditHistoryDeleteEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15644"), new PermissionType(UInteger.valueOf(33))),
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(65535)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=3012"), NodeId.parse("i=46"), NodeId.parse("i=3027").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=3012"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2104").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode147() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=3014"),
            new QualifiedName(0, "AuditHistoryRawModifyDeleteEventType"),
            new LocalizedText("", "AuditHistoryRawModifyDeleteEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15644"), new PermissionType(UInteger.valueOf(33))),
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(65535)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=3014"), NodeId.parse("i=46"), NodeId.parse("i=3015").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=3014"), NodeId.parse("i=46"), NodeId.parse("i=3016").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=3014"), NodeId.parse("i=46"), NodeId.parse("i=3017").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=3014"), NodeId.parse("i=46"), NodeId.parse("i=3034").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=3014"),
            NodeId.parse("i=45"),
            NodeId.parse("i=3012").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode148() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=3019"),
            new QualifiedName(0, "AuditHistoryAtTimeDeleteEventType"),
            new LocalizedText("", "AuditHistoryAtTimeDeleteEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15644"), new PermissionType(UInteger.valueOf(33))),
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(65535)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=3019"), NodeId.parse("i=46"), NodeId.parse("i=3020").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=3019"), NodeId.parse("i=46"), NodeId.parse("i=3021").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=3019"),
            NodeId.parse("i=45"),
            NodeId.parse("i=3012").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode149() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=3022"),
            new QualifiedName(0, "AuditHistoryEventDeleteEventType"),
            new LocalizedText("", "AuditHistoryEventDeleteEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15644"), new PermissionType(UInteger.valueOf(33))),
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(65535)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=3022"), NodeId.parse("i=46"), NodeId.parse("i=3023").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=3022"), NodeId.parse("i=46"), NodeId.parse("i=3024").expanded(), true));
    node.addReference(
        new Reference(
            NodeId.parse("i=3022"),
            NodeId.parse("i=45"),
            NodeId.parse("i=3012").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode150() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=32758"),
            new QualifiedName(0, "AuditHistoryConfigurationChangeEventType"),
            new LocalizedText("", "AuditHistoryConfigurationChangeEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15644"), new PermissionType(UInteger.valueOf(33))),
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(65535)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=32758"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2052").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode151() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=32803"),
            new QualifiedName(0, "AuditHistoryBulkInsertEventType"),
            new LocalizedText("", "AuditHistoryBulkInsertEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            new RolePermissionType[] {
              new RolePermissionType(
                  NodeId.parse("i=15644"), new PermissionType(UInteger.valueOf(33))),
              new RolePermissionType(
                  NodeId.parse("i=15704"), new PermissionType(UInteger.valueOf(65535)))
            },
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=32803"),
            NodeId.parse("i=46"),
            NodeId.parse("i=32821").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=32803"),
            NodeId.parse("i=46"),
            NodeId.parse("i=32822").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=32803"),
            NodeId.parse("i=46"),
            NodeId.parse("i=32823").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=32803"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2052").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode152() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=12522"),
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
            NodeId.parse("i=12522"),
            NodeId.parse("i=46"),
            NodeId.parse("i=12542").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12522"),
            NodeId.parse("i=46"),
            NodeId.parse("i=19296").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12522"),
            NodeId.parse("i=46"),
            NodeId.parse("i=32254").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12522"),
            NodeId.parse("i=46"),
            NodeId.parse("i=23563").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12522"),
            NodeId.parse("i=47"),
            NodeId.parse("i=12543").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12522"),
            NodeId.parse("i=47"),
            NodeId.parse("i=12546").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12522"),
            NodeId.parse("i=47"),
            NodeId.parse("i=12548").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12522"),
            NodeId.parse("i=47"),
            NodeId.parse("i=12550").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12522"),
            NodeId.parse("i=45"),
            NodeId.parse("i=11575").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode153() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=19297"),
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
            NodeId.parse("i=19297"),
            NodeId.parse("i=46"),
            NodeId.parse("i=19446").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19297"),
            NodeId.parse("i=46"),
            NodeId.parse("i=19447").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19297"),
            NodeId.parse("i=46"),
            NodeId.parse("i=19448").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19297"),
            NodeId.parse("i=45"),
            NodeId.parse("i=11753").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode154() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=12555"),
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
            NodeId.parse("i=12555"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13599").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12555"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13631").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12555"),
            NodeId.parse("i=46"),
            NodeId.parse("i=19398").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12555"),
            NodeId.parse("i=47"),
            NodeId.parse("i=19450").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12555"),
            NodeId.parse("i=47"),
            NodeId.parse("i=20143").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12555"),
            NodeId.parse("i=47"),
            NodeId.parse("i=23526").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12555"),
            NodeId.parse("i=9006"),
            NodeId.parse("i=13225").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12555"),
            NodeId.parse("i=9006"),
            NodeId.parse("i=19297").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12555"), NodeId.parse("i=45"), NodeId.parse("i=58").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode155() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=13813"),
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
            NodeId.parse("i=13813"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13814").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13813"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13848").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13813"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13882").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13813"),
            NodeId.parse("i=35"),
            NodeId.parse("i=13916").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=13813"), NodeId.parse("i=45"), NodeId.parse("i=61").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode156() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=12556"),
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
            NodeId.parse("i=12556"), NodeId.parse("i=45"), NodeId.parse("i=58").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode157() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=12557"),
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
            NodeId.parse("i=12557"),
            NodeId.parse("i=45"),
            NodeId.parse("i=12556").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode158() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=12558"),
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
            NodeId.parse("i=12558"),
            NodeId.parse("i=45"),
            NodeId.parse("i=12556").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode159() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=19323"),
            new QualifiedName(0, "UserCertificateType"),
            new LocalizedText("", "UserCertificateType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=19323"),
            NodeId.parse("i=45"),
            NodeId.parse("i=12556").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode160() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=19324"),
            new QualifiedName(0, "TlsCertificateType"),
            new LocalizedText("", "TlsCertificateType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=19324"),
            NodeId.parse("i=45"),
            NodeId.parse("i=12556").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode161() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=19325"),
            new QualifiedName(0, "TlsServerCertificateType"),
            new LocalizedText("", "TlsServerCertificateType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            NodeId.parse("i=19325"),
            NodeId.parse("i=45"),
            NodeId.parse("i=19324").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode162() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=19326"),
            new QualifiedName(0, "TlsClientCertificateType"),
            new LocalizedText("", "TlsClientCertificateType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            NodeId.parse("i=19326"),
            NodeId.parse("i=45"),
            NodeId.parse("i=19324").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode163() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=12559"),
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
            NodeId.parse("i=12559"),
            NodeId.parse("i=45"),
            NodeId.parse("i=12557").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode164() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=12560"),
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
            NodeId.parse("i=12560"),
            NodeId.parse("i=45"),
            NodeId.parse("i=12557").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode165() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=23537"),
            new QualifiedName(0, "EccApplicationCertificateType"),
            new LocalizedText("", "EccApplicationCertificateType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=23537"),
            NodeId.parse("i=45"),
            NodeId.parse("i=12557").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode166() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=23538"),
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
            NodeId.parse("i=23538"),
            NodeId.parse("i=45"),
            NodeId.parse("i=23537").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode167() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=23539"),
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
            NodeId.parse("i=23539"),
            NodeId.parse("i=45"),
            NodeId.parse("i=23537").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode168() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=23540"),
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
            NodeId.parse("i=23540"),
            NodeId.parse("i=45"),
            NodeId.parse("i=23537").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode169() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=23541"),
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
            NodeId.parse("i=23541"),
            NodeId.parse("i=45"),
            NodeId.parse("i=23537").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode170() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=23542"),
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
            NodeId.parse("i=23542"),
            NodeId.parse("i=45"),
            NodeId.parse("i=23537").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode171() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=23543"),
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
            NodeId.parse("i=23543"),
            NodeId.parse("i=45"),
            NodeId.parse("i=23537").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode172() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=15437"),
            new QualifiedName(0, "ConfigurationFileType"),
            new LocalizedText("", "ConfigurationFileType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            NodeId.parse("i=15437"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15438").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15437"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15439").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15437"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15503").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15437"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15504").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15437"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15508").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15437"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15505").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15437"),
            NodeId.parse("i=45"),
            NodeId.parse("i=11575").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode173() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=15541"),
            new QualifiedName(0, "ConfigurationUpdatedAuditEventType"),
            new LocalizedText("", "ConfigurationUpdatedAuditEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=15541"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15542").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15541"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15543").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15541"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2052").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode174() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=32260"),
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
            NodeId.parse("i=32260"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2127").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode175() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=12561"),
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
            NodeId.parse("i=12561"),
            NodeId.parse("i=46"),
            NodeId.parse("i=32281").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12561"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2127").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode176() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=32286"),
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
            NodeId.parse("i=32286"),
            NodeId.parse("i=46"),
            NodeId.parse("i=32287").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=32286"),
            NodeId.parse("i=46"),
            NodeId.parse("i=32288").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=32286"),
            NodeId.parse("i=46"),
            NodeId.parse("i=32289").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=32286"),
            NodeId.parse("i=46"),
            NodeId.parse("i=32290").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=32286"),
            NodeId.parse("i=46"),
            NodeId.parse("i=32291").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=32286"),
            NodeId.parse("i=46"),
            NodeId.parse("i=32292").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=32286"), NodeId.parse("i=45"), NodeId.parse("i=58").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode177() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=16662"),
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
            NodeId.parse("i=16662"),
            NodeId.parse("i=35"),
            NodeId.parse("i=16663").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=16662"), NodeId.parse("i=45"), NodeId.parse("i=61").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode178() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=15550"),
            new QualifiedName(0, "ApplicationConfigurationFileType"),
            new LocalizedText("", "ApplicationConfigurationFileType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            NodeId.parse("i=15550"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15551").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15550"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15552").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15550"),
            NodeId.parse("i=46"),
            NodeId.parse("i=19414").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15550"),
            NodeId.parse("i=46"),
            NodeId.parse("i=19415").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15550"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15553").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15550"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15554").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15550"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15555").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15550"),
            NodeId.parse("i=46"),
            NodeId.parse("i=19416").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15550"),
            NodeId.parse("i=45"),
            NodeId.parse("i=15437").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode179() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=12581"),
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
            NodeId.parse("i=12581"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25696").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12581"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25724").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12581"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25697").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12581"),
            NodeId.parse("i=46"),
            NodeId.parse("i=18660").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12581"),
            NodeId.parse("i=46"),
            NodeId.parse("i=12708").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12581"),
            NodeId.parse("i=46"),
            NodeId.parse("i=12583").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12581"),
            NodeId.parse("i=46"),
            NodeId.parse("i=12584").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12581"),
            NodeId.parse("i=46"),
            NodeId.parse("i=12585").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12581"),
            NodeId.parse("i=46"),
            NodeId.parse("i=23593").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12581"),
            NodeId.parse("i=46"),
            NodeId.parse("i=18661").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12581"),
            NodeId.parse("i=46"),
            NodeId.parse("i=19308").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12581"),
            NodeId.parse("i=47"),
            NodeId.parse("i=12616").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12581"),
            NodeId.parse("i=47"),
            NodeId.parse("i=19337").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12581"),
            NodeId.parse("i=47"),
            NodeId.parse("i=19340").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12581"),
            NodeId.parse("i=47"),
            NodeId.parse("i=32296").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12581"),
            NodeId.parse("i=47"),
            NodeId.parse("i=12734").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12581"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25698").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12581"),
            NodeId.parse("i=47"),
            NodeId.parse("i=12731").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12581"),
            NodeId.parse("i=47"),
            NodeId.parse("i=12775").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12581"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25699").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12581"),
            NodeId.parse("i=47"),
            NodeId.parse("i=13950").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12581"),
            NodeId.parse("i=47"),
            NodeId.parse("i=32299").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12581"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15564").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12581"), NodeId.parse("i=45"), NodeId.parse("i=58").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode180() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=25731"),
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
            NodeId.parse("i=25731"),
            NodeId.parse("i=46"),
            NodeId.parse("i=26850").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25731"),
            NodeId.parse("i=46"),
            NodeId.parse("i=26851").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25731"),
            NodeId.parse("i=46"),
            NodeId.parse("i=26852").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25731"),
            NodeId.parse("i=46"),
            NodeId.parse("i=26849").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25731"),
            NodeId.parse("i=46"),
            NodeId.parse("i=23741").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25731"),
            NodeId.parse("i=47"),
            NodeId.parse("i=19423").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25731"),
            NodeId.parse("i=47"),
            NodeId.parse("i=19427").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25731"),
            NodeId.parse("i=45"),
            NodeId.parse("i=12581").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode181() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=32306"),
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
            NodeId.parse("i=32306"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2127").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode182() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=12620"),
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
            NodeId.parse("i=12620"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13735").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12620"),
            NodeId.parse("i=46"),
            NodeId.parse("i=13736").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=12620"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2127").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode183() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=17496"),
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
            NodeId.parse("i=17496"),
            NodeId.parse("i=47"),
            NodeId.parse("i=17511").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17496"),
            NodeId.parse("i=47"),
            NodeId.parse("i=17522").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17496"), NodeId.parse("i=45"), NodeId.parse("i=61").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode184() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=18001"),
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
            NodeId.parse("i=18001"),
            NodeId.parse("i=46"),
            NodeId.parse("i=18069").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=18001"),
            NodeId.parse("i=46"),
            NodeId.parse("i=18165").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=18001"),
            NodeId.parse("i=46"),
            NodeId.parse("i=18004").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=18001"),
            NodeId.parse("i=46"),
            NodeId.parse("i=18657").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=18001"),
            NodeId.parse("i=46"),
            NodeId.parse("i=18005").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=18001"),
            NodeId.parse("i=47"),
            NodeId.parse("i=17534").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=18001"),
            NodeId.parse("i=47"),
            NodeId.parse("i=18006").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=18001"),
            NodeId.parse("i=47"),
            NodeId.parse("i=18008").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=18001"), NodeId.parse("i=45"), NodeId.parse("i=58").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode185() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=18011"),
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
            NodeId.parse("i=18011"),
            NodeId.parse("i=46"),
            NodeId.parse("i=18028").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=18011"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2127").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode186() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=18029"),
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
            NodeId.parse("i=18029"),
            NodeId.parse("i=45"),
            NodeId.parse("i=18011").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode187() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=18047"),
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
            NodeId.parse("i=18047"),
            NodeId.parse("i=46"),
            NodeId.parse("i=18064").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=18047"),
            NodeId.parse("i=45"),
            NodeId.parse("i=18011").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode188() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=23556"),
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
            NodeId.parse("i=23556"),
            NodeId.parse("i=47"),
            NodeId.parse("i=23557").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23556"), NodeId.parse("i=45"), NodeId.parse("i=61").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode189() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=17852"),
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
            NodeId.parse("i=17852"),
            NodeId.parse("i=46"),
            NodeId.parse("i=18072").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17852"),
            NodeId.parse("i=46"),
            NodeId.parse("i=17860").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17852"),
            NodeId.parse("i=46"),
            NodeId.parse("i=18073").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17852"), NodeId.parse("i=45"), NodeId.parse("i=58").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode190() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=11187"),
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
            NodeId.parse("i=11187"),
            NodeId.parse("i=46"),
            NodeId.parse("i=11188").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11187"),
            NodeId.parse("i=46"),
            NodeId.parse("i=11189").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11187"),
            NodeId.parse("i=46"),
            NodeId.parse("i=11190").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11187"),
            NodeId.parse("i=46"),
            NodeId.parse("i=11191").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=11187"), NodeId.parse("i=45"), NodeId.parse("i=58").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode191() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=15906"),
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
            NodeId.parse("i=15906"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15907").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15906"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15910").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15906"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15913").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15906"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25277").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15906"), NodeId.parse("i=45"), NodeId.parse("i=58").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode192() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=15452"),
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
            NodeId.parse("i=15452"),
            NodeId.parse("i=35"),
            NodeId.parse("i=15453").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15452"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15459").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15452"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15461").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15452"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15464").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15452"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25312").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15452"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25315").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15452"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25317").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15452"), NodeId.parse("i=45"), NodeId.parse("i=61").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode193() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=15471"),
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
            NodeId.parse("i=15471"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15472").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15471"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15046").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15471"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15047").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15471"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15048").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15471"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15056").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15471"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25624").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15471"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25625").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15471"), NodeId.parse("i=45"), NodeId.parse("i=58").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode194() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=25337"),
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
            NodeId.parse("i=25337"),
            NodeId.parse("i=25345"),
            NodeId.parse("i=25626").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25337"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25634").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25337"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25635").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25337"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25340").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25337"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25636").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25337"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25637").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25337"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25638").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25337"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25639").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25337"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25640").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25337"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25641").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25337"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25644").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25337"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25647").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25337"), NodeId.parse("i=45"), NodeId.parse("i=58").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode195() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=25346"),
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
            NodeId.parse("i=25346"),
            NodeId.parse("i=35"),
            NodeId.parse("i=25347").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25346"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25358").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25346"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25366").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25346"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25369").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25346"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25371").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25346"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25374").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25346"), NodeId.parse("i=45"), NodeId.parse("i=61").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode196() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=14416"),
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
            NodeId.parse("i=14416"),
            NodeId.parse("i=14476"),
            NodeId.parse("i=14417").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14416"),
            NodeId.parse("i=47"),
            NodeId.parse("i=17296").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14416"),
            NodeId.parse("i=47"),
            NodeId.parse("i=16598").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14416"),
            NodeId.parse("i=47"),
            NodeId.parse("i=14432").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14416"),
            NodeId.parse("i=47"),
            NodeId.parse("i=14434").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14416"),
            NodeId.parse("i=47"),
            NodeId.parse("i=23622").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14416"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25403").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14416"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15844").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14416"),
            NodeId.parse("i=47"),
            NodeId.parse("i=18715").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14416"),
            NodeId.parse("i=47"),
            NodeId.parse("i=23642").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14416"),
            NodeId.parse("i=47"),
            NodeId.parse("i=23649").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14416"),
            NodeId.parse("i=46"),
            NodeId.parse("i=17479").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14416"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25432").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14416"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25433").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14416"),
            NodeId.parse("i=46"),
            NodeId.parse("i=32396").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14416"),
            NodeId.parse("i=46"),
            NodeId.parse("i=32397").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14416"),
            NodeId.parse("i=45"),
            NodeId.parse("i=15906").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode197() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=25482"),
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
            NodeId.parse("i=25482"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25505").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25482"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25508").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25482"),
            NodeId.parse("i=45"),
            NodeId.parse("i=11575").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode198() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=14509"),
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
            NodeId.parse("i=14509"),
            NodeId.parse("i=14936"),
            NodeId.parse("i=15222").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14509"),
            NodeId.parse("i=46"),
            NodeId.parse("i=14519").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14509"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15229").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14509"),
            NodeId.parse("i=46"),
            NodeId.parse("i=16759").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14509"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25521").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14509"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15481").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14509"), NodeId.parse("i=45"), NodeId.parse("i=58").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode199() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=15489"),
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
            NodeId.parse("i=15489"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15490").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15489"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15491").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15489"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15494").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15489"), NodeId.parse("i=45"), NodeId.parse("i=58").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode200() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=14534"),
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
            NodeId.parse("i=14534"),
            NodeId.parse("i=46"),
            NodeId.parse("i=14548").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14534"),
            NodeId.parse("i=47"),
            NodeId.parse("i=14555").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14534"),
            NodeId.parse("i=47"),
            NodeId.parse("i=14558").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14534"),
            NodeId.parse("i=45"),
            NodeId.parse("i=14509").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode201() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=14572"),
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
            NodeId.parse("i=14572"),
            NodeId.parse("i=46"),
            NodeId.parse("i=14586").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14572"),
            NodeId.parse("i=46"),
            NodeId.parse("i=14587").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14572"),
            NodeId.parse("i=46"),
            NodeId.parse("i=14588").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14572"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15052").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14572"),
            NodeId.parse("i=45"),
            NodeId.parse("i=14509").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode202() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=14477"),
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
            NodeId.parse("i=14477"),
            NodeId.parse("i=35"),
            NodeId.parse("i=14478").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14477"),
            NodeId.parse("i=47"),
            NodeId.parse("i=14487").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14477"),
            NodeId.parse("i=47"),
            NodeId.parse("i=14493").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14477"),
            NodeId.parse("i=47"),
            NodeId.parse("i=14496").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14477"),
            NodeId.parse("i=47"),
            NodeId.parse("i=16935").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14477"),
            NodeId.parse("i=47"),
            NodeId.parse("i=16960").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14477"),
            NodeId.parse("i=47"),
            NodeId.parse("i=14499").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14477"),
            NodeId.parse("i=47"),
            NodeId.parse("i=16994").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14477"),
            NodeId.parse("i=47"),
            NodeId.parse("i=16997").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14477"), NodeId.parse("i=45"), NodeId.parse("i=61").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode203() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=14209"),
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
            NodeId.parse("i=14209"),
            NodeId.parse("i=46"),
            NodeId.parse("i=14595").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14209"),
            NodeId.parse("i=47"),
            NodeId.parse("i=17306").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14209"),
            NodeId.parse("i=46"),
            NodeId.parse("i=17485").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14209"),
            NodeId.parse("i=47"),
            NodeId.parse("i=14221").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14209"),
            NodeId.parse("i=47"),
            NodeId.parse("i=17203").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14209"),
            NodeId.parse("i=18804"),
            NodeId.parse("i=17310").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14209"),
            NodeId.parse("i=18805"),
            NodeId.parse("i=17325").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14209"),
            NodeId.parse("i=47"),
            NodeId.parse("i=14600").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14209"),
            NodeId.parse("i=47"),
            NodeId.parse("i=19241").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14209"),
            NodeId.parse("i=47"),
            NodeId.parse("i=17427").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14209"),
            NodeId.parse("i=47"),
            NodeId.parse("i=17465").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14209"),
            NodeId.parse("i=47"),
            NodeId.parse("i=14225").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14209"), NodeId.parse("i=45"), NodeId.parse("i=58").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode204() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=17721"),
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
            NodeId.parse("i=17721"), NodeId.parse("i=45"), NodeId.parse("i=58").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode205() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=14232"),
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
            NodeId.parse("i=14232"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15926").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14232"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15927").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14232"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15928").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14232"),
            NodeId.parse("i=46"),
            NodeId.parse("i=17724").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14232"),
            NodeId.parse("i=46"),
            NodeId.parse("i=17488").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14232"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15265").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14232"), NodeId.parse("i=45"), NodeId.parse("i=58").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode206() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=17725"),
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
            NodeId.parse("i=17725"),
            NodeId.parse("i=46"),
            NodeId.parse("i=17736").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17725"),
            NodeId.parse("i=46"),
            NodeId.parse("i=17737").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17725"),
            NodeId.parse("i=46"),
            NodeId.parse("i=17738").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17725"),
            NodeId.parse("i=46"),
            NodeId.parse("i=17739").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17725"),
            NodeId.parse("i=46"),
            NodeId.parse("i=17740").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17725"),
            NodeId.parse("i=46"),
            NodeId.parse("i=17559").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17725"),
            NodeId.parse("i=47"),
            NodeId.parse("i=17741").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17725"),
            NodeId.parse("i=47"),
            NodeId.parse("i=17742").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17725"),
            NodeId.parse("i=15296"),
            NodeId.parse("i=17743").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17725"),
            NodeId.parse("i=47"),
            NodeId.parse("i=17812").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17725"),
            NodeId.parse("i=47"),
            NodeId.parse("i=17969").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17725"),
            NodeId.parse("i=47"),
            NodeId.parse("i=17992").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17725"),
            NodeId.parse("i=45"),
            NodeId.parse("i=14232").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode207() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=17997"),
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
            NodeId.parse("i=17997"), NodeId.parse("i=45"), NodeId.parse("i=58").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode208() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=17998"),
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
            NodeId.parse("i=17998"), NodeId.parse("i=45"), NodeId.parse("i=58").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode209() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=17999"),
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
            NodeId.parse("i=17999"),
            NodeId.parse("i=15297"),
            NodeId.parse("i=18076").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17999"),
            NodeId.parse("i=47"),
            NodeId.parse("i=21015").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17999"),
            NodeId.parse("i=47"),
            NodeId.parse("i=21080").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17999"),
            NodeId.parse("i=47"),
            NodeId.parse("i=21081").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17999"),
            NodeId.parse("i=47"),
            NodeId.parse("i=21082").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17999"),
            NodeId.parse("i=47"),
            NodeId.parse("i=21085").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=17999"),
            NodeId.parse("i=45"),
            NodeId.parse("i=14232").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode210() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=21090"),
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
            NodeId.parse("i=21090"), NodeId.parse("i=45"), NodeId.parse("i=58").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode211() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=21091"),
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
            NodeId.parse("i=21091"), NodeId.parse("i=45"), NodeId.parse("i=58").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode212() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=15298"),
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
            NodeId.parse("i=15298"),
            NodeId.parse("i=46"),
            NodeId.parse("i=21092").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15298"),
            NodeId.parse("i=46"),
            NodeId.parse("i=21093").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15298"),
            NodeId.parse("i=46"),
            NodeId.parse("i=21094").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15298"),
            NodeId.parse("i=46"),
            NodeId.parse("i=17493").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15298"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15303").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15298"),
            NodeId.parse("i=47"),
            NodeId.parse("i=21095").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15298"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15299").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15298"),
            NodeId.parse("i=47"),
            NodeId.parse("i=19550").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15298"), NodeId.parse("i=45"), NodeId.parse("i=58").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode213() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=15305"),
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
            NodeId.parse("i=15305"), NodeId.parse("i=45"), NodeId.parse("i=58").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode214() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=21096"),
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
            NodeId.parse("i=21096"), NodeId.parse("i=45"), NodeId.parse("i=58").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode215() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=15306"),
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
            NodeId.parse("i=15306"),
            NodeId.parse("i=46"),
            NodeId.parse("i=21097").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15306"),
            NodeId.parse("i=46"),
            NodeId.parse("i=21098").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15306"),
            NodeId.parse("i=46"),
            NodeId.parse("i=21099").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15306"),
            NodeId.parse("i=46"),
            NodeId.parse("i=21100").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15306"),
            NodeId.parse("i=46"),
            NodeId.parse("i=21101").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15306"),
            NodeId.parse("i=46"),
            NodeId.parse("i=21102").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15306"),
            NodeId.parse("i=46"),
            NodeId.parse("i=17563").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15306"),
            NodeId.parse("i=46"),
            NodeId.parse("i=17564").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15306"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15932").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15306"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15933").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15306"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15934").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15306"),
            NodeId.parse("i=46"),
            NodeId.parse("i=17494").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15306"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15311").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15306"),
            NodeId.parse("i=47"),
            NodeId.parse("i=21103").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15306"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15307").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15306"),
            NodeId.parse("i=47"),
            NodeId.parse("i=19609").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15306"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15316").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15306"),
            NodeId.parse("i=47"),
            NodeId.parse("i=17386").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15306"),
            NodeId.parse("i=47"),
            NodeId.parse("i=17389").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15306"), NodeId.parse("i=45"), NodeId.parse("i=58").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode216() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=15319"),
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
            NodeId.parse("i=15319"), NodeId.parse("i=45"), NodeId.parse("i=58").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode217() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=21104"),
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
            NodeId.parse("i=21104"), NodeId.parse("i=45"), NodeId.parse("i=58").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode218() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=15108"),
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
            NodeId.parse("i=15108"), NodeId.parse("i=45"), NodeId.parse("i=58").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode219() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=15111"),
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
            NodeId.parse("i=15111"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15114").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15111"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15115").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15111"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15118").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15111"),
            NodeId.parse("i=45"),
            NodeId.parse("i=15108").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode220() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=15127"),
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
            NodeId.parse("i=15127"),
            NodeId.parse("i=45"),
            NodeId.parse("i=15108").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode221() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=23795"),
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
            NodeId.parse("i=23795"),
            NodeId.parse("i=35"),
            NodeId.parse("i=23796").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23795"),
            NodeId.parse("i=47"),
            NodeId.parse("i=23807").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23795"),
            NodeId.parse("i=47"),
            NodeId.parse("i=23811").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23795"),
            NodeId.parse("i=47"),
            NodeId.parse("i=23814").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23795"),
            NodeId.parse("i=47"),
            NodeId.parse("i=23816").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23795"),
            NodeId.parse("i=47"),
            NodeId.parse("i=23819").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23795"), NodeId.parse("i=45"), NodeId.parse("i=61").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode222() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=23828"),
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
            NodeId.parse("i=23828"),
            NodeId.parse("i=47"),
            NodeId.parse("i=23829").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23828"),
            NodeId.parse("i=46"),
            NodeId.parse("i=23830").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23828"),
            NodeId.parse("i=46"),
            NodeId.parse("i=23831").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23828"), NodeId.parse("i=45"), NodeId.parse("i=58").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode223() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=14643"),
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
            NodeId.parse("i=14643"),
            NodeId.parse("i=47"),
            NodeId.parse("i=14644").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14643"),
            NodeId.parse("i=47"),
            NodeId.parse("i=14645").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14643"),
            NodeId.parse("i=47"),
            NodeId.parse("i=14646").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=14643"), NodeId.parse("i=45"), NodeId.parse("i=58").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode224() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=19677"),
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
            NodeId.parse("i=19677"),
            NodeId.parse("i=47"),
            NodeId.parse("i=19678").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19677"),
            NodeId.parse("i=47"),
            NodeId.parse("i=19679").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19677"),
            NodeId.parse("i=47"),
            NodeId.parse("i=19684").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19677"),
            NodeId.parse("i=47"),
            NodeId.parse("i=19689").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19677"),
            NodeId.parse("i=47"),
            NodeId.parse("i=19690").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19677"),
            NodeId.parse("i=47"),
            NodeId.parse("i=19691").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19677"),
            NodeId.parse("i=47"),
            NodeId.parse("i=19722").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19677"), NodeId.parse("i=45"), NodeId.parse("i=58").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode225() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=19732"),
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
            NodeId.parse("i=19732"),
            NodeId.parse("i=47"),
            NodeId.parse("i=19777").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19732"),
            NodeId.parse("i=45"),
            NodeId.parse("i=19677").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode226() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=19786"),
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
            NodeId.parse("i=19786"),
            NodeId.parse("i=47"),
            NodeId.parse("i=19831").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19786"),
            NodeId.parse("i=45"),
            NodeId.parse("i=19677").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode227() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=19834"),
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
            NodeId.parse("i=19834"),
            NodeId.parse("i=47"),
            NodeId.parse("i=19848").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19834"),
            NodeId.parse("i=47"),
            NodeId.parse("i=19879").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19834"),
            NodeId.parse("i=45"),
            NodeId.parse("i=19677").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode228() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=19903"),
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
            NodeId.parse("i=19903"),
            NodeId.parse("i=47"),
            NodeId.parse("i=19917").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19903"),
            NodeId.parse("i=47"),
            NodeId.parse("i=19948").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19903"),
            NodeId.parse("i=45"),
            NodeId.parse("i=19677").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode229() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=19968"),
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
            NodeId.parse("i=19968"),
            NodeId.parse("i=47"),
            NodeId.parse("i=19982").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19968"),
            NodeId.parse("i=47"),
            NodeId.parse("i=20013").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19968"),
            NodeId.parse("i=45"),
            NodeId.parse("i=19677").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode230() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=20027"),
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
            NodeId.parse("i=20027"),
            NodeId.parse("i=47"),
            NodeId.parse("i=20041").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=20027"),
            NodeId.parse("i=47"),
            NodeId.parse("i=20072").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=20027"),
            NodeId.parse("i=45"),
            NodeId.parse("i=19677").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode231() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=23832"),
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
            NodeId.parse("i=23832"),
            NodeId.parse("i=46"),
            NodeId.parse("i=23833").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23832"),
            NodeId.parse("i=46"),
            NodeId.parse("i=23834").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23832"),
            NodeId.parse("i=46"),
            NodeId.parse("i=23835").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23832"),
            NodeId.parse("i=46"),
            NodeId.parse("i=23836").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23832"),
            NodeId.parse("i=46"),
            NodeId.parse("i=23837").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23832"),
            NodeId.parse("i=46"),
            NodeId.parse("i=23838").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23832"),
            NodeId.parse("i=46"),
            NodeId.parse("i=32651").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23832"),
            NodeId.parse("i=46"),
            NodeId.parse("i=32844").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23832"),
            NodeId.parse("i=46"),
            NodeId.parse("i=32845").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23832"),
            NodeId.parse("i=46"),
            NodeId.parse("i=32846").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23832"),
            NodeId.parse("i=46"),
            NodeId.parse("i=32847").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23832"),
            NodeId.parse("i=46"),
            NodeId.parse("i=32652").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23832"),
            NodeId.parse("i=46"),
            NodeId.parse("i=32653").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23832"),
            NodeId.parse("i=46"),
            NodeId.parse("i=32654").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23832"),
            NodeId.parse("i=46"),
            NodeId.parse("i=32655").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23832"),
            NodeId.parse("i=46"),
            NodeId.parse("i=32848").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23832"), NodeId.parse("i=45"), NodeId.parse("i=58").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode232() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=15535"),
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
            NodeId.parse("i=15535"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15545").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15535"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15546").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15535"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15547").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15535"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2130").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode233() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=15548"),
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
            NodeId.parse("i=15548"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15561").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15548"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15562").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15548"),
            NodeId.parse("i=45"),
            NodeId.parse("i=15535").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode234() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=15563"),
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
            NodeId.parse("i=15563"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15576").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15563"),
            NodeId.parse("i=45"),
            NodeId.parse("i=15535").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode235() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=21105"),
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
            NodeId.parse("i=21105"),
            NodeId.parse("i=46"),
            NodeId.parse("i=21106").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=21105"),
            NodeId.parse("i=46"),
            NodeId.parse("i=21107").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=21105"),
            NodeId.parse("i=46"),
            NodeId.parse("i=21108").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=21105"),
            NodeId.parse("i=46"),
            NodeId.parse("i=21109").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=21105"),
            NodeId.parse("i=46"),
            NodeId.parse("i=21110").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=21105"),
            NodeId.parse("i=45"),
            NodeId.parse("i=17998").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode236() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=21111"),
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
            NodeId.parse("i=21111"),
            NodeId.parse("i=46"),
            NodeId.parse("i=21112").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=21111"),
            NodeId.parse("i=46"),
            NodeId.parse("i=21113").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=21111"),
            NodeId.parse("i=46"),
            NodeId.parse("i=21114").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=21111"),
            NodeId.parse("i=46"),
            NodeId.parse("i=21115").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=21111"),
            NodeId.parse("i=45"),
            NodeId.parse("i=21096").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode237() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=21116"),
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
            NodeId.parse("i=21116"),
            NodeId.parse("i=46"),
            NodeId.parse("i=21117").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=21116"),
            NodeId.parse("i=46"),
            NodeId.parse("i=21119").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=21116"),
            NodeId.parse("i=46"),
            NodeId.parse("i=17477").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=21116"),
            NodeId.parse("i=46"),
            NodeId.parse("i=21120").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=21116"),
            NodeId.parse("i=46"),
            NodeId.parse("i=21121").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=21116"),
            NodeId.parse("i=46"),
            NodeId.parse("i=21122").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=21116"),
            NodeId.parse("i=46"),
            NodeId.parse("i=21123").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=21116"),
            NodeId.parse("i=46"),
            NodeId.parse("i=21124").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=21116"),
            NodeId.parse("i=46"),
            NodeId.parse("i=21125").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=21116"),
            NodeId.parse("i=45"),
            NodeId.parse("i=21104").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode238() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=21126"),
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
            NodeId.parse("i=21126"),
            NodeId.parse("i=46"),
            NodeId.parse("i=21127").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=21126"),
            NodeId.parse("i=45"),
            NodeId.parse("i=17998").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode239() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=21128"),
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
            NodeId.parse("i=21128"),
            NodeId.parse("i=46"),
            NodeId.parse("i=21129").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=21128"),
            NodeId.parse("i=45"),
            NodeId.parse("i=21096").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode240() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=21130"),
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
            NodeId.parse("i=21130"),
            NodeId.parse("i=46"),
            NodeId.parse("i=21131").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=21130"),
            NodeId.parse("i=46"),
            NodeId.parse("i=21132").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=21130"),
            NodeId.parse("i=45"),
            NodeId.parse("i=21104").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode241() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=15064"),
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
            NodeId.parse("i=15064"),
            NodeId.parse("i=47"),
            NodeId.parse("i=15072").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15064"),
            NodeId.parse("i=46"),
            NodeId.parse("i=23839").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15064"),
            NodeId.parse("i=46"),
            NodeId.parse("i=23840").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15064"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25525").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15064"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25526").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15064"),
            NodeId.parse("i=45"),
            NodeId.parse("i=17721").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode242() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=21133"),
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
            NodeId.parse("i=21133"),
            NodeId.parse("i=46"),
            NodeId.parse("i=21134").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=21133"),
            NodeId.parse("i=46"),
            NodeId.parse("i=21135").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=21133"),
            NodeId.parse("i=47"),
            NodeId.parse("i=23842").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=21133"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25527").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=21133"),
            NodeId.parse("i=46"),
            NodeId.parse("i=23847").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=21133"),
            NodeId.parse("i=46"),
            NodeId.parse("i=23848").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=21133"),
            NodeId.parse("i=46"),
            NodeId.parse("i=23849").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=21133"),
            NodeId.parse("i=45"),
            NodeId.parse("i=17997").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode243() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=24016"),
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
            NodeId.parse("i=24016"),
            NodeId.parse("i=47"),
            NodeId.parse("i=24017").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24016"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25528").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24016"),
            NodeId.parse("i=46"),
            NodeId.parse("i=24022").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24016"),
            NodeId.parse("i=46"),
            NodeId.parse("i=24023").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24016"),
            NodeId.parse("i=45"),
            NodeId.parse("i=15319").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode244() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=15155"),
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
            NodeId.parse("i=15155"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15156").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15155"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15178").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=15155"),
            NodeId.parse("i=45"),
            NodeId.parse("i=17721").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode245() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=21136"),
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
            NodeId.parse("i=21136"),
            NodeId.parse("i=46"),
            NodeId.parse("i=21137").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=21136"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15246").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=21136"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15247").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=21136"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15249").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=21136"),
            NodeId.parse("i=45"),
            NodeId.parse("i=17997").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode246() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=21138"),
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
            NodeId.parse("i=21138"),
            NodeId.parse("i=46"),
            NodeId.parse("i=21139").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=21138"),
            NodeId.parse("i=46"),
            NodeId.parse("i=21140").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=21138"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15250").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=21138"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15251").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=21138"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15330").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=21138"),
            NodeId.parse("i=46"),
            NodeId.parse("i=21141").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=21138"),
            NodeId.parse("i=45"),
            NodeId.parse("i=15305").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode247() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=21142"),
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
            NodeId.parse("i=21142"),
            NodeId.parse("i=46"),
            NodeId.parse("i=21143").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=21142"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15334").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=21142"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15419").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=21142"),
            NodeId.parse("i=46"),
            NodeId.parse("i=15420").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=21142"),
            NodeId.parse("i=46"),
            NodeId.parse("i=21144").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=21142"),
            NodeId.parse("i=45"),
            NodeId.parse("i=15319").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode248() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=21145"),
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
            NodeId.parse("i=21145"),
            NodeId.parse("i=47"),
            NodeId.parse("i=21146").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=21145"), NodeId.parse("i=45"), NodeId.parse("i=58").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode249() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=21147"),
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
            NodeId.parse("i=21147"),
            NodeId.parse("i=47"),
            NodeId.parse("i=21149").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=21147"),
            NodeId.parse("i=45"),
            NodeId.parse("i=21145").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode250() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=23455"),
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
            NodeId.parse("i=23455"), NodeId.parse("i=45"), NodeId.parse("i=58").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode251() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=23456"),
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
            NodeId.parse("i=23456"),
            NodeId.parse("i=35"),
            NodeId.parse("i=23457").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23456"),
            NodeId.parse("i=35"),
            NodeId.parse("i=23458").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23456"),
            NodeId.parse("i=47"),
            NodeId.parse("i=23462").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23456"),
            NodeId.parse("i=47"),
            NodeId.parse("i=23963").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23456"),
            NodeId.parse("i=46"),
            NodeId.parse("i=32850").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23456"),
            NodeId.parse("i=47"),
            NodeId.parse("i=23972").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23456"),
            NodeId.parse("i=47"),
            NodeId.parse("i=23975").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=23456"), NodeId.parse("i=45"), NodeId.parse("i=61").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode252() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=24264"),
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
            NodeId.parse("i=24264"),
            NodeId.parse("i=46"),
            NodeId.parse("i=24265").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24264"),
            NodeId.parse("i=46"),
            NodeId.parse("i=24266").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24264"),
            NodeId.parse("i=46"),
            NodeId.parse("i=24267").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24264"),
            NodeId.parse("i=46"),
            NodeId.parse("i=24268").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24264"),
            NodeId.parse("i=47"),
            NodeId.parse("i=24269").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24264"),
            NodeId.parse("i=47"),
            NodeId.parse("i=24271").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24264"),
            NodeId.parse("i=47"),
            NodeId.parse("i=24273").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24264"),
            NodeId.parse("i=47"),
            NodeId.parse("i=24275").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24264"), NodeId.parse("i=45"), NodeId.parse("i=58").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode253() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=26871"),
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
            NodeId.parse("i=26871"),
            NodeId.parse("i=46"),
            NodeId.parse("i=26872").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=26871"),
            NodeId.parse("i=47"),
            NodeId.parse("i=26873").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=26871"),
            NodeId.parse("i=47"),
            NodeId.parse("i=26875").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=26871"),
            NodeId.parse("i=47"),
            NodeId.parse("i=26878").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=26871"), NodeId.parse("i=45"), NodeId.parse("i=58").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode254() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=24148"),
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
            NodeId.parse("i=24148"),
            NodeId.parse("i=47"),
            NodeId.parse("i=24149").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24148"),
            NodeId.parse("i=47"),
            NodeId.parse("i=24150").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24148"),
            NodeId.parse("i=47"),
            NodeId.parse("i=24151").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24148"),
            NodeId.parse("i=47"),
            NodeId.parse("i=24152").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24148"),
            NodeId.parse("i=45"),
            NodeId.parse("i=17602").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode255() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=24158"),
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
            NodeId.parse("i=24158"),
            NodeId.parse("i=47"),
            NodeId.parse("i=24159").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24158"),
            NodeId.parse("i=47"),
            NodeId.parse("i=24165").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24158"),
            NodeId.parse("i=47"),
            NodeId.parse("i=24166").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24158"),
            NodeId.parse("i=45"),
            NodeId.parse("i=17602").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode256() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=24233"),
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
            NodeId.parse("i=24233"),
            NodeId.parse("i=47"),
            NodeId.parse("i=24234").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24233"),
            NodeId.parse("i=45"),
            NodeId.parse("i=17602").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode257() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=24167"),
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
            NodeId.parse("i=24167"),
            NodeId.parse("i=47"),
            NodeId.parse("i=24168").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24167"),
            NodeId.parse("i=45"),
            NodeId.parse("i=17602").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode258() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=25218"),
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
            NodeId.parse("i=25218"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25219").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25218"),
            NodeId.parse("i=45"),
            NodeId.parse("i=17602").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode259() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=24169"),
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
            NodeId.parse("i=24169"),
            NodeId.parse("i=47"),
            NodeId.parse("i=24170").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24169"),
            NodeId.parse("i=47"),
            NodeId.parse("i=24171").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24169"),
            NodeId.parse("i=47"),
            NodeId.parse("i=24172").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24169"),
            NodeId.parse("i=45"),
            NodeId.parse("i=17602").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode260() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=24173"),
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
            NodeId.parse("i=24173"),
            NodeId.parse("i=47"),
            NodeId.parse("i=24174").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24173"),
            NodeId.parse("i=47"),
            NodeId.parse("i=24175").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24173"),
            NodeId.parse("i=47"),
            NodeId.parse("i=24176").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24173"),
            NodeId.parse("i=47"),
            NodeId.parse("i=24177").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24173"),
            NodeId.parse("i=47"),
            NodeId.parse("i=24178").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24173"),
            NodeId.parse("i=45"),
            NodeId.parse("i=17602").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode261() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=24179"),
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
            NodeId.parse("i=24179"),
            NodeId.parse("i=47"),
            NodeId.parse("i=24180").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24179"),
            NodeId.parse("i=47"),
            NodeId.parse("i=24181").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24179"),
            NodeId.parse("i=47"),
            NodeId.parse("i=24182").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24179"),
            NodeId.parse("i=45"),
            NodeId.parse("i=17602").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode262() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=24183"),
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
            NodeId.parse("i=24183"),
            NodeId.parse("i=47"),
            NodeId.parse("i=24184").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24183"),
            NodeId.parse("i=47"),
            NodeId.parse("i=24185").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24183"),
            NodeId.parse("i=47"),
            NodeId.parse("i=24186").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24183"),
            NodeId.parse("i=47"),
            NodeId.parse("i=24187").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24183"),
            NodeId.parse("i=45"),
            NodeId.parse("i=17602").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode263() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=24188"),
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
            NodeId.parse("i=24188"),
            NodeId.parse("i=47"),
            NodeId.parse("i=24189").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24188"),
            NodeId.parse("i=47"),
            NodeId.parse("i=24190").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24188"),
            NodeId.parse("i=45"),
            NodeId.parse("i=17602").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode264() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=24191"),
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
            NodeId.parse("i=24191"),
            NodeId.parse("i=47"),
            NodeId.parse("i=24194").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24191"),
            NodeId.parse("i=45"),
            NodeId.parse("i=24188").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode265() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=24195"),
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
            NodeId.parse("i=24195"),
            NodeId.parse("i=47"),
            NodeId.parse("i=24198").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24195"),
            NodeId.parse("i=45"),
            NodeId.parse("i=24188").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode266() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=24199"),
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
            NodeId.parse("i=24199"),
            NodeId.parse("i=47"),
            NodeId.parse("i=24200").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24199"),
            NodeId.parse("i=47"),
            NodeId.parse("i=24201").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24199"),
            NodeId.parse("i=45"),
            NodeId.parse("i=17602").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode267() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=24202"),
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
            NodeId.parse("i=24202"),
            NodeId.parse("i=47"),
            NodeId.parse("i=24203").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24202"),
            NodeId.parse("i=47"),
            NodeId.parse("i=24204").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24202"),
            NodeId.parse("i=45"),
            NodeId.parse("i=17602").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode268() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=24205"),
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
            NodeId.parse("i=24205"),
            NodeId.parse("i=47"),
            NodeId.parse("i=24206").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24205"),
            NodeId.parse("i=47"),
            NodeId.parse("i=24207").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24205"),
            NodeId.parse("i=47"),
            NodeId.parse("i=24208").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24205"),
            NodeId.parse("i=47"),
            NodeId.parse("i=24209").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=24205"),
            NodeId.parse("i=45"),
            NodeId.parse("i=17602").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode269() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=25221"),
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
            NodeId.parse("i=25221"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25222").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25221"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25223").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25221"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25224").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25221"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25225").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25221"),
            NodeId.parse("i=25238"),
            NodeId.parse("i=25226").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25221"),
            NodeId.parse("i=17603"),
            NodeId.parse("i=24148").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25221"), NodeId.parse("i=45"), NodeId.parse("i=58").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode270() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=25227"),
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
            NodeId.parse("i=25227"),
            NodeId.parse("i=46"),
            NodeId.parse("i=25228").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25227"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25229").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25227"),
            NodeId.parse("i=47"),
            NodeId.parse("i=25231").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=25227"), NodeId.parse("i=45"), NodeId.parse("i=58").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode271() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=18973"),
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
            NodeId.parse("i=18973"),
            NodeId.parse("i=47"),
            NodeId.parse("i=18974").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=18973"),
            NodeId.parse("i=47"),
            NodeId.parse("i=18980").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=18973"),
            NodeId.parse("i=47"),
            NodeId.parse("i=18987").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=18973"), NodeId.parse("i=45"), NodeId.parse("i=58").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode272() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=18996"),
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
            NodeId.parse("i=18996"),
            NodeId.parse("i=47"),
            NodeId.parse("i=18997").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=18996"),
            NodeId.parse("i=47"),
            NodeId.parse("i=18998").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=18996"),
            NodeId.parse("i=47"),
            NodeId.parse("i=18999").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=18996"),
            NodeId.parse("i=47"),
            NodeId.parse("i=19000").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=18996"),
            NodeId.parse("i=47"),
            NodeId.parse("i=19001").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=18996"), NodeId.parse("i=45"), NodeId.parse("i=58").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode273() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=19002"),
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
            NodeId.parse("i=19002"),
            NodeId.parse("i=46"),
            NodeId.parse("i=19003").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19002"),
            NodeId.parse("i=46"),
            NodeId.parse("i=19004").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19002"),
            NodeId.parse("i=46"),
            NodeId.parse("i=19005").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19002"),
            NodeId.parse("i=46"),
            NodeId.parse("i=19006").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19002"),
            NodeId.parse("i=46"),
            NodeId.parse("i=19007").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19002"),
            NodeId.parse("i=46"),
            NodeId.parse("i=19008").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19002"), NodeId.parse("i=45"), NodeId.parse("i=58").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode274() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=19009"),
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
            NodeId.parse("i=19009"),
            NodeId.parse("i=46"),
            NodeId.parse("i=19010").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19009"),
            NodeId.parse("i=46"),
            NodeId.parse("i=19011").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19009"),
            NodeId.parse("i=46"),
            NodeId.parse("i=19012").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19009"),
            NodeId.parse("i=46"),
            NodeId.parse("i=19013").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19009"),
            NodeId.parse("i=46"),
            NodeId.parse("i=19014").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19009"),
            NodeId.parse("i=46"),
            NodeId.parse("i=19015").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19009"),
            NodeId.parse("i=47"),
            NodeId.parse("i=19016").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19009"), NodeId.parse("i=45"), NodeId.parse("i=58").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode275() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=19033"),
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
            NodeId.parse("i=19033"),
            NodeId.parse("i=47"),
            NodeId.parse("i=19034").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19033"),
            NodeId.parse("i=47"),
            NodeId.parse("i=19035").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19033"),
            NodeId.parse("i=47"),
            NodeId.parse("i=19036").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19033"),
            NodeId.parse("i=47"),
            NodeId.parse("i=19037").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19033"),
            NodeId.parse("i=47"),
            NodeId.parse("i=19038").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19033"),
            NodeId.parse("i=47"),
            NodeId.parse("i=19039").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19033"),
            NodeId.parse("i=47"),
            NodeId.parse("i=19040").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19033"),
            NodeId.parse("i=47"),
            NodeId.parse("i=19041").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19033"),
            NodeId.parse("i=47"),
            NodeId.parse("i=19042").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19033"),
            NodeId.parse("i=47"),
            NodeId.parse("i=19043").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19033"),
            NodeId.parse("i=47"),
            NodeId.parse("i=19044").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19033"),
            NodeId.parse("i=47"),
            NodeId.parse("i=19045").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19033"),
            NodeId.parse("i=47"),
            NodeId.parse("i=19046").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19033"),
            NodeId.parse("i=47"),
            NodeId.parse("i=19047").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19033"),
            NodeId.parse("i=47"),
            NodeId.parse("i=19078").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19033"), NodeId.parse("i=45"), NodeId.parse("i=58").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode276() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=19824"),
            new QualifiedName(0, "SerializationEntityType"),
            new LocalizedText("", "SerializationEntityType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            NodeId.parse("i=19824"),
            NodeId.parse("i=47"),
            NodeId.parse("i=19825").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19824"),
            NodeId.parse("i=46"),
            NodeId.parse("i=19826").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19824"),
            NodeId.parse("i=46"),
            NodeId.parse("i=19827").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19824"),
            NodeId.parse("i=46"),
            NodeId.parse("i=19828").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19824"),
            NodeId.parse("i=46"),
            NodeId.parse("i=19829").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19824"),
            NodeId.parse("i=46"),
            NodeId.parse("i=19830").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19824"),
            NodeId.parse("i=46"),
            NodeId.parse("i=19835").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19824"),
            NodeId.parse("i=46"),
            NodeId.parse("i=19836").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19824"),
            NodeId.parse("i=46"),
            NodeId.parse("i=19837").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19824"),
            NodeId.parse("i=46"),
            NodeId.parse("i=19838").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19824"),
            NodeId.parse("i=47"),
            NodeId.parse("i=19839").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19824"), NodeId.parse("i=45"), NodeId.parse("i=58").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode277() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=19352"),
            new QualifiedName(0, "LogObjectType"),
            new LocalizedText("", "LogObjectType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            false);
    node.addReference(
        new Reference(
            NodeId.parse("i=19352"),
            NodeId.parse("i=47"),
            NodeId.parse("i=19353").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19352"),
            NodeId.parse("i=46"),
            NodeId.parse("i=19356").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19352"),
            NodeId.parse("i=46"),
            NodeId.parse("i=19357").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19352"),
            NodeId.parse("i=46"),
            NodeId.parse("i=19744").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19352"),
            NodeId.parse("i=47"),
            NodeId.parse("i=24372").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19352"), NodeId.parse("i=45"), NodeId.parse("i=58").expanded(), false));
    this.nodeManager.addNode(node);
  }

  void loadNode278() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=19362"),
            new QualifiedName(0, "BaseLogEventType"),
            new LocalizedText("", "BaseLogEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=19362"),
            NodeId.parse("i=46"),
            NodeId.parse("i=19363").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19362"),
            NodeId.parse("i=46"),
            NodeId.parse("i=19364").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19362"),
            NodeId.parse("i=46"),
            NodeId.parse("i=19365").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19362"),
            NodeId.parse("i=46"),
            NodeId.parse("i=19366").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19362"),
            NodeId.parse("i=46"),
            NodeId.parse("i=24376").expanded(),
            true));
    node.addReference(
        new Reference(
            NodeId.parse("i=19362"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2041").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode279() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=19369"),
            new QualifiedName(0, "LogOverflowEventType"),
            new LocalizedText("", "LogOverflowEventType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=19369"),
            NodeId.parse("i=45"),
            NodeId.parse("i=2041").expanded(),
            false));
    this.nodeManager.addNode(node);
  }

  void loadNode280() {
    var node =
        new UaObjectTypeNode(
            this.context,
            NodeId.parse("i=19370"),
            new QualifiedName(0, "LogEntryConditionClassType"),
            new LocalizedText("", "LogEntryConditionClassType"),
            LocalizedText.NULL_VALUE,
            UInteger.valueOf(0),
            UInteger.valueOf(0),
            null,
            null,
            new AccessRestrictionType(UShort.valueOf(0)),
            true);
    node.addReference(
        new Reference(
            NodeId.parse("i=19370"),
            NodeId.parse("i=45"),
            NodeId.parse("i=11163").expanded(),
            false));
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
  }
}
